package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.BengaliNumberFormatter
import com.example.math.QuizTopic
import com.example.ui.MathViewModel
import com.example.ui.components.MathSectionHeader
import com.example.ui.components.MathTopAppBar

@Composable
fun QuizzesScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val quizScores by viewModel.quizScores.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = "ইন্টারেক্টিভ কুইজ",
            subtitle = "জ্ঞান যাচাই, নৈর্ব্যক্তিক পরীক্ষা ও স্কোরবোর্ড",
            onBackClick = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = Color.Black.copy(alpha = 0.08f),
                            spotColor = Color.Black.copy(alpha = 0.12f)
                        ),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 6.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "কুইজ বিভাগ নির্বাচন করুন",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "প্রতিটি বিষয়ে ১০টি প্রশ্ন • তাৎক্ষণিক সমাধান ও বিশ্লেষণ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }

            // Quiz Topics
            items(QuizTopic.values()) { topic ->
                val topicScores = quizScores.filter { it.topicId == topic.id }
                val bestScore = topicScores.maxByOrNull { it.percentage }

                val (icon, badgeColor) = when (topic) {
                    QuizTopic.MULTIPLICATION -> Icons.AutoMirrored.Filled.MenuBook to Color(0xFF2563EB)
                    QuizTopic.PRIMES -> Icons.Default.LooksOne to Color(0xFF0D9488)
                    QuizTopic.SQUARES_CUBES -> Icons.Default.Straighten to Color(0xFFD97706)
                    QuizTopic.ALGEBRA -> Icons.Default.Functions to Color(0xFF7C3AED)
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Color.Black.copy(alpha = 0.06f),
                            spotColor = Color.Black.copy(alpha = 0.10f)
                        )
                        .clickable { viewModel.startQuiz(topic, 10) }
                        .testTag("quiz_topic_${topic.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 6.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = badgeColor.copy(alpha = 0.15f),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = badgeColor,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = topic.titleBn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (bestScore != null)
                                    "সেরা স্কোর: ${BengaliNumberFormatter.toBn(bestScore.score)}/${BengaliNumberFormatter.toBn(bestScore.totalQuestions)} (${BengaliNumberFormatter.toBn(bestScore.percentage)}%)"
                                else
                                    "১০টি নৈর্ব্যক্তিক প্রশ্ন • অংশ নিন",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (bestScore != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "শুরু করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Score History
            if (quizScores.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    MathSectionHeader(
                        title = "বিগত কুইজ ইতিহাস",
                        subtitle = "অফলাইন ডাটাবেসে সংরক্ষিত ফলাফল"
                    )
                }

                items(quizScores.take(5)) { score ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color.Black.copy(alpha = 0.04f),
                                spotColor = Color.Black.copy(alpha = 0.06f)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = score.topicTitleBn,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "স্কোর: ${BengaliNumberFormatter.toBn(score.score)} / ${BengaliNumberFormatter.toBn(score.totalQuestions)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (score.percentage >= 80) Color(0xFF10B981) else if (score.percentage >= 50) Color(0xFFF59E0B) else Color(0xFFEF4444)
                            ) {
                                Text(
                                    text = "${BengaliNumberFormatter.toBn(score.percentage)}%",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizPlayScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.quizSession.collectAsState()

    // When back is pressed in quiz play, cleanly close review mode or return to quiz list without trapping
    BackHandler {
        if (session.isReviewMode) {
            viewModel.toggleQuizReviewMode()
        } else {
            viewModel.exitQuizToTopicList()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = session.topic.titleBn,
            subtitle = if (!session.isFinished)
                "প্রশ্ন ${BengaliNumberFormatter.toBn(session.currentIndex + 1)} / ${BengaliNumberFormatter.toBn(session.questions.size)} • সঠিক: ${BengaliNumberFormatter.toBn(session.correctCount)}টি"
            else "পরীক্ষা সমাপ্তি ও ফলাফল",
            onBackClick = { viewModel.exitQuizToTopicList() }
        )

        if (session.isFinished) {
            // ==========================================
            // QUIZ COMPLETED RESULT SCREEN (BUG-FREE & ENRICHED)
            // ==========================================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val total = session.questions.size
                val correct = session.correctCount
                val incorrect = (total - correct).coerceAtLeast(0)
                val percentage = if (total > 0) (correct * 100) / total else 0

                val scoreColor = if (percentage >= 80) Color(0xFF10B981) else if (percentage >= 50) Color(0xFFF59E0B) else Color(0xFFEF4444)

                item {
                    // Celebratory Hero Score Card
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(22.dp),
                                ambientColor = Color.Black.copy(alpha = 0.08f),
                                spotColor = Color.Black.copy(alpha = 0.14f)
                            )
                            .testTag("quiz_result_card"),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Circular Gauge
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .background(scoreColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${BengaliNumberFormatter.toBn(percentage)}%",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.ExtraBold
                                        ),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${BengaliNumberFormatter.toBn(correct)} / ${BengaliNumberFormatter.toBn(total)}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = when {
                                    percentage == 100 -> "অসাধারণ! শতভাগ সঠিক উত্তর! 🏆"
                                    percentage >= 80 -> "চমৎকার পারফরম্যান্স! 🌟"
                                    percentage >= 50 -> "ভালো হয়েছে! নিয়মিত অনুশীলন করুন 👍"
                                    else -> "আরও নিয়মিত পড়ার পরামর্শ দেওয়া হচ্ছে 📚"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "আপনি ${BengaliNumberFormatter.toBn(total)} টি প্রশ্নের মধ্যে ${BengaliNumberFormatter.toBn(correct)} টি সঠিক এবং ${BengaliNumberFormatter.toBn(incorrect)} টি ভুল উত্তর দিয়েছেন।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Stats Pills Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF10B981).copy(alpha = 0.15f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "সঠিক",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF047857)
                                        )
                                        Text(
                                            text = "${BengaliNumberFormatter.toBn(correct)} টি",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color(0xFF047857)
                                        )
                                    }
                                }

                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFEF4444).copy(alpha = 0.15f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "ভুল",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFFB91C1C)
                                        )
                                        Text(
                                            text = "${BengaliNumberFormatter.toBn(incorrect)} টি",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color(0xFFB91C1C)
                                        )
                                    }
                                }

                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "মোট",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "${BengaliNumberFormatter.toBn(total)} টি",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // --- ACTION BUTTONS (NEVER LEAVE USER TRAPPED) ---
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 1. Next Quiz Topic Button
                        val allTopics = QuizTopic.values()
                        val currentTopicIdx = allTopics.indexOf(session.topic)
                        val nextTopic = if (currentTopicIdx != -1 && currentTopicIdx + 1 < allTopics.size) {
                            allTopics[currentTopicIdx + 1]
                        } else {
                            allTopics.first()
                        }

                        Button(
                            onClick = { viewModel.startNextQuizTopic() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_next_quiz_topic"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "পরবর্তী কুইজ: ${nextTopic.titleBn}",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }

                        // 2. Review Answers Toggle Button
                        OutlinedButton(
                            onClick = { viewModel.toggleQuizReviewMode() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_review_answers")
                        ) {
                            Icon(
                                imageVector = if (session.isReviewMode) Icons.Default.KeyboardArrowUp else Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (session.isReviewMode) "উত্তর পর্যালোচনা বন্ধ করুন" else "সকল উত্তর ও সমাধান দেখুন",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // 3. Retry Quiz Button
                        OutlinedButton(
                            onClick = { viewModel.startQuiz(session.topic, 10) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("retake_quiz_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "এই কুইজ আবার পরীক্ষা দিন",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // 4. Return to Quiz List Button
                        OutlinedButton(
                            onClick = { viewModel.exitQuizToTopicList() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_exit_to_quiz_list")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("কুইজ তালিকায় ফিরে যান")
                        }
                    }
                }

                // --- 5. DETAILED QUESTION-BY-QUESTION REVIEW ---
                if (session.isReviewMode) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "প্রশ্নোত্তর পর্যালোচনা (১০টি প্রশ্ন):",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    itemsIndexed(session.questions) { qIndex, question ->
                        val selectedOptIdx = session.userAnswers[qIndex]
                        val isUserCorrect = selectedOptIdx == question.correctIndex

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(14.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.04f),
                                    spotColor = Color.Black.copy(alpha = 0.08f)
                                )
                                .testTag("review_question_$qIndex"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 4.dp
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "প্রশ্ন ${BengaliNumberFormatter.toBn(qIndex + 1)}",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isUserCorrect) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = if (isUserCorrect) "✓ সঠিক" else "✗ ভুল",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (isUserCorrect) Color(0xFF047857) else Color(0xFFB91C1C),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = question.questionBn,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Options Review
                                question.optionsBn.forEachIndexed { optIdx, optText ->
                                    val isSelectedByUser = selectedOptIdx == optIdx
                                    val isCorrectChoice = question.correctIndex == optIdx

                                    val rowBg = when {
                                        isCorrectChoice -> Color(0xFF10B981).copy(alpha = 0.12f)
                                        isSelectedByUser -> Color(0xFFEF4444).copy(alpha = 0.12f)
                                        else -> Color.Transparent
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = rowBg,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${BengaliNumberFormatter.toBn(optIdx + 1)}. ",
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = optText,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = if (isCorrectChoice || isSelectedByUser) FontWeight.Bold else FontWeight.Normal
                                                ),
                                                color = if (isCorrectChoice) Color(0xFF047857) else if (isSelectedByUser) Color(0xFFB91C1C) else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (isCorrectChoice) {
                                                Text(
                                                    text = "সঠিক উত্তর",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = Color(0xFF047857)
                                                )
                                            } else if (isSelectedByUser) {
                                                Text(
                                                    text = "আপনার উত্তর",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = Color(0xFFB91C1C)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Explanation
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "ব্যাখ্যা: ${question.explanationBn}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.toggleQuizReviewMode() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("btn_close_review_bottom")
                            ) {
                                Text("পর্যালোচনা বন্ধ করুন")
                            }

                            Button(
                                onClick = { viewModel.exitQuizToTopicList() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("btn_exit_from_review_bottom")
                            ) {
                                Text("কুইজ তালিকা")
                            }
                        }
                    }
                }
            }
        } else {
            // ==========================================
            // ACTIVE QUESTION SCREEN
            // ==========================================
            val currentQ = session.questions.getOrNull(session.currentIndex)
            if (currentQ != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Progress Indicator with Question Counter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "প্রশ্ন ${BengaliNumberFormatter.toBn(session.currentIndex + 1)} / ${BengaliNumberFormatter.toBn(session.questions.size)}",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "স্কোর: ${BengaliNumberFormatter.toBn(session.correctCount)}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val progressRatio = (session.currentIndex + 1).toFloat() / session.questions.size.toFloat()
                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Question Card
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(18.dp),
                                ambientColor = Color.Black.copy(alpha = 0.06f),
                                spotColor = Color.Black.copy(alpha = 0.10f)
                            ),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 6.dp
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = currentQ.topicTitleBn,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = currentQ.questionBn,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Options list
                    currentQ.optionsBn.forEachIndexed { optIndex, optionText ->
                        val isSelected = session.selectedOptionIndex == optIndex
                        val isCorrectOption = optIndex == currentQ.correctIndex

                        val containerColor = when {
                            session.isAnswerSubmitted && isCorrectOption -> Color(0xFF10B981).copy(alpha = 0.2f)
                            session.isAnswerSubmitted && isSelected && !isCorrectOption -> Color(0xFFEF4444).copy(alpha = 0.2f)
                            isSelected -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        }

                        val outlineColor = when {
                            session.isAnswerSubmitted && isCorrectOption -> Color(0xFF10B981)
                            session.isAnswerSubmitted && isSelected && !isCorrectOption -> Color(0xFFEF4444)
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        }

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(14.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.04f),
                                    spotColor = Color.Black.copy(alpha = 0.06f)
                                )
                                .clickable(enabled = !session.isAnswerSubmitted) {
                                    viewModel.selectQuizOption(optIndex)
                                }
                                .testTag("quiz_option_$optIndex"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 4.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.5.dp, outlineColor, RoundedCornerShape(14.dp))
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = BengaliNumberFormatter.toBn(optIndex + 1),
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

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

                    // Explanation Box after submit
                    AnimatedVisibility(visible = session.isAnswerSubmitted) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "ব্যাখ্যা ও সমাধান:",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = currentQ.explanationBn,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Bottom Action buttons
                    if (!session.isAnswerSubmitted) {
                        Button(
                            onClick = { viewModel.submitQuizAnswer() },
                            enabled = session.selectedOptionIndex != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("submit_answer_btn"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("উত্তর জমা দিন", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.nextQuizQuestion() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("next_question_btn"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = if (session.currentIndex + 1 < session.questions.size) "পরবর্তী প্রশ্ন →" else "ফলাফল দেখুন ★",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "কোনো প্রশ্ন লোড করা হয়নি বা পরীক্ষা সমাপ্ত হয়েছে",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.exitQuizToTopicList() },
                            modifier = Modifier.testTag("btn_empty_quiz_exit")
                        ) {
                            Text("কুইজ তালিকায় ফিরে যান")
                        }
                    }
                }
            }
        }
    }
}
