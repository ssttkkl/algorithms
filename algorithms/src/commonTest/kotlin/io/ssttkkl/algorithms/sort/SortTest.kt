package io.ssttkkl.algorithms.sort

import kotlin.random.Random
import kotlin.test.Test

abstract class SortTest(val sortImpl: Sort) {
    @Test
    fun test() {
        repeat(5) {
            val size = Random.nextInt(1000, 2000)
            val list = List(size) { Random.nextInt() }.toMutableList()
            sortImpl.sort(list)

            for (i in 1 until size) {
                assert(list[i] >= list[i - 1])
            }
        }
    }
}

class InsertSortTest : SortTest(InsertSort)
class SelectSortTest : SortTest(SelectSort)
class ShellSortTest : SortTest(ShellSort())
class MergeSortTest : SortTest(MergeSort)
class QuickSortTest : SortTest(QuickSort)