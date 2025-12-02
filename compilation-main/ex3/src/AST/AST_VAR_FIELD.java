package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

import java.util.Arrays;
import java.util.List;

/**
 * Node for a field of an object.
 * @attribute var: A chain of atomic variables comprised of defined types TODO: right?
 * @attribute fieldName: The ID of the field of said object.
 */
public class AST_VAR_FIELD extends AST_VAR {
	public AST_VAR var;
	public String fieldName;
	
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int lineNum) {
		super("var -> var DOT ID( %s )", lineNum); // x.attribute

		this.var = var;
		this.fieldName = fieldName;
	}

	public TYPE SemantMe() {
		SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();
		TYPE type = var.SemantMe();
		if (!(type instanceof TYPE_CLASS)) throwException("Object is not of type class, has no fields");
		TYPE_CLASS classType = (TYPE_CLASS) type;
		TYPE fieldType = table.findMemberType(classType, fieldName);
		if (fieldType == null) { throwException("undefined field " + fieldName); }
		return fieldType;
	}

	@Override
	protected String GetNodeName() {
		return String.format("FIELD\nVAR...->%s", fieldName);
	}

	@Override
	protected List<? extends AST_Node> GetChildren() {
		return Arrays.asList(var);
	}
}