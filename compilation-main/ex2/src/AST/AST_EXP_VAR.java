package AST;

import java.util.Arrays;
import java.util.List;

public class AST_EXP_VAR extends AST_EXP
{
	public AST_VAR var;

	public AST_EXP_VAR(AST_VAR var)
	{
		super("exp -> var");

		this.var = var;
	}

	@Override
	protected String GetNodeName() {
		return "EXP\nVAR";
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(var);
	}
}
