package kz.nikitos.hackingthebookshelf.data.data_sources

import kz.nikitos.hackingthebookshelf.domain.data_sources.EventsDataSource
import kz.nikitos.hackingthebookshelf.domain.models.Event
import java.time.LocalDateTime
import javax.inject.Inject

private val upcoming = listOf(
    Event(
        1,
        "1",
        "1",
        true,
        LocalDateTime.of(2024, 9, 21, 1, 1),
        LocalDateTime.of(2024, 9, 21, 1, 2)
    ),
    Event(
        2,
        "2",
        "2",
        true,
        LocalDateTime.of(2024, 9, 22, 1, 1),
        LocalDateTime.of(2024, 9, 22, 1, 2)
    )
)

private val upcomingToday = listOf(
    Event(
        3,
        "3",
        "3",
        true,
        LocalDateTime.of(2024, 9, 21, 1, 1),
        LocalDateTime.of(2024, 9, 21, 1, 2)
    ),
    Event(
        4,
        "4",
        "4",
        true,
        LocalDateTime.of(2024, 9, 8, 1, 1),
        LocalDateTime.of(2024, 9, 8, 1, 2)
    )
)

private val pastToday = listOf(
    Event(
        3,
        "3",
        "3",
        false,
        LocalDateTime.of(2024, 9, 21, 0, 1),
        LocalDateTime.of(2024, 9, 21, 0, 2)
    ),
    Event(
        4,
        "4",
        "4",
        false,
        LocalDateTime.of(2024, 9, 8, 0, 1),
        LocalDateTime.of(2024, 9, 8, 0, 2)
    )
)

class FakeEventsDataSource @Inject constructor() : EventsDataSource {
    override suspend fun getAllEvents(): List<Event> {
        return upcoming + upcomingToday + pastToday
    }

    override suspend fun getUpcomingEvents(): List<Event> {
        return upcoming
    }

    override suspend fun getUpcomingTodayEvents(): List<Event> {
        return upcomingToday
    }

    override suspend fun getStartedEvents(): List<Event> {
        return pastToday
    }

    override suspend fun registerPossibleEvents(): List<Event> {
        return upcoming + upcomingToday
    }

    override suspend fun subscribeToEvent(eventId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getMySubscriptions(): List<Event> {
        return emptyList()
    }

    override suspend fun unsubscribeEvent(eventId: Int) {
        TODO("Not yet implemented")
    }
}