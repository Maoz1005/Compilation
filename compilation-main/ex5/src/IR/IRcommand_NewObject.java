package IR;
import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.List;
import java.util.Set;

public class IRcommand_NewObject extends IRcommand{

    TEMP dst;
    String className;
    List<InitialConstVal> initialVals;

    public IRcommand_NewObject(TEMP dst, String className, List<InitialConstVal> initialVals) {
        this.dst = dst;
        this.className = className;
        this.initialVals = initialVals;
    }

    public String toString() {
        return dst + " := " + "newClassObject(" + className + ")";
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here we need to remove dst
        inSet.remove(dst.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
    }

    public void MIPSme() {
        MIPSGenerator.getInstance().initializeObject(dst, "vtable_" + className, initialVals);
    }
}
