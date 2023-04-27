package legend.game.combat.formula;

import legend.game.characters.Element;
import legend.game.combat.types.AttackType;

import static legend.game.combat.Bttl_800c.dragoonSpaceElement_800c6b64;
import static legend.game.combat.Bttl_800f.adjustDamageForPower;

public final class PhysicalDamageFormula {
  private PhysicalDamageFormula() { }

  public static int calculatePhysicalDamage(final State<Integer> state) {
    return state.bobjs.get(Side.ATTACKER).calculatePhysicalDamage(state.bobjs.get(Side.DEFENDER));
  }

  public static int applyElementalInteractions(final State<Integer> state) {
    int damage = state.value();

    final Element defendElement = state.bobjs.get(Side.DEFENDER).getElement();

    for(final Element attackElement : state.bobjs.get(Side.ATTACKER).getAttackElements()) {
      damage = attackElement.adjustAttackingElementalDamage(AttackType.PHYSICAL, damage, defendElement);
      damage = defendElement.adjustDefendingElementalDamage(AttackType.PHYSICAL, damage, attackElement);
    }

    return damage;
  }

  public static int applyPower(final State<Integer> state) {
    return adjustDamageForPower(state.value(), state.bobjs.get(Side.ATTACKER).powerAttack_b4, state.bobjs.get(Side.DEFENDER).powerDefence_b8);
  }

  public static int applyDragoonSpace(final State<Integer> state) {
    int damage = state.value();

    if(dragoonSpaceElement_800c6b64 != null) {
      for(final Element attackElement : state.bobjs.get(Side.ATTACKER).getAttackElements()) {
        damage = dragoonSpaceElement_800c6b64.adjustDragoonSpaceDamage(AttackType.PHYSICAL, damage, attackElement);
      }
    }

    return damage;
  }

  public static int applyDamageMultipliers(final State<Integer> state) {
    return state.bobjs.get(Side.ATTACKER).applyPhysicalDamageMultipliers(state.value());
  }

  public static int applyAttackEffects(final State<Integer> state) {
    state.bobjs.get(Side.ATTACKER).applyAttackEffects();
    return state.value();
  }

  public static int applyResistanceAndImmunity(final State<Integer> state) {
    return state.bobjs.get(Side.DEFENDER).applyDamageResistanceAndImmunity(state.value(), AttackType.PHYSICAL);
  }

  public static int applyElementalResistanceAndImmunity(final State<Integer> state) {
    int damage = state.value();

    for(final Element attackElement : state.bobjs.get(Side.ATTACKER).getAttackElements()) {
      damage = state.bobjs.get(Side.DEFENDER).applyElementalResistanceAndImmunity(damage, attackElement);
    }

    return damage;
  }

  public static Operation<Integer, Integer> minimum(final int minimum) {
    return state -> Math.max(state.value(), minimum);
  }
}
