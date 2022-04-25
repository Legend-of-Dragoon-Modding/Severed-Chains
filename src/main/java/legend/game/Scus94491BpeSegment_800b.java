package legend.game;

import legend.core.cdrom.CdlDIR;
import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.CdlPacket;
import legend.core.cdrom.FileLoadingInfo;
import legend.core.cdrom.Response;
import legend.core.cdrom.SyncCode;
import legend.core.gpu.TimHeader;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.VoidRef;
import legend.game.types.BigStruct;
import legend.game.types.Drgn0_6666File;
import legend.game.types.ScriptState;
import legend.game.types.Renderable58;
import legend.game.types.GsRVIEW2;
import legend.game.types.JoyStruct;
import legend.game.types.MemcardStruct28;
import legend.game.types.MrgFile;
import legend.game.types.ScriptEffectStruct;
import legend.game.types.RunningScript;
import legend.game.types.SpuStruct08;
import legend.game.types.SoundFile;
import legend.game.types.SpuStruct28;
import legend.game.types.SshdFile;
import legend.game.types.SssqFile;
import legend.game.types.Struct4c;
import legend.game.types.Struct84;

import java.util.function.Function;

import static legend.core.Hardware.MEMORY;
import static legend.core.LibDs.DSL_MAX_COMMAND;

public final class Scus94491BpeSegment_800b {
  private Scus94491BpeSegment_800b() { }

  public static final Value _800babc0 = MEMORY.ref(1, 0x800babc0L);

  public static final Value _800babc8 = MEMORY.ref(4, 0x800babc8L);

  public static final Value _800bac50 = MEMORY.ref(4, 0x800bac50L);
  public static final Value _800bac54 = MEMORY.ref(4, 0x800bac54L);
  public static final Value _800bac58 = MEMORY.ref(4, 0x800bac58L);
  public static final Value _800bac5c = MEMORY.ref(4, 0x800bac5cL);
  public static final Value _800bac60 = MEMORY.ref(4, 0x800bac60L);

  public static final Value _800bac68 = MEMORY.ref(4, 0x800bac68L);
  public static final Value _800bac6c = MEMORY.ref(4, 0x800bac6cL);
  public static final Value _800bac70 = MEMORY.ref(4, 0x800bac70L);

  public static final ArrayRef<UnsignedIntRef> scriptFlags2_800bac84 = MEMORY.ref(4, 0x800bac84L, ArrayRef.of(UnsignedIntRef.class, 0x20, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> scriptFlags1_800bad04 = MEMORY.ref(4, 0x800bad04L, ArrayRef.of(UnsignedIntRef.class, 8, 4, UnsignedIntRef::new));

  public static final Value _800bad24 = MEMORY.ref(4, 0x800bad24L);

  public static final Value _800bad44 = MEMORY.ref(4, 0x800bad44L);

  public static final Value _800bad64 = MEMORY.ref(1, 0x800bad64L);

  public static final Value _800bad6c = MEMORY.ref(4, 0x800bad6cL);

  public static final Value _800badac = MEMORY.ref(2, 0x800badacL);
  public static final Value _800badae = MEMORY.ref(2, 0x800badaeL);
  public static final Value _800badb0 = MEMORY.ref(1, 0x800badb0L);

  public static final Value _800bae00 = MEMORY.ref(4, 0x800bae00L);

  public static final Value _800baef4 = MEMORY.ref(4, 0x800baef4L);

  public static final Value _800baefc = MEMORY.ref(2, 0x800baefcL);

  public static final Value _800baf02 = MEMORY.ref(2, 0x800baf02L);

  public static final Value _800baf07 = MEMORY.ref(1, 0x800baf07L);

  public static final Value _800bb080 = MEMORY.ref(4, 0x800bb080L);

  public static final Value _800bb0a0 = MEMORY.ref(2, 0x800bb0a0L);
  public static final Value _800bb0a2 = MEMORY.ref(2, 0x800bb0a2L);
  public static final Value _800bb0a4 = MEMORY.ref(1, 0x800bb0a4L);
  public static final Value _800bb0a5 = MEMORY.ref(1, 0x800bb0a5L);
  public static final Value _800bb0a6 = MEMORY.ref(2, 0x800bb0a6L);
  public static final Value mono_800bb0a8 = MEMORY.ref(1, 0x800bb0a8L);
  public static final Value vibrationEnabled_800bb0a9 = MEMORY.ref(1, 0x800bb0a9L);
  public static final Value _800bb0aa = MEMORY.ref(1, 0x800bb0aaL);

  public static final Value _800bb0ab = MEMORY.ref(1, 0x800bb0abL);
  public static final Value _800bb0ac = MEMORY.ref(1, 0x800bb0acL);

  public static final Value _800bb0b0 = MEMORY.ref(4, 0x800bb0b0L);

  public static final Value submapStage_800bb0f4 = MEMORY.ref(4, 0x800bb0f4L);
  public static final Value submapScene_800bb0f8 = MEMORY.ref(4, 0x800bb0f8L);
  public static final Value _800bb0fc = MEMORY.ref(4, 0x800bb0fcL);

  public static final Value _800bb104 = MEMORY.ref(4, 0x800bb104L);
  public static final Value doubleBufferFrame_800bb108 = MEMORY.ref(4, 0x800bb108L);
  public static final Value pregameLoadingStage_800bb10c = MEMORY.ref(4, 0x800bb10cL);
  public static final Value _800bb110 = MEMORY.ref(2, 0x800bb110L);
  public static final Value _800bb112 = MEMORY.ref(2, 0x800bb112L);
  public static final Value _800bb114 = MEMORY.ref(2, 0x800bb114L);
  public static final Value _800bb116 = MEMORY.ref(2, 0x800bb116L);
  public static final Value _800bb118 = MEMORY.ref(2, 0x800bb118L);
  public static final Value _800bb11a = MEMORY.ref(2, 0x800bb11aL);

  public static final Value _800bb120 = MEMORY.ref(2, 0x800bb120L);

  public static final Value _800bb134 = MEMORY.ref(2, 0x800bb134L);

  public static final ScriptEffectStruct scriptEffect_800bb140 = MEMORY.ref(4, 0x800bb140L, ScriptEffectStruct::new);
  public static final Value _800bb168 = MEMORY.ref(4, 0x800bb168L); //TODO is this part of the previous struct?

  public static final ArrayRef<UnsignedIntRef> array_800bb198 = MEMORY.ref(4, 0x800bb198L, ArrayRef.of(UnsignedIntRef.class, 36, 4, UnsignedIntRef::new));

  public static final Value _800bb228 = MEMORY.ref(4, 0x800bb228L);

  public static final Value _800bb348 = MEMORY.ref(4, 0x800bb348L);

  public static final Value transferDest_800bb460 = MEMORY.ref(4, 0x800bb460L);
  public static final Value fileSize_800bb464 = MEMORY.ref(4, 0x800bb464L);
  public static final FileLoadingInfo currentlyLoadingFileInfo_800bb468 = MEMORY.ref(4, 0x800bb468L, FileLoadingInfo::new);
  public static final Value fileTransferDest_800bb488 = MEMORY.ref(4, 0x800bb488L);
  public static final Value fileSize_800bb48c = MEMORY.ref(4, 0x800bb48cL);
  public static final Value numberOfTransfers_800bb490 = MEMORY.ref(4, 0x800bb490L);
  public static final Value transferIndex_800bb494 = MEMORY.ref(4, 0x800bb494L);

  public static final ArrayRef<CdlFILE> CdlFILE_800bb4c8 = MEMORY.ref(0x600, 0x800bb4c8L, ArrayRef.of(CdlFILE.class, 0x40, 0x18, CdlFILE::new));

  public static final Value _800bbac8 = MEMORY.ref(1, 0x800bbac8L);

  public static final Value linkedListEntry_800bbacc = MEMORY.ref(4, 0x800bbaccL);
  public static final BoolRef SInitBinLoaded_800bbad0 = MEMORY.ref(1, 0x800bbad0L, BoolRef::new);

  public static final ArrayRef<FileLoadingInfo> fileLoadingInfoArray_800bbad8 = MEMORY.ref(0x580, 0x800bbad8L, ArrayRef.of(FileLoadingInfo.class, 44, 32, FileLoadingInfo::new));
  public static final Value drgnBinIndex_800bc058 = MEMORY.ref(4, 0x800bc058L);
  public static final Value _800bc05c = MEMORY.ref(4, 0x800bc05cL);
  public static final ArrayRef<Pointer<MrgFile>> drgnMrg_800bc060 = MEMORY.ref(4, 0x800bc060L, ArrayRef.of(Pointer.classFor(MrgFile.class), 4, 4, Pointer.deferred(4, MrgFile::new)));
  public static final RunningScript RunningScript_800bc070 = MEMORY.ref(4, 0x800bc070L, RunningScript::new);

  public static final Value _800bc0b8 = MEMORY.ref(1, 0x800bc0b8L);
  public static final Value _800bc0b9 = MEMORY.ref(1, 0x800bc0b9L);

  public static final ScriptState<VoidRef> scriptState_800bc0c0 = MEMORY.ref(4, 0x800bc0c0L, ScriptState.of(VoidRef::new));
  public static final ArrayRef<Pointer<ScriptState<? extends MemoryRef>>> scriptStatePtrArr_800bc1c0 = (ArrayRef<Pointer<ScriptState<? extends MemoryRef>>>)MEMORY.ref(4, 0x800bc1c0L, ArrayRef.of(Pointer.classFor(ScriptState.class), 0x48, 4, (Function)Pointer.deferred(4, ScriptState.of(ref -> { throw new RuntimeException("Can't auto-instantiate"); }))));
  public static final TimHeader timHeader_800bc2e0 = MEMORY.ref(0x1c, 0x800bc2e0L, TimHeader::new);

  public static final Value _800bc300 = MEMORY.ref(4, 0x800bc300L);
  public static final Value _800bc304 = MEMORY.ref(4, 0x800bc304L);
  public static final Value _800bc308 = MEMORY.ref(4, 0x800bc308L);

  public static final Value _800bc960 = MEMORY.ref(1, 0x800bc960L);

  //TODO structure @ 800bc980... 3 * 12?
  public static final Value _800bc980 = MEMORY.ref(4, 0x800bc980L);

  public static final ArrayRef<SpuStruct08> _800bc9a8 = MEMORY.ref(4, 0x800bc9a8L, ArrayRef.of(SpuStruct08.class, 24, 0x8, SpuStruct08::new));

  public static final Value _800bca68 = MEMORY.ref(1, 0x800bca68L);

  public static final Value _800bca6c = MEMORY.ref(4, 0x800bca6cL);

  public static final ArrayRef<SpuStruct28> spu28Arr_800bca78 = MEMORY.ref(1, 0x800bca78L, ArrayRef.of(SpuStruct28.class, 32, 0x28, SpuStruct28::new));

  /**
   * Bits:
   * 0 - MRG @ 62802 - audio
   */
  public static final Value loadedDrgnFiles_800bcf78 = MEMORY.ref(4, 0x800bcf78L);

  public static final ArrayRef<SoundFile> soundFileArr_800bcf80 = MEMORY.ref(2, 0x800bcf80L, ArrayRef.of(SoundFile.class, 13, 0x1c, SoundFile::new));

  public static final Value _800bd0f0 = MEMORY.ref(2, 0x800bd0f0L);

  public static final Value sssqChannelIndex_800bd0f8 = MEMORY.ref(2, 0x800bd0f8L);

  public static final Value _800bd0fc = MEMORY.ref(4, 0x800bd0fcL);
  public static final Value sssqTempoScale_800bd100 = MEMORY.ref(4, 0x800bd100L);
  public static final Value sssqTempo_800bd104 = MEMORY.ref(4, 0x800bd104L);
  public static final Value _800bd108 = MEMORY.ref(2, 0x800bd108L);

  public static final ArrayRef<SpuStruct28> spu28Arr_800bd110 = MEMORY.ref(1, 0x800bd110L, ArrayRef.of(SpuStruct28.class, 32, 0x28, SpuStruct28::new));
  public static final Value _800bd610 = MEMORY.ref(2, 0x800bd610L);

  public static final Value _800bd614 = MEMORY.ref(4, 0x800bd614L);

  public static final Value _800bd61c = MEMORY.ref(2, 0x800bd61cL);

  public static final Value _800bd680 = MEMORY.ref(4, 0x800bd680L);
  public static final Value _800bd684 = MEMORY.ref(4, 0x800bd684L);

  public static final Value _800bd690 = MEMORY.ref(4, 0x800bd690L);

  public static final Value _800bd6f8 = MEMORY.ref(4, 0x800bd6f8L);

  public static final Value _800bd700 = MEMORY.ref(1, 0x800bd700L);
  public static final Value _800bd704 = MEMORY.ref(4, 0x800bd704L);
  public static final Value _800bd708 = MEMORY.ref(4, 0x800bd708L);
  public static final Value _800bd70c = MEMORY.ref(4, 0x800bd70cL);
  public static final Value _800bd710 = MEMORY.ref(4, 0x800bd710L);
  public static final Value _800bd714 = MEMORY.ref(4, 0x800bd714L);

  public static final Value _800bd740 = MEMORY.ref(4, 0x800bd740L);

  public static final Pointer<MrgFile> soundMrgPtr_800bd748 = MEMORY.ref(4, 0x800bd748L, Pointer.deferred(4, MrgFile::new));

  public static final Pointer<MrgFile> soundMrgPtr_800bd76c = MEMORY.ref(4, 0x800bd76cL, Pointer.deferred(4, MrgFile::new));

  public static final Value soundbank_800bd778 = MEMORY.ref(4, 0x800bd778L);

  public static final Value _800bd780 = MEMORY.ref(1, 0x800bd780L);
  public static final Value _800bd781 = MEMORY.ref(1, 0x800bd781L);
  public static final Value _800bd782 = MEMORY.ref(1, 0x800bd782L);

  public static final Pointer<SshdFile> soundMrgSshdPtr_800bd784 = MEMORY.ref(4, 0x800bd784L, Pointer.deferred(4, SshdFile::new));
  public static final Pointer<SssqFile> soundMrgSssqPtr_800bd788 = MEMORY.ref(4, 0x800bd788L, Pointer.deferred(4, SssqFile::new));

  public static final Value _800bd7a0 = MEMORY.ref(4, 0x800bd7a0L);
  public static final Value _800bd7a4 = MEMORY.ref(4, 0x800bd7a4L);
  public static final Value _800bd7a8 = MEMORY.ref(4, 0x800bd7a8L);
  public static final Value _800bd7ac = MEMORY.ref(4, 0x800bd7acL);
  public static final Value _800bd7b0 = MEMORY.ref(4, 0x800bd7b0L);
  public static final Value _800bd7b4 = MEMORY.ref(2, 0x800bd7b4L);

  public static final Value _800bd7b8 = MEMORY.ref(4, 0x800bd7b8L);

  public static final UnboundedArrayRef<GsCOORD2PARAM> _800bd7c0 = MEMORY.ref(4, 0x800bd7c0L, UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new));

  public static final GsRVIEW2 rview2_800bd7e8 = MEMORY.ref(4, 0x800bd7e8L, GsRVIEW2::new);
  public static final Value _800bd808 = MEMORY.ref(4, 0x800bd808L);
  public static final Value _800bd80c = MEMORY.ref(4, 0x800bd80cL);
  public static final Value projectionPlaneDistance_800bd810 = MEMORY.ref(4, 0x800bd810L);

  public static final Value _800bd818 = MEMORY.ref(4, 0x800bd818L);

  public static final UnboundedArrayRef<GsDOBJ2> _800bd9f8 = MEMORY.ref(4, 0x800bd9f8L, UnboundedArrayRef.of(0x10, GsDOBJ2::new));

  public static final Value _800bda08 = MEMORY.ref(4, 0x800bda08L);

  public static final BigStruct bigStruct_800bda10 = MEMORY.ref(4, 0x800bda10L, BigStruct::new);

  public static final UnboundedArrayRef<GsCOORDINATE2> _800bdb38 = MEMORY.ref(4, 0x800bdb38L, UnboundedArrayRef.of(0x50, GsCOORDINATE2::new));

  public static final Value _800bdb88 = MEMORY.ref(4, 0x800bdb88L);

  public static final Value _800bdb90 = MEMORY.ref(4, 0x800bdb90L);
  public static final Pointer<Renderable58> renderablePtr_800bdb94 = MEMORY.ref(4, 0x800bdb94L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdb98 = MEMORY.ref(4, 0x800bdb98L, Pointer.deferred(4, Renderable58::new));
  public static final Value _800bdb9c = MEMORY.ref(4, 0x800bdb9cL);
  public static final Value _800bdba0 = MEMORY.ref(4, 0x800bdba0L);
  public static final Pointer<Renderable58> renderablePtr_800bdba4 = MEMORY.ref(4, 0x800bdba4L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdba8 = MEMORY.ref(4, 0x800bdba8L, Pointer.deferred(4, Renderable58::new));

  public static final Value _800bdbb8 = MEMORY.ref(4, 0x800bdbb8L);

  public static final Pointer<Renderable58> renderablePtr_800bdbe0 = MEMORY.ref(4, 0x800bdbe0L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdbe4 = MEMORY.ref(4, 0x800bdbe4L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdbe8 = MEMORY.ref(4, 0x800bdbe8L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdbec = MEMORY.ref(4, 0x800bdbecL, Pointer.deferred(4, Renderable58::new));
  public static final Value _800bdbf0 = MEMORY.ref(4, 0x800bdbf0L);

  public static final Value _800bdbf8 = MEMORY.ref(4, 0x800bdbf8L);

  public static final Pointer<Renderable58> renderablePtr_800bdc20 = MEMORY.ref(4, 0x800bdc20L, Pointer.deferred(4, Renderable58::new));
  public static final Value _800bdc24 = MEMORY.ref(4, 0x800bdc24L);
  public static final Value inventoryMenuState_800bdc28 = MEMORY.ref(4, 0x800bdc28L);
  public static final Value _800bdc2c = MEMORY.ref(4, 0x800bdc2cL);
  public static final Value _800bdc30 = MEMORY.ref(4, 0x800bdc30L);

  public static final Value _800bdc34 = MEMORY.ref(4, 0x800bdc34L);
  /**
   * 0xe - load game
   * 0x13 - also load game (maybe save game...?)
   * 0x18 - char swap
   *
   * Seems any other value shows the inventory
   */
  public static final Value whichMenu_800bdc38 = MEMORY.ref(4, 0x800bdc38L);
  public static final Pointer<Drgn0_6666File> drgn0_6666FilePtr_800bdc3c = MEMORY.ref(4, 0x800bdc3cL, Pointer.deferred(4, Drgn0_6666File::new));
  public static final Value _800bdc40 = MEMORY.ref(4, 0x800bdc40L);
  public static final Value inventoryJoypadInput_800bdc44 = MEMORY.ref(4, 0x800bdc44L);

  public static final Pointer<ArrayRef<MemcardStruct28>> memcardStruct28ArrPtr_800bdc50 = MEMORY.ref(4, 0x800bdc50L, Pointer.deferred(4, ArrayRef.of(MemcardStruct28.class, 0x10, 0x28, MemcardStruct28::new)));

  public static final Value _800bdc58 = MEMORY.ref(4, 0x800bdc58L);
  public static final Pointer<Renderable58> renderablePtr_800bdc5c = MEMORY.ref(4, 0x800bdc5cL, Pointer.deferred(4, Renderable58::new));

  public static final Value _800bdea0 = MEMORY.ref(4, 0x800bdea0L);

  public static final Value _800bdf00 = MEMORY.ref(4, 0x800bdf00L);
  public static final Value _800bdf04 = MEMORY.ref(4, 0x800bdf04L);
  public static final Value _800bdf08 = MEMORY.ref(4, 0x800bdf08L);

  public static final Value _800bdf18 = MEMORY.ref(4, 0x800bdf18L);

  public static final ArrayRef<Struct84> _800bdf38 = MEMORY.ref(4, 0x800bdf38L, ArrayRef.of(Struct84.class, 8, 0x84, Struct84::new));
  public static final ArrayRef<Struct4c> _800be358 = MEMORY.ref(4, 0x800be358L, ArrayRef.of(Struct4c.class, 8, 0x4c, Struct4c::new));
  public static final Value _800be5b8 = MEMORY.ref(4, 0x800be5b8L);
  public static final Value _800be5bc = MEMORY.ref(4, 0x800be5bcL);
  public static final Value _800be5c0 = MEMORY.ref(4, 0x800be5c0L);
  public static final Value _800be5c4 = MEMORY.ref(2, 0x800be5c4L);
  public static final Value _800be5c8 = MEMORY.ref(4, 0x800be5c8L);

  public static final Value _800be5d0 = MEMORY.ref(4, 0x800be5d0L);

  public static final Value _800be5d8 = MEMORY.ref(1, 0x800be5d8L);
  public static final Value _800be5d9 = MEMORY.ref(1, 0x800be5d9L);
  public static final Value _800be5da = MEMORY.ref(1, 0x800be5daL);
  public static final Value _800be5db = MEMORY.ref(1, 0x800be5dbL);
  public static final Value _800be5dc = MEMORY.ref(1, 0x800be5dcL);
  public static final Value _800be5dd = MEMORY.ref(1, 0x800be5ddL);
  public static final Value _800be5de = MEMORY.ref(1, 0x800be5deL);
  public static final Value _800be5df = MEMORY.ref(1, 0x800be5dfL);
  public static final Value _800be5e0 = MEMORY.ref(1, 0x800be5e0L);
  public static final Value _800be5e1 = MEMORY.ref(1, 0x800be5e1L);
  public static final Value _800be5e2 = MEMORY.ref(1, 0x800be5e2L);
  public static final Value _800be5e3 = MEMORY.ref(1, 0x800be5e3L);
  public static final Value _800be5e4 = MEMORY.ref(1, 0x800be5e4L);
  public static final Value _800be5e5 = MEMORY.ref(1, 0x800be5e5L);
  public static final Value _800be5e6 = MEMORY.ref(1, 0x800be5e6L);
  public static final Value _800be5e7 = MEMORY.ref(1, 0x800be5e7L);
  public static final Value _800be5e8 = MEMORY.ref(1, 0x800be5e8L);
  public static final Value _800be5e9 = MEMORY.ref(1, 0x800be5e9L);
  public static final Value _800be5ea = MEMORY.ref(1, 0x800be5eaL);
  public static final Value _800be5eb = MEMORY.ref(1, 0x800be5ebL);
  public static final Value _800be5ec = MEMORY.ref(1, 0x800be5ecL);
  public static final Value _800be5ed = MEMORY.ref(1, 0x800be5edL);
  public static final Value _800be5ee = MEMORY.ref(1, 0x800be5eeL);
  public static final Value _800be5ef = MEMORY.ref(1, 0x800be5efL);
  public static final Value _800be5f0 = MEMORY.ref(1, 0x800be5f0L);
  public static final Value _800be5f1 = MEMORY.ref(1, 0x800be5f1L);
  public static final Value _800be5f2 = MEMORY.ref(1, 0x800be5f2L);
  public static final Value _800be5f3 = MEMORY.ref(1, 0x800be5f3L);

  //TODO This is a giant structure
  public static final Value _800be5f8 = MEMORY.ref(4, 0x800be5f8L);
  public static final Value _800be5fc = MEMORY.ref(2, 0x800be5fcL);
  public static final Value _800be5fe = MEMORY.ref(2, 0x800be5feL);
  public static final Value _800be600 = MEMORY.ref(2, 0x800be600L);
  public static final Value _800be602 = MEMORY.ref(2, 0x800be602L);
  public static final Value _800be604 = MEMORY.ref(2, 0x800be604L);
  public static final Value _800be606 = MEMORY.ref(1, 0x800be606L);
  public static final Value _800be607 = MEMORY.ref(1, 0x800be607L);

  public static final Value _800be628 = MEMORY.ref(1, 0x800be628L);

  public static final Value _800be62d = MEMORY.ref(1, 0x800be62dL);
  public static final Value _800be62e = MEMORY.ref(1, 0x800be62eL);

  public static final Value _800be636 = MEMORY.ref(1, 0x800be636L);

  public static final Value _800be63e = MEMORY.ref(2, 0x800be63eL);
  public static final Value _800be640 = MEMORY.ref(2, 0x800be640L);
  public static final Value _800be642 = MEMORY.ref(2, 0x800be642L);
  public static final Value _800be644 = MEMORY.ref(2, 0x800be644L);
  public static final Value _800be646 = MEMORY.ref(2, 0x800be646L);
  public static final Value _800be648 = MEMORY.ref(2, 0x800be648L);
  public static final Value _800be64a = MEMORY.ref(2, 0x800be64aL);
  public static final Value _800be64c = MEMORY.ref(2, 0x800be64cL);
  public static final Value _800be64e = MEMORY.ref(2, 0x800be64eL);
  public static final Value _800be650 = MEMORY.ref(2, 0x800be650L);
  public static final Value _800be652 = MEMORY.ref(2, 0x800be652L);
  public static final Value _800be654 = MEMORY.ref(2, 0x800be654L);
  public static final Value _800be656 = MEMORY.ref(2, 0x800be656L);
  public static final Value _800be658 = MEMORY.ref(2, 0x800be658L);
  public static final Value _800be65a = MEMORY.ref(2, 0x800be65aL);
  public static final Value _800be65c = MEMORY.ref(2, 0x800be65cL);
  public static final Value _800be65e = MEMORY.ref(2, 0x800be65eL);
  public static final Value _800be660 = MEMORY.ref(1, 0x800be660L);
  public static final Value _800be661 = MEMORY.ref(1, 0x800be661L);
  public static final Value _800be662 = MEMORY.ref(1, 0x800be662L);
  public static final Value _800be663 = MEMORY.ref(1, 0x800be663L);
  public static final Value _800be664 = MEMORY.ref(1, 0x800be664L);
  public static final Value _800be665 = MEMORY.ref(1, 0x800be665L);
  public static final Value _800be666 = MEMORY.ref(2, 0x800be666L);
  public static final Value _800be668 = MEMORY.ref(1, 0x800be668L);
  public static final Value _800be669 = MEMORY.ref(1, 0x800be669L);
  public static final Value _800be66a = MEMORY.ref(1, 0x800be66aL);
  public static final Value _800be66b = MEMORY.ref(1, 0x800be66bL);
  public static final Value _800be66c = MEMORY.ref(1, 0x800be66cL);
  public static final Value _800be66d = MEMORY.ref(1, 0x800be66dL);
  public static final Value _800be66e = MEMORY.ref(1, 0x800be66eL);
  public static final Value _800be66f = MEMORY.ref(1, 0x800be66fL);
  public static final Value _800be670 = MEMORY.ref(1, 0x800be670L);
  public static final Value _800be671 = MEMORY.ref(1, 0x800be671L);
  public static final Value _800be672 = MEMORY.ref(1, 0x800be672L);
  public static final Value _800be673 = MEMORY.ref(1, 0x800be673L);
  public static final Value _800be674 = MEMORY.ref(1, 0x800be674L);
  public static final Value _800be675 = MEMORY.ref(1, 0x800be675L);
  public static final Value _800be676 = MEMORY.ref(1, 0x800be676L);
  public static final Value _800be677 = MEMORY.ref(1, 0x800be677L);
  public static final Value _800be678 = MEMORY.ref(1, 0x800be678L);
  public static final Value _800be679 = MEMORY.ref(1, 0x800be679L);
  public static final Value _800be67a = MEMORY.ref(1, 0x800be67aL);
  public static final Value _800be67b = MEMORY.ref(1, 0x800be67bL);
  public static final Value _800be67c = MEMORY.ref(1, 0x800be67cL);
  public static final Value _800be67e = MEMORY.ref(2, 0x800be67eL);
  public static final Value _800be680 = MEMORY.ref(2, 0x800be680L);
  public static final Value _800be682 = MEMORY.ref(2, 0x800be682L);
  public static final Value _800be684 = MEMORY.ref(2, 0x800be684L);
  public static final Value _800be686 = MEMORY.ref(2, 0x800be686L);
  public static final Value _800be688 = MEMORY.ref(2, 0x800be688L);
  public static final Value _800be68a = MEMORY.ref(2, 0x800be68aL);
  public static final Value _800be68c = MEMORY.ref(2, 0x800be68cL);
  public static final Value _800be68e = MEMORY.ref(2, 0x800be68eL);
  public static final Value _800be690 = MEMORY.ref(1, 0x800be690L);
  public static final Value _800be691 = MEMORY.ref(1, 0x800be691L);
  public static final Value _800be692 = MEMORY.ref(1, 0x800be692L);
  public static final Value _800be693 = MEMORY.ref(1, 0x800be693L);
  public static final Value _800be694 = MEMORY.ref(2, 0x800be694L);
  public static final Value _800be696 = MEMORY.ref(1, 0x800be696L);
  public static final Value _800be697 = MEMORY.ref(1, 0x800be697L);
  //TODO

  public static final MATRIX matrix_800bed30 = MEMORY.ref(4, 0x800bed30L, MATRIX::new);
  public static final IntRef screenOffsetX_800bed50 = MEMORY.ref(4, 0x800bed50L, IntRef::new);
  public static final IntRef screenOffsetY_800bed54 = MEMORY.ref(4, 0x800bed54L, IntRef::new);
  public static final Value hasNoEncounters_800bed58 = MEMORY.ref(4, 0x800bed58L);

  public static final ArrayRef<JoyStruct> _800bed60 = MEMORY.ref(4, 0x800bed60L, ArrayRef.of(JoyStruct.class, 2, 0x90, JoyStruct::new));

  //00
//  public static final Value _800bed60 = MEMORY.ref(2, 0x800bed60L);
  //02
//  public static final Value _800bed62 = MEMORY.ref(2, 0x800bed62L);
  //04
//  public static final Value _800bed64 = MEMORY.ref(2, 0x800bed64L);
  //06
//  public static final Value _800bed66 = MEMORY.ref(2, 0x800bed66L);

  //0c
//  public static final Value _800bed6c = MEMORY.ref(2, 0x800bed6eL);
  //0e
//  public static final Value _800bed6e = MEMORY.ref(2, 0x800bed6cL);

  //12
//  public static final Value _800bed72 = MEMORY.ref(2, 0x800bed72L);
  //14
//  public static final Value _800bed74 = MEMORY.ref(2, 0x800bed74L);
  //16
//  public static final Value _800bed76 = MEMORY.ref(2, 0x800bed76L);

  //34
//  public static final Value _800bed94 = MEMORY.ref(1, 0x800bed94L);

  //3c
//  public static final Value _800bed9c = MEMORY.ref(4, 0x800bed9cL);

  //44
//  public static final Value _800beda4 = MEMORY.ref(4, 0x800beda4L);
  //48
//  public static final Value _800beda8 = MEMORY.ref(2, 0x800beda8L);

  //52
//  public static final Value _800bedb2 = MEMORY.ref(2, 0x800bedb2L);

  //56
//  public static final Value _800bedb6 = MEMORY.ref(2, 0x800bedb6L);
  //58
//  public static final Value _800bedb8 = MEMORY.ref(1, 0x800bedb8L);
  //59
//  public static final Value _800bedb9 = MEMORY.ref(1, 0x800bedb9L);
  //5a
//  public static final Value _800bedba = MEMORY.ref(1, 0x800bedbaL);
  //5b
//  public static final Value _800bedbb = MEMORY.ref(1, 0x800bedbbL);
  //5c
//  public static final Value _800bedbc = MEMORY.ref(2, 0x800bedbcL);
  //5e
//  public static final ArrayRef<ByteRef> _800bedbe = MEMORY.ref(1, 0x800bedbeL, ArrayRef.of(ByteRef.class, 35, 1, ByteRef::new));

  //80
//  public static final Value _800bede0 = MEMORY.ref(1, 0x800bede0L);
  //81
//  public static final Value _800bede1 = MEMORY.ref(1, 0x800bede1L);
  //82
//  public static final Value _800bede2 = MEMORY.ref(1, 0x800bede2L);
  //83
//  public static final Value _800bede3 = MEMORY.ref(1, 0x800bede3L);
  //84
//  public static final Value _800bede4 = MEMORY.ref(1, 0x800bede4L);

  //88
//  public static final Value _800bede8 = MEMORY.ref(4, 0x800bede8L);
  //8c
//  public static final Value _800bedec = MEMORY.ref(1, 0x800bedecL);
  // End of struct 800bedef

  // Next
  //58
//  public static final Value _800bee48 = MEMORY.ref(1, 0x800bee48L);
  //59
//  public static final Value _800bee49 = MEMORY.ref(1, 0x800bee49L);
  //5a
//  public static final Value _800bee4a = MEMORY.ref(1, 0x800bee4aL);
  //5b
//  public static final Value _800bee4b = MEMORY.ref(1, 0x800bee4bL);

  //5e
//  public static final ArrayRef<ByteRef> _800bee4e = MEMORY.ref(1, 0x800bee4eL, ArrayRef.of(ByteRef.class, 35, 1, ByteRef::new));
  // End of struct 800bee7f

  public static final ArrayRef<UnsignedIntRef> _800bee80 = MEMORY.ref(4, 0x800bee80L, ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));
  public static final Value _800bee88 = MEMORY.ref(4, 0x800bee88L);
  public static final Value _800bee8c = MEMORY.ref(4, 0x800bee8cL);
  public static final Value _800bee90 = MEMORY.ref(4, 0x800bee90L);
  public static final Value _800bee94 = MEMORY.ref(4, 0x800bee94L);
  public static final Value _800bee98 = MEMORY.ref(4, 0x800bee98L);
  public static final Value _800bee9c = MEMORY.ref(4, 0x800bee9cL);

  public static final Value _800beea4 = MEMORY.ref(4, 0x800beea8L);

  public static final Value _800beeac = MEMORY.ref(4, 0x800beeacL);

  public static final Value _800beeb4 = MEMORY.ref(4, 0x800beeb8L);

  public static final Value _800beebc = MEMORY.ref(4, 0x800beebcL);

  public static final Value _800beec4 = MEMORY.ref(4, 0x800beec4L);

  public static final Value _800bef44 = MEMORY.ref(4, 0x800bef44L);

  public static final Value _800befc4 = MEMORY.ref(4, 0x800befc4L);

  public static final Value _800bf044 = MEMORY.ref(1, 0x800bf044L);

  public static final Value _800bf064 = MEMORY.ref(4, 0x800bf064L);
  public static final ArrayRef<ArrayRef<JoyStruct.JoyStruct2>> _800bf068 = MEMORY.ref(2, 0x800bf068L, ArrayRef.of(ArrayRef.classFor(JoyStruct.JoyStruct2.class), 2, 0x20, ArrayRef.of(JoyStruct.JoyStruct2.class, 2, 0x10, JoyStruct.JoyStruct2::new)));
//  public static final Value _800bf068 = MEMORY.ref(2, 0x800bf068L);
//  public static final Value _800bf06a = MEMORY.ref(2, 0x800bf06aL);
//  public static final Value _800bf06c = MEMORY.ref(2, 0x800bf06cL);
//  public static final Value _800bf06e = MEMORY.ref(2, 0x800bf06eL);

//  public static final Value _800bf074 = MEMORY.ref(2, 0x800bf074L);
//  public static final Value _800bf076 = MEMORY.ref(2, 0x800bf076L);

//  public static final Value _800bf07a = MEMORY.ref(2, 0x800bf07aL);
//  public static final Value _800bf07c = MEMORY.ref(2, 0x800bf07cL);
//  public static final Value _800bf07e = MEMORY.ref(2, 0x800bf07eL);

  public static final Value _800bf0a8 = MEMORY.ref(1, 0x800bf0a8L);
  public static final Value _800bf0ac = MEMORY.ref(4, 0x800bf0acL);

  public static final Value _800bf0b0 = MEMORY.ref(4, 0x800bf0b0L);
  public static final Value _800bf0b4 = MEMORY.ref(4, 0x800bf0b4L);

  public static final Value _800bf0c0 = MEMORY.ref(4, 0x800bf0c0L);
  public static final Value _800bf0c4 = MEMORY.ref(4, 0x800bf0c4L);
  public static final Value _800bf0c8 = MEMORY.ref(4, 0x800bf0c8L);
  public static final Value _800bf0cc = MEMORY.ref(1, 0x800bf0ccL);
  public static final Value _800bf0cd = MEMORY.ref(1, 0x800bf0cdL);
  public static final Value _800bf0ce = MEMORY.ref(1, 0x800bf0ceL);
  public static final Value _800bf0cf = MEMORY.ref(1, 0x800bf0cfL);
  public static final Value _800bf0d0 = MEMORY.ref(1, 0x800bf0d0L);

  public static final Value _800bf0d8 = MEMORY.ref(4, 0x800bf0d8L);

  public static final Value _800bf0dc = MEMORY.ref(4, 0x800bf0dcL);
  public static final Value _800bf0e0 = MEMORY.ref(4, 0x800bf0e0L);

  public static final Value _800bf0ec = MEMORY.ref(4, 0x800bf0ecL);

  public static final Value _800bf140 = MEMORY.ref(4, 0x800bf140L);
  public static final Value memcardStatus_800bf144 = MEMORY.ref(4, 0x800bf144L);
  public static final Value _800bf148 = MEMORY.ref(4, 0x800bf148L);
  public static final Value _800bf14c = MEMORY.ref(4, 0x800bf14cL);
  public static final Value _800bf150 = MEMORY.ref(4, 0x800bf150L);
  public static final Value _800bf154 = MEMORY.ref(4, 0x800bf154L);
  public static final Value _800bf158 = MEMORY.ref(4, 0x800bf158L);

  public static final Value previousMemcardEvent_800bf160 = MEMORY.ref(4, 0x800bf160L);
  public static final Value previousMemcardState_800bf164 = MEMORY.ref(4, 0x800bf164L);

  public static final Value activeMemcardEvent_800bf170 = MEMORY.ref(4, 0x800bf170L);
  /**
   * 0 - okay
   * 1 - busy
   * 2 - error
   * 3 - ejected
   * 4 - ?
   * 5 - failed to open memcard file
   */
  public static final Value memcardState_800bf174 = MEMORY.ref(4, 0x800bf174L);
  public static final Value _800bf178 = MEMORY.ref(4, 0x800bf178L);
  public static final Value _800bf17c = MEMORY.ref(4, 0x800bf17cL);
  public static final Value cardPort_800bf180 = MEMORY.ref(4, 0x800bf180L);
  public static final IntRef memcardFileHandle_800bf184 = MEMORY.ref(4, 0x800bf184L, IntRef::new);
  public static final Value memcardPos_800bf188 = MEMORY.ref(4, 0x800bf188L);
  public static final Value memcardLength_800bf18c = MEMORY.ref(4, 0x800bf18cL);
  public static final Value memcardDest_800bf190 = MEMORY.ref(4, 0x800bf190L);

  public static final Pointer<BiConsumerRef<Long, Long>> _800bf1b4 = MEMORY.ref(4, 0x800bf1b4L, Pointer.of(4, BiConsumerRef::new));
  public static final Value _800bf1b8 = MEMORY.ref(4, 0x800bf1b8L);
  public static final Value _800bf1bc = MEMORY.ref(4, 0x800bf1bcL);
  public static final Value _800bf1c0 = MEMORY.ref(4, 0x800bf1c0L);
  public static final Value _800bf1c4 = MEMORY.ref(4, 0x800bf1c4L);
  public static final Pointer<BiConsumerRef<Long, Long>> _800bf1c8 = MEMORY.ref(4, 0x800bf1c8L, Pointer.deferred(4, BiConsumerRef::new));

  public static final Value deviceCallback_800bf1d0 = MEMORY.ref(4, 0x800bf1d0L);

  /** A string */
  public static final Value _800bf1d8 = MEMORY.ref(1, 0x800bf1d8L);

  public static final Value _800bf200 = MEMORY.ref(4, 0x800bf200L);

  public static final Value _800bf23c = MEMORY.ref(4, 0x800bf23cL);
  public static final Value memcardVsyncCallbacks_800bf240 = MEMORY.ref(4, 0x800bf240L);

  public static final Value SwCARD_EvSpIOE_EventId_800bf250 = MEMORY.ref(4, 0x800bf250L);
  public static final Value SwCARD_EvSpERROR_EventId_800bf254 = MEMORY.ref(4, 0x800bf254L);
  public static final Value SwCARD_EvSpTIMOUT_EventId_800bf258 = MEMORY.ref(4, 0x800bf258L);
  public static final Value SwCARD_EvSpNEW_EventId_800bf25c = MEMORY.ref(4, 0x800bf25cL);
  public static final Value HwCARD_EvSpIOE_EventId_800bf260 = MEMORY.ref(4, 0x800bf260L);
  public static final Value HwCARD_EvSpERROR_EventId_800bf264 = MEMORY.ref(4, 0x800bf264L);
  public static final Value HwCARD_EvSpTIMOUT_EventId_800bf268 = MEMORY.ref(4, 0x800bf268L);
  public static final Value HwCARD_EvSpNEW_EventId_800bf26c = MEMORY.ref(4, 0x800bf26cL);
  public static final BoolRef cardDoneRead_800bf270 = MEMORY.ref(4, 0x800bf270L, BoolRef::new);
  public static final BoolRef cardErrorWrite_800bf274 = MEMORY.ref(4, 0x800bf274L, BoolRef::new);
  public static final BoolRef cardErrorBusy_800bf278 = MEMORY.ref(4, 0x800bf278L, BoolRef::new);
  public static final BoolRef cardErrorEject_800bf27c = MEMORY.ref(4, 0x800bf27cL, BoolRef::new);
  public static final BoolRef cardFinishedOkay_800bf280 = MEMORY.ref(4, 0x800bf280L, BoolRef::new);
  public static final BoolRef cardError8000_800bf284 = MEMORY.ref(4, 0x800bf284L, BoolRef::new);
  public static final BoolRef cardErrorBusyLow_800bf288 = MEMORY.ref(4, 0x800bf288L, BoolRef::new);
  public static final BoolRef cardError2000_800bf28c = MEMORY.ref(4, 0x800bf28cL, BoolRef::new);

  public static final Value _800bf550 = MEMORY.ref(4, 0x800bf550L);
  public static final Value _800bf554 = MEMORY.ref(2, 0x800bf554L);

  public static final Value _800bf558 = MEMORY.ref(4, 0x800bf558L);
  public static final Value _800bf55c = MEMORY.ref(4, 0x800bf55cL);
  public static final Value _800bf560 = MEMORY.ref(4, 0x800bf560L);
  public static final Value _800bf564 = MEMORY.ref(4, 0x800bf564L);
  public static final Value _800bf568 = MEMORY.ref(4, 0x800bf568L);
  public static final Value _800bf56c = MEMORY.ref(4, 0x800bf56cL);
  public static final Value _800bf570 = MEMORY.ref(4, 0x800bf570L);
  public static final Value _800bf574 = MEMORY.ref(4, 0x800bf574L);
  public static final Value _800bf578 = MEMORY.ref(4, 0x800bf578L);
  public static final Value _800bf57c = MEMORY.ref(4, 0x800bf57cL);
  public static final Value _800bf580 = MEMORY.ref(4, 0x800bf580L);
  public static final Value _800bf584 = MEMORY.ref(4, 0x800bf584L);
  public static final Value _800bf588 = MEMORY.ref(4, 0x800bf588L);
  public static final Value _800bf58c = MEMORY.ref(4, 0x800bf58cL);
  public static final Value _800bf590 = MEMORY.ref(4, 0x800bf590L);
  public static final Value _800bf594 = MEMORY.ref(4, 0x800bf594L);
  public static final Pointer<RunnableRef> cdromDmaSubCallback_800bf598 = MEMORY.ref(4, 0x800bf598L, Pointer.of(4, RunnableRef::new));
  public static final Value _800bf59c = MEMORY.ref(4, 0x800bf59cL);
  public static final Value _800bf5a0 = MEMORY.ref(4, 0x800bf5a0L);

  public static final Value _800bf5b0 = MEMORY.ref(4, 0x800bf5b0L);
  public static final Value _800bf5b4 = MEMORY.ref(4, 0x800bf5b4L);

  public static final ArrayRef<ByteRef> cdromResponses_800bf5c0 = MEMORY.ref(8, 0x800bf5c0L, ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));
  public static final ArrayRef<ByteRef> cdromResponses_800bf5c8 = MEMORY.ref(8, 0x800bf5c8L, ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));
  public static final ArrayRef<ByteRef> cdromResponses_800bf5d0 = MEMORY.ref(8, 0x800bf5d0L, ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));

  public static final Pointer<CString> _800bf5e0 = MEMORY.ref(4, 0x800bf5e0L, Pointer.of(4, CString::new));

  public static final BoolRef sectorIsDataOnly_800bf5f0 = MEMORY.ref(4, 0x800bf5f0L, BoolRef::new);

  public static final Response cdromResponse_800bf5f8 = MEMORY.ref(4, 0x800bf5f8L, Response::new);
  public static final Value batch_800bf608 = MEMORY.ref(4, 0x800bf608L);
  public static final EnumRef<SyncCode> syncCode_800bf60c = MEMORY.ref(1, 0x800bf60cL, EnumRef.of(SyncCode.values()));
  public static final Value response_800bf60d = MEMORY.ref(1, 0x800bf60dL);

  public static final Value batch_800bf618 = MEMORY.ref(4, 0x800bf618L);
  public static final EnumRef<SyncCode> syncCode_800bf61c = MEMORY.ref(1, 0x800bf61cL, EnumRef.of(SyncCode.values()));
  public static final Value response_800bf61d = MEMORY.ref(1, 0x800bf61dL);

  public static final Value batch_800bf628 = MEMORY.ref(4, 0x800bf628L);
  public static final EnumRef<SyncCode> syncCode_800bf62c = MEMORY.ref(1, 0x800bf62cL, EnumRef.of(SyncCode.values()));
  public static final Value response_800bf62d = MEMORY.ref(1, 0x800bf62dL);

  // Command buffer
  // Repeats 8 times in memory
  public static final ArrayRef<CdlPacket> CdlPacket_800bf638 = MEMORY.ref(4, 0x800bf638L, ArrayRef.of(CdlPacket.class, DSL_MAX_COMMAND, 0x18, CdlPacket::new));

  public static final Value cdromParamsPtr_800bf644 = MEMORY.ref(4, 0x800bf644L); //TODO figure out a way to remove refs to this
  //

  public static final Value _800bf6f8 = MEMORY.ref(4, 0x800bf6f8L);
  public static final Value _800bf6fc = MEMORY.ref(4, 0x800bf6fcL);
  public static final Value cdlPacketIndex_800bf700 = MEMORY.ref(4, 0x800bf700L);

  public static final ArrayRef<Response> cdromResponseBuffer_800bf708 = MEMORY.ref(4, 0x800bf708L, ArrayRef.of(Response.class, DSL_MAX_COMMAND, 0x10, Response::new));
  public static final Value cdromResponseBufferIndex_800bf788 = MEMORY.ref(4, 0x800bf788L);

  public static final Pointer<RunnableRef> _800bf798 = MEMORY.ref(4, 0x800bf798L, Pointer.of(4, RunnableRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> _800bf79c = MEMORY.ref(4, 0x800bf79cL, Pointer.of(4, BiConsumerRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> cdromReadCompleteSubCallbackPtr_800bf7a0 = MEMORY.ref(4, 0x800bf7a0L, Pointer.of(4, BiConsumerRef::new));
  public static final Pointer<BiConsumerRef<SyncCode, byte[]>> _800bf7a4 = MEMORY.ref(4, 0x800bf7a4L, Pointer.of(4, BiConsumerRef::new));
  /**
   * CD file list
   *
   * 0x800bf7a8-0x800bfda8
   */
  public static final ArrayRef<CdlFILE> CdlFILE_800bf7a8 = MEMORY.ref(1, 0x800bf7a8L, ArrayRef.of(CdlFILE.class, 40, 24, CdlFILE::new));

  /**
   * TODO Seems to be a pointer to the CdlLOC of file #62. Not sure why.
   */
  public static final Value _800bfd84 = MEMORY.ref(4, 0x800bfd84L);
  //

  public static final ArrayRef<CdlDIR> CdlDIR_800bfda8 = MEMORY.ref(1600, 0x800bfda8L, ArrayRef.of(CdlDIR.class, 0x80, 0x2c, CdlDIR::new));

  public static final Value _800bfdd8 = MEMORY.ref(4, 0x800bfdd8L);
}
