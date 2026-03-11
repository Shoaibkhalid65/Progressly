package com.example.progresstracker.practice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TimePickersPracticeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var buttonText by remember { mutableStateOf("") }
        var showTimePickerDialog by remember { mutableStateOf(false) }
        Button(
            onClick = {
                showTimePickerDialog = true
            }
        ) {
            Text(
                text = buttonText
            )
        }
        if (showTimePickerDialog) {
            TimePickerDialog(onDismiss = {
                showTimePickerDialog=false
            }, onConfirm = { hour,minute ->
                buttonText= "$hour:$minute"
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(onDismiss: () -> Unit,onConfirm : (Int, Int)-> Unit) {
    val state = rememberTimePickerState()
    TimePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                   onConfirm(state.hourInput,state.minute)
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