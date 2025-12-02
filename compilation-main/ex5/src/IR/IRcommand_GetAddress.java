package IR;

import TEMP.TEMP;

import java.util.Set;

// represents all "what is the address of" commands, which include globals, locals, constant strings and others
public abstract class IRcommand_GetAddress extends IRcommand {

    public TEMP dst;
    public String nameToLoad;

    public IRcommand_GetAddress(TEMP dst, String var_name) {
        this.dst = dst;
        this.nameToLoad = var_name;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do t := load(var_name), so we need to remove t from the updated out set
        inSet.remove(dst.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
    }
}
