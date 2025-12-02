package AST;

import TYPES.TYPE;
import TYPES.TYPE_STRING;
import TEMP.TEMP;
import IR.*;

public class AST_EXP_STRING extends AST_EXP {
    public String value;

    public AST_EXP_STRING(String value, int lineNum) {
        super(String.format("exp -> STRING( %s )", value), lineNum);

        this.value = value.substring(1, value.length() - 1); // cut off the " " at the ends
        AST_PROGRAM.addStringConstant(this.value);
    }

    public TYPE SemantMe() {
        return TYPE_STRING.getInstance();
    }

    public TEMP IRme() {
        TEMP temp = new TEMP();
        IR.getInstance().add(new IRcommand_GetConstStrAddress(temp, value));
        return temp;
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
