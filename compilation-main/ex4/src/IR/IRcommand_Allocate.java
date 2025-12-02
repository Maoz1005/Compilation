package IR;

import TEMP.*;

/**
 * Responsible for defining variables: x = t1
 * Used when defining a fresh variable in L-code: int x = y
 * Usage in skeleton: VARDEC
 */
public class IRcommand_Allocate extends IRcommand {
	public String var_name;
	
	public IRcommand_Allocate(String var_name) {
		this.var_name = var_name;
	}

	@Override
	public String toString() {
		return "allocate(" + var_name + ")";
	}
}
