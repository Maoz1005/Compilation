package AST;

public class AST_TYPE extends AST_Node {

  public String type;

  public AST_TYPE(String type){
    super("type -> " + type);

    this.type = type;
  }

  @Override
  public String GetNodeName(){
    return "TYPE " + type;
  }
}
