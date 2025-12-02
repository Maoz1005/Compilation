package AST;

import java.util.Arrays;
import java.util.List;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	
	public AST_Node(String derivation) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.println("====================== " + derivation);
	}

	protected abstract String GetNodeName();

	// default - 0 children
	protected List<? extends AST_Node> GetChildren() { return Arrays.asList(); }

	public final void PrintMe(){
		// print me, add me as a node, do the same to my children, log the edges to them
		System.out.println("next node: \n***\n" + GetNodeName() + "\n***");

		AST_GRAPHVIZ.getInstance().logNode( SerialNumber, GetNodeName());

		for (AST_Node child : GetChildren()){
			child.PrintMe();
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, child.SerialNumber);
		}
	}
}
