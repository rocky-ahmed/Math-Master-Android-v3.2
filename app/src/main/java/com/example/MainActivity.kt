package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MathViewModel
import com.example.ui.Screen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.CustomizeScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FormulaDetailScreen
import com.example.ui.screens.FormulaLibraryScreen
import com.example.ui.screens.FormulasScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MentalMathPlayScreen
import com.example.ui.screens.MentalMathScreen
import com.example.ui.screens.NumberSystemPracticePlayScreen
import com.example.ui.screens.NumberSystemScreen
import com.example.ui.screens.PracticeLabPlayScreen
import com.example.ui.screens.PracticeLabScreen
import com.example.ui.screens.PracticeTutorScreen
import com.example.ui.screens.PrimesScreen
import com.example.ui.screens.ProblemSolverScreen
import com.example.ui.screens.QuizPlayScreen
import com.example.ui.screens.QuizzesScreen
import com.example.ui.screens.SquaresCubesScreen
import com.example.ui.screens.TableDetailScreen
import com.example.ui.screens.TablesScreen
import com.example.ui.theme.MathMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MathViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            val customState by viewModel.customizationState.collectAsState()
            val previewCustomState by viewModel.previewCustomizationState.collectAsState()
            val screenStack by viewModel.screenStack.collectAsState()
            val currentScreen = screenStack.lastOrNull() ?: Screen.Home

            val activeConfig = if (currentScreen is Screen.Customize) {
                previewCustomState.global
            } else {
                customState.global
            }

            MathMasterTheme(
                themeMode = themeMode,
                globalConfig = activeConfig
            ) {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MathViewModel) {
    val screenStack by viewModel.screenStack.collectAsState()
    val currentScreen = screenStack.lastOrNull() ?: Screen.Home

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Show bottom navigation bar when on primary sections
            val showBottomNav = currentScreen in listOf(
                Screen.Home,
                Screen.Tables,
                Screen.Dashboard,
                Screen.Quizzes,
                Screen.Formulas,
                Screen.FormulaLibrary,
                Screen.PracticeLab,
                Screen.Bookmarks
            )

            if (showBottomNav) {
                NavigationBar(modifier = Modifier.testTag("app_bottom_nav")) {
                    NavigationBarItem(
                        selected = currentScreen == Screen.Home,
                        onClick = { viewModel.navigateToHome() },
                        icon = { Icon(Icons.Default.Home, contentDescription = "হোম") },
                        label = { Text("হোম") },
                        modifier = Modifier.testTag("nav_item_home")
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Tables,
                        onClick = { viewModel.navigateTo(Screen.Tables) },
                        icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "নামতা") },
                        label = { Text("নামতা") },
                        modifier = Modifier.testTag("nav_item_tables")
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Dashboard,
                        onClick = { viewModel.navigateTo(Screen.Dashboard) },
                        icon = { Icon(Icons.Default.BarChart, contentDescription = "অগ্রগতি") },
                        label = { Text("অগ্রগতি") },
                        modifier = Modifier.testTag("nav_item_dashboard")
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Bookmarks,
                        onClick = { viewModel.navigateTo(Screen.Bookmarks) },
                        icon = { Icon(Icons.Default.Bookmark, contentDescription = "বুকমার্ক") },
                        label = { Text("বুকমার্ক") },
                        modifier = Modifier.testTag("nav_item_bookmarks")
                    )
                    NavigationBarItem(
                        selected = currentScreen == Screen.Quizzes,
                        onClick = { viewModel.navigateTo(Screen.Quizzes) },
                        icon = { Icon(Icons.Default.Quiz, contentDescription = "কুইজ") },
                        label = { Text("কুইজ") },
                        modifier = Modifier.testTag("nav_item_quizzes")
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (currentScreen) {
            is Screen.Home -> HomeScreen(viewModel = viewModel, modifier = modifier)
            is Screen.Tables -> TablesScreen(viewModel = viewModel, modifier = modifier)
            is Screen.TableDetail -> TableDetailScreen(viewModel = viewModel, modifier = modifier)
            is Screen.Primes -> PrimesScreen(viewModel = viewModel, modifier = modifier)
            is Screen.SquaresCubes -> SquaresCubesScreen(viewModel = viewModel, modifier = modifier)
            is Screen.Formulas -> FormulasScreen(viewModel = viewModel, modifier = modifier)
            is Screen.FormulaDetail -> FormulaDetailScreen(viewModel = viewModel, modifier = modifier)
            is Screen.FormulaLibrary -> FormulaLibraryScreen(viewModel = viewModel, modifier = modifier)
            is Screen.PracticeLab -> PracticeLabScreen(viewModel = viewModel, modifier = modifier)
            is Screen.PracticeLabPlay -> PracticeLabPlayScreen(viewModel = viewModel, modifier = modifier)
            is Screen.MentalMath -> MentalMathScreen(viewModel = viewModel, modifier = modifier)
            is Screen.MentalMathPlay -> MentalMathPlayScreen(viewModel = viewModel, modifier = modifier)
            is Screen.NumberSystem -> NumberSystemScreen(viewModel = viewModel, modifier = modifier)
            is Screen.NumberSystemPractice -> NumberSystemPracticePlayScreen(viewModel = viewModel, modifier = modifier)
            is Screen.Quizzes -> QuizzesScreen(viewModel = viewModel, modifier = modifier)
            is Screen.QuizPlay -> QuizPlayScreen(viewModel = viewModel, modifier = modifier)
            is Screen.Bookmarks -> BookmarksScreen(viewModel = viewModel, modifier = modifier)
            is Screen.PracticeTutor -> PracticeTutorScreen(viewModel = viewModel, modifier = modifier)
            is Screen.Dashboard -> DashboardScreen(viewModel = viewModel, modifier = modifier)
            is Screen.Customize -> CustomizeScreen(viewModel = viewModel, modifier = modifier)
            is Screen.ProblemSolver -> ProblemSolverScreen(viewModel = viewModel, modifier = modifier)
            is Screen.VisualExplorer -> com.example.ui.screens.VisualExplorerScreen(viewModel = viewModel, modifier = modifier)
        }
    }
}
