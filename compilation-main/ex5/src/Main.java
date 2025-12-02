import java.io.*;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.FileNotFoundException;

import MIPS.MIPSGenerator;
import java_cup.runtime.Symbol;
import AST.*;
import IR.*;
import CFG.*;


public class Main {

	public static String outputFilename = null;

	static public void main(String argv[]) {
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
		CFG cfg;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		outputFilename = argv[1];

		try {
			// Read file
			file_reader = new FileReader(inputFilename);
			file_writer = new PrintWriter(outputFilename);
			l = new Lexer(file_reader);
			System.out.println("\nLexical analysis successful.\n");
			p = new Parser(l);
			System.out.println("\nSyntactic analysis successful.\n");


//			try {
//				System.setOut(new PrintStream("output/Logger.txt"));
//			}
//			catch (FileNotFoundException e) {}
			// Semantic phase
			AST = (AST_PROGRAM) p.parse().value;
			AST.PrintMe();
			try {
				System.out.println("=========================\n===Initiating Semantme===\n=========================");
				AST.SemantMe();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

			AST.IRme();
			System.out.println("Successful IR translation.");
			System.out.println("\n=========================\n===== Printing IRme =====\n=========================");
			for (IRcommand command : IR.getInstance().getCommands()) {
				System.out.println(command);
			}


			System.out.println("\n=========================\n===== Simplifying IR =====\n=========================");
			try {
				cfg = new CFG(IR.getInstance().getCommands());
			} catch (Exception e) {
				writeFailureToFile("Register Allocation Failed", outputFilename);
				System.out.println("Register Allocation Failed");
				System.exit(0);
			}

			System.out.println("Finished simplifying IR + register allocation");
			// end of temp simplification

			System.out.println("\n=========================\n===== MIPSing to file =====\n=========================");
			MIPSGenerator.getInstance().setOutPath(outputFilename);
			IR.getInstance().MIPSme();
			MIPSGenerator.getInstance().finalizeFile();
			System.out.println("Successful MIPS translation.");

			AST_GRAPHVIZ.getInstance().finalizeFile();

			file_writer.close();
			System.out.println("SUCCESS!");
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void writeFailureToFile(String message, String PATH) {
		try {
			FileWriter fw = new FileWriter(PATH);
			BufferedWriter writer = new BufferedWriter(fw);
			
			writer.write(message);
			writer.close();
		}
		catch (IOException e) {
			System.out.println("ERROR WHEN OPENING FILE.");
			e.printStackTrace();
		}
	}
}


