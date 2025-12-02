package AST;

import TYPES.TYPE;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_INT;

import java.util.Arrays;
import java.util.List;

/**
 * Node for accessing an index of a variable. For example a[x + 3] @@@@ Sh*t explanation innit
 * @attribute var: A chain of atomic variables comprised of defined types TODO: right?
 * @attribute subscript: The expression inside the brackets
 */
public class AST_VAR_SUBSCRIPT extends AST_VAR {
	public AST_VAR var;
	public AST_EXP subscript;
	
	public AST_VAR_SUBSCRIPT(AST_VAR var, AST_EXP subscript, int lineNum) {
		super("var -> var [ exp ]", lineNum); // x[index]

		this.var = var;
		this.subscript = subscript;
	}

	public TYPE SemantMe() { // TODO: Has to be an array, right?
		TYPE subscriptType = subscript.SemantMe();
		if (!(subscriptType instanceof TYPE_INT)) { // Note: doesn't seem like you need to validate indices.
			throwException("Index must be an integer.");
		}
		TYPE varType = var.SemantMe();
		if (!(varType.isArray())) {
			throwException("Accessing an index is only valid with arrays.");
		}
		TYPE_ARRAY arrType = (TYPE_ARRAY)varType;
		return arrType.typeOfElements;
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(var, subscript);
	}

	@Override
	protected String GetNodeName() {
		return "SUBSCRIPT\nVAR\n...[...]";
	}
}
