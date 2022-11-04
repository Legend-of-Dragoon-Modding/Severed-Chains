package legend.game;

import legend.core.MemoryHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gte.DVECTOR;
import legend.core.gte.MATRIX;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.combat.types.BattleStruct24;
import legend.game.combat.types.BattleStruct7cc;
import legend.game.combat.types.BttlScriptData6cSubBase1;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;
import legend.game.types.Translucency;

import static legend.core.Hardware.GPU;
import static legend.core.Hardware.MEMORY;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800c.DISPENV_800c34b0;
import static legend.game.combat.Bttl_800c.struct7cc_800c693c;
import static legend.game.combat.Bttl_800e.FUN_800e7dbc;
import static legend.game.combat.Bttl_800e.FUN_800e7ea4;
import static legend.game.combat.Bttl_800e.FUN_800e8594;
import static legend.game.combat.Bttl_800e.allocateEffectManager;

public final class Temp {
  private Temp() { }

  private static final BattleStruct24 _800cbb74 = new BattleStruct24();

  @Method(0x800ca200L)
  public static void FUN_800ca200(final int effectIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final TempEffectData_d18 s1 = manager.effect_44.derefAs(TempEffectData_d18.class);
    s1._14.incr();
    final TempEffectData_d18_Sub34 s0 = s1._18.get(s1._14.get() & 0x3f);
    s0._00.setX(manager._10.trans_04.getX() + ((rand() & 0x1ff) - 0xff) * s1._10.get() / 0x1000);
    s0._00.setY(manager._10.trans_04.getY() - (rand() & 0x1ff) * s1._10.get() / 0x1000);
    s0._00.setZ(manager._10.trans_04.getZ() + ((rand() & 0x1ff) - 0xff) * s1._10.get() / 0x1000);
    s0._10.set((rand() & 0x1f) * s1._10.get() * 2 / 0x1000 + 1);
    s0._18.set((32 - s0._10.get()) * 2 + 64);

    if((rand() & 1) != 0) {
      s0._18.neg();
    }

    //LAB_800ca36c
    s0._20.set(0x8_0000);
    s0._24.set(-0x2000);
    s0._28.set(s1._10.get() / 2);
    s0._2c.set(s1._10.get() / 32);
    s0._30.set(0);
    s0._31.set(rand() & 0x7f);

    //LAB_800ca3c4
    for(int i = 0; i < 0x40; i++) {
      final TempEffectData_d18_Sub34 a2 = s1._18.get(i);
      a2._00.y.sub(a2._10.get());
      a2._14.add(a2._18.get());
      a2._20.add(a2._24.get());
      a2._28.add(a2._2c.get());
      a2._30.incr();
    }
  }

  @Method(0x800ca438L)
  public static void FUN_800ca438(final int effectIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final TempEffectData_d18 effect = manager.effect_44.derefAs(TempEffectData_d18.class);

    final MATRIX sp0x10 = new MATRIX();
    FUN_800e8594(sp0x10, manager);
    effect._10.set(manager._10.scale_16.getZ());

    final DVECTOR sp0x30 = new DVECTOR();
    if(FUN_800e7dbc(sp0x30, sp0x10.transfer) != 0) {
      final BattleStruct7cc a0 = struct7cc_800c693c.deref();
      final BattleStruct24 s1 = _800cbb74;
      s1._00.set(manager._10._00.get());
      s1.tpage_0c.set((a0.v_3ae.get() & 0x100) >>> 4 | (a0.u_3ac.get() & 0x3ff) >>> 6);
      s1.u_0e.set((a0.u_3ac.get() & 0x3f) << 2);
      s1.v_0f.set(a0.v_3ae.get());
      s1.clutX_10.set(a0.clut_3b2.get() << 4 & 0x3ff);
      s1.clutY_12.set(a0.clut_3b2.get() >>> 6 & 0x1ff);

      //LAB_800ca528
      for(int i = 0; i < 0x40; i++) {
        final TempEffectData_d18_Sub34 s0 = effect._18.get(i);
        s1._1e.set((short)(s0._28.get() / 4));
        s1._1c.set((short)(s0._28.get() / 4));
        s1.rotation_20.set(s0._14.get());
        s1.r_14.set(manager._10.colour_1c.getX() * s0._20.get() / 0x100000);
        s1.g_15.set(manager._10.colour_1c.getY() * s0._20.get() / 0x100000);
        s1.b_16.set(manager._10.colour_1c.getZ() * s0._20.get() / 0x100000);
        s1._00.and(0xcfff_ffffL).or(s0._31.get() < s0._30.get() ? 0x2000_0000L : 0x1000_0000L);
        FUN_800e7ea4(s1, s0._00);
      }
    }

    //LAB_800ca620
  }

  @Method(0x800ca658L)
  public static long FUN_800ca658(final RunningScript script) {
    final int effectIndex = allocateEffectManager(
      script.scriptStateIndex_00.get(),
      0xd18,
      MEMORY.ref(4, MemoryHelper.getMethodAddress(Temp.class, "FUN_800ca200", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, MemoryHelper.getMethodAddress(Temp.class, "FUN_800ca438", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      null,
      TempEffectData_d18::new
    );

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(effectIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final TempEffectData_d18 effect = manager.effect_44.derefAs(TempEffectData_d18.class);

    effect._00.set(0);
    effect._04.set(0);
    effect._08.set(0);
    effect._10.set(0x1000);
    effect._14.set(0);

    //LAB_800ca6c0
    for(int i = 0; i < 0x40; i++) {
      final TempEffectData_d18_Sub34 v1 = effect._18.get(i);
      v1._00.set(0, 0, 0);
      v1._10.set(0);
      v1._14.set(0);
      v1._18.set(0);
      v1._1c.set(0);
      v1._20.set(0);
      v1._24.set(0);
      v1._28.set(0);
      v1._2c.set(0);
      v1._30.set(0);
      v1._31.set(0);
    }

    manager._10._00.or(0x5000_0000L);
    script.params_20.get(0).deref().set(effectIndex);
    return 0;
  }

  @Method(0x800ca89cL)
  public static void FUN_800ca89c(final int effectIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final TempEffectData_08 s4 = manager.effect_44.derefAs(TempEffectData_08.class);

    //LAB_800ca8fc
    for(int i = 0; i < 5; i++) {
      final int s2 = i * 64;
      final int s1 = s2 - 160;

      GPU.queueCommand(1, new GpuCommandQuad()
        .bpp(Bpp.BITS_15)
        .translucent(Translucency.B_MINUS_F)
        .vramPos(s4.u_00.get() & 0x3c0, s4.v_02.get() & 0x100)
        .monochrome(0x80)
        .pos(s1, -120, 64, 240)
        .uv(0, 0)
      );

      GPU.queueCommand(1, new GpuCommandQuad()
        .rgb(manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
        .pos(s1, -120, 64, 240)
      );

      GPU.queueCommand(1, new GpuCommandCopyVramToVram(DISPENV_800c34b0.disp.x.get() + s2, DISPENV_800c34b0.disp.y.get(), s4.u_00.get(), s4.v_02.get(), 64, 240));
    }
  }

  @Method(0x800caae4L)
  public static long FUN_800caae4(final RunningScript script) {
    final int effectIndex = allocateEffectManager(
      script.scriptStateIndex_00.get(),
      0x8,
      null,
      MEMORY.ref(4, MemoryHelper.getMethodAddress(Temp.class, "FUN_800ca89c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      null,
      TempEffectData_08::new
    );

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(effectIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final TempEffectData_08 effect = manager.effect_44.derefAs(TempEffectData_08.class);
    effect.u_00.set((short)0x300);
    effect.v_02.set((short)0);
    effect._04.set(0xff);
    effect._05.set(0xff);
    effect._06.set((short)0);
    manager._10._00.and(0xfbff_ffffL).or(0x5000_0000L);
    script.params_20.get(0).deref().set(effectIndex);
    return 0;
  }

  private static class TempEffectData_d18 extends BttlScriptData6cSubBase1 {
    public final IntRef _00;
    public final IntRef _04;
    public final IntRef _08;

    public final IntRef _10;
    public final IntRef _14;
    public final ArrayRef<TempEffectData_d18_Sub34> _18;

    public TempEffectData_d18(final Value ref) {
      super(ref);

      this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
      this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
      this._08 = ref.offset(4, 0x08L).cast(IntRef::new);

      this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
      this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
      this._18 = ref.offset(4, 0x18L).cast(ArrayRef.of(TempEffectData_d18_Sub34.class, 0x40, 0x34, TempEffectData_d18_Sub34::new));
    }
  }

  private static class TempEffectData_d18_Sub34 implements MemoryRef {
    private final Value ref;

    public final VECTOR _00;
    public final IntRef _10;
    public final IntRef _14;
    public final IntRef _18;
    public final IntRef _1c;
    public final IntRef _20;
    public final IntRef _24;
    public final IntRef _28;
    public final IntRef _2c;
    public final ByteRef _30;
    public final ByteRef _31;

    public TempEffectData_d18_Sub34(final Value ref) {
      this.ref = ref;

      this._00 = ref.offset(4, 0x00L).cast(VECTOR::new);
      this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
      this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
      this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
      this._1c = ref.offset(4, 0x1cL).cast(IntRef::new);
      this._20 = ref.offset(4, 0x20L).cast(IntRef::new);
      this._24 = ref.offset(4, 0x24L).cast(IntRef::new);
      this._28 = ref.offset(4, 0x28L).cast(IntRef::new);
      this._2c = ref.offset(4, 0x2cL).cast(IntRef::new);
      this._30 = ref.offset(1, 0x30L).cast(ByteRef::new);
      this._31 = ref.offset(1, 0x31L).cast(ByteRef::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }

  private static class TempEffectData_08 extends BttlScriptData6cSubBase1 {
    public final ShortRef u_00;
    public final ShortRef v_02;
    public final UnsignedByteRef _04;
    public final UnsignedByteRef _05;

    public final ShortRef _06;

    public TempEffectData_08(final Value ref) {
      super(ref);

      this.u_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
      this.v_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
      this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
      this._05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);

      this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    }
  }
}
