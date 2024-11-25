package legend.game;

import com.sun.jna.FromNativeContext;
import com.sun.jna.IntegerType;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Uses JNA to enable ANSI colours in the command prompt
 */

public final class MainWindows {
  private MainWindows() { }

  public static void main(final String[] args) {
    try {
      if(System.getProperty("os.name").startsWith("Windows")) {
        // Set output mode to handle virtual terminal sequences
        final Kernel32.DWORD STD_OUTPUT_HANDLE = new Kernel32.DWORD(-11);
        final Kernel32.HANDLE hOut = Kernel32.INSTANCE.GetStdHandle(STD_OUTPUT_HANDLE);

        final Kernel32.DWORDByReference p_dwMode = new Kernel32.DWORDByReference(new Kernel32.DWORD(0));
        Kernel32.INSTANCE.GetConsoleMode(hOut, p_dwMode);

        final int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;
        final Kernel32.DWORD dwMode = p_dwMode.getValue();
        dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
        Kernel32.INSTANCE.SetConsoleMode(hOut, dwMode);
      }
    } catch(final Throwable t) {
      System.err.println("Failed to enable colours in Windows terminal");
      t.printStackTrace();
    }

    Main.main(args);
  }

  public interface Kernel32 extends StdCallLibrary {
    Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

    HANDLE GetStdHandle(DWORD spec);
    BOOL GetConsoleMode(HANDLE handle, DWORDByReference mode);
    BOOL SetConsoleMode(HANDLE handle, DWORD mode);

    HANDLE INVALID_HANDLE_VALUE = new HANDLE(Pointer.createConstant(Native.POINTER_SIZE == 8 ? -1 : 0xffffffffL));

    class WORD extends IntegerType implements Comparable<WORD> {
      public static final int SIZE = 2;

      public WORD() {
        this(0);
      }

      public WORD(final long value) {
        super(SIZE, value, true);
      }

      @Override
      public int compareTo(final WORD other) {
        return compare(this, other);
      }
    }

    class DWORD extends IntegerType implements Comparable<DWORD> {
      public static final int SIZE = 4;

      public DWORD() {
        this(0);
      }

      public DWORD(final long value) {
        super(SIZE, value, true);
      }

      public WORD getLow() {
        return new WORD(this.longValue() & 0xFFFF);
      }

      public WORD getHigh() {
        return new WORD((this.longValue() >> 16) & 0xFFFF);
      }

      @Override
      public int compareTo(final DWORD other) {
        return compare(this, other);
      }
    }

    class DWORDByReference extends ByReference {
      public DWORDByReference() {
        this(new DWORD(0));
      }

      public DWORDByReference(final DWORD value) {
        super(DWORD.SIZE);
        this.setValue(value);
      }

      public void setValue(final DWORD value) {
        this.getPointer().setInt(0, value.intValue());
      }

      public DWORD getValue() {
        return new DWORD(this.getPointer().getInt(0));
      }
    }

    class BOOL extends IntegerType implements Comparable<BOOL> {
      public static final int SIZE = 4;

      public BOOL() {
        this(0);
      }

      public BOOL(final boolean value) {
        this(value ? 1L : 0L);
      }

      public BOOL(final long value) {
        super(SIZE, value, false);
        assert value == 0 || value == 1;
      }

      public boolean booleanValue() {
        return this.intValue() > 0;
      }

      @Override
      public String toString() {
        return Boolean.toString(this.booleanValue());
      }

      @Override
      public int compareTo(final BOOL other) {
        return compare(this, other);
      }

      public static int compare(final BOOL v1, final BOOL v2) {
        if(v1 == v2) {
          return 0;
        } else if(v1 == null) {
          return 1;   // v2 cannot be null or v1 == v2 would hold
        } else if(v2 == null) {
          return (-1);
        } else {
          return compare(v1.booleanValue(), v2.booleanValue());
        }
      }

      public static int compare(final BOOL v1, final boolean v2) {
        if(v1 == null) {
          return 1;
        } else {
          return compare(v1.booleanValue(), v2);
        }
      }

      public static int compare(final boolean v1, final boolean v2) {
        if(v1 == v2) {
          return 0;
        } else if(v1) {
          return 1;   // v2 cannot be true or v1 == v2
        } else {
          return (-1);
        }
      }
    }

    class HANDLE extends PointerType {
      private boolean immutable;

      public HANDLE() {
      }

      public HANDLE(final Pointer p) {
        this.setPointer(p);
        this.immutable = true;
      }

      @Override
      public Object fromNative(final Object nativeValue, final FromNativeContext context) {
        final Object o = super.fromNative(nativeValue, context);
        if(INVALID_HANDLE_VALUE.equals(o)) {
          return INVALID_HANDLE_VALUE;
        }
        return o;
      }

      @Override
      public void setPointer(final Pointer p) {
        if(this.immutable) {
          throw new UnsupportedOperationException("immutable reference");
        }

        super.setPointer(p);
      }

      @Override
      public String toString() {
        return String.valueOf(this.getPointer());
      }
    }
  }
}
