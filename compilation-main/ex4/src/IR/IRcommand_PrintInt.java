package IR;
import TEMP.*;

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
		return "print " + t;
	}
}
