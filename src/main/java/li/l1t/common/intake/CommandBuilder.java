/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.intake;

import com.google.common.base.Preconditions;
import com.sk89q.intake.dispatcher.Dispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides a fluent builder interface for Intake commands. Creates an Intake context for a
 * command, registers it with Bukkit and saves its metadata to the commands manager.
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
        this.dispatcher = manager.getCommandGraph()
                .commands()
                .group(nameAndAliases.toArray(new String[nameAndAliases.size()]))
                .registerMethods(handler)
                .parent()
                .graph()
                .getDispatcher();
        return this;
    }

    public CommandBuilder create() {
        Preconditions.checkNotNull(dispatcher, "dispatcher");
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(aliases, "aliases");
        this.command = new IntakeCommand(manager.getPlugin(), dispatcher, name, aliases);
        manager.applyNamespaceTemplateTo(command.getNamespace());
        return this;
    }

    public CommandBuilder register() {
        Preconditions.checkNotNull(command, "command");
        manager.commandRegistrationManager.registerCommand(command);
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
