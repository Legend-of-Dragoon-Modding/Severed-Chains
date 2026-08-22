package legend.core.renderer;

import java.util.ArrayList;
import java.util.List;

public class FrameBufferBuilder {
  private final List<FrameBufferAttachment> attachments = new ArrayList<>();

  public void attachment(final FrameBufferAttachmentType type, final Texture texture) {
    this.attachments.add(new FrameBufferAttachment(type, texture));
  }

  public FrameBufferAttachment[] getAttachments() {
    return this.attachments.toArray(FrameBufferAttachment[]::new);
  }
}
