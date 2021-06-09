package legend.game;

import legend.core.memory.EntryPoint;
import legend.core.memory.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EntryPoint
public final class Scus94491 {
  private Scus94491() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491.class);

  @Method(0x801bf4e8L)
  public static void start(final long param1, final long param2) {
    LOGGER.info("--- SCUS 94491 start! ---");
  }
}
