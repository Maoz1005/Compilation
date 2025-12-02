package AST;

import IR.InitialConstVal;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE;
import IR.*;

import java.util.Arrays;
import java.util.List;

public class AST_CFIELD_VAR extends AST_CFIELD {
    public AST_VARDEC varDec;

    public AST_CFIELD_VAR(AST_VARDEC varDec, int lineNum) {
        super("CField -> varDec", lineNum);

        this.varDec = varDec;
    }

    public TYPE SemantMe() {
        if (varDec.exp != null && !varDec.exp.isConstant()) throwException("field may only be initialized with a constant value");
        InitialConstVal initCfield = new InitialConstVal(varDec.exp);
        SYMBOL_TABLE.getInstance().currentClass.addInitialVal(initCfield);

        return varDec.SemantMe();
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(varDec);
    }

    @Override
    protected String GetNodeName() {
        return "CFIELD\nVARDEC";
    }

    public TEMP IRme() {
        return null;
    }
}
