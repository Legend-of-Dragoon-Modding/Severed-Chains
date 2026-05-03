package legend.lodmod.spells;

import legend.game.characters.Element;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public class DragonSpell extends RetailSpell {
  private final int battleStage;

  public DragonSpell(final int targetType, final int flags, final int specialEffect, final int damage, final int multi, final int accuracy, final int mp, final int statusChance, final RegistryDelegate<Element> element, final int statusType, final int buffType, final int _0b, final int index, final int battleStage) {
    super(targetType, flags, specialEffect, damage, multi, accuracy, mp, statusChance, element, statusType, buffType, _0b, index);
    this.battleStage = battleStage;
  }

  @Override
  public int getBattleStage() {
    return this.battleStage;
  }
}
