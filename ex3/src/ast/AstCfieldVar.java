package ast;

import java.util.Arrays;
import java.util.List;

public class AstCfieldVar extends AstCfield {
    public AstVardec varDec;

    public AstCfieldVar(AstVardec varDec) {
        super("CField -> varDec");
        this.varDec = varDec;
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(varDec);
    }

    @Override
    protected String GetNodeName() {
        return "CFIELD\nVARDEC";
    }
}
