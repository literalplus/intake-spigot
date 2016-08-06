package li.l1t.common.intake.exception;

/**
 * Thrown if an illegal argument is provided for a command.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class CommandArgumentException extends RuntimeException {
    public CommandArgumentException(String message) {
        super(message);
    }

    public CommandArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandArgumentException(Throwable cause) {
        super(cause);
    }
}
