package io.ssttkkl.algorithms.utils

import java.lang.IllegalArgumentException

internal fun <E> MutableList<E>.swap(indexI: Int, indexJ: Int) {
    val temp = this[indexI]
    this[indexI] = this[indexJ]
    this[indexJ] = temp
}

internal fun <E> MutableList<in E>.copyFrom(list: List<E>, copyStart: Int, copyLen: Int, writeStart: Int) {
    if (copyStart + copyLen > list.size) {
        throw IllegalArgumentException("list.size (= ${list.size}) must be at least copyStart + copyLen (= ${copyStart + copyLen})")
    }
    if (writeStart + copyLen > this.size) {
        throw IllegalArgumentException("this.size (= ${this.size}) must be at least writeStart + copyLen (= ${writeStart + copyLen})")
    }
    for (i in 0 until copyLen) {
        this[writeStart + i] = list[copyStart + i]
    }
}