package AST;

import java.util.Arrays;
import java.util.List;

public class AST_VARDEC extends AST_DEC{
  public AST_TYPE type;
  public String id;
  public AST_EXP exp;

  public AST_VARDEC(AST_TYPE type, String id){
    super("varDec -> type ID SEMICOLON");
    this.type = type;
    this.id = id;
  }

  // this also accepts newExp (since it inherits from exp) - surely this will have no consequences whatsoever
  public AST_VARDEC(AST_TYPE type, String id, AST_EXP exp){
    super("varDec -> type ID ASSIGN exp SEMICOLON");
    this.type = type;
    this.id = id;
    this.exp = exp;
  }

  @Override
  public String GetNodeName(){
    return String.format("VARDEC\nTYPE ID( %s )", id);
  }

  @Override
  public List<? extends AST_Node> GetChildren(){
    if (exp == null) return Arrays.asList(type);
    return Arrays.asList(type, exp);
  }
}
