package IR;
import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.Set;

/**
 * Assign a constant to a temporary: t1 = 42.
 * Usage in skeleton: AST_EXP_INT
 */
public class IRcommand_ConstInt extends IRcommand {
	public TEMP t;
	int value;
	
	public IRcommand_ConstInt(TEMP t, int value) {
		this.t = t;
		this.value = value;
	}

	@Override
	public String toString() {
		return t + " := " + value;
	}

	@Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do t := value, so we need to remove t from the updated out set
        inSet.remove(t.getSerialNumber());
    }

	@Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(t);
    }

	public void MIPSme()
	{
		MIPSGenerator.getInstance().li(t,value);
	}
}
