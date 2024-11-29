package io.ssttkkl.algorithms.bst

class ClassicBinarySearchTree<K, V>(
    override val key: K,
    override val value: V,
    comparator: Comparator<K>
) : MutableBinarySearchTree<K, V, ClassicBinarySearchTree<K, V>>(comparator) {
    override val thisNode: ClassicBinarySearchTree<K, V>
        get() = this

    override fun createNode(key: K, value: V): ClassicBinarySearchTree<K, V> {
        return ClassicBinarySearchTree(key, value, comparator)
    }
}