package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_GetLocalAddress extends IRcommand_GetAddress{
    int offset;

    public IRcommand_GetLocalAddress(TEMP dst, String id, int offset) {
        super(dst, id);
        this.offset = offset;
    }

    @Override
    public String toString() {
        return String.format("%s := address of local %s (offset %d)", dst, nameToLoad, offset);
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().getLocalAddress(dst, offset);
    }

}
