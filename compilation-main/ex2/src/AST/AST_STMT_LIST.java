package AST;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_LIST extends AST_Node
{
	public AST_STMT head;
	public AST_STMT_LIST tail;

	public AST_STMT_LIST(AST_STMT head, AST_STMT_LIST tail)
	{
		super("stmtlist -> " + (tail != null ? "stmt stmtlist" : "stmt"));

		this.head = head;
		this.tail = tail;
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
