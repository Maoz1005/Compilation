package AST;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_EXP_METHOD extends AST_EXP_FUNC_ABSTRACT {
    public AST_VAR var;
    public String methodName;
    public TYPE_CLASS classData;

    public List<AST_EXP> exps;

    public AST_EXP_METHOD(AST_VAR var, String methodName, int lineNum){
        super("exp -> var DOT ID LPAREN RPAREN", lineNum);

        this.methodName = methodName;
        this.var = var;
        exps = Arrays.asList();
    }

    public AST_EXP_METHOD(AST_VAR var, String methodName, NODE_LIST<AST_EXP> expStar, int lineNum){
        super("exp -> var DOT ID LPAREN exp* RPAREN", lineNum); // x.attribute(exp, exp...)

        this.methodName = methodName;
        this.var = var;
        exps = expStar.unroll();
    }

    public TYPE SemantMe() {
        SYMBOL_TABLE table = SYMBOL_TABLE.getInstance();
        TYPE data = var.SemantMe();
        if (!(data instanceof TYPE_CLASS)) {
            throwException("Attempting to call method on a non-class type.");
        }
        classData = (TYPE_CLASS) data;
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

    @Override
    public TEMP IRme() {
        IR ir = IR.getInstance();

        List<TEMP> arguments = new ArrayList<>();
        for (AST_EXP exp : exps) {
            arguments.add(exp.IRme());
        }

        int methodOffset = classData.getMethodIndex(methodName);

        TEMP caller = var.IRme();
        TEMP dst = new TEMP();
        ir.add(new IRcommand_CallMethod(methodName, methodOffset, caller, arguments, dst));

        return dst;
    }

    private List<TYPE> expsToList() {
        List<TYPE> argumentTypes = new ArrayList<>();
        for (AST_EXP expression : exps) {
            argumentTypes.add(expression.SemantMe());
        }
        return argumentTypes;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        List<AST_Node> children = new ArrayList<>(exps);
        children.add(0, var);
        return children;
    }

    @Override
    protected String GetNodeName() {
        return String.format("VAR.%s(%s)", methodName, exps != null ? "PARAMS" : "");
    }
}
