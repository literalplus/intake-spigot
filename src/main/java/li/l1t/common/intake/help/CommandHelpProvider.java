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

import li.l1t.common.chat.ComponentSender;
import li.l1t.common.intake.CommandsManager;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides usage information for commands.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-01
 */
public class CommandHelpProvider {
    private final CommandsManager manager;
    private final Map<String, Collection<BaseComponent[]>> cachedUsages = new HashMap<>();

    public CommandHelpProvider(CommandsManager manager) {
        this.manager = manager;
    }

    public void sendHelpOfTo(CommandSender sender, String argLine) {
        Collection<BaseComponent[]> messages = cachedUsages.get(argLine);
        if (messages == null) {
            messages = fetchHelpOf(argLine);
        }
        messages.forEach(components -> ComponentSender.sendTo(components, sender));
    }

    private Collection<BaseComponent[]> fetchHelpOf(String argLine) {
        Collection<BaseComponent[]> result;
        CommandHelpExtractor extractor = new CommandHelpExtractor(
                manager, new ArrayDeque<>(Arrays.asList(argLine.split(" ")))
        );
        result = extractor.build().getMessages();
        if (extractor.isFound()) {
            cachedUsages.put(argLine, result);
        }
        return result;
    }
}
