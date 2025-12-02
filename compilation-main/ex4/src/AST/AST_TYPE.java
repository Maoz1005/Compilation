package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;

public class AST_TYPE extends AST_Node {

    public String type;

    public AST_TYPE(String type, int lineNum){
        super("type -> " + type, lineNum);

        this.type = type;
    }

    @Override
    public TYPE SemantMe() {
        return tryTableFind(type);
    }

    @Override
    public String GetNodeName(){
        return "TYPE " + type;
    }
}
