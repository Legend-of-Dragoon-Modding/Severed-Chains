package legend.core.renderer;

import java.util.function.Consumer;

import static legend.core.GameEngine.RENDERER;

public interface FrameBuffer {
  static FrameBuffer create(final String name, final Consumer<FrameBufferBuilder> callback) {
    final FrameBufferBuilder builder = new FrameBufferBuilder();
    callback.accept(builder);
    return RENDERER.api().makeFrameBuffer(name, builder.getAttachments());
  }

  void delete();
  void bind();
}
