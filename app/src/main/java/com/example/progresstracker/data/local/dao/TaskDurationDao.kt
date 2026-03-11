package com.example.progresstracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.progresstracker.data.local.entity.TaskDurationEntity
import com.example.progresstracker.model.DailyTask
import com.example.progresstracker.model.TaskDuration
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDurationDao {

    @Upsert
    suspend fun upsertTaskDuration(taskDuration: TaskDurationEntity): Long

    @Upsert
    suspend fun upsertAllTaskDurations(taskDurations: List<TaskDurationEntity>)

    @Delete
    suspend fun deleteTaskDuration(taskDuration: TaskDurationEntity): Int

    @Query("Delete from task_durations where dailyTaskId=:taskId")
    suspend fun deleteDurationsByTaskId(taskId: Long) : Int

    @Query("Select * from task_durations order by id")
    fun getAllDurations(): Flow<List<TaskDurationEntity>>

    @Query("Select IfNull(Max(id),-1) from task_durations")
    suspend fun getMaxId(): Long

    @Query("Select * from task_durations where id=:durationId")
    fun getDurationById(durationId: Long): Flow<TaskDurationEntity>

    @Query("Select * from task_durations where dailyTaskId=:dailyTaskId")
    fun getAllDurationsByTaskId(dailyTaskId: Long): Flow<List<TaskDurationEntity>>

}