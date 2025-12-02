package SYMBOL_TABLE;

import TYPES.*;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;


public class SYMBOL_TABLE {
	public static final String PRINT_INT = "PrintInt";
	public static final String PRINT_STRING = "PrintString";

	private final int hashArraySize = 1;

	/* The actual symbol table data structure... */
	// v Incase this confuses you: the symbol table is a linked-list made of SYMBOL_TABLE_ENTRY's,
	// the hash table only exists for performance reasons.
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top;
	private int top_index = 0;

	/* Data about the current run */
	private final int GLOBALSCOPE = 0;
	private TYPE expectedReturnType;
	private int scopeCounter = GLOBALSCOPE;
	public TYPE_CLASS currentClass;
	public TYPE_FUNCTION currentFunction;

	/** Gets a type if we're currently in a function, otherwise null. */
	public TYPE getExpectedReturnType(){ return expectedReturnType; }
	public void setExpectedReturnType(TYPE type){ expectedReturnType = type; }
	public int getScopeCounter() { return scopeCounter; }
	/** Returns true if we're currently in the global scope. */
	public boolean inGlobalScope(){ return scopeCounter == GLOBALSCOPE; }

	/* If you're confused by this: I'm keeping the option for a hash function, but we're not actually using one atm. */
	private int hash(String s) { return 0; }

	/** Inserts a name and its associated TYPE to the table. */
	public void enter(String name, TYPE type) { enter(name, type, null); }

	public void enter(String name, TYPE type, METADATA metadata) {
		int hashValue = hash(name);
		SYMBOL_TABLE_ENTRY next = table[hashValue];

		/* Dekel: Chain the new entry to be the top of the table, so it's the first entry. */
		SYMBOL_TABLE_ENTRY newEntry;
		if (metadata == null) {
			newEntry = new SYMBOL_TABLE_ENTRY(name, type, hashValue, next, top, top_index++, scopeCounter);
		}
		else {
			newEntry = new SYMBOL_TABLE_ENTRY(name, type, hashValue, next, top, top_index++, scopeCounter, metadata);
		}

		top = newEntry; // [4] Update the top of the symbol table ...
		table[hashValue] = newEntry; // [5] Enter the new entry to the table
		PrintMe(); // [6] Print Symbol Table
	}

	/**
	 * Match "name" with its first occurrence in the table
	 * @return the type of the entry, and NULL if not found.
	 */
	public TYPE find(String name) {
		SYMBOL_TABLE_ENTRY found = find_entry(name);
		boolean inClass = (this.currentClass != null);
		boolean isFound = (found != null);
		if (inClass && (!isFound || found.scope == GLOBALSCOPE)) { // Possibly an attribute.
			TYPE memberType = findMemberType(currentClass, name);
			if (memberType != null) { return memberType; } // Certainly an attribute!
		}
		if (isFound) return found.type;
		return null;
	}

	/** Scans the table for the first match with "name" */
	private SYMBOL_TABLE_ENTRY find_entry(String name){
		SYMBOL_TABLE_ENTRY current_entry = table[hash(name)];
		while (current_entry != null) {
			if (name.equals(current_entry.name)) { break; }
			current_entry = current_entry.next;
		}
		return current_entry;
	}

	/** Returns the type of an attribute, or null on failure. */
	public TYPE findMemberType(TYPE_CLASS classType, String memberName) {
		TYPE_CLASS_MEMBER_DEC member = findMember(classType, memberName);
		return member != null ? member.t : null;
	}

	public TYPE_CLASS_MEMBER_DEC findMember(TYPE_CLASS classType, String memberName) {
		TYPE_CLASS currentClass = classType;
		while (currentClass != null) {
			TYPE_CLASS_MEMBER_DEC desiredMember = findMemberInClass(currentClass, memberName);
			if (desiredMember != null) { return desiredMember; }
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
			if (member.name.equals(memberName)) { return member; }
		}
		return null;
	}

	/** find() for metadata. (Duplicated logic) */
	public METADATA findMetadata(String name) {
		SYMBOL_TABLE_ENTRY found = find_entry(name);
		boolean inClass = (currentClass != null);
		boolean isFound = (found != null);
		if (inClass && (!isFound || found.scope == GLOBALSCOPE)) { // Possibly an attribute.
			TYPE_CLASS_MEMBER_DEC member = findMember(currentClass, name);
			if (member != null) { return member.metadata; } // Certainly an attribute!
		}
		if (isFound) return found.metadata;
		return null;
	}

	public boolean isInCurrentScope(String name){
		SYMBOL_TABLE_ENTRY entry = find_entry(name);
		if (entry == null) return false;
		return entry.scope == scopeCounter;
	}

	/** begin scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	public void beginScope() {
		enter(
			"SCOPE-BOUNDARY",
			new TYPE_FOR_SCOPE_BOUNDARIES("NONE")
			);
		scopeCounter++;
		PrintMe(); // Print the symbol table after every change
	}

	/** Eliminate all elements of the list up until "SCOPE-BOUNDARY" (including) */
	public void endScope() {
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

	/* ################################## */
	/* SINGLETON IMPLEMENTATION + PRINTME */
	/* ################################## */
	private static SYMBOL_TABLE instance = null;
	protected SYMBOL_TABLE() {}
	/** Obtain the symbol table object, or create a new one if it doesn't exist. */
	public static SYMBOL_TABLE getInstance() {
		if (instance == null) {
			instance = new SYMBOL_TABLE(); /* [0] The instance itself ... */

			/* [1] Enter primitive types int, string */
			instance.enter("int",   TYPE_INT.getInstance());
			instance.enter("string",TYPE_STRING.getInstance());
			instance.enter("void", TYPE_VOID.getInstance());
			instance.enter("nil", TYPE_NIL.getInstance());

			/* [3] Enter library function PrintInt */
			instance.enter(PRINT_INT, new TYPE_FUNCTION(
															TYPE_VOID.getInstance(),
															PRINT_INT,
															Arrays.asList(TYPE_INT.getInstance())
															)
			);
			instance.enter( PRINT_STRING, new TYPE_FUNCTION(
															TYPE_VOID.getInstance(),
															PRINT_STRING,
															Arrays.asList(TYPE_STRING.getInstance())
															)
			);
		}
		return instance;
	}

	/** Some messed up print function */
	public void PrintMe() {
		int i=0;
		int j=0;
		int counter=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",counter++);

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
}
