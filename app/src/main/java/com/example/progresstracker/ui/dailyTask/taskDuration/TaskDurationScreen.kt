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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.progresstracker.model.TaskDuration
import com.example.progresstracker.ui.gaols.showToast

@Composable
fun TaskDurationScreen(
    viewModel: TaskDurationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

//    val durations by viewModel.durationsToShow.collectAsStateWithLifecycle()
//    val startTime by viewModel.startTime.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TaskDurationUiEvent.Success -> {
                    showToast(context, "Success: ${event.message}")
                }

                is TaskDurationUiEvent.Error -> {
                    showToast(context, "Error: ${event.message}")
                }
            }
        }
    }
    if(uiState.durations.isEmpty()){
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
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
    ) {
        items(uiState.durations, key = { it.id }) { duration ->
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineContent = {
                    Text(
                        text = "Duration Time : ${viewModel.millisToFormattedDuration(duration.durationTime)}"
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
                            text = "Start time ${viewModel.millisToFormattedTime(duration.startTime)}"
                        )

                        Text(
                            text = "End time ${viewModel.millisToFormattedTime(duration.endTime)}"
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

    if(uiState.showCreationDialog){
        DurationCreationDialog(
            onDismiss = {
                viewModel.updateShowCreationDialog(false)
            },
            onConfirm = { endTime ->
                viewModel.createTaskDuration(TaskDuration(startTime=uiState.startTime, endTime = endTime))
            },
            startTime = uiState.startTime,
            formattedStartTime = {
                viewModel.millisToFormattedTime(it)
            },
            formatedDurationTime = {
                viewModel.millisToFormattedDuration(it,true)
            },
            onStartTimeUpdated = {
                viewModel.saveStartTime(it)
            },
            onEndTimeUpdated = {
                viewModel.updateEndTime(it)
            }
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
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
    startTime: Long,
    formattedStartTime: (Long)->String,
    formatedDurationTime: (Long) -> String,
    onStartTimeUpdated: (Long) -> Unit,
    onEndTimeUpdated: (Long) -> Unit
) {
    val composition by rememberLottieComposition(
        spec= LottieCompositionSpec.RawRes(R.raw.clock_time)
    )
    val progress by animateLottieCompositionAsState(
        composition=composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 0.5f
    )

    var currentMillisTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(currentMillisTime) {
        currentMillisTime= System.currentTimeMillis()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Track the Task Duration",
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )

            Button(
                onClick = {
                   onStartTimeUpdated(System.currentTimeMillis())
                }
            ) {
                Text(
                    text = "Start Work"
                )
            }

            if(startTime!=0L) {
                Text(
                    text = "Work Started at ${formattedStartTime(startTime)}"
                )

                LottieAnimation(composition=composition,progress={progress}, modifier = Modifier.size(200.dp))

                Text(
                    text = "Duration : ${formatedDurationTime(currentMillisTime-startTime)}",
                    color = Color.Green,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    val endTime= System.currentTimeMillis()
                    onEndTimeUpdated(endTime)
                    onConfirm(endTime)
                    onDismiss()
                }
            ) {
                Text(
                    text = "End Work"
                )
            }

            Button(
                onClick = {
                   onDismiss()
                    onEndTimeUpdated(0L)
                    onStartTimeUpdated(0L)
                }
            ) {
                Text(
                    text = "Cancel Tracking"
                )
            }
        }
    }
}