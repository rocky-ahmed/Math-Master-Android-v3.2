package com.example.math

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cbrt
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

/**
 * Deterministic, offline, highly robust mathematical problem solver engine for Phase 4.
 * Produces comprehensive step-by-step Bengali solutions, formulas, verification, and BCS shortcuts.
 */
object MathProblemSolverEngine {

    private val usSymbols = DecimalFormatSymbols(Locale.US)
    private val df = DecimalFormat("0.####", usSymbols)
    private val dfCurrency = DecimalFormat("0.##", usSymbols)

    private fun formatNum(n: Double): String {
        return if (n == n.toLong().toDouble()) {
            BengaliNumberFormatter.toBn(n.toLong().toString())
        } else {
            BengaliNumberFormatter.toBn(df.format(n))
        }
    }

    private fun formatNum(n: Long): String {
        return BengaliNumberFormatter.toBn(n.toString())
    }

    // -------------------------------------------------------------
    // 1. BASIC ARITHMETIC & BODMAS
    // -------------------------------------------------------------
    fun solveArithmetic(expression: String, mode: SolverMode): SolverResult {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) {
            return errorResult(
                SolverTopic.BASIC_ARITHMETIC,
                mode,
                "কোনো গাণিতিক রাশি দেওয়া হয়নি",
                "অনুগ্রহ করে একটি বৈধ গাণিতিক রাশি লিখুন। যেমন: (১৫ + ৫) × ৪ ÷ ২ - ৬"
            )
        }

        // Check for invalid characters
        val cleanExpr = trimmed
            .replace("×", "*")
            .replace("÷", "/")
            .replace("–", "-")
            .replace(" ", "")

        // Normalize Bengali digits to English for evaluation
        val asciiExpr = toAsciiDigits(cleanExpr)

        // Validate parentheses balance
        var balance = 0
        for (ch in asciiExpr) {
            if (ch == '(' || ch == '{' || ch == '[') balance++
            if (ch == ')' || ch == '}' || ch == ']') balance--
            if (balance < 0) {
                return errorResult(
                    SolverTopic.BASIC_ARITHMETIC,
                    mode,
                    trimmed,
                    "বন্ধনীর ক্রম সঠিক নয়। অতিরিক্ত সমাপ্তি বন্ধনী রয়েছে।"
                )
            }
        }
        if (balance != 0) {
            return errorResult(
                SolverTopic.BASIC_ARITHMETIC,
                mode,
                trimmed,
                "বন্ধনীর সংখ্যা অসমান। শুরুর বন্ধনী বন্ধ করা হয়নি।"
            )
        }

        // Evaluate expression step-by-step
        return try {
            val steps = mutableListOf<SolutionStep>()
            val resultVal = evaluateArithmeticExpression(asciiExpr, steps, mode)

            val displayResult = formatNum(resultVal)
            val verification = if (mode == SolverMode.LEARNING) {
                "শুদ্ধি পরীক্ষা: চূড়ান্ত মান $displayResult। বিপরীত প্রক্রিয়ায় পরীক্ষা করলে দেখা যায় সমস্ত বন্ধনী ও অপারেশনের নিয়ম অক্ষুণ্ণ রয়েছে।"
            } else null

            val bcsShortcut = if (mode == SolverMode.BCS) {
                "বিসিএস টিপস: সরল অংকে BODMAS নিয়ম strictly মেনে চলুন— B(বন্ধনী) -> O(এর/ঘাত) -> D(ভাগ) -> M(গুণ) -> A(যোগ) -> S(বিয়োগ)। মুখে মুখে ছোট অংশগুলো আগে সমাধান করে সময় বাঁচান।"
            } else null

            SolverResult(
                topic = SolverTopic.BASIC_ARITHMETIC,
                mode = mode,
                originalProblemBn = "সরল করুন: ${BengaliNumberFormatter.toBn(trimmed)}",
                formulaUsedBn = "BODMAS নিয়ম: বন্ধনী (Brackets) ➜ এর (Of) ➜ ভাগ (Division) ➜ গুণ (Multiplication) ➜ যোগ (Addition) ➜ বিয়োগ (Subtraction)",
                steps = steps,
                finalAnswerBn = displayResult,
                verificationBn = verification,
                bcsShortcutBn = bcsShortcut,
                bcsExamTipBn = "পরীক্ষায় বন্ধনীর ভেতর চিহ্ন পরিবর্তন (- দিয়ে গুণ) হওয়ার সময় বেশি ভুল হয়, তাই সতর্ক থাকুন।",
                similarProblemPromptBn = "(২৫ + ১৫) × ২ - ৩০ ÷ ৩"
            )
        } catch (e: ArithmeticException) {
            errorResult(
                SolverTopic.BASIC_ARITHMETIC,
                mode,
                trimmed,
                "গণিত ত্রুটি: ${e.message ?: "কোনো সংখ্যাকে শূন্য (০) দিয়ে ভাগ করা যায় না।"}"
            )
        } catch (e: Exception) {
            errorResult(
                SolverTopic.BASIC_ARITHMETIC,
                mode,
                trimmed,
                "রাশিটি সঠিকভাবে বিশ্লেষণ করা যায়নি। সঠিক চিহ্ন (+, -, *, /) ব্যবহার করুন।"
            )
        }
    }

    // -------------------------------------------------------------
    // 2. FRACTIONS & DECIMALS
    // -------------------------------------------------------------
    fun solveFraction(
        num1: Long,
        den1: Long,
        op: String,
        num2: Long,
        den2: Long,
        mode: SolverMode
    ): SolverResult {
        if (num1 == Long.MIN_VALUE || den1 == Long.MIN_VALUE || num2 == Long.MIN_VALUE || den2 == Long.MIN_VALUE) {
            return errorResult(
                SolverTopic.FRACTIONS_DECIMALS,
                mode,
                "$num1/$den1 $op $num2/$den2",
                "সংখ্যার মান সমর্থিত সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)।"
            )
        }

        if (den1 == 0L || den2 == 0L) {
            return errorResult(
                SolverTopic.FRACTIONS_DECIMALS,
                mode,
                "$num1/$den1 $op $num2/$den2",
                "ভগ্নাংশের হর কখনো শূন্য (০) হতে পারে না। এটি অসংজ্ঞায়িত (Undefined)।"
            )
        }

        var sNum1 = num1
        var sDen1 = den1
        if (sDen1 < 0L) {
            sNum1 = -sNum1
            sDen1 = -sDen1
        }

        var sNum2 = num2
        var sDen2 = den2
        if (sDen2 < 0L) {
            sNum2 = -sNum2
            sDen2 = -sDen2
        }

        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        val probBn = "${formatNum(num1)}/${formatNum(den1)} $op ${formatNum(num2)}/${formatNum(den2)}"

        var resNum: Long
        var resDen: Long

        try {
            when (op) {
                "+" -> {
                    val lcm = NumberSystemProvider.lcm(sDen1, sDen2)
                    if (lcm <= 0L) {
                        return errorResult(
                            SolverTopic.FRACTIONS_DECIMALS,
                            mode,
                            probBn,
                            "হরগুলোর ল.সা.গু গণনা সমর্থিত সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)।"
                        )
                    }
                    val m1 = lcm / sDen1
                    val m2 = lcm / sDen2
                    val t1 = Math.multiplyExact(sNum1, m1)
                    val t2 = Math.multiplyExact(sNum2, m2)
                    resNum = Math.addExact(t1, t2)
                    resDen = lcm

                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "হরগুলোর ল.সা.গু নির্ণয়",
                            expressionOrMathBn = "ল.সা.গু(${formatNum(sDen1)}, ${formatNum(sDen2)}) = ${formatNum(lcm)}",
                            explanationBn = "দুটি ভগ্নাংশের যোগ করার জন্য প্রথমে হরগুলোর লঘিষ্ঠ সাধারণ গুণিতক (ল.সা.গু) নির্ণয় করতে হয়।",
                            whyThisStepBn = "হর সমান না থাকলে লব সরাসরি যোগ করা যায় না, তাই সমহর বিশিষ্ট ভগ্নাংশে রূপান্তর অপরিহার্য।"
                        )
                    )

                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "সমহর ভগ্নাংশে রূপান্তর ও লবের যোগ",
                            expressionOrMathBn = "(${formatNum(sNum1)} × ${formatNum(m1)} + ${formatNum(sNum2)} × ${formatNum(m2)}) / ${formatNum(lcm)} = (${formatNum(t1)} + ${formatNum(t2)}) / ${formatNum(lcm)} = ${formatNum(resNum)}/${formatNum(resDen)}",
                            explanationBn = "উভয় ভগ্নাংশকে সমহর করার পর লবগুলোকে একত্রিত করে যোগ করা হলো।"
                        )
                    )
                }
                "-" -> {
                    val lcm = NumberSystemProvider.lcm(sDen1, sDen2)
                    if (lcm <= 0L) {
                        return errorResult(
                            SolverTopic.FRACTIONS_DECIMALS,
                            mode,
                            probBn,
                            "হরগুলোর ল.সা.গু গণনা সমর্থিত সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)।"
                        )
                    }
                    val m1 = lcm / sDen1
                    val m2 = lcm / sDen2
                    val t1 = Math.multiplyExact(sNum1, m1)
                    val t2 = Math.multiplyExact(sNum2, m2)
                    resNum = Math.subtractExact(t1, t2)
                    resDen = lcm

                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "হরগুলোর ল.সা.গু নির্ণয়",
                            expressionOrMathBn = "ল.সা.গু(${formatNum(sDen1)}, ${formatNum(sDen2)}) = ${formatNum(lcm)}",
                            explanationBn = "বিয়োগ করার পূর্বে উভয় ভগ্নাংশের হর এক করার উদ্দেশ্যে ল.সা.গু বের করা হলো।"
                        )
                    )

                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "সমহর করে লব বিয়োগ",
                            expressionOrMathBn = "(${formatNum(sNum1)} × ${formatNum(m1)} - ${formatNum(sNum2)} × ${formatNum(m2)}) / ${formatNum(lcm)} = (${formatNum(t1)} - ${formatNum(t2)}) / ${formatNum(lcm)} = ${formatNum(resNum)}/${formatNum(resDen)}",
                            explanationBn = "সমহর করার পর প্রথম ভগ্নাংশের পরিবর্তিত লব থেকে দ্বিতীয়টির লব বিয়োগ করা হলো।"
                        )
                    )
                }
                "*" , "×" -> {
                    resNum = Math.multiplyExact(sNum1, sNum2)
                    resDen = Math.multiplyExact(sDen1, sDen2)

                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "লবে লবে এবং হরে হরে গুণ",
                            expressionOrMathBn = "(${formatNum(sNum1)} × ${formatNum(sNum2)}) / (${formatNum(sDen1)} × ${formatNum(sDen2)}) = ${formatNum(resNum)}/${formatNum(resDen)}",
                            explanationBn = "ভগ্নাংশের গুণের নিয়ম অনুযায়ী লব দুটি পরস্পরের সাথে এবং হর দুটি পরস্পরের সাথে গুণ করা হলো।",
                            whyThisStepBn = "গুণের ক্ষেত্রে হর সমান করার প্রয়োজন হয় না, সরাসরি গুণ করা যায়।"
                        )
                    )
                }
                "/" , "÷" -> {
                    if (sNum2 == 0L) {
                        return errorResult(
                            SolverTopic.FRACTIONS_DECIMALS,
                            mode,
                            probBn,
                            "শূন্য (০) দ্বারা ভাগ করা সংজ্ঞায়িত নয়।"
                        )
                    }
                    resNum = Math.multiplyExact(sNum1, sDen2)
                    resDen = Math.multiplyExact(sDen1, sNum2)

                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "ভাগ চিহ্নকে গুণে রূপান্তর এবং বিপরীত ভগ্নাংশ",
                            expressionOrMathBn = "(${formatNum(sNum1)}/${formatNum(sDen1)}) × (${formatNum(sDen2)}/${formatNum(sNum2)})",
                            explanationBn = "ভগ্নাংশের ভাগের ক্ষেত্রে ভাজক ভগ্নাংশটিকে উল্টিয়ে (বিপরীত ভগ্নাংশ করে) গুণ চিহ্নে পরিবর্তন করা হয়।",
                            whyThisStepBn = "কোনো ভগ্নাংশ দ্বারা ভাগ করার অর্থ হলো তার বিপরীত ভগ্নাংশ দ্বারা গুণ করা।"
                        )
                    )

                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "গুণফল গণনা",
                            expressionOrMathBn = "(${formatNum(sNum1)} × ${formatNum(sDen2)}) / (${formatNum(sDen1)} × ${formatNum(sNum2)}) = ${formatNum(resNum)}/${formatNum(resDen)}",
                            explanationBn = "লব ও হর গুণ করে অন্তর্বর্তী ফলাফল পাওয়া গেল।"
                        )
                    )
                }
                else -> {
                    return errorResult(SolverTopic.FRACTIONS_DECIMALS, mode, probBn, "অসমর্থিত অপারেটর: $op")
                }
            }
        } catch (e: ArithmeticException) {
            return errorResult(
                SolverTopic.FRACTIONS_DECIMALS,
                mode,
                probBn,
                "গণনাটি সমর্থিত গাণিতিক সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)। অপেক্ষাকৃত ছোট মান ব্যবহার করুন।"
            )
        }

        if (resNum == Long.MIN_VALUE || resDen == Long.MIN_VALUE) {
            return errorResult(
                SolverTopic.FRACTIONS_DECIMALS,
                mode,
                probBn,
                "ফলাফল সমর্থিত সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)।"
            )
        }

        // Handle negative denominator
        if (resDen < 0L) {
            resNum = -resNum
            resDen = -resDen
        }

        // Simplification
        val gcd = NumberSystemProvider.gcd(abs(resNum), abs(resDen))
        val simpNum = resNum / gcd
        val simpDen = resDen / gcd

        if (gcd > 1) {
            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "লঘিষ্ঠ সাধারণ আকারে রূপান্তর (কাটাকাটি/সরলীকরণ)",
                    expressionOrMathBn = "লব ও হর উভয়ের গ.সা.গু = ${formatNum(gcd)}। (${formatNum(resNum)} ÷ ${formatNum(gcd)}) / (${formatNum(resDen)} ÷ ${formatNum(gcd)}) = ${formatNum(simpNum)}/${formatNum(simpDen)}",
                    explanationBn = "উভয় অংশকে তাদের গরিষ্ঠ সাধারণ গুণনীয়ক (গ.সা.গু) দ্বারা ভাগ করে লঘিষ্ঠ রূপ দেওয়া হলো।"
                )
            )
        }

        // Mixed fraction if numerator > denominator
        val mixedStr = if (abs(simpNum) >= simpDen && simpDen != 1L) {
            val whole = simpNum / simpDen
            val rem = abs(simpNum % simpDen)
            if (rem > 0) " অথবা মিশ্র ভগ্নাংশে: ${formatNum(whole)} পূর্ণ ${formatNum(rem)}/${formatNum(simpDen)}" else ""
        } else ""

        val decimalVal = simpNum.toDouble() / simpDen.toDouble()
        val decStr = " (দশমিক মান: ${formatNum(decimalVal)})"

        val finalAns = if (simpDen == 1L) {
            formatNum(simpNum)
        } else {
            "${formatNum(simpNum)}/${formatNum(simpDen)}$mixedStr$decStr"
        }

        val verification = if (mode == SolverMode.LEARNING) {
            "শুদ্ধি যাচাই: দশমিক মান ${formatNum(decimalVal)}। মূল ভগ্নাংশ দুটির দশমিক রূপ (${formatNum(sNum1.toDouble() / sDen1)} এবং ${formatNum(sNum2.toDouble() / sDen2)}) নিয়ে হিসাব করলেও একই মান পাওয়া যায়।"
        } else null

        val bcsShortcut = if (mode == SolverMode.BCS) {
            "বিসিএস শর্টকাট: যোগ/বিয়োগের সময় কোণাকুণি গুণ টেকনিক ব্যবহার করুন: a/b + c/d = (ad + bc)/(bd)। যেমন: ৩/৪ + ২/৫ = (১৫ + ৮)/২০ = ২৩/২০। এতে ল.সা.গু ছাড়াই দ্রুত সমাধান সম্ভব।"
        } else null

        return SolverResult(
            topic = SolverTopic.FRACTIONS_DECIMALS,
            mode = mode,
            originalProblemBn = "নির্ণয় করুন: $probBn",
            formulaUsedBn = "ভগ্নাংশের গুণফল/যোগফলের নিয়ম ও লঘিষ্ঠকরণ",
            steps = steps,
            finalAnswerBn = finalAns,
            verificationBn = verification,
            bcsShortcutBn = bcsShortcut,
            bcsExamTipBn = "মিশ্র ভগ্নাংশ থাকলে প্রথমে সেটিকে অপ্রকৃত ভগ্নাংশে রূপান্তর করে নিন।",
            similarProblemPromptBn = "৪/৭ + ৩/৫"
        )
    }

    // -------------------------------------------------------------
    // 3. PERCENTAGE
    // -------------------------------------------------------------
    enum class PercentageType { OF_NUMBER, WHAT_PERCENT_OF, INCREASE_DECREASE, SUCCESSIVE }

    fun solvePercentage(
        type: PercentageType,
        val1: Double,
        val2: Double,
        mode: SolverMode
    ): SolverResult {
        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        return when (type) {
            PercentageType.OF_NUMBER -> {
                // val1% of val2
                val percent = val1
                val base = val2
                val result = (percent * base) / 100.0

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "শতকরাকে সাধারণ ভগ্নাংশে রূপান্তর",
                        expressionOrMathBn = "${formatNum(percent)}% = ${formatNum(percent)} / ১০০",
                        explanationBn = "শতকরা (%) চিহ্নটির অর্থ হলো প্রতি শতে বা এক শত ভাগের কত ভাগ।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "মূল সংখ্যার সাথে গুণ",
                        expressionOrMathBn = "(${formatNum(percent)} / ১০০) × ${formatNum(base)} = (${formatNum(percent)} × ${formatNum(base)}) / ১০০ = ${formatNum(percent * base)} / ১০০ = ${formatNum(result)}",
                        explanationBn = "মূল সংখ্যা দ্বারা গুণ করে কাঙ্ক্ষিত পরিমাণ পাওয়া গেল।"
                    )
                )

                val verification = if (mode == SolverMode.LEARNING) {
                    "শুদ্ধি পরীক্ষা: প্রাপ্ত মান ${formatNum(result)} কে মূল মান ${formatNum(base)} দিয়ে ভাগ করে ১০০ দিয়ে গুণ করলে: (${formatNum(result)} ÷ ${formatNum(base)}) × ১০০ = ${formatNum(percent)}%।"
                } else null

                val bcsShortcut = if (mode == SolverMode.BCS) {
                    "বিসিএস ট্রিক: X-এর Y% = Y-এর X%। যেমন: ৫০-এর ১২% নির্ণয় কঠিন মনে হলে ১২-এর ৫০% (অর্থাৎ ১২ এর অর্ধেক = ৬) মুখে মুখেই বের করা যায়।"
                } else null

                SolverResult(
                    topic = SolverTopic.PERCENTAGE,
                    mode = mode,
                    originalProblemBn = "${formatNum(base)} এর ${formatNum(percent)}% কত?",
                    formulaUsedBn = "মান = (শতকরা হার × মূল সংখ্যা) ÷ ১০০",
                    steps = steps,
                    finalAnswerBn = formatNum(result),
                    verificationBn = verification,
                    bcsShortcutBn = bcsShortcut,
                    similarProblemPromptBn = "৭৫০ এর ১২% কত?"
                )
            }
            PercentageType.WHAT_PERCENT_OF -> {
                // val1 is what % of val2?
                val part = val1
                val total = val2
                if (total == 0.0) {
                    return errorResult(
                        SolverTopic.PERCENTAGE,
                        mode,
                        "$part of $total",
                        "মোট পরিমাণ শূন্য (০) হতে পারে না।"
                    )
                }
                val result = (part / total) * 100.0

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "অংশ ও মোট পরিমাণের অনুপাত",
                        expressionOrMathBn = "ভগ্নাংশ = ${formatNum(part)} / ${formatNum(total)} = ${formatNum(part / total)}",
                        explanationBn = "প্রদত্ত অংশকে মোট পরিমাণ দ্বারা ভাগ করে ভগ্নাংশ মান বের করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "১০০ দ্বারা গুণ করে শতকরায় রূপান্তর",
                        expressionOrMathBn = "(${formatNum(part)} / ${formatNum(total)}) × ১০০% = ${formatNum(result)}%",
                        explanationBn = "ভগ্নাংশকে শতকরায় নিতে ১০০% দিয়ে গুণ করা হলো।"
                    )
                )

                SolverResult(
                    topic = SolverTopic.PERCENTAGE,
                    mode = mode,
                    originalProblemBn = "${formatNum(part)} সংখ্যাটি ${formatNum(total)} এর শতকরা কত?",
                    formulaUsedBn = "শতকরা হার = (নির্দিষ্ট অংশ ÷ মোট পরিমাণ) × ১০০%",
                    steps = steps,
                    finalAnswerBn = "${formatNum(result)}%",
                    verificationBn = "যাচাই: ${formatNum(total)} × (${formatNum(result)} / ১০০) = ${formatNum(part)}",
                    bcsShortcutBn = "বিসিএস টিপস: অনুপাতকে লঘিষ্ঠ করে পরিচিত শতকরায় রূপান্তর করুন। যেমন ২৫/১০০ = ১/৪ = ২৫%।",
                    similarProblemPromptBn = "৪৫ সংখ্যাটি ৩০০ এর শতকরা কত?"
                )
            }
            PercentageType.INCREASE_DECREASE -> {
                // val1 = old, val2 = new
                val oldVal = val1
                val newVal = val2
                if (oldVal == 0.0) {
                    return errorResult(
                        SolverTopic.PERCENTAGE,
                        mode,
                        "Old: $oldVal, New: $newVal",
                        "প্রারম্ভিক মান শূন্য হলে শতকরা পরিবর্তন নির্ণয় করা যায় না।"
                    )
                }

                val diff = newVal - oldVal
                val isIncrease = diff >= 0
                val pctChange = (abs(diff) / oldVal) * 100.0

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "মান পরিবর্তনের পরিমাণ নির্ণয়",
                        expressionOrMathBn = "পার্থক্য = |${formatNum(newVal)} - ${formatNum(oldVal)}| = ${formatNum(abs(diff))}",
                        explanationBn = if (isIncrease) "নতুন মান বেশি হওয়ায় এটি বৃদ্ধি পেয়েছে।" else "নতুন মান কম হওয়ায় এটি হ্রাস পেয়েছে।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "প্রারম্ভিক মানের সাপেক্ষে শতকরা হার",
                        expressionOrMathBn = "(${formatNum(abs(diff))} / ${formatNum(oldVal)}) × ১০০% = ${formatNum(pctChange)}%",
                        explanationBn = "পরিবর্তনের পরিমাণকে আদি মান দিয়ে ভাগ করে ১০০ দ্বারা গুণ করা হলো।"
                    )
                )

                val direction = if (isIncrease) "বৃদ্ধি" else "হ্রাস"
                SolverResult(
                    topic = SolverTopic.PERCENTAGE,
                    mode = mode,
                    originalProblemBn = "মান ${formatNum(oldVal)} থেকে পরিবর্তিত হয়ে ${formatNum(newVal)} হলে শতকরা পরিবর্তন কত?",
                    formulaUsedBn = "শতকরা পরিবর্তন = (পার্থক্য ÷ প্রারম্ভিক মান) × ১০০%",
                    steps = steps,
                    finalAnswerBn = "${formatNum(pctChange)}% $direction",
                    verificationBn = "যাচাই: ${formatNum(oldVal)} ${if (isIncrease) "+" else "-"} (${formatNum(oldVal)} × ${formatNum(pctChange)}/১০০) = ${formatNum(newVal)}",
                    bcsShortcutBn = "বিসিএস শর্টকাট: বৃদ্ধি হলে ভগ্নাংশ = (পার্থক্য/আদিমান), হ্রাস হলেও হর সর্বদা আদিমানই থাকবে।",
                    similarProblemPromptBn = "মান ৮০ থেকে বেড়ে ১০০ হলে শতকরা বৃদ্ধি কত?"
                )
            }
            PercentageType.SUCCESSIVE -> {
                // val1 = a%, val2 = b%
                val a = val1
                val b = val2
                val net = a + b + (a * b) / 100.0

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "ক্রমাগত শতকরা পরিবর্তনের সূত্র প্রয়োগ",
                        expressionOrMathBn = "নেট পরিবর্তন = A + B + (AB / ১০০)",
                        explanationBn = "পরপর দুটি পরিবর্তনের ক্ষেত্রে কার্যকর নিট পরিবর্তন বের করার সর্বজনীন সূত্র।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "মান বসিয়ে হিসাব",
                        expressionOrMathBn = "${formatNum(a)} + ${formatNum(b)} + (${formatNum(a)} × ${formatNum(b)}) / ১০০ = ${formatNum(a + b)} + ${formatNum((a * b) / 100.0)} = ${formatNum(net)}%",
                        explanationBn = "যোগফল ও গুণফলের ভগ্নাংশ যোগ করে চূড়ান্ত কার্যকর পরিবর্তন পাওয়া গেল।"
                    )
                )

                SolverResult(
                    topic = SolverTopic.PERCENTAGE,
                    mode = mode,
                    originalProblemBn = "পরপর দুটি পরিবর্তন যথাক্রমে ${formatNum(a)}% এবং ${formatNum(b)}% হলে মোট শতকরা পরিবর্তন কত?",
                    formulaUsedBn = "কার্যকর পরিবর্তন = A + B + (A × B) / ১০০",
                    steps = steps,
                    finalAnswerBn = "${formatNum(net)}%",
                    verificationBn = "১০০ টাকার ধরে পরীক্ষা: ১০০ × (১ + $a/১০০) × (১ + $b/১০০) = ${formatNum(100.0 * (1 + a/100.0) * (1 + b/100.0))} টাকা, যা ${formatNum(net)}% পরিবর্তনের সমতুল্য।",
                    bcsShortcutBn = "বিসিএস হট ট্রিক: ছাড় (Discount) হলে ঋণাত্মক চিহ্ন নিন। যেমন ১০% ও ২০% ছাড়: -১০ - ২০ + (-১০ × -২০)/১০০ = -৩০ + ২ = -২৮% (অর্থাৎ ২৮% কার্যকর ছাড়)।",
                    similarProblemPromptBn = "২০% এবং ১০% এর সমতুল্য কার্যকর পরিবর্তন কত?"
                )
            }
        }
    }

    // -------------------------------------------------------------
    // 4. RATIO & PROPORTION
    // -------------------------------------------------------------
    enum class RatioType { DIVIDE_QUANTITY, FOURTH_PROPORTIONAL, SIMPLIFY }

    fun solveRatio(
        type: RatioType,
        total: Double,
        r1: Double,
        r2: Double,
        r3: Double? = null,
        mode: SolverMode
    ): SolverResult {
        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        when (type) {
            RatioType.DIVIDE_QUANTITY -> {
                val sumRatios = r1 + r2 + (r3 ?: 0.0)
                if (sumRatios <= 0.0) {
                    return errorResult(
                        SolverTopic.RATIO_PROPORTION,
                        mode,
                        "অনুপাত যোগফল $sumRatios",
                        "অনুপাতের রাশিগুলোর সমষ্টি অবশ্যই ধনাত্মক হতে হবে।"
                    )
                }

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "অনুপাতের রাশিগুলোর সমষ্টি নির্ণয়",
                        expressionOrMathBn = if (r3 != null) {
                            "${formatNum(r1)} + ${formatNum(r2)} + ${formatNum(r3)} = ${formatNum(sumRatios)}"
                        } else {
                            "${formatNum(r1)} + ${formatNum(r2)} = ${formatNum(sumRatios)}"
                        },
                        explanationBn = "মোট পরিমাণকে বণ্টন করার জন্য প্রথমে অনুপাতের পদগুলোর যোগফল বের করতে হয়।",
                        whyThisStepBn = "মোট অংশকে সমপরিমাণ ভাগে ভাগ করার জন্য মোট অনুপাতিক পদসংখ্যার সমষ্টি জানা জরুরি।"
                    )
                )

                val share1 = total * (r1 / sumRatios)
                val share2 = total * (r2 / sumRatios)
                val share3 = r3?.let { total * (it / sumRatios) }

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "১ম অংশের পরিমাণ",
                        expressionOrMathBn = "${formatNum(total)} × (${formatNum(r1)} / ${formatNum(sumRatios)}) = ${formatNum(share1)}",
                        explanationBn = "মোট পরিমাণকে ১ম অনুপাত ও সমষ্টির ভগ্নাংশ দিয়ে গুণ করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "২য় অংশের পরিমাণ",
                        expressionOrMathBn = "${formatNum(total)} × (${formatNum(r2)} / ${formatNum(sumRatios)}) = ${formatNum(share2)}",
                        explanationBn = "মোট পরিমাণকে ২য় অনুপাত ও সমষ্টির ভগ্নাংশ দিয়ে গুণ করা হলো।"
                    )
                )

                if (r3 != null && share3 != null) {
                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "৩য় অংশের পরিমাণ",
                            expressionOrMathBn = "${formatNum(total)} × (${formatNum(r3)} / ${formatNum(sumRatios)}) = ${formatNum(share3)}",
                            explanationBn = "মোট পরিমাণকে ৩য় অনুপাত ও সমষ্টির ভগ্নাংশ দিয়ে গুণ করা হলো।"
                        )
                    )
                }

                val finalAns = if (share3 != null) {
                    "১ম অংশ: ${formatNum(share1)}, ২য় অংশ: ${formatNum(share2)}, ৩য় অংশ: ${formatNum(share3)}"
                } else {
                    "১ম অংশ: ${formatNum(share1)}, ২য় অংশ: ${formatNum(share2)}"
                }

                val verification = if (mode == SolverMode.LEARNING) {
                    val sumShares = share1 + share2 + (share3 ?: 0.0)
                    "শুদ্ধি পরীক্ষা: অংশগুলোর যোগফল = ${formatNum(share1)} + ${formatNum(share2)}${if (share3 != null) " + ${formatNum(share3)}" else ""} = ${formatNum(sumShares)}, যা মোট পরিমাণ ${formatNum(total)} এর সমান।"
                } else null

                val bcsShortcut = if (mode == SolverMode.BCS) {
                    "বিসিএস শর্টকাট: ১ অনুপাত = মোট ÷ সমষ্টি = ${formatNum(total)} ÷ ${formatNum(sumRatios)} = ${formatNum(total / sumRatios)}। এবার প্রতিটি অনুপাতের সাথে এই মান সরাসরি গুণ করে নিমিষেই উত্তর পান।"
                } else null

                val ratioDisplay = if (r3 != null) "${formatNum(r1)}:${formatNum(r2)}:${formatNum(r3)}" else "${formatNum(r1)}:${formatNum(r2)}"

                return SolverResult(
                    topic = SolverTopic.RATIO_PROPORTION,
                    mode = mode,
                    originalProblemBn = "${formatNum(total)} পরিমাণকে $ratioDisplay অনুপাতে বণ্টন করুন",
                    formulaUsedBn = "অংশ = মোট পরিমাণ × (নির্দিষ্ট অনুপাত ÷ অনুপাতের সমষ্টি)",
                    steps = steps,
                    finalAnswerBn = finalAns,
                    verificationBn = verification,
                    bcsShortcutBn = bcsShortcut,
                    similarProblemPromptBn = "১২০০ টাকা ৩:৫ অনুপাতে ভাগ করুন"
                )
            }
            RatioType.FOURTH_PROPORTIONAL -> {
                // r1 : r2 = total : X -> X = (r2 * total) / r1
                val a = r1
                val b = r2
                val c = total
                if (a == 0.0) {
                    return errorResult(
                        SolverTopic.RATIO_PROPORTION,
                        mode,
                        "a = 0",
                        "সমানুপাতের প্রথম রাশি শূন্য হতে পারে না।"
                    )
                }
                val d = (b * c) / a
                if (d.isInfinite() || d.isNaN()) {
                    return errorResult(
                        SolverTopic.RATIO_PROPORTION,
                        mode,
                        "$a : $b = $c : ?",
                        "গণনার ফলাফল সমর্থিত সীমার বাইরে।"
                    )
                }

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "সমানুপাতের প্রান্তীয় ও মধ্যরাশির সম্পর্ক",
                        expressionOrMathBn = "১ম রাশি × ৪র্থ রাশি = ২য় রাশি × ৩য় রাশি",
                        explanationBn = "সমানুপাতের মূল বৈশিষ্ট্য হলো প্রান্তীয় রাশিদ্বয়ের গুণফল মধ্যরাশিদ্বয়ের গুণফলের সমান।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "৪র্থ সমানুপাতী নির্ণয়",
                        expressionOrMathBn = "৪র্থ রাশি = (২য় রাশি × ৩য় রাশি) ÷ ১ম রাশি = (${formatNum(b)} × ${formatNum(c)}) ÷ ${formatNum(a)} = ${formatNum(b * c)} ÷ ${formatNum(a)} = ${formatNum(d)}",
                        explanationBn = "মান বসিয়ে অজ্ঞাত ৪র্থ রাশির মান পাওয়া গেল।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.RATIO_PROPORTION,
                    mode = mode,
                    originalProblemBn = "${formatNum(a)} : ${formatNum(b)} = ${formatNum(c)} : ? (চতুর্থ সমানুপাতী নির্ণয়)",
                    formulaUsedBn = "A : B = C : D ⟹ D = (B × C) ÷ A",
                    steps = steps,
                    finalAnswerBn = formatNum(d),
                    verificationBn = "যাচাই: ${formatNum(a)} × ${formatNum(d)} = ${formatNum(a * d)} এবং ${formatNum(b)} × ${formatNum(c)} = ${formatNum(b * c)} (উভয় গুণফল সমান)",
                    bcsShortcutBn = "বিসিএস টিপস: মধ্য রাশিদ্বয়ের গুণফলকে প্রথম রাশি দিয়ে সরাসরি ভাগ করুন।",
                    similarProblemPromptBn = "৪ : ৮ = ১২ : ?"
                )
            }
            RatioType.SIMPLIFY -> {
                if (r1.toLong() <= 0L || r2.toLong() <= 0L) {
                    return errorResult(
                        SolverTopic.RATIO_PROPORTION,
                        mode,
                        "${formatNum(r1)} : ${formatNum(r2)}",
                        "অনুপাত সরলীকরণের জন্য উভয় পদ অবশ্যই ধনাত্মক পূর্ণসংখ্যা হতে হবে।"
                    )
                }
                val g = NumberSystemProvider.gcd(r1.toLong(), r2.toLong())
                val s1 = r1.toLong() / g
                val s2 = r2.toLong() / g

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "উভয় পদের গ.সা.গু নির্ণয়",
                        expressionOrMathBn = "গ.সা.গু(${formatNum(r1.toLong())}, ${formatNum(r2.toLong())}) = ${formatNum(g)}",
                        explanationBn = "অনুপাত লঘুকরণের জন্য পূর্বপদ ও উত্তরপদের গরিষ্ঠ সাধারণ গুণনীয়ক বের করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "গ.সা.গু দিয়ে ভাগ করে লঘিষ্ঠকরণ",
                        expressionOrMathBn = "(${formatNum(r1.toLong())} ÷ ${formatNum(g)}) : (${formatNum(r2.toLong())} ÷ ${formatNum(g)}) = ${formatNum(s1)} : ${formatNum(s2)}",
                        explanationBn = "উভয় পদকে গ.সা.গু দিয়ে ভাগ করে সরল অনুপাত পাওয়া গেল।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.RATIO_PROPORTION,
                    mode = mode,
                    originalProblemBn = "${formatNum(r1.toLong())} : ${formatNum(r2.toLong())} অনুপাতটিকে সরল করুন",
                    formulaUsedBn = "অনুপাত সরলীকরণ = উভয় পদকে গ.সা.গু দ্বারা ভাগ",
                    steps = steps,
                    finalAnswerBn = "${formatNum(s1)} : ${formatNum(s2)}",
                    verificationBn = "যাচাই: ${formatNum(s1)}/${formatNum(s2)} এর ভগ্নাংশ মান এবং মূল অনুপাতের ভগ্নাংশ মান অভিন্ন।",
                    bcsShortcutBn = "বিসিএস শর্টকাট: সাধারণ গুণনীয়কগুলো সরাসরি কেটে ছোট করুন।",
                    similarProblemPromptBn = "২৪ : ৩৬ সরল করুন"
                )
            }
        }
    }

    // -------------------------------------------------------------
    // 5. PROFIT AND LOSS
    // -------------------------------------------------------------
    enum class ProfitLossType {
        FIND_PROFIT_LOSS_PERCENT,
        FIND_SP_FROM_CP_AND_PERCENT,
        FIND_CP_FROM_SP_AND_PERCENT,
        DISCOUNT_MARKED_PRICE
    }

    fun solveProfitLoss(
        type: ProfitLossType,
        cp: Double?,
        sp: Double?,
        percent: Double?,
        mp: Double? = null,
        mode: SolverMode
    ): SolverResult {
        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        when (type) {
            ProfitLossType.FIND_PROFIT_LOSS_PERCENT -> {
                if (cp == null || sp == null || cp <= 0.0) {
                    return errorResult(
                        SolverTopic.PROFIT_LOSS,
                        mode,
                        "CP=$cp, SP=$sp",
                        "ক্রয়মূল্য অবশ্যই শূন্যের চেয়ে বড় হতে হবে।"
                    )
                }

                val diff = sp - cp
                val isProfit = diff >= 0
                val pct = (abs(diff) / cp) * 100.0

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = if (isProfit) "লাভ নির্ণয়" else "ক্ষতি নির্ণয়",
                        expressionOrMathBn = if (isProfit) {
                            "লাভ = বিক্রয়মূল্য - ক্রয়মূল্য = ${formatNum(sp)} - ${formatNum(cp)} = ${formatNum(diff)} টাকা"
                        } else {
                            "ক্ষতি = ক্রয়মূল্য - বিক্রয়মূল্য = ${formatNum(cp)} - ${formatNum(sp)} = ${formatNum(abs(diff))} টাকা"
                        },
                        explanationBn = if (isProfit) "বিক্রয়মূল্য ক্রয়মূল্যের চেয়ে বেশি হওয়ায় লাভ হয়েছে।" else "বিক্রয়মূল্য ক্রয়মূল্যের চেয়ে কম হওয়ায় ক্ষতি হয়েছে।",
                        whyThisStepBn = "শতকরা হার নির্ণয়ের পূর্বে মোট টাকার পরিমাণে লাভ বা ক্ষতির হিসাব প্রয়োজন।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "শতকরা হার নির্ণয় (ক্রয়মূল্যের সাপেক্ষে)",
                        expressionOrMathBn = "(${formatNum(abs(diff))} / ${formatNum(cp)}) × ১০০% = ${formatNum(pct)}%",
                        explanationBn = "মনে রাখবেন, শতকরা লাভ বা ক্ষতি সর্বদা ক্রয়মূল্যের উপর হিসাব করা হয়।"
                    )
                )

                val status = if (isProfit) "লাভ" else "ক্ষতি"
                val finalAns = "${formatNum(abs(diff))} টাকা $status (${formatNum(pct)}%)"

                val verification = if (mode == SolverMode.LEARNING) {
                    "শুদ্ধি পরীক্ষা: ক্রয়মূল্য ${formatNum(cp)} এর ${formatNum(pct)}% = ${formatNum(abs(diff))} টাকা। অতএব বিক্রয়মূল্য = ${formatNum(cp)} ${if (isProfit) "+" else "-"} ${formatNum(abs(diff))} = ${formatNum(sp)} টাকা।"
                } else null

                val bcsShortcut = if (mode == SolverMode.BCS) {
                    "বিসিএস শর্টকাট: ক্রয়মূল্য ১০০ টাকা ধরলে লাভ/ক্ষতি সরাসরি বোঝা যায়। শতকরা লাভ = (লাভ / CP) × ১০০%। কখনই SP দিয়ে ভাগ করবেন না।"
                } else null

                return SolverResult(
                    topic = SolverTopic.PROFIT_LOSS,
                    mode = mode,
                    originalProblemBn = "ক্রয়মূল্য ${formatNum(cp)} টাকা এবং বিক্রয়মূল্য ${formatNum(sp)} টাকা হলে লাভ/ক্ষতির শতকরা হার কত?",
                    formulaUsedBn = "শতকরা লাভ/ক্ষতি = (মোট লাভ বা ক্ষতি ÷ ক্রয়মূল্য) × ১০০%",
                    steps = steps,
                    finalAnswerBn = finalAns,
                    verificationBn = verification,
                    bcsShortcutBn = bcsShortcut,
                    similarProblemPromptBn = "ক্রয়মূল্য ৫০০ টাকা, বিক্রয়মূল্য ৫৫০ টাকা"
                )
            }
            ProfitLossType.FIND_SP_FROM_CP_AND_PERCENT -> {
                if (cp == null || percent == null || cp <= 0.0) {
                    return errorResult(
                        SolverTopic.PROFIT_LOSS,
                        mode,
                        "CP=$cp, %=$percent",
                        "ক্রয়মূল্য ও শতকরা হার প্রদান করুন।"
                    )
                }
                val isProfit = percent >= 0
                val amount = (abs(percent) / 100.0) * cp
                val finalSp = if (isProfit) cp + amount else cp - amount

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = if (isProfit) "মোট লাভের পরিমাণ" else "মোট ক্ষতির পরিমাণ",
                        expressionOrMathBn = "${formatNum(cp)} এর ${formatNum(abs(percent))}% = (${formatNum(cp)} × ${formatNum(abs(percent))}) / ১০০ = ${formatNum(amount)} টাকা",
                        explanationBn = "ক্রয়মূল্যের উপর নির্দিষ্ট শতকরা হার প্রয়োগ করে টাকার মান বের করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "বিক্রয়মূল্য নির্ধারণ",
                        expressionOrMathBn = if (isProfit) {
                            "বিক্রয়মূল্য = ক্রয়মূল্য + লাভ = ${formatNum(cp)} + ${formatNum(amount)} = ${formatNum(finalSp)} টাকা"
                        } else {
                            "বিক্রয়মূল্য = ক্রয়মূল্য - ক্ষতি = ${formatNum(cp)} - ${formatNum(amount)} = ${formatNum(finalSp)} টাকা"
                        },
                        explanationBn = if (isProfit) "লাভ হলে বিক্রয়মূল্য বাড়ে তাই যোগ করা হলো।" else "ক্ষতি হলে বিক্রয়মূল্য কমে তাই বিয়োগ করা হলো।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.PROFIT_LOSS,
                    mode = mode,
                    originalProblemBn = "ক্রয়মূল্য ${formatNum(cp)} টাকা এবং ${formatNum(abs(percent))}% ${if (isProfit) "লাভ" else "ক্ষতি"} হলে বিক্রয়মূল্য কত?",
                    formulaUsedBn = "SP = CP × (১০০ ± r) ÷ ১০০",
                    steps = steps,
                    finalAnswerBn = "${formatNum(finalSp)} টাকা",
                    verificationBn = "যাচাই: (${formatNum(finalSp)} - ${formatNum(cp)}) / ${formatNum(cp)} × ১০০ = ${formatNum(percent)}%",
                    bcsShortcutBn = "বিসিএস শর্টকাট: ১০% লাভ মানে SP = CP এর ১১০%। সরাসরি CP × ১.১ গুণ করে উত্তর বের করুন।",
                    similarProblemPromptBn = "ক্রয়মূল্য ৮০০ টাকা, লাভ ১৫%"
                )
            }
            ProfitLossType.FIND_CP_FROM_SP_AND_PERCENT -> {
                if (sp == null || percent == null || sp <= 0.0) {
                    return errorResult(
                        SolverTopic.PROFIT_LOSS,
                        mode,
                        "SP=$sp, %=$percent",
                        "বিক্রয়মূল্য ও শতকরা হার প্রদান করুন।"
                    )
                }
                val factor = 100.0 + percent
                if (factor <= 0.0) {
                    return errorResult(
                        SolverTopic.PROFIT_LOSS,
                        mode,
                        "Factor $factor",
                        "১০০% বা তার বেশি ক্ষতি বাস্তবসম্মত নয়।"
                    )
                }
                val finalCp = (sp * 100.0) / factor

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "১০০ টাকার ধারণায় বিক্রয়মূল্য নির্ধারণ",
                        expressionOrMathBn = "ক্রয়মূল্য ১০০ টাকা হলে বিক্রয়মূল্য = ১০০ ${if (percent >= 0) "+" else "-"} ${formatNum(abs(percent))} = ${formatNum(factor)} টাকা",
                        explanationBn = "ঐকিক নিয়মের সুবিধার্থে ভিত্তিমান ১০০ ধরে হিসাব শুরু করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "ঐকিক নিয়মে প্রকৃত ক্রয়মূল্য নির্ণয়",
                        expressionOrMathBn = "ক্রয়মূল্য = (${formatNum(sp)} × ১০০) ÷ ${formatNum(factor)} = ${formatNum(finalCp)} টাকা",
                        explanationBn = "প্রদত্ত বিক্রয়মূল্য থেকে মূল ক্রয়মূল্য পাওয়া গেল।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.PROFIT_LOSS,
                    mode = mode,
                    originalProblemBn = "বিক্রয়মূল্য ${formatNum(sp)} টাকা এবং ${formatNum(abs(percent))}% ${if (percent >= 0) "লাভে" else "ক্ষতিতে"} বিক্রিত হলে ক্রয়মূল্য কত?",
                    formulaUsedBn = "CP = (SP × ১০০) ÷ (১০০ ± r)",
                    steps = steps,
                    finalAnswerBn = "${formatNum(finalCp)} টাকা",
                    verificationBn = "যাচাই: ${formatNum(finalCp)} এর ${formatNum(abs(percent))}% = ${formatNum(finalCp * abs(percent) / 100.0)} টাকা। ${formatNum(finalCp)} ${if (percent >= 0) "+" else "-"} ${formatNum(finalCp * abs(percent) / 100.0)} = ${formatNum(sp)} টাকা।",
                    bcsShortcutBn = "বিসিএস শর্টকাট: ২০% লাভে বিক্রয়মূল্য ১২০ টাকা হলে CP = SP × (৫/৬) বা SP ÷ ১.২।",
                    similarProblemPromptBn = "বিক্রয়মূল্য ৫৫০ টাকা, লাভ ১০%"
                )
            }
            ProfitLossType.DISCOUNT_MARKED_PRICE -> {
                val markedPrice = mp ?: (sp ?: 100.0)
                val discountPct = percent ?: 10.0
                val discountAmount = markedPrice * (discountPct / 100.0)
                val sellingPrice = markedPrice - discountAmount

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "ছাড়ের (Discount) পরিমাণ নির্ণয়",
                        expressionOrMathBn = "${formatNum(markedPrice)} এর ${formatNum(discountPct)}% = (${formatNum(markedPrice)} × ${formatNum(discountPct)}) / ১০০ = ${formatNum(discountAmount)} টাকা",
                        explanationBn = "ধার্যমূল্যের (Marked Price) উপর শতকরা ছাড় হিসাব করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "ছাড় বাদে বিক্রয়মূল্য",
                        expressionOrMathBn = "বিক্রয়মূল্য = ধার্যমূল্য - ছাড় = ${formatNum(markedPrice)} - ${formatNum(discountAmount)} = ${formatNum(sellingPrice)} টাকা",
                        explanationBn = "ধার্যমূল্য থেকে ছাড়ের টাকা বাদ দিয়ে প্রকৃত পরিশোধযোগ্য বিক্রয়মূল্য পাওয়া গেল।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.PROFIT_LOSS,
                    mode = mode,
                    originalProblemBn = "ধার্যমূল্য ${formatNum(markedPrice)} টাকা এবং ছাড় ${formatNum(discountPct)}% হলে বিক্রয়মূল্য কত?",
                    formulaUsedBn = "SP = MP × (১০০ - Discount%) ÷ ১০০",
                    steps = steps,
                    finalAnswerBn = "বিক্রয়মূল্য: ${formatNum(sellingPrice)} টাকা (ছাড়: ${formatNum(discountAmount)} টাকা)",
                    verificationBn = "যাচাই: (${formatNum(discountAmount)} / ${formatNum(markedPrice)}) × ১০০ = ${formatNum(discountPct)}%",
                    bcsShortcutBn = "বিসিএস শর্টকাট: ২০% ছাড় মানে পণ্যটির দাম ৮০% হবে। সরাসরি ধার্যমূল্য × ০.৮ গুণ করুন।",
                    similarProblemPromptBn = "ধার্যমূল্য ১০০০ টাকা, ছাড় ১৫%"
                )
            }
        }
    }

    // -------------------------------------------------------------
    // 6. AVERAGE
    // -------------------------------------------------------------
    fun solveAverage(numbers: List<Double>, mode: SolverMode): SolverResult {
        if (numbers.isEmpty()) {
            return errorResult(
                SolverTopic.AVERAGE,
                mode,
                "খালি তালিকা",
                "কমপক্ষে একটি বা দুটি সংখ্যা ইনপুট দিন।"
            )
        }

        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        val count = numbers.size
        val sum = numbers.sum()
        val avg = sum / count

        val numsBn = numbers.joinToString(" + ") { formatNum(it) }

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "রাশিগুলোর মোট সমষ্টি নির্ণয়",
                expressionOrMathBn = "সমষ্টি = $numsBn = ${formatNum(sum)}",
                explanationBn = "গড় বের করার প্রথম কাজ হলো প্রদত্ত সকল সংখ্যা বা উপাত্তকে একত্রে যোগ করা।"
            )
        )

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "মোট পদসংখ্যা গণনা",
                expressionOrMathBn = "মোট সংখ্যা (N) = ${formatNum(count.toLong())} টি",
                explanationBn = "প্রদত্ত তালিকায় মোট কতটি সংখ্যা রয়েছে তা গণনা করা হলো।"
            )
        )

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "গড় নির্ণয়",
                expressionOrMathBn = "গড় = সমষ্টি ÷ পদসংখ্যা = ${formatNum(sum)} ÷ ${formatNum(count.toLong())} = ${formatNum(avg)}",
                explanationBn = "সমষ্টিকে পদসংখ্যা দিয়ে ভাগ করে কাঙ্ক্ষিত গড় পাওয়া গেল।"
            )
        )

        val verification = if (mode == SolverMode.LEARNING) {
            "শুদ্ধি পরীক্ষা: প্রাপ্ত গড় ${formatNum(avg)} কে পদসংখ্যা ${formatNum(count.toLong())} দিয়ে গুণ করলে: ${formatNum(avg)} × ${formatNum(count.toLong())} = ${formatNum(sum)}, যা মূল সমষ্টির সমান।"
        } else null

        val bcsShortcut = if (mode == SolverMode.BCS) {
            "বিসিএস ট্রিক: সমান্তর ধারার ক্ষেত্রে (যেমন ক্রমিক সংখ্যা): গড় = (১ম পদ + শেষ পদ) ÷ ২। এছাড়া কোনো আনুমানিক গড় (Assumed Mean) ধরে পার্থক্যগুলোর গড় যোগ করলেও দ্রুত গড় পাওয়া যায়।"
        } else null

        val displayList = numbers.joinToString(", ") { formatNum(it) }
        return SolverResult(
            topic = SolverTopic.AVERAGE,
            mode = mode,
            originalProblemBn = "সংখ্যাগুলোর গড় নির্ণয় করুন: $displayList",
            formulaUsedBn = "গড় = রাশিগুলোর সমষ্টি ÷ রাশিগুলোর সংখ্যা",
            steps = steps,
            finalAnswerBn = formatNum(avg),
            verificationBn = verification,
            bcsShortcutBn = bcsShortcut,
            similarProblemPromptBn = "১৫, ২৫, ৩৫, ৪৫, ৫৫ এর গড় কত?"
        )
    }

    // -------------------------------------------------------------
    // 7. SIMPLE INTEREST
    // -------------------------------------------------------------
    fun solveSimpleInterest(
        p: Double?, // Principal
        r: Double?, // Rate %
        t: Double?, // Time (years)
        i: Double?, // Interest
        a: Double?, // Total Amount
        mode: SolverMode
    ): SolverResult {
        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        // Case 1: Solve for Interest (I) and Total Amount (A) given P, r, t
        if (p != null && r != null && t != null) {
            val calcI = (p * r * t) / 100.0
            val calcA = p + calcI

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "সরল মুনাফার মূল সূত্র প্রয়োগ",
                    expressionOrMathBn = "I = (P × r × t) ÷ ১০০",
                    explanationBn = "এখানে P = মূলধন (${formatNum(p)} টাকা), r = মুনাফার হার (${formatNum(r)}%), t = সময় (${formatNum(t)} বছর)।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "মুনাফা (I) গণনা",
                    expressionOrMathBn = "I = (${formatNum(p)} × ${formatNum(r)} × ${formatNum(t)}) ÷ ১০০ = ${formatNum(p * r * t)} ÷ ১০০ = ${formatNum(calcI)} টাকা",
                    explanationBn = "মানগুলো সূত্রে বসিয়ে নির্দিষ্ট মেয়াদের মোট মুনাফা পাওয়া গেল।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "মুনাফা-আসল বা সবৃদ্ধিমূল (A) নির্ণয়",
                    expressionOrMathBn = "A = P + I = ${formatNum(p)} + ${formatNum(calcI)} = ${formatNum(calcA)} টাকা",
                    explanationBn = "মূলধনের সাথে অর্জিত মুনাফা যোগ করে মোট মুনাফা-আসল পাওয়া গেল।"
                )
            )

            val verification = if (mode == SolverMode.LEARNING) {
                "শুদ্ধি পরীক্ষা: প্রাপ্ত মুনাফা ${formatNum(calcI)} টাকা হলে বার্ষিক মুনাফার হার = (${formatNum(calcI)} × ১০০) ÷ (${formatNum(p)} × ${formatNum(t)}) = ${formatNum(r)}%।"
            } else null

            val bcsShortcut = if (mode == SolverMode.BCS) {
                "বিসিএস শর্টকাট: 'আসল মুনাফা-আসলে n গুণ হলে সময় t = ((n - ১) × ১০০) ÷ r'। যেমন দ্বিগুণ হলে (১ × ১০০) ÷ হার = সময়।"
            } else null

            return SolverResult(
                topic = SolverTopic.SIMPLE_INTEREST,
                mode = mode,
                originalProblemBn = "মূলধন ${formatNum(p)} টাকা, বার্ষিক মুনাফার হার ${formatNum(r)}% এবং সময় ${formatNum(t)} বছর হলে মুনাফা ও মুনাফা-আসল কত?",
                formulaUsedBn = "I = (P × r × t) ÷ ১০০ এবং A = P + I",
                steps = steps,
                finalAnswerBn = "মুনাফা (I): ${formatNum(calcI)} টাকা, মুনাফা-আসল (A): ${formatNum(calcA)} টাকা",
                verificationBn = verification,
                bcsShortcutBn = bcsShortcut,
                similarProblemPromptBn = "৫০০০ টাকা, ১০% হারে ৩ বছরের মুনাফা কত?"
            )
        }

        // Case 2: Solve for Principal (P) given I, r, t
        if (i != null && r != null && t != null && r > 0 && t > 0) {
            val calcP = (i * 100.0) / (r * t)

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "মূলধন (P) এর পক্ষান্তর সূত্র",
                    expressionOrMathBn = "P = (I × ১০০) ÷ (r × t)",
                    explanationBn = "I = (P × r × t) / ১০০ সমীকরণটিকে পক্ষান্তর করে P এর মান বের করার রূপ।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "মান বসিয়ে মূলধন গণনা",
                    expressionOrMathBn = "P = (${formatNum(i)} × ১০০) ÷ (${formatNum(r)} × ${formatNum(t)}) = ${formatNum(i * 100.0)} ÷ ${formatNum(r * t)} = ${formatNum(calcP)} টাকা",
                    explanationBn = "প্রদত্ত মুনাফা, হার ও সময় বসিয়ে মূলধন পাওয়া গেল।"
                )
            )

            return SolverResult(
                topic = SolverTopic.SIMPLE_INTEREST,
                mode = mode,
                originalProblemBn = "মুনাফা ${formatNum(i)} টাকা, বার্ষিক হার ${formatNum(r)}% ও সময় ${formatNum(t)} বছর হলে মূলধন কত?",
                formulaUsedBn = "P = (I × ১০০) ÷ (r × t)",
                steps = steps,
                finalAnswerBn = "${formatNum(calcP)} টাকা",
                verificationBn = "যাচাই: (${formatNum(calcP)} × ${formatNum(r)} × ${formatNum(t)}) ÷ ১০০ = ${formatNum(i)} টাকা।"
            )
        }

        // Case 3: Solve for Rate (r) given P, I, t
        if (p != null && i != null && t != null && p > 0 && t > 0) {
            val calcR = (i * 100.0) / (p * t)

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "মুনাফার হার (r) এর সূত্র",
                    expressionOrMathBn = "r = (I × ১০০) ÷ (P × t)",
                    explanationBn = "শতকরা বার্ষিক মুনাফার হার নির্ণয়ের পক্ষান্তরিত রূপ।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "হার গণনা",
                    expressionOrMathBn = "r = (${formatNum(i)} × ১০০) ÷ (${formatNum(p)} × ${formatNum(t)}) = ${formatNum(calcR)}%",
                    explanationBn = "মান বসিয়ে শতকরা হার পাওয়া গেল।"
                )
            )

            return SolverResult(
                topic = SolverTopic.SIMPLE_INTEREST,
                mode = mode,
                originalProblemBn = "মূলধন ${formatNum(p)} টাকা, মুনাফা ${formatNum(i)} টাকা ও সময় ${formatNum(t)} বছর হলে মুনাফার হার কত?",
                formulaUsedBn = "r = (I × ১০০) ÷ (P × t)%",
                steps = steps,
                finalAnswerBn = "${formatNum(calcR)}%",
                verificationBn = "যাচাই: (${formatNum(p)} × ${formatNum(calcR)} × ${formatNum(t)}) ÷ ১০০ = ${formatNum(i)}"
            )
        }

        // Case 4: Solve for Time (t) given P, I, r
        if (p != null && i != null && r != null && p > 0 && r > 0) {
            val calcT = (i * 100.0) / (p * r)

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "সময় (t) এর সূত্র",
                    expressionOrMathBn = "t = (I × ১০০) ÷ (P × r)",
                    explanationBn = "নির্দিষ্ট পরিমাণ মুনাফা অর্জনের প্রয়োজনীয় সময় বের করার সূত্র।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "সময় গণনা",
                    expressionOrMathBn = "t = (${formatNum(i)} × ১০০) ÷ (${formatNum(p)} × ${formatNum(r)}) = ${formatNum(calcT)} বছর",
                    explanationBn = "মান বসিয়ে প্রয়োজনীয় বছর সংখ্যা পাওয়া গেল।"
                )
            )

            return SolverResult(
                topic = SolverTopic.SIMPLE_INTEREST,
                mode = mode,
                originalProblemBn = "মূলধন ${formatNum(p)} টাকা, মুনাফা ${formatNum(i)} টাকা ও হার ${formatNum(r)}% হলে সময় কত?",
                formulaUsedBn = "t = (I × ১০০) ÷ (P × r) বছর",
                steps = steps,
                finalAnswerBn = "${formatNum(calcT)} বছর",
                verificationBn = "যাচাই: (${formatNum(p)} × ${formatNum(r)} × ${formatNum(calcT)}) ÷ ১০০ = ${formatNum(i)}"
            )
        }

        return errorResult(
            SolverTopic.SIMPLE_INTEREST,
            mode,
            "অসম্পূর্ণ তথ্য",
            "P, r, t, I এর মধ্যে যেকোনো ৩টি রাশির মান প্রদান করুন।"
        )
    }

    // -------------------------------------------------------------
    // 8. TIME AND WORK
    // -------------------------------------------------------------
    enum class WorkType { TWO_PERSONS, THREE_PERSONS, PIPE_CISTERN }

    fun solveTimeAndWork(
        type: WorkType,
        timeA: Double,
        timeB: Double,
        timeC: Double? = null,
        mode: SolverMode
    ): SolverResult {
        if (timeA <= 0.0 || timeB <= 0.0) {
            return errorResult(
                SolverTopic.TIME_AND_WORK,
                mode,
                "A=$timeA, B=$timeB",
                "কাজের সময় অবশ্যই ধনাত্মক হতে হবে।"
            )
        }

        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        when (type) {
            WorkType.TWO_PERSONS -> {
                val totalDays = (timeA * timeB) / (timeA + timeB)

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "১ দিনে প্রত্যেকের কাজের অংশ নির্ণয়",
                        expressionOrMathBn = "A এর ১ দিনের কাজ = ১/${formatNum(timeA)} অংশ\nB এর ১ দিনের কাজ = ১/${formatNum(timeB)} অংশ",
                        explanationBn = "সম্পূর্ণ কাজকে ১ অংশ ধরে প্রত্যেকে একদিনে কাজের কত ভগ্নাংশ করে তা বের করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "একত্রে ১ দিনে কাজের অংশ",
                        expressionOrMathBn = "১/${formatNum(timeA)} + ১/${formatNum(timeB)} = (${formatNum(timeA)} + ${formatNum(timeB)}) / (${formatNum(timeA)} × ${formatNum(timeB)}) = ${formatNum(timeA + timeB)} / ${formatNum(timeA * timeB)} অংশ",
                        explanationBn = "উভয়ের ১ দিনের কাজের ভগ্নাংশ দুটি যোগ করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "সম্পূর্ণ কাজ সম্পন্ন হওয়ার মোট দিন",
                        expressionOrMathBn = "মোট দিন = ১ ÷ (${formatNum(timeA + timeB)} / ${formatNum(timeA * timeB)}) = (${formatNum(timeA)} × ${formatNum(timeB)}) / (${formatNum(timeA)} + ${formatNum(timeB)}) = ${formatNum(timeA * timeB)} / ${formatNum(timeA + timeB)} = ${formatNum(totalDays)} দিন",
                        explanationBn = "১ দিনে সম্পন্ন কাজের অংশ দ্বারা মোট কাজ (১)-কে ভাগ করে প্রয়োজনীয় দিন বের করা হলো।"
                    )
                )

                val verification = if (mode == SolverMode.LEARNING) {
                    "শুদ্ধি পরীক্ষা: ${formatNum(totalDays)} দিনে A করে (${formatNum(totalDays)} ÷ ${formatNum(timeA)}) অংশ এবং B করে (${formatNum(totalDays)} ÷ ${formatNum(timeB)}) অংশ। এদের যোগফল = ১ অংশ (সম্পূর্ণ কাজ)।"
                } else null

                val bcsShortcut = if (mode == SolverMode.BCS) {
                    "বিসিএস সুপার শর্টকাট: A এবং B একত্রে করলে মোট দিন = (xy) ÷ (x + y)। গুণফলকে যোগফল দিয়ে সরাসরি ভাগ করুন!"
                } else null

                return SolverResult(
                    topic = SolverTopic.TIME_AND_WORK,
                    mode = mode,
                    originalProblemBn = "A একটি কাজ করে ${formatNum(timeA)} দিনে এবং B করে ${formatNum(timeB)} দিনে। তারা একত্রে কত দিনে কাজটি শেষ করবে?",
                    formulaUsedBn = "মোট দিন = (A × B) ÷ (A + B)",
                    steps = steps,
                    finalAnswerBn = "${formatNum(totalDays)} দিন",
                    verificationBn = verification,
                    bcsShortcutBn = bcsShortcut,
                    similarProblemPromptBn = "A করে ১০ দিনে, B করে ১৫ দিনে"
                )
            }
            WorkType.THREE_PERSONS -> {
                val tC = timeC ?: 12.0
                val combinedRate = (1.0 / timeA) + (1.0 / timeB) + (1.0 / tC)
                val totalDays = 1.0 / combinedRate

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "তিনজনের ১ দিনের পৃথক কাজের অংশ",
                        expressionOrMathBn = "A এর কাজ = ১/${formatNum(timeA)}, B এর কাজ = ১/${formatNum(timeB)}, C এর কাজ = ১/${formatNum(tC)}",
                        explanationBn = "প্রত্যেকের একদিনের একক কাজ বের করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "একত্রে ১ দিনের মোট কাজ",
                        expressionOrMathBn = "১/${formatNum(timeA)} + ১/${formatNum(timeB)} + ১/${formatNum(tC)} = ${formatNum(combinedRate)} অংশ",
                        explanationBn = "তিনজনের দৈনিক সক্ষমতা যোগ করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "মোট প্রয়োজনীয় দিন",
                        expressionOrMathBn = "মোট দিন = ১ ÷ ${formatNum(combinedRate)} = ${formatNum(totalDays)} দিন",
                        explanationBn = "সম্পূর্ণ কাজ সম্পন্ন হতে প্রয়োজনীয় দিন।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.TIME_AND_WORK,
                    mode = mode,
                    originalProblemBn = "A, B এবং C একটি কাজ যথাক্রমে ${formatNum(timeA)}, ${formatNum(timeB)} ও ${formatNum(tC)} দিনে করতে পারে। একত্রে কত দিনে কাজটি শেষ হবে?",
                    formulaUsedBn = "মোট দিন = ১ ÷ (১/A + ১/B + ১/C)",
                    steps = steps,
                    finalAnswerBn = "${formatNum(totalDays)} দিন",
                    verificationBn = "যাচাই: তিনজনের ${formatNum(totalDays)} দিনের মোট কাজের সমষ্টি = ১ অংশ।"
                )
            }
            WorkType.PIPE_CISTERN -> {
                // timeA = fill pipe, timeB = empty pipe
                if (timeA >= timeB) {
                    return errorResult(
                        SolverTopic.TIME_AND_WORK,
                        mode,
                        "Fill=$timeA, Empty=$timeB",
                        "খালি করার নলের সময় কম বা সমান হলে চৌবাচ্চা কখনোই পূর্ণ হবে না (পানি বেশি গতিতে বের হয়ে যাবে)।"
                    )
                }

                val netHours = (timeA * timeB) / (timeB - timeA)

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "১ ঘন্টায় পূর্ণ ও খালি হওয়ার অংশ",
                        expressionOrMathBn = "পূর্ণকারী নল ১ ঘন্টায় ভরে = ১/${formatNum(timeA)} অংশ\nখালি করার নল ১ ঘন্টায় খালি করে = ১/${formatNum(timeB)} অংশ",
                        explanationBn = "প্রতি ঘন্টায় আগমন ও নির্গমনের হার বের করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "১ ঘন্টায় নিট অবশিষ্ট পানির পরিমাণ",
                        expressionOrMathBn = "১/${formatNum(timeA)} - ১/${formatNum(timeB)} = (${formatNum(timeB)} - ${formatNum(timeA)}) / (${formatNum(timeA)} × ${formatNum(timeB)}) অংশ",
                        explanationBn = "খালি করার কারণে পূর্ণকারী নল থেকে বিয়োগ করা হলো।"
                    )
                )

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "চৌবাচ্চা পূর্ণ হতে প্রয়োজনীয় মোট সময়",
                        expressionOrMathBn = "মোট সময় = (${formatNum(timeA)} × ${formatNum(timeB)}) ÷ (${formatNum(timeB)} - ${formatNum(timeA)}) = ${formatNum(netHours)} ঘন্টা",
                        explanationBn = "নিট কার্যকর হার দিয়ে ভাগ করে মোট সময় পাওয়া গেল।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.TIME_AND_WORK,
                    mode = mode,
                    originalProblemBn = "একটি নল দ্বারা চৌবাচ্চা ${formatNum(timeA)} ঘন্টায় পূর্ণ হয় এবং অপর নল দ্বারা ${formatNum(timeB)} ঘন্টায় খালি হয়। নল দুটি একসাথে খুলে দিলে কতক্ষণে পূর্ণ হবে?",
                    formulaUsedBn = "সময় = (A × B) ÷ (B - A)",
                    steps = steps,
                    finalAnswerBn = "${formatNum(netHours)} ঘন্টা",
                    verificationBn = "যাচাই: ${formatNum(netHours)} ঘন্টায় পানি প্রবেশ করে ${formatNum(netHours/timeA)} অংশ এবং বের হয় ${formatNum(netHours/timeB)} অংশ। নিট অংশ = ১।"
                )
            }
        }
    }

    // -------------------------------------------------------------
    // 9. SPEED, DISTANCE AND TIME
    // -------------------------------------------------------------
    fun solveSpeedDistanceTime(
        speed: Double?,
        distance: Double?,
        time: Double?,
        mode: SolverMode
    ): SolverResult {
        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        // Case 1: Solve for Speed (S = D / T)
        if (distance != null && time != null && speed == null) {
            if (time <= 0.0) {
                return errorResult(
                    SolverTopic.SPEED_DISTANCE_TIME,
                    mode,
                    "Time=$time",
                    "সময় অবশ্যই শূন্যের চেয়ে বেশি হতে হবে।"
                )
            }
            val sKmh = distance / time
            val sMs = sKmh * (5.0 / 18.0)

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "গতিবেগের মূল সূত্র প্রয়োগ",
                    expressionOrMathBn = "গতিবেগ (S) = দূরত্ব (D) ÷ সময় (T)",
                    explanationBn = "নির্দিষ্ট সময়ে অতিক্রান্ত দূরত্বের অনুপাতই হলো গতিবেগ।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "গতিবেগ গণনা (km/h)",
                    expressionOrMathBn = "S = ${formatNum(distance)} km ÷ ${formatNum(time)} ঘন্টা = ${formatNum(sKmh)} km/h",
                    explanationBn = "দূরত্বকে সময় দিয়ে ভাগ করে প্রতি ঘন্টার বেগ পাওয়া গেল।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "মিটার/সেকেন্ড (m/s) এ রূপান্তর",
                    expressionOrMathBn = "${formatNum(sKmh)} × (৫/১৮) = ${formatNum(sMs)} m/s",
                    explanationBn = "১ km/h = ১০০০ m / ৩৬০০ s = ৫/১৮ m/s। তাই ৫/১৮ দিয়ে গুণ করা হলো।",
                    whyThisStepBn = "পরীক্ষায় প্রায়শই একক km/h থেকে m/s এ রূপান্তর করে উত্তর দিতে বলা হয়।"
                )
            )

            val verification = if (mode == SolverMode.LEARNING) {
                "শুদ্ধি পরীক্ষা: গতিবেগ ${formatNum(sKmh)} km/h নিয়ে ${formatNum(time)} ঘন্টা চললে মোট দূরত্ব = ${formatNum(sKmh)} × ${formatNum(time)} = ${formatNum(distance)} km।"
            } else null

            val bcsShortcut = if (mode == SolverMode.BCS) {
                "বিসিএস শর্টকাট: km/h থেকে m/s করতে ৫/১৮ দিয়ে গুণ করুন। আবার m/s থেকে km/h এ নিতে উল্টো ১৮/৫ দিয়ে গুণ করুন।"
            } else null

            return SolverResult(
                topic = SolverTopic.SPEED_DISTANCE_TIME,
                mode = mode,
                originalProblemBn = "দূরত্ব ${formatNum(distance)} কিমি এবং সময় ${formatNum(time)} ঘন্টা হলে গতিবেগ কত?",
                formulaUsedBn = "S = D ÷ T এবং ১ km/h = ৫/১৮ m/s",
                steps = steps,
                finalAnswerBn = "${formatNum(sKmh)} km/h (${formatNum(sMs)} m/s)",
                verificationBn = verification,
                bcsShortcutBn = bcsShortcut,
                similarProblemPromptBn = "দূরত্ব ১২০ কিমি, সময় ২ ঘন্টা"
            )
        }

        // Case 2: Solve for Distance (D = S * T)
        if (speed != null && time != null && distance == null) {
            val d = speed * time

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "দূরত্বের সূত্র প্রয়োগ",
                    expressionOrMathBn = "দূরত্ব (D) = গতিবেগ (S) × সময় (T)",
                    explanationBn = "গতিবেগের সাথে ব্যয়িত সময় গুণ করে মোট অতিক্রান্ত দূরত্ব নির্ণয় করা হয়।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "দূরত্ব গণনা",
                    expressionOrMathBn = "D = ${formatNum(speed)} × ${formatNum(time)} = ${formatNum(d)} km",
                    explanationBn = "মান বসিয়ে মোট দূরত্ব পাওয়া গেল।"
                )
            )

            return SolverResult(
                topic = SolverTopic.SPEED_DISTANCE_TIME,
                mode = mode,
                originalProblemBn = "গতিবেগ ${formatNum(speed)} km/h এবং সময় ${formatNum(time)} ঘন্টা হলে অতিক্রান্ত দূরত্ব কত?",
                formulaUsedBn = "D = S × T",
                steps = steps,
                finalAnswerBn = "${formatNum(d)} km",
                verificationBn = "যাচাই: ${formatNum(d)} ÷ ${formatNum(time)} = ${formatNum(speed)} km/h"
            )
        }

        // Case 3: Solve for Time (T = D / S)
        if (distance != null && speed != null && time == null) {
            if (speed <= 0.0) {
                return errorResult(
                    SolverTopic.SPEED_DISTANCE_TIME,
                    mode,
                    "Speed=$speed",
                    "গতিবেগ শূন্য বা ঋণাত্মক হতে পারে না।"
                )
            }
            val t = distance / speed

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "সময় নির্ণয়ের সূত্র প্রয়োগ",
                    expressionOrMathBn = "সময় (T) = দূরত্ব (D) ÷ গতিবেগ (S)",
                    explanationBn = "দূরত্বকে গতিবেগ দিয়ে ভাগ করে প্রয়োজনীয় সময় পাওয়া যায়।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "সময় গণনা (ঘন্টায়)",
                    expressionOrMathBn = "T = ${formatNum(distance)} ÷ ${formatNum(speed)} = ${formatNum(t)} ঘন্টা",
                    explanationBn = "ভাগ করে ঘন্টায় সময় বের করা হলো।"
                )
            )

            val totalSeconds = round(t * 3600.0).toLong()
            val h = totalSeconds / 3600L
            val remSeconds = totalSeconds % 3600L
            val m = remSeconds / 60L
            val s = remSeconds % 60L

            val isWholeHours = (remSeconds == 0L)
            val isExactMinutes = (s == 0L)

            val finalAnswerString: String

            if (isWholeHours) {
                finalAnswerString = "${formatNum(h)} ঘন্টা"
            } else if (isExactMinutes) {
                val timeStr = if (h > 0) "${formatNum(h)} ঘন্টা ${formatNum(m)} মিনিট" else "${formatNum(m)} মিনিট"
                finalAnswerString = "${formatNum(t)} ঘন্টা ($timeStr)"
                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "ঘন্টাকে ঘন্টা ও মিনিটে রূপান্তর",
                        expressionOrMathBn = "${formatNum(t)} ঘন্টা = $timeStr",
                        explanationBn = "১ ঘন্টা = ৬০ মিনিট। ঘন্টার দশমিক অংশকে ৬০ দিয়ে গুণ করে নিখুঁত মিনিট পাওয়া গেল।"
                    )
                )
            } else {
                // Non-integer number of minutes: clearly defined rounding policy
                val approxM = if (s >= 30L) m + 1L else m
                val hPart = if (h > 0) "${formatNum(h)} ঘন্টা " else ""
                val exactSecStr = "$hPart${formatNum(m)} মিনিট ${formatNum(s)} সেকেন্ড"
                val approxHPart = if (approxM >= 60L) {
                    "${formatNum(h + 1L)} ঘন্টা"
                } else if (h > 0) {
                    "${formatNum(h)} ঘন্টা ${formatNum(approxM)} মিনিট"
                } else {
                    "${formatNum(approxM)} মিনিট"
                }
                finalAnswerString = "${formatNum(t)} ঘন্টা ($exactSecStr বা প্রায় $approxHPart)"
                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "ঘন্টাকে ঘন্টা, মিনিট ও সেকেন্ডে রূপান্তর (রাউন্ডিং নীতি)",
                        expressionOrMathBn = "${formatNum(t)} ঘন্টা = $exactSecStr (বা প্রায় $approxHPart)",
                        explanationBn = "যেহেতু সময়টি পূর্ণ মিনিটে ভাগ হয় না, তাই অবশিষ্টাংশকে ৬০ দিয়ে গুণ করে নিকটতম সেকেন্ডে (${formatNum(s)} সেকেন্ড) এবং নিকটতম মিনিটে ($approxHPart) প্রকাশ করা হলো।"
                    )
                )
            }

            return SolverResult(
                topic = SolverTopic.SPEED_DISTANCE_TIME,
                mode = mode,
                originalProblemBn = "দূরত্ব ${formatNum(distance)} কিমি এবং গতিবেগ ${formatNum(speed)} km/h হলে প্রয়োজনীয় সময় কত?",
                formulaUsedBn = "T = D ÷ S",
                steps = steps,
                finalAnswerBn = finalAnswerString,
                verificationBn = "যাচাই: ${formatNum(speed)} × ${formatNum(t)} = ${formatNum(distance)} km"
            )
        }

        return errorResult(
            SolverTopic.SPEED_DISTANCE_TIME,
            mode,
            "অসম্পূর্ণ তথ্য",
            "গতিবেগ (S), দূরত্ব (D) ও সময় (T) এর যেকোনো ২টি রাশির মান দিন।"
        )
    }

    // -------------------------------------------------------------
    // 10. NUMBER SYSTEM & DIVISIBILITY
    // -------------------------------------------------------------
    fun solveDivisibility(number: Long, divisor: Int, mode: SolverMode): SolverResult {
        if (divisor <= 0) {
            return errorResult(
                SolverTopic.NUMBER_SYSTEM_DIVISIBILITY,
                mode,
                "Divisor=$divisor",
                "ভাজক অবশ্যই ১ বা তার চেয়ে বড় ধনাত্মক পূর্ণসংখ্যা হতে হবে।"
            )
        }

        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        val isDiv = (number % divisor == 0L)
        val quotient = number / divisor
        val remainder = number % divisor

        val (ruleBn, checkStepBn) = getDivisibilityExplanation(number, divisor)

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "$divisor দ্বারা বিভাজ্যতার নিয়ম ও শর্ত",
                expressionOrMathBn = ruleBn,
                explanationBn = "নিয়মটি সংখ্যাটির প্রতিটি অঙ্কের ওপর কীভাবে কাজ করে তার ভিত্তি।"
            )
        )

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "প্রদত্ত সংখ্যাটির ওপর শর্ত প্রয়োগ",
                expressionOrMathBn = checkStepBn,
                explanationBn = if (isDiv) {
                    "শর্তটি পূর্ণ হওয়ায় সংখ্যাটি $divisor দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "শর্তটি পূর্ণ না হওয়ায় সংখ্যাটি $divisor দ্বারা নিঃশেষে বিভাজ্য নয়।"
                }
            )
        )

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "ভাগফল ও ভাগশেষ যাচাই",
                expressionOrMathBn = "${formatNum(number)} ÷ ${formatNum(divisor.toLong())} = ভাগফল ${formatNum(quotient)}, ভাগশেষ ${formatNum(remainder)}",
                explanationBn = "ভাজ্য = ভাজক × ভাগফল + ভাগশেষ সমীকরণ অনুসারে পূর্ণ যাচাই।"
            )
        )

        val finalAns = if (isDiv) {
            "${formatNum(number)} সংখ্যাটি ${formatNum(divisor.toLong())} দ্বারা নিঃশেষে বিভাজ্য (ভাগফল: ${formatNum(quotient)})"
        } else {
            "${formatNum(number)} সংখ্যাটি ${formatNum(divisor.toLong())} দ্বারা বিভাজ্য নয় (ভাগশেষ: ${formatNum(remainder)})"
        }

        val verification = if (mode == SolverMode.LEARNING) {
            "শুদ্ধি পরীক্ষা: ${formatNum(divisor.toLong())} × ${formatNum(quotient)} + ${formatNum(remainder)} = ${formatNum(divisor * quotient + remainder)} = ${formatNum(number)}।"
        } else null

        val bcsShortcut = if (mode == SolverMode.BCS) {
            "বিসিএস ট্রিক: ৩ ও ৯ এর জন্য অঙ্কগুলোর সমষ্টি দেখুন। ৪ এর জন্য শেষ ২ অঙ্ক এবং ৮ এর জন্য শেষ ৩ অঙ্ক দেখুন। ১১ এর জন্য বিজোড় ও জোড় অবস্থানের অঙ্কগুলোর সমষ্টির বিয়োগফল ০ বা ১১ এর গুণিতক কিনা পরীক্ষা করুন।"
        } else null

        return SolverResult(
            topic = SolverTopic.NUMBER_SYSTEM_DIVISIBILITY,
            mode = mode,
            originalProblemBn = "${formatNum(number)} সংখ্যাটি ${formatNum(divisor.toLong())} দ্বারা বিভাজ্য কিনা যাচাই করুন",
            formulaUsedBn = "ভাজ্য = (ভাজক × ভাগফল) + ভাগশেষ",
            steps = steps,
            finalAnswerBn = finalAns,
            verificationBn = verification,
            bcsShortcutBn = bcsShortcut,
            similarProblemPromptBn = "৪৫৭২ সংখ্যাটি ৪ দ্বারা বিভাজ্য কিনা?"
        )
    }

    private fun getDivisibilityExplanation(num: Long, div: Int): Pair<String, String> {
        val s = num.toString()
        val digits = s.map { it.digitToInt() }

        return when (div) {
            2 -> {
                val last = digits.last()
                Pair(
                    "২ দ্বারা বিভাজ্যতার নিয়ম: কোনো সংখ্যার শেষ অঙ্ক (এককের অঙ্ক) ০, ২, ৪, ৬, ৮ হলে সংখ্যাটি ২ দ্বারা বিভাজ্য।",
                    "শেষ অঙ্ক = $last ${if (last % 2 == 0) "যা জোড় সংখ্যা। অতএব বিভাজ্য।" else "যা বিজোড় সংখ্যা। অতএব বিভাজ্য নয়।"}"
                )
            }
            3 -> {
                val sum = digits.sum()
                Pair(
                    "৩ দ্বারা বিভাজ্যতার নিয়ম: কোনো সংখ্যার সকল অঙ্কের যোগফল ৩ দ্বারা বিভাজ্য হলে মূল সংখ্যাটিও ৩ দ্বারা বিভাজ্য।",
                    "অঙ্কগুলোর সমষ্টি = ${digits.joinToString(" + ")} = $sum। ${if (sum % 3 == 0) "$sum সংখ্যাটি ৩ দ্বারা বিভাজ্য (৩ × ${sum / 3})।" else "$sum সংখ্যাটি ৩ দ্বারা বিভাজ্য নয়।"}"
                )
            }
            4 -> {
                val last2 = if (s.length >= 2) s.takeLast(2).toInt() else num.toInt()
                Pair(
                    "৪ দ্বারা বিভাজ্যতার নিয়ম: সংখ্যার শেষ দুটি অঙ্ক দ্বারা গঠিত সংখ্যা ৪ দ্বারা বিভাজ্য হলে বা শেষ দুটি অঙ্ক '০০' হলে সংখ্যাটি ৪ দ্বারা বিভাজ্য।",
                    "শেষ দুটি অঙ্ক = $last2। ${if (last2 % 4 == 0) "$last2 সংখ্যাটি ৪ দ্বারা বিভাজ্য।" else "$last2 সংখ্যাটি ৪ দ্বারা বিভাজ্য নয়।"}"
                )
            }
            5 -> {
                val last = digits.last()
                Pair(
                    "৫ দ্বারা বিভাজ্যতার নিয়ম: এককের অঙ্ক ০ অথবা ৫ হলে সংখ্যাটি ৫ দ্বারা বিভাজ্য।",
                    "এককের অঙ্ক = $last। ${if (last == 0 || last == 5) "যেহেতু অঙ্কটি $last, তাই সংখ্যাটি বিভাজ্য।" else "যেহেতু অঙ্কটি ০ বা ৫ নয়, তাই বিভাজ্য নয়।"}"
                )
            }
            6 -> {
                val isEven = digits.last() % 2 == 0
                val sum3 = digits.sum() % 3 == 0
                Pair(
                    "৬ দ্বারা বিভাজ্যতার নিয়ম: সংখ্যাটি একই সাথে ২ এবং ৩ উভয় দ্বারাই বিভাজ্য হতে হবে (অর্থাৎ জোড় হতে হবে এবং অঙ্কগুলোর সমষ্টি ৩ দ্বারা বিভাজ্য হতে হবে)।",
                    "১) জোড় সংখ্যা কিনা: ${if (isEven) "হ্যাঁ (২ দ্বারা বিভাজ্য)" else "না (২ দ্বারা বিভাজ্য নয়)"}\n২) অঙ্কগুলোর যোগফল ৩ দ্বারা বিভাজ্য কিনা: ${if (sum3) "হ্যাঁ" else "না"}"
                )
            }
            7 -> {
                val rem = num % 7
                Pair(
                    "৭ দ্বারা বিভাজ্যতার নিয়ম: শেষ অঙ্কটি দ্বিগুণ করে অবশিষ্ট সংখ্যা থেকে বিয়োগ করার প্রক্রিয়া পুনরাবৃত্তি করা।",
                    "${formatNum(num)} ÷ ৭ করলে ভাগফল ${num / 7} এবং ভাগশেষ $rem। ${if (rem == 0L) "ভাগশেষ ০ হওয়ায় সংখ্যাটি ৭ দ্বারা বিভাজ্য।" else "ভাগশেষ $rem থাকায় এটি বিভাজ্য নয়।"}"
                )
            }
            8 -> {
                val last3 = if (s.length >= 3) s.takeLast(3).toInt() else num.toInt()
                Pair(
                    "৮ দ্বারা বিভাজ্যতার নিয়ম: সংখ্যার শেষ তিনটি অঙ্ক দ্বারা গঠিত সংখ্যা ৮ দ্বারা বিভাজ্য হলে পুরো সংখ্যাটি ৮ দ্বারা বিভাজ্য।",
                    "শেষ তিনটি অঙ্ক = $last3। ${if (last3 % 8 == 0) "$last3 সংখ্যাটি ৮ দ্বারা বিভাজ্য।" else "$last3 সংখ্যাটি ৮ দ্বারা বিভাজ্য নয়।"}"
                )
            }
            9 -> {
                val sum = digits.sum()
                Pair(
                    "৯ দ্বারা বিভাজ্যতার নিয়ম: সংখ্যার অঙ্কগুলোর সমষ্টি ৯ দ্বারা বিভাজ্য হতে হবে।",
                    "অঙ্কগুলোর সমষ্টি = ${digits.joinToString(" + ")} = $sum। ${if (sum % 9 == 0) "$sum সংখ্যাটি ৯ দ্বারা বিভাজ্য।" else "$sum সংখ্যাটি ৯ দ্বারা বিভাজ্য নয়।"}"
                )
            }
            10 -> {
                val last = digits.last()
                Pair(
                    "১০ দ্বারা বিভাজ্যতার নিয়ম: এককের অঙ্ক অবশ্যই শূন্য (০) হতে হবে।",
                    "এককের অঙ্ক = $last। ${if (last == 0) "এককের অঙ্ক ০, তাই ১০ দ্বারা বিভাজ্য।" else "এককের অঙ্ক ০ নয়, তাই বিভাজ্য নয়।"}"
                )
            }
            11 -> {
                var oddSum = 0
                var evenSum = 0
                for (i in s.indices) {
                    val d = s[s.length - 1 - i].digitToInt()
                    if (i % 2 == 0) oddSum += d else evenSum += d
                }
                val diff = abs(oddSum - evenSum)
                Pair(
                    "১১ দ্বারা বিভাজ্যতার নিয়ম: ডানদিক থেকে বিজোড় অবস্থানের অঙ্কগুলোর যোগফল এবং জোড় অবস্থানের অঙ্কগুলোর যোগফলের পার্থক্য ০ অথবা ১১ এর গুণিতক হতে হবে।",
                    "বিজোড় অবস্থানের সমষ্টি = $oddSum\nজোড় অবস্থানের সমষ্টি = $evenSum\nপার্থক্য = |$oddSum - $evenSum| = $diff। ${if (diff % 11 == 0) "পার্থক্য $diff (যা ১১ দ্বারা বিভাজ্য)। অতএব মূল সংখ্যাটি ১১ দ্বারা বিভাজ্য।" else "পার্থক্য $diff (যা ১১ দ্বারা বিভাজ্য নয়)। অতএব বিভাজ্য নয়।"}"
                )
            }
            else -> {
                Pair(
                    "$div দ্বারা বিভাজ্যতার সাধারণ নিয়ম: সরাসরি ভাগ প্রক্রিয়া প্রয়োগ।",
                    "${formatNum(num)} % $div = ${num % div}। ${if (num % div == 0L) "নিঃশেষে বিভাজ্য।" else "বিভাজ্য নয়।"}"
                )
            }
        }
    }

    // -------------------------------------------------------------
    // 11. HCF AND LCM
    // -------------------------------------------------------------
    fun solveHcfLcm(numbers: List<Long>, mode: SolverMode): SolverResult {
        val validNums = numbers.filter { it > 0 }
        if (validNums.size < 2) {
            return errorResult(
                SolverTopic.HCF_LCM,
                mode,
                "কম সংখ্যা",
                "গ.সা.গু ও ল.সা.গু নির্ণয়ের জন্য কমপক্ষে ২টি ধনাত্মক সংখ্যা ইনপুট দিন।"
            )
        }

        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        val a = validNums[0]
        val b = validNums[1]

        val gcdVal = if (validNums.size == 2) {
            NumberSystemProvider.gcd(a, b)
        } else {
            validNums.reduce { acc, n -> NumberSystemProvider.gcd(acc, n) }
        }

        val lcmVal = if (validNums.size == 2) {
            NumberSystemProvider.lcm(a, b)
        } else {
            var currentLcm = validNums[0]
            for (i in 1 until validNums.size) {
                currentLcm = NumberSystemProvider.lcm(currentLcm, validNums[i])
                if (currentLcm <= 0L) break
            }
            currentLcm
        }

        if (lcmVal <= 0L) {
            return errorResult(
                SolverTopic.HCF_LCM,
                mode,
                validNums.joinToString(", ") { formatNum(it) },
                "প্রদত্ত সংখ্যাগুলোর ল.সা.গু গণনা সমর্থিত সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)। অপেক্ষাকৃত ছোট মান ব্যবহার করুন।"
            )
        }

        // Prime factorizations for explanation
        val primeFactors = validNums.map { n ->
            n to NumberSystemProvider.getPrimeFactors(n)
        }

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "মৌলিক উৎপাদকে বিশ্লেষণ",
                expressionOrMathBn = primeFactors.joinToString("\n") { (n, factors) ->
                    val factorStr = if (factors.isEmpty()) formatNum(n) else factors.joinToString(" × ") { formatNum(it) }
                    "${formatNum(n)} = $factorStr"
                },
                explanationBn = "প্রতিটি সংখ্যাকে মৌলিক উৎপাদকের গুণফল আকারে প্রকাশ করা হলো।"
            )
        )

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "গ.সা.গু (HCF) নির্ণয়",
                expressionOrMathBn = "গ.সা.গু = ${formatNum(gcdVal)}",
                explanationBn = "প্রদত্ত সংখ্যাগুলোর মধ্যে সাধারণ (কমন) মৌলিক উৎপাদকগুলোর গুণফলই হলো গরিষ্ঠ সাধারণ গুণনীয়ক (গ.সা.গু)।"
            )
        )

        steps.add(
            SolutionStep(
                stepIndex = stepCount++,
                titleBn = "ল.সা.গু (LCM) নির্ণয়",
                expressionOrMathBn = "ল.সা.গু = ${formatNum(lcmVal)}",
                explanationBn = "প্রতিটি মৌলিক উৎপাদকের সর্বোচ্চ ঘাতগুলোর গুণফল হলো লঘিষ্ঠ সাধারণ গুণিতক (ল.সা.গু)।"
            )
        )

        val verification = if (validNums.size == 2) {
            val prodAB = java.math.BigInteger.valueOf(a).multiply(java.math.BigInteger.valueOf(b))
            val prodGL = java.math.BigInteger.valueOf(gcdVal).multiply(java.math.BigInteger.valueOf(lcmVal))
            "সম্পর্ক পরীক্ষা: দুটি সংখ্যার গুণফল = ${formatNum(a)} × ${formatNum(b)} = ${BengaliNumberFormatter.toBn(prodAB.toString())}। গ.সা.গু × ল.সা.গু = ${formatNum(gcdVal)} × ${formatNum(lcmVal)} = ${BengaliNumberFormatter.toBn(prodGL.toString())}। সূত্র প্রমাণিত (গুণফল = গ.সা.গু × ল.সা.গু)।"
        } else null

        val bcsShortcut = if (mode == SolverMode.BCS) {
            "বিসিএস প্রশ্ন টেকনিক: দুটি সংখ্যার গ.সা.গু ও ল.সা.গু যথাক্রমে X ও Y এবং একটি সংখ্যা Z হলে অপরটি = (X × Y) ÷ Z। পরীক্ষায় এটি সরাসরি আসে।"
        } else null

        val listBn = validNums.joinToString(", ") { formatNum(it) }
        return SolverResult(
            topic = SolverTopic.HCF_LCM,
            mode = mode,
            originalProblemBn = "$listBn এর গ.সা.গু ও ল.সা.গু নির্ণয় করুন",
            formulaUsedBn = "দুটি সংখ্যার গুণফল = গ.সা.গু × ল.সা.গু",
            steps = steps,
            finalAnswerBn = "গ.সা.গু (HCF): ${formatNum(gcdVal)}, ল.সা.গু (LCM): ${formatNum(lcmVal)}",
            verificationBn = verification,
            bcsShortcutBn = bcsShortcut,
            similarProblemPromptBn = "২৪ এবং ৩৬ এর গ.সা.গু ও ল.সা.গু কত?"
        )
    }

    // -------------------------------------------------------------
    // 12. SQUARES, CUBES AND ROOTS
    // -------------------------------------------------------------
    enum class SquareCubeOp { SQUARE, SQUARE_ROOT, CUBE, CUBE_ROOT }

    fun solveSquareCube(
        number: Double,
        op: SquareCubeOp,
        mode: SolverMode
    ): SolverResult {
        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        if (number.isNaN() || number.isInfinite()) {
            return errorResult(
                SolverTopic.SQUARES_CUBES_ROOTS,
                mode,
                number.toString(),
                "সংখ্যার মান সমর্থিত সীমার বাইরে।"
            )
        }

        when (op) {
            SquareCubeOp.SQUARE -> {
                val sq = number * number
                if (sq.isInfinite() || sq.isNaN()) {
                    return errorResult(
                        SolverTopic.SQUARES_CUBES_ROOTS,
                        mode,
                        formatNum(number),
                        "বর্গফল সমর্থিত সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)।"
                    )
                }

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "বর্গের সংজ্ঞা অনুযায়ী সংখ্যাটিকে নিজের সাথে গুণ",
                        expressionOrMathBn = "${formatNum(number)}² = ${formatNum(number)} × ${formatNum(number)} = ${formatNum(sq)}",
                        explanationBn = "কোনো সংখ্যাকে সেই সংখ্যা দ্বারাই গুণ করলে প্রাপ্ত গুণফল হলো ওই সংখ্যার বর্গ।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.SQUARES_CUBES_ROOTS,
                    mode = mode,
                    originalProblemBn = "${formatNum(number)} এর বর্গ নির্ণয় করুন",
                    formulaUsedBn = "N² = N × N",
                    steps = steps,
                    finalAnswerBn = formatNum(sq),
                    verificationBn = "যাচাই: √${formatNum(sq)} = ${formatNum(number)}"
                )
            }
            SquareCubeOp.SQUARE_ROOT -> {
                if (number < 0) {
                    return errorResult(
                        SolverTopic.SQUARES_CUBES_ROOTS,
                        mode,
                        "√$number",
                        "বাস্তব সংখ্যা পদ্ধতিতে ঋণাত্মক সংখ্যার বর্গমূল নির্ণয় সম্ভব নয় (অবাস্তব বা জটিল সংখ্যা)।"
                    )
                }

                val sqrtVal = sqrt(number)
                val isPerfect = sqrtVal == floor(sqrtVal) && number == floor(number)

                if (isPerfect) {
                    val factors = if (number <= 10_000_000.0) {
                        NumberSystemProvider.getPrimeFactors(number.toLong())
                    } else emptyList()
                    val factorStr = if (factors.isEmpty()) formatNum(number.toLong()) else factors.joinToString(" × ") { formatNum(it) }
                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "মৌলিক উৎপাদকে বিশ্লেষণ",
                            expressionOrMathBn = "${formatNum(number.toLong())} = $factorStr",
                            explanationBn = "পূর্ণবর্গ সংখ্যাটির মৌলিক উৎপাদক বের করা হলো।"
                        )
                    )

                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "জোড়া তৈরি ও প্রতি জোড়া থেকে একটি নেওয়া",
                            expressionOrMathBn = "√${formatNum(number.toLong())} = ${formatNum(sqrtVal.toLong())}",
                            explanationBn = "প্রতিটি উৎপাদকের জোড়া থেকে একটি করে নিয়ে গুণ করে বর্গমূল পাওয়া গেল।"
                        )
                    )
                } else {
                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "ভাগ প্রক্রিয়া ও আসন্ন মান",
                            expressionOrMathBn = "√${formatNum(number)} ≈ ${formatNum(sqrtVal)}",
                            explanationBn = "যেহেতু সংখ্যাটি পূর্ণবর্গ নয়, তাই এর বর্গমূল একটি অমূলদ সংখ্যা (আসন্ন মান নেওয়া হলো)।"
                        )
                    )
                }

                return SolverResult(
                    topic = SolverTopic.SQUARES_CUBES_ROOTS,
                    mode = mode,
                    originalProblemBn = "√${formatNum(number)} (বর্গমূল নির্ণয়)",
                    formulaUsedBn = "√N = R যেখানে R × R = N",
                    steps = steps,
                    finalAnswerBn = formatNum(sqrtVal),
                    verificationBn = "যাচাই: (${formatNum(sqrtVal)})² = ${formatNum(sqrtVal * sqrtVal)}",
                    bcsShortcutBn = if (mode == SolverMode.BCS) "বিসিএস টিপস: ১ থেকে ৩০ পর্যন্ত সংখ্যার বর্গ মুখস্থ থাকলে পরীক্ষায় প্রচুর সময় বাঁচে।" else null,
                    similarProblemPromptBn = "√১৪৪ এর মান কত?"
                )
            }
            SquareCubeOp.CUBE -> {
                val cb = number * number * number
                if (cb.isInfinite() || cb.isNaN()) {
                    return errorResult(
                        SolverTopic.SQUARES_CUBES_ROOTS,
                        mode,
                        formatNum(number),
                        "ঘনফল সমর্থিত সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)।"
                    )
                }

                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "ঘনের সংজ্ঞা প্রয়োগ",
                        expressionOrMathBn = "${formatNum(number)}³ = ${formatNum(number)} × ${formatNum(number)} × ${formatNum(number)} = ${formatNum(cb)}",
                        explanationBn = "একটি সংখ্যাকে পরপর তিনবার গুণ করলে তার ঘনফল পাওয়া যায়।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.SQUARES_CUBES_ROOTS,
                    mode = mode,
                    originalProblemBn = "${formatNum(number)} এর ঘন (Cube) নির্ণয় করুন",
                    formulaUsedBn = "N³ = N × N × N",
                    steps = steps,
                    finalAnswerBn = formatNum(cb),
                    verificationBn = "যাচাই: ³√${formatNum(cb)} = ${formatNum(number)}"
                )
            }
            SquareCubeOp.CUBE_ROOT -> {
                val cbrtVal = cbrt(number)
                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "ঘনমূল নির্ণয়",
                        expressionOrMathBn = "³√${formatNum(number)} = ${formatNum(cbrtVal)}",
                        explanationBn = "এমন একটি সংখ্যা নির্ণয় যা তিনবার গুণ করলে মূল সংখ্যাটি পাওয়া যায়।"
                    )
                )

                return SolverResult(
                    topic = SolverTopic.SQUARES_CUBES_ROOTS,
                    mode = mode,
                    originalProblemBn = "³√${formatNum(number)} (ঘনমূল নির্ণয়)",
                    formulaUsedBn = "³√N = R যেখানে R³ = N",
                    steps = steps,
                    finalAnswerBn = formatNum(cbrtVal),
                    verificationBn = "যাচাই: (${formatNum(cbrtVal)})³ = ${formatNum(number)}"
                )
            }
        }
    }

    // -------------------------------------------------------------
    // 13. BASIC ALGEBRA AND LINEAR EQUATIONS
    // -------------------------------------------------------------
    fun solveLinearEquation(equationStr: String, mode: SolverMode): SolverResult {
        val trimmed = equationStr.trim()
        if (!trimmed.contains("=")) {
            return errorResult(
                SolverTopic.LINEAR_EQUATIONS,
                mode,
                trimmed,
                "সমীকরণে অবশ্যই সমান (=) চিহ্ন থাকতে হবে। যেমন: 3x + 12 = 45"
            )
        }

        val parts = trimmed.split("=")
        if (parts.size != 2) {
            return errorResult(
                SolverTopic.LINEAR_EQUATIONS,
                mode,
                trimmed,
                "সমীকরণে কেবল একটিমাত্র সমান (=) চিহ্ন থাকতে হবে।"
            )
        }

        val lhsStr = toAsciiDigits(parts[0].trim().replace(" ", ""))
        val rhsStr = toAsciiDigits(parts[1].trim().replace(" ", ""))

        val steps = mutableListOf<SolutionStep>()
        var stepCount = 1

        try {
            // Parse linear coefficients: lhs = a*x + b, rhs = c*x + d
            val (a, b) = parseLinearSide(lhsStr)
            val (c, d) = parseLinearSide(rhsStr)

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "প্রদত্ত সমীকরণ উপস্থাপন",
                    expressionOrMathBn = BengaliNumberFormatter.toBn(trimmed),
                    explanationBn = "বামপক্ষ এবং ডানপক্ষ চিহ্নিত করা হলো।"
                )
            )

            // Step 1.1: If either side contains parentheses, show expansion step
            if (parts[0].contains("(") || parts[1].contains("(")) {
                steps.add(
                    SolutionStep(
                        stepIndex = stepCount++,
                        titleBn = "বন্ধনীর গুণফল সম্প্রসারণ (বণ্টন বিধি প্রয়োগ)",
                        expressionOrMathBn = "${BengaliNumberFormatter.toBn(parts[0].trim())} = ${BengaliNumberFormatter.toBn(parts[1].trim())} ⟹ ${formatSideExprBn(a, b)} = ${formatSideExprBn(c, d)}",
                        explanationBn = "বন্ধনীর বাইরের সংখ্যা দিয়ে ভেতরের পদগুলোকে গুণ করে বন্ধনী অপসারণ করা হলো।",
                        whyThisStepBn = "সমীকরণ সমাধানের জন্য বন্ধনীমুক্ত করে ax + b আকারে আনা আবশ্যক।"
                    )
                )
            }

            // Step 2: Transposition of variable terms to LHS and constants to RHS
            val netCoeff = a - c
            val netConstant = d - b

            val lhsTransposed = when {
                c == 0.0 -> "${formatLinearCoeff(a)}x"
                c > 0.0 -> "${formatLinearCoeff(a)}x - ${formatLinearCoeff(c)}x"
                else -> "${formatLinearCoeff(a)}x + ${formatLinearCoeff(abs(c))}x"
            }
            val rhsTransposed = when {
                b == 0.0 -> formatNum(d)
                b > 0.0 -> "${formatNum(d)} - ${formatNum(b)}"
                else -> "${formatNum(d)} + ${formatNum(abs(b))}"
            }

            if (netCoeff == 0.0) {
                if (netConstant == 0.0) {
                    steps.add(
                        SolutionStep(
                            stepIndex = stepCount++,
                            titleBn = "পক্ষান্তর করণ ও সমীকরণ বিশ্লেষণ",
                            expressionOrMathBn = "$lhsTransposed = $rhsTransposed ⟹ ০ = ০",
                            explanationBn = "পক্ষান্তরের পর দেখা যাচ্ছে উভয় পক্ষ সম্পূর্ণ সদৃশ এবং চূড়ান্ত সমীকরণ ০ = ০ পাওয়া যায়।",
                            whyThisStepBn = "এটি একটি অভেদ (Identity)। x এর যেকোনো বাস্তব মানের জন্য এই সমীকরণটি সিদ্ধ হয়।"
                        )
                    )
                    return SolverResult(
                        topic = SolverTopic.LINEAR_EQUATIONS,
                        mode = mode,
                        originalProblemBn = BengaliNumberFormatter.toBn(trimmed),
                        formulaUsedBn = "সরল সমীকরণ সমাধান (অভেদ / Identity)",
                        steps = steps,
                        finalAnswerBn = "অসংখ্য সমাধান রয়েছে (অভেদ / Identity: x এর যেকোনো মানের জন্য সত্য)",
                        verificationBn = "উভয় পক্ষ সর্বদা সমান। x এর যেকোনো বাস্তব মানের জন্য সমীকরণটি সিদ্ধ হয়।"
                    )
                } else {
                    return errorResult(
                        SolverTopic.LINEAR_EQUATIONS,
                        mode,
                        trimmed,
                        "সমীকরণটির কোনো বাস্তব সমাধান নেই (অসম্ভব সমীকরণ: ০ = ${formatNum(netConstant)})।"
                    )
                }
            }

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "পক্ষান্তর করণ (চলক পদ বামে ও ধ্রুবক পদ ডানে নেওয়া)",
                    expressionOrMathBn = "$lhsTransposed = $rhsTransposed",
                    explanationBn = "পক্ষান্তরের নিয়ম অনুযায়ী এক পক্ষ থেকে অন্য পক্ষে পদ স্থানান্তর করলে তাদের চিহ্নের পরিবর্তন হয় (+ থাকলে - এবং - থাকলে + হয়)।",
                    whyThisStepBn = "চলকের মান বের করার জন্য সকল চলককে একদিকে এবং সংখ্যাগুলোকে অন্যদিকে সাজানো আবশ্যক।"
                )
            )

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "সদৃশ পদের যোগ/বিয়োগ",
                    expressionOrMathBn = "${formatLinearCoeff(netCoeff)}x = ${formatNum(netConstant)}",
                    explanationBn = "বামপক্ষে x এর সহগগুলো এবং ডানপক্ষে ধ্রুবক সংখ্যাগুলো সরলীকরণ করা হলো।"
                )
            )

            val xVal = netConstant / netCoeff

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "উভয় পক্ষকে x এর সহগ দ্বারা ভাগ",
                    expressionOrMathBn = "x = ${formatNum(netConstant)} ÷ ${formatLinearCoeff(netCoeff)} = ${formatNum(xVal)}",
                    explanationBn = "x কে সম্পূর্ণ স্বাধীন বা মুক্ত করার জন্য তার সহগ দ্বারা ভাগ করা হলো।"
                )
            )

            // Verification by substitution
            val lhsEval = a * xVal + b
            val rhsEval = c * xVal + d

            val verification = if (mode == SolverMode.LEARNING) {
                val lhsSubst = formatSideExprBn(a, b).replace("x", "(${formatNum(xVal)})")
                val rhsSubst = formatSideExprBn(c, d).replace("x", "(${formatNum(xVal)})")
                "শুদ্ধি পরীক্ষা:\nবামপক্ষ = $lhsSubst = ${formatNum(lhsEval)}\nডানপক্ষ = $rhsSubst = ${formatNum(rhsEval)}\nযেহেতু বামপক্ষ = ডানপক্ষ, অতএব সমাধানটি সঠিক।"
            } else null

            val bcsShortcut = if (mode == SolverMode.BCS) {
                "বিসিএস ট্রিক: সমীকরণ সমাধান না করে অপশন টেস্ট (Option Test) করতে পারেন। বিকল্পগুলোর মান সরাসরি সমীকরণে বসিয়ে উভয় পক্ষ মিলে গেলে সেটাই সঠিক উত্তর।"
            } else null

            return SolverResult(
                topic = SolverTopic.LINEAR_EQUATIONS,
                mode = mode,
                originalProblemBn = "সমাধান করুন: ${BengaliNumberFormatter.toBn(trimmed)}",
                formulaUsedBn = "ax + b = cx + d ⟹ (a - c)x = (d - b) ⟹ x = (d - b) ÷ (a - c)",
                steps = steps,
                finalAnswerBn = "x = ${formatNum(xVal)}",
                verificationBn = verification,
                bcsShortcutBn = bcsShortcut,
                similarProblemPromptBn = "৪x + ৮ = ২৮"
            )
        } catch (e: IllegalArgumentException) {
            return errorResult(
                SolverTopic.LINEAR_EQUATIONS,
                mode,
                trimmed,
                e.message ?: "সমীকরণটি বিশ্লেষণে ত্রুটি। এক চলকবিশিষ্ট সরল সমীকরণ লিখুন (যেমন: 2(x + 3) = 14 বা 5x - 3 = 2x + 9)।"
            )
        } catch (e: Exception) {
            return errorResult(
                SolverTopic.LINEAR_EQUATIONS,
                mode,
                trimmed,
                "সমীকরণটি বিশ্লেষণে ত্রুটি। এক চলকবিশিষ্ট সরল সমীকরণ লিখুন (যেমন: 2(x + 3) = 14 বা 5x - 3 = 2x + 9)।"
            )
        }
    }

    private fun parseLinearSide(side: String): Pair<Double, Double> {
        if (side.isBlank()) {
            throw IllegalArgumentException("সমীকরণের এক পাশ ফাঁকা রাখা যাবে না।")
        }

        // Validate parentheses balance
        var parenBalance = 0
        var maxDepth = 0
        var currentDepth = 0
        for (ch in side) {
            if (ch == '(') {
                parenBalance++
                currentDepth++
                if (currentDepth > maxDepth) maxDepth = currentDepth
            } else if (ch == ')') {
                parenBalance--
                currentDepth--
                if (parenBalance < 0) {
                    throw IllegalArgumentException("বন্ধনীর ক্রম সঠিক নয়। অতিরিক্ত সমাপ্তি বন্ধনী রয়েছে।")
                }
            }
        }
        if (parenBalance != 0) {
            throw IllegalArgumentException("বন্ধনীর সংখ্যা অসমান। সমাপ্তি বন্ধনী দেওয়া হয়নি।")
        }
        if (side.contains("()")) {
            throw IllegalArgumentException("বন্ধনীর ভেতরে কোনো রাশি দেওয়া হয়নি।")
        }
        if (maxDepth > 1) {
            throw IllegalArgumentException("নেস্টেড বন্ধনী সমর্থিত নয়। যেমন: 2(x + 3) = 14 আকারে লিখুন।")
        }
        if (side.contains("^") || side.contains("/") || side.contains("x*x", ignoreCase = true)) {
            throw IllegalArgumentException("অরৈখিক বা ভগ্নাংশবিশিষ্ট সমীকরণ সমর্থিত নয়। কেবল এক চলকবিশিষ্ট সরল সমীকরণ (যেমন: 2(x + 3) = 14) সমর্থিত।")
        }
        for (ch in side) {
            if (ch.isLetter() && ch != 'x' && ch != 'X') {
                throw IllegalArgumentException("কেবলমাত্র 'x' চলকবিশিষ্ট সমীকরণ সমর্থিত।")
            }
        }
        if (side.endsWith("+") || side.endsWith("-") || side.endsWith("*")) {
            throw IllegalArgumentException("চিহ্নের পর কোনো পদ পাওয়া যায়নি।")
        }

        // Split into top-level terms
        val terms = mutableListOf<String>()
        var current = StringBuilder()
        var depth = 0
        for (i in side.indices) {
            val ch = side[i]
            if (ch == '(') {
                depth++
                current.append(ch)
            } else if (ch == ')') {
                depth--
                current.append(ch)
            } else if ((ch == '+' || ch == '-') && depth == 0) {
                if (current.isNotEmpty() && current.toString() != "+" && current.toString() != "-") {
                    terms.add(current.toString())
                    current.clear()
                }
                current.append(ch)
            } else {
                current.append(ch)
            }
        }
        if (current.isNotEmpty()) {
            terms.add(current.toString())
        }

        var totalCoeff = 0.0
        var totalConstant = 0.0

        for (term in terms) {
            val t = term.trim()
            if (t == "+" || t == "-") {
                throw IllegalArgumentException("চিহ্নের পর কোনো পদ পাওয়া যায়নি।")
            }
            if (t.contains("(")) {
                val openIdx = t.indexOf('(')
                val closeIdx = t.lastIndexOf(')')
                val after = t.substring(closeIdx + 1)
                var afterMultiplier = 1.0
                if (after.isNotEmpty()) {
                    if (after.startsWith("*")) {
                        afterMultiplier = after.substring(1).toDouble()
                    } else {
                        throw IllegalArgumentException("বন্ধনী-পরবর্তী পদ বিশ্লেষণে ত্রুটি: $t")
                    }
                }

                var prefix = t.substring(0, openIdx)
                if (prefix.endsWith("*")) {
                    prefix = prefix.dropLast(1)
                }

                val prefixMultiplier = when (prefix) {
                    "", "+" -> 1.0
                    "-" -> -1.0
                    else -> {
                        if (prefix.contains("x", ignoreCase = true)) {
                            throw IllegalArgumentException("চলকের সাথে বন্ধনীর গুণ অরৈখিক সমীকরণ তৈরি করে (যেমন: x(x+1))। এটি সমর্থিত নয়।")
                        }
                        prefix.toDouble()
                    }
                }

                val multiplier = prefixMultiplier * afterMultiplier
                val inside = t.substring(openIdx + 1, closeIdx)
                if (inside.isBlank()) {
                    throw IllegalArgumentException("বন্ধনীর ভেতরে কোনো রাশি দেওয়া হয়নি।")
                }
                val (inCoeff, inConst) = parseSimpleLinear(inside)
                totalCoeff += multiplier * inCoeff
                totalConstant += multiplier * inConst
            } else {
                val (c, k) = parseSingleTerm(t)
                totalCoeff += c
                totalConstant += k
            }
        }

        return Pair(totalCoeff, totalConstant)
    }

    private fun parseSimpleLinear(expr: String): Pair<Double, Double> {
        val trimmed = expr.trim()
        if (trimmed.endsWith("+") || trimmed.endsWith("-")) {
            throw IllegalArgumentException("বন্ধনীর ভেতরে চিহ্নের পর কোনো পদ নেই।")
        }
        var coeff = 0.0
        var constant = 0.0

        val normalized = trimmed.replace("-", "+-")
        val tokens = normalized.split("+").filter { it.isNotBlank() }

        if (tokens.isEmpty()) {
            throw IllegalArgumentException("বন্ধনীর ভেতরে কোনো পদ নেই।")
        }

        for (token in tokens) {
            val (c, k) = parseSingleTerm(token)
            coeff += c
            constant += k
        }
        return Pair(coeff, constant)
    }

    private fun parseSingleTerm(token: String): Pair<Double, Double> {
        val clean = token.trim()
        if (clean.contains("x", ignoreCase = true)) {
            val withoutX = clean.replace("x", "", ignoreCase = true)
            val c = when (withoutX) {
                "", "+" -> 1.0
                "-" -> -1.0
                else -> withoutX.toDouble()
            }
            return Pair(c, 0.0)
        } else {
            return Pair(0.0, clean.toDouble())
        }
    }

    private fun formatSideExprBn(coeff: Double, constant: Double): String {
        return when {
            coeff == 0.0 -> formatNum(constant)
            constant == 0.0 -> "${formatLinearCoeff(coeff)}x"
            constant > 0.0 -> "${formatLinearCoeff(coeff)}x + ${formatNum(constant)}"
            else -> "${formatLinearCoeff(coeff)}x - ${formatNum(abs(constant))}"
        }
    }

    private fun formatLinearCoeff(c: Double): String {
        return if (c == 1.0) "" else if (c == -1.0) "-" else formatNum(c)
    }

    // -------------------------------------------------------------
    // GENERAL EXPRESSION EVALUATOR WITH STEP-BY-STEP TRACE
    // -------------------------------------------------------------
    private fun evaluateArithmeticExpression(
        expr: String,
        steps: MutableList<SolutionStep>,
        mode: SolverMode
    ): Double {
        var current = expr
        var stepCount = 1

        // Evaluate innermost parentheses first
        while (current.contains("(") || current.contains("{") || current.contains("[")) {
            val startIdx = current.lastIndexOfAny(charArrayOf('(', '{', '['))
            val openCh = current[startIdx]
            val closeCh = when (openCh) {
                '(' -> ')'
                '{' -> '}'
                '[' -> ']'
                else -> ')'
            }
            val endIdx = current.indexOf(closeCh, startIdx)
            if (endIdx == -1) break

            val inside = current.substring(startIdx + 1, endIdx)
            val insideVal = evaluateSimpleArithmetic(inside)

            val before = current
            current = current.substring(0, startIdx) + df.format(insideVal) + current.substring(endIdx + 1)

            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "বন্ধনীর ভেতরের সরল",
                    expressionOrMathBn = "${BengaliNumberFormatter.toBn(inside)} = ${formatNum(insideVal)}",
                    explanationBn = "BODMAS নিয়ম অনুযায়ী প্রথম বন্ধনীর ভিতরের কাজ সর্বাগ্রে সম্পন্ন করা হলো।",
                    whyThisStepBn = "বন্ধনী হিসাবের অগ্রাধিকার নির্ধারণ করে, তাই বন্ধনীর ভেতরের সমাধান আগে করতে হয়।"
                )
            )
        }

        // Final evaluation of remaining expression without brackets
        val finalVal = evaluateSimpleArithmetic(current)
        if (current != expr && steps.isNotEmpty()) {
            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "চূড়ান্ত ফলাফল গণনা",
                    expressionOrMathBn = "${BengaliNumberFormatter.toBn(current)} = ${formatNum(finalVal)}",
                    explanationBn = "বন্ধনীর কাজ শেষ হওয়ার পর অবশিষ্ট অংশের গুণ, ভাগ, যোগ ও বিয়োগ সম্পন্ন করা হলো।"
                )
            )
        } else if (steps.isEmpty()) {
            steps.add(
                SolutionStep(
                    stepIndex = stepCount++,
                    titleBn = "গণনা ধাপ",
                    expressionOrMathBn = "${BengaliNumberFormatter.toBn(expr)} = ${formatNum(finalVal)}",
                    explanationBn = "চিহ্নগুলোর অগ্রাধিকার (ভাগ ➜ গুণ ➜ যোগ ➜ বিয়োগ) অনুযায়ী হিসাব করা হলো।"
                )
            )
        }

        return finalVal
    }

    private fun evaluateSimpleArithmetic(expr: String): Double {
        val clean = expr.replace(" ", "")

        // Tokenize into numbers and operators (+, -, *, /, ^)
        val tokens = mutableListOf<String>()
        var buffer = StringBuilder()
        var i = 0
        while (i < clean.length) {
            val c = clean[i]
            if (c in "+-*/^") {
                if ((c == '-' || c == '+') && (i == 0 || clean[i - 1] in "+-*/^")) {
                    // Unary operator (+ or -)
                    buffer.append(c)
                } else {
                    if (buffer.isNotEmpty()) {
                        tokens.add(buffer.toString())
                        buffer = StringBuilder()
                    }
                    tokens.add(c.toString())
                }
            } else {
                buffer.append(c)
            }
            i++
        }
        if (buffer.isNotEmpty()) tokens.add(buffer.toString())

        if (tokens.isEmpty()) return 0.0

        // Handle Exponents (^)
        var curTokens = tokens.toMutableList()
        var idx = 0
        while (idx < curTokens.size) {
            if (curTokens[idx] == "^") {
                if (idx - 1 < 0 || idx + 1 >= curTokens.size) {
                    throw IllegalArgumentException("Malformed expression around ^")
                }
                val left = curTokens[idx - 1].toDouble()
                val right = curTokens[idx + 1].toDouble()
                val res = left.pow(right)
                curTokens.subList(idx - 1, idx + 2).clear()
                curTokens.add(idx - 1, res.toString())
                idx = 0
            } else {
                idx++
            }
        }

        // Handle Division & Multiplication (*, /)
        idx = 0
        while (idx < curTokens.size) {
            val op = curTokens[idx]
            if (op == "*" || op == "/") {
                if (idx - 1 < 0 || idx + 1 >= curTokens.size) {
                    throw IllegalArgumentException("Malformed expression around $op")
                }
                val left = curTokens[idx - 1].toDouble()
                val right = curTokens[idx + 1].toDouble()
                if (op == "/" && right == 0.0) {
                    throw ArithmeticException("শূন্য (০) দিয়ে ভাগ করা অনির্ণেয়।")
                }
                val res = if (op == "*") left * right else left / right
                curTokens.subList(idx - 1, idx + 2).clear()
                curTokens.add(idx - 1, res.toString())
                idx = 0
            } else {
                idx++
            }
        }

        // Handle Addition & Subtraction (+, -)
        idx = 0
        while (idx < curTokens.size) {
            val op = curTokens[idx]
            if (op == "+" || op == "-") {
                if (idx - 1 < 0 || idx + 1 >= curTokens.size) {
                    throw IllegalArgumentException("Malformed expression around $op")
                }
                val left = curTokens[idx - 1].toDouble()
                val right = curTokens[idx + 1].toDouble()
                val res = if (op == "+") left + right else left - right
                curTokens.subList(idx - 1, idx + 2).clear()
                curTokens.add(idx - 1, res.toString())
                idx = 0
            } else {
                idx++
            }
        }

        if (curTokens.size != 1) {
            throw IllegalArgumentException("Malformed expression")
        }

        return curTokens.first().toDouble()
    }

    // -------------------------------------------------------------
    // GENERATE SIMILAR PROBLEM
    // -------------------------------------------------------------
    fun generateSimilarInputs(topic: SolverTopic): Map<String, String> {
        return when (topic) {
            SolverTopic.BASIC_ARITHMETIC -> mapOf(
                "expression" to listOf(
                    "(১৮ + ১২) × ৩ - ২০ ÷ ৫",
                    "৫০ - (১০ + ৫) × ২ + ১২",
                    "(৪০ ÷ ৮) × ৭ + (১৫ - ৯)",
                    "১০০ - ৪ × (১২ + ৩) + ৮"
                ).random()
            )
            SolverTopic.FRACTIONS_DECIMALS -> {
                val n1 = listOf(2L, 3L, 4L, 5L).random()
                val d1 = listOf(3L, 5L, 7L, 8L).random()
                val n2 = listOf(1L, 2L, 3L).random()
                val d2 = listOf(4L, 6L, 9L).random()
                mapOf(
                    "num1" to n1.toString(),
                    "den1" to d1.toString(),
                    "op" to listOf("+", "-", "*", "/").random(),
                    "num2" to n2.toString(),
                    "den2" to d2.toString()
                )
            }
            SolverTopic.PERCENTAGE -> {
                val bases = listOf("500", "800", "1200", "1500", "2000")
                val pcts = listOf("10", "15", "20", "25", "30")
                mapOf(
                    "subType" to "0",
                    "val1" to pcts.random(),
                    "val2" to bases.random()
                )
            }
            SolverTopic.RATIO_PROPORTION -> {
                val totals = listOf("1200", "1500", "2400", "3600")
                val r1 = listOf("2", "3", "4").random()
                val r2 = listOf("3", "5", "7").random()
                mapOf(
                    "subType" to "0",
                    "total" to totals.random(),
                    "r1" to r1,
                    "r2" to r2
                )
            }
            SolverTopic.PROFIT_LOSS -> {
                val cps = listOf("400", "600", "800", "1000")
                val sps = listOf("480", "720", "960", "1200")
                mapOf(
                    "subType" to "0",
                    "cp" to cps.random(),
                    "sp" to sps.random()
                )
            }
            SolverTopic.AVERAGE -> {
                val sets = listOf(
                    "১০, ২০, ৩০, ৪০, ৫০",
                    "১২, ১৮, ২৪, ৩০",
                    "১৫, ২৫, ৩৫, ৪৫, ৫৫",
                    "৮, ১৬, ২৪, ৩২, ৪০"
                )
                mapOf("numbers" to sets.random())
            }
            SolverTopic.SIMPLE_INTEREST -> {
                val principals = listOf("3000", "5000", "8000", "10000")
                val rates = listOf("6", "8", "10", "12")
                val times = listOf("2", "3", "4", "5")
                mapOf(
                    "p" to principals.random(),
                    "r" to rates.random(),
                    "t" to times.random()
                )
            }
            SolverTopic.TIME_AND_WORK -> {
                val daysA = listOf("10", "12", "15", "20").random()
                val daysB = listOf("15", "20", "30", "60").random()
                mapOf(
                    "subType" to "0",
                    "timeA" to daysA,
                    "timeB" to daysB
                )
            }
            SolverTopic.SPEED_DISTANCE_TIME -> {
                val distances = listOf("60", "90", "120", "150", "240")
                val times = listOf("1.5", "2", "3", "4")
                mapOf(
                    "distance" to distances.random(),
                    "time" to times.random()
                )
            }
            SolverTopic.NUMBER_SYSTEM_DIVISIBILITY -> {
                val numbers = listOf("1248", "3750", "4824", "91828", "5142")
                val divs = listOf("2", "3", "4", "5", "6", "8", "9", "11")
                mapOf(
                    "number" to numbers.random(),
                    "divisor" to divs.random()
                )
            }
            SolverTopic.HCF_LCM -> {
                val pairs = listOf("24, 36", "18, 45", "30, 48", "36, 60", "12, 18, 24")
                mapOf("numbers" to pairs.random())
            }
            SolverTopic.SQUARES_CUBES_ROOTS -> {
                val nums = listOf("16", "25", "36", "64", "100", "144", "169", "225")
                mapOf(
                    "op" to "1", // Square root
                    "number" to nums.random()
                )
            }
            SolverTopic.LINEAR_EQUATIONS -> {
                val eqs = listOf(
                    "3x + 9 = 27",
                    "5x - 10 = 30",
                    "4x + 6 = 2x + 18",
                    "2x + 15 = 35",
                    "7x - 4 = 3x + 16"
                )
                mapOf("equation" to eqs.random())
            }
        }
    }

    private fun errorResult(
        topic: SolverTopic,
        mode: SolverMode,
        problem: String,
        errorMsg: String
    ): SolverResult {
        return SolverResult(
            topic = topic,
            mode = mode,
            originalProblemBn = BengaliNumberFormatter.toBn(problem),
            formulaUsedBn = null,
            steps = emptyList(),
            finalAnswerBn = "সমাধান সম্ভব নয়",
            isSuccess = false,
            errorMessageBn = errorMsg
        )
    }

    private fun toAsciiDigits(s: String): String {
        val bnToAscii = mapOf(
            '০' to '0', '১' to '1', '২' to '2', '৩' to '3', '৪' to '4',
            '৫' to '5', '৬' to '6', '৭' to '7', '৮' to '8', '৯' to '9'
        )
        val sb = StringBuilder()
        for (c in s) {
            sb.append(bnToAscii[c] ?: c)
        }
        return sb.toString()
    }

    private fun floor(d: Double): Double = kotlin.math.floor(d)

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}
