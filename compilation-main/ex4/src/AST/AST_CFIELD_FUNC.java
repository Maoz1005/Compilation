package AST;

import TYPES.TYPE;

import java.util.Arrays;
import java.util.List;

public class AST_CFIELD_FUNC extends AST_CFIELD{
    public AST_FUNCDEC funcDec;

    public AST_CFIELD_FUNC(AST_FUNCDEC funcDec, int lineNum) {
        super("CField -> funcDec", lineNum);

        this.funcDec = funcDec;
    }

    public TYPE SemantMe() {
        return funcDec.SemantMe();
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
