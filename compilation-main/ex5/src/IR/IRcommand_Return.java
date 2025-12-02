package IR;
import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_Return extends IRcommand{
    // the return value (null if none)
    TEMP temp;
    boolean isMain = false;

    public IRcommand_Return(boolean isMain){
        this.isMain = isMain;
    }

    public IRcommand_Return(TEMP temp) {
        this.temp = temp;
    }

    public IRcommand_Return() {}

    @Override
    public String toString() {
        return "return " + (temp != null ? temp : "nathin");
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we return temp (or null)
        // so add temp to the updated out set if not null
        if (temp != null) inSet.add(temp.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        if (temp != null) temps.add(temp);
    }

    public void MIPSme() {
        MIPSGenerator mips = MIPSGenerator.getInstance();

        if (isMain) {
            mips.jump(MIPSGenerator.EOF);
            return;
        }

        if (temp != null) mips.setReturnVal(temp);

        mips.popFrameAndReturn();
    }
}
