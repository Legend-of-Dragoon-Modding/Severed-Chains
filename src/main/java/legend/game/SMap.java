package legend.game;

import legend.core.gpu.RECT;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.UnsignedIntRef;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SStrm.FUN_800fb7cc;
import static legend.game.SStrm.FUN_800fb90c;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80013200;
import static legend.game.Scus94491BpeSegment.FUN_800136dc;
import static legend.game.Scus94491BpeSegment.FUN_80015310;
import static legend.game.Scus94491BpeSegment.FUN_8001e29c;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c150;
import static legend.game.Scus94491BpeSegment_8003.ClearImage;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8004._8004dd24;
import static legend.game.Scus94491BpeSegment_8004.callbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8007._8007a398;
import static legend.game.Scus94491BpeSegment_8007._8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bf0b4;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b._800bf0dc;
import static legend.game.Scus94491BpeSegment_800b._800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.loadingStage_800bb10c;

public final class SMap {
  private SMap() { }

  public static final Value _800d1cb8 = MEMORY.ref(4, 0x800d1cb8L);

  public static final Value _800d1cc0 = MEMORY.ref(4, 0x800d1cc0L);

  public static final ArrayRef<UnsignedIntRef> textureDataPtrArray3_800d4ba0 = MEMORY.ref(4, 0x800d4ba0L, ArrayRef.of(UnsignedIntRef.class, 3, 4, UnsignedIntRef::new));

  public static final ArrayRef<RECT> rectArray3_800f96f4 = MEMORY.ref(8, 0x800f96f4L, ArrayRef.of(RECT.class, 3, 8, RECT::new));

  public static final Value _800f970c = MEMORY.ref(4, 0x800f970cL);

  public static final Value _800f9718 = MEMORY.ref(2, 0x800f9718L);

  @Method(0x800ed5b0L)
  public static void FUN_800ed5b0() {
    switch((int)loadingStage_800bb10c.get()) {
      case 0x0:
        _800d1cb8.setu(0);
        FUN_80015310(0, 0x1659L, 0, getMethodAddress(SMap.class, "FUN_800edc7c", Value.class, long.class, long.class), _800bf0dc.get(), 0x4L);

        //LAB_800ed644
        for(int s2 = 0; s2 < 3; s2++) {
          final RECT rect = rectArray3_800f96f4.get(s2);
          final long dest = addToLinkedListTail(rect.w.get() * rect.h.get() * 2);
          textureDataPtrArray3_800d4ba0.get(s2).set(dest);
          StoreImage(rectArray3_800f96f4.get(s2), dest);
        }

        SetDispMask(0);

        ClearImage(new RECT((short)0, (short)0, (short)0x3ff, (short)0x1ff), (byte)0, (byte)0, (byte)0);

        DrawSync(0);

        final long a1;
        if(_800f970c.offset(_800bf0dc.get() * 16).get() == 0) {
          //LAB_800ed6f8
          a1 = 0;
        } else {
          a1 = 0x4L;
        }

        //LAB_800ed700
        FUN_80013200(_800f9718.offset(_800bf0dc.get() * 16).get(), a1);

        loadingStage_800bb10c.setu(0x1L);
        break;

      case 0x1:
        if(_800d1cb8.get() != 0 && fileCount_8004ddc8.get() == 0) {
          loadingStage_800bb10c.setu(0x2L);
        }

        break;

      case 0x2:
        _8007a3b8.setu(0x4L);
        FUN_800ed8d0(_800bf0dc.get());

        _800bd808.setu(-0x1L);
        loadingStage_800bb10c.setu(0x3L);
        break;

      case 0x3:
        if(_800bf0d8.get() == 0x5L) {
          loadingStage_800bb10c.setu(0x4L);
        }

        break;

      case 0x4:
        FUN_8002c150(0);
        _8007a3b8.setu(0x2L);
        loadingStage_800bb10c.setu(0);
        _8004dd24.setu(_800bf0ec);
        break;
    }
  }

  @Method(0x800ed8d0L)
  public static void FUN_800ed8d0(final long a0) {
    if(_800bf0d8.get() != 0) {
      return;
    }

    _800bf0dc.setu(a0);

    if(_800bf0b4.get() == 0) {
      FUN_80012b1c(0x4L, getMethodAddress(SMap.class, "FUN_800edc50", long.class), 0);
    } else {
      //LAB_800ed91c
      _800bf0d8.setu(0x1L);
    }

    //LAB_800ed924
  }

  @Method(0x800ed960L)
  public static long FUN_800ed960() {
    if(_800bf0d8.get() != 0x1L || doubleBufferFrame_800bb108.get() == 0) {
      //LAB_800ed9d0
      return 0;
    }

    FUN_8001e29c(0x8L);
    FUN_800fb7cc(_800f970c.offset(_800bf0dc.get() * 16).getAddress(), _800bf0dc.get());

    _800bf0d8.setu(0x2L);
    callbackIndex_8004ddc4.set(0x16L);

    //LAB_800ed9d4
    return 0x1L;
  }

  @Method(0x800ed9e4L)
  public static long FUN_800ed9e4() {
    if(_800bf0d8.get() != 0x2) {
      return 0;
    }

    //LAB_800eda0c
    final long s0;
    if(_800bf0dc.get() == 0 && _8007a398.get(0x800L) != 0) {
      s0 = 0x1L;
    } else {
      s0 = 0;
    }

    //LAB_800eda38
    if(FUN_800fb90c() == 0 && s0 == 0) {
      return 0x1L;
    }

    //LAB_800eda50
    _800bf0d8.setu(0x3L);
    callbackIndex_8004ddc4.setu(0x17L);
    FUN_800136dc(0x1L, 0x1L);
    ClearImage(new RECT((short)0, (short)0, (short)1023, (short)511), (byte)0, (byte)0, (byte)0);
    DrawSync(0);
    FUN_80013200(640L, 0);

    //LAB_800edab4
    return 0x1L;
  }

  @Method(0x800edc50L)
  public static void FUN_800edc50(final long a0) {
    if(_800bf0d8.get() >= 0x3L) {
      return;
    }

    _800bf0b4.setu(0x1L);
    _800bf0d8.setu(0x1L);
  }

  @Method(0x800edc7cL)
  public static void FUN_800edc7c(final Value a0, long a1, long a2) {
    if(a0.offset(0x4L).get() < a2) {
      return;
    }

    final long v1 = a0.deref(4).offset(a2 * 8).getAddress();
    if(MEMORY.ref(4, v1).offset(0xcL).get() == 0) {
      return;
    }

    long a3 = _800d1cc0.getAddress();
    final long v0 = MEMORY.ref(4, v1).offset(0x8L).get();
    a2 = 0xbb7L;
    a1 = a0.deref(4).offset(v0).getAddress();

    //LAB_800edcbc
    do {
      MEMORY.ref(4, a3).setu(MEMORY.ref(4, a1));
      a1 += 0x4L;
      a3 += 0x4L;
      a2--;
    } while(a2 >= 0);

    removeFromLinkedList(a0.get());
    _800d1cb8.setu(0x1L);

    //LAB_800edcf0
  }
}
