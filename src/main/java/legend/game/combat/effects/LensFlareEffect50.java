package legend.game.combat.effects;

import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment_8002.rand;

public class LensFlareEffect50 implements Effect<EffectManagerParams.VoidType> {
  public int _00;
  public int _02;
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

      this._02++;
      final int sp10 = manager.params_10.flags_00;

      //LAB_8010c9fc
      for(int i = 0; i < 5; i++) {
        final LensFlareEffectInstance3c inst = this.instances_38[i];

        if(inst.enabled_02 && inst.onScreen_03) {
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
            for(int j = 0; j < 4; j++) {
              final int x = (inst.widthScale_2e * w >> 12) * lensFlareTranslationMagnitudeFactors_800fb910[j][0];
              final int y = (inst.heightScale_30 * h >> 12) * lensFlareTranslationMagnitudeFactors_800fb910[j][1];
              final int halfW = displayWidth_1f8003e0 / 2;
              final int halfH = displayHeight_1f8003e4 / 2;
              final float[][] sp0x48 = new float[4][2];
              sp0x48[0][0] = inst.x_04 - halfW + x;
              sp0x48[0][1] = inst.y_06 - halfH + y;
              sp0x48[1][0] = inst.x_04 - halfW + x + (w * inst.widthScale_2e >> 12);
              sp0x48[1][1] = inst.y_06 - halfH + y;
              sp0x48[2][0] = inst.x_04 - halfW + x;
              sp0x48[2][1] = inst.y_06 - halfH + y + (h * inst.heightScale_30 >> 12);
              sp0x48[3][0] = inst.x_04 - halfW + x + (w * inst.widthScale_2e >> 12);
              sp0x48[3][1] = inst.y_06 - halfH + y + (h * inst.heightScale_30 >> 12);

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.BITS_4)
                .clut(clutX, clutY)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .rgb(r, g, b)
                .pos(0, sp0x48[lensFlareVertexIndices_800fb930[j][0]][0], sp0x48[lensFlareVertexIndices_800fb930[j][0]][1])
                .pos(1, sp0x48[lensFlareVertexIndices_800fb930[j][1]][0], sp0x48[lensFlareVertexIndices_800fb930[j][1]][1])
                .pos(2, sp0x48[lensFlareVertexIndices_800fb930[j][2]][0], sp0x48[lensFlareVertexIndices_800fb930[j][2]][1])
                .pos(3, sp0x48[lensFlareVertexIndices_800fb930[j][3]][0], sp0x48[lensFlareVertexIndices_800fb930[j][3]][1])
                .uv(0, u, v)
                .uv(1, u + w - 1, v)
                .uv(2, u, v + h - 1)
                .uv(3, u + w - 1, v + h - 1);

              if((sp10 >>> 30 & 1) != 0) {
                cmd.translucent(Translucency.of(sp10 >>> 28 & 0b11));
              }

              GPU.queueCommand(30, cmd);
            }
          } else {
            //LAB_8010ceec
            final int halfW = displayWidth_1f8003e0 / 2;
            final int halfH = displayHeight_1f8003e4 / 2;
            final float x = inst.x_04 - halfW - (inst.widthScale_2e * w >> 12) / 2.0f;
            final float y = inst.y_06 - halfH - (inst.heightScale_30 * h >> 12) / 2.0f;
            final int w2 = w * inst.widthScale_2e >> 12;
            final int h2 = h * inst.heightScale_30 >> 12;

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

            if((sp10 >>> 30 & 1) != 0) {
              cmd.translucent(Translucency.of(sp10 >>> 28 & 0b11));
            }

            GPU.queueCommand(30, cmd);
          }
        }

        //LAB_8010d198
        //LAB_8010d19c
      }
    }

    //LAB_8010d1ac
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  private static final int[] lensFlareGlowScales_800fb8fc = {0xf0, 0xa0, 0x60, 0x30, 0x10};
  private static final int[][] lensFlareTranslationMagnitudeFactors_800fb910 = {{-1, -1}, {0, -1}, {-1, 0}, {0, 0}};
  private static final int[][] lensFlareVertexIndices_800fb930 = {{3, 2, 1, 0}, {2, 3, 0, 1}, {1, 0, 3, 2}, {0, 1, 2, 3}};
}
