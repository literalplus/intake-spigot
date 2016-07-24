package li.l1t.common.intake.i18n.translator.impl;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.util.auth.AuthorizationException;
import li.l1t.common.intake.exception.CommandExitMessage;
import li.l1t.common.intake.i18n.ErrorTranslator;

/**
 * Handles registration of shipped exception translators.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-25
 */
public class DefaultTranslators {
    private DefaultTranslators() {

    }

    public static void registerAllWith(ErrorTranslator root) {
        registerCommandExitMessageWith(root);
        registerAuthorizationExceptionWith(root);
        registerInvocationCommandExceptionWith(root);
        //FIXME: Translate internal Intake exceptions ("too many arguments", ...) vvv
        new StaticTranslator<>(CommandException.class, "Command Exception", false)
                .withArgument(CommandException::getMessage)
                .registerWith(root);
    }

    public static void registerInvocationCommandExceptionWith(ErrorTranslator root) {
        new CauseRewritingTranslator<>(InvocationCommandException.class).registerWith(root);
    }

    public static void registerAuthorizationExceptionWith(ErrorTranslator root) {
        new StaticTranslator<>(AuthorizationException.class, "Permission Denied", false)
                .registerWith(root);
    }

    public static void registerCommandExitMessageWith(ErrorTranslator root) {
        new StaticTranslator<>(CommandExitMessage.class, "Exit Message", false)
                .withArgument(CommandExitMessage::getLocalizedMessage)
                .registerWith(root);
    }
}
