package legend.game;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.memory.Method;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.inventory.Addition04;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.MainMenuScreen;
import legend.game.inventory.screens.MenuStack;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.characters.AdditionHitMultiplierEvent;
import legend.game.modding.events.characters.AdditionUnlockEvent;
import legend.game.modding.events.characters.CharacterStatsEvent;
import legend.game.modding.events.characters.XpToLevelEvent;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.EquipmentStats1c;
import legend.game.types.InventoryMenuState;
import legend.game.types.LevelStuff08;
import legend.game.types.LodString;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuAdditionInfo;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MenuGlyph06;
import legend.game.types.MenuStatus08;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.Translucency;
import legend.game.types.UiFile;
import legend.game.types.UiPart;
import legend.game.types.UiType;
import legend.game.unpacker.FileData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MEMORY;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022a94;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.clearCharacterStats;
import static legend.game.Scus94491BpeSegment_8002.clearEquipmentStats;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getJoypadInputByPriority;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_8005.standingInSavePoint_8005a368;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.inventoryJoypadInput_800bdc44;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIds_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public final class SItem {
  private SItem() { }

  public static final MenuStack menuStack = new MenuStack();

  public static final int[] charDragoonSpiritIndices_800fba58 = {0, 2, 5, 6, 4, 2, 1, 3, 5};
  public static final MenuStatus08[] menuStatus_800fba7c = {
    new MenuStatus08(new LodString("Petrify"), TextColour.MIDDLE_BROWN),
    new MenuStatus08(new LodString("Charmed"), TextColour.MIDDLE_BROWN),
    new MenuStatus08(new LodString("Confused"), TextColour.MIDDLE_BROWN),
    new MenuStatus08(new LodString("Fear"), TextColour.PURPLE),
    new MenuStatus08(new LodString("Stunned"), TextColour.MIDDLE_BROWN),
    new MenuStatus08(new LodString(""), TextColour.MIDDLE_BROWN),
    new MenuStatus08(new LodString("Dspirit"), TextColour.CYAN),
    new MenuStatus08(new LodString("Poison"), TextColour.LIME),
  };

  /** Note: arrays run into the next array's first element */
  public static final int[][] dragoonXpRequirements_800fbbf0 = {
    {0, 0, 1200, 6000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
    {0, 0, 1200, 6000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
    {0, 0, 1000, 2000, 12000, 20000, 0},
    {0, 0, 1000, 2000, 12000, 20000, 0},
    {0, 0, 1000, 6000, 12000, 20000, 0},
  };

  public static final int[] dragoonGoodsBits_800fbd08 = {0, 2, 5, 6, 4, 2, 1, 3, 5, 7};
  public static final ArrayRef<Pointer<ArrayRef<LevelStuff08>>> levelStuff_800fbd30 = MEMORY.ref(4, 0x800fbd30L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(LevelStuff08.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(LevelStuff08.class, 61, 8, LevelStuff08::new))));
  public static final ArrayRef<Pointer<ArrayRef<MagicStuff08>>> magicStuff_800fbd54 = MEMORY.ref(4, 0x800fbd54L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(MagicStuff08.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(MagicStuff08.class, 6, 8, MagicStuff08::new))));

  public static final ArrayRef<Pointer<ArrayRef<LevelStuff08>>> levelStuff_80111cfc = MEMORY.ref(4, 0x80111cfcL, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(LevelStuff08.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(LevelStuff08.class, 61, 8, LevelStuff08::new))));
  public static final ArrayRef<Pointer<ArrayRef<MagicStuff08>>> magicStuff_80111d20 = MEMORY.ref(4, 0x80111d20L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(MagicStuff08.class)), 9, 4, Pointer.deferred(4, ArrayRef.of(MagicStuff08.class, 6, 8, MagicStuff08::new))));

  public static final EquipmentStats1c[] equipmentStats_80111ff0 = new EquipmentStats1c[192];
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

  public static final ArrayRef<Pointer<ArrayRef<Addition04>>> additions_80114070 = MEMORY.ref(4, 0x80114070L, ArrayRef.of(Pointer.classFor(ArrayRef.classFor(Addition04.class)), 48, 4, Pointer.deferred(4, ArrayRef.of(Addition04.class, 6, 4, Addition04::new))));

  public static final UnboundedArrayRef<MenuGlyph06> charSwapGlyphs_80114160 = MEMORY.ref(1, 0x80114160L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> equipmentGlyphs_80114180 = MEMORY.ref(1, 0x80114180L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> characterStatusGlyphs_801141a4 = MEMORY.ref(1, 0x801141a4L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> additionGlyphs_801141e4 = MEMORY.ref(1, 0x801141e4L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> useItemGlyphs_801141fc = MEMORY.ref(1, 0x801141fcL, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> dabasMenuGlyphs_80114228 = MEMORY.ref(1, 0x80114228L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));

  public static final int[] characterValidEquipment_80114284 = {0x80, 0x40, 0x2, 0x4, 0x10, 0x40, 0x1, 0x20, 0x2};

  public static final ArrayRef<UnsignedByteRef> spellMp_80114290 = MEMORY.ref(1, 0x80114290L, ArrayRef.of(UnsignedByteRef.class, 68, 1, UnsignedByteRef::new));
  public static final MenuGlyph06 glyph_801142d4 = MEMORY.ref(1, 0x801142d4L, MenuGlyph06::new);

  public static final LodString[] chapterNames_80114248 = {
    new LodString("Ch.1 Serdian War"),
    new LodString("Ch.2 Platinum Shadow"),
    new LodString("Ch.3 Fate & Soul"),
    new LodString("Ch.4 Moon & Fate"),
  };

  public static final LodString[] characterNames_801142dc = {
    new LodString("Dart"), new LodString("Lavitz"), new LodString("Shana"), new LodString("Rose"), new LodString("Haschel"),
    new LodString("Albert"), new LodString("Meru"), new LodString("Kongol"), new LodString("Miranda"),
  };

  public static final ArrayRef<UnsignedShortRef> itemPrices_80114310 = MEMORY.ref(2, 0x80114310L, ArrayRef.of(UnsignedShortRef.class, 0x100, 2, UnsignedShortRef::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114510 = MEMORY.ref(1, 0x80114510L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));
  public static final UnboundedArrayRef<MenuGlyph06> glyphs_80114548 = MEMORY.ref(1, 0x80114548L, UnboundedArrayRef.of(0x6, MenuGlyph06::new));

  public static final LodString[] itemDescriptions_80117a10 = {
    new LodString(" "), new LodString(" "), new LodString("Fire-based attack."), new LodString(" "), new LodString("Confuses Enemy \nwith given\nprobability."),
    new LodString("Gives 50% more SP."), new LodString(" "), new LodString("Powerful but\nHP decays  \neach turn."), new LodString(" "), new LodString(" "),
    new LodString(" "), new LodString("Stuns enemy, with\na given \nprobability."), new LodString("Instantly kills \nenemy with given\nprobability."), new LodString(" "), new LodString("Darkness-based \nattack."),
    new LodString(" "), new LodString("Stuns enemy, with a\ngiven probability."), new LodString("Instantly kills \nenemy with given\nprobability."), new LodString(" "), new LodString("Frightens enemy \nwith a given\nprobability."),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString("Frightens enemy\nwith a given \nprobability."), new LodString(" "),
    new LodString(" "), new LodString("Wind-based attack."), new LodString(" "), new LodString("Light-based attack."), new LodString(" "),
    new LodString("Confuses enemy \nwith a given \nprobability."), new LodString("Poisons enemy\nwith a given \nprobability."), new LodString("Can attack all."), new LodString("Gives 50% more SP."), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString("Stuns enemy, with \na given \nprobability."), new LodString(" "), new LodString("Gives twice as \nmuch SP but not \npowerful."),
    new LodString(" "), new LodString("Stuns enemy, with\na given \nprobability."), new LodString(" "), new LodString("Thunder-based \nattack."), new LodString("Becomes powerful \ninversely to HP."),
    new LodString("Instantly kills \nenemy with given\nprobability."), new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString("When physically \nattacked SP is \naccumulated."), new LodString("Nullifies damage\ndue to fire-based\nattacks."), new LodString("Nullifies damage\ndue to wind-based\nattacks."), new LodString(" "), new LodString(" "),
    new LodString("When physically \nattacked SP is \naccumulated."), new LodString("Nullifies damage\ndue to earth-\nbased attacks."), new LodString(" "), new LodString(" "), new LodString("When physically \nattacked SP is \naccumulated."),
    new LodString("Gives 50% more SP"), new LodString("Nullifies damage\ndue to thunder-\nbased attacks."), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString("When attacked \nphysically, SP is \naccumulated."), new LodString("When attacked \nmagically, SP is \naccumulated."), new LodString("Nullifies damage\ndue to light-\nbased attacks."), new LodString("Nullifies damage\ndue to darkness-\nbased attacks."), new LodString("Nullifies damage\ndue to water-\nbased attack."),
    new LodString("Avoids \npoison/stun/arm\nblocking."), new LodString("Avoids \npoison/stun/arm\nblocking."), new LodString("Avoids \npoison/stun/arm\nblocking."), new LodString("Revives from \ndeath with a \ngiven probability."), new LodString("Greatly reduces \ndamage from \nphysical attacks."),
    new LodString(" "), new LodString(" "), new LodString("Increases hit \nrate of physical\nattacks by 10%."), new LodString(" "), new LodString("When magically \nattacked, SP is \naccumulated."),
    new LodString("When magically \nattacked, SP is \naccumulated."), new LodString("When magically \nattacked, SP is \naccumulated."), new LodString(" "), new LodString(" "), new LodString("Increases hit\nrate of magical\nattacks by 10%."),
    new LodString("When magically \nattacked, SP is \naccumulated."), new LodString("Avoids instant \ndeath."), new LodString(" "), new LodString("Avoids bewitching,\nconfusion, fear \nand dispiriting."), new LodString("Reduces damage\ndue to magical\nattacks."),
    new LodString("Raises maximum\nHP 50%."), new LodString("Raises maximum\nMP 50%."), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString("Increases escape\nrate from physical\nattacks by 5 pts."), new LodString(" "), new LodString(" "), new LodString("Increases escape\nrate from magical\nattacks by 5 pts."), new LodString("Increases escape\nrate of magi/physi\nattacks by 5 pts."),
    new LodString("Gives 20 pts. \nmore agility."), new LodString("Gives 20 pts.\nmore agility."), new LodString(" "), new LodString("Avoids the\nabnormal status\npoison."), new LodString("Avoids the\nabnormal status\ndispiriting."),
    new LodString("Avoids the\nabnormal status\narm blocking."), new LodString("Avoids the\nabnormal status\nconfusion."), new LodString("Avoids abnormal \nstatus from\nbeing stunned."), new LodString("Avoids the\nabnormal status\nfear."), new LodString("Avoids the \nabnormal status\nbewitchment."),
    new LodString("Avoids the\nabnormal status\npetrification."), new LodString("Raises physical \nattack ability\nslightly."), new LodString("Raises physical\ndefense power\nslightly."), new LodString("Raises magical\nattacking power."), new LodString("Raises magical\ndefense power."),
    new LodString("Raises physical\n& magical \nattacking power."), new LodString("Raises physical\n& magical\ndefense power."), new LodString("Raises physical \nattack & defense \npower."), new LodString("Increases escape\nrate from physical\nattack by 20 pts."), new LodString("Increases escape\nrate from magical\nattack by 20 pts."),
    new LodString("Increases A-AV\nand M-AV by\nby 20 pts."), new LodString("Raises maximum\nHP 50%."), new LodString("Doubles \nmaximum MP."), new LodString("Raises SP 50%."), new LodString("Recovers SP \neach turn."),
    new LodString("Recovers HP  \neach turn."), new LodString("Recovers MP \neach turn."), new LodString("Increases hit \nrate for attacking\nall by 20%."), new LodString("Avoids instant\ndeath."), new LodString(" "),
    new LodString("Revives from \ndeath with a \ngiven probability."), new LodString("Increases agility\nby 20 pts."), new LodString(" "), new LodString("Increases agility\nby 20 pts."), new LodString("Reduces damage\nfrom fire-based \nattack by half."),
    new LodString("Reduces damage\nfrom wind-based\nattack by half."), new LodString("Reduces damage\nfrom light-based\nattack by half."), new LodString("Reduces damage\nfrom darkness-based\nattacks by half."), new LodString("Reduces damage\nfrom water-based\nattack by half."), new LodString("Reduces damage\nfrom thunder-based\nattack by half."),
    new LodString("Reduces damage\nfrom earth-based\nattack by half."), new LodString(" "), new LodString("When damaged by\nmagic SP is \naccumulated."), new LodString("When damaged by\nmagic MP is \naccumulated."), new LodString("Avoids all\nabnormal status."),
    new LodString(" "), new LodString("When physically\ndamaged SP is\naccumulated."), new LodString(" "), new LodString("When physically\ndamaged MP is\naccumulated."), new LodString("Reduces damage\nfrom all attacks \nby half."),
    new LodString("Reduces physical\ndamage by half."), new LodString("Reduces damage\nfrom magic\nby half."), new LodString(" "), new LodString("May slightly\nincrease physical \nattack power."), new LodString("May slightly\nincrease physical\ndefense power."),
    new LodString(" "), new LodString("Automatic Addition:\nHalf Damage and SP."), new LodString("Makes Addition\ncompletely\nsuccessful."), new LodString(" "), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString(" "), new LodString(" "), new LodString(" "), new LodString("Detonates and\nattacks all."), new LodString("Thunder-based\nindividual attack\n(multi)."),
    new LodString("Fire-based\nindividual attack\n(multi)."), new LodString(" "), new LodString("Earth-based\nindividual attack\n(multi)."), new LodString("Water-based\nindividual attack\n(multi)."), new LodString("Wind-based\nindividual attack\n(multi)."),
    new LodString("Generates one of \nthe attack items."), new LodString("Light-based\nindividual attack\n(multi)."), new LodString("Darkness-based\nindividual attack\n(multi)."), new LodString("Recovers half of\nmaximum value\nof HP."), new LodString("Dissolves\npetrification."),
    new LodString("Dissolves fear,\nbewitchment, \nconfusion dispirit."), new LodString("Nullifies poison/\nstunning/arm \nblocking."), new LodString("Thunder-based\nattack for all\n(multi)."), new LodString("Earth-based\nattack for all\n(multi)."), new LodString("Fire-based\nattack for all\n(multi)."),
    new LodString("Light-based\nattack for all\n(multi)."), new LodString("Recovers 100 pts.\nof SP during \ncombat."), new LodString("Confuses minor\nenemies."), new LodString(" "), new LodString("Water-based\nattack for all\n(multi)."),
    new LodString("Stuns minor\nenemies."), new LodString("Darkness-based\nattack for all\n(multi)."), new LodString("Poisons minor\nenemies."), new LodString("Frightens minor\nenemies."), new LodString(" "),
    new LodString("Wind-based\nattack for all\n(multi)."), new LodString("Destroys \nminor enemies."), new LodString("Revitalizes and \nrecovers half of\nHP."), new LodString("Reduces risk of\nencounter."), new LodString("Minor enemy only\nattacks one ally\n3 turns (repeat)."),
    new LodString("Generates a\nrecovery item."), new LodString(" "), new LodString("Nullifies magical\nattack for 3 turns\n(repeat)."), new LodString("Nullifies physical \nattack for 3 turns \n(repeat)."), new LodString("Completely\nrecovers MP."),
    new LodString("100% sure escape \nfrom minor enemy\n(repeat)."), new LodString("Completely \nrecovers HP."), new LodString("Blocks enemy's\nmove for 3 turns\n(repeat)."), new LodString("Completely \nrecovers HP\nfor all."), new LodString("Completely \nrecovers MP\nfor all."),
    new LodString("Strength increase\nfor 3 turns\n(repeat)."), new LodString("Becomes weak\nfor 3 turns\n(repeat)."), new LodString("Doubles agility\nfor 3 turns\n(repeat)."), new LodString("Halves agility\nfor 3 turns\n(repeat)."), new LodString(" "),
    new LodString("Gives subtle\ngood aroma."), new LodString("Unbased\nattack for all\n(multi)."), new LodString("Fire-based\npowerful attack \nfor all."), new LodString("Water-based\npowerful attack\nfor all."), new LodString("Wind-based\npowerful attack\nfor all."),
    new LodString("Earth-based\npowerful attack\nfor all."), new LodString("Light-based\npowerful attack\nfor all."), new LodString("Darkness-based\npowerful attack\nfor all."), new LodString("Thunder-based\npowerful attack\nfor all."), new LodString("Recover half of\nHP for all."),
    new LodString("Unbased attack\nfor all (multi) \n(repeat)."), new LodString(" "), new LodString(" "), new LodString(" "), new LodString(" "),
    new LodString(" "),
  };

  public static final LodString[] itemNames_8011972c = {
    new LodString("Broad Sword"), new LodString("Bastard Sword"), new LodString("Heat Blade"), new LodString("Falchion"), new LodString("Mind Crush"),
    new LodString("Fairy Sword"), new LodString("Claymore"), new LodString("Soul Eater"), new LodString("Axe"), new LodString("Tomahawk"),
    new LodString("Battle Axe"), new LodString("Great Axe"), new LodString("Indora's Axe"), new LodString("Rapier"), new LodString("Shadow Cutter"),
    new LodString("Dancing Dagger"), new LodString("Flamberge"), new LodString("Gladius"), new LodString("Dragon Buster"), new LodString("Demon Stiletto"),
    new LodString("Spear"), new LodString("Lance"), new LodString("Glaive"), new LodString("Spear Of Terror"), new LodString("Partisan"),
    new LodString("Halberd"), new LodString("Twister Glaive"), new LodString("Short Bow"), new LodString("Sparkle Arrow"), new LodString("Long Bow"),
    new LodString("Bemusing Arrow"), new LodString("Virulent Arrow"), new LodString("Detonate Arrow"), new LodString("Arrow Of Force"), new LodString("Mace"),
    new LodString("Morning Star"), new LodString("War Hammer"), new LodString("Heavy Mace"), new LodString("Basher"), new LodString("Pretty Hammer"),
    new LodString("Iron Knuckle"), new LodString("Beast Fang"), new LodString("Diamond Claw"), new LodString("Thunder Fist"), new LodString("Destroyer Mace"),
    new LodString("Brass Knuckle"), new LodString("Leather Armor"), new LodString("Scale Armor"), new LodString("Chain Mail"), new LodString("Plate Mail"),
    new LodString("Saint Armor"), new LodString("Red DG Armor"), new LodString("Jade DG Armor"), new LodString("Lion Fur"), new LodString("Breast Plate"),
    new LodString("Giganto Armor"), new LodString("Gold DG Armor"), new LodString("Disciple Vest"), new LodString("Warrior Dress"), new LodString("Master's Vest"),
    new LodString("Energy Girdle"), new LodString("Violet DG Armor"), new LodString("Clothes"), new LodString("Leather Jacket"), new LodString("Silver Vest"),
    new LodString("Sparkle Dress"), new LodString("Robe"), new LodString("Silver DG Armor"), new LodString("Dark DG Armor"), new LodString("Blue DG Armor"),
    new LodString("Armor of Yore"), new LodString("Satori Vest"), new LodString("Rainbow Dress"), new LodString("Angel Robe"), new LodString("Armor Of Legend"),
    new LodString(""), new LodString("Bandana"), new LodString("Sallet"), new LodString("Armet"), new LodString("Knight Helm"),
    new LodString("Giganto Helm"), new LodString("Soul Headband"), new LodString("Felt Hat"), new LodString("Cape"), new LodString("Tiara"),
    new LodString("Jeweled Crown"), new LodString("Rose's Hair Band"), new LodString(""), new LodString("Phoenix Plume"), new LodString("Legend Casque"),
    new LodString("Dragon Helm"), new LodString("Magical Hat"), new LodString(""), new LodString("Leather Boots"), new LodString("Iron Kneepiece"),
    new LodString("Combat Shoes"), new LodString("Leather Shoes"), new LodString("Soft Boots"), new LodString("Stardust Boots"), new LodString("Magical Greaves"),
    new LodString("Dancer's Shoes"), new LodString("Bandit's Shoes"), new LodString(""), new LodString("Poison Guard"), new LodString("Active Ring"),
    new LodString("Protector"), new LodString("Panic Guard"), new LodString("Stun Guard"), new LodString("Bravery Amulet"), new LodString("Magic Ego Bell"),
    new LodString("Destone Amulet"), new LodString("Power Wrist"), new LodString("Knight Shield"), new LodString("Magical Ring"), new LodString("Spiritual Ring"),
    new LodString("Attack Badge"), new LodString("Guard Badge"), new LodString("Giganto Ring"), new LodString("Elude Cloak"), new LodString("Spirit Cloak"),
    new LodString("Sage's Cloak"), new LodString("Physical Ring"), new LodString("Amulet"), new LodString("Wargod's Sash"), new LodString("Spirit Ring"),
    new LodString("Therapy Ring"), new LodString("Mage Ring"), new LodString("Wargod's Amulet"), new LodString("Talisman"), new LodString(""),
    new LodString("Holy Ankh"), new LodString("Dancer's Ring"), new LodString(""), new LodString("Bandit's Ring"), new LodString("Red-Eye Stone"),
    new LodString("Jade Stone"), new LodString("Silver Stone"), new LodString("Darkness Stone"), new LodString("Blue Sea Stone"), new LodString("Violet Stone"),
    new LodString("Golden Stone"), new LodString(""), new LodString("Ruby Ring"), new LodString("Sapphire Pin"), new LodString("Rainbow Earring"),
    new LodString(""), new LodString("Emerald Earring"), new LodString(""), new LodString("Platinum Collar"), new LodString("Phantom Shield"),
    new LodString("Dragon Shield"), new LodString("Angel Scarf"), new LodString("Bracelet"), new LodString("Fake Power Wrist"), new LodString("Fake Shield"),
    new LodString(""), new LodString("Wargod Calling"), new LodString("Ultimate Wargod"), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""), new LodString(""), new LodString(""), new LodString("Detonate Rock"), new LodString("Spark Net"),
    new LodString("Burn Out"), new LodString(""), new LodString("Pellet"), new LodString("Spear Frost"), new LodString("Spinning Gale"),
    new LodString("Attack Ball"), new LodString("Trans Light"), new LodString("Dark Mist"), new LodString("Healing Potion"), new LodString("Depetrifier"),
    new LodString("Mind Purifier"), new LodString("Body Purifier"), new LodString("Thunderbolt"), new LodString("Meteor Fall"), new LodString("Gushing Magma"),
    new LodString("Dancing Ray"), new LodString("Spirit Potion"), new LodString("Panic Bell"), new LodString(""), new LodString("Fatal Blizzard"),
    new LodString("Stunning Hammer"), new LodString("Black Rain"), new LodString("Poison Needle"), new LodString("Midnight Terror"), new LodString(""),
    new LodString("Rave Twister"), new LodString("Total Vanishing"), new LodString("Angel's Prayer"), new LodString("Charm Potion"), new LodString("Pandemonium"),
    new LodString("Recovery Ball"), new LodString(""), new LodString("Magic Shield"), new LodString("Material Shield"), new LodString("Sun Rhapsody"),
    new LodString("Smoke Ball"), new LodString("Healing Fog"), new LodString("Magic Sig Stone"), new LodString("Healing Rain"), new LodString("Moon Serenade"),
    new LodString("Power Up"), new LodString("Power Down"), new LodString("Speed Up"), new LodString("Speed Down"), new LodString(""),
    new LodString("Sachet"), new LodString("Psyche Bomb"), new LodString("Burning Wave"), new LodString("Frozen Jet"), new LodString("Down Burst"),
    new LodString("Gravity Grabber"), new LodString("Spectral Flash"), new LodString("Night Raid"), new LodString("Flash Hall"), new LodString("Healing Breeze"),
    new LodString("Psyche Bomb X"), new LodString(""), new LodString(""), new LodString(""), new LodString(""),
    new LodString(""),
  };

  public static final LodString[] additions_8011a064 = {
    new LodString("Double Slash"), new LodString("Volcano"), new LodString("Burning Rush"), new LodString("Crush Dance"), new LodString("Madness Hero"),
    new LodString("Moon Strike"), new LodString("Blazing Dynamo"), new LodString("Dragoon Attack"), new LodString("Harpoon"), new LodString("Spinning Cane"),
    new LodString("Rod Typhoon"), new LodString("Gust of Wind Dance"), new LodString("Flower Storm"), new LodString("Dragoon Attack"), new LodString("Whip Smack"),
    new LodString("More & More"), new LodString("Hard Blade"), new LodString("Demon's Dance"), new LodString("Dragoon Attack"), new LodString("Pursuit"),
    new LodString("Inferno"), new LodString("Bone Crush"), new LodString("Dragoon Attack"), new LodString("Double Smack"), new LodString("Hammer Spin"),
    new LodString("Cool Boogie"), new LodString("Cat's Cradle"), new LodString("Perky Step"), new LodString("Dragoon Attack"), new LodString("Double Punch"),
    new LodString("Flurry of Styx"), new LodString("Summon 4 Gods"), new LodString("5 Ring Shattering"), new LodString("Hex Hammer"), new LodString("Omni-Sweep"),
    new LodString("Dragoon Attack"), new LodString("Harpoon"), new LodString("Spinning Cane"), new LodString("Rod Typhoon"), new LodString("Gust of Wind Dance"),
    new LodString("Flower Storm"), new LodString("Dragoon Attack"), new LodString("Dragoon Attack"),
  };

  public static final LodString[] goodsDescriptions_8011b75c = {
    new LodString("Dragoon Spirit \nhis father left \nhim. Fire-based."), new LodString("Dragoon Spirit \nfrom Lenus \nWater-based."), new LodString("Dragoon Spirit \nfrom Greham\nWind-based."), new LodString("Dragoon Spirit \nfound in Lohan\nEarth-based."), new LodString("Dragoon Spirit \nfrom Doel\nThunder-based."),
    new LodString("Dragoon Spirit\nfrom Shirley\nLight-based."), new LodString("Dragoon Spirit \nof mysterious Rose\nDarkness-based."), new LodString("Dragoon Spirit \nof Divine Dragon."), new LodString("Special Edition \nshows tension among\nSandora & Serdio."), new LodString("A mysterious stone \nfrom Dart's Father\nGlows when held."),
    new LodString("Key to the second \nprison tower where\nShana is held."), new LodString("An axe left in a\nshack in a field.\nIt's well-worn."), new LodString("Good spirit that\npleases the \nman in Bale."), new LodString("Dabas forced him. \nSeems useless, but\nkept as a memento."), new LodString("A bottle acquired\nin Lohan to hold\n\"Life Water\"."),
    new LodString("Life water from\na monster plant.\nRefreshes power."), new LodString("Fuel to light an\nelevator switch."), new LodString("Yellow Stone \nhidden in the \nBlack Castle."), new LodString("Blue Stone held by\nthe Spell Master \nMagi."), new LodString("Red Stone kept by\nthe janitor of the\nBlack Castle."),
    new LodString("Letter that tells \nof going alone to \nthe Gehrich Gang."), new LodString("A pass for\nZero Gravity \nValley."), new LodString("Good luck bouquet\nthrown by Kate at\nthe wedding."), new LodString("Key from the \nPhantom Ship \nCaptain."), new LodString("License to use a\nboat in Furni.\nIt's a must."),
    new LodString("A staff to confine\nDivine Dragon. \nRestrains Dragons."), new LodString("Family treasure \nof Serdio. Has\nenormous power."), new LodString("Family treasure \nof Tiberoa. Taken\naway."), new LodString("Family treasure \nof Mille Seseau.\nHidden by Flamvel."), new LodString("Attacking spell \nprepared by Savan\nof Aglis."),
    new LodString("Ultimate attack \nspell given by \nSavan for Rose."), new LodString("A certificate of\nlaw production\nin Zenebatos."), new LodString("A certificate of\nlaw enactment\nin Zenebatos."), new LodString("Dragoon Spirit \nIndora gave Kongol\nEarth-based."), new LodString("Dabas' magical\nbag. Items are \nteleported to bag."),
    new LodString("A mysterious stone \nfrom Martel for\ngetting Stardust."), new LodString("Lavitz's portrait\ndrawn in Bale. It\nlooks so real."), new LodString("Temporary event \nitem description37"), new LodString("Temporary event \nitem description38"), new LodString("Temporary event \nitem description39"),
    new LodString("Temporary event \nitem description40"), new LodString("Temporary event \nitem description41"), new LodString("Temporary event \nitem description42"), new LodString("Temporary event \nitem description43"), new LodString("Temporary event \nitem description44"),
    new LodString("Temporary event \nitem description45"), new LodString("Temporary event \nitem description46"), new LodString("Temporary event \nitem description47"), new LodString("Temporary event \nitem description48"), new LodString("Temporary event \nitem description49"),
    new LodString("Temporary event \nitem description50"), new LodString("Temporary event \nitem description51"), new LodString("Temporary event \nitem description52"), new LodString("Temporary event \nitem description53"), new LodString("Temporary event \nitem description54"),
    new LodString("Temporary event \nitem description55"), new LodString("Temporary event \nitem description56"), new LodString("Temporary event \nitem description57"), new LodString("Temporary event \nitem description58"), new LodString("Temporary event \nitem description59"),
    new LodString("Temporary event \nitem description60"), new LodString("Temporary event \nitem description61"), new LodString("Temporary event \nitem description62"), new LodString("Temporary event \nitem description63"),
  };
  public static final LodString[] goodsItemNames_8011c008 = {
    new LodString("Red Dragon DS"), new LodString("Blue Dragon DS"), new LodString("Jade Dragon DS"), new LodString("Gold Dragon DS"), new LodString("Violet Dragon DS"),
    new LodString("Silver Dragon DS"), new LodString("Dark Dragon DS"), new LodString("Divine Dragon DS"), new LodString("War Bulletin"), new LodString("Father's Stone"),
    new LodString("Prison Key"), new LodString("Axe From the Shack"), new LodString("Good Spirits"), new LodString("Shiny Bag"), new LodString("Water Bottle"),
    new LodString("Life Water"), new LodString("Magic Oil"), new LodString("Yellow Stone"), new LodString("Blue Stone"), new LodString("Red Stone"),
    new LodString("Letter From Lynn"), new LodString("Pass For Valley"), new LodString("Kate's Bouquet"), new LodString("Key to Ship"), new LodString("Boat License"),
    new LodString("Dragon Blocker"), new LodString("Moon Gem"), new LodString("Moon Dagger"), new LodString("Moon Mirror"), new LodString("Omega Bomb"),
    new LodString("Omega Master"), new LodString("Law Maker"), new LodString("Law Output"), new LodString("Gold Dragon DS"), new LodString("Magic Shiny Bag"),
    new LodString("Vanishing Stone"), new LodString("Lavitz's Picture"), new LodString("Temporary37"), new LodString("Temporary38"), new LodString("Temporary39"),
    new LodString("Temporary40"), new LodString("Temporary41"), new LodString("Temporary42"), new LodString("Temporary43"), new LodString("Temporary44"),
    new LodString("Temporary45"), new LodString("Temporary46"), new LodString("Temporary47"), new LodString("Temporary48"), new LodString("Temporary49"),
    new LodString("Temporary50"), new LodString("Temporary51"), new LodString("Temporary52"), new LodString("Temporary53"), new LodString("Temporary54"),
    new LodString("Temporary55"), new LodString("Temporary56"), new LodString("Temporary57"), new LodString("Temporary58"), new LodString("Temporary59"),
    new LodString("Temporary60"), new LodString("Temporary61"), new LodString("Temporary62"), new LodString("Temporary63"),
  };
  public static final LodString[] submapNames_8011c108 = {
    new LodString(""), new LodString("Forest"), new LodString("Forest"), new LodString("Seles"), new LodString("Hellena Prison"),
    new LodString("Prairie"), new LodString("Cave"), new LodString(""), new LodString("Bale"), new LodString("Indels Castle"),
    new LodString("Town of Hoax"), new LodString("Marshland"), new LodString("Vol. Villude"), new LodString("Nest of Dragon"), new LodString("Lohan"),
    new LodString("Shirley's Shrine"), new LodString(""), new LodString("Kazas"), new LodString("Black Castle"), new LodString("Fletz"),
    new LodString("Twin Castle"), new LodString("Barrens"), new LodString("Donau"), new LodString("Valley"), new LodString("Giganto Home"),
    new LodString(""), new LodString("The Queen Fury"), new LodString("Phantom Ship"), new LodString("Lidiera"), new LodString("Undersea Cavern"),
    new LodString("Feuno"), new LodString("Prison Island"), new LodString("Furni"), new LodString("Evergreen Frst"), new LodString("Deningrad"),
    new LodString("Crystal Palace"), new LodString("Neet"), new LodString("Wingly Forest"), new LodString("Forbidden Land"), new LodString(""),
    new LodString("Mortal Dr Mt."), new LodString(""), new LodString("Kashua Glacier"), new LodString("Flanvel Tower"), new LodString("Snowfield"),
    new LodString("Fort Magrad"), new LodString("Vellweb"), new LodString(""), new LodString("Death Frontier"), new LodString("Ulara"),
    new LodString("Zenebatos"), new LodString("Mayfil"), new LodString(""), new LodString("Rouge"), new LodString("Aglis"),
    new LodString("Divine Tree"), new LodString("Moon"),
  };
  public static final LodString[] worldMapNames_8011c1ec = {
    new LodString("So. of Serdio"),
    new LodString("No. of Serdio"),
    new LodString("Tiberoa"),
    new LodString("Illisa Bay"),
    new LodString("Mille Seseau"),
    new LodString("Gloriano"),
    new LodString("Death Frontier"),
    new LodString("Endiness"),
  };

  /** "Yes" */
  public static final LodString Yes_8011c20c = new LodString("Yes");
  /** "No" */
  public static final LodString No_8011c214 = new LodString("No");

  public static int characterCount_8011d7c4;

  public static boolean canSave_8011dc88;

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

    final XpToLevelEvent event = EVENTS.postEvent(new XpToLevelEvent(charIndex, level, xpTables[charIndex][level + 1]));

    //LAB_800fc70c
    return event.xp;
  }

  @Method(0x800fc814L)
  public static int FUN_800fc814(final int a0) {
    return 9 + a0 * 17;
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
      uiFile_800bdc3c.uiElements_0000().obj = buildUiRenderable(uiFile_800bdc3c.uiElements_0000(), "Elements");
      uiFile_800bdc3c.itemIcons_c6a4().obj = buildUiRenderable(uiFile_800bdc3c.itemIcons_c6a4(), "Item Icons");
      uiFile_800bdc3c.portraits_cfac().obj = buildUiRenderable(uiFile_800bdc3c.portraits_cfac(), "Portraits");
      uiFile_800bdc3c._d2d8().obj = buildUiRenderable(uiFile_800bdc3c._d2d8(), "d2d8");
    }

    //LAB_800fc9fc
  }

  @Method(0x800fcad4L)
  public static void renderMenus() {
    inventoryJoypadInput_800bdc44 = getJoypadInputByPriority();

    switch(inventoryMenuState_800bdc28) {
      case INIT_0 -> { // Initialize, loads some files (unknown contents)
        loadingNewGameState_800bdc34 = false;
        loadCharacterStats();

        if(engineState_8004dd20 == EngineStateEnum.WORLD_MAP_08) {
          gameState_800babc8.isOnWorldMap_4e4 = true;
          canSave_8011dc88 = true;
        } else {
          gameState_800babc8.isOnWorldMap_4e4 = false;
          canSave_8011dc88 = CONFIG.getConfig(CoreMod.SAVE_ANYWHERE_CONFIG.get()) || standingInSavePoint_8005a368;
        }

        inventoryMenuState_800bdc28 = InventoryMenuState.AWAIT_INIT_1;
      }

      case AWAIT_INIT_1 -> {
        if(uiFile_800bdc3c != null) {
          inventoryMenuState_800bdc28 = InventoryMenuState._2;
        }
      }

      case _2 -> {
        menuStack.pushScreen(new MainMenuScreen(() -> {
          menuStack.popScreen();
          inventoryMenuState_800bdc28 = InventoryMenuState.UNLOAD_125;
        }));

        inventoryMenuState_800bdc28 = InventoryMenuState.MAIN_MENU_4;
      }

      case MAIN_MENU_4 -> menuStack.render();

      case UNLOAD_125 -> {
        deallocateRenderables(0xff);

        if(uiFile_800bdc3c != null) {
          uiFile_800bdc3c.delete();
        }

        uiFile_800bdc3c = null;

        switch(whichMenu_800bdc38) {
          case RENDER_SAVE_GAME_MENU_19 ->
            whichMenu_800bdc38 = WhichMenu.UNLOAD_SAVE_GAME_MENU_20;

          case RENDER_CHAR_SWAP_MENU_24 -> {
            startFadeEffect(2, 10);
            whichMenu_800bdc38 = WhichMenu.UNLOAD_CHAR_SWAP_MENU_25;
          }

          case QUIT -> startFadeEffect(2, 10);

          default -> {
            startFadeEffect(2, 10);
            whichMenu_800bdc38 = WhichMenu.UNLOAD_INVENTORY_MENU_5;
          }
        }

        currentEngineState_8004dd04.menuClosed();

        textZ_800bdf00 = 13;
      }
    }
  }

  @Method(0x801033ccL)
  public static void FUN_801033cc(final Renderable58 a0) {
    a0.deallocationGroup_28 = 0x1;
    a0.heightScale_38 = 0;
    a0.widthScale = 0;
    a0.z_3c = 31;
  }

  @Method(0x801033e8L)
  public static void fadeOutArrow(final Renderable58 renderable) {
    unloadRenderable(renderable);

    final Renderable58 newRenderable = allocateUiElement(108, 111, renderable.x_40, renderable.y_44);
    newRenderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_ANIMATION;
    FUN_801033cc(newRenderable);
  }

  @Method(0x80103444L)
  public static void setRandomRepeatGlyph(@Nullable final Renderable58 renderable, final int repeatStartGlyph1, final int repeatEndGlyph1, final int repeatStartGlyph2, final int repeatEndGlyph2) {
    if(renderable != null) {
      if(renderable.repeatStartGlyph_18 == 0) {
        if((simpleRand() & 0x3000) != 0) {
          renderable.repeatStartGlyph_18 = repeatStartGlyph1;
          renderable.repeatEndGlyph_1c = repeatEndGlyph1;
        } else {
          //LAB_801034a0
          renderable.repeatStartGlyph_18 = repeatStartGlyph2;
          renderable.repeatEndGlyph_1c = repeatEndGlyph2;
        }
      }
    }

    //LAB_801034b0
  }

  @Method(0x801034ccL)
  public static void FUN_801034cc(final int charSlot, final int charCount) {
    setRandomRepeatGlyph(renderablePtr_800bdba4, 0x2d, 0x34, 0xaa, 0xb1);
    setRandomRepeatGlyph(renderablePtr_800bdba8, 0x25, 0x2c, 0xa2, 0xa9);

    if(charSlot != 0) {
      if(renderablePtr_800bdba4 == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 18, 16);
        renderable.repeatStartGlyph_18 = 0x2d;
        renderable.repeatEndGlyph_1c = 0x34;
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
        renderable.repeatStartGlyph_18 = 0x25;
        renderable.repeatEndGlyph_1c = 0x2c;
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
    int count = 0;
    for(int i = 0; glyphs.get(i).glyph_00.get() != 0xff; i++) {
      count++;
    }

    float offsetZ = 0.0f;

    //LAB_801037ac
    for(int i = count - 1; i >= 0; i--) {
      final Renderable58 s0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);

      initGlyph(s0, glyphs.get(i));

      s0.x_40 += x;
      s0.y_44 += y;
      s0.z_3c += offsetZ;
      offsetZ += 0.01f;
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
      renderable.flags_00 |= Renderable58.FLAG_BACKWARDS_ANIMATION;
    }

    //LAB_80103888
    if(startGlyph == endGlyph) {
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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
  public static Renderable58 renderItemIcon(final int glyph, final int x, final int y, final int flags) {
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.itemIcons_c6a4(), null);
    renderable.flags_00 |= flags | Renderable58.FLAG_NO_ANIMATION;
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
  public static boolean canEquip(final Equipment equipment, final int charIndex) {
    return (characterValidEquipment_80114284[charIndex] & equipment.equipableFlags_03) != 0;
  }

  /**
   * @return Item ID of previously-equipped item, 0xff if invalid, 0x100 if no item was equipped
   */
  @Method(0x80103a5cL)
  public static EquipItemResult equipItem(final Equipment equipment, final int charIndex) {
    if((!canEquip(equipment, charIndex))) {
      return EquipItemResult.failure();
    }

    //LAB_80103ac0
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final Equipment previousEquipment = charData.equipment_14.get(equipment.slot);
    charData.equipment_14.put(equipment.slot, equipment);

    //LAB_80103af4
    //LAB_80103af8
    return EquipItemResult.success(previousEquipment);
  }

  @Method(0x80103b10L)
  public static void cacheCharacterSlots() {
    characterCount_8011d7c4 = 0;

    //LAB_80103b48
    int usedCharacterSlots = 0;
    for(int slot = 0; slot < 9; slot++) {
      secondaryCharIds_800bdbf8[slot] = -1;
      characterIndices_800bdbb8[slot] = -1;

      if((gameState_800babc8.charData_32c[slot].partyFlags_04 & 0x1) != 0) {
        characterIndices_800bdbb8[characterCount_8011d7c4] = slot;
        characterCount_8011d7c4++;

        if(gameState_800babc8.charIds_88[0] != slot && gameState_800babc8.charIds_88[1] != slot && gameState_800babc8.charIds_88[2] != slot) {
          secondaryCharIds_800bdbf8[usedCharacterSlots] = slot;
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
    Scus94491BpeSegment_8002.renderText(text, x    , y + 1, shadowColour, 0);
    Scus94491BpeSegment_8002.renderText(text, x + 1, y    , shadowColour, 0);
    Scus94491BpeSegment_8002.renderText(text, x + 1, y + 1, shadowColour, 0);
    Scus94491BpeSegment_8002.renderText(text, x    , y    , colour, 0);
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
    renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    return renderable;
  }

  @Method(0x80104738L)
  public static void loadItemsAndEquipmentForDisplay(@Nullable final MenuEntries<Equipment> equipments, @Nullable final MenuEntries<Item> items, final long a0) {
    if(items != null) {
      items.clear();

      for(int i = 0; i < gameState_800babc8.items_2e9.size(); i++) {
        final Item item = gameState_800babc8.items_2e9.get(i);
        final MenuEntryStruct04<Item> menuEntry = MenuEntryStruct04.make(item);
        items.add(menuEntry);
      }
    }

    if(equipments != null) {
      equipments.clear();

      int equipmentIndex;
      for(equipmentIndex = 0; equipmentIndex < gameState_800babc8.equipment_1e8.size(); equipmentIndex++) {
        final Equipment equipment = gameState_800babc8.equipment_1e8.get(equipmentIndex);
        final MenuEntryStruct04<Equipment> menuEntry = MenuEntryStruct04.make(equipment);

        if(a0 != 0 && !gameState_800babc8.equipment_1e8.get(equipmentIndex).canBeDiscarded()) {
          menuEntry.flags_02 = 0x2000;
        }

        equipments.add(menuEntry);
      }

      if(a0 == 0) {
        for(int i = 0; i < characterCount_8011d7c4; i++) {
          for(final EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            if(gameState_800babc8.charData_32c[characterIndices_800bdbb8[i]].equipment_14.get(equipmentSlot) != null) {
              final Equipment equipment = gameState_800babc8.charData_32c[characterIndices_800bdbb8[i]].equipment_14.get(equipmentSlot);
              final MenuEntryStruct04<Equipment> menuEntry = MenuEntryStruct04.make(equipment);
              menuEntry.flags_02 = 0x3000 | characterIndices_800bdbb8[i];
              equipments.add(menuEntry);

              equipmentIndex++;
            }
          }
        }
      }
    }
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

    if(additionOffsets_8004f5ac[charIndex] == -1) { // No additions (Shiranda)
      return 0;
    }

    int t5 = 0;
    int t0 = 0;
    for(int additionIndex = 0; additionIndex < additionCounts_8004f5c0[charIndex]; additionIndex++) {
      final AdditionUnlockEvent event = EVENTS.postEvent(new AdditionUnlockEvent(additionOffsets_8004f5ac[charIndex] + additionIndex, additionData_80052884.get(additionOffsets_8004f5ac[charIndex] + additionIndex).level_00.get()));
      additionData_80052884.get(additionOffsets_8004f5ac[charIndex] + additionIndex).level_00.set(event.additionLevel);

      final int level = additionData_80052884.get(additionOffsets_8004f5ac[charIndex] + additionIndex).level_00.get();

      if(level == -1 && (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x40) != 0) {
        if(additions != null) {
          additions[t0].offset_00 = additionOffsets_8004f5ac[charIndex] + additionIndex;
          additions[t0].index_01 = additionIndex;
        }

        t0++;
      } else if(level > 0 && level <= gameState_800babc8.charData_32c[charIndex].level_12) {
        if(additions != null) {
          additions[t0].offset_00 = additionOffsets_8004f5ac[charIndex] + additionIndex;
          additions[t0].index_01 = additionIndex;
        }

        if(gameState_800babc8.charData_32c[charIndex].additionLevels_1a[additionIndex] == 0) {
          gameState_800babc8.charData_32c[charIndex].additionLevels_1a[additionIndex] = 1;
        }

        if(level == gameState_800babc8.charData_32c[charIndex].level_12) {
          t5 = additionOffsets_8004f5ac[charIndex] + additionIndex + 1;
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
      a0.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    }

    //LAB_80104b40
    a0.tpage_2c = 0x19;
    a0.clut_30 = 0;
    a0.x_40 = glyph.x_02.get();
    a0.y_44 = glyph.y_04.get();
  }

  @Method(0x80104b60L)
  public static void FUN_80104b60(final Renderable58 a0) {
    a0.deallocationGroup_28 = 0x1;
    a0.widthScale = 0;
    a0.heightScale_38 = 0;
    a0.z_3c = 35;
  }

  @Method(0x80104b7cL)
  public static boolean hasDragoon(final int dragoons, final int charIndex) {
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
    return (dragoons & 0x1 << charDragoonSpiritIndices_800fba58[charIndex]) != 0;
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
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
    renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
    allocateUiElement(0xb, 0xb, x - width - 5, y).flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105830
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105860
      //LAB_80105864
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105908
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105938
      //LAB_8010593c
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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
    renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105b4c
    s0 = value / 10_000 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105ba8
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105bd8
      //LAB_80105bdc
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 6;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105c18
    s0 = value / 1_000 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105c70
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105ca0
      //LAB_80105ca4
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 12;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105ce0
    s0 = value / 100 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105d38
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105d68
      //LAB_80105d6c
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 0x21;
      struct.x_40 = x + 18;
      struct.y_44 = y;
      flags |= 0x1L;
    }

    //LAB_80105da4
    s0 = value / 10 % 10;
    if(s0 != 0 || (flags & 0x1) != 0) {
      //LAB_80105dfc
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.glyph_04 = s0;
      //LAB_80105e2c
      //LAB_80105e30
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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
    struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
        struct.flags_00 |= (flags & 0x2) != 0 ? Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER : Renderable58.FLAG_NO_ANIMATION;
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
    v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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
    v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
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

    if((tickCount_800bb0fc & 0x10) == 0) {
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

    final MenuStatus08 menuStatus = menuStatus_800fba7c[statusIndex - 1];
    renderCentredText(menuStatus.text_00, x + 24, y, menuStatus.colour_04);

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
        renderText(characterNames_801142dc[charId], x + 48, y + 3, TextColour.BROWN);
      }
    }

    //LAB_80108270
  }

  @Method(0x801085e0L)
  public static void renderCharacterStats(final int charIndex, @Nullable final Equipment equipment, final boolean allocate) {
    if(charIndex != -1) {
      final ActiveStatsa0 statsTmp;

      if(equipment != null) {
        final Map<EquipmentSlot, Equipment> oldEquipment = new EnumMap<>(gameState_800babc8.charData_32c[charIndex].equipment_14);

        //LAB_80108638
        equipItem(equipment, charIndex);
        loadCharacterStats();

        //LAB_80108694
        statsTmp = new ActiveStatsa0(stats_800be5f8[charIndex]);

        //LAB_801086e8
        gameState_800babc8.charData_32c[charIndex].equipment_14.clear();
        gameState_800babc8.charData_32c[charIndex].equipment_14.putAll(oldEquipment);

        loadCharacterStats();
      } else {
        //LAB_80108720
        //LAB_80108740
        statsTmp = new ActiveStatsa0(stats_800be5f8[charIndex]);
      }

      //LAB_80108770
      final ActiveStatsa0 stats = stats_800be5f8[charIndex];
      renderThreeDigitNumberComparison( 58, 116, stats.bodyAttack_6a, statsTmp.bodyAttack_6a);
      renderThreeDigitNumberComparison( 90, 116, stats.equipmentAttack_88, statsTmp.equipmentAttack_88);
      renderThreeDigitNumberComparison(122, 116, stats.bodyAttack_6a + stats.equipmentAttack_88, statsTmp.bodyAttack_6a + statsTmp.equipmentAttack_88);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 116, stats.dragoonAttack_72, statsTmp.dragoonAttack_72);
      }

      //LAB_801087fc
      renderThreeDigitNumberComparison( 58, 128, stats.bodyDefence_6c, statsTmp.bodyDefence_6c);
      renderThreeDigitNumberComparison( 90, 128, stats.equipmentDefence_8c, statsTmp.equipmentDefence_8c);
      renderThreeDigitNumberComparison(122, 128, stats.bodyDefence_6c + stats.equipmentDefence_8c, statsTmp.bodyDefence_6c + statsTmp.equipmentDefence_8c);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 128, stats.dragoonDefence_74, statsTmp.dragoonDefence_74);
      }

      //LAB_8010886c
      renderThreeDigitNumberComparison( 58, 140, stats.bodyMagicAttack_6b, statsTmp.bodyMagicAttack_6b);
      renderThreeDigitNumberComparison( 90, 140, stats.equipmentMagicAttack_8a, statsTmp.equipmentMagicAttack_8a);
      renderThreeDigitNumberComparison(122, 140, stats.bodyMagicAttack_6b + stats.equipmentMagicAttack_8a, statsTmp.bodyMagicAttack_6b + statsTmp.equipmentMagicAttack_8a);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 140, stats.dragoonMagicAttack_73, statsTmp.dragoonMagicAttack_73);
      }

      //LAB_801088dc
      renderThreeDigitNumberComparison( 58, 152, stats.bodyMagicDefence_6d, statsTmp.bodyMagicDefence_6d);
      renderThreeDigitNumberComparison( 90, 152, stats.equipmentMagicDefence_8e, statsTmp.equipmentMagicDefence_8e);
      renderThreeDigitNumberComparison(122, 152, stats.bodyMagicDefence_6d + stats.equipmentMagicDefence_8e, statsTmp.bodyMagicDefence_6d + statsTmp.equipmentMagicDefence_8e);

      if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
        renderThreeDigitNumberComparisonWithPercent(159, 152, stats.dragoonMagicDefence_75, statsTmp.dragoonMagicDefence_75);
      }

      //LAB_8010894c
      renderThreeDigitNumberComparison( 58, 164, stats.bodySpeed_69, statsTmp.bodySpeed_69);
      renderThreeDigitNumberComparison( 90, 164, stats.equipmentSpeed_86, statsTmp.equipmentSpeed_86);
      renderThreeDigitNumberComparison(122, 164, stats.bodySpeed_69 + stats.equipmentSpeed_86, statsTmp.bodySpeed_69 + statsTmp.equipmentSpeed_86);

      renderThreeDigitNumberComparisonWithPercent( 90, 176, stats.equipmentAttackHit_90, statsTmp.equipmentAttackHit_90);
      renderThreeDigitNumberComparisonWithPercent(122, 176, stats.equipmentAttackHit_90, statsTmp.equipmentAttackHit_90);
      renderThreeDigitNumberComparisonWithPercent( 90, 188, stats.equipmentMagicHit_92, statsTmp.equipmentMagicHit_92);
      renderThreeDigitNumberComparisonWithPercent(122, 188, stats.equipmentMagicHit_92, statsTmp.equipmentMagicHit_92);
      renderThreeDigitNumberComparisonWithPercent( 90, 200, stats.equipmentAttackAvoid_94, statsTmp.equipmentAttackAvoid_94);
      renderThreeDigitNumberComparisonWithPercent(122, 200, stats.equipmentAttackAvoid_94, statsTmp.equipmentAttackAvoid_94);
      renderThreeDigitNumberComparisonWithPercent( 90, 212, stats.equipmentMagicAvoid_96, statsTmp.equipmentMagicAvoid_96);
      renderThreeDigitNumberComparisonWithPercent(122, 212, stats.equipmentMagicAvoid_96, statsTmp.equipmentMagicAvoid_96);

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

      for(final EquipmentSlot slot : EquipmentSlot.values()) {
        if(charData.equipment_14.get(slot) != null) {
          renderItemIcon(charData.equipment_14.get(slot).icon_0e, 202, 17 + 14 * slot.ordinal(), 0);
        }
      }
    }

    //LAB_80108f94
    //LAB_80108f98
    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      if(charData.equipment_14.get(slot) != null) {
        renderText(new LodString(charData.equipment_14.get(slot).name), 220, 19 + slot.ordinal() * 14, TextColour.BROWN);
      }
    }

    //LAB_8010905c
  }

  @Method(0x80109074L)
  public static void renderString(final int x, final int y, final String string, final boolean allocate) {
    if(allocate) {
      allocateUiElement(0x5b, 0x5b, x, y);
    }

    //LAB_801090e0
    LodString s0 = new LodString(string);

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
  public static void renderMenuItems(final int x, final int y, final MenuEntries<?> menuItems, final int slotScroll, final int itemCount, @Nullable final Renderable58 a5, @Nullable final Renderable58 a6) {
    int s3 = slotScroll;

    //LAB_8010947c
    int i;
    for(i = 0; i < itemCount && s3 < menuItems.size(); i++) {
      final MenuEntryStruct04<?> menuItem = menuItems.get(s3);

      //LAB_801094ac
      renderText(new LodString(menuItem.getName()), x + 21, y + FUN_800fc814(i) + 2, (menuItem.flags_02 & 0x6000) == 0 ? TextColour.BROWN : TextColour.MIDDLE_BROWN);
      renderItemIcon(menuItem.getIcon(), x + 4, y + FUN_800fc814(i), 0x8);

      final int s0 = menuItem.flags_02;
      if((s0 & 0x1000) != 0) {
        renderItemIcon(48 | s0 & 0xf, x + 148, y + FUN_800fc814(i) - 1, 0x8).clut_30 = (500 + (s0 & 0xf) & 0x1ff) << 6 | 0x2b;
        //LAB_80109574
      } else if((s0 & 0x2000) != 0) {
        renderItemIcon(58, x + 148, y + FUN_800fc814(i) - 1, 0x8).clut_30 = 0x7eaa;
      }

      //LAB_801095a4
      s3++;
    }

    //LAB_801095c0
    //LAB_801095d4
    //LAB_801095e0
    if(a5 != null) { // There was an NPE here when fading out item list
      if(slotScroll != 0) {
        a5.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
      } else {
        a5.flags_00 |= Renderable58.FLAG_INVISIBLE;
      }
    }

    //LAB_80109614
    //LAB_80109628
    if(a6 != null) { // There was an NPE here when fading out item list
      if(i + slotScroll < menuItems.size()) {
        a6.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
      } else {
        a6.flags_00 |= Renderable58.FLAG_INVISIBLE;
      }
    }
  }

  public static Obj buildUiRenderable(final UiType type, final String name) {
    final QuadBuilder builder = new QuadBuilder("UI " + name);
    int vertices = 0;

    for(final UiPart part : type.entries_08) {
      for(final RenderableMetrics14 metrics : part.metrics_00()) {
        builder
          .add()
          .uv(metrics.u_00, metrics.v_01)
          .clut((metrics.clut_04 & 0x3f) * 16, (metrics.clut_04 & 0x7fff) >>> 6)
          .vramPos((metrics.tpage_06 & 0b1111) * 64, (metrics.tpage_06 & 0b10000) != 0 ? 256 : 0)
          .bpp(Bpp.of(metrics.tpage_06 >>> 7 & 0b11))
          .posSize(1.0f, 1.0f)
          .uvSize(metrics.textureWidth, metrics.textureHeight)
        ;

        if((metrics.clut_04 & 0x8000) != 0) {
          builder.translucency(Translucency.of(metrics.tpage_06 >>> 5 & 0b11));
        }

        metrics.vertexStart = vertices;
        vertices += 4;
      }
    }

    return builder.build();
  }

  @Method(0x8010ececL)
  public static MessageBoxResult messageBox(final MessageBox20 messageBox) {
    final Renderable58 renderable;

    switch(messageBox.state_0c) {
      case 0:
        return MessageBoxResult.YES;

      case 1: // Allocate
        messageBox.state_0c = 2;
        messageBox.highlightRenderable_04 = null;
        messageBox.backgroundRenderable_08 = allocateUiElement(149, 142, messageBox.x_1c - 50, messageBox.y_1e - 10);
        messageBox.backgroundRenderable_08.z_3c = 32;
        messageBox.backgroundRenderable_08.repeatStartGlyph_18 = 142;
        messageBox.result = MessageBoxResult.AWAITING_INPUT;

      case 2:
        if(messageBox.backgroundRenderable_08.animationLoopsCompletedCount_0c != 0) {
          messageBox.state_0c = 3;
        }

        break;

      case 3:
        textZ_800bdf00 = 31;
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
        textZ_800bdf00 = 33;

        if(messageBox.type_15 == 0) {
          //LAB_8010eed8
          if(!messageBox.ignoreInput && (inventoryJoypadInput_800bdc44 & 0x60) != 0) {
            playSound(2);
            messageBox.state_0c = 4;
            messageBox.result = MessageBoxResult.YES;
          }

          break;
        }

        if(messageBox.type_15 == 2) {
          //LAB_8010ef10
          if(messageBox.highlightRenderable_04 == null) {
            renderable = allocateUiElement(125, 125, messageBox.x_1c + 45, messageBox.menuIndex_18 * 14 + y + 5);
            messageBox.highlightRenderable_04 = renderable;
            renderable.heightScale_38 = 0;
            renderable.widthScale = 0;
            messageBox.highlightRenderable_04.z_3c = 31;
          }

          //LAB_8010ef64
          textZ_800bdf00 = 31;

          renderCentredText(messageBox.yes, messageBox.x_1c + 60, y + 7, messageBox.menuIndex_18 == 0 ? TextColour.RED : TextColour.BROWN);
          renderCentredText(messageBox.no, messageBox.x_1c + 60, y + 21, messageBox.menuIndex_18 == 0 ? TextColour.BROWN : TextColour.RED);

          textZ_800bdf00 = 33;
        }

        break;

      case 4:
        messageBox.state_0c = 5;

        if(messageBox.highlightRenderable_04 != null) {
          unloadRenderable(messageBox.highlightRenderable_04);
        }

        //LAB_8010f084
        unloadRenderable(messageBox.backgroundRenderable_08);
        renderable = allocateUiElement(0x8e, 0x95, messageBox.x_1c - 50, messageBox.y_1e - 10);
        renderable.z_3c = 32;
        messageBox.backgroundRenderable_08 = renderable;
        messageBox.backgroundRenderable_08.flags_00 |= Renderable58.FLAG_DELETE_AFTER_ANIMATION;
        break;

      case 5:
        if(messageBox.backgroundRenderable_08.animationLoopsCompletedCount_0c != 0) {
          messageBox.state_0c = 6;
        }

        break;

      case 6:
        messageBox.state_0c = 0;
        return messageBox.result;
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
  public static void loadCharacterStats() {
    clearCharacterStats();

    //LAB_80110174
    for(int charId = 0; charId < 9; charId++) {
      final ActiveStatsa0 stats = stats_800be5f8[charId];

      final CharacterData2c charData = gameState_800babc8.charData_32c[charId];

      final CharacterStatsEvent statsEvent = EVENTS.postEvent(new CharacterStatsEvent(charId));

      stats.xp_00 = statsEvent.xp;
      stats.hp_04 = statsEvent.hp;
      stats.mp_06 = statsEvent.mp;
      stats.sp_08 = statsEvent.sp;
      stats.dxp_0a = statsEvent.dxp;
      stats.flags_0c = statsEvent.flags;
      stats.level_0e = statsEvent.level;
      stats.dlevel_0f = statsEvent.dlevel;

      //LAB_801101e4
      stats.equipment_30.clear();
      stats.equipment_30.putAll(charData.equipment_14);

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
      stats.spellId_70 = statsEvent.spellId;
      stats._71 = magicStuff._03.get();
      stats.dragoonAttack_72 = statsEvent.dragoonAttack;
      stats.dragoonMagicAttack_73 = statsEvent.dragoonMagicAttack;
      stats.dragoonDefence_74 = statsEvent.dragoonDefence;
      stats.dragoonMagicDefence_75 = statsEvent.dragoonMagicDefence;

      final int additionIndex = stats.selectedAddition_35;
      if(additionIndex != -1) {
        final Addition04 addition = additions_80114070.get(additionIndex).deref().get(stats.additionLevels_36[additionIndex - additionOffsets_8004f5ac[charId]]);

        stats.addition_00_9c = addition._00.get();
        stats.additionSpMultiplier_9e = addition.spMultiplier_02.get();
        stats.additionDamageMultiplier_9f = addition.damageMultiplier_03.get();

        final AdditionHitMultiplierEvent event = EVENTS.postEvent(new AdditionHitMultiplierEvent(additionIndex, stats.additionLevels_36[additionIndex - additionOffsets_8004f5ac[charId]], stats.additionSpMultiplier_9e, stats.additionDamageMultiplier_9f));
        stats.additionSpMultiplier_9e = event.additionSpMulti;
        stats.additionDamageMultiplier_9f = event.additionDmgMulti;
      } else {
        stats.additionDamageMultiplier_9f = 0;
      }

      //LAB_8011042c
      applyEquipmentStats(charId);

      final int v0 = dragoonGoodsBits_800fbd08[charId];
      if((gameState_800babc8.goods_19c[0] & 0x1 << v0) != 0) {
        stats.flags_0c |= 0x2000;

        if((gameState_800babc8.characterInitialized_4e6 & 0x1 << v0) == 0) {
          gameState_800babc8.characterInitialized_4e6 |= 0x1 << v0;

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
      if(charId == 0 && (gameState_800babc8.goods_19c[0] & 0x1 << dragoonGoodsBits_800fbd08[9]) != 0) {
        stats.flags_0c |= 0x6000;

        stats.dlevel_0f = gameState_800babc8.charData_32c[0].dlevel_13;

        final int a1 = dragoonGoodsBits_800fbd08[0];

        if((gameState_800babc8.characterInitialized_4e6 & 0x1 << a1) == 0) {
          gameState_800babc8.characterInitialized_4e6 |= 0x1 << a1;
          stats.mp_06 = statsEvent.maxMp;
          stats.maxMp_6e = statsEvent.maxMp;
        } else {
          //LAB_80110590
          stats.mp_06 = charData.mp_0a;
          stats.maxMp_6e = magicStuff.mp_00.get();
        }
      }

      //LAB_801105b0
      final int maxHp = (int)(stats.maxHp_66 * (stats.equipmentHpMulti_62 / 100.0 + 1));

      //LAB_801105f0
      stats.maxHp_66 = maxHp;

      if(stats.hp_04 > maxHp) {
        stats.hp_04 = maxHp;
      }

      //LAB_80110608
      final int maxMp = (int)(stats.maxMp_6e * (stats.equipmentMpMulti_64 / 100.0 + 1));

      stats.maxMp_6e = maxMp;

      if(stats.mp_06 > maxMp) {
        stats.mp_06 = maxMp;
      }

      //LAB_80110654
    }

    //LAB_8011069c
  }

  @Method(0x8011085cL)
  public static void applyEquipmentStats(final int charId) {
    clearEquipmentStats(charId);

    final ActiveStatsa0 characterStats = stats_800be5f8[charId];

    //LAB_801108b0
    for(final EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
      final Equipment equipment = stats_800be5f8[charId].equipment_30.get(equipmentSlot);

      if(equipment != null) {
        //TODO
//        final EquipmentStatsEvent event = EVENTS.postEvent(new EquipmentStatsEvent(charId, equipment, equipmentStats_80111ff0[equipment]));

        characterStats.specialEffectFlag_76 |= equipment.flags_00;
//        characterStats.equipmentType_77 |= equipment.type_01;
        characterStats.equipment_02_78 |= equipment._02;
        characterStats.equipmentEquipableFlags_79 |= equipment.equipableFlags_03;
        characterStats.equipmentAttackElements_7a.addAll(equipment.attackElement_04);
        characterStats.equipment_05_7b |= equipment._05;
        characterStats.equipmentElementalResistance_7c.addAll(equipment.elementalResistance_06);
        characterStats.equipmentElementalImmunity_7d.addAll(equipment.elementalImmunity_07);
        characterStats.equipmentStatusResist_7e |= equipment.statusResist_08;
        characterStats.equipment_09_7f |= equipment._09;
        characterStats.equipmentAttack1_80 += equipment.attack1_0a;
        characterStats.equipmentIcon_84 += equipment.icon_0e;
        characterStats.equipmentSpeed_86 += equipment.speed_0f;
        characterStats.equipmentAttack_88 += equipment.attack2_10 + equipment.attack1_0a;
        characterStats.equipmentMagicAttack_8a += equipment.magicAttack_11;
        characterStats.equipmentDefence_8c += equipment.defence_12;
        characterStats.equipmentMagicDefence_8e += equipment.magicDefence_13;
        characterStats.equipmentAttackHit_90 += equipment.attackHit_14;
        characterStats.equipmentMagicHit_92 += equipment.magicHit_15;
        characterStats.equipmentAttackAvoid_94 += equipment.attackAvoid_16;
        characterStats.equipmentMagicAvoid_96 += equipment.magicAvoid_17;
        characterStats.equipmentOnHitStatusChance_98 += equipment.onHitStatusChance_18;
        characterStats.equipment_19_99 += equipment._19;
        characterStats.equipment_1a_9a += equipment._1a;
        characterStats.equipmentOnHitStatus_9b |= equipment.onHitStatus_1b;

        characterStats.equipmentMpPerMagicalHit_54 += equipment.mpPerMagicalHit;
        characterStats.equipmentSpPerMagicalHit_52 += equipment.spPerMagicalHit;
        characterStats.equipmentMpPerPhysicalHit_50 += equipment.mpPerPhysicalHit;
        characterStats.equipmentSpPerPhysicalHit_4e += equipment.spPerPhysicalHit;
        characterStats.equipmentHpMulti_62 += equipment.hpMultiplier;
        characterStats.equipmentMpMulti_64 += equipment.mpMultiplier;
        characterStats.equipmentSpMultiplier_4c += equipment.spMultiplier;

        if(equipment.magicalResistance) {
          characterStats.equipmentMagicalResistance_60 = true;
        }

        if(equipment.physicalResistance) {
          characterStats.equipmentPhysicalResistance_4a = true;
        }

        if(equipment.magicalImmunity) {
          characterStats.equipmentMagicalImmunity_48 = true;
        }

        if(equipment.physicalImmunity) {
          characterStats.equipmentPhysicalImmunity_46 = true;
        }

        characterStats.equipmentRevive_5e += equipment.revive;
        characterStats.equipmentHpRegen_58 += equipment.hpRegen;
        characterStats.equipmentMpRegen_5a += equipment.mpRegen;
        characterStats.equipmentSpRegen_5c += equipment.spRegen;
        characterStats.equipmentSpecial2Flag80_56 += equipment.special2Flag80;
      }
    }
  }
}
