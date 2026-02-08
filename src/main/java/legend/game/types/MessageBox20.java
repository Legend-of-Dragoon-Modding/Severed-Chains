package legend.game.types;

import legend.game.inventory.screens.controls.Highlight;

public class MessageBox20 {
  public String[] text_00;
  public Highlight highlightRenderable_04;
  public Renderable58 backgroundRenderable_08;
  public int state_0c;

  /** The number of frames the messagebox has been displayed */
  public int ticks_10;

  public int type_15;

  public int menuIndex_18;
  public int x_1c;
  public int y_1e;

  public MessageBoxResult result = MessageBoxResult.AWAITING_INPUT;

  public String yes;
  public String no;

  public boolean ignoreInput;
}
