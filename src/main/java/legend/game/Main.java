package legend.game;

import legend.core.GameEngine;
import legend.game.modding.events.EventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Main {
  static {
    System.setProperty("log4j.skipJansi", "false");
    System.setProperty("log4j2.configurationFile", "log4j2.xml");
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
