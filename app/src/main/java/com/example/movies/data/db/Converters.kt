package com.example.movies.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun listOfIntsToString(list: List<Int>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun stringToListOfInts(string: String): List<Int> {
        return Json.decodeFromString<List<Int>>(string)
    }
}