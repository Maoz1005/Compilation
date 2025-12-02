package AST;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body)
	{
		super("stmt -> WHILE LPAREN exp RPAREN LBRACE stmtlist RBRACE");
		this.cond = cond;
		this.body = body;
	}

	@Override
	protected String GetNodeName() {
		return "WHILE\n(COND) {BODY}";
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(cond, body);
	}
}