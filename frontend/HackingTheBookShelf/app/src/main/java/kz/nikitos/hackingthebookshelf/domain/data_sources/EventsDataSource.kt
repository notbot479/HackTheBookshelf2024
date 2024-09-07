package kz.nikitos.hackingthebookshelf.domain.data_sources

import kz.nikitos.hackingthebookshelf.domain.models.Event

interface EventsDataSource {
    suspend fun getAllEvents(): List<Event>
    suspend fun getUpcomingEvents(): List<Event>
    suspend fun getUpcomingTodayEvents(): List<Event>
    suspend fun getStartedEvents(): List<Event>
    suspend fun registerPossibleEvents(): List<Event>
    suspend fun subscribeToEvent(eventId: Int)
}