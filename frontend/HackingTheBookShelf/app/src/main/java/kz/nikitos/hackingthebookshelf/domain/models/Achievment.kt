package kz.nikitos.hackingthebookshelf.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Achievment(
    @SerialName("name") val title: String,
    @SerialName("created_at_timestamp") val createdAt: Long
)
