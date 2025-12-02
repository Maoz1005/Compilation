package IR;
import TEMP.*;

/**
 * Used to assign a register (TEMP) to a variable: t1 = x
 * Usage in skeleton: AST_EXP_VAR_SIMPLE (literally just "varname")
 * Clarification: Allocate is for x = t1, this is for t1 = x:
 * Upon reading a value, we put it into a register. Upon writing to a variable, we do this "load" command. This is the
 * semantic differnece.
 */
public class IRcommand_Load extends IRcommand {
	public TEMP dst;
	public String var_name;
	
	public IRcommand_Load(TEMP dst,String var_name) {
		this.dst      = dst;
		this.var_name = var_name;
	}

	@Override
	public String toString() {
		return dst + " := " + var_name;
	}
}
