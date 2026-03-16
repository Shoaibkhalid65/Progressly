package com.example.progresstracker.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.progresstracker.data.local.db.entity.DailyTaskEntity
import com.example.progresstracker.model.DailyTask
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyTaskDao {

    @Upsert
    suspend fun upsertDailyTask(dailyTask: DailyTaskEntity): Long

    @Delete
    suspend fun deleteDailyTask(dailyTask: DailyTaskEntity): Int

    @Query("Delete from daily_tasks")
    suspend fun deleteAllDailyTasks(): Int

    @Query("Select * from daily_tasks where id=:dailyTaskId ")
    suspend fun getDailyTaskById(dailyTaskId: Long): DailyTaskEntity

    @Query("Select * from daily_tasks order by id")
    fun getAllDailyTasks(): Flow<List<DailyTaskEntity>>

    @Query("Select IfNull(Max(id),-1) from daily_tasks")
    fun getMaxId(): Flow<Long>
// function to get the real tasks only not the dummy one that created for specific reason
    @Query("Select * from daily_tasks where englishDate != 0 order by englishDate DESC")
    fun getRealDailyTasks(): Flow<List<DailyTaskEntity>>
}