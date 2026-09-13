package legend.game.inventory.screens;

import legend.core.lang.I18nText;
import legend.core.lang.RawText;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.types.GameState52c;
import legend.game.types.MessageBoxResult;
import legend.game.types.MessageBoxType;
import legend.game.wmap.WMap;
import legend.game.wmap.preset.WorldMapPreset;
import legend.game.wmap.preset.WorldMapPresetEntry;
import legend.game.wmap.preset.WorldMapPresetManager;
import legend.game.wmap.world.WorldMapTravelRequestResult;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;

/** Catalog management shared by new campaigns and in-game options. */
public class WorldMapPresetsScreen extends VerticalLayoutScreen {
  private final Dropdown<WorldMapPresetEntry> presets = new Dropdown<>((index, entry) -> new RawText(entry.name()));
  private final Consumer<WorldMapPresetEntry> selection;
  private final Runnable unload;
  @Nullable private final GameState52c state;

  public WorldMapPresetsScreen(@Nullable final GameState52c state, final WorldMapPresetEntry selected, final Consumer<WorldMapPresetEntry> selection, final Runnable unload) {
    this.state = state;
    this.selection = selection;
    this.unload = unload;
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);
    this.addControl(new Background());
    this.addHotkey(new I18nText("lod_core.ui.options_presets.back"), INPUT_ACTION_MENU_BACK, this::back);
    this.presets.setZ(35);
    this.addRow(new I18nText("lod_core.ui.world_map_presets.title"), this.presets);
    this.button("use", this::use);
    this.button("refresh", () -> this.refresh(this.presets.getSelectedOption()));
    this.addRow(new I18nText("lod_core.ui.world_map_presets.folder"), null);
    if(state != null && !state.worldMapPreset.isEmpty()) {
      final WorldMapPreset current = WorldMapPresetManager.load(state);
      this.refresh(new WorldMapPresetEntry(current));
    } else {
      this.refresh(selected);
    }
  }

  private void button(final String key, final Runnable action) {
    final Button button = new Button(new I18nText("lod_core.ui.world_map_presets." + key));
    this.addRow(RawText.BLANK, button);
    button.onPressed(action::run);
  }

  private void refresh(final WorldMapPresetEntry selected) {
    final WorldMapPresetManager.Catalog catalog = WorldMapPresetManager.discover();
    this.presets.clearOptions();
    catalog.entries().forEach(this.presets::addOption);
    if(this.state != null && catalog.entries().stream().noneMatch(entry -> entry.id().equals(selected.id()))) {
      this.presets.addOption(selected);
    }
    for(int i = 0; i < this.presets.size(); i++) {
      if(this.presets.getOption(i).id().equals(selected.id())) {
        this.presets.setSelectedIndex(i);
        break;
      }
    }
    if(!catalog.errors().isEmpty()) {
      this.message(I18n.translate("lod_core.ui.world_map_presets.discovery_error", catalog.errors().size()) + "\n" + catalog.errors().getFirst());
    }
  }

  private void use() {
    final WorldMapPresetEntry selected = this.presets.getSelectedOption();
    if(this.state == null) {
      this.selection.accept(selected);
      this.back();
      return;
    }
    this.deferAction(() -> this.getStack().pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.world_map_presets.switch_confirm"), MessageBoxType.CONFIRMATION, result -> {
      if(result != MessageBoxResult.YES) {
        return;
      }
      try {
        final WorldMapPreset preset = selected.load();
        WorldMapPresetManager.validate(preset);
        if(currentEngineState_8004dd04 instanceof final WMap map) {
          final WorldMapTravelRequestResult outcome = map.requestWorldMapPreset(preset);
          if(outcome != WorldMapTravelRequestResult.ACCEPTED) {
            this.message(I18n.translate("lod_core.ui.world_map_presets.busy", outcome));
            return;
          }
        } else {
          this.state.worldMapPreset = WorldMapPresetManager.store(this.state, preset);
        }
        this.selection.accept(selected);
        this.back();
      } catch(final Exception e) {
        this.error(e);
      }
    })));
  }

  private void error(final Exception error) {
    this.message(I18n.translate("lod_core.ui.world_map_presets.error", error.getMessage()));
  }

  private void message(final String text) {
    this.deferAction(() -> this.getStack().pushScreen(new MessageBoxScreen(text, MessageBoxType.ALERT, result -> { })));
  }

  private void back() {
    startFadeEffect(2, 10);
    this.unload.run();
  }
}
