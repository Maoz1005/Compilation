package AST;

import java.util.Arrays;
import java.util.List;

public class AST_NEWEXP extends AST_EXP{
  public AST_TYPE type;
  public AST_EXP exp;

  public AST_NEWEXP(AST_TYPE type){
    super("NewExp -> NEW type");

    this.type = type;
  }

  public AST_NEWEXP(AST_TYPE type, AST_EXP exp){
    super("NewExp -> NEW type LBRACE exp RBRACE");

    this.type = type;
    this.exp = exp;
  }

  @Override
  public String GetNodeName(){
    String nodename = "NEWEXP\nTYPE";
    if (exp != null) nodename += " EXP";
    return nodename;
  }

  @Override
  protected List<? extends AST_Node> GetChildren() {
    if (exp == null) return Arrays.asList(type);
    return Arrays.asList(type, exp);
  }
}
