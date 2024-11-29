package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.utils.BidirectionIterator
import io.ssttkkl.algorithms.utils.MutableBidirectionIterator


sealed class AbsBinarySearchTreeIterator<N : BinarySearchTree<*, *, N>, V>(
    protected val root: () -> N?,
    protected val mapper: (N) -> V
) : BidirectionIterator<V> {
    protected var nextNode: N? = root()?.minimalNode()
        private set

    protected var prevNode: N? = null
        private set

    protected fun clearNext() {
        nextNode = null
    }

    protected fun clearPrev() {
        prevNode = null
    }

    override fun hasNext(): Boolean {
        return nextNode != null
    }

    override fun next(): V {
        val result = nextNode ?: throw NoSuchElementException()
        nextNode = result.successor(ancestorBound = root())
        prevNode = result
        return mapper(result)
    }

    override fun hasPrevious(): Boolean {
        return prevNode != null
    }

    override fun previous(): V {
        val result = prevNode ?: throw NoSuchElementException()
        prevNode = result.predecessor(ancestorBound = root())
        nextNode = result
        return mapper(result)
    }
}

class BinarySearchTreeIterator<N : BinarySearchTree<*, *, N>, V>(
    root: N?,
    mapper: (N) -> V
) : AbsBinarySearchTreeIterator<N, V>({ root }, mapper)

class MutableBinarySearchTreeIterator<N : MutableBinarySearchTree<*, *, N>, V>(
    root: () -> N?,
    private val onRootChange: (N?) -> Unit,
    mapper: (N) -> V
) : AbsBinarySearchTreeIterator<N, V>(root, mapper), MutableBidirectionIterator<V> {
    override fun remove() {
        val prev = prevNode ?: throw NoSuchElementException()
        val curRoot = root() ?: error("root is null")
        val result = curRoot.removeNode(prev.key)
        if (result.newRoot != curRoot) {
            onRootChange(result.newRoot)
        }
        clearPrev()
    }
}