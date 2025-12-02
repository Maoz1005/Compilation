package AST;

public class AST_BINOP extends AST_Node {
  public BINOP op;

  public AST_BINOP(BINOP op){
    super("binop -> " + op.label);

    this.op = op;
  }

  @Override
  public String GetNodeName(){
    return String.format("BINOP( %s )", op.label);
  }
}
