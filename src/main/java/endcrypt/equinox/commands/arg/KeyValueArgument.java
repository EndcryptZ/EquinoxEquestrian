package endcrypt.equinox.commands.arg;

import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import endcrypt.equinox.equine.nbt.Keys;

public class KeyValueArgument extends CustomArgument<Object, String> {
    public KeyValueArgument() {
        super(new StringArgument("value"), info -> {
            // Get the selected key from the first argument
            Keys key = info.previousArgs().getUnchecked("key");

            if (key == null) {
                throw CustomArgumentException.fromString("No key provided before value.");
            }

            Class<?> type = key.getType();
            String input = info.currentInput();

            if (type == Integer.class) {
                try {
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    throw CustomArgumentException.fromString("Value for " + key.getKey() + " must be an integer.");
                }
            }
            else if (type == Double.class) {
                try {
                    return Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    throw CustomArgumentException.fromString("Value for " + key.getKey() + " must be a number.");
                }
            }
            else if (type == Long.class) {
                try {
                    return Long.parseLong(input);
                } catch (NumberFormatException e) {
                    throw CustomArgumentException.fromString("Value for " + key.getKey() + " must be a whole number.");
                }
            }
            else if (type == Boolean.class) {
                if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                    return Boolean.parseBoolean(input);
                } else {
                    throw CustomArgumentException.fromString("Value for " + key.getKey() + " must be true or false.");
                }
            }

            // Default to String
            return input;
        });
    }
}
