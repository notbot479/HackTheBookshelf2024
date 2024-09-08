package kz.nikitos.hackingthebookshelf.data.data_sources

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.core.os.bundleOf
import dagger.hilt.android.qualifiers.ApplicationContext
import kz.nikitos.hackingthebookshelf.domain.data_sources.CalendarDataSource
import javax.inject.Inject

class IntentCalendarDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : CalendarDataSource {
    override fun writeEvent(
        beginTimeMillis: Long,
        endTimeMillis: Long,
        title: String,
        description: String,
        location: String
    ) {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtras(
            bundleOf(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME to beginTimeMillis,
                CalendarContract.EXTRA_EVENT_END_TIME to endTimeMillis,
                CalendarContract.Events.TITLE to title,
                CalendarContract.Events.DESCRIPTION to description,
                CalendarContract.Events.EVENT_LOCATION to location,
                CalendarContract.Events.AVAILABILITY to CalendarContract.Events.AVAILABILITY_BUSY
            )
        )
        context.startActivity(intent)
    }
}