package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.BinaryTreeNode

open class BinarySearchTree<K, V>(
    private val comparator: Comparator<K>
) : AbstractMutableMap<K, V>(), MutableSortedMap<K, V> {
    open class Entry<K, V>(
        override var key: K,
    ) : BinaryTreeNode<Entry<K, V>>, MutableMap.MutableEntry<K, V> {
        internal var _value: V? = null
        override val value: V
            get() = _value!!

        override var left: Entry<K, V>? = null
            internal set

        override var right: Entry<K, V>? = null
            internal set

        override var parent: Entry<K, V>? = null
            internal set

        override var size: Int = 1
            internal set

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

        override fun setValue(newValue: V): V {
            return value.also { _value = newValue }
        }

        internal fun rotateLeft(): Entry<K, V> {
            val newRoot = right
            checkNotNull(newRoot)

            val p = newRoot.left
            newRoot.left = this
            this.parent = newRoot
            this.right = p
            p?.parent = this

            this.updateSize()
            newRoot.updateSize()
            return newRoot
        }

        internal fun rotateRight(): Entry<K, V> {
            val newRoot = left
            checkNotNull(newRoot)

            val p = newRoot.right
            newRoot.right = this
            this.parent = newRoot
            this.left = p
            p?.parent = this

            this.updateSize()
            newRoot.updateSize()
            return newRoot
        }

        internal fun updateSize() {
            size = (left?.size ?: 0) + (right?.size ?: 0) + 1
        }

        fun minimal(): Entry<K, V> {
            var cur = this
            while (cur.left != null) cur = cur.left!!
            return cur
        }

        fun maximum(): Entry<K, V> {
            var cur = this
            while (cur.right != null) cur = cur.right!!
            return cur
        }

        fun predecessor(): Entry<K, V>? {
            // 情况 1：如果 p 有左子树，前驱是左子树中的最右节点
            if (left != null) {
                return left!!.maximum()
            }

            // 情况 2：如果 p 没有左子树，前驱是向上查找直到找到一个是父节点右子树的节点
            var cur = this
            while (cur.parent != null && cur != cur.parent!!.right) {
                cur = cur.parent!!
            }
            return cur.parent
        }

        fun successor(): Entry<K, V>? {
            // 情况 1：如果 p 有右子树，后继是右子树中的最左节点
            if (right != null) {
                return right!!.minimal()
            }

            // 情况 2：如果 p 没有右子树，后继是向上查找直到找到一个是父节点左子树的节点
            var cur = this
            while (cur.parent != null && cur != cur.parent!!.left) {
                cur = cur.parent!!
            }
            return cur.parent
        }
    }

    private val Entry<*, *>?.size: Int
        get() = this?.size ?: 0

    var root: Entry<K, V>? = null
        protected set

    protected fun searchNode(key: K): Entry<K, V>? {
        var cur: Entry<K, V>? = root
        while (cur != null) {
            val cmp = comparator.compare(key, cur.key)
            when {
                cmp < 0 -> {
                    cur = cur.left
                }

                cmp > 0 -> {
                    cur = cur.right
                }

                else -> return cur
            }
        }
        return null
    }

    /**
     * @return inserted node and new root after inserting node.
     * if node already exists, returning the existing node as first
     */
    protected fun insertNode(key: K, root: Entry<K, V>? = this.root): Pair<Entry<K, V>, Entry<K, V>?> {
        if (root == null) {
            val newNode = Entry<K, V>(key)
            return Pair(newNode, newNode)
        }

        val cmp = comparator.compare(key, root.key)
        when {
            cmp < 0 -> {
                val (newNode, newLeft) = insertNode(key, root.left)
                root.left = newLeft
                newLeft?.parent = root
                root.updateSize()
                return Pair(newNode, root)
            }

            cmp > 0 -> {
                val (newNode, newRight) = insertNode(key, root.right)
                root.right = newRight
                newRight?.parent = root
                root.updateSize()
                return Pair(newNode, root)
            }

            else -> {
                return Pair(root, root)
            }
        }
    }

    /**
     * @return removed node and new root after removing node,
     * if node not existing, returning `null` value as first
     */
    protected fun removeNode(key: K, root: Entry<K, V>? = this.root): Pair<Entry<K, V>?, Entry<K, V>?> {
        if (root == null) return Pair(null, null)

        val cmp: Int = comparator.compare(key, root.key)
        when {
            cmp < 0 -> {
                val (node, newLeft) = removeNode(key, root.left)
                root.left = newLeft
                newLeft?.parent = root
                root.updateSize()
                return Pair(node, root)
            }

            cmp > 0 -> {
                val (node, newRight) = removeNode(key, root.right)
                root.right = newRight
                newRight?.parent = root
                root.updateSize()
                return Pair(node, root)
            }

            root.left == null -> {
                root.right?.parent = null
                return Pair(root, root.right)
            }

            root.right == null -> {
                root.left?.parent = null
                return Pair(root, root.left)
            }

            root.left.size > root.right.size -> {
                val replacement = root.left!!.maximum()
                val (_, newLeft) = removeNode(replacement.key, root.left)
                replacement.left = newLeft
                newLeft?.parent = replacement
                replacement.right = root.right
                root.right?.parent = replacement
                replacement.updateSize()
                replacement.parent = null
                return Pair(root, replacement)
            }

            else -> {
                val replacement = root.right!!.minimal()
                val (_, newRight) = removeNode(replacement.key, root.right)
                replacement.left = root.left
                root.left?.parent = replacement
                replacement.right = newRight
                newRight?.parent = replacement
                replacement.updateSize()
                replacement.parent = null
                return Pair(root, replacement)
            }
        }
    }

    private inner class EntryIterator<out T>(val getValue: (Entry<K, V>) -> T) : MutableBidirectionIterator<T> {
        private var next: Entry<K, V>? = root?.minimal()
        private var prev: Entry<K, V>? = null

        override fun hasNext(): Boolean {
            return next != null
        }

        private fun nextEntry(): Entry<K, V> {
            val result = next ?: throw NoSuchElementException()
            next = result.successor()
            prev = result
            return result
        }

        override fun next(): T {
            return nextEntry().let(getValue)
        }

        override fun remove() {
            val result = prev ?: throw NoSuchElementException()
            prev = null
            this@BinarySearchTree.remove(result.key)
        }

        override fun hasPrevious(): Boolean {
            return prev != null
        }

        fun previousEntry(): Entry<K, V> {
            val result = prev ?: throw NoSuchElementException()
            prev = result.predecessor()
            next = result
            return result
        }

        override fun previous(): T {
            return previousEntry().let(getValue)
        }
    }

    override val size: Int
        get() = root.size

    override val entries: MutableSortedSet<MutableMap.MutableEntry<K, V>> =
        object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>(), MutableSortedSet<MutableMap.MutableEntry<K, V>> {
            override val size: Int
                get() = this@BinarySearchTree.size

            override fun first(): MutableMap.MutableEntry<K, V> {
                return this@BinarySearchTree.firstEntry()
            }

            override fun last(): MutableMap.MutableEntry<K, V> {
                return this@BinarySearchTree.lastEntry()
            }

            override fun iterator(): MutableBidirectionIterator<MutableMap.MutableEntry<K, V>> {
                return EntryIterator { it }
            }

            override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
                throw UnsupportedOperationException()
            }

            override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean {
                val node = searchNode(element.key)
                if (node == null || node !== element) {
                    return false
                }
                val (_, newRoot) = removeNode(element.key)
                root = newRoot
                return true
            }

            override fun removeAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
                var anyRemoved = false
                elements.forEach {
                    anyRemoved = remove(it) || anyRemoved
                }
                return anyRemoved
            }

            override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean {
                val node = searchNode(element.key)
                return node === element
            }
        }

    override fun lowerEntry(key: K): Entry<K, V>? {
        val floor = floorEntry(key) ?: return null
        if (floor.key == key) {
            return floor.predecessor()
        }
        return floor
    }

    override fun floorEntry(key: K): Entry<K, V>? {
        var candidate: Entry<K, V>? = null

        var cur: Entry<K, V>? = root
        while (cur != null) {
            val cmp = comparator.compare(key, cur.key)
            if (cmp <= 0) {
                candidate = cur
                cur = cur.right
            } else {
                cur = cur.left
            }
        }
        return candidate
    }

    override fun ceilingEntry(key: K): Entry<K, V>? {
        var candidate: Entry<K, V>? = null

        var cur: Entry<K, V>? = root
        while (cur != null) {
            val cmp = comparator.compare(key, cur.key)
            if (cmp >= 0) {
                candidate = cur
                cur = cur.left
            } else {
                cur = cur.right
            }
        }
        return candidate
    }

    override fun higherEntry(key: K): Entry<K, V>? {
        val ceiling = ceilingEntry(key) ?: return null
        if (ceiling.key == key) {
            return ceiling.successor()
        }
        return ceiling
    }

    override fun entryOfRank(rank: Int): Entry<K, V>? {
        var cur = root
        var r = rank + 1
        while (r in 1..cur.size && cur != null) {
            when {
                r <= cur.left.size -> {
                    cur = cur.left
                }

                r == cur.left.size + 1 -> {
                    return cur
                }

                r <= cur.size -> {
                    r -= cur.left.size + 1
                    cur = cur.right
                }
            }
        }
        return null
    }

    override fun rankOfKey(key: K): Int {
        var cur: Entry<K, V>? = root
        var r = 1
        while (cur != null) {
            val cmp = comparator.compare(key, cur.key)
            when {
                cmp < 0 -> {
                    cur = cur.left
                }

                cmp == 0 -> {
                    r += cur.left.size
                    return r - 1
                }

                else -> {
                    r += cur.left.size + 1
                    cur = cur.right
                }
            }
        }
        return r - 1
    }

    override fun firstEntry(): Entry<K, V> {
        return root?.minimal() ?: throw NoSuchElementException()
    }

    override fun lastEntry(): Entry<K, V> {
        return root?.maximum() ?: throw NoSuchElementException()
    }

    override val keys: MutableSortedSet<K> = object : AbstractMutableSet<K>(), MutableSortedSet<K> {
        override val size: Int
            get() = this@BinarySearchTree.size

        override fun first(): K {
            return this@BinarySearchTree.firstKey()
        }

        override fun last(): K {
            return this@BinarySearchTree.lastKey()
        }

        override fun iterator(): MutableBidirectionIterator<K> {
            return EntryIterator { it.key }
        }

        override fun add(element: K): Boolean {
            throw UnsupportedOperationException()
        }

        override fun remove(element: K): Boolean {
            val (node, newRoot) = removeNode(element)
            root = newRoot
            return node != null
        }

        override fun removeAll(elements: Collection<K>): Boolean {
            var anyRemoved = false
            elements.forEach {
                anyRemoved = remove(it) || anyRemoved
            }
            return anyRemoved
        }

        override fun contains(element: K): Boolean {
            return this@BinarySearchTree.containsKey(element)
        }
    }

    override fun clear() {
        root = null
    }

    override fun remove(key: K): V? {
        val (node, newRoot) = removeNode(key)
        root = newRoot
        return node?.value
    }

    override fun put(key: K, value: V): V? {
        val (node, newRoot) = insertNode(key)
        root = newRoot

        val oldValue = node._value
        node._value = value
        return oldValue
    }

    override fun get(key: K): V? {
        return searchNode(key)?.value
    }

    override fun containsKey(key: K): Boolean {
        return searchNode(key) != null
    }
}

fun <K : Comparable<K>, V> BinarySearchTree(): BinarySearchTree<K, V> {
    return BinarySearchTree { a, b -> a.compareTo(b) }
}