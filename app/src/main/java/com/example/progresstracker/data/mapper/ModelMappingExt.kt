package com.example.progresstracker.data.mapper

import android.webkit.JavascriptInterface
import com.example.progresstracker.data.local.entity.DailyTaskEntity
import com.example.progresstracker.data.local.entity.GoalEntity
import com.example.progresstracker.data.local.entity.TaskDurationEntity
import com.example.progresstracker.model.DailyTask
import com.example.progresstracker.model.DifficultyLevel
import com.example.progresstracker.model.Goal
import com.example.progresstracker.model.ImportanceLevel
import com.example.progresstracker.model.SatisfyPercentage
import com.example.progresstracker.model.TaskDuration
import com.example.progresstracker.model.UrgencyLevel

fun DailyTaskEntity.toModel(durations: List<TaskDurationEntity>): DailyTask = DailyTask(
    id = id,
    title = title,
    description = description,
    remarks = remarks,
    satisfyPercentage = when (satisfyPercentage) {
        0 -> SatisfyPercentage.PER_0
        10 -> SatisfyPercentage.PER_10
        20 -> SatisfyPercentage.PER_20
        30 -> SatisfyPercentage.PER_30
        40 -> SatisfyPercentage.PER_40
        50 -> SatisfyPercentage.PER_50
        60 -> SatisfyPercentage.PER_60
        70 -> SatisfyPercentage.PER_70
        80 -> SatisfyPercentage.PER_80
        90 -> SatisfyPercentage.PER_90
        else -> SatisfyPercentage.PER_100
    },
    englishDate = englishDate,
    durations = durations.map { it.toModel() }
)

//fun List<DailyTaskEntity>.toModel(durations: List<TaskDurationEntity>)= map {
//    it.toModel(durations)
//}

fun DailyTask.toEntity() = DailyTaskEntity(
    id = id,
    title = title,
    description = description,
    remarks = remarks,
    satisfyPercentage = satisfyPercentage.text,
    englishDate = englishDate,
)


fun TaskDuration.toEntity(dailyTaskId: Long) = TaskDurationEntity(
    id = id,
    dailyTaskId = dailyTaskId,
    startTime = startTime,
    endTime = endTime,
    durationTime = durationTime
)

fun TaskDurationEntity.toModel() = TaskDuration(
    id = id,
    startTime = startTime,
    endTime = endTime
)

@JvmName("durationEntitiesToDurations")
fun List<TaskDurationEntity>.toModel()= map {
    it.toModel()
}



fun Goal.toEntity() = GoalEntity(
    id = id,
    createdAt = createdAt,
    title = title,
    description = description,
    isCompleted = isCompleted,
    expectedCompletionDate = expectedCompletionDate,
    completionDate = completionDate,
    difficultyLevel = difficultyLevel.name,
    importanceLevel = importanceLevel.name,
    urgencyLevel = urgencyLevel.name
)

fun GoalEntity.toModel() = Goal(
    id = id,
    createdAt = createdAt,
    title = title,
    description = description,
    isCompleted = isCompleted,
    expectedCompletionDate = expectedCompletionDate,
    completionDate = completionDate,
    difficultyLevel = when (difficultyLevel.uppercase()) {
        "HARD" -> DifficultyLevel.HARD
        "MEDIUM" -> DifficultyLevel.MEDIUM
        "EASY" -> DifficultyLevel.EASY
        else -> DifficultyLevel.EASY
    },
    importanceLevel = when (importanceLevel.uppercase()) {
        "VERY_IMPORTANT" -> ImportanceLevel.VERY_IMPORTANT
        "IMPORTANT" -> ImportanceLevel.IMPORTANT
        "AVERAGE" -> ImportanceLevel.AVERAGE
        else -> ImportanceLevel.AVERAGE
    },
    urgencyLevel = when (urgencyLevel.uppercase()) {
        "URGENT" -> UrgencyLevel.URGENT
        "AVERAGE" -> UrgencyLevel.AVERAGE
        "NOT_URGENT" -> UrgencyLevel.NOT_URGENT
        else -> UrgencyLevel.NOT_URGENT
    }
)

@JvmName("goalEntitiesToGoals")
fun List<GoalEntity>.toModel()= map {
    it.toModel()
}

