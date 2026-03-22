package legend.lodmod.characters;

import legend.game.characters.CharacterData2c;
import legend.game.characters.CharacterSpellInfo;
import legend.game.characters.SpellDragoonLevelUnlockCriterion;
import legend.game.characters.SpellDragoonSpiritUnlockCriterion;
import legend.game.characters.StatCollection;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import legend.lodmod.LodSpells;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static legend.game.DrgnFiles.loadDrgnDir;
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

public class MirandaTemplate extends ShanaTemplate {
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

    character.addSpell(LodSpells.MIRANDA_MOON_LIGHT.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion()))).unlock(gameState.timestamp_a0);
    character.addSpell(LodSpells.MIRANDA_STAR_CHILDREN.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(2))));
    character.addSpell(LodSpells.MIRANDA_GATES_OF_HEAVEN.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.WHITE_SILVER_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(5))));

    return character;
  }

  @Override
  public void copy(final CharacterData2c from, final CharacterData2c to) {
    super.copy(from, to);

    this.swapSpell(to, LodSpells.MOON_LIGHT.getId(), LodSpells.MIRANDA_MOON_LIGHT.getId());
    this.swapSpell(to, LodSpells.STAR_CHILDREN.getId(), LodSpells.MIRANDA_STAR_CHILDREN.getId());
    this.swapSpell(to, LodSpells.GATES_OF_HEAVEN.getId(), LodSpells.MIRANDA_GATES_OF_HEAVEN.getId());
  }

  @Override
  public void loadWorldMapModel(final CharacterData2c character, final Consumer<List<FileData>> onLoad) {
    Loader.loadFiles(onLoad, "SECT/DRGN22.BIN/836/231", "SECT/DRGN22.BIN/836/textures/7", "SECT/DRGN22.BIN/836/232", "SECT/DRGN22.BIN/836/233", "SECT/DRGN22.BIN/836/234");
  }

  @Override
  public int prepareAttack(final CharacterData2c character, final PlayerBattleEntity bent) {
    bent.battle.playBentSound(1, bent, 0, 0, 0, bent.isDragoon() ? 38 : 46, 0);
    bent.battle.playBentSound(1, bent, 4, 0, 0, bent.isDragoon() ? 18 : 22, 0);
    bent.battle.playBentSound(1, bent, 5, 0, 0, bent.isDragoon() ? 40 : 48, 0);
    return bent.isDragoon() ? 38 : 46;
  }

  @Override
  public Path getAttackSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1306"));
  }

  @Override
  public Path getDragoonAttackSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1315"));
  }

  @Override
  public Path getDragoonTransformSoundsPath(final CharacterData2c character, final PlayerBattleEntity bent) {
    return Loader.resolve(Path.of("SECT/DRGN0.BIN/1325"));
  }

  @Override
  public void loadHumanAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, 4095, onLoad);
  }

  @Override
  public void loadDragoonAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, 4111, onLoad);
  }

  @Override
  public int getParticleColour(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0xe8e088;
  }

  @Override
  public int getLeftHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 10;
  }

  @Override
  public int getRightHandModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 11;
  }

  @Override
  public int getFootModelPart(final CharacterData2c character, final PlayerBattleEntity bent) {
    if(bent.isDragoon()) {
      return 11;
    }

    return 12;
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x1300;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 64;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 112;
  }
}
