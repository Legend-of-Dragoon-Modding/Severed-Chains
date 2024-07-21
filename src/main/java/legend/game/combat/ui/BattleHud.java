package legend.game.combat.ui;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.characters.Element;
import legend.game.characters.VitalsStat;
import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleMenuBackgroundDisplayMetrics0c;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;
import legend.game.combat.environment.CombatPortraitBorderMetrics0c;
import legend.game.combat.environment.NameAndPortraitDisplayMetrics0c;
import legend.game.combat.environment.SpBarBorderMetrics04;
import legend.game.combat.types.BattleHudStatLabelMetrics0c;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.Item;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.events.battle.BattleDescriptionEvent;
import legend.game.modding.events.battle.StatDisplayEvent;
import legend.game.modding.events.inventory.RepeatItemReturnEvent;
import legend.game.scripting.ScriptState;
import legend.game.types.ActiveStatsa0;
import legend.game.types.Translucency;
import legend.lodmod.LodMod;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.characterStatsLoaded_800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.combat.Battle.additionNames_800fa8d4;
import static legend.game.combat.Battle.melbuStageToMonsterNameIndices_800c6f30;
import static legend.game.combat.Battle.spellStats_800fa0b8;
import static legend.game.combat.SBtld.loadAdditions;

public class BattleHud {
  private static final int[] repeatItemIds_800c6e34 = {224, 227, 228, 230, 232, 235, 236, 237, 238};

  private static final CombatPortraitBorderMetrics0c[] combatPortraitBorderVertexCoords_800c6e9c = {
    new CombatPortraitBorderMetrics0c(0, 0, 1, 1, 0, 0, 0, 0, -1, -1, 1, -1),
    new CombatPortraitBorderMetrics0c(2, 2, 3, 3, 0, 0, 0, 0, -1, 1, 1, 1),
    new CombatPortraitBorderMetrics0c(0, 0, 2, 2, 0, 1, 0, -1, -1, -1, -1, 1),
    new CombatPortraitBorderMetrics0c(1, 1, 3, 3, 0, 1, 0, -1, 1, -1, 1, 1),
  };
  private static final BattleHudStatLabelMetrics0c[] battleHudStatLabelMetrics_800c6ecc = {
    new BattleHudStatLabelMetrics0c(21, -15, 96, 32, 8, 8),
    new BattleHudStatLabelMetrics0c(21, -5, 96, 32, 8, 8),
    new BattleHudStatLabelMetrics0c(-18, -19, 0, 32, 16, 32),
  };

  private static final int[][] spBarColours_800c6f04 = {{16, 87, 240, 9, 50, 138}, {16, 87, 240, 9, 50, 138}, {0, 181, 142, 0, 102, 80}, {206, 204, 17, 118, 117, 10}, {230, 139, 0, 132, 80, 0}, {181, 0, 0, 104, 0, 0}, {16, 87, 240, 9, 50, 138}};
  private static final int[] digitOffsetX_800c7014 = {0, 27, 0, 27, 42};
  private static final int[] digitOffsetY_800c7014 = {-15, -15, -5, -5, 6};
  private static final int[] floatingTextType1DigitUs_800c7028 = {88, 16, 24, 32, 40, 48, 56, 64, 72, 80};
  private static final int[] floatingTextType3DigitUs_800c70e0 = {16, 24, 32, 40, 48, 56, 64, 72, 80, 88};
  private static final Vector2i[] battleUiElementClutVramXy_800c7114 = {new Vector2i(0x2c0, 0x1f0), new Vector2i(0x380, 0x130)};
  private static final int[] iconFlags_800c7194 = {4, 1, 5, 6, 2, 9, 3, 7};
  private static final int[] battleMenuIconStates_800c71e4 = {0, 1, 2, 1};
  private static final int[] uiTextureElementBrightness_800c71ec = {96, 64, -128};
  private static final int[] targetArrowOffsetY_800fb188 = {-20, -18, -16, -14, -12, -14, -16, -18};
  private static final int[] battleHudYOffsets_800fb198 = {46, 208, -128, 0};

  /** Targeting ("All allies", "All players", "All") */
  private static final String[] targeting_800fb36c = { "All allies", "All enemies", "All" };
  public static final String[] playerNames_800fb378 = {
    "Dart", "Lavitz", "Shana", "Rose", "Haschel",
    "Albert", "Meru", "Kongol", "Miranda", "DivinDGDart",
  };
  /** Poisoned, Dispirited, Weapon blocked, Stunned, Fearful, Confused, Bewitched, Petrified */
  private static final String[] ailments_800fb3a0 = {
    "Poisoned", "Dispirited", "Weapon blocked", "Stunned", "Fearful",
    "Confused", "Bewitched", "Petrified",
  };

  private static final NameAndPortraitDisplayMetrics0c[] hudNameAndPortraitMetrics_800fb444 = {
    new NameAndPortraitDisplayMetrics0c(104, 32, 24, 8, 24, 0, 24, 32, 0),
    new NameAndPortraitDisplayMetrics0c(112, 56, 40, 8, 48, 0, 24, 32, 2),
    new NameAndPortraitDisplayMetrics0c(128, 32, 32, 8, 0, 0, 24, 32, 1),
    new NameAndPortraitDisplayMetrics0c(0, 232, 32, 8, 72, 0, 24, 32, 3),
    new NameAndPortraitDisplayMetrics0c(216, 24, 39, 8, 96, 0, 24, 32, 4),
    new NameAndPortraitDisplayMetrics0c(152, 48, 40, 8, 120, 0, 24, 32, 5),
    new NameAndPortraitDisplayMetrics0c(32, 232, 32, 8, 144, 0, 24, 32, 6),
    new NameAndPortraitDisplayMetrics0c(152, 56, 40, 8, 168, 0, 24, 32, 7),
    new NameAndPortraitDisplayMetrics0c(64, 232, 40, 8, 192, 0, 24, 32, 8),
    new NameAndPortraitDisplayMetrics0c(104, 32, 24, 8, 24, 0, 24, 32, 0),
  };
  private static final SpBarBorderMetrics04[] spBarBorderMetrics_800fb46c = {
    new SpBarBorderMetrics04(1, 6, 39, 6),
    new SpBarBorderMetrics04(1, 7, 1, 11),
    new SpBarBorderMetrics04(39, 7, 39, 11),
    new SpBarBorderMetrics04(1, 12, 39, 12),
  };
  private static final SpBarBorderMetrics04[] spBarFlashingBorderMetrics_800fb47c = {
    new SpBarBorderMetrics04(2, 7, 38, 7),
    new SpBarBorderMetrics04(2, 8, 2, 10),
    new SpBarBorderMetrics04(38, 8, 38, 10),
    new SpBarBorderMetrics04(2, 11, 38, 11),
  };

  private static final BattleMenuBackgroundDisplayMetrics0c[] battleMenuBackgroundDisplayMetrics_800fb614 = {
    new BattleMenuBackgroundDisplayMetrics0c(0, -8, -8, 8, 8),
    new BattleMenuBackgroundDisplayMetrics0c(1, 0, -8, 8, 8),
    new BattleMenuBackgroundDisplayMetrics0c(2, -8, 0, 8, 8),
    new BattleMenuBackgroundDisplayMetrics0c(3, 0, 0, 8, 8),
    new BattleMenuBackgroundDisplayMetrics0c(0, 0, -8, 0, 8),
    new BattleMenuBackgroundDisplayMetrics0c(0, -8, 0, 8, 0),
    new BattleMenuBackgroundDisplayMetrics0c(1, 0, 0, 8, 0),
    new BattleMenuBackgroundDisplayMetrics0c(2, 0, 0, 0, 8),
  };

  public static final int[][] battleMenuIconHeights_800fb6bc = {
    {16, 16, 16},
    {16, 16, 16},
    {16, 24, 24},
    {16, 24, 24},
    {16, 24, 24},
    {16, 24, 24},
    {16, 16, 16},
    {16, 16, 16},
    {16, 24, 24},
  };

  public final BattleMenuStruct58 battleMenu_800c6c34 = new BattleMenuStruct58(this);
  public final SpellAndItemMenuA4 spellAndItemMenu_800c6b60 = new SpellAndItemMenuA4(this);

  public int currentCameraPositionIndicesIndex_800c66b0;

  /** Only ever set to 1. 0 will set it to the top of the screen. */
  private int battleHudYOffsetIndex_800c6c38;

  private final FloatingNumberC4[] floatingNumbers_800c6b5c = new FloatingNumberC4[12];
  private int countCameraPositionIndicesIndices_800c6ba0;
  private int currentCameraPositionIndicesIndicesIndex_800c6ba1;
  private final BattleDisplayStats144[] displayStats_800c6c2c = new BattleDisplayStats144[3];
  private final int[] cameraPositionIndicesIndices_800c6c30 = new int[4];
  private final BattleHudCharacterDisplay3c[] activePartyBattleHudCharacterDisplays_800c6c40 = new BattleHudCharacterDisplay3c[3];

  private final List<CombatItem02> combatItems_800c6988 = new ArrayList<>();
  private final List<String> combatAdditions = new ArrayList<>();

  private final Battle battle;

  private UiBox battleUiBackground;
  private UiBox battleUiName;
  private final Obj[] names = new Obj[3];
  private final Obj[] portraits = new Obj[3];
  private final Obj[][] stats = new Obj[3][3];
  private final MV uiTransforms = new MV();
  private final Vector3f colourTemp = new Vector3f();

  private final Obj[] floatingTextType1Digits = new Obj[10];
  private final Obj[] type1FloatingDigits = new Obj[10];
  private final Obj[] type3FloatingDigits = new Obj[10];
  private Obj miss;

  private UiBox battleUiItemSpellList;
  private UiBox battleUiSpellList;
  private UiBox battleUiItemDescription;
  private Obj spBars;
  private final MV spBarTransforms = new MV();
  private final MV lineTransforms = new MV();

  public BattleHud(final Battle battle) {
    this.battle = battle;
    Arrays.setAll(this.floatingNumbers_800c6b5c, i -> new FloatingNumberC4());
    Arrays.setAll(this.displayStats_800c6c2c, i -> new BattleDisplayStats144());
    Arrays.setAll(this.activePartyBattleHudCharacterDisplays_800c6c40, i -> new BattleHudCharacterDisplay3c());
  }

  public boolean isFloatingNumberOnScreen() {
    for(int i = 0; i < this.floatingNumbers_800c6b5c.length; i++) {
      if(this.floatingNumbers_800c6b5c[i].state_00 != 0) {
        return true;
      }
    }

    return false;
  }

  public void clearFullSpFlags(final int charSlot) {
    this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot].flags_06 &= 0xfff3;
  }

  public void clearFloatingNumber(final int index) {
    final FloatingNumberC4 num = this.floatingNumbers_800c6b5c[index];
    num.state_00 = 0;
    num.flags_02 = 0;
  }

  @Method(0x800eca98L)
  private void drawTargetArrow(final int targetType, final int combatantIdx) {
    if(combatantIdx != -1) {
      final ScriptState<? extends BattleEntity27c> targetState;
      if(targetType == 0) {
        //LAB_800ecb00
        targetState = battleState_8006e398.playerBents_e40[combatantIdx];
      } else if(targetType == 1) {
        //LAB_800ecb1c
        targetState = battleState_8006e398.aliveMonsterBents_ebc[combatantIdx];
        //LAB_800ecaf0
      } else if(targetType == 2) {
        //LAB_800ecb38
        targetState = battleState_8006e398.allBents_e0c[combatantIdx];
      } else {
        throw new IllegalStateException("Invalid target type " + targetType);
      }

      //LAB_800ecb50
      //LAB_800ecb54
      final BattleEntity27c target = targetState.innerStruct_00;
      final int colour;
      final VitalsStat targetHp = target.stats.getStat(LodMod.HP_STAT.get());
      if(targetHp.getCurrent() > targetHp.getMax() / 4) {
        colour = targetHp.getCurrent() > targetHp.getMax() / 2 ? 0 : 1;
      } else {
        colour = 2;
      }

      //LAB_800ecb90
      this.drawTargetArrow(colour, target);
    } else {
      //LAB_800ecba4
      int count = 0;
      if(targetType == 0) {
        //LAB_800ecbdc
        count = battleState_8006e398.getPlayerCount();
      } else if(targetType == 1) {
        //LAB_800ecbec
        count = battleState_8006e398.getAliveMonsterCount();
        //LAB_800ecbc8
      } else if(targetType == 2) {
        //LAB_800ecbfc
        count = battleState_8006e398.getAliveBentCount();
      }

      //LAB_800ecc04
      //LAB_800ecc1c
      for(int i = 0; i < count; i++) {
        final ScriptState<? extends BattleEntity27c> targetBent;
        if(targetType == 0) {
          //LAB_800ecc50
          targetBent = battleState_8006e398.playerBents_e40[i];
        } else if(targetType == 1) {
          //LAB_800ecc5c
          targetBent = battleState_8006e398.aliveMonsterBents_ebc[i];
          //LAB_800ecc40
        } else if(targetType == 2) {
          //LAB_800ecc68
          targetBent = battleState_8006e398.aliveBents_e78[i];
        } else {
          throw new IllegalStateException("Invalid target type " + targetType);
        }

        //LAB_800ecc74
        //LAB_800ecc78
        final BattleEntity27c target = targetBent.innerStruct_00;

        final int colour;
        final VitalsStat targetHp = target.stats.getStat(LodMod.HP_STAT.get());
        if(targetHp.getCurrent() > targetHp.getMax() / 4) {
          colour = targetHp.getCurrent() > targetHp.getMax() / 2 ? 0 : 1;
        } else {
          colour = 2;
        }

        //LAB_800eccac
        if((targetBent.storage_44[7] & 0x4000) == 0) {
          this.drawTargetArrow(colour, target);
        }

        //LAB_800eccc8
      }
    }

    //LAB_800eccd8
  }

  /**
   * @param colour 0 = blue, 1 = yellow, 2 = red
   */
  @Method(0x800eccfcL)
  private void drawTargetArrow(final int colour, final BattleEntity27c bent) {
    final float x;
    final float y;
    final float z;
    if(bent instanceof final MonsterBattleEntity monster) {
      // X and Z are swapped
      x = -monster.targetArrowPos_78.z * 100.0f;
      y = -monster.targetArrowPos_78.y * 100.0f;
      z = -monster.targetArrowPos_78.x * 100.0f;
    } else {
      //LAB_800ecd90
      if(bent instanceof final PlayerBattleEntity player && player.isDragoon()) {
        y = -1664;
      } else {
        //LAB_800ecda4
        y = -1408;
      }

      //LAB_800ecda8
      x = 0;
      z = 0;
    }

    //LAB_800ecdac
    final Vector2f screenCoords = bent.transformRelative(x, y, z);

    //LAB_800ece9c
    this.battleMenu_800c6c34.transforms.identity();
    this.battleMenu_800c6c34.transforms.transfer.set(GPU.getOffsetX() + screenCoords.x - 8, GPU.getOffsetY() + screenCoords.y + targetArrowOffsetY_800fb188[tickCount_800bb0fc & 0x7], 112.0f);
    RENDERER.queueOrthoModel(this.battleMenu_800c6c34.targetArrows[colour], this.battleMenu_800c6c34.transforms);
  }

  @Method(0x800ef7c4L)
  public void clear() {
    this.battleHudYOffsetIndex_800c6c38 = 1;

    //LAB_800ef7d4
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleHudCharacterDisplay3c charDisplay = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];
      charDisplay.charIndex_00 = -1;
      charDisplay.flags_06 = 0;
      charDisplay.x_08 = 0;
      charDisplay.y_0a = 0;
    }

    //LAB_800ef818
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleDisplayStats144 displayStats = this.displayStats_800c6c2c[charSlot];

      //LAB_800ef820
      for(int i = 0; i < displayStats.digits_04.length; i++) {
        //LAB_800ef828
        for(int j = 0; j < displayStats.digits_04[i].length; j++) {
          displayStats.digits_04[i][j].digitValue_00 = -1;
        }
      }
    }

    //LAB_800ef878
    for(final FloatingNumberC4 num : this.floatingNumbers_800c6b5c) {
      num.state_00 = 0;
      num.flags_02 = 0;
      num.bentIndex_04 = -1;
      num.translucent_08 = false;
      num.shade_0c = 0x80;
      num.ticksRemaining_14 = -1;
      num._18 = -1;

      //LAB_800ef89c
      for(int i = 0; i < num.digits_24.length; i++) {
        final FloatingNumberDigit20 digit = num.digits_24[i];
        digit.flags_00 = 0;
        digit._04 = 0;
        digit._08 = 0;
        digit.digit_0c = -1;
      }
    }

    this.battleMenu_800c6c34.clear();
    this.spellAndItemMenu_800c6b60.clear();
  }

  @Method(0x800ef8d8L)
  public void initCharacterDisplay(final int charSlot) {
    final BattleHudCharacterDisplay3c charDisplay = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];
    charDisplay.charIndex_00 = charSlot;
    charDisplay.charId_02 = battleState_8006e398.playerBents_e40[charSlot].innerStruct_00.charId_272;
    charDisplay.flags_06 |= 0x2;
    charDisplay.x_08 = charSlot * 94 + 63;
    charDisplay.y_0a = 38;

    //LAB_800ef980
    for(int i = 0; i < 10; i++) {
      charDisplay._14[i] = 0;
    }

    final BattleDisplayStats144 displayStats = this.displayStats_800c6c2c[charSlot];
    displayStats.x_00 = charDisplay.x_08;
    displayStats.y_02 = charDisplay.y_0a;
  }

  @Method(0x800ef9e4L)
  public void draw() {
    if(this.battle.countCombatUiFilesLoaded_800c6cf4 == 6) {
      final int charCount = battleState_8006e398.getPlayerCount();

      //LAB_800efa34
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        if(this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot].charIndex_00 == -1 && characterStatsLoaded_800be5d0) {
          this.initCharacterDisplay(charSlot);
        }
        //LAB_800efa64
      }

      //LAB_800efa78
      //LAB_800efa94
      //LAB_800efaac
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        final BattleHudCharacterDisplay3c charDisplay = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];

        if(charDisplay.charIndex_00 != -1 && (charDisplay.flags_06 & 0x1) != 0 && (charDisplay.flags_06 & 0x2) != 0) {
          final PlayerBattleEntity player = battleState_8006e398.playerBents_e40[charSlot].innerStruct_00;

          final VitalsStat playerHp = player.stats.getStat(LodMod.HP_STAT.get());
          final VitalsStat playerMp = player.stats.getStat(LodMod.MP_STAT.get());
          final VitalsStat playerSp = player.stats.getStat(LodMod.SP_STAT.get());

          final int colour;
          if(playerHp.getCurrent() > playerHp.getMax() / 2) {
            colour = 1;
          } else if(playerHp.getCurrent() > playerHp.getMax() / 4) {
            colour = 2;
          } else {
            colour = 3;
          }

          //LAB_800efb30
          //LAB_800efb40
          //LAB_800efb54
          this.renderNumber(charSlot, 0, playerHp.getCurrent(), colour);
          this.renderNumber(charSlot, 1, playerHp.getMax(), 1);
          this.renderNumber(charSlot, 2, playerMp.getCurrent(), 1);
          this.renderNumber(charSlot, 3, playerMp.getMax(), 1);
          this.renderNumber(charSlot, 4, playerSp.getCurrent() / 100, 1);
          EVENTS.postEvent(new StatDisplayEvent(charSlot, player));

          charDisplay._14[1] = tickCount_800bb0fc & 0x3;

          //LAB_800efc0c
          if(playerSp.getCurrent() < playerSp.getMax()) {
            charDisplay.flags_06 &= ~0xc;
          } else {
            charDisplay.flags_06 |= 0x4;
          }

          //LAB_800efc6c
          if((charDisplay.flags_06 & 0x4) != 0) {
            charDisplay.flags_06 ^= 0x8;
          }

          //LAB_800efc84
          if(charDisplay._14[2] < 6) {
            charDisplay._14[2]++;
          }
        }
        //LAB_800efc9c
      }

      //LAB_800efcac
      //LAB_800efcdc
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        final BattleDisplayStats144 displayStats = this.displayStats_800c6c2c[charSlot];
        final BattleHudCharacterDisplay3c charDisplay = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];
        final int y = battleHudYOffsets_800fb198[this.battleHudYOffsetIndex_800c6c38];
        charDisplay.y_0a = y;
        displayStats.y_02 = y;
      }

      //LAB_800efd00
      this.tickFloatingNumbers();
      this.handleSpellAndItemMenu();
    }

    this.drawUiElements();
  }

  @Method(0x800efd34L)
  private void drawUiElements() {
    int spBarIndex = 0;

    //LAB_800efe04
    //LAB_800efe9c
    //LAB_800eff1c
    //LAB_800eff70
    //LAB_800effa0
    if(this.battle.countCombatUiFilesLoaded_800c6cf4 >= 6) {
      //LAB_800f0ad4
      // Background
      if(this.activePartyBattleHudCharacterDisplays_800c6c40[0].charIndex_00 != -1 && (this.activePartyBattleHudCharacterDisplays_800c6c40[0].flags_06 & 0x1) != 0) {
        if(this.battleUiBackground == null) {
          this.battleUiBackground = new UiBox("Battle UI Background", 16, battleHudYOffsets_800fb198[this.battleHudYOffsetIndex_800c6c38] - 26, 288, 40);
        }

        this.battleUiBackground.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
      }

      //LAB_800f0000
      //LAB_800f0074
      for(int charSlot = 0; charSlot < battleState_8006e398.getPlayerCount(); charSlot++) {
        final BattleDisplayStats144 displayStats = this.displayStats_800c6c2c[charSlot];
        final BattleHudCharacterDisplay3c charDisplay = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];

        if(charDisplay.charIndex_00 != -1 && (charDisplay.flags_06 & 0x1) != 0 && (charDisplay.flags_06 & 0x2) != 0) {
          final ScriptState<PlayerBattleEntity> state = battleState_8006e398.playerBents_e40[charSlot];
          final PlayerBattleEntity player = state.innerStruct_00;
          final int brightnessIndex0;
          final int brightnessIndex1;
          if((this.battle.currentTurnBent_800c66c8.storage_44[7] & 0x4) != 0x1 && this.battle.currentTurnBent_800c66c8 == state) {
            brightnessIndex0 = 2;
            brightnessIndex1 = 2;
          } else {
            brightnessIndex0 = 0;
            brightnessIndex1 = 1;
          }

          //LAB_800f0108
          final int count;
          if((player.status_0e & 0x2000) == 0) { // Can't become dragoon
            count = 4;
          } else {
            count = 5;
          }

          //LAB_800f0120
          //LAB_800f0128
          for(int i = 0; i < count; i++) {
            //LAB_800f0134
            for(int n = 0; n < displayStats.digits_04[i].length; n++) {
              final BattleDisplayStatsDigit10 digit = displayStats.digits_04[i][n];
              if(digit.digitValue_00 == -1) {
                break;
              }

              // Numbers
              this.uiTransforms.transfer.set(displayStats.x_00 + digit.x_02, displayStats.y_02 + digit.y_04, 124.0f);
              final RenderEngine.QueuedModel<?> digitModel = RENDERER.queueOrthoModel(this.floatingTextType1Digits[digit.digitValue_00], this.uiTransforms);

              if(charDisplay._14[2] < 6) {
                digit.colour.mul(((byte)(uiTextureElementBrightness_800c71ec[brightnessIndex0] + 0x80) / 6 * charDisplay._14[2] - 0x80 & 0xff) / 128.0f, this.colourTemp);
              } else {
                digit.colour.mul((uiTextureElementBrightness_800c71ec[brightnessIndex0] & 0xff) / 128.0f, this.colourTemp);
              }

              digitModel.colour(this.colourTemp);
            }
            //LAB_800f01e0
          }

          //LAB_800f01f0
          final NameAndPortraitDisplayMetrics0c namePortraitMetrics = hudNameAndPortraitMetrics_800fb444[player.charId_272];

          // Names
          if(this.names[charSlot] == null) {
            this.names[charSlot] = this.buildUiTextureElement(
              "Name " + charSlot,
              namePortraitMetrics.nameU_00, namePortraitMetrics.nameV_01,
              namePortraitMetrics.nameW_02, namePortraitMetrics.nameH_03,
              0x2c
            );
          }

          this.uiTransforms.transfer.set(displayStats.x_00 + 1, displayStats.y_02 - 25, 124.0f);
          final RenderEngine.QueuedModel<?> nameModel = RENDERER.queueOrthoModel(this.names[charSlot], this.uiTransforms);

          // Portraits
          if(this.portraits[charSlot] == null) {
            this.portraits[charSlot] = this.buildUiTextureElement(
              "Portrait " + charSlot,
              namePortraitMetrics.portraitU_04, namePortraitMetrics.portraitV_05,
              namePortraitMetrics.portraitW_06, namePortraitMetrics.portraitH_07,
              namePortraitMetrics.portraitClutOffset_08
            );
          }

          this.uiTransforms.transfer.set(displayStats.x_00 - 44, displayStats.y_02 - 22, 124.0f);
          final RenderEngine.QueuedModel<?> portraitModel = RENDERER.queueOrthoModel(this.portraits[charSlot], this.uiTransforms);

          if(charDisplay._14[2] < 6) {
            nameModel.monochrome(((byte)(uiTextureElementBrightness_800c71ec[brightnessIndex0] + 0x80) / 6 * charDisplay._14[2] - 0x80 & 0xff) / 128.0f);
            portraitModel.monochrome(((byte)(uiTextureElementBrightness_800c71ec[brightnessIndex1] + 0x80) / 6 * charDisplay._14[2] - 0x80 & 0xff) / 128.0f);
          } else {
            nameModel.monochrome((uiTextureElementBrightness_800c71ec[brightnessIndex0] & 0xff) / 128.0f);
            portraitModel.monochrome((uiTextureElementBrightness_800c71ec[brightnessIndex1] & 0xff) / 128.0f);
          }

          if(brightnessIndex0 != 0) {
            final int v1_0 = (6 - charDisplay._14[2]) * 8 + 100;
            final int x = displayStats.x_00 - centreScreenX_1f8003dc + namePortraitMetrics.portraitW_06 / 2 - 44;
            final int y = displayStats.y_02 - centreScreenY_1f8003de + namePortraitMetrics.portraitH_07 / 2 - 22;
            int dimVertexPositionModifier = (namePortraitMetrics.portraitW_06 + 2) * v1_0 / 100 / 2;
            final int x0 = x - dimVertexPositionModifier;
            final int x1 = x + dimVertexPositionModifier - 1;

            final short[] xs = {(short)x0, (short)x1, (short)x0, (short)x1};

            dimVertexPositionModifier = (namePortraitMetrics.portraitH_07 + 2) * v1_0 / 100 / 2;
            final int y0 = y - dimVertexPositionModifier;
            final int y1 = y + dimVertexPositionModifier - 1;

            final short[] ys = {(short)y0, (short)y0, (short)y1, (short)y1};

            //LAB_800f0438
            for(int i = 0; i < 8; i++) {
              dimVertexPositionModifier = charDisplay._14[2];

              final int r;
              final int g;
              final int b;
              final boolean translucent;
              if(dimVertexPositionModifier < 6) {
                r = dimVertexPositionModifier * 0x2a;
                g = r;
                b = r;
                translucent = true;
              } else {
                r = 0xff;
                g = 0xff;
                b = 0xff;
                translucent = false;
              }

              //LAB_800f0470
              //LAB_800f047c
              final int borderLayer = i / 4;
              final CombatPortraitBorderMetrics0c borderMetrics = combatPortraitBorderVertexCoords_800c6e9c[i % 4];

              // Draw border around currently active character's portrait
              this.drawLine(
                xs[borderMetrics.x1Index_00] + borderMetrics.x1Offset_04 + borderMetrics._08 * borderLayer,
                ys[borderMetrics.y1Index_01] + borderMetrics.y1Offset_05 + borderMetrics._09 * borderLayer,
                xs[borderMetrics.x2Index_02] + borderMetrics.x2Offset_06 + borderMetrics._0a * borderLayer,
                ys[borderMetrics.y2Index_03] + borderMetrics.y2Offset_07 + borderMetrics._0b * borderLayer,
                r,
                g,
                b,
                translucent
              );
            }
          }

          //LAB_800f05d4
          final boolean canTransform = (player.status_0e & 0x2000) != 0;

          //LAB_800f05f4
          int eraseSpHeight = 0;
          for(int i = 0; i < 3; i++) {
            if(i == 2 && !canTransform) {
              eraseSpHeight = -10;
            }

            //LAB_800f060c
            final BattleHudStatLabelMetrics0c labelMetrics = battleHudStatLabelMetrics_800c6ecc[i];

            // HP: /  MP: /  SP:
            //LAB_800f0610
            if(this.stats[charSlot][i] == null) {
              this.stats[charSlot][i] = this.buildUiTextureElement(
                "Stats " + charSlot + ' ' + i,
                labelMetrics.u_04, labelMetrics.v_06,
                labelMetrics.w_08, labelMetrics.h_0a + eraseSpHeight,
                0x2c
              );
            }

            this.uiTransforms.transfer.set(displayStats.x_00 + labelMetrics.x_00, displayStats.y_02 + labelMetrics.y_02, 124.0f);
            final RenderEngine.QueuedModel<?> statsModel = RENDERER.queueOrthoModel(this.stats[charSlot][i], this.uiTransforms);

            if(charDisplay._14[2] < 6) {
              statsModel.monochrome(((byte)(uiTextureElementBrightness_800c71ec[brightnessIndex0] + 0x80) / 6 * charDisplay._14[2] - 0x80 & 0xff) / 128.0f);
            } else {
              statsModel.monochrome((uiTextureElementBrightness_800c71ec[brightnessIndex0] & 0xff) / 128.0f);
            }
          }

          if(canTransform) {
            final int sp = player.stats.getStat(LodMod.SP_STAT.get()).getCurrent();
            final int fullLevels = sp / 100;
            final int partialSp = sp % 100;

            //SP bars
            //LAB_800f0714
            for(int i = 0; i < 2; i++) {
              int spBarW;
              if(i == 0) {
                spBarW = partialSp;
                spBarIndex = fullLevels + 1;
                //LAB_800f0728
              } else if(fullLevels == 0) {
                spBarW = 0;
              } else {
                spBarW = 100;
                spBarIndex = fullLevels;
              }

              //LAB_800f0738
              spBarW = Math.max(0, (short)spBarW * 35 / 100);

              //LAB_800f0780
              final int left = displayStats.x_00 - centreScreenX_1f8003dc + 3;
              final int top = displayStats.y_02 - centreScreenY_1f8003de + 8;
              final int right = left + spBarW;
              final int bottom = top + 3;

              final int[] spBarColours = spBarColours_800c6f04[spBarIndex];

              if(this.spBars == null) {
                this.spBars = new QuadBuilder("SPBar")
                  .monochrome(0, 229.0f / 255.0f)
                  .monochrome(1, 133.0f / 255.0f)
                  .monochrome(2, 229.0f / 255.0f)
                  .monochrome(3, 133.0f / 255.0f)
                  .size(1, 1)
                  .build();
              }

              spBarTransforms.transfer.set(GPU.getOffsetX() + left, GPU.getOffsetY() + top, 31.0f);
              spBarTransforms.scaling(right - left, bottom - top, 1.0f);

              RENDERER.queueOrthoModel(this.spBars, spBarTransforms).colour(spBarColours[0] / 255.0f, spBarColours[1] / 255.0f, spBarColours[2] / 255.0f);
            }

            //SP border
            //LAB_800f0910
            for(int i = 0; i < 4; i++) {
              final int offsetX = displayStats.x_00 - centreScreenX_1f8003dc;
              final int offsetY = displayStats.y_02 - centreScreenY_1f8003de;
              this.drawLine(spBarBorderMetrics_800fb46c[i].x1_00 + offsetX, spBarBorderMetrics_800fb46c[i].y1_01 + offsetY, spBarBorderMetrics_800fb46c[i].x2_02 + offsetX, spBarBorderMetrics_800fb46c[i].y2_03 + offsetY, 0x60, 0x60, 0x60, false);
            }

            //Full SP meter
            if((charDisplay.flags_06 & 0x8) != 0) {
              //LAB_800f09ec
              for(int i = 0; i < 4; i++) {
                final int offsetX = displayStats.x_00 - centreScreenX_1f8003dc;
                final int offsetY = displayStats.y_02 - centreScreenY_1f8003de;
                this.drawLine(spBarFlashingBorderMetrics_800fb47c[i].x1_00 + offsetX, spBarFlashingBorderMetrics_800fb47c[i].y1_01 + offsetY, spBarFlashingBorderMetrics_800fb47c[i].x2_02 + offsetX, spBarFlashingBorderMetrics_800fb47c[i].y2_03 + offsetY, 0x80, 0, 0, false);
              }
            }
          }
        }
      }

      // Use item menu
      this.drawItemMenuElements();

      //LAB_800f0b3c
      this.drawFloatingNumbers();

      // Targeting
      final BattleMenuStruct58 menu = this.battleMenu_800c6c34;
      if(menu.displayTargetArrowAndName_4c) {
        this.drawTargetArrow(menu.targetType_50, menu.combatantIndex_54);
        final int targetCombatant = menu.combatantIndex_54;
        String str;
        Element element;
        if(targetCombatant == -1) {  // Target all
          str = targeting_800fb36c[menu.targetType_50];
          element = LodMod.DIVINE_ELEMENT.get();
        } else {  // Target single
          final BattleEntity27c targetBent;

          //LAB_800f0bb0
          if(menu.targetType_50 == 1) {
            //LAB_800f0ca4
            final MonsterBattleEntity monsterBent = battleState_8006e398.aliveMonsterBents_ebc[targetCombatant].innerStruct_00;

            //LAB_800f0cf0
            int enemySlot;
            for(enemySlot = 0; enemySlot < battleState_8006e398.getMonsterCount(); enemySlot++) {
              if(this.battle.monsterBents_800c6b78[enemySlot] == menu.target_48) {
                break;
              }
            }

            //LAB_800f0d10
            str = this.getTargetEnemyName(monsterBent, this.battle.currentEnemyNames_800c69d0[enemySlot]);
            element = monsterBent.displayElement_1c;
            targetBent = monsterBent;
          } else if(menu.targetType_50 == 0) {
            targetBent = battleState_8006e398.playerBents_e40[targetCombatant].innerStruct_00;
            str = playerNames_800fb378[targetBent.charId_272];
            element = targetBent.getElement();

            if(targetBent.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0 && battleState_8006e398.playerBents_e40[menu.combatantIndex_54].innerStruct_00.isDragoon()) {
              element = LodMod.DIVINE_ELEMENT.get();
            }
          } else {
            //LAB_800f0d58
            //LAB_800f0d5c
            final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[targetCombatant];
            targetBent = state.innerStruct_00;
            if(targetBent instanceof final MonsterBattleEntity monsterBent) {
              //LAB_800f0e24
              str = this.getTargetEnemyName(monsterBent, this.battle.currentEnemyNames_800c69d0[targetCombatant]);
              element = monsterBent.displayElement_1c;
            } else {
              str = playerNames_800fb378[targetBent.charId_272];
              element = targetBent.getElement();

              if(targetBent.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0 && battleState_8006e398.playerBents_e40[menu.combatantIndex_54].innerStruct_00.isDragoon()) {
                element = LodMod.DIVINE_ELEMENT.get();
              }
            }
          }

          //LAB_800f0e60
          final int status = targetBent.status_0e;

          if((status & 0xff) != 0) {
            if((tickCount_800bb0fc & 0x10) != 0) {
              int mask = 0x80;

              //LAB_800f0e94
              int statusBit;
              for(statusBit = 0; statusBit < 8; statusBit++) {
                if((status & mask) != 0) {
                  break;
                }

                mask >>= 1;
              }

              //LAB_800f0eb4
              if(statusBit == 8) {
                statusBit = 7;
              }

              //LAB_800f0ec0
              str = ailments_800fb3a0[statusBit];
            }
          }
        }

        //LAB_800f0ed8
        //Character name
        if(this.battleUiName == null) {
          this.battleUiName = new UiBox("Battle UI Name", 44, 23, 232, 14);
        }

        this.battleUiName.render(element.colour);
        Scus94491BpeSegment_8002.renderText(str, 160 - textWidth(str) / 2, 24, TextColour.WHITE, 0);
      }
    }
    //LAB_800f0f2c
  }

  /**
   * @param numberIndex which UI element it is, controls positioning
   */
  @Method(0x800f1550L)
  public void renderNumber(final int charSlot, final int numberIndex, int value, final int colour) {
    if(this.floatingTextType1Digits[0] == null) {
      for(int i = 0; i < 10; i++) {
        this.floatingTextType1Digits[i] = this.buildUiTextureElement(
          "Floating text type 1 digit " + i,
          floatingTextType1DigitUs_800c7028[i], 32,
          8, 8,
          0x80
        );
      }
    }

    final int digitCount = MathHelper.digitCount(value);

    //LAB_800f16e4
    final BattleDisplayStats144 displayStats = this.displayStats_800c6c2c[charSlot];

    final int[] digits = new int[digitCount];

    for(int i = 0; i < displayStats.digits_04[numberIndex].length; i++) {
      displayStats.digits_04[numberIndex][i].digitValue_00 = -1;
    }

    //LAB_800f171c
    Arrays.fill(digits, -1);

    int divisor = 1;

    //LAB_800f1768
    for(int i = 0; i < digitCount - 1; i++) {
      divisor *= 10;
    }

    //LAB_800f1780
    //LAB_800f17b0
    for(int i = 0; i < digitCount; i++) {
      digits[i] = value / divisor;
      value %= divisor;
      divisor /= 10;
    }

    //LAB_800f1800
    //LAB_800f1828
    final int rightAlignOffset = 4 - digitCount;

    //LAB_800f1848
    //LAB_800f184c
    //LAB_800f18cc
    for(int i = 0; i < digitCount; i++) {
      final BattleDisplayStatsDigit10 digit = displayStats.digits_04[numberIndex][i];

      if(numberIndex == 1 || numberIndex == 3 || numberIndex == 4) {
        //LAB_800f18f0
        digit.x_02 = digitOffsetX_800c7014[numberIndex] + i * 5;
      } else {
        digit.x_02 = digitOffsetX_800c7014[numberIndex] + (i + rightAlignOffset) * 5;
      }

      //LAB_800f1920
      digit.y_04 = digitOffsetY_800c7014[numberIndex];

      if(colour == 1) {
        digit.colour.set(1.0f, 1.0f, 1.0f);
      } else if(colour == 2) {
        digit.colour.set(248 / 240.0f, 224 / 240.0f, 72 / 240.0f);
      } else if(colour == 3) {
        digit.colour.set(224 / 240.0f, 72 / 240.0f, 0.0f);
      }

      //LAB_800f1998
      //LAB_800f199c
      digit.digitValue_00 = digits[i];
    }

    //LAB_800f19e0
  }

  @Method(0x800f1a00L)
  public void setBattleHudVisibility(final boolean visible) {
    if(!visible) {
      //LAB_800f1a10
      //LAB_800f1a28
      for(int i = 0; i < 3; i++) {
        final BattleHudCharacterDisplay3c v1 = this.activePartyBattleHudCharacterDisplays_800c6c40[i];

        if(v1.charIndex_00 != -1) {
          v1._14[2] = 0;
          v1.flags_06 &= ~0x3;
        }

        //LAB_800f1a4c
      }

      return;
    }

    //LAB_800f1a64
    //LAB_800f1a70
    for(int i = 0; i < 3; i++) {
      final BattleHudCharacterDisplay3c v1 = this.activePartyBattleHudCharacterDisplays_800c6c40[i];
      if(v1.charIndex_00 != -1) {
        v1._14[2] = 0;
        v1.flags_06 |= 0x3;
      }

      //LAB_800f1a90
    }
  }

  public void addFloatingNumber(final int number, final float x, final float y) {
    for(int i = 0; i < this.floatingNumbers_800c6b5c.length; i++) {
      final FloatingNumberC4 num = this.floatingNumbers_800c6b5c[i];

      if(num.state_00 == 0) {
        this.addFloatingNumber(i, 0, 0, number, x, y, 60 / vsyncMode_8007a3b8 * 5, 0);
        break;
      }
    }
  }

  @Method(0x800f3354L)
  public void addFloatingNumber(final int numIndex, final int onHitTextType, final int onHitClutCol, final int number, final float x, final float y, int ticks, int colour) {
    if(this.miss == null) {
      for(int i = 0; i < 10; i++) {
        final QuadBuilder builder1 = new QuadBuilder("Type 1 Floating Digit " + i)
          .uv(floatingTextType1DigitUs_800c7028[i], 32)
          .size(8.0f, 8.0f);
        this.setGpuPacketClutAndTpageAndQueue(builder1, 0x80, null);
        this.type1FloatingDigits[i] = builder1.build();

        final QuadBuilder builder3 = new QuadBuilder("Type 3 Floating Digit " + i)
          .uv(floatingTextType3DigitUs_800c70e0[i], 40)
          .size(8.0f, 16.0f);
        this.setGpuPacketClutAndTpageAndQueue(builder3, 0x80, null);
        this.type3FloatingDigits[i] = builder3.build();
      }

      final QuadBuilder builderMiss = new QuadBuilder("Miss Floating Digit")
        .uv(72, 128)
        .size(36.0f, 16.0f);
      this.setGpuPacketClutAndTpageAndQueue(builderMiss, 0x80, null);
      this.miss = builderMiss.build();
    }

    final FloatingNumberC4 num = this.floatingNumbers_800c6b5c[numIndex];
    final int[] damageDigits = new int[num.digits_24.length];

    final int floatingTextType;  // 0=floating numbers, 1=MP cost, 2=miss
    final int clutCol; //TODO: confirm this, it may not be this exactly
    if(number != -1) {
      floatingTextType = onHitTextType;
      clutCol = onHitClutCol;
    } else {
      floatingTextType = 2;
      clutCol = 2;
      colour = 14;
    }

    switch(colour) {
      case 0 -> num.colour.set(1.0333333f, 0.6666667f, 0.0f); // Orange
      case 1 -> num.colour.set(1.0f, 1.0f, 1.0f); // Whiter
      case 2, 6 -> num.colour.set(1.0f, 1.0f, 1.0f); // White
      case 3, 7 -> num.colour.set(0.4f, 0.93333334f, 1.0333333f); // Cyan
      case 4, 8 -> num.colour.set(1.0333333f, 0.93333334f, 0.3f); // Yellow
      case 5, 9, 14 -> num.colour.set(0.93333334f, 0.3f, 0.0f); // Red
      case 10, 12 -> num.colour.set(0.56666666f, 0.53333336f, 1.0333333f); // Violet
      case 11, 13 -> num.colour.set(0.3f, 1.0333333f, 0.3f); // Green
    }

    //LAB_800f34d4
    num.flags_02 = 0;
    num.bentIndex_04 = -1;
    num.translucent_08 = false;
    num.shade_0c = 0x80;
    num._18 = -1;
    num.ticksRemaining_14 = -1;

    //LAB_800f3528
    for(int i = 0; i < num.digits_24.length; i++) {
      num.digits_24[i].flags_00 = 0;
      num.digits_24[i]._04 = 0;
      num.digits_24[i]._08 = 0;
      num.digits_24[i].digit_0c = -1;
    }

    num.state_00 = 1;

    if(ticks == 0) {
      num.flags_02 |= 0x1;
    }

    //LAB_800f3588
    num.flags_02 |= 0x8000;
    num._10 = clutCol;

    if(clutCol == 2 && ticks == 0) {
      ticks = 60 / vsyncMode_8007a3b8 * 2;
    }

    //LAB_800f35dc
    //LAB_800f35e4
    //LAB_800f3608
    int damage = MathHelper.clamp(number, 0, 999999999);

    //LAB_800f3614
    num.x_1c = x;
    num.y_20 = y;

    //LAB_800f3654
    for(int i = 0; i < num.digits_24.length; i++) {
      num.digits_24[i].digit_0c = -1;
      damageDigits[i] = -1;
    }

    //LAB_800f36a0
    //Sets what places to render
    int currDigitPlace = (int)Math.pow(10, num.digits_24.length - 1);
    for(int i = 0; i < num.digits_24.length; i++) {
      damageDigits[i] = damage / currDigitPlace;
      damage %= currDigitPlace;
      currDigitPlace /= 10;
    }

    //LAB_800f36dc
    //LAB_800f36ec
    int digitIdx;
    for(digitIdx = 0; digitIdx < num.digits_24.length - 1; digitIdx++) {
      if(damageDigits[digitIdx] != 0) {
        break;
      }
    }

    //LAB_800f370c
    //LAB_800f3710
    int displayPosX;
    if(floatingTextType == 1) {
      //LAB_800f3738
      displayPosX = -(num.digits_24.length - digitIdx) * 5 / 2;
    } else if(floatingTextType == 2) {
      //LAB_800f3758
      displayPosX = -18;
    } else {
      //LAB_800f372c
      displayPosX = -(num.digits_24.length - digitIdx) * 4;
    }

    //LAB_800f375c
    //LAB_800f37ac
    int digitStructIdx;
    for(digitStructIdx = 0; digitStructIdx < num.digits_24.length && digitIdx < num.digits_24.length; digitStructIdx++) {
      final FloatingNumberDigit20 digit = num.digits_24[digitStructIdx];
      digit.flags_00 = 0x8000;
      digit.x_0e = displayPosX;
      digit.y_10 = 0;

      if(clutCol == 2) {
        digit.flags_00 = 0;
        digit._04 = digitStructIdx;
        digit._08 = 0;
      }

      //LAB_800f37d8
      if(floatingTextType == 1) {
        //LAB_800f382c
        digit.obj = this.type1FloatingDigits[damageDigits[digitIdx]];
        displayPosX += 5;
      } else if(floatingTextType == 2) {
        //LAB_800f386c
        digit.obj = this.miss;
        displayPosX += 36;
      } else {
        //LAB_800f37f4
        digit.obj = this.type3FloatingDigits[damageDigits[digitIdx]];
        displayPosX += 8;
      }

      //LAB_800f3898
      digit.digit_0c = damageDigits[digitIdx];

      digitIdx++;
    }

    //LAB_800f38e8
    num.ticksRemaining_14 = digitStructIdx + 12;
    num._18 = ticks + 4; //TODO: ID duration meaning
  }

  @Method(0x800f3940L)
  public void tickFloatingNumbers() {
    //LAB_800f3978
    for(final FloatingNumberC4 num : this.floatingNumbers_800c6b5c) {
      if((num.flags_02 & 0x8000) != 0) {
        if(num.state_00 != 0) {
          if(num.bentIndex_04 != -1) {
            final ScriptState<?> state = scriptStatePtrArr_800bc1c0[num.bentIndex_04];
            final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;

            final float x;
            final float y;
            final float z;
            if(bent instanceof final MonsterBattleEntity monster) {
              x = -monster.targetArrowPos_78.x * 100.0f;
              y = -monster.targetArrowPos_78.y * 100.0f;
              z = -monster.targetArrowPos_78.z * 100.0f;
            } else {
              //LAB_800f3a3c
              x = 0;
              y = -640;
              z = 0;
            }

            //LAB_800f3a44
            final Vector2f screenCoords = bent.transformRelative(x, y, z);
            num.x_1c = this.clampX(screenCoords.x + centreScreenX_1f8003dc);
            num.y_20 = this.clampY(screenCoords.y + centreScreenY_1f8003de);
          }

          //LAB_800f3ac8
          if(num.state_00 == 1) {
            //LAB_800f3b24
            if(num._10 == 0x2) {
              num.state_00 = 2;
            } else {
              //LAB_800f3b44
              num.state_00 = 98;
            }
          } else if(num.state_00 == 2) {
            //LAB_800f3b50
            for(int n = 0; n < num.digits_24.length; n++) {
              final FloatingNumberDigit20 digit = num.digits_24[n];

              if(digit.digit_0c == -1) {
                break;
              }

              if((digit.flags_00 & 0x1) != 0) {
                if((digit.flags_00 & 0x2) != 0) {
                  if(digit._08 < 5) {
                    digit.y_10 += digit._08;
                    digit._08++;
                  }
                } else {
                  //LAB_800f3bb0
                  digit.flags_00 |= 0x8002;
                  digit._04 = digit.y_10;
                  digit._08 = -4;
                }
              } else {
                //LAB_800f3bc8
                if(digit._08 == digit._04) {
                  digit.flags_00 |= 0x1;
                }

                //LAB_800f3be0
                digit._08++;
              }
            }

            //LAB_800f3c00
            num.ticksRemaining_14--;
            if(num.ticksRemaining_14 <= 0) {
              num.state_00 = 98;
              num.ticksRemaining_14 = num._18;
            }
          } else if(num.state_00 == 97) {
            //LAB_800f3c34
            if(num.ticksRemaining_14 <= 0) {
              num.state_00 = 100;
            } else {
              //LAB_800f3c50
              num.ticksRemaining_14--;
              num.shade_0c = (num.shade_0c - (num._18 & 0xff)) & 0xff;
            }
          } else if(num.state_00 == 100) {
            //LAB_800f3d38
            num.state_00 = 0;
            num.flags_02 = 0;
            num.bentIndex_04 = -1;
            num.translucent_08 = false;
            num.shade_0c = 0x80;
            num.ticksRemaining_14 = -1;
            num._18 = -1;

            //LAB_800f3d60
            for(int n = 0; n < num.digits_24.length; n++) {
              final FloatingNumberDigit20 digit = num.digits_24[n];
              digit.flags_00 = 0;
              digit._04 = 0;
              digit._08 = 0;
              digit.digit_0c = -1;
            }
            //LAB_800f3b04
          } else if(num.state_00 < 99) {
            //LAB_800f3c88
            if((num.flags_02 & 0x1) != 0) {
              num.state_00 = 99;
            } else {
              //LAB_800f3ca4
              num.ticksRemaining_14--;

              if(num.ticksRemaining_14 <= 0) {
                if(num._10 > 0 && num._10 < 3) {
                  num.state_00 = 97;
                  num.translucent_08 = true;
                  num.shade_0c = 0x60;

                  final int ticksRemaining = 60 / vsyncMode_8007a3b8 / 2;
                  num.ticksRemaining_14 = ticksRemaining;
                  num._18 = 96 / ticksRemaining;
                } else {
                  //LAB_800f3d24
                  //LAB_800f3d2c
                  num.state_00 = 100;
                }
              }
            }
          }
        }
      }
    }
  }

  @Method(0x800f3dbcL)
  public void drawFloatingNumbers() {
    //LAB_800f3e20
    for(final FloatingNumberC4 num : this.floatingNumbers_800c6b5c) {
      if((num.flags_02 & 0x8000) != 0) {
        if(num.state_00 != 0) {
          //LAB_800f3e80
          for(int i = 0; i < num.digits_24.length; i++) {
            final FloatingNumberDigit20 digit = num.digits_24[i];

            if(digit.digit_0c == -1) {
              break;
            }

            if((digit.flags_00 & 0x8000) != 0) {
              //LAB_800f3ec0
              for(int s3 = 1; s3 < 3; s3++) {
                num.transforms.transfer.set(digit.x_0e + num.x_1c, digit.y_10 + num.y_20, 28.0f);
                RENDERER.queueOrthoModel(digit.obj, num.transforms)
                  .colour(num.colour);

                if((num.state_00 & 97) == 0) {
                  //LAB_800f4118
                  break;
                }

                //LAB_800f4110
              }
            }
          }
        }
      }
      //LAB_800f4134
    }
  }

  @Method(0x800f417cL)
  public void FUN_800f417c() {
    //LAB_800f41ac
    for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
      final BattleHudCharacterDisplay3c s1 = this.activePartyBattleHudCharacterDisplays_800c6c40[i];

      if(s1.charIndex_00 == -1 && characterStatsLoaded_800be5d0) {
        this.initCharacterDisplay(i);
      }

      //LAB_800f41dc
    }

    //LAB_800f41f4
    //LAB_800f41f8
    short x = 63;

    //LAB_800f4220
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleDisplayStats144 displayStats = this.displayStats_800c6c2c[charSlot];
      final BattleHudCharacterDisplay3c v1 = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];

      if(v1.charIndex_00 != -1) {
        v1.x_08 = x;
        displayStats.x_00 = x;
      }

      //LAB_800f4238
      x += 94;
    }
  }

  @Method(0x800f4268L)
  public void addFloatingNumberForBent(final int bentIndex, final int damage, final int s4) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[bentIndex];
    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;

    final float x;
    final float y;
    final float z;
    if(bent instanceof final MonsterBattleEntity monster) {
      // ZYX is the correct order
      x = -monster.targetArrowPos_78.z * 100.0f;
      y = -monster.targetArrowPos_78.y * 100.0f;
      z = -monster.targetArrowPos_78.x * 100.0f;
    } else {
      //LAB_800f4314
      x = 0;
      y = -640;
      z = 0;
    }

    //LAB_800f4320
    final Vector2f screenCoords = bent.transformRelative(x, y, z);

    //LAB_800f4394
    this.FUN_800f89f4(bentIndex, 0, 2, damage, this.clampX(screenCoords.x + centreScreenX_1f8003dc), this.clampY(screenCoords.y + centreScreenY_1f8003de), 60 / vsyncMode_8007a3b8 / 4, s4);
  }

  @Method(0x800f49bcL)
  public void initSpellAndItemMenu(final PlayerBattleEntity player, final int menuType) {
    this.spellAndItemMenu_800c6b60.menuState_00 = 1;
    this.spellAndItemMenu_800c6b60.x_04 = 160;
    this.spellAndItemMenu_800c6b60.y_06 = 144;
    this.spellAndItemMenu_800c6b60.player_08 = player;
    this.spellAndItemMenu_800c6b60.menuType_0a = (short)menuType;
    this.spellAndItemMenu_800c6b60._0c = 0x20;
    this.spellAndItemMenu_800c6b60._0e = 0x2b;
    this.spellAndItemMenu_800c6b60.width_10 = 0;
    this.spellAndItemMenu_800c6b60.height_12 = 0;
    this.spellAndItemMenu_800c6b60._14 = 0x1;
    this.spellAndItemMenu_800c6b60._16 = 0x1000;
    this.spellAndItemMenu_800c6b60.textX_18 = 0;
    this.spellAndItemMenu_800c6b60._1a = 0;
    this.spellAndItemMenu_800c6b60.itemOrSpellId_1c = -1;
    this.spellAndItemMenu_800c6b60.listIndex_1e = 0;
    this.spellAndItemMenu_800c6b60._20 = 0;

    //LAB_800f4a58
    if(menuType == 0) {
      //LAB_800f4a9c
      this.prepareItemList();
      this.spellAndItemMenu_800c6b60.count_22 = (short)this.combatItems_800c6988.size();
    } else if(menuType == 1) {
      //LAB_800f4abc
      //LAB_800f4ae0
      //LAB_800f4b00
      //LAB_800f4b18
      short spellIndex;
      for(spellIndex = 0; spellIndex < 8; spellIndex++) {
        if(this.battle.dragoonSpells_800c6960[this.spellAndItemMenu_800c6b60.player_08.charSlot_276].spellIndex_01[spellIndex] == -1) {
          break;
        }
      }

      //LAB_800f4b3c
      this.spellAndItemMenu_800c6b60.count_22 = spellIndex;
    } else if(menuType == 2) {
      //LAB_800f4b4c
      this.prepareAdditionList(player.charId_272);
      this.spellAndItemMenu_800c6b60.count_22 = (short)this.combatAdditions.size();
    }

    //LAB_800f4b50
    //LAB_800f4b54
    //LAB_800f4b60
    this.spellAndItemMenu_800c6b60._7c = 0;
    this.spellAndItemMenu_800c6b60._80 = 0;
    this.spellAndItemMenu_800c6b60.selectionArrowFrame_84 = 0;
    this.spellAndItemMenu_800c6b60._88 = 0;
    this.spellAndItemMenu_800c6b60._8c = 0;
    this.spellAndItemMenu_800c6b60._90 = 0;
    this.spellAndItemMenu_800c6b60._94 = 0;
    this.spellAndItemMenu_800c6b60._98 = 0;
    this.spellAndItemMenu_800c6b60._9c = 0;
    this.spellAndItemMenu_800c6b60.selectionState_a0 = 0;
  }

  @Method(0x800f4b80L)
  public void handleSpellAndItemMenu() {
    if(this.spellAndItemMenu_800c6b60.menuState_00 == 0) {
      return;
    }

    int v0;
    final int a1;
    int s0;

    //LAB_800f4bc0
    switch(this.spellAndItemMenu_800c6b60.menuState_00) {
      case 1 -> {
        this.spellAndItemMenu_800c6b60._90 = 0;
        this.spellAndItemMenu_800c6b60.selectionState_a0 = 0;
        this.spellAndItemMenu_800c6b60.height_12 = 0;
        this.spellAndItemMenu_800c6b60.width_10 = 0;

        if(this.spellAndItemMenu_800c6b60.menuType_0a == 0) {
          this.spellAndItemMenu_800c6b60.listScroll_24 = this.spellAndItemMenu_800c6b60._26;
          this.spellAndItemMenu_800c6b60.flags_02 |= 0x20;
          this.spellAndItemMenu_800c6b60.listIndex_1e = this.spellAndItemMenu_800c6b60._28;
          this.spellAndItemMenu_800c6b60._20 = this.spellAndItemMenu_800c6b60._2a;
          this.spellAndItemMenu_800c6b60._94 = this.spellAndItemMenu_800c6b60._2c;

          if(this.spellAndItemMenu_800c6b60.count_22 - 1 < this.spellAndItemMenu_800c6b60.listScroll_24 + this.spellAndItemMenu_800c6b60.listIndex_1e) {
            this.spellAndItemMenu_800c6b60.listScroll_24--;

            if(this.spellAndItemMenu_800c6b60.listScroll_24 < 0) {
              this.spellAndItemMenu_800c6b60.listScroll_24 = 0;
              this.spellAndItemMenu_800c6b60.listIndex_1e = 0;
              this.spellAndItemMenu_800c6b60._20 = this.spellAndItemMenu_800c6b60._1a;
              this.spellAndItemMenu_800c6b60._94 = 0; // This was a3.1a - a3.1a
            }
          }
        } else {
          //LAB_800f4ca0
          this.spellAndItemMenu_800c6b60.listIndex_1e = 0;
          this.spellAndItemMenu_800c6b60._20 = 0;
          this.spellAndItemMenu_800c6b60._94 = 0;
          this.spellAndItemMenu_800c6b60.listScroll_24 = this.spellAndItemMenu_800c6b60._30;
        }

        //LAB_800f4cb4
        this.spellAndItemMenu_800c6b60.itemOrSpellId_1c = (short)this.getItemOrSpellId();
        this.spellAndItemMenu_800c6b60.menuState_00 = 7;
        this.spellAndItemMenu_800c6b60.flags_02 |= 0x40;
      }

      case 2 -> {
        this.spellAndItemMenu_800c6b60.flags_02 &= 0xfcff;
        this.spellAndItemMenu_800c6b60.itemOrSpellId_1c = (short)this.getItemOrSpellId();

        if((press_800bee94 & 0x4) != 0) { // L1
          if(this.spellAndItemMenu_800c6b60.listScroll_24 != 0) {
            this.spellAndItemMenu_800c6b60._88 = 2;
            this.spellAndItemMenu_800c6b60.listScroll_24 = 0;
            this.spellAndItemMenu_800c6b60.menuState_00 = 5;
            playSound(0, 1, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4d54
        if((press_800bee94 & 0x1) != 0) { // L2
          final int oldScroll = this.spellAndItemMenu_800c6b60.listScroll_24;

          if(this.spellAndItemMenu_800c6b60.count_22 - 1 >= this.spellAndItemMenu_800c6b60.listIndex_1e + 6) {
            this.spellAndItemMenu_800c6b60.listScroll_24 = 6;
          } else {
            //LAB_800f4d8c
            this.spellAndItemMenu_800c6b60.listScroll_24 = (short)(this.spellAndItemMenu_800c6b60.count_22 - (this.spellAndItemMenu_800c6b60.listIndex_1e + 1));
          }

          //LAB_800f4d90
          this.spellAndItemMenu_800c6b60._88 = 2;
          this.spellAndItemMenu_800c6b60.menuState_00 = 5;

          if(oldScroll != this.spellAndItemMenu_800c6b60.listScroll_24) {
            playSound(0, 1, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4dc4
        if((press_800bee94 & 0x8) != 0) { // R1
          if(this.spellAndItemMenu_800c6b60.listIndex_1e == 0) {
            break;
          }

          if(this.spellAndItemMenu_800c6b60.listIndex_1e < 7) {
            this.spellAndItemMenu_800c6b60.listScroll_24 = 0;
            this.spellAndItemMenu_800c6b60.listIndex_1e = 0;
            this.spellAndItemMenu_800c6b60._20 = this.spellAndItemMenu_800c6b60._1a;
          } else {
            //LAB_800f4df4
            this.spellAndItemMenu_800c6b60.listIndex_1e -= 7;
            this.spellAndItemMenu_800c6b60._20 += 98;
          }

          //LAB_800f4e00
          this.spellAndItemMenu_800c6b60._88 = 2;
          this.spellAndItemMenu_800c6b60.menuState_00 = 5;
          this.spellAndItemMenu_800c6b60._94 = this.spellAndItemMenu_800c6b60._1a - this.spellAndItemMenu_800c6b60._20;
          playSound(0, 1, (short)0, (short)0);
          break;
        }

        //LAB_800f4e40
        if((press_800bee94 & 0x2) != 0) { // R2
          if(this.spellAndItemMenu_800c6b60.listIndex_1e + 6 >= this.spellAndItemMenu_800c6b60.count_22 - 1) {
            break;
          }

          this.spellAndItemMenu_800c6b60.listIndex_1e += 7;
          this.spellAndItemMenu_800c6b60._20 -= 98;

          if(this.spellAndItemMenu_800c6b60.listIndex_1e + 6 >= this.spellAndItemMenu_800c6b60.count_22 - 1) {
            this.spellAndItemMenu_800c6b60.listScroll_24 = 0;
          }

          //LAB_800f4e98
          this.spellAndItemMenu_800c6b60._88 = 2;
          this.spellAndItemMenu_800c6b60.menuState_00 = 5;
          this.spellAndItemMenu_800c6b60._94 = this.spellAndItemMenu_800c6b60._1a - this.spellAndItemMenu_800c6b60._20;
          playSound(0, 1, (short)0, (short)0);
          break;
        }

        //LAB_800f4ecc
        if((input_800bee90 & 0x1000) != 0) { // Up
          if(this.spellAndItemMenu_800c6b60.listScroll_24 != 0) {
            this.spellAndItemMenu_800c6b60.menuState_00 = 5;
            this.spellAndItemMenu_800c6b60.listScroll_24--;
            this.spellAndItemMenu_800c6b60._88 = 2;
          } else {
            //LAB_800f4f18
            if(this.spellAndItemMenu_800c6b60.listIndex_1e == 0) {
              break;
            }

            this.spellAndItemMenu_800c6b60.menuState_00 = 3;
            this.spellAndItemMenu_800c6b60.flags_02 |= 0x200;
            this.spellAndItemMenu_800c6b60._80 = 5;
            this.spellAndItemMenu_800c6b60._7c = this.spellAndItemMenu_800c6b60._20;
            this.spellAndItemMenu_800c6b60._20 += 5;
            this.spellAndItemMenu_800c6b60.listIndex_1e--;
          }

          playSound(0, 1, (short)0, (short)0);
          break;
        }

        //LAB_800f4f74
        if((input_800bee90 & 0x4000) != 0) { // Down
          if(this.spellAndItemMenu_800c6b60.listScroll_24 != this.spellAndItemMenu_800c6b60.count_22 - 1) {
            if(this.spellAndItemMenu_800c6b60.listIndex_1e + this.spellAndItemMenu_800c6b60.listScroll_24 + 1 < this.spellAndItemMenu_800c6b60.count_22) {
              playSound(0, 1, (short)0, (short)0);

              if(this.spellAndItemMenu_800c6b60.listScroll_24 != 6) {
                this.spellAndItemMenu_800c6b60.listScroll_24++;
                this.spellAndItemMenu_800c6b60._88 = 2;
                this.spellAndItemMenu_800c6b60.menuState_00 = 5;
              } else {
                //LAB_800f4ff8
                this.spellAndItemMenu_800c6b60._80 = -5;
                this.spellAndItemMenu_800c6b60.menuState_00 = 4;
                this.spellAndItemMenu_800c6b60._7c = this.spellAndItemMenu_800c6b60._20;
                this.spellAndItemMenu_800c6b60._20 -= 5;
                this.spellAndItemMenu_800c6b60.listIndex_1e++;
                this.spellAndItemMenu_800c6b60.flags_02 |= 0x100;
              }
            }
          }

          break;
        }

        //LAB_800f5044
        this.spellAndItemMenu_800c6b60._90 = 0;

        if((press_800bee94 & 0x20) != 0) { // X
          //LAB_800f5078
          final PlayerBattleEntity player = this.spellAndItemMenu_800c6b60.player_08;
          this.battleMenu_800c6c34._800c6980 = this.spellAndItemMenu_800c6b60.player_08.charSlot_276;

          //LAB_800f50b8
          if(this.spellAndItemMenu_800c6b60.menuType_0a == 0) {
            player.itemId_52 = this.spellAndItemMenu_800c6b60.itemOrSpellId_1c;
            player.setTempItemMagicStats();

            if((player.item_d4.target_00 & 0x4) != 0) {
              this.spellAndItemMenu_800c6b60.itemTargetType_800c6b68 = 1;
            } else {
              //LAB_800f5100
              this.spellAndItemMenu_800c6b60.itemTargetType_800c6b68 = 0;
            }

            //LAB_800f5108
            //LAB_800f5128
            this.spellAndItemMenu_800c6b60.itemTargetAll_800c69c8 = (player.item_d4.target_00 & 0x2) != 0;
          } else if(this.spellAndItemMenu_800c6b60.menuType_0a == 1) {
            //LAB_800f5134
            player.setActiveSpell(this.spellAndItemMenu_800c6b60.itemOrSpellId_1c);

            if(player.stats.getStat(LodMod.MP_STAT.get()).getCurrent() < player.spell_94.mp_06) {
              //LAB_800f5160
              //LAB_800f5168
              playSound(0, 3, (short)0, (short)0);
              break;
            }

            //LAB_800f517c
            this.clearFloatingNumber(0);
          } else {
            final ActiveStatsa0 stats = stats_800be5f8[player.charId_272];
            playSound(0, 2, (short)0, (short)0);
            player.combatant_144.mrg_04 = null;
            gameState_800babc8.charData_32c[player.charId_272].selectedAddition_19 = additionOffsets_8004f5ac[player.charId_272] + this.spellAndItemMenu_800c6b60.itemOrSpellId_1c;
            loadCharacterStats();
            player.additionSpMultiplier_11a = stats.additionSpMultiplier_9e;
            player.additionDamageMultiplier_11c = stats.additionDamageMultiplier_9f;
            loadAdditions();
            this.battle.loadAttackAnimations(player.combatant_144);
            this.spellAndItemMenu_800c6b60.menuState_00 = 8;
            this.spellAndItemMenu_800c6b60.flags_02 &= 0xfff7;
            break;
          }

          //LAB_800f5190
          playSound(0, 2, (short)0, (short)0);
          this.spellAndItemMenu_800c6b60._8c = 0;
          this.spellAndItemMenu_800c6b60.flags_02 |= 0x4;
          if(this.spellAndItemMenu_800c6b60.menuType_0a == 0) {
            this.spellAndItemMenu_800c6b60._94 = this.spellAndItemMenu_800c6b60._1a - this.spellAndItemMenu_800c6b60._20;
          }

          //LAB_800f51e8
          this.spellAndItemMenu_800c6b60.menuState_00 = 6;
          this.spellAndItemMenu_800c6b60.flags_02 &= 0xfffd;
          break;
        }

        //LAB_800f5208
        if((press_800bee94 & 0x40) != 0) { // O
          playSound(0, 3, (short)0, (short)0);
          this.spellAndItemMenu_800c6b60.menuState_00 = 8;
          this.spellAndItemMenu_800c6b60.flags_02 &= 0xfff7;
        }
      }

      case 3 -> {
        s0 = this.spellAndItemMenu_800c6b60._80;
        this.spellAndItemMenu_800c6b60._90++;
        if(this.spellAndItemMenu_800c6b60._90 >= 3) {
          s0 *= 2;
        }

        //LAB_800f5278
        a1 = this.spellAndItemMenu_800c6b60._7c + 14;
        this.spellAndItemMenu_800c6b60._20 += s0;
        if(this.spellAndItemMenu_800c6b60._20 >= a1) {
          this.spellAndItemMenu_800c6b60._20 = (short)a1;
          this.spellAndItemMenu_800c6b60.menuState_00 = 2;
        }
      }

      case 4 -> {
        s0 = this.spellAndItemMenu_800c6b60._80;
        this.spellAndItemMenu_800c6b60._90++;
        if(this.spellAndItemMenu_800c6b60._90 >= 3) {
          s0 = s0 * 2;
        }

        //LAB_800f52d4
        a1 = this.spellAndItemMenu_800c6b60._7c - 14;
        this.spellAndItemMenu_800c6b60._20 += s0;
        if(this.spellAndItemMenu_800c6b60._20 <= a1) {
          //LAB_800f5300
          this.spellAndItemMenu_800c6b60._20 = (short)a1;
          this.spellAndItemMenu_800c6b60.menuState_00 = 2;
        }
      }

      case 5 -> {
        s0 = this.spellAndItemMenu_800c6b60._88;
        this.spellAndItemMenu_800c6b60._90++;
        if(this.spellAndItemMenu_800c6b60._90 >= 3) {
          s0 = s0 / 2;
        }

        //LAB_800f5338
        if(s0 <= 1) {
          this.spellAndItemMenu_800c6b60.menuState_00 = 2;
        }
      }

      case 6 -> {
        this.spellAndItemMenu_800c6b60.selectionState_a0 = 0;
        this.spellAndItemMenu_800c6b60.itemOrSpellId_1c = (short)this.getItemOrSpellId();

        //LAB_800f53c8
        final int targetType;
        final boolean targetAll;
        if(this.spellAndItemMenu_800c6b60.menuType_0a == 0) { // Items
          targetType = this.spellAndItemMenu_800c6b60.itemTargetType_800c6b68;
          targetAll = this.spellAndItemMenu_800c6b60.itemTargetAll_800c69c8;
        } else { // Spells
          //LAB_800f53f8
          final int itemTargetType = this.spellAndItemMenu_800c6b60.player_08.spell_94.targetType_00;
          targetType = (itemTargetType & 0x40) > 0 ? 1 : 0;
          targetAll = (itemTargetType & 0x8) != 0;
        }

        //LAB_800f5410
        final int ret = this.handleTargeting(targetType, targetAll);
        if(ret == 1) { // Pressed X
          if(this.spellAndItemMenu_800c6b60.menuType_0a == 0) {
            final int itemId = this.spellAndItemMenu_800c6b60.itemOrSpellId_1c + 192;
            final Item item = REGISTRIES.items.getEntry(LodMod.itemIdMap.get(this.spellAndItemMenu_800c6b60.itemOrSpellId_1c)).get(); //TODO
            takeItemId(item);

            boolean returnItem = false;
            for(int repeatItemIndex = 0; repeatItemIndex < 9; repeatItemIndex++) {
              if(itemId == repeatItemIds_800c6e34[repeatItemIndex]) {
                returnItem = true;
                break;
              }
            }

            if(itemId == 0xfa) { // Psych Bomb X
              returnItem = true;
            }

            final RepeatItemReturnEvent repeatItemReturnEvent = EVENTS.postEvent(new RepeatItemReturnEvent(itemId, returnItem));

            if(repeatItemReturnEvent.returnItem) {
              this.battle.usedRepeatItems_800c6c3c.add(item);
            }
          }

          //LAB_800f545c
          if(this.spellAndItemMenu_800c6b60.menuType_0a == 1) {
            final VitalsStat mp = this.spellAndItemMenu_800c6b60.player_08.stats.getStat(LodMod.MP_STAT.get());
            mp.setCurrent(mp.getCurrent() - this.spellAndItemMenu_800c6b60.player_08.spell_94.mp_06);
          }

          //LAB_800f5488
          playSound(0, 2, (short)0, (short)0);
          this.spellAndItemMenu_800c6b60.selectionState_a0 = 1;
          this.spellAndItemMenu_800c6b60.menuState_00 = 9;
        } else if(ret == -1) { // Pressed O
          //LAB_800f54b4
          playSound(0, 0, (short)0, (short)0);
          this.spellAndItemMenu_800c6b60.menuState_00 = 7;
          this.spellAndItemMenu_800c6b60.flags_02 &= 0xfffb;
          this.spellAndItemMenu_800c6b60.flags_02 |= 0x20;
        }
      }

      case 7 -> {
        if(this.spellAndItemMenu_800c6b60.menuType_0a == 1) {
          s0 = 128;
        } else {
          s0 = 186;
        }

        this.spellAndItemMenu_800c6b60.menuState_00 = 2;
        playSound(0, 4, (short)0, (short)0);
        this.spellAndItemMenu_800c6b60.width_10 = s0;
        this.spellAndItemMenu_800c6b60.height_12 = 82;
        this.spellAndItemMenu_800c6b60.textX_18 = (short)(this.spellAndItemMenu_800c6b60.x_04 - s0 / 2 + 9);
        v0 = (this.spellAndItemMenu_800c6b60.y_06 - this.spellAndItemMenu_800c6b60.height_12) - 16;
        this.spellAndItemMenu_800c6b60._1a = (short)v0;
        this.spellAndItemMenu_800c6b60._20 = (short)v0;
        this.spellAndItemMenu_800c6b60.flags_02 |= 0xb;
        if((this.spellAndItemMenu_800c6b60.flags_02 & 0x20) != 0) {
          v0 = v0 - this.spellAndItemMenu_800c6b60._94;
          this.spellAndItemMenu_800c6b60._20 = (short)v0;
        }

        //LAB_800f5588
        if(this.spellAndItemMenu_800c6b60.menuType_0a == 1) {
          this.spellAndItemMenu_800c6b60.itemOrSpellId_1c = (short)this.getItemOrSpellId();
          this.spellAndItemMenu_800c6b60.player_08.setActiveSpell(this.spellAndItemMenu_800c6b60.itemOrSpellId_1c);
          this.addFloatingNumber(0, 1, 0, this.spellAndItemMenu_800c6b60.player_08.spell_94.mp_06, 280, 135, 0, 1);
        }
      }

      case 8 -> {
        this.spellAndItemMenu_800c6b60.itemTargetAll_800c69c8 = false;
        this.spellAndItemMenu_800c6b60.itemTargetType_800c6b68 = 0;
        this.spellAndItemMenu_800c6b60.selectionState_a0 = -1;
        this.spellAndItemMenu_800c6b60.menuState_00 = 9;
        this.spellAndItemMenu_800c6b60.height_12 = 0;
        this.spellAndItemMenu_800c6b60.width_10 = 0;
        this.spellAndItemMenu_800c6b60.flags_02 &= 0xfffc;
        this.clearFloatingNumber(0);
      }

      case 9 -> {
        if(this.spellAndItemMenu_800c6b60.menuType_0a == 0) {
          v0 = this.spellAndItemMenu_800c6b60._1a - this.spellAndItemMenu_800c6b60._20;
          this.spellAndItemMenu_800c6b60._26 = this.spellAndItemMenu_800c6b60.listScroll_24;
          this.spellAndItemMenu_800c6b60._28 = this.spellAndItemMenu_800c6b60.listIndex_1e;
          this.spellAndItemMenu_800c6b60._2a = this.spellAndItemMenu_800c6b60._20;
          this.spellAndItemMenu_800c6b60._94 = v0;
          this.spellAndItemMenu_800c6b60._2c = v0;
        }

        //LAB_800f568c
        this.spellAndItemMenu_800c6b60.clear();

        if(this.battleUiItemSpellList != null) {
          this.battleUiItemSpellList.delete();
          this.battleUiItemSpellList = null;
        }
      }
    }

    //LAB_800f5694
    //LAB_800f5698
    this.spellAndItemMenu_800c6b60.selectionArrowFrame_84 = tickCount_800bb0fc & 0x7;

    //LAB_800f56ac
  }

  @Method(0x800f56c4L)
  private int getItemOrSpellId() {
    if(this.spellAndItemMenu_800c6b60.menuType_0a == 0) {
      //LAB_800f56f0
      return LodMod.idItemMap.getInt(this.combatItems_800c6988.get(this.spellAndItemMenu_800c6b60.listScroll_24 + this.spellAndItemMenu_800c6b60.listIndex_1e).item.getRegistryId());
    }

    if(this.spellAndItemMenu_800c6b60.menuType_0a == 1) {
      //LAB_800f5718
      //LAB_800f5740
      //LAB_800f5778
      int spellIndex = this.battle.dragoonSpells_800c6960[this.spellAndItemMenu_800c6b60.player_08.charSlot_276].spellIndex_01[this.spellAndItemMenu_800c6b60.listScroll_24 + this.spellAndItemMenu_800c6b60.listIndex_1e];
      if(this.spellAndItemMenu_800c6b60.player_08.charId_272 == 8) { // Miranda
        if(spellIndex == 65) {
          spellIndex = 10;
        }

        //LAB_800f57d4
        if(spellIndex == 66) {
          spellIndex = 11;
        }

        //LAB_800f57e0
        if(spellIndex == 67) {
          spellIndex = 12;
        }
      }

      return spellIndex;
    }

    if(this.spellAndItemMenu_800c6b60.menuType_0a == 2) {
      return this.spellAndItemMenu_800c6b60.listScroll_24 + this.spellAndItemMenu_800c6b60.listIndex_1e;
    }

    throw new RuntimeException("Undefined a0");
  }

  /**
   * @param type <ol start="0"><li>items</li><li>spells</li></ol>
   */
  @Method(0x800f57f8L)
  private void renderList(final int type) {
    int trim;

    final SpellAndItemMenuA4 menu = this.spellAndItemMenu_800c6b60;

    int y1 = menu._20;
    final int y2 = menu._1a;
    final int sp68 = menu.y_06;

    //LAB_800f5860
    //LAB_800f58a4
    int sp7c = 0;

    //LAB_800f58e0
    for(int spellSlot = 0; spellSlot < menu.count_22; spellSlot++) {
      if(y1 >= sp68) {
        break;
      }

      TextColour textColour = TextColour.WHITE;
      final String name;
      if(type == 0) {
        //LAB_800f5918
        name = this.combatItems_800c6988.get(sp7c).item.getName();
      } else if(type == 1) {
        //LAB_800f5a4c
        int spellId = this.battle.dragoonSpells_800c6960[menu.player_08.charSlot_276].spellIndex_01[spellSlot];
        name = spellStats_800fa0b8[spellId].name;

        if(menu.player_08.charId_272 == 8) { // Miranda
          if(spellId == 65) {
            spellId = 10;
          }

          //LAB_800f5ab4
          if(spellId == 66) {
            spellId = 11;
          }

          //LAB_800f5ac0
          if(spellId == 67) {
            spellId = 12;
          }
        }

        //LAB_800f5acc
        final PlayerBattleEntity bent = this.setActiveCharacterSpell(spellId);

        // Not enough MP for spell
        if(bent.stats.getStat(LodMod.MP_STAT.get()).getCurrent() < bent.spell_94.mp_06) {
          textColour = TextColour.GREY;
        }
      } else if(type == 2) {
        name = this.combatAdditions.get(sp7c);
      } else {
        throw new RuntimeException("Undefined s3");
      }

      //LAB_800f5af0
      if(y1 >= y2) {
        //LAB_800f5b90
        if(sp68 >= y1 + 12) {
          trim = 0;
        } else {
          trim = y1 - (sp68 - 12);
        }

        //LAB_800f5bb4
        if((menu.flags_02 & 0x4) != 0) {
          trim = (short)menu._8c;
        }

        //LAB_800f5bd8
        Scus94491BpeSegment_8002.renderText(name, menu.textX_18, y1, textColour, trim);

        if(type == 0) {
          Scus94491BpeSegment_8002.renderText((char)0x11d + String.valueOf(this.combatItems_800c6988.get(sp7c).count), menu.textX_18 + 128, y1, textColour, trim);
        }
      } else if(y2 < y1 + 12) {
        if((menu.flags_02 & 0x4) != 0) {
          trim = menu._8c;
        } else {
          trim = y1 - y2;
        }

        //LAB_800f5b40
        Scus94491BpeSegment_8002.renderText(name, menu.textX_18, y2, textColour, trim);

        if(type == 0) {
          Scus94491BpeSegment_8002.renderText((char)0x11d + String.valueOf(this.combatItems_800c6988.get(sp7c).count), menu.textX_18 + 128, y2, textColour, trim);
        }
      }

      //LAB_800f5c38
      y1 += 14;
      sp7c++;
    }

    //LAB_800f5c64
  }

  /** Draws most elements associated with item and dragoon magic menus.
   * This includes:
   *   - Item and Dragoon magic backgrounds, scroll arrows, and text
   *   - Item and Dragoon magic description background and text
   *   - Dragoon magic MP cost background and normal text (excluding the number value)
   */
  @Method(0x800f5c94L)
  public void drawItemMenuElements() {
    final SpellAndItemMenuA4 menu = this.spellAndItemMenu_800c6b60;
    menu.init();
    menu.transforms.identity();

    if(menu.menuState_00 != 0 && (menu.flags_02 & 0x1) != 0) {
      if((menu.flags_02 & 0x2) != 0) {
        //LAB_800f5ee8
        //Item menu
        final int w = menu.width_10 + 6;
        final int h = menu.height_12 + 17;

        if(this.battleUiItemSpellList == null) {
          this.battleUiItemSpellList = new UiBox("Battle UI Item/Spell List", menu.x_04 - w / 2, menu.y_06 - h, w, h);
        }

        this.battleUiItemSpellList.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);

        this.renderList(menu.menuType_0a);

        if((menu.flags_02 & 0x8) != 0) {
          //LAB_800f5d78
          //LAB_800f5d90
          menu.transforms.transfer.set(menu.textX_18 - 16, menu._1a + menu.listScroll_24 * 14 + 2, 124.0f);
          RENDERER.queueOrthoModel(menu.arrowObj[menu.selectionArrowFrame_84], menu.transforms);

          final int s0;
          if(menu.menuType_0a != 0) {
            s0 = 0;
          } else {
            s0 = 26;
          }

          //LAB_800f5e00
          final int s1;
          if((menu.flags_02 & 0x100) != 0) {
            s1 = 2;
          } else {
            s1 = 0;
          }

          //LAB_800f5e18
          final int t0;
          if((menu.flags_02 & 0x200) != 0) {
            t0 = -2;
          } else {
            t0 = 0;
          }

          //LAB_800f5e24
          if(menu.listIndex_1e > 0) {
            menu.transforms.transfer.set(menu.x_04 + s0 + 56, menu.y_06 + t0 - 100, 124.0f);
            RENDERER.queueOrthoModel(menu.upArrow, menu.transforms);
          }

          //LAB_800f5e7c
          if(menu.listIndex_1e + 6 < menu.count_22 - 1) {
            menu.transforms.transfer.set(menu.x_04 + s0 + 56, menu.y_06 + s1 - 7, 124.0f);
            RENDERER.queueOrthoModel(menu.downArrow, menu.transforms);
          }
        }
      }

      //LAB_800f5f50
      if((menu.flags_02 & 0x40) != 0) {
        final int textType;
        if(menu.menuType_0a == 0) { // Item
          //LAB_800f5f8c
          textType = 4;
        } else if(menu.menuType_0a == 1) { // Spell
          //LAB_800f5f94
          textType = 5;
          if((menu.flags_02 & 0x2) != 0) {
            if(this.battleUiSpellList == null) {
              this.battleUiSpellList = new UiBox("Battle UI Spell List", 236, 130, 64, 14);
            }

            this.battleUiSpellList.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);

            final BattleEntity27c bent = this.setActiveCharacterSpell(menu.itemOrSpellId_1c);
            this.addFloatingNumber(0, 1, 0, bent.spell_94.mp_06, 280, 135, 0, 1);

            menu.transforms.transfer.set(236, 130, 124.0f);
            RENDERER.queueOrthoModel(menu.mpObj, menu.transforms);
          }
        } else if(menu.menuType_0a == 2) { // Addition
          textType = 6;
        } else {
          throw new RuntimeException("Undefined s1");
        }

        //LAB_800f604c
        //LAB_800f6050
        //Selected item description
        if(this.battleUiItemDescription == null) {
          this.battleUiItemDescription = new UiBox("Battle UI Item Description", 44, 156, 232, 14);
        }

        this.battleUiItemDescription.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
        this.renderText(textType, menu.itemOrSpellId_1c, 160, 163);
      }
    }

    //LAB_800f6088
  }

  @Method(0x800f6134L)
  public void initializeMenuIcons(final ScriptState<? extends BattleEntity27c> bentState, final int displayedIconsBitset, final int disabledIconsBitset) {
    this.battleMenu_800c6c34.initIconObjs();

    this.battleMenu_800c6c34.state_00 = 1;
    this.battleMenu_800c6c34.highlightState_02 = 2;
    this.battleMenu_800c6c34.x_06 = 160;
    this.battleMenu_800c6c34.y_08 = 172;
    this.battleMenu_800c6c34.selectedIcon_22 = 0;
    this.battleMenu_800c6c34.currentIconStateTick_24 = 0;
    this.battleMenu_800c6c34.iconStateIndex_26 = 0;
    this.battleMenu_800c6c34.highlightX0_28 = 0;
    this.battleMenu_800c6c34.highlightY_2a = 0;
    this.battleMenu_800c6c34.colour_2c = 128;

    //LAB_800f61d8
    for(int i = 0; i < 9; i++) {
      this.battleMenu_800c6c34.iconFlags_10[i] = -1;
    }

    //LAB_800f61f8
    this.battleMenu_800c6c34.countHighlightMovementStep_30 = 0;
    this.battleMenu_800c6c34.highlightMovementDistance_34 = 0;
    this.battleMenu_800c6c34.currentHighlightMovementStep_38 = 0;
    this.battleMenu_800c6c34.highlightX1_3c = 0;
    this.battleMenu_800c6c34.renderSelectedIconText_40 = false;
    this.battleMenu_800c6c34.cameraPositionSwitchTicksRemaining_44 = 0;
    this.battleMenu_800c6c34.target_48 = 0;
    this.battleMenu_800c6c34.displayTargetArrowAndName_4c = false;
    this.battleMenu_800c6c34.targetType_50 = 0;
    this.battleMenu_800c6c34.combatantIndex_54 = 0;

    //LAB_800f6224
    //LAB_800f6234
    int bentIndex;
    for(bentIndex = 0; bentIndex < battleState_8006e398.getPlayerCount(); bentIndex++) {
      if(battleState_8006e398.playerBents_e40[bentIndex] == bentState) {
        break;
      }
    }

    //LAB_800f6254
    this.battleMenu_800c6c34.iconCount_0e = 0;
    this.battleMenu_800c6c34.player_04 = battleState_8006e398.playerBents_e40[bentIndex].innerStruct_00;

    //LAB_800f62a4
    for(int i = 0, used = 0; i < 8; i++) {
      if((displayedIconsBitset & 0x1 << i) != 0) {
        this.battleMenu_800c6c34.iconFlags_10[used++] = iconFlags_800c7194[i];
        this.battleMenu_800c6c34.iconCount_0e++;
      }
      //LAB_800f62d0
    }

    this.battleMenu_800c6c34.xShiftOffset_0a = (short)((this.battleMenu_800c6c34.iconCount_0e * 19 - 3) / 2);
    this.setDisabledIcons(disabledIconsBitset);
  }

  /** Handles the various combat menu actions and then renders the menu:
   * <ol>
   *   <li>0 -> Set up camera positions</li>
   *   <li>1 -> Check for and handle input</li>
   *   <li>2 -> Cycle selector to adjacent icons</li>
   *   <li>3 -> Wrap selector to other end of menu</li>
   *   <li>4 -> Count down camera movement ticks</li>
   * </ol>
   */
  @Method(0x800f6330L)
  public int tickAndRender() {
    if(this.battleMenu_800c6c34.state_00 == 0) {
      return 0;
    }

    int selectedAction = 0;

    switch(this.battleMenu_800c6c34.state_00 - 1) {
      case 0 -> {  // Set up camera position list at battle start or camera reset (like dragoon or after trying to run)
        this.battleMenu_800c6c34.state_00 = 2;
        this.battleMenu_800c6c34.highlightX0_28 = (short)(this.battleMenu_800c6c34.x_06 - this.battleMenu_800c6c34.xShiftOffset_0a + this.battleMenu_800c6c34.selectedIcon_22 * 19 - 4);
        this.battleMenu_800c6c34.highlightY_2a = (short)(this.battleMenu_800c6c34.y_08 - 22);

        //LAB_800f63e8
        this.battleMenu_800c6c34.countHighlightMovementStep_30 = 0;
        this.battleMenu_800c6c34.highlightMovementDistance_34 = 0;
        this.battleMenu_800c6c34.currentHighlightMovementStep_38 = 0;
        this.battleMenu_800c6c34.highlightX1_3c = 0;
        this.battleMenu_800c6c34.renderSelectedIconText_40 = false;
        this.battleMenu_800c6c34.cameraPositionSwitchTicksRemaining_44 = 0;
        this.battleMenu_800c6c34.target_48 = 0;
        this.battleMenu_800c6c34.displayTargetArrowAndName_4c = false;
        this.battleMenu_800c6c34.targetType_50 = 0;
        this.battleMenu_800c6c34.combatantIndex_54 = 0;

        this.battleMenu_800c6c34._800c697c = 0;
        this.currentCameraPositionIndicesIndicesIndex_800c6ba1 = 0;
        this.countCameraPositionIndicesIndices_800c6ba0 = 0;

        //LAB_800f6424
        final int[] previousIndicesList = new int[4];
        for(int i = 0; i < 4; i++) {
          previousIndicesList[i] = 0xff;
          this.cameraPositionIndicesIndices_800c6c30[i] = 0;
        }

        //LAB_800f6458
        int cameraPositionIndicesIndex;
        boolean addCameraPositionIndex;
        int cameraPositionIndex;
        for(cameraPositionIndicesIndex = 0; cameraPositionIndicesIndex < 4; cameraPositionIndicesIndex++) {
          addCameraPositionIndex = true;
          cameraPositionIndex = this.battle.currentStageData_800c6718.cameraPosIndices_18[cameraPositionIndicesIndex];

          //LAB_800f646c
          for(int i = 0; i < 4; i++) { // don't add duplicate indices
            if(previousIndicesList[i] == cameraPositionIndex) {
              addCameraPositionIndex = false;
              break;
            }
            //LAB_800f6480
          }

          if(addCameraPositionIndex) {
            previousIndicesList[this.countCameraPositionIndicesIndices_800c6ba0] = this.battle.currentStageData_800c6718.cameraPosIndices_18[cameraPositionIndicesIndex];
            this.cameraPositionIndicesIndices_800c6c30[this.countCameraPositionIndicesIndices_800c6ba0] = cameraPositionIndicesIndex;

            if(this.currentCameraPositionIndicesIndex_800c66b0 == cameraPositionIndicesIndex) {
              this.currentCameraPositionIndicesIndicesIndex_800c6ba1 = this.countCameraPositionIndicesIndices_800c6ba0;
            }

            //LAB_800f64dc
            this.countCameraPositionIndicesIndices_800c6ba0++;
          }
          //LAB_800f64ec
        }
      }

      case 1 -> {  // Checking for input
        final int countCameraPositionIndicesIndices = this.countCameraPositionIndicesIndices_800c6ba0;
        this.battleMenu_800c6c34.renderSelectedIconText_40 = false;
        this.battleMenu_800c6c34.cameraPositionSwitchTicksRemaining_44 = 0;

        // Input for changing camera angles
        if(countCameraPositionIndicesIndices >= 2 && (input_800bee90 & 0x2) != 0) {
          this.currentCameraPositionIndicesIndicesIndex_800c6ba1++;
          if(this.currentCameraPositionIndicesIndicesIndex_800c6ba1 >= countCameraPositionIndicesIndices) {
            this.currentCameraPositionIndicesIndicesIndex_800c6ba1 = 0;
          }

          //LAB_800f6560
          this.battle.cameraScriptMainTableJumpIndex_800c6748 = 33;
          this.battleMenu_800c6c34.state_00 = 5;
          this.currentCameraPositionIndicesIndex_800c66b0 = this.cameraPositionIndicesIndices_800c6c30[this.currentCameraPositionIndicesIndicesIndex_800c6ba1];
          this.battleMenu_800c6c34.cameraPositionSwitchTicksRemaining_44 = 60 / vsyncMode_8007a3b8 + 2;
          this.toggleHighlight(false);
          break;
        }

        // Input for cycling right on menu bar
        //LAB_800f65b8
        if((input_800bee90 & 0x2000) != 0) {
          playSound(0, 1, (short)0, (short)0);

          if(this.battleMenu_800c6c34.selectedIcon_22 < this.battleMenu_800c6c34.iconCount_0e - 1) {
            //LAB_800f6640
            this.battleMenu_800c6c34.selectedIcon_22++;
            this.battleMenu_800c6c34.state_00 = 3;

            //LAB_800f664c
            this.battleMenu_800c6c34.countHighlightMovementStep_30 = 3;
            this.battleMenu_800c6c34.highlightMovementDistance_34 = 19;
            this.battleMenu_800c6c34.currentHighlightMovementStep_38 = 0;
            this.battleMenu_800c6c34.iconStateIndex_26 = 0;
            break;
          }

          this.battleMenu_800c6c34.state_00 = 4;
          this.battleMenu_800c6c34.highlightState_02 |= 1;
          this.battleMenu_800c6c34.selectedIcon_22 = 0;
          this.battleMenu_800c6c34.iconStateIndex_26 = 0;
          this.battleMenu_800c6c34.countHighlightMovementStep_30 = 3;
          this.battleMenu_800c6c34.highlightMovementDistance_34 = 19;
          this.battleMenu_800c6c34.currentHighlightMovementStep_38 = 0;
          this.battleMenu_800c6c34.highlightX1_3c = this.battleMenu_800c6c34.x_06 - this.battleMenu_800c6c34.xShiftOffset_0a - 23;
          break;
        }

        // Input for cycling left on menu bar
        //LAB_800f6664
        if((input_800bee90 & 0x8000) != 0) {
          playSound(0, 1, (short)0, (short)0);

          if(this.battleMenu_800c6c34.selectedIcon_22 != 0) {
            //LAB_800f66f0
            this.battleMenu_800c6c34.selectedIcon_22--;
            this.battleMenu_800c6c34.state_00 = 3;

            //LAB_800f66fc
            this.battleMenu_800c6c34.countHighlightMovementStep_30 = 3;
            this.battleMenu_800c6c34.highlightMovementDistance_34 = -19;

            //LAB_800f6710
            this.battleMenu_800c6c34.currentHighlightMovementStep_38 = 0;
            this.battleMenu_800c6c34.iconStateIndex_26 = 0;
            break;
          }

          this.battleMenu_800c6c34.state_00 = 4;
          this.battleMenu_800c6c34.highlightState_02 |= 1;
          this.battleMenu_800c6c34.selectedIcon_22 = (short)(this.battleMenu_800c6c34.iconCount_0e - 1);
          this.battleMenu_800c6c34.highlightX1_3c = this.battleMenu_800c6c34.x_06 - this.battleMenu_800c6c34.xShiftOffset_0a + this.battleMenu_800c6c34.iconCount_0e * 19 - 4;
          this.battleMenu_800c6c34.countHighlightMovementStep_30 = 3;
          this.battleMenu_800c6c34.highlightMovementDistance_34 = -19;
          this.battleMenu_800c6c34.currentHighlightMovementStep_38 = 0;
          this.battleMenu_800c6c34.iconStateIndex_26 = 0;
          break;
        }

        if(Input.pressedThisFrame(InputAction.BUTTON_NORTH)) {
          if(additionCounts_8004f5c0[this.battle.currentTurnBent_800c66c8.innerStruct_00.charId_272] != 0) {
            playSound(0, 2, (short)0, (short)0);
            this.initSpellAndItemMenu((PlayerBattleEntity)this.battle.currentTurnBent_800c66c8.innerStruct_00, 2);
          } else {
            playSound(0, 40, (short)0, (short)0);
          }
        }

        // Input for pressing X on menu bar
        //LAB_800f671c
        if((press_800bee94 & 0x20) != 0) {
          int selectedIconFlag = this.battleMenu_800c6c34.iconFlags_10[this.battleMenu_800c6c34.selectedIcon_22];
          if((selectedIconFlag & 0x80) != 0) {
            playSound(0, 3, (short)0, (short)0);
          } else {
            selectedIconFlag = selectedIconFlag & 0xf;
            if(selectedIconFlag == 0x5) {
              this.prepareItemList();

              if(this.combatItems_800c6988.isEmpty()) {
                playSound(0, 3, (short)0, (short)0);
              } else {
                playSound(0, 2, (short)0, (short)0);
                selectedAction = this.battleMenu_800c6c34.iconFlags_10[this.battleMenu_800c6c34.selectedIcon_22] & 0xf;
              }
              //LAB_800f6790
            } else if(selectedIconFlag == 0x3L) {
              //LAB_800f67b8
              //LAB_800f67d8
              //LAB_800f67f4
              int spellIndex;
              for(spellIndex = 0; spellIndex < 8; spellIndex++) {
                if(this.battle.dragoonSpells_800c6960[this.battleMenu_800c6c34.player_04.charSlot_276].spellIndex_01[spellIndex] != -1) {
                  break;
                }
              }

              //LAB_800f681c
              if(spellIndex == 8) {
                playSound(0, 3, (short)0, (short)0);
              } else {
                playSound(0, 2, (short)0, (short)0);
                selectedAction = this.battleMenu_800c6c34.iconFlags_10[this.battleMenu_800c6c34.selectedIcon_22] & 0xf;
              }
            } else {
              //LAB_800f6858
              //LAB_800f6860
              playSound(0, 2, (short)0, (short)0);
              selectedAction = this.battleMenu_800c6c34.iconFlags_10[this.battleMenu_800c6c34.selectedIcon_22] & 0xf;
            }
          }
          //LAB_800f6898
          // Input for pressing circle on menu bar
        } else if((press_800bee94 & 0x40) != 0) {
          //LAB_800f68a4
          //LAB_800f68bc
          playSound(0, 3, (short)0, (short)0);
        }

        //LAB_800f68c4
        //LAB_800f68c8
        this.battleMenu_800c6c34.renderSelectedIconText_40 = true;
      }

      case 2 -> {  // Cycle to adjacent menu bar icon
        this.battleMenu_800c6c34.currentHighlightMovementStep_38++;
        this.battleMenu_800c6c34.highlightX0_28 += (short)(this.battleMenu_800c6c34.highlightMovementDistance_34 / this.battleMenu_800c6c34.countHighlightMovementStep_30);

        if(this.battleMenu_800c6c34.currentHighlightMovementStep_38 >= this.battleMenu_800c6c34.countHighlightMovementStep_30) {
          this.battleMenu_800c6c34.state_00 = 2;
          this.battleMenu_800c6c34.countHighlightMovementStep_30 = 0;
          this.battleMenu_800c6c34.highlightMovementDistance_34 = 0;
          this.battleMenu_800c6c34.currentHighlightMovementStep_38 = 0;
          this.battleMenu_800c6c34.highlightX0_28 = (short)(this.battleMenu_800c6c34.x_06 - this.battleMenu_800c6c34.xShiftOffset_0a + this.battleMenu_800c6c34.selectedIcon_22 * 19 - 4);
          this.battleMenu_800c6c34.highlightY_2a = (short)(this.battleMenu_800c6c34.y_08 - 22);
        }
      }

      case 3 -> {  // Wrap menu bar icon
        this.battleMenu_800c6c34.currentHighlightMovementStep_38++;
        this.battleMenu_800c6c34.highlightX0_28 += (short)(this.battleMenu_800c6c34.highlightMovementDistance_34 / this.battleMenu_800c6c34.countHighlightMovementStep_30);
        this.battleMenu_800c6c34.highlightX1_3c += this.battleMenu_800c6c34.highlightMovementDistance_34 / this.battleMenu_800c6c34.countHighlightMovementStep_30;
        this.battleMenu_800c6c34.colour_2c += (short)(0x80 / this.battleMenu_800c6c34.countHighlightMovementStep_30);

        if(this.battleMenu_800c6c34.currentHighlightMovementStep_38 >= this.battleMenu_800c6c34.countHighlightMovementStep_30) {
          this.battleMenu_800c6c34.state_00 = 2;
          this.battleMenu_800c6c34.colour_2c = 0x80;
          this.battleMenu_800c6c34.currentHighlightMovementStep_38 = 0;
          this.battleMenu_800c6c34.highlightMovementDistance_34 = 0;
          this.battleMenu_800c6c34.countHighlightMovementStep_30 = 0;
          this.battleMenu_800c6c34.highlightX0_28 = (short)(this.battleMenu_800c6c34.x_06 - this.battleMenu_800c6c34.xShiftOffset_0a + this.battleMenu_800c6c34.selectedIcon_22 * 19 - 4);
          this.battleMenu_800c6c34.highlightY_2a = (short)(this.battleMenu_800c6c34.y_08 - 22);
          this.battleMenu_800c6c34.highlightState_02 &= 0xfffe;
        }
      }

      case 4 -> {  // Seems to be related to switching camera views
        this.battleMenu_800c6c34.cameraPositionSwitchTicksRemaining_44--;
        if(this.battleMenu_800c6c34.cameraPositionSwitchTicksRemaining_44 == 1) {
          this.toggleHighlight(true);
          this.battleMenu_800c6c34.state_00 = 2;
        }
      }
    }

    //LAB_800f6a88
    //LAB_800f6a8c
    this.battleMenu_800c6c34.currentIconStateTick_24++;
    if(this.battleMenu_800c6c34.currentIconStateTick_24 >= 4) {
      this.battleMenu_800c6c34.currentIconStateTick_24 = 0;
      this.battleMenu_800c6c34.iconStateIndex_26++;
      if(this.battleMenu_800c6c34.iconStateIndex_26 >= 4) {
        this.battleMenu_800c6c34.iconStateIndex_26 = 0;
      }
    }

    //LAB_800f6ae0
    this.renderActionMenu();

    //LAB_800f6aec
    return selectedAction;
  }

  @Method(0x800f6b04L)
  public void renderActionMenu() {
    if(this.battleMenu_800c6c34.state_00 != 0 && (this.battleMenu_800c6c34.highlightState_02 & 0x2) != 0) {
      //LAB_800f704c
      final int variableW = this.battleMenu_800c6c34.iconCount_0e * 19 + 1;
      int x = this.battleMenu_800c6c34.x_06 - variableW / 2;
      int y = this.battleMenu_800c6c34.y_08 - 10;
      this.battleMenu_800c6c34.transforms.scaling(variableW, 1.0f, 1.0f);
      this.battleMenu_800c6c34.transforms.transfer.set(x, y, 124.0f);
      RENDERER.queueOrthoModel(this.battleMenu_800c6c34.actionMenuBackground[0], this.battleMenu_800c6c34.transforms);

      final int[][] battleMenuBaseCoords = new int[4][2];

      battleMenuBaseCoords[0][0] = x;
      battleMenuBaseCoords[2][0] = x;
      x = x + variableW;
      battleMenuBaseCoords[1][0] = x;
      battleMenuBaseCoords[3][0] = x;
      battleMenuBaseCoords[0][1] = y;
      battleMenuBaseCoords[1][1] = y;
      y = this.battleMenu_800c6c34.y_08 - 8;
      battleMenuBaseCoords[2][1] = y;
      battleMenuBaseCoords[3][1] = y;

      //LAB_800f710c
      for(int i = 0; i < 8; i++) {
        final BattleMenuBackgroundDisplayMetrics0c displayMetrics = battleMenuBackgroundDisplayMetrics_800fb614[i];
        x = battleMenuBaseCoords[displayMetrics.vertexBaseOffsetIndex_00][0] + displayMetrics.vertexXMod_02;
        y = battleMenuBaseCoords[displayMetrics.vertexBaseOffsetIndex_00][1] + displayMetrics.vertexYMod_04;

        final int w;
        if(displayMetrics.w_06 != 0) {
          w = displayMetrics.w_06;
        } else {
          w = variableW;
        }

        //LAB_800f7158
        final int h;
        if(displayMetrics.h_08 == 0) {
          h = 2;
        } else {
          h = displayMetrics.h_08;
        }

        //LAB_800f716c
        this.battleMenu_800c6c34.transforms.scaling(w, h, 1.0f);
        this.battleMenu_800c6c34.transforms.transfer.set(x, y, 124.0f);
        RENDERER.queueOrthoModel(this.battleMenu_800c6c34.actionMenuBackground[i + 1], this.battleMenu_800c6c34.transforms);
      }

      this.battleMenu_800c6c34.transforms.identity();

      //LAB_800f6fc8
      // Draw red glow underneath selected menu item
      this.battleMenu_800c6c34.transforms.transfer.set(this.battleMenu_800c6c34.highlightX0_28, this.battleMenu_800c6c34.highlightY_2a, 123.9f);
      RENDERER.queueOrthoModel(this.battleMenu_800c6c34.highlight, this.battleMenu_800c6c34.transforms)
        .monochrome(this.battleMenu_800c6c34.colour_2c / 255.0f);

      if((this.battleMenu_800c6c34.highlightState_02 & 0x1) != 0) {
        this.battleMenu_800c6c34.transforms.transfer.set(this.battleMenu_800c6c34.highlightX1_3c, this.battleMenu_800c6c34.highlightY_2a, 123.9f);
        RENDERER.queueOrthoModel(this.battleMenu_800c6c34.highlight, this.battleMenu_800c6c34.transforms)
          .monochrome(Math.max(0, (0x80 - this.battleMenu_800c6c34.colour_2c) / 255.0f));
      }

      //LAB_800f6c48
      for(int iconIndex = 0; iconIndex < this.battleMenu_800c6c34.iconCount_0e; iconIndex++) {
        final int iconId = (this.battleMenu_800c6c34.iconFlags_10[iconIndex] & 0xf) - 1;
        final int iconState;
        if(this.battleMenu_800c6c34.selectedIcon_22 == iconIndex) {
          iconState = battleMenuIconStates_800c71e4[this.battleMenu_800c6c34.iconStateIndex_26];
        } else {
          //LAB_800f6c88
          iconState = 0;
        }

        //LAB_800f6c90
        final int menuElementBaseX = this.battleMenu_800c6c34.x_06 - this.battleMenu_800c6c34.xShiftOffset_0a + iconIndex * 19;
        final int menuElementBaseY = this.battleMenu_800c6c34.y_08 - battleMenuIconHeights_800fb6bc[iconId][iconState];

        if(this.battleMenu_800c6c34.selectedIcon_22 == iconIndex && this.battleMenu_800c6c34.renderSelectedIconText_40) {
          // Selected combat menu icon text
          this.battleMenu_800c6c34.transforms.transfer.set(menuElementBaseX, this.battleMenu_800c6c34.y_08, 123.9f);
          RENDERER.queueOrthoModel(this.battleMenu_800c6c34.actionIconTextObj[iconId], this.battleMenu_800c6c34.transforms);
        }

        // Combat menu icons
        //LAB_800f6d70
        this.battleMenu_800c6c34.transforms.transfer.set(menuElementBaseX, menuElementBaseY, 123.8f);

        if((this.battleMenu_800c6c34.iconFlags_10[iconIndex] & 0xf) != 0x2) {
          RENDERER.queueOrthoModel(this.battleMenu_800c6c34.actionIconObj[iconId][iconState], this.battleMenu_800c6c34.transforms);
        } else if(this.battleMenu_800c6c34.player_04.charId_272 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
          RENDERER.queueOrthoModel(this.battleMenu_800c6c34.dragoonIconObj[this.battleMenu_800c6c34.player_04.charId_272][iconState], this.battleMenu_800c6c34.transforms);
        } else {
          RENDERER.queueOrthoModel(this.battleMenu_800c6c34.dragoonIconObj[9][iconState], this.battleMenu_800c6c34.transforms);

          if(iconState != 0) {
            // Divine dragoon spirit overlay
            //LAB_800f6de0
            RENDERER.queueOrthoModel(this.battleMenu_800c6c34.divineSpiritOverlay[iconState - 1], this.battleMenu_800c6c34.transforms);
          }
        }

        if((this.battleMenu_800c6c34.iconFlags_10[iconIndex] & 0x80) != 0) {
          this.battleMenu_800c6c34.transforms.transfer.set(menuElementBaseX, this.battleMenu_800c6c34.y_08 - 16, 123.7f);
          RENDERER.queueOrthoModel(this.battleMenu_800c6c34.actionDisabledObj, this.battleMenu_800c6c34.transforms);
        }

        //LAB_800f6fa4
      }
    }
    //LAB_800f71e0
  }

  /** Background of battle menu icons */
  @Method(0x800f74f4L)
  public Obj buildBattleMenuBackground(final String name, final BattleMenuBackgroundUvMetrics04 menuBackgroundMetrics, final int x, final int y, final int w, final int h, final int baseClutOffset, @Nullable final Translucency transMode, final int uvShiftType) {
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(0.5f);

    this.setGpuPacketParams(builder, x, y, 0, 0, w, h, false);

    // Modified 1 and 3 from retail to properly align bottom row of pixels
    if(uvShiftType == 0) {
      //LAB_800f7628
      builder
        .uv(menuBackgroundMetrics.u_00, menuBackgroundMetrics.v_01)
        .uvSize(menuBackgroundMetrics.w_02, menuBackgroundMetrics.h_03);
    } else if(uvShiftType == 1) {
      //LAB_800f7654
      builder
        .uv(menuBackgroundMetrics.u_00, menuBackgroundMetrics.v_01 + menuBackgroundMetrics.h_03)
        .uvSize(menuBackgroundMetrics.w_02, -menuBackgroundMetrics.h_03);
      //LAB_800f7610
    } else if(uvShiftType == 2) {
      //LAB_800f7680
      builder
        .uv(menuBackgroundMetrics.u_00 + menuBackgroundMetrics.w_02 - 1, menuBackgroundMetrics.v_01)
        .uvSize(-menuBackgroundMetrics.w_02, menuBackgroundMetrics.h_03);
    } else if(uvShiftType == 3) {
      //LAB_800f76d4
      builder
        .uv(menuBackgroundMetrics.u_00 + menuBackgroundMetrics.w_02 - 1, menuBackgroundMetrics.v_01 + menuBackgroundMetrics.h_03)
        .uvSize(-menuBackgroundMetrics.w_02, -menuBackgroundMetrics.h_03);
    }

    //LAB_800f7724
    //LAB_800f772c
    this.setGpuPacketClutAndTpageAndQueue(builder, baseClutOffset, transMode);
    return builder.build();
  }

  /**
   * @param targetType 0: chars, 1: monsters, 2: all
   */
  @Method(0x800f7768L)
  public int handleTargeting(final int targetType, final boolean targetAll) {
    final int count;
    short t3 = 1;

    if(targetType == 1) {
      this.battleMenu_800c6c34.displayTargetArrowAndName_4c = true;
      //LAB_800f77d4
      count = battleState_8006e398.getAliveMonsterCount();

      //LAB_800f77e8
      this.battleMenu_800c6c34._800c697c = this.battleMenu_800c6c34._800c697e;
    } else {
      this.battleMenu_800c6c34.displayTargetArrowAndName_4c = true;
      if(targetType == 0) {
        this.battleMenu_800c6c34._800c697c = this.battleMenu_800c6c34._800c6980;
        count = battleState_8006e398.getPlayerCount();
      } else {
        //LAB_800f77f0
        count = battleState_8006e398.getAliveBentCount();
      }
    }

    //LAB_800f77f4
    if((press_800bee94 & 0x3000) != 0) {
      this.battleMenu_800c6c34._800c697c++;
      if(this.battleMenu_800c6c34._800c697c >= count) {
        this.battleMenu_800c6c34._800c697c = 0;
      }
    }

    //LAB_800f7830
    if((press_800bee94 & 0xc000) != 0) {
      this.battleMenu_800c6c34._800c697c--;
      if(this.battleMenu_800c6c34._800c697c < 0) {
        this.battleMenu_800c6c34._800c697c = count - 1;
      }
      t3 = -1;
    }

    //LAB_800f786c
    //LAB_800f7880
    if(this.battleMenu_800c6c34._800c697c < 0 || this.battleMenu_800c6c34._800c697c >= count) {
      //LAB_800f78a0
      this.battleMenu_800c6c34._800c697c = 0;
      t3 = 1;
    }

    //LAB_800f78ac
    //LAB_800f78d4
    int v1;
    ScriptState<BattleEntity27c> target = null;
    for(v1 = 0; v1 < count; v1++) {
      target = this.battle.targetBents_800c71f0[targetType][this.battleMenu_800c6c34._800c697c];

      if(target != null && (target.storage_44[7] & 0x4000) == 0) {
        break;
      }

      this.battleMenu_800c6c34._800c697c += t3;

      if(this.battleMenu_800c6c34._800c697c >= count) {
        this.battleMenu_800c6c34._800c697c = 0;
      }

      //LAB_800f792c
      if(this.battleMenu_800c6c34._800c697c < 0) {
        this.battleMenu_800c6c34._800c697c = count - 1;
      }

      //LAB_800f7948
    }

    //LAB_800f7960
    if(v1 == count) {
      target = this.battle.targetBents_800c71f0[targetType][this.battleMenu_800c6c34._800c697c];
      this.battleMenu_800c6c34._800c697c = 0;
    }

    //LAB_800f7998
    this.battleMenu_800c6c34.targetType_50 = targetType;
    if(!targetAll) {
      this.battleMenu_800c6c34.combatantIndex_54 = this.battleMenu_800c6c34._800c697c;
    } else {
      //LAB_800f79b4
      this.battleMenu_800c6c34.combatantIndex_54 = -1;
    }

    //LAB_800f79bc
    this.battleMenu_800c6c34.target_48 = target.index;

    if(targetType == 1) {
      //LAB_800f79fc
      this.battleMenu_800c6c34._800c697e = this.battleMenu_800c6c34._800c697c;
    } else if(targetType == 0) {
      this.battleMenu_800c6c34._800c6980 = this.battleMenu_800c6c34._800c697c;
    }

    //LAB_800f7a0c
    //LAB_800f7a10
    int ret = 0;
    if((press_800bee94 & 0x20) != 0) { // Cross
      ret = 1;
      this.battleMenu_800c6c34._800c697c = 0;
      this.battleMenu_800c6c34.displayTargetArrowAndName_4c = false;
    }

    //LAB_800f7a38
    if((press_800bee94 & 0x40) != 0) { // Circle
      ret = -1;
      this.battleMenu_800c6c34._800c697c = 0;
      this.battleMenu_800c6c34.target_48 = -1;
      this.battleMenu_800c6c34.displayTargetArrowAndName_4c = false;
    }

    //LAB_800f7a68
    return ret;
  }

  @Method(0x800f83c8L)
  public void prepareItemList() {
    //LAB_800f83dc
    this.combatItems_800c6988.clear();

    //LAB_800f8420
    for(int itemSlot1 = 0; itemSlot1 < gameState_800babc8.items_2e9.size(); itemSlot1++) {
      final Item item = gameState_800babc8.items_2e9.get(itemSlot1);

      boolean found = false;

      //LAB_800f843c
      for(final CombatItem02 combatItem : this.combatItems_800c6988) {
        if(combatItem.item == item) {
          found = true;
          combatItem.count++;
          break;
        }
      }

      if(!found) {
        this.combatItems_800c6988.add(new CombatItem02(item));
      }
    }
  }

  public void prepareAdditionList(final int charId) {
    //LAB_800f83dc
    this.combatAdditions.clear();

    //LAB_800f8420
    for(int additionSlot = 0; additionSlot < additionCounts_8004f5c0[charId]; additionSlot++) {
      final int additionOffset = additionOffsets_8004f5ac[charId];

      final int level = additionData_80052884[additionOffset + additionSlot].level_00;

      if(level == -1 && (gameState_800babc8.charData_32c[charId].partyFlags_04 & 0x40) != 0) {
        final String additionName = additionNames_800fa8d4[additionOffset + additionSlot];
        this.combatAdditions.add(additionName);
      } else if(level > 0 && level <= gameState_800babc8.charData_32c[charId].level_12) {
        final String additionName = additionNames_800fa8d4[additionOffset + additionSlot];
        this.combatAdditions.add(additionName);

        if(gameState_800babc8.charData_32c[charId].additionLevels_1a[additionSlot] == 0) {
          gameState_800babc8.charData_32c[charId].additionLevels_1a[additionSlot] = 1;
        }
      }
    }
  }

  @Method(0x800f8568L)
  private String getTargetEnemyName(final BattleEntity27c target, final String targetName) {
    // Seems to be special-case handling to replace Tentacle, since the Melbu fight has more enemies than the engine can handle
    if(target.charId_272 == 0x185) {
      final int stageProgression = battleState_8006e398.battlePhase_eec;
      if(stageProgression == 0 || stageProgression == 4 || stageProgression == 6) {
        return this.battle.melbuMonsterNames_800c6ba8[melbuStageToMonsterNameIndices_800c6f30[battleState_8006e398.battlePhase_eec]];
      }
    }

    return targetName;
  }

  @Method(0x800f89f4L)
  public boolean FUN_800f89f4(final int bentIndex, final int a1, final int a2, final int rawDamage, final float x, final float y, final int a6, final int a7) {
    //LAB_800f8a30
    for(int i = 0; i < this.floatingNumbers_800c6b5c.length; i++) {
      final FloatingNumberC4 num = this.floatingNumbers_800c6b5c[i];

      if(num.state_00 == 0) {
        this.addFloatingNumber(i, a1, a2, rawDamage, x, y, a6, a7);
        num.bentIndex_04 = bentIndex;
        return true;
      }

      //LAB_800f8a74
    }

    //LAB_800f8a84
    return false;
  }

  @Method(0x800f8aa4L)
  public void renderDamage(final int bentIndex, final int damage) {
    this.addFloatingNumberForBent(bentIndex, damage, 8);
  }

  @Method(0x800f8cd8L)
  public Obj buildBattleMenuElement(final String name, final int x, final int y, final int u, final int v, final int w, final int h, final int clut, @Nullable final Translucency transMode) {
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(0.5f);

    this.setGpuPacketParams(builder, x, y, u, v, w, h, true);
    this.setGpuPacketClutAndTpageAndQueue(builder, clut, transMode);

    return builder.build();
  }

  /**
   * @param textType <ol start="0">
   *                   <li>Player names</li>
   *                   <li>Player names</li>
   *                   <li>Combat item names</li>
   *                   <li>Dragoon spells</li>
   *                   <li>Item descriptions</li>
   *                   <li>Spell descriptions</li>
   *                 </ol>
   */
  @Method(0x800f8ac4L)
  private void renderText(final int textType, final int textIndex, final int x, final int y) {
    final String str;
    if(textType == 4) {
      str = itemStats_8004f2ac[textIndex].combatDescription;
    } else if(textType == 5) {
      str = spellStats_800fa0b8[textIndex].combatDescription;
    } else if(textType == 6) {
      final int additionOffset = additionOffsets_8004f5ac[this.spellAndItemMenu_800c6b60.player_08.charId_272];
      str = additionNames_800fa8d4[additionOffset + textIndex];
    } else {
      throw new IllegalArgumentException("Only supports textType 4/5");
    }

    final BattleDescriptionEvent event = EVENTS.postEvent(new BattleDescriptionEvent(textType, textIndex, str));
    Scus94491BpeSegment_8002.renderText(event.string, x - textWidth(event.string) / 2, y - 6, TextColour.WHITE, 0);
  }

  @Method(0x800f8b74L)
  public void setDisabledIcons(final int disabledIconBitset) {
    //LAB_800f8bd8
    for(int i = 0; i < 8; i++) {
      if((disabledIconBitset & 0x1 << i) != 0) {
        //LAB_800f8bf4
        for(int icon = 0; icon < 8; icon++) {
          if((this.battleMenu_800c6c34.iconFlags_10[icon] & 0xf) == iconFlags_800c7194[i]) {
            this.battleMenu_800c6c34.iconFlags_10[icon] |= 0x80;
            break;
          }
        }
      }
    }
  }

  @Method(0x800f8c38L)
  public void toggleHighlight(final boolean render) {
    if(this.battleMenu_800c6c34.state_00 != 0) {
      //LAB_800f8c78
      if(!render || this.battleMenu_800c6c34.cameraPositionSwitchTicksRemaining_44 != 0) {
        //LAB_800f8c64
        this.battleMenu_800c6c34.highlightState_02 &= 0xfffd;
      } else {
        this.battleMenu_800c6c34.highlightState_02 |= 0x2;
      }
    }
    //LAB_800f8c98
  }

  @Method(0x800f8facL)
  public void setGpuPacketParams(final QuadBuilder cmd, final int x, final int y, final int u, final int v, final int w, final int h, final boolean textured) {
    cmd
      .pos(x, y, 0.0f)
      .size(w, h);

    if(textured) {
      cmd
        .uv(u, v);
    }

    //LAB_800f901c
  }

  @Method(0x800f9024L)
  public void setGpuPacketClutAndTpageAndQueue(final QuadBuilder cmd, int clut, @Nullable final Translucency transparencyMode) {
    final int clutIndex;
    if(clut >= 0x80) {
      clutIndex = 1;
      clut -= 0x80;
    } else {
      //LAB_800f9080
      clutIndex = 0;
    }

    //LAB_800f9088
    //LAB_800f9098
    //LAB_800f90a8
    final int clutX = battleUiElementClutVramXy_800c7114[clutIndex].x + clut & 0x3f0;
    final int clutY = battleUiElementClutVramXy_800c7114[clutIndex].y + clut % 16;

    cmd
      .bpp(Bpp.BITS_4)
      .clut(clutX, clutY)
      .vramPos(704, 256);

    if(transparencyMode != null) {
      cmd.translucency(transparencyMode);
    }
  }

  @Method(0x800f8dfcL)
  private Obj buildUiTextureElement(final String name, final int u, final int v, final int w, final int h, final int clut) {
    final QuadBuilder builder = new QuadBuilder(name)
      .size(w, h)
      .uv(u, v);

    this.setGpuPacketClutAndTpageAndQueue(builder, clut, null);

    return builder.build();
  }

  @Method(0x800f9e50L)
  private PlayerBattleEntity setActiveCharacterSpell(final int spellId) {
    final PlayerBattleEntity player = this.spellAndItemMenu_800c6b60.player_08;
    player.setActiveSpell(spellId);
    return player;
  }

  @Method(0x800f9ee8L)
  private void drawLine(final int x1, final int y1, final int x2, final int y2, final int r, final int g, final int b, final boolean translucent) {
    lineTransforms.transfer.set(GPU.getOffsetX() + x1, GPU.getOffsetY() + y1, 31.0f);
    lineTransforms.scaling(x2 - x1 + 1, y2 - y1 + 1, 1.0f);

    if(translucent) {
      RENDERER.queueOrthoModel(RENDERER.plainQuads.get(Translucency.B_PLUS_F), lineTransforms)
        .colour(r / 255.0f, g / 255.0f, b / 255.0f);
    } else {
      RENDERER.queueOrthoModel(RENDERER.opaqueQuad, lineTransforms)
        .colour(r / 255.0f, g / 255.0f, b / 255.0f);
    }
  }

  @Method(0x800fa068L)
  private float clampX(final float x) {
    return MathHelper.clamp(x, 20.0f, 300.0f);
  }

  @Method(0x800fa090L)
  private float clampY(final float y) {
    return MathHelper.clamp(y, 20.0f, 220.0f);
  }

  public void delete() {
    this.deleteUiElements();
    this.deleteFloatingTextDigits();
    this.battleMenu_800c6c34.delete();
    this.spellAndItemMenu_800c6b60.delete();

    if(this.battleUiItemSpellList != null) {
      this.battleUiItemSpellList.delete();
      this.battleUiItemSpellList = null;
    }

    if(this.battleUiSpellList != null) {
      this.battleUiSpellList.delete();
      this.battleUiSpellList = null;
    }

    if(this.battleUiItemDescription != null) {
      this.battleUiItemDescription.delete();
      this.battleUiItemDescription = null;
    }

    if(this.spBars != null) {
      this.spBars.delete();
      this.spBars = null;
    }
  }

  private void deleteUiElements() {
    if(this.battleUiBackground != null) {
      this.battleUiBackground.delete();
      this.battleUiBackground = null;
    }

    if(this.battleUiName != null) {
      this.battleUiName.delete();
      this.battleUiName = null;
    }

    for(int i = 0; i < this.names.length; i++) {
      if(this.names[i] != null) {
        this.names[i].delete();
        this.names[i] = null;
      }

      if(this.portraits[i] != null) {
        this.portraits[i].delete();
        this.portraits[i] = null;
      }

      for(int j = 0; j < this.stats[i].length; j++) {
        if(this.stats[i][j] != null) {
          this.stats[i][j].delete();
          this.stats[i][j] = null;
        }
      }
    }
  }

  private void deleteFloatingTextDigits() {
    for(int i = 0; i < this.floatingTextType1Digits.length; i++) {
      if(this.floatingTextType1Digits[i] != null) {
        this.floatingTextType1Digits[i].delete();
        this.floatingTextType1Digits[i] = null;
      }
    }

    for(int i = 0; i < 10; i++) {
      if(this.type1FloatingDigits[i] != null) {
        this.type1FloatingDigits[i].delete();
        this.type1FloatingDigits[i] = null;
      }

      if(this.type3FloatingDigits[i] != null) {
        this.type3FloatingDigits[i].delete();
        this.type3FloatingDigits[i] = null;
      }

      if(this.miss != null) {
        this.miss.delete();
        this.miss = null;
      }
    }
  }
}
