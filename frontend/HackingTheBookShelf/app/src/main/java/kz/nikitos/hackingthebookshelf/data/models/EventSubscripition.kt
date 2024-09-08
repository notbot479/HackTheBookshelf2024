package kz.nikitos.hackingthebookshelf.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventSubscripition(@SerialName("firebase_push_hash") val firebaseMessagingToken: String, @SerialName("id") val eventId: Int)