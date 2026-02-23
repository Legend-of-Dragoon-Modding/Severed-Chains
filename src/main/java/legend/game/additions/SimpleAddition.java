package legend.game.additions;

import legend.game.types.CharacterData2c;
import legend.game.types.GameState52c;

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
  public int getDamage(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats) {
    return (int)(this.baseDamage * this.getDamageMultiplier(state, charData, additionStats));
  }

  @Override
  public int getSp(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats) {
    final float multi = this.getSpMultiplier(state, charData, additionStats);
    int sp = 0;

    for(int hit = 0; hit < this.hits.length; hit++) {
      sp += (int)(this.hits[hit].sp_05 * multi);
    }

    return sp;
  }

  @Override
  public float getDamageMultiplier(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats) {
    return this.levelMultipliers[additionStats.level].damage;
  }

  @Override
  public float getSpMultiplier(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats) {
    return this.levelMultipliers[additionStats.level].sp;
  }

  @Override
  public int getHitCount(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats) {
    return this.hits.length;
  }

  @Override
  public AdditionHitProperties10 getHit(final GameState52c state, final CharacterData2c charData, final CharacterAdditionStats additionStats, final int index) {
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
