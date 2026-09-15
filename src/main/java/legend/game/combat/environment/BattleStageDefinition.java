package legend.game.combat.environment;

import legend.game.combat.Battle;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.types.StageDeffThing08;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static legend.game.combat.Battle.deffManager_800c693c;

/** A registered battle environment, independent of the retail stage tables and asset directories. */
public abstract class BattleStageDefinition extends RegistryEntry {
  /** Called after queued texture animations finish; complete only when every stage resource is ready. */
  public abstract CompletableFuture<?> load(final Battle battle);

  /** Retail transformations can intentionally retain the previous model/background until replacement. */
  public boolean clearPreviousStageBeforeLoad() {
    return true;
  }

  public abstract StageAmbiance4c ambiance();

  public StageAmbiance4c dragoonAmbiance(final int index) {
    return this.ambiance();
  }

  public StageDeffThing08 effects() {
    return new StageDeffThing08(0, 0, 0);
  }

  /** Legacy scripts see this alias; -1 means that this environment has no native identity. */
  public int legacyIndex() {
    return -1;
  }

  public int dragoonSpaceIndex(final int legacyStage) {
    return -1;
  }

  /** Preserve script edits to the active DEFF flags. Null disables stage effects. */
  @Nullable
  public StageDeffThing08 activeEffects(final Battle battle) {
    final DeffManager7cc.Struct08 flags = deffManager_800c693c._00;
    return new StageDeffThing08(flags._00, flags._02, flags._04);
  }

  public void initializeEffects(final Battle battle) {
    final StageDeffThing08 flags = this.effects();
    deffManager_800c693c._00._00 = flags._00;
    deffManager_800c693c._00._02 = flags._02;
    deffManager_800c693c._00._04 = flags._04;
  }
}
