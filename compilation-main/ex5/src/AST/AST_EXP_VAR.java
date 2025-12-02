package AST;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.*;

import java.util.Arrays;
import java.util.List;

public class AST_EXP_VAR extends AST_EXP {
	public AST_VAR var;

	public AST_EXP_VAR(AST_VAR var, int lineNum) {
		super("exp -> var", lineNum);

		this.var = var;
	}

	public TYPE SemantMe() {
		return this.var.SemantMe();
	}

	@Override
	public TEMP IRme() {
		TEMP addr = var.IRme();
		TEMP dst = new TEMP();

		IR.getInstance().add(new IRCommand_LoadWithOffset(dst, addr, 0));
		return dst;
	}


	@Override
	protected String GetNodeName() {
		return "EXP\nVAR";
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(var);
	}
}
