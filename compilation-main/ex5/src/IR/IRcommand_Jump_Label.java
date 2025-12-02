package IR;
import MIPS.MIPSGenerator;
import TEMP.*;

/**
 * Jumps to a label.
 * Usage in skeleton: AST_STMT_WHILE
*/
public class IRcommand_Jump_Label extends IRcommand_Jumptype {

	public IRcommand_Jump_Label(String label_name) {
		super(label_name);
	}

	public IRcommand_Jump_Label(String label_name, boolean ignoreCFG){
		super(label_name, ignoreCFG);
	}

	public void MIPSme()
	{
		MIPSGenerator.getInstance().jump(label_name);
	}
}
