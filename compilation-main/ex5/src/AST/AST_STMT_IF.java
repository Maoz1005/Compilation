package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import IR.*;
import TYPES.TYPE;
import TYPES.TYPE_INT;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_IF extends AST_STMT {
	public AST_EXP cond;
	public AST_STMT_LIST body;

	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body, int lineNum) {
		super("IF LPAREN exp RPAREN LBRACE stmtlist RBRACE", lineNum); // if (exp) {...}
		this.cond = cond;
		this.body = body;
	}

	public TYPE SemantMe() {
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


	public TEMP IRme() {
		IR ir = IR.getInstance();
		String startLabel = IRcommand.getFreshLabel("start");
		String endLabel = IRcommand.getFreshLabel("end");

		ir.add(new IRcommand_Label(startLabel));
		ir.add(new IRcommand_Jump_If_Eq_To_Zero(cond.IRme(), endLabel));
		if (body != null) { body.IRme(); }

 		ir.add(new IRcommand_Label(endLabel));

		return null;
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