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

import com.sk89q.intake.CommandException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.util.auth.AuthorizationException;
import li.l1t.common.intake.exception.CommandExitMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * A command implementation that forwards all calls to an Intake dispatcher.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class IntakeCommand extends Command implements PluginIdentifiableCommand {
    private final Plugin plugin;
    private final Dispatcher dispatcher;
    private final Namespace namespace = new Namespace();

    public IntakeCommand(Plugin plugin, Dispatcher dispatcher,
                         String name, List<String> aliases) {
        super(name, "The " + name + " command", "Execution error, this should not happen", aliases);
        this.plugin = plugin;
        this.dispatcher = dispatcher;
        namespace.put(plugin.getClass(), plugin);
    }

    public boolean execute(CommandSender sender, String alias, String[] args) {
        namespace.put(CommandSender.class, sender);
        StringBuilder lineBuilder = new StringBuilder(alias);
        for (String arg : args) {
            lineBuilder.append(" ").append(arg);
        }

        try {
            dispatcher.call(lineBuilder.toString(), namespace, Collections.emptyList());
        } catch (CommandException | CommandExitMessage e) {
            sender.sendMessage(e.getMessage());
        } catch (AuthorizationException e) {
            sender.sendMessage("§c§lFehler: §cDas darfst du nicht.");
        } catch (Exception e) {
            sender.sendMessage("§4§lInterner Fehler: §c" + e.getClass().getSimpleName());
            plugin.getLogger().log(Level.WARNING, "Exception executing command: /" + lineBuilder, e);
        }

        return true;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public Namespace getNamespace() {
        return namespace;
    }
}
