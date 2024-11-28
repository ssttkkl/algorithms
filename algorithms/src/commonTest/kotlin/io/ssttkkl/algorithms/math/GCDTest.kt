package io.ssttkkl.algorithms.math

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class GCDTest {
    @Test
    fun testGCD() {
        assertEquals(2, gcd(4, 6))
        assertEquals(1, gcd(3, 5))
        assertEquals(1, gcd(1, 1))
        assertEquals(1, gcd(1, 0))
        assertEquals(1, gcd(0, 1))
        assertEquals(0, gcd(0, 0))
        assertFails { gcd(-1, 1) }
        assertFails { gcd(1, -1) }
    }
}