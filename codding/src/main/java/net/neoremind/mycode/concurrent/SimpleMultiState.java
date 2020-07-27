package net.neoremind.mycode.concurrent;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple holder of multiple states identified by {@code ID}.
 *
 * <p>Note that this simple state holder is <b>thread unsafe</b>. If you would like to
 * make {@link #updateAndGet(Object, Object)} or {@link #updateAndGet(Object, Updater)}
 * work atomically and thread-safe, refer to {@link MultiStates#synchronizedMultiState(MultiState)}.
 *
 * @author xu.zx
 * @param <ID> the type of keys maintained by this state
 * @param <V>  the state of a specific id
 */
public class SimpleMultiState<ID, V> implements MultiState<ID, V> {

  private final Map<ID, V> internalState;

  public SimpleMultiState() {
    internalState = new HashMap<>();
  }

  @Override
  public V updateAndGet(ID id, V value) {
    internalState.put(id, value);
    return internalState.get(id);
  }

  @Override
  public V updateAndGet(ID id, Updater<ID, V> updater) {
    internalState.put(id, updater.doClosureUpdate(id));
    return internalState.get(id);
  }

  @Override
  public V get(ID id) {
    return internalState.get(id);
  }

  @Override
  public V remove(ID id) {
    return internalState.remove(id);
  }
}
