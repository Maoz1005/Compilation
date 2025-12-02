package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpTimes extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpTimes(AstExp left, AstExp right)
	{
		super("exp -> exp times exp");

		this.left = left;
		this.right = right;
		this.op = new AstBinop("*");
	}

	@Override
	protected String GetNodeName() {
		return "ExpTimes";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op, right);
	}
}
