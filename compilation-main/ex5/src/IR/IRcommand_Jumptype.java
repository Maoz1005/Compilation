package IR;

public abstract class IRcommand_Jumptype extends IRcommand {

    public String label_name;
    public boolean ignoreCFG; // if it is a jump to an error we ignore the child (since it contains no temps anyway)

    public IRcommand_Jumptype(String label_name) {
        this(label_name, false);
    }

    public IRcommand_Jumptype(String label_name, boolean ignoreCFG) {
        this.label_name = label_name;
        this.ignoreCFG = ignoreCFG;
    }

    @Override
    public String toString() {
        return "jump to " + label_name + (ignoreCFG ? " (ignore in cfg)" : "");
    }
}
