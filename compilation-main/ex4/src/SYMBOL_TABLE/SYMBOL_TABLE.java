package SYMBOL_TABLE;

import TYPES.*;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * This class appears to be an implementation of a symbol table we've talked about from recitation 6.
 * This table is essentially a stack comprised of SYMBOL_TALBE_ENTRY's, except you can actually iterate through the
 * stack without pop()ing every element (So, it's essentially a list with pop and push.)
 *
 * The symbol table is singular, as in - there isn't a separate symbol table for each scope. The table contains all scopes.
 * The class implements a Singleton architecture, meaning every time we want to access the symbol table we need to
 * .getIstance() and then call whatever method it is we want to use.
 *
 * Henceforth we might want to treat the class as a black box. We call a function with the parameters it wants, and
 * the implementation is correct (Quality coding!)
 *
 * Functions to look out for:
 * 1. enter(...) - Inserts a value into the symbol table.
 * 2. find(...) - Finds the FIRST occurrence of a value and returns it.
 * 3. beginScope() - Creates a new scope within the symbol table.
 * 4. endScope() - zzz...
 * 5. isInCurrentScope(...) - might not work (should test)
 *
 * TODO:
 * 1. Why is this hash function so fucked
 */
public class SYMBOL_TABLE {
	private final int hashArraySize = 13;
	
	/* The actual symbol table data structure... */
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top;
	private int top_index = 0;

	/* 1 2 3 4
	/* A very primitive hash function for exposition purposes ... */
	private int hash(String s) {
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
	}

	/* If currently in a function, what type should be returned. null if not in a function. */
	private final int GLOBALSCOPE = 0;
	private TYPE expectedReturnType;
	private int scopeCounter = GLOBALSCOPE; // for each scope we enter, this increments. for each scope exited, this decrements.

	public TYPE_CLASS currentClass; // specifically for searching for inherited fields inside subclass methods

	/* Enter a variable, function, class type or array type to the symbol table */
	/**
	 * Inserts a value (variable, function, class or type) into the symbol table.
	 * @param name: ID.
	 * @param type: The type of the identifier.
	 */
	public void enter(String name, TYPE type) {
		int hashValue = hash(name); // [1] Compute the hash value for this new entry

		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		SYMBOL_TABLE_ENTRY next = table[hashValue];

		/* Dekel: Chain the new entry to be the top of the table, so it's the first entry. */
		SYMBOL_TABLE_ENTRY newEntry = new SYMBOL_TABLE_ENTRY(name, type, hashValue, next, top, top_index++, scopeCounter); // [3] Prepare a new symbol table entry with name, type, next and prevtop
		top = newEntry; // [4] Update the top of the symbol table ...
		table[hashValue] = newEntry; // [5] Enter the new entry to the table
		PrintMe(); // [6] Print Symbol Table
	}

	/* Find the inner-most scope element with name */
	/**
	 * Match "name" with its first occurrence in the table (TODO: right?)
	 * @return the type of the entry, and NULL if not found.
	 */
	public TYPE find(String name) {
		SYMBOL_TABLE_ENTRY found = find_entry(name);
		if (currentClass != null && (found == null || found.scope == GLOBALSCOPE)) {
			TYPE memType = findMemberType(currentClass, name);
			// returns the memtype if it's not null, otherwise return found.type if it's not null, otherwise null
			return memType != null ? memType : (found != null ? found.type : null); // ahh yes... the rare and highly dangerous double ternary operator
		}
		if (found == null) return null;
		return found.type;
	}

	private SYMBOL_TABLE_ENTRY find_entry(String name){
		SYMBOL_TABLE_ENTRY current_entry = table[hash(name)];
		while (current_entry != null) {
			if (name.equals(current_entry.name)) {
				break;
			}
			current_entry = current_entry.next;
		}

		return current_entry;
	}

	public boolean isInCurrentScope(String name){
		SYMBOL_TABLE_ENTRY entry = find_entry(name);
		if (entry == null) return false;
		return entry.scope == scopeCounter;
	}

	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope() {
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be ablt to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		enter(
			"SCOPE-BOUNDARY",
			new TYPE_FOR_SCOPE_BOUNDARIES("NONE")
			);
		scopeCounter++;
		PrintMe(); // Print the symbol table after every change
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope() {
		/**
		 * Eliminate all elements of the list up until "SCOPE-BOUNDARY" (including)
		 */
		while (top.name != "SCOPE-BOUNDARY") { // Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit
			table[top.index] = top.next;
			top_index = top_index-1;
			top = top.prevtop;
		}
		/* Pop the SCOPE-BOUNDARY sign itself */		
		table[top.index] = top.next;
		top_index = top_index-1;
		top = top.prevtop;

		scopeCounter--;
		PrintMe(); // Print the symbol table after every change
	}

	/**
	 * @return true iff table is currently in global scope
	 */
	public boolean inGlobalScope(){
		return scopeCounter == GLOBALSCOPE;
	}


	/**
	 * Returns the type of an attribute, or null on failure.
	 * @param classType The TYPE object of a class
	 * @param memberName The name of the attribute
	 * @return The TYPE of the attribute.
	 */
	public TYPE findMemberType(TYPE_CLASS classType, String memberName) {
		TYPE_CLASS currentClass = classType;

		while (currentClass != null) {
			TYPE_CLASS_MEMBER_DEC desiredMember = findMemberInClass(currentClass, memberName);
			if (desiredMember != null) {
				return desiredMember.t;
			}
			currentClass = currentClass.father;
		}

		return null;
	}

	/**
	 * Finds a member of a class
	 * @param currentClass the name of the class
	 * @param memberName The name of the desired member
	 * @return The desired member, null if not found.
	 */
	private TYPE_CLASS_MEMBER_DEC findMemberInClass(TYPE_CLASS currentClass, String memberName) {
		List<TYPE_CLASS_MEMBER_DEC> data_members = currentClass.data_members;
		for (TYPE_CLASS_MEMBER_DEC member : data_members) {
			if (member.name.equals(memberName)) {
				return member;
			}
		}
		return null;
	}

	/**
	 * Used for functions and methods to relay information to return statements about the expected return type.
	 * Use after function semantic ends with null as the type to reset the state and prevent shenanigans
	 * @param type the type to expect, null if exiting function
	 */
	public void setExpectedReturnType(TYPE type){
		expectedReturnType = type;
	}

	public TYPE getExpectedReturnType(){
		return expectedReturnType;
	}
	
	public static int n=0;
	
	public void PrintMe() {
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try {
			PrintWriter fileWriter = new PrintWriter(dirname+filename); // [1] Open Graphviz text file for writing

			/* [2] Write Graphviz dot prolog */
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/* [3] Write Hash Table Itself */
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/* [4] Loop over hash table array and print all linked lists per array cell */
			for (i=0;i<hashArraySize;i++) {
				if (table[i] != null) {
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i); // [4a] Print hash table array[i] -> entry(i,0) edge
				}

				j = 0;
				for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next) {
					/* [4b] Print entry(i,it) node */
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtop_index);

					if (it.next != null) {
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private static SYMBOL_TABLE instance = null; /* USUAL SINGLETON IMPLEMENTATION ... */

	protected SYMBOL_TABLE() {} /* PREVENT INSTANTIATION ... */
	/**
	 * Creates a new instance of SYMBOL_TABLE if it hasn't been made already. (Singleton!)
	 * @return a SYMBOL_TABLE
	 */
	public static SYMBOL_TABLE getInstance() {
		if (instance == null) {
			instance = new SYMBOL_TABLE(); /* [0] The instance itself ... */

			/* [1] Enter primitive types int, string */
			instance.enter("int",   TYPE_INT.getInstance());
			instance.enter("string",TYPE_STRING.getInstance());
			instance.enter("void", TYPE_VOID.getInstance()); /* [2] How should we handle void ??? */
			instance.enter("nil", TYPE_NIL.getInstance());

			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter(
				"PrintInt",
				new TYPE_FUNCTION(
						TYPE_VOID.getInstance(),
						"PrintInt",
						Arrays.asList(TYPE_INT.getInstance())));
			instance.enter(
					"PrintString",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintString",
							Arrays.asList(TYPE_STRING.getInstance())));
		}
		return instance;
	}

	public void printSymbolTable() { // Debugging purposes
		System.out.println("!Printing symbol table!");
		for (SYMBOL_TABLE_ENTRY entry: table) {
			SYMBOL_TABLE_ENTRY current = entry;
			while (current != null){
				System.out.println("Index: " + current.index);
				System.out.println("Name: " + current.name);
				System.out.println("Type: " + current.type.name);
				System.out.println();
				current = current.next;
			}
		}
	}
}
