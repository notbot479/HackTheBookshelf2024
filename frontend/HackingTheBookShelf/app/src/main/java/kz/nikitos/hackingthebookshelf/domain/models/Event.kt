package kz.nikitos.hackingthebookshelf.domain.models

import java.time.LocalDateTime

data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val isRegisterAvailable: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
)