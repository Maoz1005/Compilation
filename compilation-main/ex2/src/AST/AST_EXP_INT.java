package AST;

public class AST_EXP_INT extends AST_EXP
{
	public int value;
	
	public AST_EXP_INT(int value)
	{
		super("exp -> INT( %d )");

		this.value = value;
	}

	@Override
	protected String GetNodeName() {
		return String.format("INT(%d)",value);
	}
}
