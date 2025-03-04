package legend.core;

import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActionRegistry;
import legend.core.platform.input.InputActionRegistryEvent;
import legend.game.characters.Element;
import legend.game.characters.ElementRegistry;
import legend.game.characters.ElementRegistryEvent;
import legend.game.characters.StatModType;
import legend.game.characters.StatModTypeRegistry;
import legend.game.characters.StatModTypeRegistryEvent;
import legend.game.characters.StatType;
import legend.game.characters.StatTypeRegistry;
import legend.game.characters.StatTypeRegistryEvent;
import legend.game.combat.bent.BattleEntityType;
import legend.game.combat.bent.BattleEntityTypeRegistry;
import legend.game.combat.bent.BattleEntityTypeRegistryEvent;
import legend.game.combat.deff.DeffPackage;
import legend.game.combat.deff.DeffRegistry;
import legend.game.combat.deff.RegisterDeffsEvent;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistry;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistry;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.ShopRegistry;
import legend.game.inventory.ShopRegistryEvent;
import legend.game.inventory.Spell;
import legend.game.inventory.SpellRegistry;
import legend.game.inventory.SpellRegistryEvent;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistry;
import legend.game.saves.ConfigRegistryEvent;
import legend.game.types.Shop;
import org.legendofdragoon.modloader.events.EventManager;
import org.legendofdragoon.modloader.registries.Registry;

import java.util.function.Consumer;

public class Registries extends org.legendofdragoon.modloader.registries.Registries {
  public final Registry<InputAction> inputActions = this.addRegistry(new InputActionRegistry(), InputActionRegistryEvent::new);
  public final Registry<StatType<?>> statTypes = this.addRegistry(new StatTypeRegistry(), StatTypeRegistryEvent::new);
  public final Registry<StatModType<?, ?, ?>> statModTypes = this.addRegistry(new StatModTypeRegistry(), StatModTypeRegistryEvent::new);
  public final Registry<Element> elements = this.addRegistry(new ElementRegistry(), ElementRegistryEvent::new);
  public final Registry<BattleEntityType> battleEntityTypes = this.addRegistry(new BattleEntityTypeRegistry(), BattleEntityTypeRegistryEvent::new);
  public final Registry<Item> items = this.addRegistry(new ItemRegistry(), ItemRegistryEvent::new);
  public final Registry<Equipment> equipment = this.addRegistry(new EquipmentRegistry(), EquipmentRegistryEvent::new);
  public final Registry<Shop> shop = this.addRegistry(new ShopRegistry(), ShopRegistryEvent::new);
  public final Registry<Spell> spell = this.addRegistry(new SpellRegistry(), SpellRegistryEvent::new);
  public final Registry<ConfigEntry<?>> config = this.addRegistry(new ConfigRegistry(), ConfigRegistryEvent::new);
  public final Registry<DeffPackage> deff = this.addRegistry(new DeffRegistry(), RegisterDeffsEvent::new);

  protected Registries(final EventManager events, final Consumer<Access> access) {
    super(events, access);
  }
}
