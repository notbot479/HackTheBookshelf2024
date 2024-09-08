package kz.nikitos.hackingthebookshelf.ui.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kz.nikitos.hackingthebookshelf.domain.models.Achievment

@Composable
fun AchievmentsScreen(achievments: List<Achievment>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(achievments) { achievment ->
            AchievmentCard(achievment)
        }
    }
}

@Composable
fun AchievmentCard(achievment: Achievment) {
    OutlinedCard(onClick = { /*TODO*/ }) {
        Text(text = achievment.title)
    }
}