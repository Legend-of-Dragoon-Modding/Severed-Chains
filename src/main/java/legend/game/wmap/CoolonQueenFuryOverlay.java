package legend.game.wmap;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.platform.input.InputCodepoints;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Graphics.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.wmap.WMap.UI_WHITE_SHADOWED_RIGHT;

public class CoolonQueenFuryOverlay {
  /** Wmap.coolonIconStateIndices_800ef154 */
  private static final int[] coolonIconStates = {0, 1, 2, 3, 0};
  /** Wmap.queenFuryIconStateIndices_800ef158 */
  private static final int[] queenFuryIconStates = {0, 0, 0, 0, 1, 2, 3, 4, 4, 4, 4, 3, 2, 1, 0};

  private Obj coolonSprites;
  private Obj queenFurySprites;

  public CoolonQueenFuryOverlay() {
    this.buildCoolonIcon();
    this.buildQueenFuryIcon();
  }

  private void buildCoolonIcon() {
    final QuadBuilder builder = new QuadBuilder("CoolonIcon");

    for(int i = 0; i < 4; i++) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .clut(640, 506)
        .vramPos(640, 256)
        .monochrome(1.0f)
        .size(32.0f, 16.0f)
        .uv(i * 32.0f, 128.0f)
      ;
    }

    this.coolonSprites = builder.build();
  }

  private void buildQueenFuryIcon() {
    final QuadBuilder builder = new QuadBuilder("QueenFuryIcon");

    for(int i = 0; i < 5; i++) {
      builder
        .add()
        .bpp(Bpp.BITS_4)
        .clut(640, 507)
        .vramPos(640, 256)
        .monochrome(1.0f)
        .pos(22.0f, -2.0f, 0.0f)
        // Negating the width to flip the image - it looks better with the keybind display with the door opening away from the keybind
        .posSize(-24.0f, 24.0f)
        .uv(i * 24.0f, 144.0f)
        .uvSize(23.0f, 24.0f)
      ;
    }

    this.queenFurySprites = builder.build();
  }

  private final MV iconTransforms = new MV();

  /** @param mode 0: Coolon icon, 1: Queen Fury icon */
  public void render(final int mode) {
    final int oldZ = textZ_800bdf00;
    textZ_800bdf00 = 13;
    renderText(InputCodepoints.getActionName(LodMod.INPUT_ACTION_WMAP_QUEEN_FURY_COOLON.get()), GPU.getOffsetX() + 98.0f, GPU.getOffsetY() + 87.0f, UI_WHITE_SHADOWED_RIGHT);
    textZ_800bdf00 = oldZ;

    final int iconState;
    final Obj icon;
    if(mode == 0) {
      iconState = coolonIconStates[(int)(tickCount_800bb0fc / 2 / (3.0f / vsyncMode_8007a3b8) % 5)];
      icon = this.coolonSprites;
    } else {
      iconState = queenFuryIconStates[(int)(tickCount_800bb0fc / 3 / (3.0f / vsyncMode_8007a3b8) % 15)];
      icon = this.queenFurySprites;
    }

    this.iconTransforms.transfer.set(GPU.getOffsetX() + 106.0f, GPU.getOffsetY() + 84.0f, 52.0f);
    RENDERER
      .queueOrthoModel(icon, this.iconTransforms, QueuedModelStandard.class)
      .vertices(iconState * 4, 4)
    ;
  }

  public void deallocate() {
    this.coolonSprites.delete();
    this.queenFurySprites.delete();
  }
}
