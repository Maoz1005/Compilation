package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_CLASS_MEMBER_DEC;
import ERRORS.SemanticException;
import TYPES.TYPE_NIL;

import java.util.Arrays;
import java.util.List;

public abstract class AST_Node {
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	protected int lineNum; // Every node should host the start of the line number it was encountered in.
	
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
		SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();
		if (table.isInCurrentScope(id)) throwException("Name " + id + " already defined in current scope");
		table.enter(id, type);
	}

	/**
	 * find() method from SYMBOL_TABLE, which automatically throws an error if the object wasn't found.
	 * @param ID The ID we're attempting to find
	 * @return The TYPE-class of the object if found.
	 */
	protected TYPE tryTableFind(String ID) {
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
		TYPE type = symbolTable.find(ID);
		if (type == null) { throwException("Name " + ID + " not found"); }
		return type;
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
		// TODO: find way to access current line number, then set some global variable to it so we could write the error line to file
		// we were hinted on a way to find this line number in the pdf... something to do with left/right symbol...
//		SYMBOL_TABLE.getInstance().printSymbolTable(); Doesnt actually work they overrode the default printme function :/
		throw new SemanticException(info, lineNum + 1); // +1 since cup's line counter starts on 0
	}


	// horrible, terrible, not good, very bad idea to write this function here of all places
	// too bad!
	/**
	 * compares a list of parameter rtypes to parameter ltypes to check if a function received proper parameters
	 * @param argsTypes the rtypes passed to the function
	 * @param paramsTypes the ltypes defined in the function
	 * @return true iff rtypes match ltypes
	 */
	protected static boolean matchTypesArgsParams(List<TYPE> argsTypes, List<TYPE> paramsTypes) {
		if (argsTypes.size() != paramsTypes.size()) { return false; }
		for (int i = 0; i < argsTypes.size(); i++) {
			TYPE currentArgument = argsTypes.get(i);
			TYPE currentParameter = paramsTypes.get(i);
			boolean typesMatch = (currentParameter.name).equals(currentArgument.name);
			boolean isClass = currentArgument instanceof TYPE_CLASS && currentParameter instanceof TYPE_CLASS;
			if (isClass) {
				return ((TYPE_CLASS) currentArgument).isSubtypeOf((TYPE_CLASS) currentParameter);
			}
			boolean objectAndNil = (currentArgument instanceof TYPE_NIL) && (currentParameter.isArray() ||
					currentParameter instanceof TYPE_CLASS);
			if (!(typesMatch || objectAndNil)) { return false; }
		}
		return true;
	}
}
