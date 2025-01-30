package legend.game.modding.coremod.config;

import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import org.lwjgl.PointerBuffer;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.RENDERER;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;

public class MonitorConfigEntry extends ConfigEntry<Integer> {
  public MonitorConfigEntry() {
    super(
      0,
      ConfigStorageLocation.GLOBAL,
      ConfigCategory.GRAPHICS,
      i -> new byte[] {i.byteValue()},
      bytes -> (int)bytes[0]
    );

    this.setEditControl((current, gameState) -> {
      final Dropdown<String> dropdown = new Dropdown<>();
      dropdown.onSelection(index -> gameState.setConfig(this, index));

      final PointerBuffer monitorPtrs = glfwGetMonitors();

      for(int i = 0; i < monitorPtrs.limit(); i++) {
        final long ptr = monitorPtrs.get(i);
        dropdown.addOption(glfwGetMonitorName(ptr));
      }

      final int selected = CONFIG.getConfig(this);
      if(selected >= 0 && selected < monitorPtrs.limit()) {
        dropdown.setSelectedIndex(selected);
      } else {
        dropdown.setSelectedIndex(0);
      }

      return dropdown;
    });
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final Integer oldValue, final Integer newValue) {
    super.onChange(configCollection, oldValue, newValue);
    RENDERER.window().updateMonitor();
  }
}
