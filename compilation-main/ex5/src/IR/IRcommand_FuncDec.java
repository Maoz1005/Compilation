package IR;

import MIPS.MIPSGenerator;

public class IRcommand_FuncDec extends IRcommand {
    String funcName;
    int localCount; // amount of local variables inside the func

    public IRcommand_FuncDec(String funcName, int localCount) {
        this.funcName = funcName;
        this.localCount = localCount;
    }

    @Override
    public String toString() {
        return "\nFuncDec(" + funcName + ") with " + localCount + " local vars.";
    }

    public void MIPSme() {
        MIPSGenerator mips = MIPSGenerator.getInstance();

        mips.label(funcName);
        if (!funcName.equals("main")) mips.pushFrame();
        else mips.wildcard("move $fp, $sp"); // i am working on my last strength

        if (localCount > 0) mips.stackAlloc(localCount);
    }
}
