package legend.game.types;

import legend.core.lang.TextComponent;
import legend.game.inventory.screens.controls.Highlight;

public class MessageBox20 {
  public String[] text_00;
  public Highlight highlightRenderable_04;
  public Renderable58 backgroundRenderable_08;
  public int state_0c;

  /** The number of frames the messagebox has been displayed */
  public int ticks_10;

  public MessageBoxType type_15;

  public int menuIndex_18;
  public int x_1c;
  public int y_1e;

  public MessageBoxResult result = MessageBoxResult.AWAITING_INPUT;

  public TextComponent yes;
  public TextComponent no;

  public boolean ignoreInput;

  public void delete() {
    if(this.highlightRenderable_04 != null) {
      this.highlightRenderable_04.delete();
      this.highlightRenderable_04 = null;
    }
  }
}
