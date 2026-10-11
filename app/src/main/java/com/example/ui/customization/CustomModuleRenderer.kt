package com.example.ui.customization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data item representing a home module.
 */
data class HomeModuleItem(
    val sectionKey: SectionKey,
    val titleBn: String,
    val subtitleBn: String,
    val badgeBn: String,
    val defaultIcon: ImageVector,
    val defaultGradient: List<Color>,
    val testTag: String,
    val onClick: () -> Unit
)

/**
 * CustomModuleRenderer renders module cards honoring user's layout choice (Grid, Compact,
 * Comfortable, Table) and section-specific overrides (colors, icons, fonts, corner radius).
 */
@Composable
fun CustomModuleRenderer(
    modules: List<HomeModuleItem>,
    customState: AppCustomizationState,
    modifier: Modifier = Modifier
) {
    val global = customState.global
    val layoutType = global.layoutType

    when (layoutType) {
        CustomLayoutType.GRID -> {
            // Adaptive Grid with user-chosen column count (1 to 4) using responsive rows
            val columns = global.gridColumns.coerceIn(1, 4)
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(global.verticalGapDp.dp)
            ) {
                modules.chunked(columns).forEach { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(global.horizontalGapDp.dp)
                    ) {
                        rowModules.forEach { item ->
                            Box(modifier = Modifier.weight(1f)) {
                                val sectionConfig = customState.getSectionConfig(item.sectionKey)
                                GridModuleCard(
                                    item = item,
                                    global = global,
                                    sectionConfig = sectionConfig
                                )
                            }
                        }
                        val remaining = columns - rowModules.size
                        if (remaining > 0) {
                            repeat(remaining) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
        CustomLayoutType.COMPACT_LIST -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy((global.verticalGapDp / 2).coerceAtLeast(6).dp)
            ) {
                modules.forEach { item ->
                    val sectionConfig = customState.getSectionConfig(item.sectionKey)
                    CompactModuleCard(
                        item = item,
                        global = global,
                        sectionConfig = sectionConfig
                    )
                }
            }
        }
        CustomLayoutType.TABLE_LIST -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(global.verticalGapDp.dp)
            ) {
                modules.forEach { item ->
                    val sectionConfig = customState.getSectionConfig(item.sectionKey)
                    TableModuleCard(
                        item = item,
                        global = global,
                        sectionConfig = sectionConfig
                    )
                }
            }
        }
        CustomLayoutType.COMFORTABLE_LIST -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(global.verticalGapDp.dp)
            ) {
                modules.forEach { item ->
                    val sectionConfig = customState.getSectionConfig(item.sectionKey)
                    ComfortableModuleCard(
                        item = item,
                        global = global,
                        sectionConfig = sectionConfig
                    )
                }
            }
        }
    }
}

@Composable
fun ComfortableModuleCard(
    item: HomeModuleItem,
    global: GlobalCustomConfig,
    sectionConfig: SectionCustomConfig
) {
    val cornerRadius = (sectionConfig.cornerRadiusDp.takeIf { it > 0 } ?: global.cornerRadiusDp).dp
    val iconSize = (sectionConfig.iconSizeDp.takeIf { it > 0 } ?: global.iconSizeDp).dp
    val padding = sectionConfig.paddingDp.dp
    val spacing = global.iconTextSpacingDp.dp

    // Border resolution
    val borderThickness = (if (sectionConfig.borderThicknessDp > 0) sectionConfig.borderThicknessDp else if (global.showBorder) global.borderThicknessDp.coerceAtLeast(1) else 0).dp
    val customBorderColor = ColorUtils.parseHexColor(sectionConfig.borderColorHex) ?: MaterialTheme.colorScheme.outlineVariant

    // Background color resolution
    val customBgColor = ColorUtils.parseHexColor(sectionConfig.backgroundHex)
    val cardBg = customBgColor ?: MaterialTheme.colorScheme.surface

    // Icon resolution
    val iconVector = sectionConfig.iconId?.let { AvailableIcon.fromId(it).toImageVector() } ?: item.defaultIcon
    val customIconColor = ColorUtils.parseHexColor(sectionConfig.iconColorHex)

    // Font weight resolution
    val fontWeight = when (global.fontWeight) {
        CustomFontWeight.REGULAR -> FontWeight.Normal
        CustomFontWeight.MEDIUM -> FontWeight.SemiBold
        CustomFontWeight.BOLD -> FontWeight.Bold
    }

    // Font size resolution
    val titleSize = (sectionConfig.titleFontSizeSp.takeIf { it != 16 } ?: global.titleFontSizeSp).sp
    val subtitleSize = (sectionConfig.subtitleFontSizeSp.takeIf { it != 12 } ?: global.subtitleFontSizeSp).sp

    // Text colors
    val customTitleColor = ColorUtils.parseHexColor(sectionConfig.titleColorHex) ?: MaterialTheme.colorScheme.onSurface
    val customSubtitleColor = ColorUtils.parseHexColor(sectionConfig.subtitleColorHex) ?: MaterialTheme.colorScheme.onSurfaceVariant

    val subtleHighlight = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
    val subtleBorder = customBorderColor.copy(alpha = if (borderThickness > 0.dp) 1f else 0.12f)
    val effectiveBorderThickness = if (borderThickness > 0.dp) borderThickness else 1.dp

    val cardModifier = Modifier
        .fillMaxWidth()
        .then(if (sectionConfig.cardHeightDp > 0) Modifier.heightIn(min = sectionConfig.cardHeightDp.dp) else Modifier)
        .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(cornerRadius),
            ambientColor = Color.Black.copy(alpha = 0.08f),
            spotColor = Color.Black.copy(alpha = 0.12f)
        )
        .clip(RoundedCornerShape(cornerRadius))
        .border(effectiveBorderThickness, subtleBorder, RoundedCornerShape(cornerRadius))
        .clickable { item.onClick() }
        .testTag(item.testTag)

    ElevatedCard(
        modifier = cardModifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.elevatedCardColors(containerColor = cardBg),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp, pressedElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    // Subtle 3D top edge highlight reflecting ambient light
                    drawLine(
                        color = subtleHighlight,
                        start = Offset(0f, 1f),
                        end = Offset(size.width, 1f),
                        strokeWidth = 2f
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Polished 3D Icon Container with layered shadow
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    shadowElevation = 3.dp,
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .size(iconSize + 24.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(item.defaultGradient))
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = customIconColor ?: Color.White,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(spacing))

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = if (global.contentAlignmentCenter) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    Text(
                        text = item.titleBn,
                        fontSize = titleSize,
                        fontWeight = fontWeight,
                        color = customTitleColor,
                        textAlign = if (global.contentAlignmentCenter) TextAlign.Center else TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = item.subtitleBn,
                        fontSize = subtitleSize,
                        color = customSubtitleColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = if (global.contentAlignmentCenter) TextAlign.Center else TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.75f),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = item.badgeBn,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                        )
                    }
                }

                if (!global.contentAlignmentCenter) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompactModuleCard(
    item: HomeModuleItem,
    global: GlobalCustomConfig,
    sectionConfig: SectionCustomConfig
) {
    val cornerRadius = (sectionConfig.cornerRadiusDp.takeIf { it > 0 } ?: global.cornerRadiusDp).dp
    val iconSize = ((sectionConfig.iconSizeDp.takeIf { it > 0 } ?: global.iconSizeDp) * 0.8f).dp
    val customBgColor = ColorUtils.parseHexColor(sectionConfig.backgroundHex) ?: MaterialTheme.colorScheme.surface
    val iconVector = sectionConfig.iconId?.let { AvailableIcon.fromId(it).toImageVector() } ?: item.defaultIcon
    val customIconColor = ColorUtils.parseHexColor(sectionConfig.iconColorHex)

    val borderThickness = (if (sectionConfig.borderThicknessDp > 0) sectionConfig.borderThicknessDp else if (global.showBorder) 1 else 0).dp
    val customBorderColor = ColorUtils.parseHexColor(sectionConfig.borderColorHex) ?: MaterialTheme.colorScheme.outlineVariant

    val customTitleColor = ColorUtils.parseHexColor(sectionConfig.titleColorHex) ?: MaterialTheme.colorScheme.onSurface
    val customSubtitleColor = ColorUtils.parseHexColor(sectionConfig.subtitleColorHex) ?: MaterialTheme.colorScheme.onSurfaceVariant

    val fontWeight = when (global.fontWeight) {
        CustomFontWeight.REGULAR -> FontWeight.Normal
        CustomFontWeight.MEDIUM -> FontWeight.SemiBold
        CustomFontWeight.BOLD -> FontWeight.Bold
    }

    val titleSize = (sectionConfig.titleFontSizeSp.takeIf { it != 16 } ?: global.titleFontSizeSp).sp * 0.95f
    val subtitleSize = (sectionConfig.subtitleFontSizeSp.takeIf { it != 12 } ?: global.subtitleFontSizeSp).sp * 0.95f

    val subtleBorder = customBorderColor.copy(alpha = if (borderThickness > 0.dp) 1f else 0.12f)
    val effectiveBorderThickness = if (borderThickness > 0.dp) borderThickness else 1.dp

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .border(effectiveBorderThickness, subtleBorder, RoundedCornerShape(cornerRadius))
            .clickable { item.onClick() }
            .testTag(item.testTag),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.elevatedCardColors(containerColor = customBgColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp, pressedElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = (sectionConfig.paddingDp * 0.6f).coerceAtLeast(8f).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                shadowElevation = 2.dp,
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .size(iconSize + 14.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.linearGradient(item.defaultGradient))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = customIconColor ?: Color.White,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = if (global.contentAlignmentCenter) Alignment.CenterHorizontally else Alignment.Start
            ) {
                Text(
                    text = item.titleBn,
                    fontSize = titleSize,
                    fontWeight = fontWeight,
                    color = customTitleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (global.contentAlignmentCenter) TextAlign.Center else TextAlign.Start
                )
                Text(
                    text = item.subtitleBn,
                    fontSize = subtitleSize,
                    color = customSubtitleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (global.contentAlignmentCenter) TextAlign.Center else TextAlign.Start
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.75f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Text(
                    text = item.badgeBn,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                )
            }
        }
    }
}

@Composable
fun GridModuleCard(
    item: HomeModuleItem,
    global: GlobalCustomConfig,
    sectionConfig: SectionCustomConfig
) {
    val cornerRadius = (sectionConfig.cornerRadiusDp.takeIf { it > 0 } ?: global.cornerRadiusDp).dp
    val iconSize = (sectionConfig.iconSizeDp.takeIf { it > 0 } ?: global.iconSizeDp).dp
    val customBgColor = ColorUtils.parseHexColor(sectionConfig.backgroundHex) ?: MaterialTheme.colorScheme.surface
    val iconVector = sectionConfig.iconId?.let { AvailableIcon.fromId(it).toImageVector() } ?: item.defaultIcon
    val customIconColor = ColorUtils.parseHexColor(sectionConfig.iconColorHex)

    val borderThickness = (if (sectionConfig.borderThicknessDp > 0) sectionConfig.borderThicknessDp else if (global.showBorder) 1 else 0).dp
    val customBorderColor = ColorUtils.parseHexColor(sectionConfig.borderColorHex) ?: MaterialTheme.colorScheme.outlineVariant

    val customTitleColor = ColorUtils.parseHexColor(sectionConfig.titleColorHex) ?: MaterialTheme.colorScheme.onSurface
    val customSubtitleColor = ColorUtils.parseHexColor(sectionConfig.subtitleColorHex) ?: MaterialTheme.colorScheme.onSurfaceVariant

    val titleSize = (sectionConfig.titleFontSizeSp.takeIf { it != 16 } ?: global.titleFontSizeSp).sp * 0.9f
    val subtitleSize = (sectionConfig.subtitleFontSizeSp.takeIf { it != 12 } ?: global.subtitleFontSizeSp).sp * 0.9f

    val subtleHighlight = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
    val subtleBorder = customBorderColor.copy(alpha = if (borderThickness > 0.dp) 1f else 0.12f)
    val effectiveBorderThickness = if (borderThickness > 0.dp) borderThickness else 1.dp

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (sectionConfig.cardHeightDp > 0) Modifier.heightIn(min = sectionConfig.cardHeightDp.dp) else Modifier.heightIn(min = 130.dp))
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .border(effectiveBorderThickness, subtleBorder, RoundedCornerShape(cornerRadius))
            .clickable { item.onClick() }
            .testTag(item.testTag),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.elevatedCardColors(containerColor = customBgColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp, pressedElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = subtleHighlight,
                        start = Offset(0f, 1f),
                        end = Offset(size.width, 1f),
                        strokeWidth = 2f
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(sectionConfig.paddingDp.dp),
                horizontalAlignment = if (global.contentAlignmentCenter) Alignment.CenterHorizontally else Alignment.Start
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 2.dp,
                    color = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .size(iconSize + 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(item.defaultGradient))
                            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = customIconColor ?: Color.White,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = item.titleBn,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold,
                    color = customTitleColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (global.contentAlignmentCenter) TextAlign.Center else TextAlign.Start
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.subtitleBn,
                    fontSize = subtitleSize,
                    color = customSubtitleColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (global.contentAlignmentCenter) TextAlign.Center else TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun TableModuleCard(
    item: HomeModuleItem,
    global: GlobalCustomConfig,
    sectionConfig: SectionCustomConfig
) {
    val cornerRadius = (sectionConfig.cornerRadiusDp.takeIf { it > 0 } ?: global.cornerRadiusDp).dp
    val iconSize = ((sectionConfig.iconSizeDp.takeIf { it > 0 } ?: global.iconSizeDp) * 0.85f).dp
    val customBgColor = ColorUtils.parseHexColor(sectionConfig.backgroundHex) ?: MaterialTheme.colorScheme.surface
    val iconVector = sectionConfig.iconId?.let { AvailableIcon.fromId(it).toImageVector() } ?: item.defaultIcon
    val customIconColor = ColorUtils.parseHexColor(sectionConfig.iconColorHex)

    val customTitleColor = ColorUtils.parseHexColor(sectionConfig.titleColorHex) ?: MaterialTheme.colorScheme.onSurface
    val customSubtitleColor = ColorUtils.parseHexColor(sectionConfig.subtitleColorHex) ?: MaterialTheme.colorScheme.onSurfaceVariant

    val borderThickness = (if (sectionConfig.borderThicknessDp > 0) sectionConfig.borderThicknessDp else if (global.showBorder) 1 else 0).dp
    val customBorderColor = ColorUtils.parseHexColor(sectionConfig.borderColorHex) ?: MaterialTheme.colorScheme.outlineVariant

    val fontWeight = when (global.fontWeight) {
        CustomFontWeight.REGULAR -> FontWeight.Normal
        CustomFontWeight.MEDIUM -> FontWeight.SemiBold
        CustomFontWeight.BOLD -> FontWeight.Bold
    }

    val titleSize = (sectionConfig.titleFontSizeSp.takeIf { it != 16 } ?: global.titleFontSizeSp).sp * 0.95f
    val subtitleSize = (sectionConfig.subtitleFontSizeSp.takeIf { it != 12 } ?: global.subtitleFontSizeSp).sp * 0.95f

    val subtleBorder = customBorderColor.copy(alpha = if (borderThickness > 0.dp) 1f else 0.2f)
    val effectiveBorderThickness = if (borderThickness > 0.dp) borderThickness else 1.dp

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .border(effectiveBorderThickness, subtleBorder, RoundedCornerShape(cornerRadius))
            .clickable { item.onClick() }
            .testTag(item.testTag),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.elevatedCardColors(containerColor = customBgColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp, pressedElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = (sectionConfig.paddingDp * 0.65f).coerceAtLeast(8f).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = (customIconColor ?: MaterialTheme.colorScheme.primary).copy(alpha = 0.12f),
                modifier = Modifier.size(iconSize + 14.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = customIconColor ?: MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = if (global.contentAlignmentCenter) Alignment.CenterHorizontally else Alignment.Start
            ) {
                Text(
                    text = item.titleBn,
                    fontSize = titleSize,
                    fontWeight = fontWeight,
                    color = customTitleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (global.contentAlignmentCenter) TextAlign.Center else TextAlign.Start
                )
                Text(
                    text = item.subtitleBn,
                    fontSize = subtitleSize,
                    color = customSubtitleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = if (global.contentAlignmentCenter) TextAlign.Center else TextAlign.Start
                )
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            ) {
                Text(
                    text = item.badgeBn,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                )
            }
        }
    }
}
