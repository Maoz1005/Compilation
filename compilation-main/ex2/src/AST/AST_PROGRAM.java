package AST;

import java.util.List;

public class AST_PROGRAM extends AST_Node{
    public List<AST_DEC> declarations;

    public AST_PROGRAM(NODE_LIST<AST_DEC> declarations){
        super("program -> dec+");

        this.declarations = declarations.unroll();
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
