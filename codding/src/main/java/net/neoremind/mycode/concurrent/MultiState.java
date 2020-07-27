package net.neoremind.mycode.concurrent;

/**
 * A holder of multiple states identified by {@code ID}.
 *
 * <p>The state is a mutable value supporting common retrieval and update APIs.
 *
 * <p>For example, assume the {@code ID} is a table id with type of {@link Integer},
 * the state can represent whether the table is truncating or not with type of {@link Boolean}.
 *
 * @author xu.zx
 * @param <ID> the type of keys maintained by this state
 * @param <V>  the state of a specific id
 */
public interface MultiState<ID, V> {

  /**
   * Update and get.
   *
   * @param id    ID
   * @param value state
   * @return state
   */
  V updateAndGet(ID id, V state);

  /**
   * Update state by {@link Updater} and return the state.
   *
   * @param id      ID
   * @param updater state updater
   * @return state
   */
  V updateAndGet(ID id, Updater<ID, V> updater);

  /**
   * Get state.
   *
   * @param id ID
   * @return state
   */
  V get(ID id);

  /**
   * Remove state.
   *
   * @param id ID
   * @return state
   */
  V remove(ID id);

  /**
   * State updater interface.
   *
   * @param <ID> the type of keys maintained by this state
   * @param <V>  the state of a specific id
   */
  interface Updater<ID, V> {

    /**
     * Updating state closure function which could reach out to outside
     * scope and manipulate values from context.
     *
     * @param id ID
     * @return updated state
     */
    V doClosureUpdate(ID id);
  }
}
