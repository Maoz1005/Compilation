package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_STMT_FUNC extends AST_STMT {
    public String id;
    public List<AST_EXP> args;


    public AST_STMT_FUNC(String id, NODE_LIST<AST_EXP> args, int lineNum) {
        super("stmt -> ID LPAREN (exp (COMMA exp)*)? RPAREN SEMICOLON", lineNum); // ID(exp, exp, ...);

        this.id = id;
        this.args = args.unroll();
    }

    public AST_STMT_FUNC(String id, int lineNum){
        super("stmt -> ID LPAREN RPAREN SEMICOLON", lineNum); // ID();

        this.id = id;
        this.args = Arrays.asList();
    }

    public TYPE SemantMe() {
        List<TYPE> argumentTypes = expsToList();
        TYPE type = tryTableFind(this.id);
        if (!(type instanceof TYPE_FUNCTION)) { throwException("Attempt to call to something that's not a function."); }
        TYPE_FUNCTION functionData = (TYPE_FUNCTION)type;

        List<TYPE> functionParamTypes = functionData.params;
        boolean argsAreValid = matchTypesArgsParams(argumentTypes, functionParamTypes);
        if (!argsAreValid) { throwException("Mismatch between arguments and parameters."); }

        return null;
    }

    private List<TYPE> expsToList() {
        List<TYPE> argumentTypes = new ArrayList<>();
        for (AST_EXP expression : args) {
            argumentTypes.add(expression.SemantMe());
        }
        return argumentTypes;
    }

    @Override
    protected String GetNodeName() {
        return String.format("STMT\n%s(%s)", id, (args != null ? "PARAMS" : ""));
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return args;
    }
}
