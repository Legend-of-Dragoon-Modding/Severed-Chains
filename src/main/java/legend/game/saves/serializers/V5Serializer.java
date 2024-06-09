package legend.game.saves.serializers;

import legend.core.memory.types.IntRef;
import legend.game.EngineState;
import legend.game.modding.events.saves.SaveTypeEvent;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SavedGame;
import legend.game.saves.campaigns.CampaignType;
import legend.game.saves.types.SaveDisplay;
import legend.game.saves.types.SaveType;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.REGISTRIES;

public final class V5Serializer {
  private V5Serializer() { }

  public static final int MAGIC_V5 = 0x35615344; // DSa4

  public static FileData fromV5Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V5) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame<SaveDisplay> fromV5(final String filename, final FileData data) {
    final IntRef offset = new IntRef();
    final String name = data.readAscii(offset);

    final RegistryId campaignTypeId = data.readRegistryId(offset);
    final RegistryDelegate<CampaignType> campaignType = REGISTRIES.campaignTypes.getEntry(campaignTypeId);

    final RegistryId saveTypeId = data.readRegistryId(offset);
    final RegistryDelegate<SaveType<?>> saveType = REGISTRIES.saveTypes.getEntry(saveTypeId);
    final SaveDisplay display = saveType.get().deserialize(data.slice(offset.get()), offset);
    final GameState52c state = campaignType.get().loadGameState(data, offset);

    final ConfigCollection config = new ConfigCollection();
    ConfigStorage.loadConfig(config, ConfigStorageLocation.SAVE, data.slice(offset.get()));

    final Map<RegistryId, Set<RegistryId>> registryIds = new HashMap<>();
    final int registryCount = data.readVarInt(offset);
    for(int registryIndex = 0; registryIndex < registryCount; registryIndex++) {
      final RegistryId registryId = data.readRegistryId(offset);
      final int idCount = data.readVarInt(offset);
      final Set<RegistryId> ids = new HashSet<>();

      for(int idIndex = 0; idIndex < idCount; idIndex++) {
        ids.add(data.readRegistryId(offset));
      }

      registryIds.put(registryId, ids);
    }

    return new SavedGame<>(filename, name, campaignType, (RegistryDelegate)saveType, display, state, config, registryIds);
  }

  public static int toV5(final String name, final FileData data, final GameState52c gameState, final ActiveStatsa0[] activeStats, final CampaignType campaignType, final EngineState<?> engineState) {
    final SaveType saveType = EVENTS.postEvent(new SaveTypeEvent(gameState, engineState)).saveType;

    final IntRef offset = new IntRef();
    data.writeAscii(offset, name);
    data.writeRegistryId(offset, campaignType.getRegistryId());
    data.writeRegistryId(offset, saveType.getRegistryId());

    saveType.serialize(data.slice(offset.get()), saveType.createDisplayData(gameState, activeStats, engineState), offset);
    campaignType.saveGameState(data, offset, gameState);

    offset.add(ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.SAVE, data.slice(offset.get())));

    // Cache registry keys to make sure mods haven't changed on load
    data.writeVarInt(offset, REGISTRIES.count());
    for(final Registry<?> registry : REGISTRIES) {
      data.writeRegistryId(offset, registry.id);
      data.writeVarInt(offset, registry.size());

      for(final RegistryId id : registry) {
        data.writeRegistryId(offset, id);
      }
    }

    return offset.get();
  }
}
