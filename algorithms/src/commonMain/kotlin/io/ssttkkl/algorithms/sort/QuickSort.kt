package io.ssttkkl.algorithms.sort

import io.ssttkkl.algorithms.utils.swap

object QuickSort : Sort {
    override fun <V> sort(list: MutableList<V>, comparator: Comparator<V>) {
        list.shuffle()
        sort(list, comparator, 0, list.size - 1)
    }

    private fun <V> sort(list: MutableList<V>, comparator: Comparator<V>, start: Int, endInclusive: Int) {
        if (start >= endInclusive) return
        val pivot = list[start]
        var i = start
        var j = endInclusive + 1
        while (true) {
            while (comparator.compare(list[++i], pivot) < 0) {
                if (i == endInclusive) break
            }
            while (comparator.compare(pivot, list[--j]) < 0) {
                if (j == start) break
            }
            if (i >= j) break
            list.swap(i, j)
        }
        list.swap(start, j)

        sort(list, comparator, start, j - 1)
        sort(list, comparator, j + 1, endInclusive)
    }
}