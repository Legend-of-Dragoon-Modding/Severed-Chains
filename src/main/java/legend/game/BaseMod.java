package legend.game;

import com.github.slugify.Slugify;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.modding.Mod;
import legend.game.modding.events.EventListener;
import legend.game.modding.registries.RegistryId;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistryEvent;
import legend.game.types.EquipmentStats1c;
import legend.game.types.ItemStats0c;

import static legend.game.SItem.equipmentStats_80111ff0;
import static legend.game.SItem.equipment_8011972c;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;

@Mod(id = BaseMod.MOD_ID)
@EventListener
public class BaseMod {
  public static final String MOD_ID = "lod";

  public static final InventorySizeConfigEntry INVENTORY_SIZE_CONFIG = new InventorySizeConfigEntry();

  private static final Slugify slug = Slugify.builder().underscoreSeparator(true).customReplacement("'", "").build();

  public static RegistryId id(final String entryId) {
    return new RegistryId(MOD_ID, entryId);
  }

  @EventListener
  public static void registerItems(final ItemRegistryEvent event) {
    for(int itemId = 0; itemId < itemStats_8004f2ac.length(); itemId++) {
      if(itemId == 0) {
        continue;
      }

      final String name = equipment_8011972c.get(itemId + 0xc0).deref().get();
      final ItemStats0c itemStats = itemStats_8004f2ac.get(itemId);

      event.register(new Item(id(slug.slugify(name)), name, itemStats));
    }
  }

  @EventListener
  public static void registerEquipment(final EquipmentRegistryEvent event) {
    for(int equipmentId = 0; equipmentId < Math.min(158, equipmentStats_80111ff0.length()); equipmentId++) {
      final String name = equipment_8011972c.get(equipmentId).deref().get();

      if(!name.isEmpty()) {
        final EquipmentStats1c equipmentStats = equipmentStats_80111ff0.get(equipmentId);
        event.register(new Equipment(id(slug.slugify(name)), name, equipmentStats));
      }
    }
  }

  @EventListener
  public static void registerConfig(final ConfigRegistryEvent event) {
    event.register(INVENTORY_SIZE_CONFIG);
  }

  private static class InventorySizeConfigEntry extends ConfigEntry<Integer> {
    public InventorySizeConfigEntry() {
      super(id("inventory_size"), 32, InventorySizeConfigEntry::validator, InventorySizeConfigEntry::serializer, InventorySizeConfigEntry::deserializer);
      this.setEditControl((number, gameState) -> {
        final NumberSpinner spinner = new NumberSpinner(number);
        spinner.setMin(1);
        spinner.setMax(9999);
        spinner.onChange(val -> gameState.setConfig(this, val));
        return spinner;
      });
    }

    private static boolean validator(final int val) {
      return val > 0 && val <= 9999;
    }

    private static byte[] serializer(final int val) {
      final byte[] data = new byte[4];
      MathHelper.set(data, 0, 4, val);
      return data;
    }

    private static int deserializer(final byte[] data) {
      if(data.length == 4) {
        return IoHelper.readInt(data, 0);
      }

      return 32;
    }
  }
}
