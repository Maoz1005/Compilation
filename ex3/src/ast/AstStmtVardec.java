package ast;

import java.util.Arrays;
import java.util.List;

public class AstStmtVardec extends AstStmt {
    public AstVardec dec;

    public AstStmtVardec(AstVardec dec) {
        super("stmt -> vardec");
        this.dec = dec;
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return Arrays.asList(dec);
    }

    @Override
    protected String GetNodeName() {
        return "STMT\nVARDEC";
    }
}
