package io.ssttkkl.algorithms.testutil

import kotlin.random.Random

fun randomMap(size: Int = 2000): MutableMap<Int, Int> {
    return HashMap<Int, Int>().apply {
        while (this.size < size) {
            put(Random.nextInt(), Random.nextInt())
        }
    }
}


fun randomSet(size: Int = 2000): MutableSet<Int> {
    return HashSet<Int>().apply {
        while (this.size < size) {
            add(Random.nextInt())
        }
    }
}