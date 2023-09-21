package legend.game.title;

import legend.core.gpu.RECT;
import legend.core.memory.Method;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.EngineStateEnum;
import legend.game.types.McqHeader;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;

import static legend.core.GameEngine.MODS;
import static legend.core.GameEngine.bootMods;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.renderMcq;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameOverMcq_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;

public final class GameOver {
  private GameOver() { }

  @Method(0x800c7558L)
  public static void gameOverLoaded(final FileData data) {
    final McqHeader mcq = new McqHeader(data);

    final RECT rect = new RECT().set((short)640, (short)0, (short)mcq.vramWidth_08, (short)mcq.vramHeight_0a);
    gameOverMcq_800bdc3c = mcq;
    LoadImage(rect, mcq.imageData);
    pregameLoadingStage_800bb10c.set(3);
  }

  @Method(0x800c75b4L)
  public static void renderGameOver() {
    renderMcq(gameOverMcq_800bdc3c, 640, 0, -320, -108, 36, 128);
  }

  @Method(0x800c75fcL)
  public static void gameOver() {
    switch(pregameLoadingStage_800bb10c.get()) {
      case 0 -> {
        if(Unpacker.getLoadingFileCount() == 0) {
          bootMods(MODS.getAllModIds());

          FUN_8002a9c0();
          resizeDisplay(640, 240);
          pregameLoadingStage_800bb10c.set(1);
        }
      }

      case 1 -> {
        pregameLoadingStage_800bb10c.set(2);
        loadDrgnFile(0, 6667, GameOver::gameOverLoaded);
      }

      case 3 -> {
        deallocateRenderables(0xff);
        startFadeEffect(2, 10);
        pregameLoadingStage_800bb10c.set(4);
      }

      // Game Over Screen
      case 4 -> {
        if(Input.pressedThisFrame(InputAction.BUTTON_CENTER_2) || Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          Scus94491BpeSegment_8002.playSound(2);
          pregameLoadingStage_800bb10c.set(5);
          startFadeEffect(1, 10);
        }

        renderGameOver();
      }

      case 5 -> {
        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          pregameLoadingStage_800bb10c.set(6);
        }

        //LAB_800c7740
        renderGameOver();
      }

      case 6 -> {
        deallocateRenderables(0xff);
        uiFile_800bdc3c = null;
        gameOverMcq_800bdc3c = null;
        engineStateOnceLoaded_8004dd24 = EngineStateEnum.TITLE_02;
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8 = 2;
      }
    }

    //LAB_800c7788
  }
}
