package TYPES;

public class TYPE_ARRAY extends TYPE {
    // Note: "name" (from TYPE) will be the type of the elements.
    public TYPE typeOfElements;

    @Override
    public boolean isArray() {
        return true;
    }

    public TYPE_ARRAY(String name, TYPE typeOfElements) {
        this.name = name;
        this.typeOfElements = typeOfElements;
    }
}
