package legend.game.combat.spells;
/** Defines how many eligible battle entities a spell plan targets. */
public enum TargetScope {
    /** One eligible target, preferring the entity selected by the player. */
    SINGLE,

    /** Every eligible target. */
    ALL,
}
