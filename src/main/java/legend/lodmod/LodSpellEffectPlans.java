package legend.lodmod;

import legend.game.combat.spells.CleanseSpellEffect;
import legend.game.combat.spells.DamageSpellEffect;
import legend.game.combat.spells.ExecutionMode;
import legend.game.combat.spells.HealHpSpellEffect;
import legend.game.combat.spells.ReviveSpellEffect;
import legend.game.combat.spells.SpellEffect;
import legend.game.combat.spells.SpellEffectPlan;
import legend.game.combat.spells.SpellStat;
import legend.game.combat.spells.SpellTargetProfile;
import legend.game.combat.spells.StatModifierSpellEffect;
import legend.game.combat.spells.TargetLifeState;
import legend.game.combat.spells.TargetScope;
import legend.game.combat.spells.TargetSide;

import java.util.List;

final class LodSpellEffectPlans {
  private LodSpellEffectPlans() { }

  static SpellEffectPlan damage(final TargetScope scope, final int power) {
    return enemies(scope, new DamageSpellEffect(power));
  }

  static SpellEffectPlan damageResistance() {
    return allies(
      TargetScope.ALL,
      TargetLifeState.LIVING,
      new StatModifierSpellEffect(SpellStat.DEFENCE, 50, 3),
      new StatModifierSpellEffect(SpellStat.MAGIC_DEFENCE, 50, 3)
    );
  }

  static SpellEffectPlan reviveAndRecover(final TargetScope scope, final int recovery) {
    return allies(scope, TargetLifeState.ANY, new ReviveSpellEffect(50), new CleanseSpellEffect(0xff), new HealHpSpellEffect(recovery, true));
  }

  static SpellEffectPlan enemies(final TargetScope scope, final SpellEffect... effects) {
    return new SpellEffectPlan(new SpellTargetProfile(TargetSide.ENEMIES, scope, TargetLifeState.LIVING), List.of(effects), ExecutionMode.LEGACY);
  }

  static SpellEffectPlan allies(final TargetScope scope, final TargetLifeState lifeState, final SpellEffect... effects) {
    return new SpellEffectPlan(new SpellTargetProfile(TargetSide.ALLIES, scope, lifeState), List.of(effects), ExecutionMode.LEGACY);
  }
}
