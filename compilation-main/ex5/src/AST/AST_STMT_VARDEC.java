package AST;

import SYMBOL_TABLE.METADATA;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE;

import java.util.Arrays;
import java.util.List;

public class AST_STMT_VARDEC extends AST_STMT {
    public AST_VARDEC dec;

    public AST_STMT_VARDEC(AST_VARDEC dec, int lineNum) {
        super("stmt -> vardec", lineNum);

        this.dec = dec;
    }

    @Override
    public TEMP IRme() {
        return dec.IRme();
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return Arrays.asList(dec);
    }

    @Override
    public TYPE SemantMe() {
        return dec.SemantMe();
    }

    @Override
    protected String GetNodeName() {
        return "STMT\nVARDEC";
    }
}
