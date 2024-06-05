package legend.game.saves.types;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.game.unpacker.FileData;

import java.util.ArrayList;
import java.util.List;

public class EnhancedSaveDisplay extends SaveDisplay {
  public final String location;
  public final int gold;
  public final int stardust;
  public final int time;

  public final IntList party = new IntArrayList();
  public final List<Char> chars = new ArrayList<>();
  public final List<Dragoon> dragoons = new ArrayList<>();

  public EnhancedSaveDisplay(final String location, final int gold, final int stardust, final int time) {
    this.location = location;
    this.gold = gold;
    this.stardust = stardust;
    this.time = time;
  }

  public static class Char {
    public final FileData icon;
    public final String name;
    public final int lvl;
    public final int exp;
    public final int dlvl;
    public final int dexp;
    public final int hp;
    public final int mp;
    public final int sp;
    public final int maxHp;
    public final int maxMp;
    public final int maxSp;

    public Char(final FileData icon, final String name, final int lvl, final int exp, final int dlvl, final int dexp, final int hp, final int mp, final int sp, final int maxHp, final int maxMp, final int maxSp) {
      this.icon = icon;
      this.name = name;
      this.lvl = lvl;
      this.exp = exp;
      this.dlvl = dlvl;
      this.dexp = dexp;
      this.hp = hp;
      this.mp = mp;
      this.sp = sp;
      this.maxHp = maxHp;
      this.maxMp = maxMp;
      this.maxSp = maxSp;
    }
  }

  public static class Dragoon {
    public final FileData icon;
    public final String name;

    public Dragoon(final FileData icon, final String name) {
      this.icon = icon;
      this.name = name;
    }
  }
}
