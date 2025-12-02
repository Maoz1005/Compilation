package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_STRING;

public class AST_EXP_STRING extends AST_EXP {
    public String value;

    public AST_EXP_STRING(String value, int lineNum) {
        super(String.format("exp -> STRING( %s )", value), lineNum);

        this.value = value;
    }

    public TYPE SemantMe() {
        return TYPE_STRING.getInstance();
    }

    @Override
    protected String GetNodeName() {
        return String.format("EXP\nSTRING( %s )", value);
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
