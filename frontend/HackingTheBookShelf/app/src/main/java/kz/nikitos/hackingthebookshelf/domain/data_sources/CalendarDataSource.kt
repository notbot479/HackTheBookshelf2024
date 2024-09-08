package kz.nikitos.hackingthebookshelf.domain.data_sources

interface CalendarDataSource {
    fun writeEvent(beginTimeMillis: Long, endTimeMillis: Long, title: String, description: String, location: String = "Библиотека им. Горькова")
}