package legend.game.combat.types;

import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.types.battlestate.AdditionExtra04;
import legend.game.combat.types.battlestate.SpecialEffects20;
import legend.game.combat.types.battlestate.Status04;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptState;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;

import java.util.Arrays;

import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.combat.Bttl_800c.aliveBentCount_800c669c;
import static legend.game.combat.Bttl_800c.allBentCount_800c66d0;
import static legend.game.combat.Bttl_800c.currentAssetIndex_800c66b4;

public class BattleStateEf4 {
  public final SpecialEffects20[] specialEffect_00 = new SpecialEffects20[10];

  public int _180;
  public int _184;
  public int _188;
  public int _18c;
  public int _190;
  public int _194;
  public int _198;
  public int _19c;
  public int _1a0;
  public int _1a4;
  public int _1a8;
  public int _1ac;
  public int _1b0;
  public int _1b4;
  public int _1b8;
  public int _1bc;
  public int _1c0;
  public int _1c4;
  public int _1c8;
  public int _1cc;
  public int _1d0;
  public int _1d4;
  public int _1d8;
  public int _1dc;
  public int _1e0;
  public int _1e4;
  public int _1e8;
  public int _1ec;
  public int _1f0;
  public int _1f4;
  public int _1f8;
  public int _1fc;
  public int _200;
  public int _204;
  public int _208;
  public int _20c;
  public int _210;
  public int _214;
  public int _218;
  public int _21c;
  public int _220;
  public int _224;
  public int _228;
  public int _22c;
  public int _230;
  public int _234;
  public int _238;
  public int _23c;
  public int _240;
  public int _244;
  public int _248;
  public int _24c;
  public int _250;
  public int _254;
  public int _258;
  public int _25c;
  public int _260;
  public int _264;
  public int _268;
  public int _26c;
  public int _270;
  public int _274;
  public int _278;
  public int _27c;
  /**
   * Number of addition hits completed (normal or dragoon).
   * For normal additions, this includes a final failed press, as it still hits.
   * For dragoon additions, this means whatever stage was achieved.
   */
  public int numCompleteAdditionHits_280;
  public int _284;
  /**
   * <ul>
   *   <li>0x0 - no counterattack</li>
   *   <li>0x1 - counterattack effects allocated</li>
   *   <li>0x2 - played monster counter animation</li>
   *   <li>0x3 - counter failed</li>
   * </ul>
   */
  public int counterAttackStage_288;
  public int _28c;
  /** Used in player combat script */
  public int _290;
  /** Indexed by char slot */
  public final int[] _294 = new int[3];
  public int _2a0;
  public int _2a4;
  public int _2a8;
  public int _2ac;
  /**
   * <ul>
   *   <li>0x00 Dart</li>
   *   <li>0x01 Lavitz</li>
   *   <li>0x02 Shana</li>
   *   <li>0x03 Rose</li>
   *   <li>0x04 Haschel</li>
   *   <li>0x05 Albert</li>
   *   <li>0x06 Meru</li>
   *   <li>0x07 Kongol</li>
   *   <li>0x08 Miranda</li>
   * </ul>
   */
  public int specialFlag_2b0;
  /** Used in player combat script */
  public int _2b4;
  public int _2b8;
  public int _2bc;
  public int _2c0;
  public int _2c4;
  public int _2c8;
  public int _2cc;
  public int _2d0;
  public int _2d4;
  public int _2d8;
  public int _2dc;
  public int _2e0;
  public int _2e4;
  /** Indexed by char slot */
  public final int[] _2e8 = new int[3];
  public int _2f4;
  public int _2f8;
  public int _2fc;
  public int _300;
  public int _304;
  public int _308;
  public int _30c;
  public int _310;
  public int _314;
  public int _318;
  public int _31c;
  public int _320;
  /**
   * <ul>
   *   <li>0x0 continue addition</li>
   *   <li>0x1 counter succeeded</li>
   *   <li>0x2 addition failed</li>
   *   <li>0x10 attack missed</li>
   *   <li>0x20 addition completed</li>
   *   <li>0x80 counter failed</li>
   * </ul>
   */
  public int additionState_324;
  public int _328;
  public int _32c;
  public int _330;
  /** Pretty sure this is character index loading (boolean) */
  public final int[] _334 = new int[3];
  public int _340;
  public int _344;
  public int _348;
  /** Indexed by char slot */
  public final int[] _34c = new int[3];
  /** Sequence volume is stored in here when player combat script is initialized */
  public int sequenceVolume_358;
  public int _35c;
  public int _360;
  public int _364;
  public int _368;
  public int _36c;
  public int _370;
  public int _374;
  public int _378;
  public int _37c;
  public int _380;
  public final Status04[] status_384 = new Status04[8];
  public int _3a4;
  public int _3a8;
  public int _3ac;
  public int _3b0;
  public int _3b4;
  public int _3b8;
  public int _3bc;
  public int _3c0;
  public int _3c4;
  public int _3c8;
  public int _3cc;
  public int _3d0;
  public int _3d4;
  public int _3d8;
  public int _3dc;
  public int _3e0;
  public int _3e4;
  public int _3e8;
  public int _3ec;
  public int _3f0;
  public int _3f4;
  public int _3f8;
  public int _3fc;
  public int _400;
  public int _404;
  public int _408;
  public int _40c;
  public int _410;
  public int _414;
  public int _418;
  public int _41c;
  public int _420;
  public int _424;
  public int _428;
  public int _42c;
  public int _430;
  public int _434;
  public int _438;
  public int _43c;
  public int _440;
  public int _444;
  public int _448;
  public int _44c;
  public int _450;
  public int _454;
  public int _458;
  /** Briefly a bitset of which battle entities have status afflictions, maybe a flag that says this character's status effects forked script hasn't finished yet */
  public int _45c;
  /** Indexed by char slot, something to do with bewitched (set to -1 if not bewitched) */
  public final int[] _460 = new int[3];
  /** Combat stage ID is stored here when player combat script is initialized */
  public int _46c;
  /** Used in player combat script */
  public int _470;
  public final AdditionExtra04[] additionExtra_474 = new AdditionExtra04[8];
  public int _494;
  public int _498;
  public int _49c;
  public int _4a0;
  public int _4a4;
  public int _4a8;
  public int _4ac;
  public int _4b0;
  public int _4b4;
  public int _4b8;
  public int _4bc;
  public int _4c0;
  public int _4c4;
  public int _4c8;
  public int _4cc;
  public int _4d0;
  public int _4d4;
  public int _4d8;
  public int _4dc;
  public int _4e0;
  public int _4e4;
  public int _4e8;
  /** Used in player combat script */
  public int _4ec;
  /** Used in player combat script */
  public int _4f0;
  /** Used in player combat script */
  public int _4f4;
  /** Used in player combat script */
  public int _4f8;
  /** Used in player combat script */
  public int _4fc;
  public int damageDealt_500;
  /** Used in player combat script */
  public int _504;
  public int _508;
  public int _50c;
  public int _510;
  public int _514;
  public int _518;
  public int _51c;
  public int _520;
  public int _524;
  public int _528;
  public int _52c;
  public int _530;
  public int _534;
  public int _538;
  public int _53c;
  public int _540;
  public int _544;
  public int _548;
  public int _54c;
  public int dragonBlockStaff_550;
  /** Flag 0x1 causes the player combat script to rewind and yield in its main loop */
  public int _554;
  /** Bit set specifying which bent indices are being targeted */
  public int attackTargets_558;
  public int _55c;
  public int _560;
  public int _564;
  public int _568;
  public int _56c;
  /** Used to store index into spellId array */
  public int monsterMoveId_570;
  public int _574;
  public int _578;
  public int _57c;
  public final BattleAsset08[] assets_580 = new BattleAsset08[0x100];
  // This was used for storing animation files in VRAM
//  public final int[] y_d80 = new int[3];
  public final CompressedAsset08[] compressedAssets_d8c = new CompressedAsset08[16];
  public final ScriptState<? extends BattleEntity27c>[] allBents_e0c = new ScriptState[13];
  public final ScriptState<PlayerBattleEntity>[] charBents_e40 = new ScriptState[4];
  public final ScriptState<MonsterBattleEntity>[] monsterBents_e50 = new ScriptState[10];
  public final ScriptState<? extends BattleEntity27c>[] aliveBents_e78 = new ScriptState[13];
  public final ScriptState<PlayerBattleEntity>[] aliveCharBents_eac = new ScriptState[4];
  public final ScriptState<MonsterBattleEntity>[] aliveMonsterBents_ebc = new ScriptState[10];
  // Reads directly from gameState now
//  public TransformationMode morphMode_ee4;

  // nodart code no longer uses this
//  public final Pointer<PartyPermutation08> partyPermutation_ee8;
  public int stageProgression_eec;
  public int _ef0;

  public BattleStateEf4() {
    Arrays.setAll(this.specialEffect_00, i -> new SpecialEffects20());
    Arrays.setAll(this.status_384, i -> new Status04());
    Arrays.setAll(this.additionExtra_474, AdditionExtra04::new);
    Arrays.setAll(this.compressedAssets_d8c, i -> new CompressedAsset08());
    Arrays.setAll(this.assets_580, i -> new BattleAsset08());
  }

  public void calculateInitialTurnValues() {
    for(int i = 0; i < allBentCount_800c66d0.get(); i++) {
      final ScriptState<? extends BattleEntity27c> bentState = this.allBents_e0c[i];
      final BattleEntity27c bent = bentState.innerStruct_00;

      if((bentState.storage_44[7] & 0x4) != 0) {
        bent.turnValue_4c = simpleRand() * 0xd9 / 0x10000;
      } else {
        //LAB_800c7b3c
        bent.turnValue_4c = simpleRand() * 0xa7 / 0x10000 + 0x32;
      }

      //LAB_800c7b68
    }
  }

  @Method(0x800c7e24L)
  public ScriptState<? extends BattleEntity27c> getForcedTurnBent() {
    //LAB_800c7e54
    for(int i = 0; i < aliveBentCount_800c669c.get(); i++) {
      final ScriptState<? extends BattleEntity27c> bentState = this.aliveBents_e78[i];
      if(bentState != null && (bentState.storage_44[7] & 0x20) != 0) {
        return bentState;
      }

      //LAB_800c7e8c
    }

    //LAB_800c7e98
    return null;
  }

  @Method(0x800c7ea0L)
  public ScriptState<? extends BattleEntity27c> getCurrentTurnBent() {
    //LAB_800c7ee4
    for(int s4 = 0; s4 < 32; s4++) {
      //LAB_800c7ef0
      int highestTurnValue = 0;
      int highestCombatantindex = 0;
      for(int combatantIndex = 0; combatantIndex < aliveBentCount_800c669c.get(); combatantIndex++) {
        final int turnValue = this.aliveBents_e78[combatantIndex].innerStruct_00.turnValue_4c;

        if(highestTurnValue <= turnValue) {
          highestTurnValue = turnValue;
          highestCombatantindex = combatantIndex;
        }

        //LAB_800c7f30
      }

      //LAB_800c7f40
      if(highestTurnValue > 0xd9) {
        final ScriptState<? extends BattleEntity27c> state = this.aliveBents_e78[highestCombatantindex];
        state.innerStruct_00.turnValue_4c = highestTurnValue - 0xd9;

        if((state.storage_44[7] & 0x4) == 0) {
          gameState_800babc8._b8++;
        }

        //LAB_800c7f9c
        return state;
      }

      //LAB_800c7fa4
      //LAB_800c7fb0
      for(int combatantIndex = 0; combatantIndex < aliveBentCount_800c669c.get(); combatantIndex++) {
        final BattleEntity27c bent = this.aliveBents_e78[combatantIndex].innerStruct_00;
        highestTurnValue = bent.stats.getStat(CoreMod.SPEED_STAT.get()).get() * (simpleRand() + 0x4_4925);
        final int v1 = (int)(highestTurnValue * 0x35c2_9183L >>> 32) >> 16; //TODO _pretty_ sure this is roughly /312,110 (seems oddly specific?)
        bent.turnValue_4c += v1;
      }

      //LAB_800c8028
    }

    //LAB_800c8040
    return this.aliveCharBents_eac[0];
  }

  @Method(0x800ca31cL)
  public TmdAnimationFile getAnimationGlobalAsset(final CombatantStruct1a8 combatant, final int animIndex) {
    final CombatantAsset0c asset = combatant.assets_14[animIndex];

    if(asset instanceof final CombatantAsset0c.AnimType animType) {
      return animType.anim_00;
    }

    if(asset instanceof final CombatantAsset0c.GlobalAssetType globalAssetType) {
      return new TmdAnimationFile(this.getGlobalAsset(globalAssetType.assetIndex_00).data_00);
    }

    if(asset instanceof CombatantAsset0c.BpeType || asset instanceof CombatantAsset0c.TimType) {
      if(asset.isLoaded_0b && asset.assetIndex_04 >= 0) {
        return new TmdAnimationFile(this.getGlobalAsset(asset.assetIndex_04).data_00);
      }
    }

    return null;
  }

  @Method(0x800ca9b4L)
  public void deallocateLoadedGlobalAssets() {
    //LAB_800ca9d8
    for(int assetIndex = 0; assetIndex < 0x100; assetIndex++) {
      final BattleAsset08 asset = this.assets_580[assetIndex];
      if(asset.state_04 >= 2) {
        asset.state_04 = 0;
      }
    }
  }

  @Method(0x800caa20L)
  public int getFreeGlobalAssetIndex() {
    currentAssetIndex_800c66b4.incr();
    if(currentAssetIndex_800c66b4.get() >= 0x100) {
      currentAssetIndex_800c66b4.set(0);
    }

    //LAB_800caa44
    //LAB_800caa64
    for(int i = currentAssetIndex_800c66b4.get(); i < 0x100; i++) {
      final BattleAsset08 asset = this.assets_580[i];

      if(asset.state_04 == 0) {
        //LAB_800caacc
        currentAssetIndex_800c66b4.set(i);
        asset.data_00 = null;
        asset.state_04 = 1;
        return i;
      }
    }

    //LAB_800caa88
    //LAB_800caaa4
    for(int i = 0; i < currentAssetIndex_800c66b4.get(); i++) {
      final BattleAsset08 asset = this.assets_580[i];

      if(asset.state_04 == 0) {
        //LAB_800caacc
        currentAssetIndex_800c66b4.set(i);
        asset.data_00 = null;
        asset.state_04 = 1;
        return i;
      }
    }

    //LAB_800caac4
    throw new RuntimeException("Failed to find free slot");
  }

  @Method(0x800caae4L)
  public int loadGlobalAsset(final FileData fileData, final int state) {
    final int index = this.getFreeGlobalAssetIndex();

    final BattleAsset08 asset = this.assets_580[index];
    asset.data_00 = fileData;
    asset.state_04 = state;

    //LAB_800cab3c
    return index;
  }

  @Method(0x800cac38L)
  public int loadGlobalAsset(final int drgnIndex, final int fileIndex) {
    final int index = this.getFreeGlobalAssetIndex();

    loadDrgnFile(drgnIndex, fileIndex, data -> this.globalAssetLoaded(data, index));

    //LAB_800cac98
    return index;
  }

  @Method(0x800cacb0L)
  public void globalAssetLoaded(final FileData data, final int assetIndex) {
    final BattleAsset08 asset = this.assets_580[assetIndex];

    if(asset.state_04 == 1) {
      asset.data_00 = data;
      asset.state_04 = 2;
    }

    //LAB_800cad04
  }

  @Method(0x800cad50L)
  public BattleAsset08 getGlobalAsset(final int assetIndex) {
    return this.assets_580[assetIndex];
  }

  @Method(0x800cad64L)
  public void deallocateGlobalAsset(final int assetIndex) {
    final BattleAsset08 asset = this.assets_580[assetIndex];

    if(asset.state_04 != 1) {
      asset.data_00 = null;
    }

    //LAB_800cada8
    asset.state_04 = 0;
  }
}
