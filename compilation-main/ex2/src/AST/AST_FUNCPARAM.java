package AST;

import java.util.Arrays;
import java.util.List;

public class AST_FUNCPARAM extends AST_Node {
    public AST_TYPE type;
    public String id;


    public AST_FUNCPARAM(AST_TYPE type, String id) {
        super("funcDec parameter (type ID)");

        this.type = type;
        this.id = id;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(type);
    }

    @Override
    protected String GetNodeName() {
        return String.format("PARAMETER( %s )", id);
    }
}
