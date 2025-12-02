package TYPES;

import SYMBOL_TABLE.METADATA;

public class TYPE_CLASS_MEMBER_DEC {
	public TYPE t;
	public String name; // ID of the member
	public METADATA metadata; /* This is necessary because once we close a class, we're only left with this object,
								 and we *need* its metadata. */

	public TYPE_CLASS_MEMBER_DEC(TYPE t, String name, METADATA metadata) {
		this.t = t;
		this.name = name;
		this.metadata = metadata;
	}

	public String toString() {
		return (t instanceof TYPE_FUNCTION ? "FUNCTION: " : "ATTRIBUTE: ") + name; // hehe
	}
}
