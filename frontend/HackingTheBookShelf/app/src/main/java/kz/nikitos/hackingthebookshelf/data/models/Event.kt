package kz.nikitos.hackingthebookshelf.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Event(
    val id: Int,
    val name: String,
    val description: String,
    @Contextual @SerialName("start_time") val startTime: LocalDateTime,
    @Contextual @SerialName("end_time") val endTime: LocalDateTime
)