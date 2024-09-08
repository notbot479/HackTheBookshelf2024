package kz.nikitos.hackingthebookshelf.data.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class TimestampToLocalDateTimeConverter @Inject constructor() {
    fun convert(timestamp: Long): LocalDateTime {
        val instant = Instant.ofEpochSecond(timestamp)
        val zone = ZoneId.systemDefault()
        return LocalDateTime.ofInstant(instant, zone)
    }
}