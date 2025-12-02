package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TEMP.*;
import IR.*;

/**
 * Node for a simple integer/string-type variable.
 * @attribute name: The ID of the variable.
 */
public class AST_VAR_SIMPLE extends AST_VAR {

	public String name;
	public static int localVariablesCounter = 0;// TODO: no clue wtf this is, copied from AST_EXP_VAR_SIMPLE in skeleton

	public AST_VAR_SIMPLE(String name, int lineNum) {
		super("var -> ID( %s )", lineNum);

		this.name = name;
	}

	public TYPE SemantMe() {
		return tryTableFind(this.name);
	}

	@Override
	protected String GetNodeName() {
		return String.format("SIMPLE\nVAR(%s)",name);
	}


	public TEMP IRme() {
		TEMP t = new TEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Load(t,name));
		return t;
	}
}
