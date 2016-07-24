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
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;


/**
 * Provides the executing command sender instance to commands.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class CommandSenderProvider implements Provider<CommandSender> {
    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public CommandSender get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        CommandSender sender = arguments.getNamespace().get(CommandSender.class);
        if (sender == null) {
            throw new ProvisionException("No sender in namespace");
        }
        return sender;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }
}
