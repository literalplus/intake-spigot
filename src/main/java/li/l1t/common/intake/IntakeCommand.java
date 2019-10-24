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

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.InvalidUsageException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.dispatcher.Dispatcher;
import li.l1t.common.i18n.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

/**
 * A command implementation that forwards all calls to an Intake dispatcher.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class IntakeCommand extends Command implements PluginIdentifiableCommand, TabCompleter {
    private final CommandsManager manager;
    private final Dispatcher dispatcher;
    private final Namespace namespace = new Namespace();

    public IntakeCommand(CommandsManager manager, Dispatcher dispatcher,
                         String name, List<String> aliases) {
        super(name, "The " + name + " command", "Execution error, this should not happen", aliases);
        this.manager = manager;
        this.dispatcher = dispatcher;
        namespace.put(manager.getPlugin().getClass(), manager.getPlugin());
    }

    public boolean execute(CommandSender sender, String alias, String[] args) {
        String argLine = convertToArgLine(alias, args);
        if (isHelpSubCommand(argLine)) {
            handleHelpSubCommand(sender, argLine);
        } else {
            populateNamespaceFor(sender);
            callDispatcher(sender, argLine);
        }
        return true;
    }

    private void populateNamespaceFor(CommandSender sender) {
        namespace.put(CommandSender.class, sender);
    }

    private String convertToArgLine(String alias, String[] args) {
        StringBuilder lineBuilder = new StringBuilder(alias);
        for (String arg : args) {
            lineBuilder.append(" ").append(arg);
        }
        return lineBuilder.toString();
    }

    private boolean isHelpSubCommand(String argLine) {
        return argLine.endsWith(" help");
    }

    private void handleHelpSubCommand(CommandSender sender, String argLine) {
        String rawArgLine = argLine.replace(" help", "");
        if (isHelpSubCommand(rawArgLine)) {
            sendNestedHelpError(sender);
        } else {
            sendHelp(sender, rawArgLine);
        }
    }

    private void sendNestedHelpError(CommandSender sender) {
        sender.sendMessage(manager.getTranslator().translate(sender, Message.of("Help.Nested")));
    }

    private void callDispatcher(CommandSender sender, String argLine) {
        try {
            dispatcher.call(argLine, namespace, Collections.emptyList());
        } catch (Exception e) {
            handleCommandException(argLine, sender, e);
        }
    }

    private void handleCommandException(String argLine, CommandSender sender, Exception exception) {
        if (manager.callExceptionListeners(argLine, sender, exception)) {
            handleExceptionTranslation(argLine, sender, exception);
        }
    }

    private void handleExceptionTranslation(String argLine, CommandSender sender, Exception e) {
        Message message = manager.getErrorTranslator().translateAndLogIfNecessary(e, argLine);
        if (message != null) {
            sender.sendMessage(manager.getTranslator().translate(sender, message));
        }
        sendHelpIfRequested(sender, argLine, e);
    }

    private void sendHelpIfRequested(CommandSender sender, String argLine, Exception e) {
        if (isHelpRequestedBy(e)) {
            sendHelp(sender, argLine);
        }
    }

    private boolean isHelpRequestedBy(Exception e) {
        return e instanceof InvalidUsageException && ((InvalidUsageException) e).isFullHelpSuggested();
    }

    private void sendHelp(CommandSender sender, String argLine) {
        manager.getHelpProvider().sendHelpOfTo(sender, argLine);
    }

    public Plugin getPlugin() {
        return manager.getPlugin();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        try {
            populateNamespaceFor(sender);
            return dispatcher.getSuggestions(convertToArgLine(alias, args), namespace);
        } catch (CommandException e) {
            return ImmutableList.of();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return this.tabComplete(sender, command.getName(), args);
    }
}
