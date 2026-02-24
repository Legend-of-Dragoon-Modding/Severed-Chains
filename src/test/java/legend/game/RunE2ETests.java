package legend.game;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

/**
 * Easy right click this file in IntelliJ and choose Run
 * to execute all E2E tests without any IDE configuration changes.
 */
public class RunE2ETests {
  public static void main(final String[] args) {
    final var listener = new SummaryGeneratingListener();
    final var request = LauncherDiscoveryRequestBuilder.request()
      .selectors(DiscoverySelectors.selectClass(EngineBootTest.class))
      .build();

    final var launcher = LauncherFactory.create();
    launcher.execute(request, listener);

    final var summary = listener.getSummary();
    final var out = new PrintWriter(System.out);
    summary.printTo(out);
    out.flush();

    if(summary.getTotalFailureCount() > 0) {
      summary.getFailures().forEach(f -> {
        System.err.println("FAILED: " + f.getTestIdentifier().getDisplayName());
        f.getException().printStackTrace(System.err);
      });
      System.exit(1);
    }

    System.exit(0);
  }
}
