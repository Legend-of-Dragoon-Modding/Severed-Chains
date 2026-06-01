package legend.game.types;

import legend.core.gte.MV;
import legend.core.platform.input.InputAction;
import legend.game.inventory.screens.TextColour;

import java.util.ArrayList;
import java.util.List;

import static legend.game.EngineStates.currentEngineState_8004dd04;

public class TextboxText84 {
  public static final int END = 0xa0;
  public static final int LINE = 0xa1;
  /** Waits until the X button is pressed */
  public static final int BUTTON = 0xa2;
  public static final int MUTLIBOX = 0xa3;
  public static final int SPEED = 0xa5;
  /** Pauses for N ticks */
  public static final int PAUSE = 0xa6;
  public static final int COLOUR = 0xa7;
  public static final int VAR = 0xa8;
  public static final int X_OFFSET = 0xad;
  public static final int Y_OFFSET = 0xae;
  public static final int SAUTO = 0xb0;
  public static final int ELEMENT = 0xb1;
  public static final int ARROW = 0xb2;
  public static final int ACTION = 0xc0;

  public static final int INITIALIZED = 0x1;
  public static final int TEXT_PARSE_COMPLETE = 0x2;
  public static final int SCROLL_ACTION_COMPLETE = 0x4;
  public static final int PERMIT_SCROLL_INPUT_ADVANCE = 0x8;
  public static final int SHOW_VAR = 0x10;
  public static final int NO_INPUT = 0x20;
  public static final int SKIP_SMAP_COUNTDOWN_OR_FINISH_BATTLE_TEXTBOX = 0x40;
  public static final int CHECK_SCROLL_ACTION = 0x80;
  public static final int IGNORE_SCROLL_LINE_BREAK = 0x100;
  public static final int HAS_NAME = 0x200;
  public static final int PROCESSED_NEW_LINE = 0x400;
  public static final int SELECTION = 0x800;
  public static final int SHOW_ARROW = 0x1000;
  /**
   * Added option to remove arrows. Used for special chest in phantom ship.
   */
  public static final int NO_ARROW = 0x2000;

  public TextboxTextState state_00;
  /**
   * <ul>
   *   <li>2 - I think this scrolls up instead of down</li>
   *   <li>4 - named textbox</li>
   * </ul>
   */
  public int type_04;

  /**
   * <ul>
   *   <li>0x1 - has the textbox type been initialized</li>
   *   <li>0x2 - text parsing has completed</li>
   *   <li>0x4 - scroll action has completed</li>
   *   <li>0x8 - permit scroll input advance of textbox in no input textboxes</li>
   *   <li>0x10 - textbox is displaying a var</li>
   *   <li>0x20 - disables being able to advance the textbox</li>
   *   <li>0x40 - skip smap no input textbox timer or end no input textbox in battle immediately</li>
   *   <li>0x80 - check scroll action</li>
   *   <li>0x100 - bypass normal scroll flow</li>
   *   <li>0x200 - textbox has a name (yellow text at the top)</li>
   *   <li>0x400 - processed a new line</li>
   *   <li>0x800 - textbox has selection</li>
   *   <li>0x1000 - show textbox arrow</li>
   * </ul>
   */
  public int flags_08;
  public int z_0c;

  public float x_14;
  public float y_16;
  public float _18;
  public float _1a;
  public int chars_1c;
  public int lines_1e;

  public LodString str_24;

  public TextColour textColour_28 = TextColour.WHITE;

  public float scrollSpeed_2a;
  /** Adjusts the Y value of the text as the box is scrolling */
  public float scrollAmount_2c;

  public int charIndex_30;

  public int charX_34;
  public int charY_36;

  /** Number of additional lines scrolled (first scroll is not counted) */
  public int linesScrolled_3a;
//  public int _3c;
  public int blockTimer_3e;
  public int blockTimeLeft_40;

  public int pauseTimer_44;
  public final int[] digits_46 = new int[10];
  public TextboxChar08[] chars_58;
  public TextboxTextState _5c;
  /** Which line of text is selected (i.e. inns) (unknown how this differs from {@link #selectionLine_68} */
  public int selectionLine_60;
  public int ticksUntilStateTransition_64;
  /** Which line of text is selected (i.e. inns) (unknown how this differs from {@link #selectionLine_60} */
  public int selectionLine_68;
  /** The selected line index, relative to the start of the list (i.e. if the list starts at line 1, and the selected line is 1, this value will be 0 */
  public int selectionIndex_6c;
  /** The absolute line index of the last selectable line */
  public int maxSelectionLine_70;
  /** The absolute line index of the first selectable line */
  public int minSelectionLine_72;

  /** The state to change to after {@link #ticksUntilStateTransition_64} countdown finishes */
  public TextboxTextState stateAfterTransition_78;
  public int element_7c;
  public int digitIndex_80;

  public final MV transforms = new MV();

  public int waitTicks;

  public final List<InputAction> inputActions = new ArrayList<>();

  public void clear() {
    this.state_00 = TextboxTextState.INIT_TEXTBOX_TYPE_1;
    this.flags_08 = 0;
    this.z_0c = 13;
    this.textColour_28 = TextColour.WHITE;
    this.scrollSpeed_2a = 2.0f / currentEngineState_8004dd04.tickMultiplier();
    this.scrollAmount_2c = 0.0f;
    this.charIndex_30 = 0;
    this.charX_34 = 0;
    this.charY_36 = 0;
    this.linesScrolled_3a = 0;
    this.blockTimer_3e = 1;
    this.blockTimeLeft_40 = 0;
    this.pauseTimer_44 = 0;

    //LAB_800259b4
    for(int i = 0; i < 8; i++) {
      this.digits_46[i] = 0;
    }

    //LAB_800259e4
    this._5c = TextboxTextState.UNINITIALIZED_0;
    this.selectionLine_60 = 0;
    this.ticksUntilStateTransition_64 = 0;
    this.selectionLine_68 = 0;
    this.selectionIndex_6c = 0;
    this.maxSelectionLine_70 = 0;
    this.minSelectionLine_72 = 0;
    this.stateAfterTransition_78 = TextboxTextState.UNINITIALIZED_0;
    this.element_7c = 0;
    this.digitIndex_80 = 0;

    this.waitTicks = 0;
    this.inputActions.clear();
  }

  public void delete() {
    this.chars_58 = null;
  }
}
