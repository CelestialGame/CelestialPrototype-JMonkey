package com.celestial.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FixedHashMap<K, V> implements Map<K, V> {

    private Map<K, V> map;
    
    private int MAX;

    public FixedHashMap(int max) {
        map = new HashMap<K, V>();
        MAX = max;
    }

    public V put(K key, V value) {
        if (map.size() >= MAX && !map.containsKey(key)) {
             return null;
        } else {
             map.put(key, value);
             return value;
        }
    }

	@Override
	public void clear() {
		map.clear();
		
	}

	@Override
	public boolean containsKey(Object key) {
		if(map.containsKey(key)) return true;
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		if(map.containsValue(value)) return true;
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		if(map.isEmpty()) return true;
		return false;
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
		
	}

	@Override
	public V remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

}
