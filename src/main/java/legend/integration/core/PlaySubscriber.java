package legend.integration.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.Flow;

public class PlaySubscriber implements Flow.Subscriber<PlayInput> {
    private static final Logger LOGGER = LogManager.getFormatterLogger(PlaySubscriber.class);
    private Flow.Subscription subscription;
    public List<PlayInput> consumedElements;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(PlayInput item) {
        consumedElements.add(item);
        subscription.request(1);
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