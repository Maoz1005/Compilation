package SYMBOL_TABLE;

import TYPES.*;

public class SYMBOL_TABLE_ENTRY {
	int index;
	public String name;
	public TYPE type;
	public int scope;

	/* prevtop and next symbol table entries.. */
	public SYMBOL_TABLE_ENTRY prevtop;
	public SYMBOL_TABLE_ENTRY next;

	public int prevtop_index; // The prevtop_index is just for debug purposes ...

	METADATA metadata;

	/* CONSTRUCTOR(S) */
	public SYMBOL_TABLE_ENTRY(
		String name,
		TYPE type,
		int index,
		SYMBOL_TABLE_ENTRY next,
		SYMBOL_TABLE_ENTRY prevtop,
		int prevtop_index,
		int scope
	) {
		this.index = index;
		this.name = name;
		this.type = type;
		this.next = next;
		this.prevtop = prevtop;
		this.prevtop_index = prevtop_index;
		this.scope = scope;
		this.metadata = new METADATA();
	}

	public SYMBOL_TABLE_ENTRY(
			String name,
			TYPE type,
			int index,
			SYMBOL_TABLE_ENTRY next,
			SYMBOL_TABLE_ENTRY prevtop,
			int prevtop_index,
			int scope,
			METADATA metadata
	) {
		this.index = index;
		this.name = name;
		this.type = type;
		this.next = next;
		this.prevtop = prevtop;
		this.prevtop_index = prevtop_index;
		this.scope = scope;
		this.metadata = metadata;
	}
}
