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

package li.l1t.common.intake;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

/**
 * Intake implementation of Bukkit's {@link TabCompleter}
 * Will handle tab completion for commands registered via intake
 * @author jonahseguin
 */
public class IntakeTabCompleter implements TabCompleter {

    private final CommandsManager commandsManager;

    public IntakeTabCompleter(CommandsManager commandsManager) {
        this.commandsManager = commandsManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        final String cmdName = command.getName();
        IntakeCommand intakeCommand = this.commandsManager.getCommand(cmdName);
        if (intakeCommand != null) {
            return intakeCommand.tabComplete(sender, label, args);
        }
        return Collections.emptyList();
    }
}
