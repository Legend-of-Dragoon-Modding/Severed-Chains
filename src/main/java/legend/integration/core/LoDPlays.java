package legend.integration.core;

import java.util.concurrent.Flow;

public interface LoDPlays extends Flow.Publisher<PlayInput>, Runnable {
    void play();
}