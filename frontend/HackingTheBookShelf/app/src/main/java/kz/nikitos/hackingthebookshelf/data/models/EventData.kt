package kz.nikitos.hackingthebookshelf.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventData(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("is_register_available") val isRegisterAvailable: Boolean,
    @SerialName("start_timestamp") val startTime: Long,
    @SerialName("end_timestamp") val endTime: Long,
)