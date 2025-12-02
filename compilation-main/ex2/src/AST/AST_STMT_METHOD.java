package AST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_STMT_METHOD extends AST_STMT {
    public AST_VAR var;
    public String fieldName;
    public List<AST_EXP> args;

    public AST_STMT_METHOD(AST_VAR var, String fieldName, NODE_LIST<AST_EXP> args) {
        super("stmt -> var DOT ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON");

        this.var = var;
        this.fieldName = fieldName;
        this.args = args.unroll();
    }

    public AST_STMT_METHOD(AST_VAR var, String fieldName){
        super("stmt -> ID LPAREN RPAREN SEMICOLON");

        this.var = var;
        this.fieldName = fieldName;
        this.args = Arrays.asList();
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\nVAR...->%s(%s)", fieldName, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        List<AST_Node> children = new ArrayList<>(args);
        children.add(0, var);
        return children;
    }
}
