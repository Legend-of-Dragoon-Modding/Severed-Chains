package legend.game.credits;

import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.RECT;
import legend.core.gte.DVECTOR;
import legend.core.memory.Method;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.game.tim.Tim;
import legend.game.types.EngineState;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;

import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.playXaAudio;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;

public final class Credits {
  private Credits() { }

  private static List<FileData> creditTims_800d1ae0;
  private static final IntRef fadeOutTicks_800d1ae4 = MEMORY.ref(4, 0x800d1ae4L, IntRef::new);
  private static final BoolRef creditTimsLoaded_800d1ae8 = MEMORY.ref(4, 0x800d1ae8L, BoolRef::new);
  private static final IntRef creditsPassed_800d1aec = MEMORY.ref(4, 0x800d1aecL, IntRef::new);
  private static final IntRef creditIndex_800d1af0 = MEMORY.ref(4, 0x800d1af0L, IntRef::new);

  private static final ArrayRef<CreditStruct1c> credits_800d1af8 = MEMORY.ref(4, 0x800d1af8L, ArrayRef.of(CreditStruct1c.class, 16, 0x1c, CreditStruct1c::new));

  /**
   * <ol start="0">
   *   <li>{@link Credits#initCredits}</li>
   *   <li>{@link Credits#waitForCreditsToLoadAndPlaySong}</li>
   *   <li>{@link Credits#renderCredits}</li>
   *   <li>{@link Credits#waitForCreditsFadeOut}</li>
   *   <li>{@link Credits#deallocateCreditsAndTransitionToTheEndSubmap}</li>
   * </ol>
   */
  private static final Runnable[] creditsStates_800f9378 = new Runnable[8];
  static {
    creditsStates_800f9378[0] = Credits::initCredits;
    creditsStates_800f9378[1] = Credits::waitForCreditsToLoadAndPlaySong;
    creditsStates_800f9378[2] = Credits::renderCredits;
    creditsStates_800f9378[3] = Credits::waitForCreditsFadeOut;
    creditsStates_800f9378[4] = Credits::deallocateCreditsAndTransitionToTheEndSubmap;
  }

  private static final ArrayRef<CreditType08> creditTypes_800f93b0 = MEMORY.ref(4, 0x800f93b0L, ArrayRef.of(CreditType08.class, 87, 0x8, CreditType08::new));
  private static final ArrayRef<DVECTOR> creditPos_800f9670 = MEMORY.ref(2, 0x800f9670L, ArrayRef.of(DVECTOR.class, 16, 4, DVECTOR::new));

  @Method(0x800eaa88L)
  public static void tickCredits() {
    creditsStates_800f9378[pregameLoadingStage_800bb10c.get()].run();
  }

  @Method(0x800eaad4L)
  private static void initCredits() {
    resizeDisplay(384, 240);
    vsyncMode_8007a3b8 = 2;

    //LAB_800eab00
    for(int creditSlot = 0; creditSlot < 16; creditSlot++) {
      final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

      //LAB_800eab1c
      credit.colour_00.set(0x80, 0x80, 0x80);
      credit.scroll_12.set(0);
      credit.brightnessAngle_14.set(0);
      credit.state_16.set(0);
    }

    //LAB_800eac18
    //LAB_800eac20
    credits_800d1af8.get(0).prevCreditSlot_04.set(15);
    for(int i = 1; i < 16; i++) {
      //LAB_800eac3c
      final CreditStruct1c credit = credits_800d1af8.get(i);
      credit.prevCreditSlot_04.set(i - 1);
    }

    //LAB_800eac84
    fadeOutTicks_800d1ae4.set(0);
    creditsPassed_800d1aec.set(0);
    creditIndex_800d1af0.set(0);

    creditTimsLoaded_800d1ae8.set(false);
    loadDrgnDir(0, 5720, Credits::creditsLoaded);
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800eacc8L)
  private static void creditsLoaded(final List<FileData> files) {
    creditTims_800d1ae0 = files;
    creditTimsLoaded_800d1ae8.set(true);
  }

  @Method(0x800ead58L)
  private static void waitForCreditsToLoadAndPlaySong() {
    if(creditTimsLoaded_800d1ae8.get()) {
      //LAB_800ead7c
      playXaAudio(3, 3, 1);
      startFadeEffect(2, 15);
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800ead9c
  }

  @Method(0x800eadfcL)
  private static void renderCredits() {
    renderCreditsGradient();

    if(loadAndRenderCredits()) {
      startFadeEffect(1, 15);
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800eae28
  }

  @Method(0x800eae6cL)
  private static void waitForCreditsFadeOut() {
    fadeOutTicks_800d1ae4.incr();

    if(fadeOutTicks_800d1ae4.get() >= 16) {
      //LAB_800eaea0
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800eaeac
  }

  @Method(0x800eaeb8L)
  private static void deallocateCreditsAndTransitionToTheEndSubmap() {
    //LAB_800eaedc
    creditTims_800d1ae0 = null;
    engineStateOnceLoaded_8004dd24 = EngineState.SUBMAP_05;
    pregameLoadingStage_800bb10c.set(0);

    //LAB_800eaf14
  }

  @Method(0x800eaf24L)
  private static void renderCreditsGradient() {
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
  private static boolean loadAndRenderCredits() {
    //LAB_800eb318
    //LAB_800ebc0c
    for(int creditSlot = 0; creditSlot < 16; creditSlot++) {
      //LAB_800eb334
      if(creditsPassed_800d1aec.get() >= 357) {
        return true;
      }

      final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

      //LAB_800eb358
      final int state = credit.state_16.get();
      if(state == 0) {
        //LAB_800eb3b8
        if(shouldLoadNewCredit(creditSlot)) {
          credit.state_16.set(2);
          loadCreditTims(creditSlot);
        }
      } else if(state == 2) {
        //LAB_800eb408
        moveCredits(creditSlot);

        final int w = credit.width_0e.get() * 4;
        final int h = credit.height_10.get();
        final int x = -w / 2 - 8;
        final int y = credit.y_0c.get();
        final int clut = creditSlot << 6 | 0x38;

        //LAB_800eb8e8
        renderQuad(
          Bpp.BITS_4, creditSlot / 8 * 128 + 512 & 0x3c0, 0, clut,
          credit.colour_00.getR(), credit.colour_00.getG(), credit.colour_00.getB(),
          0, creditSlot % 8 * 64,
          w, h,
          x, y,
          w, h,
          orderingTableSize_1f8003c8.get() - 3
        );

        //LAB_800eba4c
        credit.scroll_12.incr();
        //LAB_800eb3a4
      } else if(state == 3) {
        //LAB_800ebabc
        if(credits_800d1af8.get((creditSlot + 1) % 16).state_16.get() != 0) {
          credit.scroll_12.set(0);
          credit.brightnessAngle_14.set(0);
          credit.state_16.set(0);
        } else {
          //LAB_800ebb84
          credit.scroll_12.incr();
        }
      }
    }

    //LAB_800ebc18
    return false;
  }

  @Method(0x800ebc2cL)
  private static boolean shouldLoadNewCredit(final int creditSlot) {
    //LAB_800ebc5c
    boolean found = false;

    //LAB_800ebc64
    int i;
    for(i = 0; i < creditTypes_800f93b0.length(); i++) {
      //LAB_800ebcb0
      if(creditTypes_800f93b0.get(i).creditIndex_00.get() == creditIndex_800d1af0.get()) {
        found = true;
        break;
      }
    }

    final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

    //LAB_800ebd08
    if(found) {
      credit.type_08.set(creditTypes_800f93b0.get(i).type_04.get());
    } else {
      //LAB_800ebd6c
      credit.type_08.set(2);
    }

    //LAB_800ebd94
    if(creditIndex_800d1af0.get() == 0) {
      return true;
    }

    //LAB_800ebdb4
    final int prevCreditSlot = credit.prevCreditSlot_04.get();
    final CreditStruct1c prevCredit = credits_800d1af8.get(prevCreditSlot);

    if(prevCredit.state_16.get() == 0) {
      return false;
    }

    //LAB_800ebe1c
    switch(credit.type_08.get()) {
      case 0 -> {
        final int type = prevCredit.type_08.get();

        if(type == 0 || type == 1 || type == 2) {
          //LAB_800ebee4
          if(prevCredit.scroll_12.get() >= 66) {
            return true;
          }

          //LAB_800ebf24
        } else if(type == 3) {
          //LAB_800ebf2c
          if(prevCredit.scroll_12.get() >= 64) {
            return true;
          }

          //LAB_800ebf6c
          //LAB_800ebed0
        } else if(type == 4) {
          //LAB_800ebf74
          if(prevCredit.scroll_12.get() >= 144) {
            return true;
          }

          //LAB_800ebfb4
        }

        //LAB_800ebfbc
      }

      case 1 -> {
        final int type = prevCredit.type_08.get();

        if(type == 0 || type == 1 || type == 2) {
          //LAB_800ec024
          if(prevCredit.scroll_12.get() >= 36) {
            return true;
          }

          //LAB_800ec064
        } else if(type == 3) {
          //LAB_800ec06c
          if(prevCredit.scroll_12.get() >= 64) {
            return true;
          }

          //LAB_800ec0ac
          //LAB_800ec010
        } else if(type == 4) {
          //LAB_800ec0b4
          if(prevCredit.scroll_12.get() >= 144) {
            return true;
          }

          //LAB_800ec0f4
        }

        //LAB_800ec0fc
      }

      case 2 -> {
        switch(prevCredit.type_08.get()) {
          case 0, 1 -> {
            if(prevCredit.scroll_12.get() >= 27) {
              return true;
            }
          }

          //LAB_800ec1ac
          case 2 -> {
            if(prevCredit.scroll_12.get() >= 23) {
              return true;
            }
          }

          //LAB_800ec1f4
          case 3 -> {
            if(prevCredit.scroll_12.get() >= 80) {
              return true;
            }
          }

          //LAB_800ec23c
          case 4 -> {
            if(prevCredit.scroll_12.get() >= 144) {
              return true;
            }
          }

          //LAB_800ec284
        }

        //LAB_800ec28c
      }

      case 3 -> {
        if(prevCredit.type_08.get() == 3) {
          return true;
        }

        //LAB_800ec2d0
        if(prevCredit.scroll_12.get() > 16) {
          return true;
        }

        //LAB_800ec310
      }

      case 4 -> {
        if(prevCredit.scroll_12.get() >= 130) {
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
  private static void moveCredits(final int creditSlot) {
    final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

    final int scroll = credit.scroll_12.get();

    switch(credit.type_08.get()) {
      case 0, 1 -> {
        credit.y_0c.set((short)(136 - scroll));
        credit.colour_00.set(192, 93, 81);

        if(scroll > 304) {
          credit.state_16.set(3);
          creditsPassed_800d1aec.incr();
        }
      }

      //LAB_800ec51c
      case 2 -> {
        credit.y_0c.set((short)(136 - scroll));
        credit.colour_00.set(118, 107, 195);

        if(scroll > 304) {
          credit.state_16.set(3);
          creditsPassed_800d1aec.incr();
        }
      }

      //LAB_800ec620
      case 3 -> {
        final int prevCreditSlot = credit.prevCreditSlot_04.get();
        final CreditStruct1c prevCredit = credits_800d1af8.get(prevCreditSlot);

        if(credit.scroll_12.get() < 64) {
          if(credit.type_08.get() == 3) {
            credit.y_0c.set((short)(-credit.height_10.get() / 2 + 13));
            credit.colour_00.setR(rsin(credit.scroll_12.get() * 16) * 118 >> 12);
            credit.colour_00.setG(rsin(credit.scroll_12.get() * 16) * 107 >> 12);
            credit.colour_00.setB(rsin(credit.scroll_12.get() * 16) * 195 >> 12);
          } else {
            //LAB_800ec89c
            credit.y_0c.set((short)(-credit.height_10.get() / 2 - 13));
            credit.colour_00.setR(rsin(credit.scroll_12.get() * 16) * 192 >> 12);
            credit.colour_00.setG(rsin(credit.scroll_12.get() * 16) * 93 >> 12);
            credit.colour_00.setB(rsin(credit.scroll_12.get() * 16) * 81 >> 12);
          }

          //LAB_800eca68
        } else {
          //LAB_800eca70
          credit.brightnessAngle_14.incr();

          final int sp14 = credit.brightnessAngle_14.get();
          if(prevCredit.type_08.get() == 3) {
            credit.y_0c.set((short)(-credit.height_10.get() / 2 - sp14 + 13));
            credit.colour_00.set(118, 107, 195);
          } else {
            //LAB_800ecc2c
            credit.y_0c.set((short)(-credit.height_10.get() / 2 - sp14 - 13));
            credit.colour_00.set(192, 93, 81);
          }

          //LAB_800ecd1c
          if(credit.y_0c.get() < -184) {
            credit.state_16.set(3);
            creditsPassed_800d1aec.incr();
          }
        }
      }

      //LAB_800ecd90
      case 4 -> {
        if(credit.y_0c.get() < -credit.height_10.get() / 2 && scroll != 0) {
          credit.brightnessAngle_14.add(6);
          final int brightnessAngle = credit.brightnessAngle_14.get();
          credit.colour_00.setR(rcos(brightnessAngle) * 128 >> 12);
          credit.colour_00.setG(rcos(brightnessAngle) * 128 >> 12);
          credit.colour_00.setB(rcos(brightnessAngle) * 128 >> 12);

          if(brightnessAngle > 0x400) {
            credit.colour_00.set(0, 0, 0);
            credit.state_16.set(3);
            creditsPassed_800d1aec.incr();
          }

          //LAB_800ecff8
        } else {
          //LAB_800ed000
          credit.y_0c.set((short)(136 - scroll));
          credit.colour_00.set(0x80, 0x80, 0x80);
        }
      }

      //LAB_800ed0a8
    }

    //LAB_800ed0b0
  }

  @Method(0x800ed0c4L)
  private static void loadCreditTims(final int creditSlot) {
    if(creditIndex_800d1af0.get() < creditTims_800d1ae0.size()) {
      //LAB_800ed100
      if(creditTims_800d1ae0.get(creditIndex_800d1af0.get()).size() != 0) {
        //LAB_800ed138
        loadCreditTim(creditSlot);
      }
    }

    //LAB_800ed150
  }

  @Method(0x800ed160L)
  private static void loadCreditTim(final int creditSlot) {
    final Tim tim = new Tim(creditTims_800d1ae0.get(creditIndex_800d1af0.get()));

    creditIndex_800d1af0.incr();

    if(creditIndex_800d1af0.get() > 357) {
      creditIndex_800d1af0.set(357);
    }

    final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

    //LAB_800ed1f8
    final RECT imageRect = tim.getImageRect();

    final RECT rect = new RECT(
      creditPos_800f9670.get(creditSlot).getX(),
      creditPos_800f9670.get(creditSlot).getY(),
      imageRect.w.get(),
      imageRect.h.get()
    );

    credit.width_0e.set(imageRect.w.get());
    credit.height_10.set(imageRect.h.get());

    LoadImage(rect, tim.getImageData());

    if(tim.hasClut()) {
      LoadImage(new RECT((short)896, (short)creditSlot, (short)16, (short)1), tim.getClutData());
    }

    //LAB_800ed32c
  }

  @Method(0x800ed3b0L)
  private static void renderQuad(final Bpp bpp, final int vramX, final int vramY, final int clut, final int r, final int g, final int b, final int u, final int v, final int tw, final int th, final int x, final int y, final int w, final int h, final int z) {
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
