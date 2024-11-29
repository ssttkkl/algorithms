package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.inorderTraversal
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class BinarySearchMapTest {
    private fun randomMap(): MutableMap<Int, Int> {
        val size = Random.nextInt(2000, 4000)
        return HashMap<Int, Int>().apply {
            repeat(size) {
                put(Random.nextInt(), Random.nextInt())
            }
        }
    }

    @Test
    fun testAsNormalMap() {
        val map = randomMap()
        val bst = ClassicBinarySearchMap<Int, Int>()

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

    @Test
    fun testAsBinaryTree() {
        val bst = ClassicBinarySearchMap<Int, Int>()
        bst.putAll(randomMap())

        assertEquals(
            bst.keys.sorted(),
            bst.root!!.inorderTraversal().map { it.key }.toList()
        )
    }

    @Test
    fun testAsSortedMap() {
        val bst = ClassicBinarySearchMap<Int, Int>()

    }
}
