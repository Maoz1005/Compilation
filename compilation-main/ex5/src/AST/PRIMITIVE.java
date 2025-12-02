package AST;

public enum PRIMITIVE {
  INT("int"),
  STRING("string"),
  VOID("void");

  public String label;

  private PRIMITIVE(String label){
    this.label = label;
  }
}
