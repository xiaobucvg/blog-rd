package com.xiaobu.blog.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 带过期时间的 Map
 *
 * @author zh  --2020/4/2 10:12
 */
public class ExpiryMap<K, V> extends ConcurrentHashMap<K, V> {

    // 存放过期时间
    private Map<K, Long> expTimeMap = new ConcurrentHashMap<>();

    public V put(K key, V value, long expTime) {
        expTimeMap.put(key, System.currentTimeMillis() + expTime);
        return super.put(key, value);
    }


    public void putAll(Map<? extends K, ? extends V> m, long expTime) {
        Map<K, Long> map = new HashMap<>();
        m.keySet().forEach(key -> {
            map.put(key, System.currentTimeMillis() + expTime);
        });
        this.expTimeMap.putAll(map);
        super.putAll(m);
    }


    @Override
    public int size() {
        AtomicInteger size = new AtomicInteger(0);
        expTimeMap.forEach((k, finalTime) -> {
            if (System.currentTimeMillis() < finalTime) {
                size.getAndIncrement();
            }
        });
        return size.get();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        V value = super.get(key);
        if (value != null) {
            Long finalTime = this.expTimeMap.get(key);
            return System.currentTimeMillis() <= finalTime;
        }
        return false;
    }

    @Override
    public V remove(Object key) {
        V value = super.remove(key);
        if (value != null) {
            this.expTimeMap.remove(key);
        }
        return value;
    }

    @Override
    public void clear() {
        super.clear();
        this.expTimeMap.clear();
    }

    @Override
    public boolean containsValue(Object value) {
        AtomicBoolean res = new AtomicBoolean(false);
        Set<Entry<K, V>> entries = super.entrySet();
        Stream<Entry<K, V>> targetEntries = entries.stream().filter(entry -> Objects.equals(value, entry.getValue()));
        Optional<Entry<K, V>> targetEntry = targetEntries.findFirst();
        return targetEntry.map(entry -> System.currentTimeMillis() < this.expTimeMap.get(entry.getValue())).orElseGet(res::get);
    }


    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = super.entrySet();
        Stream<Entry<K, V>> targetEntrySet = entries.stream().filter(entry -> System.currentTimeMillis() < this.expTimeMap.get(entry.getKey()));
        return targetEntrySet.collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<>();
        this.forEach((key, value) -> collection.add(value));
        return collection;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V value = super.getOrDefault(key, defaultValue);
        if (!Objects.equals(value, defaultValue)) {
            if (System.currentTimeMillis() < this.expTimeMap.get(key)) {
                return value;
            }
        }
        return defaultValue;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean isRemoved = super.remove(key, value);
        if (isRemoved) {
            this.expTimeMap.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value != null) {
            long finalTime = expTimeMap.get(key);
            if (System.currentTimeMillis() > finalTime) {
                value = null;
            }
        }
        return value;
    }

    @Override
    public KeySetView<K, V> keySet() {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public V putIfAbsent(K key, V value) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public V replace(K key, V value) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public Object clone() {
        throw new RuntimeException("此方法禁止使用");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new RuntimeException("此方法禁止使用，请使用 putAll 方法的重载方法");
    }

    @Override
    public V put(K key, V value) {
        throw new RuntimeException("此方法禁止使用，请使用 put 方法的重载方法");
    }

}
