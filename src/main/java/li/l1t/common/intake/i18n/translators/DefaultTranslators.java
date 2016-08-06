package li.l1t.common.intake.i18n.translators;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.parametric.ProvisionException;
import com.sk89q.intake.util.auth.AuthorizationException;
import li.l1t.common.intake.exception.CommandArgumentException;
import li.l1t.common.intake.exception.CommandExitMessage;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.generic.CauseRewritingTranslator;
import li.l1t.common.intake.i18n.translator.generic.MessageAwareTranslator;
import li.l1t.common.intake.i18n.translator.generic.PatternBasedMessageTranslator;
import li.l1t.common.intake.i18n.translator.generic.StaticTranslator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Handles registration of default exception translators. That means all translators in this
 * class and all classes in this package.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-25
 */
public class DefaultTranslators {
    private DefaultTranslators() {

    }

    public static void registerAllWith(ErrorTranslator root) {
        registerXYCIntakeExceptionsWith(root);
        registerIntakeExceptionsWith(root);
        registerSQLExceptionWith(root);
        registerIOExceptionWith(root);
        registerIllegalArgumentWith(root);
        registerNullPointerWith(root);
    }

    private static void registerXYCIntakeExceptionsWith(ErrorTranslator root) {
        new MessageAwareTranslator<>("ExitMessage", CommandExitMessage.class).registerWith(root);
        new MessageAwareTranslator<>("CommandArgumentException", CommandArgumentException.class)
                .registerWith(root);
    }

    private static void registerIntakeExceptionsWith(ErrorTranslator root) {
        registerAuthorizationExceptionWith(root);
        registerInvocationCommandExceptionWith(root);
        registerCommandExceptionsWith(root);
    }

    private static void registerAuthorizationExceptionWith(ErrorTranslator root) {
        new StaticTranslator<>(AuthorizationException.class, "PermissionDenied", false)
                .registerWith(root);
    }

    private static void registerInvocationCommandExceptionWith(ErrorTranslator root) {
        new CauseRewritingTranslator<>(InvocationCommandException.class).registerWith(root);
        new StaticTranslator<>(ProvisionException.class, "Provision", true)
                .registerWith(root);
    }

    private static void registerCommandExceptionsWith(ErrorTranslator root) {
        registerCommandExceptionFallbackTranslatorWith(root);
        InvalidUsageTranslators.registerInvalidUsagesWith(root);

    }

    private static void registerCommandExceptionFallbackTranslatorWith(ErrorTranslator root) {
        new PatternBasedMessageTranslator<>(CommandException.class, false)
                .withBaseKey("CommandException")
                .withPattern(Pattern.compile("Value flag '(.+)' already given"), ":valueFlagDupe")
                .withPattern(Pattern.compile("No value specified for the '-(.)' flag\\."), ":flagMissingValue")
                .registerWith(root);
    }

    private static void registerSQLExceptionWith(ErrorTranslator root) {
        new StaticTranslator<>(SQLException.class, "SQLException", true)
                .registerWith(root);
    }

    private static void registerIOExceptionWith(ErrorTranslator root) {
        new StaticTranslator<>(IOException.class, "IOException", true)
                .registerWith(root);
    }

    private static void registerIllegalArgumentWith(ErrorTranslator root) {
        new MessageAwareTranslator<>("IllegalArgument", IllegalArgumentException.class).registerWith(root);
    }

    private static void registerNullPointerWith(ErrorTranslator root) {
        new MessageAwareTranslator<>("NullPointer", NullPointerException.class).registerWith(root);
    }
}
