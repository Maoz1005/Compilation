package IR;
import TEMP.*;

public abstract class IRcommand_Binop extends IRcommand {

    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop(TEMP dst,TEMP t1,TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public String toString() {
        return dst + " := " + t1 + " BINOP " + t2;
    }
}
