package legend.integration.core;

import java.util.Arrays;

public enum PlayInput {
    NOOP(new String[]{""}),
    UP(new String[]{"up"}),
    DOWN(new String[]{"down"}),
    LEFT(new String[]{"left"}),
    RIGHT(new String[]{"right"}),
    X(new String[]{"x", "cross"}),
    SQUARE(new String[]{"square"}),
    TRIANGLE(new String[]{"triangle"}),
    CIRCLE(new String[]{"circle"}),
    L1(new String[]{"l1"}),
    L2(new String[]{"l2"}),
    R1(new String[]{"r1"}),
    R2(new String[]{"r2"}),
    START(new String[]{"start"}),
    SELECT(new String[]{"select"}),
    ;

    private final String[] parseValues;

    PlayInput(String[] parseValues) {
        this.parseValues = parseValues;
    }

    public static PlayInput parse(String inputText) {
        if (inputText == null || inputText.isBlank()) {
            return PlayInput.NOOP;
        }
        final String lookup = inputText.toLowerCase();

        return Arrays.stream(PlayInput.values()).filter(pInput -> Arrays.asList(pInput.parseValues).contains(lookup)).findFirst().orElse(PlayInput.NOOP);
    }


}
