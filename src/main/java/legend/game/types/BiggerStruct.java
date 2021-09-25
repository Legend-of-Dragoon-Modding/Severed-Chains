package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

import java.util.function.Function;

public class BiggerStruct<T extends MemoryRef> implements MemoryRef {
  public static <T extends MemoryRef> Function<Value, BiggerStruct<T>> of(final Function<Value, T> constructor) {
    return ref -> new BiggerStruct<>(ref, constructor);
  }

  public static <T extends MemoryRef> Class<BiggerStruct<T>> classFor(final Class<T> t) {
    //noinspection unchecked
    return (Class<BiggerStruct<T>>)(Class<?>)BiggerStruct.class;
  }

  private final Value ref;

  public final Pointer<T> innerStruct_00; // Pointer to primitive
  public final UnsignedIntRef ui_04; // Pointer to GsOT
  public final UnsignedIntRef ui_08; // shift
  public final UnsignedIntRef ui_0c; // offset
  public final UnsignedIntRef ui_10;
  public final UnsignedIntRef ui_14;
  public final UnsignedIntRef ui_18;
  public final UnsignedIntRef ui_1c;
  public final UnsignedIntRef ui_20;
  public final UnsignedIntRef ui_24;
  public final UnsignedIntRef ui_28;
  public final UnsignedIntRef ui_2c;
  public final UnsignedIntRef ui_30;
  public final UnsignedIntRef ui_34;
  public final UnsignedIntRef ui_38;
  public final UnsignedIntRef ui_3c;
  public final UnsignedIntRef ui_40;
  public final UnsignedIntRef ui_44;
  public final UnsignedIntRef ui_48;
  public final UnsignedIntRef ui_4c;
  public final UnsignedIntRef ui_50;
  public final UnsignedIntRef ui_54;
  public final UnsignedIntRef ui_58;
  public final UnsignedIntRef ui_5c;
  public final UnsignedIntRef ui_60;
  public final UnsignedIntRef ui_64;
  public final UnsignedIntRef ui_68;
  public final UnsignedIntRef ui_6c;
  public final UnsignedIntRef ui_70;
  public final UnsignedIntRef ui_74;
  public final UnsignedIntRef ui_78;
  public final UnsignedIntRef ui_7c;
  public final UnsignedIntRef ui_80;
  public final UnsignedIntRef ui_84;
  public final UnsignedIntRef ui_88;
  public final UnsignedIntRef ui_8c;
  public final UnsignedIntRef ui_90;
  public final UnsignedIntRef ui_94;
  public final UnsignedIntRef ui_98;
  public final UnsignedIntRef ui_9c;
  public final UnsignedIntRef ui_a0;
  public final UnsignedIntRef ui_a4;
  public final UnsignedIntRef ui_a8;
  public final UnsignedIntRef ui_ac;

  public final UnsignedIntRef ui_f8;
  public final UnsignedIntRef ui_fc;
  public final T innerStruct_100;

  public BiggerStruct(final Value ref, final Function<Value, T> innerStructConstructor) {
    this.ref = ref;

    this.innerStruct_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, innerStructConstructor));
    this.ui_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.ui_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.ui_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.ui_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.ui_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this.ui_18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.ui_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.ui_20 = ref.offset(4, 0x20L).cast(UnsignedIntRef::new);
    this.ui_24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
    this.ui_28 = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);
    this.ui_2c = ref.offset(4, 0x2cL).cast(UnsignedIntRef::new);
    this.ui_30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this.ui_34 = ref.offset(4, 0x34L).cast(UnsignedIntRef::new);
    this.ui_38 = ref.offset(4, 0x38L).cast(UnsignedIntRef::new);
    this.ui_3c = ref.offset(4, 0x3cL).cast(UnsignedIntRef::new);
    this.ui_40 = ref.offset(4, 0x40L).cast(UnsignedIntRef::new);
    this.ui_44 = ref.offset(4, 0x44L).cast(UnsignedIntRef::new);
    this.ui_48 = ref.offset(4, 0x48L).cast(UnsignedIntRef::new);
    this.ui_4c = ref.offset(4, 0x4cL).cast(UnsignedIntRef::new);
    this.ui_50 = ref.offset(4, 0x50L).cast(UnsignedIntRef::new);
    this.ui_54 = ref.offset(4, 0x54L).cast(UnsignedIntRef::new);
    this.ui_58 = ref.offset(4, 0x58L).cast(UnsignedIntRef::new);
    this.ui_5c = ref.offset(4, 0x5cL).cast(UnsignedIntRef::new);
    this.ui_60 = ref.offset(4, 0x60L).cast(UnsignedIntRef::new);
    this.ui_64 = ref.offset(4, 0x64L).cast(UnsignedIntRef::new);
    this.ui_68 = ref.offset(4, 0x68L).cast(UnsignedIntRef::new);
    this.ui_6c = ref.offset(4, 0x6cL).cast(UnsignedIntRef::new);
    this.ui_70 = ref.offset(4, 0x70L).cast(UnsignedIntRef::new);
    this.ui_74 = ref.offset(4, 0x74L).cast(UnsignedIntRef::new);
    this.ui_78 = ref.offset(4, 0x78L).cast(UnsignedIntRef::new);
    this.ui_7c = ref.offset(4, 0x7cL).cast(UnsignedIntRef::new);
    this.ui_80 = ref.offset(4, 0x80L).cast(UnsignedIntRef::new);
    this.ui_84 = ref.offset(4, 0x84L).cast(UnsignedIntRef::new);
    this.ui_88 = ref.offset(4, 0x88L).cast(UnsignedIntRef::new);
    this.ui_8c = ref.offset(4, 0x8cL).cast(UnsignedIntRef::new);
    this.ui_90 = ref.offset(4, 0x90L).cast(UnsignedIntRef::new);
    this.ui_94 = ref.offset(4, 0x94L).cast(UnsignedIntRef::new);
    this.ui_98 = ref.offset(4, 0x98L).cast(UnsignedIntRef::new);
    this.ui_9c = ref.offset(4, 0x9cL).cast(UnsignedIntRef::new);
    this.ui_a0 = ref.offset(4, 0xa0L).cast(UnsignedIntRef::new);
    this.ui_a4 = ref.offset(4, 0xa4L).cast(UnsignedIntRef::new);
    this.ui_a8 = ref.offset(4, 0xa8L).cast(UnsignedIntRef::new);
    this.ui_ac = ref.offset(4, 0xacL).cast(UnsignedIntRef::new);

    this.ui_f8 = ref.offset(4, 0xf8L).cast(UnsignedIntRef::new);
    this.ui_fc = ref.offset(4, 0xfcL).cast(UnsignedIntRef::new);
    this.innerStruct_100 = ref.offset(4, 0x100L).cast(innerStructConstructor);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
