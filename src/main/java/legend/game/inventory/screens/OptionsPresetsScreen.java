package legend.game.inventory.screens;

import legend.core.lang.I18nText;
import legend.core.lang.RawText;
import legend.core.lang.TextComponent;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigPreset;
import legend.game.saves.ConfigPresetEntry;
import legend.game.saves.ConfigPresetManager;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.types.MessageBoxResult;
import legend.game.types.MessageBoxType;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.sound.Audio.playMenuSound;

public class OptionsPresetsScreen extends VerticalLayoutScreen {
  private final BiConsumer<Integer, List<ConfigPresetEntry>> unload;

  private final Dropdown<ConfigPresetEntry> presetList;
  private final Button edit;
  private final Button delete;

  public OptionsPresetsScreen(final BiConsumer<Integer, List<ConfigPresetEntry>> unload) {
    this.unload = unload;

    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.addHotkey(new I18nText("lod_core.ui.options_presets.back"), INPUT_ACTION_MENU_BACK, this::back);

    this.addControl(new Background());

    this.presetList = new Dropdown<>((i, e) -> e.getName());
    this.addRow(new I18nText("lod_core.ui.options_presets.list"), this.presetList);
    ConfigPresetManager.loadDefaultPresets().forEach(this.presetList::addOption);
    ConfigPresetManager.loadPresetList().forEach(this.presetList::addOption);
    this.presetList.onSelection(this::onPresetSelected);

    this.edit = new Button(new I18nText("lod_core.ui.options_presets.edit"));
    this.addRow(RawText.BLANK, this.edit);
    this.edit.onPressed(this::onEditPressed);

    this.delete = new Button(new I18nText("lod_core.ui.options_presets.delete"));
    this.addRow(RawText.BLANK, this.delete);
    this.delete.onPressed(this::onDeletePressed);

    final Button add = new Button(new I18nText("lod_core.ui.options_presets.add"));
    this.addRow(RawText.BLANK, add);
    add.onPressed(this::onAddPressed);

    this.onPresetSelected(this.presetList.getSelectedIndex());
  }

  private void onPresetSelected(final int index) {
    this.edit.setDisabled(!this.presetList.getSelectedOption().editable);
    this.delete.setDisabled(!this.presetList.getSelectedOption().editable);
  }

  private void onEditPressed() {
    final ConfigPreset preset = this.presetList.getSelectedOption().getPreset();

    if(preset == null) {
      this.deferAction(() -> this.getStack().pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.options_presets.invalid_preset"), MessageBoxType.CONFIRMATION, result -> { })));
      return;
    }

    final ConfigCollection newConfig = new ConfigCollection();
    newConfig.copyConfigFrom(preset.config);
    this.deferAction(() -> this.getStack().pushScreen(new OptionsCategoryScreen(newConfig, EnumSet.allOf(ConfigStorageLocation.class), () -> this.onOptionsClosed(preset.name.get(), newConfig, preset.config))));
  }

  private void onDeletePressed() {
    this.deferAction(() -> this.getStack().pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.options_presets.delete_confirm"), MessageBoxType.CONFIRMATION, this::onDeleteResult)));
  }

  private void onDeleteResult(final MessageBoxResult result) {
    if(result == MessageBoxResult.YES) {
      ConfigPresetManager.deletePreset(this.presetList.getSelectedOption());
      this.presetList.removeOption(this.presetList.getSelectedIndex());
      this.onPresetSelected(this.presetList.getSelectedIndex());
    }
  }

  private void onAddPressed() {
    final ConfigPreset preset = this.presetList.getSelectedOption().getPreset();
    this.deferAction(() -> this.getStack().pushScreen(new InputBoxScreen(new I18nText("lod_core.ui.options_presets.add_name"), preset != null ? preset.name.get() : "", (result, name) -> this.onAddResult(result, name, preset != null ? preset.config : null))));
  }

  private void onAddResult(final MessageBoxResult result, final String name, @Nullable final ConfigCollection oldConfig) {
    if(result == MessageBoxResult.YES) {
      // Make sure the name is unique
      if(ConfigPresetManager.presetExists(name)) {
        this.deferAction(() -> this.getStack().pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.options_presets.add_exists"), MessageBoxType.ALERT, r -> { })));
        return;
      }

      final ConfigCollection newConfig = new ConfigCollection();

      // Copy the reference config into the new one
      if(oldConfig != null) {
        newConfig.copyConfigFrom(oldConfig);
      }

      // Open the regular config editor
      this.deferAction(() -> this.getStack().pushScreen(new OptionsCategoryScreen(newConfig, EnumSet.allOf(ConfigStorageLocation.class), () -> this.onOptionsClosed(name, newConfig, null))));
    }
  }

  private void onOptionsClosed(final String name, final ConfigCollection config, @Nullable final ConfigCollection originalConfig) {
    startFadeEffect(2, 10);
    this.getStack().popScreen();
    this.getStack().pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.options_presets.save_changes"), MessageBoxType.CONFIRMATION, result -> this.onSaveChangesResult(result, name, config, originalConfig)));
  }

  private void onSaveChangesResult(final MessageBoxResult result, final String name, final ConfigCollection config, @Nullable final ConfigCollection originalConfig) {
    if(result == MessageBoxResult.YES) {
      final Path path = ConfigPresetManager.savePreset(name, config);
      ConfigStorage.saveConfig(config, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));

      if(originalConfig == null) {
        // We aren't editing an existing preset, add a new one to the list

        final TextComponent text = new RawText(name);
        final ConfigPresetEntry preset = new ConfigPresetEntry(path, text, CompletableFuture.completedFuture(new ConfigPreset(text, config)), true);
        this.presetList.addOption(preset);
        this.presetList.setSelected(preset);
        this.onPresetSelected(this.presetList.getSelectedIndex());
      } else {
        // We're editing an existing config, copy the changes to the original

        originalConfig.copyConfigFrom(config);
      }
    }
  }

  private void back() {
    playMenuSound(3);

    final List<ConfigPresetEntry> presets = new ArrayList<>();

    for(int i = 0; i < this.presetList.size(); i++) {
      presets.add(this.presetList.getOption(i));
    }

    this.unload.accept(this.presetList.getSelectedIndex(), presets);
  }
}
