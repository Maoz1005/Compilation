package IR;
import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.Set;

/**
 * Name is pretty telling innit
 * Usage in skeleton: AST_STMT_WHILE (No IRme() on the AST_IF)
 */
public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand_Jumptype {

	TEMP t;
	
	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label_name) {
		this(t, label_name, false);
	}

	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label_name, boolean ignoreCFG){
		super(label_name, ignoreCFG);
		this.t = t;
	}

	@Override
	public String toString() {
		return super.toString() + " if " + t + " is zero";
	}

	@Override
    public void calcOut(Set<Integer> inSet) {
        // here we only need to add t to the updated out set
        inSet.add(t.getSerialNumber());
    }

	@Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(t);
    }

	@Override
	public void MIPSme()
	{
		MIPSGenerator.getInstance().beqz(t,label_name);
	}
}
