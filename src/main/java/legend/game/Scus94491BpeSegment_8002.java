package legend.game;

import legend.core.GameEngine;
import legend.core.QueuedModelStandard;
import legend.core.font.Font;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.platform.input.InputCodepoints;
import legend.game.combat.types.EnemyDrop;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.inventory.ItemGroupSortMode;
import legend.game.inventory.ItemStack;
import legend.game.inventory.OverflowMode;
import legend.game.inventory.screens.FontOptions;
import legend.game.modding.events.inventory.GiveEquipmentEvent;
import legend.game.modding.events.inventory.Inventory;
import legend.game.modding.events.inventory.TakeEquipmentEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.NotImplementedException;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptReadable;
import legend.game.submap.SubmapEnvState;
import legend.game.tim.Tim;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.MagicStuff08;
import legend.game.types.MenuEntryStruct04;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.bootMods;
import static legend.game.Graphics.displayWidth_1f8003e0;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.rectArray28_80010770;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.shouldRestoreCameraPosition_80052c40;
import static legend.game.Scus94491BpeSegment_8005.submapCutBeforeBattle_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapEnvState_80052c44;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_800b.characterStatsLoaded_800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800e.main;
import static legend.game.Textboxes.initTextboxes;
import static legend.game.modding.coremod.CoreMod.ITEM_GROUP_SORT_MODE;

public final class Scus94491BpeSegment_8002 {
  private Scus94491BpeSegment_8002() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8002.class);

  @Method(0x80022928L)
  public static int getUnlockedDragoonSpells(final int[] spellIndicesOut, final int charIndex) {
    //LAB_80022940
    for(int spellIndex = 0; spellIndex < 8; spellIndex++) {
      spellIndicesOut[spellIndex] = -1;
    }

    if(charIndex == -1) {
      //LAB_80022a08
      return 0;
    }

    // Hardcoded Divine Dragoon spells
    if(charIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
      spellIndicesOut[0] = 9;
      spellIndicesOut[1] = 4;
      return 2;
    }

    //LAB_80022994
    //LAB_80022998
    //LAB_800229d0
    int spellCount = 0;
    for(int dlevel = 0; dlevel < stats_800be5f8[charIndex].dlevel_0f + 1; dlevel++) {
      final MagicStuff08 spellStuff = magicStuff_80111d20[charIndex][dlevel];
      final int spellIndex = spellStuff.spellIndex_02;

      if(spellIndex != -1) {
        spellIndicesOut[spellCount] = spellIndex;
        spellCount++;
      }

      //LAB_800229e8
    }

    //LAB_80022a00
    return spellCount;
  }

  @Method(0x80022a10L)
  public static int getUnlockedSpellCount(final int charIndex) {
    if(charIndex == -1) {
      return 0;
    }

    //LAB_80022a24
    // Divine dragoon
    if(charIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
      return 2;
    }

    //LAB_80022a4c
    //LAB_80022a50
    //LAB_80022a64
    int unlockedSpells = 0;
    for(int i = 0; i < 6; i++) {
      if(magicStuff_80111d20[charIndex][i].spellIndex_02 != -1) {
        unlockedSpells++;
      }

      //LAB_80022a7c
    }

    return unlockedSpells;
  }

  /**
   * @param amount Amount of HP to restore, -1 restores all hP
   * @return The amount of HP restored, -1 if all HP is restored, or -2 if HP was already full
   */
  @Method(0x80022b50L)
  public static int addHp(final int charIndex, final int amount) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    if(charData.hp_08 == stats.maxHp_66) {
      return -2;
    }

    //LAB_80022bb4
    final int ret;
    if(amount == -1) {
      charData.hp_08 = stats.maxHp_66;
      ret = -1;
    } else {
      //LAB_80022bc8
      charData.hp_08 += amount;

      if(charData.hp_08 < stats.maxHp_66) {
        ret = amount;
      } else {
        charData.hp_08 = stats.maxHp_66;
        ret = -1;
      }
    }

    //LAB_80022bec
    loadCharacterStats();

    //LAB_80022bf8
    return ret;
  }

  /**
   * @param amount Amount of MP to restore, -1 restores all MP
   * @return The amount of MP restored, -1 if all MP is restored, or -2 if MP was already full
   */
  @Method(0x80022c08L)
  public static int addMp(final int charIndex, final int amount) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    if(stats.maxMp_6e == 0 || charData.mp_0a == stats.maxMp_6e) {
      return -2;
    }

    //LAB_80022c78
    final int ret;
    if(amount == -1) {
      charData.mp_0a = stats.maxMp_6e;
      ret = -1;
    } else {
      //LAB_80022c8c
      charData.mp_0a += amount;

      if(charData.mp_0a < stats.maxMp_6e) {
        ret = amount;
      } else {
        charData.mp_0a = stats.maxMp_6e;
        ret = -1;
      }
    }

    //LAB_80022cb4
    loadCharacterStats();

    //LAB_80022cc0
    return ret;
  }

  @Method(0x80022cd0L)
  public static int addSp(final int charIndex, final int amount) {
    assert false;
    return 0;
  }

  public static boolean takeItem(final Item item) {
    return gameState_800babc8.items_2e9.take(item).isEmpty();
  }

  public static boolean takeItem(final ItemStack stack) {
    return gameState_800babc8.items_2e9.take(stack).isEmpty();
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
  public static boolean giveItem(final Item item) {
    return gameState_800babc8.items_2e9.give(item).isEmpty();
  }

  /**
   * Note: does NOT consume the passed in item stack
   */
  @Method(0x80023484L)
  public static boolean giveItem(final ItemStack item) {
    return gameState_800babc8.items_2e9.give(new ItemStack(item)).isEmpty();
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
  public static <T extends InventoryEntry> void setInventoryFromDisplay(final List<MenuEntryStruct04<T>> display, final List<T> out, final int count) {
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
  public static <T extends InventoryEntry> void sortItems(final List<MenuEntryStruct04<T>> display, final List<T> items, final int count, final List<String> retailSorting) {
    display.sort(menuItemIconComparator(retailSorting, InventoryEntry::getRegistryId));
    setInventoryFromDisplay(display, items, count);
  }

  @Method(0x80023a2cL)
  public static void sortItems(final List<MenuEntryStruct04<ItemStack>> display, final Inventory items, final int count, final List<String> retailSorting) {
    display.sort(menuItemIconComparator(retailSorting, stack -> stack.getItem().getRegistryId()));
    setInventoryFromDisplay(display, items, count);
  }

  public static <T extends InventoryEntry> Comparator<MenuEntryStruct04<T>> menuItemIconComparator(final List<String> retailSorting, final Function<T, RegistryId> idExtractor) {
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

  @ScriptDescription("Gives the player gold")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of gold")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out", description = "Always 0")
  @Method(0x80024480L)
  public static FlowControl scriptGiveGold(final RunningScript<?> script) {
    script.params_20[1].set(addGold(script.params_20[0].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives the player chest contents")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID (0xfb = 20G, 0xfc = 50G, 0xfd = 100G, 0xfe = 200G, 0xff = nothing)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemGiven", description = "The item ID if an item was given (0xff = unable to give, 0 = gave gold)")
  @Method(0x800244c4L)
  public static FlowControl scriptGiveChestContents(final RunningScript<?> script) {
    final int a0 = switch(script.params_20[0].get()) {
      case 0xfb -> addGold(20);
      case 0xfc -> addGold(50);
      case 0xfd -> addGold(100);
      case 0xfe -> addGold(200);
      case 0xff -> 0xff;
      default -> {
        final int itemId = script.params_20[0].get();

        if(itemId < 0xc0) {
          yield giveEquipment(REGISTRIES.equipment.getEntry(LodMod.id(LodMod.EQUIPMENT_IDS[itemId])).get()) ? 0 : 0xff;
        }
        yield giveItem(REGISTRIES.items.getEntry(LodMod.id(LodMod.ITEM_IDS[itemId - 192])).get()) ? 0 : 0xff;
      }
    };

    //LAB_80024574
    script.params_20[1].set(a0);

    //LAB_80024580
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes an item from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemTaken", description = "The item ID taken, of 0xff if none could be taken")
  @Method(0x80024590L)
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    final int itemId = script.params_20[0].get() & 0xff;

    if(itemId < 0xc0) {
      script.params_20[1].set(takeEquipmentId(REGISTRIES.equipment.getEntry(LodMod.id(LodMod.EQUIPMENT_IDS[itemId])).get()) ? 0 : 0xff);
    } else {
      script.params_20[1].set(takeItem(REGISTRIES.items.getEntry(LodMod.id(LodMod.ITEM_IDS[itemId - 192])).get()) ? 0 : 0xff);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Reads a value from a registry entry")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "registryId", description = "The registry to read from")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "entryId", description = "The entry to access from the registry")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "entryVar", description = "The var to read from the entry")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.ANY, name = "value", description = "The value read from the entry")
  public static FlowControl scriptReadRegistryEntryVar(final RunningScript<?> script) {
    final RegistryId registryId = script.params_20[0].getRegistryId();
    final RegistryId entryId = script.params_20[1].getRegistryId();
    final int entryVar = script.params_20[2].get();

    final Registry<?> registry = REGISTRIES.get(registryId);
    final RegistryEntry entry = registry.getEntry(entryId).get();

    if(entry instanceof final ScriptReadable readable) {
      readable.read(entryVar, script.params_20[3]);
    } else {
      throw new NotImplementedException("Registry %s entry %s is not script-readable".formatted(registryId, entryId));
    }

    return FlowControl.CONTINUE;
  }

  /**
   * Loads DRGN0 MRG @ 77382 (basic UI textures)
   * <ol start="0">
   *   <li>Game font</li>
   *   <li>Japanese font</li>
   *   <li>Japanese text</li>
   *   <li>Dialog box border</li>
   *   <li>Red downward bobbing arrow</li>
   * </ol>
   */
  @Method(0x800249b4L)
  public static void basicUiTexturesLoaded(final List<FileData> files) {
    final Rect4i[] rects = new Rect4i[28]; // image size, clut size, image size, clut size...
    System.arraycopy(rectArray28_80010770, 0, rects, 0, 28);

    rects[2] = new Rect4i(0, 0, 64, 16);
    rects[3] = new Rect4i(0, 0, 0, 0);

    final int[] indexOffsets = {0, 20, 22, 24, 26};

    //LAB_80024e88
    for(int i = 0; i < files.size(); i++) {
      final FileData data = files.get(i);

      if(data.size() != 0) {
        final Tim tim = new Tim(data);
        final int rectIndex = indexOffsets[i];

        if(i == 0 || i > 2) {
          GPU.uploadData15(rects[rectIndex], tim.getImageData());
        }

        //LAB_80024efc
        if(i == 3) {
          //LAB_80024f2c
          GPU.uploadData15(rects[indexOffsets[i] + 1], tim.getClutData());
        } else if(i < 4) {
          //LAB_80024fac
          for(int s0 = 0; s0 < 4; s0++) {
            final Rect4i rect = new Rect4i(rects[rectIndex + 1]);
            rect.x += s0 * 16;
            GPU.uploadData15(rect, tim.getClutData().slice(s0 * 0x80));
          }
          //LAB_80024f1c
        } else if(i == 4) {
          //LAB_80024f68
          GPU.uploadData15(rects[rectIndex + 1], tim.getClutData());
        }
      }
    }
  }

  @Method(0x8002504cL)
  public static void loadBasicUiTexturesAndSomethingElse() {
    loadDrgnDir(0, 6669, Scus94491BpeSegment_8002::basicUiTexturesLoaded);

    textZ_800bdf00 = 13;
    clearCharacterStats();
    initTextboxes();
  }

  private static final MV textTransforms = new MV();

  @Method(0x80029300L)
  public static void renderText(final String text, final float originX, final float originY, final FontOptions options) {
    renderText(text, originX, originY, options, null);
  }

  @Method(0x80029300L)
  public static void renderText(final String text, final float originX, final float originY, final FontOptions options, @Nullable final Consumer<QueuedModelStandard> queueCallback) {
    renderText(GameEngine.DEFAULT_FONT, text, originX, originY, options, queueCallback);
  }

  @Method(0x80029300L)
  public static void renderText(final Font font, final String text, final float originX, final float originY, final FontOptions options) {
    renderText(font, text, originX, originY, options, null);
  }

  @Method(0x80029300L)
  public static void renderText(final Font font, final String text, final float originX, final float originY, final FontOptions options, @Nullable final Consumer<QueuedModelStandard> queueCallback) {
    font.init();

    final float height = 12.0f * options.getSize();
    final float trim = Math.clamp(options.getTrim() * options.getSize(), -height, height);

    textTransforms.scaling(options.getSize());

    for(int i = 0; i < (options.hasShadow() ? 4 : 1); i++) {
      float x = switch(options.getHorizontalAlign()) {
        case LEFT -> originX;
        case CENTRE -> originX - font.lineWidth(text) * options.getSize() / 2.0f;
        case RIGHT -> originX - font.lineWidth(text) * options.getSize();
      };

      // I adjusted the texture so that glyphs start 1 pixel lower to fix bleeding - subtract 1 here to compensate
      float y = originY - 1;
      float glyphNudge = 0.0f;

      for(int charIndex = 0; charIndex < text.length(); charIndex++) {
        final char c = text.charAt(charIndex);

        if(c != ' ') {
          if(c == '\n') {
            x = switch(options.getHorizontalAlign()) {
              case LEFT -> originX;
              case CENTRE -> originX - font.lineWidth(text, charIndex + 1) * options.getSize() / 2.0f;
              case RIGHT -> originX - font.lineWidth(text, charIndex + 1) * options.getSize();
            };

            glyphNudge = 0.0f;
            y += height;
          } else {
            final float offsetX = (i & 1) * options.getSize();
            final float offsetY = (i >>> 1) * options.getSize();

            textTransforms.transfer.set(x + glyphNudge + offsetX, y + offsetY, textZ_800bdf00 * 4.0f);

            if(trim < 0) {
              textTransforms.transfer.y += trim;
            }

            if(i == 0 || font.usesColour(c)) {
              final QueuedModelStandard model = font.queueChar(InputCodepoints.getCodepoint(PLATFORM.getGamepadType(), c), textTransforms);

              if(font.usesColour(c)) {
                if(i == 0) {
                  model.colour(options.getRed(), options.getGreen(), options.getBlue());
                } else if(font.usesColour(c)) {
                  model.colour(options.getShadowRed(), options.getShadowGreen(), options.getShadowBlue());
                }
              }

              if(trim != 0) {
                if(trim < 0) {
                  model.scissor(0, (int)y + 1, displayWidth_1f8003e0, (int)(height + trim));
                } else {
                  model.scissor(0, (int)(y + 1 - trim), displayWidth_1f8003e0, (int)height);
                }
              }

              if(queueCallback != null) {
                queueCallback.accept(model);
              }
            }
          }
        }

        if(c != '\n') {
          glyphNudge += font.charWidth(c) * options.getSize();
        }
      }
    }
  }

  @Method(0x8002a6fcL)
  public static void clearCharacterStats() {
    //LAB_8002a730
    for(int charIndex = 0; charIndex < 9; charIndex++) {
      final ActiveStatsa0 stats = stats_800be5f8[charIndex];

      stats.xp_00 = 0;
      stats.hp_04 = 0;
      stats.mp_06 = 0;
      stats.sp_08 = 0;
      stats.dxp_0a = 0;
      stats.flags_0c = 0;
      stats.level_0e = 0;
      stats.dlevel_0f = 0;
      stats.equipment_30.clear();
      stats.selectedAddition_35 = 0;

      //LAB_8002a780;
      for(int i = 0; i < 8; i++) {
        stats.additionLevels_36[i] = 0;
        stats.additionXp_3e[i] = 0;
      }

      stats.equipmentPhysicalImmunity_46 = false;
      stats.equipmentMagicalImmunity_48 = false;
      stats.equipmentPhysicalResistance_4a = false;
      stats.equipmentSpMultiplier_4c = 0;
      stats.equipmentSpPerPhysicalHit_4e = 0;
      stats.equipmentMpPerPhysicalHit_50 = 0;
      stats.equipmentSpPerMagicalHit_52 = 0;
      stats.equipmentMpPerMagicalHit_54 = 0;
      stats.equipmentEscapeBonus_56 = 0;
      stats.equipmentHpRegen_58 = 0;
      stats.equipmentMpRegen_5a = 0;
      stats.equipmentSpRegen_5c = 0;
      stats.equipmentRevive_5e = 0;
      stats.equipmentMagicalResistance_60 = false;
      stats.equipmentHpMulti_62 = 0;
      stats.equipmentMpMulti_64 = 0;
      stats.maxHp_66 = 0;
      stats.addition_68 = 0;
      stats.bodySpeed_69 = 0;
      stats.bodyAttack_6a = 0;
      stats.bodyMagicAttack_6b = 0;
      stats.bodyDefence_6c = 0;
      stats.bodyMagicDefence_6d = 0;
      stats.maxMp_6e = 0;
      stats.spellId_70 = 0;
      stats._71 = 0;
      stats.dragoonAttack_72 = 0;
      stats.dragoonMagicAttack_73 = 0;
      stats.dragoonDefence_74 = 0;
      stats.dragoonMagicDefence_75 = 0;

      clearEquipmentStats(charIndex);

      stats.addition_00_9c = 0;
      stats.additionSpMultiplier_9e = 0;
      stats.additionDamageMultiplier_9f = 0;
    }

    characterStatsLoaded_800be5d0 = false;
  }

  @Method(0x8002a86cL)
  public static void clearEquipmentStats(final int charIndex) {
    final ActiveStatsa0 stats = stats_800be5f8[charIndex];

    stats.specialEffectFlag_76 = 0;
//    stats.equipmentType_77 = 0;
    stats.equipment_02_78 = 0;
    stats.equipmentEquipableFlags_79 = 0;
    stats.equipmentAttackElements_7a.clear();
    stats.equipment_05_7b = 0;
    stats.equipmentElementalResistance_7c.clear();
    stats.equipmentElementalImmunity_7d.clear();
    stats.equipmentStatusResist_7e = 0;
    stats.equipment_09_7f = 0;
    stats.equipmentAttack1_80 = 0;
    stats._83 = 0;
    stats.equipmentIcon_84 = 0;

    stats.equipmentSpeed_86 = 0;
    stats.equipmentAttack_88 = 0;
    stats.equipmentMagicAttack_8a = 0;
    stats.equipmentDefence_8c = 0;
    stats.equipmentMagicDefence_8e = 0;
    stats.equipmentAttackHit_90 = 0;
    stats.equipmentMagicHit_92 = 0;
    stats.equipmentAttackAvoid_94 = 0;
    stats.equipmentMagicAvoid_96 = 0;
    stats.equipmentOnHitStatusChance_98 = 0;
    stats.equipment_19_99 = 0;
    stats.equipment_1a_9a = 0;
    stats.equipmentOnHitStatus_9b = 0;
  }

  @Method(0x8002a9c0L)
  public static void resetSubmapToNewGame() {
    submapCut_80052c30 = 675; // Opening
    submapScene_80052c34 = 4;
    collidedPrimitiveIndex_80052c38 = 0;
    submapCutBeforeBattle_80052c3c = -1;
    shouldRestoreCameraPosition_80052c40 = false;
    submapEnvState_80052c44 = SubmapEnvState.CHECK_TRANSITIONS_1_2;
    bootMods(MODS.getAllModIds());
  }

  @Method(0x8002ced8L)
  public static void start() {
    main();
  }

  @Method(0x8002d220L)
  public static int strcmp(final String s1, final String s2) {
    return s1.compareToIgnoreCase(s2);
  }

  private static int randSeed = 0x24040001;

  @Method(0x8002d260L)
  public static int rand() {
    randSeed = randSeed * 0x41c6_4e6d + 0x3039;
    return randSeed >>> 16 & 0x7fff;
  }

  @Method(0x8002d270L)
  public static void srand(final int seed) {
    randSeed = seed;
  }
}
