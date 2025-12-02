package IR;

import MIPS.MIPSGenerator;

import java.util.Set;

// writes all string constants down
public class IRcommand_GenerateStringConsts extends IRcommand {
    Set<String> strs;

    public IRcommand_GenerateStringConsts(Set<String> strs) {
        this.strs = strs;
    }

    @Override
    public String toString() {
        String returnStr = "Generating the following String definitions:\n";
        for (String str : strs){
            returnStr += "str_" + str + ", ";
        }
        return returnStr;
    }

    @Override
    public void MIPSme() {
        MIPSGenerator.getInstance().builtInStrs();
        for (String str : strs){
            MIPSGenerator.getInstance().asciiz(str);
        }
    }
}
