package legend.integration.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Flow;

public class PlaySubscriber implements Flow.Subscriber<PlayInput> {
    private static final Logger LOGGER = LogManager.getFormatterLogger(PlaySubscriber.class);
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(PlayInput item) {
        switch(item) {
            case UP -> { /* CALL UP INPUT METHOD */ }
            case DOWN -> { /* CALL DOWN INPUT METHOD  */ }
            case LEFT -> { /* CALL LEFT INPUT METHOD */ }
            case RIGHT -> { /* CALL RIGHT INPUT METHOD */ }
            case X -> { /* CALL `╳` INPUT METHOD */ }
            case SQUARE -> { /* CALL `⃞` INPUT METHOD */  }
            case TRIANGLE -> { /* CALL `▲` INPUT METHOD */  }
            case CIRCLE -> { /* CALL `○` INPUT METHOD */  }
            case L1 -> { /* CALL L1 INPUT METHOD */  }
            case L2 -> { /* CALL L2 INPUT METHOD */  }
            case R1 -> { /* CALL R1 INPUT METHOD */  }
            case R2 -> {  /* CALL R2 INPUT METHOD */ }
            case START -> { /* CALL START INPUT METHOD */  }
            case SELECT -> { /* CALL SELECT INPUT METHOD */  }
            case NOOP -> { /* DO NOTHING */  }
        }

        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.error(throwable);
    }

    @Override
    public void onComplete() {
        LOGGER.debug("Done");
    }
}