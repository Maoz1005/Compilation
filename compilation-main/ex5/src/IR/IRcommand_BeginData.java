package IR;

import MIPS.MIPSGenerator;

public class IRcommand_BeginData extends IRcommand{

    @Override
    public String toString() {
        return "Begin data section";
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().data();
    }
}
