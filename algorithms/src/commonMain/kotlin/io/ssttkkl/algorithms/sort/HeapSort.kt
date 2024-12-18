package io.ssttkkl.algorithms.sort

import io.ssttkkl.algorithms.queue.HeapPriorityQueue

object HeapSort : Sort {
    override fun <V> sort(list: MutableList<V>, comparator: Comparator<V>) {
        val priorityQueue = HeapPriorityQueue(comparator, list)
        for (i in list.indices) {
            list[i] = priorityQueue.pop()
        }
    }
}