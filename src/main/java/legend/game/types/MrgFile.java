package legend.game.types;

import legend.core.MathHelper;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.unpacker.FileData;

import java.util.List;
import java.util.function.Function;

import static legend.core.GameEngine.MEMORY;
import static legend.game.Scus94491BpeSegment.mallocTail;

public class MrgFile implements MemoryRef {
  public static MrgFile alloc(final List<FileData> files) {
    return alloc(files, files.size());
  }

  public static MrgFile alloc(final List<FileData> files, final int fileCount) {
    final int headerSize = 8 + fileCount * 8;

    int totalSize = headerSize;
    for(int i = 0; i < fileCount; i++) {
      final FileData file = files.get(i);
      totalSize += MathHelper.roundUp(file.size(), 4);
    }

    final MrgFile mrg = MEMORY.ref(4, mallocTail(totalSize), MrgFile::new);
    mrg.count.set(fileCount);

    int offset = headerSize;
    for(int i = 0; i < fileCount; i++) {
      final FileData file = files.get(i);
      mrg.entries.get(i).offset.set(offset);
      mrg.entries.get(i).size.set(file.size());
      MEMORY.setBytes(mrg.getFile(i), file.getBytes());
      offset += MathHelper.roundUp(file.size(), 4);
    }

    return mrg;
  }

  private final Value ref;

  public final UnsignedIntRef magic;
  public final IntRef count;
  public final UnboundedArrayRef<MrgEntry> entries;

  public MrgFile(final Value ref) {
    this.ref = ref;

    this.magic = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.count = ref.offset(4, 0x4L).cast(IntRef::new);
    this.entries = ref.offset(4, 0x8L).cast(UnboundedArrayRef.of(8, MrgEntry::new, this.count::get));
  }

  public <T extends MemoryRef> T getFile(final int index, final Function<Value, T> constructor) {
    return this.ref.offset(4, this.entries.get(index).offset.get()).cast(constructor);
  }

  public long getFile(final int index) {
    return this.ref.offset(4, this.entries.get(index).offset.get()).getAddress();
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
