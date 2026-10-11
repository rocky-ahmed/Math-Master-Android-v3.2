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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookmarkEntity
import com.example.math.BengaliNumberFormatter
import com.example.math.DivisibilityRule
import com.example.math.GcdLcmResult
import com.example.math.NumberSystemCategory
import com.example.math.NumberSystemDifficulty
import com.example.math.NumberSystemProvider
import com.example.math.NumberSystemTopic
import com.example.ui.MathViewModel
import com.example.ui.Screen
import com.example.ui.components.MathSectionHeader
import com.example.ui.components.MathTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberSystemScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val bookmarks by viewModel.bookmarks.collectAsState()

    // Tabs: 0 -> বাস্তব সংখ্যা শ্রেণিবিভাগ, 1 -> বিভাজ্যতার নিয়ম (২-১১), 2 -> গ.সা.গু ও ল.সা.গু ক্যালকুলেটর, 3 -> অনুশীলন ল্যাব
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("সংখ্যার শ্রেণিবিভাগ", "বিভাজ্যতার নিয়ম", "গ.সা.গু ও ল.সা.গু", "বিসিএস প্র্যাকটিস")

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = "সংখ্যা পদ্ধতি ও বিভাজ্যতা",
            subtitle = "বাস্তব সংখ্যা, উৎপাদক, ল.সা.গু, গ.সা.গু ও BCS প্রস্তুতি",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                // Shortcut to existing Prime Numbers module
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Primes) },
                    modifier = Modifier.testTag("ns_shortcut_primes_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.LooksOne,
                        contentDescription = "মৌলিক সংখ্যা মডিউল"
                    )
                }
            }
        )

        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.testTag("number_system_tab_row")
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                            ),
                            maxLines = 1
                        )
                    },
                    modifier = Modifier.testTag("ns_tab_$index")
                )
            }
        }

        when (selectedTab) {
            0 -> NumberClassificationTab(viewModel = viewModel, bookmarks = bookmarks)
            1 -> DivisibilityRulesTab(viewModel = viewModel)
            2 -> GcdLcmCalculatorTab(viewModel = viewModel)
            3 -> NumberSystemPracticeHubTab(viewModel = viewModel)
        }
    }
}

/**
 * Tab 1: Real Number Classification (Natural, Whole, Integer, Rational, Irrational, Primes, Factors)
 */
@Composable
fun NumberClassificationTab(
    viewModel: MathViewModel,
    bookmarks: List<BookmarkEntity>
) {
    var testNumberInput by remember { mutableStateOf("") }
    var classificationResult by remember { mutableStateOf<String?>(null) }
    var expandedCategory by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Interactive Live Classifier Box
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("interactive_classifier_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ইন্টারেক্টিভ সংখ্যা পরীক্ষক ও বিশ্লেষক",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "যেকোনো পূর্ণসংখ্যা লিখে জানুন তা কোন কোন শ্রেণির অন্তর্ভুক্ত (স্বাভাবিক, অখণ্ড, জোড়/বিজোড়, মৌলিক/যৌগিক)।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = testNumberInput,
                            onValueChange = {
                                if (it.all { ch -> ch.isDigit() || ch == '-' }) {
                                    testNumberInput = it
                                }
                            },
                            label = { Text("একটি সংখ্যা লিখুন (যেমন: ৪২, -৭, ০)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    val n = testNumberInput.toLongOrNull()
                                    if (n != null) {
                                        classificationResult = NumberSystemProvider.classifyNumber(n)
                                    }
                                }
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("classifier_input_field")
                        )

                        Button(
                            onClick = {
                                val n = testNumberInput.toLongOrNull()
                                if (n != null) {
                                    classificationResult = NumberSystemProvider.classifyNumber(n)
                                }
                            },
                            modifier = Modifier.testTag("classifier_check_btn")
                        ) {
                            Text("যাচাই")
                        }
                    }

                    if (classificationResult != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = classificationResult!!,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            MathSectionHeader(
                title = "বাস্তব সংখ্যার বৃক্ষ ও সংজ্ঞাসমূহ",
                subtitle = "বিসিএস প্রিলিমিনারি ও সকল প্রতিযোগিতামূলক গণিত পরীক্ষার জন্য"
            )
        }

        // List of Real Number categories
        items(NumberSystemCategory.entries) { cat ->
            val isBookmarked = bookmarks.any { it.id == "ns_cat_${cat.id}" }
            val isExpanded = expandedCategory == cat.id

            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("number_cat_card_${cat.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cat.symbol,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = cat.titleBn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = cat.titleEn,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.toggleBookmark(
                                    BookmarkEntity(
                                        id = "ns_cat_${cat.id}",
                                        category = "NUMBER_SYSTEM",
                                        titleBn = cat.titleBn,
                                        subtitleBn = cat.titleEn,
                                        detailBn = cat.definitionBn
                                    )
                                )
                            },
                            modifier = Modifier.testTag("bookmark_ns_cat_${cat.id}")
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "বুকমার্ক",
                                tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        }

                        IconButton(
                            onClick = {
                                expandedCategory = if (isExpanded) null else cat.id
                            }
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded) "সংকোচন" else "প্রসারণ"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = cat.definitionBn,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "উদাহরণ: ${cat.examplesBn}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = cat.bcsNotesBn,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                            // If this is Prime & Composite, offer a direct button to deep-dive into the existing Primes screen
                            if (cat == NumberSystemCategory.PRIME_COMPOSITE) {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { viewModel.navigateTo(Screen.Primes) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("open_primes_module_btn")
                                ) {
                                    Icon(Icons.Default.LooksOne, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("১-২০০ মৌলিক সংখ্যার পূর্ণাঙ্গ চার্ট ও ম্যাজিক কোড দেখুন")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tab 2: Comprehensive Divisibility Rules (2, 3, 4, 5, 6, 8, 9, 10, 11) with Live Tester
 */
@Composable
fun DivisibilityRulesTab(
    viewModel: MathViewModel
) {
    val rules = NumberSystemProvider.divisibilityRules
    var selectedDivisor by remember { mutableIntStateOf(2) }
    var userTestNumber by remember { mutableStateOf("4872") }

    val activeRule = rules.find { it.divisor == selectedDivisor } ?: rules.first()
    val testNumberLong = userTestNumber.toLongOrNull() ?: activeRule.testExampleNumber
    val (isDivisible, dynamicExplanation) = NumberSystemProvider.checkDivisibility(testNumberLong, activeRule.divisor)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Selector Chips for Divisors 2, 3, 4, 5, 6, 8, 9, 10, 11
        item {
            Text(
                text = "ভাজক নির্বাচন করুন:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(rules) { rule ->
                    FilterChip(
                        selected = selectedDivisor == rule.divisor,
                        onClick = {
                            selectedDivisor = rule.divisor
                            userTestNumber = rule.testExampleNumber.toString()
                        },
                        label = {
                            Text("${BengaliNumberFormatter.toBn(rule.divisor)} এর নিয়ম")
                        },
                        modifier = Modifier.testTag("rule_chip_${rule.divisor}")
                    )
                }
            }
        }

        // Active Rule Card
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_divisibility_rule_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "÷ ${BengaliNumberFormatter.toBn(activeRule.divisor)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = activeRule.ruleNameBn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = activeRule.summaryBn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = activeRule.detailedRuleBn,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = activeRule.bcsShortcutBn,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }

        // Live Interactive Divisibility Tester
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("divisibility_tester_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "লাইভ পরীক্ষা ও ব্যাখ্যা যাচাই",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = userTestNumber,
                        onValueChange = {
                            if (it.all { ch -> ch.isDigit() }) userTestNumber = it
                        },
                        label = { Text("যেকোনো সংখ্যা লিখুন (যেমন: $testNumberLong)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("divisibility_input_field")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Verdict Banner
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDivisible) Color(0xFFDCFCE7) else Color(0xFFFFE4E6),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDivisible) Color(0xFF22C55E) else Color(0xFFEF4444)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isDivisible) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isDivisible) Color(0xFF16A34A) else Color(0xFFDC2626),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isDivisible) "নিঃশেষে বিভাজ্য ✓" else "নিঃশেষে বিভাজ্য নয় ✗",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isDivisible) Color(0xFF15803D) else Color(0xFFB91C1C)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = dynamicExplanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 18.sp,
                                    color = Color.Black.copy(alpha = 0.8f)
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
 * Tab 3: Step-by-Step HCF (GCD) and LCM Explanations & Solver
 */
@Composable
fun GcdLcmCalculatorTab(
    viewModel: MathViewModel
) {
    var inputA by remember { mutableStateOf("24") }
    var inputB by remember { mutableStateOf("36") }
    var calculatedResult by remember { mutableStateOf<GcdLcmResult?>(null) }

    // Auto-calculate default on first load
    if (calculatedResult == null) {
        val a = inputA.toLongOrNull() ?: 24L
        val b = inputB.toLongOrNull() ?: 36L
        calculatedResult = NumberSystemProvider.calculateGcdLcm(a, b)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Input Controls
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gcd_lcm_input_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "গ.সা.গু ও ল.সা.গু সমাধানক ও ধাপভিত্তিক ব্যাখ্যা",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "দুটি সংখ্যা ইনপুট দিন; সম্পূর্ণ গুণনীয়ক বিশ্লেষণ, ইউক্লিডীয় পদ্ধতি এবং ল.সা.গু এর ধাপসমূহ দেখুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = inputA,
                            onValueChange = { if (it.all { ch -> ch.isDigit() }) inputA = it },
                            label = { Text("১ম সংখ্যা (A)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("gcd_input_a")
                        )
                        OutlinedTextField(
                            value = inputB,
                            onValueChange = { if (it.all { ch -> ch.isDigit() }) inputB = it },
                            label = { Text("২য় সংখ্যা (B)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("gcd_input_b")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val a = inputA.toLongOrNull() ?: 1L
                            val b = inputB.toLongOrNull() ?: 1L
                            calculatedResult = NumberSystemProvider.calculateGcdLcm(a, b)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("calculate_gcd_lcm_btn")
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ধাপভিত্তিক হিসাব ও ব্যাখ্যা দেখুন")
                    }
                }
            }
        }

        // Summary Badges
        calculatedResult?.let { res ->
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("result_gcd_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "গ.সা.গু (HCF / GCD)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = BengaliNumberFormatter.toBn(res.gcd),
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("result_lcm_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ল.সা.গু (LCM)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (res.lcm <= 0L) "সীমাতিরিক্ত" else BengaliNumberFormatter.toBn(res.lcm),
                                style = if (res.lcm <= 0L) MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            // GCD Steps
            item {
                MathSectionHeader(
                    title = "গরিষ্ঠ সাধারণ গুণনীয়ক (গ.সা.গু) নির্ণয়ের ধাপ",
                    subtitle = "উৎপাদক ও সাধারণ গুণনীয়ক পদ্ধতি"
                )
            }

            items(res.gcdSteps) { step ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "ধাপ ${BengaliNumberFormatter.toBn(step.stepNumber)}: ${step.titleBn}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = step.detailBn,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // LCM Steps
            item {
                MathSectionHeader(
                    title = "লঘিষ্ঠ সাধারণ গুণিতক (ল.সা.গু) নির্ণয়ের ধাপ",
                    subtitle = "মৌলিক উৎপাদক ও সম্পর্কের সূত্র"
                )
            }

            items(res.lcmSteps) { step ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "ধাপ ${BengaliNumberFormatter.toBn(step.stepNumber)}: ${step.titleBn}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = step.detailBn,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Cross-verification formula
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "গাণিতিক সম্পর্ক ও যাচাইকরণ",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = res.formulaCheckBn,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tab 4: Practice Hub with Difficulty Selection (Easy, Medium, Hard) and Subtopics
 */
@Composable
fun NumberSystemPracticeHubTab(
    viewModel: MathViewModel
) {
    var selectedDifficulty by remember { mutableStateOf(NumberSystemDifficulty.EASY) }
    var selectedTopic by remember { mutableStateOf(NumberSystemTopic.ALL) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "বাস্তব সংখ্যা ও বিভাজ্যতা অনুশীলন ল্যাব",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "বিসিএস প্রিলিমিনারি, শিক্ষক নিয়োগ ও ব্যাংক পরীক্ষার স্ট্যান্ডার্ড অনুযায়ী ৩ স্তরের প্রশ্ন অনুশীলন করুন। তাৎক্ষণিক ফলাফল ও ব্যাখ্যা সংরক্ষিত হবে।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Difficulty Selection
        item {
            Text(
                text = "কঠিন্যের স্তর নির্বাচন করুন:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NumberSystemDifficulty.entries.forEach { diff ->
                    FilterChip(
                        selected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = diff },
                        label = { Text(diff.titleBn) },
                        modifier = Modifier.testTag("practice_diff_${diff.id}")
                    )
                }
            }
        }

        // Subtopic Selection
        item {
            Text(
                text = "বিষয় নির্বাচন করুন:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(NumberSystemTopic.entries) { topic ->
                    FilterChip(
                        selected = selectedTopic == topic,
                        onClick = { selectedTopic = topic },
                        label = { Text(topic.titleBn) },
                        modifier = Modifier.testTag("practice_topic_${topic.id}")
                    )
                }
            }
        }

        // Launch Action
        item {
            Button(
                onClick = {
                    viewModel.startNumberSystemPractice(selectedTopic, selectedDifficulty)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_ns_practice_btn"),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "অনুশীলন শুরু করুন (${selectedDifficulty.titleBn})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // BCS Prep Strategy Tips Card
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BCS প্রিলিমিনারি গণিত টিপস",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• বিসিএস প্রিলিমিনারিতে ১-২টি প্রশ্ন সর্বদা বাস্তব সংখ্যা (মূলদ/অমূলদ) ও বিভাজ্যতা থেকে আসে।\n" +
                                "• দুটি ক্রমিক জোড় সংখ্যার গুণফল সর্বদা ৮ দ্বারা বিভাজ্য।\n" +
                                "• π অমূলদ কিন্তু ২২/৭ মূলদ সংখ্যা—এটি বারবার আসা একটি প্রিয় প্রশ্ন।\n" +
                                "• ১১ এর বিভাজ্যতা নির্ণয়ে বিজোড় ও জোড় অবস্থানের অঙ্কগুলোর সমষ্টির অন্তর মনে রাখবেন।",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
