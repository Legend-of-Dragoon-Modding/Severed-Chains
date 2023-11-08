package legend.game.wmap;

import org.joml.Vector3f;

public class ColourBlending20 {
  public Vector3f colour0Start_00;
  public Vector3f colour0End_04;
  public Vector3f colour1Start_08;
  public Vector3f colour1End_0c;
  public int colourEndRatio_10;
  public int colourStartRatio_14;
  public int colour1Ratio_18;
  public int colour0Ratio_1c;

  public void blendColours(final Vector3f out) {
    final float r0 = (this.colour0End_04.x * this.colourEndRatio_10 + this.colour0Start_00.x * this.colourStartRatio_14) / (this.colourEndRatio_10 + this.colourStartRatio_14);
    final float g0 = (this.colour0End_04.y * this.colourEndRatio_10 + this.colour0Start_00.y * this.colourStartRatio_14) / (this.colourEndRatio_10 + this.colourStartRatio_14);
    final float b0 = (this.colour0End_04.z * this.colourEndRatio_10 + this.colour0Start_00.z * this.colourStartRatio_14) / (this.colourEndRatio_10 + this.colourStartRatio_14);

    final float r1 = (this.colour1End_0c.x * this.colourEndRatio_10 + this.colour1Start_08.x * this.colourStartRatio_14) / (this.colourEndRatio_10 + this.colourStartRatio_14);
    final float g1 = (this.colour1End_0c.y * this.colourEndRatio_10 + this.colour1Start_08.y * this.colourStartRatio_14) / (this.colourEndRatio_10 + this.colourStartRatio_14);
    final float b1 = (this.colour1End_0c.z * this.colourEndRatio_10 + this.colour1Start_08.z * this.colourStartRatio_14) / (this.colourEndRatio_10 + this.colourStartRatio_14);

    out.x = (this.colour1Ratio_18 * r1 + this.colour0Ratio_1c * r0) / (this.colour1Ratio_18 + this.colour0Ratio_1c);
    out.y = (this.colour1Ratio_18 * g1 + this.colour0Ratio_1c * g0) / (this.colour1Ratio_18 + this.colour0Ratio_1c);
    out.z = (this.colour1Ratio_18 * b1 + this.colour0Ratio_1c * b0) / (this.colour1Ratio_18 + this.colour0Ratio_1c);
  }
}
