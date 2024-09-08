package kz.nikitos.hackingthebookshelf.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import kz.nikitos.hackingthebookshelf.domain.models.Event
import kz.nikitos.hackingthebookshelf.ui.Events
import kz.nikitos.hackingthebookshelf.ui.models.EventType
import java.time.format.DateTimeFormatter

private const val TAG = "HackTheBookShelfEventsScreen"
private const val EVENTS_SCREEN_PAGES = 2

@Composable
fun EventsScreen(
    allEvents: Events,
    subscribedEvents: List<Event>,
    onRegistration: ((event: Event) -> Unit)?,
    onUnsubscribe: ((event: Event) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { EVENTS_SCREEN_PAGES })

    val pages = listOf<@Composable () -> Unit>(
        { AllEvents(events = allEvents, onRegistration = onRegistration, "Зарегистрироваться", modifier = modifier) },
        {
            EventsList(
                events = subscribedEvents,
                listTitle = "Мероприятия, где вы принимали участия",
                onUnsubscribe,
                "Отписаться",
            )
        }
    )
//    LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        item {
//            Row {
//                repeat(EVENTS_SCREEN_PAGES) { pageIndex ->
//                    val pageNum = pageIndex + 1
//                    Button(onClick = { composableScope.launch { pagerState.scrollToPage(pageIndex) } }) {
//                        Text(
//                            text = pageNum.toString(),
//                            fontWeight = if (pageIndex == pagerState.currentPage) FontWeight.Bold else FontWeight.Thin
//                        )
//                    }
//                }
//            }
//        }
//        item {
    HorizontalPager(state = pagerState, Modifier.fillMaxSize()) { pageIndex ->
        pages[pageIndex]()
    }
//        }
//    }
}

@Composable
fun AllEvents(events: Events, onRegistration: ((event: Event) -> Unit)?, buttonLabel: String, modifier: Modifier) {
    val upcomingToday = events[EventType.UpcomingToday]
    val upcoming = events[EventType.Upcoming]
    val pastToday = events[EventType.PastToday]

    val eventBundle = listOf(
        EventBundle(upcomingToday, "Ближайшие мероприятия"),
        EventBundle(upcoming, "Предстоящие мероприятия"),
        EventBundle(pastToday, "Идущие мероприятия")
    )
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(8.dp)) {
        items(eventBundle) { bundle ->
            Text(text = bundle.title)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                bundle.events?.forEach {
                    EventCard(event = it, onRegistration, buttonLabel)
                }
            }
        }
    }
}

@Composable
fun EventsList(
    events: List<Event>?,
    listTitle: String,
    onRegistration: ((event: Event) -> Unit)?,
    buttonLabel: String,
    modifier: Modifier = Modifier,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        item {
            Text(text = listTitle, fontWeight = FontWeight.ExtraBold)
        }
        items(events ?: emptyList()) { event ->
            EventCard(event, onRegistration, buttonLabel)
        }
    }
}

@Composable
fun EventCard(event: Event, onRegistration: ((event: Event) -> Unit)?, buttonLabel: String) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy: EEEE HH:mm")
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    ElevatedCard(onClick = {
        isExpanded = !isExpanded
        Log.d(TAG, "EventCard: ${event.name} $isExpanded")
    }) {
        Text(
            text = event.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(text = event.description, modifier = Modifier.fillMaxWidth())

        Row {
            Text(text = "Дата начала: ")
            Text(text = event.startTime.format(dateFormatter))
        }

        Row {
            Text(text = "Дата окончания: ")
            Text(text = event.endTime.format(dateFormatter))
        }

        if (isExpanded && event.isRegisterAvailable) {
            Button(
                onClick = { onRegistration?.invoke(event) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = buttonLabel)
            }
        }
    }
}

data class EventBundle(val events: List<Event>?, val title: String)