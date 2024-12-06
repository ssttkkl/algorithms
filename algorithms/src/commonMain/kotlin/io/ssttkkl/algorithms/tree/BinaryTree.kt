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

data class SimpleBinaryTree<V>(
    var value: V,
    override var left: SimpleBinaryTree<V>? = null,
    override var right: SimpleBinaryTree<V>? = null):BinaryTree<SimpleBinaryTree<V>>

val BinaryTree<*>?.size: Int
    get() = this?.size ?: 0

fun <N : BinaryTree<N>> N.preorderTraversal(): Sequence<N> = sequence {
    val stack = ArrayList<N>()
    stack.add(this@preorderTraversal)

    while (stack.isNotEmpty()) {
        val top = stack.removeLast()
        yield(top)
        top.right?.let { stack.add(it) }
        top.left?.let { stack.add(it) }
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

fun <N : BinaryTree<N>> N.postorderTraversal(): Sequence<N> = sequence {
    val stack1 = ArrayList<N>()
    val stack2 = ArrayList<N>()
    stack1.add(this@postorderTraversal)

    while (stack1.isNotEmpty()) {
        val node = stack1.removeLast()
        stack2.add(node)
        node.left?.let { stack1.add(it) }
        node.right?.let { stack1.add(it) }
    }

    while (stack2.isNotEmpty()) {
        yield(stack2.removeLast())
    }
}

fun <N : BinaryTree<N>> N.calcHeight(): Int {
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
