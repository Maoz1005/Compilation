package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE;
import TYPES.TYPE_INT;

import java.util.Arrays;
import java.util.List;
import IR.*;

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

	/**
	 * START: condition
	 * 		  <body>
	 * 		  goto START
	 * END: <rest of code>
	 */
	@Override
	public TEMP IRme() {
		String label_end = IRcommand.getFreshLabel("end");
		String label_start = IRcommand.getFreshLabel("start");

		IR.getInstance().add(new IRcommand_Label(label_start));
		TEMP cond_temp = cond.IRme();
		IR.getInstance().add(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end)); /* [4] Jump conditionally to the loop end */
		body.IRme();
		IR.getInstance().add(new IRcommand_Jump_Label(label_start)); /* [6] Jump to the loop entry */
		IR.getInstance().add(new IRcommand_Label(label_end)); /* [7] Loop end label */

		return null;
	}
}