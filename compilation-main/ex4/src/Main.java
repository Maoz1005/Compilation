import java.io.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import CFG.*;
import ERRORS.SemanticException;
import java_cup.runtime.Symbol;
import AST.*;
import IR.*;


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
			file_reader = new FileReader(inputFilename); /* [1] Initialize a file reader */
			file_writer = new PrintWriter(outputFilename); /* [2] Initialize a file writer */
			l = new Lexer(file_reader); /* [3] Initialize a new lexer */
			p = new Parser(l); /* [4] Initialize a new parser */

			AST = (AST_PROGRAM) p.parse().value; // Error logic is already inside of the cup file.
			AST.PrintMe(); /* [6] Print the AST ... */
			try { /* [7] Semant the AST ... */
				System.out.println("=========================\n===Initiating Semantme===\n=========================");
				AST.SemantMe();
			}
			catch (Exception e) {
//				if (e instanceof SemanticException se) { writeStatusToFile(se.getLineNumber(), "ERROR", outputFilename); }
//				else { writeStatusToFile(-1, "ERROR", outputFilename); }
				e.printStackTrace();System.exit(0);
			}
			file_writer.close(); /* [8] Close output file */
			AST_GRAPHVIZ.getInstance().finalizeFile(); /* [9] Finalize AST GRAPHIZ DOT file */

			AST.IRme();
			cfg = new CFG(IR.getInstance().getCommands());
			writeUndeclaredToFile(cfg, outputFilename);
			System.out.println("End of declaration analysis");
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
//		writeStatusToFile(-1, "SUCCESS", outputFilename);
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

	public static void writeUndeclaredToFile(CFG cfg, String path) {
		int undeclaredCount = cfg.uninitializedVars.size();
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter writer = new BufferedWriter(fw);
			if (undeclaredCount == 0) {
				writer.write("!OK");
				System.out.println("\n ALL VARIABLES DECLARED!");
			}
			else {
				System.out.println("\n UNDECLARED VARIABLES:");
				List<String> undeclaredVars = new ArrayList<>(cfg.uninitializedVars);
				Collections.sort(undeclaredVars); // Lex sort apparently
				for (int i = 0; i < undeclaredVars.size(); i++) {
					String undeclaredVar = undeclaredVars.get(i);
					System.out.println(undeclaredVar);
					writer.write(undeclaredVar);
					if (i < undeclaredVars.size() - 1) {
						writer.write("\n");
					}
				}
			}
			writer.close();
		}
		catch (IOException e) {
			System.out.println("ERROR WHEN OPENING FILE.");
			e.printStackTrace();
		}
	}
}


