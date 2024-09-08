package kz.nikitos.hackingthebookshelf.domain.use_cases

import kz.nikitos.hackingthebookshelf.di.RealEventsDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.CalendarDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.EventsDataSource
import kz.nikitos.hackingthebookshelf.domain.models.Event
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class SubscribeToEvent @Inject constructor(
    @RealEventsDataSource private val eventsDataSource: EventsDataSource,
    private val calendarDataSource: CalendarDataSource
) {
    suspend operator fun  invoke(event: Event) {
        eventsDataSource.subscribeToEvent(event.id)
        val zone: ZoneOffset = OffsetDateTime.now().offset
        calendarDataSource.writeEvent(
            event.startTime.toEpochSecond(zone) * 1000L,
            event.endTime.toEpochSecond(zone) * 1000L,
            event.name,
            event.description
        )
    }
}