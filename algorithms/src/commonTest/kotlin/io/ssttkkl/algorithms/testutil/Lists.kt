package io.ssttkkl.algorithms.testutil

fun <T: Comparable<T>> List<T>.isSorted(): Boolean {
    for (i in 1 until size) {
        if (this[i - 1] > this[i]) {
            return false
        }
    }
    return true
}
