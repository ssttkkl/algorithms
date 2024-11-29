package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.size
import kotlin.random.Random

class Treap<K, V>(
    key: K, value: V,
    comparator: Comparator<K>
) : MutableBinarySearchTree<K, V, Treap<K, V>>(key, value, comparator) {
    companion object {
        val random: Random = Random
    }

    var priority: Int = random.nextInt()
        private set

    private val Treap<K,V>?.priority: Int
        get() = this?.priority ?: Int.MAX_VALUE

    override val thisNode: Treap<K, V>
        get() = this

    private inner class InsertResultImpl(
        override var success: Boolean,
        override var insertedNode: Treap<K, V>,
        override var newRoot: Treap<K, V>
    ): InsertResult<Treap<K, V>>

    /**
     * @return inserted node and new root after inserting node.
     * if node already exists, returning the existing node as first
     */
    override fun insertNode(
        key: K, value: V,
        root: @UnsafeVariance Treap<K, V>?
    ): InsertResult<Treap<K, V>> {
        if (root == null) {
            val newNode = Treap(key, value, comparator)
            return InsertResultImpl(true, newNode, newNode)
        }

        val cmp = comparator.compare(key, root.key)
        when {
            cmp < 0 -> {
                val result = insertNode(key, value, root.left) as InsertResultImpl
                root.left = result.newRoot
                result.newRoot.parent = root
                root.updateSize()

                result.newRoot = root

                if (root.left.priority < root.priority) {
                    result.newRoot = root.rotateRight()
                }

                return result
            }

            cmp > 0 -> {
                val result = insertNode(key, value, root.right) as InsertResultImpl
                root.right = result.newRoot
                result.newRoot.parent = root
                root.updateSize()

                result.newRoot = root

                if (root.right.priority < root.priority) {
                    result.newRoot = root.rotateLeft()
                }

                return result
            }

            else -> {
                return InsertResultImpl(false, root, root)
            }
        }
    }

    private inner class RemoveResultImpl(
        override var success: Boolean,
        override var removedNode: Treap<K, V>?,
        override var newRoot: Treap<K, V>?
    ): RemoveResult<Treap<K, V>>

    /**
     * @return removed node and new root after removing node,
     * if node not existing, returning `null` value as first
     */
    override fun removeNode(
        key: K,
        root: @UnsafeVariance Treap<K, V>?
    ): RemoveResult<Treap<K, V>> {
        if (root == null) return RemoveResultImpl(false, null, null)

        val cmp: Int = comparator.compare(key, root.key)
        when {
            cmp < 0 -> {
                val result = removeNode(key, root.left) as RemoveResultImpl
                root.left = result.newRoot
                result.newRoot?.parent = root
                root.updateSize()

                result.newRoot = root
                return result
            }

            cmp > 0 -> {
                val result = removeNode(key, root.right) as RemoveResultImpl
                root.right = result.newRoot
                result.newRoot?.parent = root
                root.updateSize()

                result.newRoot = root
                return result
            }

            root.left == null -> {
                root.right?.parent = null
                return RemoveResultImpl(true, root, root.right)
            }

            root.right == null -> {
                root.left?.parent = null
                return RemoveResultImpl(true, root, root.left)
            }

            root.left.size > root.right.size -> {
                val replacement = root.left!!.maximumNode()
                val replacementResult = removeNode(replacement.key, root.left) as RemoveResultImpl
                replacement.priority = root.priority
                replacement.left = replacementResult.newRoot
                replacementResult.newRoot?.parent = replacement
                replacement.right = root.right
                root.right?.parent = replacement
                replacement.updateSize()
                replacement.parent = null

                return RemoveResultImpl(true, root, replacement)
            }

            else -> {
                val replacement = root.right!!.minimalNode()
                val replacementResult = removeNode(replacement.key, root.right) as RemoveResultImpl
                replacement.left = root.left
                root.left?.parent = replacement
                replacement.right = replacementResult.newRoot
                replacementResult.newRoot?.parent = replacement
                replacement.updateSize()
                replacement.parent = null

                return RemoveResultImpl(true, root, replacement)
            }
        }
    }
}

class TreapMap<K, V>(keyComparator: Comparator<K>) :
    BinarySearchMap<
            K,
            V,
            Treap<K, BinarySearchMap.Entry<K, V>>
            >(keyComparator) {
    override fun createNode(entry: Entry<K, V>): Treap<K, Entry<K, V>> {
        val node = Treap(entry.key, entry, keyComparator)
        return node
    }
}

fun <K : Comparable<K>, V> TreapMap(): TreapMap<K, V> =
    TreapMap<K, V>(compareBy<K> { it })


class TreapSet<E>(comparator: Comparator<E>) :
    BinarySearchSet<
            E,
            Treap<E, Unit>
            >(comparator) {
    override fun createNode(e: E): Treap<E, Unit> {
        val node = Treap(e, Unit, comparator)
        return node
    }
}

fun <E : Comparable<E>> TreapSet(): TreapSet<E> =
    TreapSet<E>(compareBy<E> { it })

