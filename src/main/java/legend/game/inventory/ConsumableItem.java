package legend.game.inventory;

import legend.game.combat.types.BattleObject27c;
import legend.game.modding.registries.RegistryId;
import legend.game.types.ItemStats0c;

import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SMap.encounterAccumulator_800c6ae8;
import static legend.game.Scus94491BpeSegment_8002.addHp;
import static legend.game.Scus94491BpeSegment_8002.addMp;
import static legend.game.Scus94491BpeSegment_8002.addSp;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.hasNoEncounters_800bed58;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;

public class ConsumableItem extends Item {
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

  public ConsumableItem(final RegistryId id, final String name, final String description, final int price, final ItemStats0c stats) {
    this(
      id,
      name,
      description,
      price,
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

  public ConsumableItem(final RegistryId id, final String name, final String description, final int price, final int target, final int element, final int damage, final int special1, final int special2, final int damage2, final int specialAmount, final int icon, final int status, final int percentage, final int uu2, final int type) {
    super(id, name, description, price);
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

  @Override
  public int getIcon() {
    return this.icon;
  }

  @Override
  public int getUseFlags() {
    if((this.target & 0x10) == 0) {
      return 0;
    }

    return this.target & 0x12;
  }

  public boolean canBeUsedNow() {
    if(this.getUseFlags() == 0) {
      return false;
    }

    if(this.type == 0x8) {
      int allStatus = 0;
      for(int i = 0; i < characterCount_8011d7c4.get(); i++) {
        allStatus |= gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(i).get()).status_10.get();
      }

      return (this.status & allStatus) != 0;
    }

    return true;
  }

  @Override
  public void use(final UseItemResponse response, final int charId) {
    if(this == Items.CHARM_POTION.get()) {
      if(mainCallbackIndex_8004dd20.get() == 0x8L || hasNoEncounters_800bed58.get() == 0) {
        response._00 = 8;
        encounterAccumulator_800c6ae8.setu(0);
      } else {
        response._00 = 9;
      }

      return;
    }

    if((this.type & 0x80) != 0) {
      response._00 = (this.target & 0x2) == 0 ? 2 : 3;

      final int amount;
      if(this.percentage == 100) {
        amount = -1;
      } else {
        amount = stats_800be5f8.get(charId).maxHp_66.get() * this.percentage / 100;
      }

      response.value_04 = addHp(charId, amount);
    }

    if((this.type & 0x40) != 0) {
      response._00 = (this.target & 0x2) == 0 ? 4 : 5;

      final int amount;
      if(this.percentage == 100) {
        amount = -1;
      } else {
        amount = stats_800be5f8.get(charId).maxMp_6e.get() * this.percentage / 100;
      }

      response.value_04 = addMp(charId, amount);
    }

    if((this.type & 0x20) != 0) {
      response._00 = 6;

      final int amount;
      if(this.percentage == 100) {
        amount = -1;
      } else {
        amount = this.percentage;
      }

      response.value_04 = addSp(charId, amount);
    }

    if((this.type & 0x8) != 0) {
      final int status = gameState_800babc8.charData_32c.get(charId).status_10.get();

      if((this.status & status) != 0) {
        response.value_04 = status;
        gameState_800babc8.charData_32c.get(charId).status_10.and(~status);
      }

      response._00 = 7;
    }
  }

  @Override
  public void applyAttackItemStats(final BattleObject27c bobj) {
    bobj.itemTarget_d4.set((short)this.target);
    bobj.itemElement_d6.set((short)this.element);
    bobj.itemDamage_d8.set((short)this.damage);
    bobj.itemSpecial1_da.set((short)this.special1);
    bobj.itemSpecial2_dc.set((short)this.special2);
    bobj.itemDamage_de.set((short)this.damage2);
    bobj.itemSpecialAmount_e0.set(this.specialAmount);
    bobj.itemIcon_e2.set((short)this.icon);
    bobj.itemStatus_e4.set((short)this.status);
    bobj.itemPercentage_e6.set((short)this.percentage);
    bobj.itemUu2_e8.set((short)this.uu2);
    bobj.itemType_ea.set((short)this.type);
  }
}
