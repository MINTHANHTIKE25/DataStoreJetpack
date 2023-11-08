package com.minthanhtike.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "Users_Info")

class DataStoring(private val context: Context) {
    val isAlright=booleanPreferencesKey(DataStoreName.ISRIGHT.name)
    suspend fun storingBoolean(isRight: Boolean){
        context.dataStore.edit {
            preferences ->
            preferences[isAlright]=isRight
        }
    }

    val getIsRightFlow: Flow<Boolean?> = context.dataStore.data.map {
        preferences-> preferences[isAlright]
    }
    companion object {
        val EMAIL = stringPreferencesKey("EMAIL")
        val MOBILENUMBER = intPreferencesKey("MOBILENUMBERS")
        val ISDARK = booleanPreferencesKey("ISONDARK")
        val NAME = stringPreferencesKey("NAME")
    }

    suspend fun saveToDataStore(userData: UserData) {
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[EMAIL] = userData.emailAddress
            mutablePreferences[MOBILENUMBER] = userData.mobileNumber
            mutablePreferences[ISDARK] = userData.isDark
            mutablePreferences[NAME] = userData.name
        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        UserData(
            emailAddress = it[EMAIL] ?: "",
            mobileNumber = it[MOBILENUMBER] ?: 0,
            isDark = it[ISDARK] ?: false,
            name = it[NAME] ?: ""
        )
    }

    suspend fun clearDataStore() = context.dataStore.edit {
        it.clear()
    }
}

data class UserData(
    val emailAddress: String,
    val mobileNumber: Int,
    val isDark: Boolean,
    val name: String
)