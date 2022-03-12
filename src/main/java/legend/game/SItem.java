package legend.game;

import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.UnsignedIntRef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.Scus94491BpeSegment.FUN_800127cc;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_800133ac;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022a94;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022afc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022b50;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022c08;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022d88;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023148;
import static legend.game.Scus94491BpeSegment_8002.FUN_800232dc;
import static legend.game.Scus94491BpeSegment_8002.FUN_800233d8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023484;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023674;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002379c;
import static legend.game.Scus94491BpeSegment_8002.FUN_800239e0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023a2c;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023b54;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023c28;
import static legend.game.Scus94491BpeSegment_8002.FUN_800242e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002437c;
import static legend.game.Scus94491BpeSegment_8002.FUN_80029300;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a6fc;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a86c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a8f8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bcc8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c150;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002dbdc;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002df60;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002e908;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002eb28;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002ed48;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002efb8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002f0d4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002f1d0;
import static legend.game.Scus94491BpeSegment_8002.getJoypadInputByPriority;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.strncmp;
import static legend.game.Scus94491BpeSegment_8004.FUN_80041070;
import static legend.game.Scus94491BpeSegment_8004.FUN_800412e0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80041420;
import static legend.game.Scus94491BpeSegment_8004.FUN_800414a0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80041600;
import static legend.game.Scus94491BpeSegment_8004.FUN_800426c4;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c6f8;
import static legend.game.Scus94491BpeSegment_8004._8004dd30;
import static legend.game.Scus94491BpeSegment_8004._8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8005._8005a368;
import static legend.game.Scus94491BpeSegment_8007.joypadDpad_8007a398;
import static legend.game.Scus94491BpeSegment_800b._800babc8;
import static legend.game.Scus94491BpeSegment_800b._800bad64;
import static legend.game.Scus94491BpeSegment_800b._800badae;
import static legend.game.Scus94491BpeSegment_800b._800bb0aa;
import static legend.game.Scus94491BpeSegment_800b._800bb0ac;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bdb94;
import static legend.game.Scus94491BpeSegment_800b._800bdb98;
import static legend.game.Scus94491BpeSegment_800b._800bdba4;
import static legend.game.Scus94491BpeSegment_800b._800bdba8;
import static legend.game.Scus94491BpeSegment_800b._800bdbb8;
import static legend.game.Scus94491BpeSegment_800b._800bdbe0;
import static legend.game.Scus94491BpeSegment_800b._800bdbe4;
import static legend.game.Scus94491BpeSegment_800b._800bdbe8;
import static legend.game.Scus94491BpeSegment_800b._800bdbec;
import static legend.game.Scus94491BpeSegment_800b._800bdbf8;
import static legend.game.Scus94491BpeSegment_800b._800bdc2c;
import static legend.game.Scus94491BpeSegment_800b._800bdc30;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bdc5c;
import static legend.game.Scus94491BpeSegment_800b._800bdf00;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b._800be5d8;
import static legend.game.Scus94491BpeSegment_800b._800be5f8;
import static legend.game.Scus94491BpeSegment_800b._800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.drgn0File6666Address_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.inventoryJoypadInput_800bdc44;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.mono_800bb0a8;
import static legend.game.Scus94491BpeSegment_800b.vibrationEnabled_800bb0a9;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public final class SItem {
  private SItem() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SItem.class);

  public static final Value _800fba7c = MEMORY.ref(4, 0x800fba7cL);

  public static final Value _800fbabc = MEMORY.ref(4, 0x800fbabcL);

  /** String "*" */
  public static final Value _800fbadc = MEMORY.ref(1, 0x800fbadcL);

  public static final ArrayRef<UnsignedIntRef> _800fbd08 = MEMORY.ref(4, 0x800fbd08L, ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _800fbd30 = MEMORY.ref(4, 0x800fbd30L, ArrayRef.of(UnsignedIntRef.class, 9, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _800fbd54 = MEMORY.ref(4, 0x800fbd54L, ArrayRef.of(UnsignedIntRef.class, 9, 4, UnsignedIntRef::new));

  public static final Value _80111cfc = MEMORY.ref(4, 0x80111cfcL);

  public static final Value _80111d20 = MEMORY.ref(4, 0x80111d20L);

  public static final Value _80111ff0 = MEMORY.ref(1, 0x80111ff0L);

  public static final Value ptrTable_80114070 = MEMORY.ref(4, 0x80114070L);

  /** Array of unknown data, stride 6 */
  public static final Value _80114130 = MEMORY.ref(1, 0x80114130L);

  public static final Value _80114160 = MEMORY.ref(1, 0x80114160L);

  public static final Value _80114258 = MEMORY.ref(1, 0x80114258L);

  public static final Value _801142d4 = MEMORY.ref(1, 0x801142d4L);

  public static final Value _8011c370 = MEMORY.ref(1, 0x8011c370L);

  public static final Value _8011c93c = MEMORY.ref(1, 0x8011c93cL);

  public static final Value _8011d734 = MEMORY.ref(4, 0x8011d734L);
  public static final Value _8011d738 = MEMORY.ref(4, 0x8011d738L);
  public static final Value _8011d73c = MEMORY.ref(4, 0x8011d73cL);
  public static final Value _8011d740 = MEMORY.ref(4, 0x8011d740L);
  public static final Value _8011d744 = MEMORY.ref(4, 0x8011d744L);

  public static final Value _8011d768 = MEMORY.ref(4, 0x8011d768L);

  public static final Value _8011d7c4 = MEMORY.ref(1, 0x8011d7c4L);

  public static final Value _8011dc88 = MEMORY.ref(1, 0x8011dc88L);

  public static final Value _8011dc8c = MEMORY.ref(4, 0x8011dc8cL);
  public static final Value _8011dc90 = MEMORY.ref(4, 0x8011dc90L);

  public static final Value _8011dc9c = MEMORY.ref(1, 0x8011dc9cL);

  public static final BoolRef _8011dcfc = MEMORY.ref(1, 0x8011dcfcL, BoolRef::new);

  public static final Value _8011dd00 = MEMORY.ref(4, 0x8011dd00L);
  public static final Value _8011dd04 = MEMORY.ref(4, 0x8011dd04L);

  public static final Value _8011dd10 = MEMORY.ref(4, 0x8011dd10L);

  public static final Value _8011dcc0 = MEMORY.ref(4, 0x8011dcc0L);

  public static final Value _8011e0b0 = MEMORY.ref(4, 0x8011e0b0L);
  public static final Value _8011e0b4 = MEMORY.ref(4, 0x8011e0b4L);
  public static final Value memcardSaveIndex_8011e0b8 = MEMORY.ref(4, 0x8011e0b8L);
  public static final Value _8011e0bc = MEMORY.ref(4, 0x8011e0bcL);
  public static final Value _8011e0c0 = MEMORY.ref(4, 0x8011e0c0L);

  public static final Value _8011e0c8 = MEMORY.ref(4, 0x8011e0c8L);

  public static final Value _8011e0d4 = MEMORY.ref(1, 0x8011e0d4L);

  public static final Value _8011e1e8 = MEMORY.ref(4, 0x8011e1e8L);

  @Method(0x800fc698L)
  public static long FUN_800fc698(final long a0) {
    if(a0 == -0x1L || a0 >= 9) {
      //LAB_800fc6a4
      return 0;
    }

    //LAB_800fc6ac
    long v1 = _800babc8.offset(1, 0x33eL).offset(a0 * 0x2cL).get();

    if(v1 >= 60L) {
      return 0;
    }

    final long a1 = v1 + 0x1L;

    v1 = switch((int)a0) {
      case 0    -> 0x801135e4L;
      case 1, 5 -> 0x801138c0L;
      case 2, 8 -> 0x80113aa8L;
      case 3    -> 0x801139b4L;
      case 4    -> 0x801136d8L;
      case 6    -> 0x801137ccL;
      case 7    -> 0x801134f0L;
      default -> throw new RuntimeException("Impossible");
    };

    //LAB_800fc70c
    return MEMORY.ref(4, v1).offset(a1 * 0x4L).get();
  }

  @Method(0x800fc78cL)
  public static long FUN_800fc78c(final long a0) {
    return a0 * 0xdL + 0x4eL;
  }

  @Method(0x800fc7a4L)
  public static long FUN_800fc7a4(final long a0) {
    return a0 * 0xdL + 0x50L;
  }

  @Method(0x800fc7bcL)
  public static long FUN_800fc7bc(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc7ecL)
  public static long FUN_800fc7ec(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc804L)
  public static long FUN_800fc804(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc814L)
  public static long FUN_800fc814(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc824L)
  public static long FUN_800fc824(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc838L)
  public static long FUN_800fc838(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc84cL)
  public static long FUN_800fc84c(final long a0) {
    return a0 * 0x48 + 0x10L;
  }

  @Method(0x800fc860L)
  public static long FUN_800fc860(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc880L)
  public static long FUN_800fc880(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc8a8L)
  public static long FUN_800fc8a8(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc8c0L)
  public static long FUN_800fc8c0(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc8dcL)
  public static long FUN_800fc8dc(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc8ecL)
  public static long FUN_800fc8ec(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc900L)
  public static long FUN_800fc900(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc944L)
  public static void fileLoadedCallback6665And6666(final Value address, final long size, final long param) {
    final Value a0 = address.deref(4);

    if(param == 0) {
      //LAB_800fc98c
      FUN_80022a94(a0.offset(0x83e0L));
      FUN_80022a94(a0);
      FUN_80022a94(a0.offset(0x6200L));
      FUN_80022a94(a0.offset(0x1_0460L));
      FUN_80022a94(a0.offset(0x1_0580L));
      FUN_800127cc(a0, 0, 0x1L);
    } else if(param == 0x1L) {
      //LAB_800fc9e4
      drgn0File6666Address_800bdc3c.setu(address);
    } else if(param == 0x4L) {
      //LAB_800fc978
      //LAB_800fc9f0
      _8011dd00.setu(address);
      _8011dd04.setu(size);
    }

    //LAB_800fc9fc
  }

  @Method(0x800fca0cL)
  public static void FUN_800fca0c(long a0, final long a1) {
    if(_800bdba4.get() != 0) {
      FUN_801033e8(_800bdba4.get());
      _800bdba4.setu(0);
    }

    //LAB_800fca40
    if(_800bdba8.get() != 0) {
      FUN_801033e8(_800bdba8.get());
      _800bdba8.setu(0);
    }

    //LAB_800fca60
    if(_800bdb94.get() != 0) {
      FUN_801033e8(_800bdb94.get());
      _800bdb94.setu(0);
    }

    //LAB_800fca80
    if(_800bdb98.get() != 0) {
      FUN_801033e8(_800bdb98.get());
      _800bdb98.setu(0);
    }

    //LAB_800fcaa4
    inventoryMenuState_800bdc28.setu(0x7bL);
    _800bdc2c.setu(a1);
    _800bdc30.setu(a0);
  }

  @Method(0x800fcad4L)
  public static void FUN_800fcad4() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long a4;
    long a5;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;

    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    LOGGER.info("Inventory menu state: %x", inventoryMenuState_800bdc28.get());

    switch((int)inventoryMenuState_800bdc28.get()) {
      case 0: // Initialize, loads some files (unknown contents)
        _800bdc34.setu(0);
        drgn0File6666Address_800bdc3c.setu(0);
        _800bdc5c.setu(0);
        _8011dc9c.setu(0);
        setWidthAndFlags(0x180L, 0);
        s0 = getMethodAddress(SItem.class, "fileLoadedCallback6665And6666", Value.class, long.class, long.class);
        loadDrgnBinFile(0, 6665L, 0, s0, 0, 0x5L);
        loadDrgnBinFile(0, 6666L, 0, s0, 0x1L, 0x3L);
        FUN_80110030(0);
        _800bdf00.setu(0x21L);

        if(mainCallbackIndex_8004dd20.get() == 0x8L) {
          _800bb0ac.setu(0x1L);
          _8011dc88.setu(0x1L);
        } else {
          //LAB_800fcbfc
          _800bb0ac.setu(0);
          _8011dc88.setu(_8005a368);
        }

        //LAB_800fcc10
        _8011d738.setu(0);
        _8011d73c.setu(0);
        inventoryMenuState_800bdc28.setu(0x1L);
        FUN_8002c150(0);
        break;

      case 0x1:
        if(drgn0File6666Address_800bdc3c.get() == 0) {
          break;
        }

        inventoryMenuState_800bdc28.setu(0x2L);
        v0 = _800bad64.getAddress();
        v0 = v0 - 0x19cL;
        _8011dcfc.set(0 < (_800bad64.offset(4, 0x4L).get() & 0x4L));
        MEMORY.ref(1, v0).offset(0x4e1L).and(0x1L);
        break;

      case 0x2:
        FUN_8002437c(0xffL);

        v1 = whichMenu_800bdc38.get();

        if(v1 == 0xeL) { // Load game screen
          //LAB_800fccd4
          inventoryMenuState_800bdc28.setu(0x25L);
          break;
        }

        if(v1 == 0x13L) {
          //LAB_800fccd0
          //LAB_800fccd4
          inventoryMenuState_800bdc28.setu(0x25L);
          break;
        }

        //LAB_800fccbc
        if(v1 == 0x18L) { // Character swap screen
          //LAB_800fcce0
          FUN_80023148();
          FUN_80103b10();
          inventoryMenuState_800bdc28.setu(0x8L);
          break;
        }

        //LAB_800fcd00
        FUN_80023148();
        FUN_80103b10();
        scriptStartEffect(0x2L, 0xaL);
        inventoryMenuState_800bdc28.setu(0x3L);
        break;

      case 0x3:
        FUN_8010376c(_80114130.getAddress(), 0, 0);
        _800bdbe0.setu(FUN_80103818(0x73L, 0x73L, 0x1dL, FUN_800fc78c(_8011d738.get())));
        FUN_80104b60(_800bdbe0.get());
        FUN_80101d10(_8011d738.get(), 0x4L, 0xffL);
        inventoryMenuState_800bdc28.setu(0x4L);
        break;

      case 0x4:
        if(FUN_8010ecec(_8011dc90.getAddress()) != 0) {
          if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
            playSound(0x3L);
            FUN_800fca0c(0x7dL, 0x1L);
          }

          //LAB_800fcdd4
          if(FUN_801040c0(_8011d738.getAddress(), 0x7L) != 0) {
            _8011d73c.setu(0);
            _800bdbe0.deref(4).offset(0x44L).setu(FUN_800fc78c(_8011d738.get()));
          }

          //LAB_800fce08
          v1 = inventoryJoypadInput_800bdc44.get();

          if((v1 & 0x2020L) != 0) {
            if((v1 & 0x2000L) != 0) {
              v0 = _8011d738.get();
              v1 = v0 + 0xaL;
            } else {
              //LAB_800fce30
              v1 = _8011d738.get();
            }

            //LAB_800fce34
            switch((int)v1) {
              case 0 -> {
                playSound(0x2L);
                _8011d734.setu(0);

                //LAB_800fcf3c
                FUN_800fca0c(0x14L, 0x1L);
              }

              case 0x1, 0xb -> {
                playSound(0x4L);
                _800bdbe4.setu(FUN_800fc900(_8011d73c.get()));
                inventoryMenuState_800bdc28.setu(0x5L);
              }

              case 0x2 -> {
                playSound(0x2L);
                _8011d734.setu(0);

                //LAB_800fcf3c
                FUN_800fca0c(0xcL, 0x1L);
              }

              case 0x3 -> {
                playSound(0x2L);
                _8011d734.setu(0);

                //LAB_800fcf3c
                FUN_800fca0c(0x17L, 0x1L);
              }

              case 0x4 -> {
                playSound(0x2L);

                //LAB_800fcf3c
                FUN_800fca0c(0x8L, 0x1L);
              }

              case 0x5 -> {
                playSound(0x4L);
                _8011d73c.setu(0);
                _800bdbe4.setu(0);
                FUN_8010f130(0, 0x1L);
                inventoryMenuState_800bdc28.setu(0x6L);
              }

              case 0x6 -> {
                if(_8011dc88.get() != 0) {
                  playSound(0x2L);

                  //LAB_800fcf3c
                  FUN_800fca0c(0x25L, 0x1L);
                } else {
                  //LAB_800fcf4c
                  playSound(0x28L);
                }
              }
            }
          }
        }

        //LAB_800fcf54
        //LAB_800fcf58
        if(_8011d738.get() == 0x1L) {
          FUN_80102064(0xffL, 0x6L);
        }

        //LAB_800fcf70
        FUN_80102484(0);

        //LAB_800fd344
        FUN_80101d10(_8011d738.get(), 0x4L, 0);
        break;

      case 0x5:
        if((inventoryJoypadInput_800bdc44.get() & 0x8040L) != 0) {
          playSound(0x3L);
          inventoryMenuState_800bdc28.setu(0x4L);
          FUN_800242e8(_800bdbe4.get());
        }

        //LAB_800fcfc0
        if(FUN_801040c0(_8011d73c.getAddress(), 0x4L) != 0) {
          _800bdbe4.deref(4).offset(0x44L).setu(FUN_800fc7a4(_8011d73c.get()) - 0x2L);
        }

        //LAB_800fcff0
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          playSound(0x2L);
          a0 = _8011d73c.get();
          if(a0 == 0) {
            //LAB_800fd058
            FUN_800fca0c(0x1aL, 0x2L);
          } else if(a0 == 0x1L) {
            //LAB_800fd04c
            //LAB_800fd058
            FUN_800fca0c(0x1fL, 0x2L);
          } else if(a0 == 0x2L) {
            //LAB_800fd034
            //LAB_800fd054
            //LAB_800fd058
            FUN_800fca0c(0x10L, 0x2L);
          } else if(a0 == 0x3L) {
            //LAB_800fd058
            FUN_800fca0c(0x23L, 0x2L);
          }
        }

        //LAB_800fd060
        FUN_80102484(0x1L);
        FUN_80102064(_8011d73c.get(), 0x4L);

        //LAB_800fd344
        FUN_80101d10(_8011d738.get(), 0x6L, 0);
        break;

      case 0x6:
        FUN_8010ecec(_8011dc90.getAddress());

        if(_8011dc90.offset(0x10L).get() >= 0x2L) {
          if(_800bdbe4.get() == 0) {
            _800bdbe4.setu(FUN_80103818(0x74L, 0x74L, FUN_800fc7bc(0) - 0x22L, FUN_800fc7ec(0) - 0x2L));
            FUN_80104b60(_800bdbe4.get());
            _800bdbe4.deref(1).offset(0x3cL).setu(0x20L);
          }

          //LAB_800fd100
          if(FUN_801040c0(_8011d73c.getAddress(), 0x4L) != 0) {
            v0 = FUN_800fc7ec(_8011d73c.get()) - 0x2L;
            _800bdbe4.deref(4).offset(0x44L).setu(v0);
          }

          //LAB_800fd130
          if((joypadDpad_8007a398.get() & 0x8000L) != 0) {
            playSound(0x2L);
            a0 = _8011d73c.get();

            //LAB_800fd174
            if(a0 == 0) {
              //LAB_800fd18c
              vibrationEnabled_800bb0a9.setu(0);
              FUN_8002379c();
            } else if(a0 == 0x1L) {
              //LAB_800fd1a0
              mono_800bb0a8.setu(0);
              FUN_8004c6f8(0);
            } else if(a0 == 0x2L) {
              //LAB_800fd1b8
              _800bb0aa.setu(0);
            } else if(a0 == 0x3L) {
              //LAB_800fd1c4
              if(_800babc8.offset(0x4e8L).get() != 0) {
                _800babc8.offset(0x4e8L).subu(0x1L);
              }
            }
          }

          //LAB_800fd1e0
          //LAB_800fd1e4
          if((joypadDpad_8007a398.get() & 0x2000L) != 0) {
            playSound(0x2L);
            v1 = _8011d73c.get();

            //LAB_800fd22c
            if(v1 == 0) {
              //LAB_800fd244
              vibrationEnabled_800bb0a9.setu(0x1L);
              FUN_8002bcc8(0, 0x100L);
              FUN_8002bda4(0, 0, 0x3cL);
              FUN_8002379c();
            } else if(v1 == 0x1L) {
              //LAB_800fd278
              mono_800bb0a8.setu(0x1L);
              FUN_8004c6f8(0x1L);
            } else if(v1 == 0x2L) {
              //LAB_800fd290
              _800bb0aa.setu(0x1L);
            } else if(v1 == 0x3L) {
              //LAB_800fd29c
              if(_800babc8.offset(0x4e8L).get() < 0x2L) {
                _800babc8.offset(0x4e8L).addu(0x1L);
              }
            }
          }

          //LAB_800fd2bc
          //LAB_800fd2c0
          if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
            playSound(0x2L);
            inventoryMenuState_800bdc28.setu(0x4L);
            _8011dc90.offset(1, 0xcL).addu(0x1L);
            FUN_800242e8(_800bdbe4.get());
          }

          //LAB_800fd30c
          FUN_8010214c(_8011d73c.get(), _800babc8.offset(1, 0x4e1L).get(), _800babc8.offset(1, 0x4e0L).get(), _800babc8.offset(1, 0x4e2L).get(), _800babc8.offset(4, 0x4e8L).get());
        }

        //LAB_800fd330
        FUN_80102484(0);

        //LAB_800fd344
        FUN_80101d10(_8011d738.get(), 0x4L, 0);
        break;

      case 0x7:
        FUN_8002437c(0xffL);
        FUN_8010376c(_80114130.getAddress(), 0, 0);
        _800bdbe0.setu(FUN_80103818(0x73L, 0x73L, 0x1dL, FUN_800fc78c(_8011d738.get())));
        _800bdbe4.setu(FUN_800fc900(_8011d73c.get()));
        FUN_80104b60(_800bdbe0.get());
        FUN_80101d10(_8011d738.get(), 0x4L, 0xffL);
        scriptStartEffect(0x2L, 0xaL);
        FUN_80102484(0x1L);
        inventoryMenuState_800bdc28.setu(0x5L);
        break;

      case 0x8:
        scriptStartEffect(0x2L, 0xaL);
        _8011d740.setu(0x1L);
        _8011d744.setu(0);
        inventoryMenuState_800bdc28.setu(0x9L);
        break;

      case 0x9:
        FUN_8002437c(0xffL);
        FUN_8010376c(_80114160.getAddress(), 0, 0);
        _800bdbe8.setu(FUN_80103818(0x7fL, 0x7fL, 0x10L, FUN_800fc84c(_8011d740.get())));
        FUN_80104b60(_800bdbe8.get());
        FUN_801024c4(0xffL);
        inventoryMenuState_800bdc28.setu(0xaL);
        break;

      case 0xa:
        FUN_801024c4(0);

        if(_800bb168.get() != 0) {
          break;
        }

        if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0) {
          if(_8011d740.get() >= 0x2L) {
            _8011d740.subu(0x1L);
            _800bdbe8.deref(4).offset(0x44L).setu(FUN_800fc84c(_8011d740.get()));
            playSound(0x1L);
          }
        }

        //LAB_800fd4e4
        //LAB_800fd4e8
        if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0) {
          if(_8011d740.get() < 0x2L) {
            _8011d740.addu(0x1L);
            _800bdbe8.deref(4).offset(0x44L).setu(FUN_800fc84c(_8011d740.get()));
            playSound(0x1L);
          }
        }

        //LAB_800fd52c
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          v1 = 0x800c_0000L;
        } else {
          v1 = 0x800c_0000L;
          v0 = 0x8012_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          v1 = v1 - 0x5438L;
          v0 = v0 << 2;
          v0 = v0 + v1;
          a0 = MEMORY.ref(4, v0).offset(0x88L).get();
          v0 = -0x1L;
          if(a0 == v0) {
            v0 = a0 << 1;
            //LAB_800fd590
            a0 = 0x28L;
            playSound(a0);
            v0 = 0x800c_0000L;
          }
          v0 = a0 << 1;
          v0 = v0 + a0;
          v0 = v0 << 2;
          v0 = v0 - a0;
          v0 = v0 << 2;
          v0 = v0 + v1;
          v0 = MEMORY.ref(4, v0).offset(0x330L).get();

          v0 = v0 & 0x20L;
          if(v0 != 0) {
            //LAB_800fd590
            a0 = 0x28L;
            playSound(a0);
            v0 = 0x800c_0000L;
          } else {
            //LAB_800fd5a0
            a0 = 0x2L;
            playSound(a0);
            s0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();

            v0 = FUN_800fc880(a0);
            a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
            s0 = v0;
            v0 = FUN_800fc8a8(a0);
            a0 = 0x80L;
            a1 = a0;
            a2 = s0;
            a3 = v0;
            v0 = FUN_80103818(a0, a1, a2, a3);
            a0 = v0;
            v0 = 0x800c_0000L;
            MEMORY.ref(4, v0).offset(-0x2414L).setu(a0);
            FUN_80104b60(a0);
            v1 = 0x800c_0000L;
            v0 = 0xbL;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          }
        }

        //LAB_800fd5f4
        v0 = 0x800c_0000L;

        //LAB_800fd5f8
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 == 0) {
          break;
        }

        a0 = 0x3L;
        playSound(a0);
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23c8L).get();
        v0 = 0x18L;
        if(v1 != v0) {
          a0 = 0x2L;
        } else {
          a0 = 0x2L;
          a0 = 0x7dL;
        }

        //LAB_800fd62c
        a1 = 0x5L;
        FUN_800fca0c(a0, a1);
        break;

      case 0xb:
        a0 = 0;
        FUN_801024c4(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x8000L;
        if(v0 != 0) {
          s0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
          t2 = a0 / 3;
          v1 = t2;
          v0 = v1 << 1;
          v0 = v0 + v1;
          v0 = a0 - v0;
          if((int)v0 > 0) {
            a0 = a0 - 0x1L;
            MEMORY.ref(4, s0).offset(-0x28bcL).setu(a0);
            v0 = FUN_800fc880(a0);
            a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
            s0 = 0x800c_0000L;
            v1 = MEMORY.ref(4, s0).offset(-0x2414L).get();
            MEMORY.ref(4, v1).offset(0x40L).setu(v0);
            v0 = FUN_800fc8a8(a0);
            v1 = MEMORY.ref(4, s0).offset(-0x2414L).get();
            a0 = 0x1L;
            MEMORY.ref(4, v1).offset(0x44L).setu(v0);
            playSound(a0);
          }
        }

        //LAB_800fd6b8
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x2000L;
        if(v0 != 0) {
          s0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
          t2 = a0 / 3;
          v1 = t2;
          v0 = v1 << 1;
          v0 = v0 + v1;
          v0 = a0 - v0;
          if((int)v0 < 0x2L) {
            a0 = a0 + 0x1L;
            MEMORY.ref(4, s0).offset(-0x28bcL).setu(a0);
            v0 = FUN_800fc880(a0);
            a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
            s0 = 0x800c_0000L;
            v1 = MEMORY.ref(4, s0).offset(-0x2414L).get();
            MEMORY.ref(4, v1).offset(0x40L).setu(v0);
            v0 = FUN_800fc8a8(a0);
            v1 = MEMORY.ref(4, s0).offset(-0x2414L).get();
            a0 = 0x1L;
            MEMORY.ref(4, v1).offset(0x44L).setu(v0);
            playSound(a0);
          }
        }

        //LAB_800fd730
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x1000L;
        if(v0 == 0) {
          s0 = 0x8012_0000L;
        } else {
          s0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();

          v0 = 0x800c_0000L;
          if((int)a0 >= 0x3L) {
            a0 = a0 - 0x3L;
            MEMORY.ref(4, s0).offset(-0x28bcL).setu(a0);
            v0 = FUN_800fc880(a0);
            a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
            s0 = 0x800c_0000L;
            v1 = MEMORY.ref(4, s0).offset(-0x2414L).get();
            MEMORY.ref(4, v1).offset(0x40L).setu(v0);
            v0 = FUN_800fc8a8(a0);
            v1 = MEMORY.ref(4, s0).offset(-0x2414L).get();
            a0 = 0x1L;
            MEMORY.ref(4, v1).offset(0x44L).setu(v0);
            playSound(a0);
          }
        }

        //LAB_800fd78c
        v0 = 0x800c_0000L;

        //LAB_800fd790
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x4000L;
        if(v0 == 0) {
          s0 = 0x8012_0000L;
        } else {
          s0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();

          if((int)a0 < 0x3L) {
            a0 = a0 + 0x3L;
            MEMORY.ref(4, s0).offset(-0x28bcL).setu(a0);
            v0 = FUN_800fc880(a0);
            a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
            s0 = 0x800c_0000L;
            v1 = MEMORY.ref(4, s0).offset(-0x2414L).get();
            MEMORY.ref(4, v1).offset(0x40L).setu(v0);
            v0 = FUN_800fc8a8(a0);
            v1 = MEMORY.ref(4, s0).offset(-0x2414L).get();
            a0 = 0x1L;
            MEMORY.ref(4, v1).offset(0x44L).setu(v0);
            playSound(a0);
          }
        }

        //LAB_800fd7e4
        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 != 0) {
          a0 = 0x3L;
          playSound(a0);
          v0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x2414L).get();

          FUN_800242e8(a0);
          v1 = 0x800c_0000L;
          v0 = 0xaL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fd820
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          v0 = 0x800c_0000L;
          break;
        }
        v0 = 0x800c_0000L;
        s1 = 0x8012_0000L;
        v1 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        s2 = v0 - 0x2408L;
        v1 = v1 << 2;
        v1 = v1 + s2;
        v1 = MEMORY.ref(4, v1).offset(0x0L).get();
        v0 = -0x1L;
        if(v1 == v0) {
          //LAB_800fd888
          playSound(0x28L);
          break;
        }
        s0 = _800babc8.getAddress();
        v0 = s0 + v1 * 0x2cL;
        v0 = MEMORY.ref(4, v0).offset(0x330L).get();

        v0 = v0 & 0x2L;
        if(v0 == 0) {
          //LAB_800fd888
          playSound(0x28L);
          break;
        }

        //LAB_800fd898
        a0 = 0x2L;
        playSound(a0);
        v0 = 0x8012_0000L;
        a1 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v1 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        a0 = a0 << 2;
        a0 = a0 + s0;
        v1 = v1 << 2;
        v1 = v1 + s2;
        a3 = MEMORY.ref(4, a0).offset(0x88L).get();
        a2 = MEMORY.ref(4, v1).offset(0x0L).get();
        v0 = 0x9L;
        MEMORY.ref(4, a1).offset(-0x23d8L).setu(v0);
        MEMORY.ref(4, a0).offset(0x88L).setu(a2);
        MEMORY.ref(4, v1).offset(0x0L).setu(a3);
        break;

      case 0xc:
        FUN_80023148();
        a0 = 0x2L;
        a1 = 0xaL;
        scriptStartEffect(a0, a1);
        a0 = 0xffL;
        FUN_8002437c(a0);

      case 0xd:
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x28bcL).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x28c0L).setu(0);

      case 0xe:
        a0 = 0;
        FUN_8002437c(a0);
        a0 = 0x8011_0000L;
        a0 = a0 + 0x4180L;
        a1 = 0;
        a2 = a1;
        FUN_8010376c(a0, a1, a2);
        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x2418L).get();

        if(v0 == 0) {
          a0 = 0x1L;
          v0 = FUN_800fc824(a0);
          a0 = 0x79L;
          a1 = a0;
          a2 = v0;
          a3 = 0;
          v0 = FUN_80103818(a0, a1, a2, a3);
          MEMORY.ref(4, s0).offset(-0x2418L).setu(v0);
          a0 = v0;
          FUN_80104b60(a0);
        }

        //LAB_800fd964
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28c0L).get();

        v0 = FUN_800fc804(a0);
        v1 = MEMORY.ref(4, s0).offset(-0x2418L).get();
        s0 = 0x8012_0000L;
        MEMORY.ref(4, v1).offset(0x44L).setu(v0);
        v1 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();
        v1 = v1 - 0x2448L;
        v0 = v0 << 2;
        v0 = v0 + v1;
        a0 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = FUN_801045fc(a0);
        a3 = 0xffL;
        v1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();
        a1 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
        a2 = MEMORY.ref(4, v1).offset(-0x28bcL).get();
        v1 = 0x8012_0000L;
        MEMORY.ref(4, v1).offset(-0x28b0L).setu(v0);
        FUN_80102660(a0, a1, a2, a3);
        v1 = 0x800c_0000L;
        v0 = 0xfL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0xf:
        s4 = 0x8012_0000L;
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s4).offset(-0x28ccL).get();
        a1 = MEMORY.ref(1, v0).offset(-0x283cL).get();
        s2 = 0x8012_0000L;
        FUN_801034cc(a0, a1);
        s3 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s4).offset(-0x28ccL).get();
        a1 = MEMORY.ref(4, s2).offset(-0x28c0L).get();
        a2 = MEMORY.ref(4, s3).offset(-0x28bcL).get();
        a3 = 0;
        FUN_80102660(a0, a1, a2, a3);
        s1 = s2 - 0x28c0L;
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x4e98L).get();

        if(v0 != 0) {
          s0 = s3 - 0x28bcL;
          break;
        }
        s0 = s3 - 0x28bcL;
        a0 = s1;
        a1 = s0;
        a2 = 0x4L;
        v0 = 0x8012_0000L;
        a3 = MEMORY.ref(4, v0).offset(-0x28b0L).get();
        v0 = 0x1L;
        a4 = v0;
        v0 = FUN_80103f00(a0, a1, a2, a3, a4);
        s0 = 0x800c_0000L;
        if(v0 != 0) {
          a0 = MEMORY.ref(4, s2).offset(-0x28c0L).get();

          v0 = FUN_800fc804(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();

          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
        }

        //LAB_800fda58
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 != 0) {
          a0 = 0x3L;
          playSound(a0);
          a0 = 0x2L;
          a1 = 0x6L;
          FUN_800fca0c(a0, a1);
        }

        //LAB_800fda80
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          v0 = 0x800c_0000L;
        } else {
          v0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, s2).offset(-0x28c0L).get();
          v1 = MEMORY.ref(4, s3).offset(-0x28bcL).get();
          v0 = MEMORY.ref(2, v0).offset(-0x5254L).getSigned();
          a0 = a0 + v1;
          if((int)a0 >= (int)v0) {
            v0 = 0x8012_0000L;
          } else {
            v0 = 0x8012_0000L;
            s0 = v0 - 0x2838L;
            v0 = a0 << 2;
            a0 = v0 + s0;
            v1 = MEMORY.ref(1, a0).offset(0x0L).get();
            v0 = 0xffL;
            s1 = 0x800c_0000L;
            if(v1 != v0) {
              v0 = MEMORY.ref(4, s4).offset(-0x28ccL).get();
              s1 = s1 - 0x2448L;
              v0 = v0 << 2;
              v0 = v0 + s1;
              a1 = MEMORY.ref(4, v0).offset(0x0L).get();
              a0 = v1;
              v0 = FUN_80103a5c(a0, a1);
              v1 = MEMORY.ref(4, s2).offset(-0x28c0L).get();
              a0 = MEMORY.ref(4, s3).offset(-0x28bcL).get();

              v1 = v1 + a0;
              v1 = v1 << 2;
              v1 = v1 + s0;
              a0 = MEMORY.ref(1, v1).offset(0x1L).get();
              s0 = v0;
              v0 = FUN_800233d8(a0);
              a0 = s0 & 0xffffL;
              v0 = FUN_80023484(a0);
              a0 = 0x2L;
              playSound(a0);
              a0 = 0;
              FUN_80110030(a0);
              v0 = MEMORY.ref(4, s4).offset(-0x28ccL).get();

              v0 = v0 << 2;
              v0 = v0 + s1;
              a0 = MEMORY.ref(1, v0).offset(0x0L).get();
              a1 = 0;
              v0 = FUN_80022b50(a0, a1);
              v0 = MEMORY.ref(4, s4).offset(-0x28ccL).get();

              v0 = v0 << 2;
              v0 = v0 + s1;
              a0 = MEMORY.ref(1, v0).offset(0x0L).get();
              a1 = 0;
              v0 = FUN_80022c08(a0, a1);
              v1 = 0x800c_0000L;
              v0 = 0xeL;
              MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            } else {
              //LAB_800fdb6c
              a0 = 0x28L;
              playSound(a0);
            }
          }
        }

        //LAB_800fdb74
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x10L;
        if(v0 == 0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          a0 = 0x2L;
          playSound(a0);
          a0 = 0x4c0L;
          a1 = 0;
          a2 = a1;
          v0 = addToLinkedListTail(a0);
          a0 = 0x4c0L;
          a1 = 0;
          a2 = a1;
          s0 = 0x8012_0000L;
          MEMORY.ref(4, s0).offset(-0x2348L).setu(v0);
          v0 = addToLinkedListTail(a0);
          a0 = 0x1L;
          s1 = s0 - 0x2348L;
          MEMORY.ref(4, s1).offset(0x4L).setu(v0);
          v0 = FUN_80104738(a0);
          a1 = 0x800c_0000L;
          a1 = a1 - 0x5250L;
          a0 = MEMORY.ref(4, s0).offset(-0x2348L).get();
          a2 = MEMORY.ref(2, a1).offset(-0x4L).getSigned();
          v1 = 0x8012_0000L;
          MEMORY.ref(4, v1).offset(-0x28acL).setu(v0);
          FUN_80023a2c(a0, a1, a2);
          a1 = 0;
          a0 = MEMORY.ref(4, s0).offset(-0x2348L).get();
          a2 = a1;
          removeFromLinkedList(a0);
          a1 = 0;
          a0 = MEMORY.ref(4, s1).offset(0x4L).get();
          a2 = a1;
          removeFromLinkedList(a0);
          v1 = 0x800c_0000L;
          v0 = 0xeL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          v0 = 0x8012_0000L;
        }

        //LAB_800fdc18
        a1 = MEMORY.ref(1, v0).offset(-0x283cL).get();
        a0 = 0x8012_0000L;
        a0 = a0 - 0x28ccL;
        v0 = FUN_8010415c(a0, a1);
        v1 = 0x800c_0000L;
        if(v0 != 0) {
          v0 = 0xdL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x10:
      case 0x1f:
        a0 = 0x2L;
        a1 = 0xaL;
        scriptStartEffect(a0, a1);
        a0 = 0xffL;
        FUN_8002437c(a0);
        a0 = 0x8011_0000L;
        a0 = a0 + 0x41c4L;
        a1 = 0;
        a2 = a1;
        FUN_8010376c(a0, a1, a2);
        a0 = 0x4c0L;
        a1 = 0;
        a2 = a1;
        v0 = addToLinkedListTail(a0);
        a0 = 0x4c0L;
        a1 = 0;
        a2 = a1;
        s0 = 0x8012_0000L;
        MEMORY.ref(4, s0).offset(-0x2348L).setu(v0);
        s0 = s0 - 0x2348L;
        v0 = addToLinkedListTail(a0);
        MEMORY.ref(4, s0).offset(0x4L).setu(v0);
        FUN_80023148();
        a0 = 0;
        v0 = 0x8012_0000L;
        s2 = 0x8012_0000L;
        s1 = 0x8012_0000L;
        s0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x28b4L).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, s2).offset(-0x28b8L).setu(0);
        MEMORY.ref(4, s1).offset(-0x28bcL).setu(0);
        MEMORY.ref(4, s0).offset(-0x28c0L).setu(0);
        MEMORY.ref(4, v0).offset(-0x28ccL).setu(0);
        v0 = FUN_800fc824(a0);
        a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();
        s0 = v0;
        v0 = FUN_800fc814(a0);
        a0 = 0x76L;
        a1 = a0;
        a2 = s0;
        a3 = v0 + 0x20L;
        v0 = FUN_80103818(a0, a1, a2, a3);
        a0 = v0;
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x2418L).setu(a0);
        FUN_80104b60(a0);
        a2 = 0xffL;
        a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        a1 = MEMORY.ref(4, s2).offset(-0x28b8L).get();
        a3 = a2;
        FUN_80102840(a0, a1, a2, a3);
        a1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, a1).offset(-0x23d8L).get();
        v0 = 0x10L;
        if(v1 != v0) {
          a0 = 0x20L;
        } else {
          a0 = 0x20L;
          a0 = 0x11L;
        }

        //LAB_800fdd24
        MEMORY.ref(4, a1).offset(-0x23d8L).setu(a0);
        break;

      case 0x11:
        v0 = 0x8012_0000L;
        s0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
        a0 = 0;
        s1 = v1 + v0;
        v0 = FUN_80104738(a0);
        v1 = 0x8012_0000L;
        a1 = MEMORY.ref(4, v1).offset(-0x28b8L).get();
        v1 = 0x8012_0000L;
        MEMORY.ref(4, v1).offset(-0x28acL).setu(v0);
        v1 = 0x800c_0000L;
        v0 = 0x12L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        v1 = 0x8012_0000L;
        v0 = 0x8012_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
        a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
        v1 = v1 - 0x2348L;

        //LAB_800fe08c
        v0 = v0 << 2;
        v0 = v0 + v1;
        v1 = MEMORY.ref(4, v0).offset(0x0L).get();
        v0 = s1 << 2;
        v0 = v0 + v1;
        a2 = MEMORY.ref(1, v0).offset(0x0L).get();
        a3 = 0;
        FUN_80102840(a0, a1, a2, a3);
        break;

      case 0x20:
        v0 = 0x8012_0000L;
        s0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = MEMORY.ref(4, s0).offset(-0x28b8L).get();
        a0 = 0x1L;
        s1 = v1 + v0;
        v0 = FUN_80104738(a0);
        v1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v1).offset(-0x28bcL).get();
        v1 = 0x8012_0000L;
        MEMORY.ref(4, v1).offset(-0x28acL).setu(v0);
        v1 = 0x800c_0000L;
        v0 = 0x21L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        v1 = 0x8012_0000L;
        v0 = 0x8012_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
        a1 = MEMORY.ref(4, s0).offset(-0x28b8L).get();
        v1 = v1 - 0x2348L;

        //LAB_800fe08c
        v0 = v0 << 2;
        v0 = v0 + v1;
        v1 = MEMORY.ref(4, v0).offset(0x0L).get();
        v0 = s1 << 2;
        v0 = v0 + v1;
        a2 = MEMORY.ref(1, v0).offset(0x0L).get();
        a3 = 0;
        FUN_80102840(a0, a1, a2, a3);
        break;

      case 0x12:
      case 0x21:
        v0 = 0x8012_0000L;
        v1 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x28ccL).get();

        a1 = v0 - 0x28b0L;
        if(v1 != 0) {
          v0 = 0x800c_0000L;
          a0 = MEMORY.ref(2, v0).offset(-0x5252L).getSigned();
          v0 = 0x8012_0000L;
        } else {
          //LAB_800fddf4
          v0 = 0x800c_0000L;
          v1 = 0x8012_0000L;
          a0 = MEMORY.ref(2, v0).offset(-0x5254L).getSigned();
          v0 = MEMORY.ref(4, v1).offset(-0x28acL).get();

          a0 = a0 + v0;
          v0 = 0x8012_0000L;
        }

        //LAB_800fde10
        v0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();

        MEMORY.ref(4, a1).offset(0x0L).setu(a0);
        if(v0 != 0) {
          v0 = 0x8012_0000L;
          v1 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          v0 = MEMORY.ref(4, v1).offset(-0x28b8L).get();
          s1 = a0 + v0;
        } else {
          //LAB_800fde38
          v0 = 0x8012_0000L;
          v1 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          v0 = MEMORY.ref(4, v1).offset(-0x28bcL).get();

          s1 = a0 + v0;
        }

        //LAB_800fde50
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 == 0) {
          v0 = 0x800c_0000L;
        } else {
          v0 = 0x800c_0000L;
          a0 = 0x3L;
          playSound(a0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2460L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2464L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2468L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x246cL).setu(0);
          v0 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x23c8L).get();
          v0 = 0x24L;
          if(v1 != v0) {
            a0 = 0x7L;
          } else {
            a0 = 0x7L;
            a0 = 0x13L;
          }

          //LAB_800fdea8
          a1 = 0x8L;
          FUN_800fca0c(a0, a1);
          v0 = 0x800c_0000L;
        }

        //LAB_800fdeb4
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x10L;
        if(v0 == 0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          a0 = 0x2L;
          playSound(a0);
          v1 = 0x8012_0000L;
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
          v1 = v1 - 0x2348L;
          v0 = a0 << 2;
          v1 = v0 + v1;
          if(a0 != 0) {
            v0 = 0x800c_0000L;
            a1 = v0 - 0x514fL;
          } else {
            //LAB_800fdef8
            v0 = 0x800c_0000L;
            a1 = v0 - 0x5250L;
          }

          //LAB_800fdf00
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v1).offset(0x0L).get();
          a2 = MEMORY.ref(4, v0).offset(-0x28b0L).get();

          FUN_80023a2c(a0, a1, a2);
          v0 = 0x8012_0000L;
        }

        //LAB_800fdf18
        v1 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x28ccL).get();

        a0 = v0 - 0x28c0L;
        if(v1 != 0) {
          v0 = 0x8012_0000L;
          a1 = v0 - 0x28b8L;
        } else {
          //LAB_800fdf38
          v0 = 0x8012_0000L;
          a1 = v0 - 0x28bcL;
        }

        //LAB_800fdf40
        a2 = 0x7L;
        v0 = 0x8012_0000L;
        a3 = MEMORY.ref(4, v0).offset(-0x28b0L).get();
        v0 = 0x1L;
        a4 = v0;
        v0 = FUN_80103f00(a0, a1, a2, a3, a4);
        if(v0 == 0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();

          v0 = FUN_800fc814(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();
          v0 = v0 + 0x20L;
          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
        }

        //LAB_800fdf7c
        s0 = 0x8012_0000L;
        a0 = s0 - 0x28ccL;
        a1 = 0x2L;
        v0 = FUN_8010415c(a0, a1);
        if(v0 == 0) {
          v0 = 0x800c_0000L;
        } else {
          v0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();

          v0 = FUN_800fc824(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();

          MEMORY.ref(4, v1).offset(0x40L).setu(v0);
          v0 = 0x800c_0000L;
        }

        //LAB_800fdfb4
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          s2 = 0x800c_0000L;
        } else {
          s2 = 0x800c_0000L;
          v1 = MEMORY.ref(4, s2).offset(-0x23d8L).get();
          v0 = 0x21L;
          if(v1 == v0) {
            v0 = 0x8012_0000L;
            v1 = 0x8012_0000L;
            v0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();
            v1 = v1 - 0x2348L;
            v0 = v0 << 2;
            v0 = v0 + v1;
            v1 = MEMORY.ref(4, v0).offset(0x0L).get();
            v0 = s1 << 2;
            v1 = v0 + v1;
            v0 = MEMORY.ref(2, v1).offset(0x2L).get();

            v0 = v0 & 0x2000L;
            if(v0 != 0) {
              //LAB_800fe064
              a0 = 0x28L;
              playSound(a0);
            } else {
              v1 = MEMORY.ref(1, v1).offset(0x0L).get();

              if(v1 == 0xffL) {
                //LAB_800fe064
                a0 = 0x28L;
                playSound(a0);
              } else {
                a0 = 0x2L;
                playSound(a0);
                a0 = 0;
                v0 = 0x8012_0000L;
                MEMORY.ref(4, v0).offset(-0x28b4L).setu(0);
                v0 = FUN_800fc860(a0);
                a0 = 0x7dL;
                a1 = a0;
                a2 = 0x13aL;
                a3 = v0;
                v0 = FUN_80103818(a0, a1, a2, a3);
                a0 = v0;
                v0 = 0x800c_0000L;
                MEMORY.ref(4, v0).offset(-0x23e0L).setu(a0);
                FUN_80104b60(a0);
                v0 = 0x22L;
                MEMORY.ref(4, s2).offset(-0x23d8L).setu(v0);
              }
            }
          }
        }

        //LAB_800fe06c
        v0 = 0x8012_0000L;

        //LAB_800fe070
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        v0 = 0x8012_0000L;
        v1 = 0x8012_0000L;
        a1 = MEMORY.ref(4, v0).offset(-0x28b8L).get();
        v0 = 0x8012_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
        v1 = v1 - 0x2348L;

        //LAB_800fe08c
        v0 = v0 << 2;
        v0 = v0 + v1;
        v1 = MEMORY.ref(4, v0).offset(0x0L).get();
        v0 = s1 << 2;
        v0 = v0 + v1;
        a2 = MEMORY.ref(1, v0).offset(0x0L).get();
        a3 = 0;
        FUN_80102840(a0, a1, a2, a3);
        break;

      case 0x22:
        v0 = 0x8012_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();

        if(v0 != 0) {
          v0 = 0x8012_0000L;
          v1 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          v0 = MEMORY.ref(4, v1).offset(-0x28b8L).get();
          s1 = a0 + v0;
        } else {
          //LAB_800fe0dc
          v0 = 0x8012_0000L;
          v1 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          v0 = MEMORY.ref(4, v1).offset(-0x28bcL).get();

          s1 = a0 + v0;
        }

        //LAB_800fe0f0
        a0 = 0x8012_0000L;
        a0 = a0 - 0x372cL;
        a1 = 0xc0L;
        a2 = 0xb4L;
        a3 = 0x4L;
        FUN_80103cc4(a0, a1, a2, a3);
        a0 = 0;
        v0 = FUN_800fc860(a0);
        a3 = 0x6L;
        v0 = v0 + 0x2L;
        a2 = v0 & 0xffffL;
        v0 = 0x8012_0000L;
        a0 = v0 - 0x3df4L;
        s0 = 0x8012_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x28b4L).get();

        if(v0 != 0) {
          s2 = s0 - 0x28b4L;
        } else {
          s2 = s0 - 0x28b4L;
          a3 = 0x5L;
        }

        //LAB_800fe13c
        a1 = 0x148L;
        FUN_80103e90(a0, a1, a2, a3);
        a0 = 0x1L;
        v0 = FUN_800fc860(a0);
        a3 = 0x6L;
        v0 = v0 + 0x2L;
        a2 = v0 & 0xffffL;
        v0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, s0).offset(-0x28b4L).get();

        a0 = v0 - 0x3decL;
        if(v1 != 0) {
          a3 = 0x5L;
        }

        //LAB_800fe170
        a1 = 0x148L;
        FUN_80103e90(a0, a1, a2, a3);
        a0 = s2;
        v0 = FUN_801041d8(a0);
        v1 = v0;
        if(v1 == 0x1L) {
          a2 = 0xffL;

          //LAB_800fe1bc
          a0 = MEMORY.ref(4, s0).offset(-0x28b4L).get();

          v0 = FUN_800fc860(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x23e0L).get();
          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
        } else if(v1 == 0x2L) {
          //LAB_800fe1d8
          a1 = 0x8012_0000L;
          v0 = MEMORY.ref(4, a1).offset(-0x28b0L).get();
          s0 = s1;
          if((int)s0 >= (int)v0) {
            v0 = 0x8012_0000L;
          } else {
            v0 = 0x8012_0000L;
            a3 = v0 - 0x2348L;
            a2 = 0x8012_0000L;
            a0 = s0 << 2;

            //LAB_800fe1fc
            do {
              v0 = MEMORY.ref(4, a2).offset(-0x28ccL).get();
              s0 = s0 + 0x1L;
              v0 = v0 << 2;
              v0 = v0 + a3;
              v1 = MEMORY.ref(4, v0).offset(0x0L).get();
              v0 = MEMORY.ref(4, a1).offset(-0x28b0L).get();
              a0 = a0 + v1;
              t3 = MEMORY.ref(4, a0).offset(0x4L).get();
              MEMORY.ref(4, a0).setu(t3);
              a0 = s0 << 2;
            } while((int)s0 < (int)v0);
          }

          //LAB_800fe238
          v1 = 0x8012_0000L;
          v0 = MEMORY.ref(4, v1).offset(-0x28b0L).get();

          v0 = v0 - 0x1L;
          MEMORY.ref(4, v1).offset(-0x28b0L).setu(v0);
          v1 = 0x8012_0000L;
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
          v1 = v1 - 0x2348L;
          v0 = a0 << 2;
          v1 = v0 + v1;
          if(a0 != 0) {
            v0 = 0x800c_0000L;
            a1 = v0 - 0x514fL;
          } else {
            //LAB_800fe274
            v0 = 0x800c_0000L;
            a1 = v0 - 0x5250L;
          }

          //LAB_800fe27c
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v1).offset(0x0L).get();
          a2 = MEMORY.ref(4, v0).offset(-0x28b0L).get();

          FUN_800239e0(a0, a1, a2);
          FUN_80023148();
          v0 = 0x800c_0000L;

          //LAB_800fe29c
          a0 = MEMORY.ref(4, v0).offset(-0x23e0L).get();

          FUN_800242e8(a0);
          v1 = 0x800c_0000L;
          v0 = 0x21L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        } else if(v1 == 0x4L) {
          //LAB_800fe29c
          a0 = MEMORY.ref(4, v0).offset(-0x23e0L).get();

          FUN_800242e8(a0);
          v1 = 0x800c_0000L;
          v0 = 0x21L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fe2b4
        a2 = 0xffL;
        v0 = 0x8012_0000L;

        //LAB_800fe2bc
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        v0 = 0x8012_0000L;
        a1 = MEMORY.ref(4, v0).offset(-0x28b8L).get();
        a3 = 0x1L;
        FUN_80102840(a0, a1, a2, a3);
        break;

      case 0x13:
        v1 = 0x800c_0000L;
        v0 = 0x1L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        v1 = 0x800c_0000L;
        v0 = 0x9L;
        MEMORY.ref(4, v1).offset(-0x23c8L).setu(v0);
        break;

      case 0x14:
        a0 = 0x2L;
        a1 = 0xaL;
        scriptStartEffect(a0, a1);
        a0 = 0xffL;
        FUN_8002437c(a0);
        v1 = 0x800c_0000L;
        //LAB_800fe3c4
        v0 = 0x15L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x15:
        a0 = 0;
        FUN_8002437c(a0);
        a0 = 0x8011_0000L;
        a0 = a0 + 0x41a4L;
        a1 = 0;
        a2 = a1;
        FUN_8010376c(a0, a1, a2);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
        a1 = 0xffL;
        FUN_801027bc(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x16L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x16:
        s0 = 0x8012_0000L;
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();
        a1 = MEMORY.ref(1, s1).offset(-0x283cL).get();

        FUN_801034cc(a0, a1);
        a0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();
        a1 = 0;
        FUN_801027bc(a0, a1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x4e98L).get();

        if(v0 != 0) {
          s0 = s0 - 0x28ccL;
          break;
        }
        s0 = s0 - 0x28ccL;
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 != 0) {
          a0 = 0x3L;
          playSound(a0);
          a0 = 0x2L;
          a1 = 0x7L;
          FUN_800fca0c(a0, a1);
        }

        //LAB_800fe3b0
        a1 = MEMORY.ref(1, s1).offset(-0x283cL).get();
        a0 = s0;
        v0 = FUN_8010415c(a0, a1);
        v1 = 0x800c_0000L;
        if(v0 != 0) {
          //LAB_800fe3c4
          v0 = 0x15L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x17:
        a0 = 0x2L;
        a1 = 0xaL;
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x28c0L).setu(0);
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x2458L).setu(0);
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x245cL).setu(0);
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x2414L).setu(0);
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x2418L).setu(0);
        scriptStartEffect(a0, a1);
        a0 = 0xffL;
        FUN_8002437c(a0);
        v1 = 0x800c_0000L;
        v0 = 0x18L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x18:
        a0 = 0;
        FUN_8002437c(a0);
        v1 = 0x800c_0000L;
        s2 = 0x8012_0000L;
        s3 = v1 - 0x2448L;
        s0 = 0x8012_0000L;
        v0 = MEMORY.ref(4, s2).offset(-0x28ccL).get();
        s1 = s0 - 0x1f68L;
        v0 = v0 << 2;
        v0 = v0 + s3;
        a0 = MEMORY.ref(4, v0).offset(0x0L).get();
        a1 = s1;
        v0 = FUN_801049b4(a0, a1);
        v0 = MEMORY.ref(1, s0).offset(-0x1f68L).get();
        s0 = 0xffL;
        if(v0 == s0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();

          v0 = FUN_800fc838(a0);
          a0 = 0x75L;
          a1 = a0;
          a2 = 0x27L;
          a3 = v0 - 0x4L;
          v0 = FUN_80103818(a0, a1, a2, a3);
          a0 = v0;
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2418L).setu(a0);
          FUN_80104b60(a0);
        }

        //LAB_800fe490
        a0 = 0x45L;
        a1 = a0;
        a2 = 0;
        a3 = a2;
        v0 = FUN_80103818(a0, a1, a2, a3);
        a0 = 0x46L;
        a1 = a0;
        a2 = 0xc0L;
        a3 = 0;
        v0 = FUN_80103818(a0, a1, a2, a3);
        v0 = 0x8012_0000L;
        a3 = 0x800c_0000L;
        a0 = MEMORY.ref(4, s2).offset(-0x28ccL).get();
        a1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = a0 << 2;
        v0 = v0 + s3;
        v1 = MEMORY.ref(4, v0).offset(0x0L).get();
        a3 = a3 - 0x5438L;
        v0 = v1 << 1;
        v0 = v0 + v1;
        v0 = v0 << 2;
        v0 = v0 - v1;
        v0 = v0 << 2;
        v0 = v0 + a3;
        a3 = MEMORY.ref(1, v0).offset(0x345L).get();
        a2 = s1;
        a4 = s0;
        FUN_80102ad8(a0, a1, a2, a3, a4);
        v1 = 0x800c_0000L;
        v0 = 0x19L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x19:
        s1 = 0x8012_0000L;
        s2 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28ccL).get();
        a1 = MEMORY.ref(1, s2).offset(-0x283cL).get();
        s3 = 0x8012_0000L;
        FUN_801034cc(a0, a1);
        s6 = s3 - 0x1f68L;
        s4 = 0x8012_0000L;
        a3 = 0x800c_0000L;
        v0 = 0x800c_0000L;
        s5 = v0 - 0x2448L;
        a0 = MEMORY.ref(4, s1).offset(-0x28ccL).get();
        s7 = a3 - 0x5438L;
        v0 = a0 << 2;
        v0 = v0 + s5;
        v1 = MEMORY.ref(4, v0).offset(0x0L).get();
        a1 = MEMORY.ref(4, s4).offset(-0x28c0L).get();
        v0 = v1 << 1;
        v0 = v0 + v1;
        v0 = v0 << 2;
        v0 = v0 - v1;
        v0 = v0 << 2;
        v0 = v0 + s7;
        a3 = MEMORY.ref(1, v0).offset(0x345L).get();
        a2 = s6;
        a4 = 0;
        FUN_80102ad8(a0, a1, a2, a3, a4);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x4e98L).get();

        if(v0 != 0) {
          s0 = s1 - 0x28ccL;
          break;
        }
        s0 = s1 - 0x28ccL;
        a1 = MEMORY.ref(1, s2).offset(-0x283cL).get();
        a0 = s0;
        v0 = FUN_8010415c(a0, a1);
        if(v0 == 0) {
          v0 = 0x800c_0000L;
        } else {
          v0 = 0x800c_0000L;
          v1 = 0x800c_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x2418L).get();
          v0 = 0x18L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          FUN_800242e8(a0);
        }

        //LAB_800fe5b8
        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 != 0) {
          a0 = 0x3L;
          playSound(a0);
          a0 = 0x2L;
          a1 = 0x9L;
          FUN_800fca0c(a0, a1);
        }

        //LAB_800fe5e4
        v0 = MEMORY.ref(1, s3).offset(-0x1f68L).get();
        v1 = 0xffL;
        if(v0 == v1) {
          break;
        }

        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          s0 = 0x8012_0000L;
        } else {
          s0 = 0x8012_0000L;
          v0 = MEMORY.ref(4, s4).offset(-0x28c0L).get();

          v0 = v0 << 1;
          v0 = v0 + s6;
          a1 = MEMORY.ref(1, v0).offset(0x0L).get();

          a0 = 0x2L;
          if(a1 != v1) {
            v0 = MEMORY.ref(4, s1).offset(-0x28ccL).get();

            v0 = v0 << 2;
            v0 = v0 + s5;
            v1 = MEMORY.ref(4, v0).offset(0x0L).get();

            v0 = v1 << 1;
            v0 = v0 + v1;
            v0 = v0 << a0;
            v0 = v0 - v1;
            v0 = v0 << a0;
            v0 = v0 + s7;
            MEMORY.ref(1, v0).offset(0x345L).setu(a1);
            playSound(a0);
            v0 = 0x800c_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x2418L).get();
            s0 = 0x8012_0000L;
            FUN_800242e8(a0);
            v1 = 0x800c_0000L;
            v0 = 0x18L;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          } else {
            //LAB_800fe680
            a0 = 0x28L;
            playSound(a0);
            s0 = 0x8012_0000L;
          }
        }

        //LAB_800fe68c
        a0 = s0 - 0x28c0L;
        a1 = 0x7L;
        v0 = FUN_801040c0(a0, a1);
        if(v0 != 0) {
          a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

          v0 = FUN_800fc838(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();
          v0 = v0 - 0x4L;
          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
        }

        break;

      case 0x1a:
        a0 = 0x2L;
        a1 = 0xaL;
        scriptStartEffect(a0, a1);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x28ccL).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x28bcL).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x28c0L).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(1, v0).offset(-0x2878L).setu(0);
        v0 = v0 - 0x2878L;
        MEMORY.ref(4, v0).offset(0x4L).setu(0);
        v0 = 0x8012_0000L;
        v1 = 0x800c_0000L;
        MEMORY.ref(1, v0).offset(-0x2364L).setu(0);
        v0 = 0x1bL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x1b:
        a0 = 0xffL;
        FUN_8002437c(a0);
        a0 = 0x8011_0000L;
        a0 = a0 + 0x41fcL;
        a1 = 0;
        a2 = a1;
        FUN_8010376c(a0, a1, a2);
        s0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

        v0 = FUN_800fc8dc(a0);
        a0 = 0x77L;
        a1 = a0;
        a2 = 0x2aL;
        a3 = v0;
        v0 = FUN_80103818(a0, a1, a2, a3);
        a0 = v0;
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x2414L).setu(a0);
        FUN_80104b60(a0);

        v0 = FUN_80104448();
        a3 = 0xffL;
        v1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v1).offset(-0x28ccL).get();
        v1 = 0x8012_0000L;
        a1 = MEMORY.ref(4, s0).offset(-0x28c0L).get();
        a2 = MEMORY.ref(4, v1).offset(-0x28bcL).get();
        v1 = 0x8012_0000L;
        MEMORY.ref(4, v1).offset(-0x28b0L).setu(v0);
        FUN_80102dfc(a0, a1, a2, a3);
        v1 = 0x800c_0000L;
        v0 = 0x1cL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x1c:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
        v0 = 0x8012_0000L;
        a1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = 0x8012_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a3 = 0;
        FUN_80102dfc(a0, a1, a2, a3);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          v1 = 0x800c_0000L;
        } else {
          v1 = 0x800c_0000L;
          v0 = 0x1dL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x1d:
        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 != 0) {
          a0 = 0x3L;
          playSound(a0);
          a0 = 0x7L;
          a1 = 0xaL;
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2460L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2464L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2468L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x246cL).setu(0);
          FUN_800fca0c(a0, a1);
        }

        //LAB_800fe824
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          a0 = 0x8012_0000L;
        } else {
          a0 = 0x8012_0000L;
          s1 = 0x8012_0000L;
          s2 = 0x8012_0000L;
          v0 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
          v1 = MEMORY.ref(4, s2).offset(-0x28bcL).get();
          s0 = a0 - 0x2838L;
          v0 = v0 + v1;
          v0 = v0 << 2;
          v0 = v0 + s0;
          a0 = MEMORY.ref(1, v0).offset(0x0L).get();

          v0 = FUN_80022afc(a0);
          v1 = 0x8012_0000L;
          a0 = v0 & 0xffL;
          if(a0 == 0) {
            MEMORY.ref(4, v1).offset(-0x2848L).setu(a0);
            //LAB_800fe93c
            playSound(0x28L);
          } else {
            MEMORY.ref(4, v1).offset(-0x2848L).setu(a0);
            v0 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
            v1 = MEMORY.ref(4, s2).offset(-0x28bcL).get();

            v0 = v0 + v1;
            v0 = v0 << 2;
            v0 = v0 + s0;
            v0 = MEMORY.ref(2, v0).offset(0x2L).get();

            v0 = v0 & 0x4000L;
            if(v0 == 0) {
              v0 = a0 & 0x2L;
              if(v0 != 0) {
                s0 = 0;
                v0 = 0x8012_0000L;
                s1 = v0 - 0x28e8L;

                //LAB_800fe8b0
                do {
                  a0 = s0;
                  v0 = FUN_800fc8c0(a0);
                  a0 = 0x7eL;
                  a1 = a0;
                  a2 = v0;
                  a3 = 0x6eL;
                  v0 = FUN_80103818(a0, a1, a2, a3);
                  MEMORY.ref(4, s1).offset(0x0L).setu(v0);
                  a0 = v0;
                  FUN_80104b60(a0);
                  s0 = s0 + 0x1L;
                  s1 = s1 + 0x4L;
                } while((int)s0 < 0x7L);
              } else {
                //LAB_800fe8f0
                v0 = 0x8012_0000L;
                a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();

                v0 = FUN_800fc8c0(a0);
                a0 = 0x7eL;
                a1 = a0;
                a2 = v0;
                a3 = 0x6eL;
                v0 = FUN_80103818(a0, a1, a2, a3);
                a0 = v0;
                v0 = 0x800c_0000L;
                MEMORY.ref(4, v0).offset(-0x2418L).setu(a0);
                FUN_80104b60(a0);
              }

              //LAB_800fe924
              a0 = 0x2L;
              playSound(a0);
              v1 = 0x800c_0000L;
              v0 = 0x1eL;
              MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            } else {
              //LAB_800fe93c
              playSound(0x28L);
            }
          }
        }

        //LAB_800fe944
        s0 = 0x8012_0000L;
        a0 = s0 - 0x28c0L;
        s1 = 0x8012_0000L;
        a1 = s1 - 0x28bcL;
        a2 = 0x5L;
        v0 = 0x8012_0000L;
        a3 = MEMORY.ref(4, v0).offset(-0x28b0L).get();
        v0 = 0x1L;
        a4 = v0;
        v0 = FUN_80103f00(a0, a1, a2, a3, a4);
        if(v0 == 0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

          v0 = FUN_800fc8dc(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x2414L).get();

          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
          v0 = 0x8012_0000L;
        }

        //LAB_800fe994
        a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
        a1 = MEMORY.ref(4, s0).offset(-0x28c0L).get();
        a2 = MEMORY.ref(4, s1).offset(-0x28bcL).get();

        //LAB_800fec18
        a3 = 0;
        FUN_80102dfc(a0, a1, a2, a3);
        break;

      case 0x1e:
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 == 0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x2848L).get();

          v0 = v0 & 0x2L;
          if(v0 == 0) {
            //LAB_800fea00
            v0 = 0x800c_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x2418L).get();

            FUN_800242e8(a0);
          } else {
            s0 = 0;
            v0 = 0x8012_0000L;
            s1 = v0 - 0x28e8L;

            //LAB_800fe9dc
            do {
              a0 = MEMORY.ref(4, s1).offset(0x0L).get();
              s1 = s1 + 0x4L;
              s0 = s0 + 0x1L;
              FUN_800242e8(a0);
            } while((int)s0 < 0x7L);
          }

          //LAB_800fea10
          a0 = 0x3L;
          playSound(a0);
          v1 = 0x800c_0000L;
          v0 = 0x1dL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fea24
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x2848L).get();

          v0 = v0 & 0x2L;
          s0 = 0;
          if(v0 != 0) {
            a0 = 0x8012_0000L;
            v0 = 0x8012_0000L;
            v1 = MEMORY.ref(1, v0).offset(-0x283cL).get();
            v0 = -0x2L;
            MEMORY.ref(4, a0).offset(-0x28acL).setu(v0);
            if(v1 != 0) {
              v0 = 0x8012_0000L;
              s2 = v0 - 0x2878L;
              v0 = 0x8012_0000L;
              s4 = v0 - 0x2838L;
              s3 = 0x8012_0000L;
              v0 = 0x800c_0000L;
              s1 = v0 - 0x2448L;

              //LAB_800fea84
              do {
                v1 = 0x8012_0000L;
                v0 = MEMORY.ref(4, s3).offset(-0x28c0L).get();
                v1 = MEMORY.ref(4, v1).offset(-0x28bcL).get();
                a2 = MEMORY.ref(1, s1).offset(0x0L).get();
                v0 = v0 + v1;
                v0 = v0 << 2;
                v0 = v0 + s4;
                a1 = MEMORY.ref(1, v0).offset(0x0L).get();
                a0 = s2;
                v0 = FUN_80022d88(a0, a1, a2);
                v1 = MEMORY.ref(4, s2).offset(0x4L).get();
                v0 = -0x2L;
                if(v1 == v0) {
                  v0 = 0x8012_0000L;
                } else {
                  v0 = 0x8012_0000L;
                  v0 = 0x8012_0000L;
                  MEMORY.ref(4, v0).offset(-0x28acL).setu(0);
                  v0 = 0x8012_0000L;
                }

                //LAB_800feac8
                v0 = MEMORY.ref(1, v0).offset(-0x283cL).get();
                s0 = s0 + 0x1L;
                s1 = s1 + 0x4L;
              } while((int)s0 < (int)v0);
            }

            //LAB_800feadc
            v0 = 0x8012_0000L;
            v1 = MEMORY.ref(4, v0).offset(-0x28acL).get();
            v0 = 0x8012_0000L;
            MEMORY.ref(4, v0).offset(-0x2874L).setu(v1);
          } else {
            //LAB_800feaf0
            v1 = 0x8012_0000L;
            v0 = 0x8012_0000L;
            a0 = 0x8012_0000L;
            v0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            a0 = MEMORY.ref(4, a0).offset(-0x28bcL).get();
            v1 = v1 - 0x2838L;
            v0 = v0 + a0;
            v0 = v0 << 2;
            v0 = v0 + v1;
            v1 = 0x800c_0000L;
            a1 = MEMORY.ref(1, v0).offset(0x0L).get();
            v0 = 0x8012_0000L;
            v1 = v1 - 0x2448L;
            v0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
            a0 = 0x8012_0000L;
            v0 = v0 << 2;
            v0 = v0 + v1;
            a2 = MEMORY.ref(1, v0).offset(0x0L).get();
            a0 = a0 - 0x2878L;
            v0 = FUN_80022d88(a0, a1, a2);
          }

          //LAB_800feb40
          a0 = 0x2L;
          playSound(a0);
          v1 = 0x8012_0000L;
          v0 = 0x8012_0000L;
          a0 = 0x8012_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a0 = MEMORY.ref(4, a0).offset(-0x28bcL).get();
          v1 = v1 - 0x2838L;
          v0 = v0 + a0;
          v0 = v0 << 2;
          v0 = v0 + v1;
          a0 = MEMORY.ref(1, v0).offset(0x0L).get();
          s0 = 0x8012_0000L;
          v0 = FUN_800232dc(a0);
          s0 = s0 - 0x2878L;
          v0 = FUN_80104448();
          a0 = 0;
          v1 = 0x8012_0000L;
          MEMORY.ref(4, v1).offset(-0x28b0L).setu(v0);
          FUN_80110030(a0);
          a0 = s0;
          FUN_80104324(a0);
          a0 = s0 + 0x8L;
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x1bL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800febb0
        v0 = 0x8012_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x2848L).get();

        v0 = v0 & 0x2L;
        if(v0 != 0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          v0 = 0x8012_0000L;
          a1 = MEMORY.ref(1, v0).offset(-0x283cL).get();
          s0 = 0x8012_0000L;
          a0 = s0 - 0x28ccL;
          v0 = FUN_8010415c(a0, a1);
          if(v0 == 0) {
            v0 = 0x8012_0000L;
          } else {
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();

            v0 = FUN_800fc8c0(a0);
            v1 = 0x800c_0000L;
            v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();
            v0 = v0 - 0x3L;
            MEMORY.ref(4, v1).offset(0x40L).setu(v0);
            v0 = 0x8012_0000L;
          }
        }

        //LAB_800fec04
        a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
        v0 = 0x8012_0000L;
        a1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = 0x8012_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x28bcL).get();

        //LAB_800fec18
        a3 = 0;
        FUN_80102dfc(a0, a1, a2, a3);
        break;

      case 0x23:
        a0 = 0x2L;
        a1 = 0xaL;
        scriptStartEffect(a0, a1);
        a0 = 0xffL;
        FUN_8002437c(a0);
        a0 = 0x8011_0000L;
        a0 = a0 + 0x41c4L;
        a1 = 0;
        a2 = a1;
        FUN_8010376c(a0, a1, a2);
        v1 = 0x8012_0000L;
        MEMORY.ref(4, v1).offset(-0x28b0L).setu(0);
        s0 = 0;
        t1 = 0xffL;
        v0 = 0x800c_0000L;
        t0 = v0 - 0x529cL;
        a3 = 0x1L;
        a1 = v1;
        v0 = 0x8012_0000L;
        a2 = v0 - 0x2838L;
        a0 = a2;

        //LAB_800fec7c
        do {
          MEMORY.ref(1, a0).offset(0x0L).setu(t1);
          if((int)s0 < 0x40L) {
            v0 = s0 & 0x1fL;
            v1 = s0 >>> 5;
            v1 = v1 << 2;
            v1 = v1 + t0;
            v1 = MEMORY.ref(4, v1).offset(0x0L).get();
            v0 = a3 << v0;
            v1 = v1 & v0;
            if(v1 != 0) {
              v0 = MEMORY.ref(4, a1).offset(-0x28b0L).get();

              v0 = v0 << 2;
              v0 = v0 + a2;
              MEMORY.ref(1, v0).offset(0x0L).setu(s0);
              v0 = MEMORY.ref(4, a1).offset(-0x28b0L).get();

              v0 = v0 << 2;
              v0 = v0 + a2;
              MEMORY.ref(1, v0).offset(0x1L).setu(s0);
              v1 = MEMORY.ref(4, a1).offset(-0x28b0L).get();

              v0 = v1 << 2;
              v1 = v1 + 0x1L;
              v0 = v0 + a2;
              MEMORY.ref(2, v0).offset(0x2L).setu(0);
              MEMORY.ref(4, a1).offset(-0x28b0L).setu(v1);
            }
          }

          //LAB_800fecf0
          s0 = s0 + 0x1L;
          a0 = a0 + 0x4L;
        } while((int)s0 < 0x46L);

        a0 = 0;
        s3 = 0x8012_0000L;
        s1 = 0x8012_0000L;
        s2 = 0x8012_0000L;
        MEMORY.ref(4, s3).offset(-0x28bcL).setu(0);
        MEMORY.ref(4, s1).offset(-0x28c0L).setu(0);
        MEMORY.ref(4, s2).offset(-0x28ccL).setu(0);
        v0 = FUN_800fc824(a0);
        a0 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
        s0 = v0;
        v0 = FUN_800fc814(a0);
        a0 = 0x76L;
        a1 = a0;
        a2 = s0;
        a3 = v0 + 0x20L;
        v0 = FUN_80103818(a0, a1, a2, a3);
        a0 = v0;
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x2418L).setu(a0);
        FUN_80104b60(a0);
        a0 = MEMORY.ref(4, s2).offset(-0x28ccL).get();
        a1 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
        a2 = MEMORY.ref(4, s3).offset(-0x28bcL).get();
        a3 = 0xffL;
        FUN_80102f74(a0, a1, a2, a3);
        v1 = 0x800c_0000L;
        v0 = 0x24L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x24:
        s1 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s1).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 != 0) {
          a0 = 0x2L;
          playSound(a0);
          a0 = 0x7L;
          a1 = 0xbL;
          a3 = 0x8012_0000L;
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2460L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2464L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2468L).setu(0);
          v0 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x246cL).setu(0);
          v0 = 0x8012_0000L;
          a2 = MEMORY.ref(4, a3).offset(-0x28b0L).get();
          v0 = v0 - 0x2838L;
          v1 = a2 << 2;
          a2 = a2 + 0x1L;
          v1 = v1 + v0;
          assert false : "Undefined s0";
          //          MEMORY.ref(1, v1).offset(0x1L).setu(s0); //TODO This seems like a legit error?
          MEMORY.ref(4, a3).offset(-0x28b0L).setu(a2);
          FUN_800fca0c(a0, a1);
        }

        //LAB_800fede4
        s0 = 0x8012_0000L;
        a0 = s0 - 0x28c0L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x28bcL;
        a2 = 0x7L;
        v0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x28b0L).get();
        v0 = 0x2L;
        a4 = v0;
        a3 = v1 & 0x1L;
        a3 = v1 + a3;
        v0 = FUN_80103f00(a0, a1, a2, a3, a4);
        if(v0 != 0) {
          a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

          v0 = FUN_800fc814(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();
          v0 = v0 + 0x20L;
          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
        }

        //LAB_800fee38
        v0 = MEMORY.ref(4, s1).offset(-0x23bcL).get();

        v0 = v0 & 0x8000L;
        s0 = 0x8012_0000L;
        if(v0 != 0) {
          v0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();

          if(v0 != 0) {
            a0 = 0x1L;
            playSound(a0);
            a0 = 0;
            MEMORY.ref(4, s0).offset(-0x28ccL).setu(0);
            v0 = FUN_800fc824(a0);
            v1 = 0x800c_0000L;
            v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();

            MEMORY.ref(4, v1).offset(0x40L).setu(v0);
          }
        }

        //LAB_800fee80
        //LAB_800fee84
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x2000L;
        s0 = 0x8012_0000L;
        if(v0 != 0) {
          v0 = MEMORY.ref(4, s0).offset(-0x28ccL).get();

          if(v0 == 0) {
            v0 = 0x8012_0000L;
            a0 = 0x1L;
            playSound(a0);
            a0 = 0x1L;
            v0 = a0;
            MEMORY.ref(4, s0).offset(-0x28ccL).setu(v0);
            v0 = FUN_800fc824(a0);
            v1 = 0x800c_0000L;
            v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();

            MEMORY.ref(4, v1).offset(0x40L).setu(v0);
          }
        }

        //LAB_800feed0
        //LAB_800feed4
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
        v0 = 0x8012_0000L;
        a1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = 0x8012_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a3 = 0;
        FUN_80102f74(a0, a1, a2, a3);
        break;

      case 0x25: // Part of load game menu
        _8004dd30.setu(0x1L);
        FUN_80110030(0);
        FUN_80103bd4(0);
        scriptStartEffect(0x2L, 0xaL);

      case 0x26: // Part of load game menu
        if(whichMenu_800bdc38.get() == 0x13L) {
          FUN_8010f130(_8011c370.getAddress(), 0x2L);
          v0 = 0x27L;
        } else {
          //LAB_800fef50
          v0 = 0x28L;
        }

        //LAB_800fef54
        inventoryMenuState_800bdc28.setu(v0);
        FUN_8002437c(0xffL);
        FUN_801030c0(_8011d744.get(), 0, 0xffL);
        break;

      case 0x27: // Part of load game menu
        v1 = FUN_8010ecec(_8011dc90.getAddress());
        if(v1 == 0x1L) {
          //LAB_800fefa8
          inventoryMenuState_800bdc28.setu(0x28L);
        } else if(v1 == 0x2L) {
          //LAB_800fefb8
          FUN_800fca0c(0x7dL, 0xeL);
        }

        //LAB_800fefc4
        //LAB_800fefc8
        //LAB_800fff94
        FUN_801030c0(_8011d744.get(), 0, 0);
        break;

      case 0x28: // Part of load game menu
        _800bdb98.setu(0);
        _800bdb94.setu(0);
        FUN_8010f130(_8011c93c.getAddress(), 0x1L);
        _8011e0d4.setu(0x1L);
        _8011d744.setu(0);
        _8011d740.setu(0);
        FUN_801030c0(0, 0, 0);
        inventoryMenuState_800bdc28.setu(0x29L);
        break;

      case 0x29: // Part of load game menu
        s0 = _8011dc90.getAddress();
        FUN_8010ecec(s0);
        FUN_801030c0(_8011d744.get(), 0, 0);

        if(MEMORY.ref(4, s0).offset(0x10L).get() < 0x3L) {
          break;
        }

        if(_800bb168.get() != 0) {
          break;
        }

        s1 = _8011d768.getAddress();
        s2 = _8011dd10.getAddress();
        FUN_80109b08(s1, s2);

        if(_8011e0d4.get() != 0) {
          break;
        }

        a0 = MEMORY.ref(4, s1).offset(0x8L).get();

        if(a0 == 0x1L) {
          //LAB_801008d0
          inventoryMenuState_800bdc28.setu(0x37L);
          MEMORY.ref(1, s0).offset(0xcL).addu(0x1L);
          break;
        }

        //LAB_800ff0c8
        if(a0 == 0x2L) {
          //LAB_801008d0
          inventoryMenuState_800bdc28.setu(0x39L);
          MEMORY.ref(1, s0).offset(0xcL).addu(0x1L);
          break;
        }

        //LAB_800ff0e0
        if(a0 == 0x4L && whichMenu_800bdc38.get() == 0xeL) {
          v0 = s2 + 0x348L;

          //LAB_800ff104
          for(s0 = 0xeL; s0 >= 0; s0--) {
            MEMORY.ref(1, v0).offset(0x4L).setu(0xffL);
            v0 -= 0x3cL;
          }

          inventoryMenuState_800bdc28.setu(0x3cL);
          _8011dc9c.addu(0x1L);
          break;
        }

        //LAB_800ff12c
        //LAB_800ff130
        if(_8011d768.offset(1, 0x4L).get() != 0 || whichMenu_800bdc38.get() != 0xeL) {
          //LAB_800ff16c
          if(_8011d768.offset(1, 0x4L).get() != 0 || _8011d768.offset(1, 0x6L).get() != 0) {
            //LAB_800ff194
            if(_8011d768.offset(1, 0x10L).get() >= 0xdL) {
              v1 = _8011d768.offset(1, 0x10L).get() - 0xcL;
              _8011d740.setu(v1);
              v0 = _8011d768.offset(1, 0x10L).get() - v1; //TODO this is just 0xc...? Why the math?
              _8011d744.setu(v0);
            } else {
              //LAB_800ff1d0
              _8011d740.setu(0);
              _8011d744.setu(_8011d768.offset(1, 0x10L));
            }

            //LAB_800ff1e0
            _800bdbe8.setu(FUN_80103818(0x81L, 0x81L, 0x10L, FUN_800fc84c(_8011d740.get())));
            _800bdbec.setu(FUN_80103818(0x82L, 0x82L, 0xc0L, FUN_800fc84c(_8011d740.get())));
            FUN_80104b60(_800bdbe8.get());
            FUN_80104b60(_800bdbec.get());
            FUN_8010361c(_8011d744.get(), 0xeL);

            inventoryMenuState_800bdc28.setu(0x2aL);
            _8011dc9c.addu(0x1L);
            break;
          }

          if(_8011d768.offset(1, 0x4L).get() == 0) {
            inventoryMenuState_800bdc28.setu(0x3aL);
            _8011dc9c.addu(0x1L);
            break;
          }
        }

        //LAB_801004ac
        inventoryMenuState_800bdc28.setu(0x3cL);
        _8011dc9c.addu(0x1L);
        break;

      case 0x2a:
        s0 = 0x8012_0000L;
        s0 = s0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        a1 = 0xeL;
        FUN_8010361c(a0, a1);
        v0 = MEMORY.ref(1, s0).offset(0xcL).get();

        a1 = 0x8012_0000L;
        if(v0 != 0) {
          a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();

          //LAB_800fff8c
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;

          //LAB_800fff94
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
          break;
        }

        //LAB_800ff2a8
        a0 = 0;
        FUN_8002437c(a0);
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        a2 = 0xffL;
        FUN_801030c0(a0, a1, a2);
        v1 = 0x800c_0000L;

        //LAB_800ff4f8
        v0 = 0x2bL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x2b:
        v0 = 0x1L;
        a4 = v0;
        s0 = 0x8012_0000L;
        a0 = s0 - 0x28c0L;
        s1 = 0x8012_0000L;
        a1 = s1 - 0x28bcL;
        a2 = 0x3L;
        a3 = 0xfL;
        v0 = FUN_80103f00(a0, a1, a2, a3, a4);
        if(v0 != 0) {
          a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

          v0 = FUN_800fc84c(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();
          a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();
          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
          v0 = FUN_800fc84c(a0);
          v1 = 0x800c_0000L;
          a0 = 0x800c_0000L;
          a1 = MEMORY.ref(4, v1).offset(-0x2414L).get();
          v1 = 0x2aL;
          MEMORY.ref(4, a0).offset(-0x23d8L).setu(v1);
          MEMORY.ref(4, a1).offset(0x44L).setu(v0);
        }

        //LAB_800ff330
        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 != 0) {
          a0 = 0x3L;
          playSound(a0);
          v1 = 0x800c_0000L;
          v0 = 0x47L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800ff35c
        v0 = MEMORY.ref(4, s0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 != 0) {
          a0 = 0;
          v0 = FUN_8002dbdc(a0);
          a0 = 0x2L;
          playSound(a0);
          v1 = 0x800c_0000L;
          s0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x246cL).get();
          v0 = 0x2cL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

          if(a0 != 0) {
            FUN_801033e8(a0);
            MEMORY.ref(4, s0).offset(-0x246cL).setu(0);
          }

          //LAB_800ff3a4
          s0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x2468L).get();

          if(a0 != 0) {
            FUN_801033e8(a0);
            MEMORY.ref(4, s0).offset(-0x2468L).setu(0);
          }
        } else {
          //LAB_800ff3c8
          a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
          a1 = 0xeL;
          FUN_8010361c(a0, a1);
        }

        //LAB_800fff80
        v0 = 0x8012_0000L;

        //LAB_800fff84
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;

        //LAB_800fff8c
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;

        //LAB_800fff94
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        break;

      case 0x2c:
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        v0 = 0x8012_0000L;
        s2 = v0 - 0x22f0L;
        a1 = s2;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1 = new Ref<>();
        final Ref<Long> refA2 = new Ref<>();
        v0 = FUN_8002efb8(a0, refA1, refA2);
        MEMORY.ref(4, a1).setu(refA1.get());
        MEMORY.ref(4, a2).setu(refA2.get());

        if(v0 == 0) {
          break;
        }
        v0 = 0x1L;
        a2 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        if(a2 == v0) {
          v1 = 0x800c_0000L;

          //LAB_800ff61c
          v0 = 0x37L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }
        v1 = 0x800c_0000L;
        v0 = 0x3L;
        if(a2 != 0) {
          if(a2 != v0) {
            v0 = 0x39L;

            //LAB_800ff65c
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
          v0 = 0x3eL;

          //LAB_800ff62c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }

        //LAB_800ff440
        v0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();

        v1 = v1 + v0;
        v0 = v1 << 4;
        v0 = v0 - v1;
        v0 = v0 << 2;
        v0 = v0 + s2;
        a3 = MEMORY.ref(1, v0).offset(0x4L).get();
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23c8L).get();
        v0 = 0xeL;
        if(v1 != v0) {
          //LAB_800ff4a0
          if((int)a3 < 0xfL || a3 == 0xffL) {
            //LAB_800ff4b0
            a0 = 0x8012_0000L;
            if(a3 == v0) {
              a0 = a0 - 0x3638L;
            } else {
              //LAB_800ff4c0
              a0 = 0x8012_0000L;
              a0 = a0 - 0x3618L;
            }

            //LAB_800ff4c8
            a1 = 0x2L;
            FUN_8010f130(a0, a1);
            v1 = 0x8012_0000L;
            v0 = 0x1L;
            MEMORY.ref(4, v1).offset(-0x2358L).setu(v0);
            v1 = 0x800c_0000L;
            v0 = 0x31L;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
        } else {
          if((int)a3 < 0xfL) {
            a0 = 0x8012_0000L;
            a0 = a0 - 0x35f8L;
            a1 = 0x2L;
            FUN_8010f130(a0, a1);
            v1 = 0x800c_0000L;
            v0 = 0x2dL;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
        }

        //LAB_800ff4ec
        a0 = 0x28L;
        playSound(a0);
        v1 = 0x800c_0000L;

        //LAB_800ff4f8
        v0 = 0x2bL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x2d:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v1 = FUN_8010ecec(a0);
        if(v1 == 0x1L) {
          //LAB_800ff530
          v1 = 0x800c_0000L;
          v0 = 0x2eL;

          //LAB_800ffc5c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        } else if(v1 == 0x2L) {
          //LAB_800ff53c
          v1 = 0x800c_0000L;
          v0 = 0x2bL;

          //LAB_800ffc5c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fff80
        v0 = 0x8012_0000L;

        //LAB_800fff84
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;

        //LAB_800fff8c
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;

        //LAB_800fff94
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        break;

      case 0x2e:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0;
        FUN_8002df60(a0);
        v1 = 0x800c_0000L;
        v0 = 0x2fL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x2f:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        v0 = 0x8012_0000L;
        s1 = v0 - 0x22f0L;
        a1 = s1;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1_0 = new Ref<>();
        final Ref<Long> refA2_0 = new Ref<>();
        v0 = FUN_8002efb8(a0, refA1_0, refA2_0);
        MEMORY.ref(4, a1).setu(refA1_0.get());
        MEMORY.ref(4, a2).setu(refA2_0.get());

        if(v0 == 0) {
          v0 = 0x1L;
          break;
        }
        v0 = 0x1L;
        a2 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        if(a2 == v0) {
          v1 = 0x800c_0000L;

          //LAB_800ff61c
          v0 = 0x37L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }
        v1 = 0x800c_0000L;
        if(a2 == 0) {
          v0 = 0x3L;

          //LAB_800ff5fc
          a0 = 0x8012_0000L;
          a0 = a0 - 0x35d4L;
          a1 = 0x1L;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x30L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }
        v0 = 0x3L;
        if(a2 == v0) {
          v0 = 0x4L;

          //LAB_800ff628
          v0 = 0x3eL;

          //LAB_800ff62c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }
        v0 = 0x4L;
        s0 = 0xeL;
        if(a2 != v0) {
          v0 = 0x39L;
        } else {
          //LAB_800ff634
          v1 = 0xffL;
          v0 = s1 + 0x348L;

          //LAB_800ff63c
          do {
            MEMORY.ref(1, v0).offset(0x4L).setu(v1);
            s0 = s0 - 0x1L;
            v0 = v0 - 0x3cL;
          } while((int)s0 >= 0);

          v1 = 0x800c_0000L;
          v0 = 0x3cL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }

        //LAB_800ff65c
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x30:
        s0 = 0x8012_0000L;
        s0 = s0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        v0 = 0x8012_0000L;
        s2 = v0 - 0x22f0L;
        a1 = s2;
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        v0 = MEMORY.ref(4, s0).offset(0x10L).get();

        if(v0 < 0x3L) {
          break;
        }
        v0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();

        v1 = v1 + v0;
        v0 = v1 << 4;
        v0 = v0 - v1;
        v0 = v0 << 2;
        v0 = v0 + s2;
        a0 = MEMORY.ref(1, v0).offset(0x4L).get();

        v0 = FUN_8010a0ec(a0);
        v1 = v0 & 0xffL;
        if(v1 == 0) {
          v0 = 0x5L;

          //LAB_800ff6ec
          v1 = 0x800c_0000L;
          v0 = 0x1L;
          MEMORY.ref(4, v1).offset(-0x23ccL).setu(v0);
          v0 = 0x800c_0000L;
          s0 = v0 - 0x5438L;
          v0 = MEMORY.ref(4, s0).offset(0xa4L).get();
          v1 = MEMORY.ref(4, s0).offset(0xa8L).get();
          a0 = 0x8005_0000L;
          MEMORY.ref(4, a0).offset(0x2c34L).setu(v0);
          v0 = 0x8005_0000L;
          MEMORY.ref(4, v0).offset(0x2c30L).setu(v1);
          v0 = 0x8005_0000L;
          MEMORY.ref(4, v0).offset(0x2c38L).setu(v1);
          v0 = 0x108L;
          if(v1 != v0) {
            v0 = 0x35L;
          } else {
            v0 = 0x35L;
            MEMORY.ref(4, a0).offset(0x2c34L).setu(v0);
          }

          //LAB_800ff730
          FUN_8002379c();
          a0 = MEMORY.ref(1, s0).offset(0x4e0L).get();

          FUN_8004c6f8(a0);
          v1 = 0x800c_0000L;
          v0 = 0x46L;
        } else {
          v0 = 0x5L;
          if(v1 != v0) {
            v0 = 0x38L;
          } else {
            //LAB_800ff750
            v0 = 0x3dL;
          }
        }

        //LAB_800ff754
        v1 = 0x800c_0000L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

        //LAB_80100bf4
        v1 = 0x8012_0000L;
        v1 = v1 - 0x2370L;
        v0 = MEMORY.ref(1, v1).offset(0xcL).get();

        v0 = v0 + 0x1L;
        MEMORY.ref(1, v1).offset(0xcL).setu(v0);
        break;

      case 0x31:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v1 = v0;
        v0 = 0x1L;
        if(v1 == v0) {
          //LAB_800ff788
          v1 = 0x800c_0000L;
          v0 = 0x32L;

          //LAB_800ffc5c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        } else if(v1 == 0x2L) {
          //LAB_800ff794
          v1 = 0x800c_0000L;
          v0 = 0x2bL;

          //LAB_800ffc5c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fff80
        v0 = 0x8012_0000L;

        //LAB_800fff84
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;

        //LAB_800fff8c
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;

        //LAB_800fff94
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        break;

      case 0x32:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0;
        FUN_8002df60(a0);
        v1 = 0x800c_0000L;
        v0 = 0x33L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x33:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1_1 = new Ref<>();
        final Ref<Long> refA2_1 = new Ref<>();
        v0 = FUN_8002efb8(a0, refA1_1, refA2_1);
        MEMORY.ref(4, a1).setu(refA1_1.get());
        MEMORY.ref(4, a2).setu(refA2_1.get());

        if(v0 == 0) {
          v0 = 0x1L;
        } else {
          v0 = 0x1L;
          a2 = MEMORY.ref(4, s0).offset(-0x2848L).get();

          if(a2 == v0) {
            v1 = 0x800c_0000L;

            //LAB_800ff854
            v0 = 0x37L;
          } else {
            v1 = 0x800c_0000L;
            if(a2 == 0) {
              v0 = 0x3L;

              //LAB_800ff838
              a0 = 0x8012_0000L;
              a0 = a0 - 0x3550L;
              a1 = 0x1L;
              FUN_8010f130(a0, a1);
              v1 = 0x800c_0000L;
              v0 = 0x34L;
            } else {
              v0 = 0x3L;
              if(a2 == v0) {
                v0 = 0x4L;

                //LAB_800ff85c
                v0 = 0x3bL;
              } else {
                v0 = 0x4L;
                if(a2 != v0) {
                  v0 = 0x39L;
                } else {
                  //LAB_800ff864
                  a0 = 0xffL;
                  FUN_8002437c(a0);
                  v1 = 0xffL;
                  s0 = 0xeL;
                  v0 = 0x8012_0000L;
                  v0 = v0 - 0x22f0L;
                  v0 = v0 + 0x348L;

                  //LAB_800ff880
                  do {
                    MEMORY.ref(1, v0).offset(0x4L).setu(v1);
                    s0 = s0 - 0x1L;
                    v0 = v0 - 0x3cL;
                  } while((int)s0 >= 0);

                  a1 = 0;
                  v0 = 0x8012_0000L;
                  a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
                  a2 = 0xffL;
                  FUN_801030c0(a0, a1, a2);
                  v1 = 0x800c_0000L;
                  v0 = 0x40L;
                }
              }
            }
          }

          //LAB_800ffc5c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fff80
        v0 = 0x8012_0000L;

        //LAB_800fff84
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;

        //LAB_800fff8c
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;

        //LAB_800fff94
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        break;

      case 0x34:
        s0 = 0x8012_0000L;
        s0 = s0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        v0 = MEMORY.ref(4, s0).offset(0x10L).get();

        if(v0 < 0x3L) {
          break;
        }
        v0 = 0x800c_0000L;
        v1 = 0x8005_0000L;
        a0 = 0x800d_0000L;
        v0 = v0 - 0x5438L;
        a3 = 0x8012_0000L;
        t0 = a3 - 0x2898L;
        v1 = MEMORY.ref(4, v1).offset(0x2c38L).get();
        a0 = MEMORY.ref(4, a0).offset(-0x4bb0L).get();
        t1 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(0xa4L).setu(v1);
        MEMORY.ref(4, v0).offset(0xa8L).setu(a0);
        v0 = 0x8012_0000L;
        a2 = v0 - 0x2340L;
        a1 = MEMORY.ref(4, t0).offset(0xcL).get();
        v0 = MEMORY.ref(4, t1).offset(-0x28c0L).get();
        v1 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        MEMORY.ref(4, a2).offset(0x30L).setu(a1);
        a0 = MEMORY.ref(1, t0).offset(0x10L).get();
        v0 = v0 + v1;
        if(a0 == v0) {
          v0 = a1 + 0x1L;
        } else {
          v0 = a1 + 0x1L;
          MEMORY.ref(4, a2).offset(0x30L).setu(v0);
        }

        //LAB_800ff940
        a1 = MEMORY.ref(4, a3).offset(-0x2898L).get();
        a2 = MEMORY.ref(4, t0).offset(0x4L).get();
        a3 = MEMORY.ref(4, t0).offset(0x8L).get();
        a0 = MEMORY.ref(1, t1).offset(-0x28c0L).get();
        v0 = MEMORY.ref(1, s1).offset(-0x28bcL).get();
        t3 = MEMORY.ref(4, t0).offset(0xcL).get();
        t4 = MEMORY.ref(4, t0).offset(0x10L).get();
        a4 = t3;
        a5 = t4;
        a0 = a0 + v0;
        a0 = a0 & 0xffL;
        v0 = FUN_8010a344(a0, a1, a2, a3, a4, a5);
        v0 = v0 & 0xffL;
        v1 = 0x800c_0000L;
        if(v0 == 0) {
          v0 = 0x35L;
        } else {
          //LAB_800ff984
          v0 = 0x3bL;
        }

        //LAB_800ff988
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

        //LAB_80100bf4
        v1 = 0x8012_0000L;
        v1 = v1 - 0x2370L;
        v0 = MEMORY.ref(1, v1).offset(0xcL).get();

        v0 = v0 + 0x1L;
        MEMORY.ref(1, v1).offset(0xcL).setu(v0);
        break;

      case 0x35:
        s0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
        v0 = 0x8012_0000L;
        s1 = v0 - 0x22f0L;
        a1 = s1;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = 0x8012_0000L;
        if(FUN_8010ecec(a0) == 0) {
          a0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();

          //LAB_800fff8c
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;

          //LAB_800fff94
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
          break;
        }

        //LAB_800ff9cc
        a1 = v0 - 0x2340L;
        v0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = MEMORY.ref(4, s0).offset(-0x28bcL).get();
        a0 = a1 + 0x30L;
        v1 = v1 + v0;
        v0 = v1 << 4;
        v0 = v0 - v1;
        v0 = v0 << 2;
        v0 = v0 + s1;

        //LAB_800ff9f4
        do {
          t3 = MEMORY.ref(4, a1).offset(0x0L).get();
          t4 = MEMORY.ref(4, a1).offset(0x4L).get();
          t5 = MEMORY.ref(4, a1).offset(0x8L).get();
          t2 = MEMORY.ref(4, a1).offset(0xcL).get();
          MEMORY.ref(4, v0).offset(0x0L).setu(t3);
          MEMORY.ref(4, v0).offset(0x4L).setu(t4);
          MEMORY.ref(4, v0).offset(0x8L).setu(t5);
          MEMORY.ref(4, v0).offset(0xcL).setu(t2);
          a1 = a1 + 0x10L;
          v0 = v0 + 0x10L;
        } while(a1 != a0);

        a0 = 0xffL;
        t3 = MEMORY.ref(4, a1).offset(0x0L).get();
        t4 = MEMORY.ref(4, a1).offset(0x4L).get();
        t5 = MEMORY.ref(4, a1).offset(0x8L).get();
        MEMORY.ref(4, v0).offset(0x0L).setu(t3);
        MEMORY.ref(4, v0).offset(0x4L).setu(t4);
        MEMORY.ref(4, v0).offset(0x8L).setu(t5);
        FUN_8002437c(a0);
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a2 = 0xffL;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x34d4L;
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x36L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x36:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          v0 = 0x800c_0000L;
          break;
        }
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23c8L).get();
        v0 = 0x13L;
        if(v1 == v0) {
          v1 = 0x800c_0000L;
          //LAB_80100020
          a0 = 0x7dL;

          //LAB_80100024
          a1 = 0xcL;
          FUN_800fca0c(a0, a1);
          break;
        }
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x37:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x34c8L;

        //LAB_800fff38
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x38:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3464L;

        //LAB_800fff38
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x39:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x343cL;

        //LAB_800fff38
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3a:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x33d0L;

        //LAB_800fff38
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3b:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3370L;

        //LAB_800fff38
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3c:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          v0 = 0x8012_0000L;

          //LAB_800fff84
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          a1 = 0x8012_0000L;

          //LAB_800fff8c
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;

          //LAB_800fff94
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
          break;
        }
        v0 = 0x8012_0000L;
        a0 = 0xffL;
        FUN_8002437c(a0);
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a2 = 0xffL;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3878L;

        //LAB_800fff38
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3d:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x28f8L;

        //LAB_800fff38
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3e:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        a0 = 0x8012_0000L;
        if(v0 != 0) {
          a0 = a0 - 0x3c44L;
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x46L;

          //LAB_800ffc5c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fff80
        v0 = 0x8012_0000L;

        //LAB_800fff84
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;

        //LAB_800fff8c
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;

        //LAB_800fff94
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        break;

      case 0x40:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
        } else {
          a0 = 0x8012_0000L;
          a0 = a0 - 0x3348L;
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x41L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          v1 = 0x800c_0000L;
          v0 = 0x32L;
          MEMORY.ref(4, v1).offset(-0x23d0L).setu(v0);
        }

        break;

      case 0x41:
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x8012_0000L;
        if(v1 == 0x7aL) {
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          v0 = 0x8012_0000L;
        } else {
          //LAB_800ffce4
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
          v0 = 0x8012_0000L;
        }

        //LAB_800ffcfc
        s0 = v0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
        } else {
          a0 = 0x8012_0000L;
          a0 = a0 - 0x32d8L;
          a1 = 0x2L;
          FUN_8010f130(a0, a1);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          MEMORY.ref(4, s0).offset(0x18L).setu(v0);
          v0 = 0x42L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x42:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v1 = v0;

        if(v1 == 0x1L) {
          //LAB_800ffd60
          a0 = 0;
          v0 = FUN_8002dbdc(a0);
          a0 = 0;
          a1 = 0x8012_0000L;
          a1 = a1 - 0x2844L;
          s0 = 0x8012_0000L;
          a2 = s0 - 0x2848L;

          final Ref<Long> refA1_2 = new Ref<>();
          final Ref<Long> refA2_2 = new Ref<>();
          v0 = FUN_8002efb8(a0, refA1_2, refA2_2);
          MEMORY.ref(4, a1).setu(refA1_2.get());
          MEMORY.ref(4, a2).setu(refA2_2.get());

          v0 = MEMORY.ref(4, s0).offset(-0x2848L).get();

          if(v0 == 0) {
            v0 = 0x800c_0000L;
            v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
            v0 = 0x8012_0000L;
            if(v1 == 0x7aL) {
              v0 = 0x8012_0000L;
              a0 = v0 - 0x399cL;
            } else {
              //LAB_800ffdb0
              a0 = v0 - 0x32a8L;
            }

            //LAB_800ffdb4
            a1 = 0x1L;
            FUN_8010f130(a0, a1);
            v1 = 0x800c_0000L;
            v0 = 0x43L;
          } else {
            //LAB_800ffdc8
            v0 = 0x45L;
          }

          //LAB_800fff68
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        } else if(v1 == 0x2L) {
          //LAB_800ffdd0
          v1 = 0x800c_0000L;
          v0 = 0x46L;

          //LAB_800fff68
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fff6c
        v0 = 0x800c_0000L;

        //LAB_800fff70
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x7aL;
        if(v1 == v0) {
          v0 = 0x8012_0000L;

          //LAB_80101580
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          break;
        }
        v0 = 0x8012_0000L;

        //LAB_800fff80
        v0 = 0x8012_0000L;

        //LAB_800fff84
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;

        //LAB_800fff8c
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;

        //LAB_800fff94
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        break;

      case 0x43:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x8012_0000L;
        if(v1 == 0x7aL) {
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          v0 = 0x8012_0000L;
        } else {
          //LAB_800ffe14
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
          v0 = 0x8012_0000L;
        }

        //LAB_800ffe2c
        s0 = v0 - 0x2370L;
        v0 = MEMORY.ref(4, s0).offset(0x10L).get();

        if(v0 < 0x3L) {
          break;
        }

        a0 = 0;
        v0 = FUN_8002f1d0(a0);
        a0 = MEMORY.ref(1, s0).offset(0xcL).get();
        v1 = 0x8012_0000L;
        MEMORY.ref(4, v1).offset(-0x2848L).setu(v0);
        a0 = a0 + 0x1L;
        MEMORY.ref(1, s0).offset(0xcL).setu(a0);
        if(v0 != 0) {
          v1 = 0x800c_0000L;
          v0 = 0x45L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        } else {
          //LAB_800ffe74
          v1 = 0x800c_0000L;
          v0 = 0x44L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x44:
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x7aL;
        v0 = 0x8012_0000L;
        if(v1 == 0x7aL) {
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          a0 = 0x8012_0000L;
        } else {
          //LAB_800ffeb0
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
          a0 = 0x8012_0000L;
        }

        //LAB_800ffec8
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          v0 = 0x800c_0000L;
          break;
        }
        v0 = 0x800c_0000L;

        //LAB_80101b18
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x23d8L).setu(v1);
        break;

      case 0x45:
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x8012_0000L;
        if(v1 == 0x7aL) {
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          a0 = 0x8012_0000L;
        } else {

          //LAB_800fff0c
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
          a0 = 0x8012_0000L;
        }

        //LAB_800fff24
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3228L;

        //LAB_800fff38
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x46:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v1 = 0x800c_0000L;
        if(v0 != 0) {
          v0 = 0x47L;

          //LAB_800fff68
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fff6c
        v0 = 0x800c_0000L;

        //LAB_800fff70
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x7aL;
        if(v1 == v0) {
          v0 = 0x8012_0000L;

          //LAB_80101580
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          break;
        }
        v0 = 0x8012_0000L;

        //LAB_800fff80
        v0 = 0x8012_0000L;

        //LAB_800fff84
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a1 = 0x8012_0000L;

        //LAB_800fff8c
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;

        //LAB_800fff94
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        break;

      case 0x47:
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x7aL;
        a1 = 0x8012_0000L;
        if(v1 == v0) {
          a1 = 0;
          v0 = 0x8012_0000L;
          v1 = 0x800c_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          v0 = 0x77L;

          //LAB_8010069c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          FUN_80103168(a0);
          break;
        }

        //LAB_800fffd0
        a1 = a1 - 0x22f0L;
        a2 = 0;
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        v0 = 0x8005_0000L;
        MEMORY.ref(4, v0).offset(-0x22d0L).setu(0);
        FUN_801030c0(a0, a1, a2);
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23c8L).get();
        v0 = 0x4L;
        if(v1 == v0) {
          v0 = 0x13L;

          //LAB_80100018
          a0 = 0x2L;
        } else {
          v0 = 0x13L;
          if(v1 != v0) {
            a0 = 0x7dL;
          } else {
            a0 = 0x7dL;
            v1 = 0x800c_0000L;
            v0 = 0x26L;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
        }

        //LAB_80100024
        a1 = 0xcL;
        FUN_800fca0c(a0, a1);
        break;

      case 0x48:
        a0 = 0x100L;
        a1 = 0;
        a2 = a1;
        v1 = 0x8005_0000L;
        v0 = 0x1L;
        MEMORY.ref(4, v1).offset(-0x22d0L).setu(v0);
        v0 = addToLinkedListTail(a0);
        s0 = 0;
        a1 = 0xffL;
        v1 = 0x8012_0000L;
        a0 = v1 - 0x2838L;
        v1 = 0x8012_0000L;
        MEMORY.ref(4, v1).offset(-0x2840L).setu(v0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x2300L).setu(0);

        //LAB_80100070
        do {
          MEMORY.ref(1, a0).offset(0x0L).setu(a1);
          MEMORY.ref(1, a0).offset(0x1L).setu(s0);
          MEMORY.ref(2, a0).offset(0x2L).setu(0);
          s0 = s0 + 0x1L;
          a0 = a0 + 0x4L;
        } while((int)s0 < 0x7L);

        v0 = 0x8012_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x2840L).get();
        s0 = 0x3fL;

        //LAB_80100098
        do {
          MEMORY.ref(4, v0).offset(0x0L).setu(0);
          s0 = s0 - 0x1L;
          v0 = v0 + 0x4L;
        } while((int)s0 >= 0);

        a0 = 0;
        a1 = 0x1a0cL;
        a2 = a0;
        a3 = 0x8010_0000L;
        a3 = a3 - 0x36bcL;
        v0 = 0x4L;
        a4 = v0;
        v0 = 0x2L;
        a5 = v0;
        v0 = loadDrgnBinFile(a0, a1, a2, a3, a4, a5);
        a0 = 0x2L;
        a1 = 0xaL;
        scriptStartEffect(a0, a1);
        v0 = 0x8012_0000L;
        v1 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x28c0L).setu(0);

        //LAB_8010172c
        v0 = 0x49L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x49:
        a0 = 0xffL;
        FUN_8002437c(a0);
        a0 = 0x8011_0000L;
        a0 = a0 + 0x4228L;
        a1 = 0;
        a2 = a1;
        FUN_8010376c(a0, a1, a2);
        s0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

        v0 = FUN_800fc8ec(a0);
        a0 = 0x9fL;
        a1 = a0;
        a2 = 0x3cL;
        a3 = v0;
        v0 = FUN_80103818(a0, a1, a2, a3);
        a0 = v0;
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x2418L).setu(a0);
        FUN_80104b60(a0);
        a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();
        a1 = 0xffL;
        FUN_80103168(a0);
        v1 = 0x800c_0000L;
        v0 = 0x4aL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x4a:
        a0 = 0;
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x2374L).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x1f6cL).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x22f8L).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x22f4L).setu(0);
        FUN_8002df60(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x31ecL;
        a1 = 0x1L;
        FUN_8010f130(a0, a1);
        s0 = 0;
        a0 = 0xffL;
        v0 = 0x8012_0000L;
        v1 = v0 - 0x2838L;

        //LAB_801001a8
        do {
          MEMORY.ref(1, v1).offset(0x0L).setu(a0);
          MEMORY.ref(1, v1).offset(0x1L).setu(s0);
          MEMORY.ref(2, v1).offset(0x2L).setu(0);
          s0 = s0 + 0x1L;
          v1 = v1 + 0x4L;
        } while((int)s0 < 0x7L);

        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v1 = 0x800c_0000L;
        v0 = 0x4bL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x4b:
        v0 = 0x8012_0000L;
        s1 = v0 - 0x2370L;
        a0 = s1;
        v0 = FUN_8010ecec(a0);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = MEMORY.ref(4, s1).offset(0x10L).get();

        if(v0 < 0x3L) {
          break;
        }
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1_3 = new Ref<>();
        final Ref<Long> refA2_3 = new Ref<>();
        v0 = FUN_8002efb8(a0, refA1_3, refA2_3);
        MEMORY.ref(4, a1).setu(refA1_3.get());
        MEMORY.ref(4, a2).setu(refA2_3.get());

        if(v0 == 0) {
          break;
        }

        v1 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        switch((int)v1) {
          case 0x4:
            //LAB_80100264
            v1 = 0x8012_0000L;
            v0 = 0x1L;
            MEMORY.ref(4, v1).offset(-0x2374L).setu(v0);

          case 0x0:
          case 0x3:
            //LAB_80100270
            a0 = 0;
            FUN_800414a0(a0);
            v1 = 0x800c_0000L;
            v0 = 0x4cL;
            return;

          case 0x1:
          case 0x2:
            //LAB_80100288
            a0 = 0x8012_0000L;
            a0 -= 0x2370L;
            a1 = 0x800c_0000L;
            v0 = MEMORY.ref(1, a0).offset(0xcL).get();
            v1 = 0x75L;

            //LAB_801004ac
            MEMORY.ref(4, a1).offset(-0x23d8L).setu(v1);
            v0 = v0 + 0x1L;
            MEMORY.ref(1, a0).offset(0xcL).setu(v0);
            return;
        }

        //LAB_801002a0
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(1, s1).offset(0xcL).get();
        a0 = 0x4eL;

        //LAB_801007f8
        MEMORY.ref(4, v0).offset(-0x23d8L).setu(a0);
        v1 = v1 + 0x1L;
        MEMORY.ref(1, s1).offset(0xcL).setu(v1);
        break;

      case 0x4c:
        v0 = 0x8012_0000L;
        s1 = v0 - 0x2370L;
        a0 = s1;
        v0 = FUN_8010ecec(a0);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;
        v0 = FUN_800426c4(a0, a1, a2);
        if(v0 == 0) {
          v0 = 0x2L;
          break;
        }
        v0 = 0x2L;
        a2 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        if(a2 == v0) {
          //LAB_801007ec
          v0 = 0x800c_0000L;

          //LAB_801007f0
          v1 = MEMORY.ref(1, s1).offset(0xcL).get();
          a0 = 0x75L;

          //LAB_801007f8
          MEMORY.ref(4, v0).offset(-0x23d8L).setu(a0);
          v1 = v1 + 0x1L;
          MEMORY.ref(1, s1).offset(0xcL).setu(v1);
          break;
        }

        a0 = 0x8012_0000L;
        //LAB_80100318
        //LAB_80100320
        //TODO this was a pretty big restructure... needs to be verified
        if(a2 == 0x1L || a2 == 0x2L || a2 >= 0x4L || _8011dc8c.get() != 0) {
          //LAB_8010049c
          a0 = a0 - 0x2370L;

          //LAB_801004a0
          a1 = 0x800c_0000L;
          v0 = MEMORY.ref(1, a0).offset(0xcL).get();
          v1 = 0x4eL;

          //LAB_801004ac
          MEMORY.ref(4, a1).offset(-0x23d8L).setu(v1);
          v0 = v0 + 0x1L;
          MEMORY.ref(1, a0).offset(0xcL).setu(v0);
          break;
        }

        v1 = 0x800c_0000L;
        v0 = 0x4dL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x4d:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0;
        a1 = 0x8010_0000L;
        a1 = a1 - 0x4860L;
        a3 = 0x580L;
        s1 = 0x8012_0000L;
        a2 = MEMORY.ref(4, s1).offset(-0x2840L).get();
        v0 = 0x80L;
        a4 = v0;
        v0 = FUN_8002e908(a0, a1, a2, a3, a4);
        a0 = 0;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1_4 = new Ref<>();
        final Ref<Long> refA2_4 = new Ref<>();
        v0 = FUN_8002efb8(a0, refA1_4, refA2_4);
        MEMORY.ref(4, a1).setu(refA1_4.get());
        MEMORY.ref(4, a2).setu(refA2_4.get());

        v0 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        if(v0 != 0) {
          a0 = 0x8012_0000L;
        } else {
          a0 = 0x8012_0000L;
          s0 = 0;
          t1 = s1;
          t0 = 0x8012_0000L;
          a3 = 0x1L;
          a2 = 0xffL;
          v0 = 0x8012_0000L;
          a0 = v0 - 0x2838L;
          a1 = s0;

          //LAB_801003cc
          do {
            v0 = MEMORY.ref(4, t1).offset(-0x2840L).get();

            v1 = v0 + a1;
            v0 = MEMORY.ref(4, v1).offset(0x14L).get();

            if(v0 != 0) {
              v0 = MEMORY.ref(1, v1).offset(0x14L).get();
              MEMORY.ref(1, a0).offset(0x0L).setu(v0);
              MEMORY.ref(4, t0).offset(-0x22f4L).setu(a3);
            } else {
              //LAB_801003fc
              MEMORY.ref(1, a0).offset(0x0L).setu(a2);
            }

            //LAB_80100400
            MEMORY.ref(1, a0).offset(0x1L).setu(s0);
            MEMORY.ref(2, a0).offset(0x2L).setu(0);
            a0 = a0 + 0x4L;
            s0 = s0 + 0x1L;
            a1 = a1 + 0x4L;
          } while((int)s0 < 0x6L);

          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x2840L).get();

          v0 = MEMORY.ref(4, a0).offset(0x2cL).get();

          v1 = 0x8012_0000L;
          if(v0 != 0) {
            a0 = MEMORY.ref(1, a0).offset(0x2cL).get();
            v0 = 0x1L;
            MEMORY.ref(4, v1).offset(-0x22f4L).setu(v0);
            v0 = 0x8012_0000L;
            MEMORY.ref(1, v0).offset(-0x2820L).setu(a0);
          } else {
            //LAB_80100450
            v1 = 0x8012_0000L;
            v0 = 0xffL;
            MEMORY.ref(1, v1).offset(-0x2820L).setu(v0);
          }

          //LAB_8010045c
          v1 = 0x8012_0000L;
          v1 = v1 - 0x2838L;
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x2840L).get();
          v0 = 0x6L;
          MEMORY.ref(1, v1).offset(0x19L).setu(v0);
          MEMORY.ref(2, v1).offset(0x1aL).setu(0);
          v1 = MEMORY.ref(4, a0).offset(0x34L).get();
          v0 = 0x8012_0000L;
          MEMORY.ref(4, v0).offset(-0x22f8L).setu(v1);
          v1 = MEMORY.ref(4, a0).offset(0x3cL).get();
          v0 = 0x1L;
          if(v1 != v0) {
            a0 = 0x8012_0000L;
          } else {
            a0 = 0x8012_0000L;
            v0 = 0x8012_0000L;
            MEMORY.ref(4, v0).offset(-0x1f6cL).setu(v1);
          }
        }

        //LAB_8010049c
        a0 = a0 - 0x2370L;

        //LAB_801004a0
        a1 = 0x800c_0000L;
        v0 = MEMORY.ref(1, a0).offset(0xcL).get();
        v1 = 0x4eL;

        //LAB_801004ac
        MEMORY.ref(4, a1).offset(-0x23d8L).setu(v1);
        v0 = v0 + 0x1L;
        MEMORY.ref(1, a0).offset(0xcL).setu(v0);
        break;

      case 0x4f:
        s1 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s1).offset(-0x23bcL).get();

        v0 = v0 & 0x40L;
        if(v0 == 0) {
          s0 = 0x8012_0000L;
        } else {
          s0 = 0x8012_0000L;
          a0 = 0x3L;
          playSound(a0);
          v1 = 0x800c_0000L;
          v0 = 0x78L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          s0 = 0x8012_0000L;
        }

        //LAB_801004ec
        a0 = s0 - 0x28c0L;
        a1 = 0x4L;
        v0 = FUN_801040c0(a0, a1);
        if(v0 != 0) {
          a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

          v0 = FUN_800fc8ec(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(-0x2418L).get();

          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
        }

        //LAB_8010051c
        v0 = MEMORY.ref(4, s1).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          v0 = 0x1L;

          //LAB_8010157c
          v0 = 0x8012_0000L;

          //LAB_80101580
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          break;
        }
        v0 = 0x1L;
        s0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

        if(s0 == v0) {
          //LAB_801005b4
          v0 = 0x8012_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x22f4L).get();

          if(v0 == 0) {
            //LAB_80100650
            a0 = 0x28L;
            playSound(a0);
            v0 = 0x8012_0000L;

            //LAB_80101580
            a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            a1 = 0;
            FUN_80103168(a0);
            break;
          }

          a0 = 0x2L;
          playSound(a0);
          v0 = 0x8012_0000L;
          v1 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x28a8L).setu(s0);
          v0 = 0x5eL;
        } else {
          if((int)s0 >= 0x2L) {
            v0 = 0x2L;
            //LAB_80100558
            if(s0 == v0) {
              v0 = 0x3L;

              //LAB_801005e4
              s1 = 0x8012_0000L;
              v0 = MEMORY.ref(4, s1).offset(-0x1f6cL).get();

              if(v0 == 0) {
                //LAB_80100650
                a0 = 0x28L;
                playSound(a0);
                v0 = 0x8012_0000L;

                //LAB_80101580
                a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
                a1 = 0;
                FUN_80103168(a0);
                break;
              }

              a0 = 0x2L;
              playSound(a0);
              v0 = 0x8012_0000L;
              v0 = MEMORY.ref(4, v0).offset(-0x2840L).get();

              if(MEMORY.ref(4, v0).offset(0x38L).get() < 0x5L) {
                v0 = 0x8012_0000L;
                v1 = 0x800c_0000L;
                MEMORY.ref(4, v0).offset(-0x28a8L).setu(s0);
                v0 = 0x60L;
              } else {
                //LAB_80100630
                a0 = 0x8012_0000L;
                a0 = a0 - 0x2fbcL;
                a1 = 0;
                FUN_8010f130(a0, a1);
                v1 = 0x800c_0000L;
                v0 = 0x6eL;
                MEMORY.ref(4, s1).offset(-0x1f6cL).setu(0);
              }
            } else {
              v0 = 0x3L;
              if(s0 == v0) {
                v0 = 0x8012_0000L;

                //LAB_80100660
                a0 = 0x2L;
                playSound(a0);
                v1 = 0x800c_0000L;
                v0 = 0x50L;
              } else {
                v0 = 0x8012_0000L;

                //LAB_80101580
                a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
                a1 = 0;
                FUN_80103168(a0);
                break;
              }
            }
          } else {
            v0 = 0x2L;
            if(s0 == 0) {
              v0 = 0x8012_0000L;

              //LAB_80100570
              v0 = 0x8012_0000L;
              v0 = MEMORY.ref(4, v0).offset(-0x22f4L).get();

              if(v0 == 0) {
                v0 = 0x8012_0000L;
                v0 = MEMORY.ref(4, v0).offset(-0x22f8L).get();

                if(v0 == 0) {
                  //LAB_80100650
                  a0 = 0x28L;
                  playSound(a0);
                  v0 = 0x8012_0000L;

                  //LAB_80101580
                  a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
                  a1 = 0;
                  FUN_80103168(a0);
                  break;
                }
              }

              //LAB_80100598
              a0 = 0x2L;
              playSound(a0);
              v0 = 0x8012_0000L;
              v1 = 0x800c_0000L;
              MEMORY.ref(4, v0).offset(-0x28a8L).setu(0);
              v0 = 0x62L;
            } else {
              v0 = 0x8012_0000L;

              //LAB_80101580
              a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
              a1 = 0;
              FUN_80103168(a0);
              break;
            }
          }
        }

        //LAB_80100670
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x50:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3998L;
        a1 = 0;
        FUN_8010f130(a0, a1);
        a1 = 0;
        v0 = 0x8012_0000L;
        v1 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = 0x51L;

        //LAB_8010069c
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        FUN_80103168(a0);
        break;

      case 0x51:
        v0 = 0x8012_0000L;
        s0 = v0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        a0 = 0x8012_0000L;

        if(v0 != 0) {
          a0 = a0 - 0x3204L;
          a1 = 0x2L;
          FUN_8010f130(a0, a1);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          MEMORY.ref(4, s0).offset(0x18L).setu(v0);
          v0 = 0x52L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x52:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v1 = v0;
        v0 = 0x1L;
        if(v1 == v0) {
          v0 = 0x2L;
          //LAB_80100714
          v1 = 0x800c_0000L;
          v0 = 0x53L;
        } else {
          v0 = 0x8012_0000L;
          if(v1 != 0x2L) {
            //LAB_80101580
            a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            a1 = 0;
            FUN_80103168(a0);
            break;
          }

          //LAB_80100720
          v1 = 0x800c_0000L;
          v0 = 0x4fL;
        }

        //LAB_80100728
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x53:
        a0 = 0;
        FUN_800414a0(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x31ecL;
        a1 = 0x1L;
        FUN_8010f130(a0, a1);
        a1 = 0;
        v0 = 0x8012_0000L;
        v1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = 0x1L;
        MEMORY.ref(1, v1).offset(-0x1f2cL).setu(v0);
        FUN_80103168(a0);
        v1 = 0x800c_0000L;
        v0 = 0x54L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x54:
        v0 = 0x8012_0000L;
        s1 = v0 - 0x2370L;
        a0 = s1;
        v0 = FUN_8010ecec(a0);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = MEMORY.ref(4, s1).offset(0x10L).get();

        if(v0 < 0x3L) {
          break;
        }
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;
        v0 = FUN_800426c4(a0, a1, a2);
        if(v0 == 0) {
          break;
        }

        a2 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        if(a2 == 0 || a2 == 0x3L) {
          //LAB_801007dc
          v1 = 0x800c_0000L;
          v0 = 0x55L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }

        //LAB_801007ec
        v0 = 0x800c_0000L;

        //LAB_801007f0
        v1 = MEMORY.ref(1, s1).offset(0xcL).get();
        a0 = 0x75L;

        //LAB_801007f8
        MEMORY.ref(4, v0).offset(-0x23d8L).setu(a0);
        v1 = v1 + 0x1L;
        MEMORY.ref(1, s1).offset(0xcL).setu(v1);
        break;

      case 0x55:
        v0 = 0x8012_0000L;
        s0 = v0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = 0x8012_0000L;
        s1 = v0 - 0x2898L;
        a0 = s1;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        a2 = 0x1L;
        v0 = FUN_80109b08(a0, a1);
        v0 = 0x8012_0000L;
        v0 = MEMORY.ref(1, v0).offset(-0x1f2cL).get();

        if(v0 != 0) {
          v0 = 0x1L;
          break;
        }
        v1 = MEMORY.ref(4, s1).offset(0x8L).get();

        v0 = 0x800c_0000L;
        if(v1 == 0x1L) {
          v1 = MEMORY.ref(1, s0).offset(0xcL).get();
          a0 = 0x75L;
          //LAB_80100878
        } else if(v1 == 0x2L) {
          v1 = MEMORY.ref(1, s0).offset(0xcL).get();
          a0 = 0x75L;
          //LAB_8010088c
        } else if(MEMORY.ref(1, s1).offset(0x5L).get() != 0) {
          v1 = MEMORY.ref(1, s0).offset(0xcL).get();
          a0 = 0x56L;
          //LAB_801008a8
        } else if(MEMORY.ref(1, s1).offset(0x6L).get() < 0x9L) {
          v1 = MEMORY.ref(1, s0).offset(0xcL).get();
          a0 = 0x73L;
        } else {
          //LAB_801008c8
          v1 = MEMORY.ref(1, s0).offset(0xcL).get();
          a0 = 0x59L;
        }

        //LAB_801008d0
        MEMORY.ref(4, v0).offset(-0x23d8L).setu(a0);
        v1 = v1 + 0x1L;
        MEMORY.ref(1, s0).offset(0xcL).setu(v1);
        break;

      case 0x56:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
        } else {
          a0 = 0x8012_0000L;
          a0 = a0 - 0x31f8L;
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x57L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x57:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = 0x8012_0000L;
        s0 = v0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
        } else {
          a0 = 0x8012_0000L;
          a0 = a0 - 0x3c5cL;
          a1 = 0x2L;
          FUN_8010f130(a0, a1);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          MEMORY.ref(4, s0).offset(0x18L).setu(v0);
          v0 = 0x58L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x58:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v1 = v0;
        v0 = 0x1L;
        if(v1 == v0) {
          v0 = 0x2L;
          //LAB_80100998
          a0 = 0;
          v0 = FUN_8002dbdc(a0);
          a0 = 0;
          a1 = 0x8012_0000L;
          a1 = a1 - 0x2844L;
          s0 = 0x8012_0000L;
          a2 = s0 - 0x2848L;

          final Ref<Long> refA1_5 = new Ref<>();
          final Ref<Long> refA2_5 = new Ref<>();
          v0 = FUN_8002efb8(a0, refA1_5, refA2_5);
          MEMORY.ref(4, a1).setu(refA1_5.get());
          MEMORY.ref(4, a2).setu(refA2_5.get());

          v0 = MEMORY.ref(4, s0).offset(-0x2848L).get();

          v1 = 0x800c_0000L;
          if(v0 == 0) {
            v0 = 0x59L;
          } else {
            //LAB_801009d0
            v0 = 0x74L;
          }
        } else {
          v0 = 0x2L;
          if(v1 == v0) {
            v0 = 0x8012_0000L;
          } else {
            v0 = 0x8012_0000L;

            //LAB_80101580
            a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            a1 = 0;
            FUN_80103168(a0);
            break;
          }

          //LAB_801009d8
          v1 = 0x800c_0000L;
          v0 = 0x5dL;
        }

        //LAB_801009e0
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x59:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0;
          break;
        }
        a0 = 0;
        a1 = 0x8010_0000L;
        a1 = a1 - 0x4860L;
        a2 = 0x9L;
        v0 = FUN_8002f0d4(a0, a1, a2);
        v1 = 0x8012_0000L;
        a0 = v0;
        v0 = 0x2L;
        if(a0 == v0) {
          MEMORY.ref(4, v1).offset(-0x2848L).setu(a0);

          //LAB_80101854
          v1 = 0x800c_0000L;

          //LAB_80101858
          v0 = 0x74L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }
        MEMORY.ref(4, v1).offset(-0x2848L).setu(a0);
        v0 = 0x1L;
        if(a0 < 0x3L) {
          if(a0 == v0) {
            a0 = 0x8012_0000L;

            //LAB_80100ed0
            v1 = 0x800c_0000L;

            //LAB_80100ed4
            v0 = 0x75L;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
          a0 = 0x8012_0000L;
          a0 = a0 - 0x3200L;
        } else {
          //LAB_80100a4c
          v0 = 0x7L;
          if(a0 != 0x4L) {
            if(a0 == v0) {
              a0 = 0x8012_0000L;

              //LAB_80101854
              v1 = 0x800c_0000L;

              //LAB_80101858
              v0 = 0x74L;
              MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
              break;
            }
            a0 = 0x8012_0000L;
            a0 = a0 - 0x3200L;
          } else {
            //LAB_80100a68
            v1 = 0x800c_0000L;
            v0 = 0x79L;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
        }

        //LAB_80100a78
        a1 = 0x1L;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x5aL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x5a:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        s0 = 0x8012_0000L;
        s0 = s0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        v0 = MEMORY.ref(4, s0).offset(0x10L).get();

        if(v0 < 0x3L) {
          break;
        }
        a0 = 0;
        a1 = 0x1L;
        a2 = 0x14L;
        v0 = FUN_800412e0(a0, a1, a2);
        a0 = 0;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        a2 = 0x8012_0000L;
        a2 = a2 - 0x2848L;
        v0 = FUN_800426c4(a0, a1, a2);
        a0 = 0;
        a1 = 0x8010_0000L;
        a1 = a1 - 0x4860L;
        a3 = a0;
        v0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x2300L).get();
        t0 = 0x8012_0000L;
        a2 = v1;
        v0 = 0x800c_0000L;
        t1 = MEMORY.ref(4, v0).offset(-0x53a0L).get();
        v0 = MEMORY.ref(4, t0).offset(-0x22fcL).get();
        s0 = 0x6L;
        v0 = v0 + 0x7fL;
        MEMORY.ref(4, v1).offset(0x580L).setu(t1);
        v1 = -0x80L;
        v0 = v0 & v1;
        a4 = v0;
        v0 = FUN_8002eb28(a0, a1, a2, a3, a4);
        a0 = 0xffL;
        v0 = 0x8012_0000L;
        v0 = v0 - 0x2838L;
        v1 = v0 + 0x18L;
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x1f6cL).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x22f8L).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, v0).offset(-0x22f4L).setu(0);

        //LAB_80100b58
        do {
          MEMORY.ref(1, v1).offset(0x0L).setu(a0);
          s0 = s0 - 0x1L;
          v1 = v1 - 0x4L;
        } while((int)s0 >= 0);

        v1 = 0x800c_0000L;
        v0 = 0x5bL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x5b:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1_6 = new Ref<>();
        final Ref<Long> refA2_6 = new Ref<>();
        v0 = FUN_8002efb8(a0, refA1_6, refA2_6);
        MEMORY.ref(4, a1).setu(refA1_6.get());
        MEMORY.ref(4, a2).setu(refA2_6.get());

        if(v0 == 0) {
          break;
        }

        v0 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        v1 = 0x800c_0000L;
        if(v0 != 0) {
          v0 = 0x74L;
        } else {
          //LAB_80100bcc
          v0 = 0x5cL;
        }

        //LAB_80100bd0
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        a0 = 0;
        v0 = FUN_80041420(a0);
        a0 = 0;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        a2 = 0x8012_0000L;
        a2 = a2 - 0x2848L;
        v0 = FUN_800426c4(a0, a1, a2);

        //LAB_80100bf4
        v1 = 0x8012_0000L;
        v1 = v1 - 0x2370L;
        v0 = MEMORY.ref(1, v1).offset(0xcL).get();

        v0 = v0 + 0x1L;
        MEMORY.ref(1, v1).offset(0xcL).setu(v0);
        break;

      case 0x5c:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0;
          break;
        }
        a0 = 0;
        a1 = 0x8010_0000L;
        a1 = a1 - 0x4860L;
        final Memory.TemporaryReservation tmpA2 = MEMORY.temp(0x30); // Not sure exactly how much space we need here
        a2 = tmpA2.address; //sp + 0x20L;
        v0 = 0x8012_0000L;
        s0 = v0 - 0x2848L;
        a3 = s0;
        v0 = 0x1L;
        a4 = 0;
        a5 = v0;

        final Ref<Long> fileCount = new Ref<>();
        FUN_8002ed48(a0, a1, a2, fileCount, a4, a5);
        MEMORY.ref(4, a3).setu(fileCount.get());

        a0 = 0;
        v0 = 0x8012_0000L;
        s1 = v0 - 0x2844L;
        a1 = s1;
        a2 = s0;

        final Ref<Long> refA1_7 = new Ref<>();
        final Ref<Long> refA2_7 = new Ref<>();
        v0 = FUN_8002efb8(a0, refA1_7, refA2_7);
        MEMORY.ref(4, a1).setu(refA1_7.get());
        MEMORY.ref(4, a2).setu(refA2_7.get());

        a1 = tmpA2.get().offset(0x20L).get();//MEMORY.ref(4, sp).offset(0x40L).get();

        if((int)a1 >= 0) {
          a0 = 0;
        } else {
          a0 = 0;
          a1 = a1 + 0x3fL;
        }

        //LAB_80100c88
        a1 = (int)a1 >> 6;
        a2 = 0x1L;
        v0 = FUN_80041600(a0, a1, a2);
        a0 = 0;
        a1 = s1;
        a2 = s0;
        v0 = FUN_800426c4(a0, a1, a2);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3a28L;
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x5dL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

        tmpA2.release();
        break;

      case 0x5e:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3738L;
        a1 = 0;
        FUN_8010f130(a0, a1);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v1 = 0x800c_0000L;
        v0 = 0x5fL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x5f:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = 0x8012_0000L;
        s0 = v0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x372cL;
        a1 = 0x2L;
        FUN_8010f130(a0, a1);
        v0 = 0x1L;
        v1 = 0x800c_0000L;
        MEMORY.ref(4, s0).offset(0x18L).setu(v0);

        //LAB_80100db4
        v0 = 0x63L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x60:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x28f0L;
        a1 = 0;
        FUN_8010f130(a0, a1);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v1 = 0x800c_0000L;
        v0 = 0x61L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x61:
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = 0x8012_0000L;
        s0 = v0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0x8012_0000L;
          break;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3744L;
        a1 = 0x2L;
        FUN_8010f130(a0, a1);
        a1 = 0;
        a0 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
        v0 = 0x1L;
        MEMORY.ref(4, s0).offset(0x18L).setu(v0);
        FUN_80103168(a0);
        v1 = 0x800c_0000L;

        //LAB_80100db4
        v0 = 0x63L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x62:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2fa0L;
        a1 = 0x2L;
        FUN_8010f130(a0, a1);
        a1 = 0;
        v0 = 0x8012_0000L;
        v1 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = 0x63L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        v1 = 0x8012_0000L;
        v0 = 0x1L;
        MEMORY.ref(4, v1).offset(-0x2358L).setu(v0);
        FUN_80103168(a0);
        break;

      case 0x63:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        v1 = v0;
        v0 = 0x1L;
        if(v1 == v0) {
          v0 = 0x2L;
          //LAB_80100e2c
          a0 = 0;
          FUN_800414a0(a0);
          v1 = 0x800c_0000L;
          v0 = 0x64L;
        } else {
          v0 = 0x2L;
          if(v1 == v0) {
            v0 = 0x8012_0000L;
          } else {
            v0 = 0x8012_0000L;

            //LAB_80101580
            a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            a1 = 0;
            FUN_80103168(a0);
            break;
          }

          //LAB_80100e40
          v1 = 0x800c_0000L;
          v0 = 0x4fL;
        }

        //LAB_80100e48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x64:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;
        v0 = FUN_800426c4(a0, a1, a2);
        if(v0 == 0) {
          break;
        }

        a2 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        if(a2 >= 0x3L) {
          if(a2 != 0x3L) {
            v1 = 0x800c_0000L;

            //LAB_80101204
            v0 = 0x71L;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
          v1 = 0x800c_0000L;
          a0 = 0x8012_0000L;
          a0 = a0 - 0x2a3cL;
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x76L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }

        //LAB_80100ebc
        v1 = 0x800c_0000L;
        if(a2 == 0) {
          v0 = 0x65L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }

        //LAB_80100ed0
        //LAB_80100ed4
        v0 = 0x75L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x65:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        s3 = 0x8012_0000L;
        v0 = 0x800c_0000L;
        s2 = 0x800c_0000L;
        a0 = MEMORY.ref(4, s3).offset(-0x2840L).get();
        v1 = MEMORY.ref(4, v0).offset(-0x53a0L).get();
        v0 = 0x66L;
        MEMORY.ref(4, s2).offset(-0x23d8L).setu(v0);
        v0 = 0x8012_0000L;
        MEMORY.ref(4, a0).offset(0x0L).setu(v1);
        v1 = MEMORY.ref(4, v0).offset(-0x28a8L).get();
        t0 = 0x1L;
        if(v1 == t0) {
          s0 = 0x5L;
          //LAB_80101104
          v0 = a0 + 0x14L;

          //LAB_80101108
          do {
            MEMORY.ref(4, v0).offset(0x14L).setu(0);
            s0 = s0 - 0x1L;
            v0 = v0 - 0x4L;
          } while((int)s0 >= 0);

          v0 = 0x8012_0000L;
          MEMORY.ref(4, v0).offset(-0x22f4L).setu(0);
          v0 = 0x8012_0000L;
          v1 = 0x800c_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x2840L).get();
          v0 = 0x68L;
          MEMORY.ref(4, v1).offset(-0x23d0L).setu(v0);
          MEMORY.ref(4, a0).offset(0x2cL).setu(0);
          break;
        }
        s0 = 0x5L;
        v0 = 0x2L;
        if((int)v1 < 0x2L) {
          if(v1 == 0) {
            a0 = 0;

            //LAB_80100f50
            a1 = 0x8010_0000L;
            a1 = a1 - 0x4860L;
            final Memory.TemporaryReservation tmpA2_1 = MEMORY.temp(0x30); // Not sure exactly how much space we need here
            a2 = tmpA2_1.address; //sp + 0x20L;
            v0 = 0x8012_0000L;
            s0 = v0 - 0x2848L;
            a3 = s0;
            a4 = 0;
            a5 = t0;

            final Ref<Long> fileCount_1 = new Ref<>();
            FUN_8002ed48(a0, a1, a2, fileCount_1, a4, a5);
            MEMORY.ref(4, a3).setu(fileCount_1.get());

            a0 = 0;
            v0 = 0x8012_0000L;
            s1 = v0 - 0x2844L;
            a1 = s1;
            a2 = s0;

            final Ref<Long> refA1_8 = new Ref<>();
            final Ref<Long> refA2_8 = new Ref<>();
            v0 = FUN_8002efb8(a0, refA1_8, refA2_8);
            MEMORY.ref(4, a1).setu(refA1_8.get());
            MEMORY.ref(4, a2).setu(refA2_8.get());

            a2 = tmpA2_1.get().offset(0x20L).get(); //MEMORY.ref(4, sp).offset(0x40L).get();

            if((int)a2 >= 0) {
              a1 = tmpA2_1.address + 0x28L; //sp + 0x48L;
            } else {
              a1 = tmpA2_1.address + 0x28L; //sp + 0x48L;
              a2 = a2 + 0x3fL;
            }

            //LAB_80100fa0
            v0 = 0x800_0000L;
            v0 = v0 | 0x7eL;
            a0 = 0;
            a2 = (int)a2 >> 6;
            a2 = a2 << 7;
            a2 = a2 + v0;
            a3 = 0x1L;
            v0 = FUN_80041070(a0, a1, a2, a3);
            a0 = 0;
            a1 = s1;
            a2 = s0;
            v0 = FUN_800426c4(a0, a1, a2);
            v0 = tmpA2_1.get().offset(0x28L).get(); //MEMORY.ref(1, sp).offset(0x48L).get();

            tmpA2_1.release();

            a2 = 0;
            if(v0 == 0) {
              v0 = 0x6fL;
              MEMORY.ref(4, s2).offset(-0x23d8L).setu(v0);
              break;
            }

            //LAB_80100fec
            a3 = a2;
            s0 = a2;
            t0 = 0xffL;
            v0 = 0x8012_0000L;
            a1 = v0 - 0x2838L;
            v1 = 0x800c_0000L;
            a0 = MEMORY.ref(4, s3).offset(-0x2840L).get();
            v0 = 0x69L;
            MEMORY.ref(4, v1).offset(-0x23d0L).setu(v0);
            MEMORY.ref(4, a0).offset(0x34L).setu(0);

            //LAB_80101014
            do {
              if(MEMORY.ref(1, a1).offset(0x0L).get() != t0) {
                if(v0 < 0xc0L) {
                  //LAB_80101034
                  a2 = a2 + 0x1L;
                } else {
                  a3 = a3 + 0x1L;
                }
              }

              //LAB_80101038
              s0 = s0 + 0x1L;
              a1 = a1 + 0x4L;
            } while((int)s0 < 0x7L);

            a1 = a1 + 0x4L;
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(2, v0).offset(-0x5254L).getSigned();
            v0 = v0 + a2;

            //LAB_80101070
            if((int)v0 >= 0x100L && a2 != 0 || _800badae.getSigned() + a3 > 0x20L && a3 != 0) {
              //LAB_80101090
              a0 = 0x8012_0000L;
              a0 = a0 - 0x30acL;
              a1 = 0;
              FUN_8010f130(a0, a1);
              v1 = 0x8012_0000L;
              v0 = 0x3L;
              MEMORY.ref(4, v1).offset(-0x28a8L).setu(v0);
              break;
            }

            v0 = 0x8012_0000L;

            //LAB_801010ac
            MEMORY.ref(4, v0).offset(-0x22f4L).setu(0);
            s0 = 0;
            v0 = 0x8012_0000L;
            s1 = v0 - 0x2838L;

            //LAB_801010bc
            do {
              a0 = MEMORY.ref(1, s1).offset(0x0L).get();
              s1 = s1 + 0x4L;
              s0 = s0 + 0x1L;
              v0 = FUN_80023484(a0);
            } while((int)s0 < 0x7L);

            v0 = 0x8012_0000L;
            v0 = MEMORY.ref(4, v0).offset(-0x2840L).get();
            s0 = 0x5L;
            v0 = v0 + 0x14L;

            //LAB_801010e4
            do {
              MEMORY.ref(4, v0).offset(0x14L).setu(0);
              s0 = s0 - 0x1L;
              v0 = v0 - 0x4L;
            } while((int)s0 >= 0);

            v0 = 0x8012_0000L;
            v0 = MEMORY.ref(4, v0).offset(-0x2840L).get();
            MEMORY.ref(4, v0).offset(0x2cL).setu(0);
            break;
          }
          a0 = 0;

          break;
        }

        //LAB_80100f40
        if(v1 != v0) {
          break;
        }

        v0 = 0x8012_0000L;

        //LAB_8010113c
        MEMORY.ref(4, a0).offset(0x3cL).setu(v1);
        v1 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x1f6cL).setu(0);
        v0 = 0x67L;
        MEMORY.ref(4, a0).offset(0x2cL).setu(0);
        MEMORY.ref(4, v1).offset(-0x23d0L).setu(v0);
        break;

      case 0x66:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          a0 = 0;
          break;
        }
        a0 = 0;
        a1 = 0x1L;
        a2 = a1;
        v0 = FUN_800412e0(a0, a1, a2);
        a0 = 0;
        v0 = 0x8012_0000L;
        s2 = v0 - 0x2844L;
        a1 = s2;
        s0 = 0x8012_0000L;
        s1 = s0 - 0x2848L;
        a2 = s1;
        v0 = FUN_800426c4(a0, a1, a2);
        a0 = 0;
        a1 = 0x8010_0000L;
        a1 = a1 - 0x4860L;
        a3 = 0x580L;
        v0 = 0x8012_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x2840L).get();
        v0 = 0x80L;
        a4 = v0;
        v0 = FUN_8002eb28(a0, a1, a2, a3, a4);
        a0 = 0;
        a1 = s2;
        a2 = s1;

        final Ref<Long> refA1_9 = new Ref<>();
        final Ref<Long> refA2_9 = new Ref<>();
        v0 = FUN_8002efb8(a0, refA1_9, refA2_9);
        MEMORY.ref(4, a1).setu(refA1_9.get());
        MEMORY.ref(4, a2).setu(refA2_9.get());

        a0 = 0;
        v0 = FUN_80041420(a0);
        v0 = MEMORY.ref(4, s0).offset(-0x2848L).get();

        a0 = 0;
        if(v0 != 0) {
          a1 = s2;
          a2 = s1;
          v0 = FUN_800426c4(a0, a1, a2);
          v1 = 0x800c_0000L;

          //LAB_80101204
          v0 = 0x71L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }

        //LAB_80101210
        a1 = s2;
        a2 = s1;
        v0 = FUN_800426c4(a0, a1, a2);
        v0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x28a8L).get();

        outer:
        if((int)v1 >= 0) {
          if((int)v1 < 0x2L) {
            s0 = 0x5L;

            //LAB_8010124c
            v1 = 0xffL;
            v0 = 0x8012_0000L;
            v0 = v0 - 0x2838L;
            v0 = v0 + 0x14L;

            //LAB_8010125c
            do {
              MEMORY.ref(1, v0).offset(0x0L).setu(v1);
              s0 = s0 - 0x1L;
              v0 = v0 - 0x4L;
            } while((int)s0 >= 0);
          } else if(v1 != 0x2L) {
            break outer;
          }

          //LAB_8010126c
          v1 = 0x8012_0000L;
          v0 = 0xffL;
          MEMORY.ref(1, v1).offset(-0x2820L).setu(v0);
        }

        //LAB_80101b14
        v0 = 0x800c_0000L;

        //LAB_80101b18
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x23d8L).setu(v1);
        break;

      case 0x67:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x3734L;
        a1 = 0;
        FUN_8010f130(a0, a1);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v1 = 0x800c_0000L;

        //LAB_80101650
        v0 = 0x6dL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x68:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x36ecL;
        a1 = 0;
        FUN_8010f130(a0, a1);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v1 = 0x800c_0000L;

        //LAB_80101650
        v0 = 0x6dL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x69:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          v0 = 0x8012_0000L;
        } else {
          v0 = 0x8012_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x22f8L).get();

          if(v0 == 0) {
            a0 = 0x8012_0000L;

            //LAB_8010175c
            v1 = 0x800c_0000L;
            v0 = 0x4fL;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
          a0 = 0x8012_0000L;
          a0 = a0 - 0x3994L;
          a1 = 0x1L;
          FUN_8010f130(a0, a1);
          v0 = 0x800c_0000L;
          v1 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2414L).setu(0);
          v0 = 0x6aL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x6a:
        s0 = 0x8012_0000L;
        s0 = s0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        v0 = MEMORY.ref(4, s0).offset(0x10L).get();

        if(v0 < 0x3L) {
          v0 = 0x8012_0000L;

          //LAB_80101580
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          break;
        }
        v0 = 0x8012_0000L;
        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x2414L).get();

        if(v0 != 0) {
          a0 = 0x70L;
        } else {
          a0 = 0x70L;
          a0 = 0xd3L;
          a1 = a0;
          a2 = 0x44L;
          a3 = 0x50L;
          v0 = FUN_80103818(a0, a1, a2, a3);
          v1 = 0x1fL;
          MEMORY.ref(4, s0).offset(-0x2414L).setu(v0);
          MEMORY.ref(1, v0).offset(0x3cL).setu(v1);
          a0 = 0x70L;
        }

        //LAB_80101380
        v0 = 0x8012_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x22f8L).get();
        a1 = 0x90L;
        FUN_801073f8(a0, a1, a2);
        a0 = 0xe2L;
        v0 = 0x800c_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x53a4L).get();
        a1 = 0x90L;
        FUN_80106d10(a0, a1, a2);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          v0 = 0x8012_0000L;

          //LAB_80101580
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          break;
        }
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s0).offset(-0x2414L).get();

        FUN_800242e8(a0);
        a0 = 0xd3L;
        a1 = 0xd9L;
        a2 = 0x44L;
        a3 = 0x50L;
        v0 = FUN_80103818(a0, a1, a2, a3);
        v1 = 0x1fL;
        MEMORY.ref(4, s0).offset(-0x2414L).setu(v0);
        MEMORY.ref(1, v0).offset(0x3cL).setu(v1);
        v1 = 0x800c_0000L;
        v0 = 0x6bL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x6b:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        a1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, a1).offset(-0x22f8L).get();

        if(a0 < 0xbL || (inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          //LAB_80101454
          s0 = 0x800c_0000L;
          v1 = 0x800c_0000L;

          //LAB_80101458
          v1 = v1 - 0x5438L;
          a1 = 0x8012_0000L;
          a0 = MEMORY.ref(4, s0).offset(-0x2414L).get();
          v0 = MEMORY.ref(4, v1).offset(0x94L).get();
          a2 = MEMORY.ref(4, a1).offset(-0x22f8L).get();
          MEMORY.ref(4, a1).offset(-0x22f8L).setu(0);
          v0 = v0 + a2;
          MEMORY.ref(4, v1).offset(0x94L).setu(v0);
          FUN_800242e8(a0);
          a0 = 0xd3L;
          a1 = a0;
          a2 = 0x44L;
          a3 = 0x50L;
          v0 = FUN_80103818(a0, a1, a2, a3);
          v1 = 0x1fL;
          MEMORY.ref(4, s0).offset(-0x2414L).setu(v0);
          MEMORY.ref(1, v0).offset(0x3cL).setu(v1);
          v1 = 0x800c_0000L;
          v0 = 0x6cL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        } else {
          v0 = 0x800c_0000L;
          v0 = v0 - 0x5438L;
          v1 = MEMORY.ref(4, v0).offset(0x94L).get();
          a0 = a0 - 0xaL;
          MEMORY.ref(4, a1).offset(-0x22f8L).setu(a0);
          v1 = v1 + 0xaL;
          MEMORY.ref(4, v0).offset(0x94L).setu(v1);
        }

        //LAB_801014a8
        v1 = 0x5f5_0000L;
        v0 = 0x800c_0000L;
        s0 = v0 - 0x5438L;
        v0 = MEMORY.ref(4, s0).offset(0x94L).get();
        v1 = v1 | 0xe0ffL;
        if((int)v1 < (int)v0) {
          v0 = 0x800c_0000L;
          MEMORY.ref(4, s0).offset(0x94L).setu(v1);
        } else {
          v0 = 0x800c_0000L;
        }

        //LAB_801014cc
        v0 = MEMORY.ref(4, v0).offset(-0x4f04L).get();

        v0 = v0 & 0x1L;
        if(v0 != 0) {
          a0 = 0x1L;
          playSound(a0);
        }

        //LAB_801014e8
        a0 = 0x70L;
        v0 = 0x8012_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x22f8L).get();
        a1 = 0x90L;
        FUN_801073f8(a0, a1, a2);
        a2 = MEMORY.ref(4, s0).offset(0x94L).get();
        a0 = 0xe2L;

        //LAB_80101574
        a1 = 0x90L;
        FUN_80106d10(a0, a1, a2);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x6c:
        v0 = 0x8012_0000L;
        s0 = v0 - 0x2370L;
        a0 = s0;
        v0 = FUN_8010ecec(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 == 0) {
          v0 = 0x800c_0000L;
        } else {
          v0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x2414L).get();

          FUN_800242e8(a0);
          a0 = 0x800c_0000L;
          v0 = MEMORY.ref(1, s0).offset(0xcL).get();
          v1 = 0x6dL;
          MEMORY.ref(4, a0).offset(-0x23d8L).setu(v1);
          v0 = v0 + 0x1L;
          MEMORY.ref(1, s0).offset(0xcL).setu(v0);
        }

        //LAB_80101554
        a0 = 0x70L;
        v0 = 0x8012_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x22f8L).get();
        a1 = 0x90L;
        FUN_801073f8(a0, a1, a2);
        a0 = 0xe2L;
        v0 = 0x800c_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x53a4L).get();

        //LAB_80101574
        a1 = 0x90L;
        FUN_80106d10(a0, a1, a2);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x6e:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          return;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2fb8L;

        //LAB_80101644
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;

        //LAB_80101650
        v0 = 0x6dL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x6f:
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2900L;
        a1 = 0;
        FUN_8010f130(a0, a1);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v1 = 0x800c_0000L;
        v0 = 0x70L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x70:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          return;
        }
        a0 = 0x8012_0000L;
        a0 = a0 - 0x28fcL;

        //LAB_80101644
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;

        //LAB_80101650
        v0 = 0x6dL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x71:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 != 0) {
          a0 = 0x8012_0000L;
          a0 = a0 - 0x28f4L;

          //LAB_80101644
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;

          //LAB_80101650
          v0 = 0x6dL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x73:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 != 0) {
          a0 = 0x8012_0000L;
          a0 = a0 - 0x31f4L;

          //LAB_801016b0
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x77L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x74:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 != 0) {
          a0 = 0x8012_0000L;
          a0 = a0 - 0x31f0L;

          //LAB_801016b0
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x77L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x75:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 != 0) {
          a0 = 0x8012_0000L;
          a0 = a0 - 0x31fcL;
          a1 = 0;
          FUN_8010f130(a0, a1);
          v1 = 0x800c_0000L;
          v0 = 0x78L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x76:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 != 0) {
          v1 = 0x800c_0000L;

          //LAB_8010172c
          v0 = 0x49L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x4e:
      case 0x5d:
      case 0x6d:
      case 0x77:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 != 0) {
          //LAB_8010175c
          v1 = 0x800c_0000L;
          v0 = 0x4fL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x78:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 != 0) {
          a1 = 0;
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x2300L).get();
          a2 = a1;
          removeFromLinkedList(a0);
          a1 = 0;
          v0 = 0x8012_0000L;
          a0 = MEMORY.ref(4, v0).offset(-0x2840L).get();
          a2 = a1;
          removeFromLinkedList(a0);
          a0 = 0x2L;
          a1 = 0xdL;
          FUN_800fca0c(a0, a1);
          v0 = 0x8005_0000L;
          MEMORY.ref(4, v0).offset(-0x22d0L).setu(0);
        }

        break;

      case 0x79:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x31e8L;
        a1 = 0;
        FUN_8010f130(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x7aL;
        MEMORY.ref(4, v1).offset(-0x23d0L).setu(v0);
        v1 = 0x800c_0000L;
        v0 = 0x41L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x7a:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x8012_0000L;
        a0 = a0 - 0x2370L;
        v0 = FUN_8010ecec(a0);
        if(v0 == 0) {
          break;
        }
        v0 = 0x8012_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x2848L).get();

        if(v1 == 0 || v1 == 0x3L) {
          //LAB_80101844
          v1 = 0x800c_0000L;
          v0 = 0x59L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
          break;
        }

        //LAB_80101854
        v1 = 0x800c_0000L;

        //LAB_80101858
        v0 = 0x74L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x7b:
        a0 = 0x1L;
        a1 = 0xaL;
        scriptStartEffect(a0, a1);
        v1 = 0x800c_0000L;
        v0 = 0x7cL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);

      case 0x7c:
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23d4L).get();

        switch((int)v0) {
          case 0x1:
            a0 = 0;
            FUN_80102484(a0);
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28c8L).get();
            a1 = 0x4L;

            //LAB_801018f0
            a2 = 0xfeL;
            FUN_80101d10(a0, a1, a2);
            break;

          case 0x2:
            a0 = 0x1L;
            FUN_80102484(a0);
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28c4L).get();
            a1 = 0x4L;
            FUN_80102064(a0, a1);
            a1 = 0x6L;
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28c8L).get();

            //LAB_801018f0
            a2 = 0xfeL;
            FUN_80101d10(a0, a1, a2);
            break;

          case 0x5:
            a0 = 0xfeL;
            FUN_801024c4(a0);
            break;

          case 0x6:
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
            v0 = 0x8012_0000L;
            a1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            v0 = 0x8012_0000L;
            a2 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
            a3 = 0xfeL;
            FUN_80102660(a0, a1, a2, a3);
            break;

          case 0x7:
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
            a1 = 0xfeL;
            FUN_801027bc(a0, a1);
            break;

          case 0x8:
            t0 = 0x8012_0000L;
            t1 = t0 - 0x28bcL;
            a3 = 0x8012_0000L;
            v1 = 0x8012_0000L;
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
            v1 = v1 - 0x2348L;
            v0 = a0 << 2;
            v0 = v0 + v1;
            a2 = MEMORY.ref(4, v0).offset(0x0L).get();
            a1 = a3 - 0x28b8L;
            if(a0 != 0) {
              v0 = 0x8012_0000L;
              v0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
              v1 = MEMORY.ref(4, a3).offset(-0x28b8L).get();
              v0 = v0 + v1;
            } else {
              //LAB_80101994
              v0 = 0x8012_0000L;
              v0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
              v1 = MEMORY.ref(4, t0).offset(-0x28bcL).get();

              v0 = v0 + v1;
            }

            //LAB_801019a8
            v0 = v0 << 2;
            v0 = a2 + v0;
            a0 = MEMORY.ref(4, t1).offset(0x0L).get();
            a1 = MEMORY.ref(4, a1).offset(0x0L).get();
            a2 = MEMORY.ref(1, v0).offset(0x0L).get();
            a3 = 0;
            FUN_80102840(a0, a1, a2, a3);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(-0x4e98L).get();

            if((int)v0 < 0xffL) {
              return;
            }
            a1 = 0;
            a2 = a1;
            s0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, s0).offset(-0x2348L).get();
            s0 = s0 - 0x2348L;
            removeFromLinkedList(a0);
            a1 = 0;
            a0 = MEMORY.ref(4, s0).offset(0x4L).get();
            a2 = a1;
            removeFromLinkedList(a0);
            break;

          case 0x9:
            a2 = 0x8012_0000L;
            a2 = a2 - 0x1f68L;
            v0 = 0x8012_0000L;
            v1 = 0x8012_0000L;
            a3 = 0x800c_0000L;
            a1 = MEMORY.ref(4, v1).offset(-0x28c0L).get();
            v1 = 0x800c_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
            v1 = v1 - 0x2448L;
            v0 = a0 << 2;
            v0 = v0 + v1;
            v1 = MEMORY.ref(4, v0).offset(0x0L).get();
            a3 = a3 - 0x5438L;
            v0 = v1 << 1;
            v0 = v0 + v1;
            v0 = v0 << 2;
            v0 = v0 - v1;
            v0 = v0 << 2;
            v0 = v0 + a3;
            a3 = MEMORY.ref(1, v0).offset(0x345L).get();
            v0 = 0xfeL;
            a4 = v0;
            FUN_80102ad8(a0, a1, a2, a3, a4);
            break;

          case 0xa:
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
            v0 = 0x8012_0000L;
            a1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            v0 = 0x8012_0000L;
            a2 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
            a3 = 0;
            FUN_80102dfc(a0, a1, a2, a3);
            break;

          case 0xb:
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28ccL).get();
            v0 = 0x8012_0000L;
            a1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            v0 = 0x8012_0000L;
            a2 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
            a3 = 0xfeL;
            FUN_80102f74(a0, a1, a2, a3);
            break;

          case 0xc:
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
            a1 = 0x8012_0000L;
            a1 = a1 - 0x22f0L;

            //LAB_80101af4
            a2 = 0xfeL;
            FUN_801030c0(a0, a1, a2);
            break;

          case 0xd:
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
            a1 = 0xfeL;
            FUN_80103168(a1);
            break;

          case 0xe:
            v0 = 0x8012_0000L;
            a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
            a1 = 0;

            //LAB_80101af4
            a2 = 0xfeL;
            FUN_801030c0(a0, a1, a2);
            break;

          case 0x3:
          case 0x4:
          default:
        }

        //LAB_80101afc
        v0 = 0x800c_0000L;

        //LAB_80101b00
        v0 = MEMORY.ref(4, v0).offset(-0x4e98L).get();

        if((int)v0 < 0xffL) {
          break;
        }

        //LAB_80101b14
        v0 = 0x800c_0000L;

        //LAB_80101b18
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x800c_0000L;
        MEMORY.ref(4, v0).offset(-0x23d8L).setu(v1);
        break;

      case 0x7d:
        a0 = 0xffL;
        FUN_8002437c(a0);
        a1 = 0;
        v0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
        a2 = a1;
        removeFromLinkedList(a0);
        s0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, s0).offset(-0x23c8L).get();
        v0 = 0x13L;
        if(v1 == v0) {
          //LAB_80101ba4
          v0 = 0x14L;
          MEMORY.ref(4, s0).offset(-0x23c8L).setu(v0);
        } else {
          if((int)v1 >= 0x14L) {
            v0 = 0xeL;
            //LAB_80101b70
            v0 = 0x18L;
            a0 = 0x2L;
            if(v1 == v0) {
              a1 = 0xaL;
              scriptStartEffect(a0, a1);
              v0 = 0x19L;
              MEMORY.ref(4, s0).offset(-0x23c8L).setu(v0);
            } else {
              //LAB_80101bb0
              a1 = 0xaL;
              scriptStartEffect(a0, a1);
              v1 = 0x800c_0000L;
              v0 = 0x5L;
              MEMORY.ref(4, v1).offset(-0x23c8L).setu(v0);
            }
          }
          v0 = 0xeL;
          if(v1 == v0) {
            a0 = 0x2L;
            //LAB_80101b90
            a1 = 0xaL;
            scriptStartEffect(a0, a1);
            v0 = 0xfL;
            MEMORY.ref(4, s0).offset(-0x23c8L).setu(v0);
          } else {
            a0 = 0x2L;
            //LAB_80101bb0
            a1 = 0xaL;
            scriptStartEffect(a0, a1);
            v1 = 0x800c_0000L;
            v0 = 0x5L;
            MEMORY.ref(4, v1).offset(-0x23c8L).setu(v0);
          }
        }

        //LAB_80101bc4
        v0 = 0x8005_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x22e0L).get();
        v0 = 0x5L;
        if(v1 != v0) {
          v1 = 0x800c_0000L;
        } else {
          v1 = 0x800c_0000L;
          v0 = 0x8005_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x22f8L).get();

          if(v0 == 0) {
            FUN_800e3fac();
            v1 = 0x800c_0000L;
          }
        }

        //LAB_80101bf8
        v0 = 0xdL;

        //LAB_80101bfc
        MEMORY.ref(4, v1).offset(-0x2100L).setu(v0);

      case 0x72:
      case 0x3f:
        //LAB_80101c00
    }
  }

  @Method(0x80101d10L)
  public static void FUN_80101d10(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long a4;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;

    s3 = a0;
    s5 = 0x6L;
    s2 = a1;
    v0 = 0x8012_0000L;
    v0 = MEMORY.ref(1, v0).offset(-0x2378L).get();
    a2 = a2 ^ 0xffL;
    if(v0 != 0) {
      s5 = s2 & 0xffL;
    }

    //LAB_80101d54
    s0 = 0x800c_0000L;
    s4 = a2 < 0x1L ? 1 : 0;
    if(s4 != 0) {
      s0 = s0 - 0x529cL;
      a0 = s0;
      a1 = 0x28L;
      a2 = 0xc5L;
      FUN_801098c0(a0, a1, a2);
      a0 = 0x43L;
      a1 = 0xb8L;
      s0 = s0 - 0x19cL;
      a2 = MEMORY.ref(4, s0).offset(0x94L).get();
      a3 = 0;
      FUN_80105f2c(a0, a1, a2, a3);
      a0 = 0x92L;
      a1 = 0xb8L;
      a2 = 0xaL;
      FUN_80107cb4(a0, a1, a2);
      a0 = 0xa4L;
      a1 = 0xb8L;
      a2 = 0xaL;
      FUN_80107cb4(a0, a1, a2);
      a0 = 0xa6L;
      a2 = MEMORY.ref(4, s0).offset(0x9cL).get();
      a1 = 0xccL;
      FUN_80104c30(a0, a1, a2);
    }

    //LAB_80101db8
    s0 = 0x800c_0000L;
    s0 = s0 - 0x5438L;
    a0 = MEMORY.ref(4, s0).offset(0xa0L).get();
    a1 = 0;
    v0 = FUN_80023674(a0, a1);
    a0 = 0x80L;
    a1 = 0xb8L;
    a2 = v0;
    a3 = 0x3L;
    FUN_80107764(a0, a1, a2, a3);
    a0 = MEMORY.ref(4, s0).offset(0xa0L).get();
    a1 = 0x1L;
    v0 = FUN_80023674(a0, a1);
    a0 = 0x98L;
    a1 = 0xb8L;
    a2 = v0;
    a3 = 0x3L;
    FUN_801079fc(a0, a1, a2, a3);
    a0 = MEMORY.ref(4, s0).offset(0xa0L).get();
    a1 = 0x2L;
    v0 = FUN_80023674(a0, a1);
    a0 = 0xaaL;
    a1 = 0xb8L;
    a2 = v0;
    a3 = 0x3L;
    FUN_801079fc(a0, a1, a2, a3);
    a0 = 0xc2L;
    a1 = 0x10L;
    s1 = s4 & 0xffL;
    a2 = MEMORY.ref(4, s0).offset(0x88L).get();
    a3 = s1;
    a4 = 0;
    FUN_80107f9c(a0, a1, a2, a3, a4);
    a0 = 0xc2L;
    a1 = 0x58L;
    a2 = MEMORY.ref(4, s0).offset(0x8cL).get();
    a3 = s1;
    a4 = 0;
    FUN_80107f9c(a0, a1, a2, a3, a4);
    a0 = 0xc2L;
    a1 = 0xa0L;
    a2 = MEMORY.ref(4, s0).offset(0x90L).get();
    a3 = s1;
    a4 = 0;
    FUN_80107f9c(a0, a1, a2, a3, a4);
    a1 = 0x5eL;
    a2 = 0x18L;
    v1 = 0x8011_0000L;
    v0 = MEMORY.ref(4, s0).offset(0x98L).get();
    v1 = v1 + 0x4248L;
    v0 = v0 << 2;
    v0 = v0 + v1;
    a0 = MEMORY.ref(4, v0).offset(0x0L).get();
    a3 = 0x4L;
    FUN_80103e90(a0, a1, a2, a3);
    v0 = 0x8005_0000L;
    v1 = MEMORY.ref(4, v0).offset(-0x22e0L).get();
    v0 = 0x5L;
    a1 = 0x5aL;
    if(v1 == v0) {
      a2 = 0x26L;
      v1 = 0x8012_0000L;
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(-0x27f8L).get();
      v1 = v1 - 0x3ef8L;
    } else {
      //LAB_80101ec0
      a2 = 0x26L;
      v1 = 0x8012_0000L;
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(-0xf50L).get();
      v1 = v1 - 0x3e14L;
    }

    //LAB_80101ed4
    v0 = v0 << 2;
    v0 = v0 + v1;
    a0 = MEMORY.ref(4, v0).offset(0x0L).get();
    FUN_80103e90(a0, a1, a2, 0x4L);
    v0 = FUN_800fc78c(0);
    a3 = 0x5L;
    v0 = v0 + 0x2L;
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x314cL;
    if(s3 != 0) {
      a3 = s2 & 0xffL;
    }

    //LAB_80101f0c
    FUN_80103e90(a0, 0x3eL, a2, a3);
    v0 = FUN_800fc78c(0x1L);
    v0 = v0 + 0x2L;
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x313cL;
    if(s3 == 0x1L) {
      a3 = 0x5L;
    } else {
      a3 = s2 & 0xffL;
    }

    //LAB_80101f3c
    FUN_80103e90(a0, 0x3eL, a2, a3);
    v0 = FUN_800fc78c(0x2L);
    v0 = v0 + 0x2L;
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x3130L;
    if(s3 == 0x2L) {
      a3 = 0x5L;
    } else {
      a3 = s2 & 0xffL;
    }

    //LAB_80101f6c
    FUN_80103e90(a0, 0x3eL, a2, a3);
    v0 = FUN_800fc78c(0x3L);
    v0 = v0 + 0x2L;
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x3124L;
    if(s3 == 0x3L) {
      a3 = 0x5L;
    } else {
      a3 = s2 & 0xffL;
    }

    //LAB_80101f9c
    FUN_80103e90(a0, 0x3eL, a2, a3);
    v0 = FUN_800fc78c(0x4L);
    v0 = v0 + 0x2L;
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x3110L;
    if(s3 == 0x4L) {
      a3 = 0x5L;
    } else {
      a3 = s2 & 0xffL;
    }

    //LAB_80101fcc
    FUN_80103e90(a0, 0x3eL, a2, a3);
    v0 = FUN_800fc78c(0x5L);
    a3 = 0x5L;
    v0 = v0 + 0x2L;
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x3100L;
    if(s3 != a3) {
      a3 = s2 & 0xffL;
    }

    //LAB_80101ff8
    FUN_80103e90(a0, 0x3eL, a2, a3);
    v0 = FUN_800fc78c(0x6L);
    v0 = v0 + 0x2L;
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x30f0L;
    if(s3 == 0x6L) {
      a3 = 0x5L;
    } else {
      a3 = s5 & 0xffL;
    }

    //LAB_80102028
    FUN_80103e90(a0, 0x3eL, a2, a3);
    if(s4 == 0) {
      FUN_80023c28();
    }

    //LAB_80102040
  }

  @Method(0x80102064L)
  public static void FUN_80102064(long a0, long a1) {
    long v0;
    long a2;
    long a3;
    long s1 = a0;
    long s0 = a1;
    a0 = 0x96L;
    a1 = 0x14L;
    a2 = 0x3cL;
    FUN_801038d4(a0, a1, a2);
    v0 = FUN_800fc7a4(0);
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x30e4L;
    if(s1 == 0) {
      a3 = 0x5L;
    } else {
      a3 = s0 & 0xffL;
    }

    //LAB_801020ac
    FUN_80103e90(a0, 0x8eL, a2, a3);
    v0 = FUN_800fc7a4(0x1L);
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x30d4L;
    if(s1 == 0x1L) {
      a3 = 0x5L;
    } else {
      a3 = s0 & 0xffL;
    }

    //LAB_801020d8
    FUN_80103e90(a0, 0x8eL, a2, a3);
    v0 = FUN_800fc7a4(0x2L);
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x30c4L;
    if(s1 == 0x2L) {
      a3 = 0x5L;
    } else {
      a3 = s0 & 0xffL;
    }

    //LAB_80102104
    FUN_80103e90(a0, 0x8eL, a2, a3);
    v0 = FUN_800fc7a4(0x3L);
    a2 = v0 & 0xffffL;
    v0 = 0x8012_0000L;
    a0 = v0 - 0x30b8L;
    if(s1 == 0x3L) {
      a3 = 0x5L;
    } else {
      a3 = s0 & 0xffL;
    }

    //LAB_80102130
    FUN_80103e90(a0, 0x8eL, a2, a3);
  }

  @Method(0x8010214cL)
  public static void FUN_8010214c(final long a0, final long a1, final long a2, final long a3, final long a4) {
    assert false;
  }

  @Method(0x80102484L)
  public static void FUN_80102484(final long a0) {
    //LAB_801024ac
    FUN_801038d4(a0 != 0 ? 0x17L : 0x18L, 0x70L, FUN_800fc78c(0x1L) + 0x3L);
  }

  @Method(0x801024c4L)
  public static void FUN_801024c4(final long a0) {
    assert false;
  }

  @Method(0x80102660L)
  public static void FUN_80102660(final long a0, final long a1, final long a2, final long a3) {
    assert false;
  }

  @Method(0x801027bcL)
  public static void FUN_801027bc(final long a0, final long a1) {
    assert false;
  }

  @Method(0x80102840L)
  public static void FUN_80102840(final long a0, final long a1, final long a2, final long a3) {
    assert false;
  }

  @Method(0x80102ad8L)
  public static void FUN_80102ad8(final long a0, final long a1, final long a2, final long a3, final long a4) {
    assert false;
  }

  @Method(0x80102dfcL)
  public static void FUN_80102dfc(final long a0, final long a1, final long a2, final long a3) {
    assert false;
  }

  @Method(0x80102f74L)
  public static void FUN_80102f74(final long a0, final long a1, final long a2, final long a3) {
    assert false;
  }

  @Method(0x801030c0L)
  public static void FUN_801030c0(final long a0, final long a1, final long a2) {
    if(a2 == 0xffL) {
      FUN_8010376c(_80114258.getAddress(), 0, 0);
    }

    //LAB_80103100
    if(a1 != 0) {
      //LAB_80103108
      for(int s0 = 0; s0 < 3; s0++) {
        final long v1 = a0 + s0;
        FUN_80108a6c(v1 & 0xffL, a1 + v1 * 0x3cL, FUN_800fc84c(s0), a2 == 0xffL ? 1 : 0);
      }
    }

    //LAB_80103144
    // Build GP0 packets
    FUN_80023c28();
  }

  @Method(0x80103168L)
  public static void FUN_80103168(final long a0) {
    assert false;
  }

  @Method(0x801033ccL)
  public static void FUN_801033cc(final long a0) {
    MEMORY.ref(1, a0).offset(0x28L).setu(0x1L);
    MEMORY.ref(4, a0).offset(0x38L).setu(0);
    MEMORY.ref(4, a0).offset(0x34L).setu(0);
    MEMORY.ref(1, a0).offset(0x3cL).setu(0x1fL);
  }

  @Method(0x801033e8L)
  public static void FUN_801033e8(final long a0) {
    final long s0 = MEMORY.ref(4, a0).offset(0x40L).get();
    final long s1 = MEMORY.ref(4, a0).offset(0x44L).get();
    FUN_800242e8(a0);

    final long a0_0 = FUN_80103818(0x6cL, 0x6fL, s0, s1);
    MEMORY.ref(4, a0_0).oru(0x10L);
    FUN_801033cc(a0_0);
  }

  @Method(0x80103444L)
  public static void FUN_80103444(final long a0, final long a1, final long a2, final long a3, final long a4) {
    if(a0 != 0) {
      if(MEMORY.ref(4, a0).offset(0x18L).get() == 0) {
        if((FUN_800133ac() & 0x3000L) != 0) {
          MEMORY.ref(4, a0).offset(0x18L).setu(a1);
          MEMORY.ref(4, a0).offset(0x1cL).setu(a2);
        } else {
          //LAB_801034a0
          MEMORY.ref(4, a0).offset(0x18L).setu(a3);
          MEMORY.ref(4, a0).offset(0x1cL).setu(a4);
        }
      }
    }

    //LAB_801034b0
  }

  @Method(0x801034ccL)
  public static void FUN_801034cc(final long a0, final long a1) {
    assert false;
  }

  @Method(0x8010361cL)
  public static void FUN_8010361c(final long a0, final long a1) {
    FUN_80103444(_800bdb94.get(), 0xc2L, 0xc9L, 0xcaL, 0xd1L);
    FUN_80103444(_800bdb98.get(), 0xb2L, 0xb9L, 0xbaL, 0xc1L);

    if(a0 != 0) {
      if(_800bdb94.get() == 0) {
        final long a0_0 = FUN_80103818(0x6fL, 0x6cL, 0xb6L, 0x10L);
        MEMORY.ref(4, a0_0).offset(0x18L).setu(0xc2L);
        _800bdb94.setu(a0_0);
        MEMORY.ref(4, a0_0).offset(0x1cL).setu(0xc9L);
        FUN_801033cc(a0_0);
      }
      //LAB_801036c8
    } else if(_800bdb94.get() != 0) {
      FUN_801033e8(_800bdb94.get());
      _800bdb94.setu(0);
    }

    //LAB_801036e8
    if((int)a0 < a1 - 0x2L) {
      if(_800bdb98.get() == 0) {
        final long a0_0 = FUN_80103818(0x6fL, 0x6cL, 0xb6L, 0xd0L);
        MEMORY.ref(4, a0_0).offset(0x18L).setu(0xb2L);
        _800bdb98.setu(a0_0);
        MEMORY.ref(4, a0_0).offset(0x1cL).setu(0xb9L);
        FUN_801033cc(a0_0);
      }
      //LAB_80103738
    } else if(_800bdb98.get() != 0) {
      FUN_801033e8(_800bdb98.get());
      _800bdb98.setu(0);
    }

    //LAB_80103754
  }

  @Method(0x8010376cL)
  public static void FUN_8010376c(final long a0, final long a1, final long a2) {
    //LAB_801037ac
    for(long s1 = a0; MEMORY.ref(1, s1).get() != 0xffL; s1 += 0x6L) {
      final long s0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), 0);

      FUN_80104b1c(s0, s1);

      MEMORY.ref(4, s0).offset(0x40L).addu(a1);
      MEMORY.ref(4, s0).offset(0x44L).addu(a2);
    }

    //LAB_801037f4
  }

  @Method(0x80103818L)
  public static long FUN_80103818(final long a0, final long a1, final long a2, final long a3) {
    final long v1 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), 0);

    if((int)a1 >= (int)a0) {
      MEMORY.ref(4, v1).offset(0x4L).setu(a0);
      MEMORY.ref(4, v1).offset(0x10L).setu(a0);
      MEMORY.ref(4, v1).offset(0x14L).setu(a1);
    } else {
      //LAB_80103870
      MEMORY.ref(4, v1).offset(0x4L).setu(a0);
      MEMORY.ref(4, v1).offset(0x10L).setu(a1);
      MEMORY.ref(4, v1).offset(0x14L).setu(a0);
      MEMORY.ref(4, v1).oru(0x20L);
    }

    //LAB_80103888
    if(a0 == a1) {
      MEMORY.ref(4, v1).oru(0x4L);
    }

    //LAB_801038a4
    MEMORY.ref(4, v1).offset(0x30L).setu(0);
    MEMORY.ref(4, v1).offset(0x2cL).setu(0x19L);
    MEMORY.ref(4, v1).offset(0x40L).setu(a2);
    MEMORY.ref(4, v1).offset(0x44L).setu(a3);

    return v1;
  }

  @Method(0x80103a5cL)
  public static long FUN_80103a5c(final long a0, final long a1) {
    assert false;
    return 0;
  }

  @Method(0x80103b10L)
  public static void FUN_80103b10() {
    _8011d7c4.setu(0);

    long a2 = 0;

    //LAB_80103b48
    for(long a0 = 0; a0 < 0x9L; a0++) {
      _800bdbf8.offset(a0 * 0x4L).setu(-0x1L);
      _800bdbb8.offset(a0 * 0x4L).setu(-0x1L);

      if((_800babc8.offset(0x330L).offset(a0 * 0x2cL).get() & 0x1L) != 0) {
        _800bdbb8.offset(_8011d7c4.get() * 0x4L).setu(a0);
        _8011d7c4.addu(0x1L);

        if(_800babc8.offset(0x88L).get() != a0) {
          if(_800babc8.offset(0x8cL).get() != a0) {
            if(_800babc8.offset(0x90L).get() != a0) {
              _800bdbf8.offset(a2).setu(a0);
              a2 = a2 + 0x4L;
            }
          }
        }
      }

      //LAB_80103bb4
    }
  }

  @Method(0x80103bd4L)
  public static void FUN_80103bd4(final long unused) {
    //LAB_80103be8
    for(long a1 = 0; a1 < 0x3L; a1++) {
      _8011dcc0.offset(0x8L).offset(a1 * 0x4L).setu(_800babc8.offset(0x88L).offset(a1 * 0x4L));
    }

    _8011dcc0.setu(0x5a02_0006L);
    _8011dcc0.offset(1, 0x14L).setu(_800babc8.offset(1, 0x33eL));
    _8011dcc0.offset(1, 0x15L).setu(_800be5f8.offset(1, 0xfL));
    _8011dcc0.offset(2, 0x16L).setu(_800babc8.offset(2, 0x334L));
    _8011dcc0.offset(2, 0x18L).setu(_800be5f8.offset(2, 0x66L));
    _8011dcc0.offset(4, 0x1cL).setu(_800babc8.offset(4, 0x94L));
    _8011dcc0.offset(4, 0x20L).setu(_800babc8.offset(4, 0xa0L));
    _8011dcc0.offset(4, 0x24L).setu(_800babc8.offset(4, 0x19cL).get() & 0x1ffL);
    _8011dcc0.offset(4, 0x28L).setu(_800babc8.offset(4, 0x9cL));

    if(mainCallbackIndex_8004dd20.get() == 0x8L) {
      //LAB_80103c8c
      _8011dcc0.offset(1, 0x2dL).setu(0x1L);
      _8011dcc0.offset(1, 0x2cL).setu(_800bf0b0.offset(1, 0x0L)); //1b
      //LAB_80103c98
    } else if(whichMenu_800bdc38.get() == 0x13L) {
      //LAB_80103c8c
      _8011dcc0.offset(1, 0x2dL).setu(0x3L);
      _8011dcc0.offset(1, 0x2cL).setu(_800babc8.offset(1, 0x98L));
    } else {
      //LAB_80103cb4
      _8011dcc0.offset(1, 0x2dL).setu(0);
      _8011dcc0.offset(1, 0x2cL).setu(_800bd808);
    }
  }

  @Method(0x80103cc4L)
  public static void FUN_80103cc4(long a0, long a1, long a2, long a3) {
    long s0;
    long s2;
    long s3;
    long s1 = a2;

    if(a3 == 0x2L) {
      //LAB_80103d18
      s2 = 0x1L;
    } else if(a3 == 0x6L) {
      //LAB_80103d20
      s2 = 0x7L;
    } else {
      s2 = 0x6L;
    }

    //LAB_80103d24
    //LAB_80103d28
    s0 = (short)a1;
    s3 = (short)s1;
    FUN_80029300(a0, s0, s3, (short)a3, 0);
    s1 = (short)(s1 + 0x1L);
    FUN_80029300(a0, s0, s1, s2, 0);
    s0 = (short)(a1 + 0x1L);
    FUN_80029300(a0, s0, s3, s2, 0);
    FUN_80029300(a0, s0, s1, s2, 0);
  }

  @Method(0x80103dd4L)
  public static long FUN_80103dd4(final long a0) {
    //LAB_80103ddc
    long v1;
    for(v1 = 0; v1 < 0xffL; v1++) {
      if(MEMORY.ref(2, a0).offset(v1 * 0x2L).get() == 0xa0ffL) {
        break;
      }
    }

    //LAB_80103dfc
    return v1;
  }

  @Method(0x80103e90L)
  public static void FUN_80103e90(final long a0, final long a1, final long a2, final long a3) {
    FUN_80103cc4(a0, (short)(a1 - (FUN_80103dd4(a0) & 0xffffL) * 0x4L), (short)a2, a3 & 0xffL);
  }

  @Method(0x80103f00L)
  public static long FUN_80103f00(final long a0, final long a1, final long a2, long a3, final long a4) {
    if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0) {
      if(MEMORY.ref(4, a0).get() == 0) {
        //LAB_80103f44
        if(MEMORY.ref(4, a1).get() < (int)a4) {
          return 0x1L;
        }

        MEMORY.ref(4, a1).subu(a4);
      } else {
        MEMORY.ref(4, a0).subu(0x1L);
      }
      //LAB_80103f64
    } else if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0) {
      if(MEMORY.ref(4, a0).get() < a2 - 0x1L) {
        MEMORY.ref(4, a0).addu(0x1L);
      } else {
        //LAB_80103f8c
        if(a3 <= MEMORY.ref(4, a1).get() + a2 * a4) {
          return 0x1L;
        }

        MEMORY.ref(4, a1).addu(a4);
      }
      //LAB_80103fb0
    } else if((inventoryJoypadInput_800bdc44.get() & 0x4L) != 0) {
      if(MEMORY.ref(4, a0).get() != 0) {
        playSound(0x1L);
        MEMORY.ref(4, a0).setu(0);
      }

      return 0x1L;
      //LAB_80103fdc
    } else if((inventoryJoypadInput_800bdc44.get() & 0x1L) != 0) {
      if(MEMORY.ref(4, a0).get() >= a2 - 0x1L) {
        return 0x1L;
      }

      playSound(0x1L);
      MEMORY.ref(4, a0).setu(a2 - 0x1L);
      return 0x1L;
      //LAB_80104008
    } else if((inventoryJoypadInput_800bdc44.get() & 0x8L) == 0 || MEMORY.ref(4, a1).get() < a4) {
      //LAB_8010404c
      if((inventoryJoypadInput_800bdc44.get() & 0x2L) == 0) {
        return 0;
      }

      if(a2 >= a3) {
        return 0;
      }

      final long v1 = MEMORY.ref(4, a1).get() + a2 * a4;
      a3 -= a2 * a4;
      if(v1 < a3) {
        MEMORY.ref(4, a1).setu(v1);
        //LAB_8010408c
      } else if(MEMORY.ref(4, a1).get() < a3) {
        MEMORY.ref(4, a1).setu(a3);
      } else {
        return 0;
      }
    } else if(MEMORY.ref(4, a1).get() >= a2 * a4) {
      MEMORY.ref(4, a1).subu(a2 * a4);
    } else {
      //LAB_80104044
      MEMORY.ref(4, a1).setu(0);
    }

    //LAB_80104098
    playSound(0x1L);

    //LAB_801040a0
    //LAB_801040ac
    return 0x1L;
  }

  @Method(0x801040c0L)
  public static long FUN_801040c0(final long a0, final long a1) {
    long v0;
    long v1;

    v0 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v0).offset(-0x23bcL).get();
    if((v1 & 0x1000L) != 0) {
      playSound(0x1L);
      v0 = MEMORY.ref(4, a0).get();

      if(v0 != 0) {
        v0 = v0 - 0x1L;
      } else {
        //LAB_80104108
        v0 = a1 - 0x1L;
      }

      //LAB_8010410c
      MEMORY.ref(4, a0).setu(v0);
    } else {
      //LAB_80104118
      if((v1 & 0x4000L) == 0) {
        return 0;
      }

      playSound(0x1L);
      v1 = MEMORY.ref(4, a0).get();
      if((int)v1 < a1 - 0x1L) {
        MEMORY.ref(4, a0).setu(v1 + 0x1L);
      } else {
        MEMORY.ref(4, a0).setu(0);
      }
    }

    //LAB_80104110
    //LAB_80104148
    return 0x1L;
  }

  @Method(0x8010415cL)
  public static long FUN_8010415c(final long a0, final long a1) {
    assert false;
    return 0;
  }

  @Method(0x801041d8L)
  public static long FUN_801041d8(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x80104324L)
  public static void FUN_80104324(final long a0) {
    assert false;
  }

  @Method(0x801038d4L)
  public static void FUN_801038d4(final long a0, final long a1, final long a2) {
    final long v0 = FUN_80103818(a0, a0, a1, a2);
    MEMORY.ref(4, v0).oru(0x8L);
  }

  @Method(0x80104448L)
  public static long FUN_80104448() {
    assert false;
    return 0;
  }

  @Method(0x801045fcL)
  public static long FUN_801045fc(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x80104738L)
  public static long FUN_80104738(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x801049b4L)
  public static long FUN_801049b4(final long a0, final long a1) {
    assert false;
    return 0;
  }

  @Method(0x80104b1cL)
  public static void FUN_80104b1c(final long a0, final long a1) {
    if(MEMORY.ref(1, a1).get() != 0xffL) {
      MEMORY.ref(4, a0).offset(0x4L).setu(MEMORY.ref(1, a1));
      MEMORY.ref(4, a0).oru(0x4L);
    }

    //LAB_80104b40
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
    MEMORY.ref(4, a0).offset(0x40L).setu(MEMORY.ref(2, a1).offset(0x2L));
    MEMORY.ref(4, a0).offset(0x44L).setu(MEMORY.ref(2, a1).offset(0x4L));
  }

  @Method(0x80104b60L)
  public static void FUN_80104b60(final long a0) {
    MEMORY.ref(1, a0).offset(0x28L).setu(0x1L);
    MEMORY.ref(4, a0).offset(0x34L).setu(0);
    MEMORY.ref(4, a0).offset(0x38L).setu(0);
    MEMORY.ref(1, a0).offset(0x3cL).setu(0x23L);
  }

  @Method(0x80104c30L)
  public static void FUN_80104c30(long a0, long a1, long a2) {
    long v0;
    long v1;
    long s0;

    long s1 = a2;
    long s3 = a0;

    long sp10 = 0;

    if(s1 > 0x63L) {
      s1 = 0x63L;
    }

    //LAB_80104c68
    a0 = s1 / 10;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0) {
      a0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), 0);
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80104cd8
        v0 = v0 | 0x4L;
      }

      //LAB_80104cdc
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(a1 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_80104d14
    sp10 = 0x1L;
    s3 = s3 + 0x6L;
    v1 = s1 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = s1 - v0;
    a0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), 0);
    v0 = MEMORY.ref(4, a0).offset(0x0L).get();
    MEMORY.ref(4, a0).offset(0x4L).setu(s0);
    if((sp10 & 0x2L) != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_80104d78
      v0 = v0 | 0x4L;
    }

    //LAB_80104d7c
    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
    MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
    MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
    MEMORY.ref(4, a0).offset(0x44L).setu(a1 & 0xffffL);
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
    sp10 |= 0x1L;
  }

  @Method(0x80104dd4L)
  public static void FUN_80104dd4(long a0, long a1, long a2) {
    long v0;
    long v1;
    long s0;
    long s5;
    long s1 = a2;
    long s3 = a0;
    long s4 = a1;

    long sp10 = 0;

    if(s1 > 0x3e7L) {
      s1 = 0x3e7L;
    }

    //LAB_80104e10
    a0 = s1 / 100;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0) {
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80104e84
        v0 = v0 | 0x4L;
      }

      //LAB_80104e88
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      v0 = 0x19L;
      MEMORY.ref(4, a0).offset(0x2cL).setu(v0);
      v0 = s3 & 0xffffL;
      MEMORY.ref(4, a0).offset(0x40L).setu(v0);
      v0 = s4 & 0xffffL;
      MEMORY.ref(4, a0).offset(0x44L).setu(v0);
      v0 = 0x21L;
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(v0);
      sp10 |= 0x1L;
    }

    //LAB_80104ec0
    a0 = s1 / 10;
    s5 = s3 + 0x6L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_80104f18
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80104f48
        v0 = v0 | 0x4L;
      }

      //LAB_80104f4c
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      v0 = 0x19L;
      MEMORY.ref(4, a0).offset(0x2cL).setu(v0);
      v0 = s5 & 0xffffL;
      MEMORY.ref(4, a0).offset(0x40L).setu(v0);
      v0 = s4 & 0xffffL;
      MEMORY.ref(4, a0).offset(0x44L).setu(v0);
      v0 = 0x21L;
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(v0);
      sp10 |= 0x1L;
    }

    //LAB_80104f84
    sp10 = 0x1L;
    s3 = s3 + 0xcL;
    v1 = s1 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = s1 - v0;
    v0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
    a1 = 0;
    v0 = FUN_80023b54(a0, a1);
    a0 = v0;
    v1 = sp10;
    v0 = MEMORY.ref(4, a0).offset(0x0L).get();
    v1 = v1 & 0x2L;
    MEMORY.ref(4, a0).offset(0x4L).setu(s0);
    if(v1 != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_80104fe8
      v0 = v0 | 0x4L;
    }

    //LAB_80104fec
    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
    v0 = 0x19L;
    MEMORY.ref(4, a0).offset(0x2cL).setu(v0);
    v0 = s3 & 0xffffL;
    MEMORY.ref(4, a0).offset(0x40L).setu(v0);
    v0 = s4 & 0xffffL;
    MEMORY.ref(4, a0).offset(0x44L).setu(v0);
    v0 = 0x21L;
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(1, a0).offset(0x3cL).setu(v0);
    sp10 |= 0x1L;
  }

  @Method(0x80105350L)
  public static void FUN_80105350(long a0, long a1, long a2) {
    long v0;
    long v1;
    long s0;
    long s4;

    long s1 = a2;
    long s3 = a0;
    long s5 = a1;

    long sp10 = 0;

    if(s1 >= 0x2710L) {
      s1 = 0x270fL;
    }

    //LAB_8010538c
    a0 = s1 / 1_000;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0) {
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105400
        v0 = v0 | 0x4L;
      }

      //LAB_80105404
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_8010543c
    a0 = s1 / 100;
    s4 = s3 + 0x6L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_80105498
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801054c8
        v0 = v0 | 0x4L;
      }

      //LAB_801054cc
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_80105504
    a0 = s1 / 10;
    s4 = s3 + 0xcL;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_8010555c
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_8010558c
        v0 = v0 | 0x4L;
      }

      //LAB_80105590
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_801055c8
    sp10 = 0x1L;
    s3 = s3 + 0x12L;
    v1 = s1 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = s1 - v0;
    v0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
    a1 = 0;
    v0 = FUN_80023b54(a0, a1);
    a0 = v0;
    v1 = sp10;
    v0 = MEMORY.ref(4, a0).offset(0x0L).get();
    v1 = v1 & 0x2L;
    MEMORY.ref(4, a0).offset(0x4L).setu(s0);
    if(v1 != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_8010562c
      v0 = v0 | 0x4L;
    }

    //LAB_80105630
    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
    MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
    MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
    MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
    sp10 |= 0x1L;
  }

  @Method(0x8010568cL)
  public static void FUN_8010568c(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long s0;
    long s3;

    long s1 = a2;
    long s5 = a0;
    long s6 = a1;
    long s4 = 0;

    long sp10 = 0;

    if(s1 >= 0x2710L) {
      s1 = 0x270fL;
    }

    //LAB_801056d0
    if(a3 >= 0x2710L) {
      s1 = 0x270fL;
    }

    v0 = a3 >>> 1;

    //LAB_801056e0
    if(s1 < v0) {
      s4 = 0x7cabL;
    }

    //LAB_801056f0
    v0 = a3 / 10;

    if(s1 < v0) {
      s4 = 0x7c2bL;
    }

    //LAB_80105714
    a0 = s1 / 1_000;
    a1 = 0;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0) {
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a1 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a1).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a1).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105784
        v0 = v0 | 0x4L;
      }

      //LAB_80105788
      MEMORY.ref(4, a1).offset(0x0L).setu(v0);
      MEMORY.ref(4, a1).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a1).offset(0x40L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a1).offset(0x44L).setu(s6 & 0xffffL);
      MEMORY.ref(4, a1).offset(0x30L).setu(0);
      MEMORY.ref(1, a1).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_801057c0
    if(a1 != 0) {
      MEMORY.ref(4, a1).offset(0x30L).setu(s4);
    }

    //LAB_801057d0
    a0 = s1 / 100;
    s3 = s5 + 0x6L;
    a1 = 0;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_80105830
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a1 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a1).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a1).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105860
        v0 = v0 | 0x4L;
      }

      //LAB_80105864
      MEMORY.ref(4, a1).offset(0x0L).setu(v0);
      MEMORY.ref(4, a1).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a1).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a1).offset(0x44L).setu(s6 & 0xffffL);
      MEMORY.ref(4, a1).offset(0x30L).setu(0);
      MEMORY.ref(1, a1).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_801058a0
    if(a1 != 0) {
      MEMORY.ref(4, a1).offset(0x30L).setu(s4);
    }

    //LAB_801058ac
    a0 = s1 / 10;
    s3 = s5 + 0xcL;
    a1 = 0;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_80105908
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a1 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a1).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a1).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105938
        v0 = v0 | 0x4L;
      }

      //LAB_8010593c
      MEMORY.ref(4, a1).offset(0x0L).setu(v0);
      MEMORY.ref(4, a1).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a1).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a1).offset(0x44L).setu(s6 & 0xffffL);
      MEMORY.ref(4, a1).offset(0x30L).setu(0);
      MEMORY.ref(1, a1).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_80105978
    if(a1 != 0) {
      MEMORY.ref(4, a1).offset(0x30L).setu(s4);
    }

    //LAB_80105984
    sp10 = 0x1L;
    s3 = s5 + 0x12L;
    v1 = s1 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = s1 - v0;
    v0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
    a1 = 0;
    v0 = FUN_80023b54(a0, a1);
    a0 = v0;
    v1 = sp10;
    v0 = MEMORY.ref(4, a0).offset(0x0L).get();
    v1 = v1 & 0x2L;
    MEMORY.ref(4, a0).offset(0x4L).setu(s0);
    if(v1 != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_801059e8
      v0 = v0 | 0x4L;
    }

    //LAB_801059ec
    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
    MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
    MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
    MEMORY.ref(4, a0).offset(0x44L).setu(s6 & 0xffffL);
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
    MEMORY.ref(4, a0).offset(0x30L).setu(s4);
    sp10 |= 0x1L;
  }

  @Method(0x80105a50L)
  public static void FUN_80105a50(long a0, long a1, long a2) {
    long v0;
    long v1;
    long s0;
    long s3;
    long s1 = a2;
    long s4 = a0;
    long s5 = a1;

    long sp10 = 0;

    if(s1 > 0xf_423fL) {
      s1 = 0xf_423fL;
    }

    //LAB_80105a98
    v0 = s1 >>> 5;
    a0 = v0 / 3_125;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0) {
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105b10
        v0 = v0 | 0x4L;
      }

      //LAB_80105b14
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_80105b4c
    a0 = s1 / 10_000;
    s3 = s4 + 0x6L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_80105ba8
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105bd8
        v0 = v0 | 0x4L;
      }

      //LAB_80105bdc
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_80105c18
    a0 = s1 / 1_000;
    s3 = s4 + 0xcL;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_80105c70
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105ca0
        v0 = v0 | 0x4L;
      }

      //LAB_80105ca4
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_80105ce0
    a0 = s1 / 100;
    s3 = s4 + 0x12L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_80105d38
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105d68
        v0 = v0 | 0x4L;
      }

      //LAB_80105d6c
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_80105da4
    a0 = s1 / 10;
    s3 = s4 + 0x18L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp10 & 0x1L) != 0) {
      //LAB_80105dfc
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp10;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105e2c
        v0 = v0 | 0x4L;
      }

      //LAB_80105e30
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp10 |= 0x1L;
    }

    //LAB_80105e68
    sp10 = 0x1L;
    s3 = s4 + 0x1eL;
    v1 = s1 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = s1 - v0;
    v0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
    a1 = 0;
    v0 = FUN_80023b54(a0, a1);
    a0 = v0;
    v1 = sp10;
    v0 = MEMORY.ref(4, a0).offset(0x0L).get();
    v1 = v1 & 0x2L;
    MEMORY.ref(4, a0).offset(0x4L).setu(s0);
    if(v1 != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_80105ecc
      v0 = v0 | 0x4L;
    }

    //LAB_80105ed0
    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
    MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
    MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
    MEMORY.ref(4, a0).offset(0x44L).setu(s5 & 0xffffL);
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
    sp10 |= 0x1L;
  }

  @Method(0x80105f2cL)
  public static void FUN_80105f2c(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long s0;
    long s3;
    long s5;

    long s1 = a2;
    long s2 = a0;
    long s4 = a1;
    long sp3c = a3;

    if(s1 > 0x5f5_e0ffL) {
      s1 = 0x5f5_e0ffL;
    }

    //LAB_80105f74
    a0 = s1 / 10_000_000;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (a3 & 0x1L) != 0) {
      //LAB_80105fc4
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp3c;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105ff4
        v0 = v0 | 0x4L;
      }

      //LAB_80105ff8
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s2 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_80106034
    a0 = s1 / 1_000_000;
    s5 = s2 + 0x6L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp3c & 0x1L) != 0) {
      //LAB_8010608c
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp3c;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801060bc
        v0 = v0 | 0x4L;
      }

      //LAB_801060c0
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_801060fc
    v0 = s1 >>> 5;
    a0 = v0 / 3_125;
    s5 = s2 + 0xcL;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp3c & 0x1L) != 0) {
      //LAB_80106158
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp3c;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80106188
        v0 = v0 | 0x4L;
      }

      //LAB_8010618c
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_801061c4
    a0 = s1 / 10_000;
    s5 = s2 + 0x12L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp3c & 0x1L) != 0) {
      //LAB_80106220
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp3c;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80106250
        v0 = v0 | 0x4L;
      }

      //LAB_80106254
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_80106290
    a0 = s1 / 1_000;
    s5 = s2 + 0x18L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp3c & 0x1L) != 0) {
      //LAB_801062e8
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp3c;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80106318
        v0 = v0 | 0x4L;
      }

      //LAB_8010631c
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_80106358
    a0 = s1 / 100;
    s5 = s2 + 0x1eL;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp3c & 0x1L) != 0) {
      //LAB_801063b0
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp3c;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801063e0
        v0 = v0 | 0x4L;
      }

      //LAB_801063e4
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_8010641c
    a0 = s1 / 10;
    s5 = s2 + 0x24L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp3c & 0x1L) != 0) {
      //LAB_80106474
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      a1 = 0;
      v0 = FUN_80023b54(a0, a1);
      a0 = v0;
      v1 = sp3c;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801064a4
        v0 = v0 | 0x4L;
      }

      //LAB_801064a8
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s5 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_801064e0
    s3 = s2 + 0x2aL;
    a0 = sp3c | 0x1L;
    v1 = s1 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = s1 - v0;
    v0 = s0 & 0xffL;
    sp3c = a0;
    if(v0 != 0 || (a0 & 0x1L) != 0) {
      //LAB_8010652c
      v0 = 0x800c_0000L;
      v0 = FUN_80023b54(MEMORY.ref(4, v0).offset(-0x23c4L).get(), 0);
      a0 = v0;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_8010655c
        v0 = v0 | 0x4L;
      }

      //LAB_80106560
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_80106598
  }

  @Method(0x80106d10L)
  public static void FUN_80106d10(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Method(0x801073f8L)
  public static void FUN_801073f8(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Method(0x80107764L)
  public static void FUN_80107764(long a0, final long a1, final long a2, final long a3) {
    long v0;
    long v1;
    long s0;
    long s3;
    long s4;

    long s1 = a2;
    long s2 = a0;
    long sp3c = a3;

    if(s1 > 999L) {
      s1 = 999L;
    }

    //LAB_801077a0
    a0 = s1 / 100;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (a3 & 0x1L) != 0) {
      //LAB_801077f0
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      v0 = FUN_80023b54(a0, 0);
      a0 = v0;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = sp3c & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80107820
        v0 = v0 | 0x4L;
      }

      //LAB_80107824
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s2 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(a1 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_8010785c
    a0 = s1 / 10;
    s4 = s2 + 0x6L;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (sp3c & 0x1L) != 0) {
      //LAB_801078b4
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      v0 = FUN_80023b54(a0, 0);
      a0 = v0;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = sp3c & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801078e4
        v0 = v0 | 0x4L;
      }

      //LAB_801078e8
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s4 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(a1 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_80107920
    s3 = s2 + 0xcL;
    a0 = sp3c | 0x1L;
    v1 = s1 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = s1 - v0;
    v0 = s0 & 0xffL;
    sp3c = a0;
    if(v0 != 0 || (a0 & 0x1L) != 0) {
      //LAB_8010796c
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      v0 = FUN_80023b54(a0, 0);
      a0 = v0;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = sp3c & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_8010799c
        v0 = v0 | 0x4L;
      }

      //LAB_801079a0
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(a1 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp3c |= 0x1L;
    }

    //LAB_801079d8
  }

  @Method(0x801079fcL)
  public static void FUN_801079fc(long a0, final long a1, final long a2, final long a3) {
    long v0;
    long v1;
    long s0;
    long s1 = a2;
    long s3 = a0;

    long sp34 = a3;

    if(s1 >= 0x64L) {
      s1 = 0x63L;
    }

    //LAB_80107a34
    a0 = s1 / 10;
    v1 = a0 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = a0 - v0;
    v0 = s0 & 0xffL;
    if(v0 != 0 || (a3 & 0x1L) != 0) {
      //LAB_80107a80
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      v0 = FUN_80023b54(a0, 0);
      a0 = v0;
      v1 = sp34;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80107ab0
        v0 = v0 | 0x4L;
      }

      //LAB_80107ab4
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(a1 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp34 |= 0x1L;
    }

    //LAB_80107aec
    s3 = s3 + 0x6L;
    v0 = sp34;
    a0 = v0 | 0x1L;
    v1 = s1 / 10;
    v0 = v1 << 2;
    v0 = v0 + v1;
    v0 = v0 << 1;
    s0 = s1 - v0;
    v0 = s0 & 0xffL;
    sp34 = a0;
    if(v0 != 0 || (a0 & 0x1L) != 0) {
      //LAB_80107b38
      v0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
      v0 = FUN_80023b54(a0, 0);
      a0 = v0;
      v1 = sp34;
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      v1 = v1 & 0x2L;
      MEMORY.ref(4, a0).offset(0x4L).setu(s0);
      if(v1 != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80107b68
        v0 = v0 | 0x4L;
      }

      //LAB_80107b6c
      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
      MEMORY.ref(4, a0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, a0).offset(0x40L).setu(s3 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x44L).setu(a1 & 0xffffL);
      MEMORY.ref(4, a0).offset(0x30L).setu(0);
      MEMORY.ref(1, a0).offset(0x3cL).setu(0x21L);
      sp34 |= 0x1L;
    }

    //LAB_80107ba4
  }

  @Method(0x80107cb4L)
  public static void FUN_80107cb4(long a0, long a1, long a2) {
    final long v0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), 0);
    MEMORY.ref(4, v0).offset(0x00L).oru(0x4L);
    MEMORY.ref(4, v0).offset(0x04L).setu(a2);
    MEMORY.ref(4, v0).offset(0x2cL).setu(0x19L);
    MEMORY.ref(4, v0).offset(0x30L).setu(0x7ca9L);
    MEMORY.ref(1, v0).offset(0x3cL).setu(0x21L);
    MEMORY.ref(4, v0).offset(0x40L).setu(a0 & 0xffffL);
    MEMORY.ref(4, v0).offset(0x44L).setu(a1 & 0xffffL);
  }

  @Method(0x80107dd4L)
  public static void FUN_80107dd4(final long a0, final long a1, final long a2) {
    if(a2 != 0) {
      FUN_80105a50(a0 & 0xffffL, a1 & 0xffffL, a2);
    } else {
      //LAB_80107e08
      final long v0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), 0);
      MEMORY.ref(4, v0).offset(0x30L).setu(0x7ca9L);
      MEMORY.ref(4, v0).offset(0x2cL).setu(0x19L);
      MEMORY.ref(4, v0).offset(0x44L).setu(a1 & 0xffffL);
      MEMORY.ref(4, v0).offset(0x4L).setu(0xdaL);
      MEMORY.ref(4, v0).offset(0x40L).setu((a0 + 0x1eL) & 0xffffL);
      MEMORY.ref(1, v0).offset(0x3cL).setu(0x21L);
      MEMORY.ref(4, v0).oru(0x4L);
    }

    //LAB_80107e58
  }

  @Method(0x80107e70L)
  public static long FUN_80107e70(final long a0, final long a1, final long a2) {
    //LAB_80107e90
    final long a0_0 = _800babc8.offset(2, 0x33cL).offset(a2 * 0x2cL).get();

    if((_800bb0fc.get() & 0x10L) == 0) {
      return 0;
    }

    long v1;
    if((a0_0 & 0x2L) != 0) {
      v1 = 0x2L;
    } else {
      v1 = a0_0 & 0x1L;
    }

    //LAB_80107f00
    if((a0_0 & 0x4L) != 0) {
      v1 = 0x3L;
    }

    //LAB_80107f10
    if((a0_0 & 0x8L) != 0) {
      v1 = 0x4L;
    }

    //LAB_80107f1c
    if((a0_0 & 0x10L) != 0) {
      v1 = 0x5L;
    }

    //LAB_80107f28
    if((a0_0 & 0x20L) != 0) {
      v1 = 0x6L;
    }

    //LAB_80107f34
    if((a0_0 & 0x40L) != 0) {
      v1 = 0x7L;
    }

    //LAB_80107f40
    if((a0_0 & 0x80L) != 0) {
      v1 = 0x8L;
    }

    //LAB_80107f50
    if((v1 & 0xffL) == 0) {
      //LAB_80107f88
      return 0;
    }

    v1 = _800fba7c.offset(((v1 - 0x1L) & 0xffL) * 0x8L).getAddress();
    FUN_80103e90(MEMORY.ref(4, v1).offset(0x0L).get(), (a0 + 0x18L) & 0xffffL, a1 & 0xffffL, MEMORY.ref(1, v1).offset(0x4L).get());

    //LAB_80107f8c
    return 0x1L;
  }

  @Method(0x80107f9cL)
  public static void FUN_80107f9c(long a0, long a1, long a2, long a3, long a4) {
    long v0;
    long v1;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long fp;
    long s5 = a0;
    long s6 = a1;
    long s7 = a2;
    long sp24 = 0;

    if(s7 != -0x1L) {
      if((a3 & 0xffL) != 0) {
        v0 = (short)s5;
        a2 = v0;
        sp24 = v0;
        fp = (short)s6;
        v0 = FUN_80103818(0x4aL, 0x4aL, a2, fp);
        MEMORY.ref(1, v0).offset(0x3cL).setu(0x21L);
        a2 = sp24;
        a3 = fp;
        FUN_80103818(0x99L, 0x99L, a2, a3);
        s2 = fp + 0x8L;
        t0 = sp24;
        s1 = t0 + 0x8L;
        if(s7 < 0x9L) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(-0x23c4L).get();
          v0 = FUN_80023b54(v0 + 0xcfacL, 0);
          s0 = v0;
          a0 = s0;
          a1 = 0x8011_0000L;
          a1 = a1 + 0x42d4L;
          FUN_80104b1c(a0, a1);
          MEMORY.ref(4, s0).offset(0x40L).setu(s1);
          MEMORY.ref(4, s0).offset(0x44L).setu(s2);
          MEMORY.ref(4, s0).offset(0x4L).setu(s7);
          MEMORY.ref(1, s0).offset(0x3cL).setu(0x21L);
          MEMORY.ref(4, s0).offset(0x2cL).addu(0x1L);
        }

        //LAB_80108098
        a0 = s5 + 0x9aL;
        a0 = a0 & 0xffffL;
        v0 = 0x800c_0000L;
        v0 = v0 - 0x1a08L;
        s2 = s7 << 2;
        s2 = s2 + s7;
        s2 = s2 << 5;
        s2 = s2 + v0;
        a1 = s6 + 0x6L;
        a2 = MEMORY.ref(1, s2).offset(0xeL).get();
        a1 = a1 & 0xffffL;
        FUN_80104c30(a0, a1, a2);
        a0 = s5 + 0x70L;
        a0 = a0 & 0xffffL;
        s0 = s6 + 0x11L;
        s0 = s0 & 0xffffL;
        a2 = MEMORY.ref(1, s2).offset(0xfL).get();
        a1 = s0;
        FUN_80104c30(a0, a1, a2);
        s4 = s5 + 0x94L;
        s4 = s4 & 0xffffL;
        a0 = s4;
        a2 = MEMORY.ref(2, s2).offset(0x8L).getSigned();
        a1 = s0;
        FUN_80104dd4(a0, a1, a2);
        a0 = s5 + 0x64L;
        a0 = a0 & 0xffffL;
        v0 = 0x800c_0000L;
        v0 = v0 - 0x5438L;
        s1 = s7 << 1;
        s1 = s1 + s7;
        s1 = s1 << 2;
        s1 = s1 - s7;
        s1 = s1 << 2;
        s1 = s1 + v0;
        s0 = s6 + 0x1cL;
        s0 = s0 & 0xffffL;
        a2 = MEMORY.ref(2, s1).offset(0x334L).getSigned();
        a3 = MEMORY.ref(2, s2).offset(0x66L).get();
        a1 = s0;
        FUN_8010568c(a0, a1, a2, a3);
        s3 = s5 + 0x7cL;
        s3 = s3 & 0xffffL;
        a0 = s3;
        a1 = s0;
        a2 = 0xbL;
        FUN_80107cb4(a0, a1, a2);
        a0 = s5 + 0x8eL;
        a0 = a0 & 0xffffL;
        a2 = MEMORY.ref(2, s2).offset(0x66L).get();
        a1 = s0;
        FUN_80105350(a0, a1, a2);
        a0 = s5 + 0x6aL;
        a0 = a0 & 0xffffL;
        s0 = s6 + 0x27L;
        s0 = s0 & 0xffffL;
        a2 = MEMORY.ref(2, s2).offset(0x6L).getSigned();
        a1 = s0;
        FUN_80104dd4(a0, a1, a2);
        a0 = s3;
        a1 = s0;
        a2 = 0xbL;
        FUN_80107cb4(a0, a1, a2);
        a0 = s4;
        a2 = MEMORY.ref(2, s2).offset(0x6eL).getSigned();
        a1 = s0;
        FUN_80104dd4(a0, a1, a2);
        a0 = (s5 + 0x58L) & 0xffffL;
        s0 = (s6 + 0x32L) & 0xffffL;
        FUN_80105a50(a0, s0, MEMORY.ref(4, s1).offset(0x32cL).get());
        FUN_80107cb4(s3, s0, 0xbL);
        v0 = FUN_800fc698(s7);
        a0 = (s5 + 0x82L) & 0xffffL;
        FUN_80107dd4(a0, s0, v0);

        if(a4 != 0) {
          v0 = FUN_80103818(0x71L, 0x71L, sp24 + 0x38L, fp + 0x18L);
          MEMORY.ref(1, v0).offset(0x3cL).setu(0x21L);
        }
      }

      //LAB_80108218
      s1 = (short)(s5 + 0x30L);
      s0 = (short)(s6 + 0x3L);
      if(FUN_80107e70(s1, s0, s7) == 0) {
        v1 = 0x8011_0000L;
        v1 = v1 + 0x42dcL;
        v0 = v1 + s7 * 0x4L;
        FUN_80103cc4(MEMORY.ref(4, v0).get(), s1, s0, 0x4L);
      }
    }

    //LAB_80108270
  }

  @Method(0x80108a6cL)
  public static void FUN_80108a6c(long a0, long a1, long a2, long a3) {
    long v1;
    long s2;
    long s4;

    long s1 = a1;
    long s3 = a2;
    long s0 = a3;
    long v0 = s0 & 0xffL;

    a2 = a0;

    if(v0 != 0) {
      a0 = 0x15L;
      a1 = s3 & 0xffffL;
      a2 = a2 & 0xffL;
      a2 = a2 + 0x1L;
      FUN_80104c30(a0, a1, a2);
    }

    //LAB_80108ab8
    v1 = MEMORY.ref(1, s1).offset(0x4L).get();
    v0 = 0xfcL;
    if(v1 == v0) {
      //LAB_80108b04
      a0 = 0x8012_0000L;
      a0 = a0 - 0x2fb4L;

      //LAB_80108b20
      a1 = 0x20L;
      a2 = s3 << 16;
      a2 = (int)a2 >> 16;
      a3 = 0x4L;
      FUN_80103cc4(a0, a1, a2, a3);
    } else {
      if((int)v1 >= 0xfdL) {
        //LAB_80108ae8
        v0 = 0xfeL;
        if(v1 == v0) {
          v0 = 0xffL;

          //LAB_80108b10
          a0 = 0x8012_0000L;
          a0 = a0 - 0x3774L;

          //LAB_80108b20
          a1 = 0x20L;
          a2 = s3 << 16;
          a2 = (int)a2 >> 16;
          a3 = 0x4L;
          FUN_80103cc4(a0, a1, a2, a3);
        } else {
          v0 = 0xffL;
          a0 = 0x8012_0000L;
          if(v1 == v0) {
            //LAB_80108b1c
            a0 = a0 - 0x3828L;

            //LAB_80108b20
            a1 = 0x20L;
            a2 = s3 << 16;
            a2 = (int)a2 >> 16;
            a3 = 0x4L;
            FUN_80103cc4(a0, a1, a2, a3);
          }
        }
      } else {
        v0 = 0x1L;
        if((int)v1 >= 0xfL || (int)v1 < 0) {
        } else {
          //LAB_80108b3c
          v1 = MEMORY.ref(1, s1).offset(0x2dL).get();

          if(v1 == v0) {
            v0 = 0x3L;

            //LAB_80108b5c
            a1 = 0x116L;
            a2 = s3 + 0x2fL;
            a2 = a2 & 0xffffL;
            v1 = 0x8012_0000L;
            v0 = MEMORY.ref(1, s1).offset(0x2cL).get();
            v1 = v1 - 0x3e14L;
          } else {
            v0 = 0x3L;
            if(v1 == v0) {
              a1 = 0x116L;

              //LAB_80108b78
              a2 = s3 + 0x2fL;
              a2 = a2 & 0xffffL;
              v1 = 0x8011_0000L;
              v0 = MEMORY.ref(1, s1).offset(0x2cL).get();
              v1 = v1 + 0x4248L;
            } else {
              a1 = 0x116L;
              a2 = s3 + 0x2fL;

              //LAB_80108b90
              a2 = a2 & 0xffffL;
              v1 = 0x8012_0000L;
              v0 = MEMORY.ref(1, s1).offset(0x2cL).get();
              v1 = v1 - 0x3ef8L;
            }
          }

          //LAB_80108ba0
          v0 = v0 * 0x4L;
          v0 = v1 + v0 * 0x4L;
          FUN_80103e90(MEMORY.ref(4, v0).get(), a1, a2, 0x4L);

          if((s0 & 0xffL) != 0) {
            v0 = FUN_80103818(0x4cL, 0x4cL, 0x10L, s3);
            MEMORY.ref(1, v0).offset(0x3cL).setu(0x21L);
            v0 = FUN_80103818(0x4dL, 0x4dL, 0xc0L, s3);
            MEMORY.ref(1, v0).offset(0x3cL).setu(0x21L);
            s2 = MEMORY.ref(4, s1).offset(0x8L).get();

            if(s2 < 0x9L) {
              s0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get() + 0xcfacL, 0);
              FUN_80104b1c(s0, _801142d4.getAddress());
              MEMORY.ref(4, s0).offset(0x40L).setu(0x26L);
              MEMORY.ref(4, s0).offset(0x44L).setu(s3 + 0x8L);
              MEMORY.ref(4, s0).offset(0x4L).setu(s2);
              MEMORY.ref(1, s0).offset(0x3cL).setu(0x21L);
              MEMORY.ref(4, s0).offset(0x2cL).addu(0x1L);
            }

            //LAB_80108c78
            s2 = MEMORY.ref(4, s1).offset(0xcL).get();

            if(s2 < 0x9L) {
              s0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get() + 0xcfacL, 0);
              FUN_80104b1c(s0, _801142d4.getAddress());
              MEMORY.ref(4, s0).offset(0x40L).setu(0x5aL);
              MEMORY.ref(4, s0).offset(0x44L).setu(s3 + 0x8L);
              MEMORY.ref(4, s0).offset(0x4L).setu(s2);
              MEMORY.ref(1, s0).offset(0x3cL).setu(0x21L);
              MEMORY.ref(4, s0).offset(0x2cL).addu(0x1L);
            }

            //LAB_80108cd4
            s2 = MEMORY.ref(4, s1).offset(0x10L).get();

            if(s2 < 0x9L) {
              s0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get() + 0xcfacL, 0);
              FUN_80104b1c(s0, _801142d4.getAddress());
              MEMORY.ref(4, s0).offset(0x40L).setu(0x8eL);
              MEMORY.ref(4, s0).offset(0x44L).setu(s3 + 0x8L);
              MEMORY.ref(4, s0).offset(0x4L).setu(s2);
              MEMORY.ref(1, s0).offset(0x3cL).setu(0x21L);
              MEMORY.ref(4, s0).offset(0x2cL).addu(0x1L);
            }

            //LAB_80108d30
            s0 = (s3 + 0x6L) & 0xffffL;
            FUN_80104c30(0x0e0L, s0, MEMORY.ref(1, s1).offset(0x14L).get());
            FUN_80104c30(0x10dL, s0, MEMORY.ref(1, s1).offset(0x15L).get());
            FUN_80105350(0x12eL, s0, MEMORY.ref(2, s1).offset(0x16L).getSigned());
            FUN_80105350(0x14cL, s0, MEMORY.ref(2, s1).offset(0x18L).getSigned());

            s0 = (s3 + 0x11L) & 0xffffL;
            FUN_80105f2c(0x0f5L, s0, MEMORY.ref(4, s1).offset(0x1cL).get(), 0);
            FUN_80107764(0x132L, s0, FUN_80023674(MEMORY.ref(4, s1).offset(0x20L).get(), 0), 0x1L);
            FUN_80107cb4(0x144L, s0, 0xaL);
            FUN_801079fc(0x14aL, s0, FUN_80023674(MEMORY.ref(4, s1).offset(0x20L).get(), 0x1L), 0x1L);
            FUN_80107cb4(0x156L, s0, 0xaL);
            FUN_801079fc(0x15cL, s0, FUN_80023674(MEMORY.ref(4, s1).offset(0x20L).get(), 0x2L), 0x1L);
            FUN_80104c30(0x158L, (s3 + 0x22L) & 0xffffL, MEMORY.ref(4, s1).offset(0x28L).get());
            FUN_801098c0(s1 + 0x24L, 0xdfL, (s3 + 0x1bL) & 0xffL);
          }
        }
      }
    }

    //LAB_80108e3c
  }

  @Method(0x801098c0L)
  public static void FUN_801098c0(long a0, long a1, long a2) {
    long v0;
    long s0;
    long s4 = a0;
    long s2 = a1 & 0xffL;

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x28);
    final Value sp18 = tmp.get();

    sp18.offset(4, 0x0L).setu(_800fbabc.offset(0x00L));
    sp18.offset(4, 0x0L).setu(_800fbabc.offset(0x04L));
    sp18.offset(4, 0x0L).setu(_800fbabc.offset(0x08L));
    sp18.offset(4, 0x0L).setu(_800fbabc.offset(0x0cL));
    sp18.offset(4, 0x0L).setu(_800fbabc.offset(0x10L));
    sp18.offset(4, 0x0L).setu(_800fbabc.offset(0x14L));
    sp18.offset(4, 0x0L).setu(_800fbabc.offset(0x18L));
    sp18.offset(4, 0x0L).setu(_800fbabc.offset(0x1cL));

    //LAB_80109934
    for(long s1 = 0; s1 < 8; s1++) {
      v0 = sp18.offset(s1 * 0x4L).get();
      a0 = v0 & 0x1fL;
      v0 = v0 >>> 5;
      v0 = MEMORY.ref(4, s4).offset(v0 * 0x4L).get();
      if((v0 & 0x1L << a0) != 0) {
        s0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), 0);
        sp18.offset(2, 0x4L).setu(a2 & 0xffL);
        sp18.offset(2, 0x2L).setu(s2);
        sp18.offset(1, 0x0L).setu(s1 + 0xdL);
        FUN_80104b1c(s0, sp18.getAddress());
        MEMORY.ref(1, s0).offset(0x3cL).setu(0x21L);
      }

      //LAB_801099a0
      s2 = s2 + 0xcL;
    }

    tmp.release();
  }

  @Method(0x80109b08L)
  public static long FUN_80109b08(long a0, long a1) {
    long v1;
    long a2;
    long a3;
    long s0;

    long v0;
    long s4 = a0;
    long s2 = a1;

    switch((int)_8011e0d4.get()) {
      case 1 -> {
        _8011e0bc.setu(0);
        _8011e0c0.offset(1, 0x6L).setu(0xfL);
        _8011e0c0.offset(1, 0x4L).setu(0);
        _8011e0c0.offset(1, 0x5L).setu(0);
        _8011e0c0.offset(4, 0x8L).setu(0);
        _8011e0c0.offset(4, 0xcL).setu(0);
        _8011e0c0.offset(1, 0x10L).setu(0);
        _8011e0c0.setu(0);
        _8011e0d4.addu(0x1L);
        FUN_8002df60(0);
      }

      case 2 -> {
        final Ref<Long> sp50 = new Ref<>();
        final Ref<Long> sp54 = new Ref<>();
        FUN_8002efb8(0, sp50, sp54);

        _8011e0b4.setu(addToLinkedListTail(0x280L));
        _8011e0b0.setu(addToLinkedListTail(0x100L));

        //LAB_80109bfc
        a1 = s2 + 0x348L;
        a2 = 0xeL;
        do {
          MEMORY.ref(1, a1).offset(0x4L).setu(0xffL);
          a2 = a2 - 0x1L;
          a1 = a1 - 0x3cL;
        } while((int)a2 >= 0);

        v1 = sp54.get();
        if(v1 == 0x1L || v1 == 0x2L) {
          //LAB_80109ea4
          _8011e0c8.setu(v1);
          _8011e0d4.setu(0x8L);
        } else if(v1 == 0x4L) {
          //LAB_80109ea4
          _8011e0c8.setu(v1);
          _8011e0d4.setu(0x8L);
        } else {
          //LAB_80109c30
          final Ref<Long> fileCount = new Ref<>(_8011e0c0.get());
          v0 = FUN_8002ed48(0, _800fbadc.getAddress(), _8011e0b4.get(), fileCount, 0, 0xfL);
          _8011e0c0.setu(fileCount.get());
          _8011e0c0.offset(0x8L).setu(v0);
          if(v0 != 0) {
            v1 = 0x8L;
          } else {
            v1 = 0x4L;
          }

          //LAB_80109c70
          _8011e0d4.setu(v1);
          memcardSaveIndex_8011e0b8.setu(0);
        }

        //LAB_8010a098
      }

      case 4 -> {
        final long memcardSaveIndex = memcardSaveIndex_8011e0b8.get();
        if(memcardSaveIndex >= _8011e0c0.get()) { // File count
          _8011e0d4.setu(0x7L);
        } else {
          //LAB_80109cb4
          a0 = _8011e0b4.deref(4).offset(memcardSaveIndex * 0x28L).getAddress();
          v0 = MEMORY.ref(4, a0).offset(0x18L).get();

          if((int)v0 >= 0) {
            a3 = v0;
          } else {
            a3 = v0 + 0x1fffL;
          }

          //LAB_80109ce0
          a3 = (int)a3 >> 13;
          v0 = v0 & 0x1fffL;
          v0 = v0 > 0 ? 1 : 0;
          s0 = a3 + v0;
          _8011e0c0.offset(1, 0x6L).subu(s0);
          if(strncmp(MEMORY.ref(1, a0).getString(), "BASCUS-94491drgnpda", 19) == 0) {
            _8011e0c0.offset(1, 0x5L).setu(s0);
            _8011e0d4.setu(0x6L);
            //LAB_80109d28
          } else if(strncmp(_8011e0b4.deref(1).offset(memcardSaveIndex_8011e0b8.get() * 0x28L).getString(), "BASCUS-94491drgn00", 18) != 0) {
            _8011e0bc.addu(s0);
            _8011e0d4.setu(0x6L);
          } else {
            //LAB_80109d70
            FUN_8002e908(0, _8011e0b4.deref(1).offset(memcardSaveIndex_8011e0b8.get() * 0x28L).getAddress(), _8011e0b0.get(), 0x180L, 0x80L);
            _8011e0d4.setu(0x5L);
          }
        }

        //LAB_8010a098
      }

      case 5 -> {
        final Ref<Long> sp50 = new Ref<>();
        final Ref<Long> sp54 = new Ref<>();
        if(FUN_8002efb8(0x1L, sp50, sp54) != 0) {
          if(sp54.get() == 0) {
            s0 = _8011e0b0.deref(1).offset(0x4L).get();

            if(s0 < 0xfL && MEMORY.ref(1, s2).offset(s0 * 0x3cL).offset(0x4L).get() == 0xffL) {
              //LAB_80109e38
              if(_8011e0b0.deref(4).get() == 0x5a02_0006L) {
                memcpy(s2 + s0 * 0x3cL, _8011e0b0.get(), 0x3c);

                a0 = MEMORY.ref(4, s2).offset(s0 * 0x3cL).offset(0x30L).get();
                if(_8011e0c0.offset(4, 0xcL).get() < a0) {
                  _8011e0c0.offset(4, 0xcL).setu(a0);
                  _8011e0c0.offset(1, 0x10L).setu(s0);
                }

                //LAB_80109e7c
                _8011e0c0.offset(1, 0x4L).addu(0x1L);
              } else {
                //LAB_80109e90
                MEMORY.ref(1, s2).offset(s0 * 0x3cL).offset(0x4L).setu(0xfdL);
              }
            } else {
              //LAB_80109e1c
              MEMORY.ref(1, s2).offset(s0 * 0x3cL).offset(0x4L).setu(0xfeL);
            }
          }

          //LAB_80109e94
          _8011e0d4.setu(0x6L);
        }

        //LAB_8010a098
      }

      case 6 -> {
        _8011e0d4.setu(0x4L);
        memcardSaveIndex_8011e0b8.addu(0x1L);

        //LAB_8010a098
      }

      // Save files loaded
      case 7 -> {
        s0 = 0xeL;

        //LAB_80109f04
        for(a2 = 0; a2 < (int)_8011e0c0.offset(1, 0x5L).get(); a2++) {
          //LAB_80109f28
          while(MEMORY.ref(1, s2).offset(s0 * 0x3cL).offset(0x4L).get() != 0xffL) {
            if(s0 != 0) {
              s0--;
            }

            //LAB_80109f38
          }

          //LAB_80109f58
          MEMORY.ref(1, s2).offset(s0 * 0x3cL).offset(0x4L).setu(0xfcL);
        }

        //LAB_80109f80
        //LAB_80109fa4
        for(a2 = 0; a2 < (int)_8011e0bc.get(); a2++) {
          //LAB_80109fc8
          while(MEMORY.ref(1, s2).offset(s0 * 0x3cL).offset(0x4L).get() != 0xffL) {
            if(s0 != 0) {
              s0--;
            }

            //LAB_80109fd8
          }

          //LAB_80109ff8
          MEMORY.ref(1, s2).offset(s0 * 0x3cL).offset(0x4L).setu(0xfeL);
        }

        //LAB_8010a068
        //LAB_8010a06c
        removeFromLinkedList(_8011e0b4.get());
        removeFromLinkedList(_8011e0b0.get());
        _8011e0d4.setu(0);

        //LAB_8010a098
      }

      case 8 -> {
        _8011e0c0.offset(1, 0x6L).setu(0xfL);
        _8011e0c0.offset(1, 0x4L).setu(0);
        _8011e0c0.offset(1, 0x5L).setu(0);
        _8011e0c0.offset(4, 0xcL).setu(0);
        _8011e0c0.offset(1, 0x10L).setu(0);
        _8011e0c0.setu(0);

        //LAB_8010a058
        a1 = s2 + 0x348L;
        a2 = 0xeL;
        do {
          MEMORY.ref(1, a1).offset(0x4L).setu(0xffL);
          a1 -= 0x3cL;
          a2--;
        } while((int)a2 >= 0);

        //LAB_8010a068
        //LAB_8010a06c
        removeFromLinkedList(_8011e0b4.get());
        removeFromLinkedList(_8011e0b0.get());
        _8011e0d4.setu(0);

        //LAB_8010a098
      }
    }

    //LAB_8010a09c
    MEMORY.ref(4, s4).offset(0x00L).setu(_8011e0c0.offset(0x00L));
    MEMORY.ref(4, s4).offset(0x04L).setu(_8011e0c0.offset(0x04L));
    MEMORY.ref(4, s4).offset(0x08L).setu(_8011e0c0.offset(0x08L));
    MEMORY.ref(4, s4).offset(0x0cL).setu(_8011e0c0.offset(0x0cL));
    MEMORY.ref(4, s4).offset(0x10L).setu(_8011e0c0.offset(0x10L));
    return s4;
  }

  @Method(0x8010a0ecL)
  public static long FUN_8010a0ec(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x8010a344L)
  public static long FUN_8010a344(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5) {
    assert false;
    return 0;
  }

  @Method(0x8010a948L)
  public static void FUN_8010a948() {
    assert false;
  }

  @Method(0x8010d614L)
  public static void FUN_8010d614() {
    assert false;
  }

  @Method(0x8010ececL)
  public static long FUN_8010ecec(long a0) {
    long v0;
    long a1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    long s3;
    long s4;
    long s5;
    long s2 = a0;
    long v1;

    switch((int)MEMORY.ref(1, s2).offset(0xcL).get()) {
      case 0:
        return 0x1L;

      case 1:
        MEMORY.ref(1, s2).offset(0xcL).setu(0x2L);
        MEMORY.ref(4, s2).offset(0x4L).setu(0);
        v0 = FUN_80103818(0x95L, 0x8eL, MEMORY.ref(2, s2).offset(0x1cL).get() - 0x32L, MEMORY.ref(2, s2).offset(0x1eL).get() - 0xaL);
        MEMORY.ref(4, s2).offset(0x8L).setu(v0);
        MEMORY.ref(1, v0).offset(0x3cL).setu(0x20L);
        _8011e1e8.setu(0);
        MEMORY.ref(4, s2).offset(0x8L).deref(4).offset(0x18L).setu(0x8eL);

      case 2:
        if(MEMORY.ref(4, s2).offset(0x8L).deref(4).offset(0xcL).get() != 0) {
          MEMORY.ref(1, s2).offset(0xcL).setu(0x3);
        }

        break;

      case 3:
        _800bdf00.setu(0x1fL);
        s5 = MEMORY.ref(1, s2).offset(0x1cL).get() + 0x3cL;
        s4 = MEMORY.ref(1, s2).offset(0x1eL).get() + 0x7L;
        MEMORY.ref(4, s2).offset(0x10L).addu(0x1L);
        if(MEMORY.ref(4, s2).get() != 0) {
          t0 = FUN_80103dd4(MEMORY.ref(4, s2).get()) & 0xffffL;
          final Memory.TemporaryReservation sp0x38tmp = MEMORY.temp((int)((t0 + 0x1L) * 0x2L));
          final Value sp0x38 = sp0x38tmp.get();

          final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp((int)(t0 * 0x4L));
          final Value sp0x10 = sp0x10tmp.get();

          sp0x10.offset(0x0L).setu(sp0x38.getAddress());

          a1 = 0;
          s3 = 0x1L;

          //LAB_8010ee1c
          for(s0 = 0; s0 < (int)t0; s0++) {
            sp0x38.offset(2, s0 * 0x2L).setu(MEMORY.ref(4, s2).deref(2).offset(s0 * 0x2L));

            if(sp0x38.offset(2, s0 * 0x2L).get() == 0xa1ffL) {
              sp0x10.offset(0x4L).offset(a1).setu(sp0x38.offset(2, (s0 + 0x1L) * 0x2L).getAddress());
              sp0x38.offset(2, s0 * 0x2L).setu(0xa0ffL);
              a1 = a1 + 0x4L;
              s3 = s3 + 0x1L;
            }

            //LAB_8010ee50
          }

          //LAB_8010ee68
          sp0x38.offset(2, s0 * 0x2L).setu(0xa0ffL);

          //LAB_8010ee80
          for(s0 = 0; s0 < s3; s0++) {
            FUN_80103e90(sp0x10.offset(s0 * 0x4L).get(), s5 & 0xffL, s4 & 0xffL, 0x4L);

            s4 = s4 + 0xeL;
          }

          sp0x10tmp.release();
          sp0x38tmp.release();
        }

        //LAB_8010eeac
        _800bdf00.setu(0x21L);
        s1 = MEMORY.ref(1, s2).offset(0x15L).get();

        if(s1 == 0) {
          //LAB_8010eed8
          if((inventoryJoypadInput_800bdc44.get() & 0x60L) != 0) {
            playSound(0x2L);
            MEMORY.ref(1, s2).offset(0xcL).setu(0x4L);
            _8011e1e8.setu(0x1L);
          }

          break;
        }

        if(s1 != 0x2L) {
          break;
        }

        //LAB_8010ef10
        if(MEMORY.ref(4, s2).offset(0x4L).get() == 0) {
          v0 = FUN_80103818(0x7dL, 0x7dL, MEMORY.ref(2, s2).offset(0x1cL).get() + 0x2dL, (s4 & 0xffL) + MEMORY.ref(4, s2).offset(0x18L).get() * 14 + 0x5L);
          MEMORY.ref(4, s2).offset(0x4L).setu(v0);
          MEMORY.ref(4, v0).offset(0x38L).setu(0);
          MEMORY.ref(4, v0).offset(0x34L).setu(0);
          MEMORY.ref(4, s2).offset(0x4L).deref(1).offset(0x3cL).setu(0x20L);
        }

        //LAB_8010ef64
        a3 = 0x5L;
        s0 = s4 & 0xffL;
        a2 = s0 + 0x7L;
        v1 = 0x8012_0000L;
        a0 = v1 - 0x3df4L;
        _800bdf00.setu(0x1fL);
        a1 = (MEMORY.ref(2, s2).offset(0x1cL).get() + 0x3cL) & 0xffffL;
        if(MEMORY.ref(4, s2).offset(0x18L).get() != 0) {
          a3 = 0x4L;
        }

        //LAB_8010ef98
        FUN_80103e90(a0, a1, a2, a3);
        a3 = 0x4L;
        a2 = s0 + 0x15L;
        v0 = 0x8012_0000L;
        a0 = v0 - 0x3decL;
        a1 = (MEMORY.ref(2, s2).offset(0x1cL).get() + 0x3cL) & 0xffffL;
        if(MEMORY.ref(4, s2).offset(0x18L).get() != 0) {
          a3 = 0x5L;
        }

        //LAB_8010efc8
        FUN_80103e90(a0, a1, a2, a3);
        _800bdf00.setu(0x21L);
        v1 = FUN_801041d8(s2 + 0x18L);
        if(v1 == s1) {
          //LAB_8010f040
          MEMORY.ref(1, s2).offset(0xcL).setu(0x4L);
          v1 = 0x8012_0000L;
          MEMORY.ref(4, v1).offset(-0x1e18L).setu(MEMORY.ref(4, s2).offset(0x18L).get() + 0x1L);
        } else if((int)v1 >= 0x3L) {
          //LAB_8010f000
          if((int)v1 < 0x5L) {
            v1 = 0x8012_0000L;

            //LAB_8010f05c
            MEMORY.ref(1, s2).offset(0xcL).setu(0x4L);
            MEMORY.ref(4, v1).offset(-0x1e18L).setu(0x2L);
          }
        } else if(v1 == 0x1L) {
          //LAB_8010f014
          v1 = MEMORY.ref(4, s2).offset(0x18L).get();

          v0 = v1 << 3;
          v0 = v0 - v1;
          v0 = v0 << 1;
          v1 = s4 & 0xffL;
          v0 = v0 + v1;
          v1 = MEMORY.ref(4, s2).offset(0x4L).get();
          v0 = v0 + 0x5L;
          MEMORY.ref(4, v1).offset(0x44L).setu(v0);
        }

        break;

      case 4:
        a0 = MEMORY.ref(4, s2).offset(0x4L).get();
        MEMORY.ref(1, s2).offset(0xcL).setu(0x5L);
        if(a0 != 0) {
          FUN_800242e8(a0);
        }

        //LAB_8010f084
        FUN_800242e8(MEMORY.ref(4, s2).offset(0x8L).get());
        v0 = FUN_80103818(0x8eL, 0x95L, MEMORY.ref(2, s2).offset(0x1cL).get() - 0x32L, MEMORY.ref(2, s2).offset(0x1eL).get() - 0xaL);
        MEMORY.ref(4, s2).offset(0x8L).setu(v0);
        MEMORY.ref(1, v0).offset(0x3cL).setu(0x20L);
        MEMORY.ref(4, s2).offset(0x8L).deref(4).oru(0x10L);
        break;

      case 5:
        if(MEMORY.ref(4, s2).offset(0x8L).deref(4).offset(0xcL).get() != 0) {
          MEMORY.ref(1, s2).offset(0xcL).setu(0x6L);
        }

        break;

      case 6:
        v0 = 0x8012_0000L;
        MEMORY.ref(1, s2).offset(0xcL).setu(0);
        return MEMORY.ref(4, v0).offset(-0x1e18L).get();
    }

    //LAB_8010f108
    //LAB_8010f10c
    return 0;
  }

  @Method(0x8010f130L)
  public static void FUN_8010f130(final long a0, final long a1) {
    _8011dc90.setu(a0);

    _8011dc90.offset(2, 0x1cL).setu(0x78L);
    _8011dc90.offset(2, 0x1eL).setu(0x64L);
    _8011dc90.offset(1, 0x15L).setu(a1);
    _8011dc90.offset(4, 0x18L).setu(0);
    _8011dc90.offset(4, 0x10L).setu(0);
    _8011dc90.offset(1, 0x0cL).setu(0x1L);
  }

  @Method(0x8010f198L)
  public static void FUN_8010f198() {
    assert false;
  }

  @Method(0x80110030L)
  public static void FUN_80110030(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long s1;

    final long spc0 = a0;

    //LAB_80110070
    final long[] sp10 = new long[10];
    for(int i = 0; i < 10; i++) {
      sp10[i] = _800fbd08.get(i).get();
    }

    //LAB_801100bc
    final long[] sp38 = new long[9];
    for(int i = 0; i < 9; i++) {
      sp38[i] = _800fbd30.get(i).get();
    }

    //LAB_80110104
    final long[] sp60 = new long[9];
    for(int i = 0; i < 9; i++) {
      sp60[i] = _800fbd54.get(i).get();
    }

    FUN_8002a6fc();

    long s5 = 0;
    long t1 = 0;
    long fp = 0;
    long s2 = _800babc8.getAddress();
    long s0 = _800be5f8.getAddress();

    //LAB_80110174
    for(int s7 = 0; s7 < 9; s7++) {
      a2 = sp38[s7];
      s1 = sp60[s7];

      MEMORY.ref(4, s0).offset(0x0L).setu(MEMORY.ref(4, s2).offset(0x32cL));
      MEMORY.ref(2, s0).offset(0x4L).setu(MEMORY.ref(2, s2).offset(0x334L));
      MEMORY.ref(2, s0).offset(0x6L).setu(MEMORY.ref(2, s2).offset(0x336L));
      MEMORY.ref(2, s0).offset(0x8L).setu(MEMORY.ref(2, s2).offset(0x338L));
      MEMORY.ref(2, s0).offset(0xaL).setu(MEMORY.ref(2, s2).offset(0x33aL));
      MEMORY.ref(2, s0).offset(0xcL).setu(MEMORY.ref(2, s2).offset(0x33cL));
      MEMORY.ref(1, s0).offset(0xeL).setu(MEMORY.ref(1, s2).offset(0x33eL));
      MEMORY.ref(1, s0).offset(0xfL).setu(MEMORY.ref(1, s2).offset(0x33fL));

      //LAB_801101e4
      for(int i = 0; i < 0x5L; i++) {
        _800be5f8.offset(1, i).offset(s5).offset(0x30L).setu(_800babc8.offset(1, i).offset(fp).offset(0x340L));
      }

      MEMORY.ref(1, s0).offset(0x35L).setu(MEMORY.ref(1, s2).offset(0x345L));

      //LAB_80110220
      for(int i = 0; i < 0x8L; i++) {
        _800be5f8.offset(1, i).offset(s5).offset(0x36L).setu(_800babc8.offset(1, i).offset(fp).offset(0x346L));
        _800be5f8.offset(1, i).offset(s5).offset(0x3eL).setu(_800babc8.offset(1, i).offset(fp).offset(0x34eL));
      }

      v0 = a2 + MEMORY.ref(1, s0).offset(0xeL).get() * 0x8L;
      MEMORY.ref(2, s0).offset(0x66L).setu(MEMORY.ref(2, v0).offset(0x0L));
      MEMORY.ref(1, s0).offset(0x68L).setu(MEMORY.ref(1, v0).offset(0x2L));
      MEMORY.ref(1, s0).offset(0x69L).setu(MEMORY.ref(1, v0).offset(0x3L));
      MEMORY.ref(1, s0).offset(0x6aL).setu(MEMORY.ref(1, v0).offset(0x4L));
      MEMORY.ref(1, s0).offset(0x6bL).setu(MEMORY.ref(1, v0).offset(0x5L));
      MEMORY.ref(1, s0).offset(0x6cL).setu(MEMORY.ref(1, v0).offset(0x6L));
      MEMORY.ref(1, s0).offset(0x6dL).setu(MEMORY.ref(1, v0).offset(0x7L));

      v0 = s1 + MEMORY.ref(1, s0).offset(0xfL).get() * 0x8L;
      MEMORY.ref(2, s0).offset(0x6eL).setu(MEMORY.ref(2, v0).offset(0x0L));
      MEMORY.ref(1, s0).offset(0x70L).setu(MEMORY.ref(1, v0).offset(0x2L));
      MEMORY.ref(1, s0).offset(0x71L).setu(MEMORY.ref(1, v0).offset(0x3L));
      MEMORY.ref(1, s0).offset(0x72L).setu(MEMORY.ref(1, v0).offset(0x4L));
      MEMORY.ref(1, s0).offset(0x73L).setu(MEMORY.ref(1, v0).offset(0x5L));

      v0 = s1 + MEMORY.ref(1, s0).offset(0xfL).get() * 0x8L;
      MEMORY.ref(1, s0).offset(0x74L).setu(MEMORY.ref(1, v0).offset(0x6L));
      MEMORY.ref(1, s0).offset(0x75L).setu(MEMORY.ref(1, v0).offset(0x7L));

      a2 = MEMORY.ref(1, s0).offset(0x35L).getSigned();
      if(a2 != -0x1L) {
        a0 = ptrTable_80114070.offset(a2 * 0x4L).deref(4).offset(_800be5f8.offset(1, a2 - _8004f5ac.offset(t1).getSigned()).offset(s5).offset(0x36L).get() * 0x4L).getAddress();

        MEMORY.ref(2, s0).offset(0x9cL).setu(MEMORY.ref(2, a0).offset(0x0L));
        MEMORY.ref(1, s0).offset(0x9eL).setu(MEMORY.ref(1, a0).offset(0x2L));
        MEMORY.ref(1, s0).offset(0x9fL).setu(MEMORY.ref(1, a0).offset(0x3L));
      }

      //LAB_8011042c
      FUN_8011085c(s7);

      v0 = sp10[s7];
      a0 = v0 & 0x1fL;
      v0 = v0 >>> 5;
      if((_800bad64.offset(4, v0 * 0x4L).get() & (0x1L << a0)) != 0) {
        MEMORY.ref(2, s0).offset(0xcL).oru(0x2000L);
        a0 = sp10[s7];

        if(((_800babc8.offset(2, 0x4e6L).getSigned() >> a0) & 0x1L) == 0) {
          _800babc8.offset(2, 0x4e6L).oru(0x1L << a0);

          v0 = s1 + MEMORY.ref(1, s0).offset(0xfL).get() * 0x8L;
          MEMORY.ref(2, s0).offset(0x6L).setu(MEMORY.ref(2, v0));
          MEMORY.ref(2, s0).offset(0x6eL).setu(MEMORY.ref(2, v0));
        }
      } else {
        //LAB_801104ec
        MEMORY.ref(2, s0).offset(0x6L).setu(0);
        MEMORY.ref(2, s0).offset(0x6eL).setu(0);
        MEMORY.ref(1, s0).offset(0xfL).setu(0);
      }

      //LAB_801104f8
      if(s7 == 0) {
        v0 = sp10[9];

        a0 = v0 & 0x1fL;
        v0 = v0 >>> 5;
        if((_800bad64.offset(4, v0 * 0x4L).get() & (0x1L << a0)) != 0) {
          _800be5f8.offset(2, 0xcL).oru(0x6000L);
          _800be5f8.offset(1, 0xfL).setu(_800babc8.offset(1, 0x33fL));

          a1 = sp10[0];

          if(((_800babc8.offset(2, 0x4e6L).getSigned() >> a1) & 0x1L) == 0) {
            _800babc8.offset(2, 0x4e6L).oru((0x1L << a1));
            v1 = s1 + _800be5f8.offset(1, 0xfL).get() * 0x8L;
            _800be5f8.offset(2, 0x6L).setu(MEMORY.ref(2, v1));
            _800be5f8.offset(2, 0x6eL).setu(MEMORY.ref(2, v1));
          } else {
            //LAB_80110590
            v1 = s1 + _800be5f8.offset(1, 0xfL).get() * 0x8L;
            _800be5f8.offset(2, 0x6L).setu(_800babc8.offset(2, 0x336L));
            _800be5f8.offset(2, 0x6eL).setu(MEMORY.ref(2, v1));
          }
        }
      }

      //LAB_801105b0
      a1 = MEMORY.ref(2, s0).offset(0x66L).get() * (MEMORY.ref(2, s0).offset(0x62L).getSigned() + 0x64L) / 100;

      if(a1 >= 9999L) {
        a1 = 9999L;
      }

      //LAB_801105f0
      MEMORY.ref(2, s0).offset(0x66L).setu(a1);

      if(MEMORY.ref(2, s0).offset(0x4L).getSigned() > a1) {
        MEMORY.ref(2, s0).offset(0x4L).setu(a1);
      }

      //LAB_80110608
      a1 = MEMORY.ref(2, s0).offset(0x6eL).getSigned() * (MEMORY.ref(2, s0).offset(0x64L).getSigned() + 0x64L) / 100;

      MEMORY.ref(2, s0).offset(0x6eL).setu(a1);

      if((short)a1 < MEMORY.ref(2, s0).offset(0x6L).getSigned()) {
        MEMORY.ref(2, s0).offset(0x6L).setu(a1);
      }

      //LAB_80110654
      s0 = s0 + 0xa0L;
      s5 = s5 + 0xa0L;
      t1 = t1 + 0x2L;
      fp = fp + 0x2cL;
      s2 = s2 + 0x2cL;
    }

    if(spc0 == 0x1L) {
      FUN_80012bb4();
      _800be5d0.setu(spc0);
    }

    //LAB_8011069c
  }

  @Method(0x801106ccL)
  public static void FUN_801106cc(final long a0) {
    FUN_8002a8f8();

    final long v0 = _80111ff0.offset(a0 * 0x1cL).getAddress();
    _800be5d8.offset(0x00L).setu(MEMORY.ref(1, v0).offset(0x00L));
    _800be5d8.offset(0x01L).setu(MEMORY.ref(1, v0).offset(0x01L));
    _800be5d8.offset(0x02L).setu(MEMORY.ref(1, v0).offset(0x02L));
    _800be5d8.offset(0x03L).setu(MEMORY.ref(1, v0).offset(0x03L));
    _800be5d8.offset(0x04L).setu(MEMORY.ref(1, v0).offset(0x04L));
    _800be5d8.offset(0x05L).setu(MEMORY.ref(1, v0).offset(0x05L));
    _800be5d8.offset(0x06L).setu(MEMORY.ref(1, v0).offset(0x06L));
    _800be5d8.offset(0x07L).setu(MEMORY.ref(1, v0).offset(0x07L));
    _800be5d8.offset(0x08L).setu(MEMORY.ref(1, v0).offset(0x08L));
    _800be5d8.offset(0x09L).setu(MEMORY.ref(1, v0).offset(0x09L));
    _800be5d8.offset(0x0aL).setu(MEMORY.ref(1, v0).offset(0x0aL));
    _800be5d8.offset(0x0bL).setu(MEMORY.ref(1, v0).offset(0x0bL));
    _800be5d8.offset(0x0cL).setu(MEMORY.ref(1, v0).offset(0x0cL));
    _800be5d8.offset(0x0dL).setu(MEMORY.ref(1, v0).offset(0x0dL));
    _800be5d8.offset(0x0eL).setu(MEMORY.ref(1, v0).offset(0x0eL));
    _800be5d8.offset(0x0fL).setu(MEMORY.ref(1, v0).offset(0x0fL));
    _800be5d8.offset(0x10L).setu(MEMORY.ref(1, v0).offset(0x10L));
    _800be5d8.offset(0x11L).setu(MEMORY.ref(1, v0).offset(0x11L));
    _800be5d8.offset(0x12L).setu(MEMORY.ref(1, v0).offset(0x12L));
    _800be5d8.offset(0x13L).setu(MEMORY.ref(1, v0).offset(0x13L));
    _800be5d8.offset(0x14L).setu(MEMORY.ref(1, v0).offset(0x14L));
    _800be5d8.offset(0x15L).setu(MEMORY.ref(1, v0).offset(0x15L));
    _800be5d8.offset(0x16L).setu(MEMORY.ref(1, v0).offset(0x16L));
    _800be5d8.offset(0x17L).setu(MEMORY.ref(1, v0).offset(0x17L));
    _800be5d8.offset(0x18L).setu(MEMORY.ref(1, v0).offset(0x18L));
    _800be5d8.offset(0x19L).setu(MEMORY.ref(1, v0).offset(0x19L));
    _800be5d8.offset(0x1aL).setu(MEMORY.ref(1, v0).offset(0x1aL));
    _800be5d8.offset(0x1bL).setu(MEMORY.ref(1, v0).offset(0x1bL));
  }

  @Method(0x8011085cL)
  public static void FUN_8011085c(long a0) {
    long v0;
    long v1;
    long a1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;

    s0 = a0;
    FUN_8002a86c(a0);
    s3 = 0;
    v0 = 0x800c_0000L;
    s6 = v0 - 0x1a08L;
    v0 = s0 << 2;
    v0 = v0 + s0;
    s4 = v0 << 5;
    s0 = s4 + s6;
    s5 = 0x800c_0000L;
    s1 = s5 - 0x1a28L;
    s2 = 0x1L;

    //LAB_801108b0
    do {
      v0 = s3 + s4;
      a0 = v0 + s6;
      v1 = MEMORY.ref(1, a0).offset(0x30L).get();
      v0 = 0xffL;
      if(v1 != v0) {
        a0 = v1;
        FUN_801106cc(a0);
        v0 = MEMORY.ref(1, s0).offset(0x76L).get();
        v1 = MEMORY.ref(1, s5).offset(-0x1a28L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x76L).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x77L).get();
        v1 = MEMORY.ref(1, s1).offset(0x1L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x77L).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x78L).get();
        v1 = MEMORY.ref(1, s1).offset(0x2L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x78L).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x79L).get();
        v1 = MEMORY.ref(1, s1).offset(0x3L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x79L).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x7aL).get();
        v1 = MEMORY.ref(1, s1).offset(0x4L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x7aL).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x7bL).get();
        v1 = MEMORY.ref(1, s1).offset(0x5L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x7bL).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x7cL).get();
        v1 = MEMORY.ref(1, s1).offset(0x6L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x7cL).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x7dL).get();
        v1 = MEMORY.ref(1, s1).offset(0x7L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x7dL).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x7eL).get();
        v1 = MEMORY.ref(1, s1).offset(0x8L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x7eL).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x7fL).get();
        v1 = MEMORY.ref(1, s1).offset(0x9L).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x7fL).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x84L).get();
        v1 = MEMORY.ref(1, s1).offset(0xeL).get();

        v0 = v0 + v1;
        MEMORY.ref(1, s0).offset(0x84L).setu(v0);
        v0 = MEMORY.ref(1, s1).offset(0xfL).get();
        v1 = MEMORY.ref(2, s0).offset(0x86L).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        MEMORY.ref(2, s0).offset(0x86L).setu(v1);
        v0 = MEMORY.ref(1, s1).offset(0x10L).get();
        v1 = MEMORY.ref(2, s0).offset(0x88L).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        MEMORY.ref(2, s0).offset(0x88L).setu(v1);
        v0 = MEMORY.ref(1, s1).offset(0x11L).get();
        v1 = MEMORY.ref(2, s0).offset(0x8aL).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        MEMORY.ref(2, s0).offset(0x8aL).setu(v1);
        v0 = MEMORY.ref(1, s1).offset(0x12L).get();
        v1 = MEMORY.ref(2, s0).offset(0x8cL).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        MEMORY.ref(2, s0).offset(0x8cL).setu(v1);
        v0 = MEMORY.ref(1, s1).offset(0x13L).get();
        v1 = MEMORY.ref(2, s0).offset(0x8eL).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        MEMORY.ref(2, s0).offset(0x8eL).setu(v1);
        v0 = MEMORY.ref(1, s1).offset(0x14L).get();
        v1 = MEMORY.ref(2, s0).offset(0x90L).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        MEMORY.ref(2, s0).offset(0x90L).setu(v1);
        v0 = MEMORY.ref(1, s1).offset(0x15L).get();
        v1 = MEMORY.ref(2, s0).offset(0x92L).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        MEMORY.ref(2, s0).offset(0x92L).setu(v1);
        v0 = MEMORY.ref(1, s1).offset(0x16L).get();
        v1 = MEMORY.ref(2, s0).offset(0x94L).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        MEMORY.ref(2, s0).offset(0x94L).setu(v1);
        v0 = MEMORY.ref(1, s1).offset(0x17L).get();
        v1 = MEMORY.ref(2, s0).offset(0x96L).get();
        v0 = v0 << 24;
        v0 = (int)v0 >> 24;
        v1 = v1 + v0;
        v0 = MEMORY.ref(1, s0).offset(0x98L).get();
        MEMORY.ref(2, s0).offset(0x96L).setu(v1);
        v1 = MEMORY.ref(1, s1).offset(0x18L).get();

        v0 = v0 + v1;
        MEMORY.ref(1, s0).offset(0x98L).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x99L).get();
        v1 = MEMORY.ref(1, s1).offset(0x19L).get();

        v0 = v0 + v1;
        MEMORY.ref(1, s0).offset(0x99L).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x9aL).get();
        v1 = MEMORY.ref(1, s1).offset(0x1aL).get();

        v0 = v0 + v1;
        MEMORY.ref(1, s0).offset(0x9aL).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x9bL).get();
        v1 = MEMORY.ref(1, s1).offset(0x1bL).get();

        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x9bL).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x80L).get();
        v1 = MEMORY.ref(1, s1).offset(0xaL).get();

        v0 = v0 + v1;
        MEMORY.ref(1, s0).offset(0x80L).setu(v0);
        v1 = MEMORY.ref(1, s1).offset(0xaL).get();
        v0 = MEMORY.ref(2, s0).offset(0x88L).get();
        a1 = 0;
        v0 = v0 + v1;
        MEMORY.ref(2, s0).offset(0x88L).setu(v0);
        v0 = MEMORY.ref(1, s0).offset(0x81L).get();
        v1 = MEMORY.ref(1, s1).offset(0xbL).get();
        a0 = 0x1L;
        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x81L).setu(v0);

        //LAB_80110b10
        do {
          v0 = MEMORY.ref(1, s1).offset(0xbL).get();

          v0 = v0 & a0;
          if(v0 == 0) {
            v0 = 0x8L;
          } else {
            v0 = 0x8L;
            if(a0 == v0) {
              //LAB_80110bcc
              v1 = MEMORY.ref(1, s1).offset(0xdL).get();
              v0 = MEMORY.ref(2, s0).offset(0x4eL).get();

              v0 = v0 + v1;
              MEMORY.ref(2, s0).offset(0x4eL).setu(v0);
            } else {
              if((int)a0 >= 0x9L) {
                v0 = 0x2L;

                //LAB_80110b64
                v0 = 0x20L;
                if(a0 == v0) {
                  //LAB_80110bac
                  MEMORY.ref(2, s0).offset(0x4aL).setu(s2);
                } else {
                  if((int)a0 >= 0x21L) {
                    v0 = 0x10L;

                    //LAB_80110b88
                    v0 = 0x40L;
                    if(a0 == v0) {
                      v0 = 0x80L;

                      //LAB_80110ba4
                      MEMORY.ref(2, s0).offset(0x48L).setu(s2);
                    } else {
                      v0 = 0x80L;
                      if(a0 == v0) {
                        MEMORY.ref(2, s0).offset(0x46L).setu(s2);
                      }
                    }
                  } else {
                    v0 = 0x10L;
                    if(a0 == v0) {
                      //LAB_80110bb4
                      v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                      v0 = MEMORY.ref(2, s0).offset(0x4cL).get();

                      v0 = v0 + v1;
                      MEMORY.ref(2, s0).offset(0x4cL).setu(v0);
                    }
                  }
                }
              } else {
                v0 = 0x2L;
                if(a0 == v0) {
                  //LAB_80110bfc
                  v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                  v0 = MEMORY.ref(2, s0).offset(0x52L).get();

                  v0 = v0 + v1;
                  MEMORY.ref(2, s0).offset(0x52L).setu(v0);
                } else {
                  if((int)a0 >= 0x3L) {
                    v0 = 0x4L;

                    //LAB_80110b54
                    if(a0 == v0) {
                      //LAB_80110be4
                      v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                      v0 = MEMORY.ref(2, s0).offset(0x50L).get();

                      v0 = v0 + v1;
                      MEMORY.ref(2, s0).offset(0x50L).setu(v0);
                    }
                  } else {
                    v0 = 0x4L;
                    if(a0 == s2) {
                      //LAB_80110c14
                      v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                      v0 = MEMORY.ref(2, s0).offset(0x54L).get();

                      v0 = v0 + v1;
                      MEMORY.ref(2, s0).offset(0x54L).setu(v0);
                    }
                  }
                }
              }
            }
          }

          //LAB_80110c28
          a0 = a0 << 1;

          //LAB_80110c2c
          a1 = a1 + 0x1L;
        } while((int)a1 < 0x8L);

        a1 = 0;
        v0 = MEMORY.ref(1, s0).offset(0x82L).get();
        v1 = MEMORY.ref(1, s1).offset(0xcL).get();
        a0 = 0x1L;
        v0 = v0 | v1;
        MEMORY.ref(1, s0).offset(0x82L).setu(v0);

        //LAB_80110c54
        do {
          v0 = MEMORY.ref(1, s1).offset(0xcL).get();

          v0 = v0 & a0;
          if(v0 == 0) {
            v0 = 0x8L;
          } else {
            v0 = 0x8L;
            if(a0 == v0) {
              //LAB_80110d40
              v1 = MEMORY.ref(1, s1).offset(0xdL).get();
              v0 = MEMORY.ref(2, s0).offset(0x5eL).get();

              v0 = v0 + v1;
              MEMORY.ref(2, s0).offset(0x5eL).setu(v0);
            } else {
              if((int)a0 >= 0x9L) {
                v0 = 0x2L;

                //LAB_80110ca8
                v0 = 0x20L;
                if(a0 == v0) {
                  //LAB_80110d10
                  v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                  v0 = MEMORY.ref(2, s0).offset(0x5aL).get();

                  v0 = v0 + v1;
                  MEMORY.ref(2, s0).offset(0x5aL).setu(v0);
                } else {
                  if((int)a0 >= 0x21L) {
                    v0 = 0x10L;

                    //LAB_80110ccc
                    v0 = 0x40L;
                    if(a0 == v0) {
                      v0 = 0x80L;

                      //LAB_80110cf8
                      v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                      v0 = MEMORY.ref(2, s0).offset(0x58L).get();

                      v0 = v0 + v1;
                      MEMORY.ref(2, s0).offset(0x58L).setu(v0);
                    } else {
                      v0 = 0x80L;
                      if(a0 == v0) {
                        v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                        v0 = MEMORY.ref(2, s0).offset(0x56L).get();

                        v0 = v0 + v1;
                        MEMORY.ref(2, s0).offset(0x56L).setu(v0);
                      }
                    }
                  } else {
                    v0 = 0x10L;
                    if(a0 == v0) {
                      //LAB_80110d28
                      v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                      v0 = MEMORY.ref(2, s0).offset(0x5cL).get();

                      v0 = v0 + v1;
                      MEMORY.ref(2, s0).offset(0x5cL).setu(v0);
                    }
                  }
                }
              } else {
                v0 = 0x2L;
                if(a0 == v0) {
                  //LAB_80110d60
                  v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                  v0 = MEMORY.ref(2, s0).offset(0x62L).get();

                  v0 = v0 + v1;
                  MEMORY.ref(2, s0).offset(0x62L).setu(v0);
                } else {
                  if((int)a0 >= 0x3L) {
                    v0 = 0x4L;

                    //LAB_80110c98
                    if(a0 == v0) {
                      //LAB_80110d58
                      MEMORY.ref(2, s0).offset(0x60L).setu(s2);
                    }
                  } else {
                    v0 = 0x4L;
                    if(a0 == s2) {
                      //LAB_80110d78
                      v1 = MEMORY.ref(1, s1).offset(0xdL).get();
                      v0 = MEMORY.ref(2, s0).offset(0x64L).get();

                      v0 = v0 + v1;
                      MEMORY.ref(2, s0).offset(0x64L).setu(v0);
                    }
                  }
                }
              }
            }
          }

          //LAB_80110d8c
          a0 = a0 << 1;

          //LAB_80110d90
          a1 = a1 + 0x1L;
        } while((int)a1 < 0x8L);
      }

      //LAB_80110da0
      s3 = s3 + 0x1L;
    } while((int)s3 < 0x5L);
  }
}
