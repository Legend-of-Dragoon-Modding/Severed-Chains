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
  public final FileData icons;

  public final IntList party = new IntArrayList();
  public final List<Char> chars = new ArrayList<>();
  public final List<Dragoon> dragoons = new ArrayList<>();

  public EnhancedSaveDisplay(final String location, final int gold, final int stardust, final int time, final FileData icons) {
    this.location = location;
    this.gold = gold;
    this.stardust = stardust;
    this.time = time;
    this.icons = icons;
  }

  public static class Char {
    public final String name;
    public final int iconU;
    public final int iconV;
    public final int iconW;
    public final int iconH;
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

    public Char(final String name, final int iconU, final int iconV, final int iconW, final int iconH, final int lvl, final int exp, final int dlvl, final int dexp, final int hp, final int mp, final int sp, final int maxHp, final int maxMp, final int maxSp) {
      this.name = name;
      this.iconU = iconU;
      this.iconV = iconV;
      this.iconW = iconW;
      this.iconH = iconH;
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
    public final String name;
    public final int iconU;
    public final int iconV;
    public final int iconW;
    public final int iconH;

    public Dragoon(final String name, final int iconU, final int iconV, final int iconW, final int iconH) {
      this.name = name;
      this.iconU = iconU;
      this.iconV = iconV;
      this.iconW = iconW;
      this.iconH = iconH;
    }
  }
}
