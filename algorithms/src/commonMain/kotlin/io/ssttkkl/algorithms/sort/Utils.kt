package io.ssttkkl.algorithms.sort

internal fun <E> MutableList<E>.swap(indexI: Int, indexJ: Int) {
    val temp = this[indexI]
    this[indexI] = this[indexJ]
    this[indexJ] = temp
}