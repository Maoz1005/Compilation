package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_NIL;

public class AST_EXP_NIL extends AST_EXP {
    public AST_EXP_NIL(int lineNum) {
        super("exp -> NIL", lineNum);
    }

    public TYPE SemantMe() {
        return TYPE_NIL.getInstance();
    }

    @Override
    protected String GetNodeName() {
        return "NIL";
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
