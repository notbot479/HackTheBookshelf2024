package kz.nikitos.hackingthebookshelf.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kz.nikitos.hackingthebookshelf.domain.models.Event
import kz.nikitos.hackingthebookshelf.ui.Events
import kz.nikitos.hackingthebookshelf.ui.models.EventType

private const val TAG = "HackTheBookShelfEventsScreen"
private const val EVENTS_SCREEN_PAGES = 2

@Composable
fun EventsScreen(allEvents: Events, subscribedEvents: List<Event>, onRegistration: ((eventId: Int) -> Unit)?, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { EVENTS_SCREEN_PAGES})
    val composableScope = rememberCoroutineScope()

    val pages = listOf<@Composable () -> Unit>(
        { AllEvents(events = allEvents, onRegistration = onRegistration, modifier = modifier) },
        { EventsList(
            events = subscribedEvents,
            listTitle = "Мероприятия, где вы принимали участия",
            onRegistration = null
        ) }
    )
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            repeat(EVENTS_SCREEN_PAGES) { pageIndex ->
                val pageNum = pageIndex+1
                Button(onClick = { composableScope.launch { pagerState.scrollToPage(pageIndex) } }) {
                    Text(text = pageNum.toString(), fontWeight = if (pageIndex == pagerState.currentPage) FontWeight.Bold else FontWeight.Thin)
                }
            }
        }

        HorizontalPager(state = pagerState, Modifier.fillMaxSize()) { pageIndex ->
            pages[pageIndex]()
        }
    }
}

@Composable
fun AllEvents(events: Events, onRegistration: ((eventId: Int) -> Unit)?, modifier: Modifier) {
    val upcomingToday = events[EventType.UpcomingToday]
    val upcoming = events[EventType.Upcoming]
    val pastToday = events[EventType.PastToday]

    val eventBundle = listOf(
        EventBundle(upcomingToday, "Ближайшие мероприятия"),
        EventBundle(upcoming, "Предстоящие мероприятия"),
        EventBundle(pastToday, "Недавно прошедшие")
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        eventBundle.forEach {
            EventsList(events = it.events, listTitle = it.title, onRegistration)
        }
    }
}

@Composable
fun EventsList(events: List<Event>?, listTitle: String, onRegistration: ((eventId: Int) -> Unit)?, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = listTitle, fontWeight = FontWeight.ExtraBold)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(events ?: emptyList()) { event ->
                EventCard(event, onRegistration)
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onRegistration: ((eventId: Int) -> Unit)?) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    ElevatedCard(onClick = {
        isExpanded = !isExpanded
        Log.d(TAG, "EventCard: ${event.name} $isExpanded")
    }) {
        Column {
            Text(
                text = event.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(text = event.description, modifier = Modifier.fillMaxWidth())

            Row {
                Text(text = "Дата начала")
                Text(text = event.startTime.toString())
            }

            Row {
                Text(text = "Дата окончания")
                Text(text = event.startTime.toString())
            }

            if (isExpanded && event.isRegisterAvailable) {
                Button(onClick = { onRegistration?.invoke(event.id) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Зарегистрироваться")
                }
            }
        }
    }
}

data class EventBundle(val events: List<Event>?, val title: String)