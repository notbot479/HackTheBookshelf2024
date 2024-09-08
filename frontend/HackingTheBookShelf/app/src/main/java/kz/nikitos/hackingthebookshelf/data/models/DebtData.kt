package kz.nikitos.hackingthebookshelf.data.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DebtData(
    @SerialName("book") val bookData: BookData,
    @SerialName("count") val available: Int,
    @SerialName("date_of_issue") val dateOfIssue: LocalDate,
    @SerialName("date_of_deadline") val dateOfDeadline: LocalDate,
)