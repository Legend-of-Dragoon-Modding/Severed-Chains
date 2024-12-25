package legend.game;

import legend.core.GameEngine;
import legend.core.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public final class Main {
  public static final Locale ORIGINAL_LOCALE = Locale.getDefault();

  static {
    System.setProperty("log4j.skipJansi", "false");
    System.setProperty("log4j2.configurationFile", "log4j2.xml");

    Locale.setDefault(Locale.US);
  }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Main.class);

  private Main() { }

  public static void main(final String[] args) {
    try {
      GameEngine.start();
    } catch(final Throwable e) {
      final List<String> messages = new ArrayList<>();
      getExceptionMessages(e, messages);

      LOGGER.error("----------------------------------------------------------------------------");
      LOGGER.error("Crash detected");
      LOGGER.error("Severed Chains %s commit %s", Version.VERSION, Version.HASH);

      for(final String message : messages) {
        LOGGER.error("Message: %s", message);
      }

      LOGGER.error("----------------------------------------------------------------------------");
      LOGGER.error("Stack trace:", e);
      LOGGER.error("Press any key to shut down...");

      final Scanner scanner = new Scanner(System.in);
      scanner.nextLine();
      System.exit(1);
    }
  }

  private static void getExceptionMessages(final Throwable e, final List<String> messages) {
    if(e.getMessage() != null) {
      messages.add(e.getMessage());
    }

    if(e.getCause() != null) {
      getExceptionMessages(e.getCause(), messages);
    }
  }
}
