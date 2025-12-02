package IR;

import AST.AST_EXP;
import AST.AST_EXP_INT;
import AST.AST_EXP_STRING;

// holds information about the initial constant value that a variable gets on declaration
public class InitialConstVal {
    private String value = "0";
    private boolean isString = false;

    public String getValue() {
        return value;
    }

    public boolean isString() {
        return isString;
    }

    public InitialConstVal(AST_EXP exp){
        if (exp == null) return; // empty initialization

        if (!exp.isConstant()) throw new RuntimeException("Tried to initialize variable with non-const value");

        if (exp instanceof AST_EXP_INT eint) value = String.valueOf(eint.value);
        else if (exp instanceof AST_EXP_STRING estr) {
            value = estr.value;
            isString = true;
        }
        // else must be nil, default values
    }
}
