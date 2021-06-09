package legend.game;

import legend.core.Hardware;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;

public final class Main {
  static {
    PluginManager.addPackage("legend");
  }

  private Main() { }

  public static void main(final String[] args) {
    Hardware.start();
  }
}
