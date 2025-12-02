package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_BranchLTZ extends IRcommand_Jumptype {
    TEMP t1;


    public IRcommand_BranchLTZ(TEMP t1, String label_name) {
        super(label_name);
        this.t1 = t1;
    }

    public IRcommand_BranchLTZ(TEMP t1, String label_name,  boolean ignoreCFG) {
        super(label_name, ignoreCFG);
        this.t1 = t1;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.add(t1.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(t1);
    }

    @Override
    public String toString() {
        return super.toString() + " if "  + t1 + " < 0";
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().bltz(t1, label_name);
    }
}
