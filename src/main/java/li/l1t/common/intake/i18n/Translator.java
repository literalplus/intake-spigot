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

package li.l1t.common.intake.i18n;

import org.bukkit.command.CommandSender;

import java.util.Locale;
import java.util.function.Function;

/**
 * Translates message keys into messages with optional arguments.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-11
 */
public interface Translator {
    /**
     * @param locale the locale to set the translator to
     *
     * @deprecated translators support translation per sender now, use {@link #setSelectionProvider(LocaleSelectionProvider)}
     */
    @Deprecated
    void setLocale(Locale locale);

    /**
     * @param selectionProvider the selection provider to use for retrieval of the preferred languages of command
     *                          senders
     */
    void setSelectionProvider(LocaleSelectionProvider selectionProvider);

    LocaleSelectionProvider getSelectionProvider();

    String translate(CommandSender sender, Message message);

    String translate(Locale locale, Message message);

    Function<Message, String> translationFunctionFor(CommandSender sender);
}
