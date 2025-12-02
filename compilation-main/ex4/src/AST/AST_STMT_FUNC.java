package AST;

import TEMP.TEMP;
import TYPES.*;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_FUNC extends AST_STMT {
    public AST_EXP_FUNC func;

    public AST_STMT_FUNC(String id, NODE_LIST<AST_EXP> args, int lineNum) {
        super("stmt -> ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON", lineNum); // ID(exp, exp, ...);
        this.func = new AST_EXP_FUNC(id, args, lineNum);
    }

    public AST_STMT_FUNC(String id, int lineNum){
        super("stmt -> ID LPAREN RPAREN SEMICOLON", lineNum); // ID();
        this.func = new AST_EXP_FUNC(id, lineNum);
    }

    public TYPE SemantMe() {
        func.SemantMe();

        return null;
    }

    @Override
    protected String GetNodeName() {
        return "STMT\nFUNC";
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(func);
    }

    @Override
    public TEMP IRme() {
        return func.IRme();
    }
}
