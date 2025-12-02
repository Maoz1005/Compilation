/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.Set;
import TEMP.TEMP;

/**
 * Generates unique labels (IRcommand_Label to PLACE a label.)
 */
public abstract class IRcommand {

	protected static int label_counter = 0; /* Label Factory */
	private static int index_counter = 0;
	public int index;


	public IRcommand() {
		index = index_counter++;
	}


	public static String getFreshLabel(String msg) {
		return String.format("Label_%d_%s",label_counter++,msg);
	}

	@Override
	public String toString() {
		return "PLACEHOLDER: " + super.toString();
	}

	public String toString(String log) {
		return toString(log, "");
	}

	public String toString(String log, String metadata) {
		return String.format("%-32s %-32s", log, metadata);
	}

	// Default implementation
	public void calcOut(Set<Integer> inSet) {}

	// Default implementation
	public void addTemps(Set<TEMP> temps) {}

	public abstract void MIPSme();

}
