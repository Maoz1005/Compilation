package ast;

import java.util.Arrays;
import java.util.List;

public class AstCfieldFunc extends AstCfield{
    public AstFuncdec funcDec;

    public AstCfieldFunc(AstFuncdec funcDec) {
        super("CField -> funcDec");
        this.funcDec = funcDec;
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(funcDec);
    }

    @Override
    protected String GetNodeName() {
        return "CFIELD\nFUNCDEC";
    }
}
