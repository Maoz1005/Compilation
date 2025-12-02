package IR;
import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.Set;

/**
 * FUCK MY LIFE THIS IS LITERALLY A "PRINT" COMMAND HOW COULD I POSSIBLY GUESS THAT WKJDABNGOKADSBGHJDASBFPDS
 */
public class IRcommand_PrintInt extends IRcommand {
	TEMP t;
	
	public IRcommand_PrintInt(TEMP t)
	{
		this.t = t;
	}

	@Override
	public String toString() {
		return "PrintInt(" + t + ")";
	}

	@Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do print t, so add t
        inSet.add(t.getSerialNumber());
    }

	@Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(t);
    }

	public void MIPSme()
	{
		MIPSGenerator.getInstance().printInt(t);
	}
}
