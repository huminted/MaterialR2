package cn.iwakeup.r2client.ui.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.iwakeup.r2client.data.BucketBasicInfo
import cn.iwakeup.r2client.domain.SystemToolkit
import com.iwakeup.r2client.api.R2Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import software.amazon.awssdk.services.s3.model.S3Object

sealed interface SearchUIState {
    object Init : SearchUIState
    object Loading : SearchUIState
    data class Success(val list: List<S3Object>, val enableCopyLink: Boolean) : SearchUIState

}

class SearchViewModel(defaultSelectedBucket: BucketBasicInfo) : ViewModel() {

    private val _uiSate: MutableStateFlow<SearchUIState> = MutableStateFlow(SearchUIState.Init)
    val uiSate: StateFlow<SearchUIState> = _uiSate

    private val _selectedBucket = mutableStateOf(defaultSelectedBucket)


    fun setSelectedBucket(selectedBucket: BucketBasicInfo) {
        _selectedBucket.value = selectedBucket
    }

    fun search(searchQuery: String) {
        if (searchQuery.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _uiSate.emit(SearchUIState.Loading)
            val searchResult = R2Client.get().searchObject(_selectedBucket.value.bucketName, searchQuery)
            _uiSate.emit(SearchUIState.Success(searchResult, !_selectedBucket.value.publicURL.isNullOrEmpty()))

        }
    }

    fun copyObjectLink(key: String) {
        viewModelScope.launch {
            _selectedBucket.value.publicURL?.let {
                SystemToolkit.copyObjectLink(it, key)
            }

        }
    }

    fun resetSearch() {
        viewModelScope.launch {
            _uiSate.emit(SearchUIState.Init)
        }
    }

}