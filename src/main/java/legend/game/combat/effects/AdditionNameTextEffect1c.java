package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.memory.types.TriConsumer;
import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptedObject;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.battleUiParts;
import static legend.game.combat.Battle.additionNames_800fa8d4;
import static legend.game.combat.Battle.asciiTable_800fa788;

public class AdditionNameTextEffect1c implements ScriptedObject {
  /** ushort */
  public int _00;
  /** ushort */
  public int additionId_02;
  public int ticks_04;
  public int length_08;
  /** ubyte */
  public int positionMovement_0c;
  public int totalSp_10;
  public TriConsumer<AdditionCharEffectData0c, Integer, Integer> renderer_14;
  public AdditionCharEffectData0c[] ptr_18;

  @Override
  public Vector3f getPosition() {
    return new Vector3f(this.ptr_18[0].offsetX_08, this.ptr_18[0].offsetY_06, 0.0f);
  }

  @Override
  public void renderScriptDebug(final ScriptState<ScriptedObject> state) {
    final Vector2f viewspace = new Vector2f(this.ptr_18[0].offsetX_08, this.ptr_18[0].offsetY_06);
    ScriptedObject.renderScriptDebug(viewspace, this.getColour());
    ScriptedObject.renderScriptDebugText(state, viewspace.x + GPU.getOffsetX() - 9.0f, viewspace.y + GPU.getOffsetY() - 9.0f);
  }

  @Method(0x800d37dcL)
  private void renderAdditionNameChar(final int x, final int y, final int additionId, final int charOffset, final int brightness) {
    int charIndex = 0;

    //LAB_800d3838
    int chr;
    while(true) {
      chr = asciiTable_800fa788[charIndex];

      if(additionNames_800fa8d4[additionId].charAt(charOffset) == chr) {
        break;
      } else if(chr == 0) {
        //LAB_800d3860
        charIndex = 91;
        break;
      }

      charIndex++;
    }

    //LAB_800d3864
    battleUiParts.queueLetter(charIndex, x, y, 0xa, Translucency.B_PLUS_F, brightness, 1.0f, 1.0f);
  }

  @Method(0x800d3a20L)
  public void renderAdditionNameChar(final AdditionCharEffectData0c charStruct, final int brightness, final int charIndex) {
    this.renderAdditionNameChar(charStruct.position_04, charStruct.offsetY_06, this.additionId_02, charIndex, brightness);
  }

  @Method(0x800d3a64L)
  public void renderAdditionSpGain(final AdditionCharEffectData0c effect, final int brightness, final int charIndex) {
    int s4;
    if(this.ticks_04 < 25) {
      s4 = this.ticks_04 & 1;
    } else {
      s4 = 0;
    }

    //LAB_800d3ab8
    //LAB_800d3ac4
    for(; s4 >= 0; s4--) {
      int x = effect.position_04;
      int sp = this.totalSp_10;
      final int digitCount = MathHelper.digitCount(sp);

      //LAB_800d3ad4
      for(int i = 0; i < digitCount; i++) {
        final int digit = sp % 10;
        battleUiParts.queueBigNumber(digit, x + (digitCount - i - 1) * 8, effect.offsetY_06, 0x29, Translucency.B_PLUS_F, brightness, 1.0f, 1.0f);
        sp /= 10;
      }

      x += digitCount * 8;

      //LAB_800d3b08
      battleUiParts.queuePoints(x, effect.offsetY_06, 0x29, Translucency.B_PLUS_F, brightness, 1.0f, 1.0f);
    }

    //LAB_800d3b98
  }

  @Method(0x800d3bb8L)
  public void tickAdditionNameEffect(final ScriptState<AdditionNameTextEffect1c> state, final int unknown) {
    this.ticks_04++;

    if(unknown == 0) {
      state.deallocateWithChildren();
    } else {
      //LAB_800d3c10
      //LAB_800d3c24
      for(int charIdx = 0; charIdx < this.length_08; charIdx++) {
        final AdditionCharEffectData0c charStruct = this.ptr_18[charIdx];

        if(charStruct.scrolling_00 != 0) {
          charStruct.position_04 += this.positionMovement_0c;

          if(charStruct.position_04 >= charStruct.offsetX_08) {
            charStruct.position_04 = charStruct.offsetX_08;
            charStruct.scrolling_00 = 0;
          }
        } else {
          //LAB_800d3c70
          if(charStruct.dupes_02 > 0) {
            charStruct.dupes_02--;
          }
        }

        //LAB_800d3c84
        //LAB_800d3c88
        this.renderer_14.accept(charStruct, 0x80, charIdx);
        int currPosition = charStruct.position_04;
        int u = charStruct.dupes_02 * 0x10;

        //LAB_800d3cbc
        for(int dupeNum = 0; dupeNum < charStruct.dupes_02 - 1; dupeNum++) {
          u -= 16;
          currPosition -= 10;
          final int origCharPosition = charStruct.position_04;
          charStruct.position_04 = currPosition;
          this.renderer_14.accept(charStruct, u & 0xff, charIdx);
          charStruct.position_04 = origCharPosition;
        }
      }
    }

    //LAB_800d3d1c
  }
}
