package io.ssttkkl.algorithms.sort

object SelectSort : Sort {
    override fun <V> sort(list: MutableList<V>, comparator: Comparator<V>) {
        for (i in 0 until list.size) {
            var selectIndex = i
            for (j in i + 1 until list.size) {
                if (comparator.compare(list[j], list[selectIndex]) < 0) {
                    selectIndex = j
                }
            }

            list.swap(i, selectIndex)
        }
    }
}