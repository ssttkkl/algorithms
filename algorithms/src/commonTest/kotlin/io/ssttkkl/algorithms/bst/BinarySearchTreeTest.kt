package io.ssttkkl.algorithms.bst

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class BinarySearchTreeTest {
    private fun randomMap(): MutableMap<Int, Int> {
        val size = Random.nextInt(2000, 4000)
        return HashMap<Int, Int>().apply {
            repeat(size) {
                put(Random.nextInt(), Random.nextInt())
            }
        }
    }

    @Test
    fun testBST() {
        val map = randomMap()
        val bst = BinarySearchTree<Int, Int>()

        map.forEach {
            bst[it.key] = it.value
        }

        assertEquals(map.size, bst.size)
        assertEquals(map, bst)
        assertEquals(map.keys, bst.keys)
        assertEquals(map.values.sorted(), bst.values.sorted())
        assertEquals(bst.keys.toList(), bst.keys.sorted())

        val keysToRemove = map.keys.take(map.size / 2).toSet()
        map -= keysToRemove
        bst -= keysToRemove

        assertEquals(map.size, bst.size)
        assertEquals(map, bst)
        assertEquals(map.keys, bst.keys)
        assertEquals(map.values.sorted(), bst.values.sorted())
        assertEquals(bst.keys.toList(), bst.keys.sorted())

        bst.clear()
        assertTrue(bst.isEmpty())
    }
}
