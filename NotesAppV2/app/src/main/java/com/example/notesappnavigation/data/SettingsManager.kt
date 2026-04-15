package com.example.notesappnavigation.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class SortOrder {
    DATE_DESC, DATE_ASC, TITLE_ASC
}

class SettingsManager(private val context: Context) {
    private val themeKey = booleanPreferencesKey("is_dark_theme")
    private val sortOrderKey = stringPreferencesKey("sort_order")

    val isDarkTheme: Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[themeKey]
    }

    val sortOrder: Flow<SortOrder> = context.dataStore.data.map { preferences ->
        val order = preferences[sortOrderKey]
        try {
            if (order != null) SortOrder.valueOf(order) else SortOrder.DATE_DESC
        } catch (e: IllegalArgumentException) {
            SortOrder.DATE_DESC
        }
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = isDark
        }
    }

    suspend fun setSortOrder(order: SortOrder) {
        context.dataStore.edit { preferences ->
            preferences[sortOrderKey] = order.name
        }
    }
}
