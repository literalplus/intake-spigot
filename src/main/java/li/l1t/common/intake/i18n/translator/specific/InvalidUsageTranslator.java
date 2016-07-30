package li.l1t.common.intake.i18n.translator.specific;

import com.sk89q.intake.InvalidUsageException;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.AbstractExceptionTranslator;
import li.l1t.common.intake.i18n.translator.generic.CauseRewritingTranslator;
import li.l1t.common.intake.i18n.translator.generic.PatternBasedMessageTranslator;

/**
 * Handles the special case of InvalidUsageException. If the exception has a cause, it is
 * rewritten to that, mimicking {@link CauseRewritingTranslator} behaviour. If it doesn't, it is
 * mapped to its message like in {@link PatternBasedMessageTranslator}.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-30
 */
public class InvalidUsageTranslator extends AbstractExceptionTranslator<InvalidUsageException> {
    private final PatternBasedMessageTranslator<InvalidUsageException> messageTranslator =
            new PatternBasedMessageTranslator<>(InvalidUsageException.class, false)
            .withBaseKey("InvalidUsage");
    private final CauseRewritingTranslator<InvalidUsageException> causeRewritingTranslator =
            new CauseRewritingTranslator<>(InvalidUsageException.class);

    public InvalidUsageTranslator() {
        super(InvalidUsageException.class, false);
    }

    @Override
    public String translate(InvalidUsageException exception, ErrorTranslator root, String commandLine) {
        if(exception.getCause() != null) {
            return rewriteToCause(exception, root, commandLine);
        } else {
            return mapToMessage(exception, root, commandLine);
        }
    }

    private String mapToMessage(InvalidUsageException exception, ErrorTranslator root, String
            commandLine) {
        return messageTranslator.translate(exception, root, commandLine);
    }

    private String rewriteToCause(InvalidUsageException exception, ErrorTranslator root, String
            commandLine) {
        return causeRewritingTranslator.translate(exception, root, commandLine);
    }
}
