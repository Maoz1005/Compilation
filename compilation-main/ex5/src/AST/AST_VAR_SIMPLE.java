package AST;

import SYMBOL_TABLE.*;
import TYPES.TYPE;
import TEMP.*;
import IR.*;
import TYPES.*;

/**
 * Node for a simple integer/string-type variable.
 * @attribute name: The ID of the variable.
 */
public class AST_VAR_SIMPLE extends AST_VAR {

	public String name;
	public TYPE_CLASS classType; // Null if not in class
	public METADATA metadata;


	public AST_VAR_SIMPLE(String name, int lineNum) {
		super("var -> ID( %s )", lineNum);
		this.name = name;
	}


	public TYPE SemantMe() {
		TYPE type = tryTableFind(this.name);
		this.classType = SYMBOL_TABLE.getInstance().currentClass;
		this.metadata = tryTableFindMetadata(this.name);
		if (this.metadata == null) { System.out.println("METADATA HASN'T BEEN INITIALIZED FOR: " + this.name); }
		return type;
	}


	@Override
	protected String GetNodeName() {
		return String.format("SIMPLE\nVAR(%s)",name);
	}


	public TEMP IRme() {
		TEMP dst = new TEMP();
		if (this.metadata == null) {
			IR.getInstance().add(new IRcommand_Placeholder("No metadata for: " + this.name));
		}
		else if (this.metadata.getRole() == METADATA.VAR_ROLE.ATTRIBUTE) {
			IR.getInstance().add(new IRcommand_GetAttributeAddress(dst, name, metadata.getOffset(), metadata.getClassName()));
		}
		else if (this.metadata.getRole() == METADATA.VAR_ROLE.LOCAL) {
			IR.getInstance().add(new IRcommand_GetLocalAddress(dst, name, metadata.getOffset()));
		}
		else if (this.metadata.getRole() == METADATA.VAR_ROLE.GLOBAL) {
			IR.getInstance().add(new IRcommand_GetGlobalAddress(dst, name));
		}
		else if (this.metadata.getRole() == METADATA.VAR_ROLE.PARAMETER) {
			IR.getInstance().add(new IRcommand_GetParameterAddress(dst, name, metadata.getOffset()));
		}
		else {
			throwException("Failed to find metadata.");
		}
		return dst;
	}

}
