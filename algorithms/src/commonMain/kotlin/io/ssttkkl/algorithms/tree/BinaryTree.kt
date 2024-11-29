package io.ssttkkl.algorithms.tree

interface BinaryTree<out N : BinaryTree<N>> {
    val left: N?

    val right: N?

    val parent: N?
        get() = throw UnsupportedOperationException("not parent-aware")

    val size: Int
        get() = throw UnsupportedOperationException("not size-aware")

    val repr: String
        get() = toString()
}

val BinaryTree<*>?.size: Int
    get() = this?.size ?: 0

fun <N : BinaryTree<N>> N.preorderTraversal(): Sequence<N> = sequence {
    val stack = ArrayList<N>()
    stack.add(this@preorderTraversal)

    while (stack.isNotEmpty()) {
        val top = stack.removeLast()
        yield(top)
        top.left?.let { stack.add(it) }
        top.right?.let { stack.add(it) }
    }
}

fun <N : BinaryTree<N>> N.inorderTraversal(): Sequence<N> = sequence {
    val stack = ArrayList<N>()

    var cur: N? = this@inorderTraversal
    while (cur != null) {
        stack.add(cur)
        cur = cur.left
    }

    while (stack.isNotEmpty()) {
        val top = stack.removeLast()
        yield(top)

        cur = top.right
        while (cur != null) {
            stack.add(cur)
            cur = cur.left
        }
    }
}

fun <N: BinaryTree<N>> N.calcHeight(): Int {
    val stack = ArrayList<Pair<N, Int>>()
    stack.add(Pair(this, 0))

    var ans = 0
    while (stack.isNotEmpty()) {
        val (top, topHeight) = stack.removeLast()
        if (topHeight > ans) {
            ans = topHeight
        }
        top.left?.let { stack.add(Pair(it, topHeight + 1)) }
        top.right?.let { stack.add(Pair(it, topHeight + 1)) }
    }

    return ans
}

fun <N : BinaryTree<N>> N.toGraphviz(): String {
    return buildString {
        appendLine("digraph g1 {")
        appendLine("node [shape=circle];")
        inorderTraversal().forEach {
            appendLine("${it.repr};")
            it.left?.let { left ->
                appendLine("${it.repr} -> ${left.repr} [label = \"L\"];")
            }
            it.right?.let { right ->
                appendLine("${it.repr} -> ${right.repr} [label = \"R\"];")
            }
        }
        appendLine("}")
    }
}
