package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_SLL extends IRcommand {
    TEMP dst;
    TEMP src;
    int shift;

    public IRcommand_SLL(TEMP dst, TEMP src, int shift) {
        this.dst = dst;
        this.src = src;
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "dst := src << 2";
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        temps.add(src);
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.remove(dst.getSerialNumber());
        inSet.add(src.getSerialNumber());
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().sll(dst, src, shift);
    }
}
