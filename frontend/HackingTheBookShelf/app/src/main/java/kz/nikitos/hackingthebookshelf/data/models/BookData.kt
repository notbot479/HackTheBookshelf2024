package kz.nikitos.hackingthebookshelf.data.models

data class BookData(
    val id: Int,
    val title: String,
    val author: String,
    val returnDateTimeStamp: Long
)