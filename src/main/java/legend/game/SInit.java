package legend.game;

import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.CdlLOC;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.SupplierRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.types.FileEntry08;
import legend.game.types.MrgFile;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getBiFunctionAddress;
import static legend.game.Scus94491BpeSegment.realloc;
import static legend.game.Scus94491BpeSegment.FUN_800194dc;
import static legend.game.Scus94491BpeSegment._80010250;
import static legend.game.Scus94491BpeSegment.mallocHead;
import static legend.game.Scus94491BpeSegment.qsort;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020460;
import static legend.game.Scus94491BpeSegment_8002.FUN_80024654;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002ac24;
import static legend.game.Scus94491BpeSegment_8002.strcmp;
import static legend.game.Scus94491BpeSegment_8003.DsSearchFile;
import static legend.game.Scus94491BpeSegment_8004._8004dd80;
import static legend.game.Scus94491BpeSegment_8004._8004dd88;
import static legend.game.Scus94491BpeSegment_8004.drgnFiles_8004dda0;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.sInitOvl_8004db88;
import static legend.game.Scus94491BpeSegment_8004.ttleOvl_8004db58;
import static legend.game.Scus94491BpeSegment_8005._80052db0;
import static legend.game.Scus94491BpeSegment_8005.diskFmvs_80052d7c;
import static legend.game.Scus94491BpeSegment_8005.lodXa00Xa_80052c74;
import static legend.game.Scus94491BpeSegment_8005.lodXa00Xa_80052c94;
import static legend.game.Scus94491BpeSegment_800b.CdlFILE_800bb4c8;
import static legend.game.Scus94491BpeSegment_800b.SInitBinLoaded_800bbad0;
import static legend.game.Scus94491BpeSegment_800b._800bbac8;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.drgnMrg_800bc060;
import static legend.game.Scus94491BpeSegment_800b.fileLoadingInfoArray_800bbad8;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bbacc;

public final class SInit {
  private SInit() { }

  public static final Value _800fb7d4 = MEMORY.ref(1, 0x800fb7d4L);

  public static final Value SInitLoadingStage_800fd318 = MEMORY.ref(4, 0x800fd318L);
  /**
   * <ol start="0">
   *   <li>{@link SInit#FUN_800fb9c0()}</li>
   *   <li>{@link SInit#FUN_800fbda8()}</li>
   *   <li>{@link SInit#FUN_800fbdb0()}</li>
   *   <li>{@link SInit#FUN_800fba80()}</li>
   *   <li>{@link SInit#FUN_800fbb9c()}</li>
   *   <li>{@link SInit#FUN_800fbcc0()}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<SupplierRef<Long>>> SInitLoadingStageCallbackArray_800fd31c = MEMORY.ref(4, 0x800fd31cL, ArrayRef.of(Pointer.classFor(SupplierRef.classFor(Long.class)), 6, 4, Pointer.deferred(4, SupplierRef::new)));

  public static final FileEntry08 mesMvb_800fd334 = MEMORY.ref(2, 0x800fd334L, FileEntry08::new);

  public static final FileEntry08 _800fd344 = MEMORY.ref(2, 0x800fd344L, FileEntry08::new);

  public static final FileEntry08 _800fd34c = MEMORY.ref(2, 0x800fd34cL, FileEntry08::new);

  public static final Value _800fd3e8 = MEMORY.ref(4, 0x800fd3e8L);

  public static final Value _800fd404 = MEMORY.ref(4, 0x800fd404L);
  public static final Value _800fd408 = MEMORY.ref(4, 0x800fd408L);

  public static final ArrayRef<Pointer<FileEntry08>> fileEntries_800fd414 = MEMORY.ref(4, 0x800fd414L, ArrayRef.of(Pointer.classFor(FileEntry08.class), 0x40, 0x4, Pointer.deferred(4, FileEntry08::new)));

  public static final IntRef filesCount_800fd514 = MEMORY.ref(4, 0x800fd514L, IntRef::new);
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
        free(linkedListEntry_800fd518.get());
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
      for(int i = 0; i < 44; i++) {
        fileLoadingInfoArray_800bbad8.get(i).used.set(false);
      }

      fileCount_8004ddc8.set(0);
    } else if(linkedListEntry_800bbacc.get() != 0) {
      free(linkedListEntry_800bbacc.get());
      linkedListEntry_800bbacc.setu(0);
    }

    //LAB_800fba30
    _800fd51c.setu(0);
    linkedListEntry_800fd518.setu(mallocHead(0x7_0000L));
    _8004dd88.set(String.format("\\SECT\\DRGN2%d.BIN", drgnBinIndex_800bc058.get()));

    return 0x1L;
  }

  @Method(0x800fba80L)
  public static long FUN_800fba80() {
    long s3 = 0;

    //LAB_800fbac0
    final long count = filesCount_800fd514.get();
    for(int fileIndex = 0; fileIndex < count; fileIndex++) {
      final String name = String.format("%s%s;1", _800fb7d4.getString(), fileEntries_800fd414.get(fileIndex).deref().name_04.deref().get());

      //LAB_800fbae0
      final CdlFILE file = CdlFILE_800bb4c8.get(fileIndex);
      if(DsSearchFile(file, name) == null) {
        //LAB_800fbb14
        if(fileEntries_800fd414.get(fileIndex).deref().fileIndex_00.get() < 0) {
          s3 = 0x1L;
        }
      } else {
        fileEntries_800fd414.get(fileIndex).deref().fileIndex_00.set((short)fileIndex);
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
    final int fileIndex = drgnFiles_8004dda0.get((int)_800fd404.get()).fileIndex_00.get();

    if(fileIndex < 0) {
      _800fd524.offset(_800fd404.get() * 4).setu(-0x1L);

      //LAB_800fbc9c
      _800fd404.addu(0x1L);
      return 0;
    }

    final long s0 = linkedListEntry_800fd518.get() + _800fd51c.get();
    final CdlLOC cdPos = CdlFILE_800bb4c8.get(fileIndex).pos;
    CDROM.readFromDisk(cdPos, 0x50, s0);

    final long a2 = MEMORY.ref(4, s0).offset(0x4L).get();
    _800fd524.offset(_800fd404.get() * 4).setu(_800fd51c);
    _800fd51c.addu(8 + (a2 << 6 >>> 3));

    //LAB_800fbc9c
    _800fd404.addu(0x1L);

    //LAB_800fbcb0
    return 0;
  }

  @Method(0x800fbcc0L)
  public static long FUN_800fbcc0() {
    final int a1 = (int)_800fd51c.get();
    if(a1 == 0) {
      free(linkedListEntry_800fd518.get());

      linkedListEntry_800fd518.setu(0);
      linkedListEntry_800bbacc.setu(0);
    } else {
      //LAB_800fbcfc
      long address = realloc(linkedListEntry_800fd518.get(), a1);
      if(address == 0) {
        address = linkedListEntry_800fd518.get();
      }

      //LAB_800fbd20
      linkedListEntry_800bbacc.setu(address);

      //LAB_800fbd50
      for(int i = 0; i < _80010250.get(); i++) {
        final long v0 = _800fd524.offset(i * 0x4L).getSigned();

        if(v0 >= 0) {
          //LAB_800fbd68
          drgnMrg_800bc060.get(i).set(MEMORY.ref(4, address + v0, MrgFile::new));
        } else {
          drgnMrg_800bc060.get(i).clear();
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
    filesCount_800fd514.set(0);
    FUN_800fbe28();
    qsort(fileEntries_800fd414, filesCount_800fd514.get(), 0x4, getBiFunctionAddress(SInit.class, "compareFileNames", Pointer.classFor(FileEntry08.class), Pointer.classFor(FileEntry08.class), long.class));
    return 1;
  }

  @Method(0x800fbdf8L)
  public static long compareFileNames(final Pointer<FileEntry08> entry1, final Pointer<FileEntry08> entry2) {
    return strcmp(entry1.deref().name_04.deref().get(), entry2.deref().name_04.deref().get());
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
    initFileEntries(ttleOvl_8004db58.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
    initFileEntries(sInitOvl_8004db88.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
    initFileEntries(_8004dd80.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
    initFileEntries(drgnFiles_8004dda0);
  }

  @Method(0x800fbec8L)
  public static void initFileEntries(final UnboundedArrayRef<FileEntry08> entries) {
    if(SInitBinLoaded_800bbad0.get()) {
      return;
    }

    for(int i = 0; !entries.get(i).name_04.isNull(); i++) {
      entries.get(i).fileIndex_00.set((short)-1);
      fileEntries_800fd414.get(filesCount_800fd514.get()).set(entries.get(i));
      filesCount_800fd514.incr();
    }
  }

  @Method(0x800fbf30L)
  public static void FUN_800fbf30() {
    initFileEntries(mesMvb_800fd334.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
    initFileEntries(_800fd344.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
    initFileEntries(_800fd34c.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
  }

  @Method(0x800fbf6cL)
  public static void FUN_800fbf6c() {
    initFileEntries((drgnBinIndex_800bc058.get() != 4 ? lodXa00Xa_80052c74 : lodXa00Xa_80052c94).reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
    initFileEntries(diskFmvs_80052d7c.get(drgnBinIndex_800bc058.get()).deref());
  }

  @Method(0x800fbfd4L)
  public static void FUN_800fbfd4() {
    initFileEntries(_80052db0.reinterpret(UnboundedArrayRef.of(0x8, FileEntry08::new)));
  }
}
