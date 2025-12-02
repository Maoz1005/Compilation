package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_VOID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_STMT_METHOD extends AST_STMT {
    public AST_VAR var;
    public String methodName;
    public List<AST_EXP> args;

    public AST_STMT_METHOD(AST_VAR var, String methodName, NODE_LIST<AST_EXP> args, int lineNum) {
        super("stmt -> var DOT ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON", lineNum); // x.method(exp, exp...);

        this.var = var;
        this.methodName = methodName;
        this.args = args.unroll();
    }

    public AST_STMT_METHOD(AST_VAR var, String methodName, int lineNum){
        super("stmt -> ID LPAREN RPAREN SEMICOLON", lineNum); // x.method();

        this.var = var;
        this.methodName = methodName;
        this.args = Arrays.asList();
    }

    public TYPE SemantMe() {
        SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();
        TYPE data = var.SemantMe();
        if (!(data instanceof TYPE_CLASS)) {
            throwException("Attempting to call method on a non-class type.");
        }
        TYPE_CLASS classData = (TYPE_CLASS) data;
        TYPE methodType = table.findMemberType(classData, methodName);
        if (!(methodType instanceof TYPE_FUNCTION)) {
            throwException("Attempting to call attribute as a method.");
        }
        TYPE_FUNCTION functionData = (TYPE_FUNCTION)methodType;
        TYPE returnType = functionData.returnType;
        List<TYPE> argsList = expsToList();
        if (!(matchTypesArgsParams(argsList, functionData.params))) throwException("bad parameters or arguments mate.");

        return returnType;
    }

    /**********************************/
    /* Duplication from AST_STMT_FUNC */
    /**********************************/
    private List<TYPE> expsToList() {
        List<TYPE> argumentTypes = new ArrayList<>();
        for (AST_EXP expression : args) {
            argumentTypes.add(expression.SemantMe());
        }
        return argumentTypes;
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\nVAR...->%s(%s)", methodName, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        List<AST_Node> children = new ArrayList<>(args);
        children.add(0, var);
        return children;
    }
}
