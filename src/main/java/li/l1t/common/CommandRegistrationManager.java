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

package li.l1t.common;

import com.google.common.base.Preconditions;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginIdentifiableCommand;

import java.lang.reflect.Field;

/**
 * Manages command registration for commands registered with the MTC command API. This interfaces
 * directly with the server's command map using Reflection, but stores the instance once
 * retrieved. That means that one instance can only be used for commands from the same server.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2015-12-30
 */
public class CommandRegistrationManager {
    private CommandMap commandMap;
    private Server server;

    /**
     * Attempts to register a command with the corresponding server's command map. Note that this method uses Reflection
     * to access the command map and may fail due to that. The command map is cached once retrieved.
     *
     * @param command the command to register
     * @throws IllegalStateException if retrieval of the command map fails
     */
    public <T extends Command & PluginIdentifiableCommand> void registerCommand(T command) throws IllegalStateException {
        getCommandMap(command.getPlugin().getServer()).register("mtc", command);
    }

    /**
     * Retrieves a server's command map or serves the one cached in this manger.
     *
     * @param server the server to find the command map for
     * @return the command map
     * @throws IllegalStateException if the command map could not be retrieved
     */
    public CommandMap getCommandMap(Server server) throws IllegalStateException {
        Preconditions.checkArgument(this.server == null || server == this.server, "Server does not match manager's server!");
        if (commandMap == null) {
            this.server = server;
            try {
                Field commandMapField = server.getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (CommandMap) commandMapField.get(server);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalStateException("Failed to retrieve command map for Intake registrations", e);
            }
        }

        return commandMap;
    }

    /**
     * @return the server whose command map this manager is accessing or null if none
     */
    public Server getServer() {
        return server;
    }
}
