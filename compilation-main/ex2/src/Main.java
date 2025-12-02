   
import java.io.*;

import java_cup.runtime.Symbol;
import AST.*;

public class Main{

	public static String outputFilename = null;

	static public void main(String argv[]) {
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		outputFilename = argv[1];
		
		try {
			/* ********************** */
			/* [0] PRINT LEXER TOKENS */
			/* ********************** */
			printLexer(inputFilename);

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
			l = new Lexer(file_reader); // Reinitialize the lexer with the new reader
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);
			
			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_PROGRAM) p.parse().value;
			writeStatusToFile("SUCCESS", outputFilename);
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();	

			/*************************/
			/* [7] Close output file */
			/*************************/
			file_writer.close();
			
			/*************************************/
			/* [8] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
		writeStatusToFile(-1, "SUCCESS", outputFilename);
	}


	public static void writeStatusToFile(String status, String PATH) {
		writeStatusToFile(-1, status, PATH);
	}


	public static void writeStatusToFile(int lineNumber, String status, String PATH) {
		try {
			FileWriter fw = new FileWriter(PATH);
			BufferedWriter writer = new BufferedWriter(fw);

			if (status.equals("ERROR")) {
				writer.write("ERROR(" + lineNumber + ")");
			}
			else if (status.equals("SUCCESS")) {
				writer.write("OK");
				System.out.println("\n SUCCESS!");
			}
			else {
				writer.close();
				throw new IOException("Incorrect status code when writing to file.");
			}
			writer.close();
		}
		catch (IOException e) {
			System.out.println("ERROR WHEN OPENING FILE.");
			e.printStackTrace();
		}
	}


	public static void logLexerSymbol(Symbol symbol) {
		if (symbol != null) {
			String tokenName = TokenNames.terminalNames[symbol.sym];
			System.out.println("Token: " + tokenName + " (line: " + symbol.left + ")");
		}
	}


	public static void printLexer(String inputFilename) {
		try {
			FileReader file_reader = new FileReader(inputFilename); // Reopen the file from the start
			Lexer l = new Lexer(file_reader);
			System.out.println("Tokens:");
			Symbol symbol;
			while ((symbol = l.next_token()).sym != 0) { // 0 represents EOF
				logLexerSymbol(symbol);
			}
			file_reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}


