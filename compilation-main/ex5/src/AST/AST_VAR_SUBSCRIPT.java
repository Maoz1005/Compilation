package AST;

import TYPES.TYPE;
import SYMBOL_TABLE.*;
import TYPES.*;
import IR.*;
import TEMP.*;

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
	public METADATA metadata;
	public String varName;
	public TYPE_CLASS classType;
	
	public AST_VAR_SUBSCRIPT(AST_VAR var, AST_EXP subscript, int lineNum) {
		super("var -> var [ exp ]", lineNum); // x[index]

		this.var = var;
		this.subscript = subscript;
	}

	public TYPE SemantMe() {
		TYPE subscriptType = subscript.SemantMe();
		if (!(subscriptType instanceof TYPE_INT)) { // Note: doesn't seem like you need to validate indices.
			throwException("Index must be an integer.");
		}
		TYPE varType = var.SemantMe();
		if (!(varType.isArray())) {
			throwException("Accessing an index is only valid with arrays.");
		}
		if (this.var instanceof AST_VAR_FIELD varField) {
			this.varName = varField.fieldName;
			this.metadata = null; // Doesn't necessarily exist on the symbol table.
		}
		else if (this.var instanceof AST_VAR_SIMPLE varSimple) {
			this.varName = varSimple.name;
			this.metadata = tryTableFindMetadata(varSimple.name);
		}
		this.classType = SYMBOL_TABLE.getInstance().currentClass;
		TYPE_ARRAY arrType = (TYPE_ARRAY)varType;
		return arrType.typeOfElements;
	}

	public TEMP IRme() {
		IR ir = IR.getInstance();

		System.out.println("for " + id + ": calculating " + var.id + "[" + subscript + "]");
		TEMP ptrToArray = var.IRme();
		TEMP index = subscript.IRme();
		TEMP array = new TEMP();
		TEMP size = new TEMP();
		TEMP indexPlus1 = new TEMP();
		TEMP shiftedOffset = new TEMP();
		TEMP dst = new TEMP();


		ir.add(new IRCommand_LoadWithOffset(array, ptrToArray, 0)); // get address saved in address
		ir.add(new IRCommand_LoadWithOffset(size, array, 0)); // get size of array
		ir.add(new IRcommand_BranchLTZ(index, IRPatterns.OUT_OF_BOUNDS_LABEL, true)); // index < 0
		ir.add(new IRcommand_BranchGE(index, size, IRPatterns.OUT_OF_BOUNDS_LABEL, true)); // index >= size

		ir.add(new IRcommand_AddImmediate(indexPlus1, index, 1)); // index + 1
		ir.add(new IRcommand_SLL(shiftedOffset, indexPlus1, 2)); // multiply by 4
		ir.add(new IRcommand_Binop_Add_Integers(dst, shiftedOffset, array)); // array[shiftedOffset]

		return dst;
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
