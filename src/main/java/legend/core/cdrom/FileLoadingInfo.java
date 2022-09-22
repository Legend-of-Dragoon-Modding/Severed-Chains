package legend.core.cdrom;

import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;

public class FileLoadingInfo implements MemoryRef {
  private final Value ref;

  /**
   * 0x0 - 4 bytes
   */
  public final CdlLOC pos;
  /**
   * 0x4 - 4 bytes
   */
  public final IntRef size;
  /**
   * 0x8 - 4 bytes
   */
  public final Pointer<TriConsumerRef<Long, Long, Long>> callback;
  /**
   * 0xc - 4 bytes
   */
  public final Value transferDest;
  /**
   * 0x10 - 4 bytes
   */
  public final Pointer<CString> namePtr;
  /**
   * 0x14 - 4 bytes - passed as the third param to the callback
   */
  public final IntRef callbackParam;
  /**
   * 0x18 - 2 bytes
   */
  public final ShortRef unknown2;
  /**
   * 0x1a - 2 bytes
   *
   * 0 - transfer to transferDest
   * 1 - compressed/allocate on linked list head
   * 2 - allocate on linked list tail
   * 3 - allocate on linked list tail (does something weird)
   * 4 - allocate on linked list head
   * 5 - allocate on linked list head (does something weird)
   */
  public ShortRef type;
  /**
   * 0x1c - 1 byte
   */
  public BoolRef used;

  public FileLoadingInfo(final Value ref) {
    this.ref = ref;
    this.pos = new CdlLOC(ref.offset(4, 0x0L));
    this.size = ref.offset(4, 0x4L).cast(IntRef::new);
    this.callback = ref.offset(4, 0x8L).cast(Pointer.deferred(4, TriConsumerRef::new));
    this.transferDest = ref.offset(4, 0xcL);
    this.namePtr = ref.offset(4, 0x10L).cast(Pointer.deferred(20, CString.maxLength(20)));
    this.callbackParam = ref.offset(4, 0x14L).cast(IntRef::new);
    this.unknown2 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this.type = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this.used = ref.offset(1, 0x1cL).cast(BoolRef::new);
  }

  public void set(final FileLoadingInfo other) {
    this.pos.set(other.pos);
    this.size.set(other.size);
    this.callback.setNullable(other.callback.derefNullable());
    this.transferDest.setu(other.transferDest);
    this.namePtr.set(other.namePtr.deref());
    this.callbackParam.set(other.callbackParam);
    this.unknown2.set(other.unknown2);
    this.type.set(other.type);
    this.used.set(other.used);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }

  @Override
  public String toString() {
    if(!this.used.get()) {
      return "FileLoadingInfo: unused";
    }

    if(this.namePtr.isNull()) {
      return "FileLoadingInfo: no name";
    }

    return "FileLoadingInfo {name: " + this.namePtr.deref().get() + ", pos: " + this.pos + ", size: " + this.size.get() + ", transfer dest: " + Long.toString(this.transferDest.get(), 16) + ", callback: " + Long.toString(this.callback.getPointer(), 16) + ", type: " + this.type.get() + '}';
  }
}
