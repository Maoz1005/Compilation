package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.List;
import java.util.Set;

// the main difference between this and CallFunc is that here we also save the reference to the object
// whose method is called in register s1 and restore the previous s1 register after
public class IRcommand_CallMethod extends IRcommand{
    String funcName;
    int funcOffset;
    List<TEMP> params;
    TEMP dst;
    TEMP caller;

    public IRcommand_CallMethod(String funcName, int funcOffset, TEMP caller, List<TEMP> params, TEMP dst) {
        this.funcName = funcName;
        this.funcOffset = funcOffset;
        this.params = params;
        this.dst = dst;
        this.caller = caller;
    }

    @Override
    public String toString() {
        String str = String.format("t%d := call $t%d.%s(offset %d)(", dst.getSerialNumber(), caller.getSerialNumber(), funcName, funcOffset);
        for (TEMP t: params) str += " t" + t.getSerialNumber() + " ";
        str += ")";
        return str;
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here we need to remove dst and add all temps in params AND caller
        inSet.add(caller.getSerialNumber());
        for (TEMP t: params) inSet.add(t.getSerialNumber());
        inSet.remove(dst.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        temps.add(caller);
        temps.addAll(params);
    }

    @Override
    public void MIPSme() {
        // backup temps -> set method's caller ->
        // push parameters -> jump -> pop parameters ->
        // restore temps -> restore previous method's caller ->
        // get return value from v0 to dst
        MIPSGenerator mips = MIPSGenerator.getInstance();

        mips.setMethodObjectReference(caller);
        mips.backupTemps();
        for (int i = params.size()-1; i >= 0; i--){
            mips.stackPush(params.get(i));
        }
        mips.loadAt(dst, caller, 0); // access object
        mips.loadAt(dst, dst, 0); // access vtable of object
        mips.loadAt(dst, dst, funcOffset); // get correct func
        mips.jalr(dst);

        mips.stackDealloc(params.size());
        mips.restoreTemps();
        mips.restoreMethodObjectReference();
        mips.getReturnVal(dst);
    }
}
