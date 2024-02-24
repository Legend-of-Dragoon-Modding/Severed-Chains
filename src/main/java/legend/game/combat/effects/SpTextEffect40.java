package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import org.joml.Math;

import java.util.Arrays;

import static legend.game.combat.SEffe.FUN_800d3f98;

public class SpTextEffect40 {
  public boolean movingY_00 = true;
  public int _01;
  public int brightness_02 = 0x80;

  public int ticks_04;
  public int value_08;
  /** .8 */
  public int x_0c;
  /** .8 */
  public int y_10 = 30;

  /** .8 */
  public int destX_1c;
  /** .8 */
  public int destY_20 = 0x5000;

  /** .8 */
  public int stepX_2c;
  /** .8 */
  public int stepY_30 = -0x800;

  public final SpTextEffectTrail10[] charArray_3c = new SpTextEffectTrail10[8];

  public SpTextEffect40() {
    Arrays.setAll(this.charArray_3c, i -> new SpTextEffectTrail10());
  }

  @Method(0x800d4018L)
  public void tickSpTextEffect(final ScriptState<SpTextEffect40> state, final int unknown) {
    if(unknown == 0 && !this.movingY_00) {
      if(this.brightness_02 == 0) {
        state.deallocateWithChildren();
        return;
      }

      //LAB_800d408c
      this.brightness_02 -= 16;
    }

    //LAB_800d4090
    this.ticks_04++;
    this.y_10 += this.stepY_30;
    this.stepY_30 += 0x200;

    if(this.y_10 > this.destY_20) {
      this.y_10 = this.destY_20;
      this.movingY_00 = false;
    }

    //LAB_800d40cc
    this.x_0c += this.stepX_2c;

    if(Math.abs(this.x_0c) > Math.abs(this.destX_1c)) {
      this.x_0c = this.destX_1c;
    }

    //LAB_800d4108
    final SpTextEffectTrail10[] charArray = this.charArray_3c;

    //LAB_800d4118
    for(int i = 7; i > 0; i--) {
      charArray[i].x_00 = charArray[i - 1].x_00;
      charArray[i].y_04 = charArray[i - 1].y_04;
    }

    charArray[0].x_00 = this.x_0c;
    charArray[0].y_04 = this.y_10;
    final String str = Integer.toString(this.value_08);
    int brightness = this.brightness_02;
    final int darkeningStep = (brightness & 0xff) >>> 3;
    final int packedClut = this._01 + 0x21 & 0xff;

    //LAB_800d419c
    int x;
    int y;
    for(int i = 0; i < 8; i++) {
      brightness -= darkeningStep;

      if(i == 0 || charArray[i].x_00 != this.x_0c || charArray[i].y_04 != this.y_10) {
        //LAB_800d41d8
        x = charArray[i].x_00 >> 8;
        y = charArray[i].y_04 >> 8;

        if(this._01 != 0) {
          FUN_800d3f98(x, y, 10, packedClut, brightness & 0xff);
          x += 8;
        }

        //LAB_800d4224
        //LAB_800d423c
        for(int charIndex = 0; charIndex < str.length(); charIndex++) {
          FUN_800d3f98(x, y, str.charAt(charIndex) - 0x30, packedClut, brightness & 0xff);
          x += 8;
        }

        //LAB_800d4274
        FUN_800d3f98(x - 2, y, 11, packedClut, brightness & 0xff);
        FUN_800d3f98(x + 4, y, 12, packedClut, brightness & 0xff);
      }
      //LAB_800d42c0
    }
    //LAB_800d42dc
  }
}
