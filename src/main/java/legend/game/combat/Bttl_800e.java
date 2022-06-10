package legend.game.combat;

import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.combat.types.BattleRenderStruct;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStruct1a8;
import legend.game.combat.types.BattleStruct24;
import legend.game.combat.types.BtldScriptData27c;
import legend.game.combat.types.BttlScriptData6c;
import legend.game.combat.types.BttlScriptData6cInner;
import legend.game.combat.types.BttlScriptData6cSub0c;
import legend.game.combat.types.BttlScriptData6cSub1c;
import legend.game.combat.types.BttlScriptData6cSub34;
import legend.game.combat.types.BttlScriptData6cSubBase1;
import legend.game.combat.types.BttlScriptData6cSubBase2;
import legend.game.combat.types.BttlStruct50;
import legend.game.types.ActiveStatsa0;
import legend.game.types.BigStruct;
import legend.game.types.DR_MOVE;
import legend.game.types.ExtendedTmd;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsOT_TAG;
import legend.game.types.LodString;
import legend.game.types.MrgFile;
import legend.game.types.RotateTranslateStruct;
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
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_80015d38;
import static legend.game.Scus94491BpeSegment.FUN_8001d068;
import static legend.game.Scus94491BpeSegment._1f8003e8;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment._1f8003ee;
import static legend.game.Scus94491BpeSegment._1f8003f8;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback08;
import static legend.game.Scus94491BpeSegment.setCallback0c;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_800214bc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021b08;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021ca0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022928;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023264;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023a88;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a55c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a59c;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.strcpy;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b5b0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003ef50;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f210;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f900;
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
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040780;
import static legend.game.Scus94491BpeSegment_8005.orderingTables_8005a370;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bc950;
import static legend.game.Scus94491BpeSegment_800b._800bda0c;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.bigStruct_800bda10;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.scriptState_800bc0c0;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;
import static legend.game.combat.Bttl_800c.FUN_800c9708;
import static legend.game.combat.Bttl_800c.FUN_800ca418;
import static legend.game.combat.Bttl_800c._800c669c;
import static legend.game.combat.Bttl_800c._800c66c8;
import static legend.game.combat.Bttl_800c._800c6758;
import static legend.game.combat.Bttl_800c._800c6768;
import static legend.game.combat.Bttl_800c._800c677c;
import static legend.game.combat.Bttl_800c._800c6920;
import static legend.game.combat.Bttl_800c._800c6928;
import static legend.game.combat.Bttl_800c._800c692c;
import static legend.game.combat.Bttl_800c._800c6930;
import static legend.game.combat.Bttl_800c._800c6938;
import static legend.game.combat.Bttl_800c._800c693c;
import static legend.game.combat.Bttl_800c._800c6940;
import static legend.game.combat.Bttl_800c._800c6944;
import static legend.game.combat.Bttl_800c._800c6948;
import static legend.game.combat.Bttl_800c._800c6950;
import static legend.game.combat.Bttl_800c._800c6958;
import static legend.game.combat.Bttl_800c._800c695c;
import static legend.game.combat.Bttl_800c._800c6960;
import static legend.game.combat.Bttl_800c._800c697e;
import static legend.game.combat.Bttl_800c._800c6980;
import static legend.game.combat.Bttl_800c._800c69c8;
import static legend.game.combat.Bttl_800c._800c69d0;
import static legend.game.combat.Bttl_800c._800c6b5c;
import static legend.game.combat.Bttl_800c._800c6b60;
import static legend.game.combat.Bttl_800c._800c6b64;
import static legend.game.combat.Bttl_800c._800c6b68;
import static legend.game.combat.Bttl_800c._800c6b6c;
import static legend.game.combat.Bttl_800c._800c6b78;
import static legend.game.combat.Bttl_800c._800c6b9c;
import static legend.game.combat.Bttl_800c._800c6ba8;
import static legend.game.combat.Bttl_800c._800c6c2c;
import static legend.game.combat.Bttl_800c._800c6c34;
import static legend.game.combat.Bttl_800c._800c6c38;
import static legend.game.combat.Bttl_800c._800c6c3c;
import static legend.game.combat.Bttl_800c._800c6c40;
import static legend.game.combat.Bttl_800c._800c6cf4;
import static legend.game.combat.Bttl_800c._800c6e18;
import static legend.game.combat.Bttl_800c._800c6e34;
import static legend.game.combat.Bttl_800c._800c6e48;
import static legend.game.combat.Bttl_800c._800c6e60;
import static legend.game.combat.Bttl_800c._800c6e9c;
import static legend.game.combat.Bttl_800c._800c6ecc;
import static legend.game.combat.Bttl_800c._800c6ef0;
import static legend.game.combat.Bttl_800c._800c6f04;
import static legend.game.combat.Bttl_800c._800fadbc;
import static legend.game.combat.Bttl_800c._800faec4;
import static legend.game.combat.Bttl_800c._800fafe8;
import static legend.game.combat.Bttl_800c._800fb148;
import static legend.game.combat.Bttl_800c._800fb188;
import static legend.game.combat.Bttl_800c._800fb198;
import static legend.game.combat.Bttl_800c._800fb36c;
import static legend.game.combat.Bttl_800c._800fb378;
import static legend.game.combat.Bttl_800c._800fb3a0;
import static legend.game.combat.Bttl_800c._800fb444;
import static legend.game.combat.Bttl_800c._800fb46c;
import static legend.game.combat.Bttl_800c._800fb47c;
import static legend.game.combat.Bttl_800c.battleStruct1a8Count_800c66a0;
import static legend.game.combat.Bttl_800c.getBattleStruct1a8;
import static legend.game.combat.Bttl_800c.light_800c6ddc;
import static legend.game.combat.Bttl_800c.script_800faebc;
import static legend.game.combat.Bttl_800d.FUN_800de76c;
import static legend.game.combat.Bttl_800d.FUN_800de840;
import static legend.game.combat.Bttl_800d.ScaleVectorL_SVEC;
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
import static legend.game.combat.Bttl_800f.FUN_800f9ee8;
import static legend.game.combat.SEffe.FUN_80115cac;

public final class Bttl_800e {
  private Bttl_800e() { }

  @Method(0x800e16a0L)
  public static long FUN_800e16a0(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long s3;
    long s5;
    long s6 = a1;
    long fp = a2;
    long s7 = a3;
    a3 = a0 + 0x4L;
    long s1 = linkedListAddress_1f8003d8.get();
    long sp3c = 0xe100_0200L | _1f8003ec.getSigned();
    long t0 = _1f8003ee.get() & 0xffL;

    final UnboundedArrayRef<GsOT_TAG> sp38 = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();

    //LAB_800e1748
    final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0x20);
    final Value sp0x10 = sp0x10tmp.get();
    while(s7 != 0) {
      v0 = FUN_800de840(sp0x10.getAddress(), a3, 0x20L);
      a3 = v0;
      a1 = MEMORY.ref(4, s6).offset(sp0x10.offset(2, 0x12L).get() * 0x4L).get();
      v0 = a1 >>> 24;
      a2 = MEMORY.ref(4, s6).offset(v0 * 0x4L).get();
      v1 = (short)a1 << 8;
      v0 = a2 << 8;
      a0 = (byte)a1 + v0;
      a0 = a0 & 0xffffL;
      v0 = (a2 & 0xff00L) << 16;
      v1 = v1 + v0;
      v1 = v1 & 0xffff_0000L;
      a0 = a0 | v1;
      v1 = a1 << 8;
      v1 = (int)v1 >> 24;
      v0 = a2 & 0xff_0000L;
      v0 = (int)v0 >> 8;
      v1 = v1 + v0;
      CPU.MTC2(a0, 0);
      CPU.MTC2(v1, 1);
      v0 = sp0x10.offset(2, 0x16L).get();
      v0 = v0 << 2;
      v0 = v0 + s6;
      a1 = MEMORY.ref(4, v0).offset(0x0L).get();
      v0 = a1 >>> 24;
      v0 = v0 << 2;
      v0 = v0 + s6;
      a0 = a1 << 24;
      a0 = (int)a0 >> 24;
      v1 = a1 << 16;
      a2 = MEMORY.ref(4, v0).offset(0x0L).get();
      v1 = (int)v1 >> 8;
      v0 = a2 << 8;
      a0 = a0 + v0;
      a0 = a0 & 0xffffL;
      v0 = a2 & 0xff00L;
      v0 = v0 << 16;
      v1 = v1 + v0;
      v1 = v1 & 0xffff_0000L;
      a0 = a0 | v1;
      v1 = a1 << 8;
      v1 = (int)v1 >> 24;
      v0 = a2 & 0xff_0000L;
      v0 = (int)v0 >> 8;
      v1 = v1 + v0;
      CPU.MTC2(a0, 2);
      CPU.MTC2(v1, 3);
      v0 = sp0x10.offset(2, 0x1aL).get();
      v0 = v0 << 2;
      v0 = v0 + s6;
      a1 = MEMORY.ref(4, v0).offset(0x0L).get();
      v0 = a1 >>> 24;
      v0 = v0 << 2;
      v0 = v0 + s6;
      a0 = a1 << 24;
      a0 = (int)a0 >> 24;
      v1 = a1 << 16;
      a2 = MEMORY.ref(4, v0).offset(0x0L).get();
      v1 = (int)v1 >> 8;
      v0 = a2 << 8;
      a0 = a0 + v0;
      a0 = a0 & 0xffffL;
      v0 = a2 & 0xff00L;
      v0 = v0 << 16;
      v1 = v1 + v0;
      v1 = v1 & 0xffff_0000L;
      a0 = a0 | v1;
      v1 = a1 << 8;
      v1 = (int)v1 >> 24;
      v0 = a2 & 0xff_0000L;
      v0 = (int)v0 >> 8;
      v1 = v1 + v0;
      CPU.MTC2(a0, 4);
      CPU.MTC2(v1, 5);
      CPU.COP2(0x280030L);
      s7 = s7 - 0x1L;
      s5 = CPU.CFC2(31);
      if((int)s5 >= 0) {
        CPU.COP2(0x1400006L);
        s5 = CPU.MFC2(24);

        if((int)s5 > 0 || (t0 & 0x2L) != 0 && s5 != 0) {
          final SVECTOR sp0x30 = new SVECTOR();

          //LAB_800e191c
          MEMORY.ref(4, s1).offset(0xcL).setu(CPU.MFC2(12));
          MEMORY.ref(4, s1).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, s1).offset(0x1cL).setu(CPU.MFC2(14));
          a1 = MEMORY.ref(4, s6).offset(sp0x10.offset(2, 0x1eL).get() * 0x4L).get();
          v0 = a1 >>> 24;
          v0 = v0 << 2;
          v0 = v0 + s6;
          v1 = a1 << 24;
          a0 = MEMORY.ref(4, v0).offset(0x0L).get();
          v1 = (int)v1 >> 24;
          v0 = a0 << 24;
          v0 = (int)v0 >> 16;
          v1 = v1 + v0;
          sp0x30.setX((short)v1);
          v1 = (short)a1 >> 8;
          v0 = (int)a0 >> 8;
          v0 = (byte)v0 << 8;
          v1 = v1 + v0;
          a1 = a1 << 8;
          a1 = (int)a1 >> 24;
          a0 = (int)a0 >> 16;
          a0 = a0 << 24;
          a0 = (int)a0 >> 16;
          a1 = a1 + a0;
          sp0x30.setY((short)v1);
          sp0x30.setZ((short)a1);
          CPU.MTC2(sp0x30.getXY(), 0);
          CPU.MTC2(sp0x30.getZ(),  1);
          CPU.COP2(0x180001L);
          s5 = CPU.CFC2(31);
          MEMORY.ref(4, s1).offset(0x24L).setu(CPU.MFC2(14));
          CPU.COP2(0x168002eL);
          s3 = (int)(CPU.MFC2(7) + _1f8003e8.get()) / 4;

          if((int)s3 >= 0xbL) {
            if((int)s3 >= 0xffeL) {
              s3 = 0xffeL;
            }

            //LAB_800e19fc
            CPU.MTC2(sp0x10.offset(4, 0x0L).get(), 6);
            v1 = MEMORY.ref(4, fp).offset(sp0x10.offset(2, 0x10L).get() * 0x4L).get();
            sp0x30.setX((short)((int)(v1 << 20) >> 19 & 0xffff_fffcL));
            sp0x30.setY((short)((int)(v1 << 10) >> 19 & 0xffff_fffcL));
            sp0x30.setZ((short)((int)v1 >> 19 & 0xffff_fffcL));
            CPU.MTC2(sp0x30.getXY(), 0);
            CPU.MTC2(sp0x30.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, s1).offset(0x8L).setu(CPU.MFC2(22));
            MEMORY.ref(1, s1).offset(0xbL).setu(t0);
            CPU.MTC2(sp0x10.offset(4, 0x4L).get(), 6);
            v1 = MEMORY.ref(4, fp).offset(sp0x10.offset(2, 0x14L).get() * 0x4L).get();
            sp0x30.setX((short)((int)(v1 << 20) >> 19 & 0xffff_fffcL));
            sp0x30.setY((short)((int)(v1 << 10) >> 19 & 0xffff_fffcL));
            sp0x30.setZ((short)((int)v1 >> 19 & 0xffff_fffcL));
            CPU.MTC2(sp0x30.getXY(), 0);
            CPU.MTC2(sp0x30.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, s1).offset(0x10L).setu(CPU.MFC2(22));
            CPU.MTC2(sp0x10.offset(4, 0x8L).get(), 6);
            v1 = MEMORY.ref(4, fp).offset(sp0x10.offset(2, 0x18L).get() * 0x4L).get();
            sp0x30.setX((short)((int)(v1 << 20) >> 19 & 0xffff_fffcL));
            sp0x30.setY((short)((int)(v1 << 10) >> 19 & 0xffff_fffcL));
            sp0x30.setZ((short)((int)v1 >> 19 & 0xffff_fffcL));
            CPU.MTC2(sp0x30.getXY(), 0);
            CPU.MTC2(sp0x30.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, s1).offset(0x18L).setu(CPU.MFC2(22));
            CPU.MTC2(sp0x10.offset(4, 0xcL).get(), 6);
            v1 = MEMORY.ref(4, fp).offset(sp0x10.offset(2, 0x1cL).get() * 0x4L).get();
            sp0x30.setX((short)((int)(v1 << 20) >> 19 & 0xffff_fffcL));
            sp0x30.setY((short)((int)(v1 << 10) >> 19 & 0xffff_fffcL));
            sp0x30.setZ((short)((int)v1 >> 19 & 0xffff_fffcL));
            CPU.MTC2(sp0x30.getXY(), 0);
            CPU.MTC2(sp0x30.getZ(),  1);
            CPU.COP2(0x108041bL);
            MEMORY.ref(4, s1).offset(0x20L).setu(CPU.MFC2(22));
            MEMORY.ref(4, s1).offset(0x4L).setu(sp3c);
            final GsOT_TAG tag = sp38.get((int)s3);
            MEMORY.ref(4, s1).offset(0x0L).setu(0x900_0000L | tag.p.get());
            tag.p.set(s1 & 0xff_ffffL);
            s1 = s1 + 0x28L;
          }
        }
      }

      //LAB_800e1bd8
    }

    sp0x10tmp.release();

    //LAB_800e1be0
    linkedListAddress_1f8003d8.setu(s1);
    return (a3 + 0x3L) & 0xffff_fffcL;
  }

  @Method(0x800e2620L)
  public static long FUN_800e2620(long a0, long s7, long a2, long s6) {
    long v0;
    long v1;
    long a1;
    long s2;
    long s4;
    long s5 = a0 + 0x4L;
    long s3 = linkedListAddress_1f8003d8.get();
    final long sp50 = _1f8003ee.get() & 0xff;
    final UnboundedArrayRef<GsOT_TAG> sp48 = orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()).org_04.deref();
    final IntRef sp0x30 = new IntRef();
    final IntRef sp0x34 = new IntRef();
    final IntRef sp0x38 = new IntRef();
    FUN_800e4d74(sp0x30, sp0x34, sp0x38);
    long s1 = s3 + 0x1eL;

    //LAB_800e26c0
    final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0x20);
    final Value sp0x10 = sp0x10tmp.get();
    while(s6 != 0) {
      v0 = FUN_800de840(sp0x10.getAddress(), s5, 0x20L);
      s5 = v0;
      a1 = MEMORY.ref(4, s7).offset(sp0x10.offset(2, 0x18L).get() * 0x4L).get();
      v0 = a1 >>> 24;
      v0 = v0 << 2;
      v0 = v0 + s7;
      a0 = a1 << 24;
      a0 = (int)a0 >> 24;
      v1 = a1 << 16;
      a2 = MEMORY.ref(4, v0).offset(0x0L).get();
      v1 = (int)v1 >> 8;
      v0 = a2 << 8;
      a0 = a0 + v0;
      a0 = a0 & 0xffffL;
      v0 = a2 & 0xff00L;
      v0 = v0 << 16;
      v1 = v1 + v0;
      v1 = v1 & 0xffff_0000L;
      a0 = a0 | v1;
      v1 = a1 << 8;
      v1 = (int)v1 >> 24;
      v0 = a2 & 0xff_0000L;
      v0 = (int)v0 >> 8;
      v1 = v1 + v0;
      CPU.MTC2(a0, 0);
      CPU.MTC2(v1, 1);
      a1 = MEMORY.ref(4, s7).offset(sp0x10.offset(2, 0x1aL).get() * 0x4L).get();
      v0 = a1 >>> 24;
      v0 = s7 + v0 * 0x4L;
      a0 = a1 << 24;
      a0 = (int)a0 >> 24;
      v1 = a1 << 16;
      a2 = MEMORY.ref(4, v0).offset(0x0L).get();
      v1 = (int)v1 >> 8;
      v0 = a2 << 8;
      a0 = a0 + v0;
      a0 = a0 & 0xffffL;
      v0 = a2 & 0xff00L;
      v0 = v0 << 16;
      v1 = v1 + v0;
      v1 = v1 & 0xffff_0000L;
      a0 = a0 | v1;
      v1 = a1 << 8;
      v1 = (int)v1 >> 24;
      v0 = a2 & 0xff_0000L;
      v0 = (int)v0 >> 8;
      v1 = v1 + v0;
      CPU.MTC2(a0, 2);
      CPU.MTC2(v1, 3);
      a1 = MEMORY.ref(4, s7).offset(sp0x10.offset(2, 0x1cL).get() * 0x4L).get();
      v0 = a1 >>> 24;
      v0 = s7 + v0 * 0x4L;
      a0 = a1 << 24;
      a0 = (int)a0 >> 24;
      v1 = a1 << 16;
      a2 = MEMORY.ref(4, v0).offset(0x0L).get();
      v1 = (int)v1 >> 8;
      v0 = a2 << 8;
      a0 = a0 + v0;
      a0 = a0 & 0xffffL;
      v0 = a2 & 0xff00L;
      v0 = v0 << 16;
      v1 = v1 + v0;
      v1 = v1 & 0xffff_0000L;
      a0 = a0 | v1;
      v1 = a1 << 8;
      v1 = (int)v1 >> 24;
      v0 = a2 & 0xff_0000L;
      v0 = (int)v0 >> 8;
      v1 = v1 + v0;
      CPU.MTC2(a0, 4);
      CPU.MTC2(v1, 5);
      CPU.COP2(0x280030L);
      MEMORY.ref(4, s1-0x12L).setu(sp0x10.offset(4, 0x0L).get());
      MEMORY.ref(4, s1-0x6L).setu(sp0x10.offset(4, 0x4L).get());
      s6 = s6 - 0x1L;

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);
        MEMORY.ref(4, s1+0x6L).setu(sp0x10.offset(4, 0x8L).get());
        s4 = CPU.MFC2(24);

        if((int)s4 > 0 || (sp50 & 0x2L) != 0 && s4 != 0) {
          //LAB_800e28c4
          MEMORY.ref(4, s3).offset(0x8L).setu(CPU.MFC2(12));
          MEMORY.ref(4, s3).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, s3).offset(0x20L).setu(CPU.MFC2(14));
          s4 = CPU.CFC2(31);
          CPU.COP2(0x158002dL);
          MEMORY.ref(1, s1).offset(-0x17L).setu(sp50);
          MEMORY.ref(1, s1).offset(-0x1aL).setu((sp0x10.offset(1, 0x0cL).get() * sp0x30.get()) >> 12);
          MEMORY.ref(1, s1).offset(-0x19L).setu((sp0x10.offset(1, 0x0dL).get() * sp0x34.get()) >> 12);
          MEMORY.ref(1, s1).offset(-0x18L).setu((sp0x10.offset(1, 0x0eL).get() * sp0x38.get()) >> 12);
          MEMORY.ref(1, s1).offset(-0x0eL).setu((sp0x10.offset(1, 0x10L).get() * sp0x30.get()) >> 12);
          MEMORY.ref(1, s1).offset(-0x0dL).setu((sp0x10.offset(1, 0x11L).get() * sp0x34.get()) >> 12);
          MEMORY.ref(1, s1).offset(-0x0cL).setu((sp0x10.offset(1, 0x12L).get() * sp0x38.get()) >> 12);
          MEMORY.ref(1, s1).offset(-0x02L).setu((sp0x10.offset(1, 0x14L).get() * sp0x30.get()) >> 12);
          MEMORY.ref(1, s1).offset(-0x01L).setu((sp0x10.offset(1, 0x15L).get() * sp0x34.get()) >> 12);
          MEMORY.ref(1, s1).offset( 0x00L).setu((sp0x10.offset(1, 0x16L).get() * sp0x38.get()) >> 12);
          s2 = (CPU.MFC2(7) + _1f8003e8.get()) / 4;
          if((int)s2 >= 0xbL) {
            if((int)s2 >= 0xffeL) {
              s2 = 0xffeL;
            }

            //LAB_800e2a18
            MEMORY.ref(4, s3).offset(0x0L).setu(sp48.get((int)s2).p.get() | 0x900_0000L);
            sp48.get((int)s2).p.set(s3 & 0xff_ffffL);
            s3 = s3 + 0x28L;
            s1 = s1 + 0x28L;
          }
        }
      }

      //LAB_800e2a4c
    }

    sp0x10tmp.release();

    //LAB_800e2a54
    linkedListAddress_1f8003d8.setu(s3);
    return (s5 + 0x3L) & 0xffff_fffcL;
  }

  @Method(0x800e3e6cL)
  public static void FUN_800e3e6c(final long[] a0) {
    long v0;
    long a2;
    long s0;
    long s1;
    long s2;
    long s3;

    final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0x50);
    final BttlStruct50 sp0x10 = sp0x10tmp.get().cast(BttlStruct50::new);
    _800c6920.set(sp0x10);
    sp0x10._00.set(0);

    if((a0[0] & 0x4000_0000L) == 0) {
      s1 = 0;
    } else {
      s1 = 0x12L;
    }

    //LAB_800e3eb4
    v0 = a0[2];
    a2 = MEMORY.ref(4, v0).offset(0x10L).get();
    s0 = MEMORY.ref(4, v0).offset(0x14L).get();
    s3 = MEMORY.ref(4, v0).offset(0x0L).get();
    s2 = MEMORY.ref(4, v0).offset(0x8L).get();

    //LAB_800e3ee4
    while(s0 != 0) {
      final BttlStruct50 v0_0 = _800c6920.deref();
      v0_0._0c.set(0);
      v0_0._08.set(0);
      v0_0._04.set(v0_0._00.get());
      s0 = s0 - MEMORY.ref(2, a2).offset(0x0L).get();
      long a1 = MEMORY.ref(4, a2).offset(0x0L).get();
      _1f8003ee.setu(((int)a1 >> 24 | s1) & 0x3eL);
      a1 = (a1 >>> 14 & 0x20L) | (a1 >>> 24 & 0xfL) | (a1 >>> 18 & 0x1L) | s1;
      a2 = (long)_800fadbc.offset(a1 * 0x4L).deref(4).call(a2, s3, s2, MEMORY.ref(2, a2).get());
    }

    sp0x10tmp.release();

    //LAB_800e3f64
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
    long a2 = 0x41L;
    long v0 = 0x800c_0000L;
    long a1 = 0x800c_0000L;
    long v1 = MEMORY.ref(4, v0).offset(0x6930L).get();
    long a0 = MEMORY.ref(4, a1).offset(0x692cL).get();
    MEMORY.ref(4, v1).offset(0x8L).setu(0x800L);
    MEMORY.ref(4, v1).offset(0x4L).setu(0x800L);
    MEMORY.ref(4, v1).offset(0x0L).setu(0x800L);
    MEMORY.ref(1, a0).offset(0xeL).setu(0x80L);
    MEMORY.ref(1, a0).offset(0xdL).setu(0x80L);
    MEMORY.ref(1, a0).offset(0xcL).setu(0x80L);
    v1 = MEMORY.ref(4, a1).offset(0x692cL).get();
    MEMORY.ref(4, a0).offset(0x0L).setu(0);
    MEMORY.ref(4, a0).offset(0x4L).setu(0x1000L);
    MEMORY.ref(4, a0).offset(0x8L).setu(0);
    MEMORY.ref(4, v1).offset(0x10L).setu(0);
    MEMORY.ref(4, v1).offset(0x4cL).setu(0);

    //LAB_800e4720
    a0 = v1 + 0x84L;
    do {
      MEMORY.ref(4, a0).offset(0x0L).setu(0);
      a0 = a0 + 0x4L;
      v0 = a2;
      a2 = a2 - 0x1L;
    } while((int)v0 > 0);
  }

  @Method(0x800e4cf8L)
  public static void FUN_800e4cf8(final long r, final long g, final long b) {
    long v0;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6930L).get();
    MEMORY.ref(4, v0).offset(0x0L).setu(r);
    MEMORY.ref(4, v0).offset(0x4L).setu(g);
    MEMORY.ref(4, v0).offset(0x8L).setu(b);
    MEMORY.ref(4, v0).offset(0x24L).setu(0);
    GsSetAmbient(r, g, b);
  }

  @Method(0x800e4d74L)
  public static void FUN_800e4d74(final IntRef a0, final IntRef a1, final IntRef a2) {
    a0.set((int)_800c6930.deref(4).offset(0x0L).get());
    a1.set((int)_800c6930.deref(4).offset(0x4L).get());
    a2.set((int)_800c6930.deref(4).offset(0x8L).get());
  }

  @Method(0x800e5768L)
  public static void FUN_800e5768(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long s0;
    s0 = a0;
    a0 = MEMORY.ref(2, s0).offset(0x0L).get();
    a1 = MEMORY.ref(2, s0).offset(0x2L).get();
    a2 = MEMORY.ref(2, s0).offset(0x4L).get();
    FUN_800e4cf8(a0, a1, a2);

    v0 = MEMORY.ref(2, s0).offset(0xeL).getSigned();

    if((int)v0 > 0) {
      v0 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v0).offset(0x6930L).get();
      v0 = 0x3L;
      MEMORY.ref(4, v1).offset(0x24L).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0xcL).get();

      MEMORY.ref(2, v1).offset(0x2cL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0xeL).get();

      MEMORY.ref(2, v1).offset(0x2eL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x0L).get();

      MEMORY.ref(4, v1).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x2L).get();

      MEMORY.ref(4, v1).offset(0x10L).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x4L).get();

      MEMORY.ref(4, v1).offset(0x14L).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x6L).get();

      MEMORY.ref(4, v1).offset(0x18L).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x8L).get();

      MEMORY.ref(4, v1).offset(0x1cL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0xaL).get();
      MEMORY.ref(4, v1).offset(0x20L).setu(v0);
    } else {
      v0 = 0x800c_0000L;

      //LAB_800e5808
      v0 = MEMORY.ref(4, v0).offset(0x6930L).get();

      MEMORY.ref(4, v0).offset(0x24L).setu(0);
    }

    //LAB_800e5814
    t1 = 0;
    t3 = 0x800c_0000L;
    t2 = 0x3L;
    a0 = s0;
    t0 = t1;

    //LAB_800e5828
    do {
      v0 = MEMORY.ref(4, t3).offset(0x692cL).get();
      v1 = MEMORY.ref(2, a0).offset(0x10L).getSigned();
      a1 = v0 + t0;
      MEMORY.ref(4, a1).offset(0x0L).setu(v1);
      v0 = MEMORY.ref(2, a0).offset(0x12L).getSigned();

      MEMORY.ref(4, a1).offset(0x4L).setu(v0);
      v0 = MEMORY.ref(2, a0).offset(0x14L).getSigned();

      MEMORY.ref(4, a1).offset(0x8L).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1aL).get();

      MEMORY.ref(1, a1).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1bL).get();

      MEMORY.ref(1, a1).offset(0xdL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1cL).get();
      a3 = a1 + 0x10L;
      MEMORY.ref(1, a1).offset(0xeL).setu(v0);
      v0 = MEMORY.ref(2, a0).offset(0x16L).get();
      v1 = MEMORY.ref(2, a0).offset(0x18L).get();

      v0 = v0 | v1;
      a2 = a1 + 0x4cL;
      if(v0 != 0) {
        v0 = MEMORY.ref(4, a1).offset(0x0L).get();
        MEMORY.ref(4, a1).offset(0x10L).setu(t2);
        MEMORY.ref(4, a3).offset(0x4L).setu(v0);
        v0 = MEMORY.ref(4, a1).offset(0x4L).get();

        MEMORY.ref(4, a3).offset(0x8L).setu(v0);
        v0 = MEMORY.ref(4, a1).offset(0x8L).get();

        MEMORY.ref(4, a3).offset(0xcL).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x16L).getSigned();

        MEMORY.ref(4, a3).offset(0x10L).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x18L).getSigned();
        MEMORY.ref(4, a3).offset(0x18L).setu(0);
        MEMORY.ref(4, a3).offset(0x14L).setu(v0);
      } else {
        //LAB_800e58cc
        MEMORY.ref(4, a1).offset(0x10L).setu(0);
      }

      //LAB_800e58d0
      v0 = MEMORY.ref(2, a0).offset(0x22L).getSigned();

      if(v0 != 0) {
        MEMORY.ref(4, a2).offset(0x0L).setu(t2);
        v0 = MEMORY.ref(1, a1).offset(0xcL).get();

        MEMORY.ref(4, a2).offset(0x4L).setu(v0);
        v0 = MEMORY.ref(1, a1).offset(0xdL).get();

        MEMORY.ref(4, a2).offset(0x8L).setu(v0);
        v0 = MEMORY.ref(1, a1).offset(0xeL).get();

        MEMORY.ref(4, a2).offset(0xcL).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1dL).get();

        MEMORY.ref(4, a2).offset(0x10L).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1eL).get();

        MEMORY.ref(4, a2).offset(0x14L).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1fL).get();

        MEMORY.ref(4, a2).offset(0x18L).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x20L).getSigned();

        MEMORY.ref(4, a2).offset(0x28L).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x22L).getSigned();
        MEMORY.ref(4, a2).offset(0x2cL).setu(v0);
      } else {
        //LAB_800e5944
        MEMORY.ref(4, a2).offset(0x0L).setu(0);
      }

      //LAB_800e5948
      a0 = a0 + 0x14L;
      t1 = t1 + 0x1L;
      t0 = t0 + 0x84L;
    } while((int)t1 < 0x3L);
  }

  @Method(0x800e5a78L)
  public static void FUN_800e5a78(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c struct) {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long s0;
    long s3;
    long hi;
    long lo;
    a0 = _800c6930.get();
    _800c6928.addu(0x1L);
    a1 = _800c6928.get();
    if(MEMORY.ref(4, a0).offset(0x24L).get() == 0x3L) {
      v1 = MEMORY.ref(2, a0).offset(0x2eL).getSigned();
      v0 = a1 + MEMORY.ref(2, a0).offset(0x2cL).getSigned();
      a0 = (v0 & 0xffff_ffffL) % (v1 & 0xffff_ffffL);

      a0 = a0 << 12;
      lo = (int)a0 / (int)v1;
      a0 = lo;

      v0 = rcos(a0);

      a1 = _800c6930.get();
      v1 = MEMORY.ref(4, a1).offset(0xcL).get();
      a3 = v0 + 0x1000L;
      lo = (long)(int)v1 * (int)a3 & 0xffff_ffffL;
      a0 = MEMORY.ref(4, a1).offset(0x18L).get();
      t0 = lo;
      a2 = 0x1000L - v0;
      lo = (long)(int)a0 * (int)a2 & 0xffff_ffffL;
      t2 = lo;
      v1 = t0 + t2;
      if((int)v1 < 0) {
        v1 = v1 + 0x1fffL;
      }

      //LAB_800e5b20
      v0 = MEMORY.ref(4, a1).offset(0x10L).get();

      lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
      a0 = lo;
      v0 = MEMORY.ref(4, a1).offset(0x1cL).get();

      lo = (long)(int)v0 * (int)a2 & 0xffff_ffffL;
      v0 = (int)v1 >> 13;
      t0 = lo;
      a0 = a0 + t0;
      MEMORY.ref(4, a1).offset(0x0L).setu(v0);
      if((int)a0 < 0) {
        a0 = a0 + 0x1fffL;
      }

      //LAB_800e5b54
      v0 = MEMORY.ref(4, a1).offset(0x14L).get();

      lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
      v1 = lo;
      v0 = MEMORY.ref(4, a1).offset(0x20L).get();

      lo = (long)(int)v0 * (int)a2 & 0xffff_ffffL;
      v0 = (int)a0 >> 13;
      MEMORY.ref(4, a1).offset(0x4L).setu(v0);
      a2 = lo;
      v0 = v1 + a2;
      if((int)v0 < 0) {
        v0 = v0 + 0x1fffL;
      }

      //LAB_800e5b8c
      v0 = (int)v0 >> 13;
      MEMORY.ref(4, a1).offset(0x8L).setu(v0);
    }

    //LAB_800e5b98
    long s1 = 0;

    //LAB_800e5ba0
    for(s3 = 0; s3 < 3; s3++) {
      a3 = _800c692c.get() + s1;
      a2 = a3 + 0x10L;
      v1 = MEMORY.ref(1, a3).offset(0x10L).get();
      if(v1 == 0x1L) {
        //LAB_800e5c50
        MEMORY.ref(4, a2).offset(0x10L).addu(MEMORY.ref(4, a2).offset(0x1cL).get());
        MEMORY.ref(4, a2).offset(0x14L).addu(MEMORY.ref(4, a2).offset(0x20L).get());
        MEMORY.ref(4, a2).offset(0x18L).addu(MEMORY.ref(4, a2).offset(0x24L).get());
        MEMORY.ref(4, a2).offset(0x04L).addu(MEMORY.ref(4, a2).offset(0x10L).get());
        MEMORY.ref(4, a2).offset(0x08L).addu(MEMORY.ref(4, a2).offset(0x14L).get());
        MEMORY.ref(4, a2).offset(0x0cL).addu(MEMORY.ref(4, a2).offset(0x18L).get());

        if((MEMORY.ref(4, a3).offset(0x10L).get() & 0x8000L) != 0) {
          MEMORY.ref(4, a2).offset(0x34L).subu(0x1L);

          if((int)MEMORY.ref(4, a2).offset(0x34L).get() <= 0) {
            MEMORY.ref(4, a3).offset(0x10L).setu(0);
            MEMORY.ref(4, a2).offset(0x4L).setu(MEMORY.ref(4, a2).offset(0x28L).get());
            MEMORY.ref(4, a2).offset(0x8L).setu(MEMORY.ref(4, a2).offset(0x2cL).get());
            MEMORY.ref(4, a2).offset(0xcL).setu(MEMORY.ref(4, a2).offset(0x30L).get());
          }
        }

        //LAB_800e5cf4
        v1 = MEMORY.ref(4, a2).offset(0x0L).get();

        if((v1 & 0x2000L) != 0) {
          MEMORY.ref(4, a3).offset(0x0L).setu((int)MEMORY.ref(4, a2).offset(0x4L).get() >> 12);
          MEMORY.ref(4, a3).offset(0x4L).setu((int)MEMORY.ref(4, a2).offset(0x8L).get() >> 12);
          MEMORY.ref(4, a3).offset(0x8L).setu((int)MEMORY.ref(4, a2).offset(0xcL).get() >> 12);
          //LAB_800e5d40
        } else if((v1 & 0x4000L) != 0) {
          final SVECTOR sp0x18 = new SVECTOR();
          sp0x18.setX((short)MEMORY.ref(2, a2).offset(0x4L).get());
          sp0x18.setY((short)MEMORY.ref(2, a2).offset(0x8L).get());
          sp0x18.setZ((short)MEMORY.ref(2, a2).offset(0xcL).get());
          FUN_800e4674(MEMORY.ref(4, a3, VECTOR::new), sp0x18); //TODO
        }
      } else if(v1 == 0x2L) {
        //LAB_800e5bf0
        v0 = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, a2).offset(0x38L).get()).deref().getAddress(); //TODO
        a1 = MEMORY.ref(4, v0).offset(0x0L).get() + 0x1bcL;

        final SVECTOR sp0x10 = new SVECTOR();
        sp0x10.setX((short)(MEMORY.ref(2, a1).offset(0x0L).get() + MEMORY.ref(2, a2).offset(0x4L).get()));
        sp0x10.setY((short)(MEMORY.ref(2, a1).offset(0x2L).get() + MEMORY.ref(2, a2).offset(0x8L).get()));
        sp0x10.setZ((short)(MEMORY.ref(2, a1).offset(0x4L).get() + MEMORY.ref(2, a2).offset(0xcL).get()));
        FUN_800e4674(MEMORY.ref(4, a3, VECTOR::new), sp0x10); //TODO
      } else if(v1 == 0x3L) {
        //LAB_800e5bdc
        //LAB_800e5d6c
        final SVECTOR sp0x18 = new SVECTOR();

        v1 = _800c6928.get() & 0xfffL;
        v0 = MEMORY.ref(4, a2).offset(0x10L).get();
        t1 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
        sp0x18.setX((short)(MEMORY.ref(2, a2).offset(0x4L).get() + t1));

        v0 = MEMORY.ref(4, a2).offset(0x14L).get();
        t1 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
        sp0x18.setY((short)(MEMORY.ref(2, a2).offset(0x8L).get() + t1));

        v0 = MEMORY.ref(4, a2).offset(0x18L).get();
        t1 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;

        //LAB_800e5dc8
        sp0x18.setZ((short)(MEMORY.ref(2, a2).offset(0xcL).get() + t1));

        //LAB_800e5dcc
        FUN_800e4674(MEMORY.ref(4, a3, VECTOR::new), sp0x18); //TODO
      }

      //LAB_800e5dd4
      s0 = a3 + 0x4cL;
      v1 = MEMORY.ref(1, s0).offset(0x0L).get();
      if(v1 == 0x1L) {
        //LAB_800e5df4
        v0 = MEMORY.ref(4, s0).offset(0x10L).get();
        v1 = MEMORY.ref(4, s0).offset(0x1cL).get();
        a0 = MEMORY.ref(4, s0).offset(0x20L).get();
        a1 = MEMORY.ref(4, s0).offset(0x24L).get();
        v0 = v0 + v1;
        MEMORY.ref(4, s0).offset(0x10L).setu(v0);
        v0 = MEMORY.ref(4, s0).offset(0x14L).get();
        v1 = MEMORY.ref(4, s0).offset(0x18L).get();
        v0 = v0 + a0;
        MEMORY.ref(4, s0).offset(0x14L).setu(v0);
        v0 = MEMORY.ref(4, s0).offset(0x4L).get();
        a0 = MEMORY.ref(4, s0).offset(0x10L).get();
        v1 = v1 + a1;
        MEMORY.ref(4, s0).offset(0x18L).setu(v1);
        v1 = MEMORY.ref(4, s0).offset(0x8L).get();
        a1 = MEMORY.ref(4, s0).offset(0x14L).get();
        v0 = v0 + a0;
        MEMORY.ref(4, s0).offset(0x4L).setu(v0);
        v0 = MEMORY.ref(4, s0).offset(0xcL).get();
        a0 = MEMORY.ref(4, s0).offset(0x18L).get();
        v1 = v1 + a1;
        MEMORY.ref(4, s0).offset(0x8L).setu(v1);
        v1 = MEMORY.ref(4, s0).offset(0x0L).get();
        v0 = v0 + a0;
        v1 = v1 & 0x8000L;
        MEMORY.ref(4, s0).offset(0xcL).setu(v0);
        if(v1 != 0) {
          v0 = MEMORY.ref(4, s0).offset(0x34L).get();

          v0 = v0 - 0x1L;
          MEMORY.ref(4, s0).offset(0x34L).setu(v0);
          if((int)v0 <= 0) {
            v0 = MEMORY.ref(4, s0).offset(0x28L).get();
            v1 = MEMORY.ref(4, s0).offset(0x2cL).get();
            a0 = MEMORY.ref(4, s0).offset(0x30L).get();
            MEMORY.ref(4, s0).offset(0x0L).setu(0);
            MEMORY.ref(4, s0).offset(0x4L).setu(v0);
            MEMORY.ref(4, s0).offset(0x8L).setu(v1);
            MEMORY.ref(4, s0).offset(0xcL).setu(a0);
          }
        }

        //LAB_800e5e90
        v1 = _800c692c.get();
        v0 = MEMORY.ref(4, s0).offset(0x4L).get();
        v1 = s1 + v1;
        v0 = (int)v0 >> 12;
        MEMORY.ref(1, v1).offset(0xcL).setu(v0);
        v1 = _800c692c.get();
        v0 = MEMORY.ref(4, s0).offset(0x8L).get();
        v1 = s1 + v1;
        v0 = (int)v0 >> 12;
        MEMORY.ref(1, v1).offset(0xdL).setu(v0);
        v1 = _800c692c.get();
        v0 = MEMORY.ref(4, s0).offset(0xcL).get();
        v1 = s1 + v1;
        v0 = (int)v0 >> 12;
        MEMORY.ref(1, v1).offset(0xeL).setu(v0);
      } else {
        v0 = 0x3L;
        if(v1 == v0) {
          v0 = 0x800c_0000L;

          //LAB_800e5ed0
          a0 = MEMORY.ref(4, s0).offset(0x28L).get();
          v0 = MEMORY.ref(4, v0).offset(0x6928L).get();
          v1 = MEMORY.ref(4, s0).offset(0x2cL).get();
          v0 = v0 + a0;
          hi = (v0 & 0xffff_ffffL) % (v1 & 0xffff_ffffL);
          a0 = hi;

          a0 = a0 << 12;
          lo = (int)a0 / (int)v1;
          a0 = lo;

          v0 = rcos(a0);
          v1 = MEMORY.ref(4, s0).offset(0x4L).get();
          a3 = v0 + 0x1000L;
          lo = (long)(int)v1 * (int)a3 & 0xffff_ffffL;
          a0 = MEMORY.ref(4, s0).offset(0x10L).get();
          a1 = lo;
          v1 = 0x1000L;
          a2 = v1 - v0;
          lo = (long)(int)a0 * (int)a2 & 0xffff_ffffL;
          v0 = _800c692c.get();
          t0 = lo;
          v1 = a1 + t0;
          a0 = s1 + v0;
          if((int)v1 < 0) {
            v1 = v1 + 0x1fffL;
          }

          //LAB_800e5f38
          v0 = (int)v1 >> 13;
          MEMORY.ref(1, a0).offset(0xcL).setu(v0);
          v0 = MEMORY.ref(4, s0).offset(0x8L).get();

          lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
          a0 = lo;
          v0 = MEMORY.ref(4, s0).offset(0x14L).get();

          lo = (long)(int)v0 * (int)a2 & 0xffff_ffffL;
          v0 = _800c692c.get();
          t0 = lo;
          a0 = a0 + t0;
          a1 = s1 + v0;
          if((int)a0 < 0) {
            a0 = a0 + 0x1fffL;
          }

          //LAB_800e5f74
          v0 = (int)a0 >> 13;
          MEMORY.ref(1, a1).offset(0xdL).setu(v0);
          v0 = MEMORY.ref(4, s0).offset(0xcL).get();

          lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
          a0 = lo;
          v0 = MEMORY.ref(4, s0).offset(0x18L).get();

          lo = (long)(int)v0 * (int)a2 & 0xffff_ffffL;
          v0 = _800c692c.get();
          v1 = lo;
          v1 = a0 + v1;
          a0 = s1 + v0;
          if((int)v1 < 0) {
            v1 = v1 + 0x1fffL;
          }

          //LAB_800e5fb0
          v0 = (int)v1 >> 13;
          MEMORY.ref(1, a0).offset(0xeL).setu(v0);
        }
      }

      //LAB_800e5fb8
      s1 = s1 + 0x84L;

      //LAB_800e5fbc
    }
  }

  @Method(0x800e5fe8L)
  public static void FUN_800e5fe8(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c struct) {
    //LAB_800e6008
    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, _800c692c.deref(4).offset(i * 0x84L).cast(GsF_LIGHT::new)); //TODO
    }

    final long v0 = _800c6930.get(); //TODO
    GsSetAmbient(MEMORY.ref(4, v0).offset(0x0L).get(), MEMORY.ref(4, v0).offset(0x4L).get(), MEMORY.ref(4, v0).offset(0x8L).get());
    _1f8003f8.setu(getProjectionPlaneDistance());
  }

  @Method(0x800e6070L)
  public static void FUN_800e6070() {
    allocateScriptState(1, 0, false, 0, 0);
    loadScriptFile(1, script_800faebc, "BTTL Script 800faebc", 0); //TODO unknown size
    setCallback04(1, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e5a78", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));
    setCallback08(1, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e5fe8", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));
    _800c6930.deref(4).offset(0x60L).setu(0);
    FUN_800e46c8();
  }

  @Method(0x800e60e0L)
  public static void FUN_800e60e0(final long a0, final long a1, final long a2) {
    final long s0 = _800c6930.get();
    //TODO
    FUN_800e4d74(MEMORY.ref(4, s0 + 0x30L + MEMORY.ref(4, s0).offset(0x60L).get() * 0xcL, IntRef::new), MEMORY.ref(4, s0 + 0x4L, IntRef::new), MEMORY.ref(4, s0 + 0x8L, IntRef::new));

    final long v1 = _800c6930.get();
    MEMORY.ref(4, v1).offset(0x0L).setu(a0);
    MEMORY.ref(4, v1).offset(0x4L).setu(a1);
    MEMORY.ref(4, v1).offset(0x8L).setu(a2);
    MEMORY.ref(4, v1).offset(0x60L).addu(0x1L).and(0x3L);
  }

  @Method(0x800e6170L)
  public static void FUN_800e6170() {
    final long a0 = _800c6930.get();
    MEMORY.ref(4, a0).offset(0x60L).subu(0x1L).and(0x3L);

    final long v1 = MEMORY.ref(4, a0).offset(0x60L).get();
    MEMORY.ref(4, a0).offset(0x0L).setu(MEMORY.ref(4, a0).offset(0x30L).offset(v1 * 0xcL).get());
    MEMORY.ref(4, a0).offset(0x4L).setu(MEMORY.ref(4, a0).offset(0x34L).offset(v1 * 0xcL).get());
    MEMORY.ref(4, a0).offset(0x8L).setu(MEMORY.ref(4, a0).offset(0x38L).offset(v1 * 0xcL).get());
  }

  @Method(0x800e61e4L)
  public static void FUN_800e61e4(final long a0, final long a1, final long a2) {
    GsSetFlatLight(0, light_800c6ddc);
    GsSetFlatLight(1, light_800c6ddc);
    GsSetFlatLight(2, light_800c6ddc);
    FUN_800e60e0(a0, a1, a2);
    final long v0 = _800c6930.get();
    GsSetAmbient(MEMORY.ref(4, v0).offset(0x0L).get(), MEMORY.ref(4, v0).offset(0x4L).get(), MEMORY.ref(4, v0).offset(0x8L).get());
  }

  @Method(0x800e62a8L)
  public static void FUN_800e62a8() {
    FUN_800e6170();

    final long v0 = _800c6930.get(); //TODO
    GsSetAmbient(MEMORY.ref(4, v0).offset(0x0L).get(), MEMORY.ref(4, v0).offset(0x4L).get(), MEMORY.ref(4, v0).offset(0x8L).get());

    //TODO
    GsSetFlatLight(0, _800c692c.deref(4).offset(0x000L).cast(GsF_LIGHT::new));
    GsSetFlatLight(1, _800c692c.deref(4).offset(0x084L).cast(GsF_LIGHT::new));
    GsSetFlatLight(2, _800c692c.deref(4).offset(0x108L).cast(GsF_LIGHT::new));
  }

  @Method(0x800e6314L)
  public static void FUN_800e6314(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    removeFromLinkedList(_800c693c.deref(4).offset(0x5a8L).get());
    _800c693c.deref(4).offset(0x5a8L).setu(0);
    _800c693c.deref(4).offset(0x5acL).setu(0);
    FUN_80012bb4();
    _800fafe8.setu(0x4L);

    if((_800c693c.deref(4).offset(0x20L).get() & 0x4_0000L) != 0) {
      FUN_8001d068(_800c6938.deref(4).offset(0x4L).get(), 0x1L);
    }

    //LAB_800e638c
    FUN_800e883c(_800c693c.deref(4).offset(0x1cL).get(), index);

    if((_800c693c.deref(4).offset(0x20L).get() & 0x10_0000L) != 0) {
      //LAB_800e63d0
      for(int i = 0; i < battleStruct1a8Count_800c66a0.get(); i++) {
        final BattleStruct1a8 v1 = getBattleStruct1a8(i);
        if((v1._19e.get() & 0x1L) != 0 && v1._1a2.get() >= 0) {
          FUN_800c9708(i);
        }

        //LAB_800e6408
      }
    }

    //LAB_800e641c
    if((_800c693c.deref(4).offset(0x20L).get() & 0x60_0000L) != 0) {
      FUN_80115cac(0);
    }

    //LAB_800e6444
    _800c693c.deref(4).offset(0x20L).and(0xff80_ffffL);
  }

  @Method(0x800e6470L)
  public static long FUN_800e6470(final RunningScript a0) {
    final long t0 = a0.params_20.get(0).deref().get();
    final long a3 = _800c693c.get();
    MEMORY.ref(4, a3).offset(0x20L).oru(t0 & 0x1_0000L).oru(t0 & 0x2_0000L).oru(t0 & 0x10_0000L);

    if((MEMORY.ref(4, a3).offset(0x20L).get() & 0x10_0000L) != 0) {
      //LAB_800e651c
      for(int i = 0; i < battleStruct1a8Count_800c66a0.get(); i++) {
        final BattleStruct1a8 v1 = getBattleStruct1a8(i);

        if((v1._19e.get() & 0x1L) != 0 && v1._04.get() != 0 && v1._1a2.get() >= 0) {
          FUN_800ca418(i);
        }

        //LAB_800e6564
      }
    }

    //LAB_800e6578
    FUN_800e883c(_800c693c.deref(4).offset(0x1cL).get(), -0x1L);
    final long s2 = FUN_800e832c(
      a0.scriptStateIndex_00.get(),
      0,
      getMethodAddress(Bttl_800e.class, "FUN_800e70bc", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class),
      MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e71dc", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      getMethodAddress(Bttl_800e.class, "FUN_800e6314", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class)
    );

    long v0 = scriptStatePtrArr_800bc1c0.get((int)s2).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, v0).offset(0x4L).setu(0x600_0400L);
    v0 = _800c6938.get();
    MEMORY.ref(4, v0).offset(0x18L).setu(s2);
    MEMORY.ref(4, v0).offset(0x0L).setu(t0 & 0xffffL);
    MEMORY.ref(4, v0).offset(0x4L).setu(a0.params_20.get(1).deref().get());
    MEMORY.ref(4, v0).offset(0x8L).setu(a0.params_20.get(2).deref().get());
    MEMORY.ref(4, v0).offset(0x10L).setu(a0.params_20.get(3).deref().get() & 0xff);
    MEMORY.ref(4, v0).offset(0x1cL).setu(0);
    MEMORY.ref(4, v0).offset(0xcL).setu(a0.scriptStateIndex_00.get());
    FUN_80012b1c(0x3L, getMethodAddress(Bttl_800e.class, "FUN_800e704c", long.class), v0 + 0x1cL);
    _800c6938.deref(4).offset(0x20L).setu(-0x1L);
    return s2;
  }

  @Method(0x800e6920L)
  public static long FUN_800e6920(final RunningScript a0) {
    long v1;
    long s1;
    long sp20;

    sp20 = (short)a0.params_20.get(0).deref().get();
    s1 = a0.params_20.get(0).deref().get() & 0xff_0000L;
    if(sp20 == -0x1L) {
      final BtldScriptData27c v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
      assert false : "?"; //a0.params_20.get(0).set(sp0x20);
      sp20 = getBattleStruct1a8(v0._26c.get())._1a2.get();
    }

    //LAB_800e69a8
    _800c693c.deref(4).offset(0x20L).oru(s1 & 0x10_0000L);
    FUN_800e6470(a0);

    v1 = _800c6938.get();
    MEMORY.ref(4, v1).offset(0x14L).setu(0);
    MEMORY.ref(4, v1).offset(0x0L).oru(0x300_0000L);
    if(sp20 < 0x100L) {
      loadDrgnBinFile(0, 4433 + sp20 * 2, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
      loadDrgnBinFile(0, 4434 + sp20 * 2, 0, getMethodAddress(Bttl_800e.class, "FUN_800e7060", long.class, long.class, long.class), _800c6938.deref(4).offset(0x18L).get(), 0x2L);
    } else {
      //LAB_800e6a30
      final long a0_0 = sp20 >>> 4;
      long s0_0 = _800faec4.offset(2, (a0_0 - 0x100L) * 0x2L).get() + (sp20 & 0xfL);
      if((int)a0_0 >= 0x140L) {
        s0_0 = s0_0 + 0x75L;
      }

      //LAB_800e6a60
      s0_0 = (s0_0 - 0x1L) * 0x2L;
      loadDrgnBinFile(0, 4945 + s0_0, 0, getMethodAddress(Bttl_800e.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
      loadDrgnBinFile(0, 4946 + s0_0, 0, getMethodAddress(Bttl_800e.class, "FUN_800e7060", long.class, long.class, long.class), _800c6938.deref(4).offset(0x18L).get(), 0x2L);
    }

    //LAB_800e6a9c
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e704cL)
  public static void FUN_800e704c(final long param) {
    _800c6938.deref(4).offset(0x1cL).setu(0x1L);
  }

  @Method(0x800e7060L)
  public static void FUN_800e7060(final long address, final long fileSize, final long param) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    _800c693c.deref(4).offset(0x5a8L).setu(mrg.getAddress()); //TODO

    if(mrg.entries.get(0).size.get() != 0) {
      FUN_800ea620(mrg.getFile(0), mrg.entries.get(0).size.get(), param);
    }

    //LAB_800e7098
    _800c6938.deref(4).offset(0x14L).setu(address + MEMORY.ref(4, address).offset(0x10L).get());
  }

  @Method(0x800e70bcL)
  public static void FUN_800e70bc(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c struct) {
    long a0 = _800c6938.get();
    if((int)MEMORY.ref(4, a0).offset(0x20L).get() != -0x1L) {
      MEMORY.ref(4, a0).offset(0x20L).addu(vsyncMode_8007a3b8.get());
    }

    //LAB_800e70fc
    long v1 = _800c6938.get();
    if(MEMORY.ref(4, v1).offset(0x1cL).get() != 0 && MEMORY.ref(4, v1).offset(0x14L).get() != 0) {
      if((_800c693c.deref(4).offset(0x20L).get() & 0x4_0000L) == 0 || (getLoadedDrgnFiles() & 0x40L) == 0) {
        //LAB_800e7154
        if((_800c693c.deref(4).offset(0x20L).get() & 0x20_0000L) != 0) {
          FUN_80115cac(0x1L);
        }

        //LAB_800e7178
        if((_800c693c.deref(4).offset(0x20L).get() & 0x40_0000L) != 0) {
          FUN_80115cac(0x3L);
        }

        //LAB_800e719c
        long v0 = _800c6938.get();
        loadScriptFile(index, MEMORY.ref(4, v0).offset(0x14L).deref(4).cast(ScriptFile::new), MEMORY.ref(4, v0).offset(0x10L).get(), "", 0); //TODO
        v0 = _800c6938.get();
        MEMORY.ref(4, v0).offset(0x1cL).setu(0);
        MEMORY.ref(4, v0).offset(0x20L).setu(0);
      }
    }

    //LAB_800e71c4
  }

  @Method(0x800e71dcL)
  public static void FUN_800e71dc(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    // empty
  }

  @Method(0x800e727cL)
  public static long FUN_800e727c(final RunningScript a0) {
    if(_800fafe8.get() != 0 && a0.scriptStateIndex_00.get() != _800c6938.deref(4).offset(0xcL).get()) {
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
      _800c6938.deref(4).offset(0x18L).setu(0);
      return 0;
    }

    //LAB_800e7304
    throw new RuntimeException("Undefined v0");
  }

  @Method(0x800e74acL)
  public static long FUN_800e74ac(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)_800c6938.deref(4).offset(0x4L).get());
    a0.params_20.get(1).deref().set((int)_800c6938.deref(4).offset(0x8L).get());
    return 0;
  }

  @Method(0x800e7944L)
  public static void FUN_800e7944(final BattleStruct24 s1, final VECTOR a1, long s2) {
    long v0;
    long v1;
    long a0;
    long a1_0;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long s0;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long lo;

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
        s0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.setu(s0 + 0x28L);
        MEMORY.ref(1, s0).offset(0x3L).setu(0x9L);
        MEMORY.ref(4, s0).offset(0x4L).setu(0x2c80_8080L);
        v0 = s1._00.get();
        v1 = MEMORY.ref(1, s0).offset(0x7L).get();
        v0 = v0 >>> 29;
        v0 = v0 & 0x2L;
        v1 = v1 | v0;
        MEMORY.ref(1, s0).offset(0x7L).setu(v1);
        v0 = s1._14.get();

        MEMORY.ref(1, s0).offset(0x4L).setu(v0);
        v0 = s1._15.get();

        MEMORY.ref(1, s0).offset(0x5L).setu(v0);
        v0 = s1._16.get();

        MEMORY.ref(1, s0).offset(0x6L).setu(v0);
        v0 = s1._04.get();
        a0 = s1._1c.get();

        lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
        v1 = lo;
        v0 = _1f8003f8.getSigned() << 10;

        lo = (int)v0 / (int)a2;
        a1_0 = lo;
        if((int)v1 < 0) {
          v1 = v1 + 0x7L;
        }
        v0 = (int)v1 >> 3;

        //LAB_800e7acc
        lo = ((long)(int)v0 * (int)a1_0) & 0xffff_ffffL;
        v1 = lo;
        if((int)v1 < 0) {
          v1 = v1 + 0x7fffL;
        }

        //LAB_800e7ae0
        v0 = s1._08.get();

        lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
        v0 = lo;
        s5 = (int)v1 >> 15;
        if((int)v0 < 0) {
          v0 = v0 + 0x7L;
        }

        //LAB_800e7afc
        v0 = (int)v0 >> 3;
        lo = ((long)(int)v0 * (int)a1_0) & 0xffff_ffffL;
        a0 = lo;
        if((int)a0 < 0) {
          a0 = a0 + 0x7fffL;
        }

        //LAB_800e7b14
        v0 = s1._06.get();
        a2 = s1._1e.get();

        lo = ((long)(int)v0 * (int)a2) & 0xffff_ffffL;
        v0 = (int)a0 >> 15;
        v1 = lo;
        s7 = s5 + v0;
        if((int)v1 < 0) {
          v1 = v1 + 0x7L;
        }

        //LAB_800e7b38
        v0 = (int)v1 >> 3;
        lo = ((long)(int)v0 * (int)a1_0) & 0xffff_ffffL;
        v1 = lo;
        if((int)v1 < 0) {
          v1 = v1 + 0x7fffL;
        }

        //LAB_800e7b50
        v0 = s1._0a.get();

        lo = ((long)(int)v0 * (int)a2) & 0xffff_ffffL;
        v0 = lo;
        s2 = (int)v1 >> 15;
        if((int)v0 < 0) {
          v0 = v0 + 0x7L;
        }

        //LAB_800e7b6c
        v0 = (int)v0 >> 3;
        lo = ((long)(int)v0 * (int)a1_0) & 0xffff_ffffL;
        v0 = lo;
        if((int)v0 < 0) {
          v0 = v0 + 0x7fffL;
        }

        //LAB_800e7b84
        v0 = (int)v0 >> 15;
        fp = s2 + v0;
        s4 = rsin(s1._20.get());
        t2 = rcos(s1._20.get());
        lo = ((long)(int)s5 * (int)t2) & 0xffff_ffffL;
        t1 = lo;
        if((int)t1 >= 0) {
          v0 = t1;
        } else {
          v0 = t1 + 0xfffL;
        }

        //LAB_800e7bb8
        lo = ((long)(int)s2 * (int)s4) & 0xffff_ffffL;
        v0 = (int)v0 >> 12;
        t6 = s3 + v0;
        a1_0 = lo;
        if((int)a1_0 >= 0) {
          v1 = a1_0;
        } else {
          v1 = a1_0 + 0xfffL;
        }

        //LAB_800e7bd4
        lo = ((long)(int)s5 * (int)s4) & 0xffff_ffffL;
        a2 = (int)v1 >> 12;
        v0 = t6 - a2;
        MEMORY.ref(2, s0).offset(0x8L).setu(v0);
        t3 = (int)s3 >> 16;
        t0 = lo;
        if((int)t0 >= 0) {
          v0 = t0;
        } else {
          v0 = t0 + 0xfffL;
        }

        //LAB_800e7bf8
        lo = ((long)(int)s2 * (int)t2) & 0xffff_ffffL;
        v0 = (int)v0 >> 12;
        t5 = t3 + v0;
        a0 = lo;
        if((int)a0 >= 0) {
          v1 = a0;
        } else {
          v1 = a0 + 0xfffL;
        }

        //LAB_800e7c14
        lo = ((long)(int)s7 * (int)t2) & 0xffff_ffffL;
        v1 = (int)v1 >> 12;
        v0 = t5 + v1;
        MEMORY.ref(2, s0).offset(0xaL).setu(v0);
        a3 = lo;
        if((int)a3 >= 0) {
          v0 = a3;
        } else {
          v0 = a3 + 0xfffL;
        }

        //LAB_800e7c34
        v0 = (int)v0 >> 12;
        t4 = s3 + v0;
        lo = ((long)(int)s7 * (int)s4) & 0xffff_ffffL;
        v0 = t4 - a2;
        MEMORY.ref(2, s0).offset(0x10L).setu(v0);
        a1_0 = lo;
        if((int)a1_0 >= 0) {
          v0 = a1_0;
        } else {
          v0 = a1_0 + 0xfffL;
        }

        //LAB_800e7c58
        a2 = (int)v0 >> 12;
        v0 = t3 + a2;
        v0 = v0 + v1;
        MEMORY.ref(2, s0).offset(0x12L).setu(v0);
        lo = ((long)(int)fp * (int)s4) & 0xffff_ffffL;
        a0 = lo;
        if((int)a0 >= 0) {
          v0 = a0;
        } else {
          v0 = a0 + 0xfffL;
        }

        //LAB_800e7c7c
        t1 = (int)v0 >> 12;
        v0 = t6 - t1;
        MEMORY.ref(2, s0).offset(0x18L).setu(v0);
        lo = ((long)(int)fp * (int)t2) & 0xffff_ffffL;
        v1 = lo;
        if((int)v1 >= 0) {
          v0 = v1;
        } else {
          v0 = v1 + 0xfffL;
        }

        //LAB_800e7c9c
        t0 = (int)v0 >> 12;
        MEMORY.ref(2, s0).offset(0x1aL).setu(t5 + t0);
        MEMORY.ref(2, s0).offset(0x20L).setu(t4 - t1);
        v0 = (int)s3 >> 16;
        v0 = v0 + a2;
        v0 = v0 + t0;
        MEMORY.ref(2, s0).offset(0x22L).setu(v0);
        MEMORY.ref(1, s0).offset(0xcL).setu(s1._0e.get());
        MEMORY.ref(1, s0).offset(0xdL).setu(s1._0f.get());
        v1 = s1._08.get() + s1._0e.get() + 0xffL;
        MEMORY.ref(1, s0).offset(0x14L).setu(v1);
        MEMORY.ref(1, s0).offset(0x15L).setu(s1._0f.get());
        a0 = (int)s6 >> 2;
        MEMORY.ref(1, s0).offset(0x1cL).setu(s1._0e.get());
        v1 = s1._0a.get() + s1._0f.get() + 0xffL;
        MEMORY.ref(1, s0).offset(0x1dL).setu(v1);
        v1 = s1._08.get() + s1._0e.get() + 0xffL;
        MEMORY.ref(1, s0).offset(0x24L).setu(v1);
        v1 = s1._0a.get() + s1._0f.get() + 0xffL;
        MEMORY.ref(1, s0).offset(0x25L).setu(v1);
        v1 = s1._12.get() << 6;
        v0 = s1._10.get() & 0x3f0L;
        v0 = v0 >>> 4;
        v1 = v1 | v0;
        MEMORY.ref(2, s0).offset(0xeL).setu(v1);
        a0 = tags_1f8003d0.getPointer() + a0 * 0x4L;
        v1 = s1._00.get() >>> 23;
        v1 = v1 & 0x60L;
        v0 = s1._0c.get() | v1;
        MEMORY.ref(2, s0).offset(0x16L).setu(v0);
        insertElementIntoLinkedList(a0, s0);
      }
    }

    //LAB_800e7d8c
  }

  @Method(0x800e7ec4L)
  public static void FUN_800e7ec4(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c struct) {
    long v0;
    long v1;
    long a0 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.getPointer(); //TODO
    v1 = MEMORY.ref(2, a0).offset(0x50L).getSigned();
    if(v1 != -0x1L) {
      v0 = MEMORY.ref(2, a0).offset(0x56L).getSigned();

      if(v0 != -0x1L) {
        scriptStatePtrArr_800bc1c0.get((int)v0).deref().innerStruct_00.derefAs(BttlScriptData6c.class).scriptIndex_54.set((short)MEMORY.ref(2, a0).offset(0x54L).get());
      } else {
        //LAB_800e7f4c
        scriptStatePtrArr_800bc1c0.get((int)v1).deref().innerStruct_00.derefAs(BttlScriptData6c.class).scriptIndex_52.set((short)MEMORY.ref(2, a0).offset(0x54L).get());
      }

      //LAB_800e7f6c
      if(MEMORY.ref(2, a0).offset(0x54L).getSigned() != -0x1L) {
        scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(2, a0).offset(0x54L).getSigned()).deref().innerStruct_00.derefAs(BttlScriptData6c.class).scriptIndex_56.set((short)MEMORY.ref(2, a0).offset(0x56L).get());
      }

      //LAB_800e7fa0
      MEMORY.ref(2, a0).offset(0x56L).setu(-0x1L);
      MEMORY.ref(2, a0).offset(0x54L).setu(-0x1L);
      MEMORY.ref(2, a0).offset(0x50L).setu(-0x1L);
    }

    //LAB_800e7fac
    //LAB_800e7fcc
    while(struct.scriptIndex_52.get() != -0x1L) {
      a0 = scriptStatePtrArr_800bc1c0.get(struct.scriptIndex_52.get()).deref().innerStruct_00.getPointer(); //TODO

      //LAB_800e7ff8
      while(MEMORY.ref(2, a0).offset(0x52L).getSigned() != -0x1L) {
        a0 = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(2, a0).offset(0x52L).get()).deref().innerStruct_00.getPointer(); //TODO
      }

      //LAB_800e8020
      FUN_80015d38(MEMORY.ref(1, a0).offset(0xeL).getSigned());
    }

    //LAB_800e8040
    if(!struct._4c.isNull()) {
      struct._4c.deref().run(index, state, struct);
    }

    //LAB_800e805c
    if(!struct._44.isNull()) {
      removeFromLinkedList(struct._44.getPointer());
    }

    //LAB_800e8074
    while(!struct._58.isNull()) {
      a0 = struct._58.getPointer();

      struct._58.setNullable(struct._58.deref()._00.derefNullable());

      //LAB_800e8088
      removeFromLinkedList(a0);

      //LAB_800e8090
    }
  }

  @Method(0x800e80c4L)
  public static int FUN_800e80c4(final int a0, final long subStructSize, @Nullable final TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c> a2, @Nullable final TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c> callback08, @Nullable final TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c> a4, @Nullable final Function<Value, BttlScriptData6cSubBase1> subStructConstructor) {
    final int index = allocateScriptState(0x6cL, BttlScriptData6c::new);

    loadScriptFile(index, script_800faebc, "BTTL Script 800faebc", 0); //TODO unknown size
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e8e9c", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));

    if(callback08 != null) {
      setCallback08(index, callback08);
    }

    //LAB_800e8150
    setCallback0c(index, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e7ec4", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));

    final BttlScriptData6c s0 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
    s0._08.set(subStructSize);
    if(subStructSize != 0) {
      s0._44.set(MEMORY.ref(4, addToLinkedListTail(subStructSize), subStructConstructor));
    } else {
      //LAB_800e8184
      s0._44.clear();
    }

    //LAB_800e8188
    s0._48.setNullable(a2);
    s0._0e.set(index);
    s0._10.vec_04.set(0, 0, 0);
    s0._10.svec_10.set((short)0, (short)0, (short)0);
    s0._10._22.set((short)0);
    s0._10._24.set(0);
    s0._10.vec_28.set(0, 0, 0);
    s0._4c.setNullable(a4);
    s0.magic_00.set(0x2020_4d45L);
    s0._04.set(0xff00_0000L);
    s0._0c.set(-1);
    s0._0d.set(-1);
    s0._10._00.set(0x5400_0000L);
    s0._10.svec_16.set((short)0x1000, (short)0x1000, (short)0x1000);
    s0._10.svec_1c.set((short)0x80, (short)0x80, (short)0x80);
    s0._50.set((short)-1);
    s0.scriptIndex_52.set((short)-1);
    s0.scriptIndex_54.set((short)-1);
    s0.scriptIndex_56.set((short)-1);
    s0._58.clear();
    long s3 = scriptStatePtrArr_800bc1c0.get(index).deref().getAddress();
    MEMORY.ref(4, s3).offset(0xf8L).setu(s0._5c.getAddress()); //TODO
    strcpy(s0._5c, _800c6e18.get());

    if(a0 != -1) {
      long v0 = scriptStatePtrArr_800bc1c0.get(a0).deref().getAddress(); //TODO
      v0 = MEMORY.ref(4, v0).offset(0x0L).get();
      v0 = MEMORY.ref(4, v0).offset(0x0L).get();
      final long a0_0;
      if(v0 != 0x2020_4d45L) {
        a0_0 = _800c693c.deref(2).offset(0x1cL).get();
      } else {
        a0_0 = a0;
      }

      //LAB_800e8294
      long v1 = scriptStatePtrArr_800bc1c0.get((int)a0_0).deref().getAddress(); //TODO
      v0 = scriptStatePtrArr_800bc1c0.get(index).deref().getAddress(); //TODO
      v1 = MEMORY.ref(4, v1).offset(0x0L).get();
      long a1_0 = MEMORY.ref(4, v0).offset(0x0L).get();

      MEMORY.ref(2, a1_0).offset(0x50L).setu(a0_0);
      v0 = MEMORY.ref(2, v1).offset(0x52L).getSigned();
      if(v0 != -0x1L) {
        MEMORY.ref(2, a1_0).offset(0x54L).setu(MEMORY.ref(2, v1).offset(0x52L).get());
        v0 = MEMORY.ref(2, v1).offset(0x52L).getSigned();

        v0 = scriptStatePtrArr_800bc1c0.get((int)v0).deref().getAddress();

        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        MEMORY.ref(2, v0).offset(0x56L).setu(index);
      }

      //LAB_800e8300
      MEMORY.ref(2, v1).offset(0x52L).setu(index);
    }

    //LAB_800e8304
    return index;
  }

  @Method(0x800e832cL)
  public static <T extends MemoryRef> int FUN_800e832c(int a0, long a1, long a2, @Nullable final TriConsumerRef<Integer, ScriptState<T>, T> callback08, long a4) {
    final int index = allocateScriptState(0x6cL, BttlScriptData6c::new);
    loadScriptFile(index, script_800faebc, "BTTL Script FUN_800e832c", 0); //TODO
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e8e9c", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));

    if(callback08 != null) {
      setCallback08(index, callback08);
    }

    //LAB_800e83b8
    setCallback0c(index, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e7ec4", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));

    final ScriptState<BttlScriptData6c> s3 = scriptStatePtrArr_800bc1c0.get(index).derefAs(ScriptState.classFor(BttlScriptData6c.class));
    final long s0 = s3.innerStruct_00.derefAs(BttlScriptData6c.class).getAddress(); //TODO

    MEMORY.ref(4, s0).offset(0x08L).setu(a1);

    if(a1 != 0) {
      assert false : "This has to be a bug, a1 is a pointer to a function"; //TODO
      MEMORY.ref(4, s0).offset(0x44L).setu(addToLinkedListTail(a1));
    } else {
      //LAB_800e83ec
      MEMORY.ref(4, s0).offset(0x44L).setu(0);
    }

    //LAB_800e83f0
    MEMORY.ref(4, s0).offset(0x48L).setu(a2);
    MEMORY.ref(1, s0).offset(0x0eL).setu(index);
    MEMORY.ref(4, s0).offset(0x14L).setu(0);
    MEMORY.ref(4, s0).offset(0x18L).setu(0);
    MEMORY.ref(4, s0).offset(0x1cL).setu(0);
    MEMORY.ref(2, s0).offset(0x20L).setu(0);
    MEMORY.ref(2, s0).offset(0x22L).setu(0);
    MEMORY.ref(2, s0).offset(0x24L).setu(0);
    MEMORY.ref(2, s0).offset(0x32L).setu(0);
    MEMORY.ref(4, s0).offset(0x34L).setu(0);
    MEMORY.ref(4, s0).offset(0x38L).setu(0);
    MEMORY.ref(4, s0).offset(0x3cL).setu(0);
    MEMORY.ref(4, s0).offset(0x40L).setu(0);
    MEMORY.ref(4, s0).offset(0x4cL).setu(a4);
    MEMORY.ref(1, s0).offset(0x00L).setu(0x45L);
    MEMORY.ref(1, s0).offset(0x01L).setu(0x4dL);
    MEMORY.ref(1, s0).offset(0x02L).setu(0x20L);
    MEMORY.ref(1, s0).offset(0x03L).setu(0x20L);
    MEMORY.ref(4, s0).offset(0x04L).setu(0xff00_0000L);
    MEMORY.ref(1, s0).offset(0x0cL).setu(-0x1L);
    MEMORY.ref(1, s0).offset(0x0dL).setu(-0x1L);
    MEMORY.ref(4, s0).offset(0x10L).setu(0x5400_0000L);
    MEMORY.ref(2, s0).offset(0x26L).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x28L).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x2aL).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x2cL).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x2eL).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x30L).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x50L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x52L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x54L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x56L).setu(-0x1L);
    MEMORY.ref(4, s0).offset(0x58L).setu(0);
    s3.ui_f8.set(s0 + 0x5cL);
    strcpy(MEMORY.ref(7, s0 + 0x5cL, CString::new), _800c6e18.get()); //TODO
    if(a0 != -1) {
      final long a0_0;
      if(scriptStatePtrArr_800bc1c0.get((short)a0).deref().innerStruct_00.derefAs(BtldScriptData27c.class).magic_00.get() == 0x2020_4d45L) {
        a0_0 = a0;
      } else {
        a0_0 = _800c693c.deref(4).offset(0x1cL).get();
      }

      //LAB_800e84fc
      final BttlScriptData6c data1 = scriptStatePtrArr_800bc1c0.get((short)a0_0).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
      final BttlScriptData6c data2 = scriptStatePtrArr_800bc1c0.get((short)index).deref().innerStruct_00.derefAs(BttlScriptData6c.class);

      data2._50.set((short)a0_0);
      if(data1.scriptIndex_52.get() != -0x1L) {
        data2.scriptIndex_54.set(data1.scriptIndex_52.get());
        scriptStatePtrArr_800bc1c0.get(data1.scriptIndex_52.get()).deref().innerStruct_00.derefAs(BttlScriptData6c.class).scriptIndex_56.set((short)index);
      }

      //LAB_800e8568
      data1.scriptIndex_52.set((short)index);
    }

    //LAB_800e856c
    return index;
  }

  @Method(0x800e8594L)
  public static void FUN_800e8594(final MATRIX s4, final BttlScriptData6c a1) {
    BttlScriptData6c s3 = a1;
    RotMatrix_8003faf0(s3._10.svec_10, s4);
    TransMatrix(s4, s3._10.vec_04);
    ScaleVectorL_SVEC(s4, s3._10.svec_16);

    int a0 = s3._0c.get();

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
      if(base.magic_00.get() == 0x4a42_4f42L) { // "BOBJ"
        final BtldScriptData27c s0 = state.innerStruct_00.derefAs(BtldScriptData27c.class);
        final BigStruct s1 = s0._148;
        FUN_800214bc(s1);

        if(s3._0d.get() == -0x1L) {
          sp0x10.set(s0._148.coord2_14.coord);
        } else {
          //LAB_800e8738
          final GsCOORDINATE2 coord2 = s1.coord2ArrPtr_04.deref().get(s3._0d.get());
          GsGetLw(coord2, sp0x10);
          coord2.flg.set(0);
        }

        //LAB_800e8774
        FUN_8003f210(sp0x10, s4, s4);
        a0 = -1;
        break;
      }

      if(base.magic_00.get() == 0x2020_4d45L) { // "EM  "
        final BttlScriptData6c s0 = state.innerStruct_00.derefAs(BttlScriptData6c.class);
        RotMatrix_8003faf0(s0._10.svec_10, sp0x10);
        TransMatrix(sp0x10, s0._10.vec_04);
        //LAB_800e866c
        ScaleVectorL_SVEC(sp0x10, s0._10.svec_16);

        if(s3._0d.get() != -0x1L) {
          FUN_8003f210(sp0x10, FUN_800ea0f4(s0, s3._0d.get()).coord, sp0x10);
        }

        //LAB_800e86ac
        FUN_8003f210(sp0x10, s4, s4);
        s3 = s0;
        a0 = s3._0c.get();
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
  public static void FUN_800e883c(long a0, long a1) {
    long v0;
    long v1;
    long a2;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    s5 = a0;
    v0 = 0x800c_0000L;
    v1 = v0 - 0x3e40L;
    v0 = s5 << 2;
    v0 = v0 + v1;
    v0 = MEMORY.ref(4, v0).offset(0x0L).get();

    s3 = MEMORY.ref(4, v0).offset(0x0L).get();

    s0 = MEMORY.ref(2, s3).offset(0x52L).getSigned();
    v0 = -0x1L;
    if(s0 == v0) {
      s1 = a1;
    } else {
      s1 = a1;
      s4 = v1;
      s2 = v0;

      //LAB_800e889c
      do {
        a0 = s0;
        a1 = s1;
        FUN_800e883c(a0, a1);
        v0 = s0 << 2;
        v0 = v0 + s4;
        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        s0 = MEMORY.ref(2, v0).offset(0x54L).getSigned();
      } while(s0 != s2);
    }

    //LAB_800e88cc
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x693cL).get();

    v0 = MEMORY.ref(4, v0).offset(0x1cL).get();

    if(s5 != v0) {
      if(s5 != s1) {
        v0 = 0x4_0000L;
        v1 = MEMORY.ref(4, s3).offset(0x4L).get();

        v0 = v1 & v0;
        if(v0 == 0) {
          v0 = 0xff00_0000L;
          v0 = v1 & v0;
          v1 = 0x200_0000L;
          if(v0 != v1) {
            v0 = MEMORY.ref(4, s3).offset(0x58L).get();

            if(v0 != 0) {
              s2 = s3 + 0x58L;
              v0 = MEMORY.ref(4, s2).offset(0x0L).get();

              //LAB_800e892c
              do {
                v0 = MEMORY.ref(1, v0).offset(0x4L).get();
                s0 = v0 << 24;
                a0 = (int)s0 >> 24;
                v0 = addToLinkedListTail(a0);
                v1 = MEMORY.ref(4, s2).offset(0x0L).get();
                s1 = v0;
                if(v1 < s1) {
                  a2 = s1;
                  a1 = v1;
                  a0 = (int)s0 >> 26;
                  v0 = a0;
                  if((int)v0 > 0) {
                    a0 = a0 - 0x1L;

                    //LAB_800e8968
                    do {
                      v0 = MEMORY.ref(4, a1).offset(0x0L).get();
                      a1 = a1 + 0x4L;
                      v1 = a0;
                      a0 = a0 - 0x1L;
                      MEMORY.ref(4, a2).offset(0x0L).setu(v0);
                      a2 = a2 + 0x4L;
                    } while((int)v1 > 0);
                  }

                  //LAB_800e8984
                  a0 = MEMORY.ref(4, s2).offset(0x0L).get();
                  removeFromLinkedList(a0);
                  MEMORY.ref(4, s2).offset(0x0L).setu(s1);
                } else {
                  //LAB_800e899c
                  removeFromLinkedList(s1);
                }

                //LAB_800e89ac
                s2 = MEMORY.ref(4, s2).offset(0x0L).get();

                v0 = MEMORY.ref(4, s2).offset(0x0L).get();
              } while(v0 != 0);
            }

            //LAB_800e89c4
            v0 = MEMORY.ref(4, s3).offset(0x44L).get();

            if(v0 != 0) {
              s0 = MEMORY.ref(4, s3).offset(0x8L).get();
              v0 = addToLinkedListTail(s0);
              v1 = MEMORY.ref(4, s3).offset(0x44L).get();
              s1 = v0;
              if(v1 < s1) {
                a2 = s1;
                a1 = v1;
                a0 = (int)s0 >> 2;
                v0 = a0;
                if((int)v0 > 0) {
                  a0 = a0 - 0x1L;

                  //LAB_800e8a0c
                  do {
                    v0 = MEMORY.ref(4, a1).offset(0x0L).get();
                    a1 = a1 + 0x4L;
                    v1 = a0;
                    a0 = a0 - 0x1L;
                    MEMORY.ref(4, a2).offset(0x0L).setu(v0);
                    a2 = a2 + 0x4L;
                  } while((int)v1 > 0);
                }

                //LAB_800e8a28
                a0 = MEMORY.ref(4, s3).offset(0x44L).get();
                removeFromLinkedList(a0);
                MEMORY.ref(4, s3).offset(0x44L).setu(s1);
              } else {
                //LAB_800e8a40
                removeFromLinkedList(s1);
              }
            }

            //LAB_800e8a50
            v0 = addToLinkedListTail(0x16cL);
            v1 = 0x800c_0000L;
            v1 = v1 - 0x3e40L;
            a0 = s5 << 2;
            a0 = a0 + v1;
            a0 = MEMORY.ref(4, a0).offset(0x0L).get();
            s1 = v0;
            if(a0 < s1) {
              a2 = s1;
              a1 = 0x5aL;

              //LAB_800e8a88
              do {
                v0 = MEMORY.ref(4, a0).offset(0x0L).get();
                MEMORY.ref(4, a2).offset(0x0L).setu(v0);
                a0 = a0 + 0x4L;
                a2 = a2 + 0x4L;
                v1 = a1;
                a1 = a1 - 0x1L;
              } while((int)v1 > 0);

              v0 = 0x800c_0000L;
              v0 = v0 - 0x3e40L;
              s0 = s5 << 2;
              s0 = s0 + v0;
              a0 = MEMORY.ref(4, s0).offset(0x0L).get();
              removeFromLinkedList(a0);
              v0 = s1 + 0x100L;
              MEMORY.ref(4, s0).offset(0x0L).setu(s1);
              MEMORY.ref(4, s1).offset(0x0L).setu(v0);
            } else {
              //LAB_800e8ad4
              removeFromLinkedList(s1);
            }
          }
        }
      }
    }

    //LAB_800e8ae4
  }

  @Method(0x800e8d04L)
  public static void FUN_800e8d04(final BttlScriptData6c a0, final long a1) {
    Pointer<BttlScriptData6cSubBase2> s0 = a0._58;

    //LAB_800e8d3c
    while(!s0.isNull()) {
      final BttlScriptData6cSubBase2 v1 = s0.deref();
      final byte v0 = v1._05.get();

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
  public static <T extends BttlScriptData6cSubBase2> T FUN_800e8dd4(final BttlScriptData6c a0, final long a1, final long a2, final BiFunctionRef<BttlScriptData6c, T, Long> callback, final long size, final Function<Value, T> constructor) {
    final T struct = MEMORY.ref(4, addToLinkedListTail(size), constructor);
    struct.size_04.set((int)size);
    struct._05.set((int)a1);
    struct._06.set((int)a2);
    struct._08.set(callback);
    struct._00.setNullable(a0._58.derefNullable());
    a0._58.set(struct);
    a0._04.or(1 << a1);
    return struct;
  }

  @Method(0x800e8e9cL)
  public static void FUN_800e8e9c(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    Pointer<BttlScriptData6cSubBase2> subPtr = data._58;

    if(!data._58.isNull()) {
      //LAB_800e8ee0
      do {
        final BttlScriptData6cSubBase2 sub = subPtr.deref();

        final BttlScriptData6cSubBase2 casted;
        if(sub.size_04.get() == 0x1c) {
          casted = subPtr.derefAs(BttlScriptData6cSub1c.class);
        } else if(sub.size_04.get() == 0x34) {
          casted = subPtr.derefAs(BttlScriptData6cSub34.class);
        } else {
          throw new RuntimeException("Unknown size %x".formatted(sub.size_04.get()));
        }

        final long v1 = sub._08.derefAs(BiFunctionRef.classFor(BttlScriptData6c.class, BttlScriptData6cSubBase2.class, Long.class)).run(data, casted);
        if(v1 == 0) {
          //LAB_800e8f2c
          data._04.and(~(1 << sub._05.get()));
          subPtr.setNullable(sub._00.derefNullable());
          removeFromLinkedList(sub.getAddress());
        } else if(v1 == 0x1L) {
          //LAB_800e8f6c
          subPtr = sub._00;
          //LAB_800e8f1c
        } else if(v1 == 0x2L) {
          //LAB_800e8f78
          FUN_80015d38(index);
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
    long v0 = addToLinkedListTail(0x7ccL);
    _800c6938.setu(v0 + 0x5b8L);
    _800c6930.setu(v0 + 0x5dcL);
    _800c692c.setu(v0 + 0x640L);
    MEMORY.ref(4, v0).offset(0x20L).setu(0x4L);
    MEMORY.ref(4, v0).offset(0x24L).setu(v0 + 0x28L);
    _800c6944.setu(v0 + 0x2f8L);
    _800c6940.setu(v0 + 0x390L);
    _800c693c.setu(v0);
    MEMORY.ref(4, v0).offset(0x1cL).setu(0);
    _800c6948.setu(v0 + 0x39cL);
    v0 = FUN_800e80c4(-1, 0, null, null, null, null);
    long a0 = _800c693c.get();
    MEMORY.ref(4, a0).offset(0x1cL).setu(v0);
    v0 = scriptStatePtrArr_800bc1c0.get((int)v0).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, v0).offset(0x4L).setu(0x600_0400L);
    MEMORY.ref(4, a0).offset(0x2cL).setu(0);
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(4, a0).offset(0x34L).setu(0);
    MEMORY.ref(4, a0).offset(0x38L).setu(0);
    FUN_800e6070();
    FUN_80012b1c(0x1L, getMethodAddress(SBtld.class, "FUN_801098f4", long.class), 0);
  }

  @Method(0x800e9100L)
  public static void FUN_800e9100() {
    FUN_800eacf4();
  }

  @Method(0x800e9288L)
  public static void FUN_800e9288(final long a0, final long a1, final long a2) {
    if(a2 != 0) {
      MEMORY.ref(4, a2).offset(0x0L).setu(a0);
    }
  }

  @Method(0x800e929cL)
  public static void FUN_800e929c(final long address, final long fileSize, final long param) {
    final long count = MEMORY.ref(4, address).offset(0x4L).get();

    //LAB_800e92d4
    for(int i = 0; i < count; i++) {
      if(MEMORY.ref(4, address).offset(i * 0x8L).offset(0xcL).get() != 0) {
        final TimHeader tim = parseTimHeader(MEMORY.ref(4, address + MEMORY.ref(4, address).offset(i * 0x8L).offset(0x8L).get() + 0x4L)); //TODO
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
    a0.params_20.get(0).deref().set(FUN_800e80c4(a0.scriptStateIndex_00.get(), 0, null, null, null, null));
    return 0;
  }

  @Method(0x800e9428L)
  public static void FUN_800e9428(final long a0, final BttlScriptData6cInner a1, final MATRIX a2) {
    if((int)a1._00.get() >= 0) {
      final BattleStruct24 sp0x10 = new BattleStruct24();
      sp0x10._00.set(a1._00.get());
      sp0x10._04.set((short)(-MEMORY.ref(1, a0).offset(0x4L).get() / 2));
      sp0x10._06.set((short)(-MEMORY.ref(1, a0).offset(0x5L).get() / 2));
      sp0x10._08.set((int)MEMORY.ref(1, a0).offset(0x4L).get());
      sp0x10._0a.set((int)MEMORY.ref(1, a0).offset(0x5L).get());
      sp0x10._0c.set((int)((MEMORY.ref(2, a0).offset(0x2L).get() & 0x100L) >>> 4 | (MEMORY.ref(2, a0).offset(0x0L).get() & 0x3ffL) >>> 6));
      sp0x10._0e.set((int)((MEMORY.ref(2, a0).offset(0x0L).get() & 0x3fL) << 2));
      sp0x10._0f.set((int)MEMORY.ref(1, a0).offset(0x2L).get());
      sp0x10._10.set((int)((MEMORY.ref(2, a0).offset(0x6L).get() << 4) & 0x3ffL));
      sp0x10._12.set((int)((MEMORY.ref(2, a0).offset(0x6L).get() >>> 6) & 0x1ffL));
      sp0x10._14.set(a1.svec_1c.getX());
      sp0x10._15.set(a1.svec_1c.getY());
      sp0x10._16.set(a1.svec_1c.getZ());
      sp0x10._1c.set(a1.svec_16.getX());
      sp0x10._1e.set(a1.svec_16.getY());
      sp0x10._20.set(a1.svec_10.getZ()); // This is correct, different svec for Z
      if((a1._00.get() & 0x400_0000L) != 0) {
        _1f8003e8.setu(a1._22.get());
        assert false;
//        FUN_800e75ac(sp0x10);
      } else {
        //LAB_800e9574
        FUN_800e7944(sp0x10, a2.transfer, a1._22.get());
      }
    }

    //LAB_800e9580
  }

  @Method(0x800e9590L)
  public static void FUN_800e9590(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    final BttlScriptData6c s0 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
    final MATRIX sp0x10 = new MATRIX();
    FUN_800e8594(sp0x10, s0);
    FUN_800e9428(s0._44.getPointer() + 0x4L, s0._10, sp0x10); //TODO
  }

  @Method(0x800e95f0L)
  public static void FUN_800e95f0(final long a0, final long a1) {
    MEMORY.ref(4, a0).offset(0x0L).setu(a1 | 0x400_0000L);
    if((a1 & 0xf_ff00L) == 0xf_ff00L) {
      final long v1 = _800c693c.get() + (a1 & 0xffL) * 0x8L;
      MEMORY.ref(4, a0).offset(0x4L).set(MEMORY.ref(4, v1).offset(0x39cL).get());
      MEMORY.ref(4, a0).offset(0x8L).set(MEMORY.ref(4, v1).offset(0x3a0L).get());
    } else{
      //LAB_800e9658
      long v0 = FUN_800eac58(a1 | 0x400_0000L);
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
    final int s2 = FUN_800e80c4(s1.scriptStateIndex_00.get(), 0xcL, null, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800e9590", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new), null, BttlScriptData6cSub0c::new);
    final ScriptState<BttlScriptData6c> v0 = scriptStatePtrArr_800bc1c0.get(s2).derefAs(ScriptState.classFor(BttlScriptData6c.class));
    final BttlScriptData6c s0 = v0.innerStruct_00.deref();
    s0._04.set(0x400_0000L);
    FUN_800e95f0(s0._44.getPointer(), s1.params_20.get(1).deref().get()); //TODO
    s0._10._00.and(0xfbff_ffffL).or(0x5000_0000L);
    s1.params_20.get(0).deref().set(s2);
    return 0;
  }

  @Method(0x800ea0f4L)
  public static GsCOORDINATE2 FUN_800ea0f4(final BttlScriptData6c a0, final long a1) {
    long s1 = a0._44.getPointer(); //TODO
    final BigStruct struct = MEMORY.ref(4, s1 + 0x10L, BigStruct::new);
    FUN_800214bc(struct);
    return struct.coord2ArrPtr_04.deref().get((int)a1);
  }

  @Method(0x800ea620L)
  public static void FUN_800ea620(final long a0, final long a1, final long a2) {
    //LAB_800ea674
    for(int s5 = 0; s5 < MEMORY.ref(2, a0).offset(0x6L).get(); s5++) {
      long s2 = a0 + MEMORY.ref(4, a0).offset(s5 * 0x8L).offset(0xcL).get();
      long v1 = MEMORY.ref(4, a0).offset(s5 * 0x8L).offset(0x8L).get() & 0xff00_0000L;
      if(v1 == 0x100_0000L) {
        //LAB_800ea6d4
        final long a0_0 = s2 + MEMORY.ref(4, s2).offset(0xcL).get();
        adjustTmdPointers(MEMORY.ref(4, a0_0 + 0x10L, Tmd::new)); //TODO

        //LAB_800ea700
        long s1 = a0_0 + 0xcL;
        for(int s0 = 0; s0 < MEMORY.ref(4, s1).offset(0x8L).get(); s0++) {
          FUN_800de76c(s1, s0);
        }
        //LAB_800ea6b4
      } else if(v1 == 0x300_0000L) {
        //LAB_800ea724
        final long a0_0 = s2 + MEMORY.ref(4, s2).offset(0xcL).get();
        adjustTmdPointers(MEMORY.ref(4, a0_0 + 0x10L, Tmd::new)); //TODO
        FUN_800de76c(a0_0 + 0xcL, 0);
      }

      if(v1 == 0x100_0000L || v1 == 0x200_0000L || v1 == 0x300_0000L) {
        //LAB_800ea748
        final long a2_0 = MEMORY.ref(4, s2).offset(0x8L).get();
        final long v1_0 = MEMORY.ref(4, s2).offset(0xcL).get();

        if(a2_0 != v1_0 && a2 != 0) {
          FUN_800eb308(scriptStatePtrArr_800bc1c0.get((int)a2).deref().innerStruct_00.derefAs(BttlScriptData6c.class), s2 + v1_0, s2 + a2_0);
        }
      }

      //LAB_800ea778
      //LAB_800ea77c
    }

    //LAB_800ea790
    _800c6950.setu(a0);
    _800c693c.deref(4).offset(0x5acL).setu(a0);
  }

  @Method(0x800ea7d0L)
  public static void FUN_800ea7d0(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long s0;
    s0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, s0).offset(0x693cL).get();
    a2 = MEMORY.ref(4, v0).offset(0x1cL).get();

    FUN_800ea620(a0, a1, a2);
    a2 = 0;
    v1 = MEMORY.ref(4, s0).offset(0x693cL).get();
    v0 = a2;
    a3 = MEMORY.ref(4, v1).offset(0x5acL).get();
    if(v0 == 0) {
      t1 = 0xff00_0000L;
      t0 = v1;
      a0 = a3;

      //LAB_800ea814
      do {
        v0 = MEMORY.ref(4, a0).offset(0x8L).get();

        v1 = v0 & t1;
        if(v1 != 0) {
          break;
        }
        a1 = v0 & 0xffL;
        v0 = MEMORY.ref(4, a0).offset(0xcL).get();
        v1 = a1 << 2;
        v1 = t0 + v1;
        v0 = a3 + v0;
        MEMORY.ref(4, v1).offset(0x390L).setu(v0);
        v0 = MEMORY.ref(2, a3).offset(0x6L).get();
        a2 = a2 + 0x1L;
        a0 = a0 + 0x8L;
      } while((int)a2 <= (int)v0);

      //LAB_800ea850
      a0 = MEMORY.ref(2, a3).offset(0x6L).get();

      if((int)a0 >= (int)a2) {
        t0 = 0xff00_0000L;
        a1 = 0x100_0000L;
        v0 = a2 << 3;
        v1 = v0 + a3;

        //LAB_800ea874
        do {
          v0 = MEMORY.ref(4, v1).offset(0x8L).get();

          v0 = v0 & t0;
          if(v0 != a1) {
            break;
          }
          a2 = a2 + 0x1L;
          v1 = v1 + 0x8L;
        } while((int)a0 >= (int)a2);
      }
    }

    v0 = 0x800c_0000L;

    //LAB_800ea89c
    v0 = MEMORY.ref(4, v0).offset(0x693cL).get();
    v1 = 0x3fL;
    v0 = v0 + 0xfcL;

    //LAB_800ea8a8
    do {
      MEMORY.ref(4, v0).offset(0x2f8L).setu(0);
      v1 = v1 - 0x1L;
      v0 = v0 - 0x4L;
    } while((int)v1 >= 0);

    v0 = MEMORY.ref(2, a3).offset(0x6L).get();

    if((int)v0 >= (int)a2) {
      v0 = 0x800c_0000L;
      t2 = 0xff00_0000L;
      t1 = 0x300_0000L;
      t0 = MEMORY.ref(4, v0).offset(0x693cL).get();
      v0 = a2 << 3;
      a0 = v0 + a3;

      //LAB_800ea8e0
      do {
        v0 = MEMORY.ref(4, a0).offset(0x8L).get();

        v1 = v0 & t2;
        if(v1 != t1) {
          break;
        }
        a1 = v0 & 0xffL;
        if(a1 >= 0x5L) {
          v0 = MEMORY.ref(4, a0).offset(0xcL).get();

          v0 = a3 + v0;
          v1 = MEMORY.ref(4, v0).offset(0xcL).get();

          v0 = v0 + v1;
          v1 = a1 << 2;
          v1 = t0 + v1;
          v0 = v0 + 0x18L;
          MEMORY.ref(4, v1).offset(0x2f8L).setu(v0);
        }

        //LAB_800ea928
        v0 = MEMORY.ref(2, a3).offset(0x6L).get();
        a2 = a2 + 0x1L;
        a0 = a0 + 0x8L;
      } while((int)a2 <= (int)v0);

      //LAB_800ea93c
      v0 = MEMORY.ref(2, a3).offset(0x6L).get();

      if((int)v0 >= (int)a2) {
        t3 = 0xff00_0000L;
        t2 = 0x400_0000L;
        t1 = 0x800c_0000L;
        v0 = a2 << 3;
        t0 = v0 + a3;

        //LAB_800ea964
        do {
          v0 = MEMORY.ref(4, t0).offset(0x8L).get();

          v1 = v0 & t3;
          if(v1 != t2) {
            break;
          }
          a1 = v0 & 0xffL;
          a0 = MEMORY.ref(4, t0).offset(0xcL).get();
          a1 = a1 << 3;
          a0 = a3 + a0;
          v0 = MEMORY.ref(4, a0).offset(0x8L).get();
          v1 = MEMORY.ref(4, t1).offset(0x693cL).get();
          a0 = a0 + v0;
          v0 = MEMORY.ref(2, a0).offset(0x0L).get();
          v1 = a1 + v1;
          MEMORY.ref(2, v1).offset(0x39cL).setu(v0);
          v0 = MEMORY.ref(2, a0).offset(0x2L).get();
          v1 = v1 + 0x39cL;
          MEMORY.ref(2, v1).offset(0x2L).setu(v0);
          v0 = MEMORY.ref(2, a0).offset(0x4L).getSigned();

          v0 = v0 << 2;
          MEMORY.ref(1, v1).offset(0x4L).setu(v0);
          v0 = MEMORY.ref(4, t1).offset(0x693cL).get();
          v1 = MEMORY.ref(1, a0).offset(0x6L).get();
          v0 = a1 + v0;
          MEMORY.ref(1, v0).offset(0x3a1L).setu(v1);
          v0 = MEMORY.ref(4, t1).offset(0x693cL).get();
          v1 = MEMORY.ref(2, a0).offset(0xaL).get();
          a1 = a1 + v0;
          v0 = MEMORY.ref(2, a0).offset(0x8L).get();
          v1 = v1 << 6;
          v0 = v0 & 0x3f0L;
          v0 = v0 >>> 4;
          v1 = v1 | v0;
          MEMORY.ref(2, a1).offset(0x3a2L).setu(v1);
          v0 = MEMORY.ref(2, a3).offset(0x6L).get();
          a2 = a2 + 0x1L;
          t0 = t0 + 0x8L;
        } while((int)a2 <= (int)v0);
      }
    }

    //LAB_800eaa00
    v0 = 0x800c_0000L;

    //LAB_800eaa04
    v0 = MEMORY.ref(4, v0).offset(0x693cL).get();

    MEMORY.ref(4, v0).offset(0x38L).setu(a3);
    MEMORY.ref(4, v0).offset(0x5acL).setu(0);
  }

  @Method(0x800eaa24L)
  public static void FUN_800eaa24(final long address, final long fileSize, final long param) {
    long v0;
    long v1;
    long a0 = address;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    s2 = a0;
    v0 = MEMORY.ref(4, s2).offset(0x18L).get();
    s1 = MEMORY.ref(4, s2).offset(0x1cL).get();
    s0 = s2 + v0;
    a0 = s1;
    v0 = addToLinkedListTail(a0);
    a0 = v0;
    a3 = a0;
    a2 = s0;
    a1 = (int)s1 >> 2;
    v0 = a1;
    if((int)v0 > 0) {
      a1 = a1 - 0x1L;

      //LAB_800eaa74
      do {
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        a2 = a2 + 0x4L;
        v1 = a1;
        a1 = a1 - 0x1L;
        MEMORY.ref(4, a3).offset(0x0L).setu(v0);
        a3 = a3 + 0x4L;
      } while((int)v1 > 0);
    }

    //LAB_800eaa90
    a1 = s1;
    a2 = 0;
    FUN_800ea7d0(a0, a1, a2);
    a1 = 0;
    a2 = a1;
    v0 = MEMORY.ref(4, s2).offset(0x20L).get();
    s1 = MEMORY.ref(4, s2).offset(0x24L).get();
    s0 = s2 + v0;
    a0 = s1;
    v0 = addToLinkedListTail(a0);
    a0 = v0;
    a3 = a0;
    a2 = s0;
    a1 = (int)s1 >> 2;
    v0 = a1;
    if((int)v0 > 0) {
      a1 = a1 - 0x1L;

      //LAB_800eaad4
      do {
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        a2 = a2 + 0x4L;
        v1 = a1;
        a1 = a1 - 0x1L;
        MEMORY.ref(4, a3).offset(0x0L).setu(v0);
        a3 = a3 + 0x4L;
      } while((int)v1 > 0);
    }

    //LAB_800eaaf0
    a1 = s1;
    a2 = 0;
    FUN_800e929c(a0, a1, a2);
    v0 = MEMORY.ref(4, s2).offset(0x10L).get();
    s1 = MEMORY.ref(4, s2).offset(0x14L).get();
    s0 = s2 + v0;
    a0 = s1;
    v0 = addToLinkedListTail(a0);
    a0 = v0;
    a3 = a0;
    a2 = s0;
    a1 = (int)s1 >> 2;
    v0 = a1;
    if((int)v0 > 0) {
      a1 = a1 - 0x1L;

      //LAB_800eab34
      do {
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        a2 = a2 + 0x4L;
        v1 = a1;
        a1 = a1 - 0x1L;
        MEMORY.ref(4, a3).offset(0x0L).setu(v0);
        a3 = a3 + 0x4L;
      } while((int)v1 > 0);
    }

    //LAB_800eab50
    v0 = 0x800c_0000L;
    a2 = MEMORY.ref(4, v0).offset(0x693cL).get();
    a1 = s1;
    a2 = a2 + 0x2cL;
    FUN_800e9288(a0, a1, a2);
    removeFromLinkedList(s2);
  }

  @Method(0x800eac58L)
  public static long FUN_800eac58(final long a0) {
    final long a2 = _800c693c.deref(4).offset(0x5acL).get();

    //LAB_800eac84
    for(int i = 0; i < MEMORY.ref(2, a2).offset(0x6L).get(); i++) {
      if(MEMORY.ref(4, a2).offset(i * 0x8L).offset(0x8L).get() == a0) {
        return a2 + MEMORY.ref(4, a2).offset(i * 0x8L).offset(0xcL).get();
      }

      //LAB_800eaca0
    }

    //LAB_800eacac
    return 0;
  }

  @Method(0x800eacf4L)
  public static void FUN_800eacf4() {
    loadDrgnBinFile(0, 4114, 0, getMethodAddress(Bttl_800e.class, "FUN_800eaa24", long.class, long.class, long.class), _800c693c.get() + 0x2cL, 0x4L);
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
  public static long FUN_800eaec8(final BttlScriptData6c data, final BttlScriptData6cSub1c sub) {
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

  @Method(0x800eb308L)
  public static void FUN_800eb308(final BttlScriptData6c a0, final long a1, final long a2) {
    if(MEMORY.ref(4, a1).offset(0x8L).get() != 0) {
      final long s2 = a1 + MEMORY.ref(4, a1).offset(0x8L).get();

      //LAB_800eb348
      for(int s1 = 0; s1 < 7; s1++) {
        final long s0 = s2 + MEMORY.ref(4, s2).offset(s1 * 0x4L).get();

        if((MEMORY.ref(2, s0).offset(0x0L).get() & 0x4000L) != 0) {
          final BttlScriptData6cSub1c sub = FUN_800e8dd4(a0, 0xaL, 0, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_800eaec8", BttlScriptData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1cL, BttlScriptData6cSub1c::new);

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
    FUN_80021b08(s2.objtable2_550, s2.dobj2s_00, s2.coord2s_a0, s2.params_3c0, 10);
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
  public static void FUN_800ebb58(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long lo;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(2, v0).offset(0x695cL).getSigned();
    t2 = 0;
    if((int)v0 > 0) {
      v0 = 0x800c_0000L;
      t7 = MEMORY.ref(4, v0).offset(0x6958L).get();
      a3 = 0;

      //LAB_800ebb7c
      do {
        t6 = t2 << 5;

        //LAB_800ebb80
        do {
          t5 = t7 + t6;
          t1 = MEMORY.ref(2, t5).offset(0x0L).get();

          v0 = t1 & 0x1fL;
          lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
          v1 = t1 << 16;
          a2 = lo;
          v0 = (int)v1 >> 21;
          v0 = v0 & 0x1fL;
          lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
          a1 = lo;
          v0 = (int)v1 >> 26;
          v0 = v0 & 0x1fL;
          lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
          t4 = (int)v1 >> 16;
          v0 = (int)a2 >> 4;
          t0 = v0 & 0x1fL;
          v0 = (int)a1 >> 4;
          a1 = v0 & 0x1fL;
          t3 = lo;
          v0 = (int)t3 >> 4;
          a2 = v0 & 0x1fL;
          v0 = t0 | a1;
          v0 = v0 | a2;
          t3 = v1 >>> 31;
          if(v0 != 0 || t4 == 0) {
            //LAB_800ebbf0
            v0 = a1 << 5;
            v0 = t0 | v0;
            v1 = a2 << 10;
            v0 = v0 | v1;
            v1 = t3 << 15;
            v0 = v0 | v1;
          } else {
            //LAB_800ebc0c
            v0 = -0x8000L;
            v0 = t1 & v0;
            v0 = v0 | 0x1L;
          }

          //LAB_800ebc18
          MEMORY.ref(2, t5).offset(0x800L).setu(v0);
          a3 = a3 + 0x1L;
          t6 = t6 + 0x2L;
        } while((int)a3 < 0x10L);

        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(2, v0).offset(0x695cL).getSigned();
        t2 = t2 + 0x1L;
        a3 = 0;
      } while((int)t2 < (int)v0);
    }

    //LAB_800ebc44
    if((int)t2 >= 0x40L) {
      v0 = 0x800c_0000L;
    } else {
      v0 = 0x800c_0000L;
      a1 = MEMORY.ref(4, v0).offset(0x6958L).get();
      a3 = 0;

      //LAB_800ebc58
      do {
        a0 = t2 << 5;

        //LAB_800ebc5c
        do {
          v0 = a1 + a0;
          v1 = MEMORY.ref(2, v0).offset(0x0L).get();
          a3 = a3 + 0x1L;
          MEMORY.ref(2, v0).offset(0x800L).setu(v1);
          a0 = a0 + 0x2L;
        } while((int)a3 < 0x10L);

        t2 = t2 + 0x1L;
        a3 = 0;
      } while((int)t2 < 0x40L);
    }

    //LAB_800ebc88
    a1 = 0;
    t2 = a1;
    v0 = 0x800c_0000L;
    t0 = MEMORY.ref(4, v0).offset(0x6958L).get();
    v0 = 0x8010_0000L;
    t1 = v0 - 0x4eb8L;
    a3 = 0;

    //LAB_800ebca4
    do {
      a2 = t2 + t1;
      v0 = a1 << 1;
      a0 = v0 + t0;

      //LAB_800ebcb0
      do {
        a1 = a1 + 0x1L;
        v1 = MEMORY.ref(1, a2).offset(0x0L).get();
        v0 = a3 << 1;
        v1 = v1 << 5;
        v0 = v0 + v1;
        v0 = t0 + v0;
        v0 = MEMORY.ref(2, v0).offset(0x800L).get();
        a3 = a3 + 0x1L;
        MEMORY.ref(2, a0).offset(0x1000L).setu(v0);
        a0 = a0 + 0x2L;
      } while((int)a3 < 0x10L);

      t2 = t2 + 0x1L;
      a3 = 0;
    } while((int)t2 < 0x40L);

    final RECT sp0x10 = new RECT((short)448, (short)240, (short)64, (short)16);
    a1 = _800c6958.get();
    a1 = a1 + 0x1000L;
    LoadImage(sp0x10, a1);
  }

  @Method(0x800ebd34L)
  public static void FUN_800ebd34(final BattleRenderStruct struct, final int index) {
    long v0;
    long a2;
    long t0;
    long t1;
    long s0;
    long s1;
    long s4;
    long s6;

    v0 = struct._5f0.get(index).get(); //TODO ptr to RECT?

    if(v0 == 0) {
      struct._618.get(index).set(0);
      return;
    }

    //LAB_800ebd84
    long x = MEMORY.ref(2, v0).offset(0x0L).get();
    long y = MEMORY.ref(2, v0).offset(0x2L).get();
    long w = MEMORY.ref(2, v0).offset(0x4L).get() >>> 2;
    long h = MEMORY.ref(2, v0).offset(0x6L).get();

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

  @Method(0x800ec0b0L)
  public static void FUN_800ec0b0(final GsDOBJ2 dobj2) {
    final TmdObjTable tmd = dobj2.tmd_08.deref();
    long primitives = tmd.primitives_10.getPointer();
    long count = tmd.n_primitive_14.get();
    long vertices = tmd.vert_top_00.get();
    long normals = tmd.normal_top_08.get();

    //LAB_800ec0ec
    while(count != 0) {
      final long primitiveCount = MEMORY.ref(2, primitives).get();
      count = count - MEMORY.ref(2, primitives).get();

      _1f8003ee.setu(MEMORY.ref(4, primitives).offset(0x0L).get() >>> 25 & 0x1L);

      final long command = MEMORY.ref(4, primitives).get() & 0xfd04_0000L;
      if(command == 0x3000_0000L) {
        //LAB_800ec190
        primitives = FUN_800edb4c(primitives, vertices, normals, primitiveCount);
      } else if(command == 0x3004_0000L) {
        //LAB_800ec1a4
        primitives = FUN_800ed414(primitives, vertices, normals, primitiveCount);
        //LAB_800ec14c
      } else if(command == 0x3400_0000L) {
        //LAB_800ec1bc
        primitives = FUN_800edd54(primitives, vertices, normals, primitiveCount);
      } else if(command == 0x3500_0000L) {
        //LAB_800ec1d0
        primitives = FUN_800ed90c(primitives, vertices, primitiveCount);
        //LAB_800ec15c
      } else if(command == 0x3800_0000L) {
        //LAB_800ec1e4
        primitives = FUN_800ecee8(primitives, vertices, normals, primitiveCount);
      } else if(command == 0x3804_0000L) {
        //LAB_800ec1f8
        primitives = FUN_800ed160(primitives, vertices, normals, primitiveCount);
        //LAB_800ec180
      } else if(command == 0x3c00_0000L) {
        //LAB_800ec210
        primitives = FUN_800edf80(primitives, vertices, normals, primitiveCount);
      } else {
        //LAB_800ec224
        primitives = FUN_800ed67c(primitives, vertices, primitiveCount);
      }

      //LAB_800ec234
    }

    //LAB_800ec23c
  }

  @Method(0x800ec258L)
  public static void FUN_800ec258(final BigStruct a0) {
    final BigStruct s2 = bigStruct_800bda10;

    GsInitCoordinate2(a0.coord2_14, s2.coord2_14);

    if(a0.ub_cc.get() != 0x3L) {
      s2.coord2_14.coord.transfer.setX(a0.vector_118.getX());

      if(a0.ub_cc.get() == 0x1L) {
        s2.coord2_14.coord.transfer.setY(a0.vector_118.getY());
      } else {
        //LAB_800ec2bc
        s2.coord2_14.coord.transfer.setY(a0.vector_118.getY() - (a0.coord2_14.coord.transfer.getY() << 12) / a0.scaleVector_fc.getY());
      }

      //LAB_800ec2e0
      s2.coord2_14.coord.transfer.setZ(a0.vector_118.getZ());
    } else {
      //LAB_800ec2ec
      s2.coord2_14.coord.transfer.setX(a0.vector_118.getX() + a0.coord2ArrPtr_04.deref().get(a0.ub_cd.get()).coord.transfer.getX());
      s2.coord2_14.coord.transfer.setY(a0.vector_118.getY() - (a0.coord2_14.coord.transfer.getY() << 12) / a0.scaleVector_fc.getY());
      s2.coord2_14.coord.transfer.setZ(a0.vector_118.getZ() + a0.coord2ArrPtr_04.deref().get(a0.ub_cd.get()).coord.transfer.getZ());
    }

    //LAB_800ec370
    s2.us_a0.set((short)(a0.us_a0.get() + 0x10));
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
    CPU.CTC2((sp0x10.get(1) & 0xffffL) << 16 | sp0x10.get(0) & 0xffffL, 0);
    CPU.CTC2((sp0x10.get(3) & 0xffffL) << 16 | sp0x10.get(2) & 0xffffL, 1);
    CPU.CTC2((sp0x10.get(5) & 0xffffL) << 16 | sp0x10.get(4) & 0xffffL, 2);
    CPU.CTC2((sp0x10.get(7) & 0xffffL) << 16 | sp0x10.get(6) & 0xffffL, 3);
    CPU.CTC2(                                  sp0x10.get(8) & 0xffffL, 4);
    CPU.CTC2(sp0x10.transfer.getX(), 5);
    CPU.CTC2(sp0x10.transfer.getY(), 6);
    CPU.CTC2(sp0x10.transfer.getZ(), 7);
    FUN_800ec0b0(s2.ObjTable_0c.top.deref().get(0));
    s2.coord2ArrPtr_04.deref().get(0).flg.decr();
  }

  @Method(0x800ec4bcL)
  public static void FUN_800ec4bc() {
    _800c6958.setu(addToLinkedListTail(0x1800L));
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
    _1f8003e8.setu(a0._5e8.get());

    //LAB_800ec5a0
    long s4 = 0x1L;
    for(int i = 0; i < a0.objtable2_550.nobj.get(); i++) {
      final GsDOBJ2 dobj2 = a0.objtable2_550.top.deref().get(i);
      if((s4 & a0._5e4.get()) == 0) {
        final MATRIX ls = new MATRIX();
        final MATRIX lw = new MATRIX();
        GsGetLws(dobj2.coord2_04.deref(), lw, ls);
        GsSetLightMatrix(lw);
        CPU.CTC2((ls.get(1) & 0xffff) << 16 | ls.get(0) & 0xffff, 0);
        CPU.CTC2((ls.get(3) & 0xffff) << 16 | ls.get(2) & 0xffff, 1);
        CPU.CTC2((ls.get(5) & 0xffff) << 16 | ls.get(4) & 0xffff, 2);
        CPU.CTC2((ls.get(7) & 0xffff) << 16 | ls.get(6) & 0xffff, 3);
        CPU.CTC2(                             ls.get(8) & 0xffff, 4);
        CPU.CTC2(ls.transfer.getX(), 5);
        CPU.CTC2(ls.transfer.getY(), 6);
        CPU.CTC2(ls.transfer.getZ(), 7);
        FUN_800ec0b0(dobj2);
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
      final RotateTranslateStruct rotTrans = a0.rotTrans_5d8.deref().get(i);
      final GsCOORDINATE2 coord2 = a0.dobj2s_00.get(i).coord2_04.deref();
      GsCOORD2PARAM param = coord2.param.deref();

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
    a0.rotTrans_5d4.set(a1.rotateTranslateArr_10);
    a0.rotTrans_5d8.set(a1.rotateTranslateArr_10);
    a0.rotTransCount_5dc.set((short)a1.count_0c.get());
    a0._5de.set(a1._0e.get());
    a0._5e0.set(0);
    FUN_800ec63c(a0);
    a0._5e0.set(1);
    a0._5e2.set(a0._5de.get());
    a0.rotTrans_5d8.set(a0.rotTrans_5d4.deref());
  }

  @Method(0x800ec7e4L)
  public static DVECTOR FUN_800ec7e4(final BigStruct a0, final short x, final short y, final short z) {
    final MATRIX sp0x20 = new MATRIX();
    GsGetLs(a0.coord2_14, sp0x20);
    setRotTransMatrix(sp0x20);

    final SVECTOR vxyz0 = new SVECTOR().set(x, y, z);
    final DVECTOR sxy2 = new DVECTOR();
    FUN_8003f900(vxyz0, sxy2, new Ref<>(), new Ref<>());
    return sxy2;
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
  public static void FUN_800ec8d0(long a0) {
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
    _1f8003e8.setu(a0.us_a0.get());

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
        CPU.CTC2((sp0x10.get(1) & 0xffffL) << 16 | sp0x10.get(0) & 0xffffL, 0);
        CPU.CTC2((sp0x10.get(3) & 0xffffL) << 16 | sp0x10.get(2) & 0xffffL, 1);
        CPU.CTC2((sp0x10.get(5) & 0xffffL) << 16 | sp0x10.get(4) & 0xffffL, 2);
        CPU.CTC2((sp0x10.get(7) & 0xffffL) << 16 | sp0x10.get(6) & 0xffffL, 3);
        CPU.CTC2(                                  sp0x10.get(8) & 0xffffL, 4);
        CPU.CTC2(sp0x10.transfer.getX(), 5);
        CPU.CTC2(sp0x10.transfer.getY(), 6);
        CPU.CTC2(sp0x10.transfer.getZ(), 7);
        FUN_800ec0b0(s2);
      }

      //LAB_800eca38
      s0 = s0 << 1;
      if(s0 == 0) {
        s0 = 0x1L;
        s6 = a0.ui_f8.get();
      }

      //LAB_800eca4c
    }

    //LAB_800eca58
    if(a0.ub_cc.get() != 0) {
      FUN_800ec258(a0);
    }

    //LAB_800eca70
  }

  @Method(0x800eca98L)
  public static void FUN_800eca98(long a0, long a1) {
    long v0;
    long v1;
    long s3 = a0;
    long s1 = 0;
    long s4 = s1;
    if(a1 != -0x1L) {
      if(s3 == 0) {
        //LAB_800ecb00
        s1 = _8006e398.offset(0xe40L).offset(a1 * 0x4L).get();
      } else if(s3 == 0x1L) {
        //LAB_800ecb1c
        s1 = _8006e398.offset(0xebcL).offset(a1 * 0x4L).get();
        //LAB_800ecaf0
      } else if(s3 == 0x2L) {
        //LAB_800ecb38
        s1 = _8006e398.offset(0xe0cL).offset(a1 * 0x4L).get();
      }

      //LAB_800ecb50
      //LAB_800ecb54
      final BtldScriptData27c a3 = scriptStatePtrArr_800bc1c0.get((int)s1).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
      v1 = a3._10.get();
      a0 = a3._08.get();
      v0 = v1 >>> 1;
      v0 = v0 < a0 ? 1 : 0;
      v1 = v1 >>> 2;
      if(v1 < a0) {
        a1 = v0 ^ 0x1L;
      } else {
        a1 = 0x2L;
      }

      //LAB_800ecb90
      FUN_800eccfc(a3._148, a1, s1, a3);
    } else {
      //LAB_800ecba4
      if(s3 == 0) {
        //LAB_800ecbdc
        s4 = _800c677c.get();
      } else if(s3 == 0x1L) {
        //LAB_800ecbec
        s4 = _800c6758.get();
        //LAB_800ecbc8
      } else if(s3 == 0x2L) {
        //LAB_800ecbfc
        s4 = _800c669c.get();
      }

      //LAB_800ecc04
      //LAB_800ecc1c
      for(int i = 0; i < s4; i++) {
        final long s0 = _8006e398.offset(i * 0x4L).getAddress();

        if(s3 == 0) {
          //LAB_800ecc50
          s1 = MEMORY.ref(4, s0).offset(0xe40L).get();
        } else if(s3 == 0x1L) {
          //LAB_800ecc5c
          s1 = MEMORY.ref(4, s0).offset(0xebcL).get();
          //LAB_800ecc40
        } else if(s3 == 0x2L) {
          //LAB_800ecc68
          s1 = MEMORY.ref(4, s0).offset(0xe78L).get();
        }

        //LAB_800ecc74
        //LAB_800ecc78
        final ScriptState<BtldScriptData27c> state = scriptStatePtrArr_800bc1c0.get((int)s1).derefAs(ScriptState.classFor(BtldScriptData27c.class));
        final BtldScriptData27c data = state.innerStruct_00.deref();

        v1 = data._10.get();
        a0 = data._08.get();
        v0 = v1 >>> 1;
        v0 = v0 < a0 ? 1 : 0;
        v1 = v1 >>> 2;
        if(v1 < a0) {
          a1 = v0 ^ 0x1L;
        } else {
          a1 = 0x2L;
        }

        //LAB_800eccac
        if((state.ui_60.get() & 0x4000L) == 0) {
          FUN_800eccfc(data._148, a1, s1, data);
        }

        //LAB_800eccc8
      }
    }

    //LAB_800eccd8
  }

  @Method(0x800eccfcL)
  public static void FUN_800eccfc(final BigStruct a0, long a1, long a2, final BtldScriptData27c data) {
    long v0;
    long v1;
    long s0;
    long a3;
    v0 = 0x800c_0000L;
    v0 = v0 - 0x3e40L;
    a2 = a2 << 2;
    a2 = a2 + v0;
    v0 = MEMORY.ref(4, a2).offset(0x0L).get();
    s0 = a1;
    v1 = MEMORY.ref(4, v0).offset(0x60L).get();

    v0 = v1 & 0x4L;
    if(v0 != 0) {
      v1 = data._78.getX();
      v0 = v1 << 1;
      v0 = v0 + v1;
      v0 = v0 << 3;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v1 = data._78.getY();
      a3 = -v0;
      v0 = v1 << 1;
      v0 = v0 + v1;
      v0 = v0 << 3;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v1 = data._78.getZ();
      a2 = -v0;
      v0 = v1 << 1;
      v0 = v0 + v1;
      v0 = v0 << 3;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v0 = -v0;
    } else {
      //LAB_800ecd90
      v0 = v1 & 0x2L;
      if(v0 != 0) {
        v0 = 0;
        a2 = -0x680L;
      } else {
        v0 = 0;

        //LAB_800ecda4
        a2 = -0x580L;
      }

      //LAB_800ecda8
      a3 = v0;
    }

    //LAB_800ecdac
    a1 = v0 << 16;
    a1 = (int)a1 >> 16;
    a2 = a2 << 16;
    a3 = a3 << 16;
    a2 = (int)a2 >> 16;
    a3 = (int)a3 >> 16;
    final DVECTOR v0_0 = FUN_800ec7e4(a0, (short)a1, (short)a2, (short)a3);
    a2 = 0x6680_0000L;
    a1 = linkedListAddress_1f8003d8.get();
    a2 = a2 | 0x8080L;
    v1 = a1 + 0x14L;
    linkedListAddress_1f8003d8.setu(v1);
    v1 = 0xf0L;
    MEMORY.ref(1, a1).offset(0xcL).setu(v1);
    v1 = 0x10L;
    MEMORY.ref(2, a1).offset(0x10L).setu(v1);
    v1 = 0x18L;
    MEMORY.ref(2, a1).offset(0x12L).setu(v1);
    v1 = v0_0.getX() - 0x8L;
    MEMORY.ref(2, a1).offset(0x8L).setu(v1);
    v1 = 0x800c_0000L;
    MEMORY.ref(1, a1).offset(0xdL).setu(0);
    v1 = MEMORY.ref(4, v1).offset(-0x4f04L).get();
    v0 = v0_0.getY();
    v1 = v1 & 0x7L;
    MEMORY.ref(1, a1).offset(0x3L).setu(0x4L);
    MEMORY.ref(4, a1).offset(0x4L).setu(a2);
    MEMORY.ref(1, a1).offset(0x4L).setu(0x80L);
    MEMORY.ref(1, a1).offset(0x5L).setu(0x80L);
    MEMORY.ref(1, a1).offset(0x6L).setu(0x80L);
    MEMORY.ref(2, a1).offset(0xaL).setu(_800fb188.offset(2, v1 * 0x2L).get() + v0);

    if(s0 == 0) {
      //LAB_800ece80
      MEMORY.ref(2, a1).offset(0xeL).setu(0x7fadL);
    } else if(s0 == 0x1L) {
      //LAB_800ece88
      MEMORY.ref(2, a1).offset(0xeL).setu(0x7fedL);
      //LAB_800ece70
    } else if(s0 == 0x2L) {
      //LAB_800ece90
      //LAB_800ece94
      MEMORY.ref(2, a1).offset(0xeL).setu(0x7c2eL);
    }

    //LAB_800ece9c
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x70L, a1);
    a1 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, a1).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, a1).offset(0x4L).setu(0xe100_021bL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x70L, a1);
    linkedListAddress_1f8003d8.addu(0x8L);
  }

  @Method(0x800ecee8L)
  public static long FUN_800ecee8(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long t8;
    long t9;
    long s8;
    t4 = a0;
    t7 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s8 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s0 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t9 = (int)t6 >> 15;
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      a0 = a0 + 0x14L;

      //LAB_800ecf34
      do {
        t5 = MEMORY.ref(2, t4).offset(0xaL).get();
        t6 = MEMORY.ref(2, t4).offset(0xeL).get();
        t7 = MEMORY.ref(2, t4).offset(0x12L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = a1 + t5;
        t6 = a1 + t6;
        t7 = a1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          t3 = CPU.MFC2(24);
          //LAB_800ecfb0
          if(t9 == 0 && (int)t3 > 0 || t9 != 0 && t3 != 0) {
            //LAB_800ecfb8
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x18L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a0).offset(0x2L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


            CPU.COP2(0x180001L);
            t3 = CPU.CFC2(31);

            if((int)t3 < 0) {
              v0 = t0 + 0x20L;
            } else {
              v0 = t0 + 0x20L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));


              CPU.COP2(0x168002eL);
              t1 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s2).offset(0x4L).get();
              t1 = t1 + s0;
              t1 = (int)t1 >> v0;
              if((int)t1 >= 0xbL) {
                v1 = MEMORY.ref(4, s1).offset(0x4L).get();

                if((int)t1 >= (int)v1) {
                  t1 = v1;
                }
                v0 = t1 << 2;

                //LAB_800ed040
                t2 = s8 + v0;
                v0 = t4 + 0x4L;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                t5 = MEMORY.ref(2, t4).offset(0x8L).get();
                t6 = MEMORY.ref(2, t4).offset(0xcL).get();
                t7 = MEMORY.ref(2, t4).offset(0x10L).get();
                t5 = t5 << 3;
                t6 = t6 << 3;
                t7 = t7 << 3;
                t5 = a2 + t5;
                t6 = a2 + t6;
                t7 = a2 + t7;
                CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
                CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
                CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
                CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
                CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


                CPU.COP2(0x118043fL);
                MEMORY.ref(4, t0).offset(0x4L).setu(CPU.MFC2(20));
                MEMORY.ref(4, t0).offset(0xcL).setu(CPU.MFC2(21));
                MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(22));
                v0 = MEMORY.ref(4, t2).offset(0x0L).get();
                v1 = 0x800_0000L;
                v0 = v0 & t8;
                v0 = v0 | v1;
                MEMORY.ref(4, t0).offset(0x0L).setu(v0);
                v0 = MEMORY.ref(2, a0).offset(0x0L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                CPU.COP2(0x108041bL);
                v0 = t0 & t8;
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                v0 = t0 + 0x1cL;
                MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                if(t9 == 0) {
                  t0 = t0 + 0x24L;
                } else {
                  t0 = t0 + 0x24L;
                  v1 = 0xe100_0000L;
                  v0 = 0x1L;
                  MEMORY.ref(1, t0).offset(0x3L).setu(v0);
                  v0 = 0x1f80_0000L;
                  v0 = MEMORY.ref(2, v0).offset(0x3ecL).get();
                  v1 = v1 | 0x200L;
                  v0 = v0 & 0x9ffL;
                  v0 = v0 | v1;
                  MEMORY.ref(4, t0).offset(0x4L).setu(v0);
                  v0 = MEMORY.ref(4, t2).offset(0x0L).get();
                  v1 = 0x100_0000L;
                  v0 = v0 & t8;
                  v0 = v0 | v1;
                  MEMORY.ref(4, t0).offset(0x0L).setu(v0);
                  v0 = t0 & t8;
                  t0 = t0 + 0x8L;
                  MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                }
              }
            }
          }
        }

        //LAB_800ed134
        a0 = a0 + 0x18L;

        //LAB_800ed138
        t4 = t4 + 0x18L;
      } while(a3 != 0);
    }

    //LAB_800ed140
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = t4;
    return v0;
  }

  @Method(0x800ed160L)
  public static long FUN_800ed160(long a0, long a1, long a2, long a3) {
    long at;
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    long sp8;
    t3 = a0;
    t7 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s8 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s0 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t9 = (int)t6 >> 15;
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      a0 = a0 + 0x20L;
      t4 = t1 + 0x4L;

      //LAB_800ed1b0
      do {
        t5 = MEMORY.ref(2, t3).offset(0x16L).get();
        t6 = MEMORY.ref(2, t3).offset(0x1aL).get();
        t7 = MEMORY.ref(2, t3).offset(0x1eL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = a1 + t5;
        t6 = a1 + t6;
        t7 = a1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t0 = CPU.CFC2(31);

        if((int)t0 >= 0) {
          CPU.COP2(0x1400006L);
          t0 = CPU.MFC2(24);
          //LAB_800ed22c
          if(t9 == 0 && (int)t0 > 0 || t9 != 0 && t0 != 0) {
            //LAB_800ed234
            MEMORY.ref(4, t1).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x18L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a0).offset(0x2L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


            CPU.COP2(0x180001L);
            t0 = CPU.CFC2(31);

            if((int)t0 < 0) {
              v0 = t1 + 0x20L;
            } else {
              v0 = t1 + 0x20L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));


              CPU.COP2(0x168002eL);
              t0 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s2).offset(0x4L).get();
              t0 = t0 + s0;
              t0 = (int)t0 >> v0;
              if((int)t0 < 0xbL) {
                v0 = t3 + 0x4L;
              } else {
                v0 = t3 + 0x4L;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                v0 = MEMORY.ref(2, a0).offset(-0xcL).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                v1 = MEMORY.ref(4, s1).offset(0x4L).get();

                if((int)t0 >= (int)v1) {
                  t0 = v1;
                }

                //LAB_800ed2d4
                CPU.COP2(0x108041bL);
                MEMORY.ref(4, t4).offset(0x0L).setu(CPU.MFC2(22));
                v0 = t3 + 0x8L;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                v0 = MEMORY.ref(2, a0).offset(-0x8L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                v0 = t0 << 2;
                t2 = s8 + v0;
                CPU.COP2(0x108041bL);
                v0 = t1 + 0xcL;
                MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                v0 = t3 + 0xcL;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                v0 = MEMORY.ref(2, a0).offset(-0x4L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                CPU.COP2(0x108041bL);
                v0 = t1 + 0x14L;
                MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                v0 = t3 + 0x10L;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                v0 = MEMORY.ref(2, a0).offset(0x0L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                CPU.COP2(0x108041bL);
                v0 = t1 + 0x1cL;
                MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                t4 = t4 + 0x24L;
                v1 = MEMORY.ref(4, t2).offset(0x0L).get();
                v0 = 0x800_0000L;
                v1 = v1 & t8;
                v1 = v1 | v0;
                v0 = t1 & t8;
                MEMORY.ref(4, t1).offset(0x0L).setu(v1);
                t1 = t1 + 0x24L;
                if(t9 == 0) {
                  MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                } else {
                  MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                  v1 = 0xe100_0000L;
                  v0 = 0x1L;
                  MEMORY.ref(1, t4).offset(-0x1L).setu(v0);
                  v0 = 0x1f80_0000L;
                  v0 = MEMORY.ref(2, v0).offset(0x3ecL).get();
                  v1 = v1 | 0x200L;
                  v0 = v0 & 0x9ffL;
                  v0 = v0 | v1;
                  MEMORY.ref(4, t4).offset(0x0L).setu(v0);
                  t4 = t4 + 0x8L;
                  v1 = MEMORY.ref(4, t2).offset(0x0L).get();
                  v0 = 0x100_0000L;
                  v1 = v1 & t8;
                  v1 = v1 | v0;
                  v0 = t1 & t8;
                  MEMORY.ref(4, t1).offset(0x0L).setu(v1);
                  t1 = t1 + 0x8L;
                  MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                }
              }
            }
          }
        }

        //LAB_800ed3e8
        a0 = a0 + 0x24L;

        //LAB_800ed3ec
        t3 = t3 + 0x24L;
      } while(a3 != 0);
    }

    //LAB_800ed3f4
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    v0 = t3;
    return v0;
  }

  @Method(0x800ed414L)
  public static long FUN_800ed414(long a0, long a1, long a2, long a3) {
    long at;
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    long spc;
    long sp8;
    t4 = a0;
    s3 = a1;
    t7 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s0 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s1 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    s8 = (int)t6 >> 15;
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      a0 = a0 + 0x18L;
      t3 = t0 + 0x4L;

      //LAB_800ed46c
      do {
        t5 = MEMORY.ref(2, t4).offset(0x12L).get();
        t6 = MEMORY.ref(2, t4).offset(0x16L).get();
        t7 = MEMORY.ref(2, t4).offset(0x1aL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s3 + t5;
        t6 = s3 + t6;
        t7 = s3 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t8 = CPU.CFC2(31);

        if((int)t8 >= 0) {
          CPU.COP2(0x1400006L);
          t8 = CPU.MFC2(24);
          //LAB_800ed4e8
          if(s8 == 0 && (int)t8 > 0 || s8 != 0 && t8 != 0) {
            //LAB_800ed4f0
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x18L).setu(CPU.MFC2(14));


            CPU.COP2(0x158002dL);
            t1 = CPU.MFC2(7);
            v0 = MEMORY.ref(4, s2).offset(0x4L).get();
            t1 = t1 + s1;
            t1 = (int)t1 >> v0;
            if((int)t1 >= 0xbL) {
              v1 = MEMORY.ref(4, a1).offset(0x4L).get();

              if((int)t1 >= (int)v1) {
                t1 = v1;
              }
              v0 = t1 << 2;

              //LAB_800ed540
              t2 = s0 + v0;
              v0 = t4 + 0x4L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a0).offset(-0x8L).get();

              v0 = v0 << 3;
              v0 = a2 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


              CPU.COP2(0x108041bL);
              MEMORY.ref(4, t3).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t4 + 0x8L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a0).offset(-0x4L).get();

              v0 = v0 << 3;
              v0 = a2 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


              CPU.COP2(0x108041bL);
              v0 = t0 + 0xcL;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t4 + 0xcL;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a0).offset(0x0L).get();

              v0 = v0 << 3;
              v0 = a2 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


              CPU.COP2(0x108041bL);
              v0 = t0 + 0x14L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              t3 = t3 + 0x1cL;
              v1 = MEMORY.ref(4, t2).offset(0x0L).get();
              v0 = 0x600_0000L;
              v1 = v1 & t9;
              v1 = v1 | v0;
              v0 = t0 & t9;
              MEMORY.ref(4, t0).offset(0x0L).setu(v1);
              t0 = t0 + 0x1cL;
              if(s8 == 0) {
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
              } else {
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                v1 = 0xe100_0000L;
                v0 = 0x1L;
                MEMORY.ref(1, t3).offset(-0x1L).setu(v0);
                v0 = 0x1f80_0000L;
                v0 = MEMORY.ref(2, v0).offset(0x3ecL).get();
                v1 = v1 | 0x200L;
                v0 = v0 & 0x9ffL;
                v0 = v0 | v1;
                MEMORY.ref(4, t3).offset(0x0L).setu(v0);
                t3 = t3 + 0x8L;
                v1 = MEMORY.ref(4, t2).offset(0x0L).get();
                v0 = 0x100_0000L;
                v1 = v1 & t9;
                v1 = v1 | v0;
                v0 = t0 & t9;
                MEMORY.ref(4, t0).offset(0x0L).setu(v1);
                t0 = t0 + 0x8L;
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
              }
            }
          }
        }

        //LAB_800ed64c
        a0 = a0 + 0x1cL;

        //LAB_800ed650
        t4 = t4 + 0x1cL;
      } while(a3 != 0);
    }

    //LAB_800ed658
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = t4;
    return v0;
  }

  @Method(0x800ed67cL)
  public static long FUN_800ed67c(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long t8;
    long t9;
    long s8;
    t4 = a0;
    t7 = 0x1f80_0000L;
    t2 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s8 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s0 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t7 = (int)t6 >> 16;
    t9 = t7 << 25;
    if(a2 != 0) {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t0 = a0 + 0x22L;
      a3 = t2 + 0x2aL;

      //LAB_800ed6d0
      do {
        t5 = MEMORY.ref(2, t4).offset(0x24L).get();
        t6 = MEMORY.ref(2, t4).offset(0x26L).get();
        t7 = MEMORY.ref(2, t4).offset(0x28L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = a1 + t5;
        t6 = a1 + t6;
        t7 = a1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        v0 = MEMORY.ref(4, t0-0x1eL).get();

        MEMORY.ref(4, a3-0x1eL).setu(v0);
        v0 = MEMORY.ref(4, t0-0x1aL).get();
        a2 = a2 - 0x1L;
        MEMORY.ref(4, a3-0x12L).setu(v0);
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, t0-0x16L).get();

          MEMORY.ref(4, a3-0x6L).setu(v0);
          t3 = CPU.MFC2(24);
          //LAB_800ed76c
          if(t9 == 0 && (int)t3 > 0 || t3 != 0) {
            //LAB_800ed774
            MEMORY.ref(4, t2).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t2).offset(0x20L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, t0).offset(0x8L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x180001L);
            a0 = 0x3c80_0000L;
            a0 = a0 | 0x8080L;
            v1 = MEMORY.ref(4, t0-0x12L).get();
            v0 = 0xcL;
            MEMORY.ref(1, a3).offset(-0x27L).setu(v0);
            MEMORY.ref(4, a3-0x26L).setu(a0);
            MEMORY.ref(4, a3+0x6L).setu(v1);
            t7 = MEMORY.ref(4, t2).offset(0x4L).get();

            t7 = t7 | t9;
            MEMORY.ref(4, t2).offset(0x4L).setu(t7);
            t3 = CPU.CFC2(31);

            if((int)t3 >= 0) {
              v0 = t2 + 0x2cL;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));


              CPU.COP2(0x168002eL);
              MEMORY.ref(1, a3).offset(-0x26L).setu(MEMORY.ref(1, t0).offset(-0xeL).get());
              MEMORY.ref(1, a3).offset(-0x25L).setu(MEMORY.ref(1, t0).offset(-0xdL).get());
              MEMORY.ref(1, a3).offset(-0x24L).setu(MEMORY.ref(1, t0).offset(-0xcL).get());
              MEMORY.ref(1, a3).offset(-0x1aL).setu(MEMORY.ref(1, t0).offset(-0xaL).get());
              MEMORY.ref(1, a3).offset(-0x19L).setu(MEMORY.ref(1, t0).offset(-0x9L).get());
              MEMORY.ref(1, a3).offset(-0x18L).setu(MEMORY.ref(1, t0).offset(-0x8L).get());
              MEMORY.ref(1, a3).offset(-0xeL).setu(MEMORY.ref(1, t0).offset(-0x6L).get());
              MEMORY.ref(1, a3).offset(-0xdL).setu(MEMORY.ref(1, t0).offset(-0x5L).get());
              MEMORY.ref(1, a3).offset(-0xcL).setu(MEMORY.ref(1, t0).offset(-0x4L).get());
              MEMORY.ref(1, a3).offset(-0x2L).setu(MEMORY.ref(1, t0).offset(-0x2L).get());
              MEMORY.ref(1, a3).offset(-0x1L).setu(MEMORY.ref(1, t0).offset(-0x1L).get());
              MEMORY.ref(1, a3).offset(0x0L).setu(MEMORY.ref(1, t0).offset(0x0L).get());

              t1 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s2).offset(0x4L).get();
              t1 = t1 + s0;
              t1 = (int)t1 >> v0;

              if((int)t1 >= 0xbL) {
                v1 = MEMORY.ref(4, s1).offset(0x4L).get();

                if((int)t1 >= (int)v1) {
                  t1 = v1;
                }
                a0 = t1 << 2;

                //LAB_800ed8b8
                a0 = s8 + a0;
                a3 = a3 + 0x34L;
                v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                v0 = 0xc00_0000L;
                v1 = v1 & t8;
                v1 = v1 | v0;
                v0 = t2 & t8;
                MEMORY.ref(4, t2).offset(0x0L).setu(v1);
                t2 = t2 + 0x34L;
                MEMORY.ref(4, a0).offset(0x0L).setu(v0);
              }
            }
          }
        }

        //LAB_800ed8e0
        t0 = t0 + 0x2cL;

        //LAB_800ed8e4
        t4 = t4 + 0x2cL;
      } while(a2 != 0);
    }

    //LAB_800ed8ec
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t2);
    v0 = t4;
    return v0;
  }

  @Method(0x800ed90cL)
  public static long FUN_800ed90c(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long t8;
    long t9;
    long s8;
    t4 = a0;
    s2 = a1;
    t7 = 0x1f80_0000L;
    t2 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s8 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s0 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t7 = (int)t6 >> 16;
    t9 = t7 << 25;
    if(a2 != 0) {
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t0 = a0 + 0x1aL;
      a3 = t2 + 0x1eL;

      //LAB_800ed964
      do {
        t5 = MEMORY.ref(2, t4).offset(0x1cL).get();
        t6 = MEMORY.ref(2, t4).offset(0x1eL).get();
        t7 = MEMORY.ref(2, t4).offset(0x20L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s2 + t5;
        t6 = s2 + t6;
        t7 = s2 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


        CPU.COP2(0x280030L);
        v0 = MEMORY.ref(4, t0-0x16L).get();

        MEMORY.ref(4, a3-0x12L).setu(v0);
        v0 = MEMORY.ref(4, t0-0x12L).get();
        a2 = a2 - 0x1L;
        MEMORY.ref(4, a3-0x6L).setu(v0);
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, t0-0xeL).get();

          MEMORY.ref(4, a3+0x6L).setu(v0);
          t3 = CPU.MFC2(24);
          //LAB_800eda00
          if(t9 == 0 && (int)t3 > 0 || t3 != 0) {
            //LAB_800eda08
            MEMORY.ref(4, t2).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t2).offset(0x20L).setu(CPU.MFC2(14));
            v1 = 0x3480_0000L;
            v1 = v1 | 0x8080L;
            v0 = 0x9L;
            MEMORY.ref(1, a3).offset(-0x1bL).setu(v0);
            MEMORY.ref(4, a3-0x1aL).setu(v1);
            t7 = MEMORY.ref(4, t2).offset(0x4L).get();

            t7 = t7 | t9;
            MEMORY.ref(4, t2).offset(0x4L).setu(t7);
            t3 = CPU.CFC2(31);

            if((int)t3 >= 0) {
              CPU.COP2(0x158002dL);
              v0 = MEMORY.ref(1, t0).offset(-0xaL).get();

              MEMORY.ref(1, a3).offset(-0x1aL).setu(v0);
              v0 = MEMORY.ref(1, t0).offset(-0x9L).get();

              MEMORY.ref(1, a3).offset(-0x19L).setu(v0);
              v0 = MEMORY.ref(1, t0).offset(-0x8L).get();

              MEMORY.ref(1, a3).offset(-0x18L).setu(v0);
              v0 = MEMORY.ref(1, t0).offset(-0x6L).get();

              MEMORY.ref(1, a3).offset(-0xeL).setu(v0);
              v0 = MEMORY.ref(1, t0).offset(-0x5L).get();

              MEMORY.ref(1, a3).offset(-0xdL).setu(v0);
              v0 = MEMORY.ref(1, t0).offset(-0x4L).get();

              MEMORY.ref(1, a3).offset(-0xcL).setu(v0);
              v0 = MEMORY.ref(1, t0).offset(-0x2L).get();

              MEMORY.ref(1, a3).offset(-0x2L).setu(v0);
              v0 = MEMORY.ref(1, t0).offset(-0x1L).get();

              MEMORY.ref(1, a3).offset(-0x1L).setu(v0);
              v0 = MEMORY.ref(1, t0).offset(0x0L).get();

              MEMORY.ref(1, a3).offset(0x0L).setu(v0);
              t1 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s1).offset(0x4L).get();
              t1 = t1 + s0;
              t1 = (int)t1 >> v0;
              if((int)t1 >= 0xbL) {
                v1 = MEMORY.ref(4, a1).offset(0x4L).get();

                if((int)t1 >= (int)v1) {
                  t1 = v1;
                }
                a0 = t1 << 2;

                //LAB_800edaf8
                a0 = s8 + a0;
                a3 = a3 + 0x28L;
                v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                v0 = 0x900_0000L;
                v1 = v1 & t8;
                v1 = v1 | v0;
                v0 = t2 & t8;
                MEMORY.ref(4, t2).offset(0x0L).setu(v1);
                t2 = t2 + 0x28L;
                MEMORY.ref(4, a0).offset(0x0L).setu(v0);
              }
            }
          }
        }

        //LAB_800edb20
        t0 = t0 + 0x24L;

        //LAB_800edb24
        t4 = t4 + 0x24L;
      } while(a2 != 0);
    }

    //LAB_800edb2c
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t2);
    v0 = t4;
    return v0;
  }

  @Method(0x800edb4cL)
  public static long FUN_800edb4c(long a0, long a1, long a2, long a3) {
    long at;
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s4;
    long s5;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    s0 = a1;
    s1 = a2;
    t7 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    t9 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s8 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t8 = (int)t6 >> 15;
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      a2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t4 = 0xff_0000L;
      t4 = t4 | 0xffffL;

      //LAB_800edb94
      do {
        t5 = MEMORY.ref(2, a0).offset(0xaL).get();
        t6 = MEMORY.ref(2, a0).offset(0xeL).get();
        t7 = MEMORY.ref(2, a0).offset(0x12L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s0 + t5;
        t6 = s0 + t6;
        t7 = s0 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          t3 = CPU.MFC2(24);

          //LAB_800edc10
          if(t8 == 0 && t3 > 0 || t8 != 0 && t3 != 0) {
            //LAB_800edc18
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x18L).setu(CPU.MFC2(14));


            CPU.COP2(0x158002dL);
            t1 = CPU.MFC2(7);
            v0 = MEMORY.ref(4, a2).offset(0x4L).get();
            t1 = t1 + s8;
            t1 = (int)t1 >> v0;
            if((int)t1 >= 0xbL) {
              v1 = MEMORY.ref(4, a1).offset(0x4L).get();

              if((int)t1 >= (int)v1) {
                t1 = v1;
              }
              v0 = t1 << 2;

              //LAB_800edc68
              t2 = t9 + v0;
              v0 = a0 + 0x4L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              t5 = MEMORY.ref(2, a0).offset(0x8L).get();
              t6 = MEMORY.ref(2, a0).offset(0xcL).get();
              t7 = MEMORY.ref(2, a0).offset(0x10L).get();
              t5 = t5 << 3;
              t6 = t6 << 3;
              t7 = t7 << 3;
              t5 = s1 + t5;
              t6 = s1 + t6;
              t7 = s1 + t7;
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


              CPU.COP2(0x118043fL);
              v0 = MEMORY.ref(4, t2).offset(0x0L).get();
              v1 = 0x600_0000L;
              v0 = v0 & t4;
              v0 = v0 | v1;
              MEMORY.ref(4, t0).offset(0x0L).setu(v0);
              v0 = t0 & t4;
              MEMORY.ref(4, t2).offset(0x0L).setu(v0);
              MEMORY.ref(4, t0).offset(0x4L).setu(CPU.MFC2(20));
              MEMORY.ref(4, t0).offset(0xcL).setu(CPU.MFC2(21));
              MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(22));
              if(t8 == 0) {
                t0 = t0 + 0x1cL;
              } else {
                t0 = t0 + 0x1cL;
                v1 = 0xe100_0000L;
                v0 = 0x1L;
                MEMORY.ref(1, t0).offset(0x3L).setu(v0);
                v0 = 0x1f80_0000L;
                v0 = MEMORY.ref(2, v0).offset(0x3ecL).get();
                v1 = v1 | 0x200L;
                v0 = v0 & 0x9ffL;
                v0 = v0 | v1;
                MEMORY.ref(4, t0).offset(0x4L).setu(v0);
                v0 = MEMORY.ref(4, t2).offset(0x0L).get();
                v1 = 0x100_0000L;
                v0 = v0 & t4;
                v0 = v0 | v1;
                MEMORY.ref(4, t0).offset(0x0L).setu(v0);
                v0 = t0 & t4;
                t0 = t0 + 0x8L;
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
              }
            }
          }
        }

        //LAB_800edd30
        a0 = a0 + 0x14L;
      } while(a3 != 0);
    }

    //LAB_800edd38
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = a0;
    return v0;
  }

  @Method(0x800edd54L)
  public static long FUN_800edd54(long a0, long a1, long a2, long a3) {
    long at;
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    long spc;
    long sp8;
    t8 = a0;
    s2 = a1;
    s3 = a2;
    t7 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s0 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s1 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t7 = (int)t6 >> 16;
    s8 = t7 << 25;
    v0 = 0x3480_0000L;
    v0 = v0 | 0x8080L;
    v1 = 0x9L;
    MEMORY.ref(1, t0).offset(0x3L).setu(v1);
    MEMORY.ref(4, t0).offset(0x4L).setu(v0);
    t7 = MEMORY.ref(4, t0).offset(0x4L).get();

    t7 = t7 | s8;
    MEMORY.ref(4, t0).offset(0x4L).setu(t7);
    v0 = t0 + 0x4L;
    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      a2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      t3 = a0 + 0xcL;
      t2 = t0 + 0x24L;

      //LAB_800edde0
      do {
        t5 = MEMORY.ref(2, t8).offset(0x12L).get();
        t6 = MEMORY.ref(2, t8).offset(0x16L).get();
        t7 = MEMORY.ref(2, t8).offset(0x1aL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s2 + t5;
        t6 = s2 + t6;
        t7 = s2 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


        CPU.COP2(0x280030L);
        v0 = MEMORY.ref(4, t3).offset(-0x8L).get();

        MEMORY.ref(4, t2).offset(-0x18L).setu(v0);
        v0 = MEMORY.ref(4, t3).offset(-0x4L).get();
        a3 = a3 - 0x1L;
        MEMORY.ref(4, t2).offset(-0xcL).setu(v0);
        t4 = CPU.CFC2(31);

        if((int)t4 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, t3).offset(0x0L).get();

          MEMORY.ref(4, t2).offset(0x0L).setu(v0);
          t4 = CPU.MFC2(24);
          //LAB_800ede7c
          if(s8 == 0 && (int)t4 > 0 || s8 != 0 && t4 != 0) {
            //LAB_800ede84
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x20L).setu(CPU.MFC2(14));


            CPU.COP2(0x158002dL);
            t1 = CPU.MFC2(7);
            v0 = MEMORY.ref(4, a2).offset(0x4L).get();
            t1 = t1 + s1;
            t1 = (int)t1 >> v0;
            if((int)t1 >= 0xbL) {
              v1 = MEMORY.ref(4, a1).offset(0x4L).get();

              if((int)t1 >= (int)v1) {
                t1 = v1;
              }
              a0 = t1 << 2;

              //LAB_800eded4
              a0 = s0 + a0;
              t5 = MEMORY.ref(2, t8).offset(0x10L).get();
              t6 = MEMORY.ref(2, t8).offset(0x14L).get();
              t7 = MEMORY.ref(2, t8).offset(0x18L).get();
              t5 = t5 << 3;
              t6 = t6 << 3;
              t7 = t7 << 3;
              t5 = s3 + t5;
              t6 = s3 + t6;
              t7 = s3 + t7;
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


              CPU.COP2(0x118043fL);
              MEMORY.ref(4, t0).offset(0x4L).setu(CPU.MFC2(20));
              MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(21));
              MEMORY.ref(4, t0).offset(0x1cL).setu(CPU.MFC2(22));
              t2 = t2 + 0x28L;
              v1 = MEMORY.ref(4, a0).offset(0x0L).get();
              v0 = 0x900_0000L;
              v1 = v1 & t9;
              v1 = v1 | v0;
              v0 = t0 & t9;
              MEMORY.ref(4, t0).offset(0x0L).setu(v1);
              t0 = t0 + 0x28L;
              MEMORY.ref(4, a0).offset(0x0L).setu(v0);
            }
          }
        }

        //LAB_800edf50
        t3 = t3 + 0x1cL;

        //LAB_800edf54
        t8 = t8 + 0x1cL;
      } while(a3 != 0);
    }

    //LAB_800edf5c
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = t8;
    return v0;
  }

  @Method(0x800edf80L)
  public static long FUN_800edf80(long a0, long a1, long a2, long a3) {
    long at;
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    long spc;
    long sp8;
    t8 = a0;
    t7 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s0 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s1 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t7 = (int)t6 >> 16;
    s8 = t7 << 25;
    v0 = 0x3c80_0000L;
    v0 = v0 | 0x8080L;
    v1 = 0xcL;
    MEMORY.ref(1, t1).offset(0x3L).setu(v1);
    MEMORY.ref(4, t1).offset(0x4L).setu(v0);
    t7 = MEMORY.ref(4, t1).offset(0x4L).get();

    t7 = t7 | s8;
    MEMORY.ref(4, t1).offset(0x4L).setu(t7);
    v0 = t1 + 0x4L;
    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s3 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      t0 = a0 + 0x20L;
      t3 = t1 + 0x28L;

      //LAB_800ee004
      do {
        t5 = MEMORY.ref(2, t8).offset(0x16L).get();
        t6 = MEMORY.ref(2, t8).offset(0x1aL).get();
        t7 = MEMORY.ref(2, t8).offset(0x1eL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = a1 + t5;
        t6 = a1 + t6;
        t7 = a1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


        CPU.COP2(0x280030L);
        v0 = MEMORY.ref(4, t0-0x1cL).get();

        MEMORY.ref(4, t3-0x1cL).setu(v0);
        v0 = MEMORY.ref(4, t0-0x18L).get();
        a3 = a3 - 0x1L;
        MEMORY.ref(4, t3-0x10L).setu(v0);
        t4 = CPU.CFC2(31);

        if((int)t4 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, t0-0x14L).get();

          MEMORY.ref(4, t3-0x4L).setu(v0);
          t4 = CPU.MFC2(24);
          //LAB_800ee0a0
          if(s8 != 0 || (int)t4 > 0 || s8 == 0 && t4 != 0) {
            //LAB_800ee0a8
            MEMORY.ref(4, t1).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x20L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, t0).offset(0x2L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


            CPU.COP2(0x180001L);
            t4 = CPU.CFC2(31);

            if((int)t4 < 0) {
              v0 = t1 + 0x2cL;
            } else {
              v0 = t1 + 0x2cL;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));


              CPU.COP2(0x168002eL);
              v0 = MEMORY.ref(4, t0-0x10L).get();

              MEMORY.ref(4, t3).offset(0x8L).setu(v0);
              t2 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s3).offset(0x4L).get();
              t2 = t2 + s1;
              t2 = (int)t2 >> v0;
              if((int)t2 >= 0xbL) {
                v1 = MEMORY.ref(4, s2).offset(0x4L).get();

                if((int)t2 >= (int)v1) {
                  t2 = v1;
                }
                a0 = t2 << 2;

                //LAB_800ee13c
                a0 = s0 + a0;
                t5 = MEMORY.ref(2, t8).offset(0x14L).get();
                t6 = MEMORY.ref(2, t8).offset(0x18L).get();
                t7 = MEMORY.ref(2, t8).offset(0x1cL).get();
                t5 = t5 << 3;
                t6 = t6 << 3;
                t7 = t7 << 3;
                t5 = a2 + t5;
                t6 = a2 + t6;
                t7 = a2 + t7;
                CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
                CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
                CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
                CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
                CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


                CPU.COP2(0x118043fL);
                MEMORY.ref(4, t1).offset(0x4L).setu(CPU.MFC2(20));
                MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(21));
                MEMORY.ref(4, t1).offset(0x1cL).setu(CPU.MFC2(22));
                v0 = MEMORY.ref(2, t0).offset(0x0L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                CPU.COP2(0x108041bL);
                v0 = MEMORY.ref(4, a0).offset(0x0L).get();
                v1 = 0xc00_0000L;
                v0 = v0 & t9;
                v0 = v0 | v1;
                MEMORY.ref(4, t1).offset(0x0L).setu(v0);
                v0 = t1 & t9;
                MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                MEMORY.ref(4, t3).offset(0x0L).setu(CPU.MFC2(22));
                t3 = t3 + 0x34L;
                t1 = t1 + 0x34L;
              }
            }
          }
        }

        //LAB_800ee1e0
        t0 = t0 + 0x24L;

        //LAB_800ee1e4
        t8 = t8 + 0x24L;
      } while(a3 != 0);
    }

    //LAB_800ee1ec
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    v0 = t8;
    return v0;
  }

  @Method(0x800ee3c0L)
  public static long FUN_800ee3c0(final RunningScript a0) {
    final BtldScriptData27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    v1._214.set(0x3);
    v1._215.set(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800ee49cL)
  public static long FUN_800ee49c(final RunningScript a0) {
    final BtldScriptData27c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    a1._254.set(a0.params_20.get(1).deref().get());
    a1._25c.set(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800ee610L)
  public static void FUN_800ee610() {
    final int[] sp0x10 = new int[9];
    for(int i = 0; i < 9; i++) {
      sp0x10[i] = (int)_800c6e34.offset(i * 0x2L).get(); //2b
    }

    _800c6cf4.setu(0);
    _800c6c38.setu(0x1L);
    _800c6c2c.setu(addToLinkedListTail(0x3ccL));
    _800c6b5c.setu(addToLinkedListTail(0x930L));
    _800c6b60.setu(addToLinkedListTail(0xa4L));
    _800c6c34.setu(addToLinkedListTail(0x58L));
    _800c6b6c.setu(addToLinkedListTail(0x3cL));

    FUN_800ef7c4();
    FUN_800f4964();

    final long v0 = _800c6b60.get();
    MEMORY.ref(2, v0).offset(0x26L).setu(0);
    MEMORY.ref(2, v0).offset(0x28L).setu(0);
    MEMORY.ref(2, v0).offset(0x2aL).setu(0);
    MEMORY.ref(4, v0).offset(0x2cL).setu(0);
    MEMORY.ref(2, v0).offset(0x30L).setu(0);

    FUN_800f60ac();
    FUN_800f9584();

    _800c6b9c.setu(0);
    _800c69c8.setu(0);
    _800c6b68.setu(0);

    //LAB_800ee764
    for(int i = 0; i < 9; i++) {
      _800c6b78.offset(i * 0x4L).setu(-0x1L);

      //LAB_800ee770
      for(int v1 = 0; v1 < 22; v1++) {
        _800c69d0.get(i).charAt(v1, 0xa0ffL);
      }
    }

    //LAB_800ee7b0
    for(int i = 0; i < 3; i++) {
      //LAB_800ee7b8
      for(int v1 = 0; v1 < 22; v1++) {
        _800c6ba8.get(i).charAt(v1, 0xa0ffL);
      }
    }

    FUN_80023264();

    _800c6c3c.setu(0);

    //LAB_800ee80c
    for(int i = 0; i < 9; i++) {
      //LAB_800ee824
      for(int v1 = 0; v1 < gameState_800babc8._1e6.get(); v1++) {
        if(gameState_800babc8._2e9.get(v1).get() == sp0x10[i]) {
          _800c6c3c.oru(0x1L << i);
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
    for(int i = 0; i < 3; i++) {
      _800bc950.offset(i * 0x4L).setu(0);
    }

    FUN_80023a88();
    FUN_800f83c8();
  }

  @Method(0x800ee8c4L)
  public static void FUN_800ee8c4(final long address, final long fileSize, final long param) {
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

  @Method(0x800eee80L)
  public static void FUN_800eee80(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t2;
    long t3;
    long t4;
    long t8;
    t4 = a0;
    v0 = 0x800c_0000L;
    t8 = v0 + 0x6e90L;

    final long[] sp0x10 = {
      MEMORY.ref(4, t8).offset(0x0L).get(),
      MEMORY.ref(4, t8).offset(0x4L).get(),
      MEMORY.ref(4, t8).offset(0x8L).get(),
    };

    v0 = 0x8011_0000L;
    t3 = v0 + 0x2068L;
    v0 = 0x800c_0000L;
    t2 = v0 + 0x6ba8L;
    a3 = 0;

    //LAB_800eeecc
    for(int i = 0; i < 3; i++) {
      a1 = a3;
      a0 = MEMORY.ref(4, t3).offset(sp0x10[i] * 0x4L).get();

      //LAB_800eeee0
      do {
        MEMORY.ref(2, t2).offset(a1).setu(MEMORY.ref(2, a0).get());

        if(MEMORY.ref(2, a0).get() > 0xa0feL) {
          break;
        }

        a1 = a1 + 0x2L;
        a0 = a0 + 0x2L;
      } while(true);

      //LAB_800eef0c
      a3 = a3 + 0x2cL;
    }

    a0 = 0x800c_0000L;
    v0 = 0x800c_0000L;
    t2 = v0 + 0x69d0L;
    a0 = a0 - 0x3e40L;
    v1 = t4 << 2;
    t3 = 0x800c_0000L;
    v1 = v1 + a0;
    a3 = MEMORY.ref(4, t3).offset(0x6b9cL).get();
    v1 = MEMORY.ref(4, v1).offset(0x0L).get();
    v0 = a3 << 1;
    v0 = v0 + a3;
    v0 = v0 << 2;
    v0 = v0 - a3;
    a2 = v0 << 2;
    a1 = MEMORY.ref(4, v1).offset(0x0L).get();
    v1 = 0x8011_0000L;
    v1 = v1 + 0x2068L;
    v0 = MEMORY.ref(2, a1).offset(0x272L).getSigned();
    t0 = MEMORY.ref(2, a1).offset(0x272L).get();
    v0 = v0 << 2;
    v0 = v0 + v1;
    a0 = MEMORY.ref(4, v0).offset(0x0L).get();

    //LAB_800eef7c
    do {
      MEMORY.ref(2, t2).offset(a2).setu(MEMORY.ref(2, a0).get());

      if(MEMORY.ref(2, a0).get() > 0xa0feL) {
        break;
      }

      a2 = a2 + 0x2L;
      a0 = a0 + 0x2L;
    } while(true);

    //LAB_800eefa8
    a2 = 0x9fL;
    a0 = a1 + 0x13eL;
    v0 = 0x800c_0000L;
    v0 = v0 + 0x6b78L;
    v1 = a3 << 2;
    v1 = v1 + v0;
    v0 = a3 + 0x1L;
    MEMORY.ref(4, v1).offset(0x0L).setu(t4);
    MEMORY.ref(4, t3).offset(0x6b9cL).setu(v0);

    //LAB_800eefcc
    do {
      MEMORY.ref(2, a0).offset(0x4L).setu(0);
      a0 = a0 - 0x2L;
      a2 = a2 - 0x1L;
    } while((int)a2 >= 0);

    v0 = t0 << 16;
    v0 = (int)v0 >> 16;
    v1 = v0 << 3;
    v1 = v1 - v0;
    v1 = v1 << 2;
    v0 = 0x8011_0000L;
    v0 = v0 - 0x4568L;
    v1 = v1 + v0;
    v0 = MEMORY.ref(2, v1).offset(0x0L).get();

    MEMORY.ref(2, a1).offset(0x5cL).setu(v0);
    v0 = MEMORY.ref(2, v1).offset(0x2L).get();

    MEMORY.ref(2, a1).offset(0x5eL).setu(v0);
    v0 = MEMORY.ref(2, v1).offset(0x4L).get();

    MEMORY.ref(2, a1).offset(0x60L).setu(v0);
    v0 = MEMORY.ref(2, v1).offset(0x6L).get();

    MEMORY.ref(2, a1).offset(0x62L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x8L).get();

    MEMORY.ref(2, a1).offset(0x64L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x9L).get();

    MEMORY.ref(2, a1).offset(0x66L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xaL).get();

    MEMORY.ref(2, a1).offset(0x68L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xbL).get();

    MEMORY.ref(2, a1).offset(0x6aL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xcL).get();

    MEMORY.ref(2, a1).offset(0x6cL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xdL).get();

    MEMORY.ref(2, a1).offset(0x6eL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xeL).get();

    MEMORY.ref(2, a1).offset(0x70L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xfL).get();

    MEMORY.ref(2, a1).offset(0x72L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x10L).get();

    MEMORY.ref(2, a1).offset(0x74L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x11L).get();

    MEMORY.ref(2, a1).offset(0x76L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x12L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x78L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x13L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x7aL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x14L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x7cL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x15L).get();

    MEMORY.ref(2, a1).offset(0x7eL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x16L).get();

    MEMORY.ref(2, a1).offset(0x80L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x17L).get();

    MEMORY.ref(2, a1).offset(0x82L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x18L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x84L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x19L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x86L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x1aL).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x88L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x1bL).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x8aL).setu(v0);
    MEMORY.ref(2, a1).offset(0x8L).setu(MEMORY.ref(2, v1).offset(0x0L).get());
    MEMORY.ref(2, a1).offset(0xcL).setu(MEMORY.ref(2, v1).offset(0x2L).get());
    MEMORY.ref(2, a1).offset(0x10L).setu(MEMORY.ref(2, v1).offset(0x0L).get());
    MEMORY.ref(2, a1).offset(0x12L).setu(MEMORY.ref(2, v1).offset(0x2L).get());
    MEMORY.ref(2, a1).offset(0x16L).setu(0);
    MEMORY.ref(2, a1).offset(0x18L).setu(0);
    MEMORY.ref(2, a1).offset(0x1aL).setu(0);
    MEMORY.ref(2, a1).offset(0x14L).setu(MEMORY.ref(1, v1).offset(0xdL).get());
    MEMORY.ref(2, a1).offset(0x1cL).setu(MEMORY.ref(1, v1).offset(0xfL).get());
    MEMORY.ref(2, a1).offset(0x20L).setu(0);
    MEMORY.ref(2, a1).offset(0x1eL).setu(MEMORY.ref(1, v1).offset(0xeL).get());
    MEMORY.ref(2, a1).offset(0x22L).setu(MEMORY.ref(1, v1).offset(0x10L).get());
    MEMORY.ref(2, a1).offset(0x26L).setu(0);
    MEMORY.ref(2, a1).offset(0x28L).setu(0);
    MEMORY.ref(2, a1).offset(0x2aL).setu(0);
    MEMORY.ref(2, a1).offset(0x2cL).setu(0);
    MEMORY.ref(2, a1).offset(0x2eL).setu(0);
    MEMORY.ref(2, a1).offset(0x30L).setu(0);
    MEMORY.ref(2, a1).offset(0x24L).setu(MEMORY.ref(1, v1).offset(0x11L).get());
    MEMORY.ref(2, a1).offset(0x32L).setu(MEMORY.ref(1, v1).offset(0x8L).get());
    MEMORY.ref(2, a1).offset(0x34L).setu(MEMORY.ref(2, v1).offset(0x4L).get());
    MEMORY.ref(2, a1).offset(0x36L).setu(MEMORY.ref(2, v1).offset(0x6L).get());
    MEMORY.ref(2, a1).offset(0x38L).setu(MEMORY.ref(1, v1).offset(0x9L).get());
    MEMORY.ref(2, a1).offset(0x3cL).setu(0);
    MEMORY.ref(2, a1).offset(0x3eL).setu(0);
    MEMORY.ref(2, a1).offset(0x3aL).setu(MEMORY.ref(1, v1).offset(0xaL).get());
    MEMORY.ref(2, a1).offset(0x40L).setu(MEMORY.ref(1, v1).offset(0xbL).get());
    MEMORY.ref(2, a1).offset(0x44L).setu(0);
    MEMORY.ref(2, a1).offset(0x46L).setu(0);
    MEMORY.ref(2, a1).offset(0x48L).setu(0);
    MEMORY.ref(2, a1).offset(0x4aL).setu(0);
    MEMORY.ref(2, a1).offset(0x58L).setu(-0x1L);
    MEMORY.ref(2, a1).offset(0x42L).setu(MEMORY.ref(1, v1).offset(0xcL).get());

    if((MEMORY.ref(2, a1).offset(0x6eL).get() & 0x8L) != 0) {
      MEMORY.ref(2, a1).offset(0x110L).setu(0x1L);
    }

    //LAB_800ef25c
    if((MEMORY.ref(2, a1).offset(0x6eL).get() & 0x4L) != 0) {
      MEMORY.ref(2, a1).offset(0x112L).setu(0x1L);
    }

    //LAB_800ef274
    FUN_80012bb4();
  }

  @Method(0x800ef28cL)
  public static void FUN_800ef28c(long a0) {
    //LAB_800ef2c4
    //TODO sp0x18 is unused, why?
    //memcpy(sp0x18, _800c6e68.getAddress(), 0x28);

    FUN_80110030(0x1L);

    //LAB_800ef31c
    for(int charIndex = 0; charIndex < 3; charIndex++) {
      //LAB_800ef328
      for(int i = 0; i < 9; i++) {
        _800c6960.offset(charIndex * 0x9L).offset(1, i).setu(0xffL);
      }
    }

    //LAB_800ef36c
    //LAB_800ef38c
    for(int charIndex = 0; charIndex < Math.min(_800c677c.get(), 3); charIndex++) {
      long s0 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe40L).offset(charIndex * 0x4L).get()).deref().innerStruct_00.getPointer(); //TODO
      long s1 = MEMORY.ref(2, s0).offset(0x272L).get();
      final byte[] sp0x10 = new byte[9];
      FUN_80022928(sp0x10, (int)MEMORY.ref(2, s0).offset(0x272L).getSigned());
      _800c6960.offset(charIndex * 0x9L).offset(1, 0x0L).setu(s1);

      //LAB_800ef3d8
      for(int i = 1; i < 9; i++) {
        _800c6960.offset(charIndex * 0x9L).offset(1, i).setu(sp0x10[i - 1]);
      }

      //LAB_800ef400
      for(int i = 0; i < 0xa0; i++) {
        MEMORY.ref(2, s0).offset(i * 0x2L).offset(0x4L).setu(0);
      }

      final ActiveStatsa0 stats = stats_800be5f8.get((short)s1);
      MEMORY.ref(2, s0).offset(0x04L).setu(stats.level_0e.get());
      MEMORY.ref(2, s0).offset(0x06L).setu(stats.dlevel_0f.get());
      MEMORY.ref(2, s0).offset(0x08L).setu(stats.hp_04.get());
      MEMORY.ref(2, s0).offset(0x0aL).setu(stats.sp_08.get());
      MEMORY.ref(2, s0).offset(0x0cL).setu(stats.mp_06.get());
      MEMORY.ref(2, s0).offset(0x0eL).setu(stats._0c.get());
      MEMORY.ref(2, s0).offset(0x10L).setu(stats.maxHp_66.get());
      MEMORY.ref(2, s0).offset(0x12L).setu(stats.maxMp_6e.get());
      MEMORY.ref(2, s0).offset(0xacL).setu(stats.dragoonAttack_72.get());
      MEMORY.ref(2, s0).offset(0xaeL).setu(stats.dragoonMagicAttack_73.get());
      MEMORY.ref(2, s0).offset(0xb0L).setu(stats.dragoonDefence_74.get());
      MEMORY.ref(2, s0).offset(0xb2L).setu(stats.dragoonMagicDefence_75.get());
      MEMORY.ref(2, s0).offset(0x14L).setu(stats._76.get());
      MEMORY.ref(2, s0).offset(0x16L).setu(stats._77.get());
      MEMORY.ref(2, s0).offset(0x18L).setu(stats._78.get());
      MEMORY.ref(2, s0).offset(0x1aL).setu(stats._79.get());
      MEMORY.ref(2, s0).offset(0x1cL).setu(stats._7a.get());
      MEMORY.ref(2, s0).offset(0x1eL).setu(stats._7b.get());
      MEMORY.ref(2, s0).offset(0x20L).setu(stats._7c.get());
      MEMORY.ref(2, s0).offset(0x22L).setu(stats._7d.get());
      MEMORY.ref(2, s0).offset(0x24L).setu(stats._7e.get());
      MEMORY.ref(2, s0).offset(0x26L).setu(stats._7f.get());
      MEMORY.ref(2, s0).offset(0x28L).setu(stats._80.get());
      MEMORY.ref(2, s0).offset(0x2aL).setu(stats._81.get());
      MEMORY.ref(2, s0).offset(0x2cL).setu(stats._82.get());
      MEMORY.ref(2, s0).offset(0x2eL).setu(stats._83.get());
      MEMORY.ref(2, s0).offset(0x30L).setu(stats._84.get());
      MEMORY.ref(2, s0).offset(0x32L).setu(stats.gearSpeed_86.get() + stats.bodySpeed_69.get());
      MEMORY.ref(2, s0).offset(0x34L).setu(stats.gearAttack_88.get() + stats.bodyAttack_6a.get());
      MEMORY.ref(2, s0).offset(0x36L).setu(stats.gearMagicAttack_8a.get() + stats.bodyMagicAttack_6b.get());
      MEMORY.ref(2, s0).offset(0x38L).setu(stats.gearDefence_8c.get() + stats.bodyDefence_6c.get());
      MEMORY.ref(2, s0).offset(0x3aL).setu(stats.gearMagicDefence_8e.get() + stats.bodyMagicDefence_6d.get());
      MEMORY.ref(2, s0).offset(0x3cL).setu(stats.attackHit_90.get());
      MEMORY.ref(2, s0).offset(0x3eL).setu(stats.magicHit_92.get());
      MEMORY.ref(2, s0).offset(0x40L).setu(stats.attackAvoid_94.get());
      MEMORY.ref(2, s0).offset(0x42L).setu(stats.magicAvoid_96.get());
      MEMORY.ref(2, s0).offset(0x44L).setu(stats._98.get());
      MEMORY.ref(2, s0).offset(0x46L).setu(stats._99.get());
      MEMORY.ref(2, s0).offset(0x48L).setu(stats._9a.get());
      MEMORY.ref(2, s0).offset(0x4aL).setu(stats._9b.get());
      MEMORY.ref(2, s0).offset(0x58L).setu(stats.selectedAddition_35.get());
      MEMORY.ref(2, s0).offset(0x118L).setu(stats._9c.get());
      MEMORY.ref(2, s0).offset(0x11aL).setu(stats._9e.get());
      MEMORY.ref(2, s0).offset(0x11cL).setu(stats._9f.get());
      MEMORY.ref(2, s0).offset(0x4eL).setu(stats._9b.get());
      MEMORY.ref(2, s0).offset(0x142L).setu(stats._9b.get());
      MEMORY.ref(2, s0).offset(0x110L).setu(stats._46.get());
      MEMORY.ref(2, s0).offset(0x112L).setu(stats._48.get());
      MEMORY.ref(2, s0).offset(0x114L).setu(stats._4a.get());
      MEMORY.ref(2, s0).offset(0x128L).setu(stats._4c.get());
      MEMORY.ref(2, s0).offset(0x12aL).setu(stats._4e.get());
      MEMORY.ref(2, s0).offset(0x12cL).setu(stats._50.get());
      MEMORY.ref(2, s0).offset(0x12eL).setu(stats._52.get());
      MEMORY.ref(2, s0).offset(0x130L).setu(stats._54.get());
      MEMORY.ref(2, s0).offset(0x132L).setu(stats._56.get());
      MEMORY.ref(2, s0).offset(0x134L).setu(stats._58.get());
      MEMORY.ref(2, s0).offset(0x136L).setu(stats._5a.get());
      MEMORY.ref(2, s0).offset(0x138L).setu(stats._5c.get());
      MEMORY.ref(2, s0).offset(0x13aL).setu(stats._5e.get());
      MEMORY.ref(2, s0).offset(0x116L).setu(stats._60.get());
      MEMORY.ref(2, s0).offset(0x13cL).setu(stats._62.get());
      MEMORY.ref(2, s0).offset(0x13eL).setu(stats._64.get());
      MEMORY.ref(2, s0).offset(0x11eL).setu(stats.equipment_30.get(0).get());
      MEMORY.ref(2, s0).offset(0x120L).setu(stats.equipment_30.get(1).get());
      MEMORY.ref(2, s0).offset(0x122L).setu(stats.equipment_30.get(2).get());
      MEMORY.ref(2, s0).offset(0x124L).setu(stats.equipment_30.get(3).get());
      MEMORY.ref(2, s0).offset(0x126L).setu(stats.equipment_30.get(4).get());
    }

    //LAB_800ef798
  }

  @Method(0x800ef7c4L)
  public static void FUN_800ef7c4() {
    //LAB_800ef7d4
    long v1 = _800c6c40.getAddress();
    for(int i = 0; i < 3; i++) {
      MEMORY.ref(2, v1).offset(0x0L).setu(-0x1L);
      MEMORY.ref(2, v1).offset(0x4L).setu(0);
      MEMORY.ref(2, v1).offset(0x6L).setu(0);
      MEMORY.ref(2, v1).offset(0x8L).setu(0);
      MEMORY.ref(2, v1).offset(0xaL).setu(0);
      MEMORY.ref(2, v1).offset(0xeL).setu(0);
      MEMORY.ref(2, v1).offset(0xcL).setu(0);
      MEMORY.ref(2, v1).offset(0x10L).setu(0);
      MEMORY.ref(2, v1).offset(0x12L).setu(0);
      v1 = v1 + 0x3cL;
    }

    long a3 = _800c6c2c.get();

    //LAB_800ef818
    for(int i = 0; i < 3; i++) {
      //LAB_800ef820
      for(int a1 = 0; a1 < 5; a1++) {
        v1 = a1 * 0x40L;

        //LAB_800ef828
        for(int a0 = 0; a0 < 4; a0++) {
          MEMORY.ref(2, a3).offset(v1).offset(0x4L).setu(-0x1L);
          v1 = v1 + 0x10L;
        }
      }

      a3 = a3 + 0x144L;
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
      v1 = a0;
      for(int a1 = 0; a1 < 5; a1++) {
        MEMORY.ref(2, v1).offset(0x30L).setu(-0x1L);
        MEMORY.ref(4, v1).offset(0x24L).setu(0);
        MEMORY.ref(4, v1).offset(0x28L).setu(0);
        MEMORY.ref(4, v1).offset(0x2cL).setu(0);
        MEMORY.ref(2, v1).offset(0x40L).setu(0);
        v1 = v1 + 0x20L;
      }

      a0 = a0 + 0xc4L;
    }
  }

  @Method(0x800ef8d8L)
  public static void FUN_800ef8d8(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    a3 = a0;
    t0 = 0;
    v0 = 0x800c_0000L;
    v0 = v0 + 0x6c40L;
    t1 = v0;
    v0 = a3 << 4;
    v0 = v0 - a3;
    v0 = v0 << 2;
    a2 = v0;
    a0 = a2 + t1;
    a1 = 0x800c_0000L;
    v1 = 0x8007_0000L;
    v1 = v1 - 0x1c68L;
    v0 = a3 << 2;
    v0 = v0 + v1;
    a1 = a1 - 0x3e40L;
    MEMORY.ref(2, a0).offset(0x0L).setu(a3);
    v1 = MEMORY.ref(4, v0).offset(0xe40L).get();
    v0 = a3 << 1;
    v0 = v0 + a3;
    v1 = v1 << 2;
    v1 = v1 + a1;
    v1 = MEMORY.ref(4, v1).offset(0x0L).get();
    v0 = v0 << 4;
    v1 = MEMORY.ref(4, v1).offset(0x0L).get();
    v0 = v0 - a3;
    a1 = MEMORY.ref(2, v1).offset(0x272L).get();
    v0 = v0 << 1;
    MEMORY.ref(2, a0).offset(0x6L).setu(0);
    v1 = MEMORY.ref(2, a0).offset(0x6L).get();
    v0 = v0 + 0x3fL;
    MEMORY.ref(2, a0).offset(0x8L).setu(v0);
    v0 = 0x26L;
    MEMORY.ref(2, a0).offset(0xaL).setu(v0);
    v0 = 0x11L;
    MEMORY.ref(2, a0).offset(0x12L).setu(v0);
    v0 = 0x20L;
    MEMORY.ref(2, a0).offset(0x4L).setu(0);
    MEMORY.ref(2, a0).offset(0x10L).setu(v0);
    v1 = v1 | 0x2L;
    MEMORY.ref(2, a0).offset(0x2L).setu(a1);
    MEMORY.ref(2, a0).offset(0x6L).setu(v1);

    //LAB_800ef980
    do {
      v0 = a2 + t1;
      MEMORY.ref(4, v0).offset(0x14L).setu(0);
      t0 = t0 + 0x1L;
      a2 = a2 + 0x4L;
    } while((int)t0 < 0xaL);

    a1 = 0x800c_0000L;
    v1 = a3 << 2;
    v1 = v1 + a3;
    v1 = v1 << 4;
    v1 = v1 + a3;
    v1 = v1 << 2;
    a0 = 0x800c_0000L;
    a0 = a0 + 0x6c40L;
    v0 = a3 << 4;
    v0 = v0 - a3;
    v0 = v0 << 2;
    v0 = v0 + a0;
    a0 = MEMORY.ref(4, a1).offset(0x6c2cL).get();
    a1 = MEMORY.ref(2, v0).offset(0x8L).get();
    v1 = v1 + a0;
    MEMORY.ref(2, v1).offset(0x0L).setu(a1);
    v0 = MEMORY.ref(2, v0).offset(0xaL).get();
    MEMORY.ref(2, v1).offset(0x2L).setu(v0);
  }

  @Method(0x800ef9e4L)
  public static void FUN_800ef9e4() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long hi;
    v0 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v0).offset(0x6cf4L).get();
    v0 = 0x6L;
    if(v1 == v0) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x677cL).get();

      if((int)v0 > 0) {
        s3 = 0;
        s1 = -0x1L;
        v0 = 0x800c_0000L;
        s0 = v0 + 0x6c40L;

        //LAB_800efa34
        do {
          v0 = MEMORY.ref(2, s0).offset(0x0L).getSigned();

          if(v0 == s1) {
            v1 = _800be5d0.get();
            v0 = 0x1L;
            if(v1 == v0) {
              a0 = s3;
              FUN_800ef8d8(a0);
            }
          }

          //LAB_800efa64
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x677cL).get();
          s3 = s3 + 0x1L;
          s0 = s0 + 0x3cL;
        } while((int)s3 < (int)v0);
      }

      //LAB_800efa78
      v0 = 0x800c_0000L;
      s4 = MEMORY.ref(4, v0).offset(0x677cL).get();

      if((int)s4 >= 0x4L) {
        s4 = 0x3L;
      }

      //LAB_800efa94
      if((int)s4 > 0) {
        s3 = 0;
        v0 = 0x800c_0000L;
        s5 = v0 - 0x3e40L;
        v0 = 0x800c_0000L;
        s2 = v0 + 0x6c40L;

        //LAB_800efaac
        do {
          v1 = MEMORY.ref(2, s2).offset(0x0L).getSigned();
          v0 = -0x1L;
          if(v1 != v0) {
            v1 = MEMORY.ref(2, s2).offset(0x6L).get();

            v0 = v1 & 0x1L;
            if(v0 != 0) {
              v0 = v1 & 0x2L;
              if(v0 != 0) {
                v1 = 0x8007_0000L;
                v1 = v1 - 0x1c68L;
                v0 = s3 << 2;
                v0 = v0 + v1;
                v0 = MEMORY.ref(4, v0).offset(0xe40L).get();

                v0 = v0 << 2;
                v0 = v0 + s5;
                v0 = MEMORY.ref(4, v0).offset(0x0L).get();

                s1 = MEMORY.ref(4, v0).offset(0x0L).get();

                v0 = MEMORY.ref(2, s1).offset(0x10L).get();
                a2 = MEMORY.ref(2, s1).offset(0x8L).getSigned();
                v0 = v0 << 16;
                v1 = (int)v0 >> 16;
                v0 = v0 >>> 31;
                v0 = v1 + v0;
                v0 = (int)v0 >> 1;
                if((int)v0 < (int)a2) {
                  a3 = 0x1L;
                } else {
                  a3 = 0x2L;
                }

                //LAB_800efb30
                v0 = v1;
                if((int)v0 < 0) {
                  v0 = v0 + 0x3L;
                }

                //LAB_800efb40
                v0 = (int)v0 >> 2;
                s0 = s3 << 16;
                if((int)v0 >= (int)a2) {
                  a3 = 0x3L;
                }

                //LAB_800efb54
                s0 = (int)s0 >> 16;
                a0 = s0;
                a1 = 0;
                FUN_800f1550(a0, a1, a2, a3);
                a0 = s0;
                a1 = 0x1L;
                a2 = MEMORY.ref(2, s1).offset(0x10L).getSigned();
                a3 = a1;
                FUN_800f1550(a0, a1, a2, a3);
                a0 = s0;
                a1 = 0x2L;
                a2 = MEMORY.ref(2, s1).offset(0xcL).getSigned();
                a3 = 0x1L;
                FUN_800f1550(a0, a1, a2, a3);
                a0 = s0;
                a1 = 0x3L;
                a2 = MEMORY.ref(2, s1).offset(0x12L).getSigned();
                a3 = 0x1L;
                FUN_800f1550(a0, a1, a2, a3);
                a0 = 0x51eb_0000L;
                v1 = MEMORY.ref(2, s1).offset(0xaL).get();
                a0 = a0 | 0x851fL;
                v1 = v1 << 16;
                v0 = (int)v1 >> 16;
                hi = (long)(int)v0 * (int)a0 >>> 32;
                a1 = 0x4L;
                a3 = 0x1L;
                a0 = s0;
                v1 = (int)v1 >> 31;
                t0 = hi;
                a2 = (int)t0 >> 5;
                a2 = a2 - v1;
                a2 = a2 << 16;
                a2 = (int)a2 >> 16;
                FUN_800f1550(a0, a1, a2, a3);
                v0 = 0x800c_0000L;
                v0 = MEMORY.ref(4, v0).offset(-0x4f04L).get();

                v0 = v0 & 0x3L;
                MEMORY.ref(4, s2).offset(0x18L).setu(v0);
                v0 = MEMORY.ref(2, s1).offset(0x6L).getSigned();
                v1 = MEMORY.ref(2, s1).offset(0x6L).get();
                if((int)v0 >= 0x5L) {
                  v1 = 0x5L;
                }

                //LAB_800efc0c
                a0 = MEMORY.ref(2, s1).offset(0xaL).getSigned();
                v1 = v1 << 16;
                v1 = (int)v1 >> 16;
                v0 = v1 << 1;
                v0 = v0 + v1;
                v0 = v0 << 3;
                v0 = v0 + v1;
                v1 = v0 << 2;
                if((int)a0 < (int)v1) {
                  MEMORY.ref(2, s2).offset(0x6L).and(0xfff3L);
                } else {
                  MEMORY.ref(2, s2).offset(0x6L).oru(0x4L);

                  if(MEMORY.ref(2, s1).offset(0xaL).getSigned() < (int)v1) {
                    //LAB_800efc5c
                    MEMORY.ref(2, s2).offset(0x6L).and(0xfff3L);
                  }
                }

                //LAB_800efc6c
                v1 = MEMORY.ref(2, s2).offset(0x6L).get();

                v0 = v1 & 0x4L;
                if(v0 != 0) {
                  v0 = v1 ^ 0x8L;
                  MEMORY.ref(2, s2).offset(0x6L).setu(v0);
                }

                //LAB_800efc84
                v1 = MEMORY.ref(4, s2).offset(0x1cL).get();

                if((int)v1 < 0x6L) {
                  v0 = v1 + 0x1L;
                  MEMORY.ref(4, s2).offset(0x1cL).setu(v0);
                }
              }
            }
          }

          //LAB_800efc9c
          s3 = s3 + 0x1L;
          s2 = s2 + 0x3cL;
        } while((int)s3 < (int)s4);
      }

      //LAB_800efcac
      if((int)s4 > 0) {
        s3 = 0;
        v0 = 0x800c_0000L;
        a1 = v0 + 0x6c40L;
        v0 = 0x800c_0000L;
        v1 = 0x8010_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x6c2cL).get();
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x6c38L).get();
        v1 = v1 - 0x4e68L;
        v0 = v0 << 1;
        v1 = v0 + v1;

        //LAB_800efcdc
        do {
          v0 = MEMORY.ref(2, v1).offset(0x0L).get();
          s3 = s3 + 0x1L;
          MEMORY.ref(2, a1).offset(0xaL).setu(v0);
          v0 = MEMORY.ref(2, v1).offset(0x0L).get();
          a1 = a1 + 0x3cL;
          MEMORY.ref(2, a0).offset(0x2L).setu(v0);
          a0 = a0 + 0x144L;
        } while((int)s3 < (int)s4);
      }

      //LAB_800efd00
      FUN_800f3940();
      FUN_800f4b80();
    }

    //LAB_800efd10
  }

  @Method(0x800efd34L)
  public static void FUN_800efd34() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long t0;
    long t1;
    long t5;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s7;
    long fp;
    long spe8;
    long spec;
    long spf0;
    long spf8;
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
      spe8 = _800c677c.get();
      if(spe8 > 0x3L) {
        spe8 = 0x3L;
      }

      //LAB_800f0000
      final Memory.TemporaryReservation sp0x68tmp = MEMORY.temp(0xc);
      final Value sp0x68 = sp0x68tmp.get();

      //LAB_800f0008
      for(s4 = 0; s4 < 3; s4++) {
        sp0x68.offset(s4 * 0x4L).setu(-0x1L);
      }

      //LAB_800f002c
      for(s4 = 0, v1 = 0; s4 < 3; s4++) {
        if(_800c6c40.offset(2, s4 * 0x3cL).getSigned() != -0x1L) {
          sp0x68.offset(v1 * 0x4L).setu(s4);
          v1++;
        }

        //LAB_800f0044
      }

      s7 = _800c6c40.getAddress();
      spf8 = 0;
      spfc = 0;

      //LAB_800f0074
      for(s4 = 0; s4 < spe8; s4++) {
        if(MEMORY.ref(2, s7).offset(0x0L).getSigned() != -0x1L) {
          v1 = MEMORY.ref(2, s7).offset(0x6L).get();

          if((v1 & 0x1L) != 0 && (v1 & 0x2L) != 0) {
            a2 = _8006e398.offset(0xe40L).offset(spfc).get();
            final BtldScriptData27c data = scriptStatePtrArr_800bc1c0.get((int)a2).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
            if((scriptStatePtrArr_800bc1c0.get((int)_800c66c8.get()).deref().ui_60.get() & 0x4L) != 0x1L && _800c66c8.get() == a2) {
              spec = 0x2L;
              s5 = 0x2L;
            } else {
              spec = 0;
              s5 = 0x1L;
            }

            //LAB_800f0108
            if((data._0e.get() & 0x2000L) == 0) {
              s2 = 0x4L;
            } else {
              s2 = 0x5L;
            }

            //LAB_800f0120
            //LAB_800f0128
            for(s0 = 0; s0 < s2; s0++) {
              //LAB_800f0134
              for(s1 = 0; s1 < 4; s1++) {
                a2 = _800c6c2c.get() + spf8;
                t0 = a2 + s0 * 0x40L + s1 * 0x10L;
                if(MEMORY.ref(2, t0).offset(0x4L).getSigned() == -0x1L) {
                  break;
                }
                FUN_800f8dfc(MEMORY.ref(2, t0).offset(0x6L).get() + MEMORY.ref(2, a2).offset(0x0L).get() - centreScreenX_1f8003dc.get(), MEMORY.ref(2, t0).offset(0x8L).get() + MEMORY.ref(2, a2).offset(0x2L).get() - centreScreenY_1f8003de.get(), MEMORY.ref(1, t0).offset(0xaL).get(), MEMORY.ref(1, t0).offset(0xcL).get(), MEMORY.ref(2, t0).offset(0xeL).getSigned(), MEMORY.ref(2, t0).offset(0x10L).getSigned(), MEMORY.ref(2, t0).offset(0x12L).getSigned(), spec, MEMORY.ref(4, s7).offset(0x1cL).get());
              }

              //LAB_800f01e0
            }

            //LAB_800f01f0
            a1 = _800c6c2c.get() + spf8;
            s0 = _800fb444.offset(data._272.get() * 0x4L).get();
            FUN_800f8dfc(MEMORY.ref(2, a1).offset(0x0L).get() - centreScreenX_1f8003dc.get() + 1, MEMORY.ref(2, a1).offset(0x2L).get() - centreScreenY_1f8003de.get() - 25, MEMORY.ref(1, s0).offset(0x0L).get(), MEMORY.ref(1, s0).offset(0x1L).get(), MEMORY.ref(1, s0).offset(0x2L).get(), MEMORY.ref(1, s0).offset(0x3L).get(), 0x2cL, spec, MEMORY.ref(4, s7).offset(0x1cL).get());
            v0 = _800c6c2c.get() + spf8;
            FUN_800f8dfc(MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenX_1f8003dc.get() - 44, MEMORY.ref(2, v0).offset(0x2L).get() - centreScreenY_1f8003de.get() - 22, MEMORY.ref(1, s0).offset(0x4L).get(), MEMORY.ref(1, s0).offset(0x5L).get(), MEMORY.ref(1, s0).offset(0x6L).get(), MEMORY.ref(1, s0).offset(0x7L).get(), MEMORY.ref(1, s0).offset(0x8L).get(), s5, MEMORY.ref(4, s7).offset(0x1cL).get());

            if(spec != 0) {
              final long v1_0 = (6 - MEMORY.ref(4, s7).offset(0x1cL).get()) * 8 + 100;
              t1 = _800c6c2c.get() + spf8;
              a0 = MEMORY.ref(2, t1).offset(0x0L).get() - centreScreenX_1f8003dc.get() + MEMORY.ref(1, s0).offset(0x6L).get() / 2 - 44;
              v1 = (MEMORY.ref(1, s0).offset(0x6L).get() + 2) * v1_0 / 100 / 2;
              v0 = a0 - v1;
              a0 = a0 + v1 - 0x1L;

              final Memory.TemporaryReservation sp0x28tmp = MEMORY.temp(0x8);
              final Value sp0x28 = sp0x28tmp.get();
              sp0x28.offset(0x0L).setu(v0);
              sp0x28.offset(0x2L).setu(a0);
              sp0x28.offset(0x4L).setu(v0);
              sp0x28.offset(0x6L).setu(a0);
              a1 = MEMORY.ref(2, t1).offset(0x2L).get() - centreScreenY_1f8003de.get() + MEMORY.ref(1, s0).offset(0x7L).get() / 2 - 22;
              v1 = (MEMORY.ref(1, s0).offset(0x7L).get() + 2) * v1_0 / 100 / 2;
              v0 = a1 - v1;
              a1 = a1 + v1 - 0x1L;
              final Memory.TemporaryReservation sp0x30tmp = MEMORY.temp(0x8);
              final Value sp0x30 = sp0x30tmp.get();
              sp0x30.offset(0x0L).setu(v0);
              sp0x30.offset(0x2L).setu(v0);
              sp0x30.offset(0x4L).setu(a1);
              sp0x30.offset(0x6L).setu(a1);

              //LAB_800f0438
              for(s2 = 0; s2 < 8; s2++) {
                s3 = 0xffL;
                s0 = 0xffL;
                t7 = 0xffL;
                v1 = MEMORY.ref(4, s7).offset(0x1cL).get();

                s1 = 0;
                if((int)v1 < 0x6L) {
                  s1 = 0x1L;
                  v0 = v1 * 0x2aL;
                  t7 = v0;
                  s3 = v0;
                  s0 = v0;
                }

                //LAB_800f0470
                //LAB_800f047c
                t5 = s2 / 4;
                t0 = sp0x38.offset(s2 % 4 * 0xcL).getAddress();
                a2 = sp0x28.offset(MEMORY.ref(1, t0).offset(0x0L).getSigned() * 2).getAddress();
                a1 = sp0x30.offset(MEMORY.ref(1, t0).offset(0x1L).getSigned() / 2).getAddress();
                v1 = sp0x28.offset(MEMORY.ref(1, t0).offset(0x2L).getSigned() / 2).getAddress();
                v0 = sp0x30.offset(MEMORY.ref(1, t0).offset(0x3L).getSigned() / 2).getAddress();
                FUN_800f9ee8(
                  MEMORY.ref(2, a2).get() + MEMORY.ref(1, t0).offset(0x4L).getSigned() + MEMORY.ref(1, t0).offset(0x8L).getSigned() * t5,
                  MEMORY.ref(2, a1).get() + MEMORY.ref(1, t0).offset(0x5L).getSigned() + MEMORY.ref(1, t0).offset(0x9L).getSigned() * t5,
                  MEMORY.ref(2, v1).get() + MEMORY.ref(1, t0).offset(0x6L).getSigned() + MEMORY.ref(1, t0).offset(0xaL).getSigned() * t5,
                  MEMORY.ref(2, v0).get() + MEMORY.ref(1, t0).offset(0x7L).getSigned() + MEMORY.ref(1, t0).offset(0xbL).getSigned() * t5,
                  t7 & 0xffL,
                  s0 & 0xffL,
                  s3 & 0xffL,
                  s1 != 0
                );
              }

              sp0x28tmp.release();
              sp0x30tmp.release();
            }

            //LAB_800f05d4
            s3 = 0;
            s0 = 0;
            s1 = (data._0e.get() & 0x2000L) > 0 ? 1 : 0;

            //LAB_800f05f4
            for(int i = 0; i < 3; i++) {
              if(i == 0x2L && s1 == 0) {
                s3 = -0xaL;
              }

              //LAB_800f060c
              v1 = sp0x78.offset(s0).getAddress();

              //LAB_800f0610
              v0 = _800c6c2c.get() + spf8;
              FUN_800f8dfc(
                MEMORY.ref(2, v1).offset(0x0L).get() + MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenX_1f8003dc.get(),
                MEMORY.ref(2, v1).offset(0x2L).get() + MEMORY.ref(2, v0).offset(0x2L).get() - centreScreenY_1f8003de.get(),
                MEMORY.ref(1, v1).offset(0x4L).get(),
                MEMORY.ref(1, v1).offset(0x6L).get(),
                MEMORY.ref(2, v1).offset(0x8L).getSigned(),
                MEMORY.ref(2, v1).offset(0xaL).get() + s3,
                0x2cL,
                spec,
                MEMORY.ref(4, s7).offset(0x1cL).get()
              );

              s0 = s0 + 0xcL;
            }

            if(s1 != 0) {
              a0 = data._0a.get();
              fp = spf8;
              s5 = a0 / 100;
              s2 = a0 % 100;

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
                v1 = (short)s1 * 35 / 100;
                s1 = v1 < 0 ? 0 : v1;

                //LAB_800f0780
                s0 = linkedListAddress_1f8003d8.get();
                FUN_8003b5b0(s0);
                gpuLinkedListSetCommandTransparency(s0, false);
                linkedListAddress_1f8003d8.addu(0x24L);

                a0 = _800c6c2c.get() + fp;
                v0 = MEMORY.ref(2, a0).get() - centreScreenX_1f8003dc.get() + 0x3L;
                MEMORY.ref(2, s0).offset(0x18L).setu(v0);
                MEMORY.ref(2, s0).offset(0x08L).setu(v0);
                v0 = MEMORY.ref(2, a0).get() - centreScreenX_1f8003dc.get() + s1 + 0x3L;
                MEMORY.ref(2, s0).offset(0x20L).setu(v0);
                MEMORY.ref(2, s0).offset(0x10L).setu(v0);
                v0 = MEMORY.ref(2, a0).offset(0x2L).get() - centreScreenY_1f8003de.get() + 0x8L;
                MEMORY.ref(2, s0).offset(0x12L).setu(v0);
                MEMORY.ref(2, s0).offset(0x0aL).setu(v0);
                v0 = MEMORY.ref(2, a0).offset(0x2L).get() - centreScreenY_1f8003de.get() + 0xbL;
                MEMORY.ref(2, s0).offset(0x22L).setu(v0);
                MEMORY.ref(2, s0).offset(0x1aL).setu(v0);

                a0 = spf0 * 2;
                v0 = sp0xb8.offset(spf0 * 0x6L).getAddress();
                MEMORY.ref(1, s0).offset(0x4L).setu(MEMORY.ref(1, v0).offset(0x0L).get());
                MEMORY.ref(1, s0).offset(0x5L).setu(MEMORY.ref(1, v0).offset(0x1L).get());
                MEMORY.ref(1, s0).offset(0x6L).setu(MEMORY.ref(1, v0).offset(0x2L).get());
                MEMORY.ref(1, s0).offset(0xcL).setu(MEMORY.ref(1, v0).offset(0x0L).get());
                MEMORY.ref(1, s0).offset(0xdL).setu(MEMORY.ref(1, v0).offset(0x1L).get());
                MEMORY.ref(1, s0).offset(0xeL).setu(MEMORY.ref(1, v0).offset(0x2L).get());
                a0 = a0 + 0x1L;
                v0 = sp0xb8.offset(a0 * 0x3L).getAddress();
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
                v0 = _800c6c2c.get() + spf8;
                final long offsetX = MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenX_1f8003dc.get();
                final long offsetY = MEMORY.ref(2, v0).offset(0x2L).get() - centreScreenY_1f8003de.get();
                FUN_800f9ee8(_800fb46c.get(i * 4).get() + offsetX, _800fb46c.get(i * 4 + 1).get() + offsetY, _800fb46c.get(i * 4 + 2).get() + offsetX, _800fb46c.get(i * 4 + 3).get() + offsetY, 0x60L, 0x60L, 0x60L, false);
              }

              if((MEMORY.ref(2, s7).offset(0x6L).get() & 0x8L) != 0) {
                //LAB_800f09ec
                for(int i = 0; i < 4; i++) {
                  v0 = _800c6c2c.get() + spf8;
                  final long offsetX = MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenX_1f8003dc.get();
                  final long offsetY = MEMORY.ref(2, v0).offset(0x2L).get() - centreScreenY_1f8003de.get();
                  FUN_800f9ee8(_800fb47c.get(i * 4).get() + offsetX, _800fb47c.get(i * 4 + 1).get() + offsetY, _800fb47c.get(i * 4 + 2).get() + offsetX, _800fb47c.get(i * 4 + 3).get() + offsetY, 0x80L, 0, 0, false);
                }
              }
            }
          }
        }

        //LAB_800f0aa8
        s7 = s7 + 0x3cL;
        spf8 = spf8 + 0x144L;
        spfc = spfc + 0x4L;
      }

      //LAB_800f0ad4
      if(_800c6c40.getSigned() != -0x1L && (_800c6c40.offset(2, 0x6L).get() & 0x1L) != 0) {
        FUN_800f1268(0x10L, _800fb198.offset(2, _800c6c38.get() * 0x2L).get() - 0x1aL, 0x120L, 0x28L, 0x8L);
      }

      //LAB_800f0b3c
      FUN_800f3dbc();
      FUN_800f5c94();

      v1 = _800c6c34.get();
      if(MEMORY.ref(4, v1).offset(0x4cL).get() != 0) {
        FUN_800eca98(MEMORY.ref(4, v1).offset(0x50L).get(), MEMORY.ref(4, v1).offset(0x54L).get());
        a2 = _800c6c34.get();
        a1 = MEMORY.ref(4, a2).offset(0x54L).get();
        LodString str;
        if((int)a1 == -0x1L) {
          str = _800fb36c.get((int)MEMORY.ref(4, a2).offset(0x50L).get()).deref();
          spf0 = 0x3L;
          FUN_8002a55c(str);
        } else {
          final BtldScriptData27c data;

          //LAB_800f0bb0
          v1 = MEMORY.ref(4, a2).offset(0x50L).get();

          if(v1 == 0x1L) {
            //LAB_800f0ca4
            data = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(a1 * 0x4L).offset(0xebcL).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

            //LAB_800f0cf0
            for(s4 = 0; s4 < _800c6768.get(); s4++) {
              if(_800c6b78.offset(s4 * 0x4L).get() == MEMORY.ref(4, a2).offset(0x48L).get()) {
                break;
              }
            }

            //LAB_800f0d10
            str = FUN_800f8568(data, _800c69d0.get((int)s4));
            FUN_8002a55c(str);
            spf0 = FUN_800f8ca0(data._1c.get());
          } else if((int)v1 >= 0x2L || v1 != 0) {
            //LAB_800f0d58
            //LAB_800f0d5c
            s4 = _800c6c34.deref(4).offset(0x54L).get();
            a1 = _8006e398.offset(s4 * 0x4L).getAddress();
            data = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, a1).offset(0xe0cL).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
            if((scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, a1).offset(0xe0cL).get()).deref().ui_60.get() & 0x4L) == 0) {
              str = _800fb378.get(data._272.get()).deref();
              spf0 = sp0xa0.offset(2, data._272.get() * 0x2L).get();

              if(data._272.get() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0 && (scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, a1).offset(0xe40L).get()).deref().ui_60.get() & 0x2L) != 0) {
                spf0 = sp0xa0.offset(0x12L).get();
              }
            } else {
              //LAB_800f0e24
              str = FUN_800f8568(data, _800c69d0.get((int)s4));
              spf0 = FUN_800f8ca0(data._1c.get());
            }

            //LAB_800f0e58
            FUN_8002a55c(str);
          } else {
            data = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(a1 * 0x4L).offset(0xe40L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
            str = _800fb378.get(data._272.get()).deref();
            FUN_8002a55c(str);

            if(data._272.get() != 0) {
              spf0 = sp0xa0.offset(2, data._272.get() * 0x2L).get();
            } else {
              if((gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0) {
                if((scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(_800c6c34.deref(4).offset(0x54L).get() * 0x4L).offset(0xe40L).get()).deref().ui_60.get() & 0x2L) != 0) {
                  spf0 = sp0xa0.offset(0x12L).get();
                }
              }
            }
          }

          //LAB_800f0e60
          a0 = data._0e.get();

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
              str = _800fb3a0.get((int)s4).deref();

              //LAB_800f0ed0
              FUN_8002a55c(str);
            }
          }
        }

        //LAB_800f0ed8
        FUN_800f1268(0x2cL, 0x17L, 0xe8L, 0xeL, (short)spf0);
        renderText(str, 160 - FUN_8002a59c(str) / 2, 24, 0);
      }

      sp0x68tmp.release();
    }

    sp0x38tmp.release();
    sp0x78tmp.release();
    sp0xa0tmp.release();
    sp0xb8tmp.release();

    //LAB_800f0f2c
  }
}
