package legend.game.unpacker;

import legend.core.MathHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedFileData extends FileData {
  private static final List<RandomAccessFile> closeMe = new ArrayList<>();

  public static void closeAll() {
    for(final RandomAccessFile file : closeMe) {
      try {
        file.close();
      } catch(final IOException e) {
        e.printStackTrace();
      }
    }

    closeMe.clear();
  }

  private final RandomAccessFile file;
  private final Path path;
  private final int offset;
  private final int size;

  private final byte[] buffer = new byte[4];

  public FileBackedFileData(final Path path, final int offset, final int size) throws FileNotFoundException {
    super(null, offset, size);
    this.file = new RandomAccessFile(path.toFile(), "rw");
    this.path = path;
    this.offset = offset;
    this.size = size;
    closeMe.add(this.file);
  }

  public FileBackedFileData(final FileBackedFileData data, final int offset, final int size) {
    super(null, offset, size);
    this.file = data.file;
    this.path = data.path;
    this.offset = offset;
    this.size = size;
  }

  @Override
  public FileData slice(final int offset, final int size) {
    return new FileBackedFileData(this, this.offset + offset, size);
  }

  @Override
  public byte[] getBytes() {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset);
        final byte[] data = new byte[this.size];
        this.file.read(data);
        return data;
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void read(final int srcOffset, final byte[] dest, final int destOffset, final int size) {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset + srcOffset);
        this.file.read(dest, destOffset, size);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void write(final int srcOffset, final byte[] src, final int destOffset, final int size) {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset + destOffset);
        this.file.write(src, srcOffset, size);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public byte readByte(final int offset) {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset + offset);
        return this.file.readByte();
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void writeByte(final int offset, final int val) {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset + offset);
        this.file.write(val);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public short readShort(final int offset) {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset + offset);
        this.file.read(this.buffer, 0, 2);
        return MathHelper.getShort(this.buffer, 0);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void writeShort(final int offset, final int val) {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset + offset);
        MathHelper.set(this.buffer, 0, 2, val);
        this.file.write(this.buffer, 0, 2);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public int readInt(final int offset) {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset + offset);
        this.file.read(this.buffer, 0, 4);
        return MathHelper.getInt(this.buffer, 0);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void writeInt(final int offset, final int val) {
    synchronized(this.file) {
      try {
        this.file.seek(this.offset + offset);
        MathHelper.set(this.buffer, 0, 4, val);
        this.file.write(this.buffer, 0, 4);
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void writeAscii(final int offset, final String val, final int lengthSize) {
    throw new RuntimeException("No can do");
  }

  @Override
  public String readAscii(final int offset, final int lengthSize) {
    throw new RuntimeException("No can do");
  }

  @Override
  public String readFixedLengthAscii(final int offset, final int length) {
    throw new RuntimeException("No can do");
  }

  @Override
  public void write(final OutputStream out) throws IOException {
    out.write(this.getBytes());
  }
}
