package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Binop_Div_Integers extends IRcommand_Binop {

    public IRcommand_Binop_Div_Integers(TEMP dst, TEMP t1, TEMP t2) {
        super(dst, t1, t2);
    }

    @Override
    public String toString() {
        return dst + " := " + t1 + " / " + t2;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().divTo(dst, t1, t2);
    }
}
