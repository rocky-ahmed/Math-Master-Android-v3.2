package com.example

import com.example.math.MathProblemSolverEngine
import com.example.math.SolverMode
import com.example.math.SolverTopic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

/**
 * Focused unit tests for Phase 4: Math Problem Solver.
 * Covers all 13 supported topics, 3 learning modes, verification, and mathematical edge cases.
 */
class MathProblemSolverUnitTest {

    // -------------------------------------------------------------
    // 1. BASIC ARITHMETIC & BODMAS
    // -------------------------------------------------------------
    @Test
    fun testArithmeticBodmasCorrectness() {
        val res = MathProblemSolverEngine.solveArithmetic("(১৫ + ৫) × ৪ ÷ ২ - ৬", SolverMode.LEARNING)
        assertTrue(res.isSuccess)
        // (15 + 5) * 4 / 2 - 6 = 20 * 4 / 2 - 6 = 80 / 2 - 6 = 40 - 6 = 34
        assertTrue(res.finalAnswerBn.contains("৩৪"))
        assertTrue(res.steps.isNotEmpty())
        assertNotNull(res.verificationBn)
    }

    @Test
    fun testArithmeticParenthesesError() {
        val unbalanced = MathProblemSolverEngine.solveArithmetic("(১৫ + ৫ × ৪", SolverMode.QUICK)
        assertFalse(unbalanced.isSuccess)
        assertTrue(unbalanced.errorMessageBn?.contains("বন্ধনীর") == true)
    }

    @Test
    fun testArithmeticDivisionByZero() {
        val divZero = MathProblemSolverEngine.solveArithmetic("২৫ ÷ (৫ - ৫)", SolverMode.LEARNING)
        assertFalse(divZero.isSuccess)
        assertTrue(divZero.errorMessageBn?.contains("শূন্য") == true || divZero.errorMessageBn?.contains("০") == true)
    }

    // -------------------------------------------------------------
    // BUG 1 REGRESSION TESTS: Locale-Sensitive Decimal Formatting
    // -------------------------------------------------------------
    @Test
    fun testDecimalExpressionEvaluation() {
        // (3.2 + 1.1) * 2 = 8.6
        val res1 = MathProblemSolverEngine.solveArithmetic("(3.2 + 1.1) * 2", SolverMode.QUICK)
        assertTrue(res1.isSuccess)
        assertEquals("৮.৬", res1.finalAnswerBn)

        // Nested expressions containing decimal values: ((1.5 + 0.5) * 2) + 0.5 = 4.5
        val res2 = MathProblemSolverEngine.solveArithmetic("((1.5 + 0.5) * 2) + 0.5", SolverMode.LEARNING)
        assertTrue(res2.isSuccess)
        assertEquals("৪.৫", res2.finalAnswerBn)

        // Decimal values below 1, such as 0.5: (0.5 + 0.25) * 2 = 1.5
        val res3 = MathProblemSolverEngine.solveArithmetic("(0.5 + 0.25) * 2", SolverMode.QUICK)
        assertTrue(res3.isSuccess)
        assertEquals("১.৫", res3.finalAnswerBn)

        // Single decimal below 1: 0.5 * 2 = 1
        val res4 = MathProblemSolverEngine.solveArithmetic("0.5 * 2", SolverMode.QUICK)
        assertTrue(res4.isSuccess)
        assertEquals("১", res4.finalAnswerBn)

        // Expression resulting in decimal below 1: 1 / 2 = 0.5
        val res5 = MathProblemSolverEngine.solveArithmetic("1 / 2", SolverMode.QUICK)
        assertTrue(res5.isSuccess)
        assertEquals("০.৫", res5.finalAnswerBn)
    }

    @Test
    fun testDecimalEvaluationUnderDifferentLocales() {
        val originalLocale = Locale.getDefault()
        try {
            // Test under Locale.FRANCE where comma is the standard decimal separator
            Locale.setDefault(Locale.FRANCE)
            val resFrance = MathProblemSolverEngine.solveArithmetic("(3.2 + 1.1) * 2", SolverMode.LEARNING)
            assertTrue(resFrance.isSuccess)
            assertEquals("৮.৬", resFrance.finalAnswerBn)

            val resFranceNested = MathProblemSolverEngine.solveArithmetic("((1.5 + 0.5) * 2) + 0.5", SolverMode.QUICK)
            assertTrue(resFranceNested.isSuccess)
            assertEquals("৪.৫", resFranceNested.finalAnswerBn)

            val resFranceSubOne = MathProblemSolverEngine.solveArithmetic("1 / 2", SolverMode.QUICK)
            assertTrue(resFranceSubOne.isSuccess)
            assertEquals("০.৫", resFranceSubOne.finalAnswerBn)

            // Test under Locale.US
            Locale.setDefault(Locale.US)
            val resUS = MathProblemSolverEngine.solveArithmetic("(3.2 + 1.1) * 2", SolverMode.LEARNING)
            assertTrue(resUS.isSuccess)
            assertEquals("৮.৬", resUS.finalAnswerBn)

            val resUSSubOne = MathProblemSolverEngine.solveArithmetic("1 / 2", SolverMode.QUICK)
            assertTrue(resUSSubOne.isSuccess)
            assertEquals("০.৫", resUSSubOne.finalAnswerBn)
        } finally {
            // Always restore the original locale
            Locale.setDefault(originalLocale)
        }
    }

    // -------------------------------------------------------------
    // BUG 2 REGRESSION TESTS: Unary Positive Operator
    // -------------------------------------------------------------
    @Test
    fun testUnaryPositiveOperator() {
        // 8 * +2 = 16
        val res1 = MathProblemSolverEngine.solveArithmetic("8 * +2", SolverMode.QUICK)
        assertTrue(res1.isSuccess)
        assertEquals("১৬", res1.finalAnswerBn)

        // 12 / +3 = 4
        val res2 = MathProblemSolverEngine.solveArithmetic("12 / +3", SolverMode.QUICK)
        assertTrue(res2.isSuccess)
        assertEquals("৪", res2.finalAnswerBn)

        // 5 + +2 = 7
        val res3 = MathProblemSolverEngine.solveArithmetic("5 + +2", SolverMode.QUICK)
        assertTrue(res3.isSuccess)
        assertEquals("৭", res3.finalAnswerBn)

        // Unary positive at the start
        val res4 = MathProblemSolverEngine.solveArithmetic("+5 + 2", SolverMode.QUICK)
        assertTrue(res4.isSuccess)
        assertEquals("৭", res4.finalAnswerBn)

        // Combined unary positive and negative: 5 + -2 = 3, 5 - +2 = 3
        val res5 = MathProblemSolverEngine.solveArithmetic("5 + -2", SolverMode.QUICK)
        assertTrue(res5.isSuccess)
        assertEquals("৩", res5.finalAnswerBn)

        val res6 = MathProblemSolverEngine.solveArithmetic("5 - +2", SolverMode.QUICK)
        assertTrue(res6.isSuccess)
        assertEquals("৩", res6.finalAnswerBn)

        // BODMAS with unary signs: 8 * +2 - 12 / +3 = 16 - 4 = 12
        val resBodmas = MathProblemSolverEngine.solveArithmetic("8 * +2 - 12 / +3", SolverMode.LEARNING)
        assertTrue(resBodmas.isSuccess)
        assertEquals("১২", resBodmas.finalAnswerBn)
    }

    @Test
    fun testInvalidArithmeticExpressionsReturnControlledError() {
        // Invalid expressions must return isSuccess = false with controlled error message, not crash
        val invalid1 = MathProblemSolverEngine.solveArithmetic("8 * +", SolverMode.QUICK)
        assertFalse(invalid1.isSuccess)
        assertNotNull(invalid1.errorMessageBn)

        val invalid2 = MathProblemSolverEngine.solveArithmetic("8 * ++2", SolverMode.QUICK)
        assertFalse(invalid2.isSuccess)
        assertNotNull(invalid2.errorMessageBn)

        val invalid3 = MathProblemSolverEngine.solveArithmetic("8 * / 2", SolverMode.QUICK)
        assertFalse(invalid3.isSuccess)
        assertNotNull(invalid3.errorMessageBn)

        val invalid4 = MathProblemSolverEngine.solveArithmetic("+", SolverMode.QUICK)
        assertFalse(invalid4.isSuccess)
        assertNotNull(invalid4.errorMessageBn)

        val invalid5 = MathProblemSolverEngine.solveArithmetic("8 *", SolverMode.QUICK)
        assertFalse(invalid5.isSuccess)
        assertNotNull(invalid5.errorMessageBn)

        val invalid6 = MathProblemSolverEngine.solveArithmetic("* 8", SolverMode.QUICK)
        assertFalse(invalid6.isSuccess)
        assertNotNull(invalid6.errorMessageBn)
    }

    // -------------------------------------------------------------
    // 2. FRACTIONS & DECIMALS
    // -------------------------------------------------------------
    @Test
    fun testFractionAdditionAndSimplification() {
        // 3/4 + 2/5 = (15 + 8)/20 = 23/20 = 1 3/20
        val res = MathProblemSolverEngine.solveFraction(3L, 4L, "+", 2L, 5L, SolverMode.LEARNING)
        assertTrue(res.isSuccess)
        assertTrue(res.finalAnswerBn.contains("২৩/২০"))
        // Check LCM step
        val lcmStep = res.steps.find { it.titleBn.contains("ল.সা.গু") }
        assertNotNull(lcmStep)
        assertTrue(lcmStep?.expressionOrMathBn?.contains("২০") == true)
    }

    @Test
    fun testFractionReductionByGcd() {
        // 3/4 - 1/4 = 2/4 = 1/2
        val res = MathProblemSolverEngine.solveFraction(3L, 4L, "-", 1L, 4L, SolverMode.QUICK)
        assertTrue(res.isSuccess)
        assertTrue(res.finalAnswerBn.contains("১/২"))
    }

    @Test
    fun testFractionMultiplicationAndDivision() {
        // 2/3 * 3/4 = 6/12 = 1/2
        val mul = MathProblemSolverEngine.solveFraction(2L, 3L, "*", 3L, 4L, SolverMode.LEARNING)
        assertTrue(mul.isSuccess)
        assertTrue(mul.finalAnswerBn.contains("১/২"))

        // 3/4 / 2/5 = 3/4 * 5/2 = 15/8
        val div = MathProblemSolverEngine.solveFraction(3L, 4L, "/", 2L, 5L, SolverMode.BCS)
        assertTrue(div.isSuccess)
        assertTrue(div.finalAnswerBn.contains("১৫/৮"))
        assertNotNull(div.bcsShortcutBn)
    }

    @Test
    fun testFractionZeroDenominator() {
        val zeroDen = MathProblemSolverEngine.solveFraction(3L, 0L, "+", 2L, 5L, SolverMode.LEARNING)
        assertFalse(zeroDen.isSuccess)
    }

    // -------------------------------------------------------------
    // 3. PERCENTAGE
    // -------------------------------------------------------------
    @Test
    fun testPercentageCalculations() {
        // 15% of 500 = 75
        val p1 = MathProblemSolverEngine.solvePercentage(
            MathProblemSolverEngine.PercentageType.OF_NUMBER,
            15.0,
            500.0,
            SolverMode.LEARNING
        )
        assertTrue(p1.isSuccess)
        assertTrue(p1.finalAnswerBn.contains("৭৫"))

        // 45 is what % of 300 = 15%
        val p2 = MathProblemSolverEngine.solvePercentage(
            MathProblemSolverEngine.PercentageType.WHAT_PERCENT_OF,
            45.0,
            300.0,
            SolverMode.QUICK
        )
        assertTrue(p2.isSuccess)
        assertTrue(p2.finalAnswerBn.contains("১৫%"))

        // 80 to 100 = 25% increase
        val p3 = MathProblemSolverEngine.solvePercentage(
            MathProblemSolverEngine.PercentageType.INCREASE_DECREASE,
            80.0,
            100.0,
            SolverMode.LEARNING
        )
        assertTrue(p3.isSuccess)
        assertTrue(p3.finalAnswerBn.contains("২৫% বৃদ্ধি"))

        // Successive: 10% and 20% -> 10 + 20 + (10*20)/100 = 32%
        val p4 = MathProblemSolverEngine.solvePercentage(
            MathProblemSolverEngine.PercentageType.SUCCESSIVE,
            10.0,
            20.0,
            SolverMode.BCS
        )
        assertTrue(p4.isSuccess)
        assertTrue(p4.finalAnswerBn.contains("৩২%"))
    }

    // -------------------------------------------------------------
    // 4. RATIO & PROPORTION
    // -------------------------------------------------------------
    @Test
    fun testRatioDivisionAndProportion() {
        // 1200 in 3:5 -> 1200 * 3/8 = 450, 1200 * 5/8 = 750
        val r1 = MathProblemSolverEngine.solveRatio(
            MathProblemSolverEngine.RatioType.DIVIDE_QUANTITY,
            1200.0,
            3.0,
            5.0,
            null,
            SolverMode.LEARNING
        )
        assertTrue(r1.isSuccess)
        assertTrue(r1.finalAnswerBn.contains("৪৫০") && r1.finalAnswerBn.contains("৭৫০"))

        // 4 : 8 = 12 : X -> X = (8 * 12) / 4 = 24
        val r2 = MathProblemSolverEngine.solveRatio(
            MathProblemSolverEngine.RatioType.FOURTH_PROPORTIONAL,
            12.0,
            4.0,
            8.0,
            null,
            SolverMode.QUICK
        )
        assertTrue(r2.isSuccess)
        assertTrue(r2.finalAnswerBn.contains("২৪"))

        // 24 : 36 -> 2 : 3
        val r3 = MathProblemSolverEngine.solveRatio(
            MathProblemSolverEngine.RatioType.SIMPLIFY,
            0.0,
            24.0,
            36.0,
            null,
            SolverMode.LEARNING
        )
        assertTrue(r3.isSuccess)
        assertTrue(r3.finalAnswerBn.contains("২ : ৩"))
    }

    // -------------------------------------------------------------
    // 5. PROFIT AND LOSS
    // -------------------------------------------------------------
    @Test
    fun testProfitAndLossCalculations() {
        // CP 500, SP 550 -> 50 profit (10%)
        val pl1 = MathProblemSolverEngine.solveProfitLoss(
            MathProblemSolverEngine.ProfitLossType.FIND_PROFIT_LOSS_PERCENT,
            500.0,
            550.0,
            null,
            null,
            SolverMode.LEARNING
        )
        assertTrue(pl1.isSuccess)
        assertTrue(pl1.finalAnswerBn.contains("৫০") && pl1.finalAnswerBn.contains("১০%"))

        // CP 500, SP 400 -> 100 loss (20%)
        val pl2 = MathProblemSolverEngine.solveProfitLoss(
            MathProblemSolverEngine.ProfitLossType.FIND_PROFIT_LOSS_PERCENT,
            500.0,
            400.0,
            null,
            null,
            SolverMode.QUICK
        )
        assertTrue(pl2.isSuccess)
        assertTrue(pl2.finalAnswerBn.contains("১০০") && pl2.finalAnswerBn.contains("২০%"))

        // Find SP: CP 800, 15% profit -> 800 * 1.15 = 920
        val pl3 = MathProblemSolverEngine.solveProfitLoss(
            MathProblemSolverEngine.ProfitLossType.FIND_SP_FROM_CP_AND_PERCENT,
            800.0,
            null,
            15.0,
            null,
            SolverMode.BCS
        )
        assertTrue(pl3.isSuccess)
        assertTrue(pl3.finalAnswerBn.contains("৯২০"))

        // Find CP: SP 550, 10% profit -> CP = 550 * 100 / 110 = 500
        val pl4 = MathProblemSolverEngine.solveProfitLoss(
            MathProblemSolverEngine.ProfitLossType.FIND_CP_FROM_SP_AND_PERCENT,
            null,
            550.0,
            10.0,
            null,
            SolverMode.LEARNING
        )
        assertTrue(pl4.isSuccess)
        assertTrue(pl4.finalAnswerBn.contains("৫০০"))

        // Marked price 1000, 15% discount -> SP = 850
        val pl5 = MathProblemSolverEngine.solveProfitLoss(
            MathProblemSolverEngine.ProfitLossType.DISCOUNT_MARKED_PRICE,
            null,
            null,
            15.0,
            1000.0,
            SolverMode.LEARNING
        )
        assertTrue(pl5.isSuccess)
        assertTrue(pl5.finalAnswerBn.contains("৮৫০"))
    }

    // -------------------------------------------------------------
    // 6. AVERAGE
    // -------------------------------------------------------------
    @Test
    fun testAverageCalculation() {
        val avg = MathProblemSolverEngine.solveAverage(listOf(10.0, 20.0, 30.0, 40.0, 50.0), SolverMode.LEARNING)
        assertTrue(avg.isSuccess)
        // Sum = 150, Count = 5, Avg = 30
        assertTrue(avg.finalAnswerBn.contains("৩০"))
        assertNotNull(avg.verificationBn)

        // Empty list
        val emptyAvg = MathProblemSolverEngine.solveAverage(emptyList(), SolverMode.QUICK)
        assertFalse(emptyAvg.isSuccess)
    }

    // -------------------------------------------------------------
    // 7. SIMPLE INTEREST
    // -------------------------------------------------------------
    @Test
    fun testSimpleInterestAllCases() {
        // P 5000, r 10%, t 3 -> I = 1500, A = 6500
        val si1 = MathProblemSolverEngine.solveSimpleInterest(5000.0, 10.0, 3.0, null, null, SolverMode.LEARNING)
        assertTrue(si1.isSuccess)
        assertTrue(si1.finalAnswerBn.contains("১৫০০") && si1.finalAnswerBn.contains("৬৫০০"))

        // Solve for P: I 1500, r 10, t 3 -> P = 5000
        val si2 = MathProblemSolverEngine.solveSimpleInterest(null, 10.0, 3.0, 1500.0, null, SolverMode.QUICK)
        assertTrue(si2.isSuccess)
        assertTrue(si2.finalAnswerBn.contains("৫০০০"))

        // Solve for r: P 5000, I 1500, t 3 -> r = 10%
        val si3 = MathProblemSolverEngine.solveSimpleInterest(5000.0, null, 3.0, 1500.0, null, SolverMode.LEARNING)
        assertTrue(si3.isSuccess)
        assertTrue(si3.finalAnswerBn.contains("১০%"))

        // Solve for t: P 5000, I 1500, r 10 -> t = 3
        val si4 = MathProblemSolverEngine.solveSimpleInterest(5000.0, 10.0, null, 1500.0, null, SolverMode.BCS)
        assertTrue(si4.isSuccess)
        assertTrue(si4.finalAnswerBn.contains("৩"))
    }

    // -------------------------------------------------------------
    // 8. TIME AND WORK
    // -------------------------------------------------------------
    @Test
    fun testTimeAndWork() {
        // A in 10, B in 15 -> (10 * 15) / (10 + 15) = 150 / 25 = 6 days
        val w1 = MathProblemSolverEngine.solveTimeAndWork(
            MathProblemSolverEngine.WorkType.TWO_PERSONS,
            10.0,
            15.0,
            null,
            SolverMode.LEARNING
        )
        assertTrue(w1.isSuccess)
        assertTrue(w1.finalAnswerBn.contains("৬"))
        assertNotNull(w1.verificationBn)

        // Three persons: A 10, B 12, C 15 -> 1 / (1/10 + 1/12 + 1/15) = 1 / (6+5+4 / 60) = 60/15 = 4 days
        val w2 = MathProblemSolverEngine.solveTimeAndWork(
            MathProblemSolverEngine.WorkType.THREE_PERSONS,
            10.0,
            12.0,
            15.0,
            SolverMode.QUICK
        )
        assertTrue(w2.isSuccess)
        assertTrue(w2.finalAnswerBn.contains("৪"))

        // Pipe Cistern: Fill in 10, Empty in 15 -> (10 * 15) / (15 - 10) = 150 / 5 = 30 hrs
        val w3 = MathProblemSolverEngine.solveTimeAndWork(
            MathProblemSolverEngine.WorkType.PIPE_CISTERN,
            10.0,
            15.0,
            null,
            SolverMode.LEARNING
        )
        assertTrue(w3.isSuccess)
        assertTrue(w3.finalAnswerBn.contains("৩০"))

        // Pipe Cistern invalid: Empty in 10, Fill in 15 -> cistern will never fill
        val w4 = MathProblemSolverEngine.solveTimeAndWork(
            MathProblemSolverEngine.WorkType.PIPE_CISTERN,
            15.0,
            10.0,
            null,
            SolverMode.LEARNING
        )
        assertFalse(w4.isSuccess)
    }

    // -------------------------------------------------------------
    // 9. SPEED, DISTANCE, AND TIME
    // -------------------------------------------------------------
    @Test
    fun testSpeedDistanceTime() {
        // Speed: D 120 km, T 2 hrs -> S = 60 km/h = 16.67 m/s
        val s1 = MathProblemSolverEngine.solveSpeedDistanceTime(null, 120.0, 2.0, SolverMode.LEARNING)
        assertTrue(s1.isSuccess)
        assertTrue(s1.finalAnswerBn.contains("৬০ km/h"))

        // Distance: S 60, T 2.5 -> D = 150 km
        val s2 = MathProblemSolverEngine.solveSpeedDistanceTime(60.0, null, 2.5, SolverMode.QUICK)
        assertTrue(s2.isSuccess)
        assertTrue(s2.finalAnswerBn.contains("১৫০ km"))

        // Time: D 150, S 50 -> T = 3 hrs
        val s3 = MathProblemSolverEngine.solveSpeedDistanceTime(50.0, 150.0, null, SolverMode.BCS)
        assertTrue(s3.isSuccess)
        assertTrue(s3.finalAnswerBn.contains("৩ ঘন্টা"))
    }

    // -------------------------------------------------------------
    // 10. NUMBER SYSTEM & DIVISIBILITY
    // -------------------------------------------------------------
    @Test
    fun testDivisibilityRules() {
        // Divisible by 3: 7521
        val d3 = MathProblemSolverEngine.solveDivisibility(7521L, 3, SolverMode.LEARNING)
        assertTrue(d3.isSuccess)
        assertTrue(d3.finalAnswerBn.contains("নিঃশেষে বিভাজ্য"))

        // Not divisible by 3: 7522
        val d3not = MathProblemSolverEngine.solveDivisibility(7522L, 3, SolverMode.LEARNING)
        assertTrue(d3not.isSuccess)
        assertTrue(d3not.finalAnswerBn.contains("বিভাজ্য নয়"))

        // Divisible by 11: 91828
        val d11 = MathProblemSolverEngine.solveDivisibility(91828L, 11, SolverMode.BCS)
        assertTrue(d11.isSuccess)
        assertTrue(d11.finalAnswerBn.contains("নিঃশেষে বিভাজ্য"))
    }

    // -------------------------------------------------------------
    // 11. HCF AND LCM
    // -------------------------------------------------------------
    @Test
    fun testHcfLcm() {
        // 24 and 36 -> HCF 12, LCM 72
        val hl = MathProblemSolverEngine.solveHcfLcm(listOf(24L, 36L), SolverMode.LEARNING)
        assertTrue(hl.isSuccess)
        assertTrue(hl.finalAnswerBn.contains("১২") && hl.finalAnswerBn.contains("৭২"))
        // Check verification invariant
        assertNotNull(hl.verificationBn)
    }

    // -------------------------------------------------------------
    // 12. SQUARES, CUBES, AND ROOTS
    // -------------------------------------------------------------
    @Test
    fun testSquareCubeRoots() {
        // Square of 12 -> 144
        val sq = MathProblemSolverEngine.solveSquareCube(12.0, MathProblemSolverEngine.SquareCubeOp.SQUARE, SolverMode.LEARNING)
        assertTrue(sq.isSuccess)
        assertTrue(sq.finalAnswerBn.contains("১৪৪"))

        // Sqrt of 144 -> 12
        val sqrt = MathProblemSolverEngine.solveSquareCube(144.0, MathProblemSolverEngine.SquareCubeOp.SQUARE_ROOT, SolverMode.LEARNING)
        assertTrue(sqrt.isSuccess)
        assertTrue(sqrt.finalAnswerBn.contains("১২"))

        // Negative sqrt error
        val negSqrt = MathProblemSolverEngine.solveSquareCube(-25.0, MathProblemSolverEngine.SquareCubeOp.SQUARE_ROOT, SolverMode.LEARNING)
        assertFalse(negSqrt.isSuccess)

        // Cube of 5 -> 125
        val cb = MathProblemSolverEngine.solveSquareCube(5.0, MathProblemSolverEngine.SquareCubeOp.CUBE, SolverMode.LEARNING)
        assertTrue(cb.isSuccess)
        assertTrue(cb.finalAnswerBn.contains("১২৫"))

        // Cube root of 125 -> 5
        val cbrt = MathProblemSolverEngine.solveSquareCube(125.0, MathProblemSolverEngine.SquareCubeOp.CUBE_ROOT, SolverMode.LEARNING)
        assertTrue(cbrt.isSuccess)
        assertTrue(cbrt.finalAnswerBn.contains("৫"))
    }

    // -------------------------------------------------------------
    // 13. LINEAR EQUATIONS
    // -------------------------------------------------------------
    @Test
    fun testLinearEquations() {
        // 3x + 12 = 45 -> 3x = 33 -> x = 11
        val eq1 = MathProblemSolverEngine.solveLinearEquation("3x + 12 = 45", SolverMode.LEARNING)
        assertTrue(eq1.isSuccess)
        assertTrue(eq1.finalAnswerBn.contains("১১"))
        assertNotNull(eq1.verificationBn)

        // 5x - 8 = 2x + 16 -> 3x = 24 -> x = 8
        val eq2 = MathProblemSolverEngine.solveLinearEquation("5x - 8 = 2x + 16", SolverMode.BCS)
        assertTrue(eq2.isSuccess)
        assertTrue(eq2.finalAnswerBn.contains("৮"))
        assertNotNull(eq2.bcsShortcutBn)

        // Invalid equation without '='
        val noEq = MathProblemSolverEngine.solveLinearEquation("3x + 12", SolverMode.QUICK)
        assertFalse(noEq.isSuccess)
    }

    // -------------------------------------------------------------
    // BATCH 3 REGRESSION TESTS: COMPOUND LINEAR EQUATIONS
    // -------------------------------------------------------------
    @Test
    fun testCompoundLinearEquations() {
        // 2(x + 3) = 14 -> 2x + 6 = 14 -> 2x = 8 -> x = 4
        val res1 = MathProblemSolverEngine.solveLinearEquation("2(x + 3) = 14", SolverMode.LEARNING)
        assertTrue(res1.isSuccess)
        assertEquals("x = ৪", res1.finalAnswerBn)
        assertNotNull(res1.verificationBn)
        // Check expansion step exists
        val expandStep1 = res1.steps.find { it.titleBn.contains("বন্ধনীর গুণফল সম্প্রসারণ") }
        assertNotNull(expandStep1)
        assertTrue(expandStep1?.expressionOrMathBn?.contains("২x + ৬ = ১৪") == true)

        // 3(x - 2) = 12 -> 3x - 6 = 12 -> 3x = 18 -> x = 6
        val res2 = MathProblemSolverEngine.solveLinearEquation("3(x - 2) = 12", SolverMode.QUICK)
        assertTrue(res2.isSuccess)
        assertEquals("x = ৬", res2.finalAnswerBn)

        // 2(x + 3) = 2x + 6 -> 2x + 6 = 2x + 6 -> 0 = 0 (Infinitely many solutions / Identity)
        val resIdentity = MathProblemSolverEngine.solveLinearEquation("2(x + 3) = 2x + 6", SolverMode.LEARNING)
        assertTrue(resIdentity.isSuccess)
        assertTrue(resIdentity.finalAnswerBn.contains("অসংখ্য সমাধান"))
        assertTrue(resIdentity.finalAnswerBn.contains("অভেদ"))

        // Negative coefficients
        // -2(x + 3) = -14 -> -2x - 6 = -14 -> -2x = -8 -> x = 4
        val resNeg1 = MathProblemSolverEngine.solveLinearEquation("-2(x + 3) = -14", SolverMode.QUICK)
        assertTrue(resNeg1.isSuccess)
        assertEquals("x = ৪", resNeg1.finalAnswerBn)

        // -3(x - 2) = 12 -> -3x + 6 = 12 -> -3x = 6 -> x = -2
        val resNeg2 = MathProblemSolverEngine.solveLinearEquation("-3(x - 2) = 12", SolverMode.QUICK)
        assertTrue(resNeg2.isSuccess)
        assertEquals("x = -২", resNeg2.finalAnswerBn)

        // 3(2 - x) = 12 -> 6 - 3x = 12 -> -3x = 6 -> x = -2
        val resNeg3 = MathProblemSolverEngine.solveLinearEquation("3(2 - x) = 12", SolverMode.QUICK)
        assertTrue(resNeg3.isSuccess)
        assertEquals("x = -২", resNeg3.finalAnswerBn)

        // -(x + 3) = 5 -> -x - 3 = 5 -> -x = 8 -> x = -8
        val resNeg4 = MathProblemSolverEngine.solveLinearEquation("-(x + 3) = 5", SolverMode.QUICK)
        assertTrue(resNeg4.isSuccess)
        assertEquals("x = -৮", resNeg4.finalAnswerBn)

        // Expressions with spaces
        val resSpaces1 = MathProblemSolverEngine.solveLinearEquation("  2 ( x + 3 )  =  14  ", SolverMode.QUICK)
        assertTrue(resSpaces1.isSuccess)
        assertEquals("x = ৪", resSpaces1.finalAnswerBn)

        val resSpaces2 = MathProblemSolverEngine.solveLinearEquation(" 3 ( x - 2 ) = 12 ", SolverMode.QUICK)
        assertTrue(resSpaces2.isSuccess)
        assertEquals("x = ৬", resSpaces2.finalAnswerBn)

        val resSpaces3 = MathProblemSolverEngine.solveLinearEquation(" - 3 ( x - 2 ) = 12 ", SolverMode.QUICK)
        assertTrue(resSpaces3.isSuccess)
        assertEquals("x = -২", resSpaces3.finalAnswerBn)

        // Multiplication with '*'
        val resMult = MathProblemSolverEngine.solveLinearEquation("2*(x + 3) = 14", SolverMode.QUICK)
        assertTrue(resMult.isSuccess)
        assertEquals("x = ৪", resMult.finalAnswerBn)

        // Bengali numerals in equation
        val resBn = MathProblemSolverEngine.solveLinearEquation("২(x + ৩) = ১৪", SolverMode.QUICK)
        assertTrue(resBn.isSuccess)
        assertEquals("x = ৪", resBn.finalAnswerBn)
    }

    @Test
    fun testCompoundLinearEquationMalformedAndUnsupported() {
        // Malformed parentheses: missing closing
        val unclosed = MathProblemSolverEngine.solveLinearEquation("2(x + 3 = 14", SolverMode.QUICK)
        assertFalse(unclosed.isSuccess)
        assertTrue(unclosed.errorMessageBn?.contains("বন্ধনীর") == true)

        // Malformed parentheses: extra closing
        val extraClose = MathProblemSolverEngine.solveLinearEquation("2(x + 3)) = 14", SolverMode.QUICK)
        assertFalse(extraClose.isSuccess)
        assertTrue(extraClose.errorMessageBn?.contains("বন্ধনীর") == true)

        // Empty parentheses
        val emptyParen = MathProblemSolverEngine.solveLinearEquation("2() = 14", SolverMode.QUICK)
        assertFalse(emptyParen.isSuccess)
        assertTrue(emptyParen.errorMessageBn?.contains("বন্ধনীর") == true)

        // Nested parentheses
        val nested = MathProblemSolverEngine.solveLinearEquation("2((x + 3)) = 14", SolverMode.QUICK)
        assertFalse(nested.isSuccess)
        assertTrue(nested.errorMessageBn?.contains("নেস্টেড বন্ধনী") == true)

        // Non-linear expression: quadratic
        val quadratic = MathProblemSolverEngine.solveLinearEquation("x^2 + 4 = 20", SolverMode.QUICK)
        assertFalse(quadratic.isSuccess)
        assertTrue(quadratic.errorMessageBn?.contains("অরৈখিক") == true)

        // Variable multiplied by variable expression
        val nonlinearVar = MathProblemSolverEngine.solveLinearEquation("x(x + 3) = 14", SolverMode.QUICK)
        assertFalse(nonlinearVar.isSuccess)
        assertTrue(nonlinearVar.errorMessageBn?.contains("অরৈখিক") == true)

        // Multiple/other variables
        val multiVar = MathProblemSolverEngine.solveLinearEquation("2x + 3y = 12", SolverMode.QUICK)
        assertFalse(multiVar.isSuccess)
        assertTrue(multiVar.errorMessageBn?.contains("চলক") == true)

        // Impossible equation: 2(x + 3) = 2x + 10 -> 0 = 4
        val impossible = MathProblemSolverEngine.solveLinearEquation("2(x + 3) = 2x + 10", SolverMode.QUICK)
        assertFalse(impossible.isSuccess)
        assertTrue(impossible.errorMessageBn?.contains("কোনো বাস্তব সমাধান নেই") == true)
    }

    // -------------------------------------------------------------
    // BATCH 3 REGRESSION TESTS: SPEED & DISTANCE TIME RESULTS
    // -------------------------------------------------------------
    @Test
    fun testSpeedDistanceTimeDurationFormatting() {
        // 120 km at 60 km/h -> 2 hours
        val t1 = MathProblemSolverEngine.solveSpeedDistanceTime(60.0, 120.0, null, SolverMode.LEARNING)
        assertTrue(t1.isSuccess)
        assertEquals("২ ঘন্টা", t1.finalAnswerBn)

        // 90 km at 60 km/h -> 1 hour 30 minutes
        val t2 = MathProblemSolverEngine.solveSpeedDistanceTime(60.0, 90.0, null, SolverMode.LEARNING)
        assertTrue(t2.isSuccess)
        assertEquals("১.৫ ঘন্টা (১ ঘন্টা ৩০ মিনিট)", t2.finalAnswerBn)
        assertTrue(t2.finalAnswerBn.contains("১ ঘন্টা ৩০ মিনিট"))
        assertTrue(t2.finalAnswerBn.contains("১.৫ ঘন্টা"))
        val convertStep2 = t2.steps.find { it.titleBn.contains("ঘন্টাকে ঘন্টা ও মিনিটে রূপান্তর") }
        assertNotNull(convertStep2)

        // 100 km at 60 km/h -> 1 hour 40 minutes
        val t3 = MathProblemSolverEngine.solveSpeedDistanceTime(60.0, 100.0, null, SolverMode.LEARNING)
        assertTrue(t3.isSuccess)
        assertEquals("১.৬৬৬৭ ঘন্টা (১ ঘন্টা ৪০ মিনিট)", t3.finalAnswerBn)
        assertTrue(t3.finalAnswerBn.contains("১ ঘন্টা ৪০ মিনিট"))
        assertTrue(t3.finalAnswerBn.contains("১.৬৬৬৭ ঘন্টা"))

        // Case producing non-integer number of minutes with clearly defined rounding policy:
        // 50 km at 80 km/h = 0.625 hrs = 37.5 mins = 37 mins 30 secs (approx 38 mins)
        val t4 = MathProblemSolverEngine.solveSpeedDistanceTime(80.0, 50.0, null, SolverMode.LEARNING)
        assertTrue(t4.isSuccess)
        assertTrue(t4.finalAnswerBn.contains("০.৬২৫ ঘন্টা"))
        assertTrue(t4.finalAnswerBn.contains("৩৭ মিনিট ৩০ সেকেন্ড"))
        assertTrue(t4.finalAnswerBn.contains("৩৮ মিনিট"))

        // 100 km at 70 km/h = 1.42857 hrs = 1 hr 25 mins 43 secs (approx 1 hr 26 mins)
        val t5 = MathProblemSolverEngine.solveSpeedDistanceTime(70.0, 100.0, null, SolverMode.LEARNING)
        assertTrue(t5.isSuccess)
        assertTrue(t5.finalAnswerBn.contains("১.৪২৮৬ ঘন্টা"))
        assertTrue(t5.finalAnswerBn.contains("১ ঘন্টা ২৫ মিনিট ৪৩ সেকেন্ড"))
        assertTrue(t5.finalAnswerBn.contains("১ ঘন্টা ২৬ মিনিট"))

        // Under 1 hour with exact minutes: 30 km at 60 km/h = 0.5 hrs = 30 minutes
        val t6 = MathProblemSolverEngine.solveSpeedDistanceTime(60.0, 30.0, null, SolverMode.QUICK)
        assertTrue(t6.isSuccess)
        assertEquals("০.৫ ঘন্টা (৩০ মিনিট)", t6.finalAnswerBn)
    }

    // -------------------------------------------------------------
    // LEARNING MODES & SIMILAR PROBLEM GENERATOR
    // -------------------------------------------------------------
    @Test
    fun testLearningModesDifferences() {
        val learning = MathProblemSolverEngine.solvePercentage(
            MathProblemSolverEngine.PercentageType.OF_NUMBER,
            20.0,
            100.0,
            SolverMode.LEARNING
        )
        assertNotNull(learning.verificationBn)

        val bcs = MathProblemSolverEngine.solvePercentage(
            MathProblemSolverEngine.PercentageType.OF_NUMBER,
            20.0,
            100.0,
            SolverMode.BCS
        )
        assertNotNull(bcs.bcsShortcutBn)
    }

    @Test
    fun testSimilarProblemGeneratorAllTopics() {
        for (topic in SolverTopic.entries) {
            val inputs = MathProblemSolverEngine.generateSimilarInputs(topic)
            assertTrue("Topic $topic should have inputs", inputs.isNotEmpty())
        }
    }

    // -------------------------------------------------------------
    // BUG 5 REGRESSION TESTS: Bengali Decimal Input with Comma Separator
    // -------------------------------------------------------------
    @Test
    fun testBengaliDecimalInputParsing() {
        // Bengali digits with comma separator: ২,৫ parses as 2.5
        val v1 = com.example.ui.screens.parseBnToDouble("২,৫")
        assertNotNull(v1)
        assertEquals(2.5, v1!!, 0.0001)

        // Bengali digits with dot separator: ২.৫ parses as 2.5
        val v2 = com.example.ui.screens.parseBnToDouble("২.৫")
        assertNotNull(v2)
        assertEquals(2.5, v2!!, 0.0001)

        // ASCII 2.5 parses correctly
        val v3 = com.example.ui.screens.parseBnToDouble("2.5")
        assertNotNull(v3)
        assertEquals(2.5, v3!!, 0.0001)

        // ASCII 2,5 parses correctly
        val v3Comma = com.example.ui.screens.parseBnToDouble("2,5")
        assertNotNull(v3Comma)
        assertEquals(2.5, v3Comma!!, 0.0001)

        // Bengali integer ২৫ parses correctly as 25.0
        val v4 = com.example.ui.screens.parseBnToDouble("২৫")
        assertNotNull(v4)
        assertEquals(25.0, v4!!, 0.0001)

        // Negative decimal inputs
        val neg1 = com.example.ui.screens.parseBnToDouble("-২,৫")
        assertNotNull(neg1)
        assertEquals(-2.5, neg1!!, 0.0001)

        val neg2 = com.example.ui.screens.parseBnToDouble("-২.৫")
        assertNotNull(neg2)
        assertEquals(-2.5, neg2!!, 0.0001)

        val neg3 = com.example.ui.screens.parseBnToDouble("-২৫")
        assertNotNull(neg3)
        assertEquals(-25.0, neg3!!, 0.0001)

        // Sub-1 decimal inputs
        val subZero1 = com.example.ui.screens.parseBnToDouble("০,৫")
        assertNotNull(subZero1)
        assertEquals(0.5, subZero1!!, 0.0001)

        val subZero2 = com.example.ui.screens.parseBnToDouble(",৫")
        assertNotNull(subZero2)
        assertEquals(0.5, subZero2!!, 0.0001)

        // Multi-decimal place unambiguous commas
        val multi1 = com.example.ui.screens.parseBnToDouble("২,২৫")
        assertNotNull(multi1)
        assertEquals(2.25, multi1!!, 0.0001)

        // Ambiguous thousands separator rejection (e.g. 1,234 or ১,২৩৪ or multiple commas)
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble("1,234"))
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble("১,২৩৪"))
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble("25,000"))
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble("1,234,567"))
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble("1,00,000"))

        // Standard thousands grouping with dot decimal
        val withThousands = com.example.ui.screens.parseBnToDouble("1,234.56")
        assertNotNull(withThousands)
        assertEquals(1234.56, withThousands!!, 0.0001)

        // Invalid inputs
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble("abc"))
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble(""))
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble(null))
        org.junit.Assert.assertNull(com.example.ui.screens.parseBnToDouble("2,"))

        // Confirm existing Problem Solver functionality with parsed decimal input
        val solverRes = MathProblemSolverEngine.solvePercentage(
            MathProblemSolverEngine.PercentageType.OF_NUMBER,
            v1, // 2.5%
            200.0,
            SolverMode.QUICK
        )
        assertTrue(solverRes.isSuccess)
        assertEquals("৫", solverRes.finalAnswerBn) // 2.5% of 200 = 5
    }

    // -------------------------------------------------------------
    // BATCH 4 REGRESSION TESTS: Numeric Overflow, Sign Normalization, Boundary Safety
    // -------------------------------------------------------------
    @Test
    fun testFractionNegativeDenominatorNormalizationAndSigns() {
        // 1 / -2 + 1 / 2 = 0
        val resAdd = MathProblemSolverEngine.solveFraction(1L, -2L, "+", 1L, 2L, SolverMode.QUICK)
        assertTrue(resAdd.isSuccess)
        assertEquals("০", resAdd.finalAnswerBn)

        // 3 / -4 - 1 / 4 = -4 / 4 = -1
        val resSub = MathProblemSolverEngine.solveFraction(3L, -4L, "-", 1L, 4L, SolverMode.QUICK)
        assertTrue(resSub.isSuccess)
        assertEquals("-১", resSub.finalAnswerBn)

        // 1 / 2 / (-1 / 4) = 1/2 * -4/1 = -2
        val resDiv = MathProblemSolverEngine.solveFraction(1L, 2L, "/", -1L, 4L, SolverMode.QUICK)
        assertTrue(resDiv.isSuccess)
        assertEquals("-২", resDiv.finalAnswerBn)

        // -2 / 3 * -3 / 2 = 1
        val resMul = MathProblemSolverEngine.solveFraction(-2L, 3L, "*", -3L, 2L, SolverMode.QUICK)
        assertTrue(resMul.isSuccess)
        assertEquals("১", resMul.finalAnswerBn)
    }

    @Test
    fun testFractionDivisionByZeroNumeratorDivisor() {
        // 1/2 / 0/3 => division by 0 fraction
        val res = MathProblemSolverEngine.solveFraction(1L, 2L, "/", 0L, 3L, SolverMode.QUICK)
        assertFalse(res.isSuccess)
        assertNotNull(res.errorMessageBn)
        assertTrue(res.errorMessageBn?.contains("শূন্য") == true || res.errorMessageBn?.contains("০") == true)
    }

    @Test
    fun testFractionOverflowReturnsControlledError() {
        // Very large numerators that would overflow Long in multiplication
        val res = MathProblemSolverEngine.solveFraction(3_000_000_000L, 1L, "*", 4_000_000_000L, 1L, SolverMode.QUICK)
        assertFalse(res.isSuccess)
        assertNotNull(res.errorMessageBn)
        assertTrue(res.errorMessageBn?.contains("সীমার বাইরে") == true || res.errorMessageBn?.contains("ওভারফ্লো") == true)

        // Long.MIN_VALUE input
        val resMin = MathProblemSolverEngine.solveFraction(Long.MIN_VALUE, 1L, "+", 1L, 1L, SolverMode.QUICK)
        assertFalse(resMin.isSuccess)
        assertNotNull(resMin.errorMessageBn)
    }

    @Test
    fun testGcdLcmBoundariesAndOverflow() {
        // GCD with zero
        assertEquals(5L, com.example.math.NumberSystemProvider.gcd(5L, 0L))
        assertEquals(5L, com.example.math.NumberSystemProvider.gcd(0L, 5L))

        // GCD with negative numbers
        assertEquals(6L, com.example.math.NumberSystemProvider.gcd(-12L, 18L))
        assertEquals(6L, com.example.math.NumberSystemProvider.gcd(-12L, -18L))

        // GCD with Long.MIN_VALUE must calculate exact mathematical GCD
        assertEquals(2L, com.example.math.NumberSystemProvider.gcd(Long.MIN_VALUE, 2L))
        assertEquals(4L, com.example.math.NumberSystemProvider.gcd(Long.MIN_VALUE, 4L))
        assertEquals(16L, com.example.math.NumberSystemProvider.gcd(Long.MIN_VALUE, 16L))
        assertEquals(1L, com.example.math.NumberSystemProvider.gcd(Long.MIN_VALUE, 3L))
        assertEquals(Long.MAX_VALUE, com.example.math.NumberSystemProvider.gcd(Long.MIN_VALUE, Long.MIN_VALUE))

        // LCM overflow check returns -1
        val lcmOverflow = com.example.math.NumberSystemProvider.lcm(5_000_000_000L, 5_000_000_001L)
        assertEquals(-1L, lcmOverflow)

        // HCF/LCM solver with overflow values returns controlled error
        val resHcfLcm = MathProblemSolverEngine.solveHcfLcm(listOf(5_000_000_000L, 5_000_000_001L), SolverMode.QUICK)
        assertFalse(resHcfLcm.isSuccess)
        assertNotNull(resHcfLcm.errorMessageBn)
    }

    @Test
    fun testPrimeFactorizationBoundaries() {
        // 0 and 1 have no prime factors
        assertTrue(com.example.math.NumberSystemProvider.getPrimeFactors(0L).isEmpty())
        assertTrue(com.example.math.NumberSystemProvider.getPrimeFactors(1L).isEmpty())
        assertTrue(com.example.math.NumberSystemProvider.getPrimeFactors(-10L).isEmpty())

        // 2 has prime factor [2]
        assertEquals(listOf(2L), com.example.math.NumberSystemProvider.getPrimeFactors(2L))

        // Factors of 1 is [1]
        assertEquals(listOf(1L), com.example.math.NumberSystemProvider.getFactors(1L))
        assertTrue(com.example.math.NumberSystemProvider.getFactors(0L).isEmpty())
        assertTrue(com.example.math.NumberSystemProvider.getFactors(-5L).isEmpty())

        // Prime check for large prime does not overflow loop
        assertTrue(com.example.math.PrimeNumberProvider.isPrime(104729))
        assertFalse(com.example.math.PrimeNumberProvider.isPrime(1))
        assertFalse(com.example.math.PrimeNumberProvider.isPrime(0))
        assertFalse(com.example.math.PrimeNumberProvider.isPrime(-7))
    }

    @Test
    fun testRatioSimplifyValidation() {
        // Non-positive terms rejected
        val resZero = MathProblemSolverEngine.solveRatio(
            MathProblemSolverEngine.RatioType.SIMPLIFY,
            0.0,
            0.0,
            5.0,
            null,
            SolverMode.QUICK
        )
        assertFalse(resZero.isSuccess)
        assertNotNull(resZero.errorMessageBn)

        val resNeg = MathProblemSolverEngine.solveRatio(
            MathProblemSolverEngine.RatioType.SIMPLIFY,
            0.0,
            -4.0,
            8.0,
            null,
            SolverMode.QUICK
        )
        assertFalse(resNeg.isSuccess)
        assertNotNull(resNeg.errorMessageBn)
    }

    @Test
    fun testSquareCubeBoundaryAndOverflow() {
        // NaN input
        val nanRes = MathProblemSolverEngine.solveSquareCube(Double.NaN, MathProblemSolverEngine.SquareCubeOp.SQUARE, SolverMode.QUICK)
        assertFalse(nanRes.isSuccess)

        // Infinity input
        val infRes = MathProblemSolverEngine.solveSquareCube(Double.POSITIVE_INFINITY, MathProblemSolverEngine.SquareCubeOp.SQUARE, SolverMode.QUICK)
        assertFalse(infRes.isSuccess)

        // Overflow in square
        val overflowSq = MathProblemSolverEngine.solveSquareCube(1e200, MathProblemSolverEngine.SquareCubeOp.SQUARE, SolverMode.QUICK)
        assertFalse(overflowSq.isSuccess)
        assertNotNull(overflowSq.errorMessageBn)

        // Overflow in cube
        val overflowCb = MathProblemSolverEngine.solveSquareCube(1e150, MathProblemSolverEngine.SquareCubeOp.CUBE, SolverMode.QUICK)
        assertFalse(overflowCb.isSuccess)
        assertNotNull(overflowCb.errorMessageBn)
    }

    @Test
    fun testFinalReleaseGateVerification() {
        // 1. GCD Long.MIN_VALUE powers of 2
        assertEquals(2L, com.example.math.NumberSystemProvider.gcd(Long.MIN_VALUE, 2L))
        assertEquals(64L, com.example.math.NumberSystemProvider.gcd(Long.MIN_VALUE, 64L))
        assertEquals(1024L, com.example.math.NumberSystemProvider.gcd(Long.MIN_VALUE, 1024L))

        // 2. LCM overflow sentinel consistency in calculateGcdLcm
        val resOverflow = com.example.math.NumberSystemProvider.calculateGcdLcm(5_000_000_000L, 5_000_000_001L)
        assertEquals(-1L, resOverflow.lcm)
        assertTrue(resOverflow.formulaCheckBn.contains("অতিরিক্ত") || resOverflow.formulaCheckBn.contains("ওভারফ্লো"))
        assertFalse(resOverflow.formulaCheckBn.contains("উভয় পক্ষ সমান"))
        assertTrue(resOverflow.lcmSteps.any { it.detailBn.contains("সীমার বাইরে") || it.detailBn.contains("ওভারফ্লো") })

        // 3. Fraction normalization and checked arithmetic for extreme values
        val minFracRes = MathProblemSolverEngine.solveFraction(Long.MIN_VALUE, 1L, "+", 0L, 1L, SolverMode.QUICK)
        assertFalse(minFracRes.isSuccess)

        val negDenFrac = com.example.math.VisualMathEngine.analyzeFraction(1, -2)
        assertEquals(-1, negDenFrac.simplifiedNumerator)
        assertEquals(2, negDenFrac.simplifiedDenominator)
        assertEquals(2, negDenFrac.denominator)

        // 4. Prime/composite classification on extreme and large values
        val classEven = com.example.math.NumberSystemProvider.classifyNumber(1_000_000_000_000L)
        assertTrue(classEven.contains("যৌগিক"))
        val classPrime = com.example.math.NumberSystemProvider.classifyNumber(104729L)
        assertTrue(classPrime.contains("মৌলিক"))

        // 5. Triangle perimeter with negative side
        val negSideTri = com.example.math.VisualMathEngine.calculateTriangle(6.0, 4.0, sideA = -1.0, sideC = 5.0)
        assertFalse(negSideTri.isValid)
        assertNotNull(negSideTri.errorMessageBn)
    }
}
