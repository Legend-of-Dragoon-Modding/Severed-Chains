package legend.game.modding.coremod.config;

import legend.core.memory.types.IntRef;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputActivation;
import legend.game.SItem;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.KeybindsScreen;
import legend.game.inventory.screens.controls.Button;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import legend.game.unpacker.FileData;
import legend.game.unpacker.GrowableFileData;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Scus94491BpeSegment.startFadeEffect;

public class ControllerKeybindsConfigEntry extends ConfigEntry<Map<RegistryDelegate<InputAction>, List<InputActivation>>> {
  public ControllerKeybindsConfigEntry() {
    super(Map.of(), ConfigStorageLocation.CAMPAIGN, ConfigCategory.CONTROLS, ControllerKeybindsConfigEntry::serializer, ControllerKeybindsConfigEntry::deserializer);

    this.setEditControl((current, configCollection) -> {
      final Button button = new Button(I18n.translate(CoreMod.MOD_ID + ".config.controller_keybinds.configure"));
      button.onPressed(() -> button.getScreen().getStack().pushScreen(new KeybindsScreen(configCollection, () -> {
        startFadeEffect(2, 10);
        SItem.menuStack.popScreen();
      })));

      return button;
    });
  }

  @Override
  public boolean availableInBattle() {
    return false;
  }

  private static byte[] serializer(final Map<RegistryDelegate<InputAction>, List<InputActivation>> actionMap) {
    final FileData out = new GrowableFileData(100);
    final IntRef offset = new IntRef();

    out.writeShort(offset, actionMap.size());

    for(final var e : actionMap.entrySet()) {
      out.writeRegistryId(offset, e.getKey().getId());
      out.writeByte(offset, e.getValue().size());

      for(final InputActivation activation : e.getValue()) {
        activation.serialize(out, offset);
      }
    }

    return out.getBytes();
  }

  private static Map<RegistryDelegate<InputAction>, List<InputActivation>> deserializer(final byte[] data) {
    if(data.length >= 1) {
      final Map<RegistryDelegate<InputAction>, List<InputActivation>> out = new HashMap<>();

      final FileData in = new FileData(data);
      final IntRef offset = new IntRef();

      final int actionsSize = in.readUShort(offset);

      for(int actionIndex = 0; actionIndex < actionsSize; actionIndex++) {
        final RegistryDelegate<InputAction> action = REGISTRIES.inputActions.getEntry(in.readRegistryId(offset));
        final int activationsSize = in.readUByte(offset);

        out.put(action, new ArrayList<>());

        for(int activationIndex = 0; activationIndex < activationsSize; activationIndex++) {
          out.get(action).add(InputActivation.deserialize(in, offset));
        }
      }

      return out;
    }

    return Map.of();
  }
}
