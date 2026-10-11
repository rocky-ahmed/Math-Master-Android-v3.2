package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.MathDatabase
import com.example.data.model.BookmarkEntity
import com.example.data.model.ProgressEntity
import com.example.data.model.QuizScoreEntity
import com.example.data.repository.MathRepository
import com.example.math.AnsweredMentalQuestion
import com.example.math.BengaliNumberFormatter
import com.example.math.DefaultMonotonicClock
import com.example.math.MentalMathCategory
import com.example.math.MentalMathDifficulty
import com.example.math.MentalMathGenerator
import com.example.math.MentalMathMode
import com.example.math.MentalMathQuestion
import com.example.math.MentalMathSessionState
import com.example.math.MonotonicClock
import com.example.math.PracticeCategory
import com.example.math.PracticeDifficulty
import com.example.math.PracticeLabGenerator
import com.example.math.PracticeQuestion
import com.example.math.PracticeSessionState
import com.example.math.QuizGenerator
import com.example.math.QuizQuestion
import com.example.math.QuizTopic
import com.example.math.TableRange
import com.example.ui.customization.AppCustomizationState
import com.example.ui.customization.CustomizationPreferences
import com.example.ui.customization.GlobalCustomConfig
import com.example.ui.customization.SectionCustomConfig
import com.example.ui.customization.SectionKey
import com.example.ui.theme.AppThemeMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Tables : Screen("tables")
    object TableDetail : Screen("table_detail")
    object Primes : Screen("primes")
    object SquaresCubes : Screen("squares_cubes")
    object Formulas : Screen("formulas")
    object FormulaDetail : Screen("formula_detail")
    object FormulaLibrary : Screen("formula_library")
    object PracticeLab : Screen("practice_lab")
    object PracticeLabPlay : Screen("practice_lab_play")
    object MentalMath : Screen("mental_math")
    object MentalMathPlay : Screen("mental_math_play")
    object NumberSystem : Screen("number_system")
    object NumberSystemPractice : Screen("number_system_practice")
    object Quizzes : Screen("quizzes")
    object QuizPlay : Screen("quiz_play")
    object Bookmarks : Screen("bookmarks")
    object PracticeTutor : Screen("practice_tutor")
    object Dashboard : Screen("dashboard")
    object Customize : Screen("customize")
    object ProblemSolver : Screen("problem_solver")
    object VisualExplorer : Screen("visual_explorer")
}

data class WeeklyDayStat(
    val dayNameBn: String,
    val dateStr: String,
    val count: Int,
    val isToday: Boolean,
    val isMet: Boolean
)

data class QuizSessionState(
    val topic: QuizTopic = QuizTopic.MULTIPLICATION,
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val correctCount: Int = 0,
    val isFinished: Boolean = false,
    val userAnswers: Map<Int, Int> = emptyMap(),
    val isReviewMode: Boolean = false
)

class MathViewModel @JvmOverloads constructor(
    application: Application,
    var clock: MonotonicClock = DefaultMonotonicClock()
) : AndroidViewModel(application) {
    private val repository: MathRepository
    private val prefs = application.getSharedPreferences("math_master_prefs", android.content.Context.MODE_PRIVATE)

    // Study Streaks & Daily Activity
    private val _streakDays = MutableStateFlow(1)
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _longestStreak = MutableStateFlow(1)
    val longestStreak: StateFlow<Int> = _longestStreak.asStateFlow()

    private val _weeklyActivities = MutableStateFlow<List<WeeklyDayStat>>(emptyList())
    val weeklyActivities: StateFlow<List<WeeklyDayStat>> = _weeklyActivities.asStateFlow()

    // Customization System State
    private val customPrefs = CustomizationPreferences(application)
    private val _customizationState = MutableStateFlow(customPrefs.loadCustomizationState())
    val customizationState: StateFlow<AppCustomizationState> = _customizationState.asStateFlow()

    // Live Preview Customization State (Allows previewing live changes before saving or canceling)
    private val _previewCustomizationState = MutableStateFlow(customPrefs.loadCustomizationState())
    val previewCustomizationState: StateFlow<AppCustomizationState> = _previewCustomizationState.asStateFlow()

    init {
        val db = MathDatabase.getInstance(application)
        repository = MathRepository(db.mathDao())
        initStreakAndActivity()
    }

    // App Navigation Stack (custom backstack for seamless BackHandler support)
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val screenStack: StateFlow<List<Screen>> = _screenStack.asStateFlow()

    val currentScreen: Screen
        get() = _screenStack.value.lastOrNull() ?: Screen.Home

    // Active Selection State
    private val _selectedTableNumber = MutableStateFlow(1)
    val selectedTableNumber: StateFlow<Int> = _selectedTableNumber.asStateFlow()

    private val _selectedFormulaId = MutableStateFlow("sq_1")
    val selectedFormulaId: StateFlow<String> = _selectedFormulaId.asStateFlow()

    private val _selectedTableRange = MutableStateFlow(TableRange.RANGE_1_10)
    val selectedTableRange: StateFlow<TableRange> = _selectedTableRange.asStateFlow()

    // Theme Mode - Loaded from persistent SharedPreferences
    private val initialTheme: AppThemeMode = try {
        val savedName = prefs.getString("selected_theme_mode", AppThemeMode.SYSTEM.name)
        AppThemeMode.valueOf(savedName ?: AppThemeMode.SYSTEM.name)
    } catch (_: Exception) {
        AppThemeMode.SYSTEM
    }

    private val _themeMode = MutableStateFlow(initialTheme)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    // Persistent Database State
    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.bookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizScores: StateFlow<List<QuizScoreEntity>> = repository.quizScores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressList: StateFlow<List<ProgressEntity>> = repository.progressList
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Quiz Session
    private val _quizSession = MutableStateFlow(QuizSessionState())
    val quizSession: StateFlow<QuizSessionState> = _quizSession.asStateFlow()

    // Math Practice Lab State
    private val _practiceSession = MutableStateFlow(PracticeSessionState())
    val practiceSession: StateFlow<PracticeSessionState> = _practiceSession.asStateFlow()

    // Mental Math Challenge State
    private val _mentalMathSession = MutableStateFlow(MentalMathSessionState())
    val mentalMathSession: StateFlow<MentalMathSessionState> = _mentalMathSession.asStateFlow()

    // Step-by-Step Interactive Practice Tutor State
    private val _tutorNumber = MutableStateFlow(7)
    val tutorNumber: StateFlow<Int> = _tutorNumber.asStateFlow()
    private val _tutorStep = MutableStateFlow(1) // 1 to 10
    val tutorStep: StateFlow<Int> = _tutorStep.asStateFlow()
    private val _tutorUserAnswer = MutableStateFlow("")
    val tutorUserAnswer: StateFlow<String> = _tutorUserAnswer.asStateFlow()
    private val _tutorFeedback = MutableStateFlow<String?>(null)
    val tutorFeedback: StateFlow<String?> = _tutorFeedback.asStateFlow()


    fun navigateTo(screen: Screen) {
        val current = _screenStack.value
        if (current.lastOrNull() != screen) {
            _screenStack.value = current + screen
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        if (current.size > 1) {
            _screenStack.value = current.dropLast(1)
            return true
        } else if (current.firstOrNull() != Screen.Home) {
            _screenStack.value = listOf(Screen.Home)
            return true
        }
        return false
    }

    fun navigateToHome() {
        _screenStack.value = listOf(Screen.Home)
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("selected_theme_mode", mode.name).apply()
    }

    fun selectRange(range: TableRange) {
        _selectedTableRange.value = range
    }

    fun openTable(number: Int) {
        _selectedTableNumber.value = number
        navigateTo(Screen.TableDetail)
    }

    fun openFormula(formulaId: String) {
        _selectedFormulaId.value = formulaId
        navigateTo(Screen.FormulaDetail)
    }

    fun isBookmarked(id: String): Boolean {
        return bookmarks.value.any { it.id == id }
    }

    fun toggleBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            val currently = isBookmarked(bookmark.id)
            repository.toggleBookmark(bookmark, currently)
        }
    }

    fun removeBookmark(id: String) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    fun isLearned(itemId: String): Boolean {
        return progressList.value.find { it.itemId == itemId }?.isLearned ?: false
    }

    fun toggleLearned(itemId: String) {
        viewModelScope.launch {
            val currently = isLearned(itemId)
            repository.toggleLearned(itemId, currently)
            if (!currently) {
                recordDailyActivity(2)
            }
        }
    }

    // Quiz Operations
    fun startQuiz(topic: QuizTopic, count: Int = 10) {
        val questions = QuizGenerator.generateQuiz(topic, count)
        _quizSession.value = QuizSessionState(
            topic = topic,
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            correctCount = 0,
            isFinished = false,
            userAnswers = emptyMap(),
            isReviewMode = false
        )
        navigateTo(Screen.QuizPlay)
    }

    fun selectQuizOption(index: Int) {
        val current = _quizSession.value
        if (!current.isAnswerSubmitted && !current.isFinished) {
            _quizSession.value = current.copy(selectedOptionIndex = index)
        }
    }

    fun submitQuizAnswer() {
        val current = _quizSession.value
        val selectedOpt = current.selectedOptionIndex
        if (selectedOpt == null || current.isAnswerSubmitted || current.isFinished) return

        val currentQ = current.questions.getOrNull(current.currentIndex) ?: return
        val isCorrect = selectedOpt == currentQ.correctIndex
        val updatedCorrectCount = if (isCorrect) current.correctCount + 1 else current.correctCount
        val updatedAnswers = current.userAnswers + (current.currentIndex to selectedOpt)

        _quizSession.value = current.copy(
            isAnswerSubmitted = true,
            correctCount = updatedCorrectCount,
            userAnswers = updatedAnswers
        )
        if (isCorrect) {
            recordDailyActivity(1)
        }
    }

    fun nextQuizQuestion() {
        val current = _quizSession.value
        if (!current.isAnswerSubmitted) return

        val nextIndex = current.currentIndex + 1
        if (nextIndex < current.questions.size) {
            _quizSession.value = current.copy(
                currentIndex = nextIndex,
                selectedOptionIndex = null,
                isAnswerSubmitted = false
            )
        } else {
            // Quiz completed
            val total = current.questions.size
            val percentage = if (total > 0) (current.correctCount * 100) / total else 0
            _quizSession.value = current.copy(isFinished = true)

            viewModelScope.launch {
                repository.recordQuizScore(
                    QuizScoreEntity(
                        topicId = current.topic.id,
                        topicTitleBn = current.topic.titleBn,
                        score = current.correctCount,
                        totalQuestions = total,
                        percentage = percentage
                    )
                )
                recordDailyActivity(4)
            }
        }
    }

    fun exitQuizToTopicList() {
        val current = _screenStack.value
        val quizzesIndex = current.indexOfLast { it == Screen.Quizzes }
        if (quizzesIndex != -1) {
            _screenStack.value = current.take(quizzesIndex + 1)
        } else {
            _screenStack.value = listOf(Screen.Home, Screen.Quizzes)
        }
        _quizSession.value = QuizSessionState()
    }

    fun startNextQuizTopic() {
        val currentTopic = _quizSession.value.topic
        val allTopics = QuizTopic.values()
        val currentIndex = allTopics.indexOf(currentTopic)
        val nextTopic = if (currentIndex != -1 && currentIndex + 1 < allTopics.size) {
            allTopics[currentIndex + 1]
        } else {
            allTopics.first()
        }
        startQuiz(nextTopic, 10)
    }

    fun toggleQuizReviewMode() {
        val current = _quizSession.value
        _quizSession.value = current.copy(isReviewMode = !current.isReviewMode)
    }

    // ==========================================
    // MATH PRACTICE LAB OPERATIONS
    // ==========================================
    fun startPracticeSession(category: PracticeCategory, difficulty: PracticeDifficulty, count: Int = 10) {
        val questions = PracticeLabGenerator.generateSession(category, difficulty, count)
        _practiceSession.value = PracticeSessionState(
            category = category,
            difficulty = difficulty,
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            customInputText = "",
            isAnswerSubmitted = false,
            isCorrect = false,
            correctCount = 0,
            wrongCount = 0,
            startTimeMs = System.currentTimeMillis(),
            totalTimeSeconds = 0L,
            isFinished = false,
            userAnswers = emptyMap(),
            isReviewMode = false
        )
        navigateTo(Screen.PracticeLabPlay)
    }

    fun selectPracticeOption(index: Int) {
        val current = _practiceSession.value
        if (!current.isAnswerSubmitted && !current.isFinished) {
            _practiceSession.value = current.copy(selectedOptionIndex = index)
        }
    }

    fun setPracticeCustomInput(text: String) {
        val current = _practiceSession.value
        if (!current.isAnswerSubmitted && !current.isFinished) {
            _practiceSession.value = current.copy(customInputText = text)
        }
    }

    fun submitPracticeAnswer() {
        val current = _practiceSession.value
        if (current.isAnswerSubmitted || current.isFinished) return
        val currentQ = current.questions.getOrNull(current.currentIndex) ?: return

        val selectedOpt = current.selectedOptionIndex
        val isCorrect: Boolean
        val effectiveSelectedOpt: Int

        if (selectedOpt != null) {
            effectiveSelectedOpt = selectedOpt
            isCorrect = selectedOpt == currentQ.correctIndex
        } else if (current.customInputText.trim().isNotEmpty()) {
            val userText = current.customInputText.trim()
            val correctAns = currentQ.correctAnswerBn
            val matchedIndex = currentQ.optionsBn.indexOfFirst {
                it.equals(userText, ignoreCase = true) || BengaliNumberFormatter.toBn(it) == userText
            }
            effectiveSelectedOpt = if (matchedIndex != -1) matchedIndex else -1
            isCorrect = userText == correctAns ||
                    userText == currentQ.optionsBn.getOrNull(currentQ.correctIndex) ||
                    BengaliNumberFormatter.toBn(userText) == correctAns
        } else {
            return
        }

        val updatedCorrect = if (isCorrect) current.correctCount + 1 else current.correctCount
        val updatedWrong = if (isCorrect) current.wrongCount else current.wrongCount + 1
        val updatedAnswers = current.userAnswers + (current.currentIndex to effectiveSelectedOpt)

        _practiceSession.value = current.copy(
            isAnswerSubmitted = true,
            isCorrect = isCorrect,
            selectedOptionIndex = if (effectiveSelectedOpt >= 0) effectiveSelectedOpt else null,
            correctCount = updatedCorrect,
            wrongCount = updatedWrong,
            userAnswers = updatedAnswers
        )

        if (isCorrect) {
            recordDailyActivity(1)
        }
    }

    fun nextPracticeQuestion() {
        val current = _practiceSession.value
        if (!current.isAnswerSubmitted) return

        val nextIndex = current.currentIndex + 1
        if (nextIndex < current.questions.size) {
            _practiceSession.value = current.copy(
                currentIndex = nextIndex,
                selectedOptionIndex = null,
                customInputText = "",
                isAnswerSubmitted = false,
                isCorrect = false
            )
        } else {
            val elapsedSec = ((System.currentTimeMillis() - current.startTimeMs) / 1000).coerceAtLeast(1)
            val total = current.questions.size
            val percentage = if (total > 0) (current.correctCount * 100) / total else 0

            _practiceSession.value = current.copy(
                isFinished = true,
                totalTimeSeconds = elapsedSec
            )

            viewModelScope.launch {
                val topicId = "PRACTICE_${current.category.id}_${current.difficulty.id}"
                val titleBn = "প্র্যাকটিস: ${current.category.titleBn} (${current.difficulty.titleBn})"
                repository.recordQuizScore(
                    QuizScoreEntity(
                        topicId = topicId,
                        topicTitleBn = titleBn,
                        score = current.correctCount,
                        totalQuestions = total,
                        percentage = percentage
                    )
                )
                repository.recordPractice("practice_${current.category.id}")
                recordDailyActivity(4)
            }
        }
    }

    fun retryPracticeSession() {
        val current = _practiceSession.value
        startPracticeSession(current.category, current.difficulty, current.questions.size.coerceAtLeast(5))
    }

    fun togglePracticeReviewMode() {
        val current = _practiceSession.value
        _practiceSession.value = current.copy(isReviewMode = !current.isReviewMode)
    }

    fun exitPracticeToCategories() {
        val current = _screenStack.value
        val practiceIndex = current.indexOfLast { it == Screen.PracticeLab }
        if (practiceIndex != -1) {
            _screenStack.value = current.take(practiceIndex + 1)
        } else {
            _screenStack.value = listOf(Screen.Home, Screen.PracticeLab)
        }
        _practiceSession.value = PracticeSessionState()
    }

    // --- Mental Math Challenge Engine ---
    fun startMentalMathChallenge(
        category: MentalMathCategory,
        difficulty: MentalMathDifficulty,
        mode: MentalMathMode,
        durationOrCount: Int
    ) {
        val countToGenerate = if (mode == MentalMathMode.FIXED_QUESTIONS) durationOrCount else 35
        val initialQuestions = MentalMathGenerator.generateQuestions(category, difficulty, countToGenerate)
        val initialDuration = if (mode == MentalMathMode.TIMED_SPRINT) durationOrCount else 0
        val now = clock.elapsedRealtime()
        val deadline = if (mode == MentalMathMode.TIMED_SPRINT) now + initialDuration * 1000L else 0L

        _mentalMathSession.value = MentalMathSessionState(
            category = category,
            difficulty = difficulty,
            mode = mode,
            configuredDurationSeconds = if (mode == MentalMathMode.TIMED_SPRINT) durationOrCount else 0,
            configuredQuestionCount = if (mode == MentalMathMode.FIXED_QUESTIONS) durationOrCount else 0,
            questions = initialQuestions,
            currentIndex = 0,
            score = 0,
            streak = 0,
            highestStreak = 0,
            correctCount = 0,
            wrongCount = 0,
            timeRemainingSeconds = initialDuration,
            elapsedSeconds = 0,
            isAnswerSubmitted = false,
            selectedOptionIndex = null,
            lastAnswerCorrect = null,
            feedbackMessageBn = null,
            isFinished = false,
            history = emptyList(),
            isReviewMode = false,
            startElapsedRealtimeMs = now,
            deadlineElapsedRealtimeMs = deadline
        )
        navigateTo(Screen.MentalMathPlay)
    }

    fun submitMentalMathAnswer(optionValue: Int) {
        val current = _mentalMathSession.value
        if (current.isAnswerSubmitted || current.isFinished) return
        val currentQ = current.questions.getOrNull(current.currentIndex) ?: return

        val isCorrect = optionValue == currentQ.correctAnswer
        val optionIndex = currentQ.options.indexOf(optionValue)

        val newStreak = if (isCorrect) current.streak + 1 else 0
        val newHighestStreak = maxOf(current.highestStreak, newStreak)

        // Combo bonus: 1x, 1.5x (2+ streak), 2x (4+ streak), 2.5x (6+ streak)
        val comboMultiplier = when {
            newStreak >= 6 -> 2.5f
            newStreak >= 4 -> 2.0f
            newStreak >= 2 -> 1.5f
            else -> 1.0f
        }
        val pointsEarned = if (isCorrect) (current.difficulty.basePoints * comboMultiplier).toInt() else 0

        val feedbackMsg = if (isCorrect) {
            if (newStreak >= 3) "চমৎকার! ${BengaliNumberFormatter.toBn(newStreak)}টি টানা সঠিক (+${BengaliNumberFormatter.toBn(pointsEarned)})"
            else "সঠিক উত্তর! (+${BengaliNumberFormatter.toBn(pointsEarned)})"
        } else {
            "ভুল উত্তর! সঠিক: ${currentQ.correctAnswerBn}"
        }

        val answeredRecord = AnsweredMentalQuestion(
            question = currentQ,
            chosenOption = optionValue,
            isCorrect = isCorrect,
            timeTakenSeconds = 1
        )

        _mentalMathSession.value = current.copy(
            isAnswerSubmitted = true,
            selectedOptionIndex = optionIndex,
            lastAnswerCorrect = isCorrect,
            score = current.score + pointsEarned,
            streak = newStreak,
            highestStreak = newHighestStreak,
            correctCount = if (isCorrect) current.correctCount + 1 else current.correctCount,
            wrongCount = if (isCorrect) current.wrongCount else current.wrongCount + 1,
            feedbackMessageBn = feedbackMsg,
            history = current.history + answeredRecord
        )

        if (isCorrect) {
            recordDailyActivity(1)
        }
    }

    fun nextMentalMathQuestion() {
        val current = _mentalMathSession.value
        if (!current.isAnswerSubmitted || current.isFinished) return

        val nextIndex = current.currentIndex + 1

        // Check if finished in FIXED_QUESTIONS mode
        if (current.mode == MentalMathMode.FIXED_QUESTIONS && nextIndex >= current.configuredQuestionCount) {
            finishMentalMathSession()
            return
        }

        // In TIMED_SPRINT mode, if questions are running low, generate more
        val currentQuestions = current.questions
        val updatedQuestions = if (nextIndex >= currentQuestions.size - 3) {
            currentQuestions + MentalMathGenerator.generateQuestions(current.category, current.difficulty, 20)
        } else {
            currentQuestions
        }

        _mentalMathSession.value = current.copy(
            questions = updatedQuestions,
            currentIndex = nextIndex,
            selectedOptionIndex = null,
            lastAnswerCorrect = null,
            feedbackMessageBn = null,
            isAnswerSubmitted = false
        )
    }

    fun tickMentalMathTimer() {
        val current = _mentalMathSession.value
        if (current.isFinished) return

        val now = clock.elapsedRealtime()
        val startMs = current.startElapsedRealtimeMs
        val actualElapsed = if (startMs > 0L) {
            ((now - startMs) / 1000L).toInt().coerceAtLeast(0)
        } else {
            current.elapsedSeconds + 1
        }

        if (current.mode == MentalMathMode.TIMED_SPRINT) {
            val deadlineMs = current.deadlineElapsedRealtimeMs
            val remainingSec = if (deadlineMs > 0L) {
                val remMs = deadlineMs - now
                if (remMs <= 0L) 0 else ((remMs + 999L) / 1000L).toInt()
            } else {
                (current.timeRemainingSeconds - 1).coerceAtLeast(0)
            }

            if (remainingSec <= 0) {
                finishMentalMathSession()
            } else {
                if (remainingSec != current.timeRemainingSeconds || actualElapsed != current.elapsedSeconds) {
                    _mentalMathSession.value = current.copy(
                        timeRemainingSeconds = remainingSec,
                        elapsedSeconds = actualElapsed
                    )
                }
            }
        } else {
            if (actualElapsed != current.elapsedSeconds) {
                _mentalMathSession.value = current.copy(elapsedSeconds = actualElapsed)
            }
        }
    }

    fun finishMentalMathSession() {
        val current = _mentalMathSession.value
        if (current.isFinished) return

        val totalAttempted = current.correctCount + current.wrongCount
        val percentage = if (totalAttempted > 0) (current.correctCount * 100) / totalAttempted else 0

        _mentalMathSession.value = current.copy(
            isFinished = true,
            isAnswerSubmitted = true,
            timeRemainingSeconds = 0,
            elapsedSeconds = if (current.mode == MentalMathMode.TIMED_SPRINT) {
                maxOf(current.configuredDurationSeconds, current.elapsedSeconds)
            } else {
                current.elapsedSeconds
            }
        )

        viewModelScope.launch {
            val topicId = "MENTAL_MATH_${current.category.id}_${current.difficulty.id}"
            val titleBn = "মেন্টাল ম্যাথ: ${current.category.titleBn} (${current.difficulty.titleBn})"
            repository.recordQuizScore(
                QuizScoreEntity(
                    topicId = topicId,
                    topicTitleBn = titleBn,
                    score = current.score,
                    totalQuestions = totalAttempted,
                    percentage = percentage
                )
            )
            repository.recordPractice("mental_math_${current.category.id}")
            recordDailyActivity(4)
        }
    }

    fun retryMentalMathChallenge() {
        val current = _mentalMathSession.value
        val durationOrCount = if (current.mode == MentalMathMode.TIMED_SPRINT) {
            current.configuredDurationSeconds
        } else {
            current.configuredQuestionCount
        }
        startMentalMathChallenge(
            category = current.category,
            difficulty = current.difficulty,
            mode = current.mode,
            durationOrCount = durationOrCount
        )
    }

    fun toggleMentalMathReviewMode() {
        val current = _mentalMathSession.value
        _mentalMathSession.value = current.copy(isReviewMode = !current.isReviewMode)
    }

    fun exitMentalMathToSetup() {
        val current = _screenStack.value
        val setupIndex = current.indexOfLast { it == Screen.MentalMath }
        if (setupIndex != -1) {
            _screenStack.value = current.take(setupIndex + 1)
        } else {
            _screenStack.value = listOf(Screen.Home, Screen.MentalMath)
        }
        _mentalMathSession.value = MentalMathSessionState()
    }

    // Step-by-Step Practice Tutor
    fun startTutor(number: Int = 5) {
        _tutorNumber.value = number
        _tutorStep.value = 1
        _tutorUserAnswer.value = ""
        _tutorFeedback.value = null
        navigateTo(Screen.PracticeTutor)
    }

    fun setTutorNumber(number: Int) {
        _tutorNumber.value = number
        _tutorStep.value = 1
        _tutorUserAnswer.value = ""
        _tutorFeedback.value = null
    }

    fun setTutorAnswerInput(input: String) {
        // filter only digits
        _tutorUserAnswer.value = input.filter { it.isDigit() }
    }

    fun checkTutorStep() {
        val num = _tutorNumber.value
        val step = _tutorStep.value
        val expected = num * step
        val userAnsInt = _tutorUserAnswer.value.toIntOrNull()

        if (userAnsInt == null) {
            _tutorFeedback.value = "অনুগ্রহ করে উত্তর লিখুন"
            return
        }

        if (userAnsInt == expected) {
            recordDailyActivity(1)
            if (step < 10) {
                _tutorFeedback.value = "সঠিক উত্তর! পরবর্তী ধাপে যাচ্ছি..."
                _tutorStep.value = step + 1
                _tutorUserAnswer.value = ""
            } else {
                _tutorFeedback.value = "অভিনন্দন! আপনি সফলভাবে ${num} এর নামতা সম্পন্ন করেছেন!"
                viewModelScope.launch {
                    repository.recordPractice("table_${num}")
                    repository.toggleLearned("table_${num}", currentlyLearned = false)
                    recordDailyActivity(3)
                }
            }
        } else {
            _tutorFeedback.value = "ভুল হয়েছে! সঠিক উত্তর: $num × $step = $expected"
        }
    }


    // --- Streak & Daily Activity Helpers ---
    private fun getTodayDateStr(): String {
        val cal = Calendar.getInstance()
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
    }

    private fun getYesterdayDateStr(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
    }

    private fun getDayOfWeekBn(calendar: Calendar): String {
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SATURDAY -> "শনি"
            Calendar.SUNDAY -> "রবি"
            Calendar.MONDAY -> "সোম"
            Calendar.TUESDAY -> "মঙ্গল"
            Calendar.WEDNESDAY -> "বুধ"
            Calendar.THURSDAY -> "বৃহঃ"
            Calendar.FRIDAY -> "শুক্র"
            else -> ""
        }
    }

    private fun initStreakAndActivity() {
        val today = getTodayDateStr()
        val yesterday = getYesterdayDateStr()
        val lastDate = prefs.getString("streak_last_active_date", null)
        var currentStreak = prefs.getInt("streak_current_days", 0)
        var longestStreak = prefs.getInt("streak_longest_days", 0)

        if (lastDate == null) {
            // First time initialization
            currentStreak = 1
            longestStreak = 1
            prefs.edit()
                .putString("streak_last_active_date", today)
                .putInt("streak_current_days", 1)
                .putInt("streak_longest_days", 1)
                .putInt("act_$today", maxOf(prefs.getInt("act_$today", 0), 1))
                .apply()
        } else if (lastDate == yesterday) {
            if (currentStreak < 1) currentStreak = 1
        } else if (lastDate != today) {
            // Missed more than a day
            currentStreak = 1
            prefs.edit().putInt("streak_current_days", 1).apply()
        }

        if (longestStreak < currentStreak) {
            longestStreak = currentStreak
            prefs.edit().putInt("streak_longest_days", longestStreak).apply()
        }

        _streakDays.value = currentStreak
        _longestStreak.value = longestStreak
        refreshWeeklyStats()
    }

    fun recordDailyActivity(count: Int = 1) {
        val today = getTodayDateStr()
        val yesterday = getYesterdayDateStr()
        val currentTodayCount = prefs.getInt("act_$today", 0)
        prefs.edit().putInt("act_$today", currentTodayCount + count).apply()

        val lastDate = prefs.getString("streak_last_active_date", null)
        var currentStreak = prefs.getInt("streak_current_days", 1)
        var longestStreak = prefs.getInt("streak_longest_days", 1)

        if (lastDate == yesterday) {
            currentStreak += 1
        } else if (lastDate != null && lastDate != today) {
            currentStreak = 1
        }

        if (currentStreak > longestStreak) {
            longestStreak = currentStreak
        }

        prefs.edit()
            .putString("streak_last_active_date", today)
            .putInt("streak_current_days", currentStreak)
            .putInt("streak_longest_days", longestStreak)
            .apply()

        _streakDays.value = currentStreak
        _longestStreak.value = longestStreak
        refreshWeeklyStats()
    }

    fun refreshWeeklyStats() {
        val list = mutableListOf<WeeklyDayStat>()
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayStr = sdf.format(cal.time)

        for (i in 6 downTo 0) {
            val dayCal = Calendar.getInstance()
            dayCal.add(Calendar.DAY_OF_YEAR, -i)
            val dateStr = sdf.format(dayCal.time)
            val dayName = getDayOfWeekBn(dayCal)
            val count = prefs.getInt("act_$dateStr", 0)
            list.add(
                WeeklyDayStat(
                    dayNameBn = dayName,
                    dateStr = dateStr,
                    count = count,
                    isToday = dateStr == todayStr,
                    isMet = count >= 2
                )
            )
        }
        _weeklyActivities.value = list
    }

    // --- Customization Management ---

    fun updatePreviewGlobal(updater: (GlobalCustomConfig) -> GlobalCustomConfig) {
        val current = _previewCustomizationState.value
        _previewCustomizationState.value = current.copy(global = updater(current.global))
    }

    fun updatePreviewSection(key: SectionKey, updater: (SectionCustomConfig) -> SectionCustomConfig) {
        val current = _previewCustomizationState.value
        val sectionCurrent = current.sections[key.id] ?: SectionCustomConfig()
        val updated = updater(sectionCurrent)
        val newMap = current.sections.toMutableMap()
        newMap[key.id] = updated
        _previewCustomizationState.value = current.copy(sections = newMap)
    }

    fun resetPreviewSection(key: SectionKey) {
        val current = _previewCustomizationState.value
        val newMap = current.sections.toMutableMap()
        newMap.remove(key.id)
        _previewCustomizationState.value = current.copy(sections = newMap)
    }

    fun resetPreviewCategory(category: String) {
        val current = _previewCustomizationState.value
        when (category) {
            "LAYOUT" -> {
                _previewCustomizationState.value = current.copy(
                    global = current.global.copy(
                        layoutType = com.example.ui.customization.CustomLayoutType.COMFORTABLE_LIST,
                        gridColumns = 2,
                        horizontalGapDp = 12,
                        verticalGapDp = 12,
                        cornerRadiusDp = 16,
                        showBorder = false,
                        borderThicknessDp = 0,
                        contentAlignmentCenter = false
                    )
                )
            }
            "TYPOGRAPHY" -> {
                _previewCustomizationState.value = current.copy(
                    global = current.global.copy(
                        fontWeight = com.example.ui.customization.CustomFontWeight.MEDIUM,
                        titleFontSizeSp = 16,
                        subtitleFontSizeSp = 12,
                        interfaceScalePercent = 100
                    )
                )
            }
            "GLOBAL_THEME" -> {
                _previewCustomizationState.value = current.copy(
                    global = current.global.copy(
                        customAccentHex = null,
                        customPrimaryActionHex = null,
                        customLightBackgroundHex = null,
                        customLightCardSurfaceHex = null,
                        customLightTextColorHex = null,
                        customDarkBackgroundHex = null,
                        customDarkCardSurfaceHex = null,
                        customDarkTextColorHex = null
                    )
                )
            }
            "SECTIONS" -> {
                _previewCustomizationState.value = current.copy(sections = emptyMap())
            }
        }
    }

    fun saveCustomization() {
        val toSave = _previewCustomizationState.value
        customPrefs.saveCustomizationState(toSave)
        _customizationState.value = toSave
    }

    fun discardCustomizationPreview() {
        _previewCustomizationState.value = _customizationState.value
    }

    fun resetAllCustomizations() {
        customPrefs.clearAll()
        val clean = AppCustomizationState()
        _customizationState.value = clean
        _previewCustomizationState.value = clean
    }

    // --- Phase 3: Number System & Divisibility Practice State ---
    private val _numberSystemPracticeState = MutableStateFlow(com.example.math.NumberSystemPracticeState())
    val numberSystemPracticeState: StateFlow<com.example.math.NumberSystemPracticeState> = _numberSystemPracticeState.asStateFlow()

    fun startNumberSystemPractice(
        topic: com.example.math.NumberSystemTopic = com.example.math.NumberSystemTopic.ALL,
        difficulty: com.example.math.NumberSystemDifficulty = com.example.math.NumberSystemDifficulty.EASY
    ) {
        val questions = com.example.math.NumberSystemProvider.getPracticeQuestions(topic, difficulty)
        _numberSystemPracticeState.value = com.example.math.NumberSystemPracticeState(
            topic = topic,
            difficulty = difficulty,
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            isSubmitted = false,
            score = 0,
            isCompleted = false,
            userAnswers = emptyMap()
        )
        navigateTo(Screen.NumberSystemPractice)
    }

    fun selectNumberSystemOption(index: Int) {
        val current = _numberSystemPracticeState.value
        if (!current.isSubmitted && !current.isCompleted) {
            _numberSystemPracticeState.value = current.copy(selectedOptionIndex = index)
        }
    }

    fun submitNumberSystemAnswer() {
        val current = _numberSystemPracticeState.value
        val selected = current.selectedOptionIndex ?: return
        if (current.isSubmitted || current.isCompleted) return

        val currentQ = current.questions.getOrNull(current.currentIndex) ?: return
        val isCorrect = selected == currentQ.correctIndex
        val newScore = if (isCorrect) current.score + 1 else current.score
        val updatedAnswers = current.userAnswers + (current.currentIndex to selected)

        _numberSystemPracticeState.value = current.copy(
            isSubmitted = true,
            score = newScore,
            userAnswers = updatedAnswers
        )

        // Award daily activity points if correct
        if (isCorrect) {
            recordDailyActivity(1)
        }
    }

    fun nextNumberSystemQuestion() {
        val current = _numberSystemPracticeState.value
        if (current.currentIndex + 1 < current.questions.size) {
            _numberSystemPracticeState.value = current.copy(
                currentIndex = current.currentIndex + 1,
                selectedOptionIndex = null,
                isSubmitted = false
            )
        } else {
            // Completed session: save quiz score to Room DB
            val total = current.questions.size
            val percentage = if (total > 0) (current.score * 100) / total else 0
            val topicTitle = "বাস্তব সংখ্যা ও বিভাজ্যতা (${current.difficulty.titleBn})"

            viewModelScope.launch {
                repository.recordQuizScore(
                    QuizScoreEntity(
                        topicId = "NUMBER_SYSTEM",
                        topicTitleBn = topicTitle,
                        score = current.score,
                        totalQuestions = total,
                        percentage = percentage
                    )
                )
                recordDailyActivity(3)
            }

            _numberSystemPracticeState.value = current.copy(
                isCompleted = true
            )
        }
    }

    fun restartNumberSystemPractice() {
        val current = _numberSystemPracticeState.value
        startNumberSystemPractice(current.topic, current.difficulty)
    }
}

