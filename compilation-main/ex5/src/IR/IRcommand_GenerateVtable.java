package IR;

import MIPS.MIPSGenerator;

import java.util.List;

public class IRcommand_GenerateVtable extends IRcommand {
    public String classname;
    public List<String> methods;

    public IRcommand_GenerateVtable(String classname, List<String> methods) {
        this.classname = classname;
        this.methods = methods;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().label("vtable_" + classname);
        for (String method : methods){
            MIPSGenerator.getInstance().word(method);
        }
    }
}
