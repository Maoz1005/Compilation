package AST;

import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_NIL;
import TEMP.*;
import IR.*;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_ASSIGN extends AST_STMT {

	public AST_VAR var;
	public AST_EXP exp;

	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, int lineNum) {
		super("stmt -> var ASSIGN exp SEMICOLON", lineNum); // var := exp;

		this.var = var;
		this.exp = exp;
	}

	public TYPE SemantMe() {
		TYPE leftTypeData = var.SemantMe();
		TYPE rightTypeData = exp.SemantMe();

		if (leftTypeData.equals(rightTypeData)) return null;

		if (leftTypeData.isArray()) {
			arraySemantCheck((TYPE_ARRAY)leftTypeData, rightTypeData, exp.isNewExp());
			return null;
		}

		if (leftTypeData instanceof TYPE_CLASS) {
			classSemantCheck((TYPE_CLASS)leftTypeData, rightTypeData);
			return null;
		}

		throwException("Type mismatch");
		return null; // cant reach this point
	}

	private void arraySemantCheck(TYPE_ARRAY leftArr, TYPE rightType, boolean isNewExp){
		if (!(rightType.isArray())) {
			if(!(rightType instanceof TYPE_NIL)) // nil is valid to array
				throwException("expression must be of array type");
			else return;
		}

		TYPE_ARRAY rightArr = (TYPE_ARRAY)rightType;
		// the reason for || !isNewExp is that otherwise the array types must be strictly the same, and that was already tested for
		if (!leftArr.typeOfElements.equals(rightArr.typeOfElements) || !isNewExp)
			throwException("Assignment of differing array types");

	}

	private void classSemantCheck(TYPE_CLASS leftClass, TYPE rightType){
		if (!(rightType instanceof TYPE_CLASS)) {
			if(!(rightType instanceof TYPE_NIL)) // nil is valid to class
				throwException("expression must be of class type");
			else return;
		}

		TYPE_CLASS rightClass = (TYPE_CLASS) rightType;
		if (!rightClass.isSubtypeOf(leftClass))
			throwException("Expression does not inherit from variable's class");
	}

	@Override
	public TEMP IRme() {
		TEMP src = exp.IRme();

		if (var instanceof AST_VAR_SIMPLE simplevar){
			IR.getInstance().Add_IRcommand( new IRcommand_Store(simplevar.name, src));
		}


		return null;
	}

	@Override
	protected String GetNodeName() {
		return "ASSIGN\nleft := right";
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(var, exp);
	}
}
