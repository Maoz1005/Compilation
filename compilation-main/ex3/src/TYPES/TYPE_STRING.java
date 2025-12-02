package TYPES;

public class TYPE_STRING extends TYPE {

	private static TYPE_STRING instance = null; /* USUAL SINGLETON IMPLEMENTATION ... */

	protected TYPE_STRING() {} /* PREVENT INSTANTIATION ... */

	public static TYPE_STRING getInstance() { /* GET SINGLETON INSTANCE ... */
		if (instance == null) {
			instance = new TYPE_STRING();
			instance.name = "string";
		}
		return instance;
	}
}
