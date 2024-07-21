package legend.lodmod.items;

import legend.core.memory.Method;
import legend.game.characters.Element;
import legend.game.inventory.Item;
import legend.game.inventory.UseItemResponse;
import legend.game.unpacker.FileData;
import legend.lodmod.LodItems;

import java.util.EnumSet;
import java.util.Set;

import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.Scus94491BpeSegment_8002.addHp;
import static legend.game.Scus94491BpeSegment_8002.addMp;
import static legend.game.Scus94491BpeSegment_8002.addSp;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;

public class FileBasedItem extends Item {
  /**
   * Old flags:
   * <ul>
   *   <li>0x2 - target all</li>
   *   <li>0x4 - target monsters</li>
   *   <li>0x10 - can be used in menu</li>
   * </ul>
   */
  private final Set<TargetType> target_00 = EnumSet.noneOf(TargetType.class);
  private final Set<UsageLocation> usage_00 = EnumSet.noneOf(UsageLocation.class);
  /** ubyte */
  private final Element element_01;
  /** ubyte */
  private final int damageMultiplier_02;
  private final int powerDefence;
  private final int powerMagicDefence;
  private final int powerAttack;
  private final int powerMagicAttack;
  private final int powerAttackHit;
  private final int powerMagicAttackHit;
  private final int powerAttackAvoid;
  private final int powerMagicAttackAvoid;
  private final boolean physicalImmunity;
  private final boolean magicalImmunity;
  private final int speedUp;
  private final int speedDown;
  private final int spPerPhysicalHit;
  private final int mpPerPhysicalHit;
  private final int spPerMagicalHit;
  private final int mpPerMagicalHit;
  /** ubyte */
  private final int damage_05;
  /** byte */
  private final int icon_07;
  /** ubyte */
  private final int status_08;
  /** ubyte */
  private final int percentage_09;
  /** ubyte */
  private final int uu2_0a;
  /**
   * <ul>
   *   <li>0x04 - cause status</li>
   *   <li>0x08 - cure status</li>
   *   <li>0x10 - revive</li>
   *   <li>0x20 - SP</li>
   *   <li>0x40 - MP</li>
   *   <li>0x80 - HP</li>
   * </ul>
   *
   * ubyte
   */
  public final int type_0b;

  public static FileBasedItem fromFile(final String name, final String description, final String combatDescription, final int price, final FileData data) {
    final int target = data.readUByte(0x0);

    final Set<TargetType> targets = EnumSet.noneOf(TargetType.class);

    if((target & 0x2) != 0) {
      targets.add(TargetType.ALL);
    }

    //TODO this is probably wrong since the original flag was "monsters"
    if((target & 0x4) != 0) {
      targets.add(TargetType.ENEMIES);
    } else {
      targets.add(TargetType.ALLIES);
    }

    final Set<UsageLocation> usage = EnumSet.noneOf(UsageLocation.class);

    if((target & 0x10) != 0) {
      usage.add(UsageLocation.MENU);
    }

    usage.add(UsageLocation.BATTLE);

    final Element element = Element.fromFlag(data.readUByte(0x1));
    final int damageMultiplier = data.readUByte(0x2);

    final int specialAmount = data.readByte(0x6);

    final int special1 = data.readUByte(0x3);
    final int powerDefence = (special1 & 0x80) != 0 ? specialAmount : 0;
    final int powerMagicDefence = (special1 & 0x40) != 0 ? specialAmount : 0;
    final int powerAttack = (special1 & 0x20) != 0 ? specialAmount : 0;
    final int powerMagicAttack = (special1 & 0x10) != 0 ? specialAmount : 0;
    final int powerAttackHit = (special1 & 0x8) != 0 ? specialAmount : 0;
    final int powerMagicAttackHit = (special1 & 0x4) != 0 ? specialAmount : 0;
    final int powerAttackAvoid = (special1 & 0x2) != 0 ? specialAmount : 0;
    final int powerMagicAttackAvoid = (special1 & 0x1) != 0 ? specialAmount : 0;

    final int special2 = data.readUByte(0x4);
    final boolean physicalImmunity = (special2 & 0x80) != 0;
    final boolean magicalImmunity = (special2 & 0x40) != 0;
    final int speedUp = (special2 & 0x20) != 0 ? 100 : 0;
    final int speedDown = (special2 & 0x10) != 0 ? -50 : 0;
    final int spPerPhysicalHit = (special2 & 0x8) != 0 ? specialAmount : 0;
    final int mpPerPhysicalHit = (special2 & 0x4) != 0 ? specialAmount : 0;
    final int spPerMagicalHit = (special2 & 0x2) != 0 ? specialAmount : 0;
    final int mpPerMagicalHit = (special2 & 0x1) != 0 ? specialAmount : 0;

    final int damage = data.readUByte(0x5);
    final int icon = data.readByte(0x7);
    final int status = data.readUByte(0x8);
    final int percentage = data.readUByte(0x9);
    final int uu2 = data.readUByte(0xa);
    final int type = data.readUByte(0xb);
    return new FileBasedItem(name, description, combatDescription, price, targets, usage, element, damageMultiplier, powerDefence, powerMagicDefence, powerAttack, powerMagicAttack, powerAttackHit, powerMagicAttackHit, powerAttackAvoid, powerMagicAttackAvoid, physicalImmunity, magicalImmunity, speedUp, speedDown, spPerPhysicalHit, mpPerPhysicalHit, spPerMagicalHit, mpPerMagicalHit, damage, icon, status, percentage, uu2, type);
  }

  public FileBasedItem(final String name, final String description, final String combatDescription, final int price, final Set<TargetType> target, final Set<UsageLocation> usage, final Element element, final int damageMultiplier, final int powerDefence, final int powerMagicDefence, final int powerAttack, final int powerMagicAttack, final int powerAttackHit, final int powerMagicAttackHit, final int powerAttackAvoid, final int powerMagicAttackAvoid, final boolean physicalImmunity, final boolean magicalImmunity, final int speedUp, final int speedDown, final int spPerPhysicalHit, final int mpPerPhysicalHit, final int spPerMagicalHit, final int mpPerMagicalHit, final int damage, final int icon, final int status, final int percentage, final int uu2, final int type) {
    super(name, description, combatDescription, icon, price);

    this.target_00.addAll(target);
    this.usage_00.addAll(usage);

    this.element_01 = element;
    this.damageMultiplier_02 = damageMultiplier;
    this.powerDefence = powerDefence;
    this.powerMagicDefence = powerMagicDefence;
    this.powerAttack = powerAttack;
    this.powerMagicAttack = powerMagicAttack;
    this.powerAttackHit = powerAttackHit;
    this.powerMagicAttackHit = powerMagicAttackHit;
    this.powerAttackAvoid = powerAttackAvoid;
    this.powerMagicAttackAvoid = powerMagicAttackAvoid;
    this.physicalImmunity = physicalImmunity;
    this.magicalImmunity = magicalImmunity;
    this.speedUp = speedUp;
    this.speedDown = speedDown;
    this.spPerPhysicalHit = spPerPhysicalHit;
    this.mpPerPhysicalHit = mpPerPhysicalHit;
    this.spPerMagicalHit = spPerMagicalHit;
    this.mpPerMagicalHit = mpPerMagicalHit;
    this.damage_05 = damage;
    this.icon_07 = icon;
    this.status_08 = status;
    this.percentage_09 = percentage;
    this.uu2_0a = uu2;
    this.type_0b = type;
  }

  @Override
  public boolean isProtected() {
    return
      this == LodItems.PANDEMONIUM.get() ||
      this == LodItems.MAGIC_SHIELD.get() ||
      this == LodItems.MATERIAL_SHIELD.get() ||
      this == LodItems.SMOKE_BALL.get() ||
      this == LodItems.MAGIC_SIG_STONE.get() ||
      this == LodItems.POWER_UP.get() ||
      this == LodItems.POWER_DOWN.get() ||
      this == LodItems.SPEED_UP.get() ||
      this == LodItems.SPEED_DOWN.get() ||
      this == LodItems.PSYCHE_BOMB_X.get();
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return this.usage_00.contains(location);
  }

  @Override
  public boolean canBeUsedNow(final UsageLocation location) {
    if(!this.canBeUsed(location)) {
      return false;
    }

    if(location == UsageLocation.MENU && (this.type_0b & 0x8) != 0) {
      int allStatus = 0;
      for(int i = 0; i < characterCount_8011d7c4; i++) {
        allStatus |= gameState_800babc8.charData_32c[characterIndices_800bdbb8[i]].status_10;
      }

      return (this.status_08 & allStatus) != 0;
    }

    return true;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return this.target_00.contains(type);
  }

  /** TODO refactor, use UsageLocation */
  @Override
  @Method(0x80022d88L)
  public void useItemInMenu(final UseItemResponse response, final int charIndex) {
    response._00 = 0;
    response.value_04 = 0;

    if(!this.canBeUsed(Item.UsageLocation.MENU)) {
      //LAB_80022dd8
      return;
    }

    //LAB_80022e0c
    response._00 = 1;

    //LAB_80022e94
    if((this.type_0b & 0x80) != 0) {
      //LAB_80022edc
      response._00 = this.canTarget(Item.TargetType.ALL) ? 3 : 2;

      final int amount;
      if(this.percentage_09 == 100) {
        amount = -1;
      } else {
        //LAB_80022ef0
        amount = stats_800be5f8[charIndex].maxHp_66 * this.percentage_09 / 100;
      }

      //LAB_80022f3c
      response.value_04 = addHp(charIndex, amount);
    }

    //LAB_80022f50
    if((this.type_0b & 0x40) != 0) {
      //LAB_80022f98
      response._00 = this.canTarget(Item.TargetType.ALL) ? 5 : 4;

      final int amount;
      if(this.percentage_09 == 100) {
        amount = -1;
      } else {
        //LAB_80022fac
        amount = stats_800be5f8[charIndex].maxMp_6e * this.percentage_09 / 100;
      }

      //LAB_80022ff8
      response.value_04 = addMp(charIndex, amount);
    }

    //LAB_8002300c
    if((this.type_0b & 0x20) != 0) {
      response._00 = 6;

      final int amount;
      if(this.percentage_09 == 100) {
        amount = -1;
      } else {
        amount = this.percentage_09;
      }

      //LAB_80023050
      response.value_04 = addSp(charIndex, amount);
    }

    //LAB_80023068
    if((this.type_0b & 0x8) != 0) {
      final int status = gameState_800babc8.charData_32c[charIndex].status_10;

      if((this.status_08 & status) != 0) {
        response.value_04 = status;
        gameState_800babc8.charData_32c[charIndex].status_10 &= ~status;
      }

      //LAB_800230ec
      response._00 = 7;
    }
  }
}
