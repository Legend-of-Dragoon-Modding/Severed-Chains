package legend.game.submap;

import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.memory.Method;
import legend.game.tim.Tim;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.joml.Vector2f;

import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;

public class ChapterTitleCard {
  private enum ChapterTitleState {
    LOAD,
    WAIT,
    RENDER,
    COMPLETE
  }
  private final int[] chapterCardDirs = {6670, 6671, 6672, 6673};

  private ChapterTitleState chapterTitleState_800c6708;
  private int chapterTitleAnimationTicksRemaining_800c670a;
  private final Vector2f chapterTitleDropShadowOffset_800c670c = new Vector2f();
  private List<FileData> chapterTitleCardMrg_800c6710;
  private final Vector2f chapterTitleNumberOffset_800c6714 = new Vector2f();
  private final Vector2f chapterTitleNameOffset_800c671c = new Vector2f();
  /** Inverted condition from retail */
  private boolean chapterTitleIsTranslucent_800c6724;
  private float chapterTitleBrightness_800c6728;

  /**
   * Lower 4 bits are chapter title num (starting at 1), used for displaying chapter title cards
   * 0x80 bit indicates that the origin XY of the title card have been set and the animation is ready to start rendering
   */
  private int chapterTitleNum_800c6738;

  private int chapterTitleAnimationPauseTicksRemaining_800c673c;

  private boolean chapterTitleAnimationComplete_800c686e;

  private final Vector2f chapterTitleOrigin_800c687c = new Vector2f();

  private boolean chapterTitleCardLoaded_800c68e0;

  public ChapterTitleCard() {
    this.chapterTitleState_800c6708 = ChapterTitleState.LOAD;
    this.chapterTitleAnimationTicksRemaining_800c670a = 0;
    this.chapterTitleCardMrg_800c6710 = null;
    this.chapterTitleNum_800c6738 = 0;
    this.chapterTitleAnimationPauseTicksRemaining_800c673c = 60;
    this.chapterTitleAnimationComplete_800c686e = false;
    this.chapterTitleCardLoaded_800c68e0 = false;
  }

  public boolean isMrgNull() {
    return this.chapterTitleCardMrg_800c6710 == null;
  }

  public void setMrgNull() {
    this.chapterTitleCardMrg_800c6710 = null;
  }

  public int isChapterTitleCardLoaded() {
    return this.chapterTitleCardLoaded_800c68e0 ? 1 : 0;
  }

  public void setChapterTitleCardNotLoaded() {
    this.chapterTitleCardLoaded_800c68e0 = false;
  }

  public int isChapterTitleAnimationComplete() {
    return this.chapterTitleAnimationComplete_800c686e ? 1 : 0;
  }

  public void setChapterTitleAnimationNotComplete() {
    this.chapterTitleAnimationComplete_800c686e = false;
  }

  public int getChapterNum() {
    return this.chapterTitleNum_800c6738;
  }

  public void setChapterNum(final int num) {
    this.chapterTitleNum_800c6738 = num;
  }

  public void setChapterTitleOrigin(final int x, final int y) {
    this.chapterTitleOrigin_800c687c.set(x, y);
  }

  public void setchapterTitleAnimationPauseTicksRemaining(final int ticks) {
    this.chapterTitleAnimationPauseTicksRemaining_800c673c = ticks;
  }

  @Method(0x800e2648L)
  public void handleAndRenderChapterTitle() {
    if(this.chapterTitleNum_800c6738 == 0){
      return;
    }

    switch(this.chapterTitleState_800c6708) {
      case LOAD:
        //LAB_800e26c0
        loadDrgnDir(0, this.chapterCardDirs[this.chapterTitleNum_800c6738 - 1], files -> this.submapAssetsLoadedCallback(files, 0x10));

        //LAB_800e27a4
        this.chapterTitleState_800c6708 = ChapterTitleState.WAIT;
        break;

      case WAIT:
        //LAB_800e27b8
        if(this.chapterTitleCardLoaded_800c68e0 && (this.chapterTitleNum_800c6738 & 0x80) != 0) {
          this.chapterTitleState_800c6708 = ChapterTitleState.RENDER;
        }

        break;

      case RENDER:
        //LAB_800e27e8
        final int currentTick = this.chapterTitleAnimationTicksRemaining_800c670a;

        //LAB_800e284c
        if(currentTick == 0) {
          //LAB_800e2860
          new Tim(this.chapterTitleCardMrg_800c6710.get(5)).uploadToGpu();
          new Tim(this.chapterTitleCardMrg_800c6710.get(13)).uploadToGpu();

          //LAB_800e2980
          this.chapterTitleBrightness_800c6728 = 0.0f;
          this.chapterTitleIsTranslucent_800c6724 = true;
          this.chapterTitleNumberOffset_800c6714.set(32.0f, 16.0f);
          this.chapterTitleNameOffset_800c671c.set(64.0f, 16.0f);
          this.chapterTitleAnimationTicksRemaining_800c670a++;
        } else if(currentTick == 33 * (3 - vsyncMode_8007a3b8)) {
          //LAB_800e3070
          this.chapterTitleIsTranslucent_800c6724 = false;
          this.chapterTitleNameOffset_800c671c.set(0.0f, 0.0f);
          this.chapterTitleNumberOffset_800c6714.set(0.0f, 0.0f);
          this.chapterTitleBrightness_800c6728 = 0.5f;
          this.chapterTitleAnimationTicksRemaining_800c670a++;
          this.chapterTitleDropShadowOffset_800c670c.set(1.0f, 0.0f);
        } else if(currentTick == 34 * (3 - vsyncMode_8007a3b8)) {
          //LAB_800e30c0
          this.chapterTitleDropShadowOffset_800c670c.x = this.chapterTitleDropShadowOffset_800c670c.x + 1.0f / (3 - vsyncMode_8007a3b8);

          if(this.chapterTitleDropShadowOffset_800c670c.x >= 3) {
            this.chapterTitleDropShadowOffset_800c670c.y = 1.0f;
            this.chapterTitleAnimationTicksRemaining_800c670a++;
          }
        } else if(currentTick == 35 * (3 - vsyncMode_8007a3b8)) {
          //LAB_800e30f8
          this.chapterTitleAnimationPauseTicksRemaining_800c673c--;

          if(this.chapterTitleAnimationPauseTicksRemaining_800c673c == 0) {
            this.chapterTitleAnimationTicksRemaining_800c670a = 201;
          }
        } else if(currentTick == 233 * (3 - vsyncMode_8007a3b8)) {
          //LAB_800e376c
          this.chapterTitleCardMrg_800c6710 = null;
          this.chapterTitleState_800c6708 = ChapterTitleState.COMPLETE;
        } else if(currentTick > 35 * (3 - vsyncMode_8007a3b8)) {
          //LAB_800e2828
          if(currentTick < 233 * (3 - vsyncMode_8007a3b8)) {
            if(currentTick >= 201 * (3 - vsyncMode_8007a3b8)) {
              //LAB_800e311c
              if(currentTick == 212 * (3 - vsyncMode_8007a3b8)) {
                new Tim(this.chapterTitleCardMrg_800c6710.get(1)).uploadToGpu();
                new Tim(this.chapterTitleCardMrg_800c6710.get(9)).uploadToGpu();

                //LAB_800e3248
                //LAB_800e3254
              } else if(currentTick == 216 * (3 - vsyncMode_8007a3b8)) {
                new Tim(this.chapterTitleCardMrg_800c6710.get(2)).uploadToGpu();
                new Tim(this.chapterTitleCardMrg_800c6710.get(10)).uploadToGpu();

                //LAB_800e3384
                //LAB_800e3390
              } else if(currentTick == 220 * (3 - vsyncMode_8007a3b8)) {
                new Tim(this.chapterTitleCardMrg_800c6710.get(3)).uploadToGpu();
                new Tim(this.chapterTitleCardMrg_800c6710.get(11)).uploadToGpu();

                //LAB_800e34c0
                //LAB_800e34cc
              } else if(currentTick == 224 * (3 - vsyncMode_8007a3b8)) {
                new Tim(this.chapterTitleCardMrg_800c6710.get(4)).uploadToGpu();
                new Tim(this.chapterTitleCardMrg_800c6710.get(12)).uploadToGpu();

                //LAB_800e35fc
                //LAB_800e3608
              } else if(currentTick == 228 * (3 - vsyncMode_8007a3b8)) {
                new Tim(this.chapterTitleCardMrg_800c6710.get(5)).uploadToGpu();
                new Tim(this.chapterTitleCardMrg_800c6710.get(13)).uploadToGpu();
              }

              //LAB_800e3744
              this.chapterTitleIsTranslucent_800c6724 = true;
              this.chapterTitleBrightness_800c6728 = this.chapterTitleBrightness_800c6728 - 4.0f / 255.0f / (3 - vsyncMode_8007a3b8);
            }

            //LAB_800e3790
            this.chapterTitleAnimationTicksRemaining_800c670a++;
          }
        } else if(currentTick > 0) {
          //LAB_800e29d4
          if(currentTick == 4 * (3 - vsyncMode_8007a3b8)) {
            new Tim(this.chapterTitleCardMrg_800c6710.get(4)).uploadToGpu();
            new Tim(this.chapterTitleCardMrg_800c6710.get(12)).uploadToGpu();

            //LAB_800e2afc
            //LAB_800e2b08
          } else if(currentTick == 8 * (3 - vsyncMode_8007a3b8)) {
            new Tim(this.chapterTitleCardMrg_800c6710.get(3)).uploadToGpu();
            new Tim(this.chapterTitleCardMrg_800c6710.get(11)).uploadToGpu();

            //LAB_800e2c38
            //LAB_800e2c44
          } else if(currentTick == 12 * (3 - vsyncMode_8007a3b8)) {
            new Tim(this.chapterTitleCardMrg_800c6710.get(2)).uploadToGpu();
            new Tim(this.chapterTitleCardMrg_800c6710.get(10)).uploadToGpu();

            //LAB_800e2d74
            //LAB_800e2d80
          } else if(currentTick == 16 * (3 - vsyncMode_8007a3b8)) {
            new Tim(this.chapterTitleCardMrg_800c6710.get(1)).uploadToGpu();
            new Tim(this.chapterTitleCardMrg_800c6710.get(9)).uploadToGpu();

            //LAB_800e2eb0
            //LAB_800e2ebc
          } else if(currentTick == 20 * (3 - vsyncMode_8007a3b8)) {
            new Tim(this.chapterTitleCardMrg_800c6710.get(0)).uploadToGpu();
            new Tim(this.chapterTitleCardMrg_800c6710.get(8)).uploadToGpu();

            //LAB_800e2fec
          }

          //LAB_800e2ff8
          this.chapterTitleBrightness_800c6728 = this.chapterTitleBrightness_800c6728 + 4.0f / 255.0f / (3 - vsyncMode_8007a3b8);
          this.chapterTitleNumberOffset_800c6714.x = this.chapterTitleNumberOffset_800c6714.x - 1.0f / (3 - vsyncMode_8007a3b8);
          this.chapterTitleNameOffset_800c671c.x = this.chapterTitleNameOffset_800c671c.x - 2.0f / (3 - vsyncMode_8007a3b8);

          // Decrement Y-offset every other tick
          if((this.chapterTitleAnimationTicksRemaining_800c670a & 0x1) == 0) {
            this.chapterTitleNumberOffset_800c6714.y = this.chapterTitleNumberOffset_800c6714.y - 1.0f / (3 - vsyncMode_8007a3b8);
            this.chapterTitleNameOffset_800c671c.y = this.chapterTitleNameOffset_800c671c.y - 1.0f / (3 - vsyncMode_8007a3b8);
          }

          //LAB_800e3038
          //LAB_800e3064
          this.chapterTitleAnimationTicksRemaining_800c670a++;
        } else {
          //LAB_800e3790
          this.chapterTitleAnimationTicksRemaining_800c670a++;
        }

        //LAB_800e37a0
        float left = this.chapterTitleOrigin_800c687c.x + this.chapterTitleNumberOffset_800c6714.x - 58;
        float top = this.chapterTitleOrigin_800c687c.y + this.chapterTitleNumberOffset_800c6714.y - 66;
        float right = this.chapterTitleOrigin_800c687c.x - (this.chapterTitleNumberOffset_800c6714.x - 34);
        float bottom = this.chapterTitleOrigin_800c687c.y - (this.chapterTitleNumberOffset_800c6714.y + 30);

        // Chapter number text
        final GpuCommandPoly cmd1 = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .monochrome(this.chapterTitleBrightness_800c6728)
          .clut(512, 510)
          .vramPos(512, 256)
          .uv(0, 0, 64)
          .uv(1, 91, 64)
          .uv(2, 0, 99)
          .uv(3, 91, 99)
          .pos(0, left, top)
          .pos(1, right, top)
          .pos(2, left, bottom)
          .pos(3, right, bottom);

        if(this.chapterTitleIsTranslucent_800c6724) {
          cmd1.translucent(Translucency.B_PLUS_F);
        }

        GPU.queueCommand(28, cmd1);

        left = this.chapterTitleOrigin_800c687c.x - (this.chapterTitleNameOffset_800c671c.x + 140);
        top = this.chapterTitleOrigin_800c687c.y - (this.chapterTitleNameOffset_800c671c.y + 16);
        right = this.chapterTitleOrigin_800c687c.x + this.chapterTitleNameOffset_800c671c.x + 116;
        bottom = this.chapterTitleOrigin_800c687c.y + this.chapterTitleNameOffset_800c671c.y + 45;

        // Chapter name text
        final GpuCommandPoly cmd2 = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .monochrome(this.chapterTitleBrightness_800c6728)
          .clut(512, 508)
          .vramPos(512, 256)
          .uv(0, 0, 0)
          .uv(1, 255, 0)
          .uv(2, 0, 60)
          .uv(3, 255, 60)
          .pos(0, left, top)
          .pos(1, right, top)
          .pos(2, left, bottom)
          .pos(3, right, bottom);

        if(this.chapterTitleIsTranslucent_800c6724) {
          cmd2.translucent(Translucency.B_PLUS_F);
        }

        GPU.queueCommand(28, cmd2);

        if(this.chapterTitleDropShadowOffset_800c670c.x != 0) {
          left = this.chapterTitleDropShadowOffset_800c670c.x + this.chapterTitleOrigin_800c687c.x + this.chapterTitleNumberOffset_800c6714.x - 58;
          top = this.chapterTitleDropShadowOffset_800c670c.y + this.chapterTitleOrigin_800c687c.y + this.chapterTitleNumberOffset_800c6714.y - 66;
          right = this.chapterTitleDropShadowOffset_800c670c.x + this.chapterTitleOrigin_800c687c.x - (this.chapterTitleNumberOffset_800c6714.x - 34);
          bottom = this.chapterTitleDropShadowOffset_800c670c.y + this.chapterTitleOrigin_800c687c.y - (this.chapterTitleNumberOffset_800c6714.y + 30);

          // Chapter number drop shadow
          final GpuCommandPoly cmd3 = new GpuCommandPoly(4)
            .bpp(Bpp.BITS_4)
            .translucent(Translucency.HALF_B_PLUS_HALF_F)
            .monochrome(this.chapterTitleBrightness_800c6728)
            .clut(512, 511)
            .vramPos(512, 256)
            .uv(0, 0, 64)
            .uv(1, 91, 64)
            .uv(2, 0, 99)
            .uv(3, 91, 99)
            .pos(0, left, top)
            .pos(1, right, top)
            .pos(2, left, bottom)
            .pos(3, right, bottom);

          if((this.chapterTitleNum_800c6738 & 0xf) - 2 < 3) {
            cmd3.translucent(Translucency.B_MINUS_F);
          }

          //LAB_800e3afc
          if((this.chapterTitleNum_800c6738 & 0xf) == 1) {
            cmd3.translucent(Translucency.B_PLUS_F);
          }

          //LAB_800e3b14
          GPU.queueCommand(28, cmd3);

          left = this.chapterTitleDropShadowOffset_800c670c.x + this.chapterTitleOrigin_800c687c.x - (this.chapterTitleNameOffset_800c671c.x + 140);
          top = this.chapterTitleDropShadowOffset_800c670c.y + this.chapterTitleOrigin_800c687c.y - (this.chapterTitleNameOffset_800c671c.y + 16);
          right = this.chapterTitleDropShadowOffset_800c670c.x + this.chapterTitleOrigin_800c687c.x + this.chapterTitleNameOffset_800c671c.x + 116;
          bottom = this.chapterTitleDropShadowOffset_800c670c.y + this.chapterTitleOrigin_800c687c.y + this.chapterTitleNameOffset_800c671c.y + 45;

          // Chapter name drop shadow
          final GpuCommandPoly cmd4 = new GpuCommandPoly(4)
            .bpp(Bpp.BITS_4)
            .translucent(Translucency.HALF_B_PLUS_HALF_F)
            .monochrome(this.chapterTitleBrightness_800c6728)
            .clut(512, 509)
            .vramPos(512, 256)
            .uv(0, 0, 0)
            .uv(1, 255, 0)
            .uv(2, 0, 60)
            .uv(3, 255, 60)
            .pos(0, left, top)
            .pos(1, right, top)
            .pos(2, left, bottom)
            .pos(3, right, bottom);

          if((this.chapterTitleNum_800c6738 & 0xf) - 2 < 3) {
            cmd4.translucent(Translucency.B_MINUS_F);
          }

          //LAB_800e3c20
          if((this.chapterTitleNum_800c6738 & 0xf) == 1) {
            cmd4.translucent(Translucency.B_PLUS_F);
          }

          //LAB_800e3c3c
          GPU.queueCommand(28, cmd4);
        }

        break;

      //LAB_800e26a4
      case COMPLETE:
        //LAB_800e3c60
        this.chapterTitleNum_800c6738 = 0;
        this.chapterTitleOrigin_800c687c.set(0.0f, 0.0f);
        this.chapterTitleCardLoaded_800c68e0 = false;
        this.chapterTitleDropShadowOffset_800c670c.x = 0.0f;
        this.chapterTitleAnimationTicksRemaining_800c670a = 0;
        this.chapterTitleState_800c6708 = ChapterTitleState.LOAD;
        this.chapterTitleAnimationComplete_800c686e = true;
    }
  }

  @Method(0x800e3d80L)
  private void submapAssetsLoadedCallback(final List<FileData> files, final int assetType) {
    // Chapter title cards
    if(assetType == 0x10) {
      this.chapterTitleCardMrg_800c6710 = files;
      this.chapterTitleCardLoaded_800c68e0 = true;
    }
  }
}
