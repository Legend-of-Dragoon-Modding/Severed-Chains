package legend.game.combat;

import legend.core.MemoryHelper;
import legend.core.cdrom.FileLoadingInfo;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
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
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.combat.types.BattleDisplayStats144;
import legend.game.combat.types.BattleDisplayStats144Sub10;
import legend.game.combat.types.BattleLightStruct64;
import legend.game.combat.types.BattleMenuStruct58;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleRenderStruct;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStruct14;
import legend.game.combat.types.BattleStruct24;
import legend.game.combat.types.BattleStruct24_2;
import legend.game.combat.types.BattleStruct3c;
import legend.game.combat.types.BattleStruct4c;
import legend.game.combat.types.BattleStruct7cc;
import legend.game.combat.types.BttlLightStruct84;
import legend.game.combat.types.BttlLightStruct84Sub3c;
import legend.game.combat.types.BttlScriptData6cInner;
import legend.game.combat.types.BttlScriptData6cSub0c;
import legend.game.combat.types.BttlScriptData6cSub13c;
import legend.game.combat.types.BttlScriptData6cSub14_2;
import legend.game.combat.types.BttlScriptData6cSub1c;
import legend.game.combat.types.BttlScriptData6cSub20;
import legend.game.combat.types.BttlScriptData6cSubBase1;
import legend.game.combat.types.BttlScriptData6cSubBase2;
import legend.game.combat.types.BttlStruct50;
import legend.game.combat.types.BttlStructa4;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.DeffFile;
import legend.game.combat.types.DeffPart;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.combat.types.MonsterStats1c;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.BigStruct;
import legend.game.types.CharacterData2c;
import legend.game.types.DR_MOVE;
import legend.game.types.ExtendedTmd;
import legend.game.types.GsOT_TAG;
import legend.game.types.LodString;
import legend.game.types.ModelPartTransforms;
import legend.game.types.MrgFile;
import legend.game.types.RunningScript;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;
import legend.game.types.TmdAnimationFile;

import javax.annotation.Nullable;
import java.util.function.Function;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SItem.FUN_80110030;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_8001d068;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment._1f8003ee;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment._1f8003f8;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.deallocateScriptAndChildren;
import static legend.game.Scus94491BpeSegment.getDrgnFilePos;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadAndRunOverlay;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback08;
import static legend.game.Scus94491BpeSegment.setScriptDestructor;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020fe0;
import static legend.game.Scus94491BpeSegment_8002.FUN_800214bc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021ca0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023a88;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.checkForPsychBombX;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.strcpy;
import static legend.game.Scus94491BpeSegment_8002.textLen;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003ec90;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003ef50;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f210;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetAmbient;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMove;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.TransposeMatrix;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.setGp0_38;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040780;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8005.orderingTables_8005a370;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bda0c;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.bigStruct_800bda10;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.scriptState_800bc0c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;
import static legend.game.combat.Bttl_800c.FUN_800c9708;
import static legend.game.combat.Bttl_800c.FUN_800ca418;
import static legend.game.combat.Bttl_800c._800c669c;
import static legend.game.combat.Bttl_800c._800c66c8;
import static legend.game.combat.Bttl_800c._800c6758;
import static legend.game.combat.Bttl_800c._800c6768;
import static legend.game.combat.Bttl_800c._800c6920;
import static legend.game.combat.Bttl_800c._800c6928;
import static legend.game.combat.Bttl_800c._800c6930;
import static legend.game.combat.Bttl_800c._800c6938;
import static legend.game.combat.Bttl_800c._800c6940;
import static legend.game.combat.Bttl_800c._800c6944;
import static legend.game.combat.Bttl_800c._800c6948;
import static legend.game.combat.Bttl_800c._800c6958;
import static legend.game.combat.Bttl_800c._800c695c;
import static legend.game.combat.Bttl_800c._800c697e;
import static legend.game.combat.Bttl_800c._800c6980;
import static legend.game.combat.Bttl_800c._800c69c8;
import static legend.game.combat.Bttl_800c._800c6b5c;
import static legend.game.combat.Bttl_800c._800c6b60;
import static legend.game.combat.Bttl_800c._800c6b64;
import static legend.game.combat.Bttl_800c._800c6b68;
import static legend.game.combat.Bttl_800c._800c6b6c;
import static legend.game.combat.Bttl_800c._800c6b78;
import static legend.game.combat.Bttl_800c._800c6b9c;
import static legend.game.combat.Bttl_800c._800c6ba8;
import static legend.game.combat.Bttl_800c.displayStats_800c6c2c;
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
import static legend.game.combat.Bttl_800c.currentEnemyNames_800c69d0;
import static legend.game.combat.Bttl_800c.currentStage_800c66a4;
import static legend.game.combat.Bttl_800c.deff_800c6950;
import static legend.game.combat.Bttl_800c.dragoonSpells_800c6960;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.light_800c6ddc;
import static legend.game.combat.Bttl_800c.lights_800c692c;
import static legend.game.combat.Bttl_800c.playerNames_800fb378;
import static legend.game.combat.Bttl_800c.repeatItemIds_800c6e34;
import static legend.game.combat.Bttl_800c.script_800faebc;
import static legend.game.combat.Bttl_800c.struct7cc_800c693c;
import static legend.game.combat.Bttl_800c.targeting_800fb36c;
import static legend.game.combat.Bttl_800c.usedRepeatItems_800c6c3c;
import static legend.game.combat.Bttl_800d.FUN_800dd89c;
import static legend.game.combat.Bttl_800d.FUN_800ddac8;
import static legend.game.combat.Bttl_800d.FUN_800de2e8;
import static legend.game.combat.Bttl_800d.FUN_800de36c;
import static legend.game.combat.Bttl_800d.ScaleVectorL_SVEC;
import static legend.game.combat.Bttl_800d.optimisePacketsIfNecessary;
import static legend.game.combat.Bttl_800d.unpackCtmdData;
import static legend.game.combat.Bttl_800f.FUN_800f1268;
import static legend.game.combat.Bttl_800f.FUN_800f1550;
import static legend.game.combat.Bttl_800f.FUN_800f3940;
import static legend.game.combat.Bttl_800f.FUN_800f3dbc;
import static legend.game.combat.Bttl_800f.FUN_800f4964;
import static legend.game.combat.Bttl_800f.FUN_800f4b80;
import static legend.game.combat.Bttl_800f.FUN_800f5c94;
import static legend.game.combat.Bttl_800f.FUN_800f60ac;
import static legend.game.combat.Bttl_800f.FUN_800f83c8;
import static legend.game.combat.Bttl_800f.FUN_800f8568;
import static legend.game.combat.Bttl_800f.FUN_800f8ca0;
import static legend.game.combat.Bttl_800f.FUN_800f8dfc;
import static legend.game.combat.Bttl_800f.FUN_800f9584;
import static legend.game.combat.Bttl_800f.drawLine;
import static legend.game.combat.SBtld.monsterStats_8010ba98;
import static legend.game.combat.SBtld.enemyNames_80112068;
import static legend.game.combat.SEffe.FUN_80114f3c;
import static legend.game.combat.SEffe.FUN_80115cac;

public final class Bttl_800e {
  private Bttl_800e() { }

  /** Normals */
  @Method(0x800e02e8L)
  public static long FUN_800e02e8(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long t2 = _1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

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

        long s6 = CPU.MFC2(24);
        if((int)s6 > 0 || (t2 & 0x2L) != 0 && s6 != 0) {
          //LAB_800e0580
          MEMORY.ref(4, packet).offset(0x0cL).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x12L).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);

          CPU.COP2(0x180001L);

          s6 = CPU.CFC2(31);
          MEMORY.ref(4, packet).offset(0x24L).setu(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
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

            MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(20));
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
            MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(22));
            MEMORY.ref(1, packet).offset(0x0bL).setu(t2);
            MEMORY.ref(4, packet).offset(0x04L).setu(0xe100_0200L | _1f8003ec.get());

            final long norm3 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
            norm.setX((short)((int)(norm3 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm3 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm3 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);

            CPU.COP2(0x108041bL);

            MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(22));

            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);

            packet += 0x28L;
          }
        }
      }

      //LAB_800e07fc
    }

    tmp.release();

    //LAB_800e0804
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Normals */
  @Method(0x800e0848L)
  public static long FUN_800e0848(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long fp = _1f8003ee.get();

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

        final long s5 = CPU.MFC2(24);
        if((int)s5 > 0 || (fp & 0x2L) != 0 && s5 != 0) {
          //LAB_800e0ac4
          MEMORY.ref(4, packet).offset(0x0cL).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(14));
          CPU.COP2(0x158002dL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
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

            MEMORY.ref(4, packet).offset(0x04L).setu(0xe100_0200L | _1f8003ec.getSigned());
            MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(20));
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
            MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(22));
            MEMORY.ref(1, packet).offset(0x0bL).setu(fp);

            final GsOT_TAG tag = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref().get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x700_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x20L;
          }
        }
      }

      //LAB_800e0c4c
    }

    tmp.release();

    //LAB_800e0c54
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Normals */
  @Method(0x800e0c98L)
  public static long FUN_800e0c98(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long a1 = _1f8003ee.get();

    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();
    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    primitives += 0x4L;

    MEMORY.ref(1, packet).offset(0x3L).setu(0xcL);
    MEMORY.ref(4, packet).offset(0x4L).setu((a1 & 0x2L) != 0 ? 0x3e80_8080L : 0x3c80_8080L);

    CPU.MTC2(MEMORY.ref(4, packet).offset(0x4L).get(), 6);

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

      MEMORY.ref(4, packet).offset(0x0cL).setu(unpacked.offset(2, 0x0L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(unpacked.offset(2, 0x4L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(unpacked.offset(2, 0x8L).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        long s6 = CPU.MFC2(24);
        if((int)s6 > 0 || (a1 & 0x2L) != 0 && s6 != 0) {
          //LAB_800e0f5c
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x1eL).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);

          CPU.COP2(0x180001L);

          s6 = CPU.CFC2(31);
          MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          MEMORY.ref(4, packet).offset(0x30L).setu(unpacked.offset(2, 0xcL).get());

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e104c
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

            MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20));
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
            MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22));

            final long norm3 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x1cL).get() * 0x4L).get();
            norm.setX((short)((int)(norm3 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm3 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm3 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, packet).offset(0x28L).setu(CPU.MFC2(22));

            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0xc00_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x34L;
          }
        }
      }

      //LAB_800e11d0
    }

    tmp.release();

    //LAB_800e11d8
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Normals */
  @Method(0x800e121cL)
  public static long FUN_800e121c(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

    long packet = linkedListAddress_1f8003d8.get();
    final long sp38 = _1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x18);
    final Value unpacked = tmp.get();

    final SVECTOR vert = new SVECTOR();
    final SVECTOR norm = new SVECTOR();

    MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
    MEMORY.ref(4, packet).offset(0x4L).setu((sp38 & 0x2L) != 0 ? 0x3680_8080L : 0x3480_8080L);
    CPU.MTC2(MEMORY.ref(4, packet).offset(0x4L).get(), 6); //TODO ???

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

      MEMORY.ref(4, packet).offset(0x0cL).setu(unpacked.offset(4, 0x0L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(unpacked.offset(4, 0x4L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(unpacked.offset(4, 0x8L).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final long s6 = CPU.MFC2(24);
        if((int)s6 > 0 || (sp38 & 0x2L) != 0 && s6 != 0) {
          //LAB_800e14e0
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));
          CPU.COP2(0x158002dL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
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

            MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20));
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
            MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22));

            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x28L;
          }
        }
      }

      //LAB_800e1654
    }

    tmp.release();

    //LAB_800e165c
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Normals, colours */
  @Method(0x800e16a0L)
  public static long FUN_800e16a0(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long t0 = _1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

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

        long s5 = CPU.MFC2(24);
        if((int)s5 > 0 || (t0 & 0x2L) != 0 && s5 != 0) {
          //LAB_800e191c
          MEMORY.ref(4, packet).offset(0x0cL).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x1eL).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);
          CPU.COP2(0x180001L);
          s5 = CPU.CFC2(31);
          MEMORY.ref(4, packet).offset(0x24L).setu(CPU.MFC2(14));
          CPU.COP2(0x168002eL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e19fc

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
            MEMORY.ref(4, packet).offset(0x8L).setu(CPU.MFC2(22));
            MEMORY.ref(1, packet).offset(0xbL).setu(t0);

            CPU.MTC2(unpacked.offset(4, 0x4L).get(), 6);
            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x14L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(22));

            CPU.MTC2(unpacked.offset(4, 0x8L).get(), 6);
            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x18L).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(22));

            CPU.MTC2(unpacked.offset(4, 0xcL).get(), 6);
            final long norm3 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x1cL).get() * 0x4L).get();
            norm.setX((short)((int)(norm3 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm3 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm3 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(22));

            MEMORY.ref(4, packet).offset(0x4L).setu(0xe100_0200L | _1f8003ec.get());

            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x28L;
          }
        }
      }

      //LAB_800e1bd8
    }

    tmp.release();

    //LAB_800e1be0
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Normals, colour */
  @Method(0x800e1c24L)
  public static long FUN_800e1c24(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long fp = _1f8003ee.get();

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

        final long s5 = CPU.MFC2(24);
        if((int)s5 > 0 || (fp & 0x2L) != 0 && s5 != 0) {
          //LAB_800e1ea0
          MEMORY.ref(4, packet).offset(0x0cL).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(14));
          CPU.COP2(0x158002dL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xbL) {
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
            MEMORY.ref(4, packet).offset(0x8L).setu(CPU.MFC2(22));
            MEMORY.ref(1, packet).offset(0xbL).setu(fp);

            CPU.MTC2(unpacked.offset(4, 0x4L).get(), 6);
            final long norm1 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x10L).get() * 0x4L).get();
            norm.setX((short)((int)(norm1 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm1 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm1 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(22));

            CPU.MTC2(unpacked.offset(4, 0x8L).get(), 6);
            final long norm2 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x14L).get() * 0x4L).get();
            norm.setX((short)((int)(norm2 << 20) >> 19 & 0xffff_fffcL));
            norm.setY((short)((int)(norm2 << 10) >> 19 & 0xffff_fffcL));
            norm.setZ((short)((int)norm2 >> 19 & 0xffff_fffcL));
            CPU.MTC2(norm.getXY(), 0);
            CPU.MTC2(norm.getZ(),  1);

            CPU.COP2(0x108041bL);

            MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(22));
            MEMORY.ref(4, packet).offset(0x4L).setu(0xe100_0200L | _1f8003ec.getSigned());
            final GsOT_TAG tag = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref().get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x600_0000L | tag.p.get());
            MEMORY.ref(4, packet).offset(0x0L).setu(0x700_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet = packet + 0x20L;
          }
        }
      }

      //LAB_800e2070
    }

    tmp.release();

    //LAB_800e2078
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Colours */
  @Method(0x800e20bcL)
  public static long FUN_800e20bc(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long sp58 = _1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));
    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

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

      MEMORY.ref(4, packet).offset(0x0cL).setu(unpacked.offset(4, 0x0L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(unpacked.offset(4, 0x4L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(unpacked.offset(4, 0x8L).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        long s4 = CPU.MFC2(24);
        if((int)s4 > 0 || (sp58 & 0x2L) != 0 && s4 != 0) {
          //LAB_800e2360
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x26L).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);

          CPU.COP2(0x180001L);

          MEMORY.ref(1, packet).offset(0x07L).setu(sp58);
          MEMORY.ref(4, packet).offset(0x30L).setu(unpacked.offset(4, 0xcL).get());
          s4 = CPU.CFC2(31);
          MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14));
          CPU.COP2(0x168002eL);
          MEMORY.ref(1, packet).offset(0x04L).setu(unpacked.offset(1, 0x10L).get() * r >> 12);
          MEMORY.ref(1, packet).offset(0x05L).setu(unpacked.offset(1, 0x11L).get() * g >> 12);
          MEMORY.ref(1, packet).offset(0x06L).setu(unpacked.offset(1, 0x12L).get() * b >> 12);
          MEMORY.ref(1, packet).offset(0x10L).setu(unpacked.offset(1, 0x14L).get() * r >> 12);
          MEMORY.ref(1, packet).offset(0x11L).setu(unpacked.offset(1, 0x15L).get() * g >> 12);
          MEMORY.ref(1, packet).offset(0x12L).setu(unpacked.offset(1, 0x16L).get() * b >> 12);
          MEMORY.ref(1, packet).offset(0x1cL).setu(unpacked.offset(1, 0x18L).get() * r >> 12);
          MEMORY.ref(1, packet).offset(0x1dL).setu(unpacked.offset(1, 0x19L).get() * g >> 12);
          MEMORY.ref(1, packet).offset(0x1eL).setu(unpacked.offset(1, 0x1aL).get() * b >> 12);
          MEMORY.ref(1, packet).offset(0x28L).setu(unpacked.offset(1, 0x1cL).get() * r >> 12);
          MEMORY.ref(1, packet).offset(0x29L).setu(unpacked.offset(1, 0x1dL).get() * g >> 12);
          MEMORY.ref(1, packet).offset(0x2aL).setu(unpacked.offset(1, 0x1eL).get() * b >> 12);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e25a0
            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0xc00_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x34L;
          }
        }
      }

      //LAB_800e25d4
    }

    tmp.release();

    //LAB_800e25dc
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Colours */
  @Method(0x800e2620L)
  public static long FUN_800e2620(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

    long packet = linkedListAddress_1f8003d8.get();
    final long sp50 = _1f8003ee.get();

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));

    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0x20);
    final Value sp0x10 = sp0x10tmp.get();

    final SVECTOR vert = new SVECTOR();

    for(int i = 0; i < count; i++) {
      primitives = unpackCtmdData(sp0x10.getAddress(), primitives, 0x20);

      final BVEC4 loV0 = vertices.get((int)sp0x10.offset(2, 0x18L).get());
      final BVEC4 hiV0 = vertices.get(loV0.getW());
      vert.setX((short)(loV0.getX() + ((hiV0.getX() & 0xff) << 8)));
      vert.setY((short)(loV0.getY() + ((hiV0.getY() & 0xff) << 8)));
      vert.setZ((short)(loV0.getZ() + ((hiV0.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 0);
      CPU.MTC2(vert.getZ(),  1);

      final BVEC4 loV1 = vertices.get((int)sp0x10.offset(2, 0x1aL).get());
      final BVEC4 hiV1 = vertices.get(loV1.getW());
      vert.setX((short)(loV1.getX() + ((hiV1.getX() & 0xff) << 8)));
      vert.setY((short)(loV1.getY() + ((hiV1.getY() & 0xff) << 8)));
      vert.setZ((short)(loV1.getZ() + ((hiV1.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 2);
      CPU.MTC2(vert.getZ(),  3);

      final BVEC4 loV2 = vertices.get((int)sp0x10.offset(2, 0x1cL).get());
      final BVEC4 hiV2 = vertices.get(loV2.getW());
      vert.setX((short)(loV2.getX() + ((hiV2.getX() & 0xff) << 8)));
      vert.setY((short)(loV2.getY() + ((hiV2.getY() & 0xff) << 8)));
      vert.setZ((short)(loV2.getZ() + ((hiV2.getZ() & 0xff) << 8)));
      CPU.MTC2(vert.getXY(), 4);
      CPU.MTC2(vert.getZ(),  5);

      CPU.COP2(0x280030L);

      MEMORY.ref(4, packet).offset(0x0cL).setu(sp0x10.offset(4, 0x0L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(sp0x10.offset(4, 0x4L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(sp0x10.offset(4, 0x8L).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final long s4 = CPU.MFC2(24);
        if((int)s4 > 0 || (sp50 & 0x2L) != 0 && s4 != 0) {
          //LAB_800e28c4
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));
          CPU.COP2(0x158002dL);
          MEMORY.ref(1, packet).offset(0x07L).setu(sp50);
          MEMORY.ref(1, packet).offset(0x04L).setu(sp0x10.offset(1, 0x0cL).get() * r >> 12);
          MEMORY.ref(1, packet).offset(0x05L).setu(sp0x10.offset(1, 0x0dL).get() * g >> 12);
          MEMORY.ref(1, packet).offset(0x06L).setu(sp0x10.offset(1, 0x0eL).get() * g >> 12);
          MEMORY.ref(1, packet).offset(0x10L).setu(sp0x10.offset(1, 0x10L).get() * r >> 12);
          MEMORY.ref(1, packet).offset(0x11L).setu(sp0x10.offset(1, 0x11L).get() * g >> 12);
          MEMORY.ref(1, packet).offset(0x12L).setu(sp0x10.offset(1, 0x12L).get() * b >> 12);
          MEMORY.ref(1, packet).offset(0x1cL).setu(sp0x10.offset(1, 0x14L).get() * r >> 12);
          MEMORY.ref(1, packet).offset(0x1dL).setu(sp0x10.offset(1, 0x15L).get() * g >> 12);
          MEMORY.ref(1, packet).offset(0x1eL).setu(sp0x10.offset(1, 0x16L).get() * b >> 12);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e2a18
            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x28L;
          }
        }
      }

      //LAB_800e2a4c
    }

    sp0x10tmp.release();

    //LAB_800e2a54
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Normals */
  @Method(0x800e2a98L)
  public static long FUN_800e2a98(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long sp3c = _1f8003ec.getSigned() << 16;

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));
    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x20);
    final Value unpacked = tmp.get();

    MEMORY.ref(1, packet).offset(0x3L).setu(0xcL);
    MEMORY.ref(4, packet).offset(0x4L).setu(0x3e80_8080L);

    CPU.MTC2(MEMORY.ref(4, packet).offset(0x4L).get(), 6); //TODO ???

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

      MEMORY.ref(4, packet).offset(0x0cL).setu(unpacked.offset(4, 0x0L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(unpacked.offset(4, 0x4L).get() & 0xff9f_ffffL | sp3c);
      MEMORY.ref(4, packet).offset(0x24L).setu(unpacked.offset(4, 0x8L).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          MEMORY.ref(4, packet).offset(0x8L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x1eL).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0);
          CPU.MTC2(vert.getZ(),  1);
          CPU.COP2(0x180001L);

          if((int)CPU.CFC2(31) >= 0) {
            MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14));
            MEMORY.ref(4, packet).offset(0x30L).setu(unpacked.offset(4, 0xcL).get());

            CPU.COP2(0x168002eL);

            final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
            if(z >= 0xb) {
              //LAB_800e2e3c
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

              MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20));
              MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
              MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22));

              final long norm3 = MEMORY.ref(4, normals).offset(unpacked.offset(2, 0x1cL).get() * 0x4L).get();
              norm.setX((short)((int)(norm3 << 20) >> 19 & 0xffff_fffcL));
              norm.setY((short)((int)(norm3 << 10) >> 19 & 0xffff_fffcL));
              norm.setZ((short)((int)norm3 >> 19 & 0xffff_fffcL));
              CPU.MTC2(norm.getXY(), 0);
              CPU.MTC2(norm.getZ(),  1);

              CPU.COP2(0x108041bL);

              MEMORY.ref(4, packet).offset(0x28L).setu(CPU.MFC2(22));

              final GsOT_TAG tag = tags.get(z);
              MEMORY.ref(4, packet).offset(0x0L).setu(0xc00_0000L | tag.p.get());
              tag.set(packet & 0xff_ffffL);
              packet += 0x34L;
            }
          }
        }
      }

      //LAB_800e2fc0
    }

    tmp.release();

    //LAB_800e2fc8
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Normals */
  @Method(0x800e300cL)
  public static long FUN_800e300c(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long sp34 = _1f8003ec.getSigned() << 16;

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));
    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x18);
    final Value unpacked = tmp.get();

    MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
    MEMORY.ref(4, packet).offset(0x4L).setu(0x3680_8080L);

    CPU.MTC2(MEMORY.ref(4, packet).offset(0x4L).get(), 6); //TODO ???

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

      MEMORY.ref(4, packet).offset(0x0cL).setu(unpacked.offset(4, 0x0L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(unpacked.offset(4, 0x4L).get() & 0xff9f_ffffL | sp34);
      MEMORY.ref(4, packet).offset(0x24L).setu(unpacked.offset(4, 0x8L).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));

          CPU.COP2(0x158002dL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e3304

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

            MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20));
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
            MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22));

            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x28L;
          }
        }
      }

      //LAB_800e342c
    }

    tmp.release();

    //LAB_800e3434
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Colours */
  @Method(0x800e3478L)
  public static long FUN_800e3478(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long sp54 = _1f8003ec.getSigned() << 16;

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));
    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final long r = refR.get();
    final long g = refG.get();
    final long b = refB.get();

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

      MEMORY.ref(4, packet).offset(0x0cL).setu(unpacked.offset(4, 0x0L).get()); // UV0|CLUT
      MEMORY.ref(4, packet).offset(0x18L).setu(unpacked.offset(4, 0x4L).get() & 0xff9f_ffffL | sp54); // UV1|TPAGE
      MEMORY.ref(4, packet).offset(0x24L).setu(unpacked.offset(4, 0x8L).get()); // UV2

      if((int)CPU.CFC2(31) >= 0) { // If no GTE errors
        CPU.COP2(0x140_0006L); // Normal clipping

        if(CPU.MFC2(24) != 0) { // If not looking straight on at the edge of the triangle (i.e. the triangle is visible)
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // Screen XY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // Screen XY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // Screen XY2

          final BVEC4 loV3 = vertices.get((int)unpacked.offset(2, 0x26L).get());
          final BVEC4 hiV3 = vertices.get(loV3.getW());
          vert.setX((short)(loV3.getX() + ((hiV3.getX() & 0xff) << 8)));
          vert.setY((short)(loV3.getY() + ((hiV3.getY() & 0xff) << 8)));
          vert.setZ((short)(loV3.getZ() + ((hiV3.getZ() & 0xff) << 8)));
          CPU.MTC2(vert.getXY(), 0); // Vert XY0
          CPU.MTC2(vert.getZ(),  1); // Vert Z0
          CPU.COP2(0x18_0001L); // Perspective transform single (transform last vert of quad)

          MEMORY.ref(1, packet).offset(0x3L).setu(0x0cL); // 12 elements
          MEMORY.ref(1, packet).offset(0x7L).setu(0x3eL); // Shaded textured four-point poly, translucent, blended
          MEMORY.ref(4, packet).offset(0x30L).setu(unpacked.offset(4, 0xcL).get());

          if((int)CPU.CFC2(31) >= 0) { // No GTE errors
            MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14)); // XY3

            MEMORY.ref(1, packet).offset(0x04L).setu((int)(unpacked.offset(1, 0x10L).get() * r) >> 12); // R0
            MEMORY.ref(1, packet).offset(0x05L).setu((int)(unpacked.offset(1, 0x11L).get() * g) >> 12); // G0
            MEMORY.ref(1, packet).offset(0x06L).setu((int)(unpacked.offset(1, 0x12L).get() * b) >> 12); // B0
            MEMORY.ref(1, packet).offset(0x10L).setu((int)(unpacked.offset(1, 0x14L).get() * r) >> 12); // R1
            MEMORY.ref(1, packet).offset(0x11L).setu((int)(unpacked.offset(1, 0x15L).get() * g) >> 12); // G1
            MEMORY.ref(1, packet).offset(0x12L).setu((int)(unpacked.offset(1, 0x16L).get() * b) >> 12); // B1
            MEMORY.ref(1, packet).offset(0x1cL).setu((int)(unpacked.offset(1, 0x18L).get() * r) >> 12); // R2
            MEMORY.ref(1, packet).offset(0x1dL).setu((int)(unpacked.offset(1, 0x19L).get() * g) >> 12); // G2
            MEMORY.ref(1, packet).offset(0x1eL).setu((int)(unpacked.offset(1, 0x1aL).get() * b) >> 12); // B2
            MEMORY.ref(1, packet).offset(0x28L).setu((int)(unpacked.offset(1, 0x1cL).get() * r) >> 12); // R3
            MEMORY.ref(1, packet).offset(0x29L).setu((int)(unpacked.offset(1, 0x1dL).get() * g) >> 12); // G3
            MEMORY.ref(1, packet).offset(0x2aL).setu((int)(unpacked.offset(1, 0x1eL).get() * b) >> 12); // B3

            CPU.COP2(0x168_002eL); // Average of four Z values
            // OTZ - avg Z val, capped to 0xffe
            final int avgZ = Math.min(0xffe, (int)(CPU.MFC2(7) + zOffset_1f8003e8.get()) >> 2);
            if(avgZ >= 11) { // Probably near clipping
              //LAB_800e3968
              final GsOT_TAG tag = tags.get(avgZ);
              MEMORY.ref(4, packet).offset(0x0L).setu(0xc00_0000L | tag.p.get());
              tag.set(packet & 0xff_ffffL);
              packet += 0x34L;
            }
          }
        }
      }

      //LAB_800e399c
    }

    tmp.release();

    //LAB_800e39a4
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  /** Colours */
  @Method(0x800e39e8L)
  public static long FUN_800e39e8(long primitives, final UnboundedArrayRef<SVECTOR> verts, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();
    final long sp4c = _1f8003ec.getSigned() << 16;

    primitives += 0x4L;

    final UnboundedArrayRef<BVEC4> vertices = verts.reinterpret(UnboundedArrayRef.of(4, BVEC4::new));
    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

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

      MEMORY.ref(4, packet).offset(0x0cL).setu(unpacked.offset(4, 0x0L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(unpacked.offset(4, 0x4L).get() & 0xff9f_ffffL | sp4c);
      MEMORY.ref(4, packet).offset(0x24L).setu(unpacked.offset(4, 0x8L).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
          MEMORY.ref(4, packet).offset(0x04L).setu(0x3680_8080L);

          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));

          if((int)CPU.CFC2(31) >= 0) {
            CPU.COP2(0x158002dL);

            MEMORY.ref(1, packet).offset(0x04L).setu(unpacked.offset(1, 0x0cL).get() * r >> 12);
            MEMORY.ref(1, packet).offset(0x05L).setu(unpacked.offset(1, 0x0dL).get() * g >> 12);
            MEMORY.ref(1, packet).offset(0x06L).setu(unpacked.offset(1, 0x0eL).get() * b >> 12);
            MEMORY.ref(1, packet).offset(0x10L).setu(unpacked.offset(1, 0x10L).get() * r >> 12);
            MEMORY.ref(1, packet).offset(0x11L).setu(unpacked.offset(1, 0x11L).get() * g >> 12);
            MEMORY.ref(1, packet).offset(0x12L).setu(unpacked.offset(1, 0x12L).get() * b >> 12);
            MEMORY.ref(1, packet).offset(0x1cL).setu(unpacked.offset(1, 0x14L).get() * r >> 12);
            MEMORY.ref(1, packet).offset(0x1dL).setu(unpacked.offset(1, 0x15L).get() * g >> 12);
            MEMORY.ref(1, packet).offset(0x1eL).setu(unpacked.offset(1, 0x16L).get() * b >> 12);

            final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
            if(z >= 0xb) {
              //LAB_800e3dec
              final GsOT_TAG tag = tags.get(z);
              MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
              tag.set(packet & 0xff_ffffL);
              packet += 0x28L;
            }
          }
        }
      }

      //LAB_800e3e20
    }

    tmp.release();

    //LAB_800e3e28
    linkedListAddress_1f8003d8.setu(packet);
    return primitives + 0x3L & 0xffff_fffcL;
  }

  @Method(0x800e3e6cL)
  public static void renderCtmd(final GsDOBJ2 dobj2) {
    final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0x50);
    final BttlStruct50 sp0x10 = sp0x10tmp.get().cast(BttlStruct50::new);
    _800c6920.set(sp0x10);
    sp0x10._00.set(0);

    final long s1;
    if((dobj2.attribute_00.get() & 0x4000_0000L) == 0) {
      s1 = 0;
    } else {
      s1 = 0x12L;
    }

    //LAB_800e3eb4
    final TmdObjTable objTable = dobj2.tmd_08.deref();
    final UnboundedArrayRef<SVECTOR> vertices = objTable.vert_top_00.deref();
    final long normals = objTable.normal_top_08.get();
    long primitives = objTable.primitives_10.getPointer();
    long count = objTable.n_primitive_14.get();

    //LAB_800e3ee4
    while(count != 0) {
      sp0x10._0c.set(0);
      sp0x10._08.set(0);
      sp0x10._04.set(sp0x10._00.get());

      final long length = MEMORY.ref(2, primitives).get();
      final long command = MEMORY.ref(4, primitives).get();

      _1f8003ee.setu((int)command >> 24 & 0x3eL | s1);
      final long index = command >>> 14 & 0x20L | command >>> 24 & 0xfL | command >>> 18 & 0x1L | s1;
      primitives = ctmdRenderers_800fadbc.get((int)index).deref().run(primitives, vertices, normals, length);
      count -= length;
    }

    sp0x10tmp.release();

    //LAB_800e3f64
  }

  @Method(0x800e3f88L)
  public static long FUN_800e3f88(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();

    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();
    final long t4 = _1f8003ee.get();

    //LAB_800e4008
    for(int i = 0; i < count; i++) {
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

        final long t3 = CPU.MFC2(24);
        if((int)t3 > 0 || (t4 & 0x2L) != 0 && t3 != 0) {
          //LAB_800e4088
          MEMORY.ref(4, packet).offset(0xcL).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(14));
          CPU.COP2(0x158002dL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
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
            MEMORY.ref(4, packet).offset(0x04L).setu(0xe100_0200L | _1f8003ec.get());
            MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(20));
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
            MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(22));
            MEMORY.ref(1, packet).offset(0x0bL).setu(t4);

            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x700_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x20L;
          }
        }
      }

      //LAB_800e415c
      primitives += 0x14L;
    }

    //LAB_800e4164
    linkedListAddress_1f8003d8.setu(packet);
    return primitives;
  }

  @Method(0x800e4184L)
  public static long FUN_800e4184(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();

    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();
    final long a0 = _1f8003ee.get();

    //LAB_800e41e0
    //LAB_800e41e4
    MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
    MEMORY.ref(4, packet).offset(0x4L).setu((a0 & 0x2L) != 0 ? 0x3680_8080L : 0x3480_8080L);
    CPU.MTC2(MEMORY.ref(4, packet).offset(0x4L).get(), 6);

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
      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get());
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final long t3 = CPU.MFC2(24);
        if((int)t3 > 0 || (a0 & 0x2L) != 0 && t3 != 0) {
          //LAB_800e42c0
          MEMORY.ref(4, packet).offset(0x8L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));
          CPU.COP2(0x158002dL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
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
            MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20));
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
            MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22));

            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x28L;
          }
        }
      }

      //LAB_800e4380
      primitives += 0x1cL;
    }

    //LAB_800e438c
    linkedListAddress_1f8003d8.setu(packet);
    return primitives;
  }

  @Method(0x800e43a8L)
  public static long FUN_800e43a8(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    long packet = linkedListAddress_1f8003d8.get();

    final UnboundedArrayRef<GsOT_TAG> tags = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();
    final long t8 = _1f8003ec.getSigned() << 16;

    MEMORY.ref(1, packet).offset(0x3L).setu(0x9L);
    MEMORY.ref(4, packet).offset(0x4L).setu(0x3680_8080L);
    CPU.MTC2(MEMORY.ref(4, packet).offset(0x4L).get(), 6); //TODO ???

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

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get() & 0xff9f_ffffL | t8);
      MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14));
          CPU.COP2(0x158002dL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800e451c
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
            MEMORY.ref(4, packet).offset(0x4L).setu(CPU.MFC2(20));
            MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21));
            MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22));

            final GsOT_TAG tag = tags.get(z);
            MEMORY.ref(4, packet).offset(0x0L).setu(0x900_0000L | tag.p.get());
            tag.set(packet & 0xff_ffffL);
            packet += 0x28L;
          }
        }
      }

      //LAB_800e4594
      primitives += 0x1cL;
    }

    //LAB_800e45a0
    linkedListAddress_1f8003d8.setu(packet);
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
  public static VECTOR FUN_800e4674(final VECTOR a0, final SVECTOR a1) {
    final MATRIX sp0x10 = new MATRIX();
    RotMatrix_80040010(a1, sp0x10);
    SetRotMatrix(sp0x10);
    final SVECTOR sp0x30 = new SVECTOR().set((short)0, (short)0, (short)0x1000);
    FUN_8003ef50(sp0x30, a0);
    return a0;
  }

  @Method(0x800e46c8L)
  public static void FUN_800e46c8() {
    final BattleLightStruct64 v1 = _800c6930.deref();
    v1.colour_00.set(0x800, 0x800, 0x800);

    final BttlLightStruct84 a0 = lights_800c692c.deref().get(0);
    a0.light_00.direction_00.set(0, 0x1000, 0);
    a0.light_00.r_0c.set(0x80);
    a0.light_00.g_0d.set(0x80);
    a0.light_00.b_0e.set(0x80);
    a0._10._00.set(0);
    a0._4c._00.set(0);

    //LAB_800e4720
    bzero(lights_800c692c.deref().get(1).getAddress(), 0x84);
    bzero(lights_800c692c.deref().get(2).getAddress(), 0x84);
  }

  @Method(0x800e473cL)
  public static long FUN_800e473c(final RunningScript a0) {
    FUN_800e46c8();
    return 0;
  }

  @Method(0x800e475cL)
  public static void FUN_800e475c(final int lightIndex, final int x, final int y, final int z) {
    final BttlLightStruct84 light = lights_800c692c.deref().get(lightIndex);
    light.light_00.direction_00.set(x, y, z);
    light._10._00.set(0);
  }

  @Method(0x800e4788L)
  public static long FUN_800e4788(final RunningScript a0) {
    FUN_800e475c(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800e47c8L)
  public static long FUN_800e47c8(final RunningScript a0) {
    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());
    a0.params_20.get(1).deref().set(light.light_00.direction_00.getX());
    a0.params_20.get(2).deref().set(light.light_00.direction_00.getY());
    a0.params_20.get(3).deref().set(light.light_00.direction_00.getZ());
    return 0;
  }

  @Method(0x800e4824L)
  public static void FUN_800e4824(final int lightIndex, final int x, final int y, final int z) {
    final VECTOR sp0x18 = new VECTOR();
    final SVECTOR sp0x10 = new SVECTOR().set((short)x, (short)y, (short)z);
    FUN_800e4674(sp0x18, sp0x10);
    final BttlLightStruct84 light = lights_800c692c.deref().get(lightIndex);
    light.light_00.direction_00.set(sp0x18);
    light._10._00.set(0);
  }

  @Method(0x800e48a8L)
  public static long FUN_800e48a8(final RunningScript a0) {
    FUN_800e4824(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800e48e8L)
  public static long FUN_800e48e8(final RunningScript a0) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, lights_800c692c.deref().get(a0.params_20.get(0).deref().get()).light_00.direction_00);
    a0.params_20.get(1).deref().set(sp0x10.getX());
    a0.params_20.get(2).deref().set(sp0x10.getY());
    a0.params_20.get(3).deref().set(sp0x10.getZ());
    return 0;
  }

  @Method(0x800e4964L)
  public static long FUN_800e4964(final RunningScript a0) {
    final SVECTOR sp0x10 = new SVECTOR();

    final int a2 = a0.params_20.get(1).deref().get();
    if(a2 != -1) {
      //LAB_800e49c0
      if(a2 - 1 < 3) {
        FUN_800e45c0(sp0x10, lights_800c692c.deref().get(a2 - 1).light_00.direction_00);
      } else {
        //LAB_800e49f4
        final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a2).deref().innerStruct_00.derefAs(BattleObject27c.class);
        sp0x10.setX(bobj._148.coord2Param_64.rotate.getX());
        sp0x10.setZ(bobj._148.coord2Param_64.rotate.getZ());
      }
    }

    //LAB_800e4a34
    //LAB_800e4a38
    final VECTOR sp0x18 = new VECTOR();
    sp0x10.x.add((short)a0.params_20.get(2).deref().get());
    sp0x10.y.add((short)a0.params_20.get(3).deref().get());
    sp0x10.z.add((short)a0.params_20.get(4).deref().get());
    FUN_800e4674(sp0x18, sp0x10);
    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());
    light.light_00.direction_00.set(sp0x18);
    light._10._00.set(0);
    return 0;
  }

  @Method(0x800e4abcL)
  public static long FUN_800e4abc(final RunningScript a0) {
    final int s1 = a0.params_20.get(1).deref().get();

    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, lights_800c692c.deref().get(a0.params_20.get(0).deref().get()).light_00.direction_00);

    final SVECTOR s0;
    if(s1 - 1 < 3) {
      s0 = new SVECTOR();
      FUN_800e45c0(s0, lights_800c692c.deref().get(s1 - 1).light_00.direction_00);
    } else {
      //LAB_800e4b40
      s0 = scriptStatePtrArr_800bc1c0.get(s1).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.coord2Param_64.rotate;
    }

    //LAB_800e4b64
    a0.params_20.get(1).deref().set(sp0x10.getX() - s0.getX());
    a0.params_20.get(2).deref().set(sp0x10.getY() - s0.getY());
    a0.params_20.get(3).deref().set(sp0x10.getZ() - s0.getZ());
    return 0;
  }

  @Method(0x800e4bc0L)
  public static void FUN_800e4bc0(final int lightIndex, final int r, final int g, final int b) {
    final BttlLightStruct84 light = lights_800c692c.deref().get(lightIndex);
    light.light_00.r_0c.set(r);
    light.light_00.g_0d.set(g);
    light.light_00.b_0e.set(b);
    light._4c._00.set(0);
  }

  @Method(0x800e4c10L)
  public static long FUN_800e4c10(final RunningScript a0) {
    FUN_800e4bc0(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800e4c90L)
  public static long FUN_800e4c90(final RunningScript a0) {
    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());
    a0.params_20.get(1).deref().set(light.light_00.r_0c.get());
    a0.params_20.get(2).deref().set(light.light_00.g_0d.get());
    a0.params_20.get(3).deref().set(light.light_00.b_0e.get());
    return 0;
  }

  @Method(0x800e4cf8L)
  public static void FUN_800e4cf8(final int r, final int g, final int b) {
    final BattleLightStruct64 v0 = _800c6930.deref();
    v0.colour_00.set(r, g, b);
    v0._24.set(0);
    GsSetAmbient(r, g, b);
  }

  @Method(0x800e4d2cL)
  public static long FUN_800e4d2c(final RunningScript a0) {
    FUN_800e4cf8(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get());
    _800c6930.deref()._24.set(0);
    return 0;
  }

  @Method(0x800e4d74L)
  public static void getLightColour(final IntRef r, final IntRef g, final IntRef b) {
    final BattleLightStruct64 light = _800c6930.deref();
    r.set(light.colour_00.getX());
    g.set(light.colour_00.getY());
    b.set(light.colour_00.getZ());
  }

  @Method(0x800e4db4L)
  public static long scriptGetLightColour(final RunningScript a0) {
    final BattleLightStruct64 v0 = _800c6930.deref();
    a0.params_20.get(0).deref().set(v0.colour_00.getX());
    a0.params_20.get(1).deref().set(v0.colour_00.getY());
    a0.params_20.get(2).deref().set(v0.colour_00.getZ());
    return 0;
  }

  @Method(0x800e4dfcL)
  public static long FUN_800e4dfc(final RunningScript a0) {
    lights_800c692c.deref().get(a0.params_20.get(0).deref().get())._10._00.set(0);
    return 0;
  }

  @Method(0x800e4e2cL)
  public static long FUN_800e4e2c(final RunningScript a0) {
    return lights_800c692c.deref().get(a0.params_20.get(0).deref().get())._10._00.get() > 0 ? 2 : 0;
  }

  @Method(0x800e4e64L)
  public static long FUN_800e4e64(final RunningScript a0) {
    a0.params_20.get(1).deref().set((int)lights_800c692c.deref().get(a0.params_20.get(0).deref().get())._10._00.get());
    return 0;
  }

  @Method(0x800e4ea0L)
  public static long FUN_800e4ea0(final RunningScript a0) {
    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());
    final int t1 = a0.params_20.get(4).deref().get();
    final BttlLightStruct84Sub3c t0 = light._10;

    t0._00.set(0);
    t0.vec_04.setX(light.light_00.direction_00.getX() << 12);
    t0.vec_04.setY(light.light_00.direction_00.getY() << 12);
    t0.vec_04.setZ(light.light_00.direction_00.getZ() << 12);
    t0.vec_28.setX(a0.params_20.get(1).deref().get() << 12);
    t0.vec_28.setY(a0.params_20.get(2).deref().get() << 12);
    t0.vec_28.setZ(a0.params_20.get(3).deref().get() << 12);
    t0._34.set(t1);

    if(t1 > 0) {
      t0.vec_10.setX((t0.vec_28.getX() - t0.vec_04.getX()) / t1);
      t0.vec_10.setY((t0.vec_28.getY() - t0.vec_04.getY()) / t1);
      t0.vec_10.setZ((t0.vec_28.getZ() - t0.vec_04.getZ()) / t1);
      t0.vec_1c.set(0, 0, 0);
      t0._00.set(0xa001L);
    }

    //LAB_800e4f98
    return 0;
  }

  @Method(0x800e4fa0L)
  public static long FUN_800e4fa0(final RunningScript a0) {
    final int s3 = a0.params_20.get(1).deref().get();
    final int s4 = a0.params_20.get(2).deref().get();
    final int s2 = a0.params_20.get(3).deref().get();
    final int s5 = a0.params_20.get(4).deref().get();

    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);
    light._10._00.set(0);

    final BttlLightStruct84Sub3c a3 = light._10;
    a3.vec_04.set(sp0x10);
    a3.vec_28.set(s3, s4, s2);
    a3._34.set(s5);

    if(s5 > 0) {
      a3.vec_1c.set(0, 0, 0);
      a3.vec_10.setX((s3 - a3.vec_04.getX()) / s5);
      a3.vec_10.setY((s4 - a3.vec_04.getY()) / s5);
      a3.vec_10.setZ((s2 - a3.vec_04.getZ()) / s5);
      a3._00.set(0xc001L);
    }

    //LAB_800e50c0
    //LAB_800e50c4
    return 0;
  }

  @Method(0x800e50e8L)
  public static long FUN_800e50e8(final RunningScript a0) {
    final int s3 = a0.params_20.get(0).deref().get();
    final int s2 = a0.params_20.get(1).deref().get();
    final int x = a0.params_20.get(2).deref().get();
    final int y = a0.params_20.get(3).deref().get();
    final int z = a0.params_20.get(4).deref().get();
    final int s4 = a0.params_20.get(5).deref().get();

    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, lights_800c692c.deref().get(s3).light_00.direction_00);

    final BttlLightStruct84Sub3c s0 = lights_800c692c.deref().get(s3)._10;
    s0._00.set(0);
    s0.vec_04.set(sp0x10);

    if(s2 - 1 < 2) {
      final SVECTOR sp0x18 = new SVECTOR();
      FUN_800e45c0(sp0x18, lights_800c692c.deref().get(s2 - 1).light_00.direction_00);
      s0.vec_28.set(sp0x18);
    } else {
      //LAB_800e51e8
      final SVECTOR v0 = scriptStatePtrArr_800bc1c0.get(s2).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.coord2Param_64.rotate;
      s0.vec_28.set(v0);
    }

    //LAB_800e522c
    s0._34.set(s4);
    s0.vec_28.add(x, y, z);

    if(s4 > 0) {
      s0._00.set(0xc001L);
      s0.vec_10.set(s0.vec_28).sub(s0.vec_04).div(s4);
      s0.vec_1c.set(0, 0, 0);
    }

    //LAB_800e52c8
    //LAB_800e52cc
    return 0;
  }

  @Method(0x800e52f8L)
  public static long FUN_800e52f8(final RunningScript a0) {
    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub3c v1 = light._10;
    v1._00.set(0x4001L);
    v1.vec_04.set(sp0x10.getX() << 12, sp0x10.getY() << 12, sp0x10.getZ() << 12);
    v1.vec_10.set(a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    v1.vec_1c.set(a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get());
    return 0;
  }

  @Method(0x800e540cL)
  public static long FUN_800e540c(final RunningScript a0) {
    final int bobjIndex = a0.params_20.get(1).deref().get();
    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());

    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub3c a0_0 = light._10;
    a0_0._00.set(0x4002L);
    a0_0.scriptIndex_38.set(bobjIndex);

    a0_0.vec_04.set(sp0x10).sub(scriptStatePtrArr_800bc1c0.get(bobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.coord2Param_64.rotate);
    a0_0.vec_10.set(0, 0, 0);
    a0_0.vec_1c.set(0, 0, 0);
    return 0;
  }

  @Method(0x800e54f8L)
  public static long FUN_800e54f8(final RunningScript a0) {
    lights_800c692c.deref().get(a0.params_20.get(0).deref().get())._4c._00.set(0);
    return 0;
  }

  @Method(0x800e5528L)
  public static long FUN_800e5528(final RunningScript a0) {
    return lights_800c692c.deref().get(a0.params_20.get(0).deref().get())._4c._00.get() > 0 ? 2 : 0;
  }

  @Method(0x800e5560L)
  public static long FUN_800e5560(final RunningScript a0) {
    a0.params_20.get(1).deref().set((int)lights_800c692c.deref().get(a0.params_20.get(0).deref().get())._4c._00.get());
    return 0;
  }

  @Method(0x800e559cL)
  public static long FUN_800e559c(final RunningScript a0) {
    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());
    final int t1 = a0.params_20.get(4).deref().get();
    final BttlLightStruct84Sub3c t0 = light._4c;

    t0._00.set(0);
    t0.vec_04.setX(light.light_00.r_0c.get() << 12);
    t0.vec_04.setY(light.light_00.g_0d.get() << 12);
    t0.vec_04.setZ(light.light_00.b_0e.get() << 12);
    t0.vec_28.set(a0.params_20.get(1).deref().get() << 12, a0.params_20.get(2).deref().get() << 12, a0.params_20.get(3).deref().get() << 12);
    t0._34.set(t1);

    if(t1 > 0) {
      t0.vec_1c.set(0, 0, 0);
      t0.vec_10.set(t0.vec_28).sub(t0.vec_04).div(t1);
      t0._00.set(0x8001L);
    }

    //LAB_800e5694
    return 0;
  }

  @Method(0x800e569cL)
  public static long FUN_800e569c(final RunningScript a0) {
    final BttlLightStruct84 light = lights_800c692c.deref().get(a0.params_20.get(0).deref().get());
    final BttlLightStruct84Sub3c v1 = light._4c;
    v1._00.set(0);
    v1.vec_04.set(light.light_00.r_0c.get() << 12, light.light_00.g_0d.get() << 12, light.light_00.b_0e.get() << 12);
    v1.vec_10.set(a0.params_20.get(1).deref().get() << 12, a0.params_20.get(2).deref().get() << 12, a0.params_20.get(3).deref().get() << 12);
    v1.vec_1c.set(a0.params_20.get(4).deref().get() << 12, a0.params_20.get(5).deref().get() << 12, a0.params_20.get(6).deref().get() << 12);

    if(v1._34.get() > 0) {
      v1._00.set(0x1L);
    }

    //LAB_800e5760
    return 0;
  }

  @Method(0x800e5768L)
  public static void FUN_800e5768(final BattleStruct4c struct4c) {
    FUN_800e4cf8(struct4c.ambientColour_00.getX(), struct4c.ambientColour_00.getY(), struct4c.ambientColour_00.getZ());

    final BattleLightStruct64 v1 = _800c6930.deref();
    if(struct4c._0e.get() > 0) {
      v1.colour_0c.set(struct4c.ambientColour_00);
      v1._18.set(struct4c._06.get(), struct4c._08.get(), struct4c._0a.get());
      v1._24.set(3);
      v1._2c.set(struct4c._0c.get());
      v1._2e.set(struct4c._0e.get());
    } else {
      //LAB_800e5808
      v1._24.set(0);
    }

    //LAB_800e5814
    //LAB_800e5828
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 a1 = lights_800c692c.deref().get(i);
      final BattleStruct14 a0 = struct4c._10.get(i);
      a1.light_00.direction_00.set(a0.lightDirection_00);
      a1.light_00.r_0c.set(a0.lightColour_0a.getR());
      a1.light_00.g_0d.set(a0.lightColour_0a.getG());
      a1.light_00.b_0e.set(a0.lightColour_0a.getB());

      if((a0._06.get() | a0._08.get()) != 0) {
        a1._10._00.set(0x3L);
        a1._10.vec_04.set(a1.light_00.direction_00);
        a1._10.vec_10.setX(a0._06.get());
        a1._10.vec_1c.setZ(a0._08.get());
        a1._10.vec_28.setX(0);
      } else {
        //LAB_800e58cc
        a1._10._00.set(0);
      }

      //LAB_800e58d0
      if(a0._12.get() != 0) {
        a1._4c._00.set(0x3L);
        a1._4c.vec_04.set(a1.light_00.r_0c.get(), a1.light_00.g_0d.get(), a1.light_00.b_0e.get());
        a1._4c.vec_10.set(a0._0d.getR(), a0._0d.getG(), a0._0d.getB());
        a1._4c.vec_28.setX(a0._10.get());
        a1._4c.vec_28.setY(a0._12.get());
      } else {
        //LAB_800e5944
        a1._4c._00.set(0);
      }

      //LAB_800e5948
    }
  }

  @Method(0x800e596cL)
  public static long FUN_800e596c(final RunningScript a0) {
    final int v0 = (int)currentStage_800c66a4.get() - 0x47;

    if(v0 >= 0 && v0 < 0x8) {
      FUN_800e5768(struct7cc_800c693c.deref()._98.get(v0));
    } else {
      //LAB_800e59b0
      FUN_800e5768(struct7cc_800c693c.deref()._4c);
    }

    return 0;
  }

  @Method(0x800e59d8L)
  public static long FUN_800e59d8(final RunningScript script) {
    final int a0 = script.params_20.get(0).deref().get();

    if(a0 == -1) {
      memcpy(struct7cc_800c693c.deref()._4c.getAddress(), script.params_20.get(1).getPointer(), 0x4c);
    } else if(a0 == -2) {
      //LAB_800e5a38
      //LAB_800e5a60
      FUN_800e5768(MEMORY.ref(4, script.params_20.get(1).getPointer(), BattleStruct4c::new));
      //LAB_800e5a14
    } else if(a0 == -3) {
      //LAB_800e5a40
      FUN_800e5768(struct7cc_800c693c.deref()._98.get(script.params_20.get(1).deref().get()));
    }

    //LAB_800e5a68
    return 0;
  }

  @Method(0x800e5a78L)
  public static void FUN_800e5a78(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c struct) {
    final BattleLightStruct64 light1 = _800c6930.deref();

    _800c6928.addu(0x1L);

    if(light1._24.get() == 3) {
      final int angle = rcos(((_800c6928.get() + light1._2c.get()) % light1._2e.get() << 12) / light1._2e.get());
      final int a2 = 0x1000 - angle;
      final int a3 = 0x1000 + angle;
      light1.colour_00.setX((light1.colour_0c.getX() * a3 + light1._18.getX() * a2) / 0x2000);
      light1.colour_00.setY((light1.colour_0c.getY() * a3 + light1._18.getY() * a2) / 0x2000);
      light1.colour_00.setZ((light1.colour_0c.getZ() * a3 + light1._18.getZ() * a2) / 0x2000);
    }

    //LAB_800e5b98
    //LAB_800e5ba0
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 light = lights_800c692c.deref().get(i);
      final BttlLightStruct84Sub3c a2 = light._10;

      long v1 = a2._00.get() & 0xff;
      if(v1 == 0x1L) {
        //LAB_800e5c50
        a2.vec_10.add(a2.vec_1c);
        a2.vec_04.add(a2.vec_10);

        if((a2._00.get() & 0x8000L) != 0) {
          a2._34.decr();

          if(a2._34.get() <= 0) {
            a2._00.set(0);
            a2.vec_04.set(a2.vec_28);
          }
        }

        //LAB_800e5cf4
        v1 = a2._00.get();

        if((v1 & 0x2000L) != 0) {
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
        sp0x10.set(scriptStatePtrArr_800bc1c0.get(a2.scriptIndex_38.get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.coord2Param_64.rotate).add(a2.vec_04);
        FUN_800e4674(light.light_00.direction_00, sp0x10);
      } else if(v1 == 0x3L) {
        //LAB_800e5bdc
        //LAB_800e5d6c
        final SVECTOR sp0x18 = new SVECTOR();

        v1 = _800c6928.get() & 0xfffL;
        sp0x18.setX((short)(a2.vec_04.getX() + a2.vec_10.getX() * v1));
        sp0x18.setY((short)(a2.vec_04.getY() + a2.vec_10.getY() * v1));
        sp0x18.setZ((short)(a2.vec_04.getZ() + a2.vec_10.getZ() * v1));

        //LAB_800e5dcc
        FUN_800e4674(light.light_00.direction_00, sp0x18);
      }

      //LAB_800e5dd4
      final BttlLightStruct84Sub3c s0 = light._4c;
      v1 = s0._00.get() & 0xff;
      if(v1 == 0x1L) {
        //LAB_800e5df4
        s0.vec_10.set(s0.vec_1c);
        s0.vec_04.add(s0.vec_10);

        if((s0._00.get() & 0x8000L) != 0) {
          s0._34.decr();

          if(s0._34.get() <= 0) {
            s0._00.set(0);
            s0.vec_04.set(s0.vec_28);
          }
        }

        //LAB_800e5e90
        lights_800c692c.deref().get(i).light_00.r_0c.set(s0.vec_04.getX() >> 12);
        lights_800c692c.deref().get(i).light_00.g_0d.set(s0.vec_04.getY() >> 12);
        lights_800c692c.deref().get(i).light_00.b_0e.set(s0.vec_04.getZ() >> 12);
      } else if(v1 == 0x3L) {
        //LAB_800e5ed0
        final short theta = rcos(((_800c6928.get() + s0.vec_28.getX()) % s0.vec_28.getY() << 12) / s0.vec_28.getY());
        final int a3_0 = theta + 0x1000;
        final int a2_0 = 0x1000 - theta;
        lights_800c692c.deref().get(i).light_00.r_0c.set((s0.vec_04.getX() * a3_0 + s0.vec_10.getX() * a2_0) / 0x2000);
        lights_800c692c.deref().get(i).light_00.g_0d.set((s0.vec_04.getY() * a3_0 + s0.vec_10.getY() * a2_0) / 0x2000);
        lights_800c692c.deref().get(i).light_00.b_0e.set((s0.vec_04.getZ() * a3_0 + s0.vec_10.getZ() * a2_0) / 0x2000);
      }

      //LAB_800e5fb8
      //LAB_800e5fbc
    }
  }

  @Method(0x800e5fe8L)
  public static void FUN_800e5fe8(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c struct) {
    //LAB_800e6008
    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, lights_800c692c.deref().get(i).light_00);
    }

    final BattleLightStruct64 v0 = _800c6930.deref();
    GsSetAmbient(v0.colour_00.getX(), v0.colour_00.getY(), v0.colour_00.getZ());
    _1f8003f8.setu(getProjectionPlaneDistance());
  }

  @Method(0x800e6070L)
  public static void FUN_800e6070() {
    allocateScriptState(1, 0, false, null, 0);
    loadScriptFile(1, script_800faebc, "BTTL Script 800faebc", 0); //TODO unknown size
    setCallback04(1, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e5a78", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new));
    setCallback08(1, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e5fe8", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new));
    _800c6930.deref()._60.set(0);
    FUN_800e46c8();
  }

  @Method(0x800e60e0L)
  public static void FUN_800e60e0(final int r, final int g, final int b) {
    final BattleLightStruct64 v1 = _800c6930.deref();
    final VECTOR s0 = v1._30.get(v1._60.get());

    getLightColour(s0.x, s0.y, s0.z);

    v1.colour_00.set(r, g, b);
    v1._60.incr().and(3);
  }

  @Method(0x800e6170L)
  public static void FUN_800e6170() {
    final BattleLightStruct64 a0 = _800c6930.deref();
    a0._60.decr().and(3);
    a0.colour_00.set(a0._30.get(a0._60.get()));
  }

  @Method(0x800e61e4L)
  public static void FUN_800e61e4(final int r, final int g, final int b) {
    GsSetFlatLight(0, light_800c6ddc);
    GsSetFlatLight(1, light_800c6ddc);
    GsSetFlatLight(2, light_800c6ddc);
    FUN_800e60e0(r, g, b);

    final BattleLightStruct64 v0 = _800c6930.deref();
    GsSetAmbient(v0.colour_00.getX(), v0.colour_00.getY(), v0.colour_00.getZ());
  }

  @Method(0x800e62a8L)
  public static void FUN_800e62a8() {
    FUN_800e6170();

    final BattleLightStruct64 v0 = _800c6930.deref();
    GsSetAmbient(v0.colour_00.getX(), v0.colour_00.getY(), v0.colour_00.getZ());

    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, lights_800c692c.deref().get(i).light_00);
    }
  }

  @Method(0x800e6314L)
  public static void FUN_800e6314(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();

    removeFromLinkedList(struct7cc.deffPackage_5a8.getPointer());
    struct7cc.deffPackage_5a8.clear();
    struct7cc.deff_5ac.clear();
    FUN_80012bb4();
    _800fafe8.setu(0x4L);

    if((struct7cc._20.get() & 0x4_0000L) != 0) {
      FUN_8001d068(_800c6938.deref().scriptIndex_04.get(), 0x1L);
    }

    //LAB_800e638c
    FUN_800e883c(struct7cc.scriptIndex_1c.get(), index);

    if((struct7cc._20.get() & 0x10_0000L) != 0) {
      //LAB_800e63d0
      for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
        final CombatantStruct1a8 v1 = getCombatant(i);
        if((v1.flags_19e.get() & 0x1L) != 0 && v1.charIndex_1a2.get() >= 0) {
          FUN_800c9708(i);
        }

        //LAB_800e6408
      }
    }

    //LAB_800e641c
    if((struct7cc._20.get() & 0x60_0000L) != 0) {
      FUN_80115cac(0);
    }

    //LAB_800e6444
    struct7cc._20.and(0xff80_ffffL);
  }

  @Method(0x800e6470L)
  public static long FUN_800e6470(final RunningScript a0) {
    final int t0 = a0.params_20.get(0).deref().get();
    final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();
    struct7cc._20.or(t0 & 0x1_0000L).or(t0 & 0x2_0000L).or(t0 & 0x10_0000L);

    if((struct7cc._20.get() & 0x10_0000L) != 0) {
      //LAB_800e651c
      for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
        final CombatantStruct1a8 v1 = getCombatant(i);

        if((v1.flags_19e.get() & 0x1L) != 0 && v1._04.get() != 0 && v1.charIndex_1a2.get() >= 0) {
          FUN_800ca418(i);
        }

        //LAB_800e6564
      }
    }

    //LAB_800e6578
    FUN_800e883c(struct7cc.scriptIndex_1c.get(), -1);

    final int scriptIndex = FUN_800e832c(
      a0.scriptStateIndex_00.get(),
      0,
      getMethodAddress(Bttl_800e.class, "FUN_800e70bc", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class),
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e71dc", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      getMethodAddress(Bttl_800e.class, "FUN_800e6314", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class)
    );

    scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._04.set(0x600_0400L);

    final BattleStruct24_2 v0 = _800c6938.deref();
    v0._00.set(t0 & 0xffffL);
    v0.scriptIndex_04.set(a0.params_20.get(1).deref().get());
    v0._08.set(a0.params_20.get(2).deref().get());
    v0.scriptIndex_0c.set(a0.scriptStateIndex_00.get());
    v0.scriptOffsetIndex_10.set(a0.params_20.get(3).deref().get() & 0xff);
    v0.scriptIndex_18.set(scriptIndex);
    v0._1c.set(0);
    v0.frameCount_20.set(-1);
    loadAndRunOverlay(3, getMethodAddress(Bttl_800e.class, "FUN_800e704c", long.class), v0._1c.getAddress());
    return scriptIndex;
  }

  @Method(0x800e665cL)
  public static long FUN_800e665c(final RunningScript a0) {
    final int s3 = a0.params_20.get(0).deref().get() & 0xffff;
    final int s1 = a0.params_20.get(3).deref().get() & 0xff;

    final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();
    struct7cc._20.or(_800fafec.offset(s3).get() << 16);
    FUN_800e6470(a0);

    final BattleStruct24_2 battle24 = _800c6938.deref();

    battle24._00.or(0x100_0000L);
    if((struct7cc._20.get() & 0x4_0000L) != 0) {
      //LAB_800e66fc
      //LAB_800e670c
      FUN_8001d068(battle24.scriptIndex_04.get(), s3 != 0x2e || s1 != 0 ? 0 : 0x2L);
    }

    //LAB_800e6714
    battle24.script_14.clear();

    //LAB_800e6738
    final Memory.TemporaryReservation sp0x20tmp = MEMORY.temp(0x20);
    final FileLoadingInfo file = new FileLoadingInfo(sp0x20tmp.get());
    for(int i = 0; _800fb040.offset(i).get() != 0xff; i++) {
      if(_800fb040.offset(i).get() == s3) {
        getDrgnFilePos(file, 0, 4115 + i);

        if(file.size.get() != 0) {
          loadDrgnBinFile(0, 4115 + i, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
        }
      }

      //LAB_800e679c
    }
    sp0x20tmp.release();

    //LAB_800e67b0
    loadDrgnBinFile(0, 4139 + s3 * 2, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
    loadDrgnBinFile(0, 4140 + s3 * 2, 0, getMethodAddress(Bttl_800e.class, "loadDeffPackage", long.class, long.class, long.class), battle24.scriptIndex_18.get(), 0x2L);
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e6844L)
  public static long FUN_800e6844(final RunningScript a0) {
    struct7cc_800c693c.deref()._20.or(0x40_0000L);
    FUN_800e6470(a0);
    final int s0 = ((a0.params_20.get(0).deref().get() & 0xffff) - 192) * 2;
    final BattleStruct24_2 t0 = _800c6938.deref();
    t0.script_14.clear();
    t0._00.or(0x200_0000L);
    loadDrgnBinFile(0, 4307 + s0, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
    loadDrgnBinFile(0, 4308 + s0, 0, getMethodAddress(Bttl_800e.class, "loadDeffPackage", long.class, long.class, long.class), t0.scriptIndex_18.get(), 0x2L);
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e6920L)
  public static long FUN_800e6920(final RunningScript a0) {
    final long s1 = a0.params_20.get(0).deref().get() & 0xff_0000L;
    short sp20 = (short)a0.params_20.get(0).deref().get();
    if(sp20 == -1) {
      final BattleObject27c v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
      assert false : "?"; //a0.params_20.get(0).set(sp0x20);
      sp20 = getCombatant(v0.combatantIndex_26c.get()).charIndex_1a2.get();
    }

    //LAB_800e69a8
    struct7cc_800c693c.deref()._20.or(s1 & 0x10_0000L);
    FUN_800e6470(a0);

    final BattleStruct24_2 v1 = _800c6938.deref();
    v1.script_14.clear();
    v1._00.or(0x300_0000L);

    if(sp20 < 256) {
      loadDrgnBinFile(0, 4433 + sp20 * 2, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
      loadDrgnBinFile(0, 4434 + sp20 * 2, 0, getMethodAddress(Bttl_800e.class, "loadDeffPackage", long.class, long.class, long.class), v1.scriptIndex_18.get(), 0x2L);
    } else {
      //LAB_800e6a30
      final long a0_0 = sp20 >>> 4;
      int fileIndex = (int)(_800faec4.offset(2, (a0_0 - 0x100L) * 0x2L).get() + (sp20 & 0xfL));
      if((int)a0_0 >= 0x140L) {
        fileIndex += 117;
      }

      //LAB_800e6a60
      fileIndex = (fileIndex - 1) * 2;
      loadDrgnBinFile(0, 4945 + fileIndex, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
      loadDrgnBinFile(0, 4946 + fileIndex, 0, getMethodAddress(Bttl_800e.class, "loadDeffPackage", long.class, long.class, long.class), v1.scriptIndex_18.get(), 0x2L);
    }

    //LAB_800e6a9c
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e6aecL)
  public static long FUN_800e6aec(final RunningScript a0) {
    final int v1 = a0.params_20.get(0).deref().get();
    final int s3 = v1 & 0xffff;

    FUN_800e6470(a0);

    final BattleStruct24_2 a0_0 = _800c6938.deref();
    a0_0.script_14.clear();
    a0_0._00.or(0x500_0000L);

    //LAB_800e6b5c
    final Memory.TemporaryReservation fileTmp = MEMORY.temp(0x20);
    final FileLoadingInfo file = new FileLoadingInfo(fileTmp.get());
    for(int i = 0; _800fb05c.offset(i).get() != 0xff; i++) {
      if(_800fb05c.offset(i).get() == s3) {
        getDrgnFilePos(file, 0, 5505 + i);

        if(file.size.get() != 0) {
          loadDrgnBinFile(0, 5505 + i, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
        }
      }

      //LAB_800e6bc0
    }
    fileTmp.release();

    //LAB_800e6bd4
    if((v1 & 0x1_0000L) != 0) {
      final Memory.TemporaryReservation file1tmp = MEMORY.temp(0x20);
      final Memory.TemporaryReservation file2tmp = MEMORY.temp(0x20);
      final FileLoadingInfo file1 = new FileLoadingInfo(file1tmp.get());
      final FileLoadingInfo file2 = new FileLoadingInfo(file2tmp.get());

      getDrgnFilePos(file1, 0, 5511 + s3 * 2);
      getDrgnFilePos(file2, 0, 5512 + s3 * 2);

      if(file1.size.get() < file2.size.get()) {
        //LAB_800e6ca0
        loadDrgnBinFile(0, 5511 + s3 * 2, addToLinkedListTail(file2.size.get() + 0x7ffL & 0xffff_f800L), getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0x1L, 0x4L);
      } else {
        loadDrgnBinFile(0, 5511 + s3 * 2, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
      }

      file1tmp.release();
      file2tmp.release();

      loadDrgnBinFile(0, 5512 + s3 * 2, 0, getMethodAddress(Bttl_800e.class, "loadDeffPackage", long.class, long.class, long.class), a0_0.scriptIndex_18.get(), 0x4L);
    } else {
      //LAB_800e6d1c
      loadDrgnBinFile(0, 5511 + s3 * 2, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
      loadDrgnBinFile(0, 5512 + s3 * 2, 0, getMethodAddress(Bttl_800e.class, "loadDeffPackage", long.class, long.class, long.class), a0_0.scriptIndex_18.get(), 0x2L);
    }

    //LAB_800e6d7c
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e6db4L)
  public static long FUN_800e6db4(final RunningScript a0) {
    final long v0;
    final long v1;
    switch(a0.params_20.get(0).deref().get() & 0xffff) {
      case 0, 1 -> {
        v1 = _800fafe8.get();
        if(v1 == 0x1L) {
          //LAB_800e6e20
          v0 = 0x2L;
        } else if(v1 == 0x2L) {
          //LAB_800e6e28
          v0 = 0;
        } else {
          throw new RuntimeException("undefined a2");
        }

        //LAB_800e6e2c
      }

      case 2 -> {
        v1 = _800fafe8.get();
        if(v1 == 0x1L) {
          //LAB_800e6e58
          v0 = 0x2L;
        } else if(v1 == 0x2L) {
          final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();

          //LAB_800e6e60
          if((struct7cc._20.get() & 0x20_0000L) != 0) {
            FUN_80115cac(0x1L);
          }

          //LAB_800e6e88
          if((struct7cc._20.get() & 0x40_0000L) != 0) {
            FUN_80115cac(0x3L);
          }

          //LAB_800e6eb0
          final BattleStruct24_2 struct24 = _800c6938.deref();
          loadScriptFile(struct24.scriptIndex_18.get(), struct24.script_14.deref(), struct24.scriptOffsetIndex_10.get(), "Bttl_800e Script", 0); //TODO
          struct24._1c.set(0);
          struct24.frameCount_20.set(0);
          _800fafe8.setu(0x3L);
          v0 = 0;
        } else {
          throw new RuntimeException("undefined t0");
        }

        //LAB_800e6ee4
      }

      case 3 -> {
        v1 = _800fafe8.get();
        if(v1 == 0x3L) {
          //LAB_800e6f10
          v0 = 0x2L;
        } else if(v1 == 0x4L) {
          //LAB_800e6f18
          _800fafe8.setu(0);
          v0 = 0;
        } else {
          throw new RuntimeException("undefined a3");
        }

        //LAB_800e6f20
      }

      case 4 -> {
        switch((int)_800fafe8.get()) {
          case 0:
            v0 = 0;
            break;

          case 1:
            v0 = 0x2L;
            break;

          case 2:
          case 3:
            deallocateScriptAndChildren(_800c6938.deref().scriptIndex_18.get());

          case 4:
            _800fafe8.setu(0);
            _800c6938.deref().scriptIndex_18.set(0);
            v0 = 0;
            break;

          default:
            throw new RuntimeException("Undefined a1");
        }

        //LAB_800e6f9c
      }

      default -> throw new RuntimeException("Undefined v0");
    }

    //LAB_800e6fa0
    return v0;
  }

  @Method(0x800e6fb4L)
  public static long FUN_800e6fb4(final RunningScript a0) {
    if(_800fafe8.get() != 0 && a0.scriptStateIndex_00.get() != _800c6938.deref().scriptIndex_0c.get()) {
      return 2;
    }

    //LAB_800e6fec
    //LAB_800e6ff0
    final long v1 = _800fafe8.get();

    //LAB_800e7014
    if(v1 == 0) {
      FUN_800e665c(a0);
    }

    if(v1 < 4) {
      return 2;
    }

    if(v1 == 4) {
      //LAB_800e702c
      _800fafe8.setu(0);
      _800c6938.deref().scriptIndex_18.set(0);
      return 0;
    }

    throw new IllegalStateException("Invalid v1");
  }

  @Method(0x800e704cL)
  public static void FUN_800e704c(final long param) {
    _800c6938.deref()._1c.set(1);
  }

  @Method(0x800e7060L)
  public static void loadDeffPackage(final long address, final long fileSize, final long scriptIndex) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    struct7cc_800c693c.deref().deffPackage_5a8.set(mrg);

    if(mrg.entries.get(0).size.get() != 0) {
      FUN_800ea620(mrg.getFile(0, DeffFile::new), mrg.entries.get(0).size.get(), (int)scriptIndex);
    }

    //LAB_800e7098
    _800c6938.deref().script_14.set(mrg.getFile(1, ScriptFile::new));
  }

  @Method(0x800e70bcL)
  public static void FUN_800e70bc(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c struct) {
    final BattleStruct24_2 a0 = _800c6938.deref();

    if(a0.frameCount_20.get() != -1) {
      a0.frameCount_20.add((int)vsyncMode_8007a3b8.get());
    }

    //LAB_800e70fc
    if(a0._1c.get() != 0 && !a0.script_14.isNull()) {
      final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();

      if((struct7cc._20.get() & 0x4_0000L) == 0 || (getLoadedDrgnFiles() & 0x40L) == 0) {
        //LAB_800e7154
        if((struct7cc._20.get() & 0x20_0000L) != 0) {
          FUN_80115cac(0x1L);
        }

        //LAB_800e7178
        if((struct7cc._20.get() & 0x40_0000L) != 0) {
          FUN_80115cac(0x3L);
        }

        //LAB_800e719c
        loadScriptFile(index, a0.script_14.deref(), a0.scriptOffsetIndex_10.get(), "", 0); //TODO
        a0._1c.set(0);
        a0.frameCount_20.set(0);
      }
    }

    //LAB_800e71c4
  }

  @Method(0x800e71dcL)
  public static void FUN_800e71dc(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // empty
  }

  @Method(0x800e71e4L)
  public static long FUN_800e71e4(final RunningScript a0) {
    if(_800fafe8.get() != 0 && a0.scriptStateIndex_00.get() != _800c6938.deref().scriptIndex_0c.get()) {
      return 0x2L;
    }

    //LAB_800e721c
    //LAB_800e7220
    final long v1 = _800fafe8.get();

    if(v1 < 0x4L) {
      //LAB_800e7244
      if(v1 == 0) {
        FUN_800e6844(a0);
      }

      return 0x2L;
    }

    if(v1 == 0x4L) {
      //LAB_800e725c
      _800fafe8.setu(0);
      _800c6938.deref().scriptIndex_18.set(0);
      return 0;
    }

    //LAB_800e726c
    throw new RuntimeException("Undefined v0");
  }

  @Method(0x800e727cL)
  public static long FUN_800e727c(final RunningScript a0) {
    if(_800fafe8.get() != 0 && a0.scriptStateIndex_00.get() != _800c6938.deref().scriptIndex_0c.get()) {
      return 0x2L;
    }

    //LAB_800e72b4
    //LAB_800e72b8
    final long v1 = _800fafe8.get();

    //LAB_800e72dc
    if(v1 == 0) {
      FUN_800e6920(a0);
      return 0x2L;
    }

    if(v1 < 0x4L) {
      return 0x2L;
    }

    if(v1 == 0x4L) {
      //LAB_800e72f4
      _800fafe8.setu(0);
      _800c6938.deref().scriptIndex_18.set(0);
      return 0;
    }

    //LAB_800e7304
    throw new RuntimeException("Undefined v0");
  }

  @Method(0x800e7314L)
  public static long FUN_800e7314(final RunningScript a0) {
    if(_800fafe8.get() != 0) {
      if(a0.scriptStateIndex_00.get() != _800c6938.deref().scriptIndex_0c.get()) {
        return 2;
      }
    }

    //LAB_800e734c
    //LAB_800e7350
    final long v1 = _800fafe8.get();

    if(v1 == 4) {
      //LAB_800e738c
      _800fafe8.setu(0);
      _800c6938.deref().scriptIndex_18.set(0);
      return 0;
    }

    //LAB_800e7374
    if(v1 == 0) {
      FUN_800e6aec(a0);
    }

    //LAB_800e739c
    return 2;
  }

  @Method(0x800e73acL)
  public static long FUN_800e73ac(final RunningScript a0) {
    if(_800fafe8.get() != 0) {
      return 0x2L;
    }

    final int v1 = a0.params_20.get(4).deref().get();
    if(v1 == 0x100_0000) {
      //LAB_800e7414
      FUN_800e665c(a0);
    } else if(v1 == 0x200_0000) {
      //LAB_800e7424
      FUN_800e6844(a0);
      //LAB_800e73fc
    } else if(v1 == 0x300_0000 || v1 == 0x400_0000) {
      //LAB_800e7434
      FUN_800e6920(a0);
    } else if(v1 == 0x500_0000) {
      //LAB_800e7444
      FUN_800e6aec(a0);
    }

    //LAB_800e7450
    //LAB_800e7454
    scriptStatePtrArr_800bc1c0.get(_800c6938.deref().scriptIndex_18.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._48.set(MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e74e0", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new));

    //LAB_800e7480
    return 0;
  }

  @Method(0x800e7490L)
  public static long FUN_800e7490(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)_800fafe8.get());
    return 0;
  }

  @Method(0x800e74acL)
  public static long FUN_800e74ac(final RunningScript a0) {
    final BattleStruct24_2 struct24 = _800c6938.deref();
    a0.params_20.get(0).deref().set(struct24.scriptIndex_04.get());
    a0.params_20.get(1).deref().set(struct24._08.get());
    return 0;
  }

  @Method(0x800e74e0L)
  public static void FUN_800e74e0(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final long v1 = _800fafe8.get();
    final BattleStruct24_2 struct24 = _800c6938.deref();

    if(v1 == 0x1L) {
      //LAB_800e7510
      if(struct24._1c.get() != 0 && !struct24.script_14.isNull() && ((struct7cc_800c693c.deref()._20.get() & 0x4_0000L) == 0 || (getLoadedDrgnFiles() & 0x40L) == 0)) {
        //LAB_800e756c
        _800fafe8.setu(0x2L);
      }
    } else if(v1 == 0x3L) {
      //LAB_800e7574
      if(struct24.frameCount_20.get() >= 0) {
        struct24.frameCount_20.add((int)vsyncMode_8007a3b8.get());
      }
    }

    //LAB_800e759c
  }

  @Method(0x800e75acL)
  public static void FUN_800e75ac(final BattleStruct24 a0, final MATRIX a1) {
    final MATRIX sp0x40 = new MATRIX();
    FUN_8003ec90(matrix_800c3548, a1, sp0x40);
    final long a2 = Math.min(0x3ff8, zOffset_1f8003e8.get() + sp0x40.transfer.getZ() / 4);
    if(a2 >= 0x28) {
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
      final DVECTOR sp0x30 = new DVECTOR().setXY(CPU.MFC2(12));
      final DVECTOR sp0x34 = new DVECTOR().setXY(CPU.MFC2(13));
      final DVECTOR sp0x38 = new DVECTOR().setXY(CPU.MFC2(14));
      long sp60 = CPU.MFC2(8);
      long sp64 = CPU.CFC2(31);
      long sp68 = (int)CPU.MFC2(19) >> 2;
      CPU.MTC2(sp0x28.getXY(), 0);
      CPU.MTC2(sp0x28.getZ(), 1);
      CPU.COP2(0x180001L);
      final DVECTOR sp0x3c = new DVECTOR().setXY(CPU.MFC2(14));
      sp60 = CPU.MFC2(8);
      sp64 = CPU.CFC2(31);
      sp68 = (int)CPU.MFC2(19) >> 2;
      final long s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x28L);
      MEMORY.ref(1, s0).offset(0x03L).setu(0x9L);
      MEMORY.ref(4, s0).offset(0x04L).setu(0x2c80_8080L);
      MEMORY.ref(1, s0).offset(0x04L).setu(a0.r_14.get());
      MEMORY.ref(1, s0).offset(0x05L).setu(a0.g_15.get());
      MEMORY.ref(1, s0).offset(0x06L).setu(a0.b_16.get());
      MEMORY.ref(1, s0).offset(0x07L).oru(a0._00.get() >>> 29 & 0x2L);
      MEMORY.ref(2, s0).offset(0x08L).setu(sp0x30.getX());
      MEMORY.ref(2, s0).offset(0x0aL).setu(sp0x30.getY());
      MEMORY.ref(1, s0).offset(0x0cL).setu(a0.u_0e.get());
      MEMORY.ref(1, s0).offset(0x0dL).setu(a0.v_0f.get());
      MEMORY.ref(2, s0).offset(0x0eL).setu(a0.clutY_12.get() << 6 | (a0.clutX_10.get() & 0x3f0L) >>> 4);
      MEMORY.ref(2, s0).offset(0x10L).setu(sp0x34.getX());
      MEMORY.ref(2, s0).offset(0x12L).setu(sp0x30.getY());
      MEMORY.ref(1, s0).offset(0x14L).setu(a0.u_0e.get() + a0.w_08.get());
      MEMORY.ref(1, s0).offset(0x15L).setu(a0.v_0f.get());
      MEMORY.ref(2, s0).offset(0x16L).setu(a0._0c.get() | a0._00.get() >>> 23 & 0x60L);
      MEMORY.ref(2, s0).offset(0x18L).setu(sp0x38.getX());
      MEMORY.ref(2, s0).offset(0x1aL).setu(sp0x38.getY());
      MEMORY.ref(1, s0).offset(0x1cL).setu(a0.u_0e.get());
      MEMORY.ref(1, s0).offset(0x1dL).setu(a0.v_0f.get() + a0.h_0a.get());
      MEMORY.ref(2, s0).offset(0x20L).setu(sp0x3c.getX());
      MEMORY.ref(2, s0).offset(0x22L).setu(sp0x3c.getY());
      MEMORY.ref(1, s0).offset(0x24L).setu(a0.u_0e.get() + a0.w_08.get());
      MEMORY.ref(1, s0).offset(0x25L).setu(a0.v_0f.get() + a0.h_0a.get());
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + a2 / 4 * 4, s0);
    }

    //LAB_800e7930
  }

  @Method(0x800e7944L)
  public static void FUN_800e7944(final BattleStruct24 s1, final VECTOR a1, long s2) {
    long v0;
    long v1;
    final long a1_0;
    long a2;
    final long t0;
    final long t1;
    final long t2;
    final long t3;
    final long t4;
    final long t5;
    final long t6;
    final long addr;
    long s3;
    final long s4;
    final long s5;
    long s6;
    final long s7;
    final long fp;

    if((int)s1._00.get() >= 0) {
      final VECTOR sp0x18 = ApplyMatrixLV(matrix_800c3548, a1);

      v0 = sp0x18.getZ() + matrix_800c3548.transfer.getZ();
      a2 = (sp0x18.getX() + matrix_800c3548.transfer.getX()) * _1f8003f8.getSigned() / (int)v0;
      v1 = (sp0x18.getY() + matrix_800c3548.transfer.getY()) * _1f8003f8.getSigned() / (int)v0;
      sp0x18.set(sp0x18.getX() + matrix_800c3548.transfer.getX(), (int)v0, sp0x18.getY() + matrix_800c3548.transfer.getY());

      s3 = a2 & 0xffffL;
      v1 = v1 << 16;
      s3 = s3 | v1;
      if((int)v0 < 0) {
        v0 = v0 + 0x3L;
      }

      //LAB_800e7a14
      a2 = (int)v0 >> 2;
      s6 = a2 + s2;
      if((int)s6 >= 0x28L) {
        if((int)s6 > 0x3ff8L) {
          s6 = 0x3ff8L;
        }

        //LAB_800e7a38
        addr = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);
        MEMORY.ref(1, addr).offset(0x03L).setu(0x9L);
        MEMORY.ref(4, addr).offset(0x04L).setu(0x2c80_8080L);
        MEMORY.ref(1, addr).offset(0x07L).oru(s1._00.get() >>> 29 & 0x2L);
        MEMORY.ref(1, addr).offset(0x04L).setu(s1.r_14.get()); // R
        MEMORY.ref(1, addr).offset(0x05L).setu(s1.g_15.get()); // G
        MEMORY.ref(1, addr).offset(0x06L).setu(s1.b_16.get()); // B
        a1_0 = _1f8003f8.getSigned() * 0x400 / (int)a2;
        s5 = s1.x_04.get() * s1._1c.get() / 0x8 * a1_0 / 0x8000;
        s7 = s1.w_08.get() * s1._1c.get() / 0x8 * a1_0 / 0x8000 + s5;
        s2 = s1.y_06.get() * s1._1e.get() / 0x8 * a1_0 / 0x8000;
        fp = s1.h_0a.get() * s1._1e.get() / 0x8 * a1_0 / 0x8000 + s2;
        s4 = rsin(s1._20.get());
        t2 = rcos(s1._20.get());
        t6 = s3 + s5 * t2 / 0x1000;
        a2 = s2 * s4 / 0x1000;
        MEMORY.ref(2, addr).offset(0x08L).setu(t6 - a2); // V1 x
        t3 = s3 / 0x10000;
        t5 = t3 + s5 * s4 / 0x1000;
        v1 = s2 * t2 / 0x1000;
        MEMORY.ref(2, addr).offset(0x0aL).setu(t5 + v1); // V1 y
        t4 = s3 + s7 * t2 / 0x1000;
        MEMORY.ref(1, addr).offset(0x0cL).setu(s1.u_0e.get()); // V1 u
        MEMORY.ref(1, addr).offset(0x0dL).setu(s1.v_0f.get()); // V1 v
        MEMORY.ref(2, addr).offset(0x0eL).setu(s1.clutY_12.get() << 6 | (s1.clutX_10.get() & 0x3f0L) >>> 4); // CLUT
        MEMORY.ref(2, addr).offset(0x10L).setu(t4 - a2); // V2 x
        a2 = s7 * s4 / 0x1000;
        MEMORY.ref(2, addr).offset(0x12L).setu(t3 + a2 + v1); // V2 y
        t1 = fp * s4 / 0x1000;
        t0 = fp * t2 / 0x1000;
        MEMORY.ref(1, addr).offset(0x14L).setu(s1.w_08.get() + s1.u_0e.get() + 0xffL); // V2 u
        MEMORY.ref(1, addr).offset(0x15L).setu(s1.v_0f.get()); // V2 v
        MEMORY.ref(2, addr).offset(0x16L).setu(s1._0c.get() | s1._00.get() >>> 23 & 0x60L); // TPage
        MEMORY.ref(2, addr).offset(0x18L).setu(t6 - t1); // V3 x
        MEMORY.ref(2, addr).offset(0x1aL).setu(t5 + t0); // V3 Y
        MEMORY.ref(1, addr).offset(0x1cL).setu(s1.u_0e.get()); // V3 u
        MEMORY.ref(1, addr).offset(0x1dL).setu(s1.h_0a.get() + s1.v_0f.get() + 0xffL); // V3 v
        MEMORY.ref(2, addr).offset(0x20L).setu(t4 - t1); // V4 x
        MEMORY.ref(2, addr).offset(0x22L).setu(s3 / 0x10000 + a2 + t0); // V4 y
        MEMORY.ref(1, addr).offset(0x24L).setu(s1.w_08.get() + s1.u_0e.get() + 0xffL); // V4 u
        MEMORY.ref(1, addr).offset(0x25L).setu(s1.h_0a.get() + s1.v_0f.get() + 0xffL); // V4 v
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + s6 / 4 * 4, addr);
      }
    }

    //LAB_800e7d8c
  }

  @Method(0x800e7dbcL)
  public static int FUN_800e7dbc(final DVECTOR a0, final VECTOR a1) {
    final VECTOR sp0x10 = ApplyMatrixLV(matrix_800c3548, a1);
    sp0x10.add(matrix_800c3548.transfer);

    if(sp0x10.getZ() >= 160) {
      a0.setX((short)(sp0x10.getX() * _1f8003f8.getSigned() / sp0x10.getZ()));
      a0.setY((short)(sp0x10.getY() * _1f8003f8.getSigned() / sp0x10.getZ()));
      return sp0x10.getZ() >> 2;
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
  public static void scriptManagerDestructor(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c struct) {
    EffectManagerData6c a0 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    if(a0.parentScriptIndex_50.get() != -1) {
      if(a0.newChildScriptIndex_56.get() != -1) {
        scriptStatePtrArr_800bc1c0.get(a0.newChildScriptIndex_56.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class).oldChildScriptIndex_54.set(a0.oldChildScriptIndex_54.get());
      } else {
        //LAB_800e7f4c
        scriptStatePtrArr_800bc1c0.get(a0.parentScriptIndex_50.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class).childScriptIndex_52.set(a0.oldChildScriptIndex_54.get());
      }

      //LAB_800e7f6c
      if(a0.oldChildScriptIndex_54.get() != -1) {
        scriptStatePtrArr_800bc1c0.get(a0.oldChildScriptIndex_54.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class).newChildScriptIndex_56.set(a0.newChildScriptIndex_56.get());
      }

      //LAB_800e7fa0
      a0.parentScriptIndex_50.set((short)-1);
      a0.oldChildScriptIndex_54.set((short)-1);
      a0.newChildScriptIndex_56.set((short)-1);
    }

    //LAB_800e7fac
    //LAB_800e7fcc
    while(struct.childScriptIndex_52.get() != -1) {
      a0 = scriptStatePtrArr_800bc1c0.get(struct.childScriptIndex_52.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

      //LAB_800e7ff8
      while(a0.childScriptIndex_52.get() != -1) {
        a0 = scriptStatePtrArr_800bc1c0.get(a0.childScriptIndex_52.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
      }

      //LAB_800e8020
      deallocateScriptAndChildren(a0.scriptIndex_0e.get());
    }

    //LAB_800e8040
    if(!struct.destructor_4c.isNull()) {
      struct.destructor_4c.deref().run(index, state, struct);
    }

    //LAB_800e805c
    if(!struct._44.isNull()) {
      removeFromLinkedList(struct._44.getPointer());
    }

    //LAB_800e8074
    while(!struct._58.isNull()) {
      final long ptr = struct._58.getPointer();

      struct._58.setNullable(struct._58.deref()._00.derefNullable());

      //LAB_800e8088
      removeFromLinkedList(ptr);

      //LAB_800e8090
    }
  }

  @Method(0x800e80c4L)
  public static int allocateEffectManager(int parentIndex, final long subStructSize, @Nullable final TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c> a2, @Nullable final TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c> callback08, @Nullable final TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c> destructor, @Nullable final Function<Value, BttlScriptData6cSubBase1> subStructConstructor) {
    final int index = allocateScriptState(0x6cL, EffectManagerData6c::new);

    loadScriptFile(index, script_800faebc, "BTTL Script 800faebc", 0); //TODO unknown size
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e8e9c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new));

    if(callback08 != null) {
      setCallback08(index, callback08);
    }

    //LAB_800e8150
    setScriptDestructor(index, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "scriptManagerDestructor", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new));

    final EffectManagerData6c s0 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    s0.size_08.set(subStructSize);
    if(subStructSize != 0) {
      s0._44.set(MEMORY.ref(4, addToLinkedListTail(subStructSize), subStructConstructor));
    } else {
      //LAB_800e8184
      s0._44.clear();
    }

    //LAB_800e8188
    s0.magic_00.set(BattleScriptDataBase.EM__);
    s0._04.set(0xff00_0000L);
    s0.scriptIndex_0c.set(-1);
    s0.coord2Index_0d.set(-1);
    s0.scriptIndex_0e.set(index);
    s0._10._00.set(0x5400_0000L);
    s0._10.vec_04.set(0, 0, 0);
    s0._10.svec_10.set((short)0, (short)0, (short)0);
    s0._10.svec_16.set((short)0x1000, (short)0x1000, (short)0x1000);
    s0._10.svec_1c.set((short)0x80, (short)0x80, (short)0x80);
    s0._10._22.set((short)0);
    s0._10._24.set(0);
    s0._10.vec_28.set(0, 0, 0);
    s0._48.setNullable(a2);
    s0.destructor_4c.setNullable(destructor);
    s0.parentScriptIndex_50.set((short)-1);
    s0.childScriptIndex_52.set((short)-1);
    s0.oldChildScriptIndex_54.set((short)-1);
    s0.newChildScriptIndex_56.set((short)-1);
    s0._58.clear();
    scriptStatePtrArr_800bc1c0.get(index).deref().typePtr_f8.set(s0.type_5c);
    strcpy(s0.type_5c, _800c6e18.get());

    if(parentIndex != -1) {
      if(scriptStatePtrArr_800bc1c0.get(parentIndex).deref().innerStruct_00.derefAs(BattleScriptDataBase.class).magic_00.get() != BattleScriptDataBase.EM__) {
        parentIndex = struct7cc_800c693c.deref().scriptIndex_1c.get();
      }

      //LAB_800e8294
      final EffectManagerData6c parent = scriptStatePtrArr_800bc1c0.get(parentIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
      final EffectManagerData6c child = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

      child.parentScriptIndex_50.set((short)parentIndex);
      if(parent.childScriptIndex_52.get() != -1) {
        child.oldChildScriptIndex_54.set(parent.childScriptIndex_52.get());
        scriptStatePtrArr_800bc1c0.get(parent.childScriptIndex_52.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class).newChildScriptIndex_56.set((short)index);
      }

      //LAB_800e8300
      parent.childScriptIndex_52.set((short)index);
    }

    //LAB_800e8304
    return index;
  }

  @Method(0x800e832cL)
  public static <T extends MemoryRef> int FUN_800e832c(final int a0, final long subStructSize, final long a2, @Nullable final TriConsumerRef<Integer, ScriptState<T>, T> callback08, final long a4) {
    final int index = allocateScriptState(0x6cL, EffectManagerData6c::new);
    loadScriptFile(index, script_800faebc, "BTTL Script FUN_800e832c", 0); //TODO
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e8e9c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new));

    if(callback08 != null) {
      setCallback08(index, callback08);
    }

    //LAB_800e83b8
    setScriptDestructor(index, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "scriptManagerDestructor", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new));

    final ScriptState<EffectManagerData6c> s3 = scriptStatePtrArr_800bc1c0.get(index).derefAs(ScriptState.classFor(EffectManagerData6c.class));
    final EffectManagerData6c s0 = s3.innerStruct_00.derefAs(EffectManagerData6c.class);

    s0.size_08.set(subStructSize);

    if(subStructSize != 0) {
      s0._44.setPointer(addToLinkedListTail(subStructSize));
    } else {
      //LAB_800e83ec
      s0._44.clear();
    }

    //LAB_800e83f0
    s0._48.set(MEMORY.ref(4, a2, TriConsumerRef::new));
    s0.scriptIndex_0e.set(index);
    s0._10.vec_04.set(0, 0, 0);
    s0._10.svec_10.set((short)0, (short)0, (short)0);
    s0._10._22.set((short)0);
    s0._10._24.set(0);
    s0._10.vec_28.set(0, 0, 0);
    s0.destructor_4c.set(MEMORY.ref(4, a4, TriConsumerRef::new));
    s0.magic_00.set(BattleScriptDataBase.EM__);
    s0._04.set(0xff00_0000L);
    s0.scriptIndex_0c.set(-1);
    s0.coord2Index_0d.set(-1);
    s0._10._00.set(0x5400_0000L);
    s0._10.svec_16.set((short)0x1000, (short)0x1000, (short)0x1000);
    s0._10.svec_1c.set((short)0x80, (short)0x80, (short)0x80);
    s0.parentScriptIndex_50.set((short)-1);
    s0.childScriptIndex_52.set((short)-1);
    s0.oldChildScriptIndex_54.set((short)-1);
    s0.newChildScriptIndex_56.set((short)-1);
    s0._58.clear();
    s3.typePtr_f8.set(s0.type_5c);
    strcpy(s0.type_5c, _800c6e18.get());

    if(a0 != -1) {
      final int a0_0;
      if(scriptStatePtrArr_800bc1c0.get(a0).deref().innerStruct_00.derefAs(BattleScriptDataBase.class).magic_00.get() == BattleScriptDataBase.EM__) {
        a0_0 = a0;
      } else {
        a0_0 = struct7cc_800c693c.deref().scriptIndex_1c.get();
      }

      //LAB_800e84fc
      final EffectManagerData6c data1 = scriptStatePtrArr_800bc1c0.get(a0_0).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
      final EffectManagerData6c data2 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

      data2.parentScriptIndex_50.set((short)a0_0);
      if(data1.childScriptIndex_52.get() != -0x1L) {
        data2.oldChildScriptIndex_54.set(data1.childScriptIndex_52.get());
        scriptStatePtrArr_800bc1c0.get(data1.childScriptIndex_52.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class).newChildScriptIndex_56.set((short)index);
      }

      //LAB_800e8568
      data1.childScriptIndex_52.set((short)index);
    }

    //LAB_800e856c
    return index;
  }

  @Method(0x800e8594L)
  public static void FUN_800e8594(final MATRIX s4, final EffectManagerData6c a1) {
    EffectManagerData6c s3 = a1;
    RotMatrix_8003faf0(s3._10.svec_10, s4);
    TransMatrix(s4, s3._10.vec_04);
    ScaleVectorL_SVEC(s4, s3._10.svec_16);

    int a0 = s3.scriptIndex_0c.get();

    //LAB_800e8604
    while(a0 >= 0) {
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(a0).deref();
      if(state.getAddress() == scriptState_800bc0c0.getAddress()) {
        a0 = -2;
        a1._10._00.or(0x8000_0000L);
        s4.transfer.setZ(-0x7fff);
        break;
      }

      final MATRIX sp0x10 = new MATRIX();

      final BattleScriptDataBase base = state.innerStruct_00.derefAs(BattleScriptDataBase.class);
      if(base.magic_00.get() == BattleScriptDataBase.BOBJ) {
        final BattleObject27c s0 = state.innerStruct_00.derefAs(BattleObject27c.class);
        final BigStruct s1 = s0._148;
        FUN_800214bc(s1);

        if(s3.coord2Index_0d.get() == -1) {
          sp0x10.set(s0._148.coord2_14.coord);
        } else {
          //LAB_800e8738
          final GsCOORDINATE2 coord2 = s1.coord2ArrPtr_04.deref().get(s3.coord2Index_0d.get());
          GsGetLw(coord2, sp0x10);
          coord2.flg.set(0);
        }

        //LAB_800e8774
        FUN_8003f210(sp0x10, s4, s4);
        a0 = -1;
        break;
      }

      if(base.magic_00.get() == BattleScriptDataBase.EM__) {
        final EffectManagerData6c s0 = state.innerStruct_00.derefAs(EffectManagerData6c.class);
        RotMatrix_8003faf0(s0._10.svec_10, sp0x10);
        TransMatrix(sp0x10, s0._10.vec_04);
        //LAB_800e866c
        ScaleVectorL_SVEC(sp0x10, s0._10.svec_16);

        if(s3.coord2Index_0d.get() != -1) {
          FUN_8003f210(sp0x10, FUN_800ea0f4(s0, s3.coord2Index_0d.get()).coord, sp0x10);
        }

        //LAB_800e86ac
        FUN_8003f210(sp0x10, s4, s4);
        s3 = s0;
        a0 = s3.scriptIndex_0c.get();
        //LAB_800e86c8
      } else {
        //LAB_800e878c
        a0 = -2;

        //LAB_800e8790
        a1._10._00.or(0x8000_0000L);
        s4.transfer.setZ(-0x7fff);
        break;
      }

      //LAB_800e87ac
    }

    //LAB_800e87b4
    if(a0 == -2) {
      final MATRIX m0 = matrix_800c3548;
      final MATRIX sp0x10 = new MATRIX();
      TransposeMatrix(m0, sp0x10);
      final VECTOR sp0x30 = new VECTOR().set(m0.transfer);
      sp0x10.transfer.set(ApplyMatrixLV(sp0x10, sp0x30));
      FUN_8003f210(sp0x10, s4, s4);
    }

    //LAB_800e8814
  }

  @Method(0x800e883cL)
  public static void FUN_800e883c(final int scriptIndex, final int a1) {
    final EffectManagerData6c s3 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    //LAB_800e889c
    int s0 = s3.childScriptIndex_52.get();
    while(s0 != -1) {
      FUN_800e883c(s0, a1);
      s0 = scriptStatePtrArr_800bc1c0.get(s0).deref().innerStruct_00.derefAs(EffectManagerData6c.class).oldChildScriptIndex_54.get();
    }

    //LAB_800e88cc
    if(scriptIndex != struct7cc_800c693c.deref().scriptIndex_1c.get() && scriptIndex != a1) {
      if((s3._04.get() & 0x4_0000L) == 0 && (s3._04.get() & 0xff00_0000L) != 0x200_0000L) {
        Pointer<BttlScriptData6cSubBase2> s2 = s3._58;

        //LAB_800e892c
        while(!s2.isNull()) {
          final int size = s2.deref().size_04.get();
          final long addr1 = addToLinkedListTail(size);
          final BttlScriptData6cSubBase2 addr2 = s2.deref();

          if(addr2.getAddress() < addr1) {
            //LAB_800e8968
            memcpy(addr1, addr2.getAddress(), size);

            //LAB_800e8984
            removeFromLinkedList(s2.getPointer());
            s2.set(addr1, addr2.getClass());
            MemoryHelper.copyPointerTypes(s2.deref(), addr2);
          } else {
            //LAB_800e899c
            removeFromLinkedList(addr1);
          }

          //LAB_800e89ac
          s2 = s2.deref()._00;
        }

        //LAB_800e89c4
        if(!s3._44.isNull()) {
          final int size = (int)s3.size_08.get();
          final long addr1 = addToLinkedListTail(size);
          final BttlScriptData6cSubBase1 addr2 = s3._44.deref();

          if(addr2.getAddress() < addr1) {
            //LAB_800e8a0c
            memcpy(addr1, addr2.getAddress(), size);

            //LAB_800e8a28
            removeFromLinkedList(addr2.getAddress());
            s3._44.set(addr1, addr2.getClass());
            MemoryHelper.copyPointerTypes(s3._44.deref(), addr2);
          } else {
            //LAB_800e8a40
            removeFromLinkedList(addr1);
          }
        }

        //LAB_800e8a50
        final long addr1 = addToLinkedListTail(0x16cL);
        final long addr2 = scriptStatePtrArr_800bc1c0.get(scriptIndex).getPointer();

        if(addr2 < addr1) {
          final ScriptState<?> oldState = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();

          //LAB_800e8a88
          memcpy(addr1, addr2, 0x16c);
          removeFromLinkedList(scriptStatePtrArr_800bc1c0.get(scriptIndex).getPointer());
          MEMORY.ref(4, addr1).setu(addr1 + 0x100L);
          scriptStatePtrArr_800bc1c0.get(scriptIndex).set(MEMORY.ref(4, addr1, ScriptState.of(EffectManagerData6c::new)));
          MemoryHelper.copyPointerTypes(scriptStatePtrArr_800bc1c0.get(scriptIndex).deref(), oldState);
        } else {
          //LAB_800e8ad4
          removeFromLinkedList(addr1);
        }
      }
    }

    //LAB_800e8ae4
  }

  @Method(0x800e8c84L)
  public static BttlScriptData6cSubBase2 FUN_800e8c84(final EffectManagerData6c a0, final long a1) {
    BttlScriptData6cSubBase2 v1 = a0._58.derefNullable();

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
    Pointer<BttlScriptData6cSubBase2> s0 = a0._58;

    //LAB_800e8d3c
    while(!s0.isNull()) {
      final BttlScriptData6cSubBase2 v1 = s0.deref();

      if(v1._05.get() == (byte)a1) {
        a0._04.and(~(0x1L << v1._05.get()));

        final BttlScriptData6cSubBase2 a0_0 = s0.deref();
        s0.setNullable(a0_0._00.derefNullable());
        removeFromLinkedList(a0_0.getAddress());
      } else {
        //LAB_800e8d84
        s0 = v1._00;
      }

      //LAB_800e8d88
    }

    //LAB_800e8d98
  }

  @Method(0x800e8dd4L)
  public static <T extends BttlScriptData6cSubBase2> T FUN_800e8dd4(final EffectManagerData6c a0, final long a1, final long a2, final BiFunctionRef<EffectManagerData6c, T, Long> callback, final long size, final Function<Value, T> constructor) {
    final T struct = MEMORY.ref(4, addToLinkedListTail(size), constructor);
    struct.size_04.set((int)size);
    struct._05.set((int)a1);
    struct._06.set((short)a2);
    struct._08.set(callback);
    struct._00.setNullable(a0._58.derefNullable());
    a0._58.set(struct);
    a0._04.or(1L << a1);
    return struct;
  }

  @Method(0x800e8e68L)
  public static void FUN_800e8e68(final Pointer<BttlScriptData6cSubBase2> a0) {
    final BttlScriptData6cSubBase2 v1 = a0.deref();
    a0.setNullable(v1._00.derefNullable());
    removeFromLinkedList(v1.getAddress());
  }

  @Method(0x800e8e9cL)
  public static void FUN_800e8e9c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    Pointer<BttlScriptData6cSubBase2> subPtr = data._58;

    if(!data._58.isNull()) {
      //LAB_800e8ee0
      do {
        final BttlScriptData6cSubBase2 sub = subPtr.deref();

        final long v1 = sub._08.derefAs(BiFunctionRef.classFor(EffectManagerData6c.class, BttlScriptData6cSubBase2.class, long.class)).run(data, subPtr.deref());
        if(v1 == 0) {
          //LAB_800e8f2c
          data._04.and(~(1 << sub._05.get()));
          subPtr.setNullable(sub._00.derefNullable());
          removeFromLinkedList(sub.getAddress());
        } else if(v1 == 1) {
          //LAB_800e8f6c
          subPtr = sub._00;
          //LAB_800e8f1c
        } else if(v1 == 2) {
          //LAB_800e8f78
          deallocateScriptAndChildren(index);
          return;
        }

        //LAB_800e8f8c
      } while(!subPtr.isNull());
    }

    //LAB_800e8f9c
    if(!data._48.isNull()) {
      data._48.deref().run(index, state, data);
    }

    //LAB_800e8fb8
  }

  @Method(0x800e8ffcL)
  public static void FUN_800e8ffc() {
    final BattleStruct7cc v0 = MEMORY.ref(4, addToLinkedListTail(0x7ccL), BattleStruct7cc::new);
    _800c6938.set(v0._5b8);
    _800c6930.set(v0._5dc);
    lights_800c692c.setPointer(v0._640.getAddress());
    v0._20.set(0x4L);
    v0.ptr_24.set(v0._28.getAddress());
    _800c6944.setu(v0._2f8.getAddress());
    _800c6940.setu(v0._390.getAddress());
    struct7cc_800c693c.set(v0);
    _800c6948.setu(v0._39c.getAddress());
    final int scriptIndex = allocateEffectManager(-1, 0, null, null, null, null);
    scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._04.set(0x600_0400L);
    v0.scriptIndex_1c.set(scriptIndex);
    v0.mrg_2c.clear();
    v0._30.set(0);
    v0._34.set(0);
    v0.deff_38.clear();
    FUN_800e6070();
    loadAndRunOverlay(1, getMethodAddress(SBtld.class, "FUN_801098f4", long.class), 0);
  }

  @Method(0x800e9100L)
  public static void loadBattleHudDeff_() {
    loadBattleHudDeff();
  }

  @Method(0x800e9120L)
  public static void FUN_800e9120() {
    deallocateScriptAndChildren(1);
    FUN_800eab8c();
    deallocateScriptAndChildren(struct7cc_800c693c.deref().scriptIndex_1c.get());
    removeFromLinkedList(struct7cc_800c693c.getPointer());
  }

  @Method(0x800e9178L)
  public static void FUN_800e9178(final int a0) {
    if(a0 == 1) {
      //LAB_800e91a0
      FUN_800e8d04(scriptStatePtrArr_800bc1c0.get(struct7cc_800c693c.deref().scriptIndex_1c.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class), 10);
    } else if(a0 == 2) {
      //LAB_800e91d8
      FUN_800e8d04(scriptStatePtrArr_800bc1c0.get(struct7cc_800c693c.deref().scriptIndex_1c.get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class), 10);
      FUN_800eab8c();
    } else {
      //LAB_800e9214
      FUN_800eab8c();
      deallocateScriptAndChildren(struct7cc_800c693c.deref().scriptIndex_1c.get());
      final int scriptIndex = allocateEffectManager(-1, 0, null, null, null, null);
      struct7cc_800c693c.deref().scriptIndex_1c.set(scriptIndex);
      scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._04.set(0x600_0400L);
    }

    //LAB_800e9278
  }

  @Method(0x800e9288L)
  public static <T extends MemoryRef> void FUN_800e9288(final T obj, final long a1, @Nullable final Pointer<T> ptr) {
    if(ptr != null) {
      ptr.set(obj);
    }
  }

  @Method(0x800e929cL)
  public static void FUN_800e929c(final long address, final long fileSize, final long param) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    //LAB_800e92d4
    for(int i = 0; i < mrg.count.get(); i++) {
      if(mrg.entries.get(i).size.get() != 0) {
        final TimHeader tim = parseTimHeader(MEMORY.ref(4, mrg.getFile(i) + 0x4L)); //TODO
        LoadImage(tim.getImageRect(), tim.getImageAddress());

        if((tim.flags.get() & 0x8L) != 0) {
          LoadImage(tim.getClutRect(), tim.getClutAddress());
        }

        //LAB_800e9324
        DrawSync(0);
      }

      //LAB_800e932c
    }

    //LAB_800e933c
    if((param & 0x1L) == 0) {
      removeFromLinkedList(address);
    }

    //LAB_800e9354
  }

  @Method(0x800e93e0L)
  public static long FUN_800e93e0(final RunningScript a0) {
    a0.params_20.get(0).deref().set(allocateEffectManager(a0.scriptStateIndex_00.get(), 0, null, null, null, null));
    return 0;
  }

  @Method(0x800e9428L)
  public static void FUN_800e9428(final long a0, final BttlScriptData6cInner a1, final MATRIX a2) {
    if((int)a1._00.get() >= 0) {
      final BattleStruct24 sp0x10 = new BattleStruct24();
      sp0x10._00.set(a1._00.get());
      sp0x10.x_04.set((short)(-MEMORY.ref(1, a0).offset(0x4L).get() / 2));
      sp0x10.y_06.set((short)(-MEMORY.ref(1, a0).offset(0x5L).get() / 2));
      sp0x10.w_08.set((int)MEMORY.ref(1, a0).offset(0x4L).get());
      sp0x10.h_0a.set((int)MEMORY.ref(1, a0).offset(0x5L).get());
      sp0x10._0c.set((int)((MEMORY.ref(2, a0).offset(0x2L).get() & 0x100L) >>> 4 | (MEMORY.ref(2, a0).offset(0x0L).get() & 0x3ffL) >>> 6));
      sp0x10.u_0e.set((int)((MEMORY.ref(2, a0).offset(0x0L).get() & 0x3fL) << 2));
      sp0x10.v_0f.set((int)MEMORY.ref(1, a0).offset(0x2L).get());
      sp0x10.clutX_10.set((int)(MEMORY.ref(2, a0).offset(0x6L).get() << 4 & 0x3ffL));
      sp0x10.clutY_12.set((int)(MEMORY.ref(2, a0).offset(0x6L).get() >>> 6 & 0x1ffL));
      sp0x10.r_14.set(a1.svec_1c.getX());
      sp0x10.g_15.set(a1.svec_1c.getY());
      sp0x10.b_16.set(a1.svec_1c.getZ());
      sp0x10._1c.set(a1.svec_16.getX());
      sp0x10._1e.set(a1.svec_16.getY());
      sp0x10._20.set(a1.svec_10.getZ()); // This is correct, different svec for Z
      if((a1._00.get() & 0x400_0000L) != 0) {
        zOffset_1f8003e8.setu(a1._22.get());
        FUN_800e75ac(sp0x10, a2);
      } else {
        //LAB_800e9574
        FUN_800e7944(sp0x10, a2.transfer, a1._22.get());
      }
    }

    //LAB_800e9580
  }

  @Method(0x800e9590L)
  public static void FUN_800e9590(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final EffectManagerData6c s0 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final MATRIX sp0x10 = new MATRIX();
    FUN_800e8594(sp0x10, s0);
    FUN_800e9428(s0._44.getPointer() + 0x4L, s0._10, sp0x10); //TODO 6c 0c
  }

  @Method(0x800e95f0L)
  public static void FUN_800e95f0(final long a0, final long a1) {
    MEMORY.ref(4, a0).offset(0x0L).setu(a1 | 0x400_0000L);
    if((a1 & 0xf_ff00L) == 0xf_ff00L) {
      final long v1 = (a1 & 0xffL) * 0x8L;
      MEMORY.ref(4, a0).offset(0x4L).set(struct7cc_800c693c.deref()._39c.offset(v1).offset(0x0L).get());
      MEMORY.ref(4, a0).offset(0x8L).set(struct7cc_800c693c.deref()._39c.offset(v1).offset(0x4L).get());
    } else {
      //LAB_800e9658
      long v0 = FUN_800eac58(a1 | 0x400_0000L).getAddress(); //TODO
      v0 = v0 + MEMORY.ref(4, v0).offset(0x8L).get();
      MEMORY.ref(2, a0).offset(0x4L).setu(MEMORY.ref(2, v0).offset(0x0L).get());
      MEMORY.ref(2, a0).offset(0x6L).setu(MEMORY.ref(2, v0).offset(0x2L).get());
      MEMORY.ref(1, a0).offset(0x8L).setu(MEMORY.ref(2, v0).offset(0x4L).getSigned() * 0x4L);
      MEMORY.ref(1, a0).offset(0x9L).setu(MEMORY.ref(1, v0).offset(0x6L).get());
      MEMORY.ref(2, a0).offset(0xaL).setu(MEMORY.ref(2, v0).offset(0xaL).get() << 6 | (MEMORY.ref(2, v0).offset(0x8L).get() & 0x3f0L) >>> 4);
    }

    //LAB_800e96bc
  }

  @Method(0x800e96ccL)
  public static long FUN_800e96cc(final RunningScript s1) {
    final int s2 = allocateEffectManager(s1.scriptStateIndex_00.get(), 0xcL, null, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e9590", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), null, BttlScriptData6cSub0c::new);
    final ScriptState<EffectManagerData6c> v0 = scriptStatePtrArr_800bc1c0.get(s2).derefAs(ScriptState.classFor(EffectManagerData6c.class));
    final EffectManagerData6c s0 = v0.innerStruct_00.deref();
    s0._04.set(0x400_0000L);
    FUN_800e95f0(s0._44.getPointer(), s1.params_20.get(1).deref().get()); //TODO 6c 0c
    s0._10._00.and(0xfbff_ffffL).or(0x5000_0000L);
    s1.params_20.get(0).deref().set(s2);
    return 0;
  }

  @Method(0x800e9798L)
  public static long FUN_800e9798(final RunningScript script) {
    final BattleScriptDataBase a2 = scriptStatePtrArr_800bc1c0.get(script.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    final BigStruct v1;
    if(a2.magic_00.get() == BattleScriptDataBase.EM__) {
      v1 = ((EffectManagerData6c)a2)._44.derefAs(BttlScriptData6cSub13c.class)._134.deref();
    } else {
      v1 = ((BattleObject27c)a2)._148;
    }

    //LAB_800e97e8
    //LAB_800e97ec
    final int a0 = script.params_20.get(1).deref().get();
    if(a0 == -1) {
      v1.b_cc.set(2);
      v1.b_cd.set(-1);
    } else if(a0 == -2) {
      //LAB_800e982c
      v1.b_cc.set(3);
      //LAB_800e980c
    } else if(a0 == -3) {
      //LAB_800e983c
      v1.b_cc.set(0);
    } else {
      //LAB_800e9844
      //LAB_800e9848
      v1.b_cc.set(3);
      v1.b_cd.set(a0);
    }

    //LAB_800e984c
    return 0;
  }

  @Method(0x800e9854L)
  public static long FUN_800e9854(final RunningScript a0) {
    final int scriptIndex = allocateEffectManager(
      a0.scriptStateIndex_00.get(),
      0x13c,
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea3f8", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea510", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea5f4", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub13c::new
    );

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    manager._04.set(0x200_0000L);
    final long v0 = FUN_800eac58(a0.params_20.get(1).deref().get() | 0x200_0000L).getAddress(); //TODO
    final BttlScriptData6cSub13c effect = manager._44.derefAs(BttlScriptData6cSub13c.class);
    effect._00.set(0);
    effect.part_04.set(v0);
    effect.ptr_08.set(v0 + MEMORY.ref(4, v0).offset(0xcL).get());
    effect.ptr_0c.set(v0 + MEMORY.ref(4, v0).offset(0x14L).get());
    final long v1 = v0 + MEMORY.ref(4, v0).offset(0x8L).get();
    effect._134.set(effect._10);
    final long tpage = GetTPage(0, 0, MEMORY.ref(2, v1).offset(0x0L).getSigned(), MEMORY.ref(2, v1).offset(0x2L).getSigned());
    final BigStruct struct = effect._134.deref();
    struct.ub_9d.set((int)_800fb06c.offset(tpage * 0x4L).get());
    FUN_800ddac8(struct, effect.ptr_08.get());
    FUN_800de36c(struct, effect.ptr_0c.get());
    FUN_80114f3c(scriptIndex, 0, 0x100, 0);
    manager._10._00.set(0x1400_0040L);
    a0.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800e99bcL)
  public static long FUN_800e99bc(final RunningScript a0) {
    final int scriptIndex = allocateEffectManager(
      a0.scriptStateIndex_00.get(),
      0x13cL,
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea3f8", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea510", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea5f4", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub13c::new
    );

    final EffectManagerData6c data = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    data._04.set(0x100_0000L);
    final DeffPart part = FUN_800eac58(a0.params_20.get(1).deref().get() | 0x100_0000L);
    final BttlScriptData6cSub13c s0 = data._44.derefAs(BttlScriptData6cSub13c.class);
    s0._00.set(0);

    //TODO
    s0.part_04.set(part.getAddress());
    s0.ptr_08.set(part.getAddress() + MEMORY.ref(4, part.getAddress()).offset(0xcL).get());
    s0.ptr_0c.set(part.getAddress() + MEMORY.ref(4, part.getAddress()).offset(0x14L).get());
    s0._10.ub_9d.set(0);
    s0._134.set(s0._10);
    FUN_800ddac8(s0._134.deref(), s0.ptr_08.get());
    FUN_800de36c(s0._134.deref(), s0.ptr_0c.get());
    FUN_80114f3c(scriptIndex, 0, 0x100, 0);
    data._10._00.set(0x5400_0000L);
    a0.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800e9ae4L)
  public static void FUN_800e9ae4(final BigStruct a0, final BattleRenderStruct a1) {
    a0.count_c8.set((short)a1.objtable2_550.nobj.get());
    a0.tmdNobj_ca.set((int)a1.objtable2_550.nobj.get());
    a0.ObjTable_0c.top.set(a1.objtable2_550.top.deref());
    a0.ObjTable_0c.nobj.set(a1.objtable2_550.nobj.get());

    //LAB_800e9b24
    memcpy(a0.coord2_14.getAddress(), a1.coord2_558.getAddress(), 0x50);

    //LAB_800e9b5c
    memcpy(a0.coord2Param_64.getAddress(), a1.param_5a8.getAddress(), 0x28);

    a0.tmd_8c.set(a1.tmd_5d0.deref());
    a0.partTransforms_90.set(a1.rotTrans_5d4.deref());
    a0.partTransforms_94.set(a1.rotTrans_5d8.deref());
    a0.animCount_98.set(a1.rotTransCount_5dc.get());
    a0.s_9a.set(a1._5de.get());
    a0.ub_9c.set(a1._5e0.get());
    a0.ub_9d.set(0);
    a0.zOffset_a0.set((short)0x200);
    a0.ub_a2.set(0);
    a0.ub_a3.set(0);
    a0.smallerStructPtr_a4.clear();
    a0.s_9e.set(a1._5e2.get());
    a0.ptr_a8.set(a1._5ec.get());

    //LAB_800e9c0c
    for(int i = 0; i < 7; i++) {
      a0.aub_ec.get(i).set(0);
    }

    a0.ui_f4.set(a1._5e4.get());
    a0.ui_f8.set(0);
    a0.scaleVector_fc.set(0x1000, 0x1000, 0x1000);
    a0.ui_108.set(0);
    a0.vector_10c.set(0x1000, 0x1000, 0x1000);
    a0.vector_118.set(0, 0, 0);
    a0.b_cc.set(0);
    a0.b_cd.set(0);

    final int count = a0.count_c8.get();
    final long addr = addToLinkedListHead(count * 0x10 + count * 0x50 + count * 0x28);
    a0.dobj2ArrPtr_00.setPointer(addr);
    a0.coord2ArrPtr_04.setPointer(addr + count * 0x10);
    a0.coord2ParamArrPtr_08.setPointer(addr + count * 0x60);
    memcpy(a0.dobj2ArrPtr_00.getPointer(), a1.dobj2s_00.getAddress(), count * 0x10);
    memcpy(a0.coord2ArrPtr_04.getPointer(), a1.coord2s_a0.getAddress(), count * 0x50);
    memcpy(a0.coord2ParamArrPtr_08.getPointer(), a1.params_3c0.getAddress(), count * 0x28);

    final GsCOORDINATE2 parent = a0.coord2_14;

    //LAB_800e9d34
    for(int i = 0; i < count; i++) {
      final GsDOBJ2 dobj2 = a0.dobj2ArrPtr_00.deref().get(i);
      dobj2.coord2_04.set(a0.coord2ArrPtr_04.deref().get(i));

      final GsCOORDINATE2 coord2 = dobj2.coord2_04.deref();
      coord2.param.set(a0.coord2ParamArrPtr_08.deref().get(i));
      coord2.super_.set(parent);
    }

    //LAB_800e9d90
    a0.coord2_14.param.set(a0.coord2Param_64);
    a0.ObjTable_0c.top.set(a0.dobj2ArrPtr_00.deref());
  }

  @Method(0x800e9db4L)
  public static void FUN_800e9db4(final BigStruct a0, final BigStruct a1) {
    //LAB_800e9dd8
    memcpy(a0.getAddress(), a1.getAddress(), 0x124);

    final int count = a0.count_c8.get();
    final long addr = addToLinkedListHead(count * 0x10 + count * 0x50 + count * 0x28);
    a0.dobj2ArrPtr_00.setPointer(addr);
    a0.coord2ArrPtr_04.setPointer(addr + count * 0x10);
    a0.coord2ParamArrPtr_08.setPointer(addr + count * 0x60);
    memcpy(a0.dobj2ArrPtr_00.getPointer(), a1.dobj2ArrPtr_00.getPointer(), count * 0x10);
    memcpy(a0.coord2ArrPtr_04.getPointer(), a1.coord2ArrPtr_04.getPointer(), count * 0x50);
    memcpy(a0.coord2ParamArrPtr_08.getPointer(), a1.coord2ParamArrPtr_08.getPointer(), count * 0x28);

    final GsCOORDINATE2 parent = a0.coord2_14;

    //LAB_800e9ee8
    for(int i = 0; i < count; i++) {
      final GsDOBJ2 dobj2 = a0.dobj2ArrPtr_00.deref().get(i);
      dobj2.coord2_04.set(a0.coord2ArrPtr_04.deref().get(i));

      final GsCOORDINATE2 coord2 = dobj2.coord2_04.deref();
      coord2.param.set(a0.coord2ParamArrPtr_08.deref().get(i));
      coord2.super_.set(parent);
    }

    //LAB_800e9f44
    a0.coord2_14.param.set(a0.coord2Param_64);
    a0.ObjTable_0c.top.set(a0.dobj2ArrPtr_00.deref());
  }

  @Method(0x800e9f68L)
  public static long FUN_800e9f68(final RunningScript a0) {
    final int s2 = a0.params_20.get(1).deref().get();
    final int managerIndex = allocateEffectManager(
      a0.scriptStateIndex_00.get(),
      0x13c,
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea3f8", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea510", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800ea5f4", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub13c::new
    );

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(managerIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    manager._04.set(0x200_0000L);

    final BttlScriptData6cSub13c s0 = manager._44.derefAs(BttlScriptData6cSub13c.class);
    s0._00.set(0);
    s0.part_04.set(0);
    s0.ptr_08.set(0);
    s0.ptr_0c.set(0);
    s0._134.set(s0._10);

    if((s2 & 0xff00_0000) == 0x700_0000) {
      FUN_800e9ae4(s0._10, _1f8003f4.deref().render_963c);
    } else {
      //LAB_800ea030
      FUN_800e9db4(s0._10, scriptStatePtrArr_800bc1c0.get(s2).deref().innerStruct_00.derefAs(BattleObject27c.class)._148);
    }

    //LAB_800ea04c
    final BigStruct v1 = s0._134.deref();
    manager._10.vec_04.set(v1.coord2_14.coord.transfer);
    manager._10.svec_10.set(v1.coord2Param_64.rotate);
    manager._10.svec_16.set(v1.scaleVector_fc);
    manager._10._00.set(0x1400_0040L);
    a0.params_20.get(0).deref().set(managerIndex);
    return 0;
  }

  @Method(0x800ea0f4L)
  public static GsCOORDINATE2 FUN_800ea0f4(final EffectManagerData6c a0, final long a1) {
    final BigStruct struct = a0._44.derefAs(BttlScriptData6cSub13c.class)._10;
    FUN_800214bc(struct);
    return struct.coord2ArrPtr_04.deref().get((int)a1);
  }

  @Method(0x800ea13cL)
  public static long FUN_800ea13c(final RunningScript a0) {
    final BigStruct v0 = scriptStatePtrArr_800bc1c0.get((short)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.derefAs(BttlScriptData6cSub13c.class)._134.deref();
    final int a1 = a0.params_20.get(1).deref().get() & 0xffff;

    //TODO
    MEMORY.ref(4, v0.ui_f4.getAddress()).offset(((short)a1 >> 5) * 0x4L).oru(1L << (a1 & 0x1f));
    return 0;
  }

  @Method(0x800ea19cL)
  public static long FUN_800ea19c(final RunningScript a0) {
    final BigStruct v0 = scriptStatePtrArr_800bc1c0.get((short)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.derefAs(BttlScriptData6cSub13c.class)._134.deref();
    final int v1 = a0.params_20.get(1).deref().get() & 0xffff;

    //TODO
    MEMORY.ref(4, v0.ui_f4.getAddress()).offset(((short)v1 >> 5) * 0x4L).and(~(1L << (v1 & 0x1f)));
    return 0;
  }

  @Method(0x800ea200L)
  public static long FUN_800ea200(final RunningScript a0) {
    final int effectIndex = a0.params_20.get(0).deref().get();
    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(effectIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub13c effect = manager._44.derefAs(BttlScriptData6cSub13c.class);
    long v0 = FUN_800eac58(a0.params_20.get(1).deref().get() | 0x200_0000).getAddress();
    v0 = v0 + MEMORY.ref(4, v0).offset(0x14L).get();
    effect.ptr_0c.set(v0);
    FUN_800de36c(effect._134.deref(), v0);
    manager._10._24.set(0);
    FUN_80114f3c(effectIndex, 0, 0x100, 0);
    return 0;
  }

  @Method(0x800ea2a0L)
  public static long FUN_800ea2a0(final RunningScript script) {
    final BattleScriptDataBase a2 = scriptStatePtrArr_800bc1c0.get(script.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    final BigStruct v0;
    if(a2.magic_00.get() == BattleScriptDataBase.EM__) {
      v0 = ((EffectManagerData6c)a2)._44.derefAs(BttlScriptData6cSub13c.class)._134.deref();
    } else {
      //LAB_800ea2f8
      v0 = ((BattleObject27c)a2)._148;
    }

    //LAB_800ea300
    v0.vector_10c.setX(script.params_20.get(1).deref().get());
    v0.vector_10c.setZ(script.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800ea30cL)
  public static long FUN_800ea30c(final RunningScript script) {
    final BattleScriptDataBase a3 = scriptStatePtrArr_800bc1c0.get(script.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    final BigStruct v0;
    if(a3.magic_00.get() == BattleScriptDataBase.EM__) {
      v0 = ((EffectManagerData6c)a3)._44.derefAs(BttlScriptData6cSub13c.class)._134.deref();
    } else {
      //LAB_800ea36c
      v0 = ((BattleObject27c)a3)._148;
    }

    //LAB_800ea374
    v0.vector_118.set(script.params_20.get(1).deref().get(), script.params_20.get(2).deref().get(), script.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800ea384L)
  public static long FUN_800ea384(final RunningScript a0) {
    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub13c effect = manager._44.derefAs(BttlScriptData6cSub13c.class);

    if(effect.ptr_0c.get() == 0) {
      a0.params_20.get(1).deref().set(0);
    } else {
      //LAB_800ea3cc
      a0.params_20.get(1).deref().set((int)(manager._10._24.get() + 2) / effect._134.deref().s_9a.get());
    }

    //LAB_800ea3e4
    return 0;
  }

  @Method(0x800ea3f8L)
  public static void FUN_800ea3f8(final int scriptIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final MATRIX sp0x10 = new MATRIX();
    FUN_800e8594(sp0x10, manager);

    final BttlScriptData6cSub13c s0 = manager._44.derefAs(BttlScriptData6cSub13c.class);
    final BigStruct v1 = s0._134.deref();
    v1.coord2Param_64.rotate.set(manager._10.svec_10);
    v1.scaleVector_fc.set(manager._10.svec_16);
    v1.zOffset_a0.set(manager._10._22.get());
    v1.coord2_14.coord.set(sp0x10);
    v1.coord2_14.flg.set(0);

    if(s0.ptr_0c.get() != 0) {
      FUN_800de2e8(v1, manager._10._24.get());
    }

    //LAB_800ea4fc
  }

  @Method(0x800ea510L)
  public static void FUN_800ea510(final int scriptIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub13c s1 = manager._44.derefAs(BttlScriptData6cSub13c.class);
    if((int)manager._10._00.get() >= 0) {
      if((manager._10._00.get() & 0x40L) == 0) {
        FUN_800e61e4(manager._10.svec_1c.getX() << 5, manager._10.svec_1c.getY() << 5, manager._10.svec_1c.getZ() << 5);
      } else {
        //LAB_800ea564
        FUN_800e60e0(0x1000, 0x1000, 0x1000);
      }

      //LAB_800ea574
      final BigStruct a0 = s1._134.deref();

      final long s2 = a0.ui_108.get();

      if((manager._10._00.get() & 0x4000_0000L) != 0) {
        a0.ui_108.set(manager._10._00.get() >>> 23 & 0x60L);
      }

      //LAB_800ea598
      FUN_800dd89c(a0, manager._10._00.get());

      a0.ui_108.set(s2);

      if((manager._10._00.get() & 0x40L) == 0) {
        FUN_800e62a8();
      } else {
        //LAB_800ea5d4
        FUN_800e6170();
      }
    }

    //LAB_800ea5dc
  }

  @Method(0x800ea5f4L)
  public static void FUN_800ea5f4(final int scriptIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    FUN_80020fe0(manager._44.derefAs(BttlScriptData6cSub13c.class)._134.deref());
  }

  @Method(0x800ea620L)
  public static void FUN_800ea620(final DeffFile deff, final long size, final int scriptIndex) {
    //LAB_800ea674
    for(int i = 0; i < deff.pointerCount_06.get(); i++) {
      final DeffPart deffPart = deff.pointers_08.get(i).part_04.deref();
      final long type = deff.pointers_08.get(i).flags_00.get() & 0xff00_0000L;
      if(type == 0x100_0000L) {
        //LAB_800ea6d4
        final ExtendedTmd extTmd = MEMORY.ref(4, deffPart.getAddress() + MEMORY.ref(4, deffPart.getAddress()).offset(0xcL).get(), ExtendedTmd::new); //TODO
        adjustTmdPointers(extTmd.tmdPtr_00.deref().tmd);

        //LAB_800ea700
        final TmdWithId tmd = extTmd.tmdPtr_00.deref();
        for(int objectIndex = 0; objectIndex < tmd.tmd.header.nobj.get(); objectIndex++) {
          optimisePacketsIfNecessary(tmd, objectIndex);
        }
        //LAB_800ea6b4
      } else if(type == 0x300_0000L) {
        //LAB_800ea724
        final ExtendedTmd extTmd = MEMORY.ref(4, deffPart.getAddress() + MEMORY.ref(4, deffPart.getAddress()).offset(0xcL).get(), ExtendedTmd::new); //TODO
        adjustTmdPointers(extTmd.tmdPtr_00.deref().tmd);
        optimisePacketsIfNecessary(extTmd.tmdPtr_00.deref(), 0);
      }

      if(type == 0x100_0000L || type == 0x200_0000L || type == 0x300_0000L) {
        //LAB_800ea748
        final long a2_0 = MEMORY.ref(4, deffPart.getAddress()).offset(0x8L).get();
        final long v1_0 = MEMORY.ref(4, deffPart.getAddress()).offset(0xcL).get();

        if(a2_0 != v1_0 && scriptIndex != 0) {
          FUN_800eb308(scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class), deffPart.getAddress() + v1_0, deffPart.getAddress() + a2_0);
        }
      }

      //LAB_800ea778
      //LAB_800ea77c
    }

    //LAB_800ea790
    deff_800c6950.set(deff);
    struct7cc_800c693c.deref().deff_5ac.set(deff);
  }

  @Method(0x800ea7d0L)
  public static void FUN_800ea7d0(final DeffFile deff, final long size, final long a2) {
    long v0;
    long a1;
    final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();
    FUN_800ea620(deff, size, struct7cc.scriptIndex_1c.get());

    final DeffFile deff2 = struct7cc.deff_5ac.deref();

    //LAB_800ea814
    int i;
    for(i = 0; i <= deff2.pointerCount_06.get(); i++) {
      v0 = deff2.pointers_08.get(i).flags_00.get();

      if((v0 & 0xff00_0000L) != 0) {
        break;
      }

      struct7cc._390.get((int)(v0 & 0xff)).set(deff2.pointers_08.get(i).part_04.deref());
    }

    //LAB_800ea850
    //LAB_800ea874
    for(; i <= deff2.pointerCount_06.get(); i++) {
      if((deff2.pointers_08.get(i).flags_00.get() & 0xff00_0000L) != 0x100_0000L) {
        break;
      }
    }

    //LAB_800ea89c
    //LAB_800ea8a8
    for(int n = 0; n < 0x40; n++) {
      struct7cc._2f8.get(n).clear();
    }

    //LAB_800ea8e0
    for(; i <= deff2.pointerCount_06.get(); i++) {
      v0 = deff2.pointers_08.get(i).flags_00.get();

      if((v0 & 0xff00_0000L) != 0x300_0000L) {
        break;
      }

      a1 = v0 & 0xffL;
      if(a1 >= 0x5L) {
        v0 = deff2.pointers_08.get(i).part_04.deref().getAddress();
        struct7cc._2f8.get((int)a1).set(MEMORY.ref(4, v0 + MEMORY.ref(4, v0).offset(0xcL).get(), ExtendedTmd::new).tmdPtr_00.deref().tmd.objTable.get(0));
      }

      //LAB_800ea928
    }

    //LAB_800ea93c
    //LAB_800ea964
    for(; i <= deff2.pointerCount_06.get(); i++) {
      v0 = deff2.pointers_08.get(i).flags_00.get();

      if((v0 & 0xff00_0000L) != 0x400_0000L) {
        break;
      }

      a1 = (v0 & 0xffL) * 0x8L;
      long a0 = deff2.pointers_08.get(i).part_04.deref().getAddress(); //TODO
      a0 = a0 + MEMORY.ref(4, a0).offset(0x8L).get();
      struct7cc._39c.offset(a1).offset(2, 0x0L).setu(MEMORY.ref(2, a0).offset(0x0L).get());
      struct7cc._39c.offset(a1).offset(2, 0x2L).setu(MEMORY.ref(2, a0).offset(0x2L).get());
      struct7cc._39c.offset(a1).offset(1, 0x4L).setu(MEMORY.ref(2, a0).offset(0x4L).getSigned() * 0x4L);
      struct7cc._39c.offset(a1).offset(1, 0x5L).setu(MEMORY.ref(1, a0).offset(0x6L).get());
      struct7cc._39c.offset(a1).offset(2, 0x6L).setu(MEMORY.ref(2, a0).offset(0xaL).get() << 6 | (MEMORY.ref(2, a0).offset(0x8L).get() & 0x3f0L) >>> 4);
    }

    //LAB_800eaa00
    //LAB_800eaa04
    struct7cc.deff_38.set(deff2);
    struct7cc.deff_5ac.clear();
  }

  @Method(0x800eaa24L)
  public static void loadBattleHudDeff(final long address, final long fileSize, final long param) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    long size = mrg.entries.get(2).size.get();
    final DeffFile deff = MEMORY.ref(4, addToLinkedListTail(size), DeffFile::new);

    //LAB_800eaa74
    //LAB_800eaa90
    memcpy(deff.getAddress(), mrg.getFile(2), (int)size);
    FUN_800ea7d0(deff, size, 0);

    size = mrg.entries.get(3).size.get();
    final MrgFile mrg1 = MEMORY.ref(4, addToLinkedListTail(size), MrgFile::new);

    //LAB_800eaad4
    //LAB_800eaaf0
    memcpy(mrg1.getAddress(), mrg.getFile(3), (int)size);
    FUN_800e929c(mrg1.getAddress(), size, 0);

    size = mrg.entries.get(1).size.get();
    final MrgFile mrg2 = MEMORY.ref(4, addToLinkedListTail(size), MrgFile::new);

    //LAB_800eab34
    //LAB_800eab50
    memcpy(mrg2.getAddress(), mrg.getFile(1), (int)size);
    FUN_800e9288(mrg2, size, struct7cc_800c693c.deref().mrg_2c);
    removeFromLinkedList(address);
  }

  @Method(0x800eab8cL)
  public static void FUN_800eab8c() {
    final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();

    long a0 = struct7cc._34.get();
    if(a0 != 0) {
      removeFromLinkedList(a0);
      struct7cc._34.set(0);
    }

    //LAB_800eabc4
    a0 = struct7cc._30.get();
    if(a0 != 0) {
      removeFromLinkedList(a0);
      struct7cc._30.set(0);
    }

    //LAB_800eabf4
    if(!struct7cc.deff_38.isNull()) {
      removeFromLinkedList(struct7cc.deff_38.getPointer());
      struct7cc.deff_38.clear();
    }

    //LAB_800eac1c
    if(!struct7cc.mrg_2c.isNull()) {
      removeFromLinkedList(struct7cc.mrg_2c.getPointer());
      struct7cc.mrg_2c.clear();
    }

    //LAB_800eac48
  }

  @Method(0x800eac58L)
  public static DeffPart FUN_800eac58(final long a0) {
    final DeffFile deff = struct7cc_800c693c.deref().deff_5ac.deref();

    //LAB_800eac84
    for(int i = 0; i < deff.pointerCount_06.get(); i++) {
      if(deff.pointers_08.get(i).flags_00.get() == a0) {
        return deff.pointers_08.get(i).part_04.deref();
      }

      //LAB_800eaca0
    }

    //LAB_800eacac
    return null;
  }

  @Method(0x800eacf4L)
  public static void loadBattleHudDeff() {
    loadDrgnBinFile(0, 4114, 0, getMethodAddress(Bttl_800e.class, "loadBattleHudDeff", long.class, long.class, long.class), struct7cc_800c693c.deref().mrg_2c.getAddress(), 0x4L);
  }

  @Method(0x800ead44L)
  public static void FUN_800ead44(final RECT a0, final long a1) {
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)960, (short)256, a0.w.get(), (short)a1), a0.x.get(), a0.y.get() + a0.h.get() - a1);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT(a0.x.get(), (short)(a0.y.get() + a1), a0.w.get(), (short)(a0.h.get() - a1)), a0.x.get(), a0.y.get());
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT(a0.x.get(), a0.y.get(), a0.w.get(), (short)a1), 0x3c0L, 0x100L);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);
  }

  @Method(0x800eaec8L)
  public static long FUN_800eaec8(final EffectManagerData6c data, final BttlScriptData6cSub1c sub) {
    long a1 = sub._14.get() / 0x100;

    //LAB_800eaef0
    sub._14.add(sub._18.get());

    //LAB_800eaf08
    a1 = (sub._14.get() / 0x100 - a1) % sub._0c.h.get();

    if((int)a1 < 0) {
      a1 = a1 + sub._0c.h.get();
    }

    //LAB_800eaf30
    if(a1 != 0) {
      FUN_800ead44(sub._0c, a1);
    }

    //LAB_800eaf44
    return 0x1L;
  }

  @Method(0x800eaf54L)
  public static BttlScriptData6cSub1c FUN_800eaf54(EffectManagerData6c a0, final RECT a1) {
    //LAB_800eaf80
    while((a0._04.get() & 0x400L) == 0) {
      final int parentIndex = a0.parentScriptIndex_50.get();

      if(parentIndex == -1) {
        break;
      }

      a0 = scriptStatePtrArr_800bc1c0.get(parentIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    }

    //LAB_800eafb8
    BttlScriptData6cSub1c a0_0 = (BttlScriptData6cSub1c)FUN_800e8c84(a0, 10);

    //LAB_800eafcc
    while(a0_0 != null) {
      if(a0_0._0c.x.get() == a1.x.get() && a0_0._0c.y.get() == a1.y.get()) {
        break;
      }

      //LAB_800eaff4
      a0_0 = FUN_800e8cc8(a0_0._00.derefNullableAs(BttlScriptData6cSub1c.class), (byte)10);
    }

    //LAB_800eb00c
    return a0_0;
  }

  @Method(0x800eb01cL)
  public static long FUN_800eb01c(final RunningScript script) {
    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get((short)script.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub14_2 effect = manager._44.derefAs(BttlScriptData6cSub14_2.class);
    final long v1 = effect._04.get();
    final long v0_0 = v1 + MEMORY.ref(4, v1).offset(0x8L).get() + (short)script.params_20.get(1).deref().get() * 0x10L;
    final SVECTOR sp0x10 = MEMORY.ref(4, v0_0, SVECTOR::new);

    EffectManagerData6c v1_0 = manager;

    //LAB_800eb0c0
    while((v1_0._04.get() & 0x400L) == 0) {
      final int parentIndex = v1_0.parentScriptIndex_50.get();

      if(parentIndex == -1) {
        break;
      }

      v1_0 = scriptStatePtrArr_800bc1c0.get(parentIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    }

    //LAB_800eb0f8
    Pointer<BttlScriptData6cSubBase2> a0 = v1_0._58;
    //LAB_800eb10c
    while(!a0.isNull()) {
      final BttlScriptData6cSub1c a1 = a0.derefAs(BttlScriptData6cSub1c.class);

      if(a1._05.get() == 10) {
        if(a1._0c.x.get() == sp0x10.getX()) {
          if(a1._0c.y.get() == sp0x10.getY()) {
            FUN_800e8e68(a0);
            break;
          }
        }
      }

      //LAB_800eb15c
      a0 = a0.deref()._00;
    }

    //LAB_800eb174
    //LAB_800eb178
    return 0;
  }

  @Method(0x800eb188L)
  public static long FUN_800eb188(final RunningScript script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get((short)script.params_20.get(0).deref().get()).deref();
    final EffectManagerData6c manager = state.innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub14_2 effect = manager._44.derefAs(BttlScriptData6cSub14_2.class);

    final long v1 = effect._04.get();
    long v0 = v1 + MEMORY.ref(4, v1).offset(0x8L).get() + (short)script.params_20.get(1).deref().get() * 0x10L;
    final BttlScriptData6cSub1c a0 = FUN_800eaf54(manager, MEMORY.ref(4, v0, RECT::new));

    if(a0 != null) {
      long a1 = -a0._14.get();
      if((int)a1 >= 0) {
        v0 = a1;
      } else {
        v0 = a1 + 0xffL;
      }

      //LAB_800eb238
      a1 = ((int)v0 >> 8) % a0._0c.h.get();

      if((int)a1 < 0) {
        a1 = a1 + a0._0c.h.get();
      }

      //LAB_800eb25c
      if(a1 != 0) {
        FUN_800ead44(a0._0c, a1);
      }
    }

    //LAB_800eb270
    return 0;
  }

  @Method(0x800eb280L)
  public static void FUN_800eb280(final EffectManagerData6c a0, final RECT a1, final int a2) {
    BttlScriptData6cSub1c v0 = FUN_800eaf54(a0, a1);

    if(v0 == null) {
      v0 = FUN_800e8dd4(a0, 0xa, 0, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800eaec8", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1c, BttlScriptData6cSub1c::new);
      v0._0c.set(a1);
      v0._14.set(0);
    }

    //LAB_800eb2ec
    v0._18.set(a2);
  }

  @Method(0x800eb308L)
  public static void FUN_800eb308(final EffectManagerData6c a0, final long a1, final long a2) {
    if(MEMORY.ref(4, a1).offset(0x8L).get() != 0) {
      final long s2 = a1 + MEMORY.ref(4, a1).offset(0x8L).get();

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
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
    final EffectManagerData6c manager = state.innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub14_2 effect = manager._44.derefAs(BttlScriptData6cSub14_2.class);
    final long v0 = effect._04.get();
    final RECT sp0x10 = new RECT().set(MEMORY.ref(2, v0 + MEMORY.ref(4, v0).offset(0x8L).get() + a1 * 0x10L, RECT::new));
    FUN_800eb280(manager, sp0x10, a2);
  }

  @Method(0x800eb518L)
  public static long FUN_800eb518(final RunningScript script) {
    FUN_800eb48c(script.params_20.get(0).deref().get(), script.params_20.get(1).deref().get(), script.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800eb554L)
  public static void FUN_800eb554(final RECT a0, final DVECTOR a1, final int height) {
    final RECT sp0x10 = new RECT();

    sp0x10.x.set((short)960);
    sp0x10.y.set((short)256);
    sp0x10.w.set(a0.w.get());
    sp0x10.h.set((short)height);
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), sp0x10, a1.getX(), a1.getY() + a0.h.get() - height);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    sp0x10.x.set(a1.getX());
    sp0x10.y.set((short)(a1.getY() + height));
    sp0x10.w.set(a0.w.get());
    sp0x10.h.set((short)(a0.h.get() - height));
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), sp0x10, a1.getX(), a1.getY());
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    sp0x10.x.set(a1.getX());
    sp0x10.y.set(a1.getY());
    sp0x10.w.set(a0.w.get());
    sp0x10.h.set((short)height);
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), sp0x10, a0.x.get(), a0.y.get() + a0.h.get() - height);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    sp0x10.x.set(a0.x.get());
    sp0x10.y.set((short)(a0.y.get() + height));
    sp0x10.w.set(a0.w.get());
    sp0x10.h.set((short)(a0.h.get() - height));
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), sp0x10, a0.x.get(), a0.h.get());
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    sp0x10.x.set(a0.x.get());
    sp0x10.y.set(a0.y.get());
    sp0x10.w.set(a0.w.get());
    sp0x10.h.set((short)height);
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), sp0x10, 960, 256);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);
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
  public static long FUN_800eb84c(final RunningScript script) {
    EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(script.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub14_2 effect = manager._44.derefAs(BttlScriptData6cSub14_2.class);
    final long v0 = effect._04.get();
    final long v1 = MEMORY.ref(4, v0).offset(0x8L).get();
    final long s1 = v0 + v1 + script.params_20.get(1).deref().get() * 0x10L;
    final long s0 = v0 + v1 + script.params_20.get(2).deref().get() * 0x10L;

    //LAB_800eb8fc
    while((manager._04.get() & 0x400L) == 0) {
      final int parentIndex = manager.parentScriptIndex_50.get();

      if(parentIndex == -1) {
        break;
      }

      manager = scriptStatePtrArr_800bc1c0.get(parentIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    }

    //LAB_800eb934
    final BttlScriptData6cSub20 sub = FUN_800e8dd4(manager, 0xa, 0, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800eb7c4", EffectManagerData6c.class, BttlScriptData6cSub20.class), BiFunctionRef::new), 0x20, BttlScriptData6cSub20::new);
    sub._0c.set(MEMORY.ref(4, s1, RECT::new));
    sub._14.set(0);
    sub._18.set(script.params_20.get(3).deref().get());
    sub._1c.set(MEMORY.ref(2, s0, DVECTOR::new));
    return 0;
  }

  @Method(0x800eb9acL)
  public static void FUN_800eb9ac(final BattleRenderStruct s2, final ExtendedTmd extTmd, final TmdAnimationFile tmdAnim) {
    final int x = s2.coord2_558.coord.transfer.getX();
    final int y = s2.coord2_558.coord.transfer.getY();
    final int z = s2.coord2_558.coord.transfer.getZ();

    _800bda0c.set(s2);

    //LAB_800eb9fc
    for(int i = 0; i < 10; i++) {
      s2._618.get(i).set(0);
    }

    s2.tmd_5d0.set(extTmd.tmdPtr_00.deref().tmd);

    if(extTmd.ptr_08.get() != 0) {
      s2._5ec.set(extTmd.getAddress() + extTmd.ptr_08.get() / 0x4L * 0x4L); //TODO

      //LAB_800eba38
      for(int i = 0; i < 10; i++) {
        s2._5f0.get(i).set(s2._5ec.get() + MEMORY.ref(2, s2._5ec.get()).offset(i * 0x4L).get());
        FUN_800ec86c(s2, i);
      }
    } else {
      //LAB_800eba74
      //LAB_800eba7c
      for(int i = 0; i < 10; i++) {
        s2._5f0.get(i).set(0);
      }
    }

    //LAB_800eba8c
    adjustTmdPointers(s2.tmd_5d0.deref());
    initObjTable2(s2.objtable2_550, s2.dobj2s_00, s2.coord2s_a0, s2.params_3c0, 10);
    s2.coord2_558.param.set(s2.param_5a8);
    GsInitCoordinate2(null, s2.coord2_558);
    FUN_80021ca0(s2.objtable2_550, s2.tmd_5d0.deref(), s2.coord2_558, 10, extTmd.tmdPtr_00.deref().tmd.header.nobj.get() + 0x1L);
    FUN_800ec774(s2, tmdAnim);

    s2.coord2_558.coord.transfer.setX(x);
    s2.coord2_558.coord.transfer.setY(y);
    s2.coord2_558.coord.transfer.setZ(z);
    s2._5e4.set(0);
    s2._5e8.set((short)0x200);
  }

  @Method(0x800ebb58L)
  public static void FUN_800ebb58(final int a0) {
    long v0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    final long t7;
    t7 = _800c6958.get();

    //LAB_800ebb7c
    for(t2 = 0; t2 < _800c695c.getSigned(); t2++) {
      //LAB_800ebb80
      for(a3 = 0; a3 < 0x10; a3++) {
        t1 = MEMORY.ref(2, t7).offset(t2 * 0x20L).offset(a3 * 0x2L).get();
        a2 = (t1 & 0x1fL) * a0;
        a1 = (t1 >> 5 & 0x1fL) * a0;
        t3 = (t1 >> 10 & 0x1fL) * a0;
        t0 = a2 >> 4 & 0x1fL;
        a1 = a1 >> 4 & 0x1fL;
        a2 = t3 >> 4 & 0x1fL;
        t3 = t1 >>> 15 & 0x1L;
        if(t0 != 0 && a1 != 0 && a2 != 0 || (short)t1 == 0) {
          //LAB_800ebbf0
          v0 = t0 | t3 << 15 | a2 << 10 | a1 << 5;
        } else {
          //LAB_800ebc0c
          v0 = t1 & 0xffff_8000L | 0x1L;
        }

        //LAB_800ebc18
        MEMORY.ref(2, t7).offset(0x800L).offset(t2 * 0x20L).offset(a3 * 0x2L).setu(v0);
      }
    }

    //LAB_800ebc44
    a1 = _800c6958.get();

    //LAB_800ebc58
    for(; t2 < 0x40; t2++) {
      //LAB_800ebc5c
      for(a3 = 0; a3 < 0x10; a3++) {
        MEMORY.ref(2, a1).offset(0x800L).offset(t2 * 0x20L).offset(a3 * 0x2L).setu(MEMORY.ref(2, a1).offset(t2 * 0x20L).offset(a3 * 0x2L).get());
      }
    }

    //LAB_800ebc88
    t0 = _800c6958.get();

    //LAB_800ebca4
    for(t2 = 0; t2 < 0x40; t2++) {
      //LAB_800ebcb0
      for(a3 = 0; a3 < 0x10; a3++) {
        MEMORY.ref(2, t0).offset(0x1000L).offset(t2 * 0x20L).offset(a3 * 0x2L).setu(MEMORY.ref(2, t0).offset(0x800L).offset(_800fb148.get((int)t2).get() * 0x20L).offset(a3 * 0x2L).get());
      }
    }

    final RECT sp0x10 = new RECT((short)448, (short)240, (short)64, (short)16);
    LoadImage(sp0x10, _800c6958.get() + 0x1000L);
  }

  @Method(0x800ebd34L)
  public static void FUN_800ebd34(final BattleRenderStruct struct, final int index) {
    long v0;
    long a2;
    final long t0;
    final long t1;
    long s0;
    final long s1;
    final long s4;
    final long s6;

    v0 = struct._5f0.get(index).get(); //TODO ptr to RECT?

    if(v0 == 0) {
      struct._618.get(index).set(0);
      return;
    }

    //LAB_800ebd84
    final long x = MEMORY.ref(2, v0).offset(0x0L).get();
    final long y = MEMORY.ref(2, v0).offset(0x2L).get();
    final long w = MEMORY.ref(2, v0).offset(0x4L).get() >>> 2;
    final long h = MEMORY.ref(2, v0).offset(0x6L).get();

    //LAB_800ebdcc
    a2 = v0 + 0x8L;

    // There was a loop here, but each iteration overwrote the results from the previous iteration... I collapsed it into a single iteration
    a2 += (struct._65e.get(index).get() - 1) * 0x4L;
    s0 = MEMORY.ref(2, a2).offset(0x2L).get();
    t1 = MEMORY.ref(2, a2).offset(0x0L).get() & 0x1L;
    t0 = MEMORY.ref(2, a2).offset(0x0L).get() >>> 1;
    a2 = a2 + 0x4L;

    //LAB_800ebdf0
    if((s0 & 0xfL) != 0 && (struct._622.get(index).get() & 0xfL) != 0) {
      struct._622.get(index).decr();

      if(struct._622.get(index).get() == 0) {
        struct._622.get(index).set((int)s0);
        s0 = 0x10L;
      } else {
        //LAB_800ebe34
        s0 = 0;
      }
    }

    //LAB_800ebe38
    struct._64a.get(index).incr();

    if(struct._64a.get(index).get() >= (short)t0) {
      struct._64a.get(index).set((short)0);

      if(MEMORY.ref(2, a2).offset(0x0L).get() != 0xffffL) {
        v0 = struct._65e.get(index).get() + 0x1L;
      } else {
        //LAB_800ebe88
        v0 = 0x1L;
      }

      //LAB_800ebe8c
      struct._65e.get(index).set((short)v0);
    }

    //LAB_800ebe94
    if(s0 != 0) {
      if(t1 == 0) {
        s1 = (int)s0 >> 4;
        s4 = h - s1;
        s6 = 0x100L + s1;

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)960, (short)256, (short)w, (short)h), x, y);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)x, (short)(y + s4), (short)w, (short)s1), 960, 256);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)x, (short)y, (short)w, (short)s4), 960, s6 & 0xffffL);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);
      } else {
        //LAB_800ebf88
        s1 = (int)s0 >> 4;
        s4 = h - s1;
        s6 = 0x100L + s4;

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)960, (short)256, (short)w, (short)h), x, y);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)x, (short)y, (short)w, (short)s1), 960, s6 & 0xffffL);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)x, (short)(y + s1), (short)w, (short)s4), 960, 256);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);
      }
    }

    //LAB_800ec080
  }

  @Method(0x800ec258L)
  public static void FUN_800ec258(final BigStruct a0) {
    final BigStruct s2 = bigStruct_800bda10;

    GsInitCoordinate2(a0.coord2_14, s2.coord2_14);

    if(a0.b_cc.get() != 3) {
      s2.coord2_14.coord.transfer.setX(a0.vector_118.getX());

      if(a0.b_cc.get() == 1) {
        s2.coord2_14.coord.transfer.setY(a0.vector_118.getY());
      } else {
        //LAB_800ec2bc
        s2.coord2_14.coord.transfer.setY(a0.vector_118.getY() - (a0.coord2_14.coord.transfer.getY() << 12) / a0.scaleVector_fc.getY());
      }

      //LAB_800ec2e0
      s2.coord2_14.coord.transfer.setZ(a0.vector_118.getZ());
    } else {
      //LAB_800ec2ec
      s2.coord2_14.coord.transfer.setX(a0.vector_118.getX() + a0.coord2ArrPtr_04.deref().get(a0.b_cd.get()).coord.transfer.getX());
      s2.coord2_14.coord.transfer.setY(a0.vector_118.getY() - (a0.coord2_14.coord.transfer.getY() << 12) / a0.scaleVector_fc.getY());
      s2.coord2_14.coord.transfer.setZ(a0.vector_118.getZ() + a0.coord2ArrPtr_04.deref().get(a0.b_cd.get()).coord.transfer.getZ());
    }

    //LAB_800ec370
    s2.zOffset_a0.set((short)(a0.zOffset_a0.get() + 0x10));
    s2.scaleVector_fc.setX(a0.vector_10c.getX() / 4);
    s2.scaleVector_fc.setY(a0.vector_10c.getY() / 4);
    s2.scaleVector_fc.setZ(a0.vector_10c.getZ() / 4);
    RotMatrix_8003faf0(s2.coord2Param_64.rotate, s2.coord2_14.coord);
    final VECTOR scale = new VECTOR().set(s2.scaleVector_fc);
    ScaleMatrixL(s2.coord2_14.coord, scale);
    s2.coord2_14.flg.set(0);
    final GsCOORDINATE2 v0 = s2.dobj2ArrPtr_00.deref().get(0).coord2_04.deref();
    final GsCOORD2PARAM s0 = v0.param.deref();
    s0.rotate.set((short)0, (short)0, (short)0);
    RotMatrix_80040780(s0.rotate, v0.coord);
    s0.trans.set(0, 0, 0);
    TransMatrix(v0.coord, s0.trans);

    final MATRIX sp0x30 = new MATRIX();
    final MATRIX sp0x10 = new MATRIX();
    GsGetLws(s2.ObjTable_0c.top.deref().get(0).coord2_04.deref(), sp0x30, sp0x10);
    GsSetLightMatrix(sp0x30);
    CPU.CTC2(sp0x10.getPacked(0), 0);
    CPU.CTC2(sp0x10.getPacked(2), 1);
    CPU.CTC2(sp0x10.getPacked(4), 2);
    CPU.CTC2(sp0x10.getPacked(6), 3);
    CPU.CTC2(sp0x10.getPacked(8), 4);
    CPU.CTC2(sp0x10.transfer.getX(), 5);
    CPU.CTC2(sp0x10.transfer.getY(), 6);
    CPU.CTC2(sp0x10.transfer.getZ(), 7);
    Renderer.renderDobj2(s2.ObjTable_0c.top.deref().get(0), true);
    s2.coord2ArrPtr_04.deref().get(0).flg.decr();
  }

  @Method(0x800ec4bcL)
  public static void FUN_800ec4bc() {
    _800c6958.setu(addToLinkedListTail(0x1800L));
  }

  @Method(0x800ec4f0L)
  public static void FUN_800ec4f0() {
    removeFromLinkedList(_800c6958.get());
  }

  @Method(0x800ec51cL)
  public static void FUN_800ec51c(final BattleRenderStruct a0) {
    //LAB_800ec548
    for(int i = 0; i < 10; i++) {
      if(a0._618.get(i).get() != 0) {
        FUN_800ebd34(a0, i);
      }

      //LAB_800ec560
    }

    _1f8003ec.setu(0);
    zOffset_1f8003e8.setu(a0._5e8.get());

    //LAB_800ec5a0
    long s4 = 0x1L;
    for(int i = 0; i < a0.objtable2_550.nobj.get(); i++) {
      final GsDOBJ2 dobj2 = a0.objtable2_550.top.deref().get(i);
      if((s4 & a0._5e4.get()) == 0) {
        final MATRIX ls = new MATRIX();
        final MATRIX lw = new MATRIX();
        GsGetLws(dobj2.coord2_04.deref(), lw, ls);
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
  public static void FUN_800ec63c(final BattleRenderStruct a0) {
    //LAB_800ec688
    for(int i = 0; i < a0.rotTransCount_5dc.get(); i++) {
      final ModelPartTransforms rotTrans = a0.rotTrans_5d8.deref().get(i);
      final GsCOORDINATE2 coord2 = a0.dobj2s_00.get(i).coord2_04.deref();
      final GsCOORD2PARAM param = coord2.param.deref();

      param.rotate.set(rotTrans.rotate_00);
      RotMatrix_80040010(param.rotate, coord2.coord);

      param.trans.set(rotTrans.translate_06);
      TransMatrix(coord2.coord, param.trans);
    }

    //LAB_800ec710
    a0.rotTrans_5d8.set(a0.rotTrans_5d8.deref().slice(a0.rotTransCount_5dc.get()));
  }

  @Method(0x800ec744L)
  public static void FUN_800ec744(final BattleRenderStruct a0) {
    RotMatrix_8003faf0(a0.param_5a8.rotate, a0.coord2_558.coord);
    a0.coord2_558.flg.set(0);
  }

  @Method(0x800ec774L)
  public static void FUN_800ec774(final BattleRenderStruct a0, final TmdAnimationFile a1) {
    a0.rotTrans_5d4.set(a1.partTransforms_10);
    a0.rotTrans_5d8.set(a1.partTransforms_10);
    a0.rotTransCount_5dc.set((short)a1.count_0c.get());
    a0._5de.set(a1._0e.get());
    a0._5e0.set((short)0);
    FUN_800ec63c(a0);
    a0._5e0.set((short)1);
    a0._5e2.set(a0._5de.get());
    a0.rotTrans_5d8.set(a0.rotTrans_5d4.deref());
  }

  @Method(0x800ec7e4L)
  public static DVECTOR perspectiveTransformXyz(final BigStruct a0, final short x, final short y, final short z) {
    final MATRIX ls = new MATRIX();
    GsGetLs(a0.coord2_14, ls);
    setRotTransMatrix(ls);

    final DVECTOR screenCoords = new DVECTOR();
    perspectiveTransform(new SVECTOR().set(x, y, z), screenCoords, new Ref<>(), new Ref<>());
    return screenCoords;
  }

  @Method(0x800ec86cL)
  public static void FUN_800ec86c(final BattleRenderStruct a0, final int index) {
    final long a2 = a0._5f0.get(index).get();

    if(a2 == 0) {
      a0._618.get(index).set(0);
      return;
    }

    //LAB_800ec890
    if(MEMORY.ref(2, a2).get() == 0xffffL) {
      a0._5f0.get(index).set(0);
      return;
    }

    //LAB_800ec8a8
    a0._618.get(index).set(1);
    a0._622.get(index).set((int)MEMORY.ref(2, a2).offset(0xaL).get());
    a0._64a.get(index).set((short)0);
    a0._65e.get(index).set((short)1);
  }

  @Method(0x800ec8d0L)
  public static void FUN_800ec8d0(final long a0) {
    final long t2 = _800c6958.get();

    //LAB_800ec8ec
    for(int a3 = 0; a3 < 0x40; a3++) {
      //LAB_800ec8f4
      for(int a1 = 0; a1 < 0x10; a1++) {
        MEMORY.ref(2, t2).offset(_800fb148.get(a3).get() * 0x20L).offset(a1 * 0x2L).setu(MEMORY.ref(2, a0).offset(0x14L).offset(a1 * 0x2L).get());
      }
    }

    if(MEMORY.ref(2, a0).offset(0x8812L).offset(0x0L).get() == 0x7422L) {
      _800c695c.setu(MEMORY.ref(2, a0).offset(0x8812L).offset(0x4L).get());
    } else {
      //LAB_800ec954
      _800c695c.setu(0x3fL);
    }

    //LAB_800ec95c
    _800c695c.addu(0x1L);
  }

  @Method(0x800ec974L)
  public static void FUN_800ec974(final BigStruct a0) {
    _1f8003ec.setu(a0.ui_108.get());
    zOffset_1f8003e8.setu(a0.zOffset_a0.get());

    //LAB_800ec9d0
    long s6 = a0.ui_f4.get();
    long s0 = 0x1L;
    for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
      final GsDOBJ2 s2 = a0.ObjTable_0c.top.deref().get(i);

      if((s0 & s6) == 0) {
        final MATRIX sp0x30 = new MATRIX();
        final MATRIX sp0x10 = new MATRIX();
        GsGetLws(s2.coord2_04.deref(), sp0x30, sp0x10);
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

      //LAB_800eca38
      s0 = s0 << 1;
      if((int)s0 == 0) {
        s0 = 0x1L;
        s6 = a0.ui_f8.get();
      }

      //LAB_800eca4c
    }

    //LAB_800eca58
    if(a0.b_cc.get() != 0) {
      FUN_800ec258(a0);
    }

    //LAB_800eca70
  }

  @Method(0x800eca98L)
  public static void FUN_800eca98(final long a0, final int a1) {
    int scriptIndex = 0;
    if(a1 != -1) {
      if(a0 == 0) {
        //LAB_800ecb00
        scriptIndex = _8006e398.charBobjIndices_e40.get(a1).get();
      } else if(a0 == 1) {
        //LAB_800ecb1c
        scriptIndex = _8006e398.bobjIndices_ebc.get(a1).get();
        //LAB_800ecaf0
      } else if(a0 == 2) {
        //LAB_800ecb38
        scriptIndex = _8006e398.bobjIndices_e0c.get(a1).get();
      }

      //LAB_800ecb50
      //LAB_800ecb54
      final BattleObject27c a3 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
      final int textEffect;
      if(a3.hp_08.get() > a3.maxHp_10.get() / 4) {
        textEffect = a3.hp_08.get() > a3.maxHp_10.get() / 2 ? 0 : 1;
      } else {
        textEffect = 2;
      }

      //LAB_800ecb90
      FUN_800eccfc(a3._148, textEffect, scriptIndex, a3);
    } else {
      //LAB_800ecba4
      long count = 0;
      if(a0 == 0) {
        //LAB_800ecbdc
        count = charCount_800c677c.get();
      } else if(a0 == 1) {
        //LAB_800ecbec
        count = _800c6758.get();
        //LAB_800ecbc8
      } else if(a0 == 2) {
        //LAB_800ecbfc
        count = _800c669c.get();
      }

      //LAB_800ecc04
      //LAB_800ecc1c
      for(int i = 0; i < count; i++) {
        if(a0 == 0) {
          //LAB_800ecc50
          scriptIndex = _8006e398.charBobjIndices_e40.get(i).get();
        } else if(a0 == 1) {
          //LAB_800ecc5c
          scriptIndex = _8006e398.bobjIndices_ebc.get(i).get();
          //LAB_800ecc40
        } else if(a0 == 2) {
          //LAB_800ecc68
          scriptIndex = _8006e398.bobjIndices_e78.get(i).get();
        }

        //LAB_800ecc74
        //LAB_800ecc78
        final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).derefAs(ScriptState.classFor(BattleObject27c.class));
        final BattleObject27c data = state.innerStruct_00.deref();

        final int textEffect;
        if(data.hp_08.get() > data.maxHp_10.get() / 4) {
          textEffect = data.hp_08.get() > data.maxHp_10.get() / 2 ? 0 : 1;
        } else {
          textEffect = 2;
        }

        //LAB_800eccac
        if((state.ui_60.get() & 0x4000L) == 0) {
          FUN_800eccfc(data._148, textEffect, scriptIndex, data);
        }

        //LAB_800eccc8
      }
    }

    //LAB_800eccd8
  }

  @Method(0x800eccfcL)
  public static void FUN_800eccfc(final BigStruct a0, final int textEffect, final int scriptIndex, final BattleObject27c data) {
    final int x;
    final int y;
    final int z;
    final long v1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().ui_60.get();
    if((v1 & 0x4L) != 0) {
      // X and Z are swapped
      x = -data._78.getZ() * 100;
      y = -data._78.getY() * 100;
      z = -data._78.getX() * 100;
    } else {
      //LAB_800ecd90
      if((v1 & 0x2L) != 0) {
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
    final DVECTOR screenCoords = perspectiveTransformXyz(a0, (short)x, (short)y, (short)z);
    long addr = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, addr).offset(0x03L).setu(0x4L);
    MEMORY.ref(4, addr).offset(0x04L).setu(0x6680_8080L);
    MEMORY.ref(2, addr).offset(0x08L).setu(screenCoords.getX() - 0x8L);
    MEMORY.ref(2, addr).offset(0x0aL).setu(_800fb188.offset(2, (_800bb0fc.get() & 0x7L) * 0x2L).get() + screenCoords.getY());
    MEMORY.ref(1, addr).offset(0x0cL).setu(0xf0L);
    MEMORY.ref(1, addr).offset(0x0dL).setu(0);
    MEMORY.ref(2, addr).offset(0x10L).setu(0x10L);
    MEMORY.ref(2, addr).offset(0x12L).setu(0x18L);

    if(textEffect == 0) {
      //LAB_800ece80
      MEMORY.ref(2, addr).offset(0x0eL).setu(0x7fadL);
    } else if(textEffect == 1) {
      //LAB_800ece88
      MEMORY.ref(2, addr).offset(0x0eL).setu(0x7fedL);
      //LAB_800ece70
    } else if(textEffect == 2) {
      //LAB_800ece90
      //LAB_800ece94
      MEMORY.ref(2, addr).offset(0x0eL).setu(0x7c2eL);
    }

    //LAB_800ece9c
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x70L, addr);
    linkedListAddress_1f8003d8.addu(0x14L);

    addr = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, addr).offset(0x03L).setu(0x1L);
    MEMORY.ref(4, addr).offset(0x04L).setu(0xe100_021bL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x70L, addr);
    linkedListAddress_1f8003d8.addu(0x8L);
  }

  @Method(0x800ee210L)
  public static long FUN_800ee210(final RunningScript a0) {
    final RECT sp0x10 = new RECT().set((short)a0.params_20.get(4).deref().get(), (short)a0.params_20.get(5).deref().get(), (short)(a0.params_20.get(2).deref().get() >> 2), (short)a0.params_20.get(3).deref().get());
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), sp0x10, a0.params_20.get(0).deref().get() & 0xffffL, a0.params_20.get(1).deref().get() & 0xffffL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);
    return 0;
  }

  @Method(0x800ee2acL)
  public static long FUN_800ee2ac(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._1e8.set((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800ee2e4L)
  public static long FUN_800ee2e4(final RunningScript a0) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final int v0 = a0.params_20.get(1).deref().get();
    bobj._244.set(v0, v0, v0);
    return 0;
  }

  @Method(0x800ee324L)
  public static long FUN_800ee324(final RunningScript a0) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    bobj._244.set(a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800ee384L)
  public static long FUN_800ee384(final RunningScript a0) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    bobj._214.set(2);
    bobj._215.set(-1);
    return 0;
  }

  @Method(0x800ee3c0L)
  public static long FUN_800ee3c0(final RunningScript a0) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    v1._214.set(3);
    v1._215.set(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800ee408L)
  public static long FUN_800ee408(final RunningScript a0) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final int a0_0 = bobj._215.get();
    if(a0_0 == -2) {
      //LAB_800ee450
      bobj._214.set(0);
    } else if(a0_0 == -1) {
      bobj._214.set(2);
    } else {
      //LAB_800ee458
      bobj._214.set(3);
    }

    //LAB_800ee460
    return 0;
  }

  @Method(0x800ee468L)
  public static long FUN_800ee468(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._214.set(0);
    return 0;
  }

  @Method(0x800ee49cL)
  public static long FUN_800ee49c(final RunningScript a0) {
    final BattleObject27c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    a1._254.set(a0.params_20.get(1).deref().get());
    a1._25c.set(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800ee4e8L)
  public static long FUN_800ee4e8(final RunningScript a0) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    bobj._260.set(a0.params_20.get(1).deref().get());
    bobj._264.set(a0.params_20.get(2).deref().get());
    bobj._268.set(a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800ee548L)
  public static long FUN_800ee548(final RunningScript a0) {
    FUN_800ebb58(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800ee574L)
  public static long FUN_800ee574(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)_800bda0c.deref().objtable2_550.nobj.get());
    return 0;
  }

  @Method(0x800ee594L)
  public static long FUN_800ee594(final RunningScript a0) {
    _800bda0c.deref()._5e4.or(1L << a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800ee5c0L)
  public static long FUN_800ee5c0(final RunningScript a0) {
    _800bda0c.deref()._5e4.and(~(1L << a0.params_20.get(0).deref().get()));
    return 0;
  }

  @Method(0x800ee5f0L)
  public static long FUN_800ee5f0(final RunningScript a0) {
    _800bda0c.deref()._5e8.set((short)a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800ee610L)
  public static void FUN_800ee610() {
    _800c6cf4.setu(0);
    _800c6c38.setu(0x1L);
    displayStats_800c6c2c.setPointer(addToLinkedListTail(0x144 * 3));
    _800c6b5c.setu(addToLinkedListTail(0x930L));
    _800c6b60.setPointer(addToLinkedListTail(0xa4L));
    battleMenu_800c6c34.setPointer(addToLinkedListTail(0x58L));
    _800c6b6c.setu(addToLinkedListTail(0x3cL));

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
        currentEnemyNames_800c69d0.get(combatantIndex).charAt(v1, 0xa0ffL);
      }
    }

    //LAB_800ee7b0
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      //LAB_800ee7b8
      for(int v1 = 0; v1 < 22; v1++) {
        _800c6ba8.get(charSlot).charAt(v1, 0xa0ffL);
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
  public static void battleHudTexturesLoadedCallback(final long address, final long fileSize, final long param) {
    final short[] sp0x38 = new short[12];
    for(int i = 0; i < sp0x38.length; i++) {
      sp0x38[i] = (short)_800c6e48.offset(i * 0x2L).get();
    }

    final short[] sp0x50 = new short[6];
    for(int i = 0; i < sp0x50.length; i++) {
      sp0x50[i] = (short)_800c6e60.offset(i * 0x2L).get();
    }

    //LAB_800ee9c0
    for(int s0 = 0; s0 < MEMORY.ref(4, address).offset(0x4L).get(); s0++) {
      if(MEMORY.ref(4, address).offset(s0 * 0x8L).offset(0xcL).get() != 0) {
        final TimHeader sp0x10 = parseTimHeader(MEMORY.ref(4, address + MEMORY.ref(4, address).offset(s0 * 0x8L).offset(0x8L).get() + 0x4L)); //TODO

        if(s0 == 0) {
          final RECT sp0x30 = new RECT((short)704, (short)256, (short)64, (short)256);
          LoadImage(sp0x30, sp0x10.getImageAddress());
        }

        //LAB_800eea20
        final RECT sp0x30 = new RECT();
        if(s0 < 0x4L) {
          sp0x30.x.set((short)(sp0x50[s0] + 704));
          sp0x30.y.set((short)496);
        } else {
          //LAB_800eea3c
          sp0x30.x.set((short)(sp0x50[s0] + 896));
          sp0x30.y.set((short)304);
        }

        //LAB_800eea50
        sp0x30.w.set(sp0x38[s0 * 2    ]);
        sp0x30.h.set(sp0x38[s0 * 2 + 1]);
        LoadImage(sp0x30, sp0x10.getClutAddress());
        DrawSync(0);
        _800c6cf4.addu(0x1L);
      }

      //LAB_800eea8c
    }

    //LAB_800eeaac
    removeFromLinkedList(address);
  }

  @Method(0x800eeaecL)
  public static void FUN_800eeaec() {
    //LAB_800eeb10
    //LAB_800eebb4
    //LAB_800eebd8
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
      final CharacterData2c charData = gameState_800babc8.charData_32c.get(bobj.charIndex_272.get());

      //LAB_800eec10
      charData.hp_08.set(Math.max(1, bobj.hp_08.get()));

      if((gameState_800babc8.dragoonSpirits_19c.get(0).get() & 1L << characterDragoonIndices_800c6e68.get(bobj.charIndex_272.get()).get()) != 0) {
        charData.mp_0a.set(bobj.mp_0c.get());
      }

      //LAB_800eec78
      if(bobj.charIndex_272.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 1L << characterDragoonIndices_800c6e68.get(9).get()) != 0) {
        charData.mp_0a.set(bobj.mp_0c.get());
      }

      //LAB_800eecb8
      charData.status_10.set((int)(bobj.dragoonFlag_0e.get() & 0xc8L));
      charData.sp_0c.set(bobj.sp_0a.get());
    }

    //LAB_800eecf4
    if((gameState_800babc8.scriptFlags2_bc.get(0xd).get() & 0x4_0000L) != 0) { // Used Psych Bomb X this battle
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

    removeFromLinkedList(displayStats_800c6c2c.getPointer());
    removeFromLinkedList(_800c6b5c.get());
    removeFromLinkedList(_800c6b60.getPointer());
    removeFromLinkedList(battleMenu_800c6c34.getPointer());
    removeFromLinkedList(_800c6b6c.get());
  }

  @Method(0x800eee80L)
  public static void loadMonster(final long bobjIndex) {
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

    final BattleObject27c monster = scriptStatePtrArr_800bc1c0.get((int)bobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final LodString name = enemyNames_80112068.get(monster.charIndex_272.get()).deref();

    //LAB_800eef7c
    for(int charIndex = 0; ; charIndex++) {
      currentEnemyNames_800c69d0.get((int)_800c6b9c.get()).charAt(charIndex, name.charAt(charIndex));

      if(name.charAt(charIndex) >= 0xa0ffL) {
        break;
      }
    }

    //LAB_800eefa8
    _800c6b78.offset(_800c6b9c.get() * 0x4L).setu(bobjIndex);
    _800c6b9c.addu(0x1L);

    //LAB_800eefcc
    for(int i = 0; i < 0xa0; i++) {
      monster.all_04.get(i).set((short)0);
    }

    final MonsterStats1c monsterStats = monsterStats_8010ba98.get(monster.charIndex_272.get());
    monster.hp_08.set(monsterStats.hp_00.get());
    monster.mp_0c.set(monsterStats.mp_02.get());
    monster.maxHp_10.set(monsterStats.hp_00.get());
    monster.maxMp_12.set(monsterStats.mp_02.get());
    monster.specialEffectFlag_14.set(monsterStats.specialEffectFlag_0d.get());
    monster._16.set(0);
    monster._18.set(0);
    monster._1a.set(0);
    monster.elementFlag_1c.set((short)monsterStats.elementFlag_0f.get());
    monster._1e.set(monsterStats._0e.get());
    monster.elementalResistanceFlag_20.set(0);
    monster.elementalImmunityFlag_22.set(monsterStats.elementalImmunityFlag_10.get());
    monster.statusResistFlag_24.set(monsterStats.statusResistFlag_11.get());
    monster._26.set(0);
    monster._28.set(0);
    monster._2a.set(0);
    monster._2c.set(0);
    monster._2e.set(0);
    monster._30.set(0);
    monster.speed_32.set((short)monsterStats.speed_08.get());
    monster.attack_34.set(monsterStats.attack_04.get());
    monster.magicAttack_36.set(monsterStats.magicAttack_06.get());
    monster.defence_38.set(monsterStats.defence_09.get());
    monster.magicDefence_3a.set(monsterStats.magicDefence_0a.get());
    monster.attackHit_3c.set((short)0);
    monster.magicHit_3e.set((short)0);
    monster.attackAvoid_40.set((short)monsterStats.attackAvoid_0b.get());
    monster.magicAvoid_42.set((short)monsterStats.magicAvoid_0c.get());
    monster.onHitStatusChance_44.set(0);
    monster._46.set(0);
    monster._48.set(0);
    monster.onHitStatus_4a.set(0);
    monster.selectedAddition_58.set((short)-1);
    monster.originalHp_5c.set(monsterStats.hp_00.get());
    monster.originalMp_5e.set(monsterStats.mp_02.get());
    monster.originalAttack_60.set(monsterStats.attack_04.get());
    monster.originalMagicAttack_62.set(monsterStats.magicAttack_06.get());
    monster.originalSpeed_64.set(monsterStats.speed_08.get());
    monster.originalDefence_66.set(monsterStats.defence_09.get());
    monster.originalMagicDefence_68.set(monsterStats.magicDefence_0a.get());
    monster.originalAttackAvoid_6a.set(monsterStats.attackAvoid_0b.get());
    monster.originalMagicAvoid_6c.set(monsterStats.magicAvoid_0c.get());
    monster.damageReductionFlags_6e.set(monsterStats.specialEffectFlag_0d.get());
    monster._70.set(monsterStats._0e.get());
    monster.monsterElementFlag_72.set(monsterStats.elementFlag_0f.get());
    monster.monsterElementalImmunityFlag_74.set(monsterStats.elementalImmunityFlag_10.get());
    monster.monsterStatusResistFlag_76.set(monsterStats.statusResistFlag_11.get());
    monster._78.set(monsterStats.x_12.get(), monsterStats.y_13.get(), monsterStats.z_14.get());
    monster._7e.set(monsterStats._15.get());
    monster._80.set(monsterStats._16.get());
    monster._82.set(monsterStats._17.get());
    monster._84.set(monsterStats._18.get());
    monster._86.set(monsterStats._19.get());
    monster._88.set(monsterStats._1a.get());
    monster._8a.set(monsterStats._1b.get());

    if((monster.damageReductionFlags_6e.get() & 0x8L) != 0) {
      monster.physicalImmunity_110.set(1);
    }

    //LAB_800ef25c
    if((monster.damageReductionFlags_6e.get() & 0x4L) != 0) {
      monster.magicalImmunity_112.set(1);
    }

    //LAB_800ef274
    FUN_80012bb4();
  }

  @Method(0x800ef28cL)
  public static void FUN_800ef28c(final long a0) {
    //LAB_800ef2c4
    //TODO sp0x18 is unused, why?
    //memcpy(sp0x18, _800c6e68.getAddress(), 0x28);

    FUN_80110030(0x1L);

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
      final BattleObject27c s0 = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
      final byte[] spellIndices = new byte[8];
      getUnlockedDragoonSpells(spellIndices, s0.charIndex_272.get());
      dragoonSpells_800c6960.get(charSlot).charIndex_00.set(s0.charIndex_272.get());

      //LAB_800ef3d8
      for(int spellIndex = 0; spellIndex < 8; spellIndex++) {
        dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).set(spellIndices[spellIndex]);
      }

      //LAB_800ef400
      for(int i = 0; i < 0xa0; i++) {
        s0.all_04.get(i).set((short)0);
      }

      final ActiveStatsa0 stats = stats_800be5f8.get(s0.charIndex_272.get());
      s0.level_04.set(stats.level_0e.get());
      s0.dlevel_06.set(stats.dlevel_0f.get());
      s0.hp_08.set(stats.hp_04.get());
      s0.sp_0a.set((short)stats.sp_08.get());
      s0.mp_0c.set(stats.mp_06.get());
      s0.dragoonFlag_0e.set(stats.dragoonFlag_0c.get());
      s0.maxHp_10.set(stats.maxHp_66.get());
      s0.maxMp_12.set(stats.maxMp_6e.get());
      s0.specialEffectFlag_14.set(stats.specialEffectFlag_76.get());
      s0._16.set(stats._77.get());
      s0._18.set(stats._78.get());
      s0._1a.set(stats._79.get());
      s0.elementFlag_1c.set((short)stats.elementFlag_7a.get());
      s0._1e.set(stats._7b.get());
      s0.elementalResistanceFlag_20.set(stats.elementalResistanceFlag_7c.get());
      s0.elementalImmunityFlag_22.set(stats.elementalImmunityFlag_7d.get());
      s0.statusResistFlag_24.set(stats.statusResistFlag_7e.get());
      s0._26.set(stats._7f.get());
      s0._28.set(stats._80.get());
      s0._2a.set(stats._81.get());
      s0._2c.set(stats._82.get());
      s0._2e.set(stats._83.get());
      s0._30.set(stats._84.get());
      s0.speed_32.set((short)(stats.gearSpeed_86.get() + stats.bodySpeed_69.get()));
      s0.attack_34.set(stats.gearAttack_88.get() + stats.bodyAttack_6a.get());
      s0.magicAttack_36.set(stats.gearMagicAttack_8a.get() + stats.bodyMagicAttack_6b.get());
      s0.defence_38.set(stats.gearDefence_8c.get() + stats.bodyDefence_6c.get());
      s0.magicDefence_3a.set(stats.gearMagicDefence_8e.get() + stats.bodyMagicDefence_6d.get());
      s0.attackHit_3c.set(stats.attackHit_90.get());
      s0.magicHit_3e.set(stats.magicHit_92.get());
      s0.attackAvoid_40.set(stats.attackAvoid_94.get());
      s0.magicAvoid_42.set(stats.magicAvoid_96.get());
      s0.onHitStatusChance_44.set(stats.onHitStatusChance_98.get());
      s0._46.set(stats._99.get());
      s0._48.set(stats._9a.get());
      s0.onHitStatus_4a.set(stats.onHitStatus_9b.get());
      s0.spellId_4e.set((short)stats.onHitStatus_9b.get());
      s0.selectedAddition_58.set(stats.selectedAddition_35.get());
      s0.dragoonAttack_ac.set(stats.dragoonAttack_72.get());
      s0.dragoonMagic_ae.set(stats.dragoonMagicAttack_73.get());
      s0.dragoonDefence_b0.set(stats.dragoonDefence_74.get());
      s0.dragoonMagicDefence_b2.set(stats.dragoonMagicDefence_75.get());
      s0.physicalImmunity_110.set(stats.physicalImmunity_46.get());
      s0.magicalImmunity_112.set(stats.magicalImmunity_48.get());
      s0.physicalResistance_114.set(stats.physicalResistance_4a.get());
      s0.magicalResistance_116.set(stats.magicalResistance_60.get());
      s0._118.set(stats._9c.get());
      s0.additionSpMultiplier_11a.set((short)stats.additionSpMultiplier_9e.get());
      s0.additionDamageMultiplier_11c.set((short)stats.additionDamageMultiplier_9f.get());
      s0.equipment0_11e.set(stats.equipment_30.get(0).get());
      s0.equipment1_120.set(stats.equipment_30.get(1).get());
      s0.equipment2_122.set(stats.equipment_30.get(2).get());
      s0.equipment3_124.set(stats.equipment_30.get(3).get());
      s0.equipment4_126.set(stats.equipment_30.get(4).get());
      s0.spMultiplier_128.set(stats.spMultiplier_4c.get());
      s0.spPerPhysicalHit_12a.set(stats.spPerPhysicalHit_4e.get());
      s0.mpPerPhysicalHit_12c.set(stats.mpPerPhysicalHit_50.get());
      s0.itemSpPerMagicalHit_12e.set(stats.spPerMagicalHit_52.get());
      s0.mpPerMagicalHit_130.set(stats.mpPerMagicalHit_54.get());
      s0.hpRegen_132.set(stats.hpRegen_56.get());
      s0.mpRegen_134.set(stats.mpRegen_58.get());
      s0.spRegen_136.set(stats.spRegen_5a.get());
      s0.revive_138.set(stats._5c.get());
      s0._13a.set(stats._5e.get());
      s0._13c.set(stats._62.get());
      s0._13e.set(stats._64.get());
      s0._142.set(stats.onHitStatus_9b.get());
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
      v1._08.set((short)0);
      v1._0a.set((short)0);
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

    long a0 = _800c6b5c.get();

    //LAB_800ef878
    for(int i = 0; i < 12; i++) {
      MEMORY.ref(4, a0).offset(0x18L).setu(-0x1L);
      MEMORY.ref(4, a0).offset(0x14L).setu(-0x1L);
      MEMORY.ref(2, a0).offset(0x0L).setu(0);
      MEMORY.ref(2, a0).offset(0x2L).setu(0);
      MEMORY.ref(4, a0).offset(0x4L).setu(-0x1L);
      MEMORY.ref(4, a0).offset(0x8L).setu(0);
      MEMORY.ref(4, a0).offset(0xcL).setu(0x80_8080L);

      //LAB_800ef89c
      long v1 = a0;
      for(int a1 = 0; a1 < 5; a1++) {
        MEMORY.ref(4, v1).offset(0x24L).setu(0);
        MEMORY.ref(4, v1).offset(0x28L).setu(0);
        MEMORY.ref(4, v1).offset(0x2cL).setu(0);
        MEMORY.ref(2, v1).offset(0x30L).setu(-0x1L);
        MEMORY.ref(2, v1).offset(0x40L).setu(0);
        v1 = v1 + 0x20L;
      }

      a0 = a0 + 0xc4L;
    }
  }

  @Method(0x800ef8d8L)
  public static void FUN_800ef8d8(final int charSlot) {
    final BattleStruct3c a0_0 = _800c6c40.get(charSlot);
    a0_0.charIndex_00.set((short)charSlot);
    a0_0._02.set(scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get());
    a0_0._04.set((short)0);
    a0_0.flags_06.or(0x2);
    a0_0._08.set((short)(charSlot * 94 + 63));
    a0_0._0a.set((short)38);
    a0_0._10.set((short)32);
    a0_0._12.set((short)17);

    //LAB_800ef980
    for(int i = 0; i < 10; i++) {
      a0_0._14.get(i).set(0);
    }

    final BattleDisplayStats144 displayStats = displayStats_800c6c2c.deref().get(charSlot);
    displayStats.x_00.set(a0_0._08.get());
    displayStats.y_02.set(a0_0._0a.get());
  }

  @Method(0x800ef9e4L)
  public static void FUN_800ef9e4() {
    if(_800c6cf4.get() == 0x6L) {
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
          final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

          final int textEffect;
          if(bobj.hp_08.get() > bobj.maxHp_10.get() / 2) {
            textEffect = 1;
          } else if(bobj.hp_08.get() > bobj.maxHp_10.get() / 4) {
            textEffect = 2;
          } else {
            textEffect = 3;
          }

          //LAB_800efb30
          //LAB_800efb40
          //LAB_800efb54
          FUN_800f1550(charSlot, 0, bobj.hp_08.get(), textEffect);
          FUN_800f1550(charSlot, 1, bobj.maxHp_10.get(), 1);
          FUN_800f1550(charSlot, 2, bobj.mp_0c.get(), 1);
          FUN_800f1550(charSlot, 3, bobj.maxMp_12.get(), 1);
          FUN_800f1550(charSlot, 4, bobj.sp_0a.get() / 100, 1);

          s2._14.get(1).set(_800bb0fc.get() & 0x3L);

          //LAB_800efc0c
          if(bobj.sp_0a.get() < bobj.dlevel_06.get() * 100) {
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
        a1._0a.set((short)MEMORY.ref(2, v1).offset(0x0L).get());
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
    long v0;
    long v1;
    long a0;
    long a2;
    long t0;
    long t5;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long spec;
    long spf0;
    long spfc;
    spf0 = 0;

    final Memory.TemporaryReservation sp0x38tmp = MEMORY.temp(0x30);
    final Value sp0x38 = sp0x38tmp.get();
    memcpy(sp0x38.getAddress(), _800c6e9c.getAddress(), 0x30);

    //LAB_800efe04
    final Memory.TemporaryReservation sp0x78tmp = MEMORY.temp(0x24);
    final Value sp0x78 = sp0x78tmp.get();
    memcpy(sp0x78.getAddress(), _800c6ecc.getAddress(), 0x24);

    //LAB_800efe9c
    final Memory.TemporaryReservation sp0xa0tmp = MEMORY.temp(0x14);
    final Value sp0xa0 = sp0xa0tmp.get();
    memcpy(sp0xa0.getAddress(), _800c6ef0.getAddress(), 0x14);

    //LAB_800eff1c
    //LAB_800eff70
    final Memory.TemporaryReservation sp0xb8tmp = MEMORY.temp(0x2a);
    final Value sp0xb8 = sp0xb8tmp.get();
    memcpy(sp0xb8.getAddress(), _800c6f04.getAddress(), 0x2a);

    //LAB_800effa0
    if((int)_800c6cf4.get() >= 0x6L) {
      //LAB_800f0000
      spfc = 0;

      //LAB_800f0074
      for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
        final BattleDisplayStats144 displayStats = displayStats_800c6c2c.deref().get(charSlot);
        final BattleStruct3c s7 = _800c6c40.get(charSlot);

        if(s7.charIndex_00.get() != -1 && (s7.flags_06.get() & 0x1) != 0 && (s7.flags_06.get() & 0x2) != 0) {
          a2 = _8006e398.charBobjIndices_e40.get(charSlot).get();
          final BattleObject27c data = scriptStatePtrArr_800bc1c0.get((int)a2).deref().innerStruct_00.derefAs(BattleObject27c.class);
          if((scriptStatePtrArr_800bc1c0.get((int)_800c66c8.get()).deref().ui_60.get() & 0x4L) != 0x1L && _800c66c8.get() == a2) {
            spec = 0x2L;
            s5 = 0x2L;
          } else {
            spec = 0;
            s5 = 0x1L;
          }

          //LAB_800f0108
          if((data.dragoonFlag_0e.get() & 0x2000L) == 0) {
            s2 = 0x4L;
          } else {
            s2 = 0x5L;
          }

          //LAB_800f0120
          //LAB_800f0128
          for(s0 = 0; s0 < s2; s0++) {
            //LAB_800f0134
            for(s1 = 0; s1 < 4; s1++) {
              final BattleDisplayStats144Sub10 struct = displayStats._04.get((int)s0).get((int)s1);
              if(struct._00.get() == -1) {
                break;
              }

              //TODO Need to verify these methods for font colour
              FUN_800f8dfc(displayStats.x_00.get() + struct.x_02.get() - centreScreenX_1f8003dc.get(), displayStats.y_02.get() + struct.y_04.get() - centreScreenY_1f8003de.get(), struct.u_06.get(), struct.v_08.get(), struct.w_0a.get(), struct.h_0c.get(), struct._0e.get(), spec, s7._14.get(2).get());
            }

            //LAB_800f01e0
          }

          //LAB_800f01f0
          long a1;
          s0 = _800fb444.offset(data.charIndex_272.get() * 0x4L).get();

          // Names
          FUN_800f8dfc(displayStats.x_00.get() - centreScreenX_1f8003dc.get() + 1, displayStats.y_02.get() - centreScreenY_1f8003de.get() - 25, MEMORY.ref(1, s0).offset(0x0L).get(), MEMORY.ref(1, s0).offset(0x1L).get(), MEMORY.ref(1, s0).offset(0x2L).get(), MEMORY.ref(1, s0).offset(0x3L).get(), 0x2cL, spec, s7._14.get(2).get());

          // Portraits
          FUN_800f8dfc(displayStats.x_00.get() - centreScreenX_1f8003dc.get() - 44, displayStats.y_02.get() - centreScreenY_1f8003de.get() - 22, MEMORY.ref(1, s0).offset(0x4L).get(), MEMORY.ref(1, s0).offset(0x5L).get(), MEMORY.ref(1, s0).offset(0x6L).get(), MEMORY.ref(1, s0).offset(0x7L).get(), MEMORY.ref(1, s0).offset(0x8L).get(), s5, s7._14.get(2).get());

          if(spec != 0) {
            final long v1_0 = (6 - s7._14.get(2).get()) * 8 + 100;
            a0 = displayStats.x_00.get() - centreScreenX_1f8003dc.get() + MEMORY.ref(1, s0).offset(0x6L).get() / 2 - 44;
            v1 = (MEMORY.ref(1, s0).offset(0x6L).get() + 2) * v1_0 / 100 / 2;
            v0 = a0 - v1;
            a0 = a0 + v1 - 0x1L;

            final Memory.TemporaryReservation sp0x28tmp = MEMORY.temp(0x8);
            final Value sp0x28 = sp0x28tmp.get();
            sp0x28.offset(2, 0x0L).setu(v0);
            sp0x28.offset(2, 0x2L).setu(a0);
            sp0x28.offset(2, 0x4L).setu(v0);
            sp0x28.offset(2, 0x6L).setu(a0);
            a1 = displayStats.y_02.get() - centreScreenY_1f8003de.get() + MEMORY.ref(1, s0).offset(0x7L).get() / 2 - 22;
            v1 = (MEMORY.ref(1, s0).offset(0x7L).get() + 2) * v1_0 / 100 / 2;
            v0 = a1 - v1;
            a1 = a1 + v1 - 0x1L;
            final Memory.TemporaryReservation sp0x30tmp = MEMORY.temp(0x8);
            final Value sp0x30 = sp0x30tmp.get();
            sp0x30.offset(2, 0x0L).setu(v0);
            sp0x30.offset(2, 0x2L).setu(v0);
            sp0x30.offset(2, 0x4L).setu(a1);
            sp0x30.offset(2, 0x6L).setu(a1);

            //LAB_800f0438
            for(s2 = 0; s2 < 8; s2++) {
              v1 = s7._14.get(2).get();

              final int r;
              final int g;
              final int b;
              if(v1 < 6) {
                r = (int)(v1 * 0x2a);
                g = r;
                b = r;
                s1 = 1;
              } else {
                r = 0xff;
                g = 0xff;
                b = 0xff;
                s1 = 0;
              }

              //LAB_800f0470
              //LAB_800f047c
              t5 = s2 / 4;
              t0 = sp0x38.offset(s2 % 4 * 0xcL).getAddress();

              // Draw border around currently active character's portrait

              drawLine(
                sp0x28.offset(2, MEMORY.ref(1, t0).offset(0x0L).getSigned() * 2).getSigned() + MEMORY.ref(1, t0).offset(0x4L).getSigned() + MEMORY.ref(1, t0).offset(0x8L).getSigned() * t5,
                sp0x30.offset(2, MEMORY.ref(1, t0).offset(0x1L).getSigned() * 2).getSigned() + MEMORY.ref(1, t0).offset(0x5L).getSigned() + MEMORY.ref(1, t0).offset(0x9L).getSigned() * t5,
                sp0x28.offset(2, MEMORY.ref(1, t0).offset(0x2L).getSigned() * 2).getSigned() + MEMORY.ref(1, t0).offset(0x6L).getSigned() + MEMORY.ref(1, t0).offset(0xaL).getSigned() * t5,
                sp0x30.offset(2, MEMORY.ref(1, t0).offset(0x3L).getSigned() * 2).getSigned() + MEMORY.ref(1, t0).offset(0x7L).getSigned() + MEMORY.ref(1, t0).offset(0xbL).getSigned() * t5,
                r,
                g,
                b,
                s1 != 0
              );
            }

            sp0x28tmp.release();
            sp0x30tmp.release();
          }

          //LAB_800f05d4
          s3 = 0;
          s0 = 0;
          s1 = (data.dragoonFlag_0e.get() & 0x2000L) > 0 ? 1 : 0;

          //LAB_800f05f4
          for(int i = 0; i < 3; i++) {
            if(i == 2 && s1 == 0) {
              s3 = -0xaL;
            }

            //LAB_800f060c
            v1 = sp0x78.offset(s0).getAddress();

            // HP: MP: SP:

            //LAB_800f0610
            FUN_800f8dfc(
              MEMORY.ref(2, v1).offset(0x0L).get() + displayStats.x_00.get() - centreScreenX_1f8003dc.get(),
              MEMORY.ref(2, v1).offset(0x2L).get() + displayStats.y_02.get() - centreScreenY_1f8003de.get(),
              MEMORY.ref(1, v1).offset(0x4L).get(),
              MEMORY.ref(1, v1).offset(0x6L).get(),
              MEMORY.ref(2, v1).offset(0x8L).getSigned(),
              MEMORY.ref(2, v1).offset(0xaL).get() + s3,
              0x2cL,
              spec,
              s7._14.get(2).get()
            );

            s0 = s0 + 0xcL;
          }

          if(s1 != 0) {
            a0 = data.sp_0a.get();
            s5 = a0 / 100;
            s2 = a0 % 100;

            // SP bars

            //LAB_800f0714
            for(s3 = 0; s3 < 2; s3++) {
              if(s3 == 0) {
                s1 = s2;
                spf0 = s5 + 0x1L;
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
              s0 = linkedListAddress_1f8003d8.get();
              setGp0_38(s0);
              gpuLinkedListSetCommandTransparency(s0, false);
              linkedListAddress_1f8003d8.addu(0x24L);

              v0 = displayStats.x_00.get() - centreScreenX_1f8003dc.get() + 3;
              MEMORY.ref(2, s0).offset(0x18L).setu(v0);
              MEMORY.ref(2, s0).offset(0x08L).setu(v0);
              v0 = displayStats.x_00.get() - centreScreenX_1f8003dc.get() + s1 + 3;
              MEMORY.ref(2, s0).offset(0x20L).setu(v0);
              MEMORY.ref(2, s0).offset(0x10L).setu(v0);
              v0 = displayStats.y_02.get() - centreScreenY_1f8003de.get() + 8;
              MEMORY.ref(2, s0).offset(0x12L).setu(v0);
              MEMORY.ref(2, s0).offset(0x0aL).setu(v0);
              v0 = displayStats.y_02.get() - centreScreenY_1f8003de.get() + 11;
              MEMORY.ref(2, s0).offset(0x22L).setu(v0);
              MEMORY.ref(2, s0).offset(0x1aL).setu(v0);

              v0 = sp0xb8.offset(spf0 * 0x6L).getAddress();
              MEMORY.ref(1, s0).offset(0x4L).setu(MEMORY.ref(1, v0).offset(0x0L).get());
              MEMORY.ref(1, s0).offset(0x5L).setu(MEMORY.ref(1, v0).offset(0x1L).get());
              MEMORY.ref(1, s0).offset(0x6L).setu(MEMORY.ref(1, v0).offset(0x2L).get());
              MEMORY.ref(1, s0).offset(0xcL).setu(MEMORY.ref(1, v0).offset(0x0L).get());
              MEMORY.ref(1, s0).offset(0xdL).setu(MEMORY.ref(1, v0).offset(0x1L).get());
              MEMORY.ref(1, s0).offset(0xeL).setu(MEMORY.ref(1, v0).offset(0x2L).get());
              v0 = sp0xb8.offset(spf0 * 0x6L + 0x3L).getAddress();
              MEMORY.ref(1, s0).offset(0x14L).setu(MEMORY.ref(1, v0).offset(0x0L).get());
              MEMORY.ref(1, s0).offset(0x15L).setu(MEMORY.ref(1, v0).offset(0x1L).get());
              MEMORY.ref(1, s0).offset(0x16L).setu(MEMORY.ref(1, v0).offset(0x2L).get());
              MEMORY.ref(1, s0).offset(0x1cL).setu(MEMORY.ref(1, v0).offset(0x0L).get());
              MEMORY.ref(1, s0).offset(0x1dL).setu(MEMORY.ref(1, v0).offset(0x1L).get());
              MEMORY.ref(1, s0).offset(0x1eL).setu(MEMORY.ref(1, v0).offset(0x2L).get());
              insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);
            }

            //LAB_800f0910
            for(int i = 0; i < 4; i++) {
              final long offsetX = displayStats.x_00.get() - centreScreenX_1f8003dc.get();
              final long offsetY = displayStats.y_02.get() - centreScreenY_1f8003de.get();
              drawLine(_800fb46c.get(i * 4).get() + offsetX, _800fb46c.get(i * 4 + 1).get() + offsetY, _800fb46c.get(i * 4 + 2).get() + offsetX, _800fb46c.get(i * 4 + 3).get() + offsetY, 0x60L, 0x60L, 0x60L, false);
            }

            if((s7.flags_06.get() & 0x8) != 0) {
              //LAB_800f09ec
              for(int i = 0; i < 4; i++) {
                final long offsetX = displayStats.x_00.get() - centreScreenX_1f8003dc.get();
                final long offsetY = displayStats.y_02.get() - centreScreenY_1f8003de.get();
                drawLine(_800fb47c.get(i * 4).get() + offsetX, _800fb47c.get(i * 4 + 1).get() + offsetY, _800fb47c.get(i * 4 + 2).get() + offsetX, _800fb47c.get(i * 4 + 3).get() + offsetY, 0x80L, 0, 0, false);
              }
            }
          }
        }

        //LAB_800f0aa8
        spfc = spfc + 0x4L;
      }

      //LAB_800f0ad4
      // Background
      if(_800c6c40.get(0).charIndex_00.get() != -1 && (_800c6c40.get(0).flags_06.get() & 0x1) != 0) {
        FUN_800f1268(0x10L, _800fb198.offset(2, _800c6c38.get() * 0x2L).get() - 0x1aL, 0x120L, 0x28L, 0x8L);
      }

      //LAB_800f0b3c
      FUN_800f3dbc();

      // Use item menu
      FUN_800f5c94();

      // Targeting
      final BattleMenuStruct58 menu = battleMenu_800c6c34.deref();
      if(menu._4c.get() != 0) {
        FUN_800eca98(menu._50.get(), menu.combatantIndex.get());
        final int a1_0 = menu.combatantIndex.get();
        LodString str;
        if(a1_0 == -1) {
          str = targeting_800fb36c.get((int)menu._50.get()).deref();
          spf0 = 0x3L;
          textLen(str);
        } else {
          final BattleObject27c data;

          //LAB_800f0bb0
          if(menu._50.get() == 0x1L) {
            //LAB_800f0ca4
            data = scriptStatePtrArr_800bc1c0.get(_8006e398.bobjIndices_ebc.get(a1_0).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

            //LAB_800f0cf0
            for(s4 = 0; s4 < _800c6768.get(); s4++) {
              if(_800c6b78.offset(s4 * 0x4L).get() == menu._48.get()) {
                break;
              }
            }

            //LAB_800f0d10
            str = FUN_800f8568(data, currentEnemyNames_800c69d0.get((int)s4));
            textLen(str);
            spf0 = FUN_800f8ca0(data.elementFlag_1c.get());
          } else if(menu._50.get() == 0) {
            data = scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(a1_0).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
            str = playerNames_800fb378.get(data.charIndex_272.get()).deref();
            textLen(str);

            if(data.charIndex_272.get() != 0) {
              spf0 = sp0xa0.offset(2, data.charIndex_272.get() * 0x2L).get();
            } else {
              if((gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0) {
                if((scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(menu.combatantIndex.get()).get()).deref().ui_60.get() & 0x2L) != 0) {
                  spf0 = sp0xa0.offset(0x12L).get();
                }
              }
            }
          } else {
            //LAB_800f0d58
            //LAB_800f0d5c
            final int bobjIndex = _8006e398.bobjIndices_e0c.get(menu.combatantIndex.get()).get();
            data = scriptStatePtrArr_800bc1c0.get(bobjIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
            if((scriptStatePtrArr_800bc1c0.get(bobjIndex).deref().ui_60.get() & 0x4L) == 0) {
              str = playerNames_800fb378.get(data.charIndex_272.get()).deref();
              spf0 = sp0xa0.offset(2, data.charIndex_272.get() * 0x2L).get();

              if(data.charIndex_272.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0 && (scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(menu.combatantIndex.get()).get()).deref().ui_60.get() & 0x2L) != 0) {
                spf0 = sp0xa0.offset(0x12L).get();
              }
            } else {
              //LAB_800f0e24
              str = FUN_800f8568(data, currentEnemyNames_800c69d0.get(menu.combatantIndex.get()));
              spf0 = FUN_800f8ca0(data.elementFlag_1c.get());
            }

            //LAB_800f0e58
            textLen(str);
          }

          //LAB_800f0e60
          a0 = data.dragoonFlag_0e.get();

          if((a0 & 0xffL) != 0) {
            if((_800bb0fc.get() & 0x10L) != 0) {
              s0 = 0x80L;

              //LAB_800f0e94
              for(s4 = 0; s4 < 8; s4++) {
                if((a0 & s0) != 0) {
                  break;
                }

                s0 = (int)s0 >> 1;
              }

              //LAB_800f0eb4
              if(s4 == 0x8L) {
                s4 = 0x7L;
              }

              //LAB_800f0ec0
              str = ailments_800fb3a0.get((int)s4).deref();

              //LAB_800f0ed0
              textLen(str); //TODO why?
            }
          }
        }

        //LAB_800f0ed8
        FUN_800f1268(0x2cL, 0x17L, 0xe8L, 0xeL, (short)spf0);
        renderText(str, 160 - textWidth(str) / 2, 24, 0, 0);
      }
    }

    sp0x38tmp.release();
    sp0x78tmp.release();
    sp0xa0tmp.release();
    sp0xb8tmp.release();

    //LAB_800f0f2c
  }
}
