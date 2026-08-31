package legend.core.platform;

import legend.core.platform.input.InputClass;
import legend.core.renderer.RenderApi;
import legend.core.renderer.noop.NoopApi;

import java.nio.file.Path;

public class NoopWindow extends Window {
  private final PlatformManager manager;

  private final int width;
  private final int height;
  private boolean shouldClose;

  private final Action render;

  public NoopWindow(final PlatformManager manager, final int width, final int height) {
    this.manager = manager;
    this.width = width;
    this.height = height;
    this.render = this.manager.addAction(new Action(this::tick, 60));
  }

  @Override
  public RenderApi getRenderApi() {
    return new NoopApi();
  }

  @Override
  protected void destroy() {
    this.manager.removeAction(this.render);
    this.events().onClose();
  }

  @Override
  protected boolean shouldClose() {
    return this.shouldClose;
  }

  @Override
  public void show() {
    this.events().onResize(this.width, this.height);
  }

  @Override
  public void close() {
    this.shouldClose = true;
  }

  @Override
  public void updateMonitor() {

  }

  @Override
  public void makeFullscreen() {

  }

  @Override
  public void makeWindowed() {

  }

  @Override
  public void centerWindow() {

  }

  @Override
  public void setTitle(final String title) {

  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public boolean hasFocus() {
    return true;
  }

  @Override
  public InputClass getInputClass() {
    return InputClass.KEYBOARD;
  }

  @Override
  public void startTextInput() {

  }

  @Override
  public void stopTextInput() {

  }

  @Override
  public void disableCursor() {

  }

  @Override
  public void showCursor() {

  }

  @Override
  public void hideCursor() {

  }

  @Override
  public void useNormalCursor() {

  }

  @Override
  public void usePointerCursor() {

  }

  @Override
  public void setWindowIcon(final Path path) {

  }

  @Override
  public void setFpsLimit(final int limit) {
    this.render.setExpectedFps(limit);
  }

  @Override
  public int getFpsLimit() {
    return this.render.getExpectedFps();
  }

  private void tick() {
    this.events().onDraw();
    this.manager.clearPressed();
  }
}
