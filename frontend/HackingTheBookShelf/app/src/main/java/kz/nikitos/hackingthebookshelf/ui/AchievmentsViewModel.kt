package kz.nikitos.hackingthebookshelf.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.nikitos.hackingthebookshelf.domain.data_sources.AchievmentsDataSource
import kz.nikitos.hackingthebookshelf.domain.models.Achievment
import javax.inject.Inject

@HiltViewModel
class AchievmentsViewModel @Inject constructor(
    private val achievmentsDataSource: AchievmentsDataSource
) : ViewModel() {
    private val _achievments = MutableLiveData<List<Achievment>>()
    val achievments: LiveData<List<Achievment>> = _achievments

    fun getAchievments() {
        viewModelScope.launch {
            val reachedAchievements = achievmentsDataSource.getAllAchievments()
            Log.d(TAG, "getAchievments: $reachedAchievements")
            _achievments.postValue(reachedAchievements)
        }
    }

    companion object {
        private const val TAG = "Hack"
    }
}