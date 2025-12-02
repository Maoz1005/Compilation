package AST;

import TYPES.*;
import TEMP.*;
import IR.*;

import java.util.Arrays;
import java.util.List;

public class AST_EXP_BINOP extends AST_EXP {
	public BINOP op;
	public AST_EXP left;
	public AST_EXP right;
	public TYPE type; /* Return type.
						 INT if:
						   1. EQ(True(1)/False(0))
						   2. OP(not EQ) between Integers
						 STRING if:
						   1. PLUS between strings
					  */
	public boolean oneIsObject;

	
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
			type = TYPE_INT.getInstance();
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
			type = typeLeft;
			return typeLeft;
		}
		if (!leftIsInteger && op != BINOP.EQ) {
			throwException("BINOP without eq is only legal with strings or integers");
		}
		if (op == BINOP.DIVIDE && right instanceof AST_EXP_INT rightInt && rightInt.value == 0) {
			throwException("Explicit division by zero");
		}
		type = typeLeft; // should always be type_int at this point

		return typeLeft;
	}

	private TYPE SemantEQ(TYPE typeLeft, TYPE typeRight){
		boolean oneIsClass = typeLeft instanceof TYPE_CLASS || typeRight instanceof TYPE_CLASS;
		boolean oneIsArray = typeLeft instanceof TYPE_ARRAY || typeRight instanceof TYPE_ARRAY;
		this.oneIsObject = oneIsArray || oneIsClass;
		boolean oneIsNil = typeLeft instanceof TYPE_NIL || typeRight instanceof TYPE_NIL;

		if (typeLeft.equals(typeRight)) {
			return TYPE_INT.getInstance();
		}
		if (oneIsObject && oneIsNil) {
			return TYPE_INT.getInstance();
		}
		if (oneIsClass) {
			if (typeLeft instanceof TYPE_CLASS leftClass && typeRight instanceof TYPE_CLASS rightClass) {
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


	public TEMP IRme() {
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = new TEMP();

		if (left  != null) t1 = left.IRme(); // TODO: why are there null checks here? How can a binop have null exps?
		if (right != null) t2 = right.IRme();

		IRcommand command = null;
		boolean clampDst = false;
		switch (op) {
			case EQ -> {
					if (type instanceof TYPE_STRING) command = new IRcommand_Binop_EQ_Str(dst, t1, t2);
					else command = new IRcommand_Binop_EQ_Integers(dst, t1, t2);
				}
			case PLUS -> {
					if (type instanceof TYPE_INT) {
						command = new IRcommand_Binop_Add_Integers(dst, t1, t2);
						clampDst = true;
					}
					if (type instanceof TYPE_STRING) command = new IRcommand_Binop_Add_Strings(dst, t1, t2);
				}
			case MINUS -> {
				command = new IRcommand_Binop_Sub_Integers(dst, t1, t2);
				clampDst = true;
			}
			case TIMES ->  {
				command = new IRcommand_Binop_Mul_Integers(dst, t1, t2);
				clampDst = true;
			}
			case DIVIDE -> {
				IRPatterns.divide(dst, t1, t2);
				return dst;
			}
			case GT -> command = new IRcommand_Binop_GT_Integers(dst, t1, t2);
			case LT -> command = new IRcommand_Binop_LT_Integers(dst, t1, t2);
			default -> command = new IRcommand_Placeholder("Failed to classify BINOP IRcommand");
		}

		IR.getInstance().add(command);
		if (clampDst) IRPatterns.clampInteger(dst);
		return dst;
	}


	@Override
	public String toString() {
		return "EXP_BINOP " + op.label + " " + left.toString() + " " + right.toString() + " " + type.toString();
	}
}
