package legend.game;

import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.game.types.BtldScriptData27c;
import legend.game.types.BttlScriptData6c;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;

import javax.annotation.Nullable;

import static legend.core.Hardware.MEMORY;
import static legend.game.Bttl.FUN_800de544;
import static legend.game.Bttl.FUN_800e8d04;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public final class SEffe {
  private SEffe() { }

  @Method(0x80110030L)
  public static VECTOR FUN_80110030(final int scriptIndex) {
    final long a0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.getPointer(); //TODO

    if(MEMORY.ref(4, a0).offset(0x0L).get() == 0x2020_4d45L) {
      return MEMORY.ref(4, a0 + 0x14L, VECTOR::new);
    }

    //LAB_8011006c
    return MEMORY.ref(4, a0 + 0x174L, VECTOR::new);
  }

  @Method(0x801100b8L)
  public static void FUN_801100b8(final int scriptIndex, final Ref<SVECTOR> a1, final Ref<VECTOR> a2) {
    final ScriptState<BtldScriptData27c> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    final BtldScriptData27c a3 = state.innerStruct_00.deref();

    if(a3._00.get() == 0x2020_4d45L) {
      a2.set(MEMORY.ref(4, a3.getAddress() + 0x14L, VECTOR::new)); //TODO
      a1.set(MEMORY.ref(2, a3.getAddress() + 0x20L, SVECTOR::new)); //TODO
      return;
    }

    //LAB_801100fc
    a2.set(a3._148.coord2_14.coord.transfer);
    a1.set(a3._148.coord2Param_64.rotate);
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
  public static BttlScriptData6c FUN_8011066c(final int scriptIndex1, final int scriptIndex2, final VECTOR a2) {
    final BttlScriptData6c data = scriptStatePtrArr_800bc1c0.get(scriptIndex1).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
    long v1 = data._00.getPointer();
    if(v1 == 0x2020_4d45L && (data._04.get() & 0x2L) != 0) {
      FUN_800e8d04(data, 0x1L);
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

  @Method(0x80111ae4L)
  public static long FUN_80111ae4(final RunningScript a0) {
    final VECTOR sp0x10 = new VECTOR().set((int)a0.params_20.get(2).deref().get(), (int)a0.params_20.get(3).deref().get(), (int)a0.params_20.get(4).deref().get());
    FUN_8011066c((int)a0.params_20.get(0).deref().get(), (int)a0.params_20.get(1).deref().get(), sp0x10);
    return 0;
  }

  @Method(0x80112900L)
  public static long FUN_80112900(final RunningScript s0) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_80110228(sp0x10, FUN_80110030((int)s0.params_20.get(0).deref().get()), FUN_80110030((int)s0.params_20.get(1).deref().get()));
    s0.params_20.get(2).deref().set(sp0x10.getX());
    s0.params_20.get(3).deref().set(sp0x10.getZ());
    s0.params_20.get(4).deref().set(sp0x10.getY());
    return 0;
  }

  @Method(0x80115690L)
  public static long FUN_80115690(final RunningScript a0) {
    final long s0 = a0.params_20.get(0).deref().get();
    loadScriptFile(s0, a0.scriptState_04.deref().scriptPtr_14.deref(), 0, "S_EFFE Script", 0); //TODO unknown size
    scriptStatePtrArr_800bc1c0.get((int)s0).deref().commandPtr_18.set(a0.params_20.get(1).deref());
    return 0;
  }

  @Method(0x801156f8L)
  public static long FUN_801156f8(final RunningScript a0) {
    final long v0 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(1, v0).offset(0xcL).setu(a0.params_20.get(1).deref().get());
    MEMORY.ref(1, v0).offset(0xdL).setu(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x80115cacL)
  public static void FUN_80115cac(long a0) {
    assert false;
  }
}
