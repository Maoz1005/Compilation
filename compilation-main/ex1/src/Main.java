   
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main {

	final static int MAX_NUMBER = (int)Math.pow(2,15) - 1;

	public static final String[] id_to_name = { /* Array mapping token IDs to their names */
		"EOF",          // 0
		"LPAREN",       // 1
		"RPAREN",       // 2
		"LBRACK",       // 3
		"RBRACK",       // 4
		"LBRACE",       // 5
		"RBRACE",       // 6
		"NIL",          // 7
		"PLUS",         // 8
		"MINUS",        // 9
		"TIMES",        // 10
		"DIVIDE",       // 11
		"COMMA",        // 12
		"DOT",          // 13
		"SEMICOLON",    // 14
		"TYPE_INT",     // 15
		"TYPE_VOID",    // 16
		"ASSIGN",       // 17
		"EQ",           // 18
		"LT",           // 19
		"GT",           // 20
		"ARRAY",        // 21
		"CLASS",        // 22
		"EXTENDS",      // 23
		"RETURN",       // 24
		"WHILE",        // 25
		"IF",           // 26
		"NEW",          // 27
		"INT",       	// 28
		"STRING",       // 29
		"ID",           // 30
		"TYPE_STRING",  // 31
		"ERROR"			// 32
	};


	static private void overwriteWithError(String filename) throws IOException { 
    System.out.println("Error encountered, crashing and burning");
    PrintWriter fw = new PrintWriter(filename);
    fw.print("ERROR");
    fw.close();
	}


	static private boolean numIsValid(Object numberAsObject) {
    int number;
    try{
      String numberAsString = numberAsObject.toString();
      if (numberAsString.length() >= 10) { // Larger than 1 billion
        return false;
      }
      number = Integer.parseInt(numberAsString);
      return (0 <= number) && (number <= MAX_NUMBER); 
    }
    catch (Exception e){ // length check to prevent overflow
      return false;
    }
	}


	static public void main(String argv[]) throws IOException
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
    boolean fail = false;

    /********************************/
    /* [1] Initialize a file reader */
    /********************************/
    file_reader = new FileReader(inputFilename);

    /********************************/
    /* [2] Initialize a file writer */
    /********************************/
    file_writer = new PrintWriter(outputFilename);

    /******************************/
    /* [3] Initialize a new lexer */
    /******************************/
    l = new Lexer(file_reader);

    /***********************/
    /* [4] Read next token */
    /***********************/
    try{
      s = l.next_token();
    }
    catch (Error e){
      l.yyclose();
      file_writer.close();
      overwriteWithError(outputFilename);
      return;
    }

    /********************************/
    /* [5] Main reading tokens loop */
    /********************************/
    while (s.sym != TokenNames.EOF) 
    {
      /*************/
      /* [6] Print */ 
      /*************/
      String tokenType = id_to_name[s.sym];
      System.out.println(tokenType);
      if (tokenType.equals("ERROR")){
        fail = true;
        break;
      }
      file_writer.print(tokenType);

      boolean isNumber = tokenType.equals("INT");
      boolean isString = tokenType.equals("STRING");
      boolean isID = tokenType.equals("ID");
      if (isNumber || isString || isID) {
        if (isNumber && !numIsValid(s.value)) {
          fail = true;
          break;
        }
        file_writer.print("(");
        file_writer.print(s.value);
        file_writer.print(")");
      }
      file_writer.print("[");
      file_writer.print(l.getLine());
      file_writer.print(",");
      file_writer.print(l.getTokenStartPosition());
      file_writer.print("]");
      file_writer.print("\n");

      /***********************/
      /* [7] Read next token */
      /***********************/
      try {
        s = l.next_token();
      } catch (Error | NumberFormatException e){
        fail = true;
        break;
      }
    }

    /******************************/
    /* [8] Close lexer input file */
    /******************************/
    l.yyclose();

    /**************************/
    /* [9] Close output file */
    /**************************/
    file_writer.close();


    /******************************/
    /* [10] Overwriting if needed */
    /******************************/
    if (fail) overwriteWithError(outputFilename);
    else System.out.println("Lexing successful");
  }
}


