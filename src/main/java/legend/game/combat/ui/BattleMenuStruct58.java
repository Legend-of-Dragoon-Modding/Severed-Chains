package legend.game.combat.ui;

public class BattleMenuStruct58 {
  public short state_00;
  /** 0 = don't render, 1 = wrapping, 2 = render normally */
  public int highlightState_02;
  public short charIndex_04;
  public short x_06;
  public short y_08;
  public short xShiftOffset_0a;
  public short unused_0c;
  public short iconCount_0e;
  /**
   * <ul>
   *   <li>0x80 - disabled</li>
   * </ul>
   */
  public final short[] iconFlags_10 = new short[9];
  public short selectedIcon_22;
  public short currentIconStateTick_24;
  public short iconStateIndex_26;
  public short highlightX0_28;
  public short highlightY_2a;
  public short colour_2c;

  public int countHighlightMovementStep_30;
  public int highlightMovementDistance_34;
  public int currentHighlightMovementStep_38;
  public int highlightX1_3c;
  public boolean renderSelectedIconText_40;
  public int cameraPositionSwitchTicksRemaining_44;
  public int target_48;
  public boolean displayTargetArrowAndName_4c;
  public int targetType_50;
  public int combatantIndex_54;
}
