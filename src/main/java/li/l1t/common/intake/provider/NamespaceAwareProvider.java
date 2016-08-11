package li.l1t.common.intake.provider;

import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;
import li.l1t.common.intake.i18n.Translator;

import java.util.Collections;
import java.util.List;

/**
 * An abstract base class for providers that are aware of namespaces.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-08-11
 */
abstract class NamespaceAwareProvider<V> implements Provider<V> {
    private final Translator translator;

    protected NamespaceAwareProvider(Translator translator) {
        this.translator = translator;
    }

    <T> T getFromNamespaceOrFail(CommandArgs arguments, Class<T> key) throws
            ProvisionException {
        T value = arguments.getNamespace().get(key);
        if (value == null) {
            throw new ProvisionException("No " + key.getSimpleName() + " in namespace");
        }
        return value;
    }

    void throwLocalized(String messageKey, Object... arguments) throws CommandExitMessage {
        throw new CommandExitMessage(translator.translate(messageKey, arguments));
    }

    void throwLocalizedIf(boolean condition, String messageKey, Object... arguments) throws CommandExitMessage {
        if(condition) {
            throwLocalized(messageKey, arguments);
        }
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return Collections.emptyList();
    }
}
