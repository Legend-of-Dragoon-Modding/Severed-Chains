package legend.game.additions;

import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;

public abstract class SimpleAddition extends Addition {
  public final int baseDamage;
  private final LevelMultipliers[] levelMultipliers;
  private final AdditionHitProperties10[] hits;

  public SimpleAddition(final int baseDamage, final LevelMultipliers[] levelMultipliers, final AdditionHitProperties10[] hits) {
    this.baseDamage = baseDamage;
    this.levelMultipliers = levelMultipliers;
    this.hits = hits;
  }

  @Override
  public int getDamage(final CharacterData2c charData, final CharacterAdditionInfo additionStats) {
    return (int)(this.baseDamage * this.getDamageMultiplier(charData, additionStats));
  }

  @Override
  public int getSp(final CharacterData2c charData, final CharacterAdditionInfo additionStats) {
    final float multi = this.getSpMultiplier(charData, additionStats);
    int sp = 0;

    for(int hit = 0; hit < this.hits.length; hit++) {
      sp += (int)(this.hits[hit].sp_05 * multi);
    }

    return sp;
  }

  @Override
  public float getDamageMultiplier(final CharacterData2c charData, final CharacterAdditionInfo additionStats) {
    return this.levelMultipliers[additionStats.level].damage;
  }

  @Override
  public float getSpMultiplier(final CharacterData2c charData, final CharacterAdditionInfo additionStats) {
    return this.levelMultipliers[additionStats.level].sp;
  }

  @Override
  public int getHitCount(final CharacterData2c charData, final CharacterAdditionInfo additionStats) {
    return this.hits.length;
  }

  @Override
  public AdditionHitProperties10 getHit(final CharacterData2c charData, final CharacterAdditionInfo additionStats, final int index) {
    return this.hits[index];
  }

  public static class LevelMultipliers {
    public final float damage;
    public final float sp;

    public LevelMultipliers(final float damage, final float sp) {
      this.damage = damage;
      this.sp = sp;
    }

    @Override
    public boolean equals(final Object obj) {
      if(!(obj instanceof final LevelMultipliers other)) {
        return false;
      }

      return this.damage == other.damage && this.sp == other.sp;
    }
  }
}
