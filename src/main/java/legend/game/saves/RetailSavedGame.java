package legend.game.saves;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.GameEngine;
import legend.game.additions.CharacterAdditionStats;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.ItemStack;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.controls.RetailSaveCard;
import legend.game.types.CharacterData2c;
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
import java.util.List;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;

public class RetailSavedGame extends SavedGame {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RetailSavedGame.class);

  public String locationName;
  public int maxHp;
  public int maxMp;

  public final int[] scriptData = new int[0x20];
  public final IntList charIds = new IntArrayList();
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

  public final CharStats[] charStats = new CharStats[9];

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
    Arrays.setAll(this.charStats, i -> new CharStats());
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
    gameState.charIds_88.addAll(this.charIds);
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

    for(int charId = 0; charId < this.charStats.length; charId++) {
      final RetailSavedGame.CharStats stats = this.charStats[charId];
      final CharacterData2c charData = gameState.charData_32c[charId];

      charData.xp_00 = stats.xp;
      charData.partyFlags_04 = stats.flags;
      charData.hp_08 = stats.hp;
      charData.mp_0a = stats.mp;
      charData.sp_0c = stats.sp;
      charData.dlevelXp_0e = stats.dlevelXp;
      charData.status_10 = stats.status;
      charData.level_12 = stats.level;
      charData.dlevel_13 = stats.dlevel;

      for(final EquipmentSlot slot : EquipmentSlot.values()) {
        if(stats.equipmentIds.containsKey(slot)) {
          final RegistryDelegate<Equipment> delegate = GameEngine.REGISTRIES.equipment.getEntry(stats.equipmentIds.get(slot));

          if(delegate.isValid()) {
            charData.equipment_14.put(slot, delegate.get());
          } else {
            LOGGER.warn("Skipping unknown equipment ID %s", delegate.getId());
          }
        }
      }

      charData.selectedAddition_19 = stats.selectedAddition;
      charData.additionStats.putAll(stats.additionStats);
    }

    gameState.pathIndex_4d8 = this.pathIndex;
    gameState.dotIndex_4da = this.dotIndex;
    gameState.dotOffset_4dc = this.dotOffset;
    gameState.facing_4dd = this.facing;
    gameState.directionalPathIndex_4de = this.directionalPathIndex;

    gameState.indicatorsDisabled_4e3 = this.indicatorsDisabled;

    gameState.characterInitialized_4e6 = this.characterInitialized;

    return gameState;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  public static final class CharStats {
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
    public final Map<RegistryId, CharacterAdditionStats> additionStats = new HashMap<>();
  }
}
