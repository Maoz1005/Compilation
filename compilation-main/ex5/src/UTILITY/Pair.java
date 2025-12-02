package UTILITY;

public class Pair<T,E> {

    public T first;
    public E second;

    public Pair(T a, E b) {
        this.first = a;
        this.second = b;
    }

    public String toString() {
        return "<" + first + ", " + second + ">";
    }
}
