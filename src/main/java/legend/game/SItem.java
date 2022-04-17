package legend.game;

import legend.core.DebugHelper;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.Drgn0_6666Struct58;
import legend.game.types.MemcardDataStruct3c;
import legend.game.types.MemcardStruct28;
import legend.game.types.MenuStruct08;
import legend.game.types.MenuStruct20;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

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
import static legend.game.Scus94491BpeSegment_8002.unloadDrgn0_6666Struct;
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
import static legend.game.Scus94491BpeSegment_8004.setMonoOrStereo;
import static legend.game.Scus94491BpeSegment_8004._8004dd30;
import static legend.game.Scus94491BpeSegment_8004._8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._8005a368;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_800b._800babc8;
import static legend.game.Scus94491BpeSegment_800b._800bac5c;
import static legend.game.Scus94491BpeSegment_800b._800bac6c;
import static legend.game.Scus94491BpeSegment_800b._800bac70;
import static legend.game.Scus94491BpeSegment_800b._800bad64;
import static legend.game.Scus94491BpeSegment_800b._800badac;
import static legend.game.Scus94491BpeSegment_800b._800badae;
import static legend.game.Scus94491BpeSegment_800b._800badb0;
import static legend.game.Scus94491BpeSegment_800b._800bae00;
import static legend.game.Scus94491BpeSegment_800b._800baef4;
import static legend.game.Scus94491BpeSegment_800b._800baefc;
import static legend.game.Scus94491BpeSegment_800b._800bb0aa;
import static legend.game.Scus94491BpeSegment_800b._800bb0ac;
import static legend.game.Scus94491BpeSegment_800b._800bb0b0;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800be5fe;
import static legend.game.Scus94491BpeSegment_800b._800be600;
import static legend.game.Scus94491BpeSegment_800b._800be606;
import static legend.game.Scus94491BpeSegment_800b._800be607;
import static legend.game.Scus94491BpeSegment_800b._800be65e;
import static legend.game.Scus94491BpeSegment_800b._800be666;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666StructPtr_800bdc20;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666Ptr_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666Ptr_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666Ptr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666Ptr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b._800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666StructPtr_800bdbe0;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666StructPtr_800bdbe4;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666StructPtr_800bdbe8;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666StructPtr_800bdbec;
import static legend.game.Scus94491BpeSegment_800b._800bdbf8;
import static legend.game.Scus94491BpeSegment_800b._800bdc2c;
import static legend.game.Scus94491BpeSegment_800b._800bdc30;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666Ptr_800bdc5c;
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

  /** String: "BASCUS-94491drgnpda" */
  public static final Value drgnpda_800fb7a0 = MEMORY.ref(1, 0x800fb7a0L);

  public static final ArrayRef<MenuStruct08> _800fba7c = MEMORY.ref(4, 0x800fba7cL, ArrayRef.of(MenuStruct08.class, 8, 8, MenuStruct08::new));

  public static final Value _800fbabc = MEMORY.ref(4, 0x800fbabcL);

  /** String "*" */
  public static final Value asterisk_800fbadc = MEMORY.ref(1, 0x800fbadcL);

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

  public static final Value _80114180 = MEMORY.ref(1, 0x80114180L);

  public static final Value _801141c4 = MEMORY.ref(1, 0x801141c4L);

  public static final Value _80114258 = MEMORY.ref(1, 0x80114258L);

  public static final Value _801142d4 = MEMORY.ref(1, 0x801142d4L);

  public static final ArrayRef<Pointer<ArrayRef<UnsignedShortRef>>> _80114248 = MEMORY.ref(4, 0x80114248L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(UnsignedShortRef.class)), 4, 4, Pointer.deferred(4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new))));

  public static final ArrayRef<Pointer<ArrayRef<UnsignedShortRef>>> _801142dc = MEMORY.ref(4, 0x801142dcL, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(UnsignedShortRef.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new))));

  public static final ArrayRef<Pointer<ArrayRef<UnsignedShortRef>>> _8011c108 = MEMORY.ref(4, 0x8011c108L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(UnsignedShortRef.class)), 57, 4, Pointer.deferred(4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new))));
  public static final ArrayRef<Pointer<ArrayRef<UnsignedShortRef>>> _8011c1ec = MEMORY.ref(4, 0x8011c1ecL, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(UnsignedShortRef.class)), 8, 4, Pointer.deferred(4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new))));

  // Note: these all overlap, not sure if they need to be the full length or not
  public static final ArrayRef<UnsignedShortRef> _8011c20c = MEMORY.ref(2, 0x8011c20cL, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c214 = MEMORY.ref(2, 0x8011c214L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c370 = MEMORY.ref(2, 0x8011c370L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c3a4 = MEMORY.ref(2, 0x8011c3a4L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c3bc = MEMORY.ref(2, 0x8011c3bcL, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c5d8 = MEMORY.ref(2, 0x8011c5d8L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c664 = MEMORY.ref(2, 0x8011c664L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c668 = MEMORY.ref(2, 0x8011c668L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c66c = MEMORY.ref(2, 0x8011c66cL, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c788 = MEMORY.ref(2, 0x8011c788L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c7d8 = MEMORY.ref(2, 0x8011c7d8L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c88c = MEMORY.ref(2, 0x8011c88cL, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c8bc = MEMORY.ref(2, 0x8011c8bcL, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c8c8 = MEMORY.ref(2, 0x8011c8c8L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c8cc = MEMORY.ref(2, 0x8011c8ccL, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c8d4 = MEMORY.ref(2, 0x8011c8d4L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c914 = MEMORY.ref(2, 0x8011c914L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c93c = MEMORY.ref(2, 0x8011c93cL, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c9c8 = MEMORY.ref(2, 0x8011c9c8L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011c9e8 = MEMORY.ref(2, 0x8011c9e8L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ca08 = MEMORY.ref(2, 0x8011ca08L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ca2c = MEMORY.ref(2, 0x8011ca2cL, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cab0 = MEMORY.ref(2, 0x8011cab0, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cb2c = MEMORY.ref(2, 0x8011cb2c, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cb38 = MEMORY.ref(2, 0x8011cb38, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cb9c = MEMORY.ref(2, 0x8011cb9c, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cbc4 = MEMORY.ref(2, 0x8011cbc4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cc30 = MEMORY.ref(2, 0x8011cc30, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cc90 = MEMORY.ref(2, 0x8011cc90, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ccb8 = MEMORY.ref(2, 0x8011ccb8, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cd28 = MEMORY.ref(2, 0x8011cd28, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cd58 = MEMORY.ref(2, 0x8011cd58, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cdd8 = MEMORY.ref(2, 0x8011cdd8, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cdfc = MEMORY.ref(2, 0x8011cdfc, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ce00 = MEMORY.ref(2, 0x8011ce00, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ce04 = MEMORY.ref(2, 0x8011ce04, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ce08 = MEMORY.ref(2, 0x8011ce08, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ce0c = MEMORY.ref(2, 0x8011ce0c, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ce10 = MEMORY.ref(2, 0x8011ce10, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ce14 = MEMORY.ref(2, 0x8011ce14, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ce18 = MEMORY.ref(2, 0x8011ce18, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ceb4 = MEMORY.ref(2, 0x8011ceb4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cec4 = MEMORY.ref(2, 0x8011cec4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011ced0 = MEMORY.ref(2, 0x8011ced0, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cedc = MEMORY.ref(2, 0x8011cedc, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cef0 = MEMORY.ref(2, 0x8011cef0, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cf00 = MEMORY.ref(2, 0x8011cf00, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cf10 = MEMORY.ref(2, 0x8011cf10, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cf1c = MEMORY.ref(2, 0x8011cf1c, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cf2c = MEMORY.ref(2, 0x8011cf2c, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cf3c = MEMORY.ref(2, 0x8011cf3c, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cf48 = MEMORY.ref(2, 0x8011cf48, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011cf54 = MEMORY.ref(2, 0x8011cf54, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d044 = MEMORY.ref(2, 0x8011d044, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d048 = MEMORY.ref(2, 0x8011d048, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d04c = MEMORY.ref(2, 0x8011d04c, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d060 = MEMORY.ref(2, 0x8011d060, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d5c4 = MEMORY.ref(2, 0x8011d5c4, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d700 = MEMORY.ref(2, 0x8011d700, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d704 = MEMORY.ref(2, 0x8011d704, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d708 = MEMORY.ref(2, 0x8011d708, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d70c = MEMORY.ref(2, 0x8011d70c, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _8011d710 = MEMORY.ref(2, 0x8011d710, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));

  public static final ArrayRef<Pointer<Drgn0_6666Struct58>> _8011d718 = MEMORY.ref(4, 0x8011d718L, ArrayRef.of(Pointer.classFor(Drgn0_6666Struct58.class), 7, 4, Pointer.deferred(4, Drgn0_6666Struct58::new)));

  public static final Value _8011d734 = MEMORY.ref(4, 0x8011d734L);
  public static final Value _8011d738 = MEMORY.ref(4, 0x8011d738L);
  public static final Value _8011d73c = MEMORY.ref(4, 0x8011d73cL);
  public static final Value _8011d740 = MEMORY.ref(4, 0x8011d740L);
  public static final Value _8011d744 = MEMORY.ref(4, 0x8011d744L);
  public static final Value _8011d748 = MEMORY.ref(4, 0x8011d748L);
  public static final Value _8011d74c = MEMORY.ref(4, 0x8011d74cL);
  public static final Value _8011d750 = MEMORY.ref(4, 0x8011d750L);
  public static final Value _8011d754 = MEMORY.ref(4, 0x8011d754L);

  public static final Value _8011d768 = MEMORY.ref(4, 0x8011d768L);

  public static final Value _8011d788 = MEMORY.ref(1, 0x8011d788L);

  public static final ArrayRef<UnsignedShortRef> _8011d790 = MEMORY.ref(2, 0x8011d790L, ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new));

  public static final Value fileCount_8011d7b8 = MEMORY.ref(4, 0x8011d7b8L);
  public static final Value _8011d7bc = MEMORY.ref(4, 0x8011d7bcL);

  public static final Value _8011d7c4 = MEMORY.ref(1, 0x8011d7c4L);

  public static final Value _8011d7c8 = MEMORY.ref(1, 0x8011d7c8L);

  public static final Value _8011dc88 = MEMORY.ref(1, 0x8011dc88L);

  public static final Value _8011dc8c = MEMORY.ref(4, 0x8011dc8cL);
  public static final MenuStruct20 _8011dc90 = MEMORY.ref(4, 0x8011dc90L, MenuStruct20::new);

  public static final Value _8011dcb8 = MEMORY.ref(4, 0x8011dcb8L);
  public static final Value _8011dcbc = MEMORY.ref(4, 0x8011dcbcL);
  public static final Value _8011dcc0 = MEMORY.ref(4, 0x8011dcc0L);

  public static final BoolRef _8011dcfc = MEMORY.ref(1, 0x8011dcfcL, BoolRef::new);

  public static final Value _8011dd00 = MEMORY.ref(4, 0x8011dd00L);
  public static final Value _8011dd04 = MEMORY.ref(4, 0x8011dd04L);
  public static final Value _8011dd08 = MEMORY.ref(4, 0x8011dd08L);

  public static final Value memcardData_8011dd10 = MEMORY.ref(4, 0x8011dd10L);

  public static final Value memcardDataPtr_8011e0b0 = MEMORY.ref(4, 0x8011e0b0L);
  public static final Pointer<ArrayRef<MemcardStruct28>> _8011e0b4 = MEMORY.ref(4, 0x8011e0b4L, Pointer.of(4, ArrayRef.of(MemcardStruct28.class, 0x10, 0x28, MemcardStruct28::new)));
  public static final IntRef memcardSaveIndex_8011e0b8 = MEMORY.ref(4, 0x8011e0b8L, IntRef::new);
  public static final Value _8011e0bc = MEMORY.ref(4, 0x8011e0bcL);
  public static final Value memcardSaveCount_8011e0c0 = MEMORY.ref(4, 0x8011e0c0L);
  public static final Value _8011e0c4 = MEMORY.ref(1, 0x8011e0c4L);
  public static final Value _8011e0c5 = MEMORY.ref(1, 0x8011e0c5L);
  public static final Value _8011e0c6 = MEMORY.ref(1, 0x8011e0c6L);

  public static final Value _8011e0c8 = MEMORY.ref(4, 0x8011e0c8L);
  public static final Value _8011e0cc = MEMORY.ref(4, 0x8011e0ccL);
  public static final Value _8011e0d0 = MEMORY.ref(1, 0x8011e0d0L);

  public static final Value memcardSaveLoadingStage_8011e0d4 = MEMORY.ref(1, 0x8011e0d4L);

  public static final Value _8011e1e8 = MEMORY.ref(4, 0x8011e1e8L);

  @Method(0x800fc698L)
  public static long FUN_800fc698(final long a0) {
    if((int)a0 == -0x1L || a0 >= 9) {
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
  public static Drgn0_6666Struct58 FUN_800fc900(final long a0) {
    assert false;
    return null;
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
      FUN_800127cc(a0.getAddress(), 0, 0x1L);
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
    if(!drgn0_6666Ptr_800bdba4.isNull()) {
      FUN_801033e8(drgn0_6666Ptr_800bdba4.deref());
      drgn0_6666Ptr_800bdba4.clear();
    }

    //LAB_800fca40
    if(!drgn0_6666Ptr_800bdba8.isNull()) {
      FUN_801033e8(drgn0_6666Ptr_800bdba8.deref());
      drgn0_6666Ptr_800bdba8.clear();
    }

    //LAB_800fca60
    if(!drgn0_6666Ptr_800bdb94.isNull()) {
      FUN_801033e8(drgn0_6666Ptr_800bdb94.deref());
      drgn0_6666Ptr_800bdb94.clear();
    }

    //LAB_800fca80
    if(!drgn0_6666Ptr_800bdb98.isNull()) {
      FUN_801033e8(drgn0_6666Ptr_800bdb98.deref());
      drgn0_6666Ptr_800bdb98.clear();
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
        drgn0_6666Ptr_800bdc5c.clear();
        _8011dc90._0c.set(0);
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
        drgn0_6666StructPtr_800bdbe0.set(FUN_80103818(0x73L, 0x73L, 0x1dL, FUN_800fc78c(_8011d738.get())));
        FUN_80104b60(drgn0_6666StructPtr_800bdbe0.deref());
        FUN_80101d10(_8011d738.get(), 0x4L, 0xffL);
        inventoryMenuState_800bdc28.setu(0x4L);
        break;

      case 0x4:
        if(FUN_8010ecec(_8011dc90) != 0) {
          if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
            playSound(0x3L);
            FUN_800fca0c(0x7dL, 0x1L);
          }

          //LAB_800fcdd4
          if(FUN_801040c0(_8011d738.getAddress(), 0x7L) != 0) {
            _8011d73c.setu(0);
            drgn0_6666StructPtr_800bdbe0.deref().y_44.set(FUN_800fc78c(_8011d738.get()));
          }

          //LAB_800fce08
          v1 = inventoryJoypadInput_800bdc44.get();

          if((v1 & 0x2020L) != 0) {
            if((v1 & 0x2000L) != 0) {
              v1 = _8011d738.get() + 0xaL;
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
                drgn0_6666StructPtr_800bdbe4.set(FUN_800fc900(_8011d73c.get()));
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
                drgn0_6666StructPtr_800bdbe4.clear();
                FUN_8010f130(null, 0x1);
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
          unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbe4.deref());
        }

        //LAB_800fcfc0
        if(FUN_801040c0(_8011d73c.getAddress(), 0x4L) != 0) {
          drgn0_6666StructPtr_800bdbe4.deref().y_44.set(FUN_800fc7a4(_8011d73c.get()) - 0x2L);
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
        FUN_8010ecec(_8011dc90);

        if(_8011dc90._10.get() >= 0x2L) {
          if(drgn0_6666StructPtr_800bdbe4.isNull()) {
            drgn0_6666StructPtr_800bdbe4.set(FUN_80103818(0x74L, 0x74L, FUN_800fc7bc(0) - 0x22L, FUN_800fc7ec(0) - 0x2L));
            FUN_80104b60(drgn0_6666StructPtr_800bdbe4.deref());
            drgn0_6666StructPtr_800bdbe4.deref()._3c.set(0x20);
          }

          //LAB_800fd100
          if(FUN_801040c0(_8011d73c.getAddress(), 0x4L) != 0) {
            drgn0_6666StructPtr_800bdbe4.deref().y_44.set(FUN_800fc7ec(_8011d73c.get()) - 0x2L);
          }

          //LAB_800fd130
          if((joypadPress_8007a398.get() & 0x8000L) != 0) {
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
              setMonoOrStereo(0);
            } else if(a0 == 0x2L) {
              //LAB_800fd1b8
              _800bb0aa.setu(0);
            } else if(a0 == 0x3L) {
              //LAB_800fd1c4
              if(_800bb0b0.get() != 0) {
                _800bb0b0.subu(0x1L);
              }
            }
          }

          //LAB_800fd1e0
          //LAB_800fd1e4
          if((joypadPress_8007a398.get() & 0x2000L) != 0) {
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
              setMonoOrStereo(0x1L);
            } else if(v1 == 0x2L) {
              //LAB_800fd290
              _800bb0aa.setu(0x1L);
            } else if(v1 == 0x3L) {
              //LAB_800fd29c
              if(_800bb0b0.get() < 0x2L) {
                _800bb0b0.addu(0x1L);
              }
            }
          }

          //LAB_800fd2bc
          //LAB_800fd2c0
          if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
            playSound(0x2L);
            inventoryMenuState_800bdc28.setu(0x4L);
            _8011dc90._0c.incr();
            unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbe4.deref());
          }

          //LAB_800fd30c
          FUN_8010214c(_8011d73c.get(), vibrationEnabled_800bb0a9.get(), mono_800bb0a8.get(), _800bb0aa.get(), _800bb0b0.get());
        }

        //LAB_800fd330
        FUN_80102484(0);

        //LAB_800fd344
        FUN_80101d10(_8011d738.get(), 0x4L, 0);
        break;

      case 0x7:
        FUN_8002437c(0xffL);
        FUN_8010376c(_80114130.getAddress(), 0, 0);
        drgn0_6666StructPtr_800bdbe0.set(FUN_80103818(0x73L, 0x73L, 0x1dL, FUN_800fc78c(_8011d738.get())));
        drgn0_6666StructPtr_800bdbe4.set(FUN_800fc900(_8011d73c.get()));
        FUN_80104b60(drgn0_6666StructPtr_800bdbe0.deref());
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
        drgn0_6666StructPtr_800bdbe8.set(FUN_80103818(0x7fL, 0x7fL, 0x10L, FUN_800fc84c(_8011d740.get())));
        FUN_80104b60(drgn0_6666StructPtr_800bdbe8.deref());
        FUN_801024c4(0xffL);
        inventoryMenuState_800bdc28.setu(0xaL);
        break;

      case 0xa:
        FUN_801024c4(0);

        if(_800bb168.get() != 0) {
          break;
        }

        if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0 && _8011d740.get() >= 0x2L) {
          _8011d740.subu(0x1L);
          drgn0_6666StructPtr_800bdbe8.deref().y_44.set(FUN_800fc84c(_8011d740.get()));
          playSound(0x1L);
        }

        //LAB_800fd4e4
        //LAB_800fd4e8
        if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0 && _8011d740.get() < 0x2L) {
          _8011d740.addu(0x1L);
          drgn0_6666StructPtr_800bdbe8.deref().y_44.set(FUN_800fc84c(_8011d740.get()));
          playSound(0x1L);
        }

        //LAB_800fd52c
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          v1 = _800babc8.getAddress();
          v0 = _800babc8.offset(_8011d740.get() * 0x4L).getAddress();
          a0 = MEMORY.ref(4, v0).offset(0x88L).get();
          if((int)a0 == -0x1L || (MEMORY.ref(4, v1).offset(0x330L).offset(a0 * 0x2cL).get() & 0x20L) != 0) {
            //LAB_800fd590
            playSound(0x28L);
          } else {
            //LAB_800fd5a0
            playSound(0x2L);
            drgn0_6666StructPtr_800bdbec.set(FUN_80103818(0x80L, 0x80L, FUN_800fc880(_8011d744.get()), FUN_800fc8a8(_8011d744.get())));
            FUN_80104b60(drgn0_6666StructPtr_800bdbec.deref());
            inventoryMenuState_800bdc28.setu(0xbL);
          }
        }

        //LAB_800fd5f4
        //LAB_800fd5f8
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          if(whichMenu_800bdc38.get() != 0x18L) {
            a0 = 0x2L;
          } else {
            a0 = 0x7dL;
          }

          //LAB_800fd62c
          FUN_800fca0c(a0, 0x5L);
        }

        break;

      case 0xb:
        FUN_801024c4(0);

        if((inventoryJoypadInput_800bdc44.get() & 0x8000L) != 0 && _8011d744.get() % 3 > 0) {
          _8011d744.subu(0x1L);
          drgn0_6666StructPtr_800bdbec.deref().x_40.set(FUN_800fc880(_8011d744.get()));
          drgn0_6666StructPtr_800bdbec.deref().y_44.set(FUN_800fc8a8(_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd6b8
        if((inventoryJoypadInput_800bdc44.get() & 0x2000L) != 0 && _8011d744.get() % 3 < 0x2L) {
          _8011d744.addu(0x1L);
          drgn0_6666StructPtr_800bdbec.deref().x_40.set(FUN_800fc880(_8011d744.get()));
          drgn0_6666StructPtr_800bdbec.deref().y_44.set(FUN_800fc8a8(_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd730
        if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0 && _8011d744.get() >= 0x3L) {
          _8011d744.subu(0x3L);
          drgn0_6666StructPtr_800bdbec.deref().x_40.set(FUN_800fc880(_8011d744.get()));
          drgn0_6666StructPtr_800bdbec.deref().y_44.set(FUN_800fc8a8(_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd78c
        //LAB_800fd790
        if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0 && _8011d744.get() < 0x3L) {
          _8011d744.addu(0x3L);
          drgn0_6666StructPtr_800bdbec.deref().x_40.set(FUN_800fc880(_8011d744.get()));
          drgn0_6666StructPtr_800bdbec.deref().y_44.set(FUN_800fc8a8(_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd7e4
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbec.deref());
          inventoryMenuState_800bdc28.setu(0xaL);
        }

        //LAB_800fd820
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          v1 = _800bdbf8.offset(_8011d744.get() * 0x4L).get();
          if((int)v1 == -0x1L) {
            //LAB_800fd888
            playSound(0x28L);
            break;
          }

          if((_800babc8.offset(v1 * 0x2cL).offset(0x330L).get() & 0x2L) == 0) {
            //LAB_800fd888
            playSound(0x28L);
            break;
          }

          //LAB_800fd898
          playSound(0x2L);
          a3 = _800babc8.offset(_8011d740.get() * 0x4L).offset(0x88L).get();
          a2 = _800bdbf8.offset(_8011d744.get() * 0x4L).get();
          inventoryMenuState_800bdc28.setu(0x9L);
          _800babc8.offset(_8011d740.get() * 0x4L).offset(0x88L).setu(a2);
          _800bdbf8.offset(_8011d744.get() * 0x4L).setu(a3);
        }

        break;

      case 0xc:
        FUN_80023148();
        scriptStartEffect(0x2L, 0xaL);
        FUN_8002437c(0xffL);

      case 0xd:
        _8011d744.setu(0);
        _8011d740.setu(0);

      case 0xe:
        FUN_8002437c(0);
        FUN_8010376c(_80114180.getAddress(), 0, 0);

        if(drgn0_6666StructPtr_800bdbe8.isNull()) {
          drgn0_6666StructPtr_800bdbe8.set(FUN_80103818(0x79L, 0x79L, FUN_800fc824(0x1L), 0));
          FUN_80104b60(drgn0_6666StructPtr_800bdbe8.deref());
        }

        //LAB_800fd964
        drgn0_6666StructPtr_800bdbe8.deref().y_44.set(FUN_800fc804(_8011d740.get()));
        _8011d750.setu(FUN_801045fc(_800bdbb8.offset(_8011d734.get() * 0x4L).get()));
        FUN_80102660(_8011d734.get(), _8011d740.get(), _8011d744.get(), 0xffL);
        inventoryMenuState_800bdc28.setu(0xfL);
        break;

      case 0xf:
        FUN_801034cc(_8011d734.get(), _8011d7c4.get());
        FUN_80102660(_8011d734.get(), _8011d740.get(), _8011d744.get(), 0);

        if(_800bb168.get() != 0) {
          break;
        }

        if(FUN_80103f00(_8011d740.getAddress(), _8011d744.getAddress(), 0x4L, _8011d750.get(), 0x1L) != 0) {
          drgn0_6666StructPtr_800bdbe8.deref().y_44.set(FUN_800fc804(_8011d740.get()));
        }

        //LAB_800fda58
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          FUN_800fca0c(0x2L, 0x6L);
        }

        //LAB_800fda80
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          a0 = _8011d740.get() + _8011d744.get();
          if((int)a0 < _800badac.getSigned()) {
            v1 = _8011d7c8.offset(a0 * 0x4L).get();
            if(v1 != 0xffL) {
              v0 = FUN_80103a5c(v1, _800bdbb8.offset(_8011d734.get() * 0x4L).get());
              FUN_800233d8(_8011d7c8.offset((_8011d740.get() + _8011d744.get()) * 0x4L).offset(0x1L).get());
              FUN_80023484(v0 & 0xffffL);
              playSound(0x2L);
              FUN_80110030(0);
              FUN_80022b50(_800bdbb8.offset(1, _8011d734.get() * 0x4L).get(), 0);
              FUN_80022c08(_800bdbb8.offset(1, _8011d734.get() * 0x4L).get(), 0);
              inventoryMenuState_800bdc28.setu(0xeL);
            } else {
              //LAB_800fdb6c
              playSound(0x28L);
            }
          }
        }

        //LAB_800fdb74
        if((inventoryJoypadInput_800bdc44.get() & 0x10L) != 0) {
          playSound(0x2L);
          _8011dcb8.setu(addToLinkedListTail(0x4c0L));
          _8011dcbc.setu(addToLinkedListTail(0x4c0L));
          _8011d754.setu(FUN_80104738(0x1L));
          FUN_80023a2c(_8011dcb8.get(), _800badb0.getAddress(), _800badac.getSigned());
          removeFromLinkedList(_8011dcb8.get());
          removeFromLinkedList(_8011dcbc.get());
          MEMORY.ref(4, 0x800bdc28L).setu(0xeL);
        }

        //LAB_800fdc18
        if(FUN_8010415c(_8011d734.getAddress(), _8011d7c4.get()) != 0) {
          inventoryMenuState_800bdc28.setu(0xdL);
        }

        break;

      case 0x10:
      case 0x1f:
        scriptStartEffect(0x2L, 0xaL);
        FUN_8002437c(0xffL);
        FUN_8010376c(_801141c4.getAddress(), 0, 0);
        _8011dcb8.setu(addToLinkedListTail(0x4c0L));
        _8011dcbc.setu(addToLinkedListTail(0x4c0L));
        FUN_80023148();
        _8011d734.setu(0);
        _8011d740.setu(0);
        _8011d744.setu(0);
        _8011d748.setu(0);
        _8011d74c.setu(0);
        drgn0_6666StructPtr_800bdbe8.set(FUN_80103818(0x76L, 0x76L, FUN_800fc824(0), FUN_800fc814(_8011d740.get()) + 0x20L));
        FUN_80104b60(drgn0_6666StructPtr_800bdbe8.deref());
        FUN_80102840(_8011d744.get(), _8011d748.get(), 0xffL, 0xffL);

        if(inventoryMenuState_800bdc28.get() != 0x10L) {
          a0 = 0x20L;
        } else {
          a0 = 0x11L;
        }

        //LAB_800fdd24
        inventoryMenuState_800bdc28.setu(a0);
        break;

      case 0x11:
        _8011d754.setu(FUN_80104738(0));
        inventoryMenuState_800bdc28.setu(0x12L);

        //LAB_800fe08c
        FUN_80102840(_8011d744.get(), _8011d748.get(), _8011dcb8.offset(_8011d734.get() * 0x4L).deref(1).offset((_8011d740.get() + _8011d744.get()) * 0x4L).get(), 0);
        break;

      case 0x20:
        _8011d754.setu(FUN_80104738(0x1L));
        inventoryMenuState_800bdc28.setu(0x21L);

        //LAB_800fe08c
        FUN_80102840(_8011d744.get(), _8011d748.get(), _8011dcb8.offset(_8011d734.get() * 0x4L).deref(1).offset((_8011d740.get() + _8011d748.get()) * 0x4L).get(), 0);
        break;

      case 0x12:
      case 0x21:
        if(_8011d734.get() != 0) {
          a0 = _800badae.getSigned();
        } else {
          //LAB_800fddf4
          a0 = _800badac.getSigned() + _8011d754.get();
        }

        //LAB_800fde10
        _8011d750.setu(a0);
        if(_8011d734.get() != 0) {
          s1 = _8011d740.get() + _8011d748.get();
        } else {
          //LAB_800fde38
          s1 = _8011d740.get() + _8011d744.get();
        }

        //LAB_800fde50
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) == 0) {
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
                drgn0_6666StructPtr_800bdc20.set(FUN_80103818(a0, a1, a2, a3));
                FUN_80104b60(drgn0_6666StructPtr_800bdc20.deref());
                inventoryMenuState_800bdc28.setu(0x22L);
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
        if(_8011d734.get() != 0) {
          s1 = _8011d740.get() + _8011d748.get();
        } else {
          //LAB_800fe0dc
          s1 =  _8011d740.get() + _8011d744.get();
        }

        //LAB_800fe0f0
        FUN_80103cc4(_8011c8d4, 0xc0L, 0xb4L, 0x4L);
        v0 = FUN_800fc860(0) + 0x2L;
        a2 = v0 & 0xffffL;
        s2 = _8011d74c.getAddress();

        if(_8011d74c.get() != 0) {
          a3 = 0x6L;
        } else {
          a3 = 0x5L;
        }

        //LAB_800fe13c
        FUN_80103e90(_8011c20c, 0x148L, a2, a3);
        v0 = FUN_800fc860(0x1L) + 0x2L;
        a2 = v0 & 0xffffL;

        if(_8011d74c.get() != 0) {
          a3 = 0x5L;
        } else {
          a3 = 0x6L;
        }

        //LAB_800fe170
        FUN_80103e90(_8011c214, 0x148L, a2, a3);
        a0 = s2;
        v0 = FUN_801041d8(a0);
        v1 = v0;
        if(v1 == 0x1L) {
          //LAB_800fe1bc
          a0 = _8011d74c.get();

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

          //LAB_800fe29c
          unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdc20.deref());
          inventoryMenuState_800bdc28.setu(0x21L);
        } else if(v1 == 0x4L) {
          //LAB_800fe29c
          unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdc20.deref());
          inventoryMenuState_800bdc28.setu(0x21L);
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
          drgn0_6666StructPtr_800bdbe8.set(FUN_80103818(a0, a1, a2, a3));
          FUN_80104b60(drgn0_6666StructPtr_800bdbe8.deref());
        }

        //LAB_800fe490
        a0 = 0x45L;
        a1 = a0;
        a2 = 0;
        a3 = a2;
        FUN_80103818(a0, a1, a2, a3);
        a0 = 0x46L;
        a1 = a0;
        a2 = 0xc0L;
        a3 = 0;
        FUN_80103818(a0, a1, a2, a3);
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
        if(v0 != 0) {
          inventoryMenuState_800bdc28.setu(0x18L);
          unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbe8.deref());
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
        if(v0 != 0) {
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
            unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbe8.deref());
            inventoryMenuState_800bdc28.setu(0x18L);
          } else {
            //LAB_800fe680
            playSound(0x28L);
          }
        }

        s0 = 0x8012_0000L;

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
        drgn0_6666StructPtr_800bdbec.set(FUN_80103818(a0, a1, a2, a3));
        FUN_80104b60(drgn0_6666StructPtr_800bdbec.deref());

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
        v0 = FUN_8010ecec(_8011dc90);
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

            if((MEMORY.ref(2, s0).offset((_8011d740.get() + _8011d744.get()) * 0x4L).offset(0x2L).get() & 0x4000L) == 0) {
              if((a0 & 0x2L) != 0) {
                //LAB_800fe8b0
                for(int i = 0; i < 7; i++) {
                  _8011d718.get(i).set(FUN_80103818(0x7eL, 0x7eL, FUN_800fc8c0(i), 0x6eL));
                  FUN_80104b60(_8011d718.get(i).deref());
                }
              } else {
                //LAB_800fe8f0
                drgn0_6666StructPtr_800bdbe8.set(FUN_80103818(0x7eL, 0x7eL, FUN_800fc8c0(_8011d734.get()), 0x6eL));
                FUN_80104b60(drgn0_6666StructPtr_800bdbe8.deref());
              }

              //LAB_800fe924
              playSound(0x2L);
              inventoryMenuState_800bdc28.setu(0x1eL);
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
            unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbe8.deref());
          } else {
            //LAB_800fe9dc
            for(int i = 0; i < 7; i++) {
              unloadDrgn0_6666Struct(_8011d718.get(i).deref());
            }
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
          playSound(0x2L);
          FUN_800232dc(_8011d7c8.offset((_8011d740.get() + _8011d744.get()) * 0x4L).get());
          s0 = _8011d788.getAddress();
          _8011d750.setu(FUN_80104448());
          FUN_80110030(0);
          FUN_80104324(_8011d788.getAddress());
          FUN_8010f130(_8011d790, 0);
          inventoryMenuState_800bdc28.setu(0x1bL);
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
        drgn0_6666StructPtr_800bdbe8.set(FUN_80103818(a0, a1, a2, a3));
        FUN_80104b60(drgn0_6666StructPtr_800bdbe8.deref());
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
          FUN_8010f130(_8011c370, 0x2);
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
        v1 = FUN_8010ecec(_8011dc90);
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
        drgn0_6666Ptr_800bdb98.clear();
        drgn0_6666Ptr_800bdb94.clear();
        FUN_8010f130(_8011c93c, 0x1);
        memcardSaveLoadingStage_8011e0d4.setu(0x1L);
        _8011d744.setu(0);
        _8011d740.setu(0);
        FUN_801030c0(0, 0, 0);
        inventoryMenuState_800bdc28.setu(0x29L);
        break;

      case 0x29: // Part of load game menu
        FUN_8010ecec(_8011dc90);
        FUN_801030c0(_8011d744.get(), 0, 0);

        if(_8011dc90._10.get() < 0x3L) {
          break;
        }

        if(_800bb168.get() != 0) {
          break;
        }

        s1 = _8011d768.getAddress();
        s2 = memcardData_8011dd10.getAddress();
        executeMemcardLoadingStage(s1, s2);

        if(memcardSaveLoadingStage_8011e0d4.get() != 0) {
          break;
        }

        a0 = MEMORY.ref(4, s1).offset(0x8L).get();

        if(a0 == 0x1L) {
          //LAB_801008d0
          inventoryMenuState_800bdc28.setu(0x37L);
          _8011dc90._0c.incr();
          break;
        }

        //LAB_800ff0c8
        if(a0 == 0x2L) {
          //LAB_801008d0
          inventoryMenuState_800bdc28.setu(0x39L);
          _8011dc90._0c.incr();
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
          _8011dc90._0c.incr();
          break;
        }

        //LAB_800ff12c
        //LAB_800ff130
        if(_8011d768.offset(1, 0x4L).get() != 0 || whichMenu_800bdc38.get() != 0xeL) {
          //LAB_800ff16c
          if(_8011d768.offset(1, 0x4L).get() != 0 || _8011d768.offset(1, 0x6L).get() != 0) {
            //LAB_800ff194
            if(_8011d768.offset(1, 0x10L).get() > 0xcL) {
              _8011d740.setu(_8011d768.offset(1, 0x10L).get() - 0xcL);
              _8011d744.setu(0xcL);
            } else {
              //LAB_800ff1d0
              _8011d740.setu(0);
              _8011d744.setu(_8011d768.offset(1, 0x10L));
            }

            //LAB_800ff1e0
            drgn0_6666StructPtr_800bdbe8.set(FUN_80103818(0x81L, 0x81L, 0x10L, FUN_800fc84c(_8011d740.get())));
            drgn0_6666StructPtr_800bdbec.set(FUN_80103818(0x82L, 0x82L, 0xc0L, FUN_800fc84c(_8011d740.get())));
            FUN_80104b60(drgn0_6666StructPtr_800bdbe8.deref());
            FUN_80104b60(drgn0_6666StructPtr_800bdbec.deref());
            FUN_8010361c(_8011d744.get(), 0xeL);

            inventoryMenuState_800bdc28.setu(0x2aL);
            _8011dc90._0c.incr();
            break;
          }

          if(_8011d768.offset(1, 0x4L).get() == 0) {
            inventoryMenuState_800bdc28.setu(0x3aL);
            _8011dc90._0c.incr();
            break;
          }
        }

        //LAB_801004ac
        inventoryMenuState_800bdc28.setu(0x3cL);
        _8011dc90._0c.incr();
        break;

      case 0x2a:
        FUN_8010ecec(_8011dc90);
        FUN_8010361c(_8011d744.get(), 0xeL);

        if(_8011dc90._0c.get() != 0) {
          //LAB_800fff8c
          //LAB_800fff94
          FUN_801030c0(_8011d744.get(), memcardData_8011dd10.getAddress(), 0);
          break;
        }

        //LAB_800ff2a8
        FUN_8002437c(0);
        FUN_801030c0(_8011d744.get(), memcardData_8011dd10.getAddress(), 0xffL);

        //LAB_800ff4f8
        inventoryMenuState_800bdc28.setu(0x2bL);
        break;

      case 0x2b:
        if(FUN_80103f00(_8011d740.getAddress(), _8011d744.getAddress(), 0x3L, 0xfL, 0x1L) != 0) {
          drgn0_6666StructPtr_800bdbe8.deref().y_44.set(FUN_800fc84c(_8011d740.get()));
          drgn0_6666StructPtr_800bdbec.deref().y_44.set(FUN_800fc84c(_8011d740.get()));
          inventoryMenuState_800bdc28.setu(0x2aL);
        }

        //LAB_800ff330
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          inventoryMenuState_800bdc28.setu(0x47L);
        }

        //LAB_800ff35c
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) { // Press X to load game
          FUN_8002dbdc(0);
          playSound(0x2L);
          inventoryMenuState_800bdc28.setu(0x2cL);

          if(!drgn0_6666Ptr_800bdb94.isNull()) {
            FUN_801033e8(drgn0_6666Ptr_800bdb94.deref());
            drgn0_6666Ptr_800bdb94.clear();
          }

          //LAB_800ff3a4
          if(!drgn0_6666Ptr_800bdb98.isNull()) {
            FUN_801033e8(drgn0_6666Ptr_800bdb98.deref());
            drgn0_6666Ptr_800bdb98.clear();
          }
        } else {
          //LAB_800ff3c8
          FUN_8010361c(_8011d744.get(), 0xeL);
        }

        //LAB_800fff80
        //LAB_800fff84
        //LAB_800fff8c
        //LAB_800fff94
        FUN_801030c0(_8011d744.get(), memcardData_8011dd10.getAddress(), 0);
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
        if(v1 != 0xeL) {
          //LAB_800ff4a0
          if((int)a3 < 0xfL || a3 == 0xffL) {
            //LAB_800ff4b0
            //LAB_800ff4c8
            if(a3 == 0xeL) {
              FUN_8010f130(_8011c9c8, 0x2);
            } else {
              //LAB_800ff4c0
              FUN_8010f130(_8011c9e8, 0x2);
            }

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
            FUN_8010f130(_8011ca08, 0x2);
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
        v1 = FUN_8010ecec(_8011dc90);
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
        FUN_8010ecec(_8011dc90);
        FUN_801030c0(_8011d744.get(), memcardData_8011dd10.getAddress(), 0);
        FUN_8002df60(0);
        inventoryMenuState_800bdc28.setu(0x2fL);
        break;

      case 0x2f:
        FUN_8010ecec(_8011dc90);
        FUN_801030c0(_8011d744.get(), memcardData_8011dd10.getAddress(), 0);

        final Ref<Long> refA1_0 = new Ref<>(_8011d7bc.get());
        final Ref<Long> refA2_0 = new Ref<>(fileCount_8011d7b8.get());
        v0 = FUN_8002efb8(0x1L, refA1_0, refA2_0);
        _8011d7bc.setu(refA1_0.get());
        fileCount_8011d7b8.setu(refA2_0.get());

        if(v0 == 0) {
          break;
        }

        a2 = fileCount_8011d7b8.get();

        if(a2 == 0x1L) {
          //LAB_800ff61c
          inventoryMenuState_800bdc28.setu(0x37L);
          break;
        }

        if(a2 == 0) {
          //LAB_800ff5fc
          FUN_8010f130(_8011ca2c, 0x1);
          inventoryMenuState_800bdc28.setu(0x30L);
          break;
        }

        if(a2 == 0x3L) {
          //LAB_800ff628
          //LAB_800ff62c
          inventoryMenuState_800bdc28.setu(0x3eL);
          break;
        }

        if(a2 == 0x4L) {
          //LAB_800ff634
          //LAB_800ff63c
          v0 = memcardData_8011dd10.getAddress() + 0x348L;
          s0 = 0xeL;
          do {
            MEMORY.ref(1, v0).offset(0x4L).setu(0xffL);
            s0 = s0 - 0x1L;
            v0 = v0 - 0x3cL;
          } while((int)s0 >= 0);

          inventoryMenuState_800bdc28.setu(0x3cL);
          break;
        }

        //LAB_800ff65c
        inventoryMenuState_800bdc28.setu(0x39L);
        break;

      case 0x30:
        FUN_8010ecec(_8011dc90);
        FUN_801030c0(_8011d744.get(), memcardData_8011dd10.getAddress(), 0);

        if(_8011dc90._10.get() < 0x3L) {
          break;
        }

        v1 = FUN_8010a0ec(memcardData_8011dd10.offset(1, (_8011d740.get() + _8011d744.get()) * 0x3cL).offset(0x4L).get()) & 0xffL;
        if(v1 == 0) {
          //LAB_800ff6ec
          _800bdc34.setu(0x1L);
          _80052c34.setu(_800bac6c);
          submapCut_80052c30.setu(_800bac70);
          index_80052c38.set(_800bac70.get());

          if(_800bac70.get() == 0x108L) {
            _80052c34.setu(0x35L);
          }

          //LAB_800ff730
          FUN_8002379c();
          setMonoOrStereo(mono_800bb0a8.get());
          v0 = 0x46L;
        } else if(v1 != 0x5L) {
          v0 = 0x38L;
        } else {
          //LAB_800ff750
          v0 = 0x3dL;
        }

        //LAB_800ff754
        inventoryMenuState_800bdc28.setu(v0);

        //LAB_80100bf4
        _8011dc90._0c.incr();
        break;

      case 0x31:
        v0 = FUN_8010ecec(_8011dc90);
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
        FUN_8010ecec(_8011dc90);
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
        FUN_8010ecec(_8011dc90);
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1_1 = new Ref<>(MEMORY.ref(4, a1).get());
        final Ref<Long> refA2_1 = new Ref<>(MEMORY.ref(4, a2).get());
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
              //LAB_800ff838
              FUN_8010f130(_8011cab0, 0x1);
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
        FUN_8010ecec(_8011dc90);
        a1 = 0x8012_0000L;
        a1 = a1 - 0x22f0L;
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        a2 = 0;
        FUN_801030c0(a0, a1, a2);
        v0 = _8011dc90._10.get();

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
        v0 = 0x8012_0000L;
        if(FUN_8010ecec(_8011dc90) == 0) {
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
        FUN_8010f130(_8011cb2c, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        FUN_8010f130(_8011cb38, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        FUN_8010f130(_8011cb9c, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        FUN_8010f130(_8011cbc4, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        FUN_8010f130(_8011cc30, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        FUN_8010f130(_8011cc90, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3c:
        v0 = FUN_8010ecec(_8011dc90);
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

        //LAB_800fff38
        FUN_8010f130(_8011c788, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        FUN_8010f130(_8011d708, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3e:
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          FUN_8010f130(_8011c3bc, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          FUN_8010f130(_8011ccb8, 0);
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
        } else {
          //LAB_800ffce4
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
        }

        //LAB_800ffcfc
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          FUN_8010f130(_8011cd28, 0x2);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          _8011dc90._18.set(v0);
          v0 = 0x42L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x42:
        v0 = FUN_8010ecec(_8011dc90);
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
            //LAB_800ffdb4
            if(v1 == 0x7aL) {
              FUN_8010f130(_8011c664, 0x1);
            } else {
              //LAB_800ffdb0
              FUN_8010f130(_8011cd58, 0x1);
            }

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
        FUN_8010ecec(_8011dc90);
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
        } else {
          //LAB_800ffeb0
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
        }

        //LAB_800ffec8
        v0 = FUN_8010ecec(_8011dc90);
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
        } else {

          //LAB_800fff0c
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          a1 = 0x8012_0000L;
          a1 = a1 - 0x22f0L;
          a2 = 0;
          FUN_801030c0(a0, a1, a2);
        }

        //LAB_800fff24
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        FUN_8010f130(_8011cdd8, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x46:
        v0 = FUN_8010ecec(_8011dc90);
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
        drgn0_6666StructPtr_800bdbe8.set(FUN_80103818(a0, a1, a2, a3));
        FUN_80104b60(drgn0_6666StructPtr_800bdbe8.deref());
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
        FUN_8010f130(_8011ce14, 0x1);
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
        FUN_8010ecec(_8011dc90);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = _8011dc90._10.get();

        if(v0 < 0x3L) {
          break;
        }
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1_3 = new Ref<>(MEMORY.ref(4, a1).get());
        final Ref<Long> refA2_3 = new Ref<>(MEMORY.ref(4, a2).get());
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
        //LAB_801007f8
        inventoryMenuState_800bdc28.setu(0x4eL);
        _8011dc90._0c.incr();
        break;

      case 0x4c:
        FUN_8010ecec(_8011dc90);
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
          //LAB_801007f0
          //LAB_801007f8
          inventoryMenuState_800bdc28.setu(0x75L);
          _8011dc90._0c.incr();
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
        FUN_8010ecec(_8011dc90);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0;
        a3 = 0x580L;
        s1 = 0x8012_0000L;
        a2 = MEMORY.ref(4, s1).offset(-0x2840L).get();
        v0 = 0x80L;
        a4 = v0;
        FUN_8002e908(a0, drgnpda_800fb7a0.getString(), a2, a3, a4);
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
                FUN_8010f130(_8011d044, 0);
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
        FUN_8010f130(_8011c668, 0);
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
        v0 = FUN_8010ecec(_8011dc90);

        if(v0 != 0) {
          FUN_8010f130(_8011cdfc, 0x2);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          _8011dc90._18.set(v0);
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
        v0 = FUN_8010ecec(_8011dc90);
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
        FUN_8010f130(_8011ce14, 0x1);
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
        s1 = 0x8011dc90L;
        FUN_8010ecec(_8011dc90);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = _8011dc90._10.get();

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
        a0 = 0x75L;

        //LAB_801007f8
        MEMORY.ref(4, v0).offset(-0x23d8L).setu(a0);
        _8011dc90._0c.incr();
        break;

      case 0x55:
        FUN_8010ecec(_8011dc90);
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
        v0 = executeMemcardLoadingStage(a0, a1);
        v0 = 0x8012_0000L;
        v0 = MEMORY.ref(1, v0).offset(-0x1f2cL).get();

        if(v0 != 0) {
          v0 = 0x1L;
          break;
        }
        v1 = MEMORY.ref(4, s1).offset(0x8L).get();

        v0 = 0x800c_0000L;
        if(v1 == 0x1L) {
          a0 = 0x75L;
          //LAB_80100878
        } else if(v1 == 0x2L) {
          a0 = 0x75L;
          //LAB_8010088c
        } else if(MEMORY.ref(1, s1).offset(0x5L).get() != 0) {
          a0 = 0x56L;
          //LAB_801008a8
        } else if(MEMORY.ref(1, s1).offset(0x6L).get() < 0x9L) {
          a0 = 0x73L;
        } else {
          //LAB_801008c8
          a0 = 0x59L;
        }

        //LAB_801008d0
        MEMORY.ref(4, v0).offset(-0x23d8L).setu(a0);
        _8011dc90._0c.incr();
        break;

      case 0x56:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          FUN_8010f130(_8011ce08, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          FUN_8010f130(_8011c3a4, 0x2);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          _8011dc90._18.set(v0);
          v0 = 0x58L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x58:
        v0 = FUN_8010ecec(_8011dc90);
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

          final Ref<Long> refA1_5 = new Ref<>(MEMORY.ref(4, a1).get());
          final Ref<Long> refA2_5 = new Ref<>(MEMORY.ref(4, a2).get());
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
        v0 = FUN_8010ecec(_8011dc90);
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
            //LAB_80100ed0
            v1 = 0x800c_0000L;

            //LAB_80100ed4
            v0 = 0x75L;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
            break;
          }
        } else {
          //LAB_80100a4c
          if(a0 == 0x4L) {
            //LAB_80100a68
            v1 = 0x800c_0000L;
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(0x79L);
            break;
          }

          if(a0 == 0x7L) {
            //LAB_80101854
            v1 = 0x800c_0000L;

            //LAB_80101858
            MEMORY.ref(4, v1).offset(-0x23d8L).setu(0x74L);
            break;
          }
        }

        //LAB_80100a78
        FUN_8010f130(_8011ce00, 0x1);
        v1 = 0x800c_0000L;
        v0 = 0x5aL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x5a:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = FUN_8010ecec(_8011dc90);
        v0 = _8011dc90._10.get();

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
        FUN_8010ecec(_8011dc90);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        a0 = 0x1L;
        a1 = 0x8012_0000L;
        a1 = a1 - 0x2844L;
        s0 = 0x8012_0000L;
        a2 = s0 - 0x2848L;

        final Ref<Long> refA1_6 = new Ref<>(MEMORY.ref(4, a1).get());
        final Ref<Long> refA2_6 = new Ref<>(MEMORY.ref(4, a2).get());
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
        FUN_80103168(_8011d740.get());

        if(FUN_8010ecec(_8011dc90) == 0) {
          break;
        }

        final Memory.TemporaryReservation tmpA2 = MEMORY.temp(0x280); // Does this need to be this big? It's the size of the regular array of files
        final Ref<Long> fileCount = new Ref<>();
        FUN_8002ed48(0, drgnpda_800fb7a0.getAddress(), tmpA2.get().cast(ArrayRef.of(MemcardStruct28.class, 0x10, 0x28, MemcardStruct28::new)), fileCount, 0, 0x1L);
        fileCount_8011d7b8.setu(fileCount.get());

        final Ref<Long> refA1_7 = new Ref<>(_8011d7bc.get());
        final Ref<Long> refA2_7 = new Ref<>(fileCount_8011d7b8.get());
        FUN_8002efb8(0, refA1_7, refA2_7);
        _8011d7bc.setu(refA1_7.get());
        fileCount_8011d7b8.setu(refA2_7.get());

        a1 = tmpA2.get().offset(0x20L).get();//MEMORY.ref(4, sp).offset(0x40L).get();

        if((int)a1 < 0) {
          a1 += 0x3fL;
        }

        //LAB_80100c88
        FUN_80041600(0, a1 / 0x40L, 0x1L);
        FUN_800426c4(0, _8011d7bc.getAddress(), fileCount_8011d7b8.getAddress());
        FUN_8010f130(_8011c5d8, 0);
        inventoryMenuState_800bdc28.setu(0x5dL);

        tmpA2.release();
        break;

      case 0x5e:
        FUN_8010f130(_8011c8c8, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }
        FUN_8010f130(_8011c8d4, 0x2);
        v0 = 0x1L;
        v1 = 0x800c_0000L;
        _8011dc90._18.set(v0);

        //LAB_80100db4
        v0 = 0x63L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x60:
        FUN_8010f130(_8011d710, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          break;
        }
        FUN_8010f130(_8011c8bc, 0x2);
        a1 = 0;
        a0 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
        v0 = 0x1L;
        _8011dc90._18.set(v0);
        FUN_80103168(a0);
        v1 = 0x800c_0000L;

        //LAB_80100db4
        v0 = 0x63L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x62:
        FUN_8010f130(_8011d060, 0x2);
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
        v0 = FUN_8010ecec(_8011dc90);
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

          FUN_8010f130(_8011d5c4, 0);
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

        v0 = 0x2L;
        if((int)v1 < 0x2L) {
          if(v1 == 0) {
            //LAB_80100f50
            final Memory.TemporaryReservation tmpA2_1 = MEMORY.temp(0x30); // Does this need to be this big? It's the size of the regular array of files
            final Ref<Long> fileCount_1 = new Ref<>();
            FUN_8002ed48(0, drgnpda_800fb7a0.getAddress(), tmpA2_1.get().cast(ArrayRef.of(MemcardStruct28.class, 0x10, 0x28, MemcardStruct28::new)), fileCount_1, 0, t0);
            fileCount_8011d7b8.setu(fileCount_1.get());

            a0 = 0;
            v0 = 0x8012_0000L;
            s1 = v0 - 0x2844L;
            a1 = s1;
            a2 = fileCount_8011d7b8.getAddress();

            final Ref<Long> refA1_8 = new Ref<>();
            final Ref<Long> refA2_8 = new Ref<>();
            FUN_8002efb8(a0, refA1_8, refA2_8);
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
            a2 = fileCount_8011d7b8.getAddress();
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
              FUN_8010f130(_8011cf54, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
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
        FUN_8010f130(_8011c8cc, 0);
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
        FUN_8010f130(_8011c914, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
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

          FUN_8010f130(_8011c66c, 0x1);
          v0 = 0x800c_0000L;
          v1 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2414L).setu(0);
          v0 = 0x6aL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x6a:
        v0 = FUN_8010ecec(_8011dc90);
        v0 = _8011dc90._10.get();

        if(v0 < 0x3L) {
          v0 = 0x8012_0000L;

          //LAB_80101580
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          a1 = 0;
          FUN_80103168(a0);
          break;
        }

        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(-0x2414L).get();

        if(v0 == 0) {
          drgn0_6666StructPtr_800bdbec.set(FUN_80103818(0xd3L, 0xd3L, 0x44L, 0x50L));
          drgn0_6666StructPtr_800bdbec.deref()._3c.set(0x1f);
        }

        //LAB_80101380
        a0 = 0x70L;
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

        unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbec.deref());
        drgn0_6666StructPtr_800bdbec.set(FUN_80103818(0xd3L, 0xd9L, 0x44L, 0x50L));
        drgn0_6666StructPtr_800bdbec.deref()._3c.set(0x1f);
        inventoryMenuState_800bdc28.setu(0x6bL);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x6b:
        FUN_8010ecec(_8011dc90);
        a1 = 0x8012_0000L;
        a0 = _8011dd08.get();

        if(a0 < 0xbL || (inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          //LAB_80101454
          s0 = 0x800c_0000L;
          v1 = 0x800c_0000L;

          //LAB_80101458
          v1 = v1 - 0x5438L;
          a1 = 0x8012_0000L;
          v0 = MEMORY.ref(4, v1).offset(0x94L).get();
          a2 = MEMORY.ref(4, a1).offset(-0x22f8L).get();
          MEMORY.ref(4, a1).offset(-0x22f8L).setu(0);
          v0 = v0 + a2;
          MEMORY.ref(4, v1).offset(0x94L).setu(v0);
          unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbec.deref());
          drgn0_6666StructPtr_800bdbec.set(FUN_80103818(0xd3L, 0xd3L, 0x44L, 0x50L));
          drgn0_6666StructPtr_800bdbec.deref()._3c.set(0x1f);
          inventoryMenuState_800bdc28.setu(0x6cL);
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
        FUN_8010ecec(_8011dc90);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 != 0) {
          unloadDrgn0_6666Struct(drgn0_6666StructPtr_800bdbec.deref());
          inventoryMenuState_800bdc28.setu(0x6dL);
          _8011dc90._0c.incr();
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          return;
        }

        //LAB_80101644
        FUN_8010f130(_8011d048, 0);
        v1 = 0x800c_0000L;

        //LAB_80101650
        v0 = 0x6dL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x6f:
        FUN_8010f130(_8011d700, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 == 0) {
          return;
        }

        //LAB_80101644
        FUN_8010f130(_8011d704, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          //LAB_80101644
          FUN_8010f130(_8011d70c, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          //LAB_801016b0
          FUN_8010f130(_8011ce0c, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          //LAB_801016b0
          FUN_8010f130(_8011ce10, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
        if(v0 != 0) {
          FUN_8010f130(_8011ce04, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
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
        v0 = FUN_8010ecec(_8011dc90);
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
        v0 = FUN_8010ecec(_8011dc90);
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
        FUN_8010f130(_8011ce18, 0);
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
        v0 = FUN_8010ecec(_8011dc90);
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
  public static void FUN_80101d10(long a0, final long a1, long a2) {
    a2 = a2 ^ 0xffL;

    final long s5;
    if(_8011dc88.get() != 0) {
      s5 = a1 & 0xffL;
    } else {
      s5 = 0x6L;
    }

    //LAB_80101d54
    long s4 = a2 < 0x1L ? 1 : 0;
    if(s4 != 0) {
      FUN_801098c0(_800bad64.getAddress(), 0x28L, 0xc5L);
      FUN_80105f2c(0x43L, 0xb8L, _800bac5c.get(), 0);
      FUN_80107cb4(0x92L, 0xb8L, 0xaL);
      FUN_80107cb4(0xa4L, 0xb8L, 0xaL);
      FUN_80104c30(0xa6L, 0xccL, _800bae00.get());
    }

    //LAB_80101db8
    FUN_80107764(0x80L, 0xb8L, FUN_80023674(_800babc8.offset(0xa0L).get(), 0x0L), 0x3L);
    FUN_801079fc(0x98L, 0xb8L, FUN_80023674(_800babc8.offset(0xa0L).get(), 0x1L), 0x3L);
    FUN_801079fc(0xaaL, 0xb8L, FUN_80023674(_800babc8.offset(0xa0L).get(), 0x2L), 0x3L);
    FUN_80107f9c(0xc2L, 0x10L, _800babc8.offset(0x88L).get(), s4 & 0xffL, 0);
    FUN_80107f9c(0xc2L, 0x58L, _800babc8.offset(0x8cL).get(), s4 & 0xffL, 0);
    FUN_80107f9c(0xc2L, 0xa0L, _800babc8.offset(0x90L).get(), s4 & 0xffL, 0);
    FUN_80103e90(_80114248.get((int)_800babc8.offset(0x98L).get()).deref(), 0x5eL, 0x18L, 0x4L);

    final ArrayRef<UnsignedShortRef> v1;
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      v1 = _8011c108.get((int)_800bd808.get()).deref();
    } else {
      //LAB_80101ec0
      v1 = _8011c1ec.get((int)_800bf0b0.get()).deref();
    }

    //LAB_80101ed4
    FUN_80103e90(v1, 0x5aL, 0x26L, 0x4L);

    long a3;
    if(a0 == 0) {
      a3 = 0x5L;
    } else {
      a3 = a1 & 0xffL;
    }

    //LAB_80101f0c
    FUN_80103e90(_8011ceb4, 0x3eL, (FUN_800fc78c(0) + 0x2L) & 0xffffL, a3);

    if(a0 == 0x1L) {
      a3 = 0x5L;
    } else {
      a3 = a1 & 0xffL;
    }

    //LAB_80101f3c
    FUN_80103e90(_8011cec4, 0x3eL, (FUN_800fc78c(0x1L) + 0x2L) & 0xffffL, a3);

    if(a0 == 0x2L) {
      a3 = 0x5L;
    } else {
      a3 = a1 & 0xffL;
    }

    //LAB_80101f6c
    FUN_80103e90(_8011ced0, 0x3eL, (FUN_800fc78c(0x2L) + 0x2L) & 0xffffL, a3);

    if(a0 == 0x3L) {
      a3 = 0x5L;
    } else {
      a3 = a1 & 0xffL;
    }

    //LAB_80101f9c
    FUN_80103e90(_8011cedc, 0x3eL, (FUN_800fc78c(0x3L) + 0x2L) & 0xffffL, a3);

    if(a0 == 0x4L) {
      a3 = 0x5L;
    } else {
      a3 = a1 & 0xffL;
    }

    //LAB_80101fcc
    FUN_80103e90(_8011cef0, 0x3eL, (FUN_800fc78c(0x4L) + 0x2L) & 0xffffL, a3);

    if(a0 == 0x5L) {
      a3 = 0x5L;
    } else {
      a3 = a1 & 0xffL;
    }

    //LAB_80101ff8
    FUN_80103e90(_8011cf00, 0x3eL, (FUN_800fc78c(0x5L) + 0x2L) & 0xffffL, a3);

    if(a0 == 0x6L) {
      a3 = 0x5L;
    } else {
      a3 = s5 & 0xffL;
    }

    //LAB_80102028
    FUN_80103e90(_8011cf10, 0x3eL, (FUN_800fc78c(0x6L) + 0x2L) & 0xffffL, a3);

    if(s4 == 0) {
      FUN_80023c28();
    }

    //LAB_80102040
  }

  @Method(0x80102064L)
  public static void FUN_80102064(final long a0, final long a1) {
    FUN_801038d4(0x96L, 0x14L, 0x3cL);

    //LAB_801020ac
    FUN_80103e90(_8011cf1c, 0x8eL, FUN_800fc7a4(0) & 0xffffL, a0 == 0 ? 0x5L : a1);

    //LAB_801020d8
    FUN_80103e90(_8011cf2c, 0x8eL, FUN_800fc7a4(0x1L) & 0xffffL, a0 == 0x1L ? 0x5L : a1);

    //LAB_80102104
    FUN_80103e90(_8011cf3c, 0x8eL, FUN_800fc7a4(0x2L) & 0xffffL, a0 == 0x2L ? 0x5L : a1);

    //LAB_80102130
    FUN_80103e90(_8011cf48, 0x8eL, FUN_800fc7a4(0x3L) & 0xffffL, a0 == 0x3L ? 0x5L : a1);
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
        //TODO memcard struct
        FUN_80108a6c(v1 & 0xffL, MEMORY.ref(4, a1 + v1 * 0x3cL, MemcardDataStruct3c::new), FUN_800fc84c(s0), a2 == 0xffL ? 1 : 0);
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
  public static void FUN_801033cc(final Drgn0_6666Struct58 a0) {
    a0._28.set(0x1);
    a0._38.set(0);
    a0._34.set(0);
    a0._3c.set(0x1f);
  }

  @Method(0x801033e8L)
  public static void FUN_801033e8(final Drgn0_6666Struct58 a0) {
    final long s0 = a0.x_40.get();
    final long s1 = a0.y_44.get();

    unloadDrgn0_6666Struct(a0);

    final Drgn0_6666Struct58 a0_0 = FUN_80103818(0x6cL, 0x6fL, s0, s1);
    a0_0._00.or(0x10L);
    FUN_801033cc(a0_0);
  }

  @Method(0x80103444L)
  public static void FUN_80103444(@Nullable final Drgn0_6666Struct58 a0, final long a1, final long a2, final long a3, final long a4) {
    if(a0 != null) {
      if(a0._18.get() == 0) {
        if((FUN_800133ac() & 0x3000L) != 0) {
          a0._18.set(a1);
          a0._1c.set(a2);
        } else {
          //LAB_801034a0
          a0._18.set(a3);
          a0._1c.set(a4);
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
    FUN_80103444(drgn0_6666Ptr_800bdb94.derefNullable(), 0xc2L, 0xc9L, 0xcaL, 0xd1L);
    FUN_80103444(drgn0_6666Ptr_800bdb98.derefNullable(), 0xb2L, 0xb9L, 0xbaL, 0xc1L);

    if(a0 != 0) {
      if(drgn0_6666Ptr_800bdb94.isNull()) {
        final Drgn0_6666Struct58 a0_0 = FUN_80103818(0x6fL, 0x6cL, 0xb6L, 0x10L);
        a0_0._18.set(0xc2L);
        drgn0_6666Ptr_800bdb94.set(a0_0);
        a0_0._1c.set(0xc9L);
        FUN_801033cc(a0_0);
      }
      //LAB_801036c8
    } else if(!drgn0_6666Ptr_800bdb94.isNull()) {
      FUN_801033e8(drgn0_6666Ptr_800bdb94.deref());
      drgn0_6666Ptr_800bdb94.clear();
    }

    //LAB_801036e8
    if((int)a0 < a1 - 0x2L) {
      if(drgn0_6666Ptr_800bdb98.isNull()) {
        final Drgn0_6666Struct58 a0_0 = FUN_80103818(0x6fL, 0x6cL, 0xb6L, 0xd0L);
        a0_0._18.set(0xb2L);
        drgn0_6666Ptr_800bdb98.set(a0_0);
        a0_0._1c.set(0xb9L);
        FUN_801033cc(a0_0);
      }
      //LAB_80103738
    } else if(!drgn0_6666Ptr_800bdb98.isNull()) {
      FUN_801033e8(drgn0_6666Ptr_800bdb98.deref());
      drgn0_6666Ptr_800bdb98.clear();
    }

    //LAB_80103754
  }

  @Method(0x8010376cL)
  public static void FUN_8010376c(final long a0, final long a1, final long a2) {
    //LAB_801037ac
    for(long s1 = a0; MEMORY.ref(1, s1).get() != 0xffL; s1 += 0x6L) {
      final Drgn0_6666Struct58 s0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);

      FUN_80104b1c(s0, s1);

      s0.x_40.add(a1);
      s0.y_44.add(a2);
    }

    //LAB_801037f4
  }

  @Method(0x80103818L)
  public static Drgn0_6666Struct58 FUN_80103818(final long a0, final long a1, final long a2, final long a3) {
    final Drgn0_6666Struct58 v1 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);

    if((int)a1 >= (int)a0) {
      v1.charIndex_04.set(a0);
      v1._10.set(a0);
      v1._14.set(a1);
    } else {
      //LAB_80103870
      v1.charIndex_04.set(a0);
      v1._10.set(a1);
      v1._14.set(a0);
      v1._00.or(0x20L);
    }

    //LAB_80103888
    if(a0 == a1) {
      v1._00.or(0x4L);
    }

    //LAB_801038a4
    v1._2c.set(0x19L);
    v1._30.set(0);
    v1.x_40.set(a2);
    v1.y_44.set(a3);

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

  /** Text packets for menu popups... more? Doesn't render text on the actual save game slots. */
  @Method(0x80103cc4L)
  public static void FUN_80103cc4(ArrayRef<UnsignedShortRef> a0, long a1, long a2, long a3) {
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
  public static long FUN_80103dd4(final ArrayRef<UnsignedShortRef> a0) {
    //LAB_80103ddc
    int v1;
    for(v1 = 0; v1 < 0xff; v1++) {
      if(a0.get(v1).get() == 0xa0ffL) {
        break;
      }
    }

    //LAB_80103dfc
    return v1;
  }

  @Method(0x80103e90L)
  public static void FUN_80103e90(final ArrayRef<UnsignedShortRef> a0, final long a1, final long a2, final long a3) {
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
  public static long FUN_801041d8(long a0) {
    if(FUN_801040c0(a0, 0x2L) != 0) {
      return 0x1L;
    }

    if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
      playSound(0x3L);
      return 0x4L;
    }

    //LAB_80104220
    if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
      playSound(0x2L);

      if(MEMORY.ref(4, a0).get() == 0) {
        return 0x2L;
      }

      return 0x3L;
    }

    //LAB_80104244
    return 0;
  }

  @Method(0x80104324L)
  public static void FUN_80104324(final long a0) {
    assert false;
  }

  @Method(0x801038d4L)
  public static void FUN_801038d4(final long a0, final long a1, final long a2) {
    final Drgn0_6666Struct58 v0 = FUN_80103818(a0, a0, a1, a2);
    v0._00.or(0x8L);
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
  public static void FUN_80104b1c(final Drgn0_6666Struct58 a0, final long a1) {
    if(MEMORY.ref(1, a1).get() != 0xffL) {
      a0.charIndex_04.set(MEMORY.ref(1, a1).get());
      a0._00.or(0x4L);
    }

    //LAB_80104b40
    a0._2c.set(0x19L);
    a0._30.set(0);
    a0.x_40.set(MEMORY.ref(2, a1).offset(0x2L).get());
    a0.y_44.set(MEMORY.ref(2, a1).offset(0x4L).get());
  }

  @Method(0x80104b60L)
  public static void FUN_80104b60(final Drgn0_6666Struct58 a0) {
    a0._28.set(0x1);
    a0._34.set(0);
    a0._38.set(0);
    a0._3c.set(0x23);
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
      final Drgn0_6666Struct58 drgn = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = drgn._00.get();
      drgn.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80104cd8
        v0 = v0 | 0x4L;
      }

      //LAB_80104cdc
      drgn._00.set(v0);
      drgn._2c.set(0x19L);
      drgn.x_40.set(s3 & 0xffffL);
      drgn.y_44.set(a1 & 0xffffL);
      drgn._30.set(0);
      drgn._3c.set(0x21);
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
    final Drgn0_6666Struct58 drgn = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
    v0 = drgn._00.get();
    drgn.charIndex_04.set(s0);
    if((sp10 & 0x2L) != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_80104d78
      v0 = v0 | 0x4L;
    }

    //LAB_80104d7c
    drgn._00.set(v0);
    drgn._2c.set(0x19L);
    drgn.x_40.set(s3 & 0xffffL);
    drgn.y_44.set(a1 & 0xffffL);
    drgn._30.set(0);
    drgn._3c.set(0x21);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80104e84
        v0 = v0 | 0x4L;
      }

      //LAB_80104e88
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
      struct._30.set(0);
      struct._3c.set(0x21);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80104f48
        v0 = v0 | 0x4L;
      }

      //LAB_80104f4c
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct.x_40.set(s5 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
      struct._30.set(0);
      struct._3c.set(0x21);
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
    final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
    v0 = struct._00.get();
    struct.charIndex_04.set(s0);
    if((sp10 & 0x2L) != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_80104fe8
      v0 = v0 | 0x4L;
    }

    //LAB_80104fec
    struct._00.set(v0);
    struct._2c.set(0x19L);
    struct.x_40.set(s3 & 0xffffL);
    struct.y_44.set(s4 & 0xffffL);
    struct._30.set(0);
    struct._3c.set(0x21);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105400
        v0 = v0 | 0x4L;
      }

      //LAB_80105404
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s5 & 0xffffL);
      struct._30.set(0);
      struct._3c.set(0x21);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801054c8
        v0 = v0 | 0x4L;
      }

      //LAB_801054cc
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct.x_40.set(s4 & 0xffffL);
      struct.y_44.set(s5 & 0xffffL);
      struct._30.set(0);
      struct._3c.set(0x21);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_8010558c
        v0 = v0 | 0x4L;
      }

      //LAB_80105590
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct.x_40.set(s4 & 0xffffL);
      struct.y_44.set(s5 & 0xffffL);
      struct._30.set(0);
      struct._3c.set(0x21);
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
    final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
    v0 = struct._00.get();
    struct.charIndex_04.set(s0);
    if((sp10 & 0x2L) != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_8010562c
      v0 = v0 | 0x4L;
    }

    //LAB_80105630
    struct._00.set(v0);
    struct._2c.set(0x19L);
    struct.x_40.set(s3 & 0xffffL);
    struct.y_44.set(s5 & 0xffffL);
    struct._30.set(0);
    struct._3c.set(0x21);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105784
        v0 = v0 | 0x4L;
      }

      //LAB_80105788
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct.x_40.set(s5 & 0xffffL);
      struct.y_44.set(s6 & 0xffffL);
      struct._30.set(0);
      struct._3c.set(0x21);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105860
        v0 = v0 | 0x4L;
      }

      //LAB_80105864
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s6 & 0xffffL);
      struct._30.set(0);
      struct._3c.set(0x21);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105938
        v0 = v0 | 0x4L;
      }

      //LAB_8010593c
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s6 & 0xffffL);
      struct._30.set(0);
      struct._3c.set(0x21);
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
    final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
    v0 = struct._00.get();
    struct.charIndex_04.set(s0);
    if((sp10 & 0x2L) != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_801059e8
      v0 = v0 | 0x4L;
    }

    //LAB_801059ec
    struct._00.set(v0);
    struct._2c.set(0x19L);
    struct._30.set(s4);
    struct._3c.set(0x21);
    struct.x_40.set(s3 & 0xffffL);
    struct.y_44.set(s6 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105b10
        v0 = v0 | 0x4L;
      }

      //LAB_80105b14
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s4 & 0xffffL);
      struct.y_44.set(s5 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105bd8
        v0 = v0 | 0x4L;
      }

      //LAB_80105bdc
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s5 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105ca0
        v0 = v0 | 0x4L;
      }

      //LAB_80105ca4
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s5 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105d68
        v0 = v0 | 0x4L;
      }

      //LAB_80105d6c
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s5 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp10 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105e2c
        v0 = v0 | 0x4L;
      }

      //LAB_80105e30
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s5 & 0xffffL);
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
    final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
    v0 = struct._00.get();
    struct.charIndex_04.set(s0);
    if((sp10 & 0x2L) != 0) {
      v0 = v0 | 0xcL;
    } else {
      //LAB_80105ecc
      v0 = v0 | 0x4L;
    }

    //LAB_80105ed0
    struct._00.set(v0);
    struct._2c.set(0x19L);
    struct._30.set(0);
    struct._3c.set(0x21);
    struct.x_40.set(s3 & 0xffffL);
    struct.y_44.set(s5 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80105ff4
        v0 = v0 | 0x4L;
      }

      //LAB_80105ff8
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s2 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801060bc
        v0 = v0 | 0x4L;
      }

      //LAB_801060c0
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s5 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80106188
        v0 = v0 | 0x4L;
      }

      //LAB_8010618c
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s5 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80106250
        v0 = v0 | 0x4L;
      }

      //LAB_80106254
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s5 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80106318
        v0 = v0 | 0x4L;
      }

      //LAB_8010631c
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s5 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801063e0
        v0 = v0 | 0x4L;
      }

      //LAB_801063e4
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s5 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801064a4
        v0 = v0 | 0x4L;
      }

      //LAB_801064a8
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s5 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_8010655c
        v0 = v0 | 0x4L;
      }

      //LAB_80106560
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(s4 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80107820
        v0 = v0 | 0x4L;
      }

      //LAB_80107824
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s2 & 0xffffL);
      struct.y_44.set(a1 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_801078e4
        v0 = v0 | 0x4L;
      }

      //LAB_801078e8
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s4 & 0xffffL);
      struct.y_44.set(a1 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp3c & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_8010799c
        v0 = v0 | 0x4L;
      }

      //LAB_801079a0
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(a1 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp34 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80107ab0
        v0 = v0 | 0x4L;
      }

      //LAB_80107ab4
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(a1 & 0xffffL);
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
      final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0 = struct._00.get();
      struct.charIndex_04.set(s0);
      if((sp34 & 0x2L) != 0) {
        v0 = v0 | 0xcL;
      } else {
        //LAB_80107b68
        v0 = v0 | 0x4L;
      }

      //LAB_80107b6c
      struct._00.set(v0);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(s3 & 0xffffL);
      struct.y_44.set(a1 & 0xffffL);
      sp34 |= 0x1L;
    }

    //LAB_80107ba4
  }

  @Method(0x80107cb4L)
  public static void FUN_80107cb4(long a0, long a1, long a2) {
    final Drgn0_6666Struct58 v0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
    v0._00.or(0x4L);
    v0.charIndex_04.set(a2);
    v0._2c.set(0x19L);
    v0._30.set(0x7ca9L);
    v0._3c.set(0x21);
    v0.x_40.set(a0 & 0xffffL);
    v0.y_44.set(a1 & 0xffffL);
  }

  @Method(0x80107dd4L)
  public static void FUN_80107dd4(final long a0, final long a1, final long a2) {
    if(a2 != 0) {
      FUN_80105a50(a0 & 0xffffL, a1 & 0xffffL, a2);
    } else {
      //LAB_80107e08
      final Drgn0_6666Struct58 v0 = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
      v0._00.or(0x4L);
      v0.charIndex_04.set(0xdaL);
      v0._2c.set(0x19L);
      v0._30.set(0x7ca9L);
      v0._3c.set(0x21);
      v0.x_40.set((a0 + 0x1eL) & 0xffffL);
      v0.y_44.set(a1 & 0xffffL);
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
    if(v1 == 0) {
      //LAB_80107f88
      return 0;
    }

    final MenuStruct08 struct = _800fba7c.get((int)(v1 - 0x1L));
    FUN_80103e90(struct._00.deref(), (a0 + 0x18L) & 0xffffL, a1 & 0xffffL, struct._04.get());

    //LAB_80107f8c
    return 0x1L;
  }

  @Method(0x80107f9cL)
  public static void FUN_80107f9c(long a0, long a1, long a2, long a3, long a4) {
    if((int)a2 != -0x1L) {
      if((a3 & 0xffL) != 0) {
        FUN_80103818(0x4aL, 0x4aL, (short)a0, (short)a1)._3c.set(0x21);
        FUN_80103818(0x99L, 0x99L, (short)a0, (short)a1);

        if(a2 < 0x9L) {
          final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get() + 0xcfacL, null);
          FUN_80104b1c(struct, _801142d4.getAddress());
          struct.charIndex_04.set(a2);
          struct._2c.add(0x1L);
          struct._3c.set(0x21);
          struct.x_40.set((short)a0 + 0x8L);
          struct.y_44.set((short)a1 + 0x8L);
        }

        //LAB_80108098
        //TODO array of structs
        FUN_80104c30((a0 + 0x9aL) & 0xffffL, (a1 + 0x6L) & 0xffffL, _800be606.offset(a2 * 0xa0L).get());
        long s0 = (a1 + 0x11L) & 0xffffL;
        FUN_80104c30((a0 + 0x70L) & 0xffffL, s0, _800be607.offset(a2 * 0xa0L).get());
        long s4 = (a0 + 0x94L) & 0xffffL;
        FUN_80104dd4(s4, s0, _800be600.offset(a2 * 0xa0L).getSigned());
        s0 = (a1 + 0x1cL) & 0xffffL;
        FUN_8010568c((a0 + 0x64L) & 0xffffL, s0, _800baefc.offset(a2 * 0x2cL).getSigned(), _800be65e.offset(a2 * 0xa0L).get());
        long s3 = (a0 + 0x7cL) & 0xffffL;
        FUN_80107cb4(s3, s0, 0xbL);
        FUN_80105350((a0 + 0x8eL) & 0xffffL, s0, _800be65e.offset(a2 * 0xa0L).get());
        s0 = (a1 + 0x27L) & 0xffffL;
        FUN_80104dd4((a0 + 0x6aL) & 0xffffL, s0, _800be5fe.offset(a2 * 0xa0L).getSigned());
        FUN_80107cb4(s3, s0, 0xbL);
        FUN_80104dd4(s4, s0, _800be666.offset(a2 * 0xa0L).getSigned());
        s0 = (a1 + 0x32L) & 0xffffL;
        FUN_80105a50((a0 + 0x58L) & 0xffffL, s0, _800baef4.offset(a2 * 0x2cL).get());
        FUN_80107cb4(s3, s0, 0xbL);
        FUN_80107dd4((a0 + 0x82L) & 0xffffL, s0, FUN_800fc698(a2));

        if(a4 != 0) {
          final Drgn0_6666Struct58 struct = FUN_80103818(0x71L, 0x71L, (short)a0 + 0x38L, (short)a1 + 0x18L);
          struct._3c.set(0x21);
        }
      }

      //LAB_80108218
      long s1 = (short)(a0 + 0x30L);
      long s0 = (short)(a1 + 0x3L);
      if(FUN_80107e70(s1, s0, a2) == 0) {
        FUN_80103cc4(_801142dc.get((int)a2).deref(), s1, s0, 0x4L);
      }
    }

    //LAB_80108270
  }

  @Method(0x80108a6cL)
  public static void FUN_80108a6c(final long a0, final MemcardDataStruct3c a1, final long a2, final long a3) {
    if((a3 & 0xffL) != 0) {
      FUN_80104c30(0x15L, a2 & 0xffffL, (a0 & 0xffL) + 0x1L);
    }

    //LAB_80108ab8
    if(a1._04.get() == 0xfcL) {
      //LAB_80108b04
      //LAB_80108b20
      FUN_80103cc4(_8011d04c, 0x20L, (short)a2, 0x4L);
      //LAB_80108ae8
    } else if(a1._04.get() == 0xfeL) {
      //LAB_80108b10
      //LAB_80108b20
      FUN_80103cc4(_8011c88c, 0x20L, (short)a2, 0x4L);
    } else if(a1._04.get() == 0xffL) {
      //LAB_80108b1c
      //LAB_80108b20
      FUN_80103cc4(_8011c7d8, 0x20L, (short)a2, 0x4L);
    } else if(a1._04.get() >= 0 && a1._04.get() < 0xfL) {
      //LAB_80108b3c
      final ArrayRef<Pointer<ArrayRef<UnsignedShortRef>>> v1_0;
      if(a1._2d.get() == 0x1L) {
        //LAB_80108b5c
        v1_0 = _8011c1ec;
      } else if(a1._2d.get() == 0x3L) {
        //LAB_80108b78
        v1_0 = _80114248;
      } else {
        //LAB_80108b90
        v1_0 = _8011c108;
      }

      //LAB_80108ba0
      FUN_80103e90(v1_0.get(a1._2c.get()).deref(), 0x116L, (a2 + 0x2fL) & 0xffffL, 0x4L);

      if((a3 & 0xffL) != 0) {
        FUN_80103818(0x4cL, 0x4cL, 0x10L, a2)._3c.set(0x21);
        FUN_80103818(0x4dL, 0x4dL, 0xc0L, a2)._3c.set(0x21);

        // Load char 0
        if(a1.char0Index_08.get() < 0x9L) {
          final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get() + 0xcfacL, null);
          FUN_80104b1c(struct, _801142d4.getAddress());
          struct.charIndex_04.set(a1.char0Index_08);
          struct._2c.add(0x1L);
          struct._3c.set(0x21);
          struct.x_40.set(0x26L);
          struct.y_44.set(a2 + 0x8L);
        }

        // Load char 1
        //LAB_80108c78
        if(a1.char1Index_0c.get() < 0x9L) {
          final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get() + 0xcfacL, null);
          FUN_80104b1c(struct, _801142d4.getAddress());
          struct.charIndex_04.set(a1.char1Index_0c);
          struct._2c.add(0x1L);
          struct._3c.set(0x21);
          struct.x_40.set(0x5aL);
          struct.y_44.set(a2 + 0x8L);
        }

        // Load char 2
        //LAB_80108cd4
        if(a1.char2Index_10.get() < 0x9L) {
          final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get() + 0xcfacL, null);
          FUN_80104b1c(struct, _801142d4.getAddress());
          struct.charIndex_04.set(a1.char2Index_10);
          struct._2c.add(0x1L);
          struct._3c.set(0x21);
          struct.x_40.set(0x8eL);
          struct.y_44.set(a2 + 0x8L);
        }

        //LAB_80108d30
        long s0 = (a2 + 0x6L) & 0xffffL;
        FUN_80104c30(0x0e0L, s0, a1._14.get());
        FUN_80104c30(0x10dL, s0, a1._15.get());
        FUN_80105350(0x12eL, s0, a1._16.get());
        FUN_80105350(0x14cL, s0, a1._18.get());

        s0 = (a2 + 0x11L) & 0xffffL;
        FUN_80105f2c(0x0f5L, s0, a1._1c.get(), 0);
        FUN_80107764(0x132L, s0, FUN_80023674(a1._20.get(), 0), 0x1L);
        FUN_80107cb4(0x144L, s0, 0xaL);
        FUN_801079fc(0x14aL, s0, FUN_80023674(a1._20.get(), 0x1L), 0x1L);
        FUN_80107cb4(0x156L, s0, 0xaL);
        FUN_801079fc(0x15cL, s0, FUN_80023674(a1._20.get(), 0x2L), 0x1L);
        FUN_80104c30(0x158L, (a2 + 0x22L) & 0xffffL, a1._28.get());
        FUN_801098c0(a1._24.getAddress() /*TODO*/, 0xdfL, (a2 + 0x1bL) & 0xffL);
      }
    }

    //LAB_80108e3c
  }

  @Method(0x801098c0L)
  public static void FUN_801098c0(long a0, long a1, long a2) {
    long v0;
    long s4 = a0;
    long s2 = a1 & 0xffL;

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x28);
    final Value sp18 = tmp.get();

    sp18.offset(4, 0x00L).setu(_800fbabc.offset(0x00L));
    sp18.offset(4, 0x04L).setu(_800fbabc.offset(0x04L));
    sp18.offset(4, 0x08L).setu(_800fbabc.offset(0x08L));
    sp18.offset(4, 0x0cL).setu(_800fbabc.offset(0x0cL));
    sp18.offset(4, 0x10L).setu(_800fbabc.offset(0x10L));
    sp18.offset(4, 0x14L).setu(_800fbabc.offset(0x14L));
    sp18.offset(4, 0x18L).setu(_800fbabc.offset(0x18L));
    sp18.offset(4, 0x1cL).setu(_800fbabc.offset(0x1cL));

    //LAB_80109934
    for(long s1 = 0; s1 < 8; s1++) {
      v0 = sp18.offset(s1 * 0x4L).get();
      a0 = v0 & 0x1fL;
      v0 = v0 >>> 5;
      v0 = MEMORY.ref(4, s4).offset(v0 * 0x4L).get();
      if((v0 & 0x1L << a0) != 0) {
        final Drgn0_6666Struct58 struct = FUN_80023b54(drgn0File6666Address_800bdc3c.get(), null);
        sp18.offset(2, 0x4L).setu(a2 & 0xffL);
        sp18.offset(2, 0x2L).setu(s2);
        sp18.offset(1, 0x0L).setu(s1 + 0xdL);
        FUN_80104b1c(struct, sp18.getAddress());
        struct._3c.set(0x21);
      }

      //LAB_801099a0
      s2 = s2 + 0xcL;
    }

    tmp.release();
  }

  @Method(0x80109b08L)
  public static long executeMemcardLoadingStage(final long a0, final long memcardData) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long s0;

    LOGGER.info("Memcard loading stage %d", memcardSaveLoadingStage_8011e0d4.get());

    switch((int)memcardSaveLoadingStage_8011e0d4.get()) {
      case 1 -> {
        _8011e0bc.setu(0);
        memcardSaveCount_8011e0c0.setu(0);
        _8011e0c4.setu(0);
        _8011e0c5.setu(0);
        _8011e0c6.setu(0xfL);
        _8011e0c8.setu(0);
        _8011e0cc.setu(0);
        _8011e0d0.setu(0);
        memcardSaveLoadingStage_8011e0d4.addu(0x1L);
        FUN_8002df60(0);
      }

      case 2 -> {
        final Ref<Long> sp50 = new Ref<>();
        final Ref<Long> sp54 = new Ref<>();
        FUN_8002efb8(0, sp50, sp54);

        _8011e0b4.setPointer(addToLinkedListTail(0x280L));
        memcardDataPtr_8011e0b0.setu(addToLinkedListTail(0x100L));

        //LAB_80109bfc
        a1 = memcardData + 0x348L;
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
          memcardSaveLoadingStage_8011e0d4.setu(0x8L);
        } else if(v1 == 0x4L) {
          //LAB_80109ea4
          _8011e0c8.setu(v1);
          memcardSaveLoadingStage_8011e0d4.setu(0x8L);
        } else {
          //LAB_80109c30
          final Ref<Long> fileCount = new Ref<>(memcardSaveCount_8011e0c0.get());
          v0 = FUN_8002ed48(0, asterisk_800fbadc.getAddress(), _8011e0b4.deref(), fileCount, 0, 0xfL);
          memcardSaveCount_8011e0c0.setu(fileCount.get());
          _8011e0c8.setu(v0);
          if(v0 != 0) {
            v1 = 0x8L;
          } else {
            v1 = 0x4L;
          }

          //LAB_80109c70
          memcardSaveLoadingStage_8011e0d4.setu(v1);
          memcardSaveIndex_8011e0b8.set(0);
        }

        //LAB_8010a098
      }

      case 4 -> {
        for(int memcardSaveIndex = memcardSaveIndex_8011e0b8.get(); memcardSaveIndex < memcardSaveCount_8011e0c0.get(); memcardSaveIndex++) {
          //LAB_80109cb4
          final MemcardStruct28 struct = _8011e0b4.deref().get(memcardSaveIndex);

          if(struct._18.get() >= 0) {
            a3 = struct._18.get();
          } else {
            a3 = struct._18.get() + 0x1fffL;
          }

          //LAB_80109ce0
          a3 = a3 >> 13;
          s0 = a3 + (struct._18.get() & 0x1fffL) > 0 ? 1 : 0;
          _8011e0c6.subu(s0);
          if(strncmp(struct.name_00.get(), "BASCUS-94491drgnpda", 19) == 0) {
            _8011e0c5.setu(s0);
            //LAB_80109d28
          } else if(strncmp(struct.name_00.get(), "BASCUS-94491drgn00", 18) != 0) {
            _8011e0bc.addu(s0);
          } else {
            //LAB_80109d70
            FUN_8002e908(0, struct.name_00.get(), memcardDataPtr_8011e0b0.get(), 0x180L, 0x80L);

            final Ref<Long> sp50 = new Ref<>();
            final Ref<Long> sp54 = new Ref<>();
            if(FUN_8002efb8(0x1L, sp50, sp54) != 0) {
              if(sp54.get() == 0) {
                s0 = memcardDataPtr_8011e0b0.deref(1).offset(0x4L).get();

                if(s0 < 0xfL && MEMORY.ref(1, memcardData).offset(s0 * 0x3cL).offset(0x4L).get() == 0xffL) {
                  //LAB_80109e38
                  if(memcardDataPtr_8011e0b0.deref(4).get() == 0x5a02_0006L) {
                    memcpy(memcardData + s0 * 0x3cL, memcardDataPtr_8011e0b0.get(), 0x3c);

                    final long a0_0 = MEMORY.ref(4, memcardData).offset(s0 * 0x3cL).offset(0x30L).get();
                    if(_8011e0cc.get() < a0_0) { //TODO maybe selects the newest save?
                      _8011e0cc.setu(a0_0);
                      _8011e0d0.setu(s0);
                    }

                    //LAB_80109e7c
                    _8011e0c4.addu(0x1L);
                  } else {
                    //LAB_80109e90
                    MEMORY.ref(1, memcardData).offset(s0 * 0x3cL).offset(0x4L).setu(0xfdL);
                  }
                } else {
                  //LAB_80109e1c
                  MEMORY.ref(1, memcardData).offset(s0 * 0x3cL).offset(0x4L).setu(0xfeL);
                }
              }
            }
          }
        }

        memcardSaveLoadingStage_8011e0d4.setu(0x7L);

        //LAB_8010a098
      }

      // Save files loaded
      case 7 -> {
        s0 = 0xeL;

        //LAB_80109f04
        for(a2 = 0; a2 < (int)_8011e0c5.get(); a2++) {
          //LAB_80109f28
          while(MEMORY.ref(1, memcardData).offset(s0 * 0x3cL).offset(0x4L).get() != 0xffL) {
            if(s0 != 0) {
              s0--;
            }

            //LAB_80109f38
          }

          //LAB_80109f58
          MEMORY.ref(1, memcardData).offset(s0 * 0x3cL).offset(0x4L).setu(0xfcL);
        }

        //LAB_80109f80
        //LAB_80109fa4
        for(a2 = 0; a2 < (int)_8011e0bc.get(); a2++) {
          //LAB_80109fc8
          while(MEMORY.ref(1, memcardData).offset(s0 * 0x3cL).offset(0x4L).get() != 0xffL) {
            if(s0 != 0) {
              s0--;
            }

            //LAB_80109fd8
          }

          //LAB_80109ff8
          MEMORY.ref(1, memcardData).offset(s0 * 0x3cL).offset(0x4L).setu(0xfeL);
        }

        //LAB_8010a068
        //LAB_8010a06c
        removeFromLinkedList(_8011e0b4.getPointer());
        removeFromLinkedList(memcardDataPtr_8011e0b0.get());
        memcardSaveLoadingStage_8011e0d4.setu(0);

        //LAB_8010a098
      }

      case 8 -> {
        memcardSaveCount_8011e0c0.setu(0);
        _8011e0c4.setu(0);
        _8011e0c5.setu(0);
        _8011e0c6.setu(0xfL);
        _8011e0cc.setu(0);
        _8011e0d0.setu(0);

        //LAB_8010a058
        a1 = memcardData + 0x348L;
        a2 = 0xeL;
        do {
          MEMORY.ref(1, a1).offset(0x4L).setu(0xffL);
          a1 -= 0x3cL;
          a2--;
        } while((int)a2 >= 0);

        //LAB_8010a068
        //LAB_8010a06c
        removeFromLinkedList(_8011e0b4.getPointer());
        removeFromLinkedList(memcardDataPtr_8011e0b0.get());
        memcardSaveLoadingStage_8011e0d4.setu(0);

        //LAB_8010a098
      }
    }

    //LAB_8010a09c
    memcpy(a0, memcardSaveCount_8011e0c0.getAddress(), 0x14);
    return a0;
  }

  @Method(0x8010a0ecL)
  public static long FUN_8010a0ec(long a0) {
    final long s0 = addToLinkedListTail(0x680L);
    FUN_8002e908(0, "BASCUS-94491drgn00%02d".formatted(a0), s0, 0x200L, 0x580L);

    final Ref<Long> sp0x38 = new Ref<>(0L);
    final Ref<Long> sp0x3c = new Ref<>(1L);
    FUN_8002efb8(0, sp0x38, sp0x3c);
    final long sp3c = sp0x3c.get();

    if(sp3c == 0) {
      //LAB_8010a178
      //LAB_8010a17c
      for(int v1 = 0; v1 < 0x14bL; v1++) {
        DebugHelper.sleep(10);
      }

      memcpy(_800babc8.getAddress(), s0, 0x52c);
      return 0;
    }

    if(sp3c == 0x1L) {
      //LAB_8010a1a4
      //LAB_8010a1a8
      removeFromLinkedList(s0);
      return 0x1L;
    }

    return 0x3L;
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
  public static long FUN_8010ecec(final MenuStruct20 a0) {
    Drgn0_6666Struct58 drgn0_6666;
    long a3;
    long t0;
    int s0;
    long s1;
    long s3;
    long s4;
    long s5;
    long v1;

    switch(a0._0c.get()) {
      case 0:
        return 0x1L;

      case 1:
        a0._0c.set(0x2);
        a0.drgn0_6666_04.clear();
        drgn0_6666 = FUN_80103818(0x95L, 0x8eL, a0._1c.get() - 0x32L, a0._1e.get() - 0xaL);
        a0.drgn0_6666_08.set(drgn0_6666);
        drgn0_6666._3c.set(0x20);
        _8011e1e8.setu(0);
        a0.drgn0_6666_08.deref()._18.set(0x8eL);

      case 2:
        if(a0.drgn0_6666_08.deref()._0c.get() != 0) {
          a0._0c.set(0x3);
        }

        break;

      case 3:
        _800bdf00.setu(0x1fL);
        s5 = (a0._1c.get() & 0xff) + 0x3cL;
        s4 = (a0._1e.get() & 0xff) + 0x7L;
        a0._10.incr();
        if(!a0._00.isNull()) {
          t0 = FUN_80103dd4(a0._00.deref()) & 0xffffL;
          final Memory.TemporaryReservation sp0x38tmp = MEMORY.temp((int)((t0 + 0x1L) * 0x2L));
          final ArrayRef<UnsignedShortRef> sp0x38 = sp0x38tmp.get().cast(ArrayRef.of(UnsignedShortRef.class, (int)t0 + 1, 2, UnsignedShortRef::new));

          final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp((int)((t0 + 0x1L) * 0x4L));
          final ArrayRef<Pointer<ArrayRef<UnsignedShortRef>>> sp0x10 = sp0x10tmp.get().cast(ArrayRef.of(Pointer.classFor(ArrayRef.classFor(UnsignedShortRef.class)), (int)t0 + 1, 4, Pointer.deferred(4, ArrayRef.of(UnsignedShortRef.class, (int)t0 + 1, 2, UnsignedShortRef::new))));

          sp0x10.get(0).set(sp0x38);

          s3 = 0x1L;

          //LAB_8010ee1c
          for(s0 = 0; s0 < (int)t0; s0++) {
            sp0x38.get(s0).set(a0._00.deref().get(s0).get());

            if(sp0x38.get(s0).get() == 0xa1ffL) {
              sp0x10.get((int)s3).set(MEMORY.ref(2, sp0x38.get(s0 + 1).getAddress(), ArrayRef.of(UnsignedShortRef.class, (int)t0 + 1, 2, UnsignedShortRef::new))); //TODO
              sp0x38.get(s0).set(0xa0ff);
              s3++;
            }

            //LAB_8010ee50
          }

          //LAB_8010ee68
          sp0x38.get(s0).set(0xa0ff);

          //LAB_8010ee80
          for(int i = 0; i < s3; i++) {
            FUN_80103e90(sp0x10.get(i).deref(), s5 & 0xffL, s4 & 0xffL, 0x4L);

            s4 = s4 + 0xeL;
          }

          sp0x10tmp.release();
          sp0x38tmp.release();
        }

        //LAB_8010eeac
        _800bdf00.setu(0x21L);
        s1 = a0._15.get();

        if(s1 == 0) {
          //LAB_8010eed8
          if((inventoryJoypadInput_800bdc44.get() & 0x60L) != 0) {
            playSound(0x2L);
            a0._0c.set(0x4);
            _8011e1e8.setu(0x1L);
          }

          break;
        }

        if(s1 != 0x2L) {
          break;
        }

        //LAB_8010ef10
        if(a0.drgn0_6666_04.isNull()) {
          drgn0_6666 = FUN_80103818(0x7dL, 0x7dL, a0._1c.get() + 0x2dL, a0._18.get() * 0xeL + s4 + 0x5L);
          a0.drgn0_6666_04.set(drgn0_6666);
          drgn0_6666._38.set(0);
          drgn0_6666._34.set(0);
          a0.drgn0_6666_04.deref()._3c.set(0x20);
        }

        //LAB_8010ef64
        _800bdf00.setu(0x1fL);

        if(a0._18.get() != 0) {
          a3 = 0x4L;
        } else {
          a3 = 0x5L;
        }

        //LAB_8010ef98
        FUN_80103e90(_8011c20c, (a0._1c.get() + 0x3cL) & 0xffffL, s4 + 0x7L, a3);

        if(a0._18.get() != 0) {
          a3 = 0x5L;
        } else {
          a3 = 0x4L;
        }

        //LAB_8010efc8
        FUN_80103e90(_8011c214, (a0._1c.get() + 0x3cL) & 0xffffL, s4 + 0x15L, a3);

        _800bdf00.setu(0x21L);
        v1 = FUN_801041d8(a0._18.getAddress()); //TODO address
        if(v1 == 0x2L) {
          //LAB_8010f040
          a0._0c.set(0x4);
          _8011e1e8.setu(a0._18.get() + 0x1L);
        } else if((int)v1 >= 0x3L) {
          //LAB_8010f000
          if((int)v1 < 0x5L) {
            //LAB_8010f05c
            a0._0c.set(0x4);
            _8011e1e8.setu(0x2L);
          }
        } else if(v1 == 0x1L) {
          //LAB_8010f014
          a0.drgn0_6666_04.deref().y_44.set(a0._18.get() * 0xeL + s4 + 0x5L);
        }

        break;

      case 4:
        a0._0c.set(0x5);

        if(!a0.drgn0_6666_04.isNull()) {
          unloadDrgn0_6666Struct(a0.drgn0_6666_04.deref());
        }

        //LAB_8010f084
        unloadDrgn0_6666Struct(a0.drgn0_6666_08.deref());
        drgn0_6666 = FUN_80103818(0x8eL, 0x95L, a0._1c.get() - 0x32L, a0._1e.get() - 0xaL);
        a0.drgn0_6666_08.set(drgn0_6666);
        drgn0_6666._3c.set(0x20);
        a0.drgn0_6666_08.deref()._00.or(0x10L);
        break;

      case 5:
        if(a0.drgn0_6666_08.deref()._0c.get() != 0) {
          a0._0c.set(0x6);
        }

        break;

      case 6:
        a0._0c.set(0);
        _8011e1e8.get();
    }

    //LAB_8010f108
    //LAB_8010f10c
    return 0;
  }

  @Method(0x8010f130L)
  public static void FUN_8010f130(@Nullable final ArrayRef<UnsignedShortRef> a0, final int a1) {
    _8011dc90._00.setNullable(a0);

    _8011dc90._1c.set(0x78);
    _8011dc90._1e.set(0x64);
    _8011dc90._15.set(a1);
    _8011dc90._18.set(0);
    _8011dc90._10.set(0);
    _8011dc90._0c.set(0x1);
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
