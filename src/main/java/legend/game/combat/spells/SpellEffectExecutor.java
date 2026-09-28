package legend.game.combat.spells;

import legend.game.characters.StatType;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.lodmod.LodMod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Resolves and applies {@link ExecutionMode#DECLARATIVE declarative} spell-effect plans.
 *
 * <p>Mod consumers normally construct {@link SpellEffectPlan} values and attach them to spell
 * stats; the battle runtime invokes this executor when that spell is cast.</p>
 */
public final class SpellEffectExecutor {
    /**
     * Resolves the eligible targets and damage for every declarative plan in one cast.
     *
     * <p>Plans using a different execution mode are ignored. The returned list is immutable and
     * contains one entry for each matched target and plan.</p>
     *
     * @param caster player casting the spell
     * @param selectedTarget target selected for the cast
     * @param battleEntities entities available for target matching
     * @param plans effect plans attached to the active spell
     * @param damageCalculator callback that calculates damage for a target and combined power
     * @return immutable prepared target/effect entries for declarative plans
     */
    public List<TargetedSpellEffects> prepare(final PlayerBattleEntity caster, final BattleEntity27c selectedTarget, final List<? extends BattleEntity27c> battleEntities, final List<SpellEffectPlan> plans, final DamageCalculator damageCalculator) {
    final List<TargetedSpellEffects> prepared = new ArrayList<>();
    for(final SpellEffectPlan plan : plans) {
      if(plan.executionMode() != ExecutionMode.DECLARATIVE) {
        continue;
      }

      for(final BattleEntity27c target : this.targets(caster, selectedTarget, battleEntities, plan.target())) {
        prepared.add(new TargetedSpellEffects(target, this.prepareTarget(target, plan, damageCalculator)));
      }
    }

    return List.copyOf(prepared);
  }

  private PreparedSpellEffects prepareTarget(final BattleEntity27c target, final SpellEffectPlan plan, final DamageCalculator damageCalculator) {
    final int damagePower = plan.effects().stream().mapToInt(effect -> effect instanceof DamageSpellEffect damage ? damage.power() : 0).sum();
    final int damage = damagePower == 0 ? 0 : damageCalculator.calculate(target, damagePower);
    return new PreparedSpellEffects(plan, max(0, damage));
  }

  private List<? extends BattleEntity27c> targets(final PlayerBattleEntity caster, final BattleEntity27c selectedTarget, final List<? extends BattleEntity27c> battleEntities, final SpellTargetProfile profile) {
    if(profile.scope() == TargetScope.ALL) {
      return battleEntities.stream().filter(target -> this.matchesTarget(caster, target, profile)).toList();
    }

    if(this.matchesTarget(caster, selectedTarget, profile)) {
      return List.of(selectedTarget);
    }
    for(final BattleEntity27c target : battleEntities) {
      if(this.matchesTarget(caster, target, profile)) {
        return List.of(target);
      }
    }

    return List.of();
  }

    /**
     * Applies one prepared plan after its damage result is known.
     *
     * @param caster player casting the spell
     * @param target target receiving this plan's effects
     * @param prepared plan and non-negative prepared damage for the target
     * @param random random source used for status-chance rolls
     * @param appliedVitalLoss HP loss actually applied to the target, used by drain effects
     * @return selected status bit when a status roll succeeds, otherwise {@code -1}
     */
    public int complete(final PlayerBattleEntity caster, final BattleEntity27c target, final PreparedSpellEffects prepared, final Random random, final int appliedVitalLoss) {
    final int targetHp = target.stats.getStat(LodMod.HP_STAT.get()).getCurrent();
    final boolean targetWasDead = targetHp == 0;
    final int vitalLoss = min(max(0, appliedVitalLoss), targetHp);
    final List<SpellEffect> effects = prepared.plan().effects().stream().sorted(Comparator.comparing(this::effectPhase)).toList();
    int statusEffect = -1;

    for(final SpellEffect effect : effects) {
      switch(effect) {
        case ReviveSpellEffect revive -> this.revive(target, revive);
        case CleanseSpellEffect cleanse -> target.status_0e &= ~cleanse.statusMask();
        case HealHpSpellEffect heal -> {
          // Retail revive/recovery packages revive dead targets and recover living targets.
          if(!targetWasDead) {
            this.restore(target, LodMod.HP_STAT.get(), heal.potency(), heal.percentage());
          }
        }
        case RestoreMpSpellEffect restore -> {
          if(target instanceof PlayerBattleEntity) {
            this.restore(target, LodMod.MP_STAT.get(), restore.potency(), restore.percentage());
          }
        }
        case RestoreSpSpellEffect restore -> {
          if(target instanceof PlayerBattleEntity) {
            this.restore(target, LodMod.SP_STAT.get(), restore.potency(), restore.percentage());
          }
        }
        case DamageSpellEffect ignored -> { }
        case DrainHpSpellEffect drain -> this.restore(caster, LodMod.HP_STAT.get(), vitalLoss * drain.percent() / 100, false);
        case DrainMpSpellEffect drain -> this.restore(caster, LodMod.MP_STAT.get(), vitalLoss * drain.percent() / 100, false);
        case DrainSpSpellEffect drain -> this.restore(caster, LodMod.SP_STAT.get(), vitalLoss * drain.percent() / 100, false);
        case ApplyStatusSpellEffect status -> {
          if(statusEffect == -1 && random.nextInt(100) < status.chance()) {
            statusEffect = Integer.highestOneBit(status.statusMask() & 0xff);
          }
        }
        case StatModifierSpellEffect modifier -> this.applyStatModifier(target, modifier);
        case RegenHpSpellEffect regen -> target.spellRegen.hp.set(regen.potency(), regen.turns(), regen.percentage());
        case RegenMpSpellEffect regen -> target.spellRegen.mp.set(regen.potency(), regen.turns(), regen.percentage());
        case RegenSpSpellEffect regen -> target.spellRegen.sp.set(regen.potency(), regen.turns(), regen.percentage());
      }
    }

    return statusEffect;
  }

  private EffectPhase effectPhase(final SpellEffect effect) {
    return switch(effect) {
      case ReviveSpellEffect ignored -> EffectPhase.REVIVE;
      case CleanseSpellEffect ignored -> EffectPhase.CLEANSE;
      case HealHpSpellEffect ignored -> EffectPhase.RESTORE;
      case RestoreMpSpellEffect ignored -> EffectPhase.RESTORE;
      case RestoreSpSpellEffect ignored -> EffectPhase.RESTORE;
      case DamageSpellEffect ignored -> EffectPhase.DAMAGE;
      case DrainHpSpellEffect ignored -> EffectPhase.DRAIN;
      case DrainMpSpellEffect ignored -> EffectPhase.DRAIN;
      case DrainSpSpellEffect ignored -> EffectPhase.DRAIN;
      case ApplyStatusSpellEffect ignored -> EffectPhase.STATUS;
      case StatModifierSpellEffect ignored -> EffectPhase.STAT_MODIFIER;
      case RegenHpSpellEffect ignored -> EffectPhase.REGEN;
      case RegenMpSpellEffect ignored -> EffectPhase.REGEN;
      case RegenSpSpellEffect ignored -> EffectPhase.REGEN;
    };
  }

  private void revive(final BattleEntity27c target, final ReviveSpellEffect revive) {
    final VitalsStat hp = target.stats.getStat(LodMod.HP_STAT.get());
    if(hp.getCurrent() == 0) {
      hp.setCurrent(max(1, hp.getMax() * revive.hpPercent() / 100));
    }
  }

  private void restore(final BattleEntity27c target, final StatType<VitalsStat> statType, final int potency, final boolean percentage) {
    final VitalsStat stat = target.stats.getStat(statType);
    final int amount = percentage ? stat.getMax() * potency / 100 : potency;
    stat.setCurrent(min(stat.getMax(), stat.getCurrent() + max(0, amount)));
  }

  private void applyStatModifier(final BattleEntity27c target, final StatModifierSpellEffect modifier) {
    switch(modifier.stat()) {
      case ATTACK -> { target.powerAttack_b4 = modifier.amount(); target.powerAttackTurns_b5 = modifier.turns(); }
      case MAGIC_ATTACK -> { target.powerMagicAttack_b6 = modifier.amount(); target.powerMagicAttackTurns_b7 = modifier.turns(); }
      case DEFENCE -> { target.powerDefence_b8 = modifier.amount(); target.powerDefenceTurns_b9 = modifier.turns(); }
      case MAGIC_DEFENCE -> { target.powerMagicDefence_ba = modifier.amount(); target.powerMagicDefenceTurns_bb = modifier.turns(); }
    }
  }

  private boolean matchesTarget(final PlayerBattleEntity caster, final BattleEntity27c target, final SpellTargetProfile profile) {
    final boolean targetIsPlayer = target instanceof PlayerBattleEntity;
    final boolean validSide = switch(profile.side()) {
      case SELF -> target == caster;
      case ALLIES -> targetIsPlayer;
      case ENEMIES -> !targetIsPlayer;
      case ANY -> true;
    };
    if(!validSide) {
      return false;
    }

    final boolean targetIsLiving = target.stats.getStat(LodMod.HP_STAT.get()).getCurrent() > 0;
    final boolean validLifeState = switch(profile.lifeState()) {
      case LIVING -> targetIsLiving;
      case DEAD -> !targetIsLiving;
      case ANY -> true;
    };
    return validLifeState;
  }

    /**
     * Associates one matched target with the effects prepared for it.
     *
     * @param target matched battle entity
     * @param effects plan and damage prepared for that target
     */
    public record TargetedSpellEffects(BattleEntity27c target, PreparedSpellEffects effects) {
    public TargetedSpellEffects {
      if(target == null) {
        throw new IllegalArgumentException("Prepared spell target cannot be null");
      }
      if(effects == null) {
        throw new IllegalArgumentException("Prepared spell effects cannot be null");
      }
    }
  }

  private enum EffectPhase {
    REVIVE,
    CLEANSE,
    RESTORE,
    DAMAGE,
    DRAIN,
    STATUS,
    STAT_MODIFIER,
    REGEN,
    }

    /** Calculates spell damage for one target and combined declarative power. */
    @FunctionalInterface
    public interface DamageCalculator {
        /**
         * @param target target whose damage is being calculated
         * @param power sum of all {@link DamageSpellEffect#power()} values in the plan
         * @return calculated damage before the plan is completed
         */
        int calculate(BattleEntity27c target, int power);
    }

    /**
     * Immutable plan data prepared for one target before effects are applied.
     *
     * @param plan declarative plan being applied
     * @param damage non-negative calculated damage for the target
     */
    public record PreparedSpellEffects(SpellEffectPlan plan, int damage) {
    public PreparedSpellEffects {
      if(plan == null) {
        throw new IllegalArgumentException("Prepared spell effect plan cannot be null");
      }

      if(damage < 0) {
        throw new IllegalArgumentException("Prepared spell damage cannot be negative");
      }
    }
    }

    /** Holds the replaceable HP, MP, and SP regeneration currently active on one battle entity. */
    public static final class RegenState {
        /** Current HP regeneration. */
        public final Regen hp = new Regen();

        /** Current MP regeneration. */
        public final Regen mp = new Regen();

        /** Current SP regeneration. */
        public final Regen sp = new Regen();

        /**
         * Applies one regeneration tick after the target completes a turn.
         *
         * <p>All targets receive HP regeneration; only player targets receive MP and SP
         * regeneration.</p>
         *
         * @param target battle entity whose turn completed
         */
        public void turnFinished(final BattleEntity27c target) {
      this.hp.tick(target, LodMod.HP_STAT.get());
      if(target instanceof PlayerBattleEntity) {
        this.mp.tick(target, LodMod.MP_STAT.get());
        this.sp.tick(target, LodMod.SP_STAT.get());
      }
    }
    }

    /** Mutable state for one renewable vital, replaced when a new regeneration effect is applied. */
    public static final class Regen {
    private int potency;
    private int turns;
    private boolean percentage;

        /**
         * Replaces the active regeneration values.
         *
         * <p>Negative potency and turn values are stored as zero.</p>
         *
         * @param potency flat amount restored per turn, or percent of the maximum when
         *                {@code percentage} is true
         * @param turns number of remaining turn-completion ticks
         * @param percentage whether {@code potency} is a percentage of the vital's maximum
         */
        public void set(final int potency, final int turns, final boolean percentage) {
      this.potency = max(0, potency);
      this.turns = max(0, turns);
      this.percentage = percentage;
    }

    private void tick(final BattleEntity27c target, final StatType<VitalsStat> statType) {
      if(this.turns == 0) {
        return;
      }

      final VitalsStat stat = target.stats.getStat(statType);
      final int amount = this.percentage ? stat.getMax() * this.potency / 100 : this.potency;
      stat.setCurrent(min(stat.getMax(), stat.getCurrent() + amount));
      this.turns--;
    }
  }
}
