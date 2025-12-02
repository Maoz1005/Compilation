package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_GetGlobalAddress extends IRcommand_GetAddress {

    public IRcommand_GetGlobalAddress(TEMP dst, String var_name) {
        super(dst, var_name);
    }

    public void MIPSme() {
        MIPSGenerator.getInstance().getGlobalAddress(dst, nameToLoad);
    }


    @Override
    public String toString() {
        return String.format("%s := address of global %s", dst, nameToLoad);
    }

}
