package legend.core.renderer;

public class FrameBufferAttachment {
  public final FrameBufferAttachmentType type;
  public final Texture texture;

  public FrameBufferAttachment(final FrameBufferAttachmentType type, final Texture texture) {
    this.type = type;
    this.texture = texture;
  }
}
