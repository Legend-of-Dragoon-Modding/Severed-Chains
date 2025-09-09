package legend.game.combat.types;

import legend.core.memory.Method;
import legend.game.inventory.InventoryEntry;
import legend.game.scripting.ScriptFile;
import legend.game.types.CContainer;
import legend.game.unpacker.FileData;

import java.util.ArrayList;
import java.util.List;

import static legend.game.Scus94491BpeSegment_800b.loadingMonsterModels;

/** Data related to a combatant (player or enemy) */
public class CombatantStruct1a8 {
  public List<FileData> mrg_00;
  public List<FileData> mrg_04;
  public CContainer tmd_08;

  public ScriptFile scriptPtr_10;
  public final CombatantAsset0c[] assets_14 = new CombatantAsset0c[32];
  public int xp_194;
  public int gold_196;
  public final List<ItemDrop> drops = new ArrayList<>();
//  public int itemChance_198;
//  public int itemDrop_199;
  public int _19a;
  public int charSlot_19c;
  /**
   * <ul>
   *   <li>0x1 - used?</li>
   *   <li>0x4 - player (not NPC)</li>
   *   <li>0x20 - loading model/anims</li>
   * </ul>
   */
  public int flags_19e;
  public int vramSlot_1a0;
  /**
   * Not just char index
   * <ul>
   *   <li>0x1 - dragoon</li>
   * </ul>
   */
  public int charIndex_1a2;
  public int _1a4;
  public int _1a6;

  @Method(0x800c90b0L)
  public boolean isModelLoaded() {
    return loadingMonsterModels.get() == 0 && (this.flags_19e & 0x20) == 0 && (this._1a4 >= 0 || this.mrg_00 != null && this.mrg_00.get(32).hasVirtualSize()) && this.isAssetLoaded(0);
  }

  @Method(0x800ca054L)
  public boolean isAssetLoaded(final int assetIndex) {
    final CombatantAsset0c asset = this.assets_14[assetIndex];

    if(asset instanceof CombatantAsset0c.AnimType || asset instanceof CombatantAsset0c.GlobalAssetType) {
      return true;
    }

    if(asset instanceof CombatantAsset0c.BpeType || asset instanceof CombatantAsset0c.TimType) {
      return asset.isLoaded_0b && asset.assetIndex_04 >= 0;
    }

    //LAB_800ca0f8
    return false;
  }

  public record ItemDrop(int chance, InventoryEntry item) {
  }
}
