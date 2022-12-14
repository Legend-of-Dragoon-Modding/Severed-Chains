package legend.game;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.types.FileEntry08;
import legend.game.types.MrgFile;
import legend.game.unpacker.Unpacker;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getBiFunctionAddress;
import static legend.game.Scus94491BpeSegment.FUN_800194dc;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.mallocHead;
import static legend.game.Scus94491BpeSegment.qsort;
import static legend.game.Scus94491BpeSegment.realloc2;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020460;
import static legend.game.Scus94491BpeSegment_8002.FUN_80024654;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002ac24;
import static legend.game.Scus94491BpeSegment_8002.strcmp;
import static legend.game.Scus94491BpeSegment_8004._8004dd80;
import static legend.game.Scus94491BpeSegment_8004.drgn2xFileName_8004dd88;
import static legend.game.Scus94491BpeSegment_8004.drgnFiles_8004dda0;
import static legend.game.Scus94491BpeSegment_8004.sInitOvl_8004db88;
import static legend.game.Scus94491BpeSegment_8004.ttleOvl_8004db58;
import static legend.game.Scus94491BpeSegment_8005._80052db0;
import static legend.game.Scus94491BpeSegment_8005.diskFmvs_80052d7c;
import static legend.game.Scus94491BpeSegment_8005.lodXa00Xa_80052c74;
import static legend.game.Scus94491BpeSegment_8005.lodXa00Xa_80052c94;
import static legend.game.Scus94491BpeSegment_800b.DRGN_CACHE;
import static legend.game.Scus94491BpeSegment_800b.SInitBinLoaded_800bbad0;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.drgnFileCache_800bbacc;
import static legend.game.Scus94491BpeSegment_800b.drgnFilesCached_800bbac8;
import static legend.game.Scus94491BpeSegment_800b.drgnMrg_800bc060;

public final class SInit {
  private SInit() { }

  public static final FileEntry08 mesMvb_800fd334 = MEMORY.ref(2, 0x800fd334L, FileEntry08::new);

  public static final FileEntry08 _800fd344 = MEMORY.ref(2, 0x800fd344L, FileEntry08::new);

  public static final FileEntry08 _800fd34c = MEMORY.ref(2, 0x800fd34cL, FileEntry08::new);

  public static final ArrayRef<Pointer<FileEntry08>> fileEntries_800fd414 = MEMORY.ref(4, 0x800fd414L, ArrayRef.of(Pointer.classFor(FileEntry08.class), 0x40, 0x4, Pointer.deferred(4, FileEntry08::new)));

  public static final IntRef filesCount_800fd514 = MEMORY.ref(4, 0x800fd514L, IntRef::new);
  public static final Value drgnFileCache_800fd518 = MEMORY.ref(4, 0x800fd518L);
  public static final IntRef drgnFileCacheOffset_800fd51c = MEMORY.ref(4, 0x800fd51cL, IntRef::new);

  /**
   * @return 0 means not done yet; non-zero means finished in some way (different values may indicate success/failure?)
   */
  @Method(0x800fb8d8L)
  public static long preloadDrgnBinFiles(final long a0) {
    SInitBinLoaded_800bbad0.set(false);

    if(drgnFilesCached_800bbac8.get() && drgnFileCache_800bbacc.get() != 0) {
      free(drgnFileCache_800bbacc.get());
      drgnFileCache_800bbacc.setu(0);
    }

    //LAB_800fba30
    drgnFileCacheOffset_800fd51c.set(0);
    drgnFileCache_800fd518.setu(mallocHead(0x7_0000L));
    drgn2xFileName_8004dd88.set(String.format("\\SECT\\DRGN2%d.BIN", drgnBinIndex_800bc058.get()));

    filesCount_800fd514.set(0);
    FUN_800fbe28();
    qsort(fileEntries_800fd414, filesCount_800fd514.get(), 0x4, getBiFunctionAddress(SInit.class, "compareFileNames", Pointer.classFor(FileEntry08.class), Pointer.classFor(FileEntry08.class), long.class));

    //LAB_800fbac0
    for(int fileIndex = 0; fileIndex < filesCount_800fd514.get(); fileIndex++) {
      //LAB_800fbae0
      if(!Unpacker.exists(fileEntries_800fd414.get(fileIndex).deref().name_04.deref().get())) {
        //LAB_800fbb14
        throw new RuntimeException("Failed to find file");
      }

      fileEntries_800fd414.get(fileIndex).deref().fileIndex_00.set((short)fileIndex);
    }

    final int[] drgnFileOffsets = new int[3];
    for(int drgnBinIndex = 0; drgnBinIndex < 3; drgnBinIndex++) {
      //LAB_800fbbc8
      final MrgFile drgnMrg = drgnFileCache_800fd518.deref(4).offset(drgnFileCacheOffset_800fd51c.get()).cast(MrgFile::new);

      // No need to reload DRGN0/1 when changing disks
      final byte[] fileData;
      if(drgnBinIndex == 2 || DRGN_CACHE[drgnBinIndex] == null) {
        final int fileIndex = drgnFiles_8004dda0.get(drgnBinIndex).fileIndex_00.get();
        fileData = Unpacker.loadFile(fileEntries_800fd414.get(fileIndex).deref().name_04.deref().get());
        DRGN_CACHE[drgnBinIndex] = fileData;
      } else {
        fileData = DRGN_CACHE[drgnBinIndex];
      }

      final int mrgEntryCount = (int)MathHelper.get(fileData, 4, 4);
      final int mrgEntryTableSize = 8 + mrgEntryCount * 8;

      MEMORY.setBytes(drgnMrg.getAddress(), fileData, 0, mrgEntryTableSize);

      drgnFileOffsets[drgnBinIndex] = drgnFileCacheOffset_800fd51c.get();
      drgnFileCacheOffset_800fd51c.add(mrgEntryTableSize);
    }

    //LAB_800fbcb0
    if(drgnFileCacheOffset_800fd51c.get() == 0) {
      free(drgnFileCache_800fd518.get());

      drgnFileCache_800fd518.setu(0);
      drgnFileCache_800bbacc.setu(0);
    } else {
      //LAB_800fbcfc
      final long address = realloc2(drgnFileCache_800fd518.get(), drgnFileCacheOffset_800fd51c.get());

      //LAB_800fbd20
      drgnFileCache_800bbacc.setu(address);

      //LAB_800fbd50
      for(int i = 0; i < 3; i++) {
        if(drgnFileOffsets[i] >= 0) {
          //LAB_800fbd68
          drgnMrg_800bc060.get(i).set(MEMORY.ref(4, address + drgnFileOffsets[i], MrgFile::new));
        } else {
          drgnMrg_800bc060.get(i).clear();
        }
      }
    }

    //LAB_800fbd80
    SInitBinLoaded_800bbad0.set(true);
    drgnFilesCached_800bbac8.set(true);

    //LAB_800fb938
    //LAB_800fb96c
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
