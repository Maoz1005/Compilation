package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

// utility class to generate common patterns
public class IRPatterns {

    private IRPatterns() {} // static class
    public static int MAX_INT = 32767;
    public static int MIN_INT = -32768;
    public static final String DIV_BY_ZERO_LABEL = "div_by_zero_err";
    public static final String INVALID_DEREF_LABEL = "invalid_deref_err";
    public static final String OUT_OF_BOUNDS_LABEL = "out_of_bounds_err";


    public static void clampInteger(TEMP dst){
        TEMP minval = new TEMP();
        TEMP maxval = new TEMP();
        TEMP comparisonResult = new TEMP();
        String clmpLower = IRcommand.getFreshLabel("clamp_int_lower");
        String clmpHigher = IRcommand.getFreshLabel("clamp_int_higher");
        String clmpEnd = IRcommand.getFreshLabel("clamp_int_end");

        IR ir = IR.getInstance();

        // load max/min values for an integer
        ir.add(new IRcommand_ConstInt(minval, MIN_INT));
        ir.add(new IRcommand_ConstInt(maxval, MAX_INT));

        // jump to clamps if necessary
        ir.add(new IRcommand_Binop_LT_Integers(comparisonResult, dst, maxval));
        ir.add(new IRcommand_Jump_If_Eq_To_Zero(comparisonResult, clmpHigher));
        ir.add(new IRcommand_Binop_GT_Integers(comparisonResult, dst, minval));
        ir.add(new IRcommand_Jump_If_Eq_To_Zero(comparisonResult, clmpLower));
        ir.add(new IRcommand_Jump_Label(clmpEnd));

        // clamp to max
        ir.add(new IRcommand_Label(clmpHigher));
        ir.add(new IRcommand_Move(dst, maxval));
        ir.add(new IRcommand_Jump_Label(clmpEnd));

        // clamp to min
        ir.add(new IRcommand_Label(clmpLower));
        ir.add(new IRcommand_Move(dst, minval));

        // end
        ir.add(new IRcommand_Label(clmpEnd));
    }

    public static void divide(TEMP dst, TEMP t1, TEMP t2){
        IR ir = IR.getInstance();

        ir.add(new IRcommand_Jump_If_Eq_To_Zero(t2, DIV_BY_ZERO_LABEL, true));
        ir.add(new IRcommand_Binop_Div_Integers(dst, t1, t2));
    }

    public static void checkNullRef(TEMP pointer){
        IR ir = IR.getInstance();

        ir.add(new IRcommand_Jump_If_Eq_To_Zero(pointer, INVALID_DEREF_LABEL, true));
    }

    public static void checkArrBounds(TEMP idx, TEMP arrSize){
        IR ir = IR.getInstance();

        ir.add(new IRcommand_BranchLTZ(idx, OUT_OF_BOUNDS_LABEL, true));
        ir.add(new IRcommand_BranchGE(idx, arrSize, OUT_OF_BOUNDS_LABEL, true));
    }
}
