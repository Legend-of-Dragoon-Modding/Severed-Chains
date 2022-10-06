package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.Model124;

public class BttlScriptData6cSub13c extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _00;
  /** TODO */
  public final UnsignedIntRef part_04;
  /** TODO */
  public final UnsignedIntRef ptr_08;
  /** TODO */
  public final UnsignedIntRef ptr_0c;
  public final Model124 _10;
  public final Pointer<Model124> _134;

  public BttlScriptData6cSub13c(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.part_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.ptr_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.ptr_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(Model124::new);
    this._134 = ref.offset(4, 0x134L).cast(Pointer.deferred(4, Model124::new));
  }
}
