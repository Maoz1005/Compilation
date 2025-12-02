package AST;

import java.util.Arrays;
import java.util.List;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	
	public AST_VAR_FIELD(AST_VAR var,String fieldName)
	{
		super("var -> var DOT ID( %s )");

		this.var = var;
		this.fieldName = fieldName;
	}

	@Override
	protected String GetNodeName() {
		return String.format("FIELD\nVAR...->%s", fieldName);
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(var);
	}
}
