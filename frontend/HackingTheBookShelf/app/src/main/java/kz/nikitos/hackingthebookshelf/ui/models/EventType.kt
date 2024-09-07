package kz.nikitos.hackingthebookshelf.ui.models

sealed interface EventType {
    object Upcoming : EventType
    object PastToday : EventType
    object UpcomingToday : EventType
}