package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gte.DVECTOR;
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
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStruct7cc;
import legend.game.combat.types.BttlScriptData6cInner;
import legend.game.combat.types.BttlScriptData6cSub08_2;
import legend.game.combat.types.BttlScriptData6cSub08_3;
import legend.game.combat.types.BttlScriptData6cSub13c;
import legend.game.combat.types.BttlScriptData6cSub14_2;
import legend.game.combat.types.BttlScriptData6cSub1c;
import legend.game.combat.types.BttlScriptData6cSub30;
import legend.game.combat.types.BttlScriptData6cSub34;
import legend.game.combat.types.BttlScriptData6cSub38;
import legend.game.combat.types.BttlScriptData6cSub38Sub14;
import legend.game.combat.types.BttlScriptData6cSub44;
import legend.game.combat.types.BttlScriptData6cSub98;
import legend.game.combat.types.BttlScriptData6cSub98Inner24;
import legend.game.combat.types.BttlScriptData6cSub98Sub94;
import legend.game.combat.types.BttlScriptData6cSubBase1;
import legend.game.combat.types.EffeScriptData18;
import legend.game.combat.types.EffeScriptData1c;
import legend.game.combat.types.EffeScriptData30;
import legend.game.combat.types.EffeScriptData30Sub06;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.types.BigStruct;
import legend.game.types.DR_MODE;
import legend.game.types.RunningScript;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;

import javax.annotation.Nullable;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80018a5c;
import static legend.game.Scus94491BpeSegment.FUN_80018d60;
import static legend.game.Scus94491BpeSegment.FUN_80018dec;
import static legend.game.Scus94491BpeSegment._1f8003e8;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment._1f8003f8;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.deallocateScriptAndChildren;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback08;
import static legend.game.Scus94491BpeSegment.setScriptDestructor;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_800214bc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021de4;
import static legend.game.Scus94491BpeSegment_8002.playXaAudio;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.strcpy;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003ec90;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f210;
import static legend.game.Scus94491BpeSegment_8003.GetClut;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMode;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040e10;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040ec0;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixX;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixY;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixZ;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004._8004f650;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bda0c;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.bigStruct_800bda10;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;
import static legend.game.combat.Bttl_800c.FUN_800c7488;
import static legend.game.combat.Bttl_800c.FUN_800cea1c;
import static legend.game.combat.Bttl_800c.FUN_800cf37c;
import static legend.game.combat.Bttl_800c.FUN_800cf4f4;
import static legend.game.combat.Bttl_800c.FUN_800cf684;
import static legend.game.combat.Bttl_800c.FUN_800cfb94;
import static legend.game.combat.Bttl_800c.FUN_800cfc20;
import static legend.game.combat.Bttl_800c._800fb0ec;
import static legend.game.combat.Bttl_800c.callScriptFunction;
import static legend.game.combat.Bttl_800c._800c6944;
import static legend.game.combat.Bttl_800c._800c6948;
import static legend.game.combat.Bttl_800c._800fb954;
import static legend.game.combat.Bttl_800c.currentStage_800c66a4;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800c.stageIndices_800fb064;
import static legend.game.combat.Bttl_800c.struct7cc_800c693c;
import static legend.game.combat.Bttl_800d.FUN_800dc408;
import static legend.game.combat.Bttl_800d.FUN_800de36c;
import static legend.game.combat.Bttl_800d.FUN_800de3f4;
import static legend.game.combat.Bttl_800d.FUN_800de544;
import static legend.game.combat.Bttl_800d.FUN_800de618;
import static legend.game.combat.Bttl_800d.ScaleVectorL_SVEC;
import static legend.game.combat.Bttl_800d.optimisePacketsIfNecessary;
import static legend.game.combat.Bttl_800e.FUN_800e3e6c;
import static legend.game.combat.Bttl_800e.FUN_800e60e0;
import static legend.game.combat.Bttl_800e.FUN_800e6170;
import static legend.game.combat.Bttl_800e.FUN_800e61e4;
import static legend.game.combat.Bttl_800e.FUN_800e62a8;
import static legend.game.combat.Bttl_800e.FUN_800e7dbc;
import static legend.game.combat.Bttl_800e.FUN_800e8594;
import static legend.game.combat.Bttl_800e.FUN_800e8c84;
import static legend.game.combat.Bttl_800e.FUN_800e8d04;
import static legend.game.combat.Bttl_800e.FUN_800e8dd4;
import static legend.game.combat.Bttl_800e.FUN_800e9178;
import static legend.game.combat.Bttl_800e.FUN_800e9428;
import static legend.game.combat.Bttl_800e.FUN_800e95f0;
import static legend.game.combat.Bttl_800e.FUN_800eac58;
import static legend.game.combat.Bttl_800e.FUN_800ebb58;
import static legend.game.combat.Bttl_800e.allocateEffectManager;

public final class SEffe {
  private SEffe() { }

  private static final Value _800fb794 = MEMORY.ref(2, 0x800fb794L);

  private static final Value _800fb7bc = MEMORY.ref(1, 0x800fb7bcL);

  private static final Value _800fb7c0 = MEMORY.ref(1, 0x800fb7c0L);

  private static final Value _800fb7f0 = MEMORY.ref(1, 0x800fb7f0L);

  private static final Value _800fb7fc = MEMORY.ref(1, 0x800fb7fcL);

  private static final Value _800fb804 = MEMORY.ref(1, 0x800fb804L);

  private static final Value _800fb818 = MEMORY.ref(1, 0x800fb818L);

  private static final Value _800fb82c = MEMORY.ref(1, 0x800fb82cL);

  private static final Value _800fb840 = MEMORY.ref(1, 0x800fb840L);

  private static final Value _800fb84c = MEMORY.ref(1, 0x800fb84cL);

  private static final SVECTOR _800fb94c = MEMORY.ref(2, 0x800fb94cL, SVECTOR::new);

  private static final Value _801197c0 = MEMORY.ref(4, 0x801197c0L);

  private static final Value _801197ec = MEMORY.ref(1, 0x801197ecL);

  private static final Value _801198f0 = MEMORY.ref(1, 0x801198f0L);

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_800fe120}</li>
   *   <li>{@link SEffe#FUN_800fd600}</li>
   *   <li>{@link SEffe#FUN_800fd87c}</li>
   *   <li>{@link SEffe#FUN_800fddd8}</li>
   *   <li>{@link SEffe#FUN_800fd87c}</li>
   *   <li>{@link SEffe#FUN_800fddd0}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c>>> _80119b7c = MEMORY.ref(4, 0x80119b7cL, ArrayRef.of(Pointer.classFor(TriConsumerRef.classFor(int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class)), 65, 4, Pointer.deferred(4, TriConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80101e84}</li>
   *   <li>{@link SEffe#FUN_80101d3c}</li>
   *   <li>{@link SEffe#FUN_80101c74}</li>
   *   <li>{@link SEffe#FUN_80101c68}</li>
   *   <li>{@link SEffe#FUN_80101c74}</li>
   *   <li>{@link SEffe#FUN_80101c74}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<QuadConsumerRef<BttlScriptData6cSub98, BttlScriptData6cSub98Sub94, BttlScriptData6cSub98Inner24, Integer>>> _80119b94 = MEMORY.ref(4, 0x80119b94L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(BttlScriptData6cSub98.class, BttlScriptData6cSub98Sub94.class, BttlScriptData6cSub98Inner24.class, int.class)), 6, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c8}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9ec}</li>
   *   <li>{@link SEffe#FUN_800fba58}</li>
   *   <li>{@link SEffe#FUN_800fbb14}</li>
   *   <li>{@link SEffe#FUN_800fbb14}</li>
   *   <li>{@link SEffe#FUN_800fbbe0}</li>
   *   <li>{@link SEffe#FUN_800fbbe0}</li>
   *   <li>{@link SEffe#FUN_800fbd04}</li>
   *   <li>{@link SEffe#FUN_800fbd04}</li>
   *   <li>{@link SEffe#FUN_800fba58}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fbd68}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fbe94}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fbf50}</li>
   *   <li>{@link SEffe#FUN_800fbfd0}</li>
   *   <li>{@link SEffe#FUN_800fc068}</li>
   *   <li>{@link SEffe#FUN_800fc0d0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc1fc}</li>
   *   <li>{@link SEffe#FUN_800fc068}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc280}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc348}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc410}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc42c}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc410}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc528}</li>
   *   <li>{@link SEffe#FUN_800fc5a8}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc61c}</li>
   *   <li>{@link SEffe#FUN_800fc6bc}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc768}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc7c8}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<TriConsumerRef<EffectManagerData6c, BttlScriptData6cSub98, BttlScriptData6cSub98Sub94>>> _80119bac = MEMORY.ref(4, 0x80119bacL, ArrayRef.of(Pointer.classFor(TriConsumerRef.classFor(EffectManagerData6c.class, BttlScriptData6cSub98.class, BttlScriptData6cSub98Sub94.class)), 65, 4, Pointer.deferred(4, TriConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d60}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100e28}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100e28}</li>
   *   <li>{@link SEffe#FUN_80100e28}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100e4c}</li>
   *   <li>{@link SEffe#FUN_80100e28}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100ea0}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<QuadConsumerRef<Long, EffectManagerData6c, BttlScriptData6cSub98, BttlScriptData6cSub98Sub94>>> _80119cb0 = MEMORY.ref(4, 0x80119cb0L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(Long.class, EffectManagerData6c.class, BttlScriptData6cSub98.class, BttlScriptData6cSub98Sub94.class)), 65, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_800fea70}</li>
   *   <li>{@link SEffe#FUN_800fec3c}</li>
   *   <li>{@link SEffe#FUN_800feccc}</li>
   *   <li>{@link SEffe#FUN_800fee9c}</li>
   *   <li>{@link SEffe#FUN_800fefe4}</li>
   *   <li>{@link SEffe#FUN_800ff15c}</li>
   *   <li>{@link SEffe#FUN_800ff3e0}</li>
   *   <li>{@link SEffe#FUN_800ff3e0}</li>
   *   <li>{@link SEffe#FUN_800ff430}</li>
   *   <li>{@link SEffe#FUN_800ff590}</li>
   *   <li>{@link SEffe#FUN_800ff5c4}</li>
   *   <li>{@link SEffe#FUN_800ff6d4}</li>
   *   <li>{@link SEffe#FUN_800ff15c}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800ff6fc}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea70}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800ff788}</li>
   *   <li>{@link SEffe#FUN_800ff890}</li>
   *   <li>{@link SEffe#FUN_800ffa80}</li>
   *   <li>{@link SEffe#FUN_800ffadc}</li>
   *   <li>{@link SEffe#FUN_800ffb80}</li>
   *   <li>{@link SEffe#FUN_800ffbd8}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800ffb80}</li>
   *   <li>{@link SEffe#FUN_800ffe80}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800ffefc}</li>
   *   <li>{@link SEffe#FUN_800fea70}</li>
   *   <li>{@link SEffe#FUN_800fff04}</li>
   *   <li>{@link SEffe#FUN_800fff30}</li>
   *   <li>{@link SEffe#FUN_800fffa0}</li>
   *   <li>{@link SEffe#FUN_801000b8}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_801000f8}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_80100150}</li>
   *   <li>{@link SEffe#FUN_8010025c}</li>
   *   <li>{@link SEffe#FUN_8010025c}</li>
   *   <li>{@link SEffe#FUN_80100364}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_801003e8}</li>
   *   <li>{@link SEffe#FUN_801005b8}</li>
   *   <li>{@link SEffe#FUN_801007b4}</li>
   *   <li>{@link SEffe#FUN_80100800}</li>
   *   <li>{@link SEffe#FUN_80100878}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_801008f8}</li>
   *   <li>{@link SEffe#FUN_80100978}</li>
   *   <li>{@link SEffe#FUN_80100af4}</li>
   *   <li>{@link SEffe#FUN_80100bb4}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_80100c18}</li>
   *   <li>{@link SEffe#FUN_80100cac}</li>
   *   <li>{@link SEffe#FUN_80100cec}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea70}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_80100d00}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<QuadConsumerRef<EffectManagerData6c, BttlScriptData6cSub98, BttlScriptData6cSub98Sub94, BttlScriptData6cSub98Inner24>>> _80119db4 = MEMORY.ref(4, 0x80119db4L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(EffectManagerData6c.class, BttlScriptData6cSub98.class, BttlScriptData6cSub98Sub94.class, BttlScriptData6cSub98Inner24.class)), 65, 4, Pointer.deferred(4, QuadConsumerRef::new)));

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<QuadConsumerRef<EffectManagerData6c, BttlScriptData6cSub38, BttlScriptData6cSub38Sub14, Integer>>> _80119ebc = MEMORY.ref(4, 0x80119ebcL, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(EffectManagerData6c.class, BttlScriptData6cSub38.class, BttlScriptData6cSub38Sub14.class, int.class)), 11, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_801049d4}</li>
   *   <li>{@link SEffe#FUN_801049dc}</li>
   *   <li>{@link SEffe#FUN_80104a14}</li>
   *   <li>{@link SEffe#FUN_80104b10}</li>
   *   <li>{@link SEffe#FUN_80104bec}</li>
   *   <li>{@link SEffe#FUN_80104c9c}</li>
   *   <li>{@link SEffe#FUN_80104e40}</li>
   *   <li>{@link SEffe#FUN_80104f70}</li>
   *   <li>{@link SEffe#FUN_80105050}</li>
   *   <li>{@link SEffe#FUN_801051ac}</li>
   *   <li>{@link SEffe#FUN_801049d4}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<QuadConsumerRef<EffectManagerData6c, BttlScriptData6cSub38, BttlScriptData6cSub38Sub14, Integer>>> _80119ee8 = MEMORY.ref(4, 0x80119ee8L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(EffectManagerData6c.class, BttlScriptData6cSub38.class, BttlScriptData6cSub38Sub14.class, int.class)), 11, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_801030d8}</li>
   *   <li>{@link SEffe#FUN_801030d8}</li>
   *   <li>{@link SEffe#FUN_801030d8}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c>>> _80119f14 = MEMORY.ref(4, 0x80119f14L, ArrayRef.of(Pointer.classFor(TriConsumerRef.classFor(Integer.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class)), 11, 4, Pointer.deferred(4, TriConsumerRef::new)));

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80109358}</li>
   *   <li>{@link SEffe#FUN_80109358}</li>
   *   <li>{@link SEffe#FUN_801097e0}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c>>> _80119fd4 = MEMORY.ref(4, 0x80119fd4L, ArrayRef.of(Pointer.classFor(TriConsumerRef.classFor(Integer.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class)), 3, 4, Pointer.deferred(4, TriConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80109a4c}</li>
   *   <li>{@link SEffe#FUN_80109a4c}</li>
   *   <li>{@link SEffe#FUN_80109a6c}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c>>> _80119fe0 = MEMORY.ref(4, 0x80119fe0L, ArrayRef.of(Pointer.classFor(TriConsumerRef.classFor(Integer.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class)), 3, 4, Pointer.deferred(4, TriConsumerRef::new)));

  private static final Value _80119f40 = MEMORY.ref(1, 0x80119f40L);
  private static final Value _80119f41 = MEMORY.ref(1, 0x80119f41L);
  private static final Value _80119f42 = MEMORY.ref(1, 0x80119f42L);

  /** Struct or something, 0x1c bytes */
  private static final Value _80119f44 = MEMORY.ref(1, 0x80119f44L);
  /** Struct or something, 0x1c bytes */
  private static final Value _80119f60 = MEMORY.ref(1, 0x80119f60L);
  /** Struct or something, 0x1c bytes */
  private static final Value _80119f7c = MEMORY.ref(1, 0x80119f7cL);
  /** Struct or something, 0x1c bytes */
  private static final Value _80119f98 = MEMORY.ref(1, 0x80119f98L);

  private static final Value _80119fb4 = MEMORY.ref(1, 0x80119fb4L);

  private static final ArrayRef<UnsignedByteRef> _80119fbc = MEMORY.ref(1, 0x80119fbcL, ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));
  private static final ArrayRef<UnsignedByteRef> _80119fc4 = MEMORY.ref(1, 0x80119fc4L, ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));

  private static final Value _8011a008 = MEMORY.ref(4, 0x8011a008L);
  private static final Pointer<BttlScriptData6cSub98> _8011a00c = MEMORY.ref(4, 0x8011a00cL, Pointer.deferred(4, BttlScriptData6cSub98::new));
  private static final Pointer<BttlScriptData6cSub98> _8011a010 = MEMORY.ref(4, 0x8011a010L, Pointer.deferred(4, BttlScriptData6cSub98::new));
  private static final Value _8011a014 = MEMORY.ref(1, 0x8011a014L);

  private static final Value _8011a01c = MEMORY.ref(4, 0x8011a01cL);
  private static final Value _8011a020 = MEMORY.ref(4, 0x8011a020L);
  private static final Value _8011a024 = MEMORY.ref(4, 0x8011a024L);
  private static final Value _8011a028 = MEMORY.ref(4, 0x8011a028L);
  private static final Value _8011a02c = MEMORY.ref(4, 0x8011a02cL);

  @Method(0x800fb95cL)
  public static void FUN_800fb95c(final BttlScriptData6cSub98Sub94 a0) {
    a0._50.setX((short)(rcos(a0._14.get()) * a0._16.get() >> 12));
    a0._50.setZ((short)(rsin(a0._14.get()) * a0._16.get() >> 12));
  }

  @Method(0x800fb9c0L)
  public static void FUN_800fb9c0(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    // no-op
  }

  @Method(0x800fb9c8L)
  public static void FUN_800fb9c8(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._58.x.sub(a2._16.get());
    a2._58.y.add(a2._14.get());
  }

  @Method(0x800fb9ecL)
  public static void FUN_800fb9ec(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._14.add(a2._18.get());
    a2._16.add(a2._1a.getX());
    a2._58.y.sub((short)2);

    if(a2._1a.getX() >= 8) {
      a2._1a.y.sub((short)8);
    }
  }

  @Method(0x800fba58L)
  public static void FUN_800fba58(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._50.x.add((short)(rcos(a2._1a.getX()) * a2._1a.getY() / 0x1000));
    a2._50.z.add((short)(rsin(a2._1a.getX()) * a2._1a.getY() / 0x1000));
    a2._16.add(a2._18.get());

    if(a2._18.get() >= 4) {
      a2._18.sub((short)4);
    }

    //LAB_800fbaf0
    a2._1a.x.add(a2._1a.getZ());
  }

  @Method(0x800fbb14L)
  public static void FUN_800fbb14(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._50.x.add((short)(rcos(a2._1a.getX()) * a2._1a.getY() >> 12));
    a2._50.y.add((short)(rsin(a2._1a.getX()) * a2._1a.getY() >> 12));

    if(a2._16.get() >= a2._18.get() * 4) {
      a2._16.sub(a2._18.get());

      if(a2._18.get() >= 4) {
        a2._18.sub((short)4);
      }
    }

    //LAB_800fbbbc
    a2._1a.x.add(a2._1a.z.get());
  }

  @Method(0x800fbbe0L)
  public static void FUN_800fbbe0(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);
    a2._16.add(a2._1a.getY());
    a2._58.y.add((short)20);
    a2._10.sub(a2._24.get());
    if(a2._18.get() == 1) {
      if(a2._22.get() != 0) {
        //LAB_800fbca4
        a2._50.setY((short)-a2._2c.getY());
        a2._22.decr();
      } else {
        a2._50.setY((short)((rsin(a2._1a.getX()) * a2._20.get() >> 12) - (short)a2._2c.getY()));
        a2._1a.x.add((short)0x7f);
        a2._0a.set(a2._1a.getZ());
        a2._0c.set(a2._1a.getZ());
        a2._1a.y.add((short)5);
        a2._20.add((short)20);
      }
      //LAB_800fbcc0
    } else if(a2._2c.getY() + a2._50.getY() >= -1000) {
      a2._1a.setY((short)0);
      a2._18.set((short)1);
      a2._0a.set(a2._1a.getZ());
      a2._0c.set(a2._1a.getZ());
    }

    //LAB_800fbcf4
  }

  @Method(0x800fbd04L)
  public static void FUN_800fbd04(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._16.add(a2._18.get());
    if(a2._18.get() >= 3) {
      a2._18.sub((short)3);
    }

    //LAB_800fbd44
    a2._14.add(a2._1a.getX());
  }

  @Method(0x800fbd68L)
  public static void FUN_800fbd68(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    final SVECTOR sp0x38 = new SVECTOR();
    if(a2._18.get() == 0) {
      FUN_800fb95c(a2);
      a2._50.setY((short)0);
      a2._50.z.shl(1);
      sp0x38.set(a2._1a.getX(), (short)0, (short)0);
    } else {
      //LAB_800fbdb8
      a2._50.setX((short)0);
      a2._50.setY((short)(rsin(a2._14.get()) * a2._16.get() >> 11));
      a2._50.setZ((short)(rcos(a2._14.get()) * a2._16.get() >> 12));
      sp0x38.set((short)0, (short)0, a2._1a.getX());
    }

    //LAB_800fbe10
    a2._14.sub((short)0x80);
    a2._1a.x.sub((short)0x8);

    final VECTOR sp0x18 = new VECTOR().set(a2._50);
    final VECTOR sp0x28 = new VECTOR();
    FUN_800cf37c(a0, sp0x38, sp0x18, sp0x28);
    a2._50.set(sp0x28);
  }

  @Method(0x800fbe94L)
  public static void FUN_800fbe94(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);
    a2._50.setY((short)0);
    a2._50.z.shl(1);
    final SVECTOR sp0x38 = new SVECTOR().set(a2._18.get(), (short)0, a2._1a.getX());
    a2._14.add((short)0x80);
    final VECTOR sp0x18 = new VECTOR().set(a2._50);
    final VECTOR sp0x28 = new VECTOR();
    FUN_800cf37c(a0, sp0x38, sp0x18, sp0x28);
    a2._50.set(sp0x28);
  }

  @Method(0x800fbf50L)
  public static void FUN_800fbf50(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._16.mul(a2._18.get()).shra(8);

    final int v1 = a2._1a.getY() >> 2;
    if(a2._16.get() < v1) {
      a2._16.set((short)v1);
    }

    //LAB_800fbfac
    a2._14.add(a2._1a.getX());
  }

  @Method(0x800fbfd0L)
  public static void FUN_800fbfd0(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._50.setY((short)(rsin(a2._14.get()) * a2._16.get() >> 12));
    a2._50.setZ((short)(rcos(a2._14.get()) * a2._16.get() >> 12));
    a2._16.add(a2._18.get());
    if(a2._16.get() < 0) {
      a2._16.set((short)0);
    }

    //LAB_800fc044
    a2._14.add(a2._1a.getX());
  }

  @Method(0x800fc068L)
  public static void FUN_800fc068(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._16.add(a2._18.get());

    if(a2._50.getY() + a2._2c.getY() >= a0._10.vec_28.getZ()) {
      a2._12.set((short)1);
    }

    //LAB_800fc0bc
  }

  @Method(0x800fc0d0L)
  public static void FUN_800fc0d0(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    if(a2._50.getY() + a2._2c.getY() >= -400 && a2._14.get() == 0) {
      a2._14.set((short)1);
      a2._60.setY((short)-8);
      //LAB_800fc11c
    } else if(a1.scriptIndex_04.get() != -1 && a2._14.get() == 0) {
      final VECTOR sp0x10 = new VECTOR();
      FUN_800cea1c(a1.scriptIndex_04.get(), sp0x10);
      a2._58.setX((short)((sp0x10.getX() - (a2._3c.getX() + a2._2c.getX())) / a2._1a.getZ()));
      a2._58.setY((short)((sp0x10.getY() - (a2._3c.getY() + a2._2c.getY())) / a2._1a.getZ()));
      a2._58.setZ((short)((sp0x10.getZ() - (a2._3c.getZ() + a2._2c.getZ())) / a2._1a.getZ()));
      a2._58.x.add(a2._18.get());
      a2._58.y.add(a2._1a.getX());
      a2._58.z.add(a2._1a.getY());
    }

    //LAB_800fc1ec
  }

  @Method(0x800fc1fcL)
  public static void FUN_800fc1fc(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._16.add(a2._18.get());
    if(a2._50.getY() + a2._2c.getY() >= a0._10.vec_28.getZ()) {
      a2._50.setY((short)(a0._10.vec_28.getZ() - a2._2c.getY()));
      a2._58.y.neg().shra(1);
    }

    //LAB_800fc26c
  }

  @Method(0x800fc280L)
  public static void FUN_800fc280(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._50.setZ((short)(rcos(a2._14.get()) * 2 * a2._1a.getY() >> 12));
    a2._50.setX((short)(rsin(a2._1a.getZ()) * a2._1a.getY() >> 12));
    a2._14.add(a2._16.get());
    a2._1a.setZ((short)(a2._1a.getZ() + a2._16.get() * 2));
    a2._50.y.add((short)(0x40 + (((rcos(a2._18.get()) >> 1) + 0x800) * a2._1a.getY() >> 15)));
    a2._18.add(a2._1a.getX());
  }

  @Method(0x800fc348L)
  public static void FUN_800fc348(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._50.setX((short)((rcos(a2._14.get()) * a2._1a.getX() >> 12) * rsin(a2._18.get()) >> 12));
    a2._50.setY((short)(a2._18.get() * 2 - 0x800));
    a2._50.setZ((short)((rsin(a2._14.get()) * a2._1a.getX() >> 12) * rsin(a2._18.get()) >> 12));
    a2._14.add(a2._16.get());
  }

  @Method(0x800fc410L)
  public static void FUN_800fc410(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._58.y.add((short)(a2._14.get() / 0x100));
  }

  @Method(0x800fc42cL)
  public static void FUN_800fc42c(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._50.setX((short)(a2._14.get() + (rcos(a2._1a.getX()) * a2._1a.getZ() >> 12)));
    a2._50.setY((short)(a2._18.get() + (rsin(a2._1a.getX()) * a2._1a.getZ() >> 12)));
    a2._1a.z.add((short)16);
    a2._1a.x.add(a2._1a.getY());
  }

  @Method(0x800fc4bcL)
  public static void FUN_800fc4bc(final MATRIX a0, final EffectManagerData6c a1, final long a2) {
    RotMatrix_8003faf0(MEMORY.ref(4, a2 + 0x38L, SVECTOR::new), a0);
    TransMatrix(a0, MEMORY.ref(4, a2 + 0x18L, VECTOR::new));
    ScaleMatrixL(a0, MEMORY.ref(4, a2 + 0x28L, VECTOR::new));
  }

  @Method(0x800fc528L)
  public static void FUN_800fc528(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._50.setX((short)(rsin(a2._14.get()) * (a2._18.get() >> 1) >> 12));
    a2._50.setZ((short)(rcos(a2._14.get()) * a2._18.get() >> 12));
    a2._14.add(a2._16.get());
  }

  @Method(0x800fc5a8L)
  public static void FUN_800fc5a8(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._50.setX((short)(rsin(a2._14.get()) * a2._18.get() >> 12));
    a2._50.setZ((short)(rcos(a2._14.get()) * a2._18.get() >> 12));
    a2._18.mul((short)7).div((short)8);
  }

  @Method(0x800fc61cL)
  public static void FUN_800fc61c(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._08.set((short)(rcos(a2._14.get()) * a2._16.get() >> 12));
    a2._14.sub(a2._18.get());
    a2._08.set((short)(a2._08.get() * a0._10.svec_16.getY() >> 12));

    if(a2._16.get() > 0) {
      a2._16.sub(a2._1a.getX());
    }

    //LAB_800fc6a8
  }

  @Method(0x800fc6bcL)
  public static void FUN_800fc6bc(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._50.setX((short)(rsin(a2._14.get()) * a2._16.get() >> 12));
    a2._50.setZ((short)(rcos(a2._14.get()) * a2._16.get() >> 12));
    a2._50.x.add((short)(rsin(a2._18.get()) << 8 >> 12));
    a2._50.z.add((short)(rcos(a2._18.get()) << 8 >> 12));
    a2._18.add(a2._1a.getX());
  }

  @Method(0x800fc768L)
  public static void FUN_800fc768(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._1a.x.add(a2._14.get());
    a2._1a.y.add(a2._16.get());
    a2._1a.z.add(a2._18.get());
    a2._58.setX((short)(a2._1a.getX() >> 8));
    a2._58.setY((short)(a2._1a.getY() >> 8));
    a2._58.setZ((short)(a2._1a.getZ() >> 8));
  }

  @Method(0x800fc7c8L)
  public static void FUN_800fc7c8(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    if(a2._50.getY() + a2._2c.getY() >= a0._10.vec_28.getZ()) {
      a2._50.setY((short)(a0._10.vec_28.getZ() - a2._2c.getY()));
      a2._58.y.neg().shra(1);
      if(a2._14.get() == 0) {
        final int angle = (int)(seed_800fa754.advance().get() % 0x1001);
        a2._58.setX((short)(rcos(angle) >>> 8));
        a2._58.setZ((short)(rcos(angle) >>> 8));
        a2._58.x.mul((short)MEMORY.ref(4, a1.getAddress()).offset(0x20L).get()).shra(8); //TODO
        a2._58.z.mul((short)MEMORY.ref(4, a1.getAddress()).offset(0x20L).get()).shra(8); //TODO
      }

      //LAB_800fc8d8
      a2._14.set((short)1);
    }

    //LAB_800fc8e0
  }

  @Method(0x800fc8f8L)
  public static void FUN_800fc8f8(@Nullable VECTOR a0, @Nullable final SVECTOR a1) {
    if(a0 == null) {
      a0 = new VECTOR();
    }

    //LAB_800fc920
    final MATRIX s1 = matrix_800c3548;
    final VECTOR sp0x30 = new VECTOR().set(s1.transfer).negate();
    FUN_80040ec0(s1, sp0x30, a0);

    if(a1 != null) {
      sp0x30.set(s1.transfer).negate();
      sp0x30.z.add(0x1000);

      final VECTOR sp0x10 = new VECTOR();
      FUN_80040ec0(s1, sp0x30, sp0x10);
      sp0x10.sub(a0);
      final short angle = (short)ratan2(sp0x10.getX(), sp0x10.getZ());
      a1.setY(angle);

      //LAB_800fca44
      a1.setX((short)ratan2(-sp0x10.getY(), (rcos(-angle) * sp0x10.getZ() - rsin(-angle) * sp0x10.getX()) / 0x1000));
      a1.setZ((short)0);
    }

    //LAB_800fca5c
  }

  @Method(0x800fca78L)
  public static long FUN_800fca78(final EffectManagerData6c s3, final BttlScriptData6cSub98 fp, final BttlScriptData6cSub98Sub94 s1, final VECTOR s2, final long a4) {
    long v0;
    long v1;
    final long t0;
    final long t1;
    final long t2;
    final long t3;
    long t5;
    long s0;
    final long a2;
    final long a3;
    long a0;
    final long s4;
    final long s6;
    final long s7;
    long a1;
    long lo;
    final long sp50;
    final long sp52;
    final long sp48;
    final long sp5a;
    final long sp60;
    final long sp68;
    final long sp40;
    final long sp62;
    final long sp42;
    final long sp58;
    final long sp6a;
    final long sp4a;

    final ShortRef refX = new ShortRef();
    final ShortRef refY = new ShortRef();
    s4 = FUN_800cfc20(s1._68, s1._2c, s2, refX, refY);
    if((int)s4 >= 0x28L) {
      a0 = 0x50_0000L / (int)s4;
      s6 = a0 * (s3._10.svec_16.getX() + s1._06.get()) >> 12;
      s7 = a0 * (s3._10.svec_16.getY() + s1._08.get()) >> 12;

      s0 = s1._0e.get() + s3._10.svec_10.getX() - 0xa00L;

      if((s3._10._24.get() & 0x2L) != 0) {
        final VECTOR sp0x28 = new VECTOR();
        final SVECTOR sp0x38 = new SVECTOR();
        FUN_800fc8f8(sp0x28, sp0x38);

        //LAB_800fcb90
        //LAB_800fcbd4
        final int sp18 = (s1._48.getZ() - s2.getZ()) * -Math.abs(rsin(sp0x38.getY() - s3._10.svec_10.getY())) / 0x1000 - (s2.getX() - s1._48.getX()) * -Math.abs(rcos(sp0x38.getY() + s3._10.svec_10.getY())) / 0x1000;
        final int sp1c = s2.getY() - s1._48.getY();
        s0 = -ratan2(sp1c, sp18) + 0x400L;
        s1._48.set(s2);
      }

      //LAB_800fcc20
      a1 = fp._5e.get() >>> 1;
      lo = (long)(int)a1 * (int)s6 & 0xffff_ffffL;
      a1 = lo;
      v1 = fp._5f.get() >>> 1;
      lo = (long)(int)v1 * (int)s7 & 0xffff_ffffL;
      a0 = s0;
      v0 = -a1;
      v0 = (int)v0 >> 8;
      a1 = (int)a1 >> 8;
      sp40 = v0;
      sp48 = a1;
      v1 = lo;
      v0 = -v1;
      v0 = (int)v0 >> 8;
      v1 = (int)v1 >> 8;
      sp42 = v0;
      sp4a = v1;
      a1 = rcos(s0);
      s0 = rsin(a0);
      t5 = (short)sp40 * (int)s0;
      a0 = (short)sp40 * (int)a1;
      a3 = (short)sp48 * (int)s0;
      t0 = (short)sp48 * (int)a1;
      t1 = (short)sp42 * (int)s0;
      t2 = (short)sp42 * (int)a1;
      t3 = (short)sp4a * (int)s0;
      sp50 = (int)t5 >> 12;
      a0 = (int)a0 >> 12;
      sp60 = a0;
      sp58 = (int)a3 >> 12;
      sp68 = (int)t0 >> 12;
      t5 = (short)sp4a * (int)a1;
      sp62 = (int)t2 >> 12;
      a2 = (int)t1 >> 12;
      sp52 = (int)t1 >> 12;
      sp5a = (int)t3 >> 12;
      sp6a = (int)t5 >> 12;
      final short x = refX.get();
      final short y = refY.get();
      MEMORY.ref(2, a4).offset(0x08L).setu(x + a0 - a2);
      MEMORY.ref(2, a4).offset(0x0aL).setu(y + sp62 + sp50);
      MEMORY.ref(2, a4).offset(0x10L).setu(x + sp68 - sp52);
      MEMORY.ref(2, a4).offset(0x12L).setu(y + sp62 + sp58);
      MEMORY.ref(2, a4).offset(0x18L).setu(x + sp60 - sp5a);
      MEMORY.ref(2, a4).offset(0x1aL).setu(y + sp6a + sp50);
      MEMORY.ref(2, a4).offset(0x20L).setu(x + sp68 - sp5a);
      MEMORY.ref(2, a4).offset(0x22L).setu(y + sp6a + sp58);
    }

    //LAB_800fcde0
    return s4;
  }

  @Method(0x800fce10L)
  public static void FUN_800fce10(final EffectManagerData6c a0, final long a1) {
    if((int)MEMORY.ref(4, a1).offset(0x0L).get() >= 0) {
      final long packet = linkedListAddress_1f8003d8.get();
      MEMORY.ref(1, packet).offset(0x03L).setu(0x4L);
      MEMORY.ref(4, packet).offset(0x04L).setu(0x5242_4140L);
      MEMORY.ref(2, packet).offset(0x08L).setu(MEMORY.ref(2, a1).offset(0x08L).get());
      MEMORY.ref(2, packet).offset(0x0aL).setu(MEMORY.ref(2, a1).offset(0x10L).get());
      MEMORY.ref(1, packet).offset(0x0cL).setu(MEMORY.ref(1, a1).offset(0x44L).get());
      MEMORY.ref(1, packet).offset(0x0dL).setu(MEMORY.ref(1, a1).offset(0x45L).get());
      MEMORY.ref(1, packet).offset(0x0eL).setu(MEMORY.ref(1, a1).offset(0x46L).get());
      MEMORY.ref(2, packet).offset(0x10L).setu(MEMORY.ref(2, a1).offset(0x0cL).get());
      MEMORY.ref(2, packet).offset(0x12L).setu(MEMORY.ref(2, a1).offset(0x14L).get());
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (MEMORY.ref(4, a1).offset(0x04L).get() + a0._10._22.get()) / 4 * 4, packet);
      linkedListAddress_1f8003d8.addu(0x14L);
    }

    //LAB_800fcf08
  }

  @Method(0x800fcf20L)
  public static void FUN_800fcf20(final EffectManagerData6c a0, final TmdObjTable a1, final long a2, final long a3) {
    if(MEMORY.ref(4, a2).offset(0x0L).getSigned() >= 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800fc4bc(sp0x10, a0, a2);
      if((MEMORY.ref(4, a2).offset(0x0L).get() & 0x40L) == 0) {
        FUN_800e61e4((int)(MEMORY.ref(1, a2).offset(0x40L).getSigned() << 5), (int)(MEMORY.ref(1, a2).offset(0x41L).getSigned() << 5), (int)(MEMORY.ref(1, a2).offset(0x42L).getSigned() << 5));
      }

      //LAB_800fcf94
      GsSetLightMatrix(sp0x10);
      final MATRIX sp0x30 = new MATRIX();
      FUN_8003f210(matrix_800c3548, sp0x10, sp0x30);
      if((MEMORY.ref(4, a2).offset(0x0L).get() & 0x400_0000L) == 0) {
        RotMatrix_8003faf0(a0._10.svec_10, sp0x30);
        ScaleMatrixL(sp0x30, new VECTOR().set(a0._10.svec_16));
      }

      //LAB_800fcff8
      setRotTransMatrix(sp0x30);
      _1f8003e8.setu(0);
      if((a0._10._00.get() & 0x4000_0000L) != 0) {
        _1f8003ec.setu(a0._10._00.get() >>> 23 & 0x60L);
      } else {
        //LAB_800fd038
        _1f8003ec.setu(a3);
      }

      //LAB_800fd040
      final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
      final GsDOBJ2 sp0x60 = new GsDOBJ2(tmp.get());
      sp0x60.attribute_00.set(MEMORY.ref(4, a2).offset(0x0L).get());
      sp0x60.tmd_08.set(a1);
      FUN_800e3e6c(sp0x60);
      if((MEMORY.ref(4, a2).offset(0x0L).get() & 0x40L) == 0) {
        FUN_800e62a8();
      }
      tmp.release();
    }

    //LAB_800fd064
  }

  @Method(0x800fd084L)
  public static void FUN_800fd084(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2) {
    a2._2c.set(a0._10.vec_04);
    a2._68.set(a0._10.svec_10);

    if(a2._12.get() == 0) {
      a2._12.set((short)1);
    }

    if((a1._08._1c.get() & 0x400_0000L) != 0) {
      a2._84.set((short)(a0._10.svec_1c.getX() << 8));
      a2._86.set((short)(a0._10.svec_1c.getY() << 8));
      a2._88.set((short)(a0._10.svec_1c.getZ() << 8));

      if((a0._10._24.get() & 0x1L) == 0) {
        a2._8a.set((short)0);
        a2._8c.set((short)0);
        a2._8e.set((short)0);
        return;
      }
    }

    //LAB_800fd18c
    //LAB_800fd0dc
    a2._8a.set((short)(a2._84.get() / a2._12.get()));
    a2._8c.set((short)(a2._86.get() / a2._12.get()));
    a2._8e.set((short)(a2._88.get() / a2._12.get()));

    //LAB_800fd1d4
  }

  @Method(0x800fd1dcL)
  public static void FUN_800fd1dc(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final VECTOR a3) {
    if((a1._08._1c.get() & 0x800_0000L) == 0 || (a0._10._24.get() & 0x1L) != 0) {
      //LAB_800fd23c
      a2._84.sub(a2._8a.get());
      a2._86.sub(a2._8c.get());
      a2._88.sub(a2._8e.get());
    } else {
      a2._84.set((short)(a0._10.svec_1c.getX() << 8));
      a2._86.set((short)(a0._10.svec_1c.getY() << 8));
      a2._88.set((short)(a0._10.svec_1c.getZ() << 8));
    }

    //LAB_800fd26c
    a3.setX(a2._84.get());
    a3.setY(a2._86.get());
    a3.setZ(a2._88.get());
    a2._50.add(a2._58);
    a2._58.add(a2._60);

    if(a2._50.getY() + a2._2c.getY() >= a0._10.vec_28.getZ()) {
      if((a0._10._24.get() & 0x20L) != 0) {
        a2._12.set((short)1);
      }

      //LAB_800fd324
      if((a0._10._24.get() & 0x8L) != 0) {
        a2._50.setY((short)(a0._10.vec_28.getZ() - a2._2c.getY()));
        a2._58.setY((short)(-a2._58.getY() / 2));
      }
    }

    //LAB_800fd358
    if((a1._08._1c.get() & 0x200_0000L) == 0) {
      a2._06.add(a2._0a.get());
      a2._08.add(a2._0c.get());
    }

    //LAB_800fd38c
    if((a1._08._1c.get() & 0x100_0000L) == 0) {
      a2._0e.add(a2._10.get());
      a2._70.add(a2._78);
    } else {
      //LAB_800fd3e4
      a2._0e.set((short)(a0._10._24.get() >>> 12 & 0xff0));
    }

    //LAB_800fd3f8
    a2._58.y.add((short)(a0._10.vec_28.getY() >> 8));

    if(a1._6c.get() != 0) {
      a2._58.x.add((short)(a1.vec_70.getX() >> 8));
      a2._58.y.add((short)(a1.vec_70.getY() >> 8));
      a2._58.z.add((short)(a1.vec_70.getZ() >> 8));
    }

    //LAB_800fd458
  }

  @Method(0x800fd460L)
  public static long FUN_800fd460(final long a0, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    a3._04.decr();

    final long s0 = a3._04.get();

    //LAB_800fd53c
    if(s0 == -0xfffL) {
      a3._04.set((short)-0xfff);
    } else if(s0 == 0) {
      FUN_800fd084(a1, a2, a3);

      if((a1._10._24.get() & 0x10L) != 0) {
        a3._50.setY((short)0);
        final long v1 = a2._60.get();

        if(v1 == 0x2L || v1 == 0x5L) {
          //LAB_800fd4f0
          //LAB_800fd504
          for(int i = 0; i < a2._54.get(); i++) {
            a3._44.deref().get(i).setY((short)0);
          }
        }
      }

      //LAB_800fd520
      if((a1._10._24.get() & 0x40L) != 0) {
        a3._58.setY((short)0);
      }
    }

    //LAB_800fd54c
    a2._88.deref().run(a0, a1, a2, a3);

    if((a3._90.get() & 0x1L) == 0) {
      return 0x1L;
    }

    if(a3._12.get() > 0) {
      a3._12.decr();
    }

    //LAB_800fd58c
    if(a3._12.get() == 0 && (a1._10._24.get() & 0x80L) == 0) {
      a3._90.and(0xffff_fffeL);
      a2._90.deref().run(a0, a1, a2, a3);
      return 0x1L;
    }

    //LAB_800fd5e0
    return 0;
  }

  @Method(0x800fd600L)
  public static void FUN_800fd600(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub98 s1 = data._44.derefAs(BttlScriptData6cSub98.class);
    s1._52.incr();

    //LAB_800fd660
    for(int i = 0; i < s1._50.get(); i++) {
      final BttlScriptData6cSub98Sub94 s3 = s1._68.deref().get(i);
      if(FUN_800fd460(index, data, s1, s3) == 0) {
        s1._84.deref().run(data, s1, s3);
        final VECTOR sp0x48 = new VECTOR();
        FUN_800fd1dc(data, s1, s3, sp0x48);

        final VECTOR sp0x28 = new VECTOR();
        FUN_800cf37c(data, new SVECTOR(), new VECTOR().set(s3._50), sp0x28);

        final Memory.TemporaryReservation tmp = MEMORY.temp(0x48);
        final Value sp0x68 = tmp.get();
        sp0x68.offset(0x00L).setu(data._10._00.get());
        sp0x68.offset(0x18L).setu(s3._2c.getX() + sp0x28.getX());
        sp0x68.offset(0x1cL).setu(s3._2c.getY() + sp0x28.getY());
        sp0x68.offset(0x20L).setu(s3._2c.getZ() + sp0x28.getZ());
        sp0x68.offset(0x28L).setu(data._10.svec_16.getX() + s3._06.get());
        sp0x68.offset(0x2cL).setu(data._10.svec_16.getY() + s3._08.get());
        sp0x68.offset(0x30L).setu(data._10.svec_16.getX() + s3._06.get()); // This is correct
        sp0x68.offset(0x38L).setu(s3._70.getX() + s3._68.getX());
        sp0x68.offset(0x3aL).setu(s3._70.getY() + s3._68.getY());
        sp0x68.offset(0x3cL).setu(s3._70.getZ() + s3._68.getZ());
        sp0x68.offset(1, 0x40L).setu(sp0x48.getX() >> 8);
        sp0x68.offset(1, 0x41L).setu(sp0x48.getY() >> 8);
        sp0x68.offset(1, 0x42L).setu(sp0x48.getZ() >> 8);
        sp0x68.offset(1, 0x44L).setu(0);
        sp0x68.offset(1, 0x45L).setu(0);
        sp0x68.offset(1, 0x46L).setu(0);
        FUN_800fcf20(data, s1._30.deref(), sp0x68.getAddress(), s1._56.get());
        tmp.release();
      }

      //LAB_800fd7e0
    }

    //LAB_800fd7fc
    if(s1._6c.get() != 0) {
      s1.vec_70.setX(s1.vec_70.getX() * s1._80.get() >> 8);
      s1.vec_70.setY(s1.vec_70.getY() * s1._80.get() >> 8);
      s1.vec_70.setZ(s1.vec_70.getZ() * s1._80.get() >> 8);
    }

    //LAB_800fd858
  }

  @Method(0x800fd87cL)
  public static void FUN_800fd87c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long v0;
    long v1;
    long a0;
    long s3;

    final BttlScriptData6cSub98 s2 = data._44.derefAs(BttlScriptData6cSub98.class);
    s2._52.incr();

    if(s2._50.get() != 0) {
      final Memory.TemporaryReservation sp0x50tmp = MEMORY.temp(0x48);
      final Value sp0x50 = sp0x50tmp.get();

      //LAB_800fd8dc
      for(int s7 = 0; s7 < s2._50.get(); s7++) {
        final BttlScriptData6cSub98Sub94 s5 = s2._68.deref().get(s7);

        if(FUN_800fd460(index, data, s2, s5) == 0) {
          //LAB_800fd918
          for(int s0 = s2._54.get() - 1; s0 > 0; s0--) {
            s5._44.deref().get(s0).set(s5._44.deref().get(s0 - 1));
          }

          //LAB_800fd950
          s5._44.deref().get(0).sub(s5._50);
          s2._84.deref().run(data, s2, s5);

          final VECTOR sp0x20 = new VECTOR();
          FUN_800fd1dc(data, s2, s5, sp0x20);

          final VECTOR sp0x30 = new VECTOR();
          if((s2._24.get() & 0x1000_0000L) == 0 || (s5._90.get() & 0x8L) == 0) {
            //LAB_800fd9f4
            sp0x30.set(0, 0, 0);
          } else {
            sp0x30.set(sp0x20).negate().div(2);
          }

          //LAB_800fda00
          v0 = s5._90.get();
          v1 = v0 & 0xffff_fff7L;
          v0 = v0 >>> 3;
          v0 = ~v0;
          v0 = v0 & 0x1L;
          v0 = v0 << 3;
          v1 = v1 | v0;
          s5._90.set(v1);

          //LAB_800fda58
          //LAB_800fda90
          sp0x20.setX(MathHelper.clamp(sp0x20.getX() + sp0x30.getX(), 0, 0x8000));
          sp0x20.setY(MathHelper.clamp(sp0x20.getY() + sp0x30.getY(), 0, 0x8000));
          sp0x20.setZ(MathHelper.clamp(sp0x20.getZ() + sp0x30.getZ(), 0, 0x8000));

          //LAB_800fdac8
          if((s5._90.get() & 0x6L) != 0) {
            sp0x50.offset(4, 0x0L).setu(data._10._00.get() & 0x67ff_ffffL | (s5._90.get() >>> 1 & 0x3L) << 28);
          } else {
            sp0x50.offset(4, 0x0L).setu(data._10._00.get());
          }

          //LAB_800fdb14
          final SVECTOR sp0x98 = new SVECTOR().set(sp0x20).div(s2._54.get());
          final VECTOR sp0x40 = new VECTOR().set(s5._44.deref().get(0));
          final ShortRef refX1 = new ShortRef();
          final ShortRef refY1 = new ShortRef();
          s3 = FUN_800cfc20(s5._68, s5._2c, sp0x40, refX1, refY1) / 4;
          sp0x50.offset(2, 0x08L).setu(refX1.get());
          sp0x50.offset(2, 0x10L).setu(refY1.get());
          a0 = data._10._22.get();
          v1 = s3 + a0;
          if((int)v1 >= 0xa0L) {
            if((int)v1 >= 0xffeL) {
              s3 = 0xffeL - a0;
            }

            //LAB_800fdbc0
            sp0x50.offset(4, 0x4L).setu(s3);

            //LAB_800fdbe4
            for(int s0 = 0; s0 < s2._54.get() - 0x1L; s0++) {
              sp0x50.offset(1, 0x40L).setu(sp0x20.getX() >> 8);
              sp0x50.offset(1, 0x41L).setu(sp0x20.getY() >> 8);
              sp0x50.offset(1, 0x42L).setu(sp0x20.getZ() >> 8);
              sp0x20.sub(sp0x98);
              sp0x50.offset(1, 0x44L).setu(sp0x20.getX() >> 8);
              sp0x50.offset(1, 0x45L).setu(sp0x20.getY() >> 8);
              sp0x50.offset(1, 0x46L).setu(sp0x20.getZ() >> 8);
              sp0x40.set(s5._44.deref().get(s0));

              final ShortRef refX2 = new ShortRef();
              final ShortRef refY2 = new ShortRef();
              FUN_800cfc20(s5._68, s5._2c, sp0x40, refX2, refY2);
              sp0x50.offset(2, 0x0cL).setu(refX2.get());
              sp0x50.offset(2, 0x14L).setu(refY2.get());
              _801197c0.offset((s2._60.get() - 2) * 0x4L).deref(4).call(data, sp0x50.getAddress());
              sp0x50.offset(2, 0x08L).setu(sp0x50.offset(2, 0x0cL).get());
              sp0x50.offset(2, 0x10L).setu(sp0x50.offset(2, 0x14L).get());
            }

            //LAB_800fdcc8
            SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);
            insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s3 + data._10._22.get()) / 4 * 4, linkedListAddress_1f8003d8.get());
            linkedListAddress_1f8003d8.addu(0xcL);
          }
        }

        //LAB_800fdd28
      }

      sp0x50tmp.release();
    }

    //LAB_800fdd44
    if(s2._6c.get() != 0) {
      s2.vec_70.mul(s2._80.get()).div(0x100);
    }

    //LAB_800fdda0
  }

  @Method(0x800fddd0L)
  public static void FUN_800fddd0(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // no-op
  }

  @Method(0x800fddd8L)
  public static void FUN_800fddd8(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub98 s2 = data._44.derefAs(BttlScriptData6cSub98.class);
    s2._52.incr();

    //LAB_800fde38
    for(int i = 0; i < s2._50.get(); i++) {
      final BttlScriptData6cSub98Sub94 s4 = s2._68.deref().get(i);

      if(FUN_800fd460(index, data, s2, s4) == 0) {
        s2._84.deref().run(data, s2, s4);

        final VECTOR sp0x20 = new VECTOR();
        FUN_800fd1dc(data, s2, s4, sp0x20);
        final long s0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(s0);
        MEMORY.ref(1, s0).offset(0x3L).setu(0x2L);
        MEMORY.ref(4, s0).offset(0x4L).setu(0x6880_8080L);
        MEMORY.ref(1, s0).offset(0x7L).oru(data._10._00.get() >>> 29 & 0x2L);
        MEMORY.ref(1, s0).offset(0x4L).setu(sp0x20.getX() >> 8);
        MEMORY.ref(1, s0).offset(0x5L).setu(sp0x20.getY() >> 8);
        MEMORY.ref(1, s0).offset(0x6L).setu(sp0x20.getZ() >> 8);

        final VECTOR sp0x50 = new VECTOR().set(s4._50);
        final ShortRef sp0x60 = new ShortRef();
        final ShortRef sp0x64 = new ShortRef();
        int s1 = FUN_800cfc20(s4._68, s4._2c, sp0x50, sp0x60, sp0x64) >> 2;
        final int v1 = s1 + data._10._22.get();
        if(v1 >= 0xa0) {
          if(v1 >= 0xffe) {
            s1 = 0xffe - data._10._22.get();
          }

          //LAB_800fdf44
          MEMORY.ref(2, s0).offset(0x8L).setu(sp0x60.get());
          MEMORY.ref(2, s0).offset(0xaL).setu(sp0x64.get());
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s1 + data._10._22.get()) / 4 * 4, s0);

          SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(1, 1, 0, 0), null);
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s1 + data._10._22.get()) / 4 * 4, linkedListAddress_1f8003d8.get());
          linkedListAddress_1f8003d8.addu(0xcL);
        }
      }

      //LAB_800fdfcc
    }

    //LAB_800fdfe8
    if(s2._6c.get() != 0) {
      s2.vec_70.setX(s2.vec_70.getX() * s2._80.get() >> 8);
      s2.vec_70.setY(s2.vec_70.getY() * s2._80.get() >> 8);
      s2.vec_70.setZ(s2.vec_70.getZ() * s2._80.get() >> 8);
    }

    //LAB_800fe044
  }

  @Method(0x800fe120L)
  public static void FUN_800fe120(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    long s1;
    long s3;
    long s4;
    long s5;
    long s6;
    final VECTOR sp0x38 = new VECTOR();
    final SVECTOR sp0x48 = new SVECTOR();
    final BttlScriptData6cSub98 s2 = data._44.derefAs(BttlScriptData6cSub98.class);

    final Memory.TemporaryReservation sp0x28tmp = MEMORY.temp(0x8a);
    final VECTOR sp0x28 = sp0x28tmp.get().cast(VECTOR::new);

    s2._52.incr();

    //LAB_800fe180
    for(int i = 0; i < s2._50.get(); i++) {
      final BttlScriptData6cSub98Sub94 sp54 = s2._68.deref().get(i);

      if(FUN_800fd460(index, data, s2, sp54) == 0) {
        a2 = sp54._80.get() + (s2._54.get() - 0x1L) * 0x10L;
        a1 = a2 + 0xcL;

        //LAB_800fe1bc
        for(s4 = 0; s4 < s2._54.get() - 0x1L; s4++) {
          MEMORY.ref(4, a2).offset(0x0L).setu(MEMORY.ref(4, a1).offset(-0x1cL).get());
          MEMORY.ref(4, a1).offset(-0x8L).setu(MEMORY.ref(4, a1).offset(-0x18L).get());
          MEMORY.ref(4, a1).offset(-0x4L).setu(MEMORY.ref(4, a1).offset(-0x14L).get());
          MEMORY.ref(4, a1).offset(0x0L).setu(MEMORY.ref(4, a1).offset(-0x10L).get());
          a2 = a2 - 0x10L;
          a1 = a1 - 0x10L;
        }

        //LAB_800fe1fc
        s2._84.deref().run(data, s2, sp54);

        FUN_800fd1dc(data, s2, sp54, sp0x28);

        if((s2._08._1c.get() & 0x1000_0000L) == 0 || (sp54._90.get() & 0x8L) == 0) {
          //LAB_800fe280
          sp0x38.set(0, 0, 0);
        } else {
          sp0x38.set(sp0x28).negate().div(2);
        }

        //LAB_800fe28c
        v0 = sp54._90.get();
        v1 = v0 & 0xffff_fff7L;
        v0 = v0 >>> 3;
        v0 = ~v0;
        v0 = v0 & 0x1L;
        v0 = v0 << 3;
        v1 = v1 | v0;
        sp54._90.set(v1);
        if((data._10._00.get() & 0x400_0000L) == 0) {
          assert false : "This seems wrong";
          sp54._68.setY((short)(ratan2(FUN_800dc408(0, 0, 0, 0, 0) - sp54._50.getX(), FUN_800dc408(2, 0, 0, 0, 0) - sp54._50.getZ()) + 0x400)); //TODO last three params of both functions were undefined
        }

        //LAB_800fe300
        final VECTOR sp0x18 = new VECTOR().set(sp54._50);

        s3 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);
        MEMORY.ref(1, s3).offset(0x3L).setu(0x9L);
        MEMORY.ref(4, s3).offset(0x4L).setu(0x2c80_8080L);
        v0 = data._10._00.get();
        v0 = v0 >>> 29;
        v0 = v0 & 0x2L;
        MEMORY.ref(1, s3).offset(0x7L).oru(v0);
        v1 = s2._5a.get();
        v0 = s2._58.get();
        v1 = v1 & 0x100L;
        v1 = v1 >>> 4;
        v0 = v0 & 0x3ffL;
        v0 = v0 >>> 6;
        v1 = v1 | v0;
        MEMORY.ref(2, s3).offset(0x16L).setu(v1);
        v0 = data._10._00.get();
        v0 = v0 >>> 23;
        v0 = v0 & 0x60L;
        v1 = v1 | v0;
        MEMORY.ref(2, s3).offset(0x16L).setu(v1);
        v0 = s2._58.get() & 0x3fL;
        v0 = v0 << 2;
        MEMORY.ref(1, s3).offset(0xcL).setu(v0);
        MEMORY.ref(1, s3).offset(0xdL).setu(s2._5a.get());
        v0 = s2._58.get() & 0x3fL;
        v0 = v0 << 2;
        MEMORY.ref(1, s3).offset(0x14L).setu(s2._5e.get() + v0 + 0xffL);
        MEMORY.ref(1, s3).offset(0x15L).setu(s2._5a.get());
        v0 = s2._58.get() & 0x3fL;
        v0 = v0 << 2;
        MEMORY.ref(1, s3).offset(0x1cL).setu(v0);
        MEMORY.ref(1, s3).offset(0x1dL).setu(s2._5f.get() + s2._5a.get() + 0xffL);
        v0 = s2._58.get() & 0x3fL;
        v0 = v0 << 2;
        MEMORY.ref(1, s3).offset(0x24L).setu(s2._5e.get() + v0 + 0xffL);
        MEMORY.ref(1, s3).offset(0x25L).setu(s2._5f.get() + s2._5a.get() + 0xffL);
        MEMORY.ref(2, s3).offset(0xeL).setu(s2.clut_5c.get() & 0x7fffL);

        v1 = sp0x28.getX() + sp0x38.getX();
        if((int)v1 >= 0) {
          a0 = 0x8000L;
          if((int)a0 >= (int)v1) {
            a0 = v1;
          }
        } else {
          //LAB_800fe46c
          a0 = 0;
        }
        sp0x28.setX((int)a0);

        //LAB_800fe470
        v1 = sp0x28.getY() + sp0x38.getY();
        if((int)v1 >= 0) {
          a0 = 0x8000L;
          if((int)a0 >= (int)v1) {
            a0 = v1;
          }
        } else {
          //LAB_800fe4a4
          a0 = 0;
        }
        sp0x28.setY((int)a0);

        //LAB_800fe4a8
        v1 = sp0x28.getZ() + sp0x38.getZ();
        if((int)v1 >= 0) {
          a2 = 0x8000L;
          if((int)a2 >= (int)v1) {
            a2 = v1;
          }
        } else {
          //LAB_800fe4dc
          a2 = 0;
        }

        //LAB_800fe4e4
        sp0x28.setZ((int)a2);
        MEMORY.ref(1, s3).offset(0x4L).setu(sp0x28.getX() >> 8);
        MEMORY.ref(1, s3).offset(0x5L).setu(sp0x28.getY() >> 8);
        MEMORY.ref(1, s3).offset(0x6L).setu(sp0x28.getZ() >> 8);

        s5 = (int)FUN_800fca78(data, s2, sp54, sp0x18, s3) >> 2;
        a0 = data._10._22.get();
        v1 = s5 + a0;
        if((int)v1 >= 0xa0L) {
          if((int)v1 >= 0xffeL) {
            a0 = 0xffeL - s5;
          }

          //LAB_800fe548
          a0 = s5 + a0;
          a0 = (int)a0 >> 2;
          a0 = a0 << 2;
          a0 = tags_1f8003d0.getPointer() + a0;
          insertElementIntoLinkedList(a0, s3);
        }

        //LAB_800fe564
        if((s2._08._1c.get() & 0x6000_0000L) != 0) {
          s1 = sp54._80.get();
          MEMORY.ref(4, s1).offset(0x0L).setu(MEMORY.ref(4, s3).offset(0x8L).get());
          MEMORY.ref(4, s1).offset(0x4L).setu(MEMORY.ref(4, s3).offset(0x10L).get());
          MEMORY.ref(4, s1).offset(0x8L).setu(MEMORY.ref(4, s3).offset(0x18L).get());
          MEMORY.ref(4, s1).offset(0xcL).setu(MEMORY.ref(4, s3).offset(0x20L).get());
          sp0x48.set(sp0x28).div(s2._54.get());
          v1 = s2._54.get();
          s6 = -sp54._04.get();
          if((int)s6 > (int)v1) {
            s6 = v1;
          }

          //LAB_800fe61c
          s4 = 0;

          //LAB_800fe628
          while((int)s4 < (int)s6) {
            a2 = s3;
            a1 = 0x9L;
            s0 = linkedListAddress_1f8003d8.get();
            a0 = s0;
            linkedListAddress_1f8003d8.addu(0x28L);

            //LAB_800fe644
            do {
              MEMORY.ref(4, a0).offset(0x0L).setu(MEMORY.ref(4, a2).offset(0x0L).get());
              a2 = a2 + 0x4L;
              a0 = a0 + 0x4L;
              a1 = a1 - 0x1L;
            } while((int)a1 >= 0);

            MEMORY.ref(1, s0).offset(0x3L).setu(0x9L);
            MEMORY.ref(4, s0).offset(0x4L).setu(0x2c80_8080L);
            v0 = data._10._00.get() >>> 29;
            v0 = v0 & 0x2L;
            MEMORY.ref(1, s0).offset(0x7L).oru(v0);
            a1 = data._10._00.get() >>> 30;
            a1 = a1 & 0x1L;
            gpuLinkedListSetCommandTransparency(s3, a1 != 0);
            MEMORY.ref(1, s0).offset(0x4L).setu(sp0x28.getX() >> 8);
            MEMORY.ref(1, s0).offset(0x5L).setu(sp0x28.getY() >> 8);
            MEMORY.ref(1, s0).offset(0x6L).setu(sp0x28.getZ() >> 8);
            sp0x28.sub(sp0x48);
            MEMORY.ref(2, s0).offset(0x8L).setu(MEMORY.ref(2, s1).offset(0x0L).get());
            MEMORY.ref(2, s0).offset(0xaL).setu(MEMORY.ref(2, s1).offset(0x2L).get());
            MEMORY.ref(2, s0).offset(0x10L).setu(MEMORY.ref(2, s1).offset(0x4L).get());
            MEMORY.ref(2, s0).offset(0x12L).setu(MEMORY.ref(2, s1).offset(0x6L).get());
            MEMORY.ref(2, s0).offset(0x18L).setu(MEMORY.ref(2, s1).offset(0x8L).get());
            MEMORY.ref(2, s0).offset(0x1aL).setu(MEMORY.ref(2, s1).offset(0xaL).get());
            MEMORY.ref(2, s0).offset(0x20L).setu(MEMORY.ref(2, s1).offset(0xcL).get());
            MEMORY.ref(2, s0).offset(0x22L).setu(MEMORY.ref(2, s1).offset(0xeL).get());

            a0 = data._10._22.get();
            v1 = s5 + a0;
            if((int)v1 >= 0xa0L) {
              if((int)v1 >= 0xffeL) {
                a0 = 0xffeL - s5;
              }

              //LAB_800fe78c
              insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s5 + a0) / 0x4L * 0x4L, s0);
            }

            //LAB_800fe7a8
            s1 = s1 + 0x10L;
            s4 = s4 + 0x1L;
          }
        }
      }

      //LAB_800fe7b8
    }

    sp0x28tmp.release();

    //LAB_800fe7ec
    if(s2._6c.get() != 0) {
      s2.vec_70.setX(s2.vec_70.getX() * s2._80.get() >> 8);
      s2.vec_70.setY(s2.vec_70.getY() * s2._80.get() >> 8);
      s2.vec_70.setZ(s2.vec_70.getZ() * s2._80.get() >> 8);
    }

    //LAB_800fe848
  }

  @Method(0x800fe878L)
  @Nullable
  public static BttlScriptData6cSub98 FUN_800fe878(final BttlScriptData6cSub98 a0) {
    if(_8011a00c.getPointer() == a0.getAddress()) {
      return null;
    }

    //LAB_800fe894
    BttlScriptData6cSub98 v0 = _8011a00c.deref();
    do {
      if(v0._94.getPointer() == a0.getAddress()) {
        break;
      }

      v0 = v0._94.derefNullable();
    } while(v0 != null);

    //LAB_800fe8b0
    return v0;
  }

  @Method(0x800fe8b8L)
  public static void FUN_800fe8b8(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub98 s2 = data._44.derefAs(BttlScriptData6cSub98.class);
    final BttlScriptData6cSub98 a0 = FUN_800fe878(s2);

    if(a0 == null) {
      _8011a00c.setNullable(s2._94.derefNullable());
    } else {
      //LAB_800fe8f0
      a0._94.setNullable(s2._94.derefNullable());
    }

    //LAB_800fe8fc
    if(s2._94.isNull()) {
      _8011a010.setNullable(a0);
    }

    //LAB_800fe910
    if(s2._54.get() != 0) {
      switch(s2._60.get()) {
        case 2, 4, 5 -> {
          //LAB_800fe960
          for(int i = 0; i < s2._50.get(); i++) {
            removeFromLinkedList(s2._68.deref().get(i)._44.getPointer());
          }
        }

        case 0 -> {
          if((s2._24.get() & 0x6000_0000L) != 0) {
            //LAB_800fe9b4
            for(int i = 0; i < s2._50.get(); i++) {
              removeFromLinkedList(s2._68.deref().get(i)._80.get());
            }
          }
        }

        case 1 -> {
          if((s2._24.get() & 0x6000_0000L) != 0) {
            //LAB_800fea08
            for(int i = 0; i < s2._50.get(); i++) {
              removeFromLinkedList(s2._68.deref().get(i)._44.getPointer());
            }
          }
        }
      }
    }

    //LAB_800fea30
    //LAB_800fea3c
    removeFromLinkedList(s2._68.getPointer());
    FUN_80102534();
  }

  @Method(0x800fea68L)
  public static void FUN_800fea68(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    // no-op
  }

  @Method(0x800fea70L)
  public static long FUN_800fea70(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    seed_800fa754.advance();
    final long theta = seed_800fa754.get() % 4097;
    a2._58.setX((short)(rcos(theta) >> 8));
    a2._58.setZ((short)(rsin(theta) >> 8));

    seed_800fa754.advance();
    a2._58.setY((short)-(seed_800fa754.get() % 91 + 10));

    seed_800fa754.advance();
    final long a0_0 = seed_800fa754.get() % 101 - 50 << 8;
    a2._84.add((short)a0_0);
    a2._86.add((short)a0_0);
    a2._88.add((short)a0_0);

    return theta;
  }

  @Method(0x800fec3cL)
  public static void FUN_800fec3c(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    final long s0 = (short)FUN_800fea70(a0, a1, a2, a3);
    seed_800fa754.advance();
    a2._58.setX((short)(rcos(s0) >> 6));
    a2._58.setY((short)0);
    a2._58.setZ((short)(rsin(s0) >> 6));
  }

  @Method(0x800fecccL)
  public static void FUN_800feccc(final EffectManagerData6c u0, final BttlScriptData6cSub98 u1, final BttlScriptData6cSub98Sub94 s2, final BttlScriptData6cSub98Inner24 a3) {
    seed_800fa754.advance();
    final long s0 = seed_800fa754.get() % 4097;
    s2._58.setX((short)(rcos(s0) >> 10));
    seed_800fa754.advance();
    s2._58.setY((short)-(seed_800fa754.get() % 33 + 13));
    s2._58.setZ((short)(rsin(s0) >> 10));
    seed_800fa754.advance();
    s2._14.set((short)(seed_800fa754.get() % 3 + 1));
    seed_800fa754.advance();
    s2._16.set((short)(seed_800fa754.get() % 3));
    s2._04.set((short)(s2._04.get() / 4 * 4 + 1));
  }

  @Method(0x800fee9cL)
  public static void FUN_800fee9c(final EffectManagerData6c u0, final BttlScriptData6cSub98 u1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    seed_800fa754.advance();
    final long theta = seed_800fa754.get() % 4097;
    a2._58.setX((short)(rcos(theta) / 0x80));
    a2._58.setY((short)0);
    a2._58.setZ((short)(rsin(theta) / 0x80));
    a2._04.set((short)1);
    seed_800fa754.advance();
    a2._14.set((short)(seed_800fa754.get() % 6));
  }

  @Method(0x800fefe4L)
  public static void FUN_800fefe4(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    FUN_800fee9c(a0, a1, a2, a3);
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    a2._16.set(a3._10.get());
    a2._18.set((short)(seed_800fa754.advance().get() % 91 + 10));
    a2._1a.setX((short)120);
    a2._58.setX((short)0);
    a2._58.setY((short)-(seed_800fa754.advance().get() % 11 + 5));
    a2._58.setZ((short)0);
  }

  @Method(0x800ff15cL)
  public static void FUN_800ff15c(final EffectManagerData6c u0, final BttlScriptData6cSub98 u1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    seed_800fa754.advance();
    a2._58.setY((short)-(seed_800fa754.get() % 61 + 60));

    final long v1;
    if(vsyncMode_8007a3b8.get() != 0x4L) {
      seed_800fa754.advance();
      v1 = -(seed_800fa754.get() % 14 + 5);
    } else {
      //LAB_800ff248
      seed_800fa754.advance();
      v1 = -(seed_800fa754.get() % 21 + 10);
    }

    a2._0a.set((short)v1);
    a2._0c.set((short)v1);

    //LAB_800ff2b4
    seed_800fa754.advance();
    a2._14.set((short)(seed_800fa754.get() % 4097));
    seed_800fa754.advance();
    a2._1a.setX((short)(seed_800fa754.get() % 4097));
    seed_800fa754.advance();
    a2._1a.setZ((short)(seed_800fa754.get() % 1025 - 512));

    a2._1a.setY((short)100);
    a2._16.set(a3._10.get());
    a2._18.set((short)(a3._18.get() * 100 / 256));
  }

  @Method(0x800ff3e0L)
  public static void FUN_800ff3e0(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    FUN_800ff15c(a0, a1, a2, a3);
    a2._20.set((short)0);
    a2._22.set((short)(0x8000 / a2._12.get()));
    a2._24.set((short)(MEMORY.ref(1, a3.getAddress()).offset(0x1dL).get() << 8)); //TODO
  }

  @Method(0x800ff430L)
  public static void FUN_800ff430(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._16.set(a3._10.get());
    a2._18.set((short)0);
    a2._1a.setX((short)0);
    a2._1a.setY((short)50);
    a2._20.set((short)100);
    a2._22.set((short)0);
    a2._24.set((short)(a2._10.get() / a2._12.get()));

    a2._58.setY((short)(seed_800fa754.advance().get() % 31 + 10));
    a2._1a.setZ((short)(seed_800fa754.advance().get() % 41 + 40));
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x800ff590L)
  public static void FUN_800ff590(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    FUN_800ff430(a0, a1, a2, a3);
    a2._22.set((short)20);
    a2._20.set((short)10);
  }

  /**
   * {@link SEffe#FUN_800ff5c4} uses t2 which isn't set... assuming the value even matters, this is to pass in t2 from the previous method
   */
  private static Long brokenT2For800ff5c4;

  @Method(0x800ff5c4L)
  public static void FUN_800ff5c4(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    if(brokenT2For800ff5c4 == null) {
      throw new RuntimeException("t2 was not set");
    }

    final long t2 = brokenT2For800ff5c4;
    brokenT2For800ff5c4 = null;

    seed_800fa754.advance();
    a2._58.setY((short)(-(seed_800fa754.get() % 61 + 60) * a3._18.get() / 256));
    seed_800fa754.advance();
    a2._14.set((short)(seed_800fa754.get() % 4097));
    a2._16.set(a3._10.get());
    a2._18.set((short)100);
    a2._1a.setX((short)(t2 * 4));
    a2._60.setY((short)(-a2._58.getY() / a2._12.get()));
  }

  @Method(0x800ff6d4L)
  public static void FUN_800ff6d4(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    FUN_800ff5c4(a0, a1, a2, a3);
    a2._18.set((short)0);
  }

  @Method(0x800ff6fcL)
  public static void FUN_800ff6fc(final EffectManagerData6c u0, final BttlScriptData6cSub98 u1, final BttlScriptData6cSub98Sub94 u2, final BttlScriptData6cSub98Inner24 a3) {
    final long a2 = a3._10.get();
    final long a0 = _8011a008.get();
    MEMORY.ref(2, a2).offset(0x10L).setu(-0x80);
    final long v1 = a0 >>> 1;
    MEMORY.ref(2, a2).offset(0xeL).setu(v1 * 128);
    MEMORY.ref(2, a2).offset(0x14L).setu(v1 * 128);
    MEMORY.ref(2, a2).offset(0x16L).setu(a2);
    MEMORY.ref(2, a2).offset(0x18L).setu(a0 & 0x1L);
    MEMORY.ref(2, a2).offset(0x1aL).setu(v1 * 8);

    final long a1 = Math.max(0, MEMORY.ref(1, a3.getAddress()).offset(0x1dL).get() - a0 * 16); //TODO

    //LAB_800ff754
    MEMORY.ref(2, a2).offset(0x84L).setu(a1 << 8);
    MEMORY.ref(2, a2).offset(0x86L).setu(a1);
    MEMORY.ref(2, a2).offset(0x12L).setu(-1);
    MEMORY.ref(2, a2).offset(0x88L).setu(a1);
    MEMORY.ref(2, a2).offset(0x86L).setu(a1 << 8);
    MEMORY.ref(2, a2).offset(0x88L).setu(a1 << 8);
  }

  @Method(0x800ff788L)
  public static void FUN_800ff788(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._12.set((short)-1);
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    a2._16.set(a3._10.get());
    a2._18.set((short)(seed_800fa754.advance().get() % 4097));
    a2._1a.setX((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x800ff890L)
  public static void FUN_800ff890(final EffectManagerData6c u0, final BttlScriptData6cSub98 u1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    seed_800fa754.advance();
    seed_800fa754.advance();
    final long s1 = seed_800fa754.get() % 4097;

    seed_800fa754.advance();
    final long s0 = seed_800fa754.get() % 2049;

    a2._58.setX((short)(rcos(s1) * rsin(s0) / 0x40000));
    a2._58.setY((short)(rcos(s0) / 0x40));
    a2._58.setZ((short)(rsin(s1) * rsin(s0) / 0x40000));

    seed_800fa754.advance();
    a2._12.add((short)(seed_800fa754.get() % 21 - 10));

    if(a2._12.get() <= 0) {
      a2._12.set((short)1);
    }

    //LAB_800ffa60
  }

  @Method(0x800ffa80L)
  public static void FUN_800ffa80(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    FUN_800ff5c4(a0, a1, a2, a3);
    final short s2 = a3._10.get();
    a2._16.set(s2);
    a2._1a.setY(s2);
    a2._18.set((short)(a3._18.get() * 0xe0 >> 8));
  }

  @Method(0x800ffadcL)
  public static void FUN_800ffadc(final EffectManagerData6c u0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    final int v0 = -a3._10.get() >> 5;
    final int a0 = a3._18.get();
    a2._84.set((short)0);
    a2._86.set((short)0);
    a2._88.set((short)0);
    a2._16.set(a3._10.get());
    a2._18.set((short)(v0 * a0 >> 8));
    a2._1a.setX((short)(a0 >>> 1));
  }

  @Method(0x800ffb80L)
  public static void FUN_800ffb80(final EffectManagerData6c u0, final BttlScriptData6cSub98 u1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    final long v0;
    long a0;
    a2._60.setY((short)8);
    a0 = _8011a008.get() & 0xffffL;
    v0 = a0 << 9;
    a2._14.set((short)v0);
    a0 = a0 >>> 2;
    a0 = a0 | 0x1L;
    a2._16.set(a3._10.get());
    a2._04.set((short)a0);
    a2._18.set((short)(a3._18.get() >>> 2));
    a2._58.setY((short)(a3._14.get() * -0x40 / 0x100));
  }

  @Method(0x800ffbd8L)
  public static void FUN_800ffbd8(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    final long t6 = a3._18.get();
    a2._14.set((short)0);
    a2._18.set((short)(seed_800fa754.advance().get() % 21 - 10));
    a2._1a.setX((short)(seed_800fa754.advance().get() % 21 - 10));
    a2._1a.setY((short)(seed_800fa754.advance().get() % 81 - 40));
    a2._1a.setZ(a2._12.get());
    a2._58.setX((short)((seed_800fa754.advance().get() % 41 + 44) * t6 >> 8));
    a2._58.setY((short)((seed_800fa754.advance().get() % 81 - 40) * t6 >> 8));
    a2._58.setZ((short)((seed_800fa754.advance().get() % 41 + 44) * t6 >> 8));
    a2._12.add((short)20);
  }

  @Method(0x800ffe80L)
  public static void FUN_800ffe80(final EffectManagerData6c u0, final BttlScriptData6cSub98 u1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._16.set(a3._10.get());

    final long a0 = a3._18.get();
    a2._60.setY((short)(a0 >>> 7));
    a2._18.set((short)(a0 >>> 3));

    seed_800fa754.advance();
    a2._14.set((short)(seed_800fa754.get() % 4097));
  }

  @Method(0x800ffefcL)
  public static void FUN_800ffefc(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    // no-op
  }

  @Method(0x800fff04L)
  public static void FUN_800fff04(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._14.set((short)0);
    a2._18.set((short)0);
    a2._1a.setZ((short)0);
    final short v0 = (short)((a3._18.get() & 0xffff) >>> 2);
    a2._16.set(v0);
    a2._1a.setX(v0);
    a2._1a.setY(a3._10.get());
  }

  @Method(0x800fff30L)
  public static void FUN_800fff30(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._58.setX((short)(seed_800fa754.advance().get() % 769 + 256));
  }

  @Method(0x800fffa0L)
  public static void FUN_800fffa0(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._1a.setX(a3._10.get());
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    a2._16.set((short)((seed_800fa754.advance().get() % 123 + 64) * a3._18.get() >> 8));
    a2._18.set((short)(0x800 / a1._50.get() * (_8011a008.get() & 0xffff)));
  }

  @Method(0x801000b8L)
  public static void FUN_801000b8(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._58.setX((short)(-a2._50.getX() / 32));
    a2._58.setZ((short)(-a2._50.getZ() / 32));
  }

  @Method(0x801000f8L)
  public static void FUN_801000f8(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    FUN_800ff890(a0, a1, a2, a3);
    a2._58.setY((short)-Math.abs(a2._58.getY()));
    a2._14.set((short)(a3._18.get() * 0x300 / 0x100));
  }

  @Method(0x80100150L)
  public static void FUN_80100150(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    seed_800fa754.advance();
    a2._1a.setX((short)(seed_800fa754.get() % 4097));
    a2._14.set(a2._50.getX());
    a2._16.set(a2._50.getY());
    a2._18.set(a2._50.getZ());
    a2._1a.setZ((short)(a3._10.get() >>> 2));
    a2._58.setY((short)(a3._18.get() * -64 >> 8));
    seed_800fa754.advance();
    a2._1a.setY((short)((int)((seed_800fa754.get() % 513 - 256) * a3._18.get()) >> 8));
  }

  @Method(0x8010025cL)
  public static void FUN_8010025c(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._58.setY((short)64);
    final long theta = seed_800fa754.advance().get() % 4097;
    if(a3._20.get() == 0x2a) {
      final int s0 = (a3._10.get() & 0xffff) >>> 5;
      a2._58.setX((short)(rcos(theta) * s0 >> 12));
      a2._58.setY((short)(rsin(theta) * s0 >> 12));
    } else {
      //LAB_80100328
      a2._58.setX((short)(rcos(theta) >>> 7));
      a2._58.setY((short)(rsin(theta) >>> 7));
    }
  }

  @Method(0x801003e8L)
  public static void FUN_801003e8(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    final long s4 = a3._10.get(); //TODO read with lw here but as a short everywhere else? Is this a bug?
    FUN_800ff890(a0, a1, a2, a3);

    final long s1 = seed_800fa754.advance().get() % 4097;
    final long s0 = seed_800fa754.advance().get() % 2049;
    a2._12.set((short)(a3._1c.get() >>> 16 & 0xff));
    a2._50.setX((short)((rcos(s1) * rsin(s0) >> 12) * s4 >> 12));
    a2._50.setY((short)(rcos(s0) * s4 >> 12));
    a2._50.setZ((short)((rsin(s1) * rsin(s0) >> 12) * s4 >> 12));
    a2._58.setX((short)(rcos(s1) * rsin(s0) >> 18));
    a2._58.setY((short)(rcos(s0) >> 6));
    a2._58.setZ((short)(rsin(s1) * rsin(s0) >> 18));
  }

  @Method(0x80100364L)
  public static void FUN_80100364(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    FUN_800ff890(a0, a1, a2, a3);
    a2._14.set((short)0);
    a2._58.setY((short)-Math.abs(a2._58.getY()));
    a2._60.set(a2._58).negate().div(a2._12.get());
  }

  @Method(0x801007b4L)
  public static void FUN_801007b4(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    a2._58.set(a2._50).negate().div(a2._12.get());
  }

  @Method(0x80100af4L)
  public static void FUN_80100af4(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    FUN_800ff890(a0, a1, a2, a3);
    a2._58.setX((short)(a2._58.getX() * a3._18.get() >> 8));
    a2._58.setY((short)(a2._58.getY() * a3._18.get() >> 8));
    a2._58.setZ((short)(a2._58.getZ() * a3._18.get() >> 8));
    a2._60.set(a2._58).negate().div(a2._12.get());
  }

  @Method(0x80100bb4L)
  public static void FUN_80100bb4(final EffectManagerData6c a0, final BttlScriptData6cSub98 a1, final BttlScriptData6cSub98Sub94 a2, final BttlScriptData6cSub98Inner24 a3) {
    seed_800fa754.advance();
    a2._58.setY((short)(seed_800fa754.get() % 33 + 16));
  }

  @Method(0x80100d58L)
  public static void FUN_80100d58(final long a0, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    // no-op
  }

  @Method(0x80100d60L)
  public static void FUN_80100d60(final long scriptIndex, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    if(a3._04.get() == 0 && a2.scriptIndex_04.get() != -1) {
      final VECTOR sp0x20 = new VECTOR();
      FUN_800cea1c((int)scriptIndex, sp0x20);

      final VECTOR sp0x30 = new VECTOR();
      FUN_800cea1c(a2.scriptIndex_04.get(), sp0x30);

      final VECTOR sp0x10 = new VECTOR().set(sp0x20).sub(sp0x30);
      a3._50.set(sp0x10);
      a3._50.set(sp0x10);
    }

    //LAB_80100e14
  }

  @Method(0x80100e28L)
  public static void FUN_80100e28(final long a0, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    if(a3._04.get() == 0) {
      a3._8a.set((short)0);
      a3._8c.set((short)0);
      a3._8e.set((short)0);
    }

    //LAB_80100e44
  }

  @Method(0x80100e4cL)
  public static void FUN_80100e4c(final long a0, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    if(a3._04.get() == 0) {
      a3._8a.set((short)(-0x8000 / a3._12.get()));
      a3._8c.set((short)(-0x8000 / a3._12.get()));
      a3._8e.set((short)(-0x8000 / a3._12.get()));
    }

    //LAB_80100e98
  }

  @Method(0x80100ea0L)
  public static void FUN_80100ea0(final long a0, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    final int s2 = a2._08._18.get();
    final int s1 = a2._08._10.get() & 0xffff;

    final VECTOR sp0x10 = new VECTOR();
    final VECTOR sp0x20 = new VECTOR();
    FUN_800cea1c(a2.scriptIndex_00.get(), sp0x10);
    FUN_800cea1c(a2.scriptIndex_04.get(), sp0x20);

    final VECTOR sp0x30 = new VECTOR().set(sp0x20).sub(sp0x10);

    final int mod = s1 * 2 + 1;
    a3._58.setX((short)((sp0x30.getX() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    a3._58.setY((short)((sp0x30.getY() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    a3._58.setZ((short)((sp0x30.getZ() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    a3._58.x.mul((short)s2).shra(8);
    a3._58.y.mul((short)s2).shra(8);
    a3._58.z.mul((short)s2).shra(8);

    if(a3._58.getX() == 0) {
      a3._58.setX((short)1);
    }

    //LAB_80101068
    a3._12.set((short)(sp0x30.getX() / a3._58.getX()));
  }

  @Method(0x801010a0L)
  public static void FUN_801010a0(final long a0, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    //TODO wtf
    final long t0 = a3.getAddress() - a2._68.deref().get(0).getAddress();
    long v1 = t0 << 6;
    v1 = v1 - t0;
    v1 = v1 << 2;
    v1 = v1 + t0;
    long v0 = v1 << 3;
    v0 = v0 - v1;
    v0 = v0 << 2;
    v0 = v0 + t0;
    v1 = v0 << 18;
    v0 = v0 - v1;
    v0 = (int)v0 >> 2;
    _8011a008.setu(v0);

    FUN_80101308(a0, a1, a2, a3, a2._08);

    if(a2._54.get() != 0) {
      v1 = a2._60.get();

      if(v1 == 0) {
        //LAB_801011a0
        final long s1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);
        MEMORY.ref(1, s1).offset(0x3L).setu(0x9L);
        MEMORY.ref(4, s1).offset(0x4L).setu(0x2c80_8080L);
        long s0 = a3._80.get();

        //LAB_801011d8
        for(int i = 0; i < a2._54.get(); i++) {
          final VECTOR sp0x18 = new VECTOR().set(a3._50);
          FUN_800fca78(a1, a2, a3, sp0x18, s1);
          MEMORY.ref(2, s0).offset(0x0L).setu(MEMORY.ref(2, s1).offset(0x08L).get());
          MEMORY.ref(2, s0).offset(0x2L).setu(MEMORY.ref(2, s1).offset(0x0aL).get());
          MEMORY.ref(2, s0).offset(0x4L).setu(MEMORY.ref(2, s1).offset(0x10L).get());
          MEMORY.ref(2, s0).offset(0x6L).setu(MEMORY.ref(2, s1).offset(0x12L).get());
          MEMORY.ref(2, s0).offset(0x8L).setu(MEMORY.ref(2, s1).offset(0x18L).get());
          MEMORY.ref(2, s0).offset(0xaL).setu(MEMORY.ref(2, s1).offset(0x1aL).get());
          MEMORY.ref(2, s0).offset(0xcL).setu(MEMORY.ref(2, s1).offset(0x20L).get());
          MEMORY.ref(2, s0).offset(0xeL).setu(MEMORY.ref(2, s1).offset(0x22L).get());
          s0 = s0 + 0x10L;
        }
        //LAB_8010114c
      } else if(v1 == 0x2L || (int)v1 >= 0x4L && (int)v1 < 0x6L) {
        //LAB_80101160
        //LAB_80101170
        for(int i = 0; i < a2._54.get(); i++) {
          a3._44.deref().get(i).set(a3._50);
        }
      }
    }

    //LAB_8010127c
  }

  @Method(0x801012a0L)
  public static void FUN_801012a0(final long a0, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    if((a1._10._24.get() & 0x4L) != 0) {
      FUN_801010a0(a0, a1, a2, a3);
    }

    //LAB_801012c4
  }

  @Method(0x801012d4L)
  public static void FUN_801012d4(final long a0, final EffectManagerData6c a1, final BttlScriptData6cSub98 a2, final BttlScriptData6cSub98Sub94 a3) {
    if((a1._10._24.get() & 0x4L) == 0) {
      FUN_801010a0(a0, a1, a2, a3);
    }

    //LAB_801012f8
  }

  @Method(0x80101308L)
  public static void FUN_80101308(long a0, final EffectManagerData6c sp3c, final BttlScriptData6cSub98 fp, final BttlScriptData6cSub98Sub94 s3, final BttlScriptData6cSub98Inner24 a4) {
    long v0;
    long v1;
    final long a1;
    final long t2;
    final long s0;
    final long s2;
    final long s4;
    final long s5;
    final long s7;

    if((a4._1c.get() & 0xffL) == 0) {
      a4._1c.and(0xffff_ff00L).or(_801197ec.offset(a4._20.get() * 0x4L).offset(1, 0x2L).get());
    }

    //LAB_8010137c
    if((a4._1c.get() & 0xff00L) == 0) {
      a4._1c.and(0xffff_00ffL).or(_801197ec.offset(a4._20.get() * 0x4L).offset(1, 0x1L).get() << 8);
    }

    //LAB_801013c0
    if((a4._1c.get() & 0xff_0000L) == 0) {
      a4._1c.and(0xff00_ffffL).or(_801197ec.offset(a4._20.get() * 0x4L).offset(1, 0x0L).get() << 16);
    }

    //LAB_80101400
    fp._61.set((int)_801197ec.offset(a4._20.get() * 0x4L).offset(1, 0x3L).get());
    s3._00.set(0x73);
    s3._01.set(0x6d);
    s3._02.set(0x6b);
    s3._50.set((short)0, (short)0, (short)0);
    s3._58.set((short)0, (short)0, (short)0);
    s3._06.set((short)0);
    s3._08.set((short)0);
    s3._0a.set((short)0);
    s3._0c.set((short)0);
    s3._60.set((short)0, (short)0, (short)0);
    s3._8a.set((short)0);
    s3._8c.set((short)0);
    s3._8e.set((short)0);
    t2 = a4._20.get() * 10;
    brokenT2For800ff5c4 = t2;
    seed_800fa754.advance();
    s3._04.set((short)(seed_800fa754.get() % (a4._14.get() + 0x1L) + 0x1L));
    seed_800fa754.advance();
    s3._12.set((short)((a4._1c.get() & 0xff_0000L) >>> 16));
    a0 = (a4._1c.get() & 0xff00L) >>> 8;
    s3._84.set((short)a0);
    s3._86.set((short)a0);
    s3._88.set((short)a0);
    s3._90.or(0x90L);
    s3._0e.set((short)(seed_800fa754.get() % 4097));
    seed_800fa754.advance();
    s3._10.set((short)(seed_800fa754.get() % 513 - 256));
    seed_800fa754.advance();
    s3._70.setX((short)(seed_800fa754.get() % 4097));
    seed_800fa754.advance();
    s3._70.setY((short)(seed_800fa754.get() % 4097));
    seed_800fa754.advance();
    s3._70.setZ((short)(seed_800fa754.get() % 4097));
    seed_800fa754.advance();
    s3._78.setX((short)(seed_800fa754.get() % 129 - 64));
    seed_800fa754.advance();
    s3._78.setY((short)(seed_800fa754.get() % 129 - 64));
    seed_800fa754.advance();
    s3._78.setZ((short)0);
    v0 = seed_800fa754.get() % 101;
    v0 = v0 < 50 ? 1 : 0;
    v0 = v0 ^ 0x1L;
    v0 = v0 * 8;
    v1 = s3._90.get() & 0xffff_fff7L;
    v1 = v1 | v0;
    v1 = v1 & 0xffff_fff9L;
    s3._90.set(v1);
    s3._84.shl(8);
    s3._88.shl(8);
    s3._86.shl(8);
    s5 = _801198f0.offset(t2).getAddress();
    v1 = MEMORY.ref(1, s5).get();
    s7 = a4._20.get();
    if(v1 == 0x1L) {
      //LAB_80101840
      seed_800fa754.advance();
      s2 = seed_800fa754.get() % 4097;
      s0 = a4._10.get();
      s3._50.setX((short)(rcos(s2) * (int)s0 >> MEMORY.ref(2, s5).offset(0x2L).getSigned()));
      s3._50.setY((short)0);
      s3._50.setZ((short)(rsin(s2) * (int)s0 >> MEMORY.ref(2, s5).offset(0x2L).getSigned()));
      //LAB_80101824
    } else if(v1 == 0x2L) {
      //LAB_801018c8
      seed_800fa754.advance();
      s2 = seed_800fa754.get() % 4097;
      seed_800fa754.advance();
      s4 = seed_800fa754.get() % (a4._10.get() + 0x1L);
      s3._50.setX((short)(rcos(s2) * s4 >> MEMORY.ref(2, s5).offset(0x2L).getSigned()));
      s3._50.setY((short)0);
      s3._50.setZ((short)(rsin(s2) * s4 >> MEMORY.ref(2, s5).offset(0x2L).getSigned()));
    } else if(v1 == 0x3L) {
      //LAB_80101990
      seed_800fa754.advance();
      s3._50.setY((short)(seed_800fa754.get() % (MEMORY.ref(2, s5).offset(0x4L).getSigned() - MEMORY.ref(2, s5).offset(0x2L).getSigned() + 0x1L) + MEMORY.ref(2, s5).offset(0x2L).getSigned()));
    } else if(v1 == 0x4L) {
      //LAB_801019e4
      seed_800fa754.advance();
      s2 = seed_800fa754.get() % 4097;
      seed_800fa754.advance();
      s4 = seed_800fa754.get() % 2049;
      s3._50.setX((short)((rcos(s2) * rsin(s4) >> MEMORY.ref(2, s5).offset(0x2L).getSigned()) * a4._10.get() >> MEMORY.ref(2, s5).offset(0x4L).getSigned()));
      s3._50.setX((short)(rcos(s4) * a4._10.get() >> MEMORY.ref(2, s5).offset(0x4L).getSigned()));
      s3._50.setZ((short)((rsin(s2) * rsin(s4) >> MEMORY.ref(2, s5).offset(0x2L).getSigned()) * a4._10.get() >> MEMORY.ref(2, s5).offset(0x4L).getSigned()));
    }

    //LAB_80101b10
    //LAB_80101b18
    fp._8c.deref().run(sp3c, fp, s3, a4);

    a1 = _801198f0.offset(s7 * 0xaL).getAddress();
    if(MEMORY.ref(1, a1).offset(0x6L).get() == 0x1L) {
      s3._58.setX((short)(s3._58.getX() * a4._18.get() >> 8));
      s3._58.setY((short)(s3._58.getY() * a4._18.get() >> 8));
      s3._58.setZ((short)(s3._58.getZ() * a4._18.get() >> 8));
    }

    //LAB_80101ba4
    if(MEMORY.ref(1, a1).offset(0x7L).get() == 0x1L) {
      seed_800fa754.advance();
      v1 = (short)(seed_800fa754.get() % (MEMORY.ref(1, a1).offset(0x9L).getSigned() - MEMORY.ref(1, a1).offset(0x8L).getSigned() + 0x1L) + MEMORY.ref(1, a1).offset(0x8L).getSigned());
      s3._0a.set((short)v1);
      s3._0c.set((short)v1);
    }

    //LAB_80101c20
    s3._48.set(s3._50);
  }

  @Method(0x80101c68L)
  public static void FUN_80101c68(final BttlScriptData6cSub98 a0, final BttlScriptData6cSub98Sub94 a1, final BttlScriptData6cSub98Inner24 a2, final long a3) {
    a0._60.set(3);
  }

  @Method(0x80101c74L)
  public static void FUN_80101c74(final BttlScriptData6cSub98 a0, final BttlScriptData6cSub98Sub94 a1, final BttlScriptData6cSub98Inner24 a2, final long a3) {
    a0._60.set((int)a3 >> 20);
    a0._54.set((short)a3);

    //LAB_80101cb0
    for(int s3 = 0; s3 < a0._50.get(); s3++) {
      final BttlScriptData6cSub98Sub94 s2 = a0._68.deref().get(s3);
      s2._44.setPointer(addToLinkedListHead(a0._54.get() * 0x8L));

      //LAB_80101cdc
      for(int s1 = 0; s1 < a0._54.get(); s1++) {
        memcpy(s2._44.deref().get(s1).getAddress(), s2._50.getAddress(), 8);
      }

      //LAB_80101d04
    }

    //LAB_80101d1c
  }

  @Method(0x80101d3cL)
  public static void FUN_80101d3c(final BttlScriptData6cSub98 a0, final BttlScriptData6cSub98Sub94 a1, final BttlScriptData6cSub98Inner24 a2, final long a3) {
    a0._54.set(0);
    a0._60.set(1);

    if((a3 & 0xf_ff00L) == 0xf_ff00L) {
      a0._30.set(_800c6944.offset((a3 & 0xffL) * 0x4L).deref(4).cast(TmdObjTable::new));
      a0._56.set(0x20);
    } else {
      //LAB_80101d98
      long v0 = FUN_800eac58(0x300_0000L | a3 & 0xf_ffffL).getAddress(); //TODO
      v0 = v0 + MEMORY.ref(4, v0).offset(0xcL).get();
      a0._30.setPointer(v0 + 0x18L);
      a0._56.set((int)((MEMORY.ref(4, v0).offset(0xcL).get() & 0xffff_0000L) >>> 11));
    }

    //LAB_80101dd8
    if((a0._24.get() & 0x6000_0000L) != 0) {
      a0._54.set((int)_800fb794.offset((a0._24.get() & 0x6000_0000L) >>> 27).get());

      //LAB_80101e3c
      for(int i = 0; i < a0._50.get(); i++) {
        a0._68.deref().get(i)._44.setPointer(addToLinkedListHead(a0._54.get() * 8));
      }
    }

    //LAB_80101e6c
  }

  @Method(0x80101e84L)
  public static void FUN_80101e84(final BttlScriptData6cSub98 a0, final BttlScriptData6cSub98Sub94 a1, final BttlScriptData6cSub98Inner24 a2, final long a3) {
    a0._60.set(0);
    a0._54.set(0);

    if((a0._08._1c.get() & 0x6000_0000L) != 0) {
      final long v0 = (a0._08._1c.get() & 0x6000_0000L) >>> 27;
      a0._54.set((int)_800fb794.offset(2, v0).get());

      //LAB_80101f2c
      for(int i = 0; i < a0._50.get(); i++) {
        final BttlScriptData6cSub98Sub94 s2 = a0._68.deref().get(i);
        final long v1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);
        MEMORY.ref(1, v1).offset(0x3L).setu(0x9L);
        MEMORY.ref(4, v1).offset(0x4L).setu(0x2c80_8080L);
        s2._80.set(addToLinkedListHead(a0._54.get() * 0x10L));
      }
    }

    //LAB_80101f70
    if((a3 & 0xf_ff00L) == 0xf_ff00L) {
      final long v0 = _800c6948.get() + (a3 & 0xffL) * 0x8L;
      a0._58.set((int)MEMORY.ref(2, v0).offset(0x0L).get());
      a0._5a.set((int)MEMORY.ref(2, v0).offset(0x2L).get());
      a0._5e.set((int)MEMORY.ref(1, v0).offset(0x4L).get());
      a0._5f.set((int)MEMORY.ref(1, v0).offset(0x5L).get());
      a0.clut_5c.set((int)MEMORY.ref(2, v0).offset(0x6L).get());
    } else {
      //LAB_80101fec
      long v0 = FUN_800eac58(a3 | 0x400_0000L).getAddress(); //TODO
      v0 = v0 + MEMORY.ref(4, v0).offset(0x8L).get();
      a0._58.set((int)MEMORY.ref(2, v0).offset(0x0L).get());
      a0._5a.set((int)MEMORY.ref(2, v0).offset(0x2L).get());
      a0._5e.set((int)(MEMORY.ref(1, v0).offset(0x4L).get() * 0x4L));
      a0._5f.set((int)MEMORY.ref(1, v0).offset(0x6L).get());
      a0.clut_5c.set((int)GetClut(MEMORY.ref(2, v0).offset(0x8L).getSigned(), MEMORY.ref(2, v0).offset(0xaL).getSigned()));
    }

    //LAB_80102048
    a0._34.set(a0._5e.get() >>> 1);
    a0._36.set(a0._5f.get() >>> 1);
  }

  @Method(0x80102088L)
  public static long FUN_80102088(final RunningScript s2) {
    final int s6 = allocateEffectManager(s2.scriptStateIndex_00.get(), 0x98L, null, _80119b7c.get(s2.params_20.get(2).deref().get() >> 20).deref(), MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_800fe8b8", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), BttlScriptData6cSub98::new);
    final long v1 = s2.params_20.get(3).deref().get() & 0xffffL;
    final long s0 = v1 * 0x94L;
    final EffectManagerData6c s3 = scriptStatePtrArr_800bc1c0.get(s6).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub98 s1 = s3._44.derefAs(BttlScriptData6cSub98.class);
    s1._50.set(s2.params_20.get(3).deref().get());
    s1._68.setPointer(addToLinkedListHead(s0));

    if(_8011a00c.isNull()) {
      _8011a00c.set(s1);
    }

    //LAB_801021a8
    if(!_8011a010.isNull()) {
      _8011a010.deref()._94.set(s1);
    }

    //LAB_801021c0
    s1.size_64.set(s0);
    s1.scriptIndex_04.set(s2.params_20.get(1).deref().get());
    s1.scriptIndex_00.set(s6);
    s1._84.set(_80119bac.get(s2.params_20.get(8).deref().get()).deref());
    s1._88.set(_80119cb0.get(s2.params_20.get(8).deref().get()).deref());
    _8011a010.set(s1);
    s1._52.set(0);
    s1._34.set(0);
    s1._36.set(0);
    s1._6c.set(0);
    s1._94.clear();
    s1._8c.set(_80119db4.get(s2.params_20.get(8).deref().get()).deref());
    s2.params_20.get(0).deref().set(s6);

    //LAB_8010223c
    for(int i = 0; i < 9; i++) {
      //TODO this seems weird
      MEMORY.ref(4, s1._08.getAddress()).offset(i * 0x4L).setu(s2.params_20.get(i).deref().get());
    }

    final int v0 = s2.params_20.get(3).deref().get() & 0xffff;

    //LAB_80102278
    for(int i = 0; i < v0; i++) {
      final BttlScriptData6cSub98Sub94 s2_0 = s1._68.deref().get(i);
      _8011a008.setu(i);
      FUN_80101308(s6, s3, s1, s2_0, s1._08);
      s2_0._3c.set(s2_0._50);
    }

    final BttlScriptData6cSub98Sub94 s2_0 = s1._68.deref().get(v0);

    //LAB_801022b4
    if(s1._61.get() != 0) {
      s1._90.set(MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801012d4", long.class, EffectManagerData6c.class, BttlScriptData6cSub98.class, BttlScriptData6cSub98Sub94.class), QuadConsumerRef::new));
    } else {
      //LAB_801022cc
      s1._90.set(MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801012a0", long.class, EffectManagerData6c.class, BttlScriptData6cSub98.class, BttlScriptData6cSub98Sub94.class), QuadConsumerRef::new));
    }

    //LAB_801022d4
    s1._54.set(0);
    _80119b94.get(s2.params_20.get(2).deref().get() >> 20).deref().run(s1, s2_0, s1._08, s2.params_20.get(2).deref().get());
    s3._10._00.or(0x5000_0000L);
    s3._04.or(0x4_0000L);
    FUN_80102534();
    return 0;
  }

  @Method(0x80102364L)
  public static long FUN_80102364(final RunningScript a0) {
    final long a0_0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.getPointer(); //TODO

    final long a2 = a0.params_20.get(0).deref().get();
    if(a2 == 0) {
      MEMORY.ref(1, a0_0).offset(0x6cL).setu(0x1L);
      MEMORY.ref(4, a0_0).offset(0x70L).setu(a0.params_20.get(2).deref().get());
      MEMORY.ref(4, a0_0).offset(0x74L).setu(a0.params_20.get(3).deref().get());
      MEMORY.ref(4, a0_0).offset(0x78L).setu(a0.params_20.get(4).deref().get());
      MEMORY.ref(4, a0_0).offset(0x80L).setu(a0.params_20.get(5).deref().get());
      //LAB_801023d0
    } else if(a2 == 0x1L) {
      MEMORY.ref(1, a0_0).offset(0x6cL).setu(0);
      //LAB_801023e0
    } else if(a2 == 0x2L) {
      //LAB_801023e8
      MEMORY.ref(4, a0_0).offset(0x80L).setu(a0.params_20.get(5).deref().get());
    }

    //LAB_801023ec
    return 0;
  }

  @Method(0x801023f4L)
  public static long FUN_801023f4(final RunningScript a0) {
    return 0;
  }

  @Method(0x801023fcL)
  public static long FUN_801023fc(final RunningScript script) {
    final BttlScriptData6cSub98 a2 = scriptStatePtrArr_800bc1c0.get(script.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.derefAs(BttlScriptData6cSub98.class);
    final long a1 = script.params_20.get(1).getPointer();

    //LAB_8010243c
    for(int i = 0; i < a2._50.get(); i++) {
      final BttlScriptData6cSub98Sub94 a0 = a2._68.deref().get(i);
      MEMORY.ref(4, a1).offset(i * 0x4L).setu(a0._90.get() & 0x1L);
    }

    //LAB_80102464
    return 0;
  }

  @Method(0x8010246cL)
  public static long FUN_8010246c(final RunningScript a0) {
    final BttlScriptData6cSub98 effect = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.derefAs(BttlScriptData6cSub98.class);
    final BttlScriptData6cSub98Sub94 a1 = effect._68.deref().get(a0.params_20.get(1).deref().get());

    final VECTOR sp0x20 = new VECTOR();
    FUN_800cf684(a1._68, a1._2c, new VECTOR().set(a1._50), sp0x20);
    a0.params_20.get(2).deref().set(sp0x20.getX());
    a0.params_20.get(3).deref().set(sp0x20.getY());
    a0.params_20.get(4).deref().set(sp0x20.getZ());
    return 0;
  }

  @Method(0x80102534L)
  public static void FUN_80102534() {
    BttlScriptData6cSub98 s0 = _8011a00c.derefNullable();

    //LAB_80102550
    while(s0 != null) {
      final long s1 = addToLinkedListHead(s0.size_64.get());

      if(s1 != 0) {
        final long a1 = s0._68.getPointer();

        if(s1 < a1) {
          memcpy(s1, a1, (int)s0.size_64.get());
          removeFromLinkedList(s0._68.getPointer());
          s0._68.setPointer(s1);
        } else {
          //LAB_801025a4
          removeFromLinkedList(s1);
        }
      }

      //LAB_801025b0
      s0 = s0._94.derefNullable();
    }

    //LAB_801025c0
  }

  @Method(0x80102610)
  public static long FUN_80102610(final RunningScript a0) {
    return 0;
  }

  @Method(0x80102618L)
  public static void FUN_80102618(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2) {
    if(a1.scriptIndex_08.get() != -1) {
      final VECTOR sp0x10 = new VECTOR();
      final VECTOR sp0x20 = new VECTOR();
      FUN_800cea1c(a1.scriptIndex_08.get(), sp0x10);
      sp0x10.sub(a0._10.vec_04).div(a1._28.get());

      //LAB_801026f0
      for(int i = 0; i < a1._28.get(); i++) {
        final long addr = a2.ptr_10.get() + i * 0x30;
        MEMORY.ref(4, addr).offset(0x0L).setu(sp0x20.getX());
        MEMORY.ref(4, addr).offset(0x4L).setu(sp0x20.getY());
        MEMORY.ref(4, addr).offset(0x8L).setu(sp0x20.getZ());

        sp0x20.x.add((int)((seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7));
        sp0x20.z.add((int)((seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7));
        sp0x20.add(sp0x10);
      }
    }

    //LAB_80102848
  }

  @Method(0x80102860L)
  public static void FUN_80102860(final EffectManagerData6c data, final BttlScriptData6cSub38 s0) {
    int sp10;
    int sp12;
    int sp14;
    int sp16;
    int sp18;
    int sp1a;
    int sp20 = 0;
    int sp22 = 0;
    int sp24 = 0;
    int sp26 = 0;
    int sp28 = 0;
    int sp2a = 0;

    //LAB_801028a4
    for(int i = 0; i < s0.count_00.get(); i++) {
      final BttlScriptData6cSub38Sub14 s1 = s0._34.deref().get(i);

      sp16 = data._10.svec_1c.getX() << 8;
      sp18 = data._10.svec_1c.getY() << 8;
      sp1a = data._10.svec_1c.getZ() << 8;

      int a1 = data._10.svec_1c.getX();
      int a0 = data._10.svec_1c.getY();
      int v1 = data._10.svec_1c.getZ();

      if(a0 < a1) {
        a0 = a1;
      }

      //LAB_8010290c
      if(v1 < a0) {
        v1 = a0;
      }

      //LAB_8010292c
      sp10 = v1 << 8;
      sp12 = v1 << 8;
      sp14 = v1 << 8;

      if(s0._23.get() == 0) {
        sp20 = sp10 / s0._28.get();
        sp22 = sp12 / s0._28.get();
        sp24 = sp14 / s0._28.get();
        sp26 = sp16 / s0._28.get();
        sp28 = sp18 / s0._28.get();
        sp2a = sp1a / s0._28.get();
      }

      //LAB_801029f0
      long addr = s1.ptr_10.get();

      //LAB_80102a04
      for(a1 = 0; a1 < s0._28.get(); a1++) {
        MEMORY.ref(2, addr).offset(0x10L).setu(sp10);
        MEMORY.ref(2, addr).offset(0x12L).setu(sp12);
        MEMORY.ref(2, addr).offset(0x14L).setu(sp14);
        MEMORY.ref(2, addr).offset(0x16L).setu(sp16);
        MEMORY.ref(2, addr).offset(0x18L).setu(sp18);
        MEMORY.ref(2, addr).offset(0x1aL).setu(sp1a);

        if(s0._22.get() == 0 && s0._0c.get() != -1) {
          MEMORY.ref(2, addr).offset(0x1cL).setu(MEMORY.ref(2, addr).offset(0x10L).get() / s0._0c.get());
          MEMORY.ref(2, addr).offset(0x1eL).setu(MEMORY.ref(2, addr).offset(0x12L).get() / s0._0c.get());
          MEMORY.ref(2, addr).offset(0x20L).setu(MEMORY.ref(2, addr).offset(0x14L).get() / s0._0c.get());
          MEMORY.ref(2, addr).offset(0x22L).setu(MEMORY.ref(2, addr).offset(0x16L).get() / s0._0c.get());
          MEMORY.ref(2, addr).offset(0x24L).setu(MEMORY.ref(2, addr).offset(0x18L).get() / s0._0c.get());
          MEMORY.ref(2, addr).offset(0x26L).setu(MEMORY.ref(2, addr).offset(0x1aL).get() / s0._0c.get());
        } else {
          //LAB_80102b10
          MEMORY.ref(2, addr).offset(0x26L).setu(0);
          MEMORY.ref(2, addr).offset(0x24L).setu(0);
          MEMORY.ref(2, addr).offset(0x22L).setu(0);
          MEMORY.ref(2, addr).offset(0x20L).setu(0);
          MEMORY.ref(2, addr).offset(0x1eL).setu(0);
          MEMORY.ref(2, addr).offset(0x1cL).setu(0);
        }

        //LAB_80102b28
        sp10 = sp10 - sp20;
        sp12 = sp12 - sp22;
        sp14 = sp14 - sp24;
        sp16 = sp16 - sp26;
        sp18 = sp18 - sp28;
        sp1a = sp1a - sp2a;
        addr = addr + 0x30L;
      }

      //LAB_80102bb8
      FUN_80102618(data, s0, s1);
    }

    //LAB_80102be0
  }

  @Method(0x80102bfcL)
  public static void FUN_80102bfc(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2) {
    short sp14 = 0;
    short sp10 = 0;

    final long t3 = -(short)(a1._1c.get() / a1._28.get());

    //LAB_80102c58
    for(int i = 0; i < a1._28.get(); i++) {
      final long a3 = a2.ptr_10.get() + i * 0x30;
      MEMORY.ref(4, a3).offset(0x0L).setu(sp10);
      MEMORY.ref(4, a3).offset(0x4L).setu(t3 * i);
      MEMORY.ref(4, a3).offset(0x8L).setu(sp14);
      MEMORY.ref(1, a3).offset(0x28L).setu(seed_800fa754.advance().get() % 7 + 5);
      MEMORY.ref(2, a3).offset(0x2eL).setu(seed_800fa754.advance().get() % 193 + 64);
      MEMORY.ref(2, a3).offset(0x2aL).setu(0);
      MEMORY.ref(2, a3).offset(0x2cL).setu(sp10 >> 4);

      if(a1._14.get() == 0) {
        sp10 += (seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7;
        sp14 += (seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7;
        //LAB_80102e58
      } else if(i < a1._28.get() - 2) {
        sp10 = (short)((seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7);
        sp14 = (short)((seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7);
      } else {
        //LAB_80102f44
        sp10 = 0;
        sp14 = 0;
      }

      //LAB_80102f4c
    }

    //LAB_80102f64
    FUN_80102618(a0, a1, a2);
  }

  @Method(0x80102f7cL)
  public static void FUN_80102f7c(final SVECTOR a0, final SVECTOR a1, final long a2, final int a3, final int a4) {
    final long addr = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x24L);
    MEMORY.ref(1, addr).offset(0x03L).setu(0x8L);
    MEMORY.ref(1, addr).offset(0x04L).setu(0);
    MEMORY.ref(1, addr).offset(0x05L).setu(0);
    MEMORY.ref(1, addr).offset(0x06L).setu(0);
    MEMORY.ref(1, addr).offset(0x07L).setu(0x3aL);
    MEMORY.ref(1, addr).offset(0x0cL).setu(a1.getX() >>> 8);
    MEMORY.ref(1, addr).offset(0x0dL).setu(a1.getY() >>> 8);
    MEMORY.ref(1, addr).offset(0x0eL).setu(a1.getZ() >>> 8);
    MEMORY.ref(1, addr).offset(0x14L).setu(0);
    MEMORY.ref(1, addr).offset(0x15L).setu(0);
    MEMORY.ref(1, addr).offset(0x16L).setu(0);
    MEMORY.ref(1, addr).offset(0x1cL).setu(a0.getX() >>> 8);
    MEMORY.ref(1, addr).offset(0x1dL).setu(a0.getY() >>> 8);
    MEMORY.ref(1, addr).offset(0x1eL).setu(a0.getZ() >>> 8);
    MEMORY.ref(2, addr).offset(0x08L).setu(MEMORY.ref(2, a2).offset(0x00L).get());
    MEMORY.ref(2, addr).offset(0x0aL).setu(MEMORY.ref(2, a2).offset(0x04L).get());
    MEMORY.ref(2, addr).offset(0x10L).setu(MEMORY.ref(2, a2).offset(0x08L).get());
    MEMORY.ref(2, addr).offset(0x12L).setu(MEMORY.ref(2, a2).offset(0x0cL).get());
    MEMORY.ref(2, addr).offset(0x18L).setu(MEMORY.ref(2, a2).offset(0x10L).get());
    MEMORY.ref(2, addr).offset(0x1aL).setu(MEMORY.ref(2, a2).offset(0x14L).get());
    MEMORY.ref(2, addr).offset(0x20L).setu(MEMORY.ref(2, a2).offset(0x18L).get());
    MEMORY.ref(2, addr).offset(0x22L).setu(MEMORY.ref(2, a2).offset(0x1cL).get());
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (a3 + a4) / 4 * 4, addr);
  }

  @Method(0x801030d8L)
  public static void FUN_801030d8(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long v0;
    long v1;
    long a0;
    long a1;
    int s0;
    long s4;
    long s7;
    int sp54;
    int spac;
    int sp50;
    int sp48;
    int sp3c;
    int spb0;
    int spa8;
    int spa4;
    int sp44;
    int sp40;
    int sp38;
    int spa0;
    int sp6c;
    int sp34;
    int sp30;
    int sp1c;
    int sp5c;
    int sp60;
    int sp68;
    int sp64;
    int sp18;
    int sp58;
    int sp4c;
    int spc0 = 1;
    final BttlScriptData6cSub38 s6 = data._44.derefAs(BttlScriptData6cSub38.class);

    if(s6._04.get() + 1 == s6._0c.get()) {
      return;
    }

    final ShortRef sp0x20 = new ShortRef();
    final ShortRef sp0x24 = new ShortRef();
    final ShortRef sp0x28 = new ShortRef();
    final ShortRef sp0x2c = new ShortRef();
    final VECTOR sp0x70 = new VECTOR();

    s6._04.incr();

    //LAB_80103140
    if(s6._04.get() == 1) {
      FUN_80102860(data, s6);

      //LAB_80103174
      for(int i = 0; i < s6.count_00.get(); i++) {
        s6.callback_2c.deref().run(data, s6, s6._34.deref().get(i), i);
      }

      return;
    }

    //LAB_801031cc
    s6._2a.incr().and(31);
    if((data._10._24.get() >> s6._2a.get() & 0x1L) == 0) {
      spc0 = 0;
    }

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x20);
    final Value sp0x80 = tmp.get();

    //LAB_80103200
    //LAB_8010322c
    for(int i = 0; i < s6.count_00.get(); i++) {
      final BttlScriptData6cSub38Sub14 spbc = s6._34.deref().get(i);

      if(s6._24.get() == 0) {
        FUN_80102bfc(data, s6, spbc);
      }

      //LAB_8010324c
      s6.callback_2c.deref().run(data, s6, spbc, i);
      spbc._02.sub((short)(s6._10.get() << 7 >> 8));
      s4 = spbc.ptr_10.get();
      sp0x70.set((int)MEMORY.ref(4, s4).offset(0x0L).get(), (int)MEMORY.ref(4, s4).offset(0x4L).get(), (int)MEMORY.ref(4, s4).offset(0x8L).get());
      sp18 = FUN_800cfb94(data, spbc._04, sp0x70, sp0x20, sp0x24) >> 2;
      s4 = s4 + (s6._28.get() - 1) * 0x30L;
      sp0x70.set((int)MEMORY.ref(4, s4).offset(0x0L).get(), (int)MEMORY.ref(4, s4).offset(0x4L).get(), (int)MEMORY.ref(4, s4).offset(0x8L).get());
      sp1c = FUN_800cfb94(data, spbc._04, sp0x70, sp0x28, sp0x2c) >> 2;
      sp40 = sp0x28.get() - sp0x20.get() << 8;
      sp44 = sp0x2c.get() - sp0x24.get() << 8;
      sp48 = sp40 / (s6._28.get() - 1);
      sp4c = sp44 / (s6._28.get() - 1);
      sp60 = sp0x20.get() << 8;
      sp50 = sp0x20.get() << 8;
      sp64 = sp0x24.get() << 8;
      sp54 = sp0x24.get() << 8;
      v0 = spbc.ptr_10.get();
      final short s0_0 = (short)(MEMORY.ref(1, v0).offset(0x28L).getSigned() * data._10.svec_16.getX() >> 12);
      s7 = -ratan2(sp40, sp44);
      final short s1_0 = (short)(rcos(s7) * s0_0 >> 12);
      final short v0_0 = (short)(rsin(s7) * s0_0 >> 12);
      sp0x28.set((short)(sp0x20.get() - s1_0));
      sp0x2c.set((short)(sp0x24.get() - v0_0));
      sp0x20.add(s1_0);
      sp0x24.add(v0_0);

      //LAB_80103488
      for(int fp = 0; fp < s6._28.get(); fp++) {
        s4 = spbc.ptr_10.get() + fp * 0x30;
        MEMORY.ref(2, s4).offset(0x10L).subu(MEMORY.ref(2, s4).offset(0x1cL).get());
        MEMORY.ref(2, s4).offset(0x12L).subu(MEMORY.ref(2, s4).offset(0x1eL).get());
        MEMORY.ref(2, s4).offset(0x14L).subu(MEMORY.ref(2, s4).offset(0x20L).get());
        MEMORY.ref(2, s4).offset(0x16L).subu(MEMORY.ref(2, s4).offset(0x22L).get());
        MEMORY.ref(2, s4).offset(0x18L).subu(MEMORY.ref(2, s4).offset(0x24L).get());
        MEMORY.ref(2, s4).offset(0x1aL).subu(MEMORY.ref(2, s4).offset(0x26L).get());
        MEMORY.ref(2, s4).offset(0x2aL).addu(s6._10.get() << 8 >> 8);
      }

      //LAB_80103538
      if(spc0 == 0) {
        v1 = data._10._22.get() + sp18;
        if(v1 >= 0xa0) {
          if(v1 >= 0xffe) {
            sp18 = 0xffe - data._10._22.get();
          }

          //LAB_80103574
          s4 = spbc.ptr_10.get();

          //LAB_80103594
          for(int fp = 0; fp < s6._28.get() - 1; fp++) {
            sp58 = sp50 + sp48;
            sp5c = sp54 + sp4c;
            sp68 = sp50 + sp48;
            sp6c = sp54 + sp4c;
            sp50 += rcos(s7) * MEMORY.ref(2, s4).offset(0x2cL).getSigned() >> 12 << 8;
            sp54 += rsin(s7) * MEMORY.ref(2, s4).offset(0x2cL).getSigned() >> 12 << 8;
            sp58 += rcos(s7) * MEMORY.ref(2, s4).offset(0x5cL).getSigned() >> 12 << 8;
            sp5c += rsin(s7) * MEMORY.ref(2, s4).offset(0x5cL).getSigned() >> 12 << 8;
            s0 = (int)(MEMORY.ref(1, s4).offset(0x28L).getSigned() * data._10.svec_16.getX() >> 12);
            sp30 = (sp58 >> 8) + (rcos(s7) * s0 >> 12);
            sp34 = (sp5c >> 8) + (rsin(s7) * s0 >> 12);
            sp38 = (sp58 >> 8) - (rcos(s7) * s0 >> 12);
            sp3c = (sp5c >> 8) - (rsin(s7) * s0 >> 12);

            if(s6._29.get() == 0) {
              spa0 = (sp60 - sp50 >> 8) * (int)MEMORY.ref(2, s4).offset(0x2eL).getSigned() + sp50;
              spa4 = (sp64 - sp54 >> 8) * (int)MEMORY.ref(2, s4).offset(0x2eL).getSigned() + sp54;
              spa8 = (sp58 - sp50 >> 8) * (int)MEMORY.ref(2, s4).offset(0x2eL).getSigned() + sp50;
              spac = (sp5c - sp54 >> 8) * (int)MEMORY.ref(2, s4).offset(0x2eL).getSigned() + sp54;

              //LAB_80103808
              spb0 = (int)(MEMORY.ref(2, s4).offset(0x10L).get() + Math.abs(MEMORY.ref(2, s4).offset(0x2cL).getSigned() - MEMORY.ref(2, s4).offset(0x5cL).getSigned()) * 8);
              if((spb0 & 0xffff) > 0xff00) {
                spb0 = 0xff00;
              }

              //LAB_80103834
              a1 = linkedListAddress_1f8003d8.get();
              linkedListAddress_1f8003d8.addu(0x1cL);
              MEMORY.ref(1, a1).offset(0x03L).setu(0x6L);
              MEMORY.ref(4, a1).offset(0x04L).setu(0x3080_8080L);
              MEMORY.ref(1, a1).offset(0x04L).setu(spb0 >>> 9);
              MEMORY.ref(1, a1).offset(0x05L).setu(spb0 >>> 9);
              MEMORY.ref(1, a1).offset(0x06L).setu(spb0 >>> 9);
              MEMORY.ref(1, a1).offset(0x07L).setu(0x32);
              MEMORY.ref(2, a1).offset(0x08L).setu(spa0 >> 8);
              MEMORY.ref(2, a1).offset(0x0aL).setu(spa4 >> 8);
              MEMORY.ref(1, a1).offset(0x0cL).setu(spb0 >>> 8);
              MEMORY.ref(1, a1).offset(0x0dL).setu(spb0 >>> 8);
              MEMORY.ref(1, a1).offset(0x0eL).setu(spb0 >>> 8);
              MEMORY.ref(2, a1).offset(0x10L).setu(sp50 >> 8);
              MEMORY.ref(2, a1).offset(0x12L).setu(sp54 >> 8);
              MEMORY.ref(1, a1).offset(0x14L).setu(spb0 >>> 9);
              MEMORY.ref(1, a1).offset(0x15L).setu(spb0 >>> 9);
              MEMORY.ref(1, a1).offset(0x16L).setu(spb0 >>> 9);
              MEMORY.ref(2, a1).offset(0x18L).setu(spa8 >> 8);
              MEMORY.ref(2, a1).offset(0x1aL).setu(spac >> 8);
              insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (data._10._22.get() + sp18) / 4 * 4, a1);
            }

            //LAB_80103994
            sp0x80.offset(0x08L).setu(sp58 >> 8);
            sp0x80.offset(0x0cL).setu(sp5c >> 8);
            sp0x80.offset(0x18L).setu(sp50 >> 8);
            sp0x80.offset(0x1cL).setu(sp54 >> 8);

            sp0x80.offset(0x00L).setu(sp30);
            sp0x80.offset(0x04L).setu(sp34);
            sp0x80.offset(0x10L).setu(sp0x20.get());
            sp0x80.offset(0x14L).setu(sp0x24.get());
            FUN_80102f7c(MEMORY.ref(2, s4 + 0x16L, SVECTOR::new), MEMORY.ref(2, s4 + 0x46L, SVECTOR::new), sp0x80.getAddress(), sp18, data._10._22.get());

            sp0x80.offset(0x00L).setu((sp0x80.offset(0x00L).getSigned() - sp0x80.offset(0x08L).getSigned()) / data._10.vec_28.getZ() + sp0x80.offset(0x08L).getSigned());
            sp0x80.offset(0x04L).setu((sp0x80.offset(0x04L).getSigned() - sp0x80.offset(0x0cL).getSigned()) / data._10.vec_28.getZ() + sp0x80.offset(0x0cL).getSigned());
            sp0x80.offset(0x10L).setu((sp0x80.offset(0x10L).getSigned() - sp0x80.offset(0x18L).getSigned()) / data._10.vec_28.getZ() + sp0x80.offset(0x18L).getSigned());
            sp0x80.offset(0x14L).setu((sp0x80.offset(0x14L).getSigned() - sp0x80.offset(0x1cL).getSigned()) / data._10.vec_28.getZ() + sp0x80.offset(0x1cL).getSigned());
            FUN_80102f7c(MEMORY.ref(2, s4 + 0x10L, SVECTOR::new), MEMORY.ref(2, s4 + 0x40L, SVECTOR::new), sp0x80.getAddress(), sp18, data._10._22.get());

            sp0x80.offset(0x00L).setu(sp38);
            sp0x80.offset(0x04L).setu(sp3c);
            sp0x80.offset(0x10L).setu(sp0x28.get());
            sp0x80.offset(0x14L).setu(sp0x2c.get());
            FUN_80102f7c(MEMORY.ref(2, s4 + 0x16L, SVECTOR::new), MEMORY.ref(2, s4 + 0x46L, SVECTOR::new), sp0x80.getAddress(), sp18, data._10._22.get());

            sp0x80.offset(0x00L).setu((sp0x80.offset(0x00L).getSigned() - sp0x80.offset(0x08L).getSigned()) / data._10.vec_28.getZ() + sp0x80.offset(0x08L).getSigned());
            sp0x80.offset(0x04L).setu((sp0x80.offset(0x04L).getSigned() - sp0x80.offset(0x0cL).getSigned()) / data._10.vec_28.getZ() + sp0x80.offset(0x0cL).getSigned());
            sp0x80.offset(0x10L).setu((sp0x80.offset(0x10L).getSigned() - sp0x80.offset(0x18L).getSigned()) / data._10.vec_28.getZ() + sp0x80.offset(0x18L).getSigned());
            sp0x80.offset(0x14L).setu((sp0x80.offset(0x14L).getSigned() - sp0x80.offset(0x1cL).getSigned()) / data._10.vec_28.getZ() + sp0x80.offset(0x1cL).getSigned());
            FUN_80102f7c(MEMORY.ref(2, s4 + 0x10L, SVECTOR::new), MEMORY.ref(2, s4 + 0x40L, SVECTOR::new), sp0x80.getAddress(), sp18, data._10._22.get());

            sp0x20.set((short)sp30);
            sp0x24.set((short)sp34);
            sp0x28.set((short)sp38);
            sp0x2c.set((short)sp3c);
            sp60 = sp50;
            sp64 = sp54;
            sp50 = sp68;
            sp54 = sp6c;
            s4 = s4 + 0x30L;
          }

          //LAB_80103ca0
          SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(1, data._10._00.get() >>> 28 & 3, 0, 0), null);
          a1 = data._10._22.get();
          a0 = sp18;
          v1 = a1 + a0;
          if(v1 >= 0xa0) {
            if(v1 >= 0xffe) {
              a1 = 0xffe - a0;
            }

            //LAB_80103d24
            insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (a1 + a0) / 4 * 4, linkedListAddress_1f8003d8.get());
          }

          linkedListAddress_1f8003d8.addu(0xcL);
        }
      }

      //LAB_80103d48
    }

    tmp.release();

    //LAB_80103d80
  }

  @Method(0x80104954L)
  public static void FUN_80104954(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub38 s2 = data._44.derefAs(BttlScriptData6cSub38.class);

    //LAB_80104984
    for(int i = 0; i < s2.count_00.get(); i++) {
      removeFromLinkedList(s2._34.deref().get(i).ptr_10.get());
    }

    //LAB_801049ac
    removeFromLinkedList(s2._34.getPointer());
  }

  @Method(0x801049d4L)
  public static void FUN_801049d4(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    // no-op
  }

  @Method(0x801049dcL)
  public static void FUN_801049dc(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    a2._04.setY((short)(0x1000 / a1.count_00.get() * a3));
    a2._04.setZ((short)(a1._1e.get() * 2));
  }

  @Method(0x80104a14L)
  public static void FUN_80104a14(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    a2._04.setX((short)(seed_800fa754.advance().get() % 4097));
    a2._04.setY((short)(seed_800fa754.advance().get() % 4097));
    a2._04.setZ((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x801052d4L)
  public static void FUN_801052d4(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    // no-op
  }

  @Method(0x801052dcL)
  public static long FUN_801052dc(final RunningScript fp) {
    final int s0 = fp.params_20.get(6).deref().get();
    final int s1 = fp.params_20.get(7).deref().get();
    final int scriptIndex = allocateEffectManager(fp.scriptStateIndex_00.get(), 0x38L, null, _80119f14.get(s1).deref(), MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80104954", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), BttlScriptData6cSub38::new);
    final EffectManagerData6c s7 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub38 s6 = s7._44.derefAs(BttlScriptData6cSub38.class);
    s6.count_00.set(fp.params_20.get(3).deref().get());
    s6._04.set(0);
    s6.scriptIndex_08.set(fp.params_20.get(1).deref().get());
    s6._0c.set(s0 >> 16 & 0xff);
    s6._10.set(fp.params_20.get(5).deref().get());
    s6._14.set(s0 >>> 24 & 0x8);
    s6._18.set(s0 >>> 24 & 0x10);
    s6._1c.set((short)fp.params_20.get(2).deref().get());
    s6._1e.set((short)fp.params_20.get(4).deref().get());
    s6._20.set((short)s1);
    s6._22.set(s0 >>> 24 & 0x1);
    s6._23.set(s0 >>> 24 & 0x2);
    s6._24.set(s0 >>> 24 & 0x4);
    s6._26.set(s0 & 0xff);
    s6._28.set(s0 >> 8 & 0xff);
    s6._29.set(s0 >>> 24 & 0x20);
    s6._2a.set(0);
    s6.callback_2c.set(_80119ee8.get(s1).deref());
    s6._34.setPointer(addToLinkedListTail(s6.count_00.get() * 0x14L));

    if(s6._0c.get() == 0) {
      s6._0c.set(-1);
    }

    //LAB_8010549c
    //LAB_801054b4
    for(int i = 0; i < s6.count_00.get(); i++) {
      final BttlScriptData6cSub38Sub14 struct = s6._34.deref().get(i);
      struct._00.set(1);
      struct._02.set((short)(seed_800fa754.advance().get() % 4097));
      struct._04.set((short)0, (short)0, (short)0);
      struct.ptr_10.set(addToLinkedListTail(s6._28.get() * 0x30L));
      _80119ebc.get(s6._20.get()).deref().run(s7, s6, struct, i);
      FUN_80102bfc(s7, s6, struct);
    }

    //LAB_80105590
    fp.params_20.get(0).deref().set(scriptIndex);
    s7._10._00.or(0x5000_0000L);
    s7._10.svec_1c.set((short)0, (short)0, (short)0xff);
    s7._10.vec_28.setX(0x100);
    s7._10.vec_28.setZ(0x3);
    return 0;
  }

  @Method(0x80105604L)
  public static long FUN_80105604(final RunningScript s0) {
    final EffectManagerData6c a0 = scriptStatePtrArr_800bc1c0.get(s0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub38Sub14 a1 = a0._44.derefAs(BttlScriptData6cSub38.class)._34.deref().get(s0.params_20.get(1).deref().get());
    final long v0 = a1.ptr_10.get() + s0.params_20.get(2).deref().get() * 0x30L;

    final VECTOR sp0x10 = new VECTOR().set((VECTOR)MEMORY.ref(4, v0).cast(VECTOR::new));
    final VECTOR sp0x20 = new VECTOR();
    FUN_800cf4f4(a0, a1._04, sp0x10, sp0x20);
    s0.params_20.get(3).deref().set(sp0x20.getX());
    s0.params_20.get(4).deref().set(sp0x20.getY());
    s0.params_20.get(5).deref().set(sp0x20.getZ());
    return 0;
  }

  @Method(0x80105f98L)
  public static void FUN_80105f98(final int scriptIndex, final VECTOR a1, final long a2) {
    final MATRIX sp0x10 = new MATRIX();
    final VECTOR sp0x30 = new VECTOR();

    final BattleObject27c v0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);

    final GsCOORDINATE2 coord2;
    if(a2 == 0) {
      coord2 = v0._148.coord2ArrPtr_04.deref().get(1);
    } else {
      //LAB_80105fe4
      coord2 = v0._148.coord2_14;
    }

    //LAB_80105fec
    GsGetLw(coord2, sp0x10);
    a1.set(ApplyMatrixLV(sp0x10, sp0x30)).add(sp0x10.transfer);
  }

  @Method(0x80106050L)
  public static void FUN_80106050(final long a0, final long a1) {
    final long callback = getMethodAddress(Bttl_800d.class, "FUN_800d46d4", RunningScript.class);

    if(Math.abs((byte)a0) >= 0x2L) {
      callScriptFunction(callback, 0x24, 0x77, 0x2b, 0x1, 0x80);
      callScriptFunction(callback, (int)_800fb7bc.offset(1, 0x0L).offset(a1).get(), 0x73, 0x30, 0x1, 0x80);
    } else {
      //LAB_80106114
      callScriptFunction(callback, 0x24, 0x77, 0x33, 0x1, 0x80);
      callScriptFunction(callback, (int)_800fb7bc.offset(1, 0x2L).offset(a1).get(), 0x73, 0x30, 0x1, 0x80);
      callScriptFunction(callback, 0x25, 0x73, 0x32, 0x1, 0x80);
    }
  }

  @Method(0x801061bcL)
  public static long FUN_801061bc(final int charSlot, final long a1, final long a2, final long a3) {
    //LAB_80106264
    final long v0;
    if(a3 == 0x1L || a3 == 0x3L) {
      //LAB_80106274
      v0 = _800fb7c0.offset(a1 * 0x10L).offset(1, a2).get();
    } else {
      //LAB_8010628c
      v0 = FUN_800c7488(charSlot, a1, a2) & 0xffL;
    }

    //LAB_80106298
    return v0;
  }

  @Method(0x801062a8L)
  public static void FUN_801062a8(final int scriptIndex, final int a1, final BttlScriptData6cSub44 a2, final long a3) {
    long v0;
    long s3;
    long s6;
    long s7;
    final BattleObject27c s5 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);

    //LAB_8010633c
    for(s3 = 0; s3 < 8; s3++) {
      if((FUN_801061bc(s5.charSlot_276.get(), s3, 0x1L, a3) & 0xffL) == 0) {
        break;
      }
    }

    //LAB_80106374
    v0 = s3 - 0x1L;
    a2._30.set((int)v0);
    a2.scriptIndex_00.set(scriptIndex);
    a2.scriptIndex_04.set(a1);
    a2._34.set((short)0);
    a2._31.set(0);
    a2._32.set(0);
    a2._38.set(0);
    a2._39.set(0);
    a2._3a.set((int)a3);
    a2._40.set(addToLinkedListTail((v0 & 0xffL) * 0x20L));
    s6 = FUN_801061bc(s5.charSlot_276.get(), 0, 0xfL, a3) & 0xffL;
    a2._36.set((int)s6);

    s7 = a2._40.get();

    //LAB_801063f0
    for(s3 = 0; s3 < a2._30.get(); s3++) {
      MEMORY.ref(1, s7).offset(0x0L).setu(0x1L);
      MEMORY.ref(1, s7).offset(0x1L).setu(0);
      MEMORY.ref(2, s7).offset(0x8L).setu(0);
      MEMORY.ref(2, s7).offset(0x10L).setu(s6 + 0x2L);
      seed_800fa754.advance();
      MEMORY.ref(1, s7).offset(0x2L).setu(0x3L);
      MEMORY.ref(1, s7).offset(0x1cL).setu(0);
      _8011a014.offset(s3).offset(1, 0x0L).setu(0);
      v0 = FUN_801061bc(s5.charSlot_276.get(), s3, 0x1L, a3) & 0xffL;
      s6 = s6 + v0;
      MEMORY.ref(2, s7).offset(0xaL).setu(v0);
      v0 = FUN_801061bc(s5.charSlot_276.get(), s3, 0x2L, a3) & 0xffL;
      MEMORY.ref(2, s7).offset(0xcL).setu(v0);
      v0 = FUN_801061bc(s5.charSlot_276.get(), s3, 0x3L, a3) & 0xffL;
      MEMORY.ref(2, s7).offset(0xeL).setu(v0);
      long a3_0 = MEMORY.ref(2, s7).offset(0x10L).get() + MEMORY.ref(2, s7).offset(0xcL).get();
      MEMORY.ref(2, s7).offset(0x10L).setu(a3_0 - (short)v0 / 2 + 1);
      MEMORY.ref(2, s7).offset(0x12L).setu(a3_0 + v0 - (short)MEMORY.ref(2, s7).offset(0xeL).get() / 2);

      a3_0 = addToLinkedListTail(0xeeL);
      MEMORY.ref(4, s7).offset(0x18L).setu(a3_0);

      //LAB_8010652c
      for(int i = 16; i >= 0; i--) {
        MEMORY.ref(2, a3_0).offset(0x8L).setu((18 - i) * 10);
        MEMORY.ref(1, a3_0).offset(0x0L).setu(0x1L);
        //LAB_8010656c
        //LAB_80106574
        MEMORY.ref(2, a3_0).offset(0x2L).setu((16 - i) * 0x80 + 0x200L);
        MEMORY.ref(1, a3_0).offset(0xcL).setu(0x5L);
        MEMORY.ref(1, a3_0).offset(0xdL).setu(0);
        MEMORY.ref(2, a3_0).offset(0xaL).setu(MEMORY.ref(2, s7).offset(0x10L).get() + (MEMORY.ref(2, s7).offset(0xeL).getSigned() - 0x1L) / 2 + i - 0x11L);
        MEMORY.ref(1, a3_0).offset(0x4L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x0L).get());
        MEMORY.ref(1, a3_0).offset(0x5L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x1L).get());
        MEMORY.ref(1, a3_0).offset(0x6L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x2L).get());
        a3_0 = a3_0 + 0xeL;
      }

      a3_0 = a3_0 - 0xeL;

      //LAB_80106634
      for(int i = 0; i < 3; i++) {
        MEMORY.ref(2, a3_0).offset(0x8L).setu(0x14L - i * 0x2L);
        MEMORY.ref(1, a3_0).offset(0x0L).setu(0x1L);
        MEMORY.ref(2, a3_0).offset(0x2L).setu(0x200L);
        MEMORY.ref(1, a3_0).offset(0xcL).setu(0x11L);
        MEMORY.ref(1, a3_0).offset(0xdL).setu(0x1L);
        MEMORY.ref(2, a3_0).offset(0xaL).setu(MEMORY.ref(2, s7).offset(0x10L).get() - 0x11L);

        if(i != 0x1L) {
          MEMORY.ref(1, a3_0).offset(0x4L).setu(0x30L);
          MEMORY.ref(1, a3_0).offset(0x5L).setu(0x30L);
          MEMORY.ref(1, a3_0).offset(0x6L).setu(0x30L);
        } else {
          //LAB_80106680
          MEMORY.ref(1, a3_0).offset(0xdL).setu(0xffL);
        }

        //LAB_80106684
        a3_0 = a3_0 - 0xeL;
      }

      MEMORY.ref(4, s7).offset(0x14L).setu(a3_0);
      s7 = s7 + 0x20L;
    }

    //LAB_801066c8
    FUN_80105f98(a2.scriptIndex_00.get(), a2.vec_10, 0);

    final VECTOR sp0x10 = new VECTOR();
    FUN_80105f98(a2.scriptIndex_04.get(), sp0x10, 0x1L);

    final int a0_0 = (int)MEMORY.ref(4, a2._40.get()).offset(0x10L).getSigned();
    a2.vec_20.setX(sp0x10.getX() - a2.vec_10.getX() / a0_0);
    a2.vec_20.setY(sp0x10.getY() - a2.vec_10.getY() / a0_0);
    a2.vec_20.setZ(sp0x10.getZ() - a2.vec_10.getZ() / a0_0);
  }

  @Method(0x80106774L)
  public static long FUN_80106774(final long a0, final long a1) {
    long numberOfNegativeComponents = 0;
    long v0 = MEMORY.ref(1, a0).offset(0x4L).get() - a1;
    final long sp10;
    if((int)v0 > 0) {
      sp10 = v0;
    } else {
      sp10 = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067b0
    v0 = MEMORY.ref(1, a0).offset(0x5L).get() - a1;
    final long sp14;
    if((int)v0 > 0) {
      sp14 = v0;
    } else {
      sp14 = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067c4
    v0 = MEMORY.ref(1, a0).offset(0x6L).get() - a1;
    final long sp18;
    if((int)v0 > 0) {
      sp18 = v0;
    } else {
      sp18 = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067d8
    MEMORY.ref(1, a0).offset(0x4L).setu(sp10);
    MEMORY.ref(1, a0).offset(0x5L).setu(sp14);
    MEMORY.ref(1, a0).offset(0x6L).setu(sp18);
    return numberOfNegativeComponents;
  }

  @Method(0x80106808L)
  public static void FUN_80106808(final BttlScriptData6cSubBase1 a0, final long a1, final long a2, final ScriptState<EffectManagerData6c> a3, final EffectManagerData6c a4) {
    if((int)a4._10._00.get() >= 0) {
      final long v0 = MEMORY.ref(4, a1).offset(0x14L).get();
      final long s4 = v0 + 0x1cL;

      //LAB_8010685c
      for(int s5 = 0; s5 < 2; s5++) {
        final long s3 = MEMORY.ref(2, s4).offset(0x8L).getSigned() - s5 * 0x8L;

        //LAB_80106874
        final long[] sp0x18 = new long[8];
        for(int i = 0; i < 4; i++) {
          sp0x18[i    ] =  rcos(MEMORY.ref(2, s4).offset(0x2L).getSigned() + i * 0x400L) * s3 >> 12;
          sp0x18[i + 1] = (rsin(MEMORY.ref(2, s4).offset(0x2L).getSigned() + i * 0x400L) * s3 >> 12) + 30L;
        }

        final long addr = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x18L);
        MEMORY.ref(1, addr).offset(0x3L).setu(0x5L);
        MEMORY.ref(4, addr).offset(0x4L).setu(0x2a80_8080L);

        if(a2 == 0x1L) {
          MEMORY.ref(1, addr).offset(0x4L).setu(0xffL);
          MEMORY.ref(1, addr).offset(0x5L).setu(0xffL);
          MEMORY.ref(1, addr).offset(0x6L).setu(0xffL);
          //LAB_80106918
        } else if(a2 != -0x2L) {
          //LAB_80106988
          MEMORY.ref(1, addr).offset(0x4L).setu(0x30L);
          MEMORY.ref(1, addr).offset(0x5L).setu(0x30L);
          MEMORY.ref(1, addr).offset(0x6L).setu(0x30L);
        } else if(MEMORY.ref(1, a1).offset(0x1cL).getSigned() != 0) {
          MEMORY.ref(1, addr).offset(0x4L).setu(MEMORY.ref(1, s4).offset(-0xaL).get() * 3);
          MEMORY.ref(1, addr).offset(0x5L).setu(MEMORY.ref(1, s4).offset(-0x9L).get());
          MEMORY.ref(1, addr).offset(0x6L).setu((MEMORY.ref(1, s4).offset(-0x8L).get() - 0x1L) * 0x8L);
        } else {
          //LAB_80106964
          MEMORY.ref(1, addr).offset(0x4L).setu(MEMORY.ref(1, s4).offset(-0xaL).get());
          MEMORY.ref(1, addr).offset(0x5L).setu(MEMORY.ref(1, s4).offset(-0x9L).get());
          MEMORY.ref(1, addr).offset(0x6L).setu(MEMORY.ref(1, s4).offset(-0x8L).get());
        }

        //LAB_80106994
        MEMORY.ref(2, addr).offset(0x8L).setu(sp0x18[0]);
        MEMORY.ref(2, addr).offset(0xaL).setu(sp0x18[1]);
        MEMORY.ref(2, addr).offset(0xcL).setu(sp0x18[2]);
        MEMORY.ref(2, addr).offset(0xeL).setu(sp0x18[3]);
        MEMORY.ref(2, addr).offset(0x10L).setu(sp0x18[6]);
        MEMORY.ref(2, addr).offset(0x12L).setu(sp0x18[7]);
        MEMORY.ref(2, addr).offset(0x14L).setu(sp0x18[4]);
        MEMORY.ref(2, addr).offset(0x16L).setu(sp0x18[5]);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, addr);
      }

      SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0xcL);
    }

    //LAB_80106a4c
  }

  @Method(0x80106ac4L)
  public static void FUN_80106ac4(final long a0, final long a1, final long a2) {
    final long s2 = a1 + 0x400L;
    long s0 = a2 - 0x1L;
    final long sp10 = rcos(a1) * s0 >> 12;
    final long sp14 = rsin(a1) * s0 >> 12;
    final long sp18 = rcos(s2) * s0 >> 12;
    final long sp1c = rsin(s2) * s0 >> 12;
    s0 = a2 - 0xbL;
    final long sp20 = rcos(a1) * s0 >> 12;
    final long sp24 = rsin(a1) * s0 >> 12;
    final long sp28 = rcos(s2) * s0 >> 12;
    final long sp2c = rsin(s2) * s0 >> 12;
    final long colour = MEMORY.ref(2, a0).offset(0x8L).getSigned() * 0x4L;

    final long addr = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x24L);
    MEMORY.ref(1, addr).offset(0x3L).setu(0x8L);
    MEMORY.ref(4, addr).offset(0x4L).setu(0x3a80_8080L);
    MEMORY.ref(1, addr).offset(0x14L).setu(0);
    MEMORY.ref(1, addr).offset(0x15L).setu(0);
    MEMORY.ref(1, addr).offset(0x16L).setu(0);
    MEMORY.ref(1, addr).offset(0x1cL).setu(0);
    MEMORY.ref(1, addr).offset(0x1dL).setu(0);
    MEMORY.ref(1, addr).offset(0x1eL).setu(0);
    MEMORY.ref(1, addr).offset(0x4L).setu(colour);
    MEMORY.ref(1, addr).offset(0x5L).setu(colour);
    MEMORY.ref(1, addr).offset(0x6L).setu(colour);
    MEMORY.ref(1, addr).offset(0xcL).setu(colour);
    MEMORY.ref(1, addr).offset(0xdL).setu(colour);
    MEMORY.ref(1, addr).offset(0xeL).setu(colour);
    MEMORY.ref(2, addr).offset(0x8L).setu(sp10);
    MEMORY.ref(2, addr).offset(0xaL).setu(sp14 + 0x1eL);
    MEMORY.ref(2, addr).offset(0x10L).setu(sp18);
    MEMORY.ref(2, addr).offset(0x12L).setu(sp1c + 0x1eL);
    MEMORY.ref(2, addr).offset(0x18L).setu(sp20);
    MEMORY.ref(2, addr).offset(0x1aL).setu(sp24 + 0x1eL);
    MEMORY.ref(2, addr).offset(0x20L).setu(sp28);
    MEMORY.ref(2, addr).offset(0x22L).setu(sp2c + 0x1eL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, addr);
  }

  @Method(0x80106cccL)
  public static void FUN_80106ccc(final long a0, final long a1, final BttlScriptData6cSubBase1 a2, final long a3, final ScriptState<EffectManagerData6c> a4) {
    long s7 = MEMORY.ref(4, a3).offset(0x18L).get();
    final long sp28 = _8011a014.offset(a1).getAddress();
    long s3 = s7 + 0x2L;

    //LAB_80106d18
    for(long s6 = 0; s6 < 17; s6++) {
      if(MEMORY.ref(1, s7).offset(0x0L).getSigned() != 0) {
        if(MEMORY.ref(2, s3).offset(0x8L).getSigned() <= 0) {
          final long s2 = MEMORY.ref(2, s3).offset(0x6L).getSigned();
          long sp20 = rcos(MEMORY.ref(2, s3).offset(0x0L).getSigned()) * s2 >> 12;
          long sp24 = (rsin(MEMORY.ref(2, s3).offset(0x0L).getSigned()) * s2 >> 12) + 0x1eL;

          //LAB_80106d80
          long s4 = 0;
          for(long s5 = 0; s5 < 4; s5++) {
            final long s1 = linkedListAddress_1f8003d8.get();
            linkedListAddress_1f8003d8.addu(0x10L);
            MEMORY.ref(1, s1).offset(0x3L).setu(0x3L);
            MEMORY.ref(4, s1).offset(0x4L).setu(0x4080_8080L);
            final long v1 = MEMORY.ref(1, s3).offset(0xbL).get();

            //LAB_80106dc0
            final long v0;
            if(v1 != 0 && v1 != 0xffL || MEMORY.ref(1, sp28).offset(0x0L).getSigned() < 0) {
              //LAB_80106de8
              v0 = MEMORY.ref(1, s1).offset(0x7L).get() | 0x2L;
            } else {
              v0 = MEMORY.ref(1, s1).offset(0x7L).get() & 0xfdL;
            }

            //LAB_80106df4
            MEMORY.ref(4, s1).offset(0x4L).setu(v0 << 24 | 0x80_8080L);

            if(MEMORY.ref(1, a3).offset(0x1cL).getSigned() != 0 && s6 != 0x10L) {
              MEMORY.ref(1, s1).offset(0x4L).setu(MEMORY.ref(1, s3).offset(0x2L).get() * 3);
              MEMORY.ref(1, s1).offset(0x5L).setu(MEMORY.ref(1, s3).offset(0x3L).get());
              MEMORY.ref(1, s1).offset(0x6L).setu((MEMORY.ref(1, s3).offset(0x4L).get() + 1) / 8);
            } else {
              //LAB_80106e58
              MEMORY.ref(1, s1).offset(0x4L).setu(MEMORY.ref(1, s3).offset(0x2L).get());
              MEMORY.ref(1, s1).offset(0x5L).setu(MEMORY.ref(1, s3).offset(0x3L).get());
              MEMORY.ref(1, s1).offset(0x6L).setu(MEMORY.ref(1, s3).offset(0x4L).get());
            }

            //LAB_80106e74
            s4 = s4 + 0x400L;
            final long sp18 = rcos(MEMORY.ref(2, s3).offset(0x0L).getSigned() + s4) * s2 >> 12;
            final long sp1c = (rsin(MEMORY.ref(2, s3).offset(0x0L).getSigned() + s4) * s2 >> 12) + 0x1eL;
            MEMORY.ref(2, s1).offset(0x8L).setu(sp20);
            MEMORY.ref(2, s1).offset(0xaL).setu(sp24);
            MEMORY.ref(2, s1).offset(0xcL).setu(sp18);
            MEMORY.ref(2, s1).offset(0xeL).setu(sp1c);
            insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, s1);
            sp20 = sp18;
            sp24 = sp1c;
          }

          if(MEMORY.ref(1, s3).offset(0xbL).get() == 0) {
            FUN_80106ac4(a3, MEMORY.ref(2, s3).offset(0x0L).get() + s4         , s2);
            FUN_80106ac4(a3, MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0x400L, s2);
            FUN_80106ac4(a3, MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0x800L, s2);
            FUN_80106ac4(a3, MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0xc00L, s2);
          }
        }
      }

      //LAB_80106fac
      s3 = s3 + 0xeL;
      s7 = s7 + 0xeL;
    }

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x2L, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
  }

  @Method(0x80107088L)
  public static long FUN_80107088(final long a0, final long a1, final BttlScriptData6cSub44 a2, final long a3) {
    if(a2._34.get() >= MEMORY.ref(2, a3).offset(0x10L).getSigned() - 0x11L) {
      MEMORY.ref(2, a3).offset(0x8L).addu(0x1L);

      if(MEMORY.ref(2, a3).offset(0x8L).getSigned() >= 0xeL) {
        MEMORY.ref(2, a3).offset(0x8L).setu(0xdL);
      }
    }

    //LAB_801070ec
    final long s5 = _8011a014.offset(a1).getAddress();
    long s1 = MEMORY.ref(4, a3).offset(0x18L).get();
    long s4 = 0;

    //LAB_80107104
    for(int i = 0; i < 17; i++) {
      if(MEMORY.ref(1, s5).offset(0x0L).getSigned() < 0) {
        MEMORY.ref(2, a3).offset(0x8L).subu(0x3L);

        if(MEMORY.ref(2, a3).offset(0x8L).getSigned() < 0) {
          MEMORY.ref(2, a3).offset(0x8L).setu(0);
        }

        //LAB_80107134
        if(FUN_80106774(s1, 0x20L) == 0x3L) {
          MEMORY.ref(1, s1).offset(0x0L).setu(0);
        }
      }

      //LAB_80107150
      if(MEMORY.ref(1, s1).offset(0x0L).getSigned() != 0) {
        if(MEMORY.ref(2, s1).offset(0xaL).getSigned() > 0) {
          MEMORY.ref(2, s1).offset(0xaL).subu(0x1L);
        } else {
          //LAB_80107178
          if(MEMORY.ref(1, s1).offset(0xdL).get() != 0xffL) {
            MEMORY.ref(1, s1).offset(0xdL).addu(0x1L);
          }

          //LAB_80107190
          MEMORY.ref(1, s1).offset(0xcL).subu(0x1L);
          if(MEMORY.ref(1, s1).offset(0xcL).get() == 0) {
            MEMORY.ref(1, s1).offset(0x0L).setu(0);
          }

          //LAB_801071b0
          if(i < 14) {
            FUN_80106774(s1, 0x4eL);
          }

          s4 = 0x1L;
        }
      }

      //LAB_801071c0
      s1 = s1 + 0xeL;
    }

    return s4;
  }

  @Method(0x801071fcL)
  public static void FUN_801071fc(final BttlScriptData6cSub44 a0, long a1, long a2) {
    final long a3 = _8011a014.offset(a2).getSigned();
    a0._32.set(1);

    a1 = a1 + 0x20L + 0xcL;

    //LAB_80107234
    a2 = a2 + 0x1L;
    for(; a2 < a0._30.get(); a2++) {
      MEMORY.ref(2, a1).offset(0x6L).setu(-0x1L);
      MEMORY.ref(2, a1).offset(0x4L).setu(-0x1L);
      MEMORY.ref(2, a1).offset(0x2L).setu(0);
      MEMORY.ref(2, a1).offset(0x0L).setu(0);
      _8011a014.offset(a2).setu(a3);
      a1 = a1 + 0x20L;
    }

    //LAB_80107264
  }

  @Method(0x8010726cL)
  public static void FUN_8010726c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub44 s2 = data._44.derefAs(BttlScriptData6cSub44.class);

    if(s2._31.get() != 0x1L) {
      if((int)data._10._00.get() >= 0) {
        long s1 = s2._40.get();

        //LAB_801072c4
        int s0;
        for(s0 = 0; s0 < s2._30.get(); s0++) {
          FUN_80106ccc(MEMORY.ref(1, s1).offset(0x2L).getSigned(), s0, s2, s1, state);
          s1 = s1 + 0x20L;
        }

        //LAB_801072f4
        s1 = s2._40.get();

        //LAB_8010730c
        for(s0 = 0; s0 < s2._30.get(); s0++) {
          if(_8011a014.offset(1, s0).getSigned() == 0) {
            break;
          }

          s1 = s1 + 0x20L;
        }

        //LAB_80107330
        if(s0 < s2._30.get()) {
          FUN_80106050((byte)(MEMORY.ref(2, s1).offset(0x10L).getSigned() + (MEMORY.ref(2, s1).offset(0x12L).getSigned() - MEMORY.ref(2, s1).offset(0x10L).getSigned()) / 2 - s2._34.get() - 0x1L), MEMORY.ref(1, s1).offset(0x1cL).getSigned());

          final byte v1 = (byte)s2._34.get();
          if(MEMORY.ref(2, s1).offset(0x10L).getSigned() <= v1 && MEMORY.ref(2, s1).offset(0x12L).getSigned() >= v1) {
            FUN_80106808(s2, s1, -0x2L, state, data);
          }
        }
      }
    }

    //LAB_801073b4
  }

  @Method(0x801073d4L)
  public static void FUN_801073d4(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long v1;
    final long a0;
    long s0;
    long s1;
    long s2;
    long s4;
    final BttlScriptData6cSub44 s3 = data._44.derefAs(BttlScriptData6cSub44.class);

    if(s3._31.get() == 0) {
      s4 = 0x1L;
      s2 = s3._40.get();
      s3._34.incr();

      //LAB_80107440
      for(s0 = 0; s0 < s3._30.get(); s0++) {
        if(s3._34.get() == MEMORY.ref(2, s2).offset(0x12L).getSigned() + 0x1L) {
          if(_8011a014.offset(s0).getSigned() == 0) {
            _8011a014.offset(s0).setu(-0x2L);
            FUN_801071fc(s3, s2, s0);

            //LAB_80107478
            if(_8011a014.offset(s0).getSigned() == 0) {
              s4 = 0;
            }
          }
        } else {
          if(_8011a014.offset(s0).getSigned() == 0) {
            s4 = 0;
          }
        }

        //LAB_8010748c
        s2 = s2 + 0x20L;
      }

      //LAB_801074a8
      if(s4 != 0) {
        FUN_801071fc(s3, s2, s0);
      }

      //LAB_801074bc
      s1 = 0;
      s2 = s3._40.get();

      //LAB_801074d0
      for(s0 = 0; s0 < s3._30.get(); s0++) {
        s1 = s1 + FUN_80107088(MEMORY.ref(1, s2).offset(0x2L).getSigned(), s0, s3, s2);
        s2 = s2 + 0x20L;
      }

      //LAB_80107500
      if(s1 == 0 && s3._32.get() != 0) {
        _80119f41.setu(0);

        //LAB_8010752c
        s2 = s3._40.get();
        for(s0 = 0; s0 < s3._30.get(); s0++) {
          removeFromLinkedList(MEMORY.ref(4, s2).offset(0x18L).get());
          s2 = s2 + 0x20L;
        }

        //LAB_80107554
        deallocateScriptAndChildren(index);
      } else {
        //LAB_8010756c
        if(s3._34.get() >= 0x9L) {
          s2 = s3._40.get();

          //LAB_80107598
          for(s0 = 0; s0 < s3._30.get(); s0++) {
            if(_8011a014.offset(s0).getSigned() == 0) {
              break;
            }

            s2 = s2 + 0x20L;
          }

          //LAB_801075bc
          if(s0 < s3._30.get()) {
            if(state.storage_44.get(8).get() != 0) {
              MEMORY.ref(1, s2).offset(0x1cL).setu(0x1L);
              state.storage_44.get(8).set(0);
            }

            //LAB_801075e8
            if(s3._3a.get() > 0x2L) {
              v1 = s3._3a.get() & 0xffL;

              //LAB_8010763c
              if(v1 != 0x1 && v1 != 0x3L) {
                if(MEMORY.ref(1, s2).offset(0x1cL).getSigned() == 0) {
                  a0 = 0x20L;
                } else {
                  a0 = 0x40L;
                }

                //LAB_80107664
                v1 = joypadPress_8007a398.get();
                if((v1 & 0x60L) != 0) {
                  _8011a014.offset(1, s0).offset(0x0L).setu(-0x1L);
                  if((v1 & a0) == 0 || (v1 & ~a0) != 0) {
                    //LAB_801076d8
                    //LAB_801076dc
                    _8011a014.offset(s0).setu(-0x3L);
                  } else {
                    v1 = s3._34.get();

                    if(v1 >= MEMORY.ref(2, s2).offset(0x10L).getSigned() && v1 <= MEMORY.ref(2, s2).offset(0x12L).getSigned()) {
                      _8011a014.offset(1, s0).offset(0x0L).setu(0x1L);
                      MEMORY.ref(1, s2).offset(0x1L).setu(0x1L);
                    }
                  }

                  //LAB_801076f0
                  if(_8011a014.offset(s0).getSigned() < 0) {
                    FUN_801071fc(s3, s2, s0);
                  }

                  //LAB_80107718
                  //LAB_8010771c
                  s3._38.set(2);
                  s3._39.set((int)s0);
                  s3._3c.set(s2);
                }
              }
            } else {
              v1 = s3._34.get();

              if(v1 >= MEMORY.ref(2, s2).offset(0x10L).getSigned() && v1 <= MEMORY.ref(2, s2).offset(0x12L).getSigned()) {
                _8011a014.offset(s0).setu(0x1L);
                MEMORY.ref(1, s2).offset(0x1L).setu(0x1L);
                s3._38.set(2);
                s3._39.set((int)s0);
                s3._3c.set(s2);
              }
            }
          }

          //LAB_80107728
          if(s3._38.get() != 0) {
            s3._38.decr();
            FUN_80106808(s3, s3._3c.get(), _8011a014.offset(s3._39.get()).getSigned(), state, data);
          }
        }
      }
    }

    //LAB_80107764
  }

  @Method(0x80107790L)
  public static void FUN_80107790(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    removeFromLinkedList(data._44.derefAs(BttlScriptData6cSub44.class)._40.get());
  }

  @Method(0x801077bcL)
  public static long FUN_801077bc(final RunningScript a0) {
    a0.params_20.get(2).deref().set((int)_8011a014.offset(a0.params_20.get(1).deref().get()).getSigned());
    return 0;
  }

  @Method(0x801077e8L)
  public static long FUN_801077e8(final RunningScript s1) {
    final int s2 = allocateEffectManager(
      s1.scriptStateIndex_00.get(),
      0x44L,
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801073d4", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_8010726c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80107790", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub44::new
    );

    final ScriptState<EffectManagerData6c> v0 = scriptStatePtrArr_800bc1c0.get(s2).derefAs(ScriptState.classFor(EffectManagerData6c.class));
    FUN_801062a8(s1.params_20.get(0).deref().get(), s1.params_20.get(1).deref().get(), v0.innerStruct_00.deref()._44.derefAs(BttlScriptData6cSub44.class), s1.params_20.get(2).deref().get());
    v0.storage_44.get(8).set(0);
    s1.params_20.get(4).deref().set(s2);
    _80119f41.setu(0x1L);
    return 0;
  }

  @Method(0x801078c0L)
  public static long FUN_801078c0(final RunningScript a0) {
    final EffectManagerData6c v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final long a2 = v0._44.getPointer(); //TODO
    final int v1 = a0.params_20.get(1).deref().get();
    if(v1 != 1) {
      if(v1 < 2) {
        if(v1 == 0) {
          //LAB_80107924
          MEMORY.ref(1, a2).offset(0x31L).setu(MEMORY.ref(1, a2).offset(0x31L).get() < 1 ? 1 : 0);
        }

        return 0;
      }

      //LAB_80107910
      if(v1 == 2) {
        //LAB_80107984
        //LAB_80107994
        MEMORY.ref(1, a2).offset(0x31L).setu(MEMORY.ref(1, a2).offset(0x31L).get() < 1 ? 2 : 0);
      }

      return 0;
    }

    //LAB_80107930
    MEMORY.ref(1, a2).offset(0x32L).setu(v1);

    //LAB_80107954
    long ptr = MEMORY.ref(4, a2).offset(0x40L).get() + 0x10L;
    for(int i = 0; i < MEMORY.ref(1, a2).offset(0x30L).get(); i++) {
      MEMORY.ref(2, ptr).offset(0x0L).setu(0);
      MEMORY.ref(2, ptr).offset(0x2L).setu(0);
      _8011a014.offset(i).setu(-0x1L);
      ptr = ptr + 0x20L;
    }

    //LAB_80107998
    //LAB_8010799c
    return 0;
  }

  @Method(0x801079a4L)
  public static long FUN_801079a4(final RunningScript a0) {
    if(a0.params_20.get(0).deref().get() == 0) {
      a0.params_20.get(1).deref().set((int)_80119f41.getSigned());
    } else {
      //LAB_801079d0
      a0.params_20.get(1).deref().set((int)_80119f42.getSigned());
    }

    //LAB_801079e0
    return 0;
  }

  @Method(0x801079e8L)
  public static void FUN_801079e8(final EffeScriptData1c a0) {
    final int s4 = (int)_8011a01c.get();
    final int s3 = (int)_8011a020.get();
    final int fp = s4 + 16;
    final int s7 = s3 + 70;
    final long func = getMethodAddress(Bttl_800d.class, "FUN_800d46d4", RunningScript.class);

    final int a2;
    if(FUN_80108460(a0, 0) != 0) {
      callScriptFunction(func, 5, 36, fp, s3 + 64, 1, 128);
      a2 = 33;
    } else {
      //LAB_80107a80
      if(FUN_80108460(a0, 2) != 0) {
        callScriptFunction(func, 5, 36, fp, s3 + 60, 1, 128);
        a2 = 33;
      } else {
        //LAB_80107ad4
        callScriptFunction(func, 5, 36, fp, s3 + 56, 1, 128);
        a2 = 35;
      }
    }

    //LAB_80107b10
    callScriptFunction(func, 5, a2, s4 + 12, s3 + 66, 1, 128);

    if(a0._11.get() != 0) {
      final byte v0 = (byte)(a0._11.get() * 64 - 1);
      final byte[] sp0x30 = {v0, v0, v0};
      callScriptFunction(func, 5, 20, fp - 4, s7 - 4, 1, 128);
      FUN_80018a5c((short)fp - 2, (short)s7 - 5, 0xe8L, 0x78L, 0xffL, 0x8fL, 0xcL, 0x1L, sp0x30, a0._11.get() * 256 + 6404, a0._11.get() * 256 + 4096);
    }

    //LAB_80107bf0

    final int s1 = Math.min(4, a0._07.get());

    //LAB_80107c08
    callScriptFunction(func, 5, s1 + 4, fp + 36, s7,      1, 128);
    callScriptFunction(func, 5, 24,     fp -  4, s7,      1, 128);
    callScriptFunction(func, 5, 26,     fp -  4, s7 + 12, 1, 128);
    callScriptFunction(func, 5, 30,     fp -  4, s7 +  4, 1, 128);
    callScriptFunction(func, 5, 31,     fp + 48, s7 +  4, 1, 128);

    //LAB_80107cd4
    for(int i = 0; i < 48; i += 8) {
      callScriptFunction(func, 5, 28, fp + i, s7,      1, 128);
      callScriptFunction(func, 5, 29, fp + i, s7 + 12, 1, 128);
      callScriptFunction(func, 5, 32, fp + i, s7 -  4, 1, 128);
    }

    callScriptFunction(func, 5, 25, fp + 48, s7     , 1, 128);
    callScriptFunction(func, 5, 27, fp + 48, s7 + 12, 1, 128);
  }

  @Method(0x80107dc4L)
  public static void FUN_80107dc4(final EffeScriptData1c a0, final int a1, final int angle) {
    final Memory.TemporaryReservation sp0x40tmp = MEMORY.temp(0x6);
    final Value sp0x40 = sp0x40tmp.get();
    memcpy(sp0x40.getAddress(), _800fb7fc.getAddress(), 0x6);

    final Memory.TemporaryReservation sp0x48tmp = MEMORY.temp(0x14);
    final Value sp0x48 = sp0x48tmp.get();
    memcpy(sp0x48.getAddress(), _800fb804.getAddress(), 0x14);

    final Memory.TemporaryReservation sp0x60tmp = MEMORY.temp(0x14);
    final Value sp0x60 = sp0x60tmp.get();
    memcpy(sp0x60.getAddress(), _800fb818.getAddress(), 0x14);

    final Memory.TemporaryReservation sp0x78tmp = MEMORY.temp(0x14);
    final Value sp0x78 = sp0x78tmp.get();
    memcpy(sp0x78.getAddress(), _800fb82c.getAddress(), 0x14);

    final Memory.TemporaryReservation sp0x90tmp = MEMORY.temp(0xa);
    final Value sp0x90 = sp0x90tmp.get();
    memcpy(sp0x90.getAddress(), _800fb840.getAddress(), 0xa);

    final Memory.TemporaryReservation sp0xa0tmp = MEMORY.temp(0xa);
    final Value sp0xa0 = sp0xa0tmp.get();
    memcpy(sp0xa0.getAddress(), _800fb84c.getAddress(), 0xa);

    final byte colour = (byte)((_80119fb4.get() + 1) * 64);
    final byte[] sp0x30 = {colour, colour, colour};

    final int y = (short)_8011a020.get() + (rsin(angle) * 17 >> 12) + 24;

    final int x;
    if(_8011a028.deref(4).offset(a0._06.get() * 0x4L).get() >= 2) {
      x = (short)_8011a01c.get() + (rcos(angle) * 17 >> 12) + 28;
    } else {
      x = (short)_8011a01c.get() + 28;
    }

    //LAB_80108048
    FUN_80018d60((short)x, (short)y, 128, 64, 16, 16, 51, 1, sp0x30, 0x1000);

    sp0x30[0] = (byte)0x80;
    sp0x30[1] = (byte)0x80;
    sp0x30[2] = (byte)0x80;

    //LAB_801080ac
    for(int i = 0; i < 5; i++) {
      if(a0._07.get() < i) {
        sp0x30[0] = 0x10;
        sp0x30[1] = 0x10;
        sp0x30[2] = 0x10;
      }

      //LAB_801080cc
      FUN_80018d60((short)_8011a01c.get() + sp0x48.offset(2, i * 0x4L).getSigned(), (short)_8011a020.get() + sp0x48.offset(2, i * 0x4L).offset(0x2L).getSigned(), sp0x60.offset(1, i * 0x4L).getSigned(), sp0x60.offset(1, i * 0x4L).offset(0x2L).getSigned(), sp0x78.offset(2, i * 0x4L).getSigned(), sp0x78.offset(2, i * 0x4L).offset(0x2L).getSigned(), 53 + i, 1, sp0x30, 0x1000);
    }

    FUN_801079e8(a0);

    sp0x30[0] = (byte)0x80;
    sp0x30[1] = (byte)0x80;
    sp0x30[2] = (byte)0x80;

    //LAB_801081a8
    for(int i = 0; i < a0._0d.get(); i++) {
      FUN_80018d60((short)_8011a01c.get() + 18, (short)_8011a020.get() + 16, 224, 208, 31, 31, sp0xa0.offset(1, a0._18.get()).get(), 1, sp0x30, 0x1000);

      if(a0._18.get() == 9) {
        FUN_80018dec(_8011a01c.getSigned() + 23, _8011a020.getSigned() + 21, 232, 120, 23, 23, 12, 1, sp0x30, 0x800, 0x1800);
      }

      //LAB_80108250
    }

    //LAB_80108268
    FUN_80018d60((short)_8011a01c.get() + 32, (short)_8011a020.get() -  4, 152, 208,  8, 24, 50, 0, sp0x30, 0x1000);
    FUN_80018d60((short)_8011a01c.get() + 18, (short)_8011a020.get() + 16, 224, 208, 31, 31, sp0xa0.offset(1, a0._18.get()).getSigned(), sp0x40.offset(1, a1 * 2).getSigned(), sp0x30, 0x1000);
    FUN_80018d60((short)_8011a01c.get() + 17, (short)_8011a020.get() + 14, 112, 200, 40, 40, 52, sp0x40.offset(1, a1 * 2).offset(0x1L).getSigned(), sp0x30, 0x1000);
    FUN_80018d60((short)_8011a01c.get(),      (short)_8011a020.get(),      160, 192, 64, 48, sp0x90.offset(1, a0._18.get()).getSigned(), -1, sp0x30, 0x1000);
    FUN_80018d60((short)_8011a01c.get() +  8, (short)_8011a020.get() + 48, 200,  80, 42,  8, sp0x90.offset(1, a0._18.get()).getSigned(), -1, sp0x30, 0x1000);
    _80119fb4.setu(1 - _80119fb4.get());

    sp0x40tmp.release();
    sp0x48tmp.release();
    sp0x60tmp.release();
    sp0x78tmp.release();
    sp0x90tmp.release();
    sp0xa0tmp.release();
  }

  @Method(0x80108460L)
  public static int FUN_80108460(final EffeScriptData1c a0, final int a1) {
    int t4 = 0;
    int t1 = 0;
    long t0 = _8011a02c.get();
    long t2 = _8011a028.get();
    final int t3 = a0._04.get() - 1;

    //LAB_80108484
    for(int i = 0; i < 5; i++) {
      t1 += MEMORY.ref(4, t2).offset(0x0L).get();
      final int v1 = t1 - a1 - ((int)MEMORY.ref(4, t0).offset(0x0L).get() >> 1);
      //LAB_801084c4
      if((i & 0x1L) == 0 && t3 >= v1 + 1 || (i & 0x1L) != 0 && t3 >= v1) {
        //LAB_801084cc
        final int a2 = (int)MEMORY.ref(4, t0).offset(0x0L).get();
        if(a2 != 0 && t1 + (a2 >> 1) >= a0._04.get() - 1) {
          t4 = i + 1;
        }
      }

      //LAB_801084f8
      //LAB_801084fc
      t0 = t0 + 0x4L;
      t2 = t2 + 0x4L;
    }

    return t4;
  }

  @Method(0x80108514L)
  public static void FUN_80108514(final int index, final ScriptState<EffeScriptData1c> state, final EffeScriptData1c data) {
    FUN_80107dc4(data, 0, data._02.get() - 0x400);
  }

  @Method(0x80108574L)
  public static void FUN_80108574(final int index, final ScriptState<EffeScriptData1c> state, final EffeScriptData1c data) {
    if(data._0f.get() == 0) {
      if(data._10.get() == 0) {
        data._12.decr();
        if(data._12.get() == 0) {
          deallocateScriptAndChildren(index);
        } else if((joypadPress_8007a398.get() >>> 4 & 0x2L) != 0 && data._13.get() != 2) {
          data._10.set(1);
          _80119f42.setu(1);
        }
      } else {
        //LAB_80108600
        if(data._11.get() != 0) {
          data._11.decr();
        }

        //LAB_80108614
        if(data._0e.get() != 0) {
          data._0e.decr();

          if(data._0e.get() == 0) {
            _80119f42.setu(0);

            //LAB_80108638
            deallocateScriptAndChildren(index);
          }
        } else {
          //LAB_8010864c
          data._04.incr();
          data._02.add(0x1000 / (int)_8011a028.deref(4).offset(data._06.get() * 0x4L).get());

          if(data._06.get() + 1 << 12 < data._02.get()) {
            data._06.incr();
          }

          //LAB_801086a8
          if(data._0d.get() != 0) {
            data._0d.decr();
          }

          //LAB_801086bc
          //LAB_801086e0
          if(FUN_80108460(data, 0) != 0 && data._13.get() == 1 || (joypadPress_8007a398.get() >>> 4 & 0x2L) != 0 && data._13.get() == 0) {
            //LAB_8010870c
            data._11.set(4);
            data._0d.set(0);

            final int v0 = FUN_80108460(data, 0);
            if(v0 != 0) {
              data._07.set(v0);
              data._0d.set(4);
              _80119f40.setu(0);
            } else {
              //LAB_8010873c
              if(data._07.get() == 0) {
                _80119f40.setu(-1);
              } else {
                //LAB_8010875c
                _80119f40.setu(data._07.get());
              }

              //LAB_80108760
              data._0e.set(1);
            }
          }

          //LAB_80108768
          if(data._07.get() < data._02.get() - 0x400 >> 12) {
            data._0d.set(0);

            if(data._07.get() == 0) {
              _80119f40.setu(-1);
            } else {
              //LAB_801087a4
              if(data._07.get() == data._14.get()) {
                FUN_80108cf4();
              }

              //LAB_801087bc
              _80119f40.setu(data._07.get());
            }

            //LAB_801087c8
            data._0e.set(4);
          }

          //LAB_801087d0
          data._05.set(data._02.get() & 0xff);
        }
      }
    }

    //LAB_801087dc
  }

  @Method(0x801087f0L)
  public static void FUN_801087f0(final int index, final ScriptState<MemoryRef> state, final MemoryRef data) {
    // no-op
  }

  /**
   * Pretty sure this is the script allocation for dragoon additions
   *
   * {@link SEffe#FUN_80108514}
   * {@link SEffe#FUN_80108574}
   * {@link SEffe#FUN_801087f0}
   */
  @Method(0x801087f8L)
  public static long FUN_801087f8(final RunningScript script) {
    final int s4 = script.params_20.get(1).deref().get();
    final int s2 = script.params_20.get(0).deref().get();

    final int scriptIndex = allocateScriptState(0x1c, EffeScriptData1c::new);
    loadScriptFile(scriptIndex, _8004f650, "", 0); //TODO
    setCallback04(scriptIndex, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80108574", int.class, ScriptState.classFor(EffeScriptData1c.class), EffeScriptData1c.class), TriConsumerRef::new));
    setCallback08(scriptIndex, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80108514", int.class, ScriptState.classFor(EffeScriptData1c.class), EffeScriptData1c.class), TriConsumerRef::new));
    setScriptDestructor(scriptIndex, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801087f0", int.class, ScriptState.classFor(MemoryRef.class), MemoryRef.class), TriConsumerRef::new));

    final EffeScriptData1c s1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffeScriptData1c.class);
    s1._00.set(1);
    s1._02.set(0);
    s1._04.set(0);
    s1._05.set(0);
    s1._06.set(0);
    s1._07.set(0);
    s1._0d.set(0);
    s1._0e.set(0);
    s1._0f.set((s2 ^ 0x3L) < 0x1L ? 1 : 0);
    s1._10.set(0);
    s1._11.set(0);
    s1._12.set(script.params_20.get(2).deref().get());
    s1._13.set(s2 & 0xff);
    s1._18.set(s4);

    if(s2 == 1) {
      s1._10.set(s2 & 0xff);
    }

    //LAB_80108910
    _8011a01c.setu(script.params_20.get(3).deref().get());
    _8011a020.setu(script.params_20.get(4).deref().get());

    //LAB_80108924
    for(int i = 0; i < 5; i++) {
      s1._08.get(i).set(0);
    }

    if(s4 == 7) {
      _8011a028.setu(_80119f7c.getAddress());
      _8011a02c.setu(_80119f98.getAddress());
      s1._14.set(3);
    } else {
      //LAB_80108964
      _8011a028.setu(_80119f44.getAddress());
      _8011a02c.setu(_80119f60.getAddress());
      s1._14.set(4);
    }

    //LAB_80108984
    _80119f40.setu(0);
    _80119f42.setu(0);
    _8011a024.setu(scriptIndex);
    return 0;
  }

  @Method(0x801089ccL)
  public static long FUN_801089cc(final RunningScript a0) {
    a0.params_20.get(1).deref().set((byte)_80119f40.getSigned());
    return 0;
  }

  @Method(0x801089e8L)
  public static void FUN_801089e8(final int index, final ScriptState<EffeScriptData30> state, final EffeScriptData30 data) {
    //LAB_80108a38
    for(int i = 7; i >= 0; i--) {
      final byte[] sp0x28 = {(byte)0x80, (byte)0x80, (byte)0x80};

      final long fp = _80119fbc.get(i).getAddress();
      final long s2 = _80119fc4.get(i).getAddress();

      final EffeScriptData30Sub06 struct = data._00.get(i);

      final int v1 = struct._00.get();
      if(v1 == 0) {
        //LAB_80108a78
        struct._02.decr();
        if(struct._02.get() < struct._04.get()) {
          struct._02.set(struct._04.get());
          struct._00.incr();
        }

        //LAB_80108aac
        //LAB_80108ac4
        int s0 = 0;
        do {
          sp0x28[0] -= 32;
          sp0x28[1] -= 32;
          sp0x28[2] -= 32;
          s0++;
          FUN_80018d60(struct._02.get() + s0 * 6, (short)_8011a020.get() + 16, MEMORY.ref(1, fp).offset(0x0L).get(), MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, 1, sp0x28, 0x1000);
        } while(s0 < 4);
      } else if(v1 == 1) {
        //LAB_80108b58
        if(data._00.get(7)._00.get() == 1) {
          struct._00.set(2);
        }
        //LAB_80108a60
      } else if(v1 == 2) {
        //LAB_80108b84
        struct._01.incr();
        if(struct._01.get() >= 12 && i == 0) {
          data._00.get(7)._00.incr();
        }
      } else if(v1 == 3) {
        //LAB_80108bd0
        deallocateScriptAndChildren(index);
        return;
      }

      //LAB_80108be8
      //LAB_80108bec
      //LAB_80108bf0
      FUN_80018d60(struct._02.get(), (short)_8011a020.get() + 16, MEMORY.ref(1, fp).offset(0x0L).get(), MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, 1, sp0x28, 0x1000);

      if((struct._01.get() & 0x1L) != 0) {
        FUN_80018d60(struct._02.get(), (short)_8011a020.get() + 16, MEMORY.ref(1, fp).offset(0x0L).get(), MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, 1, sp0x28, 0x1000);
      }

      //LAB_80108cb0
    }

    //LAB_80108cc4
  }

  /**
   * {@link SEffe#FUN_801089e8}
   * {@link SEffe#FUN_801087f0}
   */
  @Method(0x80108cf4L)
  public static void FUN_80108cf4() {
    playSound(0, 50, 0, 0, (short)0, (short)0);

    final int scriptIndex = allocateScriptState(0x30, EffeScriptData30::new);
    loadScriptFile(scriptIndex, _8004f650, "", 0); //TODO
    setCallback04(scriptIndex, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801089e8", int.class, ScriptState.classFor(EffeScriptData30.class), EffeScriptData30.class), TriConsumerRef::new));
    setScriptDestructor(scriptIndex, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801087f0", int.class, ScriptState.classFor(MemoryRef.class), MemoryRef.class), TriConsumerRef::new));
    final EffeScriptData30 data = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffeScriptData30.class);

    //LAB_80108d9c
    int s2 = (int)_8011a01c.get();
    int s3 = 130;
    for(int i = 0; i < 8; i++) {
      final EffeScriptData30Sub06 s1 = data._00.get(i);

      s2 = s2 + 8;
      s1._00.set(0);
      s1._01.set(0);
      s1._02.set(s3);
      s1._04.set(s2);
      s3 = s3 + 32;
    }
  }

  @Method(0x80108df8L)
  public static long FUN_80108df8(final RunningScript a0) {
    a0.params_20.get(0).deref().set(allocateEffectManager(a0.scriptStateIndex_00.get(), 0, null, null, null, null));
    return 0;
  }

  @Method(0x80108e40L)
  public static void FUN_80108e40(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub08_3 s4 = data._44.derefAs(BttlScriptData6cSub08_3.class);
    long s1 = s4.ptr_04.get();

    //LAB_80108e84
    for(long s3 = 0; s3 < s4.count_00.get(); s3++) {
      if(Math.abs(Math.abs(MEMORY.ref(2, s1).offset(0x4L).getSigned() + MEMORY.ref(2, s1).offset(0x2L).getSigned()) - Math.abs(MEMORY.ref(2, s1).offset(0x8L).getSigned() + MEMORY.ref(2, s1).offset(0x6L).getSigned())) > 180) {
        final long a1 = linkedListAddress_1f8003d8.get();
        MEMORY.ref(1, a1).offset(0x03L).setu(0x4L);
        MEMORY.ref(1, a1).offset(0x04L).setu(0);
        MEMORY.ref(1, a1).offset(0x05L).setu(0);
        MEMORY.ref(1, a1).offset(0x06L).setu(0);
        MEMORY.ref(4, a1).offset(0x07L).setu(0x52L);
        MEMORY.ref(2, a1).offset(0x08L).setu(MEMORY.ref(2, s1).offset(0x06L).get() - 0x100L);
        MEMORY.ref(2, a1).offset(0x0aL).setu(MEMORY.ref(2, s1).offset(0x08L).get() - 0x80L);
        MEMORY.ref(1, a1).offset(0x0cL).setu(data._10.svec_1c.getX());
        MEMORY.ref(1, a1).offset(0x0dL).setu(data._10.svec_1c.getY());
        MEMORY.ref(1, a1).offset(0x0eL).setu(data._10.svec_1c.getZ());
        MEMORY.ref(2, a1).offset(0x10L).setu(MEMORY.ref(2, s1).offset(0x02L).get() - 0x100L);
        MEMORY.ref(2, a1).offset(0x12L).setu(MEMORY.ref(2, s1).offset(0x04L).get() - 0x80L);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, a1);
        linkedListAddress_1f8003d8.addu(0x14L);
      }

      //LAB_80108f6c
      s1 = s1 + 0xcL;
    }

    //LAB_80108f84
    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(1, data._10._00.get() >>> 28 & 0x3L, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
  }

  @Method(0x80109000L)
  public static void FUN_80109000(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub08_3 s3 = data._44.derefAs(BttlScriptData6cSub08_3.class);
    long v1 = s3.ptr_04.get();

    //LAB_80109038
    for(int i = 0; i < s3.count_00.get(); i++) {
      long sp10 = rsin(data._10.svec_10.getX()) << 5 >> 12;
      long sp12 = rcos(data._10.svec_10.getX()) << 5 >> 12;
      sp10 = sp10 * data._10.svec_16.getX() * MEMORY.ref(2, v1).offset(0xaL).getSigned() >> 24;
      sp12 = sp12 * data._10.svec_16.getX() * MEMORY.ref(2, v1).offset(0xaL).getSigned() >> 24;
      MEMORY.ref(2, v1).offset(0x6L).setu(MEMORY.ref(2, v1).offset(0x2L).get());
      MEMORY.ref(2, v1).offset(0x8L).setu(MEMORY.ref(2, v1).offset(0x4L).get());
      MEMORY.ref(2, v1).offset(0x2L).addu(sp10).and(0x1ff);
      MEMORY.ref(2, v1).offset(0x4L).addu(sp12).and(0xff);
      v1 = v1 + 0xcL;
    }

    //LAB_80109110
  }

  @Method(0x8010912cL)
  public static void FUN_8010912c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    removeFromLinkedList(data._44.derefAs(BttlScriptData6cSub08_3.class).ptr_04.get());
  }

  @Method(0x80109158L)
  public static long FUN_80109158(final RunningScript a0) {
    final int count = a0.params_20.get(1).deref().get();
    final int scriptIndex = allocateEffectManager(
      a0.scriptStateIndex_00.get(),
      0x8L,
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80109000", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80108e40", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_8010912c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub08_3::new
    );

    final EffectManagerData6c s1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub08_3 s0 = s1._44.derefAs(BttlScriptData6cSub08_3.class);
    long t1 = addToLinkedListTail(count * 0xcL);
    s0.count_00.set(count);
    s0.ptr_04.set(t1);
    s1._10._00.set(0x5000_0000L);

    //LAB_80109204
    for(int i = 0; i < count; i++) {
      MEMORY.ref(1, t1).offset(0x0L).setu(0x1L);
      MEMORY.ref(2, t1).offset(0x2L).setu(seed_800fa754.advance().get() % 513);
      MEMORY.ref(2, t1).offset(0x4L).setu(seed_800fa754.advance().get() % 257);
      MEMORY.ref(2, t1).offset(0xaL).setu(seed_800fa754.advance().get() % 3073 + 1024);
      t1 = t1 + 0xcL;
    }

    //LAB_80109328
    a0.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  /** TODO there's no way this method is right */
  @Method(0x80109358L)
  public static void FUN_80109358(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long v0;
    long a1;
    long s0;
    long s1;
    long s2;

    final long u = doubleBufferFrame_800bb108.get() == 0 ? 16 : 0;

    final BttlScriptData6cSub08_2 sp48 = data._44.derefAs(BttlScriptData6cSub08_2.class);
    final long sp30 = data._10.svec_16.getX() >> 8;
    final long sp2c = data._10.svec_16.getY() >> 11;
    final long sp38 = data._10.svec_16.getZ() * 15 >> 9;

    //LAB_801093f0
    long s3 = 0x1L;
    do {
      final long t1 = sp48._00.get();
      long sp28 = t1;
      long sp34 = t1;

      //LAB_80109410
      long sp40;
      long s5;
      if(s3 != 0x1L) {
        s5 = -0x1L;
        sp40 = 0x77L;
      } else {
        s5 = 0;
        sp40 = 0x78L;
      }

      //LAB_80109430
      //LAB_8010944c
      while(s3 != 1 && s5 > -sp38 || s5 < sp38) {
        v0 = (int)(rsin(sp28) * sp2c) >> 12;
        s2 = v0 + 0x1L + sp2c;
        if(s2 == 0) {
          s2 = 0x1L;
        }

        //LAB_8010949c
        //LAB_801094b8
        for(int s6 = 0; s6 < s2; s6++) {
          s1 = (int)(rsin(sp34) * sp30) >> 12;
          s0 = s5 + s6 * s3;

          a1 = linkedListAddress_1f8003d8.get();
          MEMORY.ref(1, a1).offset(0x3L).setu(0x4L);
          MEMORY.ref(1, a1).offset(0x4L).setu(data._10.svec_1c.getX());
          MEMORY.ref(1, a1).offset(0x5L).setu(data._10.svec_1c.getY());
          MEMORY.ref(1, a1).offset(0x6L).setu(data._10.svec_1c.getZ());
          MEMORY.ref(1, a1).offset(0x7L).setu(0x66L);
          MEMORY.ref(2, a1).offset(0x8L).setu(-0xa0L - s1);
          MEMORY.ref(2, a1).offset(0xaL).setu(s0);
          MEMORY.ref(1, a1).offset(0xcL).setu(0);
          MEMORY.ref(1, a1).offset(0xdL).setu(u + sp40);
          MEMORY.ref(2, a1).offset(0x10L).setu(0x100L);
          MEMORY.ref(2, a1).offset(0x12L).setu(0x1L);
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, a1);
          linkedListAddress_1f8003d8.addu(0x14L);

          a1 = linkedListAddress_1f8003d8.get();
          MEMORY.ref(1, a1).offset(0x3L).setu(0x4L);
          MEMORY.ref(1, a1).offset(0x4L).setu(data._10.svec_1c.getX());
          MEMORY.ref(1, a1).offset(0x5L).setu(data._10.svec_1c.getY());
          MEMORY.ref(1, a1).offset(0x6L).setu(data._10.svec_1c.getZ());
          MEMORY.ref(1, a1).offset(0x7L).setu(0x66L);
          MEMORY.ref(2, a1).offset(0x8L).setu(0x60L - s1);
          MEMORY.ref(2, a1).offset(0xaL).setu(s0);
          MEMORY.ref(1, a1).offset(0xcL).setu(0);
          MEMORY.ref(1, a1).offset(0xdL).setu(u + sp40);
          MEMORY.ref(2, a1).offset(0x10L).setu(0x40L);
          MEMORY.ref(2, a1).offset(0x12L).setu(0x1L);
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x74L, a1);
          linkedListAddress_1f8003d8.addu(0x14L);

          sp34 = sp34 + s3 * 0x20L;
        }

        //LAB_80109678
        sp28 = sp28 + s2 * 0x20L;
        sp40 = sp40 + s3;
        s5 = s5 + s2 * s3;

        //LAB_801096b8
      }

      //LAB_801096cc
      s3 -= 2;
    } while(s3 < -1);

    final long y = doubleBufferFrame_800bb108.get() == 0 ? 0 : 256;

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x2L, data._10._00.get() >>> 28 & 0x3L, 0, y), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x2L, data._10._00.get() >>> 28 & 0x3L, 0x100L, y), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x74L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
  }

  @Method(0x801097e0L)
  public static void FUN_801097e0(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final long y = doubleBufferFrame_800bb108.get() == 0 ? 0 : 256;
    final long v = doubleBufferFrame_800bb108.get() == 0 ? 16 : 0;

    final long packet1 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, packet1).offset(0x3L).setu(0x4L);
    MEMORY.ref(1, packet1).offset(0x4L).setu(data._10.svec_1c.getX());
    MEMORY.ref(1, packet1).offset(0x5L).setu(data._10.svec_1c.getY());
    MEMORY.ref(1, packet1).offset(0x6L).setu(data._10.svec_1c.getZ());
    MEMORY.ref(1, packet1).offset(0x7L).setu(0x66L);
    MEMORY.ref(2, packet1).offset(0x8L).setu(-0xa0L);
    MEMORY.ref(2, packet1).offset(0xaL).setu(-0x78L);
    MEMORY.ref(1, packet1).offset(0xcL).setu(0);
    MEMORY.ref(1, packet1).offset(0xdL).setu(v);
    MEMORY.ref(2, packet1).offset(0x10L).setu(0x100L);
    MEMORY.ref(2, packet1).offset(0x12L).setu(0xf0L);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, packet1);
    linkedListAddress_1f8003d8.addu(0x14L);

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x2L, data._10._00.get() >>> 28 & 0x3L, 0, y), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);

    final long packet2 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, packet2).offset(0x3L).setu(0x4L);
    MEMORY.ref(1, packet2).offset(0x7L).setu(0x66L);
    MEMORY.ref(1, packet2).offset(0x4L).setu(data._10.svec_1c.getX());
    MEMORY.ref(1, packet2).offset(0x5L).setu(data._10.svec_1c.getY());
    MEMORY.ref(1, packet2).offset(0x6L).setu(data._10.svec_1c.getZ());
    MEMORY.ref(2, packet2).offset(0x8L).setu(0x60L);
    MEMORY.ref(2, packet2).offset(0xaL).setu(-0x78L);
    MEMORY.ref(1, packet2).offset(0xcL).setu(0);
    MEMORY.ref(1, packet2).offset(0xdL).setu(v);
    MEMORY.ref(2, packet2).offset(0x10L).setu(0x40L);
    MEMORY.ref(2, packet2).offset(0x12L).setu(0xf0L);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, packet2);
    linkedListAddress_1f8003d8.addu(0x14L);

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x2L, data._10._00.get() >>> 28 & 0x3L, 0x100L, y), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
  }

  @Method(0x80109a4cL)
  public static void FUN_80109a4c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub08_2 v0 = data._44.derefAs(BttlScriptData6cSub08_2.class);
    v0._00.add(v0._04.get());
  }

  @Method(0x80109a6cL)
  public static void FUN_80109a6c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // no-op
  }

  @Method(0x80109a74L)
  public static void FUN_80109a74(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // no-op
  }

  @Method(0x80109a7cL)
  public static long FUN_80109a7c(final RunningScript s0) {
    final int scriptIndex = allocateEffectManager(
      s0.scriptStateIndex_00.get(),
      0x8L,
      _80119fd4.get(s0.params_20.get(2).deref().get()).deref(),
      _80119fe0.get(s0.params_20.get(2).deref().get()).deref(),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80109a74", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub08_2::new
    );

    final EffectManagerData6c a0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub08_2 v1 = a0._44.derefAs(BttlScriptData6cSub08_2.class);
    v1._00.set(0x800L);
    v1._04.set(s0.params_20.get(1).deref().get());
    a0._10._00.set(0x4000_0000L);
    s0.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x80109b3cL)
  public static void FUN_80109b3c(final int index, final ScriptState<EffeScriptData18> state, final EffeScriptData18 data) {
    // no-op
  }

  @Method(0x80109b44L)
  public static void FUN_80109b44(final int index, final ScriptState<EffeScriptData18> state, final EffeScriptData18 data) {
    long v1;

    data.ticksRemaining_00.decr();
    if(data.ticksRemaining_00.get() < 0) {
      deallocateScriptAndChildren(index);
      return;
    }

    //LAB_80109b7c
    long t1 = data._0c.get();

    //LAB_80109b90
    for(int i = 0; i < data.count_08.get(); i++) {
      final long a1 = i * 0x10;
      final long p10 = data.ptr_10.get() + a1;
      final long p14 = data.ptr_14.get() + a1;
      MEMORY.ref(4, p14).offset(0x0L).setu(MEMORY.ref(4, p10).offset(0x0L).get());
      MEMORY.ref(4, p14).offset(0x4L).setu(MEMORY.ref(4, p10).offset(0x4L).get());
      MEMORY.ref(4, p14).offset(0x8L).setu(MEMORY.ref(4, p10).offset(0x8L).get());
      v1 = MEMORY.ref(4, p10).offset(0x0L).get();
      v1 = v1 + (v1 * data._04.get() >> 8);
      MEMORY.ref(4, p10).offset(0x0L).setu(v1);
      v1 = MEMORY.ref(4, p10).offset(0x4L).get();
      v1 = v1 + (v1 * data._04.get() >> 8);
      MEMORY.ref(4, p10).offset(0x4L).setu(v1);
      v1 = MEMORY.ref(4, p10).offset(0x8L).get();
      v1 = v1 + (v1 * data._04.get() >> 8);
      MEMORY.ref(4, p10).offset(0x8L).setu(v1);
      MEMORY.ref(2, t1).offset(0x0L).setu(MEMORY.ref(4, p14).offset(0x0L).get() >> 8);
      MEMORY.ref(2, t1).offset(0x2L).setu(MEMORY.ref(4, p14).offset(0x4L).get() >> 8);
      MEMORY.ref(2, t1).offset(0x4L).setu(MEMORY.ref(4, p14).offset(0x8L).get() >> 8);
      t1 = t1 + 0x8L;
    }

    //LAB_80109ce0
  }

  @Method(0x80109cf0L)
  public static void FUN_80109cf0(final int index, final ScriptState<EffeScriptData18> state, final EffeScriptData18 data) {
    removeFromLinkedList(data.ptr_10.get());
    removeFromLinkedList(data.ptr_14.get());
  }

  @Method(0x80109d30L)
  public static long FUN_80109d30(final RunningScript a0) {
    final int s5 = a0.params_20.get(2).deref().get();
    final int s4 = a0.params_20.get(3).deref().get();
    final int s2 = allocateScriptState(0x18, EffeScriptData18::new);
    loadScriptFile(s2, _8004f650, "", 0); //TODO
    setCallback04(s2, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80109b44", int.class, ScriptState.classFor(EffeScriptData18.class), EffeScriptData18.class), TriConsumerRef::new));
    setCallback08(s2, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80109b3c", int.class, ScriptState.classFor(EffeScriptData18.class), EffeScriptData18.class), TriConsumerRef::new));
    setScriptDestructor(s2, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80109cf0", int.class, ScriptState.classFor(EffeScriptData18.class), EffeScriptData18.class), TriConsumerRef::new));
    long v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.getPointer(); //TODO
    long v1 = MEMORY.ref(4, v0).offset(0x8L).get();
    v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.getPointer(); //TODO
    long s2_0 = MEMORY.ref(4, v1).offset(0x0L).get();
    final long s0 = MEMORY.ref(4, v1).offset(0x4L).get();
    final EffeScriptData18 s3 = scriptStatePtrArr_800bc1c0.get(s2).deref().innerStruct_00.derefAs(EffeScriptData18.class);
    s3.ticksRemaining_00.set(s5);
    s3._04.set(s4);
    s3.count_08.set(s0);
    s3._0c.set(s2_0);
    s3.ptr_10.set(addToLinkedListTail(s0 * 0x10));
    s3.ptr_14.set(addToLinkedListTail(s0 * 0x10));
    v1 = MEMORY.ref(4, v0).offset(0x8L).get();
    final long s6 = MEMORY.ref(4, v1).offset(0x0L).get();
    v1 = 0x8012_0000L;
    MEMORY.ref(1, v1).offset(-0x5fd0L).setu(0x1L);

    //LAB_80109e78
    for(int i = 0; i < s0; i++) {
      final long a0_0 = i * 0x10L;
      v1 = s3.ptr_14.get() + a0_0;
      MEMORY.ref(4, v1).offset(0x0L).setu(MEMORY.ref(2, s2_0).offset(0x0L).getSigned() * 0x100);
      MEMORY.ref(4, v1).offset(0x4L).setu(MEMORY.ref(2, s2_0).offset(0x2L).getSigned() * 0x100);
      MEMORY.ref(4, v1).offset(0x8L).setu(MEMORY.ref(2, s2_0).offset(0x4L).getSigned() * 0x100);
      s2_0 = s2_0 + 0x8L;
    }

    //LAB_80109ecc
    s2_0 = s6;

    //LAB_80109ee4
    for(int i = 0; i < s3.count_08.get(); i++) {
      final long a0_0 = i * 0x10L;
      final long v0_0 = s3.ptr_14.get() + a0_0;
      final long v0_1 = s3.ptr_10.get() + a0_0;
      MEMORY.ref(4, v0_1).offset(0x0L).setu((MEMORY.ref(2, s2_0).offset(0x0L).getSigned() * 0x100 - MEMORY.ref(4, v0_0).offset(0x0L).get()) / s5);
      MEMORY.ref(4, v0_1).offset(0x4L).setu((MEMORY.ref(2, s2_0).offset(0x2L).getSigned() * 0x100 - MEMORY.ref(4, v0_0).offset(0x4L).get()) / s5);
      MEMORY.ref(4, v0_1).offset(0x8L).setu((MEMORY.ref(2, s2_0).offset(0x4L).getSigned() * 0x100 - MEMORY.ref(4, v0_0).offset(0x8L).get()) / s5);
      s2_0 = s2_0 + 0x8L;
    }

    //LAB_80109f90
    return 0;
  }

  @Method(0x80110030L)
  public static VECTOR FUN_80110030(final int scriptIndex) {
    final BattleScriptDataBase a0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(a0.magic_00.get() == BattleScriptDataBase.EM__) {
      return ((EffectManagerData6c)a0)._10.vec_04;
    }

    //LAB_8011006c
    return ((BattleObject27c)a0)._148.coord2_14.coord.transfer;
  }

  @Method(0x80110074L)
  public static SVECTOR FUN_80110074(final long a0) {
    final BattleScriptDataBase data = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);
    return data.magic_00.get() != BattleScriptDataBase.EM__ ? ((BattleObject27c)data)._148.coord2Param_64.rotate : ((EffectManagerData6c)data)._10.svec_10;
  }

  @Method(0x801100b8L)
  public static void FUN_801100b8(final int scriptIndex, final Ref<SVECTOR> a1, final Ref<VECTOR> a2) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
    final BattleScriptDataBase a3 = state.innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(a3.magic_00.get() == BattleScriptDataBase.EM__) {
      final EffectManagerData6c a3_0 = state.innerStruct_00.derefAs(EffectManagerData6c.class);
      a2.set(a3_0._10.vec_04);
      a1.set(a3_0._10.svec_10);
      return;
    }

    //LAB_801100fc
    final BattleObject27c a3_0 = state.innerStruct_00.derefAs(BattleObject27c.class);
    a2.set(a3_0._148.coord2_14.coord.transfer);
    a1.set(a3_0._148.coord2Param_64.rotate);
  }

  @Method(0x80110120L)
  public static SVECTOR FUN_80110120(final SVECTOR a0, @Nullable VECTOR a1, final VECTOR a2) {
    if(a1 == null) {
      a1 = new VECTOR();
    }

    //LAB_8011014c
    final VECTOR sp0x10 = new VECTOR().set(a2).sub(a1);
    a0.setZ((short)0);
    a0.setY((short)ratan2(sp0x10.getX(), sp0x10.getZ()));

    short s1 = (short)(rcos(-a0.getY()) * sp0x10.getZ() - rsin(-a0.getY()) * sp0x10.getX());
    if(s1 < 0) {
      s1 += 0xfff;
    }

    //LAB_801101f4
    a0.setX((short)ratan2(-sp0x10.getY(), s1 >> 12));
    return a0;
  }

  @Method(0x80110228L)
  public static SVECTOR FUN_80110228(final SVECTOR s2, @Nullable VECTOR a3, final VECTOR a2) {
    if(a3 == null) {
      a3 = new VECTOR();
    }

    //LAB_80110258
    final VECTOR sp0x10 = new VECTOR().set(a2).sub(a3).negate();
    final SVECTOR sp0x30 = new SVECTOR();
    sp0x30.setY((short)ratan2(sp0x10.getX(), sp0x10.getZ()));
    final long s1 = rcos(-sp0x30.getY()) * sp0x10.getZ() - rsin(-sp0x30.getY()) * sp0x10.getX();

    //LAB_80110308
    sp0x30.setX((short)ratan2(-sp0x10.getY(), (int)s1 / 0x1000));

    final MATRIX sp0x38 = new MATRIX();
    RotMatrix_80040010(sp0x30, sp0x38);
    FUN_800de544(s2, sp0x38);

    return s2;
  }

  @Method(0x8011035cL)
  public static long FUN_8011035c(final int scriptIndex1, final int scriptIndex2, final VECTOR a2) {
    final VECTOR s0 = FUN_80110030(scriptIndex1);

    if(scriptIndex2 == -0x1L) {
      a2.set(s0);
    } else {
      //LAB_801103b8
      final Ref<SVECTOR> sp0x58 = new Ref<>();
      final Ref<VECTOR> sp0x5c = new Ref<>();
      FUN_801100b8(scriptIndex2, sp0x58, sp0x5c);

      final VECTOR sp0x18 = new VECTOR().set(s0).sub(sp0x5c.get());
      final MATRIX sp0x38 = new MATRIX();
      RotMatrix_8003faf0(sp0x58.get(), sp0x38);

      final VECTOR sp0x28 = new VECTOR();
      FUN_80040ec0(sp0x38, sp0x18, sp0x28);
      a2.set(sp0x28);
    }

    //LAB_80110450
    return scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.getPointer(); //TODO
  }

  @Method(0x80110488L)
  public static long FUN_80110488(final int scriptIndex1, final int scriptIndex2, final VECTOR s1) {
    final MATRIX sp0x10 = new MATRIX();
    FUN_800e8594(sp0x10, scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(EffectManagerData6c.class));

    if(scriptIndex2 == -0x1L) {
      s1.set(sp0x10.transfer);
    } else {
      //LAB_80110500
      final MATRIX sp0x30 = new MATRIX();
      FUN_800e8594(sp0x30, scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(EffectManagerData6c.class));
      sp0x10.transfer.sub(sp0x30.transfer);

      final VECTOR sp0x50 = new VECTOR();
      FUN_80040ec0(sp0x30, sp0x10.transfer, sp0x50);
      s1.set(sp0x50);
    }

    //LAB_80110594
    return scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.getPointer();
  }

  @Method(0x801105ccL)
  public static void FUN_801105cc(final VECTOR a0, final int scriptIndex, final VECTOR a2) {
    final Ref<SVECTOR> sp0x30 = new Ref<>();
    final Ref<VECTOR> sp0x34 = new Ref<>();
    FUN_801100b8(scriptIndex, sp0x30, sp0x34);

    final MATRIX sp0x10 = new MATRIX();
    RotMatrix_8003faf0(sp0x30.get(), sp0x10);

    a0
      .set(ApplyMatrixLV(sp0x10, a2))
      .add(sp0x34.get());
  }

  @Method(0x8011066cL)
  public static BattleScriptDataBase FUN_8011066c(final int scriptIndex1, final int scriptIndex2, final VECTOR a2) {
    final BattleScriptDataBase data = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(data.magic_00.get() == BattleScriptDataBase.EM__ && (((EffectManagerData6c)data)._04.get() & 0x2L) != 0) {
      FUN_800e8d04((EffectManagerData6c)data, 0x1L);
    }

    //LAB_801106dc
    final VECTOR a0_0 = FUN_80110030(scriptIndex1);
    if(scriptIndex2 == -1) {
      a0_0.set(a2);
    } else {
      //LAB_80110718
      FUN_801105cc(a0_0, scriptIndex2, a2);
    }

    //LAB_80110720
    return data;
  }

  @Method(0x80110740L)
  public static long FUN_80110740(final EffectManagerData6c s2, final BttlScriptData6cSub34 s1) {
    s1._18.add(s1._24);
    s1._0c.add(s1._18);

    if(s1.scriptIndex_30.get() == -1) {
      s2._10.vec_04.set(s1._0c).div(0x100);
    } else {
      //LAB_80110814
      final Ref<SVECTOR> sp0x40 = new Ref<>();
      final Ref<VECTOR> sp0x44 = new Ref<>();
      FUN_801100b8(s1.scriptIndex_30.get(), sp0x40, sp0x44);

      final MATRIX sp0x10 = new MATRIX();
      RotMatrix_8003faf0(sp0x40.get(), sp0x10);

      final VECTOR sp0x30 = new VECTOR().set(s1._0c).div(0x100);
      s2._10.vec_04.set(ApplyMatrixLV(sp0x10, sp0x30)).add(sp0x44.get());
    }

    //LAB_801108bc
    if(s1._32.get() == -1) {
      return 0x1L;
    }

    s1._32.decr();

    if(s1._32.get() > 0) {
      //LAB_801108e0
      return 0x1L;
    }

    //LAB_801108e4
    return 0;
  }

  @Method(0x801108fcL)
  public static BttlScriptData6cSub34 FUN_801108fc(final long a0, final long a1, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    final EffectManagerData6c s0 = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    if((s0._04.get() & 0x2L) != 0) {
      FUN_800e8d04(s0, 0x1L);
    }

    //LAB_80110980
    final BttlScriptData6cSub34 s2 = FUN_800e8dd4(s0, 0x1L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    s2._0c.set(s0._10.vec_04.getX() << 8, s0._10.vec_04.getY() << 8, s0._10.vec_04.getZ() << 8);
    s2.scriptIndex_30.set(-1);
    s2._32.set((short)-1);

    final int transformedX1;
    final int transformedY1;
    final int transformedZ1;
    final int transformedX2;
    final int transformedY2;
    final int transformedZ2;
    if((int)a1 != -0x1L) {
      final MATRIX rotation = new MATRIX();
      RotMatrix_8003faf0(FUN_80110074(a1), rotation);
      final VECTOR sp0x38 = new VECTOR().set(x1, y1, z1);
      final VECTOR sp0x48 = new VECTOR();
      sp0x48.set(ApplyMatrixLV(rotation, sp0x38));
      transformedX1 = sp0x48.getX();
      transformedY1 = sp0x48.getY();
      transformedZ1 = sp0x48.getZ();
      sp0x38.set(x2, y2, z2);
      sp0x48.set(ApplyMatrixLV(rotation, sp0x38));
      transformedX2 = sp0x48.getX();
      transformedY2 = sp0x48.getY();
      transformedZ2 = sp0x48.getZ();
    } else {
      transformedX1 = x1;
      transformedY1 = y1;
      transformedZ1 = z1;
      transformedX2 = x2;
      transformedY2 = y2;
      transformedZ2 = z2;
    }

    //LAB_80110a5c
    s2._18.set(transformedX1, transformedY1, transformedZ1);
    s2._24.set(transformedX2, transformedY2, transformedZ2);
    return s2;
  }

  @Method(0x80110aa8L)
  public static BttlScriptData6cSub34 FUN_80110aa8(final long a0, final int a1, final int a2, final int a3, final int x, final int y, final int z) {
    if(a3 < 0) {
      return null;
    }

    //LAB_80110afc
    final EffectManagerData6c s2 = scriptStatePtrArr_800bc1c0.get(a1).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    if((s2._04.get() & 0x2L) != 0) {
      FUN_800e8d04(s2, 0x1L);
    }

    final VECTOR sp0x18 = new VECTOR();

    //LAB_80110b38
    final BttlScriptData6cSub34 s0 = FUN_800e8dd4(s2, 0x1L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    if(a2 == -1) {
      final VECTOR v0 = FUN_80110030(a1);
      sp0x18.set(x, y, z).sub(v0);
    } else {
      //LAB_80110b9c
      final VECTOR s1 = FUN_80110030(a1);

      if(a0 == 0) {
        //LAB_80110bc0
        final VECTOR v0 = FUN_80110030(a2);
        sp0x18.set(x, y, z).add(v0).sub(s1);
      } else if(a0 == 0x1L) {
        //LAB_80110c0c
        final VECTOR sp0x28 = new VECTOR().set(x, y, z);
        FUN_801105cc(sp0x18, a2, sp0x28);
        sp0x18.sub(s1);
      }
    }

    //LAB_80110c6c
    s0.scriptIndex_30.set(-1);
    s0._32.set((short)a3);
    s0._0c.set(s2._10.vec_04).mul(0x100);
    s0._18.set(sp0x18).mul(0x100).div(a3);
    s0._24.set(0, 0, 0);

    //LAB_80110d04
    return s0;
  }

  @Method(0x80110d34L)
  public static BttlScriptData6cSub34 FUN_80110d34(final int a0, final int scriptIndex1, final int scriptIndex2, final int a3, final int x, final int y, final int z) {
    final EffectManagerData6c s1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    if((s1._04.get() & 0x2L) != 0) {
      FUN_800e8d04(s1, 1);
    }

    //LAB_80110db8
    final BttlScriptData6cSub34 s0 = FUN_800e8dd4(s1, 1, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    s0._0c.setX(s1._10.vec_04.getX() << 8);
    s0._0c.setY(s1._10.vec_04.getY() << 8);
    s0._0c.setZ(s1._10.vec_04.getZ() << 8);
    s0.scriptIndex_30.set(-1);

    if(a3 <= 0) {
      s0._32.set((short)-1);
      s0._18.set(0, 0, 0);
    } else {
      //LAB_80110e30
      final VECTOR v1 = FUN_80110030(scriptIndex1);
      final VECTOR sp0x18 = new VECTOR();

      if(scriptIndex2 == -1) {
        sp0x18.set(x, y, z).sub(v1);
        //LAB_80110e70
      } else if(a0 == 0) {
        //LAB_80110e94
        final VECTOR v2 = FUN_80110030(scriptIndex2);
        sp0x18.set(x, y, z).add(v2).sub(v1);
      } else if(a0 == 1) {
        //LAB_80110ee0
        final VECTOR sp0x28 = new VECTOR().set(x, y, z);
        FUN_801105cc(sp0x18, scriptIndex2, sp0x28);
        sp0x18.sub(v1);
      }

      //LAB_80110f38
      final VECTOR sp0x28 = new VECTOR();
      FUN_80040e10(sp0x18, sp0x28);

      s0._32.set((short)((SquareRoot0(sp0x28.getX() + sp0x28.getY() + sp0x28.getZ()) << 8) / a3));
      if(s0._32.get() == 0) {
        s0._32.set((short)1);
      }

      //LAB_80110f80
      s0._18.setX((sp0x18.getX() << 8) / s0._32.get());
      s0._18.setY((sp0x18.getY() << 8) / s0._32.get());
      s0._18.setZ((sp0x18.getZ() << 8) / s0._32.get());
    }

    //LAB_80110fec
    s0._24.set(0, 0, 0);
    return s0;
  }

  @Method(0x8011102cL)
  public static BttlScriptData6cSub34 FUN_8011102c(final int scriptIndex1, final int scriptIndex2) {
    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    if((manager._04.get() & 0x2L) != 0) {
      FUN_800e8d04(manager, 1);
    }

    //LAB_80111084
    final BttlScriptData6cSub34 effect = FUN_800e8dd4(manager, 1, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34, BttlScriptData6cSub34::new);
    final VECTOR v1 = FUN_80110030(scriptIndex1);
    final VECTOR v2 = FUN_80110030(scriptIndex2);
    final VECTOR sp0x18 = new VECTOR().set(v1).sub(v2);
    effect._0c.set(sp0x18);
    effect.scriptIndex_30.set(scriptIndex2);
    effect._32.set((short)-1);
    effect._18.set(0, 0, 0);
    effect._24.set(0, 0, 0);
    return effect;
  }

  @Method(0x801115ecL)
  public static long FUN_801115ec(final RunningScript s0) {
    final VECTOR sp0x10 = new VECTOR();
    FUN_8011035c(s0.params_20.get(0).deref().get(), s0.params_20.get(1).deref().get(), sp0x10);
    s0.params_20.get(2).deref().set(sp0x10.getX());
    s0.params_20.get(3).deref().set(sp0x10.getY());
    s0.params_20.get(4).deref().set(sp0x10.getZ());
    return 0;
  }

  @Method(0x80111658L)
  public static long FUN_80111658(final RunningScript s0) {
    final VECTOR sp0x10 = new VECTOR();
    FUN_80110488(s0.params_20.get(0).deref().get(), s0.params_20.get(1).deref().get(), sp0x10);
    s0.params_20.get(2).deref().set(sp0x10.getX());
    s0.params_20.get(3).deref().set(sp0x10.getY());
    s0.params_20.get(4).deref().set(sp0x10.getZ());
    return 0;
  }

  @Method(0x801116c4L)
  public static MATRIX FUN_801116c4(final MATRIX a0, final int scriptIndex, final int a2) {
    final long v0;
    long v1;
    final long a3;
    final VECTOR sp0x30 = new VECTOR();
    final VECTOR sp0x40 = new VECTOR();
    final SVECTOR sp0x50 = new SVECTOR();
    final MATRIX sp0x58 = new MATRIX();
    final BattleScriptDataBase s0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);
    if(s0.magic_00.get() == BattleScriptDataBase.EM__) {
      final EffectManagerData6c effects = (EffectManagerData6c)s0;

      //LAB_8011172c
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, effects);
      v1 = effects._04.get() & 0xff00_0000L;

      //LAB_80111768
      if(v1 == 0x100_0000L || v1 == 0x200_0000L) {
        //LAB_80111998
        final BttlScriptData6cSub13c struct13c = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.derefAs(BttlScriptData6cSub13c.class);
        final BigStruct struct = struct13c._134.deref();
        struct.coord2_14.flg.set(0);
        struct.coord2_14.coord.set(sp0x10);

        //LAB_80111a0c
        final GsCOORDINATE2 coord2 = struct.coord2ArrPtr_04.deref().get(a2);
        GsGetLw(coord2, a0);
        coord2.flg.set(0);
      } else if(v1 == 0) {
        //LAB_80111778
        final long a2_0 = effects._44.getPointer(); //TODO
        if((MEMORY.ref(4, a2_0).offset(0x0L).get() & 0x7L) == 0) {
          v1 = MEMORY.ref(4, a2_0).offset(0xcL).get();

          //LAB_801117ac
          long a0_0 = Math.max(0, effects._10._24.get()) % (MEMORY.ref(4, a2_0).offset(0x8L).get() * 2);
          final long a1_0 = a0_0 / 2;
          a3 = v1 + MEMORY.ref(4, v1).offset(0x10L).offset(a2 * 0xcL).get();
          v0 = a3 + a1_0 * 0x14L;
          sp0x50.set((SVECTOR)MEMORY.ref(2, v0).offset(0xcL).cast(SVECTOR::new));
          sp0x30.set((SVECTOR)MEMORY.ref(2, v0).offset(0x6L).cast(SVECTOR::new));
          sp0x40.set((SVECTOR)MEMORY.ref(2, v0).offset(0x0L).cast(SVECTOR::new));

          if((a0_0 & 0x1L) != 0) {
            v1 = a1_0 + 0x1L;

            if(v1 == MEMORY.ref(4, a2_0).offset(0x8L).get()) {
              v1 = 0;
            }

            //LAB_8011188c
            a0_0 = a3 + v1 * 0x14L;
            sp0x30.add((SVECTOR)MEMORY.ref(2, a0_0).offset(0x6L).cast(SVECTOR::new)).div(2);
            sp0x40.add((SVECTOR)MEMORY.ref(2, a0_0).offset(0x0L).cast(SVECTOR::new)).div(2);
          }

          //LAB_80111958
          RotMatrix_80040010(sp0x50, sp0x58);
          TransMatrix(sp0x58, sp0x30);
          ScaleMatrixL(sp0x58, sp0x40);
          FUN_8003ec90(sp0x10, sp0x58, a0);
        }
      }
    } else {
      final BigStruct struct = ((BattleObject27c)s0)._148;
      FUN_800214bc(struct);
      final GsCOORDINATE2 coord2 = struct.coord2ArrPtr_04.deref().get(a2);
      GsGetLw(coord2, a0);
      coord2.flg.set(0);
    }

    //LAB_80111a3c
    return a0;
  }

  @Method(0x80111a58L)
  public static long FUN_80111a58(final RunningScript a0) {
    final int a2 = a0.params_20.get(1).deref().get();
    if(a2 == -1) {
      //LAB_80111acc
      return FUN_80111658(a0);
    }

    final MATRIX sp0x20 = new MATRIX();
    FUN_801116c4(sp0x20, a0.params_20.get(0).deref().get(), a2);
    a0.params_20.get(2).deref().set(sp0x20.transfer.getX());
    a0.params_20.get(3).deref().set(sp0x20.transfer.getY());
    a0.params_20.get(4).deref().set(sp0x20.transfer.getZ());

    //LAB_80111ad4
    return 0;
  }

  @Method(0x80111ae4L)
  public static long FUN_80111ae4(final RunningScript a0) {
    final VECTOR sp0x10 = new VECTOR().set(a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get());
    FUN_8011066c(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), sp0x10);
    return 0;
  }

  @Method(0x80111b60L)
  public static long FUN_80111b60(final RunningScript a0) {
    final VECTOR v0 = FUN_80110030(a0.params_20.get(0).deref().get());
    final DVECTOR sp0x10 = new DVECTOR();
    a0.params_20.get(3).deref().set(FUN_800e7dbc(sp0x10, v0));
    a0.params_20.get(1).deref().set(sp0x10.getX());
    a0.params_20.get(2).deref().set(sp0x10.getY());
    a0.params_20.get(3).deref().shl(2);
    return 0;
  }

  @Method(0x80111be8L)
  public static long FUN_80111be8(final RunningScript a0) {
    final EffectManagerData6c data = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    return data._04.get() & 0x2L;
  }

  @Method(0x80111c2cL)
  public static long FUN_80111c2c(final RunningScript a0) {
    FUN_801108fc(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x80111cc4L)
  public static long FUN_80111cc4(final RunningScript a0) {
    final int a1 = a0.params_20.get(0).deref().get();
    final int s0 = a0.params_20.get(1).deref().get();
    int s4 = a0.params_20.get(2).deref().get();
    int s6 = a0.params_20.get(3).deref().get();
    int s3 = a0.params_20.get(4).deref().get();
    int s5 = a0.params_20.get(5).deref().get();
    int s7 = a0.params_20.get(6).deref().get();
    int fp = a0.params_20.get(7).deref().get();

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(a1).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    if((manager._04.get() & 0x2L) == 0) {
      FUN_801108fc(a1, s0, s4, s6, s3, s5, s7, fp);
    } else {
      //LAB_80111dac
      final BttlScriptData6cSub34 s2 = (BttlScriptData6cSub34)FUN_800e8c84(manager, 1);

      if(s2._06.get() == 0) {
        if(s0 != -1) {
          final MATRIX sp0x20 = new MATRIX();
          RotMatrix_8003faf0(FUN_80110074(s0), sp0x20);

          VECTOR sp0x50 = ApplyMatrixLV(sp0x20, new VECTOR().set(s4, s6, s3));
          s4 = sp0x50.getX();
          s6 = sp0x50.getY();
          s3 = sp0x50.getZ();

          sp0x50 = ApplyMatrixLV(sp0x20, new VECTOR().set(s5, s7, fp));
          s5 = sp0x50.getX();
          s7 = sp0x50.getY();
          fp = sp0x50.getZ();
        }

        //LAB_80111e40
        s2._18.x.add(s4);
        s2._18.y.add(s6);
        s2._18.z.add(s3);
        s2._24.x.add(s5);
        s2._24.y.add(s7);
        s2._24.z.add(fp);
      }
    }

    //LAB_80111ea4
    return 0;
  }

  @Method(0x80111ed4L)
  public static long FUN_80111ed4(final RunningScript a0) {
    final int scriptIndex = a0.params_20.get(0).deref().get();
    final EffectManagerData6c data = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    if((data._04.get() & 0x2L) != 0) {
      final BttlScriptData6cSub34 s3 = (BttlScriptData6cSub34)FUN_800e8c84(data, 0x1L);

      if(s3._32.get() != -1 && s3._06.get() == 0) {
        final VECTOR v0 = FUN_80110030(scriptIndex);
        int s0 = s3._32.get();

        final VECTOR sp0x10 = new VECTOR().set(
          (s0 * s3._24.getX() / 2 + s3._18.getX()) * s0 + s3._0c.getX() >> 8,
          (s0 * s3._24.getY() / 2 + s3._18.getY()) * s0 + s3._0c.getY() >> 8,
          (s0 * s3._24.getZ() / 2 + s3._18.getZ()) * s0 + s3._0c.getZ() >> 8
        );

        final SVECTOR sp0x20 = new SVECTOR();
        FUN_80110120(sp0x20, v0, sp0x10);

        final MATRIX sp0x28 = new MATRIX();
        RotMatrix_80040010(sp0x20, sp0x28);

        final VECTOR sp0x48 = new VECTOR().set(
          a0.params_20.get(2).deref().get(),
          a0.params_20.get(3).deref().get(),
          a0.params_20.get(4).deref().get()
        );

        final VECTOR sp0x58 = ApplyMatrixLV(sp0x28, sp0x48);
        s0++;
        s3._18.x.sub(sp0x58.getX() * s0 / 2);
        s3._18.y.sub(sp0x58.getY() * s0 / 2);
        s3._18.z.sub(sp0x58.getZ() * s0 / 2);
        s3._24.add(sp0x58);
      }
    }

    //LAB_8011215c
    return 0;
  }

  @Method(0x80112184L)
  public static long FUN_80112184(final RunningScript a0) {
    FUN_80110aa8(0, a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get());
    return 0;
  }

  @Method(0x801121fcL)
  public static long FUN_801121fc(final RunningScript a0) {
    FUN_80110aa8(0x1L, a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get());
    return 0;
  }

  @Method(0x80112274L)
  public static long FUN_80112274(final RunningScript a0) {
    FUN_80110d34(0, a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get());
    return 0;
  }

  @Method(0x801122ecL)
  public static long FUN_801122ec(final RunningScript a0) {
    FUN_80110d34(1, a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get());
    return 0;
  }

  @Method(0x80112364L)
  public static long FUN_80112364(final RunningScript a0) {
    FUN_8011102c(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x80112398L)
  public static long FUN_80112398(final RunningScript s0) {
    final EffectManagerData6c a0 = scriptStatePtrArr_800bc1c0.get(s0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final VECTOR sp0x10 = new VECTOR();

    if((a0._04.get() & 0x2L) != 0) {
      final BttlScriptData6cSub34 v1 = (BttlScriptData6cSub34)FUN_800e8c84(a0, 1);

      if(v1._06.get() == 0) {
        sp0x10.set(v1._18);
      }
    }

    //LAB_80112430
    s0.params_20.get(2).deref().set(sp0x10.getX());
    s0.params_20.get(3).deref().set(sp0x10.getY());
    s0.params_20.get(4).deref().set(sp0x10.getZ());
    return 0;
  }

  @Method(0x80112474L)
  public static void FUN_80112474(final int a0, final int a1, final SVECTOR a2) {
    final SVECTOR s0 = FUN_80110074(a0);

    if(a1 == -1) {
      a2.set(s0);
    } else {
      //LAB_801124c8
      a2.set(s0).sub(FUN_80110074(a1)).and(0xfff);
    }

    //LAB_80112518
  }

  @Method(0x80112530L)
  public static long FUN_80112530(final long a0, final long a1, final SVECTOR a2) {
    final EffectManagerData6c data = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    if(data.magic_00.get() == BattleScriptDataBase.EM__ && (data._04.get() & 0x4L) != 0) {
      FUN_800e8d04(data, 0x2L);
    }

    //LAB_8011259c
    final SVECTOR s0 = FUN_80110074(a0).set(a2);
    if((int)a1 != -1) {
      //LAB_801125d8
      s0.add(FUN_80110074(a1));
    }

    //LAB_8011261c
    return 0;
  }

  @Method(0x80112638L)
  public static long FUN_80112638(final EffectManagerData6c a0, final BttlScriptData6cSub34 a1) {
    a1._18.add(a1._24);
    a1._0c.add(a1._18);
    a0._10.svec_10.set(a1._0c);

    if(a1._32.get() == -1) {
      return 0x1L;
    }

    a1._32.decr();

    if(a1._32.get() > 0) {
      //LAB_801126f8
      return 0x1L;
    }

    //LAB_801126fc
    return 0;
  }

  @Method(0x80112704L)
  public static long FUN_80112704(final RunningScript a0) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_80112474(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), sp0x10);
    a0.params_20.get(2).deref().set(sp0x10.getX());
    a0.params_20.get(3).deref().set(sp0x10.getY());
    a0.params_20.get(4).deref().set(sp0x10.getZ());
    return 0;
  }

  @Method(0x80112770L)
  public static long FUN_80112770(final RunningScript a0) {
    FUN_80112530(
      a0.params_20.get(0).deref().get(),
      a0.params_20.get(1).deref().get(),
      new SVECTOR().set(
        (short)a0.params_20.get(2).deref().get(),
        (short)a0.params_20.get(3).deref().get(),
        (short)a0.params_20.get(4).deref().get()
      )
    );

    return 0;
  }

  @Method(0x80112900L)
  public static long FUN_80112900(final RunningScript s0) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_80110228(sp0x10, FUN_80110030(s0.params_20.get(0).deref().get()), FUN_80110030(s0.params_20.get(1).deref().get()));

    // XZY is the correct order
    s0.params_20.get(2).deref().set(sp0x10.getX());
    s0.params_20.get(3).deref().set(sp0x10.getZ());
    s0.params_20.get(4).deref().set(sp0x10.getY());
    return 0;
  }

  @Method(0x8011299cL)
  public static long FUN_8011299c(final RunningScript a0) {
    final int s0 = a0.params_20.get(1).deref().get();

    final VECTOR sp0x10 = new VECTOR().set(a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get());
    final Ref<SVECTOR> sp0x50 = new Ref<>(new SVECTOR());
    final Ref<VECTOR> sp0x54 = new Ref<>(new VECTOR());

    FUN_801100b8(a0.params_20.get(0).deref().get(), sp0x50, sp0x54);

    if(s0 != -0x1L) {
      final Ref<SVECTOR> sp0x58 = new Ref<>(new SVECTOR());
      final Ref<VECTOR> sp0x5c = new Ref<>(new VECTOR());

      FUN_801100b8(s0, sp0x58, sp0x5c);

      final MATRIX sp0x20 = new MATRIX();
      RotMatrix_8003faf0(sp0x58.get(), sp0x20);
      sp0x10.set(ApplyMatrixLV(sp0x20, sp0x10)).add(sp0x5c.get());
    }

    //LAB_80112a80
    FUN_80110228(sp0x50.get(), sp0x54.get(), sp0x10);
    return 0;
  }

  @Method(0x80112aa4L)
  public static long FUN_80112aa4(final RunningScript a0) {
    final EffectManagerData6c s0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    if((s0._04.get() & 0x4L) != 0) {
      FUN_800e8d04(s0, 0x2L);
    }

    //LAB_80112b58
    final BttlScriptData6cSub34 v0 = FUN_800e8dd4(s0, 0x2L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80112638", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    v0.scriptIndex_30.set(-1);
    v0._32.set((short)-1);
    v0._0c.set(s0._10.svec_10);
    v0._18.setX(a0.params_20.get(2).deref().get());
    v0._18.setY(a0.params_20.get(3).deref().get());
    v0._18.setZ(a0.params_20.get(4).deref().get());
    v0._24.setX(a0.params_20.get(5).deref().get());
    v0._24.setY(a0.params_20.get(6).deref().get());
    v0._24.setZ(a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x80112bf0L)
  public static long FUN_80112bf0(final RunningScript a0) {
    final int s0 = a0.params_20.get(0).deref().get();
    final int s4 = a0.params_20.get(1).deref().get();
    final int s2 = a0.params_20.get(2).deref().get();
    final int s5 = a0.params_20.get(3).deref().get();
    final int s6 = a0.params_20.get(4).deref().get();
    final int s7 = a0.params_20.get(5).deref().get();

    if(s2 >= 0) {
      final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(s0).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
      final BttlScriptData6cSub34 effect = FUN_800e8dd4(manager, 2, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80112638", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34, BttlScriptData6cSub34::new);

      final SVECTOR v1 = FUN_80110074(s0);
      final int sp18;
      final int sp1c;
      final int sp20;
      if(s4 == -1) {
        sp18 = s5 - v1.getX();
        sp1c = s6 - v1.getY();
        sp20 = s7 - v1.getZ();
      } else {
        //LAB_80112ce4
        final SVECTOR v2 = FUN_80110074(s4);
        sp18 = v2.getX() + s5 - v1.getX();
        sp1c = v2.getY() + s6 - v1.getY();
        sp20 = v2.getZ() + s7 - v1.getZ();
      }

      //LAB_80112d48
      effect.scriptIndex_30.set(-1);
      effect._32.set((short)s2);
      effect._0c.set(manager._10.svec_10);
      effect._0c.set(manager._10.svec_10);
      effect._0c.set(manager._10.svec_10);
      effect._18.setX(sp18 / s2);
      effect._18.setY(sp1c / s2);
      effect._18.setZ(sp20 / s2);
      effect._24.set(0, 0, 0);
    }

    //LAB_80112dd0
    return 0;
  }

  @Method(0x80113624L)
  public static SVECTOR FUN_80113624(final int scriptIndex, final int a1, final SVECTOR a2) {
    ScriptState<BattleScriptDataBase> a3 = scriptStatePtrArr_800bc1c0.get(scriptIndex).derefAs(ScriptState.classFor(BattleScriptDataBase.class));
    BattleScriptDataBase t0 = a3.innerStruct_00.deref();

    final SVECTOR t1;
    if(t0.magic_00.get() == BattleScriptDataBase.EM__) {
      t1 = ((EffectManagerData6c)t0)._10.svec_16;
    } else {
      //LAB_80113660
      t1 = new SVECTOR().set(((BattleObject27c)t0)._244);
    }

    //LAB_801136a0
    if(a1 == -1) {
      a2.set(t1);
    } else {
      //LAB_801136d0
      a3 = scriptStatePtrArr_800bc1c0.get(scriptIndex).derefAs(ScriptState.classFor(BattleScriptDataBase.class));
      t0 = a3.innerStruct_00.deref();

      final SVECTOR svec;
      if(t0.magic_00.get() == BattleScriptDataBase.EM__) {
        svec = ((EffectManagerData6c)t0)._10.svec_16;
      } else {
        //LAB_80113708
        svec = new SVECTOR().set(((BattleObject27c)t0)._244);
      }

      //LAB_80113744
      if(svec.getX() == 0) {
        svec.setX((short)1);
      }

      //LAB_80113758
      if(svec.getY() == 0) {
        svec.setY((short)1);
      }

      //LAB_8011376c
      if(svec.getZ() == 0) {
        svec.setZ((short)1);
      }

      //LAB_80113780
      a2.setX((short)((t1.getX() << 12) / svec.getX()));
      a2.setY((short)((t1.getY() << 12) / svec.getY()));
      a2.setZ((short)((t1.getZ() << 12) / svec.getZ()));
    }

    //LAB_801137ec
    return a2;
  }

  @Method(0x801137f8L)
  public static SVECTOR FUN_801137f8(final int scriptIndex1, final int scriptIndex2, final SVECTOR a2) {
    ScriptState<BattleScriptDataBase> a0_0 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleScriptDataBase.class));
    BattleScriptDataBase a3 = a0_0.innerStruct_00.deref();

    final SVECTOR t0;
    if(a3.magic_00.get() == BattleScriptDataBase.EM__) {
      t0 = ((EffectManagerData6c)a3)._10.svec_16;
    } else {
      //LAB_80113834
      t0 = new SVECTOR().set(((BattleObject27c)a3)._244);
    }

    //LAB_80113874
    if(scriptIndex2 == -1) {
      a2.set(t0);
    } else {
      //LAB_801138a4
      a0_0 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).derefAs(ScriptState.classFor(BattleScriptDataBase.class));
      a3 = a0_0.innerStruct_00.deref();

      final SVECTOR a0_1;
      if(a3.magic_00.get() == BattleScriptDataBase.EM__) {
        a0_1 = ((EffectManagerData6c)a3)._10.svec_16;
      } else {
        //LAB_801138dc
        a0_1 = new SVECTOR().set(((BattleObject27c)a3)._244);
      }

      //LAB_8011391c
      a2.set(t0).sub(a0_1);
    }

    //LAB_80113958
    return a2;
  }

  @Method(0x80113964L)
  public static long FUN_80113964(final RunningScript a0) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_80113624(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), sp0x10);
    a0.params_20.get(2).deref().set(sp0x10.getX());
    a0.params_20.get(3).deref().set(sp0x10.getY());
    a0.params_20.get(4).deref().set(sp0x10.getZ());
    return 0;
  }

  @Method(0x801139d0L)
  public static long FUN_801139d0(final RunningScript a0) {
    final int t1 = a0.params_20.get(0).deref().get();
    final int a1 = a0.params_20.get(1).deref().get();
    final short x = (short)a0.params_20.get(2).deref().get();
    final short y = (short)a0.params_20.get(3).deref().get();
    final short z = (short)a0.params_20.get(4).deref().get();
    final SVECTOR sp0x00 = new SVECTOR();
    if(a1 == -0x1L) {
      sp0x00.set(x, y, z);
    } else {
      //LAB_80113a28
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(a1).deref();
      final BattleScriptDataBase a1_0 = state.innerStruct_00.derefAs(BattleScriptDataBase.class);

      final SVECTOR v1;
      if(a1_0.magic_00.get() == BattleScriptDataBase.EM__) {
        v1 = ((EffectManagerData6c)a1_0)._10.svec_16;
      } else {
        //LAB_80113a64
        v1 = new SVECTOR().set(((BattleObject27c)a1_0)._244);
      }

      //LAB_80113aa0
      //LAB_80113abc
      //LAB_80113ae0
      //LAB_80113b04
      sp0x00.set((short)(x * v1.getX() >> 12), (short)(y * v1.getY() >> 12), (short)(z * v1.getZ() >> 12));
    }

    //LAB_80113b0c
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(t1).deref();
    final BattleScriptDataBase a0_0 = state.innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(a0_0.magic_00.get() == BattleScriptDataBase.EM__) {
      ((EffectManagerData6c)a0_0)._10.svec_16.set(sp0x00);
    } else {
      //LAB_80113b64
      ((BattleObject27c)a0_0)._148.scaleVector_fc.set(sp0x00);
    }

    //LAB_80113b94
    return 0;
  }

  @Method(0x80113ba0L)
  public static long FUN_80113ba0(final EffectManagerData6c data, final BttlScriptData6cSub34 sub) {
    sub._18.add(sub._24);
    sub._0c.add(sub._18);
    data._10.svec_16.set(sub._0c);

    if(sub._32.get() == -0x1L) {
      return 0x1L;
    }

    sub._32.decr();
    if(sub._32.get() > 0) {
      //LAB_80113c60
      return 0x1L;
    }

    //LAB_80113c64
    return 0;
  }

  @Method(0x80113c6cL)
  public static long FUN_80113c6c(final RunningScript a0) {
    final EffectManagerData6c s0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    if((s0._04.get() & 0x8L) != 0) {
      FUN_800e8d04(s0, 0x3L);
    }

    //LAB_80113d20
    final BttlScriptData6cSub34 v0 = FUN_800e8dd4(s0, 0x3L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80113ba0", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    v0.scriptIndex_30.set(-1);
    v0._32.set((short)-1);
    v0._0c.set(s0._10.svec_16);
    v0._18.set(a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    v0._24.set(a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get());
    return 0;
  }

  @Method(0x80113db8L)
  public static long FUN_80113db8(final long a0, final RunningScript a1) {
    final int s7 = a1.params_20.get(0).deref().get();
    final int s3 = a1.params_20.get(1).deref().get();
    final long s2 = a1.params_20.get(2).deref().get();
    final long s4 = a1.params_20.get(3).deref().get();
    final long s5 = a1.params_20.get(4).deref().get();
    final long s6 = a1.params_20.get(5).deref().get();

    if((int)s2 >= 0) {
      final EffectManagerData6c s1 = scriptStatePtrArr_800bc1c0.get(s7).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

      if((s1._04.get() & 0x8L) != 0) {
        FUN_800e8d04(s1, 0x3L);
      }

      //LAB_80113e70
      final BttlScriptData6cSub34 s0 = FUN_800e8dd4(s1, 0x3L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80113ba0", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);

      final VECTOR sp0x18 = new VECTOR().set((int)s4, (int)s5, (int)s6);
      if(a0 == 0) {
        //LAB_80113eac
        final SVECTOR sp0x28 = new SVECTOR();
        FUN_801137f8(s7, s3, sp0x28);
        sp0x18.sub(sp0x28);
      } else if(a0 == 0x1L) {
        //LAB_80113ee8
        if(s3 != -0x1L) {
          //LAB_80113f04
          final SVECTOR sp0x28 = new SVECTOR();
          FUN_80113624(s3, -1, sp0x28);

          //LAB_80113f2c
          //LAB_80113f50
          //LAB_80113f74
          sp0x18.mul(sp0x28).div(0x1000);
        }

        //LAB_80113f7c
        //LAB_80113fb4
        sp0x18.sub(s1._10.svec_16);
      } else {
        throw new RuntimeException("Invalid a0 " + a0);
      }

      //LAB_80113fc0
      s0.scriptIndex_30.set(-1);
      s0._32.set((short)s2);
      s0._0c.set(s1._10.svec_16);
      s0._18.set(sp0x18).div((int)s2);
      s0._24.set(0, 0, 0);
    }

    //LAB_8011403c
    return 0;
  }

  @Method(0x80114094L)
  public static long FUN_80114094(final RunningScript a0) {
    return FUN_80113db8(1, a0);
  }

  @Method(0x8011441cL)
  public static long FUN_8011441c(final int scriptIndex1, final int scriptIndex2, final SVECTOR a2) {
    final BattleScriptDataBase data1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);
    final SVECTOR svec1;
    if(data1.magic_00.get() != BattleScriptDataBase.EM__) {
      svec1 = _800fb94c;
    } else {
      svec1 = ((EffectManagerData6c)data1)._10.svec_1c;
    }

    //LAB_80114480
    if(scriptIndex2 == -1) {
      a2.set(svec1);
    } else {
      //LAB_801144b0
      final BattleScriptDataBase data2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);
      final SVECTOR svec2;
      if(data2.magic_00.get() == BattleScriptDataBase.EM__) {
        svec2 = ((EffectManagerData6c)data2)._10.svec_1c;
      } else {
        svec2 = _800fb94c;
      }

      //LAB_801144e4
      a2.set(svec1).sub(svec2);
    }

    //LAB_80114520
    return 0;
  }

  @Method(0x8011452cL)
  public static long FUN_8011452c(final RunningScript a0) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_8011441c(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), sp0x10);
    a0.params_20.get(2).deref().set(sp0x10.getX());
    a0.params_20.get(3).deref().set(sp0x10.getY());
    a0.params_20.get(4).deref().set(sp0x10.getZ());
    return 0;
  }

  @Method(0x80114598L)
  public static long FUN_80114598(final RunningScript a0) {
    BattleScriptDataBase a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    final SVECTOR a3;
    if(a1.magic_00.get() == BattleScriptDataBase.EM__) {
      a3 = ((EffectManagerData6c)a1)._10.svec_1c;
    } else {
      a3 = new SVECTOR().set(_800fb94c);
    }

    //LAB_80114614
    if(a0.params_20.get(1).deref().get() == -0x1L) {
      a3.setX((short)a0.params_20.get(2).deref().get());
      a3.setY((short)a0.params_20.get(3).deref().get());
      a3.setZ((short)a0.params_20.get(4).deref().get());
    } else {
      //LAB_80114668
      a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

      final SVECTOR a2;
      if(a1.magic_00.get() == BattleScriptDataBase.EM__) {
        a2 = ((EffectManagerData6c)a1)._10.svec_1c;
      } else {
        a2 = _800fb94c;
      }

      //LAB_8011469c
      a3.setX((short)(a0.params_20.get(2).deref().get() + a2.getX()));
      a3.setY((short)(a0.params_20.get(3).deref().get() + a2.getY()));
      a3.setZ((short)(a0.params_20.get(4).deref().get() + a2.getZ()));
    }

    //LAB_801146f0
    return 0;
  }

  @Method(0x801146fcL)
  public static long FUN_801146fc(final EffectManagerData6c a0, final BttlScriptData6cSub34 a1) {
    a1._18.add(a1._24);
    a1._0c.add(a1._18);
    a0._10.svec_1c.setX((short)(a1._0c.getX() >> 8 & 0xff));
    a0._10.svec_1c.setY((short)(a1._0c.getY() >> 8 & 0xff));
    a0._10.svec_1c.setZ((short)(a1._0c.getZ() >> 8 & 0xff));

    if(a1._32.get() != -1) {
      a1._32.decr();

      if(a1._32.get() <= 0) {
        return 0;
      }
    }

    //LAB_801147bc
    //LAB_801147c0
    return 0x1L;
  }

  @Method(0x80114920L)
  public static long FUN_80114920(final RunningScript a0) {
    final int scriptIndex1 = a0.params_20.get(0).deref().get();
    final int scriptIndex2 = a0.params_20.get(1).deref().get();
    final int scale = a0.params_20.get(2).deref().get();
    final VECTOR vec = new VECTOR().set(
      a0.params_20.get(3).deref().get(),
      a0.params_20.get(4).deref().get(),
      a0.params_20.get(5).deref().get()
    );

    if(scale >= 0) {
      final EffectManagerData6c s2 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
      if((s2._04.get() & 0x10L) != 0) {
        FUN_800e8d04(s2, 0x4L);
      }

      //LAB_801149d0
      final BttlScriptData6cSub34 s0 = FUN_800e8dd4(s2, 0x4L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801146fc", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
      final SVECTOR sp0x28 = new SVECTOR();
      FUN_8011441c(scriptIndex1, scriptIndex2, sp0x28);
      s0.scriptIndex_30.set(-1);
      s0._32.set((short)scale);
      s0._0c.set(s2._10.svec_1c).mul(0x100);
      s0._18.set(vec).sub(sp0x28).mul(0x100).div(scale);
      s0._24.set(0, 0, 0);
    }

    //LAB_80114ad0
    return 0;
  }

  @Method(0x80114e0cL)
  public static long FUN_80114e0c(final RunningScript a0) {
    final long v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); // TODO
    a0.params_20.get(2).deref().set((int)MEMORY.ref(4, v0).offset(0x34L).offset(a0.params_20.get(1).deref().get() * 0x4L).get());
    return 0;
  }

  @Method(0x80114eb4L)
  public static long FUN_80114eb4(final RunningScript script) {
    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(script.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final int a0 = script.params_20.get(1).deref().get() + 5;

    if((1L << a0 & manager._04.get()) != 0) {
      FUN_800e8d04(manager, (byte)a0);
    }

    //LAB_80114f24
    return 0;
  }

  @Method(0x80114e60L)
  public static long FUN_80114e60(final RunningScript a0) {
    final long v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, v0).offset(0x34L).offset(a0.params_20.get(1).deref().get() * 0x4L).setu(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x80114f3cL)
  public static void FUN_80114f3c(final int scriptIndex, final int a1, final int a2, final int a3) {
    final EffectManagerData6c s0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    if((1L << a1 + 5 & s0._04.get()) != 0) {
      FUN_800e8d04(s0, a1 + 5);
    }

    //LAB_80114fa8
    final BttlScriptData6cSub1c v0 = FUN_800e8dd4(s0, a1 + 0x5L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80114d98", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1cL, BttlScriptData6cSub1c::new);
    MEMORY.ref(4, v0.getAddress()).offset(0x0cL).setu(MEMORY.ref(4, s0._10._24.getAddress()).offset(a1 * 0x4L).get() << 8); //TODO
    MEMORY.ref(4, v0.getAddress()).offset(0x10L).setu(a2);
    MEMORY.ref(4, v0.getAddress()).offset(0x14L).setu(a3);
    MEMORY.ref(1, v0.getAddress()).offset(0x18L).setu(-0x1L);
    MEMORY.ref(2, v0.getAddress()).offset(0x1aL).setu(-0x1L);
  }

  @Method(0x80114d98L)
  public static long FUN_80114d98(final EffectManagerData6c a0, final BttlScriptData6cSub1c a1) {
    //TODO
    MEMORY.ref(4, a1.getAddress()).offset(0x10L).addu(MEMORY.ref(4, a1.getAddress()).offset(0x14L).get());
    MEMORY.ref(4, a1.getAddress()).offset(0xcL).addu(MEMORY.ref(4, a1.getAddress()).offset(0x10L).get());
    MEMORY.ref(4, a0._10._24.getAddress()).offset((MEMORY.ref(1, a1.getAddress()).offset(0x5L).getSigned() - 5) * 0x4L).setu(MEMORY.ref(4, a1.getAddress()).offset(0xcL).getSigned() >> 8);

    if(MEMORY.ref(2, a1.getAddress()).offset(0x1aL).getSigned() == -1) {
      return 1;
    }

    MEMORY.ref(2, a1.getAddress()).offset(0x1aL).subu(0x1L);
    if(MEMORY.ref(2, a1.getAddress()).offset(0x1aL).getSigned() > 0) {
      //LAB_80114e00
      return 1;
    }

    //LAB_80114e04
    return 0;
  }

  @Method(0x80115014L)
  public static long FUN_80115014(final RunningScript a0) {
    FUN_80114f3c(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x80115058L)
  public static long FUN_80115058(final RunningScript a0) {
    final int s1 = a0.params_20.get(1).deref().get();
    final int s3 = a0.params_20.get(2).deref().get();
    final int s2 = a0.params_20.get(3).deref().get();

    final EffectManagerData6c s0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    if((0x1L << s1 + 5 & s0._04.get()) != 0) {
      FUN_800e8d04(s0, s1 + 5);
    }

    //LAB_801150e8
    final long v0 = FUN_800e8dd4(s0, s1 + 0x5L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80114d98", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1cL, BttlScriptData6cSub1c::new).getAddress(); //TODO
    MEMORY.ref(1, v0).offset(0x18L).setu(-0x1L);
    MEMORY.ref(2, v0).offset(0x1aL).setu(s2);
    MEMORY.ref(4, v0).offset(0xcL).setu(MEMORY.ref(4, s0._10._24.getAddress()).offset(s1 * 0x4L).get() << 8); //TODO
    MEMORY.ref(4, v0).offset(0x10L).setu((int)(s3 * 0x100 - MEMORY.ref(4, v0).offset(0xcL).get()) / s2);
    MEMORY.ref(4, v0).offset(0x14L).setu(0);
    return 0;
  }

  @Method(0x80115288L)
  public static long FUN_80115288(final EffectManagerData6c a0, final BttlScriptData6cSub1c a1) {
    MEMORY.ref(2, a1.getAddress()).offset(0x1aL).subu(0x1L); //TODO

    //LAB_801152a8
    return MEMORY.ref(2, a1.getAddress()).offset(0x1aL).getSigned() > 0 ? 0x1L : 0x2L; //TODO
  }

  @Method(0x801152b0L)
  public static long FUN_801152b0(final RunningScript a0) {
    final BttlScriptData6cSub1c v0 = FUN_800e8dd4(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class), 0, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80115288", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1cL, BttlScriptData6cSub1c::new);
    MEMORY.ref(2, v0.getAddress()).offset(0x1aL).setu(a0.params_20.get(1).deref().get()); //TODO
    return 0;
  }

  @Method(0x80115324L)
  public static long FUN_80115324(final RunningScript a0) {
    final EffectManagerData6c effects = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    effects._10._00.and(0x7fff_ffffL).or(((a0.params_20.get(1).deref().get() ^ 0x1L) & 0x1L) << 31);
    return 0;
  }

  @Method(0x80115388L)
  public static long FUN_80115388(final RunningScript a0) {
    final EffectManagerData6c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    a1._10._00.and(0xbfff_ffffL).or(a0.params_20.get(1).deref().get() << 30);
    return 0;
  }

  @Method(0x801153e4L)
  public static long FUN_801153e4(final RunningScript a0) {
    final EffectManagerData6c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    a1._10._00.and(0xcfff_ffffL).or(a0.params_20.get(1).deref().get() << 28);
    return 0;
  }

  @Method(0x80115440L)
  public static long FUN_80115440(final RunningScript a0) {
    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    manager._10._00.and(0xfbff_ffffL).or(a0.params_20.get(1).deref().get() << 26);
    return 0;
  }

  @Method(0x8011549cL)
  public static long FUN_8011549c(final RunningScript a0) {
    final long a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, a1).offset(0x10L).and(0xffff_ffbfL).oru(a0.params_20.get(1).deref().get() << 6);
    return 0;
  }

  @Method(0x8011554cL)
  public static long FUN_8011554c(final RunningScript a0) {
    FUN_800e8d04(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class), (byte)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x801155a0L)
  public static long FUN_801155a0(final RunningScript a0) {
    final long v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    a0.params_20.get(1).deref().set((int)(1 << a0.params_20.get(2).deref().get() & MEMORY.ref(4, v1).offset(0x4L).get()));
    return 0;
  }

  @Method(0x801155f8L)
  public static long FUN_801155f8(final RunningScript a0) {
    return 0;
  }

  @Method(0x80115600L)
  public static long FUN_80115600(final RunningScript a0) {
    return 0;
  }

  @Method(0x80115608L)
  public static long FUN_80115608(final RunningScript a0) {
    final int scriptIndex = a0.params_20.get(0).deref().get();
    final int offset = a0.params_20.get(2).deref().get();
    final int mrgIndex = a0.params_20.get(1).deref().get();

    final ScriptFile file;
    if(mrgIndex == -1) {
      file = a0.scriptState_04.deref().scriptPtr_14.deref();
    } else {
      //LAB_80115654
      file = struct7cc_800c693c.deref().mrg_2c.deref().getFile(mrgIndex, ScriptFile::new);
    }

    //LAB_80115674
    loadScriptFile(scriptIndex, file, offset, "", 0); //TODO
    return 0;
  }

  @Method(0x80115690L)
  public static long FUN_80115690(final RunningScript a0) {
    final int s0 = a0.params_20.get(0).deref().get();
    loadScriptFile(s0, a0.scriptState_04.deref().scriptPtr_14.deref(), 0, "S_EFFE Script", 0); //TODO unknown size
    scriptStatePtrArr_800bc1c0.get(s0).deref().commandPtr_18.set(a0.params_20.get(1).deref());
    return 0;
  }

  @Method(0x801156f8L)
  public static long FUN_801156f8(final RunningScript a0) {
    final long v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(1, v0).offset(0xcL).setu(a0.params_20.get(1).deref().get());
    MEMORY.ref(1, v0).offset(0xdL).setu(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x8011574cL)
  public static long FUN_8011574c(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._10._22.get());
    return 0;
  }

  @Method(0x8011578cL)
  public static long FUN_8011578c(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._10._22.set((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x801157d0L)
  public static long FUN_801157d0(final RunningScript a0) {
    final int scriptIndex = a0.params_20.get(1).deref().get();
    final int coord2Index = a0.params_20.get(2).deref().get();

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    final MATRIX sp0x10 = new MATRIX();
    final MATRIX sp0x30 = new MATRIX();
    FUN_800e8594(sp0x10, manager);

    if(scriptIndex == -1) {
      sp0x30.set(sp0x10);
    } else {
      //LAB_8011588c
      final Memory.TemporaryReservation sp0x50tmp = MEMORY.temp(0x6c);
      final EffectManagerData6c sp0x50 = new EffectManagerData6c(sp0x50tmp.get());

      sp0x50._10.vec_04.set(0, 0, 0);
      sp0x50._10.svec_10.set((short)0, (short)0, (short)0);
      sp0x50._10.svec_16.set((short)0x1000, (short)0x1000, (short)0x1000);

      sp0x50.scriptIndex_0c.set(scriptIndex);
      sp0x50.coord2Index_0d.set(coord2Index);

      final MATRIX sp0xc0 = new MATRIX();
      FUN_800e8594(sp0xc0, sp0x50);

      final SVECTOR sp0xe0 = new SVECTOR();
      final SVECTOR sp0xe8 = new SVECTOR();
      FUN_800de618(sp0xe0, sp0xe8, sp0xc0);
      sp0xe0.negate();
      RotMatrix_80040010(sp0xe0, sp0xc0);

      final VECTOR sp0xf0 = new VECTOR().set(0x100_0000, 0x100_0000, 0x100_0000).div(sp0xe8);
      ScaleMatrixL(sp0xc0, sp0xf0);
      FUN_80021de4(sp0xc0, sp0x10, sp0x30);

      final VECTOR sp0x100 = new VECTOR().set(sp0x10.transfer).sub(sp0xc0.transfer);
      sp0x30.transfer.set(ApplyMatrixLV(sp0xc0, sp0x100));

      sp0x50tmp.release();
    }

    //LAB_801159cc
    FUN_800de618(manager._10.svec_10, manager._10.svec_16, sp0x30);
    manager._10.vec_04.set(sp0x30.transfer);
    manager.scriptIndex_0c.set(scriptIndex);
    manager.coord2Index_0d.set(coord2Index);
    return 0;
  }

  @Method(0x80115a28L)
  public static long FUN_80115a28(final RunningScript a0) {
    FUN_800e9178(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x80115a58L)
  public static long scriptPlayXaAudio(final RunningScript a0) {
    playXaAudio(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x80115a94L)
  public static long FUN_80115a94(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)_800bf0cf.get());
    return 0;
  }

  @Method(0x80115ab0L)
  public static long FUN_80115ab0(final RunningScript a0) {
    return (_800bf0cf.get() != a0.params_20.get(0).deref().get()) ? 2 : 0;
  }

  @Method(0x80115ad8L)
  public static long FUN_80115ad8(final RunningScript a0) {
    final BattleStruct7cc v1 = struct7cc_800c693c.deref();

    final long v0;
    if(a0.params_20.get(0).deref().get() >= 0) {
      //LAB_80115b08
      v0 = a0.params_20.get(0).deref().get() | v1._20.get();
    } else {
      v0 = a0.params_20.get(0).deref().get() & v1._20.get();
    }

    //LAB_80115b20
    v1._20.set(v0);
    return 0;
  }

  @Method(0x80115b2cL)
  public static void FUN_80115b2c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get(index).deref();
    final int s0 = v0.storage_44.get(8).get();
    final int s1 = v0.storage_44.get(9).get();

    if(s1 == s0) {
      deallocateScriptAndChildren(index);
    } else {
      //LAB_80115b80
      FUN_800ebb58(s0);

      //LAB_80115bd4
      if(s1 < s0) {
        v0.storage_44.get(8).decr();
      } else {
        //LAB_80115bb4
        v0.storage_44.get(8).incr();
      }
    }

    //LAB_80115bd8
  }

  @Method(0x80115bf0L)
  public static void FUN_80115bf0(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    FUN_800ebb58(scriptStatePtrArr_800bc1c0.get(index).deref().storage_44.get(9).get());
  }

  @Method(0x80115c2cL)
  public static void FUN_80115c2c(final int a0, final int a1) {
    final int scriptIndex = allocateEffectManager(struct7cc_800c693c.deref().scriptIndex_1c.get(), 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80115b2c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), null, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80115bf0", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), null);
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
    state.storage_44.get(8).set(a0);
    state.storage_44.get(9).set(a1);
  }

  @Method(0x80115cacL)
  public static long FUN_80115cac(final long a0) {
    final long sp10;
    final long sp14;
    final long sp18;
    if(currentStage_800c66a4.get() - 0x47L >= 0x8L) {
      //LAB_80115d14
      //LAB_80115d2c
      for(int i = 0; ; i++) {
        if(stageIndices_800fb064.offset(i).get() == 0xffL) {
          //LAB_80115cd8
          final SVECTOR v0 = struct7cc_800c693c.deref().svec_00;
          sp10 = v0.getX() & 0xffff;
          sp14 = v0.getY() & 0xffff;
          sp18 = v0.getZ() & 0xffff;
          break;
        }

        if(stageIndices_800fb064.offset(i).get() == currentStage_800c66a4.get()) {
          //LAB_80115d58
          final DVECTOR v0 = struct7cc_800c693c.deref().dvecs_08.get(i);
          sp10 = v0.getX() & 0xffff;
          sp14 = v0.getY() & 0xffff;
          sp18 = 0;
          break;
        }
      }

      //LAB_80115d84
      if(a0 == 0) {
        //LAB_80115dc0
        if((_800bda0c.deref()._5e4.get() & 0x8000L) != 0) {
          FUN_80115c2c(6, 16);
        }

        //LAB_80115de8
        _800bda0c.deref()._5e4.set(~(sp10 | sp14 | sp18) & _800bda0c.deref()._5e4.get());
        //LAB_80115da8
      } else if(a0 == 0x1L) {
        //LAB_80115e18
        _800bda0c.deref()._5e4.or(sp10);
      } else if(a0 == 0x2L) {
        //LAB_80115e34
        _800bda0c.deref()._5e4.or(sp14);
        if((_800bda0c.deref()._5e4.get() & 0x8000L) != 0) {
          FUN_80115c2c(16, 6);
        }
      } else if(a0 == 0x3L) {
        //LAB_80115e70
        //LAB_80115e8c
        _800bda0c.deref()._5e4.or(sp18);
      }
    }

    //LAB_80115e90
    //LAB_80115e94
    return 0;
  }

  @Method(0x80115ea4L)
  public static long FUN_80115ea4(final RunningScript a0) {
    FUN_80115cac(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x80115ed4L)
  public static long FUN_80115ed4(final RunningScript script) {
    long v0;
    final int a1;
    final int a3;
    final int t0;
    final int t1;
    final int a2 = script.params_20.get(0).deref().get();
    final int s0 = script.params_20.get(1).deref().get();
    long v1 = a2 & 0xff00_0000L;
    if(v1 == 0) {
      //LAB_80115f54
      final BattleScriptDataBase a0 = scriptStatePtrArr_800bc1c0.get(a2).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);
      if(a0.magic_00.get() != BattleScriptDataBase.EM__) {
        //LAB_8011604c
        v0 = _800fb0ec.offset(((BattleObject27c)a0).colourMap_1e5.get() * 0x4L).get();
        t1 = (int)((v0 & 0xf) << 6);
        a3 = (int)((v0 & 0x10) << 4);
        a1 = 0x100;
        t0 = 0x100;
      } else {
        final EffectManagerData6c manager = (EffectManagerData6c)a0;
        v1 = manager._04.get() & 0xff00_0000L;
        if(v1 == 0x200_0000L) {
          //LAB_80115fd8
          v0 = _800fb0ec.offset(manager._44.derefAs(BttlScriptData6cSub13c.class)._134.deref().ub_9d.get() * 0x4L).get();
          t1 = (int)((v0 & 0xf) << 6);
          a3 = (int)((v0 & 0x10) << 4);
          a1 = 0x100;
          t0 = 0x100;
        } else if(v1 == 0x300_0000L) {
          //LAB_80116014
          throw new RuntimeException("ASM is bugged");
//          v0 = manager._44.deref();
//          MEMORY.ref(4, v0).offset(0x4L).setu(0);
//          v0 = t2 + MEMORY.ref(4, t2).offset(0x8L).get() + s0 * 0x10L;
//          t1 = MEMORY.ref(2, v0).offset(0x0L).getSigned();
//          a3 = MEMORY.ref(2, v0).offset(0x2L).getSigned();
//          a1 = MEMORY.ref(2, v0).offset(0x4L).getSigned() * 4;
//          t0 = MEMORY.ref(2, v0).offset(0x6L).getSigned();
          //LAB_80115fc8
        } else if(v1 == 0x400_0000L) {
          //LAB_8011602c
          v0 = manager._44.getPointer(); //TODO
          t1 = (int)MEMORY.ref(2, v0).offset(0x4L).get();
          a3 = (int)MEMORY.ref(2, v0).offset(0x6L).get();
          a1 = (int)MEMORY.ref(1, v0).offset(0x8L).get();
          t0 = (int)MEMORY.ref(1, v0).offset(0x9L).get();
        } else {
          throw new RuntimeException("Invalid state");
        }
      }
    } else if(v1 == 0x200_0000L) {
      //LAB_80116098
      v0 = FUN_800eac58(a2).getAddress();
      v0 = v0 + MEMORY.ref(4, v0).offset(0x8L).get();
      t1 = (int)MEMORY.ref(2, v0).offset(0x0L).getSigned();
      a3 = (int)MEMORY.ref(2, v0).offset(0x2L).getSigned();

      //LAB_801160b4
      a1 = 0x100;
      t0 = 0x100;
    } else if(v1 == 0x100_0000L || v1 == 0x300_0000L) {
      //LAB_801160c0
      //LAB_801160d4
      v0 = FUN_800eac58(a2).getAddress();
      v0 = v0 + MEMORY.ref(4, v0).offset(0x8L).get() + s0 * 0x10L;
      t1 = (int)MEMORY.ref(2, v0).offset(0x0L).getSigned();
      a3 = (int)MEMORY.ref(2, v0).offset(0x2L).getSigned();
      a1 = (int)(MEMORY.ref(2, v0).offset(0x4L).getSigned() * 4);
      t0 = (int)MEMORY.ref(2, v0).offset(0x6L).getSigned();
      //LAB_80115f34
    } else if(v1 == 0x400_0000L) {
      //LAB_801160f4
      final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0xc);
      final Value sp0x10 = sp0x10tmp.get();
      FUN_800e95f0(sp0x10.getAddress(), a2 & 0xff_ffffL);
      t1 = (int)sp0x10.offset(2, 0x4L).get();
      a3 = (int)sp0x10.offset(2, 0x6L).get();
      a1 = (int)sp0x10.offset(1, 0x8L).get();
      t0 = (int)sp0x10.offset(1, 0x9L).get();
      sp0x10tmp.release();
    } else {
      throw new RuntimeException("Invalid state");
    }

    //LAB_80116118
    script.params_20.get(2).deref().set(t1);
    script.params_20.get(3).deref().set(a3);
    script.params_20.get(4).deref().set(a1);
    script.params_20.get(5).deref().set(t0);
    return 0;
  }

  @Method(0x8011826cL)
  public static void FUN_8011826c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub14_2 s1 = data._44.derefAs(BttlScriptData6cSub14_2.class);

    if((int)data._10._00.get() >= 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, data);
      if((data._10._00.get() & 0x4000_0000L) != 0) {
        _1f8003ec.setu(data._10._00.get() >>> 23 & 0x60L);
      } else {
        //LAB_801182bc
        _1f8003ec.setu(s1._10.get());
      }

      //LAB_801182c8
      _1f8003e8.setu(data._10._22.get());
      if((data._10._00.get() & 0x40L) == 0) {
        FUN_800e61e4(data._10.svec_1c.getX() << 5, data._10.svec_1c.getY() << 5, data._10.svec_1c.getZ() << 5);
      } else {
        //LAB_80118304
        FUN_800e60e0(0x1000, 0x1000, 0x1000);
      }

      //LAB_80118314
      if(data.scriptIndex_0c.get() < -0x2L) {
        if(data.scriptIndex_0c.get() == -0x4L) {
          sp0x10.transfer.setZ((int)_1f8003f8.get());
        }

        //LAB_8011833c
        GsSetLightMatrix(sp0x10);
        setRotTransMatrix(sp0x10);

        final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
        final GsDOBJ2 dobj2 = new GsDOBJ2(tmp.get());
        dobj2.attribute_00.set(data._10._00.get());
        dobj2.tmd_08.set(s1.tmd_08.deref());
        FUN_800e3e6c(dobj2);
        tmp.release();
      } else {
        //LAB_80118370
        FUN_800de3f4(s1.tmd_08.deref(), data._10, sp0x10);
      }

      //LAB_80118380
      if((data._10._00.get() & 0x40L) == 0) {
        FUN_800e62a8();
      } else {
        //LAB_801183a4
        FUN_800e6170();
      }
    }

    //LAB_801183ac
  }

  @Method(0x801183c0L)
  public static long FUN_801183c0(final RunningScript s3) {
    final long s1 = s3.params_20.get(1).deref().get();
    final int s4 = allocateEffectManager(
      s3.scriptStateIndex_00.get(),
      0x14L,
      null,
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_8011826c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      null,
      BttlScriptData6cSub14_2::new
    );

    final EffectManagerData6c s2 = scriptStatePtrArr_800bc1c0.get(s4).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    s2._04.set(0x300_0000L);

    final BttlScriptData6cSub14_2 s0 = s2._44.derefAs(BttlScriptData6cSub14_2.class);
    s0._00.set(s1 | 0x300_0000L);

    if((s1 & 0xf_ff00L) == 0xf_ff00L) {
      s0._10.set(0x20);
      s0._04.set(0);
      s0.tmd_08.set(struct7cc_800c693c.deref()._2f8.get((int)(s1 & 0xff)).deref());
    } else {
      //LAB_8011847c
      long v0 = FUN_800eac58(s1 | 0x300_0000L).getAddress(); //TODO
      s0._04.set(v0);
      v0 = v0 + MEMORY.ref(4, v0).offset(0xcL).get();
      s0.tmd_08.set(MEMORY.ref(4, v0 + 0x18L, TmdObjTable::new));
      s0._10.set((int)((MEMORY.ref(4, v0).offset(0xcL).get() & 0xffff_0000L) >>> 11));
    }

    //LAB_801184ac
    s2._10._00.set(0x1400_0000L);
    s3.params_20.get(0).deref().set(s4);
    return 0;
  }

  @Method(0x801184e4L)
  public static long FUN_801184e4(final RunningScript a0) {
    long v0;
    long v1;
    final int s1 = a0.params_20.get(1).deref().get();
    final int s2 = a0.params_20.get(2).deref().get();

    final int scriptIndex = allocateEffectManager(
      a0.scriptStateIndex_00.get(),
      0x14,
      null,
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_8011826c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      null,
      BttlScriptData6cSub14_2::new
    );

    final EffectManagerData6c s4 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    s4._04.set(0x300_0000L);

    final BttlScriptData6cSub14_2 s0 = s4._44.derefAs(BttlScriptData6cSub14_2.class);
    s0._10.set(0x20);
    s0._00.set(0x300_0000L);
    s0._04.set(0);

    v1 = s1 & 0xff00_0000L;
    if(v1 == 0x100_0000L) {
      //LAB_801185e4
      v0 = FUN_800eac58(s1).getAddress(); //TODO
      v0 = v0 + MEMORY.ref(4, v0).offset(0xcL).get() + s2 * 0x1cL + 0x18L;
      s0.tmd_08.setPointer(v0); //TODO
    } else if(v1 == 0x200_0000L) {
      //LAB_801185c0
      v0 = FUN_800eac58(s1).getAddress(); //TODO
      v0 = v0 + MEMORY.ref(4, v0).offset(0xcL).get();
      s0.tmd_08.set(optimisePacketsIfNecessary(MEMORY.ref(4, v0 + 0xcL, TmdWithId::new), s2)); //TODO
      //LAB_801185b0
    } else if(v1 == 0x700_0000L) {
      //LAB_80118610
      s0.tmd_08.set(_1f8003f4.deref().render_963c.dobj2s_00.get(s2).tmd_08.deref());
    } else {
      //LAB_80118634
      final BattleScriptDataBase a0_0 = scriptStatePtrArr_800bc1c0.get(s1).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);
      if(a0_0.magic_00.get() == BattleScriptDataBase.EM__) {
        final EffectManagerData6c effects = (EffectManagerData6c)a0_0;
        v1 = effects._04.get() & 0xff00_0000L;
        if(v1 == 0x100_0000L || v1 == 0x200_0000L) {
          //LAB_8011867c
          s0.tmd_08.set(effects._44.derefAs(BttlScriptData6cSub13c.class)._134.deref().dobj2ArrPtr_00.deref().get(s2).tmd_08.deref());
        }
      } else {
        //LAB_801186a4
        //LAB_801186b4
        s0.tmd_08.set(((BattleObject27c)a0_0)._148.dobj2ArrPtr_00.deref().get(s2).tmd_08.deref());
      }
    }

    //LAB_801186bc
    //LAB_801186c0
    s4._10._00.set(0x1400_0000L);
    a0.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x801186f8L)
  public static void FUN_801186f8(final long a0, final long a1) {
    MEMORY.ref(4, a0).offset(0x0L).setu(a1 | 0x300_0000L);
    if((a1 & 0xf_ff00L) == 0xf_ff00L) {
      MEMORY.ref(2, a0).offset(0x10L).setu(0x20L);
      MEMORY.ref(4, a0).offset(0x4L).setu(0);
      MEMORY.ref(4, a0).offset(0x8L).setu(struct7cc_800c693c.deref()._2f8.get((int)(a1 & 0xff)).deref().getAddress()); //TODO
    } else {
      //LAB_80118750
      long v0 = FUN_800eac58(a1 | 0x300_0000L).getAddress(); //TODO
      MEMORY.ref(4, a0).offset(0x4L).setu(v0);
      v0 = v0 + MEMORY.ref(4, v0).offset(0xcL).get();
      MEMORY.ref(4, a0).offset(0x8L).setu(v0 + 0x18L);
      MEMORY.ref(2, a0).offset(0x10L).setu((MEMORY.ref(4, v0).offset(0xcL).get() & 0xffff_0000L) >>> 11);
    }

    //LAB_80118780
  }

  @Method(0x80118790L)
  public static void FUN_80118790(final int scriptIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final EffectManagerData6c s1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    if((int)s1._10._00.get() >= 0) {
      final int y = s1._10.vec_04.getY();
      s1._10.vec_04.setY(0);
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, s1);
      sp0x10.transfer.setY(y);
      s1._10.vec_04.setY(y);

      final short rotY = (short)ratan2(-sp0x10.get(6), sp0x10.get(0));
      RotMatrixY(-rotY, sp0x10);
      final short rotZ = (short)ratan2(sp0x10.get(3), sp0x10.get(0));
      RotMatrixZ(-rotZ, sp0x10);
      final short rotX = (short)ratan2(-sp0x10.get(5), sp0x10.get(8));
      RotMatrixX(-rotX, sp0x10);
      RotMatrixY(rotY, sp0x10);
      sp0x10.set(3, (short)0);
      sp0x10.set(4, (short)0);
      sp0x10.set(5, (short)0);
      _1f8003ec.setu(s1._10._00.get() >>> 23 & 0x60L);
      _1f8003e8.setu(s1._10._22.get());
      FUN_800e60e0(s1._10.svec_1c.getX() << 5, s1._10.svec_1c.getY() << 5, s1._10.svec_1c.getZ() << 5);
      FUN_800de3f4(bigStruct_800bda10.dobj2ArrPtr_00.deref().get(0).tmd_08.deref(), s1._10, sp0x10);
      FUN_800e6170();
    }

    //LAB_801188d8
  }

  @Method(0x801188ecL)
  public static long FUN_801188ec(final RunningScript a0) {
    final int scriptIndex = allocateEffectManager(a0.scriptStateIndex_00.get(), 0, null, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80118790", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), null, null);
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
    final EffectManagerData6c manager = state.innerStruct_00.derefAs(EffectManagerData6c.class);
    manager._04.set(0x600_0000L);
    manager._10.svec_16.set((short)0x400, (short)0x400, (short)0x400);
    manager._10._24.set(0);
    manager._10._00.set(0x6400_0040L);
    a0.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x80118984L)
  public static long FUN_80118984(final RunningScript a0) {
    final int effectIndex = a0.params_20.get(0).deref().get();
    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(effectIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub13c effect = manager._44.derefAs(BttlScriptData6cSub13c.class);
    long v0 = FUN_800eac58(a0.params_20.get(1).deref().get() | 0x500_0000L).getAddress();
    v0 = v0 + MEMORY.ref(4, v0).offset(0x14L).get();
    effect.ptr_0c.set(v0);
    FUN_800de36c(effect._134.deref(), v0);
    manager._10._24.set(0);
    FUN_80114f3c(effectIndex, 0, 0x100, 0);
    return 0;
  }

  @Method(0x80118e98L)
  public static void FUN_80118e98(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long v0;
    long v1;
    final long a2;
    final long a3;
    final long t0;
    long t1;
    final long t5;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long lo;
    final VECTOR sp0x84 = new VECTOR();
    final VECTOR sp0x90 = new VECTOR();
    final VECTOR sp0x9c = new VECTOR();
    long spb0;
    long spb4;
    long spb8;

    final EffectManagerData6c fp = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub30 s0 = fp._44.derefAs(BttlScriptData6cSub30.class);
    if((int)fp._10._00.get() >= 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, fp);

      v1 = s0.scriptIndex_04.get() & 0xff00_0000L;
      if(v1 == 0x300_0000L) {
        //LAB_80118f38
        if((fp._10._00.get() & 0x4000_0000L) != 0) {
          _1f8003ec.setu(fp._10._00.get() >>> 23 & 0x60L);
        } else {
          //LAB_80118f5c
          _1f8003ec.setu(s0._2c.get());
        }

        //LAB_80118f68
        _1f8003e8.setu(fp._10._22.get());

        if((fp._10._00.get() & 0x40L) == 0) {
          FUN_800e61e4(fp._10.svec_1c.getX() << 5, fp._10.svec_1c.getY() << 5, fp._10.svec_1c.getZ() << 5);
        }

        //LAB_80118f9c
        FUN_800de3f4(s0.tmd_24.deref(), fp._10, sp0x10);
      } else if(v1 == 0x400_0000L) {
        FUN_800e9428(s0.getAddress() + 0x20L, fp._10, sp0x10); //TODO
      }

      //LAB_80118fac
      if(s0._08.get() != 0) {
        final Memory.TemporaryReservation sp0x50tmp = MEMORY.temp(0x34);
        final BttlScriptData6cInner sp0x50 = sp0x50tmp.get().cast(BttlScriptData6cInner::new);

        //LAB_80118fc4
        memcpy(sp0x50.getAddress(), fp._10.getAddress(), 0x34);

        t0 = s0._08.get();
        v0 = s0._0c.get() + 0x1L;
        lo = (long)(int)t0 * (int)v0 & 0xffff_ffffL;
        a2 = lo;
        if((s0.scriptIndex_00.get() & 0x4L) != 0) {
          v0 = s0._10.get() - 0x1000L;
          lo = sp0x50.svec_1c.getX() * (int)v0 & 0xffff_ffffL;
          v1 = lo;
          lo = (int)v1 / (int)a2;
          t1 = lo;
          lo = sp0x50.svec_1c.getY() * (int)v0 & 0xffff_ffffL;
          v1 = lo;
          sp0x84.setX((int)t1);
          lo = (int)v1 / (int)a2;
          t1 = lo;
          lo = sp0x50.svec_1c.getZ() * (int)v0 & 0xffff_ffffL;
          t5 = lo;
          sp0x50.vec_28.setX(sp0x50.svec_1c.getX() << 12);
          sp0x50.vec_28.setY(sp0x50.svec_1c.getY() << 12);
          sp0x50.vec_28.setZ(sp0x50.svec_1c.getZ() << 12);
          sp0x84.setY((int)t1);
          lo = (int)t5 / (int)a2;
          t1 = lo;
          sp0x84.setZ((int)t1);
        }

        //LAB_801190a8
        if((s0.scriptIndex_00.get() & 0x8L) != 0) {
          v0 = s0._10.get() - 0x1000L;
          lo = sp0x50.svec_16.getX() * (int)v0 & 0xffff_ffffL;
          v1 = lo;
          lo = (int)v1 / (int)a2;
          t1 = lo;
          lo = sp0x50.svec_16.getY() * (int)v0 & 0xffff_ffffL;
          v1 = lo;
          sp0x9c.setX((int)t1);
          lo = (int)v1 / (int)a2;
          t1 = lo;
          lo = sp0x50.svec_16.getZ() * (int)v0 & 0xffff_ffffL;
          sp0x90.setX(sp0x50.svec_16.getX() << 12);
          sp0x90.setY(sp0x50.svec_16.getY() << 12);
          a3 = lo;
          sp0x90.setZ(sp0x50.svec_16.getZ() << 12);
          sp0x9c.setY((int)t1);
          lo = (int)a3 / (int)a2;
          t1 = lo;
          sp0x9c.setZ((int)t1);
        }

        //LAB_80119130
        //LAB_8011914c
        for(s6 = 1; s6 < s0._08.get() && s6 < s0._14.get(); s6++) {
          final VECTOR a1 = s0._18.deref().get((int)((s0._14.get() - s6 - 0x1L) % s0._08.get()));

          v1 = s0._0c.get() + 0x1L;
          spb0 = (a1.getX() - sp0x10.transfer.getX() << 12) / (int)v1;
          spb4 = (a1.getY() - sp0x10.transfer.getY() << 12) / (int)v1;
          spb8 = (a1.getZ() - sp0x10.transfer.getZ() << 12) / (int)v1;

          s4 = sp0x10.transfer.getX() << 12;
          s3 = sp0x10.transfer.getY() << 12;
          s2 = sp0x10.transfer.getZ() << 12;

          s5 = s0.scriptIndex_04.get() & 0xff00_0000L;

          //LAB_80119204
          for(s1 = s0._0c.get(); s1 >= 0; s1--) {
            if((s0.scriptIndex_00.get() & 0x4L) != 0) {
              sp0x50.vec_28.add(sp0x84);
              //LAB_80119254
              //LAB_80119270
              //LAB_8011928c
              sp0x50.svec_1c.setX((short)(sp0x50.vec_28.getX() / 0x1000));
              sp0x50.svec_1c.setY((short)(sp0x50.vec_28.getY() / 0x1000));
              sp0x50.svec_1c.setZ((short)(sp0x50.vec_28.getZ() / 0x1000));
            }

            //LAB_80119294
            if((s0.scriptIndex_00.get() & 0x8L) != 0) {
              sp0x90.add(sp0x9c);

              //LAB_801192e4
              //LAB_80119300
              //LAB_8011931c
              sp0x50.svec_16.setX((short)(sp0x90.getX() / 0x1000));
              sp0x50.svec_16.setY((short)(sp0x90.getY() / 0x1000));
              sp0x50.svec_16.setZ((short)(sp0x90.getZ() / 0x1000));
            }

            //LAB_80119324
            s4 = s4 + spb0;
            s3 = s3 + spb4;
            s2 = s2 + spb8;

            //LAB_80119348
            //LAB_80119360
            //LAB_80119378
            final MATRIX sp0x30 = new MATRIX().set(sp0x10);
            sp0x30.transfer.set((int)(s4 / 0x1000), (int)(s3 / 0x1000), (int)(s2 / 0x1000));

            ScaleVectorL_SVEC(sp0x30, sp0x50.svec_16);
            if(s5 == 0x300_0000L) {
              //LAB_801193f0
              FUN_800de3f4(s0.tmd_24.deref(), sp0x50, sp0x30);
            } else if(s5 == 0x400_0000L) {
              FUN_800e9428(s0.getAddress() + 0x20L, sp0x50, sp0x30); //TODO
            }

            //LAB_80119400
            //LAB_80119404
          }

          //LAB_8011940c
        }

        sp0x50tmp.release();

        //LAB_80119420
        if((s0.scriptIndex_04.get() & 0xff00_0000L) == 0x300_0000L && (fp._10._00.get() & 0x40L) == 0) {
          FUN_800e62a8();
        }
      }
    }

    //LAB_80119454
  }

  @Method(0x80119484L)
  public static long FUN_80119484(final RunningScript a0) {
    int s4 = a0.params_20.get(1).deref().get();
    final int s2 = a0.params_20.get(2).deref().get();
    final int s6 = a0.params_20.get(3).deref().get();
    final int s0 = a0.params_20.get(4).deref().get();
    final int s1 = a0.params_20.get(5).deref().get();

    final int fp = allocateEffectManager(
      a0.scriptStateIndex_00.get(),
      0x30L,
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801196bc", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80118e98", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80119788", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub30::new
    );

    final ScriptState<EffectManagerData6c> v0 = scriptStatePtrArr_800bc1c0.get(fp).derefAs(ScriptState.classFor(EffectManagerData6c.class));
    final EffectManagerData6c data = v0.innerStruct_00.deref();
    strcpy(data.type_5c, _800fb954.get());

    final BttlScriptData6cSub30 s3 = data._44.derefAs(BttlScriptData6cSub30.class);
    s3.scriptIndex_00.set(s2);
    s3.scriptIndex_04.set(s4);
    s3._08.set(s6);
    s3._0c.set(s0);
    s3._10.set(s1);
    s3._14.set(0);

    if(s6 != 0) {
      s3._18.setPointer(addToLinkedListHead(s6 * 0x10L));
    } else {
      //LAB_80119568
      s3._18.clear();
    }

    //LAB_8011956c
    final long v1 = s4 & 0xff00_0000L;
    if(v1 == 0x400_0000L) {
      //LAB_80119640
      FUN_800e95f0(s3.getAddress() + 0x1cL, s4); //TODO
      data._10._00.or(0x5000_0000L).and(0xfbff_ffffL);
    } else if(v1 == 0x300_0000L) {
      //LAB_80119668
      FUN_801186f8(s3.getAddress() + 0x1cL, s4); //TODO
      data._10._00.set(0x1400_0000L);
    } else if(v1 == 0) {
      //LAB_801195a8
      final BttlScriptData6cSubBase1 v1_0 = scriptStatePtrArr_800bc1c0.get(s4).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.deref();
      s4 = v1_0.scriptIndex_00.get();
      s3.scriptIndex_04.set(s4);

      final long a1 = s4 & 0xff00_0000L;
      if(a1 == 0x300_0000L) {
        //LAB_8011960c
        //TODO
        MEMORY.ref(4, s3.getAddress()).offset(0x1cL).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x0L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x20L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x4L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x24L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x8L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x28L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0xcL).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x2cL).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x10L).get());
      } else if(a1 == 0x400_0000L) {
        //TODO
        MEMORY.ref(4, s3.getAddress()).offset(0x1cL).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x0L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x20L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x4L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x24L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x8L).get());
      }
    }

    //LAB_8011967c
    a0.params_20.get(0).deref().set(fp);
    return 0;
  }

  @Method(0x801196bcL)
  public static void FUN_801196bc(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final EffectManagerData6c a1 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final BttlScriptData6cSub30 s0 = a1._44.derefAs(BttlScriptData6cSub30.class);

    if(s0._08.get() != 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, a1);
      s0._18.deref().get((int)(s0._14.get() % s0._08.get())).set(sp0x10.transfer);
      s0._14.incr();
    }

    //LAB_80119778
  }

  @Method(0x80119788L)
  public static void FUN_80119788(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    if(!data._44.derefAs(BttlScriptData6cSub30.class)._18.isNull()) {
      removeFromLinkedList(data._44.derefAs(BttlScriptData6cSub30.class)._18.getPointer());
    }
  }
}
