package io.ssttkkl.algorithms.math

fun gcd(a: Int, b: Int): Int {
    if (a < 0 || b < 0) {
        throw IllegalArgumentException("a and b must be non-negative")
    }
    if (b == 0) return a
    return gcd(b, a % b)
}
