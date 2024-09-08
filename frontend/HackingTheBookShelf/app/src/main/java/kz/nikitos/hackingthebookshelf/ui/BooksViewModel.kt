package kz.nikitos.hackingthebookshelf.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kz.nikitos.hackingthebookshelf.data.models.BookData
import kz.nikitos.hackingthebookshelf.data.models.DebtData
import kz.nikitos.hackingthebookshelf.domain.data_sources.BooksDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.CalendarDataSource
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val booksDataSource: BooksDataSource,
    private val calendarDataSource: CalendarDataSource
) : ViewModel() {
    private val _books = MutableLiveData<List<DebtData>>(emptyList())
    val books: LiveData<List<DebtData>> = _books

    fun getBooks() {
//        viewModelScope.launch {
//            _books.postValue(booksDataSource.getDebtBooks())
//        }
        _books.postValue(listOf(
            DebtData(BookData(1, "title", "author", "202020", 2024, 3, 3), 3, LocalDate(2024, 12, 12), LocalDate(2024, 12, 13))
        ))
    }

    fun registerReminder(book: DebtData) {
//        val zone: ZoneOffset = OffsetDateTime.now().offset
        calendarDataSource.writeEvent(
            book.dateOfIssue.toEpochDays().toLong() * 24L * 60L * 60L  * 1000L,
            book.dateOfDeadline.toEpochDays().toLong() * 24L * 60L * 60L  * 1000L,
            "Вернуть книгу",
            book.bookData.title + " " + book.bookData.author
        )
    }
}