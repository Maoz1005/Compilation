package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY;

import java.util.Arrays;
import java.util.List;

public class AST_DEC_ARRAYTYPEDEF extends AST_DEC{
    public String id;
    public AST_TYPE type;

    public AST_DEC_ARRAYTYPEDEF(String id, AST_TYPE type, int lineNum) {
        super("arrayTypeDef -> ARRAY ID EQ type LBRACK RBRACK SEMICOLON", lineNum); // array type_that'll_be_defined = type[];

        this.id = id;
        this.type = type;
    }

    public TYPE SemantMe() {
        SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();

        if (!symbolTable.inGlobalScope()) { throwException("Array must be in global scope."); }
        TYPE typeOfElements = type.SemantMe();
        TYPE_ARRAY thisType = new TYPE_ARRAY(id, typeOfElements);
        tryTableEnter(id, thisType);

        return thisType; // Not really necessary.
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
