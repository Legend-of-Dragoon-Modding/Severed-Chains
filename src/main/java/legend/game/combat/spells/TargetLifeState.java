package legend.game.combat.spells;
/** Restricts a spell plan to targets with a matching life state. */
public enum TargetLifeState {
    /** Targets whose current HP is greater than zero. */
    LIVING,

    /** Targets whose current HP is zero. */
    DEAD,

    /** Targets regardless of current HP. */
    ANY,
}
