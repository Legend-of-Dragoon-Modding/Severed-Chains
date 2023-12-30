package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.BigList;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.gamestate.GameLoadedEvent;
import legend.game.saves.Campaign;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.types.LodString;
import legend.game.types.MessageBoxResult;

import java.nio.file.Path;
import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.SAVES;
import static legend.core.GameEngine.bootMods;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class CampaignSelectionScreen extends MenuScreen {
  public CampaignSelectionScreen() {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.addControl(new Background());

    final SaveCard saveCard = this.addControl(new SaveCard());
    saveCard.setPos(16, 160);

    final BigList<Campaign> campaignList = this.addControl(new BigList<>(Campaign::filename));
    campaignList.setPos(16, 16);
    campaignList.setSize(360, 144);
    campaignList.onHighlight(campaign -> saveCard.setSaveData(campaign.latestSave()));
    campaignList.onSelection(this::onSelection);
    this.setFocus(campaignList);

    for(final Campaign campaign : SAVES.loadAllCampaigns()) {
      campaignList.addEntry(campaign);
    }
  }

  private void onSelection(final Campaign campaign) {
    CONFIG.clearConfig(ConfigStorageLocation.CAMPAIGN);
    ConfigStorage.loadConfig(CONFIG, ConfigStorageLocation.CAMPAIGN, Path.of("saves", campaign.filename(), "campaign_config.dcnf"));

    final String[] modIds = CONFIG.getConfig(CoreMod.ENABLED_MODS_CONFIG.get());
    final Set<String> missingMods;
    if(modIds.length != 0) {
      missingMods = bootMods(Set.of(modIds));
    } else {
      // Fallback for old saves from before the config key existed
      missingMods = bootMods(MODS.getAllModIds());
    }

    final Runnable loadGameScreen = () -> menuStack.pushScreen(new LoadGameScreen(save -> {
      menuStack.popScreen();

      CONFIG.clearConfig(ConfigStorageLocation.SAVE);
      CONFIG.copyConfigFrom(save.config());

      GameEngine.bootRegistries();

      final GameLoadedEvent event = EVENTS.postEvent(new GameLoadedEvent(save.state()));

      gameState_800babc8 = event.gameState;
      gameState_800babc8.syncIds();

      loadingNewGameState_800bdc34 = true;
      whichMenu_800bdc38 = WhichMenu.UNLOAD_CAMPAIGN_SELECTION_MENU;

      submapScene_80052c34 = gameState_800babc8.submapScene_a4;
      submapCut_80052c30 = gameState_800babc8.submapCut_a8;
      collidedPrimitiveIndex_80052c38 = gameState_800babc8.submapCut_a8;

      if(gameState_800babc8.submapCut_a8 == 264) { // Somewhere in Home of Giganto
        submapScene_80052c34 = 53;
      }
    }, () -> {
      menuStack.popScreen();
      startFadeEffect(2, 10);
    }, campaign));

    if(missingMods.isEmpty()) {
      loadGameScreen.run();
    } else {
      menuStack.pushScreen(new MessageBoxScreen(new LodString("Missing mods, continue?"), 2, result -> {
        if(result == MessageBoxResult.YES) {
          loadGameScreen.run();
        }
      }));
    }
  }

  @Override
  protected void render() {
    SItem.renderCentredText(new LodString("Campaigns"), 188, 10, TextColour.BROWN);
  }

  private void menuEscape() {
    playSound(3);
    whichMenu_800bdc38 = WhichMenu.UNLOAD_CAMPAIGN_SELECTION_MENU;

    // Restore all mods when going back to the title screen
    bootMods(MODS.getAllModIds());
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
