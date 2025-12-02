package AST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_EXP_METHOD extends AST_EXP{
    public AST_VAR var;
    public String methodName;

    public List<AST_EXP> exps;

    public AST_EXP_METHOD(AST_VAR var, String methodName){
        super("exp -> var DOT ID LPAREN RPAREN");

        this.methodName = methodName;
        this.var = var;
        exps = Arrays.asList();
    }

    public AST_EXP_METHOD(AST_VAR var, String methodName, NODE_LIST expStar){
        super("exp -> var DOT ID LPAREN exp* RPAREN");

        this.methodName = methodName;
        this.var = var;
        exps = expStar.unroll();
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        List<AST_Node> children = new ArrayList<>(exps);
        children.add(0, var);
        return children;
    }

    @Override
    protected String GetNodeName() {
        return String.format("VAR.%s(%s)", methodName, exps != null ? "PARAMS" : "");
    }
}
