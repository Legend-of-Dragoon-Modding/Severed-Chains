package legend.game;

import legend.core.DebugHelper;
import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.CdlLOC;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.SupplierRef;

import java.util.function.Function;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80012444;
import static legend.game.Scus94491BpeSegment.FUN_80013434;
import static legend.game.Scus94491BpeSegment.FUN_800194dc;
import static legend.game.Scus94491BpeSegment._80010250;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020460;
import static legend.game.Scus94491BpeSegment_8002.FUN_80024654;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002ac24;
import static legend.game.Scus94491BpeSegment_8002.strcmp;
import static legend.game.Scus94491BpeSegment_8003.DsSearchFile;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003429c;
import static legend.game.Scus94491BpeSegment_8003.FUN_80036f20;
import static legend.game.Scus94491BpeSegment_8003.handleCdromDmaTimeout;
import static legend.game.Scus94491BpeSegment_8004._8004db58;
import static legend.game.Scus94491BpeSegment_8004._8004db88;
import static legend.game.Scus94491BpeSegment_8004._8004dd80;
import static legend.game.Scus94491BpeSegment_8004._8004dd88;
import static legend.game.Scus94491BpeSegment_8004._8004dda0;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8005._80052c74;
import static legend.game.Scus94491BpeSegment_8005._80052c94;
import static legend.game.Scus94491BpeSegment_8005._80052d7c;
import static legend.game.Scus94491BpeSegment_8005._80052db0;
import static legend.game.Scus94491BpeSegment_800b.CdlFILE_800bb4c8;
import static legend.game.Scus94491BpeSegment_800b.SInitBinLoaded_800bbad0;
import static legend.game.Scus94491BpeSegment_800b._800bbac8;
import static legend.game.Scus94491BpeSegment_800b._800bc060;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.fileLoadingInfoArray_800bbad8;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bbacc;

public final class SInit {
  private SInit() { }

  public static final Value _800fb7d4 = MEMORY.ref(1, 0x800fb7d4L);

  public static final Value SInitLoadingStage_800fd318 = MEMORY.ref(4, 0x800fd318L);
  public static final ArrayRef<Pointer<SupplierRef<Long>>> SInitLoadingStageCallbackArray_800fd31c = (ArrayRef<Pointer<SupplierRef<Long>>>)MEMORY.ref(4, 0x800fd31cL, ArrayRef.of(Pointer.class, 6, 4, (Function)Pointer.of(4, SupplierRef::new)));

  public static final Value _800fd334 = MEMORY.ref(2, 0x800fd334L);

  public static final Value _800fd344 = MEMORY.ref(2, 0x800fd344L);

  public static final Value _800fd34c = MEMORY.ref(2, 0x800fd34cL);

  public static final Value _800fd3e8 = MEMORY.ref(4, 0x800fd3e8L);

  public static final Value _800fd404 = MEMORY.ref(4, 0x800fd404L);
  public static final Value _800fd408 = MEMORY.ref(4, 0x800fd408L);

  /** Array of 40 things */
  public static final Value _800fd414 = MEMORY.ref(4, 0x800fd414L);

  public static final Value filesCount_800fd514 = MEMORY.ref(4, 0x800fd514L);
  public static final Value linkedListEntry_800fd518 = MEMORY.ref(4, 0x800fd518L);
  public static final Value _800fd51c = MEMORY.ref(4, 0x800fd51cL);

  public static final Value _800fd524 = MEMORY.ref(4, 0x800fd524L);

  /**
   * @return 0 means not done yet; non-zero means finished in some way (different values may indicate success/failure?)
   */
  @Method(0x800fb8d8L)
  public static long executeSInitLoadingStage(final long a0) {
    _800fd3e8.setu(a0);
    final long v1 = SInitLoadingStageCallbackArray_800fd31c.get((int)SInitLoadingStage_800fd318.get()).deref().run();

    if(v1 == 0x0L) {
      return 0;
    }

    if(v1 == 0x1L) {
      //LAB_800fb950
      SInitLoadingStage_800fd318.addu(0x1L);
      _800fd404.setu(0);
      return 0;
    }

    //LAB_800fb938
    if(v1 == 0x2L) {
      //LAB_800fb96c
      SInitLoadingStage_800fd318.setu(0);
      _800fd404.setu(0);
      return 0x1L;
    }

    if(v1 == 0x3L) {
      //LAB_800fb980
      if(linkedListEntry_800fd518.get() != 0) {
        removeFromLinkedList(linkedListEntry_800fd518.get());
        linkedListEntry_800fd518.setu(0);
      }

      //LAB_800fb99c
      SInitLoadingStage_800fd318.setu(0);
      _800fd404.setu(0);
      return -0x1L;
    }

    //LAB_800fb9ac
    return 0x3L;
  }

  @Method(0x800fb9c0L)
  public static long FUN_800fb9c0() {
    SInitBinLoaded_800bbad0.set(false);

    if(_800bbac8.get() == 0) {
      //LAB_800fba00
      //LAB_800fba14
      for(int i = 0; i < 44L; i++) {
        fileLoadingInfoArray_800bbad8.get(i).used.set(false);
      }

      fileCount_8004ddc8.setu(0);
    } else if(linkedListEntry_800bbacc.get() != 0) {
      removeFromLinkedList(linkedListEntry_800bbacc.get());
      linkedListEntry_800bbacc.setu(0);
    }

    //LAB_800fba30
    _800fd51c.setu(0);
    linkedListEntry_800fd518.setu(addToLinkedListHead(0x7_0000L));
    _8004dd88.set(String.format("\\SECT\\DRGN2%d.BIN", drgnBinIndex_800bc058.get()));

    return 0x1L;
  }

  @Method(0x800fba80L)
  public static long FUN_800fba80() {
    long s3 = 0;

    //LAB_800fbac0
    final long count = filesCount_800fd514.get();
    for(int s1 = 0; s1 < count; s1++) {
      //TODO dunno if these params are right
      final String name = String.format("%s%s;1", _800fb7d4.getString(), _800fd414.offset(s1 * 4L).deref(4).offset(0x4L).deref(1).getString());

      //LAB_800fbae0
      while(FUN_80036f20() != 0x1L) {
        DebugHelper.sleep(1);
      }

      final CdlFILE file = CdlFILE_800bb4c8.get(s1);
      if(DsSearchFile(file, name) == null) {
        //LAB_800fbb14
        if(_800fd414.offset(s1 * 4L).deref(2).getSigned() < 0) {
          s3 = 0x1L;
        }
      } else {
        _800fd414.offset(s1 * 4L).deref(2).setu(s1);
      }

      //LAB_800fbb30
    }

    //LAB_800fbb4c
    if(s3 == 0) {
      return 0x1L;
    }

    _800fd404.addu(0x1L);

    if(_800fd404.get() > _800fd408.get()) {
      return 0x3L;
    }

    return 0;
  }

  @Method(0x800fbb9cL)
  public static long FUN_800fbb9c() {
    if(_800fd404.get() >= _80010250.get()) {
      return 0x1L;
    }

    //LAB_800fbbc8
    final long a0 = _8004dda0.offset(_800fd404.get() * 8).getSigned();

    if(a0 < 0) {
      _800fd524.offset(_800fd404.get() * 4).setu(-0x1L);

      //LAB_800fbc9c
      _800fd404.addu(0x1L);
      return 0;
    }

    final long s0 = linkedListEntry_800fd518.get() + _800fd51c.get();
    final CdlLOC cdPos = CdlFILE_800bb4c8.get((int)a0).pos;
    CDROM.readFromDisk(cdPos, 0x50, s0);
    FUN_8003429c(0);
    handleCdromDmaTimeout(1);

    final long a2 = MEMORY.ref(4, s0).offset(0x4L).get();
    _800fd524.offset(_800fd404.get() * 4).setu(_800fd51c);
    _800fd51c.addu(0x8L + ((a2 << 0x6L & 0xffffffffL) >>> 0x3L));

    //LAB_800fbc9c
    _800fd404.addu(0x1L);

    // Pre-optimisation
//    //LAB_800fbc04
//    final long s0 = _800fd518.get() + _800fd51c.get();
//    if(startCdromDmaTransfer(new CdlLOC(_800bb4c8.offset(a0 * 24)), 0x50L, s0, new CdlMODE().doubleSpeed()) == 0) {
//      return 0;
//    }
//
//    //LAB_800fbc44
//    long remainingTransfers;
//    do { //TODO this is looping infinitely --------------------------------------------------------------
//      remainingTransfers = FUN_80035a30(0x9100_0000L); //TODO this was putting something on the stack, just a temp dumping ground by the looks of it
//    } while(remainingTransfers > 0);
//
//    if(remainingTransfers == 0) {
//      final long a2 = MEMORY.ref(4, s0).offset(0x4L).get();
//      final long v1 = _800fd51c.get();
//      _800fd524.offset(_800fd404.get() * 4).setu(v1);
//      _800fd51c.setu(v1 + 0x8L + (a2 << 0x6L >>> 0x3L));
//
//      //LAB_800fbc9c
//      _800fd404.addu(0x1L);
//    }

    //LAB_800fbcb0
    return 0;
  }

  @Method(0x800fbcc0L)
  public static long FUN_800fbcc0() {
    final long a1 = _800fd51c.get();
    if(a1 == 0) {
      removeFromLinkedList(linkedListEntry_800fd518.get());

      linkedListEntry_800fd518.setu(0);
      linkedListEntry_800bbacc.setu(0);
    } else {
      //LAB_800fbcfc
      long address = FUN_80012444(linkedListEntry_800fd518.get(), a1);
      if(address == 0) {
        address = linkedListEntry_800fd518.get();
      }

      //LAB_800fbd20
      linkedListEntry_800bbacc.setu(address);

      //LAB_800fbd50
      for(long a0 = 0; a0 < _80010250.get(); a0++) {
        final long v0 = _800fd524.offset(a0 * 4).getSigned();

        if(v0 >= 0) {
          //LAB_800fbd68
          _800bc060.offset(a0 * 4).setu(v0 + address);
        } else {
          _800bc060.offset(a0 * 4).setu(0);
        }
      }
    }

    //LAB_800fbd80
    SInitBinLoaded_800bbad0.set(true);
    _800bbac8.setu(0x1L);

    return 0x2L;
  }

  @Method(0x800fbda8L)
  public static long FUN_800fbda8() {
    return 1;
  }

  @Method(0x800fbdb0L)
  public static long FUN_800fbdb0() {
    filesCount_800fd514.setu(0);
    FUN_800fbe28();
    FUN_80013434(_800fd414.getAddress(), filesCount_800fd514.get(), 0x4L, getMethodAddress(SInit.class, "compareFileNames", long.class, long.class));
    return 1;
  }

  @Method(0x800fbdf8L)
  public static long compareFileNames(final long pointerToFileSomething1, final long pointerToFileSomething2) {
    return strcmp(MEMORY.ref(4, pointerToFileSomething1).deref(4).offset(0x4L).deref(1).getString(), MEMORY.ref(4, pointerToFileSomething2).deref(4).offset(0x4L).deref(1).getString());
  }

  @Method(0x800fbe28L)
  public static void FUN_800fbe28() {
    FUN_800fbe80();
    FUN_800fbf30();
    FUN_8002ac24();
    FUN_800194dc();
    FUN_80020460();
    FUN_80024654();
    FUN_800fbfd4();
    FUN_800fbf6c();
  }

  @Method(0x800fbe80L)
  public static void FUN_800fbe80() {
    FUN_800fbec8(_8004db58.getAddress());
    FUN_800fbec8(_8004db88.getAddress());
    FUN_800fbec8(_8004dd80.getAddress());
    FUN_800fbec8(_8004dda0.getAddress());
  }

  @Method(0x800fbec8L)
  public static void FUN_800fbec8(long a0) {
    if(SInitBinLoaded_800bbad0.get()) {
      return;
    }

    while(MEMORY.ref(4, a0).offset(0x4L).get() != 0) {
      MEMORY.ref(2, a0).setu(-0x1L);
      _800fd414.offset(filesCount_800fd514.get() * 4).setu(a0);
      filesCount_800fd514.addu(0x1L);
      a0 += 0x8L;
    }
  }

  @Method(0x800fbf30L)
  public static void FUN_800fbf30() {
    FUN_800fbec8(_800fd334.getAddress());
    FUN_800fbec8(_800fd344.getAddress());
    FUN_800fbec8(_800fd34c.getAddress());
  }

  @Method(0x800fbf6cL)
  public static void FUN_800fbf6c() {
    FUN_800fbec8((drgnBinIndex_800bc058.get() == 0x4L ? _80052c94 : _80052c74).getAddress());
    FUN_800fbec8(_80052d7c.offset(drgnBinIndex_800bc058.get() * 4L).get());
  }

  @Method(0x800fbfd4L)
  public static void FUN_800fbfd4() {
    FUN_800fbec8(_80052db0.getAddress());
  }
}
