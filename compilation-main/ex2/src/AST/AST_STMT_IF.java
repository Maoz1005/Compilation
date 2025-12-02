package AST;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body)
	{
		super("IF LPAREN exp RPAREN LBRACE stmtlist RBRACE");
		this.cond = cond;
		this.body = body;
	}

	@Override
	protected String GetNodeName() {
		return "IF\n(EXP) {STMTLIST}";
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(cond, body);
	}
}