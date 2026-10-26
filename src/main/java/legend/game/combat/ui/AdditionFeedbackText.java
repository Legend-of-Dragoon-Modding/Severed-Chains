package legend.game.combat.ui;

import legend.core.renderer.Translucency;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import org.apache.commons.lang3.StringUtils;

import static legend.game.Text.renderText;

public class AdditionFeedbackText {
  final static int DECAY_FRAME_INDEX = 30;
  final static float TEXT_POSITION_X = 287f;
  final static float TEXT_POSITION_Y = 138f;

  final static FontOptions defaultFont = new FontOptions().size(1.1f).horizontalAlign(HorizontalAlign.CENTRE);

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
    public int frameIndex;
    public FeedbackTextFrameData[] frames;
    public AdditionFeedbackValue feedback;
    public String text;
    public FontOptions font;

    private int translucentFrameCount;

    public FeedbackTextElement(final AdditionFeedbackValue feedback) {
      this.feedback = feedback;
      this.setAnimationFrames();
      this.setText();
    }

    private void setText()
    {
      String value = this.feedback.toString();

      switch(this.feedback) {
        case AdditionFeedbackValue.GOOD:
          this.font = defaultFont.colour(TextColour.NEON_GREEN).shadowColour(TextColour.BLACK_0);
          value = AdditionFeedbackValue.GOOD.toString();
          break;
        case AdditionFeedbackValue.GOOD_MINUS:
          this.font = defaultFont.colour(TextColour.NEON_GREEN).shadowColour(TextColour.BLACK_0);
          value = AdditionFeedbackValue.GOOD.toString() + '-';
          break;
        case AdditionFeedbackValue.GOOD_PLUS:
          this.font = defaultFont.colour(TextColour.NEON_GREEN).shadowColour(TextColour.BLACK_0);
          value = AdditionFeedbackValue.GOOD.toString() + '+';
          break;
        case AdditionFeedbackValue.EARLY:
          this.font = defaultFont.colour(TextColour.MAGENTA).shadowColour(TextColour.BLACK_0);
          break;
        case AdditionFeedbackValue.LATE, AdditionFeedbackValue.NO_PRESS:
          this.font = defaultFont.colour(TextColour.RED_ORANGE).shadowColour(TextColour.BLACK_0);
          value = AdditionFeedbackValue.LATE.toString();
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

      this.text = StringUtils.capitalize(value.toLowerCase()); //LANG.getTranslation("addition_feedback_value_%s", feedback.toString());
    }

    public boolean hasRemainingFrames() {
      return this.frameIndex < this.frames.length;
    }

    public FeedbackTextFrameData getFrame() {
      return this.frames[this.frameIndex++];
    }

    private void setAnimationFrames() {
      this.frames = new FeedbackTextFrameData[this.feedback == AdditionFeedbackValue.FLAWLESS ? 45 : 35];

      for (int i = 0; i < 10; i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, Math.max(TEXT_POSITION_Y, TEXT_POSITION_Y + 15 - i * 3), i < 3 ? Translucency.B_PLUS_QUARTER_F : this.getTranslucency());
      }

      for (int i = 0; i < (this.feedback == AdditionFeedbackValue.FLAWLESS ? 30 : 20); i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, TEXT_POSITION_Y, this.getTranslucency());
      }

      for (int i = 0; i < 5; i++) {
        this.frames[this.frameIndex++] = new FeedbackTextFrameData(TEXT_POSITION_X, TEXT_POSITION_Y - i * 6, i > 2 ? Translucency.B_PLUS_QUARTER_F : Translucency.B_PLUS_F);
      }

      this.frameIndex = 0;
    }

    private Translucency getTranslucency() {
      Translucency t = Translucency.B_PLUS_F;
      if (this.feedback == AdditionFeedbackValue.FLAWLESS) {
        t = this.translucentFrameCount >= 2 ? Translucency.B_PLUS_QUARTER_F : Translucency.B_PLUS_F;
        this.translucentFrameCount = this.translucentFrameCount > 2 ? 0 : this.translucentFrameCount + 1;
      }
      return t;
    }
  }

  private FeedbackTextElement[] feedbackTextElements;

  public AdditionFeedbackText() {
    this.feedbackTextElements = new FeedbackTextElement[8];
  }

  public void initializeFeedbackTextElements(final int length) {
    this.feedbackTextElements = new FeedbackTextElement[length];
  }

  public void setFeedbackTextElement(final int index, final AdditionFeedbackValue feedback) {
    this.feedbackTextElements[index] = new FeedbackTextElement(feedback);

    if (index > 0 && this.feedbackTextElements[index - 1].frameIndex < DECAY_FRAME_INDEX) {
      this.feedbackTextElements[index - 1].frameIndex = DECAY_FRAME_INDEX;
    }
  }

  public void renderAdditionFeedbackText() {
    for (int i = 0; i < this.feedbackTextElements.length; i++) {
      final FeedbackTextElement element = this.feedbackTextElements[i];
      if (element != null && element.hasRemainingFrames()) {
        final FeedbackTextFrameData frame = element.getFrame();
        if (frame != null) {
          renderText(element.text, frame.x, frame.y, element.font);
        }
      }
    }
  }
}
