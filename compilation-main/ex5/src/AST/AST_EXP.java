package AST;

public abstract class AST_EXP extends AST_Node {
	public AST_EXP(String derivation, int lineNum) {
		super(derivation, lineNum);
	}

	// to be overwritten:
	public boolean isConstant() { return false; }
	public boolean isNewExp() {return false; } // this is specifically to handle the god-awful array type-checking (see AST_STMT_ASSIGN semantic array check)
}