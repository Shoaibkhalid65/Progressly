package com.example.progresstracker.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun SearchSortPracticeScreen() {
    var query by remember { mutableStateOf("") }
    val sortOptions = listOf("low to high", "high to low")
    var selectedIndex by remember { mutableIntStateOf(0) }
    val items = (1..50).toList()
    val searchItems = if (query.isEmpty()) items else items.filter { it.toString().contains(query) }
    val searchItems1 by remember(query) {
        derivedStateOf {
            items.filter {
                it.toString().contains(query)
            }
        }
    }
    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        SingleChoiceSegmentedButtonRow {
            sortOptions.forEachIndexed { index, i ->
                SegmentedButton(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    shape = SegmentedButtonDefaults.itemShape(index, count = sortOptions.size),
                    label = {
                        Text(i)
                    }
                )
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(searchItems1.sortedBy {
                if(selectedIndex==0) it else -it
            }, key = { it }) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = "headline content for the $it item",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            ListItemDefaults.containerColor,
                            RoundedCornerShape(12.dp)
                        )
                )
            }

        }
    }
}