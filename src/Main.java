import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main
{
	final static int MAX_NUMBER = (int)Math.pow(2,15) - 1;  // Maximal int

	public static final String[] id_to_name = { /* Array mapping token IDs to their names */
			"EOF",          // 0
			"LPAREN",       // 1
			"RPAREN",       // 2
			"LBRACK",       // 3
			"RBRACK",       // 4
			"LBRACE",       // 5
			"RBRACE",       // 6
			"PLUS",         // 7
			"MINUS",        // 8
			"TIMES",        // 9
			"DIVIDE",       // 10
			"COMMA",        // 11
			"DOT",          // 12
			"SEMICOLON",    // 13
			"TYPE_INT",     // 14
			"TYPE_STRING",  // 15
			"TYPE_VOID",    // 16
			"ASSIGN",       // 17
			"EQ",           // 18
			"LT",           // 19
			"GT",           // 20
			"ARRAY",        // 21
			"CLASS",        // 22
			"RETURN",       // 23
			"WHILE",        // 24
			"IF",           // 25
			"ELSE",         // 26
			"NEW",          // 27
			"EXTENDS",      // 28
			"NIL",          // 29
			"INT",       	// 30
			"STRING",       // 31
			"ID",           // 32
			"ERROR"			// 33
	};

	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader fileReader;
		PrintWriter fileWriter;
		String inputFileName = argv[0];
		String outputFileName = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			fileReader = new FileReader(inputFileName);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			fileWriter = new PrintWriter(outputFileName);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(fileReader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF)
			{
				/************************/
				/* [6] Print to console */
				/************************/
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]:");
				System.out.print(s.value);
				System.out.print("\n");
				
				/*********************/
				/* [7] Print to file */
				/*********************/
				fileWriter.print(l.getLine());
				fileWriter.print(": ");
				fileWriter.print(s.value);
				fileWriter.print("\n");
				
				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			fileWriter.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


