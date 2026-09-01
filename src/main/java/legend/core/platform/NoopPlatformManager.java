package legend.core.platform;

import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputAxis;
import legend.core.platform.input.InputButton;
import legend.core.platform.input.InputGamepadType;
import legend.core.platform.input.InputKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NoopPlatformManager extends PlatformManager {
  private final List<Window> windows = new ArrayList<>();

  @Override
  public void init() {

  }

  @Override
  public boolean isContextCurrent() {
    return true;
  }

  @Override
  public boolean hasGamepad() {
    return false;
  }

  @Override
  public InputGamepadType getGamepadType() {
    return InputGamepadType.STANDARD;
  }

  @Override
  public int getMouseButton(final int index) {
    return index;
  }

  @Override
  public String[] listDisplays() {
    return new String[] {"Virtual"};
  }

  @Override
  protected Window createWindow(final String title, final int width, final int height) {
    final Window window = new NoopWindow(this, width, height);
    this.windows.add(window);
    return window;
  }

  @Override
  protected void removeWindows(final Collection<Window> windows) {
    this.windows.removeAll(windows);
  }

  @Override
  public Window getLastWindow() {
    return this.windows.getFirst();
  }

  @Override
  protected void tickInput() {

  }

  @Override
  public void rumble(final float intensity, final int ms) {

  }

  @Override
  public void rumble(final float bigIntensity, final float smallIntensity, final int ms) {

  }

  @Override
  public void adjustRumble(final float intensity, final int ms) {

  }

  @Override
  public void adjustRumble(final float bigIntensity, final float smallIntensity, final int ms) {

  }

  @Override
  public void stopRumble() {

  }

  @Override
  public boolean isActionPressed(final InputAction action) {
    return false;
  }

  @Override
  public boolean isActionRepeat(final InputAction action) {
    return false;
  }

  @Override
  public boolean isActionHeld(final InputAction action) {
    return false;
  }

  @Override
  public float getAxis(final InputAction action) {
    return 0.0f;
  }

  @Override
  public String getKeyName(final InputKey key) {
    return key.toString();
  }

  @Override
  public String getScancodeName(final InputKey key) {
    return key.toString();
  }

  @Override
  public String getButtonName(final InputButton button) {
    return button.toString();
  }

  @Override
  public String getAxisName(final InputAxis axis) {
    return axis.toString();
  }

  @Override
  public void openUrl(final String url) {

  }
}
