package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.utils.BidirectionIterator
import io.ssttkkl.algorithms.utils.MutableBidirectionIterator


class BinarySearchTreeIterator<N : BinarySearchTree<*, *, N>, V>(
    private val root: () -> N?,
    private val mapper: (N) -> V
) : BidirectionIterator<V> {
    private var nextNode: N? = root()?.minimalNode()
    private var prevNode: N? = null

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

class MutableBinarySearchTreeIterator<N : MutableBinarySearchTree<*, *, N>, V>(
    private val root: () -> N?,
    private val onRootChange: (N?) -> Unit,
    private val mapper: (N) -> V
) : MutableBidirectionIterator<V> {

    private var nextNode: N? = root()?.minimalNode()
    private var prevNode: N? = null

    private var lastNextRetNode: N? = null
    private var lastPrevRetNode: N? = null

    override fun hasNext(): Boolean {
        return nextNode != null
    }

    override fun next(): V {
        val result = nextNode ?: throw NoSuchElementException()
        nextNode = result.successor(ancestorBound = root())
        prevNode = result

        lastNextRetNode = result
        lastPrevRetNode = null

        return mapper(result)
    }

    override fun hasPrevious(): Boolean {
        return prevNode != null
    }

    override fun previous(): V {
        val result = prevNode ?: throw NoSuchElementException()
        prevNode = result.predecessor(ancestorBound = root())
        nextNode = result

        lastNextRetNode = null
        lastPrevRetNode = result

        return mapper(result)
    }

    override fun remove() {
        if (lastPrevRetNode != null) {
            nextNode = nextNode?.successor(ancestorBound = root())

            val curRoot = root() ?: error("root is null")
            val result = curRoot.removeNode(lastPrevRetNode!!.key)
            if (result.newRoot != curRoot) {
                onRootChange(result.newRoot)
            }
        } else if (lastNextRetNode != null) {
            prevNode = prevNode?.predecessor(ancestorBound = root())

            val curRoot = root() ?: error("root is null")
            val result = curRoot.removeNode(lastNextRetNode!!.key)
            if (result.newRoot != curRoot) {
                onRootChange(result.newRoot)
            }
        } else {
            throw IllegalStateException()
        }

        lastNextRetNode = null
        lastPrevRetNode = null
    }
}