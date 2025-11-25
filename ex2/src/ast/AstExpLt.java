package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpLt extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpLt(AstExp left, AstExp right)
	{
		super("exp -> exp LT exp");

		this.left = left;
		this.right = right;
		this.op = new AstBinop("<");
	}

	@Override
	protected String GetNodeName() {
		return "ExpLt";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op, right);
	}
}
