package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.game.i18n.I18n;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.BigList;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.SaveCard;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.gamestate.GameLoadedEvent;
import legend.game.saves.Campaign;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.types.MessageBoxResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.SAVES;
import static legend.core.GameEngine.bootMods;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8005.collidedPrimitiveIndex_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class CampaignSelectionScreen extends MenuScreen {
  private static final Logger LOGGER = LogManager.getFormatterLogger(MenuScreen.class);

  private final BigList<Campaign> campaignList;

  public CampaignSelectionScreen() {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.addControl(new Background());

    final Label title = this.addControl(new Label(I18n.translate("lod_core.ui.campaign_selection.title")));
    title.getFontOptions().set(UI_TEXT_CENTERED);
    title.setPos(0, 10);
    title.setWidth(this.getWidth());

    final Label hotkeys = this.addControl(new Label(I18n.translate("lod_core.ui.campaign_selection.hotkeys", "\u0120", "\u011f")));
    hotkeys.getFontOptions().set(UI_TEXT).horizontalAlign(HorizontalAlign.RIGHT);
    hotkeys.setPos(10, 226);
    hotkeys.setWidth(this.getWidth() - 20);

    final SaveCard saveCard = this.addControl(new SaveCard());
    saveCard.setPos(16, 160);

    this.campaignList = this.addControl(new BigList<>(c -> c.name));
    this.campaignList.setPos(16, 16);
    this.campaignList.setSize(360, 144);
    this.campaignList.onHighlight(campaign -> {
      if(campaign == null) {
        this.menuEscape();
        return;
      }

      saveCard.setSaveData(campaign.latestSave);
    });
    this.campaignList.onSelection(this::onSelection);
    this.setFocus(this.campaignList);

    for(final Campaign campaign : SAVES.loadAllCampaigns()) {
      this.campaignList.addEntry(campaign);
    }
  }

  private void onSelection(final Campaign campaign) {
    playMenuSound(2);

    CONFIG.clearConfig(ConfigStorageLocation.CAMPAIGN);
    campaign.loadConfigInto(CONFIG);

    final Set<String> missingMods;
    if(CONFIG.hasConfig(CoreMod.ENABLED_MODS_CONFIG.get())) {
      missingMods = bootMods(Set.of(CONFIG.getConfig(CoreMod.ENABLED_MODS_CONFIG.get())));
    } else {
      // Fallback for old saves from before the config key existed
      missingMods = bootMods(MODS.getAllModIds());
    }

    final Runnable loadGameScreen = () -> menuStack.pushScreen(new LoadGameScreen(save -> {
      menuStack.reset();

      CONFIG.clearConfig(ConfigStorageLocation.SAVE);
      CONFIG.copyConfigFrom(save.config);

      GameEngine.bootRegistries();

      final GameLoadedEvent event = EVENTS.postEvent(new GameLoadedEvent(save.state));

      gameState_800babc8 = event.gameState;
      gameState_800babc8.syncIds();

      loadingNewGameState_800bdc34 = true;
      whichMenu_800bdc38 = WhichMenu.UNLOAD;

      submapScene_80052c34 = gameState_800babc8.submapScene_a4;
      submapCut_80052c30 = gameState_800babc8.submapCut_a8;
      collidedPrimitiveIndex_80052c38 = gameState_800babc8.submapCut_a8;

      if(gameState_800babc8.submapCut_a8 == 264) { // Somewhere in Home of Giganto
        submapScene_80052c34 = 53;
      }
    }, () -> {
      menuStack.popScreen();
      startFadeEffect(2, 10);
      bootMods(MODS.getAllModIds());
    }, campaign));

    if(missingMods.isEmpty()) {
      loadGameScreen.run();
    } else {
      menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.campaign_selection.missing_mods_confirm"), 2, result -> {
        if(result == MessageBoxResult.YES) {
          loadGameScreen.run();
        }
      }));
    }
  }

  @Override
  public void setFocus(@Nullable final Control control) {
    super.setFocus(this.campaignList);
  }

  @Override
  protected void render() {

  }

  private void menuMods() {
    final Campaign campaign = this.campaignList.getSelected();

    if(campaign == null) {
      playMenuSound(40);
      return;
    }

    final Set<String> originalMods = Set.of(campaign.config.getConfig(CoreMod.ENABLED_MODS_CONFIG.get()));
    final Set<String> modIds = new HashSet<>(originalMods);

    menuStack.pushScreen(new ModsScreen(modIds, () -> {
      if(!originalMods.equals(modIds)) {
        menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.campaign_selection.change_mods_confirm"), 2, result -> {
          if(result == MessageBoxResult.YES) {
            campaign.config.setConfig(CoreMod.ENABLED_MODS_CONFIG.get(), modIds.toArray(String[]::new));
            ConfigStorage.saveConfig(campaign.config, ConfigStorageLocation.CAMPAIGN, campaign.path.resolve("campaign_config.dcnf"));
            startFadeEffect(2, 10);
            this.getStack().popScreen();
          }
        }));
      } else {
        startFadeEffect(2, 10);
        this.getStack().popScreen();
      }
    }));
  }

  private void menuDelete() {
    playMenuSound(40);

    if(this.campaignList.getSelected() != null) {
      menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.campaign_selection.delete_campaign_confirm"), 2, result -> {
        if(result == MessageBoxResult.YES) {
          try {
            this.campaignList.getSelected().delete();
            this.campaignList.removeEntry(this.campaignList.getSelected());
          } catch(final IOException e) {
            LOGGER.error(I18n.translate("lod_core.ui.campaign_selection.failed_to_delete_campaign"), e);
            this.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.campaign_selection.failed_to_delete_campaign"), 0, result1 -> {})));
          }
        }
      }));
    }
  }

  private void menuEscape() {
    playMenuSound(3);
    whichMenu_800bdc38 = WhichMenu.UNLOAD;

    // Restore all mods when going back to the title screen
    bootMods(MODS.getAllModIds());
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_NORTH) {
      this.menuMods();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_WEST) {
      this.menuDelete();
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
