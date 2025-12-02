package AST;

import java.util.Arrays;
import java.util.List;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	public AST_VAR_SUBSCRIPT(AST_VAR var, AST_EXP subscript)
	{
		super("var -> var [ exp ]");

		this.var = var;
		this.subscript = subscript;
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(var, subscript);
	}

	@Override
	protected String GetNodeName() {
		return "SUBSCRIPT\nVAR\n...[...]";
	}
}
