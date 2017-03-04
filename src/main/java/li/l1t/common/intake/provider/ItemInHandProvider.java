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

package li.l1t.common.intake.provider;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.ProvisionException;
import org.bukkit.Material;
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
        throwLocalizedIf(itemInHand == null || itemInHand.getType() == Material.AIR, "NoItemInHand");
        return itemInHand;
    }

}
