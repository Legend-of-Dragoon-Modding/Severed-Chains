package legend.game.input;

public class DummyController extends Controller {
  @Override
  public void poll() {
    for(int i = 0; i < this.bindings.size(); i++) {
      this.bindings.get(i).poll();
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

  @Override
  public String getName() {
    return "No Controller";
  }

  @Override
  public String getGuid() {
    return "";
  }
}
