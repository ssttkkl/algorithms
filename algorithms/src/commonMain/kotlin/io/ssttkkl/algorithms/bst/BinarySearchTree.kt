package io.ssttkkl.algorithms.bst

import kotlin.random.Random

open class BinarySearchTree<K, V>(
    val comparator: Comparator<K>
) : AbstractMutableMap<K, V>() {
    class Entry<K, V>(
        override var key: K,
    ) : MutableMap.MutableEntry<K, V> {
        internal var _value: V? = null
        override val value: V
            get() = _value!!

        internal var left: Entry<K, V>? = null

        internal var right: Entry<K, V>? = null

        internal var parent: Entry<K, V>? = null

        override fun setValue(newValue: V): V {
            return value.also { _value = newValue }
        }

        internal fun minimal(): Entry<K,V> {
            var cur = this
            while (cur.left != null) cur = cur.left!!
            return cur
        }

        internal fun maximum(): Entry<K,V> {
            var cur = this
            while (cur.right != null) cur = cur.right!!
            return cur
        }

        internal fun predecessor(): Entry<K, V>? {
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

        internal fun successor(): Entry<K, V>? {
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

    protected var root: Entry<K, V>? = null

    final override var size: Int = 0
        private set

    protected fun searchNode(key: K, insert: Boolean = false): Entry<K, V>? {
        var cur: Entry<K, V>? = root
        var parent: Entry<K, V>? = null
        var cmp: Int = 0
        while (cur != null) {
            cmp = comparator.compare(key, cur.key)
            when {
                cmp < 0 -> {
                    parent = cur
                    cur = cur.left
                }

                cmp > 0 -> {
                    parent = cur
                    cur = cur.right
                }

                else -> return cur
            }
        }
        if (insert) {
            cur = Entry(key)
            cur.parent = parent
            if (cmp < 0) {
                parent!!.left = cur
            } else {
                parent!!.right = cur
            }
            size++
        }
        return cur
    }

    protected fun removeNode(node: Entry<K, V>) {
        val parent = node.parent
        val hasLeftChild = node.left != null
        val hasRightChild = node.right != null
        if (!hasLeftChild && !hasRightChild) {
            if (parent != null) {
                val isLeftChild = parent.left == node
                if (isLeftChild) {
                    parent.left = null
                } else {
                    parent.right = null
                }
            } else {
                root = null
            }
        } else if (hasLeftChild xor hasRightChild) {
            val child = if (hasLeftChild) node.left else node.right
            if (parent != null) {
                val isLeftChild = parent.left == node
                if (isLeftChild) {
                    parent.left = child
                } else {
                    parent.right = child
                }
            } else {
                root = child
            }
            child!!.parent = null
        } else {
            val left = node.left!!
            val right = node.right!!
            val replacer: Entry<K, V>
            if (Random.nextBoolean()) {
                val leftMaximum = left.maximum()
                if (leftMaximum != left) {
                    leftMaximum.parent!!.right = leftMaximum.left
                    leftMaximum.left?.parent = leftMaximum.parent
                } else {
                    node.left = left.left
                    node.left?.parent = node
                }
                replacer = leftMaximum
            } else {
                val rightMinimal = right.minimal()
                if (rightMinimal != right) {
                    rightMinimal.parent!!.left = rightMinimal.right
                    rightMinimal.right?.parent = rightMinimal.parent
                } else {
                    node.right = right.right
                    node.right?.parent = node
                }
                replacer = rightMinimal
            }

            replacer.parent = parent
            if (parent != null) {
                if (parent.left == node)
                    parent.left = replacer
                else
                    parent.right = replacer
            } else {
                root = replacer
            }

            replacer.left = node.left
            replacer.left?.parent = replacer
            replacer.right = node.right
            replacer.right?.parent = replacer
        }
        size--
    }

    protected inner class EntryIterator<out T>(val getValue: (Entry<K, V>)->T) : MutableIterator<T> {
        private var next: Entry<K, V>? = root?.minimal()

        override fun hasNext(): Boolean {
            return next != null
        }

        fun nextEntry(): Entry<K, V> {
            val result = next
            checkNotNull(result) { "there's no next entry" }
            next = result.successor()
            return result
        }

        override fun next(): T {
            return nextEntry().let(getValue)
        }

        override fun remove() {
            val result = next
            checkNotNull(result) { "there's no next entry" }
            next = result.successor()
            removeNode(result)
        }
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
        override val size: Int
            get() = this@BinarySearchTree.size

        override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> {
            return EntryIterator { it }
        }

        override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
            throw UnsupportedOperationException()
        }

        override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean {
            val node = searchNode(element.key)
            if (node != null) {
                this@BinarySearchTree.removeNode(node)
                return true
            }
            return false
        }
    }

    override val keys: MutableSet<K> = object : AbstractMutableSet<K>() {
        override val size: Int
            get() = this@BinarySearchTree.size

        override fun iterator(): MutableIterator<K> {
            return EntryIterator { it.key }
        }

        override fun add(element: K): Boolean {
            throw UnsupportedOperationException()
        }

        override fun remove(element: K): Boolean {
            val node = searchNode(element)
            if (node != null) {
                this@BinarySearchTree.removeNode(node)
                return true
            }
            return false
        }
    }

    override fun clear() {
        root = null
    }

    override fun remove(key: K): V? {
        val node = searchNode(key)
        if (node != null) {
            removeNode(node)
            return node.value
        }
        return null
    }

    override fun put(key: K, value: V): V? {
        var oldValue: V? = null
        if (root == null) {
            root = Entry<K, V>(key).apply {
                _value = value
            }
            size++
        } else {
            searchNode(key, true)!!.apply {
                oldValue = _value
                _value = value
            }
        }
        return oldValue
    }

    override fun get(key: K): V? {
        return searchNode(key)?.value
    }

    override fun containsKey(key: K): Boolean {
        return searchNode(key) != null
    }

    fun toGraphviz(): String {
        return buildString {
            fun Entry<K,V>.dfs() {
                if (left!=null){
                    appendLine("${key} -> ${left!!.key} [label = \"L\"];")
                }
                if (right!=null){
                    appendLine("${key} -> ${right!!.key} [label = \"R\"];")
                }
                left?.dfs()
                right?.dfs()
            }

            appendLine("digraph g1 {")
            appendLine("node [shape=circle];")
            root?.dfs()
            appendLine("}")
        }
    }
}

fun <K : Comparable<K>, V> BinarySearchTree(): BinarySearchTree<K, V> {
    return BinarySearchTree { a, b -> a.compareTo(b) }
}