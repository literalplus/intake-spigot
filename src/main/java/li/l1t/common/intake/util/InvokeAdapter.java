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

package li.l1t.common.intake.util;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.ImmutableDescription;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.ArgumentParser;
import com.sk89q.intake.parametric.handler.InvokeHandler;
import com.sk89q.intake.parametric.handler.InvokeListener;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * Adapter for an invoke listener that is also an invoke handler and just silently accepts all
 * invocations without any checks.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-15
 */
public abstract class InvokeAdapter implements InvokeListener, InvokeHandler {
    @Override
    public boolean preProcess(List<? extends Annotation> annotations, ArgumentParser parser, CommandArgs commandArgs) throws CommandException, ArgumentException {
        return true;
    }

    @Override
    public boolean preInvoke(List<? extends Annotation> annotations, ArgumentParser parser, Object[] args, CommandArgs commandArgs) throws CommandException, ArgumentException {
        return true;
    }

    @Override
    public void postInvoke(List<? extends Annotation> annotations, ArgumentParser parser, Object[] args, CommandArgs commandArgs) throws CommandException, ArgumentException {
        //no-op
    }

    @Override
    public InvokeHandler createInvokeHandler() {
        return this;
    }

    @Override
    public void updateDescription(Set<Annotation> annotations, ArgumentParser parser, ImmutableDescription.Builder descriptionBuilder) {
        //no-op
    }
}
