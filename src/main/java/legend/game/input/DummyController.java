package legend.game.input;

public class DummyController extends Controller {
  @Override
  public void poll() {
    for(int i = 0; i < this.bindings.size(); i++) {
      this.bindings.get(i).poll();
    }
  }

  @Override
  public boolean readButton(final int input) {
    return false;
  }

  @Override
  public float readAxis(final int input) {
    return 0;
  }

  @Override
  public boolean readHat(final int input, final int hatIndex) {
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
