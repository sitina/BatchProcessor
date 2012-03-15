package net.sitina.bp.modules.db;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * cache solution implemented according to example in Java Concurrency in
 * Practice
 * 
 */
public class Memoizer<K, V> implements ValueProvider<K, V> {

	private final ConcurrentMap<K, Future<V>> cache = new ConcurrentHashMap<K, Future<V>>();

	private final ValueProvider<K, V> valueProvider;

	public Memoizer(ValueProvider<K, V> valueProvider) {
		this.valueProvider = valueProvider;
	}

	@Override
	public V getValue(final K key) {
		while (true) {
			Future<V> f = cache.get(key);
			if (f == null) {
				Callable<V> eval = new Callable<V>() {
					@Override
					public V call() throws InterruptedException {
						return valueProvider.getValue(key);
					}
				};

				FutureTask<V> ft = new FutureTask<V>(eval);
				f = cache.putIfAbsent(key, ft);
				if (f == null) {
					f = ft;
					ft.run();
				}
			}

			try {
				return f.get();
			} catch (CancellationException e) {
				cache.remove(key, f);
			} catch (Exception e) {
				throw new RuntimeException(e.getCause());
			}
		}
	}

}
