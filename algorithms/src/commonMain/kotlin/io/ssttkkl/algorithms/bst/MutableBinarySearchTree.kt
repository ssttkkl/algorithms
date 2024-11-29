package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.BinaryTree
import io.ssttkkl.algorithms.tree.size

abstract class BinarySearchTree<K, out V, out N : BinarySearchTree<K, V, N>>(
    val comparator: Comparator<K>
) : BinaryTree<N> {
    protected abstract val thisNode: N

    abstract val key: K

    abstract val value: V

    fun searchNode(key: K): N? {
        var cur: N? = thisNode
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
}

abstract class MutableBinarySearchTree<K, V, out N : MutableBinarySearchTree<K, V, N>>(
    comparator: Comparator<K>
) : BinaryTree<N>, BinarySearchTree<K, V, N>(comparator) {
    protected abstract fun createNode(key: K, value: V): N

    override var left: @UnsafeVariance N? = null
        protected set

    override var right: @UnsafeVariance N? = null
        protected set

    override var parent: @UnsafeVariance N? = null
        protected set

    override var size: Int = 1
        protected set

    internal fun rotateLeft(): N {
        val newRoot = right
        checkNotNull(newRoot)

        val p = newRoot.left
        newRoot.left = thisNode
        this.parent = newRoot
        this.right = p
        p?.parent = thisNode

        this.updateSize()
        newRoot.updateSize()
        return newRoot
    }

    internal fun rotateRight(): N {
        val newRoot = left
        checkNotNull(newRoot)

        val p = newRoot.right
        newRoot.right = thisNode
        this.parent = newRoot
        this.left = p
        p?.parent = thisNode

        this.updateSize()
        newRoot.updateSize()
        return newRoot
    }

    internal fun updateSize() {
        size = left.size + right.size + 1
    }

    /**
     * @return inserted node and new root after inserting node.
     * if node already exists, returning the existing node as first
     */
    fun insertNode(key: K, value: V): Pair<N, N?> {
        return insertNode(key, value, thisNode)
    }

    /**
     * @return inserted node and new root after inserting node.
     * if node already exists, returning the existing node as first
     */
    protected fun insertNode(key: K, value: V, root: @UnsafeVariance N?): Pair<N, N?> {
        if (root == null) {
            val newNode = createNode(key, value)
            return Pair(newNode, newNode)
        }

        val cmp = comparator.compare(key, root.key)
        when {
            cmp < 0 -> {
                val (newNode, newLeft) = insertNode(key, value, root.left)
                root.left = newLeft
                newLeft?.parent = root
                root.updateSize()
                return Pair(newNode, root)
            }

            cmp > 0 -> {
                val (newNode, newRight) = insertNode(key, value, root.right)
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
    fun removeNode(key: K): Pair<N?, N?> {
        return removeNode(key, thisNode)
    }

    /**
     * @return removed node and new root after removing node,
     * if node not existing, returning `null` value as first
     */
    protected fun removeNode(key: K, root: @UnsafeVariance N?): Pair<N?, N?> {
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
                val replacement = root.left!!.maximumNode()
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
                val replacement = root.right!!.maximumNode()
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
}

fun <N : BinarySearchTree<*, *, N>> N.minimalNode(): N {
    var cur = this
    while (cur.left != null) cur = cur.left!!
    return cur
}

fun <N : BinarySearchTree<*, *, N>> N.maximumNode(): N {
    var cur = this
    while (cur.right != null) cur = cur.right!!
    return cur
}

fun <N : BinarySearchTree<*, *, N>> N.predecessor(): N? {
    // 情况 1：如果 p 有左子树，前驱是左子树中的最右节点
    if (left != null) {
        return left!!.maximumNode()
    }

    // 情况 2：如果 p 没有左子树，前驱是向上查找直到找到一个是父节点右子树的节点
    var cur = this
    while (cur.parent != null && cur != cur.parent!!.right) {
        cur = cur.parent!!
    }
    return cur.parent
}

fun <N : BinarySearchTree<*, *, N>> N.successor(): N? {
    // 情况 1：如果 p 有右子树，后继是右子树中的最左节点
    if (right != null) {
        return right!!.minimalNode()
    }

    // 情况 2：如果 p 没有右子树，后继是向上查找直到找到一个是父节点左子树的节点
    var cur = this
    while (cur.parent != null && cur != cur.parent!!.left) {
        cur = cur.parent!!
    }
    return cur.parent
}


fun <K, N : BinarySearchTree<K, *, N>> N.lowerEntry(key: K): N? {
    val floor = floorEntry(key) ?: return null
    if (floor.key == key) {
        return floor.predecessor()
    }
    return floor
}

fun <K, N : BinarySearchTree<K, *, N>> N.floorEntry(key: K): N? {
    var candidate: N? = null

    var cur: N? = this
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

fun <K, N : BinarySearchTree<K, *, N>> N.ceilingEntry(key: K): N? {
    var candidate: N? = null

    var cur: N? = this
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

fun <K, N : BinarySearchTree<K, *, N>> N.higherEntry(key: K): N? {
    val ceiling = ceilingEntry(key) ?: return null
    if (ceiling.key == key) {
        return ceiling.successor()
    }
    return ceiling
}

fun <K, N : BinarySearchTree<K, *, N>> N.entryOfRank(rank: Int): N? {
    var cur: N? = this
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

fun <K, N : BinarySearchTree<K, *, N>> N.rankOfKey(key: K): Int {
    var cur: N? = this
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
