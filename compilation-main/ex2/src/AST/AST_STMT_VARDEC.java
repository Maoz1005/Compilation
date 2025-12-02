package AST;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_VARDEC extends AST_STMT {
    public AST_VARDEC dec;

    public AST_STMT_VARDEC(AST_VARDEC dec) {
        super("stmt -> vardec");

        this.dec = dec;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(dec);
    }

    @Override
    protected String GetNodeName() {
        return "STMT\nVARDEC";
    }
}
