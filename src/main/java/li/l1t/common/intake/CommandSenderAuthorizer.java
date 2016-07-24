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
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.util.auth.Authorizer;
import org.bukkit.command.CommandSender;

/**
 * Authorizes command execution based on whether the command sender in the namespace has a
 * permission.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-23
 */
public class CommandSenderAuthorizer implements Authorizer {
    @Override
    public boolean testPermission(Namespace namespace, String permission) {
        CommandSender sender = namespace.get(CommandSender.class);
        Preconditions.checkNotNull(sender, "No command sender available for authorization");
        return sender.hasPermission(permission);
    }
}
