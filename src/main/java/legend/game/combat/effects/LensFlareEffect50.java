package legend.game.combat.effects;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment_8002.rand;

public class LensFlareEffect50 implements Effect<EffectManagerParams.VoidType> {
  // /** Set to 5 and never used */
  // public int _00;
  // /** Initialized to 0, incremented each render call, never used */
  // public int _02;
  public final int[] u_04 = new int[5];
  public final int[] v_0e = new int[5];
  public final int[] w_18 = new int[5];
  public final int[] h_22 = new int[5];
  public final int[] clut_2c = new int[5];

  public final LensFlareEffectInstance3c[] instances_38 = new LensFlareEffectInstance3c[5];
  public int bentIndex_3c;
  public short x_40;
  public short y_42;
  public short z_44;

  public short brightness_48;
  public short shouldRender_4a;

  private final IntList visibleFlareIndices = new IntArrayList();
  private final MV transforms = new MV();
  private PolyBuilder builder;
  private Obj obj;

  public LensFlareEffect50() {
    Arrays.setAll(this.instances_38, i -> new LensFlareEffectInstance3c());
  }

  @Override
  @Method(0x8010c69cL)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    this.shouldRender_4a = (short)(rand() % 30);

    if(this.shouldRender_4a != 0) {
      final Vector2f screenCoords = SCRIPTS.getObject(this.bentIndex_3c, BattleEntity27c.class).transformRelative(this.x_40, this.y_42, this.z_44);
      final float x = -(screenCoords.x * 2.5f);
      final float y = -(screenCoords.y * 2.5f);

      //LAB_8010c7c0
      for(int i = 0; i < 5; i++) {
        final LensFlareEffectInstance3c inst = this.instances_38[i];
        final int dispW = displayWidth_1f8003e0;
        final int dispH = displayHeight_1f8003e4;
        inst.x_04 = screenCoords.x + dispW / 2.0f;
        inst.y_06 = screenCoords.y + dispH / 2.0f;

        if(inst.x_04 > 0 && inst.x_04 < dispW && inst.y_06 > 0 && inst.y_06 < dispH) {
          inst.onScreen_03 = true;

          final int scale = lensFlareGlowScales_800fb8fc[i];
          inst.x_04 += x * scale / 0x100;
          inst.y_06 += y * scale / 0x100;
        } else {
          //LAB_8010c870
          inst.onScreen_03 = false;
        }
        //LAB_8010c874
      }

      // Adjust brightness based on X position
      float screenX = Math.abs(screenCoords.x);
      final int screenWidth = displayWidth_1f8003e0 / 2;
      if(screenX > screenWidth) {
        screenX = screenWidth;
      }

      //LAB_8010c8b8
      this.brightness_48 = (short)(255.0f - 255.0f / screenWidth * screenX);
    }
    //LAB_8010c8e0
  }

  @Override
  @Method(0x8010c8f8L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    if(this.shouldRender_4a != 0) {
      final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

      final int flags = manager.params_10.flags_00;

      //LAB_8010c9fc
      this.builder = new PolyBuilder("Lens flare").bpp(Bpp.BITS_4);
      for(int i = 0; i < 5; i++) {
        final LensFlareEffectInstance3c inst = this.instances_38[i];

        if(inst.enabled_02 && inst.onScreen_03) {
          this.visibleFlareIndices.add(i);

          final int w = this.w_18[i];
          final int h = this.h_22[i];
          final int tpage = (this.v_0e[i] & 0x100) >>> 4 | (this.u_04[i] & 0x3ff) >>> 6;
          final int u = (this.u_04[i] & 0x3f) * 4;
          final int v = this.v_0e[i] & 0xff;
          final int clutX = this.clut_2c[i] << 4 & 0x3ff;
          final int clutY = this.clut_2c[i] >>> 6 & 0x1ff;
          final int r = manager.params_10.colour_1c.x * this.brightness_48 >> 8;
          final int g = manager.params_10.colour_1c.y * this.brightness_48 >> 8;
          final int b = manager.params_10.colour_1c.z * this.brightness_48 >> 8;

          if(i == 0) {
            // This flare is a textured gradient circle rendered in 4 quadrants
            for(int j = 0; j < 4; j++) {
              final float x = (inst.widthScale_2e * w) * lensFlareTranslationMagnitudeFactors_800fb910[j][0];
              final float y = (inst.heightScale_30 * h) * lensFlareTranslationMagnitudeFactors_800fb910[j][1];
              final int halfW = displayWidth_1f8003e0 / 2;
              final int halfH = displayHeight_1f8003e4 / 2;
              final float[][] flareVertexPositions = new float[4][2];
              flareVertexPositions[0][0] = inst.x_04 - halfW + x;
              flareVertexPositions[0][1] = inst.y_06 - halfH + y;
              flareVertexPositions[1][0] = inst.x_04 - halfW + x + (w * inst.widthScale_2e);
              flareVertexPositions[1][1] = inst.y_06 - halfH + y;
              flareVertexPositions[2][0] = inst.x_04 - halfW + x;
              flareVertexPositions[2][1] = inst.y_06 - halfH + y + (h * inst.heightScale_30);
              flareVertexPositions[3][0] = inst.x_04 - halfW + x + (w * inst.widthScale_2e);
              flareVertexPositions[3][1] = inst.y_06 - halfH + y + (h * inst.heightScale_30);

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.BITS_4)
                .clut(clutX, clutY)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .rgb(r, g, b)
                .pos(0, flareVertexPositions[lensFlareVertexIndices_800fb930[j][0]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][0]][1])
                .pos(1, flareVertexPositions[lensFlareVertexIndices_800fb930[j][1]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][1]][1])
                .pos(2, flareVertexPositions[lensFlareVertexIndices_800fb930[j][2]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][2]][1])
                .pos(3, flareVertexPositions[lensFlareVertexIndices_800fb930[j][3]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][3]][1])
                .uv(0, u, v)
                .uv(1, u + w - 1, v)
                .uv(2, u, v + h - 1)
                .uv(3, u + w - 1, v + h - 1);

              this.builder
                .addVertex(flareVertexPositions[lensFlareVertexIndices_800fb930[j][0]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][0]][1], 0)
                .clut(clutX, clutY)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .uv(u, v)
                .rgb(r / 255.0f, g / 255.0f, b /  255.0f)
                .addVertex(flareVertexPositions[lensFlareVertexIndices_800fb930[j][1]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][1]][1], 0)
                .uv(u + w - 1, v)
                .addVertex(flareVertexPositions[lensFlareVertexIndices_800fb930[j][2]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][2]][1], 0)
                .uv(u, v + h - 1)
                .addVertex(flareVertexPositions[lensFlareVertexIndices_800fb930[j][2]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][2]][1], 0)
                .uv(u, v + h - 1)
                .addVertex(flareVertexPositions[lensFlareVertexIndices_800fb930[j][1]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][1]][1], 0)
                .uv(u + w - 1, v)
                .addVertex(flareVertexPositions[lensFlareVertexIndices_800fb930[j][3]][0], flareVertexPositions[lensFlareVertexIndices_800fb930[j][3]][1], 0)
                .uv(u + w - 1, v + h - 1);

              if((flags >>> 30 & 1) != 0) {
                cmd.translucent(Translucency.of(flags >>> 28 & 0b11));
              }

              GPU.queueCommand(30, cmd);
            }
          } else {
            //LAB_8010ceec
            final int halfW = displayWidth_1f8003e0 / 2;
            final int halfH = displayHeight_1f8003e4 / 2;
            final float x = inst.x_04 - halfW - (inst.widthScale_2e * w) / 2.0f;
            final float y = inst.y_06 - halfH - (inst.heightScale_30 * h) / 2.0f;
            final float w2 = w * inst.widthScale_2e;
            final float h2 = h * inst.heightScale_30;

            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.BITS_4)
              .clut(clutX, clutY)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .rgb(r, g, b)
              .pos(0, x, y)
              .pos(1, x + w2, y)
              .pos(2, x, y + h2)
              .pos(3, x + w2, y + h2)
              .uv(0, u, v)
              .uv(1, u + w - 1, v)
              .uv(2, u, v + h - 1)
              .uv(3, u + w - 1, v + h - 1);

            this.builder
              .addVertex(x, y, 0)
              .clut(clutX, clutY)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .uv(u, v)
              .rgb(r / 255.0f, g / 255.0f, b /  255.0f)
              .addVertex(x + w2, y, 0)
              .uv(u + w - 1, v)
              .addVertex(x, y + h2, 0)
              .uv(u, v + h - 1)
              .addVertex(x, y + h2, 0)
              .uv(u, v + h - 1)
              .addVertex(x + w2, y, 0)
              .uv(u + w - 1, v)
              .addVertex(x + w2, y + h2, 0)
              .uv(u + w - 1, v + h - 1);

            if((flags >>> 30 & 1) != 0) {
              cmd.translucent(Translucency.of(flags >>> 28 & 0b11));
            }

            GPU.queueCommand(30, cmd);
          }
        }
        //LAB_8010d198
        //LAB_8010d19c
      }

      if((flags >>> 30 & 1) != 0 && this.builder != null) {
        this.builder.translucency(Translucency.of(flags >>> 28 & 0b11));
      }

      this.renderPolyObj();
    }
    //LAB_8010d1ac
  }

  private void renderPolyObj() {
    if(this.builder != null) {
      this.obj = this.builder.build();

      int vertexIndexOffset = 0;
      for(int i = 0; i < this.visibleFlareIndices.size(); i++) {
        final int flareIndex = this.visibleFlareIndices.getInt(i);
        this.transforms.identity();
        this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), 120.0f);
        if(flareIndex == 0) {
          vertexIndexOffset = 18;
          RENDERER.queueOrthoModel(this.obj, this.transforms, QueuedModelStandard.class)
            .vertices(i * 24, 24);
        } else {
          RENDERER.queueOrthoModel(this.obj, this.transforms, QueuedModelStandard.class)
            .vertices(vertexIndexOffset + i * 6, 6);
        }
      }

      this.obj.delete();
      this.obj = null;
      this.builder = null;
      this.visibleFlareIndices.clear();
    }
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }
  }

  private static final int[] lensFlareGlowScales_800fb8fc = {0xf0, 0xa0, 0x60, 0x30, 0x10};
  private static final int[][] lensFlareTranslationMagnitudeFactors_800fb910 = {{-1, -1}, {0, -1}, {-1, 0}, {0, 0}};
  private static final int[][] lensFlareVertexIndices_800fb930 = {{3, 2, 1, 0}, {2, 3, 0, 1}, {1, 0, 3, 2}, {0, 1, 2, 3}};
}
