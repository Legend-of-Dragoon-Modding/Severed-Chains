package legend.game.combat.types;

import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptedObject;
import org.joml.Vector3f;

public class DragoonAdditionScriptData1c implements ScriptedObject {
  public int unused_00;
  public int baseAngle_02;
  public int currentTick_04;
  public int unused_05;
  public int stepCountIndex_06;
  public int currentPressNumber_07;
  // public final int[] _08 = new int[5]; // Never used
  public int countEyeFlashTicks_0d;
  public int ticksUntilDeallocationAfterCompletion_0e;
  public int tickEffect_0f;
  public int meterSpinning_10;
  public int buttonPressGlowBrightnessFactor_11;
  public int ticksRemainingToBeginAddition_12;
  /** 0 = requires input, 1 = automatic, 2 = can't be started for some reason and will never be deallocated */
  public int inputMode_13;
  public int totalPressCount_14;
  public int charId_18;

  @Override
  public Vector3f getPosition() {
    return null;
  }

  @Override
  public void renderScriptDebug(final ScriptState<ScriptedObject> state) {

  }
}
