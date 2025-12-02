package AST;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_RETURN extends AST_STMT {
  public AST_EXP exp;

  public AST_STMT_RETURN(AST_EXP exp){
    super("stmt -> RETURN exp");

    this.exp = exp;
  }

  public AST_STMT_RETURN(){
    super("stmt -> RETURN");
  }

  @Override
  public String GetNodeName(){
    return "RETURN";
  }

  @Override
  public List<? extends AST_Node> GetChildren(){
    if (exp == null) return Arrays.asList();
    return Arrays.asList(exp);
  }
}
