package legend.game.types;

import legend.game.unpacker.FileData;

public record UiFile(UiType uiElements_0000, UiType itemIcons_c6a4, UiType portraits_cfac, UiType _d2d8) {
  public static UiFile fromFile(final FileData data) {
    final UiType uiElements_0000 = UiType.fromFile(data);
    final int uiElementsSize = calculateSize(uiElements_0000);
    final UiType itemIcons_c6a4 = UiType.fromFile(data.slice(uiElementsSize));
    final int itemIconsSize = calculateSize(itemIcons_c6a4);
    final UiType portraits_cfac = UiType.fromFile(data.slice(uiElementsSize + itemIconsSize));
    final int portraitsSize = calculateSize(portraits_cfac);
    final UiType _d2d8 = UiType.fromFile(data.slice(uiElementsSize + itemIconsSize + portraitsSize));

    return new UiFile(uiElements_0000, itemIcons_c6a4, portraits_cfac, _d2d8);
  }

  private static int calculateSize(final UiType type) {
    int size = 0x8 + type.entries_08.length * 0x8;

    for(final UiPart part : type.entries_08) {
      size += 0x8 + part.metrics_00().length * 0x14;
    }

    return size;
  }

  public void delete() {
    if(this.uiElements_0000.obj != null) {
      this.uiElements_0000.obj.delete();
      this.uiElements_0000.obj = null;
    }

    if(this.itemIcons_c6a4.obj != null) {
      this.itemIcons_c6a4.obj.delete();
      this.itemIcons_c6a4.obj = null;
    }

    if(this.portraits_cfac.obj != null) {
      this.portraits_cfac.obj.delete();
      this.portraits_cfac.obj = null;
    }

    if(this._d2d8.obj != null) {
      this._d2d8.obj.delete();
      this._d2d8.obj = null;
    }
  }
}
