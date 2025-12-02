package AST;

import java.util.List;

public class AST_CLASSDEC extends AST_DEC{
    public String id;
    public String superclass;
    public List<AST_CFIELD> fields;

    public AST_CLASSDEC(String id, String superclass, NODE_LIST<AST_CFIELD> fields){
        super("CLASS ID EXTENDS ID LBRACE cField (cField)* RBRACE");

        this.id = id;
        this.superclass = superclass;
        this.fields = fields.unroll();
    }

    public AST_CLASSDEC(String id, NODE_LIST<AST_CFIELD> fields){
        super("CLASS ID EXTENDS ID LBRACE cField (cField)* RBRACE");

        this.id = id;
        this.fields = fields.unroll();
    }

    @Override
    protected String GetNodeName() {
        return String.format("CLASS %s%s\nCFIELDS", id, (superclass != null ? " EXTENDS " + superclass : ""));
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return fields;
    }
}
