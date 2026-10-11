package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewCompact
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.math.BengaliNumberFormatter
import com.example.ui.MathViewModel
import com.example.ui.components.MathTopAppBar
import com.example.ui.customization.AvailableIcon
import com.example.ui.customization.ColorUtils
import com.example.ui.customization.CustomLayoutType
import com.example.ui.customization.CustomModuleRenderer
import com.example.ui.customization.CustomFontWeight
import com.example.ui.customization.HomeModuleItem
import com.example.ui.customization.SectionKey
import com.example.ui.theme.AppThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.discardCustomizationPreview()
        viewModel.navigateBack()
    }

    val previewState by viewModel.previewCustomizationState.collectAsState()
    val savedState by viewModel.customizationState.collectAsState()
    val currentThemeMode by viewModel.themeMode.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedSectionKey by remember { mutableStateOf(SectionKey.TABLES) }
    var showResetAllDialog by remember { mutableStateOf(false) }
    var showSavedSnackbar by remember { mutableStateOf(false) }

    val tabs = listOf(
        "লেআউট",
        "টাইপোগ্রাফি",
        "আইকন",
        "সেকশন ভিত্তিক",
        "গ্লোবাল থিম",
        "লাইভ প্রিভিউ"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MathTopAppBar(
            title = "অ্যাপ কাস্টমাইজেশন",
            subtitle = "লেআউট, আইকন, ফন্ট ও রঙের পূর্ণ নিয়ন্ত্রণ",
            onBackClick = {
                viewModel.discardCustomizationPreview()
                viewModel.navigateBack()
            },
            actions = {
                IconButton(
                    onClick = { showResetAllDialog = true },
                    modifier = Modifier.testTag("btn_reset_all")
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "রিসেট অল",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        // Top Action Bar: Save Changes, Live Status & Discard
        Surface(
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.discardCustomizationPreview()
                        viewModel.navigateBack()
                    },
                    modifier = Modifier.testTag("btn_cancel_customization")
                ) {
                    Text("বাতিল / পূর্বাবস্থা", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        viewModel.saveCustomization()
                        showSavedSnackbar = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("btn_save_customization")
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("সংরক্ষণ করুন", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showSavedSnackbar) {
            Surface(
                color = Color(0xFF10B981),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "✓ পরিবর্তনসমূহ সফলভাবে সংরক্ষিত হয়েছে!",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextButton(onClick = { showSavedSnackbar = false }) {
                        Text("ঠিক আছে", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // Category Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("custom_tab_$index")
                )
            }
        }

        // Tab Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTabIndex) {
                0 -> {
                    // Category 1: Layout Customization
                    item {
                        LayoutCustomizationSection(
                            previewState = previewState,
                            onUpdateGlobal = { updater -> viewModel.updatePreviewGlobal(updater) },
                            onResetCategory = { viewModel.resetPreviewCategory("LAYOUT") }
                        )
                    }
                }
                1 -> {
                    // Category 2: Typography
                    item {
                        TypographyCustomizationSection(
                            previewState = previewState,
                            onUpdateGlobal = { updater -> viewModel.updatePreviewGlobal(updater) },
                            onResetCategory = { viewModel.resetPreviewCategory("TYPOGRAPHY") }
                        )
                    }
                }
                2 -> {
                    // Category 3: Icon Customization
                    item {
                        IconCustomizationSection(
                            previewState = previewState,
                            selectedSection = selectedSectionKey,
                            onSelectSection = { selectedSectionKey = it },
                            onUpdateSection = { key, updater -> viewModel.updatePreviewSection(key, updater) },
                            onResetSection = { key -> viewModel.resetPreviewSection(key) }
                        )
                    }
                }
                3 -> {
                    // Category 4: Independent Section Appearance
                    item {
                        SectionAppearanceCustomizationSection(
                            previewState = previewState,
                            selectedSection = selectedSectionKey,
                            onSelectSection = { selectedSectionKey = it },
                            onUpdateSection = { key, updater -> viewModel.updatePreviewSection(key, updater) },
                            onResetSection = { key -> viewModel.resetPreviewSection(key) }
                        )
                    }
                }
                4 -> {
                    // Category 5: Global Theme & Colors
                    item {
                        GlobalThemeCustomizationSection(
                            previewState = previewState,
                            themeMode = currentThemeMode,
                            onSetThemeMode = { viewModel.setThemeMode(it) },
                            onUpdateGlobal = { updater -> viewModel.updatePreviewGlobal(updater) },
                            onResetCategory = { viewModel.resetPreviewCategory("GLOBAL_THEME") }
                        )
                    }
                }
                5 -> {
                    // Category 6: Live Preview
                    item {
                        LivePreviewSection(previewState = previewState)
                    }
                }
            }

            // Always render Live Preview at the bottom of every configuration tab for real-time feedback
            if (selectedTabIndex != 5) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "লাইভ প্রিভিউ (তাৎক্ষণিক প্রতিফলন):",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LivePreviewSection(previewState = previewState)
                }
            }
        }
    }

    if (showResetAllDialog) {
        AlertDialog(
            onDismissRequest = { showResetAllDialog = false },
            title = { Text("সমস্ত কাস্টমাইজেশন রিসেট করবেন?") },
            text = {
                Text(
                    "এটি অ্যাপের সকল কাস্টম লেআউট, রঙ, ফন্ট ও আইকনকে মূল ডিফল্ট অবস্থায় ফিরিয়ে নেবে। " +
                    "আপনার সেভ করা বুকমার্ক ও কুইজের রেকর্ড সম্পূর্ণ নিরাপদ থাকবে।"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAllCustomizations()
                        showResetAllDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("রিসেট করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetAllDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

// ==========================================
// CATEGORY 1: LAYOUT CUSTOMIZATION
// ==========================================
@Composable
fun LayoutCustomizationSection(
    previewState: com.example.ui.customization.AppCustomizationState,
    onUpdateGlobal: ((com.example.ui.customization.GlobalCustomConfig) -> com.example.ui.customization.GlobalCustomConfig) -> Unit,
    onResetCategory: () -> Unit
) {
    val global = previewState.global

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "১. হোম স্ক্রিন লেআউট শৈলী",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onResetCategory) {
                    Text("ডিফল্ট", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Layout Style Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomLayoutType.values().forEach { type ->
                    FilterChip(
                        selected = global.layoutType == type,
                        onClick = { onUpdateGlobal { it.copy(layoutType = type) } },
                        label = { Text(type.displayNameBn.split(" ")[0], fontSize = 11.sp) },
                        modifier = Modifier.testTag("chip_layout_${type.name.lowercase()}")
                    )
                }
            }

            if (global.layoutType == CustomLayoutType.GRID) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "গ্রিড কলাম সংখ্যা: ${BengaliNumberFormatter.toBn(global.gridColumns)} কলাম",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Slider(
                    value = global.gridColumns.toFloat(),
                    onValueChange = { onUpdateGlobal { g -> g.copy(gridColumns = it.toInt()) } },
                    valueRange = 1f..4f,
                    steps = 2,
                    modifier = Modifier.testTag("slider_grid_columns")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Corner Radius Slider
            Text(
                text = "কার্ড কর্নার রাউন্ড (কোণ বাঁকা): ${BengaliNumberFormatter.toBn(global.cornerRadiusDp)} dp",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = global.cornerRadiusDp.toFloat(),
                onValueChange = { onUpdateGlobal { g -> g.copy(cornerRadiusDp = it.toInt()) } },
                valueRange = 4f..32f,
                modifier = Modifier.testTag("slider_corner_radius")
            )

            // Horizontal & Vertical Gaps
            Text(
                text = "কার্ডের মধ্যবর্তী ফাঁকা (Spacing): ${BengaliNumberFormatter.toBn(global.verticalGapDp)} dp",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = global.verticalGapDp.toFloat(),
                onValueChange = { onUpdateGlobal { g -> g.copy(verticalGapDp = it.toInt(), horizontalGapDp = it.toInt()) } },
                valueRange = 4f..24f,
                modifier = Modifier.testTag("slider_card_gap")
            )

            // Content Alignment Center Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "কার্ডের লেখা মাঝে সারিবদ্ধ (Center Align)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = global.contentAlignmentCenter,
                    onCheckedChange = { onUpdateGlobal { g -> g.copy(contentAlignmentCenter = it) } },
                    modifier = Modifier.testTag("switch_center_align")
                )
            }

            // Show Border Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "কার্ডের চারপাশে আউটলাইন বর্ডার",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = global.showBorder,
                    onCheckedChange = { onUpdateGlobal { g -> g.copy(showBorder = it, borderThicknessDp = if (it) 1 else 0) } },
                    modifier = Modifier.testTag("switch_show_border")
                )
            }
        }
    }
}

// ==========================================
// CATEGORY 2: TYPOGRAPHY CUSTOMIZATION
// ==========================================
@Composable
fun TypographyCustomizationSection(
    previewState: com.example.ui.customization.AppCustomizationState,
    onUpdateGlobal: ((com.example.ui.customization.GlobalCustomConfig) -> com.example.ui.customization.GlobalCustomConfig) -> Unit,
    onResetCategory: () -> Unit
) {
    val global = previewState.global

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "২. ফন্ট ও টাইপোগ্রাফি",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onResetCategory) {
                    Text("ডিফল্ট", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Font Weight selection
            Text(
                text = "ফন্টের পুরুত্ব (Font Weight):",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomFontWeight.values().forEach { weight ->
                    FilterChip(
                        selected = global.fontWeight == weight,
                        onClick = { onUpdateGlobal { it.copy(fontWeight = weight) } },
                        label = { Text(weight.displayNameBn.split(" ")[0], fontSize = 12.sp) },
                        modifier = Modifier.testTag("chip_font_weight_${weight.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title Font Size Slider
            Text(
                text = "শিরোনামের আকার (Title Size): ${BengaliNumberFormatter.toBn(global.titleFontSizeSp)} sp",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = global.titleFontSizeSp.toFloat(),
                onValueChange = { onUpdateGlobal { g -> g.copy(titleFontSizeSp = it.toInt()) } },
                valueRange = 13f..24f,
                modifier = Modifier.testTag("slider_title_font_size")
            )

            // Subtitle Font Size Slider
            Text(
                text = "উপশিরোনামের আকার (Subtitle Size): ${BengaliNumberFormatter.toBn(global.subtitleFontSizeSp)} sp",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = global.subtitleFontSizeSp.toFloat(),
                onValueChange = { onUpdateGlobal { g -> g.copy(subtitleFontSizeSp = it.toInt()) } },
                valueRange = 10f..18f,
                modifier = Modifier.testTag("slider_subtitle_font_size")
            )
        }
    }
}

// ==========================================
// CATEGORY 3: ICON CUSTOMIZATION
// ==========================================
@Composable
fun IconCustomizationSection(
    previewState: com.example.ui.customization.AppCustomizationState,
    selectedSection: SectionKey,
    onSelectSection: (SectionKey) -> Unit,
    onUpdateSection: (SectionKey, (com.example.ui.customization.SectionCustomConfig) -> com.example.ui.customization.SectionCustomConfig) -> Unit,
    onResetSection: (SectionKey) -> Unit
) {
    val sectionConfig = previewState.getSectionConfig(selectedSection)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "৩. সেকশন অনুযায়ী আইকন পরিবর্তন",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Section Selector Chips
            Text(text = "সেকশন নির্বাচন করুন:", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SectionKey.values()) { section ->
                    FilterChip(
                        selected = selectedSection == section,
                        onClick = { onSelectSection(section) },
                        label = { Text(section.titleBn.split(" ")[0], fontSize = 11.sp) },
                        modifier = Modifier.testTag("chip_sec_${section.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Current Active Icon & Controls
            val activeIcon = sectionConfig.iconId?.let { AvailableIcon.fromId(it) } ?: selectedSection.defaultIcon

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = activeIcon.toImageVector(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = selectedSection.titleBn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "বর্তমান আইকন: ${activeIcon.displayNameBn}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                TextButton(onClick = { onResetSection(selectedSection) }) {
                    Text("রিসেট", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Icon Size Slider
            Text(
                text = "আইকনের আকার: ${BengaliNumberFormatter.toBn(sectionConfig.iconSizeDp)} dp",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = sectionConfig.iconSizeDp.toFloat(),
                onValueChange = { onUpdateSection(selectedSection) { cfg -> cfg.copy(iconSizeDp = it.toInt()) } },
                valueRange = 20f..44f,
                modifier = Modifier.testTag("slider_icon_size")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Available Icon Grid
            Text(
                text = "নতুন আইকন বেছে নিন:",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(AvailableIcon.values()) { iconChoice ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (activeIcon == iconChoice) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (activeIcon == iconChoice) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                onUpdateSection(selectedSection) { cfg -> cfg.copy(iconId = iconChoice.id) }
                            }
                            .testTag("icon_pick_${iconChoice.id}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = iconChoice.toImageVector(),
                                contentDescription = iconChoice.displayNameBn,
                                modifier = Modifier.size(22.dp),
                                tint = if (activeIcon == iconChoice) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// CATEGORY 4: SECTION INDEPENDENT CUSTOMIZATION
// ==========================================
@Composable
fun SectionAppearanceCustomizationSection(
    previewState: com.example.ui.customization.AppCustomizationState,
    selectedSection: SectionKey,
    onSelectSection: (SectionKey) -> Unit,
    onUpdateSection: (SectionKey, (com.example.ui.customization.SectionCustomConfig) -> com.example.ui.customization.SectionCustomConfig) -> Unit,
    onResetSection: (SectionKey) -> Unit
) {
    val sectionConfig = previewState.getSectionConfig(selectedSection)
    var hexInput by remember { mutableStateOf("") }
    var contrastWarning by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "৪. স্বতন্ত্র সেকশন ডিজাইন",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { onResetSection(selectedSection) }) {
                    Text("এই সেকশন রিসেট", fontSize = 11.sp)
                }
            }

            Text(
                text = "নির্দিষ্ট সেকশনের নিজস্ব ব্যাকগ্রাউন্ড, টেক্সট কালার ও বর্ডার সেট করুন।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Select Section
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SectionKey.values()) { sec ->
                    FilterChip(
                        selected = selectedSection == sec,
                        onClick = { onSelectSection(sec) },
                        label = { Text(sec.titleBn.split(" ")[0], fontSize = 11.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Section Background Color Palette Picker
            Text(
                text = "সেকশন ব্যাকগ্রাউন্ড রঙ:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ColorUtils.PRESET_PALETTES) { (paletteColor, label) ->
                    val hexStr = ColorUtils.toHex(paletteColor)
                    val isSelected = sectionConfig.backgroundHex.equals(hexStr, ignoreCase = true)

                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                onUpdateSection(selectedSection) { cfg -> cfg.copy(backgroundHex = hexStr) }
                            },
                        shape = CircleShape,
                        color = paletteColor,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(3.dp, Color.White) else null
                    ) {
                        if (isSelected) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Custom Hex Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = hexInput,
                    onValueChange = {
                        hexInput = it
                        if (ColorUtils.isValidHex(it)) {
                            val parsed = ColorUtils.parseHexColor(it)
                            contrastWarning = parsed != null && !ColorUtils.hasAdequateContrast(Color.White, parsed)
                        }
                    },
                    label = { Text("হেক্স কালার কোড (যেমন #1E3A8A)", fontSize = 11.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_hex_color"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (ColorUtils.isValidHex(hexInput)) {
                            onUpdateSection(selectedSection) { cfg -> cfg.copy(backgroundHex = hexInput.trim()) }
                            hexInput = ""
                        }
                    },
                    enabled = ColorUtils.isValidHex(hexInput)
                ) {
                    Text("প্রয়োগ")
                }
            }

            if (contrastWarning) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "সতর্কতা: এই রঙের উপর সাদা লেখা পড়তে সমস্যা হতে পারে (Contrast low)।",
                        fontSize = 11.sp,
                        color = Color(0xFFD97706)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Card Padding Slider
            Text(
                text = "কার্ডের ভেতরের প্যাডিং: ${BengaliNumberFormatter.toBn(sectionConfig.paddingDp)} dp",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = sectionConfig.paddingDp.toFloat(),
                onValueChange = { onUpdateSection(selectedSection) { cfg -> cfg.copy(paddingDp = it.toInt()) } },
                valueRange = 8f..24f
            )

            // Corner Radius Slider
            Text(
                text = "এই সেকশনের কোণ বাঁকা: ${BengaliNumberFormatter.toBn(sectionConfig.cornerRadiusDp)} dp",
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = sectionConfig.cornerRadiusDp.toFloat(),
                onValueChange = { onUpdateSection(selectedSection) { cfg -> cfg.copy(cornerRadiusDp = it.toInt()) } },
                valueRange = 4f..32f
            )
        }
    }
}

// ==========================================
// CATEGORY 5: GLOBAL THEME & COLORS
// ==========================================
@Composable
fun GlobalThemeCustomizationSection(
    previewState: com.example.ui.customization.AppCustomizationState,
    themeMode: AppThemeMode,
    onSetThemeMode: (AppThemeMode) -> Unit,
    onUpdateGlobal: ((com.example.ui.customization.GlobalCustomConfig) -> com.example.ui.customization.GlobalCustomConfig) -> Unit,
    onResetCategory: () -> Unit
) {
    val global = previewState.global

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "৫. গ্লোবাল থিম ও রঙের প্যালেট",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onResetCategory) {
                    Text("ডিফল্ট কালার", fontSize = 12.sp)
                }
            }

            Text(
                text = "লাইট ও ডার্ক মোডের জন্য প্রাথমিক ও অ্যাকসেন্ট রঙ কাস্টমাইজ করুন।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // App Theme Mode Selector (System / Light / Dark)
            Text(
                text = "অ্যাপ থিম মোড (Theme Mode):",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppThemeMode.values().forEach { mode ->
                    FilterChip(
                        selected = themeMode == mode,
                        onClick = { onSetThemeMode(mode) },
                        label = { Text(mode.titleBn, fontSize = 12.sp) },
                        modifier = Modifier.testTag("chip_theme_${mode.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Global Accent Color
            Text(text = "অ্যাপের প্রাথমিক রঙ (Primary Theme Color):", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ColorUtils.PRESET_PALETTES) { (paletteColor, _) ->
                    val hexStr = ColorUtils.toHex(paletteColor)
                    val isSelected = global.customPrimaryActionHex.equals(hexStr, ignoreCase = true)

                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                onUpdateGlobal { it.copy(customPrimaryActionHex = hexStr, customAccentHex = hexStr) }
                            },
                        shape = CircleShape,
                        color = paletteColor,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(3.dp, Color.White) else null
                    ) {
                        if (isSelected) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Light Background Palette
            Text(text = "লাইট মোড ব্যাকগ্রাউন্ড রঙ:", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))

            val lightBgOptions = listOf(
                Color(0xFFF8FAFC) to "স্লেট হালকা",
                Color(0xFFF1F5F9) to "হালকা গ্রে",
                Color(0xFFEFF6FF) to "হালকা ব্লু",
                Color(0xFFF0FDF4) to "হালকা সবুজ",
                Color(0xFFFFFBEB) to "হালকা হলুদ"
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(lightBgOptions) { (color, label) ->
                    val hexStr = ColorUtils.toHex(color)
                    val isSelected = global.customLightBackgroundHex.equals(hexStr, ignoreCase = true)
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                onUpdateGlobal { it.copy(customLightBackgroundHex = hexStr) }
                            },
                        shape = CircleShape,
                        color = color,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
                    ) {
                        if (isSelected) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dark Background Palette
            Text(text = "ডার্ক মোড ব্যাকগ্রাউন্ড রঙ:", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))

            val darkBgOptions = listOf(
                Color(0xFF0B0F19) to "ডিপ নেভি",
                Color(0xFF111827) to "স্লেট ডার্ক",
                Color(0xFF000000) to "পিওর ওলেড ব্ল্যাক",
                Color(0xFF042F2E) to "ডিপ টিল ডার্ক",
                Color(0xFF1E1E24) to "চারকোল"
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(darkBgOptions) { (color, label) ->
                    val hexStr = ColorUtils.toHex(color)
                    val isSelected = global.customDarkBackgroundHex.equals(hexStr, ignoreCase = true)
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                onUpdateGlobal { it.copy(customDarkBackgroundHex = hexStr) }
                            },
                        shape = CircleShape,
                        color = color,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
                    ) {
                        if (isSelected) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// CATEGORY 6: LIVE PREVIEW
// ==========================================
@Composable
fun LivePreviewSection(
    previewState: com.example.ui.customization.AppCustomizationState
) {
    // Generate safe mock module list reflecting actual existing mathematical sections
    val mockModules = listOf(
        HomeModuleItem(
            sectionKey = SectionKey.TABLES,
            titleBn = "গুণ ও নামতা (১ থেকে ৪০)",
            subtitleBn = "৪টি রেঞ্জে বিভক্ত: ১-১০, ১১-২০, ২১-৩০, ৩১-৪০",
            badgeBn = "৪০ টি নামতা",
            defaultIcon = AvailableIcon.MENU_BOOK.toImageVector(),
            defaultGradient = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)),
            testTag = "preview_module_tables",
            onClick = {}
        ),
        HomeModuleItem(
            sectionKey = SectionKey.PRIMES,
            titleBn = "মৌলিক সংখ্যা (১ থেকে ২০০)",
            subtitleBn = "১-১০০ (২৫টি) ও ১০১-২০০ (২১টি) বিশ্লেষণ",
            badgeBn = "৪৬ টি মৌলিক",
            defaultIcon = AvailableIcon.LOOKS_ONE.toImageVector(),
            defaultGradient = listOf(Color(0xFF0D9488), Color(0xFF0F766E)),
            testTag = "preview_module_primes",
            onClick = {}
        ),
        HomeModuleItem(
            sectionKey = SectionKey.FORMULAS,
            titleBn = "বীজগণিতীয় সূত্রাবলী",
            subtitleBn = "বর্গ ও ঘনের সূত্র, প্রতিপাদন ও অনুসিদ্ধান্ত",
            badgeBn = "ব্যাখ্যা ও সমাধান",
            defaultIcon = AvailableIcon.FUNCTIONS.toImageVector(),
            defaultGradient = listOf(Color(0xFF7C3AED), Color(0xFF6D28D9)),
            testTag = "preview_module_formulas",
            onClick = {}
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "হোম মডিউল লেআউট রূপরেখা:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = previewState.global.layoutType.displayNameBn.split(" ")[0],
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            CustomModuleRenderer(
                modules = mockModules,
                customState = previewState
            )
        }
    }
}
