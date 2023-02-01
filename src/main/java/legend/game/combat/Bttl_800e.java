package legend.game.combat;

import legend.core.Config;
import legend.core.DebugHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gte.BVEC4;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.Cmb;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.types.AttackHitFlashEffect0c;
import legend.game.combat.types.BattleDisplayStats144;
import legend.game.combat.types.BattleDisplayStats144Sub10;
import legend.game.combat.types.BattleLightStruct64;
import legend.game.combat.types.BattleMenuStruct58;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStage;
import legend.game.combat.types.BattleStageDarkening1800;
import legend.game.combat.types.BattleStruct14;
import legend.game.combat.types.BattleStruct24;
import legend.game.combat.types.BattleStruct24_2;
import legend.game.combat.types.BattleStruct3c;
import legend.game.combat.types.BattleStruct4c;
import legend.game.combat.types.BttlLightStruct84;
import legend.game.combat.types.BttlLightStruct84Sub38;
import legend.game.combat.types.BttlScriptData6cSub13c;
import legend.game.combat.types.BttlScriptData6cSub1c;
import legend.game.combat.types.BttlScriptData6cSub20;
import legend.game.combat.types.BttlScriptData6cSubBase1;
import legend.game.combat.types.BttlScriptData6cSubBase2;
import legend.game.combat.types.BttlStructa4;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.CtmdUnpackingData50;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.combat.types.EffectManagerData6cInner;
import legend.game.combat.types.FloatingNumberC4;
import legend.game.combat.types.FloatingNumberC4Sub20;
import legend.game.combat.types.GuardHealEffect14;
import legend.game.combat.types.MonsterStats1c;
import legend.game.combat.types.Ptr;
import legend.game.combat.types.SpriteMetrics08;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.ExtendedTmd;
import legend.game.types.LodString;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms;
import legend.game.types.MrgFile;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment.FUN_8001d068;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023a88;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.checkForPsychBombX;
import static legend.game.Scus94491BpeSegment_8002.deallocateModel;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.prepareObjTable2;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.ApplyRotMatrix;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003ec90;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetAmbient;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.MulMatrix0;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.TransposeMatrix;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040780;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.model_800bda10;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stage_800bda0c;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Bttl_800c.FUN_800ca418;
import static legend.game.combat.Bttl_800c._800c669c;
import static legend.game.combat.Bttl_800c._800c6928;
import static legend.game.combat.Bttl_800c._800c6930;
import static legend.game.combat.Bttl_800c._800c6938;
import static legend.game.combat.Bttl_800c._800c697e;
import static legend.game.combat.Bttl_800c._800c6980;
import static legend.game.combat.Bttl_800c._800c69c8;
import static legend.game.combat.Bttl_800c._800c6b60;
import static legend.game.combat.Bttl_800c._800c6b64;
import static legend.game.combat.Bttl_800c._800c6b68;
import static legend.game.combat.Bttl_800c._800c6b6c;
import static legend.game.combat.Bttl_800c._800c6b78;
import static legend.game.combat.Bttl_800c._800c6b9c;
import static legend.game.combat.Bttl_800c._800c6ba8;
import static legend.game.combat.Bttl_800c._800c6c38;
import static legend.game.combat.Bttl_800c._800c6c40;
import static legend.game.combat.Bttl_800c._800c6cf4;
import static legend.game.combat.Bttl_800c._800c6e18;
import static legend.game.combat.Bttl_800c._800c6e48;
import static legend.game.combat.Bttl_800c._800c6e60;
import static legend.game.combat.Bttl_800c._800c6e90;
import static legend.game.combat.Bttl_800c._800c6e9c;
import static legend.game.combat.Bttl_800c._800c6ecc;
import static legend.game.combat.Bttl_800c._800c6ef0;
import static legend.game.combat.Bttl_800c._800c6f04;
import static legend.game.combat.Bttl_800c._800faec4;
import static legend.game.combat.Bttl_800c._800fafe8;
import static legend.game.combat.Bttl_800c._800fafec;
import static legend.game.combat.Bttl_800c._800fb040;
import static legend.game.combat.Bttl_800c._800fb05c;
import static legend.game.combat.Bttl_800c._800fb06c;
import static legend.game.combat.Bttl_800c._800fb148;
import static legend.game.combat.Bttl_800c._800fb188;
import static legend.game.combat.Bttl_800c._800fb198;
import static legend.game.combat.Bttl_800c._800fb444;
import static legend.game.combat.Bttl_800c._800fb46c;
import static legend.game.combat.Bttl_800c._800fb47c;
import static legend.game.combat.Bttl_800c.ailments_800fb3a0;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.characterDragoonIndices_800c6e68;
import static legend.game.combat.Bttl_800c.combatantCount_800c66a0;
import static legend.game.combat.Bttl_800c.ctmdRenderers_800fadbc;
import static legend.game.combat.Bttl_800c.ctmdUnpackingData_800c6920;
import static legend.game.combat.Bttl_800c.currentEnemyNames_800c69d0;
import static legend.game.combat.Bttl_800c.currentStage_800c66a4;
import static legend.game.combat.Bttl_800c.currentTurnBobj_800c66c8;
import static legend.game.combat.Bttl_800c.deffManager_800c693c;
import static legend.game.combat.Bttl_800c.displayStats_800c6c2c;
import static legend.game.combat.Bttl_800c.dragoonSpells_800c6960;
import static legend.game.combat.Bttl_800c.enemyCount_800c6758;
import static legend.game.combat.Bttl_800c.floatingNumbers_800c6b5c;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.light_800c6ddc;
import static legend.game.combat.Bttl_800c.lights_800c692c;
import static legend.game.combat.Bttl_800c.loadAttackAnimations;
import static legend.game.combat.Bttl_800c.monsterCount_800c6768;
import static legend.game.combat.Bttl_800c.playerNames_800fb378;
import static legend.game.combat.Bttl_800c.repeatItemIds_800c6e34;
import static legend.game.combat.Bttl_800c.spriteMetrics_800c6948;
import static legend.game.combat.Bttl_800c.stageDarkeningClutCount_800c695c;
import static legend.game.combat.Bttl_800c.stageDarkening_800c6958;
import static legend.game.combat.Bttl_800c.targeting_800fb36c;
import static legend.game.combat.Bttl_800c.tmds_800c6944;
import static legend.game.combat.Bttl_800c.usedRepeatItems_800c6c3c;
import static legend.game.combat.Bttl_800d.FUN_800dd89c;
import static legend.game.combat.Bttl_800d.ScaleVectorL_SVEC;
import static legend.game.combat.Bttl_800d.applyAnimation;
import static legend.game.combat.Bttl_800d.loadModelAnim;
import static legend.game.combat.Bttl_800d.loadModelTmd;
import static legend.game.combat.Bttl_800d.optimisePacketsIfNecessary;
import static legend.game.combat.Bttl_800d.unpackCtmdData;
import static legend.game.combat.Bttl_800f.FUN_800f3940;
import static legend.game.combat.Bttl_800f.FUN_800f4964;
import static legend.game.combat.Bttl_800f.FUN_800f4b80;
import static legend.game.combat.Bttl_800f.FUN_800f60ac;
import static legend.game.combat.Bttl_800f.FUN_800f83c8;
import static legend.game.combat.Bttl_800f.FUN_800f9584;
import static legend.game.combat.Bttl_800f.drawFloatingNumbers;
import static legend.game.combat.Bttl_800f.drawItemMenuElements;
import static legend.game.combat.Bttl_800f.drawLine;
import static legend.game.combat.Bttl_800f.drawUiTextureElement;
import static legend.game.combat.Bttl_800f.getTargetEnemyElement;
import static legend.game.combat.Bttl_800f.getTargetEnemyName;
import static legend.game.combat.Bttl_800f.renderNumber;
import static legend.game.combat.Bttl_800f.renderTextBoxBackground;
import static legend.game.combat.SBtld.enemyNames_80112068;
import static legend.game.combat.SBtld.monsterStats_8010ba98;
import static legend.game.combat.SEffe.FUN_80114f3c;
import static legend.game.combat.SEffe.FUN_80115cac;

public final class Bttl_800e {
  private Bttl_800e() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Bttl_800e.class);
  private static final Marker EFFECTS = MarkerManager.getMarker("EFFECTS");

  /** LSC 4 VERTEX GOURAUD NON-TEXTURED (SOLID) */
  @Method(0x800e02e8L)
  public static long FUN_800e02e8(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x14);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    //LAB_800e039c
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x14);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x6L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0xaL).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0xeL).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800e0580
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x12L).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);

          CPU.COP2(0x180001L);

          final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e0660
            CPU.MTC2(unpacked.offset(2, 0x0L).get(), 6);

            // Normals encoding:
            // zzzzzzzzzz yyyyyyyyyy xxxxxxxxxx uu
            // u = unused
            // Each component is signed and multiplied by 8
            final long norm0 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x4L).get() * 0x4L).get();
            norm.setX((short)((int)(norm0 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm0 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm0 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);

            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x8L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 2);
            CPU.MTC2(norm.getZ(),  3);

            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0xcL).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 4);
            CPU.MTC2(norm.getZ(),  5);

            CPU.COP2(0x118043fL);

            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .pos(3, v3.getX(), v3.getY())
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            final long norm3 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
            norm.setX((short)((int)(norm3 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm3 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm3 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);

            CPU.COP2(0x108041bL);

            cmd.rgb(3, (int)CPU.MFC2(22));

            final int tpage = tmdGp0Tpage_1f8003ec.get();

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e07fc
    }

    tmp.release();

    //LAB_800e0804
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** LSC 3 VERTEX GOURAUD NON-TEXTURED (SOLID) */
  @Method(0x800e0848L)
  public static long FUN_800e0848(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    //LAB_800e08f0
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x10);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x6L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0xaL).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0xeL).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2L) != 0 && winding != 0) {
          //LAB_800e0ac4
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e0b0c
            CPU.MTC2(unpacked.offset(4, 0x0L).get(), 6);

            // Normals encoding:
            // zzzzzzzzzz yyyyyyyyyy xxxxxxxxxx uu
            // u = unused
            // Each component is signed and multiplied by 8
            final long norm0 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x4L).get() * 0x4L).get();
            norm.setX((short)((int)(norm0 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm0 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm0 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);

            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x8L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 2);
            CPU.MTC2(norm.getZ(),  3);

            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0xcL).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 4);
            CPU.MTC2(norm.getZ(),  5);

            CPU.COP2(0x118043fL);

            final GpuCommandPoly cmd = new GpuCommandPoly(3)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            if((command & 0x2) != 0) {
              final int tpage = tmdGp0Tpage_1f8003ec.get();
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e0c4c
    }

    tmp.release();

    //LAB_800e0c54
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** LSC 4 VERTEX GOURAUD TEXTURE */
  @Method(0x800e0c98L)
  public static long FUN_800e0c98(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    primitives += 0x4L;

    CPU.MTC2(0x808080, 6);

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x20);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    //LAB_800e0d5c
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x20);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x12L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x16L).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x1aL).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800e0f5c
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x1eL).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);

          CPU.COP2(0x180001L);

          final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e104c
            final int clut = (int)unpacked.offset(2, 0x2L).get();
            final int tpage = (int)unpacked.offset(2, 0x6L).get();

            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .pos(3, v3.getX(), v3.getY())
              .uv(0, (int)unpacked.offset(1, 0x0L).get(), (int)unpacked.offset(1, 0x1L).get())
              .uv(1, (int)unpacked.offset(1, 0x4L).get(), (int)unpacked.offset(1, 0x5L).get())
              .uv(2, (int)unpacked.offset(1, 0x8L).get(), (int)unpacked.offset(1, 0x9L).get())
              .uv(3, (int)unpacked.offset(1, 0xcL).get(), (int)unpacked.offset(1, 0xdL).get());

            final long norm0 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
            norm.setX((short)((int)(norm0 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm0 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm0 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);

            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x14L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 2);
            CPU.MTC2(norm.getZ(),  3);

            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x18L).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 4);
            CPU.MTC2(norm.getZ(),  5);

            CPU.COP2(0x118043fL);

            cmd
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            final long norm3 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x1cL).get() * 0x4L).get();
            norm.setX((short)((int)(norm3 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm3 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm3 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);

            cmd.rgb(3, (int)CPU.MFC2(22));

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e11d0
    }

    tmp.release();

    //LAB_800e11d8
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** LSC 3 VERTEX GOURAUD TEXTURED */
  @Method(0x800e121cL)
  public static long FUN_800e121c(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x18);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    CPU.MTC2(0x808080, 6);

    //LAB_800e12e0
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x18);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0xeL).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x12L).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x16L).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final long s6 = CPU.MFC2(24);
        if((int)s6 > 0 || (command & 0x2L) != 0 && s6 != 0) {
          //LAB_800e14e0
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            // Normals encoding:
            // zzzzzzzzzz yyyyyyyyyy xxxxxxxxxx uu
            // u = unused
            // Each component is signed and multiplied by 8
            final long norm0 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0xcL).get() * 0x4L).get();
            norm.setX((short)((int)(norm0 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm0 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm0 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);

            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 2);
            CPU.MTC2(norm.getZ(),  3);

            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x14L).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 4);
            CPU.MTC2(norm.getZ(),  5);

            CPU.COP2(0x118043fL);

            final int clut = (int)unpacked.offset(2, 0x02L).get();
            final int tpage = (int)unpacked.offset(2, 0x06L).get();

            final GpuCommandPoly cmd = new GpuCommandPoly(3)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .uv(0, (int)unpacked.offset(1, 0x0L).get(), (int)unpacked.offset(1, 0x1L).get())
              .uv(1, (int)unpacked.offset(1, 0x4L).get(), (int)unpacked.offset(1, 0x5L).get())
              .uv(2, (int)unpacked.offset(1, 0x8L).get(), (int)unpacked.offset(1, 0x9L).get())
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e1654
    }

    tmp.release();

    //LAB_800e165c
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** LSC 4 VERTEX GOURAUD NON-TEXTURED (SOLID) */
  @Method(0x800e16a0L)
  public static long FUN_800e16a0(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    //LAB_800e1748
    final Memory.TemporaryReservation tmp = MEMORY.temp(0x20);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x20);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x12L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x16L).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x1aL).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800e191c
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x1eL).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);
          CPU.COP2(0x180001L);

          final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e19fc
            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .pos(3, v3.getX(), v3.getY());

            // Normals encoding:
            // zzzzzzzzzz yyyyyyyyyy xxxxxxxxxx uu
            // u = unused
            // Each component is signed and multiplied by 8
            CPU.MTC2(unpacked.offset(4, 0x0L).get(), 6);
            final long norm0 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
            norm.setX((short)((int)(norm0 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm0 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm0 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);

            cmd.rgb(0, (int)CPU.MFC2(22));

            CPU.MTC2(unpacked.offset(4, 0x4L).get(), 6);
            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x14L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);

            cmd.rgb(1, (int)CPU.MFC2(22));

            CPU.MTC2(unpacked.offset(4, 0x8L).get(), 6);
            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x18L).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);

            cmd.rgb(2, (int)CPU.MFC2(22));

            CPU.MTC2(unpacked.offset(4, 0xcL).get(), 6);
            final long norm3 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x1cL).get() * 0x4L).get();
            norm.setX((short)((int)(norm3 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm3 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm3 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);

            cmd.rgb(3, (int)CPU.MFC2(22));

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tmdGp0Tpage_1f8003ec.get() >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e1bd8
    }

    tmp.release();

    //LAB_800e1be0
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** LSC 3 VERTEX GOURAUD NON-TEXTURED (SOLID) */
  @Method(0x800e1c24L)
  public static long FUN_800e1c24(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x18);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    //LAB_800e1ccc
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x18);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0xeL).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x12L).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x16L).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800e1ea0
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final GpuCommandPoly cmd = new GpuCommandPoly(3)
            .pos(0, v0.getX(), v0.getY())
            .pos(1, v1.getX(), v1.getY())
            .pos(2, v2.getX(), v2.getY());

          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            // Normals encoding:
            // zzzzzzzzzz yyyyyyyyyy xxxxxxxxxx uu
            // u = unused
            // Each component is signed and multiplied by 8
            CPU.MTC2(unpacked.offset(4, 0x0L).get(), 6);
            final long norm0 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0xcL).get() * 0x4L).get();
            norm.setX((short)((int)(norm0 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm0 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm0 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);

            cmd.rgb(0, (int)CPU.MFC2(22));

            CPU.MTC2(unpacked.offset(4, 0x4L).get(), 6);
            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);

            cmd.rgb(1, (int)CPU.MFC2(22));

            CPU.MTC2(unpacked.offset(4, 0x8L).get(), 6);
            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x14L).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);

            cmd.rgb(2, (int)CPU.MFC2(22));

            if((command & 0x2) != 0) {
              final int tpage = tmdGp0Tpage_1f8003ec.get();
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e2070
    }

    tmp.release();

    //LAB_800e2078
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** NLSC 4 VERTEX GRADATION TEXTURED */
  @Method(0x800e20bcL)
  public static long FUN_800e20bc(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    final SVECTOR vert = new SVECTOR();

    //LAB_800e215c
    final Memory.TemporaryReservation tmp = MEMORY.temp(0x28);
    final Value unpacked = tmp.get();

    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x28);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x20L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x22L).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x24L).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800e2360
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x26L).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);

          CPU.COP2(0x180001L);

          final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e25a0
            final int clut = (int)unpacked.offset(2, 0x2L).get();
            final int tpage = (int)unpacked.offset(2, 0x6L).get();

            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .pos(3, v3.getX(), v3.getY())
              .uv(0, (int)unpacked.offset(1, 0x0L).get(), (int)unpacked.offset(1, 0x1L).get())
              .uv(1, (int)unpacked.offset(1, 0x4L).get(), (int)unpacked.offset(1, 0x5L).get())
              .uv(2, (int)unpacked.offset(1, 0x8L).get(), (int)unpacked.offset(1, 0x9L).get())
              .uv(3, (int)unpacked.offset(1, 0xcL).get(), (int)unpacked.offset(1, 0xdL).get())
              .rgb(0, (int)unpacked.offset(1, 0x10L).get() * r >> 12, (int)unpacked.offset(1, 0x11L).get() * g >> 12, (int)unpacked.offset(1, 0x12L).get() * b >> 12)
              .rgb(1, (int)unpacked.offset(1, 0x14L).get() * r >> 12, (int)unpacked.offset(1, 0x15L).get() * g >> 12, (int)unpacked.offset(1, 0x16L).get() * b >> 12)
              .rgb(2, (int)unpacked.offset(1, 0x18L).get() * r >> 12, (int)unpacked.offset(1, 0x19L).get() * g >> 12, (int)unpacked.offset(1, 0x1aL).get() * b >> 12)
              .rgb(3, (int)unpacked.offset(1, 0x1cL).get() * r >> 12, (int)unpacked.offset(1, 0x1dL).get() * g >> 12, (int)unpacked.offset(1, 0x1eL).get() * b >> 12);

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e25d4
    }

    tmp.release();

    //LAB_800e25dc
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** NLSC 3 VERTEX GRADATION TEXTURED */
  @Method(0x800e2620L)
  public static long FUN_800e2620(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x20);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();

    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x20);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x18L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x1aL).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x1cL).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2L) != 0 && winding != 0) {
          //LAB_800e28c4
          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e2a18
            final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
            final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
            final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

            final int clut = (int)unpacked.offset(2, 0x02L).get();
            final int tpage = (int)unpacked.offset(2, 0x06L).get();

            final GpuCommandPoly cmd = new GpuCommandPoly(3)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .uv(0, (int)unpacked.offset(1, 0x0L).get(), (int)unpacked.offset(1, 0x1L).get())
              .uv(1, (int)unpacked.offset(1, 0x4L).get(), (int)unpacked.offset(1, 0x5L).get())
              .uv(2, (int)unpacked.offset(1, 0x8L).get(), (int)unpacked.offset(1, 0x9L).get())
              .rgb(0, (int)unpacked.offset(1, 0x0cL).get() * r >> 12, (int)unpacked.offset(1, 0x0dL).get() * g >> 12, (int)unpacked.offset(1, 0x0eL).get() * g >> 12)
              .rgb(1, (int)unpacked.offset(1, 0x10L).get() * r >> 12, (int)unpacked.offset(1, 0x11L).get() * g >> 12, (int)unpacked.offset(1, 0x12L).get() * b >> 12)
              .rgb(2, (int)unpacked.offset(1, 0x14L).get() * r >> 12, (int)unpacked.offset(1, 0x15L).get() * g >> 12, (int)unpacked.offset(1, 0x16L).get() * b >> 12);

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e2a4c
    }

    tmp.release();

    //LAB_800e2a54
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** LSC 4 VERTEX GOURAUD TEXTURED 2 SEMI TRANS? */
  @Method(0x800e2a98L)
  public static long FUN_800e2a98(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x20);
    final Value unpacked = tmp.get();

    CPU.MTC2(0x808080, 6);

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    //LAB_800e2b50
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x20);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x12L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x16L).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x1aL).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x1eL).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);
          CPU.COP2(0x180001L);

          if((int)CPU.CFC2(31) >= 0) {
            final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

            CPU.COP2(0x168002eL);

            final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
            if(z >= 0xb) {
              //LAB_800e2e3c
              final int clut = (int)unpacked.offset(2, 0x2L).get();
              final int tpage = (int)unpacked.offset(2, 0x6L).get() & 0xff9f | tmdGp0Tpage_1f8003ec.get();

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.of(tpage >>> 7 & 0b11))
                .translucent(Translucency.of(tpage >>> 5 & 0b11))
                .clut((clut & 0b111111) * 16, clut >>> 6)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .pos(0, v0.getX(), v0.getY())
                .pos(1, v1.getX(), v1.getY())
                .pos(2, v2.getX(), v2.getY())
                .pos(3, v3.getX(), v3.getY())
                .uv(0, (int)unpacked.offset(1, 0x0L).get(), (int)unpacked.offset(1, 0x1L).get())
                .uv(1, (int)unpacked.offset(1, 0x4L).get(), (int)unpacked.offset(1, 0x5L).get())
                .uv(2, (int)unpacked.offset(1, 0x8L).get(), (int)unpacked.offset(1, 0x9L).get())
                .uv(3, (int)unpacked.offset(1, 0xcL).get(), (int)unpacked.offset(1, 0xdL).get());

              final long norm0 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
              norm.setX((short)((int)(norm0 << 20) >> 19 & 0xffff_fff8L));
              norm.setY((short)((int)(norm0 << 10) >> 19 & 0xffff_fff8L));
              norm.setZ((short)((int)norm0 >> 19 & 0xffff_fff8L));
              CPU.MTC2(norm.getXY(), 0);
              CPU.MTC2(norm.getZ(),  1);

              final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x14L).get() * 0x4L).get();
              norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fff8L));
              norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fff8L));
              norm.setZ((short)((int)norm1 >> 19 & 0xffff_fff8L));
              CPU.MTC2(norm.getXY(), 2);
              CPU.MTC2(norm.getZ(),  3);

              final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x18L).get() * 0x4L).get();
              norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fff8L));
              norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fff8L));
              norm.setZ((short)((int)norm2 >> 19 & 0xffff_fff8L));
              CPU.MTC2(norm.getXY(), 4);
              CPU.MTC2(norm.getZ(),  5);

              CPU.COP2(0x118043fL);

              cmd
                .rgb(0, (int)CPU.MFC2(20))
                .rgb(1, (int)CPU.MFC2(21))
                .rgb(2, (int)CPU.MFC2(22));

              final long norm3 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x1cL).get() * 0x4L).get();
              norm.setX((short)((int)(norm3 << 20) >> 19 & 0xffff_fffcL));
              norm.setY((short)((int)(norm3 << 10) >> 19 & 0xffff_fffcL));
              norm.setZ((short)((int)norm3 >> 19 & 0xffff_fffcL));
              CPU.MTC2(norm.getXY(), 0);
              CPU.MTC2(norm.getZ(),  1);

              CPU.COP2(0x108041bL);

              cmd.rgb(3, (int)CPU.MFC2(22));

              GPU.queueCommand(z, cmd);
            }
          }
        }
      }

      //LAB_800e2fc0
    }

    tmp.release();

    //LAB_800e2fc8
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** LSC 3 VERTEX GOURAUD TEXTURED 2 SEMI TRANS */
  @Method(0x800e300cL)
  public static long FUN_800e300c(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x18);
    final Value unpacked = tmp.get();

    CPU.MTC2(0x808080, 6);

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    //LAB_800e30c4
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x18);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0xeL).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x12L).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x16L).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e3304
            final int clut = (int)unpacked.offset(2, 0x2L).get();
            final int tpage = (int)unpacked.offset(2, 0x6L).get() & 0xff9f | tmdGp0Tpage_1f8003ec.get();

            final GpuCommandPoly cmd = new GpuCommandPoly(3)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .translucent(Translucency.of(tpage >>> 5 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .uv(0, (int)unpacked.offset(1, 0x0L).get(), (int)unpacked.offset(1, 0x1L).get())
              .uv(1, (int)unpacked.offset(1, 0x4L).get(), (int)unpacked.offset(1, 0x5L).get())
              .uv(2, (int)unpacked.offset(1, 0x8L).get(), (int)unpacked.offset(1, 0x9L).get());

            // Normals encoding:
            // zzzzzzzzzz yyyyyyyyyy xxxxxxxxxx uu
            // u = unused
            // Each component is signed and multiplied by 8
            final long norm0 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0xcL).get() * 0x4L).get();
            norm.setX((short)((int)(norm0 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm0 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm0 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);

            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 2);
            CPU.MTC2(norm.getZ(),  3);

            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x14L).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fff8L));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fff8L));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fff8L));
            CPU.MTC2(norm.getXY(), 4);
            CPU.MTC2(norm.getZ(),  5);

            CPU.COP2(0x118043fL);

            cmd
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e342c
    }

    tmp.release();

    //LAB_800e3434
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** NLSC 4 VERTEX GRADATION TEXTURED SEMI TRANS */
  @Method(0x800e3478L)
  public static long FUN_800e3478(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x28);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();

    //LAB_800e351c
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x28);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x20L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0); // Vert XY0
      CPU.MTC2(vert.getZ(),  1); // Vert Z0

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x22L).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2); // Vert XY1
      CPU.MTC2(vert.getZ(),  3); // Vert Z1

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x24L).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4); // Vert XY2
      CPU.MTC2(vert.getZ(),  5); // Vert Z2
      CPU.COP2(0x28_0030L); // Perspective transform triple (transform first 3 verts of quad)

      if((int)CPU.CFC2(31) >= 0) { // If no GTE errors
        CPU.COP2(0x140_0006L); // Normal clipping

        if(CPU.MFC2(24) != 0) { // If not looking straight on at the edge of the triangle (i.e. the triangle is visible)
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12)); // Screen XY0
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13)); // Screen XY1
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14)); // Screen XY2

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x26L).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0); // Vert XY0
          CPU.MTC2(vert.getZ(),  1); // Vert Z0
          CPU.COP2(0x18_0001L); // Perspective transform single (transform last vert of quad)

          if((int)CPU.CFC2(31) >= 0) { // No GTE errors
            final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14)); // XY3

            CPU.COP2(0x168_002eL); // Average of four Z values

            final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
            if(z >= 11) { // Probably near clipping
              //LAB_800e3968
              final int clut = (int)unpacked.offset(2, 0x2L).get();
              final int tpage = (int)unpacked.offset(2, 0x6L).get() & 0xff9f | tmdGp0Tpage_1f8003ec.get();

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.of(tpage >>> 7 & 0b11))
                .translucent(Translucency.of(tpage >>> 5 & 0b11))
                .clut((clut & 0b111111) * 16, clut >>> 6)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .pos(0, v0.getX(), v0.getY())
                .pos(1, v1.getX(), v1.getY())
                .pos(2, v2.getX(), v2.getY())
                .pos(3, v3.getX(), v3.getY())
                .uv(0, (int)unpacked.offset(1, 0x0L).get(), (int)unpacked.offset(1, 0x1L).get())
                .uv(1, (int)unpacked.offset(1, 0x4L).get(), (int)unpacked.offset(1, 0x5L).get())
                .uv(2, (int)unpacked.offset(1, 0x8L).get(), (int)unpacked.offset(1, 0x9L).get())
                .uv(3, (int)unpacked.offset(1, 0xcL).get(), (int)unpacked.offset(1, 0xdL).get())
                .rgb(0, (int)unpacked.offset(1, 0x10L).get() * r >> 12, (int)unpacked.offset(1, 0x11L).get() * g >> 12, (int)unpacked.offset(1, 0x12L).get() * b >> 12)
                .rgb(1, (int)unpacked.offset(1, 0x14L).get() * r >> 12, (int)unpacked.offset(1, 0x15L).get() * g >> 12, (int)unpacked.offset(1, 0x16L).get() * b >> 12)
                .rgb(2, (int)unpacked.offset(1, 0x18L).get() * r >> 12, (int)unpacked.offset(1, 0x19L).get() * g >> 12, (int)unpacked.offset(1, 0x1aL).get() * b >> 12)
                .rgb(3, (int)unpacked.offset(1, 0x1cL).get() * r >> 12, (int)unpacked.offset(1, 0x1dL).get() * g >> 12, (int)unpacked.offset(1, 0x1eL).get() * b >> 12);

              GPU.queueCommand(z, cmd);
            }
          }
        }
      }

      //LAB_800e399c
    }

    tmp.release();

    //LAB_800e39a4
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** NLSC 3 VERTEX GRADATION TEXTURED SEMI TRANS */
  @Method(0x800e39e8L)
  public static long FUN_800e39e8(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x20);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();

    //LAB_800e3a8c
    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(unpacked.getAddress(), primitives, 0x20);

      final BVEC4 loV0 = vertices.get((int)unpacked.offset(2, 0x18L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)unpacked.offset(2, 0x1aL).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)unpacked.offset(2, 0x1cL).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          if((int)CPU.CFC2(31) >= 0) {
            CPU.COP2(0x158002dL);

            final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
            if(z >= 0xb) {
              //LAB_800e3dec
              final int clut = (int)unpacked.offset(2, 0x2L).get();
              final int tpage = (int)unpacked.offset(2, 0x6L).get() & 0xff9f | tmdGp0Tpage_1f8003ec.get();

              final GpuCommandPoly cmd = new GpuCommandPoly(3)
                .bpp(Bpp.of(tpage >>> 7 & 0b11))
                .translucent(Translucency.of(tpage >>> 5 & 0b11))
                .clut((clut & 0b111111) * 16, clut >>> 6)
                .vramPos((tpage & 0b1111) * 64, (clut & 0b10000) != 0 ? 256 : 0)
                .pos(0, v0.getX(), v0.getY())
                .pos(1, v1.getX(), v1.getY())
                .pos(2, v2.getX(), v2.getY())
                .uv(0, (int)unpacked.offset(1, 0x0L).get(), (int)unpacked.offset(1, 0x1L).get())
                .uv(1, (int)unpacked.offset(1, 0x4L).get(), (int)unpacked.offset(1, 0x5L).get())
                .uv(2, (int)unpacked.offset(1, 0x8L).get(), (int)unpacked.offset(1, 0x9L).get())
                .rgb(0, (int)unpacked.offset(1, 0x0cL).get() * r >> 12, (int)unpacked.offset(1, 0x0dL).get() * g >> 12, (int)unpacked.offset(1, 0x0eL).get() * b >> 12)
                .rgb(1, (int)unpacked.offset(1, 0x10L).get() * r >> 12, (int)unpacked.offset(1, 0x11L).get() * g >> 12, (int)unpacked.offset(1, 0x12L).get() * b >> 12)
                .rgb(2, (int)unpacked.offset(1, 0x14L).get() * r >> 12, (int)unpacked.offset(1, 0x15L).get() * g >> 12, (int)unpacked.offset(1, 0x16L).get() * b >> 12);

              GPU.queueCommand(z, cmd);
            }
          }
        }
      }

      //LAB_800e3e20
    }

    tmp.release();

    //LAB_800e3e28
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Render All method **/
  @Method(0x800e3e6cL)
  public static void renderCtmd(final GsDOBJ2 dobj2) {
    final CtmdUnpackingData50 unpackingData = new CtmdUnpackingData50();
    ctmdUnpackingData_800c6920 = unpackingData;
    unpackingData._00 = 0;

    final int mode;
    if((dobj2.attribute_00 & 0x4000_0000) == 0) {
      mode = 0;
    } else {
      mode = 0x12; // Shaded and translucent
    }

    //LAB_800e3eb4
    final TmdObjTable objTable = dobj2.tmd_08;
    final UnboundedArrayRef<SVECTOR> vertices = objTable.vert_top_00.deref();
    final long normals = objTable.normal_top_08.get();
    long primitives = objTable.primitives_10.getPointer();
    long count = objTable.n_primitive_14.get();

    //LAB_800e3ee4
    while(count != 0) {
      unpackingData._0c = 0;
      unpackingData._08 = 0;
      unpackingData._04 = unpackingData._00;

      final long length = MEMORY.ref(2, primitives).get();
      final int command = (int)MEMORY.ref(4, primitives).get();

      tmdGp0CommandId_1f8003ee.set(command >>> 24 & 0x3e | mode);
      final int index = command >>> 14 & 0x20 | command >>> 24 & 0xf | command >>> 18 & 0x1 | mode;
      primitives = ctmdRenderers_800fadbc.get(index).deref().run(primitives, vertices, normals, length);
      count -= length;
    }

    ctmdUnpackingData_800c6920 = null;

    //LAB_800e3f64
  }

  @Method(0x800e3f88L)
  public static long FUN_800e3f88(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    //LAB_800e4008
    for(int i = 0; i < count; i++) {
      final GpuCommandPoly cmd = new GpuCommandPoly(3);

      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0aL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800e4088
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          cmd
            .pos(0, v0.getX(), v0.getY())
            .pos(1, v1.getX(), v1.getY())
            .pos(2, v2.getX(), v2.getY());
          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e40d0
            CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6);
            final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x08L).get() * 0x8L;
            final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x0cL).get() * 0x8L;
            final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5);
            CPU.COP2(0x118043fL);

            final int tpage = tmdGp0Tpage_1f8003ec.get();

            cmd
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e415c
      primitives += 0x14L;
    }

    //LAB_800e4164
    return primitives;
  }

  @Method(0x800e4184L)
  public static long FUN_800e4184(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

    //LAB_800e41e0
    //LAB_800e41e4
    CPU.MTC2(0x808080, 6);

    //LAB_800e4220
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2L) != 0 && winding != 0) {
          //LAB_800e42c0
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e4308
            final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
            final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
            final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5);
            CPU.COP2(0x118043fL);

            final int clut = (int)MEMORY.ref(2, primitives).offset(0x6L).get();
            final int tpage = (int)MEMORY.ref(2, primitives).offset(0xaL).get();

            final GpuCommandPoly cmd = new GpuCommandPoly(3)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .uv(0, (int)MEMORY.ref(1, primitives).offset(0x4L).get(), (int)MEMORY.ref(1, primitives).offset(0x5L).get())
              .uv(1, (int)MEMORY.ref(1, primitives).offset(0x8L).get(), (int)MEMORY.ref(1, primitives).offset(0x9L).get())
              .uv(2, (int)MEMORY.ref(1, primitives).offset(0xcL).get(), (int)MEMORY.ref(1, primitives).offset(0xdL).get())
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e4380
      primitives += 0x1cL;
    }

    //LAB_800e438c
    return primitives;
  }

  @Method(0x800e43a8L)
  public static long FUN_800e43a8(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    CPU.MTC2(0x808080, 6); //TODO ???

    //LAB_800e443c
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e451c
            final int clut = (int)MEMORY.ref(2, primitives).offset(0x6L).get();
            final int tpage = (int)MEMORY.ref(2, primitives).offset(0xaL).get() & 0xff9f | tmdGp0Tpage_1f8003ec.get();

            final GpuCommandPoly cmd = new GpuCommandPoly(3)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .translucent(Translucency.of(tpage >>> 5 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .uv(0, (int)MEMORY.ref(1, primitives).offset(0x4L).get(), (int)MEMORY.ref(1, primitives).offset(0x5L).get())
              .uv(1, (int)MEMORY.ref(1, primitives).offset(0x8L).get(), (int)MEMORY.ref(1, primitives).offset(0x9L).get())
              .uv(2, (int)MEMORY.ref(1, primitives).offset(0xcL).get(), (int)MEMORY.ref(1, primitives).offset(0xdL).get());

            final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
            final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
            final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5);
            CPU.COP2(0x118043fL);

            cmd
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800e4594
      primitives += 0x1cL;
    }

    //LAB_800e45a0
    return primitives;
  }

  @Method(0x800e45c0L)
  public static void FUN_800e45c0(final SVECTOR a0, final VECTOR a1) {
    final int angle = ratan2(a1.getX(), a1.getZ());
    a0.setX((short)ratan2(-a1.getY(), (rcos(-angle) * a1.getZ() - rsin(-angle) * a1.getX()) / 0x1000));
    a0.setY((short)angle);
    a0.setZ((short)0);
  }

  @Method(0x800e4674L)
  public static VECTOR FUN_800e4674(final VECTOR out, final SVECTOR rotation) {
    final MATRIX rotMatrix = new MATRIX();
    RotMatrix_80040010(rotation, rotMatrix);
    SetRotMatrix(rotMatrix);
    ApplyRotMatrix(new SVECTOR().set((short)0, (short)0, (short)(1 << 12)), out);
    return out;
  }

  @Method(0x800e46c8L)
  public static void resetLights() {
    final BattleLightStruct64 v1 = _800c6930;
    v1.colour_00.set(0x800, 0x800, 0x800);

    final BttlLightStruct84 a0 = lights_800c692c[0];
    a0.light_00.direction_00.set(0, 1 << 12, 0);
    a0.light_00.r_0c.set(0x80);
    a0.light_00.g_0d.set(0x80);
    a0.light_00.b_0e.set(0x80);
    a0._10._00 = 0;
    a0._4c._00 = 0;

    //LAB_800e4720
    lights_800c692c[1].clear();
    lights_800c692c[2].clear();
  }

  @Method(0x800e473cL)
  public static FlowControl scriptResetLights(final RunningScript<?> script) {
    resetLights();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e475cL)
  public static void setLightDirection(final int lightIndex, final int x, final int y, final int z) {
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.direction_00.set(x, y, z);
    light._10._00 = 0;
  }

  @Method(0x800e4788L)
  public static FlowControl scriptSetLightDirection(final RunningScript<?> script) {
    setLightDirection(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e47c8L)
  public static FlowControl scriptGetLightDirection(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    script.params_20[1].set(light.light_00.direction_00.getX());
    script.params_20[2].set(light.light_00.direction_00.getY());
    script.params_20[3].set(light.light_00.direction_00.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4824L)
  public static void FUN_800e4824(final int lightIndex, final int x, final int y, final int z) {
    final VECTOR sp0x18 = new VECTOR();
    final SVECTOR sp0x10 = new SVECTOR().set((short)x, (short)y, (short)z);
    FUN_800e4674(sp0x18, sp0x10);
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.direction_00.set(sp0x18);
    light._10._00 = 0;
  }

  @Method(0x800e48a8L)
  public static FlowControl FUN_800e48a8(final RunningScript<?> script) {
    FUN_800e4824(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e48e8L)
  public static FlowControl FUN_800e48e8(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, lights_800c692c[script.params_20[0].get()].light_00.direction_00);
    script.params_20[1].set(sp0x10.getX());
    script.params_20[2].set(sp0x10.getY());
    script.params_20[3].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4964L)
  public static FlowControl FUN_800e4964(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();

    final int a2 = script.params_20[1].get();
    if(a2 != -1) {
      //LAB_800e49c0
      if(a2 - 1 < 3) {
        FUN_800e45c0(sp0x10, lights_800c692c[a2 - 1].light_00.direction_00);
      } else {
        //LAB_800e49f4
        final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[a2].innerStruct_00;
        sp0x10.setX(bobj.model_148.coord2Param_64.rotate.getX());
        sp0x10.setZ(bobj.model_148.coord2Param_64.rotate.getZ());
      }
    }

    //LAB_800e4a34
    //LAB_800e4a38
    final VECTOR sp0x18 = new VECTOR();
    sp0x10.x.add((short)script.params_20[2].get());
    sp0x10.y.add((short)script.params_20[3].get());
    sp0x10.z.add((short)script.params_20[4].get());
    FUN_800e4674(sp0x18, sp0x10);
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    light.light_00.direction_00.set(sp0x18);
    light._10._00 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4abcL)
  public static FlowControl FUN_800e4abc(final RunningScript<?> script) {
    final int s1 = script.params_20[1].get();

    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, lights_800c692c[script.params_20[0].get()].light_00.direction_00);

    final SVECTOR s0;
    if(s1 - 1 < 3) {
      s0 = new SVECTOR();
      FUN_800e45c0(s0, lights_800c692c[s1 - 1].light_00.direction_00);
    } else {
      //LAB_800e4b40
      final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[s1].innerStruct_00;
      s0 = bobj.model_148.coord2Param_64.rotate;
    }

    //LAB_800e4b64
    script.params_20[1].set(sp0x10.getX() - s0.getX());
    script.params_20[2].set(sp0x10.getY() - s0.getY());
    script.params_20[3].set(sp0x10.getZ() - s0.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4bc0L)
  public static void FUN_800e4bc0(final int lightIndex, final int r, final int g, final int b) {
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.r_0c.set(r);
    light.light_00.g_0d.set(g);
    light.light_00.b_0e.set(b);
    light._4c._00 = 0;
  }

  @Method(0x800e4c10L)
  public static FlowControl FUN_800e4c10(final RunningScript<?> script) {
    FUN_800e4bc0(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4c90L)
  public static FlowControl FUN_800e4c90(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    script.params_20[1].set(light.light_00.r_0c.get());
    script.params_20[2].set(light.light_00.g_0d.get());
    script.params_20[3].set(light.light_00.b_0e.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4cf8L)
  public static void FUN_800e4cf8(final int r, final int g, final int b) {
    final BattleLightStruct64 v0 = _800c6930;
    v0.colour_00.set(r, g, b);
    v0._24 = 0;
    GsSetAmbient(r, g, b);
  }

  @Method(0x800e4d2cL)
  public static FlowControl FUN_800e4d2c(final RunningScript<?> script) {
    FUN_800e4cf8(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    _800c6930._24 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4d74L)
  public static void getLightColour(final IntRef r, final IntRef g, final IntRef b) {
    final BattleLightStruct64 light = _800c6930;
    r.set(light.colour_00.getX());
    g.set(light.colour_00.getY());
    b.set(light.colour_00.getZ());
  }

  @Method(0x800e4db4L)
  public static FlowControl scriptGetLightColour(final RunningScript<?> script) {
    final BattleLightStruct64 v0 = _800c6930;
    script.params_20[0].set(v0.colour_00.getX());
    script.params_20[1].set(v0.colour_00.getY());
    script.params_20[2].set(v0.colour_00.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4dfcL)
  public static FlowControl FUN_800e4dfc(final RunningScript<?> script) {
    lights_800c692c[script.params_20[0].get()]._10._00 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4e2cL)
  public static FlowControl FUN_800e4e2c(final RunningScript<?> script) {
    return lights_800c692c[script.params_20[0].get()]._10._00 != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @Method(0x800e4e64L)
  public static FlowControl FUN_800e4e64(final RunningScript<?> script) {
    script.params_20[1].set(lights_800c692c[script.params_20[0].get()]._10._00);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4ea0L)
  public static FlowControl FUN_800e4ea0(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final int t1 = script.params_20[4].get();
    final BttlLightStruct84Sub38 t0 = light._10;

    t0._00 = 0;
    t0.vec_04.setX(light.light_00.direction_00.getX() << 12);
    t0.vec_04.setY(light.light_00.direction_00.getY() << 12);
    t0.vec_04.setZ(light.light_00.direction_00.getZ() << 12);
    t0.vec_28.setX(script.params_20[1].get() << 12);
    t0.vec_28.setY(script.params_20[2].get() << 12);
    t0.vec_28.setZ(script.params_20[3].get() << 12);
    t0._34 = t1;

    if(t1 > 0) {
      t0.vec_10.setX((t0.vec_28.getX() - t0.vec_04.getX()) / t1);
      t0.vec_10.setY((t0.vec_28.getY() - t0.vec_04.getY()) / t1);
      t0.vec_10.setZ((t0.vec_28.getZ() - t0.vec_04.getZ()) / t1);
      t0.vec_1c.set(0, 0, 0);
      t0._00 = 0xa001;
    }

    //LAB_800e4f98
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4fa0L)
  public static FlowControl FUN_800e4fa0(final RunningScript<?> script) {
    final int s3 = script.params_20[1].get();
    final int s4 = script.params_20[2].get();
    final int s2 = script.params_20[3].get();
    final int s5 = script.params_20[4].get();

    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);
    light._10._00 = 0;

    final BttlLightStruct84Sub38 a3 = light._10;
    a3.vec_04.set(sp0x10);
    a3.vec_28.set(s3, s4, s2);
    a3._34 = s5;

    if(s5 > 0) {
      a3.vec_1c.set(0, 0, 0);
      a3.vec_10.setX((s3 - a3.vec_04.getX()) / s5);
      a3.vec_10.setY((s4 - a3.vec_04.getY()) / s5);
      a3.vec_10.setZ((s2 - a3.vec_04.getZ()) / s5);
      a3._00 = 0xc001;
    }

    //LAB_800e50c0
    //LAB_800e50c4
    return FlowControl.CONTINUE;
  }

  @Method(0x800e50e8L)
  public static FlowControl FUN_800e50e8(final RunningScript<?> script) {
    final int s3 = script.params_20[0].get();
    final int s2 = script.params_20[1].get();
    final int x = script.params_20[2].get();
    final int y = script.params_20[3].get();
    final int z = script.params_20[4].get();
    final int s4 = script.params_20[5].get();

    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, lights_800c692c[s3].light_00.direction_00);

    final BttlLightStruct84Sub38 s0 = lights_800c692c[s3]._10;
    s0._00 = 0;
    s0.vec_04.set(sp0x10);

    if(s2 - 1 < 2) {
      final SVECTOR sp0x18 = new SVECTOR();
      FUN_800e45c0(sp0x18, lights_800c692c[s2 - 1].light_00.direction_00);
      s0.vec_28.set(sp0x18);
    } else {
      //LAB_800e51e8
      final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[s2].innerStruct_00;
      final SVECTOR v0 = bobj.model_148.coord2Param_64.rotate;
      s0.vec_28.set(v0);
    }

    //LAB_800e522c
    s0._34 = s4;
    s0.vec_28.add(x, y, z);

    if(s4 > 0) {
      s0._00 = 0xc001;
      s0.vec_10.set(s0.vec_28).sub(s0.vec_04).div(s4);
      s0.vec_1c.set(0, 0, 0);
    }

    //LAB_800e52c8
    //LAB_800e52cc
    return FlowControl.CONTINUE;
  }

  @Method(0x800e52f8L)
  public static FlowControl FUN_800e52f8(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub38 v1 = light._10;
    v1._00 = 0x4001;
    v1.vec_04.set(sp0x10.getX() << 12, sp0x10.getY() << 12, sp0x10.getZ() << 12);
    v1.vec_10.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    v1.vec_1c.set(script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e540cL)
  public static FlowControl FUN_800e540c(final RunningScript<?> script) {
    final int bobjIndex = script.params_20[1].get();
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];

    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub38 a0_0 = light._10;
    a0_0._00 = 0x4002;
    light.scriptIndex_48 = bobjIndex;

    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[bobjIndex].innerStruct_00;
    a0_0.vec_04.set(sp0x10).sub(bobj.model_148.coord2Param_64.rotate);
    a0_0.vec_10.set(0, 0, 0);
    a0_0.vec_1c.set(0, 0, 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e54f8L)
  public static FlowControl FUN_800e54f8(final RunningScript<?> script) {
    lights_800c692c[script.params_20[0].get()]._4c._00 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5528L)
  public static FlowControl FUN_800e5528(final RunningScript<?> script) {
    return lights_800c692c[script.params_20[0].get()]._4c._00 != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @Method(0x800e5560L)
  public static FlowControl FUN_800e5560(final RunningScript<?> script) {
    script.params_20[1].set(lights_800c692c[script.params_20[0].get()]._4c._00);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e559cL)
  public static FlowControl FUN_800e559c(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final int t1 = script.params_20[4].get();
    final BttlLightStruct84Sub38 t0 = light._4c;

    t0._00 = 0;
    t0.vec_04.setX(light.light_00.r_0c.get() << 12);
    t0.vec_04.setY(light.light_00.g_0d.get() << 12);
    t0.vec_04.setZ(light.light_00.b_0e.get() << 12);
    t0.vec_28.set(script.params_20[1].get() << 12, script.params_20[2].get() << 12, script.params_20[3].get() << 12);
    t0._34 = t1;

    if(t1 > 0) {
      t0.vec_1c.set(0, 0, 0);
      t0.vec_10.set(t0.vec_28).sub(t0.vec_04).div(t1);
      t0._00 = 0x8001;
    }

    //LAB_800e5694
    return FlowControl.CONTINUE;
  }

  @Method(0x800e569cL)
  public static FlowControl FUN_800e569c(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final BttlLightStruct84Sub38 v1 = light._4c;
    v1._00 = 0;
    v1.vec_04.set(light.light_00.r_0c.get() << 12, light.light_00.g_0d.get() << 12, light.light_00.b_0e.get() << 12);
    v1.vec_10.set(script.params_20[1].get() << 12, script.params_20[2].get() << 12, script.params_20[3].get() << 12);
    v1.vec_1c.set(script.params_20[4].get() << 12, script.params_20[5].get() << 12, script.params_20[6].get() << 12);

    if(v1._34 > 0) {
      v1._00 = 0x1;
    }

    //LAB_800e5760
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5768L)
  public static void FUN_800e5768(final BattleStruct4c struct4c) {
    FUN_800e4cf8(struct4c.ambientColour_00.getX(), struct4c.ambientColour_00.getY(), struct4c.ambientColour_00.getZ());

    final BattleLightStruct64 v1 = _800c6930;
    if(struct4c._0e > 0) {
      v1.colour_0c.set(struct4c.ambientColour_00);
      v1._18.set(struct4c._06, struct4c._08, struct4c._0a);
      v1._24 = 3;
      v1._2c = (short)struct4c._0c;
      v1._2e = (short)struct4c._0e;
    } else {
      //LAB_800e5808
      v1._24 = 0;
    }

    //LAB_800e5814
    //LAB_800e5828
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 a1 = lights_800c692c[i];
      final BattleStruct14 a0 = struct4c._10[i];
      a1.light_00.direction_00.set(a0.lightDirection_00);
      a1.light_00.r_0c.set(a0.lightColour_0a.getR());
      a1.light_00.g_0d.set(a0.lightColour_0a.getG());
      a1.light_00.b_0e.set(a0.lightColour_0a.getB());

      if((a0._06 | a0._08) != 0) {
        a1._10._00 = 0x3;
        a1._10.vec_04.set(a1.light_00.direction_00);
        a1._10.vec_10.setX(a0._06);
        a1._10.vec_1c.setZ(a0._08);
        a1._10.vec_28.setX(0);
      } else {
        //LAB_800e58cc
        a1._10._00 = 0;
      }

      //LAB_800e58d0
      if(a0._12 != 0) {
        a1._4c._00 = 0x3;
        a1._4c.vec_04.set(a1.light_00.r_0c.get(), a1.light_00.g_0d.get(), a1.light_00.b_0e.get());
        a1._4c.vec_10.set(a0._0d.getR(), a0._0d.getG(), a0._0d.getB());
        a1._4c.vec_28.setX(a0._10);
        a1._4c.vec_28.setY(a0._12);
      } else {
        //LAB_800e5944
        a1._4c._00 = 0;
      }

      //LAB_800e5948
    }
  }

  @Method(0x800e596cL)
  public static FlowControl FUN_800e596c(final RunningScript<?> script) {
    final int v0 = currentStage_800c66a4.get() - 0x47;

    if(v0 >= 0 && v0 < 0x8) {
      FUN_800e5768(deffManager_800c693c._98[v0]);
    } else {
      //LAB_800e59b0
      FUN_800e5768(deffManager_800c693c._4c);
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x800e59d8L)
  public static FlowControl FUN_800e59d8(final RunningScript<?> script) {
    final int a0 = script.params_20[0].get();

    if(a0 == -1) {
      deffManager_800c693c._4c.set(script.params_20[1]);
    } else if(a0 == -2) {
      //LAB_800e5a38
      //LAB_800e5a60
      FUN_800e5768(new BattleStruct4c().set(script.params_20[1]));
      //LAB_800e5a14
    } else if(a0 == -3) {
      //LAB_800e5a40
      FUN_800e5768(deffManager_800c693c._98[script.params_20[1].get()]);
    }

    //LAB_800e5a68
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5a78L)
  public static void FUN_800e5a78(final ScriptState<Void> state, final Void struct) {
    final BattleLightStruct64 light1 = _800c6930;

    _800c6928.addu(0x1L);

    if(light1._24 == 3) {
      final int angle = rcos(((_800c6928.get() + light1._2c) % light1._2e << 12) / light1._2e);
      final int a2 = 0x1000 - angle;
      final int a3 = 0x1000 + angle;
      light1.colour_00.setX((light1.colour_0c.getX() * a3 + light1._18.getX() * a2) / 0x2000);
      light1.colour_00.setY((light1.colour_0c.getY() * a3 + light1._18.getY() * a2) / 0x2000);
      light1.colour_00.setZ((light1.colour_0c.getZ() * a3 + light1._18.getZ() * a2) / 0x2000);
    }

    //LAB_800e5b98
    //LAB_800e5ba0
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 light = lights_800c692c[i];
      final BttlLightStruct84Sub38 a2 = light._10;

      int v1 = a2._00 & 0xff;
      if(v1 == 0x1) {
        //LAB_800e5c50
        a2.vec_10.add(a2.vec_1c);
        a2.vec_04.add(a2.vec_10);

        if((a2._00 & 0x8000) != 0) {
          a2._34--;

          if(a2._34 <= 0) {
            a2._00 = 0;
            a2.vec_04.set(a2.vec_28);
          }
        }

        //LAB_800e5cf4
        v1 = a2._00;

        if((v1 & 0x2000) != 0) {
          light.light_00.direction_00.set(a2.vec_04).div(0x1000);
          //LAB_800e5d40
        } else if((v1 & 0x4000L) != 0) {
          final SVECTOR sp0x18 = new SVECTOR();
          sp0x18.set(a2.vec_04);
          FUN_800e4674(light.light_00.direction_00, sp0x18);
        }
      } else if(v1 == 0x2L) {
        //LAB_800e5bf0
        final SVECTOR sp0x10 = new SVECTOR();
        final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[light.scriptIndex_48].innerStruct_00;
        sp0x10.set(bobj.model_148.coord2Param_64.rotate).add(a2.vec_04);
        FUN_800e4674(light.light_00.direction_00, sp0x10);
      } else if(v1 == 0x3L) {
        //LAB_800e5bdc
        //LAB_800e5d6c
        final SVECTOR sp0x18 = new SVECTOR();

        v1 = (int)(_800c6928.get() & 0xfff);
        sp0x18.setX((short)(a2.vec_04.getX() + a2.vec_10.getX() * v1));
        sp0x18.setY((short)(a2.vec_04.getY() + a2.vec_10.getY() * v1));
        sp0x18.setZ((short)(a2.vec_04.getZ() + a2.vec_10.getZ() * v1));

        //LAB_800e5dcc
        FUN_800e4674(light.light_00.direction_00, sp0x18);
      }

      //LAB_800e5dd4
      final BttlLightStruct84Sub38 s0 = light._4c;
      v1 = s0._00 & 0xff;
      if(v1 == 0x1L) {
        //LAB_800e5df4
        s0.vec_10.set(s0.vec_1c);
        s0.vec_04.add(s0.vec_10);

        if((s0._00 & 0x8000) != 0) {
          s0._34--;

          if(s0._34 <= 0) {
            s0._00 = 0;
            s0.vec_04.set(s0.vec_28);
          }
        }

        //LAB_800e5e90
        lights_800c692c[i].light_00.r_0c.set(s0.vec_04.getX() >> 12);
        lights_800c692c[i].light_00.g_0d.set(s0.vec_04.getY() >> 12);
        lights_800c692c[i].light_00.b_0e.set(s0.vec_04.getZ() >> 12);
      } else if(v1 == 0x3L) {
        //LAB_800e5ed0
        final short theta = rcos(((_800c6928.get() + s0.vec_28.getX()) % s0.vec_28.getY() << 12) / s0.vec_28.getY());
        final int a3_0 = theta + 0x1000;
        final int a2_0 = 0x1000 - theta;
        lights_800c692c[i].light_00.r_0c.set((s0.vec_04.getX() * a3_0 + s0.vec_10.getX() * a2_0) / 0x2000);
        lights_800c692c[i].light_00.g_0d.set((s0.vec_04.getY() * a3_0 + s0.vec_10.getY() * a2_0) / 0x2000);
        lights_800c692c[i].light_00.b_0e.set((s0.vec_04.getZ() * a3_0 + s0.vec_10.getZ() * a2_0) / 0x2000);
      }

      //LAB_800e5fb8
      //LAB_800e5fbc
    }
  }

  @Method(0x800e5fe8L)
  public static void FUN_800e5fe8(final ScriptState<Void> state, final Void struct) {
    //LAB_800e6008
    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, lights_800c692c[i].light_00);
    }

    final BattleLightStruct64 v0 = _800c6930;
    GsSetAmbient(v0.colour_00.getX(), v0.colour_00.getY(), v0.colour_00.getZ());
    projectionPlaneDistance_1f8003f8.set(getProjectionPlaneDistance());
  }

  @Method(0x800e6070L)
  public static void FUN_800e6070() {
    final ScriptState<Void> state = SCRIPTS.allocateScriptState(1, null, 0, null);
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(Bttl_800e::FUN_800e5a78);
    state.setRenderer(Bttl_800e::FUN_800e5fe8);
    _800c6930._60 = 0;
    resetLights();
  }

  @Method(0x800e60e0L)
  public static void FUN_800e60e0(final int r, final int g, final int b) {
    final BattleLightStruct64 v1 = _800c6930;
    final VECTOR s0 = v1._30[v1._60];

    getLightColour(s0.x, s0.y, s0.z);

    v1.colour_00.set(r, g, b);
    v1._60 = v1._60 + 1 & 3;
  }

  @Method(0x800e6170L)
  public static void FUN_800e6170() {
    final BattleLightStruct64 a0 = _800c6930;
    a0._60 = a0._60 - 1 & 3;
    a0.colour_00.set(a0._30[a0._60]);
  }

  @Method(0x800e61e4L)
  public static void FUN_800e61e4(final int r, final int g, final int b) {
    GsSetFlatLight(0, light_800c6ddc);
    GsSetFlatLight(1, light_800c6ddc);
    GsSetFlatLight(2, light_800c6ddc);
    FUN_800e60e0(r, g, b);

    final BattleLightStruct64 v0 = _800c6930;
    GsSetAmbient(v0.colour_00.getX(), v0.colour_00.getY(), v0.colour_00.getZ());
  }

  @Method(0x800e62a8L)
  public static void FUN_800e62a8() {
    FUN_800e6170();

    final BattleLightStruct64 v0 = _800c6930;
    GsSetAmbient(v0.colour_00.getX(), v0.colour_00.getY(), v0.colour_00.getZ());

    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, lights_800c692c[i].light_00);
    }
  }

  @Method(0x800e6314L)
  public static void FUN_800e6314(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final DeffManager7cc struct7cc = deffManager_800c693c;

    if(struct7cc != null) {
      free(struct7cc.deffPackage_5a8.getAddress());
      struct7cc.deffPackage_5a8 = null;
    }

    decrementOverlayCount();
    _800fafe8.setu(0x4L);

    if((struct7cc._20 & 0x4_0000) != 0) {
      FUN_8001d068(_800c6938.bobjState_04, 1);
    }

    if((struct7cc._20 & 0x10_0000) != 0) {
      //LAB_800e63d0
      for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
        final CombatantStruct1a8 v1 = getCombatant(i);
        if((v1.flags_19e & 0x1) != 0 && v1.charIndex_1a2 >= 0) {
          loadAttackAnimations(i);
        }

        //LAB_800e6408
      }
    }

    //LAB_800e641c
    if((struct7cc._20 & 0x60_0000) != 0) {
      FUN_80115cac(0);
    }

    //LAB_800e6444
    struct7cc._20 &= 0xff80_ffff;
  }

  @Method(0x800e6470L)
  public static ScriptState<EffectManagerData6c> FUN_800e6470(final RunningScript<? extends BattleScriptDataBase> script) {
    final int t0 = script.params_20[0].get();
    final DeffManager7cc struct7cc = deffManager_800c693c;
    struct7cc._20 |= t0 & 0x1_0000 | t0 & 0x2_0000 | t0 & 0x10_0000;

    if((struct7cc._20 & 0x10_0000) != 0) {
      //LAB_800e651c
      for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
        final CombatantStruct1a8 v1 = getCombatant(i);

        if((v1.flags_19e & 0x1) != 0 && v1.mrg_04 != null && v1.charIndex_1a2 >= 0) {
          FUN_800ca418(i);
        }

        //LAB_800e6564
      }
    }

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0,
      Bttl_800e::FUN_800e70bc,
      null,
      Bttl_800e::FUN_800e6314,
      null
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager.flags_04 = 0x600_0400;

    final BattleStruct24_2 v0 = _800c6938;
    v0._00 = t0 & 0xffff;
    v0.bobjState_04 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()];
    v0._08 = script.params_20[2].get();
    v0.scriptIndex_0c = script.scriptState_04.index;
    v0.scriptOffsetIndex_10 = script.params_20[3].get() & 0xff;
    v0.managerState_18 = state;
    v0._1c = 0;
    v0.frameCount_20 = -1;
    loadSupportOverlay(3, Bttl_800e::FUN_800e704c);
    return state;
  }

  @Method(0x800e665cL)
  public static long FUN_800e665c(final RunningScript<? extends BattleScriptDataBase> script) {
    final int s3 = script.params_20[0].get() & 0xffff;
    final int s1 = script.params_20[3].get() & 0xff;

    final DeffManager7cc deffManager = deffManager_800c693c;
    deffManager._20 |= _800fafec.offset(s3).get() << 16;
    FUN_800e6470(script);

    final BattleStruct24_2 battle24 = _800c6938;

    battle24._00 |= 0x100_0000;
    if((deffManager._20 & 0x4_0000) != 0) {
      //LAB_800e66fc
      //LAB_800e670c
      FUN_8001d068(battle24.bobjState_04, s3 != 0x2e || s1 != 0 ? 0 : 2);
    }

    //LAB_800e6714
    if(battle24.script_14 != null) {
      battle24.script_14 = null;
    }

    //LAB_800e6738
    for(int i = 0; _800fb040.offset(i).get() != 0xff; i++) {
      if(_800fb040.offset(i).get() == s3) {
        if(Unpacker.isDirectory("SECT/DRGN0.BIN/%d".formatted(4115 + i))) {
          loadDrgnDir(0, 4115 + i, Bttl_800e::FUN_800e929c);
        }
      }

      //LAB_800e679c
    }

    //LAB_800e67b0
    loadDrgnDir(0, 4139 + s3 * 2, Bttl_800e::FUN_800e929c);
    loadDrgnDir(0, 4140 + s3 * 2 + "/0", files -> Bttl_800e.loadDeffPackage(files, battle24.managerState_18));
    loadDrgnFile(0, 4140 + s3 * 2 + "/1", file -> _800c6938.script_14 = new ScriptFile(4140 + s3 * 2 + "/1", file));
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e6844L)
  public static long FUN_800e6844(final RunningScript<? extends BattleScriptDataBase> script) {
    deffManager_800c693c._20 |= 0x40_0000;
    FUN_800e6470(script);
    final int s0 = ((script.params_20[0].get() & 0xffff) - 192) * 2;
    final BattleStruct24_2 t0 = _800c6938;

    if(t0.script_14 != null) {
      t0.script_14 = null;
    }

    t0._00 |= 0x200_0000;
    loadDrgnDir(0, 4307 + s0, Bttl_800e::FUN_800e929c);
    loadDrgnDir(0, 4308 + s0 + "/0", files -> Bttl_800e.loadDeffPackage(files, t0.managerState_18));
    loadDrgnFile(0, 4308 + s0 + "/1", file -> _800c6938.script_14 = new ScriptFile(4308 + s0 + "/1", file));
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e6920L)
  public static long FUN_800e6920(final RunningScript<? extends BattleScriptDataBase> script) {
    final long s1 = script.params_20[0].get() & 0xff_0000L;
    int sp20 = (short)script.params_20[0].get();
    if(sp20 == -1) {
      final BattleObject27c v0 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;
      assert false : "?"; //script.params_20.get(0).set(sp0x20);
      sp20 = getCombatant(v0.combatantIndex_26c).charIndex_1a2;
    }

    //LAB_800e69a8
    deffManager_800c693c._20 |= s1 & 0x10_0000;
    FUN_800e6470(script);

    final BattleStruct24_2 v1 = _800c6938;

    if(v1.script_14 != null) {
      v1.script_14 = null;
    }

    v1._00 |= 0x300_0000;

    if(sp20 < 256) {
      loadDrgnDir(0, 4433 + sp20 * 2, Bttl_800e::FUN_800e929c);
      loadDrgnDir(0, 4434 + sp20 * 2 + "/0", files -> Bttl_800e.loadDeffPackage(files, v1.managerState_18));
      final int finalSp2 = sp20;
      loadDrgnFile(0, 4434 + sp20 * 2 + "/1", file -> _800c6938.script_14 = new ScriptFile(4434 + finalSp2 * 2 + "/1", file));
    } else {
      //LAB_800e6a30
      final long a0_0 = sp20 >>> 4;
      int fileIndex = (int)(_800faec4.offset(2, (a0_0 - 0x100L) * 0x2L).get() + (sp20 & 0xfL));
      if((int)a0_0 >= 0x140L) {
        fileIndex += 117;
      }

      //LAB_800e6a60
      fileIndex = (fileIndex - 1) * 2;
      loadDrgnDir(0, 4945 + fileIndex, Bttl_800e::FUN_800e929c);
      loadDrgnDir(0, 4946 + fileIndex + "/0", files -> Bttl_800e.loadDeffPackage(files, v1.managerState_18));
      final int finalFileIndex = fileIndex;
      loadDrgnFile(0, 4946 + fileIndex + "/1", file -> _800c6938.script_14 = new ScriptFile(4946 + finalFileIndex + "/1", file));
    }

    //LAB_800e6a9c
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e6aecL)
  public static long FUN_800e6aec(final RunningScript<? extends BattleScriptDataBase> script) {
    final int v1 = script.params_20[0].get();
    final int s3 = v1 & 0xffff;

    FUN_800e6470(script);

    final BattleStruct24_2 a0_0 = _800c6938;

    if(a0_0.script_14 != null) {
      a0_0.script_14 = null;
    }

    a0_0._00 |= 0x500_0000;

    //LAB_800e6b5c
    for(int i = 0; _800fb05c.offset(i).get() != 0xff; i++) {
      if(_800fb05c.offset(i).get() == s3) {
        if(Unpacker.isDirectory("SECT/DRGN0.BIN/%d".formatted(5505 + i))) {
          loadDrgnDir(0, 5505 + i, Bttl_800e::FUN_800e929c);
        }
      }

      //LAB_800e6bc0
    }

    //LAB_800e6bd4
    loadDrgnDir(0, 5511 + s3 * 2, Bttl_800e::FUN_800e929c);
    loadDrgnDir(0, 5512 + s3 * 2 + "/0", files -> Bttl_800e.loadDeffPackage(files, a0_0.managerState_18));
    loadDrgnFile(0, 5512 + s3 * 2 + "/1", file -> _800c6938.script_14 = new ScriptFile(5512 + s3 * 2 + "/1", file));

    //LAB_800e6d7c
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e6db4L)
  public static FlowControl FUN_800e6db4(final RunningScript<?> script) {
    final FlowControl flow;
    final long v1;
    switch(script.params_20[0].get() & 0xffff) {
      case 0, 1 -> {
        v1 = _800fafe8.get();
        if(v1 == 0x1L) {
          //LAB_800e6e20
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(v1 == 0x2L) {
          //LAB_800e6e28
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined a2");
        }

        //LAB_800e6e2c
      }

      case 2 -> {
        v1 = _800fafe8.get();
        if(v1 == 0x1L) {
          //LAB_800e6e58
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(v1 == 0x2L) {
          final DeffManager7cc struct7cc = deffManager_800c693c;

          //LAB_800e6e60
          if((struct7cc._20 & 0x20_0000) != 0) {
            FUN_80115cac(1);
          }

          //LAB_800e6e88
          if((struct7cc._20 & 0x40_0000) != 0) {
            FUN_80115cac(3);
          }

          //LAB_800e6eb0
          final BattleStruct24_2 struct24 = _800c6938;
          struct24.managerState_18.loadScriptFile(struct24.script_14, struct24.scriptOffsetIndex_10);
          struct24._1c = 0;
          struct24.frameCount_20 = 0;
          _800fafe8.setu(0x3L);
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined t0");
        }

        //LAB_800e6ee4
      }

      case 3 -> {
        v1 = _800fafe8.get();
        if(v1 == 0x3L) {
          //LAB_800e6f10
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(v1 == 0x4L) {
          //LAB_800e6f18
          _800fafe8.setu(0);
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined a3");
        }

        //LAB_800e6f20
      }

      case 4 -> {
        switch((int)_800fafe8.get()) {
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
            _800fafe8.setu(0);
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

  @Method(0x800e6fb4L)
  public static FlowControl FUN_800e6fb4(final RunningScript<? extends BattleScriptDataBase> script) {
    if(_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e6fec
    //LAB_800e6ff0
    final long v1 = _800fafe8.get();

    //LAB_800e7014
    if(v1 == 0) {
      FUN_800e665c(script);
    }

    if(v1 < 4) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(v1 == 4) {
      //LAB_800e702c
      _800fafe8.setu(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    throw new IllegalStateException("Invalid v1");
  }

  @Method(0x800e704cL)
  public static void FUN_800e704c() {
    _800c6938._1c = 1;
  }

  @Method(0x800e7060L)
  public static void loadDeffPackage(final List<byte[]> files, final ScriptState<EffectManagerData6c> state) {
    final MrgFile mrg = MrgFile.alloc(files);

    deffManager_800c693c.deffPackage_5a8 = mrg;

    FUN_800ea620(mrg, state);
  }

  @Method(0x800e70bcL)
  public static void FUN_800e70bc(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c struct) {
    final BattleStruct24_2 a0 = _800c6938;

    if(a0.frameCount_20 != -1) {
      a0.frameCount_20 += vsyncMode_8007a3b8.get();
    }

    //LAB_800e70fc
    if(a0._1c != 0 && a0.script_14 != null) {
      final DeffManager7cc struct7cc = deffManager_800c693c;

      if((struct7cc._20 & 0x4_0000) == 0 || (getLoadedDrgnFiles() & 0x40L) == 0) {
        //LAB_800e7154
        if((struct7cc._20 & 0x20_0000) != 0) {
          FUN_80115cac(1);
        }

        //LAB_800e7178
        if((struct7cc._20 & 0x40_0000) != 0) {
          FUN_80115cac(3);
        }

        //LAB_800e719c
        state.loadScriptFile(a0.script_14, a0.scriptOffsetIndex_10);
        a0._1c = 0;
        a0.frameCount_20 = 0;
      }
    }

    //LAB_800e71c4
  }

  @Method(0x800e71e4L)
  public static FlowControl FUN_800e71e4(final RunningScript<? extends BattleScriptDataBase> script) {
    if(_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e721c
    //LAB_800e7220
    final long v1 = _800fafe8.get();

    if(v1 < 0x4L) {
      //LAB_800e7244
      if(v1 == 0) {
        FUN_800e6844(script);
      }

      return FlowControl.PAUSE_AND_REWIND;
    }

    if(v1 == 0x4L) {
      //LAB_800e725c
      _800fafe8.setu(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e726c
    throw new RuntimeException("Undefined v0");
  }

  @Method(0x800e727cL)
  public static FlowControl FUN_800e727c(final RunningScript<? extends BattleScriptDataBase> script) {
    if(_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e72b4
    //LAB_800e72b8
    final long v1 = _800fafe8.get();

    //LAB_800e72dc
    if(v1 == 0) {
      FUN_800e6920(script);
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(v1 < 0x4L) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(v1 == 0x4L) {
      //LAB_800e72f4
      _800fafe8.setu(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e7304
    throw new RuntimeException("Undefined v0");
  }

  @Method(0x800e7314L)
  public static FlowControl FUN_800e7314(final RunningScript<? extends BattleScriptDataBase> script) {
    if(_800fafe8.get() != 0) {
      if(script.scriptState_04.index != _800c6938.scriptIndex_0c) {
        return FlowControl.PAUSE_AND_REWIND;
      }
    }

    //LAB_800e734c
    //LAB_800e7350
    final long v1 = _800fafe8.get();

    if(v1 == 4) {
      //LAB_800e738c
      _800fafe8.setu(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e7374
    if(v1 == 0) {
      FUN_800e6aec(script);
    }

    //LAB_800e739c
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x800e73acL)
  public static FlowControl FUN_800e73ac(final RunningScript<? extends BattleScriptDataBase> script) {
    if(_800fafe8.get() != 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final int v1 = script.params_20[4].get();
    if(v1 == 0x100_0000) {
      //LAB_800e7414
      FUN_800e665c(script);
    } else if(v1 == 0x200_0000) {
      //LAB_800e7424
      FUN_800e6844(script);
      //LAB_800e73fc
    } else if(v1 == 0x300_0000 || v1 == 0x400_0000) {
      //LAB_800e7434
      FUN_800e6920(script);
    } else if(v1 == 0x500_0000) {
      //LAB_800e7444
      FUN_800e6aec(script);
    }

    //LAB_800e7450
    //LAB_800e7454
    final EffectManagerData6c manager = _800c6938.managerState_18.innerStruct_00;
    manager.ticker_48 = Bttl_800e::FUN_800e74e0;

    //LAB_800e7480
    return FlowControl.CONTINUE;
  }

  @Method(0x800e7490L)
  public static FlowControl FUN_800e7490(final RunningScript<?> script) {
    script.params_20[0].set((int)_800fafe8.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e74acL)
  public static FlowControl FUN_800e74ac(final RunningScript<?> script) {
    final BattleStruct24_2 struct24 = _800c6938;
    script.params_20[0].set(struct24.bobjState_04.index);
    script.params_20[1].set(struct24._08);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e74e0L)
  public static void FUN_800e74e0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final long v1 = _800fafe8.get();
    final BattleStruct24_2 struct24 = _800c6938;

    if(v1 == 0x1L) {
      //LAB_800e7510
      if(struct24._1c != 0 && struct24.script_14 != null && ((deffManager_800c693c._20 & 0x4_0000) == 0 || (getLoadedDrgnFiles() & 0x40) == 0)) {
        //LAB_800e756c
        _800fafe8.setu(0x2L);
      }
    } else if(v1 == 0x3L) {
      //LAB_800e7574
      if(struct24.frameCount_20 >= 0) {
        struct24.frameCount_20 += vsyncMode_8007a3b8.get();
      }
    }

    //LAB_800e759c
  }

  /** Used in Astral Drain */
  @Method(0x800e75acL)
  public static void FUN_800e75ac(final BattleStruct24 a0, final MATRIX a1) {
    final MATRIX sp0x40 = new MATRIX();
    FUN_8003ec90(worldToScreenMatrix_800c3548, a1, sp0x40);
    final int z = Math.min(0x3ff8, zOffset_1f8003e8.get() + sp0x40.transfer.getZ() / 4);

    if(z >= 40) {
      //LAB_800e7610
      CPU.CTC2(sp0x40.getPacked(0), 0);
      CPU.CTC2(sp0x40.getPacked(2), 1);
      CPU.CTC2(sp0x40.getPacked(4), 2);
      CPU.CTC2(sp0x40.getPacked(6), 3);
      CPU.CTC2(sp0x40.getPacked(8), 4);
      CPU.CTC2(sp0x40.transfer.getX(), 5);
      CPU.CTC2(sp0x40.transfer.getY(), 6);
      CPU.CTC2(sp0x40.transfer.getZ(), 7);
      final SVECTOR sp0x10 = new SVECTOR().set((short)(a0.x_04.get() * 64), (short)(a0.y_06.get() * 64), (short)0);
      final SVECTOR sp0x18 = new SVECTOR().set((short)((a0.x_04.get() + a0.w_08.get()) * 64), (short)0, (short)(a0.y_06.get() * 64));
      final SVECTOR sp0x20 = new SVECTOR().set((short)(a0.x_04.get() * 64), (short)((a0.y_06.get() + a0.h_0a.get()) * 64), (short)0);
      final SVECTOR sp0x28 = new SVECTOR().set((short)((a0.x_04.get() + a0.w_08.get()) * 64), (short)((a0.y_06.get() + a0.h_0a.get()) * 64), (short)0);
      CPU.MTC2(sp0x10.getXY(), 0);
      CPU.MTC2(sp0x10.getZ(),  1);
      CPU.MTC2(sp0x18.getXY(), 2);
      CPU.MTC2(sp0x18.getZ(),  3);
      CPU.MTC2(sp0x20.getXY(), 4);
      CPU.MTC2(sp0x20.getZ(),  5);
      CPU.COP2(0x280030L);
      final DVECTOR sxy0 = new DVECTOR().setXY(CPU.MFC2(12));
      final DVECTOR sxy1 = new DVECTOR().setXY(CPU.MFC2(13));
      final DVECTOR sxy2 = new DVECTOR().setXY(CPU.MFC2(14));
      CPU.MTC2(sp0x28.getXY(), 0);
      CPU.MTC2(sp0x28.getZ(), 1);
      CPU.COP2(0x180001L);
      final DVECTOR sxy3 = new DVECTOR().setXY(CPU.MFC2(14));

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .clut(a0.clutX_10.get(), a0.clutY_12.get())
        .vramPos((a0.tpage_0c.get() & 0b1111) * 64, (a0.tpage_0c.get() & 0b10000) != 0 ? 256 : 0)
        .rgb(a0.r_14.get(), a0.g_15.get(), a0.b_16.get())
        .pos(0, sxy0.getX(), sxy0.getY())
        .pos(1, sxy1.getX(), sxy1.getY())
        .pos(2, sxy2.getX(), sxy2.getY())
        .pos(3, sxy3.getX(), sxy3.getY())
        .uv(0, a0.u_0e.get(), a0.v_0f.get())
        .uv(1, a0.u_0e.get() + a0.w_08.get(), a0.v_0f.get())
        .uv(2, a0.u_0e.get(), a0.v_0f.get() + a0.h_0a.get())
        .uv(3, a0.u_0e.get() + a0.w_08.get(), a0.v_0f.get() + a0.h_0a.get());

      if((a0._00.get() >>> 30 & 1) != 0) {
        cmd.translucent(Translucency.of((int)a0._00.get() >>> 28 & 0b11));
      }

      GPU.queueCommand(z >> 2, cmd);
    }

    //LAB_800e7930
  }

  @Method(0x800e7944L)
  public static void FUN_800e7944(final BattleStruct24 s1, final VECTOR trans, final int a2) {
    if((int)s1._00.get() >= 0) {
      final VECTOR sp0x18 = ApplyMatrixLV(worldToScreenMatrix_800c3548, trans);
      sp0x18.add(worldToScreenMatrix_800c3548.transfer);

      final int x;
      final int y;
      if(sp0x18.getZ() != 0) {
        x = sp0x18.getX() * projectionPlaneDistance_1f8003f8.get() / sp0x18.getZ();
        y = sp0x18.getY() * projectionPlaneDistance_1f8003f8.get() / sp0x18.getZ();
      } else {
        x = 0;
        y = 0;
      }

      int z = a2 + (sp0x18.getZ() >> 2);
      if(z >= 0x28) {
        if(z > 0x3ff8) {
          z = 0x3ff8;
        }

        //LAB_800e7a38
        final int a1 = (projectionPlaneDistance_1f8003f8.get() << 10) / (sp0x18.getZ() >> 2);
        final int s5 = s1.x_04.get() * s1._1c.get() / 8 * a1 / 8 >> 12;
        final int s7 = s5 + (s1.w_08.get() * s1._1c.get() / 8 * a1 / 8 >> 12);
        final int s2 = s1.y_06.get() * s1._1e.get() / 8 * a1 / 8 >> 12;
        final int fp = s2 + (s1.h_0a.get() * s1._1e.get() / 8 * a1 / 8 >> 12);
        final int sin = rsin(s1.rotation_20.get());
        final int cos = rcos(s1.rotation_20.get());

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .clut(s1.clutX_10.get(), s1.clutY_12.get())
          .vramPos((s1.tpage_0c.get() & 0b1111) * 64, (s1.tpage_0c.get() & 0b10000) != 0 ? 256 : 0)
          .pos(0, x + (s5 * cos >> 12) - (s2 * sin >> 12), y + (s5 * sin >> 12) + (s2 * cos >> 12))
          .pos(1, x + (s7 * cos >> 12) - (s2 * sin >> 12), y + (s7 * sin >> 12) + (s2 * cos >> 12))
          .pos(2, x + (s5 * cos >> 12) - (fp * sin >> 12), y + (s5 * sin >> 12) + (fp * cos >> 12))
          .pos(3, x + (s7 * cos >> 12) - (fp * sin >> 12), y + (s7 * sin >> 12) + (fp * cos >> 12))
          .uv(0, s1.u_0e.get(), s1.v_0f.get())
          .uv(1, s1.w_08.get() + s1.u_0e.get() - 1, s1.v_0f.get())
          .uv(2, s1.u_0e.get(), s1.h_0a.get() + s1.v_0f.get() - 1)
          .uv(3, s1.w_08.get() + s1.u_0e.get() - 1, s1.h_0a.get() + s1.v_0f.get() - 1);

        if((s1._00.get() & 1 << 30) != 0) {
          cmd.translucent(Translucency.of((int)s1._00.get() >>> 28 & 0b11));
        }

        GPU.queueCommand(z >> 2, cmd);
      }
    }

    //LAB_800e7d8c
  }

  @Method(0x800e7dbcL)
  public static int FUN_800e7dbc(final DVECTOR out, final VECTOR translation) {
    final VECTOR transformed = ApplyMatrixLV(worldToScreenMatrix_800c3548, translation);
    transformed.add(worldToScreenMatrix_800c3548.transfer);

    if(transformed.getZ() >= 160) {
      out.setX((short)(transformed.getX() * projectionPlaneDistance_1f8003f8.get() / transformed.getZ()));
      out.setY((short)(transformed.getY() * projectionPlaneDistance_1f8003f8.get() / transformed.getZ()));
      return transformed.getZ() >> 2;
    }

    //LAB_800e7e8c
    //LAB_800e7e90
    return 0;
  }

  @Method(0x800e7ea4L)
  public static void FUN_800e7ea4(final BattleStruct24 a0, final VECTOR a1) {
    FUN_800e7944(a0, a1, 0);
  }

  @Method(0x800e7ec4L)
  public static void effectManagerDestructor(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c struct) {
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
      EffectManagerData6c child = struct.childScript_52.innerStruct_00;

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

    //LAB_800e805c
    if(struct.effect_44 instanceof MemoryRef) {
      free(((MemoryRef)struct.effect_44).getAddress());
    }

    //LAB_800e8074
    while(struct._58 != null) {
      final BttlScriptData6cSubBase2 ptr = struct._58;

      struct._58 = struct._58._00.derefNullable();

      //LAB_800e8088
      free(ptr.getAddress());

      //LAB_800e8090
    }
  }

  @Method(0x800e80c4L)
  public static ScriptState<EffectManagerData6c> allocateEffectManager(@Nullable ScriptState<? extends BattleScriptDataBase> parentState, final int subStructSize, @Nullable final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c> ticker, @Nullable final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c> renderer, @Nullable final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c> destructor, @Nullable final Function<Value, BttlScriptData6cSubBase1> subStructConstructor) {
    final ScriptState<EffectManagerData6c> state = SCRIPTS.allocateScriptState(new EffectManagerData6c());
    final EffectManagerData6c manager = state.innerStruct_00;

    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(Bttl_800e::effectManagerTicker);

    if(renderer != null) {
      state.setRenderer(renderer);
    }

    state.setDestructor(Bttl_800e::effectManagerDestructor);

    final StackWalker.StackFrame caller = DebugHelper.getCallerFrame();

    manager.size_08 = subStructSize;
    if(subStructSize != 0) { // Memulated
      manager.effect_44 = MEMORY.ref(4, mallocTail(subStructSize), subStructConstructor);
      LOGGER.info(EFFECTS, "Allocating memulated effect manager %d for %s (parent: %d) @ %08x from %s.%s(%s:%d)", state.index, manager.effect_44.getClass().getSimpleName(), parentState != null ? parentState.index : -1, ((MemoryRef)manager.effect_44).getAddress(), caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());

      if(!(manager.effect_44 instanceof MemoryRef)) {
        throw new RuntimeException("Size for non-memulated effect managers should be set to 0");
      }
    } else if(subStructConstructor != null) { // Not memulated
      manager.effect_44 = subStructConstructor.apply(null);
      LOGGER.info(EFFECTS, "Allocating effect manager %d for %s (parent: %d) from %s.%s(%s:%d)", state.index, manager.effect_44.getClass().getSimpleName(), parentState != null ? parentState.index : -1, caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());
    } else {
      manager.effect_44 = null;
      LOGGER.info(EFFECTS, "Allocating empty effect manager %d (parent: %d) from %s.%s(%s:%d)", state.index, parentState != null ? parentState.index : -1, caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());
    }

    manager.magic_00 = BattleScriptDataBase.EM__;
    manager.flags_04 = 0xff00_0000;
    manager.scriptIndex_0c = -1;
    manager.coord2Index_0d = -1;
    manager.myScriptState_0e = state;
    manager._10.flags_00 = 0x5400_0000;
    manager._10.trans_04.set(0, 0, 0);
    manager._10.rot_10.set((short)0, (short)0, (short)0);
    manager._10.scale_16.set((short)0x1000, (short)0x1000, (short)0x1000);
    manager._10.colour_1c.set((short)0x80, (short)0x80, (short)0x80);
    manager._10.z_22 = 0;
    manager._10._24 = 0;
    manager._10._28 = 0;
    manager._10._2c = 0;
    manager._10._30 = 0;
    manager.ticker_48 = ticker;
    manager.destructor_4c = destructor;
    manager.parentScript_50 = null;
    manager.childScript_52 = null;
    manager.oldChildScript_54 = null;
    manager.newChildScript_56 = null;
    manager._58 = null;
    manager.type_5c = _800c6e18.get();
    state.type_f8 = manager.type_5c;

    if(parentState != null) {
      if(!BattleScriptDataBase.EM__.equals(parentState.innerStruct_00.magic_00)) {
        parentState = deffManager_800c693c.scriptState_1c;
      }

      final EffectManagerData6c parent = (EffectManagerData6c)parentState.innerStruct_00;
      final EffectManagerData6c child = state.innerStruct_00;

      child.parentScript_50 = (ScriptState<EffectManagerData6c>)parentState;
      if(parent.childScript_52 != null) {
        child.oldChildScript_54 = parent.childScript_52;
        parent.childScript_52.innerStruct_00.newChildScript_56 = state;
      }

      parent.childScript_52 = state;
    }

    return state;
  }

  @Method(0x800e8594L)
  public static void FUN_800e8594(final MATRIX a0, final EffectManagerData6c a1) {
    RotMatrix_8003faf0(a1._10.rot_10, a0);
    TransMatrix(a0, a1._10.trans_04);
    ScaleVectorL_SVEC(a0, a1._10.scale_16);

    EffectManagerData6c s3 = a1;
    int scriptIndex = a1.scriptIndex_0c;

    //LAB_800e8604
    while(scriptIndex >= 0) {
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0[scriptIndex];
      if(state == null) {
        a1._10.flags_00 |= 0x8000_0000;
        a0.transfer.setZ(-0x7fff);
        scriptIndex = -2;
        break;
      }

      final BattleScriptDataBase base = (BattleScriptDataBase)state.innerStruct_00;
      if(BattleScriptDataBase.EM__.equals(base.magic_00)) {
        final EffectManagerData6c manager = (EffectManagerData6c)base;
        final MATRIX sp0x10 = new MATRIX();
        RotMatrix_8003faf0(manager._10.rot_10, sp0x10);
        TransMatrix(sp0x10, manager._10.trans_04);
        ScaleVectorL_SVEC(sp0x10, manager._10.scale_16);
        if(s3.coord2Index_0d != -1) {
          //LAB_800e866c
          MulMatrix0(sp0x10, FUN_800ea0f4(manager, s3.coord2Index_0d).coord, sp0x10);
        }

        //LAB_800e86ac
        MulMatrix0(sp0x10, a0, a0);
        s3 = manager;
        scriptIndex = s3.scriptIndex_0c;
        //LAB_800e86c8
      } else if(BattleScriptDataBase.BOBJ.equals(base.magic_00)) {
        final BattleObject27c bobj = (BattleObject27c)base;
        final Model124 s1 = bobj.model_148;
        applyModelRotationAndScale(s1);
        final int coord2Index = s3.coord2Index_0d;

        final MATRIX sp0x10 = new MATRIX();
        if(coord2Index == -1) {
          sp0x10.set(s1.coord2_14.coord);
        } else {
          //LAB_800e8738
          GsGetLw(s1.coord2ArrPtr_04[coord2Index], sp0x10);
          s1.coord2ArrPtr_04[coord2Index].flg = 0;
        }

        //LAB_800e8774
        MulMatrix0(sp0x10, a0, a0);
        s3 = null;
        scriptIndex = -1;
      } else {
        //LAB_800e878c
        //LAB_800e8790
        a1._10.flags_00 |= 0x8000_0000;
        a0.transfer.setZ(-0x7fff);
        scriptIndex = -2;
        break;
      }
    }

    //LAB_800e87b4
    if(scriptIndex == -2) {
      final MATRIX transposedWs = new MATRIX();
      final VECTOR sp0x30 = new VECTOR();
      TransposeMatrix(worldToScreenMatrix_800c3548, transposedWs);
      sp0x30.set(worldToScreenMatrix_800c3548.transfer).negate();
      transposedWs.transfer.set(ApplyMatrixLV(transposedWs, sp0x30));
      MulMatrix0(transposedWs, a0, a0);
    }

    //LAB_800e8814
  }

  @Method(0x800e8c84L)
  public static BttlScriptData6cSubBase2 FUN_800e8c84(final EffectManagerData6c a0, final long a1) {
    BttlScriptData6cSubBase2 v1 = a0._58;

    //LAB_800e8c98
    while(v1 != null) {
      if(v1._05.get() == a1) {
        //LAB_800e8cc0
        return v1;
      }

      v1 = v1._00.derefNullable();
    }

    //LAB_800e8cb8
    return null;
  }

  @Method(0x800e8cc8L)
  public static BttlScriptData6cSub1c FUN_800e8cc8(@Nullable BttlScriptData6cSub1c a0, final byte a1) {
    //LAB_800e8cd4
    while(a0 != null) {
      if(a0._05.get() == a1) {
        //LAB_800e8cfc
        return a0;
      }

      a0 = a0._00.derefNullableAs(BttlScriptData6cSub1c.class);
    }

    //LAB_800e8cf4
    return null;
  }

  @Method(0x800e8d04L)
  public static void FUN_800e8d04(final EffectManagerData6c a0, final long a1) {
    Ptr<BttlScriptData6cSubBase2> s0 = new Ptr<>(() -> a0._58, val -> a0._58 = val);

    //LAB_800e8d3c
    while(s0.get() != null) {
      final BttlScriptData6cSubBase2 v1 = s0.get();

      if(v1._05.get() == (byte)a1) {
        a0.flags_04 &= ~(0x1 << v1._05.get());

        final BttlScriptData6cSubBase2 a0_0 = s0.get();
        s0.set(a0_0._00.derefNullable());
        free(a0_0.getAddress());
      } else {
        //LAB_800e8d84
        s0 = new Ptr<>(v1._00::derefNullable, v1._00::setNullable);
      }

      //LAB_800e8d88
    }

    //LAB_800e8d98
  }

  @Method(0x800e8dd4L)
  public static <T extends BttlScriptData6cSubBase2> T FUN_800e8dd4(final EffectManagerData6c a0, final long a1, final long a2, final BiFunctionRef<EffectManagerData6c, T, Long> callback, final long size, final Function<Value, T> constructor) {
    final T struct = MEMORY.ref(4, mallocTail(size), constructor);
    struct.size_04.set((int)size);
    struct._05.set((int)a1);
    struct._06.set((short)a2);
    struct._08.set(callback);
    struct._00.setNullable(a0._58);
    a0._58 = struct;
    a0.flags_04 |= 1 << a1;
    return struct;
  }

  @Method(0x800e8e68L)
  public static void FUN_800e8e68(final Ptr<BttlScriptData6cSubBase2> a0) {
    final BttlScriptData6cSubBase2 v1 = a0.get();
    a0.set(v1._00.derefNullable());
    free(v1.getAddress());
  }

  @Method(0x800e8e9cL)
  public static void effectManagerTicker(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    Ptr<BttlScriptData6cSubBase2> subPtr = new Ptr<>(() -> data._58, val -> data._58 = val);

    //LAB_800e8ee0
    while(subPtr.get() != null) {
      final BttlScriptData6cSubBase2 sub = subPtr.get();

      final long v1 = sub._08.derefAs(BiFunctionRef.classFor(EffectManagerData6c.class, BttlScriptData6cSubBase2.class, long.class)).run(data, subPtr.get());
      if(v1 == 0) {
        //LAB_800e8f2c
        data.flags_04 &= ~(1 << sub._05.get());
        subPtr.set(sub._00.derefNullable());
        free(sub.getAddress());
      } else if(v1 == 1) {
        //LAB_800e8f6c
        subPtr = new Ptr<>(sub._00::derefNullable, sub._00::setNullable);
        //LAB_800e8f1c
      } else if(v1 == 2) {
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
  public static void FUN_800e8ffc() {
    final DeffManager7cc deffManager = new DeffManager7cc();
    _800c6938 = deffManager._5b8;
    _800c6930 = deffManager._5dc;
    lights_800c692c = deffManager._640;
    deffManager._20 = 0x4;
    tmds_800c6944 = deffManager.tmds_2f8;
    deffManager_800c693c = deffManager;
    spriteMetrics_800c6948 = deffManager.spriteMetrics_39c;
    final ScriptState<EffectManagerData6c> manager = allocateEffectManager(null, 0, null, null, null, null);
    manager.innerStruct_00.flags_04 = 0x600_0400;
    deffManager.scriptState_1c = manager;
    FUN_800e6070();
    loadSupportOverlay(1, SBtld::FUN_801098f4);
  }

  @Method(0x800e9100L)
  public static void loadBattleHudDeff_() {
    loadBattleHudDeff();
  }

  @Method(0x800e9120L)
  public static void FUN_800e9120() {
    scriptStatePtrArr_800bc1c0[1].deallocateWithChildren();
    FUN_800eab8c();
    deffManager_800c693c.scriptState_1c.deallocateWithChildren();
    deffManager_800c693c = null;
  }

  @Method(0x800e9178L)
  public static void FUN_800e9178(final int a0) {
    if(a0 == 1) {
      //LAB_800e91a0
      FUN_800e8d04(deffManager_800c693c.scriptState_1c.innerStruct_00, 10);
    } else if(a0 == 2) {
      //LAB_800e91d8
      FUN_800e8d04(deffManager_800c693c.scriptState_1c.innerStruct_00, 10);
      FUN_800eab8c();
    } else {
      //LAB_800e9214
      FUN_800eab8c();
      deffManager_800c693c.scriptState_1c.deallocateWithChildren();
      final ScriptState<EffectManagerData6c> manager = allocateEffectManager(null, 0, null, null, null, null);
      deffManager_800c693c.scriptState_1c = manager;
      manager.innerStruct_00.flags_04 = 0x600_0400;
    }

    //LAB_800e9278
  }

  @Method(0x800e929cL)
  public static void FUN_800e929c(final List<byte[]> files) {
    //LAB_800e92d4
    for(final byte[] file : files) {
      if(file.length != 0) {
        new Tim(file).uploadToGpu();
      }
    }

    //LAB_800e9354
  }

  @Method(0x800e93e0L)
  public static FlowControl FUN_800e93e0(final RunningScript<? extends BattleScriptDataBase> script) {
    script.params_20[0].set(allocateEffectManager(script.scriptState_04, 0, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e9428L)
  public static void FUN_800e9428(final SpriteMetrics08 metrics, final EffectManagerData6cInner a1, final MATRIX a2) {
    if(a1.flags_00 >= 0) {
      final BattleStruct24 sp0x10 = new BattleStruct24();
      sp0x10._00.set(a1.flags_00 & 0xffff_ffffL);
      sp0x10.x_04.set((short)(-metrics.w_04.get() / 2));
      sp0x10.y_06.set((short)(-metrics.h_05.get() / 2));
      sp0x10.w_08.set(metrics.w_04.get());
      sp0x10.h_0a.set(metrics.h_05.get());
      sp0x10.tpage_0c.set((metrics.v_02.get() & 0x100) >>> 4 | (metrics.u_00.get() & 0x3ff) >>> 6);
      sp0x10.u_0e.set((metrics.u_00.get() & 0x3f) * 4);
      sp0x10.v_0f.set(metrics.v_02.get());
      sp0x10.clutX_10.set(metrics.clut_06.get() << 4 & 0x3ff);
      sp0x10.clutY_12.set(metrics.clut_06.get() >>> 6 & 0x1ff);
      sp0x10.r_14.set(a1.colour_1c.getX());
      sp0x10.g_15.set(a1.colour_1c.getY());
      sp0x10.b_16.set(a1.colour_1c.getZ());
      sp0x10._1c.set(a1.scale_16.getX());
      sp0x10._1e.set(a1.scale_16.getY());
      sp0x10.rotation_20.set(a1.rot_10.getZ()); // This is correct, different svec for Z
      if((a1.flags_00 & 0x400_0000) != 0) {
        zOffset_1f8003e8.set(a1.z_22);
        FUN_800e75ac(sp0x10, a2);
      } else {
        //LAB_800e9574
        FUN_800e7944(sp0x10, a2.transfer, a1.z_22);
      }
    }

    //LAB_800e9580
  }

  @Method(0x800e9590L)
  public static void renderAttackHitFlashEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final MATRIX sp0x10 = new MATRIX();
    FUN_800e8594(sp0x10, data);
    FUN_800e9428(((AttackHitFlashEffect0c)data.effect_44).metrics_04, data._10, sp0x10);
  }

  @Method(0x800e95f0L)
  public static void FUN_800e95f0(final AttackHitFlashEffect0c a0, final int a1) {
    a0._00.set(a1 | 0x400_0000);

    if((a1 & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[a1 & 0xff];
      a0.metrics_04.u_00.set(metrics.u_00.get());
      a0.metrics_04.v_02.set(metrics.v_02.get());
      a0.metrics_04.w_04.set(metrics.w_04.get());
      a0.metrics_04.h_05.set(metrics.h_05.get());
      a0.metrics_04.clut_06.set(metrics.clut_06.get());
    } else {
      //LAB_800e9658
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(a1 | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08.deref();
      a0.metrics_04.u_00.set(deffMetrics.u_00.get());
      a0.metrics_04.v_02.set(deffMetrics.v_02.get());
      a0.metrics_04.w_04.set(deffMetrics.w_04.get() * 4);
      a0.metrics_04.h_05.set(deffMetrics.h_06.get());
      a0.metrics_04.clut_06.set(deffMetrics.clutY_0a.get() << 6 | (deffMetrics.clutX_08.get() & 0x3f0) >>> 4);
    }

    //LAB_800e96bc
  }

  @Method(0x800e96ccL)
  public static FlowControl allocateAttackHitFlashEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0xc,
      null,
      Bttl_800e::renderAttackHitFlashEffect,
      null,
      AttackHitFlashEffect0c::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager.flags_04 = 0x400_0000;
    FUN_800e95f0(((AttackHitFlashEffect0c)manager.effect_44), script.params_20[1].get());
    manager._10.flags_00 = manager._10.flags_00 & 0xfbff_ffff | 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e9798L)
  public static FlowControl FUN_800e9798(final RunningScript<?> script) {
    final BattleScriptDataBase a2 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final Model124 model;
    if(BattleScriptDataBase.EM__.equals(a2.magic_00)) {
      model = ((BttlScriptData6cSub13c)((EffectManagerData6c)a2).effect_44).model_134;
    } else {
      model = ((BattleObject27c)a2).model_148;
    }

    //LAB_800e97e8
    //LAB_800e97ec
    final int a0 = script.params_20[1].get();
    if(a0 == -1) {
      model.b_cc = 2;
      model.b_cd = -1;
    } else if(a0 == -2) {
      //LAB_800e982c
      model.b_cc = 3;
      //LAB_800e980c
    } else if(a0 == -3) {
      //LAB_800e983c
      model.b_cc = 0;
    } else {
      //LAB_800e9844
      //LAB_800e9848
      model.b_cc = 3;
      model.b_cd = a0;
    }

    //LAB_800e984c
    return FlowControl.CONTINUE;
  }

  @Method(0x800e9854L)
  public static FlowControl FUN_800e9854(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0,
      Bttl_800e::FUN_800ea3f8,
      Bttl_800e::FUN_800ea510,
      Bttl_800e::FUN_800ea5f4,
      value -> new BttlScriptData6cSub13c()
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager.flags_04 = 0x200_0000;

    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x200_0000);
    final BttlScriptData6cSub13c effect = (BttlScriptData6cSub13c)manager.effect_44;
    effect._00 = 0;
    effect.tmdType_04 = animatedTmdType;
    effect.extTmd_08 = animatedTmdType.tmd_0c.deref();
    effect.anim_0c = animatedTmdType.anim_14.deref();
    final DeffPart.TextureInfo textureInfo = animatedTmdType.textureInfo_08.deref().get(0);
    effect.model_134 = effect.model_10;
    final long tpage = GetTPage(Bpp.BITS_4, Translucency.HALF_B_PLUS_HALF_F, textureInfo.vramPos_00.x.get(), textureInfo.vramPos_00.y.get());
    final Model124 model = effect.model_134;
    model.colourMap_9d = (int)_800fb06c.offset(tpage * 0x4L).get();
    loadModelTmd(model, effect.extTmd_08);
    loadModelAnim(model, effect.anim_0c);
    FUN_80114f3c(state, 0, 0x100, 0);
    manager._10.flags_00 = 0x1400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e99bcL)
  public static FlowControl FUN_800e99bc(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0,
      Bttl_800e::FUN_800ea3f8,
      Bttl_800e::FUN_800ea510,
      Bttl_800e::FUN_800ea5f4,
      value -> new BttlScriptData6cSub13c()
    );

    final EffectManagerData6c data = state.innerStruct_00;
    data.flags_04 = 0x100_0000;
    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x100_0000);
    final BttlScriptData6cSub13c s0 = (BttlScriptData6cSub13c)data.effect_44;
    s0._00 = 0;

    s0.tmdType_04 = animatedTmdType;
    s0.extTmd_08 = animatedTmdType.tmd_0c.deref();
    s0.anim_0c = animatedTmdType.anim_14.deref();
    s0.model_10.colourMap_9d = 0;
    s0.model_134 = s0.model_10;
    loadModelTmd(s0.model_134, s0.extTmd_08);
    loadModelAnim(s0.model_134, s0.anim_0c);
    FUN_80114f3c(state, 0, 0x100, 0);
    data._10.flags_00 = 0x5400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e9ae4L)
  public static void FUN_800e9ae4(final Model124 model, final BattleStage a1) {
    model.count_c8 = a1.objTable2_550.nobj;
    model.tmdNobj_ca = a1.objTable2_550.nobj;
    model.ObjTable_0c.top = a1.objTable2_550.top;
    model.ObjTable_0c.nobj = a1.objTable2_550.nobj;
    model.coord2_14.set(a1.coord2_558);
    model.coord2Param_64.set(a1.param_5a8);

    model.tmd_8c = a1.tmd_5d0;
    model.animType_90 = -1;
    model.partTransforms_90 = a1.rotTrans_5d4;
    model.partTransforms_94 = a1.rotTrans_5d8;
    model.animCount_98 = a1.partCount_5dc;
    model.s_9a = a1._5de;
    model.ub_9c = a1._5e0;
    model.colourMap_9d = 0;
    model.zOffset_a0 = 0x200;
    model.ub_a2 = 0;
    model.ub_a3 = 0;
    model.smallerStructPtr_a4 = null;
    model.s_9e = a1._5e2;
    model.ptr_a8 = a1._5ec;

    //LAB_800e9c0c
    for(int i = 0; i < 7; i++) {
      model.aub_ec[i] = 0;
    }

    model.ui_f4 = a1._5e4;
    model.scaleVector_fc.set(0x1000, 0x1000, 0x1000);
    model.tpage_108 = 0;
    model.vector_10c.set(0x1000, 0x1000, 0x1000);
    model.vector_118.set(0, 0, 0);
    model.b_cc = 0;
    model.b_cd = 0;

    final int count = model.count_c8;
    model.dobj2ArrPtr_00 = new GsDOBJ2[count];
    model.coord2ArrPtr_04 = new GsCOORDINATE2[count];
    model.coord2ParamArrPtr_08 = new GsCOORD2PARAM[count];

    for(int i = 0; i < count; i++) {
      model.dobj2ArrPtr_00[i] = new GsDOBJ2().set(a1.dobj2s_00[i]);
      model.coord2ArrPtr_04[i] = new GsCOORDINATE2().set(a1.coord2s_a0[i]);
      model.coord2ParamArrPtr_08[i] = new GsCOORD2PARAM().set(a1.params_3c0[i]);
    }

    final GsCOORDINATE2 parent = model.coord2_14;

    //LAB_800e9d34
    for(int i = 0; i < count; i++) {
      final GsDOBJ2 dobj2 = model.dobj2ArrPtr_00[i];
      dobj2.coord2_04 = model.coord2ArrPtr_04[i];

      final GsCOORDINATE2 coord2 = dobj2.coord2_04;
      coord2.param = model.coord2ParamArrPtr_08[i];
      coord2.super_ = parent;
    }

    //LAB_800e9d90
    model.coord2_14.param = model.coord2Param_64;
    model.ObjTable_0c.top = model.dobj2ArrPtr_00;
  }

  @Method(0x800e9db4L)
  public static void FUN_800e9db4(final Model124 model1, final Model124 model2) {
    //LAB_800e9dd8
    model1.set(model2);

    final int count = model1.count_c8;
    model1.dobj2ArrPtr_00 = new GsDOBJ2[count];
    model1.coord2ArrPtr_04 = new GsCOORDINATE2[count];
    model1.coord2ParamArrPtr_08 = new GsCOORD2PARAM[count];

    for(int i = 0; i < count; i++) {
      model1.dobj2ArrPtr_00[i] = new GsDOBJ2().set(model2.dobj2ArrPtr_00[i]);
      model1.coord2ArrPtr_04[i] = new GsCOORDINATE2().set(model2.coord2ArrPtr_04[i]);
      model1.coord2ParamArrPtr_08[i] = new GsCOORD2PARAM().set(model2.coord2ParamArrPtr_08[i]);
    }

    final GsCOORDINATE2 parent = model1.coord2_14;

    //LAB_800e9ee8
    for(int i = 0; i < count; i++) {
      final GsDOBJ2 dobj2 = model1.dobj2ArrPtr_00[i];
      dobj2.coord2_04 = model1.coord2ArrPtr_04[i];

      final GsCOORDINATE2 coord2 = dobj2.coord2_04;
      coord2.param = model1.coord2ParamArrPtr_08[i];
      coord2.super_ = parent;
    }

    //LAB_800e9f44
    model1.coord2_14.param = model1.coord2Param_64;
    model1.ObjTable_0c.top = model1.dobj2ArrPtr_00;
  }

  @Method(0x800e9f68L)
  public static FlowControl FUN_800e9f68(final RunningScript<? extends BattleScriptDataBase> script) {
    final int s2 = script.params_20[1].get();
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0,
      Bttl_800e::FUN_800ea3f8,
      Bttl_800e::FUN_800ea510,
      Bttl_800e::FUN_800ea5f4,
      value -> new BttlScriptData6cSub13c()
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager.flags_04 = 0x200_0000;

    final BttlScriptData6cSub13c s0 = (BttlScriptData6cSub13c)manager.effect_44;
    s0._00 = 0;
    s0.tmdType_04 = null;
    s0.extTmd_08 = null;
    s0.anim_0c = null;
    s0.model_134 = s0.model_10;

    if((s2 & 0xff00_0000) == 0x700_0000) {
      FUN_800e9ae4(s0.model_10, _1f8003f4.stage_963c);
    } else {
      //LAB_800ea030
      FUN_800e9db4(s0.model_10, ((BattleObject27c)scriptStatePtrArr_800bc1c0[s2].innerStruct_00).model_148);
    }

    //LAB_800ea04c
    final Model124 model = s0.model_134;
    manager._10.trans_04.set(model.coord2_14.coord.transfer);
    manager._10.rot_10.set(model.coord2Param_64.rotate);
    manager._10.scale_16.set(model.scaleVector_fc);
    manager._10.flags_00 = 0x1400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea0f4L)
  public static GsCOORDINATE2 FUN_800ea0f4(final EffectManagerData6c effectManager, final int coord2Index) {
    final Model124 model = ((BttlScriptData6cSub13c)effectManager.effect_44).model_10;
    applyModelRotationAndScale(model);
    return model.coord2ArrPtr_04[coord2Index];
  }

  @Method(0x800ea13cL)
  public static FlowControl FUN_800ea13c(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[(short)script.params_20[0].get()].innerStruct_00;
    final Model124 model = ((BttlScriptData6cSub13c)manager.effect_44).model_134;
    final int a1 = script.params_20[1].get() & 0xffff;

    final int index = a1 >>> 5;
    final int shift = a1 & 0x1f;

    model.ui_f4 |= 0x1L << shift + index * 32;
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea19cL)
  public static FlowControl FUN_800ea19c(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[(short)script.params_20[0].get()].innerStruct_00;
    final Model124 model = ((BttlScriptData6cSub13c)manager.effect_44).model_134;
    final int v1 = script.params_20[1].get() & 0xffff;

    final int index = v1 >>> 5;
    final int shift = v1 & 0x1f;

    model.ui_f4 &= ~(0x1L << shift + index * 32);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea200L)
  public static FlowControl FUN_800ea200(final RunningScript<?> script) {
    final int effectIndex = script.params_20[0].get();
    final ScriptState<EffectManagerData6c> state = (ScriptState<EffectManagerData6c>)scriptStatePtrArr_800bc1c0[effectIndex];
    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub13c effect = (BttlScriptData6cSub13c)manager.effect_44;

    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x200_0000);
    final Anim cmb = animatedTmdType.anim_14.deref();
    effect.anim_0c = cmb;
    loadModelAnim(effect.model_134, cmb);
    manager._10._24 = 0;
    FUN_80114f3c(state, 0, 0x100, 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea2a0L)
  public static FlowControl FUN_800ea2a0(final RunningScript<?> script) {
    final BattleScriptDataBase a2 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final Model124 model;
    if(BattleScriptDataBase.EM__.equals(a2.magic_00)) {
      model = ((BttlScriptData6cSub13c)((EffectManagerData6c)a2).effect_44).model_134;
    } else {
      //LAB_800ea2f8
      model = ((BattleObject27c)a2).model_148;
    }

    //LAB_800ea300
    model.vector_10c.setX(script.params_20[1].get());
    model.vector_10c.setZ(script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea30cL)
  public static FlowControl FUN_800ea30c(final RunningScript<?> script) {
    final BattleScriptDataBase a3 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final Model124 model;
    if(BattleScriptDataBase.EM__.equals(a3.magic_00)) {
      model = ((BttlScriptData6cSub13c)((EffectManagerData6c)a3).effect_44).model_134;
    } else {
      //LAB_800ea36c
      model = ((BattleObject27c)a3).model_148;
    }

    //LAB_800ea374
    model.vector_118.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea384L)
  public static FlowControl FUN_800ea384(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BttlScriptData6cSub13c effect = (BttlScriptData6cSub13c)manager.effect_44;

    if(effect.anim_0c == null) {
      script.params_20[1].set(0);
    } else {
      //LAB_800ea3cc
      script.params_20[1].set((manager._10._24 + 2) / effect.model_134.s_9a);
    }

    //LAB_800ea3e4
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea3f8L)
  public static void FUN_800ea3f8(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final MATRIX sp0x10 = new MATRIX();
    FUN_800e8594(sp0x10, manager);

    final BttlScriptData6cSub13c effect = (BttlScriptData6cSub13c)manager.effect_44;
    final Model124 model = effect.model_134;
    model.coord2Param_64.rotate.set(manager._10.rot_10);
    model.scaleVector_fc.set(manager._10.scale_16);
    model.zOffset_a0 = manager._10.z_22;
    model.coord2_14.coord.set(sp0x10);
    model.coord2_14.flg = 0;

    if(effect.anim_0c != null) {
      applyAnimation(model, manager._10._24);
    }

    //LAB_800ea4fc
  }

  @Method(0x800ea510L)
  public static void FUN_800ea510(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub13c effect = (BttlScriptData6cSub13c)manager.effect_44;
    if(manager._10.flags_00 >= 0) {
      if((manager._10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager._10.colour_1c.getX() << 5, manager._10.colour_1c.getY() << 5, manager._10.colour_1c.getZ() << 5);
      } else {
        //LAB_800ea564
        FUN_800e60e0(0x1000, 0x1000, 0x1000);
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

  @Method(0x800ea5f4L)
  public static void FUN_800ea5f4(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    deallocateModel(((BttlScriptData6cSub13c)manager.effect_44).model_134);
  }

  @Method(0x800ea620L)
  public static void FUN_800ea620(final MrgFile deff, final ScriptState<EffectManagerData6c> deffManagerState) {
    //LAB_800ea674
    for(int i = 0; i < deff.count.get(); i++) {
      final DeffPart deffPart = deff.getFile(i, DeffPart::new);

      final int type = deffPart.flags_00.get() & 0xff00_0000;
      if(type == 0x100_0000) {
        final DeffPart.TmdType tmdType = deffPart.reinterpret(DeffPart.TmdType::new);
        final ExtendedTmd extTmd = tmdType.tmd_0c.deref();
        final TmdWithId tmd = extTmd.tmdPtr_00.deref();

        adjustTmdPointers(extTmd.tmdPtr_00.deref().tmd);

        for(int objectIndex = 0; objectIndex < tmd.tmd.header.nobj.get(); objectIndex++) {
          optimisePacketsIfNecessary(tmd, objectIndex);
        }
      } else if(type == 0x300_0000) {
        final DeffPart.TmdType tmdType = deffPart.reinterpret(DeffPart.TmdType::new);
        final ExtendedTmd extTmd = tmdType.tmd_0c.deref();

        adjustTmdPointers(extTmd.tmdPtr_00.deref().tmd);
        optimisePacketsIfNecessary(extTmd.tmdPtr_00.deref(), 0);
      }

      if(type == 0x100_0000 || type == 0x200_0000 || type == 0x300_0000) {
        final DeffPart.TmdType tmdType = deffPart.reinterpret(DeffPart.TmdType::new);
        final ExtendedTmd extTmd = tmdType.tmd_0c.deref();

        final long a2_0 = MEMORY.ref(4, deffPart.getAddress()).offset(0x8L).get(); //TODO

        if(a2_0 != tmdType.tmd_0c.getPointer() && deffManagerState.index != 0) {
          FUN_800eb308(deffManagerState.innerStruct_00, extTmd, deffPart.getAddress() + a2_0);
        }
      }

      //LAB_800ea778
      //LAB_800ea77c
    }

    //LAB_800ea790
  }

  @Method(0x800ea7d0L)
  public static void hudDeffLoaded(final List<byte[]> files) {
    // Temporary measure for memory management
    final MrgFile deffMrg = MrgFile.alloc(files);

    final DeffManager7cc struct7cc = deffManager_800c693c;
    FUN_800ea620(deffMrg, struct7cc.scriptState_1c);

    //LAB_800ea814
    int i;
    for(i = 0; i < deffMrg.count.get(); i++) {
      final DeffPart part = deffMrg.getFile(i, DeffPart::new);
      final int flags = part.flags_00.get();

      if((flags & 0xff00_0000) != 0) {
        break;
      }

      struct7cc.lmbs_390[flags & 0xff] = part.reinterpret(DeffPart.LmbType::new);
    }

    //LAB_800ea850
    //LAB_800ea874
    for(; i < deffMrg.count.get(); i++) {
      if((deffMrg.getFile(i, DeffPart::new).flags_00.get() & 0xff00_0000) != 0x100_0000) {
        break;
      }
    }

    //LAB_800ea89c
    //LAB_800ea8a8
    //LAB_800ea8e0
    for(; i < deffMrg.count.get(); i++) {
      final DeffPart part = deffMrg.getFile(i, DeffPart::new);
      final int flags = part.flags_00.get();

      if((flags & 0xff00_0000) != 0x300_0000) {
        break;
      }

      final int index = flags & 0xff;
      if(index >= 5) {
        final DeffPart.TmdType tmdType = part.reinterpret(DeffPart.TmdType::new);
        struct7cc.tmds_2f8[index] = tmdType.tmd_0c.deref().tmdPtr_00.deref().tmd.objTable.get(0);
      }

      //LAB_800ea928
    }

    //LAB_800ea93c
    //LAB_800ea964
    for(; i < deffMrg.count.get(); i++) {
      final DeffPart part = deffMrg.getFile(i, DeffPart::new);
      final int flags = part.flags_00.get();

      if((flags & 0xff00_0000) != 0x400_0000) {
        break;
      }

      final DeffPart.SpriteType spriteType = part.reinterpret(DeffPart.SpriteType::new);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08.deref();
      final SpriteMetrics08 metrics = struct7cc.spriteMetrics_39c[flags & 0xff];
      metrics.u_00.set(deffMetrics.u_00.get());
      metrics.v_02.set(deffMetrics.v_02.get());
      metrics.w_04.set(deffMetrics.w_04.get() * 4);
      metrics.h_05.set(deffMetrics.h_06.get());
      metrics.clut_06.set(deffMetrics.clutY_0a.get() << 6 | (deffMetrics.clutX_08.get() & 0x3f0) >>> 4);
    }

    //LAB_800eaa00
    //LAB_800eaa04
    struct7cc.deff_38 = deffMrg;
  }

  @Method(0x800eab8cL)
  public static void FUN_800eab8c() {
    final DeffManager7cc struct7cc = deffManager_800c693c;

    long a0 = struct7cc.ptr_34;
    if(a0 != 0) {
      free(a0);
      struct7cc.ptr_34 = 0;
    }

    //LAB_800eabc4
    a0 = struct7cc.ptr_30;
    if(a0 != 0) {
      free(a0);
      struct7cc.ptr_30 = 0;
    }

    //LAB_800eabf4
    if(struct7cc.deff_38 != null) {
      free(struct7cc.deff_38.getAddress());
      struct7cc.deff_38 = null;
    }

    //LAB_800eac1c
    if(struct7cc.scripts_2c != null) {
      struct7cc.scripts_2c = null;
    }

    //LAB_800eac48
  }

  /** See {@link DeffPart#flags_00} */
  @Method(0x800eac58L)
  public static DeffPart getDeffPart(final int flags) {
    final MrgFile deff = deffManager_800c693c.deffPackage_5a8;

    final Function<Value, ? extends DeffPart> ctor = switch(flags >>> 24) {
      case 0 -> DeffPart.LmbType::new;
      case 1, 2 -> DeffPart.AnimatedTmdType::new;
      case 3 -> DeffPart.TmdType::new;
      case 4 -> DeffPart.SpriteType::new;
      case 5 -> value -> {
        if(value.offset(4, 0x0).get() == Cmb.MAGIC) {
          return new DeffPart.CmbType(value);
        }

        return new DeffPart.AnimatedTmdType(value);
      };
      default -> throw new IllegalArgumentException("Invalid DEFF type %x".formatted(flags & 0xff00_0000));
    };

    //LAB_800eac84
    for(int i = 0; i < deff.count.get(); i++) {
      final DeffPart part = deff.getFile(i, DeffPart::new);

      if(part.flags_00.get() == flags) {
        return part.reinterpret(ctor);
      }

      //LAB_800eaca0
    }

    //LAB_800eacac
    throw new IllegalArgumentException("Couldn't find DEFF with flags " + Long.toHexString(flags));
  }

  @Method(0x800eacf4L)
  public static void loadBattleHudDeff() {
    loadDrgnDir(0, "4114/2", Bttl_800e::hudDeffLoaded);
    loadDrgnDir(0, "4114/3", Bttl_800e::FUN_800e929c);
    loadDrgnDir(0, "4114/1", files -> {
      deffManager_800c693c.scripts_2c = new ScriptFile[files.size()];

      for(int i = 0; i < files.size(); i++) {
        deffManager_800c693c.scripts_2c[i] = new ScriptFile("DRGN0.4114.1." + i, files.get(i));
      }
    });
  }

  @Method(0x800ead44L)
  public static void FUN_800ead44(final RECT a0, final int h) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, a0.x.get(), a0.y.get() + a0.h.get() - h, a0.w.get(), h));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x.get(), a0.y.get() + h, a0.x.get(), a0.y.get(), a0.w.get(), a0.h.get() - h));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x.get(), a0.y.get(), 960, 256, a0.w.get(), h));
  }

  @Method(0x800eaec8L)
  public static long FUN_800eaec8(final EffectManagerData6c data, final BttlScriptData6cSub1c sub) {
    int h = sub._14.get() / 0x100;

    //LAB_800eaef0
    sub._14.add(sub._18.get());

    //LAB_800eaf08
    h = (sub._14.get() / 0x100 - h) % sub._0c.h.get();

    if(h < 0) {
      h = h + sub._0c.h.get();
    }

    //LAB_800eaf30
    if(h != 0) {
      FUN_800ead44(sub._0c, h);
    }

    //LAB_800eaf44
    return 0x1L;
  }

  @Method(0x800eaf54L)
  public static BttlScriptData6cSub1c FUN_800eaf54(EffectManagerData6c manager, final RECT vramPos) {
    //LAB_800eaf80
    while((manager.flags_04 & 0x400) == 0) {
      final ScriptState<EffectManagerData6c> parent = manager.parentScript_50;

      if(parent == null) {
        break;
      }

      manager = parent.innerStruct_00;
    }

    //LAB_800eafb8
    BttlScriptData6cSub1c a0_0 = (BttlScriptData6cSub1c)FUN_800e8c84(manager, 10);

    //LAB_800eafcc
    while(a0_0 != null) {
      if(a0_0._0c.x.get() == vramPos.x.get() && a0_0._0c.y.get() == vramPos.y.get()) {
        break;
      }

      //LAB_800eaff4
      a0_0 = FUN_800e8cc8(a0_0._00.derefNullableAs(BttlScriptData6cSub1c.class), (byte)10);
    }

    //LAB_800eb00c
    return a0_0;
  }

  @Method(0x800eb01cL)
  public static FlowControl FUN_800eb01c(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[(short)script.params_20[0].get()].innerStruct_00;
    final GuardHealEffect14 effect = (GuardHealEffect14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04.deref();
    final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08.deref().get((short)script.params_20[1].get());

    EffectManagerData6c v1_0 = manager;

    //LAB_800eb0c0
    while((v1_0.flags_04 & 0x400) == 0) {
      final ScriptState<EffectManagerData6c> parent = v1_0.parentScript_50;

      if(parent == null) {
        break;
      }

      v1_0 = parent.innerStruct_00;
    }

    //LAB_800eb0f8
    final EffectManagerData6c finalV1_0 = v1_0;
    Ptr<BttlScriptData6cSubBase2> a0 = new Ptr<>(() -> finalV1_0._58, val -> finalV1_0._58 = val);

    //LAB_800eb10c
    while(a0.get() != null) {
      final BttlScriptData6cSub1c a1 = (BttlScriptData6cSub1c)a0.get();

      if(a1._05.get() == 10) {
        if(a1._0c.x.get() == textureInfo.vramPos_00.x.get()) {
          if(a1._0c.y.get() == textureInfo.vramPos_00.y.get()) {
            FUN_800e8e68(a0);
            break;
          }
        }
      }

      //LAB_800eb15c
      a0 = new Ptr<>(a0.get()._00::derefNullable, a0.get()._00::setNullable);
    }

    //LAB_800eb174
    //LAB_800eb178
    return FlowControl.CONTINUE;
  }

  @Method(0x800eb188L)
  public static FlowControl FUN_800eb188(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[(short)script.params_20[0].get()];
    final EffectManagerData6c manager = (EffectManagerData6c)state.innerStruct_00;
    final GuardHealEffect14 effect = (GuardHealEffect14)manager.effect_44;

    final DeffPart.TmdType tmdType = effect.tmdType_04.deref();
    final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08.deref().get((short)script.params_20[1].get());
    final BttlScriptData6cSub1c a0 = FUN_800eaf54(manager, textureInfo.vramPos_00);

    if(a0 != null) {
      int h = -a0._14.get() / 256 % a0._0c.h.get();

      if(h < 0) {
        h = h + a0._0c.h.get();
      }

      //LAB_800eb25c
      if(h != 0) {
        FUN_800ead44(a0._0c, h);
      }
    }

    //LAB_800eb270
    return FlowControl.CONTINUE;
  }

  @Method(0x800eb280L)
  public static void FUN_800eb280(final EffectManagerData6c manager, final RECT vramPos, final int a2) {
    BttlScriptData6cSub1c v0 = FUN_800eaf54(manager, vramPos);

    if(v0 == null) {
      v0 = FUN_800e8dd4(manager, 0xa, 0, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800eaec8", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1c, BttlScriptData6cSub1c::new);
      v0._0c.set(vramPos);
      v0._14.set(0);
    }

    //LAB_800eb2ec
    v0._18.set(a2);
  }

  @Method(0x800eb308L)
  public static void FUN_800eb308(final EffectManagerData6c a0, final ExtendedTmd extendedTmd, final long a2) {
    if(extendedTmd.ptr_08.get() != 0) {
      final long s2 = extendedTmd.getAddress() + extendedTmd.ptr_08.get(); //TODO

      //LAB_800eb348
      for(int s1 = 0; s1 < 7; s1++) {
        final long s0 = s2 + MEMORY.ref(4, s2).offset(s1 * 0x4L).get();

        if((MEMORY.ref(2, s0).offset(0x0L).get() & 0x4000L) != 0) {
          final BttlScriptData6cSub1c sub = FUN_800e8dd4(a0, 0xaL, 0, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800eaec8", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1cL, BttlScriptData6cSub1c::new);

          if((MEMORY.ref(2, s0).offset(0x2L).get() & 0x3c0L) == 0) {
            sub._0c.x.set((short)(MEMORY.ref(2, a2).offset(0x0L).get() & 0x3c0L | MEMORY.ref(2, s0).offset(0x2L).get()));
            sub._0c.y.set((short)(MEMORY.ref(2, a2).offset(0x2L).get() & 0x100L | MEMORY.ref(2, s0).offset(0x4L).get()));
          } else {
            //LAB_800eb3cc
            sub._0c.x.set((short)MEMORY.ref(2, s0).offset(0x2L).get());
            sub._0c.y.set((short)MEMORY.ref(2, s0).offset(0x4L).get());
          }

          //LAB_800eb3dc
          //LAB_800eb3f8
          sub._0c.w.set((short)(MEMORY.ref(2, s0).offset(0x6L).getSigned() / 4));
          sub._0c.h.set((short)MEMORY.ref(2, s0).offset(0x8L).get());
          sub._14.set(0);

          final long v1 = MEMORY.ref(2, s0).offset(0xcL).get();
          final long v0;
          if(v1 >= 0x10L) {
            v0 = v1 * 0x10L;
          } else {
            //LAB_800eb42c
            v0 = 0x100L / (int)v1;
          }

          //LAB_800eb434
          sub._18.set((int)v0);
          if(MEMORY.ref(2, s0).offset(0xaL).get() == 0) {
            sub._18.set(-sub._18.get());
          }
        }

        //LAB_800eb45c
      }
    }

    //LAB_800eb46c
  }

  @Method(0x800eb48cL)
  public static void FUN_800eb48c(final int scriptIndex, final int a1, final int a2) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[scriptIndex];
    final EffectManagerData6c manager = (EffectManagerData6c)state.innerStruct_00;
    final GuardHealEffect14 effect = (GuardHealEffect14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04.deref();
    FUN_800eb280(manager, new RECT().set(tmdType.textureInfo_08.deref().get(a1).vramPos_00), a2);
  }

  @Method(0x800eb518L)
  public static FlowControl FUN_800eb518(final RunningScript<?> script) {
    FUN_800eb48c(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  /** Used in Dart transform */
  @Method(0x800eb554L)
  public static void FUN_800eb554(final RECT a0, final DVECTOR a1, final int height) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, a1.getX(), a1.getY() + a0.h.get() - height, a0.w.get(), height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a1.getX(), a1.getY() + height, a1.getX(), a1.getY(), a0.w.get(), a0.h.get() - height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a1.getX(), a1.getY(), a0.x.get(), a0.y.get() + a0.h.get() - height, a0.w.get(), height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x.get(), a0.y.get() + height, a0.x.get(), a0.h.get(), a0.w.get(), a0.h.get() - height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x.get(), a0.y.get(), 960, 256, a0.w.get(), height));
  }

  @Method(0x800eb7c4L)
  public static long FUN_800eb7c4(final EffectManagerData6c manager, final BttlScriptData6cSub20 effect) {
    int a2 = effect._14.get() / 256;

    //LAB_800eb7e8
    effect._14.add(effect._18.get());

    //LAB_800eb800
    a2 = (effect._14.get() / 256 - a2) % effect._0c.h.get();

    if(a2 < 0) {
      a2 = a2 + effect._0c.h.get();
    }

    //LAB_800eb828
    if(a2 != 0) {
      FUN_800eb554(effect._0c, effect._1c, a2);
    }

    //LAB_800eb838
    return 1;
  }

  @Method(0x800eb84cL)
  public static FlowControl FUN_800eb84c(final RunningScript<?> script) {
    EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final GuardHealEffect14 effect = (GuardHealEffect14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04.deref();
    final DeffPart.TextureInfo textureInfo1 = tmdType.textureInfo_08.deref().get(script.params_20[1].get());
    final DeffPart.TextureInfo textureInfo2 = tmdType.textureInfo_08.deref().get(script.params_20[2].get());

    //LAB_800eb8fc
    while((manager.flags_04 & 0x400) == 0) {
      final ScriptState<EffectManagerData6c> parent = manager.parentScript_50;

      if(parent == null) {
        break;
      }

      manager = parent.innerStruct_00;
    }

    //LAB_800eb934
    final BttlScriptData6cSub20 sub = FUN_800e8dd4(manager, 0xa, 0, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800eb7c4", EffectManagerData6c.class, BttlScriptData6cSub20.class), BiFunctionRef::new), 0x20, BttlScriptData6cSub20::new);
    sub._0c.set(textureInfo1.vramPos_00);
    sub._14.set(0);
    sub._18.set(script.params_20[3].get());
    sub._1c.setX(textureInfo2.vramPos_00.x.get());
    sub._1c.setY(textureInfo2.vramPos_00.y.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800eb9acL)
  public static void loadStageTmd(final BattleStage stage, final ExtendedTmd extTmd, final TmdAnimationFile tmdAnim) {
    final int x = stage.coord2_558.coord.transfer.getX();
    final int y = stage.coord2_558.coord.transfer.getY();
    final int z = stage.coord2_558.coord.transfer.getZ();

    stage_800bda0c = stage;

    //LAB_800eb9fc
    for(int i = 0; i < 10; i++) {
      stage._618[i] = 0;
    }

    stage.tmd_5d0 = extTmd.tmdPtr_00.deref().tmd;

    if(extTmd.ptr_08.get() != 0) {
      stage._5ec = extTmd.getAddress() + extTmd.ptr_08.get() / 0x4L * 0x4L; //TODO

      //LAB_800eba38
      for(int i = 0; i < 10; i++) {
        stage._5f0[i] = stage._5ec + MEMORY.ref(2, stage._5ec).offset(i * 0x4L).get();
        FUN_800ec86c(stage, i);
      }
    } else {
      //LAB_800eba74
      //LAB_800eba7c
      for(int i = 0; i < 10; i++) {
        stage._5f0[i] = 0;
      }
    }

    //LAB_800eba8c
    adjustTmdPointers(stage.tmd_5d0);
    initObjTable2(stage.objTable2_550, stage.dobj2s_00, stage.coord2s_a0, stage.params_3c0, 10);
    stage.coord2_558.param = stage.param_5a8;
    GsInitCoordinate2(null, stage.coord2_558);
    prepareObjTable2(stage.objTable2_550, stage.tmd_5d0, stage.coord2_558, 10, extTmd.tmdPtr_00.deref().tmd.header.nobj.get() + 1);
    applyInitialStageTransforms(stage, tmdAnim);

    stage.coord2_558.coord.transfer.set(x, y, z);
    stage._5e4 = 0;
    stage.z_5e8 = 0x200;
  }

  @Method(0x800ebb58L)
  public static void applyScreenDarkening(final int multiplier) {
    final BattleStageDarkening1800 darkening = stageDarkening_800c6958.deref();

    //LAB_800ebb7c
    int t2;
    for(t2 = 0; t2 < stageDarkeningClutCount_800c695c.get(); t2++) {
      //LAB_800ebb80
      for(int a3 = 0; a3 < 0x10; a3++) {
        final int colour = darkening._000.get(t2).get(a3).get();
        final int mask = colour >>> 15 & 0x1;
        final int b = (colour >>> 10 & 0x1f) * multiplier >> 4 & 0x1f;
        final int g = (colour >>> 5 & 0x1f) * multiplier >> 4 & 0x1f;
        final int r = (colour & 0x1f) * multiplier >> 4 & 0x1f;

        final int v0;
        if(r != 0 || g != 0 || b != 0 || colour == 0) {
          //LAB_800ebbf0
          v0 = mask << 15 | b << 10 | g << 5 | r;
        } else {
          //LAB_800ebc0c
          v0 = colour & 0xffff_8000 | 0x1;
        }

        //LAB_800ebc18
        darkening._800.get(t2).get(a3).set(v0);
      }
    }

    //LAB_800ebc44
    //LAB_800ebc58
    for(; t2 < 0x40; t2++) {
      //LAB_800ebc5c
      for(int a3 = 0; a3 < 0x10; a3++) {
        darkening._800.get(t2).get(a3).set(darkening._000.get(t2).get(a3).get());
      }
    }

    //LAB_800ebc88
    //LAB_800ebca4
    for(t2 = 0; t2 < 0x40; t2++) {
      //LAB_800ebcb0
      for(int a3 = 0; a3 < 0x10; a3++) {
        darkening._1000.get(t2).get(a3).set(darkening._800.get(_800fb148.get(t2).get()).get(a3).get());
      }
    }

    LoadImage(new RECT().set((short)448, (short)240, (short)64, (short)16), stageDarkening_800c6958.deref()._1000.getAddress());
  }

  @Method(0x800ebd34L)
  public static void FUN_800ebd34(final BattleStage struct, final int index) {
    long v0;
    long a2;
    final int s1;
    final int s4;
    final int s6;

    v0 = struct._5f0[index]; //TODO ptr to RECT?

    if(v0 == 0) {
      struct._618[index] = 0;
      return;
    }

    //LAB_800ebd84
    final int x = (short)MEMORY.ref(2, v0).offset(0x0L).get();
    final int y = (short)MEMORY.ref(2, v0).offset(0x2L).get();
    final int w = (short)(MEMORY.ref(2, v0).offset(0x4L).get() / 4);
    final int h = (short)MEMORY.ref(2, v0).offset(0x6L).get();

    //LAB_800ebdcc
    a2 = v0 + 0x8L;

    // There was a loop here, but each iteration overwrote the results from the previous iteration... I collapsed it into a single iteration
    a2 += (struct._65e[index] - 1) * 0x4L;
    int s0 = (short)MEMORY.ref(2, a2).offset(0x2L).get();
    final int t1 = (short)(MEMORY.ref(2, a2).offset(0x0L).get() & 1);
    final int t0 = (short)(MEMORY.ref(2, a2).offset(0x0L).get() >>> 1);
    a2 = a2 + 0x4L;

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

      if(MEMORY.ref(2, a2).offset(0x0L).get() != 0xffffL) {
        struct._65e[index]++;
      } else {
        //LAB_800ebe88
        struct._65e[index] = 1;
      }
    }

    //LAB_800ebe94
    if(s0 != 0) {
      s1 = s0 / 16;
      s4 = h - s1;

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
  public static void FUN_800ec258(final Model124 model) {
    final Model124 s2 = model_800bda10;

    GsInitCoordinate2(model.coord2_14, s2.coord2_14);

    if(model.b_cc != 3) {
      s2.coord2_14.coord.transfer.setX(model.vector_118.getX());

      if(model.b_cc == 1) {
        s2.coord2_14.coord.transfer.setY(model.vector_118.getY());
      } else {
        //LAB_800ec2bc
        s2.coord2_14.coord.transfer.setY(model.vector_118.getY() - (model.coord2_14.coord.transfer.getY() << 12) / model.scaleVector_fc.getY());
      }

      //LAB_800ec2e0
      s2.coord2_14.coord.transfer.setZ(model.vector_118.getZ());
    } else {
      //LAB_800ec2ec
      s2.coord2_14.coord.transfer.setX(model.vector_118.getX() + model.coord2ArrPtr_04[model.b_cd].coord.transfer.getX());
      s2.coord2_14.coord.transfer.setY(model.vector_118.getY() - (model.coord2_14.coord.transfer.getY() << 12) / model.scaleVector_fc.getY());
      s2.coord2_14.coord.transfer.setZ(model.vector_118.getZ() + model.coord2ArrPtr_04[model.b_cd].coord.transfer.getZ());
    }

    //LAB_800ec370
    s2.zOffset_a0 = model.zOffset_a0 + 16;
    s2.scaleVector_fc.setX(model.vector_10c.getX() / 4);
    s2.scaleVector_fc.setY(model.vector_10c.getY() / 4);
    s2.scaleVector_fc.setZ(model.vector_10c.getZ() / 4);
    RotMatrix_8003faf0(s2.coord2Param_64.rotate, s2.coord2_14.coord);
    final VECTOR scale = new VECTOR().set(s2.scaleVector_fc);
    ScaleMatrixL(s2.coord2_14.coord, scale);
    s2.coord2_14.flg = 0;
    final GsCOORDINATE2 v0 = s2.dobj2ArrPtr_00[0].coord2_04;
    final GsCOORD2PARAM s0 = v0.param;
    s0.rotate.set((short)0, (short)0, (short)0);
    RotMatrix_80040780(s0.rotate, v0.coord);
    s0.trans.set(0, 0, 0);
    TransMatrix(v0.coord, s0.trans);

    final MATRIX sp0x30 = new MATRIX();
    final MATRIX sp0x10 = new MATRIX();
    GsGetLws(s2.ObjTable_0c.top[0].coord2_04, sp0x30, sp0x10);
    GsSetLightMatrix(sp0x30);
    CPU.CTC2(sp0x10.getPacked(0), 0);
    CPU.CTC2(sp0x10.getPacked(2), 1);
    CPU.CTC2(sp0x10.getPacked(4), 2);
    CPU.CTC2(sp0x10.getPacked(6), 3);
    CPU.CTC2(sp0x10.getPacked(8), 4);
    CPU.CTC2(sp0x10.transfer.getX(), 5);
    CPU.CTC2(sp0x10.transfer.getY(), 6);
    CPU.CTC2(sp0x10.transfer.getZ(), 7);
    Renderer.renderDobj2(s2.ObjTable_0c.top[0], true);
    s2.coord2ArrPtr_04[0].flg--;
  }

  @Method(0x800ec4bcL)
  public static void allocateStageDarkeningStorage() {
    stageDarkening_800c6958.setPointer(mallocTail(0x1800L));
  }

  @Method(0x800ec4f0L)
  public static void deallocateStageDarkeningStorage() {
    free(stageDarkening_800c6958.getPointer());
  }

  @Method(0x800ec51cL)
  public static void FUN_800ec51c(final BattleStage stage) {
    //LAB_800ec548
    for(int i = 0; i < 10; i++) {
      if(stage._618[i] != 0) {
        FUN_800ebd34(stage, i);
      }

      //LAB_800ec560
    }

    tmdGp0Tpage_1f8003ec.set(0);
    zOffset_1f8003e8.set(stage.z_5e8);

    //LAB_800ec5a0
    long s4 = 0x1L;
    for(int i = 0; i < stage.objTable2_550.nobj; i++) {
      final GsDOBJ2 dobj2 = stage.objTable2_550.top[i];
      if((s4 & stage._5e4) == 0) {
        final MATRIX ls = new MATRIX();
        final MATRIX lw = new MATRIX();
        GsGetLws(dobj2.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        CPU.CTC2(ls.getPacked(0), 0);
        CPU.CTC2(ls.getPacked(2), 1);
        CPU.CTC2(ls.getPacked(4), 2);
        CPU.CTC2(ls.getPacked(6), 3);
        CPU.CTC2(ls.getPacked(8), 4);
        CPU.CTC2(ls.transfer.getX(), 5);
        CPU.CTC2(ls.transfer.getY(), 6);
        CPU.CTC2(ls.transfer.getZ(), 7);
        Renderer.renderDobj2(dobj2, true);
      }

      //LAB_800ec608
      s4 = s4 << 1;
    }

    //LAB_800ec618
  }

  @Method(0x800ec63cL)
  public static void applyStagePartAnimations(final BattleStage stage) {
    //LAB_800ec688
    for(int i = 0; i < stage.partCount_5dc; i++) {
      final ModelPartTransforms rotTrans = stage.rotTrans_5d8.get(i);
      final GsCOORDINATE2 coord2 = stage.dobj2s_00[i].coord2_04;
      final GsCOORD2PARAM param = coord2.param;

      param.rotate.set(rotTrans.rotate_00);
      RotMatrix_80040010(param.rotate, coord2.coord);

      param.trans.set(rotTrans.translate_06);
      TransMatrix(coord2.coord, param.trans);
    }

    //LAB_800ec710
    stage.rotTrans_5d8 = stage.rotTrans_5d8.slice(stage.partCount_5dc);
  }

  @Method(0x800ec744L)
  public static void FUN_800ec744(final BattleStage stage) {
    RotMatrix_8003faf0(stage.param_5a8.rotate, stage.coord2_558.coord);
    stage.coord2_558.flg = 0;
  }

  @Method(0x800ec774L)
  public static void applyInitialStageTransforms(final BattleStage stage, final TmdAnimationFile anim) {
    stage.rotTrans_5d4 = anim.partTransforms_10;
    stage.rotTrans_5d8 = anim.partTransforms_10;
    stage.partCount_5dc = anim.count_0c.get();
    stage._5de = anim._0e.get();
    stage._5e0 = 0;
    applyStagePartAnimations(stage);
    stage._5e0 = 1;
    stage._5e2 = stage._5de;
    stage.rotTrans_5d8 = stage.rotTrans_5d4;
  }

  @Method(0x800ec7e4L)
  public static DVECTOR perspectiveTransformXyz(final Model124 model, final short x, final short y, final short z) {
    final MATRIX ls = new MATRIX();
    GsGetLs(model.coord2_14, ls);
    setRotTransMatrix(ls);

    final DVECTOR screenCoords = new DVECTOR();
    perspectiveTransform(new SVECTOR().set(x, y, z), screenCoords, new Ref<>(), new Ref<>());
    return screenCoords;
  }

  @Method(0x800ec86cL)
  public static void FUN_800ec86c(final BattleStage stage, final int index) {
    final long a2 = stage._5f0[index];

    if(a2 == 0) {
      stage._618[index] = 0;
      return;
    }

    //LAB_800ec890
    if(MEMORY.ref(2, a2).get() == 0xffffL) {
      stage._5f0[index] = 0;
      return;
    }

    //LAB_800ec8a8
    stage._618[index] = 1;
    stage._622[index] = (int)MEMORY.ref(2, a2).offset(0xaL).get();
    stage._64a[index] = 0;
    stage._65e[index] = 1;
  }

  /** Stage darkening for counterattacks change the clut, this saves a backup copy */
  @Method(0x800ec8d0L)
  public static void backupStageClut(final long timFile) {
    final BattleStageDarkening1800 darkening = stageDarkening_800c6958.deref();

    //LAB_800ec8ec
    for(int a3 = 0; a3 < 0x40; a3++) {
      //LAB_800ec8f4
      for(int a1 = 0; a1 < 0x10; a1++) {
        darkening._000.get(_800fb148.get(a3).get()).get(a1).set((int)MEMORY.ref(2, timFile).offset(0x14L).offset(a3 * 0x20L).offset(a1 * 0x2L).get());
      }
    }

    if(MEMORY.ref(2, timFile).offset(0x8812L).offset(0x0L).get() == 0x7422L) {
      stageDarkeningClutCount_800c695c.set((int)MEMORY.ref(2, timFile).offset(0x8812L).offset(0x4L).get());
    } else {
      //LAB_800ec954
      stageDarkeningClutCount_800c695c.set(63);
    }

    //LAB_800ec95c
    stageDarkeningClutCount_800c695c.incr();
  }

  @Method(0x800ec974L)
  public static void renderBttlModel(final Model124 model) {
    tmdGp0Tpage_1f8003ec.set(model.tpage_108);
    zOffset_1f8003e8.set(model.zOffset_a0);

    //LAB_800ec9d0
    for(int i = 0; i < model.ObjTable_0c.nobj; i++) {
      if((model.ui_f4 & 1L << i) == 0) {
        final GsDOBJ2 s2 = model.ObjTable_0c.top[i];
        final MATRIX sp0x30 = new MATRIX();
        final MATRIX sp0x10 = new MATRIX();
        GsGetLws(s2.coord2_04, sp0x30, sp0x10);
        GsSetLightMatrix(sp0x30);
        CPU.CTC2(sp0x10.getPacked(0), 0);
        CPU.CTC2(sp0x10.getPacked(2), 1);
        CPU.CTC2(sp0x10.getPacked(4), 2);
        CPU.CTC2(sp0x10.getPacked(6), 3);
        CPU.CTC2(sp0x10.getPacked(8), 4);
        CPU.CTC2(sp0x10.transfer.getX(), 5);
        CPU.CTC2(sp0x10.transfer.getY(), 6);
        CPU.CTC2(sp0x10.transfer.getZ(), 7);
        Renderer.renderDobj2(s2, true);
      }
    }

    //LAB_800eca58
    if(model.b_cc != 0) {
      FUN_800ec258(model);
    }

    //LAB_800eca70
  }

  @Method(0x800eca98L)
  public static void drawTargetArrow(final int targetType, final int combatantIdx) {
    if(combatantIdx != -1) {
      final ScriptState<BattleObject27c> state;
      if(targetType == 0) {
        //LAB_800ecb00
        state = _8006e398.charBobjIndices_e40[combatantIdx];
      } else if(targetType == 1) {
        //LAB_800ecb1c
        state = _8006e398.enemyBobjIndices_ebc[combatantIdx];
        //LAB_800ecaf0
      } else if(targetType == 2) {
        //LAB_800ecb38
        state = _8006e398.bobjIndices_e0c[combatantIdx];
      } else {
        throw new IllegalStateException("Invalid target type " + targetType);
      }

      //LAB_800ecb50
      //LAB_800ecb54
      final BattleObject27c a3 = state.innerStruct_00;
      final int textEffect;
      if(a3.hp_08 > a3.maxHp_10 / 4) {
        textEffect = a3.hp_08 > a3.maxHp_10 / 2 ? 0 : 1;
      } else {
        textEffect = 2;
      }

      //LAB_800ecb90
      drawTargetArrow(a3.model_148, textEffect, state, a3);
    } else {
      //LAB_800ecba4
      long count = 0;
      if(targetType == 0) {
        //LAB_800ecbdc
        count = charCount_800c677c.get();
      } else if(targetType == 1) {
        //LAB_800ecbec
        count = enemyCount_800c6758.get();
        //LAB_800ecbc8
      } else if(targetType == 2) {
        //LAB_800ecbfc
        count = _800c669c.get();
      }

      //LAB_800ecc04
      //LAB_800ecc1c
      for(int i = 0; i < count; i++) {
        final ScriptState<BattleObject27c> state;
        if(targetType == 0) {
          //LAB_800ecc50
          state = _8006e398.charBobjIndices_e40[i];
        } else if(targetType == 1) {
          //LAB_800ecc5c
          state = _8006e398.enemyBobjIndices_ebc[i];
          //LAB_800ecc40
        } else if(targetType == 2) {
          //LAB_800ecc68
          state = _8006e398.bobjIndices_e78[i];
        } else {
          throw new IllegalStateException("Invalid target type " + targetType);
        }

        //LAB_800ecc74
        //LAB_800ecc78
        final BattleObject27c data = state.innerStruct_00;

        final int textEffect;
        if(data.hp_08 > data.maxHp_10 / 4) {
          textEffect = data.hp_08 > data.maxHp_10 / 2 ? 0 : 1;
        } else {
          textEffect = 2;
        }

        //LAB_800eccac
        if((state.storage_44[7] & 0x4000) == 0) {
          drawTargetArrow(data.model_148, textEffect, state, data);
        }

        //LAB_800eccc8
      }
    }

    //LAB_800eccd8
  }

  @Method(0x800eccfcL)
  public static void drawTargetArrow(final Model124 model, final int textEffect, final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    final int x;
    final int y;
    final int z;
    final int flags = state.storage_44[7];
    if((flags & 0x4) != 0) {
      // X and Z are swapped
      x = -data._78.getZ() * 100;
      y = -data._78.getY() * 100;
      z = -data._78.getX() * 100;
    } else {
      //LAB_800ecd90
      if((flags & 0x2) != 0) {
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
    final DVECTOR screenCoords = perspectiveTransformXyz(model, (short)x, (short)y, (short)z);

    final GpuCommandQuad cmd = new GpuCommandQuad()
      .bpp(Bpp.BITS_4)
      .translucent(Translucency.HALF_B_PLUS_HALF_F)
      .vramPos(704, 256)
      .monochrome(0x80)
      .pos(screenCoords.getX() - 8, screenCoords.getY() + (int)_800fb188.offset(2, (tickCount_800bb0fc.get() & 0x7) * 0x2L).getSigned(), 16, 24)
      .uv(240, 0);

    if(textEffect == 0) {
      //LAB_800ece80
      cmd.clut(720, 510);
    } else if(textEffect == 1) {
      //LAB_800ece88
      cmd.clut(720, 511);
      //LAB_800ece70
    } else if(textEffect == 2) {
      //LAB_800ece90
      //LAB_800ece94
      cmd.clut(736, 496);
    }

    //LAB_800ece9c
    GPU.queueCommand(28, cmd);
  }

  @Method(0x800ee210L)
  public static FlowControl scriptCopyVram(final RunningScript<?> script) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(script.params_20[4].get(), script.params_20[5].get(), script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get() / 4, (short)script.params_20[3].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee2acL)
  public static FlowControl scriptSetBobjZOffset(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.zOffset_a0 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee2e4L)
  public static FlowControl scriptSetBobjScaleUniform(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final int scale = script.params_20[1].get();
    bobj.model_148.scaleVector_fc.set(scale, scale, scale);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee324L)
  public static FlowControl scriptSetBobjScale(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.scaleVector_fc.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee384L)
  public static FlowControl FUN_800ee384(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.b_cc = 2;
    bobj.model_148.b_cd = -1;
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee3c0L)
  public static FlowControl FUN_800ee3c0(final RunningScript<?> script) {
    final BattleObject27c v1 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    v1.model_148.b_cc = 3;
    v1.model_148.b_cd = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee408L)
  public static FlowControl FUN_800ee408(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final int a0_0 = bobj.model_148.b_cd;
    if(a0_0 == -2) {
      //LAB_800ee450
      bobj.model_148.b_cc = 0;
    } else if(a0_0 == -1) {
      bobj.model_148.b_cc = 2;
    } else {
      //LAB_800ee458
      bobj.model_148.b_cc = 3;
    }

    //LAB_800ee460
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee468L)
  public static FlowControl FUN_800ee468(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.b_cc = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee49cL)
  public static FlowControl FUN_800ee49c(final RunningScript<?> script) {
    final BattleObject27c a1 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    a1.model_148.vector_10c.setX(script.params_20[1].get());
    a1.model_148.vector_10c.setZ(script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee4e8L)
  public static FlowControl FUN_800ee4e8(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.vector_118.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee548L)
  public static FlowControl scriptApplyScreenDarkening(final RunningScript<?> script) {
    applyScreenDarkening(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee574L)
  public static FlowControl scriptGetStageNobj(final RunningScript<?> script) {
    script.params_20[0].set(stage_800bda0c.objTable2_550.nobj);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee594L)
  public static FlowControl FUN_800ee594(final RunningScript<?> a0) {
    stage_800bda0c._5e4 |= 1L << a0.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee5c0L)
  public static FlowControl FUN_800ee5c0(final RunningScript<?> a0) {
    stage_800bda0c._5e4 &= ~(1L << a0.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee5f0L)
  public static FlowControl scriptSetStageZ(final RunningScript<?> script) {
    stage_800bda0c.z_5e8 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee610L)
  public static void FUN_800ee610() {
    _800c6cf4.setu(0);
    _800c6c38.setu(0x1L);
    displayStats_800c6c2c.setPointer(mallocTail(0x144 * 3));
    floatingNumbers_800c6b5c.setPointer(mallocTail(0xc4 * 12));
    _800c6b60.setPointer(mallocTail(0xa4L));
    battleMenu_800c6c34.setPointer(mallocTail(0x58L));
    _800c6b6c.setu(mallocTail(0x3cL));

    FUN_800ef7c4();
    FUN_800f4964();

    final BttlStructa4 v0 = _800c6b60.deref();
    v0._26.set((short)0);
    v0._28.set((short)0);
    v0._2a.set((short)0);
    v0._2c.set(0);
    v0._30.set((short)0);

    FUN_800f60ac();
    FUN_800f9584();

    _800c6b9c.setu(0);
    _800c69c8.setu(0);
    _800c6b68.setu(0);

    //LAB_800ee764
    for(int combatantIndex = 0; combatantIndex < 9; combatantIndex++) {
      _800c6b78.offset(combatantIndex * 0x4L).setu(-0x1L);

      //LAB_800ee770
      for(int v1 = 0; v1 < 22; v1++) {
        currentEnemyNames_800c69d0.get(combatantIndex).charAt(v1, 0xa0ff);
      }
    }

    //LAB_800ee7b0
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      //LAB_800ee7b8
      for(int v1 = 0; v1 < 22; v1++) {
        _800c6ba8.get(charSlot).charAt(v1, 0xa0ff);
      }
    }

    checkForPsychBombX();

    usedRepeatItems_800c6c3c.set(0);

    //LAB_800ee80c
    for(int repeatItemIndex = 0; repeatItemIndex < 9; repeatItemIndex++) {
      //LAB_800ee824
      for(int itemSlot = 0; itemSlot < gameState_800babc8.itemCount_1e6.get(); itemSlot++) {
        if(gameState_800babc8.items_2e9.get(itemSlot).get() == repeatItemIds_800c6e34.get(repeatItemIndex).get()) {
          usedRepeatItems_800c6c3c.or(1 << repeatItemIndex);
          break;
        }

        //LAB_800ee848
      }

      //LAB_800ee858
    }

    _800c697e.setu(0);
    _800c6980.setu(0);
    _800c6b64.setu(-0x1L);

    //LAB_800ee894
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      spGained_800bc950.get(charSlot).set(0);
    }

    FUN_80023a88();
    FUN_800f83c8();
  }

  @Method(0x800ee8c4L)
  public static void battleHudTexturesLoadedCallback(final List<byte[]> files) {
    final short[] clutX = new short[6];
    for(int i = 0; i < 4; i++) {
      clutX[i] = _800c6e60.get(i).get();
    }

    clutX[4] = 0;
    clutX[5] = 16;

    //LAB_800ee9c0
    for(int fileIndex = 0; fileIndex < files.size(); fileIndex++) {
      if(files.get(fileIndex).length != 0) {
        final Tim tim = new Tim(files.get(fileIndex));

        if(fileIndex == 0) {
          GPU.uploadData(new RECT().set((short)704, (short)256, (short)64, (short)256), tim.getData(), tim.getImageData());
        }

        //LAB_800eea20
        final RECT sp0x30 = new RECT();
        if(fileIndex < 4) {
          sp0x30.x.set((short)(clutX[fileIndex] + 704));
          sp0x30.y.set((short)496);
        } else {
          //LAB_800eea3c
          sp0x30.x.set((short)(clutX[fileIndex] + 896));
          sp0x30.y.set((short)304);
        }

        //LAB_800eea50
        sp0x30.w.set(_800c6e48.get(fileIndex).getX());
        sp0x30.h.set(_800c6e48.get(fileIndex).getY());
        GPU.uploadData(sp0x30, tim.getData(), tim.getClutData());
        _800c6cf4.addu(0x1L);
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
      final BattleObject27c bobj = _8006e398.charBobjIndices_e40[charSlot].innerStruct_00;
      final CharacterData2c charData = gameState_800babc8.charData_32c.get(bobj.charIndex_272);

      //LAB_800eec10
      charData.hp_08.set(Math.max(1, bobj.hp_08));

      if((gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0x1 << characterDragoonIndices_800c6e68.get(bobj.charIndex_272).get()) != 0) {
        charData.mp_0a.set(bobj.mp_0c);
      }

      //LAB_800eec78
      if(bobj.charIndex_272 == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0x1 << characterDragoonIndices_800c6e68.get(9).get()) != 0) {
        charData.mp_0a.set(bobj.mp_0c);
      }

      //LAB_800eecb8
      charData.status_10.set(bobj.status_0e & 0xc8);
      charData.sp_0c.set(bobj.sp_0a);
    }

    //LAB_800eecf4
    if((gameState_800babc8.scriptFlags2_bc.get(0xd).get() & 0x4_0000) != 0) { // Used Psych Bomb X this battle
      //LAB_800eed30
      boolean hasPsychBombX = false;
      for(int i = 0; i < gameState_800babc8.itemCount_1e6.get(); i++) {
        if(gameState_800babc8.items_2e9.get(i).get() == 0xfa) { // Psych Bomb X
          hasPsychBombX = true;
          break;
        }
      }

      //LAB_800eed54
      if(!hasPsychBombX) {
        giveItem(0xfa); // Psych Bomb X
      }
    }

    //LAB_800eed64
    checkForPsychBombX();

    //LAB_800eed78
    for(int repeatItemIndex = 0; repeatItemIndex < 9; repeatItemIndex++) {
      if((usedRepeatItems_800c6c3c.get() >> repeatItemIndex & 1) != 0) {
        boolean hasRepeatItem = false;

        //LAB_800eedb0
        for(int itemSlot = 0; itemSlot < gameState_800babc8.itemCount_1e6.get(); itemSlot++) {
          if(gameState_800babc8.items_2e9.get(itemSlot).get() == repeatItemIds_800c6e34.get(repeatItemIndex).get()) {
            hasRepeatItem = true;
            break;
          }
        }

        //LAB_800eedd8
        if(!hasRepeatItem) {
          giveItem(repeatItemIds_800c6e34.get(repeatItemIndex).get());
        }
      }
    }

    usedRepeatItems_800c6c3c.set(0);

    free(displayStats_800c6c2c.getPointer());
    free(floatingNumbers_800c6b5c.getPointer());
    free(_800c6b60.getPointer());
    free(battleMenu_800c6c34.getPointer());
    free(_800c6b6c.get());
  }

  @Method(0x800eee80L)
  public static void loadMonster(final ScriptState<BattleObject27c> state) {
    final long t8 = _800c6e90.getAddress();

    final long[] sp0x10 = {
      MEMORY.ref(4, t8).offset(0x0L).get(),
      MEMORY.ref(4, t8).offset(0x4L).get(),
      MEMORY.ref(4, t8).offset(0x8L).get(),
    };

    //LAB_800eeecc
    for(int i = 0; i < 3; i++) {
      final LodString a0_0 = enemyNames_80112068.get((int)sp0x10[i]).deref();

      //LAB_800eeee0
      for(int charIndex = 0; ; charIndex++) {
        _800c6ba8.get(i).charAt(charIndex, a0_0.charAt(charIndex));

        if(a0_0.charAt(charIndex) >= 0xa0ffL) {
          break;
        }
      }

      //LAB_800eef0c
    }

    final BattleObject27c monster = state.innerStruct_00;
    final LodString name = enemyNames_80112068.get(monster.charIndex_272).deref();

    //LAB_800eef7c
    for(int charIndex = 0; ; charIndex++) {
      currentEnemyNames_800c69d0.get((int)_800c6b9c.get()).charAt(charIndex, name.charAt(charIndex));

      if(name.charAt(charIndex) >= 0xa0ffL) {
        break;
      }
    }

    //LAB_800eefa8
    _800c6b78.offset(_800c6b9c.get() * 0x4L).setu(state.index);
    _800c6b9c.addu(0x1L);

    //LAB_800eefcc
    final MonsterStats1c monsterStats = monsterStats_8010ba98.get(monster.charIndex_272);
    monster.hp_08 = monsterStats.hp_00.get();
    monster.mp_0c = monsterStats.mp_02.get();
    monster.maxHp_10 = monsterStats.hp_00.get();
    monster.maxMp_12 = monsterStats.mp_02.get();
    monster.specialEffectFlag_14 = monsterStats.specialEffectFlag_0d.get();
    monster._16 = 0;
    monster._18 = 0;
    monster._1a = 0;
    monster.elementFlag_1c = monsterStats.elementFlag_0f.get();
    monster._1e = monsterStats._0e.get();
    monster.elementalResistanceFlag_20 = 0;
    monster.elementalImmunityFlag_22 = monsterStats.elementalImmunityFlag_10.get();
    monster.statusResistFlag_24 = monsterStats.statusResistFlag_11.get();
    monster._26 = 0;
    monster._28 = 0;
    monster._2a = 0;
    monster._2c = 0;
    monster._2e = 0;
    monster._30 = 0;
    monster.speed_32 = monsterStats.speed_08.get();
    monster.attack_34 = monsterStats.attack_04.get();
    monster.magicAttack_36 = monsterStats.magicAttack_06.get();
    monster.defence_38 = monsterStats.defence_09.get();
    monster.magicDefence_3a = monsterStats.magicDefence_0a.get();
    monster.attackHit_3c = 0;
    monster.magicHit_3e = 0;
    monster.attackAvoid_40 = monsterStats.attackAvoid_0b.get();
    monster.magicAvoid_42 = monsterStats.magicAvoid_0c.get();
    monster.onHitStatusChance_44 = 0;
    monster._46 = 0;
    monster._48 = 0;
    monster.onHitStatus_4a = 0;
    monster.selectedAddition_58 = -1;
    monster.originalHp_5c = monsterStats.hp_00.get();
    monster.originalMp_5e = monsterStats.mp_02.get();
    monster.originalAttack_60 = monsterStats.attack_04.get();
    monster.originalMagicAttack_62 = monsterStats.magicAttack_06.get();
    monster.originalSpeed_64 = monsterStats.speed_08.get();
    monster.originalDefence_66 = monsterStats.defence_09.get();
    monster.originalMagicDefence_68 = monsterStats.magicDefence_0a.get();
    monster.originalAttackAvoid_6a = monsterStats.attackAvoid_0b.get();
    monster.originalMagicAvoid_6c = monsterStats.magicAvoid_0c.get();
    monster.damageReductionFlags_6e = monsterStats.specialEffectFlag_0d.get();
    monster._70 = monsterStats._0e.get();
    monster.monsterElementFlag_72 = monsterStats.elementFlag_0f.get();
    monster.monsterElementalImmunityFlag_74 = monsterStats.elementalImmunityFlag_10.get();
    monster.monsterStatusResistFlag_76 = monsterStats.statusResistFlag_11.get();
    monster._78.set(monsterStats.x_12.get(), monsterStats.y_13.get(), monsterStats.z_14.get());
    monster._7e = monsterStats._15.get();
    monster._80 = monsterStats._16.get();
    monster._82 = monsterStats._17.get();
    monster._84 = monsterStats._18.get();
    monster._86 = monsterStats._19.get();
    monster._88 = monsterStats._1a.get();
    monster._8a = monsterStats._1b.get();

    if((monster.damageReductionFlags_6e & 0x8) != 0) {
      monster.physicalImmunity_110 = 1;
    }

    //LAB_800ef25c
    if((monster.damageReductionFlags_6e & 0x4) != 0) {
      monster.magicalImmunity_112 = 1;
    }

    //LAB_800ef274
    decrementOverlayCount();
  }

  @Method(0x800ef28cL)
  public static void FUN_800ef28c() {
    //LAB_800ef2c4
    //TODO sp0x18 is unused, why?
    //memcpy(sp0x18, _800c6e68.getAddress(), 0x28);

    loadCharacterStats(0x1L);

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
      final BattleObject27c s0 = _8006e398.charBobjIndices_e40[charSlot].innerStruct_00;
      final byte[] spellIndices = new byte[8];
      getUnlockedDragoonSpells(spellIndices, s0.charIndex_272);
      dragoonSpells_800c6960.get(charSlot).charIndex_00.set(s0.charIndex_272);

      //LAB_800ef3d8
      for(int spellIndex = 0; spellIndex < 8; spellIndex++) {
        dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).set(spellIndices[spellIndex]);
      }

      //LAB_800ef400
      final ActiveStatsa0 stats = stats_800be5f8.get(s0.charIndex_272);
      s0.level_04 = stats.level_0e.get();
      s0.dlevel_06 = stats.dlevel_0f.get();
      s0.hp_08 = stats.hp_04.get();
      s0.sp_0a = stats.sp_08.get();
      s0.mp_0c = stats.mp_06.get();
      s0.status_0e = stats.dragoonFlag_0c.get();
      s0.maxHp_10 = stats.maxHp_66.get();
      s0.maxMp_12 = stats.maxMp_6e.get();
      s0.specialEffectFlag_14 = stats.specialEffectFlag_76.get();
      s0._16 = stats._77.get();
      s0._18 = stats._78.get();
      s0._1a = stats._79.get();
      s0.elementFlag_1c = stats.elementFlag_7a.get();
      s0._1e = stats._7b.get();
      s0.elementalResistanceFlag_20 = stats.elementalResistanceFlag_7c.get();
      s0.elementalImmunityFlag_22 = stats.elementalImmunityFlag_7d.get();
      s0.statusResistFlag_24 = stats.statusResistFlag_7e.get();
      s0._26 = stats._7f.get();
      s0._28 = stats._80.get();
      s0._2a = stats._81.get();
      s0._2c = stats._82.get();
      s0._2e = stats._83.get();
      s0._30 = stats._84.get();
      s0.speed_32 = stats.gearSpeed_86.get() + stats.bodySpeed_69.get();
      s0.attack_34 = stats.gearAttack_88.get() + stats.bodyAttack_6a.get();
      s0.magicAttack_36 = stats.gearMagicAttack_8a.get() + stats.bodyMagicAttack_6b.get();
      s0.defence_38 = stats.gearDefence_8c.get() + stats.bodyDefence_6c.get();
      s0.magicDefence_3a = stats.gearMagicDefence_8e.get() + stats.bodyMagicDefence_6d.get();
      s0.attackHit_3c = stats.attackHit_90.get();
      s0.magicHit_3e = stats.magicHit_92.get();
      s0.attackAvoid_40 = stats.attackAvoid_94.get();
      s0.magicAvoid_42 = stats.magicAvoid_96.get();
      s0.onHitStatusChance_44 = stats.onHitStatusChance_98.get();
      s0._46 = stats._99.get();
      s0._48 = stats._9a.get();
      s0.onHitStatus_4a = stats.onHitStatus_9b.get();
      s0.spellId_4e = stats.onHitStatus_9b.get();
      s0.selectedAddition_58 = stats.selectedAddition_35.get();
      s0.dragoonAttack_ac = stats.dragoonAttack_72.get();
      s0.dragoonMagic_ae = stats.dragoonMagicAttack_73.get();
      s0.dragoonDefence_b0 = stats.dragoonDefence_74.get();
      s0.dragoonMagicDefence_b2 = stats.dragoonMagicDefence_75.get();
      s0.physicalImmunity_110 = stats.physicalImmunity_46.get();
      s0.magicalImmunity_112 = stats.magicalImmunity_48.get();
      s0.physicalResistance_114 = stats.physicalResistance_4a.get();
      s0.magicalResistance_116 = stats.magicalResistance_60.get();
      s0._118 = stats._9c.get();
      s0.additionSpMultiplier_11a = stats.additionSpMultiplier_9e.get();
      s0.additionDamageMultiplier_11c = stats.additionDamageMultiplier_9f.get();
      s0.equipment0_11e = stats.equipment_30.get(0).get();
      s0.equipment1_120 = stats.equipment_30.get(1).get();
      s0.equipment2_122 = stats.equipment_30.get(2).get();
      s0.equipment3_124 = stats.equipment_30.get(3).get();
      s0.equipment4_126 = stats.equipment_30.get(4).get();
      s0.spMultiplier_128 = stats.spMultiplier_4c.get();
      s0.spPerPhysicalHit_12a = stats.spPerPhysicalHit_4e.get();
      s0.mpPerPhysicalHit_12c = stats.mpPerPhysicalHit_50.get();
      s0.itemSpPerMagicalHit_12e = stats.spPerMagicalHit_52.get();
      s0.mpPerMagicalHit_130 = stats.mpPerMagicalHit_54.get();
      s0._132 = stats._56.get();
      s0.hpRegen_134 = stats.hpRegen_58.get();
      s0.mpRegen_136 = stats.mpRegen_5a.get();
      s0.spRegen_138 = stats.spRegen_5c.get();
      s0.revive_13a = stats.revive_5e.get();
      s0.hpMulti_13c = stats.hpMulti_62.get();
      s0.mpMulti_13e = stats.mpMulti_64.get();
      s0._142 = stats.onHitStatus_9b.get();
    }

    //LAB_800ef798
  }

  @Method(0x800ef7c4L)
  public static void FUN_800ef7c4() {
    //LAB_800ef7d4
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleStruct3c v1 = _800c6c40.get(charSlot);
      v1.charIndex_00.set((short)-1);
      v1._04.set((short)0);
      v1.flags_06.set((short)0);
      v1.x_08.set((short)0);
      v1.y_0a.set((short)0);
      v1._0c.set((short)0);
      v1._0e.set((short)0);
      v1._10.set((short)0);
      v1._12.set((short)0);
    }

    //LAB_800ef818
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleDisplayStats144 displayStats = displayStats_800c6c2c.deref().get(charSlot);

      //LAB_800ef820
      for(int a1 = 0; a1 < 5; a1++) {
        //LAB_800ef828
        for(int a0 = 0; a0 < 4; a0++) {
          displayStats._04.get(a1).get(a0)._00.set((short)-1);
        }
      }
    }

    //LAB_800ef878
    for(int i = 0; i < 12; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c.deref().get(i);
      num.state_00.set(0);
      num.flags_02.set(0);
      num.bobjIndex_04.set(-1);
      num.translucent_08.set(false);
      num.b_0c.set(0x80);
      num.g_0d.set(0x80);
      num.r_0e.set(0x80);
      num._14.set(-1);
      num._18.set(-1);

      //LAB_800ef89c
      for(int a1 = 0; a1 < 5; a1++) {
        final FloatingNumberC4Sub20 v1 = num.digits_24.get(a1);
        v1._00.set(0);
        v1._04.set(0);
        v1._08.set(0);
        v1.digit_0c.set((short)-1);
        v1._1c.set(0);
      }
    }
  }

  @Method(0x800ef8d8L)
  public static void FUN_800ef8d8(final int charSlot) {
    final BattleStruct3c a0_0 = _800c6c40.get(charSlot);
    a0_0.charIndex_00.set((short)charSlot);
    a0_0._02.set((short)_8006e398.charBobjIndices_e40[charSlot].innerStruct_00.charIndex_272);
    a0_0._04.set((short)0);
    a0_0.flags_06.or(0x2);
    a0_0.x_08.set((short)(charSlot * 94 + 63));
    a0_0.y_0a.set((short)38);
    a0_0._10.set((short)32);
    a0_0._12.set((short)17);

    //LAB_800ef980
    for(int i = 0; i < 10; i++) {
      a0_0._14.get(i).set(0);
    }

    final BattleDisplayStats144 displayStats = displayStats_800c6c2c.deref().get(charSlot);
    displayStats.x_00.set(a0_0.x_08.get());
    displayStats.y_02.set(a0_0.y_0a.get());
  }

  @Method(0x800ef9e4L)
  public static void FUN_800ef9e4() {
    if(_800c6cf4.get() == 0x6L) {
      if (Config.changeBattleRGB()) {
        Bttl_800c._800c7004.set(Config.getBattleRGB());
      }

      final long charCount = charCount_800c677c.get();

      //LAB_800efa34
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        if(_800c6c40.get(charSlot).charIndex_00.get() == -1 && _800be5d0.get() == 0x1L) {
          FUN_800ef8d8(charSlot);
        }

        //LAB_800efa64
      }

      //LAB_800efa78
      //LAB_800efa94
      //LAB_800efaac
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        final BattleStruct3c s2 = _800c6c40.get(charSlot);

        if(s2.charIndex_00.get() != -1 && (s2.flags_06.get() & 0x1L) != 0 && (s2.flags_06.get() & 0x2L) != 0) {
          final BattleObject27c bobj = _8006e398.charBobjIndices_e40[charSlot].innerStruct_00;

          final int textEffect;
          if(bobj.hp_08 > bobj.maxHp_10 / 2) {
            textEffect = 1;
          } else if(bobj.hp_08 > bobj.maxHp_10 / 4) {
            textEffect = 2;
          } else {
            textEffect = 3;
          }

          //LAB_800efb30
          //LAB_800efb40
          //LAB_800efb54
          renderNumber(charSlot, 0, bobj.hp_08, textEffect);
          renderNumber(charSlot, 1, bobj.maxHp_10, 1);
          renderNumber(charSlot, 2, bobj.mp_0c, 1);
          renderNumber(charSlot, 3, bobj.maxMp_12, 1);
          renderNumber(charSlot, 4, bobj.sp_0a / 100, 1);

          s2._14.get(1).set(tickCount_800bb0fc.get() & 0x3);

          //LAB_800efc0c
          if(bobj.sp_0a < bobj.dlevel_06 * 100) {
            s2.flags_06.and(0xfff3);
          } else {
            s2.flags_06.or(0x4);
          }

          //LAB_800efc6c
          if((s2.flags_06.get() & 0x4) != 0) {
            s2.flags_06.xor(0x8);
          }

          //LAB_800efc84
          if(s2._14.get(2).get() < 6) {
            s2._14.get(2).incr();
          }
        }

        //LAB_800efc9c
      }

      //LAB_800efcac
      final long v1 = _800fb198.offset(_800c6c38.get() * 0x2L).getAddress();

      //LAB_800efcdc
      for(int charSlot = 0; charSlot < charCount; charSlot++) {
        final BattleDisplayStats144 displayStats = displayStats_800c6c2c.deref().get(charSlot);
        final BattleStruct3c a1 = _800c6c40.get(charSlot);
        a1.y_0a.set((short)MEMORY.ref(2, v1).offset(0x0L).get());
        displayStats.y_02.set((short)MEMORY.ref(2, v1).offset(0x0L).get());
      }

      //LAB_800efd00
      FUN_800f3940();
      FUN_800f4b80();
    }

    //LAB_800efd10
  }

  @Method(0x800efd34L)
  public static void drawUiElements() {
    int spf0 = 0;

    //LAB_800efe04
    //LAB_800efe9c
    //LAB_800eff1c
    //LAB_800eff70
    //LAB_800effa0
    if((int)_800c6cf4.get() >= 0x6L) {
      //LAB_800f0000
      //LAB_800f0074
      for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        final BattleDisplayStats144 displayStats = displayStats_800c6c2c.deref().get(charSlot);
        final BattleStruct3c s7 = _800c6c40.get(charSlot);

        if(s7.charIndex_00.get() != -1 && (s7.flags_06.get() & 0x1) != 0 && (s7.flags_06.get() & 0x2) != 0) {
          final ScriptState<BattleObject27c> state = _8006e398.charBobjIndices_e40[charSlot];
          final BattleObject27c data = state.innerStruct_00;
          final int spec;
          int s5;
          if((currentTurnBobj_800c66c8.storage_44[7] & 0x4) != 0x1 && currentTurnBobj_800c66c8 == state) {
            spec = 2;
            s5 = 2;
          } else {
            spec = 0;
            s5 = 1;
          }

          //LAB_800f0108
          int s2;
          if((data.status_0e & 0x2000) == 0) {
            s2 = 4;
          } else {
            s2 = 5;
          }

          //LAB_800f0120
          //LAB_800f0128
          for(int i = 0; i < s2; i++) {
            //LAB_800f0134
            for(int n = 0; n < 4; n++) {
              final BattleDisplayStats144Sub10 struct = displayStats._04.get(i).get(n);
              if(struct._00.get() == -1) {
                break;
              }

              // Numbers
              drawUiTextureElement(
                displayStats.x_00.get() + struct.x_02.get() - centreScreenX_1f8003dc.get(),
                displayStats.y_02.get() + struct.y_04.get() - centreScreenY_1f8003de.get(),
                struct.u_06.get(),
                struct.v_08.get(),
                struct.w_0a.get(),
                struct.h_0c.get(),
                struct._0e.get(),
                spec,
                s7._14.get(2).get()
              );
            }

            //LAB_800f01e0
          }

          //LAB_800f01f0
          final long s0 = _800fb444.offset(data.charIndex_272 * 0x4L).get();

          // Names
          drawUiTextureElement(displayStats.x_00.get() - centreScreenX_1f8003dc.get() + 1, displayStats.y_02.get() - centreScreenY_1f8003de.get() - 25, (int)MEMORY.ref(1, s0).offset(0x0L).get(), (int)MEMORY.ref(1, s0).offset(0x1L).get(), (int)MEMORY.ref(1, s0).offset(0x2L).get(), (int)MEMORY.ref(1, s0).offset(0x3L).get(), 0x2c, spec, s7._14.get(2).get());

          // Portraits
          drawUiTextureElement(displayStats.x_00.get() - centreScreenX_1f8003dc.get() - 44, displayStats.y_02.get() - centreScreenY_1f8003de.get() - 22, (int)MEMORY.ref(1, s0).offset(0x4L).get(), (int)MEMORY.ref(1, s0).offset(0x5L).get(), (int)MEMORY.ref(1, s0).offset(0x6L).get(), (int)MEMORY.ref(1, s0).offset(0x7L).get(), (int)MEMORY.ref(1, s0).offset(0x8L).get(), s5, s7._14.get(2).get());

          if(spec != 0) {
            final int v1_0 = (6 - s7._14.get(2).get()) * 8 + 100;
            final int x = displayStats.x_00.get() - centreScreenX_1f8003dc.get() + (int)MEMORY.ref(1, s0).offset(0x6L).get() / 2 - 44;
            final int y = displayStats.y_02.get() - centreScreenY_1f8003de.get() + (int)MEMORY.ref(1, s0).offset(0x7L).get() / 2 - 22;
            int v1 = ((int)MEMORY.ref(1, s0).offset(0x6L).get() + 2) * v1_0 / 100 / 2;
            final int x0 = x - v1;
            final int x1 = x + v1 - 1;

            final short[] xs = {(short)x0, (short)x1, (short)x0, (short)x1};

            v1 = ((int)MEMORY.ref(1, s0).offset(0x7L).get() + 2) * v1_0 / 100 / 2;
            final int y0 = y - v1;
            final int y1 = y + v1 - 1;

            final short[] ys = {(short)y0, (short)y0, (short)y1, (short)y1};

            //LAB_800f0438
            for(s2 = 0; s2 < 8; s2++) {
              v1 = s7._14.get(2).get();

              final int r;
              final int g;
              final int b;
              final boolean translucent;
              if(v1 < 6) {
                r = v1 * 0x2a;
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
              final int t5 = s2 / 4;
              final long t0 = _800c6e9c.offset(s2 % 4 * 0xcL).getAddress();

              // Draw border around currently active character's portrait
              drawLine(
                xs[(int)MEMORY.ref(1, t0).offset(0x0L).getSigned()] + (int)MEMORY.ref(1, t0).offset(0x4L).getSigned() + (int)MEMORY.ref(1, t0).offset(0x8L).getSigned() * t5,
                ys[(int)MEMORY.ref(1, t0).offset(0x1L).getSigned()] + (int)MEMORY.ref(1, t0).offset(0x5L).getSigned() + (int)MEMORY.ref(1, t0).offset(0x9L).getSigned() * t5,
                xs[(int)MEMORY.ref(1, t0).offset(0x2L).getSigned()] + (int)MEMORY.ref(1, t0).offset(0x6L).getSigned() + (int)MEMORY.ref(1, t0).offset(0xaL).getSigned() * t5,
                ys[(int)MEMORY.ref(1, t0).offset(0x3L).getSigned()] + (int)MEMORY.ref(1, t0).offset(0x7L).getSigned() + (int)MEMORY.ref(1, t0).offset(0xbL).getSigned() * t5,
                r,
                g,
                b,
                translucent
              );
            }
          }

          //LAB_800f05d4
          final boolean canTransform = (data.status_0e & 0x2000) != 0;

          //LAB_800f05f4
          int s3 = 0;
          for(int i = 0; i < 3; i++) {
            if(i == 2 && !canTransform) {
              s3 = -10;
            }

            //LAB_800f060c
            final long v1_0 = _800c6ecc.offset(i * 0xcL).getAddress();

            // HP: /  MP: /  SP:
            //LAB_800f0610
            drawUiTextureElement(
              (short)MEMORY.ref(2, v1_0).offset(0x0L).get() + displayStats.x_00.get() - centreScreenX_1f8003dc.get(),
              (short)MEMORY.ref(2, v1_0).offset(0x2L).get() + displayStats.y_02.get() - centreScreenY_1f8003de.get(),
              (int)MEMORY.ref(1, v1_0).offset(0x4L).get(),
              (int)MEMORY.ref(1, v1_0).offset(0x6L).get(),
              (short)MEMORY.ref(2, v1_0).offset(0x8L).getSigned(),
              (short)MEMORY.ref(2, v1_0).offset(0xaL).get() + s3,
              0x2c,
              spec,
              s7._14.get(2).get()
            );
          }

          if(canTransform) {
            final int sp = data.sp_0a;
            s5 = sp / 100;
            s2 = sp % 100;

            //SP bars
            //LAB_800f0714
            for(s3 = 0; s3 < 2; s3++) {
              int s1;
              if(s3 == 0) {
                s1 = s2;
                spf0 = s5 + 1;
                //LAB_800f0728
              } else if(s5 == 0) {
                s1 = 0;
              } else {
                s1 = 100;
                spf0 = s5;
              }

              //LAB_800f0738
              s1 = Math.max(0, (short)s1 * 35 / 100);

              //LAB_800f0780
              final int left = displayStats.x_00.get() - centreScreenX_1f8003dc.get() + 3;
              final int top = displayStats.y_02.get() - centreScreenY_1f8003de.get() + 8;
              final int right = left + s1;
              final int bottom = top + 3;

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .pos(0, left, top)
                .pos(1, right, top)
                .pos(2, left, bottom)
                .pos(3, right, bottom);

              long addr = _800c6f04.offset(spf0 * 0x6L).getAddress();

              cmd
                .rgb(0, (int)MEMORY.ref(1, addr).offset(0x0L).get(), (int)MEMORY.ref(1, addr).offset(0x1L).get(), (int)MEMORY.ref(1, addr).offset(0x2L).get())
                .rgb(1, (int)MEMORY.ref(1, addr).offset(0x0L).get(), (int)MEMORY.ref(1, addr).offset(0x1L).get(), (int)MEMORY.ref(1, addr).offset(0x2L).get());

              addr = _800c6f04.offset(spf0 * 0x6L + 0x3L).getAddress();

              cmd
                .rgb(2, (int)MEMORY.ref(1, addr).offset(0x0L).get(), (int)MEMORY.ref(1, addr).offset(0x1L).get(), (int)MEMORY.ref(1, addr).offset(0x2L).get())
                .rgb(3, (int)MEMORY.ref(1, addr).offset(0x0L).get(), (int)MEMORY.ref(1, addr).offset(0x1L).get(), (int)MEMORY.ref(1, addr).offset(0x2L).get());

              GPU.queueCommand(31, cmd);
            }

            //SP border
            //LAB_800f0910
            for(int i = 0; i < 4; i++) {
              final int offsetX = displayStats.x_00.get() - centreScreenX_1f8003dc.get();
              final int offsetY = displayStats.y_02.get() - centreScreenY_1f8003de.get();
              drawLine((int)_800fb46c.get(i * 4).get() + offsetX, _800fb46c.get(i * 4 + 1).get() + offsetY, _800fb46c.get(i * 4 + 2).get() + offsetX, _800fb46c.get(i * 4 + 3).get() + offsetY, 0x60, 0x60, 0x60, false);
            }

            //Full SP meter
            if((s7.flags_06.get() & 0x8) != 0) {
              //LAB_800f09ec
              for(int i = 0; i < 4; i++) {
                final int offsetX = displayStats.x_00.get() - centreScreenX_1f8003dc.get();
                final int offsetY = displayStats.y_02.get() - centreScreenY_1f8003de.get();
                drawLine((int)_800fb47c.get(i * 4).get() + offsetX, _800fb47c.get(i * 4 + 1).get() + offsetY, _800fb47c.get(i * 4 + 2).get() + offsetX, _800fb47c.get(i * 4 + 3).get() + offsetY, 0x80, 0, 0, false);
              }
            }
          }
        }
      }

      //LAB_800f0ad4
      // Background
      if(_800c6c40.get(0).charIndex_00.get() != -1 && (_800c6c40.get(0).flags_06.get() & 0x1) != 0) {
        renderTextBoxBackground(16, (int)_800fb198.offset(2, _800c6c38.get() * 0x2L).get() - 26, 288, 40, 8);
      }

      //LAB_800f0b3c
      drawFloatingNumbers();

      // Use item menu
      drawItemMenuElements();

      // Targeting
      final BattleMenuStruct58 menu = battleMenu_800c6c34.deref();
      if(menu._4c.get() != 0) {
        drawTargetArrow(menu.targetType_50.get(), menu.combatantIndex.get());
        final int targetCombatant = menu.combatantIndex.get();
        LodString str;
        int element;
        if(targetCombatant == -1) {  // Target all
          str = targeting_800fb36c.get(menu.targetType_50.get()).deref();
          element = 3;
        } else {  // Target single
          final BattleObject27c targetBobj;

          //LAB_800f0bb0
          if(menu.targetType_50.get() == 1) {
            //LAB_800f0ca4
            targetBobj = _8006e398.enemyBobjIndices_ebc[targetCombatant].innerStruct_00;

            //LAB_800f0cf0
            int enemySlot;
            for(enemySlot = 0; enemySlot < monsterCount_800c6768.get(); enemySlot++) {
              if(_800c6b78.offset(enemySlot * 0x4L).get() == menu.target_48.get()) {
                break;
              }
            }

            //LAB_800f0d10
            str = getTargetEnemyName(targetBobj, currentEnemyNames_800c69d0.get(enemySlot));
            element = getTargetEnemyElement(targetBobj.elementFlag_1c);
          } else if(menu.targetType_50.get() == 0) {
            targetBobj = _8006e398.charBobjIndices_e40[targetCombatant].innerStruct_00;
            str = playerNames_800fb378.get(targetBobj.charIndex_272).deref();
            element = (int)_800c6ef0.offset(2, targetBobj.charIndex_272 * 0x2L).get();

            if(targetBobj.charIndex_272 == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0 && (_8006e398.charBobjIndices_e40[menu.combatantIndex.get()].storage_44[7] & 0x2) != 0) {
              element = (int)_800c6ef0.offset(0x12L).get();
            }
          } else {
            //LAB_800f0d58
            //LAB_800f0d5c
            final ScriptState<BattleObject27c> state = _8006e398.bobjIndices_e0c[targetCombatant];
            targetBobj = state.innerStruct_00;
            if((state.storage_44[7] & 0x4) == 0) {
              str = playerNames_800fb378.get(targetBobj.charIndex_272).deref();
              element = (int)_800c6ef0.offset(2, targetBobj.charIndex_272 * 0x2L).get();

              if(targetBobj.charIndex_272 == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0 && (_8006e398.charBobjIndices_e40[menu.combatantIndex.get()].storage_44[7] & 0x2) != 0) {
                element = (int)_800c6ef0.offset(0x12L).get();
              }
            } else {
              //LAB_800f0e24
              str = getTargetEnemyName(targetBobj, currentEnemyNames_800c69d0.get(targetCombatant));
              element = getTargetEnemyElement(targetBobj.elementFlag_1c);
            }
          }

          //LAB_800f0e60
          final int status = targetBobj.status_0e;

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
        renderTextBoxBackground(44, 23, 232, 14, (short)element);
        renderText(str, 160 - textWidth(str) / 2, 24, 0, 0);
      }
    }

    //LAB_800f0f2c
  }
}
