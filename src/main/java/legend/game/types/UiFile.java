package legend.game.types;

import legend.game.unpacker.FileData;

public record UiFile(UiType uiElements_0000, UiType itemIcons_c6a4, UiType portraits_cfac, UiType _d2d8) {
  public static UiFile fromFile(final FileData data) {
    final UiType uiElements_0000 = UiType.fromFile(data);
    final UiType itemIcons_c6a4 = UiType.fromFile(data.slice(0xc6a4));
    final UiType portraits_cfac = UiType.fromFile(data.slice(0xcfac));
    final UiType _d2d8 = UiType.fromFile(data.slice(0xd2d8));

    return new UiFile(uiElements_0000, itemIcons_c6a4, portraits_cfac, _d2d8);
  }
}
