package io.ssttkkl.algorithms.queue

import io.ssttkkl.algorithms.utils.swap

class HeapPriorityQueue<E> private constructor(
    private val comparator: Comparator<E>,
    private val heap: MutableList<E>
) : PriorityQueue<E> {
    constructor(comparator: Comparator<E>) : this(comparator, arrayListOf())
    constructor(comparator: Comparator<E>, elements: Collection<E>) : this(comparator, ArrayList(elements))

    init {
        var i = heap.size / 2
        while (i >= 0) {
            sink(i)
            i--
        }
    }

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

    private fun sink(index: Int) {
        var p = index
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
    }

    private fun swim(index: Int) {
        var p = index
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

    override fun pop(): E {
        heap.swap(0, heap.size - 1)
        val element = heap.removeLast()
        sink(0)
        return element
    }

    override fun push(element: E) {
        heap.add(element)
        swim(heap.size - 1)
    }
}