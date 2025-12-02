package TEMP;


/**
 * Class to get fresh temporaries (register-esc objects) from
 */
public class TEMP {
	private static int nextSerialNum = 0;
	private int serial=0;

	public TEMP() {
		this.serial = nextSerialNum++;
	}

	public TEMP(int serial){ // TODO: we will probably never use this. Delete
		this.serial = serial;
	}
	
	public int getSerialNumber()
	{
		return serial;
	}

	@Override
	public String toString() {
		return "t" + serial;
	}
}
