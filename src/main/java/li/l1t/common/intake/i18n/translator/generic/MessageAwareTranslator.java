package li.l1t.common.intake.i18n.translator.generic;

/**
 * Translates exceptions, using its localized message as parameter. If the message is null, the
 * cause's class name and message are used, and if that is null, the exception class name is used.
 * <p>
 * If cause and message are both null, the exception is also logged to allow further
 * diagnostics. Without any message, it would be impossible to say what kind of error the
 * user encountered.
 * </p>
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-06
 */
public class MessageAwareTranslator extends StaticTranslator<Exception> {
    public MessageAwareTranslator(String messageKey, Class<Exception> exceptionType) {
        super(exceptionType, messageKey, false);
        initArgumentProvider();
    }

    private void initArgumentProvider() {
        withArgument(e -> {
            if (e.getLocalizedMessage() != null) {
                return e.getLocalizedMessage();
            } else if (e.getCause() != null) {
                return e.getCause().getClass().getName() + ": " + e.getCause().getLocalizedMessage();
            } else {
                return e.getClass().getName();
            }
        });
    }

    @Override
    public boolean needsLogging(Exception exception) {
        return exception.getLocalizedMessage() == null && exception.getCause() == null;
    }
}
