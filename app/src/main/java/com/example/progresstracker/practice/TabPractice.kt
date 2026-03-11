package com.example.progresstracker.practice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun TabPracticeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().safeContentPadding(),

    ) {
        var selectedIndex by remember { mutableIntStateOf(0) }
        PrimaryScrollableTabRow(selectedTabIndex = selectedIndex) {
            DailyTaskTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    text = { Text(tab.name) }
                )
            }
        }

        when(selectedIndex){
            0-> HomeScreen()
            1-> ProfileScreen()
        }
    }
}

enum class DailyTaskTab {
    DailyTask, TaskDuration
}


@Composable
fun TabPracticeScreenWithPager(){
    val pagerState= rememberPagerState(pageCount = { DailyTaskTab.entries.size})
    val coroutineScope= rememberCoroutineScope()

    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize()
    ) {
        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage

        ) {
            DailyTaskTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage==index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {Text(tab.name)}
                )
            }
        }

        HorizontalPager(
            state = pagerState,
        ) { index->
            when(index){
                0->HomeScreen()
                1->ProfileScreen()
            }

        }
    }

}

@Composable
fun MyTabScreen() {

    val tabs = listOf("Home", "Profile")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column (
        modifier = Modifier.safeContentPadding()
    ){
        SecondaryTabRow(
            selectedTabIndex,
            Modifier,
            TabRowDefaults.primaryContainerColor,
            TabRowDefaults.primaryContentColor,
            tabs={
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }
        )

//        when (selectedTabIndex) {
//            0 -> HomeScreen()
//            1 -> ProfileScreen()
//        }
    }
}

@Composable
fun HomeScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen")
    }
}