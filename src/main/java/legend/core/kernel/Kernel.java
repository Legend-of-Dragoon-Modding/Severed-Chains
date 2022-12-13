package legend.core.kernel;

import legend.core.memory.Method;
import legend.core.memory.Value;

import static legend.core.Hardware.MEMORY;
import static legend.core.kernel.Bios.dev_cd_open_Impl_A5f;
import static legend.core.kernel.Bios.dev_cd_read_Impl_A60;
import static legend.core.kernel.Bios.memcpy_Impl_A2a;
import static legend.core.kernel.Bios.strcmp_Impl_A17;

public final class Kernel {
  private Kernel() { }

  private static final Value systemMemoryInitialized_00006d30 = MEMORY.ref(4, 0x00006d30L);

  private static final Value systemMemoryAddr_00007460 = MEMORY.ref(4, 0x00007460L);
  private static final Value systemMemorySize_00007464 = MEMORY.ref(4, 0x00007464L);
  private static final Value systemMemoryEnd_00007468 = MEMORY.ref(4, 0x00007468L);

  private static final Value _000085f8 = MEMORY.ref(4, 0x000085f8L);
  private static final Value _000085fc = MEMORY.ref(4, 0x000085fcL);

  private static final Value _00008640 = MEMORY.ref(4, 0x00008640L);
  private static final Value FileControlBlockBaseAddr_00008648 = MEMORY.ref(1, 0x00008648L);

  @Method(0x1030L)
  public static long FUN_00001030(long a0) {
    final long a2;

    if(a0 == 0) {
      //LAB_000010b8
      a0 = systemMemoryAddr_00007460.get();
      final long t7 = systemMemoryEnd_00007468.get();
      a2 = 0;
      if(a0 >= t7) {
        return -0x1L;
      }
    } else {
      long v0 = systemMemorySize_00007464.get();

      //LAB_00001040
      final long a1;
      final long v1;
      if(v0 >= a0) {
        v1 = v0;
        a1 = a0;
      } else {
        //LAB_00001060
        v1 = a0;
        a1 = v0;
      }

      //LAB_00001064
      a0 = systemMemoryAddr_00007460.get();

      //LAB_00001070
      v0 = systemMemoryEnd_00007468.get() - a0;
      v0 &= ~0x3L;
      v0 -= 0x4L;
      if(v0 < v1) {
        //LAB_000010a0
        if(v0 >= a1) {
          a2 = a1;
        } else {
          //LAB_000010b0
          return -0x1L;
        }
      } else {
        a2 = v1;
      }
    }

    //LAB_000010e4
    final long t3 = a2 + 0x4L;
    final long t2 = a2 | 0x1L;
    long at = a2;
    if((int)a2 < 0) {
      at += 0x3L;
    }

    //LAB_00001100
    final long t0 = at & ~0x3L;
    final long t1 = a0 + t0;
    MEMORY.ref(4, t1).setu(-0x2L);
    MEMORY.ref(4, a0).offset(-0x4L).setu(t2);
    at = t3;
    if((int)t3 < 0) {
      at += 0x3L;
    }

    //LAB_00001120
    systemMemoryAddr_00007460.setu(a0 + at & ~0x3L);
    return 0;
  }

  @Method(0x113cL)
  public static void SysInitMemory_Impl_C08(final long addr, final int size) {
    systemMemoryAddr_00007460.setu(addr);
    systemMemorySize_00007464.setu(size);
    systemMemoryEnd_00007468.setu(addr + (size & 0xfffffffcL) + 0x4L);

    MEMORY.ref(4, addr).setu(0);

    systemMemoryInitialized_00006d30.setu(0);
  }

  @Method(0x1174L)
  public static long alloc_kernel_memory_Impl_B00(final int size) {
    long v0;

    final long s1 = size + 3 & 0xfffffffcL;
    long t6 = systemMemoryInitialized_00006d30.get();
    long s0 = 0x2L;
    if(t6 == 0) {
      //LAB_000011b8
      v0 = systemMemoryAddr_00007460.get();
      if(v0 >= systemMemoryEnd_00007468.get()) {
        return 0;
      }

      //LAB_000011e4
      MEMORY.ref(4, v0).setu(0xfffffffeL);
      _000085fc.setu(v0);
      v0 += 0x4L;
      systemMemoryAddr_00007460.setu(v0);
      if(FUN_00001030(s1) != 0) {
        return 0;
      }

      //LAB_00001214
      _000085f8.setu(_000085fc);
      systemMemoryInitialized_00006d30.setu(0x1L);
    }

    //LAB_00001230
    long a0 = _000085fc.deref(4).get();
    long v1 = a0 & 0x1L;

    //LAB_00001248
    do {
      if(v1 != 0) {
        v0 = s1 | 0x1L;
        if(v0 == a0) {
          _000085fc.deref(4).setu(a0 & 0xfffffffeL);
          break;
        }

        //LAB_00001274
        if(v0 < a0) {
          long at = s1;
          if(s1 < 0) {
            at += 0x3L;
          }

          //LAB_0000129c
          _000085fc.deref(4).offset(at & ~0x3L).offset(0x4L).setu(a0 - s1 - 0x4L);
          _000085fc.deref(4).setu(s1);
          break;
        }

        //LAB_000012bc
        if(a0 >= s1) {
          continue;
        }

        v1 = _000085fc.get() + (a0 & ~0x3L);
        v0 = MEMORY.ref(4, v1).offset(0x4L).get();
        v1 += 0x4L;
        t6 = v0 & 0x1L;
        if(v0 == 0xfffffffeL) {
          s0--;
          if(s0 <= 0) {
            v0 = FUN_00001030(s1);
            if(v0 != 0) {
              return 0;
            }
          } else {
            //LAB_0000130c
            _000085fc.setu(_000085f8);
          }
        } else {
          //LAB_00001324
          if(t6 == 0) {
            //LAB_00001348
            _000085fc.setu(v1);
          } else {
            //LAB_0000132c
            _000085fc.deref(4).setu(a0 + (v0 & 0xfffffffeL) + 0x4L);
          }
        }

        //LAB_00001350
        a0 = _000085fc.deref(4).get();
        v1 = a0 & 0x1L;
        continue;
      }

      //LAB_00001368
      if(a0 == 0xfffffffeL) {
        //LAB_00001394
        s0--;
        if(s0 <= 0) {
          v0 = FUN_00001030(s1);
          if(v0 != 0) {
            return v0;
          }
        } else {
          //LAB_000013b8
          _000085fc.setu(_000085f8);
        }
      } else {
        _000085fc.addu((a0 & ~0x3L) + 0x4L);
      }

      //LAB_000013c8
      a0 = _000085fc.deref(4).get();
      v1 = a0 & 0x1L;
    } while(true);

    //LAB_000013e0
    //LAB_000013f0
    //LAB_00001400
    return _000085fc.get() + 0x4L;
  }

  @Method(0x2958L)
  public static int FileOpen_Impl_B32(final String filename, final int mode) {
    final long fcb = FUN_00003060();

    if(fcb == 0) {
      _00008640.setu(0x18L);
      return -1;
    }

    //LAB_00002988
    //LAB_000029c4
    MEMORY.ref(4, fcb).setu(mode);

    if(dev_cd_open_Impl_A5f(fcb, filename, mode) != 0) {
      throw new RuntimeException("Failed to open file " + filename);
    }

    //LAB_00002a30
    final long v1 = (fcb - FileControlBlockBaseAddr_00008648.getAddress()) / 0x2cL;
    MEMORY.ref(4, fcb).offset(0x10L).setu(0);

    //LAB_00002a54
    return (int)v1;
  }

  @Method(0x2b28L)
  public static int FileRead_Impl_B34(final int fd, final long dest, final int length) {
    final long fcb = getFcb(fd);
    if(fcb == 0 || MEMORY.ref(4, fcb).get() == 0) {
      //LAB_00002b54
      _00008640.setu(0x9L);
      return -1;
    }

    //LAB_00002b68
    final int ret = dev_cd_read_Impl_A60(fcb, dest, length);

    //LAB_00002c6c
    if(ret < 0) {
      _00008640.setu(MEMORY.ref(4, fcb).offset(0x18L));
    }

    //LAB_00002c84
    return ret;
  }

  @Method(0x2e00L)
  public static int FileClose_Impl_B36(final int fd) {
    final long v0 = getFcb(fd);
    if(v0 == 0 || MEMORY.ref(4, v0).get() == 0) {
      //LAB_00002e30
      _00008640.setu(0x9L);
      return -1;
    }

    //LAB_00002e44;
    MEMORY.ref(4, v0).setu(0);

    //LAB_00002e80
    return fd;
  }

  @Method(0x3060L)
  public static long FUN_00003060() {
    long fcb = FileControlBlockBaseAddr_00008648.getAddress();

    //LAB_00003078
    do {
      if(MEMORY.ref(4, fcb).get() == 0) {
        return fcb;
      }

      //LAB_00003090
      fcb += 0x2cL;
    } while(fcb < 0x8908L);

    throw new RuntimeException("Out of file descriptors");
  }

  @Method(0x30c8L)
  public static long getFcb(final int fd) {
    if(fd < 0 || fd >= 0x10L) {
      return 0;
    }

    return FileControlBlockBaseAddr_00008648.offset(fd * 0x2cL).getAddress();
  }

  @Method(0x6b10L)
  public static int strcmp(final String str1, final String str2) {
    return strcmp_Impl_A17(str1, str2);
  }

  @Method(0x6b20L)
  public static long memcpy(final long dst, final long src, final int len) {
    return memcpy_Impl_A2a(dst, src, len);
  }
}
