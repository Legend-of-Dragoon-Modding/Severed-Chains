package legend.core;

import legend.core.gte.Gte;

public class Cpu {
  private final Gte gte = new Gte();

  /**
   * Stores val in GTE control register
   *
   * Register is (cmd >>> 11) &amp; 0x1f
   */
  public void CTC2(final long val, final long register) {
    this.gte.writeControl((int)register, (int)val);
  }

  /**
   * Stores val in GTE data register
   *
   * Register is (cmd >>> 11) &amp; 0x1f
   */
  public void MTC2(final long val, final long register) {
    this.gte.writeData((int)register, (int)val);
  }

  /**
   * Returns value in GTE data register
   *
   * Return moved from a0 to return
   *
   * Register is (cmd >>> 11) &amp; 0x1f
   */
  public long MFC2(final long register) {
    return this.gte.loadData((int)register);
  }

  /**
   * Returns value in GTE control register
   *
   * Return moved from a0 to return
   *
   * Register is (cmd >>> 11) &amp; 0x1f
   */
  public long CFC2(final long register) {
    return this.gte.loadControl((int)register);
  }

  /** Use {@link Cpu#MTC2} */
  @Deprecated
  public void LWC2() {
    assert false;
  }

  /** Use {@link Cpu#MFC2} */
  @Deprecated
  public void SWC2() {
    assert false;
  }

  public long COP2(final long cmd) {
    this.gte.execute((int)cmd);
    return 0;
  }
}
