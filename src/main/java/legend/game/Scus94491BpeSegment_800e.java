package legend.game;

import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80011e1c;
import static legend.game.Scus94491BpeSegment.FUN_80019500;
import static legend.game.Scus94491BpeSegment._1f8003c0;
import static legend.game.Scus94491BpeSegment._1f8003c4;
import static legend.game.Scus94491BpeSegment._1f8003c8;
import static legend.game.Scus94491BpeSegment._1f8003cc;
import static legend.game.Scus94491BpeSegment._1f8003fc;
import static legend.game.Scus94491BpeSegment._800103d0;
import static legend.game.Scus94491BpeSegment._8001051c;
import static legend.game.Scus94491BpeSegment._8011e210;
import static legend.game.Scus94491BpeSegment.allocateLinkedList;
import static legend.game.Scus94491BpeSegment.isStackPointerModified_1f8003bc;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.processControllerInput;
import static legend.game.Scus94491BpeSegment.timHeader_80010548;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c008;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002db2c;
import static legend.game.Scus94491BpeSegment_8002.SetMem;
import static legend.game.Scus94491BpeSegment_8002._bu_init;
import static legend.game.Scus94491BpeSegment_8002.initMemcard;
import static legend.game.Scus94491BpeSegment_8002.setCdMix;
import static legend.game.Scus94491BpeSegment_8003.ClearImage;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b3f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003bc30;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c5e0;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.ResetCallback;
import static legend.game.Scus94491BpeSegment_8003.ResetGraph;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.SetGraphDebug;
import static legend.game.Scus94491BpeSegment_8003.SetTmr0InterruptCallback;
import static legend.game.Scus94491BpeSegment_8003.VSync;
import static legend.game.Scus94491BpeSegment_8003.initGte;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.resetCdromStuff;
import static legend.game.Scus94491BpeSegment_8003.set80053498;
import static legend.game.Scus94491BpeSegment_8003.setCdDebug;
import static legend.game.Scus94491BpeSegment_8003.setClip;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.FUN_80042090;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004cdbc;
import static legend.game.Scus94491BpeSegment_8004._8004dd24;
import static legend.game.Scus94491BpeSegment_8004._8004e31c;
import static legend.game.Scus94491BpeSegment_8004._8004e41c;
import static legend.game.Scus94491BpeSegment_8004._8004e59c;
import static legend.game.Scus94491BpeSegment_8004._8004e61c;
import static legend.game.Scus94491BpeSegment_8004._8004e69c;
import static legend.game.Scus94491BpeSegment_8004._8004e71c;
import static legend.game.Scus94491BpeSegment_8004._8004e91c;
import static legend.game.Scus94491BpeSegment_8004._8004e990;
import static legend.game.Scus94491BpeSegment_8004._8004ea1c;
import static legend.game.Scus94491BpeSegment_8004._8004ea9c;
import static legend.game.Scus94491BpeSegment_8004._8004eb1c;
import static legend.game.Scus94491BpeSegment_8004._8004eb9c;
import static legend.game.Scus94491BpeSegment_8004._8004ec1c;
import static legend.game.Scus94491BpeSegment_8004._8004ec9c;
import static legend.game.Scus94491BpeSegment_8004._8004ed1c;
import static legend.game.Scus94491BpeSegment_8004._8004ed9c;
import static legend.game.Scus94491BpeSegment_8004._8004ee1c;
import static legend.game.Scus94491BpeSegment_8004._8004ee9c;
import static legend.game.Scus94491BpeSegment_8004._8004ef1c;
import static legend.game.Scus94491BpeSegment_8004._8004ef9c;
import static legend.game.Scus94491BpeSegment_8004._8004f01c;
import static legend.game.Scus94491BpeSegment_8004._8004f09c;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8005._8005a370;
import static legend.game.Scus94491BpeSegment_8005._8005a374;
import static legend.game.Scus94491BpeSegment_8005._8005a384;
import static legend.game.Scus94491BpeSegment_8005._8005a388;
import static legend.game.Scus94491BpeSegment_8005._8005a398;
import static legend.game.Scus94491BpeSegment_8006._8006a398;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007._8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb112;
import static legend.game.Scus94491BpeSegment_800b._800bb228;
import static legend.game.Scus94491BpeSegment_800b._800bb348;
import static legend.game.Scus94491BpeSegment_800b._800bc0c0;
import static legend.game.Scus94491BpeSegment_800b._800bc1c0;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bda10;
import static legend.game.Scus94491BpeSegment_800b._800bda84;
import static legend.game.Scus94491BpeSegment_800b._800bda86;
import static legend.game.Scus94491BpeSegment_800b._800bda88;
import static legend.game.Scus94491BpeSegment_800b._800bdaad;
import static legend.game.Scus94491BpeSegment_800b._800bdadc;
import static legend.game.Scus94491BpeSegment_800b._800bdb90;
import static legend.game.Scus94491BpeSegment_800b._800bdc24;
import static legend.game.Scus94491BpeSegment_800b._800bdc40;
import static legend.game.Scus94491BpeSegment_800b._800bdc5c;
import static legend.game.Scus94491BpeSegment_800b._800bf0b4;
import static legend.game.Scus94491BpeSegment_800b._800bf0cd;
import static legend.game.Scus94491BpeSegment_800b._800bf0ce;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b._800bf0d0;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b.array_800bb198;
import static legend.game.Scus94491BpeSegment_800b.loadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800c._800ca734;
import static legend.game.Scus94491BpeSegment_800c.timHeader_800c6748;

public final class Scus94491BpeSegment_800e {
  private Scus94491BpeSegment_800e() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_800e.class);

  private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];

  public static final Value ramSize_800e6f04 = MEMORY.ref(4, 0x800e6f04L);
  public static final Value stackSize_800e6f08 = MEMORY.ref(4, 0x800e6f08L);

  public static final Value _800e6f64 = MEMORY.ref(4, 0x800e6f64L);

  public static final Value _800e6fe4 = MEMORY.ref(4, 0x800e6fe4L);

  public static final Value _800e7040 = MEMORY.ref(4, 0x800e7040L);

  public static final Value _800e7048 = MEMORY.ref(4, 0x800e7048L);

  public static final Value _800e7094 = MEMORY.ref(4, 0x800e7094L);

  public static final Value _800e7114 = MEMORY.ref(4, 0x800e7114L);

  public static final Value _800e7150 = MEMORY.ref(4, 0x800e7150L);

  public static final Value _800e7170 = MEMORY.ref(4, 0x800e7170L);

  public static final Value _800e71f0 = MEMORY.ref(4, 0x800e71f0L);

  public static final Value _800e7270 = MEMORY.ref(4, 0x800e7270L);

  public static final Value _800e72f0 = MEMORY.ref(4, 0x800e72f0L);

  public static final Value _800e7370 = MEMORY.ref(4, 0x800e7370L);

  public static final ArrayRef<UnsignedIntRef> _800e73f0 = MEMORY.ref(0x80, 0x800e73f0L, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _800e7470 = MEMORY.ref(0x80, 0x800e7470L, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _800e74f0 = MEMORY.ref(0x80, 0x800e74f0L, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _800e7570 = MEMORY.ref(0x80, 0x800e7570L, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));

  public static final Value _800e75ac = MEMORY.ref(4, 0x800e75acL);

  public static final Value _800e75f8 = MEMORY.ref(4, 0x800e75f8L);

  public static final Value _800e7670 = MEMORY.ref(4, 0x800e7670L);

  public static final Value _800e76b0 = MEMORY.ref(4, 0x800e76b0L);

  @Method(0x800e5d44L)
  public static void main() {
    FUN_800e5d64();
    FUN_80011e1c();
  }

  @Method(0x800e5d64L)
  public static void FUN_800e5d64() {
    ResetCallback();
    SetMem(2);

    isStackPointerModified_1f8003bc.set(0);

    initMemcard(false);
    FUN_8002db2c();
    FUN_80042090();

    _bu_init();

    FUN_8002c008();
    VSync(0);
    SetDispMask(0);
    ResetGraph(0);
    SetGraphDebug(2);

    ClearImage(new RECT((short)0, (short)0, (short)640, (short)512), (byte)0, (byte)0, (byte)0);
    ClearImage(new RECT((short)640, (short)0, (short)384, (short)512), (byte)0, (byte)0, (byte)0);
    DrawSync(0);
    VSync(0);

    FUN_8003bc30((short)640, (short)480, 0b110101, true, false);
    setClip((short)0, (short)16, (short)0, (short)16);

    _8005a370.setu(0xeL);
    _8005a374.setu(_8005a398.getAddress());
    _8005a384.setu(0xeL);
    _8005a388.setu(_8006a398.getAddress());

    _1f8003c0.setu(0xeL);
    _1f8003c4.setu(0);
    _1f8003c8.setu(0x4000L);
    _1f8003cc.setu(0x3ffeL);

    FUN_8003c5e0();

    _8007a3a8.setu(0);
    _800bb104.setu(0);
    _800babc0.setu(0);

    initGte();
    setProjectionPlaneDistance(640);
    resetCdromStuff();
    set80053498(0x1L);
    FUN_80019500();
    setCdDebug(3); // I think 3 is the most detailed logging

    _8004dd24.setu(0);
    loadingStage_800bb10c.setu(0);
    _8007a3b8.setu(0x2L);
    _800bb0fc.setu(0);

    processControllerInput();
    FUN_800e60d8();
    loadSystemFont();
    FUN_800e6654();
    allocateLinkedList(_8011e210.getAddress(), 0xdedf0L);
    loadOvalBlobTexture();
    FUN_800e6dd4();
    FUN_800e6e3c();
    FUN_800e67ac();
    FUN_800e6888();
    FUN_800e6d60();
    FUN_800e670c();
    FUN_800e6ecc();
    FUN_800e6774();
    FUN_800e6e6c();
    SetTmr0InterruptCallback(getMethodAddress(Scus94491BpeSegment.class, "spuTimerInterruptCallback"));
  }

  @Method(0x800e60d8L)
  public static void FUN_800e60d8() {
    for(int s2 = 0; s2 < 3; s2++) {
      for(int s1 = 0; s1 < 5; s1++) {
        _800bb110.offset(s2 * 16L).offset(s1 * 4L).setu(FUN_8003b3f0(s2, s1, 0, 0));
        _800bb112.offset(s2 * 16L).offset(s1 * 4L).setu(FUN_8003b3f0(s2, s1, 0, 0x100L));
      }
    }
  }

  @Method(0x800e6524L)
  private static void loadSystemFont() {
    final TimHeader header = parseTimHeader(timHeader_800c6748);

    final RECT imageRect = new RECT((short)832, (short)424, (short)64, (short)56);
    LoadImage(imageRect, header.getImageAddress());

    _800bb348.setu(_800bb112).oru(0xdL);

    if(header.hasClut()) {
      final RECT clutRect = new RECT((short)832, (short)422, (short)32, (short)1);
      LoadImage(clutRect, header.getClutAddress());
    }

    //LAB_800e65c4
    DrawSync(0);
    _1f8003fc.setu(_800bb228.getAddress());

    //LAB_800e65e8
    for(int i = 2; i < 37; i++) {
      long v1 = 0xffff_ffffL;
      long a1 = 0x1L;

      //LAB_800e65fc
      while(v1 >= i) {
        a1 *= i;
        v1 /= i;
      }

      //LAB_800e6620
      array_800bb198.get(i - 2).set(a1);
    }
  }

  @Method(0x800e6654L)
  public static void FUN_800e6654() {
    //LAB_800e666c
    for(int i = 0; i < 0x48; i++) {
      _800bc1c0.offset(i * 4).setu(_800bc0c0.getAddress());
    }
  }

  @Method(0x800e670cL)
  public static void FUN_800e670c() {
    //LAB_800e6720
    for(int i = 0; i < 32; i++) {
      _8004ee1c.offset(i * 4).setu(_800e6f64.offset(i * 4));
    }

    //LAB_800e6750
    for(int i = 0; i < 23; i++) {
      _8004ef9c.offset(i * 4).setu(_800e6fe4.offset(i * 4));
    }
  }

  @Method(0x800e6774)
  public static void FUN_800e6774() {
    //LAB_800e6788
    for(int i = 0; i < 2; i++) {
      _8004f09c.offset(i * 4).setu(_800e7040.offset(i * 4));
    }
  }

  @Method(0x800e67acL)
  private static void FUN_800e67ac() {
    //LAB_800e67c0
    for(int i = 0; i < 19; i++) {
      _8004e31c.offset(i * 4).setu(_800e7048.offset(i * 4));
    }

    //LAB_800e67f0
    for(int i = 0; i < 32; i++) {
      _8004e61c.offset(i * 4).setu(_800e7094.offset(i * 4));
    }

    //LAB_800e6820
    for(int i = 0; i < 15; i++) {
      _8004ed9c.offset(i * 4).setu(_800e7114.offset(i * 4));
    }

    //LAB_800e683c
    //LAB_800e6850
    for(int i = 0; i < 8; i++) {
      _8004ef1c.offset(i * 4).setu(_800e7150.offset(i * 4));
    }
  }

  @Method(0x800e6874L)
  public static void FUN_800e6874() {
    _8004e990.setu(_800ca734.getAddress());
  }

  @Method(0x800e6888L)
  public static void FUN_800e6888() {
    //LAB_800e68a4
    for(int i = 0; i < 32; i++) {
      _8004e91c.offset(i * 4).setu(_800e7170.offset(i * 4));
    }

    //LAB_800e68d4
    for(int i = 0; i < 32; i++) {
      _8004eb1c.offset(i * 4).setu(_800e71f0.offset(i * 4));
    }

    //LAB_800e6904
    for(int i = 0; i < 32; i++) {
      _8004eb9c.offset(i * 4).setu(_800e7270.offset(i * 4));
    }

    //LAB_800e6934
    for(int i = 0; i < 32; i++) {
      _8004ec1c.offset(i * 4).setu(_800e72f0.offset(i * 4));
    }

    //LAB_800e6964
    for(int i = 0; i < 32; i++) {
      _8004ec9c.offset(i * 4).setu(_800e7370.offset(i * 4));
    }

    FUN_800e6874();
  }

  @Method(0x800e6998L)
  public static void loadOvalBlobTexture() {
    //LAB_800e69b8
    for(int i = 0; i < 32; i++) {
      _8004e41c.get(i).set(_800e73f0.get(i));
    }

    //LAB_800e69e8
    for(int i = 0; i < 32; i++) {
      _8004e71c.get(i).set(_800e7470.get(i));
    }

    //LAB_800e6a18
    for(int i = 0; i < 14; i++) {
      _8004ea9c.get(i).set(_800e7570.get(i));
    }

    //LAB_800e6a48
    for(int i = 0; i < 32; i++) {
      _8004ed1c.get(i).set(_800e74f0.get(i));
    }

    _800bd808.setu(0);

    final TimHeader header = parseTimHeader(timHeader_80010548);
    LoadImage(header.getImageRect(), header.getImageAddress());

    if(header.hasClut()) {
      LoadImage(header.getClutRect(), header.getClutAddress());
    }

    //LAB_800e6af0
    DrawSync(0);

    FUN_800e6b3c(_800bda10.getAddress(), _800103d0.getAddress(), _8001051c.getAddress());

    _800bda84.setu(0);
    _800bda86.setu(0);
    _800bda88.setu(0);
    _800bdadc.setu(0);
    _800bdaad.setu(0);
  }

  @Method(0x800e6b3cL)
  public static void FUN_800e6b3c(long a0, long a1, long a2) {
    assert false : "Unimplemented";
    //TODO
  }

  @Method(0x800e6d60L)
  public static void FUN_800e6d60() {
    _800bdb90.setu(0);
    _800bdc24.setu(0);
    _800bdc40.setu(0);
    _800bdc5c.setu(0);
    FUN_800e6d9c();
  }

  @Method(0x800e6d9cL)
  public static void FUN_800e6d9c() {
    for(int i = 0; i < 3; i++) {
      _8004f01c.offset(i * 4).setu(_800e75ac.offset(i * 4));
    }
  }

  @Method(0x800e6dd4L)
  public static void FUN_800e6dd4() {
    //LAB_800e6de8
    for(int i = 0; i < 16; i++) {
      _8004e59c.get(i).set(_800e7570.get(14 + i));
    }

    //LAB_800e6e18
    for(int i = 0; i < 30; i++) {
      _8004ea1c.get(i).set(_800e75f8.offset(i * 4).get());
    }
  }

  @Method(0x800e6e3cL)
  public static void FUN_800e6e3c() {
    memcpy(_8004e69c.getAddress(), _800e7670.getAddress(), 0x40);
  }

  @Method(0x800e6e6cL)
  public static void FUN_800e6e6c() {
    FUN_8004cdbc(0x1L, 0x1L);
    setCdVolume(0x7fL, 0x7fL);
    setCdMix(0x3fL);

    _800bf0b4.setu(0);
    _800bf0cd.setu(0);
    _800bf0ce.setu(0x7fL);
    _800bf0cf.setu(0);
    _800bf0d0.setu(0);
    _800bf0d8.setu(0);
  }

  @Method(0x800e6eccL)
  public static void FUN_800e6ecc() {
    //LAB_800e6ee0
    for(int i = 0; i < 0x17; i++) {
      _8004ee9c.offset(i * 4).setu(_800e76b0.offset(i * 4));
    }
  }
}
