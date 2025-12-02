package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_INT;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_WHILE extends AST_STMT {

	public AST_EXP cond;
	public AST_STMT_LIST body;

	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body, int lineNum) {
		super("stmt -> WHILE LPAREN exp RPAREN LBRACE stmtlist RBRACE", lineNum); // while (exp) {...}
		this.cond = cond;
		this.body = body;
	}

	public TYPE SemantMe() {
		/* COPIED FROM STMT IF */
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
		TYPE conditionType = cond.SemantMe();
		if (!(conditionType instanceof TYPE_INT)) {
			throwException("Conditions must be of type INT.");
		}
		symbolTable.beginScope();

		if (body != null) { body.SemantMe(); }

		symbolTable.endScope();
		return null;
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