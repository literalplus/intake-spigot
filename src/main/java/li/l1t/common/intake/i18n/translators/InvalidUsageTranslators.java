package li.l1t.common.intake.i18n.translators;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.MissingArgumentException;
import com.sk89q.intake.argument.UnusedArgumentException;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.generic.NullableParameterTranslator;
import li.l1t.common.intake.i18n.translator.generic.StaticTranslator;
import li.l1t.common.intake.i18n.translator.specific.InvalidUsageTranslator;

/**
 * Registers default translators for InvalidUsageException.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-30
 */
class InvalidUsageTranslators {

    static void registerInvalidUsagesWith(ErrorTranslator root) {
        registerRootInvalidUsageWith(root);
        registerMissingArgumentWith(root);
        registerUnusedArgumentWith(root);
        ArgumentParseTranslators.registerArgumentParseWith(root);
        registerArgumentExceptionWith(root);
    }

    private static void registerRootInvalidUsageWith(ErrorTranslator root) {
        new InvalidUsageTranslator().registerWith(root);
    }

    private static void registerMissingArgumentWith(ErrorTranslator root) {
        new NullableParameterTranslator<>(MissingArgumentException.class)
                .withBaseKey("MissingArgument")
                .withParameterMapper(MissingArgumentException::getParameter)
                .registerWith(root);
    }

    private static void registerUnusedArgumentWith(ErrorTranslator root) {
        new StaticTranslator<>(UnusedArgumentException.class, "UnusedArgument", false)
                .withArgument(UnusedArgumentException::getUnconsumed)
                .registerWith(root);
    }

    private static void registerArgumentExceptionWith(ErrorTranslator root) {
        new StaticTranslator<>(ArgumentException.class, "ArgumentException", false)
                .withArgument(ArgumentException::getMessage)
                .registerWith(root);
    }
}
