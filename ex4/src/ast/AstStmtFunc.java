package ast;

import types.Type;
import types.TypeFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.Utils.matchTypesArgsParams;

public class AstStmtFunc extends AstStmt {
    public AstExpFunc func;


    public AstStmtFunc(String id, NodeList<AstExp> args, int lineNum) {
        super("stmt -> ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON", lineNum); // ID(exp, exp, ...);
        this.func = new AstExpFunc(id, args, lineNum);
    }

    public AstStmtFunc(String id, int lineNum){
        super("stmt -> ID LPAREN RPAREN SEMICOLON", lineNum); // ID();
        this.func = new AstExpFunc(id, lineNum);
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\n%s(%s)", id, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AstNode> GetChildren() {
        return args;
    }

    public Type SemantMe() {
        func.SemantMe();
        return null;
    }


    @Override
    public Temp IRme() {
        return func.IRme();
    }
}
