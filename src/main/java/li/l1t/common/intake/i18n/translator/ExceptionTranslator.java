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

package li.l1t.common.intake.i18n.translator;

import li.l1t.common.intake.i18n.ErrorTranslator;

/**
 * Translates exceptions into localized messages.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public interface ExceptionTranslator<E extends Exception> {
    String translate(E exception, ErrorTranslator root, String commandLine);

    Class<? extends E> getExceptionType();

    boolean needsLogging(E exception);
}
