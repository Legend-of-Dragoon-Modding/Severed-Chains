package legend.game.types;

public class CharacterData2c {
  public int xp_00;
  /**
   * <ul>
   *   <li>0x1 - in party</li>
   *   <li>0x2 - can be put in main party (without this flag a char can only appear in secondary and can't be put into main)</li>
   *   <li>0x20 - can't remove (don't select, can't be taken out of main party)</li>
   *   <li>0x40 - ultimate addition unlocked</li>
   * </ul>
   */
  public int partyFlags_04;
  public int hp_08;
  public int mp_0a;
  public int sp_0c;
  public int dlevelXp_0e;
  /** i.e. poison */
  public int status_10;
  public int level_12;
  public int dlevel_13;
  public final int[] equipment_14 = new int[5];
  public int selectedAddition_19;
  public final int[] additionLevels_1a = new int[8];
  public final int[] additionXp_22 = new int[8];

  public void set(final CharacterData2c other) {
    this.xp_00 = other.xp_00;
    this.partyFlags_04 = other.partyFlags_04;
    this.hp_08 = other.hp_08;
    this.mp_0a = other.mp_0a;
    this.sp_0c = other.sp_0c;
    this.dlevelXp_0e = other.dlevelXp_0e;
    this.status_10 = other.status_10;
    this.level_12 = other.level_12;
    this.dlevel_13 = other.dlevel_13;
    System.arraycopy(other.equipment_14, 0, this.equipment_14, 0, this.equipment_14.length);
    this.selectedAddition_19 = other.selectedAddition_19;
    System.arraycopy(other.additionLevels_1a, 0, this.additionLevels_1a, 0, this.additionLevels_1a.length);
    System.arraycopy(other.additionXp_22, 0, this.additionXp_22, 0, this.additionXp_22.length);
  }
}
