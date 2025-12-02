package AST;

import java.util.Arrays;
import java.util.List;

public class AST_DEC_ARRAYTYPEDEF extends AST_DEC{
    public String id;
    public AST_TYPE type;

    public AST_DEC_ARRAYTYPEDEF(String id, AST_TYPE type) {
        super("arrayTypeDef -> ARRAY ID EQ type LBRACK RBRACK SEMICOLON");

        this.id = id;
        this.type = type;
    }

    @Override
    protected String GetNodeName() {
        return String.format("ARRAY_TYPEDEF( %s )\nTYPE", id);
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(type);
    }
}
