package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.core.memory.types.TriConsumer;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;

import static legend.game.Scus94491BpeSegment.renderButtonPressHudTexturedRect;
import static legend.game.combat.Bttl.FUN_800d3f98;
import static legend.game.combat.Bttl._800faa9d;
import static legend.game.combat.Bttl.additionNames_800fa8d4;
import static legend.game.combat.Bttl.asciiTable_800fa788;

public class AdditionNameTextEffect1c {
  /** ushort */
  public int _00;
  /** ushort */
  public int addition_02;
  public int _04;
  public int length_08;
  /** ubyte */
  public int positionMovement_0c;
  public int _10;
  public TriConsumer<AdditionCharEffectData0c, Integer, Integer> renderer_14;
  public AdditionCharEffectData0c[] ptr_18;

  @Method(0x800d37dcL)
  private void renderAdditionNameChar(final short displayX, final short displayY, final short addition, final short charOffset, final int charAlpha) {
    int charIdx = 0;

    //LAB_800d3838
    int chr;
    do {
      chr = asciiTable_800fa788[charIdx];

      if(additionNames_800fa8d4[addition].charAt(charOffset) == chr) {
        break;
      } else if(chr == 0) {
        //LAB_800d3860
        charIdx = 0x5b;
        break;
      }

      charIdx++;
    } while(true);

    //LAB_800d3864
    renderButtonPressHudTexturedRect(displayX, displayY, charIdx % 21 * 12 & 0xfc, charIdx / 21 * 12 + 144 & 0xfc, 12, 12, 0xa, Translucency.B_PLUS_F, charAlpha, 0x1000);
  }

  @Method(0x800d3a20L)
  public void renderAdditionNameChar(final AdditionCharEffectData0c charStruct, final long charAlpha, final long charIdx) {
    this.renderAdditionNameChar((short)charStruct.position_04, (short)charStruct.offsetY_06, (short)this.addition_02, (short)charIdx, (int)charAlpha);
  }

  @Method(0x800d3a64L)
  public void renderAdditionNameEffect(final AdditionCharEffectData0c a1, final long charAlpha, final long a3) {
    final String sp0x18 = String.valueOf(this._10);

    long s4;
    if(this._04 < 25) {
      s4 = this._04 & 1;
    } else {
      s4 = 0;
    }

    //LAB_800d3ab8
    //LAB_800d3ac4
    for(; s4 >= 0; s4--) {
      int s1 = a1.position_04;

      //LAB_800d3ad4
      for(int i = 0; i < sp0x18.length(); i++) {
        FUN_800d3f98((short)s1, (short)a1.offsetY_06, sp0x18.charAt(i) - 0x30, (short)41, (int)charAlpha);
        s1 += 8;
      }

      //LAB_800d3b08
      FUN_800d3f98((short) s1      , (short)a1.offsetY_06, 0x0d, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 +  8), (short)a1.offsetY_06, 0x0e, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 + 16), (short)a1.offsetY_06, 0x0f, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 + 24), (short)a1.offsetY_06, 0x10, (short)41, (int)charAlpha);
    }

    //LAB_800d3b98
  }

  @Method(0x800d3bb8L)
  public void tickAdditionNameEffect(final ScriptState<AdditionNameTextEffect1c> state, final AdditionNameTextEffect1c additionStruct) {
    this._04++;

    if(_800faa9d.get() == 0) {
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
        int s2 = charStruct.dupes_02 * 0x10;

        //LAB_800d3cbc
        for(int dupeNum = 0; dupeNum < charStruct.dupes_02 - 1; dupeNum++) {
          s2 -= 16;
          currPosition -= 10;
          final int origCharPosition = charStruct.position_04;
          charStruct.position_04 = currPosition;
          this.renderer_14.accept(charStruct, s2 & 0xff, charIdx);
          charStruct.position_04 = origCharPosition;
        }
      }
    }

    //LAB_800d3d1c
  }
}
