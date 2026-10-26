package legend.game.wmap;

import legend.core.gpu.Rect4i;
import legend.core.renderer.Translucency;
import org.joml.Vector3f;
import java.util.List;
import java.util.Objects;

/** Location prompt layout and facility presentation, independent of travel decisions. */
final class WMapLocationPresentation {
  private WMapLocationPresentation() { }

  static WmapPromptPopup createPrompt(final String name, final int textZ, final int destinationCut, final int destinationScene, final String[] regions, final List<String> placeServices) {
    final WmapPromptPopup popup = new WmapPromptPopup(Objects.requireNonNull(name), textZ * 4.0f)
      .addOptionText("Don't enter");

    if(destinationCut == 999) { // Going to a different region
      final String dest1 = regions[destinationScene >>> 4 & 0xffff];
      final String dest2 = regions[destinationScene & 0xf];

      popup
        .addOptionText(dest1)
        .addOptionText(dest2);
      popup.setOptionSpacing(18.0f);
      popup.setTranslation(WmapPromptPopup.ObjFields.OPTIONS, 240.0f, 164.0f, textZ * 4.0f - 2.0f);
    } else {
      popup.addOptionText("Enter");
    }
    for(final String service : placeServices) popup.addAltText(service);

    if(placeServices.isEmpty()) {
      popup.addAltText("No facilities");
      popup.setTranslation(WmapPromptPopup.ObjFields.ALT_TEXT, 240.0f, 63.0f, textZ * 4.0f - 2.0f);
    }

    popup.setHighlight(
      WmapPromptPopup.HighlightMode.SHADOW,
      new WmapMenuTextHighlight40(
        0.0f,
        new Vector3f(0.5f),
        new Rect4i(176, 120, 128, 40),
        8,
        8,
        4,
        true,
        Translucency.B_MINUS_F,
        55.0f
      )
    );

    popup.setHighlight(
      WmapPromptPopup.HighlightMode.SELECTOR,
      new WmapMenuTextHighlight40(
        0.5f,
        new Vector3f(1.0f, 0.0f, 0.0f),
        new Rect4i(176, 150, 128, 24),
        1,
        2,
        2,
        true,
        Translucency.B_PLUS_F,
        51.0f
      )
    );
    return popup;
  }
}
