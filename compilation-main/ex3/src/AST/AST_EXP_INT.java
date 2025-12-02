package AST;

import TYPES.TYPE;
import TYPES.TYPE_INT;

public class AST_EXP_INT extends AST_EXP
{
	public int value;
	
	public AST_EXP_INT(int value, int lineNum) {
		super("exp -> INT( %d )", lineNum);

		this.value = value;
	}

	public TYPE SemantMe() {
		return TYPE_INT.getInstance();
	}

	@Override
	protected String GetNodeName() {
		return String.format("INT(%d)",value);
	}

	@Override
	public boolean isConstant() {
		return true;
	}
}
