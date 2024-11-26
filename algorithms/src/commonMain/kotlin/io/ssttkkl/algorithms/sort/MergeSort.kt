package io.ssttkkl.algorithms.sort

import java.lang.IllegalArgumentException

object MergeSort : Sort {
    override fun <V> sort(list: MutableList<V>, comparator: Comparator<V>) {
        sort(list, comparator, 0, list.size - 1)
    }

    private fun <V> sort(list: MutableList<V>, comparator: Comparator<V>, start: Int, endInclusive: Int) {
        if (start >= endInclusive) return
        val mid = (start + endInclusive + 1) / 2
        if (start <= mid - 1) {
            sort(list, comparator, start, mid - 1)
        }
        if (mid <= endInclusive) {
            sort(list, comparator, mid, endInclusive)
        }
        mergeInplace(list, start, mid, endInclusive, comparator)
    }

    private fun <V> merge(
        listA: List<V>, startA: Int, endInclusiveA: Int,
        listB: List<V>, startB: Int, endInclusiveB: Int,
        comparator: Comparator<V>
    ): List<V> {
        check(startA <= endInclusiveA)
        check(startB <= endInclusiveB)

        val dest = ArrayList<V>(endInclusiveA - startA + 1 + endInclusiveB - startB)
        var i = startA
        var j = startB
        var k = 0
        while (i <= endInclusiveA && j <= endInclusiveB) {
            if (comparator.compare(listA[i], listB[j]) < 0) {
                dest.add(listA[i])
                i++
            } else {
                dest.add(listA[j])
                j++
            }
            k++
        }
        while (i <= endInclusiveA) {
            dest.add(listA[i])
            i++
            k++
        }
        while (j <= endInclusiveB) {
            dest.add(listA[j])
            j++
            k++
        }
        return dest
    }

    private fun <V> mergeInplace(
        list: MutableList<V>, start: Int, mid: Int, endInclusive: Int,
        comparator: Comparator<V>
    ) {
        if (mid == start) return
        val mergeResult = merge(list, start, mid - 1, list, mid, endInclusive, comparator)
        list.copyFrom(mergeResult, 0, mid - start, start)
        list.copyFrom(mergeResult, mid - start, endInclusive - mid + 1, mid)
    }
}