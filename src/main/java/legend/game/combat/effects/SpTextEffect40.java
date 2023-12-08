package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import org.joml.Math;

import java.util.Arrays;

import static legend.game.combat.Bttl.FUN_800d3f98;
import static legend.game.combat.Bttl._800faa94;

public class SpTextEffect40 {
  public int _00 = 1;
  public int _01;
  public int _02 = 0x80;

  public int _04;
  public int _08;
  public int _0c;
  public int _10 = 30;

  public int _1c;
  public int _20 = 0x5000;

  public int _2c;
  public int _30 = -0x800;

  public final SpTextEffectTrail10[] charArray_3c = new SpTextEffectTrail10[8];

  public SpTextEffect40() {
    Arrays.setAll(this.charArray_3c, i -> new SpTextEffectTrail10());
  }

  @Method(0x800d4018L)
  public void tickSpTextEffect(final ScriptState<SpTextEffect40> state, final SpTextEffect40 s3) {
    if(_800faa94.get() == 0 && this._00 == 0) {
      if(this._02 == 0) {
        state.deallocateWithChildren();
        return;
      }

      //LAB_800d408c
      this._02 -= 16;
    }

    //LAB_800d4090
    this._04++;
    this._10 += this._30;
    this._30 += 0x200;

    if(this._20 < this._10) {
      this._10 = this._20;
      this._00 = 0;
    }

    //LAB_800d40cc
    this._0c += this._2c;

    if(Math.abs(this._1c) < Math.abs(this._0c)) {
      this._0c = this._1c;
    }

    //LAB_800d4108
    final SpTextEffectTrail10[] charArray = this.charArray_3c;

    //LAB_800d4118
    int i;
    for (i = 7; i > 0; i--) {
      charArray[i]._00 = charArray[i - 1]._00;
      charArray[i]._04 = charArray[i - 1]._04;
    }

    charArray[0]._00 = this._0c;
    charArray[0]._04 = this._10;
    final String sp0x18 = Integer.toString(this._08);
    int fp = this._02;
    final int sp20 = (fp & 0xff) >>> 3;
    final int sp2c = this._01 + 0x21 & 0xff;

    //LAB_800d419c
    short s2;
    short s5;
    for (i = 0; i < 8; i++) {
      fp = fp - sp20;

      if(i == 0 || charArray[i]._00 != this._0c || charArray[i]._04 != this._10) {
        //LAB_800d41d8
        s2 = (short)(charArray[i]._00 >> 8);
        s5 = (short)(charArray[i]._04 >> 8);

        if(this._01 != 0) {
          FUN_800d3f98(s2, s5, 10, (short)sp2c, fp & 0xff);
          s2 += 8;
        }

        //LAB_800d4224
        //LAB_800d423c
        for(int j = 0; j < sp0x18.length(); j++) {
          FUN_800d3f98(s2, s5, sp0x18.charAt(j) - 0x30, (short)sp2c, fp & 0xff);
          s2 += 8;
        }

        //LAB_800d4274
        FUN_800d3f98((short)(s2 - 2), s5, 11, (short)sp2c, fp & 0xff);
        FUN_800d3f98((short)(s2 + 4), s5, 12, (short)sp2c, fp & 0xff);
      }
      //LAB_800d42c0
    }
    //LAB_800d42dc
  }
}
