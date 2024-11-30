package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.tree.calcHeight
import io.ssttkkl.algorithms.tree.inorderTraversal
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class BinarySearchTreeTest<T: MutableBinarySearchTree<Int,Int,T>> {
    abstract fun createBst(key: Int, value: Int): T

    private fun randomMap(size: Int = 100000): MutableMap<Int, Int> {
        return HashMap<Int, Int>().apply {
            while (this.size < size) {
                put(Random.nextInt(), Random.nextInt())
            }
        }
    }

    @Test
    fun testTree() {
        val map = randomMap(100000)
        val first = map.entries.first()
        val mapWithoutFirst = map - first.key

        var rbt = createBst(first.key, first.value)
        mapWithoutFirst.forEach { entry ->
            val result = rbt.insertNode(entry.key, entry.value)
            assertTrue(result.success)
            assertTrue(result.insertedNode.key == entry.key)
            assertTrue(result.insertedNode.value == entry.value)
            rbt = result.newRoot
        }
        assertTrue(rbt.size == map.size)

        // 重复插入
        map.forEach { entry ->
            val node = rbt.searchNode(entry.key)
            val result = rbt.insertNode(entry.key, entry.value)
            assertFalse(result.success)
            assertTrue(result.insertedNode == node)
            assertTrue(result.newRoot == rbt)
            assertTrue(rbt.size == map.size)
        }

        assertEquals(
            map.keys.sorted(),
            rbt.inorderTraversal().map { it.key }.toList()
        )

        println("size: ${rbt.size}")
        println("height: ${rbt.calcHeight()}")

        val keysToRemove = map.keys.take(map.size / 2)
        keysToRemove.forEach { key ->
            val result = rbt.removeNode(key)
            assertTrue(result.success)
            assertTrue(result.removedNode?.key == key)
            assertTrue(result.removedNode?.value == map[key])
            rbt = result.newRoot!!

            assertTrue(rbt.searchNode(key) == null)

            map.remove(key)
        }

        assertEquals(
            map.keys.sorted(),
            rbt.inorderTraversal().map { it.key }.toList()
        )

        println("size: ${rbt.size}")
        println("height: ${rbt.calcHeight()}")
    }
}

class ClassicBinarySearchTreeTest: BinarySearchTreeTest<ClassicBinarySearchTree<Int,Int>>() {
    override fun createBst(
        key: Int,
        value: Int
    ): ClassicBinarySearchTree<Int, Int> {
        return ClassicBinarySearchTree(key, value, compareBy { it })
    }
}
//class RedBlackTreeTest: BinarySearchTreeTest<RedBlackTree<Int,Int>>() {
//    override fun createBst(
//        key: Int,
//        value: Int
//    ): RedBlackTree<Int, Int> {
//        return RedBlackTree(key, value, compareBy { it })
//    }
//}

class TreapTest: BinarySearchTreeTest<Treap<Int,Int>>() {
    override fun createBst(
        key: Int,
        value: Int
    ): Treap<Int, Int> {
        return Treap(key, value, compareBy { it })
    }
}
