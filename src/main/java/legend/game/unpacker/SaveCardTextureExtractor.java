package legend.game.unpacker;

import legend.core.gpu.Rect4i;
import legend.core.gpu.VramTextureLoader;
import legend.core.gpu.VramTextureSingle;
import legend.game.textures.PngWriter;
import legend.game.tim.Tim;
import legend.lodmod.LodGoods;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Set;

import static legend.lodmod.LodCharacterTemplates.ALBERT;
import static legend.lodmod.LodCharacterTemplates.DART;
import static legend.lodmod.LodCharacterTemplates.HASCHEL;
import static legend.lodmod.LodCharacterTemplates.KONGOL;
import static legend.lodmod.LodCharacterTemplates.LAVITZ;
import static legend.lodmod.LodCharacterTemplates.MERU;
import static legend.lodmod.LodCharacterTemplates.MIRANDA;
import static legend.lodmod.LodCharacterTemplates.ROSE;
import static legend.lodmod.LodCharacterTemplates.SHANA;

public final class SaveCardTextureExtractor {
  private SaveCardTextureExtractor() { }

  private static final String[] characterNames = {
    DART.getId().entryId(),
    LAVITZ.getId().entryId(),
    SHANA.getId().entryId(),
    ROSE.getId().entryId(),
    HASCHEL.getId().entryId(),
    ALBERT.getId().entryId(),
    MERU.getId().entryId(),
    KONGOL.getId().entryId(),
    MIRANDA.getId().entryId(),
  };

  private static final String[] dragoonSpiritNames = {
    LodGoods.RED_DRAGOON_SPIRIT.getId().entryId(),
    LodGoods.JADE_DRAGOON_SPIRIT.getId().entryId(),
    LodGoods.SILVER_DRAGOON_SPIRIT.getId().entryId(),
    LodGoods.DARK_DRAGOON_SPIRIT.getId().entryId(),
    LodGoods.VIOLET_DRAGOON_SPIRIT.getId().entryId(),
    LodGoods.BLUE_DRAGOON_SPIRIT.getId().entryId(),
    LodGoods.GOLD_DRAGOON_SPIRIT.getId().entryId(),
    LodGoods.DIVINE_DRAGOON_SPIRIT.getId().entryId(),
  };

  public static boolean portraitExtractorDiscriminator(final PathNode node, final Set<String> flags) {
    return "SECT/DRGN0.BIN/6665".equals(node.fullPath) && !flags.contains(node.fullPath);
  }

  public static void portraitExtractorTransformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add(node.fullPath);

    final Tim firstTim = new Tim(node.data);

    // Walk to the last TIM in the file
    Tim thirdTim = null;
    int offset = 0;
    for(int i = 0; i < 3; i++) {
      thirdTim = new Tim(node.data.slice(offset));
      final Rect4i rect = thirdTim.getImageRect();
      offset += 0x14 + thirdTim.getImageDataOffset() + rect.w * rect.h * 2;
    }

    final int[] newData = new int[512 * 64];
    extractPortraits(thirdTim, newData);
    extractSpirits(firstTim, newData);

    transformations.addNode(node);

    for(int i = 0; i < characterNames.length; i++) {
      transformations.addNode("characters/%s/portrait.png".formatted(characterNames[i]), createPng(newData, new Rect4i(i * 48, 0, 48, 48), 512));
    }

    for(int i = 0; i < dragoonSpiritNames.length; i++) {
      transformations.addNode("goods/%s/icon.png".formatted(dragoonSpiritNames[i]), createPng(newData, new Rect4i(i * 16, 48, 16, 16), 512));
    }

    transformations.addNode("retail_atlas.png", createPng(newData, new Rect4i(0, 0, 512, 64), 512));
  }

  private static FileData createPng(final int[] uncompressed, final Rect4i region, final int stride) {
    final ByteBuffer buffer = BufferUtils.createByteBuffer(region.w * region.h * 4);
    final IntBuffer ints = buffer.order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();

    for(int y = 0; y < region.h; y++) {
      final int dataY = region.y + y;
      final int dataIndex = dataY * stride + region.x;
      ints.put(y * region.w, uncompressed, dataIndex, region.w);
    }

    return new FileData(PngWriter.compress(buffer, region.w, region.h));
  }

  private static void extractPortraits(final Tim tim, final int[] newData) {
    final VramTextureSingle texture = VramTextureLoader.textureFromTim(tim);
    final VramTextureSingle[] palettes = VramTextureLoader.palettesFromTim(tim);

    // Apply all 3 CLUTs
    final int[][] applied = new int[3][];
    for(int i = 0; i < 3; i++) {
      applied[i] = texture.applyPalette(palettes[i], new Rect4i(0, 0, texture.rect.w, texture.rect.h));
    }

    // Applied is the TIM with its CLUTs applied
    // Raw is an intermediate abstraction, as if the texture was one single row with each portrait in order, repeated 3 times (once for each colour channel)
    // New is the output data

    final int rawWidth = 432 * 3;
    final int rawHeight = 48;
    final int rawArea = rawWidth * rawHeight;
    final int appliedWidth = 256;

    for(int i = 0; i < rawArea; i++) {
      final int rawX = i % rawWidth;
      final int rawY = i / rawWidth;

      int appliedX = rawX % appliedWidth;
      int appliedY = rawY + rawX / appliedWidth * rawHeight;

      // Handle the lower 32 pixels of the right side of Miranda's blue channel being split up in the last row
      while(appliedY >= 256) {
        appliedY -= 16;
        appliedX += 16;
      }

      final int channel = i / (rawWidth / 3) % 3;
      final int appliedIndex = appliedY * appliedWidth + appliedX;
      final int pixel = applied[channel][appliedIndex];
      final int newIndex = i / rawWidth * 512 + (i % (rawWidth / 3));

      newData[newIndex] |= pixel & 0xffffff;
    }

    for(int i = 0; i < newData.length; i++) {
      if((newData[i] & 0xff) != 0/* && (newData[i] & 0xff00) != 0 && (newData[i] & 0xff0000) != 0*/) {
        newData[i] |= 0xff000000;
      } else {
        newData[i] = 0;
      }
    }
  }

  public static void extractSpirits(final Tim tim, final int[] newData) {
    final VramTextureSingle texture = VramTextureLoader.textureFromTim(tim);
    final VramTextureSingle[] palettes = VramTextureLoader.palettesFromTim(tim);
    final int[][] applied = new int[8][];

    for(int spirit = 0; spirit < 7; spirit++) {
      applied[spirit] = texture.applyPalette(palettes[4 + spirit], new Rect4i(130, 118, 11, 9));
    }

    applied[7] = texture.applyPalette(palettes[11], new Rect4i(242, 118, 11, 9));

    for(int spirit = 0; spirit < 8; spirit++) {
      for(int y = 0; y < 9; y++) {
        for(int x = 0; x < 11; x++) {
          final int pixel = applied[spirit][y * 11 + x];

          if(pixel != 0) {
            final int r = pixel        & 0xff;
            final int g = pixel >>>  8 & 0xff;
            final int b = pixel >>> 16 & 0xff;
            newData[(48 + y) * 512 + spirit * 16 + x] = 0xff00_0000 | b << 16 | g << 8 | r;
          }
        }
      }
    }
  }
}
