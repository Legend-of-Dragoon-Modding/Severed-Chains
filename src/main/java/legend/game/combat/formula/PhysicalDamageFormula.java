package legend.game.combat.formula;

import legend.game.characters.Element;
import legend.game.combat.Battle;
import legend.game.combat.effects.AdditionOverlaysEffect44;
import legend.game.combat.types.AttackType;
import legend.game.modding.coremod.CoreMod;

import static legend.core.GameEngine.CONFIG;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.combat.Battle.adjustDamageForPower;

public final class PhysicalDamageFormula {
  private PhysicalDamageFormula() { }

  public static int calculatePhysicalDamage(final State<Integer> state) {
    return state.bents.get(Side.ATTACKER).calculatePhysicalDamage(state.bents.get(Side.DEFENDER));
  }

  public static int applyElementalInteractions(final State<Integer> state) {
    int damage = state.value();

    final Element defendElement = state.bents.get(Side.DEFENDER).getElement();

    for(final Element attackElement : state.bents.get(Side.ATTACKER).getAttackElements()) {
      damage = attackElement.adjustAttackingElementalDamage(AttackType.PHYSICAL, damage, defendElement);
      damage = defendElement.adjustDefendingElementalDamage(AttackType.PHYSICAL, damage, attackElement);
    }

    return damage;
  }

  public static int applyPower(final State<Integer> state) {
    return adjustDamageForPower(state.value(), state.bents.get(Side.ATTACKER).powerAttack_b4, state.bents.get(Side.DEFENDER).powerDefence_b8);
  }

  public static int applyAdditionResultsModifier(final State<Integer> state) {
    int damage = state.value();
    if(AdditionOverlaysEffect44.additionResults != null) {
      if(CONFIG.getConfig(CoreMod.ADDITION_ALLOW_MISINPUT_CONFIG.get()) && AdditionOverlaysEffect44.additionResults.hits < AdditionOverlaysEffect44.additionResults.additionHits) {
        final float ratio = (float)(AdditionOverlaysEffect44.additionResults.hits + 1) / (AdditionOverlaysEffect44.additionResults.additionHits + 1);
        damage = Math.round(damage * ratio);
      }
    }
    return damage;
  }

  public static int applyDragoonSpace(final State<Integer> state) {
    final Element element = ((Battle)currentEngineState_8004dd04).dragoonSpaceElement_800c6b64;
    int damage = state.value();

    if(element != null) {
      for(final Element attackElement : state.bents.get(Side.ATTACKER).getAttackElements()) {
        damage = element.adjustDragoonSpaceDamage(AttackType.PHYSICAL, damage, attackElement);
      }
    }

    return damage;
  }

  public static int applyDamageMultipliers(final State<Integer> state) {
    return state.bents.get(Side.ATTACKER).applyPhysicalDamageMultipliers(state.bents.get(Side.DEFENDER), state.value());
  }

  public static int applyAttackEffects(final State<Integer> state) {
    state.bents.get(Side.ATTACKER).applyAttackEffects();
    return state.value();
  }

  public static int applyResistanceAndImmunity(final State<Integer> state) {
    return state.bents.get(Side.DEFENDER).applyDamageResistanceAndImmunity(state.value(), AttackType.PHYSICAL);
  }

  public static int applyElementalResistanceAndImmunity(final State<Integer> state) {
    int damage = state.value();

    for(final Element attackElement : state.bents.get(Side.ATTACKER).getAttackElements()) {
      damage = state.bents.get(Side.DEFENDER).applyElementalResistanceAndImmunity(damage, attackElement);
    }

    return damage;
  }

  public static Operation<Integer, Integer> minimum(final int minimum) {
    return state -> Math.max(state.value(), minimum);
  }
}
