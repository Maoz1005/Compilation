package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.List;
import java.util.Set;

public class IRcommand_CallFunc extends IRcommand {
    String funcname;
    List<TEMP> params;
    TEMP dst;

    public IRcommand_CallFunc(String funcname, List<TEMP> params, TEMP dst) {
        this.funcname = funcname;
        this.params = params;
        this.dst = dst;
    }

    @Override
    public String toString() {
        String str = String.format("t%d := call %s(", dst.getSerialNumber(), funcname);
        for (TEMP t: params) str += " t" + t.getSerialNumber() + " ";
        str += ")";
        return str;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here we need to remove dst and add all temps in params
        for (TEMP t: params) inSet.add(t.getSerialNumber());
        inSet.remove(dst.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        temps.addAll(params);
    }

    @Override
    public void MIPSme() {
        // backup temps -> push parameters -> jump -> pop parameters -> restore temps -> get return value from v0 to dst
        MIPSGenerator mips = MIPSGenerator.getInstance();
        mips.backupTemps();
        for (int i = params.size()-1; i >= 0; i--){
            mips.stackPush(params.get(i));
        }
        mips.jal(funcname);
        if (params.size() > 0) mips.stackDealloc(params.size());
        mips.restoreTemps();
        mips.getReturnVal(dst);
    }
}
