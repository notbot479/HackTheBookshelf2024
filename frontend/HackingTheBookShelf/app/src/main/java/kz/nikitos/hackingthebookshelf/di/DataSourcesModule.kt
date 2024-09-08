package kz.nikitos.hackingthebookshelf.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kz.nikitos.hackingthebookshelf.data.data_sources.DataStorageUserCredentialsDataSource
import kz.nikitos.hackingthebookshelf.data.data_sources.FirebaseNotificationTokenStorage
import kz.nikitos.hackingthebookshelf.data.data_sources.IntentCalendarDataSource
import kz.nikitos.hackingthebookshelf.data.data_sources.JWTDataSource
import kz.nikitos.hackingthebookshelf.data.data_sources.KtorAchievmentsDataSource
import kz.nikitos.hackingthebookshelf.data.data_sources.KtorBooksDataSource
import kz.nikitos.hackingthebookshelf.data.data_sources.KtorEventsDataSource
import kz.nikitos.hackingthebookshelf.data.data_sources.KtorTokenDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.AchievmentsDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.BooksDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.CalendarDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.EventsDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.LocalTokenDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.NotificationTokenStorage
import kz.nikitos.hackingthebookshelf.domain.data_sources.RemoteTokenDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.UserCredentialsDataSource

@Module
@InstallIn(SingletonComponent::class)
interface DataSourcesModule {
    @Binds
    fun getTokenDataSource(
        tokenDataSource: KtorTokenDataSource
    ): RemoteTokenDataSource

    @Binds
    fun getSettingsDataSource(
        JWTDataSource: JWTDataSource
    ): LocalTokenDataSource

    @Binds
    fun getUserCredentialsDataSource(
        dataStorageUserCredentialsDataSource: DataStorageUserCredentialsDataSource
    ): UserCredentialsDataSource

    @Binds
    @RealEventsDataSource
    fun getEventsDataSource(
        EventsDataSource: KtorEventsDataSource
    ): EventsDataSource

    @Binds
    @FakeEventsDataSource
    fun getFakeEventsDataSource(
        eventsDataSource: kz.nikitos.hackingthebookshelf.data.data_sources.FakeEventsDataSource
    ): EventsDataSource

    @Binds
    fun getNotificationTokenStorage(
        notificationTokenStorage: FirebaseNotificationTokenStorage
    ): NotificationTokenStorage

    @Binds
    fun getCalendarDataSource(
        calendarDataSource: IntentCalendarDataSource
    ): CalendarDataSource

    @Binds
    fun getBooksDataSource(
        booksDataSource: KtorBooksDataSource
    ): BooksDataSource

    @Binds
    fun getAchievmentsDataSource(
        achievmentsDataSource: KtorAchievmentsDataSource
    ): AchievmentsDataSource
}