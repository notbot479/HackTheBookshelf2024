package kz.nikitos.hackingthebookshelf.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.nikitos.hackingthebookshelf.data.models.DebtData

@Composable
fun BooksScreen(books: List<DebtData>, onRegisterBookToRemindToReturn: (book: DebtData) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier, verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(8.dp)) {
        items(books) { book ->
            BookCard(book, onRegisterBookToRemindToReturn)
        }
    }
}

@Composable
fun BookCard(book: DebtData, onRegisterBookToRemindToReturn: (debtData: DebtData) -> Unit) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    ElevatedCard(onClick = { isExpanded = !isExpanded }, modifier = Modifier.fillMaxWidth()) {
        Text(text = "${book.bookData.author}: ${book.bookData.title}")

        Text(text = "Страниц: ${book.bookData.pages}")

        Text(text = "Взята: ${book.dateOfIssue}")

        Text(text = "Необходимо вернуть до: ${book.dateOfDeadline}")

        Text(text = "Взято экземпляров: ${book.available}")
        
        if (isExpanded) {
            Text(text = "Количество илюстраций: ${book.bookData.images}")
            Text(text = "Сертификационный номер ${book.bookData.certificateNumber}")
            Text(text = "Год сертификации ${book.bookData.certificationYear}")

            Button(onClick = { onRegisterBookToRemindToReturn(book) }) {
                Text(text = "Напомнить вернуть")
            }
        }
    }
}