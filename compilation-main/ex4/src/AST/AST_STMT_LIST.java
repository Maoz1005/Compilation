package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_LIST extends AST_Node {

	public AST_STMT head;
	public AST_STMT_LIST tail;

	public AST_STMT_LIST(AST_STMT head, AST_STMT_LIST tail, int lineNum) {
		super("stmtlist -> " + (tail != null ? "stmt stmtlist" : "stmt"), lineNum);

		this.head = head;
		this.tail = tail;
	}

	public TYPE SemantMe() {
		AST_STMT_LIST current = this;
		while (current != null) {
			current.head.SemantMe();
			current = current.tail;
		}
		return null; // A sequence of statements doesn't have a type.
	}

	public TEMP IRme() {
		if (head != null) head.IRme();
		if (tail != null) tail.IRme();

		return null;
	}

	@Override
	protected String GetNodeName() {
		return "STMT\nLIST";
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		if (tail == null){
			return Arrays.asList(head);
		}
		return Arrays.asList(head, tail);
	}
}
