package io.ssttkkl.algorithms.bst

class RedBlackTree<K, V>(
    override val key: K,
    override var value: V,
    comparator: Comparator<K>
) : MutableBinarySearchTree<K, V, RedBlackTree<K, V>>(comparator) {
    var red: Boolean = false
        private set

    override val thisNode: RedBlackTree<K, V>
        get() = this

    override fun rotateLeft(): RedBlackTree<K, V> {
        val newNode = super.rotateLeft()
        newNode.red = thisNode.red
        thisNode.red = true
        return newNode
    }

    override fun rotateRight(): RedBlackTree<K, V> {
        val newNode = super.rotateRight()
        newNode.red = thisNode.red
        thisNode.red = true
        return newNode
    }

    private fun flipColors() {
        left?.let { left ->
            left.red = !left.red
        }
        right?.let { right ->
            right.red = !right.red
        }
        this.red = !this.red
    }

    private inner class InsertResultImpl(
        override var success: Boolean,
        override var insertedNode: RedBlackTree<K, V>,
        override var newRoot: RedBlackTree<K, V>
    ): InsertResult<RedBlackTree<K, V>>

    /**
     * @return inserted node and new root after inserting node.
     * if node already exists, returning the existing node as first
     */
    override fun insertNode(
        key: K, value: V,
        root: @UnsafeVariance RedBlackTree<K, V>?
    ): InsertResult<RedBlackTree<K, V>> {
        if (root == null) {
            val newNode = RedBlackTree(key, value, comparator)
            newNode.red = true
            return InsertResultImpl(true, newNode, newNode)
        }

        val cmp = comparator.compare(key, root.key)
        val result = when {
            cmp < 0 -> {
                val result = insertNode(key, value, root.left) as InsertResultImpl
                root.left = result.newRoot
                result.newRoot.parent = root
                root.updateSize()

                result.newRoot = root
                result
            }

            cmp > 0 -> {
                val result = insertNode(key, value, root.right) as InsertResultImpl
                root.right = result.newRoot
                result.newRoot.parent = root
                root.updateSize()

                result.newRoot = root
                result
            }

            else -> {
                InsertResultImpl(false, root, root)
            }
        }

        if (result.success) {
            if (result.newRoot.left?.red != true && result.newRoot.right?.red == true) {
                result.newRoot = result.newRoot.rotateLeft()
            }
            if (result.newRoot.left?.red == true && result.newRoot.left?.left?.red == true) {
                result.newRoot = result.newRoot.rotateRight()
            }
            if (result.newRoot.left?.red == true && result.newRoot.right?.red == true) {
                result.newRoot.flipColors()
            }
            result.newRoot.updateSize()
        }

        return result
    }

    private inner class RemoveResultImpl(
        override var success: Boolean,
        override var removedNode: RedBlackTree<K, V>?,
        override var newRoot: RedBlackTree<K, V>?
    ): RemoveResult<RedBlackTree<K, V>>

    /**
     * @return removed node and new root after removing node,
     * if node not existing, returning `null` value as first
     */
    override fun removeNode(
        key: K,
        root: @UnsafeVariance RedBlackTree<K, V>?
    ): RemoveResult<RedBlackTree<K, V>> {
        throw NotImplementedError()
    }
}
