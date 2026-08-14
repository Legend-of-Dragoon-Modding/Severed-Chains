package legend.game.combat.spells;
public sealed interface SpellEffect permits DamageSpellEffect, HealHpSpellEffect, RestoreMpSpellEffect, RestoreSpSpellEffect, ReviveSpellEffect, CleanseSpellEffect, DrainHpSpellEffect, DrainMpSpellEffect, DrainSpSpellEffect, ApplyStatusSpellEffect, StatModifierSpellEffect, RegenHpSpellEffect, RegenMpSpellEffect, RegenSpSpellEffect { }
