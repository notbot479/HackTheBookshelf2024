package kz.nikitos.hackingthebookshelf.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kz.nikitos.hackingthebookshelf.data.repositories.MyJWTTokenRepository
import kz.nikitos.hackingthebookshelf.domain.repositories.JWTTokenRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {
    @Binds
    fun getTokenRepository(myTokenRepository: MyJWTTokenRepository): JWTTokenRepository
}