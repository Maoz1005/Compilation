package IR;
import TEMP.*;

/**
 * Places a label
 * Usage in skeleton: AST_DEC_FUNC, AST_STMT_WHILE (two usages there, "start" and "end")
 */
public class IRcommand_Label extends IRcommand {
	public String label_name;
	
	public IRcommand_Label(String label_name) {
		this.label_name = label_name;
	}

	@Override
	public String toString() {
		return label_name + ":";
	}
}
