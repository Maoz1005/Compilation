package AST;

public class AST_EXP_NIL extends AST_EXP {
    public AST_EXP_NIL() {
        super("exp -> NIL");
    }

    @Override
    protected String GetNodeName() {
        return "NIL";
    }
}
