package IR;

import MIPS.MIPSGenerator;

public class IRcommand_BeginText extends IRcommand{
    @Override
    public String toString() {
        return "Begin text section";
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().text();
    }
}
