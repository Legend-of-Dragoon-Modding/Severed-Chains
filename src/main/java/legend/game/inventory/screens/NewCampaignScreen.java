package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Textbox;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.gamestate.GameLoadedEvent;
import legend.game.modding.events.gamestate.NewGameEvent;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.types.GameState52c;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.SAVES;
import static legend.core.GameEngine.bootMods;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class NewCampaignScreen extends VerticalLayoutScreen {
  private final GameState52c state = new GameState52c();
  private final Set<String> enabledMods = new HashSet<>();

  private final Textbox campaignName;

  private boolean unload;

  public NewCampaignScreen() {
    CONFIG.clearConfig(ConfigStorageLocation.CAMPAIGN);
    this.enabledMods.addAll(MODS.getAllModIds());

    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.addControl(new Background());

    this.campaignName = this.addRow("Campaign name", new Textbox());
    this.campaignName.setText(SAVES.generateCampaignName());
    this.campaignName.setMaxLength(15);
    this.campaignName.setZ(35);

    this.addRow("", new Button("Options")).onPressed(() ->
      SItem.menuStack.pushScreen(new OptionsCategoryScreen(CONFIG, EnumSet.allOf(ConfigStorageLocation.class), () -> {
        startFadeEffect(2, 10);
        SItem.menuStack.popScreen();

        // Update global config but don't save campaign config until an actual save file is made so we don't end up with orphan campaigns
        ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
      }))
    );

    this.addRow("", new Button("Mods")).onPressed(() ->
      SItem.menuStack.pushScreen(new ModsScreen(this.enabledMods, () -> {
        bootMods(this.enabledMods);

        startFadeEffect(2, 10);
        SItem.menuStack.popScreen();
      }))
    );

    final Button startGame = this.addRow("", new Button("Start Game"));
    startGame.onPressed(() -> {
      if(SAVES.campaignExists(this.campaignName.getText())) {
        menuStack.pushScreen(new MessageBoxScreen("Campaign name already\nin use", 0, result1 -> { }));
      } else {
        this.unload = true;
      }
    });
  }

  @Override
  protected void render() {
    if(this.unload) {
      GameEngine.bootRegistries();

      this.state.campaignName = this.campaignName.getText();

      final NewGameEvent newGameEvent = EVENTS.postEvent(new NewGameEvent(this.state));
      final GameLoadedEvent gameLoadedEvent = EVENTS.postEvent(new GameLoadedEvent(newGameEvent.gameState));

      gameState_800babc8 = gameLoadedEvent.gameState;

      CONFIG.setConfig(CoreMod.ENABLED_MODS_CONFIG.get(), this.enabledMods.toArray(String[]::new));

      loadingNewGameState_800bdc34 = true;
      playMenuSound(2);
      whichMenu_800bdc38 = WhichMenu.UNLOAD;
    }
  }

  private void menuEscape() {
    playMenuSound(3);
    whichMenu_800bdc38 = WhichMenu.UNLOAD;

    bootMods(MODS.getAllModIds());
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.getFocus() == this.campaignName) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
