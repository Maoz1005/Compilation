package IR;

import TEMP.TEMP;

public abstract class IRcommand_Binop_EQ extends IRcommand_Binop {
    public IRcommand_Binop_EQ(TEMP dst, TEMP t1, TEMP t2) {
        super(dst, t1, t2);
    }
}
