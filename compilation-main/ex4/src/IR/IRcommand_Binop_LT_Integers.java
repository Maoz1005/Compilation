package IR;
import TEMP.*;

/**
 * Checks for LT(<) equality on two registers (TEMPS)
 * Usage in skeleton: AST_EXP_BINOP
*/
public class IRcommand_Binop_LT_Integers extends IRcommand_Binop {
	public IRcommand_Binop_LT_Integers(TEMP dst,TEMP t1,TEMP t2) {
		super(dst, t1, t2);
	}
}