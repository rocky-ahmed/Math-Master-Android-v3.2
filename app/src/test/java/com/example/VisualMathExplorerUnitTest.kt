package com.example

import com.example.math.GeoShapeType
import com.example.math.VisualMathEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sqrt

/**
 * Unit tests for Phase 5: Visual Math Explorer.
 * Covers:
 * 1. Geometry Calculations (Triangle, Rectangle, Square, Circle) and boundary conditions.
 * 2. Visual Linear Algebra Balance Scale transformations and substitution verification.
 * 3. Number Line and Fraction-to-Decimal conversions and classifications.
 * 4. Formula Visualizer calculations and step-by-step mathematical explanations.
 */
class VisualMathExplorerUnitTest {

    // -------------------------------------------------------------
    // 1. GEOMETRIC CALCULATIONS & BOUNDARY CONDITIONS
    // -------------------------------------------------------------

    @Test
    fun testRectangleCalculation() {
        val res = VisualMathEngine.calculateRectangle(8.0, 5.0)
        assertTrue(res.isValid)
        assertEquals(40.0, res.primaryArea, 0.001)
        assertEquals(26.0, res.primaryPerimeterOrCircumference, 0.001)
        assertTrue(res.explanationStepsBn.isNotEmpty())
        assertTrue(res.formulaExpressionBn.contains("A = a · b"))
    }

    @Test
    fun testSquareCalculation() {
        val res = VisualMathEngine.calculateSquare(6.0)
        assertTrue(res.isValid)
        assertEquals(36.0, res.primaryArea, 0.001)
        assertEquals(24.0, res.primaryPerimeterOrCircumference, 0.001)
        assertTrue(res.explanationStepsBn.any { it.contains("কর্ণ") })
    }

    @Test
    fun testTriangleCalculationInsufficientInputs() {
        val res = VisualMathEngine.calculateTriangle(10.0, 7.0)
        assertTrue(res.isValid)
        // Area = 0.5 * 10 * 7 = 35.0
        assertEquals(35.0, res.primaryArea, 0.001)
        // With only base and height, general triangle perimeter is indeterminate
        assertFalse(res.isPerimeterExact)
        assertEquals(0.0, res.primaryPerimeterOrCircumference, 0.001)
        assertNotNull(res.perimeterNoteBn)
        assertTrue(res.explanationStepsBn.any { it.contains("বাকি দুটি বাহুর দৈর্ঘ্য") })
    }

    @Test
    fun testTriangleCalculationWithValidSufficientSides() {
        // Valid triangle with sides 5, 5, base 6, height 4 (isosceles)
        val res = VisualMathEngine.calculateTriangle(base = 6.0, height = 4.0, sideA = 5.0, sideC = 5.0)
        assertTrue(res.isValid)
        assertTrue(res.isPerimeterExact)
        assertEquals(12.0, res.primaryArea, 0.001)
        assertEquals(16.0, res.primaryPerimeterOrCircumference, 0.001) // 5 + 6 + 5 = 16
        assertTrue(res.explanationStepsBn.any { it.contains("পরিসীমা: তিন বাহুর সমষ্টি P = a + b + c") })
    }

    @Test
    fun testTriangleCalculationWithInvalidSides() {
        // Triangle inequality violation: 2 + 2 < 10
        val invalidSides = VisualMathEngine.calculateTriangle(base = 10.0, height = 3.0, sideA = 2.0, sideC = 2.0)
        assertFalse(invalidSides.isValid)
        assertNotNull(invalidSides.errorMessageBn)

        // Side shorter than altitude: sideA (2.0) < height (4.0)
        val shortSide = VisualMathEngine.calculateTriangle(base = 6.0, height = 4.0, sideA = 2.0, sideC = 5.0)
        assertFalse(shortSide.isValid)
        assertNotNull(shortSide.errorMessageBn)
    }

    @Test
    fun testCircleCalculation() {
        val res = VisualMathEngine.calculateCircle(7.0)
        assertTrue(res.isValid)
        val expectedArea = PI * 49.0
        val expectedCircumference = 2 * PI * 7.0
        assertEquals(expectedArea, res.primaryArea, 0.01)
        assertEquals(expectedCircumference, res.primaryPerimeterOrCircumference, 0.01)
        assertTrue(res.explanationStepsBn.any { it.contains("ব্যাসার্ধ") })
    }

    @Test
    fun testGeometryInvalidInputsHandledSafely() {
        // Zero or negative values should not crash and must flag isValid = false
        val zeroRect = VisualMathEngine.calculateRectangle(0.0, 5.0)
        assertFalse(zeroRect.isValid)
        assertNotNull(zeroRect.errorMessageBn)

        val negSquare = VisualMathEngine.calculateSquare(-4.0)
        assertFalse(negSquare.isValid)
        assertNotNull(negSquare.errorMessageBn)

        val negCircle = VisualMathEngine.calculateCircle(-1.0)
        assertFalse(negCircle.isValid)
        assertNotNull(negCircle.errorMessageBn)

        val zeroTriangle = VisualMathEngine.calculateTriangle(0.0, 0.0)
        assertFalse(zeroTriangle.isValid)
        assertNotNull(zeroTriangle.errorMessageBn)
    }

    // -------------------------------------------------------------
    // 2. VISUAL ALGEBRA (BALANCE-SCALE LINEAR EQUATIONS)
    // -------------------------------------------------------------

    @Test
    fun testVisualAlgebraStandardEquation() {
        // 2x + 4 = 14 => 2x = 10 => x = 5
        val res = VisualMathEngine.solveVisualAlgebra(2.0, 4.0, 14.0)
        assertTrue(res.isSolvable)
        assertEquals(5.0, res.solutionX, 0.001)
        // Must contain balance transformation steps
        assertTrue(res.steps.size >= 2)
        // Must contain substitution verification
        assertTrue(res.verificationBn.contains("LHS") && res.verificationBn.contains("RHS"))
    }

    @Test
    fun testVisualAlgebraNegativeConstant() {
        // 4x - 2 = 14 => 4x = 16 => x = 4
        val res = VisualMathEngine.solveVisualAlgebra(4.0, -2.0, 14.0)
        assertTrue(res.isSolvable)
        assertEquals(4.0, res.solutionX, 0.001)
    }

    @Test
    fun testVisualAlgebraZeroCoefficientError() {
        // 0x + 5 = 10 is not a linear equation in x
        val res = VisualMathEngine.solveVisualAlgebra(0.0, 5.0, 10.0)
        assertFalse(res.isSolvable)
        assertNotNull(res.errorMessageBn)
    }

    // -------------------------------------------------------------
    // 3. NUMBER LINE & FRACTION ANALYSIS
    // -------------------------------------------------------------

    @Test
    fun testProperFractionAnalysis() {
        val frac = VisualMathEngine.analyzeFraction(3, 4)
        assertEquals(0.75, frac.decimalValue, 0.001)
        assertEquals(3, frac.simplifiedNumerator)
        assertEquals(4, frac.simplifiedDenominator)
        assertTrue(frac.fractionTypeBn.contains("প্রকৃত"))
        assertTrue(frac.comparisonExplanationBn.contains("০"))
    }

    @Test
    fun testImproperFractionSimplification() {
        // 6/4 simplifies to 3/2 = 1.5
        val frac = VisualMathEngine.analyzeFraction(6, 4)
        assertEquals(1.5, frac.decimalValue, 0.001)
        assertEquals(3, frac.simplifiedNumerator)
        assertEquals(2, frac.simplifiedDenominator)
        assertTrue(frac.fractionTypeBn.contains("অপ্রকৃত"))
    }

    @Test
    fun testZeroDenominatorHandledSafely() {
        // Denominator 0 should be guarded safely
        val frac = VisualMathEngine.analyzeFraction(5, 0)
        assertEquals(5.0, frac.decimalValue, 0.001)
        assertNotNull(frac.fractionTypeBn)
    }

    // -------------------------------------------------------------
    // 4. FORMULA VISUALIZER PRESETS & CALCULATIONS
    // -------------------------------------------------------------

    @Test
    fun testPythagorasFormulaVisualization() {
        val pythagorasModel = VisualMathEngine.formulaVisualizerList.find { it.id == "vis_pythagoras" }
        assertNotNull(pythagorasModel)

        // 3, 4, 5 right-angled triangle
        val result = pythagorasModel!!.calculate(3.0, 4.0, 0.0)
        assertEquals(5.0, result.outputValue, 0.001)
        assertTrue(result.calculationStepsBn.any { it.contains("c² = a² + b²") })
    }

    @Test
    fun testSimpleInterestFormulaVisualization() {
        val interestModel = VisualMathEngine.formulaVisualizerList.find { it.id == "vis_simple_interest" }
        assertNotNull(interestModel)

        // P = 1000, n = 2 years, r = 10% => I = (1000 * 2 * 10) / 100 = 200
        val result = interestModel!!.calculate(1000.0, 2.0, 10.0)
        assertEquals(200.0, result.outputValue, 0.001)
        assertTrue(result.calculationStepsBn.any { it.contains("সুদ-আসল") })
    }

    @Test
    fun testCubeVolumeFormulaVisualization() {
        val cubeModel = VisualMathEngine.formulaVisualizerList.find { it.id == "vis_cube_vol" }
        assertNotNull(cubeModel)

        // side = 4 => volume = 64
        val result = cubeModel!!.calculate(4.0, 0.0, 0.0)
        assertEquals(64.0, result.outputValue, 0.001)
        assertTrue(result.calculationStepsBn.any { it.contains("ঘন একক") })
    }
}
