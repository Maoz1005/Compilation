package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_Move extends IRcommand{
    private TEMP dst;
    private TEMP src;

    public IRcommand_Move(TEMP dst, TEMP src) {
        this.dst = dst;
        this.src = src;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().move(dst, src);
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do dst := src
        // so, remove dst and add src
        inSet.add(src.getSerialNumber());
        inSet.remove(dst.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        temps.add(src);
    }

    @Override
    public String toString() {
        return String.format("t%d := t%d", dst.getSerialNumber(), src.getSerialNumber());
    }
}
