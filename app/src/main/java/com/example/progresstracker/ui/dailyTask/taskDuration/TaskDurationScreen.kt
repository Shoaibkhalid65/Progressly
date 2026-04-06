package com.example.progresstracker.ui.dailyTask.taskDuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.progresstracker.R
import com.example.progresstracker.utils.DateTimeUtils

@Composable
fun TaskDurationScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: TaskDurationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TaskDurationUiEvent.Success -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                is TaskDurationUiEvent.Error -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }
    if (uiState.durations.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.TimerOff,
                contentDescription = "empty durations",
                tint = Color.LightGray,
                modifier = Modifier.size(72.dp)
            )
            Text(
                text = "Currently no durations!",
                fontSize = 18.sp,
                color = Color.LightGray
            )
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 80.dp)
    ) {
        items(uiState.durations, key = { it.id }) { duration ->
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineContent = {
                    Text(
                        text = "Duration Time : ${DateTimeUtils.millisToFormattedDuration(duration.durationTime)}"
                    )
                },
                leadingContent = {
                    Text(text = duration.id.toString())
                },
                supportingContent = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Start time ${DateTimeUtils.millisToFormattedTime(duration.startTime)}"
                        )

                        Text(
                            text = "End time ${DateTimeUtils.millisToFormattedTime(duration.endTime)}"
                        )
                    }
                },
                trailingContent = {
                    IconButton(onClick = {
                        viewModel.updateShowDeletionDialog(true)
                        viewModel.updateDurationToDeleteId(duration.id)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
    if (uiState.showDeleteDialog) {
        DeleteDialog(
            onDismiss = {
                viewModel.updateShowDeletionDialog(false)
                viewModel.updateDurationToDeleteId(-1L)
            },
            onConfirm = {
                viewModel.deleteTaskDuration(uiState.durationToDeleteId)
            }
        )
    }
    if (uiState.showDeleteAllDialog) {
        DeleteDialog(
            onDismiss = {
                viewModel.updateShowAllDeletionDialog(false)
            },
            onConfirm = {
                viewModel.deleteTaskDurationsOfMaxId()
            }
        )
    }

    if (uiState.showCreationDialog) {
        DurationCreationDialog(
            isTracking = uiState.isTracking,
            startTime = uiState.startTime,
            elapsedMillis = uiState.elapsedMillis,
            formattedStartTime = { DateTimeUtils.millisToFormattedTime(it) },
            formattedElapsed = { DateTimeUtils.millisToFormattedDuration(it, isSecondsReq = true) },
            onStartWork = { viewModel.startTracking() },
            onStopAndSave = { viewModel.stopAndSave() },
            onCancel = { viewModel.cancelTracking() },
            onDismiss = { viewModel.updateShowCreationDialog(false) }
        )
    }
}

@Composable
fun DeleteDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    text = "Confirm"
                )
            }
        },
        modifier = Modifier.padding(12.dp),
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        },
        icon = {
            Icon(Icons.Default.Delete, null)
        },
        title = {
            Text(text = "Delete Duration(s)")
        },
        text = {
            Text(
                text = "Do you want to delete the duration(s),this operation can't be undone later"
            )
        },
        shape = RoundedCornerShape(16.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationCreationDialog(
    isTracking: Boolean,
    startTime: Long,
    elapsedMillis: Long,
    formattedStartTime: (Long) -> String,
    formattedElapsed: (Long) -> String,
    onStartWork: () -> Unit,
    onStopAndSave: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.clock_time)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isTracking,
        speed = 0.2f
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Track the Task Duration", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)

            if (!isTracking) {
                Button(onClick = onStartWork) { Text("Start Work") }
            }

            if (isTracking) {
                Text("Work started at ${formattedStartTime(startTime)}")

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                )

                Text(
                    text = "Duration : ${formattedElapsed(elapsedMillis)}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // Save the duration and stop
                Button(onClick = onStopAndSave) { Text("Save & Stop") }

                // Discard and stop
                Button(onClick = onCancel) { Text("Discard") }
            }
        }
    }
}