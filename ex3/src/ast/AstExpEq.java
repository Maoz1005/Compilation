package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpEq extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpEq(AstExp left, AstExp right)
	{
		super("exp -> exp EQ exp");

		this.left = left;
		this.right = right;
		this.op = new AstBinop("=");
	}

	@Override
	protected String GetNodeName() {
		return "ExpEq";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op, right);
	}
}
