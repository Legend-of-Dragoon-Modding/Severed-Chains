package legend.game.additions;

import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;

public abstract class SimpleAddition extends Addition {
  public final boolean countsTowardsMastery;
  private final LevelMultipliers[] levelMultipliers;
  private final AdditionHitProperties10[] hits;

  public SimpleAddition(final boolean countsTowardsMastery, final LevelMultipliers[] levelMultipliers, final AdditionHitProperties10[] hits) {
    this.countsTowardsMastery = countsTowardsMastery;
    this.levelMultipliers = levelMultipliers;
    this.hits = hits;
  }

  @Override
  public int getDamage(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    final float multi = this.getDamageMultiplier(character, additionInfo);
    float damage = 0;

    for(int hit = 0; hit < this.hits.length; hit++) {
      damage += this.hits[hit].damageMultiplier_04 * multi;
    }

    return (int)damage;
  }

  @Override
  public int getSp(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    final float multi = this.getSpMultiplier(character, additionInfo);
    int sp = 0;

    for(int hit = 0; hit < this.hits.length; hit++) {
      sp += (int)(this.hits[hit].sp_05 * multi);
    }

    return sp;
  }

  @Override
  public float getDamageMultiplier(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return this.levelMultipliers[additionInfo.level - 1].damage;
  }

  @Override
  public float getSpMultiplier(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return this.levelMultipliers[additionInfo.level - 1].sp;
  }

  @Override
  public int getXpToNextLevel(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return additionInfo.level * 20;
  }

  @Override
  public int getMaxLevel(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return this.levelMultipliers.length;
  }

  @Override
  public boolean isComplete(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return additionInfo.level >= this.getMaxLevel(character, additionInfo);
  }

  @Override
  public boolean countsTowardsMastery(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return this.countsTowardsMastery;
  }

  @Override
  public int getHitCount(final CharacterData2c character, final CharacterAdditionInfo additionInfo) {
    return this.hits.length;
  }

  @Override
  public AdditionHitProperties10 getHit(final CharacterData2c character, final CharacterAdditionInfo additionInfo, final int index) {
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
