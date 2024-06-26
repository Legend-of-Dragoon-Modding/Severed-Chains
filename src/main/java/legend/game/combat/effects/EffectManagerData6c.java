package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.function.BiFunction;

public class EffectManagerData6c<T extends EffectManagerParams<T>> extends BattleObject implements AttachmentHost {
  public final String name;

  /** The first 11 (or more?) bits denote which attachments this effect has */
  public int flags_04;
  public int scriptIndex_0c;
  public int coord2Index_0d;
  public ScriptState<EffectManagerData6c<T>> myScriptState_0e;

  public final T params_10;
  public Effect<T> effect_44;
//  public BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> ticker_48;
//  public BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> destructor_4c;
  public ScriptState<EffectManagerData6c<?>> parentScript_50;
  public ScriptState<EffectManagerData6c<?>> childScript_52;
  /** If replacing a child, this is the old child's ID */
  public ScriptState<EffectManagerData6c<?>> oldChildScript_54;
  /** If replaced as a child, this is the new child's ID */
  public ScriptState<EffectManagerData6c<?>> newChildScript_56;
  /** A linked list of attachments */
  private EffectAttachment attachment_58;
  //  public String type_5c; Equivalent to "name" above

  public static <T extends EffectManagerParams<T>> Class<EffectManagerData6c<T>> classFor(final Class<T> cls) {
    return (Class<EffectManagerData6c<T>>)(Class<?>)EffectManagerData6c.class;
  }

  public EffectManagerData6c(final String name, final T params) {
    super(BattleObject.EM__);
    this.name = name;
    this.params_10 = params;
  }

  public void set(final EffectManagerData6c<T> other) {
    this.flags_04 = other.flags_04;
    this.scriptIndex_0c = other.scriptIndex_0c;
    this.coord2Index_0d = other.coord2Index_0d;
    this.myScriptState_0e = other.myScriptState_0e;
    this.params_10.set(other.params_10);
    this.effect_44 = other.effect_44;
    this.parentScript_50 = other.parentScript_50;
    this.childScript_52 = other.childScript_52;
    this.oldChildScript_54 = other.oldChildScript_54;
    this.newChildScript_56 = other.newChildScript_56;
    this.attachment_58 = other.attachment_58;
  }

  @Override
  public Vector3f getPosition() {
    return this.params_10.trans_04;
  }

  @Override
  public Vector3f getRotation() {
    return this.params_10.rot_10;
  }

  @Override
  public Vector3f getScale() {
    return this.params_10.scale_16;
  }

  @Override
  public Vector3i getColour() {
    return this.params_10.colour_1c;
  }

  public boolean hasAttachment(final int id) {
    return (this.flags_04 & 0x1 << id) != 0;
  }

  @Override
  public EffectAttachment getAttachment() {
    return this.attachment_58;
  }

  @Override
  public void setAttachment(final EffectAttachment attachment) {
    this.attachment_58 = attachment;
  }

  @Method(0x800e8dd4L)
  public <Attachment extends EffectAttachment> Attachment addAttachment(final int id, final int a2, final BiFunction<EffectManagerData6c<T>, Attachment, Integer> ticker, final Attachment attachment) {
    attachment.id_05 = id;
    attachment._06 = (short)a2;
    attachment.ticker_08 = (BiFunction)ticker;
    attachment.parent_00 = this.attachment_58;
    this.attachment_58 = attachment;
    this.flags_04 |= 0x1 << id;
    return attachment;
  }

  @Method(0x800e8c84L)
  public EffectAttachment findAttachment(final int id) {
    EffectAttachment attachment = this.attachment_58;

    while(attachment != null) {
      if(attachment.id_05 == id) {
        return attachment;
      }

      attachment = attachment.parent_00;
    }

    return null;
  }

  @Method(0x800e8d04L)
  public void removeAttachment(final int id) {
    AttachmentHost current = this;

    while(current.getAttachment() != null) {
      final EffectAttachment attachment = current.getAttachment();

      if(attachment.id_05 == id) {
        this.flags_04 &= ~(0x1 << id);
        current.setAttachment(attachment.getAttachment());
      } else {
        current = attachment;
      }
    }
  }
}
