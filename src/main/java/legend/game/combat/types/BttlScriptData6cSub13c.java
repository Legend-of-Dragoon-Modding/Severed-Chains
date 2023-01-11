package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.DeffPart;
import legend.game.types.ExtendedTmd;
import legend.game.types.Model124;

public class BttlScriptData6cSub13c extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _00;
  /** Can be LMB or CMB subtype */
  public final Pointer<DeffPart.TmdType> tmdType_04;
  public final Pointer<ExtendedTmd> extTmd_08;
  public final Pointer<Anim> anim_0c;
  public final Model124 model_10;
  public final Pointer<Model124> model_134;

  public BttlScriptData6cSub13c(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.tmdType_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, DeffPart.TmdType::new));
    this.extTmd_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, ExtendedTmd::new));
    this.anim_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, value -> { throw new RuntimeException("Can't be instantiated"); }));
    this.model_10 = ref.offset(4, 0x10L).cast(Model124::new);
    this.model_134 = ref.offset(4, 0x134L).cast(Pointer.deferred(4, Model124::new));
  }
}
