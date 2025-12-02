package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.Set;

public class IRcommand_GetParameterAddress extends IRcommand {
    TEMP dst;
    String id;
    int offset;

    public IRcommand_GetParameterAddress(TEMP dst, String id, int offset) {
        this.dst = dst;
        this.id = id;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return String.format("%s := address of parameter %s (offset %d)", dst, id, offset);
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.remove(dst.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().getParameterAddress(dst, offset);
    }
}
