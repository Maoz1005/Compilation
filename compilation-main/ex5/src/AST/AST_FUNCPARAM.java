package AST;

import IR.*;
import SYMBOL_TABLE.*;
import TYPES.TYPE;
import TEMP.TEMP;

import java.util.Arrays;
import java.util.List;

public class AST_FUNCPARAM extends AST_Node {
    public AST_TYPE typeNode;
    public String id;
    public METADATA metadata;

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
        return SemantMe(-1);
    }

    public TYPE SemantMe(int offset) {
        TYPE paramType = typeNode.SemantMe();
        constructMetadata(offset);
        tryTableEnter(id, paramType, metadata);

        return paramType;
    }

    private void constructMetadata(int offset) {
        this.metadata = new METADATA();
        try {
            metadata.setAsVariable();
            metadata.setParameter();
            metadata.setOffset(offset);
            System.out.println("Found parameter: " + id + " with offset " + offset);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TEMP IRme() {
        IR.getInstance().add(new IRcommand_Parameter(id, metadata.getOffset()));
        return null;
    }

    @Override
    protected String GetNodeName() {
        return String.format("PARAMETER( %s )", id);
    }
}
