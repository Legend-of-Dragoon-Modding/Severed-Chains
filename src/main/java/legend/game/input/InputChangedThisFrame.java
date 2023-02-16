package legend.game.input;

public interface InputChangedThisFrame {
  void onPressedThisFrame(InputKeyCode inputKeyCode);

  void onReleasedThisFrame(InputKeyCode inputKeyCode);
}
