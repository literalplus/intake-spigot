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

import com.sk89q.intake.CommandException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.parametric.ProvisionException;
import com.sk89q.intake.util.auth.AuthorizationException;
import li.l1t.common.exception.InternalException;
import li.l1t.common.exception.UserException;
import li.l1t.common.intake.exception.UncheckedException;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.intake.i18n.translator.generic.CauseRewritingTranslator;
import li.l1t.common.intake.i18n.translator.generic.MessageAwareTranslator;
import li.l1t.common.intake.i18n.translator.generic.PatternBasedMessageTranslator;
import li.l1t.common.intake.i18n.translator.generic.StaticTranslator;
import li.l1t.common.intake.i18n.translator.specific.ExitMessageTranslator;

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
        registerIllegalStateWith(root);
    }

    private static void registerXYCIntakeExceptionsWith(ErrorTranslator root) {
        new ExitMessageTranslator().registerWith(root);
        new MessageAwareTranslator<>("UserException", UserException.class).registerWith(root);
        new MessageAwareTranslator<>("InternalException", InternalException.class).registerWith(root);
        new CauseRewritingTranslator<>(UncheckedException.class).registerWith(root);
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
        new MessageAwareTranslator<>("IllegalArgument", IllegalArgumentException.class, true)
                .registerWith(root);
    }

    private static void registerNullPointerWith(ErrorTranslator root) {
        new MessageAwareTranslator<>("NullPointer", NullPointerException.class, true)
                .registerWith(root);
    }

    private static void registerIllegalStateWith(ErrorTranslator root) {
        new MessageAwareTranslator<>("IllegalState", IllegalStateException.class, true)
                .registerWith(root);
    }
}
