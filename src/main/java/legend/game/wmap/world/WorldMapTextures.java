package legend.game.wmap.world;

import legend.core.gpu.VramTextureLoader;
import legend.core.renderer.Texture;
import legend.game.tim.Tim;

import java.util.Objects;

/** Render-thread conversion helpers. Returned textures belong to the caller, not emulated VRAM. */
public final class WorldMapTextures {
  private WorldMapTextures() { }

  /**
   * Decode an indexed TIM with one explicitly selected palette to an owned RGBA texture.
   * UVs must be local to this image. Multi-CLUT materials and palette animation require a custom
   * renderer; this conversion intentionally produces one static palette. Zero pixels stay clear.
   */
  public static Texture fromTim(final String name, final Tim tim, final int paletteIndex) {
    Objects.requireNonNull(tim, "tim");
    if(!tim.hasClut()) {
      throw new IllegalArgumentException("World map RGBA conversion requires an indexed TIM");
    }
    final var palettes = VramTextureLoader.palettesFromTim(tim);
    if(paletteIndex < 0 || paletteIndex >= palettes.length) {
      throw new IllegalArgumentException("Unknown TIM palette " + paletteIndex + " for " + name);
    }
    final var palette = palettes[paletteIndex];
    final int[] colours = palette.getData();
    for(int i = 0; i < colours.length; i++) {
      if(colours[i] != 0) {
        colours[i] |= 0xff000000;
      }
    }
    return VramTextureLoader.textureFromTim(tim).createOpenglTexture(name, palette);
  }
}
