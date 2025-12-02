package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;
import IR.*;
import UTILITY.Pair;

import java.util.Arrays;
import java.util.List;

public class AST_VARDEC extends AST_DEC {
    public AST_TYPE typeNode;
    public String id;
    public AST_EXP exp;
    public METADATA metadata;

    public AST_VARDEC(AST_TYPE typeNode, String id, int lineNum){
        super("varDec -> type ID SEMICOLON", lineNum); // int x;
        this.typeNode = typeNode;
        this.id = id;
    }

    // this also accepts newExp (since it inherits from exp) - surely this will have no consequences whatsoever
    public AST_VARDEC(AST_TYPE typeNode, String id, AST_EXP exp, int lineNum){
        super("varDec -> type ID ASSIGN exp SEMICOLON", lineNum); // int x := exp;
        this.typeNode = typeNode;
        this.id = id;
        this.exp = exp;
    }


    public TYPE SemantMe() {
        TYPE vartype = typeNode.SemantMe();
        if (vartype instanceof TYPE_VOID) throwException("Cannot declare void type variables.");

        constructVariableMetadata();

        tryTableEnter(id, vartype, metadata);

        TYPE exptype = null;

        if (exp != null) exptype = exp.SemantMe();

        if (metadata.getRole() == METADATA.VAR_ROLE.GLOBAL){
            InitialConstVal init = new InitialConstVal(exp);
            Pair<String, InitialConstVal> global = new Pair<>(id, init);
            AST_PROGRAM.addGlobal(global);
        }

        if (exptype == null) return vartype;

        if (exptype.equals(vartype)) {
            return vartype;
        }
        if (vartype.isArray()){
            arraySemantCheck((TYPE_ARRAY)vartype, exptype);
            return vartype;
        }
        if (vartype instanceof TYPE_CLASS){
            classSemantCheck((TYPE_CLASS)vartype, exptype);
            return vartype;
        }

        throwException("Type mismatch");
        return null; // never reaches this point
    }

    /**
     * Example int x = 42:
     * <allocate variable x>
     * <it was assigned a value>: x = 42
     */
    public TEMP IRme() {
        IR ir = IR.getInstance();
        // only interests us if it is local and has an initial assignment, since all others are handled elsewhere
        if (metadata.getRole() == METADATA.VAR_ROLE.LOCAL && exp != null) {
            TEMP tempExp = exp.IRme();
            TEMP dst = new TEMP();
            ir.add(new IRcommand_GetLocalAddress(dst, id, metadata.getOffset()));
            ir.add(new IRcommand_Store(tempExp, dst, 0));
        }
        return null;
    }

    private void constructVariableMetadata() {
        this.metadata = new METADATA();
        try { // Parameters aren't a VARDEC!
            metadata.setAsVariable();
            if (SYMBOL_TABLE.getInstance().inGlobalScope()) {
                metadata.setGlobal();
                System.out.println("Found global variable: " + id);
            }
            else if (SYMBOL_TABLE.getInstance().getScopeCounter() == 1 && SYMBOL_TABLE.getInstance().currentClass != null) {
                metadata.setAttribute();
                metadata.setOffset(++attributeCounter);
                metadata.setClassName(SYMBOL_TABLE.getInstance().currentClass.name);
                System.out.println("Found attribute: " + id);
            }
            else {
                vardecCounter++;
                metadata.setLocal();
                metadata.setOffset(vardecCounter);
                System.out.println("Found local variable: " + id + " with offset: " + vardecCounter);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void arraySemantCheck(TYPE_ARRAY leftArr, TYPE rightType){
        if (!(rightType.isArray())) {
            if(!(rightType instanceof TYPE_NIL)) // nil is valid to array
                throwException("expression must be of array type");
            else return;
        }

        TYPE_ARRAY rightArr = (TYPE_ARRAY)rightType;
        if (!leftArr.typeOfElements.equals(rightArr.typeOfElements))
            throwException("Assignment of differing array types");
    }

    private void classSemantCheck(TYPE_CLASS leftClass, TYPE rightType){
        if (!(rightType instanceof TYPE_CLASS)) {
            if(!(rightType instanceof TYPE_NIL)) // nil is valid to class
                throwException("expression must be of class type");
            else return;
        }

        TYPE_CLASS rightClass = (TYPE_CLASS) rightType;
        if (!rightClass.isSubtypeOf(leftClass))
            throwException("Expression does not inherit from variable's class");
    }


    @Override
    public String GetNodeName(){
        return String.format("VARDEC\nTYPE ID( %s )", id);
    }

    @Override
    public List<? extends AST_Node> GetChildren(){
        if (exp == null) return Arrays.asList(typeNode);
        return Arrays.asList(typeNode, exp);
    }
}
