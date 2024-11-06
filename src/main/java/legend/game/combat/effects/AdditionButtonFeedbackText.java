package legend.game.combat.effects;

import legend.core.gpu.Bpp;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.game.types.Translucency;
import org.joml.Matrix4f;
import org.joml.Vector2d;

import java.nio.file.Path;

import static legend.core.GameEngine.RENDERER;

public class AdditionButtonFeedbackText {

  final static int DECAY_FRAME_INDEX = 30;
  final static float TEXT_POSITION_X = 236f;
  final static float TEXT_POSITION_Y = 138f;

  private class FeedbackTextFrameData {
    public float x;
    public float y;
    public Translucency translucency;
    public FeedbackTextFrameData(final float x, final float y, Translucency translucency) {
      this.x = x;
      this.y = y;
      this.translucency = translucency;
    }
  }

  private class FeedbackTextElement {
    public int frameIndex;
    public FeedbackTextFrameData[] frames;
    public int feedbackIndex;

    private int translucentFrameCount;

    public FeedbackTextElement(final int feedbackIndex) {
      this.feedbackIndex = feedbackIndex;
      this.setAnimationFrames();
    }

    public boolean hasRemainingFrames() {
      return this.frameIndex < this.frames.length;
    }

    public FeedbackTextFrameData getFrame() {
      return this.frames[this.frameIndex++];
    }

    private void setAnimationFrames() {
      this.frames = new FeedbackTextFrameData[this.feedbackIndex == 3 ? 45 : 35];

      for (int i = 0; i < 10; i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, Math.max(TEXT_POSITION_Y, TEXT_POSITION_Y + 15 - i * 3), i < 3 ? Translucency.B_PLUS_QUARTER_F : this.getTranslucency());
      }

      for (int i = 0; i < (this.feedbackIndex == 3 ? 30 : 20); i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, TEXT_POSITION_Y, this.getTranslucency());
      }

      for (int i = 0; i < 5; i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, TEXT_POSITION_Y - i * 6, i > 2 ? Translucency.B_PLUS_QUARTER_F : Translucency.B_PLUS_F);
      }

      this.frameIndex = 0;
    }

    private Translucency getTranslucency() {
      Translucency t = Translucency.B_PLUS_F;
      if (this.feedbackIndex == 3) {
        t = this.translucentFrameCount >= 2 ? Translucency.B_PLUS_QUARTER_F : Translucency.B_PLUS_F;
        this.translucentFrameCount = this.translucentFrameCount > 2 ? 0 : this.translucentFrameCount + 1;
      }
      return t;
    }
  }

  final private Obj additionButtonTextQuad;
  final private Texture[] additionButtonTextTextures;
  private FeedbackTextElement[] feedbackTextElements;
  final Matrix4f transformMatrix;

  public AdditionButtonFeedbackText() {
    this.feedbackTextElements = new FeedbackTextElement[8];
    this.transformMatrix = new Matrix4f();

    this.additionButtonTextQuad = new QuadBuilder("Addition Button Text background")
      .rgb(1.0f, 1.0f, 1.0f)
      .size(1.0f, 1.0f)
      .uv(0.0f, 0.0f)
      .uvSize(1.0f, 1.0f)
      .bpp(Bpp.BITS_24)
      .build();

    this.additionButtonTextTextures = new Texture[] {
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Good.png")),
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Early.png")),
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Late.png")),
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Perfect.png")),
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Counter.png")),
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Flawless.png"))
    };
  }

  public void initializeFeedbackTextElements(final int length) {
    this.feedbackTextElements = new FeedbackTextElement[length];
  }

  public void setFeedbackTextElement(final int index, final int feedbackIndex) {
    this.feedbackTextElements[index] = new FeedbackTextElement(feedbackIndex);

    if (index > 0 && this.feedbackTextElements[index - 1].frameIndex < DECAY_FRAME_INDEX) {
      this.feedbackTextElements[index - 1].frameIndex = DECAY_FRAME_INDEX;
    }
  }

  public void renderAdditionFeedbackChar() {

    for (int i = 0; i < this.feedbackTextElements.length; i++) {
      final FeedbackTextElement element = this.feedbackTextElements[i];
      if (element != null && element.hasRemainingFrames()) {
        final FeedbackTextFrameData frame = element.getFrame();
        if (frame != null) {
          this.transformMatrix.translation(frame.x + RENDERER.getWidescreenOrthoOffsetX(), frame.y, i);
          this.transformMatrix.scale(100f, 22f, 1f);

          RENDERER
            .queueOrthoModel(this.additionButtonTextQuad, this.transformMatrix)
            .texture(this.GetTexture(element.feedbackIndex))
            .translucency(frame.translucency); //HALF_B_PLUS_HALF_F slightly transparent, B_PLUS_F slightly transparent without black
        }
      }
    }
  }

  //Failed Counter = -4, Late = -3, Early = -2, No Press = -1, None = 0, Success = 1, Perfect = 2, Flawless = 3
  private Texture GetTexture(final int feedbackIndex) {
    return switch(feedbackIndex) {
      case -4 -> this.additionButtonTextTextures[4];
      case -3, -1 -> this.additionButtonTextTextures[2];
      case -2 -> this.additionButtonTextTextures[1];
      case 1 -> this.additionButtonTextTextures[0];
      case 2 -> this.additionButtonTextTextures[3];
      case 3 -> this.additionButtonTextTextures[5];
      default -> null;
    };
  }
}
