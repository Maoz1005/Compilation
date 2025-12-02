package TYPES;

import SYMBOL_TABLE.SYMBOL_TABLE;

public abstract class TYPE {
	public String name; /*  Every type has a name ... Neri: this IS the name of the type, like int and string
													  identifiers are kept in the symbol table as a separate field
													  every entry has a name and a type).
													  */

	public boolean isArray(){ return false; } // TODO

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TYPE other))
			return false;
		return this.name.equals(other.name);
	}
}
