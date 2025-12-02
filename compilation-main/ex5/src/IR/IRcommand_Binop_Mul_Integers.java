package IR;
import MIPS.MIPSGenerator;
import TEMP.*;

/**
 * Performs multiplication on two registers (TEMPS)
 * Usage in skeleton: AST_EXP_BINOP
 */
public class IRcommand_Binop_Mul_Integers extends IRcommand_Binop {
	public IRcommand_Binop_Mul_Integers(TEMP dst,TEMP t1,TEMP t2) {
		super(dst, t1, t2);
	}

	@Override
	public String toString() {
		return dst + " := " + t1 + " * " + t2;
	}

	@Override
	public void MIPSme()
	{
		MIPSGenerator.getInstance().mul(dst,t1,t2);
	}

}