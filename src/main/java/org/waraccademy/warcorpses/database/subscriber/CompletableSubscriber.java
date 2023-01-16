package org.waraccademy.warcorpses.database.subscriber;

import org.waraccademy.mongo.database.subscribers.OperationSubscriber;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CompletableSubscriber<T,V> extends OperationSubscriber<T> {
    private final CompletableFuture<V> future = new CompletableFuture<>();
    private final Function<List<T>,V> function;

    public CompletableSubscriber(Function<List<T>,V> function) {
        this.function = function;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if(!getReceived().isEmpty()) {
            future.complete(function.apply(getReceived()));
        } else {
            future.complete(null);
        }
    }

    public CompletableFuture<V> getResult() {
        return future;
    }
}