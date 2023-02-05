package com.example.reactiveprogramminginsprong5.push_vs_pull.push_pull_model;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class SubscriptionUtils {
    private SubscriptionUtils() {
    }

    public static long addCap(long current, long requested) {
        var cap = current + requested;
        if (cap < 0L) {
            cap = Long.MAX_VALUE;
        }
        return cap;
    }

    @SuppressWarnings("unchecked")
    public static long request(long n, Object instance, AtomicLongFieldUpdater updater) {
        for (; ; ) {
            var currentDemand = updater.get(instance);
            if (currentDemand == Long.MAX_VALUE) {
                return Long
                        .MAX_VALUE;
            }

            addCap(currentDemand, n);
        }
    }
}
