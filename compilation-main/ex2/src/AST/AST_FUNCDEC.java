package AST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_FUNCDEC extends AST_DEC {
    public AST_TYPE returnType;
    public String id;
    public AST_STMT_LIST statements;
    public List<AST_FUNCPARAM> params;

    public AST_FUNCDEC(AST_TYPE returnType, String id, NODE_LIST<AST_FUNCPARAM> params, AST_STMT_LIST statements) {
        super("funcDec -> type ID LPAREN (type ID (COMMA type ID)*)? RPAREN LBRACE stmt (stmt)* RBRACE");

        this.returnType = returnType;
        this.id = id;
        this.statements = statements;
        if (params != null) this.params = params.unroll();
        else this.params = Arrays.asList();
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        List<AST_Node> children;

        if (params != null) children = new ArrayList<>(params);
        else children = new ArrayList<>();

        children.add(statements);
        children.add(0, returnType);

        return children;
    }

    @Override
    protected String GetNodeName() {
        return String.format("DEC\nFUNC(%s)", id);
    }
}
