package com.example.progresstracker.ui.dailyTask

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.progresstracker.ui.dailyTask.taskDuration.TaskDurationScreen
import com.example.progresstracker.ui.dailyTask.taskDuration.TaskDurationViewModel
import com.example.progresstracker.ui.dailyTask.tasksList.TasksListScreen
import com.example.progresstracker.ui.dailyTask.tasksList.TasksListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTaskMainScreen(
    navController: NavHostController,
    tasksListViewModel: TasksListViewModel = hiltViewModel(),
    taskDurationViewModel: TaskDurationViewModel = hiltViewModel()
) {
    val titles = listOf("tasks", "durations")
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { titles.size }
    val snackbarHostState = remember { SnackbarHostState() }

    val isTasksScreen = pagerState.currentPage == 0
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val density = LocalDensity.current
    val fabOffsetPx = with(density) { 45.dp.toPx() }
    val snackbarOffset = with(density) { 80.dp.toPx() }

    val fabOffset by animateFloatAsState(
        targetValue = if (snackbarHostState.currentSnackbarData == null) 0f else -fabOffsetPx,
        animationSpec = tween(300),
        label = "fab offset"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Crossfade(targetState = isTasksScreen) { isTasks ->
                        Text(
                            text = if (isTasks) "My Tasks" else "My Durations"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isTasksScreen) {
                                tasksListViewModel.updateShowDeleteAllDialog(true)
                            } else {
                                taskDurationViewModel.updateShowAllDeletionDialog(true)
                            }
                        }
                    ) { Icon(Icons.Default.Delete, null) }
                },
                windowInsets = WindowInsets(),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (isTasksScreen) {
                        tasksListViewModel.onCreateTaskClick()
                    } else {
                        taskDurationViewModel.updateShowCreationDialog(true)
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                text = {
                    Crossfade(targetState = isTasksScreen, label = "fab text") { isTasksScreen ->
                        Text(
                            text = if (isTasksScreen) "Add Task     " else "Add Duration"
                        )
                    }
                },
                modifier = Modifier.graphicsLayer {
                    translationY = fabOffset
                }
            )
        },
        contentWindowInsets = WindowInsets(),
        snackbarHost = {
            SnackbarHost(
                snackbarHostState, modifier = Modifier.graphicsLayer {
                    translationY = snackbarOffset
                }
            )
        }

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            PrimaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage
            ) {
                titles.forEachIndexed { index, string ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = string
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                when (index) {
                    0 -> TasksListScreen(snackbarHostState, navController)
                    1 -> TaskDurationScreen(snackbarHostState)
                }
            }
        }
    }
}