/*
 * Copyright (c) 2013 - 2015 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) or email xxyy98+xyclicense@gmail.com for details.
 */

package li.l1t.common.intake.provider;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;


/**
 * Provides the executing command sender instance to commands as a player instance or sends them an
 * error message.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class PlayerSenderProvider implements Provider<Player> {
    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public Player get(CommandArgs arguments, List<? extends Annotation> modifiers) throws
            ArgumentException, ProvisionException {
        CommandSender sender = arguments.getNamespace().get(CommandSender.class);
        if (sender == null) {
            throw new ProvisionException("No sender in namespace");
        } else if(!(sender instanceof Player)) {
            throw new CommandExitMessage("§cThis command must be executed by a player!");
        }
        return (Player) sender;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }
}
