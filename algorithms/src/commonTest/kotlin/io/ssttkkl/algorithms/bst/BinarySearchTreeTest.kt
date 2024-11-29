package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.inorderTraversal
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class BinarySearchTreeTest {
    private fun randomMap(size: Int = 2000): MutableMap<Int, Int> {
        return HashMap<Int, Int>().apply {
            while(this.size < size) {
                put(Random.nextInt(), Random.nextInt())
            }
        }
    }
}
