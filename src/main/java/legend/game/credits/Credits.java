package legend.game.credits;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.core.opengl.QuadBuilder;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.tim.Tim;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.RENDERER;
import static legend.core.MathHelper.cos;
import static legend.core.MathHelper.sin;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.playXaAudio;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class Credits extends EngineState {
  public enum CreditsType {
    MAJOR_HEADER_0,
    MINOR_HEADER_1,
    NAME_2,
    DIRECTOR_3,
    UNUSED_4, // May have been for "The End" originally
  }

  public enum CreditState {
    LOAD_0,
    NONE_1,
    RENDER_2,
    PASSED_3,
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

  private final MV transforms = new MV();
  private Obj gradient;
  private Obj credits;

  @Override
  @Method(0x800eaa88L)
  public void tick() {
    super.tick();

    if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || PLATFORM.isActionPressed(INPUT_ACTION_MENU_BACK.get())) {
      this.loadingStage = 4;
    }
    this.creditsStates_800f9378[this.loadingStage].run();
  }

  @Method(0x800eaad4L)
  private void initCredits() {
    resizeDisplay(384, 240);
    vsyncMode_8007a3b8 = 1;

    //LAB_800eab00
    for(int creditSlot = 0; creditSlot < 16; creditSlot++) {
      final CreditData1c credit = this.credits_800d1af8[creditSlot];

      //LAB_800eab1c
      credit.colour_00.set(0.5f);
      credit.scroll_12 = 0.0f;
      credit.brightnessAngle_14 = 0.0f;
      credit.state_16 = CreditState.LOAD_0;
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

    this.gradient = new PolyBuilder("CreditsGradient", GL_TRIANGLE_STRIP)
      .translucency(Translucency.B_MINUS_F)
      .addVertex(-192.0f, -120.0f, 0.0f)
      .monochrome(1.0f)
      .addVertex(-192.0f, -64.0f, 0.0f)
      .monochrome(0.0f)
      .addVertex(192.0f, -120.0f, 0.0f)
      .monochrome(1.0f)
      .addVertex(192.0f, -64.0f, 0.0f)
      .monochrome(0.0f)
      .build();

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

      final QuadBuilder builder = new QuadBuilder("Credits");

      int creditSlot = 0;
      for(int i = 0; i < this.creditTims_800d1ae0.size(); i ++) {
        final Rect4i credit = new Tim(this.creditTims_800d1ae0.get(i)).getImageRect();
        builder.add()
          .bpp(Bpp.BITS_4)
          .vramPos(creditSlot / 8 * 128 + 512 & 0x3c0, 0)
          .clut(896, creditSlot)
          .monochrome(1.0f)
          .uv(0, creditSlot % 8 * 64)
          .size(credit.w * 4, credit.h);

        creditSlot = (creditSlot + 1) % 16;
      }

      this.credits = builder.build();
      GPU.clearData(512, 0, 256, 512);
      this.loadingStage++;
    }
    //LAB_800ead9c
  }

  @Method(0x800eadfcL)
  private void renderCredits() {
    final boolean creditsComplete = this.loadAndRenderCredits();
    this.renderCreditsGradient();

    if(creditsComplete) {
      startFadeEffect(1, 15);
      this.loadingStage++;
    }
    //LAB_800eae28
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

    if(prevCredit.state_16 == CreditState.LOAD_0) {
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

  @Method(0x800ed160L)
  private void loadCreditTim(final int creditSlot) {
    if(this.creditIndex_800d1af0 < this.creditTims_800d1ae0.size()) {
      //LAB_800ed100
      if(this.creditTims_800d1ae0.get(this.creditIndex_800d1af0).size() != 0) {
        final Tim tim = new Tim(this.creditTims_800d1ae0.get(this.creditIndex_800d1af0));

        this.creditIndex_800d1af0++;

        if(this.creditIndex_800d1af0 > 357) {
          this.creditIndex_800d1af0 = 357;
        }

        final CreditData1c credit = this.credits_800d1af8[creditSlot];
        credit.index = this.creditIndex_800d1af0 - 1;

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
      }
    }
    //LAB_800ed32c
  }

  @Method(0x800ec37cL)
  private void moveCredits(final int creditSlot) {
    final CreditData1c credit = this.credits_800d1af8[creditSlot];

    final float scroll = credit.scroll_12;

    switch(credit.type_08) {
      case MAJOR_HEADER_0, MINOR_HEADER_1 -> {
        credit.y_0c = 136.0f - scroll;
        credit.colour_00.set(192.0f / 255.0f, 93.0f / 255.0f, 81.0f / 255.0f);

        if(scroll > 304) {
          credit.state_16 = CreditState.PASSED_3;
          this.creditsPassed_800d1aec++;
        }
      }

      //LAB_800ec51c
      case NAME_2 -> {
        credit.y_0c = 136.0f - scroll;
        credit.colour_00.set(118.0f / 255.0f, 107.0f / 255.0f, 195.0f / 255.0f);

        if(scroll > 304) {
          credit.state_16 = CreditState.PASSED_3;
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
            credit.colour_00.x = sin((credit.scroll_12 * 16.0f / 4096.0f) * MathHelper.TWO_PI) * (118.0f / 255.0f);
            credit.colour_00.y = sin((credit.scroll_12 * 16.0f / 4096.0f) * MathHelper.TWO_PI) * (107.0f / 255.0f);
            credit.colour_00.z = sin((credit.scroll_12 * 16.0f / 4096.0f) * MathHelper.TWO_PI) * (195.0f / 255.0f);
          } else {
            //LAB_800ec89c
            credit.y_0c = -credit.height_10 / 2 - 13;
            credit.colour_00.x = sin(MathHelper.psxDegToRad(credit.scroll_12 * 16.0f)) * (192.0f / 255.0f);
            credit.colour_00.y = sin(MathHelper.psxDegToRad(credit.scroll_12 * 16.0f)) * (93.0f / 255.0f);
            credit.colour_00.z = sin(MathHelper.psxDegToRad(credit.scroll_12 * 16.0f)) * (81.0f / 255.0f);
          }
          //LAB_800eca68
        } else {
          //LAB_800eca70
          credit.brightnessAngle_14 = credit.brightnessAngle_14 + 1.0f / (3 - vsyncMode_8007a3b8);

          final float brightnessAngle = credit.brightnessAngle_14;
          if(prevCredit.type_08 == CreditsType.DIRECTOR_3) {
            credit.y_0c = -credit.height_10 / 2.0f - brightnessAngle + 13;
            credit.colour_00.set(118.0f / 255.0f, 107.0f / 255.0f, 195.0f / 255.0f);
          } else {
            //LAB_800ecc2c
            credit.y_0c = -credit.height_10 / 2.0f - brightnessAngle - 13;
            credit.colour_00.set(192.0f / 255.0f, 93.0f / 255.0f, 81.0f / 255.0f);
          }

          //LAB_800ecd1c
          if(credit.y_0c < -184) {
            credit.state_16 = CreditState.PASSED_3;
            this.creditsPassed_800d1aec++;
          }
        }
      }

      //LAB_800ecd90
      case UNUSED_4 -> {
        if(credit.y_0c < -credit.height_10 / 2 && scroll != 0) {
          credit.brightnessAngle_14 += 6.0f / (3 - vsyncMode_8007a3b8);
          final float brightnessAngle = credit.brightnessAngle_14;
          credit.colour_00.x = cos(MathHelper.psxDegToRad(brightnessAngle)) * 0.5f;
          credit.colour_00.y = cos(MathHelper.psxDegToRad(brightnessAngle)) * 0.5f;
          credit.colour_00.z = cos(MathHelper.psxDegToRad(brightnessAngle)) * 0.5f;

          if(credit.colour_00.x < 0.0f) {
            credit.colour_00.set(0.0f, 0.0f, 0.0f);
            credit.state_16 = CreditState.PASSED_3;
            this.creditsPassed_800d1aec++;
          }
          //LAB_800ecff8
        } else {
          //LAB_800ed000
          credit.y_0c = 136.0f - scroll;
          credit.colour_00.set(0.5f);
        }
      }
      //LAB_800ed0a8
    }
    //LAB_800ed0b0
  }

  @Method(0x800eaf24L)
  private void renderCreditsGradient() {
    this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), 40.0f);
    RENDERER.queueOrthoModel(this.gradient, this.transforms, QueuedModelStandard.class);
    this.transforms.rotate(MathHelper.PI, 0, 0, 0);
    RENDERER.queueOrthoModel(this.gradient, this.transforms, QueuedModelStandard.class);
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
      switch(credit.state_16) {
        case LOAD_0 -> {
          if(this.creditIndex_800d1af0 < 357) {
            //LAB_800eb3b8
            if(this.shouldLoadNewCredit(creditSlot)) {
              credit.state_16 = CreditState.RENDER_2;
              this.loadCreditTim(creditSlot);
            }
          }
        }

        case RENDER_2 -> {
          //LAB_800eb408
          this.moveCredits(creditSlot);

          final float w = credit.width_0e * 4;
          final float x = -w / 2 - 8;
          final float y = credit.y_0c;

          //LAB_800eb8e8
          this.renderQuad(credit, x, y);

          //LAB_800eba4c
          credit.scroll_12 = credit.scroll_12 + 1.0f / (3 - vsyncMode_8007a3b8);
        }

        //LAB_800eb3a4
        case PASSED_3 -> {
          //LAB_800ebabc
          if(this.credits_800d1af8[(creditSlot + 1) % 16].state_16 != CreditState.LOAD_0) {
            credit.scroll_12 = 0.0f;
            credit.brightnessAngle_14 = 0.0f;
            credit.state_16 = CreditState.LOAD_0;
          } else {
            //LAB_800ebb84
            credit.scroll_12 = credit.scroll_12 + 1.0f / (3 - vsyncMode_8007a3b8);
          }
        }
      }
    }

    //LAB_800ebc18
    return false;
  }

  @Method(0x800ed3b0L)
  private void renderQuad(final CreditData1c credit, final float x, final float y) {
    this.transforms.identity();
    this.transforms.transfer.set(GPU.getOffsetX() + x, GPU.getOffsetY() +  y, (orderingTableSize_1f8003c8 - 3) * 4.0f);
    RENDERER.queueOrthoModel(this.credits, this.transforms, QueuedModelStandard.class)
      .vertices(credit.index * 4, 4)
      .colour(credit.colour_00);
  }

  @Method(0x800eae6cL)
  private void waitForCreditsFadeOut() {
    this.fadeOutTicks_800d1ae4++;

    if(this.fadeOutTicks_800d1ae4 >= 16 * (3 - vsyncMode_8007a3b8)) {
      //LAB_800eaea0
      this.loadingStage++;
    }
    //LAB_800eaeac
  }

  @Method(0x800eaeb8L)
  private void deallocateCreditsAndTransitionToTheEndSubmap() {
    //LAB_800eaedc
    this.creditTims_800d1ae0 = null;

    if(this.gradient != null) {
      this.gradient.delete();
      this.gradient = null;
    }

    if(this.credits != null) {
      this.credits.delete();
      this.credits = null;
    }
    engineStateOnceLoaded_8004dd24 = EngineStateEnum.SUBMAP_05;

    //LAB_800eaf14
  }

  public static class CreditHeader08 {
    public final int creditIndex_00;
    public final CreditsType type_04;

    public CreditHeader08(final int creditIndex, final Credits.CreditsType type) {
      this.creditIndex_00 = creditIndex;
      this.type_04 = type;
    }
  }

  public static class CreditData1c {
    public int index;

    public final Vector3f colour_00 = new Vector3f();
    public int prevCreditSlot_04;
    public CreditsType type_08;
    public float y_0c;
    public float width_0e;
    public float height_10;
    public float scroll_12;
    public float brightnessAngle_14;
    /**
     * <ul>
     *   <li>0 - load</li>
     *   <li>2 - render</li>
     *   <li>3 - off screen</li>
     * </ul>
     */
    public CreditState state_16;
  }
}
