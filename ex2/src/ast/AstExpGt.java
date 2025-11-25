package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpGt extends AstExp
{
	public AstExp left;
	public AstExp right;
	public AstBinop op;
	
	public AstExpGt(AstExp left, AstExp right)
	{
		super("exp -> exp GT exp");

		this.left = left;
		this.right = right;
		this.op = new AstBinop(">");
	}

	@Override
	protected String GetNodeName() {
		return "ExpGt";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, op, right);
	}
}
