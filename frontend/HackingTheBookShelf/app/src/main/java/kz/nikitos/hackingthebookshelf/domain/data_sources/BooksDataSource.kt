package kz.nikitos.hackingthebookshelf.domain.data_sources

import kz.nikitos.hackingthebookshelf.data.models.DebtData

interface BooksDataSource {
    suspend fun getDebtBooks(): List<DebtData>
}