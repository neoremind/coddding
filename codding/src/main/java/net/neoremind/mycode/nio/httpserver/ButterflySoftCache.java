package net.neoremind.mycode.nio.httpserver;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ButterflySoftCache {

	public static class CacheEntry {
		public byte[] header;
		public byte[] body;

		public CacheEntry(byte[] header, byte[] body) {
			this.header = header;
			this.body = body;
		}

	}

	public static class MapEntry extends SoftReference<CacheEntry> {

		String key;

		public MapEntry(String key, CacheEntry referent,
				ReferenceQueue<CacheEntry> q) {
			super(referent, q);
			this.key = key;
		}

	}

	private ReferenceQueue<CacheEntry> queue = new ReferenceQueue<CacheEntry>();

	/**
	 * the back map used
	 */
	private Map<String, MapEntry> map = new ConcurrentHashMap<String, MapEntry>();

	public CacheEntry get(String key) {
		CacheEntry result = null;
		MapEntry entry = map.get(key);
		if (entry != null) {
			result = entry.get();
			if (result == null) {
				map.remove(entry.key);
			}
		}
		return result;
	}

	private void processQueue() {
		MapEntry entry;
		while ((entry = (MapEntry) queue.poll()) != null) {
			map.remove(entry.key);
		}
	}

	public void put(String key, byte[] header, byte[] body) {
		processQueue();
		map.put(key, new MapEntry(key, new CacheEntry(header, body), queue));
	}

	/**
	 * debug help
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int memory = 0;
		for (String key : map.keySet()) {
			CacheEntry entry = map.get(key).get();
			if (entry != null) {
				int size = 0;
				if (entry.body != null)
					size += entry.body.length;
				if (entry.header != null)
					size += entry.header.length;

				sb.append(key).append("\t").append(size).append("\t")
						.append(size / 1024).append("k\n");
				memory += size;
			}
		}

		StringBuilder sb2 = new StringBuilder();
		sb2.append("cache item count: ").append(map.size()).append("\n");
		sb2.append("memory size:\t").append(memory).append("\t")
				.append((double) memory / 1024).append("k\n");
		sb2.append(sb);
		return sb2.toString();

	}

}
