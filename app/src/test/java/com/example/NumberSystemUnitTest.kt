package com.example

import com.example.math.NumberSystemDifficulty
import com.example.math.NumberSystemProvider
import com.example.math.NumberSystemTopic
import com.example.math.PrimeNumberProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Focused unit tests for Phase 3: Number System & Divisibility.
 * Covers:
 * - Divisibility rules for 2, 3, 4, 5, 6, 8, 9, 10, 11
 * - Prime & composite numbers classification (reusing PrimeNumberProvider)
 * - Factors & Multiples calculation
 * - GCD (Euclidean) & LCM algorithms with mathematical edge cases
 * - Curated practice questions bank consistency
 */
class NumberSystemUnitTest {

    @Test
    fun testDivisibilityBy2() {
        val (divEven, _) = NumberSystemProvider.checkDivisibility(4872L, 2)
        assertTrue(divEven)

        val (divOdd, _) = NumberSystemProvider.checkDivisibility(4873L, 2)
        assertFalse(divOdd)

        val (divZero, _) = NumberSystemProvider.checkDivisibility(0L, 2)
        assertTrue(divZero)
    }

    @Test
    fun testDivisibilityBy3() {
        // 7 + 5 + 2 + 1 = 15 (15 % 3 == 0)
        val (div1, _) = NumberSystemProvider.checkDivisibility(7521L, 3)
        assertTrue(div1)

        // 7 + 5 + 2 + 2 = 16 (16 % 3 != 0)
        val (div2, _) = NumberSystemProvider.checkDivisibility(7522L, 3)
        assertFalse(div2)
    }

    @Test
    fun testDivisibilityBy4() {
        // Last 2 digits: 24 (24 % 4 == 0)
        val (div1, _) = NumberSystemProvider.checkDivisibility(93124L, 4)
        assertTrue(div1)

        // Last 2 digits: 00
        val (div2, _) = NumberSystemProvider.checkDivisibility(1500L, 4)
        assertTrue(div2)

        // Last 2 digits: 25
        val (div3, _) = NumberSystemProvider.checkDivisibility(93125L, 4)
        assertFalse(div3)
    }

    @Test
    fun testDivisibilityBy5() {
        assertTrue(NumberSystemProvider.checkDivisibility(38475L, 5).first)
        assertTrue(NumberSystemProvider.checkDivisibility(38470L, 5).first)
        assertFalse(NumberSystemProvider.checkDivisibility(38473L, 5).first)
    }

    @Test
    fun testDivisibilityBy6() {
        // 5142 is even and sum = 12 (div by 3) => div by 6
        assertTrue(NumberSystemProvider.checkDivisibility(5142L, 6).first)
        // 5143 is odd => not div by 6
        assertFalse(NumberSystemProvider.checkDivisibility(5143L, 6).first)
        // 5144 is even but sum = 14 => not div by 3 => not div by 6
        assertFalse(NumberSystemProvider.checkDivisibility(5144L, 6).first)
    }

    @Test
    fun testDivisibilityBy8() {
        // 72152: 152 / 8 = 19
        assertTrue(NumberSystemProvider.checkDivisibility(72152L, 8).first)
        // 1000: last 3 digits 000
        assertTrue(NumberSystemProvider.checkDivisibility(1000L, 8).first)
        assertFalse(NumberSystemProvider.checkDivisibility(72155L, 8).first)
    }

    @Test
    fun testDivisibilityBy9() {
        // 8352: 8+3+5+2 = 18 => div by 9
        assertTrue(NumberSystemProvider.checkDivisibility(8352L, 9).first)
        // 8353: 8+3+5+3 = 19 => not div by 9
        assertFalse(NumberSystemProvider.checkDivisibility(8353L, 9).first)
    }

    @Test
    fun testDivisibilityBy10() {
        assertTrue(NumberSystemProvider.checkDivisibility(98740L, 10).first)
        assertFalse(NumberSystemProvider.checkDivisibility(98745L, 10).first)
    }

    @Test
    fun testDivisibilityBy11() {
        // 91828: odd positions (from right: 8, 8, 9 => 25), even positions (2, 1 => 3), diff = 22 => div by 11
        assertTrue(NumberSystemProvider.checkDivisibility(91828L, 11).first)
        // 121: (1+1) - 2 = 0 => div by 11
        assertTrue(NumberSystemProvider.checkDivisibility(121L, 11).first)
        // 1234: (4+2) - (3+1) = 6 - 4 = 2 => not div by 11
        assertFalse(NumberSystemProvider.checkDivisibility(1234L, 11).first)
    }

    @Test
    fun testGcdAndLcmCalculations() {
        assertEquals(6L, NumberSystemProvider.gcd(24L, 18L))
        assertEquals(72L, NumberSystemProvider.lcm(24L, 18L))

        // Coprimes
        assertEquals(1L, NumberSystemProvider.gcd(13L, 17L))
        assertEquals(221L, NumberSystemProvider.lcm(13L, 17L))

        // Mathematical invariant: a * b = gcd(a, b) * lcm(a, b)
        val a = 48L
        val b = 36L
        val gcd = NumberSystemProvider.gcd(a, b)
        val lcm = NumberSystemProvider.lcm(a, b)
        assertEquals(a * b, gcd * lcm)
    }

    @Test
    fun testFactorsComputation() {
        val factors24 = NumberSystemProvider.getFactors(24L)
        assertEquals(listOf(1L, 2L, 3L, 4L, 6L, 8L, 12L, 24L), factors24)

        val primeFactors24 = NumberSystemProvider.getPrimeFactors(24L)
        assertEquals(listOf(2L, 2L, 2L, 3L), primeFactors24)
    }

    @Test
    fun testPrimeFactorsBoundary() {
        // getPrimeFactors(1) returns an empty list because 1 has no prime factors
        assertEquals(emptyList<Long>(), NumberSystemProvider.getPrimeFactors(1L))

        // getPrimeFactors(0) does not return a misleading prime factorization
        assertEquals(emptyList<Long>(), NumberSystemProvider.getPrimeFactors(0L))

        // Negative inputs do not return a misleading positive prime factorization
        assertEquals(emptyList<Long>(), NumberSystemProvider.getPrimeFactors(-5L))
        assertEquals(emptyList<Long>(), NumberSystemProvider.getPrimeFactors(-12L))

        // getPrimeFactors(12) returns prime factors 2, 2, 3
        assertEquals(listOf(2L, 2L, 3L), NumberSystemProvider.getPrimeFactors(12L))

        // Single prime
        assertEquals(listOf(2L), NumberSystemProvider.getPrimeFactors(2L))
        assertEquals(listOf(13L), NumberSystemProvider.getPrimeFactors(13L))
    }

    @Test
    fun testGcdLcmWithBoundaryValues() {
        // Ensure calculateGcdLcm with 1L and 0L works and produces clean explanations
        val res1 = NumberSystemProvider.calculateGcdLcm(1L, 12L)
        assertEquals(1L, res1.gcd)
        assertEquals(12L, res1.lcm)
        assertTrue(res1.gcdSteps.isNotEmpty())
        assertTrue(res1.lcmSteps.isNotEmpty())
    }

    @Test
    fun testPrimeAndCompositeReuse() {
        // Verify reuse of PrimeNumberProvider
        assertTrue(PrimeNumberProvider.isPrime(2))
        assertTrue(PrimeNumberProvider.isPrime(3))
        assertTrue(PrimeNumberProvider.isPrime(97))
        assertFalse(PrimeNumberProvider.isPrime(1))
        assertFalse(PrimeNumberProvider.isPrime(4))
        assertFalse(PrimeNumberProvider.isPrime(100))

        // 1-100 has 25 primes
        assertEquals(25, PrimeNumberProvider.primes1To100.size)
        // 101-200 has 21 primes
        assertEquals(21, PrimeNumberProvider.primes101To200.size)
    }

    @Test
    fun testCuratedPracticeQuestions() {
        val easyQuestions = NumberSystemProvider.getPracticeQuestions(NumberSystemTopic.ALL, NumberSystemDifficulty.EASY)
        val mediumQuestions = NumberSystemProvider.getPracticeQuestions(NumberSystemTopic.ALL, NumberSystemDifficulty.MEDIUM)
        val hardQuestions = NumberSystemProvider.getPracticeQuestions(NumberSystemTopic.ALL, NumberSystemDifficulty.HARD)

        assertTrue(easyQuestions.isNotEmpty())
        assertTrue(mediumQuestions.isNotEmpty())
        assertTrue(hardQuestions.isNotEmpty())

        // Verify all questions have valid options and a valid correct index
        for (q in easyQuestions + mediumQuestions + hardQuestions) {
            assertTrue(q.optionsBn.size >= 2)
            assertTrue(q.correctIndex in q.optionsBn.indices)
            assertTrue(q.explanationBn.isNotBlank())
        }
    }
}
