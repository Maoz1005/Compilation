package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.METADATA;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import ERRORS.SemanticException;
import TYPES.TYPE_NIL;
import TEMP.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AST_Node {
	/* The serial number is for debug purposes
	   In particular, it can help in creating
	   a graphviz dot format of the AST...     */
	public int SerialNumber;
	protected int lineNum; // Every node should host the start of the line number it was encountered in.

	public static int vardecCounter; // for local variables.
	public static int attributeCounter;
	
	public AST_Node(String derivation, int lineNum) {
		this.lineNum = lineNum;
		SerialNumber = AST_Node_Serial_Number.getFresh(); /* SET A UNIQUE SERIAL NUMBER */
		System.out.println("====================== " + derivation); /* PRINT CORRESPONDING DERIVATION RULE */
	}

	public abstract TYPE SemantMe();

	/** Attempts to enter the table entry {id, type} into the symbol_table singleton.
	 *  Activates throwException on failure.
	 *  Exists because checking for null is very common.
	 * @param id the id to insert
	 * @param type the type of id
	 */
	protected final void tryTableEnter(String id, TYPE type){
		tryTableEnter(id, type, null);
	}

	protected final void tryTableEnter(String id, TYPE type, METADATA metadata) {
		SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();
		if (table.isInCurrentScope(id)) throwException("Name " + id + " already defined in current scope");
		if (metadata == null) {
			table.enter(id, type);
		}
		else {
			table.enter(id, type, metadata);
		}
	}

	/** find() method from SYMBOL_TABLE, which automatically throws an error if the object wasn't found. */
	protected TYPE tryTableFind(String ID) {
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
		TYPE type = symbolTable.find(ID);
		if (type == null) { throwException("Name " + ID + " not found"); }
		return type;
	}

	/** Returns the variables metadata (as opposed to its TYPE) */
	protected METADATA tryTableFindMetadata(String id) {
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
		METADATA metadata = symbolTable.findMetadata(id);
		if (metadata == null) { throwException("Name " + id + " not found"); }
		return metadata;
	}

	protected abstract String GetNodeName();

	// default - 0 children
	protected List<? extends AST_Node> GetChildren() { return Arrays.asList(); }

	public final void PrintMe(){
		// print me, add me as a node, do the same to my children, log the edges to them
		System.out.println("next node: \n***\n" + GetNodeName() + "\n***");

		AST_GRAPHVIZ.getInstance().logNode( SerialNumber, GetNodeName());

		for (AST_Node child : GetChildren()){
			child.PrintMe();
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, child.SerialNumber);
		}
	}

	/**
	 * Sets the global failure line in SYMBOL_TABLE to the line of the current command, then throws an exception.
	 * @param info specific debugging info in case we implemented the semantic checker wrong
	 */
	protected final void throwException(String info){
		throw new SemanticException(info, lineNum + 1); // +1 since cup's line counter starts on 0
	}

	public TEMP IRme() {
		System.out.println("Activated unknown IR conversion on " + GetNodeName());
		return null;
	}
}
