package legend.game.inventory.screens;

import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.Textbox;
import legend.game.types.GameState52c;

import static legend.core.GameEngine.SAVES;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.savedGameSelected_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class NewGameScreen extends MenuScreen {
  private final GameState52c state = new GameState52c();

  private final Textbox campaignName;

  private boolean unload;

  public NewGameScreen() {
    deallocateRenderables(0xff);
    scriptStartEffect(2, 10);

    this.state.indicatorMode_4e8 = 2;

    this.addControl(new Background());

    this.addControl(new Label("Save name")).setPos(10, 10);

    this.campaignName = this.addControl(new Textbox());
    this.campaignName.setText(SAVES.generateCampaignName());
    this.campaignName.setPos(110, 10);
    this.campaignName.setSize(150, 16);

    final Button options = this.addControl(new Button("Options"));
    options.setPos(10, 30);
    options.onMouseClick((x, y, button, mods) -> {
      SItem.menuStack.pushScreen(new OptionsScreen(this.state, () -> {
        scriptStartEffect(2, 10);
        SItem.menuStack.popScreen();
      }));

      return InputPropagation.HANDLED;
    });

    final Button startGame = this.addControl(new Button("Start Game"));
    startGame.setPos(340 - startGame.getWidth(), 220 - startGame.getHeight());
    startGame.onMouseClick((x, y, button, mods) -> {
      this.unload = true;
      return InputPropagation.HANDLED;
    });
  }

  @Override
  protected void render() {
    if(this.unload) {
      this.state.campaignName = this.campaignName.getText();
      gameState_800babc8 = this.state;
      savedGameSelected_800bdc34.set(true);
      this.menuEscape();
    }
  }

  private void menuEscape() {
    playSound(3);
    whichMenu_800bdc38 = WhichMenu.UNLOAD_NEW_GAME_MENU;
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
