package legend.game.inventory.screens.controls;

import legend.game.inventory.Good;
import legend.game.inventory.GoodsInventory;
import legend.game.inventory.screens.Control;
import legend.game.types.MenuGlyph06;
import legend.game.types.Renderable58;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import static legend.game.Menus.allocateManualRenderable;
import static legend.game.Menus.uploadRenderable;
import static legend.game.SItem.initGlyph;
import static legend.lodmod.LodGoods.BLUE_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.DARK_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.DIVINE_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.GOLD_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.JADE_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.RED_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.SILVER_DRAGOON_SPIRIT;
import static legend.lodmod.LodGoods.VIOLET_DRAGOON_SPIRIT;

public class DragoonSpirits extends Control {
  private static final RegistryDelegate<Good>[] dragoonSpiritGoodsBits_800fbabc = new RegistryDelegate[8];
  static {
    dragoonSpiritGoodsBits_800fbabc[0] = RED_DRAGOON_SPIRIT;
    dragoonSpiritGoodsBits_800fbabc[1] = JADE_DRAGOON_SPIRIT;
    dragoonSpiritGoodsBits_800fbabc[2] = SILVER_DRAGOON_SPIRIT;
    dragoonSpiritGoodsBits_800fbabc[3] = DARK_DRAGOON_SPIRIT;
    dragoonSpiritGoodsBits_800fbabc[4] = VIOLET_DRAGOON_SPIRIT;
    dragoonSpiritGoodsBits_800fbabc[5] = BLUE_DRAGOON_SPIRIT;
    dragoonSpiritGoodsBits_800fbabc[6] = GOLD_DRAGOON_SPIRIT;
    dragoonSpiritGoodsBits_800fbabc[7] = DIVINE_DRAGOON_SPIRIT;
  }

  private final Renderable58[] renderables = new Renderable58[8];

  public DragoonSpirits(final GoodsInventory spirits) {
    this.setSpirits(spirits);
  }

  public void setSpirits(final GoodsInventory spirits) {
    for(int spiritIndex = 0; spiritIndex < dragoonSpiritGoodsBits_800fbabc.length; spiritIndex++) {
      final RegistryDelegate<Good> bit = dragoonSpiritGoodsBits_800fbabc[spiritIndex];
      if(spirits.has(bit)) {
        final Renderable58 renderable = allocateManualRenderable();
        final MenuGlyph06 glyph = new MenuGlyph06(13 + spiritIndex, spiritIndex * 12, 0);
        initGlyph(renderable, glyph);
        renderable.z_3c = 33;
        this.renderables[spiritIndex] = renderable;
      } else {
        this.renderables[spiritIndex] = null;
      }
    }
  }

  @Override
  protected void render(final int x, final int y) {
    for(final Renderable58 renderable : this.renderables) {
      if(renderable != null) {
        uploadRenderable(renderable, x, y);
      }
    }
  }
}
