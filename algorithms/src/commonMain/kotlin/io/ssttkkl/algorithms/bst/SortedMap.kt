/*
 * Copyright (c) 1998, 2018, Oracle and/or its affiliates. All rights reserved.
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
package io.ssttkkl.algorithms.bst

/**
 * A [Map] that further provides a *total ordering* on its keys.
 * The map is ordered according to the [natural][Comparable] of its keys, or by a [Comparator] typically
 * provided at sorted map creation time.  This order is reflected when
 * iterating over the sorted map's collection views (returned by the
 * `entrySet`, `keySet` and `values` methods).
 * Several additional operations are provided to take advantage of the
 * ordering.  (This interface is the map analogue of [SortedSet].)
 *
 *
 * All keys inserted into a sorted map must implement the `Comparable`
 * interface (or be accepted by the specified comparator).  Furthermore, all
 * such keys must be *mutually comparable*: `k1.compareTo(k2)` (or
 * `comparator.compare(k1, k2)`) must not throw a
 * `ClassCastException` for any keys `k1` and `k2` in
 * the sorted map.  Attempts to violate this restriction will cause the
 * offending method or constructor invocation to throw a
 * `ClassCastException`.
 *
 *
 * Note that the ordering maintained by a sorted map (whether or not an
 * explicit comparator is provided) must be *consistent with equals* if
 * the sorted map is to correctly implement the `Map` interface.  (See
 * the `Comparable` interface or `Comparator` interface for a
 * precise definition of *consistent with equals*.)  This is so because
 * the `Map` interface is defined in terms of the `equals`
 * operation, but a sorted map performs all key comparisons using its
 * `compareTo` (or `compare`) method, so two keys that are
 * deemed equal by this method are, from the standpoint of the sorted map,
 * equal.  The behavior of a tree map *is* well-defined even if its
 * ordering is inconsistent with equals; it just fails to obey the general
 * contract of the `Map` interface.
 *
 *
 * All general-purpose sorted map implementation classes should provide four
 * "standard" constructors. It is not possible to enforce this recommendation
 * though as required constructors cannot be specified by interfaces. The
 * expected "standard" constructors for all sorted map implementations are:
 *
 *  1. A void (no arguments) constructor, which creates an empty sorted map
 * sorted according to the natural ordering of its keys.
 *  1. A constructor with a single argument of type `Comparator`, which
 * creates an empty sorted map sorted according to the specified comparator.
 *  1. A constructor with a single argument of type `Map`, which creates
 * a new map with the same key-value mappings as its argument, sorted
 * according to the keys' natural ordering.
 *  1. A constructor with a single argument of type `SortedMap`, which
 * creates a new sorted map with the same key-value mappings and the same
 * ordering as the input sorted map.
 *
 *
 *
 * **Note**: several methods return submaps with restricted key
 * ranges. Such ranges are *half-open*, that is, they include their low
 * endpoint but not their high endpoint (where applicable).  If you need a
 * *closed range* (which includes both endpoints), and the key type
 * allows for calculation of the successor of a given key, merely request
 * the subrange from `lowEndpoint` to
 * `successor(highEndpoint)`.  For example, suppose that `m`
 * is a map whose keys are strings.  The following idiom obtains a view
 * containing all of the key-value mappings in `m` whose keys are
 * between `low` and `high`, inclusive:<pre>
 * SortedMap&lt;String, V&gt; sub = m.subMap(low, high+"\0");</pre>
 *
 * A similar technique can be used to generate an *open range*
 * (which contains neither endpoint).  The following idiom obtains a
 * view containing all of the key-value mappings in `m` whose keys
 * are between `low` and `high`, exclusive:<pre>
 * SortedMap&lt;String, V&gt; sub = m.subMap(low+"\0", high);</pre>
 *
 *
 * This interface is a member of the
 * [
 * Java Collections Framework]({@docRoot}/java.base/java/util/package-summary.html#CollectionsFramework).
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author  Josh Bloch
 * @see Map
 *
 * @see TreeMap
 *
 * @see SortedSet
 *
 * @see Comparator
 *
 * @see Comparable
 *
 * @see Collection
 *
 * @see ClassCastException
 *
 * @since 1.2
</V></K> */
interface SortedMap<K, out V> : Map<K, V> {

    /**
     * Returns a key-value mapping associated with the least
     * key in this map.
     *
     * @return an entry with the least key
     * @throws NoSuchElementException if this map is empty
     */
    fun firstEntry(): Map.Entry<K, V>

    /**
     * Returns the first (lowest) key currently in this map.
     *
     * @return the first (lowest) key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    fun firstKey(): K {
        return firstEntry().key
    }

    /**
     * Returns a key-value mapping associated with the greatest
     * key in this map.
     *
     * @return an entry with the greatest key.
     * @throws NoSuchElementException if this map is empty
     */
    fun lastEntry(): Map.Entry<K, V>

    /**
     * Returns the last (highest) key currently in this map.
     *
     * @return the last (highest) key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    fun lastKey(): K {
        return lastEntry().key
    }

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
    fun lowerEntry(key: K): Map.Entry<K, V>?

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
    fun floorEntry(key: K): Map.Entry<K, V>?

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
    fun ceilingEntry(key: K): Map.Entry<K, V>?

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
    fun higherEntry(key: K): Map.Entry<K, V>?

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

    fun entryOfRank(rank: Int): Map.Entry<K, V>?

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
    override val keys: SortedSet<K>

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
    override val values: Collection<V>

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
    override val entries: SortedSet<Map.Entry<K, V>>
}

interface MutableSortedMap<K, V> : MutableMap<K, V>, SortedMap<K, V> {

    override fun lowerEntry(key: K): MutableMap.MutableEntry<K, V>?
    override fun floorEntry(key: K): MutableMap.MutableEntry<K, V>?
    override fun ceilingEntry(key: K): MutableMap.MutableEntry<K, V>?
    override fun higherEntry(key: K): MutableMap.MutableEntry<K, V>?
    override fun firstEntry(): MutableMap.MutableEntry<K, V>
    override fun lastEntry(): MutableMap.MutableEntry<K, V>
    override fun entryOfRank(rank: Int): MutableMap.MutableEntry<K, V>?

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
    override val keys: MutableSortedSet<K>

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
    override val entries: MutableSortedSet<MutableMap.MutableEntry<K, V>>
}
