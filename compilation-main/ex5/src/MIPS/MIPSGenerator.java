/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.InitialConstVal;
import TEMP.*;

public class MIPSGenerator
{
	private static int WORD_SIZE=4;
	private PrintWriter fileWriter; /* The file writer ... */
	private static final String STRCPY_LABEL = "strcpy";
	private static final String STRLEN_LABEL = "strlen";
	public static final String EOF = "end_of_file";

	private void printf(String format, Object... args){
		fileWriter.printf("\t" + format +"\n", args);
	}

	public void data(){
		fileWriter.printf(".data\n");
	}

	public void text(){
		fileWriter.printf(".text\n");
	}
	/* The file writer ... */
	public void finalizeFile() {
		label(EOF);
		printf("li $v0,10");
		printf("syscall");
		fileWriter.close();
	}

	public void builtInStrs(){
		instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
		instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
		instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
	}

	public void builtInFuncs(){
		defineStrlen();
		defineStrcpy();
	}

	// thats it im tired
	public void wildcard(String command){
		printf(command);
	}

	// writes a definition for the function that gets string length
	// uses a0 as input, and s0 as a temp, v0 as output
	public void defineStrlen(){
		label(STRLEN_LABEL);
		printf("li $v0, 0");

		label("strlen_loop");
		printf("lb $s0, 0($a0)");
		printf("beq $s0, $zero, strlen_end");
		printf("addi $v0, $v0, 1");
		printf("addi $a0, $a0, 1");
		printf("j strlen_loop");

		label("strlen_end");
		printf("jr $ra");
	}

	// writes a definition for the function that copies the str in a1 into the address at a0
	// uses a0 and a1 as input, s0 as temp
	public void defineStrcpy(){
		label(STRCPY_LABEL);
		label("strcpy_loop");
		printf("lb $s0, 0($a1)");
		printf("sb $s0, 0($a0)");
		printf("beq $s0, $zero, strcpy_done");
		printf("addi $a0, $a0, 1");
		printf("addi $a1, $a1, 1");
		printf("j strcpy_loop");

		label("strcpy_done");
		printf("jr $ra");
	}

	// appends string in t1 to string in t2, allocates memory and saves in dst
	// DOES NOT PRESERVE s0, s2, s3, a0, a1, v0
	// also does not preserve ra but should be fine since it is saved in stack for every non-builtin function call anyway
	public void appendStrs(TEMP dst, TEMP t1, TEMP t2){
		// call strlen on t1, save in s2
		printf("move $a0, $t%d", t1.getSerialNumber());
		printf("jal %s", STRLEN_LABEL);
		printf("move $s2, $v0");

		// call strlen on t2, save in s3
		printf("move $a0, $t%d", t2.getSerialNumber());
		printf("jal %s", STRLEN_LABEL);
		printf("move $s3, $v0");

		// save t2 in $s4 to avoid it being overwritten
		printf("move $s4, $t%d", t2.getSerialNumber());

		// calculate total length, save in a0, allocate memory
		printf("add $a0, $s2, $s3");
		printf("addi $a0, $a0, 1"); // +1 for null terminator
		printf("li $v0, 9");
		printf("syscall");

		// set dst address
		printf("move $t%d, $v0", dst.getSerialNumber());

		// copy t1 into dst
		printf("move $a0, $t%d", dst.getSerialNumber()); // dest
		printf("move $a1, $t%d", t1.getSerialNumber());  // src = t1
		printf("jal %s", STRCPY_LABEL);

		// copy t2 into dst + len(t1)
		printf("add $a0, $t%d, $s2", dst.getSerialNumber()); // dest + len(t1)
		printf("move $a1, $s4"); // src = t2
		printf("jal %s", STRCPY_LABEL);
	}

	// errLbl = the label starting the error section
	// errMsgDataLbl = the .data string to print on error
	public void errorSection(String errLbl, String errMsgDataLbl){
		label(errLbl);
		printDataStr(errMsgDataLbl);
		printf("li $v0,10");
		printf("syscall");
	}

	// does not preserve a0, v0
	public void printInt(TEMP t) {
		int idx=t.getSerialNumber();
		// printf("addi $a0,$t%d,0",idx);
		printf("move $a0,$t%d",idx);
		printf("li $v0,1");
		printf("syscall");

		// Prints a " " character
		printf("li $a0,32");
		printf("li $v0,11");
		printf("syscall");
	}

	// does not preserve a0, v0
	public void printDataStr(String dataLabel){
		printf("la $a0,%s",dataLabel);
		printf("li $v0,4");
		printf("syscall");
	}

	// does not preserve a0, v0
	public void printStr(TEMP addr){
		printf("move $a0, $t%d", addr.getSerialNumber());
		printf("li $v0, 4");
		printf("syscall");
	}

	// pushes src to stack
	public void stackPush(TEMP src){
		int srcidx = src.getSerialNumber();

		stackAlloc(1);
		printf("sw $t%d,0($sp)", srcidx);
	}

	// sets dst to the address of parameter #offset
	public void getParameterAddress(TEMP dst, int offset){
		printf("li $t%d, %d", dst.getSerialNumber(), (offset + 1) * WORD_SIZE);
		printf("add $t%d, $t%d, $fp", dst.getSerialNumber(), dst.getSerialNumber());
	}

	public void getLocalAddress(TEMP dst, int offset){
		printf("li $t%d, %d", dst.getSerialNumber(), -offset * WORD_SIZE);
		printf("add $t%d, $t%d, $fp", dst.getSerialNumber(), dst.getSerialNumber());
	}

	// pushes stack amount*WORD_SIZE back
	public void stackAlloc(int amount){
		printf("subu $sp,$sp,%d", amount * WORD_SIZE);
	}

	// pushes stack amount*WORD_SIZE forward
	public void stackDealloc(int amount){
		printf("addu $sp,$sp,%d", amount * WORD_SIZE);
	}

	// $s1 holds the pointer to the object who called the current method
	// pushed previous $s1 val to stack and sets $s1 to ref
	public void setMethodObjectReference(TEMP ref){
		stackAlloc(1);
		printf("sw $s1, 0($sp)");
		printf("lw $s1, 0($t%d)", ref.getSerialNumber());
	}

	// just pops stack into $s1
	public void restoreMethodObjectReference(){
		printf("lw $s1, 0($sp)");
		stackDealloc(1);
	}

	public void getAttributeAddress(TEMP dst, int offset){
		printf("add $t%d, $s1, %d", dst.getSerialNumber(), offset * WORD_SIZE);
	}

	public void backupTemps(){
		stackAlloc(10);
		for (int i = 0; i <= 9; i++){
			printf("sw $t%d,%d($sp)", i, i * WORD_SIZE);
		}
	}

	// pushes ra and fp to the stack (start of function)
	public void pushFrame(){
		printf("subu $sp,$sp,4");
		printf("sw $fp,0($sp)");
		printf("subu $sp,$sp,4");
		printf("sw $ra,0($sp)");
		printf("move $fp, $sp");
	}

	// changes fp and sp to reflect the previous frame, jumps back to ra
	public void popFrameAndReturn(){
		printf("move $sp,$fp");
		printf("lw $ra,0($sp)");
		printf("lw $fp,4($sp)");
		printf("addu $sp,$sp,8");
		printf("jr $ra");
	}

	// creates an array and saves the pointer in dst
	// the size of the array is saved in offset 0, so all addresses are +1
	public void allocateArray(TEMP dst, TEMP size){
		printf("li $v0, 9");
		printf("move $s0, $t%d", size.getSerialNumber());
		printf("move $a0, $s0");
		printf("add $a0, $a0, 1");
		printf("sll $a0, $a0, 2");
		printf("syscall");
		printf("sw $s0, 0($v0)");
		printf("move $t%d, $v0", dst.getSerialNumber());
	}

	public void initializeObject(TEMP dst, String virtualPointer, List<InitialConstVal> initialValues){
		// allocate memory
		printf("li $v0, 9");
		printf("li $a0, %d", initialValues.size() + 1); // 0 is the virtual table pointer
		printf("mul $a0, $a0, 4");
		printf("syscall");

		// set vt pointer
		printf("move $t%d, $v0", dst.getSerialNumber());
		printf("la $s0, %s", virtualPointer);
		printf("sw $s0, 0($t%d)", dst.getSerialNumber());

		// set initial values
		for (int i = 0; i < initialValues.size(); i++){
			InitialConstVal val = initialValues.get(i);

			if (val.isString())
				printf("la $s0, str_%s", val.getValue());
			else
				printf("li $s0, %s", val.getValue());

			printf("sw $s0, %d($t%d)", (i+1) * 4, dst.getSerialNumber());
		}
	}

	public void restoreTemps(){
		for (int i = 0; i <= 9; i++){
			printf("lw $t%d,%d($sp)", i, i * WORD_SIZE);
		}
		stackDealloc(10);
	}

	// moves val from src to v0
	public void setReturnVal(TEMP src){
		printf("move $v0,$t%d", src.getSerialNumber());
	}

	// moves return val from v0 to dst
	public void getReturnVal(TEMP dst){
		printf("move $t%d,$v0",dst.getSerialNumber());
	}

	public void allocateGlobal(String var_name, String initVal, boolean isStringVal) {
		if (isStringVal) initVal = "str_" + initVal;
		printf("global_%s: .word %s", var_name, initVal);
	}

	public void load(TEMP dst,String var_name) {
		int idxdst=dst.getSerialNumber();
		printf("lw $t%d,global_%s",idxdst,var_name);
	}

	public void storeGlobal(String var_name, TEMP src) {
		int idxsrc=src.getSerialNumber();
		printf("sw $t%d,global_%s",idxsrc,var_name);
	}

	public void sw(TEMP src, TEMP dst, int offset){
		printf("sw $t%d, %d($t%d)", src.getSerialNumber(), offset * WORD_SIZE, dst.getSerialNumber());
	}

	// loads address src+tempOffset+immediateOffset, sets dst to it
	public void loadAt(TEMP dst, TEMP src, TEMP tempOffset, int immediateOffset){
		if (tempOffset == null){
			printf("lw $t%d, %d($t%d)", dst.getSerialNumber(), immediateOffset * WORD_SIZE, src.getSerialNumber());
			return;
		}

		printf("move $s0, $t%d", tempOffset.getSerialNumber());

		if (immediateOffset != 0) printf("addi $s0, $s0, %d", immediateOffset);

		printf("mul $s0, $s0, 4");
		printf("add $s0, $s0, $t%d", src.getSerialNumber());
		printf("lw $t%d, 0($s0)", dst.getSerialNumber());
	}

	public void loadAt(TEMP dst, TEMP src, TEMP offset){
		loadAt(dst, src, offset, 0);
	}

	public void loadAt(TEMP dst, TEMP src, int offset){
		loadAt(dst, src, null, offset);
	}

	public void li(TEMP t,int value) {
		int idx=t.getSerialNumber();
		printf("li $t%d,%d",idx,value);
	}

	public void sub(TEMP dst, TEMP op1, TEMP op2){
		int i1 = op1.getSerialNumber();
		int i2 = op2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		printf("sub $t%d,$t%d,$t%d",dstidx,i1,i2);
	}

	public void addi(TEMP dst, TEMP op, int immediate){
		printf("addi $t%d, $t%d, %d", dst.getSerialNumber(), op.getSerialNumber(), immediate);
	}

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2) {
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		printf("add $t%d,$t%d,$t%d",dstidx,i1,i2);
	}

	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2) {
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		printf("mul $t%d,$t%d,$t%d",dstidx,i1,i2);
	}

	public void sll(TEMP dst, TEMP src, int shift){
		printf("sll $t%d, $t%d, %d", dst.getSerialNumber(), src.getSerialNumber(), shift);
	}


	public void getGlobalAddress(TEMP dst, String varName){
		printf("la $t%d, global_%s", dst.getSerialNumber(), varName);
	}

	public void label(String label) {
		if (label.equals("main")) fileWriter.format(".globl main\n");
		fileWriter.format("%s:\n",label);
	}

	// this sucks but i had to do it like this for reasons
	// DOES NOT PRESERVE s2, s3, s4, s5
	public void compareStr(TEMP dst, TEMP t1, TEMP t2, String lblCompare, String lblNotEq, String lblEnd){
		printf("li $t%d, 1", dst.getSerialNumber()); // assume equal

		printf("move $s2, $t%d", t1.getSerialNumber());
		printf("move $s3, $t%d", t2.getSerialNumber());

		// comparison loop
		label(lblCompare);
		printf("lb $s4, 0($s2)");
		printf("lb $s5, 0($s3)");
		printf("bne $s4, $s5, %s", lblNotEq); // break loop if unequal
		printf("beq $s4, $zero, %s", lblEnd); // break loop if reached null terminator
		printf("addi $s2, $s2, 1"); // advance both pointers by a single byte
		printf("addi $s3, $s3, 1");
		printf("j %s", lblCompare); // loop

		label(lblNotEq); // set to 0 if unequal
		printf("li $t%d, 0", dst.getSerialNumber());

		label(lblEnd);
	}

	public void asciiz(String strConst){
		fileWriter.format("str_%s: .asciiz \"%s\"\n", strConst, strConst);
	}

	public void loadStringConst(TEMP dst, String strConst){
		printf("la $t%d, str_%s", dst.getSerialNumber(), strConst);
	}

	public void word(String pointer){
		printf(".word %s", pointer);
	}

	public void word(){
		printf(".word");
	}

	public void jump(String inlabel) {
		printf("j %s",inlabel);
	}

	public void blt(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		printf("blt $t%d,$t%d,%s",i1,i2,label);
	}

	public void bltz(TEMP t1, String label){
		printf("bltz $t%d, %s", t1.getSerialNumber(), label);
	}

	public void bge(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		printf("bge $t%d,$t%d,%s",i1,i2,label);
	}

	public void bne(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		printf("bne $t%d,$t%d,%s",i1,i2,label);
	}

	public void beq(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		printf("beq $t%d,$t%d,%s",i1,i2,label);
	}

	public void beqz(TEMP oprnd1,String label) {
		int i1 =oprnd1.getSerialNumber();

		printf("beq $t%d,$zero,%s",i1,label);
	}

	public void jal(String funcname){
		printf("jal %s", funcname);
	}

	public void jalr(TEMP address){
		printf("jalr $t%d", address.getSerialNumber());
	}

	public void move(TEMP dst, TEMP src){
		int i1 = dst.getSerialNumber();
		int i2 = src.getSerialNumber();

		printf("move $t%d,$t%d", i1, i2);
	}

	public void divTo(TEMP dst, TEMP t1, TEMP t2){
		int i1 = t1.getSerialNumber();
		int i2 = t2.getSerialNumber();
		int dstidx = dst.getSerialNumber();

		printf("div $t%d,$t%d", i1, i2);
		printf("mflo $t%d", dstidx);
	}

	private static MIPSGenerator instance = null; /* USUAL SINGLETON IMPLEMENTATION ... */

	/* PREVENT INSTANTIATION ... */
	protected MIPSGenerator() {}

	public void setOutPath(String path){
		try {
			instance.fileWriter = new PrintWriter(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/* GET SINGLETON INSTANCE ... */
	public static MIPSGenerator getInstance() {
		if (instance == null) {
			instance = new MIPSGenerator(); /*[0] The instance itself ... */
		}
		return instance;
	}
}
