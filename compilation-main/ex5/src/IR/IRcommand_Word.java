package IR;

import MIPS.MIPSGenerator;

public class IRcommand_Word extends IRcommand{
    String pointer;

    public IRcommand_Word(String initialVal) {
        this.pointer = initialVal;
    }

    public IRcommand_Word(){
        pointer = null;
    }

    @Override
    public String toString() {
        return ".word " + (pointer != null ? pointer : null);
    }

    @Override
    public void MIPSme() {
        if (pointer != null) MIPSGenerator.getInstance().word(pointer);
        else MIPSGenerator.getInstance().word();
    }
}
