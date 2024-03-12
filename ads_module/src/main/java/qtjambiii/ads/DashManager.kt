package qtjambiii.ads

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import org.joda.time.DateTime
import org.joda.time.Seconds

private val Context._dataStore: DataStore<Preferences> by preferencesDataStore("APP_PREFERENCES")

class DashManager(val context: Context) {

    val dataStore = context._dataStore

    //Create some keys
    companion object {
        val IS_ADS_ENABLED = intPreferencesKey("isEnabled")
        val ADS_INTERVAL = intPreferencesKey("i")
        val CLICK_HISTORY = intPreferencesKey("click")
        val LAST_TIME_INTERISTIAL_SHOWED = longPreferencesKey("lastTime")
        val IS_APP_FIRSTLAUNCH = booleanPreferencesKey("firtLaunch")
        val IS_APP_RATED = booleanPreferencesKey("rate")
    }

    suspend fun storeInterval(interval: Int) {
        dataStore.edit {
            it[ADS_INTERVAL] = interval
        }
    }

    suspend fun storeIsEnabled(isEnabled: Int) {
        dataStore.edit {
            it[IS_ADS_ENABLED] = isEnabled
        }
    }

    suspend fun getLastTime(): Long {
        return dataStore.data.first()[LAST_TIME_INTERISTIAL_SHOWED] ?: 0L
    }

    suspend fun setLastTime(time: Long) {
        dataStore.edit {
            it[LAST_TIME_INTERISTIAL_SHOWED] = time
        }
    }

    suspend fun getInterval(): Int {
        return dataStore.data.first()[ADS_INTERVAL] ?: 20
    }

    suspend fun getIsEnabled(): Int {
        return dataStore.data.first()[IS_ADS_ENABLED] ?: 1
    }

    suspend fun getShowInback(): Int {
        return dataStore.data.first()[ADS_INTERVAL]!!
    }

    suspend fun isFirtTime(): Boolean {
        val isFirstLaunch = dataStore.data.first()[IS_APP_FIRSTLAUNCH] ?: true
        if (isFirstLaunch) {
            dataStore.edit {
                it[IS_APP_FIRSTLAUNCH] = false
            }
            return true
        }
        return false
    }

    suspend fun isAppRated(): Boolean {
        val isRated = dataStore.data.first()[IS_APP_RATED] ?: false
        if (!isRated) {
            dataStore.edit {
                it[IS_APP_RATED] = true
            }
            return false
        }
        return isRated
    }

    suspend fun increaseClickHistory() {
        val clicks = dataStore.data.first()[CLICK_HISTORY] ?: 0
        dataStore.edit {
            it[CLICK_HISTORY] = clicks + 1
        }
    }

    suspend fun getClickHistory(): Int {
        return dataStore.data.first()[CLICK_HISTORY] ?: 0
    }

    suspend fun canShowInteristial(): Boolean {
        increaseClickHistory()
        var last = getLastTime()
        if (last.toInt() == 0 || Seconds.secondsBetween(
                DateTime(last),
                DateTime()
            ).seconds >= getInterval()
        ) {
            setLastTime(DateTime().getMillis())
            return true
        }

        return false
    }
}