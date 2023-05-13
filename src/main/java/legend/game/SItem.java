package legend.game;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.GpuCommandPoly;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.combat.Bttl_800c;
import legend.game.combat.bobj.BattleObject27c;
import legend.game.combat.bobj.PlayerBattleObject;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.MainMenuScreen;
import legend.game.inventory.screens.MenuStack;
import legend.game.inventory.screens.TextColour;
import legend.game.inventory.screens.TooManyItemsScreen;
import legend.game.modding.events.EventManager;
import legend.game.modding.events.characters.AdditionHitMultiplierEvent;
import legend.game.modding.events.characters.AdditionUnlockEvent;
import legend.game.modding.events.characters.CharacterStatsEvent;
import legend.game.modding.events.characters.XpToLevelEvent;
import legend.game.modding.events.inventory.EquipmentStatsEvent;
import legend.game.scripting.ScriptState;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentStats1c;
import legend.game.types.InventoryMenuState;
import legend.game.types.LevelStuff08;
import legend.game.types.LodString;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuAdditionInfo;
import legend.game.types.MenuGlyph06;
import legend.game.types.MenuItemStruct04;
import legend.game.types.MenuStatus08;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.game.types.Translucency;
import legend.game.types.UiFile;
import legend.game.unpacker.FileData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SMap.FUN_800e3fac;
import static legend.game.Scus94491BpeSegment.FUN_80018e84;
import static legend.game.Scus94491BpeSegment.FUN_800192d8;
import static legend.game.Scus94491BpeSegment.FUN_80019470;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.getCharacterName;
import static legend.game.Scus94491BpeSegment.loadDir;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022a94;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a86c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a8f8;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.clearCharacterStats;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getItemIcon;
import static legend.game.Scus94491BpeSegment_8002.getJoypadInputByPriority;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.giveItems;
import static legend.game.Scus94491BpeSegment_8002.itemCantBeDiscarded;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.loadingGameStateOverlay_8004dd08;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_8005.combatants_8005e398;
import static legend.game.Scus94491BpeSegment_8005.spells_80052734;
import static legend.game.Scus94491BpeSegment_8005.standingInSavePoint_8005a368;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc910;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bc968;
import static legend.game.Scus94491BpeSegment_800b._800bc97c;
import static legend.game.Scus94491BpeSegment_800b._800bdc2c;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.confirmDest_800bdc30;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.equipmentStats_800be5d8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b.inventoryJoypadInput_800bdc44;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemiesCount_800bc978;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.savedGameSelected_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIndices_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.combat.Bttl_800c._800c66d0;
import static legend.game.combat.Bttl_800c.addCombatant;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.combatantCount_800c66a0;
import static legend.game.combat.Bttl_800c.combatantTmdAndAnimLoadedCallback;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.loadCombatantTim;
import static legend.game.combat.Bttl_800c.loadCombatantTmdAndAnims;
import static legend.game.combat.Bttl_800f.FUN_800f863c;

public final class SItem {
  private SItem() { }

  public static final MenuStack menuStack = new MenuStack();

  public static final Value _800fba58 = MEMORY.ref(4, 0x800fba58L);

  public static final ArrayRef<UnsignedByteRef> additionXpPerLevel_800fba2c = MEMORY.ref(1, 0x800fba2cL, ArrayRef.of(UnsignedByteRef.class, 5, 1, UnsignedByteRef::new));

  public static final ArrayRef<MenuStatus08> menuStatus_800fba7c = MEMORY.ref(4, 0x800fba7cL, ArrayRef.of(MenuStatus08.class, 8, 8, MenuStatus08::new));
  public static final ArrayRef<IntRef> dragoonSpiritGoodsBits_800fbabc = MEMORY.ref(4, 0x800fbabcL, ArrayRef.of(IntRef.class, 8, 4, IntRef::new));

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

  public static final ArrayRef<EquipmentStats1c> equipmentStats_80111ff0 = MEMORY.ref(1, 0x80111ff0L, ArrayRef.of(EquipmentStats1c.class, 0xc0, 0x1c, EquipmentStats1c::new));
  public static final int[] kongolXpTable_801134f0 = new int[61];
  public static final int[] dartXpTable_801135e4 = new int[61];
  public static final int[] haschelXpTable_801136d8 = new int[61];
  public static final int[] meruXpTable_801137cc = new int[61];
  public static final int[] lavitzXpTable_801138c0 = new int[61];
  public static final int[] albertXpTable_801138c0 = new int[61];
  public static final int[] roseXpTable_801139b4 = new int[61];
  public static final int[] shanaXpTable_80113aa8 = new int[61];
  public static final int[] mirandaXpTable_80113aa8 = new int[61];
  public static final int[][] xpTables = {dartXpTable_801135e4, lavitzXpTable_801138c0, shanaXpTable_80113aa8, roseXpTable_801139b4, haschelXpTable_801136d8, albertXpTable_801138c0, meruXpTable_801137cc, kongolXpTable_801134f0, mirandaXpTable_80113aa8};

  public static final Value ptrTable_80114070 = MEMORY.ref(4, 0x80114070L);

  public static final UnboundedArrayRef<MenuGlyph06> charSwapGlyphs_80114160 = MEMORY.ref(1, 0x80114160L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> equipmentGlyphs_80114180 = MEMORY.ref(1, 0x80114180L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> characterStatusGlyphs_801141a4 = MEMORY.ref(1, 0x801141a4L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> additionGlyphs_801141e4 = MEMORY.ref(1, 0x801141e4L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> useItemGlyphs_801141fc = MEMORY.ref(1, 0x801141fcL, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> dabasMenuGlyphs_80114228 = MEMORY.ref(1, 0x80114228L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));

  public static final Value characterValidEquipment_80114284 = MEMORY.ref(1, 0x80114284L);

  public static final Value _80114290 = MEMORY.ref(1, 0x80114290L);

  public static final MenuGlyph06 glyph_801142d4 = MEMORY.ref(1, 0x801142d4L, MenuGlyph06::new);

  public static final ArrayRef<Pointer<LodString>> chapterNames_80114248 = MEMORY.ref(4, 0x80114248L, ArrayRef.of(Pointer.classFor(LodString.class), 4, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> characterNames_801142dc = MEMORY.ref(4, 0x801142dcL, ArrayRef.of(Pointer.classFor(LodString.class), 9, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<UnsignedShortRef> itemPrices_80114310 = MEMORY.ref(2, 0x80114310L, ArrayRef.of(UnsignedShortRef.class, 0x100, 2, UnsignedShortRef::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114510 = MEMORY.ref(1, 0x80114510L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114548 = MEMORY.ref(1, 0x80114548L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));

  public static final ArrayRef<Pointer<LodString>> itemDescriptions_80117a10 = MEMORY.ref(4, 0x80117a10L, ArrayRef.of(Pointer.classFor(LodString.class), 256, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> equipment_8011972c = MEMORY.ref(4, 0x8011972cL, ArrayRef.of(Pointer.classFor(LodString.class), 256, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> additions_8011a064 = MEMORY.ref(4, 0x8011a064L, ArrayRef.of(Pointer.classFor(LodString.class), 43, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> _8011b75c = MEMORY.ref(4, 0x8011b75cL, ArrayRef.of(Pointer.classFor(LodString.class), 64, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> goodsItemNames_8011c008 = MEMORY.ref(4, 0x8011c008L, ArrayRef.of(Pointer.classFor(LodString.class), 64, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> submapNames_8011c108 = MEMORY.ref(4, 0x8011c108L, ArrayRef.of(Pointer.classFor(LodString.class), 57, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> worldMapNames_8011c1ec = MEMORY.ref(4, 0x8011c1ecL, ArrayRef.of(Pointer.classFor(LodString.class), 8, 4, Pointer.deferred(4, LodString::new)));

  public static final LodString _8011c254 = MEMORY.ref(4, 0x8011c254L, LodString::new);

  /** "Yes" */
  public static final LodString Yes_8011c20c = MEMORY.ref(2, 0x8011c20cL, LodString::new);
  /** "No" */
  public static final LodString No_8011c214 = MEMORY.ref(2, 0x8011c214L, LodString::new);
  public static final LodString This_item_cannot_be_thrown_away_8011c2a8 = MEMORY.ref(2, 0x8011c2a8L, LodString::new);
  public static final LodString Acquired_item_8011c2f8 = MEMORY.ref(2, 0x8011c2f8L, LodString::new);
  public static final LodString Armed_item_8011c314 = MEMORY.ref(2, 0x8011c314L, LodString::new);
  public static final LodString Used_item_8011c32c = MEMORY.ref(2, 0x8011c32cL, LodString::new);
  public static final LodString Addition_cannot_be_used_8011c340 = MEMORY.ref(2, 0x8011c340L, LodString::new);
  public static final LodString Not_enough_money_8011c468 = MEMORY.ref(2, 0x8011c468L, LodString::new);
  public static final LodString Which_item_do_you_want_to_sell_8011c4e4 = MEMORY.ref(2, 0x8011c4e4L, LodString::new);
  public static final LodString Which_weapon_do_you_want_to_sell_8011c524 = MEMORY.ref(2, 0x8011c524L, LodString::new);
  public static final LodString New_Addition_8011c5a8 = MEMORY.ref(2, 0x8011c5a8L, LodString::new);
  public static final LodString Spell_Unlocked_8011c5c4 = MEMORY.ref(2, 0x8011c5c4L, LodString::new);

  public static final LodString Buy_8011c6a4 = MEMORY.ref(2, 0x8011c6a4L, LodString::new);
  public static final LodString Sell_8011c6ac = MEMORY.ref(2, 0x8011c6acL, LodString::new);
  public static final LodString Carried_8011c6b8 = MEMORY.ref(2, 0x8011c6b8L, LodString::new);
  public static final LodString Leave_8011c6c8 = MEMORY.ref(2, 0x8011c6c8L, LodString::new);
  public static final LodString Cannot_be_armed_with_8011c6d4 = MEMORY.ref(2, 0x8011c6d4L, LodString::new);

  public static final LodString Number_kept_8011c7f4 = MEMORY.ref(2, 0x8011c7f4L, LodString::new);
  /** "Overwrite save?" */
  public static final LodString Overwrite_save_8011c9e8 = MEMORY.ref(2, 0x8011c9e8L, LodString::new);
  public static final LodString AcquiredGold_8011cdd4 = new LodString("Acquired Gold");
  public static final LodString HP_recovered_for_all_8011cfcc = MEMORY.ref(2, 0x8011cfccL, LodString::new);
  public static final LodString MP_recovered_for_all_8011cff8 = MEMORY.ref(2, 0x8011cff8L, LodString::new);
  public static final LodString Press_to_sort_8011d024 = MEMORY.ref(2, 0x8011d024L, LodString::new);
  public static final LodString DigDabas_8011d04c = new LodString("Diiig Dabas!");
  public static final LodString AcquiredItems_8011d050 = new LodString("Acquired Items");
  public static final LodString SpecialItem_8011d054 = new LodString("Special Item");
  public static final LodString Take_8011d058 = new LodString("Take");
  public static final LodString Discard_8011d05c = new LodString("Discard");
  public static final LodString NextDig_8011d064 = new LodString("Next Dig");
  public static final LodString Completely_recovered_8011d534 = MEMORY.ref(2, 0x8011d534L, LodString::new);
  public static final LodString Recovered_8011d560 = MEMORY.ref(2, 0x8011d560L, LodString::new);
  public static final LodString HP_8011d57c = MEMORY.ref(2, 0x8011d57cL, LodString::new);
  public static final LodString MP_8011d584 = MEMORY.ref(2, 0x8011d584L, LodString::new);
  public static final LodString SP_8011d58c = MEMORY.ref(2, 0x8011d58cL, LodString::new);
  public static final LodString Encounter_risk_reduced_8011d594 = MEMORY.ref(2, 0x8011d594L, LodString::new);
  public static final LodString Detoxified_8011d5c8 = MEMORY.ref(2, 0x8011d5c8L, LodString::new);
  public static final LodString Spirit_recovered_8011d5e0 = MEMORY.ref(2, 0x8011d5e0L, LodString::new);
  public static final LodString Fear_gone_8011d604 = MEMORY.ref(2, 0x8011d604L, LodString::new);
  public static final LodString Nothing_happened_8011d618 = MEMORY.ref(2, 0x8011d618L, LodString::new);

  public static final UnsignedByteRef characterCount_8011d7c4 = MEMORY.ref(1, 0x8011d7c4L, UnsignedByteRef::new);

  public static final BoolRef canSave_8011dc88 = MEMORY.ref(1, 0x8011dc88L, BoolRef::new);

  public static final MessageBox20 messageBox_8011dc90 = new MessageBox20();

  public static final BoolRef _8011dcfc = MEMORY.ref(1, 0x8011dcfcL, BoolRef::new);

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

  @Method(0x800fbd78L)
  public static void allocatePlayerBattleObjects() {
    //LAB_800fbdb8
    for(charCount_800c677c.set(0); charCount_800c677c.get() < 3; charCount_800c677c.incr()) {
      if(gameState_800babc8.charIds_88[charCount_800c677c.get()] < 0) {
        break;
      }
    }

    //LAB_800fbde8
    final long fp = _80111d38.offset(charCount_800c677c.get() * 0xcL).getAddress();
    final int[] charIndices = new int[charCount_800c677c.get()];

    //LAB_800fbe18
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      charIndices[charSlot] = addCombatant(0x200 + gameState_800babc8.charIds_88[charSlot] * 2, charSlot);
    }

    //LAB_800fbe4c
    //LAB_800fbe70
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];
      final String name = "Char ID " + charIndex + " (bobj + " + (charSlot + 6) + ')';
      final ScriptState<PlayerBattleObject> state = SCRIPTS.allocateScriptState(charSlot + 6, name, 0, new PlayerBattleObject(name, charSlot + 6));
      state.setTicker(Bttl_800c::bobjTicker);
      state.setDestructor(Bttl_800c::bobjDestructor);
      _8006e398.bobjIndices_e0c[_800c66d0.get()] = state;
      _8006e398.charBobjIndices_e40[charSlot] = state;
      final BattleObject27c bobj = state.innerStruct_00;
      bobj.magic_00 = BattleScriptDataBase.BOBJ;
      bobj.combatant_144 = getCombatant((short)charIndices[charSlot]);
      bobj.charIndex_272 = charIndex;
      bobj.charSlot_276 = charSlot;
      bobj.combatantIndex_26c = charIndices[charSlot];
      bobj._274 = _800c66d0.get();
      bobj.model_148.coord2_14.coord.transfer.setX((int)MEMORY.ref(2, fp).offset(charSlot * 0x4L).offset(0x0L).getSigned());
      bobj.model_148.coord2_14.coord.transfer.setY(0);
      bobj.model_148.coord2_14.coord.transfer.setZ((int)MEMORY.ref(2, fp).offset(charSlot * 0x4L).offset(0x2L).getSigned());
      bobj.model_148.coord2Param_64.rotate.set((short)0, (short)0x400, (short)0);
      _800c66d0.incr();
    }

    //LAB_800fbf6c
    _8006e398.bobjIndices_e0c[_800c66d0.get()] = null;
    _8006e398.charBobjIndices_e40[charCount_800c677c.get()] = null;

    FUN_800f863c();
    decrementOverlayCount();
  }

  @Method(0x800fbfe0L)
  public static void loadEncounterAssets() {
    loadSupportOverlay(2, () -> SItem.loadEnemyTextures(2625 + encounterId_800bb0f8.get()));

    //LAB_800fc030
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      if(getCombatant(i).charSlot_19c < 0) { // I think this means it's not a player
        loadCombatantTmdAndAnims(i);
      }

      //LAB_800fc050
    }

    //LAB_800fc064
    //LAB_800fc09c
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      combatants_8005e398[_8006e398.charBobjIndices_e40[i].innerStruct_00.combatantIndex_26c].flags_19e |= 0x2a;
    }

    //LAB_800fc104
    loadSupportOverlay(2, SItem::deferLoadPartyTims);
    loadSupportOverlay(2, SItem::deferLoadPartyTmdAndAnims);
    _800bc960.or(0x400);
    decrementOverlayCount();
  }

  @Method(0x800fc210L)
  public static void loadCharTmdAndAnims(final List<FileData> files, final int charSlot) {
    //LAB_800fc260
    final BattleObject27c data = _8006e398.charBobjIndices_e40[charSlot].innerStruct_00;

    //LAB_800fc298
    combatantTmdAndAnimLoadedCallback(files, data.combatantIndex_26c, false);

    //LAB_800fc34c
    _800bc960.or(0x4);
    decrementOverlayCount();
  }

  @Method(0x800fc3c0L)
  public static void loadEnemyTextures(final int fileIndex) {
    // Example file: 2856
    loadDrgnDir(0, fileIndex, SItem::enemyTexturesLoadedCallback);
  }

  @Method(0x800fc404L)
  public static void enemyTexturesLoadedCallback(final List<FileData> files) {
    final BattlePreloadedEntities_18cb0 s2 = battlePreloadedEntities_1f8003f4;

    //LAB_800fc434
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 a0 = getCombatant(i);

      if(a0.charSlot_19c < 0) {
        final int enemyIndex = a0.charIndex_1a2 & 0x1ff;

        //LAB_800fc464
        for(int enemySlot = 0; enemySlot < 3; enemySlot++) {
          if((s2.encounterData_00.enemyIndices_00[enemySlot] & 0x1ff) == enemyIndex && files.get(enemySlot).real()) {
            loadCombatantTim(i, files.get(enemySlot));
            break;
          }
        }
      }
    }

    //LAB_800fc4cc
    decrementOverlayCount();
  }

  @Method(0x800fc504L)
  public static void deferLoadPartyTims() {
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charId = gameState_800babc8.charIds_88[charSlot];
      final String name = getCharacterName(charId).toLowerCase();
      final int finalCharSlot = charSlot;
      loadFile("characters/%s/textures/combat".formatted(name), files -> SItem.loadCharacterTim(files, finalCharSlot));
    }
  }

  @Method(0x800fc548L)
  public static void loadCharacterTim(final FileData file, final int charSlot) {
    final BattleObject27c bobj = _8006e398.charBobjIndices_e40[charSlot].innerStruct_00;
    loadCombatantTim(bobj.combatantIndex_26c, file);

    decrementOverlayCount();
  }

  @Method(0x800fc654L)
  public static void deferLoadPartyTmdAndAnims() {
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charId = gameState_800babc8.charIds_88[charSlot];
      final String name = getCharacterName(charId).toLowerCase();
      final int finalCharSlot = charSlot;
      loadDir("characters/%s/models/combat".formatted(name), files -> SItem.loadCharTmdAndAnims(files, finalCharSlot));
    }
  }

  @Method(0x800fc698L)
  public static int getXpToNextLevel(final int charIndex) {
    if(charIndex == -1 || charIndex > 8) {
      //LAB_800fc6a4
      throw new RuntimeException("Character index " + charIndex + " out of bounds");
    }

    //LAB_800fc6ac
    final int level = gameState_800babc8.charData_32c[charIndex].level_12;

    if(level >= 60) {
      return 0; // Max level
    }

    final XpToLevelEvent event = EventManager.INSTANCE.postEvent(new XpToLevelEvent(charIndex, level, xpTables[charIndex][level + 1]));

    //LAB_800fc70c
    return event.xp;
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

  @Method(0x800fc84cL)
  public static int getSlotY(final int slot) {
    return 16 + slot * 72;
  }

  @Method(0x800fc8c0L)
  public static int getCharacterPortraitX(final int slot) {
    return 21 + slot * 50;
  }

  @Method(0x800fc8dcL)
  public static int getItemSlotY(final int slot) {
    return 18 + slot * 17;
  }

  @Method(0x800fc944L)
  public static void menuAssetsLoaded(final FileData data, final int whichFile) {
    if(whichFile == 0) {
      //LAB_800fc98c
      FUN_80022a94(data.slice(0x83e0)); // Character textures
      FUN_80022a94(data); // Menu textures
      FUN_80022a94(data.slice(0x6200)); // Item textures
      FUN_80022a94(data.slice(0x1_0460));
      FUN_80022a94(data.slice(0x1_0580));
    } else if(whichFile == 1) {
      //LAB_800fc9e4
      uiFile_800bdc3c = UiFile.fromFile(data);
    }

    //LAB_800fc9fc
  }

  @Method(0x800fcad4L)
  public static void renderMenus() {
    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    switch(inventoryMenuState_800bdc28.get()) {
      case INIT_0 -> { // Initialize, loads some files (unknown contents)
        savedGameSelected_800bdc34.set(false);
        messageBox_8011dc90.state_0c = 0;
        loadCharacterStats(0);

        if(mainCallbackIndex_8004dd20.get() == 8) {
          gameState_800babc8.isOnWorldMap_4e4 = true;
          canSave_8011dc88.set(true);
        } else {
          gameState_800babc8.isOnWorldMap_4e4 = false;
          canSave_8011dc88.set(Config.saveAnywhere() || standingInSavePoint_8005a368.get());
        }

        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
      }

      case AWAIT_INIT_1 -> {
        if(uiFile_800bdc3c != null) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
          _8011dcfc.set((gameState_800babc8.goods_19c[1] & 0x4) > 0);
        }
      }

      case _2 -> {
        menuStack.pushScreen(new MainMenuScreen(() -> {
          menuStack.popScreen();
          inventoryMenuState_800bdc28.set(InventoryMenuState.UNLOAD_125);
        }));

        inventoryMenuState_800bdc28.set(InventoryMenuState.MAIN_MENU_4);
      }

      case MAIN_MENU_4 -> menuStack.render();

      case _19 -> {
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        whichMenu_800bdc38 = WhichMenu.RENDER_SHOP_MENU_9;
      }

      case UNLOAD_125 -> {
        deallocateRenderables(0xff);
        uiFile_800bdc3c = null;

        switch(whichMenu_800bdc38) {
          case RENDER_SAVE_GAME_MENU_19 ->
            whichMenu_800bdc38 = WhichMenu.UNLOAD_SAVE_GAME_MENU_20;

          case RENDER_CHAR_SWAP_MENU_24 -> {
            scriptStartEffect(2, 10);
            whichMenu_800bdc38 = WhichMenu.UNLOAD_CHAR_SWAP_MENU_25;
          }

          default -> {
            scriptStartEffect(2, 10);
            whichMenu_800bdc38 = WhichMenu.UNLOAD_INVENTORY_MENU_5;
          }
        }

        if(mainCallbackIndex_8004dd20.get() == 5 && loadingGameStateOverlay_8004dd08.get() == 0) {
          FUN_800e3fac();
        }

        textZ_800bdf00.set(13);
      }
    }
  }

  @Method(0x801033ccL)
  public static void FUN_801033cc(final Renderable58 a0) {
    a0._28 = 0x1;
    a0._38 = 0;
    a0._34 = 0;
    a0.z_3c = 31;
  }

  @Method(0x801033e8L)
  public static void fadeOutArrow(final Renderable58 renderable) {
    unloadRenderable(renderable);

    final Renderable58 newRenderable = allocateUiElement(108, 111, renderable.x_40, renderable.y_44);
    newRenderable.flags_00 |= 0x10;
    FUN_801033cc(newRenderable);
  }

  @Method(0x80103444L)
  public static void FUN_80103444(@Nullable final Renderable58 a0, final int a1, final int a2, final int a3, final int a4) {
    if(a0 != null) {
      if(a0._18 == 0) {
        if((simpleRand() & 0x3000L) != 0) {
          a0._18 = a1;
          a0._1c = a2;
        } else {
          //LAB_801034a0
          a0._18 = a3;
          a0._1c = a4;
        }
      }
    }

    //LAB_801034b0
  }

  @Method(0x801034ccL)
  public static void FUN_801034cc(final int charSlot, final int charCount) {
    FUN_80103444(renderablePtr_800bdba4, 0x2d, 0x34, 0xaa, 0xb1);
    FUN_80103444(renderablePtr_800bdba8, 0x25, 0x2c, 0xa2, 0xa9);

    if(charSlot != 0) {
      if(renderablePtr_800bdba4 == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 18, 16);
        renderable._18 = 0x2d;
        renderable._1c = 0x34;
        renderablePtr_800bdba4 = renderable;
        FUN_801033cc(renderable);
      }
    } else {
      //LAB_80103578
      if(renderablePtr_800bdba4 != null) {
        fadeOutArrow(renderablePtr_800bdba4);
        renderablePtr_800bdba4 = null;
      }
    }

    //LAB_80103598
    if(charSlot < charCount - 1) {
      if(renderablePtr_800bdba8 == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 350, 16);
        renderable._18 = 0x25;
        renderable._1c = 0x2c;
        renderablePtr_800bdba8 = renderable;
        FUN_801033cc(renderable);
      }
      //LAB_801035e8
    } else if(renderablePtr_800bdba8 != null) {
      fadeOutArrow(renderablePtr_800bdba8);
      renderablePtr_800bdba8 = null;
    }

    //LAB_80103604
  }

  @Method(0x8010376cL)
  public static void renderGlyphs(final UnboundedArrayRef<MenuGlyph06> glyphs, final int x, final int y) {
    //LAB_801037ac
    for(int i = 0; glyphs.get(i).glyph_00.get() != 0xff; i++) {
      final Renderable58 s0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);

      initGlyph(s0, glyphs.get(i));

      s0.x_40 += x;
      s0.y_44 += y;
    }

    //LAB_801037f4
  }

  public static Renderable58 allocateUiElement(final Renderable58 renderable, final int startGlyph, final int endGlyph, final int x, final int y) {
    if(endGlyph >= startGlyph) {
      renderable.glyph_04 = startGlyph;
      renderable.startGlyph_10 = startGlyph;
      renderable.endGlyph_14 = endGlyph;
    } else {
      //LAB_80103870
      renderable.glyph_04 = startGlyph;
      renderable.startGlyph_10 = endGlyph;
      renderable.endGlyph_14 = startGlyph;
      renderable.flags_00 |= 0x20;
    }

    //LAB_80103888
    if(startGlyph == endGlyph) {
      renderable.flags_00 |= 0x4;
    }

    //LAB_801038a4
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = 0;
    renderable.x_40 = x;
    renderable.y_44 = y;

    return renderable;
  }

  @Method(0x80103818L)
  public static Renderable58 allocateUiElement(final int startGlyph, final int endGlyph, final int x, final int y) {
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    return allocateUiElement(renderable, startGlyph, endGlyph, x, y);
  }

  @Method(0x80103910L)
  public static Renderable58 renderItemIcon(final int glyph, final int x, final int y, final long flags) {
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.itemIcons_c6a4(), null);
    renderable.flags_00 |= flags | 0x4;
    renderable.glyph_04 = glyph;
    renderable.startGlyph_10 = glyph;
    renderable.endGlyph_14 = glyph;
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = 0;
    renderable.x_40 = x;
    renderable.y_44 = y;
    return renderable;
  }

  @Method(0x801039a0L)
  public static boolean canEquip(final int equipmentId, final int charIndex) {
    return charIndex != -1 && equipmentId < 0xc0 && (characterValidEquipment_80114284.offset(charIndex).get() & equipmentStats_80111ff0.get(equipmentId).equipableFlags_03.get()) != 0;
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

    if((!canEquip(equipmentId, charIndex))) {
      return 0xff;
    }

    final int slot = getEquipmentSlot(equipmentId);
    if(slot == -1) {
      //LAB_80103ab8
      return 0xff;
    }

    //LAB_80103ac0
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    int previousId = charData.equipment_14[slot];
    charData.equipment_14[slot] = equipmentId;

    if(previousId == 0xff) {
      previousId = 0x100;
    }

    //LAB_80103af4
    //LAB_80103af8
    return previousId;
  }

  @Method(0x80103b10L)
  public static void cacheCharacterSlots() {
    characterCount_8011d7c4.set(0);

    //LAB_80103b48
    int usedCharacterSlots = 0;
    for(int slot = 0; slot < 9; slot++) {
      secondaryCharIndices_800bdbf8.get(slot).set(-1);
      characterIndices_800bdbb8.get(slot).set(-1);

      if((gameState_800babc8.charData_32c[slot].partyFlags_04 & 0x1) != 0) {
        characterIndices_800bdbb8.get(characterCount_8011d7c4.get()).set(slot);
        characterCount_8011d7c4.incr();

        if(gameState_800babc8.charIds_88[0] != slot && gameState_800babc8.charIds_88[1] != slot && gameState_800babc8.charIds_88[2] != slot) {
          secondaryCharIndices_800bdbf8.get(usedCharacterSlots).set(slot);
          usedCharacterSlots++;
        }
      }

      //LAB_80103bb4
    }
  }

  @Method(0x80103cc4L)
  public static void renderText(final LodString text, final int x, final int y, final TextColour colour) {
    final TextColour shadowColour;
    if(colour == TextColour.LIME) {
      //LAB_80103d18
      shadowColour = TextColour.GREEN;
    } else if(colour == TextColour.MIDDLE_BROWN) {
      //LAB_80103d20
      shadowColour = TextColour.LIGHT_BROWN;
    } else {
      shadowColour = TextColour.MIDDLE_BROWN;
    }

    //LAB_80103d24
    //LAB_80103d28
    Scus94491BpeSegment_8002.renderText(text, x    , y    , colour, 0);
    Scus94491BpeSegment_8002.renderText(text, x    , y + 1, shadowColour, 0);
    Scus94491BpeSegment_8002.renderText(text, x + 1, y    , shadowColour, 0);
    Scus94491BpeSegment_8002.renderText(text, x + 1, y + 1, shadowColour, 0);
  }

  @Method(0x80103dd4L)
  public static int textLength(final LodString text) {
    //LAB_80103ddc
    int v1;
    for(v1 = 0; v1 < 0xff; v1++) {
      if(text.charAt(v1) == 0xa0ff) {
        break;
      }
    }

    //LAB_80103dfc
    return v1;
  }

  @Method(0x80103e90L)
  public static void renderCentredText(final LodString text, final int x, final int y, final TextColour colour) {
    renderText(text, x - textWidth(text) / 2, y, colour);
  }

  @Method(0x801038d4L)
  public static Renderable58 allocateOneFrameGlyph(final int glyph, final int x, final int y) {
    final Renderable58 renderable = allocateUiElement(glyph, glyph, x, y);
    renderable.flags_00 |= 0x8;
    return renderable;
  }

  @Method(0x80104738L)
  public static int loadItemsAndEquipmentForDisplay(final List<MenuItemStruct04> equipment, final List<MenuItemStruct04> items, final long a0) {
    equipment.clear();
    items.clear();

    for(int i = 0; i < gameState_800babc8.items_2e9.size(); i++) {
      final MenuItemStruct04 item = new MenuItemStruct04();
      item.itemId_00 = gameState_800babc8.items_2e9.getInt(i);
      item.flags_02 = 0;
      items.add(item);
    }

    int equipmentIndex;
    for(equipmentIndex = 0; equipmentIndex < gameState_800babc8.equipment_1e8.size(); equipmentIndex++) {
      final MenuItemStruct04 item = new MenuItemStruct04();

      item.itemId_00 = gameState_800babc8.equipment_1e8.getInt(equipmentIndex);
      item.flags_02 = 0;

      if(a0 != 0 && itemCantBeDiscarded(gameState_800babc8.equipment_1e8.getInt(equipmentIndex))) {
        item.flags_02 |= 0x2000;
      }

      equipment.add(item);
    }

    int equippedItemsCount = 0;

    if(a0 == 0) {
      for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
        for(int equipmentSlot = 0; equipmentSlot < 5; equipmentSlot++) {
          if(gameState_800babc8.charData_32c[characterIndices_800bdbb8.get(i).get()].equipment_14[equipmentSlot] != 0xff) {
            final MenuItemStruct04 item = new MenuItemStruct04();
            item.itemId_00 = gameState_800babc8.charData_32c[characterIndices_800bdbb8.get(i).get()].equipment_14[equipmentSlot];
            item.flags_02 = 0x3000 | characterIndices_800bdbb8.get(i).get();
            equipment.add(item);

            equippedItemsCount++;
            equipmentIndex++;
          }
        }
      }
    }

    return equippedItemsCount;
  }

  public static int loadAdditions(final int charIndex, @Nullable final MenuAdditionInfo[] additions) {
    if(additions != null) {
      for(int i = 0; i < 9; i++) {
        additions[i].offset_00 = -1;
        additions[i].index_01 = -1;
      }
    }

    if(charIndex == -1) {
      return 0;
    }

    if(additionOffsets_8004f5ac.get(charIndex).get() == -1) { // No additions (Shiranda)
      return 0;
    }

    int t5 = 0;
    int t0 = 0;
    for(int additionIndex = 0; additionIndex < additionCounts_8004f5c0.get(charIndex).get(); additionIndex++) {
      final AdditionUnlockEvent event = EventManager.INSTANCE.postEvent(new AdditionUnlockEvent(additionOffsets_8004f5ac.get(charIndex).get() + additionIndex, additionData_80052884.get(additionOffsets_8004f5ac.get(charIndex).get() + additionIndex).level_00.get()));
      additionData_80052884.get(additionOffsets_8004f5ac.get(charIndex).get() + additionIndex).level_00.set(event.additionLevel);

      final int level = additionData_80052884.get(additionOffsets_8004f5ac.get(charIndex).get() + additionIndex).level_00.get();

      if(level == -1 && (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x40) != 0) {
        if(additions != null) {
          additions[t0].offset_00 = additionOffsets_8004f5ac.get(charIndex).get() + additionIndex;
          additions[t0].index_01 = additionIndex;
        }

        t0++;
      } else if(level > 0 && level <= gameState_800babc8.charData_32c[charIndex].level_12) {
        if(additions != null) {
          additions[t0].offset_00 = additionOffsets_8004f5ac.get(charIndex).get() + additionIndex;
          additions[t0].index_01 = additionIndex;
        }

        if(gameState_800babc8.charData_32c[charIndex].additionLevels_1a[additionIndex] == 0) {
          gameState_800babc8.charData_32c[charIndex].additionLevels_1a[additionIndex] = 1;
        }

        if(level == gameState_800babc8.charData_32c[charIndex].level_12) {
          t5 = additionOffsets_8004f5ac.get(charIndex).get() + additionIndex + 1;
        }

        t0++;
      }
    }

    return t5;
  }

  @Method(0x80104b1cL)
  public static void initGlyph(final Renderable58 a0, final MenuGlyph06 glyph) {
    if(glyph.glyph_00.get() != 0xff) {
      a0.glyph_04 = glyph.glyph_00.get();
      a0.flags_00 |= 0x4;
    }

    //LAB_80104b40
    a0.tpage_2c = 0x19;
    a0.clut_30 = 0;
    a0.x_40 = glyph.x_02.get();
    a0.y_44 = glyph.y_04.get();
  }

  @Method(0x80104b60L)
  public static void FUN_80104b60(final Renderable58 a0) {
    a0._28 = 0x1;
    a0._34 = 0;
    a0._38 = 0;
    a0.z_3c = 35;
  }

  @Method(0x80104b7cL)
  public static boolean hasDragoon(final long dragoons, final int charIndex) {
    //LAB_80104b94
    if(charIndex == -1) {
      return false;
    }

    //LAB_80104be0
    if(charIndex == 0 && (dragoons & 0xff) >>> 7 != 0) { // Divine
      return true;
    }

    //LAB_80104c24
    //LAB_80104c28
    return (dragoons & 0x1L << (_800fba58.offset(charIndex * 0x4L).get() & 0x1fL)) > 0;
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
  public static int renderThreeDigitNumberComparison(final int x, final int y, final int currentVal, int newVal) {
    long flags = 0;
    final int clut;
    if(currentVal < newVal) {
      clut = 0x7c6b;
      //LAB_80105090
    } else if(currentVal > newVal) {
      clut = 0x7c2b;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      //LAB_80105138
      //LAB_8010513c
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      renderable.x_40 = x;
      renderable.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105190
    s0 = newVal / 10 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_801051ec
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      //LAB_8010521c
      //LAB_80105220
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      renderable.x_40 = x + 6;
      renderable.y_44 = y;
    }

    //LAB_80105274
    s0 = newVal % 10;
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    //LAB_801052d8
    //LAB_801052dc
    renderable.flags_00 |= 0xc;
    renderable.glyph_04 = s0;
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = clut;
    renderable.z_3c = 0x21;
    renderable.x_40 = x + 12;
    renderable.y_44 = y;
    return clut;
  }

  public static void renderFraction(final int x, final int y, final int numerator, final int denominator) {
    final int width = renderRightAlignedNumber(x, y, denominator);
    allocateUiElement(0xb, 0xb, x - width - 5, y).flags_00 |= 0x8;
    renderRightAlignedNumber(x - width - 5, y, numerator);
  }

  @Method(0x80105350L)
  public static void renderFourDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0, 4);
  }

  /** Does something different with CLUT */
  @Method(0x8010568cL)
  public static void renderFourDigitNumber(final int x, final int y, int value, final int max) {
    int clut = 0;
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
      clut = 0x7cab;
    }

    //LAB_801056f0
    if(value < max / 10) {
      clut = 0x7c2b;
    }

    //LAB_80105714
    int s0 = value / 1_000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105784
      //LAB_80105788
      renderable.flags_00 |= 0x4;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x;
      renderable.y_44 = y;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      flags |= 0x1L;
    }

    //LAB_801057c0
    //LAB_801057d0
    s0 = value / 100 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105830
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105860
      //LAB_80105864
      renderable.flags_00 |= 0x4;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x + 6;
      renderable.y_44 = y;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      flags |= 0x1L;
    }

    //LAB_801058a0
    //LAB_801058ac
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105908
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105938
      //LAB_8010593c
      renderable.flags_00 |= 0x4;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x + 12;
      renderable.y_44 = y;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
    }

    //LAB_80105978
    //LAB_80105984
    s0 = value % 10;
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    renderable.glyph_04 = s0;
    //LAB_801059e8
    //LAB_801059ec
    renderable.flags_00 |= 0x4;
    renderable.tpage_2c = 0x19;
    renderable.x_40 = x + 18;
    renderable.y_44 = y;
    renderable.clut_30 = clut;
    renderable.z_3c = 0x21;
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
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105b10
      //LAB_80105b14
      struct.flags_00 |= 0x4;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105b4c
    s0 = value / 10_000 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105ba8
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105bd8
      //LAB_80105bdc
      struct.flags_00 |= 0x4;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 6;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105c18
    s0 = value / 1_000 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105c70
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105ca0
      //LAB_80105ca4
      struct.flags_00 |= 0x4;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 12;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105ce0
    s0 = value / 100 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105d38
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105d68
      //LAB_80105d6c
      struct.flags_00 |= 0x4;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 18;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105da4
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1L) != 0) {
      //LAB_80105dfc
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105e2c
      //LAB_80105e30
      struct.flags_00 |= 0x4;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 24;
      struct.y_44 = y;
    }

    //LAB_80105e68
    s0 = value % 10;
    final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    struct.glyph_04 = s0;
    //LAB_80105ecc
    //LAB_80105ed0
    struct.flags_00 |= 0x4;
    struct.tpage_2c = 0x19;
    struct.clut_30 = 0;
    struct.z_3c = 0x21;
    struct.x_40 = x + 30;
    struct.y_44 = y;
  }

  @Method(0x80105f2cL)
  public static void renderEightDigitNumber(final int x, final int y, final int value, final int flags) {
    renderNumber(x, y, value, flags, 8);
  }

  @Method(0x801065bcL)
  public static void renderFiveDigitNumber(final int x, final int y, final int value) {
    renderNumber(x, y, value, 0x2, 5);
  }

  public static int renderRightAlignedNumber(final int x, final int y, final int value) {
    final int digitCount = MathHelper.digitCount(value);

    int totalWidth = 0;
    for(int i = 0; i < digitCount; i++) {
      final int digit = value / (int)Math.pow(10, i) % 10;

      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.flags_00 |= 0xc;
      struct.glyph_04 = digit;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x - (i + 1) * 6;
      totalWidth += 6;

      struct.y_44 = y;
    }

    return totalWidth;
  }

  /**
   * @param flags Bitset - 0x1: render leading zeros, 0x2: unload at end of frame
   */
  public static void renderNumber(final int x, final int y, int value, int flags, final int digitCount) {
    if(value >= Math.pow(10, digitCount)) {
      value = (int)Math.pow(10, digitCount) - 1;
    }

    for(int i = 0; i < digitCount; i++) {
      final int digit = value / (int)Math.pow(10, digitCount - (i + 1)) % 10;

      if(digit != 0 || i == digitCount - 1 || (flags & 0x1) != 0) {
        final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
        struct.flags_00 |= (flags & 0x2) != 0 ? 0xc : 0x4;
        struct.glyph_04 = digit;
        struct.tpage_2c = 0x19;
        struct.clut_30 = 0;
        struct.z_3c = 33;
        struct.x_40 = x + 6 * i;
        struct.y_44 = y;
        flags |= 0x1;
      }
    }
  }

  @Method(0x80107764L)
  public static void renderThreeDigitNumber(final int x, final int y, final int value, final int flags) {
    renderNumber(x, y, value, flags, 3);
  }

  @Method(0x801079fcL)
  public static void renderTwoDigitNumber(final int x, final int y, final int value, final int flags) {
    renderNumber(x, y, value, flags, 2);
  }

  @Method(0x80107cb4L)
  public static void renderCharacter(final int x, final int y, final int character) {
    final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    v0.flags_00 |= 0x4;
    v0.glyph_04 = character;
    v0.tpage_2c = 0x19;
    v0.clut_30 = 0x7ca9;
    v0.z_3c = 0x21;
    v0.x_40 = x;
    v0.y_44 = y;
  }

  @Method(0x80107d34L)
  public static void renderThreeDigitNumberComparisonWithPercent(final int x, final int y, final int currentVal, final int newVal) {
    final int clut = renderThreeDigitNumberComparison(x, y, currentVal, newVal);
    final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    v0.flags_00 |= 0xc;
    v0.glyph_04 = 0xc;
    v0.tpage_2c = 0x19;
    v0.clut_30 = clut;
    v0.z_3c = 0x21;
    v0.x_40 = x + 20;
    v0.y_44 = y;
  }

  @Method(0x80107dd4L)
  public static void renderXp(final int x, final int y, final int xp) {
    if(xp != 0) {
      renderSixDigitNumber(x, y, xp);
    } else {
      //LAB_80107e08
      final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      v0.flags_00 |= 0x4;
      v0.glyph_04 = 218;
      v0.tpage_2c = 0x19;
      v0.clut_30 = 0x7ca9;
      v0.z_3c = 0x21;
      v0.x_40 = x + 30;
      v0.y_44 = y;
    }

    //LAB_80107e58
  }

  @Method(0x80107e70L)
  public static boolean renderCharacterStatusEffect(final int x, final int y, final int charIndex) {
    //LAB_80107e90
    final int status = gameState_800babc8.charData_32c[charIndex].status_10;

    if((tickCount_800bb0fc.get() & 0x10) == 0) {
      return false;
    }

    int statusIndex = status & 0x1;

    if((status & 0x2) != 0) {
      statusIndex = 2;
    }

    //LAB_80107f00
    if((status & 0x4) != 0) {
      statusIndex = 3;
    }

    //LAB_80107f10
    if((status & 0x8) != 0) {
      statusIndex = 4;
    }

    //LAB_80107f1c
    if((status & 0x10) != 0) {
      statusIndex = 5;
    }

    //LAB_80107f28
    if((status & 0x20) != 0) {
      statusIndex = 6;
    }

    //LAB_80107f34
    if((status & 0x40) != 0) {
      statusIndex = 7;
    }

    //LAB_80107f40
    if((status & 0x80) != 0) {
      statusIndex = 8;
    }

    //LAB_80107f50
    if(statusIndex == 0) {
      //LAB_80107f88
      return false;
    }

    final MenuStatus08 menuStatus = menuStatus_800fba7c.get(statusIndex - 1);
    renderCentredText(menuStatus.text_00.deref(), x + 24, y, menuStatus.colour_04.get());

    //LAB_80107f8c
    return true;
  }

  @Method(0x80107f9cL)
  public static void renderCharacterSlot(final int x, final int y, final int charId, final boolean allocate, final boolean dontSelect) {
    if(charId != -1) {
      if(allocate) {
        allocateUiElement( 74,  74, x, y).z_3c = 33;
        allocateUiElement(153, 153, x, y);

        if(charId < 9) {
          final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.portraits_cfac(), null);
          initGlyph(struct, glyph_801142d4);
          struct.glyph_04 = charId;
          struct.tpage_2c++;
          struct.z_3c = 33;
          struct.x_40 = x + 8;
          struct.y_44 = y + 8;
        }

        //LAB_80108098
        final ActiveStatsa0 stats = stats_800be5f8[charId];
        renderTwoDigitNumber(x + 154, y + 6, stats.level_0e);
        renderTwoDigitNumber(x + 112, y + 17, stats.dlevel_0f);
        renderThreeDigitNumber(x + 148, y + 17, stats.sp_08);
        renderFourDigitNumber(x + 100, y + 28, stats.hp_04, stats.maxHp_66);
        renderCharacter(x + 124, y + 28, 11);
        renderFourDigitNumber(x + 142, y + 28, stats.maxHp_66);
        renderThreeDigitNumber(x + 106, y + 39, stats.mp_06);
        renderCharacter(x + 124, y + 39, 11);
        renderThreeDigitNumber(x + 148, y + 39, stats.maxMp_6e);
        renderSixDigitNumber(x + 88, y + 50, gameState_800babc8.charData_32c[charId].xp_00);
        renderCharacter(x + 124, y + 50, 11);
        renderXp(x + 130, y + 50, getXpToNextLevel(charId));

        // Render "don't select" overlay
        if(dontSelect) {
          final Renderable58 struct = allocateUiElement(113, 113, x + 56, y + 24);
          struct.z_3c = 33;
        }
      }

      //LAB_80108218
      if(!renderCharacterStatusEffect(x + 48, y + 3, charId)) {
        renderText(characterNames_801142dc.get(charId).deref(), x + 48, y + 3, TextColour.BROWN);
      }
    }

    //LAB_80108270
  }

  @Method(0x801085e0L)
  public static void renderCharacterStats(final int charIndex, final int equipmentId, final boolean allocate) {
    if(charIndex != -1) {
      final ActiveStatsa0 statsTmp;

      if(equipmentId != 0xff) {
        final int[] oldEquipment = Arrays.copyOf(gameState_800babc8.charData_32c[charIndex].equipment_14, 5);

        //LAB_80108638
        equipItem(equipmentId, charIndex);
        loadCharacterStats(0);

        //LAB_80108694
        statsTmp = new ActiveStatsa0(stats_800be5f8[charIndex]);

        //LAB_801086e8
        System.arraycopy(oldEquipment, 0, gameState_800babc8.charData_32c[charIndex].equipment_14, 0, 5);

        loadCharacterStats(0);
      } else {
        //LAB_80108720
        //LAB_80108740
        statsTmp = new ActiveStatsa0(stats_800be5f8[charIndex]);
      }

      //LAB_80108770
      final ActiveStatsa0 stats = stats_800be5f8[charIndex];
      renderThreeDigitNumberComparison( 58, 116, stats.bodyAttack_6a, statsTmp.bodyAttack_6a);
      renderThreeDigitNumberComparison( 90, 116, stats.gearAttack_88, statsTmp.gearAttack_88);
      renderThreeDigitNumberComparison(122, 116, stats.bodyAttack_6a + stats.gearAttack_88, statsTmp.bodyAttack_6a + statsTmp.gearAttack_88);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 116, stats.dragoonAttack_72, statsTmp.dragoonAttack_72);
      }

      //LAB_801087fc
      renderThreeDigitNumberComparison( 58, 128, stats.bodyDefence_6c, statsTmp.bodyDefence_6c);
      renderThreeDigitNumberComparison( 90, 128, stats.gearDefence_8c, statsTmp.gearDefence_8c);
      renderThreeDigitNumberComparison(122, 128, stats.bodyDefence_6c + stats.gearDefence_8c, statsTmp.bodyDefence_6c + statsTmp.gearDefence_8c);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 128, stats.dragoonDefence_74, statsTmp.dragoonDefence_74);
      }

      //LAB_8010886c
      renderThreeDigitNumberComparison( 58, 140, stats.bodyMagicAttack_6b, statsTmp.bodyMagicAttack_6b);
      renderThreeDigitNumberComparison( 90, 140, stats.gearMagicAttack_8a, statsTmp.gearMagicAttack_8a);
      renderThreeDigitNumberComparison(122, 140, stats.bodyMagicAttack_6b + stats.gearMagicAttack_8a, statsTmp.bodyMagicAttack_6b + statsTmp.gearMagicAttack_8a);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 140, stats.dragoonMagicAttack_73, statsTmp.dragoonMagicAttack_73);
      }

      //LAB_801088dc
      renderThreeDigitNumberComparison( 58, 152, stats.bodyMagicDefence_6d, statsTmp.bodyMagicDefence_6d);
      renderThreeDigitNumberComparison( 90, 152, stats.gearMagicDefence_8e, statsTmp.gearMagicDefence_8e);
      renderThreeDigitNumberComparison(122, 152, stats.bodyMagicDefence_6d + stats.gearMagicDefence_8e, statsTmp.bodyMagicDefence_6d + statsTmp.gearMagicDefence_8e);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 152, stats.dragoonMagicDefence_75, statsTmp.dragoonMagicDefence_75);
      }

      //LAB_8010894c
      renderThreeDigitNumberComparison( 58, 164, stats.bodySpeed_69, statsTmp.bodySpeed_69);
      renderThreeDigitNumberComparison( 90, 164, stats.gearSpeed_86, statsTmp.gearSpeed_86);
      renderThreeDigitNumberComparison(122, 164, stats.bodySpeed_69 + stats.gearSpeed_86, statsTmp.bodySpeed_69 + statsTmp.gearSpeed_86);

      renderThreeDigitNumberComparisonWithPercent( 90, 176, stats.attackHit_90, statsTmp.attackHit_90);
      renderThreeDigitNumberComparisonWithPercent(122, 176, stats.attackHit_90, statsTmp.attackHit_90);
      renderThreeDigitNumberComparisonWithPercent( 90, 188, stats.magicHit_92, statsTmp.magicHit_92);
      renderThreeDigitNumberComparisonWithPercent(122, 188, stats.magicHit_92, statsTmp.magicHit_92);
      renderThreeDigitNumberComparisonWithPercent( 90, 200, stats.attackAvoid_94, statsTmp.attackAvoid_94);
      renderThreeDigitNumberComparisonWithPercent(122, 200, stats.attackAvoid_94, statsTmp.attackAvoid_94);
      renderThreeDigitNumberComparisonWithPercent( 90, 212, stats.magicAvoid_96, statsTmp.magicAvoid_96);
      renderThreeDigitNumberComparisonWithPercent(122, 212, stats.magicAvoid_96, statsTmp.magicAvoid_96);

      if(allocate) {
        allocateUiElement(0x56, 0x56, 16, 94);
      }
    }

    //LAB_80108a50
  }

  @Method(0x80108e60L)
  public static void renderCharacterEquipment(final int charIndex, final boolean allocate) {
    if(charIndex == -1) {
      return;
    }

    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];

    if(allocate) {
      allocateUiElement(0x59, 0x59, 194, 16);

      if(charData.equipment_14[0] != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14[0]), 202, 17, 0);
      }

      //LAB_80108ee4
      if(charData.equipment_14[1] != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14[1]), 202, 31, 0);
      }

      //LAB_80108f10
      if(charData.equipment_14[2] != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14[2]), 202, 45, 0);
      }

      //LAB_80108f3c
      if(charData.equipment_14[3] != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14[3]), 202, 59, 0);
      }

      //LAB_80108f68
      if(charData.equipment_14[4] != 0xff) {
        renderItemIcon(getItemIcon(charData.equipment_14[4]), 202, 73, 0);
      }
    }

    //LAB_80108f94
    //LAB_80108f98
    renderText(equipment_8011972c.get(charData.equipment_14[0]).deref(), 220, 19, TextColour.BROWN);
    renderText(equipment_8011972c.get(charData.equipment_14[1]).deref(), 220, 33, TextColour.BROWN);
    renderText(equipment_8011972c.get(charData.equipment_14[2]).deref(), 220, 47, TextColour.BROWN);
    renderText(equipment_8011972c.get(charData.equipment_14[3]).deref(), 220, 61, TextColour.BROWN);
    renderText(equipment_8011972c.get(charData.equipment_14[4]).deref(), 220, 75, TextColour.BROWN);

    //LAB_8010905c
  }

  @Method(0x80109074L)
  public static void renderString(final int stringType, final int x, final int y, final int stringIndex, final boolean allocate) {
    if(allocate) {
      allocateUiElement(0x5b, 0x5b, x, y);
    }

    //LAB_801090e0
    LodString s0 = null;
    if(stringType == 0) {
      //LAB_80109118
      if(stringIndex == 0xff) {
        return;
      }

      s0 = itemDescriptions_80117a10.get(stringIndex).deref();
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
      final LodString s3 = new LodString(len + 1);

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
      s3.charAt(a1, 0xa0ff);

      renderText(s3, x + 2, y + i * 14 + 4, TextColour.BROWN);

      if(textLength(s3) > len) {
        //LAB_80109270
        break;
      }

      //LAB_80109254
      s0 = s0.slice(textLength(s3) + s4);
    }

    //LAB_80109284
  }

  @Method(0x80109410L)
  public static void renderMenuItems(final int x, final int y, final List<MenuItemStruct04> menuItems, final int slotScroll, final int itemCount, @Nullable final Renderable58 a5, @Nullable final Renderable58 a6) {
    int s3 = slotScroll;

    //LAB_8010947c
    int i;
    for(i = 0; i < itemCount && s3 < menuItems.size(); i++) {
      final MenuItemStruct04 menuItem = menuItems.get(s3);

      //LAB_801094ac
      renderText(equipment_8011972c.get(menuItem.itemId_00).deref(), x + 21, y + FUN_800fc814(i) + 2, (menuItem.flags_02 & 0x6000) == 0 ? TextColour.BROWN : TextColour.MIDDLE_BROWN);
      renderItemIcon(getItemIcon(menuItem.itemId_00), x + 4, y + FUN_800fc814(i), 0x8L);

      final int s0 = menuItem.flags_02;
      if((s0 & 0x1000) != 0) {
        renderItemIcon(48 | s0 & 0xf, x + 148, y + FUN_800fc814(i) - 1, 0x8L).clut_30 = (500 + (s0 & 0xf) & 0x1ff) << 6 | 0x2b;
        //LAB_80109574
      } else if((s0 & 0x2000) != 0) {
        renderItemIcon(58, x + 148, y + FUN_800fc814(i) - 1, 0x8L).clut_30 = 0x7eaa;
      }

      //LAB_801095a4
      s3++;
    }

    //LAB_801095c0
    //LAB_801095d4
    //LAB_801095e0
    if(a5 != null) { // There was an NPE here when fading out item list
      if(slotScroll != 0) {
        a5.flags_00 &= 0xffff_ffbf;
      } else {
        a5.flags_00 |= 0x40;
      }
    }

    //LAB_80109614
    //LAB_80109628
    if(a6 != null) { // There was an NPE here when fading out item list
      if(i + slotScroll < menuItems.size()) {
        a6.flags_00 &= 0xffff_ffbf;
      } else {
        a6.flags_00 |= 0x40;
      }
    }
  }

  @Method(0x8010a808L)
  public static int FUN_8010a808(final int index) {
    return index * 17 + 18;
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
    int xp = gameState_800babc8.charData_32c[charIndex].xp_00;
    if(xp <= 999999) {
      xp = xp + cappedPendingXp;
    } else {
      xp = 999999;
    }

    //LAB_8010ccd4
    gameState_800babc8.charData_32c[charIndex].xp_00 = xp;
    pendingXp_8011e180.get(charIndex).sub(cappedPendingXp);

    //LAB_8010cd30
    while(gameState_800babc8.charData_32c[charIndex].xp_00 >= getXpToNextLevel(charIndex) && gameState_800babc8.charData_32c[charIndex].level_12 < 60) {
      gameState_800babc8.charData_32c[charIndex].level_12++;

      _8011e1c8.offset(charSlot).addu(0x1L);
      if(additionsUnlocked_8011e1b8.get(charSlot).get() == 0) {
        additionsUnlocked_8011e1b8.get(charSlot).set(loadAdditions(charIndex, null));
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
      gameState_800babc8.charData_32c[charIndex].dlevelXp_0e += spGained_800bc950.get(charSlot).get();

      if(gameState_800babc8.charData_32c[charIndex].dlevelXp_0e > 32000) {
        gameState_800babc8.charData_32c[charIndex].dlevelXp_0e = 32000;
      }

      //LAB_8010ceb0
      //LAB_8010cecc
      while(gameState_800babc8.charData_32c[charIndex].dlevelXp_0e >= _800fbbf0.offset(charIndex * 0x4L).deref(2).offset(gameState_800babc8.charData_32c[charIndex].dlevel_13 * 0x2L).offset(0x2L).get() && gameState_800babc8.charData_32c[charIndex].dlevel_13 < 5) {
        loadCharacterStats(0);
        final byte[] spellIndices = new byte[8];
        final int spellCount = getUnlockedDragoonSpells(spellIndices, charIndex);

        gameState_800babc8.charData_32c[charIndex].dlevel_13++;
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
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c._d2d8(), null);
    renderable.glyph_04 = startGlyph;
    renderable.startGlyph_10 = startGlyph;

    if(startGlyph != endGlyph) {
      renderable.endGlyph_14 = endGlyph;
    } else {
      renderable.endGlyph_14 = endGlyph;
      renderable.flags_00 |= 0x4;
    }

    //LAB_8010d004
    renderable.x_40 = x;
    renderable.y_44 = y;
    renderable.clut_30 = v << 6 | (u & 0x3f0) >> 4;
    renderable.tpage_2c = 0x1b;
    return renderable;
  }

  @Method(0x8010d050L)
  public static void FUN_8010d050(final InventoryMenuState nextMenuState, final long a1) {
    inventoryMenuState_800bdc28.set(InventoryMenuState.LIST_INIT_16);
    _800bdc2c.setu(a1);
    confirmDest_800bdc30.set(nextMenuState);
  }

  @Method(0x8010d078L)
  public static void FUN_8010d078(int x, int y, final int w, final int h, final int type) {
    x -= 8 + displayWidth_1f8003e0.get() / 2;
    y -= 120;

    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .pos(0, x, y)
      .pos(1, x + w, y)
      .pos(2, x, y + h)
      .pos(3, x + w, y + h);

    final int z;
    switch(type) {
      case 0 -> {
        z = 36;

        cmd
          .rgb(0, 0, 0, 1)
          .rgb(1, 0, 0, 1)
          .rgb(2, 0, 0, 1)
          .rgb(3, 0, 0, 1);
      }

      case 1 -> {
        z = 36;

        cmd
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .rgb(0, 0x80, 0x80, 0x80)
          .rgb(1,    0, 0x14, 0x50)
          .rgb(2,    0, 0x14, 0x50)
          .rgb(3,    0,    0,    0);
      }

      case 2 -> {
        z = 36;

        cmd
          .monochrome(0, 0x7f)
          .monochrome(1, 0x7f)
          .monochrome(2, 0)
          .monochrome(3, 0);
      }

      case 3 -> {
        z = 34;

        cmd
          .rgb(0, 0xff, 0x7a, 0)
          .rgb(1, 0xff, 0x7a, 0)
          .rgb(2, 0x49, 0x23, 0)
          .rgb(3, 0x49, 0x23, 0);
      }

      case 4 -> {
        z = 35;

        cmd
          .rgb(0, 0xff, 0x7a, 0)
          .rgb(1, 0xff, 0x7a, 0)
          .rgb(2, 0xff, 0x7a, 0)
          .rgb(3, 0xff, 0x7a, 0);
      }

      case 5 -> {
        z = 34;

        cmd
          .rgb(0, 0, 0x84, 0xfe)
          .rgb(1, 0, 0x84, 0xfe)
          .rgb(2, 0, 0x26, 0x48)
          .rgb(3, 0, 0x26, 0x48);
      }

      case 6 -> {
        z = 35;

        cmd
          .monochrome(0, 0x7f)
          .monochrome(1, 0x7f)
          .monochrome(2, 0)
          .monochrome(3, 0);
      }

      default -> z = 0;
    }

    //LAB_8010d2c4
    GPU.queueCommand(z, cmd);

    //LAB_8010d318
  }

  @Method(0x8010d32cL)
  public static boolean characterIsAlive(final int charSlot) {
    final int charIndex = gameState_800babc8.charIds_88[charSlot];

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
  public static void renderAdditionUnlocked(final int x, final int y, final int additionIndex, final int height) {
    FUN_8010d078(x, y + 20 - height, 134, (height + 1) * 2, 4);
    FUN_8010d078(x + 1, y + 20 - height + 1, 132, height * 2, 3);

    if(height >= 20) {
      Scus94491BpeSegment_8002.renderText(additions_8011a064.get(additionIndex).deref(), x - 4, y + 6, TextColour.WHITE, 0);
      Scus94491BpeSegment_8002.renderText(New_Addition_8011c5a8, x - 4, y + 20, TextColour.WHITE, 0);
    }

    //LAB_8010d470
  }

  @Method(0x8010d498L)
  public static void renderSpellUnlocked(final int x, final int y, final int spellIndex, final int height) {
    FUN_8010d078(x, y + 20 - height, 134, (height + 1) * 2, 6); // New spell border
    FUN_8010d078(x + 1, y + 20 - height + 1, 132, height * 2, 5); // New spell background

    if(height >= 20) {
      Scus94491BpeSegment_8002.renderText(spells_80052734.get(spellIndex).deref(), x - 4, y + 6, TextColour.WHITE, 0);
      Scus94491BpeSegment_8002.renderText(Spell_Unlocked_8011c5c4, x - 4, y + 20, TextColour.WHITE, 0);
    }

    //LAB_8010d470
  }

  @Method(0x8010d598L)
  public static int FUN_8010d598(final int charSlot) {
    final int charIndex = gameState_800babc8.charIds_88[charSlot];

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
  public static void renderPostCombatReport() {
    inventoryJoypadInput_800bdc44.setu(getJoypadInputByPriority());

    switch(inventoryMenuState_800bdc28.get()) {
      case INIT_0:
        renderablePtr_800bdc5c = null;
        uiFile_800bdc3c = null;
        resizeDisplay(320, 240);
        loadDrgnFile(0, 6665, data -> menuAssetsLoaded(data, 0));
        loadDrgnFile(0, 6666, data -> menuAssetsLoaded(data, 1));
        textZ_800bdf00.set(33);
        inventoryMenuState_800bdc28.set(InventoryMenuState.AWAIT_INIT_1);
        break;

      case AWAIT_INIT_1:
        if(uiFile_800bdc3c != null) {
          scriptStartEffect(2, 10);
          inventoryMenuState_800bdc28.set(InventoryMenuState._2);
        }
        break;

      case _2:
        if(_800bb168.get() == 0) {
          deallocateRenderables(0xffL);
          Renderable58 glyph = FUN_8010cfa0(0, 0, 165, 21, 720, 497);
          glyph._34 = 0;
          glyph._38 = 0;
          glyph = FUN_8010cfa0(2, 2, 13, 21, 720, 497);
          glyph._34 = 0;
          glyph._38 = 0;
          glyph = FUN_8010cfa0(1, 1, 13, 149, 720, 497);
          glyph._34 = 0;
          glyph._38 = 0;

          FUN_8010cfa0(0x3e, 0x3e, 24, 28, 736, 497);
          FUN_8010cfa0(0x3d, 0x3d, 24, 40, 736, 497);
          FUN_8010cfa0(0x40, 0x40, 24, 52, 736, 497);

          //LAB_8010d81c
          for(int i = 0; i < 6; i++) {
            if(i >= itemsDroppedByEnemiesCount_800bc978.get()) {
              itemsDroppedByEnemies_800bc928.get(i).set(0xff);
            }

            //LAB_8010d830
          }

          cacheCharacterSlots();

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
              pendingXp_8011e180.get(gameState_800babc8.charIds_88[charSlot]).set(totalXpFromCombat_800bc95c.get() / xpDivisor_8011e174.get());
            }
          }

          //LAB_8010d9d4
          //LAB_8010d9f8
          for(int secondaryCharSlot = 0; secondaryCharSlot < 6; secondaryCharSlot++) {
            final int secondaryCharIndex = secondaryCharIndices_800bdbf8.get(secondaryCharSlot).get();

            if(secondaryCharIndex != -1) {
              pendingXp_8011e180.get(secondaryCharIndex).set(MathHelper.safeDiv(totalXpFromCombat_800bc95c.get(), xpDivisor_8011e174.get()) / 2);
            }

            //LAB_8010da24
          }

          inventoryMenuState_800bdc28.set(InventoryMenuState.INIT_MAIN_MENU_3);
          FUN_8010e9a8(1);
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

        FUN_8010e9a8(0);
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
          gameState_800babc8.gold_94 += goldGained;
        } else {
          //LAB_8010db00
          goldGainedFromCombat_800bc920.sub(goldTick);
          gameState_800babc8.gold_94 += goldTick;
        }

        //LAB_8010db18
        if(gameState_800babc8.gold_94 > 99999999) {
          gameState_800babc8.gold_94 = 99999999;
        }

        //LAB_8010db3c
        //LAB_8010db40
        soundTick_8011e17c.addu(0x1L);

        if((soundTick_8011e17c.get() & 0x1L) != 0) {
          playSound(0x1L);
        }

        FUN_8010e9a8(0);
        break;

      case _5:
        final boolean moreXpToGive =
          givePendingXp(gameState_800babc8.charIds_88[0], 0) ||
          givePendingXp(gameState_800babc8.charIds_88[1], 1) ||
          givePendingXp(gameState_800babc8.charIds_88[2], 2) ||
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
            inventoryMenuState_800bdc28.set(InventoryMenuState.REPLACE_INIT_8);
          } else if((joypadPress_8007a398.get() & 0x20L) != 0) {
            playSound(0x2L);
            _8011e178.setu(0);
            inventoryMenuState_800bdc28.set(InventoryMenuState.CONFIG_6);
          }
        }

        FUN_8010e9a8(0);
        break;

      case CONFIG_6:
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
        renderAdditionsUnlocked((int)_8011e178.get());
        FUN_8010e9a8(0);
        break;

      case _7:
        if((int)_8011e178.get() > 0) {
          _8011e178.subu(0x2L);
        } else {
          //LAB_8010dd28
          inventoryMenuState_800bdc28.set(InventoryMenuState.REPLACE_INIT_8);
        }

        renderAdditionsUnlocked((int)_8011e178.get());
        FUN_8010e9a8(0);
        break;

      case REPLACE_INIT_8:
        if(_8011e170.get() >= 0x9L) {
          //LAB_8010dd90
          inventoryMenuState_800bdc28.set(InventoryMenuState.REPLACE_MENU_10);
        } else if(_8011e1c8.offset(_8011e170.get()).get() != 0) {
          FUN_800192d8(-0x50L, 0x2cL);
          playSound(0x9L);
          inventoryMenuState_800bdc28.set(InventoryMenuState._9);
        } else {
          //LAB_8010dd88
          _8011e170.addu(0x1L);
        }

        FUN_8010e9a8(0);
        break;

      case _9:
        FUN_8010e708(24, 152, secondaryCharIndices_800bdbf8.get((int)(_8011e170.get() - 3)).get());

        if((joypadPress_8007a398.get() & 0x60L) != 0) {
          playSound(0x2L);
          _8011e1c8.offset(_8011e170.get()).setu(0);
          inventoryMenuState_800bdc28.set(InventoryMenuState.REPLACE_INIT_8);
          _8011e170.addu(0x1L);
        }

        FUN_8010e9a8(0);
        break;

      case REPLACE_MENU_10:
        for(int charSlot = 0; charSlot < 3; charSlot++) {
          if(characterIsAlive(charSlot)) {
            levelUpDragoon(gameState_800babc8.charIds_88[charSlot], charSlot);
          }
        }

        //LAB_8010de6c
        if(spellsUnlocked_8011e1a8.get(0).get() != 0 || spellsUnlocked_8011e1a8.get(1).get() != 0 || spellsUnlocked_8011e1a8.get(2).get() != 0) {
          inventoryMenuState_800bdc28.set(InventoryMenuState._11);
        } else {
          //LAB_8010de98
          inventoryMenuState_800bdc28.set(InventoryMenuState._14);
        }

        FUN_8010e9a8(0);
        break;

      case _11:
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          _8011e178.setu(0);
          playSound(0x2L);

          //LAB_8010decc
          inventoryMenuState_800bdc28.set(InventoryMenuState.EQUIPMENT_INIT_12);
        }

        FUN_8010e9a8(0);
        break;

      case EQUIPMENT_INIT_12:
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
        renderSpellsUnlocked((int)_8011e178.get());
        FUN_8010e9a8(0);
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
        renderSpellsUnlocked((int)_8011e178.get());
        FUN_8010e9a8(0);
        break;

      case _14:
        if((joypadPress_8007a398.get() & 0x60L) != 0) {
          playSound(3);

          if(itemsDroppedByEnemiesCount_800bc978.get() == 0 || (giveItems(itemsDroppedByEnemies_800bc928, itemsDroppedByEnemiesCount_800bc978) & 0xff) == 0) {
            //LAB_8010dfac
            // No items remaining
            FUN_8010d050(InventoryMenuState._18, 0x1L);
          } else {
            // Some items remaining
            resizeDisplay(384, 240);
            deallocateRenderables(0xff);
            menuStack.pushScreen(new TooManyItemsScreen());
            inventoryMenuState_800bdc28.set(InventoryMenuState._19);
          }
        }

        //LAB_8010dfb8
        //LAB_8010dfbc
        FUN_8010e9a8(0);
        break;

      case LIST_INIT_16:
        scriptStartEffect(1, 10);
        inventoryMenuState_800bdc28.set(InventoryMenuState._17);

      case _17:
        FUN_8010e9a8(0);

        if(_800bb168.get() >= 0xff) {
          inventoryMenuState_800bdc28.set(confirmDest_800bdc30.get());
          FUN_80019470();
        }

        break;

      case _18:
        scriptStartEffect(2, 10);
        deallocateRenderables(0xffL);
        uiFile_800bdc3c = null;
        whichMenu_800bdc38 = WhichMenu.UNLOAD_POST_COMBAT_REPORT_30;
        textZ_800bdf00.set(13);
        break;

      case _19:
        menuStack.render();
        break;
    }

    //LAB_8010e09c
    //LAB_8010e0a0
    FUN_8010d078(166,  22, 136, 192, 1);
    FUN_8010d078( 14,  22, 144, 120, 1);
    FUN_8010d078( 14, 150, 144,  64, 1);
    FUN_8010d078( 0,    0, 240, 240, 0);
  }

  @Method(0x8010e114L)
  public static Renderable58 FUN_8010e114(final int x, final int y, final int charSlot) {
    if(charSlot >= 9) {
      //LAB_8010e1ec
      throw new IllegalArgumentException("Invalid character index");
    }

    final int glyph = (int)_800fbc9c.offset(charSlot).getSigned();
    final Renderable58 renderable = FUN_8010cfa0(glyph, glyph, x, y, 704, (int)_800fbc88.offset(charSlot * 0x2L).getSigned());
    renderable.z_3c = 35;

    //LAB_8010e1f0
    return renderable;
  }

  @Method(0x8010e200L)
  public static void FUN_8010e200(final int x, final int y, int val, final UnsignedIntRef a3) {
    val = val % 10;
    if(val != 0 || a3.get() != 0) {
      //LAB_8010e254
      final Renderable58 renderable = FUN_8010cfa0(val + 3, val + 3, x, y, 736, 497);
      renderable.flags_00 |= 0x8;
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
      renderable.flags_00 |= 0x8;
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
      FUN_8010d078(x + 1, y + 5, 24, 32, 2);
      final Renderable58 renderable = FUN_8010e114(x - 1, y + 4, charIndex);
      renderable.flags_00 |= 0x8;
      FUN_8010cfa0((int)_800fbca8.offset(charIndex).get(), (int)_800fbca8.offset(charIndex).get(), x + 32, y + 4, 736, 497).flags_00 |= 0x8;
      FUN_8010cfa0(0x3b, 0x3b, x + 30, y + 16, 736, 497).flags_00 |= 0x8;
      FUN_8010cfa0(0x3c, 0x3c, x + 30, y + 28, 736, 497).flags_00 |= 0x8;
      FUN_8010cfa0(0x3d, 0x3d, x, y + 40, 736, 497).flags_00 |= 0x8;
      FUN_8010cfa0(0x3c, 0x3c, x, y + 52, 736, 497).flags_00 |= 0x8;
      FUN_8010cfa0(0x3d, 0x3d, x + 10, y + 52, 736, 497).flags_00 |= 0x8;

      FUN_8010e2a0(x + 108, y + 16, gameState_800babc8.charData_32c[charIndex].level_12);

      final int dlevel;
      if(!hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        dlevel = 0;
      } else {
        dlevel = gameState_800babc8.charData_32c[charIndex].dlevel_13;
      }

      //LAB_8010e8e0
      FUN_8010e2a0(x + 108, y + 28, dlevel);
      final int xp = getXpToNextLevel(charIndex);
      FUN_8010e340(x + 76 - getXpWidth(xp), y + 40, gameState_800babc8.charData_32c[charIndex].xp_00);
      FUN_8010cfa0(0x22, 0x22, x - (getXpWidth(xp) - 114), y + 40, 736, 497).flags_00 |= 0x8;
      FUN_8010e630(x + 84, y + 40, xp);


      final int dxp = (int) _800fbbf0.offset(charIndex * 0x4L).deref(2).offset(gameState_800babc8.charData_32c[charIndex].dlevel_13 * 0x2L).offset(0x2L).get();
      FUN_8010e340(x + 76 - getXpWidth(dxp), y + 52, gameState_800babc8.charData_32c[charIndex].dlevelXp_0e);
      FUN_8010cfa0(0x22, 0x22, x - (getXpWidth(dxp) - 114), y + 52, 736, 497).flags_00 |= 0x8;
      FUN_8010e630(x + 84, y + 52, dxp);
    }

    //LAB_8010e978
  }

  @Method(0x8010e9a8L)
  public static void FUN_8010e9a8(final int a0) {
    int y1 = 24;
    int y2 = -82;
    int y3 = -70;

    //LAB_8010e9fc
    for(int i = 0; i < 3; i++) {
      if(gameState_800babc8.charIds_88[i] != -1) {
        FUN_8010e708(176, y1, gameState_800babc8.charIds_88[i]);

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
    for(int i = 0; i < itemsDroppedByEnemiesCount_800bc978.get(); i++) {
      if(itemsDroppedByEnemies_800bc928.get(i).get() != 0xff) {
        renderItemIcon(getItemIcon(itemsDroppedByEnemies_800bc928.get(i).get()), 18, y1, 0x8L);
        renderText(equipment_8011972c.get(itemsDroppedByEnemies_800bc928.get(i).get()).deref(), 28, y2, TextColour.WHITE);
      }

      //LAB_8010eb38
      y2 += 16;
      y1 += 16;
    }

    //LAB_8010eb58
    FUN_8010e490(96, 156, gameState_800babc8.gold_94);

    if(a0 != 0) {
      FUN_8010cfa0(0x3f, 0x3f, 144,  28, 736, 497);
      FUN_8010cfa0(0x3f, 0x3f, 144, 156, 736, 497);
    }

    //LAB_8010ebb0
    uploadRenderables();
    FUN_80018e84();
  }

  @Method(0x8010ebecL)
  public static void renderAdditionsUnlocked(final int height) {
    for(int i = 0; i < 3; i++) {
      if(additionsUnlocked_8011e1b8.get(i).get() != 0) {
        renderAdditionUnlocked(168, 40 + i * 64, additionsUnlocked_8011e1b8.get(i).get() - 1, height);
      }
    }
  }

  @Method(0x8010ec6cL)
  public static void renderSpellsUnlocked(final int height) {
    //LAB_8010ec98
    for(int i = 0; i < 3; i++) {
      if(spellsUnlocked_8011e1a8.get(i).get() != 0) {
        renderSpellUnlocked(168, 40 + i * 64, spellsUnlocked_8011e1a8.get(i).get() - 1, height);
      }

      //LAB_8010ecc0
    }
  }

  @Method(0x8010ececL)
  public static MessageBoxResult messageBox(final MessageBox20 messageBox) {
    final Renderable58 renderable;

    switch(messageBox.state_0c) {
      case 0:
        return MessageBoxResult.YES;

      case 1: // Allocate
        messageBox.state_0c = 2;
        messageBox.renderable_04 = null;
        messageBox.renderable_08 = allocateUiElement(149, 142, messageBox.x_1c - 50, messageBox.y_1e - 10);
        messageBox.renderable_08.z_3c = 32;
        messageBox.renderable_08._18 = 142;
        msgboxResult_8011e1e8.set(MessageBoxResult.AWAITING_INPUT);

      case 2:
        if(messageBox.renderable_08._0c != 0) {
          messageBox.state_0c = 3;
        }

        break;

      case 3:
        textZ_800bdf00.set(31);
        final int x = messageBox.x_1c + 60;
        int y = messageBox.y_1e + 7;

        messageBox.ticks_10++;

        if(messageBox.text_00 != null) {
          for(final LodString line : messageBox.text_00) {
            renderCentredText(line, x, y, TextColour.BROWN);
            y += 14;
          }
        }

        //LAB_8010eeac
        textZ_800bdf00.set(33);

        if(messageBox.type_15 == 0) {
          //LAB_8010eed8
          if(!messageBox.ignoreInput && (inventoryJoypadInput_800bdc44.get() & 0x60) != 0) {
            playSound(2);
            messageBox.state_0c = 4;
            msgboxResult_8011e1e8.set(MessageBoxResult.YES);
          }

          break;
        }

        if(messageBox.type_15 == 2) {
          //LAB_8010ef10
          if(messageBox.renderable_04 == null) {
            renderable = allocateUiElement(125, 125, messageBox.x_1c + 45, messageBox.menuIndex_18 * 14 + y + 5);
            messageBox.renderable_04 = renderable;
            renderable._38 = 0;
            renderable._34 = 0;
            messageBox.renderable_04.z_3c = 32;
          }

          //LAB_8010ef64
          textZ_800bdf00.set(31);

          renderCentredText(messageBox.yes, messageBox.x_1c + 60, y + 7, messageBox.menuIndex_18 == 0 ? TextColour.RED : TextColour.BROWN);
          renderCentredText(messageBox.no, messageBox.x_1c + 60, y + 21, messageBox.menuIndex_18 == 0 ? TextColour.BROWN : TextColour.RED);

          textZ_800bdf00.set(33);
        }

        break;

      case 4:
        messageBox.state_0c = 5;

        if(messageBox.renderable_04 != null) {
          unloadRenderable(messageBox.renderable_04);
        }

        //LAB_8010f084
        unloadRenderable(messageBox.renderable_08);
        renderable = allocateUiElement(0x8e, 0x95, messageBox.x_1c - 50, messageBox.y_1e - 10);
        messageBox.renderable_08 = renderable;
        renderable.z_3c = 32;
        messageBox.renderable_08.flags_00 |= 0x10;
        break;

      case 5:
        if(messageBox.renderable_08._0c != 0) {
          messageBox.state_0c = 6;
        }

        break;

      case 6:
        messageBox.state_0c = 0;
        return msgboxResult_8011e1e8.get();
    }

    //LAB_8010f108
    //LAB_8010f10c
    return MessageBoxResult.AWAITING_INPUT;
  }

  public static void setMessageBoxOptions(final MessageBox20 messageBox, final LodString yes, final LodString no) {
    messageBox.yes = yes;
    messageBox.no = no;
  }

  @Method(0x8010f130L)
  public static void setMessageBoxText(final MessageBox20 messageBox, @Nullable final LodString text, final int type) {
    setMessageBoxOptions(messageBox, Yes_8011c20c, No_8011c214);

    if(text != null) {
      final List<LodString> lines = new ArrayList<>();
      final int length = textLength(text);

      int lineStart = 0;
      for(int charIndex = 0; charIndex < length; charIndex++) {
        if(text.charAt(charIndex) == 0xa1ff) {
          final LodString slice = text.slice(lineStart, charIndex - lineStart + 1);
          slice.charAt(charIndex - lineStart, 0xa0ff);
          lines.add(slice);
          lineStart = charIndex + 1;
        }
      }

      lines.add(text.slice(lineStart));

      messageBox.text_00 = lines.toArray(LodString[]::new);
    } else {
      messageBox.text_00 = null;
    }

    messageBox.x_1c = 120;
    messageBox.y_1e = 100;
    messageBox.type_15 = type;
    messageBox.menuIndex_18 = 0;
    messageBox.ticks_10 = 0;
    messageBox.state_0c = 1;
  }

  @Method(0x80110030L)
  public static void loadCharacterStats(final long spc0) {
    clearCharacterStats();

    //LAB_80110174
    for(int charId = 0; charId < 9; charId++) {
      final ActiveStatsa0 stats = stats_800be5f8[charId];

      final CharacterData2c charData = gameState_800babc8.charData_32c[charId];

      final CharacterStatsEvent statsEvent = EventManager.INSTANCE.postEvent(new CharacterStatsEvent(charId));

      stats.xp_00 = statsEvent.xp;
      stats.hp_04 = statsEvent.hp;
      stats.mp_06 = statsEvent.mp;
      stats.sp_08 = statsEvent.sp;
      stats.dxp_0a = statsEvent.dxp;
      stats.flags_0c = statsEvent.flags;
      stats.level_0e = statsEvent.level;
      stats.dlevel_0f = statsEvent.dlevel;

      //LAB_801101e4
      for(int i = 0; i < 5; i++) {
        stats.equipment_30[i] = charData.equipment_14[i];
      }

      stats.selectedAddition_35 = charData.selectedAddition_19;

      //LAB_80110220
      for(int i = 0; i < 8; i++) {
        stats.additionLevels_36[i] = charData.additionLevels_1a[i];
        stats.additionXp_3e[i] = charData.additionXp_22[i];
      }

      stats.maxHp_66 = statsEvent.maxHp;
      stats.addition_68 = statsEvent.addition;
      stats.bodySpeed_69 = statsEvent.bodySpeed;
      stats.bodyAttack_6a = statsEvent.bodyAttack;
      stats.bodyMagicAttack_6b = statsEvent.bodyMagicAttack;
      stats.bodyDefence_6c = statsEvent.bodyDefence;
      stats.bodyMagicDefence_6d = statsEvent.bodyMagicDefence;

      final MagicStuff08 magicStuff = magicStuff_800fbd54.get(charId).deref().get(stats.dlevel_0f);
      stats.maxMp_6e = statsEvent.maxMp;
      stats.spellIndex_70 = statsEvent.spellId;
      stats._71 = magicStuff._03.get();
      stats.dragoonAttack_72 = statsEvent.dragoonAttack;
      stats.dragoonMagicAttack_73 = statsEvent.dragoonMagicAttack;
      stats.dragoonDefence_74 = statsEvent.dragoonDefence;
      stats.dragoonMagicDefence_75 = statsEvent.dragoonMagicDefence;

      final int additionIndex = stats.selectedAddition_35;
      if(additionIndex != -1) {
        //TODO straighten this out
        final long a0 = ptrTable_80114070.offset(additionIndex * 0x4L).deref(4).offset(stats.additionLevels_36[additionIndex - additionOffsets_8004f5ac.get(charId).get()] * 0x4L).getAddress();

        stats._9c = (int)MEMORY.ref(2, a0).offset(0x0L).get();
        stats.additionSpMultiplier_9e = (int)MEMORY.ref(1, a0).offset(0x2L).get();
        stats.additionDamageMultiplier_9f = (int)MEMORY.ref(1, a0).offset(0x3L).get();

        final AdditionHitMultiplierEvent event = EventManager.INSTANCE.postEvent(new AdditionHitMultiplierEvent(additionIndex, stats.additionLevels_36[additionIndex - additionOffsets_8004f5ac.get(charId).get()], stats.additionSpMultiplier_9e, stats.additionDamageMultiplier_9f));
        stats.additionSpMultiplier_9e = event.additionSpMulti;
        stats.additionDamageMultiplier_9f = event.additionDmgMulti;
      } else {
        stats.additionDamageMultiplier_9f = 0;
      }

      //LAB_8011042c
      applyEquipmentStats(charId);

      long v0 = _800fbd08.get(charId).get();
      if((gameState_800babc8.goods_19c[0] & 0x1 << v0) != 0) {
        stats.flags_0c |= 0x2000;

        if((gameState_800babc8.characterInitialized_4e6 >> v0 & 1) == 0) {
          gameState_800babc8.characterInitialized_4e6 |= 1 << v0;

          stats.mp_06 = statsEvent.maxMp;
          stats.maxMp_6e = statsEvent.maxMp;
        }
      } else {
        //LAB_801104ec
        stats.mp_06 = 0;
        stats.maxMp_6e = 0;
        stats.dlevel_0f = 0;
      }

      //LAB_801104f8
      if(charId == 0) {
        v0 = _800fbd08.get(9).get();

        if((gameState_800babc8.goods_19c[0] & 0x1 << v0) != 0) {
          stats.flags_0c |= 0x6000;

          stats.dlevel_0f = gameState_800babc8.charData_32c[0].dlevel_13;

          final long a1 = _800fbd08.get(0).get();

          if((gameState_800babc8.characterInitialized_4e6 >> a1 & 1) == 0) {
            gameState_800babc8.characterInitialized_4e6 |= 1 << a1;
            stats.mp_06 = statsEvent.maxMp;
            stats.maxMp_6e = statsEvent.maxMp;
          } else {
            //LAB_80110590
            stats.mp_06 = charData.mp_0a;
            stats.maxMp_6e = magicStuff.mp_00.get();
          }
        }
      }

      //LAB_801105b0
      final int maxHp = (int)(stats.maxHp_66 * ((stats.hpMulti_62 / 100.0) + 1));

      //LAB_801105f0
      stats.maxHp_66 = maxHp;

      if(stats.hp_04 > maxHp) {
        stats.hp_04 = maxHp;
      }

      //LAB_80110608
      final int maxMp = (int)(stats.maxMp_6e * ((stats.mpMulti_64 / 100.0) + 1));

      stats.maxMp_6e = maxMp;

      if(stats.mp_06 > maxMp) {
        stats.mp_06 = maxMp;
      }

      //LAB_80110654
    }

    if(spc0 == 0x1L) {
      decrementOverlayCount();
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
  public static void applyEquipmentStats(final int charId) {
    FUN_8002a86c(charId);
    final ActiveStatsa0 characterStats = stats_800be5f8[charId];

    //LAB_801108b0
    for(int equipmentSlot = 0; equipmentSlot < 5; equipmentSlot++) {
      final int equipmentId = stats_800be5f8[charId].equipment_30[equipmentSlot];

      if(equipmentId != 0xff) {
        FUN_801106cc(equipmentId);

        final EquipmentStatsEvent event = EventManager.INSTANCE.postEvent(new EquipmentStatsEvent(charId, equipmentId));

        characterStats.specialEffectFlag_76 |= event.flags;
        characterStats._77 |= event.type;
        characterStats._78 |= event._02;
        characterStats._79 |= event.equipableFlags;
        characterStats.elementFlag_7a |= event.element;
        characterStats._7b |= event._05;
        characterStats.elementalResistanceFlag_7c |= event.elementalResistance;
        characterStats.elementalImmunityFlag_7d |= event.elementalImmunity;
        characterStats.statusResistFlag_7e |= event.statusResist;
        characterStats._7f |= event._09;
        characterStats._84 += event.icon;
        characterStats.gearSpeed_86 += (short)event.speed;
        characterStats.gearAttack_88 += (short)event.attack;
        characterStats.gearMagicAttack_8a += (short)event.magicAttack;
        characterStats.gearDefence_8c += (short)event.defence;
        characterStats.gearMagicDefence_8e += (short)event.magicDefence;
        characterStats.attackHit_90 += (short)event.attackHit;
        characterStats.magicHit_92 += (short)event.magicHit;
        characterStats.attackAvoid_94 += (short)event.attackAvoid;
        characterStats.magicAvoid_96 += (short)event.magicAvoid;
        characterStats.onHitStatusChance_98 += event.statusChance;
        characterStats._99 += event._19;
        characterStats._9a += event._1a;
        characterStats.onHitStatus_9b |= event.onHitStatus;
        characterStats._80 += event.attack & 0xff; // This used to just be the low attack value, but since we aren't using two values in the event like the old table did, I dunno if this will work

        if(event.mpPerMagicalHit != 0) {
          characterStats.special1_81 |= 0x1;
        }

        if(event.spPerMagicalHit != 0) {
          characterStats.special1_81 |= 0x2;
        }

        if(event.mpPerPhysicalHit != 0) {
          characterStats.special1_81 |= 0x4;
        }

        if(event.spPerPhysicalHit != 0) {
          characterStats.special1_81 |= 0x8;
        }

        if(event.spMultiplier != 0) {
          characterStats.special1_81 |= 0x10;
        }

        if(event.physicalResistance) {
          characterStats.special1_81 |= 0x20;
        }

        if(event.magicalImmunity) {
          characterStats.special1_81 |= 0x40;
        }

        if(event.physicalImmunity) {
          characterStats.special1_81 |= 0x80;
        }

        if(event.mpMultiplier != 0) {
          characterStats.special2_82 |= 0x1;
        }

        if(event.hpMultiplier != 0) {
          characterStats.special2_82 |= 0x2;
        }

        if(event.magicalResistance) {
          characterStats.special2_82 |= 0x4;
        }

        if(event.revive != 0) {
          characterStats.special2_82 |= 0x8;
        }

        if(event.spRegen != 0) {
          characterStats.special2_82 |= 0x10;
        }

        if(event.mpRegen != 0) {
          characterStats.special2_82 |= 0x20;
        }

        if(event.hpRegen != 0) {
          characterStats.special2_82 |= 0x40;
        }

        if(event._56 != 0) {
          characterStats.special2_82 |= 0x80;
        }

        characterStats.mpPerMagicalHit_54 += (short)event.mpPerMagicalHit;
        characterStats.spPerMagicalHit_52 += (short)event.spPerMagicalHit;
        characterStats.mpPerPhysicalHit_50 += (short)event.mpPerPhysicalHit;
        characterStats.spPerPhysicalHit_4e += (short)event.spPerPhysicalHit;
        characterStats.hpMulti_62 += (short)event.hpMultiplier;
        characterStats.mpMulti_64 += (short)event.mpMultiplier;
        characterStats.spMultiplier_4c += (short)event.spMultiplier;
        characterStats.magicalResistance_60 = event.magicalResistance ? 1 : 0;
        characterStats.physicalResistance_4a = event.physicalResistance ? 1 : 0;
        characterStats.magicalImmunity_48 = event.magicalImmunity ? 1 : 0;
        characterStats.physicalImmunity_46 = event.physicalImmunity ? 1 : 0;
        characterStats.revive_5e += (short)event.revive;
        characterStats.hpRegen_58 += (short)event.hpRegen;
        characterStats.mpRegen_5a += (short)event.mpRegen;
        characterStats.spRegen_5c += (short)event.spRegen;
        characterStats._56 += (short)event._56;
      }
    }
  }
}
