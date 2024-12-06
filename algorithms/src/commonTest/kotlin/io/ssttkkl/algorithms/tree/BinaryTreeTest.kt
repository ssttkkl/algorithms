package io.ssttkkl.algorithms.tree

import kotlin.test.Test
import kotlin.test.assertEquals

class BinaryTreeTest {

    private val tree = SimpleBinaryTree(
        value = 4,
        SimpleBinaryTree(
            value = 2,
            SimpleBinaryTree(value = 1),
            SimpleBinaryTree(value = 3)
        ),
        SimpleBinaryTree(
            value = 6,
            SimpleBinaryTree(value = 5),
            SimpleBinaryTree(value = 7),
        ),
    )

    @Test
    fun testPreorderTraversal() {
        val result = tree.preorderTraversal().map { it.value }.toList()
        assertEquals(listOf(4, 2, 1, 3, 6, 5, 7), result)
    }

    @Test
    fun testInorderTraversal() {
        val result = tree.inorderTraversal().map { it.value }.toList()
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), result)
    }

    @Test
    fun testPostorderTraversal() {
        val result = tree.postorderTraversal().map { it.value }.toList()
        assertEquals(listOf(1, 3, 2, 5, 7, 6, 4), result)
    }

    @Test
    fun testCalcHeight() {
        val height = tree.calcHeight()
        assertEquals(2, height)
    }
}
