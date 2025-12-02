package ast;

import java.util.Arrays;
import java.util.List;

public class AstStmtReturn extends AstStmt {
  public AstExp exp;

  public AstStmtReturn(AstExp exp){
    super("stmt -> RETURN exp");
    this.exp = exp;
  }

  public AstStmtReturn(){
    super("stmt -> RETURN");
  }

  @Override
  public String GetNodeName(){
    return "RETURN";
  }

  @Override
  public List<? extends AstNode> GetChildren(){
    if (exp == null) return Arrays.asList();
    return Arrays.asList(exp);
  }
}
