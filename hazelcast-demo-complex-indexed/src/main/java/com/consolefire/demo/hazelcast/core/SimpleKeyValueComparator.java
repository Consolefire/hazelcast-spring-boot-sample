package com.consolefire.demo.hazelcast.core;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

public final class SimpleKeyValueComparator implements Comparator<Map.Entry<Long, Double>>, Serializable {
    @Override
    public int compare(Map.Entry<Long, Double> x, Map.Entry<Long, Double> y) {
        if (x == null) {
            return -1;
        }
        if (y == null) {
            return 1;
        }
        if (x.getKey().equals(y.getKey())) {
            return 0;
        }
        return y.getValue().compareTo(x.getValue());
    }
}
