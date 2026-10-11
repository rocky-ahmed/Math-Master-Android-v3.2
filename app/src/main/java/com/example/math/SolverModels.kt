package com.example.math

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * The 13 required mathematical problem-solving categories for Phase 4.
 */
enum class SolverTopic(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String,
    val primaryColor: Color,
    val secondaryColor: Color
) {
    BASIC_ARITHMETIC(
        id = "basic_arithmetic",
        titleBn = "মৌলিক পাটিগণিত ও সরল (BODMAS)",
        titleEn = "Basic Arithmetic & BODMAS",
        descriptionBn = "যোগ, বিয়োগ, গুণ, ভাগ এবং বন্ধনীযুক্ত জটিল সরল অংকের ধাপভিত্তিক সমাধান",
        primaryColor = Color(0xFF2563EB),
        secondaryColor = Color(0xFFDBEAFE)
    ),
    FRACTIONS_DECIMALS(
        id = "fractions_decimals",
        titleBn = "ভগ্নাংশ ও দশমিক",
        titleEn = "Fractions & Decimals",
        descriptionBn = "সাধারণ ও মিশ্র ভগ্নাংশের যোগ, বিয়োগ, গুণ, ভাগ, সমহর ও দশমিক রূপান্তর",
        primaryColor = Color(0xFF0D9488),
        secondaryColor = Color(0xFFCCFBF1)
    ),
    PERCENTAGE(
        id = "percentage",
        titleBn = "শতকরা (Percentage)",
        titleEn = "Percentage",
        descriptionBn = "শতকরা মান, বৃদ্ধি/হ্রাস, ক্রমাগত শতকরা ও ব্যবহারিক সমস্যা সমাধান",
        primaryColor = Color(0xFF7C3AED),
        secondaryColor = Color(0xFFEDE9FE)
    ),
    RATIO_PROPORTION(
        id = "ratio_proportion",
        titleBn = "অনুপাত ও সমানুপাত",
        titleEn = "Ratio & Proportion",
        descriptionBn = "রাশির আনুপাতিক বণ্টন, সরল অনুপাত, ব্যস্ত অনুপাত ও সমানুপাতী নির্ণয়",
        primaryColor = Color(0xFFEA580C),
        secondaryColor = Color(0xFFFFEDD5)
    ),
    PROFIT_LOSS(
        id = "profit_loss",
        titleBn = "লাভ ও ক্ষতি (Profit & Loss)",
        titleEn = "Profit & Loss",
        descriptionBn = "ক্রয়মূল্য, বিক্রয়মূল্য, শতকরা লাভ/ক্ষতি, ধার্যমূল্য ও ছাড়ের হিসাব",
        primaryColor = Color(0xFF059669),
        secondaryColor = Color(0xFFD1FAE5)
    ),
    AVERAGE(
        id = "average",
        titleBn = "গড় (Average)",
        titleEn = "Average",
        descriptionBn = "একাধিক সংখ্যার গড়, সমষ্টি, গড় পরিবর্তন ও অজানা রাশির মান নির্ণয়",
        primaryColor = Color(0xFF0284C7),
        secondaryColor = Color(0xFFE0F2FE)
    ),
    SIMPLE_INTEREST(
        id = "simple_interest",
        titleBn = "সরল সুদ ও মুনাফা (Simple Interest)",
        titleEn = "Simple Interest",
        descriptionBn = "মূলধন, বার্ষিক হার, সময় ও মুনাফা-আসল সংক্রান্ত যেকোনো অজ্ঞাত রাশি নির্ণয়",
        primaryColor = Color(0xFFD97706),
        secondaryColor = Color(0xFFFEF3C7)
    ),
    TIME_AND_WORK(
        id = "time_work",
        titleBn = "সময় ও কাজ (Time & Work)",
        titleEn = "Time & Work",
        descriptionBn = "যৌথ কাজের হিসাব, দৈনিক কার্যক্ষমতা ও চৌবাচ্চা-নল সংক্রান্ত অংক",
        primaryColor = Color(0xFF4F46E5),
        secondaryColor = Color(0xFFE0E7FF)
    ),
    SPEED_DISTANCE_TIME(
        id = "speed_distance_time",
        titleBn = "গতিবেগ, দূরত্ব ও সময়",
        titleEn = "Speed, Distance & Time",
        descriptionBn = "গতি, অতিক্রান্ত দূরত্ব, সময়, km/h থেকে m/s রূপান্তর ও ট্রেন সমস্যা",
        primaryColor = Color(0xFFE11D48),
        secondaryColor = Color(0xFFFFE4E6)
    ),
    NUMBER_SYSTEM_DIVISIBILITY(
        id = "number_divisibility",
        titleBn = "সংখ্যা পদ্ধতি ও বিভাজ্যতা",
        titleEn = "Number System & Divisibility",
        descriptionBn = "২ থেকে ১১ দ্বারা বিভাজ্যতার শর্ত, নিঃশেষে বিভাজ্য যাচাই ও ভাগশেষ নির্ণয়",
        primaryColor = Color(0xFF0891B2),
        secondaryColor = Color(0xFFCFFAFE)
    ),
    HCF_LCM(
        id = "hcf_lcm",
        titleBn = "গ.সা.গু ও ল.সা.গু (HCF & LCM)",
        titleEn = "HCF & LCM",
        descriptionBn = "মৌলিক উৎপাদক ও ইউক্লিডীয় প্রক্রিয়ায় গ.সা.গু ও ল.সা.গু নির্ণয় ও সম্পর্ক",
        primaryColor = Color(0xFF9333EA),
        secondaryColor = Color(0xFFF3E8FF)
    ),
    SQUARES_CUBES_ROOTS(
        id = "squares_cubes",
        titleBn = "বর্গ, বর্গমূল, ঘন ও ঘনমূল",
        titleEn = "Squares, Cubes & Roots",
        descriptionBn = "বর্গ, ঘন, মৌলিক উৎপাদকে ও ভাগ প্রক্রিয়ায় বর্গমূল ও ঘনমূলের নিখুঁত মান",
        primaryColor = Color(0xFFB45309),
        secondaryColor = Color(0xFFFDE68A)
    ),
    LINEAR_EQUATIONS(
        id = "linear_equations",
        titleBn = "সরল সমীকরণ ও বীজগণিত",
        titleEn = "Linear Equations & Algebra",
        descriptionBn = "এক চলকবিশিষ্ট সরল সমীকরণ সমাধান, পক্ষান্তর, মান নির্ণয় ও শুদ্ধি পরীক্ষা",
        primaryColor = Color(0xFFBE185D),
        secondaryColor = Color(0xFFFCE7F3)
    );

    fun getIcon(): ImageVector = when (this) {
        BASIC_ARITHMETIC -> Icons.Default.Calculate
        FRACTIONS_DECIMALS -> Icons.Default.PieChart
        PERCENTAGE -> Icons.Default.Percent
        RATIO_PROPORTION -> Icons.Default.SwapHoriz
        PROFIT_LOSS -> Icons.Default.MonetizationOn
        AVERAGE -> Icons.Default.Equalizer
        SIMPLE_INTEREST -> Icons.AutoMirrored.Filled.TrendingUp
        TIME_AND_WORK -> Icons.Default.Work
        SPEED_DISTANCE_TIME -> Icons.Default.Speed
        NUMBER_SYSTEM_DIVISIBILITY -> Icons.Default.Numbers
        HCF_LCM -> Icons.Default.ViewInAr
        SQUARES_CUBES_ROOTS -> Icons.Default.Straighten
        LINEAR_EQUATIONS -> Icons.Default.Functions
    }
}

/**
 * Three distinct educational learning modes.
 */
enum class SolverMode(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String
) {
    LEARNING(
        id = "learning",
        titleBn = "লার্নিং মোড",
        titleEn = "Learning Mode",
        descriptionBn = "প্রতিটি হিসাবের বিশদ বাংলা ব্যাখ্যা, কেন এই ধাপ করা হলো এবং শুদ্ধি পরীক্ষা"
    ),
    QUICK(
        id = "quick",
        titleBn = "কুইক সলিউশন",
        titleEn = "Quick Solution",
        descriptionBn = "সংক্ষিপ্ত হিসাবের ধাপ ও দ্রুত ফলাফল সরাসরি দেখার মোড"
    ),
    BCS(
        id = "bcs",
        titleBn = "বিসিএস প্রস্তুতি",
        titleEn = "BCS Practice",
        descriptionBn = "বিসিএস ও চাকরির পরীক্ষার শর্টকাট টেকনিক, সময় সাশ্রয়ী কৌশল ও গুরুত্বপূর্ণ টিপস"
    )
}

/**
 * Represents an individual sequential step within a solution.
 */
data class SolutionStep(
    val stepIndex: Int,
    val titleBn: String,
    val expressionOrMathBn: String,
    val explanationBn: String,
    val whyThisStepBn: String? = null
)

/**
 * Complete result output of the Math Problem Solver.
 */
data class SolverResult(
    val topic: SolverTopic,
    val mode: SolverMode,
    val originalProblemBn: String,
    val formulaUsedBn: String? = null,
    val steps: List<SolutionStep> = emptyList(),
    val finalAnswerBn: String,
    val verificationBn: String? = null,
    val bcsShortcutBn: String? = null,
    val bcsExamTipBn: String? = null,
    val similarProblemPromptBn: String? = null,
    val isSuccess: Boolean = true,
    val errorMessageBn: String? = null
)

/**
 * Sample / Curated problem model for user quick-start chips.
 */
data class CuratedProblemExample(
    val labelBn: String,
    val topic: SolverTopic,
    val problemDescriptionBn: String,
    val defaultInputs: Map<String, String>
)
