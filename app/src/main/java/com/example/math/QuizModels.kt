package com.example.math

import kotlin.random.Random

data class QuizQuestion(
    val id: String,
    val questionBn: String,
    val optionsBn: List<String>,
    val correctIndex: Int,
    val explanationBn: String,
    val topicTitleBn: String
)

enum class QuizTopic(val id: String, val titleBn: String) {
    MULTIPLICATION("MULTIPLICATION", "গুণ ও নামতা কুইজ (১-৪০)"),
    PRIMES("PRIMES", "মৌলিক সংখ্যা কুইজ (১-২০০)"),
    SQUARES_CUBES("SQUARES_CUBES", "বর্গ ও ঘন কুইজ (১-২০)"),
    ALGEBRA("ALGEBRA", "বীজগণিত সূত্রাবলি কুইজ")
}

object QuizGenerator {
    fun generateQuiz(topic: QuizTopic, questionCount: Int = 10): List<QuizQuestion> {
        return when (topic) {
            QuizTopic.MULTIPLICATION -> generateMultiplicationQuestions(questionCount)
            QuizTopic.PRIMES -> generatePrimeQuestions(questionCount)
            QuizTopic.SQUARES_CUBES -> generateSquareCubeQuestions(questionCount)
            QuizTopic.ALGEBRA -> generateAlgebraQuestions(questionCount)
        }
    }

    private fun generateMultiplicationQuestions(count: Int): List<QuizQuestion> {
        val list = mutableListOf<QuizQuestion>()
        repeat(count) { idx ->
            val a = Random.nextInt(1, 41)
            val b = Random.nextInt(1, 11)
            val correctAns = a * b
            val questionBn = "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)} এর মান কত?"

            // Distractors
            val distractors = mutableSetOf<Int>()
            while (distractors.size < 3) {
                val delta = Random.nextInt(-10, 11)
                val fake = correctAns + delta
                if (fake > 0 && fake != correctAns) {
                    distractors.add(fake)
                }
            }
            val allOptions = (distractors.toList() + correctAns).shuffled()
            val correctIdx = allOptions.indexOf(correctAns)

            list.add(
                QuizQuestion(
                    id = "mul_$idx",
                    questionBn = questionBn,
                    optionsBn = allOptions.map { BengaliNumberFormatter.toBn(it) },
                    correctIndex = correctIdx,
                    explanationBn = "${BengaliNumberFormatter.toBn(a)} কে ${BengaliNumberFormatter.toBn(b)} দিয়ে গুণ করলে গুণফল হয় ${BengaliNumberFormatter.toBn(correctAns)}।",
                    topicTitleBn = "নামতা কুইজ"
                )
            )
        }
        return list
    }

    private fun generatePrimeQuestions(count: Int): List<QuizQuestion> {
        val list = mutableListOf<QuizQuestion>()
        repeat(count) { idx ->
            val type = Random.nextInt(3)
            when (type) {
                0 -> {
                    // "Which one is prime?"
                    val primeNum = if (Random.nextBoolean()) {
                        PrimeNumberProvider.primes1To100.random()
                    } else {
                        PrimeNumberProvider.primes101To200.random()
                    }
                    val composites = mutableSetOf<Int>()
                    while (composites.size < 3) {
                        val num = Random.nextInt(4, 200)
                        if (!PrimeNumberProvider.isPrime(num)) composites.add(num)
                    }
                    val allOptions = (composites.toList() + primeNum).shuffled()
                    val correctIdx = allOptions.indexOf(primeNum)

                    list.add(
                        QuizQuestion(
                            id = "prime_q_$idx",
                            questionBn = "নিচের কোন সংখ্যাটি মৌলিক সংখ্যা?",
                            optionsBn = allOptions.map { BengaliNumberFormatter.toBn(it) },
                            correctIndex = correctIdx,
                            explanationBn = "${BengaliNumberFormatter.toBn(primeNum)} একটি মৌলিক সংখ্যা কারণ এর ১ ও এটি ছাড়া অন্য উৎপাদক নেই।",
                            topicTitleBn = "মৌলিক সংখ্যা"
                        )
                    )
                }
                1 -> {
                    // "Which one is composite?"
                    val compositeNum = run {
                        var c = Random.nextInt(4, 200)
                        while (PrimeNumberProvider.isPrime(c)) c = Random.nextInt(4, 200)
                        c
                    }
                    val primes = mutableSetOf<Int>()
                    while (primes.size < 3) {
                        val p = if (Random.nextBoolean()) PrimeNumberProvider.primes1To100.random() else PrimeNumberProvider.primes101To200.random()
                        primes.add(p)
                    }
                    val allOptions = (primes.toList() + compositeNum).shuffled()
                    val correctIdx = allOptions.indexOf(compositeNum)

                    list.add(
                        QuizQuestion(
                            id = "prime_comp_$idx",
                            questionBn = "নিচের কোনটি মৌলিক সংখ্যা নয় (যৌগিক সংখ্যা)?",
                            optionsBn = allOptions.map { BengaliNumberFormatter.toBn(it) },
                            correctIndex = correctIdx,
                            explanationBn = "${BengaliNumberFormatter.toBn(compositeNum)} যৌগিক সংখ্যা। এর গুণনীয়ক ১ এবং নিজেকে ছাড়া আরও রয়েছে।",
                            topicTitleBn = "মৌলিক সংখ্যা"
                        )
                    )
                }
                else -> {
                    // "How many primes in a range?" or specific prime check
                    val num = Random.nextInt(2, 199)
                    val isP = PrimeNumberProvider.isPrime(num)
                    val q = "${BengaliNumberFormatter.toBn(num)} কি মৌলিক সংখ্যা?"
                    val options = listOf("হ্যাঁ, এটি মৌলিক", "না, এটি যৌগিক")
                    val correctIdx = if (isP) 0 else 1
                    val explanation = PrimeNumberProvider.getPrimeInfo(num).explanationBn

                    list.add(
                        QuizQuestion(
                            id = "prime_check_$idx",
                            questionBn = q,
                            optionsBn = options,
                            correctIndex = correctIdx,
                            explanationBn = explanation,
                            topicTitleBn = "মৌলিক সংখ্যা"
                        )
                    )
                }
            }
        }
        return list
    }

    private fun generateSquareCubeQuestions(count: Int): List<QuizQuestion> {
        val list = mutableListOf<QuizQuestion>()
        repeat(count) { idx ->
            val n = Random.nextInt(1, 21)
            val isSquare = Random.nextBoolean()

            if (isSquare) {
                val isRoot = Random.nextBoolean()
                if (isRoot) {
                    val sq = n * n
                    val questionBn = "√${BengaliNumberFormatter.toBn(sq)} এর মান কত?"
                    val correctAns = n
                    val distractors = mutableSetOf<Int>()
                    while (distractors.size < 3) {
                        val fake = Random.nextInt(1, 21)
                        if (fake != correctAns) distractors.add(fake)
                    }
                    val allOptions = (distractors.toList() + correctAns).shuffled()
                    list.add(
                        QuizQuestion(
                            id = "sq_q_$idx",
                            questionBn = questionBn,
                            optionsBn = allOptions.map { BengaliNumberFormatter.toBn(it) },
                            correctIndex = allOptions.indexOf(correctAns),
                            explanationBn = "${BengaliNumberFormatter.toBn(n)}² = ${BengaliNumberFormatter.toBn(sq)}, সুতরাং √${BengaliNumberFormatter.toBn(sq)} = ${BengaliNumberFormatter.toBn(n)}",
                            topicTitleBn = "বর্গ ও বর্গমূল"
                        )
                    )
                } else {
                    val sq = n * n
                    val questionBn = "${BengaliNumberFormatter.toBn(n)}² (বর্গ) এর মান কত?"
                    val correctAns = sq
                    val distractors = mutableSetOf<Int>()
                    while (distractors.size < 3) {
                        val delta = Random.nextInt(-15, 16)
                        val fake = correctAns + delta
                        if (fake > 0 && fake != correctAns) distractors.add(fake)
                    }
                    val allOptions = (distractors.toList() + correctAns).shuffled()
                    list.add(
                        QuizQuestion(
                            id = "sq_val_$idx",
                            questionBn = questionBn,
                            optionsBn = allOptions.map { BengaliNumberFormatter.toBn(it) },
                            correctIndex = allOptions.indexOf(correctAns),
                            explanationBn = "${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} = ${BengaliNumberFormatter.toBn(sq)}",
                            topicTitleBn = "বর্গ ও বর্গমূল"
                        )
                    )
                }
            } else {
                val isRoot = Random.nextBoolean()
                val cb = n * n * n
                if (isRoot) {
                    val questionBn = "∛${BengaliNumberFormatter.toBn(cb)} এর মান কত?"
                    val correctAns = n
                    val distractors = mutableSetOf<Int>()
                    while (distractors.size < 3) {
                        val fake = Random.nextInt(1, 21)
                        if (fake != correctAns) distractors.add(fake)
                    }
                    val allOptions = (distractors.toList() + correctAns).shuffled()
                    list.add(
                        QuizQuestion(
                            id = "cb_root_$idx",
                            questionBn = questionBn,
                            optionsBn = allOptions.map { BengaliNumberFormatter.toBn(it) },
                            correctIndex = allOptions.indexOf(correctAns),
                            explanationBn = "${BengaliNumberFormatter.toBn(n)}³ = ${BengaliNumberFormatter.toBn(cb)}, সুতরাং ∛${BengaliNumberFormatter.toBn(cb)} = ${BengaliNumberFormatter.toBn(n)}",
                            topicTitleBn = "ঘন ও ঘনমূল"
                        )
                    )
                } else {
                    val questionBn = "${BengaliNumberFormatter.toBn(n)}³ (ঘন) এর মান কত?"
                    val correctAns = cb
                    val distractors = mutableSetOf<Int>()
                    while (distractors.size < 3) {
                        val delta = Random.nextInt(-30, 31)
                        val fake = correctAns + delta
                        if (fake > 0 && fake != correctAns) distractors.add(fake)
                    }
                    val allOptions = (distractors.toList() + correctAns).shuffled()
                    list.add(
                        QuizQuestion(
                            id = "cb_val_$idx",
                            questionBn = questionBn,
                            optionsBn = allOptions.map { BengaliNumberFormatter.toBn(it) },
                            correctIndex = allOptions.indexOf(correctAns),
                            explanationBn = "${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} × ${BengaliNumberFormatter.toBn(n)} = ${BengaliNumberFormatter.toBn(cb)}",
                            topicTitleBn = "ঘন ও ঘনমূল"
                        )
                    )
                }
            }
        }
        return list
    }

    private fun generateAlgebraQuestions(count: Int): List<QuizQuestion> {
        val pool = listOf(
            QuizQuestion(
                id = "alg_1",
                questionBn = "(a + b)² এর সঠিক বিস্তার কোনটি?",
                optionsBn = listOf("a² + 2ab + b²", "a² - 2ab + b²", "a² + b²", "a² + ab + b²"),
                correctIndex = 0,
                explanationBn = "(a + b)² = a² + 2ab + b²",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_2",
                questionBn = "a² - b² সমান কোনটি?",
                optionsBn = listOf("(a + b)(a - b)", "(a - b)²", "(a + b)²", "a² - 2ab + b²"),
                correctIndex = 0,
                explanationBn = "বর্গান্তরের সূত্র: a² - b² = (a + b)(a - b)",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_3",
                questionBn = "(a - b)² এর সূত্র কোনটি?",
                optionsBn = listOf("a² - 2ab + b²", "a² + 2ab + b²", "a² - b²", "a² - ab + b²"),
                correctIndex = 0,
                explanationBn = "(a - b)² = a² - 2ab + b²",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_4",
                questionBn = "4ab সমান নিচের কোনটি?",
                optionsBn = listOf("(a + b)² - (a - b)²", "(a + b)² + (a - b)²", "2(a² + b²)", "(a² + b²)/2"),
                correctIndex = 0,
                explanationBn = "4ab = (a + b)² - (a - b)²",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_5",
                questionBn = "2(a² + b²) এর অনুসিদ্ধান্ত কোনটি?",
                optionsBn = listOf("(a + b)² + (a - b)²", "(a + b)² - (a - b)²", "4ab", "(a + b)²"),
                correctIndex = 0,
                explanationBn = "2(a² + b²) = (a + b)² + (a - b)²",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_6",
                questionBn = "(a + b)³ এর সঠিক সূত্র কোনটি?",
                optionsBn = listOf("a³ + 3a²b + 3ab² + b³", "a³ - 3a²b + 3ab² - b³", "a³ + b³ + ab", "a³ + b³"),
                correctIndex = 0,
                explanationBn = "(a + b)³ = a³ + 3a²b + 3ab² + b³",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_7",
                questionBn = "a³ + b³ এর উৎপাদক সূত্র কোনটি?",
                optionsBn = listOf("(a + b)(a² - ab + b²)", "(a - b)(a² + ab + b²)", "(a + b)³ - 3ab", "(a + b)(a² + b²)"),
                correctIndex = 0,
                explanationBn = "a³ + b³ = (a + b)(a² - ab + b²)",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_8",
                questionBn = "a³ - b³ এর উৎপাদক সূত্র কোনটি?",
                optionsBn = listOf("(a - b)(a² + ab + b²)", "(a + b)(a² - ab + b²)", "(a - b)³ + 3ab", "(a - b)(a² - b²)"),
                correctIndex = 0,
                explanationBn = "a³ - b³ = (a - b)(a² + ab + b²)",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_9",
                questionBn = "(a - b)³ এর সঠিক সূত্র কোনটি?",
                optionsBn = listOf("a³ - 3a²b + 3ab² - b³", "a³ + 3a²b - 3ab² + b³", "a³ - b³ - 3ab", "a³ - b³"),
                correctIndex = 0,
                explanationBn = "(a - b)³ = a³ - 3a²b + 3ab² - b³ = a³ - b³ - 3ab(a - b)",
                topicTitleBn = "বীজগণিত সূত্র"
            ),
            QuizQuestion(
                id = "alg_10",
                questionBn = "(a + b + c)² সমান নিচের কোনটি?",
                optionsBn = listOf(
                    "a² + b² + c² + 2(ab + bc + ca)",
                    "a² + b² + c² + ab + bc + ca",
                    "(a + b)² + c²",
                    "a² + b² + c² - 2(ab + bc + ca)"
                ),
                correctIndex = 0,
                explanationBn = "(a + b + c)² = a² + b² + c² + 2(ab + bc + ca)",
                topicTitleBn = "বীজগণিত সূত্র"
            )
        )
        return pool.shuffled().take(count)
    }
}
