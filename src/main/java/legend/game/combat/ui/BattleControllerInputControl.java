package legend.game.combat.ui;

import legend.game.Scus94491BpeSegment_8002;
import legend.game.inventory.screens.TextColour;
import legend.game.types.Icon;
import legend.game.types.RenderTextProperties;

import java.util.ArrayList;
import java.util.List;

public class BattleControllerInputControl {
  public static final List<RenderTextProperties> defaultControllerInputControl = new ArrayList<>();
  private static final float scaleX = 0.5f;
  private static final float scaleY = 0.5f;
  private static final float scaleZ = 1.0f;
  private static final float labelAbove = -1.0f;
  private static final float labelBelow = 1.0f;
  private static final float labelLeft = -1.0f;
  private static final float labelRight = 1.0f;
  private static final float scaleTextSpacing = 0.4f;
  private static final float scaleTextModifier = 0.7f;
  private static final float dpadBaselinePositionX = 290.0f;
  private static final float dpadBaselinePositionY = 47.0f;
  private static final float cardinalBaselinePositionX = 290.0f;
  private static final float cardinalBaselinePositionY = 15.0f;
  private static final float backButtonsBaselinePositionX = 299.0f;
  private static final float backButtonsBaselinePositionY = 77.0f;
  private static final float joystickBaseLinePositionX = 290.0f;
  private static final float joystickBaseLinePositionY = 112.0f;

  private static boolean isRendering = false;

  static {
    createIcon(Icon.UP_ARROW, 0, -10, -1," UP ", labelLeft, labelAbove, dpadBaselinePositionX, dpadBaselinePositionY);
    createIcon(Icon.DOWN_ARROW, 0, 10, -1, "DOWN", labelLeft, labelBelow, dpadBaselinePositionX, dpadBaselinePositionY);
    createIcon(Icon.LEFT_ARROW, -10, 0, -1, "LEFT", labelLeft, 0, dpadBaselinePositionX, dpadBaselinePositionY);
    createIcon(Icon.RIGHT_ARROW, 10, 0, -1, "RIGHT", labelRight, 0, dpadBaselinePositionX, dpadBaselinePositionY);

    createIcon(Icon.TRIANGLE, 0, -10, -1, " ADDITION", labelLeft, labelAbove, cardinalBaselinePositionX, cardinalBaselinePositionY);
    createIcon(Icon.X_THIN, 0, 10, -1, " SELECT", labelLeft, labelBelow, cardinalBaselinePositionX, cardinalBaselinePositionY);
    createIcon(Icon.SQUARE, -10, 0, 42, "SPELLS", labelLeft, 0, cardinalBaselinePositionX, cardinalBaselinePositionY);
    createIcon(Icon.CIRCLE, 10, 0, -1, "CANCEL", labelRight, 0, cardinalBaselinePositionX, cardinalBaselinePositionY);

    createIcon(Icon.LEFT_BUTTON_2, 0, -10, 60, "CONTROLS", labelLeft, 0, backButtonsBaselinePositionX, backButtonsBaselinePositionY);
    createIcon(Icon.RIGHT_BUTTON_2, 0, 0, 48, "CAMERA", labelLeft, 0, backButtonsBaselinePositionX, backButtonsBaselinePositionY);
    createIcon(Icon.LEFT_BUTTON_1, 0, 10, 48, "ESCAPE", labelLeft, 0, backButtonsBaselinePositionX, backButtonsBaselinePositionY);
    createIcon(Icon.RIGHT_BUTTON_1, 0, 20, 44, "GUARD", labelLeft, 0, backButtonsBaselinePositionX, backButtonsBaselinePositionY);

    createIcon(Icon.RIGHT_JOYSTICK, -1, 0, -1, "", 0, 0, joystickBaseLinePositionX, joystickBaseLinePositionY);
    createIcon(Icon.UP_ARROW, 0, -9, -1, " DRAGOON", labelLeft, labelAbove, joystickBaseLinePositionX, joystickBaseLinePositionY);
    createIcon(Icon.DOWN_ARROW, 0, 9, -1, " SPECIAL", labelLeft, labelBelow, joystickBaseLinePositionX, joystickBaseLinePositionY);
  }

  private static void createIcon(char text, final float x, final float y, final int labelWidth, String label, final float labelXSign, final float labelYSign, final float baselinePositionX, final float baselinePositionY) {
    final var iconLength = Scus94491BpeSegment_8002.charWidth(text);

    var labelLength = (labelWidth == -1)
      ? label.chars().map(chr -> Scus94491BpeSegment_8002.charWidth((char) chr)).sum()
      : labelWidth;

    final var isTop = (labelXSign < 0 && labelYSign < 0);
    final var isBottom = (labelXSign < 0 && labelYSign > 0);
    final var isLeft = (labelXSign < 0 && labelYSign == 0);
    final var isRight = (labelXSign > 0 && labelYSign == 0);

    var labelX = 0.0f;
    var labelY = 0.0f;

    if(isTop || isBottom) {
      labelX = (iconLength) - (labelLength / 2.0f);
      labelY = (labelYSign) * 10.0f;
    }

    if(isLeft) {
      labelX = (labelXSign) * (labelLength) + (x * labelXSign);
    }

    if(isRight) {
      labelX = (labelXSign) * (iconLength);
    }

    defaultControllerInputControl.add(new RenderTextProperties(Character.toString(text), baselinePositionX + (x * scaleX), baselinePositionY + (y * scaleY), TextColour.WHITE, 0, scaleX, scaleY, scaleZ, scaleTextSpacing));
    defaultControllerInputControl.add(new RenderTextProperties(label, baselinePositionX + ((x + labelX) * scaleX), baselinePositionY + ((y + labelY) * scaleY), TextColour.WHITE, 0, scaleX * scaleTextModifier, scaleY * scaleTextModifier, scaleZ * scaleTextModifier, scaleTextSpacing));
  }

  public void render() {
    for(RenderTextProperties properties : defaultControllerInputControl) {
      Scus94491BpeSegment_8002.renderText(properties);
    }
  }

  public boolean isRendering() {
    return this.isRendering;
  }

  public void toggleRendering() {
    this.isRendering = !this.isRendering;
  }
}
