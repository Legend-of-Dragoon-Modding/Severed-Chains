package legend.game.modding.events.characters;

import legend.game.modding.coremod.CoreMod;
import legend.game.types.CharacterData2c;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;
import org.legendofdragoon.modloader.events.Event;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
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

    final CharacterData2c charData = gameState_800babc8.charData_32c[characterId];
    this.xp = charData.xp_00;
    this.hp = charData.hp_08;
    this.mp = charData.mp_0a;
    this.sp = charData.sp_0c;
    this.dxp = charData.dlevelXp_0e;
    this.flags = charData.status_10;
    this.level = charData.level_12;
    this.dlevel = charData.dlevel_13;

    final LevelStuff08 levelStuff = CoreMod.CHARACTER_DATA[characterId].statsTable[this.level];
    this.maxHp = levelStuff.hp_00;
    this.addition = levelStuff.addition_02;
    this.bodySpeed = levelStuff.bodySpeed_03;
    this.bodyAttack = levelStuff.bodyAttack_04;
    this.bodyMagicAttack = levelStuff.bodyMagicAttack_05;
    this.bodyDefence = levelStuff.bodyDefence_06;
    this.bodyMagicDefence = levelStuff.bodyMagicDefence_07;

    final MagicStuff08 magicStuff = CoreMod.CHARACTER_DATA[characterId].dragoonStatsTable[this.dlevel];
    this.maxMp = magicStuff.mp_00;
    this.spellId = magicStuff.spellIndex_02;
    this.dragoonAttack = magicStuff.dragoonAttack_04;
    this.dragoonMagicAttack = magicStuff.dragoonMagicAttack_05;
    this.dragoonDefence = magicStuff.dragoonDefence_06;
    this.dragoonMagicDefence = magicStuff.dragoonMagicDefence_07;
  }
}
