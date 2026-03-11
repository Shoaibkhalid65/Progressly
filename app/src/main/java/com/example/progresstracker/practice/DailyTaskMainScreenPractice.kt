package com.example.progresstracker.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTaskMainScreen3() {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    val titles = listOf("Tasks", "Durations")

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = pagerState.currentPage == 0,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                TopAppBar(
                    title = { Text("My Tasks Screen") },
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.Delete, null) }
                    },
                    windowInsets = WindowInsets()
                )
            }
            AnimatedVisibility(
                visible = pagerState.currentPage == 1,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                TopAppBar(
                    title = { Text("Task durations Screen") },
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.DeleteForever, null) }
                    },
                    windowInsets = WindowInsets()
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = pagerState.currentPage == 0,
//                enter = fadeIn() + slideInHorizontally(),
//                exit = fadeOut() + slideOutHorizontally()
            ) {
//                ExtendedFloatingActionButton(onClick = {}, text = { Text("Add Task") }, icon = {
//                    Icon(
//                        Icons.Default.Add, null
//                    )
//                },elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp))
                FloatingActionButton(
                    onClick = {},
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
                ) { Icon(Icons.Default.Add, null) }

            }
            AnimatedVisibility(
                visible = pagerState.currentPage == 1,
//                enter = fadeIn() + slideInHorizontally(),
//                exit = fadeOut() + slideOutHorizontally()
            ) {
//                ExtendedFloatingActionButton(
//                    onClick = {}, text = { Text("Add Duration") },
//                    icon = {
//                        Icon(
//                            Icons.Default.Add, null
//                        )
//                    },
//                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
//                )
                FloatingActionButton(
                    onClick = {},
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
                ) { Icon(Icons.Default.Add, null) }
            }
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            PrimaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                when (index) {
                    0 -> MyTasksListScreen()
                    1 -> MyDurationsListScreen()
                }
            }
        }
    }
}

@Composable
fun MyTasksListScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("Hello 1 from my tasks screen")
        Text("Hello 2 from my tasks screen")
    }
}

@Composable
fun MyDurationsListScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("Hello 1 from my Durations screen")
        Text("Hello 2 from my Durations screen")
    }
}

@Composable
fun MyTasksListScreen1() {
    Scaffold(
        contentWindowInsets = WindowInsets(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
            ) { Icon(Icons.Default.Add, null) }

        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Hello 1 from my tasks screen")
            Text("Hello 2 from my tasks screen")
        }
    }
}

@Composable
fun MyDurationsListScreen1() {
    Scaffold(
        contentWindowInsets = WindowInsets(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
            ) { Icon(Icons.Default.Add, null) }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Hello 1 from my Durations screen")
            Text("Hello 2 from my Durations screen")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTaskMainScreen1() {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    val titles = listOf("Tasks", "Durations")
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = pagerState.currentPage == 0,
//            enter =  slideInHorizontally(),
//            exit = slideOutHorizontally()
        ) {
            TopAppBar(
                title = { Text("My Tasks Screen") },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Delete, null) }
                },
                windowInsets = WindowInsets()
            )
        }
        AnimatedVisibility(
            visible = pagerState.currentPage == 1,
//            enter = fadeIn() + slideInHorizontally(),
//            exit = fadeOut() + slideOutHorizontally()
        ) {
            TopAppBar(
                title = { Text("Task durations Screen") },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.DeleteForever, null) }
                },
                windowInsets = WindowInsets()
            )
        }

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
                        Text(string)
                    }
                )
            }
        }

        HorizontalPager(pagerState, modifier = Modifier.fillMaxSize()) {
            when(pagerState.currentPage){
                0 -> MyTasksListScreen1()
                1 -> MyDurationsListScreen1()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTaskMainScreen2() {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    val titles = listOf("Tasks", "Durations")

    // Derive current page state
    val isTasksPage = pagerState.currentPage == 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Crossfade only the TEXT content
                    Crossfade(targetState = isTasksPage, label = "topbar_title") { _ ->
                        Text(if (isTasksPage) "My Tasks Screen" else "Task Durations Screen")
                    }
                },
                actions = {
                    // Crossfade only the ICON content
                    Crossfade(targetState = isTasksPage, label = "topbar_action") { isTasksPage ->
                        if (isTasksPage) {
                            IconButton(onClick = {}) { Icon(Icons.Default.Delete, null) }
                        } else {
                            IconButton(onClick = {}) { Icon(Icons.Default.DeleteForever, null) }
                        }
                    }
                },
                windowInsets = WindowInsets()
            )
        },
        floatingActionButton = {
            // Single FAB — only the onClick/icon change, size never fluctuates
            FloatingActionButton(
                onClick = { if (isTasksPage) { /* add task */ } else { /* add duration */ } },
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
            ) {
                Icon(Icons.Default.Add, null)
            }
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            PrimaryScrollableTabRow(selectedTabIndex = pagerState.currentPage) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                when (index) {
                    0 -> MyTasksListScreen()
                    1 -> MyDurationsListScreen()
                }
            }
        }
    }
}
