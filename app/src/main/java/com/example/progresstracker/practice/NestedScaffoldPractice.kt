package com.example.progresstracker.practice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedScaffoldScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
        NavigationBar() {
            listOf("ali", "khamenei").forEachIndexed { index, string ->
                NavigationBarItem(
                    selected = true,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = ""
                        )
                    },
                    onClick = {

                    }
                )
            }
        }
    }) { innerPadding ->
        Scaffold(
            modifier = Modifier.padding(innerPadding).fillMaxSize(), topBar = {
                TopAppBar(title = {
                    Text(
                        text = "top bar title"
                    )
                })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null
                    )
                }
            }) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(
                    "the main content inside the nested scaffolds"
                )
            }
        }
    }
}