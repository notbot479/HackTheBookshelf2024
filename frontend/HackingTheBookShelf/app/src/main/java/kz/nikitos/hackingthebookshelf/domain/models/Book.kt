package kz.nikitos.hackingthebookshelf.domain.models

import java.time.LocalDateTime

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val returnDate: LocalDateTime
)