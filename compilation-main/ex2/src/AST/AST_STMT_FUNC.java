package AST;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_FUNC extends AST_STMT {
    public String id;
    public List<AST_EXP> args;

    public AST_STMT_FUNC(String id, NODE_LIST<AST_EXP> args) {
        super("stmt -> ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON");

        this.id = id;
        this.args = args.unroll();
    }

    public AST_STMT_FUNC(String id){
        super("stmt -> ID LPAREN RPAREN SEMICOLON");

        this.id = id;
        this.args = Arrays.asList();
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\n%s(%s)", id, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return args;
    }
}
