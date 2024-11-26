package io.ssttkkl.algorithms.datastructure.queue

interface PriorityQueue<E> {
    val size: Int
    fun peek(): E
    fun pop(): E
    fun push(element: E)
}