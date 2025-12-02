package AST;

import java.util.ArrayList;
import java.util.List;

// not an AST node, just a container of nodes to later be unrolled into a flat list of nodes
public class NODE_LIST<T extends AST_Node> {
    public T head;
    public NODE_LIST<T> next;

    public NODE_LIST(T head, NODE_LIST<T> next){
        this.head = head;
        this.next = next;
    }

    public List<T> unroll(){
        List<T> l = new ArrayList<>();

        NODE_LIST<T> current = this;

        while (current != null){
            l.add(current.head);
            current = current.next;
        }

        return l;
    }
}
