package com.example.progresstracker.ui.taskcreation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.progresstracker.model.SatisfyPercentage
import com.example.progresstracker.navigation.Screen
import com.example.progresstracker.ui.gaols.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditTaskScreen(
    navHostController: NavHostController,
    viewModel: CreateEditTaskViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                CreateEditTaskUiEvent.NavigateToTasksScreen -> {
                    navHostController.navigate(Screen.DailyTaskScreen.route) {
                        popUpTo(Screen.CreateEditTaskScreen.route) {
                            inclusive = true
                        }
                    }
                }

                is CreateEditTaskUiEvent.Error -> {
                    showToast(context, event.message)
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEditMode) "Edit Task" else "Create Task"
                    )
                },
                windowInsets = WindowInsets()
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTaskDataTextField(
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                hint = "Enter title"
            )

            CustomTaskDataTextField(
                value = uiState.description,
                onValueChange = { viewModel.updateDescription(it) },
                hint = "Enter description",
                minLines = 2
            )

            CustomTaskDataTextField(
                value = uiState.remarks,
                onValueChange = { viewModel.updateRemarks(it) },
                hint = "Enter remarks",
                minLines = 2
            )

            Text(
                text = "How many percent you are satisfied with your performance today?"
            )

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = uiState.satisfyPercentage.text.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                IconButton(
                    onClick = {
                        viewModel.updateShowPercentageDialog(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = "select the satisfy percentage"
                    )
                }
            }

            uiState.taskDurations.forEachIndexed { index, state ->
                TaskDurationItem(
                    index = index,
                    taskDurationUiState = state,
                    onRemove = {
                        viewModel.removeTaskDuration(it)
                    },
                    updateShowTimePicker = {
                        viewModel.updateShowTimePickerDialog(it)
                    },
                    updateIsTimePickerForStartTime = {
                        viewModel.updateIsTimePickerForStartTime(it)
                    },
                    millisToFormatedTime = {
                        viewModel.millisToFormattedTime(it)
                    },
                    onDurationSelected = {
                        viewModel.updateSelectedDurationId(state.durationId)
                    },
                    millisToFormatedDuration = {
                        viewModel.millisToFormattedDuration(it)
                    }
                )

            }

            Button(
                onClick = {
                    viewModel.addTaskDuration()
                }
            ) {
                Text(
                    text = "Add Duration"
                )
            }


            Button(
                onClick = {
                    viewModel.createOrUpdateDailyTask(uiState.toModel())
                }
            ) {
                Text(
                    text = "Save Task"
                )
            }
        }
        if (uiState.showSelectSatisfyPerDialog) {
            SelectSatisfyPercentageDialog(
                onDismiss = {
                    viewModel.updateShowPercentageDialog(false)
                }
            ) { percentage ->
                viewModel.updateSatisfyPercentage(percentage)
            }
        }

        if (uiState.showTimePickerDialog) {
            val selectedDuration = uiState.taskDurations.find { it.durationId == uiState.selectedDurationId }

            selectedDuration?.let { state ->
                TimePickerDialog(
                    onDismiss = {
                        viewModel.updateShowTimePickerDialog(false)
                    }
                ) { hour, minute ->
                    val timeInMillis = viewModel.timePickerToMillis(hour, minute)
                    val startTimeToUpdate = uiState.isTimePickerForStartTime
                    if (startTimeToUpdate) {
                        viewModel.onTaskDurationUpdated(
                            taskDurationUiState = state.copy(
                                startTime = timeInMillis
                            )
                        )
                    } else {
                        viewModel.onTaskDurationUpdated(
                            taskDurationUiState = state.copy(
                                endTime = timeInMillis
                            )
                        )
                    }

                }
            }
        }

//        if(uiState.showTimePickerDialog){
//            TimePickerDialog(
//                onDismiss = {
//                    viewModel.updateShowTimePickerDialog(false)
//                }
//            ) { hour,minute ->
//                val timeInMillis = viewModel.timePickerToMillis(hour,minute)
//                viewModel.onTaskDurationUpdated()
//            }
//        }
    }
}


@Composable
fun TaskDurationItem(
    index: Int,
    taskDurationUiState: TaskDurationUiState,
    onRemove: (String) -> Unit,
    updateShowTimePicker: (Boolean) -> Unit,
    updateIsTimePickerForStartTime: (Boolean) -> Unit,
    millisToFormatedTime: (Long) -> String,
    onDurationSelected : () -> Unit,
    millisToFormatedDuration : (Long) -> String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Duration # ${index.plus(1)}"
            )

            IconButton(
                onClick = {
                    onRemove(taskDurationUiState.durationId)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SuggestionChip(
                onClick = {
                    onDurationSelected()
                    updateShowTimePicker(true)
                    updateIsTimePickerForStartTime(true)

                },
                label = {
                    Text(
                        text = if (taskDurationUiState.startTime == 0L) {
                            "Select Start Time"
                        } else {
                            millisToFormatedTime(taskDurationUiState.startTime)
                        }
                    )
                }
            )

            SuggestionChip(
                onClick = {
                    onDurationSelected()
                    updateShowTimePicker(true)
                    updateIsTimePickerForStartTime(false)
                },
                label = {
                    Text(
                        text = if (taskDurationUiState.endTime == 0L) {
                            "Select End Time"
                        } else {
                            millisToFormatedTime(taskDurationUiState.endTime)
                        }
                    )
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val crossDuration = taskDurationUiState.endTime - taskDurationUiState.startTime
            if (taskDurationUiState.startTime !=0L && taskDurationUiState.endTime !=0L) {
                Text(
                    text = "Total Duration",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    // may be there the other function should use (which will calculate the total time in readable format)
                    text = millisToFormatedDuration(crossDuration),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }


    }
}

//@Composable
//fun SelectTimeChip(timeName: String, time: Long, updateShowTimePicker:(Boolean)-> Unit,) {
//    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        SuggestionChip(
//            onClick = {
//                updateShowTimePicker(true)
//            },
//            label = {
//                Text(
//                    text = if (time==0L){
//                        "Select $timeName"
//                    }else{
//
//                    }
//                )
//            }
//        )
//
//
//    }
//}

@Composable
fun CustomTaskDataTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = hint
            )
        },
        minLines = minLines,
        maxLines = minLines + 1
    )
}

@Composable
fun SelectSatisfyPercentageDialog(onDismiss: () -> Unit, onConfirm: (SatisfyPercentage) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .wrapContentSize()
                .clip(RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SatisfyPercentage.entries.forEach { satisfyPercentage ->
                Text(
                    text = "${satisfyPercentage.text} Percent",
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = {
                            onConfirm(satisfyPercentage)
                            onDismiss()
                        }
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit) {
    val state = rememberTimePickerState()
    TimePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(state.hour, state.minute)
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        title = {
            Text(text = "Select the time")
        }
    ) {
        TimePicker(state)
    }
}

