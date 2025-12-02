package AST;

import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_NIL;

import java.util.List;

/** We already have AST_EXP_FUNC, this exists strictly for AST_EXP_FUNC and AST_EXP_METHOD */
public abstract class AST_EXP_FUNC_ABSTRACT extends AST_EXP {

    public AST_EXP_FUNC_ABSTRACT(String derivation, int lineNum) {
        super(derivation, lineNum);
    }

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
