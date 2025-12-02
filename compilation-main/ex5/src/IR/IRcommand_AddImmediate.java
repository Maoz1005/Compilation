package IR;

import MIPS.MIPSGenerator;
import TEMP.*;

import java.util.Set;

public class IRcommand_AddImmediate extends IRcommand {
    TEMP dst;
    TEMP op1;
    int immediate;

    public IRcommand_AddImmediate(TEMP dst, TEMP op1, int immediate){
        this.dst = dst;
        this.op1 = op1;
        this.immediate = immediate;
    }

    public IRcommand_AddImmediate(TEMP dst, int immediate){
        this.dst = dst;
        this.op1 = null;
        this.immediate = immediate;
    }

    @Override
    public String toString() {
        if (op1 != null) return String.format("%s := %s + %d", dst, op1, immediate);
        return String.format("%s := %s + %d", dst, dst, immediate);
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.remove(dst.getSerialNumber());
        if (op1 != null) inSet.add(op1.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        if (op1 != null) temps.add(op1);
    }

    @Override
    public void MIPSme() {
        if (op1 != null) MIPSGenerator.getInstance().addi(dst, op1, immediate);
        else MIPSGenerator.getInstance().addi(dst, dst, immediate);
    }
}
