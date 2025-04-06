package dev.xvcf.yawline.client.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Manager<T, S> {

    private final ConcurrentHashMap<T, S> entries = new ConcurrentHashMap<>();

    public void register(T key, S value) {
        entries.putIfAbsent(key, value);
    }

    public void registerFromArray(T[] keys, S value) {
        for (T key : keys) {
            register(key, value);
        }
    }

    public boolean unregister(T key) {
        return entries.remove(key) != null;
    }

    public S get(T key) {
        return entries.get(key);
    }

    public boolean contains(T key) {
        return entries.containsKey(key);
    }

    public Collection<S> values() {
        return entries.values();
    }

    public Set<Map.Entry<T, S>> entries() {
        return entries.entrySet();
    }

    public Set<T> keys() {
        return entries.keySet();
    }

}
