package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_RETURN extends AST_STMT {
    public AST_EXP exp;

    public AST_STMT_RETURN(AST_EXP exp, int lineNum){
        super("stmt -> RETURN exp", lineNum); // return exp

        this.exp = exp;
    }

    public AST_STMT_RETURN(int lineNum){
        super("stmt -> RETURN", lineNum);
    }

    public TYPE SemantMe() {
        SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
        TYPE expectedReturnType = symbolTable.getExpectedReturnType();

        // void function
        if (expectedReturnType instanceof TYPE_VOID){
            if (exp == null) return null;
            else throwException("Function defined as void can't have a return expression");
        }

        // not void function
        TYPE returnType = exp.SemantMe();
        if (returnType instanceof TYPE_CLASS && expectedReturnType instanceof TYPE_CLASS) {
            if (((TYPE_CLASS)returnType).isSubtypeOf((TYPE_CLASS)expectedReturnType)) { // Check inheritance
                return null;
            }
            throwException("Invalid return inheritance type.");
        }
        // Check if return is object
        if (returnType instanceof TYPE_NIL &&
                (expectedReturnType instanceof TYPE_CLASS || expectedReturnType instanceof TYPE_ARRAY)) {
            return null;
        }
        // Check if matches
        if (!returnType.equals(expectedReturnType)) {
            throwException("Invalid return type.");
        }
        return null; // matching primitives
    }

    @Override
    public String GetNodeName(){
        return "RETURN";
    }

    @Override
    public List<? extends AST_Node> GetChildren(){
        if (exp == null) return Arrays.asList();
        return Arrays.asList(exp);
    }
}
