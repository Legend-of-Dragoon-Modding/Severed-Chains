package legend.game.combat.ui;

import legend.core.renderer.Translucency;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;

import static legend.game.Text.renderText;

public class AdditionFeedbackText {
  final static int DECAY_FRAME = 30;
  final static float TEXT_POSITION_X = 287.0f;
  final static float TEXT_POSITION_Y = 138.0f;
  final static FontOptions defaultFont = new FontOptions().size(1.1f).horizontalAlign(HorizontalAlign.CENTRE);

  private FeedbackTextElement[] feedbackTextElements;

  private static class FeedbackTextFrameData {
    public float x;
    public float y;
    public Translucency translucency;
    public FeedbackTextFrameData(final float x, final float y, final Translucency translucency) {
      this.x = x;
      this.y = y;
      this.translucency = translucency;
    }
  }

  private static class FeedbackTextElement {
    public int currentFrame;
    public FeedbackTextFrameData[] frames;
    public AdditionFeedbackValue value;
    public String text;
    public FontOptions font;

    private int translucentFrameCount;

    public FeedbackTextElement(final AdditionFeedbackValue value) {
      this.value = value;
      this.setAnimationFrames();
      this.setText();
    }

    private void setText()
    {
      switch(this.value) {
        case AdditionFeedbackValue.GOOD, AdditionFeedbackValue.GOOD_MINUS, AdditionFeedbackValue.GOOD_PLUS:
          this.font = defaultFont.colour(TextColour.NEON_GREEN).shadowColour(TextColour.BLACK_0);
          break;
        case AdditionFeedbackValue.EARLY:
          this.font = defaultFont.colour(TextColour.MAGENTA).shadowColour(TextColour.BLACK_0);
          break;
        case AdditionFeedbackValue.LATE, AdditionFeedbackValue.NO_PRESS:
          this.font = defaultFont.colour(TextColour.RED_ORANGE).shadowColour(TextColour.BLACK_0);
          break;
        case AdditionFeedbackValue.PERFECT:
          this.font = defaultFont.colour(TextColour.VIVID_BLUE).shadowColour(TextColour.BLACK_0);
          break;
        case AdditionFeedbackValue.COUNTER:
          this.font = defaultFont.colour(TextColour.YELLOW_GOLD).shadowColour(TextColour.BLACK_0);
          break;
        case AdditionFeedbackValue.FLAWLESS:
          this.font = defaultFont.colour(TextColour.WHITE_255).shadowColour(TextColour.PURE_RED);
          break;
        case AdditionFeedbackValue.WRONG:
          this.font = defaultFont.colour(TextColour.GREY_90).shadowColour(TextColour.BLACK_0);
          break;
      }
      this.text = I18n.translate("lod.addition_feedback." + this.value.toString().toLowerCase());
    }

    private boolean hasRemainingFrames() {
      return this.currentFrame < this.frames.length;
    }

    private void setAnimationFrames() {
      this.frames = new FeedbackTextFrameData[this.value == AdditionFeedbackValue.FLAWLESS ? 45 : 35];

      for (int i = 0; i < 10; i++) {
        this.frames[this.currentFrame++] = new FeedbackTextFrameData(TEXT_POSITION_X, Math.max(TEXT_POSITION_Y, TEXT_POSITION_Y + 15 - i * 3), i < 3 ? Translucency.B_PLUS_QUARTER_F : this.getTranslucency());
      }

      for (int i = 0; i < (this.value == AdditionFeedbackValue.FLAWLESS ? 30 : 20); i++) {
        this.frames[this.currentFrame++] = new FeedbackTextFrameData(TEXT_POSITION_X, TEXT_POSITION_Y, this.getTranslucency());
      }

      for (int i = 0; i < 5; i++) {
        this.frames[this.currentFrame++] = new FeedbackTextFrameData(TEXT_POSITION_X, TEXT_POSITION_Y - i * 6, i > 2 ? Translucency.B_PLUS_QUARTER_F : Translucency.B_PLUS_F);
      }

      this.currentFrame = 0;
    }

    private Translucency getTranslucency() {
      Translucency t = Translucency.B_PLUS_F;
      if (this.value == AdditionFeedbackValue.FLAWLESS) {
        t = this.translucentFrameCount >= 2 ? Translucency.B_PLUS_QUARTER_F : Translucency.B_PLUS_F;
        this.translucentFrameCount = this.translucentFrameCount > 2 ? 0 : this.translucentFrameCount + 1;
      }
      return t;
    }
  }

  public AdditionFeedbackText() {
    this.feedbackTextElements = new FeedbackTextElement[8];
  }

  public void initializeFeedbackTextElements(final int length) {
    this.feedbackTextElements = new FeedbackTextElement[length];
  }

  public void setFeedbackTextElement(final int frame, final AdditionFeedbackValue feedback) {
    this.feedbackTextElements[frame] = new FeedbackTextElement(feedback);

    if (frame > 0 && this.feedbackTextElements[frame - 1].currentFrame < DECAY_FRAME) {
      this.feedbackTextElements[frame - 1].currentFrame = DECAY_FRAME;
    }
  }

  public void renderAdditionFeedbackText() {
    for (int i = 0; i < this.feedbackTextElements.length; i++) {
      final FeedbackTextElement element = this.feedbackTextElements[i];
      if (element != null && element.hasRemainingFrames()) {
        final FeedbackTextFrameData frame = element.frames[element.currentFrame++];
        if (frame != null) {
          renderText(element.text, frame.x, frame.y, element.font.translucency(frame.translucency));
        }
      }
    }
  }
}
