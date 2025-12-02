package IR;

import MIPS.MIPSGenerator;

// the first command to run, writes basic builtins
public class IRcommand_GenerateBuiltinFuncs extends IRcommand{

    @Override
    public void MIPSme() {
        generateErrorSections();
        MIPSGenerator.getInstance().builtInFuncs();
    }

    public static void generateErrorSections(){
        MIPSGenerator mips = MIPSGenerator.getInstance();

        mips.errorSection(IRPatterns.INVALID_DEREF_LABEL, "string_invalid_ptr_dref");
        mips.errorSection(IRPatterns.DIV_BY_ZERO_LABEL, "string_illegal_div_by_0");
        mips.errorSection(IRPatterns.OUT_OF_BOUNDS_LABEL, "string_access_violation");
    }

    @Override
    public String toString() {
        return "generate all builtins (error sections, string functions, printing)";
    }
}
