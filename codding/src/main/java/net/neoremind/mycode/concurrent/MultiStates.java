package net.neoremind.mycode.concurrent;

/**
 * The helper class of {@link MultiState} implementations.
 *
 * <p>This class works exactly like {@link java.util.Collections} which provides
 * some useful decorator wrappers for {@code MultiState}.
 *
 * @author xu.zx
 */
public class MultiStates {

  /**
   * Get a decorated state holder with synchronized API calls.
   */
  public static <ID, V> MultiState<ID, V> synchronizedMultiState(MultiState<ID, V> state) {
    return new SynchronizedMultiState<>(state);
  }

  /**
   * Thread safe decorator for a state holder.
   *
   * <p>This class is especially useful when one would like to update and get state atomically
   * and in a thread-safe manner.
   *
   * <p>For example, assume we have a jobs queue, thread1 is a loop thread (<em>AsyncJobManager</em>), thread1 might be
   * interrupted, conflicts with thread2 (<em>Truncating table thread</em>). Here introduce a state to coordinate between the two
   * threads, <code>lock updateAndGet</code> can avoid a circumstance when jobs is empty, and thread1 could falsely
   * dequeue job and work, which does not aware of the state which will set to true by thread2 afterwards.
   *
   * <p>Once thread2 updates state to true, thread1 can no longer dequeue job and work.
   *
   * <pre>
   *
   *  &lt;ID, state&gt; = false
   *
   *  +--------------+
   *  |  |  | ...    | jobs queue
   *  +--------------+
   *
   *   +--- thread1 ---+                   +--- thread2 ---+
   *   loop... in for(;;)                         |
   *          |                                   |
   *       lock get                               |
   *          |                                   |
   *   if state = false, dequeue job and work     |
   *          |                                   |
   *          |                   lock updateAndGet <- if jobs is empty, update state to true
   *          |                                   |
   *       lock get                   do work that might conflict with thread1
   *          |                                   |
   *   if state = true, skip job                  |
   *          |                                   |
   * </pre>
   */
  static class SynchronizedMultiState<ID, V> implements MultiState<ID, V> {

    /** Backing state holder. */
    final MultiState<ID, V> s;

    /** Object on which to synchronize. */
    final Object mutex;

    public SynchronizedMultiState(MultiState<ID, V> s) {
      this.s = s;
      this.mutex = this;
    }

    @Override
    public V updateAndGet(ID id, V value) {
      synchronized (mutex) {
        return s.updateAndGet(id, value);
      }
    }

    @Override
    public V updateAndGet(ID id, Updater<ID, V> updater) {
      synchronized (mutex) {
        return s.updateAndGet(id, updater);
      }
    }

    @Override
    public V get(ID id) {
      synchronized (mutex) {
        return s.get(id);
      }
    }

    @Override
    public V remove(ID id) {
      synchronized (mutex) {
        return s.remove(id);
      }
    }
  }
}
