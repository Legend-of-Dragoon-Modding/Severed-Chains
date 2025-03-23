package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.core.platform.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.Textbox;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.gamestate.GameLoadedEvent;
import legend.game.modding.events.gamestate.NewGameEvent;
import legend.game.saves.Campaign;
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
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;

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

    this.campaignName = new Textbox();
    this.campaignName.setText(SAVES.generateCampaignName());
    this.campaignName.setMaxLength(15);
    this.campaignName.setZ(35);
    this.addRow("Campaign name", this.campaignName);

    final Button options = new Button("Options");
    this.addRow("", options);
    options.onPressed(() ->
      this.getStack().pushScreen(new OptionsCategoryScreen(CONFIG, EnumSet.allOf(ConfigStorageLocation.class), () -> {
        startFadeEffect(2, 10);
        this.getStack().popScreen();

        // Update global config but don't save campaign config until an actual save file is made so we don't end up with orphan campaigns
        ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
      }))
    );

    final Button mods = new Button("Mods");
    this.addRow("", mods);
    mods.onPressed(() ->
      this.deferAction(() ->
        this.getStack().pushScreen(new ModsScreen(this.enabledMods, () -> {
          bootMods(this.enabledMods);

          startFadeEffect(2, 10);
          this.getStack().popScreen();
        }))
      )
    );

    final Button startGame = new Button("Start Game");
    this.addRow("", startGame);
    startGame.onPressed(() -> {
      if(SAVES.campaignExists(this.campaignName.getText())) {
        this.deferAction(() -> this.getStack().pushScreen(new MessageBoxScreen("Campaign name already\nin use", 0, result1 -> { })));
      } else {
        this.unload = true;
      }
    });

    final Label saveSlots = this.addControl(new Label("Severed Chains has unlimited save slots and we recommend\nyou save in a new slot each time."));
    saveSlots.setWidth(this.getWidth());
    saveSlots.getFontOptions().size(0.66f).horizontalAlign(HorizontalAlign.CENTRE);
    saveSlots.setY(200);
  }

  @Override
  protected void render() {
    if(this.unload) {
      GameEngine.bootRegistries();

      this.state.campaign = Campaign.create(SAVES, this.campaignName.getText().strip());

      final NewGameEvent newGameEvent = EVENTS.postEvent(new NewGameEvent(this.state));
      final GameLoadedEvent gameLoadedEvent = EVENTS.postEvent(new GameLoadedEvent(newGameEvent.gameState));

      gameState_800babc8 = gameLoadedEvent.gameState;

      this.state.campaign.loadConfigInto(CONFIG);
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
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.getFocus() == this.campaignName) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get()) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
