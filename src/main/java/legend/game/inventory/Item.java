package legend.game.inventory;

import legend.game.modding.registries.RegistryEntry;
import legend.game.types.ItemStats0c;

public class Item extends RegistryEntry {
  public final String name;
  public final int target;
  public final int element;
  public final int damage;
  public final int special1;
  public final int special2;
  public final int damage2;
  public final int specialAmount;
  public final int icon;
  public final int status;
  public final int percentage;
  public final int uu2;
  /**
   * <ul>
   *   <li>0x04 - cause status</li>
   *   <li>0x08 - cure status</li>
   *   <li>0x10 - revive</li>
   *   <li>0x20 - SP</li>
   *   <li>0x40 - MP</li>
   *   <li>0x80 - HP</li>
   * </ul>
   */
  public final int type;

  public Item(final String name, final ItemStats0c stats) {
    this(
      name,
      stats.target_00.get(),
      stats.element_01.get(),
      stats.damage_02.get(),
      stats.special1_03.get(),
      stats.special2_04.get(),
      stats.damage_02.get(),
      stats.specialAmount_06.get(),
      stats.icon_07.get(),
      stats.status_08.get(),
      stats.percentage_09.get(),
      stats.uu2_0a.get(),
      stats.type_0b.get()
    );
  }

  public Item(final String name, final int target, final int element, final int damage, final int special1, final int special2, final int damage2, final int specialAmount, final int icon, final int status, final int percentage, final int uu2, final int type) {
    this.name = name;
    this.target = target;
    this.element = element;
    this.damage = damage;
    this.special1 = special1;
    this.special2 = special2;
    this.damage2 = damage2;
    this.specialAmount = specialAmount;
    this.icon = icon;
    this.status = status;
    this.percentage = percentage;
    this.uu2 = uu2;
    this.type = type;
  }
}
