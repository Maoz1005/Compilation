package ast;

public class AstBinop extends AstNode {
  public String op;

  public AstBinop(String op){
    super("binop -> " + op);

    this.op = op;
  }

  @Override
  public String GetNodeName(){
    return String.format("BINOP( %s )", op);
  }
}
