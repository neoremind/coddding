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

import java.util.Arrays;

/**
 * Used by {@code BOHMap} as a container of bytes.
 *
 * @author cfelde (Christian Felde, cfelde.com)
 */
public final class Binary {
    private final byte[] value;
    
    public Binary(byte[] value) {
        if (value == null)
            throw new NullPointerException("value is null");
        
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Arrays.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Binary other = (Binary) obj;
        
        return Arrays.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "Binary{" + "value=size(" + value.length + ")}";
    }
}