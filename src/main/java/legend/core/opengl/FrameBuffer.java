package legend.core.opengl;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glReadBuffer;
import static org.lwjgl.opengl.GL20C.glDrawBuffers;
import static org.lwjgl.opengl.GL30C.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30C.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30C.glBindFramebuffer;
import static org.lwjgl.opengl.GL30C.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30C.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30C.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30C.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30C.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30C.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30C.glGenFramebuffers;
import static org.lwjgl.opengl.GL30C.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30C.glRenderbufferStorage;

public class FrameBuffer {
  public static void unbind() {
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
  }

  public static FrameBuffer create(final Consumer<Builder> callback) {
    final Builder builder = new Builder();

    callback.accept(builder);
    return builder.build();
  }

  private final int id;
  private final int renderBufferId;

  private FrameBuffer(final int[] attachmentIds, final int[] attachmentTypes, final int[] drawBuffers, final int readBuffer, final int rbStorageFormat, final int rbAttachment, final int rbWidth, final int rbHeight) {
    this.id = glGenFramebuffers();
    this.bind();

    for(int i = 0; i < attachmentTypes.length; i++) {
      glFramebufferTexture2D(GL_FRAMEBUFFER, attachmentTypes[i], GL_TEXTURE_2D, attachmentIds[i], 0);
    }

    if(drawBuffers.length != 0) {
      glDrawBuffers(drawBuffers);
    }

    if(readBuffer != 0) {
      glReadBuffer(readBuffer);
    }

    if(rbStorageFormat != 0) {
      this.renderBufferId = glGenRenderbuffers();
      glBindRenderbuffer(GL_RENDERBUFFER, this.renderBufferId);
      glRenderbufferStorage(GL_RENDERBUFFER, rbStorageFormat, rbWidth, rbHeight);
      glBindRenderbuffer(GL_RENDERBUFFER, 0);

      glFramebufferRenderbuffer(GL_FRAMEBUFFER, rbAttachment, GL_RENDERBUFFER, this.renderBufferId);
    } else {
      this.renderBufferId = 0;
    }

    if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
      throw new RuntimeException("Render buffer is not complete!");
    }

    unbind();
  }

  public void delete() {
    glDeleteFramebuffers(this.id);

    if(this.renderBufferId != 0) {
      glDeleteRenderbuffers(this.id);
    }
  }

  public void bind() {
    glBindFramebuffer(GL_FRAMEBUFFER, this.id);
  }

  public static class Builder {
    private final IntList attachmentIds = new IntArrayList();
    private final IntList attachmentTypes = new IntArrayList();
    private final IntList drawBuffers = new IntArrayList();
    private int readBuffer;
    private int rbStorageFormat;
    private int rbAttachment;
    private int rbWidth;
    private int rbHeight;

    public void attachment(final Texture texture) {
      this.attachment(texture, GL_COLOR_ATTACHMENT0);
    }

    public void attachment(final Texture texture, final int attachment) {
      this.attachmentIds.add(texture.id);
      this.attachmentTypes.add(attachment);
    }

    public void drawBuffer(final int drawBuffer) {
      this.drawBuffers.add(drawBuffer);
    }

    public void readBuffer(final int readBuffer) {
      this.readBuffer = readBuffer;
    }

    public void renderBuffer(final int storageFormat, final int attachment, final int width, final int height) {
      this.rbStorageFormat = storageFormat;
      this.rbAttachment = attachment;
      this.rbWidth = width;
      this.rbHeight = height;
    }

    FrameBuffer build() {
      return new FrameBuffer(
        this.attachmentIds.toIntArray(),
        this.attachmentTypes.toIntArray(),
        this.drawBuffers.toIntArray(),
        this.readBuffer,
        this.rbStorageFormat,
        this.rbAttachment,
        this.rbWidth,
        this.rbHeight
      );
    }
  }
}
