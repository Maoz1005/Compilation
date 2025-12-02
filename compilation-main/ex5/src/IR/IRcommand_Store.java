package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.Set;

// stores the value at src to the address at dst
public class IRcommand_Store extends IRcommand {
    TEMP src;
    TEMP dst;
    int offset;

    public IRcommand_Store(TEMP src, TEMP dst, int offset) {
        this.src = src;
        this.dst = dst;
        this.offset = offset;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do dst[offset] := src
        // so, add src and dst to the updated out set
        // neri - actually no i am stupid, we need to remove dst
        // neri - nvm i am actually double stupid how is that possible
        inSet.add(src.getSerialNumber());
        inSet.add(dst.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(src);
        temps.add(dst);
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().sw(src, dst, offset);
    }

    @Override
    public String toString() {
        return super.toString(String.format("t%d[%d] := t%d", dst.getSerialNumber(), offset, src.getSerialNumber()));
    }
}
