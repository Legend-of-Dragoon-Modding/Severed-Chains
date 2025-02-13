package legend.game.title;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.McqBuilder;
import legend.core.opengl.Obj;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.types.McqHeader;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.resetSubmapToNewGame;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameOverMcq_800bdc3c;

public class GameOver extends EngineState {
  private int loadingStage;

  private Obj background;
  private final MV transforms = new MV();

  @Override
  public boolean allowsWidescreen() {
    return false;
  }

  @Method(0x800c7558L)
  private void gameOverLoaded(final FileData data) {
    final McqHeader mcq = new McqHeader(data);

    final Rect4i rect = new Rect4i(640, 0, mcq.vramWidth_08, mcq.vramHeight_0a);
    gameOverMcq_800bdc3c = mcq;
    GPU.uploadData15(rect, mcq.imageData);
    this.loadingStage = 3;
  }

  @Method(0x800c75b4L)
  private void renderGameOver() {
    if(this.background == null) {
      this.background = new McqBuilder("Game over", gameOverMcq_800bdc3c)
        .vramOffset(640, 0)
        .build();
    }

    this.transforms.transfer.set(GPU.getOffsetX() - 320.0f, GPU.getOffsetY() - 120.0f, 144.0f);
    RENDERER.queueOrthoModel(this.background, this.transforms, QueuedModelStandard.class);
  }

  @Override
  @Method(0x800c75fcL)
  public void tick() {
    switch(this.loadingStage) {
      case 0 -> {
        if(Loader.getLoadingFileCount() == 0) {
          resetSubmapToNewGame();
          resizeDisplay(640, 240);
          this.loadingStage = 1;
        }
      }

      case 1 -> {
        this.loadingStage = 2;
        loadDrgnFile(0, 6667, this::gameOverLoaded);
      }

      case 3 -> {
        deallocateRenderables(0xff);
        startFadeEffect(2, 10);
        this.loadingStage = 4;
      }

      // Game Over Screen
      case 4 -> {
        if(Input.pressedThisFrame(InputAction.BUTTON_CENTER_2) || Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          Scus94491BpeSegment_8002.playMenuSound(2);
          this.loadingStage = 5;
          startFadeEffect(1, 10);
        }

        this.renderGameOver();
      }

      case 5 -> {
        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          this.loadingStage = 6;
        }

        //LAB_800c7740
        this.renderGameOver();
      }

      case 6 -> {
        deallocateRenderables(0xff);

        if(this.background != null) {
          this.background.delete();
          this.background = null;
        }

        gameOverMcq_800bdc3c = null;
        engineStateOnceLoaded_8004dd24 = EngineStateEnum.TITLE_02;
        vsyncMode_8007a3b8 = 2;
      }
    }

    //LAB_800c7788
  }
}
