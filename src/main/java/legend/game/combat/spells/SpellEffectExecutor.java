package legend.game.combat.spells;

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

public final class SpellEffectExecutor {
  public List<TargetedSpellEffects> prepare(final PlayerBattleEntity caster, final BattleEntity27c selectedTarget, final List<? extends BattleEntity27c> battleEntities, final List<SpellEffectPlan> plans, final DamageCalculator damageCalculator) {
    final List<TargetedSpellEffects> prepared = new ArrayList<>();
    for(final SpellEffectPlan plan : plans) {
      if(plan.executionMode() != ExecutionMode.DECLARATIVE) {
        continue;
      }

      if(plan.target().scope() == TargetScope.SINGLE) {
        final BattleEntity27c target = this.singleTarget(caster, selectedTarget, battleEntities, plan.target());
        if(target != null) {
          prepared.add(new TargetedSpellEffects(target, this.prepare(target, plan, damageCalculator)));
        }
        continue;
      }

      for(final BattleEntity27c target : battleEntities) {
        if(this.matchesTarget(caster, target, plan.target())) {
          prepared.add(new TargetedSpellEffects(target, this.prepare(target, plan, damageCalculator)));
        }
      }
    }

    return List.copyOf(prepared);
  }

  private PreparedSpellEffects prepare(final BattleEntity27c target, final SpellEffectPlan plan, final DamageCalculator damageCalculator) {
    int damagePower = 0;
    for(final SpellEffect effect : plan.effects()) {
      if(effect instanceof DamageSpellEffect damageEffect) {
        damagePower += damageEffect.power();
      }
    }

    final int damage = damagePower == 0 ? 0 : damageCalculator.calculate(target, damagePower);
    return new PreparedSpellEffects(plan, max(0, damage));
  }

  private BattleEntity27c singleTarget(final PlayerBattleEntity caster, final BattleEntity27c selectedTarget, final List<? extends BattleEntity27c> battleEntities, final SpellTargetProfile profile) {
    if(this.matchesTarget(caster, selectedTarget, profile)) {
      return selectedTarget;
    }

    for(final BattleEntity27c target : battleEntities) {
      if(this.matchesTarget(caster, target, profile)) {
        return target;
      }
    }

    return null;
  }

  public int complete(final PlayerBattleEntity caster, final BattleEntity27c target, final PreparedSpellEffects prepared, final Random random, final int appliedVitalLoss) {
    final SpellEffectPlan plan = prepared.plan();
    final boolean targetWasDead = target.stats.getStat(LodMod.HP_STAT.get()).getCurrent() == 0;
    final List<SpellEffect> effects = new ArrayList<>(plan.effects());
    effects.sort(Comparator.comparing(this::effectPhase));
    final int vitalLoss = min(max(0, appliedVitalLoss), target.stats.getStat(LodMod.HP_STAT.get()).getCurrent());
    int statusEffect = -1;

    for(final SpellEffect effect : effects) {
      if(effect instanceof ReviveSpellEffect revive) {
        this.revive(target, revive);
      } else if(effect instanceof CleanseSpellEffect cleanse) {
        target.status_0e &= ~cleanse.statusMask();
      } else if(effect instanceof HealHpSpellEffect heal) {
        // Retail revive/recovery packages revive dead targets and recover living targets.
        if(!targetWasDead) {
          this.restore(target, LodMod.HP_STAT.get(), heal.potency(), heal.percentage());
        }
      } else if(effect instanceof RestoreMpSpellEffect restore && target instanceof PlayerBattleEntity) {
        this.restore(target, LodMod.MP_STAT.get(), restore.potency(), restore.percentage());
      } else if(effect instanceof RestoreSpSpellEffect restore && target instanceof PlayerBattleEntity) {
        this.restore(target, LodMod.SP_STAT.get(), restore.potency(), restore.percentage());
      } else if(effect instanceof DrainHpSpellEffect drain) {
        this.restore(caster, LodMod.HP_STAT.get(), vitalLoss * drain.percent() / 100, false);
      } else if(effect instanceof DrainMpSpellEffect drain) {
        this.restore(caster, LodMod.MP_STAT.get(), vitalLoss * drain.percent() / 100, false);
      } else if(effect instanceof DrainSpSpellEffect drain) {
        this.restore(caster, LodMod.SP_STAT.get(), vitalLoss * drain.percent() / 100, false);
      } else if(effect instanceof ApplyStatusSpellEffect status && statusEffect == -1 && random.nextInt(100) < status.chance()) {
        statusEffect = Integer.highestOneBit(status.statusMask() & 0xff);
      } else if(effect instanceof StatModifierSpellEffect modifier) {
        this.applyStatModifier(target, modifier);
      } else if(effect instanceof RegenHpSpellEffect regen) {
        target.spellRegen.hp.set(regen.potency(), regen.turns(), regen.percentage());
      } else if(effect instanceof RegenMpSpellEffect regen) {
        target.spellRegen.mp.set(regen.potency(), regen.turns(), regen.percentage());
      } else if(effect instanceof RegenSpSpellEffect regen) {
        target.spellRegen.sp.set(regen.potency(), regen.turns(), regen.percentage());
      }
    }

    return statusEffect;
  }

  private EffectPhase effectPhase(final SpellEffect effect) {
    if(effect instanceof ReviveSpellEffect) {
      return EffectPhase.REVIVE;
    }
    if(effect instanceof CleanseSpellEffect) {
      return EffectPhase.CLEANSE;
    }
    if(effect instanceof HealHpSpellEffect || effect instanceof RestoreMpSpellEffect || effect instanceof RestoreSpSpellEffect) {
      return EffectPhase.RESTORE;
    }
    if(effect instanceof DamageSpellEffect) {
      return EffectPhase.DAMAGE;
    }
    if(effect instanceof DrainHpSpellEffect || effect instanceof DrainMpSpellEffect || effect instanceof DrainSpSpellEffect) {
      return EffectPhase.DRAIN;
    }
    if(effect instanceof ApplyStatusSpellEffect) {
      return EffectPhase.STATUS;
    }
    if(effect instanceof StatModifierSpellEffect) {
      return EffectPhase.STAT_MODIFIER;
    }
    if(effect instanceof RegenHpSpellEffect || effect instanceof RegenMpSpellEffect || effect instanceof RegenSpSpellEffect) {
      return EffectPhase.REGEN;
    }

    throw new IllegalArgumentException("Unsupported spell effect " + effect.getClass().getName());
  }

  private void revive(final BattleEntity27c target, final ReviveSpellEffect revive) {
    final VitalsStat hp = target.stats.getStat(LodMod.HP_STAT.get());
    if(hp.getCurrent() == 0) {
      hp.setCurrent(max(1, hp.getMax() * revive.hpPercent() / 100));
    }
  }

  private void restore(final BattleEntity27c target, final Object statType, final int potency, final boolean percentage) {
    @SuppressWarnings("unchecked") final VitalsStat stat = target.stats.getStat((legend.game.characters.StatType<VitalsStat>)statType);
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

  @FunctionalInterface
  public interface DamageCalculator {
    int calculate(BattleEntity27c target, int power);
  }

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

  public static final class RegenState {
    public final Regen hp = new Regen();
    public final Regen mp = new Regen();
    public final Regen sp = new Regen();

    public void turnFinished(final BattleEntity27c target) {
      this.hp.tick(target, LodMod.HP_STAT.get());
      if(target instanceof PlayerBattleEntity) {
        this.mp.tick(target, LodMod.MP_STAT.get());
        this.sp.tick(target, LodMod.SP_STAT.get());
      }
    }
  }

  public static final class Regen {
    private int potency;
    private int turns;
    private boolean percentage;

    public void set(final int potency, final int turns, final boolean percentage) {
      this.potency = max(0, potency);
      this.turns = max(0, turns);
      this.percentage = percentage;
    }

    private void tick(final BattleEntity27c target, final Object statType) {
      if(this.turns == 0) {
        return;
      }

      @SuppressWarnings("unchecked") final VitalsStat stat = target.stats.getStat((legend.game.characters.StatType<VitalsStat>)statType);
      final int amount = this.percentage ? stat.getMax() * this.potency / 100 : this.potency;
      stat.setCurrent(min(stat.getMax(), stat.getCurrent() + amount));
      this.turns--;
    }
  }
}
