package com.example.progresstracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.progresstracker.data.local.dao.DailyTaskDao
import com.example.progresstracker.data.local.dao.GoalDao
import com.example.progresstracker.data.local.dao.TaskDurationDao
import com.example.progresstracker.data.local.entity.DailyTaskEntity
import com.example.progresstracker.data.local.entity.GoalEntity
import com.example.progresstracker.data.local.entity.TaskDurationEntity


@Database(
    entities = [DailyTaskEntity::class, TaskDurationEntity::class, GoalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dailyTaskDao: DailyTaskDao
    abstract val durationDao: TaskDurationDao
    abstract val goalDao: GoalDao
}