package io.ssttkkl.algorithms.bst

import io.ssttkkl.algorithms.testutil.isSorted
import io.ssttkkl.algorithms.testutil.randomSet
import io.ssttkkl.algorithms.tree.inorderTraversal
import kotlin.test.Test
import kotlin.test.assertTrue

class BinarySearchTreeIteratorTest {
    @Test
    fun testReadonly() {
        val set = randomSet()
        var bst = TreapSet<Int>().let {
            it.addAll(set)
            it.root
        }

        val iter = BinarySearchTreeIterator({ bst }, { it.key })
        repeat(set.size / 2) {
            assertTrue(iter.hasNext())
            val next = iter.next()
            assertTrue(next in set)
        }
        repeat(set.size / 4) {
            assertTrue(iter.hasPrevious())
            val prev = iter.previous()
            assertTrue(prev in set)
        }
        while (iter.hasNext()) {
            val next = iter.next()
            assertTrue(next in set)
        }
        while (iter.hasPrevious()) {
            val prev = iter.previous()
            assertTrue(prev in set)
        }

        assertTrue(iter.asSequence().toList() == set.toList().sorted())
    }

    @Test
    fun testMutable() {
        val set = randomSet()
        var bst = TreapSet<Int>().let {
            it.addAll(set)
            it.root
        }

        val iter = MutableBinarySearchTreeIterator({ bst }, { bst = it }, { it.key })
        repeat(set.size / 2) {
            assertTrue(iter.hasNext())
            val next = iter.next()
            assertTrue(next in set)
        }
        repeat(set.size / 4) {
            assertTrue(iter.hasPrevious())
            val prev = iter.previous()
            assertTrue(prev in set)
        }
        while (iter.hasNext()) {
            val next = iter.next()
            assertTrue(next in set)
        }
        while (iter.hasPrevious()) {
            val prev = iter.previous()
            assertTrue(prev in set)
        }

        assertTrue(iter.asSequence().toList() == set.toList().sorted())

        val iter2 = MutableBinarySearchTreeIterator({ bst }, { bst = it }, { it.key })
        while (iter2.hasNext()) {
            val next = iter2.next()
            if (next % 2 == 0) {
                iter2.remove()
            }
        }
        assertTrue(bst?.inorderTraversal()?.toList()?.none { it.key % 2 == 0 } == true)

        while (iter2.hasPrevious()) {
            val prev = iter2.previous()
            if (prev % 3 == 0) {
                iter2.remove()
            }
        }
        assertTrue(bst?.inorderTraversal()?.toList()?.none { it.key % 3 == 0 } == true)
    }
}