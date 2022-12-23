package legend.game.inventory.screens;

import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;

public class LoadGameScreen extends MenuScreen {
  private int loadingStage;

  @Override
  public void render() {
    switch(this.loadingStage) {
      case 0 -> {
        deallocateRenderables(0xffL);
        this.loadingStage++;
      }

      case 1 -> {

      }
    }
  }

  @Override
  public void handleInput() {

  }
}
