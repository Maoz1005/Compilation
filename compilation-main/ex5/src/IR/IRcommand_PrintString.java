package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.Set;

public class IRcommand_PrintString extends IRcommand{
    TEMP addr;

    public IRcommand_PrintString(TEMP addr){
        this.addr = addr;
    }

    
	@Override
    public void calcOut(Set<Integer> inSet) {
        inSet.add(addr.getSerialNumber());
    }

	@Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(addr);
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().printStr(addr);
    }

    @Override
    public String toString() {
        return String.format("PrintString(%s)", addr);
    }
}
