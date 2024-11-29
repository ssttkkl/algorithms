/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Written by Doug Lea and Josh Bloch with assistance from members of JCP
 * JSR-166 Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package io.ssttkkl.algorithms.utils

/**
 * A [SortedMap] extended with navigation methods returning the
 * closest matches for given search targets. Methods
 * [.lowerEntry], [.floorEntry], [.ceilingEntry],
 * and [.higherEntry] return `MutableMap.MutableEntry` objects
 * associated with keys respectively less than, less than or equal,
 * greater than or equal, and greater than a given key, returning
 * `null` if there is no such key.  Similarly, methods
 * [.lowerKey], [.floorKey], [.ceilingKey], and
 * [.higherKey] return only the associated keys. All of these
 * methods are designed for locating, not traversing entries.
 *
 *
 * A `NavigableMap` may be accessed and traversed in either
 * ascending or descending key order.  The [.descendingMap]
 * method returns a view of the map with the senses of all relational
 * and directional methods inverted. The performance of ascending
 * operations and views is likely to be faster than that of descending
 * ones.  Methods
 * [subMap(K, boolean, K, boolean)][.subMap],
 * [headMap(K, boolean)][.headMap], and
 * [tailMap(K, boolean)][.tailMap]
 * differ from the like-named `SortedMap` methods in accepting
 * additional arguments describing whether lower and upper bounds are
 * inclusive versus exclusive.  Submaps of any `NavigableMap`
 * must implement the `NavigableMap` interface.
 *
 *
 * This interface additionally defines methods [.firstEntry],
 * [.pollFirstEntry], [.lastEntry], and
 * [.pollLastEntry] that return and/or remove the least and
 * greatest mappings, if any exist, else returning `null`.
 *
 *
 * Implementations of entry-returning methods are expected to
 * return `MutableMap.MutableEntry` pairs representing snapshots of mappings
 * at the time they were produced, and thus generally do *not*
 * support the optional `Entry.setValue` method. Note however
 * that it is possible to change mappings in the associated map using
 * method `put`.
 *
 *
 * Methods
 * [subMap(K, K)][.subMap],
 * [headMap(K)][.headMap], and
 * [tailMap(K)][.tailMap]
 * are specified to return `SortedMap` to allow existing
 * implementations of `SortedMap` to be compatibly retrofitted to
 * implement `NavigableMap`, but extensions and implementations
 * of this interface are encouraged to override these methods to return
 * `NavigableMap`.  Similarly,
 * [.keySet] can be overridden to return [NavigableSet].
 *
 *
 * This interface is a member of the
 * [
 * Java Collections Framework]({@docRoot}/java.base/java/util/package-summary.html#CollectionsFramework).
 *
 * @author Doug Lea
 * @author Josh Bloch
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @since 1.6
</V></K> */
interface NavigableMap<K, V> : SortedMap<K, V> {
    /**
     * Returns a key-value mapping associated with the greatest key
     * strictly less than the given key, or `null` if there is
     * no such key.
     *
     * @param key the key
     * @return an entry with the greatest key less than `key`,
     * or `null` if there is no such key
     * @throws ClassCastException if the specified key cannot be compared
     * with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     * and this map does not permit null keys
     */
    fun lowerEntry(key: K): MutableMap.MutableEntry<K, V>?

    /**
     * Returns the greatest key strictly less than the given key, or
     * `null` if there is no such key.
     *
     * @param key the key
     * @return the greatest key less than `key`,
     * or `null` if there is no such key
     * @throws ClassCastException if the specified key cannot be compared
     * with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     * and this map does not permit null keys
     */
    fun lowerKey(key: K): K? {
        return lowerEntry(key)?.key
    }

    /**
     * Returns a key-value mapping associated with the greatest key
     * less than or equal to the given key, or `null` if there
     * is no such key.
     *
     * @param key the key
     * @return an entry with the greatest key less than or equal to
     * `key`, or `null` if there is no such key
     * @throws ClassCastException if the specified key cannot be compared
     * with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     * and this map does not permit null keys
     */
    fun floorEntry(key: K): MutableMap.MutableEntry<K, V>?

    /**
     * Returns the greatest key less than or equal to the given key,
     * or `null` if there is no such key.
     *
     * @param key the key
     * @return the greatest key less than or equal to `key`,
     * or `null` if there is no such key
     * @throws ClassCastException if the specified key cannot be compared
     * with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     * and this map does not permit null keys
     */
    fun floorKey(key: K): K? {
        return floorEntry(key)?.key
    }

    /**
     * Returns a key-value mapping associated with the least key
     * greater than or equal to the given key, or `null` if
     * there is no such key.
     *
     * @param key the key
     * @return an entry with the least key greater than or equal to
     * `key`, or `null` if there is no such key
     * @throws ClassCastException if the specified key cannot be compared
     * with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     * and this map does not permit null keys
     */
    fun ceilingEntry(key: K): MutableMap.MutableEntry<K, V>?

    /**
     * Returns the least key greater than or equal to the given key,
     * or `null` if there is no such key.
     *
     * @param key the key
     * @return the least key greater than or equal to `key`,
     * or `null` if there is no such key
     * @throws ClassCastException if the specified key cannot be compared
     * with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     * and this map does not permit null keys
     */
    fun ceilingKey(key: K): K? {
        return ceilingEntry(key)?.key
    }

    /**
     * Returns a key-value mapping associated with the least key
     * strictly greater than the given key, or `null` if there
     * is no such key.
     *
     * @param key the key
     * @return an entry with the least key greater than `key`,
     * or `null` if there is no such key
     * @throws ClassCastException if the specified key cannot be compared
     * with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     * and this map does not permit null keys
     */
    fun higherEntry(key: K): MutableMap.MutableEntry<K, V>?

    /**
     * Returns the least key strictly greater than the given key, or
     * `null` if there is no such key.
     *
     * @param key the key
     * @return the least key greater than `key`,
     * or `null` if there is no such key
     * @throws ClassCastException if the specified key cannot be compared
     * with the keys currently in the map
     * @throws NullPointerException if the specified key is null
     * and this map does not permit null keys
     */
    fun higherKey(key: K): K? {
        return higherEntry(key)?.key
    }

    fun entryOfRank(rank: Int): MutableMap.MutableEntry<K, V>?

    fun keyOfRank(rank: Int): K? {
        return entryOfRank(rank)?.key
    }

    fun rankOfKey(key: K): Int

    /**
     * Returns a [Set] view of the keys contained in this map.
     * The set's iterator returns the keys in ascending order.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own `remove` operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * `Iterator.remove`, `Set.remove`,
     * `removeAll`, `retainAll`, and `clear`
     * operations.  It does not support the `add` or `addAll`
     * operations.
     *
     * @return a set view of the keys contained in this map, sorted in
     * ascending order
     */
    override val keys: NavigableSet<K>

    /**
     * Returns a [Collection] view of the values contained in this map.
     * The collection's iterator returns the values in ascending order
     * of the corresponding keys.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own `remove` operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the `Iterator.remove`,
     * `Collection.remove`, `removeAll`,
     * `retainAll` and `clear` operations.  It does not
     * support the `add` or `addAll` operations.
     *
     * @return a collection view of the values contained in this map,
     * sorted in ascending key order
     */
    override val values: MutableCollection<V>

    /**
     * Returns a [Set] view of the mappings contained in this map.
     * The set's iterator returns the entries in ascending key order.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own `remove` operation, or through the
     * `setValue` operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the `Iterator.remove`,
     * `Set.remove`, `removeAll`, `retainAll` and
     * `clear` operations.  It does not support the
     * `add` or `addAll` operations.
     *
     * @return a set view of the mappings contained in this map,
     * sorted in ascending key order
     */
    override val entries: NavigableSet<MutableMap.MutableEntry<K, V>>
}
