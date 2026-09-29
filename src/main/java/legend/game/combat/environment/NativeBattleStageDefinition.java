package legend.game.combat.environment;

import legend.game.combat.Battle;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.types.StageDeffThing08;
import legend.game.types.McqHeader;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.Battle.melbuStageIndices_800fb064;
import static legend.game.combat.SBtld._8011517c;
import static legend.game.combat.environment.Ambiance.stageAmbiance_801134fc;

/** Adapter for retail stage assets, lighting, Dragoon space, and Melbu script conventions. */
public class NativeBattleStageDefinition extends BattleStageDefinition {
  private final int index;

  public NativeBattleStageDefinition(final int index) {
    if(index < 0 || index >= count()) throw new IllegalArgumentException("Invalid native battle stage " + index);
    this.index = index;
  }

  public static int count() {
    return Math.min(_8011517c.length, stageAmbiance_801134fc.length);
  }

  @Override
  public int legacyIndex() {
    return this.index;
  }

  @Override
  public boolean clearPreviousStageBeforeLoad() {
    return false;
  }

  @Override
  public CompletableFuture<PreparedBattleStage> prepare() {
    return loadDrgnDir(0, 2497 + this.index).thenCombine(
      loadDrgnDir(0, (2497 + this.index) + "/0"),
      (background, model) -> battle -> {
        if(background.get(1).hasVirtualSize()) battle.loadStageMcq(new McqHeader(background.get(1)));
        if(background.get(2).size() != 0) battle.loadStageTim(background.get(2));
        battle.loadStageTmdAndAnim("DRGN0/" + (2497 + this.index) + "/0", model);
      }
    );
  }

  @Override
  public StageAmbiance4c ambiance() {
    return stageAmbiance_801134fc[this.index];
  }

  @Override
  public StageAmbiance4c dragoonAmbiance(final int index) {
    return stageAmbiance_801134fc[71 + index];
  }

  @Override
  public int dragoonSpaceIndex(final int legacyStage) {
    return legacyStage >= 71 && legacyStage <= 78 ? legacyStage - 71 : -1;
  }

  @Override
  public StageDeffThing08 effects() {
    return _8011517c[this.index];
  }

  @Override
  public void initializeEffects(final Battle battle) {
    super.initializeEffects(battle);
    final StageDeffThing08 flags = this.effects();
    for(int i = 0; melbuStageIndices_800fb064[i] != -1; i++) {
      deffManager_800c693c._08[i]._00 = flags._00;
      deffManager_800c693c._08[i]._02 = flags._02;
    }
  }

  @Nullable
  @Override
  public StageDeffThing08 activeEffects(final Battle battle) {
    final int stage = battle.currentStage_800c66a4;
    if(this.dragoonSpaceIndex(stage) >= 0) return null;
    for(int i = 0; melbuStageIndices_800fb064[i] != -1; i++) {
      if(melbuStageIndices_800fb064[i] == stage) {
        final DeffManager7cc.Struct04 flags = deffManager_800c693c._08[i];
        return new StageDeffThing08(flags._00, flags._02, 0);
      }
    }
    return super.activeEffects(battle);
  }
}
