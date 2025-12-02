package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpMinus extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpMinus(AstExp left, AstExp right)
	{
		super("exp -> exp minus exp");

		this.left = left;
		this.right = right;
		this.op = new AstBinop("-");
	}

	@Override
	protected String GetNodeName() {
		return "ExpMinus";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op, right);
	}
}
