package IR;
import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_NewArray extends IRcommand {

    TEMP dst;
    TEMP size;
    String typeName;

    public IRcommand_NewArray(TEMP dst, TEMP size, String typeName) {
        this.dst = dst;
        this.size = size;
        this.typeName = typeName;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here we need to remove dst and add size
        inSet.add(size.getSerialNumber());
        inSet.remove(dst.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        temps.add(size);
    }

    public String toString() {
        return dst + " := " + "newArray[" + size + "]";
    }

    public void MIPSme() {
        MIPSGenerator.getInstance().allocateArray(dst, size);
    }
}

