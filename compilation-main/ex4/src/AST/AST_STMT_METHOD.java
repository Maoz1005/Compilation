package AST;

import TYPES.TYPE;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_METHOD extends AST_STMT {
    public AST_EXP_METHOD method;

    public AST_STMT_METHOD(AST_VAR var, String methodName, NODE_LIST<AST_EXP> args, int lineNum) {
        super("stmt -> var DOT ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON", lineNum); // x.method(exp, exp...);
        this.method = new AST_EXP_METHOD(var, methodName, args, lineNum);
    }

    public AST_STMT_METHOD(AST_VAR var, String methodName, int lineNum){
        super("stmt -> ID LPAREN RPAREN SEMICOLON", lineNum); // x.method();
        this.method = new AST_EXP_METHOD(var, methodName, lineNum);
    }

    public TYPE SemantMe() {
        method.SemantMe();

        return null;
    }

    @Override
    protected String GetNodeName() {
        return "STMT\nMETHOD";
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(method);
    }
}
