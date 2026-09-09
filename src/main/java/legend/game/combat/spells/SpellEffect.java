package legend.game.combat.spells;
/**
 * Describes one immutable operation in a declarative {@link SpellEffectPlan}.
 *
 * <p>Mod listeners may compose the permitted effect records into a plan and return that plan
 * through a spell-stats replacement. Effects are interpreted only when the plan uses
 * {@link ExecutionMode#DECLARATIVE}.</p>
 */
public sealed interface SpellEffect permits DamageSpellEffect, HealHpSpellEffect, RestoreMpSpellEffect, RestoreSpSpellEffect, ReviveSpellEffect, CleanseSpellEffect, DrainHpSpellEffect, DrainMpSpellEffect, DrainSpSpellEffect, ApplyStatusSpellEffect, StatModifierSpellEffect, RegenHpSpellEffect, RegenMpSpellEffect, RegenSpSpellEffect { }
