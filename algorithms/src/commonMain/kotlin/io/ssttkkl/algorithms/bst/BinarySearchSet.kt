package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.bst.BinarySearchMap
import io.ssttkkl.algorithms.tree.size

abstract class BinarySearchSet<
        E,
        out N : MutableBinarySearchTree<
                E,
                Unit,
                N>
        > internal constructor(
    protected val comparator: Comparator<E>
) : AbstractMutableSet<E>(), NavigableSet<E> {

    protected abstract fun createNode(e: E): N

    var root: @UnsafeVariance N? = null
        private set

    override val size: Int
        get() = root.size

    override fun iterator(): MutableBidirectionIterator<E> {
        return MutableBinarySearchTreeIterator<N, E>(
            ::root,
            { this@BinarySearchSet.root = it },
            { it.key }
        )
    }

    override fun lower(e: E): E? {
        return root?.lowerNode(e)?.key
    }

    override fun floor(e: E): E? {
        return root?.floorNode(e)?.key
    }

    override fun ceiling(e: E): E? {
        return root?.ceilingNode(e)?.key
    }

    override fun higher(e: E): E? {
        return root?.higherNode(e)?.key
    }

    override fun first(): E {
        return root?.minimalNode()?.key ?: throw NoSuchElementException()
    }

    override fun last(): E {
        return root?.maximumNode()?.key ?: throw NoSuchElementException()
    }

    override fun clear() {
        root = null
    }

    override fun remove(element: E): Boolean {
        root.let { root ->
            if (root == null) return false

            val result = root.removeNode(element)
            this@BinarySearchSet.root = result.newRoot
            return result.success
        }
    }

    override fun add(element: E): Boolean {
        root.let { root ->
            if (root == null) {
                this@BinarySearchSet.root = createNode(element)
                return true
            }
            val result = root.insertNode(element, Unit)
            this@BinarySearchSet.root = result.newRoot
            return result.success
        }
    }

    override fun contains(element: E): Boolean {
        return root?.searchNode(element) != null
    }
}