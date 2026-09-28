package legend.game.combat.spells;
/** Restricts targets by their relationship to the caster. */
public enum TargetSide {
    /** Only the caster. */
    SELF,

    /** Battle entities on the caster's side. */
    ALLIES,

    /** Battle entities on the opposing side. */
    ENEMIES,

    /** Battle entities on either side, including the caster. */
    ANY,
}
