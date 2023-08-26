package legend.game.combat.effects;

import legend.core.memory.Method;

import java.util.function.BiFunction;

public abstract class EffectAttachment implements AttachmentHost {
  protected EffectAttachment parent_00;
//  /** ubyte */
//  public int size_04;
  public int id_05;
  public short _06;
  public BiFunction<EffectManagerData6c<?>, ? extends EffectAttachment, Integer> ticker_08;

  @Method(0x800e8cc8L)
  public EffectAttachment findAttachment(final int id) {
    EffectAttachment attachment = this.parent_00;

    while(attachment != null) {
      if(attachment.id_05 == id) {
        return attachment;
      }

      attachment = attachment.parent_00;
    }

    return null;
  }

  @Override
  public EffectAttachment getAttachment() {
    return this.parent_00;
  }

  @Override
  public void setAttachment(final EffectAttachment attachment) {
    this.parent_00 = attachment;
  }
}
