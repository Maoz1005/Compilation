package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_GetConstStrAddress extends IRcommand_GetAddress{

    public IRcommand_GetConstStrAddress(TEMP dst, String value) {
        super(dst, value);
    }

    @Override
    public String toString() {
        return String.format("%s := the address of the constant string \"%s\"", dst, nameToLoad);
    }

    public void MIPSme() {
        MIPSGenerator mips = MIPSGenerator.getInstance();

        mips.loadStringConst(dst, nameToLoad);
    }
}
