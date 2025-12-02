package CFG;

import java.util.*;
import IR.*;
import TEMP.*;


public class CFGBlock {

    private final List<IRcommand> body;
    private final List<CFGBlock> parents;
    private final List<CFGBlock> children;
    private Set<Integer> out;


    public CFGBlock(List<IRcommand> commands) {
        if (commands == null || commands.isEmpty()) { // Are empty blocks fine? o_O
            System.err.println("Cannot make a CFGBlock with no instructions.");
            throw new IllegalArgumentException();
        }
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.body = List.copyOf(commands);
        this.out = new HashSet<>();
    }


    // This constructor is used specifically for the demi-block
    public CFGBlock(){
        this.body = null;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        this.out = new HashSet<>();
    }


    public void addChild(CFGBlock block) {
        if (block == null) { throw new IllegalArgumentException(); }
        if (children.size() >= 2) {
            System.err.println("Too many children");
            throw new IllegalArgumentException();
        }
        children.add(block);
    }


    public void addParent(CFGBlock block) {
        if (block == null) { throw new IllegalArgumentException(); }
        parents.add(block);
    }


    /**
     * Calculates the out of the current node in chaotic iterations.
     * Notice! Changes out in-place! (hee hee out in-place)
     * @return true if the update changed the previously calculated out.
     */
    public boolean updateOut(){

        // Compute current in set - union all out sets of the children (backward analysis)
        Set<Integer> inSet = new HashSet<>();
        for (CFGBlock child : this.children) {
            inSet.addAll(child.getOut());
        }

        // Update our out set according to the in set (change inSet in place - this is the updated out set)
        if (this.body != null) {
            IRcommand command = this.body.get(0);
            command.calcOut(inSet); // Update the out set of the command (changes inSet in place)
        }
        
        // Check if the out set after update (it is inSet - was changed in place - yeah the name is confusing)
        // has changed from the previous out set (this.out)
        System.out.println("old out set: " + this.out);
        System.out.println("new out set: " + inSet);
        boolean changed = !(inSet.equals(this.out));
        if (changed) this.out = inSet;
    
        return changed;
    }

    /* 
        WHAT IR_COMMANDS SHOULD BE TAKEN INTO ACCOUNT WHEN UPDATING THE OUT SET?
        2. Array_Create - IGNORE?
        3. Array_Set - IGNORE?
        4. Binop - DONE
        5. BranchGE - DONE
        6. BranchLT - DONE
        7. Jump_If_Eq_To_Zero - DONE
        8. CallFunc - DONE
        9. CallMethod - DONE, BUT CHECK AGAIN!
        10. ConstInt - DONE
        11. ConstString - DONE
        12. Load - DONE
        13. LoadWithOffset - DONE
        14. Move - DONE
        15. NewArrayObject - DONE
        16. NewClassObject - DONE
        17. PrintInt - DONE
        18. Return - DONE
        19. Store - DONE
        20. StoreAt - DONE
    */

    public List<IRcommand> getBody() {
        return body;
    }


    public Set<Integer> getOut() {
        return out;
    }

    
    public List<CFGBlock> getChildren() {
        return children;
    }


    public List<CFGBlock> getParents() {
        return parents;
    }
}
