package AST;

public class AST_EXP_STRING extends AST_EXP {
    public String value;

    public AST_EXP_STRING(String value) {
        super(String.format("exp -> STRING( %s )", value));

        this.value = value;
    }

    @Override
    protected String GetNodeName() {
        return String.format("EXP\nSTRING( %s )", value);
    }
}
