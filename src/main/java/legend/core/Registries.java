package legend.core;

import legend.game.EngineStateType;
import legend.game.EngineStateTypeRegistry;
import legend.game.EngineStateTypeRegistryEvent;
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
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistry;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistry;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.Spell;
import legend.game.inventory.SpellRegistry;
import legend.game.inventory.SpellRegistryEvent;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistry;
import legend.game.saves.ConfigRegistryEvent;
import legend.game.saves.campaigns.CampaignType;
import legend.game.saves.campaigns.CampaignTypeRegistry;
import legend.game.saves.campaigns.CampaignTypeRegistryEvent;
import legend.game.saves.types.SaveType;
import legend.game.saves.types.SaveTypeRegistry;
import legend.game.saves.types.SaveTypeRegistryEvent;
import org.legendofdragoon.modloader.events.EventManager;
import org.legendofdragoon.modloader.registries.Registry;

import java.util.function.Consumer;

public class Registries extends org.legendofdragoon.modloader.registries.Registries {
  public final Registry<StatType<?>> statTypes = this.addRegistry(new StatTypeRegistry(), StatTypeRegistryEvent::new);
  public final Registry<StatModType<?, ?, ?>> statModTypes = this.addRegistry(new StatModTypeRegistry(), StatModTypeRegistryEvent::new);
  public final Registry<Element> elements = this.addRegistry(new ElementRegistry(), ElementRegistryEvent::new);
  public final Registry<BattleEntityType> battleEntityTypes = this.addRegistry(new BattleEntityTypeRegistry(), BattleEntityTypeRegistryEvent::new);
  public final Registry<Item> items = this.addRegistry(new ItemRegistry(), ItemRegistryEvent::new);
  public final Registry<Equipment> equipment = this.addRegistry(new EquipmentRegistry(), EquipmentRegistryEvent::new);
  public final Registry<Spell> spell = this.addRegistry(new SpellRegistry(), SpellRegistryEvent::new);
  public final Registry<ConfigEntry<?>> config = this.addRegistry(new ConfigRegistry(), ConfigRegistryEvent::new);
  public final Registry<SaveType<?>> saveTypes = this.addRegistry(new SaveTypeRegistry(), SaveTypeRegistryEvent::new);
  public final Registry<EngineStateType<?>> engineStateTypes = this.addRegistry(new EngineStateTypeRegistry(), EngineStateTypeRegistryEvent::new);
  public final Registry<CampaignType> campaignTypes = this.addRegistry(new CampaignTypeRegistry(), CampaignTypeRegistryEvent::new);

  protected Registries(final EventManager events, final Consumer<Access> access) {
    super(events, access);
  }
}
