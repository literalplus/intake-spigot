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
