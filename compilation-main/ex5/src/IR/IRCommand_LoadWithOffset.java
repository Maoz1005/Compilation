package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Set;

public class IRCommand_LoadWithOffset extends IRcommand {

    TEMP dst;
    TEMP src;
    TEMP offset; // if we need to add some variable
    int immediateOffset; // if we need to add some constant

    public IRCommand_LoadWithOffset(TEMP dst, TEMP src, TEMP offset, int immediateOffset) {
        this.dst = dst;
        this.src = src;
        this.offset = offset;
        this.immediateOffset = immediateOffset;
    }

    public IRCommand_LoadWithOffset(TEMP dst, TEMP src, TEMP offset){
        this(dst, src, offset, 0);
    }

    public IRCommand_LoadWithOffset(TEMP dst, TEMP src, int immediateOffset){
        this(dst, src, null, immediateOffset);
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // here, we do dst := src[offset]
        // so we need to remove dst, and add src and offset to the updated out set
        inSet.remove(dst.getSerialNumber());
        inSet.add(src.getSerialNumber());
        if (offset != null) inSet.add(offset.getSerialNumber());
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        temps.add(src);
        if (offset != null) temps.add(offset);
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().loadAt(dst, src, offset, immediateOffset);
    }

    public String toString() {
        if (offset != null)
            return super.toString(dst + " := " + src + "[" + offset + " + " + immediateOffset + "]","");
        else return super.toString(dst + " := " + src + "["+ immediateOffset + "]");
    }
}
