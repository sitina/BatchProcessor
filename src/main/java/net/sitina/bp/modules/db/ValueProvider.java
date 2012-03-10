package net.sitina.bp.modules.db;

public interface ValueProvider<K, V> {

	public V getValue(K key);
	
}
