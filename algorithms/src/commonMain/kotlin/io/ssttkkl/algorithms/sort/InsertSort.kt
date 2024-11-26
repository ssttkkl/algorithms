package io.ssttkkl.algorithms.sort

object InsertSort : Sort {
    override fun <V> sort(list: MutableList<V>, comparator: Comparator<V>) {
        for (i in 1 until list.size) {
            val insertElement = list[i]
            var j = 0
            while (j < i && comparator.compare(list[i], list[j]) > 0) {
                j++
            }

            if (j != i) {
                var k = j
                while (k < i) {
                    list[k + 1] = list[k]
                    k++
                }
                list[j] = insertElement
            }
        }
    }
}