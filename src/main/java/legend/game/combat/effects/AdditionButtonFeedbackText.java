package legend.game.combat.effects;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.game.types.Translucency;
import org.joml.Matrix4f;

import java.nio.file.Path;

import static legend.core.GameEngine.RENDERER;

public class AdditionButtonFeedbackText {

  public static Texture[] xboxAFrames = new Texture[] {
    Texture.png(Path.of("gfx", "ui", "Large_Button_Xbox_A_0.png")),
    Texture.png(Path.of("gfx", "ui", "Large_Button_Xbox_A_1.png")),
    Texture.png(Path.of("gfx", "ui", "Large_Button_Xbox_A_2.png"))
  };

  public static Texture[] xboxBFrames = new Texture[] {
    Texture.png(Path.of("gfx", "ui", "Large_Button_Xbox_B_0.png")),
    Texture.png(Path.of("gfx", "ui", "Large_Button_Xbox_B_1.png")),
    Texture.png(Path.of("gfx", "ui", "Large_Button_Xbox_B_2.png"))
  };

  public static Texture[] nintendoAFrames = new Texture[] {
    Texture.png(Path.of("gfx", "ui", "Large_Button_Nintendo_A_0.png")),
    Texture.png(Path.of("gfx", "ui", "Large_Button_Nintendo_A_1.png")),
    Texture.png(Path.of("gfx", "ui", "Large_Button_Nintendo_A_2.png"))
  };

  public static Texture[] nintendoBFrames = new Texture[] {
    Texture.png(Path.of("gfx", "ui", "Large_Button_Nintendo_B_0.png")),
    Texture.png(Path.of("gfx", "ui", "Large_Button_Nintendo_B_1.png")),
    Texture.png(Path.of("gfx", "ui", "Large_Button_Nintendo_B_2.png"))
  };

  final static int DECAY_FRAME_INDEX = 30;
  final static float TEXT_POSITION_X = 236f;
  final static float TEXT_POSITION_Y = 138f;

  private class FeedbackTextFrameData {
    public float x;
    public float y;
    public Translucency translucency;
    public FeedbackTextFrameData(final float x, final float y, final Translucency translucency) {
      this.x = x;
      this.y = y;
      this.translucency = translucency;
    }
  }

  private class FeedbackTextElement {
    public int frameIndex;
    public FeedbackTextFrameData[] frames;
    public AdditionButtonFeedback feedback;

    private int translucentFrameCount;

    public FeedbackTextElement(final AdditionButtonFeedback feedback) {
      this.feedback = feedback;
      this.setAnimationFrames();
    }

    public boolean hasRemainingFrames() {
      return this.frameIndex < this.frames.length;
    }

    public FeedbackTextFrameData getFrame() {
      return this.frames[this.frameIndex++];
    }

    private void setAnimationFrames() {
      this.frames = new FeedbackTextFrameData[this.feedback == AdditionButtonFeedback.FLAWLESS ? 45 : 35];

      for (int i = 0; i < 10; i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, Math.max(TEXT_POSITION_Y, TEXT_POSITION_Y + 15 - i * 3), i < 3 ? Translucency.B_PLUS_QUARTER_F : this.getTranslucency());
      }

      for (int i = 0; i < (this.feedback == AdditionButtonFeedback.FLAWLESS ? 30 : 20); i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, TEXT_POSITION_Y, this.getTranslucency());
      }

      for (int i = 0; i < 5; i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, TEXT_POSITION_Y - i * 6, i > 2 ? Translucency.B_PLUS_QUARTER_F : Translucency.B_PLUS_F);
      }

      this.frameIndex = 0;
    }

    private Translucency getTranslucency() {
      Translucency t = Translucency.B_PLUS_F;
      if (this.feedback == AdditionButtonFeedback.FLAWLESS) {
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
      .rgb(0.75f, 0.75f, 0.75f)
      .size(1.0f, 1.0f)
      .uv(0.0f, 0.0f)
      .uvSize(1.0f, 1.0f)
      .bpp(Bpp.BITS_24)
      .build();

    this.additionButtonTextTextures = new Texture[] {
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Good.png")),     //0
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Early.png")),    //1
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Late.png")),     //2
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Perfect.png")),  //3
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Counter.png")),  //4
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Flawless.png")), //5
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Good-.png")),    //6
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Good+.png")),    //7
      Texture.png(Path.of("gfx", "ui", "additionFeedbackText_Wrong.png"))     //8
    };
  }

  private Texture getTexture(final AdditionButtonFeedback feedback) {
    return switch(feedback) {
      case AdditionButtonFeedback.GOOD -> this.additionButtonTextTextures[0];
      case AdditionButtonFeedback.EARLY -> this.additionButtonTextTextures[1];
      case AdditionButtonFeedback.LATE, AdditionButtonFeedback.NO_PRESS -> this.additionButtonTextTextures[2];
      case AdditionButtonFeedback.PERFECT -> this.additionButtonTextTextures[3];
      case AdditionButtonFeedback.COUNTER -> this.additionButtonTextTextures[4];
      case AdditionButtonFeedback.FLAWLESS -> this.additionButtonTextTextures[5];
      case AdditionButtonFeedback.GOOD_MINUS -> this.additionButtonTextTextures[6];
      case AdditionButtonFeedback.GOOD_PLUS -> this.additionButtonTextTextures[7];
      case AdditionButtonFeedback.WRONG -> this.additionButtonTextTextures[8];
      default -> null;
    };
  }

  public void initializeFeedbackTextElements(final int length) {
    this.feedbackTextElements = new FeedbackTextElement[length];
  }

  public void setFeedbackTextElement(final int index, final AdditionButtonFeedback feedback) {
    this.feedbackTextElements[index] = new FeedbackTextElement(feedback);

    if (index > 0 && this.feedbackTextElements[index - 1].frameIndex < DECAY_FRAME_INDEX) {
      this.feedbackTextElements[index - 1].frameIndex = DECAY_FRAME_INDEX;
    }
  }

  public void renderAdditionFeedbackTexture() {
    for (int i = 0; i < this.feedbackTextElements.length; i++) {
      final FeedbackTextElement element = this.feedbackTextElements[i];
      if (element != null && element.hasRemainingFrames()) {
        final FeedbackTextFrameData frame = element.getFrame();
        if (frame != null) {
          this.transformMatrix.translation(frame.x + RENDERER.getWidescreenOrthoOffsetX(), frame.y, i);
          this.transformMatrix.scale(100f, 22f, 1f);

          RENDERER
            .queueOrthoModel(this.additionButtonTextQuad, this.transformMatrix, QueuedModelStandard.class)
            .texture(this.getTexture(element.feedback))
            .translucency(frame.translucency); //HALF_B_PLUS_HALF_F slightly transparent, B_PLUS_F slightly transparent without black
        }
      }
    }
  }
}
