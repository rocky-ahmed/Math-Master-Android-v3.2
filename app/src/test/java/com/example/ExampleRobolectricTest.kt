package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.math.AlgebraFormulaProvider
import com.example.math.BengaliNumberFormatter
import com.example.math.FormulaLibraryProvider
import com.example.math.FormulaSubject
import com.example.math.MentalMathCategory
import com.example.math.MentalMathDifficulty
import com.example.math.MentalMathGenerator
import com.example.math.MentalMathMode
import com.example.math.MultiplicationProvider
import com.example.math.PracticeCategory
import com.example.math.PracticeDifficulty
import com.example.math.PracticeLabGenerator
import com.example.math.PrimeNumberProvider
import com.example.math.QuizGenerator
import com.example.math.QuizTopic
import com.example.math.SquareCubeProvider
import com.example.math.TableRange
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `verify app name resource`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Math Master", appName)
    }

    @Test
    fun `verify MainActivity launches without crash`() {
        val controller = org.robolectric.Robolectric.buildActivity(MainActivity::class.java).setup()
        assertNotNull(controller.get())
    }

    @Test
    fun `verify multiplication tables 1 to 40 in 4 ranges`() {
        assertEquals(4, TableRange.values().size)

        val range1 = MultiplicationProvider.getTablesForRange(TableRange.RANGE_1_10)
        val range2 = MultiplicationProvider.getTablesForRange(TableRange.RANGE_11_20)
        val range3 = MultiplicationProvider.getTablesForRange(TableRange.RANGE_21_30)
        val range4 = MultiplicationProvider.getTablesForRange(TableRange.RANGE_31_40)

        assertEquals(10, range1.size)
        assertEquals(10, range2.size)
        assertEquals(10, range3.size)
        assertEquals(10, range4.size)

        // Verify table 7
        val table7 = MultiplicationProvider.getTable(7)
        assertEquals(10, table7.rows.size)
        assertEquals(7, table7.rows[0].product) // 7 * 1
        assertEquals(70, table7.rows[9].product) // 7 * 10
    }

    @Test
    fun `verify prime numbers count in ranges`() {
        val primes1To100 = PrimeNumberProvider.primes1To100
        val primes101To200 = PrimeNumberProvider.primes101To200

        assertEquals(25, primes1To100.size)
        assertEquals(21, primes101To200.size)

        assertTrue(PrimeNumberProvider.isPrime(2))
        assertTrue(PrimeNumberProvider.isPrime(97))
        assertTrue(PrimeNumberProvider.isPrime(101))
        assertTrue(PrimeNumberProvider.isPrime(199))

        assertFalse(PrimeNumberProvider.isPrime(1))
        assertFalse(PrimeNumberProvider.isPrime(4))
        assertFalse(PrimeNumberProvider.isPrime(91)) // 7 * 13
        assertFalse(PrimeNumberProvider.isPrime(119)) // 7 * 17
    }

    @Test
    fun `verify squares and cubes from 1 to 20`() {
        val squareList = SquareCubeProvider.squareTableList
        val cubeList = SquareCubeProvider.cubeTableList

        assertEquals(20, squareList.size) // 1 to 20
        assertEquals(20, cubeList.size)   // 1 to 20

        val item1 = squareList.first { it.n == 1 }
        assertEquals("1² = 1", item1.squareText)
        assertEquals("√1 = 1", item1.squareRootText)

        val item12 = squareList.first { it.n == 12 }
        assertEquals("12² = 144", item12.squareText)
        assertEquals("√144 = 12", item12.squareRootText)

        val item20 = squareList.first { it.n == 20 }
        assertEquals("20² = 400", item20.squareText)
        assertEquals("√400 = 20", item20.squareRootText)

        val cube1 = cubeList.first { it.n == 1 }
        assertEquals("1³ = 1", cube1.cubeText)
        assertEquals("∛1 = 1", cube1.cubeRootText)

        val cube10 = cubeList.first { it.n == 10 }
        assertEquals("10³ = 1000", cube10.cubeText)
        assertEquals("∛1000 = 10", cube10.cubeRootText)

        val cube20 = cubeList.first { it.n == 20 }
        assertEquals("20³ = 8000", cube20.cubeText)
        assertEquals("∛8000 = 20", cube20.cubeRootText)
    }

    @Test
    fun `verify algebraic square and cube formulas`() {
        val squares = AlgebraFormulaProvider.squareFormulas
        val cubes = AlgebraFormulaProvider.cubeFormulas

        assertEquals(14, squares.size) // Exact 14 square formulas
        assertEquals(8, cubes.size)     // Exact 8 cube formulas

        assertTrue(squares.any { it.formulaLatex.contains("(a + b)² = a² + 2ab + b²") })
        assertTrue(squares.any { it.formulaLatex.contains("(a − b − c)²") })
        assertTrue(cubes.any { it.formulaLatex.contains("(a + b)³ = a³ + 3a²b + 3ab² + b³") })
        assertTrue(cubes.any { it.formulaLatex.contains("a³ − b³ = (a − b)(a² + ab + b²)") })
    }

    @Test
    fun `verify quiz question generator`() {
        val questions = QuizGenerator.generateQuiz(QuizTopic.MULTIPLICATION, 5)
        assertEquals(5, questions.size)
        questions.forEach { q ->
            assertEquals(4, q.optionsBn.size)
            assertTrue(q.correctIndex in 0..3)
        }
    }

    @Test
    fun `verify bengali numeral converter`() {
        assertEquals("০", BengaliNumberFormatter.toBn(0))
        assertEquals("১২৩৪৫", BengaliNumberFormatter.toBn(12345))
        assertEquals("১০০", BengaliNumberFormatter.toBn(100))
    }

    @Test
    fun `verify math viewmodel creation and streak initialization`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)
        assertTrue(viewModel.streakDays.value >= 1)
        assertTrue(viewModel.longestStreak.value >= 1)
        assertEquals(7, viewModel.weeklyActivities.value.size)
        assertEquals(com.example.ui.Screen.Home, viewModel.currentScreen)
    }

    @Test
    fun `verify prime numbers magic code for 1 to 100 and 101 to 200`() {
        val magic100 = PrimeNumberProvider.magicCode1To100
        assertEquals("4422322321", magic100.codeString)
        assertEquals(25, magic100.totalPrimes)
        assertEquals(10, magic100.decadeGroups.size)
        assertEquals(25, magic100.decadeGroups.sumOf { it.magicDigit })
        assertEquals(listOf(4, 4, 2, 2, 3, 2, 2, 3, 2, 1), magic100.decadeGroups.map { it.magicDigit })

        val magic200 = PrimeNumberProvider.magicCode101To200
        assertEquals("4113122214", magic200.codeString)
        assertEquals(21, magic200.totalPrimes)
        assertEquals(10, magic200.decadeGroups.size)
        assertEquals(21, magic200.decadeGroups.sumOf { it.magicDigit })
        assertEquals(listOf(4, 1, 1, 3, 1, 2, 2, 2, 1, 4), magic200.decadeGroups.map { it.magicDigit })

        // Verify first decade of 1-100 has 4 primes: 2, 3, 5, 7
        assertEquals(listOf(2, 3, 5, 7), magic100.decadeGroups[0].primes)
        // Verify last decade of 1-100 has 1 prime: 97
        assertEquals(listOf(97), magic100.decadeGroups[9].primes)

        // Verify 101-110 has 4 primes: 101, 103, 107, 109
        assertEquals(listOf(101, 103, 107, 109), magic200.decadeGroups[0].primes)
        // Verify 191-200 has 4 primes: 191, 193, 197, 199
        assertEquals(listOf(191, 193, 197, 199), magic200.decadeGroups[9].primes)
    }

    @Test
    fun `verify customization state and persistence`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        // Initial default state
        assertEquals(com.example.ui.customization.CustomLayoutType.COMFORTABLE_LIST, viewModel.customizationState.value.global.layoutType)
        assertTrue(viewModel.customizationState.value.sections.isEmpty())

        // Update preview layout to GRID and configure section
        viewModel.updatePreviewGlobal { it.copy(layoutType = com.example.ui.customization.CustomLayoutType.GRID, gridColumns = 3) }
        viewModel.updatePreviewSection(com.example.ui.customization.SectionKey.TABLES) {
            it.copy(iconId = com.example.ui.customization.AvailableIcon.CALCULATE.id, iconSizeDp = 32, cornerRadiusDp = 20)
        }

        assertEquals(com.example.ui.customization.CustomLayoutType.GRID, viewModel.previewCustomizationState.value.global.layoutType)
        assertEquals(3, viewModel.previewCustomizationState.value.global.gridColumns)
        val secCfg = viewModel.previewCustomizationState.value.getSectionConfig(com.example.ui.customization.SectionKey.TABLES)
        assertEquals("calculate", secCfg.iconId)
        assertEquals(32, secCfg.iconSizeDp)
        assertEquals(20, secCfg.cornerRadiusDp)

        // Save customization
        viewModel.saveCustomization()
        assertEquals(com.example.ui.customization.CustomLayoutType.GRID, viewModel.customizationState.value.global.layoutType)

        // Discard preview returns to saved
        viewModel.updatePreviewGlobal { it.copy(layoutType = com.example.ui.customization.CustomLayoutType.COMPACT_LIST) }
        viewModel.discardCustomizationPreview()
        assertEquals(com.example.ui.customization.CustomLayoutType.GRID, viewModel.previewCustomizationState.value.global.layoutType)

        // Verify independent section reset
        viewModel.resetPreviewSection(com.example.ui.customization.SectionKey.TABLES)
        assertNull(viewModel.previewCustomizationState.value.sections[com.example.ui.customization.SectionKey.TABLES.id])

        // Verify reset all customizations restores defaults
        viewModel.resetAllCustomizations()
        assertEquals(com.example.ui.customization.CustomLayoutType.COMFORTABLE_LIST, viewModel.customizationState.value.global.layoutType)
        assertTrue(viewModel.customizationState.value.sections.isEmpty())
    }

    @Test
    fun `verify color contrast and hex validation`() {
        assertTrue(com.example.ui.customization.ColorUtils.isValidHex("#1E3A8A"))
        assertTrue(com.example.ui.customization.ColorUtils.isValidHex("1E3A8A"))
        assertFalse(com.example.ui.customization.ColorUtils.isValidHex("invalid_hex"))

        val darkBlue = com.example.ui.customization.ColorUtils.parseHexColor("#1E3A8A")
        assertNotNull(darkBlue)
        assertTrue(com.example.ui.customization.ColorUtils.hasAdequateContrast(androidx.compose.ui.graphics.Color.White, darkBlue!!))
    }

    @Test
    fun `verify theme mode persistence across ViewModel instances`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel1 = com.example.ui.MathViewModel(app)

        // Set Dark theme
        viewModel1.setThemeMode(com.example.ui.theme.AppThemeMode.DARK)
        assertEquals(com.example.ui.theme.AppThemeMode.DARK, viewModel1.themeMode.value)

        // New ViewModel instance should restore DARK from SharedPreferences
        val viewModel2 = com.example.ui.MathViewModel(app)
        assertEquals(com.example.ui.theme.AppThemeMode.DARK, viewModel2.themeMode.value)

        // Set Light theme
        viewModel2.setThemeMode(com.example.ui.theme.AppThemeMode.LIGHT)
        assertEquals(com.example.ui.theme.AppThemeMode.LIGHT, viewModel2.themeMode.value)

        val viewModel3 = com.example.ui.MathViewModel(app)
        assertEquals(com.example.ui.theme.AppThemeMode.LIGHT, viewModel3.themeMode.value)

        // Set System default
        viewModel3.setThemeMode(com.example.ui.theme.AppThemeMode.SYSTEM)
        assertEquals(com.example.ui.theme.AppThemeMode.SYSTEM, viewModel3.themeMode.value)
    }

    @Test
    fun `verify full quiz 10 of 10 completion, final score, next quiz, retry, review, and return to quiz list`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        // Start Multiplication Quiz
        viewModel.startQuiz(QuizTopic.MULTIPLICATION, 10)
        var session = viewModel.quizSession.value
        assertEquals(10, session.questions.size)
        assertEquals(0, session.currentIndex)
        assertFalse(session.isFinished)
        assertEquals(com.example.ui.Screen.QuizPlay, viewModel.currentScreen)

        // Answer all 10 questions correctly
        for (i in 0 until 10) {
            val q = viewModel.quizSession.value.questions[i]
            // Select correct option
            viewModel.selectQuizOption(q.correctIndex)
            assertEquals(q.correctIndex, viewModel.quizSession.value.selectedOptionIndex)

            // Submit answer
            viewModel.submitQuizAnswer()
            assertTrue(viewModel.quizSession.value.isAnswerSubmitted)
            assertEquals(i + 1, viewModel.quizSession.value.correctCount)

            // Advance to next question (or finish on the 10th question)
            viewModel.nextQuizQuestion()
        }

        // Quiz must be finished!
        session = viewModel.quizSession.value
        assertTrue("Quiz must be finished after question 10", session.isFinished)
        assertEquals(10, session.correctCount)
        assertEquals(10, session.questions.size)
        val percentage = (session.correctCount * 100) / session.questions.size
        assertEquals(100, percentage)

        // Verify Review Answers toggle
        assertFalse(session.isReviewMode)
        viewModel.toggleQuizReviewMode()
        assertTrue(viewModel.quizSession.value.isReviewMode)
        viewModel.toggleQuizReviewMode()
        assertFalse(viewModel.quizSession.value.isReviewMode)

        // Verify Next Quiz Topic button starts next topic cleanly
        viewModel.startNextQuizTopic()
        val nextSession = viewModel.quizSession.value
        assertFalse(nextSession.isFinished)
        assertEquals(0, nextSession.currentIndex)
        assertEquals(10, nextSession.questions.size)
        assertEquals(QuizTopic.PRIMES, nextSession.topic)

        // Verify Retry Quiz button restarts current topic
        viewModel.startQuiz(QuizTopic.PRIMES, 10)
        val retrySession = viewModel.quizSession.value
        assertFalse(retrySession.isFinished)
        assertEquals(0, retrySession.currentIndex)
        assertEquals(QuizTopic.PRIMES, retrySession.topic)

        // Verify Return to Quiz List exits cleanly
        viewModel.exitQuizToTopicList()
        assertEquals(com.example.ui.Screen.Quizzes, viewModel.currentScreen)
        assertFalse(viewModel.quizSession.value.isFinished)
        assertTrue(viewModel.quizSession.value.questions.isEmpty())
    }

    @Test
    fun `verify quiz session state with partial score 7 of 10`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        viewModel.startQuiz(QuizTopic.SQUARES_CUBES, 10)
        for (i in 0 until 10) {
            val q = viewModel.quizSession.value.questions[i]
            // Answer first 7 correctly, last 3 incorrectly
            val chosenOpt = if (i < 7) q.correctIndex else (q.correctIndex + 1) % 4
            viewModel.selectQuizOption(chosenOpt)
            viewModel.submitQuizAnswer()
            viewModel.nextQuizQuestion()
        }

        val session = viewModel.quizSession.value
        assertTrue(session.isFinished)
        assertEquals(7, session.correctCount)
        assertEquals(10, session.questions.size)
        val percentage = (session.correctCount * 100) / session.questions.size
        assertEquals(70, percentage)
    }

    @Test
    fun `verify all customization options and categories reset`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        // Test all Layout types
        com.example.ui.customization.CustomLayoutType.values().forEach { layoutType ->
            viewModel.updatePreviewGlobal { it.copy(layoutType = layoutType) }
            assertEquals(layoutType, viewModel.previewCustomizationState.value.global.layoutType)
        }

        // Test all Font weights
        com.example.ui.customization.CustomFontWeight.values().forEach { weight ->
            viewModel.updatePreviewGlobal { it.copy(fontWeight = weight) }
            assertEquals(weight, viewModel.previewCustomizationState.value.global.fontWeight)
        }

        // Test numerical parameters
        viewModel.updatePreviewGlobal {
            it.copy(
                gridColumns = 4,
                cornerRadiusDp = 24,
                horizontalGapDp = 18,
                verticalGapDp = 18,
                titleFontSizeSp = 20,
                subtitleFontSizeSp = 14,
                contentAlignmentCenter = true,
                showBorder = true,
                borderThicknessDp = 2,
                customPrimaryActionHex = "#1E3A8A",
                customLightBackgroundHex = "#F1F5F9",
                customDarkBackgroundHex = "#0B0F19"
            )
        }

        val g = viewModel.previewCustomizationState.value.global
        assertEquals(4, g.gridColumns)
        assertEquals(24, g.cornerRadiusDp)
        assertEquals(20, g.titleFontSizeSp)
        assertEquals(14, g.subtitleFontSizeSp)
        assertTrue(g.contentAlignmentCenter)
        assertTrue(g.showBorder)
        assertEquals(2, g.borderThicknessDp)
        assertEquals("#1E3A8A", g.customPrimaryActionHex)
        assertEquals("#F1F5F9", g.customLightBackgroundHex)
        assertEquals("#0B0F19", g.customDarkBackgroundHex)

        // Test reset category: LAYOUT
        viewModel.resetPreviewCategory("LAYOUT")
        assertEquals(com.example.ui.customization.CustomLayoutType.COMFORTABLE_LIST, viewModel.previewCustomizationState.value.global.layoutType)
        assertEquals(2, viewModel.previewCustomizationState.value.global.gridColumns)
        assertFalse(viewModel.previewCustomizationState.value.global.contentAlignmentCenter)
        // Typography and colors should remain untouched
        assertEquals(20, viewModel.previewCustomizationState.value.global.titleFontSizeSp)
        assertEquals("#1E3A8A", viewModel.previewCustomizationState.value.global.customPrimaryActionHex)

        // Test reset category: TYPOGRAPHY
        viewModel.resetPreviewCategory("TYPOGRAPHY")
        assertEquals(16, viewModel.previewCustomizationState.value.global.titleFontSizeSp)
        assertEquals(12, viewModel.previewCustomizationState.value.global.subtitleFontSizeSp)
        assertEquals(com.example.ui.customization.CustomFontWeight.MEDIUM, viewModel.previewCustomizationState.value.global.fontWeight)

        // Test reset category: GLOBAL_THEME
        viewModel.resetPreviewCategory("GLOBAL_THEME")
        assertNull(viewModel.previewCustomizationState.value.global.customPrimaryActionHex)
        assertNull(viewModel.previewCustomizationState.value.global.customLightBackgroundHex)
        assertNull(viewModel.previewCustomizationState.value.global.customDarkBackgroundHex)
    }

    @Test
    fun `verify formula library categorizes math formulas in algebra geometry and calculus in bengali`() {
        val all = FormulaLibraryProvider.allFormulas
        val algebra = FormulaLibraryProvider.getFormulasBySubject(FormulaSubject.ALGEBRA)
        val geometry = FormulaLibraryProvider.getFormulasBySubject(FormulaSubject.GEOMETRY)
        val calculus = FormulaLibraryProvider.getFormulasBySubject(FormulaSubject.CALCULUS)

        assertTrue("All formulas must be at least 40 items", all.size >= 40)
        assertTrue("Algebra formulas must have at least 15 items", algebra.size >= 15)
        assertTrue("Geometry formulas must have at least 15 items", geometry.size >= 15)
        assertTrue("Calculus formulas must have at least 14 items", calculus.size >= 14)

        // Verify Bengali categorization and subject names
        assertEquals("বীজগণিত", FormulaSubject.ALGEBRA.titleBn)
        assertEquals("জ্যামিতি", FormulaSubject.GEOMETRY.titleBn)
        assertEquals("ক্যালকুলাস", FormulaSubject.CALCULUS.titleBn)

        // Verify each formula contains necessary student reference fields
        all.forEach { f ->
            assertTrue("Formula ID must not be empty", f.id.isNotEmpty())
            assertTrue("Formula titleBn must not be empty", f.titleBn.isNotEmpty())
            assertTrue("Formula expression must not be empty", f.formulaExpression.isNotEmpty())
            assertTrue("Formula explanationBn must not be empty", f.explanationBn.isNotEmpty())
            assertTrue("Formula example problem must not be empty", f.exampleProblemBn.isNotEmpty())
            assertTrue("Formula example solution must not be empty", f.exampleSolutionBn.isNotEmpty())
            assertTrue("Formula tags must not be empty", f.tags.isNotEmpty())
        }

        // Verify key Algebra formulas
        assertTrue(algebra.any { it.formulaExpression.contains("(a + b)² = a² + 2ab + b²") })
        assertTrue(algebra.any { it.formulaExpression.contains("a² − b² = (a + b)(a − b)") })
        assertTrue(algebra.any { it.formulaExpression.contains("aᵐ · aⁿ = aᵐ⁺ⁿ") })
        assertTrue(algebra.any { it.formulaExpression.contains("Tₙ = a + (n − 1)d") })
        assertTrue(algebra.any { it.formulaExpression.contains("x = [−b ± √(b² − 4ac)] / (2a)") })

        // Verify key Geometry formulas
        assertTrue(geometry.any { it.formulaExpression.contains("½ × ভূমি × উচ্চতা") || it.formulaExpression.contains("½ · b · h") })
        assertTrue(geometry.any { it.formulaExpression.contains("c² = a² + b²") })
        assertTrue(geometry.any { it.formulaExpression.contains("2πr") && it.formulaExpression.contains("πr²") })
        assertTrue(geometry.any { it.formulaExpression.contains("4πr²") && it.formulaExpression.contains("(4/3)πr³") })
        assertTrue(geometry.any { it.formulaExpression.contains("√[(x₂ − x₁)² + (y₂ − y₁)²]") })

        // Verify key Calculus formulas
        assertTrue(calculus.any { it.formulaExpression.contains("d/dx (xⁿ) = n · xⁿ⁻¹") })
        assertTrue(calculus.any { it.formulaExpression.contains("u(dv/dx) + v(du/dx)") })
        assertTrue(calculus.any { it.formulaExpression.contains("d/dx(sin x) = cos x") })
        assertTrue(calculus.any { it.formulaExpression.contains("∫ xⁿ dx = (xⁿ⁺¹ / (n + 1)) + C") })
        assertTrue(calculus.any { it.formulaExpression.contains("lim(x→0) [sin x / x] = 1") })
    }

    @Test
    fun `verify formula library bookmark integration and screen navigation`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        // Test navigation to FormulaLibrary
        viewModel.navigateTo(com.example.ui.Screen.FormulaLibrary)
        assertEquals(com.example.ui.Screen.FormulaLibrary, viewModel.currentScreen)

        // Test bookmarking a library formula
        val formula = FormulaLibraryProvider.allFormulas.first()
        val bookmark = com.example.data.model.BookmarkEntity(
            id = formula.id,
            category = "FORMULA_LIBRARY",
            titleBn = formula.titleBn,
            subtitleBn = "${formula.subject.titleBn} • ${formula.subCategory}",
            detailBn = formula.formulaExpression
        )

        assertFalse(viewModel.isBookmarked(formula.id))
        viewModel.toggleBookmark(bookmark)

        // Verify navigateBack
        assertTrue(viewModel.navigateBack())
        assertEquals(com.example.ui.Screen.Home, viewModel.currentScreen)
    }

    @Test
    fun `verify practice lab generates mathematically valid questions for all 8 categories across all 3 difficulty levels`() {
        assertEquals(8, PracticeCategory.values().size)
        assertEquals(3, PracticeDifficulty.values().size)

        PracticeCategory.values().forEach { cat ->
            PracticeDifficulty.values().forEach { diff ->
                val questions = PracticeLabGenerator.generateSession(cat, diff, 5)
                assertEquals("Each category ($cat, $diff) must produce 5 questions", 5, questions.size)

                questions.forEach { q ->
                    assertEquals(cat, q.category)
                    assertEquals(diff, q.difficulty)
                    assertTrue("Question must have non-empty expression", q.expression.isNotEmpty())
                    assertTrue("Question must have non-empty Bengali text", q.questionBn.isNotEmpty())
                    assertEquals("Question must provide exactly 4 options", 4, q.optionsBn.size)
                    assertTrue("Correct index must be within 0..3", q.correctIndex in 0..3)
                    assertEquals("Correct option must match correctAnswerBn", q.optionsBn[q.correctIndex], q.correctAnswerBn)
                    assertTrue("Explanation must have at least 1 step", q.stepByStepExplanationBn.isNotEmpty())
                }
            }
        }
    }

    @Test
    fun `verify practice lab session execution answer checking duplicate submit protection and completion stats`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        // Start session: Fractions, Medium, 5 questions
        viewModel.startPracticeSession(PracticeCategory.FRACTIONS, PracticeDifficulty.MEDIUM, 5)
        var session = viewModel.practiceSession.value
        assertEquals(5, session.questions.size)
        assertEquals(0, session.currentIndex)
        assertFalse(session.isAnswerSubmitted)
        assertFalse(session.isFinished)
        assertEquals(com.example.ui.Screen.PracticeLabPlay, viewModel.currentScreen)

        // Answer question 1 correctly
        val q0 = session.questions[0]
        viewModel.selectPracticeOption(q0.correctIndex)
        viewModel.submitPracticeAnswer()
        session = viewModel.practiceSession.value
        assertTrue(session.isAnswerSubmitted)
        assertTrue(session.isCorrect)
        assertEquals(1, session.correctCount)
        assertEquals(0, session.wrongCount)

        // Duplicate submit attempt should NOT increment score again
        viewModel.submitPracticeAnswer()
        session = viewModel.practiceSession.value
        assertEquals(1, session.correctCount)

        // Advance to Q2
        viewModel.nextPracticeQuestion()
        session = viewModel.practiceSession.value
        assertEquals(1, session.currentIndex)
        assertFalse(session.isAnswerSubmitted)

        // Answer Q2 incorrectly
        val q1 = session.questions[1]
        val wrongOpt = (q1.correctIndex + 1) % 4
        viewModel.selectPracticeOption(wrongOpt)
        viewModel.submitPracticeAnswer()
        session = viewModel.practiceSession.value
        assertTrue(session.isAnswerSubmitted)
        assertFalse(session.isCorrect)
        assertEquals(1, session.correctCount)
        assertEquals(1, session.wrongCount)

        // Answer remaining 3 questions correctly
        for (i in 2 until 5) {
            viewModel.nextPracticeQuestion()
            val q = viewModel.practiceSession.value.questions[i]
            viewModel.selectPracticeOption(q.correctIndex)
            viewModel.submitPracticeAnswer()
        }

        // Finish session on last question
        viewModel.nextPracticeQuestion()
        session = viewModel.practiceSession.value
        assertTrue("Session must be finished after question 5", session.isFinished)
        assertEquals(4, session.correctCount)
        assertEquals(1, session.wrongCount)
        assertEquals(5, session.questions.size)
    }

    @Test
    fun `verify practice lab retry review mode and category exit navigation`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        viewModel.startPracticeSession(PracticeCategory.PERCENTAGES, PracticeDifficulty.EASY, 5)
        var session = viewModel.practiceSession.value
        assertEquals(PracticeCategory.PERCENTAGES, session.category)

        // Toggle review mode
        assertFalse(session.isReviewMode)
        viewModel.togglePracticeReviewMode()
        assertTrue(viewModel.practiceSession.value.isReviewMode)
        viewModel.togglePracticeReviewMode()
        assertFalse(viewModel.practiceSession.value.isReviewMode)

        // Retry session
        viewModel.retryPracticeSession()
        session = viewModel.practiceSession.value
        assertEquals(0, session.currentIndex)
        assertFalse(session.isFinished)
        assertEquals(PracticeCategory.PERCENTAGES, session.category)

        // Exit practice lab
        viewModel.exitPracticeToCategories()
        assertFalse(viewModel.practiceSession.value.isFinished)
        assertTrue(viewModel.practiceSession.value.questions.isEmpty())
        assertEquals(com.example.ui.Screen.PracticeLab, viewModel.currentScreen)
    }

    @Test
    fun `verify mental math arithmetic correctness across all 6 categories and 3 difficulties`() {
        assertEquals(6, MentalMathCategory.values().size)
        assertEquals(3, MentalMathDifficulty.values().size)

        MentalMathCategory.values().forEach { cat ->
            MentalMathDifficulty.values().forEach { diff ->
                val questions = MentalMathGenerator.generateQuestions(cat, diff, 5)
                assertEquals("Should generate 5 questions for $cat ($diff)", 5, questions.size)

                questions.forEach { q ->
                    assertEquals(cat, q.category)
                    assertEquals(diff, q.difficulty)
                    assertTrue("Expression must not be empty", q.expressionBn.isNotEmpty())
                    assertEquals("Must have 4 distinct options", 4, q.options.toSet().size)
                    assertEquals("Must have 4 Bengali options", 4, q.optionsBn.size)
                    assertTrue("Correct index must be in 0..3", q.correctIndex in 0..3)
                    assertEquals("Option at correctIndex must equal correctAnswer", q.correctAnswer, q.options[q.correctIndex])
                    assertEquals("CorrectAnswerBn must match Bengali formatter", BengaliNumberFormatter.toBn(q.correctAnswer), q.correctAnswerBn)
                    assertTrue("Correct answer must be positive", q.correctAnswer > 0)
                }
            }
        }
    }

    @Test
    fun `verify division edge cases in mental math - no division by zero, exact integer quotients, positive dividends`() {
        listOf(MentalMathDifficulty.EASY, MentalMathDifficulty.MEDIUM, MentalMathDifficulty.HARD).forEach { diff ->
            val divQuestions = MentalMathGenerator.generateQuestions(MentalMathCategory.DIVISION, diff, 20)
            assertEquals(20, divQuestions.size)

            divQuestions.forEach { q ->
                // Plain expression is formatted as "$dividend / $divisor"
                val parts = q.expressionPlain.split("/")
                assertEquals(2, parts.size)
                val dividend = parts[0].trim().toInt()
                val divisor = parts[1].trim().toInt()

                assertTrue("Divisor must never be zero", divisor > 0)
                assertTrue("Dividend must be greater than divisor", dividend >= divisor)
                assertEquals("Dividend must be evenly divisible by divisor", 0, dividend % divisor)
                assertEquals("Quotient must match correctAnswer", dividend / divisor, q.correctAnswer)
                assertTrue("All options must be positive integers", q.options.all { it > 0 })
            }
        }
    }

    @Test
    fun `verify mental math scoring, combo multiplier, streak, and duplicate answer prevention`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        // Start Fixed Questions challenge: Addition, Medium (+20 base points), 5 questions
        viewModel.startMentalMathChallenge(
            category = MentalMathCategory.ADDITION,
            difficulty = MentalMathDifficulty.MEDIUM,
            mode = MentalMathMode.FIXED_QUESTIONS,
            durationOrCount = 5
        )

        var session = viewModel.mentalMathSession.value
        assertEquals(5, session.questions.size)
        assertEquals(0, session.currentIndex)
        assertEquals(0, session.score)
        assertEquals(0, session.streak)
        assertFalse(session.isFinished)
        assertEquals(com.example.ui.Screen.MentalMathPlay, viewModel.currentScreen)

        // Question 1: Answer correctly (streak 1, 1.0x -> +20 pts)
        val q0 = session.questions[0]
        viewModel.submitMentalMathAnswer(q0.correctAnswer)
        session = viewModel.mentalMathSession.value
        assertTrue(session.isAnswerSubmitted)
        assertEquals(true, session.lastAnswerCorrect)
        assertEquals(20, session.score)
        assertEquals(1, session.streak)
        assertEquals(1, session.correctCount)

        // Duplicate answer tap protection: must NOT increase score or streak again
        viewModel.submitMentalMathAnswer(q0.correctAnswer)
        session = viewModel.mentalMathSession.value
        assertEquals(20, session.score)
        assertEquals(1, session.correctCount)

        // Next to Question 2
        viewModel.nextMentalMathQuestion()
        val q1 = viewModel.mentalMathSession.value.questions[1]
        // Question 2: Answer correctly (streak 2, 1.5x -> +30 pts, total 50)
        viewModel.submitMentalMathAnswer(q1.correctAnswer)
        session = viewModel.mentalMathSession.value
        assertEquals(50, session.score)
        assertEquals(2, session.streak)
        assertEquals(2, session.correctCount)

        // Next to Question 3
        viewModel.nextMentalMathQuestion()
        val q2 = viewModel.mentalMathSession.value.questions[2]
        // Question 3: Answer incorrectly
        val wrongAns = q2.options.first { it != q2.correctAnswer }
        viewModel.submitMentalMathAnswer(wrongAns)
        session = viewModel.mentalMathSession.value
        assertEquals(50, session.score) // Score unchanged
        assertEquals(0, session.streak) // Streak reset to 0
        assertEquals(2, session.highestStreak) // Highest streak retained
        assertEquals(2, session.correctCount)
        assertEquals(1, session.wrongCount)

        // Next to Question 4
        viewModel.nextMentalMathQuestion()
        val q3 = viewModel.mentalMathSession.value.questions[3]
        viewModel.submitMentalMathAnswer(q3.correctAnswer) // +20 (streak 1) -> 70
        assertEquals(70, viewModel.mentalMathSession.value.score)

        // Next to Question 5
        viewModel.nextMentalMathQuestion()
        val q4 = viewModel.mentalMathSession.value.questions[4]
        viewModel.submitMentalMathAnswer(q4.correctAnswer) // +30 (streak 2, 1.5x) -> 100
        assertEquals(100, viewModel.mentalMathSession.value.score)

        // Advance after last question -> Finished!
        viewModel.nextMentalMathQuestion()
        session = viewModel.mentalMathSession.value
        assertTrue("Session must be finished after question 5", session.isFinished)
        assertEquals(4, session.correctCount)
        assertEquals(1, session.wrongCount)
        assertEquals(100, session.score)
    }

    @Test
    fun `verify mental math timer countdown and completion when time reaches zero`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)
        val testClock = object : com.example.math.MonotonicClock {
            var timeMs = 100000L
            override fun elapsedRealtime() = timeMs
        }
        viewModel.clock = testClock

        // Start Timed Sprint with 3 seconds
        viewModel.startMentalMathChallenge(
            category = MentalMathCategory.MULTIPLICATION,
            difficulty = MentalMathDifficulty.EASY,
            mode = MentalMathMode.TIMED_SPRINT,
            durationOrCount = 3
        )

        var session = viewModel.mentalMathSession.value
        assertEquals(3, session.timeRemainingSeconds)
        assertEquals(0, session.elapsedSeconds)
        assertFalse(session.isFinished)

        // Tick 1 (1 second passes)
        testClock.timeMs += 1000L
        viewModel.tickMentalMathTimer()
        session = viewModel.mentalMathSession.value
        assertEquals(2, session.timeRemainingSeconds)
        assertEquals(1, session.elapsedSeconds)
        assertFalse(session.isFinished)

        // Tick 2 (1 second passes)
        testClock.timeMs += 1000L
        viewModel.tickMentalMathTimer()
        session = viewModel.mentalMathSession.value
        assertEquals(1, session.timeRemainingSeconds)
        assertEquals(2, session.elapsedSeconds)
        assertFalse(session.isFinished)

        // Tick 3: Reaches 0 -> Finished!
        testClock.timeMs += 1000L
        viewModel.tickMentalMathTimer()
        session = viewModel.mentalMathSession.value
        assertEquals(0, session.timeRemainingSeconds)
        assertEquals(3, session.elapsedSeconds)
        assertTrue("Challenge must be finished when time reaches 0", session.isFinished)
    }

    @Test
    fun `verify delayed timer updates do not extend session and remaining time never becomes negative`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)
        val testClock = object : com.example.math.MonotonicClock {
            var timeMs = 100000L
            override fun elapsedRealtime() = timeMs
        }
        viewModel.clock = testClock

        // Start 60-second sprint
        viewModel.startMentalMathChallenge(
            category = MentalMathCategory.ADDITION,
            difficulty = MentalMathDifficulty.EASY,
            mode = MentalMathMode.TIMED_SPRINT,
            durationOrCount = 60
        )

        assertEquals(60, viewModel.mentalMathSession.value.timeRemainingSeconds)

        // Simulate 20 seconds delay (e.g. app backgrounded or coroutine delayed)
        testClock.timeMs += 20000L
        viewModel.tickMentalMathTimer()

        // Remaining time must jump directly to 40 seconds (not 59)
        val sessionAfterDelay = viewModel.mentalMathSession.value
        assertEquals(40, sessionAfterDelay.timeRemainingSeconds)
        assertEquals(20, sessionAfterDelay.elapsedSeconds)
        assertFalse(sessionAfterDelay.isFinished)

        // Simulate massive delay past the deadline (e.g. 50 seconds more, total 70s > 60s)
        testClock.timeMs += 50000L
        viewModel.tickMentalMathTimer()

        val finishedSession = viewModel.mentalMathSession.value
        assertEquals(0, finishedSession.timeRemainingSeconds)
        assertTrue("Remaining time must never become negative", finishedSession.timeRemainingSeconds >= 0)
        assertTrue("Session must be finished on timeout", finishedSession.isFinished)

        // Multiple subsequent ticks must not duplicate completion or make time negative
        viewModel.tickMentalMathTimer()
        viewModel.tickMentalMathTimer()
        assertEquals(0, viewModel.mentalMathSession.value.timeRemainingSeconds)
        assertTrue(viewModel.mentalMathSession.value.isFinished)

        // Starting a new session resets the timer correctly
        viewModel.startMentalMathChallenge(
            category = MentalMathCategory.SUBTRACTION,
            difficulty = MentalMathDifficulty.MEDIUM,
            mode = MentalMathMode.TIMED_SPRINT,
            durationOrCount = 30
        )
        val newSession = viewModel.mentalMathSession.value
        assertEquals(30, newSession.timeRemainingSeconds)
        assertEquals(0, newSession.elapsedSeconds)
        assertFalse(newSession.isFinished)
    }

    @Test
    fun `verify mental math retry, review mode, and setup exit navigation`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.MathViewModel(app)

        viewModel.startMentalMathChallenge(
            category = MentalMathCategory.SQUARES,
            difficulty = MentalMathDifficulty.HARD,
            mode = MentalMathMode.FIXED_QUESTIONS,
            durationOrCount = 10
        )

        // Test review mode toggle
        assertFalse(viewModel.mentalMathSession.value.isReviewMode)
        viewModel.toggleMentalMathReviewMode()
        assertTrue(viewModel.mentalMathSession.value.isReviewMode)
        viewModel.toggleMentalMathReviewMode()
        assertFalse(viewModel.mentalMathSession.value.isReviewMode)

        // Test retry
        viewModel.retryMentalMathChallenge()
        val retriedSession = viewModel.mentalMathSession.value
        assertEquals(0, retriedSession.currentIndex)
        assertFalse(retriedSession.isFinished)
        assertEquals(MentalMathCategory.SQUARES, retriedSession.category)
        assertEquals(MentalMathDifficulty.HARD, retriedSession.difficulty)

        // Test exit to setup
        viewModel.exitMentalMathToSetup()
        assertEquals(com.example.ui.Screen.MentalMath, viewModel.currentScreen)
        assertFalse(viewModel.mentalMathSession.value.isFinished)
    }
}



