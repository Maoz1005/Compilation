package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_FUNCTION;
import IR.*;
import TEMP.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_EXP_FUNC extends AST_EXP_FUNC_ABSTRACT {
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
        IR ir = IR.getInstance();

        if (id.equals(SYMBOL_TABLE.PRINT_INT)){
            ir.add(new IRcommand_PrintInt(exps.getFirst().IRme()));
            return null;
        }
        if (id.equals(SYMBOL_TABLE.PRINT_STRING)){
            ir.add(new IRcommand_PrintString(exps.getFirst().IRme()));
            return null;
        }

        List<TEMP> arguments = new ArrayList<>();
        for (AST_EXP exp : exps) {
            arguments.add(exp.IRme());
        }

        // This means that function statements that ignore the return value still take a temp away. Too bad!
        TEMP dst = new TEMP();
        ir.add(new IRcommand_CallFunc(id, arguments, dst));

        return dst;
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
