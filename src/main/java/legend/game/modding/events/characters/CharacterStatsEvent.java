package legend.game.modding.events.characters;

import legend.game.modding.events.Event;
import legend.game.types.CharacterData2c;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;

import static legend.game.SItem.levelStuff_800fbd30;
import static legend.game.SItem.magicStuff_800fbd54;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class CharacterStatsEvent extends Event {
  public final int characterId;

  // Basic stats
  public int xp;
  public int hp;
  public int mp;
  public int sp;
  public int dxp;
  public int flags;
  public int level;
  public int dlevel;

  // Current level stats
  public int maxHp;
  /** The addition that unlocks at this level, if there is one */
  public int addition;
  public int bodySpeed;
  public int bodyAttack;
  public int bodyMagicAttack;
  public int bodyDefence;
  public int bodyMagicDefence;

  // Current dlevel stats
  public int maxMp;
  /** The spell that unlocks at this dlevel, if there is one */
  public int spellId;
  public int dragoonAttack;
  public int dragoonMagicAttack;
  public int dragoonDefence;
  public int dragoonMagicDefence;

  public CharacterStatsEvent(final int characterId) {
    this.characterId = characterId;

    final CharacterData2c charData = gameState_800babc8.charData_32c.get(characterId);
    this.xp = charData.xp_00.get();
    this.hp = charData.hp_08.get();
    this.mp = charData.mp_0a.get();
    this.sp = charData.sp_0c.get();
    this.dxp = charData.dlevelXp_0e.get();
    this.flags = charData.status_10.get();
    this.level = charData.level_12.get();
    this.dlevel = charData.dlevel_13.get();

    final LevelStuff08 levelStuff = levelStuff_800fbd30.get(characterId).deref().get(this.level);
    this.maxHp = levelStuff.hp_00.get();
    this.addition = levelStuff.addition_02.get();
    this.bodySpeed = levelStuff.bodySpeed_03.get();
    this.bodyAttack = levelStuff.bodyAttack_04.get();
    this.bodyMagicAttack = levelStuff.bodyMagicAttack_05.get();
    this.bodyDefence = levelStuff.bodyDefence_06.get();
    this.bodyMagicDefence = levelStuff.bodyMagicDefence_07.get();

    final MagicStuff08 magicStuff = magicStuff_800fbd54.get(characterId).deref().get(this.dlevel);
    this.maxMp = magicStuff.mp_00.get();
    this.spellId = magicStuff.spellIndex_02.get();
    this.dragoonAttack = magicStuff.dragoonAttack_04.get();
    this.dragoonMagicAttack = magicStuff.dragoonMagicAttack_05.get();
    this.dragoonDefence = magicStuff.dragoonDefence_06.get();
    this.dragoonMagicDefence = magicStuff.dragoonMagicDefence_07.get();
  }
}
