package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.Set;

public class IRcommand_GetArrayAccessAddress extends IRcommand_GetAddress {
    TEMP offset;
    TEMP arrPtr;

    public IRcommand_GetArrayAccessAddress(TEMP dst, String var_name, TEMP arrPtr, TEMP offset) {
        super(dst, var_name);
        this.offset = offset;
        this.arrPtr = arrPtr;
    }

    @Override
    public String toString() {
        return String.format("%s := the address of array %s inside %s with offset + %s", dst, nameToLoad, arrPtr, offset);
    }

    @Override
    public void addTemps(Set<TEMP> temps) {
        temps.add(dst);
        temps.add(offset);
        temps.add(arrPtr);
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        inSet.add(dst.getSerialNumber());
        inSet.add(offset.getSerialNumber());
        inSet.add(arrPtr.getSerialNumber());
    }

    @Override
    public void MIPSme() {
        MIPSGenerator mips = MIPSGenerator.getInstance();

        // check bound restrictions
        mips.loadAt(dst, arrPtr, 0);
        mips.bltz(offset, IRPatterns.OUT_OF_BOUNDS_LABEL); // check > 0
        mips.bge(offset, dst, IRPatterns.OUT_OF_BOUNDS_LABEL); // check < size

        // get address
        mips.addi(dst, offset, 1); // add 1 because of keeping the array size
        mips.sll(dst, dst, 2); // multiply by 4
        mips.add(dst, dst, arrPtr);
    }
}
