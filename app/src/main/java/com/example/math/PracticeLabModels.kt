package com.example.math

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.random.Random

/**
 * The 8 core practice categories for the Math Practice Lab.
 */
enum class PracticeCategory(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val primaryColor: Color,
    val containerColor: Color
) {
    ADDITION_SUBTRACTION(
        id = "add_sub",
        titleBn = "যোগ ও বিয়োগ",
        titleEn = "Addition & Subtraction",
        subtitleBn = "সহজ থেকে জটিল দুই বা ততোধিক সংখ্যার যোগ-বিয়োগ",
        primaryColor = Color(0xFF2563EB), // Blue
        containerColor = Color(0xFFDBEAFE)
    ),
    MULTIPLICATION_DIVISION(
        id = "mul_div",
        titleBn = "গুণ ও ভাগ",
        titleEn = "Multiplication & Division",
        subtitleBn = "দ্রুত গণনা, নামতার প্রয়োগ ও নির্ভুল ভাগফল",
        primaryColor = Color(0xFF059669), // Emerald
        containerColor = Color(0xFFD1FAE5)
    ),
    FRACTIONS(
        id = "fractions",
        titleBn = "ভগ্নাংশ",
        titleEn = "Fractions",
        subtitleBn = "সমহর, বিষমহর, লঘিষ্ঠ রূপ ও মিশ্র ভগ্নাংশের হিসাব",
        primaryColor = Color(0xFFD97706), // Amber
        containerColor = Color(0xFFFEF3C7)
    ),
    PERCENTAGES(
        id = "percentages",
        titleBn = "শতকরা",
        titleEn = "Percentages",
        subtitleBn = "শতাংশ নির্ণয়, শতকরা বৃদ্ধি/হ্রাস ও ব্যবহারিক সমস্যা",
        primaryColor = Color(0xFF7C3AED), // Purple
        containerColor = Color(0xFFEDE9FE)
    ),
    SQUARES_ROOTS(
        id = "squares_roots",
        titleBn = "বর্গ ও বর্গমূল",
        titleEn = "Squares & Square Roots",
        subtitleBn = "পূর্ণবর্গ সংখ্যা ও তাদের বর্গমূলের দ্রুত হিসাব",
        primaryColor = Color(0xFFE11D48), // Rose
        containerColor = Color(0xFFFFE4E6)
    ),
    CUBES_ROOTS(
        id = "cubes_roots",
        titleBn = "ঘন ও ঘনমূল",
        titleEn = "Cubes & Cube Roots",
        subtitleBn = "ঘনফল নির্ণয় ও নিখুঁত ঘনমূল যাচাই",
        primaryColor = Color(0xFF0891B2), // Cyan
        containerColor = Color(0xFFCFFAFE)
    ),
    BASIC_ALGEBRA(
        id = "basic_algebra",
        titleBn = "প্রাথমিক বীজগণিত",
        titleEn = "Basic Algebra",
        subtitleBn = "এক চলকবিশিষ্ট সমীকরণ ও বীজগণিতীয় রাশির মান",
        primaryColor = Color(0xFF4F46E5), // Indigo
        containerColor = Color(0xFFE0E7FF)
    ),
    NUMBER_OPERATIONS(
        id = "number_operations",
        titleBn = "মিশ্র প্রক্রিয়া ও BODMAS",
        titleEn = "Number Operations & BODMAS",
        subtitleBn = "বন্ধনী, এর, ভাগ, গুণ, যোগ ও বিয়োগের সঠিক ক্রম",
        primaryColor = Color(0xFFB45309), // Warm Brown
        containerColor = Color(0xFFFDE68A)
    );

    fun getIcon(): ImageVector = when (this) {
        ADDITION_SUBTRACTION -> Icons.Default.AddCircle
        MULTIPLICATION_DIVISION -> Icons.Default.Calculate
        FRACTIONS -> Icons.Default.PieChart
        PERCENTAGES -> Icons.Default.Percent
        SQUARES_ROOTS -> Icons.Default.Straighten
        CUBES_ROOTS -> Icons.Default.ViewInAr
        BASIC_ALGEBRA -> Icons.Default.Functions
        NUMBER_OPERATIONS -> Icons.Default.Category
    }
}

/**
 * Three levels of practice difficulty.
 */
enum class PracticeDifficulty(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val badgeColor: Color
) {
    EASY("easy", "সহজ", "Easy", Color(0xFF10B981)),
    MEDIUM("medium", "মাঝারি", "Medium", Color(0xFFF59E0B)),
    HARD("hard", "কঠিন", "Hard", Color(0xFFEF4444))
}

/**
 * Model representing an individual question generated for practice.
 */
data class PracticeQuestion(
    val id: String,
    val category: PracticeCategory,
    val difficulty: PracticeDifficulty,
    val questionBn: String,
    val expression: String,
    val optionsBn: List<String>,
    val correctIndex: Int,
    val correctAnswerBn: String,
    val stepByStepExplanationBn: List<String>,
    val tipBn: String?
)

/**
 * State holding an ongoing or completed practice lab session.
 */
data class PracticeSessionState(
    val category: PracticeCategory = PracticeCategory.ADDITION_SUBTRACTION,
    val difficulty: PracticeDifficulty = PracticeDifficulty.EASY,
    val questions: List<PracticeQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val customInputText: String = "",
    val isAnswerSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val startTimeMs: Long = 0L,
    val totalTimeSeconds: Long = 0L,
    val isFinished: Boolean = false,
    val userAnswers: Map<Int, Int> = emptyMap(),
    val isReviewMode: Boolean = false
)

/**
 * Generator engine producing educational, mathematically verified questions.
 */
object PracticeLabGenerator {

    fun generateSession(
        category: PracticeCategory,
        difficulty: PracticeDifficulty,
        questionCount: Int = 10
    ): List<PracticeQuestion> {
        val questions = mutableListOf<PracticeQuestion>()
        val seenExpressions = mutableSetOf<String>()
        var attempts = 0

        while (questions.size < questionCount && attempts < questionCount * 6) {
            attempts++
            val q = when (category) {
                PracticeCategory.ADDITION_SUBTRACTION -> generateAddSubQuestion(questions.size + 1, difficulty)
                PracticeCategory.MULTIPLICATION_DIVISION -> generateMulDivQuestion(questions.size + 1, difficulty)
                PracticeCategory.FRACTIONS -> generateFractionQuestion(questions.size + 1, difficulty)
                PracticeCategory.PERCENTAGES -> generatePercentageQuestion(questions.size + 1, difficulty)
                PracticeCategory.SQUARES_ROOTS -> generateSquareRootQuestion(questions.size + 1, difficulty)
                PracticeCategory.CUBES_ROOTS -> generateCubeRootQuestion(questions.size + 1, difficulty)
                PracticeCategory.BASIC_ALGEBRA -> generateAlgebraQuestion(questions.size + 1, difficulty)
                PracticeCategory.NUMBER_OPERATIONS -> generateBodmasQuestion(questions.size + 1, difficulty)
            }

            if (!seenExpressions.contains(q.expression)) {
                seenExpressions.add(q.expression)
                questions.add(q)
            }
        }

        return questions
    }

    // 1. ADDITION & SUBTRACTION
    private fun generateAddSubQuestion(index: Int, difficulty: PracticeDifficulty): PracticeQuestion {
        return when (difficulty) {
            PracticeDifficulty.EASY -> {
                val isAdd = Random.nextBoolean()
                if (isAdd) {
                    val a = Random.nextInt(15, 90)
                    val b = Random.nextInt(12, 85)
                    val ans = a + b
                    buildStandardIntQuestion(
                        id = "add_sub_easy_$index",
                        category = PracticeCategory.ADDITION_SUBTRACTION,
                        difficulty = difficulty,
                        questionBn = "নিচের যোগফলটি কত?",
                        expression = "${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "প্রথম সংখ্যা: ${BengaliNumberFormatter.toBn(a)}",
                            "দ্বিতীয় সংখ্যা: ${BengaliNumberFormatter.toBn(b)}",
                            "যোগফল = ${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "প্রথমে দশকের ঘরের সংখ্যাগুলো যোগ করে তারপর এককের ঘর যোগ করলে দ্রুত উত্তর পাওয়া যায়।"
                    )
                } else {
                    val a = Random.nextInt(40, 150)
                    val b = Random.nextInt(12, a - 5)
                    val ans = a - b
                    buildStandardIntQuestion(
                        id = "add_sub_easy_$index",
                        category = PracticeCategory.ADDITION_SUBTRACTION,
                        difficulty = difficulty,
                        questionBn = "নিচের বিয়োগফলটি কত?",
                        expression = "${BengaliNumberFormatter.toBn(a)} − ${BengaliNumberFormatter.toBn(b)} = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "বিয়োগ্য সংখ্যা: ${BengaliNumberFormatter.toBn(a)}",
                            "বিয়োগক: ${BengaliNumberFormatter.toBn(b)}",
                            "বিয়োগফল = ${BengaliNumberFormatter.toBn(a)} − ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "প্রথমে কাছাকাছি দশ বা একশ সংখ্যা বিয়োগ করে অতিরিক্ত মান সমন্বয় করুন।"
                    )
                }
            }
            PracticeDifficulty.MEDIUM -> {
                val a = Random.nextInt(120, 650)
                val b = Random.nextInt(110, 480)
                val c = Random.nextInt(35, 180)
                val isAddMix = Random.nextBoolean()
                if (isAddMix) {
                    val ans = a + b - c
                    buildStandardIntQuestion(
                        id = "add_sub_med_$index",
                        category = PracticeCategory.ADDITION_SUBTRACTION,
                        difficulty = difficulty,
                        questionBn = "সরল করো:",
                        expression = "${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} − ${BengaliNumberFormatter.toBn(c)} = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "ধাপ ১ (যোগ): ${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(a + b)}",
                            "ধাপ ২ (বিয়োগ): ${BengaliNumberFormatter.toBn(a + b)} − ${BengaliNumberFormatter.toBn(c)} = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "প্রথমে যোগ সম্পন্ন করুন, তারপর প্রাপ্ত মান থেকে বিয়োগ করুন।"
                    )
                } else {
                    val large = Random.nextInt(600, 1400)
                    val sub = Random.nextInt(150, 450)
                    val ans = large - sub
                    buildStandardIntQuestion(
                        id = "add_sub_med_$index",
                        category = PracticeCategory.ADDITION_SUBTRACTION,
                        difficulty = difficulty,
                        questionBn = "বিয়োগফল নির্ণয় করো:",
                        expression = "${BengaliNumberFormatter.toBn(large)} − ${BengaliNumberFormatter.toBn(sub)} = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "হাতে রেখে বিয়োগের নিয়ম অনুসারে:",
                            "${BengaliNumberFormatter.toBn(large)} − ${BengaliNumberFormatter.toBn(sub)} = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "একক, দশক ও শতকের ঘরের ধাপে ধাপে ধার নেওয়ার নিয়ম মনে রাখুন।"
                    )
                }
            }
            PracticeDifficulty.HARD -> {
                val a = Random.nextInt(1200, 4800)
                val b = Random.nextInt(1100, 3900)
                val c = Random.nextInt(900, 2500)
                val ans = a + b - c
                buildStandardIntQuestion(
                    id = "add_sub_hard_$index",
                    category = PracticeCategory.ADDITION_SUBTRACTION,
                    difficulty = difficulty,
                    questionBn = "চার অঙ্কের সংখ্যার সরলফল নির্ণয় করো:",
                    expression = "${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} − ${BengaliNumberFormatter.toBn(c)} = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "ধাপ ১ (যোগ): ${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(a + b)}",
                        "ধাপ ২ (বিয়োগ): ${BengaliNumberFormatter.toBn(a + b)} − ${BengaliNumberFormatter.toBn(c)} = ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "হাজার ও শতকের অঙ্কগুলো আলাদা হিসেবে রাখলে নির্ভুল হিসাব সম্ভব।"
                )
            }
        }
    }

    // 2. MULTIPLICATION & DIVISION
    private fun generateMulDivQuestion(index: Int, difficulty: PracticeDifficulty): PracticeQuestion {
        return when (difficulty) {
            PracticeDifficulty.EASY -> {
                val isMul = Random.nextBoolean()
                if (isMul) {
                    val a = Random.nextInt(11, 25)
                    val b = Random.nextInt(3, 10)
                    val ans = a * b
                    buildStandardIntQuestion(
                        id = "mul_div_easy_$index",
                        category = PracticeCategory.MULTIPLICATION_DIVISION,
                        difficulty = difficulty,
                        questionBn = "গুণফল কত হবে?",
                        expression = "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)} = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(a)} কে ${BengaliNumberFormatter.toBn(b)} দিয়ে গুণ করতে হবে।",
                            "গুণফল = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "নামতা মনে রাখলে এই হিসাবগুলো তাৎক্ষণিক সমাধান করা যায়।"
                    )
                } else {
                    val divisor = Random.nextInt(3, 10)
                    val quotient = Random.nextInt(5, 25)
                    val dividend = divisor * quotient
                    buildStandardIntQuestion(
                        id = "mul_div_easy_$index",
                        category = PracticeCategory.MULTIPLICATION_DIVISION,
                        difficulty = difficulty,
                        questionBn = "ভাগফল কত হবে?",
                        expression = "${BengaliNumberFormatter.toBn(dividend)} ÷ ${BengaliNumberFormatter.toBn(divisor)} = ?",
                        correctAns = quotient,
                        explanation = listOf(
                            "ভাজ্য: ${BengaliNumberFormatter.toBn(dividend)}, ভাজক: ${BengaliNumberFormatter.toBn(divisor)}",
                            "ভাগফল = ${BengaliNumberFormatter.toBn(dividend)} ÷ ${BengaliNumberFormatter.toBn(divisor)} = ${BengaliNumberFormatter.toBn(quotient)}"
                        ),
                        tip = "ভাগ হলো গুণের বিপরীত প্রক্রিয়া। ভাজকের নামতা স্মরণ করুন।"
                    )
                }
            }
            PracticeDifficulty.MEDIUM -> {
                val isMul = Random.nextBoolean()
                if (isMul) {
                    val a = Random.nextInt(15, 45)
                    val b = Random.nextInt(11, 25)
                    val ans = a * b
                    buildStandardIntQuestion(
                        id = "mul_div_med_$index",
                        category = PracticeCategory.MULTIPLICATION_DIVISION,
                        difficulty = difficulty,
                        questionBn = "গুণফল নির্ণয় করো:",
                        expression = "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)} = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(b)} কে বিভক্ত করে: ${BengaliNumberFormatter.toBn(a)} × (${BengaliNumberFormatter.toBn(10)} + ${BengaliNumberFormatter.toBn(b - 10)})",
                            "= ${BengaliNumberFormatter.toBn(a * 10)} + ${BengaliNumberFormatter.toBn(a * (b - 10))} = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "দুই অঙ্কের গুণকে ১০ বা ২০ এর গুণিতকে ভেঙে সহজে করা যায়।"
                    )
                } else {
                    val divisor = Random.nextInt(11, 20)
                    val quotient = Random.nextInt(12, 35)
                    val dividend = divisor * quotient
                    buildStandardIntQuestion(
                        id = "mul_div_med_$index",
                        category = PracticeCategory.MULTIPLICATION_DIVISION,
                        difficulty = difficulty,
                        questionBn = "ভাগফল নির্ণয় করো:",
                        expression = "${BengaliNumberFormatter.toBn(dividend)} ÷ ${BengaliNumberFormatter.toBn(divisor)} = ?",
                        correctAns = quotient,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(dividend)} ÷ ${BengaliNumberFormatter.toBn(divisor)}",
                            "নিঃশেষে বিভাজ্য, ভাগফল = ${BengaliNumberFormatter.toBn(quotient)}"
                        ),
                        tip = "ভাজক দিয়ে ভাগ করার সময় প্রথম দুই অঙ্কের মান পরীক্ষা করুন।"
                    )
                }
            }
            PracticeDifficulty.HARD -> {
                val a = Random.nextInt(25, 75)
                val b = Random.nextInt(22, 65)
                val ans = a * b
                buildStandardIntQuestion(
                    id = "mul_div_hard_$index",
                    category = PracticeCategory.MULTIPLICATION_DIVISION,
                    difficulty = difficulty,
                    questionBn = "বৃহৎ সংখ্যার গুণফল নির্ণয় করো:",
                    expression = "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)} = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "ধাপে ধাপে গুণ:",
                        "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b % 10)} = ${BengaliNumberFormatter.toBn(a * (b % 10))}",
                        "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn((b / 10) * 10)} = ${BengaliNumberFormatter.toBn(a * (b / 10) * 10)}",
                        "মোট গুণফল = ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "খাতায় লিখে অথবা বৈদিক নিয়মে কোণাকুণি গুণ করে দ্রুত সমাধান করা যায়।"
                )
            }
        }
    }

    // 3. FRACTIONS
    private fun generateFractionQuestion(index: Int, difficulty: PracticeDifficulty): PracticeQuestion {
        return when (difficulty) {
            PracticeDifficulty.EASY -> {
                // Like fractions addition or subtraction
                val denom = listOf(5, 7, 8, 9, 11).random()
                val isAdd = Random.nextBoolean()
                if (isAdd) {
                    val num1 = Random.nextInt(1, denom - 2)
                    val num2 = Random.nextInt(1, denom - num1)
                    val sum = num1 + num2
                    val correctBn = "${BengaliNumberFormatter.toBn(sum)}/${BengaliNumberFormatter.toBn(denom)}"
                    val opts = generateFractionOptions(sum, denom)
                    val shuffledOpts = opts.shuffled()
                    PracticeQuestion(
                        id = "frac_easy_$index",
                        category = PracticeCategory.FRACTIONS,
                        difficulty = difficulty,
                        questionBn = "সমহর ভগ্নাংশের যোগফল কত?",
                        expression = "${BengaliNumberFormatter.toBn(num1)}/${BengaliNumberFormatter.toBn(denom)} + ${BengaliNumberFormatter.toBn(num2)}/${BengaliNumberFormatter.toBn(denom)} = ?",
                        optionsBn = shuffledOpts,
                        correctIndex = shuffledOpts.indexOf(correctBn),
                        correctAnswerBn = correctBn,
                        stepByStepExplanationBn = listOf(
                            "যেহেতু হর একই (${BengaliNumberFormatter.toBn(denom)}), তাই লব দুটি সরাসরি যোগ হবে।",
                            "লবের যোগফল = ${BengaliNumberFormatter.toBn(num1)} + ${BengaliNumberFormatter.toBn(num2)} = ${BengaliNumberFormatter.toBn(sum)}",
                            "ফলাফল = ${BengaliNumberFormatter.toBn(sum)}/${BengaliNumberFormatter.toBn(denom)}"
                        ),
                        tipBn = "হর একই থাকলে শুধু লবগুলোর যোগ করলেই হয়।"
                    )
                } else {
                    val num1 = Random.nextInt(4, denom)
                    val num2 = Random.nextInt(1, num1)
                    val diff = num1 - num2
                    val correctBn = "${BengaliNumberFormatter.toBn(diff)}/${BengaliNumberFormatter.toBn(denom)}"
                    val opts = generateFractionOptions(diff, denom)
                    val shuffledOpts = opts.shuffled()
                    PracticeQuestion(
                        id = "frac_easy_$index",
                        category = PracticeCategory.FRACTIONS,
                        difficulty = difficulty,
                        questionBn = "সমহর ভগ্নাংশের বিয়োগফল কত?",
                        expression = "${BengaliNumberFormatter.toBn(num1)}/${BengaliNumberFormatter.toBn(denom)} − ${BengaliNumberFormatter.toBn(num2)}/${BengaliNumberFormatter.toBn(denom)} = ?",
                        optionsBn = shuffledOpts,
                        correctIndex = shuffledOpts.indexOf(correctBn),
                        correctAnswerBn = correctBn,
                        stepByStepExplanationBn = listOf(
                            "হর অপরিবর্তিত থাকবে: ${BengaliNumberFormatter.toBn(denom)}",
                            "লবের বিয়োগফল = ${BengaliNumberFormatter.toBn(num1)} − ${BengaliNumberFormatter.toBn(num2)} = ${BengaliNumberFormatter.toBn(diff)}",
                            "ফলাফল = ${BengaliNumberFormatter.toBn(diff)}/${BengaliNumberFormatter.toBn(denom)}"
                        ),
                        tipBn = "হর অপরিবর্তিত রেখে প্রথম লব থেকে দ্বিতীয় লব বাদ দিন।"
                    )
                }
            }
            PracticeDifficulty.MEDIUM -> {
                // Unlike fractions with simple LCM
                val pairs = listOf(
                    Triple(2, 3, 6),
                    Triple(3, 4, 12),
                    Triple(2, 5, 10),
                    Triple(4, 5, 20),
                    Triple(3, 5, 15)
                ).random()
                val d1 = pairs.first
                val d2 = pairs.second
                val lcm = pairs.third
                val n1 = 1
                val n2 = 1
                val sumNum = (n1 * (lcm / d1)) + (n2 * (lcm / d2))
                val correctBn = "${BengaliNumberFormatter.toBn(sumNum)}/${BengaliNumberFormatter.toBn(lcm)}"
                val opts = generateFractionOptions(sumNum, lcm)
                val shuffledOpts = opts.shuffled()
                PracticeQuestion(
                    id = "frac_med_$index",
                    category = PracticeCategory.FRACTIONS,
                    difficulty = difficulty,
                    questionBn = "ভগ্নাংশের যোগফল নির্ণয় করো:",
                    expression = "${BengaliNumberFormatter.toBn(n1)}/${BengaliNumberFormatter.toBn(d1)} + ${BengaliNumberFormatter.toBn(n2)}/${BengaliNumberFormatter.toBn(d2)} = ?",
                    optionsBn = shuffledOpts,
                    correctIndex = shuffledOpts.indexOf(correctBn),
                    correctAnswerBn = correctBn,
                    stepByStepExplanationBn = listOf(
                        "হরদ্বয় ${BengaliNumberFormatter.toBn(d1)} ও ${BengaliNumberFormatter.toBn(d2)} এর লসাগু = ${BengaliNumberFormatter.toBn(lcm)}",
                        "রূপান্তরিত লব: (${BengaliNumberFormatter.toBn(n1)} × ${BengaliNumberFormatter.toBn(lcm / d1)}) + (${BengaliNumberFormatter.toBn(n2)} × ${BengaliNumberFormatter.toBn(lcm / d2)})",
                        "= ${BengaliNumberFormatter.toBn(n1 * (lcm / d1))} + ${BengaliNumberFormatter.toBn(n2 * (lcm / d2))} = ${BengaliNumberFormatter.toBn(sumNum)}",
                        "ফলাফল = ${BengaliNumberFormatter.toBn(sumNum)}/${BengaliNumberFormatter.toBn(lcm)}"
                    ),
                    tipBn = "হরগুলোর লসাগু বের করে সমহর ভগ্নাংশে রূপান্তর করুন।"
                )
            }
            PracticeDifficulty.HARD -> {
                // Fraction multiplication
                val n1 = Random.nextInt(2, 6)
                val d1 = Random.nextInt(5, 9)
                val n2 = Random.nextInt(3, 7)
                val d2 = Random.nextInt(5, 11)
                val rawN = n1 * n2
                val rawD = d1 * d2
                val gcd = gcd(rawN, rawD)
                val simN = rawN / gcd
                val simD = rawD / gcd
                val correctBn = "${BengaliNumberFormatter.toBn(simN)}/${BengaliNumberFormatter.toBn(simD)}"
                val opts = generateFractionOptions(simN, simD)
                val shuffledOpts = opts.shuffled()
                PracticeQuestion(
                    id = "frac_hard_$index",
                    category = PracticeCategory.FRACTIONS,
                    difficulty = difficulty,
                    questionBn = "ভগ্নাংশের গুণফল (লঘিষ্ঠ আকারে) নির্ণয় করো:",
                    expression = "${BengaliNumberFormatter.toBn(n1)}/${BengaliNumberFormatter.toBn(d1)} × ${BengaliNumberFormatter.toBn(n2)}/${BengaliNumberFormatter.toBn(d2)} = ?",
                    optionsBn = shuffledOpts,
                    correctIndex = shuffledOpts.indexOf(correctBn),
                    correctAnswerBn = correctBn,
                    stepByStepExplanationBn = listOf(
                        "লবের গুণফল = ${BengaliNumberFormatter.toBn(n1)} × ${BengaliNumberFormatter.toBn(n2)} = ${BengaliNumberFormatter.toBn(rawN)}",
                        "হরের গুণফল = ${BengaliNumberFormatter.toBn(d1)} × ${BengaliNumberFormatter.toBn(d2)} = ${BengaliNumberFormatter.toBn(rawD)}",
                        "গসাগু ${BengaliNumberFormatter.toBn(gcd)} দিয়ে ভাগ করে লঘিষ্ঠ রূপ: ${BengaliNumberFormatter.toBn(simN)}/${BengaliNumberFormatter.toBn(simD)}"
                    ),
                    tipBn = "গুণের ক্ষেত্রে লবের সাথে লব এবং হরের সাথে হর সরাসরি গুণ হয়।"
                )
            }
        }
    }

    // 4. PERCENTAGES
    private fun generatePercentageQuestion(index: Int, difficulty: PracticeDifficulty): PracticeQuestion {
        return when (difficulty) {
            PracticeDifficulty.EASY -> {
                val base = listOf(100, 200, 300, 400, 500, 600, 800, 1000).random()
                val pct = listOf(5, 10, 20, 25, 50).random()
                val ans = (base * pct) / 100
                buildStandardIntQuestion(
                    id = "pct_easy_$index",
                    category = PracticeCategory.PERCENTAGES,
                    difficulty = difficulty,
                    questionBn = "শতকরা মান কত?",
                    expression = "${BengaliNumberFormatter.toBn(base)} এর ${BengaliNumberFormatter.toBn(pct)}% = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "${BengaliNumberFormatter.toBn(pct)}% মানে প্রতি ১০০-তে ${BengaliNumberFormatter.toBn(pct)}",
                        "হিসাব: (${BengaliNumberFormatter.toBn(base)} × ${BengaliNumberFormatter.toBn(pct)}) ÷ ১০০",
                        "= ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "১০% বের করতে সংখ্যাটির ডানদিক থেকে এক ঘর দশমিক বামে সরান; ২০% এর জন্য তার দ্বিগুণ করুন।"
                )
            }
            PracticeDifficulty.MEDIUM -> {
                val base = listOf(240, 360, 450, 750, 840, 950).random()
                val pct = listOf(15, 20, 30, 40, 60).random()
                val ans = (base * pct) / 100
                buildStandardIntQuestion(
                    id = "pct_med_$index",
                    category = PracticeCategory.PERCENTAGES,
                    difficulty = difficulty,
                    questionBn = "মান নির্ণয় করো:",
                    expression = "${BengaliNumberFormatter.toBn(base)} এর ${BengaliNumberFormatter.toBn(pct)}% = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "শতকরা রূপ: (${BengaliNumberFormatter.toBn(base)} × ${BengaliNumberFormatter.toBn(pct)}) ÷ ১০০",
                        "= ${BengaliNumberFormatter.toBn(base * pct)} ÷ ১০০ = ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "১৫% বের করতে প্রথমে ১০% বের করুন, তারপর তার অর্ধেক (৫%) যোগ করুন।"
                )
            }
            PracticeDifficulty.HARD -> {
                val cost = listOf(400, 500, 800, 1200, 1500).random()
                val profitPct = listOf(15, 20, 25, 30).random()
                val profit = (cost * profitPct) / 100
                val sellingPrice = cost + profit
                buildStandardIntQuestion(
                    id = "pct_hard_$index",
                    category = PracticeCategory.PERCENTAGES,
                    difficulty = difficulty,
                    questionBn = "একটি দ্রব্যের ক্রয়মূল্য ${BengaliNumberFormatter.toBn(cost)} টাকা এবং লাভ ${BengaliNumberFormatter.toBn(profitPct)}% হলে বিক্রয়মূল্য কত?",
                    expression = "${BengaliNumberFormatter.toBn(cost)} টাকার ক্রয়মূল্যে ${BengaliNumberFormatter.toBn(profitPct)}% লাভে বিক্রয়মূল্য = ?",
                    correctAns = sellingPrice,
                    explanation = listOf(
                        "লাভ = ${BengaliNumberFormatter.toBn(cost)} এর ${BengaliNumberFormatter.toBn(profitPct)}% = ${BengaliNumberFormatter.toBn(profit)} টাকা",
                        "বিক্রয়মূল্য = ক্রয়মূল্য + লাভ = ${BengaliNumberFormatter.toBn(cost)} + ${BengaliNumberFormatter.toBn(profit)} = ${BengaliNumberFormatter.toBn(sellingPrice)} টাকা"
                    ),
                    tip = "বিক্রয়মূল্য = ক্রয়মূল্য × (১ + লাভ% / ১০০)।"
                )
            }
        }
    }

    // 5. SQUARES & SQUARE ROOTS
    private fun generateSquareRootQuestion(index: Int, difficulty: PracticeDifficulty): PracticeQuestion {
        return when (difficulty) {
            PracticeDifficulty.EASY -> {
                val n = Random.nextInt(4, 16) // 4 to 15
                val isRoot = Random.nextBoolean()
                if (isRoot) {
                    val sq = n * n
                    buildStandardIntQuestion(
                        id = "sq_easy_$index",
                        category = PracticeCategory.SQUARES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "বর্গমূল নির্ণয় করো:",
                        expression = "√${BengaliNumberFormatter.toBn(sq)} = ?",
                        correctAns = n,
                        explanation = listOf(
                            "যেহেতু ${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} = ${BengaliNumberFormatter.toBn(sq)}",
                            "সুতরাং √${BengaliNumberFormatter.toBn(sq)} এর মান = ${BengaliNumberFormatter.toBn(n)}"
                        ),
                        tip = "১ থেকে ১৫ পর্যন্ত মৌলিক বর্গসংখ্যাগুলো মুখস্থ থাকলে তাৎক্ষণিক উত্তর দেওয়া যায়।"
                    )
                } else {
                    val ans = n * n
                    buildStandardIntQuestion(
                        id = "sq_easy_$index",
                        category = PracticeCategory.SQUARES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "বর্গ কত হবে?",
                        expression = "${BengaliNumberFormatter.toBn(n)}² = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(n)} এর বর্গ = ${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "কোনো সংখ্যার বর্গ মানে সংখ্যাটিকে নিজের সাথেই গুণ করা।"
                    )
                }
            }
            PracticeDifficulty.MEDIUM -> {
                val n = Random.nextInt(16, 26) // 16 to 25
                val sq = n * n
                val isRoot = Random.nextBoolean()
                if (isRoot) {
                    buildStandardIntQuestion(
                        id = "sq_med_$index",
                        category = PracticeCategory.SQUARES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "নিচের পূর্ণবর্গ সংখ্যার বর্গমূল কত?",
                        expression = "√${BengaliNumberFormatter.toBn(sq)} = ?",
                        correctAns = n,
                        explanation = listOf(
                            "যেহেতু ${BengaliNumberFormatter.toBn(n)}² = ${BengaliNumberFormatter.toBn(sq)}",
                            "সুতরাং √${BengaliNumberFormatter.toBn(sq)} = ${BengaliNumberFormatter.toBn(n)}"
                        ),
                        tip = "এককের ঘরের অঙ্ক দেখে বর্গমূল অনুমান করার কৌশল প্রয়োগ করুন।"
                    )
                } else {
                    buildStandardIntQuestion(
                        id = "sq_med_$index",
                        category = PracticeCategory.SQUARES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "বর্গের মান নির্ণয় করো:",
                        expression = "${BengaliNumberFormatter.toBn(n)}² = ?",
                        correctAns = sq,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(n)}² = (${BengaliNumberFormatter.toBn(20)} + ${BengaliNumberFormatter.toBn(n - 20)})² নিয়মে অথবা সরাসরি গুণ করে = ${BengaliNumberFormatter.toBn(sq)}"
                        ),
                        tip = "২৫ এর বর্গ = ৬২৫। ৫ দিয়ে শেষ হওয়া সংখ্যার বর্গে শেষ দুটি অঙ্ক সর্বদা ২৫ হয়।"
                    )
                }
            }
            PracticeDifficulty.HARD -> {
                // Sum or difference of squares
                val a = Random.nextInt(6, 15)
                val b = Random.nextInt(3, a)
                val isDiff = Random.nextBoolean()
                if (isDiff) {
                    val ans = (a * a) - (b * b)
                    buildStandardIntQuestion(
                        id = "sq_hard_$index",
                        category = PracticeCategory.SQUARES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "বর্গের অন্তর নির্ণয় করো:",
                        expression = "${BengaliNumberFormatter.toBn(a)}² − ${BengaliNumberFormatter.toBn(b)}² = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "সূত্র: a² − b² = (a + b)(a − b)",
                            "= (${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)})(${BengaliNumberFormatter.toBn(a)} − ${BengaliNumberFormatter.toBn(b)})",
                            "= ${BengaliNumberFormatter.toBn(a + b)} × ${BengaliNumberFormatter.toBn(a - b)} = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "সরাসরি বর্গ না করে (a + b)(a − b) সূত্র প্রয়োগ করলে হিসাব অনেক সহজ হয়।"
                    )
                } else {
                    val ans = (a * a) + (b * b)
                    buildStandardIntQuestion(
                        id = "sq_hard_$index",
                        category = PracticeCategory.SQUARES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "বর্গের যোগফল কত?",
                        expression = "${BengaliNumberFormatter.toBn(a)}² + ${BengaliNumberFormatter.toBn(b)}² = ?",
                        correctAns = ans,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(a)}² = ${BengaliNumberFormatter.toBn(a * a)}",
                            "${BengaliNumberFormatter.toBn(b)}² = ${BengaliNumberFormatter.toBn(b * b)}",
                            "যোগফল = ${BengaliNumberFormatter.toBn(a * a)} + ${BengaliNumberFormatter.toBn(b * b)} = ${BengaliNumberFormatter.toBn(ans)}"
                        ),
                        tip = "পিথাগোরীয় উপপাদ্যে লম্ব² + ভূমি² হিসেবে এই সমীকরণ খুব প্রচলিত।"
                    )
                }
            }
        }
    }

    // 6. CUBES & CUBE ROOTS
    private fun generateCubeRootQuestion(index: Int, difficulty: PracticeDifficulty): PracticeQuestion {
        return when (difficulty) {
            PracticeDifficulty.EASY -> {
                val n = Random.nextInt(2, 7) // 2 to 6
                val cube = n * n * n
                val isRoot = Random.nextBoolean()
                if (isRoot) {
                    buildStandardIntQuestion(
                        id = "cube_easy_$index",
                        category = PracticeCategory.CUBES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "ঘনমূল নির্ণয় করো:",
                        expression = "∛${BengaliNumberFormatter.toBn(cube)} = ?",
                        correctAns = n,
                        explanation = listOf(
                            "যেহেতু ${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} = ${BengaliNumberFormatter.toBn(cube)}",
                            "সুতরাং ∛${BengaliNumberFormatter.toBn(cube)} = ${BengaliNumberFormatter.toBn(n)}"
                        ),
                        tip = "ঘনমূল হলো এমন সংখ্যা যা তিনবার নিজেদের মধ্যে গুণ হয়ে মূল সংখ্যা দেয়।"
                    )
                } else {
                    buildStandardIntQuestion(
                        id = "cube_easy_$index",
                        category = PracticeCategory.CUBES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "ঘনফল নির্ণয় করো:",
                        expression = "${BengaliNumberFormatter.toBn(n)}³ = ?",
                        correctAns = cube,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(n)}³ = ${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} = ${BengaliNumberFormatter.toBn(cube)}"
                        ),
                        tip = "প্রথমে সংখ্যার বর্গ বের করুন, তারপর আবার সংখ্যাটি দিয়ে গুণ করুন।"
                    )
                }
            }
            PracticeDifficulty.MEDIUM -> {
                val n = Random.nextInt(7, 13) // 7 to 12
                val cube = n * n * n
                val isRoot = Random.nextBoolean()
                if (isRoot) {
                    buildStandardIntQuestion(
                        id = "cube_med_$index",
                        category = PracticeCategory.CUBES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "ঘনমূল কত?",
                        expression = "∛${BengaliNumberFormatter.toBn(cube)} = ?",
                        correctAns = n,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(n)}³ = ${BengaliNumberFormatter.toBn(cube)}",
                            "সুতরাং এর ঘনমূল = ${BengaliNumberFormatter.toBn(n)}"
                        ),
                        tip = "১০³ = ১০০০ এবং এর কাছাকাছি ঘন সংখ্যাগুলোর তালিকা মনে রাখুন।"
                    )
                } else {
                    buildStandardIntQuestion(
                        id = "cube_med_$index",
                        category = PracticeCategory.CUBES_ROOTS,
                        difficulty = difficulty,
                        questionBn = "ঘনফল কত?",
                        expression = "${BengaliNumberFormatter.toBn(n)}³ = ?",
                        correctAns = cube,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(n)}² = ${BengaliNumberFormatter.toBn(n * n)}",
                            "${BengaliNumberFormatter.toBn(n * n)} × ${BengaliNumberFormatter.toBn(n)} = ${BengaliNumberFormatter.toBn(cube)}"
                        ),
                        tip = "১১³ = ১৩৩১ এবং ১২³ = ১৭২৮ খুব সাধারণ ও প্রয়োজনীয় মান।"
                    )
                }
            }
            PracticeDifficulty.HARD -> {
                val a = Random.nextInt(2, 6)
                val b = Random.nextInt(2, 5)
                val cubeA = a * a * a
                val cubeB = b * b * b
                val ans = cubeA + cubeB
                buildStandardIntQuestion(
                    id = "cube_hard_$index",
                    category = PracticeCategory.CUBES_ROOTS,
                    difficulty = difficulty,
                    questionBn = "ঘনের সমষ্টি কত?",
                    expression = "${BengaliNumberFormatter.toBn(a)}³ + ${BengaliNumberFormatter.toBn(b)}³ = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "${BengaliNumberFormatter.toBn(a)}³ = ${BengaliNumberFormatter.toBn(cubeA)}",
                        "${BengaliNumberFormatter.toBn(b)}³ = ${BengaliNumberFormatter.toBn(cubeB)}",
                        "সমষ্টি = ${BengaliNumberFormatter.toBn(cubeA)} + ${BengaliNumberFormatter.toBn(cubeB)} = ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "প্রতিটি পদের ঘনফল পৃথকভাবে বের করে যোগ করুন।"
                )
            }
        }
    }

    // 7. BASIC ALGEBRA
    private fun generateAlgebraQuestion(index: Int, difficulty: PracticeDifficulty): PracticeQuestion {
        return when (difficulty) {
            PracticeDifficulty.EASY -> {
                // One-step equation: x + a = b or a*x = b
                val isAdd = Random.nextBoolean()
                if (isAdd) {
                    val x = Random.nextInt(4, 25)
                    val a = Random.nextInt(5, 30)
                    val b = x + a
                    buildStandardIntQuestion(
                        id = "alg_easy_$index",
                        category = PracticeCategory.BASIC_ALGEBRA,
                        difficulty = difficulty,
                        questionBn = "সমীকরণটি সমাধান করে x এর মান নির্ণয় করো:",
                        expression = "x + ${BengaliNumberFormatter.toBn(a)} = ${BengaliNumberFormatter.toBn(b)}  ⇒  x = ?",
                        correctAns = x,
                        explanation = listOf(
                            "x + ${BengaliNumberFormatter.toBn(a)} = ${BengaliNumberFormatter.toBn(b)}",
                            "পক্ষান্তর করে: x = ${BengaliNumberFormatter.toBn(b)} − ${BengaliNumberFormatter.toBn(a)}",
                            "x = ${BengaliNumberFormatter.toBn(x)}"
                        ),
                        tip = "বাম পাশের যোগ রাশি ডান পাশে গেলে বিয়োগ চিহ্নে রূপান্তরিত হয়।"
                    )
                } else {
                    val x = Random.nextInt(3, 15)
                    val a = Random.nextInt(2, 8)
                    val b = a * x
                    buildStandardIntQuestion(
                        id = "alg_easy_$index",
                        category = PracticeCategory.BASIC_ALGEBRA,
                        difficulty = difficulty,
                        questionBn = "x এর মান কত?",
                        expression = "${BengaliNumberFormatter.toBn(a)}x = ${BengaliNumberFormatter.toBn(b)}  ⇒  x = ?",
                        correctAns = x,
                        explanation = listOf(
                            "${BengaliNumberFormatter.toBn(a)}x = ${BengaliNumberFormatter.toBn(b)}",
                            "উভয় পক্ষকে ${BengaliNumberFormatter.toBn(a)} দিয়ে ভাগ করে:",
                            "x = ${BengaliNumberFormatter.toBn(b)} ÷ ${BengaliNumberFormatter.toBn(a)} = ${BengaliNumberFormatter.toBn(x)}"
                        ),
                        tip = "চলকের সাথে গুণিতক থাকলে পক্ষান্তর করার পর তা ভাগে পরিণত হয়।"
                    )
                }
            }
            PracticeDifficulty.MEDIUM -> {
                // Two-step equation: a*x + b = c
                val x = Random.nextInt(3, 12)
                val a = Random.nextInt(2, 6)
                val b = Random.nextInt(3, 20)
                val c = (a * x) + b
                buildStandardIntQuestion(
                    id = "alg_med_$index",
                    category = PracticeCategory.BASIC_ALGEBRA,
                    difficulty = difficulty,
                    questionBn = "সমীকরণটির মূল (x এর মান) নির্ণয় করো:",
                    expression = "${BengaliNumberFormatter.toBn(a)}x + ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(c)}  ⇒  x = ?",
                    correctAns = x,
                    explanation = listOf(
                        "ধাপ ১: ${BengaliNumberFormatter.toBn(a)}x = ${BengaliNumberFormatter.toBn(c)} − ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(c - b)}",
                        "ধাপ ২: x = ${BengaliNumberFormatter.toBn(c - b)} ÷ ${BengaliNumberFormatter.toBn(a)}",
                        "সুতরাং x = ${BengaliNumberFormatter.toBn(x)}"
                    ),
                    tip = "প্রথমে ধ্রুবক পদ পক্ষান্তর করুন, তারপর চলকের সহগ দিয়ে ভাগ করুন।"
                )
            }
            PracticeDifficulty.HARD -> {
                // Value of algebraic expression: if a = 3, b = 4 then 2a² + 3ab - b²
                val a = Random.nextInt(2, 6)
                val b = Random.nextInt(2, 5)
                val ans = (a * a) + (2 * a * b) + (b * b) // (a + b)^2
                buildStandardIntQuestion(
                    id = "alg_hard_$index",
                    category = PracticeCategory.BASIC_ALGEBRA,
                    difficulty = difficulty,
                    questionBn = "যদি a = ${BengaliNumberFormatter.toBn(a)} এবং b = ${BengaliNumberFormatter.toBn(b)} হয়, তবে a² + 2ab + b² এর মান কত?",
                    expression = "a = ${BengaliNumberFormatter.toBn(a)}, b = ${BengaliNumberFormatter.toBn(b)}  ⇒  (a + b)² = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "সূত্র: a² + 2ab + b² = (a + b)²",
                        "= (${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)})² = ${BengaliNumberFormatter.toBn(a + b)}²",
                        "= ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "আলাদা মান বসানোর বদলে অভেদের সূত্র (a + b)² প্রয়োগ করলে হিসাব নিমেষেই শেষ হয়।"
                )
            }
        }
    }

    // 8. NUMBER OPERATIONS & BODMAS
    private fun generateBodmasQuestion(index: Int, difficulty: PracticeDifficulty): PracticeQuestion {
        return when (difficulty) {
            PracticeDifficulty.EASY -> {
                // Two operations: a + b * c or a * b - c
                val b = Random.nextInt(3, 9)
                val c = Random.nextInt(2, 7)
                val a = Random.nextInt(10, 35)
                val ans = a + (b * c)
                buildStandardIntQuestion(
                    id = "bodmas_easy_$index",
                    category = PracticeCategory.NUMBER_OPERATIONS,
                    difficulty = difficulty,
                    questionBn = "BODMAS নিয়ম অনুসারে সরলফল নির্ণয় করো:",
                    expression = "${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} × ${BengaliNumberFormatter.toBn(c)} = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "BODMAS অনুসারে যোগের আগে গুণের কাজ আগে করতে হবে।",
                        "ধাপ ১ (গুণ): ${BengaliNumberFormatter.toBn(b)} × ${BengaliNumberFormatter.toBn(c)} = ${BengaliNumberFormatter.toBn(b * c)}",
                        "ধাপ ২ (যোগ): ${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b * c)} = ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "সবসময় গুণের কাজ আগে, যোগের কাজ পরে। ভুলেও আগে যোগ করবেন না।"
                )
            }
            PracticeDifficulty.MEDIUM -> {
                // Parentheses: (a + b) * c - d
                val a = Random.nextInt(4, 15)
                val b = Random.nextInt(3, 12)
                val c = Random.nextInt(2, 6)
                val d = Random.nextInt(5, 20)
                val ans = ((a + b) * c) - d
                buildStandardIntQuestion(
                    id = "bodmas_med_$index",
                    category = PracticeCategory.NUMBER_OPERATIONS,
                    difficulty = difficulty,
                    questionBn = "বন্ধনীর সরলফল নির্ণয় করো:",
                    expression = "(${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)}) × ${BengaliNumberFormatter.toBn(c)} − ${BengaliNumberFormatter.toBn(d)} = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "ধাপ ১ (বন্ধনী): (${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)}) = ${BengaliNumberFormatter.toBn(a + b)}",
                        "ধাপ ২ (গুণ): ${BengaliNumberFormatter.toBn(a + b)} × ${BengaliNumberFormatter.toBn(c)} = ${BengaliNumberFormatter.toBn((a + b) * c)}",
                        "ধাপ ৩ (বিয়োগ): ${BengaliNumberFormatter.toBn((a + b) * c)} − ${BengaliNumberFormatter.toBn(d)} = ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "বন্ধনীর (Brackets) ভেতরের কাজ সর্বদা সবার আগে সম্পন্ন করতে হয়।"
                )
            }
            PracticeDifficulty.HARD -> {
                // Three tiers: a + b * (c - d / e)
                val e = Random.nextInt(2, 5)
                val d = e * Random.nextInt(2, 6) // d divisible by e
                val c = (d / e) + Random.nextInt(3, 8)
                val b = Random.nextInt(3, 6)
                val a = Random.nextInt(10, 40)
                val insideBracket = c - (d / e)
                val ans = a + (b * insideBracket)
                buildStandardIntQuestion(
                    id = "bodmas_hard_$index",
                    category = PracticeCategory.NUMBER_OPERATIONS,
                    difficulty = difficulty,
                    questionBn = "জটিল BODMAS রাশির সরল মান নির্ণয় করো:",
                    expression = "${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} × (${BengaliNumberFormatter.toBn(c)} − ${BengaliNumberFormatter.toBn(d)} ÷ ${BengaliNumberFormatter.toBn(e)}) = ?",
                    correctAns = ans,
                    explanation = listOf(
                        "ধাপ ১ (বন্ধনীর ভেতরের ভাগ): ${BengaliNumberFormatter.toBn(d)} ÷ ${BengaliNumberFormatter.toBn(e)} = ${BengaliNumberFormatter.toBn(d / e)}",
                        "ধাপ ২ (বন্ধনীর ভেতরের বিয়োগ): ${BengaliNumberFormatter.toBn(c)} − ${BengaliNumberFormatter.toBn(d / e)} = ${BengaliNumberFormatter.toBn(insideBracket)}",
                        "ধাপ ৩ (গুণ): ${BengaliNumberFormatter.toBn(b)} × ${BengaliNumberFormatter.toBn(insideBracket)} = ${BengaliNumberFormatter.toBn(b * insideBracket)}",
                        "ধাপ ৪ (যোগ): ${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b * insideBracket)} = ${BengaliNumberFormatter.toBn(ans)}"
                    ),
                    tip = "ক্রম: প্রথম বন্ধনী → ভাগ (Division) → গুণ (Multiplication) → যোগ (Addition) → বিয়োগ (Subtraction)।"
                )
            }
        }
    }

    // Helper to generate distinct integer distractors
    private fun buildStandardIntQuestion(
        id: String,
        category: PracticeCategory,
        difficulty: PracticeDifficulty,
        questionBn: String,
        expression: String,
        correctAns: Int,
        explanation: List<String>,
        tip: String
    ): PracticeQuestion {
        val distractors = mutableSetOf<Int>()
        var attempts = 0
        while (distractors.size < 3 && attempts < 40) {
            attempts++
            val delta = when (attempts % 4) {
                0 -> 1
                1 -> -1
                2 -> 10
                else -> Random.nextInt(-15, 16)
            }
            val fake = correctAns + delta
            if (fake > 0 && fake != correctAns) {
                distractors.add(fake)
            }
        }
        // Fill if not enough
        var fallback = 1
        while (distractors.size < 3) {
            val candidate = correctAns + fallback
            if (candidate > 0 && candidate != correctAns) {
                distractors.add(candidate)
            }
            fallback += 2
        }

        val all = (distractors.toList() + correctAns).shuffled()
        val correctIdx = all.indexOf(correctAns)
        val optsBn = all.map { BengaliNumberFormatter.toBn(it) }

        return PracticeQuestion(
            id = id,
            category = category,
            difficulty = difficulty,
            questionBn = questionBn,
            expression = expression,
            optionsBn = optsBn,
            correctIndex = correctIdx,
            correctAnswerBn = BengaliNumberFormatter.toBn(correctAns),
            stepByStepExplanationBn = explanation,
            tipBn = tip
        )
    }

    private fun generateFractionOptions(num: Int, den: Int): List<String> {
        val correct = "${BengaliNumberFormatter.toBn(num)}/${BengaliNumberFormatter.toBn(den)}"
        val set = mutableSetOf(correct)
        var delta = 1
        while (set.size < 4) {
            val fakeNum = (num + delta).coerceAtLeast(1)
            val fake = "${BengaliNumberFormatter.toBn(fakeNum)}/${BengaliNumberFormatter.toBn(den)}"
            set.add(fake)
            delta++
        }
        return set.toList()
    }

    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
}
