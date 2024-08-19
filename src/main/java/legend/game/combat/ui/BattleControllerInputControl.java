package legend.game.combat.ui;

import legend.game.Scus94491BpeSegment_8002;
import legend.game.inventory.screens.TextColour;
import legend.game.types.Icon;
import legend.game.types.RenderTextProperties;

import java.util.ArrayList;
import java.util.List;

public class BattleControllerInputControl {
  public static List<RenderTextProperties> defaultControllerInputControl = new ArrayList<>();

  static {
    final var scaleX = 1.0f;
    final var scaleY = 1.0f;
    final var scaleZ = 1.0f;

    defaultControllerInputControl.add(new RenderTextProperties(Character.toString(Icon.UP_ARROW), 10, 50, TextColour.WHITE, 0, scaleX, scaleY, scaleZ));
  }

  public void render() {
    for(RenderTextProperties properties : defaultControllerInputControl) {
      Scus94491BpeSegment_8002.renderText(properties);
    }
  }
}
