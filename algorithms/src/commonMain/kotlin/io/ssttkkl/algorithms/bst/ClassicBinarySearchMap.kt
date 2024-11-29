package io.ssttkkl.algorithms.bst

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
