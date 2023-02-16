package legend.game.input;

import static legend.game.unpacker.Unpacker.LOGGER;

public class TestInput_EventStyle implements InputChangedThisFrame {
  @Override
  public void onPressedThisFrame(final InputKeyCode inputKeyCode) {
    if(inputKeyCode == InputKeyCode.BUTTON_EAST) {
      LOGGER.info("EAST PRESSED TRIGGERED VIA EVENT: ");
    }
  }

  @Override
  public void onReleasedThisFrame(final InputKeyCode inputKeyCode) {
    if(inputKeyCode == InputKeyCode.BUTTON_EAST) {
      LOGGER.info("EAST RELEASED TRIGGERED VIA EVENT: ");
    }
  }
}
