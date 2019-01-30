/**
 * Copyright 2014 Christian Felde (cfelde [at] cfelde [dot] com)
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.neoremind.mycode.offheap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import sun.misc.Unsafe;

/**
 * Uses off-heap memory (i.e., memory not managed by GC) to store 
 * binary keys and values.
 * 
 * The internal memory structure is as follows:
 * 
 * A set of partitions (like in a hash map) are used where map entries are
 * mapped to one of these. The number of partitions are specified at map
 * construction, and should be scaled according to the expected number of
 * entries stored in the map. Too few partitions and performance will suffer,
 * too many and you're wasting memory.
 * 
 * By default the hash of the key using Arrays::hashCode is used to
 * determin the location of the entry. But alternative hash code functions
 * can be used during map construction.
 * 
 * The partition address points to the first address location of the
 * first partition. This address will be zero if the partition has yet to be
 * allocated. The next partition location can be found by moving the address
 * 8 bytes down (assuming 64-bit addressing, otherwise 4 bytes).
 * 
 * At the partition location, an int (4 bytes) will first announce the
 * size of the partition. Thereafter two address values are used
 * to indicate the address of the entry key and the entry value.
 * 
 * At the entry key and entry value locations, an int is again used to
 * announce the size (in bytes) of either the key or value.
 * 
 * No attampts are made at making this map implementation thread-safe, but
 * concurrent read operations will not cause any concurrency issues.
 * 
 * The map accepts null values but not null keys.
 *
 * @author cfelde (Christian Felde, cfelde.com)
 */
public class BOHMap implements Map<Binary, Binary> {
    private final Unsafe unsafe;
    private final int addressSize;
    private final int partitionCount;
    private final long partitionAddress;
    private final Function<byte[], Integer> hashFunction;
    
    private long itemCount;
    
    /**
     * Create a new binary off-heap hash map with the specified number of
     * partitions. Assuming a uniform distribution of key hashes each partition
     * will contain approximately an equal share of entries.
     * 
     * The number of partitions should be scaled with tne number of expected
     * map entries, in order to maintain efficient lookup performance.
     * 
     * The default Arrays::hashCode method is used to calculate key hashes.
     * 
     * @param partitionCount A positive number of partitions
     */
    public BOHMap(int partitionCount) {
        this(partitionCount, Arrays::hashCode);
    }
    
    /**
     * Create a new binary off-heap hash map with the specified number of
     * partitions. Assuming a uniform distribution of key hashes each partition
     * will contain approximately an equal share of entries.
     * 
     * The number of partitions should be scaled with tne number of expected
     * map entries, in order to maintain efficient lookup performance.
     * 
     * @param partitionCount A positive number of partitions
     * @param hashFunction Hash function to use when calculating key hashes
     */
    public BOHMap(int partitionCount, Function<byte[], Integer> hashFunction) {
        this.unsafe = getUnsafe();
        this.addressSize = unsafe.addressSize();
        this.partitionCount = partitionCount;
        this.hashFunction = hashFunction;
        
        this.partitionAddress = allocate(this.partitionCount * addressSize, true);
    }
    
    private Unsafe getUnsafe() {
        try {
            Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            return (Unsafe) singleoneInstanceField.get(null);
        } catch (IllegalArgumentException | SecurityException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    private long allocate(long size, boolean init) {
        final long address = unsafe.allocateMemory(size);
        
        for (long offset = 0; init && offset < size; offset++) {
            unsafe.putByte(address + offset, (byte)0);
        }
        
        return address;
    }

    private long getPartitionOffset(byte[] keyData) {
        return Math.abs(hashFunction.apply(keyData) % partitionCount);
    }

    @Override
    public int size() {
        if (itemCount > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        
        return (int) itemCount;
    }

    @Override
    public boolean isEmpty() {
        return itemCount == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Binary))
            return false;
        
        final Binary bKey = (Binary) key;
        final byte[] keyData = bKey.getValue();
        final int keySize = keyData.length;

        final long offset = getPartitionOffset(keyData);

        // This is the location of the partition on which the entry key belongs
        long locationAddress = unsafe.getAddress(partitionAddress + (offset * addressSize));

        // Skip if unallocated
        if (locationAddress == 0)
            return false;
        
        // Read how many entries we expect in this partition
        int entryCount = unsafe.getInt(locationAddress);
        
        // Move pointer past size int
        locationAddress += Integer.BYTES;
        
        for (long locationOffset = 0; locationOffset < entryCount; locationOffset++) {
            // Address of key within partition
            long keyAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2));
            
            // Get size of key
            int size = unsafe.getInt(keyAddress);
            
            // If size of this key is different than the one
            // we're looking for, continue..
            if (size != keySize)
                continue;
            
            // Move pointer past size int
            keyAddress += Integer.BYTES;
            
            // Scan each byte to check for differences
            boolean isEqual = true;
            for (int keyOffset = 0; keyOffset < keySize; keyOffset++) {
                if (keyData[keyOffset] != unsafe.getByte(keyAddress + keyOffset)) {
                    isEqual = false;
                    break;
                }
            }
            
            // Check if we found the key
            if (isEqual)
                return true;
        }
        
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value != null && !(value instanceof Binary))
            return false;
        
        final Binary bValue = (Binary) value;
        final byte[] valueData = bValue == null ? null : bValue.getValue();
        final int valueSize = valueData == null ? -1 : valueData.length;
        
        // For each partition..
        for (long offset = 0; offset < partitionCount; offset++) {
            // ..get partition address
            long locationAddress = unsafe.getAddress(partitionAddress + (offset * addressSize));

            // Skip if unallocated
            if (locationAddress == 0)
                continue;

            // Read how many entries we expect in this partition
            int entryCount = unsafe.getInt(locationAddress);

            // Move pointer past size int
            locationAddress += Integer.BYTES;
            
            // Move pointer past key address in key/value pair
            locationAddress += addressSize;

            for (long locationOffset = 0; locationOffset < entryCount; locationOffset++) {
                // Address of value within partition
                long valueAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2));
                
                // Check if null value
                if (valueAddress == 0) {
                    if (bValue == null)
                        return true;
                    else
                        continue;
                }

                // Size of value
                int size = unsafe.getInt(valueAddress);

                // If size of this value is different than the one
                // we're looking for, continue..
                if (size != valueSize)
                    continue;

                // Move pointer past size int
                valueAddress += Integer.BYTES;

                // Scan each byte to check for differences
                boolean isEqual = true;
                for (int valueOffset = 0; valueOffset < valueSize; valueOffset++) {
                    if (valueData[valueOffset] != unsafe.getByte(valueAddress + valueOffset)) {
                        isEqual = false;
                        break;
                    }
                }

                if (isEqual)
                    return true;
            }
        }
        
        return false;
    }

    @Override
    public Binary get(Object key) {
        if (!(key instanceof Binary))
            return null;
        
        final Binary bKey = (Binary) key;
        final byte[] keyData = bKey.getValue();
        final int keySize = keyData.length;

        final long offset = getPartitionOffset(keyData);

        // This is the location of the partition on which the entry key belongs
        long locationAddress = unsafe.getAddress(partitionAddress + (offset * addressSize));

        // Skip if unallocated
        if (locationAddress == 0)
            return null;
        
        // Read how many entries we expect in this partition
        int entryCount = unsafe.getInt(locationAddress);
        
        // Move pointer past size int
        locationAddress += Integer.BYTES;
        
        for (long locationOffset = 0; locationOffset < entryCount; locationOffset++) {
            // Address of key within partition
            long keyAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2));
            
            // Get size of key
            int size = unsafe.getInt(keyAddress);
            
            // If size of this key is different than the one
            // we're looking for, continue..
            if (size != keySize)
                continue;
            
            // Move pointer past size int
            keyAddress += Integer.BYTES;
            
            // Scan each byte to check for differences
            boolean isEqual = true;
            for (int keyOffset = 0; keyOffset < keySize; keyOffset++) {
                if (keyData[keyOffset] != unsafe.getByte(keyAddress + keyOffset)) {
                    isEqual = false;
                    break;
                }
            }
            
            // Check if we found the key
            if (isEqual) {
                long valueAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2) + addressSize);
                
                // Check if this is a null value
                if (valueAddress == 0)
                    return null;
                
                int valueSize = unsafe.getInt(valueAddress);
                
                byte[] valueData = new byte[valueSize];
                
                // Move pointer past size int
                valueAddress += Integer.BYTES;
                
                for (int valueOffset = 0; valueOffset < valueSize; valueOffset++) {
                    valueData[valueOffset] = unsafe.getByte(valueAddress + valueOffset);
                }
                
                return new Binary(valueData);
            }
        }
        
        return null;
    }

    @Override
    public Binary put(Binary key, Binary value) {
        final byte[] keyData = key.getValue();
        final int keySize = keyData.length;

        final long offset = getPartitionOffset(keyData);

        // This is the location of the partition on which the entry key belongs
        long locationAddress = unsafe.getAddress(partitionAddress + (offset * addressSize));

        // Read how many entries we expect in this partition
        int entryCount = locationAddress == 0 ? 0 : unsafe.getInt(locationAddress);
        
        // Move pointer past size int
        locationAddress += Integer.BYTES;
        
        for (long locationOffset = 0; locationOffset < entryCount; locationOffset++) {
            // Address of key within partition
            long keyAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2));
            
            // Get size of key
            int size = unsafe.getInt(keyAddress);
            
            // If size of this key is different than the one
            // we're looking for, continue..
            if (size != keySize)
                continue;
            
            // Move pointer past size int
            keyAddress += Integer.BYTES;
            
            // Scan each byte to check for differences
            boolean isEqual = true;
            for (int keyOffset = 0; keyOffset < keySize; keyOffset++) {
                if (keyData[keyOffset] != unsafe.getByte(keyAddress + keyOffset)) {
                    isEqual = false;
                    break;
                }
            }
            
            // Check if we found the key
            if (isEqual) {
                long valueAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2) + addressSize);
                
                Binary oldValue = null;
                
                // Read old value if we have one
                if (valueAddress != 0) {
                    int valueSize = unsafe.getInt(valueAddress);

                    byte[] valueData = new byte[valueSize];

                    // Move pointer past size int
                    valueAddress += Integer.BYTES;

                    for (int valueOffset = 0; valueOffset < valueSize; valueOffset++) {
                        valueData[valueOffset] = unsafe.getByte(valueAddress + valueOffset);
                    }

                    oldValue = new Binary(valueData);

                    // Move pointer back to start
                    valueAddress -= Integer.BYTES;

                    // Free old value
                    unsafe.freeMemory(valueAddress);
                }
                
                if (value != null) {
                    // Deal with inserting new value
                    byte[] valueData = value.getValue();
                    int valueSize = valueData.length;

                    // Allocate new value space
                    valueAddress = allocate(Integer.BYTES + valueSize, false);
                    
                    // Copy data over
                    unsafe.putInt(valueAddress, valueSize);
                    
                    for (int valueOffset = 0; valueOffset < valueSize; valueOffset++) {
                        unsafe.putByte(valueAddress + valueOffset + Integer.BYTES, valueData[valueOffset]);
                    }
                } else {
                    valueAddress = 0;
                }
                
                // Update value address in partition
                unsafe.putAddress(locationAddress + (locationOffset * addressSize * 2) + addressSize, valueAddress);
                
                // Return old value
                return oldValue;
            }
        }
        
        // Existing entry not found on key, insert new
        itemCount++;
        
        // Move partition pointer back to start
        locationAddress -= Integer.BYTES;
        
        // Allocate and copy key
        long keyAddress = allocate(Integer.BYTES + keySize, false);
        unsafe.putInt(keyAddress, keySize);
        for (int keyOffset = 0; keyOffset < keySize; keyOffset++) {
            unsafe.putByte(keyAddress + Integer.BYTES + keyOffset, keyData[keyOffset]);
        }
        
        // Allocate and copy value
        long valueAddress = 0;
        
        if (value != null) {
            byte[] valueData = value.getValue();
            int valueSize = valueData.length;
            valueAddress = allocate(Integer.BYTES + valueSize, false);
            unsafe.putInt(valueAddress, valueSize);
            for (int valueOffset = 0; valueOffset < valueSize; valueOffset++) {
                unsafe.putByte(valueAddress + Integer.BYTES + valueOffset, valueData[valueOffset]);
            }
        }
        
        // Allocate or reallocate partition
        if (locationAddress == 0) {
            locationAddress = allocate(Integer.BYTES + addressSize + addressSize, false);
        } else {
            locationAddress = unsafe.reallocateMemory(locationAddress, Integer.BYTES + (addressSize * 2 * (entryCount + 1)));
        }
        
        // Insert key and value pointers
        unsafe.putAddress(locationAddress + Integer.BYTES + (addressSize * 2 * entryCount), keyAddress);
        unsafe.putAddress(locationAddress + Integer.BYTES + (addressSize * 2 * entryCount) + addressSize, valueAddress);
        
        // Update entry count
        unsafe.putInt(locationAddress, entryCount + 1);
        
        // Update pointer to partition
        unsafe.putAddress(partitionAddress + (offset * addressSize), locationAddress);
        
        return null;
    }

    @Override
    public Binary remove(Object key) {
        if (!(key instanceof Binary))
            return null;
        
        final Binary bKey = (Binary) key;
        final byte[] keyData = bKey.getValue();
        final int keySize = keyData.length;

        final long offset = getPartitionOffset(keyData);

        // This is the location of the partition on which the entry key belongs
        long locationAddress = unsafe.getAddress(partitionAddress + (offset * addressSize));

        // Skip if unallocated
        if (locationAddress == 0)
            return null;
        
        // Read how many entries we expect in this partition
        int entryCount = unsafe.getInt(locationAddress);
        
        // Move pointer past size int
        locationAddress += Integer.BYTES;
        
        for (long locationOffset = 0; locationOffset < entryCount; locationOffset++) {
            // Address of key within partition
            long keyAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2));
            
            // Get size of key
            int size = unsafe.getInt(keyAddress);
            
            // If size of this key is different than the one
            // we're looking for, continue..
            if (size != keySize)
                continue;
            
            // Move pointer past size int
            keyAddress += Integer.BYTES;
            
            // Scan each byte to check for differences
            boolean isEqual = true;
            for (int keyOffset = 0; keyOffset < keySize; keyOffset++) {
                if (keyData[keyOffset] != unsafe.getByte(keyAddress + keyOffset)) {
                    isEqual = false;
                    break;
                }
            }
            
            // Check if we found the key
            if (isEqual) {
                // Move key address back to start + free it
                keyAddress -= Integer.BYTES;
                unsafe.freeMemory(keyAddress);
                
                long valueAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2) + addressSize);
                
                Binary removedValue = null;
                
                // Check if this is a null value
                if (valueAddress != 0) {
                    int valueSize = unsafe.getInt(valueAddress);

                    byte[] valueData = new byte[valueSize];

                    // Move pointer past size int
                    valueAddress += Integer.BYTES;

                    for (int valueOffset = 0; valueOffset < valueSize; valueOffset++) {
                        valueData[valueOffset] = unsafe.getByte(valueAddress + valueOffset);
                    }

                    removedValue = new Binary(valueData);
                    
                    // Move value address back to start + free it
                    valueAddress -= Integer.BYTES;
                    unsafe.freeMemory(valueAddress);
                }
                
                // Next remove entry and shrink the partition
                // But only move if the entry we're removing isn't already
                // the last one in the partition
                if (locationOffset < entryCount - 1) {
                    // Move last entry to this entry position
                    // Key
                    long address = unsafe.getAddress(locationAddress + ((entryCount - 1) * addressSize * 2));
                    unsafe.putAddress(locationAddress + (locationOffset * addressSize * 2), address);
                    
                    // Value
                    address = unsafe.getAddress(locationAddress + ((entryCount - 1) * addressSize * 2) + addressSize);
                    unsafe.putAddress(locationAddress + (locationOffset * addressSize * 2) + addressSize, address);
                }
                
                // Move location back to start
                locationAddress -= Integer.BYTES;
                
                // Decrease partition counter value
                unsafe.putInt(locationAddress, entryCount - 1);
                
                // Shrink partition memory
                locationAddress = unsafe.reallocateMemory(locationAddress, Integer.BYTES + (addressSize * 2 * (entryCount - 1)));
                unsafe.putAddress(partitionAddress + (offset * addressSize), locationAddress);
                
                itemCount--;
                
                return removedValue;
            }
        }
        
        return null;
    }

    @Override
    public void clear() {
        // For each partition..
        for (long offset = 0; offset < partitionCount; offset++) {
            // ..get partition address
            long locationAddress = unsafe.getAddress(partitionAddress + (offset * addressSize));

            // Skip if unallocated
            if (locationAddress == 0)
                continue;

            // Read how many entries we expect in this partition
            int entryCount = unsafe.getInt(locationAddress);

            // Move pointer past size int
            locationAddress += Integer.BYTES;
            
            for (long locationOffset = 0; locationOffset < entryCount; locationOffset++) {
                long keyAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2));
                
                if (keyAddress != 0)
                    unsafe.freeMemory(keyAddress);
                
                long valueAddress = unsafe.getAddress(locationAddress + (locationOffset * addressSize * 2) + addressSize);
                
                if (valueAddress != 0)
                    unsafe.freeMemory(valueAddress);
            }
            
            locationAddress -= Integer.BYTES;
            
            unsafe.freeMemory(locationAddress);
            
            unsafe.putAddress(partitionAddress + (offset * addressSize), 0);
        }
        
        // Reset item counter
        itemCount = 0;
    }

    @Override
    protected void finalize() throws Throwable {
        // clear() will free all memory but the partition area itself
        clear();
        
        // Finally free the partition area itself
        unsafe.freeMemory(partitionAddress);
        
        super.finalize();
    }

    @Override
    public void putAll(Map<? extends Binary, ? extends Binary> m) {
        m.forEach((k, v) -> put(k, v));
    }

    @Override
    public Set<Binary> keySet() {
        return new KeySet(this);
    }

    @Override
    public Collection<Binary> values() {
        return new Values(this);
    }

    @Override
    public Set<Entry<Binary, Binary>> entrySet() {
        return new EntrySet(this);
    }
    
    private class KeySet implements Set<Binary> {
        private final BOHMap map;
        
        private KeySet(BOHMap map) {
            this.map = map;
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Binary))
                return false;
            
            return map.containsKey((Binary)o);
        }

        @Override
        public Iterator<Binary> iterator() {
            return new KeySetIterator(map);
        }

        @Override
        public Object[] toArray() {
            Object[] keys = new Object[map.size()];
            
            Iterator<Binary> it = iterator();
            for (int i = 0; i < keys.length && it.hasNext(); i++) {
                keys[i] = it.next();
            }
            
            return keys;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            int size = size();
            T[] r = a.length >= size ? a : (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            
            Iterator<Binary> it = iterator();
            for (int i = 0; i < r.length; i++) {
                if (!it.hasNext()) {
                    // Null terminate
                    r[i] = null;
                    return r;
                }
                
                r[i] = (T) it.next();
            }
            
            return r;
        }

        @Override
        public boolean add(Binary e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Binary))
                return false;
            
            return map.remove((Binary)o) != null;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return c.stream().noneMatch((k) -> (!map.containsKey((Binary)k)));
        }

        @Override
        public boolean addAll(Collection<? extends Binary> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean changed = false;
            for (Object k : c) {
                if (!(k instanceof Binary))
                    continue;
                
                changed = changed || map.containsKey((Binary)k);
                map.remove((Binary)k);
            }
            
            return changed;
        }

        @Override
        public void clear() {
            map.clear();
        }
    }
    
    private class KeySetIterator implements Iterator<Binary> {
        private final BOHMap map;
        
        private long offset, locationOffset;
        
        private KeySetIterator(BOHMap map) {
            this.map = map;
        }

        @Override
        public boolean hasNext() {
            while (offset < map.partitionCount) {
                long locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));

                if (locationAddress == 0) {
                    offset++;
                    continue;
                }
                
                break;
            }
            
            return offset < map.partitionCount;
        }

        @Override
        public Binary next() {
            if (offset >= map.partitionCount)
                throw new NoSuchElementException();
            
            long locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));

            while (locationAddress == 0) {
                offset++;
                
                if (offset >= map.partitionCount)
                    throw new NoSuchElementException();
                
                locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));
            }

            // Read how many entries we expect in this partition
            int entryCount = map.unsafe.getInt(locationAddress);

            // Move pointer past size int
            locationAddress += Integer.BYTES;

            long keyAddress = map.unsafe.getAddress(locationAddress + (locationOffset * map.addressSize * 2));

            // Get size of key
            int keyDataSize = map.unsafe.getInt(keyAddress);

            // Move pointer past size int
            keyAddress += Integer.BYTES;

            locationOffset++;
            if (locationOffset >= entryCount) {
                locationOffset = 0;
                offset++;
            }
            
            byte[] keyData = new byte[keyDataSize];
            for (int keyOffset = 0; keyOffset < keyDataSize; keyOffset++) {
                keyData[keyOffset] = map.unsafe.getByte(keyAddress + keyOffset);
            }
            
            return new Binary(keyData);
        }
    }
    
    private class Values implements Collection<Binary> {
        private final BOHMap map;
        
        private Values(BOHMap map) {
            this.map = map;
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Binary))
                return false;
            
            return map.containsValue((Binary)o);
        }

        @Override
        public Iterator<Binary> iterator() {
            return new ValuesIterator(map);
        }

        @Override
        public Object[] toArray() {
            Object[] values = new Object[map.size()];
            
            Iterator<Binary> it = iterator();
            for (int i = 0; i < values.length && it.hasNext(); i++) {
                values[i] = it.next();
            }
            
            return values;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            int size = size();
            T[] r = a.length >= size ? a : (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            
            Iterator<Binary> it = iterator();
            for (int i = 0; i < r.length; i++) {
                if (!it.hasNext()) {
                    // Null terminate
                    r[i] = null;
                    return r;
                }
                
                r[i] = (T) it.next();
            }
            
            return r;
        }

        @Override
        public boolean add(Binary e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return c.stream().noneMatch((v) -> (!contains((Binary)v)));
        }

        @Override
        public boolean addAll(Collection<? extends Binary> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            map.clear();
        }
    }
    
    private class ValuesIterator implements Iterator<Binary> {
        private final BOHMap map;
        
        private long offset, locationOffset;
        
        private ValuesIterator(BOHMap map) {
            this.map = map;
        }

        @Override
        public boolean hasNext() {
            while (offset < map.partitionCount) {
                long locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));

                if (locationAddress == 0) {
                    offset++;
                    continue;
                }
                
                break;
            }
            
            return offset < map.partitionCount;
        }

        @Override
        public Binary next() {
            if (offset >= map.partitionCount)
                throw new NoSuchElementException();
            
            long locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));

            while (locationAddress == 0) {
                offset++;
                
                if (offset >= map.partitionCount)
                    throw new NoSuchElementException();
                
                locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));
            }

            // Read how many entries we expect in this partition
            int entryCount = map.unsafe.getInt(locationAddress);

            // Move pointer past size int
            locationAddress += Integer.BYTES;

            long valueAddress = map.unsafe.getAddress(locationAddress + (locationOffset * map.addressSize * 2) + map.addressSize);

            locationOffset++;
            if (locationOffset >= entryCount) {
                locationOffset = 0;
                offset++;
            }
            
            // Check if null value
            if (valueAddress == 0)
                return null;

            // Get size of value
            int valueDataSize = map.unsafe.getInt(valueAddress);

            // Move pointer past size int
            valueAddress += Integer.BYTES;
            
            byte[] valueData = new byte[valueDataSize];
            for (int valueOffset = 0; valueOffset < valueDataSize; valueOffset++) {
                valueData[valueOffset] = map.unsafe.getByte(valueAddress + valueOffset);
            }
            
            return new Binary(valueData);
        }
    }
    
    private class EntrySet implements Set<Entry<Binary, Binary>> {
        private final BOHMap map;
        
        private EntrySet(BOHMap map) {
            this.map = map;
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            
            Object oKey = ((Map.Entry<Object, Object>)o).getKey();
            Object oValue = ((Map.Entry<Object, Object>)o).getValue();
            
            if (!(oKey instanceof Binary) || (oValue != null && !(oValue instanceof Binary)))
                return false;
            
            Binary key = (Binary) oKey;
            Binary value = (Binary) oValue;
            
            if (value == null) {
                return map.containsKey(key) && map.get(key) == null;
            }
            
            Binary mapValue = map.get(key);
            
            return Objects.equals(value, mapValue);
        }

        @Override
        public Iterator<Entry<Binary, Binary>> iterator() {
            return new EntrySetIterator(map);
        }

        @Override
        public Object[] toArray() {
            Object[] values = new Object[map.size()];
            
            Iterator<Entry<Binary, Binary>> it = iterator();
            for (int i = 0; i < values.length && it.hasNext(); i++) {
                values[i] = it.next();
            }
            
            return values;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            int size = size();
            T[] r = a.length >= size ? a : (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
            
            Iterator<Entry<Binary, Binary>> it = iterator();
            for (int i = 0; i < r.length; i++) {
                if (!it.hasNext()) {
                    // Null terminate
                    r[i] = null;
                    return r;
                }
                
                r[i] = (T) it.next();
            }
            
            return r;
        }

        @Override
        public boolean add(Entry<Binary, Binary> e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return c.stream().noneMatch((e) -> (!contains((Entry<Binary, Binary>)e)));
        }

        @Override
        public boolean addAll(Collection<? extends Entry<Binary, Binary>> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            map.clear();
        }
    }
    
    private class EntrySetIterator implements Iterator<Entry<Binary, Binary>> {
        private final BOHMap map;
        
        private long offset, locationOffset;
        
        private EntrySetIterator(BOHMap map) {
            this.map = map;
        }

        @Override
        public boolean hasNext() {
            while (offset < map.partitionCount) {
                long locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));

                if (locationAddress == 0) {
                    offset++;
                    continue;
                }
                
                break;
            }
            
            return offset < map.partitionCount;
        }

        @Override
        public Entry<Binary, Binary> next() {
            if (offset >= map.partitionCount)
                throw new NoSuchElementException();
            
            long locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));

            while (locationAddress == 0) {
                offset++;
                
                if (offset >= map.partitionCount)
                    throw new NoSuchElementException();
                
                locationAddress = map.unsafe.getAddress(map.partitionAddress + (offset * map.addressSize));
            }

            // Read how many entries we expect in this partition
            int entryCount = map.unsafe.getInt(locationAddress);

            // Move pointer past size int
            locationAddress += Integer.BYTES;

            long keyAddress = map.unsafe.getAddress(locationAddress + (locationOffset * map.addressSize * 2));
            long valueAddress = map.unsafe.getAddress(locationAddress + (locationOffset * map.addressSize * 2) + map.addressSize);

            locationOffset++;
            if (locationOffset >= entryCount) {
                locationOffset = 0;
                offset++;
            }
            
            // Get size of key
            int keyDataSize = map.unsafe.getInt(keyAddress);
            
            // Move pointer past size int
            keyAddress += Integer.BYTES;
            
            byte[] keyData = new byte[keyDataSize];
            for (int keyOffset = 0; keyOffset < keyDataSize; keyOffset++) {
                keyData[keyOffset] = map.unsafe.getByte(keyAddress + keyOffset);
            }
            
            final Binary key = new Binary(keyData);
            
            // Check if null value
            if (valueAddress == 0) {
                return new Map.Entry<Binary, Binary>() {
                    @Override
                    public Binary getKey() {
                        return key;
                    }

                    @Override
                    public Binary getValue() {
                        return null;
                    }

                    @Override
                    public Binary setValue(Binary value) {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            // Get size of value
            int valueDataSize = map.unsafe.getInt(valueAddress);

            // Move pointer past size int
            valueAddress += Integer.BYTES;
            
            byte[] valueData = new byte[valueDataSize];
            for (int valueOffset = 0; valueOffset < valueDataSize; valueOffset++) {
                valueData[valueOffset] = map.unsafe.getByte(valueAddress + valueOffset);
            }
            
            final Binary value = new Binary(valueData);
            
            return new Map.Entry<Binary, Binary>() {
                @Override
                public Binary getKey() {
                    return key;
                }

                @Override
                public Binary getValue() {
                    return value;
                }

                @Override
                public Binary setValue(Binary value) {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
}