package IR;

public class IRcommand_Placeholder extends IRcommand {

    String message;

    public IRcommand_Placeholder() {
        this.message = "No message :(";
    }

    public IRcommand_Placeholder(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PLACEHOLDER:" + this.message;
    }

    public void MIPSme() { }
}
