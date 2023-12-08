package legend.game.combat.ui;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.RenderEngine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.characters.Element;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.CombatPortraitBorderMetrics0c;
import legend.game.combat.environment.NameAndPortraitDisplayMetrics0c;
import legend.game.combat.environment.SpBarBorderMetrics04;
import legend.game.combat.types.BattleHudStatLabelMetrics0c;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.StatDisplayEvent;
import legend.game.scripting.ScriptState;
import legend.game.types.LodString;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.characterStatsLoaded_800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.countCombatUiFilesLoaded_800c6cf4;
import static legend.game.combat.Bttl_800c.currentEnemyNames_800c69d0;
import static legend.game.combat.Bttl_800c.currentTurnBent_800c66c8;
import static legend.game.combat.Bttl_800c.hud;
import static legend.game.combat.Bttl_800c.monsterBents_800c6b78;
import static legend.game.combat.Bttl_800c.monsterCount_800c6768;
import static legend.game.combat.Bttl_800c.spellAndItemMenu_800c6b60;
import static legend.game.combat.Bttl_800c.uiTextureElementBrightness_800c71ec;
import static legend.game.combat.Bttl_800e.drawTargetArrow;
import static legend.game.combat.Bttl_800e.perspectiveTransformXyz;
import static legend.game.combat.Bttl_800f.buildUiTextureElement;
import static legend.game.combat.Bttl_800f.clampX;
import static legend.game.combat.Bttl_800f.clampY;
import static legend.game.combat.Bttl_800f.drawLine;
import static legend.game.combat.Bttl_800f.getTargetEnemyName;
import static legend.game.combat.Bttl_800f.setGpuPacketClutAndTpageAndQueue;

public class BattleHud {
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

  private static final int[] battleHudYOffsets_800fb198 = {46, 208, -128, 0};

  /** Targeting ("All allies", "All players", "All") */
  private static final LodString[] targeting_800fb36c = { new LodString("All allies"), new LodString("All enemies"), new LodString("All") };
  public static final LodString[] playerNames_800fb378 = {
    new LodString("Dart"), new LodString("Lavitz"), new LodString("Shana"), new LodString("Rose"), new LodString("Haschel"),
    new LodString("Albert"), new LodString("Meru"), new LodString("Kongol"), new LodString("Miranda"), new LodString("DivinDGDart"),
  };
  /** Poisoned, Dispirited, Weapon blocked, Stunned, Fearful, Confused, Bewitched, Petrified */
  private static final LodString[] ailments_800fb3a0 = {
    new LodString("Poisoned"), new LodString("Dispirited"), new LodString("Weapon blocked"), new LodString("Stunned"), new LodString("Fearful"),
    new LodString("Confused"), new LodString("Bewitched"), new LodString("Petrified"),
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

  /** Only ever set to 1. 0 will set it to the top of the screen. */
  private int battleHudYOffsetIndex_800c6c38;

  private final FloatingNumberC4[] floatingNumbers_800c6b5c = new FloatingNumberC4[12];
  private final BattleDisplayStats144[] displayStats_800c6c2c = new BattleDisplayStats144[3];
  private final BattleHudCharacterDisplay3c[] activePartyBattleHudCharacterDisplays_800c6c40 = new BattleHudCharacterDisplay3c[3];

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

  public BattleHud() {
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
  }

  @Method(0x800ef8d8L)
  public void initCharacterDisplay(final int charSlot) {
    final BattleHudCharacterDisplay3c charDisplay = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];
    charDisplay.charIndex_00 = charSlot;
    charDisplay.charId_02 = battleState_8006e398.charBents_e40[charSlot].innerStruct_00.charId_272;
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
    if(countCombatUiFilesLoaded_800c6cf4.get() == 6) {
      final int charCount = charCount_800c677c.get();

      //LAB_800efa34
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        if(this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot].charIndex_00 == -1 && characterStatsLoaded_800be5d0) {
          hud.initCharacterDisplay(charSlot);
        }
        //LAB_800efa64
      }

      //LAB_800efa78
      //LAB_800efa94
      //LAB_800efaac
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        final BattleHudCharacterDisplay3c charDisplay = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];

        if(charDisplay.charIndex_00 != -1 && (charDisplay.flags_06 & 0x1) != 0 && (charDisplay.flags_06 & 0x2) != 0) {
          final PlayerBattleEntity player = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;

          final VitalsStat playerHp = player.stats.getStat(CoreMod.HP_STAT.get());
          final VitalsStat playerMp = player.stats.getStat(CoreMod.MP_STAT.get());
          final VitalsStat playerSp = player.stats.getStat(CoreMod.SP_STAT.get());

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
            charDisplay.flags_06 &= 0xfff3;
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
      spellAndItemMenu_800c6b60.handleSpellAndItemMenu();
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
    if(countCombatUiFilesLoaded_800c6cf4.get() >= 6) {
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
      for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        final BattleDisplayStats144 displayStats = this.displayStats_800c6c2c[charSlot];
        final BattleHudCharacterDisplay3c charDisplay = this.activePartyBattleHudCharacterDisplays_800c6c40[charSlot];

        if(charDisplay.charIndex_00 != -1 && (charDisplay.flags_06 & 0x1) != 0 && (charDisplay.flags_06 & 0x2) != 0) {
          final ScriptState<PlayerBattleEntity> state = battleState_8006e398.charBents_e40[charSlot];
          final PlayerBattleEntity player = state.innerStruct_00;
          final int brightnessIndex0;
          final int brightnessIndex1;
          if((currentTurnBent_800c66c8.storage_44[7] & 0x4) != 0x1 && currentTurnBent_800c66c8 == state) {
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
              final RenderEngine.QueuedModel digitModel = RENDERER.queueOrthoOverlayModel(this.floatingTextType1Digits[digit.digitValue_00], this.uiTransforms);

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
            this.names[charSlot] = buildUiTextureElement(
              "Name " + charSlot,
              namePortraitMetrics.nameU_00, namePortraitMetrics.nameV_01,
              namePortraitMetrics.nameW_02, namePortraitMetrics.nameH_03,
              0x2c
            );
          }

          this.uiTransforms.transfer.set(displayStats.x_00 + 1, displayStats.y_02 - 25, 124.0f);
          final RenderEngine.QueuedModel nameModel = RENDERER.queueOrthoOverlayModel(this.names[charSlot], this.uiTransforms);

          // Portraits
          if(this.portraits[charSlot] == null) {
            this.portraits[charSlot] = buildUiTextureElement(
              "Portrait " + charSlot,
              namePortraitMetrics.portraitU_04, namePortraitMetrics.portraitV_05,
              namePortraitMetrics.portraitW_06, namePortraitMetrics.portraitH_07,
              namePortraitMetrics.portraitClutOffset_08
            );
          }

          this.uiTransforms.transfer.set(displayStats.x_00 - 44, displayStats.y_02 - 22, 124.0f);
          final RenderEngine.QueuedModel portraitModel = RENDERER.queueOrthoOverlayModel(this.portraits[charSlot], this.uiTransforms);

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
              drawLine(
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
              this.stats[charSlot][i] = buildUiTextureElement(
                "Stats " + charSlot + ' ' + i,
                labelMetrics.u_04, labelMetrics.v_06,
                labelMetrics.w_08, labelMetrics.h_0a + eraseSpHeight,
                0x2c
              );
            }

            this.uiTransforms.transfer.set(displayStats.x_00 + labelMetrics.x_00, displayStats.y_02 + labelMetrics.y_02, 124.0f);
            final RenderEngine.QueuedModel statsModel = RENDERER.queueOrthoOverlayModel(this.stats[charSlot][i], this.uiTransforms);

            if(charDisplay._14[2] < 6) {
              statsModel.monochrome(((byte)(uiTextureElementBrightness_800c71ec[brightnessIndex0] + 0x80) / 6 * charDisplay._14[2] - 0x80 & 0xff) / 128.0f);
            } else {
              statsModel.monochrome((uiTextureElementBrightness_800c71ec[brightnessIndex0] & 0xff) / 128.0f);
            }
          }

          if(canTransform) {
            final int sp = player.stats.getStat(CoreMod.SP_STAT.get()).getCurrent();
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

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .pos(0, left, top)
                .pos(1, right, top)
                .pos(2, left, bottom)
                .pos(3, right, bottom);

              final int[] spBarColours = spBarColours_800c6f04[spBarIndex];

              cmd
                .rgb(0, spBarColours[0], spBarColours[1], spBarColours[2])
                .rgb(1, spBarColours[0], spBarColours[1], spBarColours[2]);

              cmd
                .rgb(2, spBarColours[3], spBarColours[4], spBarColours[5])
                .rgb(3, spBarColours[3], spBarColours[4], spBarColours[5]);

              GPU.queueCommand(31, cmd);
            }

            //SP border
            //LAB_800f0910
            for(int i = 0; i < 4; i++) {
              final int offsetX = displayStats.x_00 - centreScreenX_1f8003dc;
              final int offsetY = displayStats.y_02 - centreScreenY_1f8003de;
              drawLine(spBarBorderMetrics_800fb46c[i].x1_00 + offsetX, spBarBorderMetrics_800fb46c[i].y1_01 + offsetY, spBarBorderMetrics_800fb46c[i].x2_02 + offsetX, spBarBorderMetrics_800fb46c[i].y2_03 + offsetY, 0x60, 0x60, 0x60, false);
            }

            //Full SP meter
            if((charDisplay.flags_06 & 0x8) != 0) {
              //LAB_800f09ec
              for(int i = 0; i < 4; i++) {
                final int offsetX = displayStats.x_00 - centreScreenX_1f8003dc;
                final int offsetY = displayStats.y_02 - centreScreenY_1f8003de;
                drawLine(spBarFlashingBorderMetrics_800fb47c[i].x1_00 + offsetX, spBarFlashingBorderMetrics_800fb47c[i].y1_01 + offsetY, spBarFlashingBorderMetrics_800fb47c[i].x2_02 + offsetX, spBarFlashingBorderMetrics_800fb47c[i].y2_03 + offsetY, 0x80, 0, 0, false);
              }
            }
          }
        }
      }

      //LAB_800f0b3c
      this.drawFloatingNumbers();

      // Use item menu
      battleMenu_800c6c34.drawItemMenuElements();

      // Targeting
      final BattleMenuStruct58 menu = battleMenu_800c6c34;
      if(menu.displayTargetArrowAndName_4c) {
        drawTargetArrow(menu.targetType_50, menu.combatantIndex_54);
        final int targetCombatant = menu.combatantIndex_54;
        LodString str;
        Element element;
        if(targetCombatant == -1) {  // Target all
          str = targeting_800fb36c[menu.targetType_50];
          element = CoreMod.DIVINE_ELEMENT.get();
        } else {  // Target single
          final BattleEntity27c targetBent;

          //LAB_800f0bb0
          if(menu.targetType_50 == 1) {
            //LAB_800f0ca4
            final MonsterBattleEntity monsterBent = battleState_8006e398.aliveMonsterBents_ebc[targetCombatant].innerStruct_00;

            //LAB_800f0cf0
            int enemySlot;
            for(enemySlot = 0; enemySlot < monsterCount_800c6768.get(); enemySlot++) {
              if(monsterBents_800c6b78.get(enemySlot).get() == menu.target_48) {
                break;
              }
            }

            //LAB_800f0d10
            str = getTargetEnemyName(monsterBent, currentEnemyNames_800c69d0[enemySlot]);
            element = monsterBent.displayElement_1c;
            targetBent = monsterBent;
          } else if(menu.targetType_50 == 0) {
            targetBent = battleState_8006e398.charBents_e40[targetCombatant].innerStruct_00;
            str = playerNames_800fb378[targetBent.charId_272];
            element = targetBent.getElement();

            if(targetBent.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0 && battleState_8006e398.charBents_e40[menu.combatantIndex_54].innerStruct_00.isDragoon()) {
              element = CoreMod.DIVINE_ELEMENT.get();
            }
          } else {
            //LAB_800f0d58
            //LAB_800f0d5c
            final ScriptState<? extends BattleEntity27c> state = battleState_8006e398.allBents_e0c[targetCombatant];
            targetBent = state.innerStruct_00;
            if(targetBent instanceof final MonsterBattleEntity monsterBent) {
              //LAB_800f0e24
              str = getTargetEnemyName(monsterBent, currentEnemyNames_800c69d0[targetCombatant]);
              element = monsterBent.displayElement_1c;
            } else {
              str = playerNames_800fb378[targetBent.charId_272];
              element = targetBent.getElement();

              if(targetBent.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0 && battleState_8006e398.charBents_e40[menu.combatantIndex_54].innerStruct_00.isDragoon()) {
                element = CoreMod.DIVINE_ELEMENT.get();
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
        renderText(str, 160 - textWidth(str) / 2, 24, TextColour.WHITE, 0);
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
        this.floatingTextType1Digits[i] = buildUiTextureElement(
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
  public void FUN_800f1a00(final boolean a0) {
    if(!a0) {
      //LAB_800f1a10
      //LAB_800f1a28
      for(int i = 0; i < 3; i++) {
        final BattleHudCharacterDisplay3c v1 = this.activePartyBattleHudCharacterDisplays_800c6c40[i];

        if(v1.charIndex_00 != -1) {
          v1._14[2] = 0;
          v1.flags_06 &= 0xffff_fffe;
          v1.flags_06 &= 0xffff_fffd;
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
        hud.addFloatingNumber(i, 0, 0, number, x, y, 60 / vsyncMode_8007a3b8 * 5, 0);
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
        setGpuPacketClutAndTpageAndQueue(builder1, 0x80, null);
        this.type1FloatingDigits[i] = builder1.build();

        final QuadBuilder builder3 = new QuadBuilder("Type 3 Floating Digit " + i)
          .uv(floatingTextType3DigitUs_800c70e0[i], 40)
          .size(8.0f, 16.0f);
        setGpuPacketClutAndTpageAndQueue(builder3, 0x80, null);
        this.type3FloatingDigits[i] = builder3.build();
      }

      final QuadBuilder builderMiss = new QuadBuilder("Miss Floating Digit")
        .uv(72, 128)
        .size(36.0f, 16.0f);
      setGpuPacketClutAndTpageAndQueue(builderMiss, 0x80, null);
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
            final Vector2f screenCoords = perspectiveTransformXyz(bent.model_148, x, y, z);
            num.x_1c = clampX(screenCoords.x + centreScreenX_1f8003dc);
            num.y_20 = clampY(screenCoords.y + centreScreenY_1f8003de);
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
              num.transforms.transfer.set(digit.x_0e + num.x_1c, digit.y_10 + num.y_20, 28.0f);
              RENDERER.queueOrthoOverlayModel(digit.obj, num.transforms)
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
      //LAB_800f4134
    }
  }

  @Method(0x800f417cL)
  public void FUN_800f417c() {
    //LAB_800f41ac
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      final BattleHudCharacterDisplay3c s1 = this.activePartyBattleHudCharacterDisplays_800c6c40[i];

      if(s1.charIndex_00 == -1 && characterStatsLoaded_800be5d0) {
        hud.initCharacterDisplay(i);
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
    final Vector2f screenCoords = perspectiveTransformXyz(bent.model_148, x, y, z);

    //LAB_800f4394
    this.FUN_800f89f4(bentIndex, 0, 2, damage, clampX(screenCoords.x + centreScreenX_1f8003dc), clampY(screenCoords.y + centreScreenY_1f8003de), 60 / vsyncMode_8007a3b8 / 4, s4);
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

  public void deleteUiElements() {
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

  public void deleteFloatingTextDigits() {
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
