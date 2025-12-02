package TYPES;

import java.util.List;

public class TYPE_CLASS extends TYPE {
    public TYPE_CLASS father; /* If this class does not extend a father class this should be null  */

    /**************************************************/
    /* Gather up all data members in one place        */
    /* Note that data members coming from the AST are */
    /* packed together with the class methods         */
    /**************************************************/
    public List<TYPE_CLASS_MEMBER_DEC> data_members;

    public TYPE_CLASS(TYPE_CLASS father,String name,List<TYPE_CLASS_MEMBER_DEC> data_members) {
        this.name = name;
        this.father = father;
        this.data_members = data_members;
    }

    public boolean isSubtypeOf(TYPE_CLASS other){
        for (TYPE_CLASS current = this; current != null; current = current.father){
            if (current.equals(other)) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof TYPE_CLASS other)) return false;
        return this.name.equals(other.name);
    }
}
