package com.example.math

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Exposure
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.random.Random

/**
 * Categories available for Mental Math Challenges.
 */
enum class MentalMathCategory(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val primaryColor: Color,
    val containerColor: Color
) {
    ADDITION(
        id = "addition",
        titleBn = "যোগ",
        titleEn = "Addition",
        subtitleBn = "দ্রুত ও নির্ভুল দুই বা ততোধিক সংখ্যার যোগ",
        primaryColor = Color(0xFF2563EB), // Blue
        containerColor = Color(0xFFDBEAFE)
    ),
    SUBTRACTION(
        id = "subtraction",
        titleBn = "বিয়োগ",
        titleEn = "Subtraction",
        subtitleBn = "ধার নেওয়া ও দ্রুত মানসিক বিয়োগের কৌশল",
        primaryColor = Color(0xFF0D9488), // Teal
        containerColor = Color(0xFFCCFBF1)
    ),
    MULTIPLICATION(
        id = "multiplication",
        titleBn = "গুণ",
        titleEn = "Multiplication",
        subtitleBn = "নামতা ও দ্রুত গুণনের তীব্র মানসিক গতি",
        primaryColor = Color(0xFF7C3AED), // Purple
        containerColor = Color(0xFFEDE9FE)
    ),
    DIVISION(
        id = "division",
        titleBn = "ভাগ",
        titleEn = "Division",
        subtitleBn = "নিঃশেষে বিভাজ্য নিখুঁত ও দ্রুত ভাগফল",
        primaryColor = Color(0xFF0891B2), // Cyan
        containerColor = Color(0xFFCFFAFE)
    ),
    SQUARES(
        id = "squares",
        titleBn = "বর্গ সংখ্যা",
        titleEn = "Squares",
        subtitleBn = "১ থেকে ৩৫ পর্যন্ত সংখ্যার দ্রুত বর্গ নির্ণয়",
        primaryColor = Color(0xFFE11D48), // Rose
        containerColor = Color(0xFFFFE4E6)
    ),
    MIXED_ARITHMETIC(
        id = "mixed",
        titleBn = "মিশ্র গণিত",
        titleEn = "Mixed Arithmetic",
        subtitleBn = "যোগ, বিয়োগ, গুণ ও ভাগের সংমিশ্রণ চ্যালেঞ্জ",
        primaryColor = Color(0xFFEA580C), // Orange
        containerColor = Color(0xFFFFEDD5)
    );

    fun getIcon(): ImageVector = when (this) {
        ADDITION -> Icons.Default.Add
        SUBTRACTION -> Icons.Default.Remove
        MULTIPLICATION -> Icons.Default.Close
        DIVISION -> Icons.Default.Exposure
        SQUARES -> Icons.Default.Straighten
        MIXED_ARITHMETIC -> Icons.Default.Calculate
    }
}

/**
 * Three levels of difficulty for Mental Math.
 */
enum class MentalMathDifficulty(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val basePoints: Int,
    val badgeColor: Color
) {
    EASY(
        id = "easy",
        titleBn = "সহজ",
        titleEn = "Easy",
        basePoints = 10,
        badgeColor = Color(0xFF10B981)
    ),
    MEDIUM(
        id = "medium",
        titleBn = "মাঝারি",
        titleEn = "Medium",
        basePoints = 20,
        badgeColor = Color(0xFFF59E0B)
    ),
    HARD(
        id = "hard",
        titleBn = "কঠিন",
        titleEn = "Hard",
        basePoints = 30,
        badgeColor = Color(0xFFEF4444)
    )
}

/**
 * Mode of the challenge: Timed Sprint (e.g. 30s, 60s, 120s) or Fixed Question Count (10, 20, 30 questions).
 */
enum class MentalMathMode(
    val id: String,
    val titleBn: String,
    val subtitleBn: String
) {
    TIMED_SPRINT("timed_sprint", "টাইমড স্প্রিন্ট", "নির্দিষ্ট সময়ে সর্বোচ্চ স্কোর অর্জন করুন"),
    FIXED_QUESTIONS("fixed_questions", "নির্দিষ্ট প্রশ্ন", "১০/২০/৩০ প্রশ্নে নির্ভুলতা যাচাই করুন")
}

/**
 * An individual mental math challenge question.
 */
data class MentalMathQuestion(
    val id: String,
    val category: MentalMathCategory,
    val difficulty: MentalMathDifficulty,
    val expressionBn: String,
    val expressionPlain: String,
    val correctAnswer: Int,
    val correctAnswerBn: String,
    val options: List<Int>,
    val optionsBn: List<String>,
    val correctIndex: Int,
    val explanationBn: String
)

/**
 * Record of an answered question during a session.
 */
data class AnsweredMentalQuestion(
    val question: MentalMathQuestion,
    val chosenOption: Int,
    val isCorrect: Boolean,
    val timeTakenSeconds: Int
)

/**
 * State representing a mental math challenge session.
 */
data class MentalMathSessionState(
    val category: MentalMathCategory = MentalMathCategory.ADDITION,
    val difficulty: MentalMathDifficulty = MentalMathDifficulty.EASY,
    val mode: MentalMathMode = MentalMathMode.TIMED_SPRINT,
    val configuredDurationSeconds: Int = 60,
    val configuredQuestionCount: Int = 10,
    val questions: List<MentalMathQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val score: Int = 0,
    val streak: Int = 0,
    val highestStreak: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val timeRemainingSeconds: Int = 60,
    val elapsedSeconds: Int = 0,
    val isAnswerSubmitted: Boolean = false,
    val selectedOptionIndex: Int? = null,
    val lastAnswerCorrect: Boolean? = null,
    val feedbackMessageBn: String? = null,
    val isFinished: Boolean = false,
    val history: List<AnsweredMentalQuestion> = emptyList(),
    val isReviewMode: Boolean = false,
    val startElapsedRealtimeMs: Long = 0L,
    val deadlineElapsedRealtimeMs: Long = 0L
)

/**
 * Monotonic clock abstraction for reliable, testable elapsed time measurement.
 */
interface MonotonicClock {
    fun elapsedRealtime(): Long
}

class DefaultMonotonicClock : MonotonicClock {
    override fun elapsedRealtime(): Long {
        return try {
            android.os.SystemClock.elapsedRealtime()
        } catch (e: Throwable) {
            System.nanoTime() / 1_000_000L
        }
    }
}

/**
 * Generator engine producing mathematically valid, educational mental math problems.
 */
object MentalMathGenerator {

    /**
     * Generates a batch of distinct, valid mental math questions.
     */
    fun generateQuestions(
        category: MentalMathCategory,
        difficulty: MentalMathDifficulty,
        count: Int = 20
    ): List<MentalMathQuestion> {
        val list = mutableListOf<MentalMathQuestion>()
        val seenExpressions = mutableSetOf<String>()
        var attempts = 0

        while (list.size < count && attempts < count * 8) {
            attempts++
            val q = when (category) {
                MentalMathCategory.ADDITION -> generateAddition(list.size + 1, difficulty)
                MentalMathCategory.SUBTRACTION -> generateSubtraction(list.size + 1, difficulty)
                MentalMathCategory.MULTIPLICATION -> generateMultiplication(list.size + 1, difficulty)
                MentalMathCategory.DIVISION -> generateDivision(list.size + 1, difficulty)
                MentalMathCategory.SQUARES -> generateSquare(list.size + 1, difficulty)
                MentalMathCategory.MIXED_ARITHMETIC -> generateMixed(list.size + 1, difficulty)
            }

            if (!seenExpressions.contains(q.expressionPlain)) {
                seenExpressions.add(q.expressionPlain)
                list.add(q)
            }
        }

        return list
    }

    // 1. ADDITION
    private fun generateAddition(index: Int, difficulty: MentalMathDifficulty): MentalMathQuestion {
        val (a, b, c) = when (difficulty) {
            MentalMathDifficulty.EASY -> {
                val x = Random.nextInt(12, 85)
                val y = Random.nextInt(11, 75)
                Triple(x, y, 0)
            }
            MentalMathDifficulty.MEDIUM -> {
                val threeNumbers = Random.nextBoolean()
                if (threeNumbers) {
                    val x = Random.nextInt(15, 60)
                    val y = Random.nextInt(15, 55)
                    val z = Random.nextInt(10, 45)
                    Triple(x, y, z)
                } else {
                    val x = Random.nextInt(110, 490)
                    val y = Random.nextInt(95, 380)
                    Triple(x, y, 0)
                }
            }
            MentalMathDifficulty.HARD -> {
                val x = Random.nextInt(350, 1500)
                val y = Random.nextInt(280, 1200)
                Triple(x, y, 0)
            }
        }

        val ans = a + b + c
        val exprPlain = if (c > 0) "$a + $b + $c" else "$a + $b"
        val exprBn = if (c > 0) {
            "${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} + ${BengaliNumberFormatter.toBn(c)} = ?"
        } else {
            "${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} = ?"
        }

        val explanation = "যোগফল = $exprBn\nসঠিক উত্তর: ${BengaliNumberFormatter.toBn(ans)}"
        val options = generateDistractors(ans)

        return buildQuestion(
            id = "mm_add_${difficulty.id}_$index",
            category = MentalMathCategory.ADDITION,
            difficulty = difficulty,
            expressionBn = exprBn,
            expressionPlain = exprPlain,
            correctAnswer = ans,
            options = options,
            explanationBn = explanation
        )
    }

    // 2. SUBTRACTION (Guaranteed positive result)
    private fun generateSubtraction(index: Int, difficulty: MentalMathDifficulty): MentalMathQuestion {
        val (a, b) = when (difficulty) {
            MentalMathDifficulty.EASY -> {
                val x = Random.nextInt(35, 99)
                val y = Random.nextInt(11, x - 5)
                Pair(x, y)
            }
            MentalMathDifficulty.MEDIUM -> {
                val x = Random.nextInt(120, 550)
                val y = Random.nextInt(35, x - 15)
                Pair(x, y)
            }
            MentalMathDifficulty.HARD -> {
                val x = Random.nextInt(600, 2200)
                val y = Random.nextInt(180, x - 50)
                Pair(x, y)
            }
        }

        val ans = a - b
        val exprPlain = "$a - $b"
        val exprBn = "${BengaliNumberFormatter.toBn(a)} − ${BengaliNumberFormatter.toBn(b)} = ?"
        val explanation = "${BengaliNumberFormatter.toBn(a)} থেকে ${BengaliNumberFormatter.toBn(b)} বিয়োগ করলে থাকে ${BengaliNumberFormatter.toBn(ans)}"
        val options = generateDistractors(ans)

        return buildQuestion(
            id = "mm_sub_${difficulty.id}_$index",
            category = MentalMathCategory.SUBTRACTION,
            difficulty = difficulty,
            expressionBn = exprBn,
            expressionPlain = exprPlain,
            correctAnswer = ans,
            options = options,
            explanationBn = explanation
        )
    }

    // 3. MULTIPLICATION
    private fun generateMultiplication(index: Int, difficulty: MentalMathDifficulty): MentalMathQuestion {
        val (a, b) = when (difficulty) {
            MentalMathDifficulty.EASY -> {
                val x = Random.nextInt(3, 13)
                val y = Random.nextInt(3, 13)
                Pair(x, y)
            }
            MentalMathDifficulty.MEDIUM -> {
                val x = Random.nextInt(12, 28)
                val y = Random.nextInt(4, 15)
                Pair(x, y)
            }
            MentalMathDifficulty.HARD -> {
                val x = Random.nextInt(18, 48)
                val y = Random.nextInt(14, 35)
                Pair(x, y)
            }
        }

        val ans = a * b
        val exprPlain = "$a * $b"
        val exprBn = "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)} = ?"
        val explanation = "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)} এর গুণফল = ${BengaliNumberFormatter.toBn(ans)}"
        val options = generateDistractors(ans)

        return buildQuestion(
            id = "mm_mul_${difficulty.id}_$index",
            category = MentalMathCategory.MULTIPLICATION,
            difficulty = difficulty,
            expressionBn = exprBn,
            expressionPlain = exprPlain,
            correctAnswer = ans,
            options = options,
            explanationBn = explanation
        )
    }

    // 4. DIVISION (Divisor is never zero, dividend evenly divisible)
    private fun generateDivision(index: Int, difficulty: MentalMathDifficulty): MentalMathQuestion {
        val (divisor, quotient) = when (difficulty) {
            MentalMathDifficulty.EASY -> {
                val d = Random.nextInt(2, 11) // Divisor 2..10 (never 0)
                val q = Random.nextInt(3, 15) // Quotient 3..14
                Pair(d, q)
            }
            MentalMathDifficulty.MEDIUM -> {
                val d = Random.nextInt(4, 16)
                val q = Random.nextInt(11, 32)
                Pair(d, q)
            }
            MentalMathDifficulty.HARD -> {
                val d = Random.nextInt(11, 26)
                val q = Random.nextInt(14, 45)
                Pair(d, q)
            }
        }

        val dividend = divisor * quotient
        val exprPlain = "$dividend / $divisor"
        val exprBn = "${BengaliNumberFormatter.toBn(dividend)} ÷ ${BengaliNumberFormatter.toBn(divisor)} = ?"
        val explanation = "${BengaliNumberFormatter.toBn(dividend)} কে ${BengaliNumberFormatter.toBn(divisor)} দিয়ে ভাগ করলে ভাগফল হয় ${BengaliNumberFormatter.toBn(quotient)}"
        val options = generateDistractors(quotient)

        return buildQuestion(
            id = "mm_div_${difficulty.id}_$index",
            category = MentalMathCategory.DIVISION,
            difficulty = difficulty,
            expressionBn = exprBn,
            expressionPlain = exprPlain,
            correctAnswer = quotient,
            options = options,
            explanationBn = explanation
        )
    }

    // 5. SQUARES
    private fun generateSquare(index: Int, difficulty: MentalMathDifficulty): MentalMathQuestion {
        val n = when (difficulty) {
            MentalMathDifficulty.EASY -> Random.nextInt(2, 13) // 2 to 12
            MentalMathDifficulty.MEDIUM -> Random.nextInt(13, 26) // 13 to 25
            MentalMathDifficulty.HARD -> Random.nextInt(26, 36) // 26 to 35
        }

        val ans = n * n
        val exprPlain = "$n^2"
        val exprBn = "${BengaliNumberFormatter.toBn(n)}² = ?"
        val explanation = "${BengaliNumberFormatter.toBn(n)} এর বর্গ (${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)}) = ${BengaliNumberFormatter.toBn(ans)}"
        val options = generateDistractors(ans)

        return buildQuestion(
            id = "mm_sq_${difficulty.id}_$index",
            category = MentalMathCategory.SQUARES,
            difficulty = difficulty,
            expressionBn = exprBn,
            expressionPlain = exprPlain,
            correctAnswer = ans,
            options = options,
            explanationBn = explanation
        )
    }

    // 6. MIXED ARITHMETIC
    private fun generateMixed(index: Int, difficulty: MentalMathDifficulty): MentalMathQuestion {
        return when (difficulty) {
            MentalMathDifficulty.EASY -> {
                val a = Random.nextInt(10, 40)
                val b = Random.nextInt(5, 30)
                val c = Random.nextInt(2, (a + b) - 5)
                val ans = a + b - c
                val exprPlain = "$a + $b - $c"
                val exprBn = "${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} − ${BengaliNumberFormatter.toBn(c)} = ?"
                val explanation = "ধাপ ১: ${BengaliNumberFormatter.toBn(a)} + ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(a + b)}\nধাপ ২: ${BengaliNumberFormatter.toBn(a + b)} − ${BengaliNumberFormatter.toBn(c)} = ${BengaliNumberFormatter.toBn(ans)}"
                buildQuestion(
                    id = "mm_mix_easy_$index",
                    category = MentalMathCategory.MIXED_ARITHMETIC,
                    difficulty = difficulty,
                    expressionBn = exprBn,
                    expressionPlain = exprPlain,
                    correctAnswer = ans,
                    options = generateDistractors(ans),
                    explanationBn = explanation
                )
            }
            MentalMathDifficulty.MEDIUM -> {
                val isMulAdd = Random.nextBoolean()
                if (isMulAdd) {
                    val a = Random.nextInt(4, 12)
                    val b = Random.nextInt(3, 11)
                    val c = Random.nextInt(10, 50)
                    val ans = (a * b) + c
                    val exprPlain = "($a * $b) + $c"
                    val exprBn = "(${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)}) + ${BengaliNumberFormatter.toBn(c)} = ?"
                    val explanation = "ধাপ ১: ${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(a * b)}\nধাপ ২: ${BengaliNumberFormatter.toBn(a * b)} + ${BengaliNumberFormatter.toBn(c)} = ${BengaliNumberFormatter.toBn(ans)}"
                    buildQuestion(
                        id = "mm_mix_med_$index",
                        category = MentalMathCategory.MIXED_ARITHMETIC,
                        difficulty = difficulty,
                        expressionBn = exprBn,
                        expressionPlain = exprPlain,
                        correctAnswer = ans,
                        options = generateDistractors(ans),
                        explanationBn = explanation
                    )
                } else {
                    val divisor = Random.nextInt(2, 9)
                    val quotient = Random.nextInt(5, 18)
                    val dividend = divisor * quotient
                    val c = Random.nextInt(5, 30)
                    val ans = quotient + c
                    val exprPlain = "($dividend / $divisor) + $c"
                    val exprBn = "(${BengaliNumberFormatter.toBn(dividend)} ÷ ${BengaliNumberFormatter.toBn(divisor)}) + ${BengaliNumberFormatter.toBn(c)} = ?"
                    val explanation = "ধাপ ১: ${BengaliNumberFormatter.toBn(dividend)} ÷ ${BengaliNumberFormatter.toBn(divisor)} = ${BengaliNumberFormatter.toBn(quotient)}\nধাপ ২: ${BengaliNumberFormatter.toBn(quotient)} + ${BengaliNumberFormatter.toBn(c)} = ${BengaliNumberFormatter.toBn(ans)}"
                    buildQuestion(
                        id = "mm_mix_med_$index",
                        category = MentalMathCategory.MIXED_ARITHMETIC,
                        difficulty = difficulty,
                        expressionBn = exprBn,
                        expressionPlain = exprPlain,
                        correctAnswer = ans,
                        options = generateDistractors(ans),
                        explanationBn = explanation
                    )
                }
            }
            MentalMathDifficulty.HARD -> {
                val a = Random.nextInt(6, 15)
                val b = Random.nextInt(5, 12)
                val c = Random.nextInt(3, 9)
                val d = Random.nextInt(2, 8)
                val prod1 = a * b
                val prod2 = c * d
                val isAdd = Random.nextBoolean()
                val ans = if (isAdd) prod1 + prod2 else (prod1 - prod2).coerceAtLeast(1)
                val opSymbol = if (isAdd) "+" else "−"
                val exprPlain = "($a * $b) $opSymbol ($c * $d)"
                val exprBn = "(${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)}) $opSymbol (${BengaliNumberFormatter.toBn(c)} × ${BengaliNumberFormatter.toBn(d)}) = ?"
                val explanation = "(${BengaliNumberFormatter.toBn(prod1)}) $opSymbol (${BengaliNumberFormatter.toBn(prod2)}) = ${BengaliNumberFormatter.toBn(ans)}"
                buildQuestion(
                    id = "mm_mix_hard_$index",
                    category = MentalMathCategory.MIXED_ARITHMETIC,
                    difficulty = difficulty,
                    expressionBn = exprBn,
                    expressionPlain = exprPlain,
                    correctAnswer = ans,
                    options = generateDistractors(ans),
                    explanationBn = explanation
                )
            }
        }
    }

    private fun buildQuestion(
        id: String,
        category: MentalMathCategory,
        difficulty: MentalMathDifficulty,
        expressionBn: String,
        expressionPlain: String,
        correctAnswer: Int,
        options: List<Int>,
        explanationBn: String
    ): MentalMathQuestion {
        val optionsBn = options.map { BengaliNumberFormatter.toBn(it) }
        val correctIndex = options.indexOf(correctAnswer)
        return MentalMathQuestion(
            id = id,
            category = category,
            difficulty = difficulty,
            expressionBn = expressionBn,
            expressionPlain = expressionPlain,
            correctAnswer = correctAnswer,
            correctAnswerBn = BengaliNumberFormatter.toBn(correctAnswer),
            options = options,
            optionsBn = optionsBn,
            correctIndex = correctIndex,
            explanationBn = explanationBn
        )
    }

    /**
     * Generates 4 strictly distinct integer options containing the correct answer.
     */
    fun generateDistractors(correctAnswer: Int): List<Int> {
        val optionsSet = mutableSetOf(correctAnswer)
        val deltas = mutableListOf(-10, 10, -1, 1, -2, 2, -5, 5, -20, 20, -15, 15, -3, 3).shuffled()

        for (d in deltas) {
            val candidate = correctAnswer + d
            if (candidate > 0 && candidate != correctAnswer) {
                optionsSet.add(candidate)
            }
            if (optionsSet.size == 4) break
        }

        // Fallback if needed
        var counter = 1
        while (optionsSet.size < 4) {
            val cand1 = correctAnswer + (counter * 7)
            val cand2 = (correctAnswer - (counter * 7)).coerceAtLeast(1)
            if (cand1 != correctAnswer) optionsSet.add(cand1)
            if (optionsSet.size < 4 && cand2 != correctAnswer) optionsSet.add(cand2)
            counter++
        }

        return optionsSet.toList().shuffled()
    }
}
