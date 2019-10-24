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

import com.google.common.base.Preconditions;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.fluent.DispatcherNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides a fluent builder interface for Intake commands. Creates an Intake context for a command, registers it with
 * Bukkit and saves its metadata to the commands manager.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandBuilder {
    private final CommandsManager manager;
    private Dispatcher dispatcher;
    private String name;
    private List<String> aliases;
    private IntakeCommand command;
    private DispatcherNode rootGroup;

    public CommandBuilder(CommandsManager manager) {
        this.manager = manager;
    }

    public CommandBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CommandBuilder withAliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    public CommandBuilder withDispatcherFor(Object handler) {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(aliases, "aliases");
        List<String> nameAndAliases = new ArrayList<>();
        nameAndAliases.add(name);
        nameAndAliases.addAll(aliases);
        this.rootGroup = manager.getCommandGraph()
                .commands()
                .group(nameAndAliases.toArray(new String[nameAndAliases.size()]));
        this.dispatcher = rootGroup.registerMethods(handler)
                .parent().graph().getDispatcher();
        return this;
    }


    /**
     * Adds a handler for sub-commands in a separate class from the root handler.
     *
     * @param handler the handler to register
     * @param aliases the aliases (below this builder's command) to register with
     * @return this instance
     */
    public CommandBuilder withSubHandler(Object handler, String... aliases) {
        if (aliases != null && aliases.length > 0) {
            rootGroup.group(aliases).registerMethods(handler);
        }
        else {
            rootGroup.registerMethods(handler);
        }
        return this;
    }

    public CommandBuilder create() {
        Preconditions.checkNotNull(dispatcher, "dispatcher");
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(aliases, "aliases");
        this.command = new IntakeCommand(manager, dispatcher, name, aliases);
        manager.applyNamespaceTemplateTo(command.getNamespace());
        return this;
    }

    public CommandBuilder register() {
        Preconditions.checkNotNull(command, "command");
        manager.commandRegistrationManager.registerCommand(command, manager.getFallbackPrefix());
        manager.getPlugin().getServer().getPluginCommand(command.getName()).setTabCompleter(command);
        return this;
    }

    CommandBuilder putIntoNamespaceIfAvailable(Object key, Object value) {
        if (command != null) {
            command.getNamespace().put(key, value);
        }
        return this;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public CommandsManager getManager() {
        return manager;
    }

    public IntakeCommand getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
