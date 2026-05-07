package legend.game.inventory;

import legend.game.characters.Element;
import legend.game.combat.Battle;
import legend.game.combat.effects.ScriptDeffEffect;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;
import legend.game.scripting.ScriptState;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class SpellStats0c extends RegistryEntry implements ScriptReadable {
  /**
   * <ul>
   *   <li>0x8 - attack all</li>
   * </ul>
   */
  public final int targetType_00;
  /**
   * <ul>
   *   <li>0x4 - either buff spell or always hit (or both)</li>
   * </ul>
   */
  public final int flags_01;
  public final int specialEffect_02;
  public final int damageMultiplier_03;
  public final int multi_04;
  public final int accuracy_05;
  public final int mp_06;
  public final int statusChance_07;
  /** TODO this can be turned back into a regular Element once spells are in a registry */
  public final RegistryDelegate<Element> element_08;
  public final int statusType_09;
  /**
   * <ul>
   *   <li>0x80 - def up</li>
   *   <li>0x40 - mdef up</li>
   *   <li>0x20 - atk up</li>
   *   <li>0x10 - matk up</li>
   *   <li>0x8 - def down</li>
   *   <li>0x4 - mdef down</li>
   *   <li>0x2 - atk down</li>
   *   <li>0x1 - matk down</li>
   * </ul>
   */
  public final int buffType_0a;
  public final int _0b;

  public SpellStats0c(final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b) {
    this.targetType_00 = targetType;
    this.flags_01 = flags;
    this.specialEffect_02 = specialEffect;
    this.damageMultiplier_03 = damage;
    this.multi_04 = multi;
    this.accuracy_05 = accuracy;
    this.mp_06 = mp;
    this.statusChance_07 = statusChance;
    this.element_08 = element;
    this.statusType_09 = statusType;
    this.buffType_0a = buffType;
    this._0b = _0b;
  }

  public abstract void loadDeff(final Battle battle, final ScriptState<? extends BattleObject> parent, final ScriptDeffEffect effect, final int flags, final int bentIndex, final int deffParam, final int entrypoint);

  /** A battle stage ID if this spell changes the battle stage, or -1 if it doesn't */
  public int getBattleStage() {
    return -1;
  }

  @Override
  public void read(final int index, final Param out) {
    switch(index) {
      // Switches battle stage
      case 0 -> out.set(this.getBattleStage());

      default -> ScriptReadable.super.read(index, out);
    }
  }
}
