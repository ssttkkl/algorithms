package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.inorderTraversal
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BinarySearchTreeTest {
    private fun randomMap(size: Int = 2000): MutableMap<Int, Int> {
        return HashMap<Int, Int>().apply {
            while (this.size < size) {
                put(Random.nextInt(), Random.nextInt())
            }
        }
    }

    @Test
    fun testRedBlackTree() {
        val map = randomMap(1000)
        val first = map.entries.first()
        map -= first.key

        var rbt = RedBlackTree<Int, Int>(first.key, first.value, compareBy { it })
        map.forEach { entry ->
            val result = rbt.insertNode(entry.key, entry.value)
            assertTrue(result.success)
            assertTrue(result.insertedNode.key == entry.key)
            assertTrue(result.insertedNode.value == entry.value)
            rbt = result.newRoot
        }

        assertEquals(
            (map.keys + first.key).sorted(),
            rbt.inorderTraversal().map { it.key }.toList()
        )

    }
}
