package com.example.progresstracker.data.local.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.progresstracker.data.local.db.dao.DailyTaskDao
import com.example.progresstracker.data.local.db.dao.GoalDao
import com.example.progresstracker.data.local.db.dao.TaskDurationDao
import com.example.progresstracker.data.local.db.entity.DailyTaskEntity
import com.example.progresstracker.data.local.db.entity.GoalEntity
import com.example.progresstracker.data.local.db.entity.TaskDurationEntity


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