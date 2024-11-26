package io.ssttkkl.algorithms.queue

interface PriorityQueue<E> {
    val size: Int
    fun peek(): E
    fun pop(): E
    fun push(element: E)
}