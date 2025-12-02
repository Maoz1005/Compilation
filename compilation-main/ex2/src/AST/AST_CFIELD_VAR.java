package AST;

import java.util.Arrays;
import java.util.List;

public class AST_CFIELD_VAR extends AST_CFIELD {
    public AST_VARDEC varDec;

    public AST_CFIELD_VAR(AST_VARDEC varDec) {
        super("CField -> varDec");

        this.varDec = varDec;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(varDec);
    }

    @Override
    protected String GetNodeName() {
        return "CFIELD\nVARDEC";
    }
}
