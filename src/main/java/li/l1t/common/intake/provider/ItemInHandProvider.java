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

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.i18n.Translator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;


/**
 * Provides the item in the hand of the executing player.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class ItemInHandProvider extends NamespaceAwareProvider<ItemStack> {
    public ItemInHandProvider(Translator translator) {
        super(translator);
    }

    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public ItemStack get(CommandArgs arguments, List<? extends Annotation> modifiers) throws
            ArgumentException, ProvisionException {
        CommandSender sender = getFromNamespaceOrFail(arguments, CommandSender.class);
        throwLocalizedIf(!(sender instanceof Player), "PlayerOnlyCommand");
        //noinspection ConstantConditions
        Player player = (Player) sender;
        ItemStack itemInHand = player.getItemInHand();
        throwLocalizedIf(itemInHand == null, "NoItemInHand");
        return itemInHand;
    }

}
