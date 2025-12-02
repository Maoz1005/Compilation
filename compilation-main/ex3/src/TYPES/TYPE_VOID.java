package TYPES;

public class TYPE_VOID extends TYPE {

	private static TYPE_VOID instance = null; /* USUAL SINGLETON IMPLEMENTATION ... */

	protected TYPE_VOID() {} /* PREVENT INSTANTIATION ... */

	public static TYPE_VOID getInstance() {
		if (instance == null) {
			instance = new TYPE_VOID();
			instance.name = "void";
		}
		return instance;
	}
}
