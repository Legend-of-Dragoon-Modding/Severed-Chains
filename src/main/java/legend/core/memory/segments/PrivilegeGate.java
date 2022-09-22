package legend.core.memory.segments;

import legend.core.memory.PrivilegeNotAcquiredException;
import legend.core.memory.Segment;
import legend.core.memory.segments.PrivilegedSegment;

public class PrivilegeGate {
  private final Object lock = new Object();
  private int acquisitions;

  public void acquire() {
    synchronized(this.lock) {
      this.acquisitions++;
    }
  }

  public void release() {
    synchronized(this.lock) {
      this.acquisitions--;

      if(this.acquisitions < 0) {
        throw new RuntimeException("Privilege gate released more times than acquired");
      }
    }
  }

  public void test(final long address) {
    synchronized(this.lock) {
      if(this.acquisitions == 0) {
        throw new PrivilegeNotAcquiredException("Attempted to access privileged memory 0x" + Long.toHexString(address));
      }
    }
  }

  public Segment wrap(final Segment segment) {
    return new PrivilegedSegment(segment, this, false);
  }

  public Segment readonly(final Segment segment) {
    return new PrivilegedSegment(segment, this, true);
  }
}
