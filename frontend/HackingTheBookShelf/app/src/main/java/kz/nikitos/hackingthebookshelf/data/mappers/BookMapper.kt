package kz.nikitos.hackingthebookshelf.data.mappers

import kz.nikitos.hackingthebookshelf.data.models.BookData
import kz.nikitos.hackingthebookshelf.data.utils.TimestampToLocalDateTimeConverter
import kz.nikitos.hackingthebookshelf.domain.models.Book
import javax.inject.Inject

class BookMapper @Inject constructor(
    private val timestampToLocalDateTimeConverter: TimestampToLocalDateTimeConverter
) {
    operator fun invoke(books: List<BookData>): List<Book> =
        books.map(::mapSingle)

    private fun mapSingle(book: BookData): Book {
        val returnDate = timestampToLocalDateTimeConverter.convert(book.returnDateTimeStamp)
        return Book(
            book.id,
            book.title,
            book.author,
            returnDate
        )
    }
}