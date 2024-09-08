package kz.nikitos.hackingthebookshelf.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookData(
    val id: Int,
    @SerialName("name") val title: String,
    val author: String,
    @SerialName("certificate_number") val certificateNumber: String,
    @SerialName("certification_year") val certificationYear: Int,
    @SerialName("number_of_printed_pages") val pages: Int,
    @SerialName("number_of_images") val images: Int
)