package kz.nikitos.hackingthebookshelf.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.nikitos.hackingthebookshelf.data.utils.TimestampToLocalDateTimeConverter
import kz.nikitos.hackingthebookshelf.domain.models.Achievment
import java.time.format.DateTimeFormatter

@Composable
fun AchievmentsScreen(achievments: List<Achievment>, modifier: Modifier = Modifier) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(8.dp)) {
        items(achievments) { achievment ->
            AchievmentCard(achievment)
        }
    }
}

@Composable
fun AchievmentCard(achievment: Achievment) {
    val timestampToLocalDateTimeConverter = TimestampToLocalDateTimeConverter()
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy: EEEE HH:mm")
    val achievedDate = timestampToLocalDateTimeConverter.convert(achievment.createdAt)
    OutlinedCard(onClick = { /*TODO*/ }) {
        Text(text = achievment.title)
        Text(text = "Дата получения: ${achievedDate.format(dateFormatter)}")
    }
}