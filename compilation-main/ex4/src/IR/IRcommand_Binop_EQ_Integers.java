package IR;
import TEMP.*;

/**
 * Performs EQ on 2 registers (TEMPS)
 * Usage in skeleton: AST_EXP_BINOP
 */
public class IRcommand_Binop_EQ_Integers extends IRcommand_Binop {
	public IRcommand_Binop_EQ_Integers(TEMP dst,TEMP t1,TEMP t2) {
		super(dst, t1, t2);
	}
}
