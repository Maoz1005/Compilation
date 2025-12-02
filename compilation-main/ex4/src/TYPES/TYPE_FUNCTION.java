package TYPES;

import AST.AST_STMT_LIST;

import java.util.List;

public class TYPE_FUNCTION extends TYPE {
	public TYPE returnType; /* The return type of the function */
	public List<TYPE> params; /* types of input params */

	public TYPE_FUNCTION(TYPE returnType, String name, List<TYPE> params) {
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TYPE_FUNCTION)) return false;
		TYPE_FUNCTION other = (TYPE_FUNCTION) obj;

		if (!returnType.equals(other.returnType)) return false;
		if (other.params.size() != params.size()) return false;
		for (int i = 0; i < params.size(); i++){
			if (!params.get(i).equals(other.params.get(i))) return false;
		}
		return true;
	}
}
