package legend.game.types;

import legend.game.characters.Element;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public class SpellStats0c {
  public final String name;
  public final String battleDescription;

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
  public final int buffType_0a;
  public final int _0b;

  public static SpellStats0c fromFile(final String name, final String battleDescription, final FileData data) {
    final int targetType_00 = data.readUByte(0x0);
    final int flags_01 = data.readUByte(0x1);
    final int specialEffect_02 = data.readUByte(0x2);
    final int damage_03 = data.readUByte(0x3);
    final int multi_04 = data.readUByte(0x4);
    final int accuracy_05 = data.readUByte(0x5);
    final int mp_06 = data.readUByte(0x6);
    final int statusChance_07 = data.readUByte(0x7);
    final RegistryDelegate<Element> element_08 = Element.fromFlag(data.readUByte(0x8));
    final int statusType_09 = data.readUByte(0x9);
    final int buffType_0a = data.readUByte(0xa);
    final int _0b = data.readUByte(0xb);

    return new SpellStats0c(name, battleDescription, targetType_00, flags_01, specialEffect_02, damage_03, multi_04, accuracy_05, mp_06, statusChance_07, element_08, statusType_09, buffType_0a, _0b);
  }

  public SpellStats0c() {
    this.name = "";
    this.battleDescription = "";
    this.targetType_00 = 0;
    this.flags_01 = 0;
    this.specialEffect_02 = 0;
    this.damageMultiplier_03 = 0;
    this.multi_04 = 0;
    this.accuracy_05 = 0;
    this.mp_06 = 0;
    this.statusChance_07 = 0;
    this.element_08 = LodMod.NO_ELEMENT;
    this.statusType_09 = 0;
    this.buffType_0a = 0;
    this._0b = 0;
  }

  public SpellStats0c(final String name, final String battleDescription, final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b) {
    this.name = name;
    this.battleDescription = battleDescription;
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
}
