package legend.lodmod.characters;

import legend.core.Latch;
import legend.core.gte.MV;
import legend.core.memory.types.IntRef;
import legend.game.additions.UnlockState;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.CharacterTemplate;
import legend.game.characters.LevelUpActions;
import legend.game.characters.StatCollection;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.Good;
import legend.game.modding.events.characters.CharacterDragoonLevelUpEvent;
import legend.game.modding.events.characters.CharacterLevelUpEvent;
import legend.game.saves.SavedCharacter;
import legend.game.saves.SeveredSavedCharacterV2;
import legend.game.textures.Image;
import legend.game.textures.TextureAtlasIcon;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import legend.lodmod.LodMod;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.getTextureAtlas;
import static legend.lodmod.LodLevelUpActions.UNLOCK_ADDITION;
import static legend.lodmod.LodLevelUpActions.UNLOCK_SPELL;
import static legend.lodmod.LodMod.ATTACK_AVOID_STAT;
import static legend.lodmod.LodMod.ATTACK_HIT_STAT;
import static legend.lodmod.LodMod.ATTACK_STAT;
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
    stats.getStat(GUARD_HEAL_STAT.get()).setRaw(10);

    this.applyLevelUp(character, null);
    this.applyDragoonLevelUp(character, null);

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
    data.writeBool(offset, character.hasTransformed);
    data.writeInt(offset, character.status_10);
    character.stats.serialize(data, offset);

    data.writeInt(offset, EquipmentSlot.values().length);

    for(final EquipmentSlot slot : EquipmentSlot.values()) {
      data.writeAscii(offset, slot.name());
      data.writeRegistryId(offset, character.getEquipment(slot));
    }

    data.writeRegistryId(offset, character.selectedAddition_19);

    final Set<RegistryId> additionIds = character.getAllAdditions();
    data.writeShort(offset, additionIds.size());

    for(final RegistryId additionId : additionIds) {
      final CharacterAdditionInfo info = character.getAdditionInfo(additionId);
      data.writeRegistryId(offset, additionId);
      data.writeEnum(offset, info.getUnlockState());
      data.writeInt(offset, info.getUnlockTimestamp());
      data.writeShort(offset, info.level);
      data.writeInt(offset, info.xp);
    }

    final Collection<RegistryId> spellIds = character.getAllSpells();
    data.writeShort(offset, spellIds.size());

    for(final RegistryId spellId : spellIds) {
      final CharacterSpellInfo info = character.getSpellInfo(spellId);
      data.writeRegistryId(offset, spellId);
      data.writeEnum(offset, info.getUnlockState());
      data.writeInt(offset, info.getUnlockTimestamp());
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
    character.hasTransformed = data.readBool(offset);
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

    final int additionInfoCount = data.readUShort(offset);

    for(int i = 0; i < additionInfoCount; i++) {
      final RegistryId additionId = data.readRegistryId(offset);
      final UnlockState unlockState = data.readEnum(offset, UnlockState.class);
      final int unlockTimestamp = data.readInt(offset);
      final int level = data.readShort(offset);
      final int xp = data.readInt(offset);
      character.additionInfo.put(additionId, new SeveredSavedCharacterV2.AdditionInfo(unlockState, unlockTimestamp, level, xp));
    }

    final int spellInfoCount = data.readUShort(offset);

    for(int i = 0; i < spellInfoCount; i++) {
      final RegistryId spellId = data.readRegistryId(offset);
      final UnlockState unlockState = data.readEnum(offset, UnlockState.class);
      final int unlockTimestamp = data.readInt(offset);
      character.spellInfo.put(spellId, new SeveredSavedCharacterV2.SpellInfo(unlockState, unlockTimestamp));
    }

    return character;
  }

  @Override
  public Image loadPortrait() {
    return this.portrait.get();
  }

  @Override
  public void renderTransformIcon(final CharacterData2c character, final PlayerBattleEntity bent, final MV transforms, final int frame) {
    final TextureAtlasIcon icon = getTextureAtlas().getIcon(LodMod.id(bent.getElement().getRegistryId().entryId() + '_' + frame));
    icon.render(transforms);
  }

  @Override
  public void applyLevelUp(final CharacterData2c character, @Nullable final LevelUpActions actions) {
    final CharacterLevelUpEvent event = new CharacterLevelUpEvent(character);

    event.statsToAdd.put(HP_STAT.get(), this.getHpToAdd(character.level_12));
    event.statsToAdd.put(ATTACK_STAT.get(), this.getAttackToAdd(character.level_12));
    event.statsToAdd.put(DEFENSE_STAT.get(), this.getDefenseToAdd(character.level_12));
    event.statsToAdd.put(MAGIC_ATTACK_STAT.get(), this.getMagicAttackToAdd(character.level_12));
    event.statsToAdd.put(MAGIC_DEFENSE_STAT.get(), this.getMagicDefenseToAdd(character.level_12));
    event.statsToAdd.put(SPEED_STAT.get(), this.getSpeedToAdd(character.level_12));

    EVENTS.postEvent(event);

    for(final var entry : event.statsToAdd.object2IntEntrySet()) {
      this.addToStat(character, entry.getKey(), entry.getIntValue());
    }

    character.level_12 += event.levelsToAdd;

    this.checkUnlocks(character, actions);
  }

  @Override
  public void applyDragoonLevelUp(final CharacterData2c character, @Nullable final LevelUpActions actions) {
    final CharacterDragoonLevelUpEvent event = new CharacterDragoonLevelUpEvent(character);

    event.statsToAdd.put(MP_STAT.get(), this.getMpToAdd(character.dlevel_13));
    event.statsToAdd.put(SP_STAT.get(), this.getSpToAdd(character.dlevel_13));
    event.statsToAdd.put(DRAGOON_ATTACK_STAT.get(), this.getDragoonAttackToAdd(character.dlevel_13));
    event.statsToAdd.put(DRAGOON_DEFENSE_STAT.get(), this.getDragoonDefenseToAdd(character.dlevel_13));
    event.statsToAdd.put(DRAGOON_MAGIC_ATTACK_STAT.get(), this.getDragoonMagicAttackToAdd(character.dlevel_13));
    event.statsToAdd.put(DRAGOON_MAGIC_DEFENSE_STAT.get(), this.getDragoonMagicDefenseToAdd(character.dlevel_13));

    EVENTS.postEvent(event);

    for(final var entry : event.statsToAdd.object2IntEntrySet()) {
      this.addToStat(character, entry.getKey(), entry.getIntValue());
    }

    character.dlevel_13 += event.levelsToAdd;

    this.checkUnlocks(character, actions);
  }

  @Override
  public void checkUnlocks(final CharacterData2c character, @Nullable final LevelUpActions actions) {
    for(final RegistryId id : character.getAllAdditions()) {
      final CharacterAdditionInfo info = character.getAdditionInfo(id);

      if(info.checkUnlock(character)) {
        info.unlock(character.gameState.timestamp_a0);

        if(actions != null) {
          actions.add(UNLOCK_ADDITION.get(), new UnlockAdditionLevelUpActionOptions(id));
        }
      }
    }

    for(final RegistryId id : character.getAllSpells()) {
      final CharacterSpellInfo info = character.getSpellInfo(id);

      if(info.checkUnlock(character)) {
        info.unlock(character.gameState.timestamp_a0);

        if(actions != null) {
          actions.add(UNLOCK_SPELL.get(), new UnlockSpellLevelUpActionOptions(id));
        }
      }
    }
  }

  @Override
  public boolean hasDragoon(final CharacterData2c character) {
    return character.gameState.goods_19c.has(this.getDragoonSpirit());
  }

  @Override
  public void loadAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    if(!bent.isDragoon()) {
      this.loadHumanAttackAnimations(character, bent, onLoad);
    } else {
      this.loadDragoonAttackAnimations(character, bent, onLoad);
    }
  }

  public void loadHumanAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    REGISTRIES.additions.getEntry(character.selectedAddition_19).get().loadAnimations(character, character.getAdditionInfo(character.selectedAddition_19), onLoad);
  }

  public abstract void loadDragoonAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad);

  @Override
  public Path getBattleModelPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    final String file = bent.isDragoon() ? "dragoon" : "combat";
    return Loader.resolve(Path.of("characters", this.getRegistryId().entryId(), "models", file));
  }

  @Override
  public Path getBattleTexturePath(final CharacterData2c character, final PlayerBattleEntity bent) {
    final String file = bent.isDragoon() ? "dragoon" : "combat";
    return Loader.resolve(Path.of("characters", this.getRegistryId().entryId(), "textures", file));
  }

  @Override
  public Path getBattleSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("characters", this.getRegistryId().entryId(), "sounds", "combat"));
  }

  protected abstract Good getDragoonSpirit();

  protected abstract int getHpToAdd(final int level);

  protected int getMpToAdd(final int level) {
    return 20;
  }

  protected int getSpToAdd(final int level) {
    return 100;
  }

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

  protected void swapAddition(final CharacterData2c character, final RegistryId from, final RegistryId to) {
    final CharacterAdditionInfo info = character.getAdditionInfo(from);

    if(info != null) {
      character.removeAddition(from);
      character.addAddition(to, info);
    }

    if(from.equals(character.selectedAddition_19)) {
      character.selectedAddition_19 = to;
    }
  }

  protected void swapSpell(final CharacterData2c character, final RegistryId from, final RegistryId to) {
    final CharacterSpellInfo info = character.getSpellInfo(from);

    if(info != null) {
      character.removeSpell(from);
      character.addSpell(to, info);
    }
  }
}
