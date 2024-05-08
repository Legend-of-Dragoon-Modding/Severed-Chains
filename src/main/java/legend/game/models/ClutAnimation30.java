package legend.game.models;

import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.memory.Method;

import static legend.core.GameEngine.GPU;

/** 0x30 bytes long */
public class ClutAnimation30 implements TextureAnimation {
  public ClutAnimationFile tmdExt_00;
  public final boolean[] enabled_04 = new boolean[4];
  public final short[] sa_08 = new short[4];
  public final int[] sa_10 = new int[4];
  public final short[] destYOffset_18 = new short[4];
  public final TmdSubExtension[] tmdSubExtensionArr_20 = new TmdSubExtension[4];

  @Override
  public void apply(final Model124 model) {
    for(int i = 0; i < 4; i++) {
      if(this.enabled_04[i]) {
        this.animateSubmapModelClut(model, i);
      }
    }
  }

  /** (pulled from SMAP) Used in pre-Melbu submap cutscene, Prairie, new game Rose cutscene (animates the cloud flicker by changing CLUT, pretty sure this is CLUT animation) */
  @Method(0x800dde70L)
  private void animateSubmapModelClut(final Model124 model, final int index) {
    if(this.tmdSubExtensionArr_20[index] == null) {
      this.enabled_04[index] = false;
    } else {
      //LAB_800ddeac
      final int x = model.uvAdjustments_9d.clutX;
      final int y = model.uvAdjustments_9d.clutY;

      final TmdSubExtension v = this.tmdSubExtensionArr_20[index];
      int a1 = 0;

      //LAB_800ddef8
      for(int i = 0; i < this.sa_08[index]; i++) {
        a1 += 2;
      }

      //LAB_800ddf08
      final int sourceYOffset = v.sourceYOffset_04[a1];
      a1++;

      this.sa_10[index]++;

      if(this.sa_10[index] == v.sourceYOffset_04[a1]) {
        this.sa_10[index] = 0;

        if(v.sourceYOffset_04[a1 + 1] == -1) {
          this.sa_08[index] = 0;
        } else {
          //LAB_800ddf70
          this.sa_08[index]++;
        }
      }

      //LAB_800ddf8c
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + sourceYOffset, x, y + this.destYOffset_18[index], 16, 1));
    }
    //LAB_800ddff4
  }
}
