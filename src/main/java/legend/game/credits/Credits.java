package legend.game.credits;

import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.Rect4i;
import legend.core.memory.Method;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.tim.Tim;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.playXaAudio;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;

public class Credits extends EngineState {
  public enum CreditsType {
    MAJOR_HEADER_0,
    MINOR_HEADER_1,
    NAME_2,
    DIRECTOR_3,
    UNUSED_4, // May have been for "The End" originally
  }
  private List<FileData> creditTims_800d1ae0;
  private int fadeOutTicks_800d1ae4;
  private boolean creditTimsLoaded_800d1ae8;
  private int creditsPassed_800d1aec;
  private int creditIndex_800d1af0;

  private int loadingStage;

  private final CreditData1c[] credits_800d1af8 = new CreditData1c[16];
  {
    Arrays.setAll(this.credits_800d1af8, i -> new CreditData1c());
  }

  /**
   * <ol start="0">
   *   <li>{@link Credits#initCredits}</li>
   *   <li>{@link Credits#waitForCreditsToLoadAndPlaySong}</li>
   *   <li>{@link Credits#renderCredits}</li>
   *   <li>{@link Credits#waitForCreditsFadeOut}</li>
   *   <li>{@link Credits#deallocateCreditsAndTransitionToTheEndSubmap}</li>
   * </ol>
   */
  private final Runnable[] creditsStates_800f9378 = new Runnable[8];
  {
    this.creditsStates_800f9378[0] = this::initCredits;
    this.creditsStates_800f9378[1] = this::waitForCreditsToLoadAndPlaySong;
    this.creditsStates_800f9378[2] = this::renderCredits;
    this.creditsStates_800f9378[3] = this::waitForCreditsFadeOut;
    this.creditsStates_800f9378[4] = this::deallocateCreditsAndTransitionToTheEndSubmap;
  }

  private static final CreditHeader08[] creditHeaderEntries_800f93b0 = {
    new CreditHeader08(  0, CreditsType.DIRECTOR_3),
    new CreditHeader08(  1, CreditsType.DIRECTOR_3),
    new CreditHeader08(  2, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(  4, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(  6, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(  8, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 10, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 12, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 14, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 26, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08( 28, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 32, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 34, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08( 36, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08( 38, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 40, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 44, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08( 46, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 48, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 51, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 53, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 55, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 57, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 59, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 61, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 70, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08( 72, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 74, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 86, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 88, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 90, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 92, CreditsType.MINOR_HEADER_1),
    new CreditHeader08( 94, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08( 96, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(103, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(107, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(111, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(120, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(125, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(128, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(153, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(159, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(161, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(163, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(166, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(169, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(172, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(175, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(177, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(201, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(212, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(217, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(220, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(223, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(227, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(230, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(235, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(237, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(244, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(248, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(251, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(264, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(266, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(268, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(270, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(271, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(273, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(275, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(277, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(279, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(281, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(283, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(285, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(287, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(289, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(293, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(294, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(297, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(301, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(303, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(305, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(309, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(322, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(324, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(326, CreditsType.MINOR_HEADER_1),
    new CreditHeader08(328, CreditsType.MAJOR_HEADER_0),
    new CreditHeader08(344, CreditsType.MAJOR_HEADER_0),
  };
  private static final Vector2i[] creditVramPos_800f9670 = {
    new Vector2i(0x200, 0),
    new Vector2i(0x200, 0x40),
    new Vector2i(0x200, 0x80),
    new Vector2i(0x200, 0xc0),
    new Vector2i(0x200, 0x100),
    new Vector2i(0x200, 0x140),
    new Vector2i(0x200, 0x180),
    new Vector2i(0x200, 0x1c0),
    new Vector2i(0x280, 0),
    new Vector2i(0x280, 0x40),
    new Vector2i(0x280, 0x80),
    new Vector2i(0x280, 0xc0),
    new Vector2i(0x280, 0x100),
    new Vector2i(0x280, 0x140),
    new Vector2i(0x280, 0x180),
    new Vector2i(0x280, 0x1c0),
  };

  @Override
  @Method(0x800eaa88L)
  public void tick() {
    this.creditsStates_800f9378[this.loadingStage].run();
  }

  @Method(0x800eaad4L)
  private void initCredits() {
    resizeDisplay(384, 240);
    vsyncMode_8007a3b8 = 2;

    //LAB_800eab00
    for(int creditSlot = 0; creditSlot < 16; creditSlot++) {
      final CreditData1c credit = this.credits_800d1af8[creditSlot];

      //LAB_800eab1c
      credit.colour_00.set(0x80, 0x80, 0x80);
      credit.scroll_12 = 0;
      credit.brightnessAngle_14 = 0;
      credit.state_16 = 0;
    }

    //LAB_800eac18
    //LAB_800eac20
    this.credits_800d1af8[0].prevCreditSlot_04 = 15;
    for(int i = 1; i < 16; i++) {
      //LAB_800eac3c
      final CreditData1c credit = this.credits_800d1af8[i];
      credit.prevCreditSlot_04 = i - 1;
    }

    //LAB_800eac84
    this.fadeOutTicks_800d1ae4 = 0;
    this.creditsPassed_800d1aec = 0;
    this.creditIndex_800d1af0 = 0;

    this.creditTimsLoaded_800d1ae8 = false;
    loadDrgnDir(0, 5720, this::creditsLoaded);
    this.loadingStage++;
  }

  @Method(0x800eacc8L)
  private void creditsLoaded(final List<FileData> files) {
    this.creditTims_800d1ae0 = files;
    this.creditTimsLoaded_800d1ae8 = true;
  }

  @Method(0x800ead58L)
  private void waitForCreditsToLoadAndPlaySong() {
    if(this.creditTimsLoaded_800d1ae8) {
      //LAB_800ead7c
      playXaAudio(3, 3, 1);
      startFadeEffect(2, 15);
      this.loadingStage++;
    }
    //LAB_800ead9c
  }

  @Method(0x800eadfcL)
  private void renderCredits() {
    this.renderCreditsGradient();

    if(this.loadAndRenderCredits()) {
      startFadeEffect(1, 15);
      this.loadingStage++;
    }
    //LAB_800eae28
  }

  @Method(0x800eae6cL)
  private void waitForCreditsFadeOut() {
    this.fadeOutTicks_800d1ae4++;

    if(this.fadeOutTicks_800d1ae4 >= 16) {
      //LAB_800eaea0
      this.loadingStage++;
    }
    //LAB_800eaeac
  }

  @Method(0x800eaeb8L)
  private void deallocateCreditsAndTransitionToTheEndSubmap() {
    //LAB_800eaedc
    this.creditTims_800d1ae0 = null;
    engineStateOnceLoaded_8004dd24 = EngineStateEnum.SUBMAP_05;

    //LAB_800eaf14
  }

  @Method(0x800eaf24L)
  private void renderCreditsGradient() {
    GPU.queueCommand(10, new GpuCommandPoly(4)
      .translucent(Translucency.B_MINUS_F)
      .monochrome(0, 0xff)
      .monochrome(1, 0xff)
      .monochrome(2, 0)
      .monochrome(3, 0)
      .pos(0, -192, -120)
      .pos(1, 192, -120)
      .pos(2, -192, -64)
      .pos(3, 192, -64)
    );

    GPU.queueCommand(10, new GpuCommandPoly(4)
      .translucent(Translucency.B_MINUS_F)
      .monochrome(0, 0)
      .monochrome(1, 0)
      .monochrome(2, 0xff)
      .monochrome(3, 0xff)
      .pos(0, -192, 64)
      .pos(1, 192, 64)
      .pos(2, -192, 120)
      .pos(3, 192, 120)
    );
  }

  @Method(0x800eb304L)
  private boolean loadAndRenderCredits() {
    //LAB_800eb318
    //LAB_800ebc0c
    for(int creditSlot = 0; creditSlot < 16; creditSlot++) {
      //LAB_800eb334
      if(this.creditsPassed_800d1aec >= 357) {
        return true;
      }

      final CreditData1c credit = this.credits_800d1af8[creditSlot];

      //LAB_800eb358
      final int state = credit.state_16;
      if(state == 0) {
        //LAB_800eb3b8
        if(this.shouldLoadNewCredit(creditSlot)) {
          credit.state_16 = 2;
          this.loadCreditTims(creditSlot);
        }
      } else if(state == 2) {
        //LAB_800eb408
        this.moveCredits(creditSlot);

        final int w = credit.width_0e * 4;
        final int h = credit.height_10;
        final int x = -w / 2 - 8;
        final int y = credit.y_0c;
        final int clut = creditSlot << 6 | 0x38;

        //LAB_800eb8e8
        this.renderQuad(
          Bpp.BITS_4, creditSlot / 8 * 128 + 512 & 0x3c0, 0, clut,
          credit.colour_00.x, credit.colour_00.y, credit.colour_00.z,
          0, creditSlot % 8 * 64,
          w, h,
          x, y,
          w, h,
          orderingTableSize_1f8003c8 - 3
        );

        //LAB_800eba4c
        credit.scroll_12++;
        //LAB_800eb3a4
      } else if(state == 3) {
        //LAB_800ebabc
        if(this.credits_800d1af8[(creditSlot + 1) % 16].state_16 != 0) {
          credit.scroll_12 = 0;
          credit.brightnessAngle_14 = 0;
          credit.state_16 = 0;
        } else {
          //LAB_800ebb84
          credit.scroll_12++;
        }
      }
    }

    //LAB_800ebc18
    return false;
  }

  @Method(0x800ebc2cL)
  private boolean shouldLoadNewCredit(final int creditSlot) {
    //LAB_800ebc5c
    boolean found = false;

    //LAB_800ebc64
    int i;
    for(i = 0; i < creditHeaderEntries_800f93b0.length; i++) {
      //LAB_800ebcb0
      if(creditHeaderEntries_800f93b0[i].creditIndex_00 == this.creditIndex_800d1af0) {
        found = true;
        break;
      }
    }

    final CreditData1c credit = this.credits_800d1af8[creditSlot];

    //LAB_800ebd08
    if(found) {
      credit.type_08 = creditHeaderEntries_800f93b0[i].type_04;
    } else {
      //LAB_800ebd6c
      credit.type_08 = CreditsType.NAME_2;
    }

    //LAB_800ebd94
    if(this.creditIndex_800d1af0 == 0) {
      return true;
    }

    //LAB_800ebdb4
    final int prevCreditSlot = credit.prevCreditSlot_04;
    final CreditData1c prevCredit = this.credits_800d1af8[prevCreditSlot];

    if(prevCredit.state_16 == 0) {
      return false;
    }

    //LAB_800ebe1c
    switch(credit.type_08) {
      case MAJOR_HEADER_0 -> {
        switch(prevCredit.type_08) {
          case MAJOR_HEADER_0, MINOR_HEADER_1, NAME_2 -> {
            //LAB_800ebee4
            if(prevCredit.scroll_12 >= 66) {
              return true;
            }
          }

          //LAB_800ebf24
          case DIRECTOR_3 -> {
            //LAB_800ebf2c
            if(prevCredit.scroll_12 >= 64) {
              return true;
            }
          }

          //LAB_800ebf6c
          //LAB_800ebed0
          case UNUSED_4 -> {
            //LAB_800ebf74
            if(prevCredit.scroll_12 >= 144) {
              return true;
            }
          }
          //LAB_800ebfb4
        }
        //LAB_800ebfbc
      }

      case MINOR_HEADER_1 -> {
        switch(prevCredit.type_08) {
          case MAJOR_HEADER_0, MINOR_HEADER_1, NAME_2 -> {
            //LAB_800ec024
            if(prevCredit.scroll_12 >= 36) {
              return true;
            }
          }

          //LAB_800ec064
          case DIRECTOR_3 -> {
            //LAB_800ec06c
            if(prevCredit.scroll_12 >= 64) {
              return true;
            }
          }

          //LAB_800ec0ac
          //LAB_800ec010
          case UNUSED_4 -> {
            //LAB_800ec0b4
            if(prevCredit.scroll_12 >= 144) {
              return true;
            }
          }
          //LAB_800ec0f4
        }
        //LAB_800ec0fc
      }

      case NAME_2 -> {
        switch(prevCredit.type_08) {
          case MAJOR_HEADER_0, MINOR_HEADER_1 -> {
            if(prevCredit.scroll_12 >= 27) {
              return true;
            }
          }

          //LAB_800ec1ac
          case NAME_2 -> {
            if(prevCredit.scroll_12 >= 23) {
              return true;
            }
          }

          //LAB_800ec1f4
          case DIRECTOR_3 -> {
            if(prevCredit.scroll_12 >= 80) {
              return true;
            }
          }

          //LAB_800ec23c
          case UNUSED_4 -> {
            if(prevCredit.scroll_12 >= 144) {
              return true;
            }
          }
          //LAB_800ec284
        }
        //LAB_800ec28c
      }

      case DIRECTOR_3 -> {
        if(prevCredit.type_08 == CreditsType.DIRECTOR_3) {
          return true;
        }

        //LAB_800ec2d0
        if(prevCredit.scroll_12 > 16) {
          return true;
        }
        //LAB_800ec310
      }

      case UNUSED_4 -> {
        if(prevCredit.scroll_12 >= 130) {
          return true;
        }
        //LAB_800ec358
      }
    }

    //LAB_800ec360
    //LAB_800ec36c
    return false;
  }

  @Method(0x800ec37cL)
  private void moveCredits(final int creditSlot) {
    final CreditData1c credit = this.credits_800d1af8[creditSlot];

    final int scroll = credit.scroll_12;

    switch(credit.type_08) {
      case MAJOR_HEADER_0, MINOR_HEADER_1 -> {
        credit.y_0c = 136 - scroll;
        credit.colour_00.set(192, 93, 81);

        if(scroll > 304) {
          credit.state_16 = 3;
          this.creditsPassed_800d1aec++;
        }
      }

      //LAB_800ec51c
      case NAME_2 -> {
        credit.y_0c = 136 - scroll;
        credit.colour_00.set(118, 107, 195);

        if(scroll > 304) {
          credit.state_16 = 3;
          this.creditsPassed_800d1aec++;
        }
      }

      //LAB_800ec620
      case DIRECTOR_3 -> {
        final int prevCreditSlot = credit.prevCreditSlot_04;
        final CreditData1c prevCredit = this.credits_800d1af8[prevCreditSlot];

        if(credit.scroll_12 < 64) {
          if(prevCredit.type_08 == CreditsType.DIRECTOR_3) {
            credit.y_0c = -credit.height_10 / 2 + 13;
            credit.colour_00.x = rsin(credit.scroll_12 * 16) * 118 >> 12;
            credit.colour_00.y = rsin(credit.scroll_12 * 16) * 107 >> 12;
            credit.colour_00.z = rsin(credit.scroll_12 * 16) * 195 >> 12;
          } else {
            //LAB_800ec89c
            credit.y_0c = -credit.height_10 / 2 - 13;
            credit.colour_00.x = rsin(credit.scroll_12 * 16) * 192 >> 12;
            credit.colour_00.y = rsin(credit.scroll_12 * 16) * 93 >> 12;
            credit.colour_00.z = rsin(credit.scroll_12 * 16) * 81 >> 12;
          }
          //LAB_800eca68
        } else {
          //LAB_800eca70
          credit.brightnessAngle_14++;

          final int brightnessAngle = credit.brightnessAngle_14;
          if(prevCredit.type_08 == CreditsType.DIRECTOR_3) {
            credit.y_0c = -credit.height_10 / 2 - brightnessAngle + 13;
            credit.colour_00.set(118, 107, 195);
          } else {
            //LAB_800ecc2c
            credit.y_0c = -credit.height_10 / 2 - brightnessAngle - 13;
            credit.colour_00.set(192, 93, 81);
          }

          //LAB_800ecd1c
          if(credit.y_0c < -184) {
            credit.state_16 = 3;
            this.creditsPassed_800d1aec++;
          }
        }
      }

      //LAB_800ecd90
      case UNUSED_4 -> {
        if(credit.y_0c < -credit.height_10 / 2 && scroll != 0) {
          credit.brightnessAngle_14 += 6;
          final int brightnessAngle = credit.brightnessAngle_14;
          credit.colour_00.x = rcos(brightnessAngle) * 128 >> 12;
          credit.colour_00.y = rcos(brightnessAngle) * 128 >> 12;
          credit.colour_00.z = rcos(brightnessAngle) * 128 >> 12;

          if(brightnessAngle > 0x400) {
            credit.colour_00.set(0, 0, 0);
            credit.state_16 = 3;
            this.creditsPassed_800d1aec++;
          }
          //LAB_800ecff8
        } else {
          //LAB_800ed000
          credit.y_0c = 136 - scroll;
          credit.colour_00.set(0x80, 0x80, 0x80);
        }
      }
      //LAB_800ed0a8
    }
    //LAB_800ed0b0
  }

  @Method(0x800ed0c4L)
  private void loadCreditTims(final int creditSlot) {
    if(this.creditIndex_800d1af0 < this.creditTims_800d1ae0.size()) {
      //LAB_800ed100
      if(this.creditTims_800d1ae0.get(this.creditIndex_800d1af0).size() != 0) {
        //LAB_800ed138
        this.loadCreditTim(creditSlot);
      }
    }
    //LAB_800ed150
  }

  @Method(0x800ed160L)
  private void loadCreditTim(final int creditSlot) {
    final Tim tim = new Tim(this.creditTims_800d1ae0.get(this.creditIndex_800d1af0));

    this.creditIndex_800d1af0++;

    if(this.creditIndex_800d1af0 > 357) {
      this.creditIndex_800d1af0 = 357;
    }

    final CreditData1c credit = this.credits_800d1af8[creditSlot];

    //LAB_800ed1f8
    final Rect4i imageRect = tim.getImageRect();
    imageRect.x = creditVramPos_800f9670[creditSlot].x;
    imageRect.y = creditVramPos_800f9670[creditSlot].y;

    credit.width_0e = imageRect.w;
    credit.height_10 = imageRect.h;

    GPU.uploadData15(imageRect, tim.getImageData());

    if(tim.hasClut()) {
      GPU.uploadData15(new Rect4i(896, creditSlot, 16, 1), tim.getClutData());
    }
    //LAB_800ed32c
  }

  @Method(0x800ed3b0L)
  private void renderQuad(final Bpp bpp, final int vramX, final int vramY, final int clut, final int r, final int g, final int b, final int u, final int v, final int tw, final int th, final int x, final int y, final int w, final int h, final int z) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .bpp(bpp)
      .clut((clut & 0b111111) * 16, clut >>> 6)
      .vramPos(vramX, vramY)
      .rgb(r, g, b)
      .pos(0, x, y)
      .pos(1, x + w, y)
      .pos(2, x, y + h)
      .pos(3, x + w, y + h)
      .uv(0, u, v)
      .uv(1, u + tw, v)
      .uv(2, u, v + th)
      .uv(3, u + tw, v + th);

    GPU.queueCommand(z, cmd);
  }
}
