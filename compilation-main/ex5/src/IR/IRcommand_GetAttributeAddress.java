package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_GetAttributeAddress extends IRcommand_GetAddress {
    int offset;
    String class_name;

    public IRcommand_GetAttributeAddress(TEMP dst, String var_name, int offset, String class_name) {
        super(dst, var_name);
        this.class_name = class_name;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return String.format("%s := address of attribute %s (from %s offset %d)", dst, nameToLoad, class_name, offset);
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().getAttributeAddress(dst, offset);
    }
}