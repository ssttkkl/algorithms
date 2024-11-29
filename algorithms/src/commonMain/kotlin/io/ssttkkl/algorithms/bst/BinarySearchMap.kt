package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.size

abstract class BinarySearchMap<
        K,
        V,
        out N : MutableBinarySearchTree<
                K,
                BinarySearchMap.Entry<K, V>,
                N>
        > internal constructor(
    protected val keyComparator: Comparator<K>
) : AbstractMutableMap<K, V>(), MutableSortedMap<K, V> {
    protected abstract fun createNode(entry: Entry<K, V>): N

    var root: @UnsafeVariance N? = null
        private set

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

    private inner class EntryIterator<out T>(val getValue: (@UnsafeVariance N) -> T) : MutableBidirectionIterator<T> {
        private var nextNode: N? = root?.minimalNode()
        private var prevNode: N? = null

        override fun hasNext(): Boolean {
            return nextNode != null
        }

        private fun nextNode(): N {
            val result = nextNode ?: throw NoSuchElementException()
            nextNode = result.successor()
            prevNode = result
            return result
        }

        override fun next(): T {
            return nextNode().let(getValue)
        }

        override fun remove() {
            val result = prevNode ?: throw NoSuchElementException()
            prevNode = null
            this@BinarySearchMap.remove(result.key)
        }

        override fun hasPrevious(): Boolean {
            return prevNode != null
        }

        private fun previousNode(): N {
            val result = prevNode ?: throw NoSuchElementException()
            prevNode = result.predecessor()
            nextNode = result
            return result
        }

        override fun previous(): T {
            return previousNode().let(getValue)
        }
    }

    override val size: Int
        get() = root.size

    private inner class EntrySet : AbstractMutableSet<MutableMap.MutableEntry<K, V>>(),
        MutableSortedSet<MutableMap.MutableEntry<K, V>> {

        override val size: Int
            get() = this@BinarySearchMap.size

        override fun first(): MutableMap.MutableEntry<K, V> {
            return this@BinarySearchMap.firstEntry()
        }

        override fun last(): MutableMap.MutableEntry<K, V> {
            return this@BinarySearchMap.lastEntry()
        }

        override fun iterator(): MutableBidirectionIterator<MutableMap.MutableEntry<K, V>> {
            return EntryIterator { it.value }
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
                val (_, newRoot) = root.removeNode(element.key)
                this@BinarySearchMap.root = newRoot
                return true
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
    }

    private val _entries by lazy { EntrySet() }

    override val entries: MutableSortedSet<MutableMap.MutableEntry<K, V>> = _entries

    // Workaround of KT-20070, KT-33203
    @PublishedApi
    internal fun getEntries(): MutableSet<MutableMap.MutableEntry<K, V>> = _entries

    override fun lowerEntry(key: K): MutableMap.MutableEntry<K, V>? {
        return root?.lowerEntry(key)?.value
    }

    override fun floorEntry(key: K): MutableMap.MutableEntry<K, V>? {
        return root?.floorEntry(key)?.value
    }

    override fun ceilingEntry(key: K): MutableMap.MutableEntry<K, V>? {
        return root?.ceilingEntry(key)?.value
    }

    override fun higherEntry(key: K): MutableMap.MutableEntry<K, V>? {
        return root?.higherEntry(key)?.value
    }

    override fun entryOfRank(rank: Int): MutableMap.MutableEntry<K, V>? {
        return root?.entryOfRank(rank)?.value
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

    override val keys: MutableSortedSet<K> = object : AbstractMutableSet<K>(), MutableSortedSet<K> {
        override val size: Int
            get() = this@BinarySearchMap.size

        override fun first(): K {
            return this@BinarySearchMap.firstKey()
        }

        override fun last(): K {
            return this@BinarySearchMap.lastKey()
        }

        override fun iterator(): MutableBidirectionIterator<K> {
            return EntryIterator { it.key }
        }

        override fun add(element: K): Boolean {
            throw UnsupportedOperationException()
        }

        override fun remove(element: K): Boolean {
            root.let { root ->
                if (root == null) return false

                val (node, newRoot) = root.removeNode(element)
                this@BinarySearchMap.root = newRoot
                return node != null
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
    }

    override fun clear() {
        root = null
    }

    override fun remove(key: K): V? {
        root.let { root ->
            if (root == null) return null

            val (node, newRoot) = root.removeNode(key)
            this@BinarySearchMap.root = newRoot
            return node?.value?.value
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
            val (node, newRoot) = root.insertNode(key, createEntry(key))
            this@BinarySearchMap.root = newRoot

            val entry = node.value
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
