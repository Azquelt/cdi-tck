package org.jboss.cdi.tck.tests.context.request.event.jms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class AbstractMessageListener implements MessageListener {

    private static AtomicInteger processedMessages = new AtomicInteger(0);

    private static boolean initializedEventObserver;

    @Inject
    private RequestScopedObserver observer;

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            processedMessages.incrementAndGet();
            initializedEventObserver = observer.isInitializedObserved();
        } else {
            throw new IllegalArgumentException("Unsupported message type");
        }
    }

    public static void reset() {
        processedMessages.set(0);
        initializedEventObserver = false;
    }

    public static int getProcessedMessages() {
        return processedMessages.get();
    }

    public static boolean isInitializedEventObserver() {
        return initializedEventObserver;
    }
}
