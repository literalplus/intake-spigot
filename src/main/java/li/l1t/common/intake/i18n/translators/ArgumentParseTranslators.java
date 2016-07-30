package li.l1t.common.intake.i18n.translators;

import com.sk89q.intake.argument.ArgumentParseException;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.generic.NullableParameterTranslator;
import li.l1t.common.intake.i18n.translator.generic.PatternBasedMessageTranslator;

import java.util.regex.Pattern;

/**
 * Handles registration of translators for ArgumentParseExceptions.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-30
 */
class ArgumentParseTranslators {
    private static final String BASE_KEY = "ArgumentParse";

    static void registerArgumentParseWith(ErrorTranslator root) {
        new NullableParameterTranslator<>(ArgumentParseException.class)
                .withBaseKey(BASE_KEY)
                .withMessageTranslator(createMessageTranslator())
                .withParameterMapper(ArgumentParseException::getParameter)
                .registerWith(root);
    }

    private static PatternBasedMessageTranslator<ArgumentParseException> createMessageTranslator() {
        return new PatternBasedMessageTranslator<>(ArgumentParseException.class, false)
                .withPattern(Pattern.compile("Expected a number, got '(.+)'"), ":expectedNumber")
                .withPattern(Pattern.compile("Expected a boolean \\(yes/no\\), got '(.+)'"), ":expectedBoolean")
                .withPattern(Pattern.compile("Expected '(.+)' to be a number"), ":expectedNumber")
                .withPattern(Pattern.compile("A valid value is greater than or equal to (.+) \\(you entered (.+)\\)"), ":validIsGreaterThanOrEqualButFound")
                .withPattern(Pattern.compile("A valid value is less than or equal to (.+) \\(you entered (.+)\\)"), ":validIsLessThanOrEqualButFound")
                .withPattern(Pattern.compile("The given text doesn't match the right format \\(technically speaking, the 'format' is (.+)\\)"), ":textDoesntMatchRegex")
                .withPattern(Pattern.compile("No matching value found in the '(.+)' list\\."), ":invalidEnumOfType");
    }

}
