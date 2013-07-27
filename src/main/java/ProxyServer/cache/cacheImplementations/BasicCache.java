package ProxyServer.cache.cacheImplementations;

import ProxyServer.cache.CachedObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Maciek
 * Date: 24.07.13
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
class BasicCache<K,V> extends LinkedHashMap<K,V> {

    private int capacity=0;

    public BasicCache(int capacity) {
        super(capacity+1,1.0f,false);
        this.capacity = capacity;
    }

    @Override
    public V put(K key, V value) {
        return super.put(key,value);
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
