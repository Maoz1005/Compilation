package AST;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_ASSIGN extends AST_STMT
{
	public AST_VAR var;
	public AST_EXP exp;

	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp)
	{
		super("stmt -> var ASSIGN exp SEMICOLON");

		this.var = var;
		this.exp = exp;
	}

	@Override
	protected String GetNodeName() {
		return "ASSIGN\nleft := right";
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(var, exp);
	}
}
