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

package li.l1t.common.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


/**
 * Static utility class that allows to send {@link ComponentBuilder}s to Bukkit {@link CommandSender} instances.
 * This allows to use the builder but still not exclude non-Player senders from receiving formatted messages.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2015-09-10
 */
public class ComponentSender {
    private ComponentSender() {

    }

    /**
     * Sends the built components from the given builder to a receiver. Players will receiver fully formatted JSON chat
     * including events and all that fancy stuff, everything else will receive solely the legacy text representation.
     *
     * @param builder  the builder to use to create the parts to send
     * @param receiver the receiver of the parts
     *
     * @return whether a message was sent
     */
    public static boolean sendTo(ComponentBuilder builder, CommandSender receiver) {
        return sendTo(builder.create(), receiver);
    }

    /**
     * Sends the given components to a receiver. Players will receiver fully formatted JSON chat including events and
     * all that fancy stuff, everything else will receive solely the legacy text representation.
     *
     * @param parts    the parts to send
     * @param receiver the receiver of the parts
     *
     * @return whether a message was sent
     */
    public static boolean sendTo(BaseComponent[] parts, CommandSender receiver) {
        if (receiver instanceof Player) {
            ((Player) receiver).spigot().sendMessage(parts);
        } else {
            receiver.sendMessage(TextComponent.toLegacyText(parts));
        }
        return true;
    }

    /**
     * Sends the given components to a receiver. Players will receiver fully formatted JSON chat including events and
     * all that fancy stuff, everything else will receive solely the legacy text representation. This method executes
     * the action in the main server thread and is intended for code in async threads.
     *
     * @param parts    the parts to send
     * @param receiver the receiver of the parts
     * @param plugin   the plugin to use for accessing the scheduler
     *
     * @return whether a message was sent
     */
    public static boolean sendToSync(BaseComponent[] parts, CommandSender receiver, Plugin plugin) {
        plugin.getServer().getScheduler().runTask(plugin, () -> sendTo(parts, receiver));
        return true;
    }

    /**
     * Sends the given components to a receiver. Players will receiver fully formatted JSON chat including events and
     * all that fancy stuff, everything else will receive solely the legacy text representation. This method executes
     * the action in the main server thread and is intended for code in async threads.
     *
     * @param builder  the builder to use to create the parts to send
     * @param receiver the receiver of the parts
     * @param plugin   the plugin to use for accessing the scheduler
     *
     * @return whether a message was sent
     */
    public static boolean sendToSync(ComponentBuilder builder, CommandSender receiver, Plugin plugin) {
        plugin.getServer().getScheduler().runTask(plugin, () -> sendTo(builder.create(), receiver));
        return true;
    }
}
