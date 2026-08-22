package legend.core.renderer;

public enum BufferUsage {
  /** Updated and used once */
  STREAMING,
  /** Updated once, used many times */
  STATIC,
  /** Updated and used many times */
  DYNAMIC,
}
