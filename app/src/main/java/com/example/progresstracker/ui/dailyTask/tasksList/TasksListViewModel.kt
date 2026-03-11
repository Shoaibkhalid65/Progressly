package com.example.progresstracker.ui.dailyTask.tasksList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progresstracker.data.repository.DailyTaskRepository
import com.example.progresstracker.model.DailyTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TasksListViewModel @Inject constructor(
    private val repository: DailyTaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksListUiState())
    val uiState = _uiState.asStateFlow()


    private val _uiEvent = MutableSharedFlow<TasksListUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val dailyTasks = repository.observeAllRealTasks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadDailyTasks()
    }


    private fun loadDailyTasks() {
        viewModelScope.launch {
            repository.observeAllRealTasks().sortTasks(_uiState.value.sortOption).collect { dailyTasks ->
//                val query=_uiState.value.searchQuery
//                val filteredTasks= if(query.isEmpty()) dailyTasks else dailyTasks.filter { it.title.contains(_uiState.value.searchQuery) }
                _uiState.update {
                    it.copy(tasks = dailyTasks, isLoading = false)
                }
            }
        }
    }

    private fun Flow<List<DailyTask>>.sortTasks(sortOption: SortOption): Flow<List<DailyTask>> {
        return map { tasks ->
            when (sortOption) {
                SortOption.NEWEST_FIRST -> {
                    tasks
                }

                SortOption.OLDEST_FIRST -> {
                    tasks.sortedBy { it.englishDate }
                }

                SortOption.WORK_TIME_HIGH_TO_LOW -> {
                    tasks.sortedByDescending { it.totalTaskDuration }
                }

                SortOption.WORK_TIME_LOW_TO_HIGH -> {
                    tasks.sortedBy { it.totalTaskDuration }
                }

                SortOption.SATIS_PER_HIGH_TO_LOW -> {
                    tasks.sortedByDescending { it.satisfyPercentage.text }
                }

                SortOption.SATIS_PER_LOW_TO_HIGH -> {
                    tasks.sortedBy { it.satisfyPercentage.text }
                }
            }
        }
    }



    fun onUpdateTaskClick(taskId: Long) {
        viewModelScope.launch {
            _uiEvent.emit(
                TasksListUiEvent.NavigateToEditTask(taskId)
            )
        }
    }

    fun onCreateTaskClick() {
        viewModelScope.launch {
            _uiEvent.emit(
                TasksListUiEvent.NavigateToCreateTask
            )
        }
    }


    fun deleteDailyTask(taskId: Long) {
        viewModelScope.launch {
            if (taskId != -1L) {
                val dailyTask = repository.getDailyTaskById(taskId)
                repository.deleteDailyTask(dailyTask)
                    .onSuccess { tasksDeleted ->
                        if (tasksDeleted == 0) {
                            _uiEvent.emit(
                                TasksListUiEvent.Error("task not deleted")
                            )
                        } else {
                            _uiEvent.emit(
                                TasksListUiEvent.Success("task deleted successfully")
                            )
                        }

                    }
                    .onFailure {
                        _uiEvent.emit(
                            TasksListUiEvent.Error(it.message ?: "unknown error message")
                        )
                    }
            }
        }
    }

    fun deleteAllDailyTasks() {
        viewModelScope.launch {
            repository.deleteAllDailyTasks()
                .onSuccess { tasksDeleted ->
                    if (tasksDeleted == 0) {
                        _uiEvent.emit(
                            TasksListUiEvent.Error("tasks not deleted")
                        )
                    } else {
                        _uiEvent.emit(
                            TasksListUiEvent.Success("$tasksDeleted tasks deleted successfully")
                        )
                    }
                }
                .onFailure {
                    _uiEvent.emit(
                        TasksListUiEvent.Error(it.message ?: "unknown error message")
                    )
                }
        }
    }

    fun updateShowDeleteDialog(showDialog: Boolean) {
        _uiState.update {
            it.copy(showDeleteDialog = showDialog)
        }
    }

    fun millisToFormattedTime(millis: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(millis),
            ZoneId.systemDefault()
        )
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            localDateTime.hour,
            localDateTime.minute
        )
    }


    fun millisToFormattedDuration(millis: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(millis),
            ZoneId.of("UTC")
        )
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            localDateTime.hour,
            localDateTime.minute
        )
    }

    fun updateShowDeleteAllDialog(showDialog: Boolean) {
        _uiState.update {
            it.copy(showDeleteAllDialog = showDialog)
        }
    }

    fun updateTaskToDeleteId(taskId: Long) {
        _uiState.update {
            it.copy(taskToDeleteId = taskId)
        }
    }

    fun updateSearchQuery(searchQuery: String) {
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    fun updateSortOption(sortOption: SortOption) {
        _uiState.update {
            it.copy(sortOption = sortOption)
        }
    }

    fun updateShowSortDropDown(showDropDown: Boolean){
        _uiState.update {
            it.copy(showSortDropDown = showDropDown)
        }
    }

    fun formatedDate(epochMillis: Long, isOnlyDateRequired: Boolean = false): String {
        val instant = Instant.ofEpochMilli(epochMillis)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter =
            if (isOnlyDateRequired) DateTimeFormatter.ofPattern("dd MMM yyyy") else DateTimeFormatter.ofPattern(
                "dd MMM yyyy, hh:mm a"
            )
        return localDateTime.format(formatter)
    }


}

data class TasksListUiState(
    val isLoading: Boolean = true,
    val tasks: List<DailyTask> = emptyList(),
    val showDeleteDialog: Boolean = false,
    val showDeleteAllDialog: Boolean = false,
    val taskToDeleteId: Long = -1L,
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.NEWEST_FIRST,
    val showSortDropDown : Boolean = false
)

sealed class TasksListUiEvent() {
    data class NavigateToEditTask(val taskId: Long) : TasksListUiEvent()
    object NavigateToCreateTask : TasksListUiEvent()
    data class Error(val message: String) : TasksListUiEvent()
    data class Success(val message: String) : TasksListUiEvent()
}

enum class SortOption(val text: String) {
    NEWEST_FIRST("Newest First"),
    OLDEST_FIRST("Oldest First"),
    WORK_TIME_HIGH_TO_LOW("Work Time Desc"),
    WORK_TIME_LOW_TO_HIGH("Work Time Asc"),
    SATIS_PER_HIGH_TO_LOW("Satisfy Desc"),
    SATIS_PER_LOW_TO_HIGH("Satisfy Asc")
}

