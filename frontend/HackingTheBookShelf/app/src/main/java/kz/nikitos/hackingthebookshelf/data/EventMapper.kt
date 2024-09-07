package kz.nikitos.hackingthebookshelf.data

import kz.nikitos.hackingthebookshelf.data.models.EventData
import kz.nikitos.hackingthebookshelf.data.utils.TimestampToLocalDateTimeConverter
import kz.nikitos.hackingthebookshelf.domain.models.Event
import javax.inject.Inject

class EventMapper @Inject constructor(
    private val timestampToLocalDateTimeConverter: TimestampToLocalDateTimeConverter
) {
    operator fun invoke(events: List<EventData>): List<Event> =
        events.map(::mapSingle)

    private fun mapSingle(event: EventData): Event {
        val eventStart = timestampToLocalDateTimeConverter.convert(event.startTime)
        val eventEnd = timestampToLocalDateTimeConverter.convert(event.endTime)
        return Event(
            event.id,
            event.name,
            event.description,
            event.isRegisterAvailable,
            eventStart,
            eventEnd
        )
    }
}