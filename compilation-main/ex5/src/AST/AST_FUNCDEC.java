package AST;

import MIPS.MIPSGenerator;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;
import UTILITY.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST_FUNCDEC extends AST_DEC {
    public AST_TYPE returnTypeNode;
    public String id;
    public AST_STMT_LIST statements;
    public List<AST_FUNCPARAM> params;
    public String className; // Useful for naming the method
    public int localVarAmount; // to be found at the end of semantme

    public AST_FUNCDEC(AST_TYPE returnTypeNode, String id, NODE_LIST<AST_FUNCPARAM> params, AST_STMT_LIST statements, int lineNum) {
        super("funcDec -> type ID LPAREN (type ID (COMMA type ID)*)? RPAREN LBRACE stmt (stmt)* RBRACE", lineNum);

        this.returnTypeNode = returnTypeNode;
        this.id = id;
        this.statements = statements;
        if (params != null) this.params = params.unroll();
        else this.params = Arrays.asList();
    }

    public TYPE SemantMe() {
        // Initialization
        SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
        if (symbolTable.getExpectedReturnType() != null) // yea this probably never happens
            throwException("Nested function declaration.");
        TYPE returnType = returnTypeNode.SemantMe();
        List<TYPE> paramTypes = new ArrayList<>();
        TYPE_FUNCTION thisType = new TYPE_FUNCTION(returnType, id, paramTypes);
        tryTableEnter(id, thisType);

        // Beginning of function
        symbolTable.currentFunction = thisType;
        vardecCounter = 0;
        symbolTable.beginScope();

        System.out.println("\nFunction Name: " + id);
        List<Pair<String, TYPE>> idTypeList = new ArrayList<>();
        int offset = 1;
        for (AST_FUNCPARAM param : params) { // SemantMe on parameters
            String paramName = param.id;
            TYPE paramType = param.SemantMe(offset);
            paramTypes.add(paramType);
            Pair<String, TYPE> idType = new Pair<>(paramName, paramType);
            System.out.println("paramName: " + paramName + ", paramType: " + paramType);
            idTypeList.add(idType);
            offset++;
        }

        symbolTable.setExpectedReturnType(returnType);
        statements.SemantMe();
        localVarAmount = vardecCounter; // hope this le works

        if (symbolTable.currentClass != null) { this.className = symbolTable.currentClass.name;}

        symbolTable.setExpectedReturnType(null);
        symbolTable.endScope();
        vardecCounter = 0; // Paranoia
        symbolTable.currentFunction = null;

        return thisType;
    }

    public TEMP IRme() {
        IR ir = IR.getInstance();
        String labelName;
        if (className != null) labelName = className + "_" + id; // Method names are inherently unique (Person_getAge:..)
        else labelName = id; // Global function names are also inherently unique.
        ir.add(new IRcommand_FuncDec(labelName, localVarAmount));

        for (AST_FUNCPARAM param : params) { // Logging purposes
            param.IRme();
        }
        statements.IRme();


        if (!(statements.getStatements().getLast() instanceof AST_STMT_RETURN))
            ir.add(new IRcommand_Return(id.equals("main")));
        return null;
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        List<AST_Node> children;

        if (params != null) children = new ArrayList<>(params);
        else children = new ArrayList<>();

        children.add(statements);
        children.add(0, returnTypeNode);

        return children;
    }


    @Override
    protected String GetNodeName() {
        return String.format("DEC\nFUNC(%s)", id);
    }

}
