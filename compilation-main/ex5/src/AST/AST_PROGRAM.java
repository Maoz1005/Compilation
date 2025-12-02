package AST;

import IR.*;
import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import UTILITY.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AST_PROGRAM extends AST_Node{
    public List<AST_DEC> declarations;

    // just when you thought this thing couldn't get any more spaghetti
    // because I cant have multiple data sections
    private static final Set<String> stringConstants = new HashSet<>();
    private static final List<AST_CLASSDEC> classes = new ArrayList<>();
    private static final List<Pair<String, InitialConstVal>> globals = new ArrayList<>();

    public static void addClass(AST_CLASSDEC cls) {
        classes.add(cls);
    }

    public static void addGlobal(Pair<String,InitialConstVal> name){
        globals.add(name);
    }

    public static void addStringConstant(String constant){
        stringConstants.add(constant);
    }

    public AST_PROGRAM(NODE_LIST<AST_DEC> declarations, int lineNum){
        super("program -> dec+", lineNum);

        this.declarations = declarations.unroll();
    }

    @Override
    public TYPE SemantMe() {
        for (AST_DEC dec : declarations)
            dec.SemantMe();
        return null;
    }

    @Override
    protected String GetNodeName() {
        return "PROGRAM\nDECLARATIONS";
    }

    @Override
    protected List<? extends AST_Node> GetChildren() {
        return declarations;
    }

    @Override
    public TEMP IRme() {
        IR ir = IR.getInstance();
        ir.add(new IRcommand_BeginData());
        ir.add(new IRcommand_GenerateStringConsts(stringConstants));
        generateGlobals();
        generateVtables();
        ir.add(new IRcommand_BeginText());
        ir.add(new IRcommand_GenerateBuiltinFuncs());

        // the actual body:
        for (AST_DEC dec: declarations) dec.IRme();
        return null;
    }

    private void generateGlobals(){
        for (Pair<String, InitialConstVal> name_init : globals){
            IR.getInstance().add(
                    new IRcommand_Allocate_Global(
                            name_init.first,
                            name_init.second.getValue(),
                            name_init.second.isString()));
        }
    }

    private void generateVtables(){
        IR ir = IR.getInstance();

        for (AST_CLASSDEC clsDec : classes){
            List<Pair<TYPE_CLASS, TYPE_FUNCTION>> methods = clsDec.typeObject.methodsInfo;
            ir.add(new IRcommand_Label("vtable_" + clsDec.id));
            for (Pair<TYPE_CLASS, TYPE_FUNCTION> cls_method : methods){
                ir.add(new IRcommand_Word(cls_method.first.name + "_" + cls_method.second.name));
            }
        }
    }

}
