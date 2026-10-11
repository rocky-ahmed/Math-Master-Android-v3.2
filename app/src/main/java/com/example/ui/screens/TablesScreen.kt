package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookmarkEntity
import com.example.math.BengaliNumberFormatter
import com.example.math.MultiplicationProvider
import com.example.math.TableRange
import com.example.ui.MathViewModel
import com.example.ui.components.BookmarkIconButton
import com.example.ui.components.LearnedStatusBadge
import com.example.ui.components.MathTopAppBar

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TablesScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val selectedRange by viewModel.selectedTableRange.collectAsState()
    val progressList by viewModel.progressList.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()

    val ranges = TableRange.values()

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = "গুণ ও নামতা (১ - ৪০)",
            subtitle = "৪টি রেঞ্জে বিভক্ত নামতার তালিকা",
            onBackClick = { viewModel.navigateBack() }
        )

        // 4 Range Tabs
        PrimaryTabRow(
            selectedTabIndex = ranges.indexOf(selectedRange),
            modifier = Modifier.fillMaxWidth().testTag("table_range_tabs")
        ) {
            ranges.forEach { range ->
                val isSelected = selectedRange == range
                Tab(
                    selected = isSelected,
                    onClick = { viewModel.selectRange(range) },
                    text = {
                        Text(
                            text = "${BengaliNumberFormatter.toBn(range.startNum)}-${BengaliNumberFormatter.toBn(range.endNum)}",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Grid of 10 tables for the selected range
        val tables = MultiplicationProvider.getTablesForRange(selectedRange)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tables) { table ->
                val itemId = "table_${table.number}"
                val isLearned = progressList.any { it.itemId == itemId && it.isLearned }
                val isBookmarked = bookmarks.any { it.id == itemId }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openTable(table.number) }
                        .testTag("table_item_${table.number}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (isLearned) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = table.bnNumber,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }

                            if (isLearned) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "শেখা সম্পন্ন",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "${table.bnNumber} এর ঘরের নামতা",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Preview first 2 rows
                        Text(
                            text = "${table.bnNumber} × ১ = ${BengaliNumberFormatter.toBn(table.number * 1)}\n${table.bnNumber} × ২ = ${BengaliNumberFormatter.toBn(table.number * 2)}...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "বিস্তারিত দেখুন",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )

                            BookmarkIconButton(
                                isBookmarked = isBookmarked,
                                onToggle = {
                                    viewModel.toggleBookmark(
                                        BookmarkEntity(
                                            id = itemId,
                                            category = "TABLE",
                                            titleBn = "${table.bnNumber} এর ঘরের নামতা",
                                            subtitleBn = "গুণ ও নামতা (১-৪০)",
                                            detailBn = "${table.bnNumber} × ১ থেকে ১০ পর্যন্ত সম্পূর্ণ নামতা"
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableDetailScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val tableNumber by viewModel.selectedTableNumber.collectAsState()
    val table = MultiplicationProvider.getTable(tableNumber)
    val itemId = "table_${tableNumber}"

    val bookmarks by viewModel.bookmarks.collectAsState()
    val progressList by viewModel.progressList.collectAsState()

    val isBookmarked = bookmarks.any { it.id == itemId }
    val isLearned = progressList.any { it.itemId == itemId && it.isLearned }

    Column(modifier = modifier.fillMaxSize()) {
        MathTopAppBar(
            title = table.titleBn,
            subtitle = "১ থেকে ১০ পর্যন্ত গুণফল",
            onBackClick = { viewModel.navigateBack() },
            actions = {
                BookmarkIconButton(
                    isBookmarked = isBookmarked,
                    onToggle = {
                        viewModel.toggleBookmark(
                            BookmarkEntity(
                                id = itemId,
                                category = "TABLE",
                                titleBn = table.titleBn,
                                subtitleBn = "গুণ ও নামতা",
                                detailBn = "${table.bnNumber} এর পূর্ণাঙ্গ নামতা"
                            )
                        )
                    }
                )
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Card with Quick Practice action and Mastery toggle
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "মুখস্থ হয়েছে কি?",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            LearnedStatusBadge(
                                isLearned = isLearned,
                                onToggle = { viewModel.toggleLearned(itemId) }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Interactive step practice button for this table
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.startTutor(tableNumber) }
                                .testTag("practice_table_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "এই নামতাটি অনুশীলন করুন (স্টেপ-বাই-স্টেপ)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Multiplication Rows 1 to 10
            items(table.rows) { row ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${row.bnMultiplier}  ×  ${row.bnMultiplicand}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Text(
                            text = "=   ${row.bnProduct}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }
}
