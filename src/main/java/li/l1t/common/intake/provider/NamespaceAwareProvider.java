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

package li.l1t.common.intake.provider;

import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;
import li.l1t.common.i18n.Message;

import java.util.Collections;
import java.util.List;

/**
 * An abstract base class for providers that are aware of namespaces.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-11
 */
abstract class NamespaceAwareProvider<V> implements Provider<V> {
    <T> T getFromNamespaceOrFail(CommandArgs arguments, Class<T> key) throws ProvisionException {
        T value = arguments.getNamespace().get(key);
        if (value == null) {
            throw new ProvisionException("No " + key.getSimpleName() + " in namespace");
        }
        return value;
    }

    void throwLocalizedIf(boolean condition, String messageKey, Object... arguments) throws CommandExitMessage {
        if(condition) {
            throw new CommandExitMessage(Message.of(messageKey, arguments));
        }
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return Collections.emptyList();
    }
}
