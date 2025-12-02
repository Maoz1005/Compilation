package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_FUNCDEC extends AST_DEC {
    public AST_TYPE returnTypeNode;
    public String id;
    public AST_STMT_LIST statements;
    public List<AST_FUNCPARAM> params;

    public AST_FUNCDEC(AST_TYPE returnTypeNode, String id, NODE_LIST<AST_FUNCPARAM> params, AST_STMT_LIST statements, int lineNum) {
        super("funcDec -> type ID LPAREN (type ID (COMMA type ID)*)? RPAREN LBRACE stmt (stmt)* RBRACE", lineNum);

        this.returnTypeNode = returnTypeNode;
        this.id = id;
        this.statements = statements;
        if (params != null) this.params = params.unroll();
        else this.params = Arrays.asList();
    }

    public TYPE SemantMe() {
        SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
        // yea this probably never happens
        if (symbolTable.getExpectedReturnType() != null)
            throwException("Nested function declaration.");

        TYPE returnType = returnTypeNode.SemantMe();
        List<TYPE> paramTypes = new ArrayList<>();
        TYPE_FUNCTION thisType = new TYPE_FUNCTION(returnType, id, paramTypes);
        tryTableEnter(id, thisType);

        symbolTable.beginScope();

        for (AST_FUNCPARAM param : params) {
            paramTypes.add(param.SemantMe());
        }

        symbolTable.setExpectedReturnType(returnType);

        statements.SemantMe();

        symbolTable.setExpectedReturnType(null);
        symbolTable.endScope();

        return thisType;
    }

    public TEMP IRme() {
        String funcName = "MAIN";
        IR.getInstance().Add_IRcommand(new IRcommand_Label(funcName)); // that's funny
        if (statements != null) statements.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Label(funcName + "END"));
        return null;
    }


    @Override
    protected List<? extends AST_Node> GetChildren() {
        List<AST_Node> children;

        if (params != null) children = new ArrayList<>(params);
        else children = new ArrayList<>();

        children.add(statements);
        children.add(0, returnTypeNode);

        return children;
    }


    @Override
    protected String GetNodeName() {
        return String.format("DEC\nFUNC(%s)", id);
    }

}
