package legend.game.combat.particles;

public class ParticleEffectData98Inner24 {
//  public int scriptIndex_00; // unused
//  public int parentScriptIndex_04;
//  public int particleTypeId_08;
//  public short totalCountParticleInstance_0c;

  public final short _10; // Used in calculations for X, Y, and Z values; entirely scripted; used to scale starting position during initialization

  public final short _14;

  public final float _18; // Used in calculations for X, Y, and Z values; is used to modify effect velocity during initialization; entirely scripted

  /**
   * Not a finalized name, still unsure of exact purpose. Seems like packed value containing
   * some kind of color info and a flag indicating how to set certain values.
   */
  public final int particleInnerStuff_1c;
  public final int behaviourType_20;

  public ParticleEffectData98Inner24(final short _10, final short _14, final float _18, final int particleInnerStuff, final int behaviourType) {
    this._10 = _10;
    this._14 = _14;
    this._18 = _18;
    this.particleInnerStuff_1c = particleInnerStuff;
    this.behaviourType_20 = behaviourType;
  }
}
