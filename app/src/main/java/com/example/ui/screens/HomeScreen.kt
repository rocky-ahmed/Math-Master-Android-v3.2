package com.example.ui.screens

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
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Tune
import com.example.ui.customization.CustomModuleRenderer
import com.example.ui.customization.HomeModuleItem
import com.example.ui.customization.SectionKey
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.BengaliNumberFormatter
import com.example.ui.MathViewModel
import com.example.ui.Screen
import com.example.ui.components.MathSectionHeader
import com.example.ui.components.MathTopAppBar
import com.example.ui.theme.AppThemeMode

@Composable
fun HomeScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val progressList by viewModel.progressList.collectAsState()
    val quizScores by viewModel.quizScores.collectAsState()
    val customState by viewModel.customizationState.collectAsState()

    var showThemeMenu by remember { mutableStateOf(false) }

    val learnedTablesCount = progressList.count { it.itemId.startsWith("table_") && it.isLearned }
    val learnedPercentage = (learnedTablesCount * 100) / 40

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = "Math Master",
            subtitle = "গণিত শিক্ষা ও অনুশীলনের পূর্ণাঙ্গ মাধ্যম",
            actions = {
                // Progress Dashboard shortcut
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Dashboard) },
                    modifier = Modifier.testTag("home_dashboard_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "অগ্রগতি ড্যাশবোর্ড"
                    )
                }

                // Theme picker menu
                Box {
                    IconButton(
                        onClick = { showThemeMenu = true },
                        modifier = Modifier.testTag("theme_mode_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ColorLens,
                            contentDescription = "থিম পরিবর্তন"
                        )
                    }
                    DropdownMenu(
                        expanded = showThemeMenu,
                        onDismissRequest = { showThemeMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("সিস্টেম ডিফল্ট") },
                            onClick = {
                                viewModel.setThemeMode(AppThemeMode.SYSTEM)
                                showThemeMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("লাইট মোড") },
                            onClick = {
                                viewModel.setThemeMode(AppThemeMode.LIGHT)
                                showThemeMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("ডার্ক মোড") },
                            onClick = {
                                viewModel.setThemeMode(AppThemeMode.DARK)
                                showThemeMenu = false
                            }
                        )
                    }
                }

                // Customize screen shortcut
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Customize) },
                    modifier = Modifier.testTag("home_customize_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "অ্যাপ কাস্টমাইজ করুন"
                    )
                }

                // Bookmark shortcut
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Bookmarks) },
                    modifier = Modifier.testTag("home_bookmarks_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "বুকমার্ক তালিকা"
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Hero Welcome Banner
            item {
                HomeHeroBanner(
                    learnedCount = learnedTablesCount,
                    totalTables = 40,
                    percentage = learnedPercentage,
                    quizCount = quizScores.size,
                    onStartPractice = { viewModel.startTutor(5) },
                    onOpenDashboard = { viewModel.navigateTo(Screen.Dashboard) }
                )
            }

            // Quick Modules Grid / Cards
            item {
                MathSectionHeader(
                    title = "গণিত মডিউলসমূহ",
                    subtitle = "নিচে থেকে আপনার পছন্দের বিষয় নির্বাচন করুন"
                )
            }

            item {
                val homeModules = listOf(
                    HomeModuleItem(
                        sectionKey = SectionKey.TABLES,
                        titleBn = "গুণ ও নামতা (১ থেকে ৪০)",
                        subtitleBn = "৪টি রেঞ্জে বিভক্ত: ১-১০, ১১-২০, ২১-৩০, ৩১-৪০",
                        badgeBn = "৪০ টি নামতা",
                        defaultIcon = Icons.AutoMirrored.Filled.MenuBook,
                        defaultGradient = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)),
                        testTag = "module_tables_card",
                        onClick = { viewModel.navigateTo(Screen.Tables) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.PRIMES,
                        titleBn = "মৌলিক সংখ্যা (১ থেকে ২০০)",
                        subtitleBn = "১-১০০ (২৫টি) ও ১০১-২০০ (২১টি) এর মৌলিক সংখ্যার তালিকা ও বিশ্লেষণ",
                        badgeBn = "৪৬ টি মৌলিক",
                        defaultIcon = Icons.Default.LooksOne,
                        defaultGradient = listOf(Color(0xFF0D9488), Color(0xFF0F766E)),
                        testTag = "module_primes_card",
                        onClick = { viewModel.navigateTo(Screen.Primes) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.SQUARES,
                        titleBn = "বর্গ ও ঘন (১ থেকে ২০)",
                        subtitleBn = "বর্গ, বর্গমূল, ঘন ও ঘনমূলের নির্ভুল মান ও চার্ট",
                        badgeBn = "বর্গমূল ও ঘনমূল",
                        defaultIcon = Icons.Default.Straighten,
                        defaultGradient = listOf(Color(0xFFD97706), Color(0xFFB45309)),
                        testTag = "module_squares_card",
                        onClick = { viewModel.navigateTo(Screen.SquaresCubes) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.FORMULAS,
                        titleBn = "বীজগণিতীয় সূত্রাবলী",
                        subtitleBn = "বর্গ ও ঘনের সূত্র, প্রতিপাদন, উদাহরণ ও অনুসিদ্ধান্ত",
                        badgeBn = "ব্যাখ্যা ও সমাধান",
                        defaultIcon = Icons.Default.Functions,
                        defaultGradient = listOf(Color(0xFF7C3AED), Color(0xFF6D28D9)),
                        testTag = "module_formulas_card",
                        onClick = { viewModel.navigateTo(Screen.Formulas) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.FORMULA_LIBRARY,
                        titleBn = "ফর্মুলা লাইব্রেরি",
                        subtitleBn = "বীজগণিত, জ্যামিতি ও ক্যালকুলাসের পূর্ণাঙ্গ সূত্র ভাণ্ডার",
                        badgeBn = "সব বিষয় ও ক্যাটাগরি",
                        defaultIcon = Icons.Default.School,
                        defaultGradient = listOf(Color(0xFF4F46E5), Color(0xFF4338CA)),
                        testTag = "module_formula_library_card",
                        onClick = { viewModel.navigateTo(Screen.FormulaLibrary) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.PRACTICE_LAB,
                        titleBn = "গণিত প্র্যাকটিস ল্যাব",
                        subtitleBn = "যোগ, বিয়োগ, গুণ, ভাগ, ভগ্নাংশ ও শতকরাসহ ৮টি বিষয়ের অনুশীলন",
                        badgeBn = "৩ স্তর • ৮ বিষয়",
                        defaultIcon = Icons.Default.Psychology,
                        defaultGradient = listOf(Color(0xFF0D9488), Color(0xFF0F766E)),
                        testTag = "module_practice_lab_card",
                        onClick = { viewModel.navigateTo(Screen.PracticeLab) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.MENTAL_MATH,
                        titleBn = "মেন্টাল ম্যাথ চ্যালেঞ্জ",
                        subtitleBn = "টাইমার ও স্পিড রান মোডে দ্রুত হিসাবের তীব্র মানসিক প্রতিযোগিতা",
                        badgeBn = "টাইমার মোড • কম্বো",
                        defaultIcon = Icons.Default.Speed,
                        defaultGradient = listOf(Color(0xFFEA580C), Color(0xFFC2410C)),
                        testTag = "module_mental_math_card",
                        onClick = { viewModel.navigateTo(Screen.MentalMath) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.NUMBER_SYSTEM,
                        titleBn = "বাস্তব সংখ্যা ও বিভাজ্যতার নিয়ম",
                        subtitleBn = "মূলদ/অমূলদ, ২-১১ এর বিভাজ্যতার শর্ত, গ.সা.গু, ল.সা.গু ও BCS প্রশ্ন",
                        badgeBn = "বিসিএস স্পেশাল • ৪ বিভাগ",
                        defaultIcon = Icons.Default.Numbers,
                        defaultGradient = listOf(Color(0xFF0284C7), Color(0xFF0369A1)),
                        testTag = "module_number_system_card",
                        onClick = { viewModel.navigateTo(Screen.NumberSystem) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.PROBLEM_SOLVER,
                        titleBn = "গণিত সমস্যা সমাধানকারী (Problem Solver)",
                        subtitleBn = "পাটিগণিত, ভগ্নাংশ, শতকরা, লাভ-ক্ষতি ও বীজগণিতের বাংলা ব্যাখ্যাসহ সমাধান",
                        badgeBn = "১৩টি বিষয় • ৩ মোড",
                        defaultIcon = Icons.Default.Calculate,
                        defaultGradient = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)),
                        testTag = "module_problem_solver_card",
                        onClick = { viewModel.navigateTo(Screen.ProblemSolver) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.VISUAL_EXPLORER,
                        titleBn = "ভিজ্যুয়াল ম্যাথ এক্সপ্লোরার (Visual Explorer)",
                        subtitleBn = "জ্যামিতিক চিত্র, দাঁড়িপাল্লা বীজগণিত, সংখ্যা রেখা ও সূত্র ভিজ্যুয়ালাইজার",
                        badgeBn = "৪টি ভিজ্যুয়াল বিভাগ",
                        defaultIcon = Icons.Default.Straighten,
                        defaultGradient = listOf(Color(0xFF059669), Color(0xFF047857)),
                        testTag = "module_visual_explorer_card",
                        onClick = { viewModel.navigateTo(Screen.VisualExplorer) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.QUIZZES,
                        titleBn = "ইন্টারেক্টিভ কুইজ পরীক্ষা",
                        subtitleBn = "নামতা, মৌলিক সংখ্যা, বর্গ ও বীজগণিতের এমসিকিউ পরীক্ষা",
                        badgeBn = "স্কোরবোর্ড যুক্ত",
                        defaultIcon = Icons.Default.Quiz,
                        defaultGradient = listOf(Color(0xFFE11D48), Color(0xFFBE123C)),
                        testTag = "module_quizzes_card",
                        onClick = { viewModel.navigateTo(Screen.Quizzes) }
                    ),
                    HomeModuleItem(
                        sectionKey = SectionKey.DASHBOARD,
                        titleBn = "অগ্রগতি ও পরিসংখ্যান ড্যাশবোর্ড",
                        subtitleBn = "দৈনিক স্ট্রিক, কুইজ পারফরম্যান্স ও চার্টভিত্তিক অ্যানালিটিক্স",
                        badgeBn = "চার্ট ও বিশ্লেষণ",
                        defaultIcon = Icons.Default.BarChart,
                        defaultGradient = listOf(Color(0xFF0284C7), Color(0xFF0369A1)),
                        testTag = "module_dashboard_card",
                        onClick = { viewModel.navigateTo(Screen.Dashboard) }
                    )
                )

                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    CustomModuleRenderer(
                        modules = homeModules,
                        customState = customState
                    )
                }
            }

            // Recent Quiz Results or Summary
            if (quizScores.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    MathSectionHeader(
                        title = "সর্বশেষ কুইজ ফলাফল",
                        subtitle = "অফলাইন ডাটাবেসে সংরক্ষিত অগ্রগতি"
                    )
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(quizScores.take(5)) { score ->
                            ElevatedCard(
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(14.dp),
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 6.dp
                                ),
                                modifier = Modifier
                                    .width(180.dp)
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(14.dp),
                                        ambientColor = Color.Black.copy(alpha = 0.08f),
                                        spotColor = Color.Black.copy(alpha = 0.12f)
                                    )
                                    .border(
                                        0.5.dp,
                                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                        RoundedCornerShape(14.dp)
                                    )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = score.topicTitleBn,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "স্কোর: ${BengaliNumberFormatter.toBn(score.score)} / ${BengaliNumberFormatter.toBn(score.totalQuestions)}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "সঠিকতা: ${BengaliNumberFormatter.toBn(score.percentage)}%",
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
}

@Composable
fun HomeHeroBanner(
    learnedCount: Int,
    totalTables: Int,
    percentage: Int,
    quizCount: Int,
    onStartPractice: () -> Unit,
    onOpenDashboard: () -> Unit = {}
) {
        ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            )
            .testTag("hero_banner_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "স্বাগতম Math Master এ!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "সহজে গণিত শিখুন, মুখস্থ করুন এবং নিজেকে যাচাই করুন।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "নামতা শিক্ষা অগ্রগতি:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${BengaliNumberFormatter.toBn(learnedCount)}/${BengaliNumberFormatter.toBn(totalTables)} সম্পন্ন (${BengaliNumberFormatter.toBn(percentage)}%)",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { percentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onStartPractice() }
                        .testTag("tutor_practice_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "অনুশীলন টিউটর",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenDashboard() }
                        .testTag("hero_dashboard_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "অগ্রগতি চার্ট",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModuleFeatureCard(
    titleBn: String,
    subtitleBn: String,
    badgeBn: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    testTag: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = titleBn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitleBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = badgeBn,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
