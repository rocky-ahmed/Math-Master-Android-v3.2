package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizScoreEntity
import com.example.math.BengaliNumberFormatter
import com.example.math.QuizTopic
import com.example.ui.MathViewModel
import com.example.ui.Screen
import com.example.ui.WeeklyDayStat
import com.example.ui.components.MathSectionHeader
import com.example.ui.components.MathTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val streakDays by viewModel.streakDays.collectAsState()
    val longestStreak by viewModel.longestStreak.collectAsState()
    val weeklyActivities by viewModel.weeklyActivities.collectAsState()
    val progressList by viewModel.progressList.collectAsState()
    val quizScores by viewModel.quizScores.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()

    // Calculated stats
    val tablesLearned = progressList.count { it.itemId.startsWith("table_") && it.isLearned }
    val primesLearned = progressList.count { it.itemId.startsWith("prime_") && it.isLearned }
    val squaresCubesLearned = progressList.count { (it.itemId.startsWith("sq_") || it.itemId.startsWith("cube_")) && it.isLearned }
    val formulasLearned = progressList.count { it.itemId.startsWith("formula_") && it.isLearned }

    val totalTables = 40
    val totalPrimes = 46
    val totalSquaresCubes = 40
    val totalFormulas = 22
    val totalSyllabusItems = totalTables + totalPrimes + totalSquaresCubes + totalFormulas
    val totalLearned = tablesLearned + primesLearned + squaresCubesLearned + formulasLearned
    val overallPercentage = if (totalSyllabusItems > 0) (totalLearned * 100) / totalSyllabusItems else 0

    val totalQuizzes = quizScores.size
    val averageQuizPercent = if (quizScores.isNotEmpty()) quizScores.map { it.percentage }.average().toInt() else 0
    val bestQuizPercent = quizScores.maxOfOrNull { it.percentage } ?: 0
    val totalCorrectAnswers = quizScores.sumOf { it.score }
    val perfectScoreCount = quizScores.count { it.percentage == 100 }

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = "অগ্রগতি ও পরিসংখ্যান",
            subtitle = "আপনার দৈনিক স্ট্রিক, কুইজ স্কোর ও বিষয়ভিত্তিক বিশ্লেষণ",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Bookmarks) },
                    modifier = Modifier.testTag("dashboard_bookmarks_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "বুকমার্ক দেখুন"
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("dashboard_scroll_column"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Daily Study Streak Hero Card
            item {
                StreakSummaryHeroCard(
                    currentStreak = streakDays,
                    longestStreak = longestStreak,
                    weeklyActivities = weeklyActivities,
                    onPracticeClick = { viewModel.startTutor(7) }
                )
            }

            // 2. Weekly Study Streak Activity Chart
            item {
                WeeklyStreakBarChartCard(
                    weeklyActivities = weeklyActivities
                )
            }

            // 3. Overall Topic Mastery Donut Chart
            item {
                TopicMasteryDonutCard(
                    tablesLearned = tablesLearned,
                    totalTables = totalTables,
                    primesLearned = primesLearned,
                    totalPrimes = totalPrimes,
                    squaresCubesLearned = squaresCubesLearned,
                    totalSquaresCubes = totalSquaresCubes,
                    formulasLearned = formulasLearned,
                    totalFormulas = totalFormulas,
                    overallPercentage = overallPercentage
                )
            }

            // 4. Detailed Topic Progress Breakdown Bars
            item {
                TopicProgressBreakdownCard(
                    tablesLearned = tablesLearned,
                    totalTables = totalTables,
                    primesLearned = primesLearned,
                    totalPrimes = totalPrimes,
                    squaresCubesLearned = squaresCubesLearned,
                    totalSquaresCubes = totalSquaresCubes,
                    formulasLearned = formulasLearned,
                    totalFormulas = totalFormulas,
                    onOpenTables = { viewModel.navigateTo(Screen.Tables) },
                    onOpenPrimes = { viewModel.navigateTo(Screen.Primes) },
                    onOpenSquares = { viewModel.navigateTo(Screen.SquaresCubes) },
                    onOpenFormulas = { viewModel.navigateTo(Screen.Formulas) }
                )
            }

            // 5. Quiz Performance & Trend Chart
            item {
                QuizPerformanceAnalyticsCard(
                    quizScores = quizScores,
                    totalQuizzes = totalQuizzes,
                    averagePercent = averageQuizPercent,
                    bestPercent = bestQuizPercent,
                    totalCorrect = totalCorrectAnswers,
                    perfectCount = perfectScoreCount,
                    onStartQuiz = { viewModel.navigateTo(Screen.Quizzes) }
                )
            }

            // 6. Interactive Learning Milestones & Badges
            item {
                LearningAchievementsCard(
                    streakDays = streakDays,
                    tablesLearned = tablesLearned,
                    totalCorrect = totalCorrectAnswers,
                    perfectCount = perfectScoreCount,
                    formulasLearned = formulasLearned
                )
            }

            // 7. Quick Action CTA
            item {
                DashboardActionRow(
                    onStartQuiz = { viewModel.navigateTo(Screen.Quizzes) },
                    onStartTutor = { viewModel.startTutor(5) }
                )
            }
        }
    }
}

/**
 * 1. Daily Study Streak Hero Card
 */
@Composable
fun StreakSummaryHeroCard(
    currentStreak: Int,
    longestStreak: Int,
    weeklyActivities: List<WeeklyDayStat>,
    onPracticeClick: () -> Unit
) {
    val todayStat = weeklyActivities.find { it.isToday }
    val todayCount = todayStat?.count ?: 0
    val isTodayActive = todayCount > 0

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("streak_summary_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEA580C).copy(alpha = 0.12f),
                            Color(0xFFF59E0B).copy(alpha = 0.04f),
                            Color.Transparent
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEA580C)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "দৈনিক অধ্যবসায় স্ট্রিক",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isTodayActive) {
                                "আজকের লক্ষ্য সম্পন্ন! আপনি নিয়মিত গণিত চর্চা করছেন।"
                            } else {
                                "আজকের লক্ষ্য পূরণ করতে এখনই একটি কুইজ বা অনুশীলন সম্পন্ন করুন।"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Streak numbers pill
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            text = "${BengaliNumberFormatter.toBn(currentStreak)} দিন",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFEA580C)
                            )
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF59E0B).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "সর্বোচ্চ: ${BengaliNumberFormatter.toBn(longestStreak)} দিন",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = if (isTodayActive) Color(0xFF10B981) else MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.size(12.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTodayActive) "আজ সক্রিয় (${BengaliNumberFormatter.toBn(todayCount)}টি কার্যক্রম)" else "আজ এখনও বাকি",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .clickable { onPracticeClick() }
                            .testTag("streak_quick_practice_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "অনুশীলন করুন",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 2. Weekly Study Streak Activity Chart (Custom Canvas Bar Chart)
 */
@Composable
fun WeeklyStreakBarChartCard(
    weeklyActivities: List<WeeklyDayStat>
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("weekly_streak_chart_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "সাপ্তাহিক অনুশীলনের চার্ট",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "গত ৭ দিনের গণিত অনুশীলন ও সক্রিয়তা",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "৭ দিন",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Canvas Bar Chart
            val animProgress = remember { Animatable(0f) }
            LaunchedEffect(weeklyActivities) {
                animProgress.snapTo(0f)
                animProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                )
            }

            val maxCount = maxOf(weeklyActivities.maxOfOrNull { it.count } ?: 1, 5)
            val primaryColor = MaterialTheme.colorScheme.primary
            val trackColor = MaterialTheme.colorScheme.surfaceVariant
            val fireColor = Color(0xFFEA580C)
            val activeColor = Color(0xFFF59E0B)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val count = weeklyActivities.size
                    if (count == 0) return@Canvas

                    val barWidth = size.width / (count * 2.2f)
                    val availableHeight = size.height - 30.dp.toPx()
                    val bottomY = size.height - 24.dp.toPx()

                    weeklyActivities.forEachIndexed { index, stat ->
                        val slotWidth = size.width / count
                        val centerX = slotWidth * index + slotWidth / 2f
                        val leftX = centerX - barWidth / 2f

                        // Draw background track
                        drawRoundRect(
                            color = trackColor.copy(alpha = 0.5f),
                            topLeft = Offset(leftX, 0f),
                            size = Size(barWidth, bottomY),
                            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )

                        // Draw filled active bar
                        val ratio = if (maxCount > 0) (stat.count.toFloat() / maxCount).coerceIn(0f, 1f) else 0f
                        val barHeight = (availableHeight * ratio * animProgress.value).coerceAtLeast(
                            if (stat.count > 0) 8.dp.toPx() else 0f
                        )

                        val barTopY = bottomY - barHeight

                        val barBrush = if (stat.isToday) {
                            Brush.verticalGradient(
                                colors = listOf(fireColor, activeColor),
                                startY = barTopY,
                                endY = bottomY
                            )
                        } else if (stat.count > 0) {
                            Brush.verticalGradient(
                                colors = listOf(primaryColor, primaryColor.copy(alpha = 0.7f)),
                                startY = barTopY,
                                endY = bottomY
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Transparent)
                            )
                        }

                        if (barHeight > 0f) {
                            drawRoundRect(
                                brush = barBrush,
                                topLeft = Offset(leftX, barTopY),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                            )
                        }

                        // Outline today's bar
                        if (stat.isToday) {
                            drawRoundRect(
                                color = fireColor,
                                topLeft = Offset(leftX - 2.dp.toPx(), 0f),
                                size = Size(barWidth + 4.dp.toPx(), bottomY),
                                cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx()),
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                    }
                }
            }

            // X-Axis Labels (Day names & Count badges in Bengali)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weeklyActivities.forEach { stat ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = BengaliNumberFormatter.toBn(stat.count),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (stat.count > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = stat.dayNameBn,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (stat.isToday) FontWeight.ExtraBold else FontWeight.Medium
                            ),
                            color = if (stat.isToday) Color(0xFFEA580C) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (stat.isToday) {
                            Surface(
                                shape = RoundedCornerShape(3.dp),
                                color = Color(0xFFEA580C),
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = "আজ",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
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
 * 3. Overall Topic Mastery Donut Chart
 */
@Composable
fun TopicMasteryDonutCard(
    tablesLearned: Int,
    totalTables: Int,
    primesLearned: Int,
    totalPrimes: Int,
    squaresCubesLearned: Int,
    totalSquaresCubes: Int,
    formulasLearned: Int,
    totalFormulas: Int,
    overallPercentage: Int
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("topic_donut_chart_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "সমগ্র সিলেবাস সমাপ্তির চার্ট",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "৪টি প্রধান শাখার আনুপাতিক আয়ত্তীকরণ বিশ্লেষণ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            val animDonut = remember { Animatable(0f) }
            LaunchedEffect(tablesLearned, primesLearned, squaresCubesLearned, formulasLearned) {
                animDonut.snapTo(0f)
                animDonut.animateTo(1f, tween(durationMillis = 900, easing = FastOutSlowInEasing))
            }

            // Colors for 4 segments
            val colorTables = Color(0xFF2563EB)   // Blue
            val colorPrimes = Color(0xFF0D9488)   // Teal
            val colorSquares = Color(0xFFD97706)  // Amber
            val colorFormulas = Color(0xFF7C3AED) // Purple
            val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)

            // Calculate ratios for a 360 degree circle
            val tablesRatio = (tablesLearned.toFloat() / totalTables).coerceIn(0f, 1f)
            val primesRatio = (primesLearned.toFloat() / totalPrimes).coerceIn(0f, 1f)
            val squaresRatio = (squaresCubesLearned.toFloat() / totalSquaresCubes).coerceIn(0f, 1f)
            val formulasRatio = (formulasLearned.toFloat() / totalFormulas).coerceIn(0f, 1f)

            val sliceSpan = 85f // each topic has a 85 deg quadrant with a 5 deg gap

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Donut Chart Canvas
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 18.dp.toPx()
                        val arcSize = size.width - strokeWidth
                        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                        // 1. Draw full track circles
                        drawArc(
                            color = trackColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        // 2. Draw 4 segments with subtle gap
                        val segments = listOf(
                            Triple(0f, tablesRatio, colorTables),
                            Triple(90f, primesRatio, colorPrimes),
                            Triple(180f, squaresRatio, colorSquares),
                            Triple(270f, formulasRatio, colorFormulas)
                        )

                        segments.forEach { (startAngle, ratio, color) ->
                            val sweep = sliceSpan * ratio * animDonut.value
                            if (sweep > 0.5f) {
                                drawArc(
                                    color = color,
                                    startAngle = startAngle,
                                    sweepAngle = sweep,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }
                        }
                    }

                    // Center percentage display
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${BengaliNumberFormatter.toBn(overallPercentage)}%",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "আয়ত্ত হয়েছে",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Legend List
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LegendItem(
                        color = colorTables,
                        titleBn = "নামতা (১-৪০)",
                        countTextBn = "${BengaliNumberFormatter.toBn(tablesLearned)}/${BengaliNumberFormatter.toBn(totalTables)}",
                        percent = (tablesLearned * 100) / totalTables
                    )
                    LegendItem(
                        color = colorPrimes,
                        titleBn = "মৌলিক (১-২০০)",
                        countTextBn = "${BengaliNumberFormatter.toBn(primesLearned)}/${BengaliNumberFormatter.toBn(totalPrimes)}",
                        percent = (primesLearned * 100) / totalPrimes
                    )
                    LegendItem(
                        color = colorSquares,
                        titleBn = "বর্গ ও ঘন (১-২০)",
                        countTextBn = "${BengaliNumberFormatter.toBn(squaresCubesLearned)}/${BengaliNumberFormatter.toBn(totalSquaresCubes)}",
                        percent = (squaresCubesLearned * 100) / totalSquaresCubes
                    )
                    LegendItem(
                        color = colorFormulas,
                        titleBn = "বীজগণিত সূত্র",
                        countTextBn = "${BengaliNumberFormatter.toBn(formulasLearned)}/${BengaliNumberFormatter.toBn(totalFormulas)}",
                        percent = (formulasLearned * 100) / totalFormulas
                    )
                }
            }
        }
    }
}

@Composable
fun LegendItem(
    color: Color,
    titleBn: String,
    countTextBn: String,
    percent: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                shape = CircleShape,
                color = color,
                modifier = Modifier.size(10.dp)
            ) {}
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = titleBn,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
        Text(
            text = "$countTextBn (${BengaliNumberFormatter.toBn(percent)}%)",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 4. Detailed Topic Progress Breakdown Cards
 */
@Composable
fun TopicProgressBreakdownCard(
    tablesLearned: Int,
    totalTables: Int,
    primesLearned: Int,
    totalPrimes: Int,
    squaresCubesLearned: Int,
    totalSquaresCubes: Int,
    formulasLearned: Int,
    totalFormulas: Int,
    onOpenTables: () -> Unit,
    onOpenPrimes: () -> Unit,
    onOpenSquares: () -> Unit,
    onOpenFormulas: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("topic_breakdown_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "টপিকভিত্তিক বিস্তারিত অগ্রগতি",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "প্রতিটি অধ্যায়ের নির্ভুল সমাপ্তি পরিসংখ্যান",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            TopicBarRow(
                icon = Icons.AutoMirrored.Filled.MenuBook,
                titleBn = "নামতা (১ থেকে ৪০)",
                subtitleBn = "৪টি রেঞ্জে বিভক্ত নামতা তালিকা",
                learned = tablesLearned,
                total = totalTables,
                barColor = Color(0xFF2563EB),
                onClick = onOpenTables
            )

            Spacer(modifier = Modifier.height(12.dp))

            TopicBarRow(
                icon = Icons.Default.LooksOne,
                titleBn = "মৌলিক সংখ্যা (১ থেকে ২০০)",
                subtitleBn = "১-১০০ (২৫টি) ও ১০১-২০০ (২১টি) মৌলিক সংখ্যা",
                learned = primesLearned,
                total = totalPrimes,
                barColor = Color(0xFF0D9488),
                onClick = onOpenPrimes
            )

            Spacer(modifier = Modifier.height(12.dp))

            TopicBarRow(
                icon = Icons.Default.Straighten,
                titleBn = "বর্গ ও ঘন (১ থেকে ২০)",
                subtitleBn = "বর্গমূল ও ঘনমূলের স্পষ্ট চার্ট",
                learned = squaresCubesLearned,
                total = totalSquaresCubes,
                barColor = Color(0xFFD97706),
                onClick = onOpenSquares
            )

            Spacer(modifier = Modifier.height(12.dp))

            TopicBarRow(
                icon = Icons.Default.Functions,
                titleBn = "বীজগণিতীয় সূত্রাবলী",
                subtitleBn = "বর্গের ১৪টি ও ঘনের ৮টি সূত্র এবং প্রমাণ",
                learned = formulasLearned,
                total = totalFormulas,
                barColor = Color(0xFF7C3AED),
                onClick = onOpenFormulas
            )
        }
    }
}

@Composable
fun TopicBarRow(
    icon: ImageVector,
    titleBn: String,
    subtitleBn: String,
    learned: Int,
    total: Int,
    barColor: Color,
    onClick: () -> Unit
) {
    val percentage = if (total > 0) (learned * 100) / total else 0

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(barColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = barColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = titleBn,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = subtitleBn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = barColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${BengaliNumberFormatter.toBn(percentage)}%",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = barColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "আয়ত্ত: ${BengaliNumberFormatter.toBn(learned)}/${BengaliNumberFormatter.toBn(total)} টি",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = barColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

/**
 * 5. Quiz Performance & Trend Chart
 */
@Composable
fun QuizPerformanceAnalyticsCard(
    quizScores: List<QuizScoreEntity>,
    totalQuizzes: Int,
    averagePercent: Int,
    bestPercent: Int,
    totalCorrect: Int,
    perfectCount: Int,
    onStartQuiz: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_analytics_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "কুইজ পারফরম্যান্স ও ট্রেন্ড",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "পরীক্ষার নম্বর ও নির্ভুলতার ধারা",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE11D48).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${BengaliNumberFormatter.toBn(totalQuizzes)} টি কুইজ",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFE11D48),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Mini Stat Cards Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatPillBox(
                    label = "গড় নির্ভুলতা",
                    value = "${BengaliNumberFormatter.toBn(averagePercent)}%",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                StatPillBox(
                    label = "সর্বোচ্চ স্কোর",
                    value = "${BengaliNumberFormatter.toBn(bestPercent)}%",
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
                StatPillBox(
                    label = "সঠিক উত্তর",
                    value = BengaliNumberFormatter.toBn(totalCorrect),
                    color = Color(0xFFD97706),
                    modifier = Modifier.weight(1f)
                )
                StatPillBox(
                    label = "১০০% স্কোর",
                    value = BengaliNumberFormatter.toBn(perfectCount),
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Quiz Trend Chart
            if (quizScores.isNotEmpty()) {
                Text(
                    text = "সর্বশেষ কুইজের ধারা (স্কোর %)",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                QuizScoreTrendLineChart(scores = quizScores.take(8).reversed())
            } else {
                // Empty state prompt
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "এখনও কোনো কুইজ সম্পন্ন হয়নি!",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "আপনার গণিত দক্ষতা যাচাই করতে একটি কুইজ শুরু করুন।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onStartQuiz,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("প্রথম কুইজ শুরু করুন")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatPillBox(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.1f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

/**
 * Custom Canvas Line Chart for Quiz Trend
 */
@Composable
fun QuizScoreTrendLineChart(scores: List<QuizScoreEntity>) {
    val animLine = remember { Animatable(0f) }
    LaunchedEffect(scores) {
        animLine.snapTo(0f)
        animLine.animateTo(1f, tween(durationMillis = 800, easing = FastOutSlowInEasing))
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    val dotColor = Color(0xFF10B981)

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val pointCount = scores.size
                if (pointCount < 1) return@Canvas

                val availableWidth = size.width - 20.dp.toPx()
                val availableHeight = size.height - 30.dp.toPx()
                val leftPad = 10.dp.toPx()
                val topPad = 10.dp.toPx()
                val bottomY = topPad + availableHeight

                // Draw horizontal guide lines (0%, 50%, 100%)
                listOf(0f, 0.5f, 1f).forEach { fraction ->
                    val y = bottomY - availableHeight * fraction
                    drawLine(
                        color = gridColor,
                        start = Offset(leftPad, y),
                        end = Offset(leftPad + availableWidth, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Calculate points
                val points = scores.mapIndexed { index, entity ->
                    val x = if (pointCount == 1) {
                        leftPad + availableWidth / 2f
                    } else {
                        leftPad + (availableWidth / (pointCount - 1)) * index
                    }
                    val percentRatio = (entity.percentage.toFloat() / 100f).coerceIn(0f, 1f)
                    val y = bottomY - availableHeight * percentRatio * animLine.value
                    Offset(x, y)
                }

                // Draw filled gradient area under curve
                if (points.size >= 2) {
                    val fillPath = Path().apply {
                        moveTo(points.first().x, bottomY)
                        points.forEach { lineTo(it.x, it.y) }
                        lineTo(points.last().x, bottomY)
                        close()
                    }
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent),
                            startY = topPad,
                            endY = bottomY
                        )
                    )

                    // Draw connecting line
                    val linePath = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        for (i in 1 until points.size) {
                            lineTo(points[i].x, points[i].y)
                        }
                    }
                    drawPath(
                        path = linePath,
                        color = primaryColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Draw circular points
                points.forEach { pt ->
                    // Outer glow
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.2f),
                        radius = 8.dp.toPx(),
                        center = pt
                    )
                    // Inner dot
                    drawCircle(
                        color = dotColor,
                        radius = 4.5.dp.toPx(),
                        center = pt
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2.dp.toPx(),
                        center = pt
                    )
                }
            }
        }

        // X-Axis score labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            scores.forEachIndexed { idx, s ->
                Text(
                    text = "${BengaliNumberFormatter.toBn(s.percentage)}%",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (s.percentage >= 80) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}

/**
 * 6. Interactive Learning Milestones & Badges
 */
@Composable
fun LearningAchievementsCard(
    streakDays: Int,
    tablesLearned: Int,
    totalCorrect: Int,
    perfectCount: Int,
    formulasLearned: Int
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("achievements_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "অর্জিত ব্যাজ ও মাইলফলক",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "আপনার অধ্যয়ন ও সাফল্যের স্বীকৃতি",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val badges = listOf(
                BadgeData(
                    titleBn = "ধারাবাহিক তুখোর",
                    descBn = "টানা ৩ দিনের স্ট্রিক অর্জন",
                    icon = Icons.Default.LocalFireDepartment,
                    unlocked = streakDays >= 3,
                    color = Color(0xFFEA580C)
                ),
                BadgeData(
                    titleBn = "নামতা বিজয়ী",
                    descBn = "১০টি নামতা মুখস্থ সম্পন্ন",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    unlocked = tablesLearned >= 10,
                    color = Color(0xFF2563EB)
                ),
                BadgeData(
                    titleBn = "কুইজ জিনিয়াস",
                    descBn = "কুইজে ১০০% পূর্ণ নম্বর",
                    icon = Icons.Default.Star,
                    unlocked = perfectCount >= 1,
                    color = Color(0xFFF59E0B)
                ),
                BadgeData(
                    titleBn = "সূত্র মাস্টার",
                    descBn = "৫টি বীজগণিত সূত্র আয়ত্ত",
                    icon = Icons.Default.Functions,
                    unlocked = formulasLearned >= 5,
                    color = Color(0xFF7C3AED)
                ),
                BadgeData(
                    titleBn = "গণিত গতি",
                    descBn = "২৫টি সঠিক কুইজ উত্তর",
                    icon = Icons.Default.School,
                    unlocked = totalCorrect >= 25,
                    color = Color(0xFF10B981)
                ),
                BadgeData(
                    titleBn = "পূর্ণাঙ্গ পণ্ডিত",
                    descBn = "সব ৪০টি নামতা আয়ত্ত",
                    icon = Icons.Default.EmojiEvents,
                    unlocked = tablesLearned >= 40,
                    color = Color(0xFFE11D48)
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                badges.chunked(2).forEach { rowBadges ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowBadges.forEach { badge ->
                            BadgeItemView(badge = badge, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

data class BadgeData(
    val titleBn: String,
    val descBn: String,
    val icon: ImageVector,
    val unlocked: Boolean,
    val color: Color
)

@Composable
fun BadgeItemView(
    badge: BadgeData,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (badge.unlocked) badge.color.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = if (badge.unlocked) androidx.compose.foundation.BorderStroke(1.dp, badge.color.copy(alpha = 0.4f)) else null,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (badge.unlocked) badge.color else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (badge.unlocked) badge.icon else Icons.Default.Check,
                    contentDescription = null,
                    tint = if (badge.unlocked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = badge.titleBn,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = if (badge.unlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = 1
                )
                Text(
                    text = if (badge.unlocked) "আনলকড!" else badge.descBn,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (badge.unlocked) badge.color else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * 7. Quick Action CTA
 */
@Composable
fun DashboardActionRow(
    onStartQuiz: () -> Unit,
    onStartTutor: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dashboard_action_row"),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onStartQuiz,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("কুইজ দিন")
        }

        OutlinedButton(
            onClick = onStartTutor,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("অনুশীলন টিউটর")
        }
    }
}
