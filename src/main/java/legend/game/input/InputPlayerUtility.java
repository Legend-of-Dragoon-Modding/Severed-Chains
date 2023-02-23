package legend.game.input;

import static legend.core.GameEngine.SPU;

public final class InputPlayerUtility {
  public static void update()
  {
    if(Input.pressedThisFrame(InputAction.BUTTON_CENTER_1))
    {
      killSoundSpuVoices();
    }
  }

  private static void killSoundSpuVoices()
  {
      for(int i = 0; i < 24; i++) {
        SPU.voices[i].LEFT.set(0);
        SPU.voices[i].RIGHT.set(0);
      }
  }


}
