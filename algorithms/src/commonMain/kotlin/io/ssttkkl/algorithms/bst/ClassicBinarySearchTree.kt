package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.size

class ClassicBinarySearchTree<K, V>(
    override val key: K,
    override var value: V,
    comparator: Comparator<K>
) : MutableBinarySearchTree<K, V, ClassicBinarySearchTree<K, V>>(comparator) {
    override val thisNode: ClassicBinarySearchTree<K, V>
        get() = this

    private inner class InsertResultImpl(
        override var success: Boolean,
        override var insertedNode: ClassicBinarySearchTree<K, V>,
        override var newRoot: ClassicBinarySearchTree<K, V>
    ): InsertResult<ClassicBinarySearchTree<K, V>>

    /**
     * @return inserted node and new root after inserting node.
     * if node already exists, returning the existing node as first
     */
    override fun insertNode(
        key: K, value: V,
        root: @UnsafeVariance ClassicBinarySearchTree<K, V>?
    ): InsertResult<ClassicBinarySearchTree<K, V>> {
        if (root == null) {
            val newNode = ClassicBinarySearchTree(key, value, comparator)
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
                return result
            }

            cmp > 0 -> {
                val result = insertNode(key, value, root.right) as InsertResultImpl
                root.right = result.newRoot
                result.newRoot.parent = root
                root.updateSize()

                result.newRoot = root
                return result
            }

            else -> {
                return InsertResultImpl(false, root, root)
            }
        }
    }

    private inner class RemoveResultImpl(
        override var success: Boolean,
        override var removedNode: ClassicBinarySearchTree<K, V>?,
        override var newRoot: ClassicBinarySearchTree<K, V>?
    ): RemoveResult<ClassicBinarySearchTree<K, V>>

    /**
     * @return removed node and new root after removing node,
     * if node not existing, returning `null` value as first
     */
    override fun removeNode(
        key: K,
        root: @UnsafeVariance ClassicBinarySearchTree<K, V>?
    ): RemoveResult<ClassicBinarySearchTree<K, V>> {
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

class ClassicBinarySearchMap<K, V>(keyComparator: Comparator<K>) :
    BinarySearchMap<
            K,
            V,
            ClassicBinarySearchTree<K, BinarySearchMap.Entry<K, V>>
            >(keyComparator) {
    override fun createNode(entry: Entry<K, V>): ClassicBinarySearchTree<K, Entry<K, V>> {
        val node = ClassicBinarySearchTree(entry.key, entry, keyComparator)
        return node
    }
}

fun <K : Comparable<K>, V> ClassicBinarySearchMap(): ClassicBinarySearchMap<K, V> =
    ClassicBinarySearchMap<K, V>(compareBy<K> { it })


class ClassicBinarySearchSet<E>(comparator: Comparator<E>) :
    BinarySearchSet<
            E,
            ClassicBinarySearchTree<E, Unit>
            >(comparator) {
    override fun createNode(e: E): ClassicBinarySearchTree<E, Unit> {
        val node = ClassicBinarySearchTree(e, Unit, comparator)
        return node
    }
}

fun <E : Comparable<E>> ClassicBinarySearchSet(): ClassicBinarySearchSet<E> =
    ClassicBinarySearchSet<E>(compareBy<E> { it })

