/***********/
/* PACKAGE */
/***********/
package IR;

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
}
