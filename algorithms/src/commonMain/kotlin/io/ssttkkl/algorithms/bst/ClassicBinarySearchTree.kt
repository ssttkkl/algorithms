package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.size

class ClassicBinarySearchTree<K, V>(
    key: K, value: V,
    comparator: Comparator<K>
) : MutableBinarySearchTree<K, V, ClassicBinarySearchTree<K, V>>(key, value, comparator) {
    override val thisNode: ClassicBinarySearchTree<K, V>
        get() = this

    override fun createNode(key: K, value: V): ClassicBinarySearchTree<K, V> {
        return ClassicBinarySearchTree(key, value, comparator)
    }

    /**
     * @return inserted node and new root after inserting node.
     * if node already exists, returning the existing node as first
     */
    override fun insertNode(
        key: K, value: V,
        root: @UnsafeVariance ClassicBinarySearchTree<K, V>?
    ): Pair<ClassicBinarySearchTree<K, V>, ClassicBinarySearchTree<K, V>?> {
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
    override fun removeNode(
        key: K,
        root: @UnsafeVariance ClassicBinarySearchTree<K, V>?
    ): Pair<ClassicBinarySearchTree<K, V>?, ClassicBinarySearchTree<K, V>?> {
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
                val replacement = root.right!!.minimalNode()
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