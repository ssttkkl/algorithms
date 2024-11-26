package io.ssttkkl.algorithms.sort

interface Sort {
    fun <V> sort(list: MutableList<V>, comparator: Comparator<V>)
}

fun <V : Comparable<V>> Sort.sort(list: MutableList<V>) {
    sort(list, Comparator { o1, o2 -> o1.compareTo(o2) })
}

fun <V> Sort.sorted(coll: Collection<V>, comparator: Comparator<V>): List<V> {
    val copied = ArrayList(coll)
    sort(copied, comparator)
    return copied
}

fun <V : Comparable<V>> Sort.sorted(coll: Collection<V>): List<V> {
    return sorted(coll, Comparator { o1, o2 -> o1.compareTo(o2) })
}