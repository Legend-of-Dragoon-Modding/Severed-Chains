package legend.game;

import legend.core.Tuple;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.combat.Bttl_800c;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.DR_TPAGE;
import legend.game.types.DabasData100;
import legend.game.types.EquipmentStats1c;
import legend.game.types.InventoryMenuState;
import legend.game.types.ItemStats0c;
import legend.game.types.LevelStuff08;
import legend.game.types.LodString;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuAdditionInfo;
import legend.game.types.MenuGlyph06;
import legend.game.types.MenuItemStruct04;
import legend.game.types.MenuStruct08;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;
import legend.game.types.MrgFile;
import legend.game.types.Renderable58;
import legend.game.types.SavedGameDisplayData;
import legend.game.types.ScriptState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static legend.core.Hardware.MEMORY;
import static legend.core.MathHelper.roundUp;
import static legend.core.MemoryHelper.getBiFunctionAddress;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.SMap._800cb450;
import static legend.game.SMap.shops_800f4930;
import static legend.game.Scus94491BpeSegment.FUN_800127cc;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_80018e84;
import static legend.game.Scus94491BpeSegment.FUN_800192d8;
import static legend.game.Scus94491BpeSegment.FUN_80019470;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.decompress;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.gpuPacketAddr_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadAndRunOverlay;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.qsort;
import static legend.game.Scus94491BpeSegment.queueGpuPacket;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setScriptDestructor;
import static legend.game.Scus94491BpeSegment.setScriptTicker;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022898;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022a94;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022afc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023544;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002379c;
import static legend.game.Scus94491BpeSegment_8002.FUN_800239e0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023a2c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a86c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a8f8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bcc8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002bda4;
import static legend.game.Scus94491BpeSegment_8002.addGold;
import static legend.game.Scus94491BpeSegment_8002.addHp;
import static legend.game.Scus94491BpeSegment_8002.addMp;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.clearCharacterStats;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getItemIcon;
import static legend.game.Scus94491BpeSegment_8002.getJoypadInputByPriority;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedSpellCount;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.intToStr;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.recalcInventory;
import static legend.game.Scus94491BpeSegment_8002.strcpy;
import static legend.game.Scus94491BpeSegment_8002.takeEquipment;
import static legend.game.Scus94491BpeSegment_8002.takeItem;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8002.useItemInMenu;
import static legend.game.Scus94491BpeSegment_8003.SetDrawTPage;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8004._8004dd30;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8004.loadingSmapOvl_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.setMonoOrStereo;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._8005a368;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_8005.combatants_8005e398;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.spells_80052734;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006f280;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.shopId_8007a3b4;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc910;
import static legend.game.Scus94491BpeSegment_800b._800bc928;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bc968;
import static legend.game.Scus94491BpeSegment_800b._800bc978;
import static legend.game.Scus94491BpeSegment_800b._800bc97c;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bdb9c;
import static legend.game.Scus94491BpeSegment_800b._800bdba0;
import static legend.game.Scus94491BpeSegment_800b._800bdc2c;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bdc40;
import static legend.game.Scus94491BpeSegment_800b._800bdf00;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.confirmDest_800bdc30;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.equipmentStats_800be5d8;
import static legend.game.Scus94491BpeSegment_800b.gameOverMcq_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b.inventoryJoypadInput_800bdc44;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbe8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbec;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdbf0;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc20;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIndices_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe0;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe4;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.combat.Bttl_800c.FUN_800ca75c;
import static legend.game.combat.Bttl_800c._800c66d0;
import static legend.game.combat.Bttl_800c.addCombatant;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.combatantCount_800c66a0;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.loadCombatantTmdAndAnims;
import static legend.game.combat.Bttl_800f.FUN_800f863c;

public final class SItem {
  private SItem() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SItem.class);

  public static final Value _800fba58 = MEMORY.ref(4, 0x800fba58L);

  public static final ArrayRef<UnsignedByteRef> additionXpPerLevel_800fba2c = MEMORY.ref(1, 0x800fba2cL, ArrayRef.of(UnsignedByteRef.class, 5, 1, UnsignedByteRef::new));

  public static final ArrayRef<MenuStruct08> _800fba7c = MEMORY.ref(4, 0x800fba7cL, ArrayRef.of(MenuStruct08.class, 8, 8, MenuStruct08::new));

  public static final Value _800fbabc = MEMORY.ref(4, 0x800fbabcL);

  public static final Value _800fbb44 = MEMORY.ref(1, 0x800fbb44L);

  public static final Value _800fbbf0 = MEMORY.ref(4, 0x800fbbf0L);

  public static final Value _800fbc88 = MEMORY.ref(2, 0x800fbc88L);

  public static final Value _800fbc9c = MEMORY.ref(1, 0x800fbc9cL);

  public static final Value _800fbca8 = MEMORY.ref(1, 0x800fbca8L);

  public static final ArrayRef<UnsignedIntRef> _800fbd08 = MEMORY.ref(4, 0x800fbd08L, ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));
  public static final ArrayRef<Pointer<ArrayRef<LevelStuff08>>> levelStuff_800fbd30 = MEMORY.ref(4, 0x800fbd30L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(LevelStuff08.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(LevelStuff08.class, 61, 8, LevelStuff08::new))));
  public static final ArrayRef<Pointer<ArrayRef<MagicStuff08>>> magicStuff_800fbd54 = MEMORY.ref(4, 0x800fbd54L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(MagicStuff08.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(MagicStuff08.class, 6, 8, MagicStuff08::new))));

  public static final ArrayRef<Pointer<ArrayRef<LevelStuff08>>> levelStuff_80111cfc = MEMORY.ref(4, 0x80111cfcL, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(LevelStuff08.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(LevelStuff08.class, 61, 8, LevelStuff08::new))));
  public static final ArrayRef<Pointer<ArrayRef<MagicStuff08>>> magicStuff_80111d20 = MEMORY.ref(4, 0x80111d20L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(MagicStuff08.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(MagicStuff08.class, 6, 8, MagicStuff08::new))));

  public static final Value _80111d38 = MEMORY.ref(4, 0x80111d38L);

  /** Contains data for every combination of party members (like a DRGN0 file index that contains the textures and models of each char */
  public static final Value partyPermutations_80111d68 = MEMORY.ref(1, 0x80111d68L);

  public static final ArrayRef<EquipmentStats1c> equipmentStats_80111ff0 = MEMORY.ref(1, 0x80111ff0L, ArrayRef.of(EquipmentStats1c.class, 0xc0, 0x1c, EquipmentStats1c::new));
  public static final ArrayRef<IntRef> kongolXpTable_801134f0 = MEMORY.ref(4, 0x801134f0L, ArrayRef.of(IntRef.class, 61, 4, IntRef::new));
  public static final ArrayRef<IntRef> dartXpTable_801135e4 = MEMORY.ref(4, 0x801135e4L, ArrayRef.of(IntRef.class, 61, 4, IntRef::new));
  public static final ArrayRef<IntRef> haschelXpTable_801136d8 = MEMORY.ref(4, 0x801136d8L, ArrayRef.of(IntRef.class, 61, 4, IntRef::new));
  public static final ArrayRef<IntRef> meruXpTable_801137cc = MEMORY.ref(4, 0x801137ccL, ArrayRef.of(IntRef.class, 61, 4, IntRef::new));
  public static final ArrayRef<IntRef> lavitzXpTable_801138c0 = MEMORY.ref(4, 0x801138c0L, ArrayRef.of(IntRef.class, 61, 4, IntRef::new));
  public static final ArrayRef<IntRef> roseXpTable_801139b4 = MEMORY.ref(4, 0x801139b4L, ArrayRef.of(IntRef.class, 61, 4, IntRef::new));
  public static final ArrayRef<IntRef> shanaXpTable_80113aa8 = MEMORY.ref(4, 0x80113aa8L, ArrayRef.of(IntRef.class, 61, 4, IntRef::new));

  public static final Value ptrTable_80114070 = MEMORY.ref(4, 0x80114070L);

  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114130 = MEMORY.ref(1, 0x80114130L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114160 = MEMORY.ref(1, 0x80114160L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114180 = MEMORY.ref(1, 0x80114180L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_801141a4 = MEMORY.ref(1, 0x801141a4L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_801141c4 = MEMORY.ref(1, 0x801141c4L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_801141e4 = MEMORY.ref(1, 0x801141e4L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_801141fc = MEMORY.ref(1, 0x801141fcL, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> dabasMenuGlyphs_80114228 = MEMORY.ref(1, 0x80114228L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114258 = MEMORY.ref(1, 0x80114258L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));

  public static final Value _80114284 = MEMORY.ref(1, 0x80114284L);

  public static final Value _80114290 = MEMORY.ref(1, 0x80114290L);

  public static final MenuGlyph06 glyph_801142d4 = MEMORY.ref(1, 0x801142d4L, MenuGlyph06::new);

  public static final ArrayRef<Pointer<LodString>> chapterNames_80114248 = MEMORY.ref(4, 0x80114248L, ArrayRef.of(Pointer.classFor(LodString.class), 4, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> characterNames_801142dc = MEMORY.ref(4, 0x801142dcL, ArrayRef.of(Pointer.classFor(LodString.class), 9, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<UnsignedShortRef> itemPrices_80114310 = MEMORY.ref(2, 0x80114310L, ArrayRef.of(UnsignedShortRef.class, 0x100, 2, UnsignedShortRef::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114510 = MEMORY.ref(1, 0x80114510L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114548 = MEMORY.ref(1, 0x80114548L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));

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
  public static final LodString Too_many_8011c21c = MEMORY.ref(2, 0x8011c21cL, LodString::new);
  public static final LodString items_8011c230 = MEMORY.ref(2, 0x8011c230L, LodString::new);
  public static final LodString Replace_8011c240 = MEMORY.ref(2, 0x8011c240L, LodString::new);
  public static final LodString Petrify_8011c258 = MEMORY.ref(2, 0x8011c258L, LodString::new);
  /** Spelling mistake included */
  public static final LodString To_many_items_8011c268 = MEMORY.ref(2, 0x8011c268L, LodString::new);
  public static final LodString Discard_8011c288 = MEMORY.ref(2, 0x8011c288L, LodString::new);
  public static final LodString End_8011c29c = MEMORY.ref(2, 0x8011c29cL, LodString::new);
  public static final LodString This_item_cannot_be_thrown_away_8011c2a8 = MEMORY.ref(2, 0x8011c2a8L, LodString::new);
  public static final LodString Acquired_item_8011c2f8 = MEMORY.ref(2, 0x8011c2f8L, LodString::new);
  public static final LodString _8011c314 = MEMORY.ref(2, 0x8011c314L, LodString::new);
  public static final LodString _8011c32c = MEMORY.ref(2, 0x8011c32cL, LodString::new);
  public static final LodString _8011c340 = MEMORY.ref(2, 0x8011c340L, LodString::new);
  /** "Do you want to save now?" */
  public static final LodString Do_you_want_to_save_now_8011c370 = MEMORY.ref(2, 0x8011c370L, LodString::new);
  /** "Confused" */
  public static final LodString Confused_8011c3bc = MEMORY.ref(2, 0x8011c3bcL, LodString::new);
  public static final LodString Are_you_sure_you_want_to_buy_8011c3ec = MEMORY.ref(2, 0x8011c3ecL, LodString::new);
  public static final LodString Cannot_carry_anymore_8011c43c = MEMORY.ref(2, 0x8011c43cL, LodString::new);
  public static final LodString Not_enough_money_8011c468 = MEMORY.ref(2, 0x8011c468L, LodString::new);
  /** "Conf." */
  public static final LodString Conf_8011c48c = MEMORY.ref(2, 0x8011c48cL, LodString::new);
  public static final LodString What_do_you_want_to_sell_8011c498 = MEMORY.ref(2, 0x8011c498L, LodString::new);
  public static final LodString Armed_8011c4cc = MEMORY.ref(2, 0x8011c4ccL, LodString::new);
  public static final LodString item_8011c4d8 = MEMORY.ref(2, 0x8011c4d8L, LodString::new);
  public static final LodString Which_item_do_you_want_to_sell_8011c4e4 = MEMORY.ref(2, 0x8011c4e4L, LodString::new);
  public static final LodString Which_weapon_do_you_want_to_sell_8011c524 = MEMORY.ref(2, 0x8011c524L, LodString::new);
  public static final LodString Are_you_sure_you_want_to_sell_8011c568 = MEMORY.ref(2, 0x8011c568L, LodString::new);
  /** "New Addition" */
  public static final LodString New_Addition_8011c5a8 = MEMORY.ref(2, 0x8011c5a8L, LodString::new);
  public static final LodString Spell_Unlocked_8011c5c4 = MEMORY.ref(2, 0x8011c5c4L, LodString::new);
  public static final LodString No_item_to_sell_8011c5dc = MEMORY.ref(2, 0x8011c5dcL, LodString::new);
  public static final LodString No_weapon_to_sell_8011c5fc = MEMORY.ref(2, 0x8011c5fcL, LodString::new);
  public static final LodString Do_you_want_to_be_armed_with_it_8011c620 = MEMORY.ref(2, 0x8011c620L, LodString::new);
  public static final LodString Is_armed_8011c670 = MEMORY.ref(2, 0x8011c670L, LodString::new);
  public static final LodString Put_in_the_bag_8011c684 = MEMORY.ref(2, 0x8011c684L, LodString::new);

  public static final LodString Buy_8011c6a4 = MEMORY.ref(2, 0x8011c6a4L, LodString::new);
  public static final LodString Sell_8011c6ac = MEMORY.ref(2, 0x8011c6acL, LodString::new);
  public static final LodString Carried_8011c6b8 = MEMORY.ref(2, 0x8011c6b8L, LodString::new);
  public static final LodString Leave_8011c6c8 = MEMORY.ref(2, 0x8011c6c8L, LodString::new);
  public static final LodString Cannot_be_armed_with_8011c6d4 = MEMORY.ref(2, 0x8011c6d4L, LodString::new);

  public static final LodString Number_kept_8011c7f4 = MEMORY.ref(2, 0x8011c7f4L, LodString::new);
  public static final LodString Note_8011c814 = MEMORY.ref(2, 0x8011c814L, LodString::new);
  public static final LodString Stay_8011c820 = MEMORY.ref(2, 0x8011c820L, LodString::new);
  public static final LodString Half_8011c82c = MEMORY.ref(2, 0x8011c82cL, LodString::new);
  public static final LodString Off_8011c838 = MEMORY.ref(2, 0x8011c838L, LodString::new);
  /**
   * "Really want"
   * "to throw"
   * "this away?"
   */
  public static final LodString Really_want_to_throw_this_away_8011c8d4 = MEMORY.ref(2, 0x8011c8d4L, LodString::new);
  /** "Save new game?" */
  public static final LodString Save_new_game_8011c9c8 = MEMORY.ref(2, 0x8011c9c8L, LodString::new);
  /** "Overwrite save?" */
  public static final LodString Overwrite_save_8011c9e8 = MEMORY.ref(2, 0x8011c9e8L, LodString::new);
  /** "Load this data?" */
  public static final LodString Load_this_data_8011ca08 = MEMORY.ref(2, 0x8011ca08L, LodString::new);
  /** "Saved" */
  public static final LodString Saved_8011cb2c = MEMORY.ref(2, 0x8011cb2cL, LodString::new);
  public static final LodString AcquiredGold_8011cdd4 = new LodString("Acquired Gold");
  /** "" */
  public static final LodString _8011ce10 = MEMORY.ref(2, 0x8011ce10L, LodString::new);
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
  public static final LodString DigDabas_8011d04c = new LodString("Diiig Dabas!");
  public static final LodString AcquiredItems_8011d050 = new LodString("Acquired Items");
  public static final LodString SpecialItem_8011d054 = new LodString("Special Item");
  public static final LodString Take_8011d058 = new LodString("Take");
  public static final LodString Discard_8011d05c = new LodString("Discard");
  public static final LodString NextDig_8011d064 = new LodString("Next Dig");
  public static final LodString _8011d534 = MEMORY.ref(2, 0x8011d534L, LodString::new);
  public static final LodString _8011d560 = MEMORY.ref(2, 0x8011d560L, LodString::new);
  public static final LodString _8011d57c = MEMORY.ref(2, 0x8011d57cL, LodString::new);
  public static final LodString _8011d584 = MEMORY.ref(2, 0x8011d584L, LodString::new);
  public static final LodString _8011d58c = MEMORY.ref(2, 0x8011d58cL, LodString::new);
  public static final LodString _8011d594 = MEMORY.ref(2, 0x8011d594L, LodString::new);
  public static final LodString _8011d5c8 = MEMORY.ref(2, 0x8011d5c8L, LodString::new);
  public static final LodString _8011d5e0 = MEMORY.ref(2, 0x8011d5e0L, LodString::new);
  public static final LodString _8011d604 = MEMORY.ref(2, 0x8011d604L, LodString::new);
  public static final LodString _8011d618 = MEMORY.ref(2, 0x8011d618L, LodString::new);

  public static final ArrayRef<Pointer<Renderable58>> _8011d718 = MEMORY.ref(4, 0x8011d718L, ArrayRef.of(Pointer.classFor(Renderable58.class), 7, 4, Pointer.deferred(4, Renderable58::new)));

  public static final IntRef charSlot_8011d734 = MEMORY.ref(4, 0x8011d734L, IntRef::new);
  public static final IntRef selectedMenuOption_8011d738 = MEMORY.ref(4, 0x8011d738L, IntRef::new);
  public static final IntRef selectedItemSubmenuOption_8011d73c = MEMORY.ref(4, 0x8011d73cL, IntRef::new);
  public static final IntRef selectedSlot_8011d740 = MEMORY.ref(4, 0x8011d740L, IntRef::new);
  /** The first save game displayed on the menu, increments as you scroll down */
  public static final IntRef slotScroll_8011d744 = MEMORY.ref(4, 0x8011d744L, IntRef::new);
  public static final IntRef slotScroll_8011d748 = MEMORY.ref(4, 0x8011d748L, IntRef::new);
  public static final IntRef menuIndex_8011d74c = MEMORY.ref(4, 0x8011d74cL, IntRef::new);
  public static final IntRef count_8011d750 = MEMORY.ref(4, 0x8011d750L, IntRef::new);
  public static final Value _8011d754 = MEMORY.ref(4, 0x8011d754L);
  /**
   * <ol start="0">
   *   <li>Send</li>
   *   <li>Discard</li>
   *   <li>New Dig</li>
   *   <li>Nothing?</li>
   * </ol>
   */
  public static final Value dabasState_8011d758 = MEMORY.ref(4, 0x8011d758L);

  public static final Value saveCount_8011d768 = MEMORY.ref(4, 0x8011d768L);

  public static final Value _8011d788 = MEMORY.ref(1, 0x8011d788L);

  public static final Value _8011d78c = MEMORY.ref(4, 0x8011d78cL);
  public static final LodString _8011d790 = MEMORY.ref(2, 0x8011d790L, LodString::new);

  public static final UnsignedIntRef _8011d7b8 = MEMORY.ref(4, 0x8011d7b8L, UnsignedIntRef::new);
  public static final UnsignedIntRef _8011d7bc = MEMORY.ref(4, 0x8011d7bcL, UnsignedIntRef::new);
  public static final Pointer<DabasData100> dabasData_8011d7c0 = MEMORY.ref(4, 0x8011d7c0L, Pointer.deferred(4, DabasData100::new));

  public static final UnsignedByteRef characterCount_8011d7c4 = MEMORY.ref(1, 0x8011d7c4L, UnsignedByteRef::new);

  public static final ArrayRef<MenuItemStruct04> menuItems_8011d7c8 = MEMORY.ref(1, 0x8011d7c8L, ArrayRef.of(MenuItemStruct04.class, 0x100, 0x4, MenuItemStruct04::new));

  public static final Value canSave_8011dc88 = MEMORY.ref(1, 0x8011dc88L);

  public static final Value _8011dc8c = MEMORY.ref(4, 0x8011dc8cL);
  public static final MessageBox20 messageBox_8011dc90 = MEMORY.ref(4, 0x8011dc90L, MessageBox20::new);

  public static final Value _8011dca8 = MEMORY.ref(4, 0x8011dca8L);

  public static final ArrayRef<Pointer<ArrayRef<MenuItemStruct04>>> _8011dcb8 = MEMORY.ref(4, 0x8011dcb8L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(MenuItemStruct04.class)), 2, 4, Pointer.deferred(4, ArrayRef.of(MenuItemStruct04.class, 0x130, 0x4, MenuItemStruct04::new))));
  public static final Value tempSaveData_8011dcc0 = MEMORY.ref(4, 0x8011dcc0L);

  public static final BoolRef _8011dcfc = MEMORY.ref(1, 0x8011dcfcL, BoolRef::new);

  /** DRGN0 file 6668 */
  public static final Value dabasFilePtr_8011dd00 = MEMORY.ref(4, 0x8011dd00L);
  public static final Value dabasFileSize_8011dd04 = MEMORY.ref(4, 0x8011dd04L);
  public static final IntRef dabasGold_8011dd08 = MEMORY.ref(4, 0x8011dd08L, IntRef::new);
  public static final Value dabasHasItems_8011dd0c = MEMORY.ref(4, 0x8011dd0cL);

  public static final Value _8011e094 = MEMORY.ref(4, 0x8011e094L);
  public static final ArrayRef<MenuAdditionInfo> additions_8011e098 = MEMORY.ref(1, 0x8011e098L, ArrayRef.of(MenuAdditionInfo.class, 9, 0x2, MenuAdditionInfo::new));

  public static final IntRef menuIndex_8011e0d8 = MEMORY.ref(4, 0x8011e0d8L, IntRef::new);
  public static final IntRef menuIndex_8011e0dc = MEMORY.ref(4, 0x8011e0dcL, IntRef::new);
  public static final IntRef menuIndex_8011e0e0 = MEMORY.ref(4, 0x8011e0e0L, IntRef::new);
  public static final IntRef menuScroll_8011e0e4 = MEMORY.ref(4, 0x8011e0e4L, IntRef::new);
  public static final IntRef menuOption_8011e0e8 = MEMORY.ref(4, 0x8011e0e8L, IntRef::new);
  public static final IntRef menuOption_8011e0ec = MEMORY.ref(4, 0x8011e0ecL, IntRef::new);
  public static final Pointer<Renderable58> renderable_8011e0f0 = MEMORY.ref(4, 0x8011e0f0L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderable_8011e0f4 = MEMORY.ref(4, 0x8011e0f4L, Pointer.deferred(4, Renderable58::new));
  public static final UnboundedArrayRef<MenuItemStruct04> menuItems_8011e0f8 = MEMORY.ref(4, 0x8011e0f8L, UnboundedArrayRef.of(0x4, MenuItemStruct04::new));

  public static final UnsignedByteRef currentShopItemCount_8011e13c = MEMORY.ref(1, 0x8011e13cL, UnsignedByteRef::new);
  /**
   * <ul>
   *   <li>0x0 - Item Shop</li>
   *   <li>0x1 - Weapon Shop</li>
   * </ul>
  */
  public static final Value shopType_8011e13d = MEMORY.ref(1, 0x8011e13dL);
  public static final Value _8011e13e = MEMORY.ref(1, 0x8011e13eL);

  public static final ArrayRef<Pointer<Renderable58>> characterRenderables_8011e148 = MEMORY.ref(4, 0x8011e148L, ArrayRef.of(Pointer.classFor(Renderable58.class), 9, 4, Pointer.deferred(4, Renderable58::new)));

  public static final Value _8011e170 = MEMORY.ref(1, 0x8011e170L);

  public static final UnsignedByteRef xpDivisor_8011e174 = MEMORY.ref(1, 0x8011e174L, UnsignedByteRef::new);

  public static final Value _8011e178 = MEMORY.ref(4, 0x8011e178L);
  public static final Value soundTick_8011e17c = MEMORY.ref(4, 0x8011e17cL);
  public static final ArrayRef<IntRef> pendingXp_8011e180 = MEMORY.ref(4, 0x8011e180L, ArrayRef.of(IntRef.class, 10, 4, IntRef::new));

  public static final ArrayRef<UnsignedByteRef> spellsUnlocked_8011e1a8 = MEMORY.ref(1, 0x8011e1a8L, ArrayRef.of(UnsignedByteRef.class, 10, 1, UnsignedByteRef::new));

  public static final ArrayRef<UnsignedByteRef> additionsUnlocked_8011e1b8 = MEMORY.ref(1, 0x8011e1b8L, ArrayRef.of(UnsignedByteRef.class, 10, 1, UnsignedByteRef::new));

  public static final Value _8011e1c8 = MEMORY.ref(1, 0x8011e1c8L);

  public static final Value _8011e1d8 = MEMORY.ref(1, 0x8011e1d8L);

  public static final EnumRef<MessageBoxResult> msgboxResult_8011e1e8 = MEMORY.ref(4, 0x8011e1e8L, EnumRef.of(MessageBoxResult.values()));

  public static final IntRef menuIndex_8011e1f0 = MEMORY.ref(4, 0x8011e1f0L, IntRef::new);
  public static final IntRef slotIndex_8011e1f4 = MEMORY.ref(4, 0x8011e1f4L, IntRef::new);
  public static final IntRef slotScroll_8011e1f8 = MEMORY.ref(4, 0x8011e1f8L, IntRef::new);
  public static final IntRef menuIndex_8011e1fc = MEMORY.ref(4, 0x8011e1fcL, IntRef::new);

  public static final Pointer<Renderable58> renderable_8011e200 = MEMORY.ref(4, 0x8011e200L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderable_8011e204 = MEMORY.ref(4, 0x8011e204L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderable_8011e208 = MEMORY.ref(4, 0x8011e208L, Pointer.deferred(4, Renderable58::new));

  private static final List<Tuple<String, SavedGameDisplayData>> saves = new ArrayList<>();

  @Method(0x800fbd78L)
  public static void allocatePlayerBattleObjects(final long a0) {
    //LAB_800fbdb8
    for(charCount_800c677c.set(0); charCount_800c677c.get() < 3; charCount_800c677c.incr()) {
      if(gameState_800babc8.charIndex_88.get(charCount_800c677c.get()).get() < 0) {
        break;
      }
    }

    //LAB_800fbde8
    final long fp = _80111d38.offset(charCount_800c677c.get() * 0xcL).getAddress();
    final long[] charIndices = new long[(int)charCount_800c677c.get()];

    //LAB_800fbe18
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      charIndices[charSlot] = addCombatant(0x200L + gameState_800babc8.charIndex_88.get(charSlot).get() * 0x2L, charSlot);
    }

    //LAB_800fbe4c
    //LAB_800fbe70
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charIndex = gameState_800babc8.charIndex_88.get(charSlot).get();
      final int bobjIndex = allocateScriptState(charSlot + 6, 0x27cL, false, null, 0, BattleObject27c::new);
      setScriptTicker(bobjIndex, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cae50", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
      setScriptDestructor(bobjIndex, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb058", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
      _8006e398.bobjIndices_e0c.get(_800c66d0.get()).set(bobjIndex);
      _8006e398.charBobjIndices_e40.get(charSlot).set(bobjIndex);
      final BattleObject27c s1 = scriptStatePtrArr_800bc1c0.get(bobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
      s1.magic_00.set(BattleScriptDataBase.BOBJ);
      s1.combatant_144.set(getCombatant((short)charIndices[charSlot]));
      s1.charIndex_272.set((short)charIndex);
      s1.charSlot_276.set((short)charSlot);
      s1.combatantIndex_26c.set((short)charIndices[charSlot]);
      s1._274.set((short)_800c66d0.get());
      s1.model_148.coord2_14.coord.transfer.setX((int)MEMORY.ref(2, fp).offset(charSlot * 0x4L).offset(0x0L).getSigned());
      s1.model_148.coord2_14.coord.transfer.setY(0);
      s1.model_148.coord2_14.coord.transfer.setZ((int)MEMORY.ref(2, fp).offset(charSlot * 0x4L).offset(0x2L).getSigned());
      s1.model_148.coord2Param_64.rotate.set((short)0, (short)0x400, (short)0);
      _800c66d0.incr();
    }

    //LAB_800fbf6c
    _8006e398.bobjIndices_e0c.get(_800c66d0.get()).set(-1);
    _8006e398.charBobjIndices_e40.get(charCount_800c677c.get()).set(-1);

    FUN_800f863c();
    FUN_80012bb4();
  }

  @Method(0x800fbfe0L)
  public static void loadEncounterAssets(final long param) {
    loadAndRunOverlay(2, getMethodAddress(SItem.class, "loadEnemyTextures", long.class), 2625 + encounterId_800bb0f8.get());

    //LAB_800fc030
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      if(getCombatant(i).charSlot_19c.get() < 0) { // I think this means it's not a player
        loadCombatantTmdAndAnims(i);
      }

      //LAB_800fc050
    }

    //LAB_800fc064
    //LAB_800fc09c
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      combatants_8005e398.get(scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(i).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).combatantIndex_26c.get()).flags_19e.or(0x2a);
    }

    //LAB_800fc104
    final long v1 = charCount_800c677c.get();
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
    loadAndRunOverlay(2, getMethodAddress(SItem.class, "FUN_800fc504", long.class), MEMORY.ref(2, s2).getSigned());
    loadAndRunOverlay(2, getMethodAddress(SItem.class, "FUN_800fc654", long.class), MEMORY.ref(2, s2).getSigned() + 0x1L);
    _800bc960.oru(0x400L);
    FUN_80012bb4();
  }

  @Method(0x800fc210L)
  public static void FUN_800fc210(final long address, final long fileSize, final long param) {
    final long s3 = _8006e398.ptr_ee8.get();

    //LAB_800fc260
    long s0 = 0; //TODO this was uninitialized
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final BattleObject27c data = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
      final CombatantStruct1a8 combatant = data.combatant_144.deref();

      //LAB_800fc298
      for(int a1 = 0; a1 < 3; a1++) {
        if(MEMORY.ref(2, s3).offset(a1 * 0x2L).offset(0x2L).getSigned() == data.charIndex_272.get()) {
          s0 = s0 & 0xffff_ff80L;
          s0 = s0 | combatant.charSlot_19c.get() & 0x7fL;
          s0 = s0 & 0xffff_81ffL;
          s0 = s0 | (data.combatantIndex_26c.get() & 0x3fL) << 9;
          s0 = s0 & 0xffff_ff7fL;
          s0 = s0 & 0xffff_feffL;
          decompress(address + MEMORY.ref(4, address).offset(a1 * 0x8L).offset(0x8L).get(), _1f8003f4.deref()._9cdc.offset(combatant.charSlot_19c.get() * 0x4L).get(), getMethodAddress(Bttl_800c.class, "combatantTmdAndAnimLoadedCallback", long.class, long.class, long.class), s0, 0);
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

  @Method(0x800fc3a0L)
  public static void FUN_800fc3a0(final long a0) {
    FUN_80012bb4();
  }

  @Method(0x800fc3c0L)
  public static void loadEnemyTextures(final long fileIndex) {
    loadDrgnBinFile(0, (int)fileIndex, 0, getMethodAddress(SItem.class, "enemyTexturesLoadedCallback", long.class, long.class, long.class), 0, 0x5L);
  }

  @Method(0x800fc404L)
  public static void enemyTexturesLoadedCallback(final long address, final long fileSize, final long param) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    final long s2 = _1f8003f4.getPointer(); //TODO

    //LAB_800fc434
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 a0 = getCombatant(i);

      if(a0.charSlot_19c.get() < 0) {
        final long a2 = a0.charIndex_1a2.get() & 0x1ffL;

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
    loadDrgnBinFile(0, (int)fileIndex, 0, getMethodAddress(SItem.class, "FUN_800fc548", long.class, long.class, long.class), 0, 0x5L);
  }

  @Method(0x800fc548L)
  public static void FUN_800fc548(final long address, final long fileSize, final long param) {
    final long s3 = _8006e398.ptr_ee8.get();

    //LAB_800fc590
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

      //LAB_800fc5b4
      for(long a1 = 0; a1 < 3; a1++) {
        if(MEMORY.ref(2, s3).offset(a1 * 0x2L).offset(0x2L).getSigned() == bobj.charIndex_272.get()) {
          a1 = address + MEMORY.ref(4, address).offset(a1 * 0x8L).offset(0x8L).get();
          a1 = a1 + MEMORY.ref(4, a1).offset(0x8L).get();
          FUN_800ca75c(bobj.combatantIndex_26c.get(), a1);
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
    loadDrgnBinFile(0, (int)fileIndex, 0, getMethodAddress(SItem.class, "FUN_800fc210", long.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x800fc698L)
  public static int getXpToNextLevel(final int charIndex) {
    if(charIndex == -1 || charIndex > 8) {
      //LAB_800fc6a4
      throw new RuntimeException("Character index " + charIndex + " out of bounds");
    }

    //LAB_800fc6ac
    final int level = gameState_800babc8.charData_32c.get(charIndex).level_12.get();

    if(level >= 60) {
      return 0; // Max level
    }

    final ArrayRef<IntRef> table = switch(charIndex) {
      case 0    -> dartXpTable_801135e4;
      case 1, 5 -> lavitzXpTable_801138c0;
      case 2, 8 -> shanaXpTable_80113aa8;
      case 3    -> roseXpTable_801139b4;
      case 4    -> haschelXpTable_801136d8;
      case 6    -> meruXpTable_801137cc;
      case 7    -> kongolXpTable_801134f0;
      default -> throw new RuntimeException("Impossible");
    };

    //LAB_800fc70c
    return table.get(level + 1).get();
  }

  @Method(0x800fc78cL)
  public static int getMenuOptionY(final int option) {
    return 78 + option * 13;
  }

  @Method(0x800fc7a4L)
  public static int getItemSubmenuOptionY(final int option) {
    return 80 + option * 13;
  }

  @Method(0x800fc7bcL)
  public static int FUN_800fc7bc(final int a0) {
    return 130 + a0 * 56;
  }

  @Method(0x800fc7d0L)
  public static int FUN_800fc7d0(final int a0) {
    return 130 + a0 * 46;
  }

  @Method(0x800fc7ecL)
  public static int menuOptionY(final int a0) {
    return 107 + a0 * 13;
  }

  @Method(0x800fc804L)
  public static int FUN_800fc804(final int a0) {
    return 99 + a0 * 17;
  }

  @Method(0x800fc814L)
  public static int FUN_800fc814(final int a0) {
    return 9 + a0 * 17;
  }

  @Method(0x800fc824L)
  public static int FUN_800fc824(final int a0) {
    if(a0 == 0) {
      return 43;
    }

    return 221;
  }

  @Method(0x800fc838L)
  public static int getAdditionSlotY(final int a0) {
    return 113 + a0 * 14;
  }

  @Method(0x800fc84cL)
  public static int getSlotY(final int slot) {
    return 16 + slot * 72;
  }

  @Method(0x800fc860L)
  public static int FUN_800fc860(final int a0) {
    return 180 + a0 * 17;
  }

  @Method(0x800fc880L)
  public static int FUN_800fc880(int a0) {
    if(a0 >= 3) {
      a0 -= 3;
    }

    //LAB_800fc890
    return 198 + a0 * 57;
  }

  @Method(0x800fc8a8L)
  public static int FUN_800fc8a8(final int a0) {
    //LAB_800fc8b8
    return a0 >= 3 ? 122 : 16;
  }

  @Method(0x800fc8c0L)
  public static int FUN_800fc8c0(final int a0) {
    return 21 + a0 * 50;
  }

  @Method(0x800fc8dcL)
  public static int FUN_800fc8dc(final int a0) {
    return 18 + a0 * 17;
  }

  @Method(0x800fc8ecL)
  public static int getDabasMenuY(final int a0) {
    return 57 + a0 * 14;
  }

  @Method(0x800fc900L)
  public static Renderable58 FUN_800fc900(final int option) {
    final Renderable58 renderable = allocateUiElement(116, 116, 122, getItemSubmenuOptionY(option) - 2);
    FUN_80104b60(renderable);
    return renderable;
  }

  /** TODO also used for file 6668 */
  @Method(0x800fc944L)
  public static void menuAssetsLoaded(final long address, final long size, final long param) {
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
    } else if(param == 0x4L) { // Dabas minigame assets
      //LAB_800fc978
      //LAB_800fc9f0
      dabasFilePtr_8011dd00.setu(address);
      dabasFileSize_8011dd04.setu(size);
    }

    //LAB_800fc9fc
  }

  @Method(0x800fca0cL)
  public static void FUN_800fca0c(final InventoryMenuState nextMenuState, final long a1) {
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
    inventoryMenuState_800bdc28.set(InventoryMenuState._123);
    _800bdc2c.setu(a1);
    confirmDest_800bdc30.set(nextMenuState);
  }

  @Method(0x800fcad4L)
  public static void FUN_800fcad4() {
    long v0;
    long v1;
    final long a0;
    final long a2;
    final long t0;
    final long s0;
    final long s1;

    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    LOGGER.info("Inventory menu state: %s", inventoryMenuState_800bdc28.get());

    switch(inventoryMenuState_800bdc28.get()) {
      case INIT_0: // Initialize, loads some files (unknown contents)
        _800bdc34.setu(0);
        drgn0_6666FilePtr_800bdc3c.clear();
        renderablePtr_800bdc5c.clear();
        messageBox_8011dc90._0c.set(0);
        setWidthAndFlags(384, 0);
        s0 = getMethodAddress(SItem.class, "menuAssetsLoaded", long.class, long.class, long.class);
        loadDrgnBinFile(0, 6665, 0, s0, 0, 0x5L);
        loadDrgnBinFile(0, 6666, 0, s0, 1, 0x3L);
        loadCharacterStats(0);
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
        selectedMenuOption_8011d738.set(0);
        selectedItemSubmenuOption_8011d73c.set(0);
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        break;

      case AWAIT_INIT_1:
        if(!drgn0_6666FilePtr_800bdc3c.isNull()) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
          _8011dcfc.set((gameState_800babc8.dragoonSpirits_19c.get(1).get() & 0x4L) > 0);
          gameState_800babc8.vibrationEnabled_4e1.and(1);
        }
        break;

      case _2:
        deallocateRenderables(0xffL);

        v1 = whichMenu_800bdc38.get();

        if(v1 == 0xeL) { // Load game screen
          //LAB_800fccd4
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_LOAD_GAME_37);
          break;
        }

        if(v1 == 0x13L) {
          //LAB_800fccd0
          //LAB_800fccd4
          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_LOAD_GAME_37);
          break;
        }

        //LAB_800fccbc
        if(v1 == 0x18L) { // Character swap screen
          //LAB_800fcce0
          recalcInventory();
          FUN_80103b10();
          inventoryMenuState_800bdc28.set(InventoryMenuState._8);
          break;
        }

        //LAB_800fcd00
        recalcInventory();
        FUN_80103b10();
        scriptStartEffect(0x2L, 0xaL);
        inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_MAIN_MENU_3);
        break;

      case INIT_MAIN_MENU_3:
        renderGlyphs(glyphs_80114130, 0, 0);
        selectedMenuOptionRenderablePtr_800bdbe0.set(allocateUiElement(115, 115, 29, getMenuOptionY(selectedMenuOption_8011d738.get())));
        FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe0.deref());
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x4L, 0xffL);
        inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
        break;

      case MAIN_MENU_4: // Main inventory menu
        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          if((inventoryJoypadInput_800bdc44.get() & 0x40) != 0) { // Circle
            playSound(0x3L);
            FUN_800fca0c(InventoryMenuState._125, 0x1L);
          }

          //LAB_800fcdd4
          if(handleMenuUpDown(selectedMenuOption_8011d738, 7)) {
            selectedItemSubmenuOption_8011d73c.set(0);
            selectedMenuOptionRenderablePtr_800bdbe0.deref().y_44.set(getMenuOptionY(selectedMenuOption_8011d738.get()));
          }

          //LAB_800fce08
          v1 = inventoryJoypadInput_800bdc44.get();

          if((v1 & 0x2020L) != 0) { // Right or cross
            if((v1 & 0x2000L) != 0) { // Right
              v1 = selectedMenuOption_8011d738.get() + 10;
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
                FUN_800fca0c(InventoryMenuState.STATUS_INIT_20, 0x1L);
              }

              case 0x1, 0xb -> {
                playSound(0x4L);
                selectedMenuOptionRenderablePtr_800bdbe4.set(FUN_800fc900(selectedItemSubmenuOption_8011d73c.get()));
                inventoryMenuState_800bdc28.set(InventoryMenuState._5);
              }

              case 0x2 -> {
                playSound(0x2L);
                charSlot_8011d734.set(0);

                //LAB_800fcf3c
                FUN_800fca0c(InventoryMenuState._12, 0x1L);
              }

              case 0x3 -> {
                playSound(0x2L);
                charSlot_8011d734.set(0);

                //LAB_800fcf3c
                FUN_800fca0c(InventoryMenuState.ADDITIONS_INIT_23, 0x1L);
              }

              case 0x4 -> {
                playSound(0x2L);

                //LAB_800fcf3c
                FUN_800fca0c(InventoryMenuState._8, 0x1L);
              }

              case 0x5 -> {
                playSound(0x4L);
                selectedItemSubmenuOption_8011d73c.set(0);
                selectedMenuOptionRenderablePtr_800bdbe4.clear();
                setMessageBoxText(null, 0x1);
                inventoryMenuState_800bdc28.set(InventoryMenuState._6);
              }

              case 0x6 -> {
                if(canSave_8011dc88.get() != 0) {
                  playSound(0x2L);

                  //LAB_800fcf3c
                  FUN_800fca0c(InventoryMenuState.INIT_LOAD_GAME_37, 0x1L);
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
        if(selectedMenuOption_8011d738.get() == 1) {
          renderItemSubmenu(0xff, 0x6L);
        }

        //LAB_800fcf70
        FUN_80102484(0);

        //LAB_800fd344
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x4L, 0);
        break;

      case _5: // "Item" inventory submenu
        if((inventoryJoypadInput_800bdc44.get() & 0x8040L) != 0) { // Left or circle
          playSound(0x3L);
          inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
          unloadRenderable(selectedMenuOptionRenderablePtr_800bdbe4.deref());
        }

        //LAB_800fcfc0
        if(handleMenuUpDown(selectedItemSubmenuOption_8011d73c, 5)) {
          selectedMenuOptionRenderablePtr_800bdbe4.deref().y_44.set(getItemSubmenuOptionY(selectedItemSubmenuOption_8011d73c.get()) - 2);
        }

        //LAB_800fcff0
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) { // Cross
          playSound(0x2L);
          final int menuIndex = selectedItemSubmenuOption_8011d73c.get();
          if(menuIndex == 0) {
            //LAB_800fd058
            FUN_800fca0c(InventoryMenuState._26, 0x2L);
          } else if(menuIndex == 1) {
            //LAB_800fd04c
            //LAB_800fd058
            FUN_800fca0c(InventoryMenuState._31, 0x2L);
          } else if(menuIndex == 2) {
            //LAB_800fd034
            //LAB_800fd054
            //LAB_800fd058
            FUN_800fca0c(InventoryMenuState._16, 0x2L);
          } else if(menuIndex == 3) {
            //LAB_800fd058
            FUN_800fca0c(InventoryMenuState._35, 0x2L);
          } else if(menuIndex == 4) {
            FUN_800fca0c(InventoryMenuState.DABAS_INIT_72, 0x2L);
          }
        }

        //LAB_800fd060
        FUN_80102484(0x1L);
        renderItemSubmenu(selectedItemSubmenuOption_8011d73c.get(), 0x4L);

        //LAB_800fd344
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x6L, 0);
        break;

      case _6:
        messageBox(messageBox_8011dc90);

        if(messageBox_8011dc90.ticks_10.get() >= 0x2L) {
          if(selectedMenuOptionRenderablePtr_800bdbe4.isNull()) {
            selectedMenuOptionRenderablePtr_800bdbe4.set(allocateUiElement(0x74, 0x74, FUN_800fc7bc(0) - 34, menuOptionY(0) - 2));
            FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe4.deref());
            selectedMenuOptionRenderablePtr_800bdbe4.deref()._3c.set(0x20);
          }

          //LAB_800fd100
          if(handleMenuUpDown(selectedItemSubmenuOption_8011d73c, 4)) {
            selectedMenuOptionRenderablePtr_800bdbe4.deref().y_44.set(menuOptionY(selectedItemSubmenuOption_8011d73c.get()) - 2);
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
            inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
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

      case _7:
        deallocateRenderables(0xffL);
        renderGlyphs(glyphs_80114130, 0, 0);
        selectedMenuOptionRenderablePtr_800bdbe0.set(allocateUiElement(0x73, 0x73, 29, getMenuOptionY(selectedMenuOption_8011d738.get())));
        selectedMenuOptionRenderablePtr_800bdbe4.set(FUN_800fc900(selectedItemSubmenuOption_8011d73c.get()));
        FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe0.deref());
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 0x4L, 0xffL);
        scriptStartEffect(0x2L, 0xaL);
        FUN_80102484(0x1L);
        inventoryMenuState_800bdc28.set(InventoryMenuState._5);
        break;

      case _8:
        scriptStartEffect(0x2L, 0xaL);
        selectedSlot_8011d740.set(1);
        slotScroll_8011d744.set(0);
        inventoryMenuState_800bdc28.set(InventoryMenuState._9);
        break;

      case _9:
        deallocateRenderables(0xffL);
        renderGlyphs(glyphs_80114160, 0, 0);
        renderablePtr_800bdbe8.set(allocateUiElement(0x7f, 0x7f, 16, getSlotY(selectedSlot_8011d740.get())));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
        renderCharacterSwapScreen(0xffL);
        inventoryMenuState_800bdc28.set(InventoryMenuState._10);
        break;

      case _10:
        renderCharacterSwapScreen(0);

        if(_800bb168.get() != 0) {
          break;
        }

        if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0 && selectedSlot_8011d740.get() > 1) {
          selectedSlot_8011d740.decr();
          renderablePtr_800bdbe8.deref().y_44.set(getSlotY(selectedSlot_8011d740.get()));
          playSound(0x1L);
        }

        //LAB_800fd4e4
        //LAB_800fd4e8
        if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0 && selectedSlot_8011d740.get() < 2) {
          selectedSlot_8011d740.incr();
          renderablePtr_800bdbe8.deref().y_44.set(getSlotY(selectedSlot_8011d740.get()));
          playSound(0x1L);
        }

        //LAB_800fd52c
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          final int charIndex = gameState_800babc8.charIndex_88.get(selectedSlot_8011d740.get()).get();
          if(charIndex == -1 || (gameState_800babc8.charData_32c.get(charIndex).partyFlags_04.get() & 0x20L) != 0) {
            //LAB_800fd590
            playSound(0x28L);
          } else {
            //LAB_800fd5a0
            playSound(0x2L);
            renderablePtr_800bdbec.set(allocateUiElement(0x80, 0x80, FUN_800fc880(slotScroll_8011d744.get()), FUN_800fc8a8(slotScroll_8011d744.get())));
            FUN_80104b60(renderablePtr_800bdbec.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState._11);
          }
        }

        //LAB_800fd5f4
        //LAB_800fd5f8
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);

          //LAB_800fd62c
          FUN_800fca0c(whichMenu_800bdc38.get() != 0x18L ? InventoryMenuState._2 : InventoryMenuState._125, 0x5L);
        }

        break;

      case _11:
        renderCharacterSwapScreen(0);

        if((inventoryJoypadInput_800bdc44.get() & 0x8000L) != 0 && slotScroll_8011d744.get() % 3 > 0) {
          slotScroll_8011d744.decr();
          renderablePtr_800bdbec.deref().x_40.set(FUN_800fc880(slotScroll_8011d744.get()));
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8a8(slotScroll_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd6b8
        if((inventoryJoypadInput_800bdc44.get() & 0x2000L) != 0 && slotScroll_8011d744.get() % 3 < 2) {
          slotScroll_8011d744.incr();
          renderablePtr_800bdbec.deref().x_40.set(FUN_800fc880(slotScroll_8011d744.get()));
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8a8(slotScroll_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd730
        if((inventoryJoypadInput_800bdc44.get() & 0x1000L) != 0 && slotScroll_8011d744.get() > 2) {
          slotScroll_8011d744.sub(3);
          renderablePtr_800bdbec.deref().x_40.set(FUN_800fc880(slotScroll_8011d744.get()));
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8a8(slotScroll_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd78c
        //LAB_800fd790
        if((inventoryJoypadInput_800bdc44.get() & 0x4000L) != 0 && slotScroll_8011d744.get() < 3) {
          slotScroll_8011d744.add(3);
          renderablePtr_800bdbec.deref().x_40.set(FUN_800fc880(slotScroll_8011d744.get()));
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8a8(slotScroll_8011d744.get()));
          playSound(0x1L);
        }

        //LAB_800fd7e4
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          unloadRenderable(renderablePtr_800bdbec.deref());
          inventoryMenuState_800bdc28.set(InventoryMenuState._10);
        }

        //LAB_800fd820
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          final int secondaryCharIndex = secondaryCharIndices_800bdbf8.get(slotScroll_8011d744.get()).get();
          if(secondaryCharIndex == -1) {
            //LAB_800fd888
            playSound(0x28L);
            break;
          }

          if((gameState_800babc8.charData_32c.get(secondaryCharIndex).partyFlags_04.get() & 0x2L) == 0) {
            //LAB_800fd888
            playSound(0x28L);
            break;
          }

          //LAB_800fd898
          playSound(0x2L);
          final int charIndex = gameState_800babc8.charIndex_88.get(selectedSlot_8011d740.get()).get();
          gameState_800babc8.charIndex_88.get(selectedSlot_8011d740.get()).set(secondaryCharIndices_800bdbf8.get(slotScroll_8011d744.get()).get());
          secondaryCharIndices_800bdbf8.get(slotScroll_8011d744.get()).set(charIndex);
          inventoryMenuState_800bdc28.set(InventoryMenuState._9);
        }

        break;

      case _12:
        recalcInventory();
        scriptStartEffect(0x2L, 0xaL);
        deallocateRenderables(0xffL);

      case _13:
        slotScroll_8011d744.set(0);
        selectedSlot_8011d740.set(0);

      case _14:
        deallocateRenderables(0);
        renderGlyphs(glyphs_80114180, 0, 0);

        if(renderablePtr_800bdbe8.isNull()) {
          renderablePtr_800bdbe8.set(allocateUiElement(0x79, 0x79, FUN_800fc824(1), 0));
          FUN_80104b60(renderablePtr_800bdbe8.deref());
        }

        //LAB_800fd964
        renderablePtr_800bdbe8.deref().y_44.set(FUN_800fc804(selectedSlot_8011d740.get()));
        count_8011d750.set(FUN_801045fc(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()));
        FUN_80102660(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xffL);
        inventoryMenuState_800bdc28.set(InventoryMenuState._15);
        break;

      case _15:
        FUN_801034cc(charSlot_8011d734.get(), characterCount_8011d7c4.get());
        FUN_80102660(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);

        if(_800bb168.get() != 0) {
          break;
        }

        if(scrollMenu(selectedSlot_8011d740, slotScroll_8011d744, 4, count_8011d750.get(), 1)) {
          renderablePtr_800bdbe8.deref().y_44.set(FUN_800fc804(selectedSlot_8011d740.get()));
        }

        //LAB_800fda58
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          FUN_800fca0c(InventoryMenuState._2, 0x6L);
        }

        //LAB_800fda80
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) { // Equip item
          final int slot = selectedSlot_8011d740.get() + slotScroll_8011d744.get();
          if(slot < gameState_800babc8.equipmentCount_1e4.get()) {
            final int equipmentId = menuItems_8011d7c8.get(slot).itemId_00.get();
            if(equipmentId != 0xff) {
              final int previousEquipmentId = equipItem(equipmentId, characterIndices_800bdbb8.get(charSlot_8011d734.get()).get());
              takeEquipment(menuItems_8011d7c8.get(slot).itemSlot_01.get());
              giveItem(previousEquipmentId);
              playSound(0x2L);
              loadCharacterStats(0);
              addHp(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get(), 0);
              addMp(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get(), 0);
              inventoryMenuState_800bdc28.set(InventoryMenuState._14);
            } else {
              //LAB_800fdb6c
              playSound(0x28L);
            }
          }
        }

        //LAB_800fdb74
        if((inventoryJoypadInput_800bdc44.get() & 0x10L) != 0) {
          playSound(0x2L);
          _8011dcb8.get(0).setPointer(mallocTail(0x4c0L));
          _8011dcb8.get(1).setPointer(mallocTail(0x4c0L));
          _8011d754.setu(FUN_80104738(0x1L));
          FUN_80023a2c(_8011dcb8.get(0).deref(), gameState_800babc8.equipment_1e8, gameState_800babc8.equipmentCount_1e4.get());
          free(_8011dcb8.get(0).getPointer());
          free(_8011dcb8.get(1).getPointer());
          MEMORY.ref(4, 0x800bdc28L).setu(0xeL);
        }

        //LAB_800fdc18
        if(handleMenuLeftRight(charSlot_8011d734, characterCount_8011d7c4.get())) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._13);
        }

        break;

      case _16:
      case _31:
        scriptStartEffect(0x2L, 0xaL);
        deallocateRenderables(0xffL);
        renderGlyphs(glyphs_801141c4, 0, 0);
        _8011dcb8.get(0).setPointer(mallocTail(0x4c0L));
        _8011dcb8.get(1).setPointer(mallocTail(0x4c0L));
        recalcInventory();
        charSlot_8011d734.set(0);
        selectedSlot_8011d740.set(0);
        slotScroll_8011d744.set(0);
        slotScroll_8011d748.set(0);
        menuIndex_8011d74c.set(0);
        renderablePtr_800bdbe8.set(allocateUiElement(0x76, 0x76, FUN_800fc824(0), FUN_800fc814(selectedSlot_8011d740.get()) + 32));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
        FUN_80102840(slotScroll_8011d744.get(), slotScroll_8011d748.get(), 0xff, 0xffL);

        if(inventoryMenuState_800bdc28.get() != InventoryMenuState._16) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._32);
        } else {
          inventoryMenuState_800bdc28.set(InventoryMenuState._17);
        }

        break;

      case _17:
        _8011d754.setu(FUN_80104738(0));
        inventoryMenuState_800bdc28.set(InventoryMenuState._18);

        //LAB_800fe08c
        FUN_80102840(slotScroll_8011d744.get(), slotScroll_8011d748.get(), _8011dcb8.get(charSlot_8011d734.get()).deref().get(selectedSlot_8011d740.get() + slotScroll_8011d744.get()).itemId_00.get(), 0);
        break;

      case _32:
        _8011d754.setu(FUN_80104738(0x1L));
        inventoryMenuState_800bdc28.set(InventoryMenuState._33);

        //LAB_800fe08c
        FUN_80102840(slotScroll_8011d744.get(), slotScroll_8011d748.get(), _8011dcb8.get(charSlot_8011d734.get()).deref().get(selectedSlot_8011d740.get() + slotScroll_8011d748.get()).itemId_00.get(), 0);
        break;

      case _18:
      case _33: // Discard items menu
        //LAB_800fde10
        if(charSlot_8011d734.get() != 0) {
          count_8011d750.set(gameState_800babc8.itemCount_1e6.get());
        } else {
          //LAB_800fddf4
          count_8011d750.set(gameState_800babc8.equipmentCount_1e4.get() + (int)_8011d754.get());
        }

        if(charSlot_8011d734.get() != 0) {
          s1 = selectedSlot_8011d740.get() + slotScroll_8011d748.get();
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
            FUN_800fca0c(InventoryMenuState._7, 0x8L);
          } else {
            FUN_800fca0c(InventoryMenuState._19, 0x8L);
          }
        }

        //LAB_800fdeb4
        if((inventoryJoypadInput_800bdc44.get() & 0x10L) != 0) { // Discard items menu - sort items
          playSound(0x2L);

          final ArrayRef<UnsignedByteRef> a1_0;
          if(charSlot_8011d734.get() != 0) {
            a1_0 = gameState_800babc8.items_2e9;
          } else {
            //LAB_800fdef8
            a1_0 = gameState_800babc8.equipment_1e8;
          }

          //LAB_800fdf00
          FUN_80023a2c(_8011dcb8.get(charSlot_8011d734.get()).deref(), a1_0, count_8011d750.get());
        }

        //LAB_800fdf18
        //LAB_800fdf38
        //LAB_800fdf40
        if(scrollMenu(selectedSlot_8011d740, charSlot_8011d734.get() != 0 ? slotScroll_8011d748 : slotScroll_8011d744, 7, count_8011d750.get(), 1)) {
          renderablePtr_800bdbe8.deref().y_44.set(FUN_800fc814(selectedSlot_8011d740.get()) + 32);
        }

        //LAB_800fdf7c
        if(handleMenuLeftRight(charSlot_8011d734, 2)) {
          renderablePtr_800bdbe8.deref().x_40.set(FUN_800fc824(charSlot_8011d734.get()));
        }

        //LAB_800fdfb4
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0 && inventoryMenuState_800bdc28.get() == InventoryMenuState._33) {
          if((_8011dcb8.get(charSlot_8011d734.get()).deref().get((int)s1).price_02.get() & 0x2000) != 0) {
            //LAB_800fe064
            playSound(0x28L);
          } else if(_8011dcb8.get(charSlot_8011d734.get()).deref().get((int)s1).itemId_00.get() == 0xff) {
            //LAB_800fe064
            playSound(0x28L);
          } else {
            playSound(0x2L);
            menuIndex_8011d74c.set(0);
            renderablePtr_800bdc20.set(allocateUiElement(0x7d, 0x7d, 314, FUN_800fc860(0)));
            FUN_80104b60(renderablePtr_800bdc20.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState.CONFIRM_ITEM_DISCARD_34);
          }
        }

        //LAB_800fe06c
        //LAB_800fe070
        //LAB_800fe08c
        FUN_80102840(slotScroll_8011d744.get(), slotScroll_8011d748.get(), _8011dcb8.get(charSlot_8011d734.get()).deref().get((int)s1).itemId_00.get(), 0);
        break;

      case CONFIRM_ITEM_DISCARD_34:
        if(charSlot_8011d734.get() != 0) {
          s1 = selectedSlot_8011d740.get() + slotScroll_8011d748.get();
        } else {
          //LAB_800fe0dc
          s1 =  selectedSlot_8011d740.get() + slotScroll_8011d744.get();
        }

        //LAB_800fe0f0
        renderText(Really_want_to_throw_this_away_8011c8d4, 192, 180, 0x4L);
        renderCentredText(Yes_8011c20c, 328, FUN_800fc860(0) + 2, menuIndex_8011d74c.get() == 0 ? 0x5L : 0x6L);
        renderCentredText(No_8011c214, 328, FUN_800fc860(1) + 2, menuIndex_8011d74c.get() == 0 ? 0x6L : 0x5L);

        switch(handleYesNo(menuIndex_8011d74c)) {
          case SCROLLED ->
            //LAB_800fe1bc
            renderablePtr_800bdc20.deref().y_44.set(FUN_800fc860(menuIndex_8011d74c.get()));

          case YES -> {
            //LAB_800fe1d8
            //LAB_800fe1fc
            for(int i = (int)s1; i < count_8011d750.get(); i++) {
              final MenuItemStruct04 a = _8011dcb8.get(charSlot_8011d734.get()).deref().get(i);
              final MenuItemStruct04 b = _8011dcb8.get(charSlot_8011d734.get()).deref().get(i + 1);
              a.itemId_00.set(b.itemId_00);
              a.itemSlot_01.set(b.itemSlot_01);
              a.price_02.set(b.price_02);
            }

            //LAB_800fe238
            count_8011d750.decr();

            final ArrayRef<UnsignedByteRef> items;
            if(charSlot_8011d734.get() != 0) {
              items = gameState_800babc8.items_2e9;
            } else {
              //LAB_800fe274
              items = gameState_800babc8.equipment_1e8;
            }

            //LAB_800fe27c
            FUN_800239e0(_8011dcb8.get(charSlot_8011d734.get()).deref(), items, count_8011d750.get());
            recalcInventory();

            //LAB_800fe29c
            unloadRenderable(renderablePtr_800bdc20.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState._33);
          }

          case NO, CANCELLED -> {
            //LAB_800fe29c
            unloadRenderable(renderablePtr_800bdc20.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState._33);
          }
        }

        //LAB_800fe2b4
        //LAB_800fe2bc
        FUN_80102840(slotScroll_8011d744.get(), slotScroll_8011d748.get(), 0xff, 0x1L);
        break;

      case _19:
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        whichMenu_800bdc38.setu(0x9L);
        break;

      case STATUS_INIT_20:
        scriptStartEffect(0x2L, 0xaL);
        deallocateRenderables(0xffL);

        //LAB_800fe3c4
        inventoryMenuState_800bdc28.set(InventoryMenuState.STATUS_LOAD_21);
        break;

      case STATUS_LOAD_21:
        deallocateRenderables(0);
        renderGlyphs(glyphs_801141a4, 0, 0);
        renderStatusMenu(charSlot_8011d734.get(), 0xffL);
        inventoryMenuState_800bdc28.set(InventoryMenuState.STATUS_MENU_22);
        break;

      case STATUS_MENU_22:
        FUN_801034cc(charSlot_8011d734.get(), characterCount_8011d7c4.get());
        renderStatusMenu(charSlot_8011d734.get(), 0);

        if(_800bb168.get() == 0) {
          if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
            playSound(0x3L);
            FUN_800fca0c(InventoryMenuState._2, 0x7L);
          }

          //LAB_800fe3b0
          if(handleMenuLeftRight(charSlot_8011d734, characterCount_8011d7c4.get())) {
            //LAB_800fe3c4
            inventoryMenuState_800bdc28.set(InventoryMenuState.STATUS_LOAD_21);
          }
        }

        break;

      case ADDITIONS_INIT_23:
        selectedSlot_8011d740.set(0);
        renderablePtr_800bdba8.clear();
        renderablePtr_800bdba4.clear();
        renderablePtr_800bdbec.clear();
        renderablePtr_800bdbe8.clear();
        scriptStartEffect(2, 10);
        deallocateRenderables(0xff);
        inventoryMenuState_800bdc28.set(InventoryMenuState.ADDITIONS_LOAD_24);
        break;

      case ADDITIONS_LOAD_24:
        deallocateRenderables(0);
        loadAdditions(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get(), additions_8011e098);

        if(additions_8011e098.get(0).offset_00.get() != -1) {
          renderablePtr_800bdbe8.set(allocateUiElement(117, 117, 39, getAdditionSlotY(selectedSlot_8011d740.get()) - 4));
          FUN_80104b60(renderablePtr_800bdbe8.deref());
        }

        //LAB_800fe490
        allocateUiElement(69, 69,   0, 0);
        allocateUiElement(70, 70, 192, 0);
        renderAdditions(charSlot_8011d734.get(), selectedSlot_8011d740.get(), additions_8011e098, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()).selectedAddition_19.get(), 0xffL);
        inventoryMenuState_800bdc28.set(InventoryMenuState.ADDITIONS_MENU_25);
        break;

      case ADDITIONS_MENU_25:
        FUN_801034cc(charSlot_8011d734.get(), characterCount_8011d7c4.get());
        renderAdditions(charSlot_8011d734.get(), selectedSlot_8011d740.get(), additions_8011e098, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()).selectedAddition_19.get(), 0);

        if(_800bb168.get() != 0) {
          break;
        }

        if(handleMenuLeftRight(charSlot_8011d734, characterCount_8011d7c4.get())) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.ADDITIONS_LOAD_24);
          unloadRenderable(renderablePtr_800bdbe8.deref());
        }

        //LAB_800fe5b8
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          FUN_800fca0c(InventoryMenuState._2, 0x9L);
        }

        //LAB_800fe5e4
        if(additions_8011e098.get(0).offset_00.get() == -1) {
          break;
        }

        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          final int additionOffset = additions_8011e098.get(selectedSlot_8011d740.get()).offset_00.get();

          if(additionOffset != -1) {
            gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()).selectedAddition_19.set(additionOffset);
            playSound(0x2L);
            unloadRenderable(renderablePtr_800bdbe8.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState.ADDITIONS_LOAD_24);
          } else {
            //LAB_800fe680
            playSound(0x28L);
          }
        }

        //LAB_800fe68c
        if(handleMenuUpDown(selectedSlot_8011d740, 7)) {
          renderablePtr_800bdbe8.deref().y_44.set(getAdditionSlotY(selectedSlot_8011d740.get()) - 4);
        }

        break;

      case _26:
        scriptStartEffect(2, 10);
        charSlot_8011d734.set(0);
        slotScroll_8011d744.set(0);
        selectedSlot_8011d740.set(0);
        _8011d788.setu(0);
        v0 = _8011d788.getAddress();
        MEMORY.ref(4, v0).offset(0x4L).setu(0);
        v0 = 0x8012_0000L;
        MEMORY.ref(1, v0).offset(-0x2364L).setu(0);
        inventoryMenuState_800bdc28.set(InventoryMenuState._27);
        break;

      case _27: // Item sub-menu
        deallocateRenderables(0xffL);
        renderGlyphs(glyphs_801141fc, 0, 0);
        renderablePtr_800bdbec.set(allocateUiElement(0x77, 0x77, 42, FUN_800fc8dc(selectedSlot_8011d740.get())));
        FUN_80104b60(renderablePtr_800bdbec.deref());
        count_8011d750.set(FUN_80104448());
        FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xffL);
        inventoryMenuState_800bdc28.set(InventoryMenuState._28);
        break;

      case _28: // Transition from item sub-menu to item list
        FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);

        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._29);
        }

        break;

      case _29: // Item list
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          _800bdb9c.clear();
          _800bdba0.clear();
          saveListDownArrow_800bdb98.clear();
          saveListUpArrow_800bdb94.clear();
          FUN_800fca0c(InventoryMenuState._7, 0xaL);
        }

        //LAB_800fe824
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          a0 = FUN_80022afc(menuItems_8011d7c8.get(selectedSlot_8011d740.get() + slotScroll_8011d744.get()).itemId_00.get());
          _8011d7b8.set(a0);

          if(a0 == 0) {
            //LAB_800fe93c
            playSound(0x28L);
          } else if((menuItems_8011d7c8.get(selectedSlot_8011d740.get() + slotScroll_8011d744.get()).price_02.get() & 0x4000) == 0) {
            if((a0 & 0x2L) != 0) {
              //LAB_800fe8b0
              for(int i = 0; i < 7; i++) {
                _8011d718.get(i).set(allocateUiElement(0x7e, 0x7e, FUN_800fc8c0(i), 110));
                FUN_80104b60(_8011d718.get(i).deref());
              }
            } else {
              //LAB_800fe8f0
              renderablePtr_800bdbe8.set(allocateUiElement(0x7e, 0x7e, FUN_800fc8c0(charSlot_8011d734.get()), 110));
              FUN_80104b60(renderablePtr_800bdbe8.deref());
            }

            //LAB_800fe924
            playSound(0x2L);
            inventoryMenuState_800bdc28.set(InventoryMenuState._30);
          } else {
            //LAB_800fe93c
            playSound(0x28L);
          }
        }

        //LAB_800fe944
        if(scrollMenu(selectedSlot_8011d740, slotScroll_8011d744, 5, count_8011d750.get(), 1)) {
          renderablePtr_800bdbec.deref().y_44.set(FUN_800fc8dc(selectedSlot_8011d740.get()));
        }

        //LAB_800fe994
        //LAB_800fec18
        FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);
        break;

      case _30: // Confirm use item
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) { // Circle
          if((_8011d7b8.get() & 0x2L) == 0) {
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
          inventoryMenuState_800bdc28.set(InventoryMenuState._29);
        }

        //LAB_800fea24
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) { // Cross
          if((_8011d7b8.get() & 0x2L) != 0) {
            _8011d754.setu(-0x2L);

            if(characterCount_8011d7c4.get() != 0) {
              //LAB_800fea84
              for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
                useItemInMenu(_8011d788.getAddress(), menuItems_8011d7c8.get(selectedSlot_8011d740.get() + slotScroll_8011d744.get()).itemId_00.get(), characterIndices_800bdbb8.get(i).get());

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
            useItemInMenu(_8011d788.getAddress(), menuItems_8011d7c8.get(selectedSlot_8011d740.get() + slotScroll_8011d744.get()).itemId_00.get(), characterIndices_800bdbb8.get(charSlot_8011d734.get()).get());
          }

          //LAB_800feb40
          playSound(0x2L);
          takeItem(menuItems_8011d7c8.get(selectedSlot_8011d740.get() + slotScroll_8011d744.get()).itemId_00.get());
          count_8011d750.set(FUN_80104448());
          loadCharacterStats(0);
          FUN_80104324(_8011d788.getAddress());
          setMessageBoxText(_8011d790, 0);
          inventoryMenuState_800bdc28.set(InventoryMenuState._27);
        }

        //LAB_800febb0
        if((_8011d7b8.get() & 0x2L) == 0 && handleMenuLeftRight(charSlot_8011d734, characterCount_8011d7c4.get())) {
          renderablePtr_800bdbe8.deref().x_40.set(FUN_800fc8c0(charSlot_8011d734.get()) - 3);
        }

        //LAB_800fec04
        //LAB_800fec18
        FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);
        break;

      case _35: // Goods menu
        scriptStartEffect(0x2L, 0xaL);
        deallocateRenderables(0xffL);
        renderGlyphs(glyphs_801141c4, 0, 0);
        count_8011d750.set(0);

        //LAB_800fec7c
        for(int i = 0; i < 70; i++) {
          menuItems_8011d7c8.get(i).itemId_00.set(0xff);

          if(i < 0x40) {
            if((gameState_800babc8.dragoonSpirits_19c.get(i >>> 5).get() & 0x1L << (i & 0x1fL)) != 0) {
              menuItems_8011d7c8.get(count_8011d750.get()).itemId_00.set(i);
              menuItems_8011d7c8.get(count_8011d750.get()).itemSlot_01.set(i);
              menuItems_8011d7c8.get(count_8011d750.get()).price_02.set(0);
              count_8011d750.incr();
            }
          }

          //LAB_800fecf0
        }

        slotScroll_8011d744.set(0);
        selectedSlot_8011d740.set(0);
        charSlot_8011d734.set(0);
        renderablePtr_800bdbe8.set(allocateUiElement(0x76, 0x76, FUN_800fc824(0), FUN_800fc814(selectedSlot_8011d740.get()) + 32));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
        FUN_80102f74(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xffL);
        inventoryMenuState_800bdc28.set(InventoryMenuState._36);
        break;

      case _36:
        if((inventoryJoypadInput_800bdc44.get() & 0x40) != 0) {
          playSound(2);
          _800bdba0.clear();
          _800bdb9c.clear();
          saveListDownArrow_800bdb98.clear();
          saveListUpArrow_800bdb94.clear();
          menuItems_8011d7c8.get(count_8011d750.get()).itemSlot_01.set(0x30); // This is a bug - it's set to s0 but s0 is undefined. The value of s0 at this point is an address with the lower byte of 0x30. Unknown if this value needs to be set or not, and if it's just chance that 0x30 is a valid value.
          count_8011d750.incr();
          FUN_800fca0c(InventoryMenuState._7, 0xbL);
        }

        //LAB_800fede4
        if(scrollMenu(selectedSlot_8011d740, slotScroll_8011d744, 7, roundUp(count_8011d750.get(), 2), 2)) {
          renderablePtr_800bdbe8.deref().y_44.set(FUN_800fc814(selectedSlot_8011d740.get()) + 32);
        }

        //LAB_800fee38
        if((inventoryJoypadInput_800bdc44.get() & 0x8000) != 0 && charSlot_8011d734.get() != 0) {
          playSound(1);
          charSlot_8011d734.set(0);
          renderablePtr_800bdbe8.deref().x_40.set(FUN_800fc824(0));
        }

        //LAB_800fee80
        //LAB_800fee84
        if((inventoryJoypadInput_800bdc44.get() & 0x2000) != 0 && charSlot_8011d734.get() == 0) {
          playSound(1);
          charSlot_8011d734.set(1);
          renderablePtr_800bdbe8.deref().x_40.set(FUN_800fc824(1));
        }

        //LAB_800feed0
        //LAB_800feed4
        FUN_80102f74(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);
        break;

      case INIT_LOAD_GAME_37:
        _8004dd30.setu(0x1L);
        loadCharacterStats(0);
        FUN_80103bd4();
        scriptStartEffect(0x2L, 0xaL);
        inventoryMenuState_800bdc28.set(InventoryMenuState._40);
        deallocateRenderables(0xffL);
        renderSavedGames(slotScroll_8011d744.get(), false, 0xffL);
        break;

      case DISK_CHANGE_DO_YOU_WANT_TO_SAVE_NOW_38:
        setMessageBoxText(Do_you_want_to_save_now_8011c370, 0x2);
        inventoryMenuState_800bdc28.set(InventoryMenuState._39);
        deallocateRenderables(0xffL);
        renderSavedGames(slotScroll_8011d744.get(), false, 0xffL);
        break;

      case _39: // Part of load game menu
        switch(messageBox(messageBox_8011dc90)) {
          case YES ->
            //LAB_800fefa8
            inventoryMenuState_800bdc28.set(InventoryMenuState._40);

          case NO ->
            //LAB_800fefb8
            FUN_800fca0c(InventoryMenuState._125, 0xeL);
        }

        //LAB_800fefc4
        //LAB_800fefc8
        //LAB_800fff94
        renderSavedGames(slotScroll_8011d744.get(), false, 0);
        break;

      case _40: // Part of load game menu
        saveListDownArrow_800bdb98.clear();
        saveListUpArrow_800bdb94.clear();
        slotScroll_8011d744.set(0);
        selectedSlot_8011d740.set(0);
        renderSavedGames(0, false, 0);
        inventoryMenuState_800bdc28.set(InventoryMenuState._41);

      case _41: // Part of load game menu
        renderSavedGames(slotScroll_8011d744.get(), false, 0);

        saves.clear();
        saves.addAll(SaveManager.loadAllDisplayData());

        //LAB_800ff194
        if(saveCount_8011d768.offset(1, 0x10L).get() > 0xcL) {
          selectedSlot_8011d740.set((int)(saveCount_8011d768.offset(1, 0x10L).get() - 12));
          slotScroll_8011d744.set(12);
        } else {
          //LAB_800ff1d0
          selectedSlot_8011d740.set(0);
          slotScroll_8011d744.set((int)saveCount_8011d768.offset(1, 0x10L).get());
        }

        //LAB_800ff1e0
        renderablePtr_800bdbe8.set(allocateUiElement(129, 129,  16, getSlotY(selectedSlot_8011d740.get())));
        renderablePtr_800bdbec.set(allocateUiElement(130, 130, 192, getSlotY(selectedSlot_8011d740.get())));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
        FUN_80104b60(renderablePtr_800bdbec.deref());
        renderSaveListArrows(slotScroll_8011d744.get());

        inventoryMenuState_800bdc28.set(InventoryMenuState._42);
        messageBox_8011dc90._0c.incr();
        break;

      case _42:
        renderSaveListArrows(slotScroll_8011d744.get());

        //LAB_800ff2a8
        deallocateRenderables(0);
        renderSavedGames(slotScroll_8011d744.get(), true, 0xffL);

        //LAB_800ff4f8
        inventoryMenuState_800bdc28.set(InventoryMenuState.LOAD_GAME_MENU_43);
        break;

      case LOAD_GAME_MENU_43:
        if(scrollMenu(selectedSlot_8011d740, slotScroll_8011d744, 3, whichMenu_800bdc38.get() == 0xeL ? saves.size() : saves.size() + 1, 1)) {
          renderablePtr_800bdbe8.deref().y_44.set(getSlotY(selectedSlot_8011d740.get()));
          renderablePtr_800bdbec.deref().y_44.set(getSlotY(selectedSlot_8011d740.get()));
          inventoryMenuState_800bdc28.set(InventoryMenuState._42);
        }

        //LAB_800ff330
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          messageBox_8011dc90._0c.set(0);
          inventoryMenuState_800bdc28.set(InventoryMenuState._71);
        }

        //LAB_800ff35c
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) { // Press X to load game
          playSound(0x2L);
          inventoryMenuState_800bdc28.set(InventoryMenuState._44);

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
          renderSaveListArrows(slotScroll_8011d744.get());
        }

        //LAB_800fff80
        //LAB_800fff84
        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(slotScroll_8011d744.get(), true, 0);
        break;

      case _44:
        renderSavedGames(slotScroll_8011d744.get(), true, 0);

        //LAB_800ff440
        if(whichMenu_800bdc38.get() == 0xeL) { // Load game menu
          setMessageBoxText(Load_this_data_8011ca08, 0x2);
          inventoryMenuState_800bdc28.set(InventoryMenuState.DO_YOU_WANT_TO_LOAD_THIS_SAVE_45);
        } else {
          //LAB_800ff4a0
          //LAB_800ff4b0
          //LAB_800ff4c8
          if(slotScroll_8011d744.get() + selectedSlot_8011d740.get() < saves.size()) {
            //LAB_800ff4c0
            setMessageBoxText(Overwrite_save_8011c9e8, 0x2);
          } else {
            setMessageBoxText(Save_new_game_8011c9c8, 0x2);
          }

          messageBox_8011dc90.menuIndex_18.set(1);
          inventoryMenuState_800bdc28.set(InventoryMenuState._49);
        }

        break;

      case DO_YOU_WANT_TO_LOAD_THIS_SAVE_45:
        switch(messageBox(messageBox_8011dc90)) {
          case YES ->
            //LAB_800ff530
            //LAB_800ffc5c
            inventoryMenuState_800bdc28.set(InventoryMenuState.LOAD_SAVE_48);

          case NO ->
            //LAB_800ff53c
            //LAB_800ffc5c
            inventoryMenuState_800bdc28.set(InventoryMenuState.LOAD_GAME_MENU_43);
        }

        //LAB_800fff80
        //LAB_800fff84
        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(slotScroll_8011d744.get(), true, 0);
        break;

      case LOAD_SAVE_48:
        renderSavedGames(slotScroll_8011d744.get(), true, 0);

        _8011d7bc.set(0);
        _8011d7b8.set(0);

        loadSaveFile(slotScroll_8011d744.get() + selectedSlot_8011d740.get());

        //LAB_800ff6ec
        _800bdc34.setu(0x1L);
        _80052c34.setu(gameState_800babc8.submapScene_a4.get());
        submapCut_80052c30.set(gameState_800babc8.submapCut_a8.get());
        index_80052c38.set(gameState_800babc8.submapCut_a8.get());

        if(gameState_800babc8.submapCut_a8.get() == 264) {
          _80052c34.setu(0x35L);
        }

        //LAB_800ff730
        FUN_8002379c();
        setMonoOrStereo(gameState_800babc8.mono_4e0.get());

        //LAB_800ff754
        inventoryMenuState_800bdc28.set(InventoryMenuState._71);

        //LAB_80100bf4
        messageBox_8011dc90._0c.incr();
        break;

      case _49:
        switch(messageBox(messageBox_8011dc90)) {
          case YES ->
            //LAB_800ff788
            //LAB_800ffc5c
            inventoryMenuState_800bdc28.set(InventoryMenuState._52);

          case NO ->
            //LAB_800ff794
            //LAB_800ffc5c
            inventoryMenuState_800bdc28.set(InventoryMenuState.LOAD_GAME_MENU_43);
        }

        //LAB_800fff80
        //LAB_800fff84
        //LAB_800fff8c
        //LAB_800fff94
        renderSavedGames(slotScroll_8011d744.get(), true, 0);
        break;

      case _52:
        renderSavedGames(slotScroll_8011d744.get(), true, 0);

        _8011d7bc.set(0);
        _8011d7b8.set(0);

        gameState_800babc8.submapScene_a4.set(index_80052c38.get());
        gameState_800babc8.submapCut_a8.set((int)_800cb450.get());

        t0 = saveCount_8011d768.getAddress();
        a2 = tempSaveData_8011dcc0.getAddress();
        MEMORY.ref(4, a2).offset(0x30L).setu(MEMORY.ref(4, t0).offset(0xcL).get());
        if(MEMORY.ref(1, t0).offset(0x10L).get() != selectedSlot_8011d740.get() + slotScroll_8011d744.get()) {
          MEMORY.ref(4, a2).offset(0x30L).addu(0x1L);
        }

        //LAB_800ff940
        saveGame(selectedSlot_8011d740.get() + slotScroll_8011d744.get());

        //LAB_800ff988
        inventoryMenuState_800bdc28.set(InventoryMenuState._53);

        //LAB_80100bf4
        messageBox_8011dc90._0c.incr();
        break;

      case _53:
        renderSavedGames(slotScroll_8011d744.get(), true, 0);

        //LAB_800ff9cc
        //LAB_800ff9f4
        deallocateRenderables(0xffL);
        renderSavedGames(slotScroll_8011d744.get(), true, 0xffL);
        setMessageBoxText(Saved_8011cb2c, 0);
        inventoryMenuState_800bdc28.set(InventoryMenuState._54);
        break;

      case _54:
        renderSavedGames(slotScroll_8011d744.get(), true, 0);

        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          if(whichMenu_800bdc38.get() == 0x13L) {
            //LAB_80100020
            //LAB_80100024
            FUN_800fca0c(InventoryMenuState._125, 0xcL);
            break;
          }

          //LAB_800fff48
          inventoryMenuState_800bdc28.set(InventoryMenuState._71);
        }
        break;

      case _71: // Fade out arrows and progress to menu fade out
        if(confirmDest_800bdc30.get() == InventoryMenuState._122) {
          //LAB_8010069c
          inventoryMenuState_800bdc28.set(InventoryMenuState._119);
          renderDabasMenu(selectedSlot_8011d740.get());
          break;
        }

        //LAB_800fffd0
        _8004dd30.setu(0);
        renderSavedGames(slotScroll_8011d744.get(), true, 0);

        v1 = whichMenu_800bdc38.get();

        if(v1 == 0x13L) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.DISK_CHANGE_DO_YOU_WANT_TO_SAVE_NOW_38);
          //LAB_80100024
        } else if(v1 == 0x4L) {
          //LAB_80100018
          FUN_800fca0c(InventoryMenuState._2, 0xcL);
        } else {
          FUN_800fca0c(InventoryMenuState._125, 0xcL);
        }

        break;

      case DABAS_INIT_72:
        _8004dd30.setu(0x1L);
        dabasData_8011d7c0.setPointer(mallocTail(0x100L));
        dabasFilePtr_8011dd00.setu(0);

        //LAB_80100070
        for(int i = 0; i < 7; i++) {
          final MenuItemStruct04 menuItem = menuItems_8011d7c8.get(i);
          menuItem.itemId_00.set(0xff);
          menuItem.itemSlot_01.set(i);
          menuItem.price_02.set(0);
        }

        //LAB_80100098
        bzero(dabasData_8011d7c0.getPointer(), 0x100);

        loadDrgnBinFile(0, 6668, 0, getMethodAddress(SItem.class, "menuAssetsLoaded", long.class, long.class, long.class), 4, 0x2);
        scriptStartEffect(2, 10);
        selectedSlot_8011d740.set(0);

        //LAB_8010172c
        inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_INIT_2_73);
        break;

      case DABAS_INIT_2_73:
        deallocateRenderables(0xff);
        renderGlyphs(dabasMenuGlyphs_80114228, 0, 0);
        renderablePtr_800bdbe8.set(allocateUiElement(0x9f, 0x9f, 60, getDabasMenuY(selectedSlot_8011d740.get())));
        FUN_80104b60(renderablePtr_800bdbe8.deref());
        renderDabasMenu(selectedSlot_8011d740.get());
        inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_INIT_3_74);
        break;

      case DABAS_INIT_3_74:
        _8011dc8c.setu(0);
        _8011e094.setu(0);
        dabasGold_8011dd08.set(0);
        dabasHasItems_8011dd0c.setu(0);

        //LAB_801001a8
        for(int i = 0; i < 7; i++) {
          final MenuItemStruct04 menuItem = menuItems_8011d7c8.get(i);
          menuItem.itemId_00.set(0xff);
          menuItem.itemSlot_01.set(i);
          menuItem.price_02.set(0);
        }

        renderDabasMenu(selectedSlot_8011d740.get());

        if(DabasManager.hasSave()) {
          inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_LOAD_DATA_77);
        } else {
          inventoryMenuState_800bdc28.set(InventoryMenuState._78);
        }

        break;

      case DABAS_LOAD_DATA_77: {
        renderDabasMenu(selectedSlot_8011d740.get());

        MEMORY.setBytes(dabasData_8011d7c0.getPointer(), DabasManager.loadSave(), 0x580, 0x80);

        final DabasData100 dabasData = dabasData_8011d7c0.deref();

        //LAB_801003cc
        for(int i = 0; i < 6; i++) {
          final MenuItemStruct04 menuItem = menuItems_8011d7c8.get(i);

          final int itemId = dabasData.items_14.get(i).get();

          if(itemId != 0) {
            menuItem.itemId_00.set(itemId);
            dabasHasItems_8011dd0c.setu(0x1L);
          } else {
            //LAB_801003fc
            menuItem.itemId_00.set(0xff);
          }

          //LAB_80100400
          menuItem.itemSlot_01.set(i);
          menuItem.price_02.set(0);
        }

        final int specialItemId = dabasData.specialItem_2c.get();
        if(specialItemId != 0) {
          dabasHasItems_8011dd0c.setu(0x1L);
          menuItems_8011d7c8.get(6).itemId_00.set(specialItemId);
        } else {
          //LAB_80100450
          menuItems_8011d7c8.get(6).itemId_00.set(0xff);
        }

        //LAB_8010045c
        menuItems_8011d7c8.get(6).itemSlot_01.set(6);
        menuItems_8011d7c8.get(6).price_02.set(0);
        dabasGold_8011dd08.set(dabasData.gold_34.get());

        if(dabasData._3c.get() == 1) {
          _8011e094.setu(dabasData._3c.get());
        }

        //LAB_8010049c
        //LAB_801004a0
        //LAB_801004ac
        inventoryMenuState_800bdc28.set(InventoryMenuState._78);
        break;
      }

      case DABAS_MENU_79:
        if((inventoryJoypadInput_800bdc44.get() & 0x40) != 0) {
          playSound(3);
          inventoryMenuState_800bdc28.set(InventoryMenuState._120);
        }

        //LAB_801004ec
        if(handleMenuUpDown(selectedSlot_8011d740, 3)) {
          renderablePtr_800bdbe8.deref().y_44.set(getDabasMenuY(selectedSlot_8011d740.get()));
        }

        //LAB_8010051c
        if((inventoryJoypadInput_800bdc44.get() & 0x20) != 0) {
          if(selectedSlot_8011d740.get() == 0) {
            //LAB_80100570
            if(dabasHasItems_8011dd0c.get() != 0 || dabasGold_8011dd08.get() != 0) {
              //LAB_80100598
              playSound(2);
              dabasState_8011d758.setu(0);
              inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_TAKE_ITEMS_INIT_98);
            } else {
              //LAB_80100650
              playSound(40);
            }
          } else if(selectedSlot_8011d740.get() == 1) {
            //LAB_801005b4
            if(dabasHasItems_8011dd0c.get() != 0) {
              playSound(2);
              dabasState_8011d758.setu(1);
              inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_DISCARD_INIT_94);
            } else {
              //LAB_80100650
              playSound(40);
            }
            //LAB_80100558
          } else if(selectedSlot_8011d740.get() == 2) {
            //LAB_801005e4
            if(_8011e094.get() != 0) {
              playSound(2);

              if(dabasData_8011d7c0.deref()._38.get() < 5) {
                dabasState_8011d758.setu(2);
                inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_NEW_DIG_INIT_96);
              } else {
                //LAB_80100630
                setMessageBoxText(_8011d044, 0);
                _8011e094.setu(0);
                inventoryMenuState_800bdc28.set(InventoryMenuState._110);
              }
            } else {
              //LAB_80100650
              playSound(40);
            }
          }
        }

        //LAB_8010157c
        //LAB_80101580
        renderDabasMenu(selectedSlot_8011d740.get());
        break;

      case DABAS_DISCARD_INIT_94: {
        final LodString str = MEMORY.ref(2, mallocTail(48), LodString::new);
        str.set("Discard items?");

        setMessageBoxText(str, 0x2);
        messageBox_8011dc90.menuIndex_18.set(1);
        renderDabasMenu(selectedSlot_8011d740.get());

        //LAB_80100db4
        inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_CONFIRM_ACTION_99);
        break;
      }

      case DABAS_NEW_DIG_INIT_96: {
        final LodString str = MEMORY.ref(2, mallocTail(44), LodString::new);
        str.set("Begin new expedition?");

        setMessageBoxText(str, 0x2);
        messageBox_8011dc90.menuIndex_18.set(1);
        renderDabasMenu(selectedSlot_8011d740.get());

        //LAB_80100db4
        inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_CONFIRM_ACTION_99);
        break;
      }

      case DABAS_TAKE_ITEMS_INIT_98: {
        final LodString str = MEMORY.ref(2, mallocTail(46), LodString::new);
        str.set("Take items from Dabas?");

        setMessageBoxText(str, 0x2);
        inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_CONFIRM_ACTION_99);
        _8011dca8.setu(0x1L);
        renderDabasMenu(selectedSlot_8011d740.get());
        break;
      }

      case DABAS_CONFIRM_ACTION_99:
        switch(messageBox(messageBox_8011dc90)) {
          case YES -> {
            free(messageBox_8011dc90.text_00.getPointer());
            messageBox_8011dc90.text_00.clear();

            //LAB_80100e2c
            inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_PERFORM_ACTION_101);
          }

          case NO -> {
            free(messageBox_8011dc90.text_00.getPointer());
            messageBox_8011dc90.text_00.clear();

            //LAB_80100e40
            inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_MENU_79);
          }
        }

        //LAB_80100e48
        //LAB_8010157c
        //LAB_80101580
        renderDabasMenu(selectedSlot_8011d740.get());
        break;

      case DABAS_PERFORM_ACTION_101: {
        renderDabasMenu(selectedSlot_8011d740.get());
        inventoryMenuState_800bdc28.set(InventoryMenuState._102);

        final DabasData100 dabasData2 = dabasData_8011d7c0.deref();

        dabasData2.chapterIndex_00.set(gameState_800babc8.chapterIndex_98.get());

        final long dabasState = dabasState_8011d758.get();
        if(dabasState == 0) { // Send
          //LAB_80100f50
          //LAB_80100fec
          int equipmentCount = 0;
          int itemCount = 0;
          confirmDest_800bdc30.set(InventoryMenuState.DABAS_TAKE_ITEMS_FINISHED_105);
          dabasData2.gold_34.set(0);

          //LAB_80101014
          for(int i = 0; i < 7; i++) {
            final int itemId = menuItems_8011d7c8.get(i).itemId_00.get();
            if(itemId != 0xff) {
              if(itemId < 0xc0) {
                //LAB_80101034
                equipmentCount++;
              } else {
                itemCount++;
              }
            }
          }

          //LAB_80101070
          if(equipmentCount != 0 && gameState_800babc8.equipmentCount_1e4.get() + equipmentCount >= 0x100 || itemCount != 0 && gameState_800babc8.itemCount_1e6.get() + itemCount > 0x20) {
            //LAB_80101090
            final LodString str = MEMORY.ref(2, mallocTail(78), LodString::new);
            str.set("Dabas has more items\nthan you can hold");
            setMessageBoxText(str, 0);
            dabasState_8011d758.setu(3);
            break;
          }

          //LAB_801010ac
          dabasHasItems_8011dd0c.setu(0);

          //LAB_801010bc
          for(int i = 0; i < 7; i++) {
            giveItem(menuItems_8011d7c8.get(i).itemId_00.get());
          }

          //LAB_801010e4
          for(int i = 0; i < 6; i++) {
            dabasData2.items_14.get(i).set(0);
          }

          dabasData2.specialItem_2c.set(0);
          break;
        }

        if(dabasState == 1) { // Discard
          //LAB_80101104
          //LAB_80101108
          for(int i = 0; i < 6; i++) {
            dabasData2.items_14.get(i).set(0);
          }

          dabasHasItems_8011dd0c.setu(0);
          confirmDest_800bdc30.set(InventoryMenuState.DABAS_DISCARD_ITEMS_FINISHED_104);
          dabasData2.specialItem_2c.set(0);
          break;
        }

        //LAB_80100f40
        if(dabasState == 2) { // New dig
          //LAB_8010113c
          dabasData2._3c.set(2);
          dabasData2.specialItem_2c.set(0);
          _8011e094.setu(0);
          confirmDest_800bdc30.set(InventoryMenuState.DABAS_NEW_DIG_FINISHED_103);
        }
        break;
      }

      case _102:
        renderDabasMenu(selectedSlot_8011d740.get());

        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          final long state = dabasState_8011d758.get();
          if(state == 0 || state == 1) {
            //LAB_8010124c
            //LAB_8010125c
            for(int i = 0; i < 7; i++) {
              menuItems_8011d7c8.get(i).itemId_00.set(0xff);
            }
          } else if(state == 2) {
            //LAB_8010126c
            menuItems_8011d7c8.get(6).itemId_00.set(0xff);
          } else if(state == 3) {
            free(messageBox_8011dc90.text_00.getPointer());
          }

          //LAB_80101b14
          //LAB_80101b18
          inventoryMenuState_800bdc28.set(confirmDest_800bdc30.get());
        }
        break;

      case DABAS_NEW_DIG_FINISHED_103: {
        final String[] responses = {
          "Dabas bids you farewell.",
          "Dabas gives Dart an uncomfortable hug.",
          "Dabas gives Dart a comforting hug.",
          "Dabas gives Dart a comforting hug.\nHe was covered in dirt.",
          "Dabas gives Dart a comforting hug.\nShana is jealous.",
          "Dabas gives Rose a hug.\nShe punches him.",
          "Dabas gives Rose a hug.\nIt has been 1000 years.\nShe needed that.",
          "Dabas grabs his pickaxe.",
          "Dabas dons his helmet",
          "Dabas says goodbye.",
          "Dabas pulls out an accordion\nand plays himself off.",
          "Dabas walks off into the sunset.",
          "Dabas runs off into the sunset.",
          "Dabas leaves, mumbling about\na Dragoni Plant.",
        };

        final String response = responses[ThreadLocalRandom.current().nextInt(responses.length)];
        final LodString string = MEMORY.ref(2, mallocTail((response.length() + 1) * 2), LodString::new);
        string.set(response);

        setMessageBoxText(string, 0);
        renderDabasMenu(selectedSlot_8011d740.get());

        //LAB_80101650
        inventoryMenuState_800bdc28.set(InventoryMenuState._109);
        break;
      }

      case DABAS_DISCARD_ITEMS_FINISHED_104: {
        final String[] responses = {
          "Dabas is disappointed.",
          "Dabas isn't angry, he's\njust disappointed.",
          "Dabas isn't angry. He swears.",
          "You discarded all of\nDabas' hard work.",
          "Dabas reconsiders his\nchoice of career.",
          "Dabas used his good\npickaxe for that.",
          "Dabas throws the items\ninto the ocean.",
          "Dabas reconsiders his\nchoice of friends.",
          "A lone tear rolls down\nDabas' cheek.",
          "Dabas plays a sad song\non his accordion.",
        };

        final String response = responses[ThreadLocalRandom.current().nextInt(responses.length)];
        final LodString string = MEMORY.ref(2, mallocTail((response.length() + 1) * 2), LodString::new);
        string.set(response);

        setMessageBoxText(string, 0);
        renderDabasMenu(selectedSlot_8011d740.get());

        //LAB_80101650
        inventoryMenuState_800bdc28.set(InventoryMenuState._109);
        break;
      }

      case DABAS_TAKE_ITEMS_FINISHED_105:
        renderDabasMenu(selectedSlot_8011d740.get());
        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          if(dabasGold_8011dd08.get() == 0) {
            //LAB_8010175c
            inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_MENU_79);
            break;
          }

          final String[] responses = {
            "Dabas thanks you.",
            "Dabas thanks you for\nhis hard work.",
            "Dabas pats himself\non the back.",
            "Dabas plays a happy song\non his accordion.",
            "Dabas wonders why he is\nthe one paying you.",
            "You thank Dabas even though\nthe items are dirty.",

          };

          final String response = responses[ThreadLocalRandom.current().nextInt(responses.length)];
          final LodString string = MEMORY.ref(2, mallocTail((response.length() + 1) * 2), LodString::new);
          string.set(response);

          setMessageBoxText(string, 0x1);
          renderablePtr_800bdbec.clear();
          inventoryMenuState_800bdc28.set(InventoryMenuState._106);
        }

        break;

      case _106:
        messageBox(messageBox_8011dc90);

        if(messageBox_8011dc90.ticks_10.get() < 3) {
          //LAB_80101580
          renderDabasMenu(selectedSlot_8011d740.get());
          break;
        }

        if(renderablePtr_800bdbec.isNull()) {
          renderablePtr_800bdbec.set(allocateUiElement(0xd3, 0xd3, 68, 80));
          renderablePtr_800bdbec.deref()._3c.set(0x1f);
        }

        //LAB_80101380
        FUN_801073f8(112, 144, dabasGold_8011dd08.get());
        FUN_80106d10(226, 144, gameState_800babc8.gold_94.get());

        if((inventoryJoypadInput_800bdc44.get() & 0x20) == 0) {
          //LAB_80101580
          renderDabasMenu(selectedSlot_8011d740.get());
          break;
        }

        unloadRenderable(renderablePtr_800bdbec.deref());
        renderablePtr_800bdbec.set(allocateUiElement(0xd3, 0xd9, 68, 80));
        renderablePtr_800bdbec.deref()._3c.set(0x1f);
        inventoryMenuState_800bdc28.set(InventoryMenuState._107);

        //LAB_8010157c
        //LAB_80101580
        renderDabasMenu(selectedSlot_8011d740.get());
        break;

      case _107:
        messageBox(messageBox_8011dc90);

        if(dabasGold_8011dd08.get() <= 10 || (inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          //LAB_80101454
          //LAB_80101458
          dabasGold_8011dd08.set(0);
          gameState_800babc8.gold_94.add(dabasGold_8011dd08.get());
          unloadRenderable(renderablePtr_800bdbec.deref());
          renderablePtr_800bdbec.set(allocateUiElement(0xd3, 0xd3, 68, 80));
          renderablePtr_800bdbec.deref()._3c.set(0x1f);
          inventoryMenuState_800bdc28.set(InventoryMenuState._108);
        } else {
          dabasGold_8011dd08.sub(10);
          gameState_800babc8.gold_94.add(10);
        }

        //LAB_801014a8
        if(gameState_800babc8.gold_94.get() > 99999999) {
          gameState_800babc8.gold_94.set(99999999);
        }

        //LAB_801014cc
        if((_800bb0fc.get() & 0x1) != 0) {
          playSound(1);
        }

        //LAB_801014e8
        FUN_801073f8(112, 144, dabasGold_8011dd08.get());

        //LAB_80101574
        FUN_80106d10(226, 144, gameState_800babc8.gold_94.get());

        //LAB_8010157c
        //LAB_80101580
        renderDabasMenu(selectedSlot_8011d740.get());
        break;

      case _108:
        messageBox(messageBox_8011dc90);
        if((inventoryJoypadInput_800bdc44.get() & 0x20) != 0) {
          unloadRenderable(renderablePtr_800bdbec.deref());
          inventoryMenuState_800bdc28.set(InventoryMenuState._109);
          messageBox_8011dc90._0c.incr();
        }

        //LAB_80101554
        FUN_801073f8(112, 144, dabasGold_8011dd08.get());

        //LAB_80101574
        FUN_80106d10(226, 144, gameState_800babc8.gold_94.get());

        //LAB_8010157c
        //LAB_80101580
        renderDabasMenu(selectedSlot_8011d740.get());
        break;

      case _110:
        renderDabasMenu(selectedSlot_8011d740.get());

        if(messageBox(messageBox_8011dc90) == MessageBoxResult.AWAITING_INPUT) {
          return;
        }

        //LAB_80101644
        setMessageBoxText(_8011d048, 0);

        //LAB_80101650
        inventoryMenuState_800bdc28.set(InventoryMenuState._109);
        break;

      case _116:
        renderDabasMenu(selectedSlot_8011d740.get());

        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          //LAB_801016b0
          setMessageBoxText(_8011ce10, 0);
          inventoryMenuState_800bdc28.set(InventoryMenuState._119);
        }

        break;

      case _78:
      case _109:
      case _119:
        renderDabasMenu(selectedSlot_8011d740.get());

        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          if(inventoryMenuState_800bdc28.get() == InventoryMenuState._109) {
            free(messageBox_8011dc90.text_00.getPointer());
          }

          //LAB_8010175c
          inventoryMenuState_800bdc28.set(InventoryMenuState.DABAS_MENU_79);
        }

        break;

      case _120:
        renderDabasMenu(selectedSlot_8011d740.get());

        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          free(dabasFilePtr_8011dd00.get());
          free(dabasData_8011d7c0.getPointer());
          FUN_800fca0c(InventoryMenuState._2, 0xdL);
          _8004dd30.setu(0);
        }

        break;

      case _122:
        renderDabasMenu(selectedSlot_8011d740.get());
        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          v1 = _8011d7b8.get();
          if(v1 == 0 || v1 == 0x3L) {
            //LAB_80101844
//            inventoryMenuState_800bdc28.set(InventoryMenuState._59);
//            break;
            throw new RuntimeException("Need to figure out what 59 was");
          }

          //LAB_80101854
          //LAB_80101858
          inventoryMenuState_800bdc28.set(InventoryMenuState._116);
        }
        break;

      case _123: // Start fade out
        scriptStartEffect(0x1L, 0xaL);
        inventoryMenuState_800bdc28.set(InventoryMenuState._124);

      case _124:
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

          case 0x5 -> renderCharacterSwapScreen(0xfeL);
          case 0x6 -> FUN_80102660(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xfeL);
          case 0x7 -> renderStatusMenu(charSlot_8011d734.get(), 0xfeL);

          case 0x8 -> {
            if(charSlot_8011d734.get() != 0) {
              v0 = selectedSlot_8011d740.get() + slotScroll_8011d748.get();
            } else {
              //LAB_80101994
              v0 = selectedSlot_8011d740.get() + slotScroll_8011d744.get();
            }

            //LAB_801019a8
            FUN_80102840(slotScroll_8011d744.get(), slotScroll_8011d748.get(), _8011dcb8.get(charSlot_8011d734.get()).deref().get((int)v0).itemId_00.get(), 0);

            if((int)_800bb168.get() < 0xffL) {
              return;
            }

            free(_8011dcb8.get(0).getPointer());
            free(_8011dcb8.get(1).getPointer());
          }

          case 0x9 -> renderAdditions(charSlot_8011d734.get(), selectedSlot_8011d740.get(), additions_8011e098, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(charSlot_8011d734.get()).get()).selectedAddition_19.get(), 0xfeL);
          case 0xa -> FUN_80102dfc(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0);
          case 0xb -> FUN_80102f74(charSlot_8011d734.get(), selectedSlot_8011d740.get(), slotScroll_8011d744.get(), 0xfeL);
          //LAB_80101af4
          case 0xc -> renderSavedGames(slotScroll_8011d744.get(), true, 0xfeL);
          case 0xd -> renderDabasMenu(selectedSlot_8011d740.get());
          //LAB_80101af4
          case 0xe -> renderSavedGames(slotScroll_8011d744.get(), false, 0xfeL);
        }

        //LAB_80101afc
        //LAB_80101b00
        if((int)_800bb168.get() >= 0xffL) {
          //LAB_80101b14
          //LAB_80101b18
          inventoryMenuState_800bdc28.set(confirmDest_800bdc30.get());
        }

        break;

      case _125:
        deallocateRenderables(0xffL);
        free(drgn0_6666FilePtr_800bdc3c.getPointer());

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
        break;
    }
  }

  @Method(0x80101d10L)
  public static void renderInventoryMenu(final long selectedOption, final long a1, long a2) {
    a2 = a2 ^ 0xffL;

    final long s5 = canSave_8011dc88.get() != 0 ? a1 : 0x6L;

    //LAB_80101d54
    final long s4 = a2 < 0x1L ? 1 : 0;
    if(s4 != 0) {
      renderDragoonSpirits((int)gameState_800babc8.dragoonSpirits_19c.get(0).get(), 40, 197);
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
      v1 = worldMapNames_8011c1ec.get((int)continentIndex_800bf0b0.get()).deref();
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
  public static void renderItemSubmenu(final int selectedIndex, final long a1) {
    FUN_801038d4(150, 20, 60);
    renderCentredText(Use_it_8011cf1c, 142, getItemSubmenuOptionY(0), selectedIndex == 0 ? 0x5L : a1);
    renderCentredText(Discard_8011cf2c, 142, getItemSubmenuOptionY(1), selectedIndex == 1 ? 0x5L : a1);
    renderCentredText(List_8011cf3c, 142, getItemSubmenuOptionY(2), selectedIndex == 2 ? 0x5L : a1);
    renderCentredText(Goods_8011cf48, 142, getItemSubmenuOptionY(3), selectedIndex == 3 ? 0x5L : a1);

    final LodString dabas = MEMORY.ref(2, mallocTail(12), LodString::new);
    dabas.set("Diiig");
    renderCentredText(dabas, 142, getItemSubmenuOptionY(4), selectedIndex == 4 ? 0x5L : a1);
    free(dabas.getAddress());
  }

  @Method(0x8010214cL)
  public static void renderOptionsMenu(final long optionIndex, final long vibrateMode, final long soundMode, final long morphMode, final long noteMode) {
    _800bdf00.setu(0x20L);

    renderCentredText(Vibrate_8011cf58, FUN_800fc7bc(0) - 15, menuOptionY(0), optionIndex == 0 ? 0x5L : 0x4L);
    renderCentredText(Off_8011cf6c, FUN_800fc7bc(1), menuOptionY(0), vibrateMode == 0 ? 0x5L : 0x4L);
    renderCentredText(On_8011cf74, FUN_800fc7bc(2), menuOptionY(0), vibrateMode != 0 ? 0x5L : 0x4L);
    renderCentredText(Sound_8011cf7c, FUN_800fc7bc(0) - 15, menuOptionY(1), optionIndex == 1 ? 0x5L : 0x4L);
    renderCentredText(Stereo_8011cf88, FUN_800fc7bc(1), menuOptionY(1), soundMode == 0 ? 0x5L : 0x4L);
    renderCentredText(Mono_8011cf98, FUN_800fc7bc(2), menuOptionY(1), soundMode != 0 ? 0x5L : 0x4L);
    renderCentredText(Morph_8011cfa4, FUN_800fc7bc(0) - 15, menuOptionY(2), optionIndex == 2 ? 0x5L : 0x4L);
    renderCentredText(Normal_8011cfb0, FUN_800fc7bc(1), menuOptionY(2), morphMode == 0 ? 0x5L : 0x4L);
    renderCentredText(Short_8011cfc0, FUN_800fc7bc(2), menuOptionY(2), morphMode != 0 ? 0x5L : 0x4L);
    renderCentredText(Note_8011c814, FUN_800fc7bc(0) - 15, menuOptionY(3), optionIndex != 3 ? 0x4L : 0x5L);
    renderCentredText(Off_8011c838, FUN_800fc7d0(1), menuOptionY(3), noteMode == 0 ? 0x5L : 0x4L);
    renderCentredText(Half_8011c82c, FUN_800fc7d0(2), menuOptionY(3), noteMode == 1 ? 0x5L : 0x4L);
    renderCentredText(Stay_8011c820, FUN_800fc7d0(3), menuOptionY(3), noteMode == 2 ? 0x5L : 0x4L);

    _800bdf00.setu(0x21L);
  }

  @Method(0x80102484L)
  public static void FUN_80102484(final long a0) {
    //LAB_801024ac
    FUN_801038d4(a0 != 0 ? 23 : 24, 112, getMenuOptionY(1) + 3);
  }

  @Method(0x801024c4L)
  public static void renderCharacterSwapScreen(final long a0) {
    final long s1 = (a0 ^ 0xffL) < 0x1L ? 1 : 0;

    FUN_801082a0(198,  16, secondaryCharIndices_800bdbf8.get(0).get(), s1);
    FUN_801082a0(255,  16, secondaryCharIndices_800bdbf8.get(1).get(), s1);
    FUN_801082a0(312,  16, secondaryCharIndices_800bdbf8.get(2).get(), s1);
    FUN_801082a0(198, 122, secondaryCharIndices_800bdbf8.get(3).get(), s1);
    FUN_801082a0(255, 122, secondaryCharIndices_800bdbf8.get(4).get(), s1);
    FUN_801082a0(312, 122, secondaryCharIndices_800bdbf8.get(5).get(), s1);

    if(gameState_800babc8.charIndex_88.get(0).get() != -1) {
      renderCharacterSlot(16, 16, gameState_800babc8.charIndex_88.get(0).get(), s1, gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(0).get()).partyFlags_04.get() & 0x20L);
    }

    //LAB_801025b4
    if(gameState_800babc8.charIndex_88.get(1).get() != -1) {
      renderCharacterSlot(16, 88, gameState_800babc8.charIndex_88.get(1).get(), s1, gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(1).get()).partyFlags_04.get() & 0x20L);
    }

    //LAB_801025f8
    if(gameState_800babc8.charIndex_88.get(2).get() != -1) {
      renderCharacterSlot(16, 160, gameState_800babc8.charIndex_88.get(2).get(), s1, gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(2).get()).partyFlags_04.get() & 0x20L);
    }

    //LAB_8010263c
    uploadRenderables();
  }

  @Method(0x80102660L)
  public static void FUN_80102660(final int charSlot, final int slotIndex, final int slotScroll, final long a3) {
    final long s0 = (a3 ^ 0xffL) < 0x1L ? 1 : 0;

    renderCharacterSlot(16, 21, characterIndices_800bdbb8.get(charSlot).get(), s0, 0);
    renderCharacterStats(characterIndices_800bdbb8.get(charSlot).get(), menuItems_8011d7c8.get(slotIndex + slotScroll).itemId_00.get(), s0);
    renderCharacterEquipment(characterIndices_800bdbb8.get(charSlot).get(), s0);

    if(s0 != 0) {
      allocateUiElement(0x5a, 0x5a, 194, 96);
      _800bdb9c.set(allocateUiElement(0x3d, 0x44, 358, FUN_800fc804(0)));
      _800bdba0.set(allocateUiElement(0x35, 0x3c, 358, FUN_800fc804(3)));
    }

    //LAB_80102748
    renderMenuItems(194, 92, menuItems_8011d7c8, slotScroll, 4, _800bdb9c.deref(), _800bdba0.deref());
    renderString(0, 194, 178, menuItems_8011d7c8.get(slotIndex + slotScroll).itemId_00.get(), s0);

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
  public static void FUN_80102840(final int slotScroll1, final int slotScroll2, final int itemId, final long a3) {
    renderMenuItems( 16, 33, _8011dcb8.get(0).deref(), slotScroll1, 7, saveListUpArrow_800bdb94.derefNullable(), saveListDownArrow_800bdb98.derefNullable());
    renderMenuItems(194, 33, _8011dcb8.get(1).deref(), slotScroll2, 7, _800bdb9c.derefNullable(), _800bdba0.derefNullable());
    renderThreeDigitNumber(136, 24, gameState_800babc8.equipmentCount_1e4.get(), 0x2L);
    renderTwoDigitNumber(326, 24, gameState_800babc8.itemCount_1e6.get(), 0x2L);

    final long s1 = (a3 ^ 0xffL) < 0x1L ? 1 : 0;
    if(s1 != 0) {
      allocateUiElement(0xb, 0xb, 154, 24);
      renderThreeDigitNumber(160, 24, 0xff);
      allocateUiElement(0xb, 0xb, 338, 24);
      renderTwoDigitNumber(344, 24, 32);
      allocateUiElement(0x55, 0x55, 16, 16);
      saveListUpArrow_800bdb94.set(allocateUiElement(0x3d, 0x44, 180, FUN_800fc814(2)));
      saveListDownArrow_800bdb98.set(allocateUiElement(0x35, 0x3c, 180, FUN_800fc814(8)));
      allocateUiElement(0x55, 0x55, 194, 16);
      _800bdb9c.set(allocateUiElement(0x3d, 0x44, 358, FUN_800fc814(2)));
      _800bdba0.set(allocateUiElement(0x35, 0x3c, 358, FUN_800fc814(8)));
    }

    //LAB_80102a1c
    renderText(_8011c314,  32, 22, 0x4L);
    renderText(_8011c32c, 210, 22, 0x4L);

    if(a3 != 0x1L) {
      FUN_801038d4(0x89, 84, 178).clut_30.set(0x7cebL);
      renderText(_8011d024, 37, 178, 0x4L);
    }

    //LAB_80102a88
    renderString(0, 194, 178, itemId, s1);
    uploadRenderables();
  }

  @Method(0x80102ad8L)
  public static void renderAdditions(final int charSlot, final int slotIndex, final ArrayRef<MenuAdditionInfo> additions, final int selectedAdditionOffset, final long a4) {
    final long sp2c = (a4 ^ 0xffL) < 0x1L ? 1 : 0;
    final int charIndex = characterIndices_800bdbb8.get(charSlot).get();

    if(additions.get(0).offset_00.get() == -1) {
      renderText(_8011c340, 106, 150, 0x4L);
    } else {
      //LAB_80102b9c
      if(sp2c != 0) {
        renderGlyphs(glyphs_801141e4, 0, 0);
      }

      //LAB_80102bbc
      //LAB_80102bf0
      for(int i = 0; i < 8; i++) {
        final int y = getAdditionSlotY(i);

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
            renderThreeDigitNumber(297, y, (int)(additionData_80052884.get(offset).damage_0c.get() * (ptrTable_80114070.offset(offset * 0x4L).deref(1).offset(level * 0x4L).offset(0x3L).get() + 100) / 100)); // Damage
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
  public static void FUN_80102dfc(final int charSlot, final int selectedSlot, final int slotScroll, final long a3) {
    final long s2 = (a3 ^ 0xffL) < 0x1L ? 1 : 0;

    //LAB_80102e48
    for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
      FUN_80108464(FUN_800fc8c0(i) - 5, 120, characterIndices_800bdbb8.get(i).get(), s2);
    }

    //LAB_80102e88
    if(s2 != 0) {
      allocateUiElement(84, 84, 16, 16);
      saveListUpArrow_800bdb94.set(allocateUiElement(61, 68, 180, FUN_800fc8dc(0) + 2));
      saveListDownArrow_800bdb98.set(allocateUiElement(53, 60, 180, FUN_800fc8dc(4) + 2));
    }

    //LAB_80102ee8
    renderMenuItems(16, 10, menuItems_8011d7c8, slotScroll, 5, saveListUpArrow_800bdb94.derefNullable(), saveListDownArrow_800bdb98.derefNullable());
    renderString(0, 194, 16, menuItems_8011d7c8.get(selectedSlot + slotScroll).itemId_00.get(), s2);
    uploadRenderables();
  }

  @Method(0x80102f74L)
  public static void FUN_80102f74(final int charSlot, final int selectedSlot, final int slotScroll, final long a3) {
    final long s1 = (a3 ^ 0xffL) < 0x1L ? 1 : 0;

    if(s1 != 0) {
      allocateUiElement(0x55, 0x55,  16, 16);
      allocateUiElement(0x55, 0x55, 194, 16);
      _800bdb9c.set(allocateUiElement(0x3d, 0x44, 358, FUN_800fc814(2)));
      _800bdba0.set(allocateUiElement(0x35, 0x3c, 358, FUN_800fc814(8)));
    }

    //LAB_8010301c
    renderText(Goods_8011cf48,  32, 22, 0x4L);
    renderText(Goods_8011cf48, 210, 22, 0x4L);
    FUN_8010965c(slotScroll, _800bdb9c.derefNullable(), _800bdba0.derefNullable());
    renderString(1, 194, 178, menuItems_8011d7c8.get(charSlot + selectedSlot * 2 + slotScroll).itemId_00.get(), s1);
    uploadRenderables();
  }

  /**
   * @param fileScroll The first save game do display on the screen
   */
  @Method(0x801030c0L)
  public static void renderSavedGames(final int fileScroll, final boolean renderSaves, final long a2) {
    if(a2 == 0xffL) {
      renderGlyphs(glyphs_80114258, 0, 0);
    }

    //LAB_80103100
    if(renderSaves) {
      //LAB_80103108
      for(int i = 0; i < 3; i++) {
        final int fileIndex = fileScroll + i;

        if(fileIndex < saves.size()) {
          renderSaveGameSlot(fileIndex, getSlotY(i), a2 == 0xffL ? 1 : 0);
        } else {
          if(whichMenu_800bdc38.get() != 0xeL) {
            renderCentredText(new LodString("New save"), 188, getSlotY(i) + 25, 0x4L);
          }

          break;
        }
      }
    }

    //LAB_80103144
    uploadRenderables();
  }

  @Method(0x80103168L)
  public static void renderDabasMenu(final int selectedSlot) {
    //LAB_801031cc
    renderText(DigDabas_8011d04c,  48,  28, 0x4L);
    renderText(AcquiredItems_8011d050, 210,  28, 0x4L);
    renderText(SpecialItem_8011d054, 210, 170, 0x4L);
    renderText(AcquiredGold_8011cdd4,  30, 124, 0x4L);
    renderCentredText(Take_8011d058, 94, getDabasMenuY(0) + 2, selectedSlot == 0 ? 0x5L : (dabasHasItems_8011dd0c.get() | dabasGold_8011dd08.get()) == 0 ? 0x6L : 0x4L);
    renderCentredText(Discard_8011d05c, 94, getDabasMenuY(1) + 2, selectedSlot == 1 ? 0x5L : dabasHasItems_8011dd0c.get() == 0 ? 0x6L : 0x4L);
    renderCentredText(NextDig_8011d064, 94, getDabasMenuY(2) + 2, selectedSlot == 2 ? 0x5L : _8011e094.get() == 0 ? 0x6L : 0x4L);
    renderMenuItems(194, 37, menuItems_8011d7c8, 0, 6, _800bdb9c.derefNullable(), _800bdba0.derefNullable());
    renderEightDigitNumber(100, 147, dabasGold_8011dd08.get(), 0x2L);

    final int itemId = menuItems_8011d7c8.get(6).itemId_00.get();
    if(itemId != 0xff) {
      renderItemIcon(getItemIcon(itemId), 198, 192, 0x8L);
      renderText(equipment_8011972c.get(menuItems_8011d7c8.get(6).itemId_00.get()).deref(), 214, 194, 0x4L);
    }

    //LAB_80103390
    renderString(2, 16, 178, selectedSlot, 0);
    uploadRenderables();
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
    final int x = renderable.x_40.get();
    final int y = renderable.y_44.get();

    unloadRenderable(renderable);

    final Renderable58 newRenderable = allocateUiElement(108, 111, x, y);
    newRenderable.flags_00.or(0x10L);
    FUN_801033cc(newRenderable);
  }

  @Method(0x80103444L)
  public static void FUN_80103444(@Nullable final Renderable58 a0, final int a1, final int a2, final int a3, final int a4) {
    if(a0 != null) {
      if(a0._18.get() == 0) {
        if((simpleRand() & 0x3000L) != 0) {
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
    FUN_80103444(renderablePtr_800bdba4.derefNullable(), 0x2d, 0x34, 0xaa, 0xb1);
    FUN_80103444(renderablePtr_800bdba8.derefNullable(), 0x25, 0x2c, 0xa2, 0xa9);

    if(a0 != 0) {
      if(renderablePtr_800bdba4.isNull()) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 18, 16);
        renderable._18.set(0x2d);
        renderable._1c.set(0x34);
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
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 350, 16);
        renderable._18.set(0x25);
        renderable._1c.set(0x2c);
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
  public static void renderSaveListArrows(final long scroll) {
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
    if(scroll < (whichMenu_800bdc38.get() == 0x13L ? saves.size() - 3 : saves.size() - 2) && (whichMenu_800bdc38.get() == 0x13L && saves.size() > 2 || saves.size() > 3)) {
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
  public static void renderGlyphs(final UnboundedArrayRef<MenuGlyph06> glyphs, final int x, final int y) {
    //LAB_801037ac
    for(int i = 0; glyphs.get(i).glyph_00.get() != 0xff; i++) {
      final Renderable58 s0 = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);

      initGlyph(s0, glyphs.get(i));

      s0.x_40.add(x);
      s0.y_44.add(y);
    }

    //LAB_801037f4
  }

  @Method(0x80103818L)
  public static Renderable58 allocateUiElement(final int startGlyph, final int endGlyph, final int x, final int y) {
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
  public static Renderable58 renderItemIcon(final int glyph, final int x, final int y, final long flags) {
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
  public static long FUN_801039a0(final int equipmentId, final int charIndex) {
    if(charIndex == -1) {
      return 0;
    }

    //LAB_801039b4
    if(equipmentId < 0xc0) {
      return _80114284.offset(charIndex).get() & equipmentStats_80111ff0.get(equipmentId).equips_03.get();
    }

    //LAB_801039f0
    return 0;
  }

  @Method(0x801039f8L)
  public static int getEquipmentSlot(final int itemId) {
    if(itemId < 0xc0) {
      final int type = equipmentStats_80111ff0.get(itemId).type_01.get();

      //LAB_80103a2c
      for(int i = 0; i < 5; i++) {
        if((type & 0x80 >> i) != 0) {
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

    if(previousId == 0xff) {
      previousId = 0x100;
    }

    //LAB_80103af4
    //LAB_80103af8
    return previousId;
  }

  @Method(0x80103b10L)
  public static void FUN_80103b10() {
    characterCount_8011d7c4.set(0);

    //LAB_80103b48
    int a2 = 0;
    for(int slot = 0; slot < 9; slot++) {
      secondaryCharIndices_800bdbf8.get(slot).set(-1);
      characterIndices_800bdbb8.get(slot).set(-1);

      if((gameState_800babc8.charData_32c.get(slot).partyFlags_04.get() & 0x1L) != 0) {
        characterIndices_800bdbb8.get(characterCount_8011d7c4.get()).set(slot);
        characterCount_8011d7c4.incr();

        if(gameState_800babc8.charIndex_88.get(0).get() != slot && gameState_800babc8.charIndex_88.get(1).get() != slot && gameState_800babc8.charIndex_88.get(2).get() != slot) {
          secondaryCharIndices_800bdbf8.get(a2).set(slot);
          a2++;
        }
      }

      //LAB_80103bb4
    }
  }

  @Method(0x80103bd4L)
  public static void FUN_80103bd4() {
    //LAB_80103be8
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      tempSaveData_8011dcc0.offset(0x8L).offset(charSlot * 0x4L).setu(gameState_800babc8.charIndex_88.get(charSlot).get());
    }

    tempSaveData_8011dcc0.setu(0x5a02_0006L);
    tempSaveData_8011dcc0.offset(1, 0x14L).setu(gameState_800babc8.charData_32c.get(0).level_12.get());
    tempSaveData_8011dcc0.offset(1, 0x15L).setu(stats_800be5f8.get(0).dlevel_0f.get());
    tempSaveData_8011dcc0.offset(2, 0x16L).setu(gameState_800babc8.charData_32c.get(0).hp_08.get());
    tempSaveData_8011dcc0.offset(2, 0x18L).setu(stats_800be5f8.get(0).maxHp_66.get());
    tempSaveData_8011dcc0.offset(4, 0x1cL).setu(gameState_800babc8.gold_94.get());
    tempSaveData_8011dcc0.offset(4, 0x20L).setu(gameState_800babc8.timestamp_a0.get());
    tempSaveData_8011dcc0.offset(4, 0x24L).setu(gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0x1ffL);
    tempSaveData_8011dcc0.offset(4, 0x28L).setu(gameState_800babc8.stardust_9c.get());

    if(mainCallbackIndex_8004dd20.get() == 0x8L) {
      //LAB_80103c8c
      tempSaveData_8011dcc0.offset(1, 0x2dL).setu(0x1L);
      tempSaveData_8011dcc0.offset(1, 0x2cL).setu(continentIndex_800bf0b0.offset(1, 0x0L)); //1b
      //LAB_80103c98
    } else if(whichMenu_800bdc38.get() == 0x13L) {
      //LAB_80103c8c
      tempSaveData_8011dcc0.offset(1, 0x2dL).setu(0x3L);
      tempSaveData_8011dcc0.offset(1, 0x2cL).setu(gameState_800babc8.chapterIndex_98.get());
    } else {
      //LAB_80103cb4
      tempSaveData_8011dcc0.offset(1, 0x2dL).setu(0);
      tempSaveData_8011dcc0.offset(1, 0x2cL).setu(_800bd808);
    }
  }

  @Method(0x80103cc4L)
  public static void renderText(final LodString text, final int x, final int y, final long a3) {
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
  public static void renderCentredText(final LodString text, final int x, final int y, final long a3) {
    renderText(text, x - textWidth(text) / 2, y, a3);
  }

  /**
   * @param scrollAmount I'm pretty sure this is the amount the window scrolls when you reach the end of the elements that are currently on screen
   */
  @Method(0x80103f00L)
  public static boolean scrollMenu(final IntRef selectedSlot, @Nullable final IntRef scroll, int slotsDisplayed, int slotCount, final int scrollAmount) {
    slotsDisplayed = Math.min(slotsDisplayed, slotCount);

    if((inventoryJoypadInput_800bdc44.get() & 0x1000) != 0) {
      if(selectedSlot.get() == 0) {
        if(scroll == null || scroll.get() == 0) { // Wrap around up
          selectedSlot.set(slotsDisplayed - 1);

          if(scroll != null) {
            scroll.set(slotCount - slotsDisplayed);
          }
        } else {
          //LAB_80103f44
          if(scroll.get() < scrollAmount) {
            return true;
          }

          scroll.sub(scrollAmount);
        }
      } else {
        selectedSlot.decr();
      }
      //LAB_80103f64
    } else if((inventoryJoypadInput_800bdc44.get() & 0x4000) != 0) {
      if(selectedSlot.get() < slotsDisplayed - 1) {
        selectedSlot.incr();
      } else {
        if(scroll == null || scroll.get() + slotsDisplayed == slotCount) {
          selectedSlot.set(0);

          if(scroll != null) {
            scroll.set(0);
          }
        } else {
          //LAB_80103f8c
          if(slotCount <= scroll.get() + slotsDisplayed * scrollAmount) {
            return true;
          }

          scroll.add(scrollAmount);
        }
      }
      //LAB_80103fb0
    } else if((inventoryJoypadInput_800bdc44.get() & 0x4) != 0) {
      if(selectedSlot.get() != 0) {
        playSound(0x1L);
        selectedSlot.set(0);
      }

      return true;
      //LAB_80103fdc
    } else if((inventoryJoypadInput_800bdc44.get() & 0x1) != 0) {
      if(selectedSlot.get() >= slotsDisplayed - 1) {
        return true;
      }

      playSound(0x1L);
      selectedSlot.set(slotsDisplayed - 1);
      return true;
      //LAB_80104008
    } else if((inventoryJoypadInput_800bdc44.get() & 0x8) == 0 || scroll.get() < scrollAmount) {
      //LAB_8010404c
      if((inventoryJoypadInput_800bdc44.get() & 0x2) == 0) {
        return false;
      }

      if(slotsDisplayed >= slotCount) {
        return false;
      }

      final int v1 = scroll.get() + slotsDisplayed * scrollAmount;
      slotCount -= slotsDisplayed * scrollAmount;
      if(v1 < slotCount) {
        scroll.set(v1);
        //LAB_8010408c
      } else if(scroll.get() < slotCount) {
        scroll.set(slotCount);
      } else {
        return false;
      }
    } else if(scroll.get() >= slotsDisplayed * scrollAmount) {
      scroll.sub(slotsDisplayed * scrollAmount);
    } else {
      //LAB_80104044
      scroll.set(0);
    }

    //LAB_80104098
    playSound(0x1L);

    //LAB_801040a0
    //LAB_801040ac
    return true;
  }

  @Method(0x801040c0L)
  public static boolean handleMenuUpDown(final IntRef menuIndex, final int menuOptionCount) {
    if((inventoryJoypadInput_800bdc44.get() & 0x1000) != 0) { // Up
      playSound(0x1L);

      if(menuIndex.get() != 0) {
        menuIndex.decr();
      } else {
        //LAB_80104108
        menuIndex.set(menuOptionCount - 1);
      }

      //LAB_8010410c
      //LAB_80104118
    } else if((inventoryJoypadInput_800bdc44.get() & 0x4000) != 0) { // Down
      playSound(0x1L);

      if(menuIndex.get() < menuOptionCount - 1) {
        menuIndex.incr();
      } else {
        menuIndex.set(0);
      }
    } else {
      return false;
    }

    //LAB_80104110
    //LAB_80104148
    return true;
  }

  @Method(0x8010415cL)
  public static boolean handleMenuLeftRight(final IntRef menuIndex, final int menuItemCount) {
    if((inventoryJoypadInput_800bdc44.get() & 0x8000) != 0 && menuIndex.get() != 0) { // Left
      menuIndex.decr();
      playSound(0x1L);
      return true;
    }

    //LAB_80104184
    if((inventoryJoypadInput_800bdc44.get() & 0x2000) != 0 && menuIndex.get() < menuItemCount - 1) { // Right
      //LAB_801041b0
      menuIndex.incr();
      playSound(0x1L);

      //LAB_801041c8
      return true;
    }

    //LAB_801041c4
    return false;
  }

  @Method(0x801041d8L)
  public static YesNoResult handleYesNo(final IntRef menuOption) {
    if(handleMenuUpDown(menuOption, 2)) {
      return YesNoResult.SCROLLED;
    }

    if((inventoryJoypadInput_800bdc44.get() & 0x40) != 0) { // Circle/cancel
      playSound(0x3L);
      return YesNoResult.CANCELLED;
    }

    //LAB_80104220
    if((inventoryJoypadInput_800bdc44.get() & 0x20) != 0) { // Cross/accept
      playSound(0x2L);

      if(menuOption.get() == 0) {
        return YesNoResult.YES;
      }

      return YesNoResult.NO;
    }

    //LAB_80104244
    return YesNoResult.NONE;
  }

  @Method(0x80104254L) //TODO a1 struct
  public static void FUN_80104254(final LodString a0, final long a1) {
    final int v1 = (int)MEMORY.ref(4, a1).offset(0x4L).get();

    final LodString a0_0;
    final LodString a1_0;
    if(v1 == -2) {
      a0_0 = MEMORY.ref(2, a1 + 0x8L, LodString::new);
      a1_0 = _8011d618;
      //LAB_8010428c
    } else if(v1 == -1) {
      a0_0 = FUN_80103e04(MEMORY.ref(2, a1 + 0x8L, LodString::new), a0);
      a1_0 = _8011d534;
      //LAB_801042b4
    } else if(v1 != 0) {
      intToStr(v1, FUN_80103e04(MEMORY.ref(4, a1 + 0x8L, LodString::new), a0));
      a0_0 = MEMORY.ref(2, a1 + 0x8L + textLength(MEMORY.ref(2, a1 + 0x8L, LodString::new)) * 0x2L, LodString::new);
      a1_0 = _8011d560;
    } else {
      //LAB_801042fc
      a0_0 = MEMORY.ref(2, a1 + 0x8L, LodString::new);
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
        final long v1 = MEMORY.ref(4, a0).offset(0x4L).get();

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
  public static Renderable58 FUN_801038d4(final int glyph, final int x, final int y) {
    final Renderable58 renderable = allocateUiElement(glyph, glyph, x, y);
    renderable.flags_00.or(0x8L);
    return renderable;
  }

  @Method(0x80104448L)
  public static int FUN_80104448() {
    //LAB_8010449c
    long s5 = 0;
    for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
      s5 |= gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(i).get()).status_10.get();
    }

    //LAB_801044d4
    //LAB_801044e8
    for(int i = 0; i < 32; i++) {
      menuItems_8011d7c8.get(i).itemId_00.set(0xff);
    }

    //LAB_80104530
    int count = 0;
    for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
      final int itemId = gameState_800babc8.items_2e9.get(i).get();

      if(FUN_80022afc(itemId) != 0) {
        final ItemStats0c itemStats = itemStats_8004f2ac.get(itemId - 0xc0);
        final MenuItemStruct04 s3 = menuItems_8011d7c8.get(count);
        s3.itemId_00.set(itemId);
        s3.itemSlot_01.set(i);
        s3.price_02.set(0);

        if(itemStats.type_0b.get() == 0x8 && (itemStats.status_08.get() & s5) == 0) {
          s3.price_02.set(0x4000);
        }

        //LAB_80104594
        count++;
      }

      //LAB_8010459c
    }

    //LAB_801045b0
    qsort(menuItems_8011d7c8, count, 0x4, getBiFunctionAddress(Scus94491BpeSegment_8002.class, "compareItems", MenuItemStruct04.class, MenuItemStruct04.class, long.class));
    return count;
  }

  @Method(0x801045fcL)
  public static int FUN_801045fc(final int charIndex) {
    //LAB_80104640
    for(int i = 0; i < 0x100; i++) {
      menuItems_8011d7c8.get(i).itemId_00.set(0xff);
    }

    //LAB_80104694
    int s4 = 0;
    for(int equipmentSlot = 0; equipmentSlot < gameState_800babc8.equipmentCount_1e4.get(); equipmentSlot++) {
      final int equipmentId = gameState_800babc8.equipment_1e8.get(equipmentSlot).get();
      if(FUN_801039a0(equipmentId, charIndex) != 0) {
        final int equipmentSlot2 = getEquipmentSlot(equipmentId);

        if(equipmentSlot2 != 0xff) {
          if(equipmentId != gameState_800babc8.charData_32c.get(charIndex).equipment_14.get(equipmentSlot2).get()) {
            final MenuItemStruct04 s2 = menuItems_8011d7c8.get(s4);
            s2.itemId_00.set(equipmentId);
            s2.itemSlot_01.set(equipmentSlot);
            s2.price_02.set(0);
            s4++;
          }
        }
      }
    }

    //LAB_80104708
    return s4;
  }

  @Method(0x80104738L)
  public static long FUN_80104738(final long a0) {
    //LAB_8010476c
    for(int i = 0; i < 0x130; i++) {
      _8011dcb8.get(0).deref().get(i).itemId_00.set(0xff);
      _8011dcb8.get(1).deref().get(i).itemId_00.set(0xff);
    }

    //LAB_801047bc
    for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
      _8011dcb8.get(1).deref().get(i).itemId_00.set(gameState_800babc8.items_2e9.get(i).get());
      _8011dcb8.get(1).deref().get(i).itemSlot_01.set(i);
      _8011dcb8.get(1).deref().get(i).price_02.set(0);
    }

    //LAB_8010480c
    //LAB_8010482c
    int s1;
    for(s1 = 0; s1 < gameState_800babc8.equipmentCount_1e4.get(); s1++) {
      _8011dcb8.get(0).deref().get(s1).itemId_00.set(gameState_800babc8.equipment_1e8.get(s1).get());
      _8011dcb8.get(0).deref().get(s1).itemSlot_01.set(s1);
      _8011dcb8.get(0).deref().get(s1).price_02.set(0);

      if(a0 != 0 && FUN_80022898(gameState_800babc8.equipment_1e8.get(s1).get()) != 0) {
        _8011dcb8.get(0).deref().get(s1).price_02.or(0x2000);
      }

      //LAB_80104898
    }

    //LAB_801048ac
    if(a0 != 0) {
      return 0;
    }

    int s2 = s1;
    int t0 = 0;

    //LAB_801048e0
    for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
      //LAB_801048e8
      for(int a1 = 0; a1 < 5; a1++) {
        if(gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(i).get()).equipment_14.get(a1).get() != 0xff) {
          _8011dcb8.get(0).deref().get(s2).itemId_00.set(gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(i).get()).equipment_14.get(a1).get());
          _8011dcb8.get(0).deref().get(s2).itemSlot_01.set(s2);
          _8011dcb8.get(0).deref().get(s2).price_02.set(0x3000 | characterIndices_800bdbb8.get(i).get());

          t0++;
          s2++;
        }

        //LAB_80104968
      }
    }

    //LAB_8010498c
    //LAB_80104990
    return t0;
  }

  @Method(0x801049b4L)
  public static int loadAdditions(final int charIndex, final ArrayRef<MenuAdditionInfo> additions) {
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
    int t5 = 0;
    int t0 = 0;
    for(int additionIndex = 0; additionIndex < additionCounts_8004f5c0.get(charIndex).get(); additionIndex++) {
      final long a0_0 = additionData_80052884.get(additionOffsets_8004f5ac.get(charIndex).get() + additionIndex)._00.get();

      if(a0_0 == -1 && (gameState_800babc8.charData_32c.get(charIndex).partyFlags_04.get() & 0x40L) != 0) {
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
          t5 = additionOffsets_8004f5ac.get(charIndex).get() + additionIndex + 1;
        }

        t0++;
      }

      //LAB_80104b00
    }

    //LAB_80104b14
    return t5;
  }

  @Method(0x80104b1cL)
  public static void initGlyph(final Renderable58 a0, final MenuGlyph06 glyph) {
    if(glyph.glyph_00.get() != 0xff) {
      a0.glyph_04.set(glyph.glyph_00.get());
      a0.flags_00.or(0x4L);
    }

    //LAB_80104b40
    a0.tpage_2c.set(0x19L);
    a0.clut_30.set(0);
    a0.x_40.set(glyph.x_02.get());
    a0.y_44.set(glyph.y_04.get());
  }

  @Method(0x80104b60L)
  public static void FUN_80104b60(final Renderable58 a0) {
    a0._28.set(0x1);
    a0._34.set(0);
    a0._38.set(0);
    a0._3c.set(0x23);
  }

  @Method(0x80104b7cL)
  public static long FUN_80104b7c(final long dragoons, final int charIndex) {
    final Memory.TemporaryReservation sp0x0tmp = MEMORY.temp(0x2c);

    sp0x0tmp.get().offset(0x28L).setu(dragoons);

    final long sp = sp0x0tmp.address;
    final long v0 = _800fba58.getAddress();

    //LAB_80104b94
    memcpy(sp, v0, 0x24);

    if(charIndex == -0x1L) {
      return 0;
    }

    //LAB_80104be0
    long v1 = (MEMORY.ref(4, sp).offset(0x28L).offset((MEMORY.ref(4, sp).offset(charIndex * 0x4L).get() >>> 5) * 0x4L).get() & 0x1L << (MEMORY.ref(4, sp).offset(charIndex * 0x4L).get() & 0x1fL)) > 0 ? 1 : 0;
    if(charIndex == 0) {
      v1 = v1 | dragoons >>> 7;
    }

    sp0x0tmp.release();

    //LAB_80104c24
    //LAB_80104c28
    return v1;
  }

  @Method(0x80104c30L)
  public static void renderTwoDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0, 2);
  }

  @Method(0x80104dd4L)
  public static void renderThreeDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0, 3);
  }

  @Method(0x80105048L)
  public static long FUN_80105048(final int x, final int y, final int currentVal, int newVal) {
    long flags = 0;
    final long clut;
    if(currentVal < newVal) {
      clut = 0x7c6bL;
      //LAB_80105090
    } else if(currentVal > newVal) {
      clut = 0x7c2bL;
    } else {
      clut = 0;
    }

    //LAB_801050a0
    //LAB_801050a4
    if(newVal > 999) {
      newVal = 999;
    }

    //LAB_801050b0
    int s0 = newVal / 100 % 10;
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
      renderable.x_40.set(x);
      renderable.y_44.set(y);
      flags |= 0x1L;
    }

    //LAB_80105190
    s0 = newVal / 10 % 10;
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
      renderable.x_40.set(x + 6);
      renderable.y_44.set(y);
    }

    //LAB_80105274
    s0 = newVal % 10;
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    //LAB_801052d8
    //LAB_801052dc
    renderable.flags_00.or(0xcL);
    renderable.glyph_04.set(s0);
    renderable.tpage_2c.set(0x19L);
    renderable.clut_30.set(clut);
    renderable._3c.set(0x21);
    renderable.x_40.set(x + 12);
    renderable.y_44.set(y);
    return clut;
  }

  @Method(0x80105350L)
  public static void renderFourDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0, 4);
  }

  /** Does something different with CLUT */
  @Method(0x8010568cL)
  public static void renderFourDigitNumber(final int x, final int y, int value, final int max) {
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
    int s0 = value / 1_000 % 10;
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
  public static void renderSixDigitNumber(final int x, final int y, int value) {
    long flags = 0;

    if(value > 999999) {
      value = 999999;
    }

    //LAB_80105a98
    int s0 = value / 100_000 % 10;
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
  public static void renderEightDigitNumber(final int x, final int y, final int value, final long flags) {
    renderNumber(x, y, value, flags, 8);
  }

  @Method(0x801065bcL)
  public static void renderFiveDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0x2L, 5);
  }

  @Method(0x801069d0L)
  public static void FUN_801069d0(final int x, final int y, final int value) {
    // I didn't look at this method too closely, this may or may not be right
    renderNumber(x, y, value, 0x2L, 4);
  }

  @Method(0x80106d10L)
  public static void FUN_80106d10(final int x, final int y, final int value) {
    long sp10 = 0x2L;

    //LAB_80106d5c
    int s0 = value / 10000000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      renderable.x_40.set(x);
      renderable.y_44.set(y);
      sp10 |= 0x1L;
    }

    //LAB_80106e10
    s0 = value / 1000000 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80106e78
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      renderable.x_40.set(x + 6);
      renderable.y_44.set(y);
      sp10 |= 0x1L;
    }

    //LAB_80106ee8
    s0 = value / 100000 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80106f50
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      renderable.x_40.set(x + 12);
      renderable.y_44.set(y);
      sp10 |= 0x1L;
    }

    //LAB_80106fbc
    s0 = value / 10000 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80107024
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      renderable.x_40.set(x + 18);
      renderable.y_44.set(y);
      sp10 |= 0x1L;
    }

    //LAB_80107094
    s0 = value / 1000 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_801070f8
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      renderable.x_40.set(x + 24);
      renderable.y_44.set(y);
      sp10 |= 0x1L;
    }

    //LAB_80107168
    s0 = value / 100 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_801071cc
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      renderable.x_40.set(x + 30);
      renderable.y_44.set(y);
      sp10 |= 0x1L;
    }

    //LAB_80107238
    s0 = value / 10 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_8010729c
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      renderable.x_40.set(x + 36);
      renderable.y_44.set(y);
    }

    //LAB_80107308
    //LAB_80107360
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    renderable.flags_00.or(0xcL);
    renderable.glyph_04.set(value % 10);
    renderable.tpage_2c.set(0x19L);
    renderable.clut_30.set(0);
    renderable._3c.set(0x1f);
    renderable.x_40.set(x + 42);
    renderable.y_44.set(y);
  }

  @Method(0x801073f8L)
  public static void FUN_801073f8(final int x, final int y, final int value) {
    long sp10 = 0x2L;

    //LAB_80107438
    int s0 = value / 1000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.x_40.set(x);
      renderable.y_44.set(y);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      sp10 |= 0x1L;
    }

    //LAB_801074ec
    s0 = value / 100 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80107554
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.x_40.set(x + 6);
      renderable.y_44.set(y);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
      sp10 |= 0x1L;
    }

    //LAB_801075c0
    s0 = value / 10 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80107624
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00.or(0xcL);
      renderable.glyph_04.set(s0);
      renderable.tpage_2c.set(0x19L);
      renderable.x_40.set(x + 12);
      renderable.y_44.set(y);
      renderable.clut_30.set(0);
      renderable._3c.set(0x1f);
    }

    //LAB_80107690
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    renderable.flags_00.or(0xcL);
    renderable.x_40.set(value % 10);
    renderable.tpage_2c.set(0x19L);
    renderable.x_40.set(x + 18);
    renderable.y_44.set(y);
    renderable.clut_30.set(0);
    renderable._3c.set(0x1f);
  }

  /**
   * @param flags Bitset - 0x1: render leading zeros, 0x2: ?
   */
  public static void renderNumber(final int x, final int y, int value, long flags, final int digitCount) {
    if(value >= Math.pow(10, digitCount)) {
      value = (int)Math.pow(10, digitCount) - 1;
    }

    for(int i = 0; i < digitCount; i++) {
      final int digit = value / (int)Math.pow(10, digitCount - (i + 1)) % 10;

      if(digit != 0 || i == digitCount - 1 || (flags & 0x1L) != 0) {
        final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
        struct.flags_00.or((flags & 0x2L) != 0 ? 0xcL : 0x4L);
        struct.glyph_04.set(digit);
        struct.tpage_2c.set(0x19L);
        struct.clut_30.set(0);
        struct._3c.set(0x21);
        struct.x_40.set(x + 6 * i);
        struct.y_44.set(y);
        flags |= 0x1L;
      }
    }
  }

  @Method(0x80107764L)
  public static void renderThreeDigitNumber(final int x, final int y, final int value, final long flags) {
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
  public static void renderTwoDigitNumber(final int x, final int y, final int value, final long flags) {
    renderNumber(x, y, value, flags, 2);
  }

  @Method(0x80107cb4L)
  public static void renderCharacter(final int x, final int y, final int character) {
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
  public static void FUN_80107d34(final int x, final int y, final int currentVal, final int newVal) {
    final long clut = FUN_80105048(x, y, currentVal, newVal);
    final Renderable58 v0 = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    v0.flags_00.or(0xcL);
    v0.glyph_04.set(0xc);
    v0.tpage_2c.set(0x19L);
    v0.clut_30.set(clut);
    v0._3c.set(0x21);
    v0.x_40.set(x + 20);
    v0.y_44.set(y);
  }

  @Method(0x80107dd4L)
  public static void renderXp(final int x, final int y, final int xp) {
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
  public static long FUN_80107e70(final int x, final int y, final int charIndex) {
    //LAB_80107e90
    final long a0_0 = gameState_800babc8.charData_32c.get(charIndex).status_10.get();

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
  public static void renderCharacterSlot(final int x, final int y, final int charIndex, final long a3, final long a4) {
    if(charIndex != -1) {
      if(a3 != 0) {
        allocateUiElement( 74,  74, x, y)._3c.set(0x21);
        allocateUiElement(153, 153, x, y);

        if(charIndex < 9) {
          final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          initGlyph(struct, glyph_801142d4);
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
  public static void FUN_801082a0(final int x, final int y, final int charIndex, final long a3) {
    if(charIndex != -0x1L) {
      if(a3 != 0) {
        if(charIndex < 9) {
          final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          initGlyph(renderable, glyph_801142d4);
          renderable.glyph_04.set(charIndex);
          renderable.tpage_2c.add(0x1L);
          renderable._3c.set(0x21);
          renderable.x_40.set(x + 2);
          renderable.y_44.set(y + 8);
        }

        //LAB_8010834c
        allocateUiElement(0x50, 0x50, x, y)._3c.set(0x21);
        allocateUiElement(0x9c, 0x9c, x, y);

        if((gameState_800babc8.charData_32c.get(charIndex).partyFlags_04.get() & 0x2L) == 0) {
          allocateUiElement(0x72, 0x72, x, y + 24)._3c.set(0x21);
        }

        //LAB_801083c4
        final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);
        renderFourDigitNumber(x + 25, y + 57, stats.level_0e.get());
        renderFourDigitNumber(x + 25, y + 68, stats.dlevel_0f.get());
        renderFourDigitNumber(x + 25, y + 79, stats.hp_04.get(), stats.maxHp_66.get());
        renderFourDigitNumber(x + 25, y + 90, stats.mp_06.get());
      }
    }

    //LAB_80108438
  }

  @Method(0x80108464L)
  public static void FUN_80108464(final int x, final int y, final int charIndex, final long a4) {
    if(charIndex != -1) {
      FUN_80107e70(x - 4, y - 6, charIndex);

      if(a4 != 0) {
        allocateUiElement(112, 112, x, y)._3c.set(0x21);

        if(charIndex < 9) {
          final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
          initGlyph(renderable, glyph_801142d4);
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
        loadCharacterStats(0);

        //LAB_80108694
        memcpy(statsTmp.getAddress(), stats_800be5f8.get(charIndex).getAddress(), 0xa0);

        //LAB_801086e8
        memcpy(gameState_800babc8.charData_32c.get(charIndex).equipment_14.getAddress(), sp0xb0tmp.address, 5);

        sp0xb0tmp.release();

        loadCharacterStats(0);
      } else {
        //LAB_80108720
        //LAB_80108740
        memcpy(statsTmp.getAddress(), stats_800be5f8.get(charIndex).getAddress(), 0xa0);
      }

      //LAB_80108770
      final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);
      FUN_80105048( 58, 116, stats.bodyAttack_6a.get(), statsTmp.bodyAttack_6a.get());
      FUN_80105048( 90, 116, stats.gearAttack_88.get(), statsTmp.gearAttack_88.get());
      FUN_80105048(122, 116, stats.bodyAttack_6a.get() + stats.gearAttack_88.get(), statsTmp.bodyAttack_6a.get() + statsTmp.gearAttack_88.get());

      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) != 0) {
        FUN_80107d34(159, 116, stats.dragoonAttack_72.get(), statsTmp.dragoonAttack_72.get());
      }

      //LAB_801087fc
      FUN_80105048( 58, 128, stats.bodyDefence_6c.get(), statsTmp.bodyDefence_6c.get());
      FUN_80105048( 90, 128, stats.gearDefence_8c.get(), statsTmp.gearDefence_8c.get());
      FUN_80105048(122, 128, stats.bodyDefence_6c.get() + stats.gearDefence_8c.get(), statsTmp.bodyDefence_6c.get() + statsTmp.gearDefence_8c.get());

      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) != 0) {
        FUN_80107d34(159, 128, stats.dragoonDefence_74.get(), statsTmp.dragoonDefence_74.get());
      }

      //LAB_8010886c
      FUN_80105048( 58, 140, stats.bodyMagicAttack_6b.get(), statsTmp.bodyMagicAttack_6b.get());
      FUN_80105048( 90, 140, stats.gearMagicAttack_8a.get(), statsTmp.gearMagicAttack_8a.get());
      FUN_80105048(122, 140, stats.bodyMagicAttack_6b.get() + stats.gearMagicAttack_8a.get(), statsTmp.bodyMagicAttack_6b.get() + statsTmp.gearMagicAttack_8a.get());

      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) != 0) {
        FUN_80107d34(159, 140, stats.dragoonMagicAttack_73.get(), statsTmp.dragoonMagicAttack_73.get());
      }

      //LAB_801088dc
      FUN_80105048( 58, 152, stats.bodyMagicDefence_6d.get(), statsTmp.bodyMagicDefence_6d.get());
      FUN_80105048( 90, 152, stats.gearMagicDefence_8e.get(), statsTmp.gearMagicDefence_8e.get());
      FUN_80105048(122, 152, stats.bodyMagicDefence_6d.get() + stats.gearMagicDefence_8e.get(), statsTmp.bodyMagicDefence_6d.get() + statsTmp.gearMagicDefence_8e.get());

      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) != 0) {
        FUN_80107d34(159, 152, stats.dragoonMagicDefence_75.get(), statsTmp.dragoonMagicDefence_75.get());
      }

      //LAB_8010894c
      FUN_80105048( 58, 164, stats.bodySpeed_69.get(), statsTmp.bodySpeed_69.get());
      FUN_80105048( 90, 164, stats.gearSpeed_86.get(), statsTmp.gearSpeed_86.get());
      FUN_80105048(122, 164, stats.bodySpeed_69.get() + stats.gearSpeed_86.get(), statsTmp.bodySpeed_69.get() + statsTmp.gearSpeed_86.get());

      FUN_80107d34( 90, 176, stats.attackHit_90.get(), statsTmp.attackHit_90.get());
      FUN_80107d34(122, 176, stats.attackHit_90.get(), statsTmp.attackHit_90.get());
      FUN_80107d34( 90, 188, stats.magicHit_92.get(), statsTmp.magicHit_92.get());
      FUN_80107d34(122, 188, stats.magicHit_92.get(), statsTmp.magicHit_92.get());
      FUN_80107d34( 90, 200, stats.attackAvoid_94.get(), statsTmp.attackAvoid_94.get());
      FUN_80107d34(122, 200, stats.attackAvoid_94.get(), statsTmp.attackAvoid_94.get());
      FUN_80107d34( 90, 212, stats.magicAvoid_96.get(), statsTmp.magicAvoid_96.get());
      FUN_80107d34(122, 212, stats.magicAvoid_96.get(), statsTmp.magicAvoid_96.get());

      sp0x10tmp.release();

      if(a2 != 0) {
        allocateUiElement(0x56, 0x56, 16, 94);
      }
    }

    //LAB_80108a50
  }

  @Method(0x80108a6cL)
  public static void renderSaveGameSlot(final int fileIndex, final int y, final long a3) {
    final SavedGameDisplayData saveData = saves.get(fileIndex).b();

    if((a3 & 0xffL) != 0) {
      renderTwoDigitNumber(21, y, fileIndex + 1); // File number
    }

    //LAB_80108b3c
    final ArrayRef<Pointer<LodString>> locationNames;
    if(saveData.saveType == 1) {
      //LAB_80108b5c
      locationNames = worldMapNames_8011c1ec;
    } else if(saveData.saveType == 3) {
      //LAB_80108b78
      locationNames = chapterNames_80114248;
    } else {
      //LAB_80108b90
      locationNames = submapNames_8011c108;
    }

    //LAB_80108ba0
    renderCentredText(locationNames.get(saveData.locationIndex).deref(), 278, y + 47, 0x4L); // Location text

    if((a3 & 0xffL) != 0) {
      allocateUiElement(0x4c, 0x4c,  16, y)._3c.set(0x21); // Left half of border
      allocateUiElement(0x4d, 0x4d, 192, y)._3c.set(0x21); // Right half of border

      // Load char 0
      if(saveData.char0Index >= 0 && saveData.char0Index < 9) {
        final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
        initGlyph(struct, glyph_801142d4);
        struct.glyph_04.set(saveData.char0Index);
        struct.tpage_2c.add(0x1L);
        struct._3c.set(0x21);
        struct.x_40.set(38);
        struct.y_44.set(y + 8);
      }

      // Load char 1
      //LAB_80108c78
      if(saveData.char1Index >= 0 && saveData.char1Index < 9) {
        final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
        initGlyph(struct, glyph_801142d4);
        struct.glyph_04.set(saveData.char1Index);
        struct.tpage_2c.add(0x1L);
        struct._3c.set(0x21);
        struct.x_40.set(90);
        struct.y_44.set(y + 8);
      }

      // Load char 2
      //LAB_80108cd4
      if(saveData.char2Index >= 0 && saveData.char2Index < 9) {
        final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
        initGlyph(struct, glyph_801142d4);
        struct.glyph_04.set(saveData.char2Index);
        struct.tpage_2c.add(0x1L);
        struct._3c.set(0x21);
        struct.x_40.set(142);
        struct.y_44.set(y + 8);
      }

      //LAB_80108d30
      renderTwoDigitNumber(224, y + 6, saveData.level); // Level
      renderTwoDigitNumber(269, y + 6, saveData.dlevel); // Dragoon level
      renderFourDigitNumber(302, y + 6, saveData.currentHp); // Current HP
      renderFourDigitNumber(332, y + 6, saveData.maxHp); // Max HP
      renderEightDigitNumber(245, y + 17, saveData.gold, 0); // Gold
      renderThreeDigitNumber(306, y + 17, getTimestampPart(saveData.time, 0), 0x1L); // Time played hour
      renderCharacter(324, y + 17, 10); // Hour-minute colon
      renderTwoDigitNumber(330, y + 17, getTimestampPart(saveData.time, 1), 0x1L); // Time played minute
      renderCharacter(342, y + 17, 10); // Minute-second colon
      renderTwoDigitNumber(348, y + 17, getTimestampPart(saveData.time, 2), 0x1L); // Time played second
      renderTwoDigitNumber(344, y + 34, saveData.stardust); // Stardust
      renderDragoonSpirits(saveData.dragoonSpirits, 223, y + 27);
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
      allocateUiElement(0x59, 0x59, 194, 16);

      if(charData.equipment_14.get(0).get() != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14.get(0).get()), 202, 17, 0);
      }

      //LAB_80108ee4
      if(charData.equipment_14.get(1).get() != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14.get(1).get()), 202, 31, 0);
      }

      //LAB_80108f10
      if(charData.equipment_14.get(2).get() != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14.get(2).get()), 202, 45, 0);
      }

      //LAB_80108f3c
      if(charData.equipment_14.get(3).get() != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14.get(3).get()), 202, 59, 0);
      }

      //LAB_80108f68
      if(charData.equipment_14.get(4).get() != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14.get(4).get()), 202, 73, 0);
      }
    }

    //LAB_80108f94
    //LAB_80108f98
    renderText(equipment_8011972c.get(charData.equipment_14.get(0).get()).deref(), 220, 19, 0x4L);
    renderText(equipment_8011972c.get(charData.equipment_14.get(1).get()).deref(), 220, 33, 0x4L);
    renderText(equipment_8011972c.get(charData.equipment_14.get(2).get()).deref(), 220, 47, 0x4L);
    renderText(equipment_8011972c.get(charData.equipment_14.get(3).get()).deref(), 220, 61, 0x4L);
    renderText(equipment_8011972c.get(charData.equipment_14.get(4).get()).deref(), 220, 75, 0x4L);

    //LAB_8010905c
  }

  @Method(0x80109074L)
  public static void renderString(final int stringType, final int x, final int y, final int stringIndex, final long a4) {
    if(a4 != 0) {
      allocateUiElement(0x5b, 0x5b, x, y);
    }

    //LAB_801090e0
    LodString s0 = null;
    if(stringType == 0) {
      //LAB_80109118
      if(stringIndex == 0xff) {
        return;
      }

      s0 = _80117a10.get(stringIndex).deref();
    } else if(stringType == 0x1L) {
      //LAB_8010912c
      if(stringIndex >= 0xff) {
        //LAB_80109140
        s0 = _8011c254;
      } else {
        //LAB_80109154
        s0 = _8011b75c.get(stringIndex).deref();
      }
      //LAB_80109108
    } else if(stringType == 0x2L) {
      //LAB_8010914c
      s0 = switch(stringIndex) {
        case 0 -> new LodString("Send gold and items\nDabas has found to\nthe main game.");
        case 1 -> new LodString("Delete items from\nthe Pocket Station.");
        case 2 -> new LodString("Leave for the\nnext adventure.");
        default -> null;
      };
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
      allocateUiElement(0x58, 0x58, 194, 101);
    }

    //LAB_80109308
    if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) == 0) {
      return;
    }

    final byte[] spellIndices = new byte[8];
    getUnlockedDragoonSpells(spellIndices, charIndex);
    final int unlockedSpellCount = getUnlockedSpellCount(charIndex);

    //LAB_80109354
    for(int i = 0; i < 4; i++) {
      if(a1 != 0 && i < unlockedSpellCount) {
        renderCharacter(200, 127 + i * 14, i + 1);
      }

      //LAB_80109370
      final byte spellIndex = spellIndices[i];
      if(spellIndex != -1) {
        renderText(spells_80052734.get(spellIndex).deref(), 210, 125 + i * 14, 0x4L);

        if(a1 != 0) {
          renderThreeDigitNumber(342, 128 + i * 14, (int)_80114290.offset(spellIndex).get());
        }
      }

      //LAB_801093c4
    }

    //LAB_801093e0
  }

  @Method(0x80109410L)
  public static void renderMenuItems(final int x, final int y, final ArrayRef<MenuItemStruct04> menuItems, final int slotScroll, final int itemCount, @Nullable final Renderable58 a5, @Nullable final Renderable58 a6) {
    int s3 = slotScroll;

    //LAB_8010947c
    int i;
    MenuItemStruct04 menuItem;
    for(i = 0, menuItem = menuItems.get(s3); i < itemCount && menuItem.itemId_00.get() != 0xff; i++, menuItem = menuItems.get(s3)) {
      //LAB_801094ac
      renderText(equipment_8011972c.get(menuItem.itemId_00.get()).deref(), x + 21, y + FUN_800fc814(i) + 2, (menuItem.price_02.get() & 0x6000) == 0 ? 0x4L : 0x6L);
      renderItemIcon(getItemIcon(menuItem.itemId_00.get()), x + 4, y + FUN_800fc814(i), 0x8L);

      final int s0 = menuItem.price_02.get();
      if((s0 & 0x1000L) != 0) {
        renderItemIcon(48 | s0 & 0xf, x + 148, y + FUN_800fc814(i) - 1, 0x8L).clut_30.set(s0 + 0x1f4L << 6 | 0x2bL);
        //LAB_80109574
      } else if((s0 & 0x2000L) != 0) {
        renderItemIcon(58, x + 148, y + FUN_800fc814(i) - 1, 0x8L).clut_30.set(0x7eaaL);
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
      if(menuItems.get(i + slotScroll).itemId_00.get() != 0xff) {
        a6.flags_00.and(0xffff_ffbfL);
      } else {
        a6.flags_00.or(0x40L);
      }
    }
  }

  @Method(0x8010965cL)
  public static void FUN_8010965c(final int slotScroll, @Nullable final Renderable58 a1, @Nullable final Renderable58 a2) {
    //LAB_801096c8
    int i;
    for(i = 0; i < 14 && menuItems_8011d7c8.get(slotScroll + i).itemId_00.get() != 0xff; i += 2) {
      renderText(_8011c008.get(menuItems_8011d7c8.get(slotScroll + i).itemId_00.get()).deref(), 37, FUN_800fc814(i / 2) + 34, 0x4L);

      if(menuItems_8011d7c8.get(slotScroll + i + 1).itemId_00.get() != 0xff) {
        renderText(_8011c008.get(menuItems_8011d7c8.get(slotScroll + i + 1).itemId_00.get()).deref(), 214, FUN_800fc814(i / 2) + 34, 0x4L);
      }
    }

    //LAB_8010977c
    //LAB_80109790
    //LAB_8010979c
    if(a1 != null) { // There was an NPE here
      if(slotScroll != 0) {
        a1.flags_00.and(0xffff_ffbfL);
      } else {
        a1.flags_00.or(0x40L);
      }
    }

    //LAB_801097d8
    //LAB_801097ec
    if(a2 != null) { // There was an NPE here
      if(menuItems_8011d7c8.get(slotScroll + i).itemId_00.get() != 0xff) {
        a2.flags_00.and(0xffff_ffbfL);
      } else {
        a2.flags_00.or(0x40L);
      }
    }
  }

  @Method(0x80109820L)
  public static Renderable58 FUN_80109820(final int x, final int y, final int glyph) {
    if(glyph >= 0x9L) {
      //LAB_801098a0
      return null;
    }

    final Renderable58 s0 = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
    initGlyph(s0, glyph_801142d4);
    s0.tpage_2c.add(0x1L);
    s0.glyph_04.set(glyph);
    s0._3c.set(0x21);
    s0.x_40.set(x);
    s0.y_44.set(y);

    //LAB_801098a4
    return s0;
  }

  @Method(0x801098c0L)
  public static void renderDragoonSpirits(final int spirits, final int x, final int y) {
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
      if((spirits & 0x1L << (v0 & 0x1fL)) != 0) {
        final Renderable58 struct = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);

        final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(6);
        final MenuGlyph06 glyph = sp0x10tmp.get().cast(MenuGlyph06::new);
        glyph.glyph_00.set(i + 0xd);
        glyph.x_02.set((short)(x + i * 12));
        glyph.y_04.set((short)y);
        initGlyph(struct, glyph);
        sp0x10tmp.release();

        struct._3c.set(0x21);
      }

      //LAB_801099a0
    }

    tmp.release();
  }

  @Method(0x8010a0ecL)
  public static void loadSaveFile(final int saveSlot) {
    final byte[] data = SaveManager.loadGame(saves.get(saveSlot).a());
    MEMORY.setBytes(gameState_800babc8.getAddress(), data, 0x200, 0x52c);
  }

  @Method(0x8010a248L)
  public static void FUN_8010a248(final long a0, long a1) {
    if(a1 > 99) {
      a1 = 99;
    }

    //LAB_8010a258
    MEMORY.ref(1, a0).offset(0x3L).setu(a1 % 10 + 79);
    MEMORY.ref(1, a0).offset(0x1L).setu(a1 / 10 + 79); //TODO it's possible this is wrong, there was more math here but I'm pretty sure it worked out to 0
  }

  @Method(0x8010a2b0L)
  public static void FUN_8010a2b0(final long a0, final long a1) {
    long a2 = a1;
    if(a2 > 999) {
      a2 = 999;
    }

    //LAB_8010a2c4
    MEMORY.ref(1, a0).offset(0x3L).setu(a2 / 10 - a1 / 100 * 10 + 79);
    MEMORY.ref(1, a0).offset(0x5L).setu(a2 % 10 + 79);
    MEMORY.ref(1, a0).offset(0x1L).setu(a1 / 100 % 10 + 79);
  }

  @Method(0x8010a344L)
  public static void saveGame(final long slot) {
    //LAB_8010a3f0
    //LAB_8010a3f4
    //LAB_8010a424
    final Memory.TemporaryReservation sp0x48tmp = MEMORY.temp(0x4c);
    final Value sp0x48 = sp0x48tmp.get();
    memcpy(sp0x48.getAddress(), _800fbb44.getAddress(), 0x3d);

    final long s3 = mallocTail(0x2000L);
    final long s4 = mallocTail(0x680L);
    memcpy(s4, gameState_800babc8.getAddress(), 0x52c);
    MEMORY.ref(1, s3).offset(0x00L).setu(0x53L);
    MEMORY.ref(1, s3).offset(0x01L).setu(0x43L);
    MEMORY.ref(1, s3).offset(0x02L).setu(0x11L);
    MEMORY.ref(1, s3).offset(0x03L).setu(0x01L);
    MEMORY.ref(2, s3).offset(0x50L).setu(0x01L);
    MEMORY.ref(1, s3).offset(0x53L).setu(0x52L);
    MEMORY.ref(1, s3).offset(0x54L).setu(0x44L);
    MEMORY.ref(1, s3).offset(0x52L).setu(0x43L);
    MEMORY.ref(1, s3).offset(0x55L).setu(0x30L);
    MEMORY.ref(1, s3).offset(0x57L).setu(0);
    MEMORY.ref(1, s3).offset(0x56L).setu(0);

    //LAB_8010a4ec
    for(int i = 0; i < 8; i++) {
      MEMORY.ref(1, s3).offset(0x58L).offset(i).setu(0);
    }

    //LAB_8010a504
    for(int i = 0; i < 12; i++) {
      MEMORY.ref(1, s3).offset(0x44L).offset(i).setu(0);
    }

    FUN_8010a248(sp0x48.offset(0x10L).getAddress(), slot + 0x1L);
    FUN_8010a248(sp0x48.offset(0x1aL).getAddress(), gameState_800babc8.charData_32c.get(0).level_12.get());
    FUN_8010a2b0(sp0x48.offset(0x2aL).getAddress(), getTimestampPart(gameState_800babc8.timestamp_a0.get(), 0));
    FUN_8010a248(sp0x48.offset(0x32L).getAddress(), getTimestampPart(gameState_800babc8.timestamp_a0.get(), 1));
    FUN_8010a248(sp0x48.offset(0x48L).getAddress(), getTimestampPart(gameState_800babc8.timestamp_a0.get(), 2));
    final long s7 = tempSaveData_8011dcc0.getAddress();
    MEMORY.ref(4, s7).offset(0x20L).setu(gameState_800babc8.timestamp_a0.get());
    strcpy(MEMORY.ref(0x3d, s3 + 0x4L, CString::new), sp0x48.offset(1, 0x0L).getString());
    memcpy(s3 + 0x060L, drgn0_6666FilePtr_800bdc3c.getPointer() + 0xdf30L + slot * 0x20, 0x20); //TODO
    memcpy(s3 + 0x080L, drgn0_6666FilePtr_800bdc3c.getPointer() + 0xe11cL + slot * 0x80, 0x80);
    memcpy(s3 + 0x100L, drgn0_6666FilePtr_800bdc3c.getPointer() + 0xe91cL + slot * 0x80, 0x80);

    if(_800bdc40.get() != 0) {
      throw new RuntimeException("Dev code?");
    }

    //LAB_8010a6a8
    MEMORY.ref(4, s4).offset(0x4L).setu(0);

    //LAB_8010a6b8
    long a1_0 = 0;
    for(int i = 0; i < 0x14b; i++) {
      a1_0 = a1_0 + MEMORY.ref(4, s4).offset(i * 0x4L).get();
    }

    MEMORY.ref(4, s4).offset(0x4L).setu(a1_0);

    final long s0 = tempSaveData_8011dcc0.getAddress();

    final byte[] data = new byte[0x780];
    MEMORY.getBytes(s3, data,     0, 0x180);
    MEMORY.getBytes(s0, data, 0x180, 0x080);
    MEMORY.getBytes(s4, data, 0x200, 0x580);

    if(slot < saves.size()) {
      SaveManager.overwriteSave(saves.get((int)slot).a(), data);
    } else {
      SaveManager.newSave(data);
    }

    //LAB_8010a7a8
    //LAB_8010a7ac
    free(s3);
    free(s4);

    sp0x48tmp.release();
  }

  @Method(0x8010a7fcL)
  public static int getShopMenuYOffset(final int index) {
    return index * 16 + 58;
  }

  @Method(0x8010a808L)
  public static int FUN_8010a808(final int index) {
    return index * 17 + 18;
  }

  @Method(0x8010a818L)
  public static int FUN_8010a818(final int index) {
    return index * 50 + 17;
  }

  @Method(0x8010a834L)
  public static int FUN_8010a834(final int index) {
    return index * 17 + 126;
  }

  @Method(0x8010a844L)
  public static void FUN_8010a844(final InventoryMenuState nextMenuState, final long a1) {
    inventoryMenuState_800bdc28.set(InventoryMenuState._16);
    _800bdc2c.setu(a1);
    confirmDest_800bdc30.set(nextMenuState);
  }

  @Method(0x8010a864L)
  public static int FUN_8010a864(final int equipmentId) {
    int s3 = -1;

    //LAB_8010a8a4
    for(int i = 0; i < 7; i++) {
      if(characterIndices_800bdbb8.get(i).get() != -1) {
        characterRenderables_8011e148.get(i).deref().y_44.set(174);

        if(equipmentId != 0xff) {
          if(FUN_801039a0(equipmentId, characterIndices_800bdbb8.get(i).get()) == 0) {
            characterRenderables_8011e148.get(i).deref().y_44.set(250);
            //LAB_8010a8f0
          } else if(s3 == -1) {
            s3 = i;
          }
        }
      }

      //LAB_8010a8fc
    }

    if(s3 == -1) {
      s3 = 0;
    }

    //LAB_8010a924
    return s3;
  }

  @Method(0x8010a948L)
  public static void FUN_8010a948() {
    final long v0;
    final long v1;

    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    LOGGER.error("%s %s", inventoryMenuState_800bdc28.get(), inventoryJoypadInput_800bdc44.get());

    switch(inventoryMenuState_800bdc28.get()) {
      case INIT_0 -> {
        renderablePtr_800bdc5c.clear();
        drgn0_6666FilePtr_800bdc3c.clear();
        setWidthAndFlags(384, 0);
        loadDrgnBinFile(0, 6665, 0, getMethodAddress(SItem.class, "menuAssetsLoaded", long.class, long.class, long.class), 0, 5);
        loadDrgnBinFile(0, 6666, 0, getMethodAddress(SItem.class, "menuAssetsLoaded", long.class, long.class, long.class), 1, 3);
        loadCharacterStats(0);
        _800bdf00.set(0x21L);
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        menuIndex_8011e0dc.set(0);
        menuIndex_8011e0e0.set(0);
        menuScroll_8011e0e4.set(0);
      }

      case AWAIT_INIT_1 -> {
        if(!drgn0_6666FilePtr_800bdc3c.isNull()) {
          scriptStartEffect(2, 10);
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
        }
      }

      case _2 -> {
        deallocateRenderables(0xffL);
        renderGlyphs(glyphs_80114510, 0, 0);
        selectedMenuOptionRenderablePtr_800bdbe0.set(allocateUiElement(0x7a, 0x7a, 49, getShopMenuYOffset(menuIndex_8011e0dc.get())));
        FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe0.deref());
        currentShopItemCount_8011e13c.set(0);

        //LAB_8010ab00
        for(int i = 0; i < 16; i++) {
          final int menuItemIndex = currentShopItemCount_8011e13c.get();
          final int itemId = shops_800f4930.get(shopId_8007a3b4.get()).item_00.get(menuItemIndex).id_01.get();

          if(itemId != 0xff) {
            final MenuItemStruct04 menuItem = menuItems_8011e0f8.get(menuItemIndex);
            menuItem.itemId_00.set(itemId);
            menuItem.price_02.set((int)(itemPrices_80114310.get(itemId).get() * 0x2L));
            currentShopItemCount_8011e13c.incr();
          } else {
            //LAB_8010ab6c
            final MenuItemStruct04 menuItem = menuItems_8011e0f8.get(i);
            menuItem.itemId_00.set(0xff);
            menuItem.price_02.set(0);
          }
        }

        final MenuItemStruct04 menuItem = menuItems_8011e0f8.get(16);
        menuItem.itemId_00.set(0xff);
        menuItem.price_02.set(0);
        recalcInventory();
        FUN_80103b10();

        //LAB_8010abc4
        for(int charSlot = 0; charSlot < characterCount_8011d7c4.get(); charSlot++) {
          characterRenderables_8011e148.get(charSlot).set(FUN_80109820(FUN_8010a818(charSlot), 174, characterIndices_800bdbb8.get(charSlot).get()));
        }

        //LAB_8010ac00
        shopType_8011e13d.setu(shops_800f4930.get(shopId_8007a3b4.get()).shopType_00.get() & 0x1L);
        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
        inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_MAIN_MENU_3);
      }

      case INIT_MAIN_MENU_3 -> {
        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());

        if(_800bb168.get() == 0) {
          if((inventoryJoypadInput_800bdc44.get() & 0x40) != 0) {
            playSound(3);
            FUN_8010a844(InventoryMenuState._19, 0x1L);
          }

          //LAB_8010acac
          if(handleMenuUpDown(menuIndex_8011e0dc, 4)) {
            menuScroll_8011e0e4.set(0);
            menuIndex_8011e0e0.set(0);
            selectedMenuOptionRenderablePtr_800bdbe0.deref().y_44.set(getShopMenuYOffset(menuIndex_8011e0dc.get()));
          }

          //LAB_8010ace4
          if((inventoryJoypadInput_800bdc44.get() & 0x20) != 0) {
            playSound(2);

            shopType_8011e13d.setu(shops_800f4930.get(shopId_8007a3b4.get()).shopType_00.get() & 0x1L);

            v1 = menuIndex_8011e0dc.get();
            if(v1 == 0) {
              //LAB_8010ad64
              selectedMenuOptionRenderablePtr_800bdbe4.set(allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(menuIndex_8011e0e0.get())));
              FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe4.deref());

              if(shopType_8011e13d.get() == 0) {
                menuIndex_8011e0d8.set(FUN_8010a864(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get()));
                renderablePtr_800bdbe8.set(allocateUiElement(0x83, 0x83, FUN_8010a818(menuIndex_8011e0d8.get()), 174));
                FUN_80104b60(renderablePtr_800bdbe8.deref());
              }

              //LAB_8010ae00
              renderable_8011e0f0.set(allocateUiElement(0x3d, 0x44, 358, FUN_8010a808(0)));
              renderable_8011e0f4.set(allocateUiElement(0x35, 0x3c, 358, FUN_8010a808(5)));
              inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
            } else if(v1 == 0x1L) {
              //LAB_8010ae58
              menuOption_8011e0e8.set(0);
              renderable_8011e0f0.set(allocateUiElement(0x3d, 0x44, 358, FUN_8010a808(0)));
              renderable_8011e0f4.set(allocateUiElement(0x35, 0x3c, 358, FUN_8010a808(5)));
              inventoryMenuState_800bdc28.set(InventoryMenuState._8);
              //LAB_8010ad48
            } else if(v1 == 0x2L) {
              //LAB_8010aeb8
              FUN_8010a844(InventoryMenuState._18, 0x1L);
            } else if(v1 == 0x3L) {
              FUN_8010a844(InventoryMenuState._19, 0x1L);
            }
          }
        }
      }

      case MAIN_MENU_4 -> {
        if(shopType_8011e13d.get() == 0) {
          if(handleMenuLeftRight(menuIndex_8011e0d8, 7)) {
            renderablePtr_800bdbe8.deref().x_40.set(FUN_8010a818(menuIndex_8011e0d8.get()));
          }

          //LAB_8010af18
          FUN_8010c8e4(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get(), characterIndices_800bdbb8.get(menuIndex_8011e0d8.get()).get());
        } else {
          //LAB_8010af64
          FUN_8010cb80(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get());
        }

        //LAB_8010af94
        renderString(0, 16, 122, menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get(), 0);

        if((inventoryJoypadInput_800bdc44.get() & 0x40) != 0) {
          playSound(3);
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
        }

        //LAB_8010aff8
        if((inventoryJoypadInput_800bdc44.get() & 0x20) != 0) {
          if(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get() == 0xff) {
            playSound(0x28);
          } else {
            //LAB_8010b044
            playSound(2);
            menuOption_8011e0ec.set(0);
            renderablePtr_800bdbf0.set(allocateUiElement(0x7d, 0x7d, 132, FUN_8010a834(0)));
            FUN_80104b60(renderablePtr_800bdbf0.deref());

            if(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get() < 0xc0) {
              v0 = gameState_800babc8.equipmentCount_1e4.get() < 0xff ? 1 : 0;
            } else {
              //LAB_8010b0bc
              v0 = gameState_800babc8.itemCount_1e6.get() < 0x20 ? 1 : 0;
            }

            //LAB_8010b0cc
            inventoryMenuState_800bdc28.set(v0 != 0 ? InventoryMenuState._5 : InventoryMenuState._6);

            if(gameState_800babc8.gold_94.get() < menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).price_02.get()) {
              inventoryMenuState_800bdc28.set(InventoryMenuState._7);
            }
          }
        }

        //LAB_8010b124
        //LAB_8010b128
        if(scrollMenu(menuIndex_8011e0e0, menuScroll_8011e0e4, 6, currentShopItemCount_8011e13c.get(), 1)) {
          selectedMenuOptionRenderablePtr_800bdbe4.deref().y_44.set(FUN_8010a808(menuIndex_8011e0e0.get()));

          if(shopType_8011e13d.get() == 0) {
            menuIndex_8011e0d8.set(FUN_8010a864(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get()));
            renderablePtr_800bdbe8.deref().x_40.set(FUN_8010a818(menuIndex_8011e0d8.get()));
          }
        }

        //LAB_8010b1c4
        //LAB_8010b1c8
        FUN_8010c458(menuItems_8011e0f8.getAddress(), menuScroll_8011e0e4.get(), renderable_8011e0f0.deref(), renderable_8011e0f4.deref());
        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
      }

      case _5 -> {
        renderText(Are_you_sure_you_want_to_buy_8011c3ec, 16, 128, 0x4L);
        renderCentredText(Yes_8011c20c, 148, FUN_8010a834(0) + 2, menuOption_8011e0ec.get() == 0 ? 0x5L : 0x4L);
        renderCentredText(No_8011c214, 148, FUN_8010a834(1) + 2, menuOption_8011e0ec.get() == 0 ? 0x4L : 0x5L);

        switch(handleYesNo(menuOption_8011e0ec)) {
          case SCROLLED ->
            //LAB_8010b2bc
            renderablePtr_800bdbf0.deref().y_44.set(FUN_8010a834(menuOption_8011e0ec.get()));

          case YES -> {
            //LAB_8010b2d8
            gameState_800babc8.gold_94.sub(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).price_02.get());
            unloadRenderable(renderablePtr_800bdbf0.deref());

            if(shopType_8011e13d.get() != 0) {
              giveItem(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get());

              //LAB_8010b378
              inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
            } else {
              //LAB_8010b360
              inventoryMenuState_800bdc28.set(InventoryMenuState._13);
            }

            //LAB_8010b37c
            //LAB_8010b2a8
          }

          case NO, CANCELLED -> {
            //LAB_8010b368
            unloadRenderable(renderablePtr_800bdbf0.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
          }
        }

        //LAB_8010b380
        //LAB_8010b384
        if(shopType_8011e13d.get() == 0) {
          if(handleMenuLeftRight(menuIndex_8011e0d8, 7)) {
            renderablePtr_800bdbe8.deref().x_40.set(FUN_8010a818(menuIndex_8011e0d8.get()));
          }

          //LAB_8010b3cc
          FUN_8010c8e4(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get(), characterIndices_800bdbb8.get(menuIndex_8011e0d8.get()).get());
        } else {
          //LAB_8010b418
          FUN_8010cb80(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get());
        }

        //LAB_8010b448
        FUN_8010c458(menuItems_8011e0f8.getAddress(), menuScroll_8011e0e4.get(), renderable_8011e0f0.deref(), renderable_8011e0f4.deref());
        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
      }

      case _6, _7 -> {
        //LAB_8010b490
        //LAB_8010b498
        renderText(inventoryMenuState_800bdc28.get() == InventoryMenuState._6 ? Cannot_carry_anymore_8011c43c : Not_enough_money_8011c468, 16, 128, 0x4L);
        renderCentredText(Conf_8011c48c, 148, FUN_8010a834(0) + 2, 0x5L);
        renderablePtr_800bdbf0.deref().y_44.set(FUN_8010a834(0));

        if((inventoryJoypadInput_800bdc44.get() & 0x60) != 0) {
          playSound(2);
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
        }

        //LAB_8010b508
        FUN_8010c458(menuItems_8011e0f8.getAddress(), menuScroll_8011e0e4.get(), renderable_8011e0f0.deref(), renderable_8011e0f4.deref());
        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
      }

      case _8 -> {
        renderablePtr_800bdbec.set(allocateUiElement(0x7d, 0x7d, 132, FUN_8010a834(menuOption_8011e0e8.get())));
        FUN_80104b60(renderablePtr_800bdbec.deref());
        renderable_8011e0f0.deref().flags_00.or(0x40L);
        renderable_8011e0f4.deref().flags_00.or(0x40L);
        FUN_8010a864(0xff);
        inventoryMenuState_800bdc28.set(InventoryMenuState._9);
        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
      }

      case _9 -> {
        renderText(What_do_you_want_to_sell_8011c498, 16, 128, 0x4L);
        renderCentredText(Armed_8011c4cc, 148, FUN_8010a834(0) + 2, menuOption_8011e0e8.get() == 0 ? 0x5L : 0x4L);
        renderCentredText(item_8011c4d8, 148, FUN_8010a834(1) + 2, menuOption_8011e0e8.get() == 0 ? 0x4L : 0x5L);

        switch(handleYesNo(menuOption_8011e0e8)) {
          case SCROLLED -> {
            //LAB_8010b69c
            renderablePtr_800bdbec.deref().y_44.set(FUN_8010a834(menuOption_8011e0e8.get()));
          }

          case YES -> {
            //LAB_8010b6b8
            menuIndex_8011e0e0.set(0);
            menuScroll_8011e0e4.set(0);
            _8011e13e.setu(0);

            if(gameState_800babc8.equipmentCount_1e4.get() != 0) {
              inventoryMenuState_800bdc28.set(InventoryMenuState._10);
              selectedMenuOptionRenderablePtr_800bdbe4.set(allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(0)));
              FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe4.deref());
              unloadRenderable(renderablePtr_800bdbec.deref());
              FUN_8010a864(gameState_800babc8.equipment_1e8.get(0).get());
            } else {
              //LAB_8010b7b4
              menuOption_8011e0e8.set(0);
              inventoryMenuState_800bdc28.set(InventoryMenuState._12);
            }
          }

          //LAB_8010b680
          case NO -> {
            //LAB_8010b73c
            _8011e13e.setu(0x1L);
            menuScroll_8011e0e4.set(0);
            menuIndex_8011e0e0.set(0);

            if(gameState_800babc8.itemCount_1e6.get() != 0) {
              inventoryMenuState_800bdc28.set(InventoryMenuState._10);
              selectedMenuOptionRenderablePtr_800bdbe4.set(allocateUiElement(0x7b, 0x7b, 170, FUN_8010a808(0)));
              FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe4.deref());
              unloadRenderable(renderablePtr_800bdbec.deref());
            } else {
              //LAB_8010b7b4
              menuOption_8011e0e8.set(0);
              inventoryMenuState_800bdc28.set(InventoryMenuState._12);
            }
          }

          case CANCELLED -> {
            //LAB_8010b7c8
            inventoryMenuState_800bdc28.set(InventoryMenuState._2);
          }
        }

        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
      }

      case _10 -> {
        if(_8011e13e.get() == 0) {
          //LAB_8010b868
          renderText(Which_weapon_do_you_want_to_sell_8011c524, 16, 128, 0x4L);
          renderString(0, 193, 122, gameState_800babc8.equipment_1e8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).get(), 0);

          if(scrollMenu(menuIndex_8011e0e0, menuScroll_8011e0e4, 6, gameState_800babc8.equipmentCount_1e4.get(), 1)) {
            FUN_8010a864(gameState_800babc8.equipment_1e8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).get());
            selectedMenuOptionRenderablePtr_800bdbe4.deref().y_44.set(FUN_8010a808(menuIndex_8011e0e0.get()));
          }
        } else {
          renderText(Which_item_do_you_want_to_sell_8011c4e4, 16, 128, 0x4L);
          renderString(0, 193, 122, gameState_800babc8.items_2e9.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).get(), 0);

          if(scrollMenu(menuIndex_8011e0e0, menuScroll_8011e0e4, 6, gameState_800babc8.itemCount_1e6.get(), 1)) {
            selectedMenuOptionRenderablePtr_800bdbe4.deref().y_44.set(FUN_8010a808(menuIndex_8011e0e0.get()));
          }
        }

        //LAB_8010b918
        if((inventoryJoypadInput_800bdc44.get() & 0x40) != 0) {
          playSound(3);
          unloadRenderable(selectedMenuOptionRenderablePtr_800bdbe4.deref());
          inventoryMenuState_800bdc28.set(InventoryMenuState._8);
        }

        //LAB_8010b954
        renderItemList(menuScroll_8011e0e4.get(), _8011e13e.get(), renderable_8011e0f0.deref(), renderable_8011e0f4.deref());
        renderShopMenu(menuIndex_8011e0dc.get(), _8011e13e.get());

        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          //LAB_8010b9e8
          v0 = menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get();
          //TODO not sure if this condition is right
          if(_8011e13e.get() != 0 && gameState_800babc8.items_2e9.get((int)v0).get() == 0xffL || _8011e13e.get() == 0 && (gameState_800babc8.equipment_1e8.get((int)v0).get() == 0xffL || FUN_80022898(gameState_800babc8.equipment_1e8.get((int)v0).get()) != 0)) {
            //LAB_8010ba28
            playSound(0x28);
          } else {
            //LAB_8010ba38
            playSound(2);
            menuOption_8011e0ec.set(0);
            renderablePtr_800bdbf0.set(allocateUiElement(0x7d, 0x7d, 132, FUN_8010a834(0)));
            FUN_80104b60(renderablePtr_800bdbf0.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState._11);
          }
        }
      }

      case _11 -> {
        renderText(Are_you_sure_you_want_to_sell_8011c568, 16, 128, 0x4L);
        renderCentredText(Yes_8011c20c, 148, FUN_8010a834(0) + 2, menuOption_8011e0ec.get() == 0 ? 0x5L : 0x4L);
        renderCentredText(No_8011c214, 148, FUN_8010a834(1) + 2, menuOption_8011e0ec.get() == 0 ? 0x4L : 0x5L);

        switch(handleYesNo(menuOption_8011e0ec)) {
          case SCROLLED ->
            //LAB_8010bb50
            renderablePtr_800bdbf0.deref().y_44.set(FUN_8010a834(menuOption_8011e0ec.get()));

          case YES -> {
            //LAB_8010bb6c
            final int itemId;
            if(_8011e13e.get() != 0) {
              itemId = gameState_800babc8.items_2e9.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).get();
              v0 = takeItem(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get());
            } else {
              //LAB_8010bbc0
              itemId = gameState_800babc8.equipment_1e8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).get();
              v0 = takeEquipment(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get());
            }

            //LAB_8010bbfc
            if(v0 == 0) {
              addGold(itemPrices_80114310.get(itemId).get());
            }

            //LAB_8010bc1c
            unloadRenderable(selectedMenuOptionRenderablePtr_800bdbe4.deref());
            unloadRenderable(renderablePtr_800bdbf0.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState._8);
            //LAB_8010bb3c
          }

          case NO, CANCELLED -> {
            //LAB_8010bc48
            unloadRenderable(renderablePtr_800bdbf0.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState._10);
          }
        }

        //LAB_8010bcf8
        //LAB_8010bcfc
        //LAB_8010bd00
        renderItemList(menuScroll_8011e0e4.get(), _8011e13e.get(), renderable_8011e0f0.deref(), renderable_8011e0f4.deref());
        renderShopMenu(menuIndex_8011e0dc.get(), _8011e13e.get());
      }

      case _12 -> {
        renderCentredText(Conf_8011c48c, 148, FUN_8010a834(0) + 2, 0x5L);
        renderablePtr_800bdbec.deref().y_44.set(FUN_8010a834(0));

        //LAB_8010bcb4
        //LAB_8010bcbc
        renderText(_8011e13e.get() != 0 ? No_item_to_sell_8011c5dc : No_weapon_to_sell_8011c5fc, 16, 128, 0x4L);

        if((inventoryJoypadInput_800bdc44.get() & 0x60) != 0) {
          playSound(2);

          //LAB_8010bcf4
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
        }

        //LAB_8010bcf8
        //LAB_8010bcfc
        //LAB_8010bd00
        renderItemList(menuScroll_8011e0e4.get(), _8011e13e.get(), renderable_8011e0f0.deref(), renderable_8011e0f4.deref());
        renderShopMenu(menuIndex_8011e0dc.get(), _8011e13e.get());
      }

      case _13, _14 -> {
        if(inventoryMenuState_800bdc28.get() == InventoryMenuState._13) {
          menuOption_8011e0ec.set(0);
          renderablePtr_800bdbf0.set(allocateUiElement(0x7d, 0x7d, 132, FUN_8010a834(0)));
          FUN_80104b60(renderablePtr_800bdbf0.deref());
          inventoryMenuState_800bdc28.set(InventoryMenuState._14);
        }

        FUN_8010c8e4(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get(), characterIndices_800bdbb8.get(menuIndex_8011e0d8.get()).get());

        if(handleMenuLeftRight(menuIndex_8011e0d8, 7)) {
          renderablePtr_800bdbe8.deref().x_40.set(FUN_8010a818(menuIndex_8011e0d8.get()));
        }

        //LAB_8010be00
        renderText(Do_you_want_to_be_armed_with_it_8011c620, 16, 128, 0x4L);
        renderCentredText(Yes_8011c20c, 148, FUN_8010a834(0) + 2, menuOption_8011e0ec.get() == 0 ? 0x5L : 0x4L);
        renderCentredText(No_8011c214, 148, FUN_8010a834(1) + 2, menuOption_8011e0ec.get() == 0 ? 0x4L : 0x5L);

        switch(handleYesNo(menuOption_8011e0ec)) {
          case SCROLLED ->
            //LAB_8010becc
            renderablePtr_800bdbf0.deref().y_44.set(FUN_8010a834(menuOption_8011e0ec.get()));

          case YES ->
            //LAB_8010bee8
            inventoryMenuState_800bdc28.set(InventoryMenuState._15);
            //LAB_8010beb8

          case NO, CANCELLED -> {
            //LAB_8010bef4
            unloadRenderable(renderablePtr_800bdbf0.deref());
            giveItem(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get());

            //LAB_8010bf38
            inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
          }
        }

        //LAB_8010bf3c
        //LAB_8010bf40
        FUN_8010c458(menuItems_8011e0f8.getAddress(), menuScroll_8011e0e4.get(), renderable_8011e0f0.deref(), renderable_8011e0f4.deref());
        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
      }

      case _15 -> {
        FUN_8010c8e4(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get(), characterIndices_800bdbb8.get(menuIndex_8011e0d8.get()).get());

        if(handleMenuLeftRight(menuIndex_8011e0d8, 7)) {
          renderablePtr_800bdbe8.deref().x_40.set(FUN_8010a818(menuIndex_8011e0d8.get()));
        }

        //LAB_8010bfe4
        renderCentredText(Conf_8011c48c, 148, FUN_8010a834(0) + 2, 0x5L);
        renderablePtr_800bdbf0.deref().y_44.set(FUN_8010a834(0));

        if(FUN_801039a0(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get(), characterIndices_800bdbb8.get(menuIndex_8011e0d8.get()).get()) == 0) {
          //LAB_8010c0fc
          renderText(Put_in_the_bag_8011c684, 16, 128, 0x4L);

          if((inventoryJoypadInput_800bdc44.get() & 0x60) != 0) {
            playSound(2);

            //LAB_8010c150
            giveItem(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get());
            unloadRenderable(renderablePtr_800bdbf0.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
          }
        } else {
          renderText(characterNames_801142dc.get(characterIndices_800bdbb8.get(menuIndex_8011e0d8.get()).get()).deref(), 24, 128, 0x4L);
          renderText(Is_armed_8011c670, 16, 142, 0x4L);

          if((inventoryJoypadInput_800bdc44.get() & 0x60) != 0) {
            playSound(2);
            giveItem(equipItem(menuItems_8011e0f8.get(menuScroll_8011e0e4.get() + menuIndex_8011e0e0.get()).itemId_00.get(), characterIndices_800bdbb8.get(menuIndex_8011e0d8.get()).get()));
            unloadRenderable(renderablePtr_800bdbf0.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
          }
        }

        //LAB_8010c174
        FUN_8010c458(menuItems_8011e0f8.getAddress(), menuScroll_8011e0e4.get(), renderable_8011e0f0.deref(), renderable_8011e0f4.deref());
        renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
      }

      case _16, _17 -> {
        if(inventoryMenuState_800bdc28.get() == InventoryMenuState._16) {
          scriptStartEffect(1, 10);
          inventoryMenuState_800bdc28.set(InventoryMenuState._17);
        }

        if(_800bb168.get() >= 0xff) {
          inventoryMenuState_800bdc28.set(confirmDest_800bdc30.get());
        }

        //LAB_8010c1e0
        if(_800bdc2c.get() == 0x1L) {
          //LAB_8010c1f0
          //LAB_8010c1f4
          renderShopMenu(menuIndex_8011e0dc.get(), shopType_8011e13d.get());
        }
      }

      case _18 -> {
        inventoryMenuState_800bdc28.set(InventoryMenuState._16);
        whichMenu_800bdc38.setu(0x24L);
      }

      case _19 -> {
        scriptStartEffect(2, 10);
        deallocateRenderables(0xff);
        free(gameOverMcq_800bdc3c.getPointer());
        if(mainCallbackIndex_8004dd20.get() == 0x5L && loadingSmapOvl_8004dd08.get() == 0) {
          FUN_800e3fac();
        }

        //LAB_8010c290
        //LAB_8010c294
        whichMenu_800bdc38.setu(0xaL);
        _800bdf00.setu(0xdL);
      }
    }

    //LAB_8010c2a4
  }

  @Method(0x8010c2c8L)
  public static void renderShopMenu(final int selectedMenuItem, final long a2) {
    renderCentredText(Buy_8011c6a4, 72, getShopMenuYOffset(0) + 2, selectedMenuItem != 0 ? 0x4L : 0x5L);
    renderCentredText(Sell_8011c6ac, 72, getShopMenuYOffset(1) + 2, selectedMenuItem != 1 ? 0x4L : 0x5L);
    renderCentredText(Carried_8011c6b8, 72, getShopMenuYOffset(2) + 2, selectedMenuItem != 2 ? 0x4L : 0x5L);
    renderCentredText(Leave_8011c6c8, 72, getShopMenuYOffset(3) + 2, selectedMenuItem != 3 ? 0x4L : 0x5L);

    if((a2 & 0xffL) != 0) {
      renderTwoDigitNumber(105, 36, gameState_800babc8.itemCount_1e6.get(), 0x2L);
      FUN_801038d4(94, 16, 16);
      renderTwoDigitNumber(123, 36, 32, 0x2L);
    } else {
      //LAB_8010c3e8
      renderThreeDigitNumber(93, 36, gameState_800babc8.equipmentCount_1e4.get(), 0x2L);
      FUN_801038d4(95, 16, 16);
      renderThreeDigitNumber(117, 36, 255, 0x2L);
    }

    //LAB_8010c428
    renderEightDigitNumber(87, 24, gameState_800babc8.gold_94.get(), 0x2L);
    uploadRenderables();
  }

  @Method(0x8010c458L)
  public static void FUN_8010c458(final long a0, final long a1, final Renderable58 a2, final Renderable58 a3) {
    //LAB_8010c4b4
    int i;
    for(i = 0; MEMORY.ref(1, a0).offset((a1 + i) * 0x4L).get() != 0xff; i++) {
      if(i >= 0x6L) {
        break;
      }

      final long s0 = a0 + (a1 + i) * 0x4L;
      renderText(equipment_8011972c.get((int)MEMORY.ref(1, s0).get()).deref(), 168, FUN_8010a808(i) + 2, 0x4L);
      renderFiveDigitNumber(324, FUN_8010a808(i) + 4, (int)MEMORY.ref(2, s0).offset(0x2L).get());
      renderItemIcon(getItemIcon((int)MEMORY.ref(1, s0).get()), 151, FUN_8010a808(i), 0x8L);
    }

    //LAB_8010c558
    //LAB_8010c578
    if(a1 != 0) {
      a2.flags_00.and(0xffff_ffbfL);
    } else {
      //LAB_8010c56c
      a2.flags_00.or(0x40L);
    }

    //LAB_8010c5b0
    if(MEMORY.ref(1, a0).offset((i + a1) * 0x4L).get() != 0xffL) {
      a3.flags_00.and(0xffff_ffbfL);
    } else {
      //LAB_8010c5a4
      a3.flags_00.or(0x40L);
    }
  }

  @Method(0x8010c5e0L)
  public static void renderItemList(final int firstItem, final long a1, final Renderable58 upArrow, final Renderable58 downArrow) {
    if((a1 & 0xff) != 0) {
      //LAB_8010c654
      int i;
      for(i = 0; gameState_800babc8.items_2e9.get(firstItem + i).get() != 0xff && i < 6; i++) {
        final int itemId = gameState_800babc8.items_2e9.get(firstItem + i).get();
        renderItemIcon(getItemIcon(itemId), 151, FUN_8010a808(i), 0x8L);

        //LAB_8010c6b0
        renderText(equipment_8011972c.get(itemId).deref(), 168, FUN_8010a808(i) + 2, FUN_80022898(itemId) == 0 ? 0x4L : 0x6L);
        FUN_801069d0(324, FUN_8010a808(i) + 4, itemPrices_80114310.get(itemId).get());
      }

      //LAB_8010c708
      if(gameState_800babc8.items_2e9.get(firstItem + i).get() == 0xff) {
        downArrow.flags_00.or(0x40L);
      } else {
        downArrow.flags_00.and(0xffff_ffbfL);
      }
    } else {
      //LAB_8010c734
      //LAB_8010c764
      int i;
      for(i = 0; gameState_800babc8.equipment_1e8.get(firstItem + i).get() != 0xff && i < 6; i++) {
        final int itemId = gameState_800babc8.equipment_1e8.get(firstItem + i).get();
        renderItemIcon(getItemIcon(itemId), 151, FUN_8010a808(i), 0x8L);

        //LAB_8010c7c0
        renderText(equipment_8011972c.get(itemId).deref(), 168, FUN_8010a808(i) + 2, FUN_80022898(itemId) == 0 ? 0x4L : 0x6L);

        if(FUN_80022898(itemId) != 0) {
          renderItemIcon(58, 330, FUN_8010a808(i), 0x8L).clut_30.set(0x7eaaL);
        } else {
          //LAB_8010c814
          renderFiveDigitNumber(322, FUN_8010a808(i) + 4, itemPrices_80114310.get(itemId).get());
        }
      }

      //LAB_8010c854
      if(gameState_800babc8.equipment_1e8.get(firstItem + i).get() == 0xff) {
        //LAB_8010c880
        downArrow.flags_00.or(0x40L);
      } else {
        downArrow.flags_00.and(0xffff_ffbfL);
      }
    }

    //LAB_8010c88c
    if(firstItem == 0) {
      //LAB_8010c8a4
      upArrow.flags_00.or(0x40L);
    } else {
      upArrow.flags_00.and(0xffff_ffbfL);
    }
  }

  @Method(0x8010c8e4L)
  public static void FUN_8010c8e4(final int equipmentId, final int charIndex) {
    if(charIndex != -1) {
      final Memory.TemporaryReservation tmp = MEMORY.temp(0xa0);
      final ActiveStatsa0 oldStats = new ActiveStatsa0(tmp.get());

      //LAB_8010c920
      memcpy(oldStats.getAddress(), stats_800be5f8.get(charIndex).getAddress(), 0xa0);

      //LAB_8010c974
      final int[] oldEquipment = new int[5];
      for(int equipmentSlot = 0; equipmentSlot < 5; equipmentSlot++) {
        oldEquipment[equipmentSlot] = gameState_800babc8.charData_32c.get(charIndex).equipment_14.get(equipmentSlot).get();
      }

      if(equipItem(equipmentId, charIndex) != 0xff) {
        FUN_801038d4(0x67, 210, 127);
        FUN_801038d4(0x68, 210, 137);
        FUN_801038d4(0x69, 210, 147);
        FUN_801038d4(0x6a, 210, 157);
        final ActiveStatsa0 newStats = stats_800be5f8.get(charIndex);
        renderThreeDigitNumber(246, 127, newStats.gearAttack_88.get(), 0x2L);
        renderThreeDigitNumber(246, 137, newStats.gearDefence_8c.get(), 0x2L);
        renderThreeDigitNumber(246, 147, newStats.gearMagicAttack_8a.get(), 0x2L);
        renderThreeDigitNumber(246, 157, newStats.gearMagicDefence_8e.get(), 0x2L);
        FUN_801038d4(0x6b, 274, 127);
        FUN_801038d4(0x6b, 274, 137);
        FUN_801038d4(0x6b, 274, 147);
        FUN_801038d4(0x6b, 274, 157);
        loadCharacterStats(0);
        FUN_80105048(284, 127, oldStats.gearAttack_88.get(), newStats.gearAttack_88.get());
        FUN_80105048(284, 137, oldStats.gearDefence_8c.get(), newStats.gearDefence_8c.get());
        FUN_80105048(284, 147, oldStats.gearMagicAttack_8a.get(), newStats.gearMagicAttack_8a.get());
        FUN_80105048(284, 157, oldStats.gearMagicDefence_8e.get(), newStats.gearMagicDefence_8e.get());
      } else {
        //LAB_8010cafc
        renderText(Cannot_be_armed_with_8011c6d4, 228, 137, 0x4L);
      }

      //LAB_8010cb18
      //LAB_8010cb3c
      for(int equipmentSlot = 0; equipmentSlot < 5; equipmentSlot++) {
        gameState_800babc8.charData_32c.get(charIndex).equipment_14.get(equipmentSlot).set(oldEquipment[equipmentSlot]);
      }

      loadCharacterStats(0);

      tmp.release();
    }

    //LAB_8010cb6c
  }

  @Method(0x8010cb80L)
  public static void FUN_8010cb80(final int itemId) {
    if(itemId != 0xff) {
      //LAB_8010cbb8
      int count = 0;
      for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
        if(gameState_800babc8.items_2e9.get(i).get() == itemId) {
          count++;
        }

        //LAB_8010cbcc
      }

      //LAB_8010cbdc
      final LodString num = new LodString(11);
      intToStr(count, num);
      renderText(Number_kept_8011c7f4, 228, 137, 0x4L);
      renderText(num, 274, 137, 0x4L);
    }

    //LAB_8010cc14
  }

  /**
   * @return True if there is remaining XP to give
   */
  @Method(0x8010cc24L)
  public static boolean givePendingXp(final int charIndex, final int charSlot) {
    if(charIndex == -1) {
      return false;
    }

    final int pendingXp = pendingXp_8011e180.get(charIndex).get();

    if(pendingXp == 0) {
      //LAB_8010cc68
      return false;
    }

    //LAB_8010cc70
    final int cappedPendingXp;
    if((joypadPress_8007a398.get() & 0x20L) != 0 || pendingXp < 10) {
      cappedPendingXp = pendingXp;
    } else {
      cappedPendingXp = 10;
    }

    //LAB_8010cc94
    //LAB_8010cc98
    int xp = gameState_800babc8.charData_32c.get(charIndex).xp_00.get();
    if(xp <= 999999) {
      xp = xp + cappedPendingXp;
    } else {
      xp = 999999;
    }

    //LAB_8010ccd4
    gameState_800babc8.charData_32c.get(charIndex).xp_00.set(xp);
    pendingXp_8011e180.get(charIndex).sub(cappedPendingXp);

    //LAB_8010cd30
    while(gameState_800babc8.charData_32c.get(charIndex).xp_00.get() >= getXpToNextLevel(charIndex) && gameState_800babc8.charData_32c.get(charIndex).level_12.get() < 60) {
      gameState_800babc8.charData_32c.get(charIndex).level_12.incr();

      _8011e1c8.offset(charSlot).addu(0x1L);
      if(additionsUnlocked_8011e1b8.get(charSlot).get() == 0) {
        additionsUnlocked_8011e1b8.get(charSlot).set(loadAdditions(charIndex, additions_8011e098));
      }

      //LAB_8010cd9c
    }

    //LAB_8010cdb0
    //LAB_8010cdcc
    return pendingXp_8011e180.get(charIndex).get() > 0;
  }

  @Method(0x8010cde8L)
  public static void levelUpDragoon(final int charIndex, final int charSlot) {
    if(charIndex != -1) {
      gameState_800babc8.charData_32c.get(charIndex).dlevelXp_0e.add(spGained_800bc950.get(charSlot).get());

      if(gameState_800babc8.charData_32c.get(charIndex).dlevelXp_0e.get() > 32000) {
        gameState_800babc8.charData_32c.get(charIndex).dlevelXp_0e.set(32000);
      }

      //LAB_8010ceb0
      //LAB_8010cecc
      while(gameState_800babc8.charData_32c.get(charIndex).dlevelXp_0e.get() >= _800fbbf0.offset(charIndex * 0x4L).deref(2).offset(gameState_800babc8.charData_32c.get(charIndex).dlevel_13.get() * 0x2L).offset(0x2L).get() && gameState_800babc8.charData_32c.get(charIndex).dlevel_13.get() < 5) {
        loadCharacterStats(0);
        final byte[] spellIndices = new byte[8];
        final int spellCount = getUnlockedDragoonSpells(spellIndices, charIndex);

        gameState_800babc8.charData_32c.get(charIndex).dlevel_13.incr();
        _8011e1d8.offset(charSlot).addu(0x1L);

        loadCharacterStats(0);
        if(spellCount != getUnlockedDragoonSpells(spellIndices, charIndex)) {
          spellsUnlocked_8011e1a8.get(charSlot).set(spellIndices[spellCount] + 1);
        }

        //LAB_8010cf70
      }
    }

    //LAB_8010cf84
  }

  @Method(0x8010cfa0L)
  public static Renderable58 FUN_8010cfa0(final int startGlyph, final int endGlyph, final int x, final int y, final int u, final int v) {
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._d2d8, null);
    renderable.glyph_04.set(startGlyph);
    renderable.startGlyph_10.set(startGlyph);

    if(startGlyph != endGlyph) {
      renderable.endGlyph_14.set(endGlyph);
    } else {
      renderable.endGlyph_14.set(endGlyph);
      renderable.flags_00.or(0x4L);
    }

    //LAB_8010d004
    renderable.x_40.set(x);
    renderable.y_44.set(y);
    renderable.clut_30.set(v << 6 | (u & 0x3f0L) >> 4);
    renderable.tpage_2c.set(0x1bL);
    return renderable;
  }

  @Method(0x8010d050L)
  public static void FUN_8010d050(final InventoryMenuState nextMenuState, final long a1) {
    inventoryMenuState_800bdc28.set(InventoryMenuState._16);
    _800bdc2c.setu(a1);
    confirmDest_800bdc30.set(nextMenuState);
  }

  @Method(0x8010d078L)
  public static void FUN_8010d078(long x, long y, final long w, final long h, final long a4) {
    final long t0 = gpuPacketAddr_1f8003d8.get();
    x -= 8 + displayWidth_1f8003e0.get() / 2;
    y -= 120;
    MEMORY.ref(1, t0).offset(0x03L).setu(0x8L);
    MEMORY.ref(4, t0).offset(0x04L).setu(0x3880_8080L);
    MEMORY.ref(2, t0).offset(0x08L).setu(x);
    MEMORY.ref(2, t0).offset(0x0aL).setu(y);
    MEMORY.ref(2, t0).offset(0x10L).setu(x + w);
    MEMORY.ref(2, t0).offset(0x12L).setu(y);
    MEMORY.ref(2, t0).offset(0x18L).setu(x);
    MEMORY.ref(2, t0).offset(0x1aL).setu(y + h);
    MEMORY.ref(2, t0).offset(0x20L).setu(x + w);
    MEMORY.ref(2, t0).offset(0x22L).setu(y + h);
    gpuPacketAddr_1f8003d8.addu(0x24L);

    final int z;
    switch((int)a4) {
      case 0 -> {
        z = 36;
        MEMORY.ref(1, t0).offset(0x4L).setu(0);
        MEMORY.ref(1, t0).offset(0x5L).setu(0);
        MEMORY.ref(1, t0).offset(0x6L).setu(0x1L);
        MEMORY.ref(1, t0).offset(0xcL).setu(0);
        MEMORY.ref(1, t0).offset(0xdL).setu(0);
        MEMORY.ref(1, t0).offset(0xeL).setu(0x1L);
        MEMORY.ref(1, t0).offset(0x14L).setu(0);
        MEMORY.ref(1, t0).offset(0x15L).setu(0);
        MEMORY.ref(1, t0).offset(0x16L).setu(0x1L);
        MEMORY.ref(1, t0).offset(0x1cL).setu(0);
        MEMORY.ref(1, t0).offset(0x1dL).setu(0);
        MEMORY.ref(1, t0).offset(0x1eL).setu(0x1L);
      }

      case 1 -> {
        z = 36;
        MEMORY.ref(1, t0).offset(0x7L).oru(0x2L);
        MEMORY.ref(1, t0).offset(0xcL).setu(0);
        MEMORY.ref(1, t0).offset(0xdL).setu(0x14L);
        MEMORY.ref(1, t0).offset(0xeL).setu(0x50L);
        MEMORY.ref(1, t0).offset(0x14L).setu(0);
        MEMORY.ref(1, t0).offset(0x15L).setu(0x14L);
        MEMORY.ref(1, t0).offset(0x16L).setu(0x50L);
        MEMORY.ref(1, t0).offset(0x1cL).setu(0);
        MEMORY.ref(1, t0).offset(0x1dL).setu(0);
        MEMORY.ref(1, t0).offset(0x1eL).setu(0);
      }

      case 2 -> {
        z = 36;
        MEMORY.ref(1, t0).offset(0x4L).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0x5L).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0x6L).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0xcL).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0xdL).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0xeL).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0x14L).setu(0);
        MEMORY.ref(1, t0).offset(0x15L).setu(0);
        MEMORY.ref(1, t0).offset(0x16L).setu(0);
        MEMORY.ref(1, t0).offset(0x1cL).setu(0);
        MEMORY.ref(1, t0).offset(0x1dL).setu(0);
        MEMORY.ref(1, t0).offset(0x1eL).setu(0);
      }

      case 3 -> {
        z = 34;
        MEMORY.ref(1, t0).offset(0x4L).setu(0xffL);
        MEMORY.ref(1, t0).offset(0xcL).setu(0xffL);
        MEMORY.ref(1, t0).offset(0x5L).setu(0x7aL);
        MEMORY.ref(1, t0).offset(0xdL).setu(0x7aL);
        MEMORY.ref(1, t0).offset(0x6L).setu(0);
        MEMORY.ref(1, t0).offset(0xeL).setu(0);
        MEMORY.ref(1, t0).offset(0x14L).setu(0x49L);
        MEMORY.ref(1, t0).offset(0x15L).setu(0x23L);
        MEMORY.ref(1, t0).offset(0x16L).setu(0);
        MEMORY.ref(1, t0).offset(0x1cL).setu(0x49L);
        MEMORY.ref(1, t0).offset(0x1dL).setu(0x23L);
        MEMORY.ref(1, t0).offset(0x1eL).setu(0);
      }

      case 4 -> {
        z = 35;
        MEMORY.ref(1, t0).offset(0x4L).setu(0xffL);
        MEMORY.ref(1, t0).offset(0x5L).setu(0x7aL);
        MEMORY.ref(1, t0).offset(0x6L).setu(0);
        MEMORY.ref(1, t0).offset(0xcL).setu(0xffL);
        MEMORY.ref(1, t0).offset(0xdL).setu(0x7aL);
        MEMORY.ref(1, t0).offset(0xeL).setu(0);
        MEMORY.ref(1, t0).offset(0x14L).setu(0xffL);
        MEMORY.ref(1, t0).offset(0x15L).setu(0x7aL);
        MEMORY.ref(1, t0).offset(0x16L).setu(0);
        MEMORY.ref(1, t0).offset(0x1cL).setu(0xffL);
        MEMORY.ref(1, t0).offset(0x1dL).setu(0x7aL);
        MEMORY.ref(1, t0).offset(0x1eL).setu(0);
      }

      case 5 -> {
        z = 34;
        MEMORY.ref(1, t0).offset(0x5L).setu(0x84L);
        MEMORY.ref(1, t0).offset(0xdL).setu(0x84L);
        MEMORY.ref(1, t0).offset(0x6L).setu(0xfeL);
        MEMORY.ref(1, t0).offset(0xeL).setu(0xfeL);
        MEMORY.ref(1, t0).offset(0x4L).setu(0);
        MEMORY.ref(1, t0).offset(0xcL).setu(0);
        MEMORY.ref(1, t0).offset(0x14L).setu(0);
        MEMORY.ref(1, t0).offset(0x15L).setu(0x26L);
        MEMORY.ref(1, t0).offset(0x16L).setu(0x48L);
        MEMORY.ref(1, t0).offset(0x1cL).setu(0);
        MEMORY.ref(1, t0).offset(0x1dL).setu(0x26L);
        MEMORY.ref(1, t0).offset(0x1eL).setu(0x48L);
      }

      case 6 -> {
        z = 35;

        //LAB_8010d290
        MEMORY.ref(1, t0).offset(0x4L).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0x5L).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0x6L).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0xcL).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0xdL).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0xeL).setu(0x7fL);
        MEMORY.ref(1, t0).offset(0x14L).setu(0);
        MEMORY.ref(1, t0).offset(0x15L).setu(0);
        MEMORY.ref(1, t0).offset(0x16L).setu(0);
        MEMORY.ref(1, t0).offset(0x1cL).setu(0);
        MEMORY.ref(1, t0).offset(0x1dL).setu(0);

        //LAB_8010d2c0
        MEMORY.ref(1, t0).offset(0x1eL).setu(0);
      }

      default -> z = 0;
    }

    //LAB_8010d2c4
    queueGpuPacket(tags_1f8003d0.deref().get(z).getAddress(), t0);

    if(a4 == 0x1L) {
      SetDrawTPage(gpuPacketAddr_1f8003d8.deref(4).cast(DR_TPAGE::new), false, true, 0);
      queueGpuPacket(tags_1f8003d0.deref().get(36).getAddress(), gpuPacketAddr_1f8003d8.get());
      gpuPacketAddr_1f8003d8.addu(0x8L);
    }

    //LAB_8010d318
  }

  @Method(0x8010d32cL)
  public static boolean characterIsAlive(final int charSlot) {
    final int charIndex = gameState_800babc8.charIndex_88.get(charSlot).get();

    if(charIndex != -1) {
      //LAB_8010d36c
      for(int i = 0; i < _800bc97c.get(); i++) {
        if(_800bc968.offset(i * 0x4L).get() == charIndex) {
          return true;
        }

        //LAB_8010d384
      }
    }

    //LAB_8010d390
    return false;
  }

  @Method(0x8010d398L)
  public static void renderAdditionUnlocked(final int x, final int y, final int additionIndex, final long a3) {
    FUN_8010d078(x, y + 20 - a3, 134, (a3 + 1) * 2, 0x4L);
    FUN_8010d078(x + 1, y + 20 - a3 + 1, 132, a3 * 2, 0x3L);

    if(a3 >= 20) {
      Scus94491BpeSegment_8002.renderText(additions_8011a064.get(additionIndex).deref(), x - 4, y + 6, 0, 0);
      Scus94491BpeSegment_8002.renderText(New_Addition_8011c5a8, x - 4, y + 20, 0, 0);
    }

    //LAB_8010d470
  }

  @Method(0x8010d498L)
  public static void renderSpellUnlocked(final int x, final int y, final int spellIndex, final long a3) {
    FUN_8010d078(x, y + 20 - a3, 134, (a3 + 1) * 2, 0x4L); // New spell border
    FUN_8010d078(x + 1, y + 20 - a3 + 1, 132, a3 * 2, 0x3L); // New spell background

    if(a3 >= 20) {
      Scus94491BpeSegment_8002.renderText(spells_80052734.get(spellIndex).deref(), x - 4, y + 6, 0, 0);
      Scus94491BpeSegment_8002.renderText(Spell_Unlocked_8011c5c4, x - 4, y + 20, 0, 0);
    }

    //LAB_8010d470
  }

  @Method(0x8010d598L)
  public static int FUN_8010d598(final int charSlot) {
    final int charIndex = gameState_800babc8.charIndex_88.get(charSlot).get();

    if(charIndex == -1) {
      return 0;
    }

    if(_800bc910.offset(charSlot * 0x4L).get() == 0) {
      //LAB_8010d5d0
      return 0;
    }

    //LAB_8010d5d8
    final int a0 = additionOffsets_8004f5ac.get(charIndex).get() + additionCounts_8004f5c0.get(charIndex).get();
    if(a0 == -1) {
      return 0;
    }

    //LAB_8010d60c
    return a0;
  }

  @Method(0x8010d614L)
  public static void FUN_8010d614() {
    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    switch(inventoryMenuState_800bdc28.get()) {
      case INIT_0:
        renderablePtr_800bdc5c.clear();
        drgn0_6666FilePtr_800bdc3c.clear();
        setWidthAndFlags(320, 0);
        loadDrgnBinFile(0, 6665, 0, getMethodAddress(SItem.class, "menuAssetsLoaded", long.class, long.class, long.class), 0, 0x5L);
        loadDrgnBinFile(0, 6666, 0, getMethodAddress(SItem.class, "menuAssetsLoaded", long.class, long.class, long.class), 1, 0x3L);
        _800bdf00.setu(0x21L);
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        break;

      case AWAIT_INIT_1:
        if(!drgn0_6666FilePtr_800bdc3c.isNull()) {
          scriptStartEffect(0x2L, 0xaL);
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
        }
        break;

      case _2:
        if(_800bb168.get() == 0) {
          deallocateRenderables(0xffL);
          Renderable58 glyph = FUN_8010cfa0(0, 0, 165, 21, 720, 497);
          glyph._34.set(0);
          glyph._38.set(0);
          glyph = FUN_8010cfa0(2, 2, 13, 21, 720, 497);
          glyph._34.set(0);
          glyph._38.set(0);
          glyph = FUN_8010cfa0(1, 1, 13, 149, 720, 497);
          glyph._34.set(0);
          glyph._38.set(0);

          FUN_8010cfa0(0x3e, 0x3e, 24, 28, 720, 497);
          FUN_8010cfa0(0x3d, 0x3d, 24, 40, 720, 497);
          FUN_8010cfa0(0x40, 0x40, 24, 52, 720, 497);

          //LAB_8010d81c
          for(int i = 0; i < 6; i++) {
            if(i >= _800bc978.get()) {
              _800bc928.offset(i * 0x4L).setu(0xffL);
            }

            //LAB_8010d830
          }

          FUN_80103b10();
          recalcInventory();

          //LAB_8010d87c
          for(int i = 0; i < 10; i++) {
            spellsUnlocked_8011e1a8.get(i).set(0);
            additionsUnlocked_8011e1b8.get(i).set(0);
            _8011e1c8.offset(i).setu(0);
            _8011e1d8.offset(i).setu(0);
            pendingXp_8011e180.get(i).set(0);
          }

          additionsUnlocked_8011e1b8.get(0).set(FUN_8010d598(0));
          additionsUnlocked_8011e1b8.get(1).set(FUN_8010d598(1));
          additionsUnlocked_8011e1b8.get(2).set(FUN_8010d598(2));

          xpDivisor_8011e174.set(0);
          for(int charSlot = 0; charSlot < 3; charSlot++) {
            if(characterIsAlive(charSlot)) {
              xpDivisor_8011e174.incr();
            }
          }

          for(int charSlot = 0; charSlot < 3; charSlot++) {
            if(characterIsAlive(charSlot)) {
              pendingXp_8011e180.get(gameState_800babc8.charIndex_88.get(charSlot).get()).set(totalXpFromCombat_800bc95c.get() / xpDivisor_8011e174.get());
            }
          }

          //LAB_8010d9d4
          //LAB_8010d9f8
          for(int secondaryCharSlot = 0; secondaryCharSlot < 6; secondaryCharSlot++) {
            final int secondaryCharIndex = secondaryCharIndices_800bdbf8.get(secondaryCharSlot).get();

            if(secondaryCharIndex != -1) {
              pendingXp_8011e180.get(secondaryCharIndex).set(totalXpFromCombat_800bc95c.get() / xpDivisor_8011e174.get() / 2);
            }

            //LAB_8010da24
          }

          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_MAIN_MENU_3);
          FUN_8010e9a8(0x1L, xpDivisor_8011e174.get());
        }

        break;

      case INIT_MAIN_MENU_3:
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          //LAB_8010da84
          if(goldGainedFromCombat_800bc920.get() == 0) {
            inventoryMenuState_800bdc28.set(InventoryMenuState._5);
          } else {
            inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
          }
        }

        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case MAIN_MENU_4:
        final int goldTick;
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          goldTick = goldGainedFromCombat_800bc920.get();
        } else {
          //LAB_8010dab4
          goldTick = 10;
        }

        //LAB_8010dabc
        final int goldGained = goldGainedFromCombat_800bc920.get();

        if(goldTick >= goldGained) {
          soundTick_8011e17c.setu(0);
          goldGainedFromCombat_800bc920.set(0);
          inventoryMenuState_800bdc28.set(InventoryMenuState._5);
          gameState_800babc8.gold_94.add(goldGained);
        } else {
          //LAB_8010db00
          goldGainedFromCombat_800bc920.sub(goldTick);
          gameState_800babc8.gold_94.add(goldTick);
        }

        //LAB_8010db18
        if(gameState_800babc8.gold_94.get() > 99999999) {
          gameState_800babc8.gold_94.set(99999999);
        }

        //LAB_8010db3c
        //LAB_8010db40
        soundTick_8011e17c.addu(0x1L);

        if((soundTick_8011e17c.get() & 0x1L) != 0) {
          playSound(0x1L);
        }

        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _5:
        final boolean moreXpToGive =
          givePendingXp(gameState_800babc8.charIndex_88.get(0).get(), 0) ||
          givePendingXp(gameState_800babc8.charIndex_88.get(1).get(), 1) ||
          givePendingXp(gameState_800babc8.charIndex_88.get(2).get(), 2) ||
          givePendingXp(secondaryCharIndices_800bdbf8.get(0).get(), 3) ||
          givePendingXp(secondaryCharIndices_800bdbf8.get(1).get(), 4) ||
          givePendingXp(secondaryCharIndices_800bdbf8.get(2).get(), 5) ||
          givePendingXp(secondaryCharIndices_800bdbf8.get(3).get(), 6) ||
          givePendingXp(secondaryCharIndices_800bdbf8.get(4).get(), 7) ||
          givePendingXp(secondaryCharIndices_800bdbf8.get(5).get(), 8);

        if(moreXpToGive) {
          soundTick_8011e17c.addu(0x1L);

          if((soundTick_8011e17c.get() & 0x1L) != 0) {
            playSound(0x1L);
          }
        } else {
          _8011e170.setu(0x3L);
          totalXpFromCombat_800bc95c.set(0);

          if(additionsUnlocked_8011e1b8.get(0).get() + additionsUnlocked_8011e1b8.get(1).get() + additionsUnlocked_8011e1b8.get(2).get() == 0) {
            //LAB_8010dc9c
            inventoryMenuState_800bdc28.set(InventoryMenuState._8);
          } else if((joypadPress_8007a398.get() & 0x20L) != 0) {
            playSound(0x2L);
            _8011e178.setu(0);
            inventoryMenuState_800bdc28.set(InventoryMenuState._6);
          }
        }

        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _6:
        if((int)_8011e178.get() < 0x14L) {
          _8011e178.addu(0x2L);
        } else {
          //LAB_8010dcc8
          if((joypadPress_8007a398.get() & 0x20L) != 0) {
            playSound(0x2L);

            //LAB_8010dcf0
            inventoryMenuState_800bdc28.set(InventoryMenuState._7);
          }
        }

        //LAB_8010dcf4
        //LAB_8010dcf8
        renderAdditionsUnlocked(_8011e178.get());
        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _7:
        if((int)_8011e178.get() > 0) {
          _8011e178.subu(0x2L);
        } else {
          //LAB_8010dd28
          inventoryMenuState_800bdc28.set(InventoryMenuState._8);
        }

        renderAdditionsUnlocked(_8011e178.get());
        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _8:
        if(_8011e170.get() >= 0x9L) {
          //LAB_8010dd90
          inventoryMenuState_800bdc28.set(InventoryMenuState._10);
        } else if(_8011e1c8.offset(_8011e170.get()).get() != 0) {
          FUN_800192d8(-0x50L, 0x2cL);
          playSound(0x9L);
          inventoryMenuState_800bdc28.set(InventoryMenuState._9);
        } else {
          //LAB_8010dd88
          _8011e170.addu(0x1L);
        }

        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _9:
        FUN_8010e708(24, 152, secondaryCharIndices_800bdbf8.get((int)(_8011e170.get() - 3)).get());

        if((joypadPress_8007a398.get() & 0x60L) != 0) {
          playSound(0x2L);
          _8011e1c8.offset(_8011e170.get()).setu(0);
          inventoryMenuState_800bdc28.set(InventoryMenuState._8);
          _8011e170.addu(0x1L);
        }

        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _10:
        for(int charSlot = 0; charSlot < 3; charSlot++) {
          if(characterIsAlive(charSlot)) {
            levelUpDragoon(gameState_800babc8.charIndex_88.get(charSlot).get(), charSlot);
          }
        }

        //LAB_8010de6c
        if(spellsUnlocked_8011e1a8.get(0).get() != 0 || spellsUnlocked_8011e1a8.get(1).get() != 0 || spellsUnlocked_8011e1a8.get(2).get() != 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._11);
        } else {
          //LAB_8010de98
          inventoryMenuState_800bdc28.set(InventoryMenuState._14);
        }

        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _11:
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          _8011e178.setu(0);
          playSound(0x2L);

          //LAB_8010decc
          inventoryMenuState_800bdc28.set(InventoryMenuState._12);
        }

        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _12:
        if(_8011e178.get() < 0x14L) {
          _8011e178.addu(0x2L);
        } else {
          //LAB_8010def4
          if((joypadPress_8007a398.get() & 0x20L) != 0) {
            playSound(0x2L);

            //LAB_8010df1c
            inventoryMenuState_800bdc28.set(InventoryMenuState._13);
          }
        }

        //LAB_8010df20
        //LAB_8010df24
        renderSpellsUnlocked(_8011e178.get());
        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _13:
        if((int)_8011e178.get() > 0) {
          _8011e178.subu(0x2L);
        } else {
          //LAB_8010df54
          //LAB_8010df1c
          inventoryMenuState_800bdc28.set(InventoryMenuState._14);
        }

        //LAB_8010df20
        //LAB_8010df24
        renderSpellsUnlocked(_8011e178.get());
        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _14:
        if((joypadPress_8007a398.get() & 0x60L) != 0) {
          playSound(0x3L);

          final InventoryMenuState nextMenuState;
          if(_800bc978.get() != 0 && (FUN_80023544(_800bc928.getAddress(), _800bc978.getAddress()) & 0xffL) != 0) {
            nextMenuState = InventoryMenuState._19;
          } else {
            //LAB_8010dfac
            nextMenuState = InventoryMenuState._18;
          }

          //LAB_8010dfb0
          FUN_8010d050(nextMenuState, 0x1L);
        }

        //LAB_8010dfb8
        //LAB_8010dfbc
        FUN_8010e9a8(0, xpDivisor_8011e174.get());
        break;

      case _16:
        scriptStartEffect(0x1L, 0xaL);
        inventoryMenuState_800bdc28.set(InventoryMenuState._17);

      case _17:
        FUN_8010e9a8(0, xpDivisor_8011e174.get());

        if((int)_800bb168.get() >= 0xffL) {
          inventoryMenuState_800bdc28.set(confirmDest_800bdc30.get());
          FUN_80019470();
        }

        break;

      case _18:
        scriptStartEffect(0x2L, 0xaL);
        deallocateRenderables(0xffL);
        free(drgn0_6666FilePtr_800bdc3c.getPointer());
        whichMenu_800bdc38.setu(0x1eL);
        _800bdf00.setu(0xdL);
        break;

      case _19:
        setWidthAndFlags(384, 0);
        deallocateRenderables(0xffL);
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        whichMenu_800bdc38.setu(0x22L);
        break;
    }

    //LAB_8010e09c
    //LAB_8010e0a0
    FUN_8010d078(0xa6L, 0x16L, 0x88L, 0xc0L, 0x1L);
    FUN_8010d078(0xeL, 0x16L, 0x90L, 0x78L, 0x1L);
    FUN_8010d078(0xeL, 0x96L, 0x90L, 0x40L, 0x1L);
    FUN_8010d078(0, 0, 0xf0L, 0xf0L, 0);
  }

  @Method(0x8010e114L)
  public static Renderable58 FUN_8010e114(final int x, final int y, final int charSlot) {
    if(charSlot >= 9) {
      //LAB_8010e1ec
      throw new IllegalArgumentException("Invalid character index");
    }

    final int glyph = (int)_800fbc9c.offset(charSlot).getSigned();
    final Renderable58 renderable = FUN_8010cfa0(glyph, glyph, x, y, 704, (int)_800fbc88.offset(charSlot * 0x2L).getSigned());
    renderable._3c.set(0x23);

    //LAB_8010e1f0
    return renderable;
  }

  @Method(0x8010e200L)
  public static void FUN_8010e200(final int x, final int y, int val, final UnsignedIntRef a3) {
    val = val % 10;
    if(val != 0 || a3.get() != 0) {
      //LAB_8010e254
      final Renderable58 renderable = FUN_8010cfa0(val + 3, val + 3, x, y, 736, 497);
      renderable.flags_00.or(0x8L);
      a3.set(1);
    }

    //LAB_8010e290
  }

  @Method(0x8010e2a0L)
  public static void FUN_8010e2a0(final int x, final int y, final int dlevel) {
    final int s2 = Math.min(99, dlevel);
    final UnsignedIntRef sp0x10 = new UnsignedIntRef();
    FUN_8010e200(x, y, s2 / 10, sp0x10.set(0));
    FUN_8010e200(x + 6, y, s2, sp0x10.incr());
  }

  @Method(0x8010e340L)
  public static void FUN_8010e340(final int x, final int y, final int val) {
    final int s2 = Math.min(999_999, val);
    final UnsignedIntRef sp0x10 = new UnsignedIntRef();
    FUN_8010e200(x, y, s2 / 100_000, sp0x10);
    FUN_8010e200(x +  6, y, s2 / 10_000, sp0x10);
    FUN_8010e200(x + 12, y, s2 /  1_000, sp0x10);
    FUN_8010e200(x + 18, y, s2 /    100, sp0x10);
    FUN_8010e200(x + 24, y, s2 /     10, sp0x10);
    FUN_8010e200(x + 30, y, s2, sp0x10.incr());
  }

  @Method(0x8010e490L)
  public static void FUN_8010e490(final int x, final int y, final int val) {
    final int s2 = Math.min(99_999_999, val);
    final UnsignedIntRef sp0x10 = new UnsignedIntRef();
    FUN_8010e200(x, y, s2 / 10_000_000, sp0x10);
    FUN_8010e200(x +  6, y, s2 / 1_000_000, sp0x10);
    FUN_8010e200(x + 12, y, s2 /   100_000, sp0x10);
    FUN_8010e200(x + 18, y, s2 /    10_000, sp0x10);
    FUN_8010e200(x + 24, y, s2 /     1_000, sp0x10);
    FUN_8010e200(x + 30, y, s2 /       100, sp0x10);
    FUN_8010e200(x + 36, y, s2 /        10, sp0x10);
    FUN_8010e200(x + 42, y, s2, sp0x10.incr());
  }

  @Method(0x8010e630L)
  public static void FUN_8010e630(final int x, final int y, final int val) {
    if(val != 0) {
      FUN_8010e340(x, y, val);
    } else {
      //LAB_8010e660
      final Renderable58 renderable = FUN_8010cfa0(0x47, 0x47, x + 30, y, 736, 497);
      renderable.flags_00.or(0x8L);
    }

    //LAB_8010e698
  }

  @Method(0x8010e6a8L)
  public static int getXpWidth(final int xp) {
    if(xp > 99999) {
      return 36;
    }

    //LAB_8010e6c4
    if(xp > 9999) {
      return 30;
    }

    //LAB_8010e6d4
    if(xp > 999) {
      return 24;
    }

    //LAB_8010e6e4
    if(xp > 99) {
      //LAB_8010e6fc
      return 18;
    }

    if(xp > 9) {
      //LAB_8010e700
      return 12;
    }

    return 6;
  }

  @Method(0x8010e708L)
  public static void FUN_8010e708(final int x, final int y, final int charIndex) {
    if(charIndex != -1) {
      FUN_8010d078(x + 1, y + 9, 0x18L, 0x20L, 0x2L);
      final Renderable58 renderable = FUN_8010e114(x - 1, y + 8, charIndex);
      renderable.flags_00.or(0x8L);
      FUN_8010cfa0((int)_800fbca8.offset(charIndex).get(), (int)_800fbca8.offset(charIndex).get(), x + 32, y + 4, 736, 497).flags_00.or(0x8L);
      FUN_8010cfa0(0x3b, 0x3b, x + 30, y + 20, 736, 497).flags_00.or(0x8L);
      FUN_8010cfa0(0x3c, 0x3c, x + 30, y + 32, 736, 497).flags_00.or(0x8L);
      FUN_8010cfa0(0x3d, 0x3d, x, y + 48, 736, 497).flags_00.or(0x8L);

      FUN_8010e2a0(x + 108, y + 20, gameState_800babc8.charData_32c.get(charIndex).level_12.get());

      final int dlevel;
      if(FUN_80104b7c(gameState_800babc8.dragoonSpirits_19c.get(0).get(), charIndex) == 0) {
        dlevel = 0;
      } else {
        dlevel = gameState_800babc8.charData_32c.get(charIndex).dlevel_13.get();
      }

      //LAB_8010e8e0
      FUN_8010e2a0(x + 108, y + 32, dlevel);
      final int xp = getXpToNextLevel(charIndex);
      FUN_8010e340(x + 76 - getXpWidth(xp), y + 48, gameState_800babc8.charData_32c.get(charIndex).xp_00.get());
      FUN_8010cfa0(0x22, 0x22, x - (getXpWidth(xp) - 114), y + 48, 736, 497).flags_00.or(0x8L);
      FUN_8010e630(x + 84, y + 48, xp);
    }

    //LAB_8010e978
  }

  @Method(0x8010e9a8L)
  public static void FUN_8010e9a8(final long a0, final long a1) {
    int y1 = 24;
    int y2 = -82;
    int y3 = -70;

    //LAB_8010e9fc
    for(int i = 0; i < 3; i++) {
      if(gameState_800babc8.charIndex_88.get(i).get() != -1) {
        FUN_8010e708(176, y1, gameState_800babc8.charIndex_88.get(i).get());

        if(_8011e1c8.offset(i).get() != 0) {
          _8011e1c8.offset(i).setu(0);
          FUN_800192d8(72, y2);
          playSound(9);
        }

        //LAB_8010ea44
        if(_8011e1d8.offset(i).get() != 0) {
          _8011e1d8.offset(i).setu(0);
          FUN_800192d8(72, y3);
          playSound(9);
        }
      }

      //LAB_8010ea70
      y1 += 64;
      y2 += 64;
      y3 += 64;
    }

    FUN_8010e490( 96, 28, goldGainedFromCombat_800bc920.get());
    FUN_8010e340(108, 40, totalXpFromCombat_800bc95c.get());

    y1 = 63;
    y2 = 64;

    //LAB_8010eae0
    for(int i = 0; i < _800bc978.get(); i++) {
      if(_800bc928.offset(i * 0x4L).get() != 0xff) {
        renderItemIcon(getItemIcon((int)_800bc928.offset(i * 0x4L).get()), 18, y1, 0x8L);
        renderText(equipment_8011972c.get((int)_800bc928.offset(i * 0x4L).get()).deref(), 28, y2, 0);
      }

      //LAB_8010eb38
      y2 += 16;
      y1 += 16;
    }

    //LAB_8010eb58
    FUN_8010e490(96, 156, gameState_800babc8.gold_94.get());

    if(a0 != 0) {
      FUN_8010cfa0(0x3f, 0x3f, 144,  28, 736, 497);
      FUN_8010cfa0(0x3f, 0x3f, 144, 156, 736, 497);
    }

    //LAB_8010ebb0
    uploadRenderables();
    FUN_80018e84();
  }

  @Method(0x8010ebecL)
  public static void renderAdditionsUnlocked(final long a0) {
    for(int i = 0; i < 3; i++) {
      if(additionsUnlocked_8011e1b8.get(i).get() != 0) {
        renderAdditionUnlocked(168, 40 + i * 64, additionsUnlocked_8011e1b8.get(i).get() - 1, a0);
      }
    }
  }

  @Method(0x8010ec6cL)
  public static void renderSpellsUnlocked(final long a0) {
    //LAB_8010ec98
    for(int i = 0; i < 3; i++) {
      if(spellsUnlocked_8011e1a8.get(i).get() != 0) {
        renderSpellUnlocked(168, 40 + i * 64, spellsUnlocked_8011e1a8.get(i).get() - 1, a0);
      }

      //LAB_8010ecc0
    }
  }

  @Method(0x8010ececL)
  public static MessageBoxResult messageBox(final MessageBox20 menu) {
    final Renderable58 renderable;
    final long s1;

    switch(menu._0c.get()) {
      case 0:
        return MessageBoxResult.YES;

      case 1: // Allocate "loading saved games" box
        menu._0c.set(0x2);
        menu.renderable_04.clear();
        menu.renderable_08.set(allocateUiElement(149, 142, menu.x_1c.get() - 50, menu.y_1e.get() - 10));
        menu.renderable_08.deref()._3c.set(0x20);
        menu.renderable_08.deref()._18.set(142);
        msgboxResult_8011e1e8.set(MessageBoxResult.AWAITING_INPUT);

      case 2:
        if(menu.renderable_08.deref()._0c.get() != 0) {
          menu._0c.set(0x3);
        }

        break;

      case 3:
        _800bdf00.setu(0x1fL);
        final int x = menu.x_1c.get() + 60;
        int y = menu.y_1e.get() + 7;

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
            msgboxResult_8011e1e8.set(MessageBoxResult.YES);
          }

          break;
        }

        if(s1 == 0x2L) {
          //LAB_8010ef10
          if(menu.renderable_04.isNull()) {
            renderable = allocateUiElement(125, 125, menu.x_1c.get() + 45, menu.menuIndex_18.get() * 14 + y + 5);
            menu.renderable_04.set(renderable);
            renderable._38.set(0);
            renderable._34.set(0);
            menu.renderable_04.deref()._3c.set(0x20);
          }

          //LAB_8010ef64
          _800bdf00.setu(0x1fL);

          renderCentredText(Yes_8011c20c, menu.x_1c.get() + 60, y + 7, menu.menuIndex_18.get() == 0 ? 0x5L : 0x4L);
          renderCentredText(No_8011c214, menu.x_1c.get() + 60, y + 21, menu.menuIndex_18.get() == 0 ? 0x4L : 0x5L);

          _800bdf00.setu(0x21L);
          final YesNoResult msgboxYesNo = handleYesNo(menu.menuIndex_18);
          if(msgboxYesNo == YesNoResult.SCROLLED) {
            //LAB_8010f014
            menu.renderable_04.deref().y_44.set(menu.menuIndex_18.get() * 14 + y + 5);
          } else if(msgboxYesNo == YesNoResult.YES) {
            //LAB_8010f040
            menu._0c.set(0x4);
            msgboxResult_8011e1e8.set(MessageBoxResult.YES);
          } else if(msgboxYesNo == YesNoResult.NO || msgboxYesNo == YesNoResult.CANCELLED) {
            //LAB_8010f000
            //LAB_8010f05c
            menu._0c.set(0x4);
            msgboxResult_8011e1e8.set(MessageBoxResult.NO);
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
        renderable = allocateUiElement(0x8e, 0x95, menu.x_1c.get() - 50, menu.y_1e.get() - 10);
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
        return msgboxResult_8011e1e8.get();
    }

    //LAB_8010f108
    //LAB_8010f10c
    return MessageBoxResult.AWAITING_INPUT;
  }

  @Method(0x8010f130L)
  public static void setMessageBoxText(@Nullable final LodString text, final int a1) {
    messageBox_8011dc90.text_00.setNullable(text);

    messageBox_8011dc90.x_1c.set(120);
    messageBox_8011dc90.y_1e.set(100);
    messageBox_8011dc90._15.set(a1);
    messageBox_8011dc90.menuIndex_18.set(0);
    messageBox_8011dc90.ticks_10.set(0);
    messageBox_8011dc90._0c.set(1);
  }

  @Method(0x8010f178L)
  public static int FUN_8010f178(final int a0) {
    return a0 * 17 + 42;
  }

  @Method(0x8010f188L)
  public static int FUN_8010f188(final int a0) {
    return 160 + a0 * 17;
  }

  @Method(0x8010f198L)
  public static void FUN_8010f198() {
    long v1;
    long a0;
    long a1;
    long s0;
    long s2;
    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    //TODO this menu should use its own state var - the enum is for the giant main state method above
    switch(inventoryMenuState_800bdc28.get()) {
      case INIT_0:
        drgn0_6666FilePtr_800bdc3c.clear();
        renderablePtr_800bdc5c.clear();
        setWidthAndFlags(384, 0);
        loadDrgnBinFile(0, 6666, 0, getMethodAddress(SItem.class, "menuAssetsLoaded", long.class, long.class, long.class), 0x1L, 0x3L);
        loadDrgnBinFile(0, 6665, 0, getMethodAddress(SItem.class, "menuAssetsLoaded", long.class, long.class, long.class), 0, 0x5L);
        _800bdf00.setu(0x21L);
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        break;

      case AWAIT_INIT_1:
        if(!drgn0_6666FilePtr_800bdc3c.isNull()) {
          _8011dcb8.get(0).setPointer(mallocTail(0x4c0L));
          _8011dcb8.get(1).setPointer(mallocTail(0x4c0L));
          recalcInventory();
          FUN_80104738(0x1L);
          messageBox_8011dc90._0c.set(0);

          //LAB_8010f2e8
          a0 = menuItems_8011d7c8.getAddress();
          a1 = _800bc928.getAddress();
          for(s0 = 0; s0 < 10; s0++) {
            if((int)s0 >= (int)_800bc978.get()) {
              v1 = 0xffL;
            } else {
              v1 = MEMORY.ref(1, a1).offset(0x0L).get();
            }

            //LAB_8010f300
            MEMORY.ref(1, a0).offset(0x0L).setu(v1);
            MEMORY.ref(2, a0).offset(0x2L).setu(0);
            MEMORY.ref(1, a0).offset(0x1L).setu(0);
            a0 = a0 + 0x4L;
            a1 = a1 + 0x4L;
          }

          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
        }
        break;

      case _2:
        deallocateRenderables(0xffL);
        slotScroll_8011e1f8.set(0);
        slotIndex_8011e1f4.set(0);
        menuIndex_8011e1f0.set(0);
        inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_MAIN_MENU_3);
        break;

      case INIT_MAIN_MENU_3:
        deallocateRenderables(0);
        FUN_8010fd80(0x1L, menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get(), slotIndex_8011e1f4.get(), slotScroll_8011e1f8.get(), 0);
        menuIndex_8011e1fc.set(0);
        final Renderable58 renderable = allocateUiElement(125, 125, 136, FUN_8010f188(0) - 2);
        renderable_8011e208.set(renderable);
        FUN_80104b60(renderable);
        scriptStartEffect(0x2L, 0xaL);
        inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
        break;

      case MAIN_MENU_4:
        FUN_8010fd80(0, menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get(), slotIndex_8011e1f4.get(), slotScroll_8011e1f8.get(), 0);

        if(_800bb168.get() == 0) {
          renderText(Too_many_8011c21c, 16, 151, 0x4L);
          renderText(items_8011c230, 16, 168, 0x4L);
          renderText(Replace_8011c240, 16, 185, 0x4L);
          renderCentredText(Yes_8011c20c, 150, FUN_8010f188(0), menuIndex_8011e1fc.get() == 0 ? 0x5L : 0x6L);
          renderCentredText(No_8011c214, 150, FUN_8010f188(1), menuIndex_8011e1fc.get() != 0 ? 0x5L : 0x6L);

          switch(handleYesNo(menuIndex_8011e1fc)) {
            case SCROLLED ->
              renderable_8011e208.deref().y_44.set(FUN_8010f188(menuIndex_8011e1fc.get()) - 2);
            case YES -> {
              unloadRenderable(renderable_8011e208.deref());
              inventoryMenuState_800bdc28.set(InventoryMenuState._6);
            }
            case NO, CANCELLED -> {
              unloadRenderable(renderable_8011e208.deref());
              inventoryMenuState_800bdc28.set(InventoryMenuState._10);
            }
          }
        }
        break;

      case _6:
        menuIndex_8011e1f0.set(0);
        final Renderable58 renderable2 = allocateUiElement(124, 124, 42, FUN_8010f178(0));
        renderable_8011e200.set(renderable2);
        FUN_80104b60(renderable2);

      case _7:
        deallocateRenderables(0);
        FUN_8010fd80(0x1L, menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get(), slotIndex_8011e1f4.get(), slotScroll_8011e1f8.get(), 0x1L);
        inventoryMenuState_800bdc28.set(InventoryMenuState._8);
        break;

      case _8:
        if(scrollMenu(menuIndex_8011e1f0, null, 5, (int)_800bc978.get(), 1)) {
          renderable_8011e200.deref().y_44.set(FUN_8010f178(menuIndex_8011e1f0.get()));
        }

        //LAB_8010f608
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          if(menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get() != 0xff) {
            slotScroll_8011e1f8.set(0);
            slotIndex_8011e1f4.set(0);
            final Renderable58 renderable3 = allocateUiElement(118, 118, 220, FUN_8010f178(0));
            renderable_8011e204.set(renderable3);
            FUN_80104b60(renderable3);
            playSound(0x2L);
            inventoryMenuState_800bdc28.set(InventoryMenuState._9);
          } else {
            //LAB_8010f68c
            playSound(0x28L);
          }
        }

        //LAB_8010f694
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          unloadRenderable(renderable_8011e200.deref());
          inventoryMenuState_800bdc28.set(InventoryMenuState._10);
        }

        //LAB_8010f6d4
        FUN_8010fd80(0, menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get(), slotIndex_8011e1f4.get(), slotScroll_8011e1f8.get(), 0x1L);
        break;

      case _9:
        final int slotCount;
        if(menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get() < 0xc0) {
          slotCount = gameState_800babc8.equipmentCount_1e4.get();
        } else {
          //LAB_8010f754
          slotCount = gameState_800babc8.itemCount_1e6.get();
        }

        //LAB_8010f76c
        if(scrollMenu(slotIndex_8011e1f4, slotScroll_8011e1f8, 7, slotCount, 1)) {
          renderable_8011e204.deref().y_44.set(FUN_8010f178(slotIndex_8011e1f4.get()));
        }

        //LAB_8010f79c
        if((inventoryJoypadInput_800bdc44.get() & 0x40L) != 0) {
          playSound(0x3L);
          unloadRenderable(renderable_8011e204.deref());
          inventoryMenuState_800bdc28.set(InventoryMenuState._8);
        }

        //LAB_8010f7d8
        if((inventoryJoypadInput_800bdc44.get() & 0x10L) != 0) {
          playSound(0x2L);

          if(menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get() < 0xc0) {
            FUN_80023a2c(_8011dcb8.get(0).deref(), gameState_800babc8.equipment_1e8, gameState_800babc8.equipmentCount_1e4.get());
          } else {
            //LAB_8010f838
            FUN_80023a2c(_8011dcb8.get(1).deref(), gameState_800babc8.items_2e9, gameState_800babc8.itemCount_1e6.get());
          }
        }

        //LAB_8010f858
        if((inventoryJoypadInput_800bdc44.get() & 0x20L) != 0) {
          final MenuItemStruct04 newItem = menuItems_8011d7c8.get(menuIndex_8011e1f0.get());
          final int isItem = menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get() >= 0xc0 ? 1 : 0;
          final MenuItemStruct04 existingItem = _8011dcb8.get(isItem).deref().get(slotIndex_8011e1f4.get() + slotScroll_8011e1f8.get());

          if((existingItem.price_02.get() & 0x6000) != 0) {
            playSound(0x28L);
          } else {
            //LAB_8010f8f4
            final int itemId = existingItem.itemId_00.get();
            final int itemSlot = existingItem.itemSlot_01.get();
            final int flags = existingItem.price_02.get();

            existingItem.itemId_00.set(newItem.itemId_00.get());
            existingItem.itemSlot_01.set(newItem.itemSlot_01.get());
            existingItem.price_02.set(newItem.price_02.get());

            newItem.itemId_00.set(itemId);
            newItem.itemSlot_01.set(itemSlot);
            newItem.price_02.set(flags);

            playSound(2);
            unloadRenderable(renderable_8011e204.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState._8);

            //LAB_8010f99c
            if(isItem != 0) {
              FUN_800239e0(_8011dcb8.get(1).deref(), gameState_800babc8.items_2e9, gameState_800babc8.itemCount_1e6.get());
            } else {
              //LAB_8010f98c
              FUN_800239e0(_8011dcb8.get(0).deref(), gameState_800babc8.equipment_1e8, gameState_800babc8.equipmentCount_1e4.get());
            }
          }
        }

        //LAB_8010f9a4
        //LAB_8010f9a8
        FUN_8010fd80(0, menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get(), slotIndex_8011e1f4.get(), slotScroll_8011e1f8.get(), 0x3L);
        break;

      case _10:
        menuIndex_8011e1fc.set(0);
        final Renderable58 renderable4 = allocateUiElement(125, 125, 136, FUN_8010f188(0) - 2);
        renderable_8011e208.set(renderable4);
        FUN_80104b60(renderable4);
        inventoryMenuState_800bdc28.set(InventoryMenuState._11);

      case _11:
        renderText(To_many_items_8011c268, 16, 151, 0x4L);
        renderText(Discard_8011c288, 16, 168, 0x4L);
        renderText(End_8011c29c, 16, 185, 0x4L);
        renderCentredText(Yes_8011c20c, 150, FUN_8010f188(0), menuIndex_8011e1fc.get() == 0 ? 0x5L : 0x6L);
        renderCentredText(No_8011c214, 150, FUN_8010f188(1), menuIndex_8011e1fc.get() != 0 ? 0x5L : 0x6L);
        FUN_8010fd80(0, menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get(), slotIndex_8011e1f4.get(), slotScroll_8011e1f8.get(), 0);

        switch(handleYesNo(menuIndex_8011e1fc)) {
          //LAB_8010fb28
          case SCROLLED ->
            //LAB_8010fb4c
            renderable_8011e208.deref().y_44.set(FUN_8010f188(menuIndex_8011e1fc.get()) - 2);

          case YES -> {
            //LAB_8010fb6c
            unloadRenderable(renderable_8011e208.deref());

            //LAB_8010fb94
            s2 = 0;
            for(int i = 0; i < _800bc978.get(); i++) {
              if(FUN_80022898(menuItems_8011d7c8.get(i).itemId_00.get()) != 0) {
                s2 = s2 + 0x1L;
              }

              //LAB_8010fbb0
            }

            //LAB_8010fbc4
            if(s2 != 0) {
              setMessageBoxText(This_item_cannot_be_thrown_away_8011c2a8, 0);
              inventoryMenuState_800bdc28.set(InventoryMenuState._13);
            } else {
              //LAB_8010fbe8
              scriptStartEffect(0x1L, 0xaL);
              inventoryMenuState_800bdc28.set(InventoryMenuState._12);
            }
          }

          case NO, CANCELLED -> {
            //LAB_8010fc04
            //LAB_8010fc08
            unloadRenderable(renderable_8011e208.deref());
            inventoryMenuState_800bdc28.set(InventoryMenuState._6);
          }
        }
        break;

      case _12:
        FUN_8010fd80(0, menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get(), slotIndex_8011e1f4.get(), slotScroll_8011e1f8.get(), 0);

        if(_800bb168.get() >= 0xff) {
          scriptStartEffect(0x2L, 0xaL);
          free(_8011dcb8.get(0).getPointer());
          free(_8011dcb8.get(1).getPointer());
          deallocateRenderables(0xffL);
          free(drgn0_6666FilePtr_800bdc3c.getPointer());
          whichMenu_800bdc38.setu(0x23L);

          if(mainCallbackIndex_8004dd20.get() == 0x5L && loadingSmapOvl_8004dd08.get() == 0) {
            FUN_800e3fac();
          }

          //LAB_8010fd00
          //LAB_8010fd04
          _800bdf00.setu(0xdL);
        }
        break;

      case _13:
        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._6);
        }

        //LAB_8010fd28
        //LAB_8010fd5c
        FUN_8010fd80(0, menuItems_8011d7c8.get(menuIndex_8011e1f0.get()).itemId_00.get(), slotIndex_8011e1f4.get(), slotScroll_8011e1f8.get(), 0);
        break;
    }

    //LAB_8010fd64
  }

  @Method(0x8010fd80L)
  public static void FUN_8010fd80(final long a0, final int itemId, final int slotIndex, final int slotScroll, final long a4) {
    if(a0 != 0) {
      renderGlyphs(glyphs_80114548, 0, 0);
      saveListUpArrow_800bdb94.set(allocateUiElement(61, 68, 358, FUN_8010f178(0)));
      saveListDownArrow_800bdb98.set(allocateUiElement(53, 60, 358, FUN_8010f178(6)));
    }

    //LAB_8010fe18
    //LAB_8010fe38
    renderMenuItems(16, 33, menuItems_8011d7c8, 0, Math.min(5, (int)_800bc978.get()), saveListUpArrow_800bdb94.deref(), saveListDownArrow_800bdb98.deref());

    if((a4 & 0x1L) != 0 && a0 == 0) {
      renderString(0, 16, 164, itemId, 0);
    }

    //LAB_8010fe90
    //LAB_8010fe94
    renderText(Acquired_item_8011c2f8, 32, 22, 0x4L);

    if(itemId >= 0xc0) {
      //LAB_8010ff30
      if(itemId >= 0xff && (a4 & 0x2L) != 0) {
        final Renderable58 renderable = FUN_801038d4(137, 84, 140);
        renderable.clut_30.set(0x7cebL);
        renderText(_8011d024, 37, 140, 0x4L);
      }

      renderText(_8011c32c, 210, 22, 0x4L);

      if((a4 & 0x1L) != 0) {
        renderMenuItems(194, 33, _8011dcb8.get(1).deref(), slotScroll, 7, saveListUpArrow_800bdb94.deref(), saveListDownArrow_800bdb98.deref());
      }

      //LAB_8010ff90
      if((a4 & 0x2L) != 0) {
        //LAB_8010ffb4
        renderString(0, 194, 164, _8011dcb8.get(1).deref().get(slotScroll + slotIndex).itemId_00.get(), a0);

        //LAB_8010ffcc
        if((a4 & 0x2L) != 0) {
          final Renderable58 renderable = FUN_801038d4(137, 84, 140);
          renderable.clut_30.set(0x7cebL);
          renderText(_8011d024, 37, 140, 0x4L);
        }
      }
    } else {
      renderText(_8011c314, 210, 22, 0x4L);

      if((a4 & 0x1L) != 0) {
        renderMenuItems(194, 33, _8011dcb8.get(0).deref(), slotScroll, 7, saveListUpArrow_800bdb94.deref(), saveListDownArrow_800bdb98.deref());
      }

      //LAB_8010ff08
      if((a4 & 0x2L) != 0) {
        renderString(0, 194, 164, _8011dcb8.get(0).deref().get(slotScroll + slotIndex).itemId_00.get(), a0);

        if((a4 & 0x2L) != 0) {
          final Renderable58 renderable = FUN_801038d4(137, 84, 140);
          renderable.clut_30.set(0x7cebL);
          renderText(_8011d024, 37, 140, 0x4L);
        }
      }
    }

    //LAB_80110004
    uploadRenderables();
  }

  @Method(0x80110030L)
  public static void loadCharacterStats(long a0) {
    final long spc0 = a0;

    clearCharacterStats();

    //LAB_80110174
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);

      final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);

      stats.xp_00.set(charData.xp_00.get());
      stats.hp_04.set(charData.hp_08.get());
      stats.mp_06.set(charData.mp_0a.get());
      stats.sp_08.set(charData.sp_0c.get());
      stats._0a.set(charData.dlevelXp_0e.get());
      stats.dragoonFlag_0c.set(charData.status_10.get());
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

      final LevelStuff08 levelStuff = levelStuff_800fbd30.get(charIndex).deref().get(stats.level_0e.get());
      stats.maxHp_66.set(levelStuff.hp_00.get());
      stats.addition_68.set(levelStuff.addition_02.get());
      stats.bodySpeed_69.set(levelStuff.bodySpeed_03.get());
      stats.bodyAttack_6a.set(levelStuff.bodyAttack_04.get());
      stats.bodyMagicAttack_6b.set(levelStuff.bodyMagicAttack_05.get());
      stats.bodyDefence_6c.set(levelStuff.bodyDefence_06.get());
      stats.bodyMagicDefence_6d.set(levelStuff.bodyMagicDefence_07.get());

      final MagicStuff08 magicStuff = magicStuff_800fbd54.get(charIndex).deref().get(stats.dlevel_0f.get());
      stats.maxMp_6e.set(magicStuff.mp_00.get());
      stats.spellIndex_70.set(magicStuff.spellIndex_02.get());
      stats._71.set(magicStuff._03.get());
      stats.dragoonAttack_72.set(magicStuff.dragoonAttack_04.get());
      stats.dragoonMagicAttack_73.set(magicStuff.dragoonMagicAttack_05.get());
      stats.dragoonDefence_74.set(magicStuff.dragoonDefence_06.get());
      stats.dragoonMagicDefence_75.set(magicStuff.dragoonMagicDefence_07.get());

      final int a2 = stats.selectedAddition_35.get();
      if(a2 != -1) {
        //TODO straighten this out
        a0 = ptrTable_80114070.offset(a2 * 0x4L).deref(4).offset(MEMORY.ref(1, stats.additionLevels_36.getAddress()).offset(a2 - additionOffsets_8004f5ac.get(charIndex).get()).get() * 0x4L).getAddress();

        stats._9c.set((int)MEMORY.ref(2, a0).offset(0x0L).get());
        stats.additionSpMultiplier_9e.set((int)MEMORY.ref(1, a0).offset(0x2L).get());
        stats.additionDamageMultiplier_9f.set((int)MEMORY.ref(1, a0).offset(0x3L).get());
      }

      //LAB_8011042c
      FUN_8011085c(charIndex);

      long v0 = _800fbd08.get(charIndex).get();
      a0 = v0 & 0x1fL;
      v0 = v0 >>> 5;
      if((gameState_800babc8.dragoonSpirits_19c.get((int)v0).get() & 0x1L << a0) != 0) {
        stats.dragoonFlag_0c.or(0x2000);
        a0 = _800fbd08.get(charIndex).get();

        if((gameState_800babc8._4e6.get() >> a0 & 1) == 0) {
          gameState_800babc8._4e6.or(1 << a0);

          stats.mp_06.set(magicStuff.mp_00.get());
          stats.maxMp_6e.set(magicStuff.mp_00.get());
        }
      } else {
        //LAB_801104ec
        stats.mp_06.set(0);
        stats.maxMp_6e.set(0);
        stats.dlevel_0f.set(0);
      }

      //LAB_801104f8
      if(charIndex == 0) {
        v0 = _800fbd08.get(9).get();

        a0 = v0 & 0x1fL;
        v0 = v0 >>> 5;
        if((gameState_800babc8.dragoonSpirits_19c.get((int)v0).get() & 0x1L << a0) != 0) {
          stats.dragoonFlag_0c.or(0x6000);

          final long a1 = _800fbd08.get(0).get();

          if((gameState_800babc8._4e6.get() >> a1 & 1) == 0) {
            gameState_800babc8._4e6.or(1 << a1);
            stats.mp_06.set(magicStuff.mp_00.get());
            stats.maxMp_6e.set(magicStuff.mp_00.get());
          } else {
            //LAB_80110590
            stats.mp_06.set(charData.mp_0a.get());
            stats.maxMp_6e.set(magicStuff.mp_00.get());
          }
        }
      }

      //LAB_801105b0
      int maxHp = stats.maxHp_66.get() * (stats.hpMulti_62.get() / 100 + 1);

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
      final int maxMp = stats.maxMp_6e.get() * (stats.mpMulti_64.get() / 100 + 1);

      stats.maxMp_6e.set(maxMp);

      if(stats.mp_06.get() > maxMp) {
        stats.mp_06.set(maxMp);
      }

      //LAB_80110654
    }

    if(spc0 == 0x1L) {
      FUN_80012bb4();
      _800be5d0.setu(1);
    }

    //LAB_8011069c
  }

  @Method(0x801106ccL)
  public static void FUN_801106cc(final int equipmentId) {
    FUN_8002a8f8();

    memcpy(equipmentStats_800be5d8.getAddress(), equipmentStats_80111ff0.get(equipmentId).getAddress(), 0x1c);
  }

  @Method(0x8011085cL)
  public static void FUN_8011085c(final int charIndex) {
    FUN_8002a86c(charIndex);
    final ActiveStatsa0 characterStats = stats_800be5f8.get(charIndex);
    final EquipmentStats1c equipmentStats = equipmentStats_800be5d8;

    //LAB_801108b0
    for(int equipmentSlot = 0; equipmentSlot < 5; equipmentSlot++) {
      final int equipmentId = stats_800be5f8.get(charIndex).equipment_30.get(equipmentSlot).get();

      if(equipmentId != 0xff) {
        FUN_801106cc(equipmentId);

        characterStats.specialEffectFlag_76.or(equipmentStats._00.get());
        characterStats._77.or(equipmentStats.type_01.get());
        characterStats._78.or(equipmentStats._02.get());
        characterStats._79.or(equipmentStats.equips_03.get());
        characterStats.elementFlag_7a.or(equipmentStats.element_04.get());
        characterStats._7b.or(equipmentStats._05.get());
        characterStats.elementalResistanceFlag_7c.or(equipmentStats.eHalf_06.get());
        characterStats.elementalImmunityFlag_7d.or(equipmentStats.eImmune_07.get());
        characterStats.statusResistFlag_7e.or(equipmentStats.statRes_08.get());
        characterStats._7f.or(equipmentStats._09.get());
        characterStats._84.add(equipmentStats.icon_0e.get());
        characterStats.gearSpeed_86.add(equipmentStats.spd_0f.get());
        characterStats.gearAttack_88.add(equipmentStats.atkHi_10.get());
        characterStats.gearMagicAttack_8a.add(equipmentStats.matk_11.get());
        characterStats.gearDefence_8c.add(equipmentStats.def_12.get());
        characterStats.gearMagicDefence_8e.add(equipmentStats.mdef_13.get());
        characterStats.attackHit_90.add(equipmentStats.aHit_14.get());
        characterStats.magicHit_92.add(equipmentStats.mHit_15.get());
        characterStats.attackAvoid_94.add(equipmentStats.aAv_16.get());
        characterStats.magicAvoid_96.add(equipmentStats.mAv_17.get());
        characterStats.onHitStatusChance_98.add(equipmentStats.onStatusChance_18.get());
        characterStats._99.add(equipmentStats._19.get());
        characterStats._9a.add(equipmentStats._1a.get());
        characterStats.onHitStatus_9b.or(equipmentStats.onHitStatus_1b.get());
        characterStats._80.add(equipmentStats.atk_0a.get());
        characterStats.gearAttack_88.add((short)equipmentStats.atk_0a.get());
        characterStats._81.or(equipmentStats.special1_0b.get());

        //LAB_80110b10
        long a0 = 0x1L;
        long a1;
        for(a1 = 0; a1 < 8; a1++) {
          if((equipmentStats.special1_0b.get() & a0) != 0) {
            if(a0 == 0x1L) {
              //LAB_80110c14
              characterStats.mpPerMagicalHit_54.add((short)equipmentStats.specialAmount_0d.get());
            } else if(a0 == 0x2L) {
              //LAB_80110bfc
              characterStats.spPerMagicalHit_52.add((short)equipmentStats.specialAmount_0d.get());
              //LAB_80110b54
            } else if(a0 == 0x4L) {
              //LAB_80110be4
              characterStats.mpPerPhysicalHit_50.add((short)equipmentStats.specialAmount_0d.get());
            } else if(a0 == 0x8L) {
              //LAB_80110bcc
              characterStats.spPerPhysicalHit_4e.add((short)equipmentStats.specialAmount_0d.get());
            } else if(a0 == 0x10L) {
              //LAB_80110bb4
              characterStats.spMultiplier_4c.add((short)equipmentStats.specialAmount_0d.get());
              //LAB_80110b64
            } else if(a0 == 0x20L) {
              //LAB_80110bac
              characterStats.physicalResistance_4a.set(1);
              //LAB_80110b88
            } else if(a0 == 0x40L) {
              //LAB_80110ba4
              characterStats.magicalImmunity_48.set(1);
            } else if(a0 == 0x80L) {
              characterStats.physicalImmunity_46.set(1);
            }
          }

          //LAB_80110c28
          a0 = a0 << 1;
        }

        characterStats._82.or(equipmentStats.special2_0c.get());

        //LAB_80110c54
        a0 = 0x1L;
        for(a1 = 0; a1 < 8; a1++) {
          if((equipmentStats.special2_0c.get() & a0) != 0) {
            if(a0 == 0x1L) {
              //LAB_80110d78
              characterStats.mpMulti_64.add((short)equipmentStats.specialAmount_0d.get());
            } else if(a0 == 0x2L) {
              //LAB_80110d60
              characterStats.hpMulti_62.add((short)equipmentStats.specialAmount_0d.get());
              //LAB_80110c98
            } else if(a0 == 0x4L) {
              //LAB_80110d58
              characterStats.magicalResistance_60.set(1);
            } else if(a0 == 0x8L) {
              //LAB_80110d40
              characterStats.revive_5e.add((short)equipmentStats.specialAmount_0d.get());
            } else if(a0 == 0x10L) {
              //LAB_80110d28
              characterStats.spRegen_5c.add((short)equipmentStats.specialAmount_0d.get());
              //LAB_80110ca8
            } else if(a0 == 0x20L) {
              //LAB_80110d10
              characterStats.mpRegen_5a.add((short)equipmentStats.specialAmount_0d.get());
              //LAB_80110ccc
            } else if(a0 == 0x40L) {
              //LAB_80110cf8
              characterStats.hpRegen_58.add((short)equipmentStats.specialAmount_0d.get());
            } else if(a0 == 0x80L) {
              characterStats._56.add((short)equipmentStats.specialAmount_0d.get());
            }
          }

          //LAB_80110d8c
          a0 = a0 << 1;
        }
      }
    }
  }
}
