package AST;

import java.util.Arrays;
import java.util.List;

public class AST_EXP_BINOP extends AST_EXP
{
	public AST_BINOP op;
	public AST_EXP left;
	public AST_EXP right;
	
	public AST_EXP_BINOP(AST_EXP left, AST_BINOP op, AST_EXP right)
	{
		super("exp -> exp binop exp");

		this.left = left;
		this.right = right;
		this.op = op;
	}

	@Override
	protected String GetNodeName() {
		return "EXP_BINOP";
	}
	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(left, op, right);
	}
}
