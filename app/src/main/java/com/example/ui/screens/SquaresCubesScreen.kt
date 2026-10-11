package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookmarkEntity
import com.example.math.SquareCubeProvider
import com.example.ui.MathViewModel
import com.example.ui.components.BookmarkIconButton
import com.example.ui.components.MathTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquaresCubesScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateBack() }

    val bookmarks by viewModel.bookmarks.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = বর্গ তালিকা (241713.jpg), 1 = ঘন তালিকা (241715.jpg)

    val squareItems = SquareCubeProvider.squareTableList
    val cubeItems = SquareCubeProvider.cubeTableList

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MathTopAppBar(
            title = if (selectedTab == 0) "১ থেকে ২০ এর বর্গ তালিকা" else "ঘন ও ঘনমূলের সম্পূর্ণ তালিকা",
            subtitle = if (selectedTab == 0) "ছবি: 241713.jpg" else "ছবি: 241715.jpg",
            onBackClick = { viewModel.navigateBack() }
        )

        // Tab Row
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth().testTag("square_cube_tab_row")
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "বর্গ ও বর্গমূল তালিকা",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "ঘন ও ঘনমূল তালিকা",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Text (হুবহু PDF এর হেডার টেক্সট)
            item {
                Text(
                    text = if (selectedTab == 0) SquareCubeProvider.squareHeaderTitle else SquareCubeProvider.cubeHeaderTitle,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Real Document Table
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Table Header Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(vertical = 12.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "সংখ্যা",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(0.7f)
                            )
                            Text(
                                text = if (selectedTab == 0) "বর্গ (Square)" else "ঘন (Cube)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(1.3f)
                            )
                            Text(
                                text = if (selectedTab == 0) "বর্গমূল (Square Root)" else "ঘনমূল (Cube Root)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(1.3f)
                            )
                            // Space for action
                            Spacer(modifier = Modifier.width(36.dp))
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                        // Table Data Rows
                        if (selectedTab == 0) {
                            squareItems.forEachIndexed { index, item ->
                                val bookmarkId = "sq_${item.n}"
                                val isBookmarked = bookmarks.any { it.id == bookmarkId }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (index % 2 == 0) MaterialTheme.colorScheme.surface
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                        )
                                        .padding(vertical = 12.dp, horizontal = 10.dp)
                                        .testTag("square_table_row_${item.n}"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Column 1: সংখ্যা
                                    Text(
                                        text = "${item.n}",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp
                                        ),
                                        modifier = Modifier.weight(0.7f)
                                    )

                                    // Column 2: বর্গ (Square)
                                    Text(
                                        text = item.squareText,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.weight(1.3f)
                                    )

                                    // Column 3: বর্গমূল (Square Root)
                                    Text(
                                        text = item.squareRootText,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier.weight(1.3f)
                                    )

                                    BookmarkIconButton(
                                        isBookmarked = isBookmarked,
                                        onToggle = {
                                            viewModel.toggleBookmark(
                                                BookmarkEntity(
                                                    id = bookmarkId,
                                                    category = "SQUARE_CUBE",
                                                    titleBn = "${item.n} এর বর্গ ও বর্গমূল",
                                                    subtitleBn = "বর্গ তালিকা (241713.jpg)",
                                                    detailBn = "${item.squareText} এবং ${item.squareRootText}"
                                                )
                                            )
                                        }
                                    )
                                }

                                if (index < squareItems.size - 1) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                }
                            }
                        } else {
                            cubeItems.forEachIndexed { index, item ->
                                val bookmarkId = "cube_${item.n}"
                                val isBookmarked = bookmarks.any { it.id == bookmarkId }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (index % 2 == 0) MaterialTheme.colorScheme.surface
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                        )
                                        .padding(vertical = 14.dp, horizontal = 10.dp)
                                        .testTag("cube_table_row_${item.n}"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Column 1: সংখ্যা
                                    Text(
                                        text = "${item.n}",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp
                                        ),
                                        modifier = Modifier.weight(0.7f)
                                    )

                                    // Column 2: ঘন (Cube)
                                    Text(
                                        text = item.cubeText,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.weight(1.3f)
                                    )

                                    // Column 3: ঘনমূল (Cube Root)
                                    Text(
                                        text = item.cubeRootText,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier.weight(1.3f)
                                    )

                                    BookmarkIconButton(
                                        isBookmarked = isBookmarked,
                                        onToggle = {
                                            viewModel.toggleBookmark(
                                                BookmarkEntity(
                                                    id = bookmarkId,
                                                    category = "SQUARE_CUBE",
                                                    titleBn = "${item.n} এর ঘন ও ঘনমূল",
                                                    subtitleBn = "ঘন তালিকা (241715.jpg)",
                                                    detailBn = "${item.cubeText} এবং ${item.cubeRootText}"
                                                )
                                            )
                                        }
                                    )
                                }

                                if (index < cubeItems.size - 1) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                }
                            }
                        }
                    }
                }
            }

            // Footer Note (হুবহু PDF এর ফুটার টেক্সট)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = if (selectedTab == 0) SquareCubeProvider.squareFooterNote else SquareCubeProvider.cubeFooterNote,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        }
    }
}
