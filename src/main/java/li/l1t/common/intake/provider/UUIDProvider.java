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
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;
import li.l1t.common.intake.i18n.Message;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Provides UUIDs from the arguments.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-04-26
 */
public class UUIDProvider implements Provider<UUID> {
    @Override
    public boolean isProvided() {
        return false;
    }

    @Override
    public UUID get(CommandArgs args, List<? extends Annotation> annotations)
            throws ArgumentException, ProvisionException {
        String idString = args.next();
        try {
            return UUID.fromString(idString);
        } catch (Exception e) {
            throw new CommandExitMessage(Message.of("InvalidUUID", idString, UUID.randomUUID()));
        }
    }

    @Override
    public List<String> getSuggestions(String s) {
        return Collections.emptyList();
    }
}
