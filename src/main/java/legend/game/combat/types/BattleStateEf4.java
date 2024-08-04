package legend.game.combat.types;

import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.types.battlestate.AdditionExtra04;
import legend.game.combat.types.battlestate.StatusConditions20;
import legend.game.combat.types.battlestate.Status04;
import legend.game.scripting.ScriptState;
import legend.game.types.TmdAnimationFile;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;

import java.util.Arrays;

import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class BattleStateEf4 {
  public final StatusConditions20[] statusConditions_00 = new StatusConditions20[10];

  public int _180;
  /**
   * For cutscene DEFFs, combat vars 1 to 10 are used to store combatant indices.
   * Enemy scripts may use them for battle entities instead
   */
  public int combatantBentIndex_184;
  public int combatantBentIndex_188;
  public int combatantBentIndex_18c;
  public int combatantBentIndex_190;
  public int combatantBentIndex_194;
  public int combatantBentIndex_198;
  public int combatantBentIndex_19c;
  public int combatantBentIndex_1a0;
  public int combatantBentIndex_1a4;
  public int combatantBentIndex_1a8;
  /**
   * Both cutscene deffs and some enemy scripts use vars 11-20 as script indices
   * Some enemy scripts use these for other purposes instead
   */
  public int scriptIndex_1ac;
  public int scriptIndex_1b0;
  public int scriptIndex_1b4;
  public int scriptIndex_1b8;
  public int scriptIndex_1bc;
  public int scriptIndex_1c0;
  public int scriptIndex_1c4;
  public int scriptIndex_1c8;
  public int scriptIndex_1cc;
  public int scriptIndex_1d0;
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

  /**
   * For cutscene deffs:
   * <ul>
   *   <li>0x0 - Resources not loaded yet</li>
   *   <li>0x4 - Resources loaded</li>
   * </ul>
   * Purpose of usage for enemy scripts is unknown
   */
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

  /** Only used in player combat script for third param of 800d19ec */
  public int additionStarbustEffectCountParam_328;
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
  public int scriptEffectTableJumpIndex_380;
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

  /** Hardcoded to be in range 1-999 (inclusive). If out of range, 100% is used */
  public int magicItemDamageMultiplier_3cc;
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
  /** Bitset of which scripts are currently running the status afflictions entrypoint */
  public int scriptsProcessingStatusAfflictions_45c;
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

  /** Has a value between 5-8 (inclusive). If out of range, 5 is used. Button mashing effect for enemies.*/
  public int magicItemLevel_4a8;
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
  /**
   * <ul>
   *   <li>0x1 - causes the player combat script to rewind and yield in its main loop</li>
   *   <li>0x2 - something related to curing petrify animation loading</li>
   * </ul>
   */
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
  public final ScriptState<PlayerBattleEntity>[] playerBents_e40 = new ScriptState[4];
  public final ScriptState<MonsterBattleEntity>[] monsterBents_e50 = new ScriptState[10];
  public final ScriptState<? extends BattleEntity27c>[] aliveBents_e78 = new ScriptState[13];
  public final ScriptState<PlayerBattleEntity>[] alivePlayerBents_eac = new ScriptState[4];
  public final ScriptState<MonsterBattleEntity>[] aliveMonsterBents_ebc = new ScriptState[10];
  // Reads directly from gameState now
//  public TransformationMode morphMode_ee4;

  // nodart code no longer uses this
//  public final Pointer<PartyPermutation08> partyPermutation_ee8;
  public int battlePhase_eec;
  /** Only used in 800db8b0 inside DRGN1/401 */
  public int cameraControllerScriptTicksParam_ef0;

  private int aliveBentCount_800c669c;
  private int currentAssetIndex_800c66b4;

  private int allBentCount_800c66d0;
  private int aliveMonsterCount_800c6758;
  private int alivePlayerCount_800c6760;
  private int monsterCount_800c6768;
  /** The number of player chars in combat (i.e. 1-3) */
  private int playerCount_800c677c;

  public BattleStateEf4() {
    Arrays.setAll(this.statusConditions_00, i -> new StatusConditions20());
    Arrays.setAll(this.status_384, i -> new Status04());
    Arrays.setAll(this.additionExtra_474, AdditionExtra04::new);
    Arrays.setAll(this.compressedAssets_d8c, i -> new CompressedAsset08());
    Arrays.setAll(this.assets_580, i -> new BattleAsset08());
  }

  public void clear() {
    this.allBentCount_800c66d0 = 0;
    this.monsterCount_800c6768 = 0;
    this.playerCount_800c677c = 0;
  }

  public void addMonster(final ScriptState<MonsterBattleEntity> state) {
    this.allBents_e0c[this.allBentCount_800c66d0] = state;
    this.monsterBents_e50[this.monsterCount_800c6768] = state;
    state.innerStruct_00.bentSlot_274 = this.allBentCount_800c66d0;
    state.innerStruct_00.charSlot_276 = this.monsterCount_800c6768;
    this.allBentCount_800c66d0++;
    this.monsterCount_800c6768++;
  }

  public void removeMonster(final MonsterBattleEntity monster) {
    this.allBentCount_800c66d0--;

    //LAB_800cb0d4
    for(int i = monster.bentSlot_274; i < this.allBentCount_800c66d0; i++) {
      this.allBents_e0c[i] = this.allBents_e0c[i + 1];
      this.allBents_e0c[i].innerStruct_00.bentSlot_274 = i;
    }

    //LAB_800cb11c
    this.monsterCount_800c6768--;

    //LAB_800cb168
    for(int i = monster.charSlot_276; i < this.monsterCount_800c6768; i++) {
      this.monsterBents_e50[i] = this.monsterBents_e50[i + 1];
      this.monsterBents_e50[i].innerStruct_00.charSlot_276 = i;
    }
  }

  public void addPlayer(final ScriptState<PlayerBattleEntity> state) {
    this.allBents_e0c[this.allBentCount_800c66d0] = state;
    this.playerBents_e40[this.playerCount_800c677c] = state;
    state.innerStruct_00.bentSlot_274 = this.allBentCount_800c66d0;
    state.innerStruct_00.charSlot_276 = this.playerCount_800c677c;
    this.allBentCount_800c66d0++;
    this.playerCount_800c677c++;
  }

  public void removePlayer(final PlayerBattleEntity player) {
    this.allBentCount_800c66d0--;

    //LAB_800cb0d4
    for(int i = player.bentSlot_274; i < this.allBentCount_800c66d0; i++) {
      this.allBents_e0c[i] = this.allBents_e0c[i + 1];
      this.allBents_e0c[i].innerStruct_00.bentSlot_274 = i;
    }

    //LAB_800cb11c
    //LAB_800cb1b8
    this.playerCount_800c677c--;

    //LAB_800cb1f4
    for(int i = player.charSlot_276; i < this.playerCount_800c677c; i++) {
      this.playerBents_e40[i] = this.playerBents_e40[i + 1];
      this.playerBents_e40[i].innerStruct_00.charSlot_276 = i;
    }
  }

  public PlayerBattleEntity getPlayerById(final int charId) {
    for(int i = 0; i < this.playerCount_800c677c; i++) {
      if(this.playerBents_e40[i].innerStruct_00.charId_272 == charId) {
        return this.playerBents_e40[i].innerStruct_00;
      }
    }

    return null;
  }

  public MonsterBattleEntity getMonsterById(final int monsterId) {
    for(int i = 0; i < this.monsterCount_800c6768; i++) {
      if(this.monsterBents_e50[i].innerStruct_00.charId_272 == monsterId) {
        return this.monsterBents_e50[i].innerStruct_00;
      }
    }

    return null;
  }

  public int getMonsterCount() {
    return this.monsterCount_800c6768;
  }

  public int getAliveMonsterCount() {
    return this.aliveMonsterCount_800c6758;
  }

  public int getPlayerCount() {
    return this.playerCount_800c677c;
  }

  public int getAlivePlayerCount() {
    return this.alivePlayerCount_800c6760;
  }

  public int getAllBentCount() {
    return this.allBentCount_800c66d0;
  }

  public int getAliveBentCount() {
    return this.aliveBentCount_800c669c;
  }

  public boolean hasBents() {
    return this.allBentCount_800c66d0 != 0;
  }

  public boolean hasAlivePlayers() {
    return this.alivePlayerCount_800c6760 != 0;
  }

  public boolean hasAliveMonsters() {
    return this.aliveMonsterCount_800c6758 != 0;
  }

  public void disableBents() {
    for(int i = 0; i < this.playerCount_800c677c; i++) {
      this.playerBents_e40[i].loadScriptFile(null);
    }
  }

  public void deallocateBents() {
    while(this.allBentCount_800c66d0 > 0) {
      this.allBents_e0c[0].deallocateWithChildren();
    }
  }

  @Method(0x800187ccL)
  public boolean areCharacterModelsLoaded() {
    //LAB_80018800
    for(int charSlot = 0; charSlot < this.playerCount_800c677c; charSlot++) {
      if(!this.playerBents_e40[charSlot].innerStruct_00.combatant_144.isModelLoaded()) {
        return false;
      }
    }

    //LAB_80018850
    //LAB_80018854
    return true;
  }

  @Method(0x8001886cL)
  public boolean areMonsterModelsLoaded() {
    //LAB_800188a09
    for(int i = 0; i < this.monsterCount_800c6768; i++) {
      if(!this.monsterBents_e50[i].innerStruct_00.combatant_144.isModelLoaded()) {
        return false;
      }
    }

    //LAB_800188f0
    //LAB_800188f4
    return true;
  }

  @Method(0x800c7304L)
  public void cacheLivingBents() {
    int i;
    int count;
    //LAB_800c7330
    for(i = 0, count = 0; i < this.allBentCount_800c66d0; i++) {
      final ScriptState<? extends BattleEntity27c> bentState = this.allBents_e0c[i];
      if((bentState.storage_44[7] & 0x40) == 0) {
        this.aliveBents_e78[count] = bentState;
        count++;
      }

      //LAB_800c736c
    }

    //LAB_800c737c
    this.aliveBentCount_800c669c = count;

    //LAB_800c73b0
    for(i = 0, count = 0; i < this.playerCount_800c677c; i++) {
      final ScriptState<PlayerBattleEntity> playerState = this.playerBents_e40[i];
      if((playerState.storage_44[7] & 0x40) == 0) {
        this.alivePlayerBents_eac[count] = playerState;
        count++;
      }

      //LAB_800c73ec
    }

    //LAB_800c73fc
    this.alivePlayerCount_800c6760 = count;

    //LAB_800c7430
    for(i = 0, count = 0; i < this.monsterCount_800c6768; i++) {
      final ScriptState<MonsterBattleEntity> monsterState = this.monsterBents_e50[i];
      if((monsterState.storage_44[7] & 0x40) == 0) {
        this.aliveMonsterBents_ebc[count] = monsterState;
        count++;
      }

      //LAB_800c746c
    }

    //LAB_800c747c
    this.aliveMonsterCount_800c6758 = count;
  }

  public void calculateInitialTurnValues() {
    for(int i = 0; i < this.allBentCount_800c66d0; i++) {
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
    for(int i = 0; i < this.aliveBentCount_800c669c; i++) {
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
      for(int combatantIndex = 0; combatantIndex < this.aliveBentCount_800c669c; combatantIndex++) {
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
      for(int combatantIndex = 0; combatantIndex < this.aliveBentCount_800c669c; combatantIndex++) {
        final BattleEntity27c bent = this.aliveBents_e78[combatantIndex].innerStruct_00;
        highestTurnValue = bent.stats.getStat(LodMod.SPEED_STAT.get()).get() * (simpleRand() + 0x4_4925);
        final int v1 = (int)(highestTurnValue * 0x35c2_9183L >>> 32) >> 16; //TODO _pretty_ sure this is roughly /312,110 (seems oddly specific?)
        bent.turnValue_4c += v1;
      }

      //LAB_800c8028
    }

    //LAB_800c8040
    return this.alivePlayerBents_eac[0];
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
    this.currentAssetIndex_800c66b4++;
    if(this.currentAssetIndex_800c66b4 >= 0x100) {
      this.currentAssetIndex_800c66b4 = 0;
    }

    //LAB_800caa44
    //LAB_800caa64
    for(int i = this.currentAssetIndex_800c66b4; i < 0x100; i++) {
      final BattleAsset08 asset = this.assets_580[i];

      if(asset.state_04 == 0) {
        //LAB_800caacc
        this.currentAssetIndex_800c66b4 = i;
        asset.data_00 = null;
        asset.state_04 = 1;
        return i;
      }
    }

    //LAB_800caa88
    //LAB_800caaa4
    for(int i = 0; i < this.currentAssetIndex_800c66b4; i++) {
      final BattleAsset08 asset = this.assets_580[i];

      if(asset.state_04 == 0) {
        //LAB_800caacc
        this.currentAssetIndex_800c66b4 = i;
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
