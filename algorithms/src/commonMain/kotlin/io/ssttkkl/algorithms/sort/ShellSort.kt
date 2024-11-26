package io.ssttkkl.algorithms.sort

import kotlin.math.min

class ShellSort(val steps: (size: Int) -> Sequence<Int> = DEFAULT_STEP) : Sort {
    companion object {
        val DEFAULT_STEP: (size: Int) -> Sequence<Int> = { size ->
            var h = 1
            while (h < size / 3) {
                h = 3 * h + 1
            }
            generateSequence(h) { if (it == 0) null else it / 3 }
        }
    }

    override fun <V> sort(list: MutableList<V>, comparator: Comparator<V>) {
        steps(list.size).forEach { st ->
            for (head in 0 until st) {
                for (i in head + st until list.size step st) {
                    val insertElement = list[i]
                    var j = head
                    while (j < i && comparator.compare(list[i], list[j]) > 0) {
                        j += st
                    }
                    if (j != i) {
                        var k = j
                        while (k < i) {
                            list[k + st] = list[k]
                            k += st
                        }
                        list[j] = insertElement
                    }
                }
            }
        }
    }
}