package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_CLASS_MEMBER_DEC;
import TYPES.TYPE_CLASS_VAR_DEC_LIST;

import java.util.ArrayList;
import java.util.List;

public class AST_CLASSDEC extends AST_DEC{
    public String id;
    public String superclass;
    public List<AST_CFIELD> fields;

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


    public TYPE SemantMe() { // TODO: verify. I lack confidence about this
        SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
        if (!symbolTable.inGlobalScope()) throwException("Class not defined in global scope");

        List<TYPE_CLASS_MEMBER_DEC> members = new ArrayList<>();
        TYPE_CLASS father = null;
        if (superclass != null) {
            TYPE found = tryTableFind(this.superclass);
            if (!(found instanceof TYPE_CLASS)) { throwException(found.name + " is not a class"); }
            father = (TYPE_CLASS)found;
        }

        TYPE_CLASS typeClass = new TYPE_CLASS(father, id, members);
        tryTableEnter(id, typeClass);
        symbolTable.currentClass = typeClass;

        symbolTable.beginScope();
        convertNodesToMembers(typeClass, this.fields, members);
        symbolTable.endScope();

        symbolTable.currentClass = null;
        return typeClass;
    }

    private void convertNodesToMembers(TYPE_CLASS mytype, List<AST_CFIELD> cFields, List<TYPE_CLASS_MEMBER_DEC> members) {
        SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();
        for (AST_CFIELD cField : cFields) {
            TYPE memberType = cField.SemantMe();
            String memberID = null;
            if (cField instanceof AST_CFIELD_FUNC) {
                memberID = ((AST_CFIELD_FUNC)cField).funcDec.id;
                TYPE found = table.findMemberType(mytype, memberID);
                if (found != null && !found.equals(memberType)) cField.throwException("overriding method with differing types");
            }
            else if (cField instanceof AST_CFIELD_VAR) {
                memberID = ((AST_CFIELD_VAR)cField).varDec.id;
                TYPE found = table.findMemberType(mytype, memberID);
                if (found != null) cField.throwException("Field already defined in superclass");
            }
            else { throwException("cField in class declaration is not a function or a variable."); }
            TYPE_CLASS_MEMBER_DEC member = new TYPE_CLASS_MEMBER_DEC(memberType, memberID);
            members.add(member);
        }
    }
}
