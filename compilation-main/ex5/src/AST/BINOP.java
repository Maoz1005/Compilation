package AST;

public enum BINOP {
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    LT("<"),
    GT(">"),
    EQ("=");

    public final String label;

    private BINOP(String label) {
        this.label = label;
    }
}
