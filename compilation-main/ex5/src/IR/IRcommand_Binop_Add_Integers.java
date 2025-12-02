package IR;
import MIPS.MIPSGenerator;
import TEMP.*;

/**
 * Performs addition on two registers (TEMPS)
 * Usage in skeleton: EXP_BINOP
 */
public class IRcommand_Binop_Add_Integers extends IRcommand_Binop {
	public IRcommand_Binop_Add_Integers(TEMP dst,TEMP t1,TEMP t2) {
		super(dst, t1, t2);
	}

	@Override
	public String toString() {
		return dst + " := " + t1 + " + " + t2 + "\t (Integers)";
	}

	@Override
	public void MIPSme(){
		MIPSGenerator.getInstance().add(dst, t1, t2);
	}

}
