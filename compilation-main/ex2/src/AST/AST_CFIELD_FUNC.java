package AST;

import java.util.Arrays;
import java.util.List;

public class AST_CFIELD_FUNC extends AST_CFIELD{
    public AST_FUNCDEC funcDec;

    public AST_CFIELD_FUNC(AST_FUNCDEC funcDec) {
        super("CField -> funcDec");

        this.funcDec = funcDec;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(funcDec);
    }

    @Override
    protected String GetNodeName() {
        return "CFIELD\nFUNCDEC";
    }
}
