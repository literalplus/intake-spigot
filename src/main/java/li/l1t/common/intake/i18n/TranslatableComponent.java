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

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.function.Function;

/**
 * Represents a thing that, given a translation function, creates a translated message.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-04
 */
@FunctionalInterface
public interface TranslatableComponent {
    BaseComponent[] translate(Function<Message, String> translationFunction);
}
