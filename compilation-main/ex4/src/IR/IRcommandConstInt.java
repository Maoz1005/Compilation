package IR;
import TEMP.*;

/**
 * Assign a constant to a temporary: t1 = 42.
 * Usage in skeleton: AST_EXP_INT
 */
public class IRcommandConstInt extends IRcommand {
	public TEMP t;
	int value;
	
	public IRcommandConstInt(TEMP t,int value) {
		this.t = t;
		this.value = value;
	}

	@Override
	public String toString() {
		return t + " := constval " + value;
	}
}
