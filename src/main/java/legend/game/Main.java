package legend.game;

import legend.core.Hardware;
import legend.game.modding.events.EventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Main {
  static {
    System.setProperty("log4j.skipJansi", "false");
    PluginManager.addPackage("legend");
  }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Main.class);

  private Main() { }

  public static void main(final String[] args) {
    if(System.getProperty("os.name").startsWith("Windows")) {
      // Set output mode to handle virtual terminal sequences
      final MainWindows.Kernel32.DWORD STD_OUTPUT_HANDLE = new MainWindows.Kernel32.DWORD(-11);
      final MainWindows.Kernel32.HANDLE hOut = MainWindows.Kernel32.INSTANCE.GetStdHandle(STD_OUTPUT_HANDLE);

      final MainWindows.Kernel32.DWORDByReference p_dwMode = new MainWindows.Kernel32.DWORDByReference(new MainWindows.Kernel32.DWORD(0));
      MainWindows.Kernel32.INSTANCE.GetConsoleMode(hOut, p_dwMode);

      final int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;
      final MainWindows.Kernel32.DWORD dwMode = p_dwMode.getValue();
      dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
      MainWindows.Kernel32.INSTANCE.SetConsoleMode(hOut, dwMode);
    }

    try {
      EventManager.INSTANCE.getClass(); // Trigger load

      Hardware.start();
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
