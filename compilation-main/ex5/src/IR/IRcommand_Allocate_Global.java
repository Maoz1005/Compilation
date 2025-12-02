package IR;

import MIPS.MIPSGenerator;
import SYMBOL_TABLE.METADATA.*;

// IMPORTANT: You may assume that if a global variable is initialized, then the initial value is a constant
// (i.e., string, integer, nil).
// When initializing global variables, the order should be the same as the original
// Should be evaluated before calling "main"

import TEMP.*;

/** Declaration of a global variable.
 * If it is a string, also defines a string constant.*/
public class IRcommand_Allocate_Global extends IRcommand {

    public String var_name;
    public String initVal;
    public boolean isStringVal;

    public IRcommand_Allocate_Global(String var_name, String initVal, boolean isStringVal) {
        this.var_name = var_name;
        this.initVal = initVal;
        this.isStringVal = isStringVal;
    }

    @Override
    public String toString() {
        return super.toString("AllocateGlobal(" + var_name + "), set to " + initVal,
                "name: " + var_name + ", role: " + VAR_ROLE.GLOBAL);
    }

    public void MIPSme() {
        MIPSGenerator.getInstance().allocateGlobal(var_name, initVal, isStringVal);
    }
}
