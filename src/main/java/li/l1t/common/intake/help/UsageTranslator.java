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

package li.l1t.common.intake.help;

import li.l1t.common.i18n.Message;
import li.l1t.common.intake.i18n.Translator;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * Translates bundled messages and command meta for display in usage information.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-17
 */
class UsageTranslator {
    private final Translator translator;
    private final CommandMetaTranslator metaTranslator;
    private final CommandSender sender;

    UsageTranslator(Translator translator, CommandMetaTranslator metaTranslator, CommandSender sender) {
        this.translator = translator;
        this.metaTranslator = metaTranslator;
        this.sender = sender;
    }

    public String translateBuiltIn(Message message) {
        return translator.translate(findLocale(), message);
    }

    public String translateMeta(String input) {
        return metaTranslator.translate(input, findLocale());
    }

    private Locale findLocale() {
        return translator.getSelectionProvider().findLocale(sender);
    }
}
