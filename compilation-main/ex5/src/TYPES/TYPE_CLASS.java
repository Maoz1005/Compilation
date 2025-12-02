package TYPES;

import IR.InitialConstVal;
import UTILITY.Pair;

import java.util.ArrayList;
import java.util.List;

public class TYPE_CLASS extends TYPE {
    public TYPE_CLASS father; /* If this class does not extend a father class this should be null  */

    public List<TYPE_CLASS_MEMBER_DEC> data_members;

    /*
    Explanation about these: I kept them for two reasons:
    1. They really help with logging during SemantMe and more importantly-
    2. This is the logic which helps us sort the name of each method and whether or not it was overriden (and by who)
       (This is what makes the ".word ClassName_methodName" work)

    There's most certainly a more elegant solution, but honestly, this one "just works". Writing an alternative
    implementation will take a while with the only benefit being getting to remove some methods and attributes from CLASS_DEC :/

    (If you do decide it's not for your liking the giant methods I defined in CLASSDEC and their corresponding attributes
     are useless)
     */
    public List<Pair<TYPE_CLASS,TYPE_FUNCTION>> methodsInfo;
    public List<String> attributes;
    private final List<InitialConstVal> initialValues; // for every attribute by order, saves the initial value (0 if not provided)

    public TYPE_CLASS(TYPE_CLASS father,String name,List<TYPE_CLASS_MEMBER_DEC> data_members) {
        this.name = name;
        this.father = father;
        this.data_members = data_members;
        this.methodsInfo = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.initialValues = new ArrayList<>();
    }

    public void addInitialVal(InitialConstVal val){
        initialValues.add(val);
    }

    public List<InitialConstVal> getInitialValues() {
        List<InitialConstVal> totalList = new ArrayList<>();

        if (father != null) totalList.addAll(father.getInitialValues());
        totalList.addAll(initialValues);
        return totalList;
    }

    public int getAttributeIndex(String attribute){
        for (int i = 0; i < attributes.size(); i++){
            if (attributes.get(i).equals(attribute)) return i + 1; // 1 based array
        }

        throw new RuntimeException("attribute not found in " + name);
    }

    public int getMethodIndex(String method){
        for (int i = 0; i < methodsInfo.size(); i++){
            String currentMethod = methodsInfo.get(i).second.name;
            if (currentMethod.equals(method)) return i;
        }

        throw new RuntimeException("attribute not found in " + name);
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
