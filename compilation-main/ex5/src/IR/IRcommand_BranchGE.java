package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_BranchGE extends IRcommand_Jumptype {
    private TEMP t1;
    private TEMP t2;
    private String label;

    public IRcommand_BranchGE(TEMP t1, TEMP t2, String label, boolean ignoreCFG) {
        super(label, ignoreCFG);
        this.t1 = t1;
        this.t2 = t2;
        this.label = label;
    }

    public IRcommand_BranchGE(TEMP t1, TEMP t2, String label) {
        this(t1, t2, label, false);
    }

    @Override
    public String toString() {
        return super.toString() + " if " + t1 + " >= " + t2;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // In branchGE, we have: if (t1 >= t2) goto label
        // so, we only need to add t1 and t2 to the updated out set
        inSet.add(t1.getSerialNumber());
        inSet.add(t2.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(t1);
        temps.add(t2);
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().bge(t1, t2, label);
    }
}
