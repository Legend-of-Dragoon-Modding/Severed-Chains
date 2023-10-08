package legend.game.combat.effects;

public class ParticleEffectData98Inner24 {
  public int scriptIndex_00; // unused
  public int parentScriptIndex_04;
  public int particleTypeId_08;
  public short totalCountParticleInstance_0c;

  public short _10; // Used in calculations for X, Y, and Z values; entirely scripted; used to scale starting position during initialization

  public short _14;

  public float _18; // Used in calculations for X, Y, and Z values; is used to modify effect velocity during initialization; entirely scripted

  /**
   * Not a finalized name, still unsure of exact purpose. Seems like packed value containing
   * some kind of color info and a flag indicating how to set certain values.
   */
  public int particleInnerStuff_1c;
  public short callbackIndex_20;
}
