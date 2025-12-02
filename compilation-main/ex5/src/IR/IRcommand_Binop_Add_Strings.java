package IR;

import AST.IRcommand_RemoveFromCFG;
import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.List;
import java.util.Set;

public class IRcommand_Binop_Add_Strings extends IRcommand_Binop {

    public IRcommand_Binop_Add_Strings(TEMP dst, TEMP t1, TEMP t2) {
        super(dst, t1, t2);
        // do not
        IR.getInstance().add(new IRcommand_RemoveFromCFG(dst));
    }

    @Override
    public String toString() {
        return dst + " := " + t1 + " + " + t2 + "\t (Strings)";
    }

    @Override
    public void calcOut(Set<Integer> inSet) {
        // question my methods
        inSet.add(dst.getSerialNumber());
        inSet.add(t1.getSerialNumber());
        inSet.add(t2.getSerialNumber());
    }

    @Override
    public void MIPSme() {
        System.out.println("***************************");
        System.out.println(dst + " := " + t1 + " + " + t2 + "\t (Strings)");
        MIPSGenerator.getInstance().appendStrs(dst, t1, t2);
    }
}
