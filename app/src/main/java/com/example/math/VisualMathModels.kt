package com.example.math

import java.util.Locale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * Geometric shape types supported in Geometry Explorer.
 */
enum class GeoShapeType(val id: String, val titleBn: String, val formulaSummaryBn: String) {
    TRIANGLE("triangle", "ত্রিভুজ (Triangle)", "ক্ষেত্রফল = ½ × ভূমি × উচ্চতা"),
    RECTANGLE("rectangle", "আয়তক্ষেত্র (Rectangle)", "ক্ষেত্রফল = দৈর্ঘ্য × প্রস্থ"),
    SQUARE("square", "বর্গক্ষেত্র (Square)", "ক্ষেত্রফল = বাহু²"),
    CIRCLE("circle", "বৃত্ত (Circle)", "ক্ষেত্রফল = πr², পরিধি = 2πr")
}

data class GeometryCalculationResult(
    val shapeType: GeoShapeType,
    val primaryArea: Double,
    val primaryPerimeterOrCircumference: Double,
    val formulaTitleBn: String,
    val formulaExpressionBn: String,
    val explanationStepsBn: List<String>,
    val isValid: Boolean,
    val errorMessageBn: String? = null,
    val isPerimeterExact: Boolean = true,
    val perimeterNoteBn: String? = null
)

/**
 * Transformation step in visual linear algebra (balance-scale).
 */
data class AlgebraStep(
    val stepNumber: Int,
    val explanationBn: String,
    val leftExprBn: String,
    val rightExprBn: String,
    val balanceActionBn: String
)

data class VisualAlgebraResult(
    val a: Double, // coefficient of x
    val b: Double, // constant added/subtracted
    val c: Double, // right hand side
    val solutionX: Double,
    val isSolvable: Boolean,
    val steps: List<AlgebraStep>,
    val verificationBn: String,
    val errorMessageBn: String? = null
)

/**
 * Visual Fraction representation model.
 */
data class FractionVisualModel(
    val numerator: Int,
    val denominator: Int,
    val decimalValue: Double,
    val simplifiedNumerator: Int,
    val simplifiedDenominator: Int,
    val fractionTypeBn: String, // প্রকৃত ভগ্নাংশ, অপ্রকৃত ভগ্নাংশ, সমতুল্য
    val comparisonExplanationBn: String
)

/**
 * Visual Formula item that connects to visual interactive diagram & inputs.
 */
data class VisualFormulaModel(
    val id: String,
    val libraryFormulaId: String?,
    val titleBn: String,
    val categoryBn: String,
    val formulaExpression: String,
    val param1NameBn: String,
    val param1Default: Double,
    val param1Min: Double,
    val param1Max: Double,
    val param2NameBn: String? = null,
    val param2Default: Double = 0.0,
    val param2Min: Double = 0.0,
    val param2Max: Double = 0.0,
    val param3NameBn: String? = null,
    val param3Default: Double = 0.0,
    val param3Min: Double = 0.0,
    val param3Max: Double = 0.0,
    val calculate: (p1: Double, p2: Double, p3: Double) -> FormulaVisualResult
)

data class FormulaVisualResult(
    val outputValue: Double,
    val outputLabelBn: String,
    val calculationStepsBn: List<String>,
    val diagramNotesBn: String
)

/**
 * Calculation and formatting engine for Phase 5: Visual Math Explorer.
 */
object VisualMathEngine {

    fun formatNumberBn(value: Double, decimals: Int = 2): String {
        return if (value % 1.0 == 0.0) {
            BengaliNumberFormatter.toBn(value.toLong())
        } else {
            val formatted = String.format(Locale.US, "%.${decimals}f", value)
                .trimEnd('0')
                .trimEnd('.')
            BengaliNumberFormatter.toBn(formatted)
        }
    }

    // =========================================================================
    // A. GEOMETRY CALCULATIONS
    // =========================================================================

    fun calculateTriangle(base: Double, height: Double, sideA: Double = 0.0, sideC: Double = 0.0): GeometryCalculationResult {
        if (base <= 0 || height <= 0 || sideA < 0.0 || sideC < 0.0) {
            return GeometryCalculationResult(
                shapeType = GeoShapeType.TRIANGLE,
                primaryArea = 0.0,
                primaryPerimeterOrCircumference = 0.0,
                formulaTitleBn = "ত্রিভুজের পরিমাপ",
                formulaExpressionBn = "ক্ষেত্রফল = ½ × ভূমি × উচ্চতা",
                explanationStepsBn = emptyList(),
                isValid = false,
                errorMessageBn = "ভূমি, উচ্চতা এবং বাহুগুলোর দৈর্ঘ্য কখনো ঋণাত্মক বা শূন্য হতে পারে না।",
                isPerimeterExact = false
            )
        }

        val area = 0.5 * base * height

        // If both additional sides are provided, verify geometric validity and calculate exact perimeter
        if (sideA > 0.0 && sideC > 0.0) {
            val violatesAltitude = sideA < height - 1e-9 || sideC < height - 1e-9
            val violatesInequality = (sideA + sideC <= base + 1e-9) ||
                    (sideA + base <= sideC + 1e-9) ||
                    (sideC + base <= sideA + 1e-9)

            if (violatesAltitude || violatesInequality) {
                return GeometryCalculationResult(
                    shapeType = GeoShapeType.TRIANGLE,
                    primaryArea = 0.0,
                    primaryPerimeterOrCircumference = 0.0,
                    formulaTitleBn = "ত্রিভুজের পরিমাপ",
                    formulaExpressionBn = "ক্ষেত্রফল = ½ × ভূমি × উচ্চতা",
                    explanationStepsBn = emptyList(),
                    isValid = false,
                    errorMessageBn = "প্রদত্ত বাহুত্রয় ও উচ্চতা দিয়ে ত্রিভুজ গঠন সম্ভব নয় (ত্রিভুজ অসমতা বা বাহু < উচ্চতা)।",
                    isPerimeterExact = false
                )
            }

            val perimeter = base + sideA + sideC
            val steps = listOf(
                "প্রদত্ত মান: ভূমি (b) = ${formatNumberBn(base)} একক, উচ্চতা (h) = ${formatNumberBn(height)} একক, বাহু a = ${formatNumberBn(sideA)} একক, বাহু c = ${formatNumberBn(sideC)} একক।",
                "ক্ষেত্রফলের সূত্র: A = ½ × ভূমি × উচ্চতা = ½ × ${formatNumberBn(base)} × ${formatNumberBn(height)}",
                "গণনা: A = ½ × ${formatNumberBn(base * height)} = ${formatNumberBn(area)} বর্গ একক।",
                "পরিসীমা: তিন বাহুর সমষ্টি P = a + b + c = ${formatNumberBn(sideA)} + ${formatNumberBn(base)} + ${formatNumberBn(sideC)} = ${formatNumberBn(perimeter)} একক।"
            )

            return GeometryCalculationResult(
                shapeType = GeoShapeType.TRIANGLE,
                primaryArea = area,
                primaryPerimeterOrCircumference = perimeter,
                formulaTitleBn = "সাধারণ ত্রিভুজের ক্ষেত্রফল ও সঠিক পরিসীমা",
                formulaExpressionBn = "A = ½ · b · h,  P = a + b + c",
                explanationStepsBn = steps,
                isValid = true,
                isPerimeterExact = true,
                perimeterNoteBn = null
            )
        }

        // When only base and height are provided: general triangle perimeter is indeterminate
        val perimeterNote = "সাধারণ ত্রিভুজে কেবল ভূমি ও উচ্চতা দিয়ে সঠিক পরিসীমা নির্ণয় করা সম্ভব নয়। পরিসীমা (a + b + c) নির্ধারণ করতে অপর দুটি বাহুর দৈর্ঘ্য বা নির্দিষ্ট জ্যামিতিক বৈশিষ্ট্য জানা প্রয়োজন।"
        val steps = listOf(
            "প্রদত্ত মান: ভূমি (b) = ${formatNumberBn(base)} একক, উচ্চতা (h) = ${formatNumberBn(height)} একক।",
            "ক্ষেত্রফলের সূত্র: A = ½ × ভূমি × উচ্চতা = ½ × ${formatNumberBn(base)} × ${formatNumberBn(height)}",
            "গণনা: A = ½ × ${formatNumberBn(base * height)} = ${formatNumberBn(area)} বর্গ একক।",
            "পরিসীমার বিশ্লেষণ: কেবল ভূমি ও উচ্চতা দিয়ে সাধারণ ত্রিভুজের সঠিক পরিসীমা নির্ণয় করা সম্ভব নয় (বাকি দুটি বাহুর দৈর্ঘ্য বা কোণ আবশ্যক)।"
        )

        return GeometryCalculationResult(
            shapeType = GeoShapeType.TRIANGLE,
            primaryArea = area,
            primaryPerimeterOrCircumference = 0.0,
            formulaTitleBn = "সাধারণ ত্রিভুজের ক্ষেত্রফল",
            formulaExpressionBn = "ক্ষেত্রফল A = ½ · b · h",
            explanationStepsBn = steps,
            isValid = true,
            isPerimeterExact = false,
            perimeterNoteBn = perimeterNote
        )
    }

    fun calculateRectangle(length: Double, width: Double): GeometryCalculationResult {
        if (length <= 0 || width <= 0) {
            return GeometryCalculationResult(
                shapeType = GeoShapeType.RECTANGLE,
                primaryArea = 0.0,
                primaryPerimeterOrCircumference = 0.0,
                formulaTitleBn = "আয়তক্ষেত্রের পরিমাপ",
                formulaExpressionBn = "ক্ষেত্রফল = দৈর্ঘ্য × প্রস্থ, পরিসীমা = ২(দৈর্ঘ্য + প্রস্থ)",
                explanationStepsBn = emptyList(),
                isValid = false,
                errorMessageBn = "দৈর্ঘ্য এবং প্রস্থ অবশ্যই শূন্যের চেয়ে বড় হতে হবে।"
            )
        }

        val area = length * width
        val perimeter = 2 * (length + width)
        val diagonal = sqrt(length.pow(2) + width.pow(2))

        val steps = listOf(
            "প্রদত্ত মান: দৈর্ঘ্য (a) = ${formatNumberBn(length)} একক, প্রস্থ (b) = ${formatNumberBn(width)} একক।",
            "ক্ষেত্রফল: A = a × b = ${formatNumberBn(length)} × ${formatNumberBn(width)} = ${formatNumberBn(area)} বর্গ একক।",
            "পরিসীমা: P = 2(a + b) = 2(${formatNumberBn(length)} + ${formatNumberBn(width)}) = 2 × ${formatNumberBn(length + width)} = ${formatNumberBn(perimeter)} একক।",
            "কর্ণ (পিথাগোরাস অনুসারে): d = √(a² + b²) = √(${formatNumberBn(length.pow(2))} + ${formatNumberBn(width.pow(2))}) = ${formatNumberBn(diagonal)} একক।"
        )

        return GeometryCalculationResult(
            shapeType = GeoShapeType.RECTANGLE,
            primaryArea = area,
            primaryPerimeterOrCircumference = perimeter,
            formulaTitleBn = "আয়তক্ষেত্রের ক্ষেত্রফল ও পরিসীমা",
            formulaExpressionBn = "A = a · b,  P = 2(a + b),  d = √(a² + b²)",
            explanationStepsBn = steps,
            isValid = true
        )
    }

    fun calculateSquare(side: Double): GeometryCalculationResult {
        if (side <= 0) {
            return GeometryCalculationResult(
                shapeType = GeoShapeType.SQUARE,
                primaryArea = 0.0,
                primaryPerimeterOrCircumference = 0.0,
                formulaTitleBn = "বর্গক্ষেত্রের পরিমাপ",
                formulaExpressionBn = "ক্ষেত্রফল = বাহু², পরিসীমা = ৪ × বাহু",
                explanationStepsBn = emptyList(),
                isValid = false,
                errorMessageBn = "বাহুর দৈর্ঘ্য অবশ্যই শূন্যের চেয়ে বড় হতে হবে।"
            )
        }

        val area = side * side
        val perimeter = 4 * side
        val diagonal = side * sqrt(2.0)

        val steps = listOf(
            "প্রদত্ত মান: বাহু (a) = ${formatNumberBn(side)} একক।",
            "বর্গক্ষেত্রের চার বাহু সমান এবং প্রত্যেকটি কোণ ৯০°।",
            "ক্ষেত্রফল: A = a² = ${formatNumberBn(side)}² = ${formatNumberBn(area)} বর্গ একক।",
            "পরিসীমা: P = 4a = 4 × ${formatNumberBn(side)} = ${formatNumberBn(perimeter)} একক।",
            "কর্ণ: d = a√2 = ${formatNumberBn(side)} × 1.4142 ≈ ${formatNumberBn(diagonal)} একক।"
        )

        return GeometryCalculationResult(
            shapeType = GeoShapeType.SQUARE,
            primaryArea = area,
            primaryPerimeterOrCircumference = perimeter,
            formulaTitleBn = "বর্গক্ষেত্রের ক্ষেত্রফল, পরিসীমা ও কর্ণ",
            formulaExpressionBn = "A = a²,  P = 4a,  d = a√2",
            explanationStepsBn = steps,
            isValid = true
        )
    }

    fun calculateCircle(radius: Double): GeometryCalculationResult {
        if (radius <= 0) {
            return GeometryCalculationResult(
                shapeType = GeoShapeType.CIRCLE,
                primaryArea = 0.0,
                primaryPerimeterOrCircumference = 0.0,
                formulaTitleBn = "বৃত্তের পরিমাপ",
                formulaExpressionBn = "ক্ষেত্রফল = πr², পরিধি = 2πr",
                explanationStepsBn = emptyList(),
                isValid = false,
                errorMessageBn = "ব্যাসার্ধ (r) অবশ্যই শূন্যের চেয়ে বড় হতে হবে।"
            )
        }

        val area = PI * radius * radius
        val circumference = 2 * PI * radius
        val diameter = 2 * radius

        val steps = listOf(
            "প্রদত্ত মান: ব্যাসার্ধ (r) = ${formatNumberBn(radius)} একক (ব্যাস d = ${formatNumberBn(diameter)} একক)।",
            "পাই (π) এর মান প্রায় ৩.১৪১৫৯ (বা ২২/৭)।",
            "পরিধি: C = 2πr = 2 × 3.14159 × ${formatNumberBn(radius)} = ${formatNumberBn(circumference)} একক।",
            "ক্ষেত্রফল: A = πr² = 3.14159 × ${formatNumberBn(radius)}² = 3.14159 × ${formatNumberBn(radius * radius)} = ${formatNumberBn(area)} বর্গ একক।"
        )

        return GeometryCalculationResult(
            shapeType = GeoShapeType.CIRCLE,
            primaryArea = area,
            primaryPerimeterOrCircumference = circumference,
            formulaTitleBn = "বৃত্তের ক্ষেত্রফল ও পরিধি",
            formulaExpressionBn = "C = 2πr,  A = πr²,  d = 2r",
            explanationStepsBn = steps,
            isValid = true
        )
    }

    // =========================================================================
    // B. VISUAL ALGEBRA (Balance-Scale Linear Equations ax + b = c)
    // =========================================================================

    fun solveVisualAlgebra(a: Double, b: Double, c: Double): VisualAlgebraResult {
        if (abs(a) < 0.0001) {
            return VisualAlgebraResult(
                a = a, b = b, c = c,
                solutionX = 0.0,
                isSolvable = false,
                steps = emptyList(),
                verificationBn = "",
                errorMessageBn = "x এর সহগ (a) শূন্য হতে পারবে না। এটি সরল সমীকরণ নয়।"
            )
        }

        val steps = mutableListOf<AlgebraStep>()
        val signB = if (b >= 0) "+ ${formatNumberBn(b)}" else "- ${formatNumberBn(abs(b))}"
        val initialLeft = "${formatNumberBn(a)}x $signB"
        val initialRight = formatNumberBn(c)

        steps.add(
            AlgebraStep(
                stepNumber = 1,
                explanationBn = "প্রারম্ভিক সমীকরণ দাঁড়িপাল্লায় স্থাপন: বাম পাল্লায় ${initialLeft} এবং ডান পাল্লায় ${initialRight}। উভয় পাল্লা সমান ভারে ভারসাম্যে রয়েছে।",
                leftExprBn = initialLeft,
                rightExprBn = initialRight,
                balanceActionBn = "ভারসাম্য অবস্থা: সমীকরণ ${initialLeft} = ${initialRight}"
            )
        )

        // Step 2: Eliminate constant b
        val cMinusB = c - b
        if (b != 0.0) {
            val eliminateAction = if (b > 0) {
                "উভয় পাল্লা থেকে ধ্রুবক ${formatNumberBn(abs(b))} বাদ (বিয়োগ) করি"
            } else {
                "উভয় পাল্লায় ধ্রুবক ${formatNumberBn(abs(b))} যোগ করি"
            }
            val step2Left = "${formatNumberBn(a)}x"
            val step2Right = formatNumberBn(cMinusB)

            steps.add(
                AlgebraStep(
                    stepNumber = 2,
                    explanationBn = "$eliminateAction যাতে বাম পাল্লায় কেবল চলকের অংশ থাকে। সমীকরণের ভারসাম্য অক্ষুণ্ণ রাখতে উভয় দিকে একই ক্রিয়া প্রয়োগ করা হয়।",
                    leftExprBn = step2Left,
                    rightExprBn = step2Right,
                    balanceActionBn = "${formatNumberBn(a)}x = ${initialRight} ${if (b > 0) "- ${formatNumberBn(abs(b))}" else "+ ${formatNumberBn(abs(b))}"} = $step2Right"
                )
            )
        }

        // Step 3: Divide by coefficient a
        val solutionX = cMinusB / a
        val step3Left = "x"
        val step3Right = formatNumberBn(solutionX)

        steps.add(
            AlgebraStep(
                stepNumber = steps.size + 1,
                explanationBn = "প্রতিটি x এর আসল মান জানতে উভয় পাল্লাকে ${formatNumberBn(a)} দিয়ে ভাগ করি।",
                leftExprBn = step3Left,
                rightExprBn = step3Right,
                balanceActionBn = "x = ${formatNumberBn(cMinusB)} ÷ ${formatNumberBn(a)} = $step3Right"
            )
        )

        // Verification step
        val substitutedLeft = (a * solutionX) + b
        val verificationBn = "বামপক্ষে x = ${formatNumberBn(solutionX)} বসিয়ে যাচাই:\n" +
                "LHS = ${formatNumberBn(a)}(${formatNumberBn(solutionX)}) $signB\n" +
                "= ${formatNumberBn(a * solutionX)} $signB\n" +
                "= ${formatNumberBn(substitutedLeft)} = RHS (প্রমাণিত ও সত্য!)"

        return VisualAlgebraResult(
            a = a, b = b, c = c,
            solutionX = solutionX,
            isSolvable = true,
            steps = steps,
            verificationBn = verificationBn
        )
    }

    // =========================================================================
    // C. NUMBER LINE & FRACTIONS
    // =========================================================================

    private fun gcd(a: Long, b: Long): Long {
        var n1 = abs(a)
        var n2 = abs(b)
        while (n2 != 0L) {
            val temp = n2
            n2 = n1 % n2
            n1 = temp
        }
        return if (n1 == 0L) 1L else n1
    }

    fun analyzeFraction(num: Int, den: Int): FractionVisualModel {
        val safeDen = if (den == 0) 1 else den
        val normNum = if (safeDen < 0) -num else num
        val normDen = if (safeDen < 0) -safeDen else safeDen
        val g = gcd(abs(normNum).toLong(), normDen.toLong()).toInt()
        val simpNum = normNum / g
        val simpDen = normDen / g
        val decVal = normNum.toDouble() / normDen.toDouble()

        val typeBn = when {
            normNum == 0 -> "শূন্য ভগ্নাংশ (Zero Fraction)"
            abs(normNum) < normDen -> "প্রকৃত ভগ্নাংশ (Proper Fraction, মান ১ এর কম)"
            abs(normNum) == normDen -> "একক পূর্ণ ভগ্নাংশ (Whole Number = ১)"
            else -> "অপ্রকৃত ভগ্নাংশ (Improper Fraction, মান ১ এর বেশি)"
        }

        val compBn = StringBuilder()
        compBn.append("ভগ্নাংশ: ${BengaliNumberFormatter.toBn(normNum)}/${BengaliNumberFormatter.toBn(normDen)}")
        if (g > 1 || safeDen < 0) {
            compBn.append(" (লঘিষ্ঠ রূপ: ${BengaliNumberFormatter.toBn(simpNum)}/${BengaliNumberFormatter.toBn(simpDen)})")
        }
        compBn.append("\nদশমিক সমতুল্য মান: ${formatNumberBn(decVal, 4)}")

        if (decVal < 0) {
            compBn.append("\nঅবস্থান: সংখ্যার রেখায় এটি শূন্য (০) এর বামপাশে ঋণাত্মক অংশে অবস্থান করে।")
        } else if (decVal == 0.0) {
            compBn.append("\nঅবস্থান: সংখ্যার রেখায় এটি নিরপেক্ষ কেন্দ্রবিন্দু শূন্য (০)।")
        } else {
            val lowerInt = decVal.toInt()
            val upperInt = lowerInt + 1
            compBn.append("\nঅবস্থান: সংখ্যার রেখায় এটি ${BengaliNumberFormatter.toBn(lowerInt)} এবং ${BengaliNumberFormatter.toBn(upperInt)} এর মাঝে অবস্থিত।")
        }

        return FractionVisualModel(
            numerator = normNum,
            denominator = normDen,
            decimalValue = decVal,
            simplifiedNumerator = simpNum,
            simplifiedDenominator = simpDen,
            fractionTypeBn = typeBn,
            comparisonExplanationBn = compBn.toString()
        )
    }

    // =========================================================================
    // D. FORMULA VISUALIZER PRESETS
    // =========================================================================

    val formulaVisualizerList: List<VisualFormulaModel> = listOf(
        VisualFormulaModel(
            id = "vis_pythagoras",
            libraryFormulaId = "lib_geom_04",
            titleBn = "পিথাগোরাসের উপপাদ্য (সমকোণী ত্রিভুজ)",
            categoryBn = "জ্যামিতি (Geometry)",
            formulaExpression = "c² = a² + b²  ⇒  c = √(a² + b²)",
            param1NameBn = "লম্ব (a)",
            param1Default = 3.0,
            param1Min = 1.0,
            param1Max = 20.0,
            param2NameBn = "ভূমি (b)",
            param2Default = 4.0,
            param2Min = 1.0,
            param2Max = 20.0,
            calculate = { a, b, _ ->
                val cSq = a * a + b * b
                val c = sqrt(cSq)
                FormulaVisualResult(
                    outputValue = c,
                    outputLabelBn = "অতিভুজ (c)",
                    calculationStepsBn = listOf(
                        "প্রদত্ত লম্ব a = ${formatNumberBn(a)}, ভূমি b = ${formatNumberBn(b)}",
                        "পিথাগোরাসের সূত্র: c² = a² + b² = ${formatNumberBn(a)}² + ${formatNumberBn(b)}²",
                        "বর্গ মান: c² = ${formatNumberBn(a * a)} + ${formatNumberBn(b * b)} = ${formatNumberBn(cSq)}",
                        "বর্গমূল: c = √(${formatNumberBn(cSq)}) = ${formatNumberBn(c)} একক"
                    ),
                    diagramNotesBn = "সমকোণী ত্রিভুজ যার বাহুত্রয় (${formatNumberBn(a)}, ${formatNumberBn(b)}, ${formatNumberBn(c)})"
                )
            }
        ),
        VisualFormulaModel(
            id = "vis_rect_area",
            libraryFormulaId = "lib_geom_05",
            titleBn = "আয়তক্ষেত্রের ক্ষেত্রফল ও পরিসীমা",
            categoryBn = "জ্যামিতি (Geometry)",
            formulaExpression = "ক্ষেত্রফল = a × b, পরিসীমা = ২(a + b)",
            param1NameBn = "দৈর্ঘ্য (a)",
            param1Default = 8.0,
            param1Min = 1.0,
            param1Max = 25.0,
            param2NameBn = "প্রস্থ (b)",
            param2Default = 5.0,
            param2Min = 1.0,
            param2Max = 25.0,
            calculate = { a, b, _ ->
                val area = a * b
                val perimeter = 2 * (a + b)
                FormulaVisualResult(
                    outputValue = area,
                    outputLabelBn = "ক্ষেত্রফল (বর্গ একক)",
                    calculationStepsBn = listOf(
                        "দৈর্ঘ্য a = ${formatNumberBn(a)}, প্রস্থ b = ${formatNumberBn(b)}",
                        "ক্ষেত্রফল A = a × b = ${formatNumberBn(a)} × ${formatNumberBn(b)} = ${formatNumberBn(area)} বর্গ একক",
                        "পরিসীমা P = 2(a + b) = 2(${formatNumberBn(a + b)}) = ${formatNumberBn(perimeter)} একক"
                    ),
                    diagramNotesBn = "আয়তক্ষেত্র: দৈর্ঘ্য ${formatNumberBn(a)} ও প্রস্থ ${formatNumberBn(b)}"
                )
            }
        ),
        VisualFormulaModel(
            id = "vis_circle_prop",
            libraryFormulaId = "lib_geom_08",
            titleBn = "বৃত্তের ক্ষেত্রফল ও পরিধি",
            categoryBn = "জ্যামিতি (Geometry)",
            formulaExpression = "ক্ষেত্রফল = πr²,  পরিধি = 2πr",
            param1NameBn = "ব্যাসার্ধ (r)",
            param1Default = 7.0,
            param1Min = 1.0,
            param1Max = 30.0,
            calculate = { r, _, _ ->
                val area = PI * r * r
                val circ = 2 * PI * r
                FormulaVisualResult(
                    outputValue = area,
                    outputLabelBn = "ক্ষেত্রফল (বর্গ একক)",
                    calculationStepsBn = listOf(
                        "বৃত্তের ব্যাসার্ধ r = ${formatNumberBn(r)} একক",
                        "পরিধি C = 2 × π × ${formatNumberBn(r)} ≈ ${formatNumberBn(circ)} একক",
                        "ক্ষেত্রফল A = π × r² = 3.14159 × ${formatNumberBn(r * r)} ≈ ${formatNumberBn(area)} বর্গ একক"
                    ),
                    diagramNotesBn = "বৃত্ত যার কেন্দ্র থেকে ব্যাসার্ধ r = ${formatNumberBn(r)}"
                )
            }
        ),
        VisualFormulaModel(
            id = "vis_cube_vol",
            libraryFormulaId = "lib_geom_10",
            titleBn = "ঘনকের আয়তন ও সমগ্রতলের ক্ষেত্রফল",
            categoryBn = "ঘনবস্তু (3D Shapes)",
            formulaExpression = "আয়তন V = a³,  সমগ্রতলের ক্ষেত্রফল S = 6a²",
            param1NameBn = "ধার বা বাহু (a)",
            param1Default = 4.0,
            param1Min = 1.0,
            param1Max = 15.0,
            calculate = { a, _, _ ->
                val vol = a * a * a
                val surf = 6 * a * a
                FormulaVisualResult(
                    outputValue = vol,
                    outputLabelBn = "আয়তন (ঘন একক)",
                    calculationStepsBn = listOf(
                        "ঘনকের প্রতিটি ধারের দৈর্ঘ্য a = ${formatNumberBn(a)} একক",
                        "আয়তন V = a³ = ${formatNumberBn(a)} × ${formatNumberBn(a)} × ${formatNumberBn(a)} = ${formatNumberBn(vol)} ঘন একক",
                        "সমগ্রতলের ক্ষেত্রফল S = 6a² = 6 × ${formatNumberBn(a * a)} = ${formatNumberBn(surf)} বর্গ একক"
                    ),
                    diagramNotesBn = "ত্রিমাত্রিক ঘনক: ৩টি অক্ষেই দৈর্ঘ্য ${formatNumberBn(a)}"
                )
            }
        ),
        VisualFormulaModel(
            id = "vis_simple_interest",
            libraryFormulaId = null,
            titleBn = "সরল সুদের সূত্র (I = Pnr / ১০০)",
            categoryBn = "পাটিগণিত (Arithmetic)",
            formulaExpression = "I = (P × n × r) / 100",
            param1NameBn = "মূলধন P (টাকা)",
            param1Default = 1000.0,
            param1Min = 100.0,
            param1Max = 10000.0,
            param2NameBn = "সময় n (বছর)",
            param2Default = 3.0,
            param2Min = 1.0,
            param2Max = 10.0,
            param3NameBn = "সুদের হার r (%)",
            param3Default = 7.5,
            param3Min = 1.0,
            param3Max = 25.0,
            calculate = { p, n, r ->
                val interest = (p * n * r) / 100.0
                val totalAmount = p + interest
                FormulaVisualResult(
                    outputValue = interest,
                    outputLabelBn = "মোট সুদ I (টাকা)",
                    calculationStepsBn = listOf(
                        "মূলধন P = ${formatNumberBn(p)} টাকা, সময় n = ${formatNumberBn(n)} বছর, হার r = ${formatNumberBn(r)}%",
                        "সূত্র: I = (P × n × r) / 100 = (${formatNumberBn(p)} × ${formatNumberBn(n)} × ${formatNumberBn(r)}) / 100",
                        "মোট সুদ: I = ${formatNumberBn(interest)} টাকা",
                        "সুদ-আসল: A = P + I = ${formatNumberBn(p)} + ${formatNumberBn(interest)} = ${formatNumberBn(totalAmount)} টাকা"
                    ),
                    diagramNotesBn = "মূলধন বনাম সুদের বার গ্রাফ"
                )
            }
        )
    )
}
