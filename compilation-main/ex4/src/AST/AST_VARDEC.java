package AST;

import TYPES.*;
import TEMP.*;
import IR.*;

import java.util.Arrays;
import java.util.List;

public class AST_VARDEC extends AST_DEC {
    public AST_TYPE typeNode;
    public String id;
    public AST_EXP exp;

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

    @Override
    public TYPE SemantMe() {
        TYPE vartype = typeNode.SemantMe();
        if (vartype instanceof TYPE_VOID) throwException("Cannot declare void type variables.");

        tryTableEnter(id, vartype);

        if (exp == null) return vartype;

        TYPE exptype = exp.SemantMe();

        if (exptype.equals(vartype)) return vartype;

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

    /**
     * Example int x = 42:
     * <allocate variable x>
     * <it was assigned a value>: x = 42
     */
    public TEMP IRme() {
        IR.getInstance().Add_IRcommand(new IRcommand_Allocate(id));

        if (exp != null) {
            IR.getInstance().Add_IRcommand(new IRcommand_Store(id, exp.IRme()));
        }
        return null;
    }
}
