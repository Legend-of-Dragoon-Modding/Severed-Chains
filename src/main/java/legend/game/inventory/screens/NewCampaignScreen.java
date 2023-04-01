package legend.game.inventory.screens;

import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Textbox;
import legend.game.types.GameState52c;
import legend.game.types.LodString;

import static legend.core.GameEngine.SAVES;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.savedGameSelected_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class NewCampaignScreen extends VerticalLayoutScreen {
  private final GameState52c state = new GameState52c();

  private final Textbox campaignName;

  private boolean unload;

  public NewCampaignScreen() {
    deallocateRenderables(0xff);
    scriptStartEffect(2, 10);

    this.state.indicatorMode_4e8 = 2;

    this.addControl(new Background());

    this.campaignName = this.addRow("Campaign name", new Textbox());
    this.campaignName.setText(SAVES.generateCampaignName());
    this.campaignName.setMaxLength(15);
    this.campaignName.setZ(35);

    final Button options = this.addRow("", new Button("Options"));
    options.onPressed(() ->
      SItem.menuStack.pushScreen(new OptionsScreen(this.state, () -> {
        scriptStartEffect(2, 10);
        SItem.menuStack.popScreen();
      }))
    );

    final Button startGame = this.addRow("", new Button("Start Game"));
    startGame.onPressed(() -> {
      if(SAVES.campaignExists(this.campaignName.getText())) {
        menuStack.pushScreen(new MessageBoxScreen(new LodString("Campaign name already\nin use"), 0, result1 -> { }));
      } else {
        this.unload = true;
      }
    });
  }

  @Override
  protected void render() {
    if(this.unload) {
      this.state.campaignName = this.campaignName.getText();
      gameState_800babc8 = this.state;
      savedGameSelected_800bdc34.set(true);
      playSound(2);
      whichMenu_800bdc38 = WhichMenu.UNLOAD_NEW_CAMPAIGN_MENU;
    }
  }

  private void menuEscape() {
    playSound(3);
    whichMenu_800bdc38 = WhichMenu.UNLOAD_NEW_CAMPAIGN_MENU;
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
