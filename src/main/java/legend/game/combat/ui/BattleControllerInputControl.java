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
  private static final float dpadBaselinePositionY = 15.0f;

  static {
    createIcon(Icon.UP_ARROW, 0, -10, " UP ", labelLeft, labelAbove, dpadBaselinePositionX, dpadBaselinePositionY);
    createIcon(Icon.DOWN_ARROW_2, 0, 10, "DOWN", labelLeft, labelBelow, dpadBaselinePositionX, dpadBaselinePositionY);
    createIcon(Icon.LEFT_ARROW_2, -10, 0, "LEFT", labelLeft, 0, dpadBaselinePositionX, dpadBaselinePositionY);
    createIcon(Icon.RIGHT_ARROW, 10, 0, "RIGHT", labelRight, 0, dpadBaselinePositionX, dpadBaselinePositionY);
  }

  private static void createIcon(char text, final float x, final float y, String label, final float labelXSign, final float labelYSign, final float baselinePositionX, final float baselinePositionY) {
    final var iconLength = Scus94491BpeSegment_8002.charWidth(text);

    var labelLength = 0.0f;
    for(char chr : label.toCharArray()) {
      labelLength += Scus94491BpeSegment_8002.charWidth(chr);
    }

    final var isTop = (labelXSign < 0 && labelYSign < 0);
    final var isBottom = (labelXSign < 0 && labelYSign > 0);
    final var isLeft = (labelXSign < 0 && labelYSign == 0);
    final var isRight = (labelXSign > 0 && labelYSign == 0);

    var labelX = 0.0f;
    var labelY = 0.0f;

    if(isTop || isBottom) {
      labelX = (iconLength) - (labelLength / 2.0f);
      labelY = (labelYSign) * 8.0f;
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
}
