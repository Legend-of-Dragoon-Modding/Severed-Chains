package legend.game.combat;

import legend.core.Config;
import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.RECT;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdWithId;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.opengl.Obj;
import legend.game.characters.Element;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.BattleStruct24_2;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.effects.AttachmentHost;
import legend.game.combat.effects.BillboardSpriteEffect0c;
import legend.game.combat.effects.DeffTmdRenderer14;
import legend.game.combat.effects.Effect;
import legend.game.combat.effects.EffectAttachment;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerData6cInner;
import legend.game.combat.effects.GenericSpriteEffect24;
import legend.game.combat.effects.ModelEffect13c;
import legend.game.combat.effects.RedEyeDragoonTransformationFlameArmorEffect20;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.effects.TextureAnimationAttachment1c;
import legend.game.combat.environment.BattleLightStruct64;
import legend.game.combat.environment.BattleStage;
import legend.game.combat.environment.BattleStageDarkening1800;
import legend.game.combat.environment.BattleStruct14;
import legend.game.combat.environment.BttlLightStruct84;
import legend.game.combat.environment.BttlLightStruct84Sub38;
import legend.game.combat.environment.CombatPortraitBorderMetrics0c;
import legend.game.combat.environment.NameAndPortraitDisplayMetrics0c;
import legend.game.combat.environment.StageAmbiance4c;
import legend.game.combat.types.BattleHudStatLabelMetrics0c;
import legend.game.combat.types.BattleObject;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.MonsterStats1c;
import legend.game.combat.ui.BattleDisplayStats144;
import legend.game.combat.ui.BattleDisplayStatsDigit10;
import legend.game.combat.ui.BattleHudCharacterDisplay3c;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.combat.ui.FloatingNumberC4;
import legend.game.combat.ui.FloatingNumberC4Sub20;
import legend.game.combat.ui.SpellAndItemMenuA4;
import legend.game.combat.ui.UiBox;
import legend.game.inventory.Item;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.MonsterStatsEvent;
import legend.game.modding.events.battle.StatDisplayEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CContainer;
import legend.game.types.CContainerSubfile2;
import legend.game.types.CharacterData2c;
import legend.game.types.LodString;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms0c;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.loadDeffSounds;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.prepareObjTable2;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stage_800bda0c;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Bttl_800c.FUN_800ca418;
import static legend.game.combat.Bttl_800c._800c6930;
import static legend.game.combat.Bttl_800c._800c6938;
import static legend.game.combat.Bttl_800c._800c697e;
import static legend.game.combat.Bttl_800c._800c6980;
import static legend.game.combat.Bttl_800c.activePartyBattleHudCharacterDisplays_800c6c40;
import static legend.game.combat.Bttl_800c.ailments_800fb3a0;
import static legend.game.combat.Bttl_800c.aliveBentCount_800c669c;
import static legend.game.combat.Bttl_800c.aliveMonsterCount_800c6758;
import static legend.game.combat.Bttl_800c.battleHudStatLabelMetrics_800c6ecc;
import static legend.game.combat.Bttl_800c.battleHudTextureVramXOffsets_800c6e60;
import static legend.game.combat.Bttl_800c.battleHudYOffsetIndex_800c6c38;
import static legend.game.combat.Bttl_800c.battleHudYOffsets_800fb198;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.characterDragoonIndices_800c6e68;
import static legend.game.combat.Bttl_800c.combatPortraitBorderVertexCoords_800c6e9c;
import static legend.game.combat.Bttl_800c.combatUiElementRectDimensions_800c6e48;
import static legend.game.combat.Bttl_800c.combatantCount_800c66a0;
import static legend.game.combat.Bttl_800c.countCombatUiFilesLoaded_800c6cf4;
import static legend.game.combat.Bttl_800c.currentEnemyNames_800c69d0;
import static legend.game.combat.Bttl_800c.currentStage_800c66a4;
import static legend.game.combat.Bttl_800c.currentTurnBent_800c66c8;
import static legend.game.combat.Bttl_800c.cutsceneDeffsWithExtraTims_800fb05c;
import static legend.game.combat.Bttl_800c.deffLoadingStage_800fafe8;
import static legend.game.combat.Bttl_800c.deffManager_800c693c;
import static legend.game.combat.Bttl_800c.displayStats_800c6c2c;
import static legend.game.combat.Bttl_800c.dragoonDeffFlags_800fafec;
import static legend.game.combat.Bttl_800c.dragoonDeffsWithExtraTims_800fb040;
import static legend.game.combat.Bttl_800c.dragoonSpaceElement_800c6b64;
import static legend.game.combat.Bttl_800c.dragoonSpells_800c6960;
import static legend.game.combat.Bttl_800c.enemyDeffFileIndices_800faec4;
import static legend.game.combat.Bttl_800c.floatingNumbers_800c6b5c;
import static legend.game.combat.Bttl_800c.floatingTextType1Digits;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.hudNameAndPortraitMetrics_800fb444;
import static legend.game.combat.Bttl_800c.itemTargetAll_800c69c8;
import static legend.game.combat.Bttl_800c.itemTargetType_800c6b68;
import static legend.game.combat.Bttl_800c.lightTicks_800c6928;
import static legend.game.combat.Bttl_800c.light_800c6ddc;
import static legend.game.combat.Bttl_800c.lights_800c692c;
import static legend.game.combat.Bttl_800c.loadAttackAnimations;
import static legend.game.combat.Bttl_800c.melbuMonsterNameIndices;
import static legend.game.combat.Bttl_800c.melbuMonsterNames_800c6ba8;
import static legend.game.combat.Bttl_800c.modelColourMaps_800fb06c;
import static legend.game.combat.Bttl_800c.monsterBents_800c6b78;
import static legend.game.combat.Bttl_800c.monsterCount_800c6768;
import static legend.game.combat.Bttl_800c.monsterCount_800c6b9c;
import static legend.game.combat.Bttl_800c.playerNames_800fb378;
import static legend.game.combat.Bttl_800c.spBarBorderMetrics_800fb46c;
import static legend.game.combat.Bttl_800c.spBarColours_800c6f04;
import static legend.game.combat.Bttl_800c.spBarFlashingBorderMetrics_800fb47c;
import static legend.game.combat.Bttl_800c.spellAndItemMenu_800c6b60;
import static legend.game.combat.Bttl_800c.spriteMetrics_800c6948;
import static legend.game.combat.Bttl_800c.stageDarkeningClutWidth_800c695c;
import static legend.game.combat.Bttl_800c.stageDarkening_800c6958;
import static legend.game.combat.Bttl_800c.targetArrowOffsetY_800fb188;
import static legend.game.combat.Bttl_800c.targeting_800fb36c;
import static legend.game.combat.Bttl_800c.tmds_800c6944;
import static legend.game.combat.Bttl_800c.uiTextureElementBrightness_800c71ec;
import static legend.game.combat.Bttl_800c.usedRepeatItems_800c6c3c;
import static legend.game.combat.Bttl_800d.FUN_800dd89c;
import static legend.game.combat.Bttl_800d.applyAnimation;
import static legend.game.combat.Bttl_800d.loadModelAnim;
import static legend.game.combat.Bttl_800d.loadModelTmd;
import static legend.game.combat.Bttl_800d.optimisePacketsIfNecessary;
import static legend.game.combat.Bttl_800f.tickFloatingNumbers;
import static legend.game.combat.Bttl_800f.buildUiTextureElement;
import static legend.game.combat.Bttl_800f.clearBattleMenu;
import static legend.game.combat.Bttl_800f.clearSpellAndItemMenu;
import static legend.game.combat.Bttl_800f.deleteFloatingTextDigits;
import static legend.game.combat.Bttl_800f.drawFloatingNumbers;
import static legend.game.combat.Bttl_800f.drawItemMenuElements;
import static legend.game.combat.Bttl_800f.drawLine;
import static legend.game.combat.Bttl_800f.getTargetEnemyName;
import static legend.game.combat.Bttl_800f.handleSpellAndItemMenu;
import static legend.game.combat.Bttl_800f.prepareItemList;
import static legend.game.combat.Bttl_800f.renderNumber;
import static legend.game.combat.SBtld.monsterNames_80112068;
import static legend.game.combat.SBtld.monsterStats_8010ba98;
import static legend.game.combat.SEffe.addGenericAttachment;
import static legend.game.combat.SEffe.loadDeffStageEffects;

public final class Bttl_800e {
  private Bttl_800e() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Bttl_800e.class);
  private static final Marker EFFECTS = MarkerManager.getMarker("EFFECTS");
  private static final Marker DEFF = MarkerManager.getMarker("DEFF");

  @Method(0x800e45c0L)
  public static void FUN_800e45c0(final Vector3f out, final Vector3f in) {
    final float angle = MathHelper.atan2(in.x, in.z);
    final float sin = MathHelper.sin(-angle);
    final float cos = MathHelper.cosFromSin(sin, -angle);
    out.x = MathHelper.atan2(-in.y, cos * in.z - sin * in.x); // Angle from the XZ plane
    out.y = angle; // Angle from the X axis
    out.z = 0;
  }

  @Method(0x800e4674L)
  public static Vector3f FUN_800e4674(final Vector3f out, final Vector3f rotation) {
    return out.set(0.0f, 0.0f, 1.0f).mul(new Matrix3f().rotationZYX(rotation.z, rotation.y, rotation.z));
  }

  @Method(0x800e46c8L)
  public static void resetLights() {
    final BattleLightStruct64 v1 = _800c6930;
    v1.colour_00.set(0x800, 0x800, 0x800);

    final BttlLightStruct84 a0 = lights_800c692c[0];
    a0.light_00.direction_00.set(0.0f, 1.0f, 0.0f);
    a0.light_00.r_0c = 0.5f;
    a0.light_00.g_0d = 0.5f;
    a0.light_00.b_0e = 0.5f;
    a0._10._00 = 0;
    a0._4c._00 = 0;

    //LAB_800e4720
    lights_800c692c[1].clear();
    lights_800c692c[2].clear();
  }

  @ScriptDescription("Resets battle lighting")
  @Method(0x800e473cL)
  public static FlowControl scriptResetLights(final RunningScript<?> script) {
    resetLights();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e475cL)
  public static void setLightDirection(final int lightIndex, final float x, final float y, final float z) {
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.direction_00.set(x, y, z);
    light._10._00 = 0;
  }

  @ScriptDescription("Sets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e4788L)
  public static FlowControl scriptSetLightDirection(final RunningScript<?> script) {
    setLightDirection(script.params_20[0].get(), script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e47c8L)
  public static FlowControl scriptGetLightDirection(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    script.params_20[1].set((int)(light.light_00.direction_00.x * 0x1000));
    script.params_20[2].set((int)(light.light_00.direction_00.y * 0x1000));
    script.params_20[3].set((int)(light.light_00.direction_00.z * 0x1000));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4824L)
  public static void FUN_800e4824(final int lightIndex, final float x, final float y, final float z) {
    final Vector3f rotation = new Vector3f();
    FUN_800e4674(rotation, new Vector3f(x, y, z));
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.direction_00.set(rotation);
    light._10._00 = 0;
  }

  @ScriptDescription("Unknown, sets the battle light direction using the vector (0, 0, 1) rotated by ZYX")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e48a8L)
  public static FlowControl FUN_800e48a8(final RunningScript<?> script) {
    FUN_800e4824(script.params_20[0].get(), MathHelper.psxDegToRad(script.params_20[1].get()), MathHelper.psxDegToRad(script.params_20[2].get()), MathHelper.psxDegToRad(script.params_20[3].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets the battle light direction using the vector (0, 0, 1) rotated by ZYX")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e48e8L)
  public static FlowControl FUN_800e48e8(final RunningScript<?> script) {
    final Vector3f rotation = new Vector3f();
    FUN_800e45c0(rotation, lights_800c692c[script.params_20[0].get()].light_00.direction_00);
    script.params_20[1].set(MathHelper.radToPsxDeg(rotation.x));
    script.params_20[2].set(MathHelper.radToPsxDeg(rotation.y));
    script.params_20[3].set(MathHelper.radToPsxDeg(rotation.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "Either a light index or battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e4964L)
  public static FlowControl FUN_800e4964(final RunningScript<?> script) {
    final Vector3f rotation = new Vector3f();

    final int index = script.params_20[1].get();
    if(index != -1) {
      //LAB_800e49c0
      if(index > 0 && index - 1 < 3) {
        FUN_800e45c0(rotation, lights_800c692c[index - 1].light_00.direction_00);
      } else {
        //LAB_800e49f4
        final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[index].innerStruct_00;
        rotation.x = bent.model_148.coord2_14.transforms.rotate.x;
        rotation.z = bent.model_148.coord2_14.transforms.rotate.z;
      }
    }

    //LAB_800e4a34
    //LAB_800e4a38
    final Vector3f direction = new Vector3f();
    rotation.x += MathHelper.psxDegToRad(script.params_20[2].get());
    rotation.y += MathHelper.psxDegToRad(script.params_20[3].get());
    rotation.z += MathHelper.psxDegToRad(script.params_20[4].get());
    FUN_800e4674(direction, rotation);
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    light.light_00.direction_00.set(direction);
    light._10._00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.BOTH, type = ScriptParam.Type.INT, name = "indexAndX", description = "In: either a light index or battle entity index, out: the X direction (PSX degrees)") // why
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e4abcL)
  public static FlowControl FUN_800e4abc(final RunningScript<?> script) {
    final int s1 = script.params_20[1].get();

    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, lights_800c692c[script.params_20[0].get()].light_00.direction_00);

    final Vector3f s0;
    if(s1 - 1 < 3) {
      s0 = new Vector3f();
      FUN_800e45c0(s0, lights_800c692c[s1 - 1].light_00.direction_00);
    } else {
      //LAB_800e4b40
      final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[s1].innerStruct_00;
      s0 = bent.model_148.coord2_14.transforms.rotate;
    }

    //LAB_800e4b64
    script.params_20[1].set(MathHelper.radToPsxDeg(sp0x10.x - s0.x));
    script.params_20[2].set(MathHelper.radToPsxDeg(sp0x10.y - s0.y));
    script.params_20[3].set(MathHelper.radToPsxDeg(sp0x10.z - s0.z));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4bc0L)
  public static void setBattleLightColour(final int lightIndex, final float r, final float g, final float b) {
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.r_0c = r;
    light.light_00.g_0d = g;
    light.light_00.b_0e = b;
    light._4c._00 = 0;
  }

  @ScriptDescription("Sets the battle light colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (8-bit fixed-point)")
  @Method(0x800e4c10L)
  public static FlowControl scriptSetBattleLightColour(final RunningScript<?> script) {
    setBattleLightColour(script.params_20[0].get(), script.params_20[1].get() / (float)0x100, script.params_20[2].get() / (float)0x100, script.params_20[3].get() / (float)0x100);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the battle light colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "r", description = "The red channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "g", description = "The green channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (8-bit fixed-point)")
  @Method(0x800e4c90L)
  public static FlowControl scriptGetBattleLightColour(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    script.params_20[1].set((int)(light.light_00.r_0c * 0x100));
    script.params_20[2].set((int)(light.light_00.g_0d * 0x100));
    script.params_20[3].set((int)(light.light_00.b_0e * 0x100));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4cf8L)
  public static void setBattleBackgroundLight(final float r, final float g, final float b) {
    final BattleLightStruct64 v0 = _800c6930;
    v0.colour_00.set(r, g, b);
    v0._24 = 0;
    GTE.setBackgroundColour(r, g, b);
  }

  @ScriptDescription("Sets the battle light background colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (12-bit fixed-point)")
  @Method(0x800e4d2cL)
  public static FlowControl scriptSetBattleBackgroundLightColour(final RunningScript<?> script) {
    setBattleBackgroundLight(script.params_20[0].get() / 4096.0f, script.params_20[1].get() / 4096.0f, script.params_20[2].get() / 4096.0f);
    _800c6930._24 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4d74L)
  public static void getBattleBackgroundLightColour(final Vector3f colour) {
    final BattleLightStruct64 light = _800c6930;
    colour.set(light.colour_00);
  }

  @ScriptDescription("Sets the battle light background colour")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "r", description = "The red channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "g", description = "The green channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (12-bit fixed-point)")
  @Method(0x800e4db4L)
  public static FlowControl scriptGetBattleBackgroundLightColour(final RunningScript<?> script) {
    final BattleLightStruct64 v0 = _800c6930;
    script.params_20[0].set((int)(v0.colour_00.x * 0x1000));
    script.params_20[1].set((int)(v0.colour_00.y * 0x1000));
    script.params_20[2].set((int)(v0.colour_00.z * 0x1000));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e4dfcL)
  public static FlowControl FUN_800e4dfc(final RunningScript<?> script) {
    lights_800c692c[script.params_20[0].get()]._10._00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e4e2cL)
  public static FlowControl FUN_800e4e2c(final RunningScript<?> script) {
    return lights_800c692c[script.params_20[0].get()]._10._00 != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out")
  @Method(0x800e4e64L)
  public static FlowControl FUN_800e4e64(final RunningScript<?> script) {
    script.params_20[1].set(lights_800c692c[script.params_20[0].get()]._10._00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, must change battle light over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks for the change")
  @Method(0x800e4ea0L)
  public static FlowControl FUN_800e4ea0(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final int ticks = script.params_20[4].get();
    final BttlLightStruct84Sub38 t0 = light._10;

    t0._00 = 0;
    t0.angleAndColour_04.set(light.light_00.direction_00);
    t0.vec_28.set(MathHelper.psxDegToRad(script.params_20[1].get()), MathHelper.psxDegToRad(script.params_20[2].get()), MathHelper.psxDegToRad(script.params_20[3].get()));
    t0.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      t0.vec_10.x = (t0.vec_28.x - t0.angleAndColour_04.x) / ticks;
      t0.vec_10.y = (t0.vec_28.y - t0.angleAndColour_04.y) / ticks;
      t0.vec_10.z = (t0.vec_28.z - t0.angleAndColour_04.z) / ticks;
      t0.vec_1c.zero();
      t0._00 = 0xa001;
    }

    //LAB_800e4f98
    return FlowControl.CONTINUE;
  }

  /** Used in gates of heaven */
  @ScriptDescription("Unknown, must change battle light over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks for the change")
  @Method(0x800e4fa0L)
  public static FlowControl FUN_800e4fa0(final RunningScript<?> script) {
    final float x = MathHelper.psxDegToRad(script.params_20[1].get());
    final float y = MathHelper.psxDegToRad(script.params_20[2].get());
    final float z = MathHelper.psxDegToRad(script.params_20[3].get());
    final int ticks = script.params_20[4].get();

    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);
    light._10._00 = 0;

    final BttlLightStruct84Sub38 a3 = light._10;
    a3.angleAndColour_04.set(sp0x10);
    a3.vec_28.set(x, y, z);
    a3.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      a3.vec_1c.zero();
      a3.vec_10.x = (x - a3.angleAndColour_04.x) / ticks;
      a3.vec_10.y = (y - a3.angleAndColour_04.y) / ticks;
      a3.vec_10.z = (z - a3.angleAndColour_04.z) / ticks;
      a3._00 = 0xc001;
    }

    //LAB_800e50c0
    //LAB_800e50c4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, must change battle light over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightOrBentIndex", description = "Either a light index or battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks for the change")
  @Method(0x800e50e8L)
  public static FlowControl FUN_800e50e8(final RunningScript<?> script) {
    final int lightIndex = script.params_20[0].get();
    final int lightOrBentIndex = script.params_20[1].get();
    final int x = script.params_20[2].get();
    final int y = script.params_20[3].get();
    final int z = script.params_20[4].get();
    final int ticks = script.params_20[5].get();

    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, lights_800c692c[lightIndex].light_00.direction_00);

    final BttlLightStruct84Sub38 s0 = lights_800c692c[lightIndex]._10;
    s0._00 = 0;
    s0.angleAndColour_04.set(sp0x10);

    if(lightOrBentIndex > 0 && lightOrBentIndex < 4) {
      final Vector3f sp0x18 = new Vector3f();
      FUN_800e45c0(sp0x18, lights_800c692c[lightOrBentIndex - 1].light_00.direction_00);
      s0.vec_28.set(sp0x18);
    } else {
      //LAB_800e51e8
      final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[lightOrBentIndex].innerStruct_00;
      s0.vec_28.set(bent.model_148.coord2_14.transforms.rotate);
    }

    //LAB_800e522c
    s0.ticksRemaining_34 = ticks;
    s0.vec_28.add(x, y, z);

    if(ticks > 0) {
      s0._00 = 0xc001;
      s0.vec_10.set(s0.vec_28).sub(s0.angleAndColour_04).div(ticks);
      s0.vec_1c.zero();
    }

    //LAB_800e52c8
    //LAB_800e52cc
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z1")
  @Method(0x800e52f8L)
  public static FlowControl FUN_800e52f8(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub38 v1 = light._10;
    v1._00 = 0x4001;
    v1.angleAndColour_04.set(sp0x10);
    v1.vec_10.set(script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    v1.vec_1c.set(script.params_20[4].get() / (float)0x1000, script.params_20[5].get() / (float)0x1000, script.params_20[6].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c's script index")
  @Method(0x800e540cL)
  public static FlowControl FUN_800e540c(final RunningScript<?> script) {
    final int bentIndex = script.params_20[1].get();
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];

    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub38 a0_0 = light._10;
    a0_0._00 = 0x4002;
    light.scriptIndex_48 = bentIndex;

    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;
    a0_0.angleAndColour_04.set(sp0x10).sub(bent.model_148.coord2_14.transforms.rotate);
    a0_0.vec_10.zero();
    a0_0.vec_1c.zero();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e54f8L)
  public static FlowControl FUN_800e54f8(final RunningScript<?> script) {
    lights_800c692c[script.params_20[0].get()]._4c._00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e5528L)
  public static FlowControl FUN_800e5528(final RunningScript<?> script) {
    return lights_800c692c[script.params_20[0].get()]._4c._00 != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value")
  @Method(0x800e5560L)
  public static FlowControl FUN_800e5560(final RunningScript<?> script) {
    script.params_20[1].set(lights_800c692c[script.params_20[0].get()]._4c._00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "The number of ticks")
  @Method(0x800e559cL)
  public static FlowControl FUN_800e559c(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final int ticks = script.params_20[4].get();

    final BttlLightStruct84Sub38 t0 = light._4c;
    t0._00 = 0;
    t0.angleAndColour_04.x = light.light_00.r_0c;
    t0.angleAndColour_04.y = light.light_00.g_0d;
    t0.angleAndColour_04.z = light.light_00.b_0e;
    t0.vec_28.set(script.params_20[1].get() / (float)0x80, script.params_20[2].get() / (float)0x80, script.params_20[3].get() / (float)0x80);
    t0.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      t0.vec_1c.zero();
      t0.vec_10.set(t0.vec_28).sub(t0.angleAndColour_04).div(ticks);
      t0._00 = 0x8001;
    }

    //LAB_800e5694
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z1")
  @Method(0x800e569cL)
  public static FlowControl FUN_800e569c(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final BttlLightStruct84Sub38 v1 = light._4c;
    v1._00 = 0;
    v1.angleAndColour_04.set(light.light_00.r_0c, light.light_00.g_0d, light.light_00.b_0e);
    v1.vec_10.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    v1.vec_1c.set(script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get());

    if(v1.ticksRemaining_34 > 0) {
      v1._00 = 0x1;
    }

    //LAB_800e5760
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5768L)
  public static void applyStageAmbiance(final StageAmbiance4c ambiance) {
    setBattleBackgroundLight(ambiance.ambientColour_00.x, ambiance.ambientColour_00.y, ambiance.ambientColour_00.z);

    final BattleLightStruct64 v1 = _800c6930;
    if(ambiance._0e > 0) {
      v1.colour1_0c.set(ambiance.ambientColour_00);
      v1.colour2_18.set(ambiance._06);
      v1._24 = 3;
      v1._2c = (short)ambiance._0c;
      v1._2e = (short)ambiance._0e;
    } else {
      //LAB_800e5808
      v1._24 = 0;
    }

    //LAB_800e5814
    //LAB_800e5828
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 a1 = lights_800c692c[i];
      final BattleStruct14 a0 = ambiance._10[i];
      a1.light_00.direction_00.set(a0.lightDirection_00);
      a1.light_00.r_0c = a0.lightColour_0a.getR() / (float)0x100;
      a1.light_00.g_0d = a0.lightColour_0a.getG() / (float)0x100;
      a1.light_00.b_0e = a0.lightColour_0a.getB() / (float)0x100;

      if(a0.x_06 != 0 || a0.y_08 != 0) {
        a1._10._00 = 0x3;
        a1._10.angleAndColour_04.set(a1.light_00.direction_00);
        a1._10.vec_10.set(a0.x_06, a0.y_08, 0);
      } else {
        //LAB_800e58cc
        a1._10._00 = 0;
      }

      //LAB_800e58d0
      if(a0.y_12 != 0) {
        a1._4c._00 = 0x3;
        a1._4c.angleAndColour_04.set(a1.light_00.r_0c, a1.light_00.g_0d, a1.light_00.b_0e);
        a1._4c.vec_10.set(a0._0d.getR() / (float)0x100, a0._0d.getG() / (float)0x100, a0._0d.getB() / (float)0x100);
        a1._4c.vec_28.x = a0.x_10;
        a1._4c.vec_28.y = a0.y_12;
      } else {
        //LAB_800e5944
        a1._4c._00 = 0;
      }

      //LAB_800e5948
    }
  }

  @ScriptDescription("Applies the current battle stage's ambiance (including Dragoon Space)")
  @Method(0x800e596cL)
  public static FlowControl scriptApplyStageAmbiance(final RunningScript<?> script) {
    final int v0 = currentStage_800c66a4.get() - 0x47;

    if(v0 >= 0 && v0 < 0x8) {
      applyStageAmbiance(deffManager_800c693c.dragoonSpaceAmbiance_98[v0]);
    } else {
      //LAB_800e59b0
      applyStageAmbiance(deffManager_800c693c.stageAmbiance_4c);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets or applies stage ambiance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "-1, -2, -3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "data", description = "Int or int array")
  @Method(0x800e59d8L)
  public static FlowControl FUN_800e59d8(final RunningScript<?> script) {
    final int a0 = script.params_20[0].get();

    if(a0 == -1) {
      deffManager_800c693c.stageAmbiance_4c.set(script.params_20[1]);
    } else if(a0 == -2) {
      //LAB_800e5a38
      //LAB_800e5a60
      applyStageAmbiance(new StageAmbiance4c().set(script.params_20[1]));
      //LAB_800e5a14
    } else if(a0 == -3) {
      //LAB_800e5a40
      applyStageAmbiance(deffManager_800c693c.dragoonSpaceAmbiance_98[script.params_20[1].get()]);
    }

    //LAB_800e5a68
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5a78L)
  public static void tickLighting(final ScriptState<Void> state, final Void struct) {
    final BattleLightStruct64 light1 = _800c6930;

    lightTicks_800c6928.incr();

    if(light1._24 == 3) { // Dragoon space lighting is handled here, I think this is for flickering light
      final int angle = rcos(((lightTicks_800c6928.get() + light1._2c) % light1._2e << 12) / light1._2e);
      final float minAngle = (0x1000 - angle) / (float)0x1000;
      final float maxAngle = (0x1000 + angle) / (float)0x1000;
      light1.colour_00.x = (light1.colour1_0c.x * maxAngle + light1.colour2_18.x * minAngle) / 2.0f;
      light1.colour_00.y = (light1.colour1_0c.y * maxAngle + light1.colour2_18.y * minAngle) / 2.0f;
      light1.colour_00.z = (light1.colour1_0c.z * maxAngle + light1.colour2_18.z * minAngle) / 2.0f;
    }

    //LAB_800e5b98
    //LAB_800e5ba0
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 light = lights_800c692c[i];
      final BttlLightStruct84Sub38 a2 = light._10;

      int v1 = a2._00 & 0xff;
      if(v1 == 1) {
        //LAB_800e5c50
        a2.vec_10.add(a2.vec_1c);
        a2.angleAndColour_04.add(a2.vec_10);

        if((a2._00 & 0x8000) != 0) {
          a2.ticksRemaining_34--;

          if(a2.ticksRemaining_34 <= 0) {
            a2._00 = 0;
            a2.angleAndColour_04.set(a2.vec_28);
          }
        }

        //LAB_800e5cf4
        if((a2._00 & 0x2000) != 0) {
          light.light_00.direction_00.set(a2.angleAndColour_04);
          //LAB_800e5d40
        } else if((a2._00 & 0x4000) != 0) {
          final Vector3f sp0x18 = new Vector3f();
          sp0x18.set(a2.angleAndColour_04);
          FUN_800e4674(light.light_00.direction_00, sp0x18);
        }
      } else if(v1 == 2) {
        //LAB_800e5bf0
        final Vector3f sp0x10 = new Vector3f();
        final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[light.scriptIndex_48].innerStruct_00;
        sp0x10.set(bent.model_148.coord2_14.transforms.rotate).add(a2.angleAndColour_04);
        FUN_800e4674(light.light_00.direction_00, sp0x10);
      } else if(v1 == 3) {
        //LAB_800e5bdc
        //LAB_800e5d6c
        final Vector3f sp0x18 = new Vector3f();

        final int ticks = lightTicks_800c6928.get() & 0xfff;
        sp0x18.x = a2.angleAndColour_04.x + a2.vec_10.x * ticks;
        sp0x18.y = a2.angleAndColour_04.y + a2.vec_10.y * ticks;
        sp0x18.z = a2.angleAndColour_04.z + a2.vec_10.z * ticks;

        //LAB_800e5dcc
        FUN_800e4674(light.light_00.direction_00, sp0x18);
      }

      //LAB_800e5dd4
      final BttlLightStruct84Sub38 s0 = light._4c;
      v1 = s0._00 & 0xff;
      if(v1 == 1) {
        //LAB_800e5df4
        s0.vec_10.add(s0.vec_1c);
        s0.angleAndColour_04.add(s0.vec_10);

        if((s0._00 & 0x8000) != 0) {
          s0.ticksRemaining_34--;

          if(s0.ticksRemaining_34 <= 0) {
            s0._00 = 0;
            s0.angleAndColour_04.set(s0.vec_28);
          }
        }

        //LAB_800e5e90
        lights_800c692c[i].light_00.r_0c = s0.angleAndColour_04.x;
        lights_800c692c[i].light_00.g_0d = s0.angleAndColour_04.y;
        lights_800c692c[i].light_00.b_0e = s0.angleAndColour_04.z;
      } else if(v1 == 3) {
        //LAB_800e5ed0
        final float theta = MathHelper.cos((lightTicks_800c6928.get() / (float)0x1000 + s0.vec_28.x) % s0.vec_28.y / s0.vec_28.y);
        final float ratioA = 1.0f + theta;
        final float ratioB = 1.0f - theta;
        lights_800c692c[i].light_00.r_0c = (s0.angleAndColour_04.x * ratioA + s0.vec_10.x * ratioB) / 2.0f;
        lights_800c692c[i].light_00.g_0d = (s0.angleAndColour_04.y * ratioA + s0.vec_10.y * ratioB) / 2.0f;
        lights_800c692c[i].light_00.b_0e = (s0.angleAndColour_04.z * ratioA + s0.vec_10.z * ratioB) / 2.0f;
      }

      //LAB_800e5fb8
      //LAB_800e5fbc
    }
  }

  @Method(0x800e5fe8L)
  public static void deallocateLighting(final ScriptState<Void> state, final Void struct) {
    //LAB_800e6008
    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, lights_800c692c[i].light_00);
    }

    final BattleLightStruct64 v0 = _800c6930;
    GTE.setBackgroundColour(v0.colour_00.x, v0.colour_00.y, v0.colour_00.z);
    projectionPlaneDistance_1f8003f8 = getProjectionPlaneDistance();
  }

  @Method(0x800e6070L)
  public static void allocateLighting() {
    final ScriptState<Void> state = SCRIPTS.allocateScriptState(1, "Lighting controller", 0, null);
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(Bttl_800e::tickLighting);
    state.setRenderer(Bttl_800e::deallocateLighting);
    _800c6930.colourIndex_60 = 0;
    resetLights();
  }

  @Method(0x800e60e0L)
  public static void FUN_800e60e0(final float r, final float g, final float b) {
    if(r < 0.0f) {
      LOGGER.warn("Negative R! %f", r);
    }

    if(g < 0.0f) {
      LOGGER.warn("Negative G! %f", g);
    }

    if(b < 0.0f) {
      LOGGER.warn("Negative B! %f", b);
    }

    final BattleLightStruct64 light = _800c6930;
    final Vector3f colour = light.colours_30[light.colourIndex_60];
    getBattleBackgroundLightColour(colour);

    light.colour_00.set(r, g, b);
    light.colourIndex_60 = light.colourIndex_60 + 1 & 3;
  }

  @Method(0x800e6170L)
  public static void FUN_800e6170() {
    final BattleLightStruct64 light = _800c6930;
    light.colourIndex_60 = light.colourIndex_60 - 1 & 3;
    light.colour_00.set(light.colours_30[light.colourIndex_60]);
  }

  @Method(0x800e61e4L)
  public static void FUN_800e61e4(final float r, final float g, final float b) {
    if(r < 0.0f) {
      LOGGER.warn("Negative R! %f", r);
    }

    if(g < 0.0f) {
      LOGGER.warn("Negative G! %f", g);
    }

    if(b < 0.0f) {
      LOGGER.warn("Negative B! %f", b);
    }

    GsSetFlatLight(0, light_800c6ddc);
    GsSetFlatLight(1, light_800c6ddc);
    GsSetFlatLight(2, light_800c6ddc);
    FUN_800e60e0(r, g, b);

    final BattleLightStruct64 light = _800c6930;
    GTE.setBackgroundColour(light.colour_00.x, light.colour_00.y, light.colour_00.z);
  }

  @Method(0x800e62a8L)
  public static void FUN_800e62a8() {
    FUN_800e6170();

    final BattleLightStruct64 light = _800c6930;
    GTE.setBackgroundColour(light.colour_00.x, light.colour_00.y, light.colour_00.z);

    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, lights_800c692c[i].light_00);
    }
  }

  @Method(0x800e6314L)
  public static void scriptDeffDeallocator(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> data) {
    LOGGER.info(DEFF, "Deallocating DEFF script state %d", state.index);

    final DeffManager7cc struct7cc = deffManager_800c693c;

    struct7cc.deffPackage_5a8 = null;

    deffLoadingStage_800fafe8.set(4);

    if((struct7cc.flags_20 & 0x4_0000) != 0) {
      loadDeffSounds(_800c6938.bentState_04, 1);
    }

    if((struct7cc.flags_20 & 0x10_0000) != 0) {
      //LAB_800e63d0
      for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
        final CombatantStruct1a8 combatant = getCombatant(i);
        if((combatant.flags_19e & 0x1) != 0 && combatant.charIndex_1a2 >= 0) {
          loadAttackAnimations(i);
        }

        //LAB_800e6408
      }
    }

    //LAB_800e641c
    if((struct7cc.flags_20 & 0x60_0000) != 0) {
      loadDeffStageEffects(0);
    }

    //LAB_800e6444
    struct7cc.flags_20 &= 0xff80_ffff;
  }

  @ScriptDescription("Allocates an effect manager child for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6470L)
  public static ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> scriptAllocateDeffEffectManager(final RunningScript<? extends BattleObject> script) {
    final DeffManager7cc struct7cc = deffManager_800c693c;

    final int flags = script.params_20[0].get();
    struct7cc.flags_20 |= flags & 0x1_0000 | flags & 0x2_0000 | flags & 0x10_0000;

    if((struct7cc.flags_20 & 0x10_0000) != 0) {
      //LAB_800e651c
      for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
        final CombatantStruct1a8 v1 = getCombatant(i);

        if((v1.flags_19e & 0x1) != 0 && v1.mrg_04 != null && v1.charIndex_1a2 >= 0) {
          FUN_800ca418(i);
        }

        //LAB_800e6564
      }
    }

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state = allocateEffectManager(
      "DEFF ticker for script %d (%s)".formatted(script.scriptState_04.index, script.scriptState_04.name),
      script.scriptState_04,
      Bttl_800e::scriptDeffTicker,
      null,
      Bttl_800e::scriptDeffDeallocator,
      null
    );

    LOGGER.info(DEFF, "Allocated DEFF script state %d", state.index);

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = state.innerStruct_00;
    manager.flags_04 = 0x600_0400;

    final BattleStruct24_2 v0 = _800c6938;
    v0.type_00 = flags & 0xffff;
    v0.bentState_04 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()];
    v0._08 = script.params_20[2].get();
    v0.scriptIndex_0c = script.scriptState_04.index;
    v0.scriptEntrypoint_10 = script.params_20[3].get() & 0xff;
    v0.managerState_18 = state;
    v0.init_1c = false;
    v0.frameCount_20 = -1;
    // S_EFFE
    loadSupportOverlay(3, () -> v0.init_1c = true);
    return state;
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e665cL)
  public static void loadDragoonDeff(final RunningScript<? extends BattleObject> script) {
    final int index = script.params_20[0].get() & 0xffff;
    final int scriptEntrypoint = script.params_20[3].get() & 0xff;

    LOGGER.info(DEFF, "Loading dragoon DEFF (ID: %d, flags: %x)", index, script.params_20[0].get() & 0xffff_0000);

    final DeffManager7cc deffManager = deffManager_800c693c;
    deffManager.flags_20 |= dragoonDeffFlags_800fafec.get(index).get() << 16;
    scriptAllocateDeffEffectManager(script);

    final BattleStruct24_2 battle24 = _800c6938;
    battle24.type_00 |= 0x100_0000;

    if((deffManager.flags_20 & 0x4_0000) != 0) {
      //LAB_800e66fc
      //LAB_800e670c
      loadDeffSounds(battle24.bentState_04, index != 0x2e || scriptEntrypoint != 0 ? 0 : 2);
    }

    //LAB_800e6714
    if(battle24.script_14 != null) {
      battle24.script_14 = null;
    }

    //LAB_800e6738
    for(int i = 0; dragoonDeffsWithExtraTims_800fb040.get(i).get() != -1; i++) {
      if(dragoonDeffsWithExtraTims_800fb040.get(i).get() == index) {
        if(Unpacker.isDirectory("SECT/DRGN0.BIN/%d".formatted(4115 + i))) {
          loadDrgnDir(0, 4115 + i, Bttl_800e::uploadTims);
        }
      }

      //LAB_800e679c
    }

    //LAB_800e67b0
    loadDrgnDir(0, 4139 + index * 2, Bttl_800e::uploadTims);
    loadDrgnDir(0, 4140 + index * 2 + "/0", files -> {
      Bttl_800e.loadDeffPackage(files, battle24.managerState_18);

      // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
      loadDrgnFile(0, 4140 + index * 2 + "/1", file -> {
        LOGGER.info(DEFF, "Loading DEFF script");
        _800c6938.script_14 = new ScriptFile(4140 + index * 2 + "/1", file.getBytes());
      });
    });
    deffLoadingStage_800fafe8.set(1);
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a spell or item")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6844L)
  public static void loadSpellItemDeff(final RunningScript<? extends BattleObject> script) {
    final int id = script.params_20[0].get() & 0xffff;
    final int s0 = (id - 192) * 2;

    LOGGER.info(DEFF, "Loading spell item DEFF (ID: %d, flags: %x)", id, script.params_20[0].get() & 0xffff_0000);

    deffManager_800c693c.flags_20 |= 0x40_0000;
    scriptAllocateDeffEffectManager(script);

    final BattleStruct24_2 t0 = _800c6938;

    if(t0.script_14 != null) {
      t0.script_14 = null;
    }

    t0.type_00 |= 0x200_0000;
    loadDrgnDir(0, 4307 + s0, Bttl_800e::uploadTims);
    loadDrgnDir(0, 4308 + s0 + "/0", files -> {
      Bttl_800e.loadDeffPackage(files, t0.managerState_18);

      // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
      loadDrgnFile(0, 4308 + s0 + "/1", file -> {
        LOGGER.info(DEFF, "Loading DEFF script");
        _800c6938.script_14 = new ScriptFile(4308 + s0 + "/1", file.getBytes());
      });
    });
    deffLoadingStage_800fafe8.set(1);
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for an enemy or boss")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6920L)
  public static void loadEnemyOrBossDeff(final RunningScript<? extends BattleObject> script) {
    final int s1 = script.params_20[0].get() & 0xff_0000;
    int monsterIndex = (short)script.params_20[0].get();

    if(monsterIndex == -1) {
      final BattleEntity27c v0 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;
      assert false : "?"; //script.params_20.get(0).set(sp0x20);
      monsterIndex = getCombatant(v0.combatantIndex_26c).charIndex_1a2;
    }

    LOGGER.info(DEFF, "Loading enemy/boss DEFF (ID: %d, flags: %x)", monsterIndex, s1 & 0xffff_0000);

    //LAB_800e69a8
    deffManager_800c693c.flags_20 |= s1 & 0x10_0000;
    scriptAllocateDeffEffectManager(script);

    final BattleStruct24_2 v1 = _800c6938;

    if(v1.script_14 != null) {
      v1.script_14 = null;
    }

    v1.type_00 |= 0x300_0000;

    if(monsterIndex < 256) {
      final int finalMonsterIndex = monsterIndex;
      loadDrgnDir(0, 4433 + monsterIndex * 2, Bttl_800e::uploadTims);
      loadDrgnDir(0, 4434 + monsterIndex * 2 + "/0", files -> {
        Bttl_800e.loadDeffPackage(files, v1.managerState_18);

        // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
        loadDrgnFile(0, 4434 + finalMonsterIndex * 2 + "/1", file -> {
          LOGGER.info(DEFF, "Loading DEFF script");
          _800c6938.script_14 = new ScriptFile(4434 + finalMonsterIndex * 2 + "/1", file.getBytes());
        });
      });
    } else {
      //LAB_800e6a30
      final int a0_0 = monsterIndex >>> 4;
      int fileIndex = enemyDeffFileIndices_800faec4.get(a0_0 - 0x100).get() + (monsterIndex & 0xf);
      if(a0_0 >= 320) {
        fileIndex += 117;
      }

      //LAB_800e6a60
      fileIndex = (fileIndex - 1) * 2;
      final int finalFileIndex = fileIndex;

      loadDrgnDir(0, 4945 + fileIndex, Bttl_800e::uploadTims);
      loadDrgnDir(0, 4946 + fileIndex + "/0", files -> {
        Bttl_800e.loadDeffPackage(files, v1.managerState_18);

        // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
        loadDrgnFile(0, 4946 + finalFileIndex + "/1", file -> {
          LOGGER.info(DEFF, "Loading DEFF script");
          _800c6938.script_14 = new ScriptFile(4946 + finalFileIndex + "/1", file.getBytes());
        });
      });
    }

    //LAB_800e6a9c
    deffLoadingStage_800fafe8.set(1);
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a cutscene")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6aecL)
  public static void loadCutsceneDeff(final RunningScript<? extends BattleObject> script) {
    final int v1 = script.params_20[0].get();
    final int cutsceneIndex = v1 & 0xffff;

    LOGGER.info(DEFF, "Loading cutscene DEFF (ID: %d, flags: %x)", cutsceneIndex, v1 & 0xffff_0000);

    scriptAllocateDeffEffectManager(script);

    final BattleStruct24_2 a0_0 = _800c6938;

    if(a0_0.script_14 != null) {
      a0_0.script_14 = null;
    }

    a0_0.type_00 |= 0x500_0000;

    //LAB_800e6b5c
    for(int i = 0; cutsceneDeffsWithExtraTims_800fb05c.get(i).get() != -1; i++) {
      if(cutsceneDeffsWithExtraTims_800fb05c.get(i).get() == cutsceneIndex) {
        if(Unpacker.isDirectory("SECT/DRGN0.BIN/%d".formatted(5505 + i))) {
          loadDrgnDir(0, 5505 + i, Bttl_800e::uploadTims);
        }
      }

      //LAB_800e6bc0
    }

    //LAB_800e6bd4
    loadDrgnDir(0, 5511 + cutsceneIndex * 2, Bttl_800e::uploadTims);
    loadDrgnDir(0, 5512 + cutsceneIndex * 2 + "/0", files -> {
      Bttl_800e.loadDeffPackage(files, a0_0.managerState_18);

      // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
      loadDrgnFile(0, 5512 + cutsceneIndex * 2 + "/1", file -> {
        LOGGER.info(DEFF, "Loading DEFF script");
        _800c6938.script_14 = new ScriptFile(5512 + cutsceneIndex * 2 + "/1", file.getBytes());
      });
    });

    //LAB_800e6d7c
    deffLoadingStage_800fafe8.set(1);
  }

  @ScriptDescription("Unknown, related to loading DEFFs")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800e6db4L)
  public static FlowControl FUN_800e6db4(final RunningScript<?> script) {
    final FlowControl flow;
    final int deffStage;
    switch(script.params_20[0].get() & 0xffff) {
      case 0, 1 -> {
        deffStage = deffLoadingStage_800fafe8.get();
        if(deffStage == 1) {
          //LAB_800e6e20
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(deffStage == 2) {
          //LAB_800e6e28
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined a2");
        }

        //LAB_800e6e2c
      }

      case 2 -> {
        deffStage = deffLoadingStage_800fafe8.get();
        if(deffStage == 1) {
          //LAB_800e6e58
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(deffStage == 2) {
          final DeffManager7cc struct7cc = deffManager_800c693c;

          //LAB_800e6e60
          if((struct7cc.flags_20 & 0x20_0000) != 0) {
            loadDeffStageEffects(1);
          }

          //LAB_800e6e88
          if((struct7cc.flags_20 & 0x40_0000) != 0) {
            loadDeffStageEffects(3);
          }

          //LAB_800e6eb0
          final BattleStruct24_2 struct24 = _800c6938;
          struct24.managerState_18.loadScriptFile(struct24.script_14, struct24.scriptEntrypoint_10);
          struct24.init_1c = false;
          struct24.frameCount_20 = 0;
          deffLoadingStage_800fafe8.set(3);
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined t0");
        }

        //LAB_800e6ee4
      }

      case 3 -> {
        deffStage = deffLoadingStage_800fafe8.get();
        if(deffStage == 3) {
          //LAB_800e6f10
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(deffStage == 4) {
          //LAB_800e6f18
          deffLoadingStage_800fafe8.set(0);
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined a3");
        }

        //LAB_800e6f20
      }

      case 4 -> {
        switch(deffLoadingStage_800fafe8.get()) {
          case 0:
            flow = FlowControl.CONTINUE;
            break;

          case 1:
            flow = FlowControl.PAUSE_AND_REWIND;
            break;

          case 2:
          case 3:
            _800c6938.managerState_18.deallocateWithChildren();

          case 4:
            deffLoadingStage_800fafe8.set(0);
            _800c6938.managerState_18 = null;
            flow = FlowControl.CONTINUE;
            break;

          default:
            throw new RuntimeException("Undefined a1");
        }

        //LAB_800e6f9c
      }

      default -> throw new RuntimeException("Undefined flow");
    }

    //LAB_800e6fa0
    return flow;
  }

  @ScriptDescription("Unknown, can allocate a DEFF and effect manager child for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6fb4L)
  public static FlowControl FUN_800e6fb4(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e6fec
    //LAB_800e6ff0
    final long v1 = deffLoadingStage_800fafe8.get();

    //LAB_800e7014
    if(v1 == 0) {
      loadDragoonDeff(script);
    }

    if(v1 < 4) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(v1 == 4) {
      //LAB_800e702c
      deffLoadingStage_800fafe8.set(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    throw new IllegalStateException("Invalid v1");
  }

  @Method(0x800e7060L)
  public static void loadDeffPackage(final List<FileData> files, final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state) {
    LOGGER.info(DEFF, "Loading DEFF files");

    deffManager_800c693c.deffPackage_5a8 = new DeffPart[files.size()];
    Arrays.setAll(deffManager_800c693c.deffPackage_5a8, i -> DeffPart.getDeffPart(files, i));
    prepareDeffFiles(files, state);
  }

  @Method(0x800e70bcL)
  public static void scriptDeffTicker(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> struct) {
    final BattleStruct24_2 a0 = _800c6938;

    if(a0.frameCount_20 != -1) {
      a0.frameCount_20 += vsyncMode_8007a3b8;
    }

    //LAB_800e70fc
    if(a0.init_1c && a0.script_14 != null) {
      final DeffManager7cc struct7cc = deffManager_800c693c;

      if((struct7cc.flags_20 & 0x4_0000) == 0 || (getLoadedDrgnFiles() & 0x40) == 0) {
        //LAB_800e7154
        if((struct7cc.flags_20 & 0x20_0000) != 0) {
          loadDeffStageEffects(1);
        }

        //LAB_800e7178
        if((struct7cc.flags_20 & 0x40_0000) != 0) {
          loadDeffStageEffects(3);
        }

        //LAB_800e719c
        state.loadScriptFile(a0.script_14, a0.scriptEntrypoint_10);
        a0.init_1c = false;
        a0.frameCount_20 = 0;
      }
    }

    //LAB_800e71c4
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a spell or item")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e71e4L)
  public static FlowControl scriptLoadSpellOrItemDeff(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e721c
    //LAB_800e7220
    final int deffStage = deffLoadingStage_800fafe8.get();

    if(deffStage == 4) {
      //LAB_800e725c
      deffLoadingStage_800fafe8.set(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e7244
    if(deffStage == 0) {
      loadSpellItemDeff(script);
    }

    //LAB_800e726c
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for an enemy or boss")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e727cL)
  public static FlowControl scriptLoadEnemyOrBossDeff(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e72b4
    //LAB_800e72b8
    final int deffStage = deffLoadingStage_800fafe8.get();

    if(deffStage == 4) {
      //LAB_800e72f4
      deffLoadingStage_800fafe8.set(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e72dc
    if(deffStage == 0) {
      loadEnemyOrBossDeff(script);
    }

    //LAB_800e7304
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a cutscene")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e7314L)
  public static FlowControl scriptLoadCutsceneDeff(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e734c
    //LAB_800e7350
    final int deffStage = deffLoadingStage_800fafe8.get();

    if(deffStage == 4) {
      //LAB_800e738c
      deffLoadingStage_800fafe8.set(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e7374
    if(deffStage == 0) {
      loadCutsceneDeff(script);
    }

    //LAB_800e739c
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Allocates a DEFF and effect manager child")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0x100_0000 = dragoon, 0x200_0000 = spell/item, 0x300_0000/0x400_0000 = enemy/boss, 0x500_0000 = cutscene")
  @Method(0x800e73acL)
  public static FlowControl scriptLoadDeff(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final int type = script.params_20[4].get();
    if(type == 0x100_0000) {
      loadDragoonDeff(script);
    } else if(type == 0x200_0000) {
      loadSpellItemDeff(script);
    } else if(type == 0x300_0000 || type == 0x400_0000) {
      loadEnemyOrBossDeff(script);
    } else if(type == 0x500_0000) {
      loadCutsceneDeff(script);
    }

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = _800c6938.managerState_18.innerStruct_00;
    manager.ticker_48 = Bttl_800e::FUN_800e74e0;

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the current DEFF loading stage")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "loadingStage")
  @Method(0x800e7490L)
  public static FlowControl scriptGetDeffLoadingStage(final RunningScript<?> script) {
    script.params_20[0].set(deffLoadingStage_800fafe8.get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Returns two unknown values")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "bentIndex")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800e74acL)
  public static FlowControl FUN_800e74ac(final RunningScript<?> script) {
    final BattleStruct24_2 struct24 = _800c6938;
    script.params_20[0].set(struct24.bentState_04.index);
    script.params_20[1].set(struct24._08);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e74e0L)
  public static void FUN_800e74e0(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> data) {
    final BattleStruct24_2 struct24 = _800c6938;

    final int deffStage = deffLoadingStage_800fafe8.get();
    if(deffStage == 1) {
      //LAB_800e7510
      if(struct24.init_1c && struct24.script_14 != null && ((deffManager_800c693c.flags_20 & 0x4_0000) == 0 || (getLoadedDrgnFiles() & 0x40) == 0)) {
        //LAB_800e756c
        deffLoadingStage_800fafe8.set(2);
      }
    } else if(deffStage == 3) {
      //LAB_800e7574
      if(struct24.frameCount_20 >= 0) {
        struct24.frameCount_20 += vsyncMode_8007a3b8;
      }
    }

    //LAB_800e759c
  }

  /** Used in Astral Drain (ground glow) */
  @Method(0x800e75acL)
  public static void FUN_800e75ac(final GenericSpriteEffect24 spriteEffect, final MV transformMatrix) {
    final MV finalTransform = new MV();
    transformMatrix.compose(worldToScreenMatrix_800c3548, finalTransform);
    final float z = Math.min(0x3ff8, zOffset_1f8003e8.get() + finalTransform.transfer.z / 4.0f);

    if(z >= 40) {
      //LAB_800e7610
      GTE.setTransforms(finalTransform);

      GTE.setVertex(0, spriteEffect.x_04 * 64, spriteEffect.y_06 * 64, 0);
      GTE.setVertex(1, (spriteEffect.x_04 + spriteEffect.w_08) * 64, spriteEffect.y_06 * 64, 0);
      GTE.setVertex(2, spriteEffect.x_04 * 64, (spriteEffect.y_06 + spriteEffect.h_0a) * 64, 0);
      GTE.perspectiveTransformTriangle();
      final float sx0 = GTE.getScreenX(0);
      final float sy0 = GTE.getScreenY(0);
      final float sx1 = GTE.getScreenX(1);
      final float sy1 = GTE.getScreenY(1);
      final float sx2 = GTE.getScreenX(2);
      final float sy2 = GTE.getScreenY(2);

      GTE.perspectiveTransform((spriteEffect.x_04 + spriteEffect.w_08) * 64, (spriteEffect.y_06 + spriteEffect.h_0a) * 64, 0);
      final float sx3 = GTE.getScreenX(2);
      final float sy3 = GTE.getScreenY(2);

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .clut(spriteEffect.clutX_10, spriteEffect.clutY_12)
        .vramPos((spriteEffect.tpage_0c & 0b1111) * 64, (spriteEffect.tpage_0c & 0b10000) != 0 ? 256 : 0)
        .rgb(spriteEffect.r_14, spriteEffect.g_15, spriteEffect.b_16)
        .pos(0, sx0, sy0)
        .pos(1, sx1, sy1)
        .pos(2, sx2, sy2)
        .pos(3, sx3, sy3)
        .uv(0, spriteEffect.u_0e, spriteEffect.v_0f)
        .uv(1, spriteEffect.u_0e + spriteEffect.w_08, spriteEffect.v_0f)
        .uv(2, spriteEffect.u_0e, spriteEffect.v_0f + spriteEffect.h_0a)
        .uv(3, spriteEffect.u_0e + spriteEffect.w_08, spriteEffect.v_0f + spriteEffect.h_0a);

      if((spriteEffect.flags_00 >>> 30 & 1) != 0) {
        cmd.translucent(Translucency.of((int)spriteEffect.flags_00 >>> 28 & 0b11));
      }

      GPU.queueCommand(z / 4.0f, cmd);
    }
    //LAB_800e7930
  }

  /**
   * Renderer for some kind of effect sprites like those in HUD DEFF.
   * Used for example for sprite effect overlays on red glow in Death Dimension.
   */
  @Method(0x800e7944L)
  public static void FUN_800e7944(final GenericSpriteEffect24 spriteEffect, final Vector3f translation, final int zMod) {
    if((int)spriteEffect.flags_00 >= 0) { // No errors
      final Vector3f finalTranslation = new Vector3f();
      translation.mul(worldToScreenMatrix_800c3548, finalTranslation);
      finalTranslation.add(worldToScreenMatrix_800c3548.transfer);

      final float x0 = MathHelper.safeDiv(finalTranslation.x * projectionPlaneDistance_1f8003f8, finalTranslation.z);
      final float y0 = MathHelper.safeDiv(finalTranslation.y * projectionPlaneDistance_1f8003f8, finalTranslation.z);

      // zMod needs to be ignored in z check or poly positions will overflow at low z values
      float z = zMod + finalTranslation.z / 4.0f;
      if(finalTranslation.z / 4.0f >= 40 && z >= 40) {
        if(z > 0x3ff8) {
          z = 0x3ff8;
        }

        //LAB_800e7a38
        final float zDepth = MathHelper.safeDiv(projectionPlaneDistance_1f8003f8 * 0x1000 / 4.0f, finalTranslation.z / 4.0f);
        final float x1 = spriteEffect.x_04 * spriteEffect.scaleX_1c / 8 * zDepth / 8;
        final float x2 = x1 + spriteEffect.w_08 * spriteEffect.scaleX_1c / 8 * zDepth / 8;
        final float y1 = spriteEffect.y_06 * spriteEffect.scaleY_1e / 8 * zDepth / 8;
        final float y2 = y1 + spriteEffect.h_0a * spriteEffect.scaleY_1e / 8 * zDepth / 8;
        final float sin = MathHelper.sin(spriteEffect.angle_20);
        final float cos = MathHelper.cos(spriteEffect.angle_20);

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .clut(spriteEffect.clutX_10, spriteEffect.clutY_12)
          .vramPos((spriteEffect.tpage_0c & 0b1111) * 64, (spriteEffect.tpage_0c & 0b10000) != 0 ? 256 : 0)
          .rgb(spriteEffect.r_14, spriteEffect.g_15, spriteEffect.b_16)
          .pos(0, x0 + x1 * cos - y1 * sin, y0 + x1 * sin + y1 * cos)
          .pos(1, x0 + x2 * cos - y1 * sin, y0 + x2 * sin + y1 * cos)
          .pos(2, x0 + x1 * cos - y2 * sin, y0 + x1 * sin + y2 * cos)
          .pos(3, x0 + x2 * cos - y2 * sin, y0 + x2 * sin + y2 * cos)
          .uv(0, spriteEffect.u_0e, spriteEffect.v_0f)
          .uv(1, spriteEffect.w_08 + spriteEffect.u_0e - 1, spriteEffect.v_0f)
          .uv(2, spriteEffect.u_0e, spriteEffect.h_0a + spriteEffect.v_0f - 1)
          .uv(3, spriteEffect.w_08 + spriteEffect.u_0e - 1, spriteEffect.h_0a + spriteEffect.v_0f - 1);

        if((spriteEffect.flags_00 & 0x4000_0000) != 0) {
          cmd.translucent(Translucency.of((int)spriteEffect.flags_00 >>> 28 & 0b11));
        }

        GPU.queueCommand(z / 4.0f, cmd);
      }
    }

    //LAB_800e7d8c
  }

  @Method(0x800e7dbcL)
  public static float transformToScreenSpace(final Vector2f out, final Vector3f translation) {
    final Vector3f transformed = new Vector3f();
    translation.mul(worldToScreenMatrix_800c3548, transformed);
    transformed.add(worldToScreenMatrix_800c3548.transfer);

    if(transformed.z >= 160) {
      out.x = transformed.x * projectionPlaneDistance_1f8003f8 / transformed.z;
      out.y = transformed.y * projectionPlaneDistance_1f8003f8 / transformed.z;
      return transformed.z / 4.0f;
    }

    //LAB_800e7e8c
    //LAB_800e7e90
    return 0;
  }

  @Method(0x800e7ea4L)
  public static void renderGenericSpriteAtZOffset0(final GenericSpriteEffect24 spriteEffect, final Vector3f translation) {
    FUN_800e7944(spriteEffect, translation, 0);
  }

  @Method(0x800e7ec4L)
  public static <T extends EffectManagerData6cInner<T>> void effectManagerDestructor(final ScriptState<EffectManagerData6c<T>> state, final EffectManagerData6c<T> struct) {
    LOGGER.info(EFFECTS, "Deallocating effect manager %d", state.index);

    if(struct.parentScript_50 != null) {
      if(struct.newChildScript_56 != null) {
        struct.newChildScript_56.innerStruct_00.oldChildScript_54 = struct.oldChildScript_54;
      } else {
        //LAB_800e7f4c
        struct.parentScript_50.innerStruct_00.childScript_52 = struct.oldChildScript_54;
      }

      //LAB_800e7f6c
      if(struct.oldChildScript_54 != null) {
        struct.oldChildScript_54.innerStruct_00.newChildScript_56 = struct.newChildScript_56;
      }

      //LAB_800e7fa0
      struct.parentScript_50 = null;
      struct.oldChildScript_54 = null;
      struct.newChildScript_56 = null;
    }

    //LAB_800e7fac
    //LAB_800e7fcc
    while(struct.childScript_52 != null) {
      EffectManagerData6c<?> child = struct.childScript_52.innerStruct_00;

      //LAB_800e7ff8
      while(child.childScript_52 != null) {
        child = child.childScript_52.innerStruct_00;
      }

      //LAB_800e8020
      child.myScriptState_0e.deallocateWithChildren();
    }

    //LAB_800e8040
    if(struct.destructor_4c != null) {
      struct.destructor_4c.accept(state, struct);
    }
  }

  public static ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> allocateEffectManager(final String name, @Nullable final ScriptState<? extends BattleObject> parentState, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>>, EffectManagerData6c<EffectManagerData6cInner.VoidType>> ticker, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>>, EffectManagerData6c<EffectManagerData6cInner.VoidType>> renderer, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>>, EffectManagerData6c<EffectManagerData6cInner.VoidType>> destructor, @Nullable final Effect effect) {
    return allocateEffectManager(name, parentState, ticker, renderer, destructor, effect, new EffectManagerData6cInner.VoidType());
  }

  @Method(0x800e80c4L)
  public static <T extends EffectManagerData6cInner<T>> ScriptState<EffectManagerData6c<T>> allocateEffectManager(final String name, @Nullable ScriptState<? extends BattleObject> parentState, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> ticker, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> renderer, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> destructor, @Nullable final Effect effect, final T inner) {
    final ScriptState<EffectManagerData6c<T>> state = SCRIPTS.allocateScriptState(name, new EffectManagerData6c<>(name, inner));
    final EffectManagerData6c<T> manager = state.innerStruct_00;

    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(Bttl_800e::effectManagerTicker);

    if(renderer != null) {
      state.setRenderer(renderer);
    }

    state.setDestructor(Bttl_800e::effectManagerDestructor);

    final StackWalker.StackFrame caller = DebugHelper.getCallerFrame();

    manager.effect_44 = effect;

    if(effect != null) {
      LOGGER.info(EFFECTS, "Allocating effect manager %d for %s (parent: %d) from %s.%s(%s:%d)", state.index, manager.effect_44.getClass().getSimpleName(), parentState != null ? parentState.index : -1, caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());
    } else {
      LOGGER.info(EFFECTS, "Allocating empty effect manager %d (parent: %d) from %s.%s(%s:%d)", state.index, parentState != null ? parentState.index : -1, caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());
    }

    manager.magic_00 = BattleObject.EM__;
    manager.flags_04 = 0xff00_0000;
    manager.scriptIndex_0c = -1;
    manager.coord2Index_0d = -1;
    manager.myScriptState_0e = state;
    manager._10.flags_00 = 0x5400_0000;
    manager._10.scale_16.set(1.0f, 1.0f, 1.0f);
    manager._10.colour_1c.set(0x80, 0x80, 0x80);
    manager.ticker_48 = ticker;
    manager.destructor_4c = destructor;

    if(parentState != null) {
      if(!BattleObject.EM__.equals(parentState.innerStruct_00.magic_00)) {
        parentState = deffManager_800c693c.scriptState_1c;
      }

      final EffectManagerData6c<?> parent = (EffectManagerData6c<?>)parentState.innerStruct_00;
      final EffectManagerData6c<?> child = state.innerStruct_00;

      child.parentScript_50 = (ScriptState<EffectManagerData6c<?>>)parentState;
      if(parent.childScript_52 != null) {
        child.oldChildScript_54 = parent.childScript_52;
        parent.childScript_52.innerStruct_00.newChildScript_56 = (ScriptState)state;
      }

      parent.childScript_52 = (ScriptState)state;
    }

    return state;
  }

  /** Considers all parents */
  @Method(0x800e8594L)
  public static void calculateEffectTransforms(final MV transformMatrix, final EffectManagerData6c<?> manager) {
    transformMatrix.rotationXYZ(manager._10.rot_10);
    transformMatrix.transfer.set(manager._10.trans_04);
    transformMatrix.scaleLocal(manager._10.scale_16);

    EffectManagerData6c<?> currentManager = manager;
    int scriptIndex = manager.scriptIndex_0c;

    //LAB_800e8604
    while(scriptIndex >= 0) {
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0[scriptIndex];
      if(state == null) { // error, parent no longer exists
        manager._10.flags_00 |= 0x8000_0000;
        transformMatrix.transfer.z = -0x7fff;
        scriptIndex = -2;
        break;
      }

      final BattleObject base = (BattleObject)state.innerStruct_00;
      if(BattleObject.EM__.equals(base.magic_00)) {
        final EffectManagerData6c<?> baseManager = (EffectManagerData6c<?>)base;
        final MV baseTransformMatrix = new MV();
        baseTransformMatrix.rotationXYZ(baseManager._10.rot_10);
        baseTransformMatrix.transfer.set(baseManager._10.trans_04);
        baseTransformMatrix.scaleLocal(baseManager._10.scale_16);

        if(currentManager.coord2Index_0d != -1) {
          //LAB_800e866c
          FUN_800ea0f4(baseManager, currentManager.coord2Index_0d).coord.compose(baseTransformMatrix, baseTransformMatrix);
        }

        //LAB_800e86ac
        transformMatrix.compose(baseTransformMatrix);
        currentManager = baseManager;
        scriptIndex = currentManager.scriptIndex_0c;
        //LAB_800e86c8
      } else if(BattleObject.BOBJ.equals(base.magic_00)) {
        final BattleEntity27c bent = (BattleEntity27c)base;
        final Model124 s1 = bent.model_148;
        applyModelRotationAndScale(s1);
        final int coord2Index = currentManager.coord2Index_0d;

        final MV sp0x10 = new MV();
        if(coord2Index == -1) {
          sp0x10.set(s1.coord2_14.coord);
        } else {
          //LAB_800e8738
          GsGetLw(s1.modelParts_00[coord2Index].coord2_04, sp0x10);
          s1.modelParts_00[coord2Index].coord2_04.flg = 0;
        }

        //LAB_800e8774
        transformMatrix.compose(sp0x10);
        currentManager = null;
        scriptIndex = -1; // finished
      } else { // error, parent not a bent or effect
        //LAB_800e878c
        //LAB_800e8790
        manager._10.flags_00 |= 0x8000_0000;
        transformMatrix.transfer.z = -0x7fff;
        scriptIndex = -2;
        break;
      }
    }

    //LAB_800e87b4
    if(scriptIndex == -2) { // error
      final MV transposedWs = new MV();
      final Vector3f transposedTranslation = new Vector3f();
      worldToScreenMatrix_800c3548.transpose(transposedWs);
      transposedTranslation.set(worldToScreenMatrix_800c3548.transfer).negate();
      transposedTranslation.mul(transposedWs, transposedWs.transfer);
      transformMatrix.compose(transposedWs);
    }
    //LAB_800e8814
  }

  @Method(0x800e8e9cL)
  public static <T extends EffectManagerData6cInner<T>> void effectManagerTicker(final ScriptState<EffectManagerData6c<T>> state, final EffectManagerData6c<T> data) {
    AttachmentHost subPtr = data;

    //LAB_800e8ee0
    while(subPtr.getAttachment() != null) {
      final EffectAttachment sub = subPtr.getAttachment();

      final int ret = (int)((BiFunction)sub.ticker_08).apply(data, subPtr.getAttachment());
      if(ret == 0) { // Remove this attachment
        //LAB_800e8f2c
        data.flags_04 &= ~(1 << sub.id_05);
        subPtr.setAttachment(sub.getAttachment());
      } else if(ret == 1) { // Continue
        //LAB_800e8f6c
        subPtr = sub;
        //LAB_800e8f1c
      } else if(ret == 2) { // Remove this effect entirely
        //LAB_800e8f78
        state.deallocateWithChildren();
        return;
      }

      //LAB_800e8f8c
    }

    //LAB_800e8f9c
    if(data.ticker_48 != null) {
      data.ticker_48.accept(state, data);
    }

    //LAB_800e8fb8
  }

  @Method(0x800e8ffcL)
  public static void allocateDeffManager() {
    final DeffManager7cc deffManager = new DeffManager7cc();
    _800c6938 = deffManager._5b8;
    _800c6930 = deffManager._5dc;
    lights_800c692c = deffManager._640;
    deffManager.flags_20 = 0x4;
    tmds_800c6944 = deffManager.tmds_2f8;
    deffManager_800c693c = deffManager;
    spriteMetrics_800c6948 = deffManager.spriteMetrics_39c;
    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> manager = allocateEffectManager("DEFF manager", null, null, null, null, null);
    manager.innerStruct_00.flags_04 = 0x600_0400;
    deffManager.scriptState_1c = manager;
    allocateLighting();
    loadSupportOverlay(1, SBtld::loadStageAmbiance);
  }

  @Method(0x800e9120L)
  public static void deallocateLightingControllerAndDeffManager() {
    scriptStatePtrArr_800bc1c0[1].deallocateWithChildren();
    deallocateDeffManagerScriptsArray();
    deffManager_800c693c.scriptState_1c.deallocateWithChildren();
    deffManager_800c693c = null;
  }

  @Method(0x800e9178L)
  public static void FUN_800e9178(final int mode) {
    if(mode == 1) {
      //LAB_800e91a0
      deffManager_800c693c.scriptState_1c.innerStruct_00.removeAttachment(10);
    } else if(mode == 2) {
      //LAB_800e91d8
      deffManager_800c693c.scriptState_1c.innerStruct_00.removeAttachment(10);
      deallocateDeffManagerScriptsArray();
    } else {
      // This seems to be destroying and the re-creating the DEFF manager script state? Must be for ending the DEFF or something?

      //LAB_800e9214
      deallocateDeffManagerScriptsArray();
      deffManager_800c693c.scriptState_1c.deallocateWithChildren();
      final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> manager = allocateEffectManager("DEFF manager (but different)", null, null, null, null, null);
      deffManager_800c693c.scriptState_1c = manager;
      manager.innerStruct_00.flags_04 = 0x600_0400;
    }

    //LAB_800e9278
  }

  @Method(0x800e929cL)
  public static void uploadTims(final List<FileData> files) {
    LOGGER.info(DEFF, "Loading DEFF TIMs");

    //LAB_800e92d4
    for(final FileData file : files) {
      if(file.hasVirtualSize()) {
        new Tim(file).uploadToGpu();
      }
    }

    //LAB_800e9354
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800e93e0L)
  public static FlowControl scriptAllocateEmptyEffectManagerChild(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Empty EffectManager child, allocated by script %d (%s) from FUN_800e93e0".formatted(script.scriptState_04.index, script.scriptState_04.name), script.scriptState_04, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  /** Has some relation to rendering of certain effect sprites, like ones from HUD DEFF */
  @Method(0x800e9428L)
  public static void renderBillboardSpriteEffect_(final SpriteMetrics08 metrics, final EffectManagerData6cInner<?> managerInner, final MV transformMatrix) {
    if(managerInner.flags_00 >= 0) { // No errors
      final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24();
      spriteEffect.flags_00 = managerInner.flags_00 & 0xffff_ffffL;
      spriteEffect.x_04 = (short)(-metrics.w_04 / 2);
      spriteEffect.y_06 = (short)(-metrics.h_05 / 2);
      spriteEffect.w_08 = metrics.w_04;
      spriteEffect.h_0a = metrics.h_05;
      spriteEffect.tpage_0c = (metrics.v_02 & 0x100) >>> 4 | (metrics.u_00 & 0x3ff) >>> 6;
      spriteEffect.u_0e = (metrics.u_00 & 0x3f) * 4;
      spriteEffect.v_0f = metrics.v_02;
      spriteEffect.clutX_10 = metrics.clut_06 << 4 & 0x3ff;
      spriteEffect.clutY_12 = metrics.clut_06 >>> 6 & 0x1ff;
      spriteEffect.r_14 = managerInner.colour_1c.x & 0xff;
      spriteEffect.g_15 = managerInner.colour_1c.y & 0xff;
      spriteEffect.b_16 = managerInner.colour_1c.z & 0xff;
      spriteEffect.scaleX_1c = managerInner.scale_16.x;
      spriteEffect.scaleY_1e = managerInner.scale_16.y;
      spriteEffect.angle_20 = managerInner.rot_10.z;

      if((managerInner.flags_00 & 0x400_0000) != 0) {
        zOffset_1f8003e8.set(managerInner.z_22);
        FUN_800e75ac(spriteEffect, transformMatrix);
      } else {
        //LAB_800e9574
        FUN_800e7944(spriteEffect, transformMatrix.transfer, managerInner.z_22);
      }
    }
    //LAB_800e9580
  }

  @Method(0x800e9590L)
  public static void renderBillboardSpriteEffect(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager) {
    final MV transformMatrix = new MV();
    calculateEffectTransforms(transformMatrix, manager);
    renderBillboardSpriteEffect_(((BillboardSpriteEffect0c)manager.effect_44).metrics_04, manager._10, transformMatrix);
  }

  @Method(0x800e95f0L)
  public static void getSpriteMetricsFromSource(final BillboardSpriteEffect0c spriteEffect, final int flag) {
    spriteEffect.flags_00 = flag | 0x400_0000;

    if((flag & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[flag & 0xff];
      spriteEffect.metrics_04.u_00 = metrics.u_00;
      spriteEffect.metrics_04.v_02 = metrics.v_02;
      spriteEffect.metrics_04.w_04 = metrics.w_04;
      spriteEffect.metrics_04.h_05 = metrics.h_05;
      spriteEffect.metrics_04.clut_06 = metrics.clut_06;
    } else {
      //LAB_800e9658
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(flag | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      spriteEffect.metrics_04.u_00 = deffMetrics.u_00;
      spriteEffect.metrics_04.v_02 = deffMetrics.v_02;
      spriteEffect.metrics_04.w_04 = deffMetrics.w_04 * 4;
      spriteEffect.metrics_04.h_05 = deffMetrics.h_06;
      spriteEffect.metrics_04.clut_06 = deffMetrics.clutY_0a << 6 | (deffMetrics.clutX_08 & 0x3f0) >>> 4;
    }
    //LAB_800e96bc
  }

  @ScriptDescription("Allocates a new billboard sprite effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "Flag meanings are unknown")
  @Method(0x800e96ccL)
  public static FlowControl allocateBillboardSpriteEffect(final RunningScript<? extends BattleObject> script) {
    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state = allocateEffectManager(
      "BillboardSpriteEffect0c",
      script.scriptState_04,
      null,
      Bttl_800e::renderBillboardSpriteEffect,
      null,
      new BillboardSpriteEffect0c()
    );

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = state.innerStruct_00;
    manager.flags_04 = 0x400_0000;
    getSpriteMetricsFromSource((BillboardSpriteEffect0c)manager.effect_44, script.params_20[1].get());
    manager._10.flags_00 = manager._10.flags_00 & 0xfbff_ffff | 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets shadow type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode")
  @Method(0x800e9798L)
  public static FlowControl FUN_800e9798(final RunningScript<?> script) {
    final BattleObject bobj = (BattleObject)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final Model124 model;
    if(BattleObject.EM__.equals(bobj.magic_00)) {
      model = ((ModelEffect13c)((EffectManagerData6c<?>)bobj).effect_44).model_134;
    } else {
      model = ((BattleEntity27c)bobj).model_148;
    }

    //LAB_800e97e8
    //LAB_800e97ec
    final int a0 = script.params_20[1].get();
    if(a0 == -1) {
      model.shadowType_cc = 2;
      model.modelPartWithShadowIndex_cd = -1;
    } else if(a0 == -2) {
      //LAB_800e982c
      model.shadowType_cc = 3;
      //LAB_800e980c
    } else if(a0 == -3) {
      //LAB_800e983c
      model.shadowType_cc = 0;
    } else {
      //LAB_800e9844
      //LAB_800e9848
      model.shadowType_cc = 3;
      model.modelPartWithShadowIndex_cd = a0;
    }

    //LAB_800e984c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an unknown model effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0", description = "Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800e9854L)
  public static FlowControl FUN_800e9854(final RunningScript<? extends BattleObject> script) {
    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x200_0000);

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.AnimType>> state = allocateEffectManager(
      animatedTmdType.name,
      script.scriptState_04,
      Bttl_800e::FUN_800ea3f8,
      Bttl_800e::FUN_800ea510,
      null,
      new ModelEffect13c("Script " + script.scriptState_04.index),
      new EffectManagerData6cInner.AnimType()
    );

    final EffectManagerData6c<EffectManagerData6cInner.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x200_0000;

    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;
    effect._00 = 0;
    effect.tmdType_04 = animatedTmdType;
    effect.extTmd_08 = animatedTmdType.tmd_0c;
    effect.anim_0c = animatedTmdType.anim_14;
    effect.model_134 = effect.model_10;
    final Model124 model = effect.model_134;

    // Retail bug? Trying to read textureInfo from a DEFF container that doesn't have it
    if(animatedTmdType.textureInfo_08 != null) {
      final DeffPart.TextureInfo textureInfo = animatedTmdType.textureInfo_08[0];
      final int tpage = GetTPage(Bpp.BITS_4, Translucency.HALF_B_PLUS_HALF_F, textureInfo.vramPos_00.x.get(), textureInfo.vramPos_00.y.get());
      model.colourMap_9d = modelColourMaps_800fb06c.get(tpage).get();
    } else {
      model.colourMap_9d = 0;
    }

    loadModelTmd(model, effect.extTmd_08);
    loadModelAnim(model, effect.anim_0c);
    addGenericAttachment(manager, 0, 0x100, 0);
    manager._10.flags_00 = 0x1400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an unknown model effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "The DEFF flags, mostly unknown")
  @Method(0x800e99bcL)
  public static FlowControl FUN_800e99bc(final RunningScript<? extends BattleObject> script) {
    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x100_0000);

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.AnimType>> state = allocateEffectManager(
      animatedTmdType.name,
      script.scriptState_04,
      Bttl_800e::FUN_800ea3f8,
      Bttl_800e::FUN_800ea510,
      null,
      new ModelEffect13c("Script " + script.scriptState_04.index),
      new EffectManagerData6cInner.AnimType()
    );

    final EffectManagerData6c<EffectManagerData6cInner.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x100_0000;
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;
    effect._00 = 0;

    effect.tmdType_04 = animatedTmdType;
    effect.extTmd_08 = animatedTmdType.tmd_0c;
    effect.anim_0c = animatedTmdType.anim_14;
    effect.model_10.colourMap_9d = 0;
    effect.model_134 = effect.model_10;
    loadModelTmd(effect.model_134, effect.extTmd_08);
    loadModelAnim(effect.model_134, effect.anim_0c);
    addGenericAttachment(manager, 0, 0x100, 0);
    manager._10.flags_00 = 0x5400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e9ae4L)
  public static void FUN_800e9ae4(final Model124 model, final BattleStage a1) {
    model.coord2_14.set(a1.coord2_558);
    model.coord2_14.transforms.set(a1.param_5a8);

    model.animType_90 = -1;
    model.partTransforms_90 = a1.rotTrans_5d4;
    model.partTransforms_94 = a1.rotTrans_5d8;
    model.partCount_98 = a1.partCount_5dc;
    model.totalFrames_9a = a1.totalFrames_5de;
    model.animationState_9c = a1.animationState_5e0;
    model.colourMap_9d = 0;
    model.zOffset_a0 = 0x200;
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.smallerStructPtr_a4 = null;
    model.remainingFrames_9e = a1.remainingFrames_5e2;
    model.interpolationFrameIndex = 0;
    model.ptr_a8 = a1._5ec;

    //LAB_800e9c0c
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    model.partInvisible_f4 = a1.flags_5e4;
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.tpage_108 = 0;
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.zero();
    model.shadowType_cc = 0;
    model.modelPartWithShadowIndex_cd = 0;

    model.modelParts_00 = new ModelPart10[a1.dobj2s_00.length];
    Arrays.setAll(model.modelParts_00, i -> new ModelPart10().set(a1.dobj2s_00[i]));

    //LAB_800e9d34
    for(final ModelPart10 dobj2 : model.modelParts_00) {
      dobj2.coord2_04 = new GsCOORDINATE2();
      dobj2.coord2_04.super_ = model.coord2_14;
    }
  }

  @Method(0x800e9db4L)
  public static void FUN_800e9db4(final Model124 model1, final Model124 model2) {
    //LAB_800e9dd8
    model1.set(model2);

    model1.modelParts_00 = new ModelPart10[model1.modelParts_00.length];
    Arrays.setAll(model1.modelParts_00, i -> new ModelPart10().set(model2.modelParts_00[i]));

    //LAB_800e9ee8
    for(final ModelPart10 dobj2 : model1.modelParts_00) {
      dobj2.coord2_04 = new GsCOORDINATE2();
      dobj2.coord2_04.super_ = model1.coord2_14;
    }
  }

  @ScriptDescription("Allocates an effect manager for an unknown purpose")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager's index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800e9f68L)
  public static FlowControl FUN_800e9f68(final RunningScript<? extends BattleObject> script) {
    final int s2 = script.params_20[1].get();
    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.AnimType>> state = allocateEffectManager(
      "Unknown (FUN_800e9f68, s2 = 0x%x)".formatted(s2),
      script.scriptState_04,
      Bttl_800e::FUN_800ea3f8,
      Bttl_800e::FUN_800ea510,
      null,
      new ModelEffect13c("Script " + script.scriptState_04.index),
      new EffectManagerData6cInner.AnimType()
    );

    final EffectManagerData6c<EffectManagerData6cInner.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x200_0000;

    final ModelEffect13c s0 = (ModelEffect13c)manager.effect_44;
    s0._00 = 0;
    s0.tmdType_04 = null;
    s0.extTmd_08 = null;
    s0.anim_0c = null;
    s0.model_134 = s0.model_10;

    if((s2 & 0xff00_0000) == 0x700_0000) {
      FUN_800e9ae4(s0.model_10, battlePreloadedEntities_1f8003f4.stage_963c);
    } else {
      //LAB_800ea030
      FUN_800e9db4(s0.model_10, ((BattleEntity27c)scriptStatePtrArr_800bc1c0[s2].innerStruct_00).model_148);
    }

    //LAB_800ea04c
    final Model124 model = s0.model_134;
    manager._10.trans_04.set(model.coord2_14.coord.transfer);
    manager._10.rot_10.set(model.coord2_14.transforms.rotate);
    manager._10.scale_16.set(model.coord2_14.transforms.scale);
    manager._10.flags_00 = 0x1400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea0f4L)
  public static GsCOORDINATE2 FUN_800ea0f4(final EffectManagerData6c<?> effectManager, final int coord2Index) {
    final Model124 model = ((ModelEffect13c)effectManager.effect_44).model_10;
    applyModelRotationAndScale(model);
    return model.modelParts_00[coord2Index].coord2_04;
  }

  @ScriptDescription("Hides an effect's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "part", description = "Which model part to hide")
  @Method(0x800ea13cL)
  public static FlowControl scriptHideEffectModelPart(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final Model124 model = ((ModelEffect13c)manager.effect_44).model_134;
    final int flag = script.params_20[1].get() & 0xffff;

    final int index = flag >>> 5;
    final int shift = flag & 0x1f;

    model.partInvisible_f4 |= 0x1L << shift + index * 32;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Shows an effect's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "part", description = "Which model part to show")
  @Method(0x800ea19cL)
  public static FlowControl scriptShowEffectModelPart(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final Model124 model = ((ModelEffect13c)manager.effect_44).model_134;
    final int flag = script.params_20[1].get() & 0xffff;

    final int index = flag >>> 5;
    final int shift = flag & 0x1f;

    model.partInvisible_f4 &= ~(0x1L << shift + index * 32);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads a CMB animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "The DEFF flags, mostly unknown")
  @Method(0x800ea200L)
  public static FlowControl scriptLoadCmbAnimation(final RunningScript<?> script) {
    final EffectManagerData6c<EffectManagerData6cInner.AnimType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerData6cInner.AnimType.class));
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;

    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x200_0000);
    final Anim cmb = animatedTmdType.anim_14;
    effect.anim_0c = cmb;
    loadModelAnim(effect.model_134, cmb);
    manager._10.ticks_24 = 0;
    addGenericAttachment(manager, 0, 0x100, 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the shadow size for a battle object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z size (12-bit fixed-point)")
  @Method(0x800ea2a0L)
  public static FlowControl scriptSetBttlShadowSize(final RunningScript<?> script) {
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);

    final Model124 model;
    if(BattleObject.EM__.equals(bobj.magic_00)) {
      model = ((ModelEffect13c)((EffectManagerData6c<?>)bobj).effect_44).model_134;
    } else {
      //LAB_800ea2f8
      model = ((BattleEntity27c)bobj).model_148;
    }

    //LAB_800ea300
    model.shadowSize_10c.x = script.params_20[1].get() / (float)0x1000;
    model.shadowSize_10c.z = script.params_20[2].get() / (float)0x1000;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the shadow offset for a battle object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset")
  @Method(0x800ea30cL)
  public static FlowControl scriptSetBttlShadowOffset(final RunningScript<?> script) {
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);

    final Model124 model;
    if(BattleObject.EM__.equals(bobj.magic_00)) {
      model = ((ModelEffect13c)((EffectManagerData6c<?>)bobj).effect_44).model_134;
    } else {
      //LAB_800ea36c
      model = ((BattleEntity27c)bobj).model_148;
    }

    //LAB_800ea374
    model.shadowOffset_118.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the number of times an effect's animation has looped")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "loopCount", description = "The number of loops")
  @Method(0x800ea384L)
  public static FlowControl scriptGetEffectLoopCount(final RunningScript<?> script) {
    final EffectManagerData6c<EffectManagerData6cInner.AnimType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerData6cInner.AnimType.class));
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;

    if(effect.anim_0c == null) {
      script.params_20[1].set(0);
    } else {
      //LAB_800ea3cc
      script.params_20[1].set((manager._10.ticks_24 + 2) / effect.model_134.totalFrames_9a);
    }

    //LAB_800ea3e4
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea3f8L)
  public static void FUN_800ea3f8(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.AnimType>> state, final EffectManagerData6c<EffectManagerData6cInner.AnimType> manager) {
    final MV sp0x10 = new MV();
    calculateEffectTransforms(sp0x10, manager);

    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;
    final Model124 model = effect.model_134;
    model.coord2_14.transforms.rotate.set(manager._10.rot_10);
    model.coord2_14.transforms.scale.set(manager._10.scale_16);
    model.zOffset_a0 = manager._10.z_22;
    model.coord2_14.coord.set(sp0x10);
    model.coord2_14.flg = 0;

    if(effect.anim_0c != null) {
      applyAnimation(model, manager._10.ticks_24);
    }

    //LAB_800ea4fc
  }

  @Method(0x800ea510L)
  public static void FUN_800ea510(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.AnimType>> state, final EffectManagerData6c<EffectManagerData6cInner.AnimType> manager) {
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;
    if(manager._10.flags_00 >= 0) {
      if((manager._10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager._10.colour_1c.x / 128.0f, manager._10.colour_1c.y / 128.0f, manager._10.colour_1c.z / 128.0f);
      } else {
        //LAB_800ea564
        FUN_800e60e0(1.0f, 1.0f, 1.0f);
      }

      //LAB_800ea574
      final Model124 model = effect.model_134;

      final int oldTpage = model.tpage_108;

      if((manager._10.flags_00 & 0x4000_0000L) != 0) {
        model.tpage_108 = manager._10.flags_00 >>> 23 & 0x60;
      }

      //LAB_800ea598
      FUN_800dd89c(model, manager._10.flags_00);

      model.tpage_108 = oldTpage;

      if((manager._10.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      } else {
        //LAB_800ea5d4
        FUN_800e6170();
      }
    }

    //LAB_800ea5dc
  }

  @Method(0x800ea620L)
  public static void prepareDeffFiles(final List<FileData> deff, final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> deffManagerState) {
    //LAB_800ea674
    for(int i = 0; i < deff.size(); i++) {
      final FileData data = deff.get(i);

      final int flags = data.readInt(0);
      final int type = flags & 0xff00_0000; // Flags
      if(type == 0x100_0000) {
        final DeffPart.TmdType tmdType = new DeffPart.TmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);
        final CContainer extTmd = tmdType.tmd_0c;
        final TmdWithId tmd = extTmd.tmdPtr_00;

        for(int objectIndex = 0; objectIndex < tmd.tmd.header.nobj; objectIndex++) {
          optimisePacketsIfNecessary(tmd, objectIndex);
        }

        if(tmdType.textureInfo_08 != null && deffManagerState.index != 0) {
          addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, extTmd, tmdType.textureInfo_08);
        }
      } else if(type == 0x200_0000) {
        final DeffPart.AnimatedTmdType animType = new DeffPart.AnimatedTmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);

        if(animType.textureInfo_08 != null && deffManagerState.index != 0) {
          addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, animType.tmd_0c, animType.textureInfo_08);
        }
      } else if(type == 0x300_0000) {
        final DeffPart.TmdType tmdType = new DeffPart.TmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);
        final CContainer extTmd = tmdType.tmd_0c;

        optimisePacketsIfNecessary(extTmd.tmdPtr_00, 0);

        if(tmdType.textureInfo_08 != null && deffManagerState.index != 0) {
          addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, extTmd, tmdType.textureInfo_08);
        }
      }

      //LAB_800ea778
      //LAB_800ea77c
    }

    //LAB_800ea790
  }

  @Method(0x800ea7d0L)
  public static void hudDeffLoaded(final List<FileData> files) {
    final DeffManager7cc struct7cc = deffManager_800c693c;
    prepareDeffFiles(files, struct7cc.scriptState_1c);

    //LAB_800ea814
    int i;
    for(i = 0; i < files.size(); i++) {
      final int flags = files.get(i).readInt(0); // Flags

      if((flags & 0xff00_0000) != 0) {
        break;
      }

      struct7cc.lmbs_390[flags & 0xff] = new DeffPart.LmbType(files.get(i));
    }

    //LAB_800ea850
    //LAB_800ea874
    for(; i < files.size(); i++) {
      if((files.get(i).readInt(0) & 0xff00_0000) != 0x100_0000) { // Flags
        break;
      }
    }

    //LAB_800ea89c
    //LAB_800ea8a8
    //LAB_800ea8e0
    for(; i < files.size(); i++) {
      final int flags = files.get(i).readInt(0);

      if((flags & 0xff00_0000) != 0x300_0000) {
        break;
      }

      final int index = flags & 0xff;
      if(index >= 5) {
        final DeffPart.TmdType tmdType = new DeffPart.TmdType("HUD DEFF file " + i, files.get(i));
        struct7cc.tmds_2f8[index] = tmdType.tmd_0c.tmdPtr_00.tmd.objTable[0];
      }

      //LAB_800ea928
    }

    //LAB_800ea93c
    //LAB_800ea964
    for(; i < files.size(); i++) {
      final int flags = files.get(i).readInt(0);

      if((flags & 0xff00_0000) != 0x400_0000) {
        break;
      }

      final DeffPart.SpriteType spriteType = new DeffPart.SpriteType(files.get(i));
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      final SpriteMetrics08 metrics = struct7cc.spriteMetrics_39c[flags & 0xff];
      metrics.u_00 = deffMetrics.u_00;
      metrics.v_02 = deffMetrics.v_02;
      metrics.w_04 = deffMetrics.w_04 * 4;
      metrics.h_05 = deffMetrics.h_06;
      metrics.clut_06 = deffMetrics.clutY_0a << 6 | (deffMetrics.clutX_08 & 0x3f0) >>> 4;
    }

    //LAB_800eaa00
    //LAB_800eaa04
  }

  @Method(0x800eab8cL)
  public static void deallocateDeffManagerScriptsArray() {
    deffManager_800c693c.scripts_2c = null;
  }

  /** See {@link DeffPart#flags_00} */
  @Method(0x800eac58L)
  public static DeffPart getDeffPart(final int flags) {
    //LAB_800eac84
    for(final DeffPart deffPart : deffManager_800c693c.deffPackage_5a8) {
      if(deffPart.flags_00 == flags) {
        return deffPart;
      }
      //LAB_800eaca0
    }

    //LAB_800eacac
    throw new IllegalArgumentException("Couldn't find DEFF with flags " + Long.toHexString(flags));
  }

  @Method(0x800eacf4L)
  public static void loadBattleHudDeff() {
    loadDrgnDir(0, "4114/2", Bttl_800e::hudDeffLoaded);
    loadDrgnDir(0, "4114/3", Bttl_800e::uploadTims);
    loadDrgnDir(0, "4114/1", files -> {
      deffManager_800c693c.scripts_2c = new ScriptFile[files.size()];

      for(int i = 0; i < files.size(); i++) {
        deffManager_800c693c.scripts_2c[i] = new ScriptFile("DRGN0.4114.1." + i, files.get(i).getBytes());
      }
    });
  }

  @Method(0x800ead44L)
  public static void applyTextureAnimation(final RECT rect, final int h) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, rect.x.get(), rect.y.get() + rect.h.get() - h, rect.w.get(), h));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x.get(), rect.y.get() + h, rect.x.get(), rect.y.get(), rect.w.get(), rect.h.get() - h));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x.get(), rect.y.get(), 960, 256, rect.w.get(), h));
  }

  @Method(0x800eaec8L)
  public static int tickTextureAnimationAttachment(final EffectManagerData6c<?> manager, final TextureAnimationAttachment1c anim) {
    //LAB_800eaef0
    anim.accumulator_14 += anim.step_18;

    //LAB_800eaf08
    int h = (anim.step_18 >> 8) % anim.rect_0c.h.get();

    if(h < 0) {
      h += anim.rect_0c.h.get();
    }

    //LAB_800eaf30
    if(h != 0) {
      applyTextureAnimation(anim.rect_0c, h);
    }

    //LAB_800eaf44
    return 1;
  }

  @Method(0x800eaf54L)
  public static TextureAnimationAttachment1c findTextureAnimationAttachment(EffectManagerData6c<?> manager, final RECT vramPos) {
    // Find manager with attachment 10
    //LAB_800eaf80
    while(!manager.hasAttachment(10)) {
      if(manager.parentScript_50 == null) {
        break;
      }

      manager = manager.parentScript_50.innerStruct_00;
    }

    //LAB_800eafb8
    TextureAnimationAttachment1c attachment = (TextureAnimationAttachment1c)manager.findAttachment(10);

    //LAB_800eafcc
    while(attachment != null) {
      if(attachment.rect_0c.x.get() == vramPos.x.get() && attachment.rect_0c.y.get() == vramPos.y.get()) {
        break;
      }

      //LAB_800eaff4
      attachment = (TextureAnimationAttachment1c)attachment.findAttachment(10);
    }

    //LAB_800eb00c
    return attachment;
  }

  @ScriptDescription("Removes a texture animation attachment")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex", description = "The texture index")
  @Method(0x800eb01cL)
  public static FlowControl scriptRemoveTextureAnimationAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04;
    final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08[script.params_20[1].get() * 2];

    EffectManagerData6c<?> managerWithTextureAnimationAttachment = manager;

    //LAB_800eb0c0
    while(!managerWithTextureAnimationAttachment.hasAttachment(10)) {
      final ScriptState<EffectManagerData6c<?>> parent = managerWithTextureAnimationAttachment.parentScript_50;

      if(parent == null) {
        break;
      }

      managerWithTextureAnimationAttachment = parent.innerStruct_00;
    }

    //LAB_800eb0f8
    AttachmentHost current = managerWithTextureAnimationAttachment;

    //LAB_800eb10c
    while(current.getAttachment() != null) {
      final EffectAttachment attachment = current.getAttachment();

      if(attachment.id_05 == 10 && attachment instanceof final TextureAnimationAttachment1c attachment10) {
        if(attachment10.rect_0c.x.get() == textureInfo.vramPos_00.x.get()) {
          if(attachment10.rect_0c.y.get() == textureInfo.vramPos_00.y.get()) {
            current.setAttachment(attachment.getAttachment());
            break;
          }
        }
      }

      //LAB_800eb15c
      current = attachment;
    }

    //LAB_800eb174
    //LAB_800eb178
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Applies a texture animation attachment update")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex", description = "The texture index")
  @Method(0x800eb188L)
  public static FlowControl scriptApplyTextureAnimationAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;

    final DeffPart.TmdType tmdType = effect.tmdType_04;
    final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08[script.params_20[1].get() * 2];
    final TextureAnimationAttachment1c attachment = findTextureAnimationAttachment(manager, textureInfo.vramPos_00);

    if(attachment != null) {
      int h = (-attachment.accumulator_14 >> 8) % attachment.rect_0c.h.get();

      if(h < 0) {
        h += attachment.rect_0c.h.get();
      }

      //LAB_800eb25c
      if(h != 0) {
        applyTextureAnimation(attachment.rect_0c, h);
      }
    }

    //LAB_800eb270
    return FlowControl.CONTINUE;
  }

  /** Adds a new texture update attachment, or updates an existing one's step value if one already exists for that region */
  @Method(0x800eb280L)
  public static void addOrUpdateTextureAnimationAttachment(final EffectManagerData6c<?> manager, final RECT vramPos, final int step) {
    TextureAnimationAttachment1c attachment = findTextureAnimationAttachment(manager, vramPos);

    if(attachment == null) {
      attachment = manager.addAttachment(10, 0, Bttl_800e::tickTextureAnimationAttachment, new TextureAnimationAttachment1c());
      attachment.rect_0c.set(vramPos);
      attachment.accumulator_14 = 0;
    }

    //LAB_800eb2ec
    attachment.step_18 = step;
  }

  @Method(0x800eb308L)
  public static void addCContainerTextureAnimationAttachments(final EffectManagerData6c<?> manager, final CContainer cContainer, final DeffPart.TextureInfo[] textureInfo) {
    if(cContainer.ptr_08 != null) {
      final CContainerSubfile2 s2 = cContainer.ptr_08;

      //LAB_800eb348
      for(int s1 = 0; s1 < 7; s1++) {
        final short[] s0 = s2._00[s1];

        if((s0[0] & 0x4000) != 0) {
          final TextureAnimationAttachment1c sub = manager.addAttachment(10, 0, Bttl_800e::tickTextureAnimationAttachment, new TextureAnimationAttachment1c());

          if((s0[1] & 0x3c0) == 0) {
            sub.rect_0c.x.set((short)(textureInfo[0].vramPos_00.x.get() & 0x3c0 | s0[1]));
            sub.rect_0c.y.set((short)(textureInfo[0].vramPos_00.y.get() & 0x100 | s0[2]));
          } else {
            //LAB_800eb3cc
            sub.rect_0c.x.set(s0[1]);
            sub.rect_0c.y.set(s0[2]);
          }

          //LAB_800eb3dc
          //LAB_800eb3f8
          sub.rect_0c.w.set((short)(s0[3] / 4));
          sub.rect_0c.h.set(s0[4]);
          sub.accumulator_14 = 0;

          final int v0;
          if(s0[6] >= 0x10) {
            v0 = s0[6] * 0x10;
          } else {
            //LAB_800eb42c
            v0 = 0x100 / s0[6];
          }

          //LAB_800eb434
          sub.step_18 = v0;

          if(s0[5] == 0) {
            sub.step_18 = -sub.step_18;
          }
        }

        //LAB_800eb45c
      }
    }

    //LAB_800eb46c
  }

  @Method(0x800eb48cL)
  public static void addOrUpdateTextureAnimationAttachment(final int scriptIndex, final int textureIndex, final int step) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(scriptIndex, EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04;
    addOrUpdateTextureAnimationAttachment(manager, tmdType.textureInfo_08[textureIndex * 2].vramPos_00, step);
  }

  @ScriptDescription("Adds or updates a texture animation attachment to an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "varIndex", description = "The texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "step", description = "The animation movement step")
  @Method(0x800eb518L)
  public static FlowControl scriptAddOrUpdateTextureAnimationAttachment(final RunningScript<?> script) {
    addOrUpdateTextureAnimationAttachment(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  /** Used in Dart transform */
  @Method(0x800eb554L)
  public static void applyRedEyeDragoonTransformationFlameArmorEffectTextureAnimations(final RECT a0, final Vector2i a1, final int height) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, a1.x, a1.y + a0.h.get() - height, a0.w.get(), height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a1.x, a1.y + height, a1.x, a1.y, a0.w.get(), a0.h.get() - height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a1.x, a1.y, a0.x.get(), a0.y.get() + a0.h.get() - height, a0.w.get(), height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x.get(), a0.y.get() + height, a0.x.get(), a0.y.get(), a0.w.get(), a0.h.get() - height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x.get(), a0.y.get(), 960, 256, a0.w.get(), height));
  }

  @Method(0x800eb7c4L)
  public static int tickRedEyeDragoonTransformationFlameArmorEffect(final EffectManagerData6c<?> manager, final RedEyeDragoonTransformationFlameArmorEffect20 effect) {
    //LAB_800eb7e8
    effect.accumulator_14 += effect.step_18;

    //LAB_800eb800
    int a2 = (effect.step_18 >> 8) % effect.rect_0c.h.get();

    if(a2 < 0) {
      a2 = a2 + effect.rect_0c.h.get();
    }

    //LAB_800eb828
    if(a2 != 0) {
      applyRedEyeDragoonTransformationFlameArmorEffectTextureAnimations(effect.rect_0c, effect._1c, a2);
    }

    //LAB_800eb838
    return 1;
  }

  @ScriptDescription("Attached the red eye dragoon transformation flame armour effect to an effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex1", description = "The first texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex2", description = "The second texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attachmentStep", description = "The attachment step")
  @Method(0x800eb84cL)
  public static FlowControl scriptAddRedEyeDragoonTransformationFlameArmorEffectAttachment(final RunningScript<?> script) {
    EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04;
    final DeffPart.TextureInfo textureInfo1 = tmdType.textureInfo_08[script.params_20[1].get() * 2];
    final DeffPart.TextureInfo textureInfo2 = tmdType.textureInfo_08[script.params_20[2].get() * 2];

    //LAB_800eb8fc
    while(!manager.hasAttachment(10)) {
      final ScriptState<EffectManagerData6c<?>> parent = manager.parentScript_50;

      if(parent == null) {
        break;
      }

      manager = parent.innerStruct_00;
    }

    //LAB_800eb934
    final RedEyeDragoonTransformationFlameArmorEffect20 attachment = manager.addAttachment(10, 0, Bttl_800e::tickRedEyeDragoonTransformationFlameArmorEffect, new RedEyeDragoonTransformationFlameArmorEffect20());
    attachment.rect_0c.set(textureInfo1.vramPos_00);
    attachment.accumulator_14 = 0;
    attachment.step_18 = script.params_20[3].get();
    attachment._1c.x = textureInfo2.vramPos_00.x.get();
    attachment._1c.y = textureInfo2.vramPos_00.y.get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800eb9acL)
  public static void loadStageTmd(final BattleStage stage, final CContainer extTmd, final TmdAnimationFile tmdAnim) {
    final float x = stage.coord2_558.coord.transfer.x;
    final float y = stage.coord2_558.coord.transfer.y;
    final float z = stage.coord2_558.coord.transfer.z;

    stage_800bda0c = stage;

    //LAB_800eb9fc
    for(int i = 0; i < 10; i++) {
      stage._618[i] = 0;
    }

    stage.tmd_5d0 = extTmd.tmdPtr_00.tmd;

    if(extTmd.ptr_08 != null) {
      stage._5ec = extTmd.ptr_08;

      //LAB_800eba38
      for(int i = 0; i < 10; i++) {
        stage._5f0[i] = stage._5ec._00[i];
        FUN_800ec86c(stage, i);
      }
    } else {
      //LAB_800eba74
      //LAB_800eba7c
      for(int i = 0; i < 10; i++) {
        stage._5f0[i] = null;
      }
    }

    //LAB_800eba8c
    stage.dobj2s_00 = new ModelPart10[stage.tmd_5d0.header.nobj];
    Arrays.setAll(stage.dobj2s_00, i -> new ModelPart10());
    initObjTable2(stage.dobj2s_00);
    stage.coord2_558.transforms = stage.param_5a8;
    GsInitCoordinate2(null, stage.coord2_558);
    prepareObjTable2(stage.dobj2s_00, stage.tmd_5d0, stage.coord2_558);
    applyInitialStageTransforms(stage, tmdAnim);

    stage.coord2_558.coord.transfer.set(x, y, z);
    stage.flags_5e4 = 0;
    stage.z_5e8 = 0x200;
  }

  @Method(0x800ebb58L)
  public static void applyScreenDarkening(final int multiplier) {
    final BattleStageDarkening1800 darkening = stageDarkening_800c6958;

    //LAB_800ebb7c
    for(int y = 0; y < 16; y++) {
      //LAB_800ebb80
      for(int x = 0; x < stageDarkeningClutWidth_800c695c; x++) {
        final int colour = darkening.original_000[y][x] & 0xffff;
        final int mask = colour >>> 15 & 0x1;
        final int b = (colour >>> 10 & 0x1f) * multiplier >> 4 & 0x1f;
        final int g = (colour >>> 5 & 0x1f) * multiplier >> 4 & 0x1f;
        final int r = (colour & 0x1f) * multiplier >> 4 & 0x1f;

        final int v0;
        if(r != 0 || g != 0 || b != 0 || colour == 0) {
          v0 = mask << 15 | b << 10 | g << 5 | r;
        } else {
          v0 = colour & 0xffff_8000 | 0x1;
        }

        darkening.modified_800[y][x] = (short)v0;
      }
    }

    for(int y = 0; y < 16; y++) {
      GPU.uploadData(new RECT().set((short)448, (short)(240 + y), (short)64, (short)1), stageDarkening_800c6958.modified_800[y]);
    }
  }

  @Method(0x800ebd34L)
  public static void applyBattleStageTextureAnimations(final BattleStage struct, final int index) {
    final short[] v0 = struct._5f0[index];

    if(v0 == null) {
      struct._618[index] = 0;
      return;
    }

    //LAB_800ebd84
    final int x = v0[0];
    final int y = v0[1];
    final int w = (short)(v0[2] / 4);
    final int h = v0[3];

    //LAB_800ebdcc
    int a2 = 4;

    // There was a loop here, but each iteration overwrote the results from the previous iteration... I collapsed it into a single iteration
    a2 += (struct._65e[index] - 1) * 2;
    final int t1 = (short)(v0[a2] & 1);
    final int t0 = (short)(v0[a2] >>> 1);
    int s0 = v0[a2 + 1];
    a2 += 2;

    //LAB_800ebdf0
    if((s0 & 0xf) != 0 && (struct._622[index] & 0xf) != 0) {
      struct._622[index]--;

      if(struct._622[index] == 0) {
        struct._622[index] = s0;
        s0 = 16;
      } else {
        //LAB_800ebe34
        s0 = 0;
      }
    }

    //LAB_800ebe38
    struct._64a[index]++;

    if(struct._64a[index] >= (short)t0) {
      struct._64a[index] = 0;

      if(v0[a2] != -1) {
        struct._65e[index]++;
      } else {
        //LAB_800ebe88
        struct._65e[index] = 1;
      }
    }

    //LAB_800ebe94
    if(s0 != 0) {
      final int s1 = s0 / 16;
      final int s4 = h - s1;

      final int s6;
      if(t1 == 0) {
        s6 = 256 + s1;

        GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, x, y, w, h));
        GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + s4, 960, 256, w, s1));
        GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y, 960, s6, w, s4));
      } else {
        //LAB_800ebf88
        s6 = 256 + s4;

        GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, x, y, w, h));
        GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y, 960, s6, w, s1));
        GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + s1, 960, 256, w, s4));
      }
    }

    //LAB_800ec080
  }

  @Method(0x800ec258L)
  public static void renderBttlShadow(final Model124 model) {
    final Model124 shadow = shadowModel_800bda10;

    GsInitCoordinate2(model.coord2_14, shadow.coord2_14);

    if(model.shadowType_cc == 3) {
      //LAB_800ec2ec
      shadow.coord2_14.coord.transfer.x = model.shadowOffset_118.x + model.modelParts_00[model.modelPartWithShadowIndex_cd].coord2_04.coord.transfer.x;
      shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y - MathHelper.safeDiv(model.coord2_14.coord.transfer.y, model.coord2_14.transforms.scale.y);
      shadow.coord2_14.coord.transfer.z = model.shadowOffset_118.z + model.modelParts_00[model.modelPartWithShadowIndex_cd].coord2_04.coord.transfer.z;
    } else {
      shadow.coord2_14.coord.transfer.x = model.shadowOffset_118.x;

      if(model.shadowType_cc == 1) {
        shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y;
      } else {
        //LAB_800ec2bc
        shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y - MathHelper.safeDiv(model.coord2_14.coord.transfer.y, model.coord2_14.transforms.scale.y);
      }

      //LAB_800ec2e0
      shadow.coord2_14.coord.transfer.z = model.shadowOffset_118.z;
    }

    //LAB_800ec370
    shadow.zOffset_a0 = model.zOffset_a0 + 16;
    shadow.coord2_14.transforms.scale.set(model.shadowSize_10c.x).div(4.0f);
    shadow.coord2_14.coord.rotationXYZ(shadow.coord2_14.transforms.rotate);
    shadow.coord2_14.coord.scaleLocal(shadow.coord2_14.transforms.scale);
    shadow.coord2_14.flg = 0;
    final GsCOORDINATE2 v0 = shadow.modelParts_00[0].coord2_04;
    final Transforms s0 = v0.transforms;
    s0.rotate.zero();
    v0.coord.rotationZYX(s0.rotate);
    s0.trans.zero();
    v0.coord.transfer.set(s0.trans);

    final MV sp0x30 = new MV();
    final MV sp0x10 = new MV();
    GsGetLws(shadow.modelParts_00[0].coord2_04, sp0x30, sp0x10);
    GsSetLightMatrix(sp0x30);
    GTE.setTransforms(sp0x10);
    Renderer.renderDobj2(shadow.modelParts_00[0], true, 0);
    shadow.modelParts_00[0].coord2_04.flg--;
  }

  @Method(0x800ec4bcL)
  public static void allocateStageDarkeningStorage() {
    stageDarkening_800c6958 = new BattleStageDarkening1800();
  }

  @Method(0x800ec4f0L)
  public static void deallocateStageDarkeningStorage() {
    stageDarkening_800c6958 = null;
  }

  @Method(0x800ec51cL)
  public static void renderBattleStage(final BattleStage stage) {
    //LAB_800ec548
    for(int i = 0; i < 10; i++) {
      if(stage._618[i] != 0) {
        applyBattleStageTextureAnimations(stage, i);
      }

      //LAB_800ec560
    }

    tmdGp0Tpage_1f8003ec.set(0);
    zOffset_1f8003e8.set(stage.z_5e8);

    //LAB_800ec5a0
    int partBit = 0x1;
    for(int i = 0; i < stage.dobj2s_00.length; i++) {
      final ModelPart10 part = stage.dobj2s_00[i];

      if((partBit & stage.flags_5e4) == 0) {
        final MV ls = new MV();
        final MV lw = new MV();
        GsGetLws(part.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);
        Renderer.renderDobj2(part, true, 0);

        if(part.obj != null) {
          RENDERER.queueModel(part.obj, lw)
            .lightDirection(lightDirectionMatrix_800c34e8)
            .lightColour(lightColourMatrix_800c3508)
            .backgroundColour(GTE.backgroundColour);
        }
      }

      //LAB_800ec608
      partBit <<= 1;
    }

    //LAB_800ec618
  }

  @Method(0x800ec63cL)
  public static void applyStagePartAnimations(final BattleStage stage) {
    //LAB_800ec688
    for(int i = 0; i < stage.partCount_5dc; i++) {
      final ModelPartTransforms0c rotTrans = stage.rotTrans_5d8[0][i];
      final GsCOORDINATE2 coord2 = stage.dobj2s_00[i].coord2_04;
      final Transforms param = coord2.transforms;

      param.rotate.set(rotTrans.rotate_00);
      coord2.coord.rotationZYX(param.rotate);

      param.trans.set(rotTrans.translate_06);
      coord2.coord.transfer.set(param.trans);
    }

    //LAB_800ec710
    stage.rotTrans_5d8 = Arrays.copyOfRange(stage.rotTrans_5d8, 1, stage.rotTrans_5d8.length);
  }

  @Method(0x800ec744L)
  public static void rotateBattleStage(final BattleStage stage) {
    stage.coord2_558.coord.rotationXYZ(stage.param_5a8.rotate);
    stage.coord2_558.flg = 0;
  }

  @Method(0x800ec774L)
  public static void applyInitialStageTransforms(final BattleStage stage, final TmdAnimationFile anim) {
    stage.rotTrans_5d4 = anim.partTransforms_10;
    stage.rotTrans_5d8 = anim.partTransforms_10;
    stage.partCount_5dc = anim.modelPartCount_0c;
    stage.totalFrames_5de = anim.totalFrames_0e;
    stage.animationState_5e0 = 0;
    applyStagePartAnimations(stage);
    stage.animationState_5e0 = 1;
    stage.remainingFrames_5e2 = stage.totalFrames_5de;
    stage.rotTrans_5d8 = stage.rotTrans_5d4;
  }

  @Method(0x800ec7e4L)
  public static Vector2f perspectiveTransformXyz(final Model124 model, final float x, final float y, final float z) {
    final MV ls = new MV();
    GsGetLs(model.coord2_14, ls);
    GTE.setTransforms(ls);

    GTE.perspectiveTransform(x, y, z);
    return new Vector2f(GTE.getScreenX(2), GTE.getScreenY(2));
  }

  @Method(0x800ec86cL)
  public static void FUN_800ec86c(final BattleStage stage, final int index) {
    final short[] a2 = stage._5f0[index];

    if(a2 == null) {
      stage._618[index] = 0;
      return;
    }

    //LAB_800ec890
    if(a2[0] == -1) {
      stage._5f0[index] = null;
      return;
    }

    //LAB_800ec8a8
    stage._618[index] = 1;
    stage._622[index] = a2[5];
    stage._64a[index] = 0;
    stage._65e[index] = 1;
  }

  /** Stage darkening for counterattacks change the clut, this saves a backup copy */
  @Method(0x800ec8d0L)
  public static void backupStageClut(final FileData timFile) {
    final BattleStageDarkening1800 darkening = stageDarkening_800c6958;

    //LAB_800ec8ec
    for(int a1 = 0; a1 < 0x10; a1++) {
      //LAB_800ec8f4
      for(int a3 = 0; a3 < 0x40; a3++) {
        darkening.original_000[a1][a3] = timFile.readShort(0x14 + a1 * 0x80 + a3 * 0x2);
      }
    }

    // I don't think this condition is used?
    if(timFile.readUShort(0x8812) == 0x7422) {
      stageDarkeningClutWidth_800c695c = timFile.readUShort(0x8812 + 0x4) + 1;
    } else {
      //LAB_800ec954
      stageDarkeningClutWidth_800c695c = 64;
    }
  }

  @Method(0x800ec974L)
  public static void renderBttlModel(final Model124 model) {
    tmdGp0Tpage_1f8003ec.set(model.tpage_108);
    zOffset_1f8003e8.set(model.zOffset_a0);

    //LAB_800ec9d0
    for(int i = 0; i < model.modelParts_00.length; i++) {
      if((model.partInvisible_f4 & 1L << i) == 0) {
        final ModelPart10 s2 = model.modelParts_00[i];
        final MV lw = new MV();
        final MV ls = new MV();
        GsGetLws(s2.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);
        Renderer.renderDobj2(s2, true, 0);

        if(model.modelParts_00[i].obj != null) {
          RENDERER.queueModel(model.modelParts_00[i].obj, lw)
            .lightDirection(lightDirectionMatrix_800c34e8)
            .lightColour(lightColourMatrix_800c3508)
            .backgroundColour(GTE.backgroundColour);
        }
      }
    }

    //LAB_800eca58
    if(model.shadowType_cc != 0) {
      renderBttlShadow(model);
    }

    //LAB_800eca70
  }

  @Method(0x800eca98L)
  public static void drawTargetArrow(final int targetType, final int combatantIdx) {
    if(combatantIdx != -1) {
      final ScriptState<? extends BattleEntity27c> targetState;
      if(targetType == 0) {
        //LAB_800ecb00
        targetState = battleState_8006e398.charBents_e40[combatantIdx];
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
      final VitalsStat targetHp = target.stats.getStat(CoreMod.HP_STAT.get());
      if(targetHp.getCurrent() > targetHp.getMax() / 4) {
        colour = targetHp.getCurrent() > targetHp.getMax() / 2 ? 0 : 1;
      } else {
        colour = 2;
      }

      //LAB_800ecb90
      drawTargetArrow(target.model_148, colour, targetState, target);
    } else {
      //LAB_800ecba4
      long count = 0;
      if(targetType == 0) {
        //LAB_800ecbdc
        count = charCount_800c677c.get();
      } else if(targetType == 1) {
        //LAB_800ecbec
        count = aliveMonsterCount_800c6758.get();
        //LAB_800ecbc8
      } else if(targetType == 2) {
        //LAB_800ecbfc
        count = aliveBentCount_800c669c.get();
      }

      //LAB_800ecc04
      //LAB_800ecc1c
      for(int i = 0; i < count; i++) {
        final ScriptState<? extends BattleEntity27c> targetBent;
        if(targetType == 0) {
          //LAB_800ecc50
          targetBent = battleState_8006e398.charBents_e40[i];
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
        final VitalsStat targetHp = target.stats.getStat(CoreMod.HP_STAT.get());
        if(targetHp.getCurrent() > targetHp.getMax() / 4) {
          colour = targetHp.getCurrent() > targetHp.getMax() / 2 ? 0 : 1;
        } else {
          colour = 2;
        }

        //LAB_800eccac
        if((targetBent.storage_44[7] & 0x4000) == 0) {
          drawTargetArrow(target.model_148, colour, targetBent, target);
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
  public static void drawTargetArrow(final Model124 model, final int colour, final ScriptState<? extends BattleEntity27c> state, final BattleEntity27c bent) {
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
    final Vector2f screenCoords = perspectiveTransformXyz(model, x, y, z);

    //LAB_800ece9c
    battleMenu_800c6c34.transforms.identity();
    battleMenu_800c6c34.transforms.transfer.set(GPU.getOffsetX() + screenCoords.x - 8, GPU.getOffsetY() + screenCoords.y + targetArrowOffsetY_800fb188.get(tickCount_800bb0fc.get() & 0x7).get(), 112.0f);
    RENDERER.queueOrthoModel(battleMenu_800c6c34.targetArrows[colour], battleMenu_800c6c34.transforms);
  }

  @ScriptDescription("Copies a block of vram from one position to another")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "destX", description = "The destination X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "destY", description = "The destination Y coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The block width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The block height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sourceX", description = "The source X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sourceY", description = "The source Y coordinate")
  @Method(0x800ee210L)
  public static FlowControl scriptCopyVram(final RunningScript<?> script) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(script.params_20[4].get(), script.params_20[5].get(), script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get() / 4, (short)script.params_20[3].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's Z offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset")
  @Method(0x800ee2acL)
  public static FlowControl scriptSetBentZOffset(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.zOffset_a0 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's scale uniformly")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scale", description = "The uniform scale (12-bit fixed-point)")
  @Method(0x800ee2e4L)
  public static FlowControl scriptSetBentScaleUniform(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final float scale = script.params_20[1].get() / (float)0x1000;
    bent.model_148.coord2_14.transforms.scale.set(scale, scale, scale);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's scale")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z scale (12-bit fixed-point)")
  @Method(0x800ee324L)
  public static FlowControl scriptSetBentScale(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.transforms.scale.set(script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets shadow type to 2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800ee384L)
  public static FlowControl FUN_800ee384(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowType_cc = 2;
    bent.model_148.modelPartWithShadowIndex_cd = -1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets shadow type to 3, used when player combat script is initialized, second param is based on char ID")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartAttachmentIndex", description = "The model part index to attach the shadow to")
  @Method(0x800ee3c0L)
  public static FlowControl FUN_800ee3c0(final RunningScript<?> script) {
    final BattleEntity27c v1 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    v1.model_148.shadowType_cc = 3;
    v1.model_148.modelPartWithShadowIndex_cd = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets shadow type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800ee408L)
  public static FlowControl FUN_800ee408(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final int index = bent.model_148.modelPartWithShadowIndex_cd;
    if(index == -2) {
      //LAB_800ee450
      bent.model_148.shadowType_cc = 0;
    } else if(index == -1) {
      bent.model_148.shadowType_cc = 2;
    } else {
      //LAB_800ee458
      bent.model_148.shadowType_cc = 3;
    }

    //LAB_800ee460
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Disables a battle entity's shadow")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800ee468L)
  public static FlowControl scriptDisableBentShadow(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowType_cc = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's shadow size")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The shadow's X size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The shadow's Z size (12-bit fixed-point)")
  @Method(0x800ee49cL)
  public static FlowControl scriptSetBentShadowSize(final RunningScript<?> script) {
    final BattleEntity27c a1 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    a1.model_148.shadowSize_10c.x = script.params_20[1].get() / (float)0x1000;
    a1.model_148.shadowSize_10c.z = script.params_20[2].get() / (float)0x1000;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's shadow offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The shadow's X size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The shadow's Y size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The shadow's Z size (12-bit fixed-point)")
  @Method(0x800ee4e8L)
  public static FlowControl scriptSetBentShadowOffset(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowOffset_118.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Darkens the combat stage (for things like counter-attacks, certain dragoon transformations, etc.)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "intensity", description = "How intense the darkening effect should be")
  @Method(0x800ee548L)
  public static FlowControl scriptApplyScreenDarkening(final RunningScript<?> script) {
    applyScreenDarkening(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the number of model parts in the battle stage's model")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "count", description = "The number of model parts")
  @Method(0x800ee574L)
  public static FlowControl scriptGetStageNobj(final RunningScript<?> script) {
    script.params_20[0].set(stage_800bda0c.dobj2s_00.length);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Shows the battle stage's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartIndex", description = "The model part index")
  @Method(0x800ee594L)
  public static FlowControl scriptShowStageModelPart(final RunningScript<?> a0) {
    stage_800bda0c.flags_5e4 |= 0x1 << a0.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Hides the battle stage's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartIndex", description = "The model part index")
  @Method(0x800ee5c0L)
  public static FlowControl scriptHideStageModelPart(final RunningScript<?> a0) {
    stage_800bda0c.flags_5e4 &= ~(0x1 << a0.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the battle stage model's Z offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset")
  @Method(0x800ee5f0L)
  public static FlowControl scriptSetStageZ(final RunningScript<?> script) {
    stage_800bda0c.z_5e8 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee610L)
  public static void initBattleMenu() {
    countCombatUiFilesLoaded_800c6cf4.set(0);
    battleHudYOffsetIndex_800c6c38.set(1);
    spellAndItemMenu_800c6b60 = new SpellAndItemMenuA4();
    battleMenu_800c6c34 = new BattleMenuStruct58();

    clearBattleHudDisplay();
    clearSpellAndItemMenu();
    clearBattleMenu();

    monsterCount_800c6b9c.set(0);
    itemTargetAll_800c69c8.set(false);
    itemTargetType_800c6b68.set(0);

    //LAB_800ee764
    for(int combatantIndex = 0; combatantIndex < 9; combatantIndex++) {
      monsterBents_800c6b78.get(combatantIndex).set(-1);

      //LAB_800ee770
      for(int v1 = 0; v1 < 22; v1++) {
        currentEnemyNames_800c69d0.get(combatantIndex).charAt(v1, 0xa0ff);
      }
    }

    //LAB_800ee7b0
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      //LAB_800ee7b8
      for(int v1 = 0; v1 < 22; v1++) {
        melbuMonsterNames_800c6ba8.get(charSlot).charAt(v1, 0xa0ff);
      }
    }

    usedRepeatItems_800c6c3c.clear();

    _800c697e.set((short)0);
    _800c6980.set((short)0);
    dragoonSpaceElement_800c6b64 = null;

    //LAB_800ee894
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      spGained_800bc950.get(charSlot).set(0);
    }

    sortItems();
    prepareItemList();
  }

  @Method(0x800ee8c4L)
  public static void battleHudTexturesLoadedCallback(final List<FileData> files) {
    //LAB_800ee9c0
    for(int fileIndex = 0; fileIndex < files.size(); fileIndex++) {
      if(files.get(fileIndex).hasVirtualSize()) {
        final Tim tim = new Tim(files.get(fileIndex));

        if(fileIndex == 0) {
          GPU.uploadData(new RECT().set((short)704, (short)256, (short)64, (short)256), tim.getImageData());
        }

        //LAB_800eea20
        final RECT rect = new RECT();
        if(fileIndex < 4) {
          rect.x.set((short)(battleHudTextureVramXOffsets_800c6e60[fileIndex] + 704));
          rect.y.set((short)496);
        } else {
          //LAB_800eea3c
          rect.x.set((short)(battleHudTextureVramXOffsets_800c6e60[fileIndex] + 896));
          rect.y.set((short)304);
        }

        //LAB_800eea50
        rect.w.set((short)combatUiElementRectDimensions_800c6e48[fileIndex].x);
        rect.h.set((short)combatUiElementRectDimensions_800c6e48[fileIndex].y);
        GPU.uploadData(rect, tim.getClutData());
        countCombatUiFilesLoaded_800c6cf4.add(1);
      }
    }
    //LAB_800eeaac
  }

  @Method(0x800eeaecL)
  public static void updateGameStateAndDeallocateMenu() {
    //LAB_800eeb10
    //LAB_800eebb4
    //LAB_800eebd8
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final PlayerBattleEntity bent = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;
      final CharacterData2c charData = gameState_800babc8.charData_32c[bent.charId_272];

      //LAB_800eec10
      charData.hp_08 = Math.max(1, bent.stats.getStat(CoreMod.HP_STAT.get()).getCurrent());

      if((gameState_800babc8.goods_19c[0] & 0x1 << characterDragoonIndices_800c6e68.get(bent.charId_272).get()) != 0) {
        charData.mp_0a = bent.stats.getStat(CoreMod.MP_STAT.get()).getCurrent();
      }

      //LAB_800eec78
      if(bent.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0x1 << characterDragoonIndices_800c6e68.get(9).get()) != 0) {
        charData.mp_0a = bent.stats.getStat(CoreMod.MP_STAT.get()).getCurrent();
      }

      //LAB_800eecb8
      charData.status_10 = bent.status_0e & 0xc8;
      charData.sp_0c = bent.stats.getStat(CoreMod.SP_STAT.get()).getCurrent();
    }

    //LAB_800eed78
    for(final Item item : usedRepeatItems_800c6c3c) {
      giveItem(item);
    }

    usedRepeatItems_800c6c3c.clear();

    spellAndItemMenu_800c6b60.delete();
    spellAndItemMenu_800c6b60 = null;
    battleMenu_800c6c34.delete();
    battleMenu_800c6c34 = null;
    deleteUiElements();
    deleteFloatingTextDigits();
  }

  @Method(0x800eee80L)
  public static void loadMonster(final ScriptState<MonsterBattleEntity> state) {
    //LAB_800eeecc
    for(int i = 0; i < 3; i++) {
      final LodString name = monsterNames_80112068.get(melbuMonsterNameIndices.get(i).get()).deref();

      //LAB_800eeee0
      for(int charIndex = 0; ; charIndex++) {
        melbuMonsterNames_800c6ba8.get(i).charAt(charIndex, name.charAt(charIndex));

        if(name.charAt(charIndex) >= 0xa0ff) {
          break;
        }
      }

      //LAB_800eef0c
    }

    final MonsterBattleEntity monster = state.innerStruct_00;
    final LodString name = monsterNames_80112068.get(monster.charId_272).deref();

    //LAB_800eef7c
    for(int charIndex = 0; ; charIndex++) {
      currentEnemyNames_800c69d0.get(monsterCount_800c6b9c.get()).charAt(charIndex, name.charAt(charIndex));

      if(name.charAt(charIndex) >= 0xa0ff) {
        break;
      }
    }

    //LAB_800eefa8
    monsterBents_800c6b78.get(monsterCount_800c6b9c.get()).set(state.index);
    monsterCount_800c6b9c.incr();

    //LAB_800eefcc
    final MonsterStats1c monsterStats = monsterStats_8010ba98.get(monster.charId_272);

    final MonsterStatsEvent statsEvent = EVENTS.postEvent(new MonsterStatsEvent(monster.charId_272));

    final VitalsStat monsterHp = monster.stats.getStat(CoreMod.HP_STAT.get());
    monsterHp.setCurrent(statsEvent.hp);
    monsterHp.setMaxRaw(statsEvent.maxHp);
    monster.specialEffectFlag_14 = statsEvent.specialEffectFlag;
//    monster.equipmentType_16 = 0;
    monster.equipment_02_18 = 0;
    monster.equipmentEquipableFlags_1a = 0;
    monster.displayElement_1c = statsEvent.elementFlag;
    monster.equipment_05_1e = monsterStats._0e.get();
    monster.equipmentElementalImmunity_22.set(statsEvent.elementalImmunityFlag);
    monster.equipmentStatusResist_24 = statsEvent.statusResistFlag;
    monster.equipment_09_26 = 0;
    monster.equipmentAttack1_28 = 0;
    monster._2e = 0;
    monster.equipmentIcon_30 = 0;
    monster.stats.getStat(CoreMod.SPEED_STAT.get()).setRaw(statsEvent.speed);
    monster.attack_34 = statsEvent.attack;
    monster.magicAttack_36 = statsEvent.magicAttack;
    monster.defence_38 = statsEvent.defence;
    monster.magicDefence_3a = statsEvent.magicDefence;
    monster.attackHit_3c = 0;
    monster.magicHit_3e = 0;
    monster.attackAvoid_40 = statsEvent.attackAvoid;
    monster.magicAvoid_42 = statsEvent.magicAvoid;
    monster.onHitStatusChance_44 = 0;
    monster.equipment_19_46 = 0;
    monster.equipment_1a_48 = 0;
    monster.equipmentOnHitStatus_4a = 0;
    monster.targetArrowPos_78.set(monsterStats.targetArrowX_12.get(), monsterStats.targetArrowY_13.get(), monsterStats.targetArrowZ_14.get());
    monster.hitCounterFrameThreshold_7e = monsterStats.hitCounterFrameThreshold_15.get();
    monster._80 = monsterStats._16.get();
    monster._82 = monsterStats._17.get();
    monster._84 = monsterStats._18.get();
    monster._86 = monsterStats._19.get();
    monster._88 = monsterStats._1a.get();
    monster._8a = monsterStats._1b.get();

    monster.damageReductionFlags_6e = monster.specialEffectFlag_14;
    monster._70 = monster.equipment_05_1e;
    monster.monsterElement_72 = monster.displayElement_1c;
    monster.monsterElementalImmunity_74.set(monster.equipmentElementalImmunity_22);
    monster.monsterStatusResistFlag_76 = monster.equipmentStatusResist_24;

    if((monster.damageReductionFlags_6e & 0x8) != 0) {
      monster.physicalImmunity_110 = true;
    }

    //LAB_800ef25c
    if((monster.damageReductionFlags_6e & 0x4) != 0) {
      monster.magicalImmunity_112 = true;
    }
  }

  @Method(0x800ef28cL)
  public static void FUN_800ef28c() {
    //LAB_800ef2c4
    //TODO sp0x18 is unused, why?
    //memcpy(sp0x18, _800c6e68.getAddress(), 0x28);

    loadCharacterStats();
    _800be5d0.setu(1);

    //LAB_800ef31c
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      dragoonSpells_800c6960.get(charSlot).charIndex_00.set(-1);

      //LAB_800ef328
      for(int spellSlot = 0; spellSlot < 8; spellSlot++) {
        dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellSlot).set(-1);
      }
    }

    //LAB_800ef36c
    //LAB_800ef38c
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final PlayerBattleEntity player = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;
      final byte[] spellIndices = new byte[8];
      getUnlockedDragoonSpells(spellIndices, player.charId_272);
      dragoonSpells_800c6960.get(charSlot).charIndex_00.set(player.charId_272);

      //LAB_800ef3d8
      for(int spellIndex = 0; spellIndex < 8; spellIndex++) {
        dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).set(spellIndices[spellIndex]);
      }

      //LAB_800ef400
      final VitalsStat playerHp = player.stats.getStat(CoreMod.HP_STAT.get());
      final VitalsStat playerMp = player.stats.getStat(CoreMod.MP_STAT.get());
      final VitalsStat playerSp = player.stats.getStat(CoreMod.SP_STAT.get());

      final ActiveStatsa0 stats = stats_800be5f8[player.charId_272];
      player.level_04 = stats.level_0e;
      player.dlevel_06 = stats.dlevel_0f;
      playerHp.setCurrent(stats.hp_04);
      playerHp.setMaxRaw(stats.maxHp_66);
      playerSp.setCurrent(stats.sp_08);
      playerSp.setMaxRaw(stats.dlevel_0f * 100);
      playerMp.setCurrent(stats.mp_06);
      playerMp.setMaxRaw(stats.maxMp_6e);
      player.status_0e = stats.flags_0c;
      player.specialEffectFlag_14 = stats.specialEffectFlag_76;
//      player.equipmentType_16 = stats.equipmentType_77;
      player.equipment_02_18 = stats.equipment_02_78;
      player.equipmentEquipableFlags_1a = stats.equipmentEquipableFlags_79;
      player.equipmentAttackElements_1c.set(stats.equipmentAttackElements_7a);
      player.equipment_05_1e = stats.equipment_05_7b;
      player.equipmentElementalResistance_20.set(stats.equipmentElementalResistance_7c);
      player.equipmentElementalImmunity_22.set(stats.equipmentElementalImmunity_7d);
      player.equipmentStatusResist_24 = stats.equipmentStatusResist_7e;
      player.equipment_09_26 = stats.equipment_09_7f;
      player.equipmentAttack1_28 = stats.equipmentAttack1_80;
      player._2e = stats._83;
      player.equipmentIcon_30 = stats.equipmentIcon_84;
      player.stats.getStat(CoreMod.SPEED_STAT.get()).setRaw(stats.equipmentSpeed_86 + stats.bodySpeed_69);
      player.attack_34 = stats.equipmentAttack_88 + stats.bodyAttack_6a;
      player.magicAttack_36 = stats.equipmentMagicAttack_8a + stats.bodyMagicAttack_6b;
      player.defence_38 = stats.equipmentDefence_8c + stats.bodyDefence_6c;
      player.magicDefence_3a = stats.equipmentMagicDefence_8e + stats.bodyMagicDefence_6d;
      player.attackHit_3c = stats.equipmentAttackHit_90;
      player.magicHit_3e = stats.equipmentMagicHit_92;
      player.attackAvoid_40 = stats.equipmentAttackAvoid_94;
      player.magicAvoid_42 = stats.equipmentMagicAvoid_96;
      player.onHitStatusChance_44 = stats.equipmentOnHitStatusChance_98;
      player.equipment_19_46 = stats.equipment_19_99;
      player.equipment_1a_48 = stats.equipment_1a_9a;
      player.equipmentOnHitStatus_4a = stats.equipmentOnHitStatus_9b;
      player.spellId_4e = stats.equipmentOnHitStatus_9b; //TODO retail bug?
      player.selectedAddition_58 = stats.selectedAddition_35;
      player.dragoonAttack_ac = stats.dragoonAttack_72;
      player.dragoonMagic_ae = stats.dragoonMagicAttack_73;
      player.dragoonDefence_b0 = stats.dragoonDefence_74;
      player.dragoonMagicDefence_b2 = stats.dragoonMagicDefence_75;
      player.physicalImmunity_110 = stats.equipmentPhysicalImmunity_46;
      player.magicalImmunity_112 = stats.equipmentMagicalImmunity_48;
      player.physicalResistance_114 = stats.equipmentPhysicalResistance_4a;
      player.magicalResistance_116 = stats.equipmentMagicalResistance_60;
      player._118 = stats.addition_00_9c;
      player.additionSpMultiplier_11a = stats.additionSpMultiplier_9e;
      player.additionDamageMultiplier_11c = stats.additionDamageMultiplier_9f;
      player.equipment_11e.clear();
      player.equipment_11e.putAll(stats.equipment_30);
      player.spMultiplier_128 = stats.equipmentSpMultiplier_4c;
      player.spPerPhysicalHit_12a = stats.equipmentSpPerPhysicalHit_4e;
      player.mpPerPhysicalHit_12c = stats.equipmentMpPerPhysicalHit_50;
      player.spPerMagicalHit_12e = stats.equipmentSpPerMagicalHit_52;
      player.mpPerMagicalHit_130 = stats.equipmentMpPerMagicalHit_54;
      player._132 = stats.equipmentSpecial2Flag80_56;
      player.hpRegen_134 = stats.equipmentHpRegen_58;
      player.mpRegen_136 = stats.equipmentMpRegen_5a;
      player.spRegen_138 = stats.equipmentSpRegen_5c;
      player.revive_13a = stats.equipmentRevive_5e;
      player.hpMulti_13c = stats.equipmentHpMulti_62;
      player.mpMulti_13e = stats.equipmentMpMulti_64;
      player._142 = stats.equipmentOnHitStatus_9b;
    }

    //LAB_800ef798
  }

  @Method(0x800ef7c4L)
  public static void clearBattleHudDisplay() {
    //LAB_800ef7d4
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleHudCharacterDisplay3c charDisplay = activePartyBattleHudCharacterDisplays_800c6c40.get(charSlot);
      charDisplay.charIndex_00.set((short)-1);
      charDisplay.unused_04.set((short)0);
      charDisplay.flags_06.set((short)0);
      charDisplay.x_08.set((short)0);
      charDisplay.y_0a.set((short)0);
      charDisplay.unused_0c.set((short)0);
      charDisplay.unused_0e.set((short)0);
      charDisplay.unused_10.set((short)0);
      charDisplay.unused_12.set((short)0);
    }

    //LAB_800ef818
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];

      //LAB_800ef820
      for(int i = 0; i < displayStats.digits_04.length; i++) {
        //LAB_800ef828
        for(int j = 0; j < displayStats.digits_04[i].length; j++) {
          displayStats.digits_04[i][j].digitValue_00 = -1;
        }
      }
    }

    //LAB_800ef878
    for(final FloatingNumberC4 num : floatingNumbers_800c6b5c) {
      num.state_00 = 0;
      num.flags_02 = 0;
      num.bentIndex_04 = -1;
      num.translucent_08 = false;
      num.b_0c = 0x80;
      num.g_0d = 0x80;
      num.r_0e = 0x80;
      num.ticksRemaining_14 = -1;
      num._18 = -1;

      //LAB_800ef89c
      for(int i = 0; i < num.digits_24.length; i++) {
        final FloatingNumberC4Sub20 digit = num.digits_24[i];
        digit.flags_00 = 0;
        digit._04 = 0;
        digit._08 = 0;
        digit.digit_0c = -1;
      }
    }
  }

  @Method(0x800ef8d8L)
  public static void initializeBattleHudCharacterDisplay(final int charSlot) {
    final BattleHudCharacterDisplay3c charDisplay = activePartyBattleHudCharacterDisplays_800c6c40.get(charSlot);
    charDisplay.charIndex_00.set((short)charSlot);
    charDisplay.charId_02.set((short)battleState_8006e398.charBents_e40[charSlot].innerStruct_00.charId_272);
    charDisplay.unused_04.set((short)0);
    charDisplay.flags_06.or(0x2);
    charDisplay.x_08.set((short)(charSlot * 94 + 63));
    charDisplay.y_0a.set((short)38);
    charDisplay.unused_10.set((short)32);
    charDisplay.unused_12.set((short)17);

    //LAB_800ef980
    for(int i = 0; i < 10; i++) {
      charDisplay._14.get(i).set(0);
    }

    final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];
    displayStats.x_00 = charDisplay.x_08.get();
    displayStats.y_02 = charDisplay.y_0a.get();
  }

  @Method(0x800ef9e4L)
  public static void drawUiText() {
    if(countCombatUiFilesLoaded_800c6cf4.get() == 6) {
      final int charCount = charCount_800c677c.get();

      //LAB_800efa34
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        if(activePartyBattleHudCharacterDisplays_800c6c40.get(charSlot).charIndex_00.get() == -1 && _800be5d0.get() == 1) {
          initializeBattleHudCharacterDisplay(charSlot);
        }
        //LAB_800efa64
      }

      //LAB_800efa78
      //LAB_800efa94
      //LAB_800efaac
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        final BattleHudCharacterDisplay3c charDisplay = activePartyBattleHudCharacterDisplays_800c6c40.get(charSlot);

        if(charDisplay.charIndex_00.get() != -1 && (charDisplay.flags_06.get() & 0x1) != 0 && (charDisplay.flags_06.get() & 0x2) != 0) {
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
          renderNumber(charSlot, 0, playerHp.getCurrent(), colour);
          renderNumber(charSlot, 1, playerHp.getMax(), 1);
          renderNumber(charSlot, 2, playerMp.getCurrent(), 1);
          renderNumber(charSlot, 3, playerMp.getMax(), 1);
          renderNumber(charSlot, 4, playerSp.getCurrent() / 100, 1);
          EVENTS.postEvent(new StatDisplayEvent(charSlot, player));

          charDisplay._14.get(1).set(tickCount_800bb0fc.get() & 0x3);

          //LAB_800efc0c
          if(playerSp.getCurrent() < playerSp.getMax()) {
            charDisplay.flags_06.and(0xfff3);
          } else {
            charDisplay.flags_06.or(0x4);
          }

          //LAB_800efc6c
          if((charDisplay.flags_06.get() & 0x4) != 0) {
            charDisplay.flags_06.xor(0x8);
          }

          //LAB_800efc84
          if(charDisplay._14.get(2).get() < 6) {
            charDisplay._14.get(2).incr();
          }
        }
        //LAB_800efc9c
      }

      //LAB_800efcac
      //LAB_800efcdc
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];
        final BattleHudCharacterDisplay3c charDisplay = activePartyBattleHudCharacterDisplays_800c6c40.get(charSlot);
        final short y = battleHudYOffsets_800fb198.get(battleHudYOffsetIndex_800c6c38.get()).get();
        charDisplay.y_0a.set(y);
        displayStats.y_02 = y;
      }

      //LAB_800efd00
      tickFloatingNumbers();
      handleSpellAndItemMenu();
    }
    //LAB_800efd10
  }

  private static UiBox battleUiBackground;
  private static UiBox battleUiName;
  private static final Obj[] names = new Obj[3];
  private static final Obj[] portraits = new Obj[3];
  private static final Obj[][] stats = new Obj[3][3];
  private static final MV uiTransforms = new MV();
  private static final Vector3f colourTemp = new Vector3f();

  @Method(0x800efd34L)
  public static void drawUiElements() {
    int spBarIndex = 0;

    //LAB_800efe04
    //LAB_800efe9c
    //LAB_800eff1c
    //LAB_800eff70
    //LAB_800effa0
    if(countCombatUiFilesLoaded_800c6cf4.get() >= 6) {
      //LAB_800f0ad4
      // Background
      if(activePartyBattleHudCharacterDisplays_800c6c40.get(0).charIndex_00.get() != -1 && (activePartyBattleHudCharacterDisplays_800c6c40.get(0).flags_06.get() & 0x1) != 0) {
        if(battleUiBackground == null) {
          battleUiBackground = new UiBox("Battle UI Background", 16, battleHudYOffsets_800fb198.get(battleHudYOffsetIndex_800c6c38.get()).get() - 26, 288, 40);
        }

        battleUiBackground.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
      }

      //LAB_800f0000
      //LAB_800f0074
      for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];
        final BattleHudCharacterDisplay3c charDisplay = activePartyBattleHudCharacterDisplays_800c6c40.get(charSlot);

        if(charDisplay.charIndex_00.get() != -1 && (charDisplay.flags_06.get() & 0x1) != 0 && (charDisplay.flags_06.get() & 0x2) != 0) {
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
              //TODO drawUiTextureElement last two params: brightnessIndex0, charDisplay._14.get(2).get()
              uiTransforms.transfer.set(displayStats.x_00 + digit.x_02, displayStats.y_02 + digit.y_04, 124.0f);
              final RenderEngine.QueuedModel digitModel = RENDERER.queueOrthoOverlayModel(floatingTextType1Digits[digit.digitValue_00], uiTransforms);

              if(charDisplay._14.get(2).get() < 6) {
                digit.colour.mul(((byte)(uiTextureElementBrightness_800c71ec.get(brightnessIndex0).get() + 0x80) / 6 * charDisplay._14.get(2).get() - 0x80 & 0xff) / 128.0f, colourTemp);
              } else {
                digit.colour.mul((uiTextureElementBrightness_800c71ec.get(brightnessIndex0).get() & 0xff) / 128.0f, colourTemp);
              }

              digitModel.colour(colourTemp);
            }
            //LAB_800f01e0
          }

          //LAB_800f01f0
          final NameAndPortraitDisplayMetrics0c namePortraitMetrics = hudNameAndPortraitMetrics_800fb444.get(player.charId_272).deref();

          // Names
          if(names[charSlot] == null) {
            names[charSlot] = buildUiTextureElement(
              "Name " + charSlot,
              namePortraitMetrics.nameU_00.get(), namePortraitMetrics.nameV_01.get(),
              namePortraitMetrics.nameW_02.get(), namePortraitMetrics.nameH_03.get(),
              0x2c
            );
          }

          uiTransforms.transfer.set(displayStats.x_00 + 1, displayStats.y_02 - 25, 124.0f);
          final RenderEngine.QueuedModel nameModel = RENDERER.queueOrthoOverlayModel(names[charSlot], uiTransforms);

          // Portraits
          if(portraits[charSlot] == null) {
            portraits[charSlot] = buildUiTextureElement(
              "Portrait " + charSlot,
              namePortraitMetrics.portraitU_04.get(), namePortraitMetrics.portraitV_05.get(),
              namePortraitMetrics.portraitW_06.get(), namePortraitMetrics.portraitH_07.get(),
              namePortraitMetrics.portraitClutOffset_08.get()
            );
          }

          uiTransforms.transfer.set(displayStats.x_00 - 44, displayStats.y_02 - 22, 124.0f);
          final RenderEngine.QueuedModel portraitModel = RENDERER.queueOrthoOverlayModel(portraits[charSlot], uiTransforms);

          if(charDisplay._14.get(2).get() < 6) {
            nameModel.monochrome(((byte)(uiTextureElementBrightness_800c71ec.get(brightnessIndex0).get() + 0x80) / 6 * charDisplay._14.get(2).get() - 0x80 & 0xff) / 128.0f);
            portraitModel.monochrome(((byte)(uiTextureElementBrightness_800c71ec.get(brightnessIndex1).get() + 0x80) / 6 * charDisplay._14.get(2).get() - 0x80 & 0xff) / 128.0f);
          } else {
            nameModel.monochrome((uiTextureElementBrightness_800c71ec.get(brightnessIndex0).get() & 0xff) / 128.0f);
            portraitModel.monochrome((uiTextureElementBrightness_800c71ec.get(brightnessIndex1).get() & 0xff) / 128.0f);
          }

          if(brightnessIndex0 != 0) {
            final int v1_0 = (6 - charDisplay._14.get(2).get()) * 8 + 100;
            final int x = displayStats.x_00 - centreScreenX_1f8003dc.get() + namePortraitMetrics.portraitW_06.get() / 2 - 44;
            final int y = displayStats.y_02 - centreScreenY_1f8003de.get() + namePortraitMetrics.portraitH_07.get() / 2 - 22;
            int dimVertexPositionModifier = (namePortraitMetrics.portraitW_06.get() + 2) * v1_0 / 100 / 2;
            final int x0 = x - dimVertexPositionModifier;
            final int x1 = x + dimVertexPositionModifier - 1;

            final short[] xs = {(short)x0, (short)x1, (short)x0, (short)x1};

            dimVertexPositionModifier = (namePortraitMetrics.portraitH_07.get() + 2) * v1_0 / 100 / 2;
            final int y0 = y - dimVertexPositionModifier;
            final int y1 = y + dimVertexPositionModifier - 1;

            final short[] ys = {(short)y0, (short)y0, (short)y1, (short)y1};

            //LAB_800f0438
            for(int i = 0; i < 8; i++) {
              dimVertexPositionModifier = charDisplay._14.get(2).get();

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
              final CombatPortraitBorderMetrics0c borderMetrics = combatPortraitBorderVertexCoords_800c6e9c.get(i % 4);

              // Draw border around currently active character's portrait
              drawLine(
                xs[borderMetrics.x1Index_00.get()] + borderMetrics.x1Offset_04.get() + borderMetrics._08.get() * borderLayer,
                ys[borderMetrics.y1Index_01.get()] + borderMetrics.y1Offset_05.get() + borderMetrics._09.get() * borderLayer,
                xs[borderMetrics.x2Index_02.get()] + borderMetrics.x2Offset_06.get() + borderMetrics._0a.get() * borderLayer,
                ys[borderMetrics.y2Index_03.get()] + borderMetrics.y2Offset_07.get() + borderMetrics._0b.get() * borderLayer,
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
            final BattleHudStatLabelMetrics0c labelMetrics = battleHudStatLabelMetrics_800c6ecc.get(i);

            // HP: /  MP: /  SP:
            //LAB_800f0610
            if(stats[charSlot][i] == null) {
              stats[charSlot][i] = buildUiTextureElement(
                "Stats " + charSlot + ' ' + i,
                labelMetrics.u_04.get(), labelMetrics.v_06.get(),
                labelMetrics.w_08.get(), labelMetrics.h_0a.get() + eraseSpHeight,
                0x2c
              );
            }

            uiTransforms.transfer.set(displayStats.x_00 + labelMetrics.x_00.get(), displayStats.y_02 + labelMetrics.y_02.get(), 124.0f);
            final RenderEngine.QueuedModel statsModel = RENDERER.queueOrthoOverlayModel(stats[charSlot][i], uiTransforms);

            if(charDisplay._14.get(2).get() < 6) {
              statsModel.monochrome(((byte)(uiTextureElementBrightness_800c71ec.get(brightnessIndex0).get() + 0x80) / 6 * charDisplay._14.get(2).get() - 0x80 & 0xff) / 128.0f);
            } else {
              statsModel.monochrome((uiTextureElementBrightness_800c71ec.get(brightnessIndex0).get() & 0xff) / 128.0f);
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
              final int left = displayStats.x_00 - centreScreenX_1f8003dc.get() + 3;
              final int top = displayStats.y_02 - centreScreenY_1f8003de.get() + 8;
              final int right = left + spBarW;
              final int bottom = top + 3;

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .pos(0, left, top)
                .pos(1, right, top)
                .pos(2, left, bottom)
                .pos(3, right, bottom);

              final ArrayRef<UnsignedByteRef> spBarColours = spBarColours_800c6f04.get(spBarIndex);

              cmd
                .rgb(0, spBarColours.get(0).get(), spBarColours.get(1).get(), spBarColours.get(2).get())
                .rgb(1, spBarColours.get(0).get(), spBarColours.get(1).get(), spBarColours.get(2).get());

              cmd
                .rgb(2, spBarColours.get(3).get(), spBarColours.get(4).get(), spBarColours.get(5).get())
                .rgb(3, spBarColours.get(3).get(), spBarColours.get(4).get(), spBarColours.get(5).get());

              GPU.queueCommand(31, cmd);
            }

            //SP border
            //LAB_800f0910
            for(int i = 0; i < 4; i++) {
              final int offsetX = displayStats.x_00 - centreScreenX_1f8003dc.get();
              final int offsetY = displayStats.y_02 - centreScreenY_1f8003de.get();
              drawLine(spBarBorderMetrics_800fb46c.get(i).x1_00.get() + offsetX, spBarBorderMetrics_800fb46c.get(i).y1_01.get() + offsetY, spBarBorderMetrics_800fb46c.get(i).x2_02.get() + offsetX, spBarBorderMetrics_800fb46c.get(i).y2_03.get() + offsetY, 0x60, 0x60, 0x60, false);
            }

            //Full SP meter
            if((charDisplay.flags_06.get() & 0x8) != 0) {
              //LAB_800f09ec
              for(int i = 0; i < 4; i++) {
                final int offsetX = displayStats.x_00 - centreScreenX_1f8003dc.get();
                final int offsetY = displayStats.y_02 - centreScreenY_1f8003de.get();
                drawLine(spBarFlashingBorderMetrics_800fb47c.get(i).x1_00.get() + offsetX, spBarFlashingBorderMetrics_800fb47c.get(i).y1_01.get() + offsetY, spBarFlashingBorderMetrics_800fb47c.get(i).x2_02.get() + offsetX, spBarFlashingBorderMetrics_800fb47c.get(i).y2_03.get() + offsetY, 0x80, 0, 0, false);
              }
            }
          }
        }
      }

      //LAB_800f0b3c
      drawFloatingNumbers();

      // Use item menu
      drawItemMenuElements();

      // Targeting
      final BattleMenuStruct58 menu = battleMenu_800c6c34;
      if(menu.displayTargetArrowAndName_4c) {
        drawTargetArrow(menu.targetType_50, menu.combatantIndex_54);
        final int targetCombatant = menu.combatantIndex_54;
        LodString str;
        Element element;
        if(targetCombatant == -1) {  // Target all
          str = targeting_800fb36c.get(menu.targetType_50).deref();
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
            str = getTargetEnemyName(monsterBent, currentEnemyNames_800c69d0.get(enemySlot));
            element = monsterBent.displayElement_1c;
            targetBent = monsterBent;
          } else if(menu.targetType_50 == 0) {
            targetBent = battleState_8006e398.charBents_e40[targetCombatant].innerStruct_00;
            str = playerNames_800fb378.get(targetBent.charId_272).deref();
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
              str = getTargetEnemyName(monsterBent, currentEnemyNames_800c69d0.get(targetCombatant));
              element = monsterBent.displayElement_1c;
            } else {
              str = playerNames_800fb378.get(targetBent.charId_272).deref();
              element = targetBent.getElement();

              if(targetBent.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0 && battleState_8006e398.charBents_e40[menu.combatantIndex_54].innerStruct_00.isDragoon()) {
                element = CoreMod.DIVINE_ELEMENT.get();
              }
            }
          }

          //LAB_800f0e60
          final int status = targetBent.status_0e;

          if((status & 0xff) != 0) {
            if((tickCount_800bb0fc.get() & 0x10) != 0) {
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
              str = ailments_800fb3a0.get(statusBit).deref();
            }
          }
        }

        //LAB_800f0ed8
        //Character name
        if(battleUiName == null) {
          battleUiName = new UiBox("Battle UI Name", 44, 23, 232, 14);
        }

        battleUiName.render(element.colour);
        renderText(str, 160 - textWidth(str) / 2, 24, TextColour.WHITE, 0);
      }
    }
    //LAB_800f0f2c
  }

  private static void deleteUiElements() {
    if(battleUiBackground != null) {
      battleUiBackground.delete();
      battleUiBackground = null;
    }

    if(battleUiName != null) {
      battleUiName.delete();
      battleUiName = null;
    }

    for(int i = 0; i < names.length; i++) {
      if(names[i] != null) {
        names[i].delete();
        names[i] = null;
      }

      if(portraits[i] != null) {
        portraits[i].delete();
        portraits[i] = null;
      }

      for(int j = 0; j < stats[i].length; j++) {
        if(stats[i][j] != null) {
          stats[i][j].delete();
          stats[i][j] = null;
        }
      }
    }
  }
}
