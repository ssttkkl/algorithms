package io.ssttkkl.algorithms.bst

open class BinarySearchTree<K, V>(
    val comparator: Comparator<K>
) : AbstractMutableMap<K, V>() {
    open class Entry<K, V>(
        override var key: K,
    ) : MutableMap.MutableEntry<K, V> {
        internal var _value: V? = null
        override val value: V
            get() = _value!!

        internal var left: Entry<K, V>? = null

        internal var right: Entry<K, V>? = null

        internal var parent: Entry<K, V>? = null

        internal var size: Int = 1

        override fun setValue(newValue: V): V {
            return value.also { _value = newValue }
        }

        internal inline fun forEachAncestor(action: (Entry<K, V>) -> Unit) {
            var cur: Entry<K, V>? = this.parent
            while (cur != null) {
                action(cur)
                cur = cur.parent
            }
        }

        internal fun updateSize() {
            size = (left?.size ?: 0) + (right?.size ?: 0) + 1
        }

        internal fun minimal(): Entry<K, V> {
            var cur = this
            while (cur.left != null) cur = cur.left!!
            return cur
        }

        internal fun maximum(): Entry<K, V> {
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

    private val Entry<*,*>?.size: Int
        get() = this?.size ?: 0

    protected var root: Entry<K, V>? = null

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
            cur = Entry<K, V>(key).also { newNode ->
                newNode.parent = parent
                if (cmp < 0) {
                    parent!!.left = newNode
                } else {
                    parent!!.right = newNode
                }
                newNode.forEachAncestor { it.size++ }
            }
        }
        return cur
    }

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

    protected inner class EntryIterator<out T>(val getValue: (Entry<K, V>) -> T) : MutableIterator<T> {
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
            val (node, newRoot) = removeNode(result.key)
            check(node == result)
            root = newRoot
        }
    }

    override val size: Int
        get() = root.size

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> =
        object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
            override val size: Int
                get() = this@BinarySearchTree.size

            override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> {
                return EntryIterator { it }
            }

            override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
                throw UnsupportedOperationException()
            }

            override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean {
                val (node, newRoot) = removeNode(element.key)
                if (node != null) {
                    root = newRoot
                }
                return node != null
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
            val (node, newRoot) = removeNode(element)
            if (node != null) {
                root = newRoot
            }
            return node != null
        }
    }

    override fun clear() {
        root = null
    }

    override fun remove(key: K): V? {
        val (node, newRoot) = removeNode(key)
        if (node != null) {
            root = newRoot
        }
        return node?.value
    }

    override fun put(key: K, value: V): V? {
        var oldValue: V? = null
        if (root == null) {
            root = Entry<K, V>(key).apply {
                _value = value
            }
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
            fun Entry<K, V>.dfs() {
                if (left != null) {
                    appendLine("${key} -> ${left!!.key} [label = \"L\"];")
                }
                if (right != null) {
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