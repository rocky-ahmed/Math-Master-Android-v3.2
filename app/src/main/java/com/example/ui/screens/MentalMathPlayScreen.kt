package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.BengaliNumberFormatter
import com.example.math.MentalMathMode
import com.example.math.MentalMathQuestion
import com.example.math.MentalMathSessionState
import com.example.ui.MathViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentalMathPlayScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.mentalMathSession.collectAsState()

    BackHandler {
        viewModel.exitMentalMathToSetup()
    }

    // Active timer loop
    LaunchedEffect(session.isFinished) {
        if (!session.isFinished) {
            viewModel.tickMentalMathTimer()
            while (!session.isFinished) {
                delay(500)
                viewModel.tickMentalMathTimer()
            }
        }
    }

    if (session.isFinished) {
        MentalMathSummaryView(
            session = session,
            onRetry = { viewModel.retryMentalMathChallenge() },
            onSelectAnother = { viewModel.exitMentalMathToSetup() },
            onToggleReview = { viewModel.toggleMentalMathReviewMode() },
            modifier = modifier
        )
    } else {
        val currentQ = session.questions.getOrNull(session.currentIndex)

        Column(
            modifier = modifier
                .fillMaxSize()
                .testTag("screen_mental_math_play")
        ) {
            // Header Top Bar
            MentalMathPlayHeader(
                session = session,
                onExit = { viewModel.exitMentalMathToSetup() }
            )

            // Timer & Progress Indicator
            MentalMathProgressBar(session = session)

            if (currentQ != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Question Math Expression Card
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("mm_question_card"),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp, horizontal = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    color = session.category.primaryColor.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "${session.category.titleBn} • ${session.difficulty.titleBn}",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = session.category.primaryColor,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = currentQ.expressionBn,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 38.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "দ্রুত হিসাব করে নিচের সঠিক অপশনে ট্যাপ করুন",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // Instant Feedback Banner
                    if (session.isAnswerSubmitted && session.feedbackMessageBn != null) {
                        item {
                            val isCorrect = session.lastAnswerCorrect == true
                            val bannerBg = if (isCorrect) Color(0xFFD1FAE5) else Color(0xFFFFE4E6)
                            val bannerText = if (isCorrect) Color(0xFF065F46) else Color(0xFF9F1239)

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = bannerBg,
                                modifier = Modifier.fillMaxWidth().testTag("mm_feedback_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = bannerText,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = session.feedbackMessageBn ?: "",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = bannerText
                                        )
                                        if (!isCorrect) {
                                            Text(
                                                text = currentQ.explanationBn,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = bannerText.copy(alpha = 0.9f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 4 Options (2x2 Grid)
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Row 1
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val opt0 = currentQ.options.getOrNull(0)
                                if (opt0 != null) {
                                    MentalMathOptionButton(
                                        optionValue = opt0,
                                        optionIndex = 0,
                                        currentQuestion = currentQ,
                                        session = session,
                                        onClick = { viewModel.submitMentalMathAnswer(opt0) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                val opt1 = currentQ.options.getOrNull(1)
                                if (opt1 != null) {
                                    MentalMathOptionButton(
                                        optionValue = opt1,
                                        optionIndex = 1,
                                        currentQuestion = currentQ,
                                        session = session,
                                        onClick = { viewModel.submitMentalMathAnswer(opt1) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            // Row 2
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val opt2 = currentQ.options.getOrNull(2)
                                if (opt2 != null) {
                                    MentalMathOptionButton(
                                        optionValue = opt2,
                                        optionIndex = 2,
                                        currentQuestion = currentQ,
                                        session = session,
                                        onClick = { viewModel.submitMentalMathAnswer(opt2) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                val opt3 = currentQ.options.getOrNull(3)
                                if (opt3 != null) {
                                    MentalMathOptionButton(
                                        optionValue = opt3,
                                        optionIndex = 3,
                                        currentQuestion = currentQ,
                                        session = session,
                                        onClick = { viewModel.submitMentalMathAnswer(opt3) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // Action Button (Next Question)
                    if (session.isAnswerSubmitted) {
                        item {
                            Button(
                                onClick = { viewModel.nextMentalMathQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("next_mental_math_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = session.category.primaryColor
                                )
                            ) {
                                Text(
                                    text = "পরবর্তী প্রশ্ন",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MentalMathOptionButton(
    optionValue: Int,
    optionIndex: Int,
    currentQuestion: MentalMathQuestion,
    session: MentalMathSessionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSubmitted = session.isAnswerSubmitted
    val isCorrectOption = optionValue == currentQuestion.correctAnswer
    val isSelected = session.selectedOptionIndex == optionIndex

    val targetContainer = when {
        !isSubmitted -> MaterialTheme.colorScheme.surface
        isCorrectOption -> Color(0xFF10B981) // Green
        isSelected -> Color(0xFFEF4444) // Red
        else -> MaterialTheme.colorScheme.surface
    }

    val animatedColor by animateColorAsState(targetValue = targetContainer, label = "opt_bg")

    val textColor = when {
        !isSubmitted -> MaterialTheme.colorScheme.onSurface
        isCorrectOption -> Color.White
        isSelected -> Color.White
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    }

    ElevatedCard(
        onClick = onClick,
        enabled = !isSubmitted,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = animatedColor,
            disabledContainerColor = animatedColor
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (isSubmitted) 2.dp else 4.dp
        ),
        modifier = modifier
            .height(76.dp)
            .testTag("option_btn_$optionIndex")
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = BengaliNumberFormatter.toBn(optionValue),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                ),
                color = textColor
            )
        }
    }
}

@Composable
fun MentalMathPlayHeader(
    session: MentalMathSessionState,
    onExit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onExit,
            modifier = Modifier.testTag("exit_mental_math_btn")
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "প্রস্থান")
        }

        // Streak combo badge
        if (session.streak >= 2) {
            Surface(
                color = Color(0xFFEA580C),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("streak_combo_badge")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${BengaliNumberFormatter.toBn(session.streak)} কম্বো!",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.width(4.dp))
        }

        // Score Chip
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${BengaliNumberFormatter.toBn(session.score)} pts",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun MentalMathProgressBar(session: MentalMathSessionState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (session.mode == MentalMathMode.TIMED_SPRINT) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (session.timeRemainingSeconds <= 10) Color(0xFFEF4444) else MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "অবশিষ্ট সময়: ${BengaliNumberFormatter.toBn(session.timeRemainingSeconds)} সে.",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = if (session.timeRemainingSeconds <= 10) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "সঠিক: ${BengaliNumberFormatter.toBn(session.correctCount)} | ভুল: ${BengaliNumberFormatter.toBn(session.wrongCount)}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "প্রশ্ন ${BengaliNumberFormatter.toBn(session.currentIndex + 1)} / ${BengaliNumberFormatter.toBn(session.configuredQuestionCount)}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "সঠিক: ${BengaliNumberFormatter.toBn(session.correctCount)} | ভুল: ${BengaliNumberFormatter.toBn(session.wrongCount)}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        val progress = if (session.mode == MentalMathMode.TIMED_SPRINT) {
            if (session.configuredDurationSeconds > 0) {
                session.timeRemainingSeconds.toFloat() / session.configuredDurationSeconds.toFloat()
            } else 1f
        } else {
            if (session.configuredQuestionCount > 0) {
                (session.currentIndex + 1).toFloat() / session.configuredQuestionCount.toFloat()
            } else 1f
        }

        val indicatorColor = if (session.mode == MentalMathMode.TIMED_SPRINT && session.timeRemainingSeconds <= 10) {
            Color(0xFFEF4444)
        } else {
            session.category.primaryColor
        }

        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            color = indicatorColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
    }
}

@Composable
fun MentalMathSummaryView(
    session: MentalMathSessionState,
    onRetry: () -> Unit,
    onSelectAnother: () -> Unit,
    onToggleReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalAttempted = session.correctCount + session.wrongCount
    val accuracy = if (totalAttempted > 0) (session.correctCount * 100) / totalAttempted else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("mm_summary_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Celebratory Card
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFBBF24)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFF78350F),
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "চ্যালেঞ্জ সম্পন্ন!",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "${session.category.titleBn} (${session.difficulty.titleBn}) • ${session.mode.titleBn}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Final Score Display
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "মোট স্কোর",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${BengaliNumberFormatter.toBn(session.score)} পয়েন্ট",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = session.category.primaryColor,
                                    fontSize = 34.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "সঠিক উত্তর",
                    value = BengaliNumberFormatter.toBn(session.correctCount),
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "ভুল উত্তর",
                    value = BengaliNumberFormatter.toBn(session.wrongCount),
                    color = Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "নির্ভুলতা",
                    value = "${BengaliNumberFormatter.toBn(accuracy)}%",
                    color = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "সর্বোচ্চ কম্বো",
                    value = "${BengaliNumberFormatter.toBn(session.highestStreak)} টি",
                    color = Color(0xFFEA580C),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Action Buttons
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("retry_mental_math_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = session.category.primaryColor)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "পুনরায় খেলুন",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                OutlinedButton(
                    onClick = onSelectAnother,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("another_category_btn"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "অন্য বিষয় নির্বাচন করুন",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                if (session.history.isNotEmpty()) {
                    OutlinedButton(
                        onClick = onToggleReview,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("toggle_review_btn"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (session.isReviewMode) "পর্যালোচনা লুকান" else "উত্তর পর্যালোচনা দেখুন",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Answer Review Section
        if (session.isReviewMode && session.history.isNotEmpty()) {
            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "বিস্তারিত উত্তর পর্যালোচনা",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            itemsIndexed(session.history) { index, item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isCorrect) Color(0xFFECFDF5) else Color(0xFFFFF1F2)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "প্রশ্ন ${BengaliNumberFormatter.toBn(index + 1)}: ${item.question.expressionBn}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = if (item.isCorrect) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (item.isCorrect) Color(0xFF059669) else Color(0xFFE11D48),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "আপনার উত্তর: ${BengaliNumberFormatter.toBn(item.chosenOption)} • সঠিক উত্তর: ${item.question.correctAnswerBn}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.question.explanationBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
    }
}
