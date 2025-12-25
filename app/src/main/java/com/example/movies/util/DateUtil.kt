package com.example.movies.util

import android.icu.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val inputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val outputDateFormatter = DateFormat.getDateInstance()

fun String.toFormattedDate(): String {
    val date = LocalDate.parse(this, inputDateFormatter)
    return outputDateFormatter.format(date)
}