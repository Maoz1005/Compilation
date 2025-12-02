package ERRORS;

public class SemanticException extends CompilationException {

    public SemanticException(String message, int lineNumber) {
        super(message, lineNumber);
    }

}
