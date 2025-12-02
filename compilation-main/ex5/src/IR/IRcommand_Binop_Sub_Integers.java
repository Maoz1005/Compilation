package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

/**
 * Performs addition on two registers (TEMPS)
 * Usage in skeleton: EXP_BINOP
 */
public class IRcommand_Binop_Sub_Integers extends IRcommand_Binop {
	public IRcommand_Binop_Sub_Integers(TEMP dst, TEMP t1, TEMP t2) {
		super(dst, t1, t2);
	}

	@Override
	public String toString() {
		return dst + " := " + t1 + " - " + t2;
	}
	public void MIPSme() {
		MIPSGenerator.getInstance().sub(dst, t1, t2);
	}
}
