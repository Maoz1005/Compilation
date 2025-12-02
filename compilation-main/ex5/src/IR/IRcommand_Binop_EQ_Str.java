package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Binop_EQ_Str extends IRcommand_Binop_EQ{

    public IRcommand_Binop_EQ_Str(TEMP dst, TEMP t1, TEMP t2) {
        super(dst, t1, t2);
    }

    @Override
    public String toString() {
        return String.format("t%d := (t%d = t%d) (strings)", dst.getSerialNumber(), t1.getSerialNumber(), t2.getSerialNumber());
    }

    public void MIPSme() {
        String compare = getFreshLabel("compare_loop");
        String not_equal = getFreshLabel("not_equal");
        String end = getFreshLabel("end");

        MIPSGenerator.getInstance().compareStr(dst, t1, t2, compare, not_equal, end); // god has created me to write stupid dumb code
    }

}
