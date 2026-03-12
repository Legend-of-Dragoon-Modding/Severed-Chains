package legend.lodmod.characters;

import legend.game.additions.AdditionHitProperties10;
import legend.game.additions.AdditionHits80;
import legend.game.characters.AdditionLevelUnlockCriterion;
import legend.game.characters.AdditionMasteryUnlockCriterion;
import legend.game.characters.CharacterAdditionInfo;
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
import legend.lodmod.LodAdditions;
import legend.lodmod.LodSpells;

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

public class AlbertTemplate extends LavitzTemplate {
  @Override
  public CharacterData2c make(final GameState52c gameState) {
    final StatCollection stats = new StatCollection(HP_STAT.get(), MP_STAT.get(), SP_STAT.get(), SPEED_STAT.get(), ATTACK_STAT.get(), MAGIC_ATTACK_STAT.get(), DEFENSE_STAT.get(), MAGIC_DEFENSE_STAT.get(), ATTACK_HIT_STAT.get(), MAGIC_HIT_STAT.get(), ATTACK_AVOID_STAT.get(), MAGIC_AVOID_STAT.get(), DRAGOON_ATTACK_STAT.get(), DRAGOON_MAGIC_ATTACK_STAT.get(), DRAGOON_DEFENSE_STAT.get(), DRAGOON_MAGIC_DEFENSE_STAT.get(), GUARD_HEAL_STAT.get());
    final AlbertCharacterData character = new AlbertCharacterData(gameState, this, stats);

    stats.getStat(ATTACK_HIT_STAT.get()).setRaw(100);
    stats.getStat(MAGIC_HIT_STAT.get()).setRaw(100);
    stats.getStat(GUARD_HEAL_STAT.get()).setRaw(10);

    this.applyLevelUp(character, null);
    this.applyDragoonLevelUp(character, null);

    final VitalsStat hp = character.stats.getStat(HP_STAT.get());
    final VitalsStat mp = character.stats.getStat(MP_STAT.get());
    hp.restore();
    mp.restore();

    character.addAddition(LodAdditions.ALBERT_HARPOON.getId(), new CharacterAdditionInfo(List.of()));
    character.addAddition(LodAdditions.ALBERT_SPINNING_CANE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(5))));
    character.addAddition(LodAdditions.ALBERT_ROD_TYPHOON.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(7))));
    character.addAddition(LodAdditions.ALBERT_GUST_OF_WIND_DANCE.getId(), new CharacterAdditionInfo(List.of(new AdditionLevelUnlockCriterion(11))));
    character.addAddition(LodAdditions.ALBERT_FLOWER_STORM.getId(), new CharacterAdditionInfo(List.of(new AdditionMasteryUnlockCriterion())));

    character.addSpell(LodSpells.WING_BLASTER.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion()))).unlock(gameState.timestamp_a0);
    character.addSpell(LodSpells.ROSE_STORM.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(2))));
    character.addSpell(LodSpells.GASPLESS.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(3))));
    character.addSpell(LodSpells.JADE_DRAGON.getId(), new CharacterSpellInfo(List.of(new SpellDragoonSpiritUnlockCriterion(), new SpellDragoonLevelUnlockCriterion(5))));

    character.selectedAddition_19 = LodAdditions.ALBERT_HARPOON.getId();

    return character;
  }

  @Override
  public void loadWorldMapModel(final CharacterData2c character, final Consumer<List<FileData>> onLoad) {
    Loader.loadFiles(onLoad, "SECT/DRGN22.BIN/836/132", "SECT/DRGN22.BIN/836/textures/4", "SECT/DRGN22.BIN/836/133", "SECT/DRGN22.BIN/836/134", "SECT/DRGN22.BIN/836/135");
  }

  @Override
  public AdditionHits80 getDragoonAddition(final CharacterData2c character) {
    return new AdditionHits80(new AdditionHitProperties10(0xc0, 0, 0, 0, 100, 0, 41, 0, 0, 0, 0, 0, 12, 0, 18, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 0), new AdditionHitProperties10(0xc0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
  }

  @Override
  public void loadDragoonAttackAnimations(final CharacterData2c character, final PlayerBattleEntity bent, final Consumer<List<FileData>> onLoad) {
    loadDrgnDir(0, 4108, onLoad);
  }

  @Override
  public int getShadowSize(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 0x1300;
  }

  @Override
  public int getDragoonTransformDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 47;
  }

  @Override
  public int getDragoonAttackDeff(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 53;
  }

  @Override
  public int getDragoonAttackSounds(final CharacterData2c character, final PlayerBattleEntity bent) {
    return 109;
  }
}
