package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.BengaliNumberFormatter
import com.example.math.PracticeDifficulty
import com.example.math.PracticeQuestion
import com.example.ui.MathViewModel
import com.example.ui.components.MathTopAppBar
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeLabPlayScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.practiceSession.collectAsState()

    BackHandler {
        viewModel.exitPracticeToCategories()
    }

    // Live elapsed timer calculation
    var elapsedSec by remember { mutableLongStateOf(0L) }
    LaunchedEffect(session.isFinished) {
        if (!session.isFinished && session.startTimeMs > 0) {
            while (true) {
                elapsedSec = ((System.currentTimeMillis() - session.startTimeMs) / 1000).coerceAtLeast(0)
                delay(1000)
            }
        }
    }

    if (session.isFinished) {
        PracticeSessionSummary(
            session = session,
            onRetry = { viewModel.retryPracticeSession() },
            onSelectAnother = { viewModel.exitPracticeToCategories() },
            onToggleReview = { viewModel.togglePracticeReviewMode() },
            modifier = modifier
        )
    } else {
        val currentQ = session.questions.getOrNull(session.currentIndex)

        Column(
            modifier = modifier
                .fillMaxSize()
                .testTag("screen_practice_lab_play")
        ) {
            // Header Top Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.exitPracticeToCategories() },
                                modifier = Modifier.testTag("btn_exit_practice")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "প্রস্থান"
                                )
                            }
                            Column {
                                Text(
                                    text = session.category.titleBn,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1
                                )
                                Text(
                                    text = "স্তর: ${session.difficulty.titleBn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = session.difficulty.badgeColor
                                )
                            }
                        }

                        // Timer Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "সময়",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                val mins = elapsedSec / 60
                                val secs = elapsedSec % 60
                                val timeStr = if (mins > 0) {
                                    "${BengaliNumberFormatter.toBn(mins.toInt())}:${BengaliNumberFormatter.toBn(secs.toInt().toString().padStart(2, '0'))}"
                                } else {
                                    "${BengaliNumberFormatter.toBn(secs.toInt())} সে."
                                }
                                Text(
                                    text = timeStr,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Progress indicator
                    val progressRatio = if (session.questions.isNotEmpty()) {
                        (session.currentIndex + 1).toFloat() / session.questions.size.toFloat()
                    } else 0f

                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = session.category.primaryColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            if (currentQ != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Question Counter & Bengali Prompt
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "প্রশ্ন: ${BengaliNumberFormatter.toBn(session.currentIndex + 1)} / ${BengaliNumberFormatter.toBn(session.questions.size)}",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = session.category.primaryColor
                            )
                            Text(
                                text = "সঠিক: ${BengaliNumberFormatter.toBn(session.correctCount)}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color(0xFF10B981)
                            )
                        }
                    }

                    // Mathematical Expression Card
                    item {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = currentQ.questionBn,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = session.category.primaryColor.copy(alpha = 0.1f),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        session.category.primaryColor.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = currentQ.expression,
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            letterSpacing = 1.sp,
                                            fontSize = 24.sp
                                        ),
                                        color = session.category.primaryColor,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 4 Multiple Choice Options
                    item {
                        Text(
                            text = "সঠিক উত্তরটি নির্বাচন করুন:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    itemsIndexed(currentQ.optionsBn) { optIdx, optText ->
                        val isSelected = session.selectedOptionIndex == optIdx
                        val isCorrectOption = optIdx == currentQ.correctIndex

                        val containerColor by animateColorAsState(
                            targetValue = when {
                                session.isAnswerSubmitted && isCorrectOption -> Color(0xFF10B981).copy(alpha = 0.2f)
                                session.isAnswerSubmitted && isSelected && !isCorrectOption -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                isSelected -> session.category.primaryColor.copy(alpha = 0.15f)
                                else -> MaterialTheme.colorScheme.surface
                            },
                            animationSpec = spring(),
                            label = "optionColor"
                        )

                        val borderColor = when {
                            session.isAnswerSubmitted && isCorrectOption -> Color(0xFF10B981)
                            session.isAnswerSubmitted && isSelected && !isCorrectOption -> Color(0xFFEF4444)
                            isSelected -> session.category.primaryColor
                            else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable(enabled = !session.isAnswerSubmitted) {
                                    viewModel.selectPracticeOption(optIdx)
                                }
                                .border(
                                    width = if (isSelected || (session.isAnswerSubmitted && isCorrectOption)) 2.dp else 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .testTag("practice_opt_$optIdx"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = containerColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSelected) session.category.primaryColor else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            val letterBn = when (optIdx) {
                                                0 -> "ক"
                                                1 -> "খ"
                                                2 -> "গ"
                                                else -> "ঘ"
                                            }
                                            Text(
                                                text = letterBn,
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )
                                        }
                                    }

                                    Text(
                                        text = optText,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 18.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                if (session.isAnswerSubmitted) {
                                    if (isCorrectOption) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "সঠিক",
                                            tint = Color(0xFF10B981),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "ভুল",
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Optional direct manual keypad input (if student wants to write directly)
                    if (!session.isAnswerSubmitted && session.selectedOptionIndex == null) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = session.customInputText,
                                onValueChange = { viewModel.setPracticeCustomInput(it) },
                                label = { Text("অথবা সরাসরি উত্তর টাইপ করুন") },
                                placeholder = { Text("যেমন: ${currentQ.correctAnswerBn}") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { viewModel.submitPracticeAnswer() }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_practice_custom_answer"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    // Answer Verification Banner and Explanation (shown upon submission)
                    if (session.isAnswerSubmitted) {
                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (session.isCorrect) Color(0xFFECFDF5) else Color(0xFFFEF2F2),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (session.isCorrect) Color(0xFF10B981) else Color(0xFFEF4444)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("practice_feedback_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = if (session.isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (session.isCorrect) Color(0xFF10B981) else Color(0xFFEF4444),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Column {
                                        Text(
                                            text = if (session.isCorrect) "অভিনন্দন! আপনার উত্তর সঠিক হয়েছে।" else "দুঃখিত, উত্তরটি ভুল হয়েছে।",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (session.isCorrect) Color(0xFF065F46) else Color(0xFF991B1B)
                                            )
                                        )
                                        if (!session.isCorrect) {
                                            Text(
                                                text = "সঠিক উত্তর: ${currentQ.correctAnswerBn}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF991B1B)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Step-by-Step Explanation
                        if (currentQ.stepByStepExplanationBn.isNotEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = "ধাপে ধাপে সমাধান:",
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        currentQ.stepByStepExplanationBn.forEach { step ->
                                            Text(
                                                text = "• $step",
                                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Helpful Shortcut Tip
                        if (!currentQ.tipBn.isNullOrEmpty()) {
                            item {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFEF3C7).copy(alpha = 0.6f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = Color(0xFFD97706),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = currentQ.tipBn,
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF78350F))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Sticky Bottom Action Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        if (!session.isAnswerSubmitted) {
                            val canSubmit = session.selectedOptionIndex != null || session.customInputText.trim().isNotEmpty()
                            Button(
                                onClick = { viewModel.submitPracticeAnswer() },
                                enabled = canSubmit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("btn_submit_practice_answer"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = session.category.primaryColor
                                )
                            ) {
                                Text(
                                    text = "উত্তর নিশ্চিত করুন",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        } else {
                            val isLast = session.currentIndex + 1 >= session.questions.size
                            Button(
                                onClick = { viewModel.nextPracticeQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("btn_next_practice_question"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = session.category.primaryColor
                                )
                            ) {
                                Text(
                                    text = if (isLast) "ফলাফল দেখুন" else "পরবর্তী প্রশ্ন →",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Session Summary Screen displayed when a practice lab session finishes.
 */
@Composable
private fun PracticeSessionSummary(
    session: com.example.math.PracticeSessionState,
    onRetry: () -> Unit,
    onSelectAnother: () -> Unit,
    onToggleReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    val total = session.questions.size
    val accuracy = if (total > 0) (session.correctCount * 100) / total else 0

    val mins = session.totalTimeSeconds / 60
    val secs = session.totalTimeSeconds % 60
    val timeFormatted = if (mins > 0) {
        "${BengaliNumberFormatter.toBn(mins.toInt())} মিনিট ${BengaliNumberFormatter.toBn(secs.toInt())} সেকেন্ড"
    } else {
        "${BengaliNumberFormatter.toBn(secs.toInt())} সেকেন্ড"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("practice_session_summary")
    ) {
        MathTopAppBar(
            title = "অনুশীলন ফলাফল",
            subtitle = "${session.category.titleBn} (${session.difficulty.titleBn})",
            onBackClick = onSelectAnother
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Victory / Result Card
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (accuracy >= 80) Color(0xFF10B981).copy(alpha = 0.15f) else session.category.primaryColor.copy(alpha = 0.15f),
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = if (accuracy >= 80) Color(0xFF10B981) else session.category.primaryColor,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (accuracy >= 80) "অসাধারণ পারফরম্যান্স!" else if (accuracy >= 50) "চমৎকার অগ্রগতি!" else "অনুশীলন চালিয়ে যান!",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "আপনার গণিত দক্ষতা বৃদ্ধি পাচ্ছে",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Score & Accuracy Box
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${BengaliNumberFormatter.toBn(session.correctCount)} / ${BengaliNumberFormatter.toBn(total)}",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "সঠিক উত্তর",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${BengaliNumberFormatter.toBn(accuracy)}%",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (accuracy >= 80) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "সঠিকতার হার",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = BengaliNumberFormatter.toBn(session.wrongCount),
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFFEF4444)
                                    )
                                    Text(
                                        text = "ভুল উত্তর",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "ব্যয়িত সময়: $timeFormatted",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Buttons: Retry, Review, Choose Another
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onRetry,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_practice_retry"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("পুনরায় চেষ্টা")
                    }

                    OutlinedButton(
                        onClick = onToggleReview,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_practice_review"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (session.isReviewMode) "রিভিউ লুকান" else "উত্তর পর্যালোচনা")
                    }
                }
            }

            item {
                Button(
                    onClick = onSelectAnother,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_practice_select_another"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("অন্য বিষয় বা স্তর নির্বাচন করুন")
                }
            }

            // Review Mode Section
            if (session.isReviewMode) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "সকল প্রশ্নের সমাধান ও ব্যাখ্যা:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                itemsIndexed(session.questions) { idx, q ->
                    val userSelectedOpt = session.userAnswers[idx]
                    val isCorrect = userSelectedOpt == q.correctIndex

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCorrect) Color(0xFF10B981).copy(alpha = 0.5f) else Color(0xFFEF4444).copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "প্রশ্ন ${BengaliNumberFormatter.toBn(idx + 1)}: ${q.expression}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "সঠিক উত্তর: ${q.correctAnswerBn}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF10B981)
                            )

                            if (!isCorrect && userSelectedOpt != null && userSelectedOpt >= 0) {
                                val userOpt = q.optionsBn.getOrNull(userSelectedOpt) ?: ""
                                Text(
                                    text = "আপনার দেওয়া উত্তর: $userOpt",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFEF4444)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "ব্যাখ্যা ও সমাধান:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            q.stepByStepExplanationBn.forEach { step ->
                                Text(
                                    text = "• $step",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
