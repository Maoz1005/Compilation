package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_FUNCTION;

import java.util.List;

public class AST_PROGRAM extends AST_Node{
    public List<AST_DEC> declarations;

    public AST_PROGRAM(NODE_LIST<AST_DEC> declarations, int lineNum){
        super("program -> dec+", lineNum);

        this.declarations = declarations.unroll();
    }

    @Override
    public TYPE SemantMe() {
        for (AST_DEC dec : declarations)
            dec.SemantMe();
        return null;
    }

    @Override
    protected String GetNodeName() {
        return "PROGRAM\nDECLARATIONS";
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return declarations;
    }
}
