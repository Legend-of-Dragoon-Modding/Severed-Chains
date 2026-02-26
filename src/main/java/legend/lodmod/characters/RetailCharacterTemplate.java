package legend.lodmod.characters;

import legend.core.Latch;
import legend.core.memory.types.IntRef;
import legend.game.textures.Image;
import legend.game.additions.CharacterAdditionStats;
import legend.game.additions.UnlockState;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.LevelUpActions;
import legend.game.characters.StatCollection;
import legend.game.characters.VitalsStat;
import legend.game.inventory.Good;
import legend.game.saves.SavedCharacter;
import legend.game.saves.SeveredSavedCharacterV2;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

import static legend.lodmod.LodLevelUpActions.DRAGOON_LEVEL_UP;
import static legend.lodmod.LodLevelUpActions.LEVEL_UP;
import static legend.lodmod.LodLevelUpActions.UNLOCK_ADDITION;
import static legend.lodmod.LodLevelUpActions.UNLOCK_SPELL;
import static legend.lodmod.LodMod.ATTACK_HIT_STAT;
import static legend.lodmod.LodMod.ATTACK_STAT;
import static legend.lodmod.LodMod.ATTACK_AVOID_STAT;
import static legend.lodmod.LodMod.DEFENSE_STAT;
import static legend.lodmod.LodMod.DRAGOON_ATTACK_STAT;
import static legend.lodmod.LodMod.DRAGOON_DEFENSE_STAT;
import static legend.lodmod.LodMod.DRAGOON_MAGIC_ATTACK_STAT;
import static legend.lodmod.LodMod.DRAGOON_MAGIC_DEFENSE_STAT;
import static legend.lodmod.LodMod.GUARD_HEAL_STAT;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MAGIC_ATTACK_STAT;
import static legend.lodmod.LodMod.MAGIC_AVOID_STAT;
import static legend.lodmod.LodMod.MAGIC_DEFENSE_STAT;
import static legend.lodmod.LodMod.MAGIC_HIT_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SPEED_STAT;
import static legend.lodmod.LodMod.SP_STAT;

public abstract class RetailCharacterTemplate extends CharacterTemplate {
  private final Latch<Image> portrait = new Latch<>(() -> Image.load(Loader.resolve("characters/" + this.getRegistryId().entryId() + "/portrait.png")));

  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final StatCollection stats = new StatCollection(HP_STAT.get(), MP_STAT.get(), SP_STAT.get(), SPEED_STAT.get(), ATTACK_STAT.get(), MAGIC_ATTACK_STAT.get(), DEFENSE_STAT.get(), MAGIC_DEFENSE_STAT.get(), ATTACK_HIT_STAT.get(), MAGIC_HIT_STAT.get(), ATTACK_AVOID_STAT.get(), MAGIC_AVOID_STAT.get(), DRAGOON_ATTACK_STAT.get(), DRAGOON_MAGIC_ATTACK_STAT.get(), DRAGOON_DEFENSE_STAT.get(), DRAGOON_MAGIC_DEFENSE_STAT.get(), GUARD_HEAL_STAT.get());
    final CharacterData2c character = new CharacterData2c(gameState, this, stats);

    stats.getStat(ATTACK_HIT_STAT.get()).setRaw(100);
    stats.getStat(MAGIC_HIT_STAT.get()).setRaw(100);

    this.applyLevelUp(gameState, character, null);
    this.applyDragoonLevelUp(gameState, character, null);

    final VitalsStat hp = character.stats.getStat(HP_STAT.get());
    final VitalsStat mp = character.stats.getStat(MP_STAT.get());
    hp.restore();
    mp.restore();

    return character;
  }

  @Override
  public void serialize(final CharacterData2c character, final FileData data, final IntRef offset) {
    data.writeInt(offset, character.partyFlags_04);
    data.writeInt(offset, character.xp_00);
    data.writeInt(offset, character.dlevelXp_0e);
    data.writeInt(offset, character.level_12);
    data.writeInt(offset, character.dlevel_13);
    data.writeInt(offset, character.status_10);
    character.stats.serialize(data, offset);

    data.writeInt(offset, EquipmentSlot.values().length);

    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      data.writeAscii(offset, slot.name());
      data.writeRegistryId(offset, character.getEquipment(slot));
    }

    data.writeRegistryId(offset, character.selectedAddition_19);

    data.writeShort(offset, character.additionStats.size());

    for(final var entry : character.additionStats.entrySet()) {
      final CharacterAdditionStats stats = entry.getValue();
      data.writeRegistryId(offset, entry.getKey());
      data.writeEnum(offset, stats.unlockState);
      data.writeShort(offset, stats.level);
      data.writeInt(offset, stats.xp);
    }
  }

  @Override
  public SavedCharacter deserialize(final FileData data, final IntRef offset) {
    final SeveredSavedCharacterV2 character = new SeveredSavedCharacterV2(this.getRegistryId());

    character.flags = data.readInt(offset);
    character.xp = data.readInt(offset);
    character.dxp = data.readInt(offset);
    character.level = data.readInt(offset);
    character.dlevel = data.readInt(offset);
    character.status = data.readInt(offset);
    character.stats = StatCollection.deserialize(data, offset);

    final int equipmentSlotCount = data.readInt(offset);

    for(int i = 0; i < equipmentSlotCount; i++) {
      final String slotName = data.readAscii(offset);
      final RegistryId equipmentId = data.readRegistryId(offset);

      if(equipmentId != null) {
        final EquipmentSlot slot = EquipmentSlot.valueOf(slotName);
        character.equipmentIds.put(slot, equipmentId);
      }
    }

    character.selectedAddition = data.readRegistryId(offset);

    final int additionStatsCount = data.readUShort(offset);

    for(int i = 0; i < additionStatsCount; i++) {
      final CharacterAdditionStats stats = new CharacterAdditionStats();
      final RegistryId additionId = data.readRegistryId(offset);
      stats.unlockState = data.readEnum(offset, UnlockState.class);
      stats.level = data.readShort(offset);
      stats.xp = data.readInt(offset);
      character.additionStats.put(additionId, stats);
    }

    return character;
  }

  @Override
  public Image loadPortrait() {
    return this.portrait.get();
  }

  @Override
  public void applyLevelUp(final GameState52c gameState, final CharacterData2c character, @Nullable final LevelUpActions actions) {
    this.addToStat(character, HP_STAT.get(), this.getHpToAdd(character.level_12));
    this.addToStat(character, ATTACK_STAT.get(), this.getAttackToAdd(character.level_12));
    this.addToStat(character, DEFENSE_STAT.get(), this.getDefenseToAdd(character.level_12));
    this.addToStat(character, MAGIC_ATTACK_STAT.get(), this.getMagicAttackToAdd(character.level_12));
    this.addToStat(character, MAGIC_DEFENSE_STAT.get(), this.getMagicDefenseToAdd(character.level_12));
    this.addToStat(character, SPEED_STAT.get(), this.getSpeedToAdd(character.level_12));

    character.level_12++;

    if(actions != null) {
      actions.add(LEVEL_UP.get(), null);

      final RegistryId additionId = this.getAdditionUnlock(character.level_12);
      if(additionId != null) {
        actions.add(UNLOCK_ADDITION.get(), new UnlockAdditionLevelUpActionOptions(additionId));
      }
    }
  }

  @Override
  public void applyDragoonLevelUp(final GameState52c gameState, final CharacterData2c character, @Nullable final LevelUpActions actions) {
    this.addToStat(character, MP_STAT.get(), 20);
    this.addToStat(character, SP_STAT.get(), 100);
    this.addToStat(character, DRAGOON_ATTACK_STAT.get(), this.getDragoonAttackToAdd(character.dlevel_13));
    this.addToStat(character, DRAGOON_DEFENSE_STAT.get(), this.getDragoonDefenseToAdd(character.dlevelXp_0e));
    this.addToStat(character, DRAGOON_MAGIC_ATTACK_STAT.get(), this.getDragoonMagicAttackToAdd(character.dlevel_13));
    this.addToStat(character, DRAGOON_MAGIC_DEFENSE_STAT.get(), this.getDragoonMagicDefenseToAdd(character.dlevel_13));

    character.dlevel_13++;

    if(actions != null) {
      actions.add(DRAGOON_LEVEL_UP.get(), null);

      final int spellId = this.getSpellUnlock(character.dlevel_13);
      if(spellId != -1) {
        actions.add(UNLOCK_SPELL.get(), new UnlockSpellLevelUpActionOptions(spellId));
      }
    }
  }

  @Override
  public boolean hasDragoon(final GameState52c gameState, final CharacterData2c character) {
    return gameState.goods_19c.has(this.getDragoonSpirit());
  }

  protected abstract Good getDragoonSpirit();

  protected abstract int getHpToAdd(final int level);
  protected abstract int getSpeedToAdd(final int level);
  protected abstract int getAttackToAdd(final int level);
  protected abstract int getDefenseToAdd(final int level);
  protected abstract int getMagicAttackToAdd(final int level);
  protected abstract int getMagicDefenseToAdd(final int level);
  protected abstract int getDragoonAttackToAdd(final int dlevel);

  protected int getDragoonDefenseToAdd(final int dlevel) {
    if(dlevel == 0) {
      return 200;
    }

    if(dlevel == 4) {
      return 20;
    }

    return 10;
  }

  protected abstract int getDragoonMagicAttackToAdd(final int dlevel);

  protected int getDragoonMagicDefenseToAdd(final int dlevel) {
    if(dlevel == 0) {
      return 200;
    }

    if(dlevel == 4) {
      return 20;
    }

    return 10;
  }

  protected abstract RegistryId getAdditionUnlock(final int level);
  protected abstract int getSpellUnlock(final int dlevel);
}
