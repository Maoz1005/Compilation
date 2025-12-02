package ERRORS;

public class IRException extends CompilationException {
    public IRException(String message) {
        super(message, -1); // TODO: line number during transition to IR? o_O
    }
}
