package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class RegistryIdTag implements Tag {
  private RegistryId val;

  public RegistryIdTag(final RegistryId val) {
    this.set(val);
  }

  public RegistryIdTag(final RegistryEntry val) {
    this.set(val);
  }

  public RegistryIdTag(final RegistryDelegate<?> val) {
    this.set(val);
  }

  public RegistryIdTag() {
  }

  public RegistryId get() {
    return this.val;
  }

  public void set(final RegistryId val) {
    this.val = val;
  }

  public void set(final RegistryEntry val) {
    this.val = val.getRegistryId();
  }

  public void set(final RegistryDelegate<?> val) {
    this.val = val.getId();
  }

  @Override
  public int getType() {
    return TAG_TYPE_REGISTRY_ID;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeRegistryId(offset, this.val);
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    this.val = data.readRegistryId(offset);
  }

  @Override
  public RegistryIdTag clone() {
    return new RegistryIdTag(this.val);
  }

  @Override
  public String toString() {
    return "id:" + this.val;
  }
}
