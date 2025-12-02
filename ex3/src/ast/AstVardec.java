package ast;

import java.util.Arrays;
import java.util.List;

public class AstVardec extends AstDec{
  public AstType type;
  public String id;
  public AstExp exp;

  public AstVardec(AstType type, String id){
    super("varDec -> type ID SEMICOLON");
    this.type = type;
    this.id = id;
  }

  // this also accepts newExp (since it inherits from exp) - surely this will have no consequences whatsoever
  public AstVardec(AstType type, String id, AstExp exp){
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
  public List<? extends AstNode> GetChildren(){
    if (exp == null) return Arrays.asList(type);
    return Arrays.asList(type, exp);
  }
}
