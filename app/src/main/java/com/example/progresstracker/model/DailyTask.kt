package com.example.progresstracker.model

import android.icu.util.Calendar
import android.icu.util.ULocale

data class DailyTask(
    val id: Long = 0L,
    val title: String,
    val description: String,
    val remarks: String,
    val satisfyPercentage: SatisfyPercentage,
    val englishDate: Long,
    val durations: List<TaskDuration>
) {
    val islamicDate: String
        get() = calculateIslamicDate(englishDate)

    val totalTaskDuration =
        if(durations.isNotEmpty()) durations.map { it.durationTime }.reduce { acc, duration -> acc + duration } else 0
}

enum class SatisfyPercentage(val text: Int) {
    PER_0(0),
    PER_10(10),
    PER_20(20),
    PER_30(30),
    PER_40(40),
    PER_50(50),
    PER_60(60),
    PER_70(70),
    PER_80(80),
    PER_90(90),
    PER_100(100),
}

//data class IslamicDate(
//    val day: Int,
//    val month: Int,
//    val year: Int,
//    val monthName: String,
//    val formatedDate: String
//)

private val HIJRI_MONTH_NAMES = listOf(
    "Muharram", "Safar", "Rabi al-Awwal", "Rabi al-Thani",
    "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
    "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
)


fun calculateIslamicDate(epochMillis: Long): String {
    val islamicLocale = ULocale("ar@calendar=islamic-umalqura")
    val instance = Calendar.getInstance(islamicLocale)
    instance.timeInMillis = epochMillis
    val day = instance.get(Calendar.DAY_OF_MONTH)
    val month = instance.get(Calendar.MONTH)
    val year = instance.get(Calendar.YEAR)
    val monthName = HIJRI_MONTH_NAMES.getOrElse(month) { "unknown month" }
    val formatedDate = "$day $monthName $year AH"
    return formatedDate
}





