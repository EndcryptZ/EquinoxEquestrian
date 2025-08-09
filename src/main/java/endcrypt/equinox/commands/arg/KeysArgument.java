package endcrypt.equinox.commands.arg;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import endcrypt.equinox.equine.nbt.Keys;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class KeysArgument extends CustomArgument<Keys, String> {
    public KeysArgument() {
        super(new StringArgument("key"), info -> {
            String keyName = info.currentInput();
            Keys key = Keys.valueOf(keyName);

            if (!key.name().equals(keyName)) {
                throw CustomArgumentException.fromString("No Key found: " + keyName);
            }
            return key;
        });

        this.replaceSuggestions(ArgumentSuggestions.stringCollectionAsync(info ->
                CompletableFuture.supplyAsync(() -> Arrays.stream(Keys.values()).map(Keys::name).toList())
        ));
    }
}
