package IR;

import java.util.ArrayList;
import java.util.List;

/**
 * Some clarifications (Please validate me):
 * 1. IRCommand_Allocate - Responsible for a new variable declaration: int x = 3
 * 2. IRCommand_Load - Assigns a variable's value to a TEMP: t1 = x
 * 3. IRCommand_Store - Puts a value from TEMP into a variable: x = t1
 */

/**
 * Class (list thanks neri) to store all of our IR commands.
 */
public class IR {

	private List<IRcommand> commands;

	public List<IRcommand> getCommands() {
		return commands;
	}

	public void add(IRcommand cmd) {
		commands.add(cmd); // GEE WHIZZ NOW WASN'T THAT HARD
	}

	/*** There be singletons... ***/
	private static IR instance = null;

	protected IR() {
		commands = new ArrayList<>();
	}

	public static IR getInstance() {
		if (instance == null) instance = new IR();
		return instance;
	}

	public void MIPSme(){
		for (IRcommand command : commands) { command.MIPSme(); }
	}
}
