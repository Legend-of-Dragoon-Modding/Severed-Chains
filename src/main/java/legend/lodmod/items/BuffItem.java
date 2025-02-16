package legend.lodmod.items;

import legend.game.characters.Element;
import legend.game.characters.UnaryStatModConfig;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;
import legend.lodmod.LodMod;

public class BuffItem extends BattleItem {
  private final int useItemEntrypoint;
  private final TargetType target;
  private final int powerDefence;
  private final int powerMagicDefence;
  private final int powerAttack;
  private final int powerMagicAttack;
  private final int powerAttackHit;
  private final int powerMagicAttackHit;
  private final int powerAttackAvoid;
  private final int powerMagicAttackAvoid;
  private final boolean physicalImmunity;
  private final boolean magicalImmunity;
  private final int speedUp;
  private final int speedDown;
  private final int spPerPhysicalHit;
  private final int mpPerPhysicalHit;
  private final int spPerMagicalHit;
  private final int mpPerMagicalHit;

  public BuffItem(final int useItemEntrypoint, final ItemIcon icon, final int price, final TargetType target, final int powerDefence, final int powerMagicDefence, final int powerAttack, final int powerMagicAttack, final int powerAttackHit, final int powerMagicAttackHit, final int powerAttackAvoid, final int powerMagicAttackAvoid, final boolean physicalImmunity, final boolean magicalImmunity, final int speedUp, final int speedDown, final int spPerPhysicalHit, final int mpPerPhysicalHit, final int spPerMagicalHit, final int mpPerMagicalHit) {
    super(icon, price);
    this.useItemEntrypoint = useItemEntrypoint;
    this.target = target;
    this.powerDefence = powerDefence;
    this.powerMagicDefence = powerMagicDefence;
    this.powerAttack = powerAttack;
    this.powerMagicAttack = powerMagicAttack;
    this.powerAttackHit = powerAttackHit;
    this.powerMagicAttackHit = powerMagicAttackHit;
    this.powerAttackAvoid = powerAttackAvoid;
    this.powerMagicAttackAvoid = powerMagicAttackAvoid;
    this.physicalImmunity = physicalImmunity;
    this.magicalImmunity = magicalImmunity;
    this.speedUp = speedUp;
    this.speedDown = speedDown;
    this.spPerPhysicalHit = spPerPhysicalHit;
    this.mpPerPhysicalHit = mpPerPhysicalHit;
    this.spPerMagicalHit = spPerMagicalHit;
    this.mpPerMagicalHit = mpPerMagicalHit;
  }

  @Override
  public boolean isRepeat() {
    return true;
  }

  @Override
  public boolean isProtected() {
    return true;
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == this.target;
  }

  @Override
  public void applyBuffs(final BattleEntity27c user, final BattleEntity27c target) {
    final int turnCount = user != target ? 3 : 4;

    if(this.powerDefence != 0) {
      target.powerDefence_b8 = this.powerDefence;
      target.powerDefenceTurns_b9 = turnCount;
    }

    if(this.powerMagicDefence != 0) {
      target.powerMagicDefence_ba = this.powerMagicDefence;
      target.powerMagicDefenceTurns_bb = turnCount;
    }

    if(this.powerAttack != 0) {
      target.powerAttack_b4 = this.powerAttack;
      target.powerAttackTurns_b5 = turnCount;
    }

    if(this.powerMagicAttack != 0) {
      target.powerMagicAttack_b6 = this.powerMagicAttack;
      target.powerMagicAttackTurns_b7 = turnCount;
    }

    if(this.powerAttackHit != 0) {
      target.tempAttackHit_bc = this.powerAttackHit;
      target.tempAttackHitTurns_bd = turnCount;
    }

    if(this.powerMagicAttackHit != 0) {
      target.tempMagicHit_be = this.powerMagicAttackHit;
      target.tempMagicHitTurns_bf = turnCount;
    }

    if(this.powerAttackAvoid != 0) {
      target.tempAttackAvoid_c0 = this.powerAttackAvoid;
      target.tempAttackAvoidTurns_c1 = turnCount;
    }

    if(this.powerMagicAttackAvoid != 0) {
      target.tempMagicAvoid_c2 = this.powerMagicAttackAvoid;
      target.tempMagicAvoidTurns_c3 = turnCount;
    }

    if(this.physicalImmunity) {
      target.tempPhysicalImmunity_c4 = 1;
      target.tempPhysicalImmunityTurns_c5 = turnCount;
    }

    if(this.magicalImmunity) {
      target.tempMagicalImmunity_c6 = 1;
      target.tempMagicalImmunityTurns_c7 = turnCount;
    }

    if(this.speedDown != 0) {
      target.stats.getStat(LodMod.SPEED_STAT.get()).addMod(LodMod.id("speed_down"), LodMod.UNARY_STAT_MOD_TYPE.get().make(new UnaryStatModConfig().percent(this.speedDown).turns(turnCount)));
    }

    if(this.speedUp != 0) {
      target.stats.getStat(LodMod.SPEED_STAT.get()).addMod(LodMod.id("speed_up"), LodMod.UNARY_STAT_MOD_TYPE.get().make(new UnaryStatModConfig().percent(this.speedUp).turns(turnCount)));
    }

    if(target instanceof final PlayerBattleEntity playerTarget) {
      if(this.spPerPhysicalHit != 0) {
        playerTarget.tempSpPerPhysicalHit_cc = this.spPerPhysicalHit;
        playerTarget.tempSpPerPhysicalHitTurns_cd = turnCount;
      }

      if(this.mpPerPhysicalHit != 0) {
        playerTarget.tempMpPerPhysicalHit_ce = this.mpPerPhysicalHit;
        playerTarget.tempMpPerPhysicalHitTurns_cf = turnCount;
      }

      if(this.spPerMagicalHit != 0) {
        playerTarget.tempSpPerMagicalHit_d0 = this.spPerMagicalHit;
        playerTarget.tempSpPerMagicalHitTurns_d1 = turnCount;
      }

      if(this.mpPerMagicalHit != 0) {
        playerTarget.tempMpPerMagicalHit_d2 = this.mpPerMagicalHit;
        playerTarget.tempMpPerMagicalHitTurns_d3 = turnCount;
      }
    }
  }

  @Override
  public Element getAttackElement() {
    return LodMod.NO_ELEMENT.get();
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return this.useItemEntrypoint;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[8] = 0xffffff; // Colour
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
