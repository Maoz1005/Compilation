package AST;

import TEMP.TEMP;
import TYPES.TYPE;

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

    @Override
    public TEMP IRme() {
        for (AST_DEC dec: declarations) dec.IRme();

        return null;
    }
}
