package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ss_services_prefs")

data class StoredPreferences(
    val businessWhatsAppNumber: String = "",
    val savedCustomerName: String = "",
    val savedPhoneNumber: String = "",
    val savedAddress: String = "",
    val rememberDetails: Boolean = true
)

class PreferencesRepository(private val context: Context) {

    private object Keys {
        val BUSINESS_WA_NUMBER = stringPreferencesKey("business_wa_number")
        val CUSTOMER_NAME = stringPreferencesKey("customer_name")
        val CUSTOMER_PHONE = stringPreferencesKey("customer_phone")
        val CUSTOMER_ADDRESS = stringPreferencesKey("customer_address")
        val REMEMBER_DETAILS = booleanPreferencesKey("remember_details")
    }

    val preferencesFlow: Flow<StoredPreferences> = context.dataStore.data.map { prefs ->
        StoredPreferences(
            businessWhatsAppNumber = prefs[Keys.BUSINESS_WA_NUMBER].orEmpty(),
            savedCustomerName = prefs[Keys.CUSTOMER_NAME].orEmpty(),
            savedPhoneNumber = prefs[Keys.CUSTOMER_PHONE].orEmpty(),
            savedAddress = prefs[Keys.CUSTOMER_ADDRESS].orEmpty(),
            rememberDetails = prefs[Keys.REMEMBER_DETAILS] ?: true
        )
    }

    suspend fun saveBusinessWhatsAppNumber(rawNumber: String) {
        val normalized = rawNumber.filter { it.isDigit() }
        context.dataStore.edit { prefs ->
            prefs[Keys.BUSINESS_WA_NUMBER] = normalized
        }
    }

    suspend fun saveCustomerDetails(
        name: String,
        phone: String,
        address: String,
        remember: Boolean
    ) {
        context.dataStore.edit { prefs ->
            prefs[Keys.REMEMBER_DETAILS] = remember
            if (remember) {
                prefs[Keys.CUSTOMER_NAME] = name.trim()
                prefs[Keys.CUSTOMER_PHONE] = phone.trim()
                prefs[Keys.CUSTOMER_ADDRESS] = address.trim()
            } else {
                prefs.remove(Keys.CUSTOMER_NAME)
                prefs.remove(Keys.CUSTOMER_PHONE)
                prefs.remove(Keys.CUSTOMER_ADDRESS)
            }
        }
    }

    suspend fun clearCustomerDetails() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.CUSTOMER_NAME)
            prefs.remove(Keys.CUSTOMER_PHONE)
            prefs.remove(Keys.CUSTOMER_ADDRESS)
        }
    }
}
