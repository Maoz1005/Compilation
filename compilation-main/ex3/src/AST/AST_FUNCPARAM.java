package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;

import java.util.Arrays;
import java.util.List;

public class AST_FUNCPARAM extends AST_Node {
    public AST_TYPE typeNode;
    public String id;


    public AST_FUNCPARAM(AST_TYPE type, String id, int lineNum) {
        super("funcDec parameter (type ID)", lineNum);

        this.typeNode = type;
        this.id = id;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(typeNode);
    }

    @Override
    public TYPE SemantMe() {
        TYPE paramType = typeNode.SemantMe();
        tryTableEnter(id, paramType);

        return paramType;
    }

    @Override
    protected String GetNodeName() {
        return String.format("PARAMETER( %s )", id);
    }
}
