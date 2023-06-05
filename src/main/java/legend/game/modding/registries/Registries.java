package legend.game.modding.registries;

import legend.game.characters.Element;
import legend.game.characters.ElementRegistry;
import legend.game.characters.ElementRegistryEvent;
import legend.game.characters.StatType;
import legend.game.characters.StatTypeRegistry;
import legend.game.characters.StatTypeRegistryEvent;
import legend.game.combat.bobj.BattleObjectType;
import legend.game.combat.bobj.BattleObjectTypeRegistry;
import legend.game.combat.bobj.BattleObjectTypeRegistryEvent;
import legend.game.inventory.Equipment;
import legend.game.inventory.EquipmentRegistry;
import legend.game.inventory.EquipmentRegistryEvent;
import legend.game.inventory.Item;
import legend.game.inventory.ItemRegistry;
import legend.game.inventory.ItemRegistryEvent;
import legend.game.inventory.Spell;
import legend.game.inventory.SpellRegistry;
import legend.game.inventory.SpellRegistryEvent;
import legend.game.modding.events.registries.RegistryEvent;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistry;
import legend.game.saves.ConfigRegistryEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static legend.core.GameEngine.EVENTS;

public class Registries {
  private final List<MutableRegistry<?>> registries = new ArrayList<>();
  private final Map<MutableRegistry<?>, Function<MutableRegistry<?>, RegistryEvent.Register<?>>> registryEvents = new HashMap<>();

  public final Registry<StatType<?>> stats = this.addRegistry(new StatTypeRegistry(), StatTypeRegistryEvent::new);
  public final Registry<Element> elements = this.addRegistry(new ElementRegistry(), ElementRegistryEvent::new);
  public final Registry<BattleObjectType> battleObjectTypes = this.addRegistry(new BattleObjectTypeRegistry(), BattleObjectTypeRegistryEvent::new);
  public final Registry<Item> items = this.addRegistry(new ItemRegistry(), ItemRegistryEvent::new);
  public final Registry<Equipment> equipment = this.addRegistry(new EquipmentRegistry(), EquipmentRegistryEvent::new);
  public final Registry<Spell> spell = this.addRegistry(new SpellRegistry(), SpellRegistryEvent::new);
  public final Registry<ConfigEntry<?>> config = this.addRegistry(new ConfigRegistry(), ConfigRegistryEvent::new);

  public Registries(final Consumer<Access> access) {
    access.accept(new Access());
  }

  private <Type extends RegistryEntry> Registry<Type> addRegistry(final Registry<Type> registry, final Function<MutableRegistry<Type>, RegistryEvent.Register<Type>> registryEvent) {
    final MutableRegistry<Type> mutableRegistry = (MutableRegistry<Type>)registry;
    this.registries.add(mutableRegistry);
    //noinspection unchecked
    this.registryEvents.put(mutableRegistry, (Function<MutableRegistry<?>, RegistryEvent.Register<?>>)(Object)registryEvent);
    return registry;
  }

  public class Access {
    private Access() { }

    private final Set<Registry<?>> initialized = new HashSet<>();

    public <T extends RegistryEntry> void initialize(final Registry<?> registry) {
      if(this.initialized.contains(registry)) {
        throw new IllegalStateException("Registry " + registry + " already initialized");
      }

      final MutableRegistry<?> mutableRegistry = (MutableRegistry<?>)registry;

      if(!Registries.this.registryEvents.containsKey(mutableRegistry)) {
        throw new IllegalArgumentException("Unknown registry " + registry);
      }

      EVENTS.postEvent(Registries.this.registryEvents.get(mutableRegistry).apply(mutableRegistry));
      mutableRegistry.lock();
      this.initialized.add(mutableRegistry);
    }

    public void initializeRemaining() {
      for(final MutableRegistry<?> registry : Registries.this.registries) {
        if(!this.initialized.contains(registry)) {
          this.initialize(registry);
        }
      }
    }

    public void reset() {
      this.initialized.clear();

      for(final MutableRegistry<?> registry : Registries.this.registries) {
        registry.reset();
      }
    }
  }
}
