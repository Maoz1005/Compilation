package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_NIL;
import TEMP.TEMP;
import IR.*;

public class AST_EXP_NIL extends AST_EXP {
    public AST_EXP_NIL(int lineNum) {
        super("exp -> NIL", lineNum);
    }

    public TYPE SemantMe() {
        return TYPE_NIL.getInstance();
    }

    public TEMP IRme() {
        TEMP dst = new TEMP();
        IR.getInstance().add(new IRcommand_ConstInt(dst, 0));
        return dst;
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
