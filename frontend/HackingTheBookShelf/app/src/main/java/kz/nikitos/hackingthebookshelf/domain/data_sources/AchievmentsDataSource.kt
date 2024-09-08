package kz.nikitos.hackingthebookshelf.domain.data_sources

import kz.nikitos.hackingthebookshelf.domain.models.Achievment

interface AchievmentsDataSource {
    suspend fun getAllAchievments(): List<Achievment>
}