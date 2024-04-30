package legend.game.types;

public class MessageBox20 {
  public String[] text_00;
  public Renderable58 highlightRenderable_04;
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
