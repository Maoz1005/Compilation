package AST;

import java.util.Arrays;
import java.util.List;

public class AST_EXP_FUNC extends AST_EXP {
    public String id;

    public List<AST_EXP> exps;

    public AST_EXP_FUNC(String id) {
        super(String.format("exp -> ID( %s ) LBRACK RBRACK", id));

        this.id = id;
        exps = Arrays.asList();
    }

    public AST_EXP_FUNC(String id, NODE_LIST expStar){
        super(String.format("exp -> ID( %s ) LBRACK exp* RBRACK", id));

        this.id = id;
        exps = expStar.unroll();
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return exps;
    }

    @Override
    protected String GetNodeName() {
        return String.format("%s(%s)", id, exps != null ? "PARAMS" : "");
    }
}
