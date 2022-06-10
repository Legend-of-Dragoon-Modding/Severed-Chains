package legend.game;

import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.combat.Bttl_800c;
import legend.game.combat.types.BattleStruct1a8;
import legend.game.combat.types.BtldScriptData27c;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.LodString;
import legend.game.types.MemcardDataStruct3c;
import legend.game.types.MemcardStruct28;
import legend.game.types.MenuAdditionInfo;
import legend.game.types.MenuStruct08;
import legend.game.types.MessageBox20;
import legend.game.types.MrgFile;
import legend.game.types.Renderable58;
import legend.game.types.ScriptState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.Scus94491BpeSegment.FUN_800127cc;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_800133ac;
import static legend.game.Scus94491BpeSegment.FUN_80013434;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.decompress;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback0c;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022898;
import static legend.game.Scus94491BpeSegment_8002.FUN_800228d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022928;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022a10;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022a94;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022afc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022b50;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022d88;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023148;
import static legend.game.Scus94491BpeSegment_8002.FUN_800232dc;
import static legend.game.Scus94491BpeSegment_8002.FUN_800233d8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023484;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002379c;
import static legend.game.Scus94491BpeSegment_8002.FUN_800239e0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023a2c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002437c;
import static legend.game.Scus94491BpeSegment_8002.FUN_800297a0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a6fc;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a86c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a8f8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bcc8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c150;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002dbdc;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002df60;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002eb28;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002ed48;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002efb8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002f0d4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002f1d0;
import static legend.game.Scus94491BpeSegment_8002.addMp;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.getJoypadInputByPriority;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;
import static legend.game.Scus94491BpeSegment_8002.memcardVsyncInterruptHandler;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.readMemcardFile;
import static legend.game.Scus94491BpeSegment_8002.strncmp;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8004.FUN_80041070;
import static legend.game.Scus94491BpeSegment_8004.FUN_800412e0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80041420;
import static legend.game.Scus94491BpeSegment_8004.FUN_800414a0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80041600;
import static legend.game.Scus94491BpeSegment_8004.FUN_800426c4;
import static legend.game.Scus94491BpeSegment_8004._8004dd30;
import static legend.game.Scus94491BpeSegment_8004._8004f2ac;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.loadingSmapOvl_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.setMonoOrStereo;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._8005a368;
import static legend.game.Scus94491BpeSegment_8005._8005e398;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.memcardEventIndex_80052e4c;
import static legend.game.Scus94491BpeSegment_8005.spells_80052734;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006f280;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bdb9c;
import static legend.game.Scus94491BpeSegment_800b._800bdba0;
import static legend.game.Scus94491BpeSegment_800b._800bdbf8;
import static legend.game.Scus94491BpeSegment_800b._800bdbfc;
import static legend.game.Scus94491BpeSegment_800b._800bdc00;
import static legend.game.Scus94491BpeSegment_800b._800bdc04;
import static legend.game.Scus94491BpeSegment_800b._800bdc08;
import static legend.game.Scus94491BpeSegment_800b._800bdc0c;
import static legend.game.Scus94491BpeSegment_800b._800bdc2c;
import static legend.game.Scus94491BpeSegment_800b._800bdc30;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bdf00;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b._800be5d8;
import static legend.game.Scus94491BpeSegment_800b._800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.inventoryJoypadInput_800bdc44;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbe8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbec;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc20;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe0;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe4;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.combat.Bttl_800c.FUN_800c8f50;
import static legend.game.combat.Bttl_800c.FUN_800c9290;
import static legend.game.combat.Bttl_800c.FUN_800ca75c;
import static legend.game.combat.Bttl_800c._800c66d0;
import static legend.game.combat.Bttl_800c._800c677c;
import static legend.game.combat.Bttl_800c.battleStruct1a8Count_800c66a0;
import static legend.game.combat.Bttl_800c.getBattleStruct1a8;
import static legend.game.combat.Bttl_800f.FUN_800f863c;

public final class SItem {
  private SItem() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SItem.class);

  /** String: "BASCUS-94491drgnpda" */
  public static final Value drgnpda_800fb7a0 = MEMORY.ref(1, 0x800fb7a0L);

  public static final Value _800fba58 = MEMORY.ref(4, 0x800fba58L);

  public static final ArrayRef<UnsignedByteRef> additionXpPerLevel_800fba2c = MEMORY.ref(1, 0x800fba2cL, ArrayRef.of(UnsignedByteRef.class, 5, 1, UnsignedByteRef::new));

  public static final ArrayRef<MenuStruct08> _800fba7c = MEMORY.ref(4, 0x800fba7cL, ArrayRef.of(MenuStruct08.class, 8, 8, MenuStruct08::new));

  public static final Value _800fbabc = MEMORY.ref(4, 0x800fbabcL);

  /** String "*" */
  public static final Value asterisk_800fbadc = MEMORY.ref(1, 0x800fbadcL);

  public static final ArrayRef<UnsignedIntRef> _800fbd08 = MEMORY.ref(4, 0x800fbd08L, ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _800fbd30 = MEMORY.ref(4, 0x800fbd30L, ArrayRef.of(UnsignedIntRef.class, 9, 4, UnsignedIntRef::new));
  public static final ArrayRef<UnsignedIntRef> _800fbd54 = MEMORY.ref(4, 0x800fbd54L, ArrayRef.of(UnsignedIntRef.class, 9, 4, UnsignedIntRef::new));

  public static final Value _80111cfc = MEMORY.ref(4, 0x80111cfcL);

  public static final Value _80111d20 = MEMORY.ref(4, 0x80111d20L);

  public static final Value _80111d38 = MEMORY.ref(4, 0x80111d38L);

  /** Contains data for every combination of party members (like a DRGN0 file index that contains the textures and models of each char */
  public static final Value partyPermutations_80111d68 = MEMORY.ref(1, 0x80111d68L);

  public static final Value _80111ff0 = MEMORY.ref(1, 0x80111ff0L);

  public static final Value ptrTable_80114070 = MEMORY.ref(4, 0x80114070L);

  /** Array of unknown data, stride 6 */
  public static final Value _80114130 = MEMORY.ref(1, 0x80114130L);

  public static final Value _80114160 = MEMORY.ref(1, 0x80114160L);

  public static final Value _80114180 = MEMORY.ref(1, 0x80114180L);

  public static final Value _801141a4 = MEMORY.ref(1, 0x801141a4L);

  public static final Value _801141c4 = MEMORY.ref(1, 0x801141c4L);

  public static final Value _801141e4 = MEMORY.ref(1, 0x801141e4L);

  public static final Value _801141fc = MEMORY.ref(1, 0x801141fcL);

  public static final Value _80114258 = MEMORY.ref(1, 0x80114258L);

  public static final Value _80114284 = MEMORY.ref(1, 0x80114284L);

  public static final Value _80114290 = MEMORY.ref(1, 0x80114290L);

  public static final Value _801142d4 = MEMORY.ref(1, 0x801142d4L);

  public static final ArrayRef<Pointer<LodString>> chapterNames_80114248 = MEMORY.ref(4, 0x80114248L, ArrayRef.of(Pointer.classFor(LodString.class), 4, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> characterNames_801142dc = MEMORY.ref(4, 0x801142dcL, ArrayRef.of(Pointer.classFor(LodString.class), 9, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> _80114300 = MEMORY.ref(4, 0x80114300L, ArrayRef.of(Pointer.classFor(LodString.class), 4, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> _80117a10 = MEMORY.ref(4, 0x80117a10L, ArrayRef.of(Pointer.classFor(LodString.class), 256, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> equipment_8011972c = MEMORY.ref(4, 0x8011972cL, ArrayRef.of(Pointer.classFor(LodString.class), 256, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> additions_8011a064 = MEMORY.ref(4, 0x8011a064L, ArrayRef.of(Pointer.classFor(LodString.class), 43, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> _8011b75c = MEMORY.ref(4, 0x8011b75cL, ArrayRef.of(Pointer.classFor(LodString.class), 64, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> _8011c008 = MEMORY.ref(4, 0x8011c008L, ArrayRef.of(Pointer.classFor(LodString.class), 64, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> submapNames_8011c108 = MEMORY.ref(4, 0x8011c108L, ArrayRef.of(Pointer.classFor(LodString.class), 57, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> worldMapNames_8011c1ec = MEMORY.ref(4, 0x8011c1ecL, ArrayRef.of(Pointer.classFor(LodString.class), 8, 4, Pointer.deferred(4, LodString::new)));

  public static final LodString _8011c254 = MEMORY.ref(4, 0x8011c254L, LodString::new);

  /** "Yes" */
  public static final LodString Yes_8011c20c = MEMORY.ref(2, 0x8011c20cL, LodString::new);
  /** "No" */
  public static final LodString No_8011c214 = MEMORY.ref(2, 0x8011c214L, LodString::new);
  public static final LodString _8011c314 = MEMORY.ref(2, 0x8011c314L, LodString::new);
  public static final LodString _8011c32c = MEMORY.ref(2, 0x8011c32cL, LodString::new);
  public static final LodString _8011c340 = MEMORY.ref(2, 0x8011c340L, LodString::new);
  /** "Do you want to save now?" */
  public static final LodString Do_you_want_to_save_now_8011c370 = MEMORY.ref(2, 0x8011c370L, LodString::new);
  /** "" */
  public static final LodString Blank_8011c3a4 = MEMORY.ref(2, 0x8011c3a4L, LodString::new);
  /** "Confused" */
  public static final LodString Confused_8011c3bc = MEMORY.ref(2, 0x8011c3bcL, LodString::new);
  /** "" */
  public static final LodString Blank_8011c5d8 = MEMORY.ref(2, 0x8011c5d8L, LodString::new);
  /** "" */
  public static final LodString Blank_8011c664 = MEMORY.ref(2, 0x8011c664L, LodString::new);
  /** "" */
  public static final LodString Blank_8011c668 = MEMORY.ref(2, 0x8011c668L, LodString::new);
  /** "m" */
  public static final LodString m_8011c66c = MEMORY.ref(2, 0x8011c66cL, LodString::new);
  /**
   * "Saved data for"
   * "this game"
   * "does not exist"
   */
  public static final LodString Saved_data_for_this_game_does_not_exist_8011c788 = MEMORY.ref(2, 0x8011c788L, LodString::new);
  /** "File not used" */
  public static final LodString File_not_used_8011c7d8 = MEMORY.ref(2, 0x8011c7d8L, LodString::new);
  public static final LodString Note_8011c814 = MEMORY.ref(2, 0x8011c814L, LodString::new);
  public static final LodString Stay_8011c820 = MEMORY.ref(2, 0x8011c820L, LodString::new);
  public static final LodString Half_8011c82c = MEMORY.ref(2, 0x8011c82cL, LodString::new);
  public static final LodString Off_8011c838 = MEMORY.ref(2, 0x8011c838L, LodString::new);
  /**
   * "Data from another"
   * "game"
   */
  public static final LodString Data_from_another_game_8011c88c = MEMORY.ref(2, 0x8011c88cL, LodString::new);
  /** "" */
  public static final LodString _8011c8bc = MEMORY.ref(2, 0x8011c8bcL, LodString::new);
  /** "" */
  public static final LodString _8011c8c8 = MEMORY.ref(2, 0x8011c8c8L, LodString::new);
  /** "" */
  public static final LodString _8011c8cc = MEMORY.ref(2, 0x8011c8ccL, LodString::new);
  /**
   * "Really want"
   * "to throw"
   * "this away?"
   */
  public static final LodString Really_want_to_throw_this_away_8011c8d4 = MEMORY.ref(2, 0x8011c8d4L, LodString::new);
  /** "Item thrown away" */
  public static final LodString Item_thrown_away_8011c914 = MEMORY.ref(2, 0x8011c914L, LodString::new);
  /**
   * "Checking MEMORY CARD"
   * "Do not remove MEMORY"
   * "CARD or turn off the"
   * "power"
   */
  public static final LodString Checking_MEMORY_CARD_Do_not_remove_MEMORY_CARD_or_turn_off_the_power_8011c93c = MEMORY.ref(2, 0x8011c93cL, LodString::new);
  /** "Save new game?" */
  public static final LodString Save_new_game_8011c9c8 = MEMORY.ref(2, 0x8011c9c8L, LodString::new);
  /** "Overwrite save?" */
  public static final LodString Overwrite_save_8011c9e8 = MEMORY.ref(2, 0x8011c9e8L, LodString::new);
  /** "Load this data?" */
  public static final LodString Load_this_data_8011ca08 = MEMORY.ref(2, 0x8011ca08L, LodString::new);
  /**
   * "Data loading."
   * "Do not remove MEMORY"
   * "CARD or turn off the"
   * "power."
   */
  public static final LodString Data_loading_Do_not_remove_MEMORY_CARD_or_turn_off_the_power_8011ca2c = MEMORY.ref(2, 0x8011ca2cL, LodString::new);
  /**
   * "Data saving."
   * "Do not remove MEMORY"
   * "CARD or turn off the"
   * "power."
   */
  public static final LodString Data_saving_Do_not_remove_MEMORY_CARD_or_turn_off_the_power_8011cab0 = MEMORY.ref(2, 0x8011cab0L, LodString::new);
  /** "Saved" */
  public static final LodString Saved_8011cb2c = MEMORY.ref(2, 0x8011cb2cL, LodString::new);
  /**
   * "MEMORY CARD is not"
   * "inserted in MEMORY"
   * "CARD slot 1"
   */
  public static final LodString MEMORY_CARD_is_not_inserted_in_MEMORY_CARD_slot_1_8011cb38 = MEMORY.ref(2, 0x8011cb38L, LodString::new);
  /** "Failed loading data" */
  public static final LodString Failed_loading_data_8011cb9c = MEMORY.ref(2, 0x8011cb9cL, LodString::new);
  /**
   * "Cannot access the"
   * "MEMORY CARD in MEMORY"
   * "CARD slot 1."
   */
  public static final LodString Cannot_access_the_MEMORY_CARD_in_MEMORY_CARD_slot_1_8011cbc4 = MEMORY.ref(2, 0x8011cbc4L, LodString::new);
  /**
   * "Not enough blocks."
   * "Saving data requires"
   * "1 block"
   */
  public static final LodString Not_enough_blocks_Saving_data_requires_1_block_8011cc30 = MEMORY.ref(2, 0x8011cc30L, LodString::new);
  /** "Failed saving data" */
  public static final LodString Failed_saving_data_8011cc90 = MEMORY.ref(2, 0x8011cc90L, LodString::new);
  /**
   * "The MEMORY CARD in"
   * "MEMORY CARD slot 1"
   * "is not formatted"
   */
  public static final LodString The_MEMORY_CARD_in_MEMORY_CARD_slot_1_is_not_formatted_8011ccb8 = MEMORY.ref(2, 0x8011ccb8L, LodString::new);
  /** "Do you want to format?" */
  public static final LodString Do_you_want_to_format_8011cd28 = MEMORY.ref(2, 0x8011cd28L, LodString::new);
  /**
   * "Formatting."
   * "Do not remove MEMORY"
   * "CARD or turn off the"
   * "power."
   */
  public static final LodString Formatting_Do_not_remove_MEMORY_CARD_or_turn_off_the_power_8011cd58 = MEMORY.ref(2, 0x8011cd58L, LodString::new);
  /** "Formatting failed" */
  public static final LodString Formatting_failed_8011cdd8 = MEMORY.ref(2, 0x8011cdd8L, LodString::new);
  /** "" */
  public static final LodString _8011cdfc = MEMORY.ref(2, 0x8011cdfcL, LodString::new);
  /** "" */
  public static final LodString _8011ce00 = MEMORY.ref(2, 0x8011ce00L, LodString::new);
  /** "" */
  public static final LodString _8011ce04 = MEMORY.ref(2, 0x8011ce04L, LodString::new);
  /** "" */
  public static final LodString _8011ce08 = MEMORY.ref(2, 0x8011ce08L, LodString::new);
  /** "" */
  public static final LodString _8011ce0c = MEMORY.ref(2, 0x8011ce0cL, LodString::new);
  /** "" */
  public static final LodString _8011ce10 = MEMORY.ref(2, 0x8011ce10L, LodString::new);
  /** "" */
  public static final LodString _8011ce14 = MEMORY.ref(2, 0x8011ce14L, LodString::new);
  /** "" */
  public static final LodString _8011ce18 = MEMORY.ref(2, 0x8011ce18L, LodString::new);
  /** "Status" */
  public static final LodString Status_8011ceb4 = MEMORY.ref(2, 0x8011ceb4L, LodString::new);
  /** "Item" */
  public static final LodString Item_8011cec4 = MEMORY.ref(2, 0x8011cec4L, LodString::new);
  /** "Armed" */
  public static final LodString Armed_8011ced0 = MEMORY.ref(2, 0x8011ced0L, LodString::new);
  /** "Addition" */
  public static final LodString Addition_8011cedc = MEMORY.ref(2, 0x8011cedcL, LodString::new);
  /** "Replace" */
  public static final LodString Replace_8011cef0 = MEMORY.ref(2, 0x8011cef0L, LodString::new);
  /** "Config" */
  public static final LodString Config_8011cf00 = MEMORY.ref(2, 0x8011cf00L, LodString::new);
  /** "Save" */
  public static final LodString Save_8011cf10 = MEMORY.ref(2, 0x8011cf10L, LodString::new);
  /** "Use it" */
  public static final LodString Use_it_8011cf1c = MEMORY.ref(2, 0x8011cf1cL, LodString::new);
  /** "Discard" */
  public static final LodString Discard_8011cf2c = MEMORY.ref(2, 0x8011cf2cL, LodString::new);
  /** "List" */
  public static final LodString List_8011cf3c = MEMORY.ref(2, 0x8011cf3cL, LodString::new);
  /** "Goods" */
  public static final LodString Goods_8011cf48 = MEMORY.ref(2, 0x8011cf48L, LodString::new);
  /** "" */
  public static final LodString _8011cf54 = MEMORY.ref(2, 0x8011cf54L, LodString::new);
  public static final LodString Vibrate_8011cf58 = MEMORY.ref(2, 0x8011cf58L, LodString::new);
  public static final LodString Off_8011cf6c = MEMORY.ref(2, 0x8011cf6cL, LodString::new);
  public static final LodString On_8011cf74 = MEMORY.ref(2, 0x8011cf74L, LodString::new);
  public static final LodString Sound_8011cf7c = MEMORY.ref(2, 0x8011cf7cL, LodString::new);
  public static final LodString Stereo_8011cf88 = MEMORY.ref(2, 0x8011cf88L, LodString::new);
  public static final LodString Mono_8011cf98 = MEMORY.ref(2, 0x8011cf98L, LodString::new);
  public static final LodString Morph_8011cfa4 = MEMORY.ref(2, 0x8011cfa4L, LodString::new);
  public static final LodString Normal_8011cfb0 = MEMORY.ref(2, 0x8011cfb0L, LodString::new);
  public static final LodString Short_8011cfc0 = MEMORY.ref(2, 0x8011cfc0L, LodString::new);
  public static final LodString _8011cfcc = MEMORY.ref(2, 0x8011cfccL, LodString::new);
  public static final LodString _8011cff8 = MEMORY.ref(2, 0x8011cff8L, LodString::new);
  public static final LodString _8011d024 = MEMORY.ref(2, 0x8011d024L, LodString::new);
  /** "" */
  public static final LodString _8011d044 = MEMORY.ref(2, 0x8011d044L, LodString::new);
  /** "" */
  public static final LodString _8011d048 = MEMORY.ref(2, 0x8011d048L, LodString::new);
  /** "" */
  public static final LodString _8011d04c = MEMORY.ref(2, 0x8011d04cL, LodString::new);
  /** "" */
  public static final LodString _8011d060 = MEMORY.ref(2, 0x8011d060L, LodString::new);
  public static final LodString _8011d534 = MEMORY.ref(2, 0x8011d534L, LodString::new);
  public static final LodString _8011d560 = MEMORY.ref(2, 0x8011d560L, LodString::new);
  public static final LodString _8011d57c = MEMORY.ref(2, 0x8011d57cL, LodString::new);
  public static final LodString _8011d584 = MEMORY.ref(2, 0x8011d584L, LodString::new);
  public static final LodString _8011d58c = MEMORY.ref(2, 0x8011d58cL, LodString::new);
  public static final LodString _8011d594 = MEMORY.ref(2, 0x8011d594L, LodString::new);
  /** "" */
  public static final LodString _8011d5c4 = MEMORY.ref(2, 0x8011d5c4L, LodString::new);
  public static final LodString _8011d5c8 = MEMORY.ref(2, 0x8011d5c8L, LodString::new);
  public static final LodString _8011d5e0 = MEMORY.ref(2, 0x8011d5e0L, LodString::new);
  public static final LodString _8011d604 = MEMORY.ref(2, 0x8011d604L, LodString::new);
  public static final LodString _8011d618 = MEMORY.ref(2, 0x8011d618L, LodString::new);
  /** "" */
  public static final LodString _8011d700 = MEMORY.ref(2, 0x8011d700L, LodString::new);
  /** "" */
  public static final LodString _8011d704 = MEMORY.ref(2, 0x8011d704L, LodString::new);
  /** "" */
  public static final LodString _8011d708 = MEMORY.ref(2, 0x8011d708L, LodString::new);
  /** "" */
  public static final LodString _8011d70c = MEMORY.ref(2, 0x8011d70cL, LodString::new);
  /** "" */
  public static final LodString _8011d710 = MEMORY.ref(2, 0x8011d710L, LodString::new);

  public static final ArrayRef<Pointer<Renderable58>> _8011d718 = MEMORY.ref(4, 0x8011d718L, ArrayRef.of(Pointer.classFor(Renderable58.class), 7, 4, Pointer.deferred(4, Renderable58::new)));

  public static final IntRef charSlot_8011d734 = MEMORY.ref(4, 0x8011d734L, IntRef::new);
  public static final Value selectedMenuOption_8011d738 = MEMORY.ref(4, 0x8011d738L);
  public static final Value selectedItemSubmenuOption_8011d73c = MEMORY.ref(4, 0x8011d73cL);
  public static final Value selectedSlot_8011d740 = MEMORY.ref(4, 0x8011d740L);
  /** The first save game displayed on the menu, increments as you scroll down */
  public static final Value slotScroll_8011d744 = MEMORY.ref(4, 0x8011d744L);
  public static final Value _8011d748 = MEMORY.ref(4, 0x8011d748L);
  public static final Value _8011d74c = MEMORY.ref(4, 0x8011d74cL);
  public static final Value _8011d750 = MEMORY.ref(4, 0x8011d750L);
  public static final Value _8011d754 = MEMORY.ref(4, 0x8011d754L);

  public static final Value _8011d768 = MEMORY.ref(4, 0x8011d768L);

  public static final Value _8011d788 = MEMORY.ref(1, 0x8011d788L);

  public static final Value _8011d78c = MEMORY.ref(4, 0x8011d78cL);

  public static final LodString _8011d790 = MEMORY.ref(2, 0x8011d790L, LodString::new);

  /** Also used for memcard state? */
  public static final Value fileCount_8011d7b8 = MEMORY.ref(4, 0x8011d7b8L);
  public static final Value _8011d7bc = MEMORY.ref(4, 0x8011d7bcL);

  public static final Value _8011d7c4 = MEMORY.ref(1, 0x8011d7c4L);

  public static final Value _8011d7c8 = MEMORY.ref(1, 0x8011d7c8L);

  public static final Value canSave_8011dc88 = MEMORY.ref(1, 0x8011dc88L);

  public static final Value _8011dc8c = MEMORY.ref(4, 0x8011dc8cL);
  public static final MessageBox20 messageBox_8011dc90 = MEMORY.ref(4, 0x8011dc90L, MessageBox20::new);

  public static final Value _8011dcb8 = MEMORY.ref(4, 0x8011dcb8L);
  public static final Value _8011dcbc = MEMORY.ref(4, 0x8011dcbcL);
  public static final Value _8011dcc0 = MEMORY.ref(4, 0x8011dcc0L);

  public static final BoolRef _8011dcfc = MEMORY.ref(1, 0x8011dcfcL, BoolRef::new);

  public static final Value _8011dd00 = MEMORY.ref(4, 0x8011dd00L);
  public static final Value _8011dd04 = MEMORY.ref(4, 0x8011dd04L);
  public static final Value _8011dd08 = MEMORY.ref(4, 0x8011dd08L);

  public static final UnboundedArrayRef<MemcardDataStruct3c> memcardData_8011dd10 = MEMORY.ref(4, 0x8011dd10L, UnboundedArrayRef.of(0x3c, MemcardDataStruct3c::new));

  public static final ArrayRef<MenuAdditionInfo> additions_8011e098 = MEMORY.ref(1, 0x8011e098L, ArrayRef.of(MenuAdditionInfo.class, 9, 0x2, MenuAdditionInfo::new));

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

  @Method(0x800fbd78L)
  public static void FUN_800fbd78(final long a0) {
    //LAB_800fbdb8
    for(_800c677c.setu(0); _800c677c.get() < 3; _800c677c.addu(0x1L)) {
      if(gameState_800babc8.charIndex_88.get((int)_800c677c.get()).get() < 0) {
        break;
      }
    }

    //LAB_800fbde8
    final long fp = _80111d38.offset(_800c677c.get() * 0xcL).getAddress();
    final long[] sp0x18 = new long[(int)_800c677c.get()];

    //LAB_800fbe18
    for(int i = 0; i < _800c677c.get(); i++) {
      sp0x18[i] = FUN_800c8f50(0x200L + gameState_800babc8.charIndex_88.get(i).get() * 0x2L, i);
    }

    //LAB_800fbe4c
    //LAB_800fbe70
    for(int i = 0; i < _800c677c.get(); i++) {
      final long s2 = gameState_800babc8.charIndex_88.get(i).get();
      final int s0 = allocateScriptState(i + 6, 0x27cL, false, 0, 0, BtldScriptData27c::new);
      setCallback04(s0, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cae50", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
      setCallback0c(s0, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb058", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
      _8006e398.offset(4, 0xe0cL).offset(_800c66d0.get() * 0x4L).setu(s0);
      _8006e398.offset(4, 0xe40L).offset(i * 0x4L).setu(s0);
      final long s1 = scriptStatePtrArr_800bc1c0.get((int)s0).deref().innerStruct_00.getPointer(); //TODO
      MEMORY.ref(4, s1).offset(0x0L).setu(0x4a42_4f42L); // BOBJ
      MEMORY.ref(4, s1).offset(0x144L).setu(getBattleStruct1a8((short)sp0x18[i]).getAddress()); //TODO
      MEMORY.ref(2, s1).offset(0x272L).setu(s2);
      MEMORY.ref(2, s1).offset(0x276L).setu(i);
      MEMORY.ref(2, s1).offset(0x26cL).setu(sp0x18[i]);
      MEMORY.ref(2, s1).offset(0x274L).setu(_800c66d0.get());
      MEMORY.ref(4, s1).offset(0x178L).setu(0);
      MEMORY.ref(4, s1).offset(0x174L).setu(MEMORY.ref(2, fp).offset(i * 0x4L).offset(0x0L).getSigned());
      MEMORY.ref(2, s1).offset(0x1bcL).setu(0);
      MEMORY.ref(2, s1).offset(0x1beL).setu(0x400L);
      MEMORY.ref(2, s1).offset(0x1c0L).setu(0);
      MEMORY.ref(4, s1).offset(0x17cL).setu(MEMORY.ref(2, fp).offset(i * 0x4L).offset(0x2L).getSigned());
      _800c66d0.addu(0x1L);
    }

    //LAB_800fbf6c
    _8006e398.offset(_800c66d0.get() * 0x4L).offset(4, 0xe0cL).setu(-0x1L);
    _8006e398.offset(_800c677c.get() * 0x4L).offset(4, 0xe40L).setu(-0x1L);

    FUN_800f863c();
    FUN_80012bb4();
  }

  @Method(0x800fbfe0L)
  public static void loadEncounterAssets(final long param) {
    FUN_80012b1c(0x2L, getMethodAddress(SItem.class, "loadEnemyTextures", long.class), 2625 + encounterId_800bb0f8.get());

    //LAB_800fc030
    for(int i = 0; i < battleStruct1a8Count_800c66a0.get(); i++) {
      if(getBattleStruct1a8(i)._19c.get() < 0) {
        FUN_800c9290(i);
      }

      //LAB_800fc050
    }

    //LAB_800fc064
    //LAB_800fc09c
    for(int i = 0; i < _800c677c.get(); i++) {
      _8005e398.get(scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe40L).offset(i * 0x4L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._26c.get())._19e.or(0x2a);
    }

    //LAB_800fc104
    final long v1 = _800c677c.get();
    final long s2;
    if(v1 == 1) {
      //LAB_800fc140
      s2 = partyPermutations_80111d68.offset(gameState_800babc8.charIndex_88.get(0).get() * 0x48L).getAddress();
    } else if(v1 == 2) {
      //LAB_800fc164
      s2 = partyPermutations_80111d68.offset(gameState_800babc8.charIndex_88.get(0).get() * 0x48L).offset(gameState_800babc8.charIndex_88.get(1).get() * 0x8L).getAddress();
      //LAB_800fc12c
    } else if(v1 == 3) {
      //LAB_800fc174
      //LAB_800fc180
      s2 = partyPermutations_80111d68.offset(gameState_800babc8.charIndex_88.get(1).get() * 0x48L).offset(gameState_800babc8.charIndex_88.get(2).get() * 0x8L).getAddress();
    } else {
      throw new RuntimeException("Invalid value " + v1);
    }

    //LAB_800fc19c
    //LAB_800fc1a4
    _8006f280.setu(s2);
    FUN_80012b1c(0x2L, getMethodAddress(SItem.class, "FUN_800fc504", long.class), MEMORY.ref(2, s2).getSigned());
    FUN_80012b1c(0x2L, getMethodAddress(SItem.class, "FUN_800fc654", long.class), MEMORY.ref(2, s2).getSigned() + 0x1L);
    _800bc960.oru(0x400L);
    FUN_80012bb4();
  }

  @Method(0x800fc210L)
  public static void FUN_800fc210(final long address, final long fileSize, final long param) {
    final long s3 = _8006e398.offset(4, 0xee8L).get();

    //LAB_800fc260
    long s0 = 0; //TODO this was uninitialized
    for(int s1 = 0; s1 < _800c677c.get(); s1++) {
      final BtldScriptData27c data = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe40L).offset(s1 * 0x4L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
      final BattleStruct1a8 v0 = data._144.deref();

      //LAB_800fc298
      for(int a1 = 0; a1 < 3; a1++) {
        if(MEMORY.ref(2, s3).offset(a1 * 0x2L).offset(0x2L).getSigned() == data._272.get()) {
          s0 = s0 & 0xffff_ff80L;
          s0 = s0 | (v0._19c.get() & 0x7fL);
          s0 = s0 & 0xffff_81ffL;
          s0 = s0 | ((data._26c.get() & 0x3fL) << 9);
          s0 = s0 & 0xffff_ff7fL;
          s0 = s0 & 0xffff_feffL;
          decompress(address + MEMORY.ref(4, address).offset(a1 * 0x8L).offset(0x8L).get(), _1f8003f4.deref()._9cdc.offset(v0._19c.get() * 0x4L).get(), getMethodAddress(Bttl_800c.class, "FUN_800c941c", long.class, long.class, long.class), s0, 0);
          break;
        }

        //LAB_800fc324
      }

      //LAB_800fc338
    }

    //LAB_800fc34c
    _800bc960.oru(0x4L);
    FUN_800127cc(address, 0, 0x1L);
    FUN_80012bb4();
  }

  @Method(0x800fc3c0L)
  public static void loadEnemyTextures(final long fileIndex) {
    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(SItem.class, "enemyTexturesLoadedCallback", long.class, long.class, long.class), 0, 0x5L);
  }

  @Method(0x800fc404L)
  public static void enemyTexturesLoadedCallback(final long address, final long fileSize, final long param) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    final long s2 = _1f8003f4.getPointer(); //TODO

    //LAB_800fc434
    for(int i = 0; i < battleStruct1a8Count_800c66a0.get(); i++) {
      final BattleStruct1a8 a0 = getBattleStruct1a8(i);

      if(a0._19c.get() < 0) {
        final long a2 = a0._1a2.get() & 0x1ffL;

        //LAB_800fc464
        for(int enemySlot = 0; enemySlot < 3; enemySlot++) {
          if((MEMORY.ref(2, s2).offset(enemySlot * 0x2L).get() & 0x1ffL) == a2 && mrg.entries.get(enemySlot).size.get() != 0) {
            FUN_800ca75c(i, mrg.getFile(enemySlot));
            break;
          }

          //LAB_800fc4a0
        }
      }

      //LAB_800fc4b8
    }

    //LAB_800fc4cc
    FUN_800127cc(address, 0, 0x1L);
    FUN_80012bb4();
  }

  @Method(0x800fc504L)
  public static void FUN_800fc504(final long fileIndex) {
    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(SItem.class, "FUN_800fc548", long.class, long.class, long.class), 0, 0x5L);
  }

  @Method(0x800fc548L)
  public static void FUN_800fc548(final long address, final long fileSize, final long param) {
    long s3 = _8006e398.offset(0xee8L).get();

    //LAB_800fc590
    for(long s0 = 0; s0 < _800c677c.get(); s0++) {
      final long a3 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(0xe40L).offset(s0 * 0x4L).get()).deref().innerStruct_00.getPointer(); //TODO

      //LAB_800fc5b4
      for(long a1 = 0; a1 < 3; a1++) {
        if(MEMORY.ref(2, s3).offset(a1 * 0x2L).offset(0x2L).getSigned() == MEMORY.ref(2, a3).offset(0x272L).getSigned()) {
          a1 = address + MEMORY.ref(4, address).offset(a1 * 0x8L).offset(0x8L).get();
          a1 = a1 + MEMORY.ref(4, a1).offset(0x8L).get();
          FUN_800ca75c((int)MEMORY.ref(2, a3).offset(0x26cL).getSigned(), a1);
          break;
        }

        //LAB_800fc5e8
      }

      //LAB_800fc5fc
    }

    //LAB_800fc614
    FUN_800127cc(address, 0, 0x1L);
    FUN_80012bb4();
  }

  @Method(0x800fc654L)
  public static void FUN_800fc654(final long fileIndex) {
    loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(SItem.class, "FUN_800fc210", long.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x800fc698L)
  public static long getXpToNextLevel(final int charIndex) {
    if(charIndex == -1 || charIndex > 8) {
      //LAB_800fc6a4
      return 0;
    }

    //LAB_800fc6ac
    long v1 = gameState_800babc8.charData_32c.get(charIndex).level_12.get();

    if(v1 >= 60L) {
      return 0; // Max level
    }

    final long a1 = v1 + 0x1L;

    v1 = switch(charIndex) {
      case 0    -> 0x801135e4L; //TODO
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
  public static long getMenuOptionY(final long option) {
    return 78 + option * 13;
  }

  @Method(0x800fc7a4L)
  public static long getItemSubmenuOptionY(final long option) {
    return 80 + option * 13;
  }

  @Method(0x800fc7bcL)
  public static long FUN_800fc7bc(final long a0) {
    return 130 + a0 * 56;
  }

  @Method(0x800fc7d0L)
  public static long FUN_800fc7d0(final long a0) {
    return 130 + a0 * 46;
  }

  @Method(0x800fc7ecL)
  public static long menuOptionY(final long a0) {
    return 107 + a0 * 13;
  }

  @Method(0x800fc804L)
  public static long FUN_800fc804(final long a0) {
    return 99 + a0 * 17;
  }

  @Method(0x800fc814L)
  public static long FUN_800fc814(final long a0) {
    return 9 + a0 * 17;
  }

  @Method(0x800fc824L)
  public static long FUN_800fc824(final long a0) {
    if(a0 == 0) {
      return 43;
    }

    return 221;
  }

  @Method(0x800fc838L)
  public static long getAdditionSlotY(final long a0) {
    return 113 + a0 * 14;
  }

  @Method(0x800fc84cL)
  public static long getSlotY(final long slot) {
    return 16 + slot * 72;
  }

  @Method(0x800fc860L)
  public static long FUN_800fc860(final long a0) {
    return 180 + a0 * 17;
  }

  @Method(0x800fc880L)
  public static long FUN_800fc880(long a0) {
    if((int)a0 >= 0x3L) {
      a0 -= 0x3L;
    }

    //LAB_800fc890
    return 198 + a0 * 57;
  }

  @Method(0x800fc8a8L)
  public static long FUN_800fc8a8(final long a0) {
    //LAB_800fc8b8
    return a0 >= 0x3L ? 0x7aL : 0x10L;
  }

  @Method(0x800fc8c0L)
  public static long FUN_800fc8c0(final long a0) {
    return 21 + a0 * 50;
  }

  @Method(0x800fc8dcL)
  public static long FUN_800fc8dc(final long a0) {
    return 18 + a0 * 17;
  }

  @Method(0x800fc8ecL)
  public static long FUN_800fc8ec(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fc900L)
  public static Renderable58 FUN_800fc900(final long option) {
    final Renderable58 renderable = allocateUiElement(116, 116, 122, getItemSubmenuOptionY(option) - 2);
    FUN_80104b60(renderable);
    return renderable;
  }

  @Method(0x800fc944L)
  public static void fileLoadedCallback6665And6666(final long address, final long size, final long param) {
    if(param == 0) {
      //LAB_800fc98c
      FUN_80022a94(MEMORY.ref(4, address).offset(0x83e0L)); // Character textures
      FUN_80022a94(MEMORY.ref(4, address)); // Menu textures
      FUN_80022a94(MEMORY.ref(4, address).offset(0x6200L)); // Item textures
      FUN_80022a94(MEMORY.ref(4, address).offset(0x1_0460L));
      FUN_80022a94(MEMORY.ref(4, address).offset(0x1_0580L));
      FUN_800127cc(MEMORY.ref(4, address).getAddress(), 0, 0x1L);
    } else if(param == 0x1L) {
      //LAB_800fc9e4
      drgn0_6666FilePtr_800bdc3c.setPointer(address);
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
    if(!renderablePtr_800bdba4.isNull()) {
      fadeOutArrow(renderablePtr_800bdba4.deref());
      renderablePtr_800bdba4.clear();
    }

    //LAB_800fca40
    if(!renderablePtr_800bdba8.isNull()) {
      fadeOutArrow(renderablePtr_800bdba8.deref());
      renderablePtr_800bdba8.clear();
    }

    //LAB_800fca60
    if(!saveListUpArrow_800bdb94.isNull()) {
      fadeOutArrow(saveListUpArrow_800bdb94.deref());
      saveListUpArrow_800bdb94.clear();
    }

    //LAB_800fca80
    if(!saveListDownArrow_800bdb98.isNull()) {
      fadeOutArrow(saveListDownArrow_800bdb98.deref());
      saveListDownArrow_800bdb98.clear();
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

    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    LOGGER.info("Inventory menu state: %x", inventoryMenuState_800bdc28.get());

    switch((int)inventoryMenuState_800bdc28.get()) {
      case 0: // Initialize, loads some files (unknown contents)
        _800bdc34.setu(0);
        drgn0_6666FilePtr_800bdc3c.clear();
        renderablePtr_800bdc5c.clear();
        messageBox_8011dc90._0c.set(0);
        setWidthAndFlags(0x180L, 0);
        s0 = getMethodAddress(SItem.class, "fileLoadedCallback6665And6666", long.class, long.class, long.class);
        loadDrgnBinFile(0, 6665L, 0, s0, 0, 0x5L);
        loadDrgnBinFile(0, 6666L, 0, s0, 0x1L, 0x3L);
        FUN_80110030(0);
        _800bdf00.setu(0x21L);

        if(mainCallbackIndex_8004dd20.get() == 0x8L) {
          gameState_800babc8._4e4.set(1);
          canSave_8011dc88.setu(0x1L);
        } else {
          //LAB_800fcbfc
          gameState_800babc8._4e4.set(0);
          canSave_8011dc88.setu(_8005a368);
        }

        //LAB_800fcc10
        selectedMenuOption_8011d738.setu(0);
        selectedItemSubmenuOption_8011d73c.setu(0);
        inventoryMenuState_800bdc28.setu(0x1L);
        FUN_8002c150(0);
        break;

      case 0x1:
        if(drgn0_6666FilePtr_800bdc3c.isNull()) {
          break;
        }

        inventoryMenuState_800bdc28.setu(0x2L);
        _8011dcfc.set(0 < (gameState_800babc8.dragoonSpirits_19c.get(1).get() & 0x4L));
        gameState_800babc8.vibrationEnabled_4e1.and(1);
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
        renderBackground(_80114130.getAddress(), 0, 0);
        selectedMenuOptionRenderablePtr_800bdbe0.set(allocateUiElement(115, 115, 29, getMenuOptionY(selectedMenuOption_8011d738.get())));
        FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe0.deref());
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x4L, 0xffL);
        inventoryMenuState_800bdc28.setu(0x4L);
        break;

      case 0x4: // Main inventory menu
        if(messageBox(messageBox_8011dc90) != 0) {
          if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) { // Circle
            playSound(0x3L);
            FUN_800fca0c(0x7dL, 0x1L);
          }

          //LAB_800fcdd4
          if(handleMenuUpDown(selectedMenuOption_8011d738.getAddress(), 7) != 0) {
            selectedItemSubmenuOption_8011d73c.setu(0);
            selectedMenuOptionRenderablePtr_800bdbe0.deref().y_44.set(getMenuOptionY(selectedMenuOption_8011d738.get()));
          }

          //LAB_800fce08
          v1 = inventoryJoypadInput_800bdc44.get();

          if((v1 & 0x2020L) != 0) { // Right or cross
            if((v1 & 0x2000L) != 0) { // Right
              v1 = selectedMenuOption_8011d738.get() + 0xaL;
            } else {
              //LAB_800fce30
              v1 = selectedMenuOption_8011d738.get();
            }

            //LAB_800fce34
            switch((int)v1) {
              case 0 -> {
                playSound(0x2L);
                charSlot_8011d734.set(0);

                //LAB_800fcf3c
                FUN_800fca0c(0x14L, 0x1L);
              }

              case 0x1, 0xb -> {
                playSound(0x4L);
                selectedMenuOptionRenderablePtr_800bdbe4.set(FUN_800fc900(selectedItemSubmenuOption_8011d73c.get()));
                inventoryMenuState_800bdc28.setu(0x5L);
              }

              case 0x2 -> {
                playSound(0x2L);
                charSlot_8011d734.set(0);

                //LAB_800fcf3c
                FUN_800fca0c(0xcL, 0x1L);
              }

              case 0x3 -> {
                playSound(0x2L);
                charSlot_8011d734.set(0);

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
                selectedItemSubmenuOption_8011d73c.setu(0);
                selectedMenuOptionRenderablePtr_800bdbe4.clear();
                setMessageBoxText(null, 0x1);
                inventoryMenuState_800bdc28.setu(0x6L);
              }

              case 0x6 -> {
                if(canSave_8011dc88.get() != 0) {
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
        if(selectedMenuOption_8011d738.get() == 0x1L) {
          renderItemSubmenu(0xffL, 0x6L);
        }

        //LAB_800fcf70
        FUN_80102484(0);

        //LAB_800fd344
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x4L, 0);
        break;

      case 0x5: // "Item" inventory submenu
        if((inventoryJoypadInput_800bdc44.get() & 0x8040L) != 0) { // Left or circle
          playSound(0x3L);
          inventoryMenuState_800bdc28.setu(0x4L);
          unloadRenderable(selectedMenuOptionRenderablePtr_800bdbe4.deref());
        }

        //LAB_800fcfc0
        if(handleMenuUpDown(selectedItemSubmenuOption_8011d73c.getAddress(), 4) != 0) {
          selectedMenuOptionRenderablePtr_800bdbe4.deref().y_44.set(getItemSubmenuOptionY(selectedItemSubmenuOption_8011d73c.get()) - 0x2L);
        }

        //LAB_800fcff0
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) { // Cross
          playSound(0x2L);
          a0 = selectedItemSubmenuOption_8011d73c.get();
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
        renderItemSubmenu(selectedItemSubmenuOption_8011d73c.get(), 0x4L);

        //LAB_800fd344
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x6L, 0);
        break;

      case 0x6:
        messageBox(messageBox_8011dc90);

        if(messageBox_8011dc90.ticks_10.get() >= 0x2L) {
          if(selectedMenuOptionRenderablePtr_800bdbe4.isNull()) {
            selectedMenuOptionRenderablePtr_800bdbe4.set(allocateUiElement(0x74L, 0x74L, FUN_800fc7bc(0) - 0x22L, menuOptionY(0) - 0x2L));
            FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe4.deref());
            selectedMenuOptionRenderablePtr_800bdbe4.deref()._3c.set(0x20);
          }

          //LAB_800fd100
          if(handleMenuUpDown(selectedItemSubmenuOption_8011d73c.getAddress(), 0x4L) != 0) {
            selectedMenuOptionRenderablePtr_800bdbe4.deref().y_44.set(menuOptionY(selectedItemSubmenuOption_8011d73c.get()) - 0x2L);
          }

          //LAB_800fd130
          if((joypadPress_8007a398.get() & 0x8000L) != 0) {
            playSound(0x2L);
            a0 = selectedItemSubmenuOption_8011d73c.get();

            //LAB_800fd174
            if(a0 == 0) {
              //LAB_800fd18c
              gameState_800babc8.vibrationEnabled_4e1.set(0);
              FUN_8002379c();
            } else if(a0 == 0x1L) {
              //LAB_800fd1a0
              gameState_800babc8.mono_4e0.set(0);
              setMonoOrStereo(0);
            } else if(a0 == 0x2L) {
              //LAB_800fd1b8
              gameState_800babc8.morphMode_4e2.set(0);
            } else if(a0 == 0x3L) {
              //LAB_800fd1c4
              if(gameState_800babc8.indicatorMode_4e8.get() != 0) {
                gameState_800babc8.indicatorMode_4e8.decr();
              }
            }
          }

          //LAB_800fd1e0
          //LAB_800fd1e4
          if((joypadPress_8007a398.get() & 0x2000L) != 0) {
            playSound(0x2L);
            v1 = selectedItemSubmenuOption_8011d73c.get();

            //LAB_800fd22c
            if(v1 == 0) {
              //LAB_800fd244
              gameState_800babc8.vibrationEnabled_4e1.set(1);
              FUN_8002bcc8(0, 0x100L);
              FUN_8002bda4(0, 0, 0x3cL);
              FUN_8002379c();
            } else if(v1 == 0x1L) {
              //LAB_800fd278
              gameState_800babc8.mono_4e0.set(1);
              setMonoOrStereo(0x1L);
            } else if(v1 == 0x2L) {
              //LAB_800fd290
              gameState_800babc8.morphMode_4e2.set(1);
            } else if(v1 == 0x3L) {
              //LAB_800fd29c
              if(gameState_800babc8.indicatorMode_4e8.get() < 0x2L) {
                gameState_800babc8.indicatorMode_4e8.incr();
              }
            }
          }

          //LAB_800fd2bc
          //LAB_800fd2c0
          if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
            playSound(0x2L);
            inventoryMenuState_800bdc28.setu(0x4L);
            messageBox_8011dc90._0c.incr();
            unloadRenderable(selectedMenuOptionRenderablePtr_800bdbe4.deref());
          }

          //LAB_800fd30c
          renderOptionsMenu(selectedItemSubmenuOption_8011d73c.get(), gameState_800babc8.vibrationEnabled_4e1.get(), gameState_800babc8.mono_4e0.get(), gameState_800babc8.morphMode_4e2.get(), gameState_800babc8.indicatorMode_4e8.get());
        }

        //LAB_800fd330
        FUN_80102484(0);

        //LAB_800fd344
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x4L, 0);
        break;

      case 0x7:
        FUN_8002437c(0xffL);
        renderBackground(_80114130.getAddress(), 0, 0);
        selectedMenuOptionRenderablePtr_800bdbe0.set(allocateUiElement(0x73L, 0x73L, 0x1dL, getMenuOptionY(selectedMenuOption_8011d738.get())));
        selectedMenuOptionRenderablePtr_800bdbe4.set(FUN_800fc900(selectedItemSubmenuOption_8011d73c.get()));
        FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe0.deref());
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x4L, 0xffL);
        scriptStartEffect(0x2L, 0xaL);
        FUN_80102484(0x1L);
        inventoryMenuState_800bdc28.setu(0x5L);
        break;

      case 0x8:
        scriptStartEffect(0x2L, 0xaL);
        selectedSlot_8011d740.setu(0x1L);
        slotScroll_8011d744.setu(0);
        inventoryMenuState_800bdc28.setu(0x9L);
        break;

      case 0x9:
        FUN_8002437c(0xffL);
        renderBackground(_80114160.getAddress(), 0, 0);
        renderablePtr_800bdbe8.set(allocateUiElement(0x7fL, 0x7fL, 0x10L, getSlotY(selectedSlot_8011d740.get())));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
        FUN_801024c4(0xffL);
        inventoryMenuState_800bdc28.setu(0xaL);
        break;

      case 0xa:
        FUN_801024c4(0);

        if(_800bb168.get() != 0) {
          break;
        }

        if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0 && selectedSlot_8011d740.get() >= 0x2L) {
          selectedSlot_8011d740.subu(0x1L);
          renderablePtr_800bdbe8.deref().y_44.set(getSlotY(selectedSlot_8011d740.get()));
          playSound(0x1L);
        }

        //LAB_800fd4e4
        //LAB_800fd4e8
        if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0 && selectedSlot_8011d740.get() < 0x2L) {
          selectedSlot_8011d740.addu(0x1L);
          renderablePtr_800bdbe8.deref().y_44.set(getSlotY(selectedSlot_8011d740.get()));
          playSound(0x1L);
        }

        //LAB_800fd52c
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          final int charIndex = gameState_800babc8.charIndex_88.get((int)selectedSlot_8011d740.get()).get();
          if(charIndex == -1 || (gameState_800babc8.charData_32c.get(charIndex)._04.get() & 0x20L) != 0) {
            //LAB_800fd590
            playSound(0x28L);
          } else {
            //LAB_800fd5a0
            playSound(0x2L);
            renderablePtr_800bdbec.set(allocateUiElement(0x80L, 0x80L, FUN_800fc880(slotScroll_8011d744.get()), FUN_800fc8a8(slotScroll_8011d744.get())));
            FUN_80104b60(renderablePtr_800bdbec.deref());
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

        if((inventoryJoypadInput_800bdc44.get() & 0x8000L) != 0 && slotScroll_8011d744.get() % 3 > 0) {
          slotScroll_8011d744.subu(0x1L);
          renderablePtr_800bdbec.deref().x_40.set(FUN_800fc880(slotScroll_8011d744.get()));
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8a8(slotScroll_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd6b8
        if((inventoryJoypadInput_800bdc44.get() & 0x2000L) != 0 && slotScroll_8011d744.get() % 3 < 0x2L) {
          slotScroll_8011d744.addu(0x1L);
          renderablePtr_800bdbec.deref().x_40.set(FUN_800fc880(slotScroll_8011d744.get()));
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8a8(slotScroll_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd730
        if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0 && slotScroll_8011d744.get() >= 0x3L) {
          slotScroll_8011d744.subu(0x3L);
          renderablePtr_800bdbec.deref().x_40.set(FUN_800fc880(slotScroll_8011d744.get()));
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8a8(slotScroll_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd78c
        //LAB_800fd790
        if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0 && slotScroll_8011d744.get() < 0x3L) {
          slotScroll_8011d744.addu(0x3L);
          renderablePtr_800bdbec.deref().x_40.set(FUN_800fc880(slotScroll_8011d744.get()));
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8a8(slotScroll_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd7e4
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          unloadRenderable(renderablePtr_800bdbec.deref());
          inventoryMenuState_800bdc28.setu(0xaL);
        }

        //LAB_800fd820
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          v1 = _800bdbf8.offset(slotScroll_8011d744.get() * 0x4L).get();
          if((int)v1 == -0x1L) {
            //LAB_800fd888
            playSound(0x28L);
            break;
          }

          if((gameState_800babc8.charData_32c.get((int)v1)._04.get() & 0x2L) == 0) {
            //LAB_800fd888
            playSound(0x28L);
            break;
          }

          //LAB_800fd898
          playSound(0x2L);
          a3 = gameState_800babc8.charIndex_88.get((int)selectedSlot_8011d740.get()).get();
          gameState_800babc8.charIndex_88.get((int)selectedSlot_8011d740.get()).set((int)_800bdbf8.offset(slotScroll_8011d744.get() * 0x4L).get());
          _800bdbf8.offset(slotScroll_8011d744.get() * 0x4L).setu(a3);
          inventoryMenuState_800bdc28.setu(0x9L);
        }

        break;

      case 0xc:
        FUN_80023148();
        scriptStartEffect(0x2L, 0xaL);
        FUN_8002437c(0xffL);

      case 0xd:
        slotScroll_8011d744.setu(0);
        selectedSlot_8011d740.setu(0);

      case 0xe:
        FUN_8002437c(0);
        renderBackground(_80114180.getAddress(), 0, 0);

        if(renderablePtr_800bdbe8.isNull()) {
          renderablePtr_800bdbe8.set(allocateUiElement(0x79L, 0x79L, FUN_800fc824(0x1L), 0));
          FUN_80104b60(renderablePtr_800bdbe8.deref());
        }

        //LAB_800fd964
        renderablePtr_800bdbe8.deref().y_44.set(FUN_800fc804(selectedSlot_8011d740.get()));
        _8011d750.setu(FUN_801045fc(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()));
        FUN_80102660(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xffL);
        inventoryMenuState_800bdc28.setu(0xfL);
        break;

      case 0xf:
        FUN_801034cc(charSlot_8011d734.get(), _8011d7c4.get());
        FUN_80102660(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);

        if(_800bb168.get() != 0) {
          break;
        }

        if(FUN_80103f00(selectedSlot_8011d740.getAddress(), slotScroll_8011d744.getAddress(), 0x4L, _8011d750.get(), 0x1L) != 0) {
          renderablePtr_800bdbe8.deref().y_44.set(FUN_800fc804(selectedSlot_8011d740.get()));
        }

        //LAB_800fda58
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          FUN_800fca0c(0x2L, 0x6L);
        }

        //LAB_800fda80
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) { // Equip item
          a0 = selectedSlot_8011d740.get() + slotScroll_8011d744.get();
          if(a0 < gameState_800babc8._1e4.get()) {
            final int equipmentId = (int)_8011d7c8.offset(a0 * 0x4L).get();
            if(equipmentId != 0xffL) {
              final int previousEquipmentId = equipItem(equipmentId, characterIndices_800bdbb8.get(charSlot_8011d734.get()).get());
              FUN_800233d8(_8011d7c8.offset((selectedSlot_8011d740.get() + slotScroll_8011d744.get()) * 0x4L).offset(0x1L).get());
              FUN_80023484(previousEquipmentId);
              playSound(0x2L);
              FUN_80110030(0);
              FUN_80022b50(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get(), 0);
              addMp(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get(), 0);
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
          FUN_80023a2c(_8011dcb8.get(), gameState_800babc8._1e8, gameState_800babc8._1e4.get());
          removeFromLinkedList(_8011dcb8.get());
          removeFromLinkedList(_8011dcbc.get());
          MEMORY.ref(4, 0x800bdc28L).setu(0xeL);
        }

        //LAB_800fdc18
        if(FUN_8010415c(charSlot_8011d734.getAddress(), _8011d7c4.get()) != 0) {
          inventoryMenuState_800bdc28.setu(0xdL);
        }

        break;

      case 0x10:
      case 0x1f:
        scriptStartEffect(0x2L, 0xaL);
        FUN_8002437c(0xffL);
        renderBackground(_801141c4.getAddress(), 0, 0);
        _8011dcb8.setu(addToLinkedListTail(0x4c0L));
        _8011dcbc.setu(addToLinkedListTail(0x4c0L));
        FUN_80023148();
        charSlot_8011d734.set(0);
        selectedSlot_8011d740.setu(0);
        slotScroll_8011d744.setu(0);
        _8011d748.setu(0);
        _8011d74c.setu(0);
        renderablePtr_800bdbe8.set(allocateUiElement(0x76L, 0x76L, FUN_800fc824(0), FUN_800fc814(selectedSlot_8011d740.get()) + 0x20L));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
        FUN_80102840(slotScroll_8011d744.get(), _8011d748.get(), 0xffL, 0xffL);

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
        FUN_80102840(slotScroll_8011d744.get(), _8011d748.get(), _8011dcb8.offset(charSlot_8011d734.get() * 0x4L).deref(1).offset((selectedSlot_8011d740.get() + slotScroll_8011d744.get()) * 0x4L).get(), 0);
        break;

      case 0x20:
        _8011d754.setu(FUN_80104738(0x1L));
        inventoryMenuState_800bdc28.setu(0x21L);

        //LAB_800fe08c
        FUN_80102840(slotScroll_8011d744.get(), _8011d748.get(), _8011dcb8.offset(charSlot_8011d734.get() * 0x4L).deref(1).offset((selectedSlot_8011d740.get() + _8011d748.get()) * 0x4L).get(), 0);
        break;

      case 0x12:
      case 0x21: // Discard items menu
        if(charSlot_8011d734.get() != 0) {
          a0 = gameState_800babc8._1e6.get();
        } else {
          //LAB_800fddf4
          a0 = gameState_800babc8._1e4.get() + _8011d754.get();
        }

        //LAB_800fde10
        _8011d750.setu(a0);

        if(charSlot_8011d734.get() != 0) {
          s1 = selectedSlot_8011d740.get() + _8011d748.get();
        } else {
          //LAB_800fde38
          s1 = selectedSlot_8011d740.get() + slotScroll_8011d744.get();
        }

        //LAB_800fde50
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          _800bdba0.clear();
          _800bdb9c.clear();
          saveListDownArrow_800bdb98.clear();
          saveListUpArrow_800bdb94.clear();

          //LAB_800fdea8
          if(whichMenu_800bdc38.get() != 0x24L) {
            FUN_800fca0c(0x7L, 0x8L);
          } else {
            FUN_800fca0c(0x13L, 0x8L);
          }
        }

        //LAB_800fdeb4
        if((inventoryJoypadInput_800bdc44.get() & 0x10L) != 0) { // Discard items menu - sort items
          playSound(0x2L);

          final ArrayRef<UnsignedByteRef> a1_0;
          if(charSlot_8011d734.get() != 0) {
            a1_0 = gameState_800babc8._2e9;
          } else {
            //LAB_800fdef8
            a1_0 = gameState_800babc8._1e8;
          }

          //LAB_800fdf00
          FUN_80023a2c(_8011dcb8.offset(charSlot_8011d734.get() * 0x4L).get(), a1_0, _8011d750.get());
        }

        //LAB_800fdf18
        if(charSlot_8011d734.get() != 0) {
          a1 = _8011d748.getAddress();
        } else {
          //LAB_800fdf38
          a1 = slotScroll_8011d744.getAddress();
        }

        //LAB_800fdf40
        if(FUN_80103f00(selectedSlot_8011d740.getAddress(), a1, 0x7L, _8011d750.get(), 0x1L) != 0) {
          renderablePtr_800bdbe8.deref().y_44.set(FUN_800fc814(selectedSlot_8011d740.get()) + 0x20L);
        }

        //LAB_800fdf7c
        if(FUN_8010415c(charSlot_8011d734.getAddress(), 0x2L) != 0) {
          renderablePtr_800bdbe8.deref().x_40.set(FUN_800fc824(charSlot_8011d734.get()));
        }

        //LAB_800fdfb4
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0 && inventoryMenuState_800bdc28.get() == 0x21L) {
          if((_8011dcb8.offset(charSlot_8011d734.get() * 0x4L).deref(2).offset(s1 * 0x4L).offset(0x2L).get() & 0x2000L) != 0) {
            //LAB_800fe064
            playSound(0x28L);
          } else if(_8011dcb8.offset(charSlot_8011d734.get() * 0x4L).deref(1).offset(s1 * 0x4L).get() == 0xffL) {
            //LAB_800fe064
            playSound(0x28L);
          } else {
            playSound(0x2L);
            _8011d74c.setu(0);
            renderablePtr_800bdc20.set(allocateUiElement(0x7dL, 0x7dL, 0x13aL, FUN_800fc860(0)));
            FUN_80104b60(renderablePtr_800bdc20.deref());
            inventoryMenuState_800bdc28.setu(0x22L);
          }
        }

        //LAB_800fe06c
        //LAB_800fe070
        //LAB_800fe08c
        FUN_80102840(slotScroll_8011d744.get(), _8011d748.get(), _8011dcb8.offset(charSlot_8011d734.get() * 0x4L).deref(1).offset(s1 * 0x4L).get(), 0);
        break;

      case 0x22: // Discard item confirm
        if(charSlot_8011d734.get() != 0) {
          s1 = selectedSlot_8011d740.get() + _8011d748.get();
        } else {
          //LAB_800fe0dc
          s1 =  selectedSlot_8011d740.get() + slotScroll_8011d744.get();
        }

        //LAB_800fe0f0
        renderText(Really_want_to_throw_this_away_8011c8d4, 192, 180, 0x4L);

        if(_8011d74c.get() != 0) {
          a3 = 0x6L;
        } else {
          a3 = 0x5L;
        }

        //LAB_800fe13c
        renderCentredText(Yes_8011c20c, 328, FUN_800fc860(0) + 2, a3);

        if(_8011d74c.get() != 0) {
          a3 = 0x5L;
        } else {
          a3 = 0x6L;
        }

        //LAB_800fe170
        renderCentredText(No_8011c214, 328, FUN_800fc860(1) + 2, a3);

        v1 = FUN_801041d8(_8011d74c.getAddress());
        if(v1 == 0x1L) {
          //LAB_800fe1bc
          renderablePtr_800bdc20.deref().y_44.set(FUN_800fc860(_8011d74c.get()));
        } else if(v1 == 0x2L) {
          //LAB_800fe1d8
          //LAB_800fe1fc
          for(s0 = s1; s0 < _8011d750.get(); s0++) {
            _8011dcb8.offset(charSlot_8011d734.get() * 0x4L).deref(4).offset(s0 * 0x4L).setu(_8011dcb8.offset(charSlot_8011d734.get() * 0x4L).deref(4).offset(s0 * 0x4L).offset(0x4L).get());
          }

          //LAB_800fe238
          _8011d750.subu(0x1L);

          final ArrayRef<UnsignedByteRef> a1_0;
          if(charSlot_8011d734.get() != 0) {
            a1_0 = gameState_800babc8._2e9;
          } else {
            //LAB_800fe274
            a1_0 = gameState_800babc8._1e8;
          }

          //LAB_800fe27c
          FUN_800239e0(_8011dcb8.offset(charSlot_8011d734.get() * 0x4L).get(), a1_0, _8011d750.get());
          FUN_80023148();

          //LAB_800fe29c
          unloadRenderable(renderablePtr_800bdc20.deref());
          inventoryMenuState_800bdc28.setu(0x21L);
        } else if(v1 == 0x4L) {
          //LAB_800fe29c
          unloadRenderable(renderablePtr_800bdc20.deref());
          inventoryMenuState_800bdc28.setu(0x21L);
        }

        //LAB_800fe2b4
        //LAB_800fe2bc
        FUN_80102840(slotScroll_8011d744.get(), _8011d748.get(), 0xffL, 0x1L);
        break;

      case 0x13:
        v1 = 0x800c_0000L;
        v0 = 0x1L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        v1 = 0x800c_0000L;
        v0 = 0x9L;
        MEMORY.ref(4, v1).offset(-0x23c8L).setu(v0);
        break;

      case 0x14: // Status menu
        scriptStartEffect(0x2L, 0xaL);
        FUN_8002437c(0xffL);

        //LAB_800fe3c4
        inventoryMenuState_800bdc28.setu(0x15L);
        break;

      case 0x15: // Status menu
        FUN_8002437c(0);
        renderBackground(_801141a4.getAddress(), 0, 0);
        renderStatusMenu(charSlot_8011d734.get(), 0xffL);
        inventoryMenuState_800bdc28.setu(0x16L);
        break;

      case 0x16: // Status menu
        FUN_801034cc(charSlot_8011d734.get(), _8011d7c4.get());
        renderStatusMenu(charSlot_8011d734.get(), 0);

        if(_800bb168.get() == 0) {
          if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
            playSound(0x3L);
            FUN_800fca0c(0x2L, 0x7L);
          }

          //LAB_800fe3b0
          if(FUN_8010415c(charSlot_8011d734.getAddress(), _8011d7c4.get()) != 0) {
            //LAB_800fe3c4
            inventoryMenuState_800bdc28.setu(0x15L);
          }
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

      case 0x18: // Load additions menu
        FUN_8002437c(0);
        loadAdditions(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get(), additions_8011e098);

        if(additions_8011e098.get(0).offset_00.get() != -1) {
          renderablePtr_800bdbe8.set(allocateUiElement(117, 117, 39, getAdditionSlotY(selectedSlot_8011d740.get()) - 4));
          FUN_80104b60(renderablePtr_800bdbe8.deref());
        }

        //LAB_800fe490
        allocateUiElement(69, 69, 0, 0);
        allocateUiElement(70, 70, 192, 0);
        renderAdditions(charSlot_8011d734.get(), selectedSlot_8011d740.get(), additions_8011e098, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()).selectedAddition_19.get(), 0xffL);
        inventoryMenuState_800bdc28.setu(0x19L);
        break;

      case 0x19: // Additions
        FUN_801034cc(charSlot_8011d734.get(), _8011d7c4.get());
        renderAdditions(charSlot_8011d734.get(), selectedSlot_8011d740.get(), additions_8011e098, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()).selectedAddition_19.get(), 0);

        if(_800bb168.get() != 0) {
          break;
        }

        if(FUN_8010415c(charSlot_8011d734.getAddress(), _8011d7c4.get()) != 0) {
          inventoryMenuState_800bdc28.setu(0x18L);
          unloadRenderable(renderablePtr_800bdbe8.deref());
        }

        //LAB_800fe5b8
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          FUN_800fca0c(0x2L, 0x9L);
        }

        //LAB_800fe5e4
        if(additions_8011e098.get(0).offset_00.get() == -1) {
          break;
        }

        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          final int a1_0 = additions_8011e098.get((int)selectedSlot_8011d740.get()).offset_00.get();

          if(a1_0 != -1) {
            gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()).selectedAddition_19.set(a1_0);
            playSound(0x2L);
            unloadRenderable(renderablePtr_800bdbe8.deref());
            inventoryMenuState_800bdc28.setu(0x18L);
          } else {
            //LAB_800fe680
            playSound(0x28L);
          }
        }

        //LAB_800fe68c
        if(handleMenuUpDown(selectedSlot_8011d740.getAddress(), 7) != 0) {
          renderablePtr_800bdbe8.deref().y_44.set(getAdditionSlotY(selectedSlot_8011d740.get()) - 4);
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

      case 0x1b: // Item sub-menu
        FUN_8002437c(0xffL);
        renderBackground(_801141fc.getAddress(), 0, 0);
        renderablePtr_800bdbec.set(allocateUiElement(0x77L, 0x77L, 0x2aL, FUN_800fc8dc(selectedSlot_8011d740.get())));
        FUN_80104b60(renderablePtr_800bdbec.deref());
        _8011d750.setu(FUN_80104448());
        FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xffL);
        inventoryMenuState_800bdc28.setu(0x1cL);
        break;

      case 0x1c: // Transition from item sub-menu to item list
        FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);

        if(messageBox(messageBox_8011dc90) != 0) {
          inventoryMenuState_800bdc28.setu(0x1dL);
        }

        break;

      case 0x1d: // Item list
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          _800bdb9c.clear();
          _800bdba0.clear();
          saveListDownArrow_800bdb98.clear();
          saveListUpArrow_800bdb94.clear();
          FUN_800fca0c(0x7L, 0xaL);
        }

        //LAB_800fe824
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          a0 = FUN_80022afc(_8011d7c8.offset((selectedSlot_8011d740.get() + slotScroll_8011d744.get()) * 0x4L).get()) & 0xffL;
          fileCount_8011d7b8.setu(a0);

          if(a0 == 0) {
            //LAB_800fe93c
            playSound(0x28L);
          } else if((_8011d7c8.offset((selectedSlot_8011d740.get() + slotScroll_8011d744.get()) * 0x4L).offset(2, 0x2L).get() & 0x4000L) == 0) {
            if((a0 & 0x2L) != 0) {
              //LAB_800fe8b0
              for(int i = 0; i < 7; i++) {
                _8011d718.get(i).set(allocateUiElement(0x7eL, 0x7eL, FUN_800fc8c0(i), 0x6eL));
                FUN_80104b60(_8011d718.get(i).deref());
              }
            } else {
              //LAB_800fe8f0
              renderablePtr_800bdbe8.set(allocateUiElement(0x7eL, 0x7eL, FUN_800fc8c0(charSlot_8011d734.get()), 0x6eL));
              FUN_80104b60(renderablePtr_800bdbe8.deref());
            }

            //LAB_800fe924
            playSound(0x2L);
            inventoryMenuState_800bdc28.setu(0x1eL);
          } else {
            //LAB_800fe93c
            playSound(0x28L);
          }
        }

        //LAB_800fe944
        if(FUN_80103f00(selectedSlot_8011d740.getAddress(), slotScroll_8011d744.getAddress(), 0x5L, _8011d750.get(), 0x1L) != 0) {
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8dc(selectedSlot_8011d740.get()));
        }

        //LAB_800fe994
        //LAB_800fec18
        FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);
        break;

      case 0x1e: // Confirm use item
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) { // Circle
          if((fileCount_8011d7b8.get() & 0x2L) == 0) {
            //LAB_800fea00
            unloadRenderable(renderablePtr_800bdbe8.deref());
          } else {
            //LAB_800fe9dc
            for(int i = 0; i < 7; i++) {
              unloadRenderable(_8011d718.get(i).deref());
            }
          }

          //LAB_800fea10
          playSound(0x3L);
          inventoryMenuState_800bdc28.setu(0x1d);
        }

        //LAB_800fea24
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) { // Cross
          if((fileCount_8011d7b8.get() & 0x2L) != 0) {
            _8011d754.setu(-0x2L);

            if(_8011d7c4.get() != 0) {
              //LAB_800fea84
              for(int i = 0; i < _8011d7c4.get(); i++) {
                FUN_80022d88(_8011d788.getAddress(), _8011d7c8.offset((selectedSlot_8011d740.get() + slotScroll_8011d744.get()) * 0x4L).get(), characterIndices_800bdbb8.get(i).get());

                if((int)_8011d78c.get() != -0x2L) {
                  _8011d754.setu(0);
                }

                //LAB_800feac8
              }
            }

            //LAB_800feadc
            _8011d78c.setu(_8011d754.get());
          } else {
            //LAB_800feaf0
            FUN_80022d88(_8011d788.getAddress(), _8011d7c8.offset((selectedSlot_8011d740.get() + slotScroll_8011d744.get()) * 0x4L).get(), characterIndices_800bdbb8.get(charSlot_8011d734.get()).get());
          }

          //LAB_800feb40
          playSound(0x2L);
          FUN_800232dc(_8011d7c8.offset((selectedSlot_8011d740.get() + slotScroll_8011d744.get()) * 0x4L).get());
          _8011d750.setu(FUN_80104448());
          FUN_80110030(0);
          FUN_80104324(_8011d788.getAddress());
          setMessageBoxText(_8011d790, 0);
          inventoryMenuState_800bdc28.setu(0x1bL);
        }

        //LAB_800febb0
        if((fileCount_8011d7b8.get() & 0x2L) == 0 && FUN_8010415c(charSlot_8011d734.getAddress(), _8011d7c4.get()) != 0) {
          renderablePtr_800bdbe8.deref().x_40.set(FUN_800fc8c0(charSlot_8011d734.get()) - 3);
        }

        //LAB_800fec04
        //LAB_800fec18
        FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);
        break;

      case 0x23: // Goods menu
        scriptStartEffect(0x2L, 0xaL);
        FUN_8002437c(0xffL);
        renderBackground(_801141c4.getAddress(), 0, 0);
        _8011d750.setu(0);

        //LAB_800fec7c
        for(s0 = 0; s0 < 70; s0++) {
          _8011d7c8.offset(s0 * 0x4L).setu(0xffL);

          if(s0 < 0x40L) {
            if((gameState_800babc8.dragoonSpirits_19c.get((int)(s0 >>> 5)).get() & (0x1L << (s0 & 0x1fL))) != 0) {
              _8011d7c8.offset(_8011d750.get() * 0x4L).offset(1, 0x0L).setu(s0);
              _8011d7c8.offset(_8011d750.get() * 0x4L).offset(1, 0x1L).setu(s0);
              _8011d7c8.offset(_8011d750.get() * 0x4L).offset(2, 0x2L).setu(0);
              _8011d750.addu(0x1L);
            }
          }

          //LAB_800fecf0
        }

        slotScroll_8011d744.setu(0);
        selectedSlot_8011d740.setu(0);
        charSlot_8011d734.set(0);
        renderablePtr_800bdbe8.set(allocateUiElement(0x76L, 0x76L, FUN_800fc824(0), FUN_800fc814(selectedSlot_8011d740.get()) + 0x20L));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
        FUN_80102f74(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xffL);
        inventoryMenuState_800bdc28.setu(0x24L);
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
          MEMORY.ref(1, v1).offset(0x1L).setu(0x30L); // This is a bug - it's set to s0 but s0 is undefined. The value of s0 at this point is an address with the lower byte of 0x30. Unknown if this value needs to be set or not, and if it's just chance that 0x30 is a valid value.
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
        a1 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        v0 = 0x8012_0000L;
        a2 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        a3 = 0;
        FUN_80102f74(charSlot_8011d734.get(), a1, a2, a3);
        break;

      case 0x25: // Part of load game menu
        _8004dd30.setu(0x1L);
        FUN_80110030(0);
        FUN_80103bd4(0);
        scriptStartEffect(0x2L, 0xaL);

      case 0x26: // Part of load game menu
        if(whichMenu_800bdc38.get() == 0x13L) {
          setMessageBoxText(Do_you_want_to_save_now_8011c370, 0x2);
          v0 = 0x27L;
        } else {
          //LAB_800fef50
          v0 = 0x28L;
        }

        //LAB_800fef54
        inventoryMenuState_800bdc28.setu(v0);
        FUN_8002437c(0xffL);
        renderSavedGames(slotScroll_8011d744.get(), null, 0xffL);
        break;

      case 0x27: // Part of load game menu
        v1 = messageBox(messageBox_8011dc90);
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
        renderSavedGames(slotScroll_8011d744.get(), null, 0);
        break;

      case 0x28: // Part of load game menu
        saveListDownArrow_800bdb98.clear();
        saveListUpArrow_800bdb94.clear();
        setMessageBoxText(Checking_MEMORY_CARD_Do_not_remove_MEMORY_CARD_or_turn_off_the_power_8011c93c, 0x1);
        memcardSaveLoadingStage_8011e0d4.setu(0x1L);
        slotScroll_8011d744.setu(0);
        selectedSlot_8011d740.setu(0);
        renderSavedGames(0, null, 0);
        inventoryMenuState_800bdc28.setu(0x29L);
        break;

      case 0x29: // Part of load game menu
        renderSavedGames(slotScroll_8011d744.get(), null, 0);

        if(_800bb168.get() != 0) {
          break;
        }

        s1 = _8011d768.getAddress();
        s2 = memcardData_8011dd10.getAddress();

        while(memcardSaveLoadingStage_8011e0d4.get() != 0) {
          executeMemcardLoadingStage(s1, s2);

          while(memcardEventIndex_80052e4c.getSigned() >= 0) {
            memcardVsyncInterruptHandler();
          }
        }

        if(memcardSaveLoadingStage_8011e0d4.get() != 0) {
          break;
        }

        a0 = MEMORY.ref(4, s1).offset(0x8L).get();

        if(a0 == 0x1L) {
          //LAB_801008d0
          inventoryMenuState_800bdc28.setu(0x37L);
          messageBox_8011dc90._0c.incr();
          break;
        }

        //LAB_800ff0c8
        if(a0 == 0x2L) {
          //LAB_801008d0
          inventoryMenuState_800bdc28.setu(0x39L);
          messageBox_8011dc90._0c.incr();
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
          messageBox_8011dc90._0c.incr();
          break;
        }

        //LAB_800ff12c
        //LAB_800ff130
        if(_8011d768.offset(1, 0x4L).get() != 0 || whichMenu_800bdc38.get() != 0xeL) {
          //LAB_800ff16c
          if(_8011d768.offset(1, 0x4L).get() != 0 || _8011d768.offset(1, 0x6L).get() != 0) {
            //LAB_800ff194
            if(_8011d768.offset(1, 0x10L).get() > 0xcL) {
              selectedSlot_8011d740.setu(_8011d768.offset(1, 0x10L).get() - 0xcL);
              slotScroll_8011d744.setu(0xcL);
            } else {
              //LAB_800ff1d0
              selectedSlot_8011d740.setu(0);
              slotScroll_8011d744.setu(_8011d768.offset(1, 0x10L));
            }

            //LAB_800ff1e0
            renderablePtr_800bdbe8.set(allocateUiElement(129, 129,  16, getSlotY(selectedSlot_8011d740.get())));
            renderablePtr_800bdbec.set(allocateUiElement(130, 130, 192, getSlotY(selectedSlot_8011d740.get())));
            FUN_80104b60(renderablePtr_800bdbe8.deref());
            FUN_80104b60(renderablePtr_800bdbec.deref());
            renderSaveListArrows(slotScroll_8011d744.get(), 14);

            inventoryMenuState_800bdc28.setu(0x2aL);
            messageBox_8011dc90._0c.incr();
            break;
          }

          if(_8011d768.offset(1, 0x4L).get() == 0) {
            inventoryMenuState_800bdc28.setu(0x3aL);
            messageBox_8011dc90._0c.incr();
            break;
          }
        }

        //LAB_801004ac
        inventoryMenuState_800bdc28.setu(0x3cL);
        messageBox_8011dc90._0c.incr();
        break;

      case 0x2a:
        renderSaveListArrows(slotScroll_8011d744.get(), 0xeL);

        //LAB_800ff2a8
        FUN_8002437c(0);
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0xffL);

        //LAB_800ff4f8
        inventoryMenuState_800bdc28.setu(0x2bL);
        break;

      case 0x2b: // Load game screen - ready to use
        if(FUN_80103f00(selectedSlot_8011d740.getAddress(), slotScroll_8011d744.getAddress(), 0x3L, 0xfL, 0x1L) != 0) {
          renderablePtr_800bdbe8.deref().y_44.set(getSlotY(selectedSlot_8011d740.get()));
          renderablePtr_800bdbec.deref().y_44.set(getSlotY(selectedSlot_8011d740.get()));
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

          if(!saveListUpArrow_800bdb94.isNull()) {
            fadeOutArrow(saveListUpArrow_800bdb94.deref());
            saveListUpArrow_800bdb94.clear();
          }

          //LAB_800ff3a4
          if(!saveListDownArrow_800bdb98.isNull()) {
            fadeOutArrow(saveListDownArrow_800bdb98.deref());
            saveListDownArrow_800bdb98.clear();
          }
        } else {
          //LAB_800ff3c8
          renderSaveListArrows(slotScroll_8011d744.get(), 0xeL);
        }

        //LAB_800fff80
        //LAB_800fff84
        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);
        break;

      case 0x2c:
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);

        final Ref<Long> refA1 = new Ref<>();
        final Ref<Long> refA2 = new Ref<>();
        v0 = FUN_8002efb8(0x1L, refA1, refA2);
        _8011d7bc.setu(refA1.get());
        fileCount_8011d7b8.setu(refA2.get());

        if(v0 == 0) {
          break;
        }

        a2 = fileCount_8011d7b8.get();
        if(a2 == 0) {
          //LAB_800ff440
          a3 = memcardData_8011dd10.get((int)(selectedSlot_8011d740.get() + slotScroll_8011d744.get())).fileIndex_04.get();
          if(whichMenu_800bdc38.get() == 0xeL) { // Load game menu
            if((int)a3 < 0xfL) {
              setMessageBoxText(Load_this_data_8011ca08, 0x2);
              inventoryMenuState_800bdc28.setu(0x2dL);
              break;
            }
          } else {
            //LAB_800ff4a0
            if((int)a3 < 0xfL || a3 == 0xffL) {
              //LAB_800ff4b0
              //LAB_800ff4c8
              if(a3 == 0xeL) {
                setMessageBoxText(Save_new_game_8011c9c8, 0x2);
              } else {
                //LAB_800ff4c0
                setMessageBoxText(Overwrite_save_8011c9e8, 0x2);
              }

              messageBox_8011dc90._18.set(0x1L);
              inventoryMenuState_800bdc28.setu(0x31L);
              break;
            }
          }

          //LAB_800ff4ec
          playSound(0x28L);

          //LAB_800ff4f8
          inventoryMenuState_800bdc28.setu(0x2bL);
          break;
        }

        if(fileCount_8011d7b8.get() == 0x1L) {
          //LAB_800ff61c
          inventoryMenuState_800bdc28.setu(0x37L);
          break;
        }

        if(a2 == 0x3L) {
          //LAB_800ff62c
          inventoryMenuState_800bdc28.setu(0x3eL);
          break;
        }

        //LAB_800ff65c
        inventoryMenuState_800bdc28.setu(0x39L);
        break;

      case 0x2d: // Do you want to load this save?
        v1 = messageBox(messageBox_8011dc90);
        if(v1 == 0x1L) { // Yes
          //LAB_800ff530
          //LAB_800ffc5c
          inventoryMenuState_800bdc28.setu(0x2eL);
        } else if(v1 == 0x2L) { // No
          //LAB_800ff53c
          //LAB_800ffc5c
          inventoryMenuState_800bdc28.setu(0x2bL);
        }

        //LAB_800fff80
        //LAB_800fff84
        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);
        break;

      case 0x2e:
        messageBox(messageBox_8011dc90);
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);
        FUN_8002df60(0);
        inventoryMenuState_800bdc28.setu(0x2fL);
        break;

      case 0x2f:
        messageBox(messageBox_8011dc90);
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);

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
//          setMessageBoxText(Data_loading_Do_not_remove_MEMORY_CARD_or_turn_off_the_power_8011ca2c, 0x1);
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
//        messageBox(messageBox_8011dc90);
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);

//        if(messageBox_8011dc90.ticks_10.get() < 0x3L) {
//          break;
//        }

        v1 = loadSaveFile(memcardData_8011dd10.get((int)(slotScroll_8011d744.get() + selectedSlot_8011d740.get())).fileIndex_04.get());
        if(v1 == 0) {
          //LAB_800ff6ec
          _800bdc34.setu(0x1L);
          _80052c34.setu(gameState_800babc8.submapScene_a4.get());
          submapCut_80052c30.setu(gameState_800babc8.submapCut_a8.get());
          index_80052c38.set(gameState_800babc8.submapCut_a8.get());

          if(gameState_800babc8.submapCut_a8.get() == 0x108L) {
            _80052c34.setu(0x35L);
          }

          //LAB_800ff730
          FUN_8002379c();
          setMonoOrStereo(gameState_800babc8.mono_4e0.get());
          v0 = 0x46L;
        } else if(v1 != 0x5L) { // v1 can only be 0, 1, 3?
          v0 = 0x38L;
        } else {
          //LAB_800ff750
          v0 = 0x3dL;
        }

        //LAB_800ff754
        inventoryMenuState_800bdc28.setu(v0);

        //LAB_80100bf4
        messageBox_8011dc90._0c.incr();
        break;

      case 0x31:
        v0 = messageBox(messageBox_8011dc90);
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

        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(a0, memcardData_8011dd10, 0);
        break;

      case 0x32:
        messageBox(messageBox_8011dc90);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        a0 = 0;
        FUN_8002df60(a0);
        v1 = 0x800c_0000L;
        v0 = 0x33L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x33:
        messageBox(messageBox_8011dc90);
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
              setMessageBoxText(Data_saving_Do_not_remove_MEMORY_CARD_or_turn_off_the_power_8011cab0, 0x1);
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

                  v0 = 0x8012_0000L;
                  a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
                  renderSavedGames(a0, null, 0xffL);
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

        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(a0, memcardData_8011dd10, 0);
        break;

      case 0x34:
        messageBox(messageBox_8011dc90);
        s1 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s1).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox_8011dc90.ticks_10.get();

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
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);
        if(messageBox(messageBox_8011dc90) == 0) {
          //LAB_800fff8c
          //LAB_800fff94
          renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);
          break;
        }

        //LAB_800ff9cc
        a1 = _8011dcc0.getAddress();
        a0 = a1 + 0x30L;
        v1 = selectedSlot_8011d740.get() + slotScroll_8011d744.get();
        v0 = memcardData_8011dd10.get((int)v1).getAddress(); //TODO

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
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0xffL);
        setMessageBoxText(Saved_8011cb2c, 0);
        v1 = 0x800c_0000L;
        v0 = 0x36L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x36:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23c8L).get();
        v0 = 0x13L;
        if(v1 == v0) {
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
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        setMessageBoxText(MEMORY_CARD_is_not_inserted_in_MEMORY_CARD_slot_1_8011cb38, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x38:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        setMessageBoxText(Failed_loading_data_8011cb9c, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x39:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        setMessageBoxText(Cannot_access_the_MEMORY_CARD_in_MEMORY_CARD_slot_1_8011cbc4, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3a:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        setMessageBoxText(Not_enough_blocks_Saving_data_requires_1_block_8011cc30, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3b:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        setMessageBoxText(Failed_saving_data_8011cc90, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3c:
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          v0 = 0x8012_0000L;

          //LAB_800fff84
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();

          //LAB_800fff8c
          //LAB_800fff94
          renderSavedGames(a0, memcardData_8011dd10, 0);
          break;
        }
        a0 = 0xffL;
        FUN_8002437c(a0);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0xffL);

        //LAB_800fff38
        setMessageBoxText(Saved_data_for_this_game_does_not_exist_8011c788, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3d:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        setMessageBoxText(_8011d708, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x3e:
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          setMessageBoxText(Confused_8011c3bc, 0);
          v1 = 0x800c_0000L;
          v0 = 0x46L;

          //LAB_800ffc5c
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        //LAB_800fff80
        v0 = 0x8012_0000L;

        //LAB_800fff84
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();

        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(a0, memcardData_8011dd10, 0);
        break;

      case 0x40:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
        renderSavedGames(a0, memcardData_8011dd10, 0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          setMessageBoxText(The_MEMORY_CARD_in_MEMORY_CARD_slot_1_is_not_formatted_8011ccb8, 0);
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
          FUN_80103168(a0);
        } else {
          //LAB_800ffce4
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          renderSavedGames(a0, memcardData_8011dd10, 0);
        }

        //LAB_800ffcfc
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          setMessageBoxText(Do_you_want_to_format_8011cd28, 0x2);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          messageBox_8011dc90._18.set(v0);
          v0 = 0x42L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x42:
        v0 = messageBox(messageBox_8011dc90);
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
              setMessageBoxText(Blank_8011c664, 0x1);
            } else {
              //LAB_800ffdb0
              setMessageBoxText(Formatting_Do_not_remove_MEMORY_CARD_or_turn_off_the_power_8011cd58, 0x1);
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

        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(a0, memcardData_8011dd10, 0);
        break;

      case 0x43:
        messageBox(messageBox_8011dc90);
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(-0x23d0L).get();
        v0 = 0x8012_0000L;
        if(v1 == 0x7aL) {
          a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
          FUN_80103168(a0);
        } else {
          //LAB_800ffe14
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          renderSavedGames(a0, memcardData_8011dd10, 0);
        }

        //LAB_800ffe2c
        v0 = 0x8012_0000L;
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
          FUN_80103168(a0);
        } else {
          //LAB_800ffeb0
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          renderSavedGames(a0, memcardData_8011dd10, 0);
        }

        //LAB_800ffec8
        v0 = messageBox(messageBox_8011dc90);
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
          FUN_80103168(a0);
        } else {
          //LAB_800fff0c
          a0 = MEMORY.ref(4, v0).offset(-0x28bcL).get();
          renderSavedGames(a0, memcardData_8011dd10, 0);
        }

        //LAB_800fff24
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }

        //LAB_800fff38
        setMessageBoxText(Formatting_failed_8011cdd8, 0);
        v1 = 0x800c_0000L;
        v0 = 0x46L;

        //LAB_800fff48
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x46:
        //LAB_800fff68
        inventoryMenuState_800bdc28.setu(0x47L);

        //LAB_800fff6c
        //LAB_800fff70
        if(_800bdc30.get() == 0x7aL) {
          //LAB_80101580
          FUN_80103168(selectedSlot_8011d740.get());
          break;
        }

        //LAB_800fff80
        //LAB_800fff84
        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);
        break;

      case 0x47:
        if(_800bdc30.get() == 0x7aL) {
          //LAB_8010069c
          inventoryMenuState_800bdc28.setu(0x77L);
          FUN_80103168(selectedSlot_8011d740.get());
          break;
        }

        //LAB_800fffd0
        _8004dd30.setu(0);
        renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0);

        v1 = whichMenu_800bdc38.get();

        if(v1 == 0x13L) {
          inventoryMenuState_800bdc28.setu(0x26L);
          break;
        }

        //LAB_80100024
        if(v1 == 0x4L) {
          //LAB_80100018
          FUN_800fca0c(0x2L, 0xcL);
        } else {
          FUN_800fca0c(0x7dL, 0xcL);
        }

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
        renderBackground(a0, a1, a2);
        s0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, s0).offset(-0x28c0L).get();

        v0 = FUN_800fc8ec(a0);
        a0 = 0x9fL;
        a1 = a0;
        a2 = 0x3cL;
        a3 = v0;
        renderablePtr_800bdbe8.set(allocateUiElement(a0, a1, a2, a3));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
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
        setMessageBoxText(_8011ce14, 0x1);
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
        messageBox(messageBox_8011dc90);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = messageBox_8011dc90.ticks_10.get();

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
        messageBox_8011dc90._0c.incr();
        break;

      case 0x4c:
        messageBox(messageBox_8011dc90);
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
          messageBox_8011dc90._0c.incr();
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
        messageBox(messageBox_8011dc90);
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
        readMemcardFile(a0, drgnpda_800fb7a0.getString(), a2, a3, a4);
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
        v0 = handleMenuUpDown(a0, a1);
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
                setMessageBoxText(_8011d044, 0);
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
        setMessageBoxText(Blank_8011c668, 0);
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
        v0 = messageBox(messageBox_8011dc90);

        if(v0 != 0) {
          setMessageBoxText(_8011cdfc, 0x2);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          messageBox_8011dc90._18.set(v0);
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
        v0 = messageBox(messageBox_8011dc90);
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
        setMessageBoxText(_8011ce14, 0x1);
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
        messageBox(messageBox_8011dc90);
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = messageBox_8011dc90.ticks_10.get();

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
        messageBox_8011dc90._0c.incr();
        break;

      case 0x55:
        messageBox(messageBox_8011dc90);
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
        messageBox_8011dc90._0c.incr();
        break;

      case 0x56:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          setMessageBoxText(_8011ce08, 0);
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          setMessageBoxText(Blank_8011c3a4, 0x2);
          v0 = 0x1L;
          v1 = 0x800c_0000L;
          messageBox_8011dc90._18.set(v0);
          v0 = 0x58L;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x58:
        v0 = messageBox(messageBox_8011dc90);
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
        v0 = messageBox(messageBox_8011dc90);
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
        setMessageBoxText(_8011ce00, 0x1);
        v1 = 0x800c_0000L;
        v0 = 0x5aL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x5a:
        v0 = 0x8012_0000L;
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        v0 = messageBox(messageBox_8011dc90);
        v0 = messageBox_8011dc90.ticks_10.get();

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
        messageBox(messageBox_8011dc90);
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
        FUN_80103168(selectedSlot_8011d740.get());

        if(messageBox(messageBox_8011dc90) == 0) {
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
        setMessageBoxText(Blank_8011c5d8, 0);
        inventoryMenuState_800bdc28.setu(0x5dL);

        tmpA2.release();
        break;

      case 0x5e:
        setMessageBoxText(_8011c8c8, 0);
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }
        setMessageBoxText(Really_want_to_throw_this_away_8011c8d4, 0x2);
        v0 = 0x1L;
        v1 = 0x800c_0000L;
        messageBox_8011dc90._18.set(v0);

        //LAB_80100db4
        v0 = 0x63L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x60:
        setMessageBoxText(_8011d710, 0);
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          break;
        }
        setMessageBoxText(_8011c8bc, 0x2);
        a1 = 0;
        a0 = MEMORY.ref(4, s1).offset(-0x28c0L).get();
        v0 = 0x1L;
        messageBox_8011dc90._18.set(v0);
        FUN_80103168(a0);
        v1 = 0x800c_0000L;

        //LAB_80100db4
        v0 = 0x63L;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x62:
        setMessageBoxText(_8011d060, 0x2);
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
        v0 = messageBox(messageBox_8011dc90);
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

          setMessageBoxText(_8011d5c4, 0);
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
            if((int)v0 >= 0x100L && a2 != 0 || gameState_800babc8._1e6.get() + a3 > 0x20L && a3 != 0) {
              //LAB_80101090
              setMessageBoxText(_8011cf54, 0);
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
              FUN_80023484((int)a0);
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
        v0 = messageBox(messageBox_8011dc90);
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
        setMessageBoxText(_8011c8cc, 0);
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
        setMessageBoxText(Item_thrown_away_8011c914, 0);
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
        v0 = messageBox(messageBox_8011dc90);
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

          setMessageBoxText(m_8011c66c, 0x1);
          v0 = 0x800c_0000L;
          v1 = 0x800c_0000L;
          MEMORY.ref(4, v0).offset(-0x2414L).setu(0);
          v0 = 0x6aL;
          MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        }

        break;

      case 0x6a:
        v0 = messageBox(messageBox_8011dc90);
        v0 = messageBox_8011dc90.ticks_10.get();

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
          renderablePtr_800bdbec.set(allocateUiElement(0xd3L, 0xd3L, 0x44L, 0x50L));
          renderablePtr_800bdbec.deref()._3c.set(0x1f);
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

        unloadRenderable(renderablePtr_800bdbec.deref());
        renderablePtr_800bdbec.set(allocateUiElement(0xd3L, 0xd9L, 0x44L, 0x50L));
        renderablePtr_800bdbec.deref()._3c.set(0x1f);
        inventoryMenuState_800bdc28.setu(0x6bL);

        //LAB_8010157c
        v0 = 0x8012_0000L;

        //LAB_80101580
        a0 = MEMORY.ref(4, v0).offset(-0x28c0L).get();
        a1 = 0;
        FUN_80103168(a0);
        break;

      case 0x6b:
        messageBox(messageBox_8011dc90);
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
          unloadRenderable(renderablePtr_800bdbec.deref());
          renderablePtr_800bdbec.set(allocateUiElement(0xd3L, 0xd3L, 0x44L, 0x50L));
          renderablePtr_800bdbec.deref()._3c.set(0x1f);
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
        messageBox(messageBox_8011dc90);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x23bcL).get();

        v0 = v0 & 0x20L;
        if(v0 != 0) {
          unloadRenderable(renderablePtr_800bdbec.deref());
          inventoryMenuState_800bdc28.setu(0x6dL);
          messageBox_8011dc90._0c.incr();
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          return;
        }

        //LAB_80101644
        setMessageBoxText(_8011d048, 0);
        v1 = 0x800c_0000L;

        //LAB_80101650
        v0 = 0x6dL;
        MEMORY.ref(4, v1).offset(-0x23d8L).setu(v0);
        break;

      case 0x6f:
        setMessageBoxText(_8011d700, 0);
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 == 0) {
          return;
        }

        //LAB_80101644
        setMessageBoxText(_8011d704, 0);
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          //LAB_80101644
          setMessageBoxText(_8011d70c, 0);
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          //LAB_801016b0
          setMessageBoxText(_8011ce0c, 0);
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          //LAB_801016b0
          setMessageBoxText(_8011ce10, 0);
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
        v0 = messageBox(messageBox_8011dc90);
        if(v0 != 0) {
          setMessageBoxText(_8011ce04, 0);
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
        v0 = messageBox(messageBox_8011dc90);
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
        v0 = messageBox(messageBox_8011dc90);
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
        v0 = messageBox(messageBox_8011dc90);
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
        setMessageBoxText(_8011ce18, 0);
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
        v0 = messageBox(messageBox_8011dc90);
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

      case 0x7b: // Start fade out
        scriptStartEffect(0x1L, 0xaL);
        inventoryMenuState_800bdc28.setu(0x7cL);

      case 0x7c:
        switch((int)_800bdc2c.get()) {
          case 0x1 -> {
            FUN_80102484(0);

            //LAB_801018f0
            renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x4L, 0xfeL);
          }

          case 0x2 -> {
            FUN_80102484(0x1L);
            renderItemSubmenu(selectedItemSubmenuOption_8011d73c.get(), 0x4L);

            //LAB_801018f0
            renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x6L, 0xfeL);
          }

          case 0x5 -> FUN_801024c4(0xfeL);
          case 0x6 -> FUN_80102660(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xfeL);
          case 0x7 -> renderStatusMenu(charSlot_8011d734.get(), 0xfeL);

          case 0x8 -> {
            if(charSlot_8011d734.get() != 0) {
              v0 = selectedSlot_8011d740.get() + _8011d748.get();
            } else {
              //LAB_80101994
              v0 = selectedSlot_8011d740.get() + slotScroll_8011d744.get();
            }

            //LAB_801019a8
            FUN_80102840(slotScroll_8011d744.get(), _8011d748.get(), _8011dcb8.offset(charSlot_8011d734.get() * 0x4L).deref(1).offset(v0 * 0x4L).get(), 0);

            if((int)_800bb168.get() < 0xffL) {
              return;
            }

            removeFromLinkedList(_8011dcb8.get());
            removeFromLinkedList(_8011dcbc.get());
          }

          case 0x9 -> renderAdditions(charSlot_8011d734.get(), selectedSlot_8011d740.get(), additions_8011e098, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()).selectedAddition_19.get(), 0xfeL);
          case 0xa -> FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);
          case 0xb -> FUN_80102f74(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xfeL);
          //LAB_80101af4
          case 0xc -> renderSavedGames(slotScroll_8011d744.get(), memcardData_8011dd10, 0xfeL);
          case 0xd -> FUN_80103168(selectedSlot_8011d740.get());
          //LAB_80101af4
          case 0xe -> renderSavedGames(slotScroll_8011d744.get(), null, 0xfeL);
        }

        //LAB_80101afc
        //LAB_80101b00
        if((int)_800bb168.get() >= 0xffL) {
          //LAB_80101b14
          //LAB_80101b18
          inventoryMenuState_800bdc28.setu(_800bdc30.get());
        }

        break;

      case 0x7d:
        FUN_8002437c(0xffL);
        removeFromLinkedList(drgn0_6666FilePtr_800bdc3c.getPointer());

        final long menu = whichMenu_800bdc38.get();
        if(menu == 0xeL) {
          //LAB_80101b90
          scriptStartEffect(0x2L, 0xaL);
          whichMenu_800bdc38.setu(0xfL);
        } else if(menu == 0x13L) {
          //LAB_80101ba4
          whichMenu_800bdc38.setu(0x14L);
        } else if(menu == 0x18L) {
          scriptStartEffect(0x2L, 0xaL);
          whichMenu_800bdc38.setu(0x19L);
        } else if((int)menu < 0x14L) {
          //LAB_80101bb0
          scriptStartEffect(0x2L, 0xaL);
          whichMenu_800bdc38.setu(0x5L);
        } else {
          //LAB_80101b70
          //LAB_80101bb0
          scriptStartEffect(0x2L, 0xaL);
          whichMenu_800bdc38.setu(0x5L);
        }

        //LAB_80101bc4
        if(mainCallbackIndex_8004dd20.get() == 0x5L && loadingSmapOvl_8004dd08.get() == 0) {
          FUN_800e3fac();
        }

        //LAB_80101bf8
        //LAB_80101bfc
        _800bdf00.setu(0xdL);

      case 0x72:
      case 0x3f:
        //LAB_80101c00
    }
  }

  @Method(0x80101d10L)
  public static void renderInventoryMenu(final long selectedOption, final long a1, long a2) {
    a2 = a2 ^ 0xffL;

    final long s5 = canSave_8011dc88.get() != 0 ? a1 : 0x6L;

    //LAB_80101d54
    final long s4 = a2 < 0x1L ? 1 : 0;
    if(s4 != 0) {
      renderDragoonSpirits(gameState_800babc8.dragoonSpirits_19c, 40, 197);
      renderEightDigitNumber(67, 184, gameState_800babc8.gold_94.get(), 0); // Gold
      renderCharacter(146, 184, 10);
      renderCharacter(164, 184, 10);
      renderTwoDigitNumber(166, 204, gameState_800babc8.stardust_9c.get()); // Stardust
    }

    //LAB_80101db8
    renderThreeDigitNumber(128, 184, getTimestampPart(gameState_800babc8.timestamp_a0.get(), 0), 0x3L);
    renderTwoDigitNumber(152, 184, getTimestampPart(gameState_800babc8.timestamp_a0.get(), 1), 0x3L);
    renderTwoDigitNumber(170, 184, getTimestampPart(gameState_800babc8.timestamp_a0.get(), 2), 0x3L);
    renderCharacterSlot(194,  16, gameState_800babc8.charIndex_88.get(0).get(), s4, 0);
    renderCharacterSlot(194,  88, gameState_800babc8.charIndex_88.get(1).get(), s4, 0);
    renderCharacterSlot(194, 160, gameState_800babc8.charIndex_88.get(2).get(), s4, 0);
    renderCentredText(chapterNames_80114248.get(gameState_800babc8.chapterIndex_98.get()).deref(), 94, 24, 0x4L);

    final LodString v1;
    if(mainCallbackIndex_8004dd20.get() == 0x5L) {
      v1 = submapNames_8011c108.get((int)_800bd808.get()).deref();
    } else {
      //LAB_80101ec0
      v1 = worldMapNames_8011c1ec.get((int)_800bf0b0.get()).deref();
    }

    //LAB_80101ed4
    renderCentredText(v1, 90, 38, 0x4L);

    //LAB_80101f0c
    renderCentredText(Status_8011ceb4,   62, getMenuOptionY(0) + 2, selectedOption == 0 ? 5 : a1);
    //LAB_80101f3c
    renderCentredText(Item_8011cec4,     62, getMenuOptionY(1) + 2, selectedOption == 1 ? 5 : a1);
    //LAB_80101f6c
    renderCentredText(Armed_8011ced0,    62, getMenuOptionY(2) + 2, selectedOption == 2 ? 5 : a1);
    //LAB_80101f9c
    renderCentredText(Addition_8011cedc, 62, getMenuOptionY(3) + 2, selectedOption == 3 ? 5 : a1);
    //LAB_80101fcc
    renderCentredText(Replace_8011cef0,  62, getMenuOptionY(4) + 2, selectedOption == 4 ? 5 : a1);
    //LAB_80101ff8
    renderCentredText(Config_8011cf00,   62, getMenuOptionY(5) + 2, selectedOption == 5 ? 5 : a1);
    //LAB_80102028
    renderCentredText(Save_8011cf10,     62, getMenuOptionY(6) + 2, selectedOption == 6 ? 5 : s5);

    if(s4 == 0) {
      uploadRenderables();
    }

    //LAB_80102040
  }

  @Method(0x80102064L)
  public static void renderItemSubmenu(final long a0, final long a1) {
    FUN_801038d4(150, 20, 60);

    //LAB_801020ac
    renderCentredText(Use_it_8011cf1c, 0x8eL, getItemSubmenuOptionY(0), a0 == 0 ? 0x5L : a1);

    //LAB_801020d8
    renderCentredText(Discard_8011cf2c, 0x8eL, getItemSubmenuOptionY(0x1L), a0 == 0x1L ? 0x5L : a1);

    //LAB_80102104
    renderCentredText(List_8011cf3c, 0x8eL, getItemSubmenuOptionY(0x2L), a0 == 0x2L ? 0x5L : a1);

    //LAB_80102130
    renderCentredText(Goods_8011cf48, 0x8eL, getItemSubmenuOptionY(0x3L), a0 == 0x3L ? 0x5L : a1);
  }

  @Method(0x8010214cL)
  public static void renderOptionsMenu(final long optionIndex, final long vibrateMode, final long soundMode, final long morphMode, final long noteMode) {
    _800bdf00.setu(0x20L);

    renderCentredText(Vibrate_8011cf58, FUN_800fc7bc(0) - 0xfL, menuOptionY(0), optionIndex == 0 ? 0x5L : 0x4L);
    renderCentredText(Off_8011cf6c, FUN_800fc7bc(1), menuOptionY(0), vibrateMode == 0 ? 0x5L : 0x4L);
    renderCentredText(On_8011cf74, FUN_800fc7bc(2), menuOptionY(0), vibrateMode != 0 ? 0x5L : 0x4L);
    renderCentredText(Sound_8011cf7c, FUN_800fc7bc(0) - 0xfL, menuOptionY(1), optionIndex == 1 ? 0x5L : 0x4L);
    renderCentredText(Stereo_8011cf88, FUN_800fc7bc(1), menuOptionY(1), soundMode == 0 ? 0x5L : 0x4L);
    renderCentredText(Mono_8011cf98, FUN_800fc7bc(2), menuOptionY(1), soundMode != 0 ? 0x5L : 0x4L);
    renderCentredText(Morph_8011cfa4, FUN_800fc7bc(0) - 0xfL, menuOptionY(2), optionIndex == 2 ? 0x5L : 0x4L);
    renderCentredText(Normal_8011cfb0, FUN_800fc7bc(1), menuOptionY(2), morphMode == 0 ? 0x5L : 0x4L);
    renderCentredText(Short_8011cfc0, FUN_800fc7bc(2), menuOptionY(2), morphMode != 0 ? 0x5L : 0x4L);
    renderCentredText(Note_8011c814, FUN_800fc7bc(0) - 0xfL, menuOptionY(3), optionIndex != 3 ? 0x4L : 0x5L);
    renderCentredText(Off_8011c838, FUN_800fc7d0(1), menuOptionY(3), noteMode == 0 ? 0x5L : 0x4L);
    renderCentredText(Half_8011c82c, FUN_800fc7d0(2), menuOptionY(3), noteMode == 1 ? 0x5L : 0x4L);
    renderCentredText(Stay_8011c820, FUN_800fc7d0(3), menuOptionY(3), noteMode == 2 ? 0x5L : 0x4L);

    _800bdf00.setu(0x21L);
  }

  @Method(0x80102484L)
  public static void FUN_80102484(final long a0) {
    //LAB_801024ac
    FUN_801038d4(a0 != 0 ? 23 : 24, 112, getMenuOptionY(0x1L) + 3);
  }

  @Method(0x801024c4L)
  public static void FUN_801024c4(final long a0) {
    final long s1 = (a0 ^ 0xffL) < 0x1L ? 1 : 0;

    FUN_801082a0(198,  16, (int)_800bdbf8.get(), s1);
    FUN_801082a0(255,  16, (int)_800bdbfc.get(), s1);
    FUN_801082a0(312,  16, (int)_800bdc00.get(), s1);
    FUN_801082a0(198, 122, (int)_800bdc04.get(), s1);
    FUN_801082a0(255, 122, (int)_800bdc08.get(), s1);
    FUN_801082a0(312, 122, (int)_800bdc0c.get(), s1);

    if(gameState_800babc8.charIndex_88.get(0).get() != -1) {
      renderCharacterSlot(16, 16, gameState_800babc8.charIndex_88.get(0).get(), s1, gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(0).get())._04.get() & 0x20L);
    }

    //LAB_801025b4
    if(gameState_800babc8.charIndex_88.get(1).get() != -1) {
      renderCharacterSlot(16, 88, gameState_800babc8.charIndex_88.get(1).get(), s1, gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(1).get())._04.get() & 0x20L);
    }

    //LAB_801025f8
    if(gameState_800babc8.charIndex_88.get(2).get() != -1) {
      renderCharacterSlot(16, 160, gameState_800babc8.charIndex_88.get(2).get(), s1, gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(2).get())._04.get() & 0x20L);
    }

    //LAB_8010263c
    uploadRenderables();
  }

  @Method(0x80102660L)
  public static void FUN_80102660(final int charSlot, final long slotIndex, final long slotScroll, final long a3) {
    final long s0 = (a3 ^ 0xffL) < 0x1L ? 1 : 0;

    renderCharacterSlot(0x10L, 0x15L, characterIndices_800bdbb8.get(charSlot).get(), s0, 0);
    renderCharacterStats(characterIndices_800bdbb8.get(charSlot).get(), (int)_8011d7c8.offset((slotIndex + slotScroll) * 0x4L).get(), s0);
    renderCharacterEquipment(characterIndices_800bdbb8.get(charSlot).get(), s0);

    if(s0 != 0) {
      allocateUiElement(0x5aL, 0x5aL, 0xc2L, 0x60L);
      _800bdb9c.set(allocateUiElement(0x3dL, 0x44L, 0x166L, FUN_800fc804(0)));
      _800bdba0.set(allocateUiElement(0x35L, 0x3cL, 0x166L, FUN_800fc804(3)));
    }

    //LAB_80102748
    FUN_80109410(0xc2L, 0x5cL, _8011d7c8.getAddress(), slotScroll, 0x4L, _800bdb9c.deref(), _800bdba0.deref());
    FUN_80109074(0, 0xc2L, 0xb2L, _8011d7c8.offset((slotIndex + slotScroll) * 0x4L).get(), s0);

    uploadRenderables();
  }

  @Method(0x801027bcL)
  public static void renderStatusMenu(final int charSlot, final long a1) {
    final long s0 = (a1 ^ 0xffL) < 0x1L ? 1 : 0;

    renderCharacterStats(characterIndices_800bdbb8.get(charSlot).get(), 0xff, s0);
    renderCharacterSlot(16, 21, characterIndices_800bdbb8.get(charSlot).get(), s0, 0);
    renderCharacterEquipment(characterIndices_800bdbb8.get(charSlot).get(), s0);
    renderCharacterSpells(characterIndices_800bdbb8.get(charSlot).get(), s0);

    uploadRenderables();
  }

  @Method(0x80102840L)
  public static void FUN_80102840(final long slotScroll, final long a1, final long a2, final long a3) {
    FUN_80109410(0x10L, 0x21L, _8011dcb8.get(), slotScroll, 0x7L, saveListUpArrow_800bdb94.derefNullable(), saveListDownArrow_800bdb98.derefNullable());
    FUN_80109410(0xc2L, 0x21L, _8011dcbc.get(), a1, 0x7L, _800bdb9c.derefNullable(), _800bdba0.derefNullable());
    renderThreeDigitNumber(0x88L, 0x18L, gameState_800babc8._1e4.get(), 0x2L);
    renderTwoDigitNumber(0x146L, 0x18L, gameState_800babc8._1e6.get(), 0x2L);

    final long s1 = (a3 ^ 0xffL) < 0x1L ? 1 : 0;
    if(s1 != 0) {
      allocateUiElement(0xbL, 0xbL, 0x9aL, 0x18L);
      renderThreeDigitNumber(0xa0L, 0x18L, 0xffL);
      allocateUiElement(0xbL, 0xbL, 0x152L, 0x18L);
      renderTwoDigitNumber(0x158L, 0x18L, 0x20L);
      allocateUiElement(0x55L, 0x55L, 0x10L, 0x10L);
      saveListUpArrow_800bdb94.set(allocateUiElement(0x3dL, 0x44L, 0xb4L, FUN_800fc814(0x2L)));
      saveListDownArrow_800bdb98.set(allocateUiElement(0x35L, 0x3cL, 0xb4L, FUN_800fc814(0x8L)));
      allocateUiElement(0x55L, 0x55L, 0xc2L, 0x10L);
      _800bdb9c.set(allocateUiElement(0x3dL, 0x44L, 0x166L, FUN_800fc814(0x2L)));
      _800bdba0.set(allocateUiElement(0x35L, 0x3cL, 0x166L, FUN_800fc814(0x8L)));
    }

    //LAB_80102a1c
    renderText(_8011c314, 0x20L, 0x16L, 0x4L);
    renderText(_8011c32c, 0xd2L, 0x16L, 0x4L);

    if(a3 != 0x1L) {
      FUN_801038d4(0x89L, 0x54L, 0xb2L).clut_30.set(0x7cebL);
      renderText(_8011d024, 0x25L, 0xb2L, 0x4L);
    }

    //LAB_80102a88
    FUN_80109074(0, 0xc2L, 0xb2L, a2, s1);
    uploadRenderables();
  }

  @Method(0x80102ad8L)
  public static void renderAdditions(final int charSlot, final long a1, final ArrayRef<MenuAdditionInfo> additions, final long selectedAdditionOffset, final long a4) {
    long sp2c = (a4 ^ 0xffL) < 0x1L ? 1 : 0;
    final int charIndex = characterIndices_800bdbb8.get(charSlot).get();

    if(additions.get(0).offset_00.get() == -1) {
      renderText(_8011c340, 106, 150, 0x4L);
    } else {
      //LAB_80102b9c
      if(sp2c != 0) {
        renderBackground(_801141e4.getAddress(), 0, 0);
      }

      //LAB_80102bbc
      //LAB_80102bf0
      for(int i = 0; i < 8; i++) {
        final long y = getAdditionSlotY(i);

        if(sp2c != 0 && i <  additionCounts_8004f5c0.get(charIndex).get()) { // Total number of additions
          renderCharacter(24, y, i + 1); // Addition number
        }

        //LAB_80102c30
        final int offset = additions.get(i).offset_00.get();
        final int index = additions.get(i).index_01.get();

        if(offset != -1) {
          //LAB_80102c58
          renderText(additions_8011a064.get(offset).deref(), 33, y - 2, offset != selectedAdditionOffset ? 0x4L : 0x5L);

          if(sp2c != 0) {
            final int level = gameState_800babc8.charData_32c.get(charIndex).additionLevels_1a.get(index).get();
            renderThreeDigitNumber(197, y, level); // Addition level
            renderThreeDigitNumber(230, y, additionData_80052884.get(offset).attacks_01.get()); // Number of attacks
            renderThreeDigitNumber(263, y, additionData_80052884.get(offset).sp_02.get(level - 1).get()); // SP
            renderThreeDigitNumber(297, y, additionData_80052884.get(offset).damage_0c.get() * (ptrTable_80114070.offset(offset * 0x4L).deref(1).offset(level * 0x4L).offset(0x3L).get() + 100) / 100); // Damage
            renderThreeDigitNumber(322, y, gameState_800babc8.charData_32c.get(charIndex).additionXp_22.get(index).get()); // Current XP

            if(level < 5) {
              renderThreeDigitNumber(342, y, additionXpPerLevel_800fba2c.get(level).get()); // Max XP
            } else {
              //LAB_80102d8c
              renderCharacter(354, y, 218); // Dash if at max XP
            }
          }
        }

        //LAB_80102d9c
        //LAB_80102da0
      }
    }

    //LAB_80102db0
    renderCharacterSlot(16, 21, charIndex, sp2c, 0);
    uploadRenderables();
  }

  @Method(0x80102dfcL)
  public static void FUN_80102dfc(final int charSlot, final long a1, final long a2, final long a3) {
    final long s2 = (a3 ^ 0xffL) < 0x1L ? 1 : 0;

    //LAB_80102e48
    for(int i = 0; i < _8011d7c4.get(); i++) {
      FUN_80108464(FUN_800fc8c0(i) - 0x5L, 0x78L, characterIndices_800bdbb8.get(i).get(), 0x1L, s2);
    }

    //LAB_80102e88
    if(s2 != 0) {
      allocateUiElement(84, 84, 16, 16);
      saveListUpArrow_800bdb94.set(allocateUiElement(61, 68, 180, FUN_800fc8dc(0) + 2));
      saveListDownArrow_800bdb98.set(allocateUiElement(53, 60, 180, FUN_800fc8dc(0x4L) + 2));
    }

    //LAB_80102ee8
    FUN_80109410(0x10L, 0xaL, _8011d7c8.getAddress(), a2, 0x5L, saveListUpArrow_800bdb94.derefNullable(), saveListDownArrow_800bdb98.derefNullable());
    FUN_80109074(0, 0xc2L, 0x10L, _8011d7c8.offset((a1 + a2) * 0x4L).get(), s2);
    uploadRenderables();
  }

  @Method(0x80102f74L)
  public static void FUN_80102f74(final int charSlot, final long a1, final long a2, final long a3) {
    final long s1 = (a3 ^ 0xffL) < 0x1L ? 1 : 0;

    if(s1 != 0) {
      allocateUiElement(0x55L, 0x55L, 0x10L, 0x10L);
      allocateUiElement(0x55L, 0x55L, 0xc2L, 0x10L);
      _800bdb9c.set(allocateUiElement(0x3dL, 0x44L, 0x166L, FUN_800fc814(0x2L)));
      _800bdba0.set(allocateUiElement(0x35L, 0x3cL, 0x166L, FUN_800fc814(0x8L)));
    }

    //LAB_8010301c
    renderText(Goods_8011cf48, 0x20L, 0x16L, 0x4L);
    renderText(Goods_8011cf48, 0xd2L, 0x16L, 0x4L);
    FUN_8010965c(a2, _800bdb9c.derefNullable(), _800bdba0.derefNullable());
    FUN_80109074(0x1L, 0xc2L, 0xb2L, _8011d7c8.offset((charSlot + (a1 << 0x1L) + a2) * 0x4L).get(), s1);
    uploadRenderables();
  }

  /**
   * @param fileScroll The first save game do display on the screen
   */
  @Method(0x801030c0L)
  public static void renderSavedGames(final long fileScroll, @Nullable final UnboundedArrayRef<MemcardDataStruct3c> saveData, final long a2) {
    if(a2 == 0xffL) {
      renderBackground(_80114258.getAddress(), 0, 0);
    }

    //LAB_80103100
    if(saveData != null) {
      //LAB_80103108
      for(int i = 0; i < 3; i++) {
        final long fileIndex = fileScroll + i;
        renderSaveGameSlot(fileIndex, saveData.get((int)fileIndex), getSlotY(i), a2 == 0xffL ? 1 : 0);
      }
    }

    //LAB_80103144
    uploadRenderables();
  }

  @Method(0x80103168L)
  public static void FUN_80103168(final long a0) {
    assert false;
  }

  @Method(0x801033ccL)
  public static void FUN_801033cc(final Renderable58 a0) {
    a0._28.set(0x1);
    a0._38.set(0);
    a0._34.set(0);
    a0._3c.set(0x1f);
  }

  @Method(0x801033e8L)
  public static void fadeOutArrow(final Renderable58 renderable) {
    final long x = renderable.x_40.get();
    final long y = renderable.y_44.get();

    unloadRenderable(renderable);

    final Renderable58 newRenderable = allocateUiElement(108, 111, x, y);
    newRenderable.flags_00.or(0x10L);
    FUN_801033cc(newRenderable);
  }

  @Method(0x80103444L)
  public static void FUN_80103444(@Nullable final Renderable58 a0, final long a1, final long a2, final long a3, final long a4) {
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
    FUN_80103444(renderablePtr_800bdba4.derefNullable(), 0x2dL, 0x34L, 0xaaL, 0xb1L);
    FUN_80103444(renderablePtr_800bdba8.derefNullable(), 0x25L, 0x2cL, 0xa2L, 0xa9L);

    if(a0 != 0) {
      if(renderablePtr_800bdba4.isNull()) {
        final Renderable58 renderable = allocateUiElement(0x6fL, 0x6cL, 0x12L, 0x10L);
        renderable._18.set(0x2dL);
        renderable._1c.set(0x34L);
        renderablePtr_800bdba4.set(renderable);
        FUN_801033cc(renderable);
      }
    } else {
      //LAB_80103578
      if(!renderablePtr_800bdba4.isNull()) {
        fadeOutArrow(renderablePtr_800bdba4.deref());
        renderablePtr_800bdba4.clear();
      }
    }

    //LAB_80103598
    if(a0 < a1 - 0x1L) {
      if(renderablePtr_800bdba8.isNull()) {
        final Renderable58 renderable = allocateUiElement(0x6fL, 0x6cL, 0x15eL, 0x10L);
        renderable._18.set(0x25L);
        renderable._1c.set(0x2cL);
        renderablePtr_800bdba8.set(renderable);
        FUN_801033cc(renderable);
      }
      //LAB_801035e8
    } else if(!renderablePtr_800bdba8.isNull()) {
      fadeOutArrow(renderablePtr_800bdba8.deref());
      renderablePtr_800bdba8.clear();
    }

    //LAB_80103604
  }

  @Method(0x8010361cL)
  public static void renderSaveListArrows(final long scroll, final long maxSaves) {
    FUN_80103444(saveListUpArrow_800bdb94.derefNullable(), 194, 201, 202, 209);
    FUN_80103444(saveListDownArrow_800bdb98.derefNullable(), 178, 185, 186, 193);

    if(scroll != 0) {
      if(saveListUpArrow_800bdb94.isNull()) {
        // Allocate up arrow
        final Renderable58 renderable = allocateUiElement(111, 108, 182, 16);
        renderable._18.set(194);
        renderable._1c.set(201);
        saveListUpArrow_800bdb94.set(renderable);
        FUN_801033cc(renderable);
      }
      //LAB_801036c8
    } else if(!saveListUpArrow_800bdb94.isNull()) {
      // Deallocate up arrow
      fadeOutArrow(saveListUpArrow_800bdb94.deref());
      saveListUpArrow_800bdb94.clear();
    }

    //LAB_801036e8
    if(scroll < maxSaves - 2) {
      if(saveListDownArrow_800bdb98.isNull()) {
        // Allocate down arrow
        final Renderable58 renderable = allocateUiElement(111, 108, 182, 208);
        renderable._18.set(178);
        renderable._1c.set(185);
        saveListDownArrow_800bdb98.set(renderable);
        FUN_801033cc(renderable);
      }
      //LAB_80103738
    } else if(!saveListDownArrow_800bdb98.isNull()) {
      // Deallocate down arrow
      fadeOutArrow(saveListDownArrow_800bdb98.deref());
      saveListDownArrow_800bdb98.clear();
    }

    //LAB_80103754
  }

  @Method(0x8010376cL)
  public static void renderBackground(final long a0, final long x, final long y) {
    //LAB_801037ac
    for(long s1 = a0; MEMORY.ref(1, s1).get() != 0xffL; s1 += 0x6L) {
      final Renderable58 s0 = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);

      FUN_80104b1c(s0, s1);

      s0.x_40.add(x);
      s0.y_44.add(y);
    }

    //LAB_801037f4
  }

  @Method(0x80103818L)
  public static Renderable58 allocateUiElement(final long startGlyph, final long endGlyph, final long x, final long y) {
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);

    if(endGlyph >= startGlyph) {
      renderable.glyph_04.set(startGlyph);
      renderable.startGlyph_10.set(startGlyph);
      renderable.endGlyph_14.set(endGlyph);
    } else {
      //LAB_80103870
      renderable.glyph_04.set(startGlyph);
      renderable.startGlyph_10.set(endGlyph);
      renderable.endGlyph_14.set(startGlyph);
      renderable.flags_00.or(0x20L);
    }

    //LAB_80103888
    if(startGlyph == endGlyph) {
      renderable.flags_00.or(0x4L);
    }

    //LAB_801038a4
    renderable.tpage_2c.set(0x19L);
    renderable.clut_30.set(0);
    renderable.x_40.set(x);
    renderable.y_44.set(y);

    return renderable;
  }

  @Method(0x80103910L)
  public static Renderable58 FUN_80103910(final long glyph, final long x, final long y, final long flags) {
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._c6a4, null);
    renderable.flags_00.or(flags | 0x4L);
    renderable.glyph_04.set(glyph);
    renderable.startGlyph_10.set(glyph);
    renderable.endGlyph_14.set(glyph);
    renderable.tpage_2c.set(0x19L);
    renderable.clut_30.set(0);
    renderable.x_40.set(x);
    renderable.y_44.set(y);
    return renderable;
  }

  @Method(0x801039a0L)
  public static long FUN_801039a0(final long equipmentId, final int charIndex) {
    if(charIndex == -1) {
      return 0;
    }

    //LAB_801039b4
    if(equipmentId < 0xc0L) {
      return _80114284.offset(charIndex).get() & _80111ff0.offset(equipmentId * 0x1cL).offset(0x3L).get();
    }

    //LAB_801039f0
    return 0;
  }

  @Method(0x801039f8L)
  public static int getEquipmentSlot(final int itemId) {
    if(itemId < 0xc0) {
      final long a0_0 = _80111ff0.offset(itemId * 0x1cL).offset(0x1L).get();

      //LAB_80103a2c
      for(int i = 0; i < 5; i++) {
        if((a0_0 & (0x80L >> i)) != 0) {
          return i;
        }

        //LAB_80103a44
      }
    }

    //LAB_80103a54
    return -1;
  }

  /**
   * @return Item ID of previously-equipped item, 0xff if invalid, 0x100 if no item was equipped
   */
  @Method(0x80103a5cL)
  public static int equipItem(final int equipmentId, final int charIndex) {
    if(charIndex == -1) {
      return 0xff;
    }

    if((FUN_801039a0(equipmentId, charIndex) & 0xffL) == 0) {
      return 0xff;
    }

    final int slot = getEquipmentSlot(equipmentId);
    if(slot == -1) {
      //LAB_80103ab8
      return 0xff;
    }

    //LAB_80103ac0
    final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);
    int previousId = charData.equipment_14.get(slot).get();
    charData.equipment_14.get(slot).set(equipmentId);

    if(previousId == -1) {
      previousId = 0x100;
    }

    //LAB_80103af4
    //LAB_80103af8
    return previousId;
  }

  @Method(0x80103b10L)
  public static void FUN_80103b10() {
    _8011d7c4.setu(0);

    long a2 = 0;

    //LAB_80103b48
    for(int slot = 0; slot < 9; slot++) {
      _800bdbf8.offset(slot * 0x4L).setu(-0x1L);
      characterIndices_800bdbb8.get(slot).set(-1);

      if((gameState_800babc8.charData_32c.get(slot)._04.get() & 0x1L) != 0) {
        characterIndices_800bdbb8.get((int)_8011d7c4.get()).set(slot);
        _8011d7c4.addu(0x1L);

        if(gameState_800babc8.charIndex_88.get(0).get() != slot && gameState_800babc8.charIndex_88.get(1).get() != slot && gameState_800babc8.charIndex_88.get(2).get() != slot) {
          _800bdbf8.offset(a2).setu(slot);
          a2 = a2 + 0x4L;
        }
      }

      //LAB_80103bb4
    }
  }

  @Method(0x80103bd4L)
  public static void FUN_80103bd4(final long unused) {
    //LAB_80103be8
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      _8011dcc0.offset(0x8L).offset(charSlot * 0x4L).setu(gameState_800babc8.charIndex_88.get(charSlot).get());
    }

    _8011dcc0.setu(0x5a02_0006L);
    _8011dcc0.offset(1, 0x14L).setu(gameState_800babc8.charData_32c.get(0).level_12.get());
    _8011dcc0.offset(1, 0x15L).setu(stats_800be5f8.get(0).dlevel_0f.get());
    _8011dcc0.offset(2, 0x16L).setu(gameState_800babc8.charData_32c.get(0).hp_08.get());
    _8011dcc0.offset(2, 0x18L).setu(stats_800be5f8.get(0).maxHp_66.get());
    _8011dcc0.offset(4, 0x1cL).setu(gameState_800babc8.gold_94.get());
    _8011dcc0.offset(4, 0x20L).setu(gameState_800babc8.timestamp_a0.get());
    _8011dcc0.offset(4, 0x24L).setu(gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0x1ffL);
    _8011dcc0.offset(4, 0x28L).setu(gameState_800babc8.stardust_9c.get());

    if(mainCallbackIndex_8004dd20.get() == 0x8L) {
      //LAB_80103c8c
      _8011dcc0.offset(1, 0x2dL).setu(0x1L);
      _8011dcc0.offset(1, 0x2cL).setu(_800bf0b0.offset(1, 0x0L)); //1b
      //LAB_80103c98
    } else if(whichMenu_800bdc38.get() == 0x13L) {
      //LAB_80103c8c
      _8011dcc0.offset(1, 0x2dL).setu(0x3L);
      _8011dcc0.offset(1, 0x2cL).setu(gameState_800babc8.chapterIndex_98.get());
    } else {
      //LAB_80103cb4
      _8011dcc0.offset(1, 0x2dL).setu(0);
      _8011dcc0.offset(1, 0x2cL).setu(_800bd808);
    }
  }

  @Method(0x80103cc4L)
  public static void renderText(final LodString text, final long x, final long y, final long a3) {
    final long s2;
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
    Scus94491BpeSegment_8002.renderText(text, x    , y    , a3, 0);
    Scus94491BpeSegment_8002.renderText(text, x    , y + 1, s2, 0);
    Scus94491BpeSegment_8002.renderText(text, x + 1, y    , s2, 0);
    Scus94491BpeSegment_8002.renderText(text, x + 1, y + 1, s2, 0);
  }

  @Method(0x80103dd4L)
  public static int textLength(final LodString text) {
    //LAB_80103ddc
    int v1;
    for(v1 = 0; v1 < 0xff; v1++) {
      if(text.charAt(v1) == 0xa0ffL) {
        break;
      }
    }

    //LAB_80103dfc
    return v1;
  }

  /** TODO I'm pretty sure this is right, by why return the end of the string? */
  @Method(0x80103e04L)
  public static LodString FUN_80103e04(final LodString a0, final LodString a1) {
    //LAB_80103e34
    int i;
    for(i = 0; i < textLength(a1); i++) {
      a0.charAt(i, a1.charAt(i));
    }

    //LAB_80103e64
    a0.charAt(i, 0xa0ffL);
    return a0.slice(i);
  }

  @Method(0x80103e90L)
  public static void renderCentredText(final LodString text, final long x, final long y, final long a3) {
    renderText(text, x - textLength(text) * 0x4L, y, a3);
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
  public static long handleMenuUpDown(final long menuIndexPtr, final long menuOptionCount) {
    if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0) {
      playSound(0x1L);

      if(MEMORY.ref(4, menuIndexPtr).get() != 0) {
        MEMORY.ref(4, menuIndexPtr).subu(1);
      } else {
        //LAB_80104108
        MEMORY.ref(4, menuIndexPtr).setu(menuOptionCount - 1);
      }

      //LAB_8010410c
      //LAB_80104118
    } else if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0) {
      playSound(0x1L);

      if(MEMORY.ref(4, menuIndexPtr).get() < menuOptionCount - 1) {
        MEMORY.ref(4, menuIndexPtr).addu(1);
      } else {
        MEMORY.ref(4, menuIndexPtr).setu(0);
      }
    } else {
      return 0;
    }

    //LAB_80104110
    //LAB_80104148
    return 0x1L;
  }

  @Method(0x8010415cL)
  public static long FUN_8010415c(final long menuIndexPtr, final long menuItemCount) {
    if((inventoryJoypadInput_800bdc44.get() & 0x8000L) != 0 && MEMORY.ref(4, menuIndexPtr).get() != 0) {
      MEMORY.ref(4, menuIndexPtr).subu(0x1L);
      playSound(0x1L);
      return 0x1L;
    }

    //LAB_80104184
    if((inventoryJoypadInput_800bdc44.get() & 0x2000L) != 0 && MEMORY.ref(4, menuIndexPtr).get() < menuItemCount - 0x1L) {
      //LAB_801041b0
      MEMORY.ref(4, menuIndexPtr).addu(0x1L);
      playSound(0x1L);

      //LAB_801041c8
      return 0x1L;
    }

    //LAB_801041c4
    return 0;
  }

  @Method(0x801041d8L)
  public static long FUN_801041d8(final long menuOptionPtr) {
    if(handleMenuUpDown(menuOptionPtr, 0x2L) != 0) {
      return 0x1L;
    }

    if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
      playSound(0x3L);
      return 0x4L;
    }

    //LAB_80104220
    if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
      playSound(0x2L);

      if(MEMORY.ref(4, menuOptionPtr).get() == 0) {
        return 0x2L;
      }

      return 0x3L;
    }

    //LAB_80104244
    return 0;
  }

  @Method(0x80104254L) //TODO a1 struct
  public static void FUN_80104254(final LodString a0, final long a1) {
    final long v1 = MEMORY.ref(4, a1).offset(0x4L).get();

    final LodString a0_0;
    final LodString a1_0;
    if((int)v1 == -0x2L) {
      a0_0 = MEMORY.ref(4, a1 + 0x8L, LodString::new);
      a1_0 = _8011d618;
      //LAB_8010428c
    } else if((int)v1 == -0x1L) {
      a0_0 = FUN_80103e04(MEMORY.ref(4, a1 + 0x8L, LodString::new), a0);
      a1_0 = _8011d534;
      //LAB_801042b4
    } else if(v1 != 0) {
      FUN_800297a0(v1, FUN_80103e04(MEMORY.ref(4, a1 + 0x8L, LodString::new), a0));
      a0_0 = MEMORY.ref(4, a1 + 0x8L + textLength(MEMORY.ref(4, a1 + 0x8L, LodString::new)) * 0x2L, LodString::new);
      a1_0 = _8011d560;
    } else {
      //LAB_801042fc
      a0_0 = MEMORY.ref(4, a1 + 0x8L, LodString::new);
      a1_0 = a0;
    }

    //LAB_80104304
    FUN_80103e04(a0_0, a1_0);
  }

  @Method(0x80104324L) //TODO a0 struct
  public static void FUN_80104324(final long a0) {
    switch((int)MEMORY.ref(1, a0).get()) {
      case 2:
        FUN_80104254(_8011d57c, a0);
        break;

      case 3:
        FUN_80104254(_8011cfcc, a0);
        break;

      case 4:
        FUN_80104254(_8011d584, a0);
        break;

      case 5:
        FUN_80104254(_8011cff8, a0);
        break;

      case 6:
        FUN_80104254(_8011d58c, a0);
        break;

      case 8:
        FUN_80103e04(MEMORY.ref(4, a0 + 0x8L, LodString::new), _8011d594);
        break;

      case 7:
        long v1 = MEMORY.ref(4, a0).offset(0x4L).get();

        if((v1 & 0x80L) != 0) {
          FUN_80103e04(MEMORY.ref(4, a0 + 0x8L, LodString::new), _8011d5c8);
          break;
        }

        //LAB_801043f8
        if((v1 & 0x40L) != 0) {
          FUN_80103e04(MEMORY.ref(4, a0 + 0x8L, LodString::new), _8011d5e0);
          break;
        }

        //LAB_80104410
        if((v1 & 0x8L) != 0) {
          FUN_80103e04(MEMORY.ref(4, a0 + 0x8L, LodString::new), _8011d604);
          break;
        }

      case 9:
        //LAB_80104424
        FUN_80103e04(MEMORY.ref(4, a0 + 0x8L, LodString::new), _8011d618);
        break;
    }

    //LAB_80104430
    //LAB_80104438
  }

  @Method(0x801038d4L)
  public static Renderable58 FUN_801038d4(final long glyph, final long x, final long y) {
    final Renderable58 renderable = allocateUiElement(glyph, glyph, x, y);
    renderable.flags_00.or(0x8L);
    return renderable;
  }

  @Method(0x80104448L)
  public static long FUN_80104448() {
    //LAB_8010449c
    long s5 = 0;
    for(int i = 0; i < _8011d7c4.get(); i++) {
      s5 |= gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(i).get())._10.get();
    }

    //LAB_801044d4
    //LAB_801044e8
    for(int i = 0; i < 32; i++) {
      _8011d7c8.offset(i * 0x4L).setu(0xffL);
    }

    //LAB_80104530
    long s4 = 0;
    for(int i = 0; i < gameState_800babc8._1e6.get(); i++) {
      final long s0 = gameState_800babc8._2e9.get(i).get();

      if((FUN_80022afc(s0) & 0xffL) != 0) {
        final long a0 = _8004f2ac.offset((s0 - 0xc0L) * 0xcL).getAddress();
        final long s3 = _8011d7c8.offset(s4 * 0x4L).getAddress();
        MEMORY.ref(1, s3).offset(0x0L).setu(s0);
        MEMORY.ref(1, s3).offset(0x1L).setu(i);
        MEMORY.ref(2, s3).offset(0x2L).setu(0);

        if(MEMORY.ref(1, a0).offset(0xbL).get() == 0x8L && (MEMORY.ref(1, a0).offset(0x8L).get() & s5) == 0) {
          MEMORY.ref(2, s3).offset(0x2L).setu(0x4000L);
        }

        //LAB_80104594
        s4++;
      }

      //LAB_8010459c
    }

    //LAB_801045b0
    FUN_80013434(_8011d7c8.getAddress(), s4, 0x4L, getMethodAddress(Scus94491BpeSegment_8002.class, "FUN_80023978", long.class, long.class));
    return s4;
  }

  @Method(0x801045fcL)
  public static long FUN_801045fc(long a0) {
    long v0;
    long a1;
    long s1;
    long s2;
    long s3;
    long s5;
    long s7;
    long s6 = a0;
    long s4 = 0;
    long v1 = 0xffL;
    long s0 = 0xffL;
    v0 = _8011d7c8.getAddress();
    v0 = v0 + 0x3fcL;

    //LAB_80104640
    do {
      MEMORY.ref(1, v0).offset(0x0L).setu(v1);
      s0 = s0 - 0x1L;
      v0 = v0 - 0x4L;
    } while((int)s0 >= 0);

    v0 = 0x800c_0000L;
    v1 = gameState_800babc8.getAddress();
    v0 = gameState_800babc8._1e4.get();

    if((int)v0 > 0) {
      s0 = 0;
      s5 = v1;
      v0 = s6 << 1;
      v0 = v0 + s6;
      v0 = v0 << 2;
      v0 = v0 - s6;
      s7 = v0 << 2;
      v0 = _8011d7c8.getAddress();
      v1 = s4 << 2;
      s2 = v1 + v0;

      //LAB_80104694
      do {
        v0 = s0 + s5;
        s3 = MEMORY.ref(1, v0).offset(0x1e8L).get();
        s1 = s3 & 0xffL;
        a0 = s1;
        v0 = FUN_801039a0(a0, (int)s6);
        v0 = v0 & 0xffL;
        if(v0 != 0) {
          a0 = s1;
          v0 = getEquipmentSlot((int)a0);
          v1 = v0 & 0xffL;
          v0 = 0xffL;
          if(v1 != v0) {
            v0 = v1 + s7;
            v0 = v0 + s5;
            v0 = MEMORY.ref(1, v0).offset(0x340L).get();

            if(s1 != v0) {
              MEMORY.ref(1, s2).offset(0x0L).setu(s3);
              MEMORY.ref(1, s2).offset(0x1L).setu(s0);
              MEMORY.ref(2, s2).offset(0x2L).setu(0);
              s2 = s2 + 0x4L;
              s4 = s4 + 0x1L;
            }
          }
        }

        //LAB_801046f4
        v0 = MEMORY.ref(2, s5).offset(0x1e4L).getSigned();
        s0 = s0 + 0x1L;
      } while((int)s0 < (int)v0);
    }

    //LAB_80104708
    v0 = s4;
    return v0;
  }

  @Method(0x80104738L)
  public static long FUN_80104738(final long a0) {
    //LAB_8010476c
    for(int i = 0; i < 304; i++) {
      _8011dcb8.deref(1).offset(i * 0x4L).setu(0xffL);
      _8011dcbc.deref(1).offset(i * 0x4L).setu(0xffL);
    }

    //LAB_801047bc
    for(int i = 0; i < gameState_800babc8._1e6.get(); i++) {
      _8011dcbc.deref(1).offset(i * 0x4L).offset(0x0L).setu(gameState_800babc8._2e9.get(i).get());
      _8011dcbc.deref(1).offset(i * 0x4L).offset(0x1L).setu(i);
      _8011dcbc.deref(2).offset(i * 0x4L).offset(0x2L).setu(0);
    }

    //LAB_8010480c
    //LAB_8010482c
    int s1;
    for(s1 = 0; s1 < gameState_800babc8._1e4.get(); s1++) {
      _8011dcb8.deref(1).offset(s1 * 0x4L).offset(0x0L).setu(gameState_800babc8._1e8.get(s1).get());
      _8011dcb8.deref(1).offset(s1 * 0x4L).offset(0x1L).setu(s1);
      _8011dcb8.deref(2).offset(s1 * 0x4L).offset(0x2L).setu(0);

      if(a0 != 0 && FUN_80022898(gameState_800babc8._1e8.get(s1).get()) != 0) {
        _8011dcb8.deref(2).offset(s1 * 0x4L).offset(0x2L).oru(0x2000L);
      }

      //LAB_80104898
    }

    //LAB_801048ac
    if(a0 != 0) {
      return 0;
    }

    long s2 = s1;
    long t0 = 0;

    //LAB_801048e0
    for(int i = 0; i < _8011d7c4.get(); i++) {
      //LAB_801048e8
      for(int a1 = 0; a1 < 5; a1++) {
        if(gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(i).get()).equipment_14.get(a1).get() != 0xff) {
          _8011dcb8.deref(1).offset(s2 * 0x4L).offset(0x0L).setu(gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(i).get()).equipment_14.get(a1).get());
          _8011dcb8.deref(1).offset(s2 * 0x4L).offset(0x1L).setu(s2);
          _8011dcb8.deref(2).offset(s2 * 0x4L).offset(0x2L).setu(characterIndices_800bdbb8.get(i).get());
          _8011dcb8.deref(2).offset(s2 * 0x4L).offset(0x2L).oru(0x3000L);

          t0 = t0 + 0x1L;
          s2 = s2 + 0x1L;
        }

        //LAB_80104968
      }
    }

    //LAB_8010498c
    //LAB_80104990
    return t0;
  }

  @Method(0x801049b4L)
  public static long loadAdditions(final int charIndex, final ArrayRef<MenuAdditionInfo> additions) {
    //LAB_801049c8
    for(int i = 0; i < 9; i++) {
      additions.get(i).offset_00.set(-1);
      additions.get(i).index_01.set(-1);
    }

    if(charIndex == -0x1L) {
      return 0;
    }

    if(additionOffsets_8004f5ac.get(charIndex).get() == -1) { // No additions (Shiranda)
      //LAB_80104a08
      return 0;
    }

    //LAB_80104a10
    //LAB_80104a54
    long t5 = 0;
    int t0 = 0;
    for(int additionIndex = 0; additionIndex < additionCounts_8004f5c0.get(charIndex).get(); additionIndex++) {
      final long a0_0 = additionData_80052884.get(additionOffsets_8004f5ac.get(charIndex).get() + additionIndex)._00.get();

      if(a0_0 == -1 && (gameState_800babc8.charData_32c.get(charIndex)._04.get() & 0x40L) != 0) {
        additions.get(t0).offset_00.set(additionOffsets_8004f5ac.get(charIndex).get() + additionIndex);
        additions.get(t0).index_01.set(additionIndex);
        t0++;
        //LAB_80104aa4
      } else if(a0_0 > 0 && a0_0 <= gameState_800babc8.charData_32c.get(charIndex).level_12.get()) {
        additions.get(t0).offset_00.set(additionOffsets_8004f5ac.get(charIndex).get() + additionIndex);
        additions.get(t0).index_01.set(additionIndex);

        if(gameState_800babc8.charData_32c.get(charIndex).additionLevels_1a.get(additionIndex).get() == 0) {
          gameState_800babc8.charData_32c.get(charIndex).additionLevels_1a.get(additionIndex).set(1);
        }

        //LAB_80104aec
        if(a0_0 == gameState_800babc8.charData_32c.get(charIndex).level_12.get()) {
          t5 = additionOffsets_8004f5ac.get(charIndex).get() + additionIndex + 0x1L;
        }

        t0++;
      }

      //LAB_80104b00
    }

    //LAB_80104b14
    return t5;
  }

  @Method(0x80104b1cL)
  public static void FUN_80104b1c(final Renderable58 a0, final long a1) {
    if(MEMORY.ref(1, a1).get() != 0xffL) {
      a0.glyph_04.set(MEMORY.ref(1, a1).get());
      a0.flags_00.or(0x4L);
    }

    //LAB_80104b40
    a0.tpage_2c.set(0x19L);
    a0.clut_30.set(0);
    a0.x_40.set(MEMORY.ref(2, a1).offset(0x2L).get());
    a0.y_44.set(MEMORY.ref(2, a1).offset(0x4L).get());
  }

  @Method(0x80104b60L)
  public static void FUN_80104b60(final Renderable58 a0) {
    a0._28.set(0x1);
    a0._34.set(0);
    a0._38.set(0);
    a0._3c.set(0x23);
  }

  @Method(0x80104b7cL)
  public static long FUN_80104b7c(final long a0, final int charIndex) {
    final Memory.TemporaryReservation sp0x0tmp = MEMORY.temp(0x2c);

    sp0x0tmp.get().offset(0x28L).setu(a0);

    final long sp = sp0x0tmp.address;
    final long v0 = _800fba58.getAddress();

    //LAB_80104b94
    memcpy(sp, v0, 0x24);

    if(charIndex == -0x1L) {
      return 0;
    }

    //LAB_80104be0
    long v1 = (MEMORY.ref(4, sp).offset(0x28L).offset((MEMORY.ref(4, sp).offset(charIndex * 0x4L).get() >>> 5) * 0x4L).get() & (0x1L << (MEMORY.ref(4, sp).offset(charIndex * 0x4L).get() & 0x1fL))) > 0 ? 1 : 0;
    if(charIndex == 0) {
      v1 = v1 | a0 >>> 7;
    }

    sp0x0tmp.release();

    //LAB_80104c24
    //LAB_80104c28
    return v1;
  }

  @Method(0x80104c30L)
  public static void renderTwoDigitNumber(final long x, final long y, final long value) {
    renderNumber(x, y, value, 0, 2);
  }

  @Method(0x80104dd4L)
  public static void renderThreeDigitNumber(final long x, final long y, final long value) {
    renderNumber(x, y, value, 0, 3);
  }

  @Method(0x80105048L)
  public static long FUN_80105048(final long a0, final long a1, final long a2, long value) {
    long flags = 0;
    final long clut;
    if(a2 < value) {
      clut = 0x7c6bL;
      //LAB_80105090
    } else if(a2 > value) {
      clut = 0x7c2bL;
    } else {
      clut = 0;
    }

    //LAB_801050a0
    //LAB_801050a4
    if(value >= 0x3e8L) {
      value = 0x3e7L;
    }

    //LAB_801050b0
    long s0 = value / 100 % 10;
    if(s0 != 0) {
      //LAB_80105108
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      //LAB_80105138
      //LAB_8010513c
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(clut);
      renderable._3c.set(0x21);
      renderable.x_40.set(a0);
      renderable.y_44.set(a1);
      flags |= 0x1L;
    }

    //LAB_80105190
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_801051ec
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      //LAB_8010521c
      //LAB_80105220
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(clut);
      renderable._3c.set(0x21);
      renderable.x_40.set(a0 + 6);
      renderable.y_44.set(a1);
    }

    //LAB_80105274
    s0 = value % 10;
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    //LAB_801052d8
    //LAB_801052dc
    renderable.flags_00.or(0xcL);
    renderable.glyph_04.set(s0);
    renderable.tpage_2c.set(0x19L);
    renderable.clut_30.set(clut);
    renderable._3c.set(0x21);
    renderable.x_40.set(a0 + 12);
    renderable.y_44.set(a1);
    return clut;
  }

  @Method(0x80105350L)
  public static void renderFourDigitNumber(final long x, final long y, final long value) {
    renderNumber(x, y, value, 0, 4);
  }

  /** Does something different with CLUT */
  @Method(0x8010568cL)
  public static void renderFourDigitNumber(final long x, final long y, long value, final long max) {
    long clut = 0;
    long flags = 0;

    if(value >= 9999) {
      value = 9999;
    }

    //LAB_801056d0
    if(max > 9999) {
      value = 9999;
    }

    //LAB_801056e0
    if(value < max / 2) {
      clut = 0x7cabL;
    }

    //LAB_801056f0
    if(value < max / 10) {
      clut = 0x7c2bL;
    }

    //LAB_80105714
    long s0 = value / 1_000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.glyph_04.set(s0);
      //LAB_80105784
      //LAB_80105788
      renderable.flags_00.or(0x4L);
      renderable.tpage_2c.set(0x19L);
      renderable.x_40.set(x);
      renderable.y_44.set(y);
      renderable.clut_30.set(clut);
      renderable._3c.set(0x21);
      flags |= 0x1L;
    }

    //LAB_801057c0
    //LAB_801057d0
    s0 = value / 100 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105830
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.glyph_04.set(s0);
      //LAB_80105860
      //LAB_80105864
      renderable.flags_00.or(0x4L);
      renderable.tpage_2c.set(0x19L);
      renderable.x_40.set(x + 6);
      renderable.y_44.set(y);
      renderable.clut_30.set(clut);
      renderable._3c.set(0x21);
      flags |= 0x1L;
    }

    //LAB_801058a0
    //LAB_801058ac
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105908
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.glyph_04.set(s0);
      //LAB_80105938
      //LAB_8010593c
      renderable.flags_00.or(0x4L);
      renderable.tpage_2c.set(0x19L);
      renderable.x_40.set(x + 12);
      renderable.y_44.set(y);
      renderable.clut_30.set(clut);
      renderable._3c.set(0x21);
    }

    //LAB_80105978
    //LAB_80105984
    s0 = value % 10;
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    renderable.glyph_04.set(s0);
    //LAB_801059e8
    //LAB_801059ec
    renderable.flags_00.or(0x4L);
    renderable.tpage_2c.set(0x19L);
    renderable.x_40.set(x + 18);
    renderable.y_44.set(y);
    renderable.clut_30.set(clut);
    renderable._3c.set(0x21);
  }

  @Method(0x80105a50L)
  public static void renderSixDigitNumber(final long x, final long y, long value) {
    long flags = 0;

    if(value > 999999) {
      value = 999999;
    }

    //LAB_80105a98
    long s0 = value / 100_000 % 10;
    if(s0 != 0) {
      final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      struct.glyph_04.set(s0);
      //LAB_80105b10
      //LAB_80105b14
      struct.flags_00.or(0x4L);
      struct.tpage_2c.set(0x19L);
      struct.clut_30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(x);
      struct.y_44.set(y);
      flags |= 0x1L;
    }

    //LAB_80105b4c
    s0 = value / 10_000 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105ba8
      final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      struct.glyph_04.set(s0);
      //LAB_80105bd8
      //LAB_80105bdc
      struct.flags_00.or(0x4L);
      struct.tpage_2c.set(0x19L);
      struct.clut_30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(x + 6);
      struct.y_44.set(y);
      flags |= 0x1L;
    }

    //LAB_80105c18
    s0 = value / 1_000 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105c70
      final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      struct.glyph_04.set(s0);
      //LAB_80105ca0
      //LAB_80105ca4
      struct.flags_00.or(0x4L);
      struct.tpage_2c.set(0x19L);
      struct.clut_30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(x + 12);
      struct.y_44.set(y);
      flags |= 0x1L;
    }

    //LAB_80105ce0
    s0 = value / 100 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105d38
      final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      struct.glyph_04.set(s0);
      //LAB_80105d68
      //LAB_80105d6c
      struct.flags_00.or(0x4L);
      struct.tpage_2c.set(0x19L);
      struct.clut_30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(x + 18);
      struct.y_44.set(y);
      flags |= 0x1L;
    }

    //LAB_80105da4
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105dfc
      final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      struct.glyph_04.set(s0);
      //LAB_80105e2c
      //LAB_80105e30
      struct.flags_00.or(0x4L);
      struct.tpage_2c.set(0x19L);
      struct.clut_30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(x + 24);
      struct.y_44.set(y);
    }

    //LAB_80105e68
    s0 = value % 10;
    final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    struct.glyph_04.set(s0);
    //LAB_80105ecc
    //LAB_80105ed0
    struct.flags_00.or(0x4L);
    struct.tpage_2c.set(0x19L);
    struct.clut_30.set(0);
    struct._3c.set(0x21);
    struct.x_40.set(x + 30);
    struct.y_44.set(y);
  }

  @Method(0x80105f2cL)
  public static void renderEightDigitNumber(final long x, final long y, final long value, final long flags) {
    renderNumber(x, y, value, flags, 8);
  }

  @Method(0x80106d10L)
  public static void FUN_80106d10(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Method(0x801073f8L)
  public static void FUN_801073f8(final long a0, final long a1, final long a2) {
    assert false;
  }

  /**
   * @param flags Bitset - 0x1: render leading zeros, 0x2: ?
   */
  public static void renderNumber(final long x, final long y, long value, long flags, final long digitCount) {
    if(value >= Math.pow(10, digitCount)) {
      value = (long)Math.pow(10, digitCount) - 1;
    }

    for(int i = 0; i < digitCount; i++) {
      long digit = value / (long)Math.pow(10, digitCount - (i + 1)) % 10;

      if(digit != 0 || i == digitCount - 1 || (flags & 0x1L) != 0) {
        final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
        struct.flags_00.or((flags & 0x2L) != 0 ? 0xcL : 0x4L);
        struct.glyph_04.set(digit);
        struct.tpage_2c.set(0x19L);
        struct.clut_30.set(0);
        struct._3c.set(0x21);
        struct.x_40.set(x + 6L * i);
        struct.y_44.set(y);
        flags |= 0x1L;
      }
    }
  }

  @Method(0x80107764L)
  public static void renderThreeDigitNumber(final long x, final long y, final long value, final long flags) {
    renderNumber(x, y, value, flags, 3);

/*
    if(value > 999L) {
      value = 999L;
    }

    // 100's place
    //LAB_801077a0
    long s0 = value / 100 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_801077f0
      final Drgn0_6666Struct58 struct = allocateRenderable(drgn0File6666Address_800bdc3c.get(), null);
      struct.charIndex_04.set(s0);

      //LAB_80107820
      //LAB_80107824
      struct._00.or((flags & 0x2L) != 0 ? 0xcL : 0x4L);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(x);
      struct.y_44.set(y);
      flags |= 0x1L;
    }

    // 10's place
    //LAB_8010785c
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_801078b4
      final Drgn0_6666Struct58 struct = allocateRenderable(drgn0File6666Address_800bdc3c.get(), null);
      struct.charIndex_04.set(s0);

      //LAB_801078e4
      //LAB_801078e8
      struct._00.or((flags & 0x2L) != 0 ? 0xcL : 0x4L);
      struct._2c.set(0x19L);
      struct._30.set(0);
      struct._3c.set(0x21);
      struct.x_40.set(x + 0x6L);
      struct.y_44.set(y);
    }

    // 1's place (always rendered even if 0)
    //LAB_80107920
    s0 = value % 10;

    //LAB_8010796c
    final Drgn0_6666Struct58 struct = allocateRenderable(drgn0File6666Address_800bdc3c.get(), null);
    struct.charIndex_04.set(s0);

    //LAB_8010799c
    //LAB_801079a0
    struct._00.or((flags & 0x2L) != 0 ? 0xcL : 0x4L);
    struct._2c.set(0x19L);
    struct._30.set(0);
    struct._3c.set(0x21);
    struct.x_40.set(x + 0xcL);
    struct.y_44.set(y);
*/

    //LAB_801079d8
  }

  @Method(0x801079fcL)
  public static void renderTwoDigitNumber(long x, final long y, final long value, final long flags) {
    renderNumber(x, y, value, flags, 2);
  }

  @Method(0x80107cb4L)
  public static void renderCharacter(final long x, final long y, final long character) {
    final Renderable58 v0 = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    v0.flags_00.or(0x4L);
    v0.glyph_04.set(character);
    v0.tpage_2c.set(0x19L);
    v0.clut_30.set(0x7ca9L);
    v0._3c.set(0x21);
    v0.x_40.set(x);
    v0.y_44.set(y);
  }

  @Method(0x80107d34L)
  public static void FUN_80107d34(final long x, final long y, final long a2, final long a3) {
    final long clut = FUN_80105048(x, y, a2, a3);
    final Renderable58 v0 = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    v0.flags_00.or(0xcL);
    v0.glyph_04.set(0xcL);
    v0.tpage_2c.set(0x19L);
    v0.clut_30.set(clut);
    v0._3c.set(0x21);
    v0.x_40.set(x + 20);
    v0.y_44.set(y);
  }

  @Method(0x80107dd4L)
  public static void renderXp(final long x, final long y, final long xp) {
    if(xp != 0) {
      renderSixDigitNumber(x, y, xp);
    } else {
      //LAB_80107e08
      final Renderable58 v0 = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      v0.flags_00.or(0x4L);
      v0.glyph_04.set(218);
      v0.tpage_2c.set(0x19L);
      v0.clut_30.set(0x7ca9L);
      v0._3c.set(0x21);
      v0.x_40.set(x + 30);
      v0.y_44.set(y);
    }

    //LAB_80107e58
  }

  @Method(0x80107e70L)
  public static long FUN_80107e70(final long x, final long y, final int charIndex) {
    //LAB_80107e90
    final long a0_0 = gameState_800babc8.charData_32c.get(charIndex)._10.get();

    if((_800bb0fc.get() & 0x10L) == 0) {
      return 0;
    }

    long v1 = a0_0 & 0x1L;

    if((a0_0 & 0x2L) != 0) {
      v1 = 0x2L;
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
    renderCentredText(struct.text_00.deref(), x + 24, y, struct._04.get());

    //LAB_80107f8c
    return 0x1L;
  }

  @Method(0x80107f9cL)
  public static void renderCharacterSlot(final long x, final long y, final int charIndex, final long a3, final long a4) {
    if(charIndex != -1) {
      if(a3 != 0) {
        allocateUiElement(74, 74, x, y)._3c.set(0x21);
        allocateUiElement(153, 153, x, y);

        if(charIndex < 9) {
          final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          FUN_80104b1c(struct, _801142d4.getAddress());
          struct.glyph_04.set(charIndex);
          struct.tpage_2c.add(0x1L);
          struct._3c.set(0x21);
          struct.x_40.set(x + 8);
          struct.y_44.set(y + 8);
        }

        //LAB_80108098
        final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);
        renderTwoDigitNumber(x + 154, y + 6, stats.level_0e.get());
        renderTwoDigitNumber(x + 112, y + 17, stats.dlevel_0f.get());
        renderThreeDigitNumber(x + 148, y + 17, stats.sp_08.get());
        renderFourDigitNumber(x + 100, y + 28, gameState_800babc8.charData_32c.get(charIndex).hp_08.get(), stats.maxHp_66.get());
        renderCharacter(x + 124, y + 28, 11);
        renderFourDigitNumber(x + 142, y + 28, stats.maxHp_66.get());
        renderThreeDigitNumber(x + 106, y + 39, stats.mp_06.get());
        renderCharacter(x + 124, y + 39, 11);
        renderThreeDigitNumber(x + 148, y + 39, stats.maxMp_6e.get());
        renderSixDigitNumber(x + 88, y + 50, gameState_800babc8.charData_32c.get(charIndex).xp_00.get());
        renderCharacter(x + 124, y + 50, 11);
        renderXp(x + 130, y + 50, getXpToNextLevel(charIndex));

        if(a4 != 0) {
          final Renderable58 struct = allocateUiElement(113, 113, x + 56, y + 24);
          struct._3c.set(0x21);
        }
      }

      //LAB_80108218
      if(FUN_80107e70(x + 48, y + 3, charIndex) == 0) {
        renderText(characterNames_801142dc.get(charIndex).deref(), x + 48, y + 3, 0x4L);
      }
    }

    //LAB_80108270
  }

  @Method(0x801082a0L)
  public static void FUN_801082a0(final long x, final long y, final int charIndex, final long a3) {
    if(charIndex != -0x1L) {
      if(a3 != 0) {
        if(charIndex < 9) {
          final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          FUN_80104b1c(renderable, _801142d4.getAddress());
          renderable.glyph_04.set(charIndex);
          renderable.tpage_2c.add(0x1L);
          renderable._3c.set(0x21);
          renderable.x_40.set(x + 0x2L);
          renderable.y_44.set(y + 0x8L);
        }

        //LAB_8010834c
        allocateUiElement(0x50L, 0x50L, x, y)._3c.set(0x21);
        allocateUiElement(0x9cL, 0x9cL, x, y);

        if((gameState_800babc8.charData_32c.get(charIndex)._04.get() & 0x2L) == 0) {
          allocateUiElement(0x72L, 0x72L, x, y + 0x18L)._3c.set(0x21);
        }

        //LAB_801083c4
        final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);
        renderFourDigitNumber(x + 0x19L, y + 0x39L, stats.level_0e.get());
        renderFourDigitNumber(x + 0x19L, y + 0x44L, stats.dlevel_0f.get());
        renderFourDigitNumber(x + 0x19L, y + 0x4fL, stats.hp_04.get(), stats.maxHp_66.get());
        renderFourDigitNumber(x + 0x19L, y + 0x5aL, stats.mp_06.get());
      }
    }

    //LAB_80108438
  }

  @Method(0x80108464L)
  public static void FUN_80108464(final long x, final long y, final int charIndex, final long a3, final long a4) {
    if(charIndex != -1) {
      FUN_80107e70(x - 4, y - 6, charIndex);

      if(a4 != 0) {
        allocateUiElement(112, 112, x, y)._3c.set(0x21);

        if(charIndex < 9) {
          final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          FUN_80104b1c(renderable, _801142d4.getAddress());
          renderable.glyph_04.set(charIndex);
          renderable.tpage_2c.add(0x1L);
          renderable._3c.set(0x21);
          renderable.x_40.set(x + 2);
          renderable.y_44.set(y + 8);
        }

        //LAB_80108544
        final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);
        renderFourDigitNumber(x + 25, y + 57, stats.hp_04.get(), stats.maxHp_66.get());
        renderFourDigitNumber(x + 25, y + 68, stats.maxHp_66.get());
        renderFourDigitNumber(x + 25, y + 79, stats.mp_06.get());
        renderFourDigitNumber(x + 25, y + 90, stats.maxMp_6e.get());
      }
    }

    //LAB_801085b8
  }

  @Method(0x801085e0L)
  public static void renderCharacterStats(final int charIndex, final int equipmentId, final long a2) {
    if(charIndex != -1) {
      final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0xa0);
      final ActiveStatsa0 statsTmp = sp0x10tmp.get().cast(ActiveStatsa0::new);

      if(equipmentId != 0xff) {
        final Memory.TemporaryReservation sp0xb0tmp = MEMORY.temp(0x5);

        //LAB_80108638
        memcpy(sp0xb0tmp.address, gameState_800babc8.charData_32c.get(charIndex).equipment_14.getAddress(), 5);

        equipItem(equipmentId, charIndex);
        FUN_80110030(0);

        //LAB_80108694
        memcpy(statsTmp.getAddress(), stats_800be5f8.get(charIndex).getAddress(), 0xa0);

        //LAB_801086e8
        memcpy(gameState_800babc8.charData_32c.get(charIndex).equipment_14.getAddress(), sp0xb0tmp.address, 5);

        sp0xb0tmp.release();

        FUN_80110030(0);
      } else {
        //LAB_80108720
        //LAB_80108740
        memcpy(statsTmp.getAddress(), stats_800be5f8.get(charIndex).getAddress(), 0xa0);
      }

      //LAB_80108770
      final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);
      FUN_80105048(0x3aL, 0x74L, stats.bodyAttack_6a.get(), statsTmp.bodyAttack_6a.get());
      FUN_80105048(0x5aL, 0x74L, stats.gearAttack_88.get(), statsTmp.gearAttack_88.get());
      FUN_80105048(0x7aL, 0x74L, stats.bodyAttack_6a.get() + stats.gearAttack_88.get(), statsTmp.bodyAttack_6a.get() + statsTmp.gearAttack_88.get());

      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) != 0) {
        FUN_80107d34(0x9fL, 0x74L, stats.dragoonAttack_72.get(), statsTmp.dragoonAttack_72.get());
      }

      //LAB_801087fc
      FUN_80105048(0x3aL, 0x80L, stats.bodyDefence_6c.get(), statsTmp.bodyDefence_6c.get());
      FUN_80105048(0x5aL, 0x80L, stats.gearDefence_8c.get(), statsTmp.gearDefence_8c.get());
      FUN_80105048(0x7aL, 0x80L, stats.bodyDefence_6c.get() + stats.gearDefence_8c.get(), statsTmp.bodyDefence_6c.get() + statsTmp.gearDefence_8c.get());

      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) != 0) {
        FUN_80107d34(0x9fL, 0x80L, stats.dragoonDefence_74.get(), statsTmp.dragoonDefence_74.get());
      }

      //LAB_8010886c
      FUN_80105048(0x3aL, 0x8cL, stats.bodyMagicAttack_6b.get(), statsTmp.bodyMagicAttack_6b.get());
      FUN_80105048(0x5aL, 0x8cL, stats.gearMagicAttack_8a.get(), statsTmp.gearMagicAttack_8a.get());
      FUN_80105048(0x7aL, 0x8cL, stats.bodyMagicAttack_6b.get() + stats.gearMagicAttack_8a.get(), statsTmp.bodyMagicAttack_6b.get() + statsTmp.gearMagicAttack_8a.get());

      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) != 0) {
        FUN_80107d34(0x9fL, 0x8cL, stats.dragoonMagicAttack_73.get(), statsTmp.dragoonMagicAttack_73.get());
      }

      //LAB_801088dc
      FUN_80105048(0x3aL, 0x98L, stats.bodyMagicDefence_6d.get(), statsTmp.bodyMagicDefence_6d.get());
      FUN_80105048(0x5aL, 0x98L, stats.gearMagicDefence_8e.get(), statsTmp.gearMagicDefence_8e.get());
      FUN_80105048(0x7aL, 0x98L, stats.bodyMagicDefence_6d.get() + stats.gearMagicDefence_8e.get(), statsTmp.bodyMagicDefence_6d.get() + statsTmp.gearMagicDefence_8e.get());

      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) != 0) {
        FUN_80107d34(0x9fL, 0x98L, stats.dragoonMagicDefence_75.get(), statsTmp.dragoonMagicDefence_75.get());
      }

      //LAB_8010894c
      FUN_80105048(0x3aL, 0xa4L, stats.bodySpeed_69.get(), statsTmp.bodySpeed_69.get());
      FUN_80105048(0x5aL, 0xa4L, stats.gearSpeed_86.get(), statsTmp.gearSpeed_86.get());
      FUN_80105048(0x7aL, 0xa4L, stats.bodySpeed_69.get() + stats.gearSpeed_86.get(), statsTmp.bodySpeed_69.get() + statsTmp.gearSpeed_86.get());

      FUN_80107d34(0x5aL, 0xb0L, stats.attackHit_90.get(), statsTmp.attackHit_90.get());
      FUN_80107d34(0x7aL, 0xb0L, stats.attackHit_90.get(), statsTmp.attackHit_90.get());
      FUN_80107d34(0x5aL, 0xbcL, stats.magicHit_92.get(), statsTmp.magicHit_92.get());
      FUN_80107d34(0x7aL, 0xbcL, stats.magicHit_92.get(), statsTmp.magicHit_92.get());
      FUN_80107d34(0x5aL, 0xc8L, stats.attackAvoid_94.get(), statsTmp.attackAvoid_94.get());
      FUN_80107d34(0x7aL, 0xc8L, stats.attackAvoid_94.get(), statsTmp.attackAvoid_94.get());
      FUN_80107d34(0x5aL, 0xd4L, stats.magicAvoid_96.get(), statsTmp.magicAvoid_96.get());
      FUN_80107d34(0x7aL, 0xd4L, stats.magicAvoid_96.get(), statsTmp.magicAvoid_96.get());

      sp0x10tmp.release();

      if(a2 != 0) {
        allocateUiElement(0x56L, 0x56L, 0x10L, 0x5eL);
      }
    }

    //LAB_80108a50
  }

  @Method(0x80108a6cL)
  public static void renderSaveGameSlot(final long fileIndex, final MemcardDataStruct3c saveData, final long y, final long a3) {
    if((a3 & 0xffL) != 0) {
      renderTwoDigitNumber(21, y, fileIndex + 1); // File number
    }

    //LAB_80108ab8
    if(saveData.fileIndex_04.get() == 0xfcL) {
      //LAB_80108b04
      //LAB_80108b20
      renderText(_8011d04c, 32, y, 0x4L);
      //LAB_80108ae8
    } else if(saveData.fileIndex_04.get() == 0xfeL) {
      //LAB_80108b10
      //LAB_80108b20
      renderText(Data_from_another_game_8011c88c, 32, y, 0x4L);
    } else if(saveData.fileIndex_04.get() == 0xffL) {
      //LAB_80108b1c
      //LAB_80108b20
      renderText(File_not_used_8011c7d8, 32, y, 0x4L);
    } else if(saveData.fileIndex_04.get() >= 0 && saveData.fileIndex_04.get() < 0xfL) {
      //LAB_80108b3c
      final ArrayRef<Pointer<LodString>> locationNames;
      if(saveData.saveType_2d.get() == 1) {
        //LAB_80108b5c
        locationNames = worldMapNames_8011c1ec;
      } else if(saveData.saveType_2d.get() == 3) {
        //LAB_80108b78
        locationNames = chapterNames_80114248;
      } else {
        //LAB_80108b90
        locationNames = submapNames_8011c108;
      }

      //LAB_80108ba0
      renderCentredText(locationNames.get(saveData.locationIndex_2c.get()).deref(), 278, y + 47, 0x4L); // Location text

      if((a3 & 0xffL) != 0) {
        allocateUiElement(0x4cL, 0x4cL,  16, y)._3c.set(0x21); // Left half of border
        allocateUiElement(0x4dL, 0x4dL, 192, y)._3c.set(0x21); // Right half of border

        // Load char 0
        if(saveData.char0Index_08.get() < 9) {
          final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          FUN_80104b1c(struct, _801142d4.getAddress());
          struct.glyph_04.set(saveData.char0Index_08);
          struct.tpage_2c.add(0x1L);
          struct._3c.set(0x21);
          struct.x_40.set(38);
          struct.y_44.set(y + 8);
        }

        // Load char 1
        //LAB_80108c78
        if(saveData.char1Index_0c.get() < 9) {
          final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          FUN_80104b1c(struct, _801142d4.getAddress());
          struct.glyph_04.set(saveData.char1Index_0c);
          struct.tpage_2c.add(0x1L);
          struct._3c.set(0x21);
          struct.x_40.set(90);
          struct.y_44.set(y + 8);
        }

        // Load char 2
        //LAB_80108cd4
        if(saveData.char2Index_10.get() < 9) {
          final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          FUN_80104b1c(struct, _801142d4.getAddress());
          struct.glyph_04.set(saveData.char2Index_10);
          struct.tpage_2c.add(0x1L);
          struct._3c.set(0x21);
          struct.x_40.set(142);
          struct.y_44.set(y + 8);
        }

        //LAB_80108d30
        renderTwoDigitNumber(224, y + 6, saveData.level_14.get()); // Level
        renderTwoDigitNumber(269, y + 6, saveData.dlevel_15.get()); // Dragoon level
        renderFourDigitNumber(302, y + 6, saveData.currentHp_16.get()); // Current HP
        renderFourDigitNumber(332, y + 6, saveData.maxHp_18.get()); // Max HP
        renderEightDigitNumber(245, y + 17, saveData.gold_1c.get(), 0); // Gold
        renderThreeDigitNumber(306, y + 17, getTimestampPart(saveData.time_20.get(), 0), 0x1L); // Time played hour
        renderCharacter(324, y + 17, 10); // Hour-minute colon
        renderTwoDigitNumber(330, y + 17, getTimestampPart(saveData.time_20.get(), 1), 0x1L); // Time played minute
        renderCharacter(342, y + 17, 10); // Minute-second colon
        renderTwoDigitNumber(348, y + 17, getTimestampPart(saveData.time_20.get(), 2), 0x1L); // Time played second
        renderTwoDigitNumber(344, y + 34, saveData.stardust_28.get()); // Stardust
        renderDragoonSpirits(saveData.dragoonSpirits_24, 223, y + 27);
      }
    }

    //LAB_80108e3c
  }

  @Method(0x80108e60L)
  public static void renderCharacterEquipment(final int charIndex, final long a1) {
    if(charIndex == -0x1L) {
      return;
    }

    final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);

    if(a1 != 0) {
      allocateUiElement(0x59L, 0x59L, 0xc2L, 0x10L);

      if(charData.equipment_14.get(0).get() != 0xff) {
        FUN_80103910(FUN_800228d0(charData.equipment_14.get(0).get()), 0xcaL, 0x11L, 0);
      }

      //LAB_80108ee4
      if(charData.equipment_14.get(1).get() != 0xff) {
        FUN_80103910(FUN_800228d0(charData.equipment_14.get(1).get()), 0xcaL, 0x1fL, 0);
      }

      //LAB_80108f10
      if(charData.equipment_14.get(2).get() != 0xff) {
        FUN_80103910(FUN_800228d0(charData.equipment_14.get(2).get()), 0xcaL, 0x2dL, 0);
      }

      //LAB_80108f3c
      if(charData.equipment_14.get(3).get() != 0xff) {
        FUN_80103910(FUN_800228d0(charData.equipment_14.get(3).get()), 0xcaL, 0x3bL, 0);
      }

      //LAB_80108f68
      if(charData.equipment_14.get(4).get() != 0xff) {
        FUN_80103910(FUN_800228d0(charData.equipment_14.get(4).get()), 0xcaL, 0x49L, 0);
      }
    }

    //LAB_80108f94
    //LAB_80108f98
    renderText(equipment_8011972c.get(charData.equipment_14.get(0).get()).deref(), 0xdcL, 0x13L, 0x4L);
    renderText(equipment_8011972c.get(charData.equipment_14.get(1).get()).deref(), 0xdcL, 0x21L, 0x4L);
    renderText(equipment_8011972c.get(charData.equipment_14.get(2).get()).deref(), 0xdcL, 0x2fL, 0x4L);
    renderText(equipment_8011972c.get(charData.equipment_14.get(3).get()).deref(), 0xdcL, 0x3dL, 0x4L);
    renderText(equipment_8011972c.get(charData.equipment_14.get(4).get()).deref(), 0xdcL, 0x4bL, 0x4L);

    //LAB_8010905c
  }

  @Method(0x80109074L)
  public static void FUN_80109074(final long a0, final long x, final long y, final long a3, final long a4) {
    if(a4 != 0) {
      allocateUiElement(0x5bL, 0x5bL, x, y);
    }

    //LAB_801090e0
    LodString s0 = null;
    if(a0 == 0) {
      //LAB_80109118
      if(a3 == 0xffL) {
        return;
      }

      s0 = _80117a10.get((int)a3).deref();
    } else if(a0 == 0x1L) {
      //LAB_8010912c
      if((int)a3 >= 0xffL) {
        //LAB_80109140
        s0 = _8011c254;
      } else {
        //LAB_80109154
        s0 = _8011b75c.get((int)a3).deref();
      }
      //LAB_80109108
    } else if(a0 == 0x2L) {
      //LAB_8010914c
      s0 = _80114300.get((int)a3).deref();
    }

    //LAB_80109160
    //LAB_80109168
    //LAB_80109188
    for(int i = 0; i < 4; i++) {
      int s4 = 0;
      final int len = Math.min(textLength(s0), 20);

      final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp((len + 1) * 2);
      final LodString s3 = sp0x10tmp.get().cast(LodString::new);

      //LAB_801091bc
      //LAB_801091cc
      int a1;
      for(a1 = 0; a1 < len; a1++) {
        if(s0.charAt(a1) == 0xa1ffL) {
          //LAB_8010924c
          s4 = 1;
          break;
        }

        s3.charAt(a1, s0.charAt(a1));
      }

      //LAB_801091fc
      s3.charAt(a1, 0xa0ffL);

      renderText(s3, x + 2, y + i * 14 + 4, 0x4L);

      if(textLength(s3) > len) {
        //LAB_80109270
        break;
      }

      //LAB_80109254
      s0 = s0.slice(textLength(s3) + s4);

      sp0x10tmp.release();
    }

    //LAB_80109284
  }

  @Method(0x801092b4L)
  public static void renderCharacterSpells(final int charIndex, final long a1) {
    if(charIndex == -0x1L) {
      return;
    }

    if(a1 != 0) {
      allocateUiElement(0x58L, 0x58L, 0xc2L, 0x65L);
    }

    //LAB_80109308
    if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) == 0) {
      return;
    }

    final byte[] sp0x10 = new byte[8];
    FUN_80022928(sp0x10, charIndex);
    final long s6 = FUN_80022a10(charIndex);

    //LAB_80109354
    for(int i = 0; i < 4; i++) {
      if(a1 != 0 && i < s6) {
        renderCharacter(200, 127 + i * 14, i + 0x1L);
      }

      //LAB_80109370
      final int v1 = sp0x10[i] & 0xff;
      if(v1 != 0xffL) {
        renderText(spells_80052734.get(v1).deref(), 210, 125 + i * 14, 0x4L);

        if(a1 != 0) {
          renderThreeDigitNumber(342, 128 + i * 14, _80114290.offset(v1).get());
        }
      }

      //LAB_801093c4
    }

    //LAB_801093e0
  }

  @Method(0x80109410L)
  public static void FUN_80109410(final long a0, final long a1, final long a2, final long slotScroll, final long a4, @Nullable final Renderable58 a5, @Nullable final Renderable58 a6) {
    long s3 = slotScroll;

    //LAB_8010947c
    int i;
    for(i = 0; i < a4 && MEMORY.ref(1, a2).offset(s3 * 0x4L).get() != 0xffL; i++) {
      final long s1 = a2 + s3 * 0x4L;

      long s0;
      if((MEMORY.ref(2, s1).offset(0x2L).get() & 0x6000L) == 0) {
        s0 = 0x4L;
      } else {
        s0 = 0x6L;
      }

      //LAB_801094ac
      renderText(equipment_8011972c.get((int)MEMORY.ref(1, s1).get()).deref(), a0 + 21, a1 + FUN_800fc814(i) + 2, s0);
      FUN_80103910(FUN_800228d0((int)MEMORY.ref(1, s1).get()), a0 + 4, a1 + FUN_800fc814(i), 0x8L);

      s0 = MEMORY.ref(2, s1).offset(0x2L).get();
      if((s0 & 0x1000L) != 0) {
        FUN_80103910(48 | (s0 & 0xfL), a0 + 148, a1 + FUN_800fc814(i) - 1, 0x8L).clut_30.set((s0 + 0x1f4L << 6) | 0x2bL);
        //LAB_80109574
      } else if((s0 & 0x2000L) != 0) {
        FUN_80103910(58, a0 + 148, a1 + FUN_800fc814(i) - 1, 0x8L).clut_30.set(0x7eaaL);
      }

      //LAB_801095a4
      s3++;
    }

    //LAB_801095c0
    //LAB_801095d4
    //LAB_801095e0
    if(a5 != null) { // There was an NPE here when fading out item list
      if(slotScroll != 0) {
        a5.flags_00.and(0xffff_ffbfL);
      } else {
        a5.flags_00.or(0x40L);
      }
    }

    //LAB_80109614
    //LAB_80109628
    if(a6 != null) { // There was an NPE here when fading out item list
      if(MEMORY.ref(1, a2).offset((i + slotScroll) * 0x4L).get() != 0xffL) {
        a6.flags_00.and(0xffff_ffbfL);
      } else {
        a6.flags_00.or(0x40L);
      }
    }
  }

  @Method(0x8010965cL)
  public static void FUN_8010965c(final long a0, @Nullable final Renderable58 a1, @Nullable final Renderable58 a2) {
    //LAB_801096c8
    long s1 = a0;
    int i;
    for(i = 0; i < 14 && _8011d7c8.offset(s1 * 0x4L).get() != 0xffL; i += 0x2L) {
      renderText(_8011c008.get((int)_8011d7c8.offset(s1 * 0x4L).get()).deref(), 0x25L, FUN_800fc814(i / 2) + 34, 0x4L);

      if(_8011d7c8.offset((s1 + 1) * 0x4L).get() != 0xffL) {
        renderText(_8011c008.get((int)_8011d7c8.offset((s1 + 1) * 0x4L).get()).deref(), 0xd6L, FUN_800fc814(i / 2) + 34, 0x4L);
      }

      //LAB_80109760
      s1 += 0x2L;
    }

    //LAB_8010977c
    //LAB_80109790
    //LAB_8010979c
    if(a1 != null) { // There was an NPE here
      if(a0 != 0) {
        a1.flags_00.and(0xffff_ffbfL);
      } else {
        a1.flags_00.or(0x40L);
      }
    }

    //LAB_801097d8
    //LAB_801097ec
    if(a2 != null) { // There was an NPE here
      if(_8011d7c8.offset((i + a0) * 0x4L).get() != 0xffL) {
        a2.flags_00.and(0xffff_ffbfL);
      } else {
        a2.flags_00.or(0x40L);
      }
    }
  }

  @Method(0x801098c0L)
  public static void renderDragoonSpirits(final ArrayRef<UnsignedIntRef> spirits, final long x, final long y) {
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
    for(int i = 0; i < 8; i++) {
      final long v0 = sp18.offset(i * 0x4L).get();
      if((spirits.get((int)(v0 >>> 5)).get() & 0x1L << (v0 & 0x1fL)) != 0) {
        final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);

        final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(6);
        final Value sp10 = sp0x10tmp.get();
        sp10.offset(1, 0x0L).setu(i + 0xdL);
        sp10.offset(2, 0x2L).setu(x + i * 0xcL);
        sp10.offset(2, 0x4L).setu(y);
        FUN_80104b1c(struct, sp10.getAddress());
        sp0x10tmp.release();

        struct._3c.set(0x21);
      }

      //LAB_801099a0
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
        final Ref<Long> memcardEvent = new Ref<>();
        final Ref<Long> memcardState = new Ref<>();
        FUN_8002efb8(0, memcardEvent, memcardState);

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

        v1 = memcardState.get();
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
            readMemcardFile(0, struct.name_00.get(), memcardDataPtr_8011e0b0.get(), 0x180L, 0x80L);

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
  public static long loadSaveFile(final long fileIndex) {
    final long memcardData = addToLinkedListTail(0x680L);
    readMemcardFile(0, "BASCUS-94491drgn00%02d".formatted(fileIndex), memcardData, 0x200L, 0x580L);

    final Ref<Long> eventPtr = new Ref<>(0L);
    final Ref<Long> statePtr = new Ref<>(1L);
    FUN_8002efb8(0, eventPtr, statePtr);
    final long state = statePtr.get();

    if(state == 0) {
      //LAB_8010a178
      //LAB_8010a17c
      memcpy(gameState_800babc8.getAddress(), memcardData, 0x52c);
      return 0;
    }

    if(state == 0x1L) {
      //LAB_8010a1a4
      //LAB_8010a1a8
      removeFromLinkedList(memcardData);
      return 1;
    }

    return 3;
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
  public static long messageBox(final MessageBox20 menu) {
    Renderable58 renderable;
    long a3;
    long s1;
    long v1;

    LOGGER.info("FUN_8010ecec %d", menu._0c.get());

    switch(menu._0c.get()) {
      case 0:
        return 0x1L;

      case 1: // Allocate "loading saved games" box
        menu._0c.set(0x2);
        menu.renderable_04.clear();
        menu.renderable_08.set(allocateUiElement(149, 142, menu.x_1c.get() - 50, menu.y_1e.get() - 10));
        menu.renderable_08.deref()._3c.set(0x20);
        menu.renderable_08.deref()._18.set(142);
        _8011e1e8.setu(0);

      case 2:
        if(menu.renderable_08.deref()._0c.get() != 0) {
          menu._0c.set(0x3);
        }

        break;

      case 3:
        _800bdf00.setu(0x1fL);
        long x = menu.x_1c.get() + 60;
        long y = menu.y_1e.get() + 7;

        menu.ticks_10.incr();

        if(!menu.text_00.isNull()) {
          final int textLength = textLength(menu.text_00.deref());
          final Memory.TemporaryReservation sp0x38tmp = MEMORY.temp((textLength + 1) * 2);
          final LodString line = sp0x38tmp.get().cast(LodString::new);

          final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp((textLength + 1) * 4);
          final ArrayRef<Pointer<LodString>> lines = sp0x10tmp.get().cast(ArrayRef.of(Pointer.classFor(LodString.class), textLength + 1, 4, Pointer.deferred(4, LodString::new)));

          lines.get(0).set(line);

          int lineIndex = 1;

          //LAB_8010ee1c
          int charIndex;
          for(charIndex = 0; charIndex < textLength; charIndex++) {
            line.charAt(charIndex, menu.text_00.deref().charAt(charIndex));

            if(line.charAt(charIndex) == 0xa1ffL) {
              lines.get(lineIndex).set(line.slice(charIndex + 1));
              line.charAt(charIndex, 0xa0ff);
              lineIndex++;
            }

            //LAB_8010ee50
          }

          //LAB_8010ee68
          line.charAt(charIndex, 0xa0ff);

          //LAB_8010ee80
          for(int i = 0; i < lineIndex; i++) {
            renderCentredText(lines.get(i).deref(), x, y, 0x4L);
            y = y + 14;
          }

          sp0x10tmp.release();
          sp0x38tmp.release();
        }

        //LAB_8010eeac
        _800bdf00.setu(0x21L);
        s1 = menu._15.get();

        if(s1 == 0) {
          //LAB_8010eed8
          if((inventoryJoypadInput_800bdc44.get() & 0x60L) != 0) {
            playSound(0x2L);
            menu._0c.set(0x4);
            _8011e1e8.setu(0x1L);
          }

          break;
        }

        if(s1 == 0x2L) {
          //LAB_8010ef10
          if(menu.renderable_04.isNull()) {
            renderable = allocateUiElement(125, 125, menu.x_1c.get() + 45, menu._18.get() * 14 + y + 5);
            menu.renderable_04.set(renderable);
            renderable._38.set(0);
            renderable._34.set(0);
            menu.renderable_04.deref()._3c.set(0x20);
          }

          //LAB_8010ef64
          _800bdf00.setu(0x1fL);

          if(menu._18.get() != 0) {
            a3 = 0x4L;
          } else {
            a3 = 0x5L;
          }

          //LAB_8010ef98
          renderCentredText(Yes_8011c20c, menu.x_1c.get() + 60, y + 7, a3);

          if(menu._18.get() != 0) {
            a3 = 0x5L;
          } else {
            a3 = 0x4L;
          }

          //LAB_8010efc8
          renderCentredText(No_8011c214, menu.x_1c.get() + 60, y + 21, a3);

          _800bdf00.setu(0x21L);
          v1 = FUN_801041d8(menu._18.getAddress()); //TODO address
          if(v1 == 0x2L) {
            //LAB_8010f040
            menu._0c.set(0x4);
            _8011e1e8.setu(menu._18.get() + 0x1L);
          } else if((int)v1 >= 0x3L) {
            //LAB_8010f000
            if((int)v1 < 0x5L) {
              //LAB_8010f05c
              menu._0c.set(0x4);
              _8011e1e8.setu(0x2L);
            }
          } else if(v1 == 0x1L) {
            //LAB_8010f014
            menu.renderable_04.deref().y_44.set(menu._18.get() * 14 + y + 5);
          }
        }

        break;

      case 4:
        menu._0c.set(0x5);

        if(!menu.renderable_04.isNull()) {
          unloadRenderable(menu.renderable_04.deref());
        }

        //LAB_8010f084
        unloadRenderable(menu.renderable_08.deref());
        renderable = allocateUiElement(0x8eL, 0x95L, menu.x_1c.get() - 50, menu.y_1e.get() - 10);
        menu.renderable_08.set(renderable);
        renderable._3c.set(0x20);
        menu.renderable_08.deref().flags_00.or(0x10L);
        break;

      case 5:
        if(menu.renderable_08.deref()._0c.get() != 0) {
          menu._0c.set(0x6);
        }

        break;

      case 6:
        menu._0c.set(0);
        return _8011e1e8.get();
    }

    //LAB_8010f108
    //LAB_8010f10c
    return 0;
  }

  @Method(0x8010f130L)
  public static void setMessageBoxText(@Nullable final LodString text, final int a1) {
    messageBox_8011dc90.text_00.setNullable(text);

    messageBox_8011dc90.x_1c.set(120);
    messageBox_8011dc90.y_1e.set(100);
    messageBox_8011dc90._15.set(a1);
    messageBox_8011dc90._18.set(0);
    messageBox_8011dc90.ticks_10.set(0);
    messageBox_8011dc90._0c.set(1);
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

    final long spc0 = a0;

    //LAB_80110070
    final long[] sp10 = new long[10];
    for(int charIndex = 0; charIndex < 10; charIndex++) {
      sp10[charIndex] = _800fbd08.get(charIndex).get();
    }

    //LAB_801100bc
    final long[] sp38 = new long[9];
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      sp38[charIndex] = _800fbd30.get(charIndex).get();
    }

    //LAB_80110104
    final long[] sp60 = new long[9];
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      sp60[charIndex] = _800fbd54.get(charIndex).get();
    }

    FUN_8002a6fc();

    //LAB_80110174
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);

      final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);

      stats.xp_00.set(charData.xp_00.get());
      stats.hp_04.set(charData.hp_08.get());
      stats.mp_06.set(charData.mp_0a.get());
      stats.sp_08.set(charData.sp_0c.get());
      stats._0a.set(charData._0e.get());
      stats._0c.set(charData._10.get());
      stats.level_0e.set(charData.level_12.get());
      stats.dlevel_0f.set(charData.dlevel_13.get());

      //LAB_801101e4
      for(int i = 0; i < 5; i++) {
        stats.equipment_30.get(i).set(charData.equipment_14.get(i).get());
      }

      stats.selectedAddition_35.set(charData.selectedAddition_19.get());

      //LAB_80110220
      for(int i = 0; i < 8; i++) {
        stats.additionLevels_36.get(i).set(charData.additionLevels_1a.get(i).get());
        stats.additionXp_3e.get(i).set(charData.additionXp_22.get(i).get());
      }

      v0 = sp38[charIndex] + stats.level_0e.get() * 0x8L;
      stats.maxHp_66.set((int)MEMORY.ref(2, v0).offset(0x0L).get());
      stats._68.set((int)MEMORY.ref(1, v0).offset(0x2L).get());
      stats.bodySpeed_69.set((int)MEMORY.ref(1, v0).offset(0x3L).get());
      stats.bodyAttack_6a.set((int)MEMORY.ref(1, v0).offset(0x4L).get());
      stats.bodyMagicAttack_6b.set((int)MEMORY.ref(1, v0).offset(0x5L).get());
      stats.bodyDefence_6c.set((int)MEMORY.ref(1, v0).offset(0x6L).get());
      stats.bodyMagicDefence_6d.set((int)MEMORY.ref(1, v0).offset(0x7L).get());

      v0 = sp60[charIndex] + stats.dlevel_0f.get() * 0x8L;
      stats.maxMp_6e.set((int)MEMORY.ref(2, v0).offset(0x0L).get());
      stats._70.set((int)MEMORY.ref(1, v0).offset(0x2L).get());
      stats._71.set((int)MEMORY.ref(1, v0).offset(0x3L).get());
      stats.dragoonAttack_72.set((int)MEMORY.ref(1, v0).offset(0x4L).get());
      stats.dragoonMagicAttack_73.set((int)MEMORY.ref(1, v0).offset(0x5L).get());
      stats.dragoonDefence_74.set((int)MEMORY.ref(1, v0).offset(0x6L).get());
      stats.dragoonMagicDefence_75.set((int)MEMORY.ref(1, v0).offset(0x7L).get());

      final int a2 = stats.selectedAddition_35.get();
      if(a2 != -1) {
        //TODO straighten this out
        a0 = ptrTable_80114070.offset(a2 * 0x4L).deref(4).offset(MEMORY.ref(1, stats.additionLevels_36.getAddress()).offset(a2 - additionOffsets_8004f5ac.get(charIndex).get()).get() * 0x4L).getAddress();

        stats._9c.set((int)MEMORY.ref(2, a0).offset(0x0L).get());
        stats._9e.set((int)MEMORY.ref(1, a0).offset(0x2L).get());
        stats._9f.set((int)MEMORY.ref(1, a0).offset(0x3L).get());
      }

      //LAB_8011042c
      FUN_8011085c(charIndex);

      v0 = sp10[charIndex];
      a0 = v0 & 0x1fL;
      v0 = v0 >>> 5;
      if((gameState_800babc8.dragoonSpirits_19c.get((int)v0).get() & 0x1L << a0) != 0) {
        stats._0c.or(0x2000);
        a0 = sp10[charIndex];

        if((gameState_800babc8._4e6.get() >> a0 & 1) == 0) {
          gameState_800babc8._4e6.or(1 << a0);

          v0 = sp60[charIndex] + stats.dlevel_0f.get() * 0x8L;
          stats.mp_06.set((int)MEMORY.ref(2, v0).get());
          stats.maxMp_6e.set((int)MEMORY.ref(2, v0).get());
        }
      } else {
        //LAB_801104ec
        stats.mp_06.set(0);
        stats.maxMp_6e.set(0);
        stats.dlevel_0f.set(0);
      }

      //LAB_801104f8
      if(charIndex == 0) {
        v0 = sp10[9];

        a0 = v0 & 0x1fL;
        v0 = v0 >>> 5;
        if((gameState_800babc8.dragoonSpirits_19c.get((int)v0).get() & 0x1L << a0) != 0) {
          stats._0c.or(0x6000);
          stats.dlevel_0f.set(gameState_800babc8.charData_32c.get(0).dlevel_13.get());

          a1 = sp10[0];

          if((gameState_800babc8._4e6.get() >> a1 & 1) == 0) {
            gameState_800babc8._4e6.or(1 << a1);
            v1 = sp60[charIndex] + stats.dlevel_0f.get() * 0x8L;
            stats.mp_06.set((int)MEMORY.ref(2, v1).get());
            stats.maxMp_6e.set((int)MEMORY.ref(2, v1).get());
          } else {
            //LAB_80110590
            v1 = sp60[charIndex] + stats.dlevel_0f.get() * 0x8L;
            stats.mp_06.set(gameState_800babc8.charData_32c.get(0).mp_0a.get());
            stats.maxMp_6e.set((int)MEMORY.ref(2, v1).get());
          }
        }
      }

      //LAB_801105b0
      int maxHp = stats.maxHp_66.get() * (stats._62.get() / 100 + 1);

      //TODO remove HP cap
      if(maxHp >= 9999) {
        maxHp = 9999;
      }

      //LAB_801105f0
      stats.maxHp_66.set(maxHp);

      if(stats.hp_04.get() > maxHp) {
        stats.hp_04.set(maxHp);
      }

      //LAB_80110608
      final int maxMp = stats.maxMp_6e.get() * (stats._64.get() / 100 + 1);

      stats.maxMp_6e.set(maxMp);

      if(stats.mp_06.get() > maxMp) {
        stats.mp_06.set(maxMp);
      }

      //LAB_80110654
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
    FUN_8002a86c((int)a0);
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
