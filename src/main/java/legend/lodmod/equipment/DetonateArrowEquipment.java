package legend.lodmod.equipment;

import legend.game.characters.ElementSet;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.effects.ScriptDeffManualLoadingEffect;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentAttackType;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.scripting.ScriptState;
import legend.game.types.EquipmentSlot;
import legend.lodmod.LodItems;
import legend.lodmod.LodMod;

import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;

public class DetonateArrowEquipment extends Equipment {
  public DetonateArrowEquipment(final int price) {
    super(price, 0x8, EquipmentSlot.WEAPON, 0x80, 0x2, LodMod.NO_ELEMENT.get(), 0, new ElementSet(), new ElementSet(), 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, false, false, false, false, 0, 0, 0, 0, 0, ItemIcon.BOW, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0);
  }

  @Override
  public void prepareAttack(final ScriptState<PlayerBattleEntity> player) {
    battleState_8006e398._560 = 0;
    battleState_8006e398._564 = 0;

    player.innerStruct_00.item_d4 = new ItemStack(LodItems.DETONATE_ROCK.get());
    player.innerStruct_00.battle.loadSpellItemDeff(player, 0xc1, 0, player.index, -1, 1, new ScriptDeffManualLoadingEffect());
  }

  @Override
  public EquipmentAttackType attack(final ScriptState<PlayerBattleEntity> player) {
    return EquipmentAttackType.DEFF;
  }
}
