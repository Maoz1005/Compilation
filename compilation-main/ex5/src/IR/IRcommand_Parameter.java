package IR;

import SYMBOL_TABLE.METADATA;

/**
 * Useless, only for logging
 */
public class IRcommand_Parameter extends IRcommand {

    String var_name;
    int offset;

    public IRcommand_Parameter(String var_name, int offset) {
        this.var_name = var_name;
        this.offset = offset;
    }

    public String toString() {
        return super.toString("parameter " + var_name,
                "name: " + var_name + ", offset: " + offset + ", role: " + METADATA.VAR_ROLE.PARAMETER);
    }


    public void MIPSme() {

    }
}
