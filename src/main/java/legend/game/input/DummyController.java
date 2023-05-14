package legend.game.input;

public class DummyController extends Controller {
  private boolean activityThisFrame;

  @Override
  public void poll() {
    this.activityThisFrame = false;

    for(final InputBinding binding : this.bindings) {
      binding.poll();

      if(binding.getState() == InputBindingState.PRESSED_THIS_FRAME) {
        this.activityThisFrame = true;
      }
    }
  }

  @Override
  public boolean readButton(final int glfwCode) {
    return false;
  }

  @Override
  public float readAxis(final int glfwCode) {
    return 0;
  }

  @Override
  public boolean readHat(final int glfwCode, final int hatIndex) {
    return false;
  }
}
