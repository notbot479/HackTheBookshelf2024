package kz.nikitos.hackingthebookshelf.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.nikitos.hackingthebookshelf.di.FakeEventsDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.EventsDataSource
import kz.nikitos.hackingthebookshelf.domain.models.Event
import kz.nikitos.hackingthebookshelf.ui.models.EventType
import javax.inject.Inject

typealias Events = Map<EventType, List<Event>>

@HiltViewModel
class EventsViewModel @Inject constructor(
    @FakeEventsDataSource private val eventsDataSource: EventsDataSource
) : ViewModel() {
    private val _allEvents = MutableLiveData<Events>()
    val allEvents: LiveData<Events> = _allEvents

    private val _subscribedEvents = MutableLiveData<List<Event>>()
    val subscribedEvent: LiveData<List<Event>> = _subscribedEvents

    fun getEvents() {
        viewModelScope.launch {
            val upcoming = async { eventsDataSource.getUpcomingEvents() }
            val upcomingToday = async { eventsDataSource.getUpcomingTodayEvents() }
            val pastToday = async { eventsDataSource.getStartedEvents() }
            val subscribedEvents = async { eventsDataSource.getMySubscriptions() }

            _allEvents.postValue(
                mapOf(
                    EventType.Upcoming to upcoming.await(),
                    EventType.UpcomingToday to upcomingToday.await(),
                    EventType.PastToday to pastToday.await()
                )
            )

            _subscribedEvents.postValue(subscribedEvents.await())
        }
    }

    fun registerOnEvent(id: Int) {
        viewModelScope.launch {
            eventsDataSource.subscribeToEvent(id)
        }
    }
}