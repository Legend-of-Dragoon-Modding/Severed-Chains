package legend.game;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.core.font.Font;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.characters.CharacterData2c;
import legend.game.characters.UnaryStat;
import legend.game.characters.VitalsStat;
import legend.game.combat.types.EnemyDrop;
import legend.game.i18n.I18n;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.inventory.Inventory;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.ItemGroupSortMode;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.inventory.OverflowMode;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.MenuStack;
import legend.game.inventory.screens.TextColour;
import legend.game.inventory.screens.controls.Highlight;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.inventory.GatherAttackItemsEvent;
import legend.game.modding.events.inventory.GatherRecoveryItemsEvent;
import legend.game.modding.events.inventory.GiveEquipmentEvent;
import legend.game.modding.events.inventory.TakeEquipmentEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.types.EquipmentSlot;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MenuGlyph06;
import legend.game.types.MenuStatus08;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;
import legend.game.types.MessageBoxType;
import legend.game.types.Renderable58;
import legend.game.types.RenderableMetrics14;
import legend.game.types.UiFile;
import legend.game.types.UiPart;
import legend.game.types.UiType;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.DEFAULT_FONT;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.getTextureAtlas;
import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.DrgnFiles.loadDrgnFileSync;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.Menus.allocateManualRenderable;
import static legend.game.Menus.allocateRenderable;
import static legend.game.Menus.leftArrowRenderable_800bdba4;
import static legend.game.Menus.loadMenuTexture;
import static legend.game.Menus.rightArrowRenderable_800bdba8;
import static legend.game.Menus.uiFile_800bdc3c;
import static legend.game.Menus.unloadRenderable;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIds_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.characters.CharacterData2c.IN_PARTY;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.ITEM_GROUP_SORT_MODE;
import static legend.game.sound.Audio.copyPlayingSounds;
import static legend.game.sound.Audio.playMenuSound;
import static legend.game.sound.Audio.playMusicPackage;
import static legend.game.sound.Audio.playingSoundsBackup_800bca78;
import static legend.game.sound.Audio.queuedSounds_800bd110;
import static legend.game.sound.Audio.stopAndResetSoundsAndSequences;
import static legend.game.sound.Audio.stopMusicSequence;
import static legend.game.sound.Audio.unloadSoundFile;
import static legend.game.types.Renderable58.FLAG_DELETE_AFTER_RENDER;
import static legend.lodmod.LodMod.ATTACK_AVOID_STAT;
import static legend.lodmod.LodMod.ATTACK_HIT_STAT;
import static legend.lodmod.LodMod.ATTACK_STAT;
import static legend.lodmod.LodMod.DEFENSE_STAT;
import static legend.lodmod.LodMod.DRAGOON_ATTACK_STAT;
import static legend.lodmod.LodMod.DRAGOON_DEFENSE_STAT;
import static legend.lodmod.LodMod.DRAGOON_MAGIC_ATTACK_STAT;
import static legend.lodmod.LodMod.DRAGOON_MAGIC_DEFENSE_STAT;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MAGIC_ATTACK_STAT;
import static legend.lodmod.LodMod.MAGIC_AVOID_STAT;
import static legend.lodmod.LodMod.MAGIC_DEFENSE_STAT;
import static legend.lodmod.LodMod.MAGIC_HIT_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SPEED_STAT;
import static legend.lodmod.LodMod.SP_STAT;

public final class SItem {
  private SItem() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SItem.class);

  public static final MenuStack menuStack = new MenuStack();
  private static BackgroundMusic menuMusic;

  public static final FontOptions UI_TEXT = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN);
  public static final FontOptions UI_TEXT_DISABLED = new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN);
  public static final FontOptions UI_TEXT_SELECTED = new FontOptions().colour(TextColour.RED).shadowColour(TextColour.MIDDLE_BROWN);
  public static final FontOptions UI_TEXT_CENTERED = new FontOptions().colour(TextColour.BROWN).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_TEXT_DISABLED_CENTERED = new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_TEXT_SELECTED_CENTERED = new FontOptions().colour(TextColour.RED).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_WHITE = new FontOptions().colour(TextColour.WHITE);
  public static final FontOptions UI_WHITE_CENTERED = new FontOptions().colour(TextColour.WHITE).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_WHITE_SMALL = new FontOptions().colour(TextColour.WHITE).size(0.67f);
  public static final FontOptions UI_WHITE_SHADOWED = new FontOptions().colour(TextColour.WHITE).shadowColour(TextColour.BLACK).horizontalAlign(HorizontalAlign.CENTRE);
  public static final FontOptions UI_WHITE_SHADOWED_RIGHT = new FontOptions().colour(TextColour.WHITE).shadowColour(TextColour.BLACK).horizontalAlign(HorizontalAlign.RIGHT);

  public static RegistryId shopId_8007a3b4;

  public static final MenuStatus08[] menuStatus_800fba7c = {
    new MenuStatus08("Petrify", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Charmed", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Confused", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Fear", new FontOptions().colour(TextColour.PURPLE).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Stunned", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("", new FontOptions().colour(TextColour.MIDDLE_BROWN).shadowColour(TextColour.LIGHT_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Dspirit", new FontOptions().colour(TextColour.CYAN).shadowColour(TextColour.MIDDLE_BROWN).horizontalAlign(HorizontalAlign.CENTRE)),
    new MenuStatus08("Poison", new FontOptions().colour(TextColour.LIME).shadowColour(TextColour.GREEN).horizontalAlign(HorizontalAlign.CENTRE)),
  };

  public static final MenuGlyph06[] charSwapGlyphs_80114160 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(75, 16, 83),
    new MenuGlyph06(75, 16, 155),
  };
  public static final MenuGlyph06[] equipmentGlyphs_80114180 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(75, 16, 88),
    new MenuGlyph06(75, 194, 92),
    new MenuGlyph06(75, 194, 173),
  };
  public static final MenuGlyph06[] characterStatusGlyphs_801141a4 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(75, 16, 88),
    new MenuGlyph06(75, 194, 94),
  };
  public static final MenuGlyph06[] additionGlyphs_801141e4 = {
    new MenuGlyph06(75, 16, 88),
    new MenuGlyph06(92, 16, 94),
    new MenuGlyph06(93, 184, 94),
  };
  public static final MenuGlyph06[] useItemGlyphs_801141fc = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(81, 16, 108),
    new MenuGlyph06(82, 192, 108),
    new MenuGlyph06(157, 16, 108),
    new MenuGlyph06(158, 192, 108),
  };
  public static final MenuGlyph06[] dabasMenuGlyphs_80114228 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(160, 16, 16),
    new MenuGlyph06(161, 192, 16),
  };

  public static final MenuGlyph06 glyph_801142d4 = new MenuGlyph06(0, 202, 24);

  public static final String[] chapterNames_80114248 = {
    "Ch.1 Serdian War",
    "Ch.2 Platinum Shadow",
    "Ch.3 Fate & Soul",
    "Ch.4 Moon & Fate",
  };

  public static final MenuGlyph06[] glyphs_80114548 = {
    new MenuGlyph06(69, 0, 0),
    new MenuGlyph06(70, 192, 0),
    new MenuGlyph06(101, 16, 16),
    new MenuGlyph06(152, 16, 134),
    new MenuGlyph06(85, 194, 16),
    new MenuGlyph06(91, 194, 164),
  };

  public static final String[] submapNames_8011c108 = {
    "", "Forest", "Forest", "Seles", "Hellena Prison",
    "Prairie", "Cave", "", "Bale", "Indels Castle",
    "Town of Hoax", "Marshland", "Vol. Villude", "Nest of Dragon", "Lohan",
    "Shirley's Shrine", "", "Kazas", "Black Castle", "Fletz",
    "Twin Castle", "Barrens", "Donau", "Valley", "Giganto Home",
    "", "The Queen Fury", "Phantom Ship", "Lidiera", "Undersea Cavern",
    "Fueno", "Prison Island", "Furni", "Evergreen Frst", "Deningrad",
    "Crystal Palace", "Neet", "Wingly Forest", "Forbidden Land", "",
    "Mortal Dr Mt.", "", "Kashua Glacier", "Flanvel Tower", "Snowfield",
    "Fort Magrad", "Vellweb", "", "Death Frontier", "Ulara",
    "Zenebatos", "Mayfil", "", "Rouge", "Aglis",
    "Divine Tree", "Moon",
  };
  public static final String[] worldMapNames_8011c1ec = {
    "So. of Serdio",
    "No. of Serdio",
    "Tiberoa",
    "Illisa Bay",
    "Mille Seseau",
    "Gloriano",
    "Death Frontier",
    "Endiness",
  };

  @ScriptDescription("Gets the maximum number of items a player can carry")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "size")
  public static FlowControl scriptGetMaxItemCount(final RunningScript<?> script) {
    script.params_20[0].set(CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the maximum number of equipment a player can carry")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "size")
  public static FlowControl scriptGetMaxEquipmentCount(final RunningScript<?> script) {
    script.params_20[0].set(255);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks whether or not an item slot is used")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "used")
  public static FlowControl scriptIsItemSlotUsed(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    script.params_20[1].set(slot < gameState_800babc8.items_2e9.getSize() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks whether or not an equipment slot is used")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "used")
  public static FlowControl scriptIsEquipmentSlotUsed(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    script.params_20[1].set(slot < gameState_800babc8.equipment_1e8.size() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get the registry ID of an item slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGetItemSlot(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    script.params_20[1].set(gameState_800babc8.items_2e9.get(slot).getItem().getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get the registry ID of an equipment slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGetEquipmentSlot(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    script.params_20[1].set(gameState_800babc8.equipment_1e8.get(slot).getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set the registry ID of an item slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptSetItemSlot(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    final RegistryId id = script.params_20[1].getRegistryId();
    gameState_800babc8.items_2e9.set(slot, REGISTRIES.items.getEntry(id).get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set the registry ID of an equipment slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptSetEquipmentSlot(final RunningScript<?> script) {
    final int slot = script.params_20[0].get();
    final RegistryId id = script.params_20[1].getRegistryId();
    gameState_800babc8.equipment_1e8.set(slot, REGISTRIES.equipment.getEntry(id).get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives an item to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "given", description = "True if given successfully, false otherwise (e.g. no space)")
  public static FlowControl scriptGiveItem(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final ItemStack remaining = gameState_800babc8.items_2e9.give(REGISTRIES.items.getEntry(id).get());
    script.params_20[1].set(remaining.isEmpty() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives equipment to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "given", description = "True if given successfully, false otherwise (e.g. no space)")
  public static FlowControl scriptGiveEquipment(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final boolean given = giveEquipment(REGISTRIES.equipment.getEntry(id).get());
    script.params_20[1].set(given ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes an item from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "taken", description = "True if given successfully, false otherwise (e.g. no space)")
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final ItemStack remaining = gameState_800babc8.items_2e9.take(REGISTRIES.items.getEntry(id).get());
    script.params_20[1].set(remaining.isEmpty() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes equipment from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "taken", description = "True if given successfully, false otherwise (e.g. no space)")
  public static FlowControl scriptTakeEquipment(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final boolean taken = takeEquipmentId(REGISTRIES.equipment.getEntry(id).get());
    script.params_20[1].set(taken ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Picks a random attack item")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGenerateAttackItem(final RunningScript<?> script) {
    final ItemStack[] items = EVENTS.postEvent(new GatherAttackItemsEvent()).getStacks();
    final ItemStack selected = items[seed_800fa754.nextInt(items.length)];
    script.params_20[0].set(selected.getItem().getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Picks a random recovery item")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGenerateRecoveryItem(final RunningScript<?> script) {
    final ItemStack[] items = EVENTS.postEvent(new GatherRecoveryItemsEvent()).getStacks();
    final ItemStack selected = items[seed_800fa754.nextInt(items.length)];
    script.params_20[0].set(selected.getItem().getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Check if the player has the specified good")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "has")
  public static FlowControl scriptHasGood(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    script.params_20[1].set(gameState_800babc8.goods_19c.has(REGISTRIES.goods.getEntry(id)));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives a good to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptGiveGood(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    gameState_800babc8.goods_19c.give(REGISTRIES.goods.getEntry(id));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes a good from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id")
  public static FlowControl scriptTakeGood(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    gameState_800babc8.goods_19c.take(REGISTRIES.goods.getEntry(id));
    return FlowControl.CONTINUE;
  }

  /**
   * @param amount Amount of HP to restore, -1 restores all hP
   * @return The amount of HP restored, -1 if all HP is restored, or -2 if HP was already full
   */
  @Method(0x80022b50L)
  public static int addHp(final int charIndex, final int amount) {
    final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);
    final VitalsStat hp = character.stats.getStat(HP_STAT.get());

    if(hp.isFull()) {
      return -2;
    }

    //LAB_80022bb4
    final int ret;
    if(amount == -1) {
      hp.restore();
      ret = -1;
    } else {
      //LAB_80022bc8
      hp.restore(amount);

      if(!hp.isFull()) {
        ret = amount;
      } else {
        hp.restore();
        ret = -1;
      }
    }

    //LAB_80022bec
    //LAB_80022bf8
    return ret;
  }

  /**
   * @param amount Amount of MP to restore, -1 restores all MP
   * @return The amount of MP restored, -1 if all MP is restored, or -2 if MP was already full
   */
  @Method(0x80022c08L)
  public static int addMp(final int charIndex, final int amount) {
    final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);
    final VitalsStat mp = character.stats.getStat(MP_STAT.get());

    if(mp.getMaxRaw() == 0 || mp.isFull()) {
      return -2;
    }

    //LAB_80022c78
    final int ret;
    if(amount == -1) {
      mp.restore();
      ret = -1;
    } else {
      //LAB_80022c8c
      mp.restore(amount);

      if(!mp.isFull()) {
        ret = amount;
      } else {
        mp.restore();
        ret = -1;
      }
    }

    //LAB_80022cb4
    //LAB_80022cc0
    return ret;
  }

  @Method(0x80022cd0L)
  public static int addSp(final int charIndex, final int amount) {
    final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);
    final VitalsStat sp = character.stats.getStat(SP_STAT.get());

    if(sp.isFull()) {
      return -2;
    }

    final int spToAdd = amount == -1 ? sp.getMax() - sp.getCurrent() : Math.min(amount, sp.getMax() - sp.getCurrent());
    final int responseType = amount == -1 || spToAdd < amount ? -1 : amount;

    sp.restore(spToAdd);
    character.dlevelXp_0e += amount == -1 ? spToAdd : amount;

    if(character.dlevelXp_0e >= character.getDxpToNextLevel() && character.dlevel_13 < 5) {
      character.applyDragoonLevelUp(null);
    }

    return responseType;
  }

  @Method(0x800232dcL)
  public static boolean takeItemFromSlot(final int itemSlot) {
    return takeItemFromSlot(itemSlot, gameState_800babc8.items_2e9.get(itemSlot).getSize());
  }

  @Method(0x800232dcL)
  public static boolean takeItemFromSlot(final int itemSlot, final int amount) {
    if(itemSlot >= gameState_800babc8.items_2e9.getSize()) {
      LOGGER.warn("Tried to take item index %d (out of bounds)", itemSlot);
      return false;
    }

    gameState_800babc8.items_2e9.takeFromSlot(itemSlot, amount);
    return true;
  }

  public static boolean takeEquipmentId(final Equipment equipment) {
    final int equipmentSlot = gameState_800babc8.equipment_1e8.indexOf(equipment);

    if(equipmentSlot != -1) {
      return takeEquipment(equipmentSlot);
    }

    return false;
  }

  @Method(0x800233d8L)
  public static boolean takeEquipment(final int equipmentIndex) {
    if(equipmentIndex >= gameState_800babc8.equipment_1e8.size()) {
      LOGGER.warn("Tried to take equipment index %d (out of bounds)", equipmentIndex);
      return false;
    }

    final Equipment equipment = gameState_800babc8.equipment_1e8.get(equipmentIndex);
    final TakeEquipmentEvent event = EVENTS.postEvent(new TakeEquipmentEvent(equipment, equipmentIndex));

    if(event.isCanceled()) {
      return false;
    }

    gameState_800babc8.equipment_1e8.remove(equipmentIndex);
    return true;
  }

  @Method(0x80023484L)
  public static boolean giveEquipment(final Equipment equipment) {
    final GiveEquipmentEvent event = EVENTS.postEvent(new GiveEquipmentEvent(equipment, Collections.unmodifiableList(gameState_800babc8.equipment_1e8), 255));

    if(event.isCanceled() || event.givenEquipment.isEmpty()) {
      return false;
    }

    final boolean overflowed = event.currentEquipment.size() + event.givenEquipment.size() > event.maxInventorySize;

    if(event.overflowMode == OverflowMode.FAIL && overflowed) {
      return false;
    }

    if(event.overflowMode == OverflowMode.TRUNCATE && overflowed) {
      for(int i = 0; i < event.givenEquipment.size() && event.currentEquipment.size() <= event.maxInventorySize; i++) {
        gameState_800babc8.equipment_1e8.add(event.givenEquipment.get(i));
      }

      return true;
    }

    gameState_800babc8.equipment_1e8.addAll(event.givenEquipment);
    return true;
  }

  /**
   * @param items the items to give
   * @return the number of items that could not be given
   */
  @Method(0x80023544L)
  public static int giveItems(final List<EnemyDrop> items) {
    int count = 0;

    //LAB_80023580
    final Iterator<EnemyDrop> it = items.iterator();
    while(it.hasNext()) {
      final EnemyDrop drop = it.next();

      if(!drop.performDrop()) {
        //LAB_800235a4
        //LAB_800235c0
        drop.overflow();
        it.remove();
        count++;
      }

      //LAB_80023604
    }

    //LAB_80023618
    return count;
  }

  @Method(0x8002363cL)
  public static int addGold(final int amount) {
    gameState_800babc8.gold_94 += amount;

    if(gameState_800babc8.gold_94 > 99999999) {
      gameState_800babc8.gold_94 = 99999999;
    }

    //LAB_8002366c
    return 0;
  }

  /**
   * @param part 0: second, 1: minute, 2: hour
   */
  @Method(0x80023674L)
  public static int getTimestampPart(int timestamp, final long part) {
    if(timestamp >= 216000000) { // Clamp to 1000 hours
      timestamp = 215999999;
    }

    // Hours
    if(part == 0) {
      return timestamp / 216000 % 1000;
    }

    // Minutes
    if(part == 1) {
      return timestamp / 3600 % 60;
    }

    // Seconds
    if(part == 2) {
      return timestamp / 60 % 60;
    }

    return 0;
  }

  @Method(0x800239e0L)
  public static <T extends InventoryEntry<?>> void setInventoryFromDisplay(final List<MenuEntryStruct04<T>> display, final List<T> out, final int count) {
    out.clear();

    //LAB_800239ec
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        out.add(display.get(i).item_00);
      }
    }
  }

  @Method(0x800239e0L)
  public static void setInventoryFromDisplay(final List<MenuEntryStruct04<ItemStack>> display, final Inventory out, final int count) {
    out.clear();

    //LAB_800239ec
    for(int i = 0; i < count; i++) {
      if((display.get(i).flags_02 & 0x1000) == 0) {
        out.give(display.get(i).item_00, true);
      }
    }
  }

  @Method(0x80023a2cL)
  public static <T extends InventoryEntry<?>> void sortItems(final List<MenuEntryStruct04<T>> display, final List<T> items, final int count, final List<String> retailSorting) {
    display.sort(menuItemIconComparator(retailSorting, InventoryEntry::getRegistryId));
    setInventoryFromDisplay(display, items, count);
  }

  @Method(0x80023a2cL)
  public static void sortItems(final List<MenuEntryStruct04<ItemStack>> display, final Inventory items, final int count, final List<String> retailSorting) {
    display.sort(menuItemIconComparator(retailSorting, stack -> stack.getItem().getRegistryId()));
    setInventoryFromDisplay(display, items, count);
  }

  public static <T extends InventoryEntry<?>> Comparator<MenuEntryStruct04<T>> menuItemIconComparator(final List<String> retailSorting, final Function<T, RegistryId> idExtractor) {
    final boolean retail = CONFIG.getConfig(ITEM_GROUP_SORT_MODE.get()) == ItemGroupSortMode.RETAIL;

    Comparator<MenuEntryStruct04<T>> comparator = Comparator.comparingInt(item -> item.item_00.getIcon().resolve().icon);

    if(retail) {
      comparator = comparator.thenComparingInt(item -> {
        final RegistryId id = idExtractor.apply(item.item_00);

        if(!LodMod.MOD_ID.equals(id.modId()) || !retailSorting.contains(id.entryId())) {
          return Integer.MAX_VALUE;
        }

        return retailSorting.indexOf(id.entryId());
      });

      comparator = comparator.thenComparing(item -> {
        final RegistryId id = idExtractor.apply(item.item_00);

        if(LodMod.MOD_ID.equals(id.modId()) && retailSorting.contains(id.entryId())) {
          return "";
        }

        return I18n.translate(item.getNameTranslationKey());
      });
    } else {
      comparator = comparator.thenComparing(item -> I18n.translate(item.getNameTranslationKey()));
    }

    return comparator;
  }

  public static Comparator<MenuEntryStruct04<Equipment>> menuEquipmentSlotComparator() {
    return Comparator
      .comparingInt((MenuEntryStruct04<Equipment> equipment) -> equipment.item_00.slot.ordinal())
      .thenComparing(equipment -> I18n.translate(equipment.getNameTranslationKey()));
  }

  @Method(0x80023a88L)
  public static void sortItems() {
    final List<MenuEntryStruct04<ItemStack>> items = new ArrayList<>();

    for(final ItemStack stack : gameState_800babc8.items_2e9) {
      items.add(new MenuEntryStruct04<>(stack));
    }

    sortItems(items, gameState_800babc8.items_2e9, gameState_800babc8.items_2e9.getSize(), List.of(LodMod.ITEM_IDS));
  }

  @Method(0x800fc814L)
  public static int FUN_800fc814(final int a0) {
    return 9 + a0 * 17;
  }

  public static void loadMenuAssets() {
    loadDrgnFileSync(0, 6665, data -> menuAssetsLoaded(data, 0));
    loadDrgnFileSync(0, 6666, data -> menuAssetsLoaded(data, 1));
    loadDrgnDir(0, 5815, SItem::menuMusicLoaded);
  }

  @Method(0x800fc944L)
  private static void menuAssetsLoaded(final FileData data, final int whichFile) {
    if(whichFile == 0) {
      //LAB_800fc98c
      loadMenuTexture(data.slice(0x83e0)); // Character textures
      loadMenuTexture(data); // Menu textures
      loadMenuTexture(data.slice(0x6200)); // Item textures
      loadMenuTexture(data.slice(0x1_0460));
      loadMenuTexture(data.slice(0x1_0580));
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

  private static void menuMusicLoaded(final List<FileData> files) {
    menuMusic = new BackgroundMusic(files, 5815);
  }

  /** FUN_8001e010 with param 0 */
  @Method(0x8001e010L)
  public static void startMenuMusic() {
    //LAB_8001e054
    copyPlayingSounds(queuedSounds_800bd110, playingSoundsBackup_800bca78);
    stopAndResetSoundsAndSequences();
    unloadSoundFile(8);
    unloadSoundFile(8);

    menuMusic.reset();
    playMusicPackage(menuMusic, true);
  }

  /** FUN_8001e010 with param -1 */
  @Method(0x8001e010L)
  public static void stopMenuMusic() {
    //LAB_8001e044
    //LAB_8001e0f8
    if(!loadingNewGameState_800bdc34) {
      //LAB_8001e160
      stopMusicSequence();
      unloadSoundFile(8);

      currentEngineState_8004dd04.restoreMusicAfterMenu();
    }

    //LAB_8001e26c
    stopAndResetSoundsAndSequences();
    copyPlayingSounds(playingSoundsBackup_800bca78, queuedSounds_800bd110);
    playingSoundsBackup_800bca78.clear();
  }

  @Method(0x801033ccL)
  public static void initArrowRenderable(final Renderable58 a0) {
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
    initArrowRenderable(newRenderable);
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
  public static void addLeftRightArrows(final int charSlot, final int charCount) {
    setRandomRepeatGlyph(leftArrowRenderable_800bdba4, 0x2d, 0x34, 0xaa, 0xb1);
    setRandomRepeatGlyph(rightArrowRenderable_800bdba8, 0x25, 0x2c, 0xa2, 0xa9);

    if(charSlot != 0) {
      if(leftArrowRenderable_800bdba4 == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 18, 16);
        renderable.repeatStartGlyph_18 = 0x2d;
        renderable.repeatEndGlyph_1c = 0x34;
        leftArrowRenderable_800bdba4 = renderable;
        initArrowRenderable(renderable);
      }
    } else {
      //LAB_80103578
      if(leftArrowRenderable_800bdba4 != null) {
        fadeOutArrow(leftArrowRenderable_800bdba4);
        leftArrowRenderable_800bdba4 = null;
      }
    }

    //LAB_80103598
    if(charSlot < charCount - 1) {
      if(rightArrowRenderable_800bdba8 == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 350, 16);
        renderable.repeatStartGlyph_18 = 0x25;
        renderable.repeatEndGlyph_1c = 0x2c;
        rightArrowRenderable_800bdba8 = renderable;
        initArrowRenderable(renderable);
      }
      //LAB_801035e8
    } else if(rightArrowRenderable_800bdba8 != null) {
      fadeOutArrow(rightArrowRenderable_800bdba8);
      rightArrowRenderable_800bdba8 = null;
    }

    //LAB_80103604
  }

  @Method(0x8010376cL)
  public static void renderGlyphs(final MenuGlyph06[] glyphs, final int x, final int y) {
    //LAB_801037ac
    for(int i = 0; i < glyphs.length; i++) {
      final Renderable58 s0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);

      initGlyph(s0, glyphs[i]);

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

  public static Renderable58 allocateManualUiElement(final int startGlyph, final int endGlyph, final int x, final int y) {
    final Renderable58 renderable = allocateManualRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    return allocateUiElement(renderable, startGlyph, endGlyph, x, y);
  }

  private static final MV portraitTransforms = new MV();

  @Method(0x80103910L)
  public static QueuedModelStandard renderCharacterPortrait(final int charId, final float x, final float y, final float z, final float width, final float height) {
    portraitTransforms.transfer.set(x, y, z);
    portraitTransforms.scaling(width, height, 1.0f);
    return getTextureAtlas().getIcon(gameState_800babc8.charData_32c.get(charId).template.getRegistryId()).render(portraitTransforms);
  }

  /**
   * @return Item ID of previously-equipped item, 0xff if invalid, 0x100 if no item was equipped
   */
  @Method(0x80103a5cL)
  public static EquipItemResult equipItem(final Equipment equipment, final int charIndex) {
    final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);

    if(!charData.canEquip(equipment.slot, equipment)) {
      return EquipItemResult.failure();
    }

    //LAB_80103ac0
    final Equipment previousEquipment = charData.getEquipment(equipment.slot);
    charData.equip(equipment.slot, equipment);

    //LAB_80103af4
    //LAB_80103af8
    return EquipItemResult.success(previousEquipment);
  }

  @Method(0x80103b10L)
  public static void cacheCharacterSlots() {
    secondaryCharIds_800bdbf8.clear();
    characterIndices_800bdbb8.clear();

    //LAB_80103b48
    for(int slot = 0; slot < gameState_800babc8.charData_32c.size(); slot++) {
      if((gameState_800babc8.charData_32c.get(slot).partyFlags_04 & IN_PARTY) != 0) {
        characterIndices_800bdbb8.add(slot);

        if(!gameState_800babc8.charIds_88.contains(slot)) {
          secondaryCharIds_800bdbf8.add(slot);
        }
      }

      //LAB_80103bb4
    }
  }

  @Method(0x80103e90L)
  public static float renderMenuCentredText(final Font font, final String text, final float x, final float y, final int maxWidth, final FontOptions options) {
    return renderMenuCentredText(font, text, x, y, maxWidth, options, null);
  }

  @Method(0x80103e90L)
  public static float renderMenuCentredText(final Font font, final String text, final float x, final float y, final int maxWidth, final FontOptions options, @Nullable final Text.QueueCallback queueCallback) {
    final String[] split;
    if(font.textWidth(text) * options.getSize() <= maxWidth) {
      split = new String[] {text};
    } else {
      final List<String> temp = new ArrayList<>();
      float currentWidth = 0.0f;
      int startIndex = 0;
      for(int i = 0; i < text.length(); i++) {
        final char current = text.charAt(i);
        final float charWidth = font.charWidth(current) * options.getSize();

        if(current == '\n') {
          temp.add(text.substring(startIndex, i));
          currentWidth = 0;
          startIndex = i + 1;
        } else if(currentWidth + charWidth > maxWidth) {
          boolean advanceOverSpace = false;
          for(int backtrack = 0; backtrack < 10; backtrack++) {
            if(text.charAt(i - backtrack) == ' ') {
              i -= backtrack;
              advanceOverSpace = true;
              break;
            }
          }

          temp.add(text.substring(startIndex, i));
          currentWidth = charWidth;
          startIndex = i;

          if(advanceOverSpace) {
            startIndex++;
          }
        } else {
          currentWidth += charWidth;
        }
      }

      temp.add(text.substring(startIndex));
      split = temp.toArray(String[]::new);
    }

    float height = 0.0f;

    for(int i = 0; i < split.length; i++) {
      final String str = split[i];
      renderText(str, x - font.textWidth(str) * options.getSize() / 2.0f, y + height, options, queueCallback);
      height += font.textHeight(str) * options.getSize();
    }

    return height;
  }

  @Method(0x801038d4L)
  public static Renderable58 allocateOneFrameGlyph(final int glyph, final int x, final int y) {
    final Renderable58 renderable = allocateUiElement(glyph, glyph, x, y);
    renderable.flags_00 |= FLAG_DELETE_AFTER_RENDER;
    return renderable;
  }

  @Method(0x80104738L)
  public static void loadItemsAndEquipmentForDisplay(@Nullable final MenuEntries<Equipment> equipments, @Nullable final MenuEntries<ItemStack> items, final long a0) {
    if(items != null) {
      items.clear();

      for(int i = 0; i < gameState_800babc8.items_2e9.getSize(); i++) {
        final ItemStack item = gameState_800babc8.items_2e9.get(i);
        final MenuEntryStruct04<ItemStack> menuEntry = new MenuEntryStruct04<>(item);
        items.add(menuEntry);
      }
    }

    if(equipments != null) {
      equipments.clear();

      int equipmentIndex;
      for(equipmentIndex = 0; equipmentIndex < gameState_800babc8.equipment_1e8.size(); equipmentIndex++) {
        final Equipment equipment = gameState_800babc8.equipment_1e8.get(equipmentIndex);
        final MenuEntryStruct04<Equipment> menuEntry = new MenuEntryStruct04<>(equipment);

        if(a0 != 0 && !gameState_800babc8.equipment_1e8.get(equipmentIndex).canBeDiscarded()) {
          menuEntry.flags_02 = 0x2000;
        }

        equipments.add(menuEntry);
      }

      if(a0 == 0) {
        for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
          for(final EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            final int charId = gameState_800babc8.charIds_88.getInt(i);
            final CharacterData2c character = gameState_800babc8.charData_32c.get(charId);
            final Equipment equipment = character.getEquipment(equipmentSlot);

            if(equipment != null) {
              final MenuEntryStruct04<Equipment> menuEntry = new MenuEntryStruct04<>(equipment);
              menuEntry.flags_02 = 0x3000 | charId;
              equipments.add(menuEntry);

              equipmentIndex++;
            }
          }
        }
      }
    }
  }

  @Method(0x80104b1cL)
  public static void initGlyph(final Renderable58 a0, final MenuGlyph06 glyph) {
    a0.glyph_04 = glyph.glyph_00;
    a0.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    a0.tpage_2c = 0x19;
    a0.clut_30 = 0;
    a0.x_40 = glyph.x_02;
    a0.y_44 = glyph.y_04;
  }

  @Method(0x80104b60L)
  public static void initHighlight(final Renderable58 glyph) {
    glyph.deallocationGroup_28 = 0x1;
    glyph.widthScale = 0;
    glyph.heightScale_38 = 0;
    glyph.z_3c = 35;
  }

  public static int renderNumberComparison(final int x, final int y, final int currentVal, int newVal, final int digitCount) {
    int flags = 0;
    final int clut;
    if(currentVal < newVal) {
      clut = 0x7c6b;
    } else if(currentVal > newVal) {
      clut = 0x7c2b;
    } else {
      clut = 0;
    }

    final int max = (int)Math.pow(10, digitCount) - 1;

    if(newVal > max) {
      newVal = max;
    }

    for(int i = 0; i < digitCount; i++) {
      final int digit = newVal / (int)Math.pow(10, digitCount - (i + 1)) % 10;

      if(digit != 0 || i == digitCount - 1 || (flags & 0x1) != 0) {
        final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
        renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | FLAG_DELETE_AFTER_RENDER;
        renderable.glyph_04 = digit;
        renderable.tpage_2c = 0x19;
        renderable.clut_30 = clut;
        renderable.z_3c = 33;
        renderable.x_40 = x + i * 6;
        renderable.y_44 = y;
        flags |= 0x1;
      }
    }

    return clut;
  }

  public static void renderFraction(final int x, final int y, final int numerator, final int denominator) {
    final int width = renderRightAlignedNumber(x, y, denominator, 0);
    allocateUiElement(0xb, 0xb, x - width - 5, y).flags_00 |= FLAG_DELETE_AFTER_RENDER;
    renderRightAlignedNumber(x - width - 5, y, numerator, 0);
  }

  public static void renderHp(final int x, final int y, final int numerator, final int denominator) {
    final int clut;
    if(numerator <= denominator / 4) {
      clut = 0x7c2b;
      //LAB_80105090
    } else if(numerator <= denominator / 2) {
      clut = 0x7cab;
    } else {
      clut = 0;
    }

    final int width = renderRightAlignedNumber(x, y, denominator);
    allocateUiElement(0xb, 0xb, x - width - 5, y).flags_00 |= FLAG_DELETE_AFTER_RENDER;
    renderRightAlignedNumber(x - width - 5, y, numerator, clut);
  }

  @Method(0x8010568cL)
  public static void renderFourDigitHp(final int x, final int y, int value, final int max, final int renderableFlags) {
    int clut = 0;
    int flags = 0;

    if(value >= 9999) {
      value = 9999;
    }

    //LAB_801056d0
    if(max > 9999) {
      value = 9999;
    }

    //LAB_801056e0
    if(value <= max / 2) {
      clut = 0x7cab;
    }

    //LAB_801056f0
    if(value <= max / 4) {
      clut = 0x7c2b;
    }

    //LAB_80105714
    int s0 = value / 1_000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.glyph_04 = s0;
      //LAB_80105784
      //LAB_80105788
      renderable.flags_00 |= renderableFlags | Renderable58.FLAG_NO_ANIMATION;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x;
      renderable.y_44 = y;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      flags |= 0x1;
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
      renderable.flags_00 |= renderableFlags | Renderable58.FLAG_NO_ANIMATION;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x + 6;
      renderable.y_44 = y;
      renderable.clut_30 = clut;
      renderable.z_3c = 0x21;
      flags |= 0x1;
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
      renderable.flags_00 |= renderableFlags | Renderable58.FLAG_NO_ANIMATION;
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
    renderable.flags_00 |= renderableFlags | Renderable58.FLAG_NO_ANIMATION;
    renderable.tpage_2c = 0x19;
    renderable.x_40 = x + 18;
    renderable.y_44 = y;
    renderable.clut_30 = clut;
    renderable.z_3c = 0x21;
  }

  public static int renderRightAlignedNumber(final int x, final int y, final int value, final int clut) {
    final int digitCount = MathHelper.digitCount(value);

    int totalWidth = 0;
    for(int i = 0; i < digitCount; i++) {
      final int digit = value / (int)Math.pow(10, i) % 10;

      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION | FLAG_DELETE_AFTER_RENDER;
      struct.glyph_04 = digit;
      struct.tpage_2c = 0x19;
      struct.clut_30 = clut;
      struct.z_3c = 0x21;
      struct.x_40 = x - (i + 1) * 6;
      totalWidth += 6;

      struct.y_44 = y;
    }

    return totalWidth;
  }

  public static int renderRightAlignedNumber(final int x, final int y, final int value) {
    return renderRightAlignedNumber(x, y, value, 0);
  }

  /**
   * @param flags Bitset - 0x1: render leading zeros, 0x2: unload at end of frame
   */
  public static void renderNumber(final int x, final int y, final int value, final int flags, final int digitCount) {
    renderNumber(x, y, value, flags, digitCount, 0);
  }

  /**
   * @param flags Bitset - 0x1: render leading zeros, 0x2: unload at end of frame
   */
  public static void renderNumber(final int x, final int y, int value, int flags, final int digitCount, final int clut) {
    if(value >= Math.pow(10, digitCount)) {
      value = (int)Math.pow(10, digitCount) - 1;
    }

    for(int i = 0; i < digitCount; i++) {
      final int digit = value / (int)Math.pow(10, digitCount - (i + 1)) % 10;

      if(digit != 0 || i == digitCount - 1 || (flags & 0x1) != 0) {
        final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
        struct.flags_00 |= (flags & 0x2) != 0 ? Renderable58.FLAG_NO_ANIMATION | FLAG_DELETE_AFTER_RENDER : Renderable58.FLAG_NO_ANIMATION;
        struct.glyph_04 = digit;
        struct.tpage_2c = 0x19;
        struct.clut_30 = clut;
        struct.z_3c = 33;
        struct.x_40 = x + 6 * i;
        struct.y_44 = y;
        flags |= 0x1;
      }
    }
  }

  @Method(0x80107cb4L)
  public static Renderable58 renderCharacter(final int x, final int y, final int character) {
    final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION;
    v0.glyph_04 = character;
    v0.tpage_2c = 0x19;
    v0.clut_30 = 0x7ca9;
    v0.z_3c = 0x21;
    v0.x_40 = x;
    v0.y_44 = y;
    return v0;
  }

  @Method(0x80107d34L)
  public static void renderNumberComparisonWithPercent(final int x, final int y, final int currentVal, final int newVal, final int digitCount) {
    final int clut = renderNumberComparison(x, y, currentVal, newVal, digitCount);
    final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION | FLAG_DELETE_AFTER_RENDER;
    v0.glyph_04 = 0xc;
    v0.tpage_2c = 0x19;
    v0.clut_30 = clut;
    v0.z_3c = 0x21;
    v0.x_40 = x + digitCount * 6;
    v0.y_44 = y;
  }

  @Method(0x80107e70L)
  public static boolean renderCharacterStatusEffect(final int x, final int y, final CharacterData2c character) {
    //LAB_80107e90
    final int status = character.status_10;

    if(tickCount_800bb0fc / currentEngineState_8004dd04.tickMultiplier() % 32 < 16) {
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
    renderText(menuStatus.text_00, x + 24, y, menuStatus.colour_04);

    //LAB_80107f8c
    return true;
  }

  @Method(0x801085e0L)
  public static void renderCharacterStats(final int charIndex, @Nullable final Equipment equipment) {
    if(charIndex != -1) {
      final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);

      int equipmentAttack = 0;
      int equipmentDefense = 0;
      int equipmentMagicAttack = 0;
      int equipmentMagicDefense = 0;
      int equipmentAttackHit = 0;
      int equipmentMagicHit = 0;
      int equipmentAttackAvoid = 0;
      int equipmentMagicAvoid = 0;
      int equipmentSpeed = 0;

      for(final EquipmentSlot slot : EquipmentSlot.values()) {
        final Equipment equipped = character.getEquipment(slot);

        if(equipped != null) {
          equipmentAttack += equipped.attack1_0a + equipped.attack2_10;
          equipmentDefense += equipped.defence_12;
          equipmentMagicAttack += equipped.magicAttack_11;
          equipmentMagicDefense += equipped.magicDefence_13;
          equipmentAttackHit += equipped.attackHit_14;
          equipmentMagicHit += equipped.magicHit_15;
          equipmentAttackAvoid += equipped.attackAvoid_16;
          equipmentMagicAvoid += equipped.magicAvoid_17;
          equipmentSpeed += equipped.speed_0f;
        }
      }

      int newEquipmentAttack = equipmentAttack;
      int newEquipmentDefense = equipmentDefense;
      int newEquipmentMagicAttack = equipmentMagicAttack;
      int newEquipmentMagicDefense = equipmentMagicDefense;
      int newEquipmentAttackHit = equipmentAttackHit;
      int newEquipmentMagicHit = equipmentMagicHit;
      int newEquipmentAttackAvoid = equipmentAttackAvoid;
      int newEquipmentMagicAvoid = equipmentMagicAvoid;
      int newEquipmentSpeed = equipmentSpeed;

      if(equipment != null) {
        final Equipment equipped = character.getEquipment(equipment.slot);

        if(equipped != null) {
          newEquipmentAttack -= equipped.attack1_0a + equipped.attack2_10;
          newEquipmentDefense -= equipped.defence_12;
          newEquipmentMagicAttack -= equipped.magicAttack_11;
          newEquipmentMagicDefense -= equipped.magicDefence_13;
          newEquipmentAttackHit -= equipped.attackHit_14;
          newEquipmentMagicHit -= equipped.magicHit_15;
          newEquipmentAttackAvoid -= equipped.attackAvoid_16;
          newEquipmentMagicAvoid -= equipped.magicAvoid_17;
          newEquipmentSpeed -= equipped.speed_0f;
        }

        newEquipmentAttack += equipment.attack1_0a + equipment.attack2_10;
        newEquipmentDefense += equipment.defence_12;
        newEquipmentMagicAttack += equipment.magicAttack_11;
        newEquipmentMagicDefense += equipment.magicDefence_13;
        newEquipmentAttackHit += equipment.attackHit_14;
        newEquipmentMagicHit += equipment.magicHit_15;
        newEquipmentAttackAvoid += equipment.attackAvoid_16;
        newEquipmentMagicAvoid += equipment.magicAvoid_17;
        newEquipmentSpeed += equipment.speed_0f;
      }

      final UnaryStat attack = character.stats.getStat(ATTACK_STAT.get());
      final UnaryStat defense = character.stats.getStat(DEFENSE_STAT.get());
      final UnaryStat magicAttack = character.stats.getStat(MAGIC_ATTACK_STAT.get());
      final UnaryStat magicDefense = character.stats.getStat(MAGIC_DEFENSE_STAT.get());
      final UnaryStat attackHit = character.stats.getStat(ATTACK_HIT_STAT.get());
      final UnaryStat magicHit = character.stats.getStat(MAGIC_HIT_STAT.get());
      final UnaryStat attackAvoid = character.stats.getStat(ATTACK_AVOID_STAT.get());
      final UnaryStat magicAvoid = character.stats.getStat(MAGIC_AVOID_STAT.get());
      final UnaryStat dragoonAttack = character.stats.getStat(DRAGOON_ATTACK_STAT.get());
      final UnaryStat dragoonDefense = character.stats.getStat(DRAGOON_DEFENSE_STAT.get());
      final UnaryStat dragoonMagicAttack = character.stats.getStat(DRAGOON_MAGIC_ATTACK_STAT.get());
      final UnaryStat dragoonMagicDefense = character.stats.getStat(DRAGOON_MAGIC_DEFENSE_STAT.get());
      final UnaryStat speed = character.stats.getStat(SPEED_STAT.get());

      //LAB_80108770
      renderNumber( 53, 116, attack.getRaw(), 0x2, 4);
      renderNumberComparison( 85, 116, equipmentAttack, newEquipmentAttack, 4);
      renderNumberComparison(120, 116, attack.getRaw() + equipmentAttack, attack.getRaw() + newEquipmentAttack, 4);

      if(character.hasDragoon()) {
        renderNumberComparisonWithPercent(153, 116, dragoonAttack.getRaw(), dragoonAttack.getRaw(), 4);
      }

      //LAB_801087fc
      renderNumber( 53, 128, defense.getRaw(), 0x2, 4);
      renderNumberComparison( 85, 128, equipmentDefense, newEquipmentDefense, 4);
      renderNumberComparison(120, 128, defense.getRaw() + equipmentDefense, defense.getRaw() + newEquipmentDefense, 4);

      if(character.hasDragoon()) {
        renderNumberComparisonWithPercent(153, 128, dragoonDefense.getRaw(), dragoonDefense.getRaw(), 4);
      }

      //LAB_8010886c
      renderNumber( 53, 140, magicAttack.getRaw(), 0x2, 4);
      renderNumberComparison( 85, 140, equipmentMagicAttack, newEquipmentMagicAttack, 4);
      renderNumberComparison(120, 140, magicAttack.getRaw() + equipmentMagicAttack, magicAttack.getRaw() + newEquipmentMagicAttack, 4);

      if(character.hasDragoon()) {
        renderNumberComparisonWithPercent(153, 140, dragoonMagicAttack.getRaw(), dragoonMagicAttack.getRaw(), 4);
      }

      //LAB_801088dc
      renderNumber( 53, 152, magicDefense.getRaw(), 0x2, 4);
      renderNumberComparison( 85, 152, equipmentMagicDefense, newEquipmentMagicDefense, 4);
      renderNumberComparison(120, 152, magicDefense.getRaw() + equipmentMagicDefense, magicDefense.getRaw() + newEquipmentMagicDefense, 4);

      if(character.hasDragoon()) {
        renderNumberComparisonWithPercent(153, 152, dragoonMagicDefense.getRaw(), dragoonMagicDefense.getRaw(), 4);
      }

      //LAB_8010894c
      renderNumber( 53, 164, speed.getRaw(), 0x2, 4);
      renderNumberComparison( 85, 164, equipmentSpeed, newEquipmentSpeed, 4);
      renderNumberComparison(120, 164, speed.getRaw() + equipmentSpeed, speed.getRaw() + newEquipmentSpeed, 4);

      renderNumberComparisonWithPercent( 53, 176, attackHit.getRaw(), attackHit.getRaw(), 4);
      renderNumberComparisonWithPercent( 85, 176, equipmentAttackHit, newEquipmentAttackHit, 4);
      renderNumberComparisonWithPercent(120, 176, attackHit.getRaw() + equipmentAttackHit, attackHit.getRaw() + newEquipmentAttackHit, 4);
      renderNumberComparisonWithPercent( 53, 188, magicHit.getRaw(), magicHit.getRaw(), 4);
      renderNumberComparisonWithPercent( 85, 188, equipmentMagicHit, newEquipmentMagicHit, 4);
      renderNumberComparisonWithPercent(120, 188, magicHit.getRaw() + equipmentMagicHit, magicHit.getRaw() + newEquipmentMagicHit, 4);
      renderNumberComparisonWithPercent( 53, 200, attackAvoid.getRaw(), attackAvoid.getRaw(), 4);
      renderNumberComparisonWithPercent( 85, 200, equipmentAttackAvoid, newEquipmentAttackAvoid, 4);
      renderNumberComparisonWithPercent(120, 200, attackAvoid.getRaw() + equipmentAttackAvoid, attackAvoid.getRaw() + newEquipmentAttackAvoid, 4);
      renderNumberComparisonWithPercent( 53, 212, magicAvoid.getRaw(), magicAvoid.getRaw(), 4);
      renderNumberComparisonWithPercent( 85, 212, equipmentMagicAvoid, newEquipmentMagicAvoid, 4);
      renderNumberComparisonWithPercent(120, 212, magicAvoid.getRaw() + equipmentMagicAvoid, magicAvoid.getRaw() + newEquipmentMagicAvoid, 4);

      allocateUiElement(0x56, 0x56, 16, 94).flags_00 |= FLAG_DELETE_AFTER_RENDER;
    }

    //LAB_80108a50
  }

  @Method(0x80108e60L)
  public static void renderCharacterEquipment(final int charIndex, final boolean allocate, @Nullable final EquipmentSlot highlightSlot) {
    if(charIndex == -1) {
      return;
    }

    final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);

    if(allocate) {
      allocateUiElement(0x59, 0x59, 194, 16);

      for(final EquipmentSlot slot : EquipmentSlot.values()) {
        final Equipment equipment = charData.getEquipment(slot);

        if(equipment != null) {
          equipment.renderIcon(202, 17 + 14 * slot.ordinal(), 0);
        }
      }
    }

    //LAB_80108f94
    //LAB_80108f98
    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      final Equipment equipment = charData.getEquipment(slot);

      if(equipment != null) {
        renderText(I18n.translate(equipment), 220, 19 + slot.ordinal() * 14, highlightSlot == slot ? UI_TEXT_SELECTED : UI_TEXT);
      }
    }

    //LAB_8010905c
  }

  @Method(0x80109074L)
  public static void renderString(final int x, final int y, final String string, final boolean allocate) {
    if(allocate) {
      allocateUiElement(0x5b, 0x5b, x, y);
    }

//    for(int i = 0, pos = 0; i < 4 && pos < string.length(); i++) {
//      int nextNewLine = string.indexOf('\n', pos + 1);
//      if(nextNewLine == -1) {
//        nextNewLine = string.length();
//      }

      renderText(string, x + 2, y + 4, UI_TEXT);
//      pos = nextNewLine;
//    }
  }

  @Method(0x80109410L)
  public static void renderMenuItems(final int x, final int y, final MenuEntries<?> menuItems, final int slotScroll, final int itemCount, @Nullable final Renderable58 upArrow, @Nullable final Renderable58 downArrow) {
    int s3 = slotScroll;

    //LAB_8010947c
    int i;
    for(i = 0; i < itemCount && s3 < menuItems.size(); i++) {
      final MenuEntryStruct04<?> menuItem = menuItems.get(s3);

      //LAB_801094ac
      renderText(I18n.translate(menuItem.getNameTranslationKey()), x + 21, y + FUN_800fc814(i) + 1, (menuItem.flags_02 & 0x6000) == 0 ? UI_TEXT : UI_TEXT_DISABLED);
      menuItem.item_00.renderIcon(x + 4, y + FUN_800fc814(i), 0x8);

      if(menuItem.getMaxSize() > 1) {
        renderNumber(x + 96, y + FUN_800fc814(i) + 3, menuItem.getSize(), 0x2, 10);
      }

      final int s0 = menuItem.flags_02;
      if((s0 & 0x1000) != 0) {
        renderCharacterPortrait(s0 & 0xf, x + 140, y + FUN_800fc814(i) - 1, 144.0f, 16.0f, 16.0f);
        //LAB_80109574
      } else if((s0 & 0x2000) != 0) {
        ItemIcon.WARNING.render(x + 148, y + FUN_800fc814(i) - 1, 0x8).clut_30 = 0x7eaa;
      }

      //LAB_801095a4
      s3++;
    }

    //LAB_801095c0
    //LAB_801095d4
    //LAB_801095e0
    if(upArrow != null) { // There was an NPE here when fading out item list
      if(slotScroll != 0) {
        upArrow.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
      } else {
        upArrow.flags_00 |= Renderable58.FLAG_INVISIBLE;
      }
    }

    //LAB_80109614
    //LAB_80109628
    if(downArrow != null) { // There was an NPE here when fading out item list
      if(i + slotScroll < menuItems.size()) {
        downArrow.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
      } else {
        downArrow.flags_00 |= Renderable58.FLAG_INVISIBLE;
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
        final int leftPadding = 60;
        final int topPadding = 14;
        final int x = messageBox.x_1c + leftPadding;
        int y = messageBox.y_1e + topPadding;

        messageBox.ticks_10++;

        if(messageBox.text_00 != null) {
          final int textHeight = 12;
          final int textOffset = messageBox.text_00.length * textHeight / 2;
          y -= textOffset;

          for(final String line : messageBox.text_00) {
            renderText(line, x, y, UI_TEXT_CENTERED);
            y += textHeight;
          }

          if (messageBox.type_15 == MessageBoxType.CONFIRMATION) {
            y -= (messageBox.text_00.length - 1) * 3;
          }
        }

        //LAB_8010eeac
        textZ_800bdf00 = 33;

        if(messageBox.type_15 == MessageBoxType.ALERT) {
          //LAB_8010eed8
          if(!messageBox.ignoreInput && PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get()) || PLATFORM.isActionPressed(INPUT_ACTION_MENU_BACK.get())) {
            playMenuSound(2);
            messageBox.state_0c = 4;
            messageBox.result = MessageBoxResult.YES;
          }

          break;
        }

        if(messageBox.type_15 == MessageBoxType.CONFIRMATION) {
          //LAB_8010ef10
          if(messageBox.highlightRenderable_04 == null) {
            messageBox.highlightRenderable_04 = new Highlight();
            final int horizontalSpacing = 12;
            messageBox.highlightRenderable_04.setSize(Math.max(DEFAULT_FONT.textWidth(messageBox.yes), DEFAULT_FONT.textWidth(messageBox.no)) + horizontalSpacing, DEFAULT_FONT.textHeight(messageBox.yes));
            messageBox.highlightRenderable_04.setPos(messageBox.x_1c + 60 - messageBox.highlightRenderable_04.getWidth() / 2, messageBox.menuIndex_18 * messageBox.highlightRenderable_04.getHeight() + y + 5);
            messageBox.highlightRenderable_04.setZ(31);
          }

          messageBox.highlightRenderable_04.render(messageBox.highlightRenderable_04.getX(), messageBox.highlightRenderable_04.getY() + 2);

          //LAB_8010ef64
          textZ_800bdf00 = 30;

          renderText(messageBox.yes, messageBox.x_1c + leftPadding, y + 7, messageBox.menuIndex_18 == 0 ? UI_TEXT_SELECTED_CENTERED : UI_TEXT_CENTERED);
          renderText(messageBox.no, messageBox.x_1c + leftPadding, y + 21, messageBox.menuIndex_18 == 0 ? UI_TEXT_CENTERED : UI_TEXT_SELECTED_CENTERED);

          textZ_800bdf00 = 33;
        }

        break;

      case 4:
        messageBox.state_0c = 5;

        if(messageBox.highlightRenderable_04 != null) {
          messageBox.highlightRenderable_04.delete();
          messageBox.highlightRenderable_04 = null;
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

  public static void setMessageBoxOptions(final MessageBox20 messageBox, final String yes, final String no) {
    messageBox.yes = yes;
    messageBox.no = no;
  }

  @Method(0x8010f130L)
  public static void setMessageBoxText(final MessageBox20 messageBox, @Nullable final String text, final MessageBoxType type) {
    setMessageBoxOptions(messageBox, "Yes", "No");

    if(text != null) {
      messageBox.text_00 = text.split("\n");
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
}
