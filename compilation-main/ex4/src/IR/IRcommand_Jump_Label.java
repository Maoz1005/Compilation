package IR;
import TEMP.*;

/**
 * Jumps to a label.
 * Usage in skeleton: AST_STMT_WHILE
*/
public class IRcommand_Jump_Label extends IRcommand_Jumptype {

	public IRcommand_Jump_Label(String label_name) {
		super(label_name);
	}

	@Override
	public String toString() {
		return "jmp " + label_name;
	}
}
