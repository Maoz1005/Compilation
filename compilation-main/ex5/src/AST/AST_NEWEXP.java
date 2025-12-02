package AST;

import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_INT;
import TEMP.TEMP;
import IR.*;

import java.util.Arrays;
import java.util.List;

public class AST_NEWEXP extends AST_EXP{
    public AST_TYPE type;
    public AST_EXP exp;
    public TYPE_CLASS classInfo;

    public AST_NEWEXP(AST_TYPE type, int lineNum){
        super("NewExp -> NEW type", lineNum); // new type

        this.type = type;
    }

    public AST_NEWEXP(AST_TYPE type, AST_EXP exp, int lineNum){
        super("NewExp -> NEW type LBRACE exp RBRACE", lineNum); // new type[exp]

        this.type = type;
        this.exp = exp;
    }

    public TYPE SemantMe() {
        TYPE objectType = type.SemantMe();
        if (exp == null) { // If skipped: "NEW type" is the expression. No need to evaluate exp.
            classInfo = (TYPE_CLASS) objectType;
            return objectType;
        }
        // Object is an array
        TYPE sizeType = exp.SemantMe();
        if (!(sizeType instanceof TYPE_INT)) {
            throwException("Array size should be an integer.");
        }
        else if (exp instanceof AST_EXP_INT && ((AST_EXP_INT)exp).value <= 0) {
            throwException("Array size should be a positive integer.");
        }
        return new TYPE_ARRAY(objectType.name + "[]", objectType);
    }

    public TEMP IRme() {
        TEMP dst = new TEMP();

        if (exp == null) { // Class object
            IR.getInstance().add(
                    new IRcommand_NewObject(dst, classInfo.name, classInfo.getInitialValues()));
            System.out.println("Created a class object of type: " + classInfo.name);
        }
        else { // Array object
            TEMP tempStoresSize = exp.IRme();
            IR.getInstance().add(new IRcommand_NewArray(dst, tempStoresSize, type.getName()));
            System.out.println("Created an array object of type: " + type.getName());
        }

        return dst;
    }

    @Override
    public String GetNodeName(){
        String nodename = "NEWEXP\nTYPE";
        if (exp != null) nodename += " EXP";
        return nodename;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        if (exp == null) return Arrays.asList(type);
        return Arrays.asList(type, exp);
    }

    @Override
    public boolean isNewExp() {
        return true;
    }
}
