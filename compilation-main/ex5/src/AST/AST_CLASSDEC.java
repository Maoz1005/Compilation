package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
import UTILITY.*;
import TEMP.TEMP;
import IR.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AST_CLASSDEC extends AST_DEC {

    public String id;
    public String superclass;
    public List<AST_CFIELD> fields;
    public TYPE_CLASS typeObject; // Helps us find the methods and attributes easily.


    public AST_CLASSDEC(String id, String superclass, NODE_LIST<AST_CFIELD> fields, int lineNum){
        super("CLASS ID EXTENDS ID LBRACE cField (cField)* RBRACE", lineNum);

        this.id = id;
        this.superclass = superclass;
        this.fields = fields.unroll();
    }


    public AST_CLASSDEC(String id, NODE_LIST<AST_CFIELD> fields, int lineNum){
        super("CLASS ID EXTENDS ID LBRACE cField (cField)* RBRACE", lineNum);

        this.id = id;
        this.fields = fields.unroll();
    }


    @Override
    protected String GetNodeName() {
        return String.format("CLASS %s%s\nCFIELDS", id, (superclass != null ? " EXTENDS " + superclass : ""));
    }


    @Override
    protected List<? extends AST_Node> GetChildren() {
        return fields;
    }

    @Override
    public TYPE SemantMe() {
        SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
        if (!symbolTable.inGlobalScope()) throwException("Class not defined in global scope");

        // Find father
        List<TYPE_CLASS_MEMBER_DEC> members = new ArrayList<>();
        TYPE_CLASS father = null;
        if (superclass != null) {
            TYPE found = tryTableFind(this.superclass);
            if (!(found instanceof TYPE_CLASS)) { throwException(found.name + " is not a class"); }
            father = (TYPE_CLASS)found;
            members.addAll(father.data_members);
        }

        System.out.println("\nClass name: " + this.id);
        TYPE_CLASS typeClass = new TYPE_CLASS(father, id, members);
        tryTableEnter(id, typeClass); // Have to add early - for classes which contain their own type as attributes.

        // Scoping
        symbolTable.currentClass = typeClass;
        symbolTable.beginScope();

        convertNodesToMembers(typeClass, this.fields, members); // SemantMe
        this.typeObject = typeClass;
        this.initMembersList();

        symbolTable.endScope();
        symbolTable.currentClass = null;

        // Final
        System.out.println("\nClass " + this.id + ":" +
                "\nhas a method list:\n" +
                Arrays.toString(typeObject.methodsInfo.toArray()) +
                "\nAnd an attribute list:\n" +
                Arrays.toString(typeObject.attributes.toArray())
        );
        AST_PROGRAM.addClass(this);
        return typeClass;
    }

    public TEMP IRme() {
        for (AST_CFIELD field : fields) {
             field.IRme();
        }

        return null;
    }


    private void convertNodesToMembers(TYPE_CLASS mytype, List<AST_CFIELD> cFields, List<TYPE_CLASS_MEMBER_DEC> members){
        SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();
        TYPE memberType = null;

        attributeCounter = 0;
        for (TYPE_CLASS_MEMBER_DEC member : members) { // Account for inherited attributes
            if (!(member.t instanceof TYPE_FUNCTION)) { attributeCounter++; }
        }

        for (AST_CFIELD cField : cFields) {
            String memberID = null;
            if (cField instanceof AST_CFIELD_FUNC field) {
                memberType = cField.SemantMe();
                memberID = field.funcDec.id;
                TYPE found = table.findMemberType(mytype, memberID);
                if (found != null && !found.equals(memberType)) cField.throwException("overriding method with differing types");
            }
            else if (cField instanceof AST_CFIELD_VAR field) {
                /* Mandatory note:
                *  There's some jank here. While in class we'll have to separate instances of the same variable. Once
                *  as a TYPE_CLASS_MEMBER and another as a concrete value in the Symbol Table. This shouldn't be a
                *  problem, and it is fixable, but honestly it really shouldn't matter at all.
                */
                memberType = field.SemantMe();
                memberID = field.varDec.id;
                TYPE found = table.findMemberType(mytype, memberID);
                if (found != null) cField.throwException("Field already defined in superclass");
            }
            else { throwException("cField in class declaration is not a function or a variable."); }

            METADATA metadata = tryTableFindMetadata(memberID); // Fetch the member metadata you just Semanted.
            TYPE_CLASS_MEMBER_DEC member = new TYPE_CLASS_MEMBER_DEC(memberType, memberID, metadata);
            if (member.metadata.isVariable())
                System.out.println(
                        "Class: " + member.metadata.getClassName() + ", defined an attribute: " + memberID + ", with an offset: " + member.metadata.getOffset()
                );

            members.add(member);
        }
    }

    /*
    ####################################################################################################
    TODO: Delete this comment and check out the note in TYPE_CLASS before attempting to understand this!
    ####################################################################################################
     */
    /**
     * Initializes the method-list and attribute-list for a non-elder class.
     */
    private void initMembersList() {
        // Initialization - Deep copy fathers lists
        if (typeObject.father != null) { deepCopyFather();}

        for (TYPE_CLASS_MEMBER_DEC member : typeObject.data_members) {
            if (typeObject.father != null && typeObject.father.data_members.contains(member)) { continue; }
            if (member.t instanceof TYPE_FUNCTION) {
                if (typeObject.father == null) { // Eldest class, can't inherit list
                    typeObject.methodsInfo.add(new Pair<>(typeObject, (TYPE_FUNCTION)member.t));
                }
                else {
                    insertAndHandleOverride(member);
                }
            }
            else { typeObject.attributes.addLast(member.name); }
        }
    }

    /**
     * Add a method ot its list, and handle overriding accordingly.
     * @param member The method to add.
     */
    private void insertAndHandleOverride(TYPE_CLASS_MEMBER_DEC member) {
        TYPE memberType = member.t;
        String memberName = member.name;
        Pair<TYPE_CLASS, TYPE_FUNCTION> pairToAdd = new Pair<>(typeObject, (TYPE_FUNCTION) memberType);
        boolean added = false;
        for (int i = 0; i < typeObject.methodsInfo.size(); i++) {
            Pair<TYPE_CLASS, TYPE_FUNCTION> methodInfo = typeObject.methodsInfo.get(i);
            if (memberName.equals(methodInfo.second.name)) { // isOverride
                typeObject.methodsInfo.set(i, pairToAdd);
                added = true;
                System.out.println("Class: " + this.id + ", has replaced: " + memberName + ", with its own method.");
            }
        }
        if (!added) {
            typeObject.methodsInfo.addLast(pairToAdd);
        }
    }

    /** Copies father as well as the pairs within it. */
    private void deepCopyFather() {
        typeObject.methodsInfo = new ArrayList<>();
        for (Pair<TYPE_CLASS, TYPE_FUNCTION> methodInfo : typeObject.father.methodsInfo) {
            typeObject.methodsInfo.add(new Pair<>(methodInfo.first, methodInfo.second));
        }
        typeObject.attributes = new ArrayList<>(typeObject.father.attributes);
    }
}
