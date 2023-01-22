package legend.integration.core;

import java.util.Arrays;
import java.util.Locale;

public enum PlayInput {
    NOOP(""),
    UP("up"),
    DOWN("down");

    private String parseValue;
    PlayInput(String parseValue) {
        this.parseValue = parseValue;
    }

    public static PlayInput parse(String inputText) {
        if(inputText == null || inputText.isBlank()) {
            return PlayInput.NOOP;
        }
        final String lookup = inputText.toLowerCase();

        return Arrays.stream(PlayInput.values()).filter(x -> x.parseValue.equals(lookup)).findFirst().orElse(PlayInput.NOOP);
    }


}
