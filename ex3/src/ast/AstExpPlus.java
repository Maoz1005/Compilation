package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpPlus extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpPlus(AstExp left, AstExp right)
	{
		super("exp -> exp plus exp");

		this.left = left;
		this.right = right;
		this.op = new AstBinop("+");
	}

	@Override
	protected String GetNodeName() {
		return "ExpPlus";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op, right);
	}
}
