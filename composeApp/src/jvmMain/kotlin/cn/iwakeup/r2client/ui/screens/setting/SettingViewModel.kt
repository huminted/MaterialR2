package cn.iwakeup.r2client.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.iwakeup.r2client.data.APIConfiguration
import cn.iwakeup.r2client.data.PreferenceRepository
import cn.iwakeup.r2client.data.PreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {

    private val repository = PreferenceRepository(PreferencesDataStore.instance)

    fun saveAPIConfiguration(apiConfiguration: APIConfiguration) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAPIConfiguration(apiConfiguration)
        }
    }

    fun saveBucketPublicURL(bucketNameAndURL: List<Pair<String, String>>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveBucketPublicURL(bucketNameAndURL)
        }
    }
}