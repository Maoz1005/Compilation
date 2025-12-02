package AST;

import TYPES.*;

import java.util.Arrays;
import java.util.List;

public class AST_EXP_BINOP extends AST_EXP {
	public BINOP op;
	public AST_EXP left;
	public AST_EXP right;
	
	public AST_EXP_BINOP(AST_EXP left, BINOP op, AST_EXP right, int lineNum) {
		super("exp -> exp binop exp", lineNum);

		this.left = left;
		this.right = right;
		this.op = op;
	}

	@Override
	public TYPE SemantMe() {
		TYPE typeLeft, typeRight;

		typeLeft = left.SemantMe();
		typeRight = right.SemantMe();

		if (op == BINOP.EQ) { // equality special case
			return SemantEQ(typeLeft, typeRight);
		}

		boolean typesMismatch = !typeLeft.equals(typeRight);
		boolean isString = (typeLeft == TYPE_STRING.getInstance());
		boolean notPlus = op != BINOP.PLUS;
		boolean leftIsInteger = (typeLeft instanceof TYPE_INT);
		if (typesMismatch) {
			throwException("BINOP only works on identical types.");
		}
		if (isString) {
			if (notPlus) {
				throwException("Illegal operation with strings.");
			}
			return typeLeft;
		}
		if (!leftIsInteger && op != BINOP.EQ) {
			throwException("BINOP without eq is only legal with strings or integers");
		}
		if (op == BINOP.DIVIDE && right instanceof AST_EXP_INT rightInt && rightInt.value == 0) { // TODO: might not work on nova
			throwException("Explicit division by zero");
		}
		return typeLeft;
	}

	private TYPE SemantEQ(TYPE typeLeft, TYPE typeRight){
		boolean oneIsClass = typeLeft instanceof TYPE_CLASS || typeRight instanceof TYPE_CLASS;
		boolean oneIsArray = typeLeft instanceof TYPE_ARRAY || typeRight instanceof TYPE_ARRAY;
		boolean oneIsObject = oneIsArray || oneIsClass;
		boolean oneIsNil = typeLeft instanceof TYPE_NIL || typeRight instanceof TYPE_NIL;

		if (typeLeft.equals(typeRight)) {
			return TYPE_INT.getInstance();
		}
		if (oneIsObject && oneIsNil) {
			return TYPE_INT.getInstance();
		}
		if (oneIsClass) {
			if (typeLeft instanceof TYPE_CLASS && typeRight instanceof TYPE_CLASS) {
				TYPE_CLASS leftClass = (TYPE_CLASS) typeLeft;
				TYPE_CLASS rightClass = (TYPE_CLASS) typeRight;
				if (leftClass.isSubtypeOf(rightClass) || rightClass.isSubtypeOf(leftClass)) {
					return TYPE_INT.getInstance();
				}
			}
		}
		throwException("Illegal EQ operation");
		return null; // Unreachable
	}

	@Override
	protected String GetNodeName() {
		return "EXP_BINOP(" + op.label + ")";
	}
	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(left, right);
	}
}
