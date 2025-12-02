package IR;
import TEMP.*;

/**
 * This is an IR command for assignment. We load a new value into a variable: x = t1 I think.
 * Usage in skeleton: AST_VAR_DEC, AST_STMT_ASSIGN
 * Clarification: With "Allocate" we define a new variable, here we store into an existing one.
 * (I'm so sorry if this is nonsense but this is how I made sense of the commands)
 */
public class IRcommand_Store extends IRcommand {
	public String var_name;
	public TEMP src;
	
	public IRcommand_Store(String var_name,TEMP src) {
		this.src      = src;
		this.var_name = var_name;
	}

	@Override
	public String toString() {
		return var_name +  " := " + src;
	}
}
