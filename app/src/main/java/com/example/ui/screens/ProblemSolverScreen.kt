package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookmarkEntity
import com.example.math.BengaliNumberFormatter
import com.example.math.MathProblemSolverEngine
import com.example.math.SolutionStep
import com.example.math.SolverMode
import com.example.math.SolverResult
import com.example.math.SolverTopic
import com.example.ui.MathViewModel
import com.example.ui.components.MathTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemSolverScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookmarks by viewModel.bookmarks.collectAsState()

    BackHandler {
        viewModel.navigateBack()
    }

    var selectedTopic by remember { mutableStateOf(SolverTopic.BASIC_ARITHMETIC) }
    var selectedMode by remember { mutableStateOf(SolverMode.LEARNING) }

    // Dynamic Topic Inputs
    // Arithmetic
    var arithmeticExpr by remember { mutableStateOf("(২৫ + ১৫) × ২ - ৩০ ÷ ৩") }

    // Fractions
    var fractionNum1 by remember { mutableStateOf("৩") }
    var fractionDen1 by remember { mutableStateOf("৪") }
    var fractionOp by remember { mutableStateOf("+") }
    var fractionNum2 by remember { mutableStateOf("২") }
    var fractionDen2 by remember { mutableStateOf("৫") }

    // Percentage
    var percentageSubType by remember { mutableIntStateOf(0) }
    var percentVal1 by remember { mutableStateOf("১৫") }
    var percentVal2 by remember { mutableStateOf("৫০০") }

    // Ratio & Proportion
    var ratioSubType by remember { mutableIntStateOf(0) }
    var ratioTotal by remember { mutableStateOf("১২০০") }
    var ratioR1 by remember { mutableStateOf("৩") }
    var ratioR2 by remember { mutableStateOf("৫") }
    var ratioR3 by remember { mutableStateOf("") }

    // Profit & Loss
    var profitLossSubType by remember { mutableIntStateOf(0) }
    var plCp by remember { mutableStateOf("৫০০") }
    var plSp by remember { mutableStateOf("৫৫০") }
    var plPercent by remember { mutableStateOf("১০") }
    var plMp by remember { mutableStateOf("৬০০") }

    // Average
    var averageInput by remember { mutableStateOf("১০, ২০, ৩০, ৪০, ৫০") }

    // Simple Interest
    var siP by remember { mutableStateOf("৫০০০") }
    var siR by remember { mutableStateOf("১০") }
    var siT by remember { mutableStateOf("৩") }
    var siI by remember { mutableStateOf("") }

    // Time & Work
    var workSubType by remember { mutableIntStateOf(0) }
    var workTimeA by remember { mutableStateOf("১০") }
    var workTimeB by remember { mutableStateOf("১৫") }
    var workTimeC by remember { mutableStateOf("১২") }

    // Speed, Distance, Time
    var sdtSpeed by remember { mutableStateOf("") }
    var sdtDistance by remember { mutableStateOf("১০০") }
    var sdtTime by remember { mutableStateOf("২") }

    // Number System & Divisibility
    var divNumber by remember { mutableStateOf("৭৫২১") }
    var divDivisor by remember { mutableIntStateOf(3) }

    // HCF & LCM
    var hcfLcmInput by remember { mutableStateOf("২৪, ৩৬") }

    // Squares, Cubes, Roots
    var sqCubeNumber by remember { mutableStateOf("১৪৪") }
    var sqCubeOp by remember { mutableIntStateOf(1) } // 0: Square, 1: Sqrt, 2: Cube, 3: Cbrt

    // Linear Equation
    var linearEqInput by remember { mutableStateOf("৩x + ১২ = ৪৫") }

    // Result State
    var solverResult by remember { mutableStateOf<SolverResult?>(null) }

    fun solveCurrentTopic() {
        solverResult = try {
            when (selectedTopic) {
                SolverTopic.BASIC_ARITHMETIC -> {
                    MathProblemSolverEngine.solveArithmetic(arithmeticExpr, selectedMode)
                }
                SolverTopic.FRACTIONS_DECIMALS -> {
                    val n1 = parseBnToLong(fractionNum1) ?: 1L
                    val d1 = parseBnToLong(fractionDen1) ?: 1L
                    val n2 = parseBnToLong(fractionNum2) ?: 1L
                    val d2 = parseBnToLong(fractionDen2) ?: 1L
                    MathProblemSolverEngine.solveFraction(n1, d1, fractionOp, n2, d2, selectedMode)
                }
                SolverTopic.PERCENTAGE -> {
                    val v1 = parseBnToDouble(percentVal1) ?: 0.0
                    val v2 = parseBnToDouble(percentVal2) ?: 0.0
                    val type = when (percentageSubType) {
                        0 -> MathProblemSolverEngine.PercentageType.OF_NUMBER
                        1 -> MathProblemSolverEngine.PercentageType.WHAT_PERCENT_OF
                        2 -> MathProblemSolverEngine.PercentageType.INCREASE_DECREASE
                        else -> MathProblemSolverEngine.PercentageType.SUCCESSIVE
                    }
                    MathProblemSolverEngine.solvePercentage(type, v1, v2, selectedMode)
                }
                SolverTopic.RATIO_PROPORTION -> {
                    val tot = parseBnToDouble(ratioTotal) ?: 0.0
                    val r1 = parseBnToDouble(ratioR1) ?: 1.0
                    val r2 = parseBnToDouble(ratioR2) ?: 1.0
                    val r3 = parseBnToDouble(ratioR3)
                    val type = when (ratioSubType) {
                        0 -> MathProblemSolverEngine.RatioType.DIVIDE_QUANTITY
                        1 -> MathProblemSolverEngine.RatioType.FOURTH_PROPORTIONAL
                        else -> MathProblemSolverEngine.RatioType.SIMPLIFY
                    }
                    MathProblemSolverEngine.solveRatio(type, tot, r1, r2, r3, selectedMode)
                }
                SolverTopic.PROFIT_LOSS -> {
                    val cp = parseBnToDouble(plCp)
                    val sp = parseBnToDouble(plSp)
                    val pct = parseBnToDouble(plPercent)
                    val mp = parseBnToDouble(plMp)
                    val type = when (profitLossSubType) {
                        0 -> MathProblemSolverEngine.ProfitLossType.FIND_PROFIT_LOSS_PERCENT
                        1 -> MathProblemSolverEngine.ProfitLossType.FIND_SP_FROM_CP_AND_PERCENT
                        2 -> MathProblemSolverEngine.ProfitLossType.FIND_CP_FROM_SP_AND_PERCENT
                        else -> MathProblemSolverEngine.ProfitLossType.DISCOUNT_MARKED_PRICE
                    }
                    MathProblemSolverEngine.solveProfitLoss(type, cp, sp, pct, mp, selectedMode)
                }
                SolverTopic.AVERAGE -> {
                    val nums = averageInput.split(",", " ", "،")
                        .mapNotNull { parseBnToDouble(it.trim()) }
                    MathProblemSolverEngine.solveAverage(nums, selectedMode)
                }
                SolverTopic.SIMPLE_INTEREST -> {
                    val p = parseBnToDouble(siP)
                    val r = parseBnToDouble(siR)
                    val t = parseBnToDouble(siT)
                    val i = parseBnToDouble(siI)
                    MathProblemSolverEngine.solveSimpleInterest(p, r, t, i, null, selectedMode)
                }
                SolverTopic.TIME_AND_WORK -> {
                    val a = parseBnToDouble(workTimeA) ?: 10.0
                    val b = parseBnToDouble(workTimeB) ?: 15.0
                    val c = parseBnToDouble(workTimeC)
                    val type = when (workSubType) {
                        0 -> MathProblemSolverEngine.WorkType.TWO_PERSONS
                        1 -> MathProblemSolverEngine.WorkType.THREE_PERSONS
                        else -> MathProblemSolverEngine.WorkType.PIPE_CISTERN
                    }
                    MathProblemSolverEngine.solveTimeAndWork(type, a, b, c, selectedMode)
                }
                SolverTopic.SPEED_DISTANCE_TIME -> {
                    val s = parseBnToDouble(sdtSpeed)
                    val d = parseBnToDouble(sdtDistance)
                    val t = parseBnToDouble(sdtTime)
                    MathProblemSolverEngine.solveSpeedDistanceTime(s, d, t, selectedMode)
                }
                SolverTopic.NUMBER_SYSTEM_DIVISIBILITY -> {
                    val num = parseBnToLong(divNumber) ?: 1234L
                    MathProblemSolverEngine.solveDivisibility(num, divDivisor, selectedMode)
                }
                SolverTopic.HCF_LCM -> {
                    val nums = hcfLcmInput.split(",", " ", "،")
                        .mapNotNull { parseBnToLong(it.trim()) }
                    MathProblemSolverEngine.solveHcfLcm(nums, selectedMode)
                }
                SolverTopic.SQUARES_CUBES_ROOTS -> {
                    val n = parseBnToDouble(sqCubeNumber) ?: 16.0
                    val op = when (sqCubeOp) {
                        0 -> MathProblemSolverEngine.SquareCubeOp.SQUARE
                        1 -> MathProblemSolverEngine.SquareCubeOp.SQUARE_ROOT
                        2 -> MathProblemSolverEngine.SquareCubeOp.CUBE
                        else -> MathProblemSolverEngine.SquareCubeOp.CUBE_ROOT
                    }
                    MathProblemSolverEngine.solveSquareCube(n, op, selectedMode)
                }
                SolverTopic.LINEAR_EQUATIONS -> {
                    MathProblemSolverEngine.solveLinearEquation(linearEqInput, selectedMode)
                }
            }
        } catch (e: Exception) {
            SolverResult(
                topic = selectedTopic,
                mode = selectedMode,
                originalProblemBn = "সমস্যা বিশ্লেষণ",
                finalAnswerBn = "ত্রুটি",
                isSuccess = false,
                errorMessageBn = "ইনপুট মান সঠিক নয়। অনুগ্রহ করে সংখ্যা বা রাশি যাচাই করুন।"
            )
        }
    }

    // Auto-solve once initially
    if (solverResult == null) {
        solveCurrentTopic()
    }

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = "গণিত সমাধানকারী",
            subtitle = "ধাপভিত্তিক বাংলা সমাধান • ১৩টি বিষয়",
            onBackClick = { viewModel.navigateBack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("solver_content_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Mode Selector Bar (Learning Mode / Quick Solution / BCS Practice)
            item {
                LearningModeSelector(
                    currentMode = selectedMode,
                    onModeSelected = { mode ->
                        selectedMode = mode
                        solveCurrentTopic()
                    }
                )
            }

            // Topics Carousel / Selector
            item {
                TopicSelectorCarousel(
                    currentTopic = selectedTopic,
                    onTopicSelected = { topic ->
                        selectedTopic = topic
                        solveCurrentTopic()
                    }
                )
            }

            // Topic-Specific Input Card
            item {
                TopicInputCard(
                    topic = selectedTopic,
                    // Arithmetic
                    arithmeticExpr = arithmeticExpr,
                    onArithmeticChange = { arithmeticExpr = it },
                    // Fraction
                    fractionNum1 = fractionNum1,
                    fractionDen1 = fractionDen1,
                    fractionOp = fractionOp,
                    fractionNum2 = fractionNum2,
                    fractionDen2 = fractionDen2,
                    onFractionChange = { n1, d1, op, n2, d2 ->
                        fractionNum1 = n1
                        fractionDen1 = d1
                        fractionOp = op
                        fractionNum2 = n2
                        fractionDen2 = d2
                    },
                    // Percentage
                    percentageSubType = percentageSubType,
                    percentVal1 = percentVal1,
                    percentVal2 = percentVal2,
                    onPercentageChange = { sub, v1, v2 ->
                        percentageSubType = sub
                        percentVal1 = v1
                        percentVal2 = v2
                    },
                    // Ratio
                    ratioSubType = ratioSubType,
                    ratioTotal = ratioTotal,
                    ratioR1 = ratioR1,
                    ratioR2 = ratioR2,
                    ratioR3 = ratioR3,
                    onRatioChange = { sub, tot, r1, r2, r3 ->
                        ratioSubType = sub
                        ratioTotal = tot
                        ratioR1 = r1
                        ratioR2 = r2
                        ratioR3 = r3
                    },
                    // Profit & Loss
                    profitLossSubType = profitLossSubType,
                    plCp = plCp,
                    plSp = plSp,
                    plPercent = plPercent,
                    plMp = plMp,
                    onProfitLossChange = { sub, cp, sp, pct, mp ->
                        profitLossSubType = sub
                        plCp = cp
                        plSp = sp
                        plPercent = pct
                        plMp = mp
                    },
                    // Average
                    averageInput = averageInput,
                    onAverageChange = { averageInput = it },
                    // Simple Interest
                    siP = siP,
                    siR = siR,
                    siT = siT,
                    siI = siI,
                    onInterestChange = { p, r, t, i ->
                        siP = p
                        siR = r
                        siT = t
                        siI = i
                    },
                    // Time and Work
                    workSubType = workSubType,
                    workTimeA = workTimeA,
                    workTimeB = workTimeB,
                    workTimeC = workTimeC,
                    onWorkChange = { sub, a, b, c ->
                        workSubType = sub
                        workTimeA = a
                        workTimeB = b
                        workTimeC = c
                    },
                    // Speed, Distance, Time
                    sdtSpeed = sdtSpeed,
                    sdtDistance = sdtDistance,
                    sdtTime = sdtTime,
                    onSdtChange = { s, d, t ->
                        sdtSpeed = s
                        sdtDistance = d
                        sdtTime = t
                    },
                    // Divisibility
                    divNumber = divNumber,
                    divDivisor = divDivisor,
                    onDivChange = { n, d ->
                        divNumber = n
                        divDivisor = d
                    },
                    // HCF & LCM
                    hcfLcmInput = hcfLcmInput,
                    onHcfLcmChange = { hcfLcmInput = it },
                    // Squares & Cubes
                    sqCubeNumber = sqCubeNumber,
                    sqCubeOp = sqCubeOp,
                    onSqCubeChange = { n, op ->
                        sqCubeNumber = n
                        sqCubeOp = op
                    },
                    // Linear Equations
                    linearEqInput = linearEqInput,
                    onLinearEqChange = { linearEqInput = it },
                    // Solve trigger
                    onSolveClick = { solveCurrentTopic() }
                )
            }

            // Solution Output Card
            val result = solverResult
            if (result != null) {
                item {
                    SolutionDisplayCard(
                        result = result,
                        isBookmarked = bookmarks.any { it.id == "solver_${result.topic.id}_${result.originalProblemBn.hashCode()}" },
                        onToggleBookmark = {
                            val bookmarkId = "solver_${result.topic.id}_${result.originalProblemBn.hashCode()}"
                            val bookmark = BookmarkEntity(
                                id = bookmarkId,
                                category = "SOLVER",
                                titleBn = result.topic.titleBn,
                                subtitleBn = result.originalProblemBn,
                                detailBn = result.finalAnswerBn
                            )
                            viewModel.toggleBookmark(bookmark)
                            Toast.makeText(
                                context,
                                if (bookmarks.any { it.id == bookmarkId }) "বুকমার্ক মুছে ফেলা হয়েছে" else "বুকমার্কে সংরক্ষিত হয়েছে",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onCopySolution = {
                            val sb = StringBuilder()
                            sb.append("সমস্যা: ${result.originalProblemBn}\n")
                            result.formulaUsedBn?.let { sb.append("সূত্র: $it\n") }
                            sb.append("\nধাপসমূহ:\n")
                            result.steps.forEach { s ->
                                sb.append("ধাপ ${BengaliNumberFormatter.toBn(s.stepIndex)}: ${s.titleBn}\n")
                                sb.append("হিসাব: ${s.expressionOrMathBn}\n")
                                sb.append("ব্যাখ্যা: ${s.explanationBn}\n\n")
                            }
                            sb.append("চূড়ান্ত উত্তর: ${result.finalAnswerBn}\n")
                            result.verificationBn?.let { sb.append("$it\n") }
                            result.bcsShortcutBn?.let { sb.append("$it\n") }

                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Math Solution", sb.toString())
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "সম্পূর্ণ সমাধান ক্লিপবোর্ডে কপি হয়েছে!", Toast.LENGTH_SHORT).show()
                        },
                        onTrySimilar = {
                            val inputs = MathProblemSolverEngine.generateSimilarInputs(selectedTopic)
                            when (selectedTopic) {
                                SolverTopic.BASIC_ARITHMETIC -> arithmeticExpr = inputs["expression"] ?: arithmeticExpr
                                SolverTopic.FRACTIONS_DECIMALS -> {
                                    fractionNum1 = BengaliNumberFormatter.toBn(inputs["num1"] ?: "2")
                                    fractionDen1 = BengaliNumberFormatter.toBn(inputs["den1"] ?: "3")
                                    fractionOp = inputs["op"] ?: "+"
                                    fractionNum2 = BengaliNumberFormatter.toBn(inputs["num2"] ?: "1")
                                    fractionDen2 = BengaliNumberFormatter.toBn(inputs["den2"] ?: "4")
                                }
                                SolverTopic.PERCENTAGE -> {
                                    percentVal1 = BengaliNumberFormatter.toBn(inputs["val1"] ?: "15")
                                    percentVal2 = BengaliNumberFormatter.toBn(inputs["val2"] ?: "500")
                                }
                                SolverTopic.RATIO_PROPORTION -> {
                                    ratioTotal = BengaliNumberFormatter.toBn(inputs["total"] ?: "1200")
                                    ratioR1 = BengaliNumberFormatter.toBn(inputs["r1"] ?: "2")
                                    ratioR2 = BengaliNumberFormatter.toBn(inputs["r2"] ?: "3")
                                }
                                SolverTopic.PROFIT_LOSS -> {
                                    plCp = BengaliNumberFormatter.toBn(inputs["cp"] ?: "400")
                                    plSp = BengaliNumberFormatter.toBn(inputs["sp"] ?: "480")
                                }
                                SolverTopic.AVERAGE -> averageInput = inputs["numbers"] ?: averageInput
                                SolverTopic.SIMPLE_INTEREST -> {
                                    siP = BengaliNumberFormatter.toBn(inputs["p"] ?: "5000")
                                    siR = BengaliNumberFormatter.toBn(inputs["r"] ?: "10")
                                    siT = BengaliNumberFormatter.toBn(inputs["t"] ?: "3")
                                    siI = ""
                                }
                                SolverTopic.TIME_AND_WORK -> {
                                    workTimeA = BengaliNumberFormatter.toBn(inputs["timeA"] ?: "10")
                                    workTimeB = BengaliNumberFormatter.toBn(inputs["timeB"] ?: "15")
                                }
                                SolverTopic.SPEED_DISTANCE_TIME -> {
                                    sdtDistance = BengaliNumberFormatter.toBn(inputs["distance"] ?: "120")
                                    sdtTime = BengaliNumberFormatter.toBn(inputs["time"] ?: "2")
                                    sdtSpeed = ""
                                }
                                SolverTopic.NUMBER_SYSTEM_DIVISIBILITY -> {
                                    divNumber = BengaliNumberFormatter.toBn(inputs["number"] ?: "1248")
                                    divDivisor = inputs["divisor"]?.toIntOrNull() ?: 4
                                }
                                SolverTopic.HCF_LCM -> hcfLcmInput = BengaliNumberFormatter.toBn(inputs["numbers"] ?: "24, 36")
                                SolverTopic.SQUARES_CUBES_ROOTS -> {
                                    sqCubeNumber = BengaliNumberFormatter.toBn(inputs["number"] ?: "144")
                                    sqCubeOp = 1
                                }
                                SolverTopic.LINEAR_EQUATIONS -> linearEqInput = BengaliNumberFormatter.toBn(inputs["equation"] ?: "3x + 9 = 27")
                            }
                            solveCurrentTopic()
                            Toast.makeText(context, "অনুরূপ নতুন সমস্যা লোড হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TOPIC SELECTOR CAROUSEL
// -------------------------------------------------------------
@Composable
fun TopicSelectorCarousel(
    currentTopic: SolverTopic,
    onTopicSelected: (SolverTopic) -> Unit
) {
    Column {
        Text(
            text = "গণিত বিষয় নির্বাচন করুন (${BengaliNumberFormatter.toBn(SolverTopic.entries.size)}টি বিষয়)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = Modifier.testTag("solver_topics_row")
        ) {
            items(SolverTopic.entries) { topic ->
                val isSelected = topic == currentTopic
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) topic.primaryColor else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    tonalElevation = if (isSelected) 4.dp else 1.dp,
                    modifier = Modifier
                        .clickable { onTopicSelected(topic) }
                        .testTag("solver_topic_${topic.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = topic.getIcon(),
                            contentDescription = topic.titleBn,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = topic.titleBn,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// LEARNING MODE SELECTOR
// -------------------------------------------------------------
@Composable
fun LearningModeSelector(
    currentMode: SolverMode,
    onModeSelected: (SolverMode) -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        modifier = Modifier.fillMaxWidth().testTag("learning_mode_card")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "শেখার মোড নির্বাচন করুন",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SolverMode.entries.forEach { mode ->
                    val isSelected = mode == currentMode
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onModeSelected(mode) }
                            .testTag("solver_mode_${mode.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (mode) {
                                    SolverMode.LEARNING -> Icons.Default.School
                                    SolverMode.QUICK -> Icons.Default.Speed
                                    SolverMode.BCS -> Icons.Default.AutoAwesome
                                },
                                contentDescription = mode.titleBn,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = mode.titleBn,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = currentMode.descriptionBn,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// -------------------------------------------------------------
// TOPIC DYNAMIC INPUT CARD
// -------------------------------------------------------------
@Composable
fun TopicInputCard(
    topic: SolverTopic,
    // Arithmetic
    arithmeticExpr: String,
    onArithmeticChange: (String) -> Unit,
    // Fraction
    fractionNum1: String,
    fractionDen1: String,
    fractionOp: String,
    fractionNum2: String,
    fractionDen2: String,
    onFractionChange: (String, String, String, String, String) -> Unit,
    // Percentage
    percentageSubType: Int,
    percentVal1: String,
    percentVal2: String,
    onPercentageChange: (Int, String, String) -> Unit,
    // Ratio
    ratioSubType: Int,
    ratioTotal: String,
    ratioR1: String,
    ratioR2: String,
    ratioR3: String,
    onRatioChange: (Int, String, String, String, String) -> Unit,
    // Profit & Loss
    profitLossSubType: Int,
    plCp: String,
    plSp: String,
    plPercent: String,
    plMp: String,
    onProfitLossChange: (Int, String, String, String, String) -> Unit,
    // Average
    averageInput: String,
    onAverageChange: (String) -> Unit,
    // Simple Interest
    siP: String,
    siR: String,
    siT: String,
    siI: String,
    onInterestChange: (String, String, String, String) -> Unit,
    // Time and Work
    workSubType: Int,
    workTimeA: String,
    workTimeB: String,
    workTimeC: String,
    onWorkChange: (Int, String, String, String) -> Unit,
    // Speed Distance Time
    sdtSpeed: String,
    sdtDistance: String,
    sdtTime: String,
    onSdtChange: (String, String, String) -> Unit,
    // Divisibility
    divNumber: String,
    divDivisor: Int,
    onDivChange: (String, Int) -> Unit,
    // HCF & LCM
    hcfLcmInput: String,
    onHcfLcmChange: (String) -> Unit,
    // Squares & Cubes
    sqCubeNumber: String,
    sqCubeOp: Int,
    onSqCubeChange: (String, Int) -> Unit,
    // Linear Equations
    linearEqInput: String,
    onLinearEqChange: (String) -> Unit,
    // Actions
    onSolveClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, topic.primaryColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .testTag("solver_input_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(topic.primaryColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = topic.getIcon(),
                        contentDescription = null,
                        tint = topic.primaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = topic.titleBn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = topic.descriptionBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (topic) {
                SolverTopic.BASIC_ARITHMETIC -> {
                    OutlinedTextField(
                        value = arithmeticExpr,
                        onValueChange = onArithmeticChange,
                        label = { Text("গাণিতিক রাশি লিখুন (BODMAS)") },
                        placeholder = { Text("যেমন: (২৫ + ১৫) × ২ - ৩০ ÷ ৩") },
                        modifier = Modifier.fillMaxWidth().testTag("arithmetic_input_field"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("উদাহরণ রাশি:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf(
                            "(১৫ + ৫) × ৪ ÷ ২ - ৬",
                            "৫০ - (১০ + ৫) × ২ + ১২",
                            "(৪০ ÷ ৮) × ৭ + (১৫ - ৯)",
                            "১০০ - ৪ × (১২ + ৩) + ৮"
                        ).forEach { sample ->
                            OutlinedButton(
                                onClick = { onArithmeticChange(sample) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(sample, fontSize = 11.sp)
                            }
                        }
                    }
                }
                SolverTopic.FRACTIONS_DECIMALS -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = fractionNum1,
                                onValueChange = { onFractionChange(it, fractionDen1, fractionOp, fractionNum2, fractionDen2) },
                                label = { Text("১ম লব") },
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = fractionDen1,
                                onValueChange = { onFractionChange(fractionNum1, it, fractionOp, fractionNum2, fractionDen2) },
                                label = { Text("১ম হর") },
                                singleLine = true
                            )
                        }

                        // Operator selector
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("অপারেটর", fontSize = 11.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf("+", "-", "*", "/").forEach { op ->
                                    val isSelected = fractionOp == op
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) topic.primaryColor else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable {
                                                onFractionChange(fractionNum1, fractionDen1, op, fractionNum2, fractionDen2)
                                            }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = if (op == "*") "×" else if (op == "/") "÷" else op,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = fractionNum2,
                                onValueChange = { onFractionChange(fractionNum1, fractionDen1, fractionOp, it, fractionDen2) },
                                label = { Text("২য় লব") },
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = fractionDen2,
                                onValueChange = { onFractionChange(fractionNum1, fractionDen1, fractionOp, fractionNum2, it) },
                                label = { Text("২য় হর") },
                                singleLine = true
                            )
                        }
                    }
                }
                SolverTopic.PERCENTAGE -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf("X-এর P% কত?", "X, Y-এর কত %?", "শতকরা বৃদ্ধি/হ্রাস", "ক্রমাগত % পরিবর্তন").forEachIndexed { index, label ->
                            FilterChip(
                                selected = percentageSubType == index,
                                onClick = { onPercentageChange(index, percentVal1, percentVal2) },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = percentVal1,
                            onValueChange = { onPercentageChange(percentageSubType, it, percentVal2) },
                            label = {
                                Text(
                                    when (percentageSubType) {
                                        0 -> "শতকরা হার (%)"
                                        1 -> "অংশ মান (X)"
                                        2 -> "আদি মান"
                                        else -> "১ম পরিবর্তন (%)"
                                    }
                                )
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = percentVal2,
                            onValueChange = { onPercentageChange(percentageSubType, percentVal1, it) },
                            label = {
                                Text(
                                    when (percentageSubType) {
                                        0 -> "মূল সংখ্যা"
                                        1 -> "মোট সংখ্যা (Y)"
                                        2 -> "পরিবর্তিত মান"
                                        else -> "২য় পরিবর্তন (%)"
                                    }
                                )
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
                SolverTopic.RATIO_PROPORTION -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("পরিমাণ বণ্টন", "৪র্থ সমানুপাতী", "অনুপাত সরলীকরণ").forEachIndexed { index, label ->
                            FilterChip(
                                selected = ratioSubType == index,
                                onClick = { onRatioChange(index, ratioTotal, ratioR1, ratioR2, ratioR3) },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (ratioSubType == 0) {
                        OutlinedTextField(
                            value = ratioTotal,
                            onValueChange = { onRatioChange(ratioSubType, it, ratioR1, ratioR2, ratioR3) },
                            label = { Text("বণ্টনযোগ্য মোট পরিমাণ") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedTextField(
                                value = ratioR1,
                                onValueChange = { onRatioChange(ratioSubType, ratioTotal, it, ratioR2, ratioR3) },
                                label = { Text("১ম অনুপাত") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = ratioR2,
                                onValueChange = { onRatioChange(ratioSubType, ratioTotal, ratioR1, it, ratioR3) },
                                label = { Text("২য় অনুপাত") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = ratioR3,
                                onValueChange = { onRatioChange(ratioSubType, ratioTotal, ratioR1, ratioR2, it) },
                                label = { Text("৩য় অনুপাত (ঐচ্ছিক)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    } else if (ratioSubType == 1) {
                        Text("A : B = C : ? (চতুর্থ রাশি নির্ণয়)", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedTextField(
                                value = ratioR1,
                                onValueChange = { onRatioChange(ratioSubType, ratioTotal, it, ratioR2, ratioR3) },
                                label = { Text("A") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = ratioR2,
                                onValueChange = { onRatioChange(ratioSubType, ratioTotal, ratioR1, it, ratioR3) },
                                label = { Text("B") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = ratioTotal,
                                onValueChange = { onRatioChange(ratioSubType, it, ratioR1, ratioR2, ratioR3) },
                                label = { Text("C") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedTextField(
                                value = ratioR1,
                                onValueChange = { onRatioChange(ratioSubType, ratioTotal, it, ratioR2, ratioR3) },
                                label = { Text("১ম পদ") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = ratioR2,
                                onValueChange = { onRatioChange(ratioSubType, ratioTotal, ratioR1, it, ratioR3) },
                                label = { Text("২য় পদ") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                }
                SolverTopic.PROFIT_LOSS -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf("লাভ/ক্ষতি %", "বিক্রয়মূল্য নির্ণয়", "ক্রয়মূল্য নির্ণয়", "ধার্যমূল্য ও ছাড়").forEachIndexed { index, label ->
                            FilterChip(
                                selected = profitLossSubType == index,
                                onClick = { onProfitLossChange(index, plCp, plSp, plPercent, plMp) },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when (profitLossSubType) {
                        0 -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = plCp,
                                    onValueChange = { onProfitLossChange(profitLossSubType, it, plSp, plPercent, plMp) },
                                    label = { Text("ক্রয়মূল্য (টাকা)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = plSp,
                                    onValueChange = { onProfitLossChange(profitLossSubType, plCp, it, plPercent, plMp) },
                                    label = { Text("বিক্রয়মূল্য (টাকা)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }
                        1 -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = plCp,
                                    onValueChange = { onProfitLossChange(profitLossSubType, it, plSp, plPercent, plMp) },
                                    label = { Text("ক্রয়মূল্য") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = plPercent,
                                    onValueChange = { onProfitLossChange(profitLossSubType, plCp, plSp, it, plMp) },
                                    label = { Text("লাভ/ক্ষতি % (ক্ষতি হলে -)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }
                        2 -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = plSp,
                                    onValueChange = { onProfitLossChange(profitLossSubType, plCp, it, plPercent, plMp) },
                                    label = { Text("বিক্রয়মূল্য") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = plPercent,
                                    onValueChange = { onProfitLossChange(profitLossSubType, plCp, plSp, it, plMp) },
                                    label = { Text("লাভ/ক্ষতি % (ক্ষতি হলে -)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }
                        else -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = plMp,
                                    onValueChange = { onProfitLossChange(profitLossSubType, plCp, plSp, plPercent, it) },
                                    label = { Text("ধার্যমূল্য (টাকা)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = plPercent,
                                    onValueChange = { onProfitLossChange(profitLossSubType, plCp, plSp, it, plMp) },
                                    label = { Text("ছাড়ের হার (%)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }
                    }
                }
                SolverTopic.AVERAGE -> {
                    OutlinedTextField(
                        value = averageInput,
                        onValueChange = onAverageChange,
                        label = { Text("সংখ্যার তালিকা (কমা দিয়ে লিখুন)") },
                        placeholder = { Text("যেমন: ১২, ১৮, ২৪, ৩০, ৩৬") },
                        modifier = Modifier.fillMaxWidth().testTag("average_input_field"),
                        singleLine = true
                    )
                }
                SolverTopic.SIMPLE_INTEREST -> {
                    Text("P, r, t, I এর মধ্যে যেকোনো ৩টি রাশির মান দিন (বাকিটি স্বয়ংক্রিয় বের হবে):", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = siP,
                            onValueChange = { onInterestChange(it, siR, siT, siI) },
                            label = { Text("আসল (P)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = siR,
                            onValueChange = { onInterestChange(siP, it, siT, siI) },
                            label = { Text("হার % (r)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = siT,
                            onValueChange = { onInterestChange(siP, siR, it, siI) },
                            label = { Text("সময় বছর (t)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = siI,
                            onValueChange = { onInterestChange(siP, siR, siT, it) },
                            label = { Text("মুনাফা (I)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
                SolverTopic.TIME_AND_WORK -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("A ও B এর কাজ", "A, B ও C এর কাজ", "চৌবাচ্চা ও পাইপ").forEachIndexed { index, label ->
                            FilterChip(
                                selected = workSubType == index,
                                onClick = { onWorkChange(index, workTimeA, workTimeB, workTimeC) },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = workTimeA,
                            onValueChange = { onWorkChange(workSubType, it, workTimeB, workTimeC) },
                            label = { Text(if (workSubType == 2) "পূর্ণকারী নল (ঘন্টা)" else "A এর সময় (দিন)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = workTimeB,
                            onValueChange = { onWorkChange(workSubType, workTimeA, it, workTimeC) },
                            label = { Text(if (workSubType == 2) "খালি করার নল (ঘন্টা)" else "B এর সময় (দিন)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        if (workSubType == 1) {
                            OutlinedTextField(
                                value = workTimeC,
                                onValueChange = { onWorkChange(workSubType, workTimeA, workTimeB, it) },
                                label = { Text("C এর সময় (দিন)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                }
                SolverTopic.SPEED_DISTANCE_TIME -> {
                    Text("গতিবেগ (S), দূরত্ব (D) ও সময় (T) এর যেকোনো ২টি রাশির মান দিন:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = sdtSpeed,
                            onValueChange = { onSdtChange(it, sdtDistance, sdtTime) },
                            label = { Text("গতিবেগ (km/h)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = sdtDistance,
                            onValueChange = { onSdtChange(sdtSpeed, it, sdtTime) },
                            label = { Text("দূরত্ব (km)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = sdtTime,
                            onValueChange = { onSdtChange(sdtSpeed, sdtDistance, it) },
                            label = { Text("সময় (ঘন্টা)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
                SolverTopic.NUMBER_SYSTEM_DIVISIBILITY -> {
                    OutlinedTextField(
                        value = divNumber,
                        onValueChange = { onDivChange(it, divDivisor) },
                        label = { Text("পরীক্ষার সংখ্যা") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("ভাজক নির্বাচন করুন:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11).forEach { d ->
                            FilterChip(
                                selected = divDivisor == d,
                                onClick = { onDivChange(divNumber, d) },
                                label = { Text(BengaliNumberFormatter.toBn(d), fontWeight = FontWeight.Bold) }
                            )
                        }
                    }
                }
                SolverTopic.HCF_LCM -> {
                    OutlinedTextField(
                        value = hcfLcmInput,
                        onValueChange = onHcfLcmChange,
                        label = { Text("সংখ্যাদ্বয় বা ত্রয়ী (কমা দিয়ে লিখুন)") },
                        placeholder = { Text("যেমন: ২৪, ৩৬ বা ১৮, ৩০, ৪২") },
                        modifier = Modifier.fillMaxWidth().testTag("hcf_lcm_input_field"),
                        singleLine = true
                    )
                }
                SolverTopic.SQUARES_CUBES_ROOTS -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf("বর্গ (N²)", "বর্গমূল (√N)", "ঘন (N³)", "ঘনমূল (³√N)").forEachIndexed { index, label ->
                            FilterChip(
                                selected = sqCubeOp == index,
                                onClick = { onSqCubeChange(sqCubeNumber, index) },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = sqCubeNumber,
                        onValueChange = { onSqCubeChange(it, sqCubeOp) },
                        label = { Text("সংখ্যা লিখুন") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                SolverTopic.LINEAR_EQUATIONS -> {
                    OutlinedTextField(
                        value = linearEqInput,
                        onValueChange = onLinearEqChange,
                        label = { Text("সরল সমীকরণ (ax + b = cx + d)") },
                        placeholder = { Text("যেমন: 3x + 12 = 45 বা 5x - 4 = 2x + 11") },
                        modifier = Modifier.fillMaxWidth().testTag("linear_eq_input_field"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("নমুনা সমীকরণ:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf("3x + 12 = 45", "5x - 8 = 2x + 16", "4x + 10 = 34", "7x - 5 = 3x + 15").forEach { sample ->
                            OutlinedButton(
                                onClick = { onLinearEqChange(sample) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(sample, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Solve Button
            Button(
                onClick = onSolveClick,
                colors = ButtonDefaults.buttonColors(containerColor = topic.primaryColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("solve_problem_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "সমাধান করুন",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "সমাধান করুন",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// COMPLETE SOLUTION DISPLAY CARD
// -------------------------------------------------------------
@Composable
fun SolutionDisplayCard(
    result: SolverResult,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onCopySolution: () -> Unit,
    onTrySimilar: () -> Unit
) {
    if (!result.isSuccess) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            modifier = Modifier.fillMaxWidth().testTag("solver_error_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = "ত্রুটি",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ইনপুট বা গাণিতিক ত্রুটি",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = result.errorMessageBn ?: "ইনপুট মান সঠিক নয়।",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // 1. Final Answer Hero Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("solver_answer_banner")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "চূড়ান্ত ফলাফল",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "চূড়ান্ত ফলাফল (Final Answer)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Quick Actions
                    Row {
                        IconButton(
                            onClick = onCopySolution,
                            modifier = Modifier.size(36.dp).testTag("solver_copy_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "কপি করুন",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = onToggleBookmark,
                            modifier = Modifier.size(36.dp).testTag("solver_bookmark_btn")
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "বুকমার্ক",
                                tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = result.originalProblemBn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = result.finalAnswerBn,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }

        // 2. Formula Card (If available)
        result.formulaUsedBn?.let { formula ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth().testTag("solver_formula_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Functions,
                        contentDescription = "সূত্র",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ব্যবহৃত সূত্র ও নিয়মাবলী",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formula,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // 3. Step-by-Step Explanation Cards
        if (result.steps.isNotEmpty()) {
            Text(
                text = "ধাপভিত্তিক সমাধান (${BengaliNumberFormatter.toBn(result.steps.size)}টি ধাপ)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            result.steps.forEach { step ->
                StepCardItem(step = step, mode = result.mode)
            }
        }

        // 4. Verification Card (শুদ্ধি পরীক্ষা)
        result.verificationBn?.let { verification ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFECFDF5)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth().testTag("solver_verification_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "শুদ্ধি পরীক্ষা",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(20.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "উত্তরের সত্যতা ও শুদ্ধি পরীক্ষা (Verification)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF065F46)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = verification,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF064E3B)
                        )
                    }
                }
            }
        }

        // 5. BCS Shortcut & Exam Tip Card (If available or in BCS mode)
        result.bcsShortcutBn?.let { shortcut ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFFBEB)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth().testTag("solver_bcs_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "বিসিএস শর্টকাট",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(20.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "বিসিএস ও চাকরির পরীক্ষার বিশেষ শর্টকাট টেকনিক",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = shortcut,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF78350F)
                        )
                        result.bcsExamTipBn?.let { tip ->
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "টিপস: $tip",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }
            }
        }

        // 6. "Try Similar Question" Action Button
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "অনুরূপ প্রশ্ন সমাধান করুন",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "এই নিয়মের আরেকটি প্রশ্ন অনুশীলন করে দক্ষতা যাচাই করুন",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onTrySimilar,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("solver_try_similar_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "অনুরূপ প্রশ্ন",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("অনুরূপ প্রশ্ন", fontSize = 13.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP CARD ITEM
// -------------------------------------------------------------
@Composable
fun StepCardItem(
    step: SolutionStep,
    mode: SolverMode
) {
    ElevatedCard(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = BengaliNumberFormatter.toBn(step.stepIndex),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = step.titleBn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = step.expressionOrMathBn,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = step.explanationBn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // "Why this step?" explanation (Shown in Learning Mode)
            step.whyThisStepBn?.let { why ->
                if (mode == SolverMode.LEARNING) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Help,
                                contentDescription = "কেন এই ধাপ?",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "কেন এই ধাপ: $why",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// HELPER PARSING FUNCTIONS
// -------------------------------------------------------------
fun parseBnToLong(s: String?): Long? {
    if (s.isNullOrBlank()) return null
    val ascii = toAscii(s.trim())
    return ascii.toLongOrNull()
}

/**
 * Normalizes and parses Bengali/ASCII decimal input.
 * Accepts '.' and unambiguous ',' as decimal separators (e.g. "২,৫" -> 2.5, "২.৫" -> 2.5, "-০,৫" -> -0.5).
 * Rejects ambiguous thousands separators such as "1,234" or "1,00,000".
 */
fun parseBnToDouble(s: String?): Double? {
    if (s.isNullOrBlank()) return null
    val raw = s.trim()

    // Normalize Bengali digits and commas, strip spaces
    val ascii = toAscii(raw).replace('،', ',').replace(" ", "")

    val sign = if (ascii.startsWith("-")) "-" else if (ascii.startsWith("+")) "+" else ""
    val body = if (sign.isNotEmpty()) ascii.substring(1) else ascii

    if (body.isEmpty()) return null

    // If body contains both ',' and '.'
    if (body.contains(",") && body.contains(".")) {
        // e.g. 1,234.56 -> standard thousands grouping with dot decimal
        val thousandsDotRegex = Regex("""^\d{1,3}(,\d{3})+(\.\d+)?$""")
        if (thousandsDotRegex.matches(body)) {
            val normalized = body.replace(",", "")
            return "$sign$normalized".toDoubleOrNull()
        }
        // Otherwise conflicting or ambiguous mixed separators (e.g. 1.234,56)
        return null
    }

    // If body contains only comma ',' (no dot)
    if (body.contains(",")) {
        val commaCount = body.count { it == ',' }
        if (commaCount > 1) {
            // Multiple commas like 1,00,000 or 1,234,567 -> ambiguous thousands separators
            return null
        }

        // Exactly one comma: split into left and right
        val parts = body.split(",")
        val left = parts[0]
        val right = parts[1]

        // Reject if right side is empty (e.g. "2,") or contains non-digits
        if (right.isEmpty()) return null
        if ((left.isNotEmpty() && !left.all { it.isDigit() }) || !right.all { it.isDigit() }) {
            return null
        }

        // Ambiguous thousands separator check:
        // If left is non-empty, non-zero and right has exactly 3 digits (e.g. "1,234" or "25,000"),
        // this is ambiguous under international thousands conventions. Reject it.
        if (left.isNotEmpty() && left != "0" && right.length == 3) {
            return null
        }

        // Unambiguous decimal separator with comma (e.g. "2,5" -> 2.5, "0,5" -> 0.5, "2,25" -> 2.25, ",5" -> 0.5)
        val normalized = "${if (left.isEmpty()) "0" else left}.${right}"
        return "$sign$normalized".toDoubleOrNull()
    }

    // If body contains dot '.' or is an integer
    return "$sign$body".toDoubleOrNull()
}

fun toAscii(s: String): String {
    val bnToAscii = mapOf(
        '০' to '0', '১' to '1', '২' to '2', '৩' to '3', '৪' to '4',
        '৫' to '5', '৬' to '6', '৭' to '7', '৮' to '8', '৯' to '9'
    )
    val sb = StringBuilder()
    for (c in s) {
        sb.append(bnToAscii[c] ?: c)
    }
    return sb.toString()
}
