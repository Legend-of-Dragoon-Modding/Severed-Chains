package legend.game.saves;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.GameEngine;
import legend.game.additions.Addition;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.CharacterTemplate;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.ItemStack;
import legend.game.inventory.SpellStats0c;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.controls.RetailSaveCard;
import legend.game.types.EquipmentSlot;
import legend.game.types.Flags;
import legend.game.types.GameState52c;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.lodmod.Legacy.CHARACTER_SPELLS;
import static legend.lodmod.Legacy.CHAR_IDS;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SP_STAT;

public class RetailSavedGame extends SavedGame {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RetailSavedGame.class);

  public String locationName;
  public int maxHp;
  public int maxMp;

  public final int[] scriptData = new int[0x20];
  public final IntList charIndices = new IntArrayList();
  public int gold;
  public int chapterIndex;
  public int stardust;
  public int timestamp;
  public int submapScene;
  public int submapCut;

  public int _b0;
  public int battleCount;
  public int turnCount;
  public final Flags scriptFlags2 = new Flags(32);
  public final Flags scriptFlags1 = new Flags(8);
  public final Flags wmapFlags = new Flags(8);
  public final Flags visitedLocations = new Flags(8);
  public final List<RegistryId> goodsIds = new ArrayList<>();
  public final int[] _1a4 = new int[8];
  public final int[] chestFlags = new int[8];

  public final List<RegistryId> equipmentIds = new ArrayList<>();
  public final List<InventoryEntry> itemIds = new ArrayList<>();

  public final SavedCharacter[] characters = new SavedCharacter[9];

  public int pathIndex;
  public int dotIndex;
  public float dotOffset;
  public int facing;
  public int directionalPathIndex;

  // Config stuff
  public boolean indicatorsDisabled;

  /** A bitset used to set each char's MP to max the first time each one is loaded */
  public int characterInitialized;

  public RetailSavedGame(final Campaign campaign, final String version, final String fileName, final String saveName, final RegistryId campaignType, final ConfigCollection config) {
    super(campaign, version, fileName, saveName, campaignType, config);
    Arrays.setAll(this.characters, i -> new SavedCharacter());
  }

  @Override
  public Control createSaveCard() {
    return new RetailSaveCard(this);
  }

  @Override
  public GameState52c createGameState() {
    final GameState52c gameState = new GameState52c();
    gameState.campaign = this.campaign;

    System.arraycopy(this.scriptData, 0, gameState.scriptData_08, 0, this.scriptData.length);
    gameState.charIds_88.addAll(this.charIndices);
    gameState.gold_94 = this.gold;
    gameState.chapterIndex_98 = this.chapterIndex;
    gameState.stardust_9c = this.stardust;
    gameState.timestamp_a0 = this.timestamp;
    gameState.submapScene_a4 = this.submapScene;
    gameState.submapCut_a8 = this.submapCut;
    gameState._b0 = this._b0;
    gameState.battleCount_b4 = this.battleCount;
    gameState.turnCount_b8 = this.turnCount;
    gameState.scriptFlags2_bc.set(this.scriptFlags2);
    gameState.scriptFlags1_13c.set(this.scriptFlags1);
    gameState.wmapFlags_15c.set(this.wmapFlags);
    gameState.visitedLocations_17c.set(this.visitedLocations);
    this.goodsIds.stream().map(REGISTRIES.goods::getEntry).forEach(gameState.goods_19c::give);
    System.arraycopy(this._1a4, 0, gameState._1a4, 0, this._1a4.length);
    System.arraycopy(this.chestFlags, 0, gameState.chestFlags_1c4, 0, this.chestFlags.length);
    this.equipmentIds.stream().map(REGISTRIES.equipment::getEntry).map(RegistryDelegate::get).forEach(gameState.equipment_1e8::add);

    for(final InventoryEntry entry : this.itemIds) {
      final RegistryDelegate<Item> delegate = GameEngine.REGISTRIES.items.getEntry(entry.id);

      if(!delegate.isValid()) {
        LOGGER.warn("Skipping unknown item ID %s", delegate.getId());
        continue;
      }

      final ItemStack stack = new ItemStack(delegate.get(), entry.size, entry.durability);
      stack.setExtraData(entry.extraData);
      gameState.items_2e9.give(stack, true);
    }

    for(int charId = 0; charId < this.characters.length; charId++) {
      final SavedCharacter savedCharacter = this.characters[charId];
      final CharacterTemplate template = REGISTRIES.characterTemplates.getEntry(CHAR_IDS[charId]).get();
      final CharacterData2c character = template.make(gameState);
      gameState.charData_32c.add(character);

      character.xp_00 = savedCharacter.xp;
      character.partyFlags_04 = savedCharacter.flags;
      character.stats.getStat(HP_STAT.get()).setCurrent(savedCharacter.hp);
      character.stats.getStat(MP_STAT.get()).setCurrent(savedCharacter.mp);
      character.stats.getStat(SP_STAT.get()).setCurrent(savedCharacter.sp);
      character.dlevelXp_0e = savedCharacter.dlevelXp;
      character.status_10 = savedCharacter.status;
      character.level_12 = savedCharacter.level;
      character.dlevel_13 = savedCharacter.dlevel;

      for(final EquipmentSlot slot : EquipmentSlot.values()) {
        if(savedCharacter.equipmentIds.containsKey(slot)) {
          final RegistryDelegate<Equipment> delegate = GameEngine.REGISTRIES.equipment.getEntry(savedCharacter.equipmentIds.get(slot));

          if(delegate.isValid()) {
            character.equip(slot, delegate.get());
          } else {
            LOGGER.warn("Skipping unknown equipment ID %s", delegate.getId());
          }
        }
      }

      final RegistryDelegate<Addition>[] additions = CHARACTER_ADDITIONS[charId];
      if(additions.length != 0) {
        final Set<RegistryId> seen = new HashSet<>();

        // Check retail additions first so we get them in order
        int i = 0;
        for(final RegistryDelegate<Addition> addition : additions) {
          final RegistryId id = addition.getId();
          final SeveredSavedCharacterV1.AdditionInfo saveAdditionInfo = savedCharacter.additionInfo.get(id);
          this.updateAddition(character, gameState.timestamp_a0 + i, id, saveAdditionInfo);
          seen.add(id);
          i++;
        }

        // Then add non-retail additions
        for(final var entry : savedCharacter.additionInfo.entrySet()) {
          final RegistryId id = entry.getKey();

          if(!seen.contains(id)) {
            final SeveredSavedCharacterV1.AdditionInfo saveAdditionInfo = entry.getValue();
            this.updateAddition(character, gameState.timestamp_a0 + i, id, saveAdditionInfo);
            i++;
          }
        }

        if(REGISTRIES.additions.hasEntry(savedCharacter.selectedAddition)) {
          character.selectedAddition_19 = savedCharacter.selectedAddition;
        } else {
          character.selectedAddition_19 = character.getUnlockedAdditions().getFirst();
        }
      }

      final RegistryDelegate<SpellStats0c>[] spells = CHARACTER_SPELLS[charId];
      if(spells.length != 0) {
        final Set<RegistryId> seen = new HashSet<>();

        // Check retail spells first so we get them in order
        int i = 0;
        for(final RegistryDelegate<SpellStats0c> spell : spells) {
          final RegistryId id = spell.getId();
          final CharacterSpellInfo info = character.getSpellInfo(id);

          if(info.checkUnlock(character)) {
            info.unlock(gameState.timestamp_a0 + i);
          }

          seen.add(id);
          i++;
        }

        // Then add non-retail spells
        for(final RegistryId id : character.getAllSpells()) {
          if(!seen.contains(id)) {
            final CharacterSpellInfo info = character.getSpellInfo(id);

            if(info.checkUnlock(character)) {
              info.unlock(gameState.timestamp_a0 + i);
            }

            i++;
          }
        }
      }
    }

    gameState.pathIndex_4d8 = this.pathIndex;
    gameState.dotIndex_4da = this.dotIndex;
    gameState.dotOffset_4dc = this.dotOffset;
    gameState.facing_4dd = this.facing;
    gameState.directionalPathIndex_4de = this.directionalPathIndex;

    gameState.indicatorsDisabled_4e3 = this.indicatorsDisabled;

    gameState.characterInitialized_4e6 = this.characterInitialized;

    gameState.charData_32c.get(0).hasTransformed = gameState.scriptFlags2_bc.get(0x1b8);
    gameState.charData_32c.get(1).hasTransformed = gameState.scriptFlags2_bc.get(0x1ba);
    gameState.charData_32c.get(2).hasTransformed = gameState.scriptFlags2_bc.get(0x1b9);
    gameState.charData_32c.get(3).hasTransformed = gameState.scriptFlags2_bc.get(0x1bb);
    gameState.charData_32c.get(4).hasTransformed = gameState.scriptFlags2_bc.get(0x1bc);
    gameState.charData_32c.get(5).hasTransformed = gameState.scriptFlags2_bc.get(0x1bd);
    gameState.charData_32c.get(6).hasTransformed = gameState.scriptFlags2_bc.get(0x1be);
    gameState.charData_32c.get(7).hasTransformed = gameState.scriptFlags2_bc.get(0x1bf);
    gameState.charData_32c.get(8).hasTransformed = gameState.scriptFlags2_bc.get(0x1c0);

    return gameState;
  }

  private void updateAddition(final CharacterData2c character, final int timestamp, final RegistryId id, final SeveredSavedCharacterV1.AdditionInfo saveAdditionInfo) {
    final CharacterAdditionInfo charAdditionInfo = character.getAdditionInfo(id);

    if(charAdditionInfo != null) {
      charAdditionInfo.level = saveAdditionInfo.level;
      charAdditionInfo.xp = saveAdditionInfo.xp;

      if(charAdditionInfo.checkUnlock(character)) {
        charAdditionInfo.unlock(timestamp);
      }
    }
  }

  @Override
  public boolean isValid() {
    return true;
  }

  public static final class SavedCharacter {
    public int xp;
    public int flags;
    public int hp;
    public int mp;
    public int sp;
    public int dlevelXp;
    public int status;
    public int level;
    public int dlevel;
    public final Map<EquipmentSlot, RegistryId> equipmentIds = new EnumMap<>(EquipmentSlot.class);
    public RegistryId selectedAddition;
    public final Map<RegistryId, SeveredSavedCharacterV1.AdditionInfo> additionInfo = new HashMap<>();
  }
}
