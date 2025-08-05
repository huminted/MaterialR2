package cn.iwakeup.r2client.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import cn.iwakeup.r2client.APP_NAME
import okio.Path.Companion.toPath
import java.io.File

internal const val dataStoreFileName = "material_r2.preferences_pb"

object PreferencesDataStore {
    val instance = createDataStore()
}


private fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

private fun getDataStoreFile(): File {
    val appSupportDir = when {
        System.getProperty("os.name").startsWith("Mac") -> {
            File(System.getProperty("user.home"), "Library/Application Support/$APP_NAME")
        }

        System.getProperty("os.name").startsWith("Windows") -> {
            File(System.getenv("APPDATA"), APP_NAME)
        }

        else -> {
            File(System.getProperty("user.home"), ".local/share/$APP_NAME")
        }
    }

    appSupportDir.mkdirs()

    return appSupportDir

}

private fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        val file = File(getDataStoreFile(), dataStoreFileName)
        file.absolutePath
    }
)