package kz.nikitos.hackingthebookshelf.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(val username: String, val password: String)
