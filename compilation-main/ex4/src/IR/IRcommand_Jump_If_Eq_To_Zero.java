package IR;
import TEMP.*;

/**
 * Name is pretty telling innit
 * Usage in skeleton: AST_STMT_WHILE (No IRme() on the AST_IF)
 */
public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand_Jumptype {

	TEMP t;
	
	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label_name) {
		super(label_name);
		this.t = t;
	}

	@Override
	public String toString() {
		return "jmp " + label_name + " if " + t + " is zero";
	}
}
