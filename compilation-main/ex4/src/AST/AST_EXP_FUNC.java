package AST;

import TYPES.TYPE;
import TYPES.TYPE_FUNCTION;
import IR.*;
import TEMP.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_EXP_FUNC extends AST_EXP {
    public String id;

    public List<AST_EXP> exps;

    public AST_EXP_FUNC(String id, int lineNum) {
        super(String.format("exp -> ID( %s ) LPAREN RPAREN", id), lineNum); // func()

        this.id = id;
        exps = Arrays.asList();
    }

    public AST_EXP_FUNC(String id, NODE_LIST<AST_EXP> expStar, int lineNum){
        super(String.format("exp -> ID( %s ) LPAREN exp* RPAREN", id), lineNum); // func(exp, exp...)

        this.id = id;
        exps = expStar.unroll();
    }

    public TYPE SemantMe() {
        List<TYPE> argumentTypes = expsToList();
        TYPE type = tryTableFind(this.id);
        if (!(type instanceof TYPE_FUNCTION)) { throwException("Attempt to call to something that's not a function."); }
        TYPE_FUNCTION functionData = (TYPE_FUNCTION)type;

        List<TYPE> functionParamTypes = functionData.params;
        boolean argsAreValid = matchTypesArgsParams(argumentTypes, functionParamTypes);
        if (!argsAreValid) { throwException("Mismatch between arguments and parameters."); }

        return functionData.returnType;
    }

    private List<TYPE> expsToList() {
        List<TYPE> argumentTypes = new ArrayList<>();
        for (AST_EXP expression : exps) {
            argumentTypes.add(expression.SemantMe());
        }
        return argumentTypes;
    }

    @Override
    public TEMP IRme() {
        TEMP t=null;

        if (!exps.isEmpty()) {
            // disgusting hack because printint is the only used function in this assignment
            // grab only the first parameter of the function
            t = exps.get(0).IRme();
            for (int i = 1; i < exps.size(); i++){
                exps.get(i).IRme();
            }
        }

        IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(t));

        return null;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return exps;
    }

    @Override
    protected String GetNodeName() {
        return String.format("%s(%s)", id, exps != null ? "PARAMS" : "");
    }
}
