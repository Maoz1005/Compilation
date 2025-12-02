package IR;
import TEMP.*;

import java.util.Set;

public abstract class IRcommand_Binop extends IRcommand {

    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop(TEMP dst,TEMP t1,TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // In binop, we have: dst := t1 op t2
        // so, we need to remove dst, and add t1 and t2 to the updated out set
        inSet.remove(dst.getSerialNumber());
        inSet.add(t1.getSerialNumber());
        inSet.add(t2.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        temps.add(t1);
        temps.add(t2);
    }
}
