package legend.game.combat.spells;
/** Defines which runtime interprets a {@link SpellEffectPlan}. */
public enum ExecutionMode {
    /** Uses the spell's retail fields and script behavior; declarative effects are not executed. */
    LEGACY,

    /** Executes the plan's {@link SpellEffect} values through the declarative spell runtime. */
    DECLARATIVE,

    /**
     * Uses legacy runtime behavior for spell stats that no longer represent retail-safe values.
     *
     * <p>This currently executes through the same legacy path as {@link #LEGACY}; the distinct
     * value preserves the stats' raw provenance for consumers.</p>
     */
    LEGACY_RAW,
}
