/*
 * Intake-Spigot, a Spigot bridge for the Intake command framework.
 * Copyright (C) Philipp Nowak (Literallie)
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
