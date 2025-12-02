package IR;

import MIPS.MIPSGenerator;

/**
 * The command which will be translated to ".word className_methodDec" later on. Wasn't sure what else to call this.
 * (Feel free to refractor if you come up with a better name)
 */
public class IRcommand_MethodDec extends IRcommand {
    String className;
    String methodName;

    public IRcommand_MethodDec(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return ".word " + className + "_" + methodName;
    }

    public void MIPSme() {
        MIPSGenerator.getInstance().word(className + "_" + methodName);
    }
}
