package kz.nikitos.hackingthebookshelf.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kz.nikitos.hackingthebookshelf.data.data_sources.KtorTokenDataSource
import kz.nikitos.hackingthebookshelf.domain.data_sources.TokenDataSource

@Module
@InstallIn(SingletonComponent::class)
interface DataSourcesModule {
    @Binds
    fun getTokenDataSource(
        tokenDataSource: KtorTokenDataSource
    ): TokenDataSource
}