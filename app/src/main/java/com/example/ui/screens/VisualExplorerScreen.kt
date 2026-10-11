package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.BengaliNumberFormatter
import com.example.math.GeoShapeType
import com.example.math.VisualAlgebraResult
import com.example.math.VisualMathEngine
import com.example.ui.MathViewModel
import com.example.ui.components.MathTopAppBar
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Explorer Sections
 */
enum class VisualExplorerSection(val titleBn: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    GEOMETRY("জ্যামিতি এক্সপ্লোরার", Icons.Default.SquareFoot),
    ALGEBRA("ভিজ্যুয়াল বীজগণিত", Icons.Default.Balance),
    NUMBER_LINE("সংখ্যা রেখা ও ভগ্নাংশ", Icons.Default.LinearScale),
    FORMULAS("সূত্র ভিজ্যুয়ালাইজার", Icons.Default.Functions)
}

@Composable
fun VisualExplorerScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    var selectedSection by remember { mutableStateOf(VisualExplorerSection.GEOMETRY) }

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = "ভিজ্যুয়াল ম্যাথ এক্সপ্লোরার",
            subtitle = "বাস্তব ডায়াগ্রাম, স্কেল এবং ইন্টারঅ্যাক্টিভ গণিত",
            onBackClick = { viewModel.navigateBack() }
        )

        // Navigation Tabs for 4 Modules
        ScrollableTabRow(
            selectedTabIndex = selectedSection.ordinal,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth().testTag("visual_explorer_tabs")
        ) {
            VisualExplorerSection.entries.forEach { section ->
                Tab(
                    selected = selectedSection == section,
                    onClick = { selectedSection = section },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = section.icon, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(text = section.titleBn, fontWeight = FontWeight.SemiBold)
                        }
                    },
                    modifier = Modifier.testTag("tab_${section.name.lowercase()}")
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        // Main Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedSection) {
                VisualExplorerSection.GEOMETRY -> GeometryExplorerTab()
                VisualExplorerSection.ALGEBRA -> VisualAlgebraTab()
                VisualExplorerSection.NUMBER_LINE -> NumberLineTab()
                VisualExplorerSection.FORMULAS -> VisualFormulaTab()
            }
        }
    }
}

// =============================================================================
// A. GEOMETRY EXPLORER TAB
// =============================================================================

@Composable
fun GeometryExplorerTab() {
    var selectedShape by remember { mutableStateOf(GeoShapeType.RECTANGLE) }
    
    // Sliders / Parameters State
    var baseParam by remember { mutableDoubleStateOf(10.0) } // base or length or side or radius
    var heightParam by remember { mutableDoubleStateOf(6.0) } // height or width

    val calcResult = remember(selectedShape, baseParam, heightParam) {
        when (selectedShape) {
            GeoShapeType.TRIANGLE -> VisualMathEngine.calculateTriangle(baseParam, heightParam)
            GeoShapeType.RECTANGLE -> VisualMathEngine.calculateRectangle(baseParam, heightParam)
            GeoShapeType.SQUARE -> VisualMathEngine.calculateSquare(baseParam)
            GeoShapeType.CIRCLE -> VisualMathEngine.calculateCircle(baseParam)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Shape selector chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GeoShapeType.entries.forEach { shape ->
                    FilterChip(
                        selected = selectedShape == shape,
                        onClick = {
                            selectedShape = shape
                            if (shape == GeoShapeType.SQUARE) baseParam = 8.0
                            if (shape == GeoShapeType.CIRCLE) baseParam = 7.0
                        },
                        label = { Text(shape.titleBn.split(" ")[0]) },
                        leadingIcon = if (selectedShape == shape) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        modifier = Modifier.testTag("shape_chip_${shape.id}")
                    )
                }
            }
        }

        // Native Diagram Canvas
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("geometry_canvas_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = selectedShape.titleBn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = selectedShape.formulaSummaryBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val primaryColor = MaterialTheme.colorScheme.primary
                    val secondaryColor = MaterialTheme.colorScheme.secondary
                    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .testTag("geometry_canvas")
                    ) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height
                        val cx = canvasWidth / 2f
                        val cy = canvasHeight / 2f

                        when (selectedShape) {
                            GeoShapeType.RECTANGLE -> {
                                val maxDrawW = canvasWidth * 0.7f
                                val maxDrawH = canvasHeight * 0.65f
                                val normBase = (baseParam / 25.0).coerceIn(0.2, 1.0)
                                val normHeight = (heightParam / 25.0).coerceIn(0.2, 1.0)
                                val rectW = (maxDrawW * normBase).toFloat()
                                val rectH = (maxDrawH * normHeight).toFloat()
                                val topLeft = Offset(cx - rectW / 2f, cy - rectH / 2f)

                                // Fill
                                drawRect(
                                    color = primaryColor.copy(alpha = 0.2f),
                                    topLeft = topLeft,
                                    size = Size(rectW, rectH)
                                )
                                // Stroke
                                drawRect(
                                    color = primaryColor,
                                    topLeft = topLeft,
                                    size = Size(rectW, rectH),
                                    style = Stroke(width = 4.dp.toPx())
                                )
                                // Dimension text using native canvas
                                drawContext.canvas.nativeCanvas.apply {
                                    val paint = android.graphics.Paint().apply {
                                        color = android.graphics.Color.DKGRAY
                                        textSize = 34f
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                    }
                                    // Length top label
                                    drawText(
                                        "দৈর্ঘ্য a = ${VisualMathEngine.formatNumberBn(baseParam)}",
                                        cx,
                                        topLeft.y - 14f,
                                        paint
                                    )
                                    // Width right label
                                    val sidePaint = android.graphics.Paint().apply {
                                        color = android.graphics.Color.DKGRAY
                                        textSize = 32f
                                        textAlign = android.graphics.Paint.Align.LEFT
                                        isAntiAlias = true
                                    }
                                    drawText(
                                        "b = ${VisualMathEngine.formatNumberBn(heightParam)}",
                                        topLeft.x + rectW + 12f,
                                        cy,
                                        sidePaint
                                    )
                                }
                            }
                            GeoShapeType.SQUARE -> {
                                val maxSidePx = min(canvasWidth * 0.6f, canvasHeight * 0.7f)
                                val normSide = (baseParam / 25.0).coerceIn(0.25, 1.0)
                                val sidePx = (maxSidePx * normSide).toFloat()
                                val topLeft = Offset(cx - sidePx / 2f, cy - sidePx / 2f)

                                drawRect(
                                    color = secondaryColor.copy(alpha = 0.2f),
                                    topLeft = topLeft,
                                    size = Size(sidePx, sidePx)
                                )
                                drawRect(
                                    color = secondaryColor,
                                    topLeft = topLeft,
                                    size = Size(sidePx, sidePx),
                                    style = Stroke(width = 4.dp.toPx())
                                )
                                // Diagonal dash line
                                drawLine(
                                    color = primaryColor.copy(alpha = 0.6f),
                                    start = topLeft,
                                    end = Offset(topLeft.x + sidePx, topLeft.y + sidePx),
                                    strokeWidth = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                )

                                drawContext.canvas.nativeCanvas.apply {
                                    val paint = android.graphics.Paint().apply {
                                        color = android.graphics.Color.DKGRAY
                                        textSize = 34f
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                    }
                                    drawText("বাহু a = ${VisualMathEngine.formatNumberBn(baseParam)}", cx, topLeft.y - 14f, paint)
                                }
                            }
                            GeoShapeType.TRIANGLE -> {
                                val maxW = canvasWidth * 0.7f
                                val maxH = canvasHeight * 0.7f
                                val normB = (baseParam / 25.0).coerceIn(0.25, 1.0)
                                val normH = (heightParam / 25.0).coerceIn(0.25, 1.0)
                                val bPx = (maxW * normB).toFloat()
                                val hPx = (maxH * normH).toFloat()

                                val pBottomLeft = Offset(cx - bPx / 2f, cy + hPx / 2f)
                                val pBottomRight = Offset(cx + bPx / 2f, cy + hPx / 2f)
                                val pTop = Offset(cx, cy - hPx / 2f)

                                val path = Path().apply {
                                    moveTo(pBottomLeft.x, pBottomLeft.y)
                                    lineTo(pBottomRight.x, pBottomRight.y)
                                    lineTo(pTop.x, pTop.y)
                                    close()
                                }

                                drawPath(path, color = primaryColor.copy(alpha = 0.2f))
                                drawPath(path, color = primaryColor, style = Stroke(width = 4.dp.toPx()))

                                // Altitude dashed line
                                drawLine(
                                    color = secondaryColor,
                                    start = pTop,
                                    end = Offset(cx, pBottomLeft.y),
                                    strokeWidth = 3.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                                )

                                drawContext.canvas.nativeCanvas.apply {
                                    val paint = android.graphics.Paint().apply {
                                        color = android.graphics.Color.DKGRAY
                                        textSize = 32f
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                    }
                                    drawText("ভূমি b = ${VisualMathEngine.formatNumberBn(baseParam)}", cx, pBottomLeft.y + 36f, paint)
                                    drawText("উচ্চতা h = ${VisualMathEngine.formatNumberBn(heightParam)}", cx + 50f, cy, paint)
                                }
                            }
                            GeoShapeType.CIRCLE -> {
                                val maxRadiusPx = min(canvasWidth, canvasHeight) * 0.35f
                                val normR = (baseParam / 30.0).coerceIn(0.25, 1.0)
                                val rPx = (maxRadiusPx * normR).toFloat()

                                drawCircle(
                                    color = primaryColor.copy(alpha = 0.2f),
                                    radius = rPx,
                                    center = Offset(cx, cy)
                                )
                                drawCircle(
                                    color = primaryColor,
                                    radius = rPx,
                                    center = Offset(cx, cy),
                                    style = Stroke(width = 4.dp.toPx())
                                )
                                // Radius line from center
                                drawLine(
                                    color = secondaryColor,
                                    start = Offset(cx, cy),
                                    end = Offset(cx + rPx, cy),
                                    strokeWidth = 3.dp.toPx()
                                )
                                drawCircle(
                                    color = secondaryColor,
                                    radius = 5.dp.toPx(),
                                    center = Offset(cx, cy)
                                )

                                drawContext.canvas.nativeCanvas.apply {
                                    val paint = android.graphics.Paint().apply {
                                        color = android.graphics.Color.DKGRAY
                                        textSize = 34f
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                    }
                                    drawText("r = ${VisualMathEngine.formatNumberBn(baseParam)}", cx + rPx / 2f, cy - 14f, paint)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sliders to change dimensions
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "পরিমাপ পরিবর্তন ও সমন্বয় (Sliders)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val p1Label = when (selectedShape) {
                        GeoShapeType.RECTANGLE -> "দৈর্ঘ্য (Length a)"
                        GeoShapeType.SQUARE -> "বাহু (Side a)"
                        GeoShapeType.TRIANGLE -> "ভূমি (Base b)"
                        GeoShapeType.CIRCLE -> "ব্যাসার্ধ (Radius r)"
                    }
                    val maxVal1 = if (selectedShape == GeoShapeType.CIRCLE) 30f else 25f

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = p1Label, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${VisualMathEngine.formatNumberBn(baseParam)} একক",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Slider(
                        value = baseParam.toFloat(),
                        onValueChange = { baseParam = it.toDouble().coerceIn(1.0, maxVal1.toDouble()) },
                        valueRange = 1f..maxVal1,
                        steps = 29,
                        modifier = Modifier.testTag("slider_base_param")
                    )

                    if (selectedShape == GeoShapeType.RECTANGLE || selectedShape == GeoShapeType.TRIANGLE) {
                        val p2Label = if (selectedShape == GeoShapeType.RECTANGLE) "প্রস্থ (Width b)" else "উচ্চতা (Height h)"
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = p2Label, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "${VisualMathEngine.formatNumberBn(heightParam)} একক",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Slider(
                            value = heightParam.toFloat(),
                            onValueChange = { heightParam = it.toDouble().coerceIn(1.0, 25.0) },
                            valueRange = 1f..25f,
                            steps = 24,
                            modifier = Modifier.testTag("slider_height_param")
                        )
                    }
                }
            }
        }

        // Calculation Results & Explanations in Bengali
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("geometry_result_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "পরিমাপের ফলাফল ও বাংলা বিশ্লেষণ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "ক্ষেত্রফল (Area)", style = MaterialTheme.typography.labelMedium)
                            Text(
                                text = "${VisualMathEngine.formatNumberBn(calcResult.primaryArea)} বর্গ একক",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val perimLabel = if (selectedShape == GeoShapeType.CIRCLE) "পরিধি (Circumference)" else "পরিসীমা (Perimeter)"
                            Text(text = perimLabel, style = MaterialTheme.typography.labelMedium)
                            if (selectedShape == GeoShapeType.TRIANGLE && !calcResult.isPerimeterExact) {
                                Text(
                                    text = "অপর্যাপ্ত উপাত্ত",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = "(বাকি ২ বাহু প্রয়োজন)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Text(
                                    text = "${VisualMathEngine.formatNumberBn(calcResult.primaryPerimeterOrCircumference)} একক",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "ধাপে ধাপে সমাধান প্রক্রিয়া:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    calcResult.explanationStepsBn.forEachIndexed { idx, step ->
                        Text(
                            text = "${BengaliNumberFormatter.toBn(idx + 1)}. $step",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

// =============================================================================
// B. VISUAL ALGEBRA (Balance-Scale Linear Equations)
// =============================================================================

@Composable
fun VisualAlgebraTab() {
    var aParam by remember { mutableDoubleStateOf(2.0) } // coeff of x
    var bParam by remember { mutableDoubleStateOf(4.0) } // constant b
    var cParam by remember { mutableDoubleStateOf(14.0) } // RHS c
    var activeStepIdx by remember { mutableIntStateOf(0) }

    val algebraResult = remember(aParam, bParam, cParam) {
        VisualMathEngine.solveVisualAlgebra(aParam, bParam, cParam)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Equation preset buttons
            Text(
                text = "অনুশীলনের সমীকরণ নির্বাচন করুন:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val presets = listOf(
                    Triple(2.0, 4.0, 14.0), // 2x + 4 = 14
                    Triple(3.0, 6.0, 21.0), // 3x + 6 = 21
                    Triple(4.0, -2.0, 14.0), // 4x - 2 = 14
                    Triple(5.0, 10.0, 35.0)  // 5x + 10 = 35
                )
                presets.forEach { (a, b, c) ->
                    val signStr = if (b >= 0) "+ ${VisualMathEngine.formatNumberBn(b)}" else "- ${VisualMathEngine.formatNumberBn(abs(b))}"
                    val label = "${VisualMathEngine.formatNumberBn(a)}x $signStr = ${VisualMathEngine.formatNumberBn(c)}"
                    val isSelected = aParam == a && bParam == b && cParam == c

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            aParam = a
                            bParam = b
                            cParam = c
                            activeStepIdx = 0
                        },
                        label = { Text(label, fontSize = 12.sp) },
                        modifier = Modifier.testTag("preset_${a.toInt()}_${b.toInt()}")
                    )
                }
            }
        }

        // Interactive Balance Scale Diagram Canvas
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("algebra_balance_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "দাঁড়িপাল্লায় সমীকরণ ভারসাম্য (Balance Scale)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "উভয় পক্ষ সবসময় সমান ও ভারসাম্যপূর্ণ থাকে",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val primColor = MaterialTheme.colorScheme.primary
                    val secColor = MaterialTheme.colorScheme.secondary

                    val currentStep = algebraResult.steps.getOrNull(activeStepIdx) ?: algebraResult.steps.firstOrNull()

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .testTag("balance_scale_canvas")
                    ) {
                        val w = size.width
                        val h = size.height
                        val cx = w / 2f
                        val fulcrumY = h * 0.45f
                        val baseScaleY = h * 0.88f

                        // Draw Stand and Fulcrum (Center Base)
                        val standPath = Path().apply {
                            moveTo(cx, fulcrumY)
                            lineTo(cx - 30f, baseScaleY)
                            lineTo(cx + 30f, baseScaleY)
                            close()
                        }
                        drawPath(standPath, color = Color.Gray.copy(alpha = 0.5f))
                        drawCircle(color = primColor, radius = 12f, center = Offset(cx, fulcrumY))

                        // Draw Balance Beam (Horizontal line when balanced)
                        val beamHalfLen = w * 0.36f
                        val leftBeamEnd = Offset(cx - beamHalfLen, fulcrumY)
                        val rightBeamEnd = Offset(cx + beamHalfLen, fulcrumY)

                        drawLine(
                            color = primColor,
                            start = leftBeamEnd,
                            end = rightBeamEnd,
                            strokeWidth = 6.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // Draw left and right hanging strings & pans
                        val panH = 24f
                        val panW = 100f
                        val stringLen = 55f

                        val leftPanCenter = Offset(leftBeamEnd.x, leftBeamEnd.y + stringLen)
                        val rightPanCenter = Offset(rightBeamEnd.x, rightBeamEnd.y + stringLen)

                        // Strings
                        drawLine(Color.Gray, leftBeamEnd, Offset(leftPanCenter.x - panW / 2, leftPanCenter.y), 2.dp.toPx())
                        drawLine(Color.Gray, leftBeamEnd, Offset(leftPanCenter.x + panW / 2, leftPanCenter.y), 2.dp.toPx())
                        drawLine(Color.Gray, rightBeamEnd, Offset(rightPanCenter.x - panW / 2, rightPanCenter.y), 2.dp.toPx())
                        drawLine(Color.Gray, rightBeamEnd, Offset(rightPanCenter.x + panW / 2, rightPanCenter.y), 2.dp.toPx())

                        // Pans
                        drawArc(
                            color = primColor.copy(alpha = 0.3f),
                            startAngle = 0f,
                            sweepAngle = 180f,
                            useCenter = true,
                            topLeft = Offset(leftPanCenter.x - panW / 2, leftPanCenter.y - panH),
                            size = Size(panW, panH * 2)
                        )
                        drawArc(
                            color = secColor.copy(alpha = 0.3f),
                            startAngle = 0f,
                            sweepAngle = 180f,
                            useCenter = true,
                            topLeft = Offset(rightPanCenter.x - panW / 2, rightPanCenter.y - panH),
                            size = Size(panW, panH * 2)
                        )

                        // Draw Content Labels onto the pans
                        drawContext.canvas.nativeCanvas.apply {
                            val textPaint = android.graphics.Paint().apply {
                                color = android.graphics.Color.BLACK
                                textSize = 32f
                                textAlign = android.graphics.Paint.Align.CENTER
                                isFakeBoldText = true
                                isAntiAlias = true
                            }

                            val leftTxt = currentStep?.leftExprBn ?: "LHS"
                            val rightTxt = currentStep?.rightExprBn ?: "RHS"

                            drawText(leftTxt, leftPanCenter.x, leftPanCenter.y - 10f, textPaint)
                            drawText(rightTxt, rightPanCenter.x, rightPanCenter.y - 10f, textPaint)

                            // Equal sign in the middle
                            val eqPaint = android.graphics.Paint().apply {
                                color = android.graphics.Color.DKGRAY
                                textSize = 42f
                                textAlign = android.graphics.Paint.Align.CENTER
                                isFakeBoldText = true
                            }
                            drawText("=", cx, fulcrumY - 20f, eqPaint)
                        }
                    }
                }
            }
        }

        // Stepper Navigation for Algebraic Transformation
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("algebra_steps_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "রূপান্তরের ধাপসমূহ (Algebraic Transformations)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    algebraResult.steps.forEachIndexed { idx, step ->
                        val isCurrent = idx == activeStepIdx
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { activeStepIdx = idx }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "ধাপ ${BengaliNumberFormatter.toBn(step.stepNumber)}:",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${step.leftExprBn} = ${step.rightExprBn}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = step.explanationBn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Final Answer and Substitution Verification
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("algebra_verification_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "চূড়ান্ত সমাধান ও মান যাচাইকরণ (Substitution)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "x এর মান = ${VisualMathEngine.formatNumberBn(algebraResult.solutionX)}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = algebraResult.verificationBn,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

// =============================================================================
// C. NUMBER LINE & FRACTIONS TAB
// =============================================================================

@Composable
fun NumberLineTab() {
    var numerator by remember { mutableIntStateOf(3) }
    var denominator by remember { mutableIntStateOf(4) }
    var activeValue by remember { mutableFloatStateOf(0.75f) } // decimal point on number line (-5 to +5)

    val fractionInfo = remember(numerator, denominator) {
        VisualMathEngine.analyzeFraction(numerator, denominator)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ইন্টারেক্টিভ সংখ্যা রেখা (Interactive Number Line)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "রেখার বিন্দু টেনে বা স্লাইডার নাড়িয়ে ধনাত্মক ও ঋণাত্মক অবস্থান দেখুন",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Native Number Line Canvas
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("number_line_canvas_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val currentVal = activeValue
                    val lineMin = -5f
                    val lineMax = 5f

                    val primColor = MaterialTheme.colorScheme.primary
                    val errorColor = MaterialTheme.colorScheme.error

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    val newX = change.position.x
                                    val margin = 35f
                                    val usableW = size.width - (2 * margin)
                                    val frac = ((newX - margin) / usableW).coerceIn(0f, 1f)
                                    val computedVal = lineMin + frac * (lineMax - lineMin)
                                    activeValue = (computedVal * 100).toInt() / 100f
                                }
                            }
                            .testTag("number_line_canvas")
                    ) {
                        val w = size.width
                        val h = size.height
                        val cy = h / 2f
                        val margin = 35f
                        val usableW = w - (2 * margin)

                        // Main Horizontal Line with arrows
                        drawLine(
                            color = Color.DarkGray,
                            start = Offset(margin - 15f, cy),
                            end = Offset(w - margin + 15f, cy),
                            strokeWidth = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )

                        // Tick marks from -5 to +5
                        val stepCount = 10
                        for (i in 0..stepCount) {
                            val v = lineMin + i * 1f
                            val tickX = margin + (i / stepCount.toFloat()) * usableW
                            val isZero = v == 0f

                            val tickH = if (isZero) 36f else 20f
                            val tickColor = if (isZero) primColor else Color.Gray

                            drawLine(
                                color = tickColor,
                                start = Offset(tickX, cy - tickH / 2f),
                                end = Offset(tickX, cy + tickH / 2f),
                                strokeWidth = if (isZero) 3.dp.toPx() else 1.5.dp.toPx()
                            )

                            // Labels
                            drawContext.canvas.nativeCanvas.apply {
                                val paint = android.graphics.Paint().apply {
                                    color = if (isZero) android.graphics.Color.BLACK else android.graphics.Color.DKGRAY
                                    textSize = if (isZero) 32f else 26f
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    isFakeBoldText = isZero
                                    isAntiAlias = true
                                }
                                val labelBn = BengaliNumberFormatter.toBn(v.toInt().toString())
                                drawText(labelBn, tickX, cy + 42f, paint)
                            }
                        }

                        // Active Selected Value Pointer
                        val normVal = ((currentVal - lineMin) / (lineMax - lineMin)).coerceIn(0f, 1f)
                        val pointX = margin + normVal * usableW

                        // Pulse Ring
                        drawCircle(
                            color = primColor.copy(alpha = 0.3f),
                            radius = 16.dp.toPx(),
                            center = Offset(pointX, cy)
                        )
                        drawCircle(
                            color = primColor,
                            radius = 8.dp.toPx(),
                            center = Offset(pointX, cy)
                        )

                        // Pointer Label atop
                        drawContext.canvas.nativeCanvas.apply {
                            val tipPaint = android.graphics.Paint().apply {
                                color = android.graphics.Color.BLUE
                                textSize = 32f
                                textAlign = android.graphics.Paint.Align.CENTER
                                isFakeBoldText = true
                                isAntiAlias = true
                            }
                            val dispVal = VisualMathEngine.formatNumberBn(currentVal.toDouble(), 2)
                            drawText(dispVal, pointX, cy - 30f, tipPaint)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Number line slider
                    Slider(
                        value = activeValue,
                        onValueChange = { activeValue = (it * 100).toInt() / 100f },
                        valueRange = -5f..5f,
                        modifier = Modifier.fillMaxWidth().testTag("slider_number_line")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "ঋণাত্মক সীমা: -৫", style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = "বর্তমান বিন্দু: ${VisualMathEngine.formatNumberBn(activeValue.toDouble(), 2)}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = "ধনাত্মক সীমা: +৫", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        // Visual Fraction Pie & Bar Representation
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("fraction_visual_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ভগ্নাংশ ভিজ্যুয়ালাইজেশন (Fractions & Decimals)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Fraction Pie and Bar Canvas
                    val num = numerator
                    val den = max(1, denominator)
                    val primColor = MaterialTheme.colorScheme.primary

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            .testTag("fraction_canvas")
                    ) {
                        val w = size.width
                        val h = size.height
                        val cx = w * 0.3f
                        val cy = h / 2f
                        val r = min(w * 0.22f, h * 0.42f)

                        // 1. Draw Fraction Circle Pie
                        drawCircle(
                            color = Color.LightGray.copy(alpha = 0.5f),
                            radius = r,
                            center = Offset(cx, cy)
                        )
                        val anglePerSlice = 360f / den
                        val safeNum = min(num, den)

                        for (i in 0 until den) {
                            val startAngle = -90f + i * anglePerSlice
                            val isFilled = i < safeNum
                            drawArc(
                                color = if (isFilled) primColor else Color.Transparent,
                                startAngle = startAngle,
                                sweepAngle = anglePerSlice,
                                useCenter = true,
                                topLeft = Offset(cx - r, cy - r),
                                size = Size(r * 2, r * 2)
                            )
                        }
                        // Pie outline slices
                        for (i in 0 until den) {
                            val angleRad = Math.toRadians((-90.0 + i * anglePerSlice))
                            val edgeX = (cx + r * cos(angleRad)).toFloat()
                            val edgeY = (cy + r * sin(angleRad)).toFloat()
                            drawLine(Color.White, Offset(cx, cy), Offset(edgeX, edgeY), 2.dp.toPx())
                        }
                        drawCircle(color = primColor, radius = r, center = Offset(cx, cy), style = Stroke(2.dp.toPx()))

                        // 2. Draw Fraction Bar Chart on right side
                        val barLeft = w * 0.6f
                        val barW = w * 0.32f
                        val barH = 34f
                        val barTop = cy - barH / 2f

                        drawRect(
                            color = Color.LightGray.copy(alpha = 0.5f),
                            topLeft = Offset(barLeft, barTop),
                            size = Size(barW, barH)
                        )
                        val cellW = barW / den
                        for (i in 0 until safeNum) {
                            drawRect(
                                color = primColor,
                                topLeft = Offset(barLeft + i * cellW, barTop),
                                size = Size(cellW, barH)
                            )
                        }
                        // Grid dividers
                        for (i in 1 until den) {
                            drawLine(
                                Color.White,
                                Offset(barLeft + i * cellW, barTop),
                                Offset(barLeft + i * cellW, barTop + barH),
                                2.dp.toPx()
                            )
                        }
                        drawRect(
                            color = primColor,
                            topLeft = Offset(barLeft, barTop),
                            size = Size(barW, barH),
                            style = Stroke(2.dp.toPx())
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Controls for Numerator & Denominator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "লব (Numerator): ${BengaliNumberFormatter.toBn(numerator)}", fontWeight = FontWeight.Bold)
                            Row {
                                IconButton(
                                    onClick = { if (numerator > 1) numerator-- },
                                    modifier = Modifier.testTag("btn_num_dec")
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "কমান")
                                }
                                IconButton(
                                    onClick = { if (numerator < 12) numerator++ },
                                    modifier = Modifier.testTag("btn_num_inc")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "বাড়ান")
                                }
                            }
                        }

                        Column {
                            Text(text = "হর (Denominator): ${BengaliNumberFormatter.toBn(denominator)}", fontWeight = FontWeight.Bold)
                            Row {
                                IconButton(
                                    onClick = { if (denominator > 1) denominator-- },
                                    modifier = Modifier.testTag("btn_den_dec")
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "কমান")
                                }
                                IconButton(
                                    onClick = { if (denominator < 12) denominator++ },
                                    modifier = Modifier.testTag("btn_den_inc")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "বাড়ান")
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                // Match number line position to fraction
                                activeValue = fractionInfo.decimalValue.toFloat()
                            },
                            modifier = Modifier.testTag("btn_sync_fraction_to_line")
                        ) {
                            Text("রেখায় দেখান")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Explanation in Bengali
                    Text(
                        text = fractionInfo.fractionTypeBn,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = fractionInfo.comparisonExplanationBn,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

// =============================================================================
// D. FORMULA VISUALIZER TAB
// =============================================================================

@Composable
fun VisualFormulaTab() {
    val formulas = VisualMathEngine.formulaVisualizerList
    var selectedFormulaId by remember { mutableStateOf(formulas.first().id) }
    val currentFormula = formulas.find { it.id == selectedFormulaId } ?: formulas.first()

    var p1 by remember(currentFormula) { mutableDoubleStateOf(currentFormula.param1Default) }
    var p2 by remember(currentFormula) { mutableDoubleStateOf(currentFormula.param2Default) }
    var p3 by remember(currentFormula) { mutableDoubleStateOf(currentFormula.param3Default) }

    val result = remember(currentFormula, p1, p2, p3) {
        currentFormula.calculate(p1, p2, p3)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "সূত্র নির্বাচন করুন (Choose Formula):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Formula preset chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                formulas.forEach { f ->
                    FilterChip(
                        selected = selectedFormulaId == f.id,
                        onClick = {
                            selectedFormulaId = f.id
                        },
                        label = { Text(f.titleBn.split(" ")[0], fontSize = 12.sp) },
                        modifier = Modifier.testTag("formula_chip_${f.id}")
                    )
                }
            }
        }

        // Formula Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("formula_header_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentFormula.titleBn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = currentFormula.categoryBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentFormula.formulaExpression,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Native Visual Canvas for the Formula
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("formula_diagram_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ভিজ্যুয়াল ডায়াগ্রাম (${result.diagramNotesBn})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val primColor = MaterialTheme.colorScheme.primary
                    val secColor = MaterialTheme.colorScheme.secondary

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .testTag("formula_canvas")
                    ) {
                        val w = size.width
                        val h = size.height
                        val cx = w / 2f
                        val cy = h / 2f

                        when (currentFormula.id) {
                            "vis_pythagoras" -> {
                                val aVal = p1.toFloat()
                                val bVal = p2.toFloat()
                                val maxSide = max(aVal, bVal)
                                val scale = (min(w * 0.6f, h * 0.7f) / max(maxSide, 10f)).coerceIn(5f, 20f)

                                val triW = bVal * scale
                                val triH = aVal * scale
                                val pBottomLeft = Offset(cx - triW / 2f, cy + triH / 2f)
                                val pBottomRight = Offset(cx + triW / 2f, cy + triH / 2f)
                                val pTopLeft = Offset(cx - triW / 2f, cy - triH / 2f)

                                val path = Path().apply {
                                    moveTo(pBottomLeft.x, pBottomLeft.y)
                                    lineTo(pBottomRight.x, pBottomRight.y)
                                    lineTo(pTopLeft.x, pTopLeft.y)
                                    close()
                                }
                                drawPath(path, color = primColor.copy(alpha = 0.25f))
                                drawPath(path, color = primColor, style = Stroke(width = 4.dp.toPx()))

                                // Hypotenuse highlight
                                drawLine(secColor, pBottomRight, pTopLeft, 4.dp.toPx())

                                drawContext.canvas.nativeCanvas.apply {
                                    val paint = android.graphics.Paint().apply {
                                        color = android.graphics.Color.DKGRAY
                                        textSize = 30f
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                    }
                                    drawText("ভূমি b = ${VisualMathEngine.formatNumberBn(p2)}", cx, pBottomLeft.y + 30f, paint)
                                    drawText("লম্ব a = ${VisualMathEngine.formatNumberBn(p1)}", pBottomLeft.x - 40f, cy, paint)
                                    drawText("c = ${VisualMathEngine.formatNumberBn(result.outputValue)}", cx + 20f, cy - 10f, paint)
                                }
                            }
                            "vis_simple_interest" -> {
                                // Draw Principal vs Interest comparative bars
                                val pVal = p1.toFloat()
                                val iVal = result.outputValue.toFloat()
                                val total = pVal + iVal

                                val barW = 60.dp.toPx()
                                val maxBarH = h * 0.65f

                                val pBarH = (pVal / total) * maxBarH
                                val iBarH = (iVal / total) * maxBarH

                                val pX = cx - barW - 20f
                                val iX = cx + 20f
                                val baseY = cy + maxBarH / 2f

                                // Principal bar
                                drawRect(
                                    color = primColor,
                                    topLeft = Offset(pX, baseY - pBarH),
                                    size = Size(barW, pBarH)
                                )
                                // Interest bar
                                drawRect(
                                    color = secColor,
                                    topLeft = Offset(iX, baseY - iBarH),
                                    size = Size(barW, iBarH)
                                )

                                drawContext.canvas.nativeCanvas.apply {
                                    val paint = android.graphics.Paint().apply {
                                        color = android.graphics.Color.DKGRAY
                                        textSize = 28f
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                    }
                                    drawText("মূলধন P", pX + barW / 2f, baseY + 28f, paint)
                                    drawText("সুদ I", iX + barW / 2f, baseY + 28f, paint)
                                    drawText(VisualMathEngine.formatNumberBn(p1), pX + barW / 2f, baseY - pBarH - 10f, paint)
                                    drawText(VisualMathEngine.formatNumberBn(result.outputValue), iX + barW / 2f, baseY - iBarH - 10f, paint)
                                }
                            }
                            else -> {
                                // Default Geometry or Cube wireframe preview
                                drawCircle(
                                    color = primColor.copy(alpha = 0.2f),
                                    radius = min(w, h) * 0.35f,
                                    center = Offset(cx, cy)
                                )
                                drawCircle(
                                    color = primColor,
                                    radius = min(w, h) * 0.35f,
                                    center = Offset(cx, cy),
                                    style = Stroke(3.dp.toPx())
                                )
                                drawContext.canvas.nativeCanvas.apply {
                                    val paint = android.graphics.Paint().apply {
                                        color = android.graphics.Color.DKGRAY
                                        textSize = 32f
                                        textAlign = android.graphics.Paint.Align.CENTER
                                        isAntiAlias = true
                                    }
                                    drawText(result.outputLabelBn + ": " + VisualMathEngine.formatNumberBn(result.outputValue), cx, cy, paint)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Parameter Sliders
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("formula_sliders_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "চলকের মান পরিবর্তন করুন",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Param 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = currentFormula.param1NameBn)
                        Text(text = VisualMathEngine.formatNumberBn(p1), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Slider(
                        value = p1.toFloat(),
                        onValueChange = { p1 = it.toDouble() },
                        valueRange = currentFormula.param1Min.toFloat()..currentFormula.param1Max.toFloat(),
                        modifier = Modifier.testTag("formula_slider_p1")
                    )

                    // Param 2 if present
                    if (currentFormula.param2NameBn != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = currentFormula.param2NameBn)
                            Text(text = VisualMathEngine.formatNumberBn(p2), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        }
                        Slider(
                            value = p2.toFloat(),
                            onValueChange = { p2 = it.toDouble() },
                            valueRange = currentFormula.param2Min.toFloat()..currentFormula.param2Max.toFloat(),
                            modifier = Modifier.testTag("formula_slider_p2")
                        )
                    }

                    // Param 3 if present
                    if (currentFormula.param3NameBn != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = currentFormula.param3NameBn)
                            Text(text = VisualMathEngine.formatNumberBn(p3), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                        }
                        Slider(
                            value = p3.toFloat(),
                            onValueChange = { p3 = it.toDouble() },
                            valueRange = currentFormula.param3Min.toFloat()..currentFormula.param3Max.toFloat(),
                            modifier = Modifier.testTag("formula_slider_p3")
                        )
                    }
                }
            }
        }

        // Calculation Steps and Result in Bengali
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("formula_calculation_result_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ফলাফল: ${result.outputLabelBn}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = VisualMathEngine.formatNumberBn(result.outputValue),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "ধাপে ধাপে হিসাব প্রক্রিয়া:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    result.calculationStepsBn.forEachIndexed { idx, step ->
                        Text(
                            text = "${BengaliNumberFormatter.toBn(idx + 1)}. $step",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
