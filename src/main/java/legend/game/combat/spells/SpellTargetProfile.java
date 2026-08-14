package legend.game.combat.spells;

public record SpellTargetProfile(TargetSide side, TargetScope scope, TargetLifeState lifeState) {
  public SpellTargetProfile {
    if(side == null) {
      throw new IllegalArgumentException("Spell target side cannot be null");
    }

    if(scope == null) {
      throw new IllegalArgumentException("Spell target scope cannot be null");
    }

    if(lifeState == null) {
      throw new IllegalArgumentException("Spell target life state cannot be null");
    }
  }
}
