package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.inorderTraversal
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class BinarySearchSetTest {
    private fun randomSet(size: Int = 2000): MutableSet<Int> {
        return HashSet<Int>().apply {
            while (this.size < size) {
                add(Random.nextInt())
            }
        }
    }

    @Test
    fun testAsNormalMap() {
        val map = randomSet()
        val bst = ClassicBinarySearchSet<Int>()

        bst.addAll(map)

        assertEquals(map.size, bst.size)
        assertEquals(map, bst)

        val keysToRemove = map.take(map.size / 2).toSet()
        map -= keysToRemove
        bst -= keysToRemove

        assertEquals(map.size, bst.size)
        assertEquals(map, bst)

        bst.clear()
        assertTrue(bst.isEmpty())
    }

    @Test
    fun testAsBinaryTree() {
        val bst = ClassicBinarySearchSet<Int>()
        bst.addAll(randomSet())

        assertEquals(
            bst.sorted(),
            bst.root!!.inorderTraversal().map { it.key }.toList()
        )
    }
}
