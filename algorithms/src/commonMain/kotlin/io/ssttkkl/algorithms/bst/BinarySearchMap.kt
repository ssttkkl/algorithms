package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.size
import io.ssttkkl.algorithms.utils.MutableBidirectionIterator
import io.ssttkkl.algorithms.utils.NavigableMap
import io.ssttkkl.algorithms.utils.NavigableSet

abstract class BinarySearchMap<
        K,
        V,
        out N : MutableBinarySearchTree<
                K,
                BinarySearchMap.Entry<K, V>,
                N>
        > internal constructor(
    protected val keyComparator: Comparator<K>
) : AbstractMutableMap<K, V>(), NavigableMap<K, V> {
    protected abstract fun createNode(entry: Entry<K, V>): N

    var root: @UnsafeVariance N? = null
        private set

    override val size: Int
        get() = root.size

    class Entry<K, V> internal constructor(override val key: K, private val keyComparator: Comparator<K>) :
        MutableMap.MutableEntry<K, V>, Comparable<Map.Entry<K, V>> {
        private var _value: V? = null

        internal val isSetValue: Boolean
            get() = _value != null

        internal fun initialize(value: V) {
            _value = value
        }

        override val value: V
            get() = _value!!

        override fun setValue(newValue: V): V {
            return value.also {
                _value = newValue
            }
        }

        override fun toString(): String {
            return "${key}=${value}"
        }

        override fun hashCode(): Int {
            return key.hashCode() xor value.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other === this) return true

            return other is Map.Entry<*, *> && key == other.key && value == other.value
        }

        override fun compareTo(other: Map.Entry<K, V>): Int {
            return keyComparator.compare(key, other.key)
        }
    }

    protected fun createEntry(key: K): Entry<K, V> {
        return Entry(key, keyComparator)
    }

    private inner class EntrySet : AbstractMutableSet<MutableMap.MutableEntry<K, V>>(),
        NavigableSet<MutableMap.MutableEntry<K, V>> {

        override val size: Int
            get() = this@BinarySearchMap.size

        override fun first(): MutableMap.MutableEntry<K, V> {
            return this@BinarySearchMap.firstEntry()
        }

        override fun last(): MutableMap.MutableEntry<K, V> {
            return this@BinarySearchMap.lastEntry()
        }

        override fun iterator(): MutableBidirectionIterator<MutableMap.MutableEntry<K, V>> {
            return MutableBinarySearchTreeIterator<N, MutableMap.MutableEntry<K, V>>(
                ::root,
                { this@BinarySearchMap.root = it },
                { it.value }
            )
        }

        override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
            throw UnsupportedOperationException()
        }

        override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean {
            root.let { root ->
                val node = root?.searchNode(element.key)
                if (node == null || node !== element) {
                    return false
                }
                val result = root.removeNode(element.key)
                this@BinarySearchMap.root = result.newRoot
                return result.success
            }
        }

        override fun removeAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
            var anyRemoved = false
            elements.forEach {
                anyRemoved = remove(it) || anyRemoved
            }
            return anyRemoved
        }

        override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean {
            val node = root?.searchNode(element.key)
            return node == element
        }

        override fun lower(e: MutableMap.MutableEntry<K, V>): MutableMap.MutableEntry<K, V>? {
            return this@BinarySearchMap.lowerEntry(e.key)
        }

        override fun floor(e: MutableMap.MutableEntry<K, V>): MutableMap.MutableEntry<K, V>? {
            return this@BinarySearchMap.floorEntry(e.key)
        }

        override fun ceiling(e: MutableMap.MutableEntry<K, V>): MutableMap.MutableEntry<K, V>? {
            return this@BinarySearchMap.ceilingEntry(e.key)
        }

        override fun higher(e: MutableMap.MutableEntry<K, V>): MutableMap.MutableEntry<K, V>? {
            return this@BinarySearchMap.higherEntry(e.key)
        }
    }

    private val _entries by lazy { EntrySet() }

    override val entries: NavigableSet<MutableMap.MutableEntry<K, V>> = _entries

    // Workaround of KT-20070, KT-33203
    @PublishedApi
    internal fun getEntries(): MutableSet<MutableMap.MutableEntry<K, V>> = _entries

    override val keys: NavigableSet<K> = object : AbstractMutableSet<K>(), NavigableSet<K> {
        override val size: Int
            get() = this@BinarySearchMap.size

        override fun first(): K {
            return this@BinarySearchMap.firstKey()
        }

        override fun last(): K {
            return this@BinarySearchMap.lastKey()
        }

        override fun iterator(): MutableBidirectionIterator<K> {
            return MutableBinarySearchTreeIterator<N, K>(
                ::root,
                { this@BinarySearchMap.root = it },
                { it.value.key }
            )
        }

        override fun add(element: K): Boolean {
            throw UnsupportedOperationException()
        }

        override fun remove(element: K): Boolean {
            root.let { root ->
                if (root == null) return false

                val result = root.removeNode(element)
                this@BinarySearchMap.root = result.newRoot
                return result.success
            }
        }

        override fun removeAll(elements: Collection<K>): Boolean {
            var anyRemoved = false
            elements.forEach {
                anyRemoved = remove(it) || anyRemoved
            }
            return anyRemoved
        }

        override fun contains(element: K): Boolean {
            return this@BinarySearchMap.containsKey(element)
        }

        override fun lower(e: K): K? {
            return this@BinarySearchMap.lowerKey(e)
        }

        override fun floor(e: K): K? {
            return this@BinarySearchMap.floorKey(e)
        }

        override fun ceiling(e: K): K? {
            return this@BinarySearchMap.ceilingKey(e)
        }

        override fun higher(e: K): K? {
            return this@BinarySearchMap.higherKey(e)
        }
    }

    override val values: MutableCollection<V> = object : AbstractMutableCollection<V>() {
        override val size: Int
            get() = this@BinarySearchMap.size

        override fun add(element: V): Boolean {
            throw UnsupportedOperationException()
        }

        override fun iterator(): MutableIterator<V> {
            return MutableBinarySearchTreeIterator<N, V>(
                ::root,
                { this@BinarySearchMap.root = it },
                { it.value.value }
            )
        }
    }

    override fun lowerEntry(key: K): MutableMap.MutableEntry<K, V>? {
        return root?.lowerNode(key)?.value
    }

    override fun floorEntry(key: K): MutableMap.MutableEntry<K, V>? {
        return root?.floorNode(key)?.value
    }

    override fun ceilingEntry(key: K): MutableMap.MutableEntry<K, V>? {
        return root?.ceilingNode(key)?.value
    }

    override fun higherEntry(key: K): MutableMap.MutableEntry<K, V>? {
        return root?.higherNode(key)?.value
    }

    override fun entryOfRank(rank: Int): MutableMap.MutableEntry<K, V>? {
        return root?.nodeOfRank(rank)?.value
    }

    override fun rankOfKey(key: K): Int {
        return root?.rankOfKey(key) ?: 0
    }

    override fun firstEntry(): MutableMap.MutableEntry<K, V> {
        return root?.minimalNode()?.value ?: throw NoSuchElementException()
    }

    override fun lastEntry(): MutableMap.MutableEntry<K, V> {
        return root?.maximumNode()?.value ?: throw NoSuchElementException()
    }

    override fun clear() {
        root = null
    }

    override fun remove(key: K): V? {
        root.let { root ->
            if (root == null) return null

            val result = root.removeNode(key)
            this@BinarySearchMap.root = result.newRoot
            return result.removedNode?.value?.value
        }
    }

    override fun put(key: K, value: V): V? {
        root.let { root ->
            if (root == null) {
                val entry = createEntry(key)
                entry.initialize(value)
                this@BinarySearchMap.root = createNode(entry)
                return null
            }
            val result = root.insertNode(key, createEntry(key))
            this@BinarySearchMap.root = result.newRoot

            val entry = result.insertedNode.value
            if (entry.isSetValue) {
                return entry.setValue(value)
            } else {
                entry.initialize(value)
                return null
            }
        }
    }

    override fun get(key: K): V? {
        return root?.searchNode(key)?.value?.value
    }

    override fun containsKey(key: K): Boolean {
        return root?.searchNode(key) != null
    }
}
