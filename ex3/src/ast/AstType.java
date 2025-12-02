package ast;

public class AstType extends AstNode {

  public String type;

  public AstType(String type){
    super("type -> " + type);
    this.type = type;
  }

  @Override
  public String GetNodeName(){
    return "TYPE " + type;
  }
}
