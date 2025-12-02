package IR;

import MIPS.MIPSGenerator;

/**
 * Remember this is a "data" label as opposed to a text one, so a distinction must be made.
 */
public class IRcommand_ClassDec extends IRcommand {

    String className;

    public IRcommand_ClassDec(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "---------------------------\n" +
               "ClassDec(" + className + ")";
    }

    public void MIPSme() {
        MIPSGenerator.getInstance().label("vtable_" + className);
    }
}
