package io.ssttkkl.algorithms.tree

interface BinaryTree<out N : BinaryTree<N>> {
    val left: N?

    val right: N?

    val parent: N?
        get() = throw UnsupportedOperationException("not parent-aware")

    val size: Int
        get() = throw UnsupportedOperationException("not size-aware")
}

val BinaryTree<*>?.size: Int
    get() = this?.size ?: 0

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

fun <N : BinaryTree<N>> N.toGraphviz(): String {
    return buildString {
        appendLine("digraph g1 {")
        appendLine("node [shape=circle];")
        inorderTraversal().forEach {
            left?.let { left ->
                appendLine("${it} -> ${left} [label = \"L\"];")
            }
            right?.let { right ->
                appendLine("${it} -> ${right} [label = \"R\"];")
            }
        }
        appendLine("}")
    }
}