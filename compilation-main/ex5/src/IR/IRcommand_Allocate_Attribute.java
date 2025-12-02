package IR;

// TODO: Probably useless but I need a placeholder for the logger.
public class IRcommand_Allocate_Attribute extends IRcommand{
    String var_name;
    String className;
    int offset;

    public IRcommand_Allocate_Attribute(String var_name, String className, int offset) {
        this.var_name = var_name;
        this.className = className;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return super.toString("Attribute Declaration",
                "name: " + var_name + ", Class: " + className + ", offset: " + offset);
    }

    @Override
    public void MIPSme() {}
}
