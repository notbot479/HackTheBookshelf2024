package kz.nikitos.hackingthebookshelf.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.nikitos.hackingthebookshelf.di.RealEventsDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.EventsDataSource
import kz.nikitos.hackingthebookshelf.domain.models.Event
import kz.nikitos.hackingthebookshelf.domain.use_cases.SubscribeToEvent
import kz.nikitos.hackingthebookshelf.ui.models.EventType
import javax.inject.Inject

typealias Events = Map<EventType, List<Event>>

@HiltViewModel
class EventsViewModel @Inject constructor(
    @RealEventsDataSource private val eventsDataSource: EventsDataSource,
    private val subscribeToEvent: SubscribeToEvent
) : ViewModel() {
    private val _allEvents = MutableLiveData<Events>()
    val allEvents: LiveData<Events> = _allEvents

    private val _subscribedEvents = MutableLiveData<List<Event>>()
    val subscribedEvent: LiveData<List<Event>> = _subscribedEvents

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getEvents() {
        _errorMessage.postValue("")
        viewModelScope.launch {
            try {
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
            } catch (e: Throwable) {
                _errorMessage.postValue("Something is wrong with request or internet")
            }
        }
    }

    fun registerOnEvent(event: Event) {
        viewModelScope.launch {
            subscribeToEvent(event)
        }
    }

    fun unsubscribeEvent(event: Event) {
        viewModelScope.launch {
            eventsDataSource.unsubscribeEvent(event.id)
        }
    }
}