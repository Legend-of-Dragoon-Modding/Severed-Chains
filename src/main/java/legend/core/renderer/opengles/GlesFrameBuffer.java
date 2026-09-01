package legend.core.renderer.opengles;

import legend.core.renderer.FrameBuffer;
import legend.core.renderer.FrameBufferAttachment;

import static org.lwjgl.opengles.GLES20.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengles.GLES20.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengles.GLES20.GL_FRAMEBUFFER;
import static org.lwjgl.opengles.GLES20.GL_FRAMEBUFFER_BINDING;
import static org.lwjgl.opengles.GLES20.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE_2D;
import static org.lwjgl.opengles.GLES20.glBindFramebuffer;
import static org.lwjgl.opengles.GLES20.glCheckFramebufferStatus;
import static org.lwjgl.opengles.GLES20.glDeleteFramebuffers;
import static org.lwjgl.opengles.GLES20.glFramebufferTexture2D;
import static org.lwjgl.opengles.GLES20.glGenFramebuffers;
import static org.lwjgl.opengles.GLES20.glGetInteger;

public class GlesFrameBuffer implements FrameBuffer {
  private final int id;

  GlesFrameBuffer(final FrameBufferAttachment[] attachments) {
    final int oldFramebuffer = glGetInteger(GL_FRAMEBUFFER_BINDING);

    this.id = glGenFramebuffers();
    this.bind();

    for(final FrameBufferAttachment attachment : attachments) {
      final int attachmentType = switch(attachment.type) {
        case COLOUR -> GL_COLOR_ATTACHMENT0;
        case DEPTH -> GL_DEPTH_ATTACHMENT;
      };

      glFramebufferTexture2D(GL_FRAMEBUFFER, attachmentType, GL_TEXTURE_2D, ((GlesTexture)attachment.texture).id, 0);
    }

    if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
      throw new RuntimeException("Render buffer is not complete!");
    }

    glBindFramebuffer(GL_FRAMEBUFFER, oldFramebuffer);
  }

  @Override
  public void delete() {
    glDeleteFramebuffers(this.id);
  }

  @Override
  public void bind() {
    glBindFramebuffer(GL_FRAMEBUFFER, this.id);
  }

}
