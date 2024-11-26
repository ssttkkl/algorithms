package io.ssttkkl.algorithms.datastructure.queue

import io.ssttkkl.algorithms.utils.swap

class HeapPriorityQueue<E>(private val comparator: Comparator<E>) : PriorityQueue<E> {
    private val heap = ArrayList<E>()

    private fun parentIndex(nodeIndex: Int): Int {
        return (nodeIndex + 1) / 2 - 1
    }

    private fun childIndex(nodeIndex: Int): Int {
        return nodeIndex * 2 + 1
    }

    override val size: Int
        get() = heap.size

    override fun peek(): E {
        return heap.first()
    }

    override fun pop(): E {
        val element = heap.first()
        heap.swap(0, heap.size - 1)
        heap.removeLast()

        var p = 0
        while (true) {
            val child = childIndex(p)
            if (child >= heap.size) break

            val minChild = if (child + 1 < heap.size) {
                if (comparator.compare(heap[child], heap[child + 1]) < 0) child
                else child + 1
            } else {
                child
            }

            if (comparator.compare(heap[minChild], heap[p]) < 0) {
                heap.swap(p, minChild)
                p = minChild
            } else {
                break
            }
        }

        return element
    }

    override fun push(element: E) {
        heap.addLast(element)

        var p = heap.size - 1
        while (p != 0) {
            val parent = parentIndex(p)
            if (comparator.compare(heap[p], heap[parent]) < 0) {
                heap.swap(p, parent)
                p = parent
            } else {
                break
            }
        }
    }

}