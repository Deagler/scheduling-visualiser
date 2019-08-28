package internseason.scheduler.input;

/** Exception thrown when user inputs illegal arguments into terminal
 */
public class CLIException extends Exception {
    public CLIException(String message) {
        super(message);
    }
}
