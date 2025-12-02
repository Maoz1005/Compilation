import java.io.*;
import java.io.PrintWriter;

import ERRORS.SemanticException;
import java_cup.runtime.Symbol;
import AST.*;


public class Main {

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
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_PROGRAM) p.parse().value; // Error logic is already inside of the cup file.
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			try {
				System.out.println("=========================\n===Initiating Semantme===\n=========================");
				AST.SemantMe();
			}
			catch (Exception e) {
				if (e instanceof SemanticException) {
					SemanticException se = (SemanticException) e;
					writeStatusToFile(se.getLineNumber(), "ERROR", outputFilename);
				}
				else { writeStatusToFile(-1, "ERROR", outputFilename); }
				e.printStackTrace();
				System.exit(0);
			}
			
			/*************************/
			/* [8] Close output file */
			/*************************/
			file_writer.close();

			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
		writeStatusToFile(-1, "SUCCESS", outputFilename);
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
}


