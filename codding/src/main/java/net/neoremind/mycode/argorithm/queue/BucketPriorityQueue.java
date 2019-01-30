package net.neoremind.mycode.argorithm.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Consumer;

/**
 * BucketPriorityQueue
 *
 * @author xu.zx
 */
@Deprecated
public class BucketPriorityQueue implements Iterator<byte[]> {

  private List<byte[][]> buckets;

  private final int[] bucketLimits;

  private int index = 0;

  private PriorityQueue<Entry> pq;

  private int bucketNumber;

  public BucketPriorityQueue(int bucketNumber, int initialCapacityPerBucket) {
    this.bucketNumber = bucketNumber;
    buckets = new ArrayList<>(bucketNumber);
    bucketLimits = new int[bucketNumber];
    for (int i = 0; i < bucketNumber; i++) {
      buckets.add(new byte[initialCapacityPerBucket][]);
    }
  }

  public void add(byte[] bytes) {
    int bucketNo = (index++) % bucketNumber;
    int bucketOffset = bucketLimits[bucketNo];
    byte[][] bucketElements = buckets.get(bucketNo);
    bucketElements[bucketOffset] = bytes;
    bucketLimits[bucketNo]++;
  }

  public void sortAndBuildPriorityQueue() {
//    for (int i = 0; i < buckets.size(); i++) {
//      Arrays.sort(buckets.get(i), 0, bucketLimits[i], UnsignedLongKeyComparator.COMPARATOR);
//    }
//    pq = new PriorityQueue<>(bucketNumber, new Comparator<Entry>() {
//      @Override
//      public int compare(Entry o1, Entry o2) {
//        return UnsignedLongKeyComparator.COMPARATOR.compare(o1.current(), o2.current());
//      }
//    });
    for (int i = 0; i < buckets.size(); i++) {
      if (bucketLimits[i] > 0) {
        pq.add(new Entry(i, 0, buckets.get(i)));
      }
    }
  }

  public void foreach(Consumer<byte[]> consumer) {
    while (!pq.isEmpty()) {
      Entry entry = pq.poll();
      byte[] elem = entry.bucket[entry.bucketOffset++];
      consumer.accept(elem);
      if (entry.bucketOffset < bucketLimits[entry.bucketNo]) {
        pq.add(entry);
      }
    }
  }

  @Override
  public boolean hasNext() {
    return !pq.isEmpty();
  }

  @Override
  public byte[] next() {
    Entry entry = pq.poll();
    byte[] elem = entry.bucket[entry.bucketOffset++];
    if (entry.bucketOffset < bucketLimits[entry.bucketNo]) {
      pq.add(entry);
    }
    return elem;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  class Entry {
    int bucketNo;

    int bucketOffset;

    byte[][] bucket;

    public Entry(int bucketNo, int bucketOffset, byte[][] bucket) {
      this.bucketNo = bucketNo;
      this.bucketOffset = bucketOffset;
      this.bucket = bucket;
    }

    public int getBucketNo() {
      return bucketNo;
    }

    public int getBucketOffset() {
      return bucketOffset;
    }

    public byte[][] getBucket() {
      return bucket;
    }

    public byte[] current() {
      return bucket[bucketOffset];
    }
  }
}
