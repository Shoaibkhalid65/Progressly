package com.example.progresstracker.ui.dailyTask.taskDuration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progresstracker.data.repository.DailyTaskRepository
import com.example.progresstracker.model.TaskDuration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TaskDurationViewModel @Inject constructor(
    private val repository: DailyTaskRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDurationUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<TaskDurationUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    init {
        loadTaskDurations()
        loadStartTime()
    }


    private fun loadTaskDurations() {
        viewModelScope.launch {
            repository.observeDurationsForMaxTaskIdToShow().collect {durations ->
                _uiState.update {
                    it.copy(durations = durations, isLoading = false)
                }
            }
        }
    }

    private fun loadStartTime() {
        viewModelScope.launch {
            repository.observeStartTime().collect { startTime ->
                _uiState.update {
                    it.copy(startTime = startTime)
                }
            }
        }
    }

    fun updateShowCreationDialog(showDialog: Boolean) {
        _uiState.update {
            it.copy(showCreationDialog = showDialog)
        }
    }

    fun updateShowDeletionDialog(showDialog: Boolean) {
        _uiState.update {
            it.copy(showDeleteDialog = showDialog)
        }
    }

    fun updateShowAllDeletionDialog(showDialog: Boolean) {
        _uiState.update {
            it.copy(showDeleteAllDialog = showDialog)
        }
    }

    fun updateDurationToDeleteId(durationId: Long) {
        _uiState.update {
            it.copy(durationToDeleteId = durationId)
        }
    }

    fun updateEndTime(endTime: Long) {
        _uiState.update {
            it.copy(endTime = endTime)
        }
    }

    fun saveStartTime(millis: Long) {
        viewModelScope.launch {
            repository.createOrEditStartTime(millis)
        }
    }


    fun createTaskDuration(taskDuration: TaskDuration) {
        viewModelScope.launch {
            if (taskDuration.startTime != 0L && taskDuration.endTime != 0L) {
                repository.createOrUpdateTaskDuration(taskDuration)
                    .onSuccess {
                        _uiEvent.emit(TaskDurationUiEvent.Success("Duration created of id:$it"))
                    }
                    .onFailure {
                        _uiEvent.emit(
                            TaskDurationUiEvent.Error(
                                it.message ?: "Unknown Error Message"
                            )
                        )
                    }
                saveStartTime(0L)
                updateEndTime(0L)
            } else {
                _uiEvent.emit(
                    TaskDurationUiEvent.Error(
                        "Start or End Time is empty \n start time : ${taskDuration.startTime} \n end time : ${taskDuration.endTime}"
                    )
                )
            }
        }
    }

    fun deleteTaskDuration(durationId: Long) {
        viewModelScope.launch {
            if (durationId != -1L) {
                val taskDuration = repository.observeDurationById(durationId).first()
                repository.deleteTaskDuration(taskDuration)
                    .onSuccess { noOfDurationsDeleted ->
                        when (noOfDurationsDeleted) {
                            0 -> {
                                _uiEvent.emit(TaskDurationUiEvent.Error("No Duration Deleted"))
                            }

                            1 -> {
                                _uiEvent.emit(TaskDurationUiEvent.Success("Duration Deleted"))
                            }

                            else -> {
                                _uiEvent.emit(TaskDurationUiEvent.Error("Multiple Durations Deleted"))
                            }
                        }
                    }
            } else {
                _uiEvent.emit(TaskDurationUiEvent.Error("Duration id not found to delete duraiton"))
            }
        }
    }

    fun deleteTaskDurationsOfMaxId() {
        viewModelScope.launch {
            repository.deleteAllTaskDurationsOfMaxId()
                .onSuccess { noOfDurationsDeleted ->
                    when (noOfDurationsDeleted) {
                        0 -> {
                            _uiEvent.emit(TaskDurationUiEvent.Error("No Duration Deleted"))
                        }

                        else -> {
                            _uiEvent.emit(TaskDurationUiEvent.Success("$noOfDurationsDeleted Durations Deleted"))
                        }
                    }
                }
        }
    }

}

data class TaskDurationUiState(
    val isLoading: Boolean = true,
    val durations: List<TaskDuration> = emptyList(),
    val showCreationDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showDeleteAllDialog: Boolean = false,
    val durationToDeleteId: Long = -1L,
    val startTime: Long = 0L,
    val endTime: Long = 0L
)

fun TaskDurationUiState.toModel() = TaskDuration(
    startTime = startTime,
    endTime = endTime
)

sealed class TaskDurationUiEvent {
    data class Success(val message: String) : TaskDurationUiEvent()
    data class Error(val message: String) : TaskDurationUiEvent()
}

