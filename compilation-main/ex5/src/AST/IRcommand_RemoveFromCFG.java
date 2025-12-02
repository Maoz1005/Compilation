package AST;

import IR.IRcommand;
import TEMP.TEMP;

import java.util.Set;

// absolutely disgusting hack
public class IRcommand_RemoveFromCFG extends IRcommand {

    TEMP toRemove;

    public IRcommand_RemoveFromCFG(TEMP toRemove) {
        this.toRemove = toRemove;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.remove(toRemove.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(toRemove);
    }

    @Override
    public void MIPSme() {

    }
}
