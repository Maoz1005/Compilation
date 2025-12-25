package ir;

import temp.Temp;

public class IrCommandPrintInt {
    Temp t;

    public IrCommandPrintInt(Temp t)
    {
        this.t = t;
    }

    @Override
    public String toString() {
        return "print " + t;
    }
}
